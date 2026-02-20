package com.westerosblocks.datagen.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.custom.WCCuboidNSEWUDBlock;
import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;

/**
 * Exporter for NSEWUD (6-directional) cuboid blocks.
 * Extends CuboidBlockExporter and only customizes blockstate generation with facing rotations.
 */
public class CuboidNSEWUDBlockExporter extends CuboidBlockExporter {

    private static final FacingRotation[] NSEWUD_ROTATIONS = {
        new FacingRotation("north", 0, 0),
        new FacingRotation("east", 90, 0),
        new FacingRotation("south", 180, 0),
        new FacingRotation("west", 270, 0),
        new FacingRotation("up", 0, 270),
        new FacingRotation("down", 0, 90)
    };

    public static void registerCustomCuboidNSEWUDBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        if (!(block instanceof WCCuboidNSEWUDBlock)) {
            throw new IllegalArgumentException("Block must be a WCCuboidNSEWUDBlock instance");
        }

        var states = definition.getStates();
        if (states == null || states.isEmpty()) {
            throw new IllegalStateException("Block definition states should never be null/empty after doInit() for block: " + getBlockName(block));
        }
        boolean hasMultipleStates = definition.getStateCount() > 1;

        CuboidNSEWUDBlockExporter exporter = new CuboidNSEWUDBlockExporter();
        Map<String, List<Identifier>> stateModelMap = exporter.generateModelsReturnMap(generator, block, definition);

        if (stateModelMap.isEmpty()) {
            WesterosBlocks.LOGGER.warn("No models generated for NSEWUD cuboid block: {}", getBlockName(block));
            return;
        }

        generator.blockStateCollector.accept(
                generateFacingBlockState(block, stateModelMap, states, hasMultipleStates, s -> NSEWUD_ROTATIONS));

        Identifier firstModel = stateModelMap.values().iterator().next().get(0);
        registerParentedItemModel(generator, block, firstModel);
    }
}
