package com.westerosblocks.datagen;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;

public class ModLanguageProvider extends FabricLanguageProvider {

        public ModLanguageProvider(FabricDataOutput dataOutput,
                        CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
                super(dataOutput, "en_us", registryLookup);
        }

        @Override
        public void generateTranslations(WrapperLookup registryLookup, TranslationBuilder translationBuilder) {
                translationBuilder.add("block.westerosblocks.6sided_birch", "Six-Sided Birch");
                translationBuilder.add("block.westerosblocks.6sided_jungle", "Six-Sided Jungle");
                translationBuilder.add("block.westerosblocks.6sided_oak", "Six-Sided Oak");
                translationBuilder.add("block.westerosblocks.6sided_spruce", "Six-Sided Spruce");
                translationBuilder.add("block.westerosblocks.6sided_stone_slab", "Six-Sided Stone Slab");
                translationBuilder.add("block.westerosblocks.apple_basket", "Apple Basket");
                translationBuilder.add("block.westerosblocks.apple_crate", "Apple Crate");
                translationBuilder.add("block.westerosblocks.approval_utility_block", "'Approval' Utility Block");
                translationBuilder.add("block.westerosblocks.arbor_brick_ornate", "Ashlar Engraved Arbor");
                translationBuilder.add("block.westerosblocks.bench_butcher_knives", "Bench Butcher Knives");
                translationBuilder.add("block.westerosblocks.bench_carpentry_hammer_saw", "Bench Carpentry Hammer Saw");
                translationBuilder.add("block.westerosblocks.bench_drawers", "Bench Drawers");
                translationBuilder.add("block.westerosblocks.bench_kitchen_knives", "Bench Kitchen Knives");
                translationBuilder.add("block.westerosblocks.bench_kitchen_pans", "Bench Kitchen Pans");
                translationBuilder.add("block.westerosblocks.bench_mason_hammer_mallet", "Bench Mason Hammer Mallet");
                translationBuilder.add("block.westerosblocks.berry_basket", "Berry Basket");
                translationBuilder.add("block.westerosblocks.berry_crate", "Berry Crate");
                translationBuilder.add("block.westerosblocks.apricot_basket", "Apricot Basket");
                translationBuilder.add("block.westerosblocks.black_brick_engraved", "Ashlar Engraved Black");
                translationBuilder.add("block.westerosblocks.bluegreen_carved_sandstone",
                                "Blue/Green Carved Sandstone");
                translationBuilder.add("block.westerosblocks.bone_dirt", "Bone Dirt");
                translationBuilder.add("block.westerosblocks.bookshelf_abandoned", "Bookshelf Abandoned");
                translationBuilder.add("block.westerosblocks.bookshelf_library", "Bookshelf Library");
                translationBuilder.add("block.westerosblocks.bookshelf_maester", "Bookshelf Maester");
                translationBuilder.add("block.westerosblocks.broken_cabinet", "Broken Cabinet");
                translationBuilder.add("block.westerosblocks.brown_grey_brick_engraved", "Ashlar Engraved Brown Grey");
                translationBuilder.add("block.westerosblocks.cabinet_drawer", "Cabinet Drawer");
                translationBuilder.add("block.westerosblocks.cage", "Cage");
                translationBuilder.add("block.westerosblocks.carrot_basket", "Carrot Basket");
                translationBuilder.add("block.westerosblocks.carrot_crate", "Carrot Crate");
                translationBuilder.add("block.westerosblocks.closed_basket", "Closed Basket");
                translationBuilder.add("block.westerosblocks.closed_cabinet", "Closed Cabinet");
                translationBuilder.add("block.westerosblocks.coarse_dark_red_carved_sandstone",
                                "Ashlar Engraved Pale Dark Red");
                translationBuilder.add("block.westerosblocks.coarse_red_carved_sandstone", "Ashlar Engraved Pale Red");
                translationBuilder.add("block.westerosblocks.cobble_keystone", "Cobblestone Keystone Grey");
                translationBuilder.add("block.westerosblocks.coloured_sept_window", "Coloured Sept Window");
                translationBuilder.add("block.westerosblocks.crate", "Crate One");
                translationBuilder.add("block.westerosblocks.crate2", "Crate Two");
                translationBuilder.add("block.westerosblocks.crate3", "Crate Three");
                translationBuilder.add("block.westerosblocks.dark_grey_brick_engraved", "Ashlar Engraved Dark Grey");
                translationBuilder.add("block.westerosblocks.date_basket", "Date Basket");
                translationBuilder.add("block.westerosblocks.dates", "Dates");
                translationBuilder.add("block.westerosblocks.desert_sandstone_engraved", "Ashlar Engraved Sandstone");
                translationBuilder.add("block.westerosblocks.domestic_utility_block", "'Domestic' Utility Block");
                translationBuilder.add("block.westerosblocks.done_utility_block", "'Done' Utility Block");
                translationBuilder.add("block.westerosblocks.dragon_carving", "Dragon Carving");
                translationBuilder.add("block.westerosblocks.empty_barrel", "Empty Barrel");
                translationBuilder.add("block.westerosblocks.empty_cabinet", "Empty Cabinet");
                translationBuilder.add("block.westerosblocks.faith_carved_arbor_brick", "Ashlar Faith Carved Arbor");
                translationBuilder.add("block.westerosblocks.faith_carved_black_brick", "Ashlar Faith Carved Black");
                translationBuilder.add("block.westerosblocks.faith_carved_brown_grey_brick",
                                "Ashlar Faith Carved Brown Grey");
                translationBuilder.add("block.westerosblocks.faith_carved_coarse_red_brick",
                                "Ashlar Faith Carved Pale Red");
                translationBuilder.add("block.westerosblocks.faith_carved_dark_grey_brick",
                                "Ashlar Faith Carved Dark Grey");
                translationBuilder.add("block.westerosblocks.faith_carved_dun_brick", "Ashlar Faith Carved Dun");
                translationBuilder.add("block.westerosblocks.faith_carved_grey_brick",
                                "Ashlar Faith Carved Normal Grey");
                translationBuilder.add("block.westerosblocks.faith_carved_oldtown_brick",
                                "Ashlar Faith Carved Light Oldtown");
                translationBuilder.add("block.westerosblocks.faith_carved_pink_sandstone",
                                "Ashlar Faith Carved Sandy Pink");
                translationBuilder.add("block.westerosblocks.faith_carved_reach_brick", "Ashlar Faith Carved Reach");
                translationBuilder.add("block.westerosblocks.faith_carved_small_stone_brick",
                                "Ashlar Faith Carved Green Grey");
                translationBuilder.add("block.westerosblocks.faith_carved_stone_brick", "Ashlar Faith Carved White");
                translationBuilder.add("block.westerosblocks.faith_carved_stormlands_brick",
                                "Ashlar Faith Carved Stormlands");
                translationBuilder.add("block.westerosblocks.faith_carved_westerlands_brick",
                                "Ashlar Faith Carved Westerlands");
                translationBuilder.add("block.westerosblocks.fish_barrel", "Fish Barrel");
                translationBuilder.add("block.westerosblocks.fish_basket", "Fish Basket");
                translationBuilder.add("block.westerosblocks.fish_trap", "Fish Trap");
                translationBuilder.add("block.westerosblocks.flagstone", "Flagstone");
                translationBuilder.add("block.westerosblocks.full_cabinet", "Full Cabinet");
                translationBuilder.add("block.westerosblocks.glowing_embers", "Glowing Embers");
                translationBuilder.add("block.westerosblocks.grain_basket", "Grain Basket");
                translationBuilder.add("block.westerosblocks.grain_crate", "Grain Crate");
                translationBuilder.add("block.westerosblocks.green_grey_brick_engraved", "Ashlar Engraved Green Grey");
                translationBuilder.add("block.westerosblocks.grey_brick_engraved", "Ashlar Engraved Normal Grey");
                translationBuilder.add("block.westerosblocks.grey_keystone", "Grey Keystone");
                translationBuilder.add("block.westerosblocks.high_class_utility_block", "'High Class' Utility Block");
                translationBuilder.add("block.westerosblocks.hop_basket", "Hop Basket");
                translationBuilder.add("block.westerosblocks.hop_crate", "Hop Crate");
                translationBuilder.add("block.westerosblocks.house_count_utility_block", "'House Count' Utility Block");
                translationBuilder.add("block.westerosblocks.industry_utility_block", "'Industry' Utility Block");
                translationBuilder.add("block.westerosblocks.iron_crate", "Iron Crate");
                translationBuilder.add("block.westerosblocks.kl_dun_carved_brick", "Ashlar Engraved Dun");
                translationBuilder.add("block.westerosblocks.lannisport_keystone_orange_plaster",
                                "Lannisport Keystone Orange Plaster");
                translationBuilder.add("block.westerosblocks.lannisport_keystone_yellow_plaster",
                                "Lannisport Keystone Yellow Plaster");
                translationBuilder.add("block.westerosblocks.large_clay_pot_solid", "Large Clay Pot Solid");
                translationBuilder.add("block.westerosblocks.lavender_basket", "Lavender Basket");
                translationBuilder.add("block.westerosblocks.lavender_crate", "Lavender Crate");
                translationBuilder.add("block.westerosblocks.lemon_basket", "Lemon Basket");
                translationBuilder.add("block.westerosblocks.light_grey_brick_engraved", "Ashlar Engraved Light Grey");
                translationBuilder.add("block.westerosblocks.light_grey_stone_white_plaster",
                                "Light Grey Brick Keystone White Plaster");
                translationBuilder.add("block.westerosblocks.light_oldtown_brick_engraved",
                                "Ashlar Engraved Light Oldtown");
                translationBuilder.add("block.westerosblocks.lime_basket", "Lime Basket");
                translationBuilder.add("block.westerosblocks.low_class_utility_block", "'Low Class' Utility Block");
                translationBuilder.add("block.westerosblocks.middle_class_utility_block",
                                "'Middle Class' Utility Block");
                translationBuilder.add("block.westerosblocks.mirror_block", "Mirror Block");
                translationBuilder.add("block.westerosblocks.monochrome_dark_sandstone_engraved",
                                "Ashlar Engraved Light Brown");
                translationBuilder.add("block.westerosblocks.monochrome_sandstone_engraved",
                                "Ashlar Engraved Westerlands");
                translationBuilder.add("block.westerosblocks.nether_brick_keystone", "Ashlar Embellished Black");
                translationBuilder.add("block.westerosblocks.northern_carvings", "Northern Carvings");
                translationBuilder.add("block.westerosblocks.note_utility_block", "'Note' Utility Block");
                translationBuilder.add("block.westerosblocks.olive_basket", "Olive Basket");
                translationBuilder.add("block.westerosblocks.open_basket", "Open Basket");
                translationBuilder.add("block.westerosblocks.open_crate", "Open Crate");
                translationBuilder.add("block.westerosblocks.orange_basket", "Orange Basket");
                translationBuilder.add("block.westerosblocks.orange_brick_arch_double", "Orange Brick Arch Double");
                translationBuilder.add("block.westerosblocks.orange_brick_arch_single", "Orange Brick Arch Single");
                translationBuilder.add("block.westerosblocks.orange_brick_dentil", "Orange Brick Dentil");
                translationBuilder.add("block.westerosblocks.orange_brick_rowlock", "Orange Brick Rowlock");
                translationBuilder.add("block.westerosblocks.ornate_marble", "Ornate Marble");
                translationBuilder.add("block.westerosblocks.ornate_sandstone", "Ornate Sandstone");
                translationBuilder.add("block.westerosblocks.parquet_floor", "Parquet Floor");
                translationBuilder.add("block.westerosblocks.piled_bones", "Piled Bones");
                translationBuilder.add("block.westerosblocks.pink_sandstone_engraved", "Ashlar Engraved Sandy Pink");
                translationBuilder.add("block.westerosblocks.piston_top", "Piston Top");
                translationBuilder.add("block.westerosblocks.pomegranate_basket", "Pomegranate Basket");
                translationBuilder.add("block.westerosblocks.purple_grape_basket", "Purple Grape Basket");
                translationBuilder.add("block.westerosblocks.purple_grape_crate", "Purple Grape Crate");
                translationBuilder.add("block.westerosblocks.reach_brick_engraved", "Ashlar Engraved Reach");
                translationBuilder.add("block.westerosblocks.reach_oak_wood_panelling", "Reach Oak Wood Panelling");
                translationBuilder.add("block.westerosblocks.redorange_carved_sandstone",
                                "Red/Orange Carved Sandstone");
                translationBuilder.add("block.westerosblocks.red_lantern2", "Large Red Lantern");
                translationBuilder.add("block.westerosblocks.reach_spruce_wood_panelling",
                                "Reach Spruce Wood Panelling");
                translationBuilder.add("block.westerosblocks.salt_crate", "Salt Crate");
                translationBuilder.add("block.westerosblocks.sandy_stone_slabs", "Sandy Stone Slabs");
                translationBuilder.add("block.westerosblocks.sept_crystal_large", "Sept Crystal Large");
                translationBuilder.add("block.westerosblocks.shop_utility_block", "'Shop' Utility Block");
                translationBuilder.add("block.westerosblocks.silver_tin_crate", "Silver Tin Crate");
                translationBuilder.add("block.westerosblocks.small_orange_bricks_ornate_top",
                                "Small Orange Bricks Ornate Top");
                translationBuilder.add("block.westerosblocks.small_orange_bricks_ornate", "Small Orange Bricks Ornate");
                translationBuilder.add("block.westerosblocks.small_smooth_stone_brick_blue_plaster",
                                "Small Smooth Stone Brick Blue Plaster");
                translationBuilder.add("block.westerosblocks.small_smooth_stone_brick_white_plaster",
                                "Small Smooth Stone Brick White Plaster");
                translationBuilder.add("block.westerosblocks.small_stone_brick_white_plaster",
                                "Small Stone Brick White Plaster");
                translationBuilder.add("block.westerosblocks.small_white_brick_brownish_white_plaster",
                                "Small White Brick Brownish White Plaster");
                translationBuilder.add("block.westerosblocks.small_white_brick_white_plaster",
                                "Small White Brick White Plaster");
                translationBuilder.add("block.westerosblocks.sourleaf_basket", "Sourleaf Basket");
                translationBuilder.add("block.westerosblocks.sourleaf_crate", "Sourleaf Crate");
                translationBuilder.add("block.westerosblocks.southern_brick_arch_flat", "Southern Brick Arch Flat");
                translationBuilder.add("block.westerosblocks.southern_brick_arch", "Southern Brick Arch");
                translationBuilder.add("block.westerosblocks.southern_brick_lintel", "Southern Brick Lintel");
                translationBuilder.add("block.westerosblocks.special_utility_block", "'Special' Utility Block");
                translationBuilder.add("block.westerosblocks.spit_roast", "Spit Roast");
                translationBuilder.add("block.westerosblocks.squash", "Squash");
                translationBuilder.add("block.westerosblocks.stacked_bones_solid", "Stacked Bones Solid");
                translationBuilder.add("block.westerosblocks.stormlands_brick_engraved", "Ashlar Engraved Stormlands");
                translationBuilder.add("block.westerosblocks.table_books", "Table Books");
                translationBuilder.add("block.westerosblocks.table_drawers", "Table Drawers");
                translationBuilder.add("block.westerosblocks.thick_grass_block", "Thick Grass Block");
                translationBuilder.add("block.westerosblocks.table_widgets", "Table Widgets");
                translationBuilder.add("block.westerosblocks.oak_table", "Oak Table");
                translationBuilder.add("block.westerosblocks.terracotta_engraved", "Ashlar Engraved Terracotta");
                translationBuilder.add("block.westerosblocks.turnip_basket", "Turnip Basket");
                translationBuilder.add("block.westerosblocks.turnip_crate", "Turnip Crate");
                translationBuilder.add("block.westerosblocks.unused_brown_plaster",
                                "Smooth Brown Brick Keystone Brownish White Plaster");
                translationBuilder.add("block.westerosblocks.unused_purple_plaster",
                                "Smooth Purple Brick Keystone Brownish White Plaster");
                translationBuilder.add("block.westerosblocks.vivid_dark_sandstone_engraved",
                                "Ashlar Engraved Dark Tan");
                translationBuilder.add("block.westerosblocks.vivid_sandstone_engraved", "Ashlar Engraved Tan");
                translationBuilder.add("block.westerosblocks.water_barrel", "Water Barrel");
                translationBuilder.add("block.westerosblocks.white_brick_engraved", "Ashlar Engraved White");
                translationBuilder.add("block.westerosblocks.white_grape_basket", "White Grape Basket");
                translationBuilder.add("block.westerosblocks.white_grape_crate", "White Grape Crate");
                translationBuilder.add("block.westerosblocks.winterfell_carving", "Winterfell Carving");
                translationBuilder.add("block.westerosblocks.wip_utility_block", "'WIP' Utility Block");
                translationBuilder.add("block.westerosblocks.workshop_utility_block", "'Workshop' Utility Block");
                translationBuilder.add("block.westerosblocks.yard_utility_block", "'Yard' Utility Block");
                translationBuilder.add("block.westerosblocks.yellow_stained_clay", "Yellow Stained Clay");
                translationBuilder.add("block.westerosblocks.archery_target", "Archery Target");
                translationBuilder.add("block.westerosblocks.closed_barrel", "Closed Barrel");
                translationBuilder.add("block.westerosblocks.firewood", "Firewood");
                translationBuilder.add("block.westerosblocks.jungle_log_chain", "Jungle Log Chain");
                translationBuilder.add("block.westerosblocks.jungle_log_rope", "Jungle Log Rope");
                translationBuilder.add("block.westerosblocks.marble_pillar_vertical_ctm", "Marble Pillar Vertical");
                translationBuilder.add("block.westerosblocks.marble_pillar", "Marble Pillar");
                translationBuilder.add("block.westerosblocks.mossy_birch_log", "Mossy Birch Log");
                translationBuilder.add("block.westerosblocks.mossy_jungle_log", "Mossy Jungle Log");
                translationBuilder.add("block.westerosblocks.mossy_oak_log", "Mossy Oak Log");
                translationBuilder.add("block.westerosblocks.mossy_spruce_log", "Mossy Spruce Log");
                translationBuilder.add("block.westerosblocks.oak_log_chain", "Oak Log Chain");
                translationBuilder.add("block.westerosblocks.oak_log_rope", "Oak Log Rope");
                translationBuilder.add("block.westerosblocks.palm_tree_log", "Palm Tree Log");
                translationBuilder.add("block.westerosblocks.sandstone_pillar", "Sandstone Pillar");
                translationBuilder.add("block.westerosblocks.spruce_log_chain", "Spruce Log Chain");
                translationBuilder.add("block.westerosblocks.spruce_log_rope", "Spruce Log Rope");
                translationBuilder.add("block.westerosblocks.stacked_bones", "Stacked Bones");
                translationBuilder.add("block.westerosblocks.weirwood_face_0", "Weirwood Face 0");
                translationBuilder.add("block.westerosblocks.weirwood_face_1", "Weirwood Face 1");
                translationBuilder.add("block.westerosblocks.weirwood_face_2", "Weirwood Face 2");
                translationBuilder.add("block.westerosblocks.weirwood_face_3", "Weirwood Face 3");
                translationBuilder.add("block.westerosblocks.weirwood_face_4", "Weirwood Face 4");
                translationBuilder.add("block.westerosblocks.weirwood_face_5", "Weirwood Face 5");
                translationBuilder.add("block.westerosblocks.weirwood_face_6", "Weirwood Face 6");
                translationBuilder.add("block.westerosblocks.weirwood_face_7", "Weirwood Face 7");
                translationBuilder.add("block.westerosblocks.weirwood_face_8", "Weirwood Face 8");
                translationBuilder.add("block.westerosblocks.weirwood_scars", "Weirwood Scars");
                translationBuilder.add("block.westerosblocks.apple_basket_slab", "Apple Basket Slab");
                translationBuilder.add("block.westerosblocks.apricot_basket_slab", "Apricot Basket Slab");
                translationBuilder.add("block.westerosblocks.closed_basket_slab", "Closed Basket Slab");
                translationBuilder.add("block.westerosblocks.berry_basket_slab", "Berry Basket Slab");
                translationBuilder.add("block.westerosblocks.carrot_basket_slab", "Carrot Basket Slab");
                translationBuilder.add("block.westerosblocks.cut_grain_flour_sack", "Cut Grain Flour Sack");
                translationBuilder.add("block.westerosblocks.date_basket_slab", "Date Basket Slab");
                translationBuilder.add("block.westerosblocks.firewood_slab", "Firewood Slab");
                translationBuilder.add("block.westerosblocks.fish_basket_slab", "Fish Basket Slab");
                translationBuilder.add("block.westerosblocks.grain_basket_slab", "Grain Basket Slab");
                translationBuilder.add("block.westerosblocks.grain_flour_sack", "Grain Flour Sack");
                translationBuilder.add("block.westerosblocks.hop_basket_slab", "Hop Basket Slab");
                translationBuilder.add("block.westerosblocks.timber_northern_blue_bressummer",
                                "Timber Northern Blue Bressummer");
                translationBuilder.add("block.westerosblocks.timber_northern_green_lefthatch",
                                "Timber Northern Green Lefthatch");
                translationBuilder.add("block.westerosblocks.oak_branch", "Oak Branch");
                translationBuilder.add("block.westerosblocks.birch_branch", "Birch Branch");
                translationBuilder.add("block.westerosblocks.eyrie_weirwood_door", "Eyrie Weirwood Door");
                translationBuilder.add("block.westerosblocks.birch_door", "Birch Door");
                translationBuilder.add("block.westerosblocks.grey_wood_door", "Grey Wood Door");
                translationBuilder.add("block.westerosblocks.harrenhal_secret_door", "Harrenhal Secret Door");
                translationBuilder.add("block.westerosblocks.jungle_door", "Jungle Door");
                translationBuilder.add("block.westerosblocks.locked_birch_door", "Locked Birch Door");
                translationBuilder.add("block.westerosblocks.locked_dark_northern_wood_door",
                                "Locked Dark Northern Wood Door");
                translationBuilder.add("block.westerosblocks.locked_grey_wood_door", "Locked Grey Wood Door");
                translationBuilder.add("block.westerosblocks.locked_jungle_door", "Locked Jungle Door");
                translationBuilder.add("block.westerosblocks.locked_oak_door", "Locked Oak Door");
                translationBuilder.add("block.westerosblocks.locked_spruce_door", "Locked Spruce Door");
                translationBuilder.add("block.westerosblocks.locked_white_wood_door", "Locked White Wood Door");
                translationBuilder.add("block.westerosblocks.northern_wood_door", "Northern Wood Door");
                translationBuilder.add("block.westerosblocks.oak_door", "Oak Door");
                translationBuilder.add("block.westerosblocks.red_keep_secret_door", "Red Keep Secret Door");
                translationBuilder.add("block.westerosblocks.spruce_door", "Spruce Door");
                translationBuilder.add("block.westerosblocks.white_wood_door", "White Wood Door");
                
                // Half Door Blocks (Shutters)
                translationBuilder.add("block.westerosblocks.birch_window_shutters", "Birch Window Shutters");
                translationBuilder.add("block.westerosblocks.dorne_red_window_shutters", "Dorne Red Window Shutters");
                translationBuilder.add("block.westerosblocks.green_lannisport_window_shutters", "Green Lannisport Window Shutters");
                translationBuilder.add("block.westerosblocks.grey_wood_window_shutters", "Grey Wood Window Shutters");
                translationBuilder.add("block.westerosblocks.jungle_window_shutters", "Jungle Window Shutters");
                translationBuilder.add("block.westerosblocks.northern_wood_window_shutters", "Northern Wood Window Shutters");
                translationBuilder.add("block.westerosblocks.oak_window_shutters", "Oak Window Shutters");
                translationBuilder.add("block.westerosblocks.reach_blue_window_shutters", "Reach Blue Window Shutters");
                translationBuilder.add("block.westerosblocks.spruce_window_shutters", "Spruce Window Shutters");
                translationBuilder.add("block.westerosblocks.white_wood_window_shutters", "White Wood Window Shutters");
                
                // Pane Blocks
                translationBuilder.add("block.westerosblocks.dorne_carved_stone_window", "Dorne Carved Stone Window");
                translationBuilder.add("block.westerosblocks.dorne_carved_wooden_window", "Dorne Carved Wooden Window");
                translationBuilder.add("block.westerosblocks.iron_bars", "Iron Bars");
                translationBuilder.add("block.westerosblocks.iron_crossbar", "Iron Crossbar");
                translationBuilder.add("block.westerosblocks.oxidized_iron_bars", "Oxidized Iron Bars");
                translationBuilder.add("block.westerosblocks.oxidized_iron_crossbar", "Oxidized Iron Crossbar");
                translationBuilder.add("block.westerosblocks.vertical_net", "Vertical Net");

                // Torch Blocks
                translationBuilder.add("block.westerosblocks.torch", "Torch");
                translationBuilder.add("block.westerosblocks.torch_unlit", "Unlit Torch");
                translationBuilder.add("block.westerosblocks.candle", "Candle");
                translationBuilder.add("block.westerosblocks.candle_unlit", "Unlit Candle");

                // Chair Blocks
                translationBuilder.add("block.westerosblocks.oak_chair", "Oak Chair");

                // Arrow Slit Blocks
                translationBuilder.add("block.westerosblocks.arbor_brick_arrow_slit", "Arbor Brick Arrow Slit");

                // Rail Blocks
                translationBuilder.add("block.westerosblocks.fancy_blue_carpet", "Fancy Blue Carpet");
                translationBuilder.add("block.westerosblocks.fancy_red_carpet", "Fancy Red Carpet");
                translationBuilder.add("block.westerosblocks.horizontal_chain", "Horizontal Chain");
                translationBuilder.add("block.westerosblocks.horizontal_net", "Horizontal Net");
                translationBuilder.add("block.westerosblocks.horizontal_rope", "Horizontal Rope");
                translationBuilder.add("block.westerosblocks.packed_snow", "Packed Snow");
        }

}
