package com.westerosblocks.datagen.custom;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.custom.WCCuboidNSEWBlock;
import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Exporter for NSEW cuboid blocks with directional facing support.
 * Generates blockstate files with facing variants and appropriate model rotations.
 */
public class CuboidNSEWBlockExporter extends BaseBlockExporter {

    private static final String[] FACING_DIRECTIONS = {"north", "east", "south", "west"};
    private static final int[] ROTATIONS = {0, 90, 180, 270};

    /**
     * Registers an NSEW cuboid block from a BlockDefinition.
     * Uses uniform iteration pattern: After doInit(), states is ALWAYS non-empty,
     * and each state has randomTextures normalized from simple textures.
     * Handles NSEW facing directions with proper model rotations.
     */
    public static void registerCustomCuboidNSEWBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        if (!(block instanceof WCCuboidNSEWBlock cuboidBlock)) {
            throw new IllegalArgumentException("Block must be a WCCuboidNSEWBlock instance");
        }

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
            registerCustomModelCuboidNSEWBlock(generator, block, definition, cuboidBlock);
            return;
        }

        // Collect all model identifiers for all states
        Map<String, List<Identifier>> stateModelMap = new HashMap<>();
        Map<String, List<Integer>> stateWeightMap = new HashMap<>();
        Identifier firstModel = null;

        for (BlockDefinition.StateVariant state : states) {
            String stateId = state.getStateID();
            if (stateId == null) stateId = "base";

            List<Identifier> modelIds = new ArrayList<>();
            List<Integer> weights = new ArrayList<>();

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

            // Iterate through all texture sets for this state
            for (int setIdx = 0; setIdx < textureSetCount; setIdx++) {
                Identifier modelId;
                String variantName = (hasMultipleStates ? stateId + "_" : "") + "v" + (setIdx + 1);
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

                    if (hasCuboids(definition)) {
                        // Generate custom cuboid models with geometry
                        modelId = createCuboidModel(generator, block, definition, textureList, setIdx, variantName);
                    } else {
                        // Generate standard cube models
                        TextureMap textureMap = createCuboidTextureMap(textureList);
                        if (textureList.size() == 1) {
                            modelId = Models.CUBE_ALL.upload(createGeneratedModelId(block, variantName), textureMap, generator.modelCollector);
                        } else {
                            modelId = Models.CUBE.upload(createGeneratedModelId(block, variantName), textureMap, generator.modelCollector);
                        }
                    }
                    weight = set.getWeight();
                }

                modelIds.add(modelId);
                weights.add(weight);
                if (firstModel == null) firstModel = modelId;
            }

            if (!modelIds.isEmpty()) {
                stateModelMap.put(stateId, modelIds);
                stateWeightMap.put(stateId, weights);
            }
        }

        if (stateModelMap.isEmpty()) {
            // Fallback if no valid models generated
            registerFallbackCuboidNSEWBlock(generator, block, definition, cuboidBlock);
            return;
        }

        // Generate blockstate based on whether we have multiple states
        if (hasMultipleStates) {
            // Multiple states - need "state=" prefix
            generator.blockStateCollector.accept(createAdvancedNSEWStatesBlockState(block, stateModelMap, stateWeightMap, states));
        } else {
            // Single state - no state prefix in variants, just facing
            List<Identifier> modelIds = stateModelMap.values().iterator().next();
            List<Integer> weights = stateWeightMap.values().iterator().next();

            if (modelIds.size() == 1) {
                // Single model - simple facing variants
                Identifier modelId = modelIds.get(0);
                BlockStateVariantMap variants = BlockStateVariantMap.create(WCCuboidNSEWBlock.FACING)
                    .register(Direction.EAST, createVariant(modelId, 0))
                    .register(Direction.SOUTH, createVariant(modelId, 90))
                    .register(Direction.WEST, createVariant(modelId, 180))
                    .register(Direction.NORTH, createVariant(modelId, 270));
                generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block).coordinate(variants));
            } else {
                // Multiple models (random textures) - weighted variants for each facing
                generator.blockStateCollector.accept(createNSEWBlockStateWithRandomTextures(block, modelIds, weights));
            }
        }

        // Register item model
        if (firstModel != null) {
            registerParentedItemModel(generator, block, firstModel);
        }
    }

    /**
     * Registers an NSEW cuboid block with custom models but no defined textures.
     */
    private static void registerCustomModelCuboidNSEWBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition, WCCuboidNSEWBlock cuboidBlock) {
        if (definition.hasRandomTextures()) {
            // Handle empty random texture variants - only reference existing models for isCustomModel
            List<BlockDefinition.RandomTextureVariant> randomTextures = definition.getRandomTextures();
            List<Identifier> modelIds = new ArrayList<>();

            for (int i = 0; i < randomTextures.size(); i++) {
                if (definition.hasCustomModel()) {
                    // For custom models, just reference the existing model files
                    Identifier modelId = createCustomModelId(block, "base_v" + (i + 1));
                    modelIds.add(modelId);
                } else {
                    // Generate actual models for non-custom model blocks
                    BlockDefinition.RandomTextureVariant variant = randomTextures.get(i);
                    List<String> textures = variant.getTextures();

                    // If no textures defined, use fallback texture
                    if (textures == null || textures.isEmpty()) {
                        textures = List.of("missing");
                    }

                    Identifier modelId = createCuboidModel(generator, block, definition, textures, i, "base_v" + (i + 1));
                    modelIds.add(modelId);
                }
            }

            // Check if we need to generate rotation variants (for rotateRandom blocks)
            if (definition.hasRotateRandom()) {
                // For rotateRandom blocks, generate all rotations for each model
                List<BlockStateVariant> allVariants = new ArrayList<>();
                for (Identifier modelId : modelIds) {
                    allVariants.add(createVariant(modelId, 0));
                    allVariants.add(createVariant(modelId, 90));
                    allVariants.add(createVariant(modelId, 180));
                    allVariants.add(createVariant(modelId, 270));
                }

                // For rotateRandom + facing blocks, we apply rotations to each facing direction
                BlockStateVariantMap variants = BlockStateVariantMap.create(WCCuboidNSEWBlock.FACING)
                    .register(Direction.EAST, allVariants)
                    .register(Direction.SOUTH, allVariants)
                    .register(Direction.WEST, allVariants)
                    .register(Direction.NORTH, allVariants);

                generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block).coordinate(variants));
            } else {
                // Normal random texture variants with facing rotations
                List<BlockStateVariant> eastVariants = modelIds.stream().map(id -> createVariant(id, 0)).toList();
                List<BlockStateVariant> southVariants = modelIds.stream().map(id -> createVariant(id, 90)).toList();
                List<BlockStateVariant> westVariants = modelIds.stream().map(id -> createVariant(id, 180)).toList();
                List<BlockStateVariant> northVariants = modelIds.stream().map(id -> createVariant(id, 270)).toList();

                BlockStateVariantMap variants = BlockStateVariantMap.create(WCCuboidNSEWBlock.FACING)
                    .register(Direction.EAST, eastVariants)
                    .register(Direction.SOUTH, southVariants)
                    .register(Direction.WEST, westVariants)
                    .register(Direction.NORTH, northVariants);

                generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block).coordinate(variants));
            }

            if (!modelIds.isEmpty()) {
                registerParentedItemModel(generator, block, modelIds.get(0));
            }
        } else {
            // Single custom model
            Identifier modelId;
            if (definition.hasCustomModel()) {
                modelId = createCustomModelId(block, "base_v1");
            } else {
                List<String> fallbackTextures = List.of("missing");
                modelId = createCuboidModel(generator, block, definition, fallbackTextures, 0, "base_v1");
            }

            BlockStateVariantMap variants = BlockStateVariantMap.create(WCCuboidNSEWBlock.FACING)
                .register(Direction.EAST, createVariant(modelId, 0))
                .register(Direction.SOUTH, createVariant(modelId, 90))
                .register(Direction.WEST, createVariant(modelId, 180))
                .register(Direction.NORTH, createVariant(modelId, 270));

            generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block).coordinate(variants));
            registerParentedItemModel(generator, block, modelId);
        }
    }

    /**
     * Fallback registration for NSEW cuboid blocks with no textures or models defined.
     */
    private static void registerFallbackCuboidNSEWBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition, WCCuboidNSEWBlock cuboidBlock) {
        String defaultTexture = "missing";
        List<String> fallbackTextures = List.of(defaultTexture);

        TextureMap textureMap = createCuboidTextureMap(fallbackTextures);
        Identifier modelId = Models.CUBE_ALL.upload(createGeneratedModelId(block, "base_v1"), textureMap, generator.modelCollector);

        BlockStateVariantMap variants = BlockStateVariantMap.create(WCCuboidNSEWBlock.FACING)
            .register(Direction.EAST, createVariant(modelId, 0))
            .register(Direction.SOUTH, createVariant(modelId, 90))
            .register(Direction.WEST, createVariant(modelId, 180))
            .register(Direction.NORTH, createVariant(modelId, 270));

        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block).coordinate(variants));
        registerParentedItemModel(generator, block, modelId);
    }

    // Helper methods from CuboidBlockExporter
    private static Identifier createGeneratedModelId(Block block, String variant) {
        String blockName = getBlockName(block);
        String modelPath = "block/" + blockName + "/" + variant;
        return WesterosBlocks.id(modelPath);
    }

    private static Identifier createCustomModelId(Block block, String variant) {
        String blockName = getBlockName(block);
        String modelPath = "block/custom/" + blockName + "/" + variant;
        return WesterosBlocks.id(modelPath);
    }

    private static boolean hasCuboids(BlockDefinition definition) {
        return (definition.getCuboids() != null && !definition.getCuboids().isEmpty()) ||
               definition.hasBoundingBox();
    }

    private static TextureMap createCuboidTextureMap(List<String> textures) {
        TextureMap textureMap = new TextureMap();

        if (textures == null || textures.isEmpty()) {
            String fallbackTexture = "missing";
            Identifier fallbackId = createBlockIdentifier(fallbackTexture);
            textureMap.put(TextureKey.DOWN, fallbackId);
            textureMap.put(TextureKey.UP, fallbackId);
            textureMap.put(TextureKey.NORTH, fallbackId);
            textureMap.put(TextureKey.SOUTH, fallbackId);
            textureMap.put(TextureKey.WEST, fallbackId);
            textureMap.put(TextureKey.EAST, fallbackId);
            textureMap.put(TextureKey.PARTICLE, fallbackId);
            return textureMap;
        }

        int textureCount = textures.size();
        TextureKey[] faceKeys = {TextureKey.DOWN, TextureKey.UP, TextureKey.NORTH, TextureKey.SOUTH, TextureKey.WEST, TextureKey.EAST};

        for (int i = 0; i < 6; i++) {
            String texture = i < textureCount ? textures.get(i) : textures.get(textureCount - 1);
            textureMap.put(faceKeys[i], createBlockIdentifier(texture));
        }

        textureMap.put(TextureKey.PARTICLE, createBlockIdentifier(textures.get(0)));
        return textureMap;
    }

    private static Identifier createCuboidModel(BlockStateModelGenerator generator, Block block, BlockDefinition definition, List<String> textures, int stateIndex, String variant) {
        // This would delegate to CuboidBlockExporter.createCuboidModel
        return CuboidBlockExporter.createCuboidModel(generator, block, definition, textures, stateIndex, variant);
    }

    private static BlockStateSupplier createAdvancedNSEWStatesBlockState(Block block,
                                                                          Map<String, List<Identifier>> stateModelMap,
                                                                          Map<String, List<Integer>> stateWeightMap,
                                                                          List<BlockDefinition.StateVariant> states) {
        return new BlockStateSupplier() {
            @Override
            public Block getBlock() {
                return block;
            }

            @Override
            public JsonElement get() {
                JsonObject json = new JsonObject();
                JsonObject variants = new JsonObject();

                for (BlockDefinition.StateVariant state : states) {
                    String stateId = state.getStateID();
                    if (stateId == null) stateId = "base";

                    List<Identifier> modelIds = stateModelMap.get(stateId);
                    List<Integer> weights = stateWeightMap.get(stateId);

                    if (modelIds == null || modelIds.isEmpty()) {
                        continue;
                    }

                    if (modelIds.size() == 1) {
                        // Single model per state
                        Identifier modelId = modelIds.get(0);

                        JsonObject eastVariant = new JsonObject();
                        eastVariant.addProperty("model", modelId.toString());
                        variants.add("facing=east,state=" + stateId, eastVariant);

                        JsonObject southVariant = new JsonObject();
                        southVariant.addProperty("model", modelId.toString());
                        southVariant.addProperty("y", 90);
                        variants.add("facing=south,state=" + stateId, southVariant);

                        JsonObject westVariant = new JsonObject();
                        westVariant.addProperty("model", modelId.toString());
                        westVariant.addProperty("y", 180);
                        variants.add("facing=west,state=" + stateId, westVariant);

                        JsonObject northVariant = new JsonObject();
                        northVariant.addProperty("model", modelId.toString());
                        northVariant.addProperty("y", 270);
                        variants.add("facing=north,state=" + stateId, northVariant);
                    } else {
                        // Multiple weighted models per state
                        // EAST facing (0° rotation)
                        com.google.gson.JsonArray eastArray = new com.google.gson.JsonArray();
                        for (int i = 0; i < modelIds.size(); i++) {
                            int weight = weights != null && i < weights.size() ? weights.get(i) : 1;
                            for (int w = 0; w < weight; w++) {
                                JsonObject variant = new JsonObject();
                                variant.addProperty("model", modelIds.get(i).toString());
                                eastArray.add(variant);
                            }
                        }
                        variants.add("facing=east,state=" + stateId, eastArray);

                        // SOUTH facing (90° rotation)
                        com.google.gson.JsonArray southArray = new com.google.gson.JsonArray();
                        for (int i = 0; i < modelIds.size(); i++) {
                            int weight = weights != null && i < weights.size() ? weights.get(i) : 1;
                            for (int w = 0; w < weight; w++) {
                                JsonObject variant = new JsonObject();
                                variant.addProperty("model", modelIds.get(i).toString());
                                variant.addProperty("y", 90);
                                southArray.add(variant);
                            }
                        }
                        variants.add("facing=south,state=" + stateId, southArray);

                        // WEST facing (180° rotation)
                        com.google.gson.JsonArray westArray = new com.google.gson.JsonArray();
                        for (int i = 0; i < modelIds.size(); i++) {
                            int weight = weights != null && i < weights.size() ? weights.get(i) : 1;
                            for (int w = 0; w < weight; w++) {
                                JsonObject variant = new JsonObject();
                                variant.addProperty("model", modelIds.get(i).toString());
                                variant.addProperty("y", 180);
                                westArray.add(variant);
                            }
                        }
                        variants.add("facing=west,state=" + stateId, westArray);

                        // NORTH facing (270° rotation)
                        com.google.gson.JsonArray northArray = new com.google.gson.JsonArray();
                        for (int i = 0; i < modelIds.size(); i++) {
                            int weight = weights != null && i < weights.size() ? weights.get(i) : 1;
                            for (int w = 0; w < weight; w++) {
                                JsonObject variant = new JsonObject();
                                variant.addProperty("model", modelIds.get(i).toString());
                                variant.addProperty("y", 270);
                                northArray.add(variant);
                            }
                        }
                        variants.add("facing=north,state=" + stateId, northArray);
                    }
                }

                json.add("variants", variants);
                return json;
            }
        };
    }

    /**
     * Creates a blockstate supplier for single-state NSEW blocks with random textures.
     * Generates JSON with weighted variants for each facing direction.
     */
    private static BlockStateSupplier createNSEWBlockStateWithRandomTextures(Block block, List<Identifier> modelIds, List<Integer> weights) {
        return new BlockStateSupplier() {
            @Override
            public Block getBlock() {
                return block;
            }

            @Override
            public JsonElement get() {
                JsonObject json = new JsonObject();
                JsonObject variants = new JsonObject();

                // EAST facing (0° rotation)
                com.google.gson.JsonArray eastArray = new com.google.gson.JsonArray();
                for (int i = 0; i < modelIds.size(); i++) {
                    int weight = weights != null && i < weights.size() ? weights.get(i) : 1;
                    for (int w = 0; w < weight; w++) {
                        JsonObject variant = new JsonObject();
                        variant.addProperty("model", modelIds.get(i).toString());
                        eastArray.add(variant);
                    }
                }
                variants.add("facing=east", eastArray);

                // SOUTH facing (90° rotation)
                com.google.gson.JsonArray southArray = new com.google.gson.JsonArray();
                for (int i = 0; i < modelIds.size(); i++) {
                    int weight = weights != null && i < weights.size() ? weights.get(i) : 1;
                    for (int w = 0; w < weight; w++) {
                        JsonObject variant = new JsonObject();
                        variant.addProperty("model", modelIds.get(i).toString());
                        variant.addProperty("y", 90);
                        southArray.add(variant);
                    }
                }
                variants.add("facing=south", southArray);

                // WEST facing (180° rotation)
                com.google.gson.JsonArray westArray = new com.google.gson.JsonArray();
                for (int i = 0; i < modelIds.size(); i++) {
                    int weight = weights != null && i < weights.size() ? weights.get(i) : 1;
                    for (int w = 0; w < weight; w++) {
                        JsonObject variant = new JsonObject();
                        variant.addProperty("model", modelIds.get(i).toString());
                        variant.addProperty("y", 180);
                        westArray.add(variant);
                    }
                }
                variants.add("facing=west", westArray);

                // NORTH facing (270° rotation)
                com.google.gson.JsonArray northArray = new com.google.gson.JsonArray();
                for (int i = 0; i < modelIds.size(); i++) {
                    int weight = weights != null && i < weights.size() ? weights.get(i) : 1;
                    for (int w = 0; w < weight; w++) {
                        JsonObject variant = new JsonObject();
                        variant.addProperty("model", modelIds.get(i).toString());
                        variant.addProperty("y", 270);
                        northArray.add(variant);
                    }
                }
                variants.add("facing=north", northArray);

                json.add("variants", variants);
                return json;
            }
        };
    }
}