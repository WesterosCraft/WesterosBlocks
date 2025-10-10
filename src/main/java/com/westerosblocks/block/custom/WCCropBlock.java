package com.westerosblocks.block.custom;

import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.utils.ModProperties;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.state.property.Properties;
import java.util.List;

public class WCCropBlock extends WCPlantBlock {

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(AbstractBlock.Settings settings, BlockDefinition definition) {
            boolean layerSensitive = definition != null && definition.isLayerSensitive();
            boolean toggleOnUse = definition != null && definition.toggleOnUse();
            List<String> stateValues = definition != null ? definition.getStateValues() : null;

            if (layerSensitive) {
                tempLAYERS = Properties.LAYERS;
            }

            if (stateValues != null) {
                tempSTATE = new ModProperties.StateProperty(stateValues);
            }

            return new WCCropBlock(settings, layerSensitive, toggleOnUse);
        }
    }

    protected WCCropBlock(AbstractBlock.Settings settings, boolean layerSensitive, boolean toggleOnUse) {
        super(settings, layerSensitive, toggleOnUse);
    }
}
