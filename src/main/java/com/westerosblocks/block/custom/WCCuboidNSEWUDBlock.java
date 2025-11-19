package com.westerosblocks.block.custom;

import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.utils.ModProperties;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Waterloggable;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;

import java.util.ArrayList;
import java.util.List;

/**
 * Cuboid block with 6-directional facing (NORTH, SOUTH, EAST, WEST, UP, DOWN).
 * Supports all directional orientations.
 */
public class WCCuboidNSEWUDBlock extends WCCuboidBlock implements Waterloggable {
    public static final DirectionProperty FACING = Properties.FACING;

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(BlockDefinition definition) {
            AbstractBlock.Settings settings = definition.makeSettings();
            ModProperties.StateProperty stateProperty = definition.buildStateProperty();
            boolean doToggleOnUse = definition.toggleOnUse();

            tempSTATE = stateProperty;

            return new WCCuboidNSEWUDBlock(settings, definition, doToggleOnUse);
        }
    }

    public WCCuboidNSEWUDBlock(AbstractBlock.Settings settings, BlockDefinition def, boolean doToggleOnUse) {
        super(settings, def, 6, doToggleOnUse);  // modelsPerState = 6 (EAST, SOUTH, WEST, NORTH, UP, DOWN)

        // Rotate cuboids for each facing direction
        int stcnt = def.getStateCount();
        for (int stidx = 0; stidx < stcnt; stidx++) {
            int off = stidx * this.modelsPerState;

            // Rotate base cuboids to create 6 facing variants
            List<BlockDefinition.CuboidElement> baseCuboids = cuboid_by_facing[off];
            if (baseCuboids != null && !baseCuboids.isEmpty()) {
                for (BlockDefinition.CuboidElement cuboid : baseCuboids) {
                    cuboid_by_facing[off + 1].add(rotateCuboidY(cuboid, 90));   // SOUTH
                    cuboid_by_facing[off + 2].add(rotateCuboidY(cuboid, 180));  // WEST
                    cuboid_by_facing[off + 3].add(rotateCuboidY(cuboid, 270));  // NORTH
                    cuboid_by_facing[off + 4].add(rotateCuboidX(cuboid, 270));  // UP
                    cuboid_by_facing[off + 5].add(rotateCuboidX(cuboid, 90));   // DOWN
                }
            }
        }

        // Compute shapes from rotated cuboids
        for (int i = 0; i < cuboid_by_facing.length; i++) {
            if (SHAPE_BY_INDEX[i] == null) {
                SHAPE_BY_INDEX[i] = computeShapeFromCuboids(cuboid_by_facing[i]);
            }
        }

        BlockState defbs = this.getDefaultState()
            .with(WATERLOGGED, false)
            .with(FACING, Direction.EAST);

        if (STATE != null) {
            defbs = defbs.with(STATE, STATE.defValue);
        }
        this.setDefaultState(defbs);
    }

    /**
     * Rotates a cuboid element around the Y-axis (horizontal rotation).
     * @param cuboid Original cuboid
     * @param degrees Rotation angle (90, 180, or 270)
     * @return Rotated cuboid element
     */
    private BlockDefinition.CuboidElement rotateCuboidY(BlockDefinition.CuboidElement cuboid, int degrees) {
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

    /**
     * Rotates a cuboid element around the X-axis (vertical rotation).
     * @param cuboid Original cuboid
     * @param degrees Rotation angle (90 for DOWN, 270 for UP)
     * @return Rotated cuboid element
     */
    private BlockDefinition.CuboidElement rotateCuboidX(BlockDefinition.CuboidElement cuboid, int degrees) {
        double xMin = cuboid.getXMin();
        double xMax = cuboid.getXMax();
        double yMin = cuboid.getYMin();
        double yMax = cuboid.getYMax();
        double zMin = cuboid.getZMin();
        double zMax = cuboid.getZMax();

        double newYMin, newYMax, newZMin, newZMax;

        switch (degrees % 360) {
            case 90:  // Rotate 90° (DOWN facing)
                newYMin = zMin;
                newYMax = zMax;
                newZMin = 1.0 - yMax;
                newZMax = 1.0 - yMin;
                break;
            case 270:  // Rotate 270° (UP facing)
                newYMin = 1.0 - zMax;
                newYMax = 1.0 - zMin;
                newZMin = yMin;
                newZMax = yMax;
                break;
            default:
                return cuboid;  // No rotation
        }

        return new BlockDefinition.CuboidElement() {
            @Override
            public double getXMin() { return xMin; }
            @Override
            public double getXMax() { return xMax; }
            @Override
            public double getYMin() { return newYMin; }
            @Override
            public double getYMax() { return newYMax; }
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
        Direction dir = ctx.getPlayerLookDirection().getOpposite();

        if (ctx.getPlayer() == null) {
            dir = ctx.getSide().getOpposite();
        }

        BlockState bs = this.getDefaultState()
            .with(FACING, dir)
            .with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);

        if (STATE != null) {
            bs = bs.with(STATE, STATE.defValue);
        }
        return bs;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState,
            WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        Direction facing = state.get(FACING);
        if (facing == Direction.UP || facing == Direction.DOWN) {
            return state;
        }
        return state.with(FACING, rotation.rotate(facing));
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
            case UP    -> off + 4;  // +4
            case DOWN  -> off + 5;  // +5
        };
    }
}
