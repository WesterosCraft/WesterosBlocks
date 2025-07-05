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
        ModBlocks2.initialize();
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





}
