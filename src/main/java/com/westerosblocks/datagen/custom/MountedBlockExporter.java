package com.westerosblocks.datagen.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.BlockStateVariantMap;
import net.minecraft.data.client.VariantsBlockStateSupplier;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;


public class MountedBlockExporter extends BaseBlockExporter {

    /**
     * Registers a mounted block with custom models
     */
    public static void registerMountedBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        String blockName = getBlockName(block);

        Identifier modelId = createCustomModelId(blockName, "base_v1");

        BlockStateVariantMap variants = BlockStateVariantMap.create(Properties.HORIZONTAL_FACING)
        .register(Direction.NORTH, createVariant(modelId))
        .register(Direction.EAST, createVariant(modelId, 90))
        .register(Direction.SOUTH, createVariant(modelId, 180))
        .register(Direction.WEST, createVariant(modelId, 270));

        generator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(block).coordinate(variants)
        );

        registerParentedItemModel(generator, block, modelId);
    }

    private static Identifier createCustomModelId(String blockName, String variant) {
        String modelPath = "block/custom/" + blockName + "/" + variant;
        return WesterosBlocks.id(modelPath);
    }
}
