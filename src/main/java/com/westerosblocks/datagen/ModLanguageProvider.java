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

        // TODO can prob grab the item tag def json and generate this automatically when/if we get more
        // The format desired is tag.item.<namespace>.<path> for the translation key with slashes in path turned into periods."
        translationBuilder.add("tag.item.c.shield", "Shield");
    }
}
