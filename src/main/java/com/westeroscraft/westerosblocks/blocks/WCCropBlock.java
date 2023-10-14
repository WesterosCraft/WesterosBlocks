package com.westeroscraft.westerosblocks.blocks;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.Block;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;

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
            if ((t != null) && (t.indexOf(WesterosBlockDef.LAYER_SENSITIVE) >= 0)) {
            	tempLAYERS = BlockStateProperties.LAYERS;
            }
        	BlockBehaviour.Properties props = def.makeProperties().noCollission().instabreak();
        	return def.registerRenderType(def.registerBlock(new WCCropBlock(props, def)), false, false);
        }
    }

    protected WCCropBlock(BlockBehaviour.Properties props, WesterosBlockDef def) {
        super(props, def);
    }
    private static String[] TAGS = { "crops" };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }

}
