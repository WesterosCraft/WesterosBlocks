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
     */
    public static void registerCustomCuboid16WayBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        // Use centralized priority logic from BlockDefinition
        BlockDefinition.TextureSource source = definition.getPrimaryTextureSource();

        switch (source) {
            case STATES -> registerCuboid16WayBlockWithStates(generator, block, definition);
            case RANDOM_TEXTURES -> registerCuboid16WayBlockWithRandomTextures(generator, block, definition);
            case TEXTURES -> registerSimpleCuboid16WayBlock(generator, block, definition);
            case CUSTOM_MODEL -> registerCustomModelCuboid16WayBlock(generator, block, definition);
            case NONE -> registerFallbackCuboid16WayBlock(generator, block, definition);
        }
    }

    /**
     * Registers a simple 16-way cuboid block with basic textures.
     */
    private static void registerSimpleCuboid16WayBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        List<String> textures = definition.getTextures();
        Map<String, Identifier> rotationModels = new HashMap<>();

        // Generate 4 base models with different rotation offsets
        for (int i = 0; i < MODEL_SUFFIXES.length; i++) {
            String modelName = "base" + MODEL_SUFFIXES[i] + "_v1";
            Identifier modelId;

            if (definition.hasCustomModel()) {
                modelId = createCustomModelId(block, modelName);
            } else {
                modelId = CuboidBlockExporter.createCuboidModel(generator, block, definition, textures, 0, modelName, MODEL_ROTATIONS[i]);
            }

            rotationModels.put(MODEL_SUFFIXES[i], modelId);
        }

        // Generate blockstate with 16 rotation variants
        generator.blockStateCollector.accept(createCuboid16WayBlockState(block, rotationModels, null, definition));
        registerParentedItemModel(generator, block, rotationModels.get(""));
    }

    /**
     * Registers a 16-way cuboid block with random texture variants.
     */
    private static void registerCuboid16WayBlockWithRandomTextures(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        List<BlockDefinition.RandomTextureVariant> randomTextures = definition.getRandomTextures();
        Map<String, List<ModelSet16Way>> rotationModelSets = new HashMap<>();

        // For each of the 4 rotation types
        for (int rotIdx = 0; rotIdx < MODEL_SUFFIXES.length; rotIdx++) {
            List<ModelSet16Way> modelSets = new ArrayList<>();

            // For each random texture variant
            for (int texIdx = 0; texIdx < randomTextures.size(); texIdx++) {
                BlockDefinition.RandomTextureVariant variant = randomTextures.get(texIdx);
                List<String> textures = variant.getTextures();
                String modelName = "base" + MODEL_SUFFIXES[rotIdx] + "_v" + (texIdx + 1);

                Identifier modelId;
                if (definition.hasCustomModel()) {
                    modelId = createCustomModelId(block, modelName);
                } else {
                    modelId = CuboidBlockExporter.createCuboidModel(generator, block, definition, textures, texIdx, modelName, MODEL_ROTATIONS[rotIdx]);
                }

                modelSets.add(new ModelSet16Way(modelId, variant.getWeight()));
            }

            rotationModelSets.put(MODEL_SUFFIXES[rotIdx], modelSets);
        }

        // Generate blockstate with weighted random variants
        generator.blockStateCollector.accept(createCuboid16WayBlockStateWithRandomTextures(block, rotationModelSets, definition));
        registerParentedItemModel(generator, block, rotationModelSets.get("").get(0).model);
    }

    /**
     * Registers a 16-way cuboid block with multiple states.
     */
    private static void registerCuboid16WayBlockWithStates(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        List<BlockDefinition.StateVariant> states = definition.getStates();
        Map<String, Map<String, Identifier>> stateRotationModels = new HashMap<>();

        for (int stateIdx = 0; stateIdx < states.size(); stateIdx++) {
            BlockDefinition.StateVariant state = states.get(stateIdx);
            String stateId = state.getStateID() != null ? state.getStateID() : "state" + stateIdx;
            Map<String, Identifier> rotationModels = new HashMap<>();

            // Check if state has random textures
            if (state.getRandomTextures() != null && !state.getRandomTextures().isEmpty()) {
                // Use first random texture variant for states (can be expanded if needed)
                List<String> textures = state.getRandomTextures().get(0).getTextures();

                for (int rotIdx = 0; rotIdx < MODEL_SUFFIXES.length; rotIdx++) {
                    String modelName = stateId + MODEL_SUFFIXES[rotIdx] + "_v1";
                    Identifier modelId;

                    if (definition.hasCustomModel()) {
                        modelId = createCustomModelId(block, modelName);
                    } else {
                        modelId = CuboidBlockExporter.createCuboidModel(generator, block, definition, textures, stateIdx, modelName, MODEL_ROTATIONS[rotIdx]);
                    }

                    rotationModels.put(MODEL_SUFFIXES[rotIdx], modelId);
                }
            } else {
                // Use state textures
                List<String> textures = state.getTextures();

                for (int rotIdx = 0; rotIdx < MODEL_SUFFIXES.length; rotIdx++) {
                    String modelName = stateId + MODEL_SUFFIXES[rotIdx] + "_v1";
                    Identifier modelId;

                    if (definition.hasCustomModel()) {
                        modelId = createCustomModelId(block, modelName);
                    } else {
                        modelId = CuboidBlockExporter.createCuboidModel(generator, block, definition, textures, stateIdx, modelName, MODEL_ROTATIONS[rotIdx]);
                    }

                    rotationModels.put(MODEL_SUFFIXES[rotIdx], modelId);
                }
            }

            stateRotationModels.put(stateId, rotationModels);
        }

        // Generate blockstate with state and rotation variants
        generator.blockStateCollector.accept(createCuboid16WayBlockStateWithStates(block, stateRotationModels, definition));

        // Use first state's base model for item
        if (!stateRotationModels.isEmpty()) {
            Map<String, Identifier> firstStateModels = stateRotationModels.values().iterator().next();
            registerParentedItemModel(generator, block, firstStateModels.get(""));
        }
    }

    /**
     * Registers a 16-way cuboid block with custom model references.
     */
    private static void registerCustomModelCuboid16WayBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        Map<String, Identifier> rotationModels = new HashMap<>();

        for (int i = 0; i < MODEL_SUFFIXES.length; i++) {
            rotationModels.put(MODEL_SUFFIXES[i], createCustomModelId(block, "base" + MODEL_SUFFIXES[i] + "_v1"));
        }

        generator.blockStateCollector.accept(createCuboid16WayBlockState(block, rotationModels, null, definition));
        registerParentedItemModel(generator, block, rotationModels.get(""));
    }

    /**
     * Fallback registration for 16-way cuboid blocks with no textures defined.
     */
    private static void registerFallbackCuboid16WayBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        List<String> fallbackTextures = List.of("missing");
        Map<String, Identifier> rotationModels = new HashMap<>();

        for (int i = 0; i < MODEL_SUFFIXES.length; i++) {
            String modelName = "base" + MODEL_SUFFIXES[i] + "_v1";
            Identifier modelId = CuboidBlockExporter.createCuboidModel(generator, block, definition, fallbackTextures, 0, modelName, MODEL_ROTATIONS[i]);
            rotationModels.put(MODEL_SUFFIXES[i], modelId);
        }

        generator.blockStateCollector.accept(createCuboid16WayBlockState(block, rotationModels, null, definition));
        registerParentedItemModel(generator, block, rotationModels.get(""));
    }

    /**
     * Creates a blockstate for 16-way rotation blocks.
     */
    private static BlockStateSupplier createCuboid16WayBlockState(Block block, Map<String, Identifier> rotationModels,
                                                                   String stateId, BlockDefinition definition) {
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
                    String variantKey = stateId != null ?
                        "rotation=" + rotation + ",state=" + stateId :
                        "rotation=" + rotation;

                    JsonObject variant = new JsonObject();

                    // Determine which base model to use (0-3)
                    int modelIndex = rotation % 4;
                    Identifier modelId = rotationModels.get(MODEL_SUFFIXES[modelIndex]);
                    variant.addProperty("model", modelId.toString());

                    // Calculate Y rotation (0, 90, 180, 270)
                    int yRotation = (90 * (rotation / 4)) % 360;
                    if (yRotation > 0) {
                        variant.addProperty("y", yRotation);
                    }

                    variants.add(variantKey, variant);
                }

                json.add("variants", variants);
                return json;
            }
        };
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
                                                                             Map<String, Map<String, Identifier>> stateRotationModels,
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

                // For each state
                for (Map.Entry<String, Map<String, Identifier>> stateEntry : stateRotationModels.entrySet()) {
                    String stateId = stateEntry.getKey();
                    Map<String, Identifier> rotationModels = stateEntry.getValue();

                    // For each of the 16 rotations
                    for (int rotation = 0; rotation < 16; rotation++) {
                        String variantKey = "rotation=" + rotation + ",state=" + stateId;

                        JsonObject variant = new JsonObject();

                        // Determine which base model to use (0-3)
                        int modelIndex = rotation % 4;
                        Identifier modelId = rotationModels.get(MODEL_SUFFIXES[modelIndex]);
                        variant.addProperty("model", modelId.toString());

                        // Calculate Y rotation (0, 90, 180, 270)
                        int yRotation = (90 * (rotation / 4)) % 360;
                        if (yRotation > 0) {
                            variant.addProperty("y", yRotation);
                        }

                        variants.add(variantKey, variant);
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
