package com.westerosblocks.datagen.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.datagen.ModModels;
import com.westerosblocks.datagen.ModTextureMap;
import net.minecraft.data.client.*;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class LeavesBlockExporter extends BaseBlockExporter {

    private static Model getLeavesModel(boolean tinted, boolean overlay) {
        if (overlay) return tinted ? ModModels.LEAVES_OVERLAY_TINTED : ModModels.LEAVES_OVERLAY_UNTINTED;
        return tinted ? ModModels.LEAVES_TINTED : ModModels.LEAVES_UNTINTED;
    }

    private static final Model[][] BF_MODELS = {
        { ModModels.LEAVES_BF1_UNTINTED, ModModels.LEAVES_BF1_TINTED, ModModels.LEAVES_BF1_OVERLAY_UNTINTED, ModModels.LEAVES_BF1_OVERLAY_TINTED },
        { ModModels.LEAVES_BF2_UNTINTED, ModModels.LEAVES_BF2_TINTED, ModModels.LEAVES_BF2_OVERLAY_UNTINTED, ModModels.LEAVES_BF2_OVERLAY_TINTED },
        { ModModels.LEAVES_BF3_UNTINTED, ModModels.LEAVES_BF3_TINTED, ModModels.LEAVES_BF3_OVERLAY_UNTINTED, ModModels.LEAVES_BF3_OVERLAY_TINTED },
    };

    private static Model getLeavesBetterFoliageModel(boolean tinted, boolean overlay, int variant) {
        int idx = (tinted ? 1 : 0) + (overlay ? 2 : 0);
        return BF_MODELS[variant - 1][idx];
    }

    private static TextureMap createStandardLeavesTextureMap(String[] textures, boolean hasOverlay) {
        if (hasOverlay && textures.length >= 4) {
            return ModTextureMap.leavesOverlayTextures(textures[0], textures[1], textures[2], textures[3]);
        }
        return ModTextureMap.leavesTextures(textures[0], textures[1]);
    }

    private static TextureMap createBetterFoliageTextureMap(String[] textures, boolean hasOverlay) {
        if (hasOverlay && textures.length >= 3) {
            return ModTextureMap.leavesBetterFoliageOverlayTextures(textures[0], textures[1], textures[2]);
        }
        return ModTextureMap.leavesBetterFoliageTextures(textures[0]);
    }

    public static void registerLeavesBlock(BlockStateModelGenerator generator, Block block, boolean tinted,
                                          boolean overlay, boolean betterFoliage, boolean rotateRandom, String[] textures) {
        List<Identifier> modelIds = new ArrayList<>();

        if (betterFoliage) {
            TextureMap textureMap = createBetterFoliageTextureMap(textures, overlay);
            for (int i = 1; i <= 3; i++) {
                Identifier modelId = getLeavesBetterFoliageModel(tinted, overlay, i)
                        .upload(createNestedModelId(block, "bf" + i), textureMap, generator.modelCollector);
                modelIds.add(modelId);
            }
        } else {
            TextureMap textureMap = createStandardLeavesTextureMap(textures, overlay);
            Identifier modelId = getLeavesModel(tinted, overlay)
                    .upload(createNestedModelId(block, "base"), textureMap, generator.modelCollector);
            modelIds.add(modelId);
        }

        // Create blockstate with rotations
        VariantsBlockStateSupplier blockstate = createRotatedVariantsBlockState(block, modelIds, null, rotateRandom);
        generator.blockStateCollector.accept(blockstate);
        generator.registerParentedItemModel(block, modelIds.get(0));
    }

    public static void registerLeavesBlockWithRandomTextures(BlockStateModelGenerator generator, Block block, boolean tinted,
                                                             boolean overlay, boolean betterFoliage, boolean rotateRandom,
                                                             List<BlockDefinition.TextureVariantSet> textureSets) {
        List<Identifier> modelIds = new ArrayList<>();
        List<Integer> weights = new ArrayList<>();

        for (int setIdx = 0; setIdx < textureSets.size(); setIdx++) {
            BlockDefinition.TextureVariantSet set = textureSets.get(setIdx);

            if (betterFoliage) {
                TextureMap textureMap = createBetterFoliageTextureMap(set.getTexturesAsArray(), overlay);
                for (int bfIdx = 1; bfIdx <= 3; bfIdx++) {
                    Identifier modelId = getLeavesBetterFoliageModel(tinted, overlay, bfIdx)
                            .upload(createNestedModelId(block, "bf" + bfIdx + "_v" + (setIdx + 1)), textureMap, generator.modelCollector);
                    modelIds.add(modelId);
                    weights.add(set.weight);
                }
            } else {
                TextureMap textureMap = createStandardLeavesTextureMap(set.getTexturesAsArray(), overlay);
                Identifier modelId = getLeavesModel(tinted, overlay)
                        .upload(createNestedModelId(block, "base_v" + (setIdx + 1)), textureMap, generator.modelCollector);
                modelIds.add(modelId);
                weights.add(set.weight);
            }
        }

        VariantsBlockStateSupplier blockstate = createRotatedVariantsBlockState(block, modelIds, weights, rotateRandom);
        generator.blockStateCollector.accept(blockstate);
        generator.registerParentedItemModel(block, modelIds.get(0));
    }

    public static void registerCustomLeavesBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        boolean tinted = definition.isTinted() || definition.hasColorMult();
        boolean overlay = definition.hasOverlay();
        boolean betterFoliage = definition.hasBetterFoliage();
        boolean rotateRandom = definition.hasRotateRandom();

        if (definition.hasRandomTextures()) {
            List<BlockDefinition.TextureVariantSet> textureSets = new ArrayList<>();
            for (BlockDefinition.RandomTextureVariant randomTexture : definition.getRandomTextures()) {
                List<String> textures = randomTexture.getTextures();
                if (textures != null && !textures.isEmpty()) {
                    textureSets.add(new BlockDefinition.TextureVariantSet(textures.toArray(new String[0]), randomTexture.getWeight()));
                }
            }
            registerLeavesBlockWithRandomTextures(generator, block, tinted, overlay, betterFoliage, rotateRandom, textureSets);
        } else if (definition.getTextures() != null && !definition.getTextures().isEmpty()) {
            String[] textures = definition.getTextures().toArray(new String[0]);
            registerLeavesBlock(generator, block, tinted, overlay, betterFoliage, rotateRandom, textures);
        } else {
            WesterosBlocks.LOGGER.warn("No textures defined for leaves block: {}", definition.getBlockName());
        }
    }
}
