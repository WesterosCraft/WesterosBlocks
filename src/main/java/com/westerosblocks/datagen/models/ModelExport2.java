package com.westerosblocks.datagen.models;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.ModBlock;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.Optional;

/**
 * Enhanced shared utility class for model export classes.
 * Provides common functionality for creating block states, models, and handling rotations.
 * This class is designed to be extended by specific block type exporters.
 */
public abstract class ModelExport2 {
    
    // Common constants
    public static final String GENERATED_PATH = "block/generated/";
    public static final String CUSTOM_PATH = "block/custom/";
    
    // Common texture keys
    public static final String TEXTURE_KEY = "texture";
    public static final String TEXTURE_KEY_1 = "1";
    public static final String PARTICLE_KEY = "particle";
    
    // Common rotation arrays for different directions
    public static final Direction[] CARDINAL_DIRECTIONS = {
        Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST
    };
    
    public static final int[] CARDINAL_ROTATIONS = {0, 90, 180, 270};
    
    public static final VariantSettings.Rotation[] CARDINAL_ROTATION_VARIANTS = {
        VariantSettings.Rotation.R0,
        VariantSettings.Rotation.R90,
        VariantSettings.Rotation.R180,
        VariantSettings.Rotation.R270
    };

    /**
     * Creates a model with the specified parent and texture overrides.
     * 
     * @param generator The BlockStateModelGenerator to register the model with
     * @param block The block this model is for
     * @param parentModelPath The parent model path to inherit from
     * @param texturePath The texture path to override
     * @param variant The variant suffix for the model ID
     * @param textureKey The texture key to use (e.g., "texture", "1")
     * @return The created model Identifier
     */
    public static Identifier createModel(
            BlockStateModelGenerator generator, 
            Block block, 
            String parentModelPath, 
            String texturePath, 
            String variant, 
            String textureKey) {
        
        return ModelExportUtils.createModel(generator, block, parentModelPath, texturePath, variant, textureKey);
    }

    /**
     * Creates a model with the default "texture" key.
     */
    public static Identifier createModel(
            BlockStateModelGenerator generator, 
            Block block, 
            String parentModelPath, 
            String texturePath, 
            String variant) {
        
        return ModelExportUtils.createModel(generator, block, parentModelPath, texturePath, variant);
    }

    /**
     * Creates a model with the "1" texture key (used by some block types).
     */
    public static Identifier createModelWithKey1(
            BlockStateModelGenerator generator, 
            Block block, 
            String parentModelPath, 
            String texturePath, 
            String variant) {
        
        return ModelExportUtils.createModelWithKey1(generator, block, parentModelPath, texturePath, variant);
    }

    /**
     * Creates a BlockStateVariant with optional rotation.
     * 
     * @param modelId The model identifier
     * @param yRotation The Y rotation in degrees (0, 90, 180, 270)
     * @param xRotation The X rotation in degrees (0, 90, 180, 270)
     * @return The created BlockStateVariant
     */
    public static BlockStateVariant createVariant(Identifier modelId, Integer yRotation, Integer xRotation) {
        BlockStateVariant variant = BlockStateVariant.create()
            .put(VariantSettings.MODEL, modelId);
        
        if (yRotation != null && yRotation != 0) {
            variant.put(VariantSettings.Y, getRotation(yRotation));
        }
        
        if (xRotation != null && xRotation != 0) {
            variant.put(VariantSettings.X, getRotation(xRotation));
        }
        
        return variant;
    }

    /**
     * Creates a BlockStateVariant with Y rotation only.
     */
    public static BlockStateVariant createVariant(Identifier modelId, int yRotation) {
        return createVariant(modelId, yRotation, null);
    }

    /**
     * Creates a BlockStateVariant with no rotation.
     */
    public static BlockStateVariant createVariant(Identifier modelId) {
        return createVariant(modelId, null, null);
    }

    /**
     * Converts degrees to VariantSettings.Rotation.
     */
    public static VariantSettings.Rotation getRotation(int degrees) {
        return switch (degrees) {
            case 90 -> VariantSettings.Rotation.R90;
            case 180 -> VariantSettings.Rotation.R180;
            case 270 -> VariantSettings.Rotation.R270;
            default -> VariantSettings.Rotation.R0;
        };
    }

    /**
     * Creates a BlockStateVariantMap for cardinal directions with a single model.
     * 
     * @param modelId The model identifier to use for all directions
     * @return The created BlockStateVariantMap
     */
    public static BlockStateVariantMap createCardinalVariants(Identifier modelId) {
        return BlockStateVariantMap.create(Properties.HORIZONTAL_FACING)
            .register(Direction.NORTH, createVariant(modelId))
            .register(Direction.EAST, createVariant(modelId, 90))
            .register(Direction.SOUTH, createVariant(modelId, 180))
            .register(Direction.WEST, createVariant(modelId, 270));
    }

