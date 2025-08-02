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

        // Solid Blocks
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

        public static final Block BENCH_DRAWERS = registerBlock(
                        "bench_drawers",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block BENCH_KITCHEN_KNIVES = registerBlock(
                        "bench_kitchen_knives",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block BENCH_KITCHEN_PANS = registerBlock(
                        "bench_kitchen_pans",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block BENCH_MASON_HAMMER_MALLET = registerBlock(
                        "bench_mason_hammer_mallet",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block BERRY_BASKET = registerBlock(
                        "berry_basket",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block BERRY_CRATE = registerBlock(
                        "berry_crate",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block APRICOT_BASKET = registerBlock(
                        "apricot_basket",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block BLACK_BRICK_ENGRAVED = registerBlock(
                        "black_brick_engraved",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(5.0f)
                                        .requiresTool().sounds(BlockSoundGroup.STONE)));

        public static final Block BLUEGREEN_CARVED_SANDSTONE = registerBlock(
                        "bluegreen_carved_sandstone",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(5.0f)
                                        .requiresTool().sounds(BlockSoundGroup.STONE)));

        public static final Block BONE_DIRT = registerBlock(
                        "bone_dirt",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.GRASS)));

        public static final Block BOOKSHELF_ABANDONED = registerBlock(
                        "bookshelf_abandoned",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block BOOKSHELF_LIBRARY = registerBlock(
                        "bookshelf_library",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block BOOKSHELF_MAESTER = registerBlock(
                        "bookshelf_maester",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block BROKEN_CABINET = registerBlock(
                        "broken_cabinet",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block BROWN_GREY_BRICK_ENGRAVED = registerBlock(
                        "brown_grey_brick_engraved",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(5.0f)
                                        .requiresTool().sounds(BlockSoundGroup.STONE)));

        public static final Block CABINET_DRAWER = registerBlock(
                        "cabinet_drawer",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block CAGE = registerBlock(
                        "cage",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.STONE).nonOpaque()));

        public static final Block CARROT_BASKET = registerBlock(
                        "carrot_basket",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block CARROT_CRATE = registerBlock(
                        "carrot_crate",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block CLOSED_BASKET = registerBlock(
                        "closed_basket",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block CLOSED_CABINET = registerBlock(
                        "closed_cabinet",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block COARSE_DARK_RED_CARVED_SANDSTONE = registerBlock(
                        "coarse_dark_red_carved_sandstone",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(5.0f)
                                        .requiresTool().sounds(BlockSoundGroup.STONE)));

        public static final Block COARSE_RED_CARVED_SANDSTONE = registerBlock(
                        "coarse_red_carved_sandstone",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(5.0f)
                                        .requiresTool().sounds(BlockSoundGroup.STONE)));

        public static final Block COBBLE_KEYSTONE = registerBlock(
                        "cobble_keystone",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(5.0f)
                                        .requiresTool().sounds(BlockSoundGroup.STONE)));

        public static final Block COLOURED_SEPT_WINDOW = registerBlock(
                        "coloured_sept_window",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.STONE).nonOpaque()));

        public static final Block CRATE = registerBlock(
                        "crate",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block CRATE2 = registerBlock(
                        "crate2",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block CRATE3 = registerBlock(
                        "crate3",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block DARK_GREY_BRICK_ENGRAVED = registerBlock(
                        "dark_grey_brick_engraved",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(5.0f)
                                        .requiresTool().sounds(BlockSoundGroup.STONE)));

        public static final Block DATE_BASKET = registerBlock(
                        "date_basket",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block DATES = registerBlock(
                        "dates",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.GRASS)));

        public static final Block DESERT_SANDSTONE_ENGRAVED = registerBlock(
                        "desert_sandstone_engraved",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(3.0f)
                                        .requiresTool().sounds(BlockSoundGroup.STONE)));

        public static final Block DOMESTIC_UTILITY_BLOCK = registerBlock(
                        "domestic_utility_block",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(5.0f)
                                        .requiresTool().sounds(BlockSoundGroup.METAL)));

        public static final Block DONE_UTILITY_BLOCK = registerBlock(
                        "done_utility_block",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(5.0f)
                                        .requiresTool().sounds(BlockSoundGroup.METAL)));

        public static final Block DRAGON_CARVING = registerBlock(
                        "dragon_carving",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.STONE)));

        // Log blocks
        public static final Block ARCHERY_TARGET = registerBlock(
                        "archery_target",
                        new WCLogBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        // Slab blocks
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
                        entries.add(ModBlocks.BENCH_DRAWERS);
                        entries.add(ModBlocks.BENCH_KITCHEN_KNIVES);
                        entries.add(ModBlocks.BENCH_KITCHEN_PANS);
                        entries.add(ModBlocks.BENCH_MASON_HAMMER_MALLET);
                        entries.add(ModBlocks.BERRY_BASKET);
                        entries.add(ModBlocks.BERRY_CRATE);
                        entries.add(ModBlocks.APRICOT_BASKET);
                        entries.add(ModBlocks.BLACK_BRICK_ENGRAVED);
                        entries.add(ModBlocks.BLUEGREEN_CARVED_SANDSTONE);
                        entries.add(ModBlocks.BONE_DIRT);
                        entries.add(ModBlocks.BOOKSHELF_ABANDONED);
                        entries.add(ModBlocks.BOOKSHELF_LIBRARY);
                        entries.add(ModBlocks.BOOKSHELF_MAESTER);
                        entries.add(ModBlocks.BROKEN_CABINET);
                        entries.add(ModBlocks.BROWN_GREY_BRICK_ENGRAVED);
                        entries.add(ModBlocks.CABINET_DRAWER);
                        entries.add(ModBlocks.CAGE);
                        entries.add(ModBlocks.CARROT_BASKET);
                        entries.add(ModBlocks.CARROT_CRATE);
                        entries.add(ModBlocks.CLOSED_BASKET);
                        entries.add(ModBlocks.CLOSED_CABINET);
                        entries.add(ModBlocks.COARSE_DARK_RED_CARVED_SANDSTONE);
                        entries.add(ModBlocks.COARSE_RED_CARVED_SANDSTONE);
                        entries.add(ModBlocks.COBBLE_KEYSTONE);
                        entries.add(ModBlocks.COLOURED_SEPT_WINDOW);
                        entries.add(ModBlocks.CRATE);
                        entries.add(ModBlocks.CRATE2);
                        entries.add(ModBlocks.CRATE3);
                        entries.add(ModBlocks.DARK_GREY_BRICK_ENGRAVED);
                        entries.add(ModBlocks.DATE_BASKET);
                        entries.add(ModBlocks.DATES);
                        entries.add(ModBlocks.DESERT_SANDSTONE_ENGRAVED);
                        entries.add(ModBlocks.DOMESTIC_UTILITY_BLOCK);
                        entries.add(ModBlocks.DONE_UTILITY_BLOCK);
                        entries.add(ModBlocks.DRAGON_CARVING);
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
