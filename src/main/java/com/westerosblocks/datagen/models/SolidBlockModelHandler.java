package com.westerosblocks.datagen.models;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.WesterosBlockStateRecord;
import com.westerosblocks.block.custom.WCSolidBlock;
import com.westerosblocks.datagen.ModTextureKey;
import com.westerosblocks.datagen.ModelExport;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;
import net.minecraft.block.Block;

import java.util.*;

public class SolidBlockModelHandler extends ModelExport {
    public static void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator, Block currentBlock, WesterosBlockDef blockDefinition) {
        final WCSolidBlock solidBlock = (WCSolidBlock) currentBlock;
        boolean isSymmetrical = solidBlock.symmetrical;

        // Handle single-state blocks
        if (blockDefinition.states.size() <= 1) {
            WesterosBlockStateRecord stateRecord = blockDefinition.states.getFirst();
            List<BlockStateVariant> variants = new ArrayList<>();
            String baseName = stateRecord.stateID == null ? "base" : stateRecord.stateID;

            generateVariantsForState(blockStateModelGenerator, blockDefinition, stateRecord,
                    baseName, isSymmetrical, variants);

            blockStateModelGenerator.blockStateCollector.accept(
                    VariantsBlockStateSupplier.create(currentBlock, variants.toArray(new BlockStateVariant[0]))
            );
            return;
        }

        Property<String> stateProperty = (Property<String>) currentBlock.getStateManager().getProperties().iterator().next();
        BlockStateVariantMap.SingleProperty<String> variantMap = BlockStateVariantMap.create(stateProperty);

        for (WesterosBlockStateRecord stateRecord : blockDefinition.states) {
            boolean justBase = stateRecord.stateID == null;
            Set<String> stateIDs = justBase ? null : Collections.singleton(stateRecord.stateID);
            String baseName = justBase ? "base" : stateRecord.stateID;
            boolean isTinted = stateRecord.isTinted();
            boolean hasOverlay = stateRecord.getOverlayTextureByIndex(0) != null;
            List<BlockStateVariant> stateVariants = new ArrayList<>();

            for (int setIdx = 0; setIdx < stateRecord.getRandomTextureSetCount(); setIdx++) {
                WesterosBlockDef.RandomTextureSet textureSet = stateRecord.getRandomTextureSet(setIdx);
                int cnt = stateRecord.rotateRandom ? 4 : 1;    // 4 for random, just 1 if not

                for (int i = 0; i < cnt; i++) {
                    if (isSymmetrical) {
                        Identifier smodelPath = createModelIdentifier(blockDefinition.blockName, "symmetrical", baseName, setIdx);
                        BlockStateVariant svariant = createVariant(smodelPath, textureSet.weight, i);
                        Identifier amodelPath = createModelIdentifier(blockDefinition.blockName, "asymmetrical", baseName, setIdx);
                        BlockStateVariant avariant = createVariant(amodelPath, textureSet.weight, i);

                        stateVariants.add(svariant);
                        stateVariants.add(avariant);

                        generateSolidModel(blockStateModelGenerator, smodelPath, true,
                                isTinted, hasOverlay, stateRecord, setIdx);
                        generateSolidModel(blockStateModelGenerator, amodelPath, true,
                                isTinted, hasOverlay, stateRecord, setIdx);

                    } else {
                        Identifier modelPath = createModelIdentifier(blockDefinition.blockName, null, baseName, setIdx);
                        BlockStateVariant variant = createVariant(modelPath, textureSet.weight, i);
                        stateVariants.add(variant);

                        generateSolidModel(blockStateModelGenerator, modelPath, false,
                                isTinted, hasOverlay, stateRecord, setIdx);
                    }

                }
            }

            if (!stateVariants.isEmpty()) {
                if (stateVariants.size() == 1) {
                    variantMap.register(baseName, stateVariants.getFirst());
                } else {
                    variantMap.register(baseName, stateVariants);
                }
            }
        }

        blockStateModelGenerator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(currentBlock).coordinate(variantMap)
        );
    }

    private static void generateVariantsForState(BlockStateModelGenerator generator,
                                                 WesterosBlockDef blockDef, WesterosBlockStateRecord stateRecord,
                                                 String baseName, boolean isSymmetrical, List<BlockStateVariant> variants) {

        boolean isTinted = stateRecord.isTinted();
        boolean hasOverlay = stateRecord.getOverlayTextureByIndex(0) != null;

        for (int setIdx = 0; setIdx < stateRecord.getRandomTextureSetCount(); setIdx++) {
            WesterosBlockDef.RandomTextureSet textureSet = stateRecord.getRandomTextureSet(setIdx);
            int rotationCount = stateRecord.rotateRandom ? 4 : 1;
            String[] paths = isSymmetrical ? new String[]{"symmetrical", "asymmetrical"} : new String[]{""};

            for (int rotIdx = 0; rotIdx < rotationCount; rotIdx++) {
                for (String pathType : paths) {
                    Identifier modelPath = createModelIdentifier(blockDef.blockName, pathType, baseName, setIdx);
                    BlockStateVariant var = createVariant(modelPath, textureSet.weight, rotIdx);
                    variants.add(var);
                    generateSolidModel(generator, modelPath, isSymmetrical,
                            isTinted, hasOverlay, stateRecord, setIdx);
                }
            }
        }
    }

    protected static void generateSolidModel(BlockStateModelGenerator generator,
                                             Identifier modelPath, boolean isSymmetrical,
                                             boolean isTinted, boolean hasOverlay, WesterosBlockStateRecord currentRec, int setIdx) {
        WesterosBlockDef.RandomTextureSet set = currentRec.getRandomTextureSet(setIdx);
        TextureMap textureMap = createTextureMap(set, isSymmetrical, hasOverlay, isTinted, currentRec);

        if (hasOverlay) {
            String parentPath = isTinted ? "tinted/cube_overlay" : "untinted/cube_overlay";
            ModModels.getAllSidesWithOverlay(parentPath)
                    .upload(modelPath, textureMap, generator.modelCollector);
        } else if (currentRec.getTextureCount() > 1 || isTinted) {
            String parentPath = isTinted ? "tinted/cube" : "cube";
            String namespace = isTinted ? WesterosBlocks.MOD_ID : "minecraft";
            ModModels.getAllSides(parentPath, namespace)
                    .upload(modelPath, textureMap, generator.modelCollector);
        } else {
            TextureMap textureMapAll = TextureMap.all(Identifier.of("minecraft", "block/cube"));
            Models.CUBE_ALL.upload(modelPath, textureMapAll, generator.modelCollector);
        }
    }


    private static TextureMap createTextureMap(WesterosBlockDef.RandomTextureSet ts, boolean isSymmetrical, boolean hasOverlay, boolean isTinted, WesterosBlockStateRecord currentRec) {
        if (hasOverlay) {
            return createOverlayTextureMap(ts, isSymmetrical, currentRec);
        } else if (currentRec.getTextureCount() > 1 || isTinted) {
            return createCustomTextureMap(ts, isSymmetrical);
        } else {
          return new TextureMap()
                .put(TextureKey.ALL, createBlockIdentifier(ts.getTextureByIndex(0)));
        }
    }

    private static TextureMap createOverlayTextureMap(WesterosBlockDef.RandomTextureSet ts, boolean isSymmetrical, WesterosBlockStateRecord currentRec) {
        TextureMap map = createCustomTextureMap(ts, isSymmetrical);

        return map.put(ModTextureKey.DOWN_OVERLAY, createBlockIdentifier(currentRec.getOverlayTextureByIndex(0)))
                .put(ModTextureKey.UP_OVERLAY, createBlockIdentifier(currentRec.getOverlayTextureByIndex(1)))
                .put(ModTextureKey.NORTH_OVERLAY, createBlockIdentifier(currentRec.getOverlayTextureByIndex(2)))
                .put(ModTextureKey.SOUTH_OVERLAY, createBlockIdentifier(currentRec.getOverlayTextureByIndex(3)))
                .put(ModTextureKey.WEST_OVERLAY, createBlockIdentifier(currentRec.getOverlayTextureByIndex(4)))
                .put(ModTextureKey.EAST_OVERLAY, createBlockIdentifier(currentRec.getOverlayTextureByIndex(5)));
    }

    private static TextureMap createCustomTextureMap(WesterosBlockDef.RandomTextureSet ts, boolean isSymmetrical) {
        return new TextureMap()
                .put(TextureKey.DOWN, createBlockIdentifier(ts.getTextureByIndex(0)))
                .put(TextureKey.UP, createBlockIdentifier(ts.getTextureByIndex(1)))
                .put(TextureKey.NORTH, createBlockIdentifier(ts.getTextureByIndex(2)))
                .put(TextureKey.SOUTH, createBlockIdentifier(ts.getTextureByIndex(3)))
                .put(TextureKey.WEST, createBlockIdentifier(ts.getTextureByIndex(isSymmetrical ? 4 : 6)))
                .put(TextureKey.EAST, createBlockIdentifier(ts.getTextureByIndex(isSymmetrical ? 5 : 7)))
                .put(TextureKey.PARTICLE, createBlockIdentifier(ts.getTextureByIndex(2)));
    }

    private static Identifier createModelIdentifier(String blockName, String type, String baseName, int setIdx) {
        StringBuilder path = new StringBuilder()
                .append(GENERATED_PATH)
                .append(blockName)
                .append('/');

        if (type != null && !type.isEmpty()) {
            path.append(type).append('/');
        }

        path.append(baseName)
                .append("_v")
                .append(setIdx + 1);

        return Identifier.of(WesterosBlocks.MOD_ID, path.toString());
    }

    public static void generateItemModels(ItemModelGenerator itemModelGenerator, Block currentBlock, WesterosBlockDef blockDefinition) {
        WesterosBlockStateRecord firstState = blockDefinition.states.getFirst();
        String baseName = firstState.stateID == null ? "base" : firstState.stateID;
        boolean isSymmetrical = currentBlock instanceof WCSolidBlock && ((WCSolidBlock) currentBlock).symmetrical;

        String path = isSymmetrical ?
                String.format("%s%s/symmetrical/%s_v1", GENERATED_PATH, blockDefinition.blockName, baseName) :
                String.format("%s%s/%s_v1", GENERATED_PATH, blockDefinition.blockName, baseName);

        // TODO
//        blockDefinition.getBlockColorMapResource();

        itemModelGenerator.register(
                currentBlock.asItem(),
                new Model(Optional.of(Identifier.of(WesterosBlocks.MOD_ID, path)), Optional.empty())
        );
    }
}