package com.westerosblocks.block;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.WesterosCreativeModeTabs;
import com.westerosblocks.block.custom.BlockBuilder;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import java.util.List;

public class ModBlocks {

    // Solid Blocks
    public static final Block SIX_SIDED_BIRCH = registerBlock(
            "6sided_birch",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block SIX_SIDED_JUNGLE = registerBlock(
            "6sided_jungle",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block SIX_SIDED_OAK = registerBlock(
            "6sided_oak",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block SIX_SIDED_SPRUCE = registerBlock(
            "6sided_spruce",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block SIX_SIDED_STONE_SLAB = registerBlock(
            "6sided_stone_slab",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block APPLE_BASKET = registerBlock(
            "apple_basket",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block APPLE_CRATE = registerBlock(
            "apple_crate",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block APPROVAL_UTILITY_BLOCK = registerBlock(
            "approval_utility_block",
            BlockBuilder.solid()
                    .strength(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.METAL)
                    .build());

    public static final Block ARBOR_BRICK_ORNATE = registerBlock(
            "arbor_brick_ornate",
            BlockBuilder.solid()
                    .strength(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block BENCH_BUTCHER_KNIVES = registerBlock(
            "bench_butcher_knives",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block BENCH_CARPENTRY_HAMMER_SAW = registerBlock(
            "bench_carpentry_hammer_saw",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block BENCH_DRAWERS = registerBlock(
            "bench_drawers",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block BENCH_KITCHEN_KNIVES = registerBlock(
            "bench_kitchen_knives",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block BENCH_KITCHEN_PANS = registerBlock(
            "bench_kitchen_pans",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block BENCH_MASON_HAMMER_MALLET = registerBlock(
            "bench_mason_hammer_mallet",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block BERRY_BASKET = registerBlock(
            "berry_basket",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block BERRY_CRATE = registerBlock(
            "berry_crate",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block APRICOT_BASKET = registerBlock(
            "apricot_basket",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block BLACK_BRICK_ENGRAVED = registerBlock(
            "black_brick_engraved",
            BlockBuilder.solid()
                    .strength(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block BLUEGREEN_CARVED_SANDSTONE = registerBlock(
            "bluegreen_carved_sandstone",
            BlockBuilder.solid()
                    .strength(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block BONE_DIRT = registerBlock(
            "bone_dirt",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.GRASS)
                    .build());

    public static final Block BOOKSHELF_ABANDONED = registerBlock(
            "bookshelf_abandoned",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block BOOKSHELF_LIBRARY = registerBlock(
            "bookshelf_library",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block BOOKSHELF_MAESTER = registerBlock(
            "bookshelf_maester",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block BROKEN_CABINET = registerBlock(
            "broken_cabinet",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block BROWN_GREY_BRICK_ENGRAVED = registerBlock(
            "brown_grey_brick_engraved",
            BlockBuilder.solid()
                    .strength(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block CABINET_DRAWER = registerBlock(
            "cabinet_drawer",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block CAGE = registerBlock(
            "cage",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .nonOpaque()
                    .build());

    public static final Block CARROT_BASKET = registerBlock(
            "carrot_basket",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block CARROT_CRATE = registerBlock(
            "carrot_crate",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block CLOSED_BASKET = registerBlock(
            "closed_basket",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block CLOSED_CABINET = registerBlock(
            "closed_cabinet",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block COARSE_DARK_RED_CARVED_SANDSTONE = registerBlock(
            "coarse_dark_red_carved_sandstone",
            BlockBuilder.solid()
                    .strength(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block COARSE_RED_CARVED_SANDSTONE = registerBlock(
            "coarse_red_carved_sandstone",
            BlockBuilder.solid()
                    .strength(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block COBBLE_KEYSTONE = registerBlock(
            "cobble_keystone",
            BlockBuilder.solid()
                    .strength(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .connectState(true)
                    .build());

    public static final Block COLOURED_SEPT_WINDOW = registerBlock(
            "coloured_sept_window",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .nonOpaque()
                    .build());

    public static final Block CRATE = registerBlock(
            "crate",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block CRATE2 = registerBlock(
            "crate2",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block CRATE3 = registerBlock(
            "crate3",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block DARK_GREY_BRICK_ENGRAVED = registerBlock(
            "dark_grey_brick_engraved",
            BlockBuilder.solid()
                    .strength(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block DATE_BASKET = registerBlock(
            "date_basket",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block DATES = registerBlock(
            "dates",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.GRASS)
                    .build());

    public static final Block DESERT_SANDSTONE_ENGRAVED = registerBlock(
            "desert_sandstone_engraved",
            BlockBuilder.solid()
                    .strength(3.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block DOMESTIC_UTILITY_BLOCK = registerBlock(
            "domestic_utility_block",
            BlockBuilder.solid()
                    .strength(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.METAL)
                    .build());

    public static final Block DONE_UTILITY_BLOCK = registerBlock(
            "done_utility_block",
            BlockBuilder.solid()
                    .strength(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.METAL)
                    .build());

    public static final Block DRAGON_CARVING = registerBlock(
            "dragon_carving",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block EMPTY_BARREL = registerBlock(
            "empty_barrel",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block EMPTY_CABINET = registerBlock(
            "empty_cabinet",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block FAITH_CARVED_ARBOR_BRICK = registerBlock(
            "faith_carved_arbor_brick",
            BlockBuilder.solid()
                    .strength(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block FAITH_CARVED_BLACK_BRICK = registerBlock(
            "faith_carved_black_brick",
            BlockBuilder.solid()
                    .strength(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block FAITH_CARVED_BROWN_GREY_BRICK = registerBlock(
            "faith_carved_brown_grey_brick",
            BlockBuilder.solid()
                    .strength(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block FAITH_CARVED_COARSE_RED_BRICK = registerBlock(
            "faith_carved_coarse_red_brick",
            BlockBuilder.solid()
                    .strength(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block FAITH_CARVED_DARK_GREY_BRICK = registerBlock(
            "faith_carved_dark_grey_brick",
            BlockBuilder.solid()
                    .strength(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block FAITH_CARVED_DUN_BRICK = registerBlock(
            "faith_carved_dun_brick",
            BlockBuilder.solid()
                    .strength(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block FAITH_CARVED_GREY_BRICK = registerBlock(
            "faith_carved_grey_brick",
            BlockBuilder.solid()
                    .strength(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block FAITH_CARVED_OLDTOWN_BRICK = registerBlock(
            "faith_carved_oldtown_brick",
            BlockBuilder.solid()
                    .strength(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block FAITH_CARVED_PINK_SANDSTONE = registerBlock(
            "faith_carved_pink_sandstone",
            BlockBuilder.solid()
                    .strength(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block FAITH_CARVED_REACH_BRICK = registerBlock(
            "faith_carved_reach_brick",
            BlockBuilder.solid()
                    .strength(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block FAITH_CARVED_SMALL_STONE_BRICK = registerBlock(
            "faith_carved_small_stone_brick",
            BlockBuilder.solid()
                    .strength(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block FAITH_CARVED_STONE_BRICK = registerBlock(
            "faith_carved_stone_brick",
            BlockBuilder.solid()
                    .strength(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block FAITH_CARVED_STORMLANDS_BRICK = registerBlock(
            "faith_carved_stormlands_brick",
            BlockBuilder.solid()
                    .strength(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block FAITH_CARVED_WESTERLANDS_BRICK = registerBlock(
            "faith_carved_westerlands_brick",
            BlockBuilder.solid()
                    .strength(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block FISH_BARREL = registerBlock(
            "fish_barrel",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block FISH_BASKET = registerBlock(
            "fish_basket",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block FISH_TRAP = registerBlock(
            "fish_trap",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .nonOpaque()
                    .build());

    public static final Block FLAGSTONE = registerBlock(
            "flagstone",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block FULL_CABINET = registerBlock(
            "full_cabinet",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block GLOWING_EMBERS = registerBlock(
            "glowing_embers",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .luminance(state -> 2)
                    .build());

    public static final Block GRAIN_BASKET = registerBlock(
            "grain_basket",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block GRAIN_CRATE = registerBlock(
            "grain_crate",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block GREEN_GREY_BRICK_ENGRAVED = registerBlock(
            "green_grey_brick_engraved",
            BlockBuilder.solid()
                    .strength(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block GREY_BRICK_ENGRAVED = registerBlock(
            "grey_brick_engraved",
            BlockBuilder.solid()
                    .strength(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block GREY_KEYSTONE = registerBlock(
            "grey_keystone",
            BlockBuilder.solid()
                    .strength(3.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block HIGH_CLASS_UTILITY_BLOCK = registerBlock(
            "high_class_utility_block",
            BlockBuilder.solid()
                    .strength(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.METAL)
                    .build());

    public static final Block HOP_BASKET = registerBlock(
            "hop_basket",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block HOP_CRATE = registerBlock(
            "hop_crate",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block HOUSE_COUNT_UTILITY_BLOCK = registerBlock(
            "house_count_utility_block",
            BlockBuilder.solid()
                    .strength(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.METAL)
                    .build());

    public static final Block INDUSTRY_UTILITY_BLOCK = registerBlock(
            "industry_utility_block",
            BlockBuilder.solid()
                    .strength(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.METAL)
                    .build());

    public static final Block IRON_CRATE = registerBlock(
            "iron_crate",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block KL_DUN_CARVED_BRICK = registerBlock(
            "kl_dun_carved_brick",
            BlockBuilder.solid()
                    .strength(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block LANNISPORT_KEYSTONE_ORANGE_PLASTER = registerBlock(
            "lannisport_keystone_orange_plaster",
            BlockBuilder.solid()
                    .strength(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block LANNISPORT_KEYSTONE_YELLOW_PLASTER = registerBlock(
            "lannisport_keystone_yellow_plaster",
            BlockBuilder.solid()
                    .strength(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block LARGE_CLAY_POT_SOLID = registerBlock(
            "large_clay_pot_solid",
            BlockBuilder.solid()
                    .strength(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block LAVENDER_BASKET = registerBlock(
            "lavender_basket",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block LAVENDER_CRATE = registerBlock(
            "lavender_crate",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block LEMON_BASKET = registerBlock(
            "lemon_basket",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block LIGHT_GREY_BRICK_ENGRAVED = registerBlock(
            "light_grey_brick_engraved",
            BlockBuilder.solid()
                    .strength(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block LIGHT_GREY_STONE_WHITE_PLASTER = registerBlock(
            "light_grey_stone_white_plaster",
            BlockBuilder.solid()
                    .strength(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block LIGHT_OLDTOWN_BRICK_ENGRAVED = registerBlock(
            "light_oldtown_brick_engraved",
            BlockBuilder.solid()
                    .strength(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block LIME_BASKET = registerBlock(
            "lime_basket",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block LOW_CLASS_UTILITY_BLOCK = registerBlock(
            "low_class_utility_block",
            BlockBuilder.solid()
                    .strength(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.METAL)
                    .build());

    public static final Block MIDDLE_CLASS_UTILITY_BLOCK = registerBlock(
            "middle_class_utility_block",
            BlockBuilder.solid()
                    .strength(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.METAL)
                    .build());

    public static final Block MIRROR_BLOCK = registerBlock(
            "mirror_block",
            BlockBuilder.solid()
                    .strength(3.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block MONOCHROME_DARK_SANDSTONE_ENGRAVED = registerBlock(
            "monochrome_dark_sandstone_engraved",
            BlockBuilder.solid()
                    .strength(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block MONOCHROME_SANDSTONE_ENGRAVED = registerBlock(
            "monochrome_sandstone_engraved",
            BlockBuilder.solid()
                    .strength(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block NETHER_BRICK_KEYSTONE = registerBlock(
            "nether_brick_keystone",
            BlockBuilder.solid()
                    .strength(3.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block NORTHERN_CARVINGS = registerBlock(
            "northern_carvings",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block NOTE_UTILITY_BLOCK = registerBlock(
            "note_utility_block",
            BlockBuilder.solid()
                    .strength(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.METAL)
                    .build());

    public static final Block OLIVE_BASKET = registerBlock(
            "olive_basket",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block OPEN_BASKET = registerBlock(
            "open_basket",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block OPEN_CRATE = registerBlock(
            "open_crate",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block ORANGE_BASKET = registerBlock(
            "orange_basket",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block ORANGE_BRICK_ARCH_DOUBLE = registerBlock(
            "orange_brick_arch_double",
            BlockBuilder.solid()
                    .strength(3.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .connectState(true)
                    .build());

    public static final Block ORANGE_BRICK_ARCH_SINGLE = registerBlock(
            "orange_brick_arch_single",
            BlockBuilder.solid()
                    .strength(3.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .connectState(true)
                    .build());

    public static final Block ORANGE_BRICK_DENTIL = registerBlock(
            "orange_brick_dentil",
            BlockBuilder.solid()
                    .strength(3.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block ORANGE_BRICK_ROWLOCK = registerBlock(
            "orange_brick_rowlock",
            BlockBuilder.solid()
                    .strength(3.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block ORNATE_MARBLE = registerBlock(
            "ornate_marble",
            BlockBuilder.solid()
                    .strength(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block ORNATE_SANDSTONE = registerBlock(
            "ornate_sandstone",
            BlockBuilder.solid()
                    .strength(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block PARQUET_FLOOR = registerBlock(
            "parquet_floor",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block PILED_BONES = registerBlock(
            "piled_bones",
            BlockBuilder.solid()
                    .strength(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block PINK_SANDSTONE_ENGRAVED = registerBlock(
            "pink_sandstone_engraved",
            BlockBuilder.solid()
                    .strength(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block PISTON_TOP = registerBlock(
            "piston_top",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block POMEGRANATE_BASKET = registerBlock(
            "pomegranate_basket",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block PURPLE_GRAPE_BASKET = registerBlock(
            "purple_grape_basket",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block PURPLE_GRAPE_CRATE = registerBlock(
            "purple_grape_crate",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block REACH_BRICK_ENGRAVED = registerBlock(
            "reach_brick_engraved",
            BlockBuilder.solid()
                    .strength(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block REACH_OAK_WOOD_PANELLING = registerBlock(
            "reach_oak_wood_panelling",
            BlockBuilder.solid()
                    .strength(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block REDORANGE_CARVED_SANDSTONE = registerBlock(
            "redorange_carved_sandstone",
            BlockBuilder.solid()
                    .strength(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block RED_LANTERN2 = registerBlock(
            "red_lantern2",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .luminance(state -> 7)
                    .build());

    public static final Block REACH_SPRUCE_WOOD_PANELLING = registerBlock(
            "reach_spruce_wood_panelling",
            BlockBuilder.solid()
                    .strength(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block SALT_CRATE = registerBlock(
            "salt_crate",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block SANDY_STONE_SLABS = registerBlock(
            "sandy_stone_slabs",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block SEPT_CRYSTAL_LARGE = registerBlock(
            "sept_crystal_large",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .nonOpaque()
                    .build());

    public static final Block SHOP_UTILITY_BLOCK = registerBlock(
            "shop_utility_block",
            BlockBuilder.solid()
                    .strength(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.METAL)
                    .build());

    public static final Block SILVER_TIN_CRATE = registerBlock(
            "silver_tin_crate",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block SMALL_ORANGE_BRICKS_ORNATE_TOP = registerBlock(
            "small_orange_bricks_ornate_top",
            BlockBuilder.solid()
                    .strength(3.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block SMALL_ORANGE_BRICKS_ORNATE = registerBlock(
            "small_orange_bricks_ornate",
            BlockBuilder.solid()
                    .strength(3.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block SMALL_SMOOTH_STONE_BRICK_BLUE_PLASTER = registerBlock(
            "small_smooth_stone_brick_blue_plaster",
            BlockBuilder.solid()
                    .strength(3.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block SMALL_SMOOTH_STONE_BRICK_WHITE_PLASTER = registerBlock(
            "small_smooth_stone_brick_white_plaster",
            BlockBuilder.solid()
                    .strength(3.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block SMALL_STONE_BRICK_WHITE_PLASTER = registerBlock(
            "small_stone_brick_white_plaster",
            BlockBuilder.solid()
                    .strength(3.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block SMALL_WHITE_BRICK_BROWNISH_WHITE_PLASTER = registerBlock(
            "small_white_brick_brownish_white_plaster",
            BlockBuilder.solid()
                    .strength(3.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block SMALL_WHITE_BRICK_WHITE_PLASTER = registerBlock(
            "small_white_brick_white_plaster",
            BlockBuilder.solid()
                    .strength(3.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block SOURLEAF_BASKET = registerBlock(
            "sourleaf_basket",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block SOURLEAF_CRATE = registerBlock(
            "sourleaf_crate",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block SOUTHERN_BRICK_ARCH_FLAT = registerBlock(
            "southern_brick_arch_flat",
            BlockBuilder.solid()
                    .strength(3.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .connectState(true)
                    .build());

    public static final Block SOUTHERN_BRICK_ARCH = registerBlock(
            "southern_brick_arch",
            BlockBuilder.solid()
                    .strength(3.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .connectState(true)
                    .build());

    public static final Block SOUTHERN_BRICK_LINTEL = registerBlock(
            "southern_brick_lintel",
            BlockBuilder.solid()
                    .strength(3.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block SPECIAL_UTILITY_BLOCK = registerBlock(
            "special_utility_block",
            BlockBuilder.solid()
                    .strength(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.METAL)
                    .build());

    public static final Block SPIT_ROAST = registerBlock(
            "spit_roast",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block SQUASH = registerBlock(
            "squash",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.GRASS)
                    .build());

    public static final Block STACKED_BONES_SOLID = registerBlock(
            "stacked_bones_solid",
            BlockBuilder.solid()
                    .strength(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block STORMLANDS_BRICK_ENGRAVED = registerBlock(
            "stormlands_brick_engraved",
            BlockBuilder.solid()
                    .strength(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block TABLE_BOOKS = registerBlock(
            "table_books",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block TABLE_DRAWERS = registerBlock(
            "table_drawers",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block THICK_GRASS_BLOCK = registerBlock(
            "thick_grass_block",
            BlockBuilder.solid()
                    .strength(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.GRASS)
                    .build());

    public static final Block TABLE_WIDGETS = registerBlock(
            "table_widgets",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block OAK_TABLE = registerBlock(
            "oak_table",
            BlockBuilder.table()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());



    public static final Block TERRACOTTA_ENGRAVED = registerBlock(
            "terracotta_engraved",
            BlockBuilder.solid()
                    .strength(3.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block TURNIP_BASKET = registerBlock(
            "turnip_basket",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block TURNIP_CRATE = registerBlock(
            "turnip_crate",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block UNUSED_BROWN_PLASTER = registerBlock(
            "unused_brown_plaster",
            BlockBuilder.solid()
                    .strength(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block UNUSED_PURPLE_PLASTER = registerBlock(
            "unused_purple_plaster",
            BlockBuilder.solid()
                    .strength(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block VIVID_DARK_SANDSTONE_ENGRAVED = registerBlock(
            "vivid_dark_sandstone_engraved",
            BlockBuilder.solid()
                    .strength(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block VIVID_SANDSTONE_ENGRAVED = registerBlock(
            "vivid_sandstone_engraved",
            BlockBuilder.solid()
                    .strength(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block WATER_BARREL = registerBlock(
            "water_barrel",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block WHITE_BRICK_ENGRAVED = registerBlock(
            "white_brick_engraved",
            BlockBuilder.solid()
                    .strength(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block WHITE_GRAPE_BASKET = registerBlock(
            "white_grape_basket",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block WHITE_GRAPE_CRATE = registerBlock(
            "white_grape_crate",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block WINTERFELL_CARVING = registerBlock(
            "winterfell_carving",
            BlockBuilder.solid()
                    .strength(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block WIP_UTILITY_BLOCK = registerBlock(
            "wip_utility_block",
            BlockBuilder.solid()
                    .strength(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.METAL)
                    .build());

    public static final Block WORKSHOP_UTILITY_BLOCK = registerBlock(
            "workshop_utility_block",
            BlockBuilder.solid()
                    .strength(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.METAL)
                    .build());

    public static final Block YARD_UTILITY_BLOCK = registerBlock(
            "yard_utility_block",
            BlockBuilder.solid()
                    .strength(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.METAL)
                    .build());

    public static final Block YELLOW_STAINED_CLAY = registerBlock(
            "yellow_stained_clay",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    // Torch Blocks
    public static final Block WALL_TORCH = registerBlockWithoutBlockItem(
            "wall_torch",
            BlockBuilder.wallTorch()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.METAL)
                    .luminance(state -> 13)
                    .nonOpaque()
                    .noCollision()
                    .allowUnsupported(true)
                    .noParticle(false)
                    .build());

    public static final Block TORCH = registerBlock(
            "torch",
            BlockBuilder.torch()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.METAL)
                    .luminance(state -> 13)
                    .nonOpaque()
                    .noCollision()
                    .wallBlock(WALL_TORCH)
                    .allowUnsupported(true)
                    .noParticle(false)
                    .build());

    public static final Block WALL_TORCH_UNLIT = registerBlockWithoutBlockItem(
            "wall_torch_unlit",
            BlockBuilder.wallTorch()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.METAL)
                    .luminance(state -> 0)
                    .nonOpaque()
                    .noCollision()
                    .allowUnsupported(true)
                    .noParticle(true)
                    .build());

    public static final Block TORCH_UNLIT = registerBlock(
            "torch_unlit",
            BlockBuilder.torch()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.METAL)
                    .luminance(state -> 0)
                    .nonOpaque()
                    .noCollision()
                    .wallBlock(WALL_TORCH_UNLIT)
                    .allowUnsupported(true)
                    .noParticle(true)
                    .build());

    public static final Block WALL_CANDLE = registerBlockWithoutBlockItem(
            "wall_candle",
            BlockBuilder.wallTorch()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.CANDLE)
                    .luminance(state -> 10)
                    .nonOpaque()
                    .noCollision()
                    .allowUnsupported(true)
                    .noParticle(false)
                    .build());

    public static final Block CANDLE = registerBlock(
            "candle",
            BlockBuilder.torch()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.CANDLE)
                    .luminance(state -> 10)
                    .nonOpaque()
                    .noCollision()
                    .wallBlock(WALL_CANDLE)
                    .allowUnsupported(true)
                    .noParticle(false)
                    .build());

    public static final Block WALL_CANDLE_UNLIT = registerBlockWithoutBlockItem(
            "wall_candle_unlit",
            BlockBuilder.wallTorch()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.CANDLE)
                    .luminance(state -> 0)
                    .nonOpaque()
                    .noCollision()
                    .allowUnsupported(true)
                    .noParticle(true)
                    .build());

    public static final Block CANDLE_UNLIT = registerBlock(
            "candle_unlit",
            BlockBuilder.torch()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.CANDLE)
                    .luminance(state -> 0)
                    .nonOpaque()
                    .noCollision()
                    .wallBlock(WALL_CANDLE_UNLIT)
                    .allowUnsupported(true)
                    .noParticle(true)
                    .build());

    // Door Blocks
    public static final Block BIRCH_DOOR = registerBlock(
            "birch_door",
            BlockBuilder.door()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .woodType("birch")
                    .locked(false)
                    .build());

    public static final Block EYRIE_WEIRWOOD_DOOR = registerBlock(
            "eyrie_weirwood_door",
            BlockBuilder.door()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .woodType("weirwood")
                    .locked(false)
                    .build());

    public static final Block GREY_WOOD_DOOR = registerBlock(
            "grey_wood_door",
            BlockBuilder.door()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .woodType("oak")
                    .locked(false)
                    .build());

    public static final Block HARRENHAL_SECRET_DOOR = registerBlock(
            "harrenhal_secret_door",
            BlockBuilder.door()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .woodType("oak")
                    .locked(true)
                    .build());

    public static final Block JUNGLE_DOOR = registerBlock(
            "jungle_door",
            BlockBuilder.door()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .woodType("jungle")
                    .locked(false)
                    .build());

    public static final Block NORTHERN_WOOD_DOOR = registerBlock(
            "northern_wood_door",
            BlockBuilder.door()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .woodType("northern")
                    .locked(false)
                    .build());

    public static final Block OAK_DOOR = registerBlock(
            "oak_door",
            BlockBuilder.door()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .woodType("oak")
                    .locked(false)
                    .build());

    public static final Block RED_KEEP_SECRET_DOOR = registerBlock(
            "red_keep_secret_door",
            BlockBuilder.door()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .woodType("oak")
                    .locked(true)
                    .build());

    public static final Block SPRUCE_DOOR = registerBlock(
            "spruce_door",
            BlockBuilder.door()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .woodType("spruce")
                    .locked(false)
                    .build());

    public static final Block WHITE_WOOD_DOOR = registerBlock(
            "white_wood_door",
            BlockBuilder.door()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .woodType("white")
                    .locked(false)
                    .build());

    public static final Block LOCKED_BIRCH_DOOR = registerBlock(
            "locked_birch_door",
            BlockBuilder.door()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .woodType("birch")
                    .locked(true)
                    .allowUnsupported(true)
                    .build());

    public static final Block LOCKED_DARK_NORTHERN_WOOD_DOOR = registerBlock(
            "locked_dark_northern_wood_door",
            BlockBuilder.door()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .woodType("northern")
                    .locked(true)
                    .allowUnsupported(true)
                    .build());

    public static final Block LOCKED_GREY_WOOD_DOOR = registerBlock(
            "locked_grey_wood_door",
            BlockBuilder.door()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .woodType("oak")
                    .locked(true)
                    .allowUnsupported(true)
                    .build());

    public static final Block LOCKED_JUNGLE_DOOR = registerBlock(
            "locked_jungle_door",
            BlockBuilder.door()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .woodType("jungle")
                    .locked(true)
                    .allowUnsupported(true)
                    .build());

    public static final Block LOCKED_OAK_DOOR = registerBlock(
            "locked_oak_door",
            BlockBuilder.door()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .woodType("oak")
                    .locked(true)
                    .allowUnsupported(true)
                    .build());

    public static final Block LOCKED_SPRUCE_DOOR = registerBlock(
            "locked_spruce_door",
            BlockBuilder.door()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .woodType("spruce")
                    .locked(true)
                    .allowUnsupported()
                    .build());

    public static final Block LOCKED_WHITE_WOOD_DOOR = registerBlock(
            "locked_white_wood_door",
            BlockBuilder.door()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .woodType("white")
                    .locked(true)
                    .allowUnsupported()
                    .build());

    // Half Door Blocks (Shutters)
    public static final Block BIRCH_WINDOW_SHUTTERS = registerBlock(
            "birch_window_shutters",
            BlockBuilder.halfDoor()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .allowUnsupported()
                    .build());

    public static final Block DORNE_RED_WINDOW_SHUTTERS = registerBlock(
            "dorne_red_window_shutters",
            BlockBuilder.halfDoor()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .allowUnsupported()
                    .build());

    public static final Block GREEN_LANNISPORT_WINDOW_SHUTTERS = registerBlock(
            "green_lannisport_window_shutters",
            BlockBuilder.halfDoor()
                    .strength(2.0f)
                    .resistance(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .allowUnsupported()
                    .build());

    public static final Block GREY_WOOD_WINDOW_SHUTTERS = registerBlock(
            "grey_wood_window_shutters",
            BlockBuilder.halfDoor()
                    .strength(2.0f)
                    .resistance(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .allowUnsupported()
                    .build());

    public static final Block JUNGLE_WINDOW_SHUTTERS = registerBlock(
            "jungle_window_shutters",
            BlockBuilder.halfDoor()
                    .strength(2.0f)
                    .resistance(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .allowUnsupported()
                    .build());

    public static final Block NORTHERN_WOOD_WINDOW_SHUTTERS = registerBlock(
            "northern_wood_window_shutters",
            BlockBuilder.halfDoor()
                    .strength(2.0f)
                    .resistance(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .allowUnsupported()
                    .build());

    public static final Block OAK_WINDOW_SHUTTERS = registerBlock(
            "oak_window_shutters",
            BlockBuilder.halfDoor()
                    .strength(2.0f)
                    .resistance(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .allowUnsupported()
                    .build());

    public static final Block REACH_BLUE_WINDOW_SHUTTERS = registerBlock(
            "reach_blue_window_shutters",
            BlockBuilder.halfDoor()
                    .strength(2.0f)
                    .resistance(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .allowUnsupported()
                    .build());

    public static final Block SPRUCE_WINDOW_SHUTTERS = registerBlock(
            "spruce_window_shutters",
            BlockBuilder.halfDoor()
                    .strength(2.0f)
                    .resistance(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .allowUnsupported()
                    .build());

    public static final Block WHITE_WOOD_WINDOW_SHUTTERS = registerBlock(
            "white_wood_window_shutters",
            BlockBuilder.halfDoor()
                    .strength(2.0f)
                    .resistance(5.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .locked(false)
                    .allowUnsupported(true)
                    .build());

    // Pane Blocks
    public static final Block DORNE_CARVED_STONE_WINDOW = registerBlock(
            "dorne_carved_stone_window",
            BlockBuilder.pane()
                    .strength(1.0f)
                    .resistance(3.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .nonOpaque()
                    .legacyModel(true)
                    .unconnect(false)
                    .build());

    public static final Block DORNE_CARVED_WOODEN_WINDOW = registerBlock(
            "dorne_carved_wooden_window",
            BlockBuilder.pane()
                    .strength(1.0f)
                    .resistance(3.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .nonOpaque()
                    .legacyModel(true)
                    .unconnect(false)
                    .build());

    public static final Block IRON_BARS = registerBlock(
            "iron_bars",
            BlockBuilder.pane()
                    .strength(5.0f)
                    .resistance(10.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.METAL)
                    .nonOpaque()
                    .barsModel(true)
                    .unconnect(false)
                    .build());

    public static final Block IRON_CROSSBAR = registerBlock(
            "iron_crossbar",
            BlockBuilder.pane()
                    .strength(5.0f)
                    .resistance(10.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.METAL)
                    .nonOpaque()
                    .barsModel(true)
                    .unconnect(false)
                    .build());

    public static final Block OXIDIZED_IRON_BARS = registerBlock(
            "oxidized_iron_bars",
            BlockBuilder.pane()
                    .strength(5.0f)
                    .resistance(10.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.METAL)
                    .nonOpaque()
                    .barsModel(true)
                    .unconnect(false)
                    .build());

    public static final Block OXIDIZED_IRON_CROSSBAR = registerBlock(
            "oxidized_iron_crossbar",
            BlockBuilder.pane()
                    .strength(5.0f)
                    .resistance(10.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.METAL)
                    .nonOpaque()
                    .barsModel(true)
                    .unconnect(false)
                    .build());

    public static final Block VERTICAL_NET = registerBlock(
            "vertical_net",
            BlockBuilder.pane()
                    .strength(1.0f)
                    .resistance(3.0f)
                    .sounds(BlockSoundGroup.GLASS)
                    .nonOpaque()
                    .legacyModel(true)
                    .unconnect(false)
                    .build());

    // Timber Blocks
    public static final Block TIMBER_NORTHERN_BLUE_BRESSUMMER = registerBlock(
            "timber_northern_blue_bressummer",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .connectState(true)
                    .toggleOnUse(true)
                    .states(5)
                    .symmetrical(false)
                    .build());

    public static final Block TIMBER_NORTHERN_GREEN_LEFTHATCH = registerBlock(
            "timber_northern_green_lefthatch",
            BlockBuilder.solid()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .connectState(true)
                    .toggleOnUse(true)
                    .states(5)
                    .symmetrical(true)
                    .build());

    // Log blocks
    public static final Block ARCHERY_TARGET = registerBlock(
            "archery_target",
            BlockBuilder.log()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block CLOSED_BARREL = registerBlock(
            "closed_barrel",
            BlockBuilder.log()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block FIREWOOD = registerBlock(
            "firewood",
            BlockBuilder.log()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block JUNGLE_LOG_CHAIN = registerBlock(
            "jungle_log_chain",
            BlockBuilder.log()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block JUNGLE_LOG_ROPE = registerBlock(
            "jungle_log_rope",
            BlockBuilder.log()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block MARBLE_PILLAR_VERTICAL_CTM = registerBlock(
            "marble_pillar_vertical_ctm",
            BlockBuilder.log()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block MARBLE_PILLAR = registerBlock(
            "marble_pillar",
            BlockBuilder.log()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block MOSSY_BIRCH_LOG = registerBlock(
            "mossy_birch_log",
            BlockBuilder.log()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block MOSSY_JUNGLE_LOG = registerBlock(
            "mossy_jungle_log",
            BlockBuilder.log()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block MOSSY_OAK_LOG = registerBlock(
            "mossy_oak_log",
            BlockBuilder.log()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block MOSSY_SPRUCE_LOG = registerBlock(
            "mossy_spruce_log",
            BlockBuilder.log()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block OAK_LOG_CHAIN = registerBlock(
            "oak_log_chain",
            BlockBuilder.log()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block OAK_LOG_ROPE = registerBlock(
            "oak_log_rope",
            BlockBuilder.log()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block PALM_TREE_LOG = registerBlock(
            "palm_tree_log",
            BlockBuilder.log()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block SANDSTONE_PILLAR = registerBlock(
            "sandstone_pillar",
            BlockBuilder.log()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block SPRUCE_LOG_CHAIN = registerBlock(
            "spruce_log_chain",
            BlockBuilder.log()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block SPRUCE_LOG_ROPE = registerBlock(
            "spruce_log_rope",
            BlockBuilder.log()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block STACKED_BONES = registerBlock(
            "stacked_bones",
            BlockBuilder.log()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    public static final Block WEIRWOOD_FACE_0 = registerBlock(
            "weirwood_face_0",
            BlockBuilder.log()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block WEIRWOOD_FACE_1 = registerBlock(
            "weirwood_face_1",
            BlockBuilder.log()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block WEIRWOOD_FACE_2 = registerBlock(
            "weirwood_face_2",
            BlockBuilder.log()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block WEIRWOOD_FACE_3 = registerBlock(
            "weirwood_face_3",
            BlockBuilder.log()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block WEIRWOOD_FACE_4 = registerBlock(
            "weirwood_face_4",
            BlockBuilder.log()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block WEIRWOOD_FACE_5 = registerBlock(
            "weirwood_face_5",
            BlockBuilder.log()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block WEIRWOOD_FACE_6 = registerBlock(
            "weirwood_face_6",
            BlockBuilder.log()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block WEIRWOOD_FACE_7 = registerBlock(
            "weirwood_face_7",
            BlockBuilder.log()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block WEIRWOOD_FACE_8 = registerBlock(
            "weirwood_face_8",
            BlockBuilder.log()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block WEIRWOOD_SCARS = registerBlock(
            "weirwood_scars",
            BlockBuilder.log()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    // Rail blocks
    public static final Block FANCY_BLUE_CARPET = registerBlock(
            "fancy_blue_carpet",
            BlockBuilder.rail()
                    .strength(0.1f)
                    .sounds(BlockSoundGroup.WOOL)
                    .allowUnsupported(true)
                    .nonOpaque()
                    .build());

    public static final Block FANCY_RED_CARPET = registerBlock(
            "fancy_red_carpet",
            BlockBuilder.rail()
                    .strength(0.1f)
                    .sounds(BlockSoundGroup.WOOL)
                    .allowUnsupported(true)
                    .nonOpaque()
                    .build());

    public static final Block HORIZONTAL_CHAIN = registerBlock(
            "horizontal_chain",
            BlockBuilder.rail()
                    .strength(1.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.METAL)
                    .allowUnsupported(true)
                    .nonOpaque()
                    .build());

    public static final Block HORIZONTAL_NET = registerBlock(
            "horizontal_net",
            BlockBuilder.rail()
                    .strength(0.5f)
                    .sounds(BlockSoundGroup.WOOL)
                    .allowUnsupported(true)
                    .nonOpaque()
                    .build());

    public static final Block HORIZONTAL_ROPE = registerBlock(
            "horizontal_rope",
            BlockBuilder.rail()
                    .strength(0.5f)
                    .sounds(BlockSoundGroup.WOOL)
                    .allowUnsupported(true)
                    .nonOpaque()
                    .build());

    public static final Block PACKED_SNOW = registerBlock(
            "packed_snow",
            BlockBuilder.rail()
                    .strength(0.2f)
                    .sounds(BlockSoundGroup.SNOW)
                    .allowUnsupported(true)
                    .nonOpaque()
                    .build());

    // Slab blocks
    public static final Block APPLE_BASKET_SLAB = registerBlock(
            "apple_basket_slab",
            BlockBuilder.slab()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block APRICOT_BASKET_SLAB = registerBlock(
            "apricot_basket_slab",
            BlockBuilder.slab()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block CLOSED_BASKET_SLAB = registerBlock(
            "closed_basket_slab",
            BlockBuilder.slab()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block BERRY_BASKET_SLAB = registerBlock(
            "berry_basket_slab",
            BlockBuilder.slab()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block CARROT_BASKET_SLAB = registerBlock(
            "carrot_basket_slab",
            BlockBuilder.slab()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block CUT_GRAIN_FLOUR_SACK = registerBlock(
            "cut_grain_flour_sack",
            BlockBuilder.slab()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.GRAVEL)
                    .build());

    public static final Block DATE_BASKET_SLAB = registerBlock(
            "date_basket_slab",
            BlockBuilder.slab()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block FIREWOOD_SLAB = registerBlock(
            "firewood_slab",
            BlockBuilder.slab()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block FISH_BASKET_SLAB = registerBlock(
            "fish_basket_slab",
            BlockBuilder.slab()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block GRAIN_BASKET_SLAB = registerBlock(
            "grain_basket_slab",
            BlockBuilder.slab()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block GRAIN_FLOUR_SACK = registerBlock(
            "grain_flour_sack",
            BlockBuilder.slab()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.GRAVEL)
                    .build());

    public static final Block HOP_BASKET_SLAB = registerBlock(
            "hop_basket_slab",
            BlockBuilder.slab()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    // Branch Blocks
    public static final Block OAK_BRANCH = registerBlock(
            "oak_branch",
            BlockBuilder.branch()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final Block BIRCH_BRANCH = registerBlock(
            "birch_branch",
            BlockBuilder.branch()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    // Chair Blocks
    public static final Block OAK_CHAIR = registerBlock(
            "oak_chair",
            BlockBuilder.chair()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .woodType("oak")
                    .build());

    // Arrow Slit Blocks
    public static final Block ARBOR_BRICK_ARROW_SLIT = registerBlock(
            "arbor_brick_arrow_slit",
            BlockBuilder.arrowSlit()
                    .strength(2.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .build());

    // Fan Blocks
    public static final Block WALL_CORAL_TUBE_FAN = registerBlockWithoutBlockItem(
            "wall_coral_tube_fan",
            BlockBuilder.wallFan()
                    .strength(1.0f)
                    .sounds(BlockSoundGroup.STONE)
                    .nonOpaque()
                    .noCollision()
                    .allowUnsupported(true)
                    .build());

    public static final Block CORAL_TUBE_FAN = registerBlock(
            "coral_tube_fan",
            BlockBuilder.fan()
                    .strength(1.0f)
                    .sounds(BlockSoundGroup.STONE)
                    .wallBlock(WALL_CORAL_TUBE_FAN)
                    .allowUnsupported(true)
                    .nonOpaque()
                    .noCollision()
                    .build());

    public static final Block WALL_CORAL_BRAIN_FAN = registerBlockWithoutBlockItem(
            "wall_coral_brain_fan",
            BlockBuilder.wallFan()
                    .strength(1.0f)
                    .sounds(BlockSoundGroup.STONE)
                    .nonOpaque()
                    .noCollision()
                    .allowUnsupported(true)
                    .build());

    public static final Block CORAL_BRAIN_FAN = registerBlock(
            "coral_brain_fan",
            BlockBuilder.fan()
                    .strength(1.0f)
                    .sounds(BlockSoundGroup.STONE)
                    .wallBlock(WALL_CORAL_BRAIN_FAN)
                    .allowUnsupported(true)
                    .nonOpaque()
                    .noCollision()
                    .build());

    public static final Block WALL_CORAL_BUBBLE_FAN = registerBlockWithoutBlockItem(
            "wall_coral_bubble_fan",
            BlockBuilder.wallFan()
                    .strength(1.0f)
                    .sounds(BlockSoundGroup.STONE)
                    .nonOpaque()
                    .noCollision()
                    .allowUnsupported(true)
                    .build());

    public static final Block CORAL_BUBBLE_FAN = registerBlock(
            "coral_bubble_fan",
            BlockBuilder.fan()
                    .strength(1.0f)
                    .sounds(BlockSoundGroup.STONE)
                    .wallBlock(WALL_CORAL_BUBBLE_FAN)
                    .allowUnsupported(true)
                    .nonOpaque()
                    .noCollision()
                    .build());

    public static final Block WALL_CORAL_FIRE_FAN = registerBlockWithoutBlockItem(
            "wall_coral_fire_fan",
            BlockBuilder.wallFan()
                    .strength(1.0f)
                    .sounds(BlockSoundGroup.STONE)
                    .nonOpaque()
                    .noCollision()
                    .allowUnsupported(true)
                    .build());

    public static final Block CORAL_FIRE_FAN = registerBlock(
            "coral_fire_fan",
            BlockBuilder.fan()
                    .strength(1.0f)
                    .sounds(BlockSoundGroup.STONE)
                    .wallBlock(WALL_CORAL_FIRE_FAN)
                    .allowUnsupported(true)
                    .nonOpaque()
                    .noCollision()
                    .build());

    public static final Block WALL_CORAL_HORN_FAN = registerBlockWithoutBlockItem(
            "wall_coral_horn_fan",
            BlockBuilder.wallFan()
                    .strength(1.0f)
                    .sounds(BlockSoundGroup.STONE)
                    .nonOpaque()
                    .noCollision()
                    .allowUnsupported(true)
                    .build());

    public static final Block CORAL_HORN_FAN = registerBlock(
            "coral_horn_fan",
            BlockBuilder.fan()
                    .strength(1.0f)
                    .sounds(BlockSoundGroup.STONE)
                    .wallBlock(WALL_CORAL_HORN_FAN)
                    .allowUnsupported(true)
                    .nonOpaque()
                    .noCollision()
                    .build());

    // Web Blocks
    public static final Block BEES = registerBlock(
            "bees",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .build());

    public static final Block ALYSSAS_TEARS_MIST_ONE = registerBlock(
            "alyssas_tears_mist_one",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .build());

    public static final Block ALYSSAS_TEARS_MIST_TWO = registerBlock(
            "alyssas_tears_mist_two",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .connectState(true)
                    .build());

    public static final Block ALYSSAS_TEARS_MIST_THREE = registerBlock(
            "alyssas_tears_mist_three",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .connectState(true)
                    .build());

    public static final Block ALYSSAS_TEARS_MIST_FOUR = registerBlock(
            "alyssas_tears_mist_four",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .connectState(true)
                    .build());

    public static final Block BLACK_BRICICLE = registerBlock(
            "black_bricicle",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.STONE)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .build());

    public static final Block BUSHEL_OF_HERBS = registerBlock(
            "bushel_of_herbs",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .build());

    public static final Block BUSHEL_OF_SOURLEAF = registerBlock(
            "bushel_of_sourleaf",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .build());

    public static final Block BUTTERFLY_BLUE = registerBlock(
            "butterfly_blue",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .build());

    public static final Block BUTTERFLY_ORANGE = registerBlock(
            "butterfly_orange",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .build());

    public static final Block BUTTERFLY_RED = registerBlock(
            "butterfly_red",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .build());

    public static final Block BUTTERFLY_WHITE = registerBlock(
            "butterfly_white",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .build());

    public static final Block BUTTERFLY_YELLOW = registerBlock(
            "butterfly_yellow",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .build());

    public static final Block CATTAILS = registerBlock(
            "cattails",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .layerSensitive()
                    .build());

    public static final Block CHAIN_BLOCK_HARNESS = registerBlock(
            "chain_block_harness",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .build());

    public static final Block CHILI_RISTRA = registerBlock(
            "chili_ristra",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .build());

    public static final Block COBWEB = registerBlock(
            "cobweb",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .toggleOnUse()
                    .build());

    public static final Block DEAD_FISH = registerBlock(
            "dead_fish",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .build());

    public static final Block DEAD_FOWL = registerBlock(
            "dead_fowl",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .build());

    public static final Block DEAD_FROG = registerBlock(
            "dead_frog",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .build());

    public static final Block DEAD_HARE = registerBlock(
            "dead_hare",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .build());

    public static final Block DEAD_JUNGLE_TALL_GRASS = registerBlock(
            "dead_jungle_tall_grass",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .layerSensitive()
                    .build());

    public static final Block DEAD_RAT = registerBlock(
            "dead_rat",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .build());

    public static final Block DEAD_SAVANNA_TALL_GRASS = registerBlock(
            "dead_savanna_tall_grass",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .layerSensitive()
                    .build());

    public static final Block DRAGONFLY = registerBlock(
            "dragonfly",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .build());

    public static final Block FLIES = registerBlock(
            "flies",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .build());

    public static final Block GARLIC_STRAND = registerBlock(
            "garlic_strand",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .build());

    public static final Block ICICLE = registerBlock(
            "icicle",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .build());

    public static final Block IRON_THRONE_RANDOM_BLADES = registerBlock(
            "iron_throne_random_blades",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .build());

    public static final Block JUNGLE_TALL_FERN = registerBlock(
            "jungle_tall_fern",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .layerSensitive()
                    .build());

    public static final Block JUNGLE_TALL_GRASS = registerBlock(
            "jungle_tall_grass",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .layerSensitive()
                    .build());

    public static final Block ROPE_BLOCK_HARNESS = registerBlock(
            "rope_block_harness",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .build());

    public static final Block SAUSAGES_LEG_OF_HAM = registerBlock(
            "sausages_leg_of_ham",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .build());

    public static final Block SAVANNA_TALL_GRASS = registerBlock(
            "savanna_tall_grass",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .layerSensitive()
                    .build());

    public static final Block SMOKE = registerBlock(
            "smoke",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .toggleOnUse()
                    .build());

    public static final Block VERTICAL_CHAIN = registerBlock(
            "vertical_chain",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .build());

    public static final Block VERTICAL_ROPE = registerBlock(
            "vertical_rope",
            BlockBuilder.web()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()
                    .noCollision()
                    .noInWeb()
                    .build());

    // Plant Blocks
    public static final Block BLUE_BELLS = registerBlock(
            "blue_bells",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .toggleOnUse()
                    .build());

    public static final Block BLUE_CHICORY = registerBlock(
            "blue_chicory",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .toggleOnUse()
                    .build());

    public static final Block BLUE_FORGETMENOTS = registerBlock(
            "blue_forgetmenots",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .toggleOnUse()
                    .build());

    public static final Block BLUE_FLAX = registerBlock(
            "blue_flax",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.WOOD)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .toggleOnUse()
                    .build());

    public static final Block BLUE_HYACINTH = registerBlock(
            "blue_hyacinth",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .toggleOnUse()
                    .build());

    public static final Block BLUE_ORCHID = registerBlock(
            "blue_orchid",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .toggleOnUse()
                    .build());

    public static final Block BLUE_SWAMP_BELLS = registerBlock(
            "blue_swamp_bells",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .toggleOnUse()
                    .build());

    public static final Block BRACKEN = registerBlock(
            "bracken",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .toggleOnUse()
                    .build());

    public static final Block BROWN_MUSHROOM_1 = registerBlock(
            "brown_mushroom_1",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block BROWN_MUSHROOM_2 = registerBlock(
            "brown_mushroom_2",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block BROWN_MUSHROOM_3 = registerBlock(
            "brown_mushroom_3",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block BROWN_MUSHROOM_4 = registerBlock(
            "brown_mushroom_4",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block BROWN_MUSHROOM_5 = registerBlock(
            "brown_mushroom_5",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block BROWN_MUSHROOM_6 = registerBlock(
            "brown_mushroom_6",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block BROWN_MUSHROOM_7 = registerBlock(
            "brown_mushroom_7",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block BROWN_MUSHROOM_8 = registerBlock(
            "brown_mushroom_8",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block BROWN_MUSHROOM_9 = registerBlock(
            "brown_mushroom_9",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block BROWN_MUSHROOM_10 = registerBlock(
            "brown_mushroom_10",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block BROWN_MUSHROOM_11 = registerBlock(
            "brown_mushroom_11",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block BROWN_MUSHROOM_12 = registerBlock(
            "brown_mushroom_12",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block BROWN_MUSHROOM_13 = registerBlock(
            "brown_mushroom_13",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block CORAL_BRAIN_WEB = registerBlock(
            "coral_brain_web",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.STONE)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block CORAL_BUBBLE_WEB = registerBlock(
            "coral_bubble_web",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.STONE)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block CORAL_FIRE_WEB = registerBlock(
            "coral_fire_web",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.STONE)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block CORAL_HORN_WEB = registerBlock(
            "coral_horn_web",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.STONE)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block CORAL_TUBE_WEB = registerBlock(
            "coral_tube_web",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.STONE)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block COW_PARSELY = registerBlock(
            "cow_parsely",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block CRANBERRY_BUSH = registerBlock(
            "cranberry_bush",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block DEAD_BRACKEN = registerBlock(
            "dead_bracken",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block DEAD_BUSH = registerBlock(
            "dead_bush",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block DEAD_SCRUB_GRASS = registerBlock(
            "dead_scrub_grass",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block DOCK_LEAF = registerBlock(
            "dock_leaf",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block FIREWEED = registerBlock(
            "fireweed",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block GRASS = registerBlock(
            "grass",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block GREEN_LEAFY_HERB = registerBlock(
            "green_leafy_herb",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block GREEN_SCRUB_GRASS = registerBlock(
            "green_scrub_grass",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block GREEN_SPINY_HERB = registerBlock(
            "green_spiny_herb",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block HEATHER = registerBlock(
            "heather",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block KELP = registerBlock(
            "kelp",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block LADY_FERN = registerBlock(
            "lady_fern",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block MAGENTA_ROSES = registerBlock(
            "magenta_roses",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block MEADOW_FESCUE = registerBlock(
            "meadow_fescue",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block NETTLE = registerBlock(
            "nettle",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block ORANGE_BELLS = registerBlock(
            "orange_bells",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block ORANGE_BOG_ASPHODEL = registerBlock(
            "orange_bog_asphodel",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block ORANGE_MARIGOLDS = registerBlock(
            "orange_marigolds",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block ORANGE_SUN_STAR = registerBlock(
            "orange_sun_star",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block ORANGE_TROLLIUS = registerBlock(
            "orange_trollius",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block PINK_ALLIUM = registerBlock(
            "pink_allium",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block PINK_PRIMROSE = registerBlock(
            "pink_primrose",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block PINK_ROSES = registerBlock(
            "pink_roses",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block PINK_SWEET_PEAS = registerBlock(
            "pink_sweet_peas",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block PINK_THISTLE = registerBlock(
            "pink_thistle",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block PINK_TULIPS = registerBlock(
            "pink_tulips",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block PINK_WILDFLOWERS = registerBlock(
            "pink_wildflowers",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block RED_ASTER = registerBlock(
            "red_aster",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block RED_CARNATIONS = registerBlock(
            "red_carnations",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block RED_CHRYSANTHEMUM = registerBlock(
            "red_chrysanthemum",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block RED_DARK_ROSES = registerBlock(
            "red_dark_roses",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block RED_FERN = registerBlock(
            "red_fern",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block RED_FLOWERING_SPINY_HERB = registerBlock(
            "red_flowering_spiny_herb",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block RED_MUSHROOM_1 = registerBlock(
            "red_mushroom_1",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block RED_MUSHROOM_2 = registerBlock(
            "red_mushroom_2",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block RED_MUSHROOM_3 = registerBlock(
            "red_mushroom_3",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block RED_MUSHROOM_4 = registerBlock(
            "red_mushroom_4",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block RED_MUSHROOM_5 = registerBlock(
            "red_mushroom_5",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block RED_MUSHROOM_6 = registerBlock(
            "red_mushroom_6",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block RED_MUSHROOM_7 = registerBlock(
            "red_mushroom_7",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block RED_MUSHROOM_8 = registerBlock(
            "red_mushroom_8",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block RED_MUSHROOM_9 = registerBlock(
            "red_mushroom_9",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block RED_POPPIES = registerBlock(
            "red_poppies",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block RED_ROSES = registerBlock(
            "red_roses",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block RED_SORREL = registerBlock(
            "red_sorrel",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block RED_SOURLEAF_BUSH = registerBlock(
            "red_sourleaf_bush",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block RED_TULIPS = registerBlock(
            "red_tulips",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block STRAWBERRY_BUSH = registerBlock(
            "strawberry_bush",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block THICK_GRASS = registerBlock(
            "thick_grass",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block UNSHADED_GRASS = registerBlock(
            "unshaded_grass",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block WHITE_CHAMOMILE = registerBlock(
            "white_chamomile",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block WHITE_DAISIES = registerBlock(
            "white_daisies",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block WHITE_LILYOFTHEVALLEY = registerBlock(
            "white_lilyofthevalley",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block WHITE_PEONY = registerBlock(
            "white_peony",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block WHITE_ROSES = registerBlock(
            "white_roses",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block YELLOW_BEDSTRAW = registerBlock(
            "yellow_bedstraw",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block YELLOW_BELLS = registerBlock(
            "yellow_bells",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block YELLOW_BUTTERCUPS = registerBlock(
            "yellow_buttercups",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block YELLOW_DAFFODILS = registerBlock(
            "yellow_daffodils",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block YELLOW_DAISIES = registerBlock(
            "yellow_daisies",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block YELLOW_DANDELIONS = registerBlock(
            "yellow_dandelions",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block YELLOW_HELLEBORE = registerBlock(
            "yellow_hellebore",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block YELLOW_LUPINE = registerBlock(
            "yellow_lupine",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block YELLOW_ROSES = registerBlock(
            "yellow_roses",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block YELLOW_RUDBECKIA = registerBlock(
            "yellow_rudbeckia",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block YELLOW_SUNFLOWER = registerBlock(
            "yellow_sunflower",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block YELLOW_TANSY = registerBlock(
            "yellow_tansy",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    public static final Block YELLOW_WILDFLOWERS = registerBlock(
            "yellow_wildflowers",
            BlockBuilder.plant()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .layerSensitive()
                    .build());

    // Crop Blocks
    public static final Block CROP_CARROTS = registerBlock(
            "crop_carrots",
            BlockBuilder.crop()
                    .strength(0.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.GRASS)
                    .toggleOnUse()
                    .stateValues(List.of("age0", "age1", "age2", "age3"))
                    .build());

    public static final Block CANDLE_ALTAR = registerBlock(
            "candle_altar",
            BlockBuilder.crop()
                    .strength(0.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.METAL)
                    .toggleOnUse()
                    .stateValues(List.of("lit", "unlit"))
                    .build());

    public static final Block CROP_PEAS = registerBlock(
            "crop_peas",
            BlockBuilder.crop()
                    .strength(0.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.GRASS)
                    .toggleOnUse()
                    .layerSensitive()
                    .stateValues(List.of("age0", "age1", "age2"))
                    .build());

    public static final Block CROP_TURNIPS = registerBlock(
            "crop_turnips",
            BlockBuilder.crop()
                    .strength(0.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.GRASS)
                    .toggleOnUse()
                    .layerSensitive()
                    .stateValues(List.of("age0", "age1", "age2","age3"))
                    .build());

    public static final Block CROP_WHEAT = registerBlock(
            "crop_wheat",
            BlockBuilder.crop()
                    .strength(0.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.GRASS)
                    .toggleOnUse()
                    .layerSensitive()
                    .stateValues(List.of("age0", "age1", "age2","age3", "age4", "age5", "age6", "age7"))
                    .build());

    public static final Block SEAGRASS = registerBlock(
            "seagrass",
            BlockBuilder.crop()
                    .strength(0.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.GRASS)
                    .toggleOnUse()
                    .layerSensitive()
                    .build());

    // Flowerbed Blocks
    public static final Block CLOVER = registerBlock(
            "clover",
            BlockBuilder.flowerbed()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .noCollision()
                    .build());

    // Bed Blocks
//    public static final Block ITCHY_STRAW_BED = registerBlock(
//            "itchy_straw_bed",
//            BlockBuilder.bed()
//                    .strength(0.2f)
//                    .sounds(BlockSoundGroup.GRASS)
//                    .nonOpaque()
//                    .build());

    /**
     * Initialize all blocks
     */
    public static void registerModBlocks() {
        WesterosBlocks.LOGGER.info("Registering Mod Blocks for " + WesterosBlocks.MOD_ID);

        WesterosCreativeModeTabs.addToTab("westeros_grasses_shrubs_tab",
                ModBlocks.BRACKEN,
                ModBlocks.BROWN_MUSHROOM_1,
                ModBlocks.BROWN_MUSHROOM_2,
                ModBlocks.BROWN_MUSHROOM_3,
                ModBlocks.BROWN_MUSHROOM_4,
                ModBlocks.BROWN_MUSHROOM_5,
                ModBlocks.BROWN_MUSHROOM_6,
                ModBlocks.BROWN_MUSHROOM_7,
                ModBlocks.BROWN_MUSHROOM_8,
                ModBlocks.BROWN_MUSHROOM_9,
                ModBlocks.BROWN_MUSHROOM_10,
                ModBlocks.BROWN_MUSHROOM_11,
                ModBlocks.BROWN_MUSHROOM_12,
                ModBlocks.BROWN_MUSHROOM_13,
                ModBlocks.COW_PARSELY,
                ModBlocks.DEAD_BRACKEN,
                ModBlocks.DEAD_BUSH,
                ModBlocks.DEAD_SCRUB_GRASS,
                ModBlocks.FIREWEED,
                ModBlocks.GRASS,
                ModBlocks.GREEN_LEAFY_HERB,
                ModBlocks.GREEN_SCRUB_GRASS,
                ModBlocks.GREEN_SPINY_HERB,
                ModBlocks.HEATHER,
                ModBlocks.LADY_FERN,
                ModBlocks.MEADOW_FESCUE,
                ModBlocks.NETTLE,
                ModBlocks.RED_FERN,
                ModBlocks.RED_MUSHROOM_1,
                ModBlocks.RED_MUSHROOM_2,
                ModBlocks.RED_MUSHROOM_3,
                ModBlocks.RED_MUSHROOM_4,
                ModBlocks.RED_MUSHROOM_5,
                ModBlocks.RED_MUSHROOM_6,
                ModBlocks.RED_MUSHROOM_7,
                ModBlocks.RED_MUSHROOM_8,
                ModBlocks.RED_MUSHROOM_9,
                ModBlocks.THICK_GRASS,
                ModBlocks.UNSHADED_GRASS,
                ModBlocks.CATTAILS,
                ModBlocks.DEAD_JUNGLE_TALL_GRASS,
                ModBlocks.DEAD_SAVANNA_TALL_GRASS
        );

        WesterosCreativeModeTabs.addToTab("westeros_water_air_tab",
                ModBlocks.CORAL_BRAIN_WEB,
                ModBlocks.CORAL_BUBBLE_WEB,
                ModBlocks.CORAL_FIRE_WEB,
                ModBlocks.CORAL_HORN_WEB,
                ModBlocks.CORAL_TUBE_WEB,
                ModBlocks.KELP,
                ModBlocks.CORAL_TUBE_FAN,
                ModBlocks.CORAL_BRAIN_FAN,
                ModBlocks.CORAL_BUBBLE_FAN,
                ModBlocks.CORAL_FIRE_FAN,
                ModBlocks.CORAL_HORN_FAN,
                ModBlocks.PACKED_SNOW,
                ModBlocks.ALYSSAS_TEARS_MIST_ONE,
                ModBlocks.ALYSSAS_TEARS_MIST_TWO,
                ModBlocks.ALYSSAS_TEARS_MIST_THREE,
                ModBlocks.ALYSSAS_TEARS_MIST_FOUR,
                ModBlocks.SEAGRASS
        );

        WesterosCreativeModeTabs.addToTab("westeros_crops_herbs_tab",
                ModBlocks.CRANBERRY_BUSH,
                ModBlocks.DOCK_LEAF,
                ModBlocks.RED_SOURLEAF_BUSH,
                ModBlocks.STRAWBERRY_BUSH,
                ModBlocks.BUSHEL_OF_SOURLEAF,
                ModBlocks.CROP_CARROTS,
                ModBlocks.CROP_PEAS,
                ModBlocks.CROP_TURNIPS,
                ModBlocks.CROP_WHEAT
        );

        WesterosCreativeModeTabs.addToTab("westeros_flowers_tab",
                ModBlocks.BLUE_BELLS,
                ModBlocks.BLUE_CHICORY,
                ModBlocks.BLUE_FORGETMENOTS,
                ModBlocks.BLUE_FLAX,
                ModBlocks.BLUE_HYACINTH,
                ModBlocks.BLUE_ORCHID,
                ModBlocks.BLUE_SWAMP_BELLS,
                ModBlocks.MAGENTA_ROSES,
                ModBlocks.ORANGE_BELLS,
                ModBlocks.ORANGE_BOG_ASPHODEL,
                ModBlocks.ORANGE_MARIGOLDS,
                ModBlocks.ORANGE_SUN_STAR,
                ModBlocks.ORANGE_TROLLIUS,
                ModBlocks.PINK_ALLIUM,
                ModBlocks.PINK_PRIMROSE,
                ModBlocks.PINK_ROSES,
                ModBlocks.PINK_SWEET_PEAS,
                ModBlocks.PINK_THISTLE,
                ModBlocks.PINK_TULIPS,
                ModBlocks.PINK_WILDFLOWERS,
                ModBlocks.RED_ASTER,
                ModBlocks.RED_CARNATIONS,
                ModBlocks.RED_CHRYSANTHEMUM,
                ModBlocks.RED_DARK_ROSES,
                ModBlocks.RED_FLOWERING_SPINY_HERB,
                ModBlocks.RED_POPPIES,
                ModBlocks.RED_ROSES,
                ModBlocks.RED_SORREL,
                ModBlocks.RED_TULIPS,
                ModBlocks.WHITE_CHAMOMILE,
                ModBlocks.WHITE_DAISIES,
                ModBlocks.WHITE_LILYOFTHEVALLEY,
                ModBlocks.WHITE_PEONY,
                ModBlocks.WHITE_ROSES,
                ModBlocks.YELLOW_BEDSTRAW,
                ModBlocks.YELLOW_BELLS,
                ModBlocks.YELLOW_BUTTERCUPS,
                ModBlocks.YELLOW_DAFFODILS,
                ModBlocks.YELLOW_DAISIES,
                ModBlocks.YELLOW_DANDELIONS,
                ModBlocks.YELLOW_HELLEBORE,
                ModBlocks.YELLOW_LUPINE,
                ModBlocks.YELLOW_ROSES,
                ModBlocks.YELLOW_RUDBECKIA,
                ModBlocks.YELLOW_SUNFLOWER,
                ModBlocks.YELLOW_TANSY,
                ModBlocks.YELLOW_WILDFLOWERS,
                ModBlocks.CLOVER);

        WesterosCreativeModeTabs.addToTab("westeros_logs_tab",
                ModBlocks.SIX_SIDED_BIRCH,
                ModBlocks.SIX_SIDED_JUNGLE,
                ModBlocks.SIX_SIDED_OAK,
                ModBlocks.SIX_SIDED_SPRUCE,
                ModBlocks.WEIRWOOD_FACE_0,
                ModBlocks.WEIRWOOD_FACE_1,
                ModBlocks.WEIRWOOD_FACE_2,
                ModBlocks.WEIRWOOD_FACE_3,
                ModBlocks.WEIRWOOD_FACE_4,
                ModBlocks.WEIRWOOD_FACE_5,
                ModBlocks.WEIRWOOD_FACE_6,
                ModBlocks.WEIRWOOD_FACE_7,
                ModBlocks.WEIRWOOD_FACE_8,
                ModBlocks.WEIRWOOD_SCARS,
                ModBlocks.OAK_BRANCH,
                ModBlocks.BIRCH_BRANCH,
                ModBlocks.JUNGLE_LOG_CHAIN,
                ModBlocks.JUNGLE_LOG_ROPE,
                ModBlocks.MOSSY_BIRCH_LOG,
                ModBlocks.MOSSY_JUNGLE_LOG,
                ModBlocks.MOSSY_OAK_LOG,
                ModBlocks.MOSSY_SPRUCE_LOG,
                ModBlocks.OAK_LOG_CHAIN,
                ModBlocks.OAK_LOG_ROPE,
                ModBlocks.PALM_TREE_LOG,
                ModBlocks.SPRUCE_LOG_CHAIN,
                ModBlocks.SPRUCE_LOG_ROPE
        );

        WesterosCreativeModeTabs.addToTab("westeros_half_ashlar_tab",
                ModBlocks.SIX_SIDED_STONE_SLAB
        );

        WesterosCreativeModeTabs.addToTab("westeros_food_blocks_tab",
                ModBlocks.APPLE_BASKET,
                ModBlocks.APPLE_CRATE,
                ModBlocks.APRICOT_BASKET,
                ModBlocks.BERRY_BASKET,
                ModBlocks.BERRY_CRATE,
                ModBlocks.CARROT_BASKET,
                ModBlocks.CARROT_CRATE,
                ModBlocks.DATE_BASKET,
                ModBlocks.DATES,
                ModBlocks.FISH_BARREL,
                ModBlocks.FISH_BASKET,
                ModBlocks.FISH_TRAP,
                ModBlocks.GRAIN_BASKET,
                ModBlocks.GRAIN_CRATE,
                ModBlocks.HOP_BASKET,
                ModBlocks.HOP_CRATE,
                ModBlocks.LAVENDER_BASKET,
                ModBlocks.LAVENDER_CRATE,
                ModBlocks.LEMON_BASKET,
                ModBlocks.LIME_BASKET,
                ModBlocks.APPLE_BASKET_SLAB,
                ModBlocks.APRICOT_BASKET_SLAB,
                ModBlocks.CLOSED_BASKET_SLAB,
                ModBlocks.BERRY_BASKET_SLAB,
                ModBlocks.CARROT_BASKET_SLAB,
                ModBlocks.CUT_GRAIN_FLOUR_SACK,
                ModBlocks.DATE_BASKET_SLAB,
                ModBlocks.OLIVE_BASKET,
                ModBlocks.ORANGE_BASKET,
                ModBlocks.POMEGRANATE_BASKET,
                ModBlocks.PURPLE_GRAPE_BASKET,
                ModBlocks.PURPLE_GRAPE_CRATE,
                ModBlocks.SALT_CRATE,
                ModBlocks.SOURLEAF_BASKET,
                ModBlocks.SOURLEAF_CRATE,
                ModBlocks.SPIT_ROAST,
                ModBlocks.SQUASH,
                ModBlocks.TURNIP_BASKET,
                ModBlocks.TURNIP_CRATE,
                ModBlocks.WHITE_GRAPE_BASKET,
                ModBlocks.WHITE_GRAPE_CRATE,
                ModBlocks.FISH_BASKET_SLAB,
                ModBlocks.GRAIN_BASKET_SLAB,
                ModBlocks.GRAIN_FLOUR_SACK,
                ModBlocks.HOP_BASKET_SLAB,
                ModBlocks.BUSHEL_OF_HERBS,
                ModBlocks.CHILI_RISTRA,
                ModBlocks.DEAD_FISH,
                ModBlocks.DEAD_FOWL,
                ModBlocks.DEAD_FROG,
                ModBlocks.DEAD_HARE,
                ModBlocks.DEAD_RAT
        );

        WesterosCreativeModeTabs.addToTab("westeros_utility_tab",
                ModBlocks.APPROVAL_UTILITY_BLOCK,
                ModBlocks.DONE_UTILITY_BLOCK,
                ModBlocks.DOMESTIC_UTILITY_BLOCK,
                ModBlocks.HIGH_CLASS_UTILITY_BLOCK,
                ModBlocks.HOUSE_COUNT_UTILITY_BLOCK,
                ModBlocks.INDUSTRY_UTILITY_BLOCK,
                ModBlocks.LOW_CLASS_UTILITY_BLOCK,
                ModBlocks.MIDDLE_CLASS_UTILITY_BLOCK,
                ModBlocks.NOTE_UTILITY_BLOCK,
                ModBlocks.SHOP_UTILITY_BLOCK,
                ModBlocks.SPECIAL_UTILITY_BLOCK,
                ModBlocks.WIP_UTILITY_BLOCK,
                ModBlocks.WORKSHOP_UTILITY_BLOCK,
                ModBlocks.YARD_UTILITY_BLOCK
        );

        WesterosCreativeModeTabs.addToTab("westeros_panelling_carvings_tab",
                ModBlocks.ARBOR_BRICK_ORNATE,
                ModBlocks.BLACK_BRICK_ENGRAVED,
                ModBlocks.BLUEGREEN_CARVED_SANDSTONE,
                ModBlocks.BROWN_GREY_BRICK_ENGRAVED,
                ModBlocks.COARSE_DARK_RED_CARVED_SANDSTONE,
                ModBlocks.COARSE_RED_CARVED_SANDSTONE,
                ModBlocks.COBBLE_KEYSTONE,
                ModBlocks.DARK_GREY_BRICK_ENGRAVED,
                ModBlocks.DESERT_SANDSTONE_ENGRAVED,
                ModBlocks.DRAGON_CARVING,
                ModBlocks.FAITH_CARVED_ARBOR_BRICK,
                ModBlocks.FAITH_CARVED_BLACK_BRICK,
                ModBlocks.FAITH_CARVED_BROWN_GREY_BRICK,
                ModBlocks.FAITH_CARVED_COARSE_RED_BRICK,
                ModBlocks.FAITH_CARVED_DARK_GREY_BRICK,
                ModBlocks.FAITH_CARVED_DUN_BRICK,
                ModBlocks.FAITH_CARVED_GREY_BRICK,
                ModBlocks.FAITH_CARVED_OLDTOWN_BRICK,
                ModBlocks.FAITH_CARVED_PINK_SANDSTONE,
                ModBlocks.FAITH_CARVED_REACH_BRICK,
                ModBlocks.FAITH_CARVED_SMALL_STONE_BRICK,
                ModBlocks.FAITH_CARVED_STONE_BRICK,
                ModBlocks.FAITH_CARVED_STORMLANDS_BRICK,
                ModBlocks.FAITH_CARVED_WESTERLANDS_BRICK,
                ModBlocks.GREEN_GREY_BRICK_ENGRAVED,
                ModBlocks.GREY_BRICK_ENGRAVED,
                ModBlocks.GREY_KEYSTONE,
                ModBlocks.KL_DUN_CARVED_BRICK,
                ModBlocks.LIGHT_GREY_BRICK_ENGRAVED,
                ModBlocks.LIGHT_OLDTOWN_BRICK_ENGRAVED,
                ModBlocks.MONOCHROME_DARK_SANDSTONE_ENGRAVED,
                ModBlocks.MONOCHROME_SANDSTONE_ENGRAVED,
                ModBlocks.NETHER_BRICK_KEYSTONE,
                ModBlocks.NORTHERN_CARVINGS,
                ModBlocks.ORNATE_MARBLE,
                ModBlocks.ORNATE_SANDSTONE,
                ModBlocks.PINK_SANDSTONE_ENGRAVED,
                ModBlocks.REACH_BRICK_ENGRAVED,
                ModBlocks.REACH_OAK_WOOD_PANELLING,
                ModBlocks.REACH_SPRUCE_WOOD_PANELLING,
                ModBlocks.REDORANGE_CARVED_SANDSTONE,
                ModBlocks.SMALL_ORANGE_BRICKS_ORNATE_TOP,
                ModBlocks.SMALL_ORANGE_BRICKS_ORNATE,
                ModBlocks.STORMLANDS_BRICK_ENGRAVED,
                ModBlocks.TERRACOTTA_ENGRAVED,
                ModBlocks.VIVID_DARK_SANDSTONE_ENGRAVED,
                ModBlocks.VIVID_SANDSTONE_ENGRAVED,
                ModBlocks.WHITE_BRICK_ENGRAVED,
                ModBlocks.WINTERFELL_CARVING,
                ModBlocks.SANDSTONE_PILLAR
        );

        WesterosCreativeModeTabs.addToTab("westeros_furniture_tab",
                ModBlocks.BENCH_BUTCHER_KNIVES,
                ModBlocks.BENCH_CARPENTRY_HAMMER_SAW,
                ModBlocks.BENCH_DRAWERS,
                ModBlocks.BENCH_KITCHEN_KNIVES,
                ModBlocks.BENCH_KITCHEN_PANS,
                ModBlocks.BENCH_MASON_HAMMER_MALLET,
                ModBlocks.BOOKSHELF_ABANDONED,
                ModBlocks.BOOKSHELF_LIBRARY,
                ModBlocks.BOOKSHELF_MAESTER,
                ModBlocks.BROKEN_CABINET,
                ModBlocks.CABINET_DRAWER,
                ModBlocks.EMPTY_CABINET,
                ModBlocks.FULL_CABINET,
                ModBlocks.MIRROR_BLOCK,
                ModBlocks.OAK_TABLE,
//                ModBlocks.ITCHY_STRAW_BED,
                ModBlocks.OAK_CHAIR,
                ModBlocks.TABLE_BOOKS,
                ModBlocks.TABLE_DRAWERS,
                ModBlocks.TABLE_WIDGETS
        );

        WesterosCreativeModeTabs.addToTab("westeros_grass_dirt_tab",
                ModBlocks.BONE_DIRT,
                ModBlocks.THICK_GRASS_BLOCK
        );

        WesterosCreativeModeTabs.addToTab("westeros_decor_tab",
                ModBlocks.CAGE,
                ModBlocks.CLOSED_BASKET,
                ModBlocks.CLOSED_CABINET,
                ModBlocks.CRATE,
                ModBlocks.CRATE2,
                ModBlocks.CRATE3,
                ModBlocks.EMPTY_BARREL,
                ModBlocks.IRON_CRATE,
                ModBlocks.LARGE_CLAY_POT_SOLID,
                ModBlocks.OPEN_BASKET,
                ModBlocks.OPEN_CRATE,
                ModBlocks.SILVER_TIN_CRATE,
                ModBlocks.WATER_BARREL,
                ModBlocks.CLOSED_BARREL,
                ModBlocks.FIREWOOD,
                ModBlocks.FIREWOOD_SLAB,
                ModBlocks.COBWEB
        );

        WesterosCreativeModeTabs.addToTab("westeros_cobblestone_tab",
                ModBlocks.FLAGSTONE,
                ModBlocks.SANDY_STONE_SLABS
        );

        WesterosCreativeModeTabs.addToTab("westeros_wood_planks_tab",
                ModBlocks.PARQUET_FLOOR,
                ModBlocks.BIRCH_DOOR,
                ModBlocks.EYRIE_WEIRWOOD_DOOR,
                ModBlocks.GREY_WOOD_DOOR,
                ModBlocks.JUNGLE_DOOR,
                ModBlocks.NORTHERN_WOOD_DOOR,
                ModBlocks.OAK_DOOR,
                ModBlocks.SPRUCE_DOOR,
                ModBlocks.WHITE_WOOD_DOOR,
                ModBlocks.LOCKED_BIRCH_DOOR,
                ModBlocks.LOCKED_DARK_NORTHERN_WOOD_DOOR,
                ModBlocks.LOCKED_GREY_WOOD_DOOR,
                ModBlocks.LOCKED_JUNGLE_DOOR,
                ModBlocks.LOCKED_OAK_DOOR,
                ModBlocks.LOCKED_SPRUCE_DOOR,
                ModBlocks.LOCKED_WHITE_WOOD_DOOR
        );



        WesterosCreativeModeTabs.addToTab("westeros_windows_glass_tab",
                ModBlocks.COLOURED_SEPT_WINDOW,
                ModBlocks.SEPT_CRYSTAL_LARGE,
                ModBlocks.BIRCH_WINDOW_SHUTTERS,
                ModBlocks.DORNE_RED_WINDOW_SHUTTERS,
                ModBlocks.GREEN_LANNISPORT_WINDOW_SHUTTERS,
                ModBlocks.GREY_WOOD_WINDOW_SHUTTERS,
                ModBlocks.JUNGLE_WINDOW_SHUTTERS,
                ModBlocks.NORTHERN_WOOD_WINDOW_SHUTTERS,
                ModBlocks.OAK_WINDOW_SHUTTERS,
                ModBlocks.REACH_BLUE_WINDOW_SHUTTERS,
                ModBlocks.SPRUCE_WINDOW_SHUTTERS,
                ModBlocks.WHITE_WOOD_WINDOW_SHUTTERS,
                ModBlocks.DORNE_CARVED_STONE_WINDOW,
                ModBlocks.DORNE_CARVED_WOODEN_WINDOW
        );


        WesterosCreativeModeTabs.addToTab("westeros_brick_tab",
                ModBlocks.ORANGE_BRICK_ARCH_DOUBLE,
                ModBlocks.ORANGE_BRICK_ARCH_SINGLE,
                ModBlocks.ORANGE_BRICK_DENTIL,
                ModBlocks.ORANGE_BRICK_ROWLOCK,
                ModBlocks.SOUTHERN_BRICK_ARCH_FLAT,
                ModBlocks.SOUTHERN_BRICK_ARCH,
                ModBlocks.SOUTHERN_BRICK_LINTEL
        );

        WesterosCreativeModeTabs.addToTab("westeros_timber_frame_tab",
                ModBlocks.TIMBER_NORTHERN_BLUE_BRESSUMMER,
                ModBlocks.TIMBER_NORTHERN_GREEN_LEFTHATCH
        );

        WesterosCreativeModeTabs.addToTab("westeros_marble_plaster_tab",
                ModBlocks.LANNISPORT_KEYSTONE_ORANGE_PLASTER,
                ModBlocks.LANNISPORT_KEYSTONE_YELLOW_PLASTER,
                ModBlocks.LIGHT_GREY_STONE_WHITE_PLASTER,
                ModBlocks.SMALL_SMOOTH_STONE_BRICK_BLUE_PLASTER,
                ModBlocks.SMALL_SMOOTH_STONE_BRICK_WHITE_PLASTER,
                ModBlocks.SMALL_STONE_BRICK_WHITE_PLASTER,
                ModBlocks.SMALL_WHITE_BRICK_BROWNISH_WHITE_PLASTER,
                ModBlocks.SMALL_WHITE_BRICK_WHITE_PLASTER,
                ModBlocks.UNUSED_BROWN_PLASTER,
                ModBlocks.UNUSED_PURPLE_PLASTER,
                ModBlocks.MARBLE_PILLAR_VERTICAL_CTM,
                ModBlocks.MARBLE_PILLAR
        );



        WesterosCreativeModeTabs.addToTab("westeros_sand_gravel_tab",
                ModBlocks.YELLOW_STAINED_CLAY
        );


        WesterosCreativeModeTabs.addToTab("westeros_tool_blocks_tab",
                ModBlocks.PISTON_TOP
        );


        WesterosCreativeModeTabs.addToTab("westeros_misc_tab",
                ModBlocks.PILED_BONES,
                ModBlocks.STACKED_BONES_SOLID,
                ModBlocks.HARRENHAL_SECRET_DOOR,
                ModBlocks.RED_KEEP_SECRET_DOOR,
                ModBlocks.ARCHERY_TARGET,
                ModBlocks.STACKED_BONES,
                ModBlocks.BEES,
                ModBlocks.BLACK_BRICICLE,
                ModBlocks.BUTTERFLY_BLUE,
                ModBlocks.BUTTERFLY_ORANGE,
                ModBlocks.BUTTERFLY_RED,
                ModBlocks.BUTTERFLY_WHITE,
                ModBlocks.BUTTERFLY_YELLOW
        );


        WesterosCreativeModeTabs.addToTab("westeros_lighting_tab",
                ModBlocks.GLOWING_EMBERS,
                ModBlocks.RED_LANTERN2,
                ModBlocks.TORCH,
                ModBlocks.TORCH_UNLIT,
                ModBlocks.CANDLE,
                ModBlocks.CANDLE_UNLIT,
                ModBlocks.CANDLE_ALTAR
        );

        WesterosCreativeModeTabs.addToTab("westeros_metal_tab",
                ModBlocks.IRON_BARS,
                ModBlocks.IRON_CROSSBAR,
                ModBlocks.OXIDIZED_IRON_BARS,
                ModBlocks.OXIDIZED_IRON_CROSSBAR,
                ModBlocks.HORIZONTAL_CHAIN,
                ModBlocks.CHAIN_BLOCK_HARNESS
        );

        WesterosCreativeModeTabs.addToTab("westeros_cloth_fibers_tab",
                ModBlocks.VERTICAL_NET,
                ModBlocks.FANCY_BLUE_CARPET,
                ModBlocks.FANCY_RED_CARPET,
                ModBlocks.HORIZONTAL_NET,
                ModBlocks.HORIZONTAL_ROPE
        );

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries -> {
            entries.add(ModBlocks.ARBOR_BRICK_ARROW_SLIT);
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
