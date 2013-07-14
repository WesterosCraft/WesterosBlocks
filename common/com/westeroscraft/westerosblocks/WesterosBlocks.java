package com.westeroscraft.westerosblocks;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
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
import net.minecraft.block.Block;
import net.minecraft.block.BlockMycelium;
import net.minecraft.block.material.Material;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.westeroscraft.westerosblocks.blocks.BlockLightAsh;
import com.westeroscraft.westerosblocks.blocks.BlockWCIronFence;
import com.westeroscraft.westerosblocks.blocks.BlockWCWoodFence;
import com.westeroscraft.westerosblocks.blocks.ItemBlockWCIronFence;
import com.westeroscraft.westerosblocks.blocks.MultiBlockItem;

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
    public static boolean doReplaceMycelium;
    public static boolean doReplaceIronFence;
    public static boolean doReplaceWoodFence;
    
    // Block classes
    public static Block customBlocks[];
    // Replacement block classes
    public static BlockMycelium blockMycelium;
    public static BlockWCIronFence blockIronFence;
    public static BlockWCWoodFence blockWoodFence;
    
    public static WesterosBlockDef[] customBlockDefs;
    
    public static Block findBlockByName(String blkname) {
        for (int i = 0; i < customBlockDefs.length; i++) {
            if (customBlockDefs[i].blockName.equals(blkname)) {
                return customBlocks[i];
            }
        }
        try {
            int id = Integer.parseInt(blkname);
            if ((id > 0) && (id < Block.blocksList.length)) {
                return Block.blocksList[id];
            }
        } catch (NumberFormatException nfx) {
        }
        return null;
    }
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        // Initialize 
        WesterosBlockDef.initialize();
        
        // Read our block definition resource
        InputStream in = getClass().getResourceAsStream("/WesterosBlocks.json");
        if (in == null) {
            FMLLog.severe("WesterosBlocks couldn't find its block definition resource");
            return;
        }
        InputStreamReader rdr = new InputStreamReader(in);
        Gson gson = new Gson();
        try {
            customBlockDefs = gson.fromJson(rdr, WesterosBlockDef[].class);
        } catch (JsonSyntaxException iox) {
            FMLLog.log(Level.SEVERE, iox, "WesterosBlocks couldn't parse its block definition", new Object[0]);
            return;
        } catch (JsonIOException iox) {
            FMLLog.log(Level.SEVERE, iox, "WesterosBlocks couldn't read its block definition", new Object[0]);
            return;
        } finally {
            if (in != null) { try { in.close(); } catch (IOException iox) {}; in = null; }
        }
        FMLLog.info("Loaded " + customBlockDefs.length + " block definitions");
        
        // Load configuration file - use suggested (config/WesterosBlocks.cfg)
        Configuration cfg = new Configuration(event.getSuggestedConfigurationFile());
        try
        {
            cfg.load();
            // Add settings for block defintiions
            for (int i = 0; i < customBlockDefs.length; i++) {
                customBlockDefs[i].blockID = cfg.getBlock(customBlockDefs[i].blockName, customBlockDefs[i].blockID).getInt(customBlockDefs[i].blockID);
            }
            
            // Replacement block flags
            doReplaceMycelium = cfg.get("replacements", "mycelium", true).getBoolean(true);
            doReplaceIronFence = cfg.get("replacements", "ironfence", true).getBoolean(true);
            doReplaceWoodFence = cfg.get("replacements", "woodfence", true).getBoolean(true);
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

    @EventHandler
    public void load(FMLInitializationEvent event)
    {
        // Replacement blocks
        if (doReplaceMycelium) {
            Block.blocksList[Block.mycelium.blockID] = null;
            blockMycelium = (BlockMycelium)(new BlockLightAsh(Block.mycelium.blockID)).setHardness(0.6F).setStepSound(Block.soundGrassFootstep).setUnlocalizedName("mycel").func_111022_d("mycelium");
        }
        if (doReplaceIronFence) {
            Block.blocksList[Block.fenceIron.blockID] = null;
            blockIronFence = (BlockWCIronFence) (new BlockWCIronFence(Block.fenceIron.blockID, "iron_bars", "iron_bars", Material.iron, true)).setHardness(5.0F).setResistance(10.0F).setStepSound(Block.soundMetalFootstep).setUnlocalizedName("fenceIron");
        }
        if (doReplaceWoodFence) {
            Block.blocksList[Block.fence.blockID] = null;
            blockWoodFence = new BlockWCWoodFence(85);
        }
        // Construct custom block definitions
        customBlocks = new Block[customBlockDefs.length];
        for (int i = 0; i < customBlockDefs.length; i++) {
            customBlocks[i] = customBlockDefs[i].createBlock(i);
        }
        
        // Initialize custom block definitions
        for (int i = 0; i < customBlockDefs.length; i++) {
            if (customBlocks[i] instanceof WesterosBlockLifecycle) {
                ((WesterosBlockLifecycle)customBlocks[i]).initializeBlockDefinition();
            }
        }
                
        // Register blocks
        // Register replacement blocks
        if (doReplaceMycelium) {
            GameRegistry.registerBlock(blockMycelium, "mycel");
        }
        if (doReplaceIronFence) {
            GameRegistry.registerBlock(blockIronFence, ItemBlockWCIronFence.class, "fenceIron");
        }
        if (doReplaceWoodFence) {
            GameRegistry.registerBlock(blockWoodFence, MultiBlockItem.class, "fence");
        }
        // Register replacement items
        if (doReplaceMycelium) {
            LanguageRegistry.addName(blockMycelium, "Light Ash");
        }
        if (doReplaceIronFence) {
            blockIronFence.registerNames();
        }
        if (doReplaceWoodFence) {
            blockWoodFence.registerNames();
        }
        // Register custom block definitions
        for (int i = 0; i < customBlockDefs.length; i++) {
            if (customBlocks[i] instanceof WesterosBlockLifecycle) {
                ((WesterosBlockLifecycle)customBlocks[i]).registerBlockDefinition();
            }
        }
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
    }
    
    @EventHandler
    public void serverStarted(FMLServerStartedEvent event)
    {
    }

    @EventHandler
    public void serverStopping(FMLServerStoppingEvent event)
    {
    }
}
