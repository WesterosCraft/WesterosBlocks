package com.westerosblocks.datagen.custom;

import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import com.westerosblocks.WesterosBlocks;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Exporter for cross-shaped blocks following block-models.md patterns.
 * Generates models for plants, webs, and other decorative cross-pattern blocks with optional layer sensitivity.
 */
public class CrossBlockExporter extends BaseBlockExporter {
    public static void generateCross(BlockStateModelGenerator generator, Block block, String texturePath, boolean isTinted, int rotationCount) {
        // Create the cross model
        Identifier modelId = createCrossModel(generator, block, texturePath, isTinted);
        
        // Create variants for different rotations
        if (rotationCount == 1) {
            // Single variant - no rotation
            BlockStateVariant variant = createVariant(modelId);
            generator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(block, variant)
            );
        } else {
            // Multiple variants with rotations
            BlockStateVariant[] variants = new BlockStateVariant[rotationCount];
            for (int rotation = 0; rotation < rotationCount; rotation++) {
                variants[rotation] = createVariant(modelId, rotation * 90);
            }
            generator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(block, variants)
            );
        }

        // Register item model
        generator.registerParentedItemModel(block, modelId);
    }

    public static void generateCrossWithRandomTextures(BlockStateModelGenerator generator, Block block, String[] texturePaths, boolean isTinted, int rotationCount) {
        if (texturePaths.length == 0) {
            throw new IllegalArgumentException("At least one texture path is required");
        }

        String blockName = getBlockName(block);
        BlockStateVariant[] allVariants = new BlockStateVariant[texturePaths.length * rotationCount];
        int variantIndex = 0;

        Identifier firstModelId = null;

        // Create models for each texture
        for (int textureIndex = 0; textureIndex < texturePaths.length; textureIndex++) {
            String texturePath = texturePaths[textureIndex];
            
            // Create model for this texture variant
            Identifier nestedModelId = WesterosBlocks.id("block/" + blockName + "/" + blockName + "_v" + (textureIndex + 1));
            Identifier modelId = createCrossModelWithId(generator, nestedModelId, texturePath, isTinted);
            
            if (firstModelId == null) {
                firstModelId = modelId;
            }

            // Create rotations for this texture
            for (int rotation = 0; rotation < rotationCount; rotation++) {
                if (rotation == 0) {
                    allVariants[variantIndex] = createVariant(modelId);
                } else {
                    allVariants[variantIndex] = createVariant(modelId, rotation * 90);
                }
                variantIndex++;
            }
        }

        generator.blockStateCollector.accept(
            VariantsBlockStateSupplier.create(block, allVariants)
        );

        // Register item model using the first texture variant
        generator.registerParentedItemModel(block, firstModelId);
    }


    private static Identifier createCrossModel(BlockStateModelGenerator generator, Block block, String texturePath, boolean isTinted) {
        Identifier modelId = createModelId(block);
        return createCrossModelWithId(generator, modelId, texturePath, isTinted);
    }

    private static Identifier createCrossModelWithId(BlockStateModelGenerator generator, Identifier modelId, String texturePath, boolean isTinted) {
        String parentPath = isTinted ? "block/tinted/cross" : "block/untinted/cross";
        Identifier textureId = createBlockIdentifier(texturePath);
        
        TextureMap textureMap = new TextureMap()
            .put(TextureKey.CROSS, textureId);
        
        Model model = new Model(
            Optional.of(WesterosBlocks.id(parentPath)),
            Optional.empty(),
            TextureKey.CROSS
        );
        
        model.upload(modelId, textureMap, generator.modelCollector);
        return modelId;
    }

    /**
     * Generates block state models for layer-sensitive cross blocks with multiple random textures and rotations.
     * Creates array-based blockstate variants for each layer with all texture variants.
     *
     * @param generator The BlockStateModelGenerator to use
     * @param block The layer-sensitive cross block to generate models for
     * @param texturePaths Array of texture paths for random variants
     * @param isTinted Whether the block should be tinted
     * @param rotationCount Number of random rotations per texture (1 or 4)
     */
    public static void generateLayerSensitiveCrossWithRandomTextures(BlockStateModelGenerator generator, Block block, String[] texturePaths, boolean isTinted, int rotationCount) {
        if (texturePaths.length == 0) {
            throw new IllegalArgumentException("At least one texture path is required");
        }

        String blockName = getBlockName(block);
        
        // Create models for each layer (1-8) and each texture variant
        Identifier[][] layerTextureModels = new Identifier[9][texturePaths.length]; // [layer][textureIndex]
        
        for (int layer = 1; layer <= 8; layer++) {
            for (int textureIndex = 0; textureIndex < texturePaths.length; textureIndex++) {
                String layerSuffix = (layer == 8) ? "" : "_layer" + layer;
                String textureSuffix = "_v" + (textureIndex + 1);
                String parentPath;
                
                // Layer 8 (full height) uses the regular cross template, not cross_layer8
                if (layer == 8) {
                    parentPath = isTinted ? "block/tinted/cross" : "block/untinted/cross";
                } else {
                    parentPath = isTinted ? "block/tinted/cross_layer" + layer : "block/untinted/cross_layer" + layer;
                }
                
                Identifier modelId = WesterosBlocks.id("block/" + blockName + "/base" + layerSuffix + textureSuffix);
                Identifier textureId = createBlockIdentifier(texturePaths[textureIndex]);
                
                TextureMap textureMap = new TextureMap()
                    .put(TextureKey.CROSS, textureId);
                
                Model model = new Model(
                    Optional.of(WesterosBlocks.id(parentPath)),
                    Optional.empty(),
                    TextureKey.CROSS
                );
                
                model.upload(modelId, textureMap, generator.modelCollector);
                layerTextureModels[layer][textureIndex] = modelId;
            }
        }
        
        // Create blockstate with layer variants as arrays
        BlockStateVariantMap.SingleProperty<Integer> layerMap = BlockStateVariantMap.create(Properties.LAYERS);

        for (int layer = 1; layer <= 8; layer++) {
            // Create array of variants for this layer (all texture variants with rotations)
            BlockStateVariant[] allVariantsForLayer = new BlockStateVariant[texturePaths.length * rotationCount];
            int variantIndex = 0;

            for (int textureIndex = 0; textureIndex < texturePaths.length; textureIndex++) {
                Identifier modelId = layerTextureModels[layer][textureIndex];

                for (int rotation = 0; rotation < rotationCount; rotation++) {
                    BlockStateVariant variant = BlockStateVariant.create()
                        .put(VariantSettings.MODEL, modelId);

                    if (rotation > 0) {
                        variant = variant.put(VariantSettings.Y, VariantSettings.Rotation.valueOf("R" + (rotation * 90)));
                    }

                    allVariantsForLayer[variantIndex] = variant;
                    variantIndex++;
                }
            }

            layerMap.register(layer, Arrays.asList(allVariantsForLayer));
        }
        
        generator.blockStateCollector.accept(
            VariantsBlockStateSupplier.create(block)
                .coordinate(layerMap)
        );

        // Register item model using the first texture variant at full height (layer 8)
        generator.registerParentedItemModel(block, layerTextureModels[8][0]);
    }

    /**
     * Generates block state models for layer-sensitive cross blocks with rotation variants.
     *
     * @param generator The BlockStateModelGenerator to use
     * @param block The layer-sensitive cross block to generate models for
     * @param texturePath The texture path to use
     * @param isTinted Whether the block should be tinted
     * @param rotationCount Number of random rotations (1 or 4)
     */
    public static void generateLayerSensitiveCross(BlockStateModelGenerator generator, Block block, String texturePath, boolean isTinted, int rotationCount) {

        String blockName = getBlockName(block);
        
        // Create models for each layer (1-8)
        Identifier[] layerModels = new Identifier[9]; // Index 0 unused, 1-8 for layers
        
        for (int layer = 1; layer <= 8; layer++) {
            String layerSuffix = "_layer" + layer;
            String parentPath;
            
            // Layer 8 (full height) uses the regular cross template, not cross_layer8
            if (layer == 8) {
                parentPath = isTinted ? "block/tinted/cross" : "block/untinted/cross";
            } else {
                parentPath = isTinted ? "block/tinted/cross_layer" + layer : "block/untinted/cross_layer" + layer;
            }
            
            Identifier modelId = WesterosBlocks.id("block/" + blockName + "/" + blockName + layerSuffix);
            Identifier textureId = createBlockIdentifier(texturePath);
            
            TextureMap textureMap = new TextureMap()
                .put(TextureKey.CROSS, textureId);
            
            Model model = new Model(
                Optional.of(WesterosBlocks.id(parentPath)),
                Optional.empty(),
                TextureKey.CROSS
            );
            
            model.upload(modelId, textureMap, generator.modelCollector);
            layerModels[layer] = modelId;
        }
        
        // Create blockstate with layer variants
        BlockStateVariantMap.SingleProperty<Integer> layerMap = BlockStateVariantMap.create(Properties.LAYERS);

        for (int layer = 1; layer <= 8; layer++) {
            if (rotationCount == 1) {
                // Single variant per layer
                layerMap.register(layer,
                    BlockStateVariant.create().put(VariantSettings.MODEL, layerModels[layer])
                );
            } else {
                // Multiple rotations per layer
                BlockStateVariant[] rotationVariants = new BlockStateVariant[rotationCount];
                for (int rotation = 0; rotation < rotationCount; rotation++) {
                    BlockStateVariant variant = BlockStateVariant.create()
                        .put(VariantSettings.MODEL, layerModels[layer]);

                    if (rotation > 0) {
                        variant = variant.put(VariantSettings.Y, VariantSettings.Rotation.valueOf("R" + (rotation * 90)));
                    }

                    rotationVariants[rotation] = variant;
                }
                layerMap.register(layer, Arrays.asList(rotationVariants));
            }
        }
        
        generator.blockStateCollector.accept(
            VariantsBlockStateSupplier.create(block)
                .coordinate(layerMap)
        );

        // Register item model using layer 8 (full height)
        generator.registerParentedItemModel(block, layerModels[8]);
    }

    /**
     * Registers a cross block using BlockDefinition.
     * Uses uniform iteration pattern - after doInit(), states is always non-empty
     * and each state always has randomTextures (normalized from simple textures).
     */
    public static void registerCrossBlockFromDefinition(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        boolean isTinted = definition.hasColorMult();
        boolean isLayerSensitive = definition.isLayerSensitive();

        // After doInit(), states is ALWAYS non-empty (at least synthetic base state exists)
        // Each state ALWAYS has randomTextures (normalized from simple textures)
        List<BlockDefinition.StateVariant> states = definition.getStates();

        if (states == null || states.isEmpty()) {
            throw new IllegalStateException("Block definition states should never be null/empty after doInit() for block: " + getBlockName(block));
        }

        // For now, use the first state (multi-state cross blocks can be handled later if needed)
        BlockDefinition.StateVariant state = states.get(0);

        // Determine if we have multiple texture variants that warrant random rotations
        // Multiple variants = multiple texture sets OR single set with multiple textures
        boolean hasMultipleVariants = false;
        int totalTextureCount = 0;

        if (state.getRandomTextureSetCount() > 1) {
            // Multiple texture sets = multiple variants
            hasMultipleVariants = true;
            for (int i = 0; i < state.getRandomTextureSetCount(); i++) {
                BlockDefinition.RandomTextureVariant set = state.getRandomTextureSet(i);
                totalTextureCount += (set != null) ? set.getTextureCount() : 0;
            }
        } else if (state.getRandomTextureSetCount() == 1) {
            BlockDefinition.RandomTextureVariant set = state.getRandomTextureSet(0);
            int textureCount = (set != null) ? set.getTextureCount() : 0;
            if (textureCount > 1) {
                // Single set with multiple textures = multiple variants
                hasMultipleVariants = true;
            }
            totalTextureCount = textureCount;
        }

        if (hasMultipleVariants) {
            // Collect all texture paths for random texture generation with rotations
            String[] texturePaths = new String[totalTextureCount];
            int textureIndex = 0;

            for (int setIdx = 0; setIdx < state.getRandomTextureSetCount(); setIdx++) {
                BlockDefinition.RandomTextureVariant set = state.getRandomTextureSet(setIdx);
                if (set != null) {
                    for (int i = 0; i < set.getTextureCount(); i++) {
                        String texture = set.getTextureByIndex(i);
                        if (texture != null) {
                            texturePaths[textureIndex++] = texture;
                        }
                    }
                }
            }

            // Generate with multiple variants and rotations (4 rotations for variety)
            if (isLayerSensitive) {
                generateLayerSensitiveCrossWithRandomTextures(generator, block, texturePaths, isTinted, 4);
            } else {
                generateCrossWithRandomTextures(generator, block, texturePaths, isTinted, 4);
            }
        } else {
            // Single texture variant, no random rotations needed
            String texturePath = state.getTextureByIndex(0);

            if (texturePath == null) {
                throw new IllegalArgumentException("Cross block '" + getBlockName(block) + "' has no valid textures");
            }

            // Generate with single variant, no rotations
            if (isLayerSensitive) {
                generateLayerSensitiveCross(generator, block, texturePath, isTinted, 1);
            } else {
                generateCross(generator, block, texturePath, isTinted, 1);
            }
        }
    }
}
