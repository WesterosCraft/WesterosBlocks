package com.westerosblocks.datagen.custom;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.custom.WCCuboidNSEWUDBlock;
import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;

/**
 * Exporter for NSEWUD (6-directional) cuboid blocks.
 * Follows 1.18.2 pattern: extends CuboidBlockExporter and only customizes blockstate generation.
 * Model generation is handled entirely by parent class.
 */
public class CuboidNSEWUDBlockExporter extends CuboidBlockExporter {

    /**
     * Registers a NSEWUD cuboid block from a BlockDefinition.
     * Uses CuboidBlockExporter for model generation, only customizes blockstate for 6-directional facing.
     * Handles NORTH, EAST, SOUTH, WEST, UP, DOWN facing directions with proper model rotations.
     */
    public static void registerCustomCuboidNSEWUDBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        if (!(block instanceof WCCuboidNSEWUDBlock)) {
            throw new IllegalArgumentException("Block must be a WCCuboidNSEWUDBlock instance");
        }

        // Phase 1: Extract configuration
        var states = definition.getStates();
        if (states == null || states.isEmpty()) {
            throw new IllegalStateException("Block definition states should never be null/empty after doInit() for block: " + getBlockName(block));
        }
        boolean hasMultipleStates = definition.getStateCount() > 1;

        // Phase 2: Generate models (reuse CuboidBlockExporter completely via instance)
        CuboidNSEWUDBlockExporter exporter = new CuboidNSEWUDBlockExporter();
        Map<String, List<Identifier>> stateModelMap = exporter.generateModelsReturnMap(generator, block, definition);

        if (stateModelMap.isEmpty()) {
            WesterosBlocks.LOGGER.warn("No models generated for NSEWUD cuboid block: {}", getBlockName(block));
            return;
        }

        // Phase 3: Generate NSEWUD-specific blockstate (facing=north/east/south/west/up/down variants)
        generator.blockStateCollector.accept(generateNSEWUDBlockState(block, stateModelMap, states, hasMultipleStates));

