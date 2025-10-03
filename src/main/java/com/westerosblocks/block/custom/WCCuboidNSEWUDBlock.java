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
        public Block buildBlockClass(AbstractBlock.Settings settings, BlockDefinition definition) {
            // Handle toggleOnUse and states
            boolean doToggleOnUse = definition != null && definition.toggleOnUse();
            int numStates = definition != null ? definition.getStateCount() : 0;
            boolean doAddStates = numStates > 0;

            // Set the STATE property if stateValues are provided
            if (doAddStates) {
                List<String> stateValues = definition != null ? definition.getStateValues() : null;
                if (stateValues != null && !stateValues.isEmpty()) {
                    tempSTATE = new ModProperties.StateProperty(stateValues);
                } else {
                    // Generate default state IDs if not provided
                    ArrayList<String> stateIds = new ArrayList<>();
                    for (int i = 0; i < numStates; i++) {
                        stateIds.add("state" + i);
                    }
                    tempSTATE = new ModProperties.StateProperty(stateIds);
                }
            }

            return new WCCuboidNSEWUDBlock(settings, doToggleOnUse, doAddStates, definition);
        }
    }

    public WCCuboidNSEWUDBlock(AbstractBlock.Settings settings) {
        this(settings, false, false, null);
    }

    public WCCuboidNSEWUDBlock(AbstractBlock.Settings settings, boolean doToggleOnUse, boolean addStates, BlockDefinition definition) {
        super(settings, doToggleOnUse, addStates, null);

        BlockState defbs = this.getDefaultState()
            .with(WATERLOGGED, false)
            .with(FACING, Direction.EAST);

        if (addStates && tempSTATE != null) {
            defbs = defbs.with(tempSTATE, tempSTATE.defValue);
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

        // Get the direction based on which side of a block the player clicked
        Direction dir = ctx.getSide().getOpposite();

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

        // Don't rotate UP and DOWN directions
        if (facing == Direction.UP || facing == Direction.DOWN) {
            return state;
        }

        // Rotate horizontal directions
        return state.with(FACING, rotation.rotate(facing));
    }
}
