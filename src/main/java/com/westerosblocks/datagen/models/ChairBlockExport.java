package com.westerosblocks.datagen.models;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.custom.WCChairBlock;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

public class ChairBlockExport {

    public static void generateBlockStateModels(BlockStateModelGenerator generator, Block block, String texturePath) {
        // Create different models for cardinal and diagonal directions
        Identifier cardinalModelId = ModelExportUtils.createModel(
            generator, block, 
            "westerosblocks:block/custom/chair/simple_chair", 
            texturePath, "cardinal"
        );
        Identifier diagonalModelId = ModelExportUtils.createModel(
            generator, block, 
            "westerosblocks:block/custom/chair/simple_chair_45", 
            texturePath, "diagonal"
        );

        // Create variants for each rotation (0-7 for 8 directions)
        // Cardinal directions (0, 2, 4, 6) use the base model with rotations
        // Diagonal directions (1, 3, 5, 7) use the 45-degree rotated model
        BlockStateVariantMap variants = BlockStateVariantMap.create(WCChairBlock.ROTATION)
            // Cardinal directions - use base model with Y rotations
            .register(0, BlockStateVariant.create()
                .put(VariantSettings.MODEL, cardinalModelId))
            .register(2, BlockStateVariant.create()
                .put(VariantSettings.MODEL, cardinalModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R90))
            .register(4, BlockStateVariant.create()
                .put(VariantSettings.MODEL, cardinalModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R180))
            .register(6, BlockStateVariant.create()
                .put(VariantSettings.MODEL, cardinalModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R270))
            
            // Diagonal directions - use 45-degree rotated model with Y rotations
            // Fixed rotation mapping to correct the 180° offset
            .register(1, BlockStateVariant.create()
                .put(VariantSettings.MODEL, diagonalModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R90))
            .register(3, BlockStateVariant.create()
                .put(VariantSettings.MODEL, diagonalModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R270))
            .register(5, BlockStateVariant.create()
                .put(VariantSettings.MODEL, diagonalModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R180))
            .register(7, BlockStateVariant.create()
                .put(VariantSettings.MODEL, diagonalModelId));

        // Register the block state
        ModelExportUtils.registerBlockState(generator, block, variants);
    }

    public static void generateItemModels(ItemModelGenerator generator, Block block) {
        // Create a simple item model that inherits from the cardinal block model
        ModelExportUtils.generateItemModel(generator, block, "cardinal");
    }
} 