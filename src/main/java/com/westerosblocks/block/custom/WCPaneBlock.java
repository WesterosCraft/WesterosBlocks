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

import java.util.Map;

public class WCPaneBlock extends PaneBlock {
    public static final BooleanProperty UNCONNECT = BooleanProperty.of("unconnect");
    protected static BooleanProperty tempUNCONNECT;

    private boolean legacy_model;
    private boolean bars_model;
    public final boolean unconnect;

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(AbstractBlock.Settings settings, Object... params) {
            if (params.length > 0 && params[0] instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> paramMap = (Map<String, Object>) params[0];
                boolean doUnconnect = (Boolean) paramMap.getOrDefault("unconnect", false);
                boolean legacy_model = (Boolean) paramMap.getOrDefault("legacyModel", false);
                boolean bars_model = (Boolean) paramMap.getOrDefault("barsModel", false);
                
                if (doUnconnect) {
                    tempUNCONNECT = UNCONNECT;
                }
                
                return new WCPaneBlock(settings, doUnconnect, legacy_model, bars_model);
            }
            
            // Fallback for legacy string parameter style
            boolean doUnconnect = false;
            boolean legacy_model = false;
            boolean bars_model = false;
            
            if (params.length > 0 && params[0] instanceof String) {
                String type = (String) params[0];
                String[] toks = type.split(",");
                for (String tok : toks) {
                    String[] parts = tok.split(":");
                    if (parts[0].equals("unconnect")) {
                        doUnconnect = true;
                        tempUNCONNECT = UNCONNECT;
                    } else if (tok.equals("legacy-model")) {
                        legacy_model = true;
                    } else if (tok.equals("bars-model")) {
                        bars_model = true;
                    }
                }
            }
            
            return new WCPaneBlock(settings, doUnconnect, legacy_model, bars_model);
        }
    }

    protected WCPaneBlock(AbstractBlock.Settings settings, boolean doUnconnect, boolean legacy_model, boolean bars_model) {
        super(settings);
        this.unconnect = doUnconnect;
        this.legacy_model = legacy_model;
        this.bars_model = bars_model;
        
        if (doUnconnect) {
            this.setDefaultState(this.getDefaultState()
                    .with(NORTH, Boolean.FALSE)
                    .with(EAST, Boolean.FALSE)
                    .with(SOUTH, Boolean.FALSE)
                    .with(WEST, Boolean.FALSE)
                    .with(WATERLOGGED, Boolean.FALSE)
                    .with(UNCONNECT, Boolean.FALSE));
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        if (tempUNCONNECT != null) {
            builder.add(tempUNCONNECT);
            tempUNCONNECT = null;
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

    public boolean isLegacyModel() {
        return legacy_model;
    }

    public boolean isBarsModel() {
        return bars_model;
    }
}
