package com.westerosblocks.datagen;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.WesterosBlocksDefLoader;
import com.westerosblocks.block.ModBlocks;
import com.westerosblocks.block.ModBlock;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryWrapper;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ModLanguageProvider extends FabricLanguageProvider {
    Map<String, Block> customBlocks = ModBlocks.getCustomBlocks();
    ModBlock[] customBlockDefs = WesterosBlocksDefLoader.getCustomBlockDefs();

    public ModLanguageProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, "en_us", registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup registryLookup, TranslationBuilder translationBuilder) {
        // do custom block translations
        for (ModBlock customBlockDef : customBlockDefs) {
            if (customBlockDef == null) continue;
            Block currentBlock = customBlocks.get(customBlockDef.blockName);

            translationBuilder.add(currentBlock, customBlockDef.label);

            // tooltips
            if (customBlockDef.tooltips != null && !customBlockDef.tooltips.isEmpty()) {
                boolean hasMultipleTooltips = customBlockDef.tooltips.size() > 1;

                for (int i = 0; i < customBlockDef.tooltips.size(); i++) {
                    translationBuilder.add(String.format("tooltip.%s.%s.tooltip%s",
                                    WesterosBlocks.MOD_ID,
                                    customBlockDef.blockName,
                                    hasMultipleTooltips ? "." + i : ""),
                            customBlockDef.tooltips.get(i));
                }
            }
        }

        // Items
        translationBuilder.add("item.westerosblocks.longclaw", "Longclaw");
        translationBuilder.add("item.westerosblocks.valyrian_steel_ingot", "Valyrian Steel Ingot");
//        translationBuilder.add("item.westerosblocks.lannister_shield", "Lannister Shield");
        translationBuilder.add("item.westerosblocks.stark_kite_shield", "Stark Kite Shield");
        translationBuilder.add("item.westerosblocks.tully_heater_shield", "Tully Heater Shield");
        translationBuilder.add("item.westerosblocks.hedge_knight_heater_shield", "Hedge Knight Heater Shield");
        translationBuilder.add("item.westerosblocks.laughing_tree_heater_shield", "Laughing Tree Heater Shield");
        translationBuilder.add("item.westerosblocks.targaryen_heater_shield", "Targaryen Heater Shield");
        translationBuilder.add("item.westerosblocks.bracken_heater_shield", "Bracken Heater Shield");
        translationBuilder.add("item.westerosblocks.greyjoy_round_shield", "Greyjoy Round Shield");
        translationBuilder.add("item.westerosblocks.blackfyre_heater_shield", "Blackfyre Heater Shield");
        translationBuilder.add("item.westerosblocks.blackwood_heater_shield", "Blackwood Heater Shield");

        // Config translations
        translationBuilder.add("text.autoconfig.westerosblocks.title", "WesterosBlocks Config");
        translationBuilder.add("text.autoconfig.westerosblocks.category.general", "General");
        translationBuilder.add("text.autoconfig.westerosblocks.category.doors", "Doors");
        translationBuilder.add("text.autoconfig.westerosblocks.category.world", "World");
        translationBuilder.add("text.autoconfig.westerosblocks.option.snowInTaiga", "Snow in Taiga");
        translationBuilder.add("text.autoconfig.westerosblocks.option.snowInTaiga.@Tooltip[0]", "Enable snow in taiga biome");
        translationBuilder.add("text.autoconfig.westerosblocks.option.blockDevMode", "Block Dev Mode");
        translationBuilder.add("text.autoconfig.westerosblocks.option.blockDevMode.@Tooltip[0]", "Block development mode");
        translationBuilder.add("text.autoconfig.westerosblocks.option.autoRestoreTime", "Auto Restore Time");
        translationBuilder.add("text.autoconfig.westerosblocks.option.autoRestoreTime.@Tooltip[0]", "Number of seconds before auto-restore");
        translationBuilder.add("text.autoconfig.westerosblocks.option.autoRestoreAllHalfDoors", "Auto Restore All Half-Doors");
        translationBuilder.add("text.autoconfig.westerosblocks.option.autoRestoreAllHalfDoors.@Tooltip[0]", "Auto restore all half-door blocks");
        translationBuilder.add("text.autoconfig.westerosblocks.option.doorSurviveAny", "Door Survive Any");
        translationBuilder.add("text.autoconfig.westerosblocks.option.doorSurviveAny.@Tooltip[0]", "Allow door to survive on any surface");
        translationBuilder.add("text.autoconfig.westerosblocks.option.doorNoConnect", "Door No Connect");
        translationBuilder.add("text.autoconfig.westerosblocks.option.doorNoConnect.@Tooltip[0]", "Avoid doors connecting to walls/panes/etc");
        translationBuilder.add("text.autoconfig.westerosblocks.option.seaLevelOverride", "Sea Level Override");
        translationBuilder.add("text.autoconfig.westerosblocks.option.seaLevelOverride.@Tooltip[0]", "Override sea level (default for Westeros=33, 0=disable override)");

        // Block translations

        translationBuilder.add("block.westerosblocks.arbor_brick_arrow_slit", "Arbor Brick Arrow Slit");
        translationBuilder.add("block.westerosblocks.black_granite_arrow_slit", "Black Granite Arrow Slit");

        translationBuilder.add("block.westerosblocks.oak_table", "Oak Table");
        translationBuilder.add("block.westerosblocks.spruce_table", "Spruce Table");
        translationBuilder.add("block.westerosblocks.birch_table", "Birch Table");

        translationBuilder.add("block.westerosblocks.oak_chair", "Oak Chair");
        translationBuilder.add("block.westerosblocks.birch_chair", "Birch Chair");
        translationBuilder.add("block.westerosblocks.spruce_chair", "Spruce Chair");

        translationBuilder.add("block.westerosblocks.oak_way_sign", "Oak Way Sign");
        translationBuilder.add("block.westerosblocks.wall_oak_way_sign", "Oak Way Sign");

        translationBuilder.add("block.westerosblocks.kings_landing_sewer_manhole", "King's Landing Sewer Manhole");
        translationBuilder.add("block.westerosblocks.oldtown_sewer_manhole", "Oldtown Sewer Manhole");
        translationBuilder.add("block.westerosblocks.sewer_manhole", "Generic Sewer Manhole");
        translationBuilder.add("block.westerosblocks.white_harbor_sewer_manhole", "White Harbor Sewer Manhole");

        // Log block translations
        translationBuilder.add("block.westerosblocks.oak_log_chain", "Oak Log Chain");
        translationBuilder.add("block.westerosblocks.jungle_log_chain", "Jungle Log Chain");
        translationBuilder.add("block.westerosblocks.jungle_log_rope", "Jungle Log Rope");
        translationBuilder.add("block.westerosblocks.archery_target", "Archery Target");
        translationBuilder.add("block.westerosblocks.closed_barrel", "Closed Barrel");
        translationBuilder.add("block.westerosblocks.firewood", "Firewood");
        translationBuilder.add("block.westerosblocks.marble_pillar", "Marble Pillar");
        translationBuilder.add("block.westerosblocks.marble_pillar_vertical_ctm", "Marble Pillar Vertical");
        translationBuilder.add("block.westerosblocks.mossy_oak_log", "Mossy Oak Log");
        translationBuilder.add("block.westerosblocks.mossy_birch_log", "Mossy Birch Log");
        translationBuilder.add("block.westerosblocks.mossy_spruce_log", "Mossy Spruce Log");
        translationBuilder.add("block.westerosblocks.mossy_jungle_log", "Mossy Jungle Log");

        // Additional log block translations from JSON definitions
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

        // Standalone torch block translations
        // Note: Only standing torch variants are translated since wall variants aren't in creative tab
        translationBuilder.add("block.westerosblocks.torch", "Torch");
        translationBuilder.add("block.westerosblocks.torch_unlit", "Unlit Torch");
        translationBuilder.add("block.westerosblocks.candle", "Candle");
        translationBuilder.add("block.westerosblocks.candle_unlit", "Unlit Candle");

        // Door block translations
        translationBuilder.add("block.westerosblocks.white_wood_door", "White Wood Door");
        translationBuilder.add("block.westerosblocks.locked_white_wood_door", "Locked White Wood Door");
        translationBuilder.add("block.westerosblocks.northern_wood_door", "Northern Wood Door");
        translationBuilder.add("block.westerosblocks.spruce_door", "Spruce Door");
        translationBuilder.add("block.westerosblocks.oak_door", "Oak Door");
        translationBuilder.add("block.westerosblocks.birch_door", "Birch Door");
        translationBuilder.add("block.westerosblocks.eyrie_weirwood_door", "Eyrie Weirwood Door");
        translationBuilder.add("block.westerosblocks.grey_wood_door", "Grey Wood Door");
        translationBuilder.add("block.westerosblocks.jungle_door", "Jungle Door");
        translationBuilder.add("block.westerosblocks.red_keep_secret_door", "Red Keep Secret Door");
        translationBuilder.add("block.westerosblocks.harrenhal_secret_door", "Harrenhal Secret Door");
        translationBuilder.add("block.westerosblocks.locked_birch_door", "Locked Birch Door");
        translationBuilder.add("block.westerosblocks.locked_dark_northern_wood_door", "Locked Dark Northern Wood Door");
        translationBuilder.add("block.westerosblocks.locked_grey_wood_door", "Locked Grey Wood Door");
        translationBuilder.add("block.westerosblocks.locked_spruce_door", "Locked Spruce Door");
        translationBuilder.add("block.westerosblocks.locked_jungle_door", "Locked Jungle Door");
        translationBuilder.add("block.westerosblocks.locked_oak_door", "Locked Oak Door");

        // Sand block translations
        translationBuilder.add("block.westerosblocks.sand_skeleton", "Sand Skeleton");

        // Coral fan block translations
        translationBuilder.add("block.westerosblocks.coral_brain_fan", "Coral Brain Fan");
        translationBuilder.add("block.westerosblocks.coral_bubble_fan", "Coral Bubble Fan");
        translationBuilder.add("block.westerosblocks.coral_fire_fan", "Coral Fire Fan");
        translationBuilder.add("block.westerosblocks.coral_horn_fan", "Coral Horn Fan");
        translationBuilder.add("block.westerosblocks.coral_tube_fan", "Coral Tube Fan");

        // Vines block translations
        translationBuilder.add("block.westerosblocks.dappled_moss", "Dappled Moss");
        translationBuilder.add("block.westerosblocks.jasmine_vines", "Jasmine Vines");
        translationBuilder.add("block.westerosblocks.vines", "Vines");
        translationBuilder.add("block.westerosblocks.falling_water_block_one", "Falling Water Block One");
        translationBuilder.add("block.westerosblocks.falling_water_block_two", "Falling Water Block Two");
        translationBuilder.add("block.westerosblocks.falling_water_block_three", "Falling Water Block Three");
        translationBuilder.add("block.westerosblocks.falling_water_block_four", "Falling Water Block Four");

        // Half door block translations
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

        // Cross block (plant) translations
        translationBuilder.add("block.westerosblocks.blue_bells", "Blue Bells");
        translationBuilder.add("block.westerosblocks.blue_chicory", "Blue Chicory");
        translationBuilder.add("block.westerosblocks.blue_flax", "Blue Flax");
        translationBuilder.add("block.westerosblocks.blue_forgetmenots", "Blue Forget-me-nots");
        translationBuilder.add("block.westerosblocks.blue_hyacinth", "Blue Hyacinth");
        translationBuilder.add("block.westerosblocks.blue_orchid", "Blue Orchid");
        translationBuilder.add("block.westerosblocks.blue_swamp_bells", "Blue Swamp Bells");
        translationBuilder.add("block.westerosblocks.bracken", "Bracken");
        translationBuilder.add("block.westerosblocks.brown_mushroom_1", "Brown Mushroom 1");
        translationBuilder.add("block.westerosblocks.brown_mushroom_2", "Brown Mushroom 2");
        translationBuilder.add("block.westerosblocks.brown_mushroom_3", "Brown Mushroom 3");
        translationBuilder.add("block.westerosblocks.brown_mushroom_4", "Brown Mushroom 4");
        translationBuilder.add("block.westerosblocks.brown_mushroom_5", "Brown Mushroom 5");
        translationBuilder.add("block.westerosblocks.brown_mushroom_6", "Brown Mushroom 6");
        translationBuilder.add("block.westerosblocks.brown_mushroom_7", "Brown Mushroom 7");
        translationBuilder.add("block.westerosblocks.brown_mushroom_8", "Brown Mushroom 8");
        translationBuilder.add("block.westerosblocks.brown_mushroom_9", "Brown Mushroom 9");
        translationBuilder.add("block.westerosblocks.brown_mushroom_10", "Brown Mushroom 10");
        translationBuilder.add("block.westerosblocks.brown_mushroom_11", "Brown Mushroom 11");
        translationBuilder.add("block.westerosblocks.brown_mushroom_12", "Brown Mushroom 12");
        translationBuilder.add("block.westerosblocks.brown_mushroom_13", "Brown Mushroom 13");
        translationBuilder.add("block.westerosblocks.cow_parsely", "Cow Parsely");
        translationBuilder.add("block.westerosblocks.cranberry_bush", "Cranberry Bush");
        translationBuilder.add("block.westerosblocks.dead_bracken", "Dead Bracken");
        translationBuilder.add("block.westerosblocks.dead_bush", "Dead Bush");
        translationBuilder.add("block.westerosblocks.dead_scrub_grass", "Dead Scrub Grass");
        translationBuilder.add("block.westerosblocks.dock_leaf", "Dock Leaf");
        translationBuilder.add("block.westerosblocks.fireweed", "Fireweed");
        translationBuilder.add("block.westerosblocks.grass", "Grass");
        translationBuilder.add("block.westerosblocks.green_leafy_herb", "Green Leafy Herb");
        translationBuilder.add("block.westerosblocks.green_scrub_grass", "Green Scrub Grass");
        translationBuilder.add("block.westerosblocks.green_spiny_herb", "Green Spiny Herb");
        translationBuilder.add("block.westerosblocks.heather", "Heather");
        translationBuilder.add("block.westerosblocks.kelp", "Kelp");
        translationBuilder.add("block.westerosblocks.lady_fern", "Lady Fern");
        translationBuilder.add("block.westerosblocks.magenta_roses", "Magenta Roses");
        translationBuilder.add("block.westerosblocks.meadow_fescue", "Meadow Fescue");
        translationBuilder.add("block.westerosblocks.nettle", "Nettle");
        translationBuilder.add("block.westerosblocks.orange_bells", "Orange Bells");
        translationBuilder.add("block.westerosblocks.orange_bog_asphodel", "Orange Bog Asphodel");
        translationBuilder.add("block.westerosblocks.orange_marigolds", "Orange Marigolds");
        translationBuilder.add("block.westerosblocks.orange_sun_star", "Orange Sun Star");
        translationBuilder.add("block.westerosblocks.orange_trollius", "Orange Trollius");
        translationBuilder.add("block.westerosblocks.pink_allium", "Pink Allium");
        translationBuilder.add("block.westerosblocks.pink_primrose", "Pink Primrose");
        translationBuilder.add("block.westerosblocks.pink_roses", "Pink Roses");
        translationBuilder.add("block.westerosblocks.pink_sweet_peas", "Pink Sweet Peas");
        translationBuilder.add("block.westerosblocks.pink_thistle", "Pink Thistle");
        translationBuilder.add("block.westerosblocks.pink_tulips", "Pink Tulips");
        translationBuilder.add("block.westerosblocks.pink_wildflowers", "Pink Wildflowers");
        translationBuilder.add("block.westerosblocks.purple_alpine_sowthistle", "Purple Alpine Sowthistle");
        translationBuilder.add("block.westerosblocks.purple_foxglove", "Purple Foxglove");
        translationBuilder.add("block.westerosblocks.purple_lavender", "Purple Lavender");
        translationBuilder.add("block.westerosblocks.purple_pansies", "Purple Pansies");
        translationBuilder.add("block.westerosblocks.purple_roses", "Purple Roses");
        translationBuilder.add("block.westerosblocks.purple_violets", "Purple Violets");
        translationBuilder.add("block.westerosblocks.red_aster", "Red Aster");
        translationBuilder.add("block.westerosblocks.red_carnations", "Red Carnations");
        translationBuilder.add("block.westerosblocks.red_chrysanthemum", "Red Chrysanthemum");
        translationBuilder.add("block.westerosblocks.red_dark_roses", "Red Dark Roses");
        translationBuilder.add("block.westerosblocks.red_fern", "Red Fern");
        translationBuilder.add("block.westerosblocks.red_flowering_spiny_herb", "Red Flowering Spiny Herb");
        translationBuilder.add("block.westerosblocks.red_mushroom_1", "Red Mushroom 1");
        translationBuilder.add("block.westerosblocks.red_mushroom_2", "Red Mushroom 2");
        translationBuilder.add("block.westerosblocks.red_mushroom_3", "Red Mushroom 3");
        translationBuilder.add("block.westerosblocks.red_mushroom_4", "Red Mushroom 4");
        translationBuilder.add("block.westerosblocks.red_mushroom_5", "Red Mushroom 5");
        translationBuilder.add("block.westerosblocks.red_mushroom_6", "Red Mushroom 6");
        translationBuilder.add("block.westerosblocks.red_mushroom_7", "Red Mushroom 7");
        translationBuilder.add("block.westerosblocks.red_mushroom_8", "Red Mushroom 8");
        translationBuilder.add("block.westerosblocks.red_mushroom_9", "Red Mushroom 9");
        translationBuilder.add("block.westerosblocks.red_poppies", "Red Poppies");
        translationBuilder.add("block.westerosblocks.red_roses", "Red Roses");
        translationBuilder.add("block.westerosblocks.red_sorrel", "Red Sorrel");
        translationBuilder.add("block.westerosblocks.red_sourleaf_bush", "Red Sourleaf Bush");
        translationBuilder.add("block.westerosblocks.red_tulips", "Red Tulips");
        translationBuilder.add("block.westerosblocks.strawberry_bush", "Strawberry Bush");
        translationBuilder.add("block.westerosblocks.thick_grass", "Thick Grass");
        translationBuilder.add("block.westerosblocks.unshaded_grass", "Unshaded Grass");
        translationBuilder.add("block.westerosblocks.white_chamomile", "White Chamomile");
        translationBuilder.add("block.westerosblocks.white_daisies", "White Daisies");
        translationBuilder.add("block.westerosblocks.white_lilyofthevalley", "White Lily of the Valley");
        translationBuilder.add("block.westerosblocks.white_peony", "White Peony");
        translationBuilder.add("block.westerosblocks.white_roses", "White Roses");
        translationBuilder.add("block.westerosblocks.yellow_bedstraw", "Yellow Bedstraw");
        translationBuilder.add("block.westerosblocks.yellow_bells", "Yellow Bells");
        translationBuilder.add("block.westerosblocks.yellow_buttercups", "Yellow Buttercups");
        translationBuilder.add("block.westerosblocks.yellow_daffodils", "Yellow Daffodils");
        translationBuilder.add("block.westerosblocks.yellow_daisies", "Yellow Daisies");
        translationBuilder.add("block.westerosblocks.yellow_dandelions", "Yellow Dandelions");
        translationBuilder.add("block.westerosblocks.yellow_hellebore", "Yellow Hellebore");
        translationBuilder.add("block.westerosblocks.yellow_lupine", "Yellow Lupine");
        translationBuilder.add("block.westerosblocks.yellow_roses", "Yellow Roses");
        translationBuilder.add("block.westerosblocks.yellow_rudbeckia", "Yellow Rudbeckia");
        translationBuilder.add("block.westerosblocks.yellow_sunflower", "Yellow Sunflower");
        translationBuilder.add("block.westerosblocks.yellow_tansy", "Yellow Tansy");
        translationBuilder.add("block.westerosblocks.yellow_wildflowers", "Yellow Wildflowers");

        // Flower Pot Block translations
        translationBuilder.add("block.westerosblocks.potted_blue_bells", "Potted Blue Bells");
        translationBuilder.add("block.westerosblocks.potted_blue_chicory", "Potted Blue Chicory");
        translationBuilder.add("block.westerosblocks.potted_blue_flax", "Potted Blue Flax");
        translationBuilder.add("block.westerosblocks.potted_blue_forgetmenots", "Potted Blue Forget-me-nots");
        translationBuilder.add("block.westerosblocks.potted_blue_hyacinth", "Potted Blue Hyacinth");
        translationBuilder.add("block.westerosblocks.potted_blue_orchid", "Potted Blue Orchid");
        translationBuilder.add("block.westerosblocks.potted_blue_swamp_bells", "Potted Blue Swamp Bells");
        translationBuilder.add("block.westerosblocks.potted_bracken", "Potted Bracken");
        translationBuilder.add("block.westerosblocks.potted_brown_mushroom_1", "Potted Brown Mushroom 1");
        translationBuilder.add("block.westerosblocks.potted_brown_mushroom_3", "Potted Brown Mushroom 3");
        translationBuilder.add("block.westerosblocks.potted_brown_mushroom_6", "Potted Brown Mushroom 6");
        translationBuilder.add("block.westerosblocks.potted_brown_mushroom_13", "Potted Brown Mushroom 13");
        translationBuilder.add("block.westerosblocks.potted_cattails", "Potted Cattails");
        translationBuilder.add("block.westerosblocks.potted_cow_parsely", "Potted Cow Parsely");
        translationBuilder.add("block.westerosblocks.potted_dead_bracken", "Potted Dead Bracken");
        translationBuilder.add("block.westerosblocks.potted_dead_bush", "Potted Dead Bush");
        translationBuilder.add("block.westerosblocks.potted_dead_scrub_grass", "Potted Dead Scrub Grass");
        translationBuilder.add("block.westerosblocks.potted_dock_leaf", "Potted Dock Leaf");
        translationBuilder.add("block.westerosblocks.potted_fireweed", "Potted Fireweed");
        translationBuilder.add("block.westerosblocks.potted_grass", "Potted Grass");
        translationBuilder.add("block.westerosblocks.potted_green_leafy_herb", "Potted Green Leafy Herb");
        translationBuilder.add("block.westerosblocks.potted_green_scrub_grass", "Potted Green Scrub Grass");
        translationBuilder.add("block.westerosblocks.potted_green_spiny_herb", "Potted Green Spiny Herb");
        translationBuilder.add("block.westerosblocks.potted_heather", "Potted Heather");
        translationBuilder.add("block.westerosblocks.potted_lady_fern", "Potted Lady Fern");
        translationBuilder.add("block.westerosblocks.potted_magenta_roses", "Potted Magenta Roses");
        translationBuilder.add("block.westerosblocks.potted_meadow_fescue", "Potted Meadow Fescue");
        translationBuilder.add("block.westerosblocks.potted_nettle", "Potted Nettle");
        translationBuilder.add("block.westerosblocks.potted_orange_bells", "Potted Orange Bells");
        translationBuilder.add("block.westerosblocks.potted_orange_bog_asphodel", "Potted Orange Bog Asphodel");
        translationBuilder.add("block.westerosblocks.potted_orange_marigolds", "Potted Orange Marigolds");
        translationBuilder.add("block.westerosblocks.potted_orange_sun_star", "Potted Orange Sun Star");
        translationBuilder.add("block.westerosblocks.potted_orange_trollius", "Potted Orange Trollius");
        translationBuilder.add("block.westerosblocks.potted_pink_allium", "Potted Pink Allium");
        translationBuilder.add("block.westerosblocks.potted_pink_primrose", "Potted Pink Primrose");
        translationBuilder.add("block.westerosblocks.potted_pink_roses", "Potted Pink Roses");
        translationBuilder.add("block.westerosblocks.potted_pink_sweet_peas", "Potted Pink Sweet Peas");
        translationBuilder.add("block.westerosblocks.potted_pink_thistle", "Potted Pink Thistle");
        translationBuilder.add("block.westerosblocks.potted_pink_tulips", "Potted Pink Tulips");
        translationBuilder.add("block.westerosblocks.potted_pink_wildflowers", "Potted Pink Wildflowers");
        translationBuilder.add("block.westerosblocks.potted_purple_alpine_sowthistle", "Potted Purple Alpine Sowthistle");
        translationBuilder.add("block.westerosblocks.potted_purple_foxglove", "Potted Purple Foxglove");
        translationBuilder.add("block.westerosblocks.potted_purple_lavender", "Potted Purple Lavender");
        translationBuilder.add("block.westerosblocks.potted_purple_pansies", "Potted Purple Pansies");
        translationBuilder.add("block.westerosblocks.potted_purple_roses", "Potted Purple Roses");
        translationBuilder.add("block.westerosblocks.potted_purple_violets", "Potted Purple Violets");
        translationBuilder.add("block.westerosblocks.potted_red_aster", "Potted Red Aster");
        translationBuilder.add("block.westerosblocks.potted_red_carnations", "Potted Red Carnations");
        translationBuilder.add("block.westerosblocks.potted_red_chrysanthemum", "Potted Red Chrysanthemum");
        translationBuilder.add("block.westerosblocks.potted_red_dark_roses", "Potted Red Dark Roses");
        translationBuilder.add("block.westerosblocks.potted_red_fern", "Potted Red Fern");
        translationBuilder.add("block.westerosblocks.potted_red_flowering_spiny_herb", "Potted Red Flowering Spiny Herb");
        translationBuilder.add("block.westerosblocks.potted_red_mushroom_1", "Potted Red Mushroom 1");
        translationBuilder.add("block.westerosblocks.potted_red_mushroom_2", "Potted Red Mushroom 2");
        translationBuilder.add("block.westerosblocks.potted_red_mushroom_3", "Potted Red Mushroom 3");
        translationBuilder.add("block.westerosblocks.potted_red_mushroom_7", "Potted Red Mushroom 7");
        translationBuilder.add("block.westerosblocks.potted_red_mushroom_8", "Potted Red Mushroom 8");
        translationBuilder.add("block.westerosblocks.potted_red_mushroom_9", "Potted Red Mushroom 9");
        translationBuilder.add("block.westerosblocks.potted_red_poppies", "Potted Red Poppies");
        translationBuilder.add("block.westerosblocks.potted_red_roses", "Potted Red Roses");
        translationBuilder.add("block.westerosblocks.potted_red_sorrel", "Potted Red Sorrel");
        translationBuilder.add("block.westerosblocks.potted_red_sourleaf_bush", "Potted Red Sourleaf Bush");
        translationBuilder.add("block.westerosblocks.potted_red_tulips", "Potted Red Tulips");
        translationBuilder.add("block.westerosblocks.potted_white_chamomile", "Potted White Chamomile");
        translationBuilder.add("block.westerosblocks.potted_white_daisies", "Potted White Daisies");
        translationBuilder.add("block.westerosblocks.potted_white_lilyofthevalley", "Potted White Lily of the Valley");
        translationBuilder.add("block.westerosblocks.potted_white_peony", "Potted White Peony");
        translationBuilder.add("block.westerosblocks.potted_white_roses", "Potted White Roses");
        translationBuilder.add("block.westerosblocks.potted_yellow_bedstraw", "Potted Yellow Bedstraw");
        translationBuilder.add("block.westerosblocks.potted_yellow_bells", "Potted Yellow Bells");
        translationBuilder.add("block.westerosblocks.potted_yellow_buttercups", "Potted Yellow Buttercups");
        translationBuilder.add("block.westerosblocks.potted_yellow_daffodils", "Potted Yellow Daffodils");
        translationBuilder.add("block.westerosblocks.potted_yellow_daisies", "Potted Yellow Daisies");
        translationBuilder.add("block.westerosblocks.potted_yellow_dandelions", "Potted Yellow Dandelions");
        translationBuilder.add("block.westerosblocks.potted_yellow_hellebore", "Potted Yellow Hellebore");
        translationBuilder.add("block.westerosblocks.potted_yellow_lupine", "Potted Yellow Lupine");
        translationBuilder.add("block.westerosblocks.potted_yellow_roses", "Potted Yellow Roses");
        translationBuilder.add("block.westerosblocks.potted_yellow_rudbeckia", "Potted Yellow Rudbeckia");
        translationBuilder.add("block.westerosblocks.potted_yellow_sunflower", "Potted Yellow Sunflower");
        translationBuilder.add("block.westerosblocks.potted_yellow_tansy", "Potted Yellow Tansy");
        translationBuilder.add("block.westerosblocks.potted_yellow_wildflowers", "Potted Yellow Wildflowers");

        // Coral Web Blocks
        translationBuilder.add("block.westerosblocks.coral_brain_web", "Coral Brain Web");
        translationBuilder.add("block.westerosblocks.coral_bubble_web", "Coral Bubble Web");
        translationBuilder.add("block.westerosblocks.coral_fire_web", "Coral Fire Web");
        translationBuilder.add("block.westerosblocks.coral_horn_web", "Coral Horn Web");
        translationBuilder.add("block.westerosblocks.coral_tube_web", "Coral Tube Web");

        // Web Blocks
        translationBuilder.add("block.westerosblocks.alyssas_tears_mist_one", "Alyssa's Tears Mist One");
        translationBuilder.add("block.westerosblocks.alyssas_tears_mist_two", "Alyssa's Tears Mist Two");
        translationBuilder.add("block.westerosblocks.alyssas_tears_mist_three", "Alyssa's Tears Mist Three");
        translationBuilder.add("block.westerosblocks.alyssas_tears_mist_four", "Alyssa's Tears Mist Four");
        translationBuilder.add("block.westerosblocks.black_bricicle", "Black Bricicle");
        translationBuilder.add("block.westerosblocks.bees", "Bees");

        // Additional web blocks
        translationBuilder.add("block.westerosblocks.bushel_of_herbs", "Bushel of Herbs");
        translationBuilder.add("block.westerosblocks.bushel_of_sourleaf", "Bushel of Sourleaf");
        translationBuilder.add("block.westerosblocks.butterfly_blue", "Northern Blue Butterfly");
        translationBuilder.add("block.westerosblocks.butterfly_orange", "Southern Orange Butterfly");
        translationBuilder.add("block.westerosblocks.butterfly_red", "Eastern Red Butterfly");
        translationBuilder.add("block.westerosblocks.butterfly_white", "Central White Butterfly");
        translationBuilder.add("block.westerosblocks.butterfly_yellow", "Western Yellow Butterfly");
        translationBuilder.add("block.westerosblocks.cattails", "Cattails");
        translationBuilder.add("block.westerosblocks.chain_block_harness", "Chain Block Harness");
        translationBuilder.add("block.westerosblocks.chili_ristra", "Chili Ristra");
        translationBuilder.add("block.westerosblocks.cobweb", "Cobweb");
        translationBuilder.add("block.westerosblocks.dead_fish", "Dead Fish");
        translationBuilder.add("block.westerosblocks.dead_fowl", "Dead Fowl");
        translationBuilder.add("block.westerosblocks.dead_frog", "Dead Frog");
        translationBuilder.add("block.westerosblocks.dead_hare", "Dead Hare");
        translationBuilder.add("block.westerosblocks.dead_jungle_tall_grass", "Dead Jungle Tall Grass");
        translationBuilder.add("block.westerosblocks.dead_rat", "Dead Rat");
        translationBuilder.add("block.westerosblocks.dead_savanna_tall_grass", "Dead Savanna Tall Grass");
        translationBuilder.add("block.westerosblocks.dragonfly", "Dragonfly");
        translationBuilder.add("block.westerosblocks.flies", "Flies");
        translationBuilder.add("block.westerosblocks.garlic_strand", "Garlic Strand");
        translationBuilder.add("block.westerosblocks.icicle", "Icicle");
        translationBuilder.add("block.westerosblocks.iron_throne_random_blades", "Iron Throne Random Blades");
        translationBuilder.add("block.westerosblocks.jungle_tall_fern", "Jungle Tall Fern");
        translationBuilder.add("block.westerosblocks.jungle_tall_grass", "Jungle Tall Grass");
        translationBuilder.add("block.westerosblocks.rope_block_harness", "Rope Block Harness");
        translationBuilder.add("block.westerosblocks.sausages_leg_of_ham", "Sausages Leg of Ham");
        translationBuilder.add("block.westerosblocks.savanna_tall_grass", "Savanna Tall Grass");
        translationBuilder.add("block.westerosblocks.smoke", "Smoke");
        translationBuilder.add("block.westerosblocks.vertical_chain", "Vertical Chain");
        translationBuilder.add("block.westerosblocks.vertical_rope", "Vertical Rope");

        // Pane Blocks
        translationBuilder.add("block.westerosblocks.vertical_net", "Vertical Net");
        translationBuilder.add("block.westerosblocks.dorne_carved_stone_window", "Dorne Carved Stone Window");
        translationBuilder.add("block.westerosblocks.dorne_carved_wooden_window", "Dorne Carved Wooden Window");
        translationBuilder.add("block.westerosblocks.iron_bars", "Iron Bars");
        translationBuilder.add("block.westerosblocks.oxidized_iron_crossbar", "Oxidized Iron Crossbar");
        translationBuilder.add("block.westerosblocks.iron_crossbar", "Iron Crossbar");
        translationBuilder.add("block.westerosblocks.oxidized_iron_bars", "Oxidized Iron Bars");

        // Solid Blocks
        translationBuilder.add("block.westerosblocks.six_sided_birch", "Six Sided Birch");
        translationBuilder.add("block.westerosblocks.apple_basket", "Apple Basket");
        translationBuilder.add("block.westerosblocks.apple_crate", "Apple Crate");
        translationBuilder.add("block.westerosblocks.approval_utility_block", "Approval Utility Block");
        translationBuilder.add("block.westerosblocks.apricot_basket", "Apricot Basket");
        translationBuilder.add("block.westerosblocks.six_sided_jungle", "Six Sided Jungle");
        translationBuilder.add("block.westerosblocks.six_sided_oak", "Six Sided Oak");
        translationBuilder.add("block.westerosblocks.six_sided_spruce", "Six Sided Spruce");
        translationBuilder.add("block.westerosblocks.six_sided_stone_slab", "Six Sided Stone Slab");

        // Additional Solid Blocks from JSON definitions
        translationBuilder.add("block.westerosblocks.arbor_brick_ornate", "Arbor Brick Ornate");
        translationBuilder.add("block.westerosblocks.bench_butcher_knives", "Bench with Butcher Knives");
        translationBuilder.add("block.westerosblocks.bench_carpentry_hammer_saw", "Bench with Carpentry Tools");
        translationBuilder.add("block.westerosblocks.bench_drawers", "Bench with Drawers");
        translationBuilder.add("block.westerosblocks.bench_kitchen_knives", "Bench with Kitchen Knives");
        translationBuilder.add("block.westerosblocks.bench_kitchen_pans", "Bench with Kitchen Pans");
        translationBuilder.add("block.westerosblocks.bench_mason_hammer_mallet", "Bench with Masonry Tools");
        translationBuilder.add("block.westerosblocks.berry_basket", "Berry Basket");
        translationBuilder.add("block.westerosblocks.berry_crate", "Berry Crate");
        translationBuilder.add("block.westerosblocks.black_brick_engraved", "Black Brick Engraved");
        translationBuilder.add("block.westerosblocks.bluegreen_carved_sandstone", "Blue-Green Carved Sandstone");

        // Additional Solid Blocks from JSON definitions - Part 2
        translationBuilder.add("block.westerosblocks.bone_dirt", "Bone Dirt");
        translationBuilder.add("block.westerosblocks.bookshelf_abandoned", "Abandoned Bookshelf");
        translationBuilder.add("block.westerosblocks.bookshelf_library", "Library Bookshelf");
        translationBuilder.add("block.westerosblocks.broken_cabinet", "Broken Cabinet");
        translationBuilder.add("block.westerosblocks.bookshelf_maester", "Maester Bookshelf");
        translationBuilder.add("block.westerosblocks.brown_grey_brick_engraved", "Brown Grey Brick Engraved");
        translationBuilder.add("block.westerosblocks.cabinet_drawer", "Cabinet Drawer");
        translationBuilder.add("block.westerosblocks.cage", "Cage");
        translationBuilder.add("block.westerosblocks.carrot_basket", "Carrot Basket");
        translationBuilder.add("block.westerosblocks.carrot_crate", "Carrot Crate");
        translationBuilder.add("block.westerosblocks.closed_basket", "Closed Basket");
        translationBuilder.add("block.westerosblocks.closed_cabinet", "Closed Cabinet");
        translationBuilder.add("block.westerosblocks.coarse_dark_red_carved_sandstone", "Coarse Dark Red Carved Sandstone");
        translationBuilder.add("block.westerosblocks.coarse_red_carved_sandstone", "Coarse Red Carved Sandstone");
        translationBuilder.add("block.westerosblocks.cobble_keystone", "Cobblestone Keystone");
        translationBuilder.add("block.westerosblocks.coloured_sept_window", "Coloured Sept Window");
        translationBuilder.add("block.westerosblocks.crate", "Crate One");
        translationBuilder.add("block.westerosblocks.crate2", "Crate Two");
        translationBuilder.add("block.westerosblocks.crate3", "Crate Three");
        translationBuilder.add("block.westerosblocks.dark_grey_brick_engraved", "Dark Grey Brick Engraved");
        translationBuilder.add("block.westerosblocks.date_basket", "Date Basket");
        translationBuilder.add("block.westerosblocks.dates", "Dates");

        // Crop Blocks
        translationBuilder.add("block.westerosblocks.candle_altar", "Candle Altar");
        translationBuilder.add("block.westerosblocks.crop_carrots", "Carrots");
        translationBuilder.add("block.westerosblocks.crop_peas", "Peas");
        translationBuilder.add("block.westerosblocks.crop_turnips", "Turnips");
        translationBuilder.add("block.westerosblocks.crop_wheat", "Wheat");
        translationBuilder.add("block.westerosblocks.seagrass", "Seagrass");

        // The format desired is tag.item.<namespace>.<path> for the translation key with slashes in path turned into periods."
        translationBuilder.add("tag.item.c.shield", "Shield");
    }
}
