package com.westerosblocks.datagen.models;

import com.westerosblocks.WesterosBlocks;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

import java.util.Optional;

/**
 * Standalone cross block export for builder-based block registration.
 * Generates cross models similar to CrossBlockExport but for the new system.
 */
public class CrossBlockExport extends ModelExport2 {
    
    public CrossBlockExport() {
    }
    
    @Override
    public void generateBlockStateModels(BlockStateModelGenerator generator, Block block, String texturePath) {
        generateBlockStateModels(generator, block, texturePath, false, 1);
    }
    
    /**
     * Generates block state models for cross blocks.
     * 
     * @param generator The BlockStateModelGenerator to use
     * @param block The block to generate models for
     * @param texturePath The texture path to use
     * @param isTinted Whether the block should be tinted
     * @param rotationCount Number of random rotations (1 or 4)
     */
    public void generateBlockStateModels(BlockStateModelGenerator generator, Block block, String texturePath, boolean isTinted, int rotationCount) {
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
    private Identifier createCrossModel(BlockStateModelGenerator generator, Block block, String texturePath, boolean isTinted) {
        String parentPath = isTinted ? "block/tinted/cross" : "block/untinted/cross";
        Identifier modelId = createModelId(block);
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
    
    @Override
    public void generateItemModels(ItemModelGenerator generator, Block block) {
        // Use the same approach as the old CrossBlockExport - no tinting for items
        // For the default case, we need to determine the texture path from the block name
        String texturePath = "flowers/" + block.getTranslationKey().replace("block.westerosblocks.", "");
        generateItemModels(generator, block, texturePath);
    }
    
    /**
     * Generates item models for cross blocks with a specific texture.
     * 
     * @param generator The ItemModelGenerator to use
     * @param block The block to generate item models for
     * @param texturePath The texture path to use
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