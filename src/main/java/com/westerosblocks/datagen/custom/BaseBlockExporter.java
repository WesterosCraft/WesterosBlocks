package com.westerosblocks.datagen.custom;

import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import net.minecraft.data.client.VariantSettings.Rotation;
import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.data.BlockDefinition;

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
 * <p>This abstract class follows block-models.md patterns and provides:
 * <ul>
 *   <li><b>Identifier Creation</b> - Block names, model IDs, texture paths</li>
 *   <li><b>Texture Management</b> - TextureMap validation and array filling</li>
 *   <li><b>Variant Creation</b> - BlockStateVariant with rotation support</li>
 *   <li><b>BlockState Suppliers</b> - Simple single-variant blockstates</li>
 *   <li><b>Item Model Registration</b> - Both simple and parented item models</li>
 * </ul>
 *
 * <p><b>Usage Pattern:</b>
 * <pre>{@code
 * public class MyBlockExporter extends BaseBlockExporter {
 *     public static void registerMyBlock(...) {
 *         // Use helper methods:
 *         Identifier modelId = createNestedModelId(block, "variant");
 *         TextureMap textureMap = new TextureMap()
 *             .put(TextureKey.ALL, createBlockIdentifier(texturePath));
 *         BlockStateVariant variant = createVariant(modelId, 90);
 *         registerParentedItemModel(generator, block, modelId);
 *     }
 * }
 * }</pre>
 *
 * <p><b>Design Philosophy:</b>
 * <ul>
 *   <li>All methods are {@code protected static} for easy access in subclasses</li>
 *   <li>Methods follow single responsibility principle</li>
 *   <li>Consistent naming: create*, register*, validate*</li>
 *   <li>Comprehensive validation with clear error messages</li>
 * </ul>
 *
 * @see <a href="block-models.md">block-models.md sections 5.2-5.6</a>
 */
public abstract class BaseBlockExporter {

    // ========================================
    // Identifier Creation Utilities
    // ========================================

    /**
     * Extracts the block name from the block's registry key.
     * Handles both formatted registry keys and plain block strings.
     *
     * <p><b>Examples:</b>
     * <ul>
     *   <li>{@code Block{westerosblocks:oak_log}} → {@code "oak_log"}</li>
     *   <li>{@code Block{stone}} → {@code "stone"}</li>
     * </ul>
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
     * <p><b>Examples:</b>
     * <ul>
     *   <li>{@code createNestedModelId(oakLog, "horizontal")} → {@code "westerosblocks:block/oak_log/horizontal"}</li>
     *   <li>{@code createNestedModelId(oakDoor, "bottom_left")} → {@code "westerosblocks:block/oak_door/bottom_left"}</li>
     * </ul>
     *
     * <p>Follows block-models.md section 5.2: Parent Block Model organization.
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
     * <p><b>Examples:</b>
     * <ul>
     *   <li>{@code createBlockIdentifier("oak_log")} → {@code "westerosblocks:block/oak_log"}</li>
     *   <li>{@code createBlockIdentifier("minecraft:stone")} → {@code "minecraft:stone"} (unchanged)</li>
     *   <li>{@code createBlockIdentifier("westerosblocks:custom/texture")} → {@code "westerosblocks:custom/texture"}</li>
     * </ul>
     *
     * <p>Follows block-models.md section 5.3: Using Texture Map.
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

    // ========================================
    // Texture Management Utilities
    // ========================================

    /**
     * Validates that texture paths array meets minimum requirements.
     * Ensures data integrity before texture processing.
     *
     * <p><b>Common Usage:</b>
     * <pre>{@code
     * validateTexturePaths(textures, 1);  // At least 1 texture
     * validateTexturePaths(textures, 2);  // At least 2 textures (e.g., door top/bottom)
     * validateTexturePaths(textures, 6);  // All 6 cube faces
     * }</pre>
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
     * <p><b>Texture Order:</b> {@code [down, up, north, south, east, west]}
     *
     * <p><b>Fill Behavior Examples:</b>
     * <ul>
     *   <li>{@code ["stone"]} → {@code ["stone", "stone", "stone", "stone", "stone", "stone"]}</li>
     *   <li>{@code ["dirt", "grass"]} → {@code ["dirt", "grass", "grass", "grass", "grass", "grass"]}</li>
     *   <li>{@code ["bottom", "top", "side"]} → {@code ["bottom", "top", "side", "side", "side", "side"]}</li>
     *   <li>{@code ["d", "u", "n", "s", "e", "w"]} → {@code ["d", "u", "n", "s", "e", "w"]} (no change)</li>
     * </ul>
     *
     * <p>Follows block-models.md cube texture pattern.
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

    // ========================================
    // BlockState Variant Utilities
    // ========================================

    /**
     * Creates a block state variant without rotation (0° rotation).
     * Follows block-models.md section 5.4: Custom BlockStateSupplier Method.
     *
     * <p><b>Usage:</b> For blocks that face north or have no rotation
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
     * <p><b>Rotation Examples:</b>
     * <ul>
     *   <li>{@code createVariant(modelId, 0)} - North facing (no rotation)</li>
     *   <li>{@code createVariant(modelId, 90)} - East facing</li>
     *   <li>{@code createVariant(modelId, 180)} - South facing</li>
     *   <li>{@code createVariant(modelId, 270)} - West facing</li>
     * </ul>
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

    // ========================================
    // BlockState Supplier Utilities
    // ========================================

    /**
     * Creates a simple blockstate supplier with a single model variant.
     * Follows block-models.md section 3.1: Simple Cube All pattern.
     *
     * <p><b>Usage:</b> For blocks with no state properties (no rotation, facing, etc.)
     *
     * <p><b>Example:</b>
     * <pre>{@code
     * Identifier modelId = Models.CUBE_ALL.upload(...);
     * generator.blockStateCollector.accept(createSimpleBlockState(block, modelId));
     * }</pre>
     *
     * @param block The block to create the blockstate for
     * @param modelId The model identifier to use
     * @return The blockstate supplier with a single variant
     */
    protected static VariantsBlockStateSupplier createSimpleBlockState(Block block, Identifier modelId) {
        return VariantsBlockStateSupplier.create(block, createVariant(modelId));
    }

