package com.westerosblocks.block.custom;

import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.utils.ModProperties;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;

import com.westerosblocks.data.BlockDefinition.CuboidElement.CuboidRotation;

import java.util.List;

public class WCCuboidNEBlock extends WCCuboidBlock {
    public static final DirectionProperty FACING = DirectionProperty.of("facing", Direction.EAST, Direction.NORTH);

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(BlockDefinition definition) {
            AbstractBlock.Settings settings = definition.makeSettings().nonOpaque();
            ModProperties.StateProperty stateProperty = definition.buildStateProperty();
            boolean doToggleOnUse = definition.toggleOnUse();

            tempSTATE = stateProperty;

            return new WCCuboidNEBlock(settings, definition, doToggleOnUse);
        }
    }

    public WCCuboidNEBlock(AbstractBlock.Settings settings, BlockDefinition def, boolean doToggleOnUse) {
        super(settings, def, 2, doToggleOnUse);  // modelsPerState = 2 (EAST, NORTH)

        // Rotate cuboids for NORTH facing
        int stcnt = def.getStateCount();
        for (int stidx = 0; stidx < stcnt; stidx++) {
            int off = stidx * this.modelsPerState;

            // Rotate base cuboids by 90° for NORTH
            List<BlockDefinition.CuboidElement> baseCuboids = cuboid_by_facing[off];
            if (baseCuboids != null && !baseCuboids.isEmpty()) {
                for (BlockDefinition.CuboidElement cuboid : baseCuboids) {
                    cuboid_by_facing[off + 1].add(cuboid.rotateCuboid(CuboidRotation.ROTY90));  // NORTH
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

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
        Direction[] directions = ctx.getPlacementDirections();
        Direction dir = Direction.EAST;

        for (Direction d : directions) {
            if (d == Direction.EAST || d == Direction.WEST) {
                dir = Direction.EAST;
                break;
            }
            if (d == Direction.NORTH || d == Direction.SOUTH) {
                dir = Direction.NORTH;
                break;
            }
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
        switch (rotation) {
            case CLOCKWISE_180:
            case COUNTERCLOCKWISE_90:
            case CLOCKWISE_90:
                return state.get(FACING) == Direction.EAST
                    ? state.with(FACING, Direction.NORTH)
                    : state.with(FACING, Direction.EAST);
            default:
                return state;
        }
    }

    @Override
    protected int getIndexFromState(BlockState state) {
        int off = super.getIndexFromState(state);  // stateIdx × modelsPerState
        return state.get(FACING) == Direction.EAST ? off : off + 1;  // EAST=+0, NORTH=+1
    }
}