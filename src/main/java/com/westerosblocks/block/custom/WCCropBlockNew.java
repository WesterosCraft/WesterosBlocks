package com.westerosblocks.block.custom;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;

import java.util.List;

/**
 * Crop block that extends the refactored WCPlantBlock.
 * Provides crop-specific functionality while maintaining compatibility with the new plant block system.
 * 
 * <p>This class is designed to work with the builder-based registration system in ModBlocks2.
 * It provides the same functionality as the original WCCropBlock but without the definition system dependency.
 */
public class WCCropBlockNew extends WCPlantBlock {
    
    private static final String[] TAGS = {"crops"};

    /**
     * Creates a new crop block with default settings.
     * 
     * @param settings The block settings
     */
    public WCCropBlockNew(AbstractBlock.Settings settings) {
        super(settings, "crop", "westeros_crops_tab", false, false);
    }

    /**
     * Creates a new crop block with custom configuration.
     * 
     * @param settings The block settings
     * @param blockName The block name
     * @param creativeTab The creative tab
     * @param layerSensitive Whether this crop is sensitive to layer placement
     * @param toggleOnUse Whether this crop can be toggled in creative mode
     */
    public WCCropBlockNew(AbstractBlock.Settings settings, String blockName, String creativeTab, 
                         boolean layerSensitive, boolean toggleOnUse) {
        super(settings, blockName, creativeTab, layerSensitive, toggleOnUse);
    }

    /**
     * Creates a new crop block with basic configuration.
     * 
     * @param settings The block settings
     * @param blockName The block name
     * @param creativeTab The creative tab
     */
    public WCCropBlockNew(AbstractBlock.Settings settings, String blockName, String creativeTab) {
        super(settings, blockName, creativeTab, false, false);
    }

    /**
     * Get the block tags for this crop.
     * 
     * @return Array of block tags
     */
    public String[] getBlockTags() {
        return TAGS;
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
        // Add custom crop tooltip
        tooltip.add(Text.translatable("tooltip.westerosblocks.crop." + getBlockName())
            .formatted(net.minecraft.util.Formatting.GREEN));
        super.appendTooltip(stack, context, tooltip, options);
    }
} 