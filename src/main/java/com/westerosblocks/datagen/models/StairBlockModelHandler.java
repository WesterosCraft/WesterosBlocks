//package com.westerosblocks.datagen.models;
//
//import com.westerosblocks.WesterosBlocks;
//import com.westerosblocks.block.WesterosBlockDef;
//import com.westerosblocks.block.WesterosBlockStateRecord;
//import com.westerosblocks.block.custom.WCStairBlock;
//import com.westerosblocks.datagen.ModTextureKey;
//import com.westerosblocks.datagen.ModelExportOld;
//import net.minecraft.block.Block;
//import net.minecraft.block.enums.BlockHalf;
//import net.minecraft.block.enums.StairShape;
//import net.minecraft.data.client.*;
//import net.minecraft.state.property.Properties;
//import net.minecraft.state.property.Property;
//import net.minecraft.util.Identifier;
//import net.minecraft.util.math.Direction;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//public class StairBlockModelHandler extends ModelExportOld {
//    private static final String GENERATED_PATH = "block/generated/";
//
//    public static void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator, Block block, WesterosBlockDef blockDefinition) {
//        boolean isUvLocked = !(block instanceof WCStairBlock) || !((WCStairBlock) block).no_uvlock;
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
//                generateStairModel(blockStateModelGenerator,  blockDefinition, state,
//                        baseName, setIdx, "base", textureSet, isOccluded, isTinted, hasOverlay);
//                generateStairModel(blockStateModelGenerator, blockDefinition, state,
//                        baseName, setIdx, "inner", textureSet, isOccluded, isTinted, hasOverlay);
//                generateStairModel(blockStateModelGenerator, blockDefinition, state,
//                        baseName, setIdx, "outer", textureSet, isOccluded, isTinted, hasOverlay);
//            }
//        }
//
//        if (hasMultipleStates) {
//            // Get the state property from the block
//            @SuppressWarnings("unchecked")
//            Property<String> stateProperty = (Property<String>) block.getStateManager()
//                    .getProperties()
//                    .stream()
//                    .filter(p -> p.getName().equals("state"))
//                    .findFirst()
//                    .orElseThrow();
//
//            // Create a quad property map for facing, half, shape, and state
//            BlockStateVariantMap.QuadrupleProperty<Direction, BlockHalf, StairShape, String> variantMap =
//                    BlockStateVariantMap.create(
//                            Properties.HORIZONTAL_FACING,
//                            Properties.BLOCK_HALF,
//                            Properties.STAIR_SHAPE,
//                            stateProperty
//                    );
//
//            // Register variants for each combination
//            for (Direction facing : Direction.Type.HORIZONTAL) {
//                for (BlockHalf half : BlockHalf.values()) {
//                    for (StairShape shape : StairShape.values()) {
//                        for (WesterosBlockStateRecord state : blockDefinition.states) {
//                            String stateId = state.stateID == null ? "base" : state.stateID;
//
//                            List<BlockStateVariant> variants = new ArrayList<>();
//                            for (int setIdx = 0; setIdx < state.getRandomTextureSetCount(); setIdx++) {
//                                WesterosBlockDef.RandomTextureSet textureSet = state.getRandomTextureSet(setIdx);
//                                addStairVariant(variants, setIdx, shape, facing, half,
//                                        textureSet.weight, isUvLocked, blockDefinition, state);
//                            }
//
//                            if (!variants.isEmpty()) {
//                                variantMap.register(facing, half, shape, stateId, variants);
//                            }
//                        }
//                    }
//                }
//            }
//
//            // Create and register the block state
//            blockStateModelGenerator.blockStateCollector.accept(
//                    VariantsBlockStateSupplier.create(block)
//                            .coordinate(variantMap)
//            );
//        } else {
//            // Now handle block state variants for single state
//            BlockStateVariantMap.TripleProperty<Direction, BlockHalf, StairShape> variantMap =
//                    BlockStateVariantMap.create(Properties.HORIZONTAL_FACING, Properties.BLOCK_HALF, Properties.STAIR_SHAPE);
//
//            // Register variants for each combination
//            for (Direction facing : Direction.Type.HORIZONTAL) {
//                for (BlockHalf half : BlockHalf.values()) {
//                    for (StairShape shape : StairShape.values()) {
//                        // Single state handling
//                        WesterosBlockStateRecord state = blockDefinition.states.get(0);
//                        List<BlockStateVariant> variants = new ArrayList<>();
//                        for (int setIdx = 0; setIdx < state.getRandomTextureSetCount(); setIdx++) {
//                            WesterosBlockDef.RandomTextureSet textureSet = state.getRandomTextureSet(setIdx);
//                            addStairVariant(variants, setIdx, shape, facing, half,
//                                    textureSet.weight, isUvLocked, blockDefinition, state);
//                        }
//
//                        if (!variants.isEmpty()) {
//                            variantMap.register(facing, half, shape, variants);
//                        }
//                    }
//                }
//            }
//
//            // Create and register the block state
//            blockStateModelGenerator.blockStateCollector.accept(
//                    VariantsBlockStateSupplier.create(block)
//                            .coordinate(variantMap)
//            );
//        }
//    }
//
//
//    private static void generateStairModel(
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
//        String basePath;
//        if (isOccluded) {
//            basePath = isTinted ? "tinted/" : "untinted/";
//        } else {
//            basePath = isTinted ? "tintednoocclusion/" : "noocclusion/";
//        }
//
//        if (modelType.equals("base")) {
//            return basePath + (hasOverlay ? "stairs_overlay" : "stairs");
//        }
//        return basePath + (hasOverlay ? modelType + "_stairs_overlay" : modelType + "_stairs");
//    }
//
//    private static void addStairVariant(
//            List<BlockStateVariant> variants,
//            int setIdx,
//            StairShape shape,
//            Direction facing,
//            BlockHalf half,
//            Integer weight,
//            boolean isUvLocked,
//            WesterosBlockDef blockDefinition,
//            WesterosBlockStateRecord state
//    ) {
//        String modelType = switch (shape) {
//            case STRAIGHT -> "base";
//            case INNER_LEFT, INNER_RIGHT -> "inner";
//            case OUTER_LEFT, OUTER_RIGHT -> "outer";
//        };
//
//        String statePrefix = state.stateID != null ? "/" + state.stateID : "";
//        String fullPath = String.format("block/generated/%s%s/%s_v%d",
//                blockDefinition.blockName,
//                statePrefix,
//                modelType,
//                setIdx + 1);
//
//        BlockStateVariant variant = BlockStateVariant.create()
//                .put(VariantSettings.MODEL, Identifier.of(WesterosBlocks.MOD_ID, fullPath));
//
//        // Apply X rotation for top half
//        if (half == BlockHalf.TOP) {
//            variant.put(VariantSettings.X, VariantSettings.Rotation.R180);
//        }
//
//        // Calculate Y rotation based on shape and facing
//        int yRotation = 0;
//        boolean needsUvLock = false;
//
//        switch (shape) {
//            case STRAIGHT -> {
//                yRotation = switch (facing) {
//                    case EAST -> 0;
//                    case SOUTH -> 90;
//                    case WEST -> 180;
//                    case NORTH -> 270;
//                    default -> 0;
//                };
//                needsUvLock = yRotation != 0 || half == BlockHalf.TOP;
//            }
//            case INNER_LEFT -> {
//                needsUvLock = true;
//                yRotation = switch (facing) {
//                    case NORTH -> 180;
//                    case EAST -> 270;
//                    case SOUTH -> 0;
//                    case WEST -> 90;
//                    default -> 0;
//                };
//            }
//            case INNER_RIGHT -> {
//                needsUvLock = true;
//                yRotation = switch (facing) {
//                    case NORTH -> 270;
//                    case EAST -> 0;
//                    case SOUTH -> 90;
//                    case WEST -> 180;
//                    default -> 0;
//                };
//            }
//            case OUTER_LEFT -> {
//                needsUvLock = true;
//                yRotation = switch (facing) {
//                    case NORTH -> 180;
//                    case EAST -> 270;
//                    case SOUTH -> 0;
//                    case WEST -> 90;
//                    default -> 0;
//                };
//            }
//            case OUTER_RIGHT -> {
//                needsUvLock = true;
//                yRotation = switch (facing) {
//                    case NORTH -> 270;
//                    case EAST -> 0;
//                    case SOUTH -> 90;
//                    case WEST -> 180;
//                    default -> 0;
//                };
//            }
//        }
//
//        if (yRotation != 0) {
//            variant.put(VariantSettings.Y, VariantSettings.Rotation.valueOf("R" + yRotation));
//        }
//
//        // Apply UV lock if needed and enabled
//        if (isUvLocked && needsUvLock) {
//            variant.put(VariantSettings.UVLOCK, true);
//        }
//
//        // Add weight if specified
//        if (weight != null && weight > 0) {
//            variant.put(VariantSettings.WEIGHT, weight);
//        }
//
//        // Add single variant instead of repeating it 4 times
//        variants.add(variant);
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
//        return map.put(ModTextureKey.BOTTOM_OVERLAY, createBlockIdentifier(currentRec.getOverlayTextureByIndex(0)))
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
//        boolean hasMultipleStates = blockDefinition.states.size() > 1;
//        String basePath = hasMultipleStates ? "/base" : "";
//        String path = String.format("%s%s%s/base_v1", GENERATED_PATH, blockDefinition.blockName, basePath);
//
//        itemModelGenerator.register(
//                block.asItem(),
//                new Model(Optional.of(Identifier.of(WesterosBlocks.MOD_ID, path)), Optional.empty())
//        );
//    }
//}