    // ========================================
    // Model Creation and Upload Utilities
    // ========================================

    /**
     * Creates a Model with automatic tinted/untinted path resolution.
     * Centralizes the common pattern of path selection based on tinting.
     *
     * <p><b>Examples:</b>
     * <pre>{@code
     * createTintedModel(true, "ladder", TextureKey.TEXTURE)
     *   → Model with parent "westerosblocks:block/tinted/ladder"
     *
     * createTintedModel(false, "template_fence_gate", TextureKey.TEXTURE, TextureKey.PARTICLE)
     *   → Model with parent "westerosblocks:block/untinted/template_fence_gate"
     * }</pre>
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
     * <p><b>Example:</b>
     * <pre>{@code
     * // Before:
     * Identifier modelId = model.upload(createNestedModelId(block, "base"), textureMap, generator.modelCollector);
     *
     * // After:
     * Identifier modelId = uploadModel(model, block, "base", textureMap, generator.modelCollector);
     * }</pre>
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

    // ========================================
    // TextureMap Creation Utilities
    // ========================================
    // Note: Common texture map patterns are available in ModTextureMap.java:
    // - ModTextureMap.customAllSides() for cube models
    // - ModTextureMap.customSlab() for directional blocks (bottom/top/side)

    // ========================================
    // Weighted Variant Utilities
    // ========================================

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

    // ========================================
    // Direction Utilities
    // ========================================

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

    // ========================================
    // Model Registry Helper Class
    // ========================================

    /**
     * Helper class for collecting models with weights during random texture processing.
     * Provides type-safe container for model IDs and their associated weights.
     *
     * <p><b>Usage:</b>
     * <pre>{@code
     * ModelRegistry registry = new ModelRegistry();
     * for (TextureSet set : textureSets) {
     *     Identifier modelId = uploadModel(...);
     *     registry.add(modelId, set.weight);
     * }
     * createBlockstate(block, registry.getModelIds(), registry.getWeights());
     * }</pre>
     */
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

    // ========================================
    // Random Texture Utilities
    // ========================================

    /**
     * Container for texture variant information with weight.
     * DEPRECATED: Use BlockDefinition.TextureVariantSet instead.
     *
     * @deprecated Use {@link BlockDefinition.TextureVariantSet} directly
     */
    @Deprecated
    protected static class TextureVariantSet {
        public final List<String> textures;
        public final int weight;

        public TextureVariantSet(List<String> textures, int weight) {
            this.textures = textures != null ? textures : List.of();
            this.weight = Math.max(1, weight);
        }
    }

