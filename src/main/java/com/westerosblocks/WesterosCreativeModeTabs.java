package com.westerosblocks;

import com.westerosblocks.block.ModBlock;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class WesterosCreativeModeTabs {
    public static final Map<String, RegistryKey<ItemGroup>> TABS = new HashMap<>();

    public record TabDefinition(String id, String label, String iconItem) {
    }

    public static final TabDefinition[] TAB_DEFINITIONS = {
            new TabDefinition("westeros_cobblestone_tab", "Cobblestone", "flagstone"),
            new TabDefinition("westeros_fieldstone_tab", "Fieldstone", "light_grey_brick"),
            new TabDefinition("westeros_smooth_ashlar_tab", "Smooth Ashlar", "grey_light_stone"),
            new TabDefinition("westeros_quarter_ashlar_tab", "Quarter Ashlar", "small_smooth_stone_brick"),
            new TabDefinition("westeros_medium_ashlar_tab", "Medium Ashlar", "grey_granite"),
            new TabDefinition("westeros_half_ashlar_tab", "Half Ashlar", "stone_brick"),
            new TabDefinition("westeros_polished_stone_tab", "Polished Stone", "grey_granite_polished"),
            new TabDefinition("westeros_brick_tab", "Brick", "small_orange_bricks_ornate"),
            new TabDefinition("westeros_marble_plaster_tab", "Marble and Plaster", "grey_keystone"),
            new TabDefinition("westeros_timber_frame_tab", "Timber Frame", "timber_oak_reach_brick_crosshatch"),
            new TabDefinition("westeros_roofing_tab", "Roofing", "orange_slate"),
            new TabDefinition("westeros_wood_planks_tab", "Wood and Planks", "oak_vertical_planks"),
            new TabDefinition("westeros_panelling_carvings_tab", "Panelling and Carvings", "grey_keystone"),
            new TabDefinition("westeros_metal_tab", "Metal", "oxidized_bronze_block"),
            new TabDefinition("westeros_windows_glass_tab", "Windows and Glass", "coloured_sept_window"),
            new TabDefinition("westeros_furniture_tab", "Furniture", "table"),
            new TabDefinition("westeros_decor_tab", "Decor", "dead_hare"),
            new TabDefinition("westeros_lighting_tab", "Lighting", "red_lantern2"),
            new TabDefinition("westeros_tool_blocks_tab", "Tool Blocks", "piston_extension"),
            new TabDefinition("westeros_food_blocks_tab", "Food Blocks", "squash"),
            new TabDefinition("westeros_cloth_fibers_tab", "Cloth and Fibers", "fancy_blue_carpet"),
            new TabDefinition("westeros_banners_tab", "Banners", "westeroscraft_banner"),
            new TabDefinition("westeros_terrain_sets_tab", "Terrain Sets", "terrainset_eastern_islands"),
            new TabDefinition("westeros_grass_dirt_tab", "Grass and Dirt", "classic_grass_block"),
            new TabDefinition("westeros_sand_gravel_tab", "Sand and Gravel", "sand_skeleton"),
            new TabDefinition("westeros_logs_tab", "Logs", "weirwood_face_4"),
            new TabDefinition("westeros_foliage_tab", "Foliage", "weirwood_leaves"),
            new TabDefinition("westeros_grasses_shrubs_tab", "Grasses and Shrubs", "thick_grass"),
            new TabDefinition("westeros_flowers_tab", "Flowers", "blue_bells"),
            new TabDefinition("westeros_crops_herbs_tab", "Crops and Herbs", "crop_wheat"),
            new TabDefinition("westeros_water_air_tab", "Water and Air", "falling_water_block_one"),
            new TabDefinition("westeros_misc_tab", "Miscellaneous", "piled_bones"),
            new TabDefinition("westeros_utility_tab", "Utility", "approval_utility_block"),
            new TabDefinition("westeros_test_tab", "Test", "test_block"),
            new TabDefinition("westeros_sounds_tab", "Sounds", "tavern_small"),
            new TabDefinition("westeros_do_not_use_tab", "Do Not Use", "note_utility_block")
    };

    public static void registerTabs() {
        for (TabDefinition def : TAB_DEFINITIONS) {
            TABS.put(def.id, registerTab(def.id, def.label, def.iconItem));
        }
    }

    private static RegistryKey<ItemGroup> registerTab(String tabName, String title, String iconItem) {
        Identifier tabId = WesterosBlocks.id(tabName);
        RegistryKey<ItemGroup> key = RegistryKey.of(RegistryKeys.ITEM_GROUP, tabId);

        ItemGroup group = FabricItemGroup.builder()
                .icon(() -> new ItemStack(Registries.ITEM.get(Identifier.of(WesterosBlocks.MOD_ID, iconItem))))
                .displayName(Text.literal(title))
                .entries((context, entries) -> {
                    for (ModBlock def : WesterosBlocksDefLoader.getCustomBlockDefs()) {
                        if (def != null && def.creativeTab.equals(tabName)) {
                            entries.add(Registries.ITEM.get(WesterosBlocks.id(def.blockName)));
                        }
                    }
                })
                .build();

        Registry.register(Registries.ITEM_GROUP, key, group);

        return key;
    }

    public static void registerCreativeModeTabs() {
        registerTabs();
    }
}