package com.westerosblocks.block;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.custom.WCSolidBlock;
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

        // Block definitions
        public static final Block SIX_SIDED_BIRCH = registerBlock(
                        "6sided_birch",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block SIX_SIDED_JUNGLE = registerBlock(
                        "6sided_jungle",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block SIX_SIDED_OAK = registerBlock(
                        "6sided_oak",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block SIX_SIDED_SPRUCE = registerBlock(
                        "6sided_spruce",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block SIX_SIDED_STONE_SLAB = registerBlock(
                        "6sided_stone_slab",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.STONE)));

        public static final Block APPLE_BASKET = registerBlock(
                        "apple_basket",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block APPLE_CRATE = registerBlock(
                        "apple_crate",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block APPROVAL_UTILITY_BLOCK = registerBlock(
                        "approval_utility_block",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(5.0f)
                                        .requiresTool().sounds(BlockSoundGroup.METAL)));

        public static final Block ARBOR_BRICK_ORNATE = registerBlock(
                        "arbor_brick_ornate",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(5.0f)
                                        .requiresTool().sounds(BlockSoundGroup.STONE)));

        public static final Block BENCH_BUTCHER_KNIVES = registerBlock(
                        "bench_butcher_knives",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block BENCH_CARPENTRY_HAMMER_SAW = registerBlock(
                        "bench_carpentry_hammer_saw",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        // Log blocks
        public static final Block ARCHERY_TARGET = registerBlock(
                        "archery_target",
                        new WCLogBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block APPLE_BASKET_SLAB = registerBlock(
                        "apple_basket_slab",
                        new WCSlabBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        /**
         * Initialize all blocks
         */
        public static void registerModBlocks() {
                WesterosBlocks.LOGGER.info("Registering Mod Blocks for " + WesterosBlocks.MOD_ID);

                ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries -> {
                        entries.add(ModBlocks.SIX_SIDED_BIRCH);
                        entries.add(ModBlocks.SIX_SIDED_JUNGLE);
                        entries.add(ModBlocks.SIX_SIDED_OAK);
                        entries.add(ModBlocks.SIX_SIDED_SPRUCE);
                        entries.add(ModBlocks.SIX_SIDED_STONE_SLAB);
                        entries.add(ModBlocks.APPLE_BASKET);
                        entries.add(ModBlocks.APPLE_CRATE);
                        entries.add(ModBlocks.APPROVAL_UTILITY_BLOCK);
                        entries.add(ModBlocks.ARBOR_BRICK_ORNATE);
                        entries.add(ModBlocks.BENCH_BUTCHER_KNIVES);
                        entries.add(ModBlocks.BENCH_CARPENTRY_HAMMER_SAW);
                        entries.add(ModBlocks.ARCHERY_TARGET);
                        entries.add(ModBlocks.APPLE_BASKET_SLAB);
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
