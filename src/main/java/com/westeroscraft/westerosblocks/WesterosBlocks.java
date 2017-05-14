package com.westeroscraft.westerosblocks;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent; 
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.network.FMLEmbeddedChannel;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.common.config.Configuration;
import net.minecraft.block.Block;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.ReportedException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.westeroscraft.westerosblocks.modelexport.ModelExport;
import com.westeroscraft.westerosblocks.modelexport.ModelExportFactory;
import com.westeroscraft.westerosblocks.network.PacketHandler;
import com.westeroscraft.westerosblocks.network.WesterosBlocksChannelHandler;

@Mod(modid = WesterosBlocks.MOD_ID, name = "WesterosBlocks", version = Version.VER)
public class WesterosBlocks
{    
    public static final String MOD_ID = "westerosblocks";
    public static Logger log = Logger.getLogger("WesterosBlocks");
    
    // The instance of your mod that Forge uses.
    @Instance("WesterosBlocks")
    public static WesterosBlocks instance;

    // Says where the client and server 'proxy' code is loaded.
    @SidedProxy(clientSide = "com.westeroscraft.westerosblocks.ClientProxy", serverSide = "com.westeroscraft.westerosblocks.Proxy")
    public static Proxy proxy;

    // Dynmap support
    public DynmapSupport dynmap = null;
    
    // Block classes
    public static Block customBlocks[];
    public static HashMap<String, Block> customBlocksByName;
    
    // Use stair render fix
    public boolean useFixedStairs = false;
    public boolean useFixedPressurePlate = false;
    public boolean useWaterCTMFix = false;
    public boolean snowInTaiga = false;
    
    public static BitSet slabStyleLightingBlocks = new BitSet();
    
    public static WesterosBlockConfig customConfig;
    
    public static WesterosBlockDef[] customBlockDefs;
    
    public static EnumMap<Side, FMLEmbeddedChannel> channels;

    public static Block findBlockByName(String blkname) {
        Block blk = customBlocksByName.get(blkname);
        if (blk != null) {
            return blk;
        }
        try {
            return Block.getBlockFromName(blkname);
        } catch (NumberFormatException nfx) {
        }
        return null;
    }
    
    public boolean good_init = false;
    public File modcfgdir = null;
    
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
        WesterosBlocksCreativeTab.init();
        
        // Read our block definition resource
        InputStream in = getClass().getResourceAsStream("/WesterosBlocks.json");
        if (in == null) {
            crash("WesterosBlocks couldn't find its block definition resource");
            return;
        }
        InputStreamReader rdr = new InputStreamReader(in);
        Gson gson = new Gson();
        try {
            customConfig = gson.fromJson(rdr, WesterosBlockConfig.class);
            customBlockDefs = customConfig.blocks;
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
            useFixedStairs = cfg.get("Settings",  "useFixedStairs", true).getBoolean(true);
            useFixedPressurePlate = cfg.get("Settings", "useFixedPressurePlate", true).getBoolean(true);
            useWaterCTMFix = cfg.get("Settings", "useWaterCTMFix", true).getBoolean(true);
            snowInTaiga = cfg.get("Settings", "snowInTaiga", true).getBoolean(true);
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
        // Initialize with standard block IDs
        slabStyleLightingBlocks.clear();
        /**NOTYET
        slabStyleLightingBlocks.set(Block.stoneSingleSlab.blockID);
        slabStyleLightingBlocks.set(Block.woodSingleSlab.blockID);
        slabStyleLightingBlocks.set(Block.tilledField.blockID);
        slabStyleLightingBlocks.set(Block.stairsWoodOak.blockID);
        slabStyleLightingBlocks.set(Block.stairsCobblestone.blockID);
        */
        if(snowInTaiga) {
            //TODO: need to handle this in 1.11.2
            //Biome.taiga.setEnableSnow().setTemperatureRainfall(-0.5F, 0.4F);
        }
        modcfgdir = event.getModConfigurationDirectory();
                
        // Construct custom block definitions
        ArrayList<Block> blklist = new ArrayList<Block>();
        customBlocksByName = new HashMap<String, Block>();
        for (int i = 0; i < customBlockDefs.length; i++) {
            if (customBlockDefs[i] == null) continue;
            Block[] blks = customBlockDefs[i].createBlocks();
            if (blks != null) {
                for (int j = 0; j < blks.length; j++) {
                    Block blk = blks[j];
                    blklist.add(blk);
                    customBlocksByName.put(customBlockDefs[i].getBlockName(j), blk);
                    
                    // Do blocks state export here
                    ModelExport exp = ModelExportFactory.forBlock(blk, customBlockDefs[i], modcfgdir);
                    if (exp != null) {
                        try {
                            exp.doBlockStateExport();
                            exp.doModelExports();
                        } catch (IOException iox) {
                            log.warning(String.format("Error exporting block %s - %s",  blk.getUnlocalizedName(), iox));
                        }
                    }
                }
            }
            else {
                crash("Invalid block definition for " + customBlockDefs[i].blockName + " - aborted during load()");
                return;
            }
        }
        customBlocks = blklist.toArray(new Block[blklist.size()]);
        try {
            ModelExport.writeNLSFile(modcfgdir);
        } catch (IOException iox) {
            log.warning(String.format("Error writing NLS - %s", iox));
        }
        
        // Initialize custom block definitions
        for (int i = 0; i < customBlocks.length; i++) {
            if (customBlocks[i] instanceof WesterosBlockLifecycle) {
                ((WesterosBlockLifecycle)customBlocks[i]).initializeBlockDefinition();
            }
        }
        
        // Register custom block definitions
        for (int i = 0; i < customBlocks.length; i++) {
            if (customBlocks[i] instanceof WesterosBlockLifecycle) {
                ((WesterosBlockLifecycle)customBlocks[i]).registerBlockDefinition();
            }
        }
        // Register block models
        WesterosBlocks.proxy.registerBlockModels(customBlocks);

        proxy.initRenderRegistry();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        if (!good_init) {
            crash("preInit failed - aborting load()");
            return;
        }

        // Register block coloring
        WesterosBlocks.proxy.registerColoring();

        // Set up channel
        channels = NetworkRegistry.INSTANCE.newChannel(WesterosBlocksChannelHandler.CHANNEL, new WesterosBlocksChannelHandler(), new PacketHandler());

        // Handle dynmap support
        try {
            handleDynmap();
        } catch (NoClassDefFoundError x) {
            log.info("Dynmap Mod Support API not found");
            this.dynmap = null;
        }
    }

    private void handleDynmap() {
        this.dynmap = new DynmapSupport("WesterosBlocks", Version.VER);
        if (this.dynmap.getTextureDef() == null) {
            return;
        }
        // Register textures from various block definitions
        for (int i = 0; i < customBlocks.length; i++) {
            if (customBlocks[i] instanceof WesterosBlockDynmapSupport) {
                ((WesterosBlockDynmapSupport)customBlocks[i]).registerDynmapRenderData(this.dynmap.getTextureDef());
            }
            else {
                log.info("No Dynmap render support for blockID=" + customBlocks[i].getUnlocalizedName());
            }
        }

        this.dynmap.complete();
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        try {
            Class.forName("wdl.WDL");
            crash("****** WorldDownloader not supported!!!!!! ******");
        } catch (ClassNotFoundException cnfx) {
        }
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
