package com.westeroscraft.westerosblocks;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import com.westeroscraft.westerosblocks.commands.PTimeCommand;
import com.westeroscraft.westerosblocks.commands.PWeatherCommand;
import com.westeroscraft.westerosblocks.commands.PWeatherCommand.PWEATHER_SETTING;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraftforge.client.resource.ISelectiveResourceReloadListener;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.client.resource.IResourceType;

public class ClientProxy extends Proxy implements ISelectiveResourceReloadListener {
	public ClientProxy() {
	}
	
	@Override
	public void initCleanupHooks() {
		MinecraftForge.EVENT_BUS.register(new Object() {
			@SubscribeEvent
			public void playerLogout(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
				WesterosBlocks.log.info("playerLogout");
				// Reset time offset to default
				PTimeCommand.setTimeOffset(true,  0);
				// Reset weather settings
				PWeatherCommand.setWeatherSetting(PWEATHER_SETTING.RESET, null);
			}
			@SubscribeEvent
			public void playerWorldChange(PlayerEvent.PlayerChangedDimensionEvent event) {
				PWeatherCommand.setWeatherSetting(PWEATHER_SETTING.RESET, null);
			}
		});
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
    public void registerItemRenderer(Item item, int meta, String name) {
        ModelLoader.registerItemVariants(item, new ResourceLocation(WesterosBlocks.MOD_ID + ":" + name));
        //Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, meta, new ModelResourceLocation(WesterosBlocks.MOD_ID + ":" + name, "inventory"));
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(WesterosBlocks.MOD_ID + ":" + name, "inventory"));
    }

    private List<WesterosBlockLifecycle> blocks_to_color = new ArrayList<WesterosBlockLifecycle>();
    @Override
    public void registerBlockModels(Block[] blocks) {
        for (Block block : blocks) {
        	if (block instanceof WesterosBlockLifecycle) {
        		WesterosBlockLifecycle blocklc = (WesterosBlockLifecycle) block;
        		IProperty<?>[] ignor = blocklc.getNonRenderingProperties();
        		if (ignor != null) {
        		    IStateMapper custom_mapper = (new StateMap.Builder()).ignore(ignor).build();
        		    ModelLoader.setCustomStateMapper(block, custom_mapper);
        		}
        		IBlockColor col = blocklc.getBlockColor();
        		if (col != null) {
        			blocks_to_color.add(blocklc);
        		}
        	}
        }
    }

    @Override
    public void registerColoring()
    {
        for (WesterosBlockLifecycle blocklc : blocks_to_color) {
    		IBlockColor col = blocklc.getBlockColor();
    		if (col != null) {
    			Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(col,(Block)blocklc);
    			Minecraft.getMinecraft().getItemColors().registerItemColorHandler(BlockColoring.BLOCK_ITEM_COLORING, (Block)blocklc);
    		}
        }
        // Add water coloring to cauldron vanilla block
        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(new IBlockColor()
        {
            public int colorMultiplier(IBlockState state, @Nullable IBlockAccess worldIn, @Nullable BlockPos pos, int tintIndex)
            {
                return worldIn != null && pos != null ? BiomeColorHelper.getWaterColorAtPos(worldIn, pos) : -1;
            }
        }, Blocks.CAULDRON);

		((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(this);
	}
	@Override
	public void onResourceManagerReload(IResourceManager resourceManager, Predicate<IResourceType> resourcePredicate) {
		WesterosBlocks.log.info("Handling resource reload");
		WesterosBlockDef.reloadColorHandler();
		WesterosBlocks.log.info("Handling resource reload completed");
		
	}
}
