package com.westerosblocks.block.custom;

import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WCBeaconBlock extends WCCuboidBlock {

    private static final BlockDefinition.CuboidElement[] BEACON_CUBOIDS = {
        // Outer frame edges (6 textures: glass)
        createCuboid(0f, 0f, 0f, 0f, 1f, 1f, new int[] { 0, 1, 2, 3, 4, 5 }),      // West edge
        createCuboid(0f, 0f, 0f, 1f, 0f, 1f, new int[] { 0, 1, 2, 3, 4, 5 }),      // Bottom edge
        createCuboid(0f, 0f, 0f, 1f, 1f, 0f, new int[] { 0, 1, 2, 3, 4, 5 }),      // North edge
        createCuboid(1f, 0f, 0f, 1f, 1f, 1f, new int[] { 0, 1, 2, 3, 4, 5 }),      // East edge
        createCuboid(0f, 0f, 1f, 1f, 1f, 1f, new int[] { 0, 1, 2, 3, 4, 5 }),      // South edge

        // Middle frame (textures 6-11)
        createCuboid(0.125f, 0.00625f, 0.125f, 0.125f, 0.1875f, 0.875f, new int[] { 6, 7, 8, 9, 10, 11 }),
        createCuboid(0.125f, 0.00625f, 0.125f, 0.875f, 0.00625f, 0.875f, new int[] { 6, 7, 8, 9, 10, 11 }),
        createCuboid(0.125f, 0.00625f, 0.125f, 0.875f, 0.1875f, 0.125f, new int[] { 6, 7, 8, 9, 10, 11 }),
        createCuboid(0.875f, 0.00625f, 0.125f, 0.875f, 0.1875f, 0.875f, new int[] { 6, 7, 8, 9, 10, 11 }),
        createCuboid(0.125f, 0.00625f, 0.1875f, 0.875f, 0.1875f, 0.875f, new int[] { 6, 7, 8, 9, 10, 11 }),
        createCuboid(0.125f, 0.00625f, 0.875f, 0.875f, 0.1875f, 0.875f, new int[] { 6, 7, 8, 9, 10, 11 }),

        // Inner core frame (textures 12-17)
        createCuboid(0.1875f, 0.1875f, 0.1875f, 0.1875f, 0.875f, 0.8125f, new int[] { 12, 13, 14, 15, 16, 17 }),
        createCuboid(0.1875f, 0.1875f, 0.1875f, 0.8125f, 0.1875f, 0.8125f, new int[] { 12, 13, 14, 15, 16, 17 }),
        createCuboid(0.1875f, 0.1875f, 0.1875f, 0.8125f, 0.875f, 0.1875f, new int[] { 12, 13, 14, 15, 16, 17 }),
        createCuboid(0.8125f, 0.1875f, 0.1875f, 0.8125f, 0.875f, 0.8125f, new int[] { 12, 13, 14, 15, 16, 17 }),
        createCuboid(0.1875f, 0.875f, 0.1875f, 0.8125f, 0.875f, 0.8125f, new int[] { 12, 13, 14, 15, 16, 17 }),
        createCuboid(0.1875f, 0.1875f, 0.8125f, 0.8125f, 0.875f, 0.8125f, new int[] { 12, 13, 14, 15, 16, 17 })
    };

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(BlockDefinition definition) {
            AbstractBlock.Settings settings = definition.makeSettings();
            return new WCBeaconBlock(settings, definition);
        }
    }

    protected WCBeaconBlock(AbstractBlock.Settings settings, BlockDefinition def) {
        super(settings, def, 1, false);  // modelsPerState = 1, no toggleOnUse

        // Override cuboids with hardcoded beacon geometry
        List<BlockDefinition.CuboidElement> beaconCuboidList = new ArrayList<>(Arrays.asList(BEACON_CUBOIDS));

        this.cuboid_by_facing[0] = beaconCuboidList;
        this.SHAPE_BY_INDEX[0] = computeShapeFromCuboids(beaconCuboidList);
    }

    /**
     * Helper to create a CuboidElement with the given parameters.
     */
    private static BlockDefinition.CuboidElement createCuboid(
            double xMin, double yMin, double zMin,
            double xMax, double yMax, double zMax,
            int[] sideTextures) {
        return new BlockDefinition.CuboidElement() {
            @Override
            public double getXMin() { return xMin; }
            @Override
            public double getXMax() { return xMax; }
            @Override
            public double getYMin() { return yMin; }
            @Override
            public double getYMax() { return yMax; }
            @Override
            public double getZMin() { return zMin; }
            @Override
            public double getZMax() { return zMax; }
            @Override
            public int[] getSideTextures() { return sideTextures; }
            @Override
            public int[] getSideRotations() { return null; }
            @Override
            public boolean[] getNoTint() { return null; }
            @Override
            public String getShape() { return null; }
        };
    }
}
