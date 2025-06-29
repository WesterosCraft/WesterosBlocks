package com.westerosblocks.datagen.models;

import com.westerosblocks.block.custom.WCArrowSlitBlock;
import com.westerosblocks.block.custom.WCArrowSlitBlock.ArrowSlitType;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

public class ArrowSlitBlockExport {

    public static void generateBlockStateModels(BlockStateModelGenerator generator, Block block, String texturePath) {
        // Create the base models for each type
        Identifier singleModelId = ModelExportUtils.createModelWithKey1(
            generator, block, 
            "westerosblocks:block/custom/arrow_slits/arrow_slit_single", 
            texturePath, "single"
        );
        Identifier midModelId = ModelExportUtils.createModelWithKey1(
            generator, block, 
            "westerosblocks:block/custom/arrow_slits/arrow_slit_middle", 
            texturePath, "middle"
        );
        Identifier topModelId = ModelExportUtils.createModelWithKey1(
            generator, block, 
            "westerosblocks:block/custom/arrow_slits/arrow_slit_top", 
            texturePath, "top"
        );
        Identifier bottomModelId = ModelExportUtils.createModelWithKey1(
            generator, block, 
            "westerosblocks:block/custom/arrow_slits/arrow_slit_bottom", 
            texturePath, "bottom"
        );

        // Create variants for each state and direction
        BlockStateVariantMap variants = BlockStateVariantMap.create(WCArrowSlitBlock.FACING, WCArrowSlitBlock.TYPE)
            // Single state
            .register(Direction.NORTH, ArrowSlitType.SINGLE, BlockStateVariant.create()
                .put(VariantSettings.MODEL, singleModelId))
            .register(Direction.EAST, ArrowSlitType.SINGLE, BlockStateVariant.create()
                .put(VariantSettings.MODEL, singleModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R90))
            .register(Direction.SOUTH, ArrowSlitType.SINGLE, BlockStateVariant.create()
                .put(VariantSettings.MODEL, singleModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R180))
            .register(Direction.WEST, ArrowSlitType.SINGLE, BlockStateVariant.create()
                .put(VariantSettings.MODEL, singleModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R270))

            // Middle state
            .register(Direction.NORTH, ArrowSlitType.MIDDLE, BlockStateVariant.create()
                .put(VariantSettings.MODEL, midModelId))
            .register(Direction.EAST, ArrowSlitType.MIDDLE, BlockStateVariant.create()
                .put(VariantSettings.MODEL, midModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R90))
            .register(Direction.SOUTH, ArrowSlitType.MIDDLE, BlockStateVariant.create()
                .put(VariantSettings.MODEL, midModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R180))
            .register(Direction.WEST, ArrowSlitType.MIDDLE, BlockStateVariant.create()
                .put(VariantSettings.MODEL, midModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R270))

            // Bottom state
            .register(Direction.NORTH, ArrowSlitType.BOTTOM, BlockStateVariant.create()
                .put(VariantSettings.MODEL, bottomModelId))
            .register(Direction.EAST, ArrowSlitType.BOTTOM, BlockStateVariant.create()
                .put(VariantSettings.MODEL, bottomModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R90))
            .register(Direction.SOUTH, ArrowSlitType.BOTTOM, BlockStateVariant.create()
                .put(VariantSettings.MODEL, bottomModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R180))
            .register(Direction.WEST, ArrowSlitType.BOTTOM, BlockStateVariant.create()
                .put(VariantSettings.MODEL, bottomModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R270))

            // Top state
            .register(Direction.NORTH, ArrowSlitType.TOP, BlockStateVariant.create()
                .put(VariantSettings.MODEL, topModelId))
            .register(Direction.EAST, ArrowSlitType.TOP, BlockStateVariant.create()
                .put(VariantSettings.MODEL, topModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R90))
            .register(Direction.SOUTH, ArrowSlitType.TOP, BlockStateVariant.create()
                .put(VariantSettings.MODEL, topModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R180))
            .register(Direction.WEST, ArrowSlitType.TOP, BlockStateVariant.create()
                .put(VariantSettings.MODEL, topModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R270));

        // Register the block state
        ModelExportUtils.registerBlockState(generator, block, variants);
    }

    public static void generateItemModels(ItemModelGenerator generator, Block block) {
        // Create a simple item model that inherits from the block model
        ModelExportUtils.generateItemModel(generator, block, "single");
    }
} 