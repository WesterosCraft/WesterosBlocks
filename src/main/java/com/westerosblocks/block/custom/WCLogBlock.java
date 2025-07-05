package com.westerosblocks.block.custom;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.state.StateManager;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class WCLogBlock extends PillarBlock {
    private final String blockName;
    private final String creativeTab;
    private final String woodType;
    private final String[] texturePaths;

    public WCLogBlock(AbstractBlock.Settings settings) {
        this(settings, "log", "building_blocks", "oak", null);
    }

    public WCLogBlock(AbstractBlock.Settings settings, String blockName, String creativeTab) {
        this(settings, blockName, creativeTab, "oak", null);
    }

    public WCLogBlock(AbstractBlock.Settings settings, String blockName, String creativeTab, String woodType) {
        this(settings, blockName, creativeTab, woodType, null);
    }

    public WCLogBlock(AbstractBlock.Settings settings, String blockName, String creativeTab, String woodType, String[] texturePaths) {
        super(settings);
        this.blockName = blockName;
        this.creativeTab = creativeTab;
        this.woodType = woodType;
        this.texturePaths = texturePaths;
    }

    public String getBlockName() {
        return blockName;
    }

    public String getCreativeTab() {
        return creativeTab;
    }

    public String getWoodType() {
        return woodType;
    }

    public String[] getTexturePaths() {
        return texturePaths;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(AXIS, ctx.getSide().getAxis());
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
        // Add custom tooltip if needed
        tooltip.add(Text.translatable("tooltip.westerosblocks.log." + woodType)
            .formatted(Formatting.GRAY));
        super.appendTooltip(stack, context, tooltip, options);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AXIS);
    }
} 