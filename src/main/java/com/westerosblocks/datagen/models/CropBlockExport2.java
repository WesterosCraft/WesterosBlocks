package com.westerosblocks.datagen.models;

import com.westerosblocks.WesterosBlocks;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

import java.util.Optional;

/**
 * Crop block export class that extends ModelExport2 for the builder-based system.
 * Handles crop-specific model generation with support for layer sensitivity and rotations.
 */
public class CropBlockExport2 extends ModelExport2 {
    
    /**
     * Generates block state models for crop blocks.
     * 
     * @param generator The BlockStateModelGenerator to use
     * @param block The crop block to generate models for
     * @param texturePath The texture path for the crop
     */
    @Override
    public void generateBlockStateModels(BlockStateModelGenerator generator, Block block, String texturePath) {
        generateBlockStateModels(generator, block, texturePath, false, 1);
    }
    
    /**
     * Generates block state models for crop blocks with additional parameters.
     * 
     * @param generator The BlockStateModelGenerator to use
     * @param block The crop block to generate models for
     * @param texturePath The texture path for the crop
     * @param layerSensitive Whether the crop supports layers
     * @param rotationCount Number of random rotations (1-4)
     */
    public void generateBlockStateModels(BlockStateModelGenerator generator, Block block, String texturePath, 
                                       boolean layerSensitive, int rotationCount) {
        
        if (layerSensitive) {
            // Generate layer-sensitive variants
            generateLayerSensitiveVariants(generator, block, texturePath, rotationCount);
        } else {
            // Generate simple variants with rotations
            generateSimpleVariants(generator, block, texturePath, rotationCount);
        }
    }
    
    /**
     * Generates layer-sensitive crop variants.
     */
    private void generateLayerSensitiveVariants(BlockStateModelGenerator generator, Block block, 
                                               String texturePath, int rotationCount) {
        
        // For layer-sensitive crops, we need to handle each layer separately
        // This is a simplified implementation - in practice, you'd need to handle the LAYERS property
        generateSimpleVariants(generator, block, texturePath, rotationCount);
    }
    
    /**
     * Generates simple crop variants with rotations.
     */
    private void generateSimpleVariants(BlockStateModelGenerator generator, Block block, 
                                      String texturePath, int rotationCount) {
        
        // Create the crop model
        Identifier modelId = createCropModel(generator, block, texturePath, false);
        
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
    }
    
    /**
     * Creates a crop model with the specified parameters.
     * 
     * @param generator The BlockStateModelGenerator to use
     * @param block The crop block
     * @param texturePath The texture path
     * @param isTinted Whether the crop should be tinted
     * @return The created model identifier
     */
    private Identifier createCropModel(BlockStateModelGenerator generator, Block block, 
                                     String texturePath, boolean isTinted) {
        
        String parentPath = isTinted ? "block/tinted/crop" : "block/untinted/crop";
        Identifier modelId = createModelId(block);
        Identifier textureId = createBlockIdentifier(texturePath);
        
        TextureMap textureMap = new TextureMap()
            .put(TextureKey.CROP, textureId);
        
        Model model = new Model(
            Optional.of(WesterosBlocks.id(parentPath)),
            Optional.empty(),
            TextureKey.CROP
        );
        
        model.upload(modelId, textureMap, generator.modelCollector);
        return modelId;
    }
    
    /**
     * Generates item models for crop blocks.
     * 
     * @param generator The ItemModelGenerator to use
     * @param block The crop block to generate item models for
     */
    @Override
    public void generateItemModels(ItemModelGenerator generator, Block block) {
        generateItemModel(generator, block);
    }
    
    /**
     * Generates item models for crop blocks with a specific texture.
     * 
     * @param generator The ItemModelGenerator to use
     * @param block The crop block to generate item models for
     * @param texturePath The texture path to use for the item model
     */
    public void generateItemModels(ItemModelGenerator generator, Block block, String texturePath) {
        TextureMap textureMap = TextureMap.layer0(createBlockIdentifier(texturePath));
        
        Models.GENERATED.upload(
            ModelIds.getItemModelId(block.asItem()),
            textureMap,
            generator.writer
        );
    }
} 