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
import java.util.List;

/**
 * Exporter for NSEW cuboid blocks with directional facing support.
 * Generates blockstate files with facing variants and appropriate model rotations.
 */
public class CuboidNSEWBlockExporter extends BaseBlockExporter {

    private static final String[] FACING_DIRECTIONS = {"north", "east", "south", "west"};
    private static final int[] ROTATIONS = {0, 90, 180, 270};

    /**
     * Registers an NSEW cuboid block from a BlockDefinition.
     * Handles facing directions with proper model rotations.
     */
    public static void registerCustomCuboidNSEWBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        if (!(block instanceof WCCuboidNSEWBlock cuboidBlock)) {
            throw new IllegalArgumentException("Block must be a WCCuboidNSEWBlock instance");
        }

        // Use centralized priority logic from BlockDefinition
        BlockDefinition.TextureSource source = definition.getPrimaryTextureSource();

        switch (source) {
            case STATES -> registerCuboidNSEWBlockWithStates(generator, block, definition, cuboidBlock);
            case RANDOM_TEXTURES -> registerCuboidNSEWBlockWithRandomTextures(generator, block, definition, cuboidBlock);
            case TEXTURES -> registerSimpleCuboidNSEWBlock(generator, block, definition, cuboidBlock);
            case CUSTOM_MODEL -> registerCustomModelCuboidNSEWBlock(generator, block, definition, cuboidBlock);
            case NONE -> registerFallbackCuboidNSEWBlock(generator, block, definition, cuboidBlock);
        }
    }

    /**
     * Registers a simple NSEW cuboid block with basic textures.
     */
    private static void registerSimpleCuboidNSEWBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition, WCCuboidNSEWBlock cuboidBlock) {
        List<String> textures = definition.getTextures();

        Identifier modelId;
        if (definition.hasCustomModel()) {
            // For custom models, just reference the existing model file
            modelId = createCustomModelId(block, "base_v1");
        } else if (hasCuboids(definition)) {
            modelId = createCuboidModel(generator, block, definition, textures, 0, "base_v1");
        } else {
            // Use standard cube model
            TextureMap textureMap = createCuboidTextureMap(textures);
            if (textures != null && textures.size() == 1) {
                modelId = Models.CUBE_ALL.upload(createGeneratedModelId(block, "base_v1"), textureMap, generator.modelCollector);
            } else {
                modelId = Models.CUBE.upload(createGeneratedModelId(block, "base_v1"), textureMap, generator.modelCollector);
            }
        }

        // Generate blockstate with facing variants
        BlockStateVariantMap variants = BlockStateVariantMap.create(WCCuboidNSEWBlock.FACING)
            .register(Direction.NORTH, createVariant(modelId, 0))
            .register(Direction.EAST, createVariant(modelId, 90))
            .register(Direction.SOUTH, createVariant(modelId, 180))
            .register(Direction.WEST, createVariant(modelId, 270));

        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block).coordinate(variants));
        registerParentedItemModel(generator, block, modelId);
    }

    /**
     * Registers an NSEW cuboid block with random texture variants.
     */
    private static void registerCuboidNSEWBlockWithRandomTextures(BlockStateModelGenerator generator, Block block, BlockDefinition definition, WCCuboidNSEWBlock cuboidBlock) {
        List<BlockDefinition.RandomTextureVariant> randomTextures = definition.getRandomTextures();
        List<Identifier> modelIds = new ArrayList<>();

        for (int i = 0; i < randomTextures.size(); i++) {
            BlockDefinition.RandomTextureVariant variant = randomTextures.get(i);
            List<String> textures = variant.getTextures();

            Identifier modelId;
            if (definition.hasCustomModel()) {
                // For custom models, just reference the existing model files
                modelId = createCustomModelId(block, "base_v" + (i + 1));
            } else if (hasCuboids(definition)) {
                // Generate custom cuboid models with geometry
                modelId = createCuboidModel(generator, block, definition, textures, i, "base_v" + (i + 1));
            } else {
                // Generate standard cube models
                TextureMap textureMap = createCuboidTextureMap(textures);
                modelId = Models.CUBE.upload(createGeneratedModelId(block, "base_v" + (i + 1)), textureMap, generator.modelCollector);
            }
            modelIds.add(modelId);
        }

        // Create facing variants with random model selection
        List<BlockStateVariant> northVariants = modelIds.stream().map(id -> createVariant(id, 0)).toList();
        List<BlockStateVariant> eastVariants = modelIds.stream().map(id -> createVariant(id, 90)).toList();
        List<BlockStateVariant> southVariants = modelIds.stream().map(id -> createVariant(id, 180)).toList();
        List<BlockStateVariant> westVariants = modelIds.stream().map(id -> createVariant(id, 270)).toList();

        BlockStateVariantMap variants = BlockStateVariantMap.create(WCCuboidNSEWBlock.FACING)
            .register(Direction.NORTH, northVariants)
            .register(Direction.EAST, eastVariants)
            .register(Direction.SOUTH, southVariants)
            .register(Direction.WEST, westVariants);

        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block).coordinate(variants));

        if (!modelIds.isEmpty()) {
            registerParentedItemModel(generator, block, modelIds.get(0));
        }
    }

    /**
     * Registers an NSEW cuboid block with multiple states.
     */
    private static void registerCuboidNSEWBlockWithStates(BlockStateModelGenerator generator, Block block, BlockDefinition definition, WCCuboidNSEWBlock cuboidBlock) {
        List<BlockDefinition.StateVariant> states = definition.getStates();
        List<Identifier> allModelIds = new ArrayList<>();

        // Generate models for each state
        for (int i = 0; i < states.size(); i++) {
            BlockDefinition.StateVariant state = states.get(i);
            String stateId = state.getStateID() != null ? state.getStateID() : "state" + i;

            if (state.getRandomTextures() != null && !state.getRandomTextures().isEmpty()) {
                // Handle state with multiple random texture variants
                List<BlockDefinition.RandomTextureVariant> randomTextures = state.getRandomTextures();

                for (int j = 0; j < randomTextures.size(); j++) {
                    BlockDefinition.RandomTextureVariant variant = randomTextures.get(j);
                    List<String> textures = variant.getTextures();
                    String variantName = stateId + "_v" + (j + 1);

                    Identifier modelId;
                    if (definition.hasCustomModel()) {
                        modelId = createCustomModelId(block, variantName);
                    } else if (hasCuboids(definition)) {
                        if (textures != null && !textures.isEmpty()) {
                            modelId = createCuboidModel(generator, block, definition, textures, j, variantName);
                        } else {
                            modelId = createGeneratedModelId(block, variantName);
                        }
                    } else {
                        TextureMap textureMap = createCuboidTextureMap(textures);
                        modelId = Models.CUBE.upload(createGeneratedModelId(block, variantName), textureMap, generator.modelCollector);
                    }
                    allModelIds.add(modelId);
                }
            } else {
                // Handle state with single texture set
                List<String> textures = state.getTextures();
                String variantName = stateId + "_v1";

                Identifier modelId;
                if (definition.hasCustomModel()) {
                    modelId = createCustomModelId(block, variantName);
                } else if (hasCuboids(definition)) {
                    if (textures != null && !textures.isEmpty()) {
                        modelId = createCuboidModel(generator, block, definition, textures, i, variantName);
                    } else {
                        modelId = createGeneratedModelId(block, variantName);
                    }
                } else {
                    TextureMap textureMap = createCuboidTextureMap(textures);
                    modelId = Models.CUBE.upload(createGeneratedModelId(block, variantName), textureMap, generator.modelCollector);
                }
                allModelIds.add(modelId);
            }
        }

        // Create complex blockstate with both state and facing properties
        // This would require a custom blockstate supplier for state + facing combinations
        generator.blockStateCollector.accept(createAdvancedNSEWStatesBlockState(block, definition, allModelIds));

        // Use the first model for item model
        if (!allModelIds.isEmpty()) {
            registerParentedItemModel(generator, block, allModelIds.get(0));
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
                    .register(Direction.NORTH, allVariants)
                    .register(Direction.EAST, allVariants)
                    .register(Direction.SOUTH, allVariants)
                    .register(Direction.WEST, allVariants);

                generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block).coordinate(variants));
            } else {
                // Normal random texture variants with facing rotations
                List<BlockStateVariant> northVariants = modelIds.stream().map(id -> createVariant(id, 0)).toList();
                List<BlockStateVariant> eastVariants = modelIds.stream().map(id -> createVariant(id, 90)).toList();
                List<BlockStateVariant> southVariants = modelIds.stream().map(id -> createVariant(id, 180)).toList();
                List<BlockStateVariant> westVariants = modelIds.stream().map(id -> createVariant(id, 270)).toList();

                BlockStateVariantMap variants = BlockStateVariantMap.create(WCCuboidNSEWBlock.FACING)
                    .register(Direction.NORTH, northVariants)
                    .register(Direction.EAST, eastVariants)
                    .register(Direction.SOUTH, southVariants)
                    .register(Direction.WEST, westVariants);

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
                .register(Direction.NORTH, createVariant(modelId, 0))
                .register(Direction.EAST, createVariant(modelId, 90))
                .register(Direction.SOUTH, createVariant(modelId, 180))
                .register(Direction.WEST, createVariant(modelId, 270));

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
            .register(Direction.NORTH, createVariant(modelId, 0))
            .register(Direction.EAST, createVariant(modelId, 90))
            .register(Direction.SOUTH, createVariant(modelId, 180))
            .register(Direction.WEST, createVariant(modelId, 270));

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

    private static BlockStateSupplier createAdvancedNSEWStatesBlockState(Block block, BlockDefinition definition, List<Identifier> modelIds) {
        if (modelIds.isEmpty()) {
            return createSimpleBlockState(block, WesterosBlocks.id("block/missing"));
        }

        // Check if the block has states defined in the definition
        if (!definition.hasStates()) {
            // Fallback to simple facing-only variants if no STATE property
            Identifier modelId = modelIds.get(0);
            BlockStateVariantMap variants = BlockStateVariantMap.create(WCCuboidNSEWBlock.FACING)
                .register(Direction.NORTH, createVariant(modelId, 0))
                .register(Direction.EAST, createVariant(modelId, 90))
                .register(Direction.SOUTH, createVariant(modelId, 180))
                .register(Direction.WEST, createVariant(modelId, 270));

            return VariantsBlockStateSupplier.create(block).coordinate(variants);
        }

        // Create complex blockstate with both STATE and FACING properties
        List<BlockDefinition.StateVariant> states = definition.getStates();

        // Create a custom blockstate supplier that handles both STATE and FACING properties
        return createAdvancedNSEWStatesBlockStateCustom(block, definition, modelIds);
    }

    /**
     * Creates a custom blockstate supplier for NSEW blocks with states.
     * Generates JSON with both state and facing properties.
     */
    private static BlockStateSupplier createAdvancedNSEWStatesBlockStateCustom(Block block, BlockDefinition definition, List<Identifier> modelIds) {
        return new BlockStateSupplier() {
            @Override
            public Block getBlock() {
                return block;
            }

            @Override
            public JsonElement get() {
                JsonObject json = new JsonObject();
                JsonObject variants = new JsonObject();

                List<BlockDefinition.StateVariant> states = definition.getStates();

                for (int i = 0; i < states.size() && i < modelIds.size(); i++) {
                    BlockDefinition.StateVariant state = states.get(i);
                    String stateValue = state.getStateID() != null ? state.getStateID() : "state" + i;
                    Identifier modelId = modelIds.get(i);

                    // Add variants for each facing direction for this state
                    JsonObject northVariant = new JsonObject();
                    northVariant.addProperty("model", modelId.toString());
                    variants.add("facing=north,state=" + stateValue, northVariant);

                    JsonObject eastVariant = new JsonObject();
                    eastVariant.addProperty("model", modelId.toString());
                    eastVariant.addProperty("y", 90);
                    variants.add("facing=east,state=" + stateValue, eastVariant);

                    JsonObject southVariant = new JsonObject();
                    southVariant.addProperty("model", modelId.toString());
                    southVariant.addProperty("y", 180);
                    variants.add("facing=south,state=" + stateValue, southVariant);

                    JsonObject westVariant = new JsonObject();
                    westVariant.addProperty("model", modelId.toString());
                    westVariant.addProperty("y", 270);
                    variants.add("facing=west,state=" + stateValue, westVariant);
                }

                json.add("variants", variants);
                return json;
            }
        };
    }
}