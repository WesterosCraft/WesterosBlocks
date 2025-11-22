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

public class CuboidNEBlockExporter extends CuboidBlockExporter {

    /**
     * Registers a NE cuboid block from a BlockDefinition.
     * Uses CuboidBlockExporter for model generation, only customizes blockstate for facing variants.
     * Handles EAST and NORTH facing directions with proper model rotations.
     */
    public static void registerCustomCuboidNEBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        if (!(block instanceof WCCuboidNEBlock cuboidBlock)) {
            throw new IllegalArgumentException("Block must be a WCCuboidNEBlock instance");
        }

        // Phase 1: Extract configuration
        var states = definition.getStates();
        if (states == null || states.isEmpty()) {
            throw new IllegalStateException("Block definition states should never be null/empty after doInit() for block: " + getBlockName(block));
        }
        boolean hasMultipleStates = definition.getStateCount() > 1;

        // Phase 2: Generate models (reuse CuboidBlockExporter completely via instance)
        CuboidNEBlockExporter exporter = new CuboidNEBlockExporter();
        Map<String, List<Identifier>> stateModelMap = exporter.generateModelsReturnMap(generator, block, definition);

        if (stateModelMap.isEmpty()) {
            WesterosBlocks.LOGGER.warn("No models generated for NE cuboid block: {}", getBlockName(block));
            return;
        }

        // Phase 3: Generate NE-specific blockstate (facing=east, facing=north variants)
        generator.blockStateCollector.accept(generateNEBlockState(block, stateModelMap, states, hasMultipleStates));

        // Phase 4: Register item model
        Identifier firstModel = stateModelMap.values().iterator().next().get(0);
        registerParentedItemModel(generator, block, firstModel);
    }

    /**
     * Generates NE-specific blockstate JSON with facing variants.
     * Handles both single-state and multi-state blocks.
     * Creates "facing=east" (0°) and "facing=north" (90°) variants for each state.
     */
    private static BlockStateSupplier generateNEBlockState(Block block,
                                                           Map<String, List<Identifier>> stateModelMap,
                                                           List<BlockDefinition.StateVariant> states,
                                                           boolean hasMultipleStates) {
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
                    String stateId = state.getStateID() != null ? state.getStateID() : "base";
                    List<Identifier> modelIds = stateModelMap.get(stateId);

                    if (modelIds == null || modelIds.isEmpty()) {
                        continue;
                    }

                    // Determine variant key prefix based on whether block has multiple states
                    String eastKey = hasMultipleStates ? "facing=east,state=" + stateId : "facing=east";
                    String northKey = hasMultipleStates ? "facing=north,state=" + stateId : "facing=north";

                    // Get random textures for weight information
                    List<BlockDefinition.RandomTextureVariant> randomTextures = state.getRandomTextures();

                    if (randomTextures != null && !randomTextures.isEmpty() && modelIds.size() > 1) {
                        // Multiple models with weights - create array variants
                        JsonArray eastVariants = new JsonArray();
                        JsonArray northVariants = new JsonArray();

                        for (int i = 0; i < modelIds.size() && i < randomTextures.size(); i++) {
                            int weight = randomTextures.get(i).getWeight();

                            // Add weighted variants for EAST (0° rotation)
                            for (int w = 0; w < weight; w++) {
                                JsonObject variant = new JsonObject();
                                variant.addProperty("model", modelIds.get(i).toString());
                                eastVariants.add(variant);
                            }

                            // Add weighted variants for NORTH (90° rotation)
                            for (int w = 0; w < weight; w++) {
                                JsonObject variant = new JsonObject();
                                variant.addProperty("model", modelIds.get(i).toString());
                                variant.addProperty("y", 90);
                                northVariants.add(variant);
                            }
                        }

                        variants.add(eastKey, eastVariants);
                        variants.add(northKey, northVariants);
                    } else {
                        // Single model - simple variants
                        Identifier modelId = modelIds.get(0);

                        JsonObject eastVariant = new JsonObject();
                        eastVariant.addProperty("model", modelId.toString());
                        variants.add(eastKey, eastVariant);

                        JsonObject northVariant = new JsonObject();
                        northVariant.addProperty("model", modelId.toString());
                        northVariant.addProperty("y", 90);
                        variants.add(northKey, northVariant);
                    }
                }

                json.add("variants", variants);
                return json;
            }
        };
    }
}