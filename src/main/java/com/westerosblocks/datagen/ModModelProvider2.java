package com.westerosblocks.datagen;

import com.westerosblocks.block.ModBlocks2;
import com.westerosblocks.datagen.models.*;
import net.minecraft.data.client.*;

public class ModModelProvider2 {
        static ArrowSlitBlockExport arrowSlitExport = new ArrowSlitBlockExport();
        static TableBlockExport tableExport = new TableBlockExport();
        static SandBlockExport sandExport = new SandBlockExport();
        static DoorBlockExport doorExporter = new DoorBlockExport();
        static ChairBlockExport chairExporter = new ChairBlockExport();
        static TrapDoorBlockExport trapDoorExporter = new TrapDoorBlockExport();
        static WaySignBlockExport waySignExporter = new WaySignBlockExport();
        static LogBlockExport logExporter = new LogBlockExport();
        static TorchBlockExport torchExporter = new TorchBlockExport();
        static FanBlockExport fanExporter = new FanBlockExport();
        static VinesBlockExport vinesExporter = new VinesBlockExport();
        static HalfDoorBlockExport halfDoorExporter = new HalfDoorBlockExport();
        static CrossBlockExport crossExporter = new CrossBlockExport();
        static WebBlockExport webExporter = new WebBlockExport();
        static FlowerPotBlockExport flowerPotExporter = new FlowerPotBlockExport();
        static CropBlockExport2 cropExporter = new CropBlockExport2();
        static LadderBlockExport ladderExporter = new LadderBlockExport();
        static StandalonePaneBlockExport paneExporter = new StandalonePaneBlockExport();
        static RailBlockExport2 railExporter = new RailBlockExport2();
        static FireBlockExport2 fireExporter = new FireBlockExport2();
        static SolidBlockExport2 solidExporter = new SolidBlockExport2();

