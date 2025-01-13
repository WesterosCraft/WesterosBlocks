//package com.westerosblocks.datagen.models;
//
//import com.westerosblocks.WesterosBlocks;
//import com.westerosblocks.block.WesterosBlockDef;
//import com.westerosblocks.block.WesterosBlockStateRecord;
//import com.westerosblocks.block.custom.WCWallBlock;
//import com.westerosblocks.datagen.ModTextureKey;
//import com.westerosblocks.datagen.ModelExportOld;
//import net.minecraft.block.Block;
//import net.minecraft.block.enums.WallShape;
//import net.minecraft.data.client.*;
//import net.minecraft.state.property.Properties;
//import net.minecraft.util.Identifier;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//public class WallBlockModelHandler extends ModelExportOld {
//    private static final String GENERATED_PATH = "block/generated/";
//
//    public static void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator, Block block, WesterosBlockDef blockDefinition) {
//        boolean isShortWall = block instanceof WCWallBlock && ((WCWallBlock) block).wallSize == WCWallBlock.WallSize.SHORT;
//        boolean hasMultipleStates = blockDefinition.states.size() > 1;
//
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
//                generateWallModel(blockStateModelGenerator, blockDefinition, state,
//                        baseName, setIdx, "post", textureSet, isOccluded, isTinted, hasOverlay, isShortWall);
//
//                // Generate side model
//                generateWallModel(blockStateModelGenerator, blockDefinition, state,
//                        baseName, setIdx, "side", textureSet, isOccluded, isTinted, hasOverlay, isShortWall);
//
//                // Generate tall side model
//                generateWallModel(blockStateModelGenerator, blockDefinition, state,
//                        baseName, setIdx, "side_tall", textureSet, isOccluded, isTinted, hasOverlay, isShortWall);
//            }
//        }
//
//        // Create multipart blockstate
//        MultipartBlockStateSupplier stateSupplier = MultipartBlockStateSupplier.create(block);
//
//        // Add post component when up is true
//        stateSupplier.with(
//                When.create().set(Properties.UP, true),
//                createWallBlockPostVariants(blockDefinition)
//        );
//
//        // Add side components for each direction
//        addWallSides(stateSupplier, blockDefinition, "north", Properties.NORTH_WALL_SHAPE);
//        addWallSides(stateSupplier, blockDefinition, "east", Properties.EAST_WALL_SHAPE, 90);
//        addWallSides(stateSupplier, blockDefinition, "south", Properties.SOUTH_WALL_SHAPE, 180);
//        addWallSides(stateSupplier, blockDefinition, "west", Properties.WEST_WALL_SHAPE, 270);
//
//        blockStateModelGenerator.blockStateCollector.accept(stateSupplier);
//    }
//
//    private static void generateWallModel(
//            BlockStateModelGenerator generator,
//            WesterosBlockDef blockDef,
//            WesterosBlockStateRecord state,
//            String baseName,
//            int setIdx,
//            String modelType,
//            WesterosBlockDef.RandomTextureSet textureSet,
//            boolean isOccluded,
//            boolean isTinted,
//            boolean hasOverlay,
//            boolean isShortWall) {
//
//        String baseModelPath = baseName.isEmpty() ?
//                String.format("%s%s", GENERATED_PATH, blockDef.blockName) :
//                String.format("%s%s/%s", GENERATED_PATH, blockDef.blockName, baseName);
//
//        Identifier modelId = Identifier.of(WesterosBlocks.MOD_ID,
//                baseModelPath + "/" + modelType + "_v" + (setIdx + 1));
//
//        TextureMap textureMap = createTextureMap(textureSet, state, hasOverlay);
//        String parentPath = getParentPath(isOccluded, isTinted, hasOverlay, modelType, isShortWall);
//
//        Model model = hasOverlay ?
//                ModModels.getBottomTopSideWithOverlay(parentPath) :
//                ModModels.getBottomTopSide(parentPath);
//
//        model.upload(modelId, textureMap, generator.modelCollector);
//    }
//
//    private static String getParentPath(boolean isOccluded, boolean isTinted, boolean hasOverlay, String modelType, boolean isShortWall) {
//        String basePath = isOccluded ?
//                (isTinted ? "tinted/" : "untinted/") :
//                (isTinted ? "tintednoocclusion/" : "noocclusion/");
//
//        String wallType = switch (modelType) {
//            case "post" -> "template_wall_post";
//            case "side" -> "template_wall_side" + (isShortWall ? "_2" : "");
//            case "side_tall" -> "template_wall_side_tall";
//            default -> throw new IllegalArgumentException("Invalid wall model type: " + modelType);
//        };
//
//        return basePath + wallType + (hasOverlay ? "_overlay" : "");
//    }
//
//    private static void addWallSides(
//            MultipartBlockStateSupplier stateSupplier,
//            WesterosBlockDef blockDef,
//            String direction,
//            net.minecraft.state.property.EnumProperty<WallShape> property,
//            int rotation) {
//
//        // Add low wall variants
//        stateSupplier.with(
//                When.create().set(property, WallShape.LOW),
//                createWallBlockSideVariants(blockDef, "side", rotation, true)
//        );
//
//        // Add tall wall variants
//        stateSupplier.with(
//                When.create().set(property, WallShape.TALL),
//                createWallBlockSideVariants(blockDef, "side_tall", rotation, true)
//        );
//    }
//
//    private static void addWallSides(
//            MultipartBlockStateSupplier stateSupplier,
//            WesterosBlockDef blockDef,
//            String direction,
//            net.minecraft.state.property.EnumProperty<WallShape> property) {
//        addWallSides(stateSupplier, blockDef, direction, property, 0);
//    }
//
//    private static List<BlockStateVariant> createWallBlockPostVariants(WesterosBlockDef blockDef) {
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
//    private static List<BlockStateVariant> createWallBlockSideVariants(
//            WesterosBlockDef blockDef,
//            String modelType,
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
//                                GENERATED_PATH + blockDef.blockName + "/" + baseName + modelType + "_v" + (setIdx + 1)));
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
//                .put(ModTextureKey.TOP_OVERLAY, createBlockIdentifier(currentRec.getOverlayTextureByIndex(0)))
//                .put(ModTextureKey.SIDE_OVERLAY, createBlockIdentifier(currentRec.getOverlayTextureByIndex(0)));
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
//                Optional.of(Identifier.of(WesterosBlocks.MOD_ID, "block/" + (isTinted ? "tinted" : "untinted") + "/wall_inventory")),
//                Optional.empty(),
//                TextureKey.BOTTOM,
//                TextureKey.TOP,
//                TextureKey.SIDE
//        );
//
//        model.upload(ModelIds.getItemModelId(block.asItem()), textureMap, itemModelGenerator.writer);
//    }
//}