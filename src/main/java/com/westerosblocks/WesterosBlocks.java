package com.westerosblocks;

import com.westerosblocks.block.ModBlocks;
import com.westerosblocks.block.blockentity.ModBlockEntities;
import com.westerosblocks.config.ModConfig;
import com.westerosblocks.data.BlockDefinitionRegistry;
import com.westerosblocks.data.WorldPaintExporter;
import com.westerosblocks.item.ModItems;
import com.westerosblocks.sound.ModSounds;
import net.fabricmc.api.ModInitializer;

import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WesterosBlocks implements ModInitializer {
    public static final String MOD_ID = "westerosblocks";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static ModConfig CONFIG;

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing WesterosBlocks mod...");

        // Load config
        CONFIG = ModConfig.load();
        LOGGER.info("Config loaded successfully");

        // Initialize block definitions registry first
        initializeBlockDefinitions();

        WesterosCreativeModeTabs.registerCreativeModeTabs();
        ModBlocks.registerModBlocks();
        ModItems.registerModItems();
        ModSounds.registerSounds();
        ModBlockEntities.registerModEntities();

        // Export WorldPainter CSV if config option is enabled
        if (CONFIG.dumpWorldPainterCSV) {
            LOGGER.info("WorldPainter CSV export enabled in config");
            WorldPaintExporter.exportToCSV();
        }

        LOGGER.info("WesterosBlocks mod initialization complete!");
    }

    private void initializeBlockDefinitions() {
        try {
            // Load block definitions and block set definitions from resources directory
            String blockDefinitionsPath = "definitions/block_definitions";
            String blockSetDefinitionsPath = "definitions/block_set_definitions";
            LOGGER.info("Initializing block definitions from resources...");

            BlockDefinitionRegistry.getInstance().initialize(blockDefinitionsPath, blockSetDefinitionsPath);
            BlockDefinitionRegistry.getInstance().printStatistics();

        } catch (Exception e) {
            LOGGER.error("Failed to initialize block definitions", e);
        }
    }

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }
}