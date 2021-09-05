package com.westeroscraft.westerosblocks;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Predicate;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.IFutureReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.fml.ModWorkManager;
import net.minecraftforge.resource.IResourceType;
import net.minecraftforge.resource.ISelectiveResourceReloadListener;
import net.minecraftforge.resource.VanillaResourceType;

public class ClientProxy extends Proxy implements ISelectiveResourceReloadListener {
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
	   ((IReloadableResourceManager) Minecraft.getInstance().getResourceManager()).registerReloadListener(this);
	}	
	@Override
	public void onResourceManagerReload(IResourceManager resourceManager, Predicate<IResourceType> resourcePredicate) {
    	WesterosBlocks.log.info("Handling resource reload");
        WesterosBlockDef.reloadColorHandler();
    	WesterosBlocks.log.info("Handling resource reload completed");
	}
    @Override
	public IResourceType getResourceType()
    {
        return VanillaResourceType.TEXTURES;
    }
}
