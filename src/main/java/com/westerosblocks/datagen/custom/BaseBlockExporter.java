package com.westerosblocks.datagen.custom;

import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import net.minecraft.data.client.BlockStateVariant;
import net.minecraft.data.client.VariantSettings;
import net.minecraft.data.client.VariantSettings.Rotation;
import com.westerosblocks.WesterosBlocks;

/**
 * Base class providing shared utilities for all block exporters.
 * Centralizes common functionality to eliminate code duplication.
 */
public abstract class BaseBlockExporter {

    /**
     * Extracts the block name from the block's registry key.
     * 
     * @param block The block
     * @return The block name
     */
    protected static String getBlockName(Block block) {
        String blockString = block.toString();
        if (blockString.contains(":")) {
            return blockString.split(":")[1].replace("}", "");
        }
        return blockString.toLowerCase().replace("block{", "").replace("}", "");
    }

    /**
     * Creates an identifier for block textures, handling namespaces properly.
     * 
     * @param texturePath The texture path (can include namespace like "westerosblocks:block/white_door")
     * @return The identifier for the block texture
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
     * Validates that texture paths array is not empty.
     * 
     * @param texturePaths The texture paths to validate
     * @param minRequired The minimum number of textures required
     * @throws IllegalArgumentException if validation fails
     */
    protected static void validateTexturePaths(String[] texturePaths, int minRequired) {
        if (texturePaths == null || texturePaths.length < minRequired) {
            throw new IllegalArgumentException(
                "At least " + minRequired + " texture path(s) required, got " + 
                (texturePaths == null ? 0 : texturePaths.length));
        }
    }

    /**
     * Fills texture array to ensure exactly 6 textures (for cube models).
     * Missing slots are filled with the last provided texture.
     * 
     * @param texturePaths The input texture paths
     * @return Array of exactly 6 texture paths
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
     * Creates a block state variant without rotation.
     * 
     * @param modelId The model identifier
     * @return The block state variant
     */
    protected static BlockStateVariant createVariant(Identifier modelId) {
        return BlockStateVariant.create().put(VariantSettings.MODEL, modelId);
    }

    /**
     * Creates a block state variant with rotation.
     * 
     * @param modelId The model identifier
     * @param rotation The rotation in degrees (0, 90, 180, 270)
     * @return The block state variant
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
}