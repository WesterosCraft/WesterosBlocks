package com.westerosblocks.block;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.WesterosCreativeModeTabs;
import com.westerosblocks.block.custom.BlockBuilder;
import com.westerosblocks.block.custom.WCFireBlock;
import com.westerosblocks.block.custom.WCFenceGateBlock;
import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.data.BlockDefinitionRegistry;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.WoodType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModBlocks {

    // Storage for automatically registered blocks from JSON definitions
    private static final Map<String, Block> AUTO_REGISTERED_BLOCKS = new HashMap<>();

    // Automatic Block Registration from JSON Definitions
    static {
        // Initialize solid blocks and plant blocks first
//        SolidBlocks.initialize();
        PlantBlocks.initialize();

        // Auto-register blocks from JSON definitions
        registerBlocksFromDefinitions();
    }

    /**
     * Automatically registers blocks from JSON block definitions
     */
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

            // Loop through all block definitions
            for (BlockDefinition definition : registry.getAllDefinitions()) {
                try {
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
     * Creates a Block instance from a BlockDefinition using the BlockBuilder pattern
     */
    private static Block createBlockFromDefinition(BlockDefinition definition) {
        String blockType = definition.getBlockType();
        BlockSoundGroup soundGroup = getSoundGroupFromString(definition.getStepSound());

        try {
            switch (blockType.toLowerCase()) {
                case "solid":
                    return BlockBuilder.solid()
                        .strength(definition.getStrength())
                        .resistance(definition.getResistance())
                        .requiresTool()
                        .sounds(soundGroup)
                        .nonOpaque(definition.isNonOpaque())
                        .noCollision(definition.hasNoCollision())
                        .build();

//                case "door":
//                    return BlockBuilder.door()
//                        .strength(definition.getStrength())
//                        .resistance(definition.getResistance())
//                        .requiresTool()
//                        .sounds(soundGroup)
//                        .woodType(getWoodTypeFromDefinition(definition))
//                        .locked(false) // TODO: Extract from definition
//                        .allowUnsupported(definition.isAllowUnsupported())
//                        .build();
//
//                case "slab":
//                    return BlockBuilder.slab()
//                        .strength(definition.getStrength())
//                        .resistance(definition.getResistance())
//                        .requiresTool()
//                        .sounds(soundGroup)
//                        .build();
//
//                case "log":
//                    return BlockBuilder.log()
//                        .strength(definition.getStrength())
//                        .resistance(definition.getResistance())
//                        .requiresTool()
//                        .sounds(soundGroup)
//                        .build();
//
//                case "halfdoor":
//                    return BlockBuilder.halfDoor()
//                        .strength(definition.getStrength())
//                        .resistance(definition.getResistance())
//                        .requiresTool()
//                        .sounds(soundGroup)
//                        .locked(false) // TODO: Extract from definition
//                        .allowUnsupported(definition.isAllowUnsupported())
//                        .build();
//
//                case "pane":
//                    return BlockBuilder.pane()
//                        .strength(definition.getStrength())
//                        .resistance(definition.getResistance())
//                        .requiresTool()
//                        .sounds(soundGroup)
//                        .nonOpaque()
//                        .legacyModel(true)
//                        .unconnect(false)
//                        .build();
//
//                case "torch":
//                    return BlockBuilder.torch()
//                        .strength(definition.getStrength())
//                        .sounds(soundGroup)
//                        .luminance(state -> 13) // TODO: Extract from definition
//                        .nonOpaque()
//                        .noCollision()
//                        .allowUnsupported(true)
//                        .noParticle(false)
//                        .build();
//
//                case "chair":
//                    return BlockBuilder.chair()
//                        .strength(definition.getStrength())
//                        .resistance(definition.getResistance())
//                        .requiresTool()
//                        .sounds(soundGroup)
//                        .woodType(getWoodTypeFromDefinition(definition))
//                        .build();
//
//                case "table":
//                    return BlockBuilder.table()
//                        .strength(definition.getStrength())
//                        .resistance(definition.getResistance())
//                        .requiresTool()
//                        .sounds(soundGroup)
//                        .build();
//
//                case "branch":
//                    return BlockBuilder.branch()
//                        .strength(definition.getStrength())
//                        .resistance(definition.getResistance())
//                        .requiresTool()
//                        .sounds(soundGroup)
//                        .build();

                // Add more block types as needed
                default:
                    WesterosBlocks.LOGGER.warn("Unsupported block type '{}' for auto-registration", blockType);
                    return null;
            }
        } catch (Exception e) {
            WesterosBlocks.LOGGER.error("Error creating block from definition: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Maps string sound names to BlockSoundGroup instances
     */
    private static BlockSoundGroup getSoundGroupFromString(String soundName) {
        if (soundName == null) return BlockSoundGroup.STONE;

        switch (soundName.toLowerCase()) {
            case "wood": return BlockSoundGroup.WOOD;
            case "stone": return BlockSoundGroup.STONE;
            case "metal": return BlockSoundGroup.METAL;
            case "grass": return BlockSoundGroup.GRASS;
            case "cloth": return BlockSoundGroup.WOOL;
            case "gravel": return BlockSoundGroup.GRAVEL;
            case "glass": return BlockSoundGroup.GLASS;
            case "candle": return BlockSoundGroup.CANDLE;
            case "bone": return BlockSoundGroup.BONE;
            case "ladder": return BlockSoundGroup.LADDER;
            case "snow": return BlockSoundGroup.SNOW;
            default:
                WesterosBlocks.LOGGER.warn("Unknown sound type '{}', defaulting to STONE", soundName);
                return BlockSoundGroup.STONE;
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

    /**
     * Gets an automatically registered block by name
     */
    public static Block getAutoRegisteredBlock(String blockName) {
        return AUTO_REGISTERED_BLOCKS.get(blockName);
    }

    /**
     * Gets all automatically registered blocks
     */
    public static Map<String, Block> getAllAutoRegisteredBlocks() {
        return new HashMap<>(AUTO_REGISTERED_BLOCKS);
    }

    /**
     * Registers auto-registered blocks to their creative tabs based on JSON definitions
     */
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

                // Get the auto-registered block
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

    // Table Blocks (non-solid)
    public static final Block OAK_TABLE = registerBlock(
            "oak_table",
            BlockBuilder.table()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    // Torch Blocks
    public static final Block WALL_TORCH = registerBlockWithoutBlockItem(
            "wall_torch",
            BlockBuilder.wallTorch()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.METAL)
                    .luminance(state -> 13)
                    .nonOpaque()
                    .noCollision()
                    .allowUnsupported(true)
                    .noParticle(false)
                    .build());

    public static final Block TORCH = registerBlock(
            "torch",
            BlockBuilder.torch()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.METAL)
                    .luminance(state -> 13)
                    .nonOpaque()
                    .noCollision()
                    .wallBlock(WALL_TORCH)
                    .allowUnsupported(true)
                    .noParticle(false)
                    .build());

    public static final Block WALL_TORCH_UNLIT = registerBlockWithoutBlockItem(
            "wall_torch_unlit",
            BlockBuilder.wallTorch()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.METAL)
                    .luminance(state -> 0)
                    .nonOpaque()
                    .noCollision()
                    .allowUnsupported(true)
                    .noParticle(true)
                    .build());

    public static final Block TORCH_UNLIT = registerBlock(
            "torch_unlit",
            BlockBuilder.torch()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.METAL)
                    .luminance(state -> 0)
                    .nonOpaque()
                    .noCollision()
                    .wallBlock(WALL_TORCH_UNLIT)
                    .allowUnsupported(true)
                    .noParticle(true)
                    .build());

    public static final Block WALL_CANDLE = registerBlockWithoutBlockItem(
            "wall_candle",
            BlockBuilder.wallTorch()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.CANDLE)
                    .luminance(state -> 10)
                    .nonOpaque()
                    .noCollision()
                    .allowUnsupported(true)
                    .noParticle(false)
                    .build());

    public static final Block CANDLE = registerBlock(
            "candle",
            BlockBuilder.torch()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.CANDLE)
                    .luminance(state -> 10)
                    .nonOpaque()
                    .noCollision()
                    .wallBlock(WALL_CANDLE)
                    .allowUnsupported(true)
                    .noParticle(false)
                    .build());

    public static final Block WALL_CANDLE_UNLIT = registerBlockWithoutBlockItem(
            "wall_candle_unlit",
            BlockBuilder.wallTorch()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.CANDLE)
                    .luminance(state -> 0)
                    .nonOpaque()
                    .noCollision()
                    .allowUnsupported(true)
                    .noParticle(true)
                    .build());

    public static final Block CANDLE_UNLIT = registerBlock(
            "candle_unlit",
            BlockBuilder.torch()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.CANDLE)
                    .luminance(state -> 0)
                    .nonOpaque()
                    .noCollision()
                    .wallBlock(WALL_CANDLE_UNLIT)
                    .allowUnsupported(true)
                    .noParticle(true)
                    .build());

    // Door Blocks
    public static final Block BIRCH_DOOR = registerBlock(
            "birch_door",
            BlockBuilder.door()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .woodType("birch")
                    .locked(false)
                    .build());

    public static final Block EYRIE_WEIRWOOD_DOOR = registerBlock(
            "eyrie_weirwood_door",
            BlockBuilder.door()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .woodType("weirwood")
                    .locked(false)
                    .build());

    public static final Block GREY_WOOD_DOOR = registerBlock(
            "grey_wood_door",
            BlockBuilder.door()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .woodType("oak")
                    .locked(false)
                    .build());

    public static final Block HARRENHAL_SECRET_DOOR = registerBlock(
            "harrenhal_secret_door",
            BlockBuilder.door()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .woodType("oak")
                    .locked(true)
                    .build());

    public static final Block JUNGLE_DOOR = registerBlock(
            "jungle_door",
            BlockBuilder.door()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .woodType("jungle")
                    .locked(false)
                    .build());

    public static final Block NORTHERN_WOOD_DOOR = registerBlock(
            "northern_wood_door",
            BlockBuilder.door()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .woodType("northern")
                    .locked(false)
                    .build());

    public static final Block OAK_DOOR = registerBlock(
            "oak_door",
            BlockBuilder.door()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .woodType("oak")
                    .locked(false)
                    .build());

    public static final Block RED_KEEP_SECRET_DOOR = registerBlock(
            "red_keep_secret_door",
            BlockBuilder.door()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .woodType("oak")
                    .locked(true)
                    .build());

    public static final Block SPRUCE_DOOR = registerBlock(
            "spruce_door",
            BlockBuilder.door()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .woodType("spruce")
                    .locked(false)
                    .build());

    public static final Block WHITE_WOOD_DOOR = registerBlock(
            "white_wood_door",
            BlockBuilder.door()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .woodType("white")
                    .locked(false)
                    .build());

    public static final Block LOCKED_BIRCH_DOOR = registerBlock(
            "locked_birch_door",
            BlockBuilder.door()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .woodType("birch")
                    .locked(true)
                    .allowUnsupported(true)
                    .build());

    public static final Block LOCKED_DARK_NORTHERN_WOOD_DOOR = registerBlock(
            "locked_dark_northern_wood_door",
            BlockBuilder.door()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .woodType("northern")
                    .locked(true)
                    .allowUnsupported(true)
                    .build());

    public static final Block LOCKED_GREY_WOOD_DOOR = registerBlock(
            "locked_grey_wood_door",
            BlockBuilder.door()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .woodType("oak")
                    .locked(true)
                    .allowUnsupported(true)
                    .build());

    public static final Block LOCKED_JUNGLE_DOOR = registerBlock(
            "locked_jungle_door",
            BlockBuilder.door()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .woodType("jungle")
                    .locked(true)
                    .allowUnsupported(true)
                    .build());

    public static final Block LOCKED_OAK_DOOR = registerBlock(
            "locked_oak_door",
            BlockBuilder.door()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .woodType("oak")
                    .locked(true)
                    .allowUnsupported(true)
                    .build());

    public static final Block LOCKED_SPRUCE_DOOR = registerBlock(
            "locked_spruce_door",
            BlockBuilder.door()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .woodType("spruce")
                    .locked(true)
                    .allowUnsupported()
                    .build());

    public static final Block LOCKED_WHITE_WOOD_DOOR = registerBlock(
            "locked_white_wood_door",
            BlockBuilder.door()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .woodType("white")
                    .locked(true)
                    .allowUnsupported()
                    .build());

    // Half Door Blocks (Shutters)
    public static final Block BIRCH_WINDOW_SHUTTERS = registerBlock(
            "birch_window_shutters",
            BlockBuilder.halfDoor()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .allowUnsupported()
                    .build());

    public static final Block DORNE_RED_WINDOW_SHUTTERS = registerBlock(
            "dorne_red_window_shutters",
            BlockBuilder.halfDoor()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .allowUnsupported()
                    .build());

    public static final Block GREEN_LANNISPORT_WINDOW_SHUTTERS = registerBlock(
            "green_lannisport_window_shutters",
            BlockBuilder.halfDoor()
                    .strength(2.0f)
                    .resistance(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .allowUnsupported()
                    .build());

    public static final Block GREY_WOOD_WINDOW_SHUTTERS = registerBlock(
            "grey_wood_window_shutters",
            BlockBuilder.halfDoor()
                    .strength(2.0f)
                    .resistance(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .allowUnsupported()
                    .build());

    public static final Block JUNGLE_WINDOW_SHUTTERS = registerBlock(
            "jungle_window_shutters",
            BlockBuilder.halfDoor()
                    .strength(2.0f)
                    .resistance(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .allowUnsupported()
                    .build());

    public static final Block NORTHERN_WOOD_WINDOW_SHUTTERS = registerBlock(
            "northern_wood_window_shutters",
            BlockBuilder.halfDoor()
                    .strength(2.0f)
                    .resistance(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .allowUnsupported()
                    .build());

    public static final Block OAK_WINDOW_SHUTTERS = registerBlock(
            "oak_window_shutters",
            BlockBuilder.halfDoor()
                    .strength(2.0f)
                    .resistance(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .allowUnsupported()
                    .build());

    public static final Block REACH_BLUE_WINDOW_SHUTTERS = registerBlock(
            "reach_blue_window_shutters",
            BlockBuilder.halfDoor()
                    .strength(2.0f)
                    .resistance(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .allowUnsupported()
                    .build());

    public static final Block SPRUCE_WINDOW_SHUTTERS = registerBlock(
            "spruce_window_shutters",
            BlockBuilder.halfDoor()
                    .strength(2.0f)
                    .resistance(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .allowUnsupported()
                    .build());

    public static final Block WHITE_WOOD_WINDOW_SHUTTERS = registerBlock(
            "white_wood_window_shutters",
            BlockBuilder.halfDoor()
                    .strength(2.0f)
                    .resistance(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .locked(false)
                    .allowUnsupported(true)
                    .build());

    // Pane Blocks
    public static final Block DORNE_CARVED_STONE_WINDOW = registerBlock(
            "dorne_carved_stone_window",
            BlockBuilder.pane()
                    .strength(1.0f)
                    .resistance(3.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .nonOpaque()
                    .legacyModel(true)
                    .unconnect(false)
                    .build());

    public static final Block DORNE_CARVED_WOODEN_WINDOW = registerBlock(
            "dorne_carved_wooden_window",
            BlockBuilder.pane()
                    .strength(1.0f)
                    .resistance(3.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .nonOpaque()
                    .legacyModel(true)
                    .unconnect(false)
                    .build());

    public static final Block IRON_BARS = registerBlock(
            "iron_bars",
            BlockBuilder.pane()
                    .strength(5.0f)
                    .resistance(10.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.METAL)
                    .nonOpaque()
                    .barsModel(true)
                    .unconnect(false)
                    .build());

    public static final Block IRON_CROSSBAR = registerBlock(
            "iron_crossbar",
            BlockBuilder.pane()
                    .strength(5.0f)
                    .resistance(10.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.METAL)
                    .nonOpaque()
                    .barsModel(true)
                    .unconnect(false)
                    .build());

    public static final Block OXIDIZED_IRON_BARS = registerBlock(
            "oxidized_iron_bars",
            BlockBuilder.pane()
                    .strength(5.0f)
                    .resistance(10.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.METAL)
                    .nonOpaque()
                    .barsModel(true)
                    .unconnect(false)
                    .build());

    public static final Block OXIDIZED_IRON_CROSSBAR = registerBlock(
            "oxidized_iron_crossbar",
            BlockBuilder.pane()
                    .strength(5.0f)
                    .resistance(10.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.METAL)
                    .nonOpaque()
                    .barsModel(true)
                    .unconnect(false)
                    .build());

    public static final Block VERTICAL_NET = registerBlock(
            "vertical_net",
            BlockBuilder.pane()
                    .strength(1.0f)
                    .resistance(3.0f)
                    .sounds(BlockSoundGroup.GLASS)
                    .nonOpaque()
                    .legacyModel(true)
                    .unconnect(false)
                    .build());


    // Log blocks
    public static final Block ARCHERY_TARGET = registerBlock(
            "archery_target",
            BlockBuilder.log()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block CLOSED_BARREL = registerBlock(
            "closed_barrel",
            BlockBuilder.log()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block FIREWOOD = registerBlock(
            "firewood",
            BlockBuilder.log()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block JUNGLE_LOG_CHAIN = registerBlock(
            "jungle_log_chain",
            BlockBuilder.log()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block JUNGLE_LOG_ROPE = registerBlock(
            "jungle_log_rope",
            BlockBuilder.log()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block MARBLE_PILLAR_VERTICAL_CTM = registerBlock(
            "marble_pillar_vertical_ctm",
            BlockBuilder.log()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block MARBLE_PILLAR = registerBlock(
            "marble_pillar",
            BlockBuilder.log()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block MOSSY_BIRCH_LOG = registerBlock(
            "mossy_birch_log",
            BlockBuilder.log()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block MOSSY_JUNGLE_LOG = registerBlock(
            "mossy_jungle_log",
            BlockBuilder.log()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block MOSSY_OAK_LOG = registerBlock(
            "mossy_oak_log",
            BlockBuilder.log()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block MOSSY_SPRUCE_LOG = registerBlock(
            "mossy_spruce_log",
            BlockBuilder.log()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block OAK_LOG_CHAIN = registerBlock(
            "oak_log_chain",
            BlockBuilder.log()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block OAK_LOG_ROPE = registerBlock(
            "oak_log_rope",
            BlockBuilder.log()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block PALM_TREE_LOG = registerBlock(
            "palm_tree_log",
            BlockBuilder.log()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block SANDSTONE_PILLAR = registerBlock(
            "sandstone_pillar",
            BlockBuilder.log()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block SPRUCE_LOG_CHAIN = registerBlock(
            "spruce_log_chain",
            BlockBuilder.log()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block SPRUCE_LOG_ROPE = registerBlock(
            "spruce_log_rope",
            BlockBuilder.log()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block STACKED_BONES = registerBlock(
            "stacked_bones",
            BlockBuilder.log()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block WEIRWOOD_FACE_0 = registerBlock(
            "weirwood_face_0",
            BlockBuilder.log()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block WEIRWOOD_FACE_1 = registerBlock(
            "weirwood_face_1",
            BlockBuilder.log()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block WEIRWOOD_FACE_2 = registerBlock(
            "weirwood_face_2",
            BlockBuilder.log()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block WEIRWOOD_FACE_3 = registerBlock(
            "weirwood_face_3",
            BlockBuilder.log()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block WEIRWOOD_FACE_4 = registerBlock(
            "weirwood_face_4",
            BlockBuilder.log()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block WEIRWOOD_FACE_5 = registerBlock(
            "weirwood_face_5",
            BlockBuilder.log()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block WEIRWOOD_FACE_6 = registerBlock(
            "weirwood_face_6",
            BlockBuilder.log()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block WEIRWOOD_FACE_7 = registerBlock(
            "weirwood_face_7",
            BlockBuilder.log()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block WEIRWOOD_FACE_8 = registerBlock(
            "weirwood_face_8",
            BlockBuilder.log()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block WEIRWOOD_SCARS = registerBlock(
            "weirwood_scars",
            BlockBuilder.log()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block STRIPPED_OAK_LOG = registerBlock(
            "stripped_oak_log",
            BlockBuilder.log()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());


    // Rail blocks
    public static final Block FANCY_BLUE_CARPET = registerBlock(
            "fancy_blue_carpet",
            BlockBuilder.rail()
                    .strength(0.1f)
                    .sounds(BlockSoundGroup.WOOL)
                    .allowUnsupported(true)
                    .nonOpaque()
                    .build());

    public static final Block FANCY_RED_CARPET = registerBlock(
            "fancy_red_carpet",
            BlockBuilder.rail()
                    .strength(0.1f)
                    .sounds(BlockSoundGroup.WOOL)
                    .allowUnsupported(true)
                    .nonOpaque()
                    .build());

    public static final Block HORIZONTAL_CHAIN = registerBlock(
            "horizontal_chain",
            BlockBuilder.rail()
                    .strength(1.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.METAL)
                    .allowUnsupported(true)
                    .nonOpaque()
                    .build());

    public static final Block HORIZONTAL_NET = registerBlock(
            "horizontal_net",
            BlockBuilder.rail()
                    .strength(0.5f)
                    .sounds(BlockSoundGroup.WOOL)
                    .allowUnsupported(true)
                    .nonOpaque()
                    .build());

    public static final Block HORIZONTAL_ROPE = registerBlock(
            "horizontal_rope",
            BlockBuilder.rail()
                    .strength(0.5f)
                    .sounds(BlockSoundGroup.WOOL)
                    .allowUnsupported(true)
                    .nonOpaque()
                    .build());

    public static final Block PACKED_SNOW = registerBlock(
            "packed_snow",
            BlockBuilder.rail()
                    .strength(0.2f)
                    .sounds(BlockSoundGroup.SNOW)
                    .allowUnsupported(true)
                    .nonOpaque()
                    .build());

    // Slab blocks
    public static final Block APPLE_BASKET_SLAB = registerBlock(
            "apple_basket_slab",
            BlockBuilder.slab()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block APRICOT_BASKET_SLAB = registerBlock(
            "apricot_basket_slab",
            BlockBuilder.slab()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block CLOSED_BASKET_SLAB = registerBlock(
            "closed_basket_slab",
            BlockBuilder.slab()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block BERRY_BASKET_SLAB = registerBlock(
            "berry_basket_slab",
            BlockBuilder.slab()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block CARROT_BASKET_SLAB = registerBlock(
            "carrot_basket_slab",
            BlockBuilder.slab()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block CUT_GRAIN_FLOUR_SACK = registerBlock(
            "cut_grain_flour_sack",
            BlockBuilder.slab()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.GRAVEL)
                    .build());

    public static final Block DATE_BASKET_SLAB = registerBlock(
            "date_basket_slab",
            BlockBuilder.slab()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block FIREWOOD_SLAB = registerBlock(
            "firewood_slab",
            BlockBuilder.slab()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block FISH_BASKET_SLAB = registerBlock(
            "fish_basket_slab",
            BlockBuilder.slab()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block GRAIN_BASKET_SLAB = registerBlock(
            "grain_basket_slab",
            BlockBuilder.slab()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block GRAIN_FLOUR_SACK = registerBlock(
            "grain_flour_sack",
            BlockBuilder.slab()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.GRAVEL)
                    .build());

    public static final Block HOP_BASKET_SLAB = registerBlock(
            "hop_basket_slab",
            BlockBuilder.slab()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    // Branch Blocks
    public static final Block OAK_BRANCH = registerBlock(
            "oak_branch",
            BlockBuilder.branch()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block BIRCH_BRANCH = registerBlock(
            "birch_branch",
            BlockBuilder.branch()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    // Chair Blocks
    public static final Block OAK_CHAIR = registerBlock(
            "oak_chair",
            BlockBuilder.chair()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .woodType("oak")
                    .build());

    // Arrow Slit Blocks
    public static final Block ARBOR_BRICK_ARROW_SLIT = registerBlock(
            "arbor_brick_arrow_slit",
            BlockBuilder.arrowSlit()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    // Fan Blocks
    public static final Block WALL_CORAL_TUBE_FAN = registerBlockWithoutBlockItem(
            "wall_coral_tube_fan",
            BlockBuilder.wallFan()
                    .strength(1.0f)
                    .sounds(BlockSoundGroup.STONE)
                    .nonOpaque()
                    .noCollision()
                    .allowUnsupported(true)
                    .build());

    public static final Block CORAL_TUBE_FAN = registerBlock(
            "coral_tube_fan",
            BlockBuilder.fan()
                    .strength(1.0f)
                    .sounds(BlockSoundGroup.STONE)
                    .wallBlock(WALL_CORAL_TUBE_FAN)
                    .allowUnsupported(true)
                    .nonOpaque()
                    .noCollision()
                    .build());

    public static final Block WALL_CORAL_BRAIN_FAN = registerBlockWithoutBlockItem(
            "wall_coral_brain_fan",
            BlockBuilder.wallFan()
                    .strength(1.0f)
                    .sounds(BlockSoundGroup.STONE)
                    .nonOpaque()
                    .noCollision()
                    .allowUnsupported(true)
                    .build());

    public static final Block CORAL_BRAIN_FAN = registerBlock(
            "coral_brain_fan",
            BlockBuilder.fan()
                    .strength(1.0f)
                    .sounds(BlockSoundGroup.STONE)
                    .wallBlock(WALL_CORAL_BRAIN_FAN)
                    .allowUnsupported(true)
                    .nonOpaque()
                    .noCollision()
                    .build());

    public static final Block WALL_CORAL_BUBBLE_FAN = registerBlockWithoutBlockItem(
            "wall_coral_bubble_fan",
            BlockBuilder.wallFan()
                    .strength(1.0f)
                    .sounds(BlockSoundGroup.STONE)
                    .nonOpaque()
                    .noCollision()
                    .allowUnsupported(true)
                    .build());

    public static final Block CORAL_BUBBLE_FAN = registerBlock(
            "coral_bubble_fan",
            BlockBuilder.fan()
                    .strength(1.0f)
                    .sounds(BlockSoundGroup.STONE)
                    .wallBlock(WALL_CORAL_BUBBLE_FAN)
                    .allowUnsupported(true)
                    .nonOpaque()
                    .noCollision()
                    .build());

    public static final Block WALL_CORAL_FIRE_FAN = registerBlockWithoutBlockItem(
            "wall_coral_fire_fan",
            BlockBuilder.wallFan()
                    .strength(1.0f)
                    .sounds(BlockSoundGroup.STONE)
                    .nonOpaque()
                    .noCollision()
                    .allowUnsupported(true)
                    .build());

    public static final Block CORAL_FIRE_FAN = registerBlock(
            "coral_fire_fan",
            BlockBuilder.fan()
                    .strength(1.0f)
                    .sounds(BlockSoundGroup.STONE)
                    .wallBlock(WALL_CORAL_FIRE_FAN)
                    .allowUnsupported(true)
                    .nonOpaque()
                    .noCollision()
                    .build());

    public static final Block WALL_CORAL_HORN_FAN = registerBlockWithoutBlockItem(
            "wall_coral_horn_fan",
            BlockBuilder.wallFan()
                    .strength(1.0f)
                    .sounds(BlockSoundGroup.STONE)
                    .nonOpaque()
                    .noCollision()
                    .allowUnsupported(true)
                    .build());

    public static final Block CORAL_HORN_FAN = registerBlock(
            "coral_horn_fan",
            BlockBuilder.fan()
                    .strength(1.0f)
                    .sounds(BlockSoundGroup.STONE)
                    .wallBlock(WALL_CORAL_HORN_FAN)
                    .allowUnsupported(true)
                    .nonOpaque()
                    .noCollision()
                    .build());

    // Web Blocks
    public static final Block BEES = registerBlock(
            "bees",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .build());

    public static final Block ALYSSAS_TEARS_MIST_ONE = registerBlock(
            "alyssas_tears_mist_one",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .build());

    public static final Block ALYSSAS_TEARS_MIST_TWO = registerBlock(
            "alyssas_tears_mist_two",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .connectState(true)
                    .build());

    public static final Block ALYSSAS_TEARS_MIST_THREE = registerBlock(
            "alyssas_tears_mist_three",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .connectState(true)
                    .build());

    public static final Block ALYSSAS_TEARS_MIST_FOUR = registerBlock(
            "alyssas_tears_mist_four",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .connectState(true)
                    .build());

    public static final Block BLACK_BRICICLE = registerBlock(
            "black_bricicle",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.STONE)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .build());

    public static final Block BUSHEL_OF_HERBS = registerBlock(
            "bushel_of_herbs",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .build());

    public static final Block BUSHEL_OF_SOURLEAF = registerBlock(
            "bushel_of_sourleaf",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .build());

    public static final Block BUTTERFLY_BLUE = registerBlock(
            "butterfly_blue",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .build());

    public static final Block BUTTERFLY_ORANGE = registerBlock(
            "butterfly_orange",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .build());

    public static final Block BUTTERFLY_RED = registerBlock(
            "butterfly_red",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .build());

    public static final Block BUTTERFLY_WHITE = registerBlock(
            "butterfly_white",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .build());

    public static final Block BUTTERFLY_YELLOW = registerBlock(
            "butterfly_yellow",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .build());

    public static final Block CHAIN_BLOCK_HARNESS = registerBlock(
            "chain_block_harness",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .build());

    public static final Block CHILI_RISTRA = registerBlock(
            "chili_ristra",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .build());

    public static final Block COBWEB = registerBlock(
            "cobweb",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .toggleOnUse()
                    .build());

    public static final Block DEAD_FISH = registerBlock(
            "dead_fish",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .build());

    public static final Block DEAD_FOWL = registerBlock(
            "dead_fowl",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .build());

    public static final Block DEAD_FROG = registerBlock(
            "dead_frog",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .build());

    public static final Block DEAD_HARE = registerBlock(
            "dead_hare",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .build());

    public static final Block DEAD_JUNGLE_TALL_GRASS = registerBlock(
            "dead_jungle_tall_grass",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .layerSensitive()
                    .build());

    public static final Block DEAD_RAT = registerBlock(
            "dead_rat",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .build());

    public static final Block DEAD_SAVANNA_TALL_GRASS = registerBlock(
            "dead_savanna_tall_grass",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .layerSensitive()
                    .build());

    public static final Block DRAGONFLY = registerBlock(
            "dragonfly",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .build());

    public static final Block FLIES = registerBlock(
            "flies",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .build());

    public static final Block GARLIC_STRAND = registerBlock(
            "garlic_strand",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .build());

    public static final Block ICICLE = registerBlock(
            "icicle",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .build());

    public static final Block IRON_THRONE_RANDOM_BLADES = registerBlock(
            "iron_throne_random_blades",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .build());

    public static final Block JUNGLE_TALL_FERN = registerBlock(
            "jungle_tall_fern",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .layerSensitive()
                    .build());

    public static final Block JUNGLE_TALL_GRASS = registerBlock(
            "jungle_tall_grass",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .layerSensitive()
                    .build());

    public static final Block ROPE_BLOCK_HARNESS = registerBlock(
            "rope_block_harness",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .build());

    public static final Block SAUSAGES_LEG_OF_HAM = registerBlock(
            "sausages_leg_of_ham",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .build());

    public static final Block SAVANNA_TALL_GRASS = registerBlock(
            "savanna_tall_grass",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .layerSensitive()
                    .build());

    public static final Block SMOKE = registerBlock(
            "smoke",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .toggleOnUse()
                    .build());

    public static final Block VERTICAL_CHAIN = registerBlock(
            "vertical_chain",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .build());

    public static final Block VERTICAL_ROPE = registerBlock(
            "vertical_rope",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .build());

    // Fire Blocks
    public static final Block SAFE_FIRE = registerBlock(
            "safe_fire",
            new WCFireBlock.Factory().buildBlockClass(
                    AbstractBlock.Settings.create()
                            .strength(0.0f)
                            .luminance(state -> 1)
                            .sounds(BlockSoundGroup.WOOL)
                            .noCollision()
                            .breakInstantly()
                            .nonOpaque()
            ));

    public static final Block WILDFIRE = registerBlock(
            "wildfire",
            new WCFireBlock.Factory().buildBlockClass(
                    AbstractBlock.Settings.create()
                            .strength(0.0f)
                            .luminance(state -> 9)
                            .sounds(BlockSoundGroup.CANDLE)
                            .noCollision()
                            .breakInstantly()
                            .nonOpaque()
            ));

    // Particle Emitter Blocks
    public static final Block CASCADE_PARTICLE_EMITTER = registerBlock(
            "cascade_particle_emitter",
            BlockBuilder.particleEmitter()
                    .strength(-1.0f)
                    .resistance(3600000.0f)
                    .sounds(BlockSoundGroup.AMETHYST_BLOCK)
                    .noCollision()
                    .nonOpaque()
                    .particle("cascade")
                    .build());

    public static final Block COSY_SMOKE_PARTICLE_EMITTER = registerBlock(
            "cosy_smoke_particle_emitter",
            BlockBuilder.particleEmitter()
                    .strength(-1.0f)
                    .resistance(3600000.0f)
                    .sounds(BlockSoundGroup.AMETHYST_BLOCK)
                    .noCollision()
                    .nonOpaque()
                    .particle("cosy_smoke")
                    .build());

    public static final Block SIGNAL_SMOKE_PARTICLE_EMITTER = registerBlock(
            "signal_smoke_particle_emitter",
            BlockBuilder.particleEmitter()
                    .strength(-1.0f)
                    .resistance(3600000.0f)
                    .sounds(BlockSoundGroup.AMETHYST_BLOCK)
                    .noCollision()
                    .nonOpaque()
                    .particle("signal_smoke")
                    .build());

    // Crop Blocks
    public static final Block CROP_CARROTS = registerBlock(
            "crop_carrots",
            BlockBuilder.crop()
                    .strength(0.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.GRASS)
                    .toggleOnUse()
                    .stateValues(List.of("age0", "age1", "age2", "age3"))
                    .build());

    public static final Block CANDLE_ALTAR = registerBlock(
            "candle_altar",
            BlockBuilder.crop()
                    .strength(0.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.METAL)
                    .toggleOnUse()
                    .stateValues(List.of("lit", "unlit"))
                    .build());

    public static final Block CROP_PEAS = registerBlock(
            "crop_peas",
            BlockBuilder.crop()
                    .strength(0.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.GRASS)
                    .toggleOnUse()
                    .layerSensitive()
                    .stateValues(List.of("age0", "age1", "age2"))
                    .build());

    public static final Block CROP_TURNIPS = registerBlock(
            "crop_turnips",
            BlockBuilder.crop()
                    .strength(0.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.GRASS)
                    .toggleOnUse()
                    .layerSensitive()
                    .stateValues(List.of("age0", "age1", "age2", "age3"))
                    .build());

    public static final Block CROP_WHEAT = registerBlock(
            "crop_wheat",
            BlockBuilder.crop()
                    .strength(0.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.GRASS)
                    .toggleOnUse()
                    .layerSensitive()
                    .stateValues(List.of("age0", "age1", "age2", "age3", "age4", "age5", "age6", "age7"))
                    .build());

    public static final Block SEAGRASS = registerBlock(
            "seagrass",
            BlockBuilder.crop()
                    .strength(0.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.GRASS)
                    .toggleOnUse()
                    .layerSensitive()
                    .build());

    // Flowerbed Blocks
    public static final Block CLOVER = registerBlock(
            "clover",
            BlockBuilder.flowerbed()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .build());

    // Bed Blocks
    public static final Block ITCHY_STRAW_BED = registerBlock(
            "itchy_straw_bed",
            BlockBuilder.bed()
                    .strength(0.2f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .build());

    public static final Block HAMMOCK = registerBlock(
            "hammock",
            BlockBuilder.bed()
                    .strength(0.2f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .build());

    public static final Block NIGHTS_WATCH_BED = registerBlock(
            "nights_watch_bed",
            BlockBuilder.bed()
                    .strength(0.2f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .build());

    public static final Block NOBLE_BLUE_BED = registerBlock(
            "noble_blue_bed",
            BlockBuilder.bed()
                    .strength(0.2f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .build());

    public static final Block NOBLE_RED_BED = registerBlock(
            "noble_red_bed",
            BlockBuilder.bed()
                    .strength(0.2f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .build());

    public static final Block NORTHERN_BED = registerBlock(
            "northern_bed",
            BlockBuilder.bed()
                    .strength(0.2f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .build());

    public static final Block PALE_GREEN_BED = registerBlock(
            "pale_green_bed",
            BlockBuilder.bed()
                    .strength(0.2f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .build());

    public static final Block PALE_RED_BED = registerBlock(
            "pale_red_bed",
            BlockBuilder.bed()
                    .strength(0.2f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .build());

    public static final Block STRAW_BED = registerBlock(
            "straw_bed",
            BlockBuilder.bed()
                    .strength(0.2f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .build());

    // Leaves block
    public static final Block APPLE_FRUIT_LEAVES = registerBlock(
            "apple_fruit_leaves",
            BlockBuilder.leaves()
                    .strength(0.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.GRASS)
                    .noDecay()
                    .nonOpaque()
                    .build());

    public static final Block APRICOT_FRUIT_LEAVES = registerBlock(
            "apricot_fruit_leaves",
            BlockBuilder.leaves()
                    .strength(0.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.GRASS)
                    .noDecay()
                    .nonOpaque()
                    .build());

    public static final Block BLACKBERRY_BUSH = registerBlock(
            "blackberry_bush",
            BlockBuilder.leaves()
                    .strength(0.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.GRASS)
                    .noDecay()
                    .nonOpaque()
                    .build());

    public static final Block BLUEBERRY_BUSH = registerBlock(
            "blueberry_bush",
            BlockBuilder.leaves()
                    .strength(0.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.GRASS)
                    .noDecay()
                    .nonOpaque()
                    .build());

    public static final Block CHERRY_FRUIT_LEAVES = registerBlock(
            "cherry_fruit_leaves",
            BlockBuilder.leaves()
                    .strength(0.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.GRASS)
                    .noDecay()
                    .nonOpaque()
                    .build());

    public static final Block HOP_FRUIT_LEAVES = registerBlock(
            "hop_fruit_leaves",
            BlockBuilder.leaves()
                    .strength(0.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.GRASS)
                    .noDecay()
                    .nonOpaque()
                    .build());

    public static final Block JUNIPER_BUSH = registerBlock(
            "juniper_bush",
            BlockBuilder.leaves()
                    .strength(0.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.GRASS)
                    .noDecay()
                    .nonOpaque()
                    .build());

    public static final Block LEMON_FRUIT_LEAVES = registerBlock(
            "lemon_fruit_leaves",
            BlockBuilder.leaves()
                    .strength(0.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.GRASS)
                    .noDecay()
                    .nonOpaque()
                    .build());

    public static final Block LIME_FRUIT_LEAVES = registerBlock(
            "lime_fruit_leaves",
            BlockBuilder.leaves()
                    .strength(0.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.GRASS)
                    .noDecay()
                    .nonOpaque()
                    .build());

    public static final Block OLIVE_FRUIT_LEAVES = registerBlock(
            "olive_fruit_leaves",
            BlockBuilder.leaves()
                    .strength(0.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.GRASS)
                    .noDecay()
                    .nonOpaque()
                    .build());

    public static final Block ORANGE_FRUIT_LEAVES = registerBlock(
            "orange_fruit_leaves",
            BlockBuilder.leaves()
                    .strength(0.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.GRASS)
                    .noDecay()
                    .nonOpaque()
                    .build());

    public static final Block PALM_LEAVES = registerBlock(
            "palm_leaves",
            BlockBuilder.leaves()
                    .strength(0.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.GRASS)
                    .noDecay()
                    .nonOpaque()
                    .build());

    public static final Block PEACH_FRUIT_LEAVES = registerBlock(
            "peach_fruit_leaves",
            BlockBuilder.leaves()
                    .strength(0.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.GRASS)
                    .noDecay()
                    .nonOpaque()
                    .build());

    public static final Block PINK_ROSE_BUSH = registerBlock(
            "pink_rose_bush",
            BlockBuilder.leaves()
                    .strength(0.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.GRASS)
                    .noDecay()
                    .nonOpaque()
                    .build());

    public static final Block PLUM_FRUIT_LEAVES = registerBlock(
            "plum_fruit_leaves",
            BlockBuilder.leaves()
                    .strength(0.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.GRASS)
                    .noDecay()
                    .nonOpaque()
                    .build());

    public static final Block POMEGRANATE_FRUIT_LEAVES = registerBlock(
            "pomegranate_fruit_leaves",
            BlockBuilder.leaves()
                    .strength(0.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.GRASS)
                    .noDecay()
                    .nonOpaque()
                    .build());

    public static final Block PURPLE_GRAPE_FRUIT_LEAVES = registerBlock(
            "purple_grape_fruit_leaves",
            BlockBuilder.leaves()
                    .strength(0.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.GRASS)
                    .noDecay()
                    .nonOpaque()
                    .build());

    public static final Block RASPBERRY_BUSH = registerBlock(
            "raspberry_bush",
            BlockBuilder.leaves()
                    .strength(0.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.GRASS)
                    .noDecay()
                    .nonOpaque()
                    .build());

    public static final Block RED_ROSE_BUSH = registerBlock(
            "red_rose_bush",
            BlockBuilder.leaves()
                    .strength(0.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.GRASS)
                    .noDecay()
                    .nonOpaque()
                    .build());

    public static final Block SNOWY_SPRUCE_LEAVES = registerBlock(
            "snowy_spruce_leaves",
            BlockBuilder.leaves()
                    .strength(0.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.GRASS)
                    .noDecay()
                    .nonOpaque()
                    .build());

    public static final Block SNOWY_WEIRWOOD_LEAVES = registerBlock(
            "snowy_weirwood_leaves",
            BlockBuilder.leaves()
                    .strength(0.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.GRASS)
                    .noDecay()
                    .nonOpaque()
                    .build());

    public static final Block WEIRWOOD_LEAVES = registerBlock(
            "weirwood_leaves",
            BlockBuilder.leaves()
                    .strength(0.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.GRASS)
                    .noDecay()
                    .nonOpaque()
                    .build());

    public static final Block WHITE_GRAPE_FRUIT_LEAVES = registerBlock(
            "white_grape_fruit_leaves",
            BlockBuilder.leaves()
                    .strength(0.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.GRASS)
                    .noDecay()
                    .nonOpaque()
                    .build());

    public static final Block WHITE_ROSE_BUSH = registerBlock(
            "white_rose_bush",
            BlockBuilder.leaves()
                    .strength(0.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.GRASS)
                    .noDecay()
                    .nonOpaque()
                    .build());

    public static final Block YELLOW_ROSE_BUSH = registerBlock(
            "yellow_rose_bush",
            BlockBuilder.leaves()
                    .strength(0.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.GRASS)
                    .noDecay()
                    .nonOpaque()
                    .build());

    // vines block
    public static final Block DAPPLED_MOSS = registerBlock(
            "dappled_moss",
            BlockBuilder.vines()
                    .strength(0.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WET_GRASS)
                    .nonOpaque()
                    .allowUnsupported()
                    .noClimb()
                    .canGrowDownward()
                    .build());

    public static final Block FALLING_WATER_BLOCK_FOUR = registerBlock(
            "falling_water_block_four",
            BlockBuilder.vines()
                    .strength(0.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WET_GRASS)
                    .nonOpaque()
                    .noCollision()
                    .allowUnsupported()
                    .noClimb()
                    .build());

    public static final Block FALLING_WATER_BLOCK_THREE = registerBlock(
            "falling_water_block_three",
            BlockBuilder.vines()
                    .strength(0.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WET_GRASS)
                    .nonOpaque()
                    .noCollision()
                    .allowUnsupported()
                    .noClimb()
                    .build());

    public static final Block FALLING_WATER_BLOCK_TWO = registerBlock(
            "falling_water_block_two",
            BlockBuilder.vines()
                    .strength(0.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WET_GRASS)
                    .nonOpaque()
                    .noCollision()
                    .allowUnsupported()
                    .noClimb()
                    .build());

    public static final Block FALLING_WATER_BLOCK_ONE = registerBlock(
            "falling_water_block_one",
            BlockBuilder.vines()
                    .strength(0.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WET_GRASS)
                    .nonOpaque()
                    .allowUnsupported()
                    .noCollision()
                    .noClimb()
                    .build());

    public static final Block JASMINE_VINES = registerBlock(
            "jasmine_vines",
            BlockBuilder.vines()
                    .strength(0.2f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .allowUnsupported()
                    .noClimb()
                    .canGrowDownward()
                    .build());

    public static final Block VINES = registerBlock(
            "vines",
            BlockBuilder.vines()
                    .strength(0.2f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .allowUnsupported()
                    .noClimb()
                    .canGrowDownward()
                    .build());

    // Ladder blocks

    public static final Block IRON_RUNGS = registerBlock(
            "iron_rungs",
            BlockBuilder.ladder()
                    .strength(0.8f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.METAL)
                    .nonOpaque()
                    .allowUnsupported()
                    .build());

    public static final Block IRON_RUNGS_BROKEN = registerBlock(
            "iron_rungs_broken",
            BlockBuilder.ladder()
                    .strength(0.8f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.METAL)
                    .nonOpaque()
                    .allowUnsupported()
                    .build());

    public static final Block ROPE_LADDER = registerBlock(
            "rope_ladder",
            BlockBuilder.ladder()
                    .strength(0.4f)
                    .sounds(BlockSoundGroup.LADDER)
                    .nonOpaque()
                    .allowUnsupported()
                    .build());

    public static final Block VINE_JASMINE = registerBlock(
            "vine_jasmine",
            BlockBuilder.ladder()
                    .strength(0.4f)
                    .sounds(BlockSoundGroup.LADDER)
                    .nonOpaque()
                    .allowUnsupported()
                    .build());

    public static final Block WINTERFELL_STONE_LADDER = registerBlock(
            "winterfell_stone_ladder",
            BlockBuilder.ladder()
                    .strength(1.5f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .nonOpaque()
                    .allowUnsupported()
                    .build());

    public static final Block WOOD_LADDER = registerBlock(
            "wood_ladder",
            BlockBuilder.ladder()
                    .strength(0.4f)
                    .sounds(BlockSoundGroup.LADDER)
                    .nonOpaque()
                    .allowUnsupported()
                    .build());

    // Fence Blocks
    public static final Block BIRCH_BARK_FENCE = registerBlock(
            "birch_bark_fence",
            BlockBuilder.fence()
                    .strength(2.0f)
                    .resistance(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block BIRCH_FENCE_WITH_GRAPES = registerBlock(
            "birch_fence_with_grapes",
            BlockBuilder.fence()
                    .strength(2.0f)
                    .resistance(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block BIRCH_FENCE_WITH_VINES = registerBlock(
            "birch_fence_with_vines",
            BlockBuilder.fence()
                    .strength(2.0f)
                    .resistance(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block JUNGLE_BARK_FENCE = registerBlock(
            "jungle_bark_fence",
            BlockBuilder.fence()
                    .strength(2.0f)
                    .resistance(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block JUNGLE_FENCE_WITH_GRAPES = registerBlock(
            "jungle_fence_with_grapes",
            BlockBuilder.fence()
                    .strength(2.0f)
                    .resistance(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block JUNGLE_FENCE_WITH_VINES = registerBlock(
            "jungle_fence_with_vines",
            BlockBuilder.fence()
                    .strength(2.0f)
                    .resistance(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block MARBLE_COLUMN_FENCE = registerBlock(
            "marble_column_fence",
            BlockBuilder.fence()
                    .strength(5.0f)
                    .resistance(10.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block OAK_BARK_FENCE = registerBlock(
            "oak_bark_fence",
            BlockBuilder.fence()
                    .strength(2.0f)
                    .resistance(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block OAK_FENCE_WITH_GRAPES = registerBlock(
            "oak_fence_with_grapes",
            BlockBuilder.fence()
                    .strength(2.0f)
                    .resistance(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block OAK_FENCE_WITH_VINES = registerBlock(
            "oak_fence_with_vines",
            BlockBuilder.fence()
                    .strength(2.0f)
                    .resistance(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block PALM_FENCE = registerBlock(
            "palm_fence",
            BlockBuilder.fence()
                    .strength(5.0f)
                    .resistance(10.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block REINFORCED_OAK_FENCE = registerBlock(
            "reinforced_oak_fence",
            BlockBuilder.fence()
                    .strength(5.0f)
                    .resistance(10.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block SEPT_CRYSTAL_SMALL = registerBlock(
            "sept_crystal_small",
            BlockBuilder.fence()
                    .strength(5.0f)
                    .resistance(10.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.GLASS)
                    .nonOpaque()
                    .build());

    public static final Block SPRUCE_BARK_FENCE = registerBlock(
            "spruce_bark_fence",
            BlockBuilder.fence()
                    .strength(2.0f)
                    .resistance(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block SPRUCE_FENCE_WITH_GRAPES = registerBlock(
            "spruce_fence_with_grapes",
            BlockBuilder.fence()
                    .strength(2.0f)
                    .resistance(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block SPRUCE_FENCE_WITH_VINES = registerBlock(
            "spruce_fence_with_vines",
            BlockBuilder.fence()
                    .strength(2.0f)
                    .resistance(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block STACKED_BONES_FENCE = registerBlock(
            "stacked_bones_fence",
            BlockBuilder.fence()
                    .strength(5.0f)
                    .resistance(10.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.BONE)
                    .build());

    // Fence Gate Blocks
    public static final Block LOCKED_BIRCH_BARK_FENCE_GATE = registerBlock(
            "locked_birch_bark_fence_gate",
            BlockBuilder.fenceGate()
                    .strength(2.0f)
                    .resistance(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .woodType("birch")
                    .locked()
                    .build());

    public static final Block LOCKED_JUNGLE_BARK_FENCE_GATE = registerBlock(
            "locked_jungle_bark_fence_gate",
            BlockBuilder.fenceGate()
                    .strength(2.0f)
                    .resistance(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .woodType("jungle")
                    .locked()
                    .build());

    public static final Block LOCKED_OAK_BARK_FENCE_GATE = registerBlock(
            "locked_oak_bark_fence_gate",
            BlockBuilder.fenceGate()
                    .strength(2.0f)
                    .resistance(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .woodType("oak")
                    .locked()
                    .build());

    public static final Block LOCKED_SPRUCE_BARK_FENCE_GATE = registerBlock(
            "locked_spruce_bark_fence_gate",
            BlockBuilder.fenceGate()
                    .strength(2.0f)
                    .resistance(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .woodType("spruce")
                    .locked()
                    .build());

    /**
     * Initialize all blocks
     */
    public static void registerModBlocks() {
        WesterosBlocks.LOGGER.info("Registering Mod Blocks for " + WesterosBlocks.MOD_ID);

        // Register auto-registered blocks to their creative tabs
        registerAutoBlocksToCreativeTabs();

        WesterosCreativeModeTabs.addToTab("westeros_grasses_shrubs_tab",
                PlantBlocks.BRACKEN,
                PlantBlocks.POTTED_BRACKEN,
                PlantBlocks.BROWN_MUSHROOM_1,
                PlantBlocks.BROWN_MUSHROOM_2,
                PlantBlocks.BROWN_MUSHROOM_3,
                PlantBlocks.BROWN_MUSHROOM_4,
                PlantBlocks.BROWN_MUSHROOM_5,
                PlantBlocks.BROWN_MUSHROOM_6,
                PlantBlocks.BROWN_MUSHROOM_7,
                PlantBlocks.BROWN_MUSHROOM_8,
                PlantBlocks.BROWN_MUSHROOM_9,
                PlantBlocks.BROWN_MUSHROOM_10,
                PlantBlocks.BROWN_MUSHROOM_11,
                PlantBlocks.BROWN_MUSHROOM_12,
                PlantBlocks.BROWN_MUSHROOM_13,
                PlantBlocks.COW_PARSELY,
                PlantBlocks.DEAD_BRACKEN,
                PlantBlocks.DEAD_BUSH,
                PlantBlocks.DEAD_SCRUB_GRASS,
                PlantBlocks.FIREWEED,
                PlantBlocks.GRASS,
                PlantBlocks.GREEN_LEAFY_HERB,
                PlantBlocks.GREEN_SCRUB_GRASS,
                PlantBlocks.GREEN_SPINY_HERB,
                PlantBlocks.HEATHER,
                PlantBlocks.LADY_FERN,
                PlantBlocks.MEADOW_FESCUE,
                PlantBlocks.NETTLE,
                PlantBlocks.RED_FERN,
                PlantBlocks.RED_MUSHROOM_1,
                PlantBlocks.RED_MUSHROOM_2,
                PlantBlocks.RED_MUSHROOM_3,
                PlantBlocks.RED_MUSHROOM_4,
                PlantBlocks.RED_MUSHROOM_5,
                PlantBlocks.RED_MUSHROOM_6,
                PlantBlocks.RED_MUSHROOM_7,
                PlantBlocks.RED_MUSHROOM_8,
                PlantBlocks.RED_MUSHROOM_9,
                PlantBlocks.THICK_GRASS,
                PlantBlocks.UNSHADED_GRASS,
                PlantBlocks.CATTAILS,
                ModBlocks.DEAD_JUNGLE_TALL_GRASS,
                ModBlocks.DEAD_SAVANNA_TALL_GRASS,
                ModBlocks.CLOVER
        );

        WesterosCreativeModeTabs.addToTab("westeros_water_air_tab",
                PlantBlocks.CORAL_BRAIN_WEB,
                PlantBlocks.CORAL_BUBBLE_WEB,
                PlantBlocks.CORAL_FIRE_WEB,
                PlantBlocks.CORAL_HORN_WEB,
                PlantBlocks.CORAL_TUBE_WEB,
                PlantBlocks.KELP,
                ModBlocks.CORAL_TUBE_FAN,
                ModBlocks.CORAL_BRAIN_FAN,
                ModBlocks.CORAL_BUBBLE_FAN,
                ModBlocks.CORAL_FIRE_FAN,
                ModBlocks.CORAL_HORN_FAN,
                ModBlocks.PACKED_SNOW,
                ModBlocks.ALYSSAS_TEARS_MIST_ONE,
                ModBlocks.ALYSSAS_TEARS_MIST_TWO,
                ModBlocks.ALYSSAS_TEARS_MIST_THREE,
                ModBlocks.ALYSSAS_TEARS_MIST_FOUR,
                ModBlocks.SEAGRASS,
                ModBlocks.FALLING_WATER_BLOCK_ONE,
                ModBlocks.FALLING_WATER_BLOCK_TWO,
                ModBlocks.FALLING_WATER_BLOCK_THREE,
                ModBlocks.FALLING_WATER_BLOCK_FOUR
        );

        WesterosCreativeModeTabs.addToTab("westeros_crops_herbs_tab",
                PlantBlocks.CRANBERRY_BUSH,
                PlantBlocks.DOCK_LEAF,
                PlantBlocks.RED_SOURLEAF_BUSH,
                PlantBlocks.STRAWBERRY_BUSH,
                ModBlocks.BUSHEL_OF_SOURLEAF,
                ModBlocks.CROP_CARROTS,
                ModBlocks.CROP_PEAS,
                ModBlocks.CROP_TURNIPS,
                ModBlocks.CROP_WHEAT
        );

        WesterosCreativeModeTabs.addToTab("westeros_flowers_tab",
                PlantBlocks.BLUE_BELLS,
                PlantBlocks.POTTED_BLUE_BELLS,
                PlantBlocks.BLUE_CHICORY,
                PlantBlocks.POTTED_BLUE_CHICORY,
                PlantBlocks.BLUE_FORGETMENOTS,
                PlantBlocks.POTTED_BLUE_FORGETMENOTS,
                PlantBlocks.BLUE_FLAX,
                PlantBlocks.POTTED_BLUE_FLAX,
                PlantBlocks.BLUE_HYACINTH,
                PlantBlocks.POTTED_BLUE_HYACINTH,
                PlantBlocks.BLUE_ORCHID,
                PlantBlocks.POTTED_BLUE_ORCHID,
                PlantBlocks.BLUE_SWAMP_BELLS,
                PlantBlocks.POTTED_BLUE_SWAMP_BELLS,
                PlantBlocks.POTTED_BROWN_MUSHROOM_1,
                PlantBlocks.POTTED_BROWN_MUSHROOM_3,
                PlantBlocks.POTTED_BROWN_MUSHROOM_6,
                PlantBlocks.POTTED_BROWN_MUSHROOM_13,
                PlantBlocks.POTTED_CATTAILS,
                PlantBlocks.POTTED_COW_PARSELY,
                PlantBlocks.POTTED_DEAD_BRACKEN,
                PlantBlocks.POTTED_DEAD_BUSH,
                PlantBlocks.POTTED_DEAD_SCRUB_GRASS,
                PlantBlocks.POTTED_DOCK_LEAF,
                PlantBlocks.POTTED_FIREWEED,
                PlantBlocks.POTTED_GRASS,
                PlantBlocks.POTTED_GREEN_LEAFY_HERB,
                PlantBlocks.POTTED_GREEN_SCRUB_GRASS,
                PlantBlocks.POTTED_GREEN_SPINY_HERB,
                PlantBlocks.POTTED_HEATHER,
                PlantBlocks.POTTED_LADY_FERN,
                PlantBlocks.POTTED_MAGENTA_ROSES,
                PlantBlocks.POTTED_MEADOW_FESCUE,
                PlantBlocks.POTTED_NETTLE,
                PlantBlocks.POTTED_ORANGE_BELLS,
                PlantBlocks.POTTED_ORANGE_BOG_ASPHODEL,
                PlantBlocks.POTTED_ORANGE_MARIGOLDS,
                PlantBlocks.POTTED_ORANGE_SUN_STAR,
                PlantBlocks.POTTED_ORANGE_TROLLIUS,
                PlantBlocks.POTTED_PINK_ALLIUM,
                PlantBlocks.POTTED_PINK_PRIMROSE,
                PlantBlocks.POTTED_PINK_ROSES,
                PlantBlocks.POTTED_PINK_SWEET_PEAS,
                PlantBlocks.POTTED_PINK_THISTLE,
                PlantBlocks.POTTED_PINK_TULIPS,
                PlantBlocks.POTTED_PINK_WILDFLOWERS,
                PlantBlocks.POTTED_PURPLE_ALPINE_SOWTHISTLE,
                PlantBlocks.POTTED_PURPLE_FOXGLOVE,
                PlantBlocks.POTTED_PURPLE_LAVENDER,
                PlantBlocks.POTTED_PURPLE_PANSIES,
                PlantBlocks.POTTED_PURPLE_ROSES,
                PlantBlocks.POTTED_PURPLE_VIOLETS,
                PlantBlocks.POTTED_RED_ASTER,
                PlantBlocks.POTTED_RED_CARNATIONS,
                PlantBlocks.POTTED_RED_CHRYSANTHEMUM,
                PlantBlocks.POTTED_RED_DARK_ROSES,
                PlantBlocks.POTTED_RED_FERN,
                PlantBlocks.POTTED_RED_FLOWERING_SPINY_HERB,
                PlantBlocks.POTTED_RED_MUSHROOM_1,
                PlantBlocks.POTTED_RED_MUSHROOM_2,
                PlantBlocks.POTTED_RED_MUSHROOM_3,
                PlantBlocks.POTTED_RED_MUSHROOM_7,
                PlantBlocks.POTTED_RED_MUSHROOM_8,
                PlantBlocks.POTTED_RED_MUSHROOM_9,
                PlantBlocks.POTTED_RED_POPPIES,
                PlantBlocks.POTTED_RED_ROSES,
                PlantBlocks.POTTED_RED_SORREL,
                PlantBlocks.POTTED_RED_SOURLEAF_BUSH,
                PlantBlocks.POTTED_RED_TULIPS,
                PlantBlocks.POTTED_WHITE_CHAMOMILE,
                PlantBlocks.POTTED_WHITE_DAISIES,
                PlantBlocks.POTTED_WHITE_LILYOFTHEVALLEY,
                PlantBlocks.POTTED_WHITE_PEONY,
                PlantBlocks.POTTED_WHITE_ROSES,
                PlantBlocks.POTTED_YELLOW_BEDSTRAW,
                PlantBlocks.POTTED_YELLOW_BELLS,
                PlantBlocks.POTTED_YELLOW_BUTTERCUPS,
                PlantBlocks.POTTED_YELLOW_DAFFODILS,
                PlantBlocks.POTTED_YELLOW_DAISIES,
                PlantBlocks.POTTED_YELLOW_DANDELIONS,
                PlantBlocks.POTTED_YELLOW_HELLEBORE,
                PlantBlocks.POTTED_YELLOW_LUPINE,
                PlantBlocks.POTTED_YELLOW_ROSES,
                PlantBlocks.POTTED_YELLOW_RUDBECKIA,
                PlantBlocks.POTTED_YELLOW_SUNFLOWER,
                PlantBlocks.POTTED_YELLOW_TANSY,
                PlantBlocks.POTTED_YELLOW_WILDFLOWERS,
                PlantBlocks.MAGENTA_ROSES,
                PlantBlocks.ORANGE_BELLS,
                PlantBlocks.ORANGE_BOG_ASPHODEL,
                PlantBlocks.ORANGE_MARIGOLDS,
                PlantBlocks.ORANGE_SUN_STAR,
                PlantBlocks.ORANGE_TROLLIUS,
                PlantBlocks.PINK_ALLIUM,
                PlantBlocks.PINK_PRIMROSE,
                PlantBlocks.PINK_ROSES,
                PlantBlocks.PINK_SWEET_PEAS,
                PlantBlocks.PINK_THISTLE,
                PlantBlocks.PINK_TULIPS,
                PlantBlocks.PINK_WILDFLOWERS,
                PlantBlocks.RED_ASTER,
                PlantBlocks.RED_CARNATIONS,
                PlantBlocks.RED_CHRYSANTHEMUM,
                PlantBlocks.RED_DARK_ROSES,
                PlantBlocks.RED_FLOWERING_SPINY_HERB,
                PlantBlocks.RED_POPPIES,
                PlantBlocks.RED_ROSES,
                PlantBlocks.RED_SORREL,
                PlantBlocks.RED_TULIPS,
                PlantBlocks.WHITE_CHAMOMILE,
                PlantBlocks.WHITE_DAISIES,
                PlantBlocks.WHITE_LILYOFTHEVALLEY,
                PlantBlocks.WHITE_PEONY,
                PlantBlocks.WHITE_ROSES,
                PlantBlocks.YELLOW_BEDSTRAW,
                PlantBlocks.YELLOW_BELLS,
                PlantBlocks.YELLOW_BUTTERCUPS,
                PlantBlocks.YELLOW_DAFFODILS,
                PlantBlocks.YELLOW_DAISIES,
                PlantBlocks.YELLOW_DANDELIONS,
                PlantBlocks.YELLOW_HELLEBORE,
                PlantBlocks.YELLOW_LUPINE,
                PlantBlocks.YELLOW_ROSES,
                PlantBlocks.YELLOW_RUDBECKIA,
                PlantBlocks.YELLOW_SUNFLOWER,
                PlantBlocks.YELLOW_TANSY,
                PlantBlocks.YELLOW_WILDFLOWERS
        );

        WesterosCreativeModeTabs.addToTab("westeros_logs_tab",
//                SolidBlocks.SIX_SIDED_BIRCH,
//                SolidBlocks.SIX_SIDED_JUNGLE,
//                SolidBlocks.SIX_SIDED_OAK,
//                SolidBlocks.SIX_SIDED_SPRUCE,
                ModBlocks.WEIRWOOD_FACE_0,
                ModBlocks.WEIRWOOD_FACE_1,
                ModBlocks.WEIRWOOD_FACE_2,
                ModBlocks.WEIRWOOD_FACE_3,
                ModBlocks.WEIRWOOD_FACE_4,
                ModBlocks.WEIRWOOD_FACE_5,
                ModBlocks.WEIRWOOD_FACE_6,
                ModBlocks.WEIRWOOD_FACE_7,
                ModBlocks.WEIRWOOD_FACE_8,
                ModBlocks.WEIRWOOD_SCARS,
                ModBlocks.OAK_BRANCH,
                ModBlocks.BIRCH_BRANCH,
                ModBlocks.JUNGLE_LOG_CHAIN,
                ModBlocks.JUNGLE_LOG_ROPE,
                ModBlocks.MOSSY_BIRCH_LOG,
                ModBlocks.MOSSY_JUNGLE_LOG,
                ModBlocks.MOSSY_OAK_LOG,
                ModBlocks.MOSSY_SPRUCE_LOG,
                ModBlocks.OAK_LOG_CHAIN,
                ModBlocks.OAK_LOG_ROPE,
                ModBlocks.PALM_TREE_LOG,
                ModBlocks.SPRUCE_LOG_CHAIN,
                ModBlocks.SPRUCE_LOG_ROPE,
                ModBlocks.STRIPPED_OAK_LOG,
                ModBlocks.BIRCH_BARK_FENCE,
                ModBlocks.JUNGLE_BARK_FENCE,
                ModBlocks.OAK_BARK_FENCE,
                ModBlocks.PALM_FENCE,
                ModBlocks.SPRUCE_BARK_FENCE,
                ModBlocks.LOCKED_BIRCH_BARK_FENCE_GATE
        );

//        WesterosCreativeModeTabs.addToTab("westeros_half_ashlar_tab",
//                SolidBlocks.SIX_SIDED_STONE_SLAB
//        );

        WesterosCreativeModeTabs.addToTab("westeros_food_blocks_tab",
//                SolidBlocks.APPLE_BASKET,
//                SolidBlocks.APPLE_CRATE,
//                SolidBlocks.APRICOT_BASKET,
//                SolidBlocks.BERRY_BASKET,
//                SolidBlocks.BERRY_CRATE,
//                SolidBlocks.CARROT_BASKET,
//                SolidBlocks.CARROT_CRATE,
//                SolidBlocks.DATE_BASKET,
//                SolidBlocks.DATES,
//                SolidBlocks.FISH_BARREL,
//                SolidBlocks.FISH_BASKET,
//                SolidBlocks.FISH_TRAP,
//                SolidBlocks.GRAIN_BASKET,
//                SolidBlocks.GRAIN_CRATE,
//                SolidBlocks.HOP_BASKET,
//                SolidBlocks.HOP_CRATE,
//                SolidBlocks.LAVENDER_BASKET,
//                SolidBlocks.LAVENDER_CRATE,
//                SolidBlocks.LEMON_BASKET,
//                SolidBlocks.LIME_BASKET,
                ModBlocks.APPLE_BASKET_SLAB,
                ModBlocks.APRICOT_BASKET_SLAB,
                ModBlocks.CLOSED_BASKET_SLAB,
                ModBlocks.BERRY_BASKET_SLAB,
                ModBlocks.CARROT_BASKET_SLAB,
                ModBlocks.CUT_GRAIN_FLOUR_SACK,
                ModBlocks.DATE_BASKET_SLAB,
//                SolidBlocks.OLIVE_BASKET,
//                SolidBlocks.ORANGE_BASKET,
//                SolidBlocks.POMEGRANATE_BASKET,
//                SolidBlocks.PURPLE_GRAPE_BASKET,
//                SolidBlocks.PURPLE_GRAPE_CRATE,
//                SolidBlocks.SALT_CRATE,
//                SolidBlocks.SOURLEAF_BASKET,
//                SolidBlocks.SOURLEAF_CRATE,
//                SolidBlocks.SPIT_ROAST,
//                SolidBlocks.SQUASH,
//                SolidBlocks.TURNIP_BASKET,
//                SolidBlocks.TURNIP_CRATE,
//                SolidBlocks.WHITE_GRAPE_BASKET,
//                SolidBlocks.WHITE_GRAPE_CRATE,
                ModBlocks.FISH_BASKET_SLAB,
                ModBlocks.GRAIN_BASKET_SLAB,
                ModBlocks.GRAIN_FLOUR_SACK,
                ModBlocks.HOP_BASKET_SLAB,
                ModBlocks.BUSHEL_OF_HERBS,
                ModBlocks.CHILI_RISTRA,
                ModBlocks.DEAD_FISH,
                ModBlocks.DEAD_FOWL,
                ModBlocks.DEAD_FROG,
                ModBlocks.DEAD_HARE,
                ModBlocks.DEAD_RAT
        );

//        WesterosCreativeModeTabs.addToTab("westeros_utility_tab",
//                SolidBlocks.APPROVAL_UTILITY_BLOCK,
//                SolidBlocks.DONE_UTILITY_BLOCK,
//                SolidBlocks.DOMESTIC_UTILITY_BLOCK,
//                SolidBlocks.HIGH_CLASS_UTILITY_BLOCK,
//                SolidBlocks.HOUSE_COUNT_UTILITY_BLOCK,
//                SolidBlocks.INDUSTRY_UTILITY_BLOCK,
//                SolidBlocks.LOW_CLASS_UTILITY_BLOCK,
//                SolidBlocks.MIDDLE_CLASS_UTILITY_BLOCK,
//                SolidBlocks.NOTE_UTILITY_BLOCK,
//                SolidBlocks.SHOP_UTILITY_BLOCK,
//                SolidBlocks.SPECIAL_UTILITY_BLOCK,
//                SolidBlocks.WIP_UTILITY_BLOCK,
//                SolidBlocks.WORKSHOP_UTILITY_BLOCK,
//                SolidBlocks.YARD_UTILITY_BLOCK
//        );

        WesterosCreativeModeTabs.addToTab("westeros_panelling_carvings_tab",
//                SolidBlocks.ARBOR_BRICK_ORNATE,
//                SolidBlocks.BLACK_BRICK_ENGRAVED,
//                SolidBlocks.BLUEGREEN_CARVED_SANDSTONE,
//                SolidBlocks.BROWN_GREY_BRICK_ENGRAVED,
//                SolidBlocks.COARSE_DARK_RED_CARVED_SANDSTONE,
//                SolidBlocks.COARSE_RED_CARVED_SANDSTONE,
//                SolidBlocks.COBBLE_KEYSTONE,
//                SolidBlocks.DARK_GREY_BRICK_ENGRAVED,
//                SolidBlocks.DESERT_SANDSTONE_ENGRAVED,
//                SolidBlocks.DRAGON_CARVING,
//                SolidBlocks.FAITH_CARVED_ARBOR_BRICK,
//                SolidBlocks.FAITH_CARVED_BLACK_BRICK,
//                SolidBlocks.FAITH_CARVED_BROWN_GREY_BRICK,
//                SolidBlocks.FAITH_CARVED_COARSE_RED_BRICK,
//                SolidBlocks.FAITH_CARVED_DARK_GREY_BRICK,
//                SolidBlocks.FAITH_CARVED_DUN_BRICK,
//                SolidBlocks.FAITH_CARVED_GREY_BRICK,
//                SolidBlocks.FAITH_CARVED_OLDTOWN_BRICK,
//                SolidBlocks.FAITH_CARVED_PINK_SANDSTONE,
//                SolidBlocks.FAITH_CARVED_REACH_BRICK,
//                SolidBlocks.FAITH_CARVED_SMALL_STONE_BRICK,
//                SolidBlocks.FAITH_CARVED_STONE_BRICK,
//                SolidBlocks.FAITH_CARVED_STORMLANDS_BRICK,
//                SolidBlocks.FAITH_CARVED_WESTERLANDS_BRICK,
//                SolidBlocks.GREEN_GREY_BRICK_ENGRAVED,
//                SolidBlocks.GREY_BRICK_ENGRAVED,
//                SolidBlocks.GREY_KEYSTONE,
//                SolidBlocks.KL_DUN_CARVED_BRICK,
//                SolidBlocks.LIGHT_GREY_BRICK_ENGRAVED,
//                SolidBlocks.LIGHT_OLDTOWN_BRICK_ENGRAVED,
//                SolidBlocks.MONOCHROME_DARK_SANDSTONE_ENGRAVED,
//                SolidBlocks.MONOCHROME_SANDSTONE_ENGRAVED,
//                SolidBlocks.NETHER_BRICK_KEYSTONE,
//                SolidBlocks.NORTHERN_CARVINGS,
//                SolidBlocks.ORNATE_MARBLE,
//                SolidBlocks.ORNATE_SANDSTONE,
//                SolidBlocks.PINK_SANDSTONE_ENGRAVED,
//                SolidBlocks.REACH_BRICK_ENGRAVED,
//                SolidBlocks.REACH_OAK_WOOD_PANELLING,
//                SolidBlocks.REACH_SPRUCE_WOOD_PANELLING,
//                SolidBlocks.REDORANGE_CARVED_SANDSTONE,
//                SolidBlocks.SMALL_ORANGE_BRICKS_ORNATE_TOP,
//                SolidBlocks.SMALL_ORANGE_BRICKS_ORNATE,
//                SolidBlocks.STORMLANDS_BRICK_ENGRAVED,
//                SolidBlocks.TERRACOTTA_ENGRAVED,
//                SolidBlocks.VIVID_DARK_SANDSTONE_ENGRAVED,
//                SolidBlocks.VIVID_SANDSTONE_ENGRAVED,
//                SolidBlocks.WHITE_BRICK_ENGRAVED,
//                SolidBlocks.WINTERFELL_CARVING,
                ModBlocks.SANDSTONE_PILLAR
        );

        WesterosCreativeModeTabs.addToTab("westeros_furniture_tab",
//                SolidBlocks.BENCH_BUTCHER_KNIVES,
//                SolidBlocks.BENCH_CARPENTRY_HAMMER_SAW,
//                SolidBlocks.BENCH_DRAWERS,
//                SolidBlocks.BENCH_KITCHEN_KNIVES,
//                SolidBlocks.BENCH_KITCHEN_PANS,
//                SolidBlocks.BENCH_MASON_HAMMER_MALLET,
//                SolidBlocks.BOOKSHELF_ABANDONED,
//                SolidBlocks.BOOKSHELF_LIBRARY,
//                SolidBlocks.BOOKSHELF_MAESTER,
//                SolidBlocks.BROKEN_CABINET,
//                SolidBlocks.CABINET_DRAWER,
//                SolidBlocks.EMPTY_CABINET,
//                SolidBlocks.FULL_CABINET,
//                SolidBlocks.MIRROR_BLOCK,
                ModBlocks.OAK_TABLE,
                ModBlocks.OAK_CHAIR,
//                SolidBlocks.TABLE_BOOKS,
//                SolidBlocks.TABLE_DRAWERS,
//                SolidBlocks.TABLE_WIDGETS,
                ModBlocks.ITCHY_STRAW_BED,
                ModBlocks.HAMMOCK,
                ModBlocks.NIGHTS_WATCH_BED,
                ModBlocks.NOBLE_BLUE_BED,
                ModBlocks.NOBLE_RED_BED,
                ModBlocks.NORTHERN_BED,
                ModBlocks.PALE_GREEN_BED,
                ModBlocks.PALE_RED_BED,
                ModBlocks.STRAW_BED
        );

        WesterosCreativeModeTabs.addToTab("westeros_foliage_tab",
                ModBlocks.VINE_JASMINE,
                ModBlocks.APPLE_FRUIT_LEAVES,
                ModBlocks.APRICOT_FRUIT_LEAVES,
                ModBlocks.BLACKBERRY_BUSH,
                ModBlocks.BLUEBERRY_BUSH,
                ModBlocks.CHERRY_FRUIT_LEAVES,
                ModBlocks.HOP_FRUIT_LEAVES,
                ModBlocks.JUNIPER_BUSH,
                ModBlocks.LEMON_FRUIT_LEAVES,
                ModBlocks.LIME_FRUIT_LEAVES,
                ModBlocks.OLIVE_FRUIT_LEAVES,
                ModBlocks.ORANGE_FRUIT_LEAVES,
                ModBlocks.PALM_LEAVES,
                ModBlocks.PEACH_FRUIT_LEAVES,
                ModBlocks.PINK_ROSE_BUSH,
                ModBlocks.PLUM_FRUIT_LEAVES,
                ModBlocks.POMEGRANATE_FRUIT_LEAVES,
                ModBlocks.PURPLE_GRAPE_FRUIT_LEAVES,
                ModBlocks.RASPBERRY_BUSH,
                ModBlocks.RED_ROSE_BUSH,
                ModBlocks.SNOWY_SPRUCE_LEAVES,
                ModBlocks.SNOWY_WEIRWOOD_LEAVES,
                ModBlocks.WEIRWOOD_LEAVES,
                ModBlocks.WHITE_GRAPE_FRUIT_LEAVES,
                ModBlocks.WHITE_ROSE_BUSH,
                ModBlocks.YELLOW_ROSE_BUSH,
                ModBlocks.DAPPLED_MOSS,
                ModBlocks.JASMINE_VINES,
                ModBlocks.VINES
        );

//        WesterosCreativeModeTabs.addToTab("westeros_grass_dirt_tab",
//                SolidBlocks.BONE_DIRT,
//                SolidBlocks.THICK_GRASS_BLOCK
//        );

        WesterosCreativeModeTabs.addToTab("westeros_decor_tab",
//                SolidBlocks.CAGE,
//                SolidBlocks.CLOSED_BASKET,
//                SolidBlocks.CLOSED_CABINET,
//                SolidBlocks.CRATE,
//                SolidBlocks.CRATE2,
//                SolidBlocks.CRATE3,
//                SolidBlocks.EMPTY_BARREL,
//                SolidBlocks.IRON_CRATE,
//                SolidBlocks.LARGE_CLAY_POT_SOLID,
//                SolidBlocks.OPEN_BASKET,
//                SolidBlocks.OPEN_CRATE,
//                SolidBlocks.SILVER_TIN_CRATE,
//                SolidBlocks.WATER_BARREL,
                ModBlocks.CLOSED_BARREL,
                ModBlocks.FIREWOOD,
                ModBlocks.FIREWOOD_SLAB,
                ModBlocks.COBWEB,
                ModBlocks.SEPT_CRYSTAL_SMALL,
                ModBlocks.CASCADE_PARTICLE_EMITTER,
                ModBlocks.COSY_SMOKE_PARTICLE_EMITTER,
                ModBlocks.SIGNAL_SMOKE_PARTICLE_EMITTER
        );

//        WesterosCreativeModeTabs.addToTab("westeros_cobblestone_tab",
//                SolidBlocks.FLAGSTONE,
//                SolidBlocks.SANDY_STONE_SLABS
//        );

        WesterosCreativeModeTabs.addToTab("westeros_wood_planks_tab",
                ModBlocks.WOOD_LADDER,
//                SolidBlocks.PARQUET_FLOOR,
                ModBlocks.BIRCH_DOOR,
                ModBlocks.EYRIE_WEIRWOOD_DOOR,
                ModBlocks.GREY_WOOD_DOOR,
                ModBlocks.JUNGLE_DOOR,
                ModBlocks.NORTHERN_WOOD_DOOR,
                ModBlocks.OAK_DOOR,
                ModBlocks.SPRUCE_DOOR,
                ModBlocks.WHITE_WOOD_DOOR,
                ModBlocks.LOCKED_BIRCH_DOOR,
                ModBlocks.LOCKED_DARK_NORTHERN_WOOD_DOOR,
                ModBlocks.LOCKED_GREY_WOOD_DOOR,
                ModBlocks.LOCKED_JUNGLE_DOOR,
                ModBlocks.LOCKED_OAK_DOOR,
                ModBlocks.LOCKED_SPRUCE_DOOR,
                ModBlocks.LOCKED_WHITE_WOOD_DOOR,
                ModBlocks.BIRCH_FENCE_WITH_GRAPES,
                ModBlocks.BIRCH_FENCE_WITH_VINES,
                ModBlocks.JUNGLE_FENCE_WITH_GRAPES,
                ModBlocks.JUNGLE_FENCE_WITH_VINES,
                ModBlocks.OAK_FENCE_WITH_GRAPES,
                ModBlocks.OAK_FENCE_WITH_VINES,
                ModBlocks.REINFORCED_OAK_FENCE,
                ModBlocks.SPRUCE_FENCE_WITH_GRAPES,
                ModBlocks.SPRUCE_FENCE_WITH_VINES
        );


        WesterosCreativeModeTabs.addToTab("westeros_windows_glass_tab",
//                SolidBlocks.COLOURED_SEPT_WINDOW,
//                SolidBlocks.SEPT_CRYSTAL_LARGE,
                ModBlocks.BIRCH_WINDOW_SHUTTERS,
                ModBlocks.DORNE_RED_WINDOW_SHUTTERS,
                ModBlocks.GREEN_LANNISPORT_WINDOW_SHUTTERS,
                ModBlocks.GREY_WOOD_WINDOW_SHUTTERS,
                ModBlocks.JUNGLE_WINDOW_SHUTTERS,
                ModBlocks.NORTHERN_WOOD_WINDOW_SHUTTERS,
                ModBlocks.OAK_WINDOW_SHUTTERS,
                ModBlocks.REACH_BLUE_WINDOW_SHUTTERS,
                ModBlocks.SPRUCE_WINDOW_SHUTTERS,
                ModBlocks.WHITE_WOOD_WINDOW_SHUTTERS,
                ModBlocks.DORNE_CARVED_STONE_WINDOW,
                ModBlocks.DORNE_CARVED_WOODEN_WINDOW
        );


//        WesterosCreativeModeTabs.addToTab("westeros_brick_tab",
//                SolidBlocks.ORANGE_BRICK_ARCH_DOUBLE,
//                SolidBlocks.ORANGE_BRICK_ARCH_SINGLE,
//                SolidBlocks.ORANGE_BRICK_DENTIL,
//                SolidBlocks.ORANGE_BRICK_ROWLOCK,
//                SolidBlocks.SOUTHERN_BRICK_ARCH_FLAT,
//                SolidBlocks.SOUTHERN_BRICK_ARCH,
//                SolidBlocks.SOUTHERN_BRICK_LINTEL
//        );

//        WesterosCreativeModeTabs.addToTab("westeros_timber_frame_tab",
//                SolidBlocks.TIMBER_NORTHERN_BLUE_BRESSUMMER,
//                SolidBlocks.TIMBER_NORTHERN_GREEN_LEFTHATCH
//        );

        WesterosCreativeModeTabs.addToTab("westeros_marble_plaster_tab",
//                SolidBlocks.LANNISPORT_KEYSTONE_ORANGE_PLASTER,
//                SolidBlocks.LANNISPORT_KEYSTONE_YELLOW_PLASTER,
//                SolidBlocks.LIGHT_GREY_STONE_WHITE_PLASTER,
//                SolidBlocks.SMALL_SMOOTH_STONE_BRICK_BLUE_PLASTER,
//                SolidBlocks.SMALL_SMOOTH_STONE_BRICK_WHITE_PLASTER,
//                SolidBlocks.SMALL_STONE_BRICK_WHITE_PLASTER,
//                SolidBlocks.SMALL_WHITE_BRICK_BROWNISH_WHITE_PLASTER,
//                SolidBlocks.SMALL_WHITE_BRICK_WHITE_PLASTER,
//                SolidBlocks.UNUSED_BROWN_PLASTER,
//                SolidBlocks.UNUSED_PURPLE_PLASTER,
                ModBlocks.MARBLE_PILLAR_VERTICAL_CTM,
                ModBlocks.MARBLE_PILLAR,
                ModBlocks.MARBLE_COLUMN_FENCE
        );


//        WesterosCreativeModeTabs.addToTab("westeros_sand_gravel_tab",
//                SolidBlocks.YELLOW_STAINED_CLAY
//        );


//        WesterosCreativeModeTabs.addToTab("westeros_tool_blocks_tab",
//                SolidBlocks.PISTON_TOP
//        );


        WesterosCreativeModeTabs.addToTab("westeros_misc_tab",
                ModBlocks.WINTERFELL_STONE_LADDER,
//                SolidBlocks.PILED_BONES,
//                SolidBlocks.STACKED_BONES_SOLID,
                ModBlocks.HARRENHAL_SECRET_DOOR,
                ModBlocks.RED_KEEP_SECRET_DOOR,
                ModBlocks.ARCHERY_TARGET,
                ModBlocks.STACKED_BONES,
                ModBlocks.BEES,
                ModBlocks.BLACK_BRICICLE,
                ModBlocks.BUTTERFLY_BLUE,
                ModBlocks.BUTTERFLY_ORANGE,
                ModBlocks.BUTTERFLY_RED,
                ModBlocks.BUTTERFLY_WHITE,
                ModBlocks.BUTTERFLY_YELLOW,
                ModBlocks.STACKED_BONES_FENCE
        );

        WesterosCreativeModeTabs.addToTab("westeros_lighting_tab",
//                SolidBlocks.GLOWING_EMBERS,
//                SolidBlocks.RED_LANTERN2,
                ModBlocks.TORCH,
                ModBlocks.TORCH_UNLIT,
                ModBlocks.CANDLE,
                ModBlocks.CANDLE_UNLIT,
                ModBlocks.CANDLE_ALTAR,
                ModBlocks.SAFE_FIRE,
                ModBlocks.WILDFIRE
        );

        WesterosCreativeModeTabs.addToTab("westeros_metal_tab",
                ModBlocks.IRON_BARS,
                ModBlocks.IRON_CROSSBAR,
                ModBlocks.OXIDIZED_IRON_BARS,
                ModBlocks.OXIDIZED_IRON_CROSSBAR,
                ModBlocks.HORIZONTAL_CHAIN,
                ModBlocks.CHAIN_BLOCK_HARNESS,
                ModBlocks.IRON_RUNGS,
                ModBlocks.IRON_RUNGS_BROKEN
        );

        WesterosCreativeModeTabs.addToTab("westeros_cloth_fibers_tab",
                ModBlocks.VERTICAL_NET,
                ModBlocks.ROPE_LADDER,
                ModBlocks.FANCY_BLUE_CARPET,
                ModBlocks.FANCY_RED_CARPET,
                ModBlocks.HORIZONTAL_NET,
                ModBlocks.HORIZONTAL_ROPE
        );

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries -> {
            entries.add(ModBlocks.ARBOR_BRICK_ARROW_SLIT);
        });
    }

    public static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(WesterosBlocks.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(WesterosBlocks.MOD_ID, name),
                new BlockItem(block, new Item.Settings()));
    }

    private static Block registerBlockWithoutBlockItem(String name, Block block) {
        return Registry.register(Registries.BLOCK, Identifier.of(WesterosBlocks.MOD_ID, name), block);
    }
}
