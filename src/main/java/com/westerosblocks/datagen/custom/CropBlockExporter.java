package com.westerosblocks.datagen.custom;

import com.westerosblocks.datagen.ModTextureKey;
import com.westerosblocks.datagen.ModTextureMap;
import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.utils.ModProperties;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class CropBlockExporter extends BaseBlockExporter {

    public static void registerCustomCropBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        boolean tinted = definition.isTinted() || definition.hasColorMult();
        boolean layerSensitive = definition.isLayerSensitive();
        boolean rotateRandom = definition.hasRotateRandom();
        var states = definition.getStates();

        if (states == null || states.isEmpty()) {
            throw new IllegalStateException("Crop block definition states should never be null/empty for block: " + getBlockName(block));
        }

        generateBlockState(generator, block, definition, states, layerSensitive, rotateRandom);

        for (BlockDefinition.StateVariant state : states) {
            if (state.isCustomModel()) continue;

            String stateID = state.getStateID();
            String baseName = getStateIdOrBase(stateID);

            for (int layer = 8; layer >= (layerSensitive ? 1 : 8); layer--) {
                String layerSuffix = layer != 8 ? "_layer" + layer : "";
                String modelName = baseName + layerSuffix;

                for (int setIdx = 0; setIdx < state.getRandomTextureSetCount(); setIdx++) {
                    generateCropModel(generator, block, modelName, state, setIdx, tinted, layer, layerSensitive);
                }
            }
        }

        BlockDefinition.StateVariant firstState = states.get(0);
        String firstName = getStateIdOrBase(firstState.getStateID());
        Identifier itemModelId = createGeneratedModelId(block, getModelName(firstName, 0));
        registerParentedItemModel(generator, block, itemModelId);
    }

    private static void generateBlockState(BlockStateModelGenerator generator, Block block,
                                          BlockDefinition definition, List<BlockDefinition.StateVariant> states,
                                          boolean layerSensitive, boolean rotateRandom) {
        int rotationCount = rotateRandom ? 4 : 1;
        ModProperties.StateProperty stateProperty = getStateProperty(block);
        boolean hasStateProperty = stateProperty != null && states.size() > 1;

        if (layerSensitive && hasStateProperty) {
            // DoubleProperty<Integer, String> on LAYERS + STATE
            generateLayerStateBlockState(generator, block, states, stateProperty, rotationCount);
        } else if (layerSensitive) {
            // SingleProperty<Integer> on LAYERS
            generateLayerBlockState(generator, block, states, rotationCount);
        } else if (hasStateProperty) {
            // SingleProperty<String> on STATE
            generateStateBlockState(generator, block, states, stateProperty, rotationCount);
        } else {
            // No properties — simple variant list
            generateSimpleBlockState(generator, block, states, rotationCount);
        }
    }

    private static void generateLayerStateBlockState(BlockStateModelGenerator generator, Block block,
                                                     List<BlockDefinition.StateVariant> states,
                                                     ModProperties.StateProperty stateProperty,
                                                     int rotationCount) {
        BlockStateVariantMap.DoubleProperty<Integer, String> variantMap =
            BlockStateVariantMap.create(Properties.LAYERS, stateProperty);

        // layers=8 first, then layers=1..7 (matches old layerConds order)
        int[] layerOrder = {8, 1, 2, 3, 4, 5, 6, 7};

        for (int layer : layerOrder) {
            for (BlockDefinition.StateVariant state : states) {
                String stateID = state.getStateID();
                if (stateID == null) continue;

                List<BlockStateVariant> variants = buildCropVariants(block, state, layer, rotationCount);
                if (variants.size() == 1) {
                    variantMap.register(layer, stateID, variants.get(0));
                } else if (!variants.isEmpty()) {
                    variantMap.register(layer, stateID, variants);
                }
            }
        }

        generator.blockStateCollector.accept(
            VariantsBlockStateSupplier.create(block).coordinate(variantMap)
        );
    }

    private static void generateLayerBlockState(BlockStateModelGenerator generator, Block block,
                                               List<BlockDefinition.StateVariant> states,
                                               int rotationCount) {
        BlockStateVariantMap.SingleProperty<Integer> variantMap =
            BlockStateVariantMap.create(Properties.LAYERS);

        int[] layerOrder = {8, 1, 2, 3, 4, 5, 6, 7};

        for (int layer : layerOrder) {
            List<BlockStateVariant> variants = new ArrayList<>();
            for (BlockDefinition.StateVariant state : states) {
                variants.addAll(buildCropVariants(block, state, layer, rotationCount));
            }
            if (variants.size() == 1) {
                variantMap.register(layer, variants.get(0));
            } else if (!variants.isEmpty()) {
                variantMap.register(layer, variants);
            }
        }

        generator.blockStateCollector.accept(
            VariantsBlockStateSupplier.create(block).coordinate(variantMap)
        );
    }

    private static void generateStateBlockState(BlockStateModelGenerator generator, Block block,
                                               List<BlockDefinition.StateVariant> states,
                                               ModProperties.StateProperty stateProperty,
                                               int rotationCount) {
        BlockStateVariantMap.SingleProperty<String> variantMap =
            BlockStateVariantMap.create(stateProperty);

        for (BlockDefinition.StateVariant state : states) {
            String stateID = state.getStateID();
            if (stateID == null) continue;

            List<BlockStateVariant> variants = buildCropVariants(block, state, 8, rotationCount);
            if (variants.size() == 1) {
                variantMap.register(stateID, variants.get(0));
            } else if (!variants.isEmpty()) {
                variantMap.register(stateID, variants);
            }
        }

        generator.blockStateCollector.accept(
            VariantsBlockStateSupplier.create(block).coordinate(variantMap)
        );
    }

    private static void generateSimpleBlockState(BlockStateModelGenerator generator, Block block,
                                                List<BlockDefinition.StateVariant> states,
                                                int rotationCount) {
        List<BlockStateVariant> variants = new ArrayList<>();
        for (BlockDefinition.StateVariant state : states) {
            variants.addAll(buildCropVariants(block, state, 8, rotationCount));
        }

        if (variants.size() == 1) {
            generator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(block, variants.get(0))
            );
        } else if (!variants.isEmpty()) {
            generator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(block, variants.toArray(new BlockStateVariant[0]))
            );
        }
    }

    private static List<BlockStateVariant> buildCropVariants(Block block, BlockDefinition.StateVariant state,
                                                            int layer, int rotationCount) {
        List<BlockStateVariant> variants = new ArrayList<>();
        String baseName = getStateIdOrBase(state.getStateID());
        String layerSuffix = layer != 8 ? "_layer" + layer : "";
        String modelName = baseName + layerSuffix;

        for (int setIdx = 0; setIdx < state.getRandomTextureSetCount(); setIdx++) {
            BlockDefinition.RandomTextureVariant set = state.getRandomTextureSet(setIdx);
            if (set == null || set.getTextureCount() == 0) continue;

            Identifier modelId = state.isCustomModel()
                ? createCustomModelId(block, getModelName(modelName, setIdx))
                : createGeneratedModelId(block, getModelName(modelName, setIdx));

            for (int rot = 0; rot < rotationCount; rot++) {
                BlockStateVariant variant = BlockStateVariant.create()
                    .put(VariantSettings.MODEL, modelId);

                if (set.getWeight() > 1) {
                    variant.put(VariantSettings.WEIGHT, set.getWeight());
                }
                if (rot > 0) {
                    variant.put(VariantSettings.Y, toRotation(90 * rot));
                }

                variants.add(variant);
            }
        }

        return variants;
    }

    private static void generateCropModel(BlockStateModelGenerator generator, Block block, String modelName,
                                         BlockDefinition.StateVariant state, int setIdx, boolean tinted,
                                         int layer, boolean layerSensitive) {
        BlockDefinition.RandomTextureVariant set = state.getRandomTextureSet(setIdx);
        if (set == null || set.getTextureCount() == 0) return;

        String fullModelName = getModelName(modelName, setIdx);
        Identifier modelId = createGeneratedModelId(block, fullModelName);

        String layerSuffix = (layerSensitive && layer != 8) ? "_layer" + layer : "";
        String parentPath = "crop" + layerSuffix;

        Model model = createTintedModel(tinted, parentPath, ModTextureKey.CROP, TextureKey.PARTICLE);
        TextureMap textureMap = ModTextureMap.cropTextures(set.getTextureByIndex(0));

        model.upload(modelId, textureMap, generator.modelCollector);
    }

    private static VariantSettings.Rotation toRotation(int degrees) {
        return switch (degrees) {
            case 90 -> VariantSettings.Rotation.R90;
            case 180 -> VariantSettings.Rotation.R180;
            case 270 -> VariantSettings.Rotation.R270;
            default -> VariantSettings.Rotation.R0;
        };
    }
}
