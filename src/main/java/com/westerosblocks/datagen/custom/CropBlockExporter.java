package com.westerosblocks.datagen.custom;

import com.westerosblocks.datagen.ModTextureKey;
import com.westerosblocks.datagen.ModTextureMap;
import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.utils.ModProperties;
import net.minecraft.block.Block;
import net.minecraft.client.data.*;
import net.minecraft.client.render.model.json.ModelVariantOperator;
import net.minecraft.util.math.AxisRotation;
import net.minecraft.client.render.model.json.WeightedVariant;
import net.minecraft.client.render.model.json.ModelVariant;
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
        BlockStateVariantMap.DoubleProperty<WeightedVariant, Integer, String> variantMap =
            BlockStateVariantMap.models(Properties.LAYERS, stateProperty);

        // layers=8 first, then layers=1..7 (matches old layerConds order)
        int[] layerOrder = {8, 1, 2, 3, 4, 5, 6, 7};

        for (int layer : layerOrder) {
            for (BlockDefinition.StateVariant state : states) {
                String stateID = state.getStateID();
                if (stateID == null) continue;

                List<WeightedVariant> variants = buildCropVariants(block, state, layer, rotationCount);
                if (variants.size() == 1) {
                    variantMap.register(layer, stateID, variants.get(0));
                } else if (!variants.isEmpty()) {
                    variantMap.register(layer, stateID, mergeVariants(variants));
                }
            }
        }

        generator.blockStateCollector.accept(
            VariantsBlockModelDefinitionCreator.of(block).with(variantMap)
        );
    }

    private static void generateLayerBlockState(BlockStateModelGenerator generator, Block block,
                                               List<BlockDefinition.StateVariant> states,
                                               int rotationCount) {
        BlockStateVariantMap.SingleProperty<WeightedVariant, Integer> variantMap =
            BlockStateVariantMap.models(Properties.LAYERS);

        int[] layerOrder = {8, 1, 2, 3, 4, 5, 6, 7};

        for (int layer : layerOrder) {
            List<WeightedVariant> variants = new ArrayList<>();
            for (BlockDefinition.StateVariant state : states) {
                variants.addAll(buildCropVariants(block, state, layer, rotationCount));
            }
            if (variants.size() == 1) {
                variantMap.register(layer, variants.get(0));
            } else if (!variants.isEmpty()) {
                variantMap.register(layer, mergeVariants(variants));
            }
        }

        generator.blockStateCollector.accept(
            VariantsBlockModelDefinitionCreator.of(block).with(variantMap)
        );
    }

    private static void generateStateBlockState(BlockStateModelGenerator generator, Block block,
                                               List<BlockDefinition.StateVariant> states,
                                               ModProperties.StateProperty stateProperty,
                                               int rotationCount) {
        BlockStateVariantMap.SingleProperty<WeightedVariant, String> variantMap =
            BlockStateVariantMap.models(stateProperty);

        for (BlockDefinition.StateVariant state : states) {
            String stateID = state.getStateID();
            if (stateID == null) continue;

            List<WeightedVariant> variants = buildCropVariants(block, state, 8, rotationCount);
            if (variants.size() == 1) {
                variantMap.register(stateID, variants.get(0));
            } else if (!variants.isEmpty()) {
                variantMap.register(stateID, mergeVariants(variants));
            }
        }

        generator.blockStateCollector.accept(
            VariantsBlockModelDefinitionCreator.of(block).with(variantMap)
        );
    }

    private static void generateSimpleBlockState(BlockStateModelGenerator generator, Block block,
                                                List<BlockDefinition.StateVariant> states,
                                                int rotationCount) {
        List<WeightedVariant> variants = new ArrayList<>();
        for (BlockDefinition.StateVariant state : states) {
            variants.addAll(buildCropVariants(block, state, 8, rotationCount));
        }

        if (!variants.isEmpty()) {
            generator.blockStateCollector.accept(
                VariantsBlockModelDefinitionCreator.of(block, mergeVariants(variants))
            );
        }
    }

    private static List<WeightedVariant> buildCropVariants(Block block, BlockDefinition.StateVariant state,
                                                            int layer, int rotationCount) {
        List<WeightedVariant> variants = new ArrayList<>();
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
                variants.add(createWeightedVariant(modelId, rot * 90, set.getWeight()));
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

    private static AxisRotation toRotation(int degrees) {
        return switch (degrees) {
            case 90 -> AxisRotation.R90;
            case 180 -> AxisRotation.R180;
            case 270 -> AxisRotation.R270;
            default -> AxisRotation.R0;
        };
    }
}
