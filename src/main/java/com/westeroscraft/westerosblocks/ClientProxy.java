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
	public CompletableFuture<Void> reload(PreparableReloadListener.PreparationBarrier pStage, ResourceManager pResourceManager, ProfilerFiller pPreparationsProfiler, ProfilerFiller pReloadProfiler, 
		Executor pBackgroundExecutor, Executor pGameExecutor) {
		
		return CompletableFuture.supplyAsync(() -> {
			WesterosBlocks.log.info("Handling resource reload");
			return null;
		}, pBackgroundExecutor).thenCompose(pStage::wait).thenAcceptAsync((p_10792_) -> {
			WesterosBlockDef.reloadColorHandler();
	    	WesterosBlocks.log.info("Handling resource reload completed");
		}, pGameExecutor);
	}
}
