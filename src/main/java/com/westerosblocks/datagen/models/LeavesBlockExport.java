package com.westerosblocks.datagen.models;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.ModBlock;
import com.westerosblocks.block.custom.WCLeavesBlock;
import com.westerosblocks.datagen.ModelExport;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

import java.util.*;

public class LeavesBlockExport extends ModelExport {
    private final BlockStateModelGenerator generator;
    private final Block block;
    private final ModBlock def;

    public LeavesBlockExport(BlockStateModelGenerator generator, Block block, ModBlock def) {
        super(generator, block, def);
        this.generator = generator;
        this.block = block;
        this.def = def;
    }

    public void generateBlockStateModels() {
        WCLeavesBlock leavesBlock = (block instanceof WCLeavesBlock) ? (WCLeavesBlock) block : null;
        boolean isBetterFoliage = leavesBlock != null && leavesBlock.betterfoliage;
        BlockStateBuilder blockStateBuilder = new BlockStateBuilder(block);
        final Map<String, List<BlockStateVariant>> variants = blockStateBuilder.getVariants();

        for (int setIdx = 0; setIdx < def.getRandomTextureSetCount(); setIdx++) {
            ModBlock.RandomTextureSet set = def.getRandomTextureSet(setIdx);
            int rotationCount = def.rotateRandom ? 4 : 1;

            if (isBetterFoliage) {
                addLeafVariants(blockStateBuilder, variants, set, rotationCount, "bf1", setIdx);
                addLeafVariants(blockStateBuilder, variants, set, rotationCount, "bf2", setIdx);
                addLeafVariants(blockStateBuilder, variants, set, rotationCount, "bf3", setIdx);

                generateLeafModel(generator, "bf1", set, setIdx, true, true);
                generateLeafModel(generator, "bf2", set, setIdx, true, true);
                generateLeafModel(generator, "bf3", set, setIdx, true, true);
            } else {
                addLeafVariants(blockStateBuilder, variants, set, rotationCount, "base", setIdx);
                generateLeafModel(generator, "base", set, setIdx, false, false);
            }


        }
        generateBlockStateFiles(generator, block, variants);
    }

    private void addLeafVariants(BlockStateBuilder blockStateBuilder, Map<String, List<BlockStateVariant>> variants, ModBlock.RandomTextureSet set,
                                 int rotationCount, String ext, int setIdx) {
        for (int i = 0; i < rotationCount; i++) {
            BlockStateVariant variant = BlockStateVariant.create();
            Identifier modelId = getModelId(ext, setIdx);
            variant.put(VariantSettings.MODEL, modelId);
            if (set.weight != null) {
                variant.put(VariantSettings.WEIGHT, set.weight);
            }
            if (i > 0) {
                variant.put(VariantSettings.Y, getRotation(90 * i));
            }
            blockStateBuilder.addVariant("", variant, null, variants);
        }
    }

    private void generateLeafModel(BlockStateModelGenerator generator, String type, ModBlock.RandomTextureSet set, int setIdx, boolean isBetterFoliage, boolean hasOverlay) {
        TextureMap textureMap = ModTextureMap.leaves(set, hasOverlay);
        String parentPath = createParentPath(isBetterFoliage, def.isTinted(), hasOverlay, type, setIdx);
        Identifier modelId = getModelId(type, setIdx);

        Model model = isBetterFoliage ? ModModels.BETTER_FOLIAGE(parentPath, def) : Models.LEAVES;
        model.upload(modelId, textureMap, generator.modelCollector);
    }

    private String createParentPath(boolean isBetterFoliage, boolean isTinted, boolean hasOverlay, String type, int setIdx) {
        String basePath = isTinted ? "tinted/" : "untinted/";
        String modelType = hasOverlay ? "leaves_overlay" : "leaves";

        return basePath + modelType + (isBetterFoliage ? "_" + type : "");
    }

    private Identifier getModelId(String modelType, int setIdx) {
        return Identifier.of(WesterosBlocks.MOD_ID,
                String.format("%s%s/%s-v%d", GENERATED_PATH, def.getBlockName(), modelType, setIdx + 1));
    }

    public static void generateItemModels(ItemModelGenerator itemModelGenerator, Block currentBlock, ModBlock blockDefinition) {
        generateBasicItemModel(itemModelGenerator, currentBlock, blockDefinition);
    }
}
