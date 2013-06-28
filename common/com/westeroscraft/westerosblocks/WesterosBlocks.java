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
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockStairs;

import java.util.logging.Level;
import com.westeroscraft.westerosblocks.blocks.BlockIron;
import com.westeroscraft.westerosblocks.blocks.BlockIronStairs;
import com.westeroscraft.westerosblocks.blocks.ItemBlockIron;
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
    public static int blockIronID;
    public static int blockIronStairs0ID;
    public static int blockIronStairs1ID;
    public static int blockIronStairs2ID;
    public static int blockIronStairs3ID;
    public static int blockIronStairs4ID;
    
    // Block classes
    public static Block blockIron;    
    public static Block blockIronStairs0;
    public static Block blockIronStairs1;
    public static Block blockIronStairs2;
    public static Block blockIronStairs3;
    public static Block blockIronStairs4;
    
    @PreInit
    public void preInit(FMLPreInitializationEvent event)
    {
        // Load configuration file - use suggested (config/WesterosBlocks.cfg)
        Configuration cfg = new Configuration(event.getSuggestedConfigurationFile());
        try
        {
            cfg.load();
            blockIronID = cfg.getBlock("blockIron", 4000).getInt(4000);
            blockIronStairs0ID = cfg.getBlock("blockIronStairs0", 4001).getInt(4001);
            blockIronStairs1ID = cfg.getBlock("blockIronStairs1", 4002).getInt(4002);
            blockIronStairs2ID = cfg.getBlock("blockIronStairs2", 4003).getInt(4003);
            blockIronStairs3ID = cfg.getBlock("blockIronStairs3", 4004).getInt(4004);
            blockIronStairs4ID = cfg.getBlock("blockIronStairs4", 4005).getInt(4005);
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
        blockIron = new BlockIron(blockIronID, 5).setUnlocalizedName("blockIron");
        blockIronStairs0 = (new BlockIronStairs(blockIronStairs0ID, blockIron, 0)).setUnlocalizedName("IronStairs0");
        blockIronStairs1 = (new BlockIronStairs(blockIronStairs1ID, blockIron, 1)).setUnlocalizedName("IronStairs1");
        blockIronStairs2 = (new BlockIronStairs(blockIronStairs2ID, blockIron, 2)).setUnlocalizedName("IronStairs2");
        blockIronStairs3 = (new BlockIronStairs(blockIronStairs3ID, blockIron, 3)).setUnlocalizedName("IronStairs3");
        blockIronStairs4 = (new BlockIronStairs(blockIronStairs4ID, blockIron, 4)).setUnlocalizedName("IronStairs4");
        
        MinecraftForge.setBlockHarvestLevel(blockIron, "pickaxe", 1);
        GameRegistry.registerBlock(blockIron, ItemBlockIron.class, "blockIron");
        GameRegistry.registerBlock(blockIronStairs0, "IronStairs0");
        GameRegistry.registerBlock(blockIronStairs1, "IronStairs1");
        GameRegistry.registerBlock(blockIronStairs2, "IronStairs2");
        GameRegistry.registerBlock(blockIronStairs3, "IronStairs3");
        GameRegistry.registerBlock(blockIronStairs4, "IronStairs4");

        LanguageRegistry.addName(new ItemStack((Block)blockIron, 1, 0), "Iron 1");
        LanguageRegistry.addName(new ItemStack((Block)blockIron, 1, 1), "Iron 2");
        LanguageRegistry.addName(new ItemStack((Block)blockIron, 1, 2), "Iron 3");
        LanguageRegistry.addName(new ItemStack((Block)blockIron, 1, 3), "Iron 4");
        LanguageRegistry.addName(new ItemStack((Block)blockIron, 1, 4), "Iron 5");
        LanguageRegistry.addName(blockIronStairs0, "Iron Stairs 1");
        LanguageRegistry.addName(blockIronStairs1, "Iron Stairs 2");
        LanguageRegistry.addName(blockIronStairs2, "Iron Stairs 3");
        LanguageRegistry.addName(blockIronStairs3, "Iron Stairs 4");
        LanguageRegistry.addName(blockIronStairs4, "Iron Stairs 5");
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
