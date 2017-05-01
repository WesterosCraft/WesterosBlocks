package com.westeroscraft.westerosblocks;

/*NOTYET
import com.westeroscraft.westerosblocks.blocks.EntityWCFallingSand;
import com.westeroscraft.westerosblocks.blocks.RenderWCFallingSand;
*/

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

public class ClientProxy extends Proxy {
	public ClientProxy() {
	}
	public void initRenderRegistry() {
	    // Register entity renderers
	}
    /**
     * This function returns either the player from the handler if it's on the
     * server, or directly from the minecraft instance if it's the client.
     */
    @Override
    public EntityPlayer getPlayerFromNetHandler (INetHandler handler) {
        if (handler instanceof NetHandlerPlayServer) {
            return ((NetHandlerPlayServer) handler).player;
        } else {
            return Minecraft.getMinecraft().player;
        }
    }	
    @Override
    public void registerItemRenderer(Item item, int meta, String id) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(WesterosBlocks.MOD_ID + ":" + id, "inventory"));
    }
}
