package com.westerosblocks.block.custom;

import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.utils.ModProperties;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.state.property.Properties;

public class WCCropBlock extends WCPlantBlock {

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(BlockDefinition definition) {
            ModProperties.StateProperty stateProperty = definition.buildStateProperty();
            AbstractBlock.Settings settings = definition.applyStateLuminance(
                    definition.makeSettings().noCollision().breakInstantly().nonOpaque(),
                    stateProperty);

            boolean layerSensitive = definition.isLayerSensitive();
            boolean toggleOnUse = definition.toggleOnUse();

            if (layerSensitive) {
                tempLAYERS = Properties.LAYERS;
            }

            if (stateProperty != null) {
                tempSTATE = stateProperty;
            }

            return new WCCropBlock(settings, definition, layerSensitive, toggleOnUse);
        }
    }

    protected WCCropBlock(AbstractBlock.Settings settings, BlockDefinition def, boolean layerSensitive, boolean toggleOnUse) {
        super(settings, def, layerSensitive, toggleOnUse);
    }
}
