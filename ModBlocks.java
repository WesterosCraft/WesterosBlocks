package com.westerosblocks.block;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.custom.WCSolidBlock;
import com.westerosblocks.block.custom.WCBranchBlock;
import com.westerosblocks.block.custom.WCLogBlock;
import com.westerosblocks.block.custom.WCSlabBlock;

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

public class ModBlocks {

    /**
     * Solid Blocks - Basic blocks with various properties
     */
    public static class SolidBlocks {
        // Wood-based blocks
        public static final Block SIX_SIDED_BIRCH = registerBlock(
                "6sided_birch",
                new WCSolidBlock.Factory().buildBlockClass(AbstractBlock.Settings.create().strength(2.0f)
                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block SIX_SIDED_JUNGLE = registerBlock(
                "6sided_jungle",
                new WCSolidBlock.Factory().buildBlockClass(AbstractBlock.Settings.create().strength(2.0f)
                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block SIX_SIDED_OAK = registerBlock(
                "6sided_oak",
                new WCSolidBlock.Factory().buildBlockClass(AbstractBlock.Settings.create().strength(2.0f)
                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block SIX_SIDED_SPRUCE = registerBlock(
                "6sided_spruce",
                new WCSolidBlock.Factory().buildBlockClass(AbstractBlock.Settings.create().strength(2.0f)
                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        // Storage blocks
        public static final Block APPLE_BASKET = registerBlock(
                "apple_basket",
                new WCSolidBlock.Factory().buildBlockClass(AbstractBlock.Settings.create().strength(2.0f)
                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block APPLE_CRATE = registerBlock(
                "apple_crate",
                new WCSolidBlock.Factory().buildBlockClass(AbstractBlock.Settings.create().strength(2.0f)
                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        // ... continue with all other solid blocks organized by subcategory
    }

    /**
     * Log Blocks - Pillar-like blocks with axis rotation
     */
    public static class LogBlocks {
        public static final Block ARCHERY_TARGET = registerBlock(
                "archery_target",
                new WCLogBlock(AbstractBlock.Settings.create().strength(2.0f)
                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block CLOSED_BARREL = registerBlock(
                "closed_barrel",
                new WCLogBlock(AbstractBlock.Settings.create().strength(2.0f)
                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        // ... continue with all log blocks
    }

    /**
     * Slab Blocks - Half-height blocks
     */
    public static class SlabBlocks {
        public static final Block APPLE_BASKET_SLAB = registerBlock(
                "apple_basket_slab",
                new WCSlabBlock(AbstractBlock.Settings.create().strength(2.0f)
                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        // ... continue with all slab blocks
    }

    /**
     * Branch Blocks - Complex multistate blocks
     */
    public static class BranchBlocks {
        public static final Block OAK_BRANCH = registerBlock(
                "oak_branch",
                new WCBranchBlock.Factory().buildBlockClass(AbstractBlock.Settings.create().strength(2.0f)
                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block BIRCH_BRANCH = registerBlock(
                "birch_branch",
                new WCBranchBlock.Factory().buildBlockClass(AbstractBlock.Settings.create().strength(2.0f)
                        .requiresTool().sounds(BlockSoundGroup.WOOD)));
    }

    // Keep the same registration methods and initialization
    public static void registerModBlocks() {
        WesterosBlocks.LOGGER.info("Registering Mod Blocks for " + WesterosBlocks.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries -> {
            // Add solid blocks
            entries.add(SolidBlocks.SIX_SIDED_BIRCH);
            entries.add(SolidBlocks.SIX_SIDED_JUNGLE);
            // ... continue with all blocks using the nested class references
        });
    }

    private static Block registerBlock(String name, Block block) {
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