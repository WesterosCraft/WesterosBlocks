package com.westerosblocks.datagen.models;

import com.westerosblocks.block.custom.WCWaySignBlock;
import com.westerosblocks.block.custom.WCWaySignWallBlock;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

/**
 * Refactored version of WaySignBlockExport that extends ModelExport2.
 * This demonstrates how the new utility class can simplify way sign block state generation.
 */
public class WaySignBlockExport extends ModelExport2 {

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator generator, Block block, String texturePath) {
        // Check if this is a wall block or main block
        if (block instanceof WCWaySignWallBlock) {
            generateWallBlockStateModels(generator, block, texturePath);
        } else {
            generateMainBlockStateModels(generator, block, texturePath);
        }
    }

    private void generateMainBlockStateModels(BlockStateModelGenerator generator, Block block, String texturePath) {
        // Create the base model for the fence post way sign
        Identifier waySignModelId = createModel(
            generator, block, 
            "westerosblocks:block/custom/sign_post/way_sign", 
            texturePath, "all"
        );

        // Create variants for each direction using the utility methods
        BlockStateVariantMap variants = BlockStateVariantMap.create(WCWaySignBlock.FACING)
            .register(Direction.NORTH, createVariant(waySignModelId))
            .register(Direction.EAST, createVariant(waySignModelId, 90))
            .register(Direction.SOUTH, createVariant(waySignModelId, 180))
            .register(Direction.WEST, createVariant(waySignModelId, 270));

        // Register the block state using the utility method
        registerBlockState(generator, block, variants);
    }

    private void generateWallBlockStateModels(BlockStateModelGenerator generator, Block block, String texturePath) {
        // Create the base model for the wall way sign
        Identifier waySignModelId = createModel(
            generator, block, 
            "westerosblocks:block/custom/sign_post/way_sign", 
            texturePath, "all"
        );

        // Create variants for each direction using the utility methods
        BlockStateVariantMap variants = BlockStateVariantMap.create(WCWaySignWallBlock.FACING)
            .register(Direction.NORTH, createVariant(waySignModelId))
            .register(Direction.EAST, createVariant(waySignModelId, 90))
            .register(Direction.SOUTH, createVariant(waySignModelId, 180))
            .register(Direction.WEST, createVariant(waySignModelId, 270));

        // Register the block state using the utility method
        registerBlockState(generator, block, variants);
    }

    @Override
    public void generateItemModels(ItemModelGenerator generator, Block block) {
        // Create a simple item model that inherits from the block model
        generateItemModel(generator, block, "all");
    }
} 