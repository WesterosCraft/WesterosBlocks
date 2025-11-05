package com.westerosblocks.block.custom;

import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

public class WCBeaconBlock extends WCCuboidBlock {

    public WCBeaconBlock(AbstractBlock.Settings settings, BlockDefinition def, VoxelShape shape) {
        super(settings, def, false, shape);
    }

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(BlockDefinition definition) {
            AbstractBlock.Settings settings = definition.makeSettings();

            // Calculate bounding box from cuboids
            VoxelShape shape = VoxelShapes.empty();
            if (definition.getCuboids() != null && !definition.getCuboids().isEmpty()) {
                for (BlockDefinition.CuboidElement cuboid : definition.getCuboids()) {
                    shape = VoxelShapes.union(shape, VoxelShapes.cuboid(
                        cuboid.getXMin(), cuboid.getYMin(), cuboid.getZMin(),
                        cuboid.getXMax(), cuboid.getYMax(), cuboid.getZMax()
                    ));
                }
            }

            // Use full cube if no cuboids defined
            if (shape.isEmpty()) {
                shape = VoxelShapes.fullCube();
            }

            return new WCBeaconBlock(settings, definition, shape);
        }
    }
}
