package com.westerosblocks.block;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.WesterosCreativeModeTabs;
import com.westerosblocks.block.custom.*;
import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.data.BlockDefinitionRegistry;

import net.fabricmc.loader.api.FabricLoader;
import com.westerosblocks.item.custom.WCBlockItem;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ModBlocks {
    private static final Map<String, Block> AUTO_REGISTERED_BLOCKS = new HashMap<>();

    private static final Map<String, BlockFactory> FACTORIES = Map.ofEntries(
        Map.entry("solid", new WCSolidBlock.Factory()),
        Map.entry("door", new WCDoorBlock.Factory()),
        Map.entry("halfdoor", new WCHalfDoorBlock.Factory()),
        Map.entry("log", new WCLogBlock.Factory()),
        Map.entry("plant", new WCPlantBlock.Factory()),
        Map.entry("flowerpot", new WCFlowerPotBlock.Factory()),
        Map.entry("web", new WCWebBlock.Factory()),
        Map.entry("slab", new WCSlabBlock.Factory()),
        Map.entry("fire", new WCFireBlock.Factory()),
        Map.entry("ladder", new WCLadderBlock.Factory()),
        Map.entry("vines", new WCVinesBlock.Factory()),
        Map.entry("pane", new WCPaneBlock.Factory()),
        Map.entry("fence", new WCFenceBlock.Factory()),
        Map.entry("fencegate", new WCFenceGateBlock.Factory()),
        Map.entry("trapdoor", new WCTrapDoorBlock.Factory()),
        Map.entry("leaves", new WCLeavesBlock.Factory()),
        Map.entry("bed", new WCBedBlock.Factory()),
        Map.entry("crop", new WCCropBlock.Factory()),
        Map.entry("torch", new WCTorchBlock.Factory()),
        Map.entry("fan", new WCFanBlock.Factory()),
        Map.entry("rail", new WCRailBlock.Factory()),
        Map.entry("furnace", new WCFurnaceBlock.Factory()),
        Map.entry("wall", new WCWallBlock.Factory()),
        Map.entry("stair", new WCStairBlock.Factory()),
        Map.entry("cuboid", new WCCuboidBlock.Factory()),
        Map.entry("cuboid-nsew", new WCCuboidNSEWBlock.Factory()),
        Map.entry("cuboid-nsew-stack", new WCCuboidNSEWStackBlock.Factory()),
        Map.entry("cuboid-ne", new WCCuboidNEBlock.Factory()),
        Map.entry("cuboid-nsewud", new WCCuboidNSEWUDBlock.Factory()),
        Map.entry("cuboid-16way", new WCCuboid16WayBlock.Factory()),
        Map.entry("layer", new WCLayerBlock.Factory()),
        Map.entry("beacon", new WCBeaconBlock.Factory()),
        Map.entry("sand", new WCSandBlock.Factory()),
        Map.entry("soul-sand", new WCSoulSandBlock.Factory()),
        Map.entry("particle", new WCParticleEmitterBlock.Factory()),
        Map.entry("table", new WCTableBlock.Factory()),
        Map.entry("chair", new WCChairBlock.Factory()),
        Map.entry("bench", new WCBenchBlock.Factory()),
        Map.entry("arrow-slit", new WCArrowSlitBlock.Factory()),
        Map.entry("flowerbed", new WCFlowerbedBlock.Factory()),
        Map.entry("mounted", new WCMountedBlock.Factory()),
        Map.entry("mounted_slab", new WCMountedSlabBlock.Factory()),
        Map.entry("beam_horizontal", new WCBeamBlock.Factory()),
        Map.entry("awning", new WCAwningBlock.Factory()),
        Map.entry("bigdoor", new WCBigDoorBlock.Factory()),
        Map.entry("bignarrowdoor", new WCBigNarrowDoorBlock.Factory()),
        Map.entry("bunting", new WCBuntingBlock.Factory()),
        Map.entry("balcony", new WCBalconyBlock.Factory()),
        Map.entry("mounted_mirror", new WCMountedMirrorBlock.Factory())
    );

    private static void registerBlocksFromDefinitions() {
        try {
            BlockDefinitionRegistry registry = BlockDefinitionRegistry.getInstance();

            if (!registry.isInitialized()) {
                WesterosBlocks.LOGGER.warn("BlockDefinitionRegistry not initialized - skipping automatic registration");
                return;
            }

            WesterosBlocks.LOGGER.info("Starting automatic block registration from JSON definitions...");
            int registeredCount = 0;
            int skippedCount = 0;

            boolean isDevelopment = FabricLoader.getInstance().isDevelopmentEnvironment();

            for (BlockDefinition definition : registry.getAllDefinitions()) {
                try {
                    // Skip test blocks in production (blocks in westeros_test_tab)
                    if (!isDevelopment && WesterosCreativeModeTabs.isTestBlock(definition)) {
                        skippedCount++;
                        WesterosBlocks.LOGGER.debug("Skipped test block (production): {}", definition.getBlockName());
                        continue;
                    }

                    Block block = createBlockFromDefinition(definition);
                    if (block != null) {
                        Block registeredBlock = registerBlock(definition.getBlockName(), block);
                        AUTO_REGISTERED_BLOCKS.put(definition.getBlockName(), registeredBlock);
                        registeredCount++;

                        WesterosBlocks.LOGGER.debug("Auto-registered block: {} ({})",
                                definition.getBlockName(), definition.getBlockType());
                    } else {
                        skippedCount++;
                        WesterosBlocks.LOGGER.warn("Skipped unsupported block type '{}' for block '{}'",
                                definition.getBlockType(), definition.getBlockName());
                    }
                } catch (Exception e) {
                    skippedCount++;
                    WesterosBlocks.LOGGER.error("Failed to register block '{}': {}",
                            definition.getBlockName(), e.getMessage());
                }
            }

            WesterosBlocks.LOGGER.info("Automatic block registration complete: {} registered, {} skipped",
                    registeredCount, skippedCount);

        } catch (Exception e) {
            WesterosBlocks.LOGGER.error("Error during automatic block registration", e);
        }
    }

    /**
     * Returns the appropriate BlockFactory for a given block type
     */
    private static BlockFactory getFactory(String blockType) {
        return FACTORIES.get(blockType.toLowerCase());
    }

    /**
     * Creates a Block instance from a BlockDefinition by calling factory directly
     */
    private static Block createBlockFromDefinition(BlockDefinition definition) {
        String blockType = definition.getBlockType().toLowerCase();

        try {
            BlockFactory factory = getFactory(blockType);
            if (factory != null) {
                return factory.buildBlockClass(definition);
            } else {
                WesterosBlocks.LOGGER.warn("No factory found for block type '{}'", blockType);
                return null;
            }
        } catch (Exception e) {
            WesterosBlocks.LOGGER.error("Error creating block from definition: {}", definition.getBlockName(), e);
            return null;
        }
    }

    public static Block getAutoRegisteredBlock(String blockName) {
        return AUTO_REGISTERED_BLOCKS.get(blockName);
    }

    public static Map<String, Block> getAllAutoRegisteredBlocks() {
        return new HashMap<>(AUTO_REGISTERED_BLOCKS);
    }

    private static void registerAutoBlocksToCreativeTabs() {
        try {
            BlockDefinitionRegistry registry = BlockDefinitionRegistry.getInstance();

            if (!registry.isInitialized()) {
                WesterosBlocks.LOGGER.warn("BlockDefinitionRegistry not initialized - skipping creative tab registration");
                return;
            }

            // Group blocks by creative tab. LinkedHashMap preserves JSON declaration
            // order so each tab lists blocks in the order their definitions were loaded
            // (file-walk order, then JSON array order within each file) rather than
            // alphabetically by label.
            Map<String, List<Block>> blocksByTab = new LinkedHashMap<>();

            for (BlockDefinition definition : registry.getAllDefinitions()) {
                String creativeTab = definition.getCreativeTab();
                String blockName = definition.getBlockName();

                Block block = AUTO_REGISTERED_BLOCKS.get(blockName);

                if (block != null && creativeTab != null && !creativeTab.isEmpty()) {
                    blocksByTab.computeIfAbsent(creativeTab, k -> new ArrayList<>())
                               .add(block);
                }
            }

            int totalRegistered = 0;
            for (Map.Entry<String, List<Block>> entry : blocksByTab.entrySet()) {
                String tabName = entry.getKey();
                List<Block> blocks = entry.getValue();

                if (!blocks.isEmpty()) {
                    WesterosCreativeModeTabs.addToTab(tabName, blocks.toArray(new Block[0]));

                    totalRegistered += blocks.size();
                    WesterosBlocks.LOGGER.info("Added {} blocks to creative tab '{}'", blocks.size(), tabName);
                }
            }

            WesterosBlocks.LOGGER.info("Successfully registered {} auto-blocks to {} creative tabs",
                    totalRegistered, blocksByTab.size());

        } catch (Exception e) {
            WesterosBlocks.LOGGER.error("Error registering auto-blocks to creative tabs", e);
        }
    }

    public static void registerModBlocks() {
        WesterosBlocks.LOGGER.info("Registering Mod Blocks for " + WesterosBlocks.MOD_ID);

        // Build and register all blocks from JSON definitions, then group them into
        // creative tabs. Called explicitly (and in order) from WesterosBlocks.onInitialize()
        // after the BlockDefinitionRegistry has been initialized — no static-init side effects.
        registerBlocksFromDefinitions();
        registerAutoBlocksToCreativeTabs();
    }

    public static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(WesterosBlocks.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(WesterosBlocks.MOD_ID, name),
                new WCBlockItem(block, new Item.Settings()));
    }

    /**
     * Registers a block without creating a BlockItem (used for wall torches, wall fans, etc.)
     * Made public so factories can register wall variants internally.
     * Also adds the block to AUTO_REGISTERED_BLOCKS for tracking.
     */
    public static Block registerBlockWithoutItem(String name, Block block) {
        Block registered = Registry.register(Registries.BLOCK, Identifier.of(WesterosBlocks.MOD_ID, name), block);
        AUTO_REGISTERED_BLOCKS.put(name, registered);
        return registered;
    }
}
