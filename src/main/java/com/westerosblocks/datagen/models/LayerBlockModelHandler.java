package com.westerosblocks.datagen.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.custom.WCLayerBlock;
import com.westerosblocks.datagen.ModelExport;
import net.minecraft.block.Block;
import net.minecraft.client.gui.Element;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class LayerBlockModelHandler extends ModelExport {
    private final BlockStateModelGenerator generator;
    private final Block block;
    private final WesterosBlockDef def;
    private final WCLayerBlock layerBlock;

    public LayerBlockModelHandler(BlockStateModelGenerator generator, Block block, WesterosBlockDef def) {
        super(generator, block, def);
        this.generator = generator;
        this.block = block;
        this.def = def;
        this.layerBlock = (WCLayerBlock) block;
    }

    public void generateBlockStateModels() {
        BlockStateBuilder blockStateBuilder = new BlockStateBuilder(block);
        final Map<String, List<BlockStateVariant>> variants = blockStateBuilder.getVariants();

        // Generate variants for each layer
        for (int layer = 0; layer < layerBlock.layerCount; layer++) {
            for (int setIdx = 0; setIdx < def.getRandomTextureSetCount(); setIdx++) {
                WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setIdx);

                BlockStateVariant variant = BlockStateVariant.create();
                Identifier modelId = getModelId(layer + 1, setIdx);
                variant.put(VariantSettings.MODEL, modelId);
                if (set.weight != null) {
                    variant.put(VariantSettings.WEIGHT, set.weight);
                }
                blockStateBuilder.addVariant("layers=" + (layer + 1), variant, null, variants);

                if (!def.isCustomModel()) {
                    generateLayerModel(generator, layer + 1, set, setIdx);
                }
            }
        }

        generateBlockStateFiles(generator, block, variants);
    }

    private void generateLayerModel(BlockStateModelGenerator generator,
                                    int layer,
                                    WesterosBlockDef.RandomTextureSet set,
                                    int setIdx) {
        boolean isTinted = def.isTinted();
        float yMax = (16.0f / layerBlock.layerCount) * layer;  // Calculate height based on layer number

        // Create custom model that supports elements
        Model layerModel = new Model(
                Optional.empty(),
                Optional.empty(),
                TextureKey.PARTICLE,
                ModTextureKey.TEXTURE_0,
                ModTextureKey.TEXTURE_1,
                ModTextureKey.TEXTURE_2,
                ModTextureKey.TEXTURE_3,
                ModTextureKey.TEXTURE_4,
                ModTextureKey.TEXTURE_5) {
            @Override
            public JsonObject createJson(Identifier id, Map<TextureKey, Identifier> textures) {
                JsonObject json = super.createJson(id, textures);

                // Add elements array
                JsonArray elements = new JsonArray();
                JsonObject element = new JsonObject();

                // From coordinates (always start at 0)
                JsonArray from = new JsonArray();
                from.add(0);
                from.add(0);
                from.add(0);
                element.add("from", from);

                // To coordinates (height varies by layer)
                JsonArray to = new JsonArray();
                to.add(16);
                to.add(yMax);  // Variable height based on layer
                to.add(16);
                element.add("to", to);

                // Add faces
                JsonObject faces = new JsonObject();

                // Down face
                faces.add("down", createFace(0, 0, 16, 16, "#txt0", "down", isTinted));

                // Up face
                faces.add("up", createFace(0, 0, 16, 16, "#txt1", yMax >= 16 ? "up" : null, isTinted));

                // Side faces - UV coordinates are adjusted for height
                faces.add("north", createFace(0, 16 - yMax, 16, 16, "#txt2", "north", isTinted));
                faces.add("south", createFace(0, 16 - yMax, 16, 16, "#txt3", "south", isTinted));
                faces.add("west", createFace(0, 16 - yMax, 16, 16, "#txt4", "west", isTinted));
                faces.add("east", createFace(0, 16 - yMax, 16, 16, "#txt5", "east", isTinted));

                element.add("faces", faces);
                elements.add(element);
                json.add("elements", elements);

                return json;
            }
        };

        TextureMap textureMap = ModTextureMap.customTxtN(set);

        layerModel.upload(getModelId(layer, setIdx), textureMap, generator.modelCollector);
    }

    private JsonObject createFace(float uMin, float vMin, float uMax, float vMax,
                                  String texture, String cullface, boolean tinted) {
        JsonObject face = new JsonObject();

        // UV coordinates
        JsonArray uv = new JsonArray();
        uv.add(uMin);
        uv.add(vMin);
        uv.add(uMax);
        uv.add(vMax);
        face.add("uv", uv);

        // Texture reference
        face.addProperty("texture", texture);

        // Cullface if specified
        if (cullface != null) {
            face.addProperty("cullface", cullface);
        }

        // Tint index if tinted
        if (tinted) {
            face.addProperty("tintindex", 0);
        }

        return face;
    }

    private Identifier getModelId(int layer, int setIdx) {
        return Identifier.of(WesterosBlocks.MOD_ID,
                String.format("%s%s/layer%d_v%d",
                        GENERATED_PATH,
                        def.getBlockName(),
                        layer,
                        setIdx + 1));
    }

    // TODO need to figure out a diff model for in-game GUI
    public static void generateItemModels(ItemModelGenerator itemModelGenerator,
                                          Block currentBlock,
                                          WesterosBlockDef blockDefinition) {
        String path = String.format("%s%s/layer1_v1", GENERATED_PATH, blockDefinition.blockName);

        itemModelGenerator.register(
                currentBlock.asItem(),
                new Model(Optional.of(Identifier.of(WesterosBlocks.MOD_ID, path)),
                        Optional.empty())
        );

        if (blockDefinition.isTinted()) {
            String tintResource = blockDefinition.getBlockColorMapResource();
            if (tintResource != null) {
                // TODO: Handle tinting registration for items
            }
        }
    }
}
