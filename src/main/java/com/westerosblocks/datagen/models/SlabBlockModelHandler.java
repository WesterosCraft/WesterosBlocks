package com.westerosblocks.datagen.models;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.WesterosBlockStateRecord;
import net.minecraft.block.Block;
import net.minecraft.block.enums.SlabType;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;

import java.util.*;

public class SlabBlockModelHandler {
    private static final String GENERATED_PATH = "block/generated/";

    private static String getParentPath(boolean isOccluded, boolean isTinted, boolean hasOverlay, String type) {
        String basePath;
        if (isOccluded) {
            if (isTinted) {
                basePath = "block/tinted/";
            } else {
                basePath = "block/untinted/";
            }
        } else {
            if (isTinted) {
                basePath = "block/tintednoocclusion/";
            } else {
                basePath = "block/noocclusion/";
            }
        }

        // Return the complete path based on type and overlay
        return switch (type) {
            case "bottom" -> basePath + (hasOverlay ? "half_slab_overlay" : "half_slab");
            case "top" -> basePath + (hasOverlay ? "upper_slab_overlay" : "upper_slab");
            case "double" -> basePath + (hasOverlay ? "cube_overlay" : "cube");
            default -> "block/cube_all";
        };
    }

    public static void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator, Block currentBlock, WesterosBlockDef blockDefinition) {
        List<BlockStateVariant> bottomVariants = new ArrayList<>();
        List<BlockStateVariant> topVariants = new ArrayList<>();
        List<BlockStateVariant> doubleVariants = new ArrayList<>();

        boolean isOccluded = (blockDefinition.ambientOcclusion != null) ? blockDefinition.ambientOcclusion : true;

        for (WesterosBlockStateRecord state : blockDefinition.states) {
            String baseName = state.stateID == null ? "base" : state.stateID;
            boolean isTinted = state.isTinted();
            boolean hasOverlay = state.getOverlayTextureByIndex(0) != null;

            for (int setIdx = 0; setIdx < state.getRandomTextureSetCount(); setIdx++) {
                WesterosBlockDef.RandomTextureSet textureSet = state.getRandomTextureSet(setIdx);
                String baseModelPath = Objects.equals(baseName, "base") ?
                        String.format("%s%s", GENERATED_PATH, blockDefinition.blockName) :
                        String.format("%s%s/%s", GENERATED_PATH, blockDefinition.blockName, baseName);

                // Generates models for bottom, top, and double variants
                generateVariantModels(blockStateModelGenerator, baseModelPath, setIdx, textureSet,
                        isOccluded, isTinted, hasOverlay, blockDefinition);

                addVariantToList(bottomVariants, baseModelPath + "/bottom_v" + (setIdx + 1), textureSet.weight);
                addVariantToList(topVariants, baseModelPath + "/top_v" + (setIdx + 1), textureSet.weight);
                addVariantToList(doubleVariants, baseModelPath + "/double_v" + (setIdx + 1), textureSet.weight);
            }
        }
        // generates blockstates
        blockStateModelGenerator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(currentBlock)
                        .coordinate(BlockStateVariantMap.create(Properties.SLAB_TYPE)
                                .register(SlabType.BOTTOM, bottomVariants)
                                .register(SlabType.TOP, topVariants)
                                .register(SlabType.DOUBLE, doubleVariants))
        );
    }

    private static void generateVariantModels(
            BlockStateModelGenerator blockStateModelGenerator,
            String baseModelPath,
            int setIdx,
            WesterosBlockDef.RandomTextureSet textureSet,
            boolean isOccluded,
            boolean isTinted,
            boolean hasOverlay,
            WesterosBlockDef blockDefinition
    ) {
        String[] variants = {"bottom", "top", "double"};
        for (String variant : variants) {
            Identifier modelId = Identifier.of(WesterosBlocks.MOD_ID,
                    baseModelPath + "/" + variant + "_v" + (setIdx + 1));
            TextureMap textureMap = createTextureMap(variant, textureSet, hasOverlay, blockDefinition);
            String parentPath = getParentPath(isOccluded, isTinted, hasOverlay, variant);

            switch (variant) {
                case "bottom", "top" -> {
                    Model slabModel = new Model(
                            Optional.of(Identifier.of(WesterosBlocks.MOD_ID, parentPath)),
                            Optional.empty(),
                            TextureKey.DOWN, TextureKey.UP, TextureKey.NORTH, TextureKey.SOUTH, TextureKey.WEST, TextureKey.EAST
                    );
                    slabModel.upload(modelId, textureMap, blockStateModelGenerator.modelCollector);
                }
                default -> Models.CUBE_ALL.upload(modelId, textureMap, blockStateModelGenerator.modelCollector);
            }
        }
    }

    private static void addVariantToList(List<BlockStateVariant> variants, String modelPath, Integer weight) {
        BlockStateVariant variant = BlockStateVariant.create()
                .put(VariantSettings.MODEL, Identifier.of(WesterosBlocks.MOD_ID, modelPath));
        if (weight != null && weight > 0) {
            variant.put(VariantSettings.WEIGHT, weight);
        }
        variants.add(variant);
    }

    private static TextureMap createTextureMap(String variant, WesterosBlockDef.RandomTextureSet textureSet, boolean hasOverlay, WesterosBlockDef blockDefinition) {
        TextureMap textureMap;
        if (variant.equals("double")) {
            textureMap = new TextureMap()
                    .put(TextureKey.ALL, Identifier.of(WesterosBlocks.MOD_ID, "block/" + textureSet.getTextureByIndex(0)));
        } else {
            textureMap = new TextureMap()
                    .put(TextureKey.DOWN, Identifier.of(WesterosBlocks.MOD_ID, "block/" + textureSet.getTextureByIndex(0)))
                    .put(TextureKey.UP, Identifier.of(WesterosBlocks.MOD_ID, "block/" + textureSet.getTextureByIndex(1)))
                    .put(TextureKey.NORTH, Identifier.of(WesterosBlocks.MOD_ID, "block/" + textureSet.getTextureByIndex(2)))
                    .put(TextureKey.SOUTH, Identifier.of(WesterosBlocks.MOD_ID, "block/" + textureSet.getTextureByIndex(3)))
                    .put(TextureKey.WEST, Identifier.of(WesterosBlocks.MOD_ID, "block/" + textureSet.getTextureByIndex(4)))
                    .put(TextureKey.EAST, Identifier.of(WesterosBlocks.MOD_ID, "block/" + textureSet.getTextureByIndex(5)))
                    .put(TextureKey.PARTICLE, Identifier.of(WesterosBlocks.MOD_ID, "block/" + textureSet.getTextureByIndex(2)));
        }

        // todo: replace with currentRec pattern from solid block handler
//        if (hasOverlay) {
//            textureMap
//                    .put(TextureKey.of("down_ov"), blockDefinition.getOverlayTexture(0))
//                    .put(TextureKey.of("up_ov"), blockDefinition.getOverlayTexture( 1))
//                    .put(TextureKey.of("north_ov"), blockDefinition.getOverlayTexture(2))
//                    .put(TextureKey.of("south_ov"), blockDefinition.getOverlayTexture(3))
//                    .put(TextureKey.of("west_ov"), blockDefinition.getOverlayTexture(4))
//                    .put(TextureKey.of("east_ov"), blockDefinition.getOverlayTexture(5));
//        }

        return textureMap;
    }


    public static void generateItemModels(ItemModelGenerator itemModelGenerator, Block currentBlock, WesterosBlockDef blockDefinition) {
        WesterosBlockStateRecord firstState = blockDefinition.states.getFirst();
        String baseName = firstState.stateID == null ? "base" : firstState.stateID;

        String path = baseName.equals("base") ?
                String.format("%s%s/bottom_v1", GENERATED_PATH, blockDefinition.blockName) :
                String.format("%s%s/%s/bottom_v1", GENERATED_PATH, blockDefinition.blockName, baseName);

        itemModelGenerator.register(
                currentBlock.asItem(),
                new Model(Optional.of(Identifier.of(WesterosBlocks.MOD_ID, path)), Optional.empty())
        );
    }
}