    /**
     * Extracts random texture variants from a BlockDefinition.
     * DEPRECATED: Use BlockDefinition.getRandomTextureVariantSets() instead.
     *
     * @param definition The block definition to extract variants from
     * @return List of texture variant sets, empty if no random textures defined
     * @deprecated Use {@link BlockDefinition#getRandomTextureVariantSets()} directly
     */
    @Deprecated
    protected static List<TextureVariantSet> extractRandomTextureVariants(BlockDefinition definition) {
        // Delegate to BlockDefinition method
        List<BlockDefinition.TextureVariantSet> sets = definition.getRandomTextureVariantSets();
        List<TextureVariantSet> result = new ArrayList<>();
        for (BlockDefinition.TextureVariantSet set : sets) {
            result.add(new TextureVariantSet(set.textures, set.weight));
        }
        return result;
    }

    /**
     * Converts random texture variants to String[][] format.
     * DEPRECATED: Use BlockDefinition.getRandomTextureArrays() instead.
     *
     * @param variants List of texture variant sets
     * @return 2D array where each row is a texture variant
     * @deprecated Use {@link BlockDefinition#getRandomTextureArrays()} directly
     */
    @Deprecated
    protected static String[][] convertToTextureArrays(List<TextureVariantSet> variants) {
        String[][] arrays = new String[variants.size()][];
        for (int i = 0; i < variants.size(); i++) {
            List<String> textures = variants.get(i).textures;
            arrays[i] = textures.toArray(new String[0]);
        }
        return arrays;
    }

    /**
     * Checks if definition has actual random textures (not just empty structure).
     * DEPRECATED: Use BlockDefinition.hasActualRandomTextures() instead.
     *
     * @param definition The block definition to check
     * @return true if at least one random texture variant has actual textures
     * @deprecated Use {@link BlockDefinition#hasActualRandomTextures()} directly
     */
    @Deprecated
    protected static boolean hasActualRandomTextures(BlockDefinition definition) {
        return definition.hasActualRandomTextures();
    }

    /**
     * Extracts random texture variants from a StateVariant.
     * DEPRECATED: Use BlockDefinition.getRandomTextureVariantSetsFromState() instead.
     *
     * @param stateVariant The state variant to extract random textures from
     * @return List of texture variant sets, empty if no random textures defined
     * @deprecated Use {@link BlockDefinition#getRandomTextureVariantSetsFromState(BlockDefinition.StateVariant)} directly
     */
    @Deprecated
    protected static List<TextureVariantSet> extractRandomTextureVariantsFromState(BlockDefinition.StateVariant stateVariant) {
        // Delegate to BlockDefinition method
        List<BlockDefinition.TextureVariantSet> sets = BlockDefinition.getRandomTextureVariantSetsFromState(stateVariant);
        List<TextureVariantSet> result = new ArrayList<>();
        for (BlockDefinition.TextureVariantSet set : sets) {
            result.add(new TextureVariantSet(set.textures, set.weight));
        }
        return result;
    }

    // ========================================
    // Item Model Registration Utilities
    // ========================================

    /**
     * Registers a simple 2D item model using the generated (layer0) template.
     * Follows block-models.md item model pattern for flat item textures.
     *
     * <p><b>Usage:</b> For blocks with 2D item representations (doors, torches, etc.)
     *
     * <p><b>Example:</b>
     * <pre>{@code
     * registerSimpleItemModel(generator, oakDoor,
     *     createBlockIdentifier("door/oak_door_bottom"));
     * }</pre>
     *
     * @param generator The BlockStateModelGenerator
     * @param block The block to create the item model for
     * @param textureId The texture identifier for the item (layer0)
     */
    protected static void registerSimpleItemModel(BlockStateModelGenerator generator, Block block, Identifier textureId) {
        TextureMap itemTextureMap = TextureMap.layer0(textureId);
        Models.GENERATED.upload(ModelIds.getItemModelId(block.asItem()), itemTextureMap, generator.modelCollector);
    }

    /**
     * Registers a parented item model that inherits from an existing block model.
     * Follows block-models.md parented item pattern for 3D block items.
     *
     * <p><b>Usage:</b> For blocks with 3D item representations (most solid blocks)
     *
     * <p><b>Example:</b>
     * <pre>{@code
     * Identifier modelId = Models.CUBE_ALL.upload(...);
     * registerParentedItemModel(generator, stoneBlock, modelId);
     * // Item will inherit the 3D block model
     * }</pre>
     *
     * <p><b>Note:</b> This is the most common pattern for block items - they simply
     * reference the block's model file rather than defining their own.
     *
     * @param generator The BlockStateModelGenerator
     * @param block The block to create the item model for
     * @param modelId The parent model identifier to inherit from
     */
    protected static void registerParentedItemModel(BlockStateModelGenerator generator, Block block, Identifier modelId) {
        generator.registerParentedItemModel(block, modelId);
    }
}