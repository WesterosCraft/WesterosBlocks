package com.westerosblocks.datagen.models;

import net.minecraft.block.Block;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

public class TrapDoorBlockExport extends ModelExport2 {

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator generator, Block block, String texturePath) {
        // Create the base models for each trap door state
        Identifier bottomModelId = createModelWithKey1(
            generator, block, 
            "westerosblocks:block/custom/trapdoor/trapdoor_bottom", 
            texturePath, "bottom"
        );
        Identifier topModelId = createModelWithKey1(
            generator, block, 
            "westerosblocks:block/custom/trapdoor/trapdoor_top", 
            texturePath, "top"
        );
        Identifier openModelId = createModelWithKey1(
            generator, block, 
            "westerosblocks:block/custom/trapdoor/trapdoor_open", 
            texturePath, "open"
        );

        // Create variants for all trap door states using the utility methods
        BlockStateVariantMap variants = BlockStateVariantMap.create(TrapdoorBlock.FACING, TrapdoorBlock.OPEN, TrapdoorBlock.HALF)
            // Bottom half, closed - use utility method for cardinal directions
            .register(Direction.NORTH, false, BlockHalf.BOTTOM, createVariant(bottomModelId))
            .register(Direction.EAST, false, BlockHalf.BOTTOM, createVariant(bottomModelId, 90))
            .register(Direction.SOUTH, false, BlockHalf.BOTTOM, createVariant(bottomModelId, 180))
            .register(Direction.WEST, false, BlockHalf.BOTTOM, createVariant(bottomModelId, 270))

            // Top half, closed
            .register(Direction.NORTH, false, BlockHalf.TOP, createVariant(topModelId))
            .register(Direction.EAST, false, BlockHalf.TOP, createVariant(topModelId, 90))
            .register(Direction.SOUTH, false, BlockHalf.TOP, createVariant(topModelId, 180))
            .register(Direction.WEST, false, BlockHalf.TOP, createVariant(topModelId, 270))

            // Bottom half, open
            .register(Direction.NORTH, true, BlockHalf.BOTTOM, createVariant(openModelId))
            .register(Direction.EAST, true, BlockHalf.BOTTOM, createVariant(openModelId, 90))
            .register(Direction.SOUTH, true, BlockHalf.BOTTOM, createVariant(openModelId, 180))
            .register(Direction.WEST, true, BlockHalf.BOTTOM, createVariant(openModelId, 270))

            // Top half, open
            .register(Direction.NORTH, true, BlockHalf.TOP, createVariant(openModelId))
            .register(Direction.EAST, true, BlockHalf.TOP, createVariant(openModelId, 90))
            .register(Direction.SOUTH, true, BlockHalf.TOP, createVariant(openModelId, 180))
            .register(Direction.WEST, true, BlockHalf.TOP, createVariant(openModelId, 270));

        // Register the block state using the utility method
        registerBlockState(generator, block, variants);
    }

    @Override
    public void generateItemModels(ItemModelGenerator generator, Block block) {
        // Create a simple item model that inherits from the bottom block model
        generateItemModel(generator, block, "bottom");
    }
} 