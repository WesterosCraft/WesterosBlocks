package com.westeroscraft.westerosblocks;

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
import net.minecraft.crash.CrashReport;
import net.minecraft.util.ReportedException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.westeroscraft.westerosblocks.blocks.BlockLightAsh;
import com.westeroscraft.westerosblocks.blocks.BlockWCIronFence;
import com.westeroscraft.westerosblocks.blocks.ItemBlockWCIronFence;

@Mod(modid = "WesterosBlocks", name = "WesterosBlocks", version = Version.VER)
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class WesterosBlocks
{    
    public static Logger log = Logger.getLogger("WesterosBlocks");
    
    // The instance of your mod that Forge uses.
    @Instance("WesterosBlocks")
    public static WesterosBlocks instance;

    // Says where the client and server 'proxy' code is loaded.
    @SidedProxy(clientSide = "com.westeroscraft.westerosblocks.ClientProxy", serverSide = "com.westeroscraft.westerosblocks.Proxy")
    public static Proxy proxy;

    // Configuration variables (mostly block ids)
    public static boolean doReplaceMycelium;
    public static boolean doReplaceIronFence;
    
    // Block classes
    public static Block customBlocks[];
    // Replacement block classes
    public static BlockMycelium blockMycelium;
    public static BlockWCIronFence blockIronFence;
    
    public static WesterosBlockDef[] customBlockDefs;
    
    public static Block findBlockByName(String blkname) {
        for (int i = 0; i < customBlockDefs.length; i++) {
            if (customBlockDefs[i].blockName.equals(blkname)) {
                System.out.println(blkname + " matched on index " + i + ": return " + customBlocks[i]);
                return customBlocks[i];
            }
        }
        try {
            int id = Integer.parseInt(blkname);
            if ((id > 0) && (id < Block.blocksList.length)) {
                System.out.println(blkname + " matched on ID " + id);
                return Block.blocksList[id];
            }
        } catch (NumberFormatException nfx) {
        }
        System.out.println(blkname + " missed");
        return null;
    }
    
    public boolean good_init = false;
    
    public static void crash(Exception x, String msg) {
        CrashReport crashreport = CrashReport.makeCrashReport(x, msg);
        throw new ReportedException(crashreport);
    }
    public static void crash(String msg) {
        crash(new Exception(), msg);
    }
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        // Initialize 
        WesterosBlockDef.initialize();
        
        // Read our block definition resource
        InputStream in = getClass().getResourceAsStream("/WesterosBlocks.json");
        if (in == null) {
            crash("WesterosBlocks couldn't find its block definition resource");
            return;
        }
        InputStreamReader rdr = new InputStreamReader(in);
        Gson gson = new Gson();
        try {
            customBlockDefs = gson.fromJson(rdr, WesterosBlockDef[].class);
        } catch (JsonSyntaxException iox) {
            crash(iox, "WesterosBlocks couldn't parse its block definition");
            return;
        } catch (JsonIOException iox) {
            crash(iox, "WesterosBlocks couldn't read its block definition");
            return;
        } finally {
            if (in != null) { try { in.close(); } catch (IOException iox) {}; in = null; }
        }
        log.info("Loaded " + customBlockDefs.length + " block definitions");
        if (WesterosBlockDef.sanityCheck(customBlockDefs) == false) {
            crash("WesterosBlocks.json failed sanity check");
            return;
        }
        
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
            
            good_init = true;
        }
        catch (Exception e)
        {
            crash(e, "WesterosBlocks couldn't load its configuration");
        }
        finally
        {
            cfg.save();
        }
    }

    @EventHandler
    public void load(FMLInitializationEvent event)
    {
        if (!good_init) {
            crash("preInit failed - aborting load()");
            return;
        }
        // Replacement blocks
        if (doReplaceMycelium) {
            Block.blocksList[Block.mycelium.blockID] = null;
            blockMycelium = (BlockMycelium)(new BlockLightAsh(Block.mycelium.blockID)).setHardness(0.6F).setStepSound(Block.soundGrassFootstep).setUnlocalizedName("mycel").func_111022_d("mycelium");
        }
        if (doReplaceIronFence) {
            Block.blocksList[Block.fenceIron.blockID] = null;
            blockIronFence = (BlockWCIronFence) (new BlockWCIronFence(Block.fenceIron.blockID, "iron_bars", "iron_bars", Material.iron, true)).setHardness(5.0F).setResistance(10.0F).setStepSound(Block.soundMetalFootstep).setUnlocalizedName("fenceIron");
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
        // Register replacement items
        if (doReplaceMycelium) {
            LanguageRegistry.addName(blockMycelium, "Light Ash");
        }
        if (doReplaceIronFence) {
            blockIronFence.registerNames();
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
