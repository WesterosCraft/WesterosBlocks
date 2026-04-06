package com.westerosblocks.datagen.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.custom.WCCuboidNSEWBlock;
import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.Block;
import net.minecraft.client.data.*;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;

/**
 * Exporter for NSEW (4-directional) cuboid blocks.
 * Extends CuboidBlockExporter and only customizes blockstate generation with facing rotations.
 */
public class CuboidNSEWBlockExporter extends CuboidBlockExporter {

    static final FacingRotation[] NSEW_ROTATIONS = {
        new FacingRotation("north", 270),
        new FacingRotation("east", 0),
        new FacingRotation("south", 90),
        new FacingRotation("west", 180)
    };

    public static void registerCustomCuboidNSEWBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        if (!(block instanceof WCCuboidNSEWBlock cuboidBlock)) {
            throw new IllegalArgumentException("Block must be a WCCuboidNSEWBlock instance");
        }

        var states = definition.getStates();
        if (states == null || states.isEmpty()) {
            throw new IllegalStateException("Block definition states should never be null/empty after doInit() for block: " + getBlockName(block));
        }
        boolean hasMultipleStates = definition.getStateCount() > 1;

        CuboidNSEWBlockExporter exporter = new CuboidNSEWBlockExporter();
        Map<String, List<Identifier>> stateModelMap = exporter.generateModelsReturnMap(generator, block, definition);

        if (stateModelMap.isEmpty()) {
            WesterosBlocks.LOGGER.warn("No models generated for NSEW cuboid block: {}", getBlockName(block));
            return;
        }

        generator.blockStateCollector.accept(
                generateFacingBlockState(block, stateModelMap, states, hasMultipleStates, s -> NSEW_ROTATIONS));

        Identifier firstModel = stateModelMap.values().iterator().next().get(0);
        registerParentedItemModel(generator, block, firstModel);
    }
}