    /**
     * Creates a BlockStateVariantMap for cardinal directions with different models.
     * 
     * @param northModel The model for north direction
     * @param eastModel The model for east direction
     * @param southModel The model for south direction
     * @param westModel The model for west direction
     * @return The created BlockStateVariantMap
     */
    public static BlockStateVariantMap createCardinalVariants(
            Identifier northModel, 
            Identifier eastModel, 
            Identifier southModel, 
            Identifier westModel) {
        
        return BlockStateVariantMap.create(Properties.HORIZONTAL_FACING)
            .register(Direction.NORTH, createVariant(northModel))
            .register(Direction.EAST, createVariant(eastModel))
            .register(Direction.SOUTH, createVariant(southModel))
            .register(Direction.WEST, createVariant(westModel));
    }

    /**
     * Registers a block state with the generator using the provided variants.
     * 
     * @param generator The BlockStateModelGenerator to register with
     * @param block The block to create a state for
     * @param variants The BlockStateVariantMap containing all variants
     */
    public static void registerBlockState(BlockStateModelGenerator generator, Block block, BlockStateVariantMap variants) {
        generator.blockStateCollector.accept(
            VariantsBlockStateSupplier.create(block)
                .coordinate(variants)
        );
    }

    /**
     * Registers a multipart block state with the generator.
     * 
     * @param generator The BlockStateModelGenerator to register with
     * @param stateSupplier The MultipartBlockStateSupplier to register
     */
    public static void registerMultipartBlockState(BlockStateModelGenerator generator, MultipartBlockStateSupplier stateSupplier) {
        generator.blockStateCollector.accept(stateSupplier);
    }

    /**
     * Generates a simple item model that inherits from a block model.
     * 
     * @param generator The ItemModelGenerator to register the model with
     * @param block The block to create an item model for
     * @param variant The variant suffix for the block model to inherit from
     */
    public static void generateItemModel(ItemModelGenerator generator, Block block, String variant) {
        ModelExportUtils.generateItemModel(generator, block, variant);
    }

    /**
     * Generates a simple item model that inherits from a block model without variant suffix.
     * 
     * @param generator The ItemModelGenerator to register the model with
     * @param block The block to create an item model for
     */
    public static void generateItemModel(ItemModelGenerator generator, Block block) {
        ModelExportUtils.generateItemModel(generator, block);
    }

    /**
     * Creates a block identifier from a texture path.
     * 
     * @param texturePath The texture path
     * @return The block identifier
     */
    public static Identifier createBlockIdentifier(String texturePath) {
        // If the texture path includes a namespace
        if (texturePath != null && texturePath.contains(":")) {
            String namespace = texturePath.substring(0, texturePath.indexOf(':'));
            String path = texturePath.substring(texturePath.indexOf(':') + 1);
            // If it's not a minecraft texture, prepend block/
            if (!namespace.equals("minecraft")) {
                path = "block/" + path;
            }
            return Identifier.of(namespace, path);
        }
        // No namespace, use mod ID and prepend block/
        return WesterosBlocks.id("block/" + texturePath);
    }

    /**
     * Creates a model identifier for a block with a specific variant.
     * 
     * @param block The block
     * @param variant The variant suffix
     * @return The model identifier
     */
    public static Identifier createModelId(Block block, String variant) {
        String modelPath = "block/generated/" + block.getTranslationKey().replace("block.westerosblocks.", "") + "_" + variant;
        return WesterosBlocks.id(modelPath);
    }

    /**
     * Creates a model identifier for a block without variant suffix.
     * 
     * @param block The block
     * @return The model identifier
     */
    public static Identifier createModelId(Block block) {
        String modelPath = "block/generated/" + block.getTranslationKey().replace("block.westerosblocks.", "");
        return WesterosBlocks.id(modelPath);
    }

    /**
     * Abstract method that subclasses must implement to generate block state models.
     * 
     * @param generator The BlockStateModelGenerator to use
     * @param block The block to generate models for
     * @param texturePath The texture path to use
     */
    public abstract void generateBlockStateModels(BlockStateModelGenerator generator, Block block, String texturePath);

    /**
     * Abstract method that subclasses must implement to generate item models.
     * 
     * @param generator The ItemModelGenerator to use
     * @param block The block to generate item models for
     */
    public abstract void generateItemModels(ItemModelGenerator generator, Block block);

    /**
     * Overloaded method for generating block state models with additional parameters.
     * Default implementation calls the basic version.
     * 
     * @param generator The BlockStateModelGenerator to use
     * @param block The block to generate models for
     * @param texturePath The texture path to use
     * @param additionalParams Additional parameters (implementation specific)
     */
    public void generateBlockStateModels(BlockStateModelGenerator generator, Block block, String texturePath, Object... additionalParams) {
        generateBlockStateModels(generator, block, texturePath);
    }
} 