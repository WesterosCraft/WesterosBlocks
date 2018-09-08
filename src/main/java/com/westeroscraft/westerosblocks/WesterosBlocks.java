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
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.network.FMLEmbeddedChannel;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.common.config.Configuration;
import net.minecraft.block.Block;
import net.minecraft.crash.CrashReport;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.westeroscraft.westerosblocks.asm.ClassTransformer;
import com.westeroscraft.westerosblocks.asm.ClassTransformer.TransformState;
import com.westeroscraft.westerosblocks.commands.PTimeCommand;
import com.westeroscraft.westerosblocks.commands.PWeatherCommand;
import com.westeroscraft.westerosblocks.modelexport.ModelExport;
import com.westeroscraft.westerosblocks.modelexport.ModelExportFactory;
import com.westeroscraft.westerosblocks.network.PacketHandler;
import com.westeroscraft.westerosblocks.network.WesterosBlocksChannelHandler;

@Mod(modid = WesterosBlocks.MOD_ID, name = "WesterosBlocks", version = Version.VER)
public class WesterosBlocks
{    
    public static final String MOD_ID = "westerosblocks";
    
    public static Logger log = LogManager.getLogger(MOD_ID);
    
    // The instance of your mod that Forge uses.
    @Instance(WesterosBlocks.MOD_ID)
    public static WesterosBlocks instance;

    // Says where the client and server 'proxy' code is loaded.
    @SidedProxy(clientSide = "com.westeroscraft.westerosblocks.ClientProxy", serverSide = "com.westeroscraft.westerosblocks.Proxy")
    public static Proxy proxy;

    // Dynmap support
    public DynmapSupport dynmap = null;
    
    // Block classes
    public static Block customBlocks[];
    public static HashMap<String, Block> customBlocksByName;
    
    public boolean blockDevMode = false;
    public boolean snowInTaiga = false;
    public boolean publishToDynmap = false;
    
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
            if (rdr != null) { try { rdr.close(); } catch (IOException iox) {}; rdr = null; }
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
            snowInTaiga = cfg.get("Settings", "snowInTaiga", true).getBoolean(true);
            blockDevMode = cfg.get("Settings", "blockDevMode", false).getBoolean(false);
            publishToDynmap = cfg.get("Settings", "publishToDynmap", false).getBoolean(false);
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
        if(snowInTaiga) {
        	Biomes.TAIGA.temperature = -0.5F;	// Access set up using AccessTransformer
            log.info("Enabled snow in TAIGA");
        }
        modcfgdir = new File(event.getModConfigurationDirectory(), MOD_ID);
        modcfgdir.mkdirs();
                
