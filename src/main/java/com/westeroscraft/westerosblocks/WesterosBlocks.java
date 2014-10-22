package com.westeroscraft.westerosblocks;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
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
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraft.block.Block;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.ReportedException;
import io.netty.channel.ChannelHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.westeroscraft.westerosblocks.asm.ClassTransformer;
import com.westeroscraft.westerosblocks.blocks.WCCuboidNSEWUDRenderer;
import com.westeroscraft.westerosblocks.blocks.WCCuboidRenderer;
import com.westeroscraft.westerosblocks.blocks.WCFenceRenderer;
import com.westeroscraft.westerosblocks.blocks.WCFluidCTMRenderer;
import com.westeroscraft.westerosblocks.blocks.WCLadderRenderer;
/*NOTYET
import com.westeroscraft.westerosblocks.blocks.EntityWCFallingSand;
import com.westeroscraft.westerosblocks.blocks.WCCuboidNSEWUDRenderer;
import com.westeroscraft.westerosblocks.blocks.WCCuboidRenderer;
import com.westeroscraft.westerosblocks.blocks.WCFenceRenderer;
import com.westeroscraft.westerosblocks.blocks.WCFluidCTMRenderer;
import com.westeroscraft.westerosblocks.blocks.WCHalfDoorRenderer;
import com.westeroscraft.westerosblocks.blocks.WCLadderRenderer;
*/
import com.westeroscraft.westerosblocks.blocks.WCStairRenderer;

@Mod(modid = "WesterosBlocks", name = "WesterosBlocks", version = Version.VER)
public class WesterosBlocks
{    
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
    // Custom renders
    public static int fenceRenderID;
    public static int ladderRenderID;
    public static int halfdoorRenderID;
    public static int cuboidRenderID;
    public static int cuboidNSEWUDRenderID;
    public static int stairRenderID;
    public static int fluidCTMRenderID;
    // Use stair render fix
    public boolean useFixedStairs = false;
    public boolean useFixedPressurePlate = false;
    public boolean useWaterCTMFix = false;
    
    public static BitSet slabStyleLightingBlocks = new BitSet();
    
    public static WesterosBlockConfig customConfig;
    
    public static WesterosBlockDef[] customBlockDefs;
    
