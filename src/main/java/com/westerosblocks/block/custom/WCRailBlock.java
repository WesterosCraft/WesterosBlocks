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

public class WCRailBlock extends RailBlock {

    private final String blockName;
    private final String creativeTab;
    private final boolean allowUnsupported;
    private final List<String> tooltips;

    public WCRailBlock(AbstractBlock.Settings settings, String blockName, String creativeTab, 
                      boolean allowUnsupported, List<String> tooltips) {
        super(settings);
        this.blockName = blockName;
        this.creativeTab = creativeTab;
        this.allowUnsupported = allowUnsupported;
        this.tooltips = tooltips;
    }

    /**
     * Gets the block name.
     * 
     * @return The block name
     */
    public String getBlockName() {
        return blockName;
    }

    /**
     * Gets the creative tab.
     * 
     * @return The creative tab
     */
    public String getCreativeTab() {
        return creativeTab;
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
        // Add custom tooltips if provided
        if (tooltips != null) {
            for (String tooltipText : tooltips) {
                tooltip.add(Text.translatable(tooltipText));
            }
        }
        super.appendTooltip(stack, context, tooltip, options);
    }
}
