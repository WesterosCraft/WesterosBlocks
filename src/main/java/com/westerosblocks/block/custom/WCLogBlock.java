package com.westerosblocks.block.custom;

import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;

import java.util.Map;

public class WCLogBlock extends PillarBlock {
    protected BlockDefinition def;

    public WCLogBlock(Settings settings) {
        super(settings);
        this.def = null;
    }

    public WCLogBlock(Settings settings, BlockDefinition def) {
        super(settings);
        this.def = def;
    }

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(BlockDefinition definition) {
            AbstractBlock.Settings settings = definition.makeSettings();
            return new WCLogBlock(settings, definition);
        }

        @Override
        public Block buildBlockClass(AbstractBlock.Settings settings, Map<String, Object> parameters) {
            return new WCLogBlock(settings, null);
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

    /**
     * Gets the BlockDefinition for this block.
     * @return BlockDefinition if block was created from JSON, null if created programmatically
     */
    public BlockDefinition getDefinition() {
        return def;
    }
}
