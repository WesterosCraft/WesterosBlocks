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
		ReloadableResourceManager rrm = (ReloadableResourceManager) Minecraft.getInstance().getResourceManager();
		WesterosBlockDef.reloadColorHandler(rrm);
		rrm.registerReloadListener(this);	   
	}	
	@Override
	public CompletableFuture<Void> reload(PreparableReloadListener.PreparationBarrier pStage, ResourceManager pResourceManager, ProfilerFiller pPreparationsProfiler, ProfilerFiller pReloadProfiler, 
		Executor pBackgroundExecutor, Executor pGameExecutor) {
		
		return CompletableFuture.supplyAsync(() -> {
			WesterosBlocks.log.info("Handling resource reload");
			return null;
		}, pBackgroundExecutor).thenCompose(pStage::wait).thenAcceptAsync((p_10792_) -> {
			WesterosBlockDef.reloadColorHandler(pResourceManager);
	    	WesterosBlocks.log.info("Handling resource reload completed");
		}, pGameExecutor);
	}
}
