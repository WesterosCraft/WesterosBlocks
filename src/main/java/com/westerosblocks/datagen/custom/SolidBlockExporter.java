package com.westerosblocks.datagen.custom;

import com.westerosblocks.datagen.ModTextureKey;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;
import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.datagen.ModTextureMap;
import com.westerosblocks.block.custom.WCSolidBlock;
import com.westerosblocks.data.BlockDefinition;

import java.util.List;
import java.util.Optional;

public class SolidBlockExporter extends BaseBlockExporter {

    private static boolean isSymmetrical(Block block) {
        return (block instanceof WCSolidBlock) && ((WCSolidBlock) block).symmetrical;
    }

    public static void registerCustomSolidBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        var states = definition.getStates();
        if (states == null || states.isEmpty()) {
            return;
        }

        boolean hasSymmetrical = isSymmetrical(block);
        boolean hasRotateRandom = definition.hasRotateRandom();

        // Generate blockstates
        generateBlockState(generator, block, states, hasSymmetrical, hasRotateRandom);

        // Generate models
        for (int stateIdx = 0; stateIdx < states.size(); stateIdx++) {
            BlockDefinition.StateVariant state = states.get(stateIdx);
            String stateID = state.getStateID();
            String fname = getStateIdOrBase(stateID);
            boolean isTinted = definition.isTinted() || definition.hasColorMult();
            boolean isOverlay = state.hasOverlayTextures();

            for (int setIdx = 0; setIdx < state.getRandomTextureSetCount(); setIdx++) {
                if (hasSymmetrical) {
                    generateSolidModel(generator, block, getModelName(fname, setIdx, true),
                        state, setIdx, isTinted, isOverlay, true);
                    generateSolidModel(generator, block, getModelName(fname, setIdx, false),
                        state, setIdx, isTinted, isOverlay, false);
                } else {
                    generateSolidModel(generator, block, getModelName(fname, setIdx),
                        state, setIdx, isTinted, isOverlay, false);
                }
            }
        }

