package com.westerosblocks.datagen.custom;

import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.utils.ModProperties;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cuboid16WayBlockExporter extends CuboidBlockExporter {

    private static final String[] MODEL_SUFFIXES = {"", "_rotn22", "_rotn45", "_rot22"};
    private static final Float[] MODEL_ROTATIONS = {null, -22.5f, -45f, 22.5f};

    private record ModelSet16Way(Identifier model, int weight) {}

    public static void registerCustomCuboid16WayBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        var states = definition.getStates();

        if (states == null || states.isEmpty()) {
            throw new IllegalStateException("Block definition states should never be null/empty after doInit() for block: " + getBlockName(block));
        }

        boolean hasMultipleStates = definition.getStateCount() > 1;

        if (definition.hasCustomModel() && !hasMultipleStates) {
            registerCustomModelCuboid16WayBlock(generator, block, definition);
            return;
        }

        // Collect model sets: stateId -> rotationSuffix -> List<ModelSet16Way>
        Map<String, Map<String, List<ModelSet16Way>>> stateRotationModelSets = new HashMap<>();
        Identifier firstModel = null;

        for (BlockDefinition.StateVariant state : states) {
            String stateId = state.getStateID();
            if (stateId == null) stateId = "base";

            Map<String, List<ModelSet16Way>> rotationModelSets = new HashMap<>();
            int textureSetCount = state.getRandomTextureSetCount();

            if (textureSetCount == 0) {
                if (state.isCustomModel()) {
                    textureSetCount = 1;
                } else {
                    continue;
                }
            }

            for (int rotIdx = 0; rotIdx < MODEL_SUFFIXES.length; rotIdx++) {
                List<ModelSet16Way> modelSets = new ArrayList<>();

                for (int setIdx = 0; setIdx < textureSetCount; setIdx++) {
                    Identifier modelId;
                    String variantName = (hasMultipleStates ? stateId : "base") + MODEL_SUFFIXES[rotIdx] + "_v" + (setIdx + 1);
                    int weight = 1;

                    if (state.isCustomModel()) {
                        modelId = createCustomModelId(block, variantName);
                        BlockDefinition.RandomTextureVariant set = state.getRandomTextureSet(setIdx);
                        if (set != null) {
                            weight = set.getWeight();
                        }
                    } else {
                        BlockDefinition.RandomTextureVariant set = state.getRandomTextureSet(setIdx);
                        if (set == null || set.getTextureCount() == 0) continue;

                        String[] textures = new String[set.getTextureCount()];
                        for (int i = 0; i < set.getTextureCount(); i++) {
                            textures[i] = set.getTextureByIndex(i);
                        }

                        List<String> textureList = java.util.Arrays.asList(textures);
                        modelId = CuboidModelBuilder.createCuboidModel(generator, block, definition, textureList, setIdx, variantName, MODEL_ROTATIONS[rotIdx]);
                        weight = set.getWeight();
                    }

                    modelSets.add(new ModelSet16Way(modelId, weight));
                    if (firstModel == null) firstModel = modelId;
                }

                if (!modelSets.isEmpty()) {
                    rotationModelSets.put(MODEL_SUFFIXES[rotIdx], modelSets);
                }
            }

            if (!rotationModelSets.isEmpty()) {
                stateRotationModelSets.put(stateId, rotationModelSets);
            }
        }

        if (stateRotationModelSets.isEmpty()) {
            registerFallbackCuboid16WayBlock(generator, block, definition);
            return;
        }

        ModProperties.StateProperty stateProperty = getStateProperty(block);
        if (hasMultipleStates && stateProperty != null) {
            generateBlockStateWithStates(generator, block, stateRotationModelSets, stateProperty);
        } else {
            Map<String, List<ModelSet16Way>> rotationModelSets = stateRotationModelSets.values().iterator().next();
            generateBlockState(generator, block, rotationModelSets);
        }

        if (firstModel != null) {
            registerParentedItemModel(generator, block, firstModel);
        }
    }

    private static void registerCustomModelCuboid16WayBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        Map<String, List<ModelSet16Way>> rotationModelSets = new HashMap<>();

        for (int i = 0; i < MODEL_SUFFIXES.length; i++) {
            Identifier modelId = createCustomModelId(block, "base" + MODEL_SUFFIXES[i] + "_v1");
            rotationModelSets.put(MODEL_SUFFIXES[i], List.of(new ModelSet16Way(modelId, 1)));
        }

        generateBlockState(generator, block, rotationModelSets);
        registerParentedItemModel(generator, block, rotationModelSets.get("").get(0).model());
    }

    private static void registerFallbackCuboid16WayBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        List<String> fallbackTextures = List.of("missing");
        Map<String, List<ModelSet16Way>> rotationModelSets = new HashMap<>();

        for (int i = 0; i < MODEL_SUFFIXES.length; i++) {
            String modelName = "base" + MODEL_SUFFIXES[i] + "_v1";
            Identifier modelId = CuboidModelBuilder.createCuboidModel(generator, block, definition, fallbackTextures, 0, modelName, MODEL_ROTATIONS[i]);
            rotationModelSets.put(MODEL_SUFFIXES[i], List.of(new ModelSet16Way(modelId, 1)));
        }

        generateBlockState(generator, block, rotationModelSets);
        registerParentedItemModel(generator, block, rotationModelSets.get("").get(0).model());
    }

    // Single-state: SingleProperty<Integer> on ROTATION
    private static void generateBlockState(BlockStateModelGenerator generator, Block block,
                                          Map<String, List<ModelSet16Way>> rotationModelSets) {
        BlockStateVariantMap.SingleProperty<Integer> variantMap =
            BlockStateVariantMap.create(Properties.ROTATION);

        for (int rotation = 0; rotation < 16; rotation++) {
            int modelIndex = rotation % 4;
            List<ModelSet16Way> modelSets = rotationModelSets.get(MODEL_SUFFIXES[modelIndex]);
            int yRotation = (90 * (((rotation + 1) % 16) / 4)) % 360;

            List<BlockStateVariant> variants = buildRotationVariants(modelSets, yRotation);
            if (variants.size() == 1) {
                variantMap.register(rotation, variants.get(0));
            } else {
                variantMap.register(rotation, variants);
            }
        }

        generator.blockStateCollector.accept(
            VariantsBlockStateSupplier.create(block).coordinate(variantMap)
        );
    }

    // Multi-state: DoubleProperty<Integer, String> on ROTATION + STATE
    private static void generateBlockStateWithStates(BlockStateModelGenerator generator, Block block,
                                                     Map<String, Map<String, List<ModelSet16Way>>> stateRotationModelSets,
                                                     ModProperties.StateProperty stateProperty) {
        BlockStateVariantMap.DoubleProperty<Integer, String> variantMap =
            BlockStateVariantMap.create(Properties.ROTATION, stateProperty);

        for (Map.Entry<String, Map<String, List<ModelSet16Way>>> stateEntry : stateRotationModelSets.entrySet()) {
            String stateId = stateEntry.getKey();
            Map<String, List<ModelSet16Way>> rotationModelSets = stateEntry.getValue();

            for (int rotation = 0; rotation < 16; rotation++) {
                int modelIndex = rotation % 4;
                List<ModelSet16Way> modelSets = rotationModelSets.get(MODEL_SUFFIXES[modelIndex]);
                int yRotation = (90 * (((rotation + 1) % 16) / 4)) % 360;

                if (modelSets == null || modelSets.isEmpty()) continue;

                List<BlockStateVariant> variants = buildRotationVariants(modelSets, yRotation);
                if (variants.size() == 1) {
                    variantMap.register(rotation, stateId, variants.get(0));
                } else {
                    variantMap.register(rotation, stateId, variants);
                }
            }
        }

        generator.blockStateCollector.accept(
            VariantsBlockStateSupplier.create(block).coordinate(variantMap)
        );
    }

    private static List<BlockStateVariant> buildRotationVariants(List<ModelSet16Way> modelSets, int yRotation) {
        List<BlockStateVariant> variants = new ArrayList<>();

        for (ModelSet16Way modelSet : modelSets) {
            BlockStateVariant variant = BlockStateVariant.create()
                .put(VariantSettings.MODEL, modelSet.model());

            if (yRotation > 0) {
                variant.put(VariantSettings.Y, toRotation(yRotation));
            }
            if (modelSet.weight() > 1) {
                variant.put(VariantSettings.WEIGHT, modelSet.weight());
            }

            variants.add(variant);
        }

        return variants;
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
