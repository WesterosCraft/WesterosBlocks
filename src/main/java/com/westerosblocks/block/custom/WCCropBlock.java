package com.westerosblocks.block.custom;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.state.property.Properties;

public class WCCropBlock extends WCPlantBlock {
    
    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(AbstractBlock.Settings settings, Object... params) {
            AbstractBlock.Settings props = settings.noCollision().strength(0.0f);
            boolean layerSensitive = params.length > 0 ? (Boolean) params[0] : false;
            boolean toggleOnUse = params.length > 1 ? (Boolean) params[1] : false;
            if (layerSensitive) {
                tempLAYERS = Properties.LAYERS;
            }
            return new WCCropBlock(props, layerSensitive, toggleOnUse);
        }
    }

    protected WCCropBlock(AbstractBlock.Settings settings, boolean layerSensitive, boolean toggleOnUse) {
        super(settings, layerSensitive, toggleOnUse);
    }
}
