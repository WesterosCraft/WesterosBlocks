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
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.ReportedException;

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
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    
    public static Logger log = LogManager.getLogger(MOD_ID);
    
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
    
    public boolean blockDevMode = false;
    public boolean snowInTaiga = false;
    
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
    
    private void migrateRP() throws IOException {
    	File f = new File(modcfgdir, "WesterosCraft/assets/minecraft/mcpatcher/ctm");
    	if (f.exists() == false) {
    		return;
    	}
    	// Build block ID to resouse ID map
    	HashMap<Integer, String> id_to_name = new HashMap<Integer, String>();
    	for (int id = 0; id < 4096; id++) {
    		Block b = Block.getBlockById(id);
    		if ((b != null) && (b != Blocks.AIR)) {
    			id_to_name.put(id, b.getRegistryName().toString());
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
    				String matching = null;
    				String tilesmatching = null;
					while ((line = rdr.readLine()) != null) {
    					String[] parts = line.split("=");
    					if ((parts.length > 1) && (parts[0].trim().equalsIgnoreCase("matchBlocks"))) {
    						matching = parts[1].trim();
    						log.info(String.format("%s: found matchBlocks = %s", file.toString(), matching));
    					}
    					else if ((parts.length > 1) && (parts[0].trim().equalsIgnoreCase("tiles"))) {
    					    tilesmatching = parts[1];
    					}
    					else {
    						lines.add(line);
    					}
    				}
					rdr.close();
					fis.close();
					if (matching == null) {	// Not found : see if we can infer from name
						if (fn.startsWith("block")) {
							int blkid = 0;
							int off = 5;
							while (Character.isDigit(fn.charAt(off))) {
								blkid = (10*blkid) + (fn.charAt(off) - '0');
								off++;
							}
    						log.info(String.format("%s: inferred matchBlocks = %d", file.toString(), blkid));
    						matching = Integer.toString(blkid);
						}
					}
					// If we've got something to map, process it
					if (matching != null) {
						String[] ids = matching.split(" ");
						String newline = "matchBlocks=";
						for (String id : ids) {
							try {
								int idnum = Integer.parseInt(id.trim());
								String name = id_to_name.get(idnum);
								if (name == null) {
									log.info(String.format("%s: ID not found!!!  %d", file.toString(), idnum));
									newline += idnum + " ";
								}
								else {
									newline += name + " ";
								}
							} catch (NumberFormatException nfx) {
								log.info(String.format("%s: Non number - preserved - %s", file.toString(), id.trim()));
								newline += id.trim() + " ";
							}
						}
						lines.add(0, newline);	// Add at start
					}
					if (tilesmatching != null) {
                        String[] ids = tilesmatching.split(" ");
                        String newline = "tiles=";
                        for (String id : ids) {
                            if (id.indexOf('-') > 0) {  // Range?  Assume already numbers
                                newline += id + " ";
                            }
                            else {
                                try {
                                    Integer.parseInt(id);
                                    newline += id + " ";
                                } catch (NumberFormatException nfx) {
                                    String ourdir = file.toFile().getParent();
                                    // Not a number - need to map it to one
                                    File oldfile = new File(ourdir, id + ".png");
                                    // See if already moved
                                    Integer newnum = moved_files.get(oldfile);
                                    if (newnum == null) { // Not yet?
                                        if (oldfile.exists()) { // Found old file
                                            // Find new number that isn't in use yet
                                            for (int nn = 0; ; nn++) {
                                                File newfile = new File(ourdir, nn + ".png");
                                                if (newfile.exists() == false) {
                                                    newnum = nn;
                                                    log.info(String.format("%s: rename %s to %s", file.toString(), oldfile, newfile));
                                                    oldfile.renameTo(newfile);
                                                    moved_files.put(oldfile,  newnum);
                                                    break;
                                                }
                                            }
                                        }
                                        else {
                                            log.info(String.format("%s: file '%s' not found", file.toString(), oldfile));
                                        }
                                    }
                                    if (newnum != null) {   // New value
                                        newline += newnum + " ";
                                    }
                                    else {
                                        log.info(String.format("%s: problem moving file %s", file.toString(), id.trim()));
                                        newline += id.trim() + " ";
                                    }
                                }
                            }
                        }
                        lines.add(newline);  // Add at end
					}
					if ((matching != null) || (tilesmatching != null)) {
						FileWriter fw = new FileWriter(file.toFile());
						for (String l : lines) {
							fw.write(l.trim() + "\n");
						}
						fw.close();
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
