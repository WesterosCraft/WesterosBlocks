package com.westerosblocks.block;

import com.westerosblocks.*;
import com.westerosblocks.block.custom.WCArrowSlitBlock;
import com.westerosblocks.config.ModConfig;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
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

    public static final Block TEST_ARROW_SLIT = registerArrowSlitBlock(
        "test_arrow_slit",
        "westeros_decor_tab",
        2.0f,
        6.0f,
        1
    );

    public static final Block ARBOR_BRICK_ARROW_SLIT = registerArrowSlitBlock(
        "arbor_brick_arrow_slit",
        "westeros_decor_tab",
        2.0f,
        6.0f,
        1
    );

    public static final Block BLACK_GRANITE_ARROW_SLIT = registerArrowSlitBlock(
        "black_granite_arrow_slit",
        "westeros_decor_tab",
        2.0f,
        6.0f,
        1
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
        Registry.register(Registries.ITEM, WesterosBlocks.id(name),
                new BlockItem(block, new Item.Settings()));
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
        WCArrowSlitBlock block = new WCArrowSlitBlock.Builder(name)
            .creativeTab(creativeTab)
            .hardness(hardness)
            .resistance(resistance)
            .harvestLevel(harvestLevel)
            .build();

        // Register creative tab
        ItemGroupEvents.modifyEntriesEvent(WesterosCreativeModeTabs.TABS.get(creativeTab)).register(entries -> {
            entries.add(block);
        });

        return registerBlock(name, block);
    }
}