        // Phase 4: Register item model
        Identifier firstModel = stateModelMap.values().iterator().next().get(0);
        registerParentedItemModel(generator, block, firstModel);
    }

    /**
     * Generates NSEWUD-specific blockstate JSON with facing variants.
     * Handles both single-state and multi-state blocks.
     * Creates "facing=north/east/south/west/up/down" variants with proper x/y rotations for each state.
     */
    private static BlockStateSupplier generateNSEWUDBlockState(Block block,
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

                    // Get rotation offset from state if defined
                    int rotYOffset = 0;
                    if (state.getRotYOffset() != null) {
                        rotYOffset = state.getRotYOffset().intValue();
                    }

                    // Determine variant key prefix based on whether block has multiple states
                    String northKey = hasMultipleStates ? "facing=north,state=" + stateId : "facing=north";
                    String eastKey = hasMultipleStates ? "facing=east,state=" + stateId : "facing=east";
                    String southKey = hasMultipleStates ? "facing=south,state=" + stateId : "facing=south";
                    String westKey = hasMultipleStates ? "facing=west,state=" + stateId : "facing=west";
                    String upKey = hasMultipleStates ? "facing=up,state=" + stateId : "facing=up";
                    String downKey = hasMultipleStates ? "facing=down,state=" + stateId : "facing=down";

                    // Get random textures for weight information
                    List<BlockDefinition.RandomTextureVariant> randomTextures = state.getRandomTextures();

                    if (randomTextures != null && !randomTextures.isEmpty() && modelIds.size() > 1) {
                        // Multiple models with weights - create array variants
                        JsonArray northVariants = new JsonArray();
                        JsonArray eastVariants = new JsonArray();
                        JsonArray southVariants = new JsonArray();
                        JsonArray westVariants = new JsonArray();
                        JsonArray upVariants = new JsonArray();
                        JsonArray downVariants = new JsonArray();

                        for (int i = 0; i < modelIds.size() && i < randomTextures.size(); i++) {
                            int weight = randomTextures.get(i).getWeight();

                            // NORTH (0° + offset)
                            for (int w = 0; w < weight; w++) {
                                JsonObject variant = new JsonObject();
                                variant.addProperty("model", modelIds.get(i).toString());
                                int rot = rotYOffset % 360;
                                if (rot > 0) variant.addProperty("y", rot);
                                northVariants.add(variant);
                            }

                            // EAST (90° + offset)
                            for (int w = 0; w < weight; w++) {
                                JsonObject variant = new JsonObject();
                                variant.addProperty("model", modelIds.get(i).toString());
                                int rot = (90 + rotYOffset) % 360;
                                if (rot > 0) variant.addProperty("y", rot);
                                eastVariants.add(variant);
                            }

                            // SOUTH (180° + offset)
                            for (int w = 0; w < weight; w++) {
                                JsonObject variant = new JsonObject();
                                variant.addProperty("model", modelIds.get(i).toString());
                                int rot = (180 + rotYOffset) % 360;
                                if (rot > 0) variant.addProperty("y", rot);
                                southVariants.add(variant);
                            }

                            // WEST (270° + offset)
                            for (int w = 0; w < weight; w++) {
                                JsonObject variant = new JsonObject();
                                variant.addProperty("model", modelIds.get(i).toString());
                                int rot = (270 + rotYOffset) % 360;
                                if (rot > 0) variant.addProperty("y", rot);
                                westVariants.add(variant);
                            }

                            // UP (x=270, y=offset)
                            for (int w = 0; w < weight; w++) {
                                JsonObject variant = new JsonObject();
                                variant.addProperty("model", modelIds.get(i).toString());
                                variant.addProperty("x", 270);
                                int rot = rotYOffset % 360;
                                if (rot > 0) variant.addProperty("y", rot);
                                upVariants.add(variant);
                            }

                            // DOWN (x=90, y=offset)
                            for (int w = 0; w < weight; w++) {
                                JsonObject variant = new JsonObject();
                                variant.addProperty("model", modelIds.get(i).toString());
                                variant.addProperty("x", 90);
                                int rot = rotYOffset % 360;
                                if (rot > 0) variant.addProperty("y", rot);
                                downVariants.add(variant);
                            }
                        }

                        variants.add(northKey, northVariants);
                        variants.add(eastKey, eastVariants);
                        variants.add(southKey, southVariants);
                        variants.add(westKey, westVariants);
                        variants.add(upKey, upVariants);
                        variants.add(downKey, downVariants);
                    } else {
                        // Single model - simple variants
                        Identifier modelId = modelIds.get(0);

                        // NORTH (0° + offset)
                        JsonObject northVariant = new JsonObject();
                        northVariant.addProperty("model", modelId.toString());
                        int northRot = rotYOffset % 360;
                        if (northRot > 0) northVariant.addProperty("y", northRot);
                        variants.add(northKey, northVariant);

                        // EAST (90° + offset)
                        JsonObject eastVariant = new JsonObject();
                        eastVariant.addProperty("model", modelId.toString());
                        int eastRot = (90 + rotYOffset) % 360;
                        if (eastRot > 0) eastVariant.addProperty("y", eastRot);
                        variants.add(eastKey, eastVariant);

                        // SOUTH (180° + offset)
                        JsonObject southVariant = new JsonObject();
                        southVariant.addProperty("model", modelId.toString());
                        int southRot = (180 + rotYOffset) % 360;
                        if (southRot > 0) southVariant.addProperty("y", southRot);
                        variants.add(southKey, southVariant);

                        // WEST (270° + offset)
                        JsonObject westVariant = new JsonObject();
                        westVariant.addProperty("model", modelId.toString());
                        int westRot = (270 + rotYOffset) % 360;
                        if (westRot > 0) westVariant.addProperty("y", westRot);
                        variants.add(westKey, westVariant);

                        // UP (x=270, y=offset)
                        JsonObject upVariant = new JsonObject();
                        upVariant.addProperty("model", modelId.toString());
                        upVariant.addProperty("x", 270);
                        int upRot = rotYOffset % 360;
                        if (upRot > 0) upVariant.addProperty("y", upRot);
                        variants.add(upKey, upVariant);

                        // DOWN (x=90, y=offset)
                        JsonObject downVariant = new JsonObject();
                        downVariant.addProperty("model", modelId.toString());
                        downVariant.addProperty("x", 90);
                        int downRot = rotYOffset % 360;
                        if (downRot > 0) downVariant.addProperty("y", downRot);
                        variants.add(downKey, downVariant);
                    }
                }

                json.add("variants", variants);
                return json;
            }
        };
    }
}
