package com.westerosblocks.datagen.custom;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.westerosblocks.block.custom.WCStairBlock;
import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Exporter for stair blocks following block-models.md patterns.
 * Generates models for stair blocks with all facing, half, and shape combinations.
 *
 * @see WCStairBlock
 */
public class StairBlockExporter extends BaseBlockExporter {

    /**
     * Registers a stair block from a BlockDefinition.
     * Uses uniform iteration pattern: After doInit(), states is ALWAYS non-empty,
     * and each state has randomTextures normalized from simple textures.
     */
    public static void registerCustomStairBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        if (!(block instanceof WCStairBlock stairBlock)) {
            throw new IllegalArgumentException("Block must be a WCStairBlock instance");
        }

        // After doInit(), states is ALWAYS non-empty (at least synthetic base state exists)
        var states = definition.getStates();

        if (states == null || states.isEmpty()) {
            throw new IllegalStateException("Block definition states should never be null/empty after doInit() for block: " + getBlockName(block));
        }

        // Determine if this block actually has multiple states (needs STATE property in variants)
        boolean hasMultipleStates = definition.getStateCount() > 1;

        // Check for custom model first
        if (definition.hasCustomModel()) {
            registerCustomModelStairBlock(generator, block, definition, stairBlock);
            return;
        }

        // Collect all model sets for all states
        Map<String, List<StairModelSet>> stateModelMap = new HashMap<>();
        Identifier firstModel = null;

        for (BlockDefinition.StateVariant state : states) {
            String stateId = state.getStateID();
            stateId = getStateIdOrBase(stateId);

            List<StairModelSet> modelSets = new ArrayList<>();

            // Check if we have texture sets to work with
            int textureSetCount = state.getRandomTextureSetCount();

            if (textureSetCount == 0) {
                // No texture sets - skip this state or use fallback
                continue;
            }

            // Iterate through all texture sets for this state
            for (int setIdx = 0; setIdx < textureSetCount; setIdx++) {
                BlockDefinition.RandomTextureVariant set = state.getRandomTextureSet(setIdx);
                if (set == null || set.getTextureCount() == 0) {
                    continue;
                }

                // Extract textures from this set
                String[] textures = new String[set.getTextureCount()];
                for (int i = 0; i < set.getTextureCount(); i++) {
                    textures[i] = set.getTextureByIndex(i);
                }

                // Generate the three stair models for this texture set
                Identifier baseModel = generateStairModelFromArray(generator, block, definition, textures, "base", setIdx, hasMultipleStates ? stateId : null, stairBlock);
                Identifier innerModel = generateStairModelFromArray(generator, block, definition, textures, "inner", setIdx, hasMultipleStates ? stateId : null, stairBlock);
                Identifier outerModel = generateStairModelFromArray(generator, block, definition, textures, "outer", setIdx, hasMultipleStates ? stateId : null, stairBlock);

                modelSets.add(new StairModelSet(baseModel, innerModel, outerModel, set.getWeight()));

                if (firstModel == null) firstModel = baseModel;
            }

            if (!modelSets.isEmpty()) {
                stateModelMap.put(stateId, modelSets);
            }
        }

        if (stateModelMap.isEmpty()) {
            // Fallback if no valid models generated
            registerFallbackStairBlock(generator, block, definition, stairBlock);
            return;
        }

        // Generate blockstate based on whether we have multiple states
        if (hasMultipleStates) {
            generator.blockStateCollector.accept(createStairBlockStateWithStates(block, stateModelMap, stairBlock));
        } else {
            // Single state - no state prefix in variants
            List<StairModelSet> modelSets = stateModelMap.values().iterator().next();
            if (modelSets.size() == 1) {
                // Single texture set
                StairModelSet modelSet = modelSets.get(0);
                generator.blockStateCollector.accept(createStairBlockState(block, modelSet.base, modelSet.inner, modelSet.outer, stairBlock.no_uvlock));
            } else {
                // Multiple texture sets (random textures)
                generator.blockStateCollector.accept(createStairBlockStateWithRandomTextures(block, modelSets, stairBlock.no_uvlock));
            }
        }

