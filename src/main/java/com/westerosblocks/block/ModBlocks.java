package com.westerosblocks.block;

import com.westerosblocks.*;
import com.westerosblocks.block.custom.WCArrowSlitBlock;
import com.westerosblocks.block.custom.WCChairBlock;
import com.westerosblocks.block.custom.WCTableBlock;
import com.westerosblocks.block.custom.WCWaySignBlock;
import com.westerosblocks.block.custom.WCWaySignWallBlock;
import com.westerosblocks.item.WCWaySignItem;
import com.westerosblocks.config.ModConfig;
import com.westerosblocks.util.ModUtils;
import com.westerosblocks.util.ModWoodType;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.AbstractBlock;
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

    public static final Block ARBOR_BRICK_ARROW_SLIT = registerArrowSlitBlock(
        "arbor_brick_arrow_slit",
        "westeros_decor_tab",
        2.0f,
        6.0f,
        1,
        "westerosblocks:block/ashlar_third/arbor/all"
    );

    public static final Block BLACK_GRANITE_ARROW_SLIT = registerArrowSlitBlock(
        "black_granite_arrow_slit",
        "westeros_decor_tab",
        2.0f,
        6.0f,
        1,
        "westerosblocks:block/ashlar_third/black/all"
    );

    public static final Block OAK_TABLE = registerTableBlock(
        "oak_table",
        "westeros_furniture_tab",
        2.0f,
        6.0f,
        1,
        "westerosblocks:block/oak_planks",
        WoodType.OAK
    );

    public static final Block BIRCH_TABLE = registerTableBlock(
        "birch_table",
        "westeros_furniture_tab",
        2.0f,
        6.0f,
        1,
        "westerosblocks:block/birch_planks",
        WoodType.BIRCH
    );

    public static final Block SPRUCE_TABLE = registerTableBlock(
        "spruce_table",
        "westeros_furniture_tab",
        2.0f,
        6.0f,
        1,
        "westerosblocks:block/spruce_planks",
        WoodType.SPRUCE
    );

    public static final Block OAK_CHAIR = registerChairBlock(
        "oak_chair",
        "westeros_furniture_tab",
        2.0f,
        6.0f,
        1,
        "westerosblocks:block/wood/oak/all",
        WoodType.OAK
    );

    public static final Block BIRCH_CHAIR = registerChairBlock(
        "birch_chair",
        "westeros_furniture_tab",
        2.0f,
        6.0f,
        1,
        "westerosblocks:block/wood/birch/all",
        WoodType.BIRCH
    );

    public static final Block SPRUCE_CHAIR = registerChairBlock(
        "spruce_chair",
        "westeros_furniture_tab",
        2.0f,
        6.0f,
        1,
        "westerosblocks:block/wood/spruce/all",
        WoodType.SPRUCE
    );

    public static final Block OAK_WAY_SIGN = registerWaySignBlock(
        "oak_way_sign",
        "westeros_decor_tab",
        2.0f,
        6.0f,
        1,
        "westerosblocks:block/wood/oak/all",
        WoodType.OAK
    );

    public static void registerModBlocks() {
        WesterosBlocks.LOGGER.info("Registering blocks for " + com.westerosblocks.WesterosBlocks.MOD_ID);
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

        ModBlock.dumpBlockPerf();
        WesterosBlocks.LOGGER.info("TOTAL: {} custom blocks", blockCount);

        boolean dumpBlockSets = ModConfig.get().dumpBlockSets;
        boolean dumpWorldpainterCSV = ModConfig.get().dumpWorldpainterCSV;

        if (dumpBlockSets) {
            WesterosBlocksCompatibility.dumpBlockSets(WesterosBlocksDefLoader.getCustomConfig().blockSets);
        }
        if (dumpWorldpainterCSV) {
            WesterosBlocksCompatibility.dumpWorldPainterConfig(CUSTOM_BLOCKS.values().toArray(new Block[0]));
        }
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

    public static Block registerArrowSlitBlock(String name, String creativeTab, float hardness, float resistance, int harvestLevel) {
        return registerArrowSlitBlock(name, creativeTab, hardness, resistance, harvestLevel, "westerosblocks:block/side.block");
    }

    public static Block registerArrowSlitBlock(String name, String creativeTab, float hardness, float resistance, int harvestLevel, String texturePath) {
        return registerCustomBlock(name, creativeTab, hardness, resistance, harvestLevel, texturePath, WCArrowSlitBlock.class, true);
    }

    public static Block registerTableBlock(String name, String creativeTab, float hardness, float resistance, int harvestLevel, String texturePath) {
        return registerTableBlock(name, creativeTab, hardness, resistance, harvestLevel, texturePath, "oak");
    }

    public static Block registerTableBlock(String name, String creativeTab, float hardness, float resistance, int harvestLevel, String texturePath, String woodType) {
        return registerCustomBlock(name, creativeTab, hardness, resistance, harvestLevel, texturePath, WCTableBlock.class, false, woodType);
    }

    public static Block registerTableBlock(String name, String creativeTab, float hardness, float resistance, int harvestLevel, String texturePath, WoodType woodType) {
        return registerCustomBlock(name, creativeTab, hardness, resistance, harvestLevel, texturePath, WCTableBlock.class, false, woodType);
    }

    public static Block registerChairBlock(String name, String creativeTab, float hardness, float resistance, int harvestLevel, String texturePath) {
        return registerChairBlock(name, creativeTab, hardness, resistance, harvestLevel, texturePath, "oak");
    }

    public static Block registerChairBlock(String name, String creativeTab, float hardness, float resistance, int harvestLevel, String texturePath, String woodType) {
        return registerCustomBlock(name, creativeTab, hardness, resistance, harvestLevel, texturePath, WCChairBlock.class, false, woodType);
    }

    public static Block registerChairBlock(String name, String creativeTab, float hardness, float resistance, int harvestLevel, String texturePath, WoodType woodType) {
        return registerCustomBlock(name, creativeTab, hardness, resistance, harvestLevel, texturePath, WCChairBlock.class, false, woodType);
    }

    public static Block registerWaySignBlock(String name, String creativeTab, float hardness, float resistance, int harvestLevel, String texturePath) {
        return registerWaySignBlock(name, creativeTab, hardness, resistance, harvestLevel, texturePath, WoodType.OAK);
    }

    public static Block registerWaySignBlock(String name, String creativeTab, float hardness, float resistance, int harvestLevel, String texturePath, String woodType) {
        return registerCustomBlock(name, creativeTab, hardness, resistance, harvestLevel, texturePath, WCWaySignBlock.class, false, woodType);
    }

    public static Block registerWaySignBlock(String name, String creativeTab, float hardness, float resistance, int harvestLevel, String texturePath, WoodType woodType) {
        // Register the main way sign block (fence post replacement)
        Block mainBlock = registerCustomBlock(name, creativeTab, hardness, resistance, harvestLevel, texturePath, WCWaySignBlock.class, false, woodType);
        
        // Register the wall way sign block
        Block wallBlock = registerCustomBlock("wall_" + name, creativeTab, hardness, resistance, harvestLevel, texturePath, WCWaySignWallBlock.class, false, woodType);
        
        return mainBlock;
    }

    private static Block registerCustomBlock(String name, String creativeTab, float hardness, float resistance, int harvestLevel, String texturePath, Class<?> blockClass, boolean stoneLike) {
        return registerCustomBlock(name, creativeTab, hardness, resistance, harvestLevel, texturePath, blockClass, stoneLike, "oak");
    }

    private static Block registerCustomBlock(String name, String creativeTab, float hardness, float resistance, int harvestLevel, String texturePath, Class<?> blockClass, boolean stoneLike, String woodType) {
        return registerCustomBlock(name, creativeTab, hardness, resistance, harvestLevel, texturePath, blockClass, stoneLike, ModWoodType.getWoodType(woodType));
    }

    private static Block registerCustomBlock(String name, String creativeTab, float hardness, float resistance, int harvestLevel, String texturePath, Class<?> blockClass, boolean stoneLike, WoodType woodType) {
        ModUtils.BlockBuilder builder = new ModUtils.BlockBuilder(name)
            .creativeTab(creativeTab)
            .hardness(hardness)
            .resistance(resistance)
            .harvestLevel(harvestLevel);

        if (stoneLike) {
            builder.stoneLike();
        } else {
            builder.woodLike();
        }

        AbstractBlock.Settings settings = builder.buildSettings();
        Block block;

        try {
            if (blockClass == WCArrowSlitBlock.class) {
                block = new WCArrowSlitBlock(settings, builder.getBlockName(), builder.getCreativeTab());
            } else if (blockClass == WCTableBlock.class) {
                block = new WCTableBlock(settings, builder.getBlockName(), builder.getCreativeTab(), woodType);
            } else if (blockClass == WCChairBlock.class) {
                block = new WCChairBlock(settings, builder.getBlockName(), builder.getCreativeTab(), woodType);
            } else if (blockClass == WCWaySignBlock.class) {
                block = new WCWaySignBlock(settings, builder.getBlockName(), builder.getCreativeTab(), woodType);
            } else if (blockClass == WCWaySignWallBlock.class) {
                block = new WCWaySignWallBlock(settings, builder.getBlockName(), builder.getCreativeTab(), woodType);
            } else {
                throw new IllegalArgumentException("Unsupported block class: " + blockClass.getSimpleName());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to create block instance for " + name, e);
        }

        // Register creative tab
        ItemGroupEvents.modifyEntriesEvent(WesterosCreativeModeTabs.TABS.get(creativeTab)).register(entries -> {
            entries.add(block);
        });

        return registerBlock(name, block);
    }
}
