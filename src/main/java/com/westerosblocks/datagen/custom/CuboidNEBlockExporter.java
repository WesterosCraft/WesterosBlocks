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

/**
 * Exporter for NE (North-East) cuboid blocks with two-directional facing support.
 * Generates blockstate files with facing=east and facing=north variants.
 */
public class CuboidNEBlockExporter extends BaseBlockExporter {

    /**
     * Registers a NE cuboid block from a BlockDefinition.
     * Handles EAST and NORTH facing directions with proper model rotations.
     */
    public static void registerCustomCuboidNEBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        if (!(block instanceof WCCuboidNEBlock cuboidBlock)) {
            throw new IllegalArgumentException("Block must be a WCCuboidNEBlock instance");
        }

        // Check for states first (highest priority)
        if (definition.hasStates()) {
            registerCuboidNEBlockWithStates(generator, block, definition, cuboidBlock);
        } else if (definition.hasRandomTextures() && hasActualRandomTextures(definition)) {
            // Only use random texture path if there are actual textures defined
            registerCuboidNEBlockWithRandomTextures(generator, block, definition, cuboidBlock);
        } else if (definition.getTextures() != null && !definition.getTextures().isEmpty()) {
            registerSimpleCuboidNEBlock(generator, block, definition, cuboidBlock);
        } else if (definition.hasCustomModel() || definition.hasRandomTextures()) {
            // Handle blocks with custom models or empty random textures
            registerCustomModelCuboidNEBlock(generator, block, definition, cuboidBlock);
        } else {
            // Fallback: create a simple model with default texture
            registerFallbackCuboidNEBlock(generator, block, definition, cuboidBlock);
        }
    }

    /**
     * Registers a simple NE cuboid block with basic textures.
     */
    private static void registerSimpleCuboidNEBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition, WCCuboidNEBlock cuboidBlock) {
        List<String> textures = definition.getTextures();

        Identifier modelId;
        if (definition.hasCustomModel()) {
            // For custom models, just reference the existing model file
            modelId = createCustomModelId(block, "base_v1");
        } else if (hasCuboids(definition) && textures != null && !textures.isEmpty()) {
            modelId = createCuboidModel(generator, block, definition, textures, 0, "base_v1");
        } else if (textures != null && !textures.isEmpty()) {
            // Use standard cube model
            TextureMap textureMap = createCuboidTextureMap(textures);
            if (textures.size() == 1) {
                modelId = Models.CUBE_ALL.upload(createGeneratedModelId(block, "base_v1"), textureMap, generator.modelCollector);
            } else {
                modelId = Models.CUBE.upload(createGeneratedModelId(block, "base_v1"), textureMap, generator.modelCollector);
            }
        } else {
            // Fallback: no textures defined, use missing texture
            TextureMap textureMap = TextureMap.all(createBlockIdentifier("missing"));
            modelId = Models.CUBE_ALL.upload(createGeneratedModelId(block, "base_v1"), textureMap, generator.modelCollector);
        }

        // Generate blockstate with facing=east and facing=north variants
        BlockStateVariantMap variants = BlockStateVariantMap.create(WCCuboidNEBlock.FACING)
            .register(Direction.EAST, createVariant(modelId, 0))      // East: no rotation
            .register(Direction.NORTH, createVariant(modelId, 90));   // North: 90° rotation

        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block).coordinate(variants));
        registerParentedItemModel(generator, block, modelId);
    }

    /**
     * Registers a NE cuboid block with random texture variants.
     */
    private static void registerCuboidNEBlockWithRandomTextures(BlockStateModelGenerator generator, Block block, BlockDefinition definition, WCCuboidNEBlock cuboidBlock) {
        List<BlockDefinition.RandomTextureVariant> randomTextures = definition.getRandomTextures();
        List<Identifier> modelIds = new ArrayList<>();

        for (int i = 0; i < randomTextures.size(); i++) {
            BlockDefinition.RandomTextureVariant variant = randomTextures.get(i);
            List<String> textures = variant.getTextures();

            Identifier modelId;
            if (definition.hasCustomModel()) {
                modelId = createCustomModelId(block, "base_v" + (i + 1));
            } else if (hasCuboids(definition)) {
                modelId = createCuboidModel(generator, block, definition, textures, i, "base_v" + (i + 1));
            } else {
                TextureMap textureMap = createCuboidTextureMap(textures);
                if (textures.size() == 1) {
                    modelId = Models.CUBE_ALL.upload(createGeneratedModelId(block, "base_v" + (i + 1)), textureMap, generator.modelCollector);
                } else {
                    modelId = Models.CUBE.upload(createGeneratedModelId(block, "base_v" + (i + 1)), textureMap, generator.modelCollector);
                }
            }
            modelIds.add(modelId);
        }

        // Create weighted random variants for each facing
        List<BlockStateVariant> eastVariants = new ArrayList<>();
        for (int i = 0; i < modelIds.size(); i++) {
            BlockDefinition.RandomTextureVariant rtv = randomTextures.get(i);
            int weight = rtv.getWeight();
            for (int w = 0; w < weight; w++) {
                eastVariants.add(createVariant(modelIds.get(i), 0));
            }
        }

        List<BlockStateVariant> northVariants = new ArrayList<>();
        for (int i = 0; i < modelIds.size(); i++) {
            BlockDefinition.RandomTextureVariant rtv = randomTextures.get(i);
            int weight = rtv.getWeight();
            for (int w = 0; w < weight; w++) {
                northVariants.add(createVariant(modelIds.get(i), 90));
            }
        }

        BlockStateVariantMap variants = BlockStateVariantMap.create(WCCuboidNEBlock.FACING)
            .register(Direction.EAST, eastVariants)
            .register(Direction.NORTH, northVariants);

        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block).coordinate(variants));
        registerParentedItemModel(generator, block, modelIds.get(0));
    }

    /**
     * Registers a NE cuboid block with multiple states.
     */
    private static void registerCuboidNEBlockWithStates(BlockStateModelGenerator generator, Block block, BlockDefinition definition, WCCuboidNEBlock cuboidBlock) {
        List<BlockDefinition.StateVariant> states = definition.getStates();
        List<Identifier> allModelIds = new ArrayList<>();
        Map<String, List<Identifier>> stateModelMap = new HashMap<>();

        Identifier firstModelId = null;

        for (int stateIdx = 0; stateIdx < states.size(); stateIdx++) {
            BlockDefinition.StateVariant state = states.get(stateIdx);
            String stateId = state.getStateID() != null ? state.getStateID() : "base";

            if (state.hasRandomTextures()) {
                // Handle state with random textures
                List<BlockDefinition.RandomTextureVariant> randomTextures = state.getRandomTextures();
                List<Identifier> modelIds = new ArrayList<>();

                for (int i = 0; i < randomTextures.size(); i++) {
                    List<String> textures = randomTextures.get(i).getTextures();
                    Identifier modelId = createCuboidModel(generator, block, definition, textures, i, stateId + "_v" + (i + 1));
                    modelIds.add(modelId);
                    if (firstModelId == null) firstModelId = modelId;
                }

                stateModelMap.put(stateId, modelIds);
                allModelIds.addAll(modelIds);
            } else {
                // Handle state with single texture set or custom model
                Identifier modelId;

                if (state.isCustomModel() || definition.hasCustomModel()) {
                    // Use custom model path
                    modelId = createCustomModelId(block, stateId + "_v1");
                } else {
                    // Generate model from textures
                    List<String> textures = state.getTextures() != null ? state.getTextures() : definition.getTextures();
                    if (textures != null && !textures.isEmpty()) {
                        modelId = createCuboidModel(generator, block, definition, textures, 0, stateId + "_v1");
                    } else {
                        // Fallback to custom model path if no textures
                        modelId = createCustomModelId(block, stateId + "_v1");
                    }
                }

                if (firstModelId == null) firstModelId = modelId;

                stateModelMap.put(stateId, List.of(modelId));
                allModelIds.add(modelId);
            }
        }

        // Create custom blockstate supplier with both STATE and FACING properties
        generator.blockStateCollector.accept(createAdvancedNEStatesBlockState(block, definition, stateModelMap, states));
        registerParentedItemModel(generator, block, firstModelId);
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
    private static boolean hasActualRandomTextures(BlockDefinition definition) {
        return CuboidBlockExporter.hasActualRandomTextures(definition);
    }

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
        return WesterosBlocks.id("block/generated/" + blockName + "/" + variant);
    }

    /**
     * Creates a custom blockstate supplier for NE blocks with states.
     * Generates JSON with both state and facing properties.
     */
    private static BlockStateSupplier createAdvancedNEStatesBlockState(Block block, BlockDefinition definition,
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
}