        public static void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
                // Arrow Slits
                arrowSlitExport.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.ARBOR_BRICK_ARROW_SLIT,
                                "westerosblocks:block/ashlar_third/arbor/all");
                arrowSlitExport.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.BLACK_GRANITE_ARROW_SLIT,
                                "westerosblocks:block/ashlar_third/black/all");

                // Tables
                tableExport.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.OAK_TABLE,
                                "westerosblocks:block/wood/oak/all");
                tableExport.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.BIRCH_TABLE,
                                "westerosblocks:block/wood/birch/all");
                tableExport.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.SPRUCE_TABLE,
                                "westerosblocks:block/wood/spruce/all");

                // Chairs
                chairExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.OAK_CHAIR,
                                "westerosblocks:block/wood/oak/all");
                chairExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.BIRCH_CHAIR,
                                "westerosblocks:block/wood/birch/all");
                chairExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.SPRUCE_CHAIR,
                                "westerosblocks:block/wood/spruce/all");

                // way signs
                waySignExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.OAK_WAY_SIGN,
                                "westerosblocks:block/wood/oak/all");

                // trap doors
                trapDoorExporter.generateBlockStateModels(blockStateModelGenerator,
                                ModBlocks2.KINGS_LANDING_SEWER_MANHOLE,
                                "westerosblocks:block/trapdoor_block/kings_landing_manhole");
                trapDoorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.OLDTOWN_SEWER_MANHOLE,
                                "westerosblocks:block/trapdoor_block/oldtown_manhole");
                trapDoorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.SEWER_MANHOLE,
                                "westerosblocks:block/trapdoor_block/manhole");
                trapDoorExporter.generateBlockStateModels(blockStateModelGenerator,
                                ModBlocks2.WHITE_HARBOR_SEWER_MANHOLE,
                                "westerosblocks:block/trapdoor_block/white_harbor_manhole");

                // log blocks
                logExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.OAK_LOG_CHAIN,
                                "bark/oak/top", "bark/oak/top", "bark/oak/chain");
                logExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.JUNGLE_LOG_CHAIN,
                                "bark/jungle/top", "bark/jungle/top", "bark/jungle/chain");
                logExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.JUNGLE_LOG_ROPE,
                                "bark/jungle/top", "bark/jungle/top", "bark/jungle/rope");
                logExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.ARCHERY_TARGET,
                                "archery_target/front", "archery_target/front", "archery_target/side");
                logExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.CLOSED_BARREL,
                                "barrel_closed/barrel_top_closed", "barrel_closed/barrel_top_closed",
                                "barrel_sides/side1");
                logExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.FIREWOOD,
                                "firewood/top", "firewood/top", "firewood/side");
                logExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.MARBLE_PILLAR,
                                "marble/quartz/column_topbottom", "marble/quartz/column_topbottom",
                                "marble/quartz/column_side");
                logExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.MARBLE_PILLAR_VERTICAL_CTM,
                                "marble/quartz/column_topbottom", "marble/quartz/column_topbottom",
                                "marble/quartz/column_side_ctm");
                logExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.MOSSY_OAK_LOG,
                                "bark/oak/mossy/bottom", "bark/oak/mossy/top", "bark/oak/mossy/side");
                logExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.MOSSY_BIRCH_LOG,
                                "bark/birch/mossy/bottom", "bark/birch/mossy/top", "bark/birch/mossy/side");
                logExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.MOSSY_SPRUCE_LOG,
                                "bark/spruce/mossy/bottom", "bark/spruce/mossy/top", "bark/spruce/mossy/side");
                logExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.MOSSY_JUNGLE_LOG,
                                "bark/jungle/mossy/bottom", "bark/jungle/mossy/top", "bark/jungle/mossy/side");
                logExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.OAK_LOG_ROPE,
                                "bark/oak/top", "bark/oak/top", "bark/oak/rope");
                logExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.PALM_TREE_LOG,
                                "bark/palm/top", "bark/palm/top", "bark/palm/side");
                logExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.SANDSTONE_PILLAR,
                                "ashlar_third/sandstone/column_top", "ashlar_third/sandstone/column_top",
                                "ashlar_third/sandstone/column_side");
                logExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.SPRUCE_LOG_CHAIN,
                                "bark/spruce/top", "bark/spruce/top", "bark/spruce/chain");
                logExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.SPRUCE_LOG_ROPE,
                                "bark/spruce/top", "bark/spruce/top", "bark/spruce/rope");
                logExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.STACKED_BONES,
                                "stacked_bones/bone_stacked_front", "stacked_bones/bone_stacked_front",
                                "stacked_bones/bone_stacked_side");
                logExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.WEIRWOOD_FACE_0,
                                "bark/weirwood/face_0", "bark/weirwood/face_0", "bark/weirwood/side");
                logExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.WEIRWOOD_FACE_1,
                                "bark/weirwood/face_1", "bark/weirwood/face_1", "bark/weirwood/side");
                logExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.WEIRWOOD_FACE_2,
                                "bark/weirwood/face_2", "bark/weirwood/face_2", "bark/weirwood/side");
                logExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.WEIRWOOD_FACE_3,
                                "bark/weirwood/face_3", "bark/weirwood/face_3", "bark/weirwood/side");
                logExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.WEIRWOOD_FACE_4,
                                "bark/weirwood/face_4", "bark/weirwood/face_4", "bark/weirwood/side");
                logExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.WEIRWOOD_FACE_5,
                                "bark/weirwood/face_5", "bark/weirwood/face_5", "bark/weirwood/side");
                logExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.WEIRWOOD_FACE_6,
                                "bark/weirwood/face_6", "bark/weirwood/face_6", "bark/weirwood/side");
                logExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.WEIRWOOD_FACE_7,
                                "bark/weirwood/face_7", "bark/weirwood/face_7", "bark/weirwood/side");
                logExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.WEIRWOOD_FACE_8,
                                "bark/weirwood/face_8", "bark/weirwood/face_8", "bark/weirwood/side");
                logExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.WEIRWOOD_SCARS,
                                "bark/weirwood/scars", "bark/weirwood/scars", "bark/weirwood/side");

                // Door blocks
                doorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.WHITE_WOOD_DOOR,
                                "wood/white/door_top", "wood/white/door_bottom");
                doorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.LOCKED_WHITE_WOOD_DOOR,
                                "wood/white/door_locked_top", "wood/white/door_locked_bottom");
                doorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.NORTHERN_WOOD_DOOR,
                                "wood/northern/door_top", "wood/northern/door_bottom");
                doorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.SPRUCE_DOOR,
                                "wood/spruce/door_top", "wood/spruce/door_bottom");
                doorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.OAK_DOOR,
                                "wood/oak/door_top", "wood/oak/door_bottom");
                doorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.BIRCH_DOOR,
                                "wood/birch/door_top", "wood/birch/door_bottom");
                doorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.EYRIE_WEIRWOOD_DOOR,
                                "door_block/door_weirwood_top", "door_block/door_weirwood_bottom");
                doorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.GREY_WOOD_DOOR,
                                "wood/grey/door_top", "wood/grey/door_bottom");
                doorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.JUNGLE_DOOR,
                                "wood/jungle/door_top", "wood/jungle/door_bottom");
                doorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.RED_KEEP_SECRET_DOOR,
                                "ashlar_third/pale_red/all_noctm", "ashlar_third/pale_red/all_noctm");
                doorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.HARRENHAL_SECRET_DOOR,
                                "ashlar_third/black/all_noctm", "ashlar_third/black/all_noctm");
                doorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.LOCKED_BIRCH_DOOR,
                                "wood/birch/door_locked_top", "wood/birch/door_locked_bottom");
                doorExporter.generateBlockStateModels(blockStateModelGenerator,
                                ModBlocks2.LOCKED_DARK_NORTHERN_WOOD_DOOR,
                                "wood/northern/door_locked_top", "wood/northern/door_locked_bottom");
                doorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.LOCKED_GREY_WOOD_DOOR,
                                "wood/grey/door_locked_top", "wood/grey/door_locked_bottom");
                doorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.LOCKED_SPRUCE_DOOR,
                                "wood/spruce/door_locked_top", "wood/spruce/door_locked_bottom");
                doorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.LOCKED_JUNGLE_DOOR,
                                "wood/jungle/door_locked_top", "wood/jungle/door_locked_bottom");
                doorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.LOCKED_OAK_DOOR,
                                "wood/oak/door_locked_top", "wood/oak/door_locked_bottom");

                // Torch Blocks
                torchExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.TORCH, "lighting/torch");
                torchExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.TORCH_UNLIT,
                                "lighting/torch_unlit");
                torchExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.CANDLE, "lighting/candle");
                torchExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.CANDLE_UNLIT,
                                "lighting/candle_unlit");

                // Ladder Blocks
                ladderExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.IRON_RUNGS,
                                "iron_rungs/ladder");
                ladderExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.IRON_RUNGS_BROKEN,
                                "iron_rungs/ladder2");
                ladderExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.ROPE_LADDER,
                                "rope_ladder/side");
                ladderExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.VINE_JASMINE,
                                "jasmine_vines/side1");
                ladderExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.WINTERFELL_STONE_LADDER,
                                "winterfell_stone_ladder/side1");
                ladderExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.WOOD_LADDER,
                                "wood_ladder/side");

                // Pane Blocks
                paneExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.VERTICAL_NET,
                                "vertical_net/vertical_net1");
                paneExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.DORNE_CARVED_STONE_WINDOW,
                                "pane_block/moorish_stone_window_pane");
                paneExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.DORNE_CARVED_WOODEN_WINDOW,
                                "pane_block/moorish_wood_window_pane");
                paneExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.IRON_BARS,
                                "bars_iron_block/iron_bars");
                paneExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.OXIDIZED_IRON_CROSSBAR,
                                "bars_iron_block/bars_iron_oxidized_crossbars");
                paneExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.IRON_CROSSBAR,
                                "bars_iron_block/bars_iron_crossbars");

                // Rail Blocks
                railExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.FANCY_BLUE_CARPET, "carpet/fancy_blue_carpet");
                railExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.FANCY_RED_CARPET, "carpet/fancy_red_carpet");
                railExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.HORIZONTAL_CHAIN, "rail_block/chain");
                railExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.HORIZONTAL_NET, "rail_block/net_large");
                railExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.HORIZONTAL_ROPE, "rail_block/rope");
                railExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.PACKED_SNOW, "rail_block/packed_snow");

                // Fire Blocks
                fireExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.SAFE_FIRE, "safe_fire/fire_layer_0", "safe_fire/fire_layer_1");
                fireExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.WILDFIRE, "wildfire/wildfire_layer_0", "wildfire/wildfire_layer_1");

                paneExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.OXIDIZED_IRON_BARS,
                                "bars_iron_block/bars_iron_oxidized");

                // Solid Blocks
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.SIX_SIDED_BIRCH, "bark/birch/side");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.APPLE_BASKET, 
                    "crate_block/basket_bottom", "crate_block/basket_apple", "crate_block/basket_side");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.APPLE_CRATE, 
                    "crate_block/side_bot1", "crate_block/crate_top_apples", "crate_block/side_bot1");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.APPROVAL_UTILITY_BLOCK, "utility_block/approved");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.APRICOT_BASKET, 
                    "crate_block/basket_bottom", "crate_block/basket_apricot", "crate_block/basket_side");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.SIX_SIDED_JUNGLE, "bark/jungle/side");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.SIX_SIDED_OAK, "bark/oak/side");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.SIX_SIDED_SPRUCE, "bark/spruce/side");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.SIX_SIDED_STONE_SLAB, "ashlar_half/white/tile");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.ARBOR_BRICK_ORNATE, "ashlar_engraved/arbor/all");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.BENCH_BUTCHER_KNIVES, "bench_block/spruce_top", "bench_block/crafting_table_top", "bench_block/bench_butcher_knives");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.BENCH_CARPENTRY_HAMMER_SAW, "bench_block/spruce_top", "bench_block/crafting_table_top", "bench_block/bench_carpentry_hammer_saw");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.BENCH_DRAWERS, "bench_block/spruce_top", "bench_block/crafting_table_top", "bench_block/bench_drawers");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.BENCH_KITCHEN_KNIVES, "bench_block/spruce_top", "bench_block/crafting_table_top", "bench_block/bench_kitchen_knives");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.BENCH_KITCHEN_PANS, "bench_block/spruce_top", "bench_block/crafting_table_top", "bench_block/bench_kitchen_pans");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.BENCH_MASON_HAMMER_MALLET, "bench_block/spruce_top", "bench_block/crafting_table_top", "bench_block/bench_mason_hammer_mallet");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.BERRY_BASKET, "crate_block/basket_bottom", "crate_block/basket_berry", "crate_block/basket_side");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.BERRY_CRATE, "crate_block/side_bot1", "crate_block/crate_top_berry", "crate_block/side_bot1");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.BLACK_BRICK_ENGRAVED, "ashlar_engraved/black/all");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.BLUEGREEN_CARVED_SANDSTONE, "bluegreen_carved_sandstone/side");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.BONE_DIRT, "dirt/bone");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.BOOKSHELF_ABANDONED, "bench_block/spruce_top", "bench_block/spruce_top", "bookshelf_abandoned/side1");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.BOOKSHELF_LIBRARY, "bench_block/spruce_top", "bench_block/spruce_top", "bookshelf_library/side1");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.BROKEN_CABINET, "cabinet/top_bottom", "cabinet/top_bottom", "bench_block/cabinet_broken");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.BOOKSHELF_MAESTER, "bench_block/spruce_top", "bench_block/spruce_top", "bookshelf_maester/side1");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.BROWN_GREY_BRICK_ENGRAVED, "ashlar_engraved/brown_grey/all");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.CABINET_DRAWER, "cabinet/top_bottom", "cabinet/top_bottom", "cabinet/drawer/side1");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.CAGE, "cage/bottom", "cage/top", "cage/side");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.CARROT_BASKET, "crate_block/basket_bottom", "crate_block/basket_carrot", "crate_block/basket_side");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.CARROT_CRATE, "crate_block/side_bot1", "crate_block/crate_top_carrot", "crate_block/side_bot1");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.CLOSED_BASKET, "crate_block/basket_bottom", "crate_block/basket_top_closed", "crate_block/basket_side");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.CLOSED_CABINET, "cabinet/top_bottom", "cabinet/top_bottom", "cabinet/closed/side");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.COARSE_DARK_RED_CARVED_SANDSTONE, "ashlar_engraved/pale_dark_red/all");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.COARSE_RED_CARVED_SANDSTONE, "ashlar_engraved/pale_red/all");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.COBBLE_KEYSTONE, "cobblestone/grey/keystone/top_bottom", "cobblestone/grey/keystone/top_bottom", "cobblestone/grey/keystone/side");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.COLOURED_SEPT_WINDOW, "glass/sept/all");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.CRATE, "crate_block/side_bot1", "crate_block/crate_side_crossbar_right", "crate_block/side_bot1");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.CRATE2, "crate_block/side_bot1", "crate_block/crate_side_crossbar_left", "crate_block/side_bot1");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.CRATE3, "crate_block/side_bot1", "crate_block/crate_side_crossbar_crossed", "crate_block/side_bot1");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.DARK_GREY_BRICK_ENGRAVED, "ashlar_engraved/dark_grey/all");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.DATE_BASKET, "crate_block/basket_bottom", "crate_block/basket_dates", "crate_block/basket_side");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.DATES, "dates/bottom", "dates/top", "dates/side");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.DESERT_SANDSTONE_ENGRAVED, "ashlar_engraved/sandstone/all");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.DOMESTIC_UTILITY_BLOCK, "utility_block/domestic");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.DONE_UTILITY_BLOCK, "utility_block/done");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.DRAGON_CARVING, "dragon_carving/side1");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.EMPTY_BARREL, "barrel_closed/barrel_top_closed", "crate_block/barrel_top_empty", "barrel_sides/side0");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.EMPTY_CABINET, "cabinet/top_bottom", "cabinet/top_bottom", "cabinet/empty/side");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.FAITH_CARVED_ARBOR_BRICK, "ashlar_third/arbor/faith_carved");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.FAITH_CARVED_BLACK_BRICK, "ashlar_quarter/black/faith_carved");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.FAITH_CARVED_BROWN_GREY_BRICK, "ashlar_quarter/brown_grey/faith_carved");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.FAITH_CARVED_COARSE_RED_BRICK, "ashlar_third/pale_red/faith_carved");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.FAITH_CARVED_DARK_GREY_BRICK, "ashlar_quarter/dark_grey/faith_carved");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.FAITH_CARVED_DUN_BRICK, "ashlar_third/dun/faith_carved");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.FAITH_CARVED_GREY_BRICK, "ashlar_third/grey/faith_carved");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.FAITH_CARVED_OLDTOWN_BRICK, "ashlar_quarter_rounded/light_oldtown/faith_carved");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.FAITH_CARVED_PINK_SANDSTONE, "ashlar_third/sandy_pink/faith_carved");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.FAITH_CARVED_REACH_BRICK, "ashlar_quarter_rounded/reach/faith_carved");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.FAITH_CARVED_SMALL_STONE_BRICK, "ashlar_quarter/green_grey/faith_carved");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.FAITH_CARVED_STONE_BRICK, "ashlar_half/white/faith_carved");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.FAITH_CARVED_STORMLANDS_BRICK, "ashlar_quarter/stormlands/faith_carved");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.FAITH_CARVED_WESTERLANDS_BRICK, "ashlar_quarter/westerlands/faith_carved");

                // Additional Solid Blocks from JSON definitions - Part 4
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.FISH_BARREL, "barrel_closed/barrel_top_closed", "crate_block/barrel_top_fish", "barrel_sides/side0");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.FISH_BASKET, "crate_block/basket_bottom", "crate_block/basket_fish", "crate_block/basket_side");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.FISH_TRAP, "fish_trap/side1");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.FLAGSTONE, "stone_block/flagstone");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.FULL_CABINET, "cabinet/top_bottom", "cabinet/top_bottom", "cabinet/full/side");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.GLOWING_EMBERS, "lighting/coals_glowing");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.GRAIN_BASKET, "crate_block/basket_bottom", "crate_block/basket_grain", "crate_block/basket_side");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.GRAIN_CRATE, "crate_block/side_bot1", "crate_block/crate_top_grain", "crate_block/side_bot1");

                // Additional Solid Blocks from JSON definitions - Part 5
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.GREEN_GREY_BRICK_ENGRAVED, "ashlar_engraved/green_grey/all");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.GREY_BRICK_ENGRAVED, "ashlar_engraved/grey/all");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.GREY_KEYSTONE, "stone_block/keystone_grey");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.HIGH_CLASS_UTILITY_BLOCK, "utility_block/highclass");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.HOP_BASKET, "crate_block/basket_bottom", "crate_block/basket_hop", "crate_block/basket_side");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.HOP_CRATE, "crate_block/crate_side_crossbar_right", "crate_block/crate_hops", "crate_block/crate_side_crossbar_left");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.HOUSE_COUNT_UTILITY_BLOCK, "utility_block/housecount");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.INDUSTRY_UTILITY_BLOCK, "utility_block/industry");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.IRON_CRATE, "crate_block/side_bot1", "crate_block/crate_top_iron", "crate_block/side_bot1");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.KL_DUN_CARVED_BRICK, "ashlar_engraved/dun/all");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.LANNISPORT_KEYSTONE_ORANGE_PLASTER, "fieldstone/westerlands/all", "fieldstone/westerlands/all", "plaster/smooth/lannisport_orange/all");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.LANNISPORT_KEYSTONE_YELLOW_PLASTER, "fieldstone/westerlands/all", "fieldstone/westerlands/all", "plaster/smooth/lannisport_yellow/all");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.LARGE_CLAY_POT_SOLID, "wood/oak/all", "clay_pot/solid", "clay_pot/solid");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.LAVENDER_BASKET, "crate_block/basket_bottom", "crate_block/basket_lavender", "crate_block/basket_side");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.LAVENDER_CRATE, "crate_block/crate_side_crossbar_right", "crate_block/crate_lavender", "crate_block/crate_side_crossbar_crossed");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.LEMON_BASKET, "crate_block/basket_bottom", "crate_block/basket_lemons", "crate_block/basket_side");

                // Additional Solid Blocks from JSON definitions - Part 6
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.LIGHT_GREY_BRICK_ENGRAVED, "ashlar_engraved/light_grey/all");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.LIGHT_GREY_STONE_WHITE_PLASTER, "fieldstone/grey/all", "fieldstone/grey/all", "plaster/smooth/white/all");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.LIGHT_OLDTOWN_BRICK_ENGRAVED, "ashlar_engraved/light_oldtown/all");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.LIME_BASKET, "crate_block/basket_bottom", "crate_block/basket_limes", "crate_block/basket_side");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.LOW_CLASS_UTILITY_BLOCK, "utility_block/lowclass");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.MIDDLE_CLASS_UTILITY_BLOCK, "utility_block/middleclass");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.MIRROR_BLOCK, "mirror/top_bottom", "mirror/top_bottom", "mirror/side");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.MONOCHROME_DARK_SANDSTONE_ENGRAVED, "ashlar_engraved/light_brown/all");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.MONOCHROME_SANDSTONE_ENGRAVED, "ashlar_engraved/westerlands/all");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.NETHER_BRICK_KEYSTONE, "ashlar_half/black/embellished");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.NORTHERN_CARVINGS, "wood/spruce/carving/top_bottom", "wood/spruce/carving/top_bottom", "wood/spruce/carving/side");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.NOTE_UTILITY_BLOCK, "utility_block/note");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.OLIVE_BASKET, "crate_block/basket_bottom", "crate_block/basket_olives", "crate_block/basket_side");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.OPEN_BASKET, "crate_block/basket_bottom", "crate_block/basket_top", "crate_block/basket_side");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.OPEN_CRATE, "crate_block/side_bot1", "crate_block/crate_top_empty", "crate_block/side_bot1");

                // Additional Solid Blocks from JSON definitions - Part 7
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.ORANGE_BASKET, "crate_block/basket_bottom", "crate_block/basket_oranges", "crate_block/basket_side");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.ORANGE_BRICK_ARCH_SINGLE, "brick/orange/all1", "brick/orange/all1", "brick/orange/arch_single");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.ORANGE_BRICK_ARCH_DOUBLE, "brick/orange/all1", "brick/orange/all1", "brick/orange/arch_double");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.ORANGE_BRICK_DENTIL, "brick/orange/all1", "brick/orange/all1", "brick/orange/dentil");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.ORNATE_MARBLE, "marble/quartz/ornate/side");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.ORANGE_BRICK_ROWLOCK, "brick/orange/all1", "brick/orange/all1", "brick/orange/rowlock1");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.ORNATE_SANDSTONE, "ashlar_third/sandstone/ornate/side");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.PARQUET_FLOOR, "wood/oak/ornate");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.PILED_BONES, "piled_bones/side");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.PINK_SANDSTONE_ENGRAVED, "ashlar_engraved/sandy_pink/all");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.PISTON_TOP, "wood/oak/studded");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POMEGRANATE_BASKET, "crate_block/basket_bottom", "crate_block/basket_pomegranates", "crate_block/basket_side");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.PURPLE_GRAPE_BASKET, "crate_block/basket_bottom", "crate_block/basket_grape_purple", "crate_block/basket_side");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.PURPLE_GRAPE_CRATE, "crate_block/crate_side_crossbar_right", "crate_block/crate_top_grape_purple", "crate_block/crate_side_crossbar_crossed");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.REACH_BRICK_ENGRAVED, "ashlar_engraved/reach/all");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.REACH_OAK_WOOD_PANELLING, "wood/oak/panelling/top_bottom", "wood/oak/panelling/top_bottom", "wood/oak/panelling/side1");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.RED_LANTERN2, "lighting/lantern_red_bottom", "lighting/lantern_red_top", "lighting/lantern_red_side");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.REACH_SPRUCE_WOOD_PANELLING, "wood/spruce/panelling/top_bottom", "wood/spruce/panelling/top_bottom", "wood/spruce/panelling/side1");

                // Additional Solid Blocks from JSON definitions - Part 8
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.REDORANGE_CARVED_SANDSTONE, "redorange_carved_sandstone/side");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.SALT_CRATE, "crate_block/side_bot1", "crate_block/crate_top_salt", "crate_block/side_bot1");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.SANDY_STONE_SLABS, "sandy_stone/sandy_stone_0");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.SEPT_CRYSTAL_LARGE, "crystal/all");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.SHOP_UTILITY_BLOCK, "utility_block/shop");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.SILVER_TIN_CRATE, "crate_block/crate_side_crossbar_right", "crate_block/crate_top_tin", "crate_block/crate_side_crossbar_crossed");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.SMALL_ORANGE_BRICKS_ORNATE_TOP, "brick/orange/ornate/top_bottom", "brick/orange/ornate/top_bottom", "brick/orange/all1");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.SMALL_ORANGE_BRICKS_ORNATE, "brick/orange/ornate/top_bottom");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.SMALL_SMOOTH_STONE_BRICK_BLUE_PLASTER, "ashlar_quarter_rounded/grey/all", "ashlar_quarter_rounded/grey/all", "plaster/smooth/blue/all");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.SMALL_SMOOTH_STONE_BRICK_WHITE_PLASTER, "ashlar_quarter_rounded/grey/all", "ashlar_quarter_rounded/grey/all", "plaster/smooth/brown_white/all");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.SMALL_STONE_BRICK_WHITE_PLASTER, "ashlar_quarter/green_grey/all", "ashlar_quarter/green_grey/all", "plaster/smooth/white/all");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.SMALL_WHITE_BRICK_BROWNISH_WHITE_PLASTER, "ashlar_quarter_rounded/white/all", "ashlar_quarter_rounded/white/all", "plaster/smooth/brown_white/all");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.SMALL_WHITE_BRICK_WHITE_PLASTER, "ashlar_quarter_rounded/white/all", "ashlar_quarter_rounded/white/all", "plaster/smooth/white/all");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.SOURLEAF_BASKET, "crate_block/basket_bottom", "crate_block/basket_sourleaf", "crate_block/basket_side");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.SOURLEAF_CRATE, "crate_block/crate_side_crossbar_right", "crate_block/crate_sourleaf", "crate_block/crate_side_crossbar_crossed");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.SOUTHERN_BRICK_ARCH_FLAT, "brick/southern/all1", "brick/southern/all1", "brick/southern/arch_flat");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.SOUTHERN_BRICK_ARCH, "brick/southern/all1", "brick/southern/all1", "brick/southern/arch");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.SOUTHERN_BRICK_LINTEL, "brick/southern/all1", "brick/southern/all1", "brick/southern/lintel");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.SPECIAL_UTILITY_BLOCK, "utility_block/special");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.SPIT_ROAST, "meat_spitroast/all");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.SQUASH, "squash/top", "squash/top", "squash/side");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.STACKED_BONES_SOLID, "stacked_bones/bone_stacked_front", "stacked_bones/bone_stacked_front", "stacked_bones/bone_stacked_rotated");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.STORMLANDS_BRICK_ENGRAVED, "ashlar_engraved/stormlands/all");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.TABLE_BOOKS, "bench_block/spruce_top", "bench_block/table_top", "bench_block/table_drawer_books_side");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.TABLE_DRAWERS, "bench_block/spruce_top", "bench_block/table_top", "bench_block/table_drawers_side");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.TABLE_WIDGETS, "bench_block/spruce_top", "bench_block/table_top", "bench_block/table_drawer_widgets_side");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.TERRACOTTA_ENGRAVED, "ashlar_engraved/terracotta/all");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.THICK_GRASS_BLOCK, "grass_block/forest_top1");

                // Food blocks
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.TURNIP_BASKET, "crate_block/basket_bottom", "crate_block/basket_turnip", "crate_block/basket_side");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.TURNIP_CRATE, "crate_block/side_bot1", "crate_block/crate_top_turnip", "crate_block/side_bot1");

                // Plaster blocks
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.UNUSED_BROWN_PLASTER, "ashlar_quarter_rounded/light_brown/all", "ashlar_quarter_rounded/light_brown/all", "plaster/smooth/brown_white/all");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.UNUSED_PURPLE_PLASTER, "ashlar_quarter_rounded/dark_red/all", "ashlar_quarter_rounded/dark_red/all", "plaster/smooth/brown_white/all");

                // Engraved blocks
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.VIVID_DARK_SANDSTONE_ENGRAVED, "ashlar_engraved/dark_tan/all");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.VIVID_SANDSTONE_ENGRAVED, "ashlar_engraved/tan/all");

                // Barrel block
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.WATER_BARREL, "barrel_closed/barrel_top_closed", "crate_block/barrel_top_water", "barrel_sides/side0");

                // Engraved white brick
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.WHITE_BRICK_ENGRAVED, "ashlar_engraved/white/all");

                // Grape baskets and crates
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.WHITE_GRAPE_BASKET, "crate_block/basket_bottom", "crate_block/basket_grape_white", "crate_block/basket_side");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.WHITE_GRAPE_CRATE, "crate_block/crate_side_crossbar_right", "crate_block/crate_top_grape_white", "crate_block/crate_side_crossbar_crossed");

                // Carving block
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.WINTERFELL_CARVING, "ashlar_third/dark_grey/carving/side");

                // Utility blocks
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.WIP_UTILITY_BLOCK, "utility_block/wip");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.WORKSHOP_UTILITY_BLOCK, "utility_block/workshop");
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.YARD_UTILITY_BLOCK, "utility_block/yard");

                // Timber blocks
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.TIMBER_NORTHERN_BLUE_BRESSUMMER);

                // Clay block
                solidExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.YELLOW_STAINED_CLAY, "clay/yellow_stained_clay");

                // Sand Blocks
                sandExport.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.SAND_SKELETON,
                                "sand_block/sand_skeleton");

                // Coral Fan Blocks
                fanExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.CORAL_BRAIN_FAN,
                                "coral/brain/fan1");
                fanExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.CORAL_BUBBLE_FAN,
                                "coral/bubble/fan1");
                fanExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.CORAL_FIRE_FAN,
                                "coral/fire/fan1");
                fanExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.CORAL_HORN_FAN,
                                "coral/horn/fan1");
                fanExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.CORAL_TUBE_FAN,
                                "coral/tube/fan1");

                // Vines Blocks
                vinesExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.DAPPLED_MOSS,
                                "dappled_moss/dappled", "dappled_moss/dappled");
                vinesExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.JASMINE_VINES,
                                "jasmine_vines/side1", "jasmine_vines/side1");
                vinesExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.VINES,
                                "vines/side1", "vines/side1");
                vinesExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.FALLING_WATER_BLOCK_ONE,
                                "alyssas_tears_mist/mist1", "alyssas_tears_mist/mist1");
                vinesExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.FALLING_WATER_BLOCK_TWO,
                                "alyssas_tears_mist/mist2", "alyssas_tears_mist/mist2");
                vinesExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.FALLING_WATER_BLOCK_THREE,
                                "alyssas_tears_mist/mist3", "alyssas_tears_mist/mist3");
                vinesExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.FALLING_WATER_BLOCK_FOUR,
                                "alyssas_tears_mist/mist4", "alyssas_tears_mist/mist4");

                // Half Door Blocks
                halfDoorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.BIRCH_WINDOW_SHUTTERS,
                                "wood/birch/shutters");
                halfDoorExporter.generateBlockStateModels(blockStateModelGenerator,
                                ModBlocks2.DORNE_RED_WINDOW_SHUTTERS,
                                "shutter_block/shutters_dorne");
                halfDoorExporter.generateBlockStateModels(blockStateModelGenerator,
                                ModBlocks2.GREEN_LANNISPORT_WINDOW_SHUTTERS,
                                "shutter_block/shutters_lannisport");
                halfDoorExporter.generateBlockStateModels(blockStateModelGenerator,
                                ModBlocks2.GREY_WOOD_WINDOW_SHUTTERS,
                                "wood/grey/shutters");
                halfDoorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.JUNGLE_WINDOW_SHUTTERS,
                                "wood/jungle/shutters");
                halfDoorExporter.generateBlockStateModels(blockStateModelGenerator,
                                ModBlocks2.NORTHERN_WOOD_WINDOW_SHUTTERS,
                                "wood/northern/shutters");
                halfDoorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.OAK_WINDOW_SHUTTERS,
                                "wood/oak/shutters");
                halfDoorExporter.generateBlockStateModels(blockStateModelGenerator,
                                ModBlocks2.REACH_BLUE_WINDOW_SHUTTERS,
                                "shutter_block/shutters_reach");
                halfDoorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.SPRUCE_WINDOW_SHUTTERS,
                                "wood/spruce/shutters");
                halfDoorExporter.generateBlockStateModels(blockStateModelGenerator,
                                ModBlocks2.WHITE_WOOD_WINDOW_SHUTTERS,
                                "wood/white/shutters");

                // Cross Blocks (Plant Blocks)
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.BLUE_BELLS,
                                "flowers/blue_bells", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.BLUE_CHICORY,
                                "flowers/blue_chicory/side1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.BLUE_FLAX,
                                "flowers/blue_flax1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.BLUE_FORGETMENOTS,
                                "flowers/blue_forgetmenots1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.BLUE_HYACINTH,
                                "flowers/blue_hyacinth1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.BLUE_ORCHID,
                                "flowers/blue_orchid1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.BLUE_SWAMP_BELLS,
                                "flowers/blue_swamp_bells1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.BRACKEN,
                                "bracken/side1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.BROWN_MUSHROOM_1,
                                "brown_mushroom_block/mushroom_brown_0", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.BROWN_MUSHROOM_2,
                                "brown_mushroom_block/mushroom_brown_1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.BROWN_MUSHROOM_3,
                                "brown_mushroom_block/mushroom_brown_2", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.BROWN_MUSHROOM_4,
                                "brown_mushroom_block/mushroom_brown_3", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.BROWN_MUSHROOM_5,
                                "brown_mushroom_block/mushroom_brown_4", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.BROWN_MUSHROOM_6,
                                "brown_mushroom_block/mushroom_brown_5", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.BROWN_MUSHROOM_7,
                                "brown_mushroom_block/mushroom_brown_6", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.BROWN_MUSHROOM_8,
                                "brown_mushroom_block/mushroom_brown_7", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.BROWN_MUSHROOM_9,
                                "brown_mushroom_block/mushroom_brown_8", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.BROWN_MUSHROOM_10,
                                "brown_mushroom_block/mushroom_brown_9", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.BROWN_MUSHROOM_11,
                                "brown_mushroom_block/mushroom_brown_10", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.BROWN_MUSHROOM_12,
                                "brown_mushroom_block/mushroom_brown_11", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.BROWN_MUSHROOM_13,
                                "brown_mushroom_block/mushroom_brown_12", true, 4);

                // Coral Web Blocks
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.CORAL_BRAIN_WEB,
                                "coral/brain/web1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.CORAL_BUBBLE_WEB,
                                "coral/bubble/web1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.CORAL_FIRE_WEB,
                                "coral/fire/web1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.CORAL_HORN_WEB,
                                "coral/horn/web1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.CORAL_TUBE_WEB,
                                "coral/tube/web1", true, 4);

                // Additional Plant Blocks
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.COW_PARSELY,
                                "cow_parsely/side1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.CRANBERRY_BUSH,
                                "cranberry/base1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.DEAD_BRACKEN,
                                "dead_bracken/side1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.DEAD_BUSH,
                                "dorne_bush_thorny/side1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.DEAD_SCRUB_GRASS,
                                "flowers/dead_scrub_grass1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.DOCK_LEAF,
                                "dock_leaf/side1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.FIREWEED,
                                "fireweed/side1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.GRASS,
                                "minecraft:block/fern/fern1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.GREEN_LEAFY_HERB,
                                "flowers/green_leafy_herb", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.GREEN_SCRUB_GRASS,
                                "flowers/green_scrub_grass1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.GREEN_SPINY_HERB,
                                "flowers/green_spiny_herb1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.HEATHER,
                                "heather/side1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.KELP,
                                "kelp/side1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.LADY_FERN,
                                "lady_fern/side1", true, 4);

                // Flower Blocks
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.MAGENTA_ROSES,
                                "flowers/magenta_roses1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.MEADOW_FESCUE,
                                "flowers/meadow_fescue/side1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.NETTLE,
                                "nettle/side1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.ORANGE_BELLS,
                                "flowers/orange_bells1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.ORANGE_BOG_ASPHODEL,
                                "flowers/orange_bog_asphodel1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.ORANGE_MARIGOLDS,
                                "flowers/orange_marigolds1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.ORANGE_SUN_STAR,
                                "flowers/orange_sun_star1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.ORANGE_TROLLIUS,
                                "flowers/orange_trollius1", true, 4);

                // Pink Flower Blocks
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.PINK_ALLIUM,
                                "flowers/pink_allium1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.PINK_PRIMROSE,
                                "flowers/pink_primrose1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.PINK_ROSES,
                                "flowers/pink_roses1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.PINK_SWEET_PEAS,
                                "flowers/pink_sweet_peas1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.PINK_THISTLE,
                                "flowers/pink_thistle/side1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.PINK_TULIPS,
                                "flowers/pink_tulips1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.PINK_WILDFLOWERS,
                                "flowers/pink_wildflowers", true, 4);

                // Purple Flower Blocks
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.PURPLE_ALPINE_SOWTHISTLE,
                                "flowers/purple_alpine_sowthistle/side1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.PURPLE_FOXGLOVE,
                                "flowers/purple_foxglove1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.PURPLE_LAVENDER,
                                "flowers/purple_lavender1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.PURPLE_PANSIES,
                                "flowers/purple_pansies1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.PURPLE_ROSES,
                                "flowers/purple_roses1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.PURPLE_VIOLETS,
                                "flowers/purple_violets1", true, 4);

                // Red Flower Blocks
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.RED_ASTER,
                                "flowers/red_aster1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.RED_CARNATIONS,
                                "flowers/red_carnations1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.RED_CHRYSANTHEMUM,
                                "flowers/red_chrysanthemum1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.RED_DARK_ROSES,
                                "flowers/red_dark_roses1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.RED_FERN,
                                "red_fern/side1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.RED_FLOWERING_SPINY_HERB,
                                "flowers/red_flowering_spiny_herb1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.RED_MUSHROOM_1,
                                "red_mushroom_block/mushroom_red_0", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.RED_MUSHROOM_2,
                                "red_mushroom_block/mushroom_red_1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.RED_MUSHROOM_3,
                                "red_mushroom_block/mushroom_red_2", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.RED_MUSHROOM_4,
                                "red_mushroom_block/mushroom_red_3", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.RED_MUSHROOM_5,
                                "red_mushroom_block/mushroom_red_4", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.RED_MUSHROOM_6,
                                "red_mushroom_block/mushroom_red_5", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.RED_MUSHROOM_7,
                                "red_mushroom_block/mushroom_red_6", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.RED_MUSHROOM_8,
                                "red_mushroom_block/mushroom_red_7", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.RED_MUSHROOM_9,
                                "red_mushroom_block/mushroom_red_8", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.RED_POPPIES,
                                "flowers/red_poppies1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.RED_ROSES,
                                "flowers/red_roses1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.RED_SORREL,
                                "flowers/red_sorrel1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.RED_SOURLEAF_BUSH,
                                "flowers/red_sourleaf_bush1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.RED_TULIPS,
                                "flowers/red_tulips1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.STRAWBERRY_BUSH,
                                "flowers/strawberry", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.THICK_GRASS,
                                "minecraft:block/grass/grass1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.UNSHADED_GRASS,
                                "deadbush/side1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.WHITE_CHAMOMILE,
                                "flowers/white_chamomile1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.WHITE_DAISIES,
                                "flowers/white_daisies1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.WHITE_LILYOFTHEVALLEY,
                                "flowers/white_lily_valley1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.WHITE_PEONY,
                                "flowers/white_peony1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.WHITE_ROSES,
                                "flowers/white_roses1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.YELLOW_BEDSTRAW,
                                "flowers/yellow_bedstraw/side1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.YELLOW_BELLS,
                                "flowers/yellow_bells1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.YELLOW_BUTTERCUPS,
                                "flowers/yellow_buttercups1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.YELLOW_DAFFODILS,
                                "flowers/yellow_daffodils1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.YELLOW_DAISIES,
                                "flowers/yellow_daisies1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.YELLOW_DANDELIONS,
                                "flowers/yellow_dandelions1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.YELLOW_HELLEBORE,
                                "flowers/yellow_hellebore1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.YELLOW_LUPINE,
                                "flowers/yellow_lupine1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.YELLOW_ROSES,
                                "flowers/yellow_roses1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.YELLOW_RUDBECKIA,
                                "flowers/yellow_rudbeckia1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.YELLOW_SUNFLOWER,
                                "flowers/yellow_sunflower/side1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.YELLOW_TANSY,
                                "flowers/yellow_tansy", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.YELLOW_WILDFLOWERS,
                                "flowers/yellow_sunflower/side1", true, 4);

                // Flower Pot Blocks
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_BLUE_BELLS,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/blue_bells");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_BLUE_CHICORY,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/blue_chicory/side1");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_BLUE_FLAX,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/blue_flax1");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator,
                                ModBlocks2.POTTED_BLUE_FORGETMENOTS,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/blue_forgetmenots1");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_BLUE_HYACINTH,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/blue_hyacinth1");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_BLUE_ORCHID,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/blue_orchid1");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_BLUE_SWAMP_BELLS,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/blue_swamp_bells1");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_BRACKEN,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "bracken/side1");

                // Additional potted flowerpot blocks
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_BROWN_MUSHROOM_1,
                                "minecraft:block/dirt", "minecraft:block/flower_pot",
                                "brown_mushroom_block/mushroom_brown_0");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_BROWN_MUSHROOM_3,
                                "minecraft:block/dirt", "minecraft:block/flower_pot",
                                "brown_mushroom_block/mushroom_brown_2");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_BROWN_MUSHROOM_6,
                                "minecraft:block/dirt", "minecraft:block/flower_pot",
                                "brown_mushroom_block/mushroom_brown_5");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator,
                                ModBlocks2.POTTED_BROWN_MUSHROOM_13,
                                "minecraft:block/dirt", "minecraft:block/flower_pot",
                                "brown_mushroom_block/mushroom_brown_12");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_CATTAILS,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "cattails/side1");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_COW_PARSELY,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "cow_parsely/side1");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_DEAD_BRACKEN,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "dead_bracken/side1");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_DEAD_BUSH,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "dorne_bush_thorny/side1");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_DEAD_SCRUB_GRASS,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/dead_scrub_grass1");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_DOCK_LEAF,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "dock_leaf/side1");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_FIREWEED,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "fireweed/side1");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_GRASS,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "minecraft:block/fern/fern1");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_GREEN_LEAFY_HERB,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/green_leafy_herb");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator,
                                ModBlocks2.POTTED_GREEN_SCRUB_GRASS,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/green_scrub_grass1");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_GREEN_SPINY_HERB,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/green_spiny_herb1");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_HEATHER,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "heather/side1");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_LADY_FERN,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "lady_fern/side1");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_MAGENTA_ROSES,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/magenta_roses1");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_MEADOW_FESCUE,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/meadow_fescue/side1");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_NETTLE,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "nettle/side1");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_ORANGE_BELLS,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/orange_bells1");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator,
                                ModBlocks2.POTTED_ORANGE_BOG_ASPHODEL,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/orange_bog_asphodel1");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_ORANGE_MARIGOLDS,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/orange_marigolds1");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_ORANGE_SUN_STAR,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/orange_sun_star1");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_ORANGE_TROLLIUS,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/orange_trollius1");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_PINK_ALLIUM,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/pink_allium1");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_PINK_PRIMROSE,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/pink_primrose1");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_PINK_ROSES,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/pink_roses1");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_PINK_SWEET_PEAS,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/pink_sweet_peas1");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_PINK_THISTLE,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/pink_thistle/side1");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_PINK_TULIPS,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/pink_tulips1");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_PINK_WILDFLOWERS,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/pink_wildflowers");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator,
                                ModBlocks2.POTTED_PURPLE_ALPINE_SOWTHISTLE,
                                "minecraft:block/dirt", "minecraft:block/flower_pot",
                                "flowers/purple_alpine_sowthistle/side1");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_PURPLE_FOXGLOVE,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/purple_foxglove1");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_PURPLE_LAVENDER,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/purple_lavender1");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_PURPLE_PANSIES,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/purple_pansies1");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_PURPLE_ROSES,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/purple_roses1");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_PURPLE_VIOLETS,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/purple_violets1");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_RED_ASTER,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/red_aster1");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_RED_CARNATIONS,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/red_carnations1");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator,
                                ModBlocks2.POTTED_RED_CHRYSANTHEMUM,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/red_chrysanthemum1");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_RED_DARK_ROSES,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/red_dark_roses1");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_RED_FERN,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "red_fern/side1");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator,
                                ModBlocks2.POTTED_RED_FLOWERING_SPINY_HERB,
                                "minecraft:block/dirt", "minecraft:block/flower_pot",
                                "flowers/red_flowering_spiny_herb1");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_RED_MUSHROOM_1,
                                "minecraft:block/dirt", "minecraft:block/flower_pot",
                                "red_mushroom_block/mushroom_red_0");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_RED_MUSHROOM_2,
                                "minecraft:block/dirt", "minecraft:block/flower_pot",
                                "red_mushroom_block/mushroom_red_1");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_RED_MUSHROOM_3,
                                "minecraft:block/dirt", "minecraft:block/flower_pot",
                                "red_mushroom_block/mushroom_red_2");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_RED_MUSHROOM_7,
                                "minecraft:block/dirt", "minecraft:block/flower_pot",
                                "red_mushroom_block/mushroom_red_6");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_RED_MUSHROOM_8,
                                "minecraft:block/dirt", "minecraft:block/flower_pot",
                                "red_mushroom_block/mushroom_red_7");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_RED_MUSHROOM_9,
                                "minecraft:block/dirt", "minecraft:block/flower_pot",
                                "red_mushroom_block/mushroom_red_8");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_RED_POPPIES,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/red_poppies1");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_RED_ROSES,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/red_roses1");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_RED_SORREL,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/red_sorrel1");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator,
                                ModBlocks2.POTTED_RED_SOURLEAF_BUSH,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/red_sourleaf_bush1");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_RED_TULIPS,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/red_tulips1");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_WHITE_CHAMOMILE,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/white_chamomile1");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_WHITE_DAISIES,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/white_daisies1");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator,
                                ModBlocks2.POTTED_WHITE_LILYOFTHEVALLEY,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/white_lily_valley1");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_WHITE_PEONY,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/white_peony1");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_WHITE_ROSES,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/white_roses1");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_YELLOW_BEDSTRAW,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/yellow_bedstraw/side1");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_YELLOW_BELLS,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/yellow_bells1");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator,
                                ModBlocks2.POTTED_YELLOW_BUTTERCUPS,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/yellow_buttercups1");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_YELLOW_DAFFODILS,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/yellow_daffodils1");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_YELLOW_DAISIES,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/yellow_daisies1");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator,
                                ModBlocks2.POTTED_YELLOW_DANDELIONS,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/yellow_dandelions1");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_YELLOW_HELLEBORE,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/yellow_hellebore1");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_YELLOW_LUPINE,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/yellow_lupine1");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_YELLOW_ROSES,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/yellow_roses1");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_YELLOW_RUDBECKIA,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/yellow_rudbeckia1");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_YELLOW_SUNFLOWER,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/yellow_sunflower/side1");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.POTTED_YELLOW_TANSY,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/yellow_tansy");
                flowerPotExporter.generateBlockStateModels(blockStateModelGenerator,
                                ModBlocks2.POTTED_YELLOW_WILDFLOWERS,
                                "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/yellow_wildflowers");

                // Crop Blocks
                cropExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.CANDLE_ALTAR,
                                "lighting/candle_altar/lit1");
                cropExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.CROP_CARROTS,
                                "carrots/carrots_stage_0", true, 1);
                cropExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.CROP_PEAS,
                                "peas/peas_stage_0", true, 1);
                cropExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.CROP_TURNIPS,
                                "turnips/turnips_stage_0", true, 1);
                cropExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.CROP_WHEAT,
                                "wheat/wheat_stage_0", true, 1);
                cropExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.SEAGRASS,
                                "seagrass/side1", false, 1);

                // Web Blocks
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.ALYSSAS_TEARS_MIST_ONE,
                                "alyssas_tears_mist/mist1", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.ALYSSAS_TEARS_MIST_TWO,
                                "alyssas_tears_mist/mist2", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.ALYSSAS_TEARS_MIST_THREE,
                                "alyssas_tears_mist/mist3", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.ALYSSAS_TEARS_MIST_FOUR,
                                "alyssas_tears_mist/mist4", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.BLACK_BRICICLE,
                                "ashlar_melted/black/bricicle/side", true, 4);
                crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.BEES,
                                "web_block/bug_bees", true, 4);

                // Additional web blocks
                webExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.BUSHEL_OF_HERBS,
                                "web_block/food_herbs");
                webExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.BUSHEL_OF_SOURLEAF,
                                "web_block/food_sourleaf");
                webExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.BUTTERFLY_BLUE,
                                "web_block/bug_butterfly_blue");
                webExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.BUTTERFLY_ORANGE,
                                "web_block/bug_butterfly_orange");
                webExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.BUTTERFLY_RED,
                                "web_block/bug_butterfly_red");
                webExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.BUTTERFLY_WHITE,
                                "web_block/bug_butterfly_white");
                webExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.BUTTERFLY_YELLOW,
                                "web_block/bug_butterfly_yellow");
                webExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.CATTAILS,
                                "cattails/side1");
                webExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.CHAIN_BLOCK_HARNESS,
                                "web_block/chain_blockharness");
                webExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.CHILI_RISTRA,
                                "web_block/food_chili_ristra");
                webExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.COBWEB,
                                "cobweb/side1");
                webExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.DEAD_FISH,
                                "dead_fish/fish_dead1");
                webExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.DEAD_FOWL,
                                "dead_fowl/fowl");
                webExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.DEAD_FROG,
                                "dead_frog/toad_dead1");
                webExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.DEAD_HARE,
                                "dead_hare/rabbit_dead1");
                webExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.DEAD_JUNGLE_TALL_GRASS,
                                "dead_jungle_tall_grass/down_side1");
                webExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.DEAD_RAT,
                                "dead_rat/rat1");
                webExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.DEAD_SAVANNA_TALL_GRASS,
                                "dead_savanna_tall_grass/dead_savanna_tall_grass");
                webExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.DRAGONFLY,
                                "web_block/bug_dragonfly");
                webExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.FLIES,
                                "web_block/bug_flies");
                webExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.GARLIC_STRAND,
                                "web_block/food_garlic_strand");
                webExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.ICICLE,
                                "icicle/side");
                webExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.IRON_THRONE_RANDOM_BLADES,
                                "blades_random/side1");
                webExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.JUNGLE_TALL_FERN,
                                "jungle_tall_fern/side");
                webExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.JUNGLE_TALL_GRASS,
                                "jungle_tall_grass/down_side1");
                webExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.ROPE_BLOCK_HARNESS,
                                "web_block/rope_blockharness");
                webExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.SAUSAGES_LEG_OF_HAM,
                                "sausages_leg_of_ham/default");
                webExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.SAVANNA_TALL_GRASS,
                                "savanna_tall_grass/savanna_tall_grass");
                webExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.SMOKE,
                                "smoke/smoke1");
                webExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.VERTICAL_CHAIN,
                                "web_block/chain_vertical");
                webExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.VERTICAL_ROPE,
                                "web_block/rope_vertical");
        }

        public static void generateItemModels(ItemModelGenerator itemModelGenerator) {
                // Arrow Slit Blocks
                arrowSlitExport.generateItemModels(itemModelGenerator, ModBlocks2.ARBOR_BRICK_ARROW_SLIT);
                arrowSlitExport.generateItemModels(itemModelGenerator, ModBlocks2.BLACK_GRANITE_ARROW_SLIT);

                // Table Blocks
                tableExport.generateItemModels(itemModelGenerator, ModBlocks2.OAK_TABLE);
                tableExport.generateItemModels(itemModelGenerator, ModBlocks2.BIRCH_TABLE);
                tableExport.generateItemModels(itemModelGenerator, ModBlocks2.SPRUCE_TABLE);

                // Chair Blocks
                chairExporter.generateItemModels(itemModelGenerator, ModBlocks2.OAK_CHAIR);
                chairExporter.generateItemModels(itemModelGenerator, ModBlocks2.BIRCH_CHAIR);
                chairExporter.generateItemModels(itemModelGenerator, ModBlocks2.SPRUCE_CHAIR);

                // Way Sign Blocks
                waySignExporter.generateItemModels(itemModelGenerator, ModBlocks2.OAK_WAY_SIGN);

                // TrapDoor Blocks
                trapDoorExporter.generateItemModels(itemModelGenerator, ModBlocks2.KINGS_LANDING_SEWER_MANHOLE);
                trapDoorExporter.generateItemModels(itemModelGenerator, ModBlocks2.OLDTOWN_SEWER_MANHOLE);
                trapDoorExporter.generateItemModels(itemModelGenerator, ModBlocks2.SEWER_MANHOLE);
                trapDoorExporter.generateItemModels(itemModelGenerator, ModBlocks2.WHITE_HARBOR_SEWER_MANHOLE);

                // Log Blocks
                logExporter.generateItemModels(itemModelGenerator, ModBlocks2.OAK_LOG_CHAIN);
                logExporter.generateItemModels(itemModelGenerator, ModBlocks2.JUNGLE_LOG_CHAIN);
                logExporter.generateItemModels(itemModelGenerator, ModBlocks2.JUNGLE_LOG_ROPE);
                logExporter.generateItemModels(itemModelGenerator, ModBlocks2.ARCHERY_TARGET);
                logExporter.generateItemModels(itemModelGenerator, ModBlocks2.CLOSED_BARREL);
                logExporter.generateItemModels(itemModelGenerator, ModBlocks2.FIREWOOD);
                logExporter.generateItemModels(itemModelGenerator, ModBlocks2.MARBLE_PILLAR);
                logExporter.generateItemModels(itemModelGenerator, ModBlocks2.MARBLE_PILLAR_VERTICAL_CTM);
                logExporter.generateItemModels(itemModelGenerator, ModBlocks2.MOSSY_OAK_LOG);
                logExporter.generateItemModels(itemModelGenerator, ModBlocks2.MOSSY_BIRCH_LOG);
                logExporter.generateItemModels(itemModelGenerator, ModBlocks2.MOSSY_SPRUCE_LOG);
                logExporter.generateItemModels(itemModelGenerator, ModBlocks2.MOSSY_JUNGLE_LOG);
                logExporter.generateItemModels(itemModelGenerator, ModBlocks2.OAK_LOG_ROPE);
                logExporter.generateItemModels(itemModelGenerator, ModBlocks2.PALM_TREE_LOG);
                logExporter.generateItemModels(itemModelGenerator, ModBlocks2.SANDSTONE_PILLAR);
                logExporter.generateItemModels(itemModelGenerator, ModBlocks2.SPRUCE_LOG_CHAIN);
                logExporter.generateItemModels(itemModelGenerator, ModBlocks2.SPRUCE_LOG_ROPE);
                logExporter.generateItemModels(itemModelGenerator, ModBlocks2.STACKED_BONES);
                logExporter.generateItemModels(itemModelGenerator, ModBlocks2.WEIRWOOD_FACE_0);
                logExporter.generateItemModels(itemModelGenerator, ModBlocks2.WEIRWOOD_FACE_1);
                logExporter.generateItemModels(itemModelGenerator, ModBlocks2.WEIRWOOD_FACE_2);
                logExporter.generateItemModels(itemModelGenerator, ModBlocks2.WEIRWOOD_FACE_3);
                logExporter.generateItemModels(itemModelGenerator, ModBlocks2.WEIRWOOD_FACE_4);
                logExporter.generateItemModels(itemModelGenerator, ModBlocks2.WEIRWOOD_FACE_5);
                logExporter.generateItemModels(itemModelGenerator, ModBlocks2.WEIRWOOD_FACE_6);
                logExporter.generateItemModels(itemModelGenerator, ModBlocks2.WEIRWOOD_FACE_7);
                logExporter.generateItemModels(itemModelGenerator, ModBlocks2.WEIRWOOD_FACE_8);
                logExporter.generateItemModels(itemModelGenerator, ModBlocks2.WEIRWOOD_SCARS);

                // Torch Blocks
                torchExporter.generateItemModels(itemModelGenerator, ModBlocks2.TORCH);
                torchExporter.generateItemModels(itemModelGenerator, ModBlocks2.TORCH_UNLIT);
                torchExporter.generateItemModels(itemModelGenerator, ModBlocks2.CANDLE);
                torchExporter.generateItemModels(itemModelGenerator, ModBlocks2.CANDLE_UNLIT);

                // Ladder Blocks
                ladderExporter.generateItemModels(itemModelGenerator, ModBlocks2.IRON_RUNGS);
                ladderExporter.generateItemModels(itemModelGenerator, ModBlocks2.IRON_RUNGS_BROKEN);
                ladderExporter.generateItemModels(itemModelGenerator, ModBlocks2.ROPE_LADDER);
                ladderExporter.generateItemModels(itemModelGenerator, ModBlocks2.VINE_JASMINE);
                ladderExporter.generateItemModels(itemModelGenerator, ModBlocks2.WINTERFELL_STONE_LADDER);
                ladderExporter.generateItemModels(itemModelGenerator, ModBlocks2.WOOD_LADDER);

                // Pane Blocks
                paneExporter.generateItemModels(itemModelGenerator, ModBlocks2.VERTICAL_NET,
                                "vertical_net/vertical_net1");
                paneExporter.generateItemModels(itemModelGenerator, ModBlocks2.DORNE_CARVED_STONE_WINDOW,
                                "pane_block/moorish_stone_window_pane");
                paneExporter.generateItemModels(itemModelGenerator, ModBlocks2.DORNE_CARVED_WOODEN_WINDOW,
                                "pane_block/moorish_wood_window_pane");
                paneExporter.generateItemModels(itemModelGenerator, ModBlocks2.IRON_BARS,
                                "bars_iron_block/iron_bars");
                paneExporter.generateItemModels(itemModelGenerator, ModBlocks2.OXIDIZED_IRON_CROSSBAR,
                                "bars_iron_block/bars_iron_oxidized_crossbars");
                paneExporter.generateItemModels(itemModelGenerator, ModBlocks2.IRON_CROSSBAR,
                                "bars_iron_block/bars_iron_crossbars");
                paneExporter.generateItemModels(itemModelGenerator, ModBlocks2.OXIDIZED_IRON_BARS,
                                "bars_iron_block/bars_iron_oxidized");

                // Solid Blocks
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.SIX_SIDED_BIRCH, "bark/birch/side");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.APPLE_BASKET, "crate_block/basket_apple");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.APPLE_CRATE, "crate_block/crate_top_apples");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.APPROVAL_UTILITY_BLOCK, "utility_block/approved");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.APRICOT_BASKET, "crate_block/basket_apricot");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.SIX_SIDED_JUNGLE, "bark/jungle/side");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.SIX_SIDED_OAK, "bark/oak/side");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.SIX_SIDED_SPRUCE, "bark/spruce/side");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.SIX_SIDED_STONE_SLAB, "ashlar_half/white/tile");

                // Additional Solid Blocks from JSON definitions
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.ARBOR_BRICK_ORNATE, "ashlar_engraved/arbor/all");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.BENCH_BUTCHER_KNIVES, "bench_block/bench_butcher_knives");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.BENCH_CARPENTRY_HAMMER_SAW, "bench_block/bench_carpentry_hammer_saw");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.BENCH_DRAWERS, "bench_block/bench_drawers");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.BENCH_KITCHEN_KNIVES, "bench_block/bench_kitchen_knives");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.BENCH_KITCHEN_PANS, "bench_block/bench_kitchen_pans");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.BENCH_MASON_HAMMER_MALLET, "bench_block/bench_mason_hammer_mallet");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.BERRY_BASKET, "crate_block/basket_berry");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.BERRY_CRATE, "crate_block/crate_top_berry");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.BLACK_BRICK_ENGRAVED, "ashlar_engraved/black/all");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.BLUEGREEN_CARVED_SANDSTONE, "bluegreen_carved_sandstone/side");

                // Additional Solid Blocks from JSON definitions - Part 2
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.BONE_DIRT, "dirt/bone");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.BOOKSHELF_ABANDONED, "bookshelf_abandoned/side1");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.BOOKSHELF_LIBRARY, "bookshelf_library/side1");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.BROKEN_CABINET, "bench_block/cabinet_broken");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.BOOKSHELF_MAESTER, "bookshelf_maester/side1");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.BROWN_GREY_BRICK_ENGRAVED, "ashlar_engraved/brown_grey/all");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.CABINET_DRAWER, "cabinet/drawer/side1");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.CAGE, "cage/side");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.CARROT_BASKET, "crate_block/basket_carrot");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.CARROT_CRATE, "crate_block/crate_top_carrot");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.CLOSED_BASKET, "crate_block/basket_top_closed");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.CLOSED_CABINET, "cabinet/closed/side");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.COARSE_DARK_RED_CARVED_SANDSTONE, "ashlar_engraved/pale_dark_red/all");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.COARSE_RED_CARVED_SANDSTONE, "ashlar_engraved/pale_red/all");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.COBBLE_KEYSTONE, "cobblestone/grey/keystone/side");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.COLOURED_SEPT_WINDOW, "glass/sept/all");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.CRATE, "crate_block/crate_side_crossbar_right");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.CRATE2, "crate_block/crate_side_crossbar_left");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.CRATE3, "crate_block/crate_side_crossbar_crossed");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.DARK_GREY_BRICK_ENGRAVED, "ashlar_engraved/dark_grey/all");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.DATE_BASKET, "crate_block/basket_dates");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.DATES, "dates/top");

                // Additional Solid Blocks from JSON definitions - Part 3
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.DESERT_SANDSTONE_ENGRAVED, "ashlar_engraved/sandstone/all");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.DOMESTIC_UTILITY_BLOCK, "utility_block/domestic");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.DONE_UTILITY_BLOCK, "utility_block/done");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.DRAGON_CARVING, "dragon_carving/side1");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.EMPTY_BARREL, "barrel_closed/barrel_top_closed");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.EMPTY_CABINET, "cabinet/top_bottom");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.FAITH_CARVED_ARBOR_BRICK, "ashlar_third/arbor/faith_carved");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.FAITH_CARVED_BLACK_BRICK, "ashlar_quarter/black/faith_carved");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.FAITH_CARVED_BROWN_GREY_BRICK, "ashlar_quarter/brown_grey/faith_carved");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.FAITH_CARVED_COARSE_RED_BRICK, "ashlar_third/pale_red/faith_carved");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.FAITH_CARVED_DARK_GREY_BRICK, "ashlar_quarter/dark_grey/faith_carved");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.FAITH_CARVED_DUN_BRICK, "ashlar_third/dun/faith_carved");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.FAITH_CARVED_GREY_BRICK, "ashlar_third/grey/faith_carved");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.FAITH_CARVED_OLDTOWN_BRICK, "ashlar_quarter_rounded/light_oldtown/faith_carved");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.FAITH_CARVED_PINK_SANDSTONE, "ashlar_third/sandy_pink/faith_carved");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.FAITH_CARVED_REACH_BRICK, "ashlar_quarter_rounded/reach/faith_carved");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.FAITH_CARVED_SMALL_STONE_BRICK, "ashlar_quarter/green_grey/faith_carved");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.FAITH_CARVED_STONE_BRICK, "ashlar_half/white/faith_carved");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.FAITH_CARVED_STORMLANDS_BRICK, "ashlar_quarter/stormlands/faith_carved");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.FAITH_CARVED_WESTERLANDS_BRICK, "ashlar_quarter/westerlands/faith_carved");

                // Additional Solid Blocks from JSON definitions - Part 4
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.FISH_BARREL, "barrel_closed/barrel_top_closed");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.FISH_BASKET, "crate_block/basket_fish");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.FISH_TRAP, "fish_trap/side1");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.FLAGSTONE, "stone_block/flagstone");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.FULL_CABINET, "cabinet/top_bottom");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.GLOWING_EMBERS, "lighting/coals_glowing");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.GRAIN_BASKET, "crate_block/basket_grain");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.GRAIN_CRATE, "crate_block/crate_top_grain");

                // Additional Solid Blocks from JSON definitions - Part 5
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.GREEN_GREY_BRICK_ENGRAVED, "ashlar_engraved/green_grey/all");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.GREY_BRICK_ENGRAVED, "ashlar_engraved/grey/all");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.GREY_KEYSTONE, "stone_block/keystone_grey");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.HIGH_CLASS_UTILITY_BLOCK, "utility_block/highclass");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.HOP_BASKET, "crate_block/basket_hop");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.HOP_CRATE, "crate_block/crate_hops");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.HOUSE_COUNT_UTILITY_BLOCK, "utility_block/housecount");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.INDUSTRY_UTILITY_BLOCK, "utility_block/industry");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.IRON_CRATE, "crate_block/crate_top_iron");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.KL_DUN_CARVED_BRICK, "ashlar_engraved/dun/all");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.LANNISPORT_KEYSTONE_ORANGE_PLASTER, "fieldstone/westerlands/all");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.LANNISPORT_KEYSTONE_YELLOW_PLASTER, "fieldstone/westerlands/all");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.LARGE_CLAY_POT_SOLID, "clay_pot/solid");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.LAVENDER_BASKET, "crate_block/basket_lavender");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.LAVENDER_CRATE, "crate_block/crate_lavender");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.LEMON_BASKET, "crate_block/basket_lemons");

                // Additional Solid Blocks from JSON definitions - Part 6
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.LIGHT_GREY_BRICK_ENGRAVED, "ashlar_engraved/light_grey/all");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.LIGHT_GREY_STONE_WHITE_PLASTER, "fieldstone/grey/all");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.LIGHT_OLDTOWN_BRICK_ENGRAVED, "ashlar_engraved/light_oldtown/all");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.LIME_BASKET, "crate_block/basket_limes");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.LOW_CLASS_UTILITY_BLOCK, "utility_block/lowclass");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.MIDDLE_CLASS_UTILITY_BLOCK, "utility_block/middleclass");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.MIRROR_BLOCK, "mirror/top_bottom");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.MONOCHROME_DARK_SANDSTONE_ENGRAVED, "ashlar_engraved/light_brown/all");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.MONOCHROME_SANDSTONE_ENGRAVED, "ashlar_engraved/westerlands/all");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.NETHER_BRICK_KEYSTONE, "ashlar_half/black/embellished");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.NORTHERN_CARVINGS, "wood/spruce/carving/side");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.NOTE_UTILITY_BLOCK, "utility_block/note");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.OLIVE_BASKET, "crate_block/basket_olives");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.OPEN_BASKET, "crate_block/basket_top");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.OPEN_CRATE, "crate_block/crate_top_empty");

                // Additional Solid Blocks from JSON definitions - Part 7
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.ORANGE_BASKET, "crate_block/basket_oranges");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.ORANGE_BRICK_ARCH_SINGLE, "brick/orange/arch_single");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.ORANGE_BRICK_ARCH_DOUBLE, "brick/orange/arch_double");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.ORANGE_BRICK_DENTIL, "brick/orange/dentil");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.ORNATE_MARBLE, "marble/quartz/ornate/side");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.ORANGE_BRICK_ROWLOCK, "brick/orange/rowlock1");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.ORNATE_SANDSTONE, "ashlar_third/sandstone/ornate/side");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.PARQUET_FLOOR, "wood/oak/ornate");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.PILED_BONES, "piled_bones/side");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.PINK_SANDSTONE_ENGRAVED, "ashlar_engraved/sandy_pink/all");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.PISTON_TOP, "wood/oak/studded");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.POMEGRANATE_BASKET, "crate_block/basket_pomegranates");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.PURPLE_GRAPE_BASKET, "crate_block/basket_grape_purple");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.PURPLE_GRAPE_CRATE, "crate_block/crate_top_grape_purple");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.REACH_BRICK_ENGRAVED, "ashlar_engraved/reach/all");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.REACH_OAK_WOOD_PANELLING, "wood/oak/panelling/side1");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.RED_LANTERN2, "lighting/lantern_red_side");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.REACH_SPRUCE_WOOD_PANELLING, "wood/spruce/panelling/side1");

                // Additional Solid Blocks from JSON definitions - Part 8
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.REDORANGE_CARVED_SANDSTONE, "redorange_carved_sandstone/side");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.SALT_CRATE, "crate_block/crate_top_salt");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.SANDY_STONE_SLABS, "sandy_stone/sandy_stone_0");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.SEPT_CRYSTAL_LARGE, "crystal/all");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.SHOP_UTILITY_BLOCK, "utility_block/shop");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.SILVER_TIN_CRATE, "crate_block/crate_top_tin");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.SMALL_ORANGE_BRICKS_ORNATE_TOP, "brick/orange/all1");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.SMALL_ORANGE_BRICKS_ORNATE, "brick/orange/ornate/top_bottom");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.SMALL_SMOOTH_STONE_BRICK_BLUE_PLASTER, "ashlar_quarter_rounded/grey/all");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.SMALL_SMOOTH_STONE_BRICK_WHITE_PLASTER, "ashlar_quarter_rounded/grey/all");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.SMALL_STONE_BRICK_WHITE_PLASTER, "ashlar_quarter/green_grey/all");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.SMALL_WHITE_BRICK_BROWNISH_WHITE_PLASTER, "ashlar_quarter_rounded/white/all");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.SMALL_WHITE_BRICK_WHITE_PLASTER, "ashlar_quarter_rounded/white/all");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.SOURLEAF_BASKET, "crate_block/basket_sourleaf");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.SOURLEAF_CRATE, "crate_block/crate_sourleaf");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.SOUTHERN_BRICK_ARCH_FLAT, "brick/southern/arch_flat");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.SOUTHERN_BRICK_ARCH, "brick/southern/arch");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.SOUTHERN_BRICK_LINTEL, "brick/southern/lintel");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.SPECIAL_UTILITY_BLOCK, "utility_block/special");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.SPIT_ROAST, "meat_spitroast/all");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.SQUASH, "squash/top");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.STACKED_BONES_SOLID, "stacked_bones/bone_stacked_front");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.STORMLANDS_BRICK_ENGRAVED, "ashlar_engraved/stormlands/all");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.TABLE_BOOKS, "bench_block/table_drawer_books_side");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.TABLE_DRAWERS, "bench_block/table_drawers_side");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.TABLE_WIDGETS, "bench_block/table_drawer_widgets_side");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.TERRACOTTA_ENGRAVED, "ashlar_engraved/terracotta/all");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.THICK_GRASS_BLOCK, "grass_block/forest_top1");

                // Food blocks
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.TURNIP_BASKET, "crate_block/basket_turnip");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.TURNIP_CRATE, "crate_block/crate_top_turnip");

                // Plaster blocks
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.UNUSED_BROWN_PLASTER, "ashlar_quarter_rounded/light_brown/all");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.UNUSED_PURPLE_PLASTER, "ashlar_quarter_rounded/dark_red/all");

                // Engraved blocks
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.VIVID_DARK_SANDSTONE_ENGRAVED, "ashlar_engraved/dark_tan/all");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.VIVID_SANDSTONE_ENGRAVED, "ashlar_engraved/tan/all");

                // Barrel block
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.WATER_BARREL, "barrel_closed/barrel_top_closed");

                // Engraved white brick
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.WHITE_BRICK_ENGRAVED, "ashlar_engraved/white/all");

                // Grape baskets and crates
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.WHITE_GRAPE_BASKET, "crate_block/basket_grape_white");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.WHITE_GRAPE_CRATE, "crate_block/crate_top_grape_white");

                // Carving block
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.WINTERFELL_CARVING, "ashlar_third/dark_grey/carving/side");

                // Utility blocks
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.WIP_UTILITY_BLOCK, "utility_block/wip");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.WORKSHOP_UTILITY_BLOCK, "utility_block/workshop");
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.YARD_UTILITY_BLOCK, "utility_block/yard");

                // Timber blocks
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.TIMBER_NORTHERN_BLUE_BRESSUMMER, "plaster/smooth/gulltown_blue/all");

                // Clay block
                solidExporter.generateItemModels(itemModelGenerator, ModBlocks2.YELLOW_STAINED_CLAY, "clay/yellow_stained_clay");

                // Rail Blocks
                railExporter.generateItemModels(itemModelGenerator, ModBlocks2.FANCY_BLUE_CARPET, "carpet/fancy_blue_carpet");
                railExporter.generateItemModels(itemModelGenerator, ModBlocks2.FANCY_RED_CARPET, "carpet/fancy_red_carpet");
                railExporter.generateItemModels(itemModelGenerator, ModBlocks2.HORIZONTAL_CHAIN, "rail_block/chain");
                railExporter.generateItemModels(itemModelGenerator, ModBlocks2.HORIZONTAL_NET, "rail_block/net_large");
                railExporter.generateItemModels(itemModelGenerator, ModBlocks2.HORIZONTAL_ROPE, "rail_block/rope");
                railExporter.generateItemModels(itemModelGenerator, ModBlocks2.PACKED_SNOW, "rail_block/packed_snow");

                // Fire Blocks
                fireExporter.generateItemModels(itemModelGenerator, ModBlocks2.SAFE_FIRE, "safe_fire/fire_layer_0");
                fireExporter.generateItemModels(itemModelGenerator, ModBlocks2.WILDFIRE, "wildfire/wildfire_layer_0");

                // Door Blocks
                doorExporter.generateItemModels(itemModelGenerator, ModBlocks2.WHITE_WOOD_DOOR,
                                "westerosblocks:item/white_door");
                doorExporter.generateItemModels(itemModelGenerator, ModBlocks2.LOCKED_WHITE_WOOD_DOOR,
                                "westerosblocks:item/white_door");
                doorExporter.generateItemModels(itemModelGenerator, ModBlocks2.NORTHERN_WOOD_DOOR,
                                "westerosblocks:item/northern_door");
                doorExporter.generateItemModels(itemModelGenerator, ModBlocks2.LOCKED_DARK_NORTHERN_WOOD_DOOR,
                                "westerosblocks:item/northern_door");
                doorExporter.generateItemModels(itemModelGenerator, ModBlocks2.SPRUCE_DOOR,
                                "westerosblocks:item/spruce_door");
                doorExporter.generateItemModels(itemModelGenerator, ModBlocks2.LOCKED_SPRUCE_DOOR,
                                "westerosblocks:item/spruce_door");
                doorExporter.generateItemModels(itemModelGenerator, ModBlocks2.OAK_DOOR,
                                "westerosblocks:item/oak_door");
                doorExporter.generateItemModels(itemModelGenerator, ModBlocks2.LOCKED_OAK_DOOR,
                                "westerosblocks:item/oak_door");
                doorExporter.generateItemModels(itemModelGenerator, ModBlocks2.BIRCH_DOOR,
                                "westerosblocks:item/birch_door");
                doorExporter.generateItemModels(itemModelGenerator, ModBlocks2.LOCKED_BIRCH_DOOR,
                                "westerosblocks:item/birch_door");
                doorExporter.generateItemModels(itemModelGenerator, ModBlocks2.EYRIE_WEIRWOOD_DOOR,
                                "westerosblocks:item/moon_door");
                doorExporter.generateItemModels(itemModelGenerator, ModBlocks2.GREY_WOOD_DOOR,
                                "westerosblocks:item/grey_door");
                doorExporter.generateItemModels(itemModelGenerator, ModBlocks2.LOCKED_GREY_WOOD_DOOR,
                                "westerosblocks:item/grey_door");
                doorExporter.generateItemModels(itemModelGenerator, ModBlocks2.JUNGLE_DOOR,
                                "westerosblocks:item/jungle_door");
                doorExporter.generateItemModels(itemModelGenerator, ModBlocks2.LOCKED_JUNGLE_DOOR,
                                "westerosblocks:item/jungle_door");
                doorExporter.generateItemModels(itemModelGenerator, ModBlocks2.RED_KEEP_SECRET_DOOR,
                                "westerosblocks:item/red_keep_door");
                doorExporter.generateItemModels(itemModelGenerator, ModBlocks2.HARRENHAL_SECRET_DOOR,
                                "westerosblocks:item/harrenhal_door");

                // Standalone sand item models
                sandExport.generateItemModels(itemModelGenerator, ModBlocks2.SAND_SKELETON);

                // Coral Fan Blocks
                fanExporter.generateItemModels(itemModelGenerator, ModBlocks2.CORAL_BRAIN_FAN);
                fanExporter.generateItemModels(itemModelGenerator, ModBlocks2.CORAL_BUBBLE_FAN);
                fanExporter.generateItemModels(itemModelGenerator, ModBlocks2.CORAL_FIRE_FAN);
                fanExporter.generateItemModels(itemModelGenerator, ModBlocks2.CORAL_HORN_FAN);
                fanExporter.generateItemModels(itemModelGenerator, ModBlocks2.CORAL_TUBE_FAN);

                // Vines Blocks
                vinesExporter.generateItemModels(itemModelGenerator, ModBlocks2.DAPPLED_MOSS, "dappled_moss/dappled");
                vinesExporter.generateItemModels(itemModelGenerator, ModBlocks2.JASMINE_VINES, "jasmine_vines/side1");
                vinesExporter.generateItemModels(itemModelGenerator, ModBlocks2.VINES, "vines/side1");
                vinesExporter.generateItemModels(itemModelGenerator, ModBlocks2.FALLING_WATER_BLOCK_ONE,
                                "alyssas_tears_mist/mist1");
                vinesExporter.generateItemModels(itemModelGenerator, ModBlocks2.FALLING_WATER_BLOCK_TWO,
                                "alyssas_tears_mist/mist2");
                vinesExporter.generateItemModels(itemModelGenerator, ModBlocks2.FALLING_WATER_BLOCK_THREE,
                                "alyssas_tears_mist/mist3");
                vinesExporter.generateItemModels(itemModelGenerator, ModBlocks2.FALLING_WATER_BLOCK_FOUR,
                                "alyssas_tears_mist/mist4");

                // Half Door Blocks
                halfDoorExporter.generateItemModels(itemModelGenerator, ModBlocks2.BIRCH_WINDOW_SHUTTERS,
                                "wood/birch/shutters");
                halfDoorExporter.generateItemModels(itemModelGenerator, ModBlocks2.DORNE_RED_WINDOW_SHUTTERS,
                                "shutter_block/shutters_dorne");
                halfDoorExporter.generateItemModels(itemModelGenerator, ModBlocks2.GREEN_LANNISPORT_WINDOW_SHUTTERS,
                                "shutter_block/shutters_lannisport");
                halfDoorExporter.generateItemModels(itemModelGenerator, ModBlocks2.GREY_WOOD_WINDOW_SHUTTERS,
                                "wood/grey/shutters");
                halfDoorExporter.generateItemModels(itemModelGenerator, ModBlocks2.JUNGLE_WINDOW_SHUTTERS,
                                "wood/jungle/shutters");
                halfDoorExporter.generateItemModels(itemModelGenerator, ModBlocks2.NORTHERN_WOOD_WINDOW_SHUTTERS,
                                "wood/northern/shutters");
                halfDoorExporter.generateItemModels(itemModelGenerator, ModBlocks2.OAK_WINDOW_SHUTTERS,
                                "wood/oak/shutters");
                halfDoorExporter.generateItemModels(itemModelGenerator, ModBlocks2.REACH_BLUE_WINDOW_SHUTTERS,
                                "shutter_block/shutters_reach");
                halfDoorExporter.generateItemModels(itemModelGenerator, ModBlocks2.SPRUCE_WINDOW_SHUTTERS,
                                "wood/spruce/shutters");
                halfDoorExporter.generateItemModels(itemModelGenerator, ModBlocks2.WHITE_WOOD_WINDOW_SHUTTERS,
                                "wood/white/shutters");

                // Cross Blocks (Plant Blocks)
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.BLUE_BELLS, "flowers/blue_bells");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.BLUE_CHICORY,
                                "flowers/blue_chicory/side1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.BLUE_FLAX, "flowers/blue_flax1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.BLUE_FORGETMENOTS,
                                "flowers/blue_forgetmenots1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.BLUE_HYACINTH,
                                "flowers/blue_hyacinth1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.BLUE_ORCHID, "flowers/blue_orchid1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.BLUE_SWAMP_BELLS,
                                "flowers/blue_swamp_bells1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.BRACKEN, "bracken/side1");

                // Brown Mushroom Blocks
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.BROWN_MUSHROOM_1,
                                "brown_mushroom_block/mushroom_brown_0");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.BROWN_MUSHROOM_2,
                                "brown_mushroom_block/mushroom_brown_1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.BROWN_MUSHROOM_3,
                                "brown_mushroom_block/mushroom_brown_2");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.BROWN_MUSHROOM_4,
                                "brown_mushroom_block/mushroom_brown_3");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.BROWN_MUSHROOM_5,
                                "brown_mushroom_block/mushroom_brown_4");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.BROWN_MUSHROOM_6,
                                "brown_mushroom_block/mushroom_brown_5");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.BROWN_MUSHROOM_7,
                                "brown_mushroom_block/mushroom_brown_6");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.BROWN_MUSHROOM_8,
                                "brown_mushroom_block/mushroom_brown_7");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.BROWN_MUSHROOM_9,
                                "brown_mushroom_block/mushroom_brown_8");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.BROWN_MUSHROOM_10,
                                "brown_mushroom_block/mushroom_brown_9");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.BROWN_MUSHROOM_11,
                                "brown_mushroom_block/mushroom_brown_10");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.BROWN_MUSHROOM_12,
                                "brown_mushroom_block/mushroom_brown_11");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.BROWN_MUSHROOM_13,
                                "brown_mushroom_block/mushroom_brown_12");

                // Coral Web Blocks
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.CORAL_BRAIN_WEB, "coral/brain/web1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.CORAL_BUBBLE_WEB, "coral/bubble/web1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.CORAL_FIRE_WEB, "coral/fire/web1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.CORAL_HORN_WEB, "coral/horn/web1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.CORAL_TUBE_WEB, "coral/tube/web1");

                // Additional Plant Blocks
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.COW_PARSELY, "cow_parsely/side1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.CRANBERRY_BUSH, "cranberry/base1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.DEAD_BRACKEN, "dead_bracken/side1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.DEAD_BUSH, "dorne_bush_thorny/side1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.DEAD_SCRUB_GRASS,
                                "flowers/dead_scrub_grass1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.DOCK_LEAF, "dock_leaf/side1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.FIREWEED, "fireweed/side1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.GRASS, "minecraft:block/fern/fern1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.GREEN_LEAFY_HERB,
                                "flowers/green_leafy_herb");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.GREEN_SCRUB_GRASS,
                                "flowers/green_scrub_grass1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.GREEN_SPINY_HERB,
                                "flowers/green_spiny_herb1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.HEATHER, "flowers/purple_lavender1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.KELP, "kelp/side1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.LADY_FERN, "lady_fern/side1");

                // Flower Blocks
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.MAGENTA_ROSES,
                                "flowers/magenta_roses1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.MEADOW_FESCUE,
                                "flowers/meadow_fescue/side1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.NETTLE, "nettle/side1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.ORANGE_BELLS, "flowers/orange_bells1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.ORANGE_BOG_ASPHODEL,
                                "flowers/orange_bog_asphodel1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.ORANGE_MARIGOLDS,
                                "flowers/orange_marigolds1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.ORANGE_SUN_STAR,
                                "flowers/orange_sun_star1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.ORANGE_TROLLIUS,
                                "flowers/orange_trollius1");

                // Pink Flower Blocks
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.PINK_ALLIUM, "flowers/pink_allium1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.PINK_PRIMROSE,
                                "flowers/pink_primrose1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.PINK_ROSES, "flowers/pink_roses1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.PINK_SWEET_PEAS,
                                "flowers/pink_sweet_peas1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.PINK_THISTLE,
                                "flowers/pink_thistle/side1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.PINK_TULIPS, "flowers/pink_tulips1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.PINK_WILDFLOWERS,
                                "flowers/pink_wildflowers");

                // Purple Flower Blocks
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.PURPLE_ALPINE_SOWTHISTLE,
                                "flowers/purple_alpine_sowthistle/side1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.PURPLE_FOXGLOVE,
                                "flowers/purple_foxglove1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.PURPLE_LAVENDER,
                                "flowers/purple_lavender1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.PURPLE_PANSIES,
                                "flowers/purple_pansies1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.PURPLE_ROSES, "flowers/purple_roses1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.PURPLE_VIOLETS,
                                "flowers/purple_violets1");

                // Red Flower Blocks
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.RED_ASTER, "flowers/red_aster1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.RED_CARNATIONS,
                                "flowers/red_carnations1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.RED_CHRYSANTHEMUM,
                                "flowers/red_chrysanthemum1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.RED_DARK_ROSES,
                                "flowers/red_dark_roses1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.RED_FERN, "red_fern/side1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.RED_FLOWERING_SPINY_HERB,
                                "flowers/red_flowering_spiny_herb1");

                // Red Mushroom Blocks
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.RED_MUSHROOM_1,
                                "red_mushroom_block/mushroom_red_0");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.RED_MUSHROOM_2,
                                "red_mushroom_block/mushroom_red_1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.RED_MUSHROOM_3,
                                "red_mushroom_block/mushroom_red_2");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.RED_MUSHROOM_4,
                                "red_mushroom_block/mushroom_red_3");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.RED_MUSHROOM_5,
                                "red_mushroom_block/mushroom_red_4");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.RED_MUSHROOM_6,
                                "red_mushroom_block/mushroom_red_5");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.RED_MUSHROOM_7,
                                "red_mushroom_block/mushroom_red_6");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.RED_MUSHROOM_8,
                                "red_mushroom_block/mushroom_red_7");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.RED_MUSHROOM_9,
                                "red_mushroom_block/mushroom_red_8");

                // Remaining Red Flower Blocks
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.RED_POPPIES, "flowers/red_poppies1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.RED_ROSES, "flowers/red_roses1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.RED_SORREL, "flowers/red_sorrel1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.RED_SOURLEAF_BUSH,
                                "flowers/red_sourleaf_bush1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.RED_TULIPS, "flowers/red_tulips1");

                // Remaining Plant Blocks
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.STRAWBERRY_BUSH, "flowers/strawberry");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.THICK_GRASS,
                                "minecraft:block/grass/grass1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.UNSHADED_GRASS, "deadbush/side1");

                // White Flower Blocks
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.WHITE_CHAMOMILE,
                                "flowers/white_chamomile1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.WHITE_DAISIES,
                                "flowers/white_daisies1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.WHITE_LILYOFTHEVALLEY,
                                "flowers/white_lily_valley1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.WHITE_PEONY, "flowers/white_peony1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.WHITE_ROSES, "flowers/white_roses1");

                // Yellow Flower Blocks
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.YELLOW_BEDSTRAW,
                                "flowers/yellow_bedstraw/side1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.YELLOW_BELLS, "flowers/yellow_bells1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.YELLOW_BUTTERCUPS,
                                "flowers/yellow_buttercups1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.YELLOW_DAFFODILS,
                                "flowers/yellow_daffodils1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.YELLOW_DAISIES,
                                "flowers/yellow_daisies1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.YELLOW_DANDELIONS,
                                "flowers/yellow_dandelions1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.YELLOW_HELLEBORE,
                                "flowers/yellow_hellebore1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.YELLOW_LUPINE,
                                "flowers/yellow_lupine1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.YELLOW_ROSES, "flowers/yellow_roses1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.YELLOW_RUDBECKIA,
                                "flowers/yellow_rudbeckia1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.YELLOW_SUNFLOWER,
                                "flowers/yellow_sunflower/side1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.YELLOW_TANSY, "flowers/yellow_tansy");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.YELLOW_WILDFLOWERS,
                                "flowers/yellow_sunflower/side1");

                // Flower Pot Blocks
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_BLUE_BELLS);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_BLUE_CHICORY);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_BLUE_FLAX);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_BLUE_FORGETMENOTS);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_BLUE_HYACINTH);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_BLUE_ORCHID);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_BLUE_SWAMP_BELLS);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_BRACKEN);

                // Additional potted flowerpot blocks
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_BROWN_MUSHROOM_1);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_BROWN_MUSHROOM_3);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_BROWN_MUSHROOM_6);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_BROWN_MUSHROOM_13);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_CATTAILS);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_COW_PARSELY);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_DEAD_BRACKEN);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_DEAD_BUSH);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_DEAD_SCRUB_GRASS);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_DOCK_LEAF);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_FIREWEED);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_GRASS);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_GREEN_LEAFY_HERB);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_GREEN_SCRUB_GRASS);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_GREEN_SPINY_HERB);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_HEATHER);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_LADY_FERN);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_MAGENTA_ROSES);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_MEADOW_FESCUE);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_NETTLE);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_ORANGE_BELLS);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_ORANGE_BOG_ASPHODEL);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_ORANGE_MARIGOLDS);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_ORANGE_SUN_STAR);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_ORANGE_TROLLIUS);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_PINK_ALLIUM);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_PINK_PRIMROSE);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_PINK_ROSES);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_PINK_SWEET_PEAS);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_PINK_THISTLE);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_PINK_TULIPS);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_PINK_WILDFLOWERS);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_PURPLE_ALPINE_SOWTHISTLE);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_PURPLE_FOXGLOVE);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_PURPLE_LAVENDER);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_PURPLE_PANSIES);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_PURPLE_ROSES);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_PURPLE_VIOLETS);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_RED_ASTER);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_RED_CARNATIONS);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_RED_CHRYSANTHEMUM);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_RED_DARK_ROSES);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_RED_FERN);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_RED_FLOWERING_SPINY_HERB);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_RED_MUSHROOM_1);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_RED_MUSHROOM_2);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_RED_MUSHROOM_3);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_RED_MUSHROOM_7);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_RED_MUSHROOM_8);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_RED_MUSHROOM_9);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_RED_POPPIES);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_RED_ROSES);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_RED_SORREL);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_RED_SOURLEAF_BUSH);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_RED_TULIPS);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_WHITE_CHAMOMILE);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_WHITE_DAISIES);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_WHITE_LILYOFTHEVALLEY);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_WHITE_PEONY);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_WHITE_ROSES);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_YELLOW_BEDSTRAW);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_YELLOW_BELLS);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_YELLOW_BUTTERCUPS);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_YELLOW_DAFFODILS);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_YELLOW_DAISIES);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_YELLOW_DANDELIONS);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_YELLOW_HELLEBORE);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_YELLOW_LUPINE);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_YELLOW_ROSES);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_YELLOW_RUDBECKIA);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_YELLOW_SUNFLOWER);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_YELLOW_TANSY);
                flowerPotExporter.generateItemModels(itemModelGenerator, ModBlocks2.POTTED_YELLOW_WILDFLOWERS);

                // Crop Blocks
                cropExporter.generateItemModels(itemModelGenerator, ModBlocks2.CANDLE_ALTAR,
                                "lighting/candle_altar/lit1");
                cropExporter.generateItemModels(itemModelGenerator, ModBlocks2.CROP_CARROTS, "carrots/carrots_stage_0");
                cropExporter.generateItemModels(itemModelGenerator, ModBlocks2.CROP_PEAS, "peas/peas_stage_0");
                cropExporter.generateItemModels(itemModelGenerator, ModBlocks2.CROP_TURNIPS, "turnips/turnips_stage_0");
                cropExporter.generateItemModels(itemModelGenerator, ModBlocks2.CROP_WHEAT, "wheat/wheat_stage_0");
                cropExporter.generateItemModels(itemModelGenerator, ModBlocks2.SEAGRASS, "seagrass/side1");

                // Web Blocks
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.ALYSSAS_TEARS_MIST_ONE,
                                "alyssas_tears_mist/mist1");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.ALYSSAS_TEARS_MIST_TWO,
                                "alyssas_tears_mist/mist2");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.ALYSSAS_TEARS_MIST_THREE,
                                "alyssas_tears_mist/mist3");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.ALYSSAS_TEARS_MIST_FOUR,
                                "alyssas_tears_mist/mist4");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.BLACK_BRICICLE,
                                "ashlar_melted/black/bricicle/side");
                crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.BEES, "web_block/bug_bees");

                // Additional web blocks item models
                webExporter.generateItemModels(itemModelGenerator, ModBlocks2.BUSHEL_OF_HERBS, "web_block/food_herbs");
                webExporter.generateItemModels(itemModelGenerator, ModBlocks2.BUSHEL_OF_SOURLEAF,
                                "web_block/food_sourleaf");
                webExporter.generateItemModels(itemModelGenerator, ModBlocks2.BUTTERFLY_BLUE,
                                "web_block/bug_butterfly_blue");
                webExporter.generateItemModels(itemModelGenerator, ModBlocks2.BUTTERFLY_ORANGE,
                                "web_block/bug_butterfly_orange");
                webExporter.generateItemModels(itemModelGenerator, ModBlocks2.BUTTERFLY_RED,
                                "web_block/bug_butterfly_red");
                webExporter.generateItemModels(itemModelGenerator, ModBlocks2.BUTTERFLY_WHITE,
                                "web_block/bug_butterfly_white");
                webExporter.generateItemModels(itemModelGenerator, ModBlocks2.BUTTERFLY_YELLOW,
                                "web_block/bug_butterfly_yellow");
                webExporter.generateItemModels(itemModelGenerator, ModBlocks2.CATTAILS, "cattails/side1");
                webExporter.generateItemModels(itemModelGenerator, ModBlocks2.CHAIN_BLOCK_HARNESS,
                                "web_block/chain_blockharness");
                webExporter.generateItemModels(itemModelGenerator, ModBlocks2.CHILI_RISTRA,
                                "web_block/food_chili_ristra");
                webExporter.generateItemModels(itemModelGenerator, ModBlocks2.COBWEB, "cobweb/side1");
                webExporter.generateItemModels(itemModelGenerator, ModBlocks2.DEAD_FISH, "dead_fish/fish_dead1");
                webExporter.generateItemModels(itemModelGenerator, ModBlocks2.DEAD_FOWL, "dead_fowl/fowl");
                webExporter.generateItemModels(itemModelGenerator, ModBlocks2.DEAD_FROG, "dead_frog/toad_dead1");
                webExporter.generateItemModels(itemModelGenerator, ModBlocks2.DEAD_HARE, "dead_hare/rabbit_dead1");
                webExporter.generateItemModels(itemModelGenerator, ModBlocks2.DEAD_JUNGLE_TALL_GRASS,
                                "dead_jungle_tall_grass/down_side1");
                webExporter.generateItemModels(itemModelGenerator, ModBlocks2.DEAD_RAT, "dead_rat/rat1");
                webExporter.generateItemModels(itemModelGenerator, ModBlocks2.DEAD_SAVANNA_TALL_GRASS,
                                "dead_savanna_tall_grass/dead_savanna_tall_grass");
                webExporter.generateItemModels(itemModelGenerator, ModBlocks2.DRAGONFLY, "web_block/bug_dragonfly");
                webExporter.generateItemModels(itemModelGenerator, ModBlocks2.FLIES, "web_block/bug_flies");
                webExporter.generateItemModels(itemModelGenerator, ModBlocks2.GARLIC_STRAND,
                                "web_block/food_garlic_strand");
                webExporter.generateItemModels(itemModelGenerator, ModBlocks2.ICICLE, "icicle/side");
                webExporter.generateItemModels(itemModelGenerator, ModBlocks2.IRON_THRONE_RANDOM_BLADES,
                                "blades_random/side1");
                webExporter.generateItemModels(itemModelGenerator, ModBlocks2.JUNGLE_TALL_FERN,
                                "jungle_tall_fern/side");
                webExporter.generateItemModels(itemModelGenerator, ModBlocks2.JUNGLE_TALL_GRASS,
                                "jungle_tall_grass/down_side1");
                webExporter.generateItemModels(itemModelGenerator, ModBlocks2.ROPE_BLOCK_HARNESS,
                                "web_block/rope_blockharness");
                webExporter.generateItemModels(itemModelGenerator, ModBlocks2.SAUSAGES_LEG_OF_HAM,
                                "sausages_leg_of_ham/default");
                webExporter.generateItemModels(itemModelGenerator, ModBlocks2.SAVANNA_TALL_GRASS,
                                "savanna_tall_grass/savanna_tall_grass");
                webExporter.generateItemModels(itemModelGenerator, ModBlocks2.SMOKE, "smoke/smoke1");
                webExporter.generateItemModels(itemModelGenerator, ModBlocks2.VERTICAL_CHAIN,
                                "web_block/chain_vertical");
                webExporter.generateItemModels(itemModelGenerator, ModBlocks2.VERTICAL_ROPE, "web_block/rope_vertical");
        }
}