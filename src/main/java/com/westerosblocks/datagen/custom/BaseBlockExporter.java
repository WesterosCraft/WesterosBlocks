package com.westerosblocks.datagen.custom;

import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import net.minecraft.data.client.VariantSettings.Rotation;
import com.westerosblocks.WesterosBlocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import com.google.gson.JsonElement;

/**
 * Base class providing shared utilities for all block exporters.
 * Centralizes common functionality to eliminate code duplication and ensure consistency.
 *
 * @see <a href="block-models.md">block-models.md sections 5.2-5.6</a>
 */
public abstract class BaseBlockExporter {

    /**
     * Extracts the block name from the block's registry key.
     * Handles both formatted registry keys and plain block strings.
     *
     * @param block The block to extract the name from
     * @return The block name without namespace or Block{} wrapper
     */
    protected static String getBlockName(Block block) {
        String blockString = block.toString();
        if (blockString.contains(":")) {
            return blockString.split(":")[1].replace("}", "");
        }
        return blockString.toLowerCase().replace("block{", "").replace("}", "");
    }

    /**
     * Creates a nested model identifier following the pattern: {@code block/blockName/variant}.
     * This is the standard pattern for organizing block models by block type.
     *
     * @param block The block to create the identifier for
     * @param variant The model variant (e.g., "bottom", "top", "open", "horizontal")
     * @return The nested model identifier in format {@code westerosblocks:block/blockName/variant}
     */
    protected static Identifier createNestedModelId(Block block, String variant) {
        String blockName = getBlockName(block);
        String modelPath = "block/" + blockName + "/" + variant;
        return WesterosBlocks.id(modelPath);
    }

    /**
     * Creates a default nested model identifier using the block name as variant.
     * 
     * @param block The block
     * @return The nested model identifier
     */
    protected static Identifier createNestedModelId(Block block) {
        return createNestedModelId(block, getBlockName(block));
    }

    /**
     * Creates an identifier for block textures, handling namespaces properly.
     * Automatically prepends {@code "block/"} if no namespace is provided.
     *
     * @param texturePath The texture path (can include namespace like "westerosblocks:block/white_door")
     * @return The identifier for the block texture
     * @throws IllegalArgumentException if texturePath is null or empty
     */
    protected static Identifier createBlockIdentifier(String texturePath) {
        if (texturePath == null || texturePath.trim().isEmpty()) {
            throw new IllegalArgumentException("Texture path cannot be null or empty");
        }
        
        // If the texture path includes a namespace
        if (texturePath.contains(":")) {
            String namespace = texturePath.substring(0, texturePath.indexOf(':'));
            String path = texturePath.substring(texturePath.indexOf(':') + 1);
            return Identifier.of(namespace, path);
        }
        // No namespace, use mod ID and prepend "block/"
        return WesterosBlocks.id("block/" + texturePath);
    }

    /**
     * Creates a model identifier for a block with optional variant.
     * 
     * @param block The block
     * @param variant The model variant (e.g., "bottom", "top", "open")
     * @return The model identifier
     */
    protected static Identifier createModelId(Block block, String variant) {
        String blockName = getBlockName(block);
        String modelPath = variant != null && !variant.isEmpty() 
            ? "block/" + blockName + "/" + variant
            : "block/" + blockName + "/" + blockName;
        return WesterosBlocks.id(modelPath);
    }

    /**
     * Creates a model identifier for a block without variant.
     * 
     * @param block The block
     * @return The model identifier
     */
    protected static Identifier createModelId(Block block) {
        return createModelId(block, null);
    }

    /**
     * Validates that texture paths array meets minimum requirements.
     * Ensures data integrity before texture processing.
     *
     * @param texturePaths The texture paths to validate
     * @param minRequired The minimum number of textures required
     * @throws IllegalArgumentException if validation fails with descriptive error message
     */
    protected static void validateTexturePaths(String[] texturePaths, int minRequired) {
        if (texturePaths == null || texturePaths.length < minRequired) {
            throw new IllegalArgumentException(
                "At least " + minRequired + " texture path(s) required, got " + 
                (texturePaths == null ? 0 : texturePaths.length));
        }
    }

    /**
     * Fills texture array to ensure exactly 6 textures for cube models.
     * Missing slots are filled with the last provided texture (smart fill algorithm).
     *
     * @param texturePaths The input texture paths (1-6 textures)
     * @return Array of exactly 6 texture paths
     * @throws IllegalArgumentException if texturePaths is null or empty
     */
    protected static String[] fillTextureArray(String[] texturePaths) {
        validateTexturePaths(texturePaths, 1);
        
        String[] filledTextures = new String[6];
        for (int i = 0; i < 6; i++) {
            if (i < texturePaths.length) {
                filledTextures[i] = texturePaths[i];
            } else {
                filledTextures[i] = texturePaths[texturePaths.length - 1];
            }
        }
        return filledTextures;
    }

    /**
     * Creates a block state variant without rotation (0° rotation).
     * Follows block-models.md section 5.4: Custom BlockStateSupplier Method.
     *
     * @param modelId The model identifier to use for this variant
     * @return The block state variant with MODEL setting
     */
    protected static BlockStateVariant createVariant(Identifier modelId) {
        return BlockStateVariant.create().put(VariantSettings.MODEL, modelId);
    }

