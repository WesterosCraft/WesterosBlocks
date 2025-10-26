package com.westerosblocks.block.custom;

import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

public class WCBeaconBlock extends WCCuboidBlock {
    private static final VoxelShape SHAPE = createBeaconShape();

    private static VoxelShape createBeaconShape() {
        VoxelShape shape = VoxelShapes.empty();

        // Outer frame
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0f, 0f, 0f, 0f, 1f, 1f));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0f, 0f, 0f, 1f, 0f, 1f));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0f, 0f, 0f, 1f, 1f, 0f));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(1f, 0f, 0f, 1f, 1f, 1f));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0f, 1f, 0f, 1f, 1f, 1f));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0f, 0f, 1f, 1f, 1f, 1f));

        // Bottom tier
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.125f, 0.00625f, 0.125f, 0.125f, 0.1875f, 0.875f));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.125f, 0.00625f, 0.125f, 0.875f, 0.00625f, 0.875f));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.125f, 0.00625f, 0.125f, 0.875f, 0.1875f, 0.125f));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.875f, 0.00625f, 0.125f, 0.875f, 0.1875f, 0.875f));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.125f, 0.00625f, 0.1875f, 0.875f, 0.1875f, 0.875f));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.125f, 0.00625f, 0.875f, 0.875f, 0.1875f, 0.875f));

        // Top tier
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.1875f, 0.1875f, 0.1875f, 0.1875f, 0.875f, 0.8125f));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.1875f, 0.1875f, 0.1875f, 0.8125f, 0.1875f, 0.8125f));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.1875f, 0.1875f, 0.1875f, 0.8125f, 0.875f, 0.1875f));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.8125f, 0.1875f, 0.1875f, 0.8125f, 0.875f, 0.8125f));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.1875f, 0.875f, 0.1875f, 0.8125f, 0.875f, 0.8125f));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.1875f, 0.1875f, 0.8125f, 0.8125f, 0.875f, 0.8125f));

        return shape;
    }

    public WCBeaconBlock(AbstractBlock.Settings settings, BlockDefinition def) {
        super(settings, def, false, SHAPE);
    }

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(BlockDefinition definition) {
            AbstractBlock.Settings settings = definition.makeSettings();
            return new WCBeaconBlock(settings, definition);
        }

        @Override
        public Block buildBlockClass(AbstractBlock.Settings settings, java.util.Map<String, Object> parameters) {
            return new WCBeaconBlock(settings, null);
        }
    }
}
