package com.westerosblocks.block.custom;

import net.minecraft.block.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;

import java.util.List;

/**
 * Standalone flower pot block that doesn't depend on ModBlock functionality.
 * This class provides the same core functionality as WCFlowerPotBlock but
 * without the definition-based system dependencies.
 */
public class WCFlowerPotBlock extends FlowerPotBlock {

    /**
     * Creates a flower pot block with the specified content block.
     * 
     * @param content The block that will be placed inside the flower pot
     * @param settings The block settings
     */
    public WCFlowerPotBlock(Block content, AbstractBlock.Settings settings) {
        super(content, settings);
    }

    /**
     * Creates an empty flower pot block.
     * 
     * @param settings The block settings
     */
    public WCFlowerPotBlock(AbstractBlock.Settings settings) {
        super(Blocks.AIR, settings);
    }

    /**
     * Override this method to add custom tooltips to the flower pot.
     * The default implementation does nothing.
     * 
     * @param tooltip The tooltip list to add to
     */
    protected void addCustomTooltip(List<Text> tooltip) {
        // Override in subclasses to add custom tooltips
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
        addCustomTooltip(tooltip);
        super.appendTooltip(stack, context, tooltip, options);
    }

    /**
     * Get the block tags for this flower pot block.
     * Override this method to provide custom tags.
     * 
     * @return Array of block tag names
     */
    public String[] getBlockTags() {
        return new String[]{"flower_pots"};
    }
} 