package com.westerosblocks.block;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.WesterosBlocksCompatibility;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static com.westerosblocks.WesterosBlocks.crash;

public class ModBlocks {
    // TODO: sample block registration
    public static final Block PINK_GARNET_BLOCK = registerBlock("pink_garnet_block",
            new Block(AbstractBlock.Settings.create().strength(4f)
                    .requiresTool().sounds(BlockSoundGroup.AMETHYST_BLOCK)));


    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(WesterosBlocks.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(WesterosBlocks.MOD_ID, name),
                new BlockItem(block, new Item.Settings()));
    }

    public static void registerModBlocks(WesterosBlockDef[] customBlockDefs) {
        WesterosBlocks.LOGGER.info("Registering blocks for " + com.westerosblocks.WesterosBlocks.MOD_ID);
        // Construct custom block definitions
        List<Block> blklist = new LinkedList<>();
        HashMap<String, Block> customBlocksByName = new HashMap<>();
        HashMap<String, Integer> countsByType = new HashMap<>();
        int blockcount = 0;

        for (WesterosBlockDef customBlock : customBlockDefs) {
            if (customBlock == null)
                continue;
            // TODO helper arg is gone now
            Block blk = customBlock.createBlock();

            if (blk != null) {
                blklist.add(blk);
                customBlocksByName.put(customBlock.blockName, blk);
                // Add to counts
                Integer cnt = countsByType.get(customBlock.blockType);
                cnt = (cnt == null) ? 1 : (cnt + 1);
                countsByType.put(customBlock.blockType, cnt);
                blockcount++;

            } else {
                crash("Invalid block definition for " + customBlock.blockName + " - aborted during load()");
                return;
            }
        }
        // TODO
//        customBlocks = blklist.toArray(new Block[blklist.size()]);
        WesterosBlockDef.dumpBlockPerf();
        // Dump information for external mods
        // TODO
//        WesterosBlocksCompatibility.dumpBlockSets(customConfig.blockSets, modConfigPath);
        // TODO
//        WesterosBlocksCompatibility.dumpWorldPainterConfig(customBlocks, modConfigPath);
        // Brag on block type counts
        WesterosBlocks.LOGGER.info("Count of custom blocks by type:");
        for (String type : countsByType.keySet()) {
            WesterosBlocks.LOGGER.info(type + ": " + countsByType.get(type) + " blocks");
        }
        WesterosBlocks.LOGGER.info("TOTAL: " + blockcount + " blocks");
        // TODO
//        colorMaps = customConfig.colorMaps;
        // TODO
//        menuOverrides = customConfig.menuOverrides;
        WesterosBlocks.LOGGER.info("WesterosBlocks custom block registration complete.");
    }

    //        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries -> {
//            entries.add(ModBlocks.PINK_GARNET_BLOCK);
//        });
}
