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
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockPane;
import net.minecraft.block.BlockWall;
import net.minecraft.block.material.Material;
import net.minecraft.crash.CrashReport;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMultiTextureTile;
import net.minecraft.util.ReportedException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.westeroscraft.westerosblocks.blocks.EntityWCFallingSand;
import com.westeroscraft.westerosblocks.blocks.WCCuboidRenderer;
import com.westeroscraft.westerosblocks.blocks.WCFenceRenderer;
import com.westeroscraft.westerosblocks.blocks.WCHalfDoorRenderer;
import com.westeroscraft.westerosblocks.blocks.WCLadderRenderer;
import com.westeroscraft.westerosblocks.blocks.WCStairRenderer;

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
    public static int stairRenderID;
    // Use stair render fix
    public boolean useFixedStairs = false;
    public boolean useFixedFence = false;
    public boolean useFixedWall = false;
    public boolean useFixedPane = false;
    
    public static WesterosBlockConfig customConfig;
    
    public static WesterosBlockDef[] customBlockDefs;
    
    public static HashMap<String, WesterosBlocksSoundDef> soundsDefs;
    
    public static Block findBlockByName(String blkname) {
        Block blk = customBlocksByName.get(blkname);
        if (blk != null) {
            return blk;
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
                    customBlockDefs[i].blockIDs[j] = cfg.getBlock(customBlockDefs[i].getUnlocalizedName(j), val).getInt(val);
                    if (j == 0) {
                        customBlockDefs[i].blockID = customBlockDefs[i].blockIDs[j];
                    }
                }
            }
            useFixedStairs = cfg.get("Settings",  "useFixedStairs", true).getBoolean(true);
            useFixedFence = cfg.get("Settings", "useFixedFence", true).getBoolean(true);
            useFixedWall = cfg.get("Settings", "useFixedWall", true).getBoolean(true);
            useFixedPane = cfg.get("Settings", "useFixedPane", true).getBoolean(true);
            
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
            if(FMLCommonHandler.instance().getSide().isClient()) {  
                MinecraftForge.EVENT_BUS.register(new SoundEventListener());
            }
        }
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
        halfdoorRenderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new WCHalfDoorRenderer());
        cuboidRenderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new WCCuboidRenderer());
        stairRenderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new WCStairRenderer());
        proxy.initRenderRegistry();
        
        // Construct custom block definitions
        ArrayList<Block> blklist = new ArrayList<Block>();
        customBlocksByName = new HashMap<String, Block>();
        for (int i = 0; i < customBlockDefs.length; i++) {
            Block[] blks = customBlockDefs[i].createBlocks();
            if (blks != null) {
                for (int j = 0; j < blks.length; j++) {
                    Block blk = blks[j];
                    blklist.add(blk);
                    customBlocksByName.put(customBlockDefs[i].getUnlocalizedName(j), blk);
                }
            }
            else {
                crash("Invalid block definition for " + customBlockDefs[i].blockName + " - aborted during load()");
                return;
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
        // Fix standard blocks
        if (useFixedStairs) {
            /*
            Block.blocksList[53] = null; Item.itemsList[53] = null;
            GameRegistry.registerBlock(new FixedStairs(53, Block.planks, 0), "stairsWood");
            Block.blocksList[67] = null; Item.itemsList[67] = null;
            GameRegistry.registerBlock(new FixedStairs(67, Block.cobblestone, 0), "stairsStone");
            Block.blocksList[108] = null; Item.itemsList[108] = null;
            GameRegistry.registerBlock(new FixedStairs(108, Block.brick, 0), "stairsBrick");
            Block.blocksList[109] = null; Item.itemsList[109] = null;
            GameRegistry.registerBlock(new FixedStairs(109, Block.stoneBrick, 0), "stairsStoneBrickSmooth");
            Block.blocksList[114] = null; Item.itemsList[114] = null;
            GameRegistry.registerBlock(new FixedStairs(114, Block.netherBrick, 0), "stairsNetherBrick");
            Block.blocksList[128] = null; Item.itemsList[128] = null;
            GameRegistry.registerBlock(new FixedStairs(128, Block.sandStone, 0), "stairsSandStone");
            Block.blocksList[134] = null; Item.itemsList[134] = null;
            GameRegistry.registerBlock(new FixedStairs(134, Block.planks, 1), "stairsWoodSpruce");
            Block.blocksList[135] = null; Item.itemsList[153] = null;
            GameRegistry.registerBlock(new FixedStairs(135, Block.planks, 2), "stairsWoodBirch");
            Block.blocksList[136] = null; Item.itemsList[136] = null;
            GameRegistry.registerBlock(new FixedStairs(136, Block.planks, 3), "stairsWoodJungle");
            Block.blocksList[156] = null; Item.itemsList[156] = null;
            GameRegistry.registerBlock(new FixedStairs(156, Block.blockNetherQuartz, 0), "stairsQuartz"); 
            */
        }
        // Use fixed fence (for connection to custom fences)
        if (useFixedFence) {
            Block.blocksList[85] = null; Item.itemsList[85] = null;
            GameRegistry.registerBlock((new FixedFence(85, "planks_oak", Material.wood)).setHardness(2.0F).setResistance(5.0F).setStepSound(Block.soundWoodFootstep).setUnlocalizedName("fence"), "fence");
            Block.blocksList[113] = null; Item.itemsList[113] = null;
            GameRegistry.registerBlock((new FixedFence(113, "nether_brick", Material.rock)).setHardness(2.0F).setResistance(10.0F).setStepSound(Block.soundStoneFootstep).setUnlocalizedName("netherFence"), "netherFence");
        }
        // Use fixed wall (for connection to custom walls)
        if (useFixedWall) {
            Block.blocksList[139] = null; Item.itemsList[139] = null;
            GameRegistry.registerBlock((new FixedWall(139, Block.cobblestone)).setUnlocalizedName("cobbleWall"), "cobbleWall");
            Item.itemsList[139] = (new ItemMultiTextureTile(139 - 256, Block.blocksList[139], BlockWall.types)).setUnlocalizedName("cobbleWall");
        }
        // Use fixed pane (for connection to custom panes_
        if (useFixedPane) {
            Block.blocksList[101] = null; Item.itemsList[101] = null;
            
            GameRegistry.registerBlock((new FixedPane(101, "iron_bars", "iron_bars", Material.iron, true)).setHardness(5.0F).setResistance(10.0F).setStepSound(Block.soundMetalFootstep).setUnlocalizedName("fenceIron"), "fenceIron");
            Block.blocksList[102] = null; Item.itemsList[102] = null;
            GameRegistry.registerBlock((new FixedPane(102, "glass", "glass_pane_top", Material.glass, false)).setHardness(0.3F).setStepSound(Block.soundGlassFootstep).setUnlocalizedName("thinGlass"), "thinGlass");
        }
        // Register entities
        EntityRegistry.registerModEntity(EntityWCFallingSand.class, "Falling Sand", nextEntityID++, this, 120, 20, true);;

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
    
    public class SoundEventListener {
        @ForgeSubscribe
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
}
