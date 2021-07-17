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
	}	
}