        if (blockDevMode) {
        	log.info("Block dev mode enabled : block export processing will be done to " + modcfgdir + "/assets/" + MOD_ID);
        }
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
                    if (blockDevMode) {
                    	ModelExport exp = ModelExportFactory.forBlock(blk, customBlockDefs[i], modcfgdir);
                    	if (exp != null) {
                    		try {
                    			exp.doBlockStateExport();
                    			exp.doModelExports();
                    		} catch (IOException iox) {
                    			log.warn(String.format("Error exporting block %s - %s",  blk.getUnlocalizedName(), iox));
                    		}
                    	}
                    }
                }
                // Register sound events
                customBlockDefs[i].registerSoundEvents();
            }
            else {
                crash("Invalid block definition for " + customBlockDefs[i].blockName + " - aborted during load()");
                return;
            }
        }
        customBlocks = blklist.toArray(new Block[blklist.size()]);
        
        if (blockDevMode) {
        	try {
        		ModelExport.writeNLSFile(modcfgdir);
        	} catch (IOException iox) {
        		log.warn(String.format("Error writing NLS - %s", iox));
        	}
            try {
                ModelExport.writeDynmapOverridesFile(modcfgdir);
            } catch (IOException iox) {
                log.warn(String.format("Error writing Dynmap Overrides - %s", iox));
            }
        }

        // Register tile entities
        WesterosBlockDef.processRegisterTileEntities();
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
        try {
			migrateRP();
		} catch (IOException e) {
            log.info("Migrate RP failed");
		}
        
        proxy.initCleanupHooks();
    }

    private void handleDynmap() {
        if (publishToDynmap == false) {
            return;
        }
        this.dynmap = new DynmapSupport(WesterosBlocks.MOD_ID, Version.VER);
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
    
    private static HashMap<String, SoundEvent> registered_sounds = new HashMap<String, SoundEvent>();
    
    public static SoundEvent registerSound(String soundName)
    {
    	SoundEvent event = registered_sounds.get(soundName);
    	if (event == null) {
    		ResourceLocation location = new ResourceLocation(WesterosBlocks.MOD_ID, soundName);
    		event = new SoundEvent(location);
    		event.setRegistryName(location);
    		ForgeRegistries.SOUND_EVENTS.register(event);
    		registered_sounds.put(soundName, event);
    	}
        return event;
    }
    
    private void migrateRP() throws IOException {
    	File f = new File(modcfgdir, "westeroscraft/assets/minecraft/mcpatcher/ctm");
    	if (f.exists() == false) {
    		return;
    	}
    	// Build block ID to resouse ID map
    	HashMap<Integer, String> id_to_name = new HashMap<Integer, String>();
        HashMap<String, Integer> name_to_id = new HashMap<String, Integer>();

    	for (int id = 0; id < 4096; id++) {
    		Block b = Block.getBlockById(id);
    		if ((b != null) && (b != Blocks.AIR)) {
    			id_to_name.put(id, b.getRegistryName().toString());
    			name_to_id.put(id_to_name.get(id), id);
    			log.info(String.format("%d=%s", id, id_to_name.get(id)));
    		}
    	}
    	HashMap<File, Integer> moved_files = new HashMap<File, Integer>();
    	
    	Files.walkFileTree(f.toPath(), new SimpleFileVisitor<Path>() {
    		@Override
    		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
    			String fn = file.getFileName().toString();
    			if (fn.endsWith(".properties")) {
    				FileReader fis = new FileReader(file.toString());
    				LineNumberReader rdr = new LineNumberReader(fis);
    				String line;
    				List<String> lines = new ArrayList<String>();
    				int match_id = -1;
					while ((line = rdr.readLine()) != null) {
    					String[] parts = line.split("=");
    					if ((parts.length > 1) && (parts[0].trim().equalsIgnoreCase("matchBlocks"))) {
    						String matching = parts[1].trim();
    						if (name_to_id.containsKey(matching)) {
    						    match_id = name_to_id.get(matching);
                                log.info(String.format("%s: found matchBlocks = %s(%d)", file.toString(), matching, match_id));
                                lines.add("#" + line);  // Keep line with comment
    						}
    						else {    // Keep line if we didn't match
    						    lines.add(line);
    						}
    					}
    					else {
    						lines.add(line);
    					}
    				}
					rdr.close();
					fis.close();
					int blockfileid = -1;
					int blocknameoff = 0;
					if (fn.startsWith("block")) {
						int blkid = 0;
						blocknameoff = 5;
						while (Character.isDigit(fn.charAt(blocknameoff))) {
							blkid = (10*blkid) + (fn.charAt(blocknameoff) - '0');
							blocknameoff++;
						}
						log.info(String.format("%s: inferred blockid = %d", file.toString(), blkid));
						blockfileid = blkid;
					}
					// If we have matchBlocks= field, strip it out
					if (match_id >= 0) {
                        FileWriter fw = new FileWriter(file.toFile());
                        for (String l : lines) {
                            fw.write(l.trim() + "\n");
                        }
                        fw.close();
					}
					// If block ID in name not found, or didn't match, then rename fiel
					if ((match_id > 0) && (blockfileid != match_id)) {
					    String newfn = "block" + match_id + fn.substring(blocknameoff);
					    Files.move(file, file.resolveSibling(newfn));
                        log.info(String.format("%s: renamed to %s", file.toString(), newfn));
					}
    			}
    			return FileVisitResult.CONTINUE;
    		}
    	});
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
    	// ptime is only valid for MP mode
    	if(event.getSide().isServer()) {
    		event.registerServerCommand(new PTimeCommand());
    		event.registerServerCommand(new PWeatherCommand());
    	}
	}
    
    @EventHandler
    public void serverStarted(FMLServerStartedEvent event)
    {
        if (blockDevMode) {
            log.info("--- Transform Status --- ");
            HashMap<String, ClassTransformer.TransformState> m = ClassTransformer.patchState;
            for (Entry<String, TransformState> x : m.entrySet()) {
                log.info("Transform " + x.getKey() + "=" + x.getValue().toString());
            }
            log.info("--- Transform Status Done --- ");
        }
    }

    @EventHandler
    public void serverStopping(FMLServerStoppingEvent event)
    {
        if (blockDevMode) {
            log.info("--- Transform Status --- ");
            HashMap<String, ClassTransformer.TransformState> m = ClassTransformer.patchState;
            for (Entry<String, TransformState> x : m.entrySet()) {
                log.info("Transform " + x.getKey() + "=" + x.getValue().toString());
            }
            log.info("--- Transform Status Done --- ");
        }
    }

    @EventHandler
    public void serverStopped(FMLServerStoppedEvent event)
    {
    }    
}
