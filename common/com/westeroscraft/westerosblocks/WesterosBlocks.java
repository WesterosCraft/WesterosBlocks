package com.westeroscraft.westerosblocks;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.Mod.ServerStarted;
import cpw.mods.fml.common.Mod.ServerStarting;
import cpw.mods.fml.common.Mod.ServerStopping;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent; 
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraft.block.Block;
import java.util.logging.Level;
import com.westeroscraft.westerosblocks.blocks.Block1;
import com.westeroscraft.westerosblocks.blocks.ItemBlock1;
import net.minecraft.item.ItemStack;

@Mod(modid = "WesterosBlocks", name = "WesterosBlocks", version = Version.VER)
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class WesterosBlocks
{
    // The instance of your mod that Forge uses.
    @Instance("WesterosBlocks")
    public static WesterosBlocks instance;

    // Says where the client and server 'proxy' code is loaded.
    @SidedProxy(clientSide = "com.westeroscraft.westerosblocks.ClientProxy", serverSide = "com.westeroscraft.westerosblocks.Proxy")
    public static Proxy proxy;

    // Configuration variables (mostly block ids)
    public static int block1ID;
    
    // Block classes
    public static Block block1;    
    
    
    @PreInit
    public void preInit(FMLPreInitializationEvent event)
    {
        // Load configuration file - use suggested (config/WesterosBlocks.cfg)
        Configuration cfg = new Configuration(event.getSuggestedConfigurationFile());
        try
        {
            cfg.load();
            block1ID = cfg.getBlock("block1", 4000).getInt(4000);
        }
        catch (Exception e)
        {
            FMLLog.log(Level.SEVERE, e, "WesterosBlocks couldn't load its configuration", new Object[0]);
        }
        finally
        {
            cfg.save();
        }
    }

    @Init
    public void load(FMLInitializationEvent event)
    {
        block1 = new Block1(block1ID, 5).setUnlocalizedName("block1");
        
        MinecraftForge.setBlockHarvestLevel(block1, "pickaxe", 1);
        GameRegistry.registerBlock(block1, ItemBlock1.class, "block1");

        LanguageRegistry.addName(new ItemStack((Block)block1, 1, 0), "Iron 1");
        LanguageRegistry.addName(new ItemStack((Block)block1, 1, 1), "Iron 2");
        LanguageRegistry.addName(new ItemStack((Block)block1, 1, 2), "Iron 3");
        LanguageRegistry.addName(new ItemStack((Block)block1, 1, 3), "Iron 4");
        LanguageRegistry.addName(new ItemStack((Block)block1, 1, 4), "Iron 5");
    }

    @PostInit
    public void postInit(FMLPostInitializationEvent event)
    {
    }

    @ServerStarting
    public void serverStarting(FMLServerStartingEvent event) {
    }
    
    @ServerStarted
    public void serverStarted(FMLServerStartedEvent event)
    {
    }
    @ServerStopping
    public void serverStopping(FMLServerStoppingEvent event)
    {
    }
}
