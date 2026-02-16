package com.westerosblocks.datagen.custom;

import com.westerosblocks.block.custom.WCMountedSlabBlock;
import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.Block;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.BlockStateVariantMap;
import net.minecraft.data.client.VariantsBlockStateSupplier;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

public class MountedSlabBlockExporter extends BaseBlockExporter {

    public static void registerMountedSlabBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        Identifier topModelId = createCustomModelId(block, "top_v1");
        Identifier bottomModelId = createCustomModelId(block, "bottom_v1");

        BlockStateVariantMap variants = BlockStateVariantMap.create(WCMountedSlabBlock.FACING, WCMountedSlabBlock.HALF)
                // NORTH (y=0)
                .register(Direction.NORTH, BlockHalf.BOTTOM, createVariant(bottomModelId))
                .register(Direction.NORTH, BlockHalf.TOP, createVariant(topModelId))
                // EAST (y=90)
                .register(Direction.EAST, BlockHalf.BOTTOM, createVariant(bottomModelId, 90))
                .register(Direction.EAST, BlockHalf.TOP, createVariant(topModelId, 90))
                // SOUTH (y=180)
                .register(Direction.SOUTH, BlockHalf.BOTTOM, createVariant(bottomModelId, 180))
                .register(Direction.SOUTH, BlockHalf.TOP, createVariant(topModelId, 180))
                // WEST (y=270)
                .register(Direction.WEST, BlockHalf.BOTTOM, createVariant(bottomModelId, 270))
                .register(Direction.WEST, BlockHalf.TOP, createVariant(topModelId, 270));

        generator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(block).coordinate(variants)
        );

        registerParentedItemModel(generator, block, bottomModelId);
    }
}
