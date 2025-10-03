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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.westerosblocks.datagen.custom.CuboidBlockExporter.createCuboidModel;

/**
 * Exporter for NSEWD (6-direction) cuboid blocks with directional facing support.
 * Generates blockstate files with facing variants for all 6 directions including UP and DOWN.
 */
public class CuboidNSEWUDBlockExporter extends BaseBlockExporter {

    public static void registerCustomCuboidNSEWUDBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        if (!(block instanceof WCCuboidNSEWUDBlock)) {
            throw new IllegalArgumentException("Block must be a WCCuboidNSEWUDBlock instance");
        }

        if (definition.hasStates()) {
            registerCuboidNSEWUDBlockWithStates(generator, block, definition);
        } else if (definition.hasRandomTextures() && hasActualRandomTextures(definition)) {
            registerCuboidNSEWUDBlockWithRandomTextures(generator, block, definition);
        } else if (definition.hasCustomModel()) {
            // Reference pre-existing custom model
            registerCustomModelCuboidNSEWUDBlock(generator, block, definition);
        } else {
            // Generate model from cuboids or textures
            registerSimpleCuboidNSEWUDBlock(generator, block, definition);
        }
    }

    /**
     * Registers a simple cuboid block with generated model from cuboids or textures.
     */
    private static void registerSimpleCuboidNSEWUDBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        List<String> textures = definition.getTextures();

        Identifier modelId;
        if (definition.hasCustomModel()) {
            // Reference pre-existing custom model
            modelId = createCustomModelId(block, "base_v1");
        } else if (hasCuboids(definition)) {
            // Generate model from cuboids array
            modelId = createCuboidModel(generator, block, definition, textures, 0, "base_v1");
        } else {
            // Fallback: reference custom model
            modelId = createCustomModelId(block, "base_v1");
        }

        // Generate blockstate with rotations for all 6 directions
        generator.blockStateCollector.accept(createCuboidNSEWUDBlockState(block, modelId));

        // Register item model
        registerParentedItemModel(generator, block, modelId);
    }


    /**
     * Registers a cuboid block with random texture variants.
     */
    private static void registerCuboidNSEWUDBlockWithRandomTextures(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        List<BlockDefinition.RandomTextureVariant> randomTextures = definition.getRandomTextures();
        List<ModelVariant> modelVariants = new ArrayList<>();

        for (int i = 0; i < randomTextures.size(); i++) {
            BlockDefinition.RandomTextureVariant variant = randomTextures.get(i);
            List<String> textures = variant.getTextures();

            Identifier modelId;
            if (definition.hasCustomModel()) {
                // Reference pre-existing custom model
                modelId = createCustomModelId(block, "base_v" + (i + 1));
            } else if (hasCuboids(definition)) {
                // Generate model from cuboids array
                modelId = createCuboidModel(generator, block, definition, textures, i, "base_v" + (i + 1));
            } else {
                // Fallback: reference custom model
                modelId = createCustomModelId(block, "base_v" + (i + 1));
            }

            modelVariants.add(new ModelVariant(modelId, variant.getWeight()));
        }

        // Generate blockstate with weighted random variants
        generator.blockStateCollector.accept(createCuboidNSEWUDBlockStateWithRandomTextures(block, modelVariants));

        // Register item model
        registerParentedItemModel(generator, block, modelVariants.get(0).model);
    }

    /**
     * Registers a cuboid block with multiple states.
     */
    private static void registerCuboidNSEWUDBlockWithStates(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        List<BlockDefinition.StateVariant> states = definition.getStates();
        Map<String, List<ModelVariant>> stateModelMap = new HashMap<>();

        Identifier firstModel = null;

        for (BlockDefinition.StateVariant state : states) {
            String stateId = state.getStateID() != null ? state.getStateID() : "base";
            List<ModelVariant> modelVariants = new ArrayList<>();

            if (state.hasRandomTextures()) {
                // Handle state with random textures
                List<BlockDefinition.RandomTextureVariant> randomTextures = state.getRandomTextures();
                for (int i = 0; i < randomTextures.size(); i++) {
                    List<String> textures = randomTextures.get(i).getTextures();

                    Identifier modelId;
                    if (definition.hasCustomModel()) {
                        // Reference pre-existing custom model
                        modelId = createCustomModelId(block, stateId + "_v" + (i + 1));
                    } else if (hasCuboids(definition)) {
                        // Generate model from cuboids array
                        modelId = createCuboidModel(generator, block, definition, textures, i, stateId + "_v" + (i + 1));
                    } else {
                        // Fallback: reference custom model
                        modelId = createCustomModelId(block, stateId + "_v" + (i + 1));
                    }

                    modelVariants.add(new ModelVariant(modelId, randomTextures.get(i).getWeight()));
                    if (firstModel == null) firstModel = modelId;
                }
            } else {
                // Handle state with single texture set
                List<String> textures = state.getTextures() != null ? state.getTextures() : definition.getTextures();

                Identifier modelId;
                if (definition.hasCustomModel()) {
                    // Reference pre-existing custom model
                    modelId = createCustomModelId(block, stateId + "_v1");
                } else if (hasCuboids(definition)) {
                    // Generate model from cuboids array
                    modelId = createCuboidModel(generator, block, definition, textures, 0, stateId + "_v1");
                } else {
                    // Fallback: reference custom model
                    modelId = createCustomModelId(block, stateId + "_v1");
                }

                modelVariants.add(new ModelVariant(modelId, 1));
                if (firstModel == null) firstModel = modelId;
            }

            stateModelMap.put(stateId, modelVariants);
        }

        // Generate blockstate with states
        generator.blockStateCollector.accept(createCuboidNSEWUDBlockStateWithStates(block, definition, stateModelMap, states));

        // Register item model
        if (firstModel != null) {
            registerParentedItemModel(generator, block, firstModel);
        }
    }

    /**
     * Registers a cuboid block using pre-existing custom model.
     */
    private static void registerCustomModelCuboidNSEWUDBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        Identifier modelId = createCustomModelId(block, "base_v1");

        generator.blockStateCollector.accept(createCuboidNSEWUDBlockState(block, modelId));
        registerParentedItemModel(generator, block, modelId);
    }

    /**
     * Creates a blockstate for cuboid blocks with all 6 facing directions.
     * Uses a single model with X and Y rotations.
     */
    private static BlockStateSupplier createCuboidNSEWUDBlockState(Block block, Identifier modelId) {
        return new BlockStateSupplier() {
            @Override
            public Block getBlock() {
                return block;
            }

            @Override
            public JsonElement get() {
                JsonObject json = new JsonObject();
                JsonObject variants = new JsonObject();

                // Horizontal facings (Y rotation only)
                addVariant(variants, "facing=north", modelId, 0, 0);
                addVariant(variants, "facing=east", modelId, 0, 90);
                addVariant(variants, "facing=south", modelId, 0, 180);
                addVariant(variants, "facing=west", modelId, 0, 270);

                // Vertical facings (X rotation for pitch, Y for yaw)
                addVariant(variants, "facing=up", modelId, 270, 0);
                addVariant(variants, "facing=down", modelId, 90, 0);

                json.add("variants", variants);
                return json;
            }
        };
    }

    /**
     * Creates a blockstate for cuboid blocks with random textures.
     */
    private static BlockStateSupplier createCuboidNSEWUDBlockStateWithRandomTextures(Block block, List<ModelVariant> modelVariants) {
        return new BlockStateSupplier() {
            @Override
            public Block getBlock() {
                return block;
            }

            @Override
            public JsonElement get() {
                JsonObject json = new JsonObject();
                JsonObject variants = new JsonObject();

                // Horizontal facings with random textures
                addVariantWithWeights(variants, "facing=north", modelVariants, 0, 0);
                addVariantWithWeights(variants, "facing=east", modelVariants, 0, 90);
                addVariantWithWeights(variants, "facing=south", modelVariants, 0, 180);
                addVariantWithWeights(variants, "facing=west", modelVariants, 0, 270);

                // Vertical facings with random textures
                addVariantWithWeights(variants, "facing=up", modelVariants, 270, 0);
                addVariantWithWeights(variants, "facing=down", modelVariants, 90, 0);

                json.add("variants", variants);
                return json;
            }
        };
    }

    /**
     * Creates a blockstate for cuboid blocks with states.
     */
    private static BlockStateSupplier createCuboidNSEWUDBlockStateWithStates(Block block, BlockDefinition definition,
                                                                            Map<String, List<ModelVariant>> stateModelMap,
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
                    String stateId = state.getStateID() != null ? state.getStateID() : "base";
                    List<ModelVariant> modelVariants = stateModelMap.get(stateId);

                    if (modelVariants != null && !modelVariants.isEmpty()) {
                        // Horizontal facings
                        addVariantWithWeightsAndState(variants, "facing=north", modelVariants, 0, 0, stateId);
                        addVariantWithWeightsAndState(variants, "facing=east", modelVariants, 0, 90, stateId);
                        addVariantWithWeightsAndState(variants, "facing=south", modelVariants, 0, 180, stateId);
                        addVariantWithWeightsAndState(variants, "facing=west", modelVariants, 0, 270, stateId);

                        // Vertical facings
                        addVariantWithWeightsAndState(variants, "facing=up", modelVariants, 270, 0, stateId);
                        addVariantWithWeightsAndState(variants, "facing=down", modelVariants, 90, 0, stateId);
                    }
                }

                json.add("variants", variants);
                return json;
            }
        };
    }

    // Helper method to add variant with rotation
    private static void addVariant(JsonObject variants, String condition, Identifier model, int x, int y) {
        JsonObject variant = new JsonObject();
        variant.addProperty("model", model.toString());
        if (x != 0) variant.addProperty("x", x);
        if (y != 0) variant.addProperty("y", y);
        variants.add(condition, variant);
    }

    // Helper method to add variant with weighted models
    private static void addVariantWithWeights(JsonObject variants, String condition, List<ModelVariant> modelVariants, int x, int y) {
        if (modelVariants.size() == 1) {
            addVariant(variants, condition, modelVariants.get(0).model, x, y);
        } else {
            JsonArray variantArray = new JsonArray();
            for (ModelVariant mv : modelVariants) {
                JsonObject variant = new JsonObject();
                variant.addProperty("model", mv.model.toString());
                if (x != 0) variant.addProperty("x", x);
                if (y != 0) variant.addProperty("y", y);
                if (mv.weight != 1) variant.addProperty("weight", mv.weight);
                variantArray.add(variant);
            }
            variants.add(condition, variantArray);
        }
    }

    // Helper method to add variant with weights and state
    private static void addVariantWithWeightsAndState(JsonObject variants, String condition, List<ModelVariant> modelVariants, int x, int y, String stateId) {
        String fullCondition = "state=" + stateId + "," + condition;
        addVariantWithWeights(variants, fullCondition, modelVariants, x, y);
    }

    private static Identifier createCustomModelId(Block block, String variant) {
        String blockName = getBlockName(block);
        return WesterosBlocks.id("block/custom/" + blockName + "/" + variant);
    }

    private static Identifier createGeneratedModelId(Block block, String variant) {
        String blockName = getBlockName(block);
        return WesterosBlocks.id("block/" + blockName + "/" + variant);
    }

    private static boolean hasActualRandomTextures(BlockDefinition definition) {
        if (!definition.hasRandomTextures()) return false;
        for (BlockDefinition.RandomTextureVariant variant : definition.getRandomTextures()) {
            if (variant.getTextures() != null && !variant.getTextures().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the definition has cuboids array for model generation.
     */
    private static boolean hasCuboids(BlockDefinition definition) {
        return definition.getCuboids() != null && !definition.getCuboids().isEmpty();
    }

    /**
     * Helper class to hold a model with its weight.
     */
    private static class ModelVariant {
        final Identifier model;
        final int weight;

        ModelVariant(Identifier model, int weight) {
            this.model = model;
            this.weight = weight;
        }
    }
}
