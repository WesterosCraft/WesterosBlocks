package com.westerosblocks;

import com.westerosblocks.block.ModBlocks;
import com.westerosblocks.block.blockentity.ModBlockEntities;
import com.westerosblocks.config.ModConfig;
import com.westerosblocks.data.BlockDefinitionRegistry;
import com.westerosblocks.data.WorldPainterExporter;
import com.westerosblocks.entity.ModEntities;
import com.westerosblocks.item.ModItems;
import com.westerosblocks.particle.ModParticles;
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
        CONFIG = ModConfig.load();

        initializeBlockDefinitions();

        WesterosCreativeModeTabs.registerCreativeModeTabs();
        ModBlocks.registerModBlocks();
        ModItems.registerModItems();
        ModSounds.registerSounds();
        ModParticles.registerParticles();
        ModBlockEntities.registerModBlockEntities();
        ModEntities.registerModEntities();

        // Export WorldPainter CSV if config option is enabled
        if (CONFIG.dumpWorldPainterCSV) {
            LOGGER.info("WorldPainter CSV export enabled in config");
            WorldPainterExporter.exportToCSV();
        }

        LOGGER.info("WesterosBlocks mod initialization complete!");
    }

    private void initializeBlockDefinitions() {
        try {
            // Load block definitions and block set definitions from resources directory
            String blockDefinitionsPath = "definitions/block_definitions";
            String blockSetDefinitionsPath = "definitions/block_set_definitions";

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