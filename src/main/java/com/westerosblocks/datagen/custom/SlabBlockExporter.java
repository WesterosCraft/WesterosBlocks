package com.westerosblocks.datagen.custom;

import com.westerosblocks.datagen.ModModels;
import com.westerosblocks.datagen.ModTextureKey;
import com.westerosblocks.datagen.ModTextureMap;
import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.utils.ModProperties;
import net.minecraft.block.Block;
import net.minecraft.block.enums.SlabType;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class SlabBlockExporter extends BaseBlockExporter {

    public static void registerCustomSlabBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        var states = definition.getStates();
        if (states == null || states.isEmpty()) {
            throw new IllegalStateException("Block definition states should never be null/empty after doInit() for block: " + getBlockName(block));
        }

        ModProperties.StateProperty stateProperty = getStateProperty(block);
        boolean hasMultipleStates = stateProperty != null && states.size() > 1;

        if (hasMultipleStates) {
            generateBlockStateWithStates(generator, block, states, stateProperty);
        } else {
            generateBlockState(generator, block, states);
        }

        for (int stateIdx = 0; stateIdx < states.size(); stateIdx++) {
            BlockDefinition.StateVariant state = states.get(stateIdx);
            String stateID = state.getStateID();
            String fname = getStateIdOrBase(stateID);

            for (int setIdx = 0; setIdx < state.getRandomTextureSetCount(); setIdx++) {
                if (definition.hasCustomModel() || state.isCustomModel()) {
                    continue;
                } else {
                    generateSlabModels(generator, block, fname, setIdx, state, definition);
                }
            }
        }

        BlockDefinition.StateVariant firstState = states.get(0);
        String firstName = getStateIdOrBase(firstState.getStateID());
        Identifier itemModelId = createNestedModelId(block, getModelName(firstName, 0, "bottom"));
        registerParentedItemModel(generator, block, itemModelId);
    }

    private static void generateBlockState(BlockStateModelGenerator generator, Block block,
                                          List<BlockDefinition.StateVariant> states) {
        BlockStateVariantMap.SingleProperty<SlabType> variantMap =
            BlockStateVariantMap.create(Properties.SLAB_TYPE);

        List<BlockStateVariant> bottomVariants = new ArrayList<>();
        List<BlockStateVariant> topVariants = new ArrayList<>();
        List<BlockStateVariant> doubleVariants = new ArrayList<>();

        for (BlockDefinition.StateVariant state : states) {
            String fname = getStateIdOrBase(state.getStateID());

            for (int setIdx = 0; setIdx < state.getRandomTextureSetCount(); setIdx++) {
                BlockDefinition.RandomTextureVariant set = state.getRandomTextureSet(setIdx);
                if (set == null) continue;

                Identifier bottomModel = createNestedModelId(block, getModelName(fname, setIdx, "bottom"));
                Identifier topModel = createNestedModelId(block, getModelName(fname, setIdx, "top"));
                Identifier doubleModel = createNestedModelId(block, getModelName(fname, setIdx, "double"));

                int weight = set.getWeight();
                bottomVariants.add(createSlabVariant(bottomModel, weight));
                topVariants.add(createSlabVariant(topModel, weight));
                doubleVariants.add(createSlabVariant(doubleModel, weight));
            }
        }

        if (bottomVariants.size() == 1) {
            variantMap.register(SlabType.BOTTOM, bottomVariants.get(0));
            variantMap.register(SlabType.TOP, topVariants.get(0));
            variantMap.register(SlabType.DOUBLE, doubleVariants.get(0));
        } else {
            variantMap.register(SlabType.BOTTOM, bottomVariants);
            variantMap.register(SlabType.TOP, topVariants);
            variantMap.register(SlabType.DOUBLE, doubleVariants);
        }

        generator.blockStateCollector.accept(
            VariantsBlockStateSupplier.create(block).coordinate(variantMap)
        );
    }

    private static void generateBlockStateWithStates(BlockStateModelGenerator generator, Block block,
                                                     List<BlockDefinition.StateVariant> states,
                                                     ModProperties.StateProperty stateProperty) {
        BlockStateVariantMap.DoubleProperty<SlabType, String> variantMap =
            BlockStateVariantMap.create(Properties.SLAB_TYPE, stateProperty);

        for (BlockDefinition.StateVariant state : states) {
            String stateId = state.getStateID();
            String fname = getStateIdOrBase(stateId);

            List<BlockStateVariant> bottomVariants = new ArrayList<>();
            List<BlockStateVariant> topVariants = new ArrayList<>();
            List<BlockStateVariant> doubleVariants = new ArrayList<>();

            for (int setIdx = 0; setIdx < state.getRandomTextureSetCount(); setIdx++) {
                BlockDefinition.RandomTextureVariant set = state.getRandomTextureSet(setIdx);
                if (set == null) continue;

                Identifier bottomModel = createNestedModelId(block, getModelName(fname, setIdx, "bottom"));
                Identifier topModel = createNestedModelId(block, getModelName(fname, setIdx, "top"));
                Identifier doubleModel = createNestedModelId(block, getModelName(fname, setIdx, "double"));

                int weight = set.getWeight();
                bottomVariants.add(createSlabVariant(bottomModel, weight));
                topVariants.add(createSlabVariant(topModel, weight));
                doubleVariants.add(createSlabVariant(doubleModel, weight));
            }

            if (bottomVariants.size() == 1) {
                variantMap.register(SlabType.BOTTOM, stateId, bottomVariants.get(0));
                variantMap.register(SlabType.TOP, stateId, topVariants.get(0));
                variantMap.register(SlabType.DOUBLE, stateId, doubleVariants.get(0));
            } else {
                variantMap.register(SlabType.BOTTOM, stateId, bottomVariants);
                variantMap.register(SlabType.TOP, stateId, topVariants);
                variantMap.register(SlabType.DOUBLE, stateId, doubleVariants);
            }
        }

        generator.blockStateCollector.accept(
            VariantsBlockStateSupplier.create(block).coordinate(variantMap)
        );
    }

    private static BlockStateVariant createSlabVariant(Identifier modelId, int weight) {
        BlockStateVariant variant = BlockStateVariant.create().put(VariantSettings.MODEL, modelId);
        if (weight > 1) {
            variant.put(VariantSettings.WEIGHT, weight);
        }
        return variant;
    }

    private static void generateSlabModels(BlockStateModelGenerator generator, Block block,
                                          String fname, int setIdx, BlockDefinition.StateVariant state,
                                          BlockDefinition definition) {
        BlockDefinition.RandomTextureVariant set = state.getRandomTextureSet(setIdx);
        if (set == null || set.getTextureCount() == 0) {
            generateSlabModelsWithTextures(generator, block, fname, setIdx,
                new String[]{"missing"}, false, false, null);
            return;
        }

        String[] textures = new String[set.getTextureCount()];
        for (int i = 0; i < set.getTextureCount(); i++) {
            textures[i] = set.getTextureByIndex(i);
        }

        boolean isTinted = definition.isTinted() || definition.hasColorMult();
        boolean isOverlay = state.hasOverlayTextures();

        generateSlabModelsWithTextures(generator, block, fname, setIdx, textures, isTinted, isOverlay,
                isOverlay ? state.getOverlayTextures() : null);
    }

    private static void generateSlabModelsWithTextures(BlockStateModelGenerator generator, Block block,
                                                       String fname, int setIdx, String[] textures,
                                                       boolean isTinted, boolean isOverlay,
                                                       List<String> overlayTextures) {
        String[] filledTextures = fillTextureArray(textures);
        TextureMap textureMap = ModTextureMap.customAllSides(filledTextures);

        if (isOverlay && overlayTextures != null && !overlayTextures.isEmpty()) {
            textureMap.put(ModTextureKey.DOWN_OVERLAY, createBlockIdentifier(getOverlayTextureByIndex(overlayTextures, 0)));
            textureMap.put(ModTextureKey.UP_OVERLAY, createBlockIdentifier(getOverlayTextureByIndex(overlayTextures, 1)));
            textureMap.put(ModTextureKey.NORTH_OVERLAY, createBlockIdentifier(getOverlayTextureByIndex(overlayTextures, 2)));
            textureMap.put(ModTextureKey.SOUTH_OVERLAY, createBlockIdentifier(getOverlayTextureByIndex(overlayTextures, 3)));
            textureMap.put(ModTextureKey.WEST_OVERLAY, createBlockIdentifier(getOverlayTextureByIndex(overlayTextures, 4)));
            textureMap.put(ModTextureKey.EAST_OVERLAY, createBlockIdentifier(getOverlayTextureByIndex(overlayTextures, 5)));
        }

        Model bottomModel, topModel, doubleModel;
        if (isOverlay) {
            bottomModel = isTinted ? ModModels.SLAB_BOTTOM_OVERLAY_TINTED : ModModels.SLAB_BOTTOM_OVERLAY_UNTINTED;
            topModel = isTinted ? ModModels.SLAB_TOP_OVERLAY_TINTED : ModModels.SLAB_TOP_OVERLAY_UNTINTED;
            doubleModel = isTinted ? ModModels.CUBE_OVERLAY_TINTED : ModModels.CUBE_OVERLAY_UNTINTED;
        } else if (isTinted) {
            bottomModel = ModModels.SLAB_BOTTOM_TINTED;
            topModel = ModModels.SLAB_TOP_TINTED;
            doubleModel = ModModels.CUBE_TINTED;
        } else {
            bottomModel = ModModels.SLAB_BOTTOM;
            topModel = ModModels.SLAB_TOP;
            doubleModel = Models.CUBE;
        }

        Identifier bottomModelId = createNestedModelId(block, getModelName(fname, setIdx, "bottom"));
        bottomModel.upload(bottomModelId, textureMap, generator.modelCollector);

        Identifier topModelId = createNestedModelId(block, getModelName(fname, setIdx, "top"));
        topModel.upload(topModelId, textureMap, generator.modelCollector);

        Identifier doubleModelId = createNestedModelId(block, getModelName(fname, setIdx, "double"));
        doubleModel.upload(doubleModelId, textureMap, generator.modelCollector);
    }

    protected static String getModelName(String fname, int setIdx, String variant) {
        if (setIdx == 0 && fname.equals("base")) {
            return variant;
        }
        return fname + "_v" + (setIdx + 1) + "_" + variant;
    }
}
