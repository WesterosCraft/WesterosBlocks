package com.westeroscraft.westerosblocks;

import com.westeroscraft.westerosblocks.blocks.EntityWCFallingSand;
import com.westeroscraft.westerosblocks.blocks.RenderWCFallingSand;

import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends Proxy {
	public ClientProxy() {
	}
	public void initRenderRegistry() {
        RenderingRegistry.registerEntityRenderingHandler(EntityWCFallingSand.class, new RenderWCFallingSand());
	}
}
