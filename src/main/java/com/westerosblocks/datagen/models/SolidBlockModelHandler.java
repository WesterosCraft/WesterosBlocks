package com.westerosblocks.datagen.models;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.WesterosBlockStateRecord;
import com.westerosblocks.block.custom.WCSolidBlock;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;
import net.minecraft.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class SolidBlockModelHandler {
    private static final String GENERATED_PATH = "block/generated/";
    private static final String BLOCK_PATH = "block/";

    public static void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator, Block currentBlock, WesterosBlockDef blockDefinition) {
        WCSolidBlock solidBlock = (currentBlock instanceof WCSolidBlock) ? (WCSolidBlock) currentBlock : null;
        boolean isSymmetrical = currentBlock instanceof WCSolidBlock && solidBlock.symmetrical;
        List<BlockStateVariant> variants = new ArrayList<>();

        // Process each state record
        for (WesterosBlockStateRecord stateRecord : blockDefinition.states) {
            String baseName = stateRecord.stateID == null ? "base" : stateRecord.stateID;
            boolean isTinted = stateRecord.isTinted();
            boolean hasOverlay = stateRecord.getOverlayTextureByIndex(0) != null;

            // Handle each random texture set
            for (int setIdx = 0; setIdx < stateRecord.getRandomTextureSetCount(); setIdx++) {
                WesterosBlockDef.RandomTextureSet textureSet = stateRecord.getRandomTextureSet(setIdx);
                int rotationCount = stateRecord.rotateRandom ? 4 : 1;

                for (int i = 0; i < rotationCount; i++) {
                    String[] paths = isSymmetrical ? new String[]{"symmetrical", "asymmetrical"} : new String[]{""};

                    for (String pathType : paths) {
                        Identifier modelPath = createModelIdentifier(blockDefinition.blockName, pathType, baseName, setIdx);

                        BlockStateVariant variant = BlockStateVariant.create()
                                .put(VariantSettings.MODEL, modelPath);

                        if (textureSet.weight != null && textureSet.weight > 0) {
                            variant.put(VariantSettings.WEIGHT, textureSet.weight);
                        }
                        if (i > 0) {
                            variant.put(VariantSettings.Y, VariantSettings.Rotation.values()[i]);
                        }
                        variants.add(variant);
                        generateSolidModel(blockStateModelGenerator, blockDefinition, modelPath, textureSet, isSymmetrical, isTinted, hasOverlay, stateRecord);
                    }
                }
            }
        }

        blockStateModelGenerator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(currentBlock, variants.toArray(value -> variants.toArray(new BlockStateVariant[value])))
        );
    }

    protected static void generateSolidModel(BlockStateModelGenerator blockStateModelGenerator, WesterosBlockDef blockDefinition, Identifier modelPath, WesterosBlockDef.RandomTextureSet textureSet, boolean isSymmetrical, boolean isTinted, boolean hasOverlay, WesterosBlockStateRecord currentRec) {
        TextureMap textureMap;

        if (isSymmetrical || (textureSet.getTextureCount() > 1) || isTinted) {
            textureMap = createTextureMap(textureSet, isSymmetrical, hasOverlay, isTinted, blockDefinition, currentRec);
            if (hasOverlay) {
                ModModels.ALL_SIDES_WITH_OVERLAY.upload(modelPath, textureMap, blockStateModelGenerator.modelCollector);
            } else {
                ModModels.ALL_SIDES.upload(modelPath, textureMap, blockStateModelGenerator.modelCollector);
            }
        } else {
            textureMap = TextureMap.all(Identifier.of(WesterosBlocks.MOD_ID, "block/" + textureSet.getTextureByIndex(0)));
            Models.CUBE_ALL.upload(modelPath, textureMap, blockStateModelGenerator.modelCollector);
        }
    }

    private static TextureMap createTextureMap(WesterosBlockDef.RandomTextureSet ts, boolean isSymmetrical, boolean hasOverlay, boolean isTinted, WesterosBlockDef blockDefinition, WesterosBlockStateRecord currentRec) {
        TextureMap textureMap = new TextureMap()
                .put(TextureKey.DOWN, Identifier.of(WesterosBlocks.MOD_ID, "block/" + ts.getTextureByIndex(0)))
                .put(TextureKey.UP, Identifier.of(WesterosBlocks.MOD_ID, "block/" + ts.getTextureByIndex(1)))
                .put(TextureKey.NORTH, Identifier.of(WesterosBlocks.MOD_ID, "block/" + ts.getTextureByIndex(2)))
                .put(TextureKey.SOUTH, Identifier.of(WesterosBlocks.MOD_ID, "block/" + ts.getTextureByIndex(3)))
                .put(TextureKey.WEST, Identifier.of(WesterosBlocks.MOD_ID, "block/" + (isSymmetrical ? ts.getTextureByIndex(4) : ts.getTextureByIndex(6))))
                .put(TextureKey.EAST, Identifier.of(WesterosBlocks.MOD_ID, "block/" + (isSymmetrical ? ts.getTextureByIndex(5) : ts.getTextureByIndex(7))))
                .put(TextureKey.PARTICLE, Identifier.of(WesterosBlocks.MOD_ID, "block/" + ts.getTextureByIndex(2)));

        if (hasOverlay) {
            boolean hasAll = currentRec.getOverlayTextureByIndex(1) == null && currentRec.getOverlayTextureByIndex(1) != null;
            if (hasAll) {
                Identifier overlayId = currentRec.getOverlayTextureByIndex(0);

                textureMap
                        .put(TextureKey.of("down_ov"), overlayId)
                        .put(TextureKey.of("up_ov"), overlayId)
                        .put(TextureKey.of("north_ov"), overlayId)
                        .put(TextureKey.of("south_ov"), overlayId)
                        .put(TextureKey.of("west_ov"), overlayId)
                        .put(TextureKey.of("east_ov"), overlayId);
            } else {
                textureMap
                        .put(TextureKey.of("down_ov"), currentRec.getOverlayTextureByIndex(0))
                        .put(TextureKey.of("up_ov"), currentRec.getOverlayTextureByIndex(1))
                        .put(TextureKey.of("north_ov"), currentRec.getOverlayTextureByIndex(2))
                        .put(TextureKey.of("south_ov"), currentRec.getOverlayTextureByIndex(3))
                        .put(TextureKey.of("west_ov"), currentRec.getOverlayTextureByIndex(4))
                        .put(TextureKey.of("east_ov"), currentRec.getOverlayTextureByIndex(5));
            }
        }

        return textureMap;
    }

    private static Identifier createModelIdentifier(String blockName, String type, String baseName, int setIdx) {
        StringBuilder path = new StringBuilder()
                .append(GENERATED_PATH)
                .append(blockName);

        if (type != null) {
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

        itemModelGenerator.register(
                currentBlock.asItem(),
                new Model(Optional.of(Identifier.of(WesterosBlocks.MOD_ID, path)), Optional.empty())
        );
    }
}