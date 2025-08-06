package com.westerosblocks.block.custom;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;

public class WCLogBlock extends PillarBlock {
    public WCLogBlock(Settings settings) {
        super(settings);
    }

    public static class Factory extends BlockFactory {
        public Block buildBlockClass(AbstractBlock.Settings settings, Object... params) {
            return new WCLogBlock(settings);
        }
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(AXIS, ctx.getSide().getAxis());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AXIS);
    }

}
