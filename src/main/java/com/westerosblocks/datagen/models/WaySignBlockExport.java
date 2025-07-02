package com.westerosblocks.datagen.models;

import com.westerosblocks.block.custom.WCWaySignBlock;
import com.westerosblocks.block.custom.WCWaySignWallBlock;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

public class WaySignBlockExport {

    public static void generateBlockStateModels(BlockStateModelGenerator generator, Block block, String texturePath) {
        // Check if this is a wall block or main block
        if (block instanceof WCWaySignWallBlock) {
            generateWallBlockStateModels(generator, block, texturePath);
        } else {
            generateMainBlockStateModels(generator, block, texturePath);
        }
    }

    private static void generateMainBlockStateModels(BlockStateModelGenerator generator, Block block, String texturePath) {
        // Create the base model for the fence post way sign
        Identifier waySignModelId = ModelExportUtils.createModel(
            generator, block, 
            "westerosblocks:block/custom/sign_post/way_sign", 
            texturePath, "all", "all"
        );

        // Create variants for each direction
        BlockStateVariantMap variants = BlockStateVariantMap.create(WCWaySignBlock.FACING)
            .register(Direction.NORTH, BlockStateVariant.create()
                .put(VariantSettings.MODEL, waySignModelId))
            .register(Direction.EAST, BlockStateVariant.create()
                .put(VariantSettings.MODEL, waySignModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R90))
            .register(Direction.SOUTH, BlockStateVariant.create()
                .put(VariantSettings.MODEL, waySignModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R180))
            .register(Direction.WEST, BlockStateVariant.create()
                .put(VariantSettings.MODEL, waySignModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R270));

        // Register the block state
        ModelExportUtils.registerBlockState(generator, block, variants);
    }

    private static void generateWallBlockStateModels(BlockStateModelGenerator generator, Block block, String texturePath) {
        // Create the base model for the wall way sign
        Identifier waySignModelId = ModelExportUtils.createModel(
            generator, block, 
            "westerosblocks:block/custom/sign_post/way_sign", 
            texturePath, "all", "all"
        );

        // Create variants for each direction
        BlockStateVariantMap variants = BlockStateVariantMap.create(WCWaySignWallBlock.FACING)
            .register(Direction.NORTH, BlockStateVariant.create()
                .put(VariantSettings.MODEL, waySignModelId))
            .register(Direction.EAST, BlockStateVariant.create()
                .put(VariantSettings.MODEL, waySignModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R90))
            .register(Direction.SOUTH, BlockStateVariant.create()
                .put(VariantSettings.MODEL, waySignModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R180))
            .register(Direction.WEST, BlockStateVariant.create()
                .put(VariantSettings.MODEL, waySignModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R270));

        // Register the block state
        ModelExportUtils.registerBlockState(generator, block, variants);
    }

    public static void generateItemModels(ItemModelGenerator generator, Block block) {
        // Create item model that inherits from block model
        ModelExportUtils.generateItemModel(generator, block, "all");
    }
} 