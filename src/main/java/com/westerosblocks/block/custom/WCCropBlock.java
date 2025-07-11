package com.westerosblocks.block.custom;

import net.minecraft.block.AbstractBlock;

public class WCCropBlock extends WCPlantBlock {

    private static final String[] TAGS = { "crops" };

    public WCCropBlock(AbstractBlock.Settings settings, String blockName, String creativeTab,
            boolean layerSensitive, boolean toggleOnUse) {
        super(settings, blockName, creativeTab, layerSensitive, toggleOnUse);
    }

    /**
     * Get the block tags for this crop.
     * 
     * @return Array of block tags
     */
    public String[] getBlockTags() {
        return TAGS;
    }
}