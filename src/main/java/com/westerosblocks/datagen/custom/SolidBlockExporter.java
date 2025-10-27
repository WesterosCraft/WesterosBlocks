package com.westerosblocks.datagen.custom;

import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;
import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.datagen.ModTextureMap;
import com.westerosblocks.block.custom.WCSolidBlock;
import com.westerosblocks.data.BlockDefinition;

import java.util.ArrayList;
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

        // Generate blockstate
        generateBlockState(generator, block, states, hasSymmetrical, hasRotateRandom);

        // Generate models
        for (int stateIdx = 0; stateIdx < states.size(); stateIdx++) {
            BlockDefinition.StateVariant state = states.get(stateIdx);
            String stateID = state.getStateID();
            String fname = (stateID == null) ? "base" : stateID;
            boolean isTinted = definition.isTinted() || state.hasOverlayTextures();
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
        String firstName = states.get(0).getStateID();
        firstName = (firstName == null) ? "base" : firstName;
        Identifier itemModelId = hasSymmetrical
            ? createNestedModelId(block, getModelName(firstName, 0, true))
            : createNestedModelId(block, getModelName(firstName, 0));
        registerParentedItemModel(generator, block, itemModelId);
    }


    private static String getModelName(String fname, int setIdx) {
        return fname + "_v" + (setIdx + 1);
    }

    private static String getModelName(String fname, int setIdx, boolean symmetrical) {
        String dir = symmetrical ? "symmetrical" : "asymmetrical";
        return dir + "/" + fname + "_v" + (setIdx + 1);
    }

    private static void generateBlockState(BlockStateModelGenerator generator, Block block,
            List<BlockDefinition.StateVariant> states, boolean hasSymmetrical, boolean hasRotateRandom) {

        if (hasSymmetrical) {
            // Build symmetrical blockstate
            List<BlockStateVariant> symVariants = new ArrayList<>();
            List<BlockStateVariant> asymVariants = new ArrayList<>();

            for (BlockDefinition.StateVariant state : states) {
                String stateID = state.getStateID();
                String fname = (stateID == null) ? "base" : stateID;

                for (int setIdx = 0; setIdx < state.getRandomTextureSetCount(); setIdx++) {
                    BlockDefinition.RandomTextureVariant set = state.getRandomTextureSet(setIdx);
                    if (set == null) continue;

                    int cnt = hasRotateRandom ? 4 : 1; // Matches: int cnt = sr.rotateRandom ? 4 : 1;
                    for (int i = 0; i < cnt; i++) {
                        int rotation = i * 90;
                        Identifier symModel = createNestedModelId(block, getModelName(fname, setIdx, true));
                        Identifier asymModel = createNestedModelId(block, getModelName(fname, setIdx, false));

                        symVariants.add(createWeightedVariant(symModel, rotation, set.getWeight()));
                        asymVariants.add(createWeightedVariant(asymModel, rotation, set.getWeight()));
                    }
                }
            }

            BlockStateVariantMap variantMap = BlockStateVariantMap.create(WCSolidBlock.SYMMETRICAL)
                .register(true, symVariants)
                .register(false, asymVariants);
            generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block).coordinate(variantMap));

        } else {
            // Build standard blockstate
            List<BlockStateVariant> allVariants = new ArrayList<>();

            for (BlockDefinition.StateVariant state : states) {
                String stateID = state.getStateID();
                String fname = (stateID == null) ? "base" : stateID;

                for (int setIdx = 0; setIdx < state.getRandomTextureSetCount(); setIdx++) {
                    BlockDefinition.RandomTextureVariant set = state.getRandomTextureSet(setIdx);
                    if (set == null) continue;

                    int cnt = hasRotateRandom ? 4 : 1;
                    for (int i = 0; i < cnt; i++) {
                        int rotation = i * 90;
                        Identifier modelId = createNestedModelId(block, getModelName(fname, setIdx));
                        allVariants.add(createWeightedVariant(modelId, rotation, set.getWeight()));
                    }
                }
            }

            generator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(block, allVariants.toArray(new BlockStateVariant[0])));
        }
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
            textureMap.put(TextureKey.of("down_ov"), createBlockIdentifier(getOverlayTextureByIndex(overlayTextures, 0)));
            textureMap.put(TextureKey.of("up_ov"), createBlockIdentifier(getOverlayTextureByIndex(overlayTextures, 1)));
            textureMap.put(TextureKey.of("north_ov"), createBlockIdentifier(getOverlayTextureByIndex(overlayTextures, 2)));
            textureMap.put(TextureKey.of("south_ov"), createBlockIdentifier(getOverlayTextureByIndex(overlayTextures, 3)));

            int westIdx = isSymmetrical ? 4 : 6;
            int eastIdx = isSymmetrical ? 5 : 7;
            textureMap.put(TextureKey.of("west_ov"), createBlockIdentifier(getOverlayTextureByIndex(overlayTextures, westIdx)));
            textureMap.put(TextureKey.of("east_ov"), createBlockIdentifier(getOverlayTextureByIndex(overlayTextures, eastIdx)));
        }

        String modelPath = isTinted ? "block/tinted/cube_overlay" : "block/untinted/cube_overlay";
        Model overlayModel = new Model(
            Optional.of(WesterosBlocks.id(modelPath)),
            Optional.empty(),
            TextureKey.DOWN, TextureKey.UP, TextureKey.NORTH, TextureKey.SOUTH, TextureKey.EAST, TextureKey.WEST,
            TextureKey.of("down_ov"), TextureKey.of("up_ov"), TextureKey.of("north_ov"),
            TextureKey.of("south_ov"), TextureKey.of("east_ov"), TextureKey.of("west_ov")
        );
        overlayModel.upload(modelId, textureMap, generator.modelCollector);
    }
}
