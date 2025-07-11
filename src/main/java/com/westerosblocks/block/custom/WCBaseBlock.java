package com.westerosblocks.block.custom;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;

import java.util.List;

/**
 * Base class for all WesterosBlocks custom blocks.
 * 
 * <p>This class provides common functionality shared across all custom blocks:
 * <ul>
 *   <li>Translation key management</li>
 *   <li>Tooltip handling</li>
 *   <li>Creative tab information</li>
 *   <li>Block name and metadata</li>
 * </ul>
 * 
 * <p>All custom blocks should extend this class to ensure consistent behavior
 * and reduce code duplication.
 */
public abstract class WCBaseBlock extends Block {

    /** The translation key for this block */
    protected final String translationKey;
    
    /** Optional tooltips to display for this block */
    protected final List<String> tooltips;
    
    /** The creative tab where this block is placed */
    protected final String creativeTab;
    
    /** The block name (used for registry and identification) */
    protected final String blockName;

    /**
     * Creates a new base block with the specified properties.
     * 
     * @param settings The block settings
     * @param blockName The block name (used for registry)
     * @param creativeTab The creative tab identifier
     * @param translationKey The translation key for this block
     * @param tooltips Optional tooltips to display
     */
    protected WCBaseBlock(Settings settings, String blockName, String creativeTab, 
                         String translationKey, List<String> tooltips) {
        super(settings);
        this.blockName = blockName;
        this.creativeTab = creativeTab;
        this.translationKey = translationKey;
        this.tooltips = tooltips;
    }

    /**
     * Creates a new base block with default translation key.
     * 
     * @param settings The block settings
     * @param blockName The block name (used for registry)
     * @param creativeTab The creative tab identifier
     * @param tooltips Optional tooltips to display
     */
    protected WCBaseBlock(Settings settings, String blockName, String creativeTab, List<String> tooltips) {
        this(settings, blockName, creativeTab, "block.westerosblocks." + blockName, tooltips);
    }

    /**
     * Creates a new base block with minimal configuration.
     * 
     * @param settings The block settings
     * @param blockName The block name (used for registry)
     * @param creativeTab The creative tab identifier
     */
    protected WCBaseBlock(Settings settings, String blockName, String creativeTab) {
        this(settings, blockName, creativeTab, null);
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
     * Gets the creative tab identifier.
     * 
     * @return the creative tab identifier
     */
    public String getCreativeTab() {
        return creativeTab;
    }

    /**
     * Gets the translation key for this block.
     * 
     * @return the translation key
     */
    @Override
    public String getTranslationKey() {
        return translationKey;
    }

    /**
     * Adds tooltips to the block's item.
     * 
     * <p>This method adds any custom tooltips specified during construction,
     * then calls the superclass implementation to add any additional tooltips.
     * 
     * @param stack The item stack
     * @param context The tooltip context
     * @param tooltip The tooltip list to add to
     * @param options The tooltip type options
     */
    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
        if (tooltips != null && !tooltips.isEmpty()) {
            for (String tooltipText : tooltips) {
                tooltip.add(Text.literal(tooltipText));
            }
            super.appendTooltip(stack, context, tooltip, options);
        }
    }

    /**
     * Gets the block tags for this block.
     * 
     * <p>Override this method in subclasses to provide specific block tags.
     * The default implementation returns an empty array.
     * 
     * @return Array of block tag names
     */
    public String[] getBlockTags() {
        return new String[0];
    }

    /**
     * Checks if this block has custom tooltips.
     * 
     * @return true if this block has custom tooltips
     */
    public boolean hasCustomTooltips() {
        return tooltips != null && !tooltips.isEmpty();
    }

    /**
     * Gets the custom tooltips for this block.
     * 
     * @return the custom tooltips, or null if none
     */
    public List<String> getCustomTooltips() {
        return tooltips;
    }
} 