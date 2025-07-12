package com.westerosblocks.block.custom;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PaneBlock;
import net.minecraft.fluid.Fluids;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;

public class WCStandalonePaneBlock extends PaneBlock {
    public static final BooleanProperty UNCONNECT = BooleanProperty.of("unconnect");
    private final boolean unconnect;

    public WCStandalonePaneBlock(AbstractBlock.Settings settings) {
        this(settings, false);
    }

    public WCStandalonePaneBlock(AbstractBlock.Settings settings,  boolean unconnect) {
        super(settings);
        this.unconnect = unconnect;
        
        if (unconnect) {
            this.setDefaultState(this.getDefaultState()
                    .with(NORTH, Boolean.FALSE)
                    .with(EAST, Boolean.FALSE)
                    .with(SOUTH, Boolean.FALSE)
                    .with(WEST, Boolean.FALSE)
                    .with(WATERLOGGED, Boolean.FALSE)
                    .with(UNCONNECT, Boolean.FALSE));
        }
    }

    public boolean isUnconnect() {
        return unconnect;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        if (unconnect) {
            builder.add(UNCONNECT);
        }
        super.appendProperties(builder);
    }

    @Override
    public BlockState getStateForNeighborUpdate(
            BlockState state,
            Direction direction,
            BlockState neighborState,
            WorldAccess world,
            BlockPos pos,
            BlockPos neighborPos
    ) {
        if (unconnect && state.get(UNCONNECT)) {
            if (state.get(WATERLOGGED)) {
                world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
            }
            return state;
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }
} 