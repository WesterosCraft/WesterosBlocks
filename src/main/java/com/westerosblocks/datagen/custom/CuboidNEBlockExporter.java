package com.westerosblocks.datagen.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.custom.WCCuboidNEBlock;
import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;

/**
 * Exporter for NE (2-directional) cuboid blocks.
 * Extends CuboidBlockExporter and only customizes blockstate generation with facing rotations.
 */
public class CuboidNEBlockExporter extends CuboidBlockExporter {

    private static final FacingRotation[] NE_ROTATIONS = {
        new FacingRotation("east", 0),
        new FacingRotation("north", 90)
    };

    public static void registerCustomCuboidNEBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        if (!(block instanceof WCCuboidNEBlock cuboidBlock)) {
            throw new IllegalArgumentException("Block must be a WCCuboidNEBlock instance");
        }

        var states = definition.getStates();
        if (states == null || states.isEmpty()) {
            throw new IllegalStateException("Block definition states should never be null/empty after doInit() for block: " + getBlockName(block));
        }
        boolean hasMultipleStates = definition.getStateCount() > 1;

        CuboidNEBlockExporter exporter = new CuboidNEBlockExporter();
        Map<String, List<Identifier>> stateModelMap = exporter.generateModelsReturnMap(generator, block, definition);

        if (stateModelMap.isEmpty()) {
            WesterosBlocks.LOGGER.warn("No models generated for NE cuboid block: {}", getBlockName(block));
            return;
        }

        generator.blockStateCollector.accept(
                generateFacingBlockState(block, stateModelMap, states, hasMultipleStates, s -> NE_ROTATIONS));

        Identifier firstModel = stateModelMap.values().iterator().next().get(0);
        registerParentedItemModel(generator, block, firstModel);
    }
}
