package com.westerosblocks.datagen.custom;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.BlockStateSupplier;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Exporter for 16-way rotation cuboid blocks.
 *
 */
public class Cuboid16WayBlockExporter extends BaseBlockExporter {

    private static final String[] MODEL_SUFFIXES = {"", "_rotn22", "_rotn45", "_rot22"};
    private static final Float[] MODEL_ROTATIONS = {null, -22.5f, -45f, 22.5f};

    /**
     * Registers a 16-way rotation cuboid block from a BlockDefinition.
     * Uses uniform iteration pattern: After doInit(), states is ALWAYS non-empty,
     * and each state has randomTextures normalized from simple textures.
     * Generates 4 rotation offset models (0°, -22.5°, -45°, 22.5°) for 16-way rotation.
     */
    public static void registerCustomCuboid16WayBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        // After doInit(), states is ALWAYS non-empty (at least synthetic base state exists)
        var states = definition.getStates();

        if (states == null || states.isEmpty()) {
            throw new IllegalStateException("Block definition states should never be null/empty after doInit() for block: " + getBlockName(block));
        }

        // Determine if this block actually has multiple states (needs STATE property in variants)
        boolean hasMultipleStates = definition.getStateCount() > 1;

        // Check for custom model first - but only if it's a single state block
        // Multi-state blocks with custom models need per-state iteration
        if (definition.hasCustomModel() && !hasMultipleStates) {
            registerCustomModelCuboid16WayBlock(generator, block, definition);
            return;
        }

        // Collect all model identifiers for all states
        // Structure: stateId -> rotationSuffix -> List<ModelSet16Way>
        Map<String, Map<String, List<ModelSet16Way>>> stateRotationModelSets = new HashMap<>();
        Identifier firstModel = null;

        for (BlockDefinition.StateVariant state : states) {
            String stateId = state.getStateID();
            if (stateId == null) stateId = "base";

            Map<String, List<ModelSet16Way>> rotationModelSets = new HashMap<>();

            // Check if we have texture sets to work with
            int textureSetCount = state.getRandomTextureSetCount();

            // For custom model states with no textures, ensure at least one iteration
            if (textureSetCount == 0) {
                if (state.isCustomModel()) {
                    textureSetCount = 1;  // Force one iteration for custom model reference
                } else {
                    continue;  // Skip non-custom-model states with no textures
                }
            }

            // For each of the 4 rotation types
            for (int rotIdx = 0; rotIdx < MODEL_SUFFIXES.length; rotIdx++) {
                List<ModelSet16Way> modelSets = new ArrayList<>();

                // Iterate through all texture sets for this state
                for (int setIdx = 0; setIdx < textureSetCount; setIdx++) {
                    Identifier modelId;
                    String variantName = (hasMultipleStates ? stateId : "base") + MODEL_SUFFIXES[rotIdx] + "_v" + (setIdx + 1);
                    int weight = 1;

                    // Check if this state uses custom models
                    if (state.isCustomModel()) {
                        // Use custom model reference instead of generating from textures
                        modelId = createCustomModelId(block, variantName);
                        BlockDefinition.RandomTextureVariant set = state.getRandomTextureSet(setIdx);
                        if (set != null) {
                            weight = set.getWeight();
                        }
                    } else {
                        BlockDefinition.RandomTextureVariant set = state.getRandomTextureSet(setIdx);
                        if (set == null || set.getTextureCount() == 0) {
                            continue;
                        }

                        // Extract textures from this set
                        String[] textures = new String[set.getTextureCount()];
                        for (int i = 0; i < set.getTextureCount(); i++) {
                            textures[i] = set.getTextureByIndex(i);
                        }

                        // Convert to List for compatibility with existing helper methods
                        List<String> textureList = java.util.Arrays.asList(textures);

                        // Generate model with rotation offset
                        modelId = CuboidBlockExporter.createCuboidModel(generator, block, definition, textureList, setIdx, variantName, MODEL_ROTATIONS[rotIdx]);
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
            // Fallback if no valid models generated
            registerFallbackCuboid16WayBlock(generator, block, definition);
            return;
        }

        // Generate blockstate based on whether we have multiple states
        if (hasMultipleStates) {
            // Multiple states - need "state=" prefix
            generator.blockStateCollector.accept(createCuboid16WayBlockStateWithStates(block, stateRotationModelSets));
        } else {
            // Single state - no state prefix in variants, just rotation
            Map<String, List<ModelSet16Way>> rotationModelSets = stateRotationModelSets.values().iterator().next();
            generator.blockStateCollector.accept(createCuboid16WayBlockStateWithRandomTextures(block, rotationModelSets, definition));
        }

        // Register item model
        if (firstModel != null) {
            registerParentedItemModel(generator, block, firstModel);
        }
    }

    /**
     * Registers a 16-way cuboid block with custom model references.
     */
    private static void registerCustomModelCuboid16WayBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        Map<String, List<ModelSet16Way>> rotationModelSets = new HashMap<>();

        for (int i = 0; i < MODEL_SUFFIXES.length; i++) {
            Identifier modelId = createCustomModelId(block, "base" + MODEL_SUFFIXES[i] + "_v1");
            rotationModelSets.put(MODEL_SUFFIXES[i], List.of(new ModelSet16Way(modelId, 1)));
        }

        generator.blockStateCollector.accept(createCuboid16WayBlockStateWithRandomTextures(block, rotationModelSets, definition));
        registerParentedItemModel(generator, block, rotationModelSets.get("").get(0).model);
    }

    /**
     * Fallback registration for 16-way cuboid blocks with no textures defined.
     */
    private static void registerFallbackCuboid16WayBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        List<String> fallbackTextures = List.of("missing");
        Map<String, List<ModelSet16Way>> rotationModelSets = new HashMap<>();

        for (int i = 0; i < MODEL_SUFFIXES.length; i++) {
            String modelName = "base" + MODEL_SUFFIXES[i] + "_v1";
            Identifier modelId = CuboidBlockExporter.createCuboidModel(generator, block, definition, fallbackTextures, 0, modelName, MODEL_ROTATIONS[i]);
            rotationModelSets.put(MODEL_SUFFIXES[i], List.of(new ModelSet16Way(modelId, 1)));
        }

        generator.blockStateCollector.accept(createCuboid16WayBlockStateWithRandomTextures(block, rotationModelSets, definition));
        registerParentedItemModel(generator, block, rotationModelSets.get("").get(0).model);
    }

