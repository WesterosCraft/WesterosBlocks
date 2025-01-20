package com.westerosblocks.datagen.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.datagen.ModelExport;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CakeBlockModelHandler extends ModelExport {
    private final BlockStateModelGenerator generator;
    private final Block block;
    private final WesterosBlockDef def;

    public CakeBlockModelHandler(BlockStateModelGenerator generator, Block block, WesterosBlockDef def) {
        super(generator, block, def);
        this.generator = generator;
        this.block = block;
        this.def = def;
    }

    public void generateBlockStateModels() {
        BlockStateBuilder blockStateBuilder = new BlockStateBuilder(block);
        final Map<String, List<BlockStateVariant>> variants = blockStateBuilder.getVariants();

        // Generate models for all 7 bite states (0-6)
        if (!def.isCustomModel()) {
            generateCakeModels(generator);
        }

        // Generate variants for each bite state
        for (int bites = 0; bites < 7; bites++) {
            BlockStateVariant variant = BlockStateVariant.create();
            String modelName = bites == 0 ? "uneaten" : "slice" + bites;
            Identifier modelId = getModelId(modelName, 0, def.isCustomModel());
            variant.put(VariantSettings.MODEL, modelId);

            blockStateBuilder.addVariant("bites=" + bites, variant, null, variants);
        }

        generateBlockStateFiles(generator, block, variants);
    }

    private void generateCakeModels(BlockStateModelGenerator generator) {
        // Create textures map
        TextureMap baseTextureMap = new TextureMap()
                .put(TextureKey.BOTTOM, createBlockIdentifier(def.getTextureByIndex(0)))
                .put(TextureKey.TOP, createBlockIdentifier(def.getTextureByIndex(1)))
                .put(TextureKey.SIDE, createBlockIdentifier(def.getTextureByIndex(2)))
                .put(ModTextureKey.INSIDE, createBlockIdentifier(def.getTextureByIndex(3)))
                .put(TextureKey.PARTICLE, createBlockIdentifier(def.getTextureByIndex(2))); // Side texture for particle

        // Generate models for each bite state
        for (int bites = 0; bites < 7; bites++) {
            String modelName = bites == 0 ? "uneaten" : "slice" + bites;
            Identifier modelId = getModelId(modelName, 0, false);

            // Create cake model with specific bite state
            Model cakeModel = createCakeModel(bites);
            cakeModel.upload(modelId, baseTextureMap, generator.modelCollector);
        }
    }

    private Model createCakeModel(int bites) {
        return new Model(
                Optional.of(Identifier.ofVanilla("block/thin_block")),
                Optional.empty(),
                TextureKey.PARTICLE,
                TextureKey.BOTTOM,
                TextureKey.TOP,
                TextureKey.SIDE,
                ModTextureKey.INSIDE
        ) {
            @Override
            public JsonObject createJson(Identifier id, Map<TextureKey, Identifier> textures) {
                JsonObject json = super.createJson(id, textures);

                // Create elements array
                JsonArray elements = new JsonArray();
                JsonObject element = new JsonObject();

                // Set dimensions based on bite state
                int xMin = 1 + (2 * bites);
                JsonArray from = new JsonArray();
                from.add(xMin);
                from.add(0);
                from.add(1);
                element.add("from", from);

                JsonArray to = new JsonArray();
                to.add(15);
                to.add(8);
                to.add(15);
                element.add("to", to);

                // Add faces
                JsonObject faces = new JsonObject();

                // Bottom face
                JsonObject bottomFace = new JsonObject();
                bottomFace.addProperty("texture", "#bottom");
                bottomFace.addProperty("cullface", "down");
                faces.add("down", bottomFace);

                // Top face
                JsonObject topFace = new JsonObject();
                topFace.addProperty("texture", "#top");
                faces.add("up", topFace);

                // Side faces
                JsonObject northFace = new JsonObject();
                northFace.addProperty("texture", "#side");
                faces.add("north", northFace);

                JsonObject southFace = new JsonObject();
                southFace.addProperty("texture", "#side");
                faces.add("south", southFace);

                // West face (shows inside texture when bitten)
                JsonObject westFace = new JsonObject();
                westFace.addProperty("texture", bites == 0 ? "#side" : "#inside");
                faces.add("west", westFace);

                // East face
                JsonObject eastFace = new JsonObject();
                eastFace.addProperty("texture", "#side");
                faces.add("east", eastFace);

                element.add("faces", faces);
                elements.add(element);
                json.add("elements", elements);

                return json;
            }
        };
    }

    private Identifier getModelId(String variant, int setIdx, boolean isCustom) {
        return Identifier.of(WesterosBlocks.MOD_ID,
                String.format("%s%s/%s_v%d",
                        isCustom ? CUSTOM_PATH : GENERATED_PATH,
                        def.getBlockName(),
                        variant,
                        setIdx + 1));
    }

    public static void generateItemModels(ItemModelGenerator itemModelGenerator, Block block, WesterosBlockDef blockDefinition) {
        // Use the uneaten cake model for the item model
        String path = String.format("%s%s/uneaten_v1",
                GENERATED_PATH,
                blockDefinition.getBlockName());

        itemModelGenerator.register(
                block.asItem(),
                new Model(Optional.of(Identifier.of(WesterosBlocks.MOD_ID, path)),
                        Optional.empty())
        );
    }
}
