package com.westerosblocks.block.custom;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.ColoredFallingBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.ColorCode;
import net.minecraft.util.Formatting;

import java.util.List;

/**
 * Standalone sand block that doesn't depend on the def system.
 * Provides the same functionality as WCSandBlock but with direct configuration.
 */
public class WCSandBlock extends ColoredFallingBlock {

    private final String blockName;
    private final String creativeTab;
    private final ColorCode dustColor;
    private final List<String> tooltips;

    /**
     * Creates a new standalone sand block.
     * 
     * @param settings Block settings
     * @param blockName The name of the block
     * @param creativeTab The creative tab this block belongs to
     * @param dustColor The color of the dust particles (default: 0xDBCAA0)
     * @param tooltips Optional tooltips to display
     */
    public WCSandBlock(AbstractBlock.Settings settings, String blockName, String creativeTab,
                       ColorCode dustColor, List<String> tooltips) {
        super(dustColor, settings);
        this.blockName = blockName;
        this.creativeTab = creativeTab;
        this.dustColor = dustColor;
        this.tooltips = tooltips;
    }

    /**
     * Creates a new standalone sand block with default dust color.
     * 
     * @param settings Block settings
     * @param blockName The name of the block
     * @param creativeTab The creative tab this block belongs to
     * @param tooltips Optional tooltips to display
     */
    public WCSandBlock(AbstractBlock.Settings settings, String blockName, String creativeTab,
                       List<String> tooltips) {
        this(settings, blockName, creativeTab, new ColorCode(0xDBCAA0), tooltips);
    }

    /**
     * Creates a new standalone sand block with default dust color and no tooltips.
     * 
     * @param settings Block settings
     * @param blockName The name of the block
     * @param creativeTab The creative tab this block belongs to
     */
    public WCSandBlock(AbstractBlock.Settings settings, String blockName, String creativeTab) {
        this(settings, blockName, creativeTab, new ColorCode(0xDBCAA0), null);
    }

    /**
     * Creates a new standalone sand block with all defaults.
     * 
     * @param settings Block settings
     */
    public WCSandBlock(AbstractBlock.Settings settings) {
        this(settings, "sand", "building_blocks", new ColorCode(0xDBCAA0), null);
    }

    public String getBlockName() {
        return blockName;
    }

    public String getCreativeTab() {
        return creativeTab;
    }

    public ColorCode getDustColor() {
        return dustColor;
    }

    private static final String[] TAGS = { "sand" };

    public String[] getBlockTags() {
        return TAGS;
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
        if (tooltips != null && !tooltips.isEmpty()) {
            for (String tooltipText : tooltips) {
                tooltip.add(Text.literal(tooltipText).formatted(Formatting.GRAY));
            }
        }
        super.appendTooltip(stack, context, tooltip, options);
    }
} 