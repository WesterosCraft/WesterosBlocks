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

public class WCRailBlock extends RailBlock {

    private final boolean allowUnsupported;

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(AbstractBlock.Settings settings, Object... params) {
            if (params.length > 0 && params[0] instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> paramMap = (Map<String, Object>) params[0];
                boolean allowUnsupported = (Boolean) paramMap.getOrDefault("allowUnsupported", false);
                
                return new WCRailBlock(settings, allowUnsupported);
            }
            
            // Fallback for legacy parameter style
            boolean allowUnsupported = params.length > 0 && params[0] instanceof Boolean ? (Boolean) params[0] : false;
            
            return new WCRailBlock(settings, allowUnsupported);
        }
    }

    public WCRailBlock(AbstractBlock.Settings settings, boolean allowUnsupported) {
        super(settings);
        this.allowUnsupported = allowUnsupported;
    }

    /**
     * Gets whether this rail allows unsupported placement.
     * 
     * @return True if unsupported placement is allowed
     */
    public boolean isAllowUnsupported() {
        return allowUnsupported;
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
}