    /**
     * Creates a blockstate for 16-way rotation blocks with random textures.
     */
    private static BlockStateSupplier createCuboid16WayBlockStateWithRandomTextures(Block block,
                                                                                     Map<String, List<ModelSet16Way>> rotationModelSets,
                                                                                     BlockDefinition definition) {
        return new BlockStateSupplier() {
            @Override
            public Block getBlock() {
                return block;
            }

            @Override
            public JsonElement get() {
                JsonObject json = new JsonObject();
                JsonObject variants = new JsonObject();

                // For each of the 16 rotations
                for (int rotation = 0; rotation < 16; rotation++) {
                    String variantKey = "rotation=" + rotation;

                    // Determine which base model set to use (0-3)
                    int modelIndex = rotation % 4;
                    List<ModelSet16Way> modelSets = rotationModelSets.get(MODEL_SUFFIXES[modelIndex]);

                    // Calculate Y rotation (0, 90, 180, 270)
                    int yRotation = (90 * (rotation / 4)) % 360;

                    if (modelSets.size() == 1) {
                        // Single variant
                        JsonObject variant = new JsonObject();
                        variant.addProperty("model", modelSets.get(0).model.toString());
                        if (yRotation > 0) {
                            variant.addProperty("y", yRotation);
                        }
                        variants.add(variantKey, variant);
                    } else {
                        // Multiple weighted variants
                        JsonArray variantArray = new JsonArray();
                        for (ModelSet16Way modelSet : modelSets) {
                            JsonObject variant = new JsonObject();
                            variant.addProperty("model", modelSet.model.toString());
                            if (yRotation > 0) {
                                variant.addProperty("y", yRotation);
                            }
                            if (modelSet.weight > 1) {
                                variant.addProperty("weight", modelSet.weight);
                            }
                            variantArray.add(variant);
                        }
                        variants.add(variantKey, variantArray);
                    }
                }

                json.add("variants", variants);
                return json;
            }
        };
    }

    /**
     * Creates a blockstate for 16-way rotation blocks with states.
     */
    private static BlockStateSupplier createCuboid16WayBlockStateWithStates(Block block,
                                                                             Map<String, Map<String, List<ModelSet16Way>>> stateRotationModelSets) {
        return new BlockStateSupplier() {
            @Override
            public Block getBlock() {
                return block;
            }

            @Override
            public JsonElement get() {
                JsonObject json = new JsonObject();
                JsonObject variants = new JsonObject();

                // For each state
                for (Map.Entry<String, Map<String, List<ModelSet16Way>>> stateEntry : stateRotationModelSets.entrySet()) {
                    String stateId = stateEntry.getKey();
                    Map<String, List<ModelSet16Way>> rotationModelSets = stateEntry.getValue();

                    // For each of the 16 rotations
                    for (int rotation = 0; rotation < 16; rotation++) {
                        String variantKey = "rotation=" + rotation + ",state=" + stateId;

                        // Determine which base model set to use (0-3)
                        int modelIndex = rotation % 4;
                        List<ModelSet16Way> modelSets = rotationModelSets.get(MODEL_SUFFIXES[modelIndex]);

                        // Calculate Y rotation (0, 90, 180, 270)
                        int yRotation = (90 * (rotation / 4)) % 360;

                        if (modelSets == null || modelSets.isEmpty()) {
                            continue;
                        }

                        if (modelSets.size() == 1) {
                            // Single variant
                            JsonObject variant = new JsonObject();
                            variant.addProperty("model", modelSets.get(0).model.toString());
                            if (yRotation > 0) {
                                variant.addProperty("y", yRotation);
                            }
                            variants.add(variantKey, variant);
                        } else {
                            // Multiple weighted variants
                            JsonArray variantArray = new JsonArray();
                            for (ModelSet16Way modelSet : modelSets) {
                                JsonObject variant = new JsonObject();
                                variant.addProperty("model", modelSet.model.toString());
                                if (yRotation > 0) {
                                    variant.addProperty("y", yRotation);
                                }
                                if (modelSet.weight > 1) {
                                    variant.addProperty("weight", modelSet.weight);
                                }
                                variantArray.add(variant);
                            }
                            variants.add(variantKey, variantArray);
                        }
                    }
                }

                json.add("variants", variants);
                return json;
            }
        };
    }

    private static Identifier createCustomModelId(Block block, String variant) {
        String blockName = getBlockName(block);
        return WesterosBlocks.id("block/custom/" + blockName + "/" + variant);
    }

    /**
     * Helper class to hold a model with weight.
     */
    private static class ModelSet16Way {
        final Identifier model;
        final int weight;

        ModelSet16Way(Identifier model, int weight) {
            this.model = model;
            this.weight = weight;
        }
    }
}
