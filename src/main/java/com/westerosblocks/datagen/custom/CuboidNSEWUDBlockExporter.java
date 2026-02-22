package com.westerosblocks.datagen.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.custom.WCCuboidNSEWUDBlock;
import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Exporter for NSEWUD (6-directional) cuboid blocks.
 * Extends CuboidBlockExporter and customizes blockstate generation with facing rotations.
 * Model cuboids are rotated 270° Y to face NORTH, matching the NORTH-default blockstate convention.
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

    /**
     * Returns cuboids rotated 270° around Y so the model faces NORTH.
     * Raw definition cuboids face EAST (the collision base direction),
     * but the blockstate uses NORTH-default rotations, so the model must face NORTH.
     */
    @Override
    protected List<BlockDefinition.CuboidElement> getModelCuboids(BlockDefinition definition) {
        List<BlockDefinition.CuboidElement> raw = definition.getCuboids();
        if (raw == null || raw.isEmpty()) return raw;
        List<BlockDefinition.CuboidElement> rotated = new ArrayList<>();
        for (BlockDefinition.CuboidElement c : raw) {
            rotated.add(rotateCuboidY270(c));
        }
        return rotated;
    }

    /**
     * Rotates a cuboid 270° clockwise around the Y axis (XZ plane, center 0.5,0.5).
     * Same math as WCCuboidNSEWUDBlock.rotateCuboidY(cuboid, 270).
     */
    private static BlockDefinition.CuboidElement rotateCuboidY270(BlockDefinition.CuboidElement cuboid) {
        double xMin = cuboid.getXMin();
        double xMax = cuboid.getXMax();
        double zMin = cuboid.getZMin();
        double zMax = cuboid.getZMax();

        // 270° Y rotation: newX = z, newZ = 1 - x
        double newXMin = zMin;
        double newXMax = zMax;
        double newZMin = 1.0 - xMax;
        double newZMax = 1.0 - xMin;

        return new BlockDefinition.CuboidElement() {
            @Override public double getXMin() { return newXMin; }
            @Override public double getXMax() { return newXMax; }
            @Override public double getYMin() { return cuboid.getYMin(); }
            @Override public double getYMax() { return cuboid.getYMax(); }
            @Override public double getZMin() { return newZMin; }
            @Override public double getZMax() { return newZMax; }
            @Override public int[] getSideTextures() { return cuboid.getSideTextures(); }
            @Override public int[] getSideRotations() { return cuboid.getSideRotations(); }
            @Override public boolean[] getNoTint() { return cuboid.getNoTint(); }
            @Override public String getShape() { return cuboid.getShape(); }
        };
    }
}
