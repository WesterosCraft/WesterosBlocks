package com.westerosblocks.datagen.models;

import com.westerosblocks.block.custom.WCChairBlock;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

/**
 * Refactored version of ChairBlockExport that extends ModelExport2.
 * This demonstrates how the new utility class can simplify the code.
 */
public class ChairBlockExport extends ModelExport2 {

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator generator, Block block, String texturePath) {
        // Create different models for cardinal and diagonal directions
        Identifier cardinalModelId = createModel(
            generator, block, 
            "westerosblocks:block/custom/chair/simple_chair", 
            texturePath, "cardinal"
        );
        Identifier diagonalModelId = createModel(
            generator, block, 
            "westerosblocks:block/custom/chair/simple_chair_45", 
            texturePath, "diagonal"
        );

        // Create variants for each rotation (0-7 for 8 directions)
        // Cardinal directions (0, 2, 4, 6) use the base model with rotations
        // Diagonal directions (1, 3, 5, 7) use the 45-degree rotated model
        BlockStateVariantMap variants = BlockStateVariantMap.create(WCChairBlock.ROTATION)
            // Cardinal directions - use base model with Y rotations
            .register(0, createVariant(cardinalModelId))
            .register(2, createVariant(cardinalModelId, 90))
            .register(4, createVariant(cardinalModelId, 180))
            .register(6, createVariant(cardinalModelId, 270))

            // Diagonal directions - use 45-degree rotated model with adjusted Y rotations
            // The diagonal model is already rotated 45°, so we need to adjust the Y rotations
            .register(1, createVariant(diagonalModelId, 90))
            .register(3, createVariant(diagonalModelId, 180))
            .register(5, createVariant(diagonalModelId, 270))
            .register(7, createVariant(diagonalModelId));

        // Register the block state using the utility method
        registerBlockState(generator, block, variants);
    }

    @Override
    public void generateItemModels(ItemModelGenerator generator, Block block) {
        // Create a simple item model that inherits from the cardinal block model
        generateItemModel(generator, block, "cardinal");
    }
} 