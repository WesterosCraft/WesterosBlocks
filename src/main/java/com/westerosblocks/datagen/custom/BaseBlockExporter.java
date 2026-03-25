package com.westerosblocks.datagen.custom;

import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import net.minecraft.data.client.VariantSettings.Rotation;
import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.datagen.ModTextureMap;
import com.westerosblocks.utils.ModProperties;

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
     * Fills texture array to the specified target size.
     * Missing slots are filled with the last provided texture.
     *
     * @param texturePaths The input texture paths
     * @param targetSize The desired array size
     * @return Array of exactly targetSize texture paths
     * @throws IllegalArgumentException if texturePaths is null or empty
     */
    protected static String[] fillTextureArray(String[] texturePaths, int targetSize) {
        validateTexturePaths(texturePaths, 1);

        String[] filledTextures = new String[targetSize];
        for (int i = 0; i < targetSize; i++) {
            if (i < texturePaths.length) {
                filledTextures[i] = texturePaths[i];
            } else {
                filledTextures[i] = texturePaths[texturePaths.length - 1];
            }
        }
        return filledTextures;
    }

    /**
     * Fills texture array to ensure exactly 6 textures for cube models.
     * Shorthand for {@code fillTextureArray(texturePaths, 6)}.
     *
     * @param texturePaths The input texture paths (1-6 textures)
     * @return Array of exactly 6 texture paths
     * @throws IllegalArgumentException if texturePaths is null or empty
     */
    protected static String[] fillTextureArray(String[] texturePaths) {
        return fillTextureArray(texturePaths, 6);
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
        return BlockStateVariant.create().put(VariantSettings.MODEL, modelId).put(VariantSettings.Y, toYRotation(rotation));
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
     * Converts degrees to VariantSettings.Rotation enum.
     *
     * @param degrees Rotation in degrees (0, 90, 180, or 270)
     * @return The corresponding Rotation enum value
     * @throws IllegalArgumentException if degrees is not 0, 90, 180, or 270
     */
    protected static VariantSettings.Rotation toYRotation(int degrees) {
        return switch (degrees) {
            case 0 -> Rotation.R0;
            case 90 -> Rotation.R90;
            case 180 -> Rotation.R180;
            case 270 -> Rotation.R270;
            default -> throw new IllegalArgumentException("Invalid rotation: " + degrees + ". Must be 0, 90, 180, or 270.");
        };
    }

    /**
     * Converts a Direction enum to rotation degrees for Y-axis rotation.
     * Standard mapping where model faces north at 0° rotation.
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

    /**
     * Converts a Direction to rotation degrees for south-default models.
     * Models that face south at 0° rotation (bench, table, chair).
     *
     * @param direction The direction
     * @return Rotation in degrees (0, 90, 180, or 270)
     */
    protected static int getFacingSouthDefaultRotation(Direction direction) {
        return switch (direction) {
            case NORTH -> 180;
            case SOUTH -> 0;
            case WEST -> 90;
            case EAST -> 270;
            default -> 0;
        };
    }

    /**
     * Gets the StateProperty from a block's state manager.
     *
     * @param block The block to inspect
     * @return The StateProperty, or null if not found
     */
    protected static ModProperties.StateProperty getStateProperty(Block block) {
        for (var property : block.getStateManager().getProperties()) {
            if (property instanceof ModProperties.StateProperty sp && "state".equals(property.getName()))
                return sp;
        }
        return null;
    }

    /**
     * Checks if a block has a "state" property.
     *
     * @param block The block to check
     * @return true if the block has a state property
     */
    protected static boolean hasStateProperty(Block block) {
        for (var property : block.getStateManager().getProperties()) {
            if (property.getName().equals("state"))
                return true;
        }
        return false;
    }

    /**
     * Creates a model identifier for custom (hand-authored) models.
     * Path format: {@code block/custom/blockName/variant}
     *
     * @param block The block
     * @param variant The model variant
     * @return The custom model identifier
     */
    protected static Identifier createCustomModelId(Block block, String variant) {
        String blockName = getBlockName(block);
        return WesterosBlocks.id("block/custom/" + blockName + "/" + variant);
    }

    /**
     * Creates a model identifier for generated models.
     * Path format: {@code block/blockName/variant}
     * Equivalent to {@link #createNestedModelId(Block, String)}.
     *
     * @param block The block
     * @param variant The model variant
     * @return The generated model identifier
     */
    protected static Identifier createGeneratedModelId(Block block, String variant) {
        return createNestedModelId(block, variant);
    }

    /**
     * Returns a model name with variant index suffix.
     * Example: {@code getModelName("base", 0)} returns {@code "base_v1"}.
     *
     * @param baseName The base name (e.g., state ID or "base")
     * @param variantIndex The 0-based variant index
     * @return The model name with suffix
     */
    protected static String getModelName(String baseName, int variantIndex) {
        return baseName + "_v" + (variantIndex + 1);
    }

    /**
     * Returns a model name with variant index and type suffix.
     * Example: {@code getModelName("base", 0, "bottom")} returns {@code "base_v1_bottom"}.
     *
     * @param baseName The base name
     * @param variantIndex The 0-based variant index
     * @param suffix The type suffix (e.g., "bottom", "top", "inner")
     * @return The model name with suffixes
     */
    protected static String getModelName(String baseName, int variantIndex, String suffix) {
        return baseName + "_v" + (variantIndex + 1) + "_" + suffix;
    }

    /**
     * Returns the stateID or "base" if null.
     *
     * @param stateID The state ID, possibly null
     * @return The state ID or "base"
     */
    protected static String getStateIdOrBase(String stateID) {
        return stateID == null ? "base" : stateID;
    }

    /**
     * Builds a list of weighted, optionally-rotated block state variants.
     * Used by leaves, flower pots, and other blocks that support random rotation.
     *
     * @param modelIds List of model identifiers
     * @param weights List of weights (null for uniform weight)
     * @param rotateRandom Whether to add 0/90/180/270° rotation variants
     * @return List of BlockStateVariants
     */
    protected static List<BlockStateVariant> buildRotatedVariantList(
            List<Identifier> modelIds, List<Integer> weights, boolean rotateRandom) {
        List<BlockStateVariant> variants = new ArrayList<>();
        int rotationCount = rotateRandom ? 4 : 1;

        for (int i = 0; i < modelIds.size(); i++) {
            for (int rotation = 0; rotation < rotationCount; rotation++) {
                BlockStateVariant variant = BlockStateVariant.create()
                        .put(VariantSettings.MODEL, modelIds.get(i));

                if (weights != null && weights.get(i) > 1) {
                    variant = variant.put(VariantSettings.WEIGHT, weights.get(i));
                }

                if (rotation > 0) {
                    variant = variant.put(VariantSettings.Y, toYRotation(90 * rotation));
                }

                variants.add(variant);
            }
        }

        return variants;
    }

    /**
     * Creates a VariantsBlockStateSupplier with optionally-rotated weighted variants.
     *
     * @param block The block
     * @param modelIds List of model identifiers
     * @param weights List of weights (null for uniform weight)
     * @param rotateRandom Whether to add 0/90/180/270° rotation variants
     * @return The block state supplier
     */
    protected static VariantsBlockStateSupplier createRotatedVariantsBlockState(
            Block block, List<Identifier> modelIds, List<Integer> weights, boolean rotateRandom) {
        List<BlockStateVariant> variants = buildRotatedVariantList(modelIds, weights, rotateRandom);

        if (variants.size() == 1) {
            return VariantsBlockStateSupplier.create(block, variants.get(0));
        } else {
            return VariantsBlockStateSupplier.create(block, variants.toArray(new BlockStateVariant[0]));
        }
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

    /**
     * Helper class mimicking old 1.18.2 StateObject.addVariant() pattern.
     * Collects variants with simple string conditions, then builds appropriate BlockStateVariantMap.
     *
     * This is shared across all exporters that need to build blockstates with multiple properties.
     */
    protected static class BlockStateBuilder {
        private final Block block;
        private final com.westerosblocks.utils.ModProperties.StateProperty stateProperty;

        // Maps condition string (e.g., "symmetrical=true,state=state0") to list of variants
        private final java.util.Map<String, List<BlockStateVariant>> variants = new java.util.HashMap<>();

        public BlockStateBuilder(Block block, com.westerosblocks.utils.ModProperties.StateProperty stateProperty) {
            this.block = block;
            this.stateProperty = stateProperty;
        }

        /**
         * Adds a variant with condition string and optional stateID.
         * Mimics: so.addVariant("symmetrical=true", variant, stateIDs)
         *
         * @param cond Base condition string (e.g., "symmetrical=true" or "")
         * @param variant The BlockStateVariant to add
         * @param stateID Optional state ID (null if no states)
         */
        public void addVariant(String cond, BlockStateVariant variant, String stateID) {
            String key;
            if (stateID == null) {
                // No state property, just use condition
                key = cond;
            } else {
                // Combine condition with state: "cond,state=stateID"
                key = cond + (cond.isEmpty() ? "" : ",") + "state=" + stateID;
            }

            variants.computeIfAbsent(key, k -> new ArrayList<>()).add(variant);
        }

        /**
         * Builds and registers the blockstate with the generator.
         * Analyzes collected variants and creates appropriate BlockStateVariantMap.
         */
        public void register(BlockStateModelGenerator generator) {
            if (variants.isEmpty()) {
                return;
            }

            // Analyze what properties we have by looking at the keys
            boolean hasSymmetrical = variants.keySet().stream().anyMatch(k -> k.contains("symmetrical="));
            boolean hasStates = variants.keySet().stream().anyMatch(k -> k.contains("state="));

            if (hasSymmetrical && hasStates) {
                // Case 1: Both SYMMETRICAL and STATE properties
                registerDoubleProperty(generator);
            } else if (hasStates) {
                // Case 2: STATE property only
                registerStateProperty(generator);
            } else if (hasSymmetrical) {
                // Case 3: SYMMETRICAL property only
                registerSymmetricalProperty(generator);
            } else {
                // Case 4: No properties (simple variants)
                registerSimple(generator);
            }
        }

        private void registerDoubleProperty(BlockStateModelGenerator generator) {
            BlockStateVariantMap.DoubleProperty<Boolean, String> variantMap =
                BlockStateVariantMap.create(com.westerosblocks.block.custom.WCSolidBlock.SYMMETRICAL, stateProperty);

            for (java.util.Map.Entry<String, List<BlockStateVariant>> entry : variants.entrySet()) {
                String key = entry.getKey();
                List<BlockStateVariant> variantList = entry.getValue();

                // Parse "symmetrical=true,state=state0"
                boolean symmetrical = key.contains("symmetrical=true");
                String stateID = extractStateID(key);

                if (variantList.size() == 1) {
                    variantMap.register(symmetrical, stateID, variantList.get(0));
                } else {
                    variantMap.register(symmetrical, stateID, variantList);
                }
            }

            generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block).coordinate(variantMap));
        }

        private void registerStateProperty(BlockStateModelGenerator generator) {
            BlockStateVariantMap.SingleProperty<String> variantMap =
                BlockStateVariantMap.create(stateProperty);

            for (java.util.Map.Entry<String, List<BlockStateVariant>> entry : variants.entrySet()) {
                String key = entry.getKey();
                List<BlockStateVariant> variantList = entry.getValue();
                String stateID = extractStateID(key);

                if (variantList.size() == 1) {
                    variantMap.register(stateID, variantList.get(0));
                } else {
                    variantMap.register(stateID, variantList);
                }
            }

            generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block).coordinate(variantMap));
        }

        private void registerSymmetricalProperty(BlockStateModelGenerator generator) {
            // Collect by symmetrical value
            List<BlockStateVariant> symTrue = new ArrayList<>();
            List<BlockStateVariant> symFalse = new ArrayList<>();

            for (java.util.Map.Entry<String, List<BlockStateVariant>> entry : variants.entrySet()) {
                boolean isSymmetrical = entry.getKey().contains("symmetrical=true");
                if (isSymmetrical) {
                    symTrue.addAll(entry.getValue());
                } else {
                    symFalse.addAll(entry.getValue());
                }
            }

            BlockStateVariantMap variantMap = BlockStateVariantMap.create(com.westerosblocks.block.custom.WCSolidBlock.SYMMETRICAL)
                .register(true, symTrue)
                .register(false, symFalse);

            generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block).coordinate(variantMap));
        }

        private void registerSimple(BlockStateModelGenerator generator) {
            List<BlockStateVariant> allVariants = new ArrayList<>();
            for (List<BlockStateVariant> variantList : variants.values()) {
                allVariants.addAll(variantList);
            }

            generator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(block, allVariants.toArray(new BlockStateVariant[0])));
        }

        /**
         * Extracts state ID from key like "symmetrical=true,state=state0" -> "state0"
         */
        private String extractStateID(String key) {
            int stateIndex = key.indexOf("state=");
            if (stateIndex >= 0) {
                String stateValue = key.substring(stateIndex + 6); // Skip "state="
                int commaIndex = stateValue.indexOf(',');
                if (commaIndex >= 0) {
                    stateValue = stateValue.substring(0, commaIndex);
                }
                return stateValue;
            }
            return null;
        }
    }

    /**
     * Gets an overlay texture by index, clamping to the last available texture.
     *
     * @param overlayTextures The overlay texture list
     * @param index The desired index
     * @return The texture at the index, or the last texture if index exceeds size, or null if empty
     */
    protected static String getOverlayTextureByIndex(List<String> overlayTextures, int index) {
        if (overlayTextures == null || overlayTextures.isEmpty()) {
            return null;
        }
        if (index >= overlayTextures.size()) {
            index = overlayTextures.size() - 1;
        }
        return overlayTextures.get(index);
    }

    /**
     * Creates a texture map for fence/wall blocks, with optional overlay textures.
     *
     * @param textures Array of 3 textures (top, bottom, side)
     * @param overlayTextures Array of 3 overlay textures, or null
     * @return The texture map
     */
    protected static TextureMap createFenceWallTextureMap(String[] textures, String[] overlayTextures) {
        if (overlayTextures != null) {
            return ModTextureMap.fenceWallOverlayTextures(
                    textures[0], textures[1], textures[2],
                    overlayTextures[0], overlayTextures[1], overlayTextures[2]);
        }
        return ModTextureMap.fenceWallTextures(textures[0], textures[1], textures[2]);
    }

    protected static void registerSimpleItemModel(BlockStateModelGenerator generator, Block block, Identifier textureId) {
        TextureMap itemTextureMap = TextureMap.layer0(textureId);
        Models.GENERATED.upload(ModelIds.getItemModelId(block.asItem()), itemTextureMap, generator.modelCollector);
    }

    protected static void registerParentedItemModel(BlockStateModelGenerator generator, Block block, Identifier modelId) {
        generator.registerParentedItemModel(block, modelId);
    }
}