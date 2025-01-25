package com.westerosblocks.block;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.WesterosCreativeModeTabs;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.westerosblocks.WesterosBlocks.crash;

public class ModBlocks {
    public static Block[] customBlocks = new Block[0];
    public static HashMap<String, Block> customBlocksByName = new HashMap<>();

    public static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, WesterosBlocks.id(name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, WesterosBlocks.id(name),
                new BlockItem(block, new Item.Settings()));
    }

    public static void registerModBlocks(WesterosBlockDef[] customBlockDefs) {
        WesterosBlocks.LOGGER.info("Registering blocks for " + com.westerosblocks.WesterosBlocks.MOD_ID);
        List<Block> blklist = new LinkedList<>();
        HashMap<String, Integer> countsByType = new HashMap<>();
        AtomicInteger blockCount = new AtomicInteger();

        for (WesterosBlockDef customBlock : customBlockDefs) {
            if (customBlock == null)
                continue;

            Block blk = customBlock.createBlock();

            if (blk != null) {
                // Register creative tab
                ItemGroupEvents.modifyEntriesEvent(WesterosCreativeModeTabs.TABS.get(customBlock.creativeTab)).register(entries -> {
                    entries.add(blk);
                });

                blklist.add(blk);
                customBlocksByName.put(customBlock.blockName, blk);
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

        customBlocks = blklist.toArray(new Block[0]);
        WesterosBlockDef.dumpBlockPerf();
        // TODO
        // Dump information for external mods
//        WesterosBlocksCompatibility.dumpBlockSets(customConfig.blockSets, modConfigPath);
//        WesterosBlocksCompatibility.dumpWorldPainterConfig(customBlocks, modConfigPath);
        // Brag on block type counts
        WesterosBlocks.LOGGER.info("Count of custom Westeros blocks by type:");
        for (String type : countsByType.keySet()) {
            WesterosBlocks.LOGGER.info(type + ": " + countsByType.get(type) + " blocks");
        }
        WesterosBlocks.LOGGER.info("TOTAL: " + blockCount + " blocks");
        // TODO
//        colorMaps = customConfig.colorMaps;
        // TODO
//        menuOverrides = customConfig.menuOverrides;
        WesterosBlocks.LOGGER.info("WesterosBlocks custom block registration complete.");
    }

    public static HashMap<String, Block> getCustomBlocksByName() {
        return customBlocksByName;
    }

    public static Block findBlockByName(String blkname, String namespace) {
        Block blk = customBlocksByName.get(blkname);
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

        if (id.getNamespace().equals(namespace)) {
            blk = customBlocksByName.get(id.getPath());
            if (blk != null) return blk;
        }

        return Registries.BLOCK.get(id);
    }

}
