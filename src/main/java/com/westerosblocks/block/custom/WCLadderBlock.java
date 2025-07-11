package com.westerosblocks.block.custom;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.LadderBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;

import java.util.List;

public class WCLadderBlock extends LadderBlock {

    private final String blockName;
    private final String creativeTab;
    private final boolean allowUnsupported;
    private final boolean noClimb;
    private final List<String> tooltips;


    /**
     * Creates a new StandaloneWCLadderBlock with full configuration.
     * 
     * @param settings The block settings
     * @param blockName The block name
     * @param creativeTab The creative tab
     * @param allowUnsupported Whether this ladder can be placed without support
     * @param noClimb Whether this ladder is not climbable
     * @param tooltips Optional tooltips to display
     */
    public WCLadderBlock(AbstractBlock.Settings settings, String blockName, String creativeTab,
                         boolean allowUnsupported, boolean noClimb, List<String> tooltips) {
        super(settings);
        this.blockName = blockName;
        this.creativeTab = creativeTab;
        this.allowUnsupported = allowUnsupported;
        this.noClimb = noClimb;
        this.tooltips = tooltips;
    }

    /**
     * Gets the block name.
     * 
     * @return the block name
     */
    public String getBlockName() {
        return blockName;
    }

    /**
     * Gets the creative tab.
     * 
     * @return the creative tab
     */
    public String getCreativeTab() {
        return creativeTab;
    }

    /**
     * Gets whether this ladder allows unsupported placement.
     * 
     * @return true if unsupported placement is allowed
     */
    public boolean allowsUnsupported() {
        return allowUnsupported;
    }

    /**
     * Gets whether this ladder is not climbable.
     * 
     * @return true if the ladder is not climbable
     */
    public boolean isNoClimb() {
        return noClimb;
    }

    @Override
    public VoxelShape getSidesShape(BlockState state, BlockView world, BlockPos pos) {
        return VoxelShapes.empty();
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return allowUnsupported || super.canPlaceAt(state, world, pos);
    }

    /**
     * Gets the block tags for this ladder.
     * 
     * @return array of block tags
     */
    public String[] getBlockTags() {
        if (noClimb) {
            return new String[]{};
        }
        return new String[]{"climbable"};
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
        // Add custom tooltips if provided
        if (tooltips != null && !tooltips.isEmpty()) {
            for (String tooltipText : tooltips) {
                tooltip.add(Text.literal(tooltipText));
            }
        }
        
        // Add default tooltip
        tooltip.add(Text.translatable("tooltip.westerosblocks.ladder." + blockName)
            .formatted(net.minecraft.util.Formatting.GRAY));
        
        super.appendTooltip(stack, context, tooltip, options);
    }
} 