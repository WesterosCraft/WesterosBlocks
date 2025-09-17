package com.westerosblocks.block.custom;

import com.westerosblocks.utils.ModProperties;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.state.property.Properties;
import java.util.List;
import java.util.Map;

public class WCCropBlock extends WCPlantBlock {
    
    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(AbstractBlock.Settings settings, Object... params) {
            AbstractBlock.Settings props = settings.noCollision().strength(0.0f);
            
            boolean layerSensitive = false;
            boolean toggleOnUse = false;
            List<String> stateValues = null;
            
            if (params.length > 0 && params[0] instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> paramMap = (Map<String, Object>) params[0];
                layerSensitive = (Boolean) paramMap.getOrDefault("layerSensitive", false);
                toggleOnUse = (Boolean) paramMap.getOrDefault("toggleOnUse", false);
                
                // Handle state property
                @SuppressWarnings("unchecked")
                List<String> stateValuesParam = (List<String>) paramMap.get("stateValues");
                if (stateValuesParam != null && !stateValuesParam.isEmpty()) {
                    stateValues = stateValuesParam;
                }
            } else {
                // Legacy parameter handling for backward compatibility
                layerSensitive = params.length > 0 ? (Boolean) params[0] : false;
                toggleOnUse = params.length > 1 ? (Boolean) params[1] : false;
            }
            
            if (layerSensitive) {
                tempLAYERS = Properties.LAYERS;
            }
            
            // Set the STATE property if stateValues are provided
            if (stateValues != null) {
                tempSTATE = new ModProperties.StateProperty(stateValues);
            }
            
            return new WCCropBlock(props, layerSensitive, toggleOnUse);
        }
    }

    protected WCCropBlock(AbstractBlock.Settings settings, boolean layerSensitive, boolean toggleOnUse) {
        super(settings, layerSensitive, toggleOnUse);
    }
}
