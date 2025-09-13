package com.westerosblocks.block.custom;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.state.property.Properties;
import java.util.Map;

public class WCCropBlock extends WCPlantBlock {
    
    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(AbstractBlock.Settings settings, Object... params) {
            AbstractBlock.Settings props = settings.noCollision().strength(0.0f);
            
            boolean layerSensitive = false;
            boolean toggleOnUse = false;
            
            if (params.length > 0 && params[0] instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> paramMap = (Map<String, Object>) params[0];
                layerSensitive = (Boolean) paramMap.getOrDefault("layerSensitive", false);
                toggleOnUse = (Boolean) paramMap.getOrDefault("toggleOnUse", false);
            } else {
                // Legacy parameter handling for backward compatibility
                layerSensitive = params.length > 0 ? (Boolean) params[0] : false;
                toggleOnUse = params.length > 1 ? (Boolean) params[1] : false;
            }
            
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