        // Register item model
        if (firstModel != null) {
            registerParentedItemModel(generator, block, firstModel);
        }
    }


    private static void registerCustomModelStairBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition, WCStairBlock stairBlock) {
        Identifier baseModel = createCustomModelId(block, "base_v1");
        Identifier innerModel = createCustomModelId(block, "inner_v1");
        Identifier outerModel = createCustomModelId(block, "outer_v1");

        generator.blockStateCollector.accept(createStairBlockState(block, baseModel, innerModel, outerModel, stairBlock.no_uvlock));
        registerParentedItemModel(generator, block, baseModel);
    }

    private static void registerFallbackStairBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition, WCStairBlock stairBlock) {
        // Use missing texture as fallback
        String[] fallbackTextures = new String[]{"missing", "missing", "missing"};
        Identifier baseModel = generateStairModelFromArray(generator, block, definition, fallbackTextures, "base", 0, null, stairBlock);
        Identifier innerModel = generateStairModelFromArray(generator, block, definition, fallbackTextures, "inner", 0, null, stairBlock);
        Identifier outerModel = generateStairModelFromArray(generator, block, definition, fallbackTextures, "outer", 0, null, stairBlock);

        generator.blockStateCollector.accept(createStairBlockState(block, baseModel, innerModel, outerModel, stairBlock.no_uvlock));
        registerParentedItemModel(generator, block, baseModel);
    }

    /**
     * Generates a stair model from a texture array.
     */
    private static Identifier generateStairModelFromArray(BlockStateModelGenerator generator, Block block, BlockDefinition definition,
                                                         String[] textures, String type, int variantIndex, String stateId, WCStairBlock stairBlock) {
        String variantName = (stateId != null ? stateId + "_" : "") + type + "_v" + (variantIndex + 1);
        Identifier modelId = createGeneratedModelId(block, variantName);

        // Create model JSON
        JsonObject modelJson = new JsonObject();

        // Determine parent model based on properties
        boolean isOccluded = true; // Stairs typically have ambient occlusion enabled
        boolean isTinted = definition.isTinted();
        boolean hasOverlay = definition.hasOverlay();

        String parentPath = buildStairParentPath(type, isOccluded, isTinted, hasOverlay);
        modelJson.addProperty("parent", parentPath);

        // Add textures
        JsonObject texturesJson = new JsonObject();
        String bottomTex = textures.length > 0 ? textures[0] : "missing";
        String topTex = textures.length > 1 ? textures[1] : bottomTex;
        String sideTex = textures.length > 2 ? textures[2] : topTex;

        texturesJson.addProperty("bottom", "westerosblocks:block/" + bottomTex);
        texturesJson.addProperty("top", "westerosblocks:block/" + topTex);
        texturesJson.addProperty("side", "westerosblocks:block/" + sideTex);
        texturesJson.addProperty("particle", "westerosblocks:block/" + sideTex);

        // Add overlay textures if present
        if (hasOverlay && definition.getOverlayTextures() != null) {
            List<String> overlayTextures = definition.getOverlayTextures();
            if (overlayTextures.size() > 0) {
                texturesJson.addProperty("bottom_ov", "westerosblocks:block/" + overlayTextures.get(0));
            }
            if (overlayTextures.size() > 1) {
                texturesJson.addProperty("top_ov", "westerosblocks:block/" + overlayTextures.get(1));
            }
            if (overlayTextures.size() > 2) {
                texturesJson.addProperty("side_ov", "westerosblocks:block/" + overlayTextures.get(2));
            }
        }

        modelJson.add("textures", texturesJson);

        // Upload model
        generator.modelCollector.accept(modelId, () -> modelJson);

        return modelId;
    }


    /**
     * Builds the parent path for stair models based on properties.
     */
    private static String buildStairParentPath(String type, boolean isOccluded, boolean isTinted, boolean hasOverlay) {
        StringBuilder path = new StringBuilder("westerosblocks:block/");

        if (isOccluded) {
            path.append(isTinted ? "tinted/" : "untinted/");
        } else {
            path.append(isTinted ? "tintednoocclusion/" : "noocclusion/");
        }

        switch (type) {
            case "inner" -> path.append("inner_stairs");
            case "outer" -> path.append("outer_stairs");
            default -> path.append("stairs");
        }

        if (hasOverlay) {
            path.append("_overlay");
        }

        return path.toString();
    }

    /**
     * Creates a blockstate for stairs with all facing/half/shape combinations.
     */
    private static BlockStateSupplier createStairBlockState(Block block, Identifier baseModel, Identifier innerModel, Identifier outerModel, boolean noUvlock) {
        return new BlockStateSupplier() {
            @Override
            public Block getBlock() {
                return block;
            }

            @Override
            public JsonElement get() {
                JsonObject json = new JsonObject();
                JsonObject variants = new JsonObject();

                addStairVariants(variants, baseModel, innerModel, outerModel, noUvlock);

                json.add("variants", variants);
                return json;
            }
        };
    }

    /**
     * Creates a blockstate for stairs with random textures.
     */
    private static BlockStateSupplier createStairBlockStateWithRandomTextures(Block block, List<StairModelSet> modelSets, boolean noUvlock) {
        return new BlockStateSupplier() {
            @Override
            public Block getBlock() {
                return block;
            }

            @Override
            public JsonElement get() {
                JsonObject json = new JsonObject();
                JsonObject variants = new JsonObject();

                addStairVariantsWithWeights(variants, modelSets, noUvlock);

                json.add("variants", variants);
                return json;
            }
        };
    }

    /**
     * Creates a blockstate for stairs with states.
     */
    private static BlockStateSupplier createStairBlockStateWithStates(Block block, Map<String, List<StairModelSet>> stateModelMap, WCStairBlock stairBlock) {
        return new BlockStateSupplier() {
            @Override
            public Block getBlock() {
                return block;
            }

            @Override
            public JsonElement get() {
                JsonObject json = new JsonObject();
                JsonObject variants = new JsonObject();

                for (Map.Entry<String, List<StairModelSet>> entry : stateModelMap.entrySet()) {
                    String stateId = entry.getKey();
                    List<StairModelSet> modelSets = entry.getValue();

                    if (modelSets != null && !modelSets.isEmpty()) {
                        addStairVariantsWithState(variants, modelSets, stateId, stairBlock.no_uvlock);
                    }
                }

                json.add("variants", variants);
                return json;
            }
        };
    }

    // Helper method to add all stair variants
    private static void addStairVariants(JsonObject variants, Identifier baseModel, Identifier innerModel, Identifier outerModel, boolean noUvlock) {
        // Bottom half
        addVariant(variants, "facing=east,half=bottom,shape=straight", baseModel, 0, 0, noUvlock);
        addVariant(variants, "facing=west,half=bottom,shape=straight", baseModel, 0, 180, noUvlock);
        addVariant(variants, "facing=south,half=bottom,shape=straight", baseModel, 0, 90, noUvlock);
        addVariant(variants, "facing=north,half=bottom,shape=straight", baseModel, 0, 270, noUvlock);

        addVariant(variants, "facing=east,half=bottom,shape=outer_right", outerModel, 0, 0, noUvlock);
        addVariant(variants, "facing=west,half=bottom,shape=outer_right", outerModel, 0, 180, noUvlock);
        addVariant(variants, "facing=south,half=bottom,shape=outer_right", outerModel, 0, 90, noUvlock);
        addVariant(variants, "facing=north,half=bottom,shape=outer_right", outerModel, 0, 270, noUvlock);

        addVariant(variants, "facing=east,half=bottom,shape=outer_left", outerModel, 0, 270, noUvlock);
        addVariant(variants, "facing=west,half=bottom,shape=outer_left", outerModel, 0, 90, noUvlock);
        addVariant(variants, "facing=south,half=bottom,shape=outer_left", outerModel, 0, 0, noUvlock);
        addVariant(variants, "facing=north,half=bottom,shape=outer_left", outerModel, 0, 180, noUvlock);

        addVariant(variants, "facing=east,half=bottom,shape=inner_right", innerModel, 0, 0, noUvlock);
        addVariant(variants, "facing=west,half=bottom,shape=inner_right", innerModel, 0, 180, noUvlock);
        addVariant(variants, "facing=south,half=bottom,shape=inner_right", innerModel, 0, 90, noUvlock);
        addVariant(variants, "facing=north,half=bottom,shape=inner_right", innerModel, 0, 270, noUvlock);

        addVariant(variants, "facing=east,half=bottom,shape=inner_left", innerModel, 0, 270, noUvlock);
        addVariant(variants, "facing=west,half=bottom,shape=inner_left", innerModel, 0, 90, noUvlock);
        addVariant(variants, "facing=south,half=bottom,shape=inner_left", innerModel, 0, 0, noUvlock);
        addVariant(variants, "facing=north,half=bottom,shape=inner_left", innerModel, 0, 180, noUvlock);

        // Top half
        addVariant(variants, "facing=east,half=top,shape=straight", baseModel, 180, 0, noUvlock);
        addVariant(variants, "facing=west,half=top,shape=straight", baseModel, 180, 180, noUvlock);
        addVariant(variants, "facing=south,half=top,shape=straight", baseModel, 180, 90, noUvlock);
        addVariant(variants, "facing=north,half=top,shape=straight", baseModel, 180, 270, noUvlock);

        addVariant(variants, "facing=east,half=top,shape=outer_right", outerModel, 180, 90, noUvlock);
        addVariant(variants, "facing=west,half=top,shape=outer_right", outerModel, 180, 270, noUvlock);
        addVariant(variants, "facing=south,half=top,shape=outer_right", outerModel, 180, 180, noUvlock);
        addVariant(variants, "facing=north,half=top,shape=outer_right", outerModel, 180, 0, noUvlock);

        addVariant(variants, "facing=east,half=top,shape=outer_left", outerModel, 180, 0, noUvlock);
        addVariant(variants, "facing=west,half=top,shape=outer_left", outerModel, 180, 180, noUvlock);
        addVariant(variants, "facing=south,half=top,shape=outer_left", outerModel, 180, 90, noUvlock);
        addVariant(variants, "facing=north,half=top,shape=outer_left", outerModel, 180, 270, noUvlock);

        addVariant(variants, "facing=east,half=top,shape=inner_right", innerModel, 180, 90, noUvlock);
        addVariant(variants, "facing=west,half=top,shape=inner_right", innerModel, 180, 270, noUvlock);
        addVariant(variants, "facing=south,half=top,shape=inner_right", innerModel, 180, 180, noUvlock);
        addVariant(variants, "facing=north,half=top,shape=inner_right", innerModel, 180, 0, noUvlock);

        addVariant(variants, "facing=east,half=top,shape=inner_left", innerModel, 180, 0, noUvlock);
        addVariant(variants, "facing=west,half=top,shape=inner_left", innerModel, 180, 180, noUvlock);
        addVariant(variants, "facing=south,half=top,shape=inner_left", innerModel, 180, 90, noUvlock);
        addVariant(variants, "facing=north,half=top,shape=inner_left", innerModel, 180, 270, noUvlock);
    }

    // Helper method to add variant with rotation
    private static void addVariant(JsonObject variants, String condition, Identifier model, int x, int y, boolean noUvlock) {
        JsonObject variant = new JsonObject();
        variant.addProperty("model", model.toString());
        if (x != 0) variant.addProperty("x", x);
        if (y != 0) variant.addProperty("y", y);
        if (!noUvlock && (x != 0 || y != 0)) {
            variant.addProperty("uvlock", true);
        }
        variants.add(condition, variant);
    }

    /**
     * Adds stair variants with weighted random texture models.
     */
    private static void addStairVariantsWithWeights(JsonObject variants, List<StairModelSet> modelSets, boolean noUvlock) {
        // Bottom half
        addWeightedVariant(variants, "facing=east,half=bottom,shape=straight", modelSets, "base", 0, 0, noUvlock);
        addWeightedVariant(variants, "facing=west,half=bottom,shape=straight", modelSets, "base", 0, 180, noUvlock);
        addWeightedVariant(variants, "facing=south,half=bottom,shape=straight", modelSets, "base", 0, 90, noUvlock);
        addWeightedVariant(variants, "facing=north,half=bottom,shape=straight", modelSets, "base", 0, 270, noUvlock);

        addWeightedVariant(variants, "facing=east,half=bottom,shape=outer_right", modelSets, "outer", 0, 0, noUvlock);
        addWeightedVariant(variants, "facing=west,half=bottom,shape=outer_right", modelSets, "outer", 0, 180, noUvlock);
        addWeightedVariant(variants, "facing=south,half=bottom,shape=outer_right", modelSets, "outer", 0, 90, noUvlock);
        addWeightedVariant(variants, "facing=north,half=bottom,shape=outer_right", modelSets, "outer", 0, 270, noUvlock);

        addWeightedVariant(variants, "facing=east,half=bottom,shape=outer_left", modelSets, "outer", 0, 270, noUvlock);
        addWeightedVariant(variants, "facing=west,half=bottom,shape=outer_left", modelSets, "outer", 0, 90, noUvlock);
        addWeightedVariant(variants, "facing=south,half=bottom,shape=outer_left", modelSets, "outer", 0, 0, noUvlock);
        addWeightedVariant(variants, "facing=north,half=bottom,shape=outer_left", modelSets, "outer", 0, 180, noUvlock);

        addWeightedVariant(variants, "facing=east,half=bottom,shape=inner_right", modelSets, "inner", 0, 0, noUvlock);
        addWeightedVariant(variants, "facing=west,half=bottom,shape=inner_right", modelSets, "inner", 0, 180, noUvlock);
        addWeightedVariant(variants, "facing=south,half=bottom,shape=inner_right", modelSets, "inner", 0, 90, noUvlock);
        addWeightedVariant(variants, "facing=north,half=bottom,shape=inner_right", modelSets, "inner", 0, 270, noUvlock);

        addWeightedVariant(variants, "facing=east,half=bottom,shape=inner_left", modelSets, "inner", 0, 270, noUvlock);
        addWeightedVariant(variants, "facing=west,half=bottom,shape=inner_left", modelSets, "inner", 0, 90, noUvlock);
        addWeightedVariant(variants, "facing=south,half=bottom,shape=inner_left", modelSets, "inner", 0, 0, noUvlock);
        addWeightedVariant(variants, "facing=north,half=bottom,shape=inner_left", modelSets, "inner", 0, 180, noUvlock);

        // Top half
        addWeightedVariant(variants, "facing=east,half=top,shape=straight", modelSets, "base", 180, 0, noUvlock);
        addWeightedVariant(variants, "facing=west,half=top,shape=straight", modelSets, "base", 180, 180, noUvlock);
        addWeightedVariant(variants, "facing=south,half=top,shape=straight", modelSets, "base", 180, 90, noUvlock);
        addWeightedVariant(variants, "facing=north,half=top,shape=straight", modelSets, "base", 180, 270, noUvlock);

        addWeightedVariant(variants, "facing=east,half=top,shape=outer_right", modelSets, "outer", 180, 90, noUvlock);
        addWeightedVariant(variants, "facing=west,half=top,shape=outer_right", modelSets, "outer", 180, 270, noUvlock);
        addWeightedVariant(variants, "facing=south,half=top,shape=outer_right", modelSets, "outer", 180, 180, noUvlock);
        addWeightedVariant(variants, "facing=north,half=top,shape=outer_right", modelSets, "outer", 180, 0, noUvlock);

        addWeightedVariant(variants, "facing=east,half=top,shape=outer_left", modelSets, "outer", 180, 0, noUvlock);
        addWeightedVariant(variants, "facing=west,half=top,shape=outer_left", modelSets, "outer", 180, 180, noUvlock);
        addWeightedVariant(variants, "facing=south,half=top,shape=outer_left", modelSets, "outer", 180, 90, noUvlock);
        addWeightedVariant(variants, "facing=north,half=top,shape=outer_left", modelSets, "outer", 180, 270, noUvlock);

        addWeightedVariant(variants, "facing=east,half=top,shape=inner_right", modelSets, "inner", 180, 90, noUvlock);
        addWeightedVariant(variants, "facing=west,half=top,shape=inner_right", modelSets, "inner", 180, 270, noUvlock);
        addWeightedVariant(variants, "facing=south,half=top,shape=inner_right", modelSets, "inner", 180, 180, noUvlock);
        addWeightedVariant(variants, "facing=north,half=top,shape=inner_right", modelSets, "inner", 180, 0, noUvlock);

        addWeightedVariant(variants, "facing=east,half=top,shape=inner_left", modelSets, "inner", 180, 0, noUvlock);
        addWeightedVariant(variants, "facing=west,half=top,shape=inner_left", modelSets, "inner", 180, 180, noUvlock);
        addWeightedVariant(variants, "facing=south,half=top,shape=inner_left", modelSets, "inner", 180, 90, noUvlock);
        addWeightedVariant(variants, "facing=north,half=top,shape=inner_left", modelSets, "inner", 180, 270, noUvlock);
    }

    /**
     * Helper to add a weighted variant with multiple random texture models.
     */
    private static void addWeightedVariant(JsonObject variants, String condition, List<StairModelSet> modelSets,
                                          String modelType, int x, int y, boolean noUvlock) {
        com.google.gson.JsonArray variantArray = new com.google.gson.JsonArray();

        for (StairModelSet modelSet : modelSets) {
            JsonObject variant = new JsonObject();

            // Select the appropriate model (base, inner, or outer)
            Identifier model = switch (modelType) {
                case "inner" -> modelSet.inner;
                case "outer" -> modelSet.outer;
                default -> modelSet.base;
            };

            variant.addProperty("model", model.toString());
            if (x != 0) variant.addProperty("x", x);
            if (y != 0) variant.addProperty("y", y);
            if (!noUvlock && (x != 0 || y != 0)) {
                variant.addProperty("uvlock", true);
            }
            if (modelSet.weight > 1) {
                variant.addProperty("weight", modelSet.weight);
            }

            variantArray.add(variant);
        }

        variants.add(condition, variantArray);
    }

    /**
     * Adds stair variants with state prefix.
     */
    private static void addStairVariantsWithState(JsonObject variants, List<StairModelSet> modelSets, String stateId, boolean noUvlock) {
        // Bottom half
        addStateVariant(variants, stateId, "facing=east,half=bottom,shape=straight", modelSets, "base", 0, 0, noUvlock);
        addStateVariant(variants, stateId, "facing=west,half=bottom,shape=straight", modelSets, "base", 0, 180, noUvlock);
        addStateVariant(variants, stateId, "facing=south,half=bottom,shape=straight", modelSets, "base", 0, 90, noUvlock);
        addStateVariant(variants, stateId, "facing=north,half=bottom,shape=straight", modelSets, "base", 0, 270, noUvlock);

        addStateVariant(variants, stateId, "facing=east,half=bottom,shape=outer_right", modelSets, "outer", 0, 0, noUvlock);
        addStateVariant(variants, stateId, "facing=west,half=bottom,shape=outer_right", modelSets, "outer", 0, 180, noUvlock);
        addStateVariant(variants, stateId, "facing=south,half=bottom,shape=outer_right", modelSets, "outer", 0, 90, noUvlock);
        addStateVariant(variants, stateId, "facing=north,half=bottom,shape=outer_right", modelSets, "outer", 0, 270, noUvlock);

        addStateVariant(variants, stateId, "facing=east,half=bottom,shape=outer_left", modelSets, "outer", 0, 270, noUvlock);
        addStateVariant(variants, stateId, "facing=west,half=bottom,shape=outer_left", modelSets, "outer", 0, 90, noUvlock);
        addStateVariant(variants, stateId, "facing=south,half=bottom,shape=outer_left", modelSets, "outer", 0, 0, noUvlock);
        addStateVariant(variants, stateId, "facing=north,half=bottom,shape=outer_left", modelSets, "outer", 0, 180, noUvlock);

        addStateVariant(variants, stateId, "facing=east,half=bottom,shape=inner_right", modelSets, "inner", 0, 0, noUvlock);
        addStateVariant(variants, stateId, "facing=west,half=bottom,shape=inner_right", modelSets, "inner", 0, 180, noUvlock);
        addStateVariant(variants, stateId, "facing=south,half=bottom,shape=inner_right", modelSets, "inner", 0, 90, noUvlock);
        addStateVariant(variants, stateId, "facing=north,half=bottom,shape=inner_right", modelSets, "inner", 0, 270, noUvlock);

        addStateVariant(variants, stateId, "facing=east,half=bottom,shape=inner_left", modelSets, "inner", 0, 270, noUvlock);
        addStateVariant(variants, stateId, "facing=west,half=bottom,shape=inner_left", modelSets, "inner", 0, 90, noUvlock);
        addStateVariant(variants, stateId, "facing=south,half=bottom,shape=inner_left", modelSets, "inner", 0, 0, noUvlock);
        addStateVariant(variants, stateId, "facing=north,half=bottom,shape=inner_left", modelSets, "inner", 0, 180, noUvlock);

        // Top half
        addStateVariant(variants, stateId, "facing=east,half=top,shape=straight", modelSets, "base", 180, 0, noUvlock);
        addStateVariant(variants, stateId, "facing=west,half=top,shape=straight", modelSets, "base", 180, 180, noUvlock);
        addStateVariant(variants, stateId, "facing=south,half=top,shape=straight", modelSets, "base", 180, 90, noUvlock);
        addStateVariant(variants, stateId, "facing=north,half=top,shape=straight", modelSets, "base", 180, 270, noUvlock);

        addStateVariant(variants, stateId, "facing=east,half=top,shape=outer_right", modelSets, "outer", 180, 90, noUvlock);
        addStateVariant(variants, stateId, "facing=west,half=top,shape=outer_right", modelSets, "outer", 180, 270, noUvlock);
        addStateVariant(variants, stateId, "facing=south,half=top,shape=outer_right", modelSets, "outer", 180, 180, noUvlock);
        addStateVariant(variants, stateId, "facing=north,half=top,shape=outer_right", modelSets, "outer", 180, 0, noUvlock);

        addStateVariant(variants, stateId, "facing=east,half=top,shape=outer_left", modelSets, "outer", 180, 0, noUvlock);
        addStateVariant(variants, stateId, "facing=west,half=top,shape=outer_left", modelSets, "outer", 180, 180, noUvlock);
        addStateVariant(variants, stateId, "facing=south,half=top,shape=outer_left", modelSets, "outer", 180, 90, noUvlock);
        addStateVariant(variants, stateId, "facing=north,half=top,shape=outer_left", modelSets, "outer", 180, 270, noUvlock);

        addStateVariant(variants, stateId, "facing=east,half=top,shape=inner_right", modelSets, "inner", 180, 90, noUvlock);
        addStateVariant(variants, stateId, "facing=west,half=top,shape=inner_right", modelSets, "inner", 180, 270, noUvlock);
        addStateVariant(variants, stateId, "facing=south,half=top,shape=inner_right", modelSets, "inner", 180, 180, noUvlock);
        addStateVariant(variants, stateId, "facing=north,half=top,shape=inner_right", modelSets, "inner", 180, 0, noUvlock);

        addStateVariant(variants, stateId, "facing=east,half=top,shape=inner_left", modelSets, "inner", 180, 0, noUvlock);
        addStateVariant(variants, stateId, "facing=west,half=top,shape=inner_left", modelSets, "inner", 180, 180, noUvlock);
        addStateVariant(variants, stateId, "facing=south,half=top,shape=inner_left", modelSets, "inner", 180, 90, noUvlock);
        addStateVariant(variants, stateId, "facing=north,half=top,shape=inner_left", modelSets, "inner", 180, 270, noUvlock);
    }

    /**
     * Helper to add a state variant with weighted random textures.
     */
    private static void addStateVariant(JsonObject variants, String stateId, String condition,
                                       List<StairModelSet> modelSets, String modelType, int x, int y, boolean noUvlock) {
        String fullCondition = "state=" + stateId + "," + condition;

        if (modelSets.size() == 1) {
            // Single model
            StairModelSet modelSet = modelSets.get(0);
            Identifier model = switch (modelType) {
                case "inner" -> modelSet.inner;
                case "outer" -> modelSet.outer;
                default -> modelSet.base;
            };

            JsonObject variant = new JsonObject();
            variant.addProperty("model", model.toString());
            if (x != 0) variant.addProperty("x", x);
            if (y != 0) variant.addProperty("y", y);
            if (!noUvlock && (x != 0 || y != 0)) {
                variant.addProperty("uvlock", true);
            }

            variants.add(fullCondition, variant);
        } else {
            // Multiple weighted models
            addWeightedVariant(variants, fullCondition, modelSets, modelType, x, y, noUvlock);
        }
    }

    /**
     * Helper class to hold a set of stair models.
     */
    private static class StairModelSet {
        final Identifier base;
        final Identifier inner;
        final Identifier outer;
        final int weight;

        StairModelSet(Identifier base, Identifier inner, Identifier outer, int weight) {
            this.base = base;
            this.inner = inner;
            this.outer = outer;
            this.weight = weight;
        }
    }
}
