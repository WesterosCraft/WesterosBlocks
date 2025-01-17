//package com.westerosblocks.datagen.models;
//
//import com.westerosblocks.WesterosBlocks;
//import com.westerosblocks.block.WesterosBlockDef;
//import com.westerosblocks.block.WesterosBlockStateRecord;
//import com.westerosblocks.block.custom.WCFenceBlock;
//import com.westerosblocks.datagen.models.ModTextureKey;
//import com.westerosblocks.datagen.ModelExportOld;
//import net.minecraft.block.Block;
//import net.minecraft.data.client.*;
//import net.minecraft.state.property.Properties;
//import net.minecraft.util.Identifier;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//public class FenceBlockModelHandler extends ModelExportOld {
//    private static final String GENERATED_PATH = "block/generated/";
//
//    public static void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator, Block block, WesterosBlockDef blockDefinition) {
//        boolean isUnconnected = block instanceof WCFenceBlock && ((WCFfenceBlock) block).unconnect;
//        boolean hasMultipleStates = blockDefinition.states.size() > 1;
//
//        // Generate models for each state
//        for (WesterosBlockStateRecord state : blockDefinition.states) {
//            String baseName = hasMultipleStates ? (state.stateID == null ? "base" : state.stateID) : "";
//            boolean isTinted = state.isTinted();
//            boolean hasOverlay = state.getOverlayTextureByIndex(0) != null;
//            boolean isOccluded = (blockDefinition.ambientOcclusion != null) ? blockDefinition.ambientOcclusion : true;
//
//            for (int setIdx = 0; setIdx < state.getRandomTextureSetCount(); setIdx++) {
//                WesterosBlockDef.RandomTextureSet textureSet = state.getRandomTextureSet(setIdx);
//
//                // Generate post model
//                generateFenceModel(blockStateModelGenerator, blockDefinition, state,
//                        baseName, setIdx, "post", textureSet, isOccluded, isTinted, hasOverlay);
//
//                // Generate side model
//                generateFenceModel(blockStateModelGenerator, blockDefinition, state,
//                        baseName, setIdx, "side", textureSet, isOccluded, isTinted, hasOverlay);
//            }
//        }
//
//        // Create multipart blockstate
//        MultipartBlockStateSupplier stateSupplier = MultipartBlockStateSupplier.create(block);
//
//        // Add center post - always present
//        stateSupplier.with(createFencePostVariants(blockDefinition));
//
//        // Add side connections
//        addFenceSide(stateSupplier, blockDefinition, Properties.NORTH, 0);
//        addFenceSide(stateSupplier, blockDefinition, Properties.EAST, 90);
//        addFenceSide(stateSupplier, blockDefinition, Properties.SOUTH, 180);
//        addFenceSide(stateSupplier, blockDefinition, Properties.WEST, 270);
//
//        blockStateModelGenerator.blockStateCollector.accept(stateSupplier);
//    }
//
//    private static void generateFenceModel(
//            BlockStateModelGenerator generator,
//            WesterosBlockDef blockDef,
//            WesterosBlockStateRecord state,
//            String baseName,
//            int setIdx,
//            String modelType,
//            WesterosBlockDef.RandomTextureSet textureSet,
//            boolean isOccluded,
//            boolean isTinted,
//            boolean hasOverlay) {
//
//        String baseModelPath = baseName.isEmpty() ?
//                String.format("%s%s", GENERATED_PATH, blockDef.blockName) :
//                String.format("%s%s/%s", GENERATED_PATH, blockDef.blockName, baseName);
//
//        Identifier modelId = Identifier.of(WesterosBlocks.MOD_ID,
//                baseModelPath + "/" + modelType + "_v" + (setIdx + 1));
//
//        TextureMap textureMap = createTextureMap(textureSet, state, hasOverlay);
//        String parentPath = getParentPath(isOccluded, isTinted, hasOverlay, modelType);
//
//        Model model = hasOverlay ?
//                ModModels.getBottomTopSideWithOverlay(parentPath) :
//                ModModels.getBottomTopSide(parentPath);
//
//        model.upload(modelId, textureMap, generator.modelCollector);
//    }
//
//    private static String getParentPath(boolean isOccluded, boolean isTinted, boolean hasOverlay, String modelType) {
//        String basePath = isOccluded ?
//                (isTinted ? "tinted/" : "untinted/") :
//                (isTinted ? "tintednoocclusion/" : "noocclusion/");
//
//        String fenceType = switch (modelType) {
//            case "post" -> "fence_post";
//            case "side" -> "fence_side";
//            default -> throw new IllegalArgumentException("Invalid fence model type: " + modelType);
//        };
//
//        return basePath + fenceType + (hasOverlay ? "_overlay" : "");
//    }
//
//    private static void addFenceSide(
//            MultipartBlockStateSupplier stateSupplier,
//            WesterosBlockDef blockDef,
//            net.minecraft.state.property.BooleanProperty property,
//            int rotation) {
//
//        // Create variants list and wrap it
//        List<BlockStateVariant> variants = createFenceSideVariants(blockDef, rotation, true);
//        stateSupplier.with(When.create().set(property, true), variants);
//    }
//
//    private static List<BlockStateVariant> createFencePostVariants(WesterosBlockDef blockDef) {
//        List<BlockStateVariant> variants = new ArrayList<>();
//
//        for (WesterosBlockStateRecord state : blockDef.states) {
//            String baseName = state.stateID == null ? "" : state.stateID + "/";
//
//            for (int setIdx = 0; setIdx < state.getRandomTextureSetCount(); setIdx++) {
//                WesterosBlockDef.RandomTextureSet textureSet = state.getRandomTextureSet(setIdx);
//
//                BlockStateVariant variant = BlockStateVariant.create()
//                        .put(VariantSettings.MODEL, Identifier.of(WesterosBlocks.MOD_ID,
//                                GENERATED_PATH + blockDef.blockName + "/" + baseName + "post_v" + (setIdx + 1)));
//
//                if (textureSet.weight != null && textureSet.weight > 0) {
//                    variant.put(VariantSettings.WEIGHT, textureSet.weight);
//                }
//
//                variants.add(variant);
//            }
//        }
//
//        return variants;
//    }
//
//    private static List<BlockStateVariant> createFenceSideVariants(
//            WesterosBlockDef blockDef,
//            int rotation,
//            boolean uvlock) {
//        List<BlockStateVariant> variants = new ArrayList<>();
//
//        for (WesterosBlockStateRecord state : blockDef.states) {
//            String baseName = state.stateID == null ? "" : state.stateID + "/";
//
//            for (int setIdx = 0; setIdx < state.getRandomTextureSetCount(); setIdx++) {
//                WesterosBlockDef.RandomTextureSet textureSet = state.getRandomTextureSet(setIdx);
//
//                BlockStateVariant variant = BlockStateVariant.create()
//                        .put(VariantSettings.MODEL, Identifier.of(WesterosBlocks.MOD_ID,
//                                GENERATED_PATH + blockDef.blockName + "/" + baseName + "side_v" + (setIdx + 1)));
//
//                if (rotation != 0) {
//                    variant.put(VariantSettings.Y, VariantSettings.Rotation.valueOf("R" + rotation));
//                }
//
//                if (uvlock) {
//                    variant.put(VariantSettings.UVLOCK, true);
//                }
//
//                if (textureSet.weight != null && textureSet.weight > 0) {
//                    variant.put(VariantSettings.WEIGHT, textureSet.weight);
//                }
//
//                variants.add(variant);
//            }
//        }
//
//        return variants;
//    }
//
//    private static TextureMap createTextureMap(WesterosBlockDef.RandomTextureSet ts, WesterosBlockStateRecord currentRec, boolean hasOverlay) {
//        if (hasOverlay) {
//            return createOverlayTextureMap(ts, currentRec);
//        } else {
//            return createCustomTextureMap(ts);
//        }
//    }
//
//    private static TextureMap createOverlayTextureMap(WesterosBlockDef.RandomTextureSet ts, WesterosBlockStateRecord currentRec) {
//        TextureMap map = createCustomTextureMap(ts);
//        return map
//                .put(ModTextureKey.BOTTOM_OVERLAY, createBlockIdentifier(currentRec.getOverlayTextureByIndex(0)))
//                .put(ModTextureKey.TOP_OVERLAY, createBlockIdentifier(currentRec.getOverlayTextureByIndex(1)))
//                .put(ModTextureKey.SIDE_OVERLAY, createBlockIdentifier(currentRec.getOverlayTextureByIndex(2)));
//    }
//
//    private static TextureMap createCustomTextureMap(WesterosBlockDef.RandomTextureSet ts) {
//        return new TextureMap()
//                .put(TextureKey.BOTTOM, createBlockIdentifier(ts.getTextureByIndex(0)))
//                .put(TextureKey.TOP, createBlockIdentifier(ts.getTextureByIndex(1)))
//                .put(TextureKey.SIDE, createBlockIdentifier(ts.getTextureByIndex(2)))
//                .put(TextureKey.PARTICLE, createBlockIdentifier(ts.getTextureByIndex(2)));
//    }
//
//    public static void generateItemModels(ItemModelGenerator itemModelGenerator, Block block, WesterosBlockDef blockDefinition) {
//        WesterosBlockStateRecord firstState = blockDefinition.states.getFirst();
//        WesterosBlockDef.RandomTextureSet firstSet = firstState.getRandomTextureSet(0);
//        boolean isTinted = firstState.isTinted();
//
//        TextureMap textureMap = new TextureMap()
//                .put(TextureKey.BOTTOM, createBlockIdentifier(firstSet.getTextureByIndex(0)))
//                .put(TextureKey.TOP, createBlockIdentifier(firstSet.getTextureByIndex(1)))
//                .put(TextureKey.SIDE, createBlockIdentifier(firstSet.getTextureByIndex(2)));
//
//        Model model = new Model(
//                Optional.of(Identifier.of(WesterosBlocks.MOD_ID, "block/" + (isTinted ? "tinted" : "untinted") + "/fence_inventory")),
//                Optional.empty(),
//                TextureKey.BOTTOM,
//                TextureKey.TOP,
//                TextureKey.SIDE
//        );
//
//        model.upload(ModelIds.getItemModelId(block.asItem()), textureMap, itemModelGenerator.writer);
//    }
//}