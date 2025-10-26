package com.westerosblocks.block.custom;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RailBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

import java.util.List;
import java.util.Map;

import com.westerosblocks.data.BlockDefinition;

public class WCRailBlock extends RailBlock {
    protected BlockDefinition def;
    private final boolean allowUnsupported;

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(BlockDefinition definition) {
            AbstractBlock.Settings settings = definition.makeSettings();
            boolean allowUnsupported = definition.isAllowUnsupported();
            return new WCRailBlock(settings, definition, allowUnsupported);
        }

        @Override
        public Block buildBlockClass(AbstractBlock.Settings settings, Map<String, Object> parameters) {
            boolean allowUnsupported = (Boolean) parameters.getOrDefault("allowUnsupported", false);
            return new WCRailBlock(settings, null, allowUnsupported);
        }
    }

    public WCRailBlock(AbstractBlock.Settings settings, BlockDefinition def, boolean allowUnsupported) {
        super(settings);
        this.def = def;
        this.allowUnsupported = allowUnsupported;
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        if (this.allowUnsupported) return true;
        return super.canPlaceAt(state, world, pos);
    }

    @Override
    protected void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos,
                                  boolean notify) {
        if (!this.allowUnsupported) {
            super.neighborUpdate(state, world, pos, sourceBlock, sourcePos, notify);
        } else if (!world.isClient && world.getBlockState(pos).isOf(this)) {
            this.updateBlockState(state, world, pos, sourceBlock);
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
        super.appendTooltip(stack, context, tooltip, options);
    }

    /**
     * Gets the BlockDefinition for this block.
     * @return BlockDefinition if block was created from JSON, null if created programmatically
     */
    public BlockDefinition getDefinition() {
        return def;
    }
}