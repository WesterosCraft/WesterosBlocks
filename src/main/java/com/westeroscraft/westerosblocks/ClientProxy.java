package com.westeroscraft.westerosblocks;

import net.minecraft.block.Block;

public class ClientProxy extends Proxy {
	public ClientProxy() {
	}
	@Override	
	public void initRenderRegistry() {
       for (Block blk : WesterosBlocks.customBlocks) {
    	   if (blk instanceof WesterosBlockLifecycle) {
    		   WesterosBlockDef def = ((WesterosBlockLifecycle)blk).getWBDefinition();
    		   if (def != null) {
    			   def.registerRender(blk);
    		   }
    	   }
       }
       if (WesterosBlocks.colorMaps != null) {
    	   WesterosBlocks.log.info("Initializing " + WesterosBlocks.colorMaps.length + " custom color maps");
    	   for (WesterosBlockColorMap map : WesterosBlocks.colorMaps) {
    		   for (String bn : map.blockNames) {
    			   Block blk = WesterosBlocks.findBlockByName(bn);
    			   if (blk != null) {
    				   WesterosBlockDef.registerVanillaColorMap(bn, blk, map.colorMult);
    			   }
    		   }
    	   }
       }
	}	
}
