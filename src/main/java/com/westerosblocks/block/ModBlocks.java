package com.westerosblocks.block;

import com.westerosblocks.*;

import com.westerosblocks.block.custom.WCWaySignBlock;

import com.westerosblocks.config.ModConfig;


import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.loader.api.FabricLoader;

import net.minecraft.block.Block;
import net.minecraft.block.WoodType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ModBlocks {
    public static Map<String, Block> CUSTOM_BLOCKS = new HashMap<>();
    public static ModBlock[] CUSTOM_BLOCK_DEFS = WesterosBlocksDefLoader.getCustomBlockDefs();
    static boolean isDevelopmentEnvironment = FabricLoader.getInstance().isDevelopmentEnvironment();

    public static void registerModBlocks() {
        WesterosBlocks.LOGGER.info("Registering blocks for " + com.westerosblocks.WesterosBlocks.MOD_ID);

        // Register legacy custom blocks
        registerLegacyCustomBlocks();

        // Register new builder-based blocks
        registerBuilderBlocks();

        ModBlock.dumpBlockPerf();
        WesterosBlocks.LOGGER.info("TOTAL: {} custom blocks", CUSTOM_BLOCKS.size());

        boolean dumpBlockSets = ModConfig.get().dumpBlockSets;
        boolean dumpWorldpainterCSV = ModConfig.get().dumpWorldpainterCSV;

        if (dumpBlockSets) {
            WesterosBlocksCompatibility.dumpBlockSets(WesterosBlocksDefLoader.getCustomConfig().blockSets);
        }
        if (dumpWorldpainterCSV) {
            WesterosBlocksCompatibility.dumpWorldPainterConfig(CUSTOM_BLOCKS.values().toArray(new Block[0]));
        }
    }

    private static void registerLegacyCustomBlocks() {
        HashMap<String, Integer> countsByType = new HashMap<>();
        AtomicInteger blockCount = new AtomicInteger();

        for (ModBlock customBlock : CUSTOM_BLOCK_DEFS) {
            if (customBlock == null)
                continue;

            Block blk = customBlock.createBlock();

            // Skip test blocks in production environment
            if (!isDevelopmentEnvironment && isTestBlock(customBlock)) {
                continue;
            }

            if (blk != null) {
                // Register creative tab
                ItemGroupEvents.modifyEntriesEvent(WesterosCreativeModeTabs.TABS.get(customBlock.creativeTab)).register(entries -> {
                    entries.add(blk);
                });

                Integer cnt = countsByType.get(customBlock.blockType);
                cnt = (cnt == null) ? 1 : (cnt + 1);
                countsByType.put(customBlock.blockType, cnt);
                blockCount.getAndIncrement();

            } else {
                crash("Invalid block definition for " + customBlock.blockName + " - aborted during load()");
                return;
            }
        }

        WesterosBlocks.LOGGER.info("Registered {} legacy custom blocks", blockCount.get());
    }

    private static void registerBuilderBlocks() {
        WesterosBlocks.LOGGER.info("Registering builder-based blocks");
    }

    public static Block registerBlock(String name, Block block) {
        CUSTOM_BLOCKS.put(name, block);
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, WesterosBlocks.id(name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        // Use custom item for way sign blocks
        if (block instanceof WCWaySignBlock waySignBlock) {
            Registry.register(Registries.ITEM, WesterosBlocks.id(name),
                    new com.westerosblocks.item.WCWaySignItem(block, new Item.Settings(), waySignBlock.getWoodType().toString()));
        } else {
            Registry.register(Registries.ITEM, WesterosBlocks.id(name),
                    new BlockItem(block, new Item.Settings()));
        }
    }

    public static Map<String, Block> getCustomBlocks() {
        return CUSTOM_BLOCKS;
    }

    public static Block findBlockByName(String blkname, String namespace) {
        Block blk = CUSTOM_BLOCKS.get(blkname);
        if (blk != null) return blk;

        Identifier id;
        try {
            id = Identifier.tryParse(blkname);
        } catch (InvalidIdentifierException e) {
            if (namespace != null) {
                try {
                    id = Identifier.of(namespace, blkname);
                } catch (InvalidIdentifierException e2) {
                    return null;
                }
            } else {
                return null;
            }
        }

        if (id != null && id.getNamespace().equals(namespace)) {
            blk = CUSTOM_BLOCKS.get(id.getPath());
            if (blk != null) return blk;
        }

        return Registries.BLOCK.get(id);
    }

    public static void crash(Exception x, String msg) {
        throw new CrashException(new CrashReport(msg, x));
    }

    public static void crash(String msg) {
        crash(new Exception(), msg);
    }

    private static boolean isTestBlock(ModBlock block) {
        return block.blockName.startsWith("test_") ||
                block.blockName.contains("_test_") ||
                (block.customTags != null && block.customTags.contains("test"));
    }



    // CLEAN BLOCK REGISTRATION USING BUILDER PATTERN WITH DEFAULTS

    public static final Block ARBOR_BRICK_ARROW_SLIT = registerArrowSlitBlock(
        "arbor_brick_arrow_slit",
        builder -> builder
            .creativeTab("westeros_decor_tab")
            .texture("ashlar_third/arbor/all")
    );

    public static final Block BLACK_GRANITE_ARROW_SLIT = registerArrowSlitBlock(
        "black_granite_arrow_slit",
        builder -> builder
            .creativeTab("westeros_decor_tab")
            .texture("ashlar_third/black/all")
    );

    public static final Block OAK_TABLE = registerTableBlock(
        "oak_table",
        builder -> builder
            .creativeTab("westeros_furniture_tab")
            .texture("oak_planks")
            .woodType(WoodType.OAK)
    );

    public static final Block BIRCH_TABLE = registerTableBlock(
        "birch_table",
        builder -> builder
            .creativeTab("westeros_furniture_tab")
            .texture("birch_planks")
            .woodType(WoodType.BIRCH)
    );

    public static final Block SPRUCE_TABLE = registerTableBlock(
        "spruce_table",
        builder -> builder
            .creativeTab("westeros_furniture_tab")
            .texture("spruce_planks")
            .woodType(WoodType.SPRUCE)
    );

    public static final Block OAK_CHAIR = registerChairBlock(
        "oak_chair",
        builder -> builder
            .creativeTab("westeros_furniture_tab")
            .texture("wood/oak/all")
            .woodType(WoodType.OAK)
    );

    public static final Block BIRCH_CHAIR = registerChairBlock(
        "birch_chair",
        builder -> builder
            .creativeTab("westeros_furniture_tab")
            .texture("wood/birch/all")
            .woodType(WoodType.BIRCH)
    );

    public static final Block SPRUCE_CHAIR = registerChairBlock(
        "spruce_chair",
        builder -> builder
            .creativeTab("westeros_furniture_tab")
            .texture("wood/spruce/all")
            .woodType(WoodType.SPRUCE)
    );

    public static final Block OAK_WAY_SIGN = registerWaySignBlock(
        "oak_way_sign",
        builder -> builder
            .creativeTab("westeros_decor_tab")
            .texture("wood/oak/all")
            .woodType(WoodType.OAK)
    );

    public static final Block KINGS_LANDING_SEWER_MANHOLE = registerStandaloneTrapDoorBlock(
        "kings_landing_sewer_manhole",
        builder -> builder
            .creativeTab("westeros_decor_tab")
            .hardness(2.0f)
            .resistance(5.0f)
            .texture("trapdoor_block/kings_landing_manhole")
            .woodType(WoodType.OAK)
            .soundType("iron")
    );

    public static final Block OLDTOWN_SEWER_MANHOLE = registerStandaloneTrapDoorBlock(
        "oldtown_sewer_manhole",
        builder -> builder
            .creativeTab("westeros_decor_tab")
            .hardness(2.0f)
            .resistance(5.0f)
            .texture("trapdoor_block/oldtown_manhole")
            .woodType(WoodType.OAK)
            .soundType("iron")
    );

    public static final Block SEWER_MANHOLE = registerStandaloneTrapDoorBlock(
        "sewer_manhole",
        builder -> builder
            .creativeTab("westeros_decor_tab")
            .hardness(2.0f)
            .resistance(5.0f)
            .texture("trapdoor_block/manhole")
            .woodType(WoodType.OAK)
            .soundType("iron")
    );

    public static final Block WHITE_HARBOR_SEWER_MANHOLE = registerStandaloneTrapDoorBlock(
        "white_harbor_sewer_manhole",
        builder -> builder
            .creativeTab("westeros_decor_tab")
            .hardness(2.0f)
            .resistance(5.0f)
            .texture("trapdoor_block/white_harbor_manhole")
            .woodType(WoodType.OAK)
            .soundType("iron")
    );

    // BATCH REGISTRATION METHODS FOR MASS BLOCK CREATION
    // public static void registerArrowSlits() {
    //     String[] materials = {
    //         "arbor_brick", "black_granite", "grey_granite", "white_granite",
    //         "red_brick", "yellow_brick", "blue_brick", "green_brick"
    //     };

    //     for (String material : materials) {
    //         registerArrowSlitBlock(material + "_arrow_slit", builder -> builder
    //             .texture("westerosblocks:block/ashlar_third/" + material.replace("_brick", "") + "/all"));
    //     }
    // }


    /**
     * Parses block type string into parameters and flags
     */
    public static Map<String, String> parseBlockParameters(String typeString) {
        Map<String, String> params = new HashMap<>();
        if (typeString != null) {
            for (String token : typeString.split(",")) {
                token = token.trim();
                if (token.contains(":")) {
                    String[] parts = token.split(":", 2);
                    params.put(parts[0].trim(), parts[1].trim());
                } else {
                    // For flags without values, store them with an empty string value
                    params.put(token, "");
                }
            }
        }
        return params;
    }

    // NEW BUILDER-BASED REGISTRATION METHODS WITH DEFAULTS

    @FunctionalInterface
    public interface BlockBuilderConfigurator {
        BlockBuilder configure(BlockBuilder builder);
    }

    public static Block registerArrowSlitBlock(String name, BlockBuilderConfigurator configurator) {
        BlockBuilder builder = new BlockBuilder(name)
            .creativeTab("westeros_decor_tab")
            .hardness(2.0f)
            .resistance(6.0f)
            .harvestLevel(1)
            .texture("side.block"); // Default texture

        builder.setBlockType(BlockBuilder.BlockType.ARROW_SLIT);
        return configurator.configure(builder).register();
    }

    public static Block registerTableBlock(String name, BlockBuilderConfigurator configurator) {
        BlockBuilder builder = new BlockBuilder(name)
            .creativeTab("westeros_furniture_tab")
            .hardness(2.0f)
            .resistance(6.0f)
            .harvestLevel(1)
            .woodType(WoodType.OAK); // Default wood type

        builder.setBlockType(BlockBuilder.BlockType.TABLE);
        return configurator.configure(builder).register();
    }

    public static Block registerChairBlock(String name, BlockBuilderConfigurator configurator) {
        BlockBuilder builder = new BlockBuilder(name)
            .creativeTab("westeros_furniture_tab")
            .hardness(2.0f)
            .resistance(6.0f)
            .harvestLevel(1)
            .woodType(WoodType.OAK)
            .nonOpaque();

        builder.setBlockType(BlockBuilder.BlockType.CHAIR);
        return configurator.configure(builder).register();
    }

    public static Block registerWaySignBlock(String name, BlockBuilderConfigurator configurator) {
        BlockBuilder builder = new BlockBuilder(name)
            .creativeTab("westeros_decor_tab")
            .hardness(2.0f)
            .resistance(6.0f)
            .harvestLevel(1)
            .woodType(WoodType.OAK); // Default wood type

        builder.setBlockType(BlockBuilder.BlockType.WAY_SIGN);
        return configurator.configure(builder).register();
    }

    public static Block registerStandaloneTrapDoorBlock(String name, BlockBuilderConfigurator configurator) {
        BlockBuilder builder = new BlockBuilder(name)
            .creativeTab("westeros_decor_tab")
            .hardness(2.0f)
            .resistance(6.0f)
            .harvestLevel(1)
            .woodType(WoodType.OAK)
            .soundType("iron")
            .locked(false);

        builder.setBlockType(BlockBuilder.BlockType.TRAPDOOR);
        return configurator.configure(builder).register();
    }

}
