package com.westerosblocks.block.custom;

import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.utils.ModProperties;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.state.property.Properties;

import java.util.List;
import java.util.Map;

public class WCCropBlock extends WCPlantBlock {

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(BlockDefinition definition) {
            AbstractBlock.Settings settings = definition.makeSettings();
            ModProperties.StateProperty stateProperty = definition.buildStateProperty();

            boolean layerSensitive = definition.isLayerSensitive();
            boolean toggleOnUse = definition.toggleOnUse();

            // Reset static fields before setting them (prevent leakage between blocks)
            tempLAYERS = null;
            tempSTATE = null;

            if (layerSensitive) {
                tempLAYERS = Properties.LAYERS;
            }

            // Set the STATE property if provided
            if (stateProperty != null) {
                tempSTATE = stateProperty;
            }

            return new WCCropBlock(settings, definition, layerSensitive, toggleOnUse);
        }

        @Override
        public Block buildBlockClass(AbstractBlock.Settings settings, Map<String, Object> parameters) {
            boolean layerSensitive = (Boolean) parameters.getOrDefault("layerSensitive", false);
            boolean toggleOnUse = (Boolean) parameters.getOrDefault("toggleOnUse", false);
            List<String> stateValues = (List<String>) parameters.get("stateValues");

            // Reset static fields before setting them (prevent leakage between blocks)
            tempLAYERS = null;
            tempSTATE = null;

            if (layerSensitive) {
                tempLAYERS = Properties.LAYERS;
            }

            // Set the STATE property if stateValues are provided (need at least 2 for a valid property)
            if (stateValues != null && stateValues.size() > 1) {
                tempSTATE = new ModProperties.StateProperty(stateValues);
            }

            return new WCCropBlock(settings, null, layerSensitive, toggleOnUse);
        }
    }

    protected WCCropBlock(AbstractBlock.Settings settings, BlockDefinition def, boolean layerSensitive, boolean toggleOnUse) {
        super(settings, def, layerSensitive, toggleOnUse);
    }
}
