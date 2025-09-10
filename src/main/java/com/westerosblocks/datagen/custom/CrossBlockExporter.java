package com.westerosblocks.datagen.custom;

import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.custom.WCPlantBlock;

import java.util.Arrays;
import java.util.Optional;

/**
 * Exporter for cross/plant blocks following the unified builder pattern.
 * Generates cross models for decorative plants and vegetation blocks.
 */
public class CrossBlockExporter extends BaseBlockExporter {

    /**
     * Generates block state models for cross blocks with a single texture.
     * 
     * @param generator The BlockStateModelGenerator to use
     * @param block The block to generate models for
     * @param texturePath The texture path to use
     */
    public static void generateCross(BlockStateModelGenerator generator, Block block, String texturePath) {
        generateCross(generator, block, texturePath, false, 1);
    }

    /**
     * Generates block state models for cross blocks with rotation variants.
     * 
     * @param generator The BlockStateModelGenerator to use
     * @param block The block to generate models for
     * @param texturePath The texture path to use
     * @param isTinted Whether the block should be tinted
     * @param rotationCount Number of random rotations (1 or 4)
     */
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

    /**
     * Generates block state models for cross blocks with multiple random texture variants.
     * 
     * @param generator The BlockStateModelGenerator to use
     * @param block The block to generate models for
     * @param texturePaths Array of texture paths for random variants
     */
    public static void generateCrossWithRandomTextures(BlockStateModelGenerator generator, Block block, String[] texturePaths) {
        generateCrossWithRandomTextures(generator, block, texturePaths, false, 1);
    }

    /**
     * Generates block state models for cross blocks with multiple random texture variants and rotations.
     * 
     * @param generator The BlockStateModelGenerator to use
     * @param block The block to generate models for
     * @param texturePaths Array of texture paths for random variants
     * @param isTinted Whether the block should be tinted
     * @param rotationCount Number of random rotations per texture (1 or 4)
     */
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
        if (firstModelId != null) {
            generator.registerParentedItemModel(block, firstModelId);
        }
    }

    /**
     * Creates a cross model with the specified texture.
     * 
     * @param generator The BlockStateModelGenerator to use
     * @param block The block to create a model for
     * @param texturePath The texture path to use
     * @param isTinted Whether the block should be tinted
     * @return The created model identifier
     */
    private static Identifier createCrossModel(BlockStateModelGenerator generator, Block block, String texturePath, boolean isTinted) {
        Identifier modelId = createModelId(block);
        return createCrossModelWithId(generator, modelId, texturePath, isTinted);
    }

    /**
     * Creates a cross model with the specified model ID and texture.
     * 
     * @param generator The BlockStateModelGenerator to use
     * @param modelId The model identifier to use
     * @param texturePath The texture path to use
     * @param isTinted Whether the block should be tinted
     * @return The created model identifier
     */
    private static Identifier createCrossModelWithId(BlockStateModelGenerator generator, Identifier modelId, String texturePath, boolean isTinted) {
        String parentPath = isTinted ? "block/tinted/cross" : "block/untinted/cross";
        Identifier textureId = createBlockIdentifier(texturePath);
        
        TextureMap textureMap = new TextureMap()
            .put(TextureKey.CROSS, textureId);
        
        net.minecraft.data.client.Model model = new net.minecraft.data.client.Model(
            Optional.of(WesterosBlocks.id(parentPath)),
            Optional.empty(),
            TextureKey.CROSS
        );
        
        model.upload(modelId, textureMap, generator.modelCollector);
        return modelId;
    }

    /**
     * Generates block state models for layer-sensitive cross blocks.
     * Creates variants for each layer (1-8) to support placement on slabs and other height variations.
     * 
     * @param generator The BlockStateModelGenerator to use
     * @param block The layer-sensitive plant block to generate models for
     * @param texturePath The texture path to use
     */
    public static void generateLayerSensitiveCross(BlockStateModelGenerator generator, Block block, String texturePath) {
        generateLayerSensitiveCross(generator, block, texturePath, false, 1);
    }

    /**
     * Generates block state models for layer-sensitive cross blocks with multiple random textures.
     * Creates array-based blockstate variants for each layer with all texture variants.
     * 
     * @param generator The BlockStateModelGenerator to use
     * @param block The layer-sensitive plant block to generate models for
     * @param texturePaths Array of texture paths for random variants
     */
    public static void generateLayerSensitiveCrossWithRandomTextures(BlockStateModelGenerator generator, Block block, String[] texturePaths) {
        generateLayerSensitiveCrossWithRandomTextures(generator, block, texturePaths, false, 1);
    }

    /**
     * Generates block state models for layer-sensitive cross blocks with multiple random textures and rotations.
     * Creates array-based blockstate variants for each layer with all texture variants.
     * 
     * @param generator The BlockStateModelGenerator to use
     * @param block The layer-sensitive plant block to generate models for
     * @param texturePaths Array of texture paths for random variants
     * @param isTinted Whether the block should be tinted
     * @param rotationCount Number of random rotations per texture (1 or 4)
     */
    public static void generateLayerSensitiveCrossWithRandomTextures(BlockStateModelGenerator generator, Block block, String[] texturePaths, boolean isTinted, int rotationCount) {
        if (texturePaths.length == 0) {
            throw new IllegalArgumentException("At least one texture path is required");
        }

        // Verify this is actually a layer-sensitive plant
        if (!(block instanceof WCPlantBlock) || !((WCPlantBlock) block).isLayerSensitive()) {
            // Fall back to regular cross generation with random textures
            generateCrossWithRandomTextures(generator, block, texturePaths, isTinted, rotationCount);
            return;
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
        BlockStateVariantMap layerMap = BlockStateVariantMap.create(Properties.LAYERS);
        
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
            
            ((BlockStateVariantMap.SingleProperty<Integer>) layerMap).register(layer, Arrays.asList(allVariantsForLayer));
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
     * @param block The layer-sensitive plant block to generate models for
     * @param texturePath The texture path to use
     * @param isTinted Whether the block should be tinted
     * @param rotationCount Number of random rotations (1 or 4)
     */
    public static void generateLayerSensitiveCross(BlockStateModelGenerator generator, Block block, String texturePath, boolean isTinted, int rotationCount) {
        // Verify this is actually a layer-sensitive plant
        if (!(block instanceof WCPlantBlock) || !((WCPlantBlock) block).isLayerSensitive()) {
            // Fall back to regular cross generation
            generateCross(generator, block, texturePath, isTinted, rotationCount);
            return;
        }

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
        BlockStateVariantMap layerMap = BlockStateVariantMap.create(Properties.LAYERS);
        
        for (int layer = 1; layer <= 8; layer++) {
            if (rotationCount == 1) {
                // Single variant per layer
                ((BlockStateVariantMap.SingleProperty<Integer>) layerMap).register(layer, 
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
                ((BlockStateVariantMap.SingleProperty<Integer>) layerMap).register(layer, Arrays.asList(rotationVariants));
            }
        }
        
        generator.blockStateCollector.accept(
            VariantsBlockStateSupplier.create(block)
                .coordinate(layerMap)
        );

        // Register item model using layer 8 (full height)
        generator.registerParentedItemModel(block, layerModels[8]);
    }
}
