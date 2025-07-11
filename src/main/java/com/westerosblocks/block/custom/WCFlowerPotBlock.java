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
    public WCFlowerPotBlock(Block content, AbstractBlock.Settings settings) {
        super(content, settings);
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