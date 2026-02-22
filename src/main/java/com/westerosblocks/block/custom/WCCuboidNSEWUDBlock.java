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
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;

import com.westerosblocks.data.BlockDefinition.CuboidElement.CuboidRotation;

import java.util.List;

/**
 * Cuboid block with 6-directional facing (NORTH, SOUTH, EAST, WEST, UP, DOWN).
 * Supports all directional orientations.
 */
public class WCCuboidNSEWUDBlock extends WCCuboidBlock {
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
                    cuboid_by_facing[off + 1].add(cuboid.rotateCuboid(CuboidRotation.ROTY90));   // SOUTH
                    cuboid_by_facing[off + 2].add(cuboid.rotateCuboid(CuboidRotation.ROTY180));  // WEST
                    cuboid_by_facing[off + 3].add(cuboid.rotateCuboid(CuboidRotation.ROTY270));  // NORTH
                    cuboid_by_facing[off + 4].add(cuboid.rotateCuboid(CuboidRotation.ROTX270));  // UP
                    cuboid_by_facing[off + 5].add(cuboid.rotateCuboid(CuboidRotation.ROTX90));   // DOWN
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
