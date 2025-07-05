package com.westerosblocks.datagen.models;

import com.westerosblocks.block.custom.WCArrowSlitBlock;
import com.westerosblocks.block.custom.WCArrowSlitBlock.ArrowSlitType;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

/**
 * Refactored version of ArrowSlitBlockExport that extends ModelExport2.
 * This demonstrates how the new utility class can simplify complex block state generation.
 */
public class ArrowSlitBlockExport2 extends ModelExport2 {

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator generator, Block block, String texturePath) {
        // Create the base models for each type
        Identifier singleModelId = createModelWithKey1(
            generator, block, 
            "westerosblocks:block/custom/arrow_slits/arrow_slit_single", 
            texturePath, "single"
        );
        Identifier midModelId = createModelWithKey1(
            generator, block, 
            "westerosblocks:block/custom/arrow_slits/arrow_slit_middle", 
            texturePath, "middle"
        );
        Identifier topModelId = createModelWithKey1(
            generator, block, 
            "westerosblocks:block/custom/arrow_slits/arrow_slit_top", 
            texturePath, "top"
        );
        Identifier bottomModelId = createModelWithKey1(
            generator, block, 
            "westerosblocks:block/custom/arrow_slits/arrow_slit_bottom", 
            texturePath, "bottom"
        );

        // Create variants for each state and direction using the utility methods
        BlockStateVariantMap variants = BlockStateVariantMap.create(WCArrowSlitBlock.FACING, WCArrowSlitBlock.TYPE)
            // Single state - use utility method for cardinal directions
            .register(Direction.NORTH, ArrowSlitType.SINGLE, createVariant(singleModelId))
            .register(Direction.EAST, ArrowSlitType.SINGLE, createVariant(singleModelId, 90))
            .register(Direction.SOUTH, ArrowSlitType.SINGLE, createVariant(singleModelId, 180))
            .register(Direction.WEST, ArrowSlitType.SINGLE, createVariant(singleModelId, 270))

            // Middle state
            .register(Direction.NORTH, ArrowSlitType.MIDDLE, createVariant(midModelId))
            .register(Direction.EAST, ArrowSlitType.MIDDLE, createVariant(midModelId, 90))
            .register(Direction.SOUTH, ArrowSlitType.MIDDLE, createVariant(midModelId, 180))
            .register(Direction.WEST, ArrowSlitType.MIDDLE, createVariant(midModelId, 270))

            // Bottom state
            .register(Direction.NORTH, ArrowSlitType.BOTTOM, createVariant(bottomModelId))
            .register(Direction.EAST, ArrowSlitType.BOTTOM, createVariant(bottomModelId, 90))
            .register(Direction.SOUTH, ArrowSlitType.BOTTOM, createVariant(bottomModelId, 180))
            .register(Direction.WEST, ArrowSlitType.BOTTOM, createVariant(bottomModelId, 270))

            // Top state
            .register(Direction.NORTH, ArrowSlitType.TOP, createVariant(topModelId))
            .register(Direction.EAST, ArrowSlitType.TOP, createVariant(topModelId, 90))
            .register(Direction.SOUTH, ArrowSlitType.TOP, createVariant(topModelId, 180))
            .register(Direction.WEST, ArrowSlitType.TOP, createVariant(topModelId, 270));

        // Register the block state using the utility method
        registerBlockState(generator, block, variants);
    }

    @Override
    public void generateItemModels(ItemModelGenerator generator, Block block) {
        // Create a simple item model that inherits from the block model
        generateItemModel(generator, block, "single");
    }
} 