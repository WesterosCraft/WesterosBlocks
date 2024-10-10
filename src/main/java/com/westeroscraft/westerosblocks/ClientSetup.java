package com.westeroscraft.westerosblocks;

import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class ClientSetup {

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void onRegisterClientReloadListeners(RegisterClientReloadListenersEvent event) {
		event.registerReloadListener(new WesterosResourceReloadListener());
	}

	@OnlyIn(Dist.CLIENT)
	private static class WesterosResourceReloadListener implements PreparableReloadListener {
		@Override
		public CompletableFuture<Void> reload(PreparationBarrier stage, ResourceManager resourceManager,
											  ProfilerFiller preparationsProfiler, ProfilerFiller reloadProfiler,
											  Executor backgroundExecutor, Executor gameExecutor) {
			return CompletableFuture.supplyAsync(() -> {
				WesterosBlocks.log.info("Handling resource reload");
				return null;
			}, backgroundExecutor).thenCompose(stage::wait).thenAcceptAsync((p_10792_) -> {
				WesterosBlockDef.reloadColorHandler(resourceManager);
				WesterosBlocks.log.info("Handling resource reload completed");
			}, gameExecutor);
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static void initRenderRegistry() {
		ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
		WesterosBlockDef.reloadColorHandler(resourceManager);
	}
}