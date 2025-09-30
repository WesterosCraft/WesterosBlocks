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
            AbstractBlock.Settings props = settings.noCollision().strength(0.0f);

            // Handle null definition (from BlockBuilder) with sensible defaults
            boolean layerSensitive = definition != null && definition.isLayerSensitive();
            boolean toggleOnUse = definition != null && definition.toggleOnUse();
            List<String> stateValues = definition != null ? definition.getStateValues() : null;

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
