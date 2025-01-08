package com.westerosblocks.datagen.models;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.WesterosBlockStateRecord;
import com.westerosblocks.datagen.ModTextureKey;
import com.westerosblocks.datagen.ModelExport;
import net.minecraft.block.Block;
import net.minecraft.block.enums.SlabType;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;

import java.util.*;

public class SlabBlockModelHandler extends ModelExport {
    private static final String GENERATED_PATH = "block/generated/";

    private static String getParentPath(boolean isOccluded, boolean isTinted, boolean hasOverlay, String type) {
        String basePath;
        if (isOccluded) {
            if (isTinted) {
                basePath = "tinted/";
            } else {
                basePath = "untinted/";
            }
        } else {
            if (isTinted) {
                basePath = "tintednoocclusion/";
            } else {
                basePath = "noocclusion/";
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
        boolean hasMultipleStates = blockDefinition.states.size() > 1;

        for (WesterosBlockStateRecord state : blockDefinition.states) {
            // Only include /base/ in path if there are multiple states
            String baseName = hasMultipleStates ?
                    (state.stateID == null ? "base" : state.stateID) : "";

            boolean isTinted = state.isTinted();
            boolean hasOverlay = state.getOverlayTextureByIndex(0) != null;

            for (int setIdx = 0; setIdx < state.getRandomTextureSetCount(); setIdx++) {
                WesterosBlockDef.RandomTextureSet textureSet = state.getRandomTextureSet(setIdx);
                String baseModelPath = baseName.isEmpty() ?
                        String.format("%s%s", GENERATED_PATH, blockDefinition.blockName) :
                        String.format("%s%s/%s", GENERATED_PATH, blockDefinition.blockName, baseName);

                generateVariantModels(blockStateModelGenerator, baseModelPath, setIdx, textureSet, state,
                        isOccluded, isTinted, hasOverlay);

                addVariantToList(bottomVariants, baseModelPath + "/bottom_v" + (setIdx + 1), textureSet.weight);
                addVariantToList(topVariants, baseModelPath + "/top_v" + (setIdx + 1), textureSet.weight);
                addVariantToList(doubleVariants, baseModelPath + "/double_v" + (setIdx + 1), textureSet.weight);
            }
        }

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
            WesterosBlockStateRecord currentRec,
            boolean isOccluded,
            boolean isTinted,
            boolean hasOverlay
    ) {
        // Remove this adjustment since baseModelPath is already correct from the caller
        String[] variants = {"bottom", "top", "double"};
        for (String variant : variants) {
            Identifier modelId = Identifier.of(WesterosBlocks.MOD_ID,
                    baseModelPath + "/" + variant + "_v" + (setIdx + 1));
            TextureMap textureMap = createTextureMap(textureSet, currentRec, hasOverlay);
            String parentPath = getParentPath(isOccluded, isTinted, hasOverlay, variant);

            Model model;
            if (hasOverlay) {
                model = ModModels.getAllSidesWithOverlay(parentPath);
            } else {
                model = ModModels.getAllSides(parentPath, WesterosBlocks.MOD_ID);
            }
            model.upload(modelId, textureMap, blockStateModelGenerator.modelCollector);
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

    private static TextureMap createTextureMap(WesterosBlockDef.RandomTextureSet ts, WesterosBlockStateRecord currentRec, boolean hasOverlay) {
        if (hasOverlay) {
            return createOverlayTextureMap(ts, currentRec);
        } else {
            return createCustomTextureMap(ts);
        }

    }

    private static TextureMap createOverlayTextureMap(WesterosBlockDef.RandomTextureSet ts, WesterosBlockStateRecord currentRec) {
        TextureMap map = createCustomTextureMap(ts);

        return map.put(ModTextureKey.DOWN_OVERLAY, createBlockIdentifier(currentRec.getOverlayTextureByIndex(0)))
                .put(ModTextureKey.UP_OVERLAY, createBlockIdentifier(currentRec.getOverlayTextureByIndex(1)))
                .put(ModTextureKey.NORTH_OVERLAY, createBlockIdentifier(currentRec.getOverlayTextureByIndex(2)))
                .put(ModTextureKey.SOUTH_OVERLAY, createBlockIdentifier(currentRec.getOverlayTextureByIndex(3)))
                .put(ModTextureKey.WEST_OVERLAY, createBlockIdentifier(currentRec.getOverlayTextureByIndex(4)))
                .put(ModTextureKey.EAST_OVERLAY, createBlockIdentifier(currentRec.getOverlayTextureByIndex(5)));
    }

    private static TextureMap createCustomTextureMap(WesterosBlockDef.RandomTextureSet ts) {
        return new TextureMap()
                .put(TextureKey.DOWN, createBlockIdentifier(ts.getTextureByIndex(0)))
                .put(TextureKey.UP, createBlockIdentifier(ts.getTextureByIndex(1)))
                .put(TextureKey.NORTH, createBlockIdentifier(ts.getTextureByIndex(2)))
                .put(TextureKey.SOUTH, createBlockIdentifier(ts.getTextureByIndex(3)))
                .put(TextureKey.WEST, createBlockIdentifier(ts.getTextureByIndex(4)))
                .put(TextureKey.EAST, createBlockIdentifier(ts.getTextureByIndex(5)))
                .put(TextureKey.PARTICLE, createBlockIdentifier(ts.getTextureByIndex(2)));
    }


    public static void generateItemModels(ItemModelGenerator itemModelGenerator, Block currentBlock, WesterosBlockDef blockDefinition) {
        boolean hasMultipleStates = blockDefinition.states.size() > 1;
        String basePath = hasMultipleStates ? "/base" : "";
        String path = String.format("%s%s%s/bottom_v1", GENERATED_PATH, blockDefinition.blockName, basePath);

        itemModelGenerator.register(
                currentBlock.asItem(),
                new Model(Optional.of(Identifier.of(WesterosBlocks.MOD_ID, path)), Optional.empty())
        );
    }
}