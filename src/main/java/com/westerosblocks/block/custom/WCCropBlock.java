package com.westerosblocks.block.custom;

import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.WesterosBlockFactory;
import com.westerosblocks.block.WesterosBlockLifecycle;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.state.property.Properties;

public class WCCropBlock extends WCPlantBlock implements WesterosBlockLifecycle {
    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
        	// See if we have a state property
        	WesterosBlockDef.StateProperty state = def.buildStateProperty();
        	if (state != null) {
        		tempSTATE = state;
        	}        	
            String t = def.getType();
            if ((t != null) && (t.contains(WesterosBlockDef.LAYER_SENSITIVE))) {
            	tempLAYERS = Properties.LAYERS;
            }
            AbstractBlock.Settings settings = def.makeProperties().noCollision().breakInstantly();
            Block blk = new WCCropBlock(settings, def);
            return def.registerRenderType(blk, false, false);
        }
    }

    protected WCCropBlock(AbstractBlock.Settings settings, WesterosBlockDef def) {
        super(settings, def);
    }
    private static String[] TAGS = { "crops" };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }

}
