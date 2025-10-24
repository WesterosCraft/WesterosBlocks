package com.westerosblocks.datagen.custom;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.custom.WCCuboidNEBlock;
import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CuboidNEBlockExporter extends BaseBlockExporter {

    /**
     * Registers a NE cuboid block from a BlockDefinition.
     * Uses uniform iteration pattern: After doInit(), states is ALWAYS non-empty,
     * and each state has randomTextures normalized from simple textures.
     * Handles EAST and NORTH facing directions with proper model rotations.
     */
    public static void registerCustomCuboidNEBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        if (!(block instanceof WCCuboidNEBlock cuboidBlock)) {
            throw new IllegalArgumentException("Block must be a WCCuboidNEBlock instance");
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
            registerCustomModelCuboidNEBlock(generator, block, definition, cuboidBlock);
            return;
        }

        // Collect all model identifiers for all states
        Map<String, List<Identifier>> stateModelMap = new HashMap<>();
        Identifier firstModel = null;

        for (BlockDefinition.StateVariant state : states) {
            String stateId = state.getStateID();
            if (stateId == null) stateId = "base";

            List<Identifier> modelIds = new ArrayList<>();

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

                // Check if this state uses custom models
                if (state.isCustomModel()) {
                    // Use custom model reference instead of generating from textures
                    modelId = createCustomModelId(block, variantName);
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
                }

                modelIds.add(modelId);
                if (firstModel == null) firstModel = modelId;
            }

            if (!modelIds.isEmpty()) {
                stateModelMap.put(stateId, modelIds);
            }
        }

        if (stateModelMap.isEmpty()) {
            // Fallback if no valid models generated
            registerFallbackCuboidNEBlock(generator, block, definition, cuboidBlock);
            return;
        }

        // Generate blockstate based on whether we have multiple states
        if (hasMultipleStates) {
            // Multiple states - need "state=" prefix
            generator.blockStateCollector.accept(createAdvancedNEStatesBlockState(block, stateModelMap, states));
        } else {
            // Single state - no state prefix in variants, just facing
            List<Identifier> modelIds = stateModelMap.values().iterator().next();

            if (modelIds.size() == 1) {
                // Single model - simple facing variants
                Identifier modelId = modelIds.get(0);
                BlockStateVariantMap variants = BlockStateVariantMap.create(WCCuboidNEBlock.FACING)
                    .register(Direction.EAST, createVariant(modelId, 0))
                    .register(Direction.NORTH, createVariant(modelId, 90));
                generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block).coordinate(variants));
            } else {
                // Multiple models (random textures) - weighted variants for each facing
                generator.blockStateCollector.accept(createNEBlockStateWithRandomTextures(block, modelIds, states.get(0)));
            }
        }

        // Register item model
        if (firstModel != null) {
            registerParentedItemModel(generator, block, firstModel);
        }
    }

    /**
     * Registers a NE cuboid block with custom model.
     */
    private static void registerCustomModelCuboidNEBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition, WCCuboidNEBlock cuboidBlock) {
        Identifier modelId = createCustomModelId(block, "base_v1");

        BlockStateVariantMap variants = BlockStateVariantMap.create(WCCuboidNEBlock.FACING)
            .register(Direction.EAST, createVariant(modelId, 0))
            .register(Direction.NORTH, createVariant(modelId, 90));

        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block).coordinate(variants));
        registerParentedItemModel(generator, block, modelId);
    }

    /**
     * Fallback registration for blocks without proper texture definitions.
     */
    private static void registerFallbackCuboidNEBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition, WCCuboidNEBlock cuboidBlock) {
        Identifier modelId = createGeneratedModelId(block, "base_v1");

        BlockStateVariantMap variants = BlockStateVariantMap.create(WCCuboidNEBlock.FACING)
            .register(Direction.EAST, createVariant(modelId, 0))
            .register(Direction.NORTH, createVariant(modelId, 90));

        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block).coordinate(variants));
        registerParentedItemModel(generator, block, modelId);
    }

    // Helper methods (delegate to CuboidBlockExporter methods)
    private static boolean hasCuboids(BlockDefinition definition) {
        return CuboidBlockExporter.hasCuboids(definition);
    }

    private static Identifier createCuboidModel(BlockStateModelGenerator generator, Block block, BlockDefinition definition, List<String> textures, int index, String variant) {
        return CuboidBlockExporter.createCuboidModel(generator, block, definition, textures, index, variant);
    }

    private static TextureMap createCuboidTextureMap(List<String> textures) {
        return CuboidBlockExporter.createCuboidTextureMap(textures);
    }

    private static Identifier createCustomModelId(Block block, String variant) {
        String blockName = getBlockName(block);
        return WesterosBlocks.id("block/custom/" + blockName + "/" + variant);
    }

    private static Identifier createGeneratedModelId(Block block, String variant) {
        String blockName = getBlockName(block);
        return WesterosBlocks.id("block/" + blockName + "/" + variant);
    }

    /**
     * Creates a custom blockstate supplier for NE blocks with states.
     * Generates JSON with both state and facing properties.
     */
    private static BlockStateSupplier createAdvancedNEStatesBlockState(Block block,
                                                                       Map<String, List<Identifier>> stateModelMap,
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

                for (int i = 0; i < states.size(); i++) {
                    BlockDefinition.StateVariant state = states.get(i);
                    String stateId = state.getStateID() != null ? state.getStateID() : "base";
                    List<Identifier> modelIds = stateModelMap.get(stateId);

                    if (modelIds == null || modelIds.isEmpty()) {
                        continue;
                    }

                    if (state.hasRandomTextures()) {
                        // Handle state with random textures - create weighted variants
                        List<BlockDefinition.RandomTextureVariant> randomTextures = state.getRandomTextures();

                        // EAST facing (0° rotation)
                        JsonArray eastVariants = new JsonArray();
                        for (int j = 0; j < modelIds.size() && j < randomTextures.size(); j++) {
                            int weight = randomTextures.get(j).getWeight();
                            for (int w = 0; w < weight; w++) {
                                JsonObject variant = new JsonObject();
                                variant.addProperty("model", modelIds.get(j).toString());
                                eastVariants.add(variant);
                            }
                        }
                        variants.add("facing=east,state=" + stateId, eastVariants);

                        // NORTH facing (90° rotation)
                        JsonArray northVariants = new JsonArray();
                        for (int j = 0; j < modelIds.size() && j < randomTextures.size(); j++) {
                            int weight = randomTextures.get(j).getWeight();
                            for (int w = 0; w < weight; w++) {
                                JsonObject variant = new JsonObject();
                                variant.addProperty("model", modelIds.get(j).toString());
                                variant.addProperty("y", 90);
                                northVariants.add(variant);
                            }
                        }
                        variants.add("facing=north,state=" + stateId, northVariants);
                    } else {
                        // Handle state with single texture set
                        Identifier modelId = modelIds.get(0);

                        JsonObject eastVariant = new JsonObject();
                        eastVariant.addProperty("model", modelId.toString());
                        variants.add("facing=east,state=" + stateId, eastVariant);

                        JsonObject northVariant = new JsonObject();
                        northVariant.addProperty("model", modelId.toString());
                        northVariant.addProperty("y", 90);
                        variants.add("facing=north,state=" + stateId, northVariant);
                    }
                }

                json.add("variants", variants);
                return json;
            }
        };
    }

    /**
     * Creates a blockstate supplier for single-state NE blocks with random textures.
     * Generates JSON with weighted variants for EAST and NORTH facings.
     */
    private static BlockStateSupplier createNEBlockStateWithRandomTextures(Block block, List<Identifier> modelIds, BlockDefinition.StateVariant state) {
        return new BlockStateSupplier() {
            @Override
            public Block getBlock() {
                return block;
            }

            @Override
            public JsonElement get() {
                JsonObject json = new JsonObject();
                JsonObject variants = new JsonObject();

                // Get random textures from state to access weights
                List<BlockDefinition.RandomTextureVariant> randomTextures = state.getRandomTextures();

                if (randomTextures == null || randomTextures.isEmpty()) {
                    // Fallback: single model for each facing
                    JsonObject eastVariant = new JsonObject();
                    eastVariant.addProperty("model", modelIds.get(0).toString());
                    variants.add("facing=east", eastVariant);

                    JsonObject northVariant = new JsonObject();
                    northVariant.addProperty("model", modelIds.get(0).toString());
                    northVariant.addProperty("y", 90);
                    variants.add("facing=north", northVariant);
                } else {
                    // EAST facing (0° rotation) - weighted variants
                    JsonArray eastVariants = new JsonArray();
                    for (int i = 0; i < modelIds.size() && i < randomTextures.size(); i++) {
                        int weight = randomTextures.get(i).getWeight();
                        for (int w = 0; w < weight; w++) {
                            JsonObject variant = new JsonObject();
                            variant.addProperty("model", modelIds.get(i).toString());
                            eastVariants.add(variant);
                        }
                    }
                    variants.add("facing=east", eastVariants);

                    // NORTH facing (90° rotation) - weighted variants
                    JsonArray northVariants = new JsonArray();
                    for (int i = 0; i < modelIds.size() && i < randomTextures.size(); i++) {
                        int weight = randomTextures.get(i).getWeight();
                        for (int w = 0; w < weight; w++) {
                            JsonObject variant = new JsonObject();
                            variant.addProperty("model", modelIds.get(i).toString());
                            variant.addProperty("y", 90);
                            northVariants.add(variant);
                        }
                    }
                    variants.add("facing=north", northVariants);
                }

                json.add("variants", variants);
                return json;
            }
        };
    }
}