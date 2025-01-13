//package com.westerosblocks.datagen.models;
//
//import com.westerosblocks.WesterosBlocks;
//import com.westerosblocks.block.WesterosBlockDef;
//import com.westerosblocks.block.WesterosBlockStateRecord;
//import com.westerosblocks.block.custom.WCSolidBlock;
//import com.westerosblocks.datagen.ModTextureKey;
//import com.westerosblocks.datagen.ModelExportOld;
//import net.minecraft.data.client.*;
//import net.minecraft.util.Identifier;
//import net.minecraft.block.Block;
//
//import java.util.*;
//
//public class SolidBlockModelHandlerOld extends ModelExportOld {
//    public static void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator, Block block, WesterosBlockDef blockDefinition) {
//        StateObject stateObject = new StateObject(blockStateModelGenerator, block);
//
//        for (WesterosBlockStateRecord stateRecord : blockDefinition.states) {
//            String baseName = stateRecord.stateID == null ? "base" : stateRecord.stateID;
//            boolean isSymmetrical = block instanceof WCSolidBlock && ((WCSolidBlock) block).symmetrical;
//            boolean isTinted = stateRecord.isTinted();
//            boolean hasOverlay = stateRecord.getOverlayTextureByIndex(0) != null;
//
//            for (int setIdx = 0; setIdx < stateRecord.getRandomTextureSetCount(); setIdx++) {
//                WesterosBlockDef.RandomTextureSet textureSet = stateRecord.getRandomTextureSet(setIdx);
//                int rotationCount = stateRecord.rotateRandom ? 4 : 1;
//
//                if (isSymmetrical) {
//                    generateSymmetricalVariants(stateObject, blockDefinition, stateRecord, textureSet, baseName,
//                            setIdx, rotationCount);
//                } else {
//                    generateStandardVariants(stateObject, blockDefinition, stateRecord, textureSet, baseName,
//                            setIdx, rotationCount);
//                }
//
//                // Generate the models for all variants
//                generateModels(blockStateModelGenerator, blockDefinition, stateRecord, baseName, setIdx,
//                        isSymmetrical, isTinted, hasOverlay);
//            }
//        }
//
//        stateObject.generate();
//    }
//
//
//    private static void generateSymmetricalVariants(StateObject stateObject, WesterosBlockDef blockDef,
//                                                    WesterosBlockStateRecord stateRecord,
//                                                    WesterosBlockDef.RandomTextureSet textureSet,
//                                                    String baseName, int setIdx, int rotationCount) {
//        for (int rotIdx = 0; rotIdx < rotationCount; rotIdx++) {
//            // Symmetrical variant
//            Identifier symmetricalModel = createModelIdentifier(blockDef.blockName, "symmetrical", baseName, setIdx);
//            Variant.Builder symmetricalBuilder = Variant.builder()
//                    .model(symmetricalModel.toString())
//                    .weight(textureSet.weight);
//
//            if (rotIdx > 0) {
//                symmetricalBuilder.y(rotIdx * 90);
//            }
//
//            stateObject.addVariant("symmetrical=true", symmetricalBuilder.build(),
//                    stateRecord.stateID != null ? Collections.singleton(stateRecord.stateID) : null);
//
//            // Asymmetrical variant
//            Identifier asymmetricalModel = createModelIdentifier(blockDef.blockName, "asymmetrical", baseName, setIdx);
//            Variant.Builder asymmetricalBuilder = Variant.builder()
//                    .model(asymmetricalModel.toString())
//                    .weight(textureSet.weight);
//
//            if (rotIdx > 0) {
//                asymmetricalBuilder.y(rotIdx * 90);
//            }
//
//            stateObject.addVariant("symmetrical=false", asymmetricalBuilder.build(),
//                    stateRecord.stateID != null ? Collections.singleton(stateRecord.stateID) : null);
//        }
//    }
//
//    private static void generateStandardVariants(StateObject stateObject, WesterosBlockDef blockDef,
//                                                 WesterosBlockStateRecord stateRecord,
//                                                 WesterosBlockDef.RandomTextureSet textureSet,
//                                                 String baseName, int setIdx, int rotationCount) {
//        for (int rotIdx = 0; rotIdx < rotationCount; rotIdx++) {
//            // Create proper namespace:path format for the model identifier
//            String modelPath = String.format("block/generated/%s/%s_v%d",
//                    blockDef.blockName.toLowerCase(Locale.ROOT),
//                    baseName.toLowerCase(Locale.ROOT),
//                    setIdx + 1);
//
//            Identifier modelId = Identifier.of(WesterosBlocks.MOD_ID, modelPath);
//
//            Variant.Builder variantBuilder = Variant.builder()
//                    .model(modelId.toString())
//                    .weight(textureSet.weight);
//
//            if (rotIdx > 0) {
//                variantBuilder.y(rotIdx * 90);
//            }
//
//            stateObject.addVariant("", variantBuilder.build(),
//                    stateRecord.stateID != null ? Collections.singleton(stateRecord.stateID) : null);
//        }
//    }
//
//
//    private static String getModelPath(String blockName, String symmetryType, String baseName, int setIdx) {
//        StringBuilder path = new StringBuilder(WesterosBlocks.MOD_ID + ":block/generated/")
//                .append(blockName)
//                .append('/');
//
//        if (!symmetryType.isEmpty()) {
//            path.append(symmetryType).append('/');
//        }
//
//        path.append(baseName)
//                .append("_v")
//                .append(setIdx + 1);
//
//        return path.toString();
//    }
//
//    private static void generateModels(BlockStateModelGenerator generator, WesterosBlockDef blockDef,
//                                       WesterosBlockStateRecord stateRecord, String baseName, int setIdx,
//                                       boolean isSymmetrical, boolean isTinted, boolean hasOverlay) {
//        if (isSymmetrical) {
//            generateModelVariant(generator, blockDef, stateRecord, baseName, setIdx, true,
//                    isTinted, hasOverlay);
//            generateModelVariant(generator, blockDef, stateRecord, baseName, setIdx, false,
//                    isTinted, hasOverlay);
//        } else {
//            generateModelVariant(generator, blockDef, stateRecord, baseName, setIdx, null,
//                    isTinted, hasOverlay);
//        }
//    }
//
//    private static Identifier createModelIdentifier(String blockName, String symmetryType, String baseName, int setIdx) {
//        StringBuilder path = new StringBuilder("block/generated/")
//                .append(blockName.toLowerCase(Locale.ROOT))
//                .append('/');
//
//        if (!symmetryType.isEmpty()) {
//            path.append(symmetryType.toLowerCase(Locale.ROOT)).append('/');
//        }
//
//        path.append(baseName.toLowerCase(Locale.ROOT))
//                .append("_v")
//                .append(setIdx + 1);
//
//        return Identifier.of(WesterosBlocks.MOD_ID, path.toString());
//    }
//
//    private static void generateModelVariant(BlockStateModelGenerator generator, WesterosBlockDef blockDef,
//                                             WesterosBlockStateRecord stateRecord, String baseName, int setIdx,
//                                             Boolean symmetrical, boolean isTinted, boolean hasOverlay) {
//        Identifier modelId = createModelIdentifier(blockDef.blockName,
//                symmetrical != null ? (symmetrical ? "symmetrical" : "asymmetrical") : "",
//                baseName, setIdx);
//
//        TextureMap textureMap = createTextureMap(stateRecord.getRandomTextureSet(setIdx),
//                symmetrical != null && symmetrical, hasOverlay, stateRecord);
//
//        if (hasOverlay) {
//            ModModels.getAllSidesWithOverlay(isTinted ? "tinted/cube_overlay" : "untinted/cube_overlay")
//                    .upload(modelId, textureMap, generator.modelCollector);
//        } else if (stateRecord.getTextureCount() > 1 || isTinted) {
//            ModModels.getAllSides(isTinted ? "tinted/cube" : "cube",
//                            isTinted ? WesterosBlocks.MOD_ID : "minecraft")
//                    .upload(modelId, textureMap, generator.modelCollector);
//        } else {
//            Models.CUBE_ALL.upload(modelId,
//                    TextureMap.all(createBlockIdentifier(stateRecord.getRandomTextureSet(setIdx).getTextureByIndex(0))),
//                    generator.modelCollector);
//        }
//    }
//
//
//    private static String getSymmetryPath(String blockName, String baseName, int setIdx, Boolean symmetrical) {
//        StringBuilder path = new StringBuilder(GENERATED_PATH)
//                .append(blockName)
//                .append('/');
//
//        if (symmetrical != null) {
//            path.append(symmetrical ? "symmetrical/" : "asymmetrical/");
//        }
//
//        path.append(baseName)
//                .append("_v")
//                .append(setIdx + 1);
//
//        return path.toString();
//    }
//
//    private static TextureMap createTextureMap(WesterosBlockDef.RandomTextureSet ts, boolean isSymmetrical,
//                                               boolean hasOverlay, WesterosBlockStateRecord currentRec) {
//        TextureMap map = new TextureMap()
//                .put(TextureKey.DOWN, createBlockIdentifier(ts.getTextureByIndex(0)))
//                .put(TextureKey.UP, createBlockIdentifier(ts.getTextureByIndex(1)))
//                .put(TextureKey.NORTH, createBlockIdentifier(ts.getTextureByIndex(2)))
//                .put(TextureKey.SOUTH, createBlockIdentifier(ts.getTextureByIndex(3)))
//                .put(TextureKey.WEST, createBlockIdentifier(isSymmetrical ? ts.getTextureByIndex(4) :
//                        ts.getTextureByIndex(6)))
//                .put(TextureKey.EAST, createBlockIdentifier(isSymmetrical ? ts.getTextureByIndex(5) :
//                        ts.getTextureByIndex(7)))
//                .put(TextureKey.PARTICLE, createBlockIdentifier(ts.getTextureByIndex(2)));
//
//        if (hasOverlay) {
//            map.put(ModTextureKey.DOWN_OVERLAY, createBlockIdentifier(currentRec.getOverlayTextureByIndex(0)))
//                    .put(ModTextureKey.UP_OVERLAY, createBlockIdentifier(currentRec.getOverlayTextureByIndex(1)))
//                    .put(ModTextureKey.NORTH_OVERLAY, createBlockIdentifier(currentRec.getOverlayTextureByIndex(2)))
//                    .put(ModTextureKey.SOUTH_OVERLAY, createBlockIdentifier(currentRec.getOverlayTextureByIndex(3)))
//                    .put(ModTextureKey.WEST_OVERLAY, createBlockIdentifier(currentRec.getOverlayTextureByIndex(4)))
//                    .put(ModTextureKey.EAST_OVERLAY, createBlockIdentifier(currentRec.getOverlayTextureByIndex(5)));
//        }
//
//        return map;
//    }
//
//    public static void generateItemModels(ItemModelGenerator itemModelGenerator, Block block,
//                                          WesterosBlockDef blockDefinition) {
//        WesterosBlockStateRecord firstState = blockDefinition.states.getFirst();
//        String baseName = firstState.stateID == null ? "base" : firstState.stateID;
//        boolean isSymmetrical = block instanceof WCSolidBlock && ((WCSolidBlock) block).symmetrical;
//
//        String path = isSymmetrical ?
//                String.format("%s%s/symmetrical/%s_v1", GENERATED_PATH, blockDefinition.blockName, baseName) :
//                String.format("%s%s/%s_v1", GENERATED_PATH, blockDefinition.blockName, baseName);
//
//        itemModelGenerator.register(
//                block.asItem(),
//                new Model(Optional.of(Identifier.of(WesterosBlocks.MOD_ID, path)), Optional.empty())
//        );
//    }
//}