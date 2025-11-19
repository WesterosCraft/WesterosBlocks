package com.westerosblocks.block.custom;

import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.utils.ModProperties;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.Direction;

import java.util.List;

public class WCCuboidNSEWBlock extends WCCuboidBlock {
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(BlockDefinition definition) {
            AbstractBlock.Settings settings = definition.makeSettings();
            ModProperties.StateProperty stateProperty = definition.buildStateProperty();
            boolean doToggleOnUse = definition.toggleOnUse();

            tempSTATE = stateProperty;

            return new WCCuboidNSEWBlock(settings, definition, doToggleOnUse);
        }
    }

    public WCCuboidNSEWBlock(AbstractBlock.Settings settings, BlockDefinition def, boolean doToggleOnUse) {
        super(settings, def, 4, doToggleOnUse);  // modelsPerState = 4 (one per facing)

        // Rotate cuboids for each facing direction
        int stcnt = def.getStateCount();
        for (int stidx = 0; stidx < stcnt; stidx++) {
            int off = stidx * this.modelsPerState;

            // Rotate base cuboids to create 4 facing variants
            List<BlockDefinition.CuboidElement> baseCuboids = cuboid_by_facing[off];
            if (baseCuboids != null && !baseCuboids.isEmpty()) {
                for (BlockDefinition.CuboidElement cuboid : baseCuboids) {
                    cuboid_by_facing[off + 1].add(rotateCuboidY(cuboid, 90));   // SOUTH
                    cuboid_by_facing[off + 2].add(rotateCuboidY(cuboid, 180));  // WEST
                    cuboid_by_facing[off + 3].add(rotateCuboidY(cuboid, 270));  // NORTH
                }
            }
        }

        // Compute shapes from rotated cuboids
        for (int i = 0; i < cuboid_by_facing.length; i++) {
            if (SHAPE_BY_INDEX[i] == null) {
                SHAPE_BY_INDEX[i] = computeShapeFromCuboids(cuboid_by_facing[i]);
            }
        }

        // Set default state with facing
        BlockState defbs = this.getDefaultState()
            .with(FACING, Direction.EAST)
            .with(WATERLOGGED, false);

        if (STATE != null) {
            defbs = defbs.with(STATE, STATE.defValue);
        }
        this.setDefaultState(defbs);
    }

    /**
     * Rotates a cuboid element around the Y-axis.
     * @param cuboid Original cuboid
     * @param degrees Rotation angle (90, 180, or 270)
     * @return Rotated cuboid element
     */
    private BlockDefinition.CuboidElement rotateCuboidY(BlockDefinition.CuboidElement cuboid, int degrees) {
        // Create a new cuboid with rotated coordinates
        // Rotation is around center point (0.5, 0.5) in XZ plane
        double xMin = cuboid.getXMin();
        double xMax = cuboid.getXMax();
        double yMin = cuboid.getYMin();
        double yMax = cuboid.getYMax();
        double zMin = cuboid.getZMin();
        double zMax = cuboid.getZMax();

        double newXMin, newXMax, newZMin, newZMax;

        switch (degrees % 360) {
            case 90:  // Rotate 90° clockwise (viewed from above)
                newXMin = 1.0 - zMax;
                newXMax = 1.0 - zMin;
                newZMin = xMin;
                newZMax = xMax;
                break;
            case 180:  // Rotate 180°
                newXMin = 1.0 - xMax;
                newXMax = 1.0 - xMin;
                newZMin = 1.0 - zMax;
                newZMax = 1.0 - zMin;
                break;
            case 270:  // Rotate 270° clockwise (or 90° counter-clockwise)
                newXMin = zMin;
                newXMax = zMax;
                newZMin = 1.0 - xMax;
                newZMax = 1.0 - xMin;
                break;
            default:
                return cuboid;  // No rotation
        }

        // Note: This is a simplified rotation that doesn't handle sideTextures, sideRotations, etc.
        // For full support, we'd need to implement those transformations as well
        return new BlockDefinition.CuboidElement() {
            @Override
            public double getXMin() { return newXMin; }
            @Override
            public double getXMax() { return newXMax; }
            @Override
            public double getYMin() { return yMin; }
            @Override
            public double getYMax() { return yMax; }
            @Override
            public double getZMin() { return newZMin; }
            @Override
            public double getZMax() { return newZMax; }
            @Override
            public int[] getSideTextures() { return cuboid.getSideTextures(); }
            @Override
            public int[] getSideRotations() { return cuboid.getSideRotations(); }
            @Override
            public boolean[] getNoTint() { return cuboid.getNoTint(); }
            @Override
            public String getShape() { return cuboid.getShape(); }
        };
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
        Direction[] placementDirections = ctx.getPlacementDirections();
        Direction facing = Direction.EAST;

        for (Direction direction : placementDirections) {
            if (direction == Direction.EAST || direction == Direction.WEST ||
                direction == Direction.NORTH || direction == Direction.SOUTH) {
                facing = direction.getOpposite();
                break;
            }
        }

        BlockState state = this.getDefaultState()
            .with(FACING, facing)
            .with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);

        if (STATE != null) {
            state = state.with(STATE, STATE.defValue);
        }

        return state;
    }

    @Override
    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    /**
     * Override to add facing offset to state index.
     * Index = (stateIdx × modelsPerState) + facingOffset
     */
    @Override
    protected int getIndexFromState(BlockState state) {
        int off = super.getIndexFromState(state);  // stateIdx × modelsPerState
        return switch (state.get(FACING)) {
            case EAST  -> off;      // +0
            case SOUTH -> off + 1;  // +1
            case WEST  -> off + 2;  // +2
            case NORTH -> off + 3;  // +3
            default    -> off;
        };
    }
}
