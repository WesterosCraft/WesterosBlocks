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
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

import java.util.List;

public class WCBranchBlock extends PillarBlock {
    private final String blockName;
    private final String creativeTab;
    private final String woodType;
    private final String branchType;

    public WCBranchBlock(AbstractBlock.Settings settings, String blockName, String creativeTab, String woodType,
            String branchType) {
        super(settings);
        this.blockName = blockName;
        this.creativeTab = creativeTab;
        this.woodType = woodType;
        this.branchType = branchType;
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

    public String getBranchType() {
        return branchType;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(AXIS, ctx.getSide().getAxis());
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
        // Add custom tooltip if needed
        tooltip.add(Text.translatable("tooltip.westerosblocks.branch." + woodType + "." + branchType)
                .formatted(Formatting.GRAY));
        super.appendTooltip(stack, context, tooltip, options);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AXIS);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        // Based on the model: from [4, 0, 4] to [12, 16, 12] with rotation around Y
        // axis at [11, 7, 11]
        // Convert from 16x16 texture coordinates to 16x16 block coordinates
        // The model shows a rotated cube that's offset from center
        return VoxelShapes.cuboid(0.25, 0.0, 0.25, 0.75, 1.0, 0.75);
    }
}