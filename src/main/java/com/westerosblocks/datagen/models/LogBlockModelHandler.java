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

public class LogBlockModelHandler extends ModelExport {
    private final BlockStateModelGenerator generator;
    private final Block block;
    private final WesterosBlockDef def;

    public LogBlockModelHandler(BlockStateModelGenerator generator, Block currentBlock, WesterosBlockDef customBlockDef) {
        super(generator, currentBlock, customBlockDef);
        this.generator = generator;
        this.block = currentBlock;
        this.def = customBlockDef;
    }

    private static final String[] states = {"axis=x", "axis=y", "axis=z"};
    private static final int[] xrot = {90, 0, 90};
    private static final int[] yrot = {90, 0, 0};
    private static final String[] models = {"x", "y", "z"};

    public void generateBlockStateModels() {
        BlockStateBuilder blockStateBuilder = new BlockStateBuilder(block);
        final Map<String, List<BlockStateVariant>> variants = blockStateBuilder.getVariants();

        for (int i = 0; i < states.length; i++) {
            for (int setIdx = 0; setIdx < def.getRandomTextureSetCount(); setIdx++) {
                WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setIdx);

                BlockStateVariant variant = BlockStateVariant.create();
                Identifier symId = Identifier.of(WesterosBlocks.MOD_ID, GENERATED_PATH + getModelName(models[i], setIdx));
                variant.put(VariantSettings.MODEL, symId);
                if (set.weight != null) {
                    variant.put(VariantSettings.WEIGHT, set.weight);
                }
                if (xrot[i] > 0) {
                    variant.put(VariantSettings.X, getRotation(xrot[i]));
                }

                if (yrot[i] != 0) {
                    variant.put(VariantSettings.Y, getRotation(yrot[i]));
                }
                blockStateBuilder.addVariant(states[i], variant, null, variants);
            }
        }

        generateBlockStateFiles(generator, block, variants);

        for (int setIdx = 0; setIdx < def.getRandomTextureSetCount(); setIdx++) {
            WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setIdx);
            // make models
            String[] types = {"cube_log_horizontal", "cube_log", "cube_log_horizontal"};
            for (int i = 0; i < models.length; i++) {
                generateLogModel(generator, types[i], set, def.isTinted(), setIdx, models[i]);
            }
        }
    }

    public void generateLogModel(BlockStateModelGenerator generator, String type, WesterosBlockDef.RandomTextureSet set, boolean isTinted, int setIdx, String modelSuffix) {
        TextureMap textureMap = ModTextureMap.frontTopSides(set, null, null, null);
        Identifier modelId = Identifier.of(WesterosBlocks.MOD_ID,
                String.format("%s%s/%s_v%s", GENERATED_PATH, def.blockName, modelSuffix, setIdx + 1));

        String parentPath = getParentPath(isTinted, type);
        Model model = ModModels.ALL_SIDES(parentPath, null);
        model.upload(modelId, textureMap, generator.modelCollector);
    }

    private static String getParentPath(boolean isTinted, String type) {
        String basePath;
        if (isTinted) {
            basePath = "tinted/";

        } else {
            basePath = "untinted/";
        }
        return basePath + type;
    }


    public static void generateItemModels(ItemModelGenerator itemModelGenerator, Block currentBlock, WesterosBlockDef blockDefinition) {
        itemModelGenerator.register(
                currentBlock.asItem(),
                new Model(Optional.of(Identifier.of(WesterosBlocks.MOD_ID, GENERATED_PATH + blockDefinition.blockName + "/" + models[1] + "_v1")), Optional.empty())
        );
    }
}
