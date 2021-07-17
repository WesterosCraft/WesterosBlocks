package com.westeroscraft.westerosblocks;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biomes;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ForgeRegistries;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.westeroscraft.westerosblocks.modelexport.ModelExport;
import com.westeroscraft.westerosblocks.modelexport.ModelExportFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(WesterosBlocks.MOD_ID)
public class WesterosBlocks
{
	public static final String MOD_ID = "westerosblocks";
	
	// Directly reference a log4j logger.
    public static final Logger log = LogManager.getLogger();

    public static WesterosBlockConfig customConfig;
    
    // Says where the client and server 'proxy' code is loaded.
    public static Proxy proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> Proxy::new);

    public static WesterosBlockDef[] customBlockDefs;

	public static HashMap<String, Block> customBlocksByName;

	public static Block[] customBlocks = new Block[0];
	
	public static Path modConfigPath;
    
    public WesterosBlocks() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        // Register the setup method for load complete
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::loadComplete);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        
        Path configPath = FMLPaths.CONFIGDIR.get();
        
        modConfigPath = Paths.get(configPath.toAbsolutePath().toString(), MOD_ID);

        // Create the config folder
        try
        {
            Files.createDirectory(modConfigPath);
        }
        catch (FileAlreadyExistsException e)
        {
            // Do nothing
        }
        catch (IOException e)
        {
            log.error("Failed to create westerosblocks config directory", e);
        }
        
        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.COMMON, Config.SPEC, MOD_ID + "/" + MOD_ID + ".toml");
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        log.info("HELLO from setup");

    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        log.info("Got game settings {}", event.getMinecraftSupplier().get().options);
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        log.info("HELLO from server starting");
    }

    private void loadComplete(final FMLLoadCompleteEvent event) // PostRegistrationEven
    {
        // Initialize with standard block IDs
        if (Config.snowInTaiga.get()) {
//        	Biomes.TAIGA = -0.5F;	// Access set up using AccessTransformer
            log.info("Enabled snow in TAIGA");
        }
        if (Config.blockDevMode.get()) {
        	log.info("Block dev mode enabled : block export processing will be done to " + modConfigPath + "/assets/" + MOD_ID);
        }
        // Do blocks state export here
        if (Config.blockDevMode.get()) {
        	for (int i = 0; i < customBlockDefs.length; i++) {
        		if (customBlockDefs[i] == null) continue;
        		Block blk = customBlocksByName.get(customBlockDefs[i].getBlockName(0));
        		if (blk != null) {
                	ModelExport exp = ModelExportFactory.forBlock(blk, customBlockDefs[i], modConfigPath.toFile());
                	if (exp != null) {
                		try {
                			exp.doBlockStateExport();
                			exp.doModelExports();
                		} catch (IOException iox) {
                			log.warn(String.format("Error exporting block %s - %s",  blk.getRegistryName(), iox));
                		}
                	}
                }
        	}
        	try {
        		ModelExport.writeNLSFile(modConfigPath);
        	} catch (IOException iox) {
        		log.warn(String.format("Error writing NLS - %s", iox));
        	}
            try {
                ModelExport.writeDynmapOverridesFile(modConfigPath);
            } catch (IOException iox) {
                log.warn(String.format("Error writing Dynmap Overrides - %s", iox));
            }
        }
    }
    
    public static void crash(Exception x, String msg) {
        throw new ReportedException(new CrashReport(msg, x));
    }
    public static void crash(String msg) {
        crash(new Exception(), msg);
    }
    
    public static class Config
    {
        public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
        public static final ForgeConfigSpec SPEC;
        public static final ForgeConfigSpec.BooleanValue snowInTaiga;
        public static final ForgeConfigSpec.BooleanValue blockDevMode;
        public static final ForgeConfigSpec.BooleanValue publishToDynmap;

        static
        {
            BUILDER.comment("Module options");
            snowInTaiga = BUILDER.comment("Enable snow in taiga").define("snowInTaiga", true);
            blockDevMode = BUILDER.comment("Block development mode").define("blockDevMode", false);
            publishToDynmap = BUILDER.comment("Publish blocks to dynmap").define("publishToDynmap", true);

            SPEC = BUILDER.build();
        }
    }
    
    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {            
            // Initialize 
            WesterosBlockDef.initialize();
            WesterosBlocksCreativeTab.init();
            
            // Read our block definition resource
            InputStream in = WesterosBlocks.class.getResourceAsStream("/WesterosBlocks.json");
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
            // Construct custom block definitions
            ArrayList<Block> blklist = new ArrayList<Block>();
            customBlocksByName = new HashMap<String, Block>();
            for (int i = 0; i < customBlockDefs.length; i++) {
                if (customBlockDefs[i] == null) continue;
                Block blk = customBlockDefs[i].createBlock();
                if (blk != null) {
                    blklist.add(blk);
                    customBlocksByName.put(customBlockDefs[i].getBlockName(0), blk);
                    // Register sound events
                    //TODO customBlockDefs[i].registerSoundEvents();
                }
                else {
                    //crash("Invalid block definition for " + customBlockDefs[i].blockName + " - aborted during load()");
                    //return;
                }
            }
            customBlocks = blklist.toArray(new Block[blklist.size()]);
            
            // Register tile entities
//            WesterosBlockDef.processRegisterTileEntities();
//            // Initialize custom block definitions
//            for (int i = 0; i < customBlocks.length; i++) {
//                if (customBlocks[i] instanceof WesterosBlockLifecycle) {
//                    ((WesterosBlockLifecycle)customBlocks[i]).initializeBlockDefinition();
//                }
//            }
            
            // Register block models
            WesterosBlocks.proxy.registerBlockModels(customBlocks);
            
            proxy.initRenderRegistry();            
        }
    }
    
    public static Block findBlockByName(String blkname) {
        Block blk = customBlocksByName.get(blkname);
        if (blk != null) {
            return blk;
        }
        blk = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blkname));
        return blk;
    }
}
