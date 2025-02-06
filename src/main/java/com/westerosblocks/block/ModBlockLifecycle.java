package com.westerosblocks.block;

// Used to define the interfaces for properly priming one of our custom block definitions
// must also have constructor with WesterosBlock as parameter

import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.List;

public interface ModBlockLifecycle {
    /**
     * Get definition for block
     */
    ModBlock getWBDefinition();

    /**
     * Get block tags for block
     */
    String[] getBlockTags();

    default void addCustomTooltip(List<Text> tooltip) {
        ModBlock def = getWBDefinition();
        if (def.tooltips != null && !def.tooltips.isEmpty()) {
            for (String tooltipText : def.tooltips) {
                tooltip.add(Text.literal(tooltipText));
            }
        }
    }
}
