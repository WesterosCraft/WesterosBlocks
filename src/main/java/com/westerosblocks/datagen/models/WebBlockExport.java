package com.westerosblocks.datagen.models;

import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

/**
 * WebBlockExport that extends ModelExport2 for consistent model generation patterns.
 * Handles web blocks with cross-style rendering and optional tinting.
 */
public class WebBlockExport extends ModelExport2 {

    public WebBlockExport() {
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator generator, Block block, String texturePath) {
        generateBlockStateModels(generator, block, texturePath, false, 1);
    }

    /**
     * Generates block state models for web blocks.
     * 
     * @param generator The BlockStateModelGenerator to use
     * @param block The block to generate models for
     * @param texturePath The texture path to use
     * @param isTinted Whether the block should be tinted
     * @param rotationCount Number of random rotations (1 or 4)
     */
    public void generateBlockStateModels(BlockStateModelGenerator generator, Block block, String texturePath, 
                                       boolean isTinted, int rotationCount) {
        // Create the base model for the web block
        Identifier modelId = createModel(
            generator, block,
            "westerosblocks:block/tinted/cross",
            "westerosblocks:block/" + texturePath,
            "base",
            "cross"
        );

        // Register block state with random rotations if needed
        if (rotationCount == 1) {
            // Single rotation variant
            BlockStateVariant variant = BlockStateVariant.create().put(VariantSettings.MODEL, modelId);
            generator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(block, variant)
            );
        } else {
            // Multiple rotation variants for random appearance
            BlockStateVariant[] variants = new BlockStateVariant[rotationCount];
            for (int i = 0; i < rotationCount; i++) {
                int rotation = i * 90;
                variants[i] = BlockStateVariant.create()
                    .put(VariantSettings.MODEL, modelId)
                    .put(VariantSettings.Y, VariantSettings.Rotation.values()[i]);
            }
            
            generator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(block, variants)
            );
        }
    }

    @Override
    public void generateItemModels(ItemModelGenerator generator, Block block) {
        // Use the same approach as CrossBlockExport - no tinting for items
        // For the default case, we need to determine the texture path from the block name
        String texturePath = "web_block/" + block.getTranslationKey().replace("block.westerosblocks.", "");
        generateItemModels(generator, block, texturePath);
    }

    /**
     * Generates item models for web blocks with a specific texture.
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