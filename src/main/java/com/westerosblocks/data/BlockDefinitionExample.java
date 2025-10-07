package com.westerosblocks.data;

import com.westerosblocks.WesterosBlocks;

import java.util.List;

public class BlockDefinitionExample {

    public static void demonstrateUsage() {
        BlockDefinitionRegistry registry = BlockDefinitionRegistry.getInstance();

        if (!registry.isInitialized()) {
            WesterosBlocks.LOGGER.warn("Registry not initialized - cannot demonstrate usage");
            return;
        }

        WesterosBlocks.LOGGER.info("=== Block Definition Registry Usage Examples ===");

        // Example 1: Get a specific block definition
        BlockDefinition birchBlock = registry.getDefinition("6sided_birch");
        if (birchBlock != null) {
            WesterosBlocks.LOGGER.info("Found block: {} ({})", birchBlock.getLabel(), birchBlock.getBlockType());
            WesterosBlocks.LOGGER.info("  Textures: {}", birchBlock.getTextures());
        }

        // Example 2: Get all blocks of a specific type
        List<BlockDefinition> solidBlocks = registry.getByType("solid");
        WesterosBlocks.LOGGER.info("Found {} solid blocks", solidBlocks.size());

        List<BlockDefinition> doorBlocks = registry.getByType("door");
        WesterosBlocks.LOGGER.info("Found {} door blocks", doorBlocks.size());

        List<BlockDefinition> slabBlocks = registry.getByType("slab");
        WesterosBlocks.LOGGER.info("Found {} slab blocks", slabBlocks.size());

        List<BlockDefinition> allowUnsupportedBlocks = registry.getWithAllowUnsupported();
        WesterosBlocks.LOGGER.info("Found {} blocks with allow-unsupported", allowUnsupportedBlocks.size());

        // Example 4: Loop through all definitions for automated processing
        WesterosBlocks.LOGGER.info("=== Automated Block Registration Example ===");
        int processedCount = 0;
        for (BlockDefinition definition : registry.getAllDefinitions()) {
            // This is where you would call your block registration logic
            // For example: registerBlock(definition);
            processedCount++;

            // Show a few examples
            if (processedCount <= 5) {
                WesterosBlocks.LOGGER.info("Would register: {} ({})",
                    definition.getBlockName(), definition.getBlockType());
            }
        }
        WesterosBlocks.LOGGER.info("Could automatically register {} blocks total", processedCount);
    }

    public static void showTypeBreakdown() {
        BlockDefinitionRegistry registry = BlockDefinitionRegistry.getInstance();

        if (!registry.isInitialized()) {
            return;
        }

        WesterosBlocks.LOGGER.info("=== Block Type Breakdown ===");
        for (String blockType : registry.getAllBlockTypes()) {
            int count = registry.getCountByType(blockType);
            WesterosBlocks.LOGGER.info("{}: {} blocks", blockType, count);

            // Show a few examples of each type
            List<BlockDefinition> examples = registry.getByType(blockType);
            int shown = 0;
            for (BlockDefinition def : examples) {
                if (shown < 3) {
                    WesterosBlocks.LOGGER.info("  - {} ({})", def.getBlockName(), def.getLabel());
                    shown++;
                } else if (shown == 3) {
                    WesterosBlocks.LOGGER.info("  ... and {} more", examples.size() - 3);
                    break;
                }
            }
        }
    }
}