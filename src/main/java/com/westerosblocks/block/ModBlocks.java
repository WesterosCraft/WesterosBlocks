package com.westerosblocks.block;

import com.westerosblocks.*;
import com.westerosblocks.config.ModConfig;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
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
//    public static Block[] CUSTOM_BLOCKS = new Block[0];
    public static Map<String, Block> CUSTOM_BLOCKS = new HashMap<>();
    public static ModBlock[] customBlockDefs = WesterosBlocksDefLoader.getCustomBlockDefs();

    public static void registerModBlocks() {
        WesterosBlocks.LOGGER.info("Registering blocks for " + com.westerosblocks.WesterosBlocks.MOD_ID);
        List<Block> blklist = new LinkedList<>();
        HashMap<String, Integer> countsByType = new HashMap<>();
        AtomicInteger blockCount = new AtomicInteger();

        for (ModBlock customBlock : customBlockDefs) {
            if (customBlock == null)
                continue;

            Block blk = customBlock.createBlock();

            if (blk != null) {
                // Register creative tab
                ItemGroupEvents.modifyEntriesEvent(WesterosCreativeModeTabs.TABS.get(customBlock.creativeTab)).register(entries -> {
                    entries.add(blk);
                });

                blklist.add(blk);
//                customBlocksByName.put(customBlock.blockName, blk);
                // Add to counts
                Integer cnt = countsByType.get(customBlock.blockType);
                cnt = (cnt == null) ? 1 : (cnt + 1);
                countsByType.put(customBlock.blockType, cnt);
                blockCount.getAndIncrement();

            } else {
                crash("Invalid block definition for " + customBlock.blockName + " - aborted during load()");
                return;
            }
        }

//        CUSTOM_BLOCKS = blklist.toArray(new Block[0]);
        ModBlock.dumpBlockPerf();
        WesterosBlocks.LOGGER.info("TOTAL: " + blockCount + " custom blocks");

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

//    public static Map<String, Block> getCustomBlocks() {
//        return Collections.synchronizedMap(new HashMap<>(customBlocksByName));
//    }

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
}