    /**
     * Creates a block state variant with Y-axis rotation.
     * Follows block-models.md section 5.4: Custom BlockStateSupplier Method.
     *
     * @param modelId The model identifier to use for this variant
     * @param rotation The Y-axis rotation in degrees (must be 0, 90, 180, or 270)
     * @return The block state variant with MODEL and Y rotation settings
     * @throws IllegalArgumentException if rotation is not 0, 90, 180, or 270
     */
    protected static BlockStateVariant createVariant(Identifier modelId, int rotation) {
        Rotation rotationEnum = switch (rotation) {
            case 0 -> Rotation.R0;
            case 90 -> Rotation.R90;
            case 180 -> Rotation.R180;
            case 270 -> Rotation.R270;
            default -> throw new IllegalArgumentException("Invalid rotation: " + rotation + ". Must be 0, 90, 180, or 270.");
        };
        return BlockStateVariant.create().put(VariantSettings.MODEL, modelId).put(VariantSettings.Y, rotationEnum);
    }

    /**
     * Creates a simple blockstate supplier with a single model variant.
     * Follows block-models.md section 3.1: Simple Cube All pattern.

     * @param block The block to create the blockstate for
     * @param modelId The model identifier to use
     * @return The blockstate supplier with a single variant
     */
    protected static VariantsBlockStateSupplier createSimpleBlockState(Block block, Identifier modelId) {
        return VariantsBlockStateSupplier.create(block, createVariant(modelId));
    }

    /**
     * Creates a Model with automatic tinted/untinted path resolution.
     * Centralizes the common pattern of path selection based on tinting.
     *
     * @param tinted Whether the model should use tinted textures
     * @param modelPath The model path (without tinted/untinted prefix)
     * @param textureKeys The texture keys this model requires
     * @return The configured Model instance
     */
    protected static Model createTintedModel(boolean tinted, String modelPath, TextureKey... textureKeys) {
        String tintPath = tinted ? "block/tinted/" : "block/untinted/";
        String fullPath = tintPath + modelPath;
        return new Model(Optional.of(WesterosBlocks.id(fullPath)), Optional.empty(), textureKeys);
    }

    /**
     * Uploads a model with automatic nested model ID creation.
     * Combines model.upload() with createNestedModelId() in one call.
     *
     * @param model The model to upload
     * @param block The block this model belongs to
     * @param variant The model variant name
     * @param textureMap The texture map for this model
     * @param modelCollector The model collector to upload to
     * @return The identifier of the uploaded model
     */
    protected static Identifier uploadModel(Model model, Block block, String variant,
                                           TextureMap textureMap, BiConsumer<Identifier, Supplier<JsonElement>> modelCollector) {
        return model.upload(createNestedModelId(block, variant), textureMap, modelCollector);
    }


    /**
     * Creates a BlockStateVariant with optional weight.
     * Only adds weight setting if weight > 1.
     *
     * @param modelId The model identifier
     * @param rotation Y-axis rotation (0, 90, 180, 270)
     * @param weight The variant weight (1 = normal, >1 = higher probability)
     * @return BlockStateVariant with model, rotation, and optional weight
     */
    protected static BlockStateVariant createWeightedVariant(Identifier modelId, int rotation, int weight) {
        BlockStateVariant variant = createVariant(modelId, rotation);
        if (weight > 1) {
            variant = variant.put(VariantSettings.WEIGHT, weight);
        }
        return variant;
    }

    /**
     * Creates a list of weighted variants from model IDs and weights.
     * Applies the same rotation to all variants.
     *
     * @param modelIds List of model identifiers
     * @param weights List of weights (must match modelIds size)
     * @param rotation Y-axis rotation to apply to all variants
     * @return List of weighted BlockStateVariants
     */
    protected static List<BlockStateVariant> createWeightedVariants(List<Identifier> modelIds,
                                                                     List<Integer> weights, int rotation) {
        List<BlockStateVariant> variants = new ArrayList<>();
        for (int i = 0; i < modelIds.size(); i++) {
            variants.add(createWeightedVariant(modelIds.get(i), rotation, weights.get(i)));
        }
        return variants;
    }

    /**
     * Converts a Direction enum to rotation degrees for Y-axis rotation.
     * Standard mapping for horizontal facings.
     *
     * @param direction The direction
     * @return Rotation in degrees (0, 90, 180, or 270)
     * @throws IllegalArgumentException if direction is not horizontal
     */
    protected static int getRotationForDirection(Direction direction) {
        return switch (direction) {
            case NORTH -> 0;
            case EAST -> 90;
            case SOUTH -> 180;
            case WEST -> 270;
            default -> throw new IllegalArgumentException("Direction must be horizontal: " + direction);
        };
    }

    protected static class ModelRegistry {
        private final List<Identifier> modelIds = new ArrayList<>();
        private final List<Integer> weights = new ArrayList<>();

        public void add(Identifier modelId, int weight) {
            modelIds.add(modelId);
            weights.add(weight);
        }

        public List<Identifier> getModelIds() {
            return modelIds;
        }

        public List<Integer> getWeights() {
            return weights;
        }

        public int size() {
            return modelIds.size();
        }
    }



    protected static void registerSimpleItemModel(BlockStateModelGenerator generator, Block block, Identifier textureId) {
        TextureMap itemTextureMap = TextureMap.layer0(textureId);
        Models.GENERATED.upload(ModelIds.getItemModelId(block.asItem()), itemTextureMap, generator.modelCollector);
    }

    protected static void registerParentedItemModel(BlockStateModelGenerator generator, Block block, Identifier modelId) {
        generator.registerParentedItemModel(block, modelId);
    }
}