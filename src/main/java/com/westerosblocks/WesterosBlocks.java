package com.westerosblocks;

import com.westerosblocks.block.ModBlocks;
import com.westerosblocks.block.blockentity.ModBlockEntities;
import com.westerosblocks.config.ModConfig;
import com.westerosblocks.data.BlockCompatibilityValidator;
import com.westerosblocks.data.BlockDefinitionRegistry;
import com.westerosblocks.data.BlockSetExporter;
import com.westerosblocks.data.KnownBlocksExporter;
import com.westerosblocks.data.WorldPainterExporter;
import com.westerosblocks.entity.ModEntities;
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
        CONFIG = ModConfig.load();

        initializeBlockDefinitions();

        WesterosCreativeModeTabs.registerCreativeModeTabs();
        ModBlocks.registerModBlocks();
        ModItems.registerModItems();
        ModSounds.registerSounds();
        ModBlockEntities.registerModBlockEntities();
        ModEntities.registerModEntities();

        if (CONFIG.dumpWorldPainterCSV) {
            LOGGER.info("WorldPainter CSV export enabled in config");
            WorldPainterExporter.exportToCSV();
        }

        if (CONFIG.exportBlockDefinitions) {
            BlockSetExporter.export();
        }

        if (CONFIG.exportKnownBlocks) {
            KnownBlocksExporter.export();
        }

        LOGGER.info("WesterosBlocks mod initialization complete!");
    }

    private void initializeBlockDefinitions() {
        try {
            BlockDefinitionRegistry registry = BlockDefinitionRegistry.getInstance();
            registry.initialize("definitions/WesterosBlocks.json");
            registry.printStatistics();

            // World-save compatibility guard: fail fast if block names/states regress against the
            // committed baseline (definitions/known_blocks.json). In export mode we only report
            // problems so a fresh baseline can be regenerated. See BlockCompatibilityValidator.
            boolean reportOnly = CONFIG != null && CONFIG.exportKnownBlocks;
            BlockCompatibilityValidator.validate(
                    registry.getAllDefinitions(), registry.getDuplicateBlockNames(), reportOnly);

        } catch (Exception e) {
            LOGGER.error("Failed to initialize block definitions", e);
            throw new RuntimeException("WesterosBlocks cannot start without block definitions", e);
        }
    }

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }
}