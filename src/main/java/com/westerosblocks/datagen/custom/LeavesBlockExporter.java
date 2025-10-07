package com.westerosblocks.datagen.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.datagen.ModTextureKey;
import net.minecraft.data.client.*;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LeavesBlockExporter extends BaseBlockExporter {

    private static Model createLeavesModel(boolean tinted, boolean overlay) {
        String tintPath = tinted ? "block/tinted/" : "block/untinted/";
        String overlayPath = overlay ? "leaves_overlay" : "leaves";
        String path = tintPath + overlayPath;

        if (overlay) {
            return new Model(Optional.of(WesterosBlocks.id(path)), Optional.empty(),
                TextureKey.END, TextureKey.SIDE, ModTextureKey.LEAVES_OVERLAY_END, ModTextureKey.LEAVES_OVERLAY_SIDE, TextureKey.PARTICLE);
        } else {
            return new Model(Optional.of(WesterosBlocks.id(path)), Optional.empty(),
                TextureKey.END, TextureKey.SIDE, TextureKey.PARTICLE);
        }
    }

    private static Model createLeavesBetterFoliageModel(boolean tinted, boolean overlay, int variant) {
        String tintPath = tinted ? "block/tinted/" : "block/untinted/";
        String overlayPath = overlay ? "leaves_overlay_bf" : "leaves_bf";
        String path = tintPath + overlayPath + variant;

        if (overlay) {
            return new Model(Optional.of(WesterosBlocks.id(path)), Optional.empty(),
                TextureKey.ALL, ModTextureKey.LEAVES_OVERLAY_END, ModTextureKey.LEAVES_OVERLAY_SIDE, TextureKey.PARTICLE);
        } else {
            return new Model(Optional.of(WesterosBlocks.id(path)), Optional.empty(),
                TextureKey.ALL, TextureKey.PARTICLE);
        }
    }

    private static TextureMap createStandardLeavesTextureMap(String[] textures, boolean hasOverlay) {
        TextureMap textureMap = new TextureMap()
                .put(TextureKey.END, createBlockIdentifier(textures[0]))
                .put(TextureKey.SIDE, createBlockIdentifier(textures[1]))
                .put(TextureKey.PARTICLE, createBlockIdentifier(textures[1]));

        if (hasOverlay && textures.length >= 4) {
            textureMap.put(ModTextureKey.LEAVES_OVERLAY_END, createBlockIdentifier(textures[2]));
            textureMap.put(ModTextureKey.LEAVES_OVERLAY_SIDE, createBlockIdentifier(textures[3]));
        }

        return textureMap;
    }

    private static TextureMap createBetterFoliageTextureMap(String[] textures, boolean hasOverlay) {
        TextureMap textureMap = new TextureMap()
                .put(TextureKey.ALL, createBlockIdentifier(textures[0]))
                .put(TextureKey.PARTICLE, createBlockIdentifier(textures[0]));

        if (hasOverlay && textures.length >= 3) {
            textureMap.put(ModTextureKey.LEAVES_OVERLAY_END, createBlockIdentifier(textures[1]));
            textureMap.put(ModTextureKey.LEAVES_OVERLAY_SIDE, createBlockIdentifier(textures[2]));
        }

        return textureMap;
    }

    public static void registerLeavesBlock(BlockStateModelGenerator generator, Block block, boolean tinted,
                                          boolean overlay, boolean betterFoliage, boolean rotateRandom, String[] textures) {
        List<Identifier> modelIds = new ArrayList<>();

        if (betterFoliage) {
            TextureMap textureMap = createBetterFoliageTextureMap(textures, overlay);
            for (int i = 1; i <= 3; i++) {
                Identifier modelId = createLeavesBetterFoliageModel(tinted, overlay, i)
                        .upload(createNestedModelId(block, "bf" + i), textureMap, generator.modelCollector);
                modelIds.add(modelId);
            }
        } else {
            TextureMap textureMap = createStandardLeavesTextureMap(textures, overlay);
            Identifier modelId = createLeavesModel(tinted, overlay)
                    .upload(createNestedModelId(block, "base"), textureMap, generator.modelCollector);
            modelIds.add(modelId);
        }

        // Create blockstate with rotations
        VariantsBlockStateSupplier blockstate = createLeavesBlockstate(block, modelIds, rotateRandom, null);
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
                    Identifier modelId = createLeavesBetterFoliageModel(tinted, overlay, bfIdx)
                            .upload(createNestedModelId(block, "bf" + bfIdx + "_v" + (setIdx + 1)), textureMap, generator.modelCollector);
                    modelIds.add(modelId);
                    weights.add(set.weight);
                }
            } else {
                TextureMap textureMap = createStandardLeavesTextureMap(set.getTexturesAsArray(), overlay);
                Identifier modelId = createLeavesModel(tinted, overlay)
                        .upload(createNestedModelId(block, "base_v" + (setIdx + 1)), textureMap, generator.modelCollector);
                modelIds.add(modelId);
                weights.add(set.weight);
            }
        }

        VariantsBlockStateSupplier blockstate = createLeavesBlockstate(block, modelIds, rotateRandom, weights);
        generator.blockStateCollector.accept(blockstate);
        generator.registerParentedItemModel(block, modelIds.get(0));
    }

    private static VariantsBlockStateSupplier createLeavesBlockstate(Block block, List<Identifier> modelIds,
                                                                     boolean rotateRandom, List<Integer> weights) {
        List<BlockStateVariant> variants = new ArrayList<>();
        int rotationCount = rotateRandom ? 4 : 1;

        for (int i = 0; i < modelIds.size(); i++) {
            for (int rotation = 0; rotation < rotationCount; rotation++) {
                BlockStateVariant variant = BlockStateVariant.create()
                        .put(VariantSettings.MODEL, modelIds.get(i));

                if (weights != null && weights.get(i) > 1) {
                    variant = variant.put(VariantSettings.WEIGHT, weights.get(i));
                }

                if (rotation > 0) {
                    variant = variant.put(VariantSettings.Y, VariantSettings.Rotation.valueOf("R" + (90 * rotation)));
                }

                variants.add(variant);
            }
        }

        if (variants.size() == 1) {
            return VariantsBlockStateSupplier.create(block, variants.get(0));
        } else {
            return VariantsBlockStateSupplier.create(block, variants.toArray(new BlockStateVariant[0]));
        }
    }

    public static void registerCustomLeavesBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        String type = definition.getType();
        boolean tinted = definition.isTinted() || definition.hasColorMult();
        boolean overlay = definition.hasOverlay() || (type != null && type.contains("overlay"));
        boolean betterFoliage = definition.hasBetterFoliage() || (type != null && type.contains("better-foliage"));
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
