package com.westerosblocks.datagen.models;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.datagen.ModelExport;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class HalfDoorBlockModelHandler extends ModelExport {
    private final BlockStateModelGenerator generator;
    private final Block block;
    private final WesterosBlockDef def;

    private static class ModelRec {
        String cond;
        String ext;
        int y;
        ModelRec(String c, String e, int y) {
            cond = c; ext = e; this.y = y;
        }
    }

    // Model definitions for half door variants
    private static final ModelRec[] MODELS = {
            // EAST facing
            new ModelRec("facing=east,hinge=left,open=false", "bottom_left", 0),
            new ModelRec("facing=east,hinge=left,open=true", "bottom_left_open", 90),
            new ModelRec("facing=east,hinge=right,open=false", "bottom_right", 0),
            new ModelRec("facing=east,hinge=right,open=true", "bottom_right_open", 270),

            // SOUTH facing
            new ModelRec("facing=south,hinge=left,open=false", "bottom_left", 90),
            new ModelRec("facing=south,hinge=left,open=true", "bottom_left_open", 180),
            new ModelRec("facing=south,hinge=right,open=false", "bottom_right", 90),
            new ModelRec("facing=south,hinge=right,open=true", "bottom_right_open", 0),

            // WEST facing
            new ModelRec("facing=west,hinge=left,open=false", "bottom_left", 180),
            new ModelRec("facing=west,hinge=left,open=true", "bottom_left_open", 270),
            new ModelRec("facing=west,hinge=right,open=false", "bottom_right", 180),
            new ModelRec("facing=west,hinge=right,open=true", "bottom_right_open", 90),

            // NORTH facing
            new ModelRec("facing=north,hinge=left,open=false", "bottom_left", 270),
            new ModelRec("facing=north,hinge=left,open=true", "bottom_left_open", 0),
            new ModelRec("facing=north,hinge=right,open=false", "bottom_right", 270),
            new ModelRec("facing=north,hinge=right,open=true", "bottom_right_open", 180)
    };

    public HalfDoorBlockModelHandler(BlockStateModelGenerator generator, Block block, WesterosBlockDef def) {
        super(generator, block, def);
        this.generator = generator;
        this.block = block;
        this.def = def;
    }

    public void generateBlockStateModels() {
        if (!def.isCustomModel()) {
            for (int setIdx = 0; setIdx < def.getRandomTextureSetCount(); setIdx++) {
                WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setIdx);
                generateHalfDoorModels(generator, set);
            }
        }

        BlockStateBuilder blockStateBuilder = new BlockStateBuilder(block);
        final Map<String, List<BlockStateVariant>> variants = blockStateBuilder.getVariants();

        for (ModelRec rec : MODELS) {
            BlockStateVariant variant = BlockStateVariant.create();
            Identifier modelId = getModelId(rec.ext, def.isCustomModel());
            variant.put(VariantSettings.MODEL, modelId);

            if (rec.y != 0) {
                variant.put(VariantSettings.Y, getRotation(rec.y));
            }

            blockStateBuilder.addVariant(rec.cond, variant, null, variants);
        }

        generateBlockStateFiles(generator, block, variants);
    }

    private void generateHalfDoorModels(BlockStateModelGenerator generator, WesterosBlockDef.RandomTextureSet set) {
        TextureMap textureMap = new TextureMap()
                .put(TextureKey.TOP, createBlockIdentifier(set.getTextureByIndex(0)))
                .put(TextureKey.BOTTOM, createBlockIdentifier(set.getTextureByIndex(1)));

        // Generate models for each door variant
        String[][] variants = {
                {"bottom_left", "door_bottom_left"},
                {"bottom_right", "door_bottom_right"},
                {"bottom_left_open", "door_bottom_left_open"},
                {"bottom_right_open", "door_bottom_right_open"}
        };



        for (String[] variant : variants) {
            String modelName = variant[0];
            String parentPath = "block/" + variant[1];

            Identifier modelId = getModelId(modelName, false);
            Model doorModel = new Model(
                    Optional.of(Identifier.ofVanilla(parentPath)),
                    Optional.empty(),
                    TextureKey.TOP,
                    TextureKey.BOTTOM
            );
            doorModel.upload(modelId, textureMap, generator.modelCollector);
        }
    }

    private Identifier getModelId(String variant, boolean isCustom) {
        return Identifier.of(WesterosBlocks.MOD_ID,
                String.format("%s%s/%s_v1",
                        isCustom ? CUSTOM_PATH : GENERATED_PATH,
                        def.getBlockName(),
                        variant));
    }

    public static void generateItemModels(ItemModelGenerator itemModelGenerator, Block block, WesterosBlockDef blockDefinition) {
        WesterosBlockDef.RandomTextureSet firstSet = blockDefinition.getRandomTextureSet(0);
        TextureMap textureMap = TextureMap.layer0(createBlockIdentifier(firstSet.getTextureByIndex(0)));

        Models.GENERATED.upload(
                ModelIds.getItemModelId(block.asItem()),
                textureMap,
                itemModelGenerator.writer
        );
    }
}