package com.westeroscraft.westerosblocks;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import net.minecraft.world.level.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;

public class ClientProxy extends Proxy implements PreparableReloadListener {
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
	   ((ReloadableResourceManager) Minecraft.getInstance().getResourceManager()).registerReloadListener(this);
	}	
	@Override
	public CompletableFuture<Void> reload(PreparationBarrier p_10638_, ResourceManager p_10639_,
		ProfilerFiller p_10640_, ProfilerFiller p_10641_, Executor p_10642_, Executor p_10643_) {
		WesterosBlocks.log.info("Handling resource reload");
        WesterosBlockDef.reloadColorHandler();
    	WesterosBlocks.log.info("Handling resource reload completed");
    	return null;
	}
}
