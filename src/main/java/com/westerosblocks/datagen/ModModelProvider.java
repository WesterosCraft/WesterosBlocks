package com.westerosblocks.datagen;

import com.westerosblocks.block.ModBlocks;

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

                // Slab Blocks
                registerCustomSlabBlock(bsmg, ModBlocks.APPLE_BASKET_SLAB)
                                .textures("crate_block/basket_bottom", "crate_block/basket_apple",
                                                "crate_block/basket_side_slab")
                                .build();

                // Branch Blocks
                registerCustomBranchBlock(bsmg, ModBlocks.OAK_BRANCH).texture("bark/oak/side")
                                .build();
                registerCustomBranchBlock(bsmg, ModBlocks.BIRCH_BRANCH).texture("bark/birch/side").build();
        }

        @Override
        public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        }
}
