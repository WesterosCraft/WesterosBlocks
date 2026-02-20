package com.westerosblocks.datagen.custom;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;

/**
 * Exporter for mounted (wall-facing) blocks with HORIZONTAL_FACING.
 * Extends CuboidBlockExporter to support both custom models and texture-defined cuboid models.
 * Custom models use NORTH=0° convention; generated cuboid models use EAST=0° convention.
 */
public class MountedBlockExporter extends CuboidBlockExporter {

    /**
     * Registers a mounted block with HORIZONTAL_FACING variants.
     * Supports both custom models (isCustomModel: true) and texture-defined cuboid models.
     */
    public static void registerMountedBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        String blockName = getBlockName(block);

        // Phase 1: Extract configuration
        var states = definition.getStates();
        if (states == null || states.isEmpty()) {
            throw new IllegalStateException("Block definition states should never be null/empty after doInit() for block: " + blockName);
        }
        boolean hasMultipleStates = definition.getStateCount() > 1;

        // Phase 2: Generate models (reuse CuboidBlockExporter via instance)
        MountedBlockExporter exporter = new MountedBlockExporter();
        Map<String, List<Identifier>> stateModelMap = exporter.generateModelsReturnMap(generator, block, definition);

        if (stateModelMap.isEmpty()) {
            WesterosBlocks.LOGGER.warn("No models generated for mounted block: {}", blockName);
            return;
        }

        // Phase 3: Generate blockstate with HORIZONTAL_FACING variants
        generator.blockStateCollector.accept(generateMountedBlockState(block, stateModelMap, states, hasMultipleStates));

        // Phase 4: Register item model
        Identifier firstModel = stateModelMap.values().iterator().next().get(0);
        registerParentedItemModel(generator, block, firstModel);
    }

    /**
     * Generates mounted block blockstate JSON with facing variants.
     * Handles both single-state and multi-state blocks.
     * Custom models use NORTH=0° convention; generated models use EAST=0° convention.
     */
    private static BlockStateSupplier generateMountedBlockState(Block block,
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

                    // Rotation convention depends on model type:
                    // Custom models face NORTH at 0°: N=0, E=90, S=180, W=270
                    // Generated cuboid models face EAST at 0°: N=270, E=0, S=90, W=180
                    boolean isCustom = state.isCustomModel();
                    int northRot = isCustom ? 0 : 270;
                    int eastRot = isCustom ? 90 : 0;
                    int southRot = isCustom ? 180 : 90;
                    int westRot = isCustom ? 270 : 180;

                    // Determine variant key prefix based on whether block has multiple states
                    String northKey = hasMultipleStates ? "facing=north,state=" + stateId : "facing=north";
                    String eastKey = hasMultipleStates ? "facing=east,state=" + stateId : "facing=east";
                    String southKey = hasMultipleStates ? "facing=south,state=" + stateId : "facing=south";
                    String westKey = hasMultipleStates ? "facing=west,state=" + stateId : "facing=west";

                    // Get random textures for weight information
                    List<BlockDefinition.RandomTextureVariant> randomTextures = state.getRandomTextures();

                    if (randomTextures != null && !randomTextures.isEmpty() && modelIds.size() > 1) {
                        // Multiple models with weights - create array variants
                        JsonArray northVariants = new JsonArray();
                        JsonArray eastVariants = new JsonArray();
                        JsonArray southVariants = new JsonArray();
                        JsonArray westVariants = new JsonArray();

                        for (int i = 0; i < modelIds.size() && i < randomTextures.size(); i++) {
                            int weight = randomTextures.get(i).getWeight();

                            for (int w = 0; w < weight; w++) {
                                JsonObject nv = new JsonObject();
                                nv.addProperty("model", modelIds.get(i).toString());
                                int rot = (northRot + rotYOffset) % 360;
                                if (rot > 0) nv.addProperty("y", rot);
                                northVariants.add(nv);
                            }

                            for (int w = 0; w < weight; w++) {
                                JsonObject ev = new JsonObject();
                                ev.addProperty("model", modelIds.get(i).toString());
                                int rot = (eastRot + rotYOffset) % 360;
                                if (rot > 0) ev.addProperty("y", rot);
                                eastVariants.add(ev);
                            }

                            for (int w = 0; w < weight; w++) {
                                JsonObject sv = new JsonObject();
                                sv.addProperty("model", modelIds.get(i).toString());
                                int rot = (southRot + rotYOffset) % 360;
                                if (rot > 0) sv.addProperty("y", rot);
                                southVariants.add(sv);
                            }

                            for (int w = 0; w < weight; w++) {
                                JsonObject wv = new JsonObject();
                                wv.addProperty("model", modelIds.get(i).toString());
                                int rot = (westRot + rotYOffset) % 360;
                                if (rot > 0) wv.addProperty("y", rot);
                                westVariants.add(wv);
                            }
                        }

                        variants.add(northKey, northVariants);
                        variants.add(eastKey, eastVariants);
                        variants.add(southKey, southVariants);
                        variants.add(westKey, westVariants);
                    } else {
                        // Single model - simple variants
                        Identifier modelId = modelIds.get(0);

                        JsonObject northVariant = new JsonObject();
                        northVariant.addProperty("model", modelId.toString());
                        int nRot = (northRot + rotYOffset) % 360;
                        if (nRot > 0) northVariant.addProperty("y", nRot);
                        variants.add(northKey, northVariant);

                        JsonObject eastVariant = new JsonObject();
                        eastVariant.addProperty("model", modelId.toString());
                        int eRot = (eastRot + rotYOffset) % 360;
                        if (eRot > 0) eastVariant.addProperty("y", eRot);
                        variants.add(eastKey, eastVariant);

                        JsonObject southVariant = new JsonObject();
                        southVariant.addProperty("model", modelId.toString());
                        int sRot = (southRot + rotYOffset) % 360;
                        if (sRot > 0) southVariant.addProperty("y", sRot);
                        variants.add(southKey, southVariant);

                        JsonObject westVariant = new JsonObject();
                        westVariant.addProperty("model", modelId.toString());
                        int wRot = (westRot + rotYOffset) % 360;
                        if (wRot > 0) westVariant.addProperty("y", wRot);
                        variants.add(westKey, westVariant);
                    }
                }

                json.add("variants", variants);
                return json;
            }
        };
    }
}
