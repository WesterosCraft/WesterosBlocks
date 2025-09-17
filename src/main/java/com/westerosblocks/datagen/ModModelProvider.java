package com.westerosblocks.datagen;

import com.westerosblocks.block.ModBlocks;

import com.westerosblocks.datagen.custom.BedBlockDatagen;
import com.westerosblocks.datagen.custom.CropBlockDatagen;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;

import static com.westerosblocks.datagen.ModBlockStateModelGenerator.*;

public class ModModelProvider extends FabricModelProvider {

    private final FabricDataOutput output;

    public ModModelProvider(FabricDataOutput output) {
        super(output);
        this.output = output;
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator bsmg) {
        // Solid Blocks
        registerCustomSolidBlock(bsmg, ModBlocks.SIX_SIDED_BIRCH).texture("bark/birch/side").build();
        registerCustomSolidBlock(bsmg, ModBlocks.SIX_SIDED_JUNGLE).texture("bark/jungle/side").build();
        registerCustomSolidBlock(bsmg, ModBlocks.SIX_SIDED_OAK).texture("bark/oak/side").build();
        registerCustomSolidBlock(bsmg, ModBlocks.SIX_SIDED_SPRUCE).texture("bark/spruce/side").build();
        registerCustomSolidBlock(bsmg, ModBlocks.SIX_SIDED_STONE_SLAB).texture("ashlar_half/white/tile")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.APPLE_BASKET)
                .textures("crate_block/basket_bottom", "crate_block/basket_apple",
                        "crate_block/basket_side")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.APPLE_CRATE)
                .randomTexture("crate_block/side_bot1", "crate_block/crate_top_apples",
                        "crate_block/side_bot1")
                .randomTexture("crate_block/side_bot2", "crate_block/crate_top_apples",
                        "crate_block/side_bot2")
                .randomTexture("crate_block/side_bot3", "crate_block/crate_top_apples",
                        "crate_block/side_bot3")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.APPROVAL_UTILITY_BLOCK).texture("utility_block/approved")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.ARBOR_BRICK_ORNATE).texture("ashlar_engraved/arbor/all")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.BENCH_BUTCHER_KNIVES).textures("bench_block/spruce_top",
                "bench_block/crafting_table_top", "bench_block/bench_butcher_knives").build();
        registerCustomSolidBlock(bsmg, ModBlocks.BENCH_CARPENTRY_HAMMER_SAW).textures("bench_block/spruce_top",
                "bench_block/crafting_table_top", "bench_block/bench_carpentry_hammer_saw").build();
        registerCustomSolidBlock(bsmg, ModBlocks.BENCH_DRAWERS)
                .textures("bench_block/spruce_top", "bench_block/crafting_table_top",
                        "bench_block/bench_drawers")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.BENCH_KITCHEN_KNIVES).textures("bench_block/spruce_top",
                "bench_block/crafting_table_top", "bench_block/bench_kitchen_knives").build();
        registerCustomSolidBlock(bsmg, ModBlocks.BENCH_KITCHEN_PANS)
                .textures("bench_block/spruce_top", "bench_block/crafting_table_top",
                        "bench_block/bench_kitchen_pans")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.BENCH_MASON_HAMMER_MALLET).textures("bench_block/spruce_top",
                "bench_block/crafting_table_top", "bench_block/bench_mason_hammer_mallet").build();
        registerCustomSolidBlock(bsmg, ModBlocks.BERRY_BASKET)
                .textures("crate_block/basket_bottom", "crate_block/basket_berry",
                        "crate_block/basket_side")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.BERRY_CRATE)
                .randomTexture("crate_block/side_bot1", "crate_block/crate_top_berry",
                        "crate_block/side_bot1")
                .randomTexture("crate_block/side_bot2", "crate_block/crate_top_berry",
                        "crate_block/side_bot2")
                .randomTexture("crate_block/side_bot3", "crate_block/crate_top_berry",
                        "crate_block/side_bot3")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.APRICOT_BASKET)
                .textures("crate_block/basket_bottom", "crate_block/basket_apricot",
                        "crate_block/basket_side")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.BLACK_BRICK_ENGRAVED).texture("ashlar_engraved/black/all")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.BLUEGREEN_CARVED_SANDSTONE)
                .texture("bluegreen_carved_sandstone/side")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.BONE_DIRT).texture("dirt/bone").build();
        registerCustomSolidBlock(bsmg, ModBlocks.BOOKSHELF_ABANDONED)
                .randomTexture("bench_block/spruce_top", "bench_block/spruce_top",
                        "bookshelf_abandoned/side1")
                .randomTexture("bench_block/spruce_top", "bench_block/spruce_top",
                        "bookshelf_abandoned/side2")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.BOOKSHELF_LIBRARY)
                .randomTexture("bench_block/spruce_top", "bench_block/spruce_top",
                        "bookshelf_library/side1")
                .randomTexture("bench_block/spruce_top", "bench_block/spruce_top",
                        "bookshelf_library/side2")
                .randomTexture("bench_block/spruce_top", "bench_block/spruce_top",
                        "bookshelf_library/side3")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.BOOKSHELF_MAESTER)
                .randomTexture("bench_block/spruce_top", "bench_block/spruce_top",
                        "bookshelf_maester/side1")
                .randomTexture("bench_block/spruce_top", "bench_block/spruce_top",
                        "bookshelf_maester/side2")
                .randomTexture("bench_block/spruce_top", "bench_block/spruce_top",
                        "bookshelf_maester/side3")
                .randomTexture("bench_block/spruce_top", "bench_block/spruce_top",
                        "bookshelf_maester/side4")
                .randomTexture("bench_block/spruce_top", "bench_block/spruce_top",
                        "bookshelf_maester/side5")
                .randomTexture("bench_block/spruce_top", "bench_block/spruce_top",
                        "bookshelf_maester/side6")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.BROKEN_CABINET)
                .textures("cabinet/top_bottom", "cabinet/top_bottom", "bench_block/cabinet_broken")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.BROWN_GREY_BRICK_ENGRAVED)
                .texture("ashlar_engraved/brown_grey/all")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.CABINET_DRAWER)
                .randomTexture("cabinet/top_bottom", "cabinet/top_bottom", "cabinet/drawer/side1")
                .randomTexture("cabinet/top_bottom", "cabinet/top_bottom", "cabinet/drawer/side2")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.CAGE).textures("cage/bottom", "cage/top", "cage/side").build();
        registerCustomSolidBlock(bsmg, ModBlocks.CARROT_BASKET)
                .textures("crate_block/basket_bottom", "crate_block/basket_carrot",
                        "crate_block/basket_side")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.CARROT_CRATE)
                .randomTexture("crate_block/side_bot1", "crate_block/crate_top_carrot",
                        "crate_block/side_bot1")
                .randomTexture("crate_block/side_bot2", "crate_block/crate_top_carrot",
                        "crate_block/side_bot2")
                .randomTexture("crate_block/side_bot3", "crate_block/crate_top_carrot",
                        "crate_block/side_bot3")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.CLOSED_BASKET)
                .textures("crate_block/basket_bottom", "crate_block/basket_top_closed",
                        "crate_block/basket_side")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.CLOSED_CABINET)
                .textures("cabinet/top_bottom", "cabinet/top_bottom", "cabinet/closed/side").build();
        registerCustomSolidBlock(bsmg, ModBlocks.COARSE_DARK_RED_CARVED_SANDSTONE)
                .texture("ashlar_engraved/pale_dark_red/all").build();
        registerCustomSolidBlock(bsmg, ModBlocks.COARSE_RED_CARVED_SANDSTONE)
                .texture("ashlar_engraved/pale_red/all")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.COBBLE_KEYSTONE).textures(
                "cobblestone/grey/keystone/top_bottom",
                "cobblestone/grey/keystone/top_bottom", "cobblestone/grey/keystone/side").build();
        registerCustomSolidBlock(bsmg, ModBlocks.COLOURED_SEPT_WINDOW).texture("glass/sept/all").build();
        registerCustomSolidBlock(bsmg, ModBlocks.CRATE)
                .randomTexture("crate_block/side_bot1", "crate_block/crate_side_crossbar_right",
                        "crate_block/side_bot1")
                .randomTexture("crate_block/side_bot2", "crate_block/crate_side_crossbar_right",
                        "crate_block/side_bot2")
                .randomTexture("crate_block/side_bot3", "crate_block/crate_side_crossbar_right",
                        "crate_block/side_bot3")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.CRATE2)
                .randomTexture("crate_block/side_bot1", "crate_block/crate_side_crossbar_left",
                        "crate_block/side_bot1")
                .randomTexture("crate_block/side_bot2", "crate_block/crate_side_crossbar_left",
                        "crate_block/side_bot2")
                .randomTexture("crate_block/side_bot3", "crate_block/crate_side_crossbar_left",
                        "crate_block/side_bot3")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.CRATE3)
                .randomTexture("crate_block/side_bot1", "crate_block/crate_side_crossbar_crossed",
                        "crate_block/side_bot1")
                .randomTexture("crate_block/side_bot2", "crate_block/crate_side_crossbar_crossed",
                        "crate_block/side_bot2")
                .randomTexture("crate_block/side_bot3", "crate_block/crate_side_crossbar_crossed",
                        "crate_block/side_bot3")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.DARK_GREY_BRICK_ENGRAVED)
                .texture("ashlar_engraved/dark_grey/all")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.DATE_BASKET)
                .textures("crate_block/basket_bottom", "crate_block/basket_dates",
                        "crate_block/basket_side")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.DATES).textures("dates/bottom", "dates/top", "dates/side")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.DESERT_SANDSTONE_ENGRAVED)
                .texture("ashlar_engraved/sandstone/all")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.DOMESTIC_UTILITY_BLOCK).texture("utility_block/domestic")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.DONE_UTILITY_BLOCK).texture("utility_block/done").build();
        registerCustomSolidBlock(bsmg, ModBlocks.DRAGON_CARVING).randomTexture("dragon_carving/side1")
                .randomTexture("dragon_carving/side2").randomTexture("dragon_carving/side3").build();
        registerCustomSolidBlock(bsmg, ModBlocks.EMPTY_BARREL)
                .randomTexture("barrel_closed/barrel_top_closed", "crate_block/barrel_top_empty",
                        "barrel_sides/side0")
                .randomTexture("barrel_closed/barrel_top_closed", "crate_block/barrel_top_empty",
                        "barrel_sides/side1")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.EMPTY_CABINET)
                .textures("cabinet/top_bottom", "cabinet/top_bottom", "cabinet/empty/side").build();
        registerCustomSolidBlock(bsmg, ModBlocks.FAITH_CARVED_ARBOR_BRICK)
                .texture("ashlar_third/arbor/faith_carved")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.FAITH_CARVED_BLACK_BRICK)
                .texture("ashlar_quarter/black/faith_carved")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.FAITH_CARVED_BROWN_GREY_BRICK)
                .texture("ashlar_quarter/brown_grey/faith_carved").build();
        registerCustomSolidBlock(bsmg, ModBlocks.FAITH_CARVED_COARSE_RED_BRICK)
                .texture("ashlar_third/pale_red/faith_carved").build();
        registerCustomSolidBlock(bsmg, ModBlocks.FAITH_CARVED_DARK_GREY_BRICK)
                .texture("ashlar_quarter/dark_grey/faith_carved").build();
        registerCustomSolidBlock(bsmg, ModBlocks.FAITH_CARVED_DUN_BRICK)
                .texture("ashlar_third/dun/faith_carved")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.FAITH_CARVED_GREY_BRICK)
                .texture("ashlar_third/grey/faith_carved")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.FAITH_CARVED_OLDTOWN_BRICK)
                .texture("ashlar_quarter_rounded/light_oldtown/faith_carved").build();
        registerCustomSolidBlock(bsmg, ModBlocks.FAITH_CARVED_PINK_SANDSTONE)
                .texture("ashlar_third/sandy_pink/faith_carved").build();
        registerCustomSolidBlock(bsmg, ModBlocks.FAITH_CARVED_REACH_BRICK)
                .texture("ashlar_quarter_rounded/reach/faith_carved").build();
        registerCustomSolidBlock(bsmg, ModBlocks.FAITH_CARVED_SMALL_STONE_BRICK)
                .texture("ashlar_quarter/green_grey/faith_carved").build();
        registerCustomSolidBlock(bsmg, ModBlocks.FAITH_CARVED_STONE_BRICK)
                .texture("ashlar_half/white/faith_carved")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.FAITH_CARVED_STORMLANDS_BRICK)
                .texture("ashlar_quarter/stormlands/faith_carved").build();
        registerCustomSolidBlock(bsmg, ModBlocks.FAITH_CARVED_WESTERLANDS_BRICK)
                .texture("ashlar_quarter/westerlands/faith_carved").build();
        registerCustomSolidBlock(bsmg, ModBlocks.FISH_BARREL)
                .randomTexture("barrel_closed/barrel_top_closed", "crate_block/barrel_top_fish",
                        "barrel_sides/side0")
                .randomTexture("barrel_closed/barrel_top_closed", "crate_block/barrel_top_fish",
                        "barrel_sides/side1")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.FISH_BASKET)
                .textures("crate_block/basket_bottom", "crate_block/basket_fish",
                        "crate_block/basket_side")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.FISH_TRAP).randomTexture("fish_trap/side1")
                .randomTexture("fish_trap/side2").randomTexture("fish_trap/side3")
                .randomTexture("fish_trap/side4")
                .randomTexture("fish_trap/side5").build();
        registerCustomSolidBlock(bsmg, ModBlocks.FLAGSTONE).texture("stone_block/flagstone").build();
        registerCustomSolidBlock(bsmg, ModBlocks.FULL_CABINET)
                .textures("cabinet/top_bottom", "cabinet/top_bottom", "cabinet/full/side").build();
        registerCustomSolidBlock(bsmg, ModBlocks.GLOWING_EMBERS).texture("lighting/coals_glowing").build();
        registerCustomSolidBlock(bsmg, ModBlocks.GRAIN_BASKET)
                .textures("crate_block/basket_bottom", "crate_block/basket_grain",
                        "crate_block/basket_side")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.GRAIN_CRATE)
                .randomTexture("crate_block/side_bot1", "crate_block/crate_top_grain",
                        "crate_block/side_bot1")
                .randomTexture("crate_block/side_bot2", "crate_block/crate_top_grain",
                        "crate_block/side_bot2")
                .randomTexture("crate_block/side_bot3", "crate_block/crate_top_grain",
                        "crate_block/side_bot3")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.GREEN_GREY_BRICK_ENGRAVED)
                .texture("ashlar_engraved/green_grey/all")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.GREY_BRICK_ENGRAVED).texture("ashlar_engraved/grey/all")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.GREY_KEYSTONE).texture("stone_block/keystone_grey").build();
        registerCustomSolidBlock(bsmg, ModBlocks.HIGH_CLASS_UTILITY_BLOCK).texture("utility_block/highclass")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.HOP_BASKET)
                .textures("crate_block/basket_bottom", "crate_block/basket_hop",
                        "crate_block/basket_side")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.HOP_CRATE).textures("crate_block/crate_side_crossbar_right",
                "crate_block/crate_hops", "crate_block/crate_side_crossbar_left").build();
        registerCustomSolidBlock(bsmg, ModBlocks.HOUSE_COUNT_UTILITY_BLOCK).texture("utility_block/housecount")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.INDUSTRY_UTILITY_BLOCK).texture("utility_block/industry")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.IRON_CRATE)
                .randomTexture("crate_block/side_bot1", "crate_block/crate_top_iron",
                        "crate_block/side_bot1")
                .randomTexture("crate_block/side_bot2", "crate_block/crate_top_iron",
                        "crate_block/side_bot2")
                .randomTexture("crate_block/side_bot3", "crate_block/crate_top_iron",
                        "crate_block/side_bot3")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.KL_DUN_CARVED_BRICK).texture("ashlar_engraved/dun/all")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.LANNISPORT_KEYSTONE_ORANGE_PLASTER)
                .textures("fieldstone/westerlands/all", "fieldstone/westerlands/all",
                        "plaster/smooth/lannisport_orange/all")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.LANNISPORT_KEYSTONE_YELLOW_PLASTER)
                .textures("fieldstone/westerlands/all", "fieldstone/westerlands/all",
                        "plaster/smooth/lannisport_yellow/all")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.LARGE_CLAY_POT_SOLID)
                .textures("wood/oak/all", "clay_pot/solid", "clay_pot/solid").build();
        registerCustomSolidBlock(bsmg, ModBlocks.LAVENDER_BASKET)
                .textures("crate_block/basket_bottom", "crate_block/basket_lavender",
                        "crate_block/basket_side")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.LAVENDER_CRATE)
                .textures("crate_block/crate_side_crossbar_right",
                        "crate_block/crate_lavender", "crate_block/crate_side_crossbar_crossed")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.LEMON_BASKET)
                .textures("crate_block/basket_bottom", "crate_block/basket_lemons",
                        "crate_block/basket_side")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.LIGHT_GREY_BRICK_ENGRAVED)
                .texture("ashlar_engraved/light_grey/all")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.LIGHT_GREY_STONE_WHITE_PLASTER)
                .textures("fieldstone/grey/all", "fieldstone/grey/all", "plaster/smooth/white/all")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.LIGHT_OLDTOWN_BRICK_ENGRAVED)
                .texture("ashlar_engraved/light_oldtown/all").build();
        registerCustomSolidBlock(bsmg, ModBlocks.LIME_BASKET)
                .textures("crate_block/basket_bottom", "crate_block/basket_limes",
                        "crate_block/basket_side")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.LOW_CLASS_UTILITY_BLOCK).texture("utility_block/lowclass")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.MIDDLE_CLASS_UTILITY_BLOCK)
                .texture("utility_block/middleclass")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.MIRROR_BLOCK)
                .textures("mirror/top_bottom", "mirror/top_bottom", "mirror/side").build();
        registerCustomSolidBlock(bsmg, ModBlocks.MONOCHROME_DARK_SANDSTONE_ENGRAVED)
                .texture("ashlar_engraved/light_brown/all").build();
        registerCustomSolidBlock(bsmg, ModBlocks.MONOCHROME_SANDSTONE_ENGRAVED)
                .texture("ashlar_engraved/westerlands/all").build();
        registerCustomSolidBlock(bsmg, ModBlocks.NETHER_BRICK_KEYSTONE).texture("ashlar_half/black/embellished")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.NORTHERN_CARVINGS).textures("wood/spruce/carving/top_bottom",
                "wood/spruce/carving/top_bottom", "wood/spruce/carving/side").build();
        registerCustomSolidBlock(bsmg, ModBlocks.NOTE_UTILITY_BLOCK).texture("utility_block/note").build();
        registerCustomSolidBlock(bsmg, ModBlocks.OLIVE_BASKET)
                .textures("crate_block/basket_bottom", "crate_block/basket_olives",
                        "crate_block/basket_side")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.OPEN_BASKET)
                .textures("crate_block/basket_bottom", "crate_block/basket_top",
                        "crate_block/basket_side")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.OPEN_CRATE)
                .randomTexture("crate_block/side_bot1", "crate_block/crate_top_empty",
                        "crate_block/side_bot1")
                .randomTexture("crate_block/side_bot2", "crate_block/crate_top_empty",
                        "crate_block/side_bot2")
                .randomTexture("crate_block/side_bot3", "crate_block/crate_top_empty",
                        "crate_block/side_bot3")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.ORANGE_BASKET)
                .textures("crate_block/basket_bottom", "crate_block/basket_oranges",
                        "crate_block/basket_side")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.ORANGE_BRICK_ARCH_DOUBLE)
                .textures("brick/orange/all1", "brick/orange/all1", "brick/orange/arch_double").build();
        registerCustomSolidBlock(bsmg, ModBlocks.ORANGE_BRICK_ARCH_SINGLE)
                .textures("brick/orange/all1", "brick/orange/all1", "brick/orange/arch_single").build();
        registerCustomSolidBlock(bsmg, ModBlocks.ORANGE_BRICK_DENTIL)
                .textures("brick/orange/all1", "brick/orange/all1", "brick/orange/dentil").build();
        registerCustomSolidBlock(bsmg, ModBlocks.ORANGE_BRICK_ROWLOCK)
                .randomTexture("brick/orange/all1", "brick/orange/all1", "brick/orange/rowlock1")
                .randomTexture("brick/orange/all1", "brick/orange/all1", "brick/orange/rowlock2")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.ORNATE_MARBLE).texture("marble/quartz/ornate/side").build();
        registerCustomSolidBlock(bsmg, ModBlocks.ORNATE_SANDSTONE).texture("ashlar_third/sandstone/ornate/side")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.PARQUET_FLOOR).texture("wood/oak/ornate").build();
        registerCustomSolidBlock(bsmg, ModBlocks.PILED_BONES).texture("piled_bones/side").build();
        registerCustomSolidBlock(bsmg, ModBlocks.PINK_SANDSTONE_ENGRAVED)
                .texture("ashlar_engraved/sandy_pink/all")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.PISTON_TOP).texture("wood/oak/studded").build();
        registerCustomSolidBlock(bsmg, ModBlocks.POMEGRANATE_BASKET)
                .textures("crate_block/basket_bottom", "crate_block/basket_pomegranates",
                        "crate_block/basket_side")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.PURPLE_GRAPE_BASKET)
                .textures("crate_block/basket_bottom", "crate_block/basket_grape_purple",
                        "crate_block/basket_side")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.PURPLE_GRAPE_CRATE)
                .textures("crate_block/crate_side_crossbar_right",
                        "crate_block/crate_top_grape_purple",
                        "crate_block/crate_side_crossbar_crossed")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.REACH_BRICK_ENGRAVED).texture("ashlar_engraved/reach/all")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.REACH_OAK_WOOD_PANELLING)
                .randomTexture("wood/oak/panelling/top_bottom", "wood/oak/panelling/top_bottom",
                        "wood/oak/panelling/side1")
                .randomTexture("wood/oak/panelling/top_bottom", "wood/oak/panelling/top_bottom",
                        "wood/oak/panelling/side2")
                .randomTexture("wood/oak/panelling/top_bottom", "wood/oak/panelling/top_bottom",
                        "wood/oak/panelling/side3")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.REDORANGE_CARVED_SANDSTONE)
                .texture("redorange_carved_sandstone/side")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.RED_LANTERN2)
                .textures("lighting/lantern_red_bottom", "lighting/lantern_red_top",
                        "lighting/lantern_red_side")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.REACH_SPRUCE_WOOD_PANELLING)
                .randomTexture("wood/spruce/panelling/top_bottom", "wood/spruce/panelling/top_bottom",
                        "wood/spruce/panelling/side1")
                .randomTexture("wood/spruce/panelling/top_bottom", "wood/spruce/panelling/top_bottom",
                        "wood/spruce/panelling/side2")
                .randomTexture("wood/spruce/panelling/top_bottom", "wood/spruce/panelling/top_bottom",
                        "wood/spruce/panelling/side3")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.SALT_CRATE)
                .randomTexture("crate_block/side_bot1", "crate_block/crate_top_salt",
                        "crate_block/side_bot1")
                .randomTexture("crate_block/side_bot2", "crate_block/crate_top_salt",
                        "crate_block/side_bot2")
                .randomTexture("crate_block/side_bot3", "crate_block/crate_top_salt",
                        "crate_block/side_bot3")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.SANDY_STONE_SLABS).randomTexture("sandy_stone/sandy_stone_0")
                .randomTexture("sandy_stone/sandy_stone_1").randomTexture("sandy_stone/sandy_stone_2")
                .randomTexture("sandy_stone/sandy_stone_3").randomTexture("sandy_stone/sandy_stone_4")
                .randomTexture("sandy_stone/sandy_stone_5").build();
        registerCustomSolidBlock(bsmg, ModBlocks.SEPT_CRYSTAL_LARGE).texture("crystal/all").build();
        registerCustomSolidBlock(bsmg, ModBlocks.SHOP_UTILITY_BLOCK).texture("utility_block/shop").build();
        registerCustomSolidBlock(bsmg, ModBlocks.SILVER_TIN_CRATE)
                .textures("crate_block/crate_side_crossbar_right",
                        "crate_block/crate_top_tin", "crate_block/crate_side_crossbar_crossed")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.SMALL_ORANGE_BRICKS_ORNATE_TOP)
                .textures("brick/orange/ornate/top_bottom", "brick/orange/ornate/top_bottom",
                        "brick/orange/all1")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.SMALL_ORANGE_BRICKS_ORNATE)
                .textures("brick/orange/ornate/top_bottom", "brick/orange/ornate/top_bottom",
                        "brick/orange/all1")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.SMALL_SMOOTH_STONE_BRICK_BLUE_PLASTER)
                .textures("ashlar_quarter_rounded/grey/all", "ashlar_quarter_rounded/grey/all",
                        "plaster/smooth/blue/all")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.SMALL_SMOOTH_STONE_BRICK_WHITE_PLASTER)
                .textures("ashlar_quarter_rounded/grey/all", "ashlar_quarter_rounded/grey/all",
                        "plaster/smooth/brown_white/all")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.SMALL_STONE_BRICK_WHITE_PLASTER)
                .textures("ashlar_quarter/green_grey/all", "ashlar_quarter/green_grey/all",
                        "plaster/smooth/white/all")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.SMALL_WHITE_BRICK_BROWNISH_WHITE_PLASTER)
                .textures("ashlar_quarter_rounded/white/all", "ashlar_quarter_rounded/white/all",
                        "plaster/smooth/brown_white/all")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.SMALL_WHITE_BRICK_WHITE_PLASTER)
                .textures("ashlar_quarter_rounded/white/all", "ashlar_quarter_rounded/white/all",
                        "plaster/smooth/white/all")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.SOURLEAF_BASKET)
                .textures("crate_block/basket_bottom", "crate_block/basket_sourleaf",
                        "crate_block/basket_side")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.SOURLEAF_CRATE)
                .textures("crate_block/crate_side_crossbar_right",
                        "crate_block/crate_sourleaf", "crate_block/crate_side_crossbar_crossed")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.SOUTHERN_BRICK_ARCH_FLAT)
                .textures("brick/southern/all1", "brick/southern/all1", "brick/southern/arch_flat")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.SOUTHERN_BRICK_ARCH)
                .textures("brick/southern/all1", "brick/southern/all1", "brick/southern/arch_flat")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.SOUTHERN_BRICK_LINTEL)
                .textures("brick/southern/all1", "brick/southern/all1", "brick/southern/lintel")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.SPECIAL_UTILITY_BLOCK).texture("utility_block/special")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.SPIT_ROAST).texture("meat_spitroast/all").build();
        registerCustomSolidBlock(bsmg, ModBlocks.SQUASH).textures("squash/top", "squash/top", "squash/side")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.STACKED_BONES_SOLID).textures(
                "stacked_bones/bone_stacked_front",
                "stacked_bones/bone_stacked_front", "stacked_bones/bone_stacked_rotated").build();
        registerCustomSolidBlock(bsmg, ModBlocks.STORMLANDS_BRICK_ENGRAVED)
                .texture("ashlar_engraved/stormlands/all")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.TABLE_BOOKS)
                .textures("bench_block/spruce_top", "bench_block/table_top",
                        "bench_block/table_drawer_books_side")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.TABLE_DRAWERS)
                .textures("bench_block/spruce_top", "bench_block/table_top",
                        "bench_block/table_drawers_side")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.THICK_GRASS_BLOCK).randomTexture("grass_block/forest_top1")
                .randomTexture("grass_block/forest_top2").randomTexture("grass_block/forest_top3")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.TABLE_WIDGETS)
                .textures("bench_block/spruce_top", "bench_block/table_top",
                        "bench_block/table_drawer_widgets_side")
                .build();

        // Table Blocks
        registerCustomTableBlock(bsmg, ModBlocks.OAK_TABLE).texture("wood/oak/all").build();


        registerCustomSolidBlock(bsmg, ModBlocks.TERRACOTTA_ENGRAVED).texture("ashlar_engraved/terracotta/all")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.TURNIP_BASKET)
                .textures("crate_block/basket_bottom", "crate_block/basket_turnip",
                        "crate_block/basket_side")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.TURNIP_CRATE)
                .randomTexture("crate_block/side_bot1", "crate_block/crate_top_turnip",
                        "crate_block/side_bot1")
                .randomTexture("crate_block/side_bot2", "crate_block/crate_top_turnip",
                        "crate_block/side_bot2")
                .randomTexture("crate_block/side_bot3", "crate_block/crate_top_turnip",
                        "crate_block/side_bot3")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.UNUSED_BROWN_PLASTER)
                .textures("ashlar_quarter_rounded/light_brown/all",
                        "ashlar_quarter_rounded/light_brown/all",
                        "plaster/smooth/brown_white/all")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.UNUSED_PURPLE_PLASTER)
                .textures("ashlar_quarter_rounded/dark_red/all",
                        "ashlar_quarter_rounded/dark_red/all", "plaster/smooth/brown_white/all")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.VIVID_DARK_SANDSTONE_ENGRAVED)
                .texture("ashlar_engraved/dark_tan/all")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.VIVID_SANDSTONE_ENGRAVED).texture("ashlar_engraved/tan/all")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.WATER_BARREL)
                .randomTexture("barrel_closed/barrel_top_closed", "crate_block/barrel_top_water",
                        "barrel_sides/side0")
                .randomTexture("barrel_closed/barrel_top_closed", "crate_block/barrel_top_water",
                        "barrel_sides/side1")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.WHITE_BRICK_ENGRAVED).texture("ashlar_engraved/white/all")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.WHITE_GRAPE_BASKET)
                .textures("crate_block/basket_bottom", "crate_block/basket_grape_white",
                        "crate_block/basket_side")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.WHITE_GRAPE_CRATE).textures(
                "crate_block/crate_side_crossbar_right",
                "crate_block/crate_top_grape_white", "crate_block/crate_side_crossbar_crossed").build();
        registerCustomSolidBlock(bsmg, ModBlocks.WINTERFELL_CARVING)
                .texture("ashlar_third/dark_grey/carving/side")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.WIP_UTILITY_BLOCK).texture("utility_block/wip").build();
        registerCustomSolidBlock(bsmg, ModBlocks.WORKSHOP_UTILITY_BLOCK).texture("utility_block/workshop")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.YARD_UTILITY_BLOCK).texture("utility_block/yard").build();
        registerCustomSolidBlock(bsmg, ModBlocks.YELLOW_STAINED_CLAY).texture("clay/yellow_stained_clay")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.TIMBER_NORTHERN_BLUE_BRESSUMMER)
                .state("plaster/smooth/gulltown_blue/all", "wood/northern/all",
                        "plaster/smooth/gulltown_blue/all")
                .state("plaster/smooth/light_blue/all", "wood/northern/all",
                        "plaster/smooth/light_blue/all")
                .state("plaster/smooth/blue/all", "wood/northern/all", "plaster/smooth/blue/all")
                .state("plaster/rough/gulltown_blue/all1", "wood/northern/all",
                        "plaster/rough/gulltown_blue/all1")
                .state("plaster/wattle/gulltown_blue/all", "wood/northern/all",
                        "plaster/wattle/gulltown_blue/all")
                .build();
        registerCustomSolidBlock(bsmg, ModBlocks.TIMBER_NORTHERN_GREEN_LEFTHATCH)
                .state("plaster/smooth/gulltown_green/all", "wood/northern/all",
                        "plaster/smooth/gulltown_green/all")
                .state("plaster/smooth/green/all", "wood/northern/all", "plaster/smooth/green/all")
                .state("plaster/smooth/highgarden_green/all", "wood/northern/all",
                        "plaster/smooth/highgarden_green/all")
                .state("plaster/rough/gulltown_green/all1", "wood/northern/all",
                        "plaster/rough/gulltown_green/all1")
                .state("plaster/wattle/gulltown_green/all", "wood/northern/all",
                        "plaster/wattle/gulltown_green/all")
                .build();

        // Log Blocks
        registerCustomLogBlock(bsmg, ModBlocks.ARCHERY_TARGET)
                .textures("archery_target/side", "archery_target/front")
                .build();

        registerCustomLogBlock(bsmg, ModBlocks.CLOSED_BARREL)
                .textures("barrel_sides/side1", "barrel_closed/barrel_top_closed")
                .build();

        registerCustomLogBlock(bsmg, ModBlocks.FIREWOOD)
                .textures("firewood/side", "firewood/top")
                .build();

        registerCustomLogBlock(bsmg, ModBlocks.JUNGLE_LOG_CHAIN)
                .textures("bark/jungle/chain", "bark/jungle/top")
                .build();

        registerCustomLogBlock(bsmg, ModBlocks.JUNGLE_LOG_ROPE)
                .textures("bark/jungle/rope", "bark/jungle/top")
                .build();

        registerCustomLogBlock(bsmg, ModBlocks.MARBLE_PILLAR_VERTICAL_CTM)
                .textures("marble/quartz/column_side_ctm", "marble/quartz/column_topbottom")
                .build();

        registerCustomLogBlock(bsmg, ModBlocks.MARBLE_PILLAR)
                .textures("marble/quartz/column_side", "marble/quartz/column_topbottom")
                .build();

        registerCustomLogBlock(bsmg, ModBlocks.MOSSY_BIRCH_LOG)
                .textures("bark/birch/mossy/side", "bark/birch/mossy/top")
                .build();

        registerCustomLogBlock(bsmg, ModBlocks.MOSSY_JUNGLE_LOG)
                .textures("bark/jungle/mossy/side", "bark/jungle/mossy/top")
                .build();

        registerCustomLogBlock(bsmg, ModBlocks.MOSSY_OAK_LOG)
                .textures("bark/oak/mossy/side", "bark/oak/mossy/top")
                .build();

        registerCustomLogBlock(bsmg, ModBlocks.MOSSY_SPRUCE_LOG)
                .textures("bark/spruce/mossy/side", "bark/spruce/mossy/top")
                .build();

        registerCustomLogBlock(bsmg, ModBlocks.OAK_LOG_CHAIN)
                .textures("bark/oak/chain", "bark/oak/top")
                .build();

        registerCustomLogBlock(bsmg, ModBlocks.OAK_LOG_ROPE)
                .textures("bark/oak/rope", "bark/oak/top")
                .build();

        registerCustomLogBlock(bsmg, ModBlocks.PALM_TREE_LOG)
                .textures("bark/palm/side", "bark/palm/top")
                .build();

        registerCustomLogBlock(bsmg, ModBlocks.SANDSTONE_PILLAR)
                .textures("ashlar_third/sandstone/column_side", "ashlar_third/sandstone/column_top")
                .build();

        registerCustomLogBlock(bsmg, ModBlocks.SPRUCE_LOG_CHAIN)
                .textures("bark/spruce/chain", "bark/spruce/top")
                .build();

        registerCustomLogBlock(bsmg, ModBlocks.SPRUCE_LOG_ROPE)
                .textures("bark/spruce/rope", "bark/spruce/top")
                .build();

        registerCustomLogBlock(bsmg, ModBlocks.STACKED_BONES)
                .textures("stacked_bones/bone_stacked_side", "stacked_bones/bone_stacked_front")
                .build();

        registerCustomLogBlock(bsmg, ModBlocks.WEIRWOOD_FACE_0)
                .textures("bark/weirwood/side", "bark/weirwood/face_0")
                .build();

        registerCustomLogBlock(bsmg, ModBlocks.WEIRWOOD_FACE_1)
                .textures("bark/weirwood/side", "bark/weirwood/face_1")
                .build();

        registerCustomLogBlock(bsmg, ModBlocks.WEIRWOOD_FACE_2)
                .textures("bark/weirwood/side", "bark/weirwood/face_2")
                .build();

        registerCustomLogBlock(bsmg, ModBlocks.WEIRWOOD_FACE_3)
                .textures("bark/weirwood/side", "bark/weirwood/face_3")
                .build();

        registerCustomLogBlock(bsmg, ModBlocks.WEIRWOOD_FACE_4)
                .textures("bark/weirwood/side", "bark/weirwood/face_4")
                .build();

        registerCustomLogBlock(bsmg, ModBlocks.WEIRWOOD_FACE_5)
                .textures("bark/weirwood/side", "bark/weirwood/face_5")
                .build();

        registerCustomLogBlock(bsmg, ModBlocks.WEIRWOOD_FACE_6)
                .textures("bark/weirwood/side", "bark/weirwood/face_6")
                .build();

        registerCustomLogBlock(bsmg, ModBlocks.WEIRWOOD_FACE_7)
                .textures("bark/weirwood/side", "bark/weirwood/face_7")
                .build();

        registerCustomLogBlock(bsmg, ModBlocks.WEIRWOOD_FACE_8)
                .textures("bark/weirwood/side", "bark/weirwood/face_8")
                .build();

        registerCustomLogBlock(bsmg, ModBlocks.WEIRWOOD_SCARS)
                .textures("bark/weirwood/side", "bark/weirwood/scars")
                .build();

        // Slab Blocks
        registerCustomSlabBlock(bsmg, ModBlocks.APPLE_BASKET_SLAB)
                .textures("crate_block/basket_bottom", "crate_block/basket_apple",
                        "crate_block/basket_side_slab")
                .build();

        registerCustomSlabBlock(bsmg, ModBlocks.APRICOT_BASKET_SLAB)
                .textures("crate_block/basket_bottom", "crate_block/basket_apricot",
                        "crate_block/basket_side_slab")
                .build();

        registerCustomSlabBlock(bsmg, ModBlocks.CLOSED_BASKET_SLAB)
                .textures("crate_block/basket_bottom", "crate_block/basket_top_closed",
                        "crate_block/basket_side_slab")
                .build();

        registerCustomSlabBlock(bsmg, ModBlocks.BERRY_BASKET_SLAB)
                .textures("crate_block/basket_bottom", "crate_block/basket_berry",
                        "crate_block/basket_side_slab")
                .build();

        registerCustomSlabBlock(bsmg, ModBlocks.CARROT_BASKET_SLAB)
                .textures("crate_block/basket_bottom", "crate_block/basket_carrot",
                        "crate_block/basket_side_slab")
                .build();

        registerCustomSlabBlock(bsmg, ModBlocks.CUT_GRAIN_FLOUR_SACK)
                .textures("grain_sack/all", "grain_sack/cut", "grain_sack/front", "grain_sack/front",
                        "grain_sack/side")
                .build();

        registerCustomSlabBlock(bsmg, ModBlocks.DATE_BASKET_SLAB)
                .textures("crate_block/basket_bottom", "crate_block/basket_dates",
                        "crate_block/basket_side_slab")
                .build();

        registerCustomSlabBlock(bsmg, ModBlocks.FIREWOOD_SLAB)
                .textures("firewood/side", "firewood/side", "firewood/top")
                .build();

        registerCustomSlabBlock(bsmg, ModBlocks.FISH_BASKET_SLAB)
                .textures("crate_block/basket_bottom", "crate_block/basket_fish",
                        "crate_block/basket_side_slab")
                .build();

        registerCustomSlabBlock(bsmg, ModBlocks.GRAIN_BASKET_SLAB)
                .textures("crate_block/basket_bottom", "crate_block/basket_grain",
                        "crate_block/basket_side_slab")
                .build();

        registerCustomSlabBlock(bsmg, ModBlocks.GRAIN_FLOUR_SACK)
                .textures("grain_sack/all", "grain_sack/all", "grain_sack/front", "grain_sack/front",
                        "grain_sack/side")
                .build();

        registerCustomSlabBlock(bsmg, ModBlocks.HOP_BASKET_SLAB)
                .textures("crate_block/basket_bottom", "crate_block/basket_hop",
                        "crate_block/basket_side_slab")
                .build();

        // Branch Blocks
        registerCustomBranchBlock(bsmg, ModBlocks.OAK_BRANCH).texture("bark/oak/side")
                .build();
        registerCustomBranchBlock(bsmg, ModBlocks.BIRCH_BRANCH).texture("bark/birch/side").build();

        // Door Blocks
        registerCustomDoorBlock(bsmg, ModBlocks.BIRCH_DOOR)
                .textures("wood/birch/door_top", "wood/birch/door_bottom").build();
        registerCustomDoorBlock(bsmg, ModBlocks.EYRIE_WEIRWOOD_DOOR)
                .textures("door_block/door_weirwood_top", "door_block/door_weirwood_bottom").build();
        registerCustomDoorBlock(bsmg, ModBlocks.GREY_WOOD_DOOR)
                .textures("wood/grey/door_top", "wood/grey/door_bottom").build();
        registerCustomDoorBlock(bsmg, ModBlocks.HARRENHAL_SECRET_DOOR)
                .textures("ashlar_third/black/all_noctm", "ashlar_third/black/all_noctm").build();
        registerCustomDoorBlock(bsmg, ModBlocks.JUNGLE_DOOR)
                .textures("wood/jungle/door_top", "wood/jungle/door_bottom").build();
        registerCustomDoorBlock(bsmg, ModBlocks.LOCKED_BIRCH_DOOR)
                .textures("wood/birch/door_locked_top", "wood/birch/door_locked_bottom").build();
        registerCustomDoorBlock(bsmg, ModBlocks.LOCKED_DARK_NORTHERN_WOOD_DOOR)
                .textures("wood/northern/door_locked_top", "wood/northern/door_locked_bottom").build();
        registerCustomDoorBlock(bsmg, ModBlocks.LOCKED_GREY_WOOD_DOOR)
                .textures("wood/grey/door_locked_top", "wood/grey/door_locked_bottom").build();
        registerCustomDoorBlock(bsmg, ModBlocks.LOCKED_JUNGLE_DOOR)
                .textures("wood/jungle/door_locked_top", "wood/jungle/door_locked_bottom").build();
        registerCustomDoorBlock(bsmg, ModBlocks.LOCKED_OAK_DOOR)
                .textures("wood/oak/door_locked_top", "wood/oak/door_locked_bottom").build();
        registerCustomDoorBlock(bsmg, ModBlocks.LOCKED_SPRUCE_DOOR)
                .textures("wood/spruce/door_locked_top", "wood/spruce/door_locked_bottom").build();
        registerCustomDoorBlock(bsmg, ModBlocks.LOCKED_WHITE_WOOD_DOOR)
                .textures("wood/white/door_locked_top", "wood/white/door_locked_bottom").build();
        registerCustomDoorBlock(bsmg, ModBlocks.NORTHERN_WOOD_DOOR)
                .textures("wood/northern/door_top", "wood/northern/door_bottom").build();
        registerCustomDoorBlock(bsmg, ModBlocks.OAK_DOOR)
                .textures("wood/oak/door_top", "wood/oak/door_bottom").build();
        registerCustomDoorBlock(bsmg, ModBlocks.RED_KEEP_SECRET_DOOR)
                .textures("ashlar_third/black/all_noctm", "ashlar_third/black/all_noctm").build();
        registerCustomDoorBlock(bsmg, ModBlocks.SPRUCE_DOOR)
                .textures("wood/spruce/door_top", "wood/spruce/door_bottom").build();
        registerCustomDoorBlock(bsmg, ModBlocks.WHITE_WOOD_DOOR)
                .textures("wood/white/door_top", "wood/white/door_bottom").build();

        // Half Door Blocks (Shutters)
        registerCustomHalfDoorBlock(bsmg, ModBlocks.BIRCH_WINDOW_SHUTTERS)
                .texture("wood/birch/shutters").build();
        registerCustomHalfDoorBlock(bsmg, ModBlocks.DORNE_RED_WINDOW_SHUTTERS)
                .texture("shutter_block/shutters_dorne").build();
        registerCustomHalfDoorBlock(bsmg, ModBlocks.GREEN_LANNISPORT_WINDOW_SHUTTERS)
                .texture("shutter_block/shutters_lannisport").build();
        registerCustomHalfDoorBlock(bsmg, ModBlocks.GREY_WOOD_WINDOW_SHUTTERS)
                .texture("wood/grey/shutters").build();
        registerCustomHalfDoorBlock(bsmg, ModBlocks.JUNGLE_WINDOW_SHUTTERS)
                .texture("wood/jungle/shutters").build();
        registerCustomHalfDoorBlock(bsmg, ModBlocks.NORTHERN_WOOD_WINDOW_SHUTTERS)
                .texture("wood/northern/shutters").build();
        registerCustomHalfDoorBlock(bsmg, ModBlocks.OAK_WINDOW_SHUTTERS)
                .texture("wood/oak/shutters").build();
        registerCustomHalfDoorBlock(bsmg, ModBlocks.REACH_BLUE_WINDOW_SHUTTERS)
                .texture("shutter_block/shutters_reach").build();
        registerCustomHalfDoorBlock(bsmg, ModBlocks.SPRUCE_WINDOW_SHUTTERS)
                .texture("wood/spruce/shutters").build();
        registerCustomHalfDoorBlock(bsmg, ModBlocks.WHITE_WOOD_WINDOW_SHUTTERS)
                .texture("wood/white/shutters").build();

        // Pane Blocks
        registerCustomPaneBlock(bsmg, ModBlocks.DORNE_CARVED_STONE_WINDOW)
                .texture("pane_block/moorish_stone_window_pane").build();
        registerCustomPaneBlock(bsmg, ModBlocks.DORNE_CARVED_WOODEN_WINDOW)
                .texture("pane_block/moorish_wood_window_pane").build();
        registerCustomPaneBlock(bsmg, ModBlocks.IRON_BARS)
                .texture("bars_iron_block/iron_bars").build();
        registerCustomPaneBlock(bsmg, ModBlocks.IRON_CROSSBAR)
                .texture("bars_iron_block/bars_iron_crossbars").build();
        registerCustomPaneBlock(bsmg, ModBlocks.OXIDIZED_IRON_BARS)
                .texture("bars_iron_block/bars_iron_oxidized").build();
        registerCustomPaneBlock(bsmg, ModBlocks.OXIDIZED_IRON_CROSSBAR)
                .texture("bars_iron_block/bars_iron_oxidized_crossbars").build();
        registerCustomPaneBlock(bsmg, ModBlocks.VERTICAL_NET)
                .randomTexture("vertical_net/vertical_net1")
                .randomTexture("vertical_net/vertical_net2")
                .randomTexture("vertical_net/vertical_net3")
                .randomTexture("vertical_net/vertical_net4")
                .randomTexture("vertical_net/vertical_net5")
                .build();

        // Torch Blocks
        registerCustomTorchBlock(bsmg, ModBlocks.TORCH).texture("lighting/torch").build();
        registerCustomTorchBlock(bsmg, ModBlocks.TORCH_UNLIT).texture("lighting/torch_unlit").build();
        registerCustomTorchBlock(bsmg, ModBlocks.CANDLE).texture("lighting/candle").build();
        registerCustomTorchBlock(bsmg, ModBlocks.CANDLE_UNLIT).texture("lighting/candle_unlit").build();

        // Chair Blocks
        registerCustomChairBlock(bsmg, ModBlocks.OAK_CHAIR).texture("bark/oak/side").build();

        // Arrow Slit Blocks
        registerCustomArrowSlitBlock(bsmg, ModBlocks.ARBOR_BRICK_ARROW_SLIT).texture("ashlar_third/arbor/all").build();

        // Fan Blocks
        registerCustomFanBlock(bsmg, ModBlocks.CORAL_TUBE_FAN)
                .randomTexture("coral/tube/fan1")
                .randomTexture("coral/tube/fan2")
                .build();

        registerCustomFanBlock(bsmg, ModBlocks.CORAL_BRAIN_FAN)
                .randomTexture("coral/brain/fan1")
                .randomTexture("coral/brain/fan2")
                .build();

        registerCustomFanBlock(bsmg, ModBlocks.CORAL_BUBBLE_FAN)
                .randomTexture("coral/bubble/fan1")
                .randomTexture("coral/bubble/fan2")
                .build();

        registerCustomFanBlock(bsmg, ModBlocks.CORAL_FIRE_FAN)
                .randomTexture("coral/fire/fan1")
                .randomTexture("coral/fire/fan2")
                .build();

        registerCustomFanBlock(bsmg, ModBlocks.CORAL_HORN_FAN)
                .randomTexture("coral/horn/fan1")
                .randomTexture("coral/horn/fan2")
                .build();

        // Rail Blocks
        registerCustomRailBlock(bsmg, ModBlocks.FANCY_BLUE_CARPET).texture("carpet/fancy_blue_carpet").build();
        registerCustomRailBlock(bsmg, ModBlocks.FANCY_RED_CARPET).texture("carpet/fancy_red_carpet").build();
        registerCustomRailBlock(bsmg, ModBlocks.HORIZONTAL_CHAIN).textures("rail_block/chain", "rail_block/chain_turned").build();
        registerCustomRailBlock(bsmg, ModBlocks.HORIZONTAL_NET).textures("rail_block/net_large", "rail_block/net_large_turned").build();
        registerCustomRailBlock(bsmg, ModBlocks.HORIZONTAL_ROPE).textures("rail_block/rope", "rail_block/rope_turned").build();
        registerCustomRailBlock(bsmg, ModBlocks.PACKED_SNOW).textures("rail_block/packed_snow", "rail_block/packed_snow_turned").build();

        // Plant Block
        registerCustomPlantBlock(bsmg, ModBlocks.BLUE_BELLS).texture("flowers/blue_bells").build();
        registerCustomPlantBlock(bsmg, ModBlocks.BLUE_CHICORY)
                .randomTexture("flowers/blue_chicory/side1")
                .randomTexture("flowers/blue_chicory/side2")
                .randomTexture("flowers/blue_chicory/side3")
                .randomTexture("flowers/blue_chicory/side4")
                .build();
        registerCustomPlantBlock(bsmg, ModBlocks.BLUE_FORGETMENOTS)
                .randomTexture("flowers/blue_forgetmenots1")
                .randomTexture("flowers/blue_forgetmenots2")
                .randomTexture("flowers/blue_forgetmenots3")
                .randomTexture("flowers/blue_forgetmenots4")
                .build();
        registerCustomPlantBlock(bsmg, ModBlocks.BLUE_FLAX)
                .randomTexture("flowers/blue_flax1")
                .randomTexture("flowers/blue_flax2")
                .randomTexture("flowers/blue_flax3")
                .randomTexture("flowers/blue_flax4")
                .build();
        registerCustomPlantBlock(bsmg, ModBlocks.BLUE_HYACINTH)
                .randomTexture("flowers/blue_hyacinth1")
                .randomTexture("flowers/blue_hyacinth2")
                .randomTexture("flowers/blue_hyacinth3")
                .randomTexture("flowers/blue_hyacinth4")
                .build();
        registerCustomPlantBlock(bsmg, ModBlocks.BLUE_ORCHID)
                .randomTexture("flowers/blue_orchid1")
                .randomTexture("flowers/blue_orchid2")
                .randomTexture("flowers/blue_orchid3")
                .build();
        registerCustomPlantBlock(bsmg, ModBlocks.BLUE_SWAMP_BELLS).texture("flowers/blue_swamp_bells1").build();

        registerCustomPlantBlock(bsmg, ModBlocks.BRACKEN)
                .randomTexture("bracken/side1")
                .randomTexture("bracken/side2")
                .randomTexture("bracken/side3")
                .randomTexture("bracken/side4")
                .randomTexture("bracken/side5")
                .randomTexture("bracken/side6")
                .randomTexture("bracken/side7")
                .randomTexture("bracken/side8")
                .build();

        registerCustomPlantBlock(bsmg, ModBlocks.BROWN_MUSHROOM_1).texture("brown_mushroom_block/mushroom_brown_0").build();
        registerCustomPlantBlock(bsmg, ModBlocks.BROWN_MUSHROOM_2).texture("brown_mushroom_block/mushroom_brown_1").build();
        registerCustomPlantBlock(bsmg, ModBlocks.BROWN_MUSHROOM_3).texture("brown_mushroom_block/mushroom_brown_2").build();
        registerCustomPlantBlock(bsmg, ModBlocks.BROWN_MUSHROOM_4).texture("brown_mushroom_block/mushroom_brown_3").build();
        registerCustomPlantBlock(bsmg, ModBlocks.BROWN_MUSHROOM_5).texture("brown_mushroom_block/mushroom_brown_4").build();
        registerCustomPlantBlock(bsmg, ModBlocks.BROWN_MUSHROOM_6).texture("brown_mushroom_block/mushroom_brown_5").build();
        registerCustomPlantBlock(bsmg, ModBlocks.BROWN_MUSHROOM_7).texture("brown_mushroom_block/mushroom_brown_6").build();
        registerCustomPlantBlock(bsmg, ModBlocks.BROWN_MUSHROOM_8).texture("brown_mushroom_block/mushroom_brown_7").build();
        registerCustomPlantBlock(bsmg, ModBlocks.BROWN_MUSHROOM_9).texture("brown_mushroom_block/mushroom_brown_8").build();
        registerCustomPlantBlock(bsmg, ModBlocks.BROWN_MUSHROOM_10).texture("brown_mushroom_block/mushroom_brown_9").build();
        registerCustomPlantBlock(bsmg, ModBlocks.BROWN_MUSHROOM_11).texture("brown_mushroom_block/mushroom_brown_10").build();
        registerCustomPlantBlock(bsmg, ModBlocks.BROWN_MUSHROOM_12).texture("brown_mushroom_block/mushroom_brown_11").build();
        registerCustomPlantBlock(bsmg, ModBlocks.BROWN_MUSHROOM_13).texture("brown_mushroom_block/mushroom_brown_12").build();

        registerCustomPlantBlock(bsmg, ModBlocks.CORAL_BRAIN_WEB)
                .randomTexture("coral/brain/web1")
                .randomTexture("coral/brain/web2")
                .build();

        registerCustomPlantBlock(bsmg, ModBlocks.CORAL_BUBBLE_WEB)
                .randomTexture("coral/bubble/web1")
                .randomTexture("coral/bubble/web2")
                .build();

        registerCustomPlantBlock(bsmg, ModBlocks.CORAL_FIRE_WEB)
                .randomTexture("coral/fire/web1")
                .randomTexture("coral/fire/web2")
                .build();

        registerCustomPlantBlock(bsmg, ModBlocks.CORAL_HORN_WEB)
                .randomTexture("coral/horn/web1")
                .randomTexture("coral/horn/web2")
                .build();

        registerCustomPlantBlock(bsmg, ModBlocks.CORAL_TUBE_WEB)
                .randomTexture("coral/tube/web1")
                .randomTexture("coral/tube/web2")
                .build();

        registerCustomPlantBlock(bsmg, ModBlocks.COW_PARSELY)
                .randomTexture("cow_parsely/side1")
                .randomTexture("cow_parsely/side2")
                .build();

        registerCustomPlantBlock(bsmg, ModBlocks.CRANBERRY_BUSH)
                .randomTexture("cranberry/base1")
                .randomTexture("cranberry/base2")
                .build();

        registerCustomPlantBlock(bsmg, ModBlocks.DEAD_BRACKEN)
                .randomTexture("dead_bracken/side1")
                .randomTexture("dead_bracken/side2")
                .randomTexture("dead_bracken/side3")
                .randomTexture("dead_bracken/side4")
                .randomTexture("dead_bracken/side5")
                .randomTexture("dead_bracken/side6")
                .randomTexture("dead_bracken/side7")
                .randomTexture("dead_bracken/side8")
                .randomTexture("dead_bracken/side9")
                .randomTexture("dead_bracken/side10")
                .randomTexture("dead_bracken/side11")
                .randomTexture("dead_bracken/side12")
                .build();

        registerCustomPlantBlock(bsmg, ModBlocks.DEAD_BUSH)
                .randomTexture("dorne_bush_thorny/side1")
                .randomTexture("dorne_bush_thorny/side2")
                .randomTexture("dorne_bush_thorny/side3")
                .build();

        registerCustomPlantBlock(bsmg, ModBlocks.DEAD_SCRUB_GRASS)
                .randomTexture("flowers/dead_scrub_grass1")
                .randomTexture("flowers/dead_scrub_grass2")
                .randomTexture("flowers/dead_scrub_grass3")
                .randomTexture("flowers/dead_scrub_grass4")
                .randomTexture("flowers/dead_scrub_grass5")
                .randomTexture("flowers/dead_scrub_grass6")
                .randomTexture("flowers/dead_scrub_grass7")
                .randomTexture("flowers/dead_scrub_grass8")
                .randomTexture("flowers/dead_scrub_grass9")
                .build();

        registerCustomPlantBlock(bsmg, ModBlocks.DOCK_LEAF)
                .randomTexture("dock_leaf/side1")
                .randomTexture("dock_leaf/side2")
                .build();

        registerCustomPlantBlock(bsmg, ModBlocks.FIREWEED)
                .randomTexture("fireweed/side1")
                .randomTexture("fireweed/side2")
                .randomTexture("fireweed/side3")
                .randomTexture("fireweed/side4")
                .build();

        registerCustomPlantBlock(bsmg, ModBlocks.GRASS)
                .randomTexture("minecraft:block/fern/fern1")
                .randomTexture("minecraft:block/fern/fern2")
                .randomTexture("minecraft:block/fern/fern3")
                .randomTexture("minecraft:block/fern/fern4")
                .randomTexture("minecraft:block/fern/fern5")
                .randomTexture("minecraft:block/fern/fern6")
                .randomTexture("minecraft:block/fern/fern7")
                .randomTexture("minecraft:block/fern/fern8")
                .isTinted(true)
                .build();

        registerCustomPlantBlock(bsmg, ModBlocks.GREEN_LEAFY_HERB)
                .texture("flowers/green_leafy_herb")
                .build();

        registerCustomPlantBlock(bsmg, ModBlocks.GREEN_SCRUB_GRASS)
                .randomTexture("flowers/green_scrub_grass1")
                .randomTexture("flowers/green_scrub_grass2")
                .randomTexture("flowers/green_scrub_grass3")
                .randomTexture("flowers/green_scrub_grass4")
                .randomTexture("flowers/green_scrub_grass5")
                .randomTexture("flowers/green_scrub_grass6")
                .build();

        registerCustomPlantBlock(bsmg, ModBlocks.GREEN_SPINY_HERB)
                .randomTexture("flowers/green_spiny_herb1")
                .randomTexture("flowers/green_spiny_herb2")
                .randomTexture("flowers/green_spiny_herb3")
                .randomTexture("flowers/green_spiny_herb4")
                .randomTexture("flowers/green_spiny_herb5")
                .build();

        registerCustomPlantBlock(bsmg, ModBlocks.HEATHER)
                .randomTexture("heather/side1")
                .randomTexture("heather/side2")
                .randomTexture("heather/side3")
                .randomTexture("heather/side4")
                .build();

        registerCustomPlantBlock(bsmg, ModBlocks.KELP)
                .randomTexture("kelp/side1")
                .randomTexture("kelp/side2")
                .build();

        registerCustomPlantBlock(bsmg, ModBlocks.LADY_FERN)
                .randomTexture("lady_fern/side1")
                .randomTexture("lady_fern/side2")
                .randomTexture("lady_fern/side3")
                .randomTexture("lady_fern/side4")
                .build();

        registerCustomPlantBlock(bsmg, ModBlocks.MAGENTA_ROSES)
                .randomTexture("flowers/magenta_roses1")
                .randomTexture("flowers/magenta_roses2")
                .randomTexture("flowers/magenta_roses3")
                .randomTexture("flowers/magenta_roses4")
                .build();

        registerCustomPlantBlock(bsmg, ModBlocks.MEADOW_FESCUE)
                .randomTexture("flowers/meadow_fescue/side1")
                .randomTexture("flowers/meadow_fescue/side2")
                .build();

        registerCustomPlantBlock(bsmg, ModBlocks.NETTLE)
                .randomTexture("nettle/side1")
                .randomTexture("nettle/side2")
                .randomTexture("nettle/side3")
                .build();

        registerCustomPlantBlock(bsmg, ModBlocks.ORANGE_BELLS)
                .texture("flowers/orange_bells1")
                .build();

        registerCustomPlantBlock(bsmg, ModBlocks.ORANGE_BOG_ASPHODEL)
                .randomTexture("flowers/orange_bog_asphodel1")
                .randomTexture("flowers/orange_bog_asphodel2")
                .randomTexture("flowers/orange_bog_asphodel3")
                .randomTexture("flowers/orange_bog_asphodel4")
                .build();

        registerCustomPlantBlock(bsmg, ModBlocks.ORANGE_MARIGOLDS)
                .randomTexture("flowers/orange_marigolds1")
                .randomTexture("flowers/orange_marigolds2")
                .randomTexture("flowers/orange_marigolds3")
                .randomTexture("flowers/orange_marigolds4")
                .build();

        registerCustomPlantBlock(bsmg, ModBlocks.ORANGE_SUN_STAR)
                .randomTexture("flowers/orange_sun_star1")
                .randomTexture("flowers/orange_sun_star2")
                .build();

        registerCustomPlantBlock(bsmg, ModBlocks.ORANGE_TROLLIUS)
                .randomTexture("flowers/orange_trollius1")
                .randomTexture("flowers/orange_trollius2")
                .build();

        registerCustomPlantBlock(bsmg, ModBlocks.PINK_ALLIUM)
                .randomTexture("flowers/pink_allium1")
                .randomTexture("flowers/pink_allium2")
                .randomTexture("flowers/pink_allium3")
                .randomTexture("flowers/pink_allium4")
                .build();

        registerCustomPlantBlock(bsmg, ModBlocks.PINK_PRIMROSE)
                .randomTexture("flowers/pink_primrose1")
                .randomTexture("flowers/pink_primrose2")
                .build();

        registerCustomPlantBlock(bsmg, ModBlocks.PINK_ROSES)
                .randomTexture("flowers/pink_roses1")
                .randomTexture("flowers/pink_roses2")
                .randomTexture("flowers/pink_roses3")
                .randomTexture("flowers/pink_roses4")
                .build();

        registerCustomPlantBlock(bsmg, ModBlocks.PINK_SWEET_PEAS)
                .texture("flowers/pink_sweet_peas1")
                .build();

        registerCustomPlantBlock(bsmg, ModBlocks.PINK_THISTLE)
                .randomTexture("flowers/pink_thistle/side1")
                .randomTexture("flowers/pink_thistle/side2")
                .randomTexture("flowers/pink_thistle/side3")
                .randomTexture("flowers/pink_thistle/side4")
                .build();

        registerCustomPlantBlock(bsmg, ModBlocks.PINK_TULIPS)
                .randomTexture("flowers/pink_tulips1")
                .randomTexture("flowers/pink_tulips2")
                .randomTexture("flowers/pink_tulips3")
                .randomTexture("flowers/pink_tulips4")
                .build();

        registerCustomPlantBlock(bsmg, ModBlocks.PINK_WILDFLOWERS)
                .texture("flowers/pink_wildflowers")
                .build();

        registerCustomPlantBlock(bsmg, ModBlocks.RED_ASTER)
                .randomTexture("flowers/red_aster1")
                .randomTexture("flowers/red_aster2")
                .randomTexture("flowers/red_aster3")
                .randomTexture("flowers/red_aster4")
                .build();

        registerCustomPlantBlock(bsmg, ModBlocks.RED_CARNATIONS)
                .randomTexture("flowers/red_carnations1")
                .randomTexture("flowers/red_carnations2")
                .randomTexture("flowers/red_carnations3")
                .randomTexture("flowers/red_carnations4")
                .build();

        registerCustomPlantBlock(bsmg, ModBlocks.RED_CHRYSANTHEMUM)
                .texture("flowers/red_chrysanthemum1")
                .build();

        registerCustomPlantBlock(bsmg, ModBlocks.RED_DARK_ROSES)
                .randomTexture("flowers/red_dark_roses1")
                .randomTexture("flowers/red_dark_roses2")
                .randomTexture("flowers/red_dark_roses3")
                .randomTexture("flowers/red_dark_roses4")
                .build();

        registerCustomPlantBlock(bsmg, ModBlocks.RED_FERN)
                .randomTexture("red_fern/side1")
                .randomTexture("red_fern/side2")
                .randomTexture("red_fern/side3")
                .build();

        registerCustomPlantBlock(bsmg, ModBlocks.RED_FLOWERING_SPINY_HERB)
                .texture("flowers/red_flowering_spiny_herb1")
                .build();

        registerCustomPlantBlock(bsmg, ModBlocks.RED_MUSHROOM_1).texture("red_mushroom_block/mushroom_red_0").build();
        registerCustomPlantBlock(bsmg, ModBlocks.RED_MUSHROOM_2).texture("red_mushroom_block/mushroom_red_1").build();
        registerCustomPlantBlock(bsmg, ModBlocks.RED_MUSHROOM_3).texture("red_mushroom_block/mushroom_red_2").build();
        registerCustomPlantBlock(bsmg, ModBlocks.RED_MUSHROOM_4).texture("red_mushroom_block/mushroom_red_3").build();
        registerCustomPlantBlock(bsmg, ModBlocks.RED_MUSHROOM_5).texture("red_mushroom_block/mushroom_red_4").build();
        registerCustomPlantBlock(bsmg, ModBlocks.RED_MUSHROOM_6).texture("red_mushroom_block/mushroom_red_5").build();
        registerCustomPlantBlock(bsmg, ModBlocks.RED_MUSHROOM_7).texture("red_mushroom_block/mushroom_red_6").build();
        registerCustomPlantBlock(bsmg, ModBlocks.RED_MUSHROOM_8).texture("red_mushroom_block/mushroom_red_7").build();
        registerCustomPlantBlock(bsmg, ModBlocks.RED_MUSHROOM_9).texture("red_mushroom_block/mushroom_red_8").build();

        registerCustomPlantBlock(bsmg, ModBlocks.RED_POPPIES)
                .randomTexture("flowers/red_poppies1")
                .randomTexture("flowers/red_poppies2")
                .build();

        registerCustomPlantBlock(bsmg, ModBlocks.RED_ROSES)
                .randomTexture("flowers/red_roses1")
                .randomTexture("flowers/red_roses2")
                .randomTexture("flowers/red_roses3")
                .randomTexture("flowers/red_roses4")
                .build();

        registerCustomPlantBlock(bsmg, ModBlocks.RED_SORREL)
                .texture("flowers/red_sorrel1")
                .build();

        registerCustomPlantBlock(bsmg, ModBlocks.RED_SOURLEAF_BUSH)
                .randomTexture("flowers/red_sourleaf_bush1")
                .randomTexture("flowers/red_sourleaf_bush2")
                .build();

        registerCustomPlantBlock(bsmg, ModBlocks.RED_TULIPS)
                .randomTexture("flowers/red_tulips1")
                .randomTexture("flowers/red_tulips2")
                .randomTexture("flowers/red_tulips3")
                .randomTexture("flowers/red_tulips4")
                .build();

        registerCustomPlantBlock(bsmg, ModBlocks.STRAWBERRY_BUSH)
                .texture("flowers/strawberry")
                .build();

        registerCustomPlantBlock(bsmg, ModBlocks.THICK_GRASS)
                .randomTexture("minecraft:block/grass/grass1")
                .randomTexture("minecraft:block/grass/grass2")
                .randomTexture("minecraft:block/grass/grass3")
                .randomTexture("minecraft:block/grass/grass4")
                .randomTexture("minecraft:block/grass/grass5")
                .randomTexture("minecraft:block/grass/grass6")
                .randomTexture("minecraft:block/grass/grass7")
                .randomTexture("minecraft:block/grass/grass8")
                .randomTexture("minecraft:block/grass/grass9")
                .randomTexture("minecraft:block/grass/grass10")
                .build();

        registerCustomPlantBlock(bsmg, ModBlocks.UNSHADED_GRASS)
                .randomTexture("deadbush/side1")
                .randomTexture("deadbush/side2")
                .randomTexture("deadbush/side3")
                .randomTexture("deadbush/side4")
                .randomTexture("deadbush/side5")
                .randomTexture("deadbush/side6")
                .randomTexture("deadbush/side7")
                .randomTexture("deadbush/side8")
                .build();

        registerCustomPlantBlock(bsmg, ModBlocks.WHITE_CHAMOMILE)
                .randomTexture("flowers/white_chamomile1")
                .randomTexture("flowers/white_chamomile2")
                .randomTexture("flowers/white_chamomile3")
                .build();

        registerCustomPlantBlock(bsmg, ModBlocks.WHITE_DAISIES)
                .randomTexture("flowers/white_daisies1")
                .randomTexture("flowers/white_daisies2")
                .randomTexture("flowers/white_daisies3")
                .randomTexture("flowers/white_daisies4")
                .build();

        registerCustomPlantBlock(bsmg, ModBlocks.WHITE_LILYOFTHEVALLEY)
                .texture("flowers/white_lily_valley1")
                .build();

        registerCustomPlantBlock(bsmg, ModBlocks.WHITE_PEONY)
                .texture("flowers/white_peony1")
                .build();

        registerCustomPlantBlock(bsmg, ModBlocks.WHITE_ROSES)
                .randomTexture("flowers/white_roses1")
                .randomTexture("flowers/white_roses2")
                .randomTexture("flowers/white_roses3")
                .randomTexture("flowers/white_roses4")
                .build();

        registerCustomPlantBlock(bsmg, ModBlocks.YELLOW_BEDSTRAW)
                .randomTexture("flowers/yellow_bedstraw/side1")
                .randomTexture("flowers/yellow_bedstraw/side2")
                .randomTexture("flowers/yellow_bedstraw/side3")
                .build();

        registerCustomPlantBlock(bsmg, ModBlocks.YELLOW_BELLS)
                .texture("flowers/yellow_bells1")
                .build();

        registerCustomPlantBlock(bsmg, ModBlocks.YELLOW_BUTTERCUPS)
                .randomTexture("flowers/yellow_buttercups1")
                .randomTexture("flowers/yellow_buttercups2")
                .randomTexture("flowers/yellow_buttercups3")
                .randomTexture("flowers/yellow_buttercups4")
                .build();

        registerCustomPlantBlock(bsmg, ModBlocks.YELLOW_DAFFODILS)
                .randomTexture("flowers/yellow_daffodils1")
                .randomTexture("flowers/yellow_daffodils2")
                .randomTexture("flowers/yellow_daffodils3")
                .randomTexture("flowers/yellow_daffodils4")
                .build();

        registerCustomPlantBlock(bsmg, ModBlocks.YELLOW_DAISIES)
                .randomTexture("flowers/yellow_daisies1")
                .randomTexture("flowers/yellow_daisies2")
                .build();

        registerCustomPlantBlock(bsmg, ModBlocks.YELLOW_DANDELIONS)
                .randomTexture("flowers/yellow_dandelions1")
                .randomTexture("flowers/yellow_dandelions2")
                .randomTexture("flowers/yellow_dandelions3")
                .build();

        registerCustomPlantBlock(bsmg, ModBlocks.YELLOW_HELLEBORE)
                .randomTexture("flowers/yellow_hellebore1")
                .randomTexture("flowers/yellow_hellebore2")
                .build();

        registerCustomPlantBlock(bsmg, ModBlocks.YELLOW_LUPINE)
                .randomTexture("flowers/yellow_lupine1")
                .randomTexture("flowers/yellow_lupine2")
                .randomTexture("flowers/yellow_lupine3")
                .randomTexture("flowers/yellow_lupine4")
                .build();

        registerCustomPlantBlock(bsmg, ModBlocks.YELLOW_ROSES)
                .randomTexture("flowers/yellow_roses1")
                .randomTexture("flowers/yellow_roses2")
                .randomTexture("flowers/yellow_roses3")
                .randomTexture("flowers/yellow_roses4")
                .build();

        registerCustomPlantBlock(bsmg, ModBlocks.YELLOW_RUDBECKIA)
                .texture("flowers/yellow_rudbeckia1")
                .build();

        registerCustomPlantBlock(bsmg, ModBlocks.YELLOW_SUNFLOWER)
                .randomTexture("flowers/yellow_sunflower/side1")
                .randomTexture("flowers/yellow_sunflower/side2")
                .randomTexture("flowers/yellow_sunflower/side3")
                .build();

        registerCustomPlantBlock(bsmg, ModBlocks.YELLOW_TANSY)
                .texture("flowers/yellow_tansy")
                .build();

        registerCustomPlantBlock(bsmg, ModBlocks.YELLOW_WILDFLOWERS)
                .texture("flowers/yellow_wildflowers")
                .build();

        // Flowerbed Blocks
        registerCustomFlowerbedBlock(bsmg, ModBlocks.CLOVER)
                .stemTexture("flowerbed/clover_stem")
                .flowerTexture("flowerbed/clover")
                .build();

        // Web Blocks
        registerCustomCrossBlock(bsmg, ModBlocks.BEES)
                .texture("web_block/bug_bees")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.ALYSSAS_TEARS_MIST_ONE)
                .texture("alyssas_tears_mist/mist1")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.ALYSSAS_TEARS_MIST_TWO)
                .texture("alyssas_tears_mist/mist2")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.ALYSSAS_TEARS_MIST_THREE)
                .texture("alyssas_tears_mist/mist3")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.ALYSSAS_TEARS_MIST_FOUR)
                .texture("alyssas_tears_mist/mist4")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.BLACK_BRICICLE)
                .texture("ashlar_melted/black/bricicle/side")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.BUSHEL_OF_HERBS)
                .texture("web_block/food_herbs")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.BUSHEL_OF_SOURLEAF)
                .texture("web_block/food_sourleaf")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.BUTTERFLY_BLUE)
                .texture("web_block/bug_butterfly_blue")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.BUTTERFLY_ORANGE)
                .texture("web_block/bug_butterfly_orange")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.BUTTERFLY_RED)
                .texture("web_block/bug_butterfly_red")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.BUTTERFLY_WHITE)
                .texture("web_block/bug_butterfly_white")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.BUTTERFLY_YELLOW)
                .texture("web_block/bug_butterfly_yellow")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.CATTAILS)
                .randomTexture("cattails/side1")
                .randomTexture("cattails/side2")
                .randomTexture("cattails/side3")
                .randomTexture("cattails/side4")
                .randomTexture("cattails/side5")
                .randomTexture("cattails/side6")
                .randomTexture("cattails/side7")
                .randomTexture("cattails/side8")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.CHAIN_BLOCK_HARNESS)
                .texture("web_block/chain_blockharness")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.CHILI_RISTRA)
                .texture("web_block/food_chili_ristra")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.COBWEB)
                .randomTexture("cobweb/side1")
                .randomTexture("cobweb/side2")
                .randomTexture("cobweb/side3")
                .randomTexture("cobweb/side4")
                .randomTexture("cobweb/side5")
                .randomTexture("cobweb/side6")
                .randomTexture("cobweb/side7")
                .randomTexture("cobweb/side8")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.DEAD_FISH)
                .randomTexture("dead_fish/fish_dead1")
                .randomTexture("dead_fish/fish_dead2")
                .randomTexture("dead_fish/fish_dead3")
                .randomTexture("dead_fish/fish_dead4")
                .randomTexture("dead_fish/fish_dead5")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.DEAD_FOWL)
                .randomTexture("dead_fowl/fowl")
                .randomTexture("dead_fowl/fowl2")
                .randomTexture("dead_fowl/gooseplains")
                .randomTexture("dead_fowl/gooseplains2")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.DEAD_FROG)
                .randomTexture("dead_frog/toad_dead1")
                .randomTexture("dead_frog/toad_dead2")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.DEAD_HARE)
                .randomTexture("dead_hare/rabbit_dead1")
                .randomTexture("dead_hare/rabbit_dead2")
                .randomTexture("dead_hare/rabbit_dead3")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.DEAD_JUNGLE_TALL_GRASS)
                .texture("dead_jungle_tall_grass/down_side1")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.DEAD_RAT)
                .randomTexture("dead_rat/rat1")
                .randomTexture("dead_rat/rat2")
                .randomTexture("dead_rat/rat3")
                .randomTexture("dead_rat/rat4")
                .randomTexture("dead_rat/rat5")
                .randomTexture("dead_rat/rat6")
                .randomTexture("dead_rat/rat7")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.DEAD_SAVANNA_TALL_GRASS)
                .texture("dead_savanna_tall_grass/dead_savanna_tall_grass")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.DRAGONFLY)
                .texture("web_block/bug_dragonfly")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.FLIES)
                .texture("web_block/bug_flies")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.GARLIC_STRAND)
                .texture("web_block/food_garlic_strand")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.ICICLE)
                .texture("icicle/side")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.IRON_THRONE_RANDOM_BLADES)
                .randomTexture("blades_random/side1")
                .randomTexture("blades_random/side2")
                .randomTexture("blades_random/side3")
                .randomTexture("blades_random/side4")
                .randomTexture("blades_random/side5")
                .randomTexture("blades_random/side6")
                .randomTexture("blades_random/side7")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.JUNGLE_TALL_FERN)
                .texture("jungle_tall_fern/side")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.JUNGLE_TALL_GRASS)
                .texture("jungle_tall_grass/down_side1")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.ROPE_BLOCK_HARNESS)
                .texture("web_block/rope_blockharness")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.SAUSAGES_LEG_OF_HAM)
                .texture("sausages_leg_of_ham/default")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.SAVANNA_TALL_GRASS)
                .texture("savanna_tall_grass/savanna_tall_grass")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.SMOKE)
                .state("smoke/smoke1")
                .state("smoke/smoke2")
                .state("smoke/smoke3")
                .state("smoke/smoke4")
                .state("smoke/smoke5")
                .state("smoke/smoke6")
                .state("smoke/smoke7")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.VERTICAL_CHAIN)
                .texture("web_block/chain_vertical")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.VERTICAL_ROPE)
                .texture("web_block/rope_vertical")
                .build();

        // Crop Blocks
        CropBlockDatagen
                .generateCropBlock(bsmg, ModBlocks.CROP_CARROTS, "crop_carrots")
                .addState("age0", "carrots/carrots_stage_0")
                .addState("age1", "carrots/carrots_stage_1")
                .addState("age2", "carrots/carrots_stage_2")
                .addState("age3", "carrots/carrots_stage_3")
                .build();

        CropBlockDatagen
                .generateCropBlock(bsmg, ModBlocks.CROP_TURNIPS, "crop_turnips")
                .addState("age0", "turnips/turnips_stage_0")
                .addState("age1", "turnips/turnips_stage_1")
                .addState("age2", "turnips/turnips_stage_2")
                .addState("age3", "turnips/turnips_stage_3")
                .isLayerSensitive()
                .build();

        CropBlockDatagen
                .generateCropBlock(bsmg, ModBlocks.CROP_PEAS, "crop_peas")
                .addState("age0", "peas/peas_stage_0")
                .addState("age1", "peas/peas_stage_1")
                .addState("age2", "peas/peas_stage_2")
                .isLayerSensitive()
                .build();

        CropBlockDatagen
                .generateCropBlock(bsmg, ModBlocks.CANDLE_ALTAR, "candle_altar")
                .addStateRandomTextures("lit", "lighting/candle_altar/lit1", "lighting/candle_altar/lit2", "lighting/candle_altar/lit3")
                .addStateRandomTextures("unlit", "lighting/candle_altar/unlit1", "lighting/candle_altar/unlit2", "lighting/candle_altar/unlit3")
                .build();

        CropBlockDatagen
                .generateCropBlock(bsmg, ModBlocks.CROP_WHEAT, "crop_wheat")
                .isLayerSensitive()
                .addState("age0", "wheat/wheat_stage_0")
                .addState("age1", "wheat/wheat_stage_1")
                .addState("age2", "wheat/wheat_stage_2")
                .addState("age3", "wheat/wheat_stage_3")
                .addStateRandomTextures("age4", "wheat/stage4_1", "wheat/stage4_2")
                .addStateRandomTextures("age5", "wheat/stage5_1", "wheat/stage5_2")
                .addStateRandomTextures("age6", "wheat/stage6_1", "wheat/stage6_2")
                .addStateRandomTextures("age7", "wheat/stage7_1", "wheat/stage7_2", "wheat/stage7_3", "wheat/stage7_4")
                .build();

        CropBlockDatagen
                .generateCropBlock(bsmg, ModBlocks.SEAGRASS, "seagrass")
                .addRandomTexture("seagrass/side1")
                .addRandomTexture("seagrass/side2")
                .addRandomTexture("seagrass/side3")
                .addRandomTexture("seagrass/side4")
                .isLayerSensitive()
                .build();

        // Bed Blocks
        BedBlockDatagen.generateBedBlock(bsmg, ModBlocks.ITCHY_STRAW_BED, "itchy_straw_bed")
                .bedType("normal")
                .texture("bed_block/bed_straw_itchy_0")
                .texture("bed_block/bed_straw_itchy_1")
                .texture("bed_block/bed_straw_itchy_2")
                .texture("bed_block/bed_straw_itchy_3")
                .texture("bed_block/bed_straw_itchy_4")
                .texture("bed_block/bed_straw_itchy_5")
                .build();

        BedBlockDatagen.generateBedBlock(bsmg, ModBlocks.HAMMOCK, "hammock")
                .bedType("hammock")
                .texture("bed_block/bed_hammock_0")
                .texture("bed_block/bed_hammock_1")
                .texture("bed_block/bed_hammock_2")
                .texture("bed_block/bed_hammock_3")
                .texture("bed_block/bed_hammock_4")
                .texture("bed_block/bed_hammock_5")
                .build();

        BedBlockDatagen.generateBedBlock(bsmg, ModBlocks.NIGHTS_WATCH_BED, "nights_watch_bed")
                .bedType("normal")
                .texture("bed_block/bed_night_watch_0")
                .texture("bed_block/bed_night_watch_1")
                .texture("bed_block/bed_night_watch_2")
                .texture("bed_block/bed_night_watch_3")
                .texture("bed_block/bed_night_watch_4")
                .texture("bed_block/bed_night_watch_5")
                .build();

        BedBlockDatagen.generateBedBlock(bsmg, ModBlocks.NOBLE_BLUE_BED, "noble_blue_bed")
                .bedType("raised")
                .texture("bed_block/bed_noble_blue_0")
                .texture("bed_block/bed_noble_blue_1")
                .texture("bed_block/bed_noble_blue_2")
                .texture("bed_block/bed_noble_blue_3")
                .texture("bed_block/bed_noble_blue_4")
                .texture("bed_block/bed_noble_blue_5")
                .build();

        BedBlockDatagen.generateBedBlock(bsmg, ModBlocks.NOBLE_RED_BED, "noble_red_bed")
                .bedType("raised")
                .texture("bed_block/bed_noble_red_0")
                .texture("bed_block/bed_noble_red_1")
                .texture("bed_block/bed_noble_red_2")
                .texture("bed_block/bed_noble_red_3")
                .texture("bed_block/bed_noble_red_4")
                .texture("bed_block/bed_noble_red_5")
                .build();

        BedBlockDatagen.generateBedBlock(bsmg, ModBlocks.NORTHERN_BED, "northern_bed")
                .bedType("normal")
                .texture("bed_block/bed_north_0")
                .texture("bed_block/bed_north_1")
                .texture("bed_block/bed_north_2")
                .texture("bed_block/bed_north_3")
                .texture("bed_block/bed_north_4")
                .texture("bed_block/bed_north_5")
                .build();

        BedBlockDatagen.generateBedBlock(bsmg, ModBlocks.PALE_GREEN_BED, "pale_green_bed")
                .bedType("normal")
                .texture("bed_block/bed_patchy_green_0")
                .texture("bed_block/bed_patchy_green_1")
                .texture("bed_block/bed_patchy_green_2")
                .texture("bed_block/bed_patchy_green_3")
                .texture("bed_block/bed_patchy_green_4")
                .texture("bed_block/bed_patchy_green_5")
                .build();

        BedBlockDatagen.generateBedBlock(bsmg, ModBlocks.PALE_RED_BED, "pale_red_bed")
                .bedType("normal")
                .texture("bed_block/bed_patchy_red_0")
                .texture("bed_block/bed_patchy_red_1")
                .texture("bed_block/bed_patchy_red_2")
                .texture("bed_block/bed_patchy_red_3")
                .texture("bed_block/bed_patchy_red_4")
                .texture("bed_block/bed_patchy_red_5")
                .build();

        BedBlockDatagen.generateBedBlock(bsmg, ModBlocks.STRAW_BED, "straw_bed")
                .bedType("normal")
                .texture("bed_block/bed_straw_0")
                .texture("bed_block/bed_straw_1")
                .texture("bed_block/bed_straw_2")
                .texture("bed_block/bed_straw_3")
                .texture("bed_block/bed_straw_4")
                .texture("bed_block/bed_straw_5")
                .build();
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        // Item models are now handled automatically by the block exporters
    }
}
