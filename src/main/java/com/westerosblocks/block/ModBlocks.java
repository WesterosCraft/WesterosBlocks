package com.westerosblocks.block;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.WesterosCreativeModeTabs;
import com.westerosblocks.block.custom.BlockBuilder;
import com.westerosblocks.block.custom.WCCropBlock;
import com.westerosblocks.block.custom.WCCuboidBlock;
import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.data.BlockDefinitionRegistry;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        BlockSoundGroup soundGroup = getSoundGroupFromString(definition.getSoundGroup());

        try {
            switch (blockType.toLowerCase()) {
                case "solid":
                    return BlockBuilder.solid()
                            .hardness(definition.getHardness())
                            .resistance(definition.getResistance())
                            .requiresTool()
                            .sounds(soundGroup)
                            .nonOpaque(definition.isNonOpaque())
                            .noCollision(definition.hasNoCollision())
                            .states(definition.hasStates() ? definition.getStates().size() : 0)
                            .build(definition);

                case "door":
                    return BlockBuilder.door()
                            .hardness(definition.getHardness())
                            .resistance(definition.getResistance())
                            .requiresTool()
                            .sounds(soundGroup)
                            .woodType(getWoodTypeFromDefinition(definition))
                            .locked(definition.isLocked())
                            .allowUnsupported(definition.isAllowUnsupported())
                            .build();

                case "log":
                    return BlockBuilder.log()
                            .hardness(definition.getHardness())
                            .resistance(definition.getResistance())
                            .requiresTool()
                            .sounds(soundGroup)
                            .build();

                case "plant":
                    return BlockBuilder.plant()
                            .hardness(definition.getHardness())
                            .resistance(definition.getResistance())
                            .sounds(soundGroup)
                            // plants are always nonopaque
                            .nonOpaque()
                            // plants are always nocollision.
                            .noCollision()
                            .layerSensitive(definition.isLayerSensitive())
                            .build();

                case "flowerpot":
                    return BlockBuilder.flowerPot()
                            .hardness(definition.getHardness())
                            .resistance(definition.getResistance())
                            .sounds(soundGroup)
                            .nonOpaque(definition.isNonOpaque())
                            .build();

                case "web":
                    return BlockBuilder.web()
                            .hardness(definition.getHardness())
                            .resistance(definition.getResistance())
                            .sounds(soundGroup)
                            .nonOpaque(definition.isNonOpaque())
                            .noCollision(definition.hasNoCollision())
                            .build();

                case "slab":
                    return BlockBuilder.slab()
                            .hardness(definition.getHardness())
                            .resistance(definition.getResistance())
                            .requiresTool()
                            .sounds(soundGroup)
                            .build();

                case "halfdoor":
                    return BlockBuilder.halfDoor()
                            .hardness(definition.getHardness())
                            .resistance(definition.getResistance())
                            .requiresTool()
                            .sounds(soundGroup)
                            .locked(definition.isLocked())
                            .allowUnsupported(definition.isAllowUnsupported())
                            .build();

                case "fire":
                    return BlockBuilder.fire()
                            .hardness(definition.getHardness())
                            .resistance(definition.getResistance())
                            .sounds(soundGroup)
                            .luminance(state -> definition.getLuminance())
                            .noCollision(definition.hasNoCollision())
                            .breakInstantly()
                            .nonOpaque(definition.isNonOpaque())
                            .build();

                case "ladder":
                    return BlockBuilder.ladder()
                            .hardness(definition.getHardness())
                            .resistance(definition.getResistance())
                            .requiresTool()
                            .sounds(soundGroup)
                            .nonOpaque(definition.isNonOpaque())
                            .allowUnsupported(definition.isAllowUnsupported())
                            .build();

                case "vines":
                    return BlockBuilder.vines()
                            .hardness(definition.getHardness())
                            .resistance(definition.getResistance())
                            .sounds(soundGroup)
                            // vines are always nonopaque
                            .nonOpaque(true)
                            .allowUnsupported(definition.isAllowUnsupported())
                            .noClimb()
                            .canGrowDownward()
                            .noCollision(definition.hasNoCollision())
                            .build();

                case "pane":
                    return BlockBuilder.pane()
                            .hardness(definition.getHardness())
                            .resistance(definition.getResistance())
                            .requiresTool()
                            .sounds(soundGroup)
                            .nonOpaque(definition.isNonOpaque())
                            .legacyModel(definition.isLegacyModel())
                            .unconnect(definition.isUnconnect())
                            .build();

                case "fence":
                    return BlockBuilder.fence()
                            .hardness(definition.getHardness())
                            .resistance(definition.getResistance())
                            .requiresTool()
                            .sounds(soundGroup)
                            .build();

                case "fencegate":
                    return BlockBuilder.fenceGate()
                            .hardness(definition.getHardness())
                            .resistance(definition.getResistance())
                            .requiresTool()
                            .sounds(soundGroup)
                            .woodType(getWoodTypeFromDefinition(definition))
                            .locked(definition.isLocked())
                            .build();

                case "leaves":
                    return BlockBuilder.leaves()
                            .hardness(definition.getHardness())
                            .resistance(definition.getResistance())
                            .requiresTool()
                            .sounds(soundGroup)
                            .nonOpaque()
                            .noDecay()
                            .build();

                case "bed":
                    return BlockBuilder.bed()
                            .hardness(definition.getHardness())
                            .resistance(definition.getResistance())
                            .requiresTool()
                            .sounds(soundGroup)
                            .nonOpaque()
                            .build();

                case "crop":
                    BlockBuilder<WCCropBlock> cropBuilder = BlockBuilder.crop()
                            .hardness(definition.getHardness())
                            .resistance(definition.getResistance())
                            .requiresTool()
                            .sounds(soundGroup)
                            .toggleOnUse(definition.toggleOnUse())
                            .layerSensitive(definition.isLayerSensitive())
                            .nonOpaque()
                            .noCollision();


                    if (definition.hasStates()) {
                        List<String> stateValues = definition.getStateValues();
                        if (stateValues != null && !stateValues.isEmpty()) {
                            cropBuilder.stateValues(stateValues);
                        }
                    }

                    return cropBuilder.build(definition);

                case "torch":
                    // First register the wall torch block
                    String wallTorchName = "wall_" + definition.getBlockName();
                    Block wallTorchBlock = BlockBuilder.wallTorch()
                            .hardness(definition.getHardness())
                            .resistance(definition.getResistance())
                            .sounds(soundGroup)
                            .luminance(state -> definition.getLuminance())
                            .nonOpaque(definition.isNonOpaque())
                            .noCollision(definition.hasNoCollision())
                            .allowUnsupported(definition.isAllowUnsupported())
                            .noParticle(definition.isNoParticle())
                            .build();

                    // Register the wall torch without block item
                    registerBlockWithoutBlockItem(wallTorchName, wallTorchBlock);

                    // Now create the standing torch with reference to wall torch
                    return BlockBuilder.torch()
                            .hardness(definition.getHardness())
                            .resistance(definition.getResistance())
                            .sounds(soundGroup)
                            .luminance(state -> definition.getLuminance())
                            .nonOpaque(definition.isNonOpaque())
                            .noCollision(definition.hasNoCollision())
                            .wallBlock(wallTorchBlock)
                            .allowUnsupported(definition.isAllowUnsupported())
                            .noParticle(definition.isNoParticle())
                            .build();

                case "fan":
                    // First register the wall fan block
                    String wallFanName = "wall_" + definition.getBlockName();
                    Block wallFanBlock = BlockBuilder.wallFan()
                            .hardness(definition.getHardness())
                            .resistance(definition.getResistance())
                            .sounds(soundGroup)
                            .nonOpaque(definition.isNonOpaque())
                            .noCollision(definition.hasNoCollision())
                            .allowUnsupported(definition.isAllowUnsupported())
                            .build();

                    // Register the wall fan without block item
                    registerBlockWithoutBlockItem(wallFanName, wallFanBlock);

                    // Now create the standing fan with reference to wall fan
                    return BlockBuilder.fan()
                            .hardness(definition.getHardness())
                            .resistance(definition.getResistance())
                            .sounds(soundGroup)
                            .wallBlock(wallFanBlock)
                            .allowUnsupported(definition.isAllowUnsupported())
                            .nonOpaque(definition.isNonOpaque())
                            .noCollision(definition.hasNoCollision())
                            .build();

                case "rail":
                    return BlockBuilder.rail()
                            .hardness(definition.getHardness())
                            .resistance(definition.getResistance())
                            .sounds(soundGroup)
                            .allowUnsupported(definition.isAllowUnsupported())
                            // rails are always nonopaque
                            .nonOpaque(true)
                            .noCollision(definition.hasNoCollision())
                            .build();

                case "furnace":
                    return BlockBuilder.furnace()
                            .hardness(definition.getHardness())
                            .resistance(definition.getResistance())
                            .requiresTool()
                            .nonOpaque(definition.isNonOpaque())
                            .sounds(soundGroup)
                            .luminance(state -> {
                                boolean alwaysOn = definition.isAlwaysOn();
                                boolean isLit = state.contains(Properties.LIT) &&
                                        state.get(Properties.LIT);
                                return (alwaysOn || isLit) ? definition.getLuminance() : 0;
                            })
                            .alwaysOn(definition.isAlwaysOn())
                            .build();

                case "wall":
                    return BlockBuilder.wall()
                            .hardness(definition.getHardness())
                            .resistance(definition.getResistance())
                            .requiresTool()
                            .connectState(definition.isConnectState())
                            .wallSize(definition.getWallSize())
                            .unconnect(definition.isUnconnect())
                            .toggleOnUse(definition.toggleOnUse())
                            .sounds(soundGroup)
                            .build();

                case "cuboid":
                    BlockBuilder<WCCuboidBlock> cuboidBuilder = BlockBuilder.cuboid()
                            .hardness(definition.getHardness())
                            .resistance(definition.getResistance())
                            .requiresTool()
                            .sounds(soundGroup)
                            .nonOpaque(definition.isNonOpaque())
                            .noCollision(definition.hasNoCollision())
                            .toggleOnUse(definition.toggleOnUse())
                            .states(definition.hasStates() ? definition.getStates().size() : 0);

                    // Handle bounding box if present
                    if (definition.getBoundingBox() != null) {
                        var bbox = definition.getBoundingBox();
                        cuboidBuilder.boundingBox(
                            bbox.getXMin(), bbox.getYMin(), bbox.getZMin(),
                            bbox.getXMax(), bbox.getYMax(), bbox.getZMax()
                        );
                    }

                    // Handle state values if present
                    if (definition.hasStates()) {
                        List<String> stateValues = definition.getStateValues();
                        if (stateValues != null && !stateValues.isEmpty()) {
                            cuboidBuilder.stateValues(stateValues);
                        }
                    }

                    return cuboidBuilder.build(definition);

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

        return switch (soundName.toLowerCase()) {
            case "wood" -> BlockSoundGroup.WOOD;
            case "stone" -> BlockSoundGroup.STONE;
            case "metal" -> BlockSoundGroup.METAL;
            case "grass" -> BlockSoundGroup.GRASS;
            case "wool", "cloth" -> BlockSoundGroup.WOOL;
            case "gravel" -> BlockSoundGroup.GRAVEL;
            case "glass" -> BlockSoundGroup.GLASS;
            case "candle" -> BlockSoundGroup.CANDLE;
            case "bone" -> BlockSoundGroup.BONE;
            case "ladder" -> BlockSoundGroup.LADDER;
            case "crop" -> BlockSoundGroup.CROP;
            case "snow" -> BlockSoundGroup.SNOW;
            case "chain" -> BlockSoundGroup.CHAIN;
            case "powder_snow" -> BlockSoundGroup.POWDER_SNOW;
            case "mud" -> BlockSoundGroup.MUD;
            case "packed_mud" -> BlockSoundGroup.PACKED_MUD;
            case "sand" -> BlockSoundGroup.SAND;
            case "vine" -> BlockSoundGroup.VINE;
            case "fungus" -> BlockSoundGroup.FUNGUS;
            case "rooted_dirt" -> BlockSoundGroup.ROOTED_DIRT;
            case "scaffolding" -> BlockSoundGroup.SCAFFOLDING;
            case "lantern" -> BlockSoundGroup.LANTERN;
            case "coral" -> BlockSoundGroup.CORAL;
            default -> {
                WesterosBlocks.LOGGER.warn("Unknown sound type '{}', defaulting to STONE", soundName);
                yield BlockSoundGroup.STONE;
            }
        };
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