        // Item model
        String firstName = getStateIdOrBase(states.get(0).getStateID());
        Identifier itemModelId = hasSymmetrical
            ? createNestedModelId(block, getModelName(firstName, 0, true))
            : createNestedModelId(block, getModelName(firstName, 0));
        registerParentedItemModel(generator, block, itemModelId);
    }


    private static String getModelName(String fname, int setIdx, boolean symmetrical) {
        String dir = symmetrical ? "symmetrical" : "asymmetrical";
        return dir + "/" + getModelName(fname, setIdx);
    }

    private static void generateBlockState(BlockStateModelGenerator generator, Block block,
            List<BlockDefinition.StateVariant> states, boolean hasSymmetrical, boolean hasRotateRandom) {

        var blockStateProperty = getStateProperty(block);

        boolean hasMultipleStates = blockStateProperty != null && states.size() > 1;

        BlockStateBuilder builder = new BlockStateBuilder(block, blockStateProperty);

        for (BlockDefinition.StateVariant state : states) {
            String stateID = state.getStateID();
            String fname = getStateIdOrBase(stateID);

            String builderStateID = hasMultipleStates ? stateID : null;

            for (int setIdx = 0; setIdx < state.getRandomTextureSetCount(); setIdx++) {
                BlockDefinition.RandomTextureVariant set = state.getRandomTextureSet(setIdx);
                if (set == null) continue;

                int cnt = hasRotateRandom ? 4 : 1;
                for (int i = 0; i < cnt; i++) {
                    int rotation = i * 90;
                    int weight = set.getWeight();

                    if (hasSymmetrical) {
                        Identifier symModel = createNestedModelId(block, getModelName(fname, setIdx, true));
                        builder.addVariant("symmetrical=true", createWeightedVariant(symModel, rotation, weight), builderStateID);

                        Identifier asymModel = createNestedModelId(block, getModelName(fname, setIdx, false));
                        builder.addVariant("symmetrical=false", createWeightedVariant(asymModel, rotation, weight), builderStateID);
                    } else {
                        Identifier modelId = createNestedModelId(block, getModelName(fname, setIdx));
                        builder.addVariant("", createWeightedVariant(modelId, rotation, weight), builderStateID);
                    }
                }
            }
        }

        builder.register(generator);
    }

    private static void generateSolidModel(BlockStateModelGenerator generator, Block block, String modelName,
            BlockDefinition.StateVariant state, int setIdx, boolean isTinted, boolean isOverlay, boolean isSymmetrical) {

        BlockDefinition.RandomTextureVariant set = state.getRandomTextureSet(setIdx);
        if (set == null) return;
        Identifier modelId = createNestedModelId(block, modelName);

        if (isOverlay) {
            generateOverlayModel(generator, modelId, set, state.getOverlayTextures(), isTinted, isSymmetrical);
        } else if (set.getTextureCount() > 1 || isTinted) {
            generateCubeModel(generator, modelId, set, isTinted, isSymmetrical);
        } else {
            generateCubeAllModel(generator, modelId, set);
        }
    }

    private static void generateCubeAllModel(BlockStateModelGenerator generator, Identifier modelId,
            BlockDefinition.RandomTextureVariant set) {
        TextureMap textureMap = new TextureMap()
            .put(TextureKey.ALL, createBlockIdentifier(set.getTextureByIndex(0)));
        Models.CUBE_ALL.upload(modelId, textureMap, generator.modelCollector);
    }

    private static void generateCubeModel(BlockStateModelGenerator generator, Identifier modelId,
            BlockDefinition.RandomTextureVariant set, boolean isTinted, boolean isSymmetrical) {

        String down = set.getTextureByIndex(0);
        String up = set.getTextureByIndex(1);
        String north = set.getTextureByIndex(2);
        String south = set.getTextureByIndex(3);
        String west = isSymmetrical ? set.getTextureByIndex(4) : set.getTextureByIndex(6);
        String east = isSymmetrical ? set.getTextureByIndex(5) : set.getTextureByIndex(7);

        String[] textures = fillTextureArray(new String[]{down, up, north, south, west, east});
        TextureMap textureMap = ModTextureMap.customAllSides(textures);

        if (isTinted) {
            Model tintedModel = new Model(
                Optional.of(WesterosBlocks.id("block/tinted/cube")),
                Optional.empty(),
                TextureKey.DOWN, TextureKey.UP, TextureKey.NORTH, TextureKey.SOUTH, TextureKey.EAST, TextureKey.WEST
            );
            tintedModel.upload(modelId, textureMap, generator.modelCollector);
        } else {
            Models.CUBE.upload(modelId, textureMap, generator.modelCollector);
        }
    }

    private static String getOverlayTextureByIndex(List<String> overlayTextures, int index) {
        if (overlayTextures == null || overlayTextures.isEmpty()) {
            return null;
        }
        if (index >= overlayTextures.size()) {
            index = overlayTextures.size() - 1;
        }
        return overlayTextures.get(index);
    }

    private static void generateOverlayModel(BlockStateModelGenerator generator, Identifier modelId,
            BlockDefinition.RandomTextureVariant set, List<String> overlayTextures, boolean isTinted, boolean isSymmetrical) {

        String down = set.getTextureByIndex(0);
        String up = set.getTextureByIndex(1);
        String north = set.getTextureByIndex(2);
        String south = set.getTextureByIndex(3);
        String west = isSymmetrical ? set.getTextureByIndex(4) : set.getTextureByIndex(6);
        String east = isSymmetrical ? set.getTextureByIndex(5) : set.getTextureByIndex(7);

        String[] textures = fillTextureArray(new String[]{down, up, north, south, west, east});
        TextureMap textureMap = ModTextureMap.customAllSides(textures);

        if (overlayTextures != null && !overlayTextures.isEmpty()) {
            textureMap.put(ModTextureKey.DOWN_OVERLAY, createBlockIdentifier(getOverlayTextureByIndex(overlayTextures, 0)));
            textureMap.put(ModTextureKey.UP_OVERLAY, createBlockIdentifier(getOverlayTextureByIndex(overlayTextures, 1)));
            textureMap.put(ModTextureKey.NORTH_OVERLAY, createBlockIdentifier(getOverlayTextureByIndex(overlayTextures, 2)));
            textureMap.put(ModTextureKey.SOUTH_OVERLAY, createBlockIdentifier(getOverlayTextureByIndex(overlayTextures, 3)));

            int westIdx = isSymmetrical ? 4 : 6;
            int eastIdx = isSymmetrical ? 5 : 7;
            textureMap.put(ModTextureKey.WEST_OVERLAY, createBlockIdentifier(getOverlayTextureByIndex(overlayTextures, westIdx)));
            textureMap.put(ModTextureKey.EAST_OVERLAY, createBlockIdentifier(getOverlayTextureByIndex(overlayTextures, eastIdx)));
        }

        String modelPath = isTinted ? "block/tinted/cube_overlay" : "block/untinted/cube_overlay";
        Model overlayModel = new Model(
            Optional.of(WesterosBlocks.id(modelPath)),
            Optional.empty(),
            TextureKey.DOWN, TextureKey.UP, TextureKey.NORTH, TextureKey.SOUTH, TextureKey.EAST, TextureKey.WEST,
                ModTextureKey.DOWN_OVERLAY, ModTextureKey.UP_OVERLAY, ModTextureKey.NORTH_OVERLAY,
                ModTextureKey.SOUTH_OVERLAY, ModTextureKey.EAST_OVERLAY, ModTextureKey.WEST_OVERLAY
        );
        overlayModel.upload(modelId, textureMap, generator.modelCollector);
    }
}
