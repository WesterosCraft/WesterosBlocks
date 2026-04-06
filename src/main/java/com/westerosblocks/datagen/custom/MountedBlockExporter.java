package com.westerosblocks.datagen.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.Block;
import net.minecraft.client.data.*;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;

/**
 * Exporter for mounted (wall-facing) blocks with HORIZONTAL_FACING.
 * Extends CuboidBlockExporter to support both custom models and texture-defined cuboid models.
 * Custom models use NORTH=0° convention; generated cuboid models use EAST=0° convention.
 */
public class MountedBlockExporter extends CuboidBlockExporter {

    // Custom models face NORTH at 0°
    private static final FacingRotation[] CUSTOM_MODEL_ROTATIONS = {
        new FacingRotation("north", 0),
        new FacingRotation("east", 90),
        new FacingRotation("south", 180),
        new FacingRotation("west", 270)
    };

    public static void registerMountedBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        String blockName = getBlockName(block);

        var states = definition.getStates();
        if (states == null || states.isEmpty()) {
            throw new IllegalStateException("Block definition states should never be null/empty after doInit() for block: " + blockName);
        }
        boolean hasMultipleStates = definition.getStateCount() > 1;

        MountedBlockExporter exporter = new MountedBlockExporter();
        Map<String, List<Identifier>> stateModelMap = exporter.generateModelsReturnMap(generator, block, definition);

        if (stateModelMap.isEmpty()) {
            WesterosBlocks.LOGGER.warn("No models generated for mounted block: {}", blockName);
            return;
        }

        // Custom models face NORTH at 0°; generated cuboid models face EAST at 0° (same as NSEW)
        generator.blockStateCollector.accept(
                generateFacingBlockState(block, stateModelMap, states, hasMultipleStates,
                        state -> state.isCustomModel() ? CUSTOM_MODEL_ROTATIONS : CuboidNSEWBlockExporter.NSEW_ROTATIONS));

        Identifier firstModel = stateModelMap.values().iterator().next().get(0);
        registerParentedItemModel(generator, block, firstModel);
    }
}
