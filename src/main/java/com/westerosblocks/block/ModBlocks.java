package com.westerosblocks.block;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.WesterosCreativeModeTabs;
import com.westerosblocks.block.custom.BlockBuilder;
import com.westerosblocks.block.custom.WCFireBlock;
import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.data.BlockDefinitionRegistry;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
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

    static {
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
                        .states(definition.hasStates() ? definition.getStates().size() : 0)
                        .build();

                case "door":
                    return BlockBuilder.door()
                        .strength(definition.getStrength())
                        .resistance(definition.getResistance())
                        .requiresTool()
                        .sounds(soundGroup)
                        .woodType(getWoodTypeFromDefinition(definition))
                        .locked(definition.isLocked())
                        .allowUnsupported(definition.isAllowUnsupported())
                        .build();

                case "log":
                    return BlockBuilder.log()
                        .strength(definition.getStrength())
                        .resistance(definition.getResistance())
                        .requiresTool()
                        .sounds(soundGroup)
                        .build();

                case "plant":
                    return BlockBuilder.plant()
                        .strength(definition.getStrength())
                        .resistance(definition.getResistance())
                        .sounds(soundGroup)
                        .nonOpaque()
                        .noCollision()
                        .layerSensitive(true)
                        .build();

                case "flowerpot":
                    return BlockBuilder.flowerPot()
                        .strength(definition.getStrength())
                        .resistance(definition.getResistance())
                        .sounds(soundGroup)
                        .nonOpaque()
                        .build();

                case "web":
                    return BlockBuilder.web()
                        .strength(definition.getStrength())
                        .resistance(definition.getResistance())
                        .sounds(soundGroup)
                        .nonOpaque()
                        .noCollision()
                        .build();

                case "slab":
                    return BlockBuilder.slab()
                        .strength(definition.getStrength())
                        .resistance(definition.getResistance())
                        .requiresTool()
                        .sounds(soundGroup)
                        .build();

                case "halfdoor":
                    return BlockBuilder.halfDoor()
                        .strength(definition.getStrength())
                        .resistance(definition.getResistance())
                        .requiresTool()
                        .sounds(soundGroup)
                        .locked(definition.isLocked())
                        .allowUnsupported(definition.isAllowUnsupported())
                        .build();

                case "fire":
                    return BlockBuilder.fire()
                        .strength(0.0f)
                        .sounds(soundGroup)
                        .luminance(state -> definition.getLuminance())
                        .noCollision()
                        .breakInstantly()
                        .nonOpaque()
                        .build();

                case "ladder":
                    return BlockBuilder.ladder()
                        .strength(definition.getStrength())
                        .resistance(definition.getResistance())
                        .requiresTool()
                        .sounds(soundGroup)
                        .nonOpaque()
                        .allowUnsupported(definition.isAllowUnsupported())
                        .build();

                case "vines":
                    return BlockBuilder.vines()
                        .strength(definition.getStrength())
                        .resistance(definition.getResistance())
                        .sounds(soundGroup)
                        .nonOpaque()
                        .allowUnsupported()
                        .noClimb()
                        .canGrowDownward()
                        .noCollision(definition.hasNoCollision())
                        .build();

                case "pane":
                    return BlockBuilder.pane()
                        .strength(definition.getStrength())
                        .resistance(definition.getResistance())
                        .requiresTool()
                        .sounds(soundGroup)
                        .nonOpaque()
                        .legacyModel(true)
                        .unconnect(false)
                        .build();

                case "fence":
                    return BlockBuilder.fence()
                        .strength(definition.getStrength())
                        .resistance(definition.getResistance())
                        .requiresTool()
                        .sounds(soundGroup)
                        .build();

                case "fencegate":
                    return BlockBuilder.fenceGate()
                        .strength(definition.getStrength())
                        .resistance(definition.getResistance())
                        .requiresTool()
                        .sounds(soundGroup)
                        .woodType(getWoodTypeFromDefinition(definition))
                        .locked(definition.isLocked())
                        .build();

                case "leaves":
                    return BlockBuilder.leaves()
                        .strength(definition.getStrength())
                        .resistance(definition.getResistance())
                        .requiresTool()
                        .sounds(soundGroup)
                        .nonOpaque()
                        .noDecay()
                        .build();

                case "bed":
                    return BlockBuilder.bed()
                        .strength(definition.getStrength())
                        .resistance(definition.getResistance())
                        .requiresTool()
                        .sounds(soundGroup)
                        .nonOpaque()
                        .build();

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

    // Table Blocks
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
//
//    // Branch Blocks
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

    /**
     * Initialize all blocks
     */
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

    private static Block registerBlockWithoutBlockItem(String name, Block block) {
        return Registry.register(Registries.BLOCK, Identifier.of(WesterosBlocks.MOD_ID, name), block);
    }
}
