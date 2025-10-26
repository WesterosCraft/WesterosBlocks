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

import com.westerosblocks.data.BlockDefinition;
import java.util.Map;

public class WCPaneBlock extends PaneBlock {
    protected BlockDefinition def;
    public static final BooleanProperty UNCONNECT = BooleanProperty.of("unconnect");
    protected static BooleanProperty tempUNCONNECT;

    private boolean legacy_model;
    private boolean bars_model;
    public final boolean unconnect;

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(BlockDefinition definition) {
            AbstractBlock.Settings settings = definition.makeSettings();
            boolean doUnconnect = definition.isUnconnect();
            boolean legacy_model = definition.isLegacyModel();
            boolean bars_model = definition.isBarsModel();

            if (doUnconnect) {
                tempUNCONNECT = UNCONNECT;
            }

            return new WCPaneBlock(settings, definition, doUnconnect, legacy_model, bars_model);
        }

        @Override
        public Block buildBlockClass(AbstractBlock.Settings settings, Map<String, Object> parameters) {
            boolean doUnconnect = (Boolean) parameters.getOrDefault("unconnect", false);
            boolean legacy_model = (Boolean) parameters.getOrDefault("legacyModel", false);
            boolean bars_model = (Boolean) parameters.getOrDefault("barsModel", false);

            if (doUnconnect) {
                tempUNCONNECT = UNCONNECT;
            }

            return new WCPaneBlock(settings, null, doUnconnect, legacy_model, bars_model);
        }
    }

    protected WCPaneBlock(AbstractBlock.Settings settings, BlockDefinition def, boolean doUnconnect, boolean legacy_model, boolean bars_model) {
        super(settings);
        this.def = def;
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

    /**
     * Gets the BlockDefinition for this block.
     * @return BlockDefinition if block was created from JSON, null if created programmatically
     */
    public BlockDefinition getDefinition() {
        return def;
    }
}