    public static HashMap<String, WesterosBlocksSoundDef> soundsDefs;
    

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
        log.info("Loaded " + customBlockDefs.length + " block definitions and " + customConfig.stepSounds.length + " stepsounds");
        // Process step sound definitions first
        for (WesterosBlockStepSound ss : customConfig.stepSounds) {
            WesterosBlockDef.registerStepSound(ss);
        }
        
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
                if (customBlockDefs[i].blockIDs == null) {
                    customBlockDefs[i].blockIDs = new int[] { customBlockDefs[i].blockID };
                }
                for (int j = 0; j < customBlockDefs[i].blockIDs.length; j++) {
                    int val = customBlockDefs[i].blockIDs[j];
                    /**NOTYET
                    customBlockDefs[i].blockIDs[j] = cfg.getBlock(customBlockDefs[i].getBlockName(j), val).getInt(val);
                    */
                    if (j == 0) {
                        customBlockDefs[i].blockID = customBlockDefs[i].blockIDs[j];
                    }
                }
            }
            useFixedStairs = cfg.get("Settings",  "useFixedStairs", true).getBoolean(true);
            useFixedPressurePlate = cfg.get("Settings", "useFixedPressurePlate", true).getBoolean(true);
            useWaterCTMFix = cfg.get("Settings", "useWaterCTMFix", true).getBoolean(true);
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
        // Build sound def map
        soundsDefs = new HashMap<String, WesterosBlocksSoundDef>();
        if (customConfig.sounds != null) {
            for (WesterosBlocksSoundDef sd : customConfig.sounds) {
                String rname = sd.soundResourceName;
                if (rname.indexOf(':') < 0) {
                    rname = "westerosblocks:" + rname;
                }
                sd.soundResourceID = rname.replace('/',  '.');
                soundsDefs.put(sd.name, sd);
            }
            // Register listener for client sound loading
            /*NOTYET
            if(FMLCommonHandler.instance().getSide().isClient()) {  
                MinecraftForge.EVENT_BUS.register(new SoundEventListener());
            }
            */
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
    }

    @EventHandler
    public void load(FMLInitializationEvent event)
    {
        if (!good_init) {
            crash("preInit failed - aborting load()");
            return;
        }
        // Register renderer
        fenceRenderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new WCFenceRenderer());
        ladderRenderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new WCLadderRenderer());
        cuboidRenderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new WCCuboidRenderer());
        /**NOTYET
        halfdoorRenderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new WCHalfDoorRenderer());
        */
        cuboidNSEWUDRenderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new WCCuboidNSEWUDRenderer());
        stairRenderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new WCStairRenderer());
        if (useWaterCTMFix && ClassTransformer.checkForCTMSupport()) {
            fluidCTMRenderID = RenderingRegistry.getNextAvailableRenderId();
            RenderingRegistry.registerBlockHandler(new WCFluidCTMRenderer());
        }
        else {
            useWaterCTMFix = false;
            fluidCTMRenderID = 4;   // Vanilla fluid renderer
        }
        proxy.initRenderRegistry();
        
        //NOTYET NetworkRegistry.newChannel(WesterosBlocksPacketHandler.CHANNEL, new WesterosBlocksPacketHandler());
        // Construct custom block definitions
        ArrayList<Block> blklist = new ArrayList<Block>();
        customBlocksByName = new HashMap<String, Block>();
        for (int i = 0; i < customBlockDefs.length; i++) {
            Block[] blks = customBlockDefs[i].createBlocks();
            if (blks != null) {
                for (int j = 0; j < blks.length; j++) {
                    Block blk = blks[j];
                    blklist.add(blk);
                    customBlocksByName.put(customBlockDefs[i].getBlockName(j), blk);
                }
            }
            else {
                /**NOTYET 
                crash("Invalid block definition for " + customBlockDefs[i].blockName + " - aborted during load()");
                return;
                */
            }
        }
        customBlocks = blklist.toArray(new Block[blklist.size()]);
        
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
        // Register entities
        /**NOTYET
        EntityRegistry.registerModEntity(EntityWCFallingSand.class, "Falling Sand", nextEntityID++, WesterosBlocks.instance, 120, 20, true);;
        */
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
    
    private int nextEntityID = 3000;

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
    
    /**NOTYET
    public class SoundEventListener {
        @SubscribeEvent
        public void onSound(SoundLoadEvent event)
        {
            log.info("Registering " + customConfig.sounds.length + " custom sounds");
            HashSet<String> soundids = new HashSet<String>();
            for (WesterosBlocksSoundDef sd : customConfig.sounds) {
                String rname = sd.soundResourceName;
                if (rname.indexOf(':') < 0) {
                    rname = "westerosblocks:" + rname;
                }
                if (sd.numberOfSounds > 1) {
                    for (int i = 1; i <= sd.numberOfSounds; i++) {
                        String sid = rname + i + ".ogg";
                        if (soundids.contains(sid) == false) {
                            try {
                                event.manager.soundPoolSounds.addSound(sid);
                                soundids.add(sid);
                            } catch (Exception x) {
                                log.warning("Failed to register sound " + sid + " - " + x.getMessage());
                            }
                        }
                    }
                }
                else {
                    String sid = rname + ".ogg";
                    if (soundids.contains(sid) == false) {
                        try {
                            event.manager.soundPoolSounds.addSound(sid);
                            soundids.add(sid);
                        } catch (Exception x) {
                            log.warning("Failed to register sound " + sid + " - " + x.getMessage());
                        }
                    }
                }
            }
        }
    }
    */
}
