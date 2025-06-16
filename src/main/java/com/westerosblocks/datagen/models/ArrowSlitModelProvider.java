package com.westerosblocks.datagen.models;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.custom.WCArrowSlitBlock;
import com.westerosblocks.block.custom.WCArrowSlitBlock.ArrowSlitType;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.math.Direction;
import java.util.Optional;

public class ArrowSlitModelProvider {
    public static void generateBlockStateModels(BlockStateModelGenerator generator, Block block) {
        // Create variants for each state and direction
        BlockStateVariantMap variants = BlockStateVariantMap.create(WCArrowSlitBlock.FACING, WCArrowSlitBlock.TYPE)
            // Single state
            .register(Direction.NORTH, ArrowSlitType.SINGLE, BlockStateVariant.create()
                .put(VariantSettings.MODEL, WesterosBlocks.id("block/custom/arrow_slit/test_arrow_slit_single")))
            .register(Direction.EAST, ArrowSlitType.SINGLE, BlockStateVariant.create()
                .put(VariantSettings.MODEL, WesterosBlocks.id("block/custom/arrow_slit/test_arrow_slit_single"))
                .put(VariantSettings.Y, VariantSettings.Rotation.R90))
            .register(Direction.SOUTH, ArrowSlitType.SINGLE, BlockStateVariant.create()
                .put(VariantSettings.MODEL, WesterosBlocks.id("block/custom/arrow_slit/test_arrow_slit_single"))
                .put(VariantSettings.Y, VariantSettings.Rotation.R180))
            .register(Direction.WEST, ArrowSlitType.SINGLE, BlockStateVariant.create()
                .put(VariantSettings.MODEL, WesterosBlocks.id("block/custom/arrow_slit/test_arrow_slit_single"))
                .put(VariantSettings.Y, VariantSettings.Rotation.R270))

            // Mid state
            .register(Direction.NORTH, ArrowSlitType.MID, BlockStateVariant.create()
                .put(VariantSettings.MODEL, WesterosBlocks.id("block/custom/arrow_slit/test_arrow_slit_mid")))
            .register(Direction.EAST, ArrowSlitType.MID, BlockStateVariant.create()
                .put(VariantSettings.MODEL, WesterosBlocks.id("block/custom/arrow_slit/test_arrow_slit_mid"))
                .put(VariantSettings.Y, VariantSettings.Rotation.R90))
            .register(Direction.SOUTH, ArrowSlitType.MID, BlockStateVariant.create()
                .put(VariantSettings.MODEL, WesterosBlocks.id("block/custom/arrow_slit/test_arrow_slit_mid"))
                .put(VariantSettings.Y, VariantSettings.Rotation.R180))
            .register(Direction.WEST, ArrowSlitType.MID, BlockStateVariant.create()
                .put(VariantSettings.MODEL, WesterosBlocks.id("block/custom/arrow_slit/test_arrow_slit_mid"))
                .put(VariantSettings.Y, VariantSettings.Rotation.R270))

            // Bottom state
            .register(Direction.NORTH, ArrowSlitType.BOTTOM, BlockStateVariant.create()
                .put(VariantSettings.MODEL, WesterosBlocks.id("block/custom/arrow_slit/test_arrow_slit_bottom")))
            .register(Direction.EAST, ArrowSlitType.BOTTOM, BlockStateVariant.create()
                .put(VariantSettings.MODEL, WesterosBlocks.id("block/custom/arrow_slit/test_arrow_slit_bottom"))
                .put(VariantSettings.Y, VariantSettings.Rotation.R90))
            .register(Direction.SOUTH, ArrowSlitType.BOTTOM, BlockStateVariant.create()
                .put(VariantSettings.MODEL, WesterosBlocks.id("block/custom/arrow_slit/test_arrow_slit_bottom"))
                .put(VariantSettings.Y, VariantSettings.Rotation.R180))
            .register(Direction.WEST, ArrowSlitType.BOTTOM, BlockStateVariant.create()
                .put(VariantSettings.MODEL, WesterosBlocks.id("block/custom/arrow_slit/test_arrow_slit_bottom"))
                .put(VariantSettings.Y, VariantSettings.Rotation.R270))

            // Top state
            .register(Direction.NORTH, ArrowSlitType.TOP, BlockStateVariant.create()
                .put(VariantSettings.MODEL, WesterosBlocks.id("block/custom/arrow_slit/test_arrow_slit_top")))
            .register(Direction.EAST, ArrowSlitType.TOP, BlockStateVariant.create()
                .put(VariantSettings.MODEL, WesterosBlocks.id("block/custom/arrow_slit/test_arrow_slit_top"))
                .put(VariantSettings.Y, VariantSettings.Rotation.R90))
            .register(Direction.SOUTH, ArrowSlitType.TOP, BlockStateVariant.create()
                .put(VariantSettings.MODEL, WesterosBlocks.id("block/custom/arrow_slit/test_arrow_slit_top"))
                .put(VariantSettings.Y, VariantSettings.Rotation.R180))
            .register(Direction.WEST, ArrowSlitType.TOP, BlockStateVariant.create()
                .put(VariantSettings.MODEL, WesterosBlocks.id("block/custom/arrow_slit/test_arrow_slit_top"))
                .put(VariantSettings.Y, VariantSettings.Rotation.R270));

        // Register the block state
        generator.blockStateCollector.accept(
            VariantsBlockStateSupplier.create(block)
                .coordinate(variants)
        );
    }

    public static void generateItemModels(ItemModelGenerator generator, Block block) {
        // Create a simple item model that inherits from the block model
        Model model = new Model(
            Optional.of(WesterosBlocks.id("block/custom/arrow_slit/test_arrow_slit_single")),
            Optional.empty()
        );
        generator.register(block.asItem(), model);
    }
} 