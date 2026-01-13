package com.westerosblocks.block;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.WesterosCreativeModeTabs;
import com.westerosblocks.block.custom.*;
import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.data.BlockDefinitionRegistry;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModBlocks {
    private static final Map<String, Block> AUTO_REGISTERED_BLOCKS = new HashMap<>();

    static {
        registerBlocksFromDefinitions();
    }

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
                    if (!isDevelopment && "westeros_test_tab".equals(definition.getCreativeTab())) {
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
        return switch (blockType.toLowerCase()) {
            case "solid" -> new WCSolidBlock.Factory();
            case "door" -> new WCDoorBlock.Factory();
            case "halfdoor" -> new WCHalfDoorBlock.Factory();
            case "log" -> new WCLogBlock.Factory();
            case "plant" -> new WCPlantBlock.Factory();
            case "flowerpot" -> new WCFlowerPotBlock.Factory();
            case "web" -> new WCWebBlock.Factory();
            case "slab" -> new WCSlabBlock.Factory();
            case "fire" -> new WCFireBlock.Factory();
            case "ladder" -> new WCLadderBlock.Factory();
            case "vines" -> new WCVinesBlock.Factory();
            case "pane" -> new WCPaneBlock.Factory();
            case "fence" -> new WCFenceBlock.Factory();
            case "fencegate" -> new WCFenceGateBlock.Factory();
            case "trapdoor" -> new WCTrapDoorBlock.Factory();
            case "leaves" -> new WCLeavesBlock.Factory();
            case "bed" -> new WCBedBlock.Factory();
            case "crop" -> new WCCropBlock.Factory();
            case "torch" -> new WCTorchBlock.Factory();
            case "fan" -> new WCFanBlock.Factory();
            case "rail" -> new WCRailBlock.Factory();
            case "furnace" -> new WCFurnaceBlock.Factory();
            case "wall" -> new WCWallBlock.Factory();
            case "stair" -> new WCStairBlock.Factory();
            case "cuboid" -> new WCCuboidBlock.Factory();
            case "cuboid-nsew" -> new WCCuboidNSEWBlock.Factory();
            case "cuboid-nsew-stack" -> new WCCuboidNSEWStackBlock.Factory();
            case "cuboid-ne" -> new WCCuboidNEBlock.Factory();
            case "cuboid-nsewud" -> new WCCuboidNSEWUDBlock.Factory();
            case "cuboid-16way" -> new WCCuboid16WayBlock.Factory();
            case "layer" -> new WCLayerBlock.Factory();
            case "beacon" -> new WCBeaconBlock.Factory();
            case "sand" -> new WCSandBlock.Factory();
            case "soul-sand" -> new WCSoulSandBlock.Factory();
            case "particle" -> new WCParticleEmitterBlock.Factory();
            case "table" -> new WCTableBlock.Factory();
            case "table2" -> new WCTableBlock.Factory();
            case "chair" -> new WCChairBlock.Factory();
            case "bench" -> new WCBenchBlock.Factory();
            case "branch" -> new WCBranchBlock.Factory();
            case "arrow-slit" -> new WCArrowSlitBlock.Factory();
            case "flowerbed" -> new WCFlowerbedBlock.Factory();
            case "mounted" -> new WCMountedBlock.Factory();
            default -> null;
        };
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
            WesterosBlocks.LOGGER.error("Error creating block from definition: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Extracts wood type from block definition or defaults to "oak"
     */
    private static String getWoodTypeFromDefinition(BlockDefinition definition) {
        // TODO: Add wood type extraction logic from block name or properties
        // For now, default to "oak"
        String blockName = definition.getBlockName().toLowerCase();
        if (blockName.contains("birch")) return "birch";
        if (blockName.contains("spruce")) return "spruce";
        if (blockName.contains("jungle")) return "jungle";
        if (blockName.contains("oak")) return "oak";
        return "oak"; // Default
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

            // Group blocks by creative tab
            Map<String, List<Block>> blocksByTab = new HashMap<>();

            for (BlockDefinition definition : registry.getAllDefinitions()) {
                String creativeTab = definition.getCreativeTab();
                String blockName = definition.getBlockName();

                Block block = AUTO_REGISTERED_BLOCKS.get(blockName);

                if (block != null && creativeTab != null && !creativeTab.isEmpty()) {
                    blocksByTab.computeIfAbsent(creativeTab, k -> new ArrayList<>()).add(block);
                }
            }

            // Register blocks to their creative tabs
            int totalRegistered = 0;
            for (Map.Entry<String, List<Block>> entry : blocksByTab.entrySet()) {
                String tabName = entry.getKey();
                List<Block> blocks = entry.getValue();

                if (!blocks.isEmpty()) {
                    Block[] blockArray = blocks.toArray(new Block[0]);
                    WesterosCreativeModeTabs.addToTab(tabName, blockArray);

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

        registerAutoBlocksToCreativeTabs();
    }

    public static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(WesterosBlocks.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(WesterosBlocks.MOD_ID, name),
                new BlockItem(block, new Item.Settings()));
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
