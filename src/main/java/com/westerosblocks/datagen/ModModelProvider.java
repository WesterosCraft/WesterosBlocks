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
                registerCustomSolidBlock(bsmg, ModBlocks.APPLE_BASKET).textures(
                                "crate_block/basket_bottom",
                                "crate_block/basket_apple",
                                "crate_block/basket_side").build();
                registerCustomSolidBlock(bsmg, ModBlocks.APPLE_CRATE)
                                .randomTexture(
                                                "crate_block/side_bot1",
                                                "crate_block/crate_top_apples",
                                                "crate_block/side_bot1")
                                .randomTexture(
                                                "crate_block/side_bot2",
                                                "crate_block/crate_top_apples",
                                                "crate_block/side_bot2")
                                .randomTexture(
                                                "crate_block/side_bot3",
                                                "crate_block/crate_top_apples",
                                                "crate_block/side_bot3")
                                .build();
                registerCustomSolidBlock(bsmg, ModBlocks.APPROVAL_UTILITY_BLOCK).texture("utility_block/approved")
                                .build();
                registerCustomSolidBlock(bsmg, ModBlocks.ARBOR_BRICK_ORNATE).texture("ashlar_engraved/arbor/all")
                                .build();
                registerCustomSolidBlock(bsmg, ModBlocks.BENCH_BUTCHER_KNIVES)
                                .textures(
                                                "bench_block/spruce_top",
                                                "bench_block/crafting_table_top",
                                                "bench_block/bench_butcher_knives")
                                .build();
                registerCustomSolidBlock(bsmg, ModBlocks.BENCH_CARPENTRY_HAMMER_SAW).textures(
                                "bench_block/spruce_top",
                                "bench_block/crafting_table_top",
                                "bench_block/bench_carpentry_hammer_saw").build();

                registerCustomSolidBlock(bsmg, ModBlocks.BENCH_DRAWERS)
                                .textures(
                                                "bench_block/spruce_top",
                                                "bench_block/crafting_table_top",
                                                "bench_block/bench_drawers")
                                .build();

                registerCustomSolidBlock(bsmg, ModBlocks.BENCH_KITCHEN_KNIVES)
                                .textures(
                                                "bench_block/spruce_top",
                                                "bench_block/crafting_table_top",
                                                "bench_block/bench_kitchen_knives")
                                .build();

                registerCustomSolidBlock(bsmg, ModBlocks.BENCH_KITCHEN_PANS)
                                .textures(
                                                "bench_block/spruce_top",
                                                "bench_block/crafting_table_top",
                                                "bench_block/bench_kitchen_pans")
                                .build();

                registerCustomSolidBlock(bsmg, ModBlocks.BENCH_MASON_HAMMER_MALLET)
                                .textures(
                                                "bench_block/spruce_top",
                                                "bench_block/crafting_table_top",
                                                "bench_block/bench_mason_hammer_mallet")
                                .build();

                registerCustomSolidBlock(bsmg, ModBlocks.BERRY_BASKET).textures(
                                "crate_block/basket_bottom",
                                "crate_block/basket_berry",
                                "crate_block/basket_side").build();

                registerCustomSolidBlock(bsmg, ModBlocks.BERRY_CRATE)
                                .randomTexture(
                                                "crate_block/side_bot1",
                                                "crate_block/crate_top_berry",
                                                "crate_block/side_bot1")
                                .randomTexture(
                                                "crate_block/side_bot2",
                                                "crate_block/crate_top_berry",
                                                "crate_block/side_bot2")
                                .randomTexture(
                                                "crate_block/side_bot3",
                                                "crate_block/crate_top_berry",
                                                "crate_block/side_bot3")
                                .build();

                registerCustomSolidBlock(bsmg, ModBlocks.APRICOT_BASKET).textures(
                                "crate_block/basket_bottom",
                                "crate_block/basket_apricot",
                                "crate_block/basket_side").build();

                registerCustomSolidBlock(bsmg, ModBlocks.BLACK_BRICK_ENGRAVED).texture("ashlar_engraved/black/all")
                                .build();

                registerCustomSolidBlock(bsmg, ModBlocks.BLUEGREEN_CARVED_SANDSTONE)
                                .texture("bluegreen_carved_sandstone/side")
                                .build();

                registerCustomSolidBlock(bsmg, ModBlocks.BONE_DIRT).texture("dirt/bone").build();

                registerCustomSolidBlock(bsmg, ModBlocks.BOOKSHELF_ABANDONED)
                                .randomTexture(
                                                "bench_block/spruce_top",
                                                "bench_block/spruce_top",
                                                "bookshelf_abandoned/side1")
                                .randomTexture(
                                                "bench_block/spruce_top",
                                                "bench_block/spruce_top",
                                                "bookshelf_abandoned/side2")
                                .build();

                registerCustomSolidBlock(bsmg, ModBlocks.BOOKSHELF_LIBRARY)
                                .randomTexture(
                                                "bench_block/spruce_top",
                                                "bench_block/spruce_top",
                                                "bookshelf_library/side1")
                                .randomTexture(
                                                "bench_block/spruce_top",
                                                "bench_block/spruce_top",
                                                "bookshelf_library/side2")
                                .randomTexture(
                                                "bench_block/spruce_top",
                                                "bench_block/spruce_top",
                                                "bookshelf_library/side3")
                                .build();

                registerCustomSolidBlock(bsmg, ModBlocks.BOOKSHELF_MAESTER)
                                .randomTexture(
                                                "bench_block/spruce_top",
                                                "bench_block/spruce_top",
                                                "bookshelf_maester/side1")
                                .randomTexture(
                                                "bench_block/spruce_top",
                                                "bench_block/spruce_top",
                                                "bookshelf_maester/side2")
                                .randomTexture(
                                                "bench_block/spruce_top",
                                                "bench_block/spruce_top",
                                                "bookshelf_maester/side3")
                                .randomTexture(
                                                "bench_block/spruce_top",
                                                "bench_block/spruce_top",
                                                "bookshelf_maester/side4")
                                .randomTexture(
                                                "bench_block/spruce_top",
                                                "bench_block/spruce_top",
                                                "bookshelf_maester/side5")
                                .randomTexture(
                                                "bench_block/spruce_top",
                                                "bench_block/spruce_top",
                                                "bookshelf_maester/side6")
                                .build();

                registerCustomSolidBlock(bsmg, ModBlocks.BROKEN_CABINET).textures(
                                "cabinet/top_bottom",
                                "cabinet/top_bottom",
                                "bench_block/cabinet_broken").build();

                registerCustomSolidBlock(bsmg, ModBlocks.BROWN_GREY_BRICK_ENGRAVED)
                                .texture("ashlar_engraved/brown_grey/all")
                                .build();

                registerCustomSolidBlock(bsmg, ModBlocks.CABINET_DRAWER)
                                .randomTexture(
                                                "cabinet/top_bottom",
                                                "cabinet/top_bottom",
                                                "cabinet/drawer/side1")
                                .randomTexture(
                                                "cabinet/top_bottom",
                                                "cabinet/top_bottom",
                                                "cabinet/drawer/side2")
                                .build();

                registerCustomSolidBlock(bsmg, ModBlocks.CAGE).textures(
                                "cage/bottom",
                                "cage/top",
                                "cage/side").build();

                registerCustomSolidBlock(bsmg, ModBlocks.CARROT_BASKET).textures(
                                "crate_block/basket_bottom",
                                "crate_block/basket_carrot",
                                "crate_block/basket_side").build();

                registerCustomSolidBlock(bsmg, ModBlocks.CARROT_CRATE)
                                .randomTexture(
                                                "crate_block/side_bot1",
                                                "crate_block/crate_top_carrot",
                                                "crate_block/side_bot1")
                                .randomTexture(
                                                "crate_block/side_bot2",
                                                "crate_block/crate_top_carrot",
                                                "crate_block/side_bot2")
                                .randomTexture(
                                                "crate_block/side_bot3",
                                                "crate_block/crate_top_carrot",
                                                "crate_block/side_bot3")
                                .build();

                registerCustomSolidBlock(bsmg, ModBlocks.CLOSED_BASKET).textures(
                                "crate_block/basket_bottom",
                                "crate_block/basket_top_closed",
                                "crate_block/basket_side").build();

                registerCustomSolidBlock(bsmg, ModBlocks.CLOSED_CABINET).textures(
                                "cabinet/top_bottom",
                                "cabinet/top_bottom",
                                "cabinet/closed/side").build();

                registerCustomSolidBlock(bsmg, ModBlocks.COARSE_DARK_RED_CARVED_SANDSTONE)
                                .texture("ashlar_engraved/pale_dark_red/all")
                                .build();

                registerCustomSolidBlock(bsmg, ModBlocks.COARSE_RED_CARVED_SANDSTONE)
                                .texture("ashlar_engraved/pale_red/all")
                                .build();

                registerCustomSolidBlock(bsmg, ModBlocks.COBBLE_KEYSTONE).textures(
                                "cobblestone/grey/keystone/top_bottom",
                                "cobblestone/grey/keystone/top_bottom",
                                "cobblestone/grey/keystone/side").build();

                registerCustomSolidBlock(bsmg, ModBlocks.COLOURED_SEPT_WINDOW)
                                .texture("glass/sept/all")
                                .build();

                registerCustomSolidBlock(bsmg, ModBlocks.CRATE)
                                .randomTexture(
                                                "crate_block/side_bot1",
                                                "crate_block/crate_side_crossbar_right",
                                                "crate_block/side_bot1")
                                .randomTexture(
                                                "crate_block/side_bot2",
                                                "crate_block/crate_side_crossbar_right",
                                                "crate_block/side_bot2")
                                .randomTexture(
                                                "crate_block/side_bot3",
                                                "crate_block/crate_side_crossbar_right",
                                                "crate_block/side_bot3")
                                .build();

                registerCustomSolidBlock(bsmg, ModBlocks.CRATE2)
                                .randomTexture(
                                                "crate_block/side_bot1",
                                                "crate_block/crate_side_crossbar_left",
                                                "crate_block/side_bot1")
                                .randomTexture(
                                                "crate_block/side_bot2",
                                                "crate_block/crate_side_crossbar_left",
                                                "crate_block/side_bot2")
                                .randomTexture(
                                                "crate_block/side_bot3",
                                                "crate_block/crate_side_crossbar_left",
                                                "crate_block/side_bot3")
                                .build();

                registerCustomSolidBlock(bsmg, ModBlocks.CRATE3)
                                .randomTexture(
                                                "crate_block/side_bot1",
                                                "crate_block/crate_side_crossbar_crossed",
                                                "crate_block/side_bot1")
                                .randomTexture(
                                                "crate_block/side_bot2",
                                                "crate_block/crate_side_crossbar_crossed",
                                                "crate_block/side_bot2")
                                .randomTexture(
                                                "crate_block/side_bot3",
                                                "crate_block/crate_side_crossbar_crossed",
                                                "crate_block/side_bot3")
                                .build();

                registerCustomSolidBlock(bsmg, ModBlocks.DARK_GREY_BRICK_ENGRAVED)
                                .texture("ashlar_engraved/dark_grey/all")
                                .build();

                registerCustomSolidBlock(bsmg, ModBlocks.DATE_BASKET).textures(
                                "crate_block/basket_bottom",
                                "crate_block/basket_dates",
                                "crate_block/basket_side").build();

                registerCustomSolidBlock(bsmg, ModBlocks.DATES).textures(
                                "dates/bottom",
                                "dates/top",
                                "dates/side").build();

                registerCustomSolidBlock(bsmg, ModBlocks.DESERT_SANDSTONE_ENGRAVED)
                                .texture("ashlar_engraved/sandstone/all")
                                .build();

                registerCustomSolidBlock(bsmg, ModBlocks.DOMESTIC_UTILITY_BLOCK)
                                .texture("utility_block/domestic")
                                .build();

                registerCustomSolidBlock(bsmg, ModBlocks.DONE_UTILITY_BLOCK)
                                .texture("utility_block/done")
                                .build();

                registerCustomSolidBlock(bsmg, ModBlocks.DRAGON_CARVING)
                                .randomTexture("dragon_carving/side1")
                                .randomTexture("dragon_carving/side2")
                                .randomTexture("dragon_carving/side3")
                                .build();

                registerCustomSolidBlock(bsmg, ModBlocks.EMPTY_BARREL)
                                .randomTexture(
                                                "barrel_closed/barrel_top_closed",
                                                "crate_block/barrel_top_empty",
                                                "barrel_sides/side0")
                                .randomTexture(
                                                "barrel_closed/barrel_top_closed",
                                                "crate_block/barrel_top_empty",
                                                "barrel_sides/side1")
                                .build();

                registerCustomSolidBlock(bsmg, ModBlocks.EMPTY_CABINET).textures(
                                "cabinet/top_bottom",
                                "cabinet/top_bottom",
                                "cabinet/empty/side").build();

                registerCustomSolidBlock(bsmg, ModBlocks.FAITH_CARVED_ARBOR_BRICK)
                                .texture("ashlar_third/arbor/faith_carved")
                                .build();

                registerCustomSolidBlock(bsmg, ModBlocks.FAITH_CARVED_BLACK_BRICK)
                                .texture("ashlar_quarter/black/faith_carved")
                                .build();

                registerCustomSolidBlock(bsmg, ModBlocks.FAITH_CARVED_BROWN_GREY_BRICK)
                                .texture("ashlar_quarter/brown_grey/faith_carved")
                                .build();

                registerCustomSolidBlock(bsmg, ModBlocks.FAITH_CARVED_COARSE_RED_BRICK)
                                .texture("ashlar_third/pale_red/faith_carved")
                                .build();

                registerCustomSolidBlock(bsmg, ModBlocks.FAITH_CARVED_DARK_GREY_BRICK)
                                .texture("ashlar_quarter/dark_grey/faith_carved")
                                .build();

                registerCustomSolidBlock(bsmg, ModBlocks.FAITH_CARVED_DUN_BRICK)
                                .texture("ashlar_third/dun/faith_carved")
                                .build();

                registerCustomSolidBlock(bsmg, ModBlocks.FAITH_CARVED_GREY_BRICK)
                                .texture("ashlar_third/grey/faith_carved")
                                .build();

                registerCustomSolidBlock(bsmg, ModBlocks.FAITH_CARVED_OLDTOWN_BRICK)
                                .texture("ashlar_quarter_rounded/light_oldtown/faith_carved")
                                .build();

                registerCustomSolidBlock(bsmg, ModBlocks.FAITH_CARVED_PINK_SANDSTONE)
                                .texture("ashlar_third/sandy_pink/faith_carved")
                                .build();

                registerCustomSolidBlock(bsmg, ModBlocks.FAITH_CARVED_REACH_BRICK)
                                .texture("ashlar_quarter_rounded/reach/faith_carved")
                                .build();

                registerCustomSolidBlock(bsmg, ModBlocks.FAITH_CARVED_SMALL_STONE_BRICK)
                                .texture("ashlar_quarter/green_grey/faith_carved")
                                .build();

                registerCustomSolidBlock(bsmg, ModBlocks.FAITH_CARVED_STONE_BRICK)
                                .texture("ashlar_half/white/faith_carved")
                                .build();

                registerCustomSolidBlock(bsmg, ModBlocks.FAITH_CARVED_STORMLANDS_BRICK)
                                .texture("ashlar_quarter/stormlands/faith_carved")
                                .build();

                registerCustomSolidBlock(bsmg, ModBlocks.FAITH_CARVED_WESTERLANDS_BRICK)
                                .texture("ashlar_quarter/westerlands/faith_carved")
                                .build();

                registerCustomSolidBlock(bsmg, ModBlocks.FISH_BARREL)
                                .randomTexture(
                                                "barrel_closed/barrel_top_closed",
                                                "crate_block/barrel_top_fish",
                                                "barrel_sides/side0")
                                .randomTexture(
                                                "barrel_closed/barrel_top_closed",
                                                "crate_block/barrel_top_fish",
                                                "barrel_sides/side1")
                                .build();

                registerCustomSolidBlock(bsmg, ModBlocks.FISH_BASKET).textures(
                                "crate_block/basket_bottom",
                                "crate_block/basket_fish",
                                "crate_block/basket_side").build();

                registerCustomSolidBlock(bsmg, ModBlocks.FISH_TRAP)
                                .randomTexture("fish_trap/side1")
                                .randomTexture("fish_trap/side2")
                                .randomTexture("fish_trap/side3")
                                .randomTexture("fish_trap/side4")
                                .randomTexture("fish_trap/side5")
                                .build();

                registerCustomSolidBlock(bsmg, ModBlocks.FLAGSTONE)
                                .texture("stone_block/flagstone")
                                .build();

                registerCustomSolidBlock(bsmg, ModBlocks.FULL_CABINET).textures(
                                "cabinet/top_bottom",
                                "cabinet/top_bottom",
                                "cabinet/full/side").build();

                registerCustomSolidBlock(bsmg, ModBlocks.GLOWING_EMBERS)
                                .texture("lighting/coals_glowing")
                                .build();

                registerCustomSolidBlock(bsmg, ModBlocks.GRAIN_BASKET).textures(
                                "crate_block/basket_bottom",
                                "crate_block/basket_grain",
                                "crate_block/basket_side").build();

                registerCustomSolidBlock(bsmg, ModBlocks.GRAIN_CRATE)
                                .randomTexture(
                                                "crate_block/side_bot1",
                                                "crate_block/crate_top_grain",
                                                "crate_block/side_bot1")
                                .randomTexture(
                                                "crate_block/side_bot2",
                                                "crate_block/crate_top_grain",
                                                "crate_block/side_bot2")
                                .randomTexture(
                                                "crate_block/side_bot3",
                                                "crate_block/crate_top_grain",
                                                "crate_block/side_bot3")
                                .build();

                registerCustomSolidBlock(bsmg, ModBlocks.GREEN_GREY_BRICK_ENGRAVED)
                                .texture("ashlar_engraved/green_grey/all")
                                .build();

                registerCustomSolidBlock(bsmg, ModBlocks.GREY_BRICK_ENGRAVED)
                                .texture("ashlar_engraved/grey/all")
                                .build();

                registerCustomSolidBlock(bsmg, ModBlocks.GREY_KEYSTONE)
                                .texture("stone_block/keystone_grey")
                                .build();

                // Log Blocks
                registerCustomLogBlock(bsmg, ModBlocks.ARCHERY_TARGET)
                                .textures("archery_target/side", "archery_target/front").build();

                // Slab Blocks
                registerCustomSlabBlock(bsmg, ModBlocks.APPLE_BASKET_SLAB)
                                .textures(
                                                "crate_block/basket_bottom",
                                                "crate_block/basket_apple",
                                                "crate_block/basket_side_slab")
                                .build();
        }

        @Override
        public void generateItemModels(ItemModelGenerator itemModelGenerator) {

        }

}
