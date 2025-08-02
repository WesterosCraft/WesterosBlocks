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

        public static final Block EMPTY_BARREL = registerBlock(
                        "empty_barrel",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block EMPTY_CABINET = registerBlock(
                        "empty_cabinet",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block FAITH_CARVED_ARBOR_BRICK = registerBlock(
                        "faith_carved_arbor_brick",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(5.0f)
                                        .requiresTool().sounds(BlockSoundGroup.STONE)));

        public static final Block FAITH_CARVED_BLACK_BRICK = registerBlock(
                        "faith_carved_black_brick",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(5.0f)
                                        .requiresTool().sounds(BlockSoundGroup.STONE)));

        public static final Block FAITH_CARVED_BROWN_GREY_BRICK = registerBlock(
                        "faith_carved_brown_grey_brick",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(5.0f)
                                        .requiresTool().sounds(BlockSoundGroup.STONE)));

        public static final Block FAITH_CARVED_COARSE_RED_BRICK = registerBlock(
                        "faith_carved_coarse_red_brick",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(5.0f)
                                        .requiresTool().sounds(BlockSoundGroup.STONE)));

        public static final Block FAITH_CARVED_DARK_GREY_BRICK = registerBlock(
                        "faith_carved_dark_grey_brick",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(5.0f)
                                        .requiresTool().sounds(BlockSoundGroup.STONE)));

        public static final Block FAITH_CARVED_DUN_BRICK = registerBlock(
                        "faith_carved_dun_brick",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(5.0f)
                                        .requiresTool().sounds(BlockSoundGroup.STONE)));

        public static final Block FAITH_CARVED_GREY_BRICK = registerBlock(
                        "faith_carved_grey_brick",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(5.0f)
                                        .requiresTool().sounds(BlockSoundGroup.STONE)));

        public static final Block FAITH_CARVED_OLDTOWN_BRICK = registerBlock(
                        "faith_carved_oldtown_brick",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(5.0f)
                                        .requiresTool().sounds(BlockSoundGroup.STONE)));

        public static final Block FAITH_CARVED_PINK_SANDSTONE = registerBlock(
                        "faith_carved_pink_sandstone",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(5.0f)
                                        .requiresTool().sounds(BlockSoundGroup.STONE)));

        public static final Block FAITH_CARVED_REACH_BRICK = registerBlock(
                        "faith_carved_reach_brick",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(5.0f)
                                        .requiresTool().sounds(BlockSoundGroup.STONE)));

        public static final Block FAITH_CARVED_SMALL_STONE_BRICK = registerBlock(
                        "faith_carved_small_stone_brick",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(5.0f)
                                        .requiresTool().sounds(BlockSoundGroup.STONE)));

        public static final Block FAITH_CARVED_STONE_BRICK = registerBlock(
                        "faith_carved_stone_brick",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(5.0f)
                                        .requiresTool().sounds(BlockSoundGroup.STONE)));

        public static final Block FAITH_CARVED_STORMLANDS_BRICK = registerBlock(
                        "faith_carved_stormlands_brick",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(5.0f)
                                        .requiresTool().sounds(BlockSoundGroup.STONE)));

        public static final Block FAITH_CARVED_WESTERLANDS_BRICK = registerBlock(
                        "faith_carved_westerlands_brick",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(5.0f)
                                        .requiresTool().sounds(BlockSoundGroup.STONE)));

        public static final Block FISH_BARREL = registerBlock(
                        "fish_barrel",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block FISH_BASKET = registerBlock(
                        "fish_basket",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block FISH_TRAP = registerBlock(
                        "fish_trap",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD).nonOpaque()));

        public static final Block FLAGSTONE = registerBlock(
                        "flagstone",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.STONE)));

        public static final Block FULL_CABINET = registerBlock(
                        "full_cabinet",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block GLOWING_EMBERS = registerBlock(
                        "glowing_embers",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)
                                        .luminance(state -> 2)));

        public static final Block GRAIN_BASKET = registerBlock(
                        "grain_basket",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block GRAIN_CRATE = registerBlock(
                        "grain_crate",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block GREEN_GREY_BRICK_ENGRAVED = registerBlock(
                        "green_grey_brick_engraved",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(5.0f)
                                        .requiresTool().sounds(BlockSoundGroup.STONE)));

        public static final Block GREY_BRICK_ENGRAVED = registerBlock(
                        "grey_brick_engraved",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(5.0f)
                                        .requiresTool().sounds(BlockSoundGroup.STONE)));

        public static final Block GREY_KEYSTONE = registerBlock(
                        "grey_keystone",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(3.0f)
                                        .requiresTool().sounds(BlockSoundGroup.STONE)));

        public static final Block HIGH_CLASS_UTILITY_BLOCK = registerBlock(
                        "high_class_utility_block",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(5.0f)
                                        .requiresTool().sounds(BlockSoundGroup.METAL)));

        public static final Block HOP_BASKET = registerBlock(
                        "hop_basket",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block HOP_CRATE = registerBlock(
                        "hop_crate",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block HOUSE_COUNT_UTILITY_BLOCK = registerBlock(
                        "house_count_utility_block",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(5.0f)
                                        .requiresTool().sounds(BlockSoundGroup.METAL)));

        public static final Block INDUSTRY_UTILITY_BLOCK = registerBlock(
                        "industry_utility_block",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(5.0f)
                                        .requiresTool().sounds(BlockSoundGroup.METAL)));

        public static final Block IRON_CRATE = registerBlock(
                        "iron_crate",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block KL_DUN_CARVED_BRICK = registerBlock(
                        "kl_dun_carved_brick",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(5.0f)
                                        .requiresTool().sounds(BlockSoundGroup.STONE)));

        public static final Block LANNISPORT_KEYSTONE_ORANGE_PLASTER = registerBlock(
                        "lannisport_keystone_orange_plaster",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(5.0f)
                                        .requiresTool().sounds(BlockSoundGroup.STONE)));

        public static final Block LANNISPORT_KEYSTONE_YELLOW_PLASTER = registerBlock(
                        "lannisport_keystone_yellow_plaster",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(5.0f)
                                        .requiresTool().sounds(BlockSoundGroup.STONE)));

        public static final Block LARGE_CLAY_POT_SOLID = registerBlock(
                        "large_clay_pot_solid",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(5.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block LAVENDER_BASKET = registerBlock(
                        "lavender_basket",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block LAVENDER_CRATE = registerBlock(
                        "lavender_crate",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block LEMON_BASKET = registerBlock(
                        "lemon_basket",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block LIGHT_GREY_BRICK_ENGRAVED = registerBlock(
                        "light_grey_brick_engraved",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(5.0f)
                                        .requiresTool().sounds(BlockSoundGroup.STONE)));

        public static final Block LIGHT_GREY_STONE_WHITE_PLASTER = registerBlock(
                        "light_grey_stone_white_plaster",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(5.0f)
                                        .requiresTool().sounds(BlockSoundGroup.STONE)));

        public static final Block LIGHT_OLDTOWN_BRICK_ENGRAVED = registerBlock(
                        "light_oldtown_brick_engraved",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(5.0f)
                                        .requiresTool().sounds(BlockSoundGroup.STONE)));

        public static final Block LIME_BASKET = registerBlock(
                        "lime_basket",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block LOW_CLASS_UTILITY_BLOCK = registerBlock(
                        "low_class_utility_block",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(5.0f)
                                        .requiresTool().sounds(BlockSoundGroup.METAL)));

        public static final Block MIDDLE_CLASS_UTILITY_BLOCK = registerBlock(
                        "middle_class_utility_block",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(5.0f)
                                        .requiresTool().sounds(BlockSoundGroup.METAL)));

        public static final Block MIRROR_BLOCK = registerBlock(
                        "mirror_block",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(3.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block MONOCHROME_DARK_SANDSTONE_ENGRAVED = registerBlock(
                        "monochrome_dark_sandstone_engraved",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(5.0f)
                                        .requiresTool().sounds(BlockSoundGroup.STONE)));

        public static final Block MONOCHROME_SANDSTONE_ENGRAVED = registerBlock(
                        "monochrome_sandstone_engraved",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(5.0f)
                                        .requiresTool().sounds(BlockSoundGroup.STONE)));

        public static final Block NETHER_BRICK_KEYSTONE = registerBlock(
                        "nether_brick_keystone",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(3.0f)
                                        .requiresTool().sounds(BlockSoundGroup.STONE)));

        public static final Block NORTHERN_CARVINGS = registerBlock(
                        "northern_carvings",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block NOTE_UTILITY_BLOCK = registerBlock(
                        "note_utility_block",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(5.0f)
                                        .requiresTool().sounds(BlockSoundGroup.METAL)));

        public static final Block OLIVE_BASKET = registerBlock(
                        "olive_basket",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block OPEN_BASKET = registerBlock(
                        "open_basket",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block OPEN_CRATE = registerBlock(
                        "open_crate",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block ORANGE_BASKET = registerBlock(
                        "orange_basket",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block ORANGE_BRICK_ARCH_DOUBLE = registerBlock(
                        "orange_brick_arch_double",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(3.0f)
                                        .requiresTool().sounds(BlockSoundGroup.STONE)));

        public static final Block ORANGE_BRICK_ARCH_SINGLE = registerBlock(
                        "orange_brick_arch_single",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(3.0f)
                                        .requiresTool().sounds(BlockSoundGroup.STONE)));

        public static final Block ORANGE_BRICK_DENTIL = registerBlock(
                        "orange_brick_dentil",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(3.0f)
                                        .requiresTool().sounds(BlockSoundGroup.STONE)));

        public static final Block ORANGE_BRICK_ROWLOCK = registerBlock(
                        "orange_brick_rowlock",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(3.0f)
                                        .requiresTool().sounds(BlockSoundGroup.STONE)));

        public static final Block ORNATE_MARBLE = registerBlock(
                        "ornate_marble",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(5.0f)
                                        .requiresTool().sounds(BlockSoundGroup.STONE)));

        public static final Block ORNATE_SANDSTONE = registerBlock(
                        "ornate_sandstone",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(5.0f)
                                        .requiresTool().sounds(BlockSoundGroup.STONE)));

        public static final Block PARQUET_FLOOR = registerBlock(
                        "parquet_floor",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block PILED_BONES = registerBlock(
                        "piled_bones",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(5.0f)
                                        .requiresTool().sounds(BlockSoundGroup.STONE)));

        public static final Block PINK_SANDSTONE_ENGRAVED = registerBlock(
                        "pink_sandstone_engraved",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(5.0f)
                                        .requiresTool().sounds(BlockSoundGroup.STONE)));

        public static final Block PISTON_TOP = registerBlock(
                        "piston_top",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block POMEGRANATE_BASKET = registerBlock(
                        "pomegranate_basket",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block PURPLE_GRAPE_BASKET = registerBlock(
                        "purple_grape_basket",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block PURPLE_GRAPE_CRATE = registerBlock(
                        "purple_grape_crate",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block REACH_BRICK_ENGRAVED = registerBlock(
                        "reach_brick_engraved",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(5.0f)
                                        .requiresTool().sounds(BlockSoundGroup.STONE)));

        public static final Block REACH_OAK_WOOD_PANELLING = registerBlock(
                        "reach_oak_wood_panelling",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(5.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block REDORANGE_CARVED_SANDSTONE = registerBlock(
                        "redorange_carved_sandstone",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(5.0f)
                                        .requiresTool().sounds(BlockSoundGroup.STONE)));

        public static final Block RED_LANTERN2 = registerBlock(
                        "red_lantern2",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)
                                        .luminance(state -> 7)));

        public static final Block REACH_SPRUCE_WOOD_PANELLING = registerBlock(
                        "reach_spruce_wood_panelling",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(5.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block SALT_CRATE = registerBlock(
                        "salt_crate",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block SANDY_STONE_SLABS = registerBlock(
                        "sandy_stone_slabs",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.STONE)));

        public static final Block SEPT_CRYSTAL_LARGE = registerBlock(
                        "sept_crystal_large",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.STONE)
                                        .nonOpaque()));

        public static final Block SHOP_UTILITY_BLOCK = registerBlock(
                        "shop_utility_block",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(5.0f)
                                        .requiresTool().sounds(BlockSoundGroup.METAL)));

        public static final Block SILVER_TIN_CRATE = registerBlock(
                        "silver_tin_crate",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block SMALL_ORANGE_BRICKS_ORNATE_TOP = registerBlock(
                        "small_orange_bricks_ornate_top",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(3.0f)
                                        .requiresTool().sounds(BlockSoundGroup.STONE)));

        public static final Block SMALL_ORANGE_BRICKS_ORNATE = registerBlock(
                        "small_orange_bricks_ornate",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(3.0f)
                                        .requiresTool().sounds(BlockSoundGroup.STONE)));

        public static final Block SMALL_SMOOTH_STONE_BRICK_BLUE_PLASTER = registerBlock(
                        "small_smooth_stone_brick_blue_plaster",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(3.0f)
                                        .requiresTool().sounds(BlockSoundGroup.STONE)));

        public static final Block SMALL_SMOOTH_STONE_BRICK_WHITE_PLASTER = registerBlock(
                        "small_smooth_stone_brick_white_plaster",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(3.0f)
                                        .requiresTool().sounds(BlockSoundGroup.STONE)));

        public static final Block SMALL_STONE_BRICK_WHITE_PLASTER = registerBlock(
                        "small_stone_brick_white_plaster",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(3.0f)
                                        .requiresTool().sounds(BlockSoundGroup.STONE)));

        public static final Block SMALL_WHITE_BRICK_BROWNISH_WHITE_PLASTER = registerBlock(
                        "small_white_brick_brownish_white_plaster",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(3.0f)
                                        .requiresTool().sounds(BlockSoundGroup.STONE)));

        public static final Block SMALL_WHITE_BRICK_WHITE_PLASTER = registerBlock(
                        "small_white_brick_white_plaster",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(3.0f)
                                        .requiresTool().sounds(BlockSoundGroup.STONE)));

        public static final Block SOURLEAF_BASKET = registerBlock(
                        "sourleaf_basket",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block SOURLEAF_CRATE = registerBlock(
                        "sourleaf_crate",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block SOUTHERN_BRICK_ARCH_FLAT = registerBlock(
                        "southern_brick_arch_flat",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(3.0f)
                                        .requiresTool().sounds(BlockSoundGroup.STONE)));

        public static final Block SOUTHERN_BRICK_ARCH = registerBlock(
                        "southern_brick_arch",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(3.0f)
                                        .requiresTool().sounds(BlockSoundGroup.STONE)));

        public static final Block SOUTHERN_BRICK_LINTEL = registerBlock(
                        "southern_brick_lintel",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(3.0f)
                                        .requiresTool().sounds(BlockSoundGroup.STONE)));

        public static final Block SPECIAL_UTILITY_BLOCK = registerBlock(
                        "special_utility_block",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(5.0f)
                                        .requiresTool().sounds(BlockSoundGroup.METAL)));

        public static final Block SPIT_ROAST = registerBlock(
                        "spit_roast",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block SQUASH = registerBlock(
                        "squash",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.GRASS)));

        public static final Block STACKED_BONES_SOLID = registerBlock(
                        "stacked_bones_solid",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(5.0f)
                                        .requiresTool().sounds(BlockSoundGroup.STONE)));

        public static final Block STORMLANDS_BRICK_ENGRAVED = registerBlock(
                        "stormlands_brick_engraved",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(5.0f)
                                        .requiresTool().sounds(BlockSoundGroup.STONE)));

        public static final Block TABLE_BOOKS = registerBlock(
                        "table_books",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block TABLE_DRAWERS = registerBlock(
                        "table_drawers",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block THICK_GRASS_BLOCK = registerBlock(
                        "thick_grass_block",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(5.0f)
                                        .requiresTool().sounds(BlockSoundGroup.GRASS)));

        public static final Block TABLE_WIDGETS = registerBlock(
                        "table_widgets",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(2.0f)
                                        .requiresTool().sounds(BlockSoundGroup.WOOD)));

        public static final Block TERRACOTTA_ENGRAVED = registerBlock(
                        "terracotta_engraved",
                        new WCSolidBlock(AbstractBlock.Settings.create().strength(3.0f)
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
                        entries.add(ModBlocks.EMPTY_BARREL);
                        entries.add(ModBlocks.EMPTY_CABINET);
                        entries.add(ModBlocks.FAITH_CARVED_ARBOR_BRICK);
                        entries.add(ModBlocks.FAITH_CARVED_BLACK_BRICK);
                        entries.add(ModBlocks.FAITH_CARVED_BROWN_GREY_BRICK);
                        entries.add(ModBlocks.FAITH_CARVED_COARSE_RED_BRICK);
                        entries.add(ModBlocks.FAITH_CARVED_DARK_GREY_BRICK);
                        entries.add(ModBlocks.FAITH_CARVED_DUN_BRICK);
                        entries.add(ModBlocks.FAITH_CARVED_GREY_BRICK);
                        entries.add(ModBlocks.FAITH_CARVED_OLDTOWN_BRICK);
                        entries.add(ModBlocks.FAITH_CARVED_PINK_SANDSTONE);
                        entries.add(ModBlocks.FAITH_CARVED_REACH_BRICK);
                        entries.add(ModBlocks.FAITH_CARVED_SMALL_STONE_BRICK);
                        entries.add(ModBlocks.FAITH_CARVED_STONE_BRICK);
                        entries.add(ModBlocks.FAITH_CARVED_STORMLANDS_BRICK);
                        entries.add(ModBlocks.FAITH_CARVED_WESTERLANDS_BRICK);
                        entries.add(ModBlocks.FISH_BARREL);
                        entries.add(ModBlocks.FISH_BASKET);
                        entries.add(ModBlocks.FISH_TRAP);
                        entries.add(ModBlocks.FLAGSTONE);
                        entries.add(ModBlocks.FULL_CABINET);
                        entries.add(ModBlocks.GLOWING_EMBERS);
                        entries.add(ModBlocks.GRAIN_BASKET);
                        entries.add(ModBlocks.GRAIN_CRATE);
                        entries.add(ModBlocks.GREEN_GREY_BRICK_ENGRAVED);
                        entries.add(ModBlocks.GREY_BRICK_ENGRAVED);
                        entries.add(ModBlocks.GREY_KEYSTONE);
                        entries.add(ModBlocks.HIGH_CLASS_UTILITY_BLOCK);
                        entries.add(ModBlocks.HOP_BASKET);
                        entries.add(ModBlocks.HOP_CRATE);
                        entries.add(ModBlocks.HOUSE_COUNT_UTILITY_BLOCK);
                        entries.add(ModBlocks.INDUSTRY_UTILITY_BLOCK);
                        entries.add(ModBlocks.IRON_CRATE);
                        entries.add(ModBlocks.KL_DUN_CARVED_BRICK);
                        entries.add(ModBlocks.LANNISPORT_KEYSTONE_ORANGE_PLASTER);
                        entries.add(ModBlocks.LANNISPORT_KEYSTONE_YELLOW_PLASTER);
                        entries.add(ModBlocks.LARGE_CLAY_POT_SOLID);
                        entries.add(ModBlocks.LAVENDER_BASKET);
                        entries.add(ModBlocks.LAVENDER_CRATE);
                        entries.add(ModBlocks.LEMON_BASKET);
                        entries.add(ModBlocks.LIGHT_GREY_BRICK_ENGRAVED);
                        entries.add(ModBlocks.LIGHT_GREY_STONE_WHITE_PLASTER);
                        entries.add(ModBlocks.LIGHT_OLDTOWN_BRICK_ENGRAVED);
                        entries.add(ModBlocks.LIME_BASKET);
                        entries.add(ModBlocks.LOW_CLASS_UTILITY_BLOCK);
                        entries.add(ModBlocks.MIDDLE_CLASS_UTILITY_BLOCK);
                        entries.add(ModBlocks.MIRROR_BLOCK);
                        entries.add(ModBlocks.MONOCHROME_DARK_SANDSTONE_ENGRAVED);
                        entries.add(ModBlocks.MONOCHROME_SANDSTONE_ENGRAVED);
                        entries.add(ModBlocks.NETHER_BRICK_KEYSTONE);
                        entries.add(ModBlocks.NORTHERN_CARVINGS);
                        entries.add(ModBlocks.NOTE_UTILITY_BLOCK);
                        entries.add(ModBlocks.OLIVE_BASKET);
                        entries.add(ModBlocks.OPEN_BASKET);
                        entries.add(ModBlocks.OPEN_CRATE);
                        entries.add(ModBlocks.ORNATE_MARBLE);
                        entries.add(ModBlocks.ORNATE_SANDSTONE);
                        entries.add(ModBlocks.ORANGE_BASKET);
                        entries.add(ModBlocks.ORANGE_BRICK_ARCH_DOUBLE);
                        entries.add(ModBlocks.ORANGE_BRICK_ARCH_SINGLE);
                        entries.add(ModBlocks.ORANGE_BRICK_DENTIL);
                        entries.add(ModBlocks.ORANGE_BRICK_ROWLOCK);
                        entries.add(ModBlocks.ORNATE_MARBLE);
                        entries.add(ModBlocks.ORNATE_SANDSTONE);
                        entries.add(ModBlocks.PARQUET_FLOOR);
                        entries.add(ModBlocks.PILED_BONES);
                        entries.add(ModBlocks.PINK_SANDSTONE_ENGRAVED);
                        entries.add(ModBlocks.PISTON_TOP);
                        entries.add(ModBlocks.POMEGRANATE_BASKET);
                        entries.add(ModBlocks.PURPLE_GRAPE_BASKET);
                        entries.add(ModBlocks.PURPLE_GRAPE_CRATE);
                        entries.add(ModBlocks.REACH_BRICK_ENGRAVED);
                        entries.add(ModBlocks.REACH_OAK_WOOD_PANELLING);
                        entries.add(ModBlocks.REDORANGE_CARVED_SANDSTONE);
                        entries.add(ModBlocks.RED_LANTERN2);
                        entries.add(ModBlocks.REACH_SPRUCE_WOOD_PANELLING);
                        entries.add(ModBlocks.SALT_CRATE);
                        entries.add(ModBlocks.SANDY_STONE_SLABS);
                        entries.add(ModBlocks.SEPT_CRYSTAL_LARGE);
                        entries.add(ModBlocks.SHOP_UTILITY_BLOCK);
                        entries.add(ModBlocks.SILVER_TIN_CRATE);
                        entries.add(ModBlocks.SMALL_ORANGE_BRICKS_ORNATE_TOP);
                        entries.add(ModBlocks.SMALL_ORANGE_BRICKS_ORNATE);
                        entries.add(ModBlocks.SMALL_SMOOTH_STONE_BRICK_BLUE_PLASTER);
                        entries.add(ModBlocks.SMALL_SMOOTH_STONE_BRICK_WHITE_PLASTER);
                        entries.add(ModBlocks.SMALL_STONE_BRICK_WHITE_PLASTER);
                        entries.add(ModBlocks.SMALL_WHITE_BRICK_BROWNISH_WHITE_PLASTER);
                        entries.add(ModBlocks.SMALL_WHITE_BRICK_WHITE_PLASTER);
                        entries.add(ModBlocks.SOURLEAF_BASKET);
                        entries.add(ModBlocks.SOURLEAF_CRATE);
                        entries.add(ModBlocks.SOUTHERN_BRICK_ARCH_FLAT);
                        entries.add(ModBlocks.SOUTHERN_BRICK_ARCH);
                        entries.add(ModBlocks.SOUTHERN_BRICK_LINTEL);
                        entries.add(ModBlocks.SPECIAL_UTILITY_BLOCK);
                        entries.add(ModBlocks.SPIT_ROAST);
                        entries.add(ModBlocks.SQUASH);
                        entries.add(ModBlocks.STACKED_BONES_SOLID);
                        entries.add(ModBlocks.STORMLANDS_BRICK_ENGRAVED);
                        entries.add(ModBlocks.TABLE_BOOKS);
                        entries.add(ModBlocks.TABLE_DRAWERS);
                        entries.add(ModBlocks.THICK_GRASS_BLOCK);
                        entries.add(ModBlocks.TABLE_WIDGETS);
                        entries.add(ModBlocks.TERRACOTTA_ENGRAVED);
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
