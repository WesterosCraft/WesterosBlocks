package com.westerosblocks.block;

import com.westerosblocks.WesterosBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.WoodType;
import java.util.List;
import java.util.Map;

/**
 * Builder-based block registration for WesterosBlocks.
 * This class contains the new fluent API for registering custom blocks
 * using the BlockBuilder pattern with sensible defaults.
 */
public class ModBlocks2 {
    // Arrow Slits
    public static final Block ARBOR_BRICK_ARROW_SLIT = registerArrowSlitBlock(
            "arbor_brick_arrow_slit",
            builder -> builder
                    .creativeTab("westeros_decor_tab")
                    .texture("ashlar_third/arbor/all"));

    public static final Block BLACK_GRANITE_ARROW_SLIT = registerArrowSlitBlock(
            "black_granite_arrow_slit",
            builder -> builder
                    .creativeTab("westeros_decor_tab")
                    .texture("ashlar_third/black/all"));

    // Rail Blocks
    public static final Block FANCY_BLUE_CARPET = registerRailBlock(
            "fancy_blue_carpet",
            builder -> builder
                    .creativeTab("westeros_cloth_fibers_tab")
                    .hardness(0.1f)
                    .resistance(0.1f)
                    .soundType("cloth")
                    .allowUnsupported()
                    .texture("carpet/fancy_blue_carpet"));

    public static final Block FANCY_RED_CARPET = registerRailBlock(
            "fancy_red_carpet",
            builder -> builder
                    .creativeTab("westeros_cloth_fibers_tab")
                    .hardness(0.1f)
                    .resistance(0.1f)
                    .soundType("cloth")
                    .allowUnsupported()
                    .texture("carpet/fancy_red_carpet"));

    public static final Block HORIZONTAL_CHAIN = registerRailBlock(
            "horizontal_chain",
            builder -> builder
                    .creativeTab("westeros_metal_tab")
                    .hardness(1.0f)
                    .resistance(5.0f)
                    .soundType("metal")
                    .allowUnsupported()
                    .textures("rail_block/chain", "rail_block/chain_turned"));

    public static final Block HORIZONTAL_NET = registerRailBlock(
            "horizontal_net",
            builder -> builder
                    .creativeTab("westeros_cloth_fibers_tab")
                    .hardness(0.5f)
                    .resistance(2.0f)
                    .soundType("cloth")
                    .allowUnsupported()
                    .textures("rail_block/net_large", "rail_block/net_large_turned"));

    public static final Block HORIZONTAL_ROPE = registerRailBlock(
            "horizontal_rope",
            builder -> builder
                    .creativeTab("westeros_cloth_fibers_tab")
                    .hardness(0.5f)
                    .resistance(2.0f)
                    .soundType("cloth")
                    .allowUnsupported()
                    .textures("rail_block/rope", "rail_block/rope_turned"));

    public static final Block PACKED_SNOW = registerRailBlock(
            "packed_snow",
            builder -> builder
                    .creativeTab("westeros_water_air_tab")
                    .hardness(0.2f)
                    .resistance(0.2f)
                    .soundType("snow")
                    .allowUnsupported()
                    .textures("rail_block/packed_snow", "rail_block/packed_snow_turned"));

    // Fire Blocks
    public static final Block SAFE_FIRE = registerFireBlock(
            "safe_fire",
            builder -> builder
                    .creativeTab("westeros_lighting_tab")
                    .lightLevel(1)
                    .textures("safe_fire/fire_layer_0", "safe_fire/fire_layer_1"));

    public static final Block WILDFIRE = registerFireBlock(
            "wildfire",
            builder -> builder
                    .creativeTab("westeros_lighting_tab")
                    .lightLevel(9)
                    .particleType("wildfire")
                    .tooltips(List.of("Very hot fire", "no fr its hot!"))
                    .textures("wildfire/wildfire_layer_0", "wildfire/wildfire_layer_1"));

    // Tables

    public static final Block OAK_TABLE = registerTableBlock(
            "oak_table",
            builder -> builder
                    .creativeTab("westeros_furniture_tab")
                    .texture("oak_planks")
                    .woodType(WoodType.OAK));

    public static final Block BIRCH_TABLE = registerTableBlock(
            "birch_table",
            builder -> builder
                    .creativeTab("westeros_furniture_tab")
                    .texture("birch_planks")
                    .woodType(WoodType.BIRCH));

    public static final Block SPRUCE_TABLE = registerTableBlock(
            "spruce_table",
            builder -> builder
                    .creativeTab("westeros_furniture_tab")
                    .texture("spruce_planks")
                    .woodType(WoodType.SPRUCE));

    public static final Block OAK_CHAIR = registerChairBlock(
            "oak_chair",
            builder -> builder
                    .creativeTab("westeros_furniture_tab")
                    .texture("wood/oak/all")
                    .woodType(WoodType.OAK));

    public static final Block BIRCH_CHAIR = registerChairBlock(
            "birch_chair",
            builder -> builder
                    .creativeTab("westeros_furniture_tab")
                    .texture("wood/birch/all")
                    .woodType(WoodType.BIRCH));

    public static final Block SPRUCE_CHAIR = registerChairBlock(
            "spruce_chair",
            builder -> builder
                    .creativeTab("westeros_furniture_tab")
                    .texture("wood/spruce/all")
                    .woodType(WoodType.SPRUCE));

    public static final Block OAK_WAY_SIGN = registerWaySignBlock(
            "oak_way_sign",
            builder -> builder
                    .creativeTab("westeros_decor_tab")
                    .texture("wood/oak/all")
                    .woodType(WoodType.OAK));

    public static final Block KINGS_LANDING_SEWER_MANHOLE = registerTrapDoorBlock(
            "kings_landing_sewer_manhole",
            builder -> builder
                    .creativeTab("westeros_decor_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .texture("trapdoor_block/kings_landing_manhole")
                    .woodType(WoodType.OAK)
                    .soundType("iron"));

    public static final Block OLDTOWN_SEWER_MANHOLE = registerTrapDoorBlock(
            "oldtown_sewer_manhole",
            builder -> builder
                    .creativeTab("westeros_decor_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .texture("trapdoor_block/oldtown_manhole")
                    .woodType(WoodType.OAK)
                    .soundType("iron"));

    public static final Block SEWER_MANHOLE = registerTrapDoorBlock(
            "sewer_manhole",
            builder -> builder
                    .creativeTab("westeros_decor_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .texture("trapdoor_block/manhole")
                    .woodType(WoodType.OAK)
                    .soundType("iron"));

    public static final Block WHITE_HARBOR_SEWER_MANHOLE = registerTrapDoorBlock(
            "white_harbor_sewer_manhole",
            builder -> builder
                    .creativeTab("westeros_decor_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .texture("trapdoor_block/white_harbor_manhole")
                    .woodType(WoodType.OAK)
                    .soundType("iron"));

    public static final Block OAK_LOG_CHAIN = registerLogBlock(
            "oak_log_chain",
            builder -> builder
                    .creativeTab("westeros_logs_tab")
                    .hardness(2.0f)
                    .resistance(6.0f)
                    .harvestLevel(1)
                    .requiresAxe()
                    .woodType(WoodType.OAK)
                    .soundType("wood")
                    .textures("bark/oak/top", "bark/oak/top", "bark/oak/chain"));

    public static final Block JUNGLE_LOG_CHAIN = registerLogBlock(
            "jungle_log_chain",
            builder -> builder
                    .creativeTab("westeros_logs_tab")
                    .hardness(2.0f)
                    .resistance(6.0f)
                    .harvestLevel(1)
                    .requiresAxe()
                    .woodType(WoodType.JUNGLE)
                    .soundType("wood")
                    .textures("bark/jungle/top", "bark/jungle/top", "bark/jungle/chain"));

    public static final Block JUNGLE_LOG_ROPE = registerLogBlock(
            "jungle_log_rope",
            builder -> builder
                    .creativeTab("westeros_logs_tab")
                    .hardness(2.0f)
                    .resistance(6.0f)
                    .harvestLevel(1)
                    .requiresAxe()
                    .woodType(WoodType.JUNGLE)
                    .soundType("wood")
                    .textures("bark/jungle/top", "bark/jungle/top", "bark/jungle/rope"));

    public static final Block ARCHERY_TARGET = registerLogBlock(
            "archery_target",
            builder -> builder
                    .creativeTab("westeros_misc_tab")
                    .hardness(2.0f)
                    .resistance(6.0f)
                    .harvestLevel(1)
                    .requiresAxe()
                    .woodType(WoodType.OAK)
                    .soundType("wood")
                    .textures("archery_target/front", "archery_target/front", "archery_target/side"));

    public static final Block CLOSED_BARREL = registerLogBlock(
            "closed_barrel",
            builder -> builder
                    .creativeTab("westeros_decor_tab")
                    .hardness(2.0f)
                    .resistance(6.0f)
                    .harvestLevel(1)
                    .requiresAxe()
                    .woodType(WoodType.OAK)
                    .soundType("wood")
                    .textures("barrel_closed/barrel_top_closed", "barrel_closed/barrel_top_closed",
                            "barrel_sides/side1"));

    public static final Block FIREWOOD = registerLogBlock(
            "firewood",
            builder -> builder
                    .creativeTab("westeros_decor_tab")
                    .hardness(2.0f)
                    .resistance(2.0f)
                    .harvestLevel(1)
                    .requiresAxe()
                    .woodType(WoodType.OAK)
                    .soundType("wood")
                    .textures("firewood/top", "firewood/top", "firewood/side"));

    public static final Block MARBLE_PILLAR = registerLogBlock(
            "marble_pillar",
            builder -> builder
                    .creativeTab("westeros_marble_plaster_tab")
                    .hardness(2.0f)
                    .resistance(6.0f)
                    .harvestLevel(1)
                    .requiresAxe()
                    .woodType(WoodType.OAK)
                    .soundType("stone")
                    .textures("marble/quartz/column_topbottom", "marble/quartz/column_topbottom",
                            "marble/quartz/column_side"));

    public static final Block MARBLE_PILLAR_VERTICAL_CTM = registerLogBlock(
            "marble_pillar_vertical_ctm",
            builder -> builder
                    .creativeTab("westeros_marble_plaster_tab")
                    .hardness(2.0f)
                    .resistance(6.0f)
                    .harvestLevel(1)
                    .requiresAxe()
                    .woodType(WoodType.OAK)
                    .soundType("stone")
                    .textures("marble/quartz/column_topbottom", "marble/quartz/column_topbottom",
                            "marble/quartz/column_side_ctm"));

    public static final Block MOSSY_OAK_LOG = registerLogBlock(
            "mossy_oak_log",
            builder -> builder
                    .creativeTab("westeros_logs_tab")
                    .hardness(2.0f)
                    .resistance(6.0f)
                    .harvestLevel(1)
                    .requiresAxe()
                    .woodType(WoodType.OAK)
                    .soundType("wood")
                    .textures("bark/oak/mossy/bottom", "bark/oak/mossy/top", "bark/oak/mossy/side"));

    public static final Block MOSSY_BIRCH_LOG = registerLogBlock(
            "mossy_birch_log",
            builder -> builder
                    .creativeTab("westeros_logs_tab")
                    .hardness(2.0f)
                    .resistance(6.0f)
                    .harvestLevel(1)
                    .requiresAxe()
                    .woodType(WoodType.BIRCH)
                    .soundType("wood")
                    .textures("bark/birch/mossy/bottom", "bark/birch/mossy/top", "bark/birch/mossy/side"));

    public static final Block MOSSY_SPRUCE_LOG = registerLogBlock(
            "mossy_spruce_log",
            builder -> builder
                    .creativeTab("westeros_logs_tab")
                    .hardness(2.0f)
                    .resistance(6.0f)
                    .harvestLevel(1)
                    .requiresAxe()
                    .woodType(WoodType.SPRUCE)
                    .soundType("wood")
                    .textures("bark/spruce/mossy/bottom", "bark/spruce/mossy/top", "bark/spruce/mossy/side"));

    public static final Block MOSSY_JUNGLE_LOG = registerLogBlock(
            "mossy_jungle_log",
            builder -> builder
                    .creativeTab("westeros_logs_tab")
                    .hardness(2.0f)
                    .resistance(6.0f)
                    .harvestLevel(1)
                    .requiresAxe()
                    .woodType(WoodType.JUNGLE)
                    .soundType("wood")
                    .textures("bark/jungle/mossy/bottom", "bark/jungle/mossy/top", "bark/jungle/mossy/side"));

    // Additional log blocks from JSON definitions
    public static final Block OAK_LOG_ROPE = registerLogBlock(
            "oak_log_rope",
            builder -> builder
                    .creativeTab("westeros_logs_tab")
                    .hardness(2.0f)
                    .resistance(6.0f)
                    .harvestLevel(1)
                    .requiresAxe()
                    .woodType(WoodType.OAK)
                    .soundType("wood")
                    .textures("bark/oak/top", "bark/oak/top", "bark/oak/rope"));

    public static final Block PALM_TREE_LOG = registerLogBlock(
            "palm_tree_log",
            builder -> builder
                    .creativeTab("westeros_logs_tab")
                    .hardness(2.0f)
                    .resistance(6.0f)
                    .harvestLevel(1)
                    .requiresAxe()
                    .woodType(WoodType.OAK)
                    .soundType("wood")
                    .textures("bark/palm/top", "bark/palm/top", "bark/palm/side"));

    public static final Block SANDSTONE_PILLAR = registerLogBlock(
            "sandstone_pillar",
            builder -> builder
                    .creativeTab("westeros_panelling_carvings_tab")
                    .hardness(2.0f)
                    .resistance(6.0f)
                    .harvestLevel(1)
                    .requiresAxe()
                    .woodType(WoodType.OAK)
                    .soundType("stone")
                    .textures("ashlar_third/sandstone/column_top", "ashlar_third/sandstone/column_top",
                            "ashlar_third/sandstone/column_side"));

    public static final Block SPRUCE_LOG_CHAIN = registerLogBlock(
            "spruce_log_chain",
            builder -> builder
                    .creativeTab("westeros_logs_tab")
                    .hardness(2.0f)
                    .resistance(6.0f)
                    .harvestLevel(1)
                    .requiresAxe()
                    .woodType(WoodType.SPRUCE)
                    .soundType("wood")
                    .textures("bark/spruce/top", "bark/spruce/top", "bark/spruce/chain"));

    public static final Block SPRUCE_LOG_ROPE = registerLogBlock(
            "spruce_log_rope",
            builder -> builder
                    .creativeTab("westeros_logs_tab")
                    .hardness(2.0f)
                    .resistance(6.0f)
                    .harvestLevel(1)
                    .requiresAxe()
                    .woodType(WoodType.SPRUCE)
                    .soundType("wood")
                    .textures("bark/spruce/top", "bark/spruce/top", "bark/spruce/rope"));

    public static final Block STACKED_BONES = registerLogBlock(
            "stacked_bones",
            builder -> builder
                    .creativeTab("westeros_misc_tab")
                    .hardness(2.0f)
                    .resistance(6.0f)
                    .harvestLevel(1)
                    .requiresAxe()
                    .woodType(WoodType.OAK)
                    .soundType("stone")
                    .textures("stacked_bones/bone_stacked_front", "stacked_bones/bone_stacked_front",
                            "stacked_bones/bone_stacked_side"));

    public static final Block WEIRWOOD_FACE_0 = registerLogBlock(
            "weirwood_face_0",
            builder -> builder
                    .creativeTab("westeros_logs_tab")
                    .hardness(2.0f)
                    .resistance(6.0f)
                    .harvestLevel(1)
                    .requiresAxe()
                    .woodType(WoodType.OAK)
                    .soundType("wood")
                    .textures("bark/weirwood/face_0", "bark/weirwood/face_0", "bark/weirwood/side"));

    public static final Block WEIRWOOD_FACE_1 = registerLogBlock(
            "weirwood_face_1",
            builder -> builder
                    .creativeTab("westeros_logs_tab")
                    .hardness(2.0f)
                    .resistance(6.0f)
                    .harvestLevel(1)
                    .requiresAxe()
                    .woodType(WoodType.OAK)
                    .soundType("wood")
                    .textures("bark/weirwood/face_1", "bark/weirwood/face_1", "bark/weirwood/side"));

    public static final Block WEIRWOOD_FACE_2 = registerLogBlock(
            "weirwood_face_2",
            builder -> builder
                    .creativeTab("westeros_logs_tab")
                    .hardness(2.0f)
                    .resistance(6.0f)
                    .harvestLevel(1)
                    .requiresAxe()
                    .woodType(WoodType.OAK)
                    .soundType("wood")
                    .textures("bark/weirwood/face_2", "bark/weirwood/face_2", "bark/weirwood/side"));

    public static final Block WEIRWOOD_FACE_3 = registerLogBlock(
            "weirwood_face_3",
            builder -> builder
                    .creativeTab("westeros_logs_tab")
                    .hardness(2.0f)
                    .resistance(6.0f)
                    .harvestLevel(1)
                    .requiresAxe()
                    .woodType(WoodType.OAK)
                    .soundType("wood")
                    .textures("bark/weirwood/face_3", "bark/weirwood/face_3", "bark/weirwood/side"));

    public static final Block WEIRWOOD_FACE_4 = registerLogBlock(
            "weirwood_face_4",
            builder -> builder
                    .creativeTab("westeros_logs_tab")
                    .hardness(2.0f)
                    .resistance(6.0f)
                    .harvestLevel(1)
                    .requiresAxe()
                    .woodType(WoodType.OAK)
                    .soundType("wood")
                    .textures("bark/weirwood/face_4", "bark/weirwood/face_4", "bark/weirwood/side"));

    public static final Block WEIRWOOD_FACE_5 = registerLogBlock(
            "weirwood_face_5",
            builder -> builder
                    .creativeTab("westeros_logs_tab")
                    .hardness(2.0f)
                    .resistance(6.0f)
                    .harvestLevel(1)
                    .requiresAxe()
                    .woodType(WoodType.OAK)
                    .soundType("wood")
                    .textures("bark/weirwood/face_5", "bark/weirwood/face_5", "bark/weirwood/side"));

    public static final Block WEIRWOOD_FACE_6 = registerLogBlock(
            "weirwood_face_6",
            builder -> builder
                    .creativeTab("westeros_logs_tab")
                    .hardness(2.0f)
                    .resistance(6.0f)
                    .harvestLevel(1)
                    .requiresAxe()
                    .woodType(WoodType.OAK)
                    .soundType("wood")
                    .textures("bark/weirwood/face_6", "bark/weirwood/face_6", "bark/weirwood/side"));

    public static final Block WEIRWOOD_FACE_7 = registerLogBlock(
            "weirwood_face_7",
            builder -> builder
                    .creativeTab("westeros_logs_tab")
                    .hardness(2.0f)
                    .resistance(6.0f)
                    .harvestLevel(1)
                    .requiresAxe()
                    .woodType(WoodType.OAK)
                    .soundType("wood")
                    .textures("bark/weirwood/face_7", "bark/weirwood/face_7", "bark/weirwood/side"));

    public static final Block WEIRWOOD_FACE_8 = registerLogBlock(
            "weirwood_face_8",
            builder -> builder
                    .creativeTab("westeros_logs_tab")
                    .hardness(2.0f)
                    .resistance(6.0f)
                    .harvestLevel(1)
                    .requiresAxe()
                    .woodType(WoodType.OAK)
                    .soundType("wood")
                    .textures("bark/weirwood/face_8", "bark/weirwood/face_8", "bark/weirwood/side"));

    public static final Block WEIRWOOD_SCARS = registerLogBlock(
            "weirwood_scars",
            builder -> builder
                    .creativeTab("westeros_logs_tab")
                    .hardness(2.0f)
                    .resistance(6.0f)
                    .harvestLevel(1)
                    .requiresAxe()
                    .woodType(WoodType.OAK)
                    .soundType("wood")
                    .textures("bark/weirwood/scars", "bark/weirwood/scars", "bark/weirwood/side"));

    // TORCH BLOCKS
    public static final Block TORCH = registerTorchBlock(
            "torch",
            builder -> builder
                    .creativeTab("westeros_lighting_tab")
                    .texture("lighting/torch")
                    .allowUnsupported()
                    .soundType("metal")
                    .lightLevel(13)
                    .alphaRender());

    public static final Block TORCH_UNLIT = registerTorchBlock(
            "torch_unlit",
            builder -> builder
                    .creativeTab("westeros_lighting_tab")
                    .texture("lighting/torch_unlit")
                    .allowUnsupported()
                    .noParticle()
                    .soundType("metal")
                    .lightLevel(0)
                    .alphaRender());

    public static final Block CANDLE = registerTorchBlock(
            "candle",
            builder -> builder
                    .creativeTab("westeros_lighting_tab")
                    .texture("lighting/candle")
                    .allowUnsupported()
                    .soundType("powder")
                    .lightLevel(10)
                    .alphaRender());

    public static final Block CANDLE_UNLIT = registerTorchBlock(
            "candle_unlit",
            builder -> builder
                    .creativeTab("westeros_lighting_tab")
                    .texture("lighting/candle_unlit")
                    .allowUnsupported()
                    .noParticle()
                    .soundType("powder")
                    .lightLevel(0)
                    .alphaRender());

    // DOOR BLOCKS
    public static final Block WHITE_WOOD_DOOR = registerDoorBlock(
            "white_wood_door",
            builder -> builder
                    .creativeTab("westeros_wood_planks_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .harvestLevel(1)
                    .requiresAxe()
                    .woodType(WoodType.BIRCH)
                    .soundType("wood")
                    .allowUnsupported()
                    .textures("wood/white/door_top", "wood/white/door_bottom"));

    public static final Block LOCKED_WHITE_WOOD_DOOR = registerDoorBlock(
            "locked_white_wood_door",
            builder -> builder
                    .creativeTab("westeros_wood_planks_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .harvestLevel(1)
                    .requiresAxe()
                    .woodType(WoodType.BIRCH)
                    .soundType("wood")
                    .locked(true)
                    .allowUnsupported()
                    .textures("wood/white/door_locked_top", "wood/white/door_locked_bottom"));

    // Regular doors
    public static final Block NORTHERN_WOOD_DOOR = registerDoorBlock(
            "northern_wood_door",
            builder -> builder
                    .creativeTab("westeros_wood_planks_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .harvestLevel(1)
                    .requiresAxe()
                    .woodType(WoodType.DARK_OAK)
                    .soundType("wood")
                    .allowUnsupported()
                    .textures("wood/northern/door_top", "wood/northern/door_bottom"));

    public static final Block SPRUCE_DOOR = registerDoorBlock(
            "spruce_door",
            builder -> builder
                    .creativeTab("westeros_wood_planks_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .harvestLevel(1)
                    .requiresAxe()
                    .woodType(WoodType.SPRUCE)
                    .soundType("wood")
                    .allowUnsupported()
                    .textures("wood/spruce/door_top", "wood/spruce/door_bottom"));

    public static final Block OAK_DOOR = registerDoorBlock(
            "oak_door",
            builder -> builder
                    .creativeTab("westeros_wood_planks_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .harvestLevel(1)
                    .requiresAxe()
                    .woodType(WoodType.OAK)
                    .soundType("wood")
                    .allowUnsupported()
                    .textures("wood/oak/door_top", "wood/oak/door_bottom"));

    public static final Block BIRCH_DOOR = registerDoorBlock(
            "birch_door",
            builder -> builder
                    .creativeTab("westeros_wood_planks_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .harvestLevel(1)
                    .requiresAxe()
                    .woodType(WoodType.BIRCH)
                    .soundType("wood")
                    .allowUnsupported()
                    .textures("wood/birch/door_top", "wood/birch/door_bottom"));

    public static final Block EYRIE_WEIRWOOD_DOOR = registerDoorBlock(
            "eyrie_weirwood_door",
            builder -> builder
                    .creativeTab("westeros_wood_planks_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .harvestLevel(1)
                    .requiresAxe()
                    .woodType(WoodType.OAK) // Weirwood uses oak as base
                    .soundType("wood")
                    .allowUnsupported()
                    .textures("door_block/door_weirwood_top", "door_block/door_weirwood_bottom"));

    public static final Block GREY_WOOD_DOOR = registerDoorBlock(
            "grey_wood_door",
            builder -> builder
                    .creativeTab("westeros_wood_planks_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .harvestLevel(1)
                    .requiresAxe()
                    .woodType(WoodType.ACACIA)
                    .soundType("wood")
                    .allowUnsupported()
                    .textures("wood/grey/door_top", "wood/grey/door_bottom"));

    public static final Block JUNGLE_DOOR = registerDoorBlock(
            "jungle_door",
            builder -> builder
                    .creativeTab("westeros_wood_planks_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .harvestLevel(1)
                    .requiresAxe()
                    .woodType(WoodType.JUNGLE)
                    .soundType("wood")
                    .allowUnsupported()
                    .textures("wood/jungle/door_top", "wood/jungle/door_bottom"));

    // Secret doors (stone material)
    public static final Block RED_KEEP_SECRET_DOOR = registerDoorBlock(
            "red_keep_secret_door",
            builder -> builder
                    .creativeTab("westeros_misc_tab")
                    .hardness(7.0f)
                    .resistance(7.0f)
                    .harvestLevel(1)
                    .requiresAxe()
                    .woodType(WoodType.CRIMSON)
                    .soundType("stone")
                    .locked(true)
                    .allowUnsupported()
                    .textures("ashlar_third/pale_red/all_noctm", "ashlar_third/pale_red/all_noctm"));

    public static final Block HARRENHAL_SECRET_DOOR = registerDoorBlock(
            "harrenhal_secret_door",
            builder -> builder
                    .creativeTab("westeros_misc_tab")
                    .hardness(7.0f)
                    .resistance(7.0f)
                    .harvestLevel(1)
                    .requiresAxe()
                    .woodType(WoodType.DARK_OAK)
                    .soundType("stone")
                    .locked(true)
                    .allowUnsupported()
                    .textures("ashlar_third/black/all_noctm", "ashlar_third/black/all_noctm"));

    // Locked doors
    public static final Block LOCKED_BIRCH_DOOR = registerDoorBlock(
            "locked_birch_door",
            builder -> builder
                    .creativeTab("westeros_wood_planks_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .harvestLevel(1)
                    .requiresAxe()
                    .woodType(WoodType.BIRCH)
                    .soundType("wood")
                    .locked(true)
                    .allowUnsupported()
                    .textures("wood/birch/door_locked_top", "wood/birch/door_locked_bottom"));

    public static final Block LOCKED_DARK_NORTHERN_WOOD_DOOR = registerDoorBlock(
            "locked_dark_northern_wood_door",
            builder -> builder
                    .creativeTab("westeros_wood_planks_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .harvestLevel(1)
                    .requiresAxe()
                    .woodType(WoodType.DARK_OAK)
                    .soundType("wood")
                    .locked(true)
                    .allowUnsupported()
                    .textures("wood/northern/door_locked_top", "wood/northern/door_locked_bottom"));

    public static final Block LOCKED_GREY_WOOD_DOOR = registerDoorBlock(
            "locked_grey_wood_door",
            builder -> builder
                    .creativeTab("westeros_wood_planks_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .harvestLevel(1)
                    .requiresAxe()
                    .woodType(WoodType.ACACIA)
                    .soundType("wood")
                    .locked(true)
                    .allowUnsupported()
                    .textures("wood/grey/door_locked_top", "wood/grey/door_locked_bottom"));

    public static final Block LOCKED_SPRUCE_DOOR = registerDoorBlock(
            "locked_spruce_door",
            builder -> builder
                    .creativeTab("westeros_wood_planks_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .harvestLevel(1)
                    .requiresAxe()
                    .woodType(WoodType.SPRUCE)
                    .soundType("wood")
                    .locked(true)
                    .allowUnsupported()
                    .textures("wood/spruce/door_locked_top", "wood/spruce/door_locked_bottom"));

    public static final Block LOCKED_JUNGLE_DOOR = registerDoorBlock(
            "locked_jungle_door",
            builder -> builder
                    .creativeTab("westeros_wood_planks_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .harvestLevel(1)
                    .requiresAxe()
                    .woodType(WoodType.JUNGLE)
                    .soundType("wood")
                    .locked(true)
                    .allowUnsupported()
                    .textures("wood/jungle/door_locked_top", "wood/jungle/door_locked_bottom"));

    public static final Block LOCKED_OAK_DOOR = registerDoorBlock(
            "locked_oak_door",
            builder -> builder
                    .creativeTab("westeros_wood_planks_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .harvestLevel(1)
                    .requiresAxe()
                    .woodType(WoodType.OAK)
                    .soundType("wood")
                    .locked(true)
                    .allowUnsupported()
                    .textures("wood/oak/door_locked_top", "wood/oak/door_locked_bottom"));

    // SAND BLOCKS
    public static final Block SAND_SKELETON = registerSandBlock(
            "sand_skeleton",
            builder -> builder
                    .creativeTab("westeros_sand_gravel_tab")
                    .hardness(0.5f)
                    .resistance(0.5f)
                    .requiresShovel()
                    .soundType("sand")
                    .texture("sand_block/sand_skeleton"));

    // CORAL FAN BLOCKS
    public static final Block CORAL_BRAIN_FAN = registerFanBlock(
            "coral_brain_fan",
            builder -> builder
                    .creativeTab("westeros_water_air_tab")
                    .hardness(0.0f)
                    .resistance(0.0f)
                    .noCollision()
                    .dropsNothing()
                    .soundType("stone")
                    .allowUnsupported()
                    .nonOpaque()
                    .alphaRender()
                    .texture("coral/brain/fan1"));

    public static final Block CORAL_BUBBLE_FAN = registerFanBlock(
            "coral_bubble_fan",
            builder -> builder
                    .creativeTab("westeros_water_air_tab")
                    .hardness(0.0f)
                    .resistance(0.0f)
                    .noCollision()
                    .dropsNothing()
                    .soundType("stone")
                    .allowUnsupported()
                    .nonOpaque()
                    .alphaRender()
                    .texture("coral/bubble/fan1"));

    public static final Block CORAL_FIRE_FAN = registerFanBlock(
            "coral_fire_fan",
            builder -> builder
                    .creativeTab("westeros_water_air_tab")
                    .hardness(0.0f)
                    .resistance(0.0f)
                    .noCollision()
                    .dropsNothing()
                    .soundType("stone")
                    .allowUnsupported()
                    .nonOpaque()
                    .alphaRender()
                    .texture("coral/fire/fan1"));

    public static final Block CORAL_HORN_FAN = registerFanBlock(
            "coral_horn_fan",
            builder -> builder
                    .creativeTab("westeros_water_air_tab")
                    .hardness(0.0f)
                    .resistance(0.0f)
                    .noCollision()
                    .dropsNothing()
                    .soundType("stone")
                    .allowUnsupported()
                    .nonOpaque()
                    .alphaRender()
                    .texture("coral/horn/fan1"));

    public static final Block CORAL_TUBE_FAN = registerFanBlock(
            "coral_tube_fan",
            builder -> builder
                    .creativeTab("westeros_water_air_tab")
                    .hardness(0.0f)
                    .resistance(0.0f)
                    .noCollision()
                    .dropsNothing()
                    .soundType("stone")
                    .allowUnsupported()
                    .nonOpaque()
                    .alphaRender()
                    .texture("coral/tube/fan1"));

    // VINES BLOCKS
    public static final Block DAPPLED_MOSS = registerVinesBlock(
            "dappled_moss",
            builder -> builder
                    .creativeTab("westeros_foliage_tab")
                    .hardness(0.0f)
                    .resistance(0.0f)
                    .noCollision()
                    .dropsNothing()
                    .soundType("grass")
                    .nonOpaque()
                    .alphaRender()
                    .allowUnsupported()
                    .canGrowDownward()
                    .texture("dappled_moss/dappled"));

    public static final Block JASMINE_VINES = registerVinesBlock(
            "jasmine_vines",
            builder -> builder
                    .creativeTab("westeros_foliage_tab")
                    .hardness(0.0f)
                    .resistance(0.0f)
                    .noCollision()
                    .dropsNothing()
                    .soundType("grass")
                    .nonOpaque()
                    .alphaRender()
                    .allowUnsupported()
                    .canGrowDownward()
                    .textures("jasmine_vines/side1", "jasmine_vines/side1"));

    // Additional vine blocks from JSON definitions
    public static final Block VINES = registerVinesBlock(
            "vines",
            builder -> builder
                    .creativeTab("westeros_foliage_tab")
                    .hardness(0.0f)
                    .resistance(0.0f)
                    .noCollision()
                    .dropsNothing()
                    .soundType("grass")
                    .nonOpaque()
                    .alphaRender()
                    .allowUnsupported()
                    .canGrowDownward()
                    .textures("vines/side1", "vines/side1"));

    public static final Block FALLING_WATER_BLOCK_ONE = registerVinesBlock(
            "falling_water_block_one",
            builder -> builder
                    .creativeTab("westeros_water_air_tab")
                    .hardness(0.0f)
                    .resistance(0.0f)
                    .noCollision()
                    .dropsNothing()
                    .soundType("cloth")
                    .nonOpaque()
                    .alphaRender()
                    .allowUnsupported()
                    .textures("alyssas_tears_mist/mist1", "alyssas_tears_mist/mist1"));

    public static final Block FALLING_WATER_BLOCK_TWO = registerVinesBlock(
            "falling_water_block_two",
            builder -> builder
                    .creativeTab("westeros_water_air_tab")
                    .hardness(0.0f)
                    .resistance(0.0f)
                    .noCollision()
                    .dropsNothing()
                    .soundType("cloth")
                    .nonOpaque()
                    .alphaRender()
                    .allowUnsupported()
                    .textures("alyssas_tears_mist/mist2", "alyssas_tears_mist/mist2"));

    public static final Block FALLING_WATER_BLOCK_THREE = registerVinesBlock(
            "falling_water_block_three",
            builder -> builder
                    .creativeTab("westeros_water_air_tab")
                    .hardness(0.0f)
                    .resistance(0.0f)
                    .noCollision()
                    .dropsNothing()
                    .soundType("cloth")
                    .nonOpaque()
                    .alphaRender()
                    .allowUnsupported()
                    .textures("alyssas_tears_mist/mist3", "alyssas_tears_mist/mist3"));

    public static final Block FALLING_WATER_BLOCK_FOUR = registerVinesBlock(
            "falling_water_block_four",
            builder -> builder
                    .creativeTab("westeros_water_air_tab")
                    .hardness(0.0f)
                    .resistance(0.0f)
                    .noCollision()
                    .dropsNothing()
                    .soundType("cloth")
                    .nonOpaque()
                    .alphaRender()
                    .allowUnsupported()
                    .textures("alyssas_tears_mist/mist4", "alyssas_tears_mist/mist4"));

    // HALF DOOR BLOCKS
    public static final Block BIRCH_WINDOW_SHUTTERS = registerHalfDoorBlock(
            "birch_window_shutters",
            builder -> builder
                    .creativeTab("westeros_windows_glass_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .harvestLevel(1)
                    .requiresAxe()
                    .woodType(WoodType.BIRCH)
                    .soundType("wood")
                    .allowUnsupported()
                    .texture("wood/birch/shutters"));

    public static final Block DORNE_RED_WINDOW_SHUTTERS = registerHalfDoorBlock(
            "dorne_red_window_shutters",
            builder -> builder
                    .creativeTab("westeros_windows_glass_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .harvestLevel(1)
                    .requiresAxe()
                    .woodType(WoodType.OAK)
                    .soundType("wood")
                    .allowUnsupported()
                    .texture("shutter_block/shutters_dorne"));

    public static final Block GREEN_LANNISPORT_WINDOW_SHUTTERS = registerHalfDoorBlock(
            "green_lannisport_window_shutters",
            builder -> builder
                    .creativeTab("westeros_windows_glass_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .harvestLevel(1)
                    .requiresAxe()
                    .woodType(WoodType.OAK)
                    .soundType("wood")
                    .allowUnsupported()
                    .texture("shutter_block/shutters_lannisport"));

    public static final Block GREY_WOOD_WINDOW_SHUTTERS = registerHalfDoorBlock(
            "grey_wood_window_shutters",
            builder -> builder
                    .creativeTab("westeros_windows_glass_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .harvestLevel(1)
                    .requiresAxe()
                    .woodType(WoodType.ACACIA)
                    .soundType("wood")
                    .allowUnsupported()
                    .texture("wood/grey/shutters"));

    public static final Block JUNGLE_WINDOW_SHUTTERS = registerHalfDoorBlock(
            "jungle_window_shutters",
            builder -> builder
                    .creativeTab("westeros_windows_glass_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .harvestLevel(1)
                    .requiresAxe()
                    .woodType(WoodType.JUNGLE)
                    .soundType("wood")
                    .allowUnsupported()
                    .texture("wood/jungle/shutters"));

    public static final Block NORTHERN_WOOD_WINDOW_SHUTTERS = registerHalfDoorBlock(
            "northern_wood_window_shutters",
            builder -> builder
                    .creativeTab("westeros_windows_glass_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .harvestLevel(1)
                    .requiresAxe()
                    .woodType(WoodType.DARK_OAK)
                    .soundType("wood")
                    .allowUnsupported()
                    .texture("wood/northern/shutters"));

    public static final Block OAK_WINDOW_SHUTTERS = registerHalfDoorBlock(
            "oak_window_shutters",
            builder -> builder
                    .creativeTab("westeros_windows_glass_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .harvestLevel(1)
                    .requiresAxe()
                    .woodType(WoodType.OAK)
                    .soundType("wood")
                    .allowUnsupported()
                    .texture("wood/oak/shutters"));

    public static final Block REACH_BLUE_WINDOW_SHUTTERS = registerHalfDoorBlock(
            "reach_blue_window_shutters",
            builder -> builder
                    .creativeTab("westeros_windows_glass_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .harvestLevel(1)
                    .requiresAxe()
                    .woodType(WoodType.OAK)
                    .soundType("wood")
                    .allowUnsupported()
                    .texture("shutter_block/shutters_reach"));

    public static final Block SPRUCE_WINDOW_SHUTTERS = registerHalfDoorBlock(
            "spruce_window_shutters",
            builder -> builder
                    .creativeTab("westeros_windows_glass_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .harvestLevel(1)
                    .requiresAxe()
                    .woodType(WoodType.SPRUCE)
                    .soundType("wood")
                    .allowUnsupported()
                    .texture("wood/spruce/shutters"));

    public static final Block WHITE_WOOD_WINDOW_SHUTTERS = registerHalfDoorBlock(
            "white_wood_window_shutters",
            builder -> builder
                    .creativeTab("westeros_windows_glass_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .harvestLevel(1)
                    .requiresAxe()
                    .woodType(WoodType.BIRCH)
                    .soundType("wood")
                    .allowUnsupported()
                    .texture("wood/white/shutters"));

    // PLANT BLOCKS
    public static final Block BLUE_BELLS = registerPlantBlock(
            "blue_bells",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .hardness(0.0f)
                    .resistance(0.0f)
                    .noCollision()
                    .breakInstantly()
                    .soundType("grass")
                    .nonOpaque()
                    .layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2) // 0.3-0.7, 0-0.6, 0.3-0.7
                    .texture("flowers/blue_bells"));

    public static final Block BLUE_CHICORY = registerPlantBlock(
            "blue_chicory",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .hardness(0.0f)
                    .resistance(0.0f)
                    .noCollision()
                    .breakInstantly()
                    .soundType("grass")
                    .nonOpaque()
                    .layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2) // 0.3-0.7, 0-0.6, 0.3-0.7
                    .texture("flowers/blue_chicory/side1"));

    public static final Block BLUE_FLAX = registerPlantBlock(
            "blue_flax",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .hardness(0.0f)
                    .resistance(0.0f)
                    .noCollision()
                    .breakInstantly()
                    .soundType("grass")
                    .nonOpaque()
                    .layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2) // 0.3-0.7, 0-0.6, 0.3-0.7
                    .texture("flowers/blue_flax1"));

    public static final Block BLUE_FORGETMENOTS = registerPlantBlock(
            "blue_forgetmenots",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .hardness(0.0f)
                    .resistance(0.0f)
                    .noCollision()
                    .breakInstantly()
                    .soundType("grass")
                    .nonOpaque()
                    .layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2) // 0.3-0.7, 0-0.6, 0.3-0.7
                    .texture("flowers/blue_forgetmenots1"));

    public static final Block BLUE_HYACINTH = registerPlantBlock(
            "blue_hyacinth",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .hardness(0.0f)
                    .resistance(0.0f)
                    .noCollision()
                    .breakInstantly()
                    .soundType("grass")
                    .nonOpaque()
                    .layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2) // 0.3-0.7, 0-0.6, 0.3-0.7
                    .texture("flowers/blue_hyacinth1"));

    public static final Block BLUE_ORCHID = registerPlantBlock(
            "blue_orchid",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .hardness(0.0f)
                    .resistance(0.0f)
                    .noCollision()
                    .breakInstantly()
                    .soundType("grass")
                    .nonOpaque()
                    .layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2) // 0.3-0.7, 0-0.6, 0.3-0.7
                    .texture("flowers/blue_orchid1"));

    public static final Block BLUE_SWAMP_BELLS = registerPlantBlock(
            "blue_swamp_bells",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .hardness(0.0f)
                    .resistance(0.0f)
                    .noCollision()
                    .breakInstantly()
                    .soundType("grass")
                    .nonOpaque()
                    .layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2) // 0.3-0.7, 0-0.6, 0.3-0.7
                    .texture("flowers/blue_swamp_bells1"));

    public static final Block BRACKEN = registerPlantBlock(
            "bracken",
            builder -> builder
                    .creativeTab("westeros_grasses_shrubs_tab")
                    .hardness(0.0f)
                    .resistance(0.0f)
                    .noCollision()
                    .breakInstantly()
                    .soundType("grass")
                    .nonOpaque()
                    .layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2) // 0.3-0.7, 0-0.6, 0.3-0.7
                    .texture("bracken/side1"));

    // BROWN MUSHROOM BLOCKS
    public static final Block BROWN_MUSHROOM_1 = registerPlantBlock(
            "brown_mushroom_1",
            builder -> builder
                    .creativeTab("westeros_grasses_shrubs_tab")
                    .hardness(0.0f)
                    .resistance(0.0f)
                    .noCollision()
                    .breakInstantly()
                    .soundType("grass")
                    .nonOpaque()
                    .layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2)
                    .texture("brown_mushroom_block/mushroom_brown_0"));

    public static final Block BROWN_MUSHROOM_2 = registerPlantBlock(
            "brown_mushroom_2",
            builder -> builder
                    .creativeTab("westeros_grasses_shrubs_tab")
                    .hardness(0.0f)
                    .resistance(0.0f)
                    .noCollision()
                    .breakInstantly()
                    .soundType("grass")
                    .nonOpaque()
                    .layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2)
                    .texture("brown_mushroom_block/mushroom_brown_1"));

    public static final Block BROWN_MUSHROOM_3 = registerPlantBlock(
            "brown_mushroom_3",
            builder -> builder
                    .creativeTab("westeros_grasses_shrubs_tab")
                    .hardness(0.0f)
                    .resistance(0.0f)
                    .noCollision()
                    .breakInstantly()
                    .soundType("grass")
                    .nonOpaque()
                    .layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2)
                    .texture("brown_mushroom_block/mushroom_brown_2"));

    public static final Block BROWN_MUSHROOM_4 = registerPlantBlock(
            "brown_mushroom_4",
            builder -> builder
                    .creativeTab("westeros_grasses_shrubs_tab")
                    .hardness(0.0f)
                    .resistance(0.0f)
                    .noCollision()
                    .breakInstantly()
                    .soundType("grass")
                    .nonOpaque()
                    .layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2)
                    .texture("brown_mushroom_block/mushroom_brown_3"));

    public static final Block BROWN_MUSHROOM_5 = registerPlantBlock(
            "brown_mushroom_5",
            builder -> builder
                    .creativeTab("westeros_grasses_shrubs_tab")
                    .hardness(0.0f)
                    .resistance(0.0f)
                    .noCollision()
                    .breakInstantly()
                    .soundType("grass")
                    .nonOpaque()
                    .layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2)
                    .texture("brown_mushroom_block/mushroom_brown_4"));

    public static final Block BROWN_MUSHROOM_6 = registerPlantBlock(
            "brown_mushroom_6",
            builder -> builder
                    .creativeTab("westeros_grasses_shrubs_tab")
                    .hardness(0.0f)
                    .resistance(0.0f)
                    .noCollision()
                    .breakInstantly()
                    .soundType("grass")
                    .nonOpaque()
                    .layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2)
                    .texture("brown_mushroom_block/mushroom_brown_5"));

    public static final Block BROWN_MUSHROOM_7 = registerPlantBlock(
            "brown_mushroom_7",
            builder -> builder
                    .creativeTab("westeros_grasses_shrubs_tab")
                    .hardness(0.0f)
                    .resistance(0.0f)
                    .noCollision()
                    .breakInstantly()
                    .soundType("grass")
                    .nonOpaque()
                    .layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2)
                    .texture("brown_mushroom_block/mushroom_brown_6"));

    public static final Block BROWN_MUSHROOM_8 = registerPlantBlock(
            "brown_mushroom_8",
            builder -> builder
                    .creativeTab("westeros_grasses_shrubs_tab")
                    .hardness(0.0f)
                    .resistance(0.0f)
                    .noCollision()
                    .breakInstantly()
                    .soundType("grass")
                    .nonOpaque()
                    .layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2)
                    .texture("brown_mushroom_block/mushroom_brown_7"));

    public static final Block BROWN_MUSHROOM_9 = registerPlantBlock(
            "brown_mushroom_9",
            builder -> builder
                    .creativeTab("westeros_grasses_shrubs_tab")
                    .hardness(0.0f)
                    .resistance(0.0f)
                    .noCollision()
                    .breakInstantly()
                    .soundType("grass")
                    .nonOpaque()
                    .layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2)
                    .texture("brown_mushroom_block/mushroom_brown_8"));

    public static final Block BROWN_MUSHROOM_10 = registerPlantBlock(
            "brown_mushroom_10",
            builder -> builder
                    .creativeTab("westeros_grasses_shrubs_tab")
                    .hardness(0.0f)
                    .resistance(0.0f)
                    .noCollision()
                    .breakInstantly()
                    .soundType("grass")
                    .nonOpaque()
                    .layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2)
                    .texture("brown_mushroom_block/mushroom_brown_9"));

    public static final Block BROWN_MUSHROOM_11 = registerPlantBlock(
            "brown_mushroom_11",
            builder -> builder
                    .creativeTab("westeros_grasses_shrubs_tab")
                    .hardness(0.0f)
                    .resistance(0.0f)
                    .noCollision()
                    .breakInstantly()
                    .soundType("grass")
                    .nonOpaque()
                    .layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2)
                    .texture("brown_mushroom_block/mushroom_brown_10"));

    public static final Block BROWN_MUSHROOM_12 = registerPlantBlock(
            "brown_mushroom_12",
            builder -> builder
                    .creativeTab("westeros_grasses_shrubs_tab")
                    .hardness(0.0f)
                    .resistance(0.0f)
                    .noCollision()
                    .breakInstantly()
                    .soundType("grass")
                    .nonOpaque()
                    .layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2)
                    .texture("brown_mushroom_block/mushroom_brown_11"));

    public static final Block BROWN_MUSHROOM_13 = registerPlantBlock(
            "brown_mushroom_13",
            builder -> builder
                    .creativeTab("westeros_grasses_shrubs_tab")
                    .hardness(0.0f)
                    .resistance(0.0f)
                    .noCollision()
                    .breakInstantly()
                    .soundType("grass")
                    .nonOpaque()
                    .layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2)
                    .texture("brown_mushroom_block/mushroom_brown_12"));

    // CORAL WEB BLOCKS
    public static final Block CORAL_BRAIN_WEB = registerPlantBlock(
            "coral_brain_web",
            builder -> builder
                    .creativeTab("westeros_water_air_tab")
                    .hardness(0.0f)
                    .resistance(0.0f)
                    .noCollision()
                    .breakInstantly()
                    .soundType("stone")
                    .nonOpaque()
                    .layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2)
                    .texture("coral/brain/web1"));

    public static final Block CORAL_BUBBLE_WEB = registerPlantBlock(
            "coral_bubble_web",
            builder -> builder
                    .creativeTab("westeros_water_air_tab")
                    .hardness(0.0f)
                    .resistance(0.0f)
                    .noCollision()
                    .breakInstantly()
                    .soundType("stone")
                    .nonOpaque()
                    .layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2)
                    .texture("coral/bubble/web1"));

    public static final Block CORAL_FIRE_WEB = registerPlantBlock(
            "coral_fire_web",
            builder -> builder
                    .creativeTab("westeros_water_air_tab")
                    .hardness(0.0f)
                    .resistance(0.0f)
                    .noCollision()
                    .breakInstantly()
                    .soundType("stone")
                    .nonOpaque()
                    .layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2)
                    .texture("coral/fire/web1"));

    public static final Block CORAL_HORN_WEB = registerPlantBlock(
            "coral_horn_web",
            builder -> builder
                    .creativeTab("westeros_water_air_tab")
                    .hardness(0.0f)
                    .resistance(0.0f)
                    .noCollision()
                    .breakInstantly()
                    .soundType("stone")
                    .nonOpaque()
                    .layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2)
                    .texture("coral/horn/web1"));

    public static final Block CORAL_TUBE_WEB = registerPlantBlock(
            "coral_tube_web",
            builder -> builder
                    .creativeTab("westeros_water_air_tab")
                    .hardness(0.0f)
                    .resistance(0.0f)
                    .noCollision()
                    .breakInstantly()
                    .soundType("stone")
                    .nonOpaque()
                    .layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2)
                    .texture("coral/tube/web1"));

    // ADDITIONAL PLANT BLOCKS
    public static final Block COW_PARSELY = registerPlantBlock(
            "cow_parsely",
            builder -> builder
                    .creativeTab("westeros_grasses_shrubs_tab")
                    .hardness(0.0f)
                    .resistance(0.0f)
                    .noCollision()
                    .breakInstantly()
                    .soundType("grass")
                    .nonOpaque()
                    .layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2)
                    .texture("cow_parsely/side1"));

    public static final Block CRANBERRY_BUSH = registerPlantBlock(
            "cranberry_bush",
            builder -> builder
                    .creativeTab("westeros_crops_herbs_tab")
                    .hardness(0.0f)
                    .resistance(0.0f)
                    .noCollision()
                    .breakInstantly()
                    .soundType("grass")
                    .nonOpaque()
                    .layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2)
                    .texture("cranberry/base1"));

    public static final Block DEAD_BRACKEN = registerPlantBlock(
            "dead_bracken",
            builder -> builder
                    .creativeTab("westeros_grasses_shrubs_tab")
                    .hardness(0.0f)
                    .resistance(0.0f)
                    .noCollision()
                    .breakInstantly()
                    .soundType("grass")
                    .nonOpaque()
                    .layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2)
                    .texture("dead_bracken/side1"));

    public static final Block DEAD_BUSH = registerPlantBlock(
            "dead_bush",
            builder -> builder
                    .creativeTab("westeros_grasses_shrubs_tab")
                    .hardness(0.0f)
                    .resistance(0.0f)
                    .noCollision()
                    .breakInstantly()
                    .soundType("grass")
                    .nonOpaque()
                    .layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2)
                    .texture("dorne_bush_thorny/side1"));

    public static final Block DEAD_SCRUB_GRASS = registerPlantBlock(
            "dead_scrub_grass",
            builder -> builder
                    .creativeTab("westeros_grasses_shrubs_tab")
                    .hardness(0.0f)
                    .resistance(0.0f)
                    .noCollision()
                    .breakInstantly()
                    .soundType("grass")
                    .nonOpaque()
                    .layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2)
                    .texture("flowers/dead_scrub_grass1"));

    public static final Block DOCK_LEAF = registerPlantBlock(
            "dock_leaf",
            builder -> builder
                    .creativeTab("westeros_crops_herbs_tab")
                    .hardness(0.0f)
                    .resistance(0.0f)
                    .noCollision()
                    .breakInstantly()
                    .soundType("grass")
                    .nonOpaque()
                    .layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2)
                    .texture("dock_leaf/side1"));

    public static final Block FIREWEED = registerPlantBlock(
            "fireweed",
            builder -> builder
                    .creativeTab("westeros_grasses_shrubs_tab")
                    .hardness(0.0f)
                    .resistance(0.0f)
                    .noCollision()
                    .breakInstantly()
                    .soundType("grass")
                    .nonOpaque()
                    .layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2)
                    .texture("fireweed/side1"));

    public static final Block GRASS = registerPlantBlock(
            "grass",
            builder -> builder
                    .creativeTab("westeros_grasses_shrubs_tab")
                    .hardness(0.0f)
                    .resistance(0.0f)
                    .noCollision()
                    .breakInstantly()
                    .soundType("grass")
                    .nonOpaque()
                    .layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2)
                    .texture("minecraft:block/fern/fern1"));

    public static final Block GREEN_LEAFY_HERB = registerPlantBlock(
            "green_leafy_herb",
            builder -> builder
                    .creativeTab("westeros_grasses_shrubs_tab")
                    .hardness(0.0f)
                    .resistance(0.0f)
                    .noCollision()
                    .breakInstantly()
                    .soundType("grass")
                    .nonOpaque()
                    .layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2)
                    .texture("flowers/green_leafy_herb"));

    public static final Block GREEN_SCRUB_GRASS = registerPlantBlock(
            "green_scrub_grass",
            builder -> builder
                    .creativeTab("westeros_grasses_shrubs_tab")
                    .hardness(0.0f)
                    .resistance(0.0f)
                    .noCollision()
                    .breakInstantly()
                    .soundType("grass")
                    .nonOpaque()
                    .layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2)
                    .texture("flowers/green_scrub_grass1"));

    public static final Block GREEN_SPINY_HERB = registerPlantBlock(
            "green_spiny_herb",
            builder -> builder
                    .creativeTab("westeros_grasses_shrubs_tab")
                    .hardness(0.0f)
                    .resistance(0.0f)
                    .noCollision()
                    .breakInstantly()
                    .soundType("grass")
                    .nonOpaque()
                    .layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2)
                    .texture("flowers/green_spiny_herb1"));

    public static final Block HEATHER = registerPlantBlock(
            "heather",
            builder -> builder
                    .creativeTab("westeros_grasses_shrubs_tab")
                    .hardness(0.0f)
                    .resistance(0.0f)
                    .noCollision()
                    .breakInstantly()
                    .soundType("grass")
                    .nonOpaque()
                    .layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2)
                    .texture("flowers/heather/side1"));

    public static final Block KELP = registerPlantBlock(
            "kelp",
            builder -> builder
                    .creativeTab("westeros_water_air_tab")
                    .hardness(0.0f)
                    .resistance(0.0f)
                    .noCollision()
                    .breakInstantly()
                    .soundType("grass")
                    .nonOpaque()
                    .layerSensitive()
                    .texture("kelp/side1"));

    public static final Block LADY_FERN = registerPlantBlock(
            "lady_fern",
            builder -> builder
                    .creativeTab("westeros_grasses_shrubs_tab")
                    .hardness(0.0f)
                    .resistance(0.0f)
                    .noCollision()
                    .breakInstantly()
                    .soundType("grass")
                    .nonOpaque()
                    .layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2)
                    .texture("flowers/lady_fern"));

    // FLOWER BLOCKS
    public static final Block MAGENTA_ROSES = registerPlantBlock(
            "magenta_roses",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .hardness(0.0f)
                    .resistance(0.0f)
                    .noCollision()
                    .breakInstantly()
                    .soundType("grass")
                    .nonOpaque()
                    .layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2)
                    .texture("flowers/magenta_roses1"));

    public static final Block MEADOW_FESCUE = registerPlantBlock(
            "meadow_fescue",
            builder -> builder
                    .creativeTab("westeros_grasses_shrubs_tab")
                    .hardness(0.0f)
                    .resistance(0.0f)
                    .noCollision()
                    .breakInstantly()
                    .soundType("grass")
                    .nonOpaque()
                    .layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2)
                    .texture("flowers/meadow_fescue/side1"));

    public static final Block NETTLE = registerPlantBlock(
            "nettle",
            builder -> builder
                    .creativeTab("westeros_grasses_shrubs_tab")
                    .hardness(0.0f)
                    .resistance(0.0f)
                    .noCollision()
                    .breakInstantly()
                    .soundType("grass")
                    .nonOpaque()
                    .layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2)
                    .texture("flowers/nettle"));

    public static final Block ORANGE_BELLS = registerPlantBlock(
            "orange_bells",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .hardness(0.0f)
                    .resistance(0.0f)
                    .noCollision()
                    .breakInstantly()
                    .soundType("grass")
                    .nonOpaque()
                    .layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2)
                    .texture("flowers/orange_bells1"));

    public static final Block ORANGE_BOG_ASPHODEL = registerPlantBlock(
            "orange_bog_asphodel",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .hardness(0.0f)
                    .resistance(0.0f)
                    .noCollision()
                    .breakInstantly()
                    .soundType("grass")
                    .nonOpaque()
                    .layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2)
                    .texture("flowers/orange_bog_asphodel1"));

    public static final Block ORANGE_MARIGOLDS = registerPlantBlock(
            "orange_marigolds",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .hardness(0.0f)
                    .resistance(0.0f)
                    .noCollision()
                    .breakInstantly()
                    .soundType("grass")
                    .nonOpaque()
                    .layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2)
                    .texture("flowers/orange_marigolds1"));

    public static final Block ORANGE_SUN_STAR = registerPlantBlock(
            "orange_sun_star",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .hardness(0.0f)
                    .resistance(0.0f)
                    .noCollision()
                    .breakInstantly()
                    .soundType("grass")
                    .nonOpaque()
                    .layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2)
                    .texture("flowers/orange_sun_star"));

    public static final Block ORANGE_TROLLIUS = registerPlantBlock(
            "orange_trollius",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .hardness(0.0f)
                    .resistance(0.0f)
                    .noCollision()
                    .breakInstantly()
                    .soundType("grass")
                    .nonOpaque()
                    .layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2)
                    .texture("flowers/orange_trollius1"));

    public static final Block PINK_ALLIUM = registerPlantBlock("pink_allium",
            builder -> builder.creativeTab("westeros_flowers_tab").hardness(0.0f).resistance(0.0f).noCollision()
                    .breakInstantly().soundType("grass").nonOpaque().layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2).texture("flowers/pink_allium1"));
    public static final Block PINK_PRIMROSE = registerPlantBlock("pink_primrose",
            builder -> builder.creativeTab("westeros_flowers_tab").hardness(0.0f).resistance(0.0f).noCollision()
                    .breakInstantly().soundType("grass").nonOpaque().layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2).texture("flowers/pink_primrose1"));
    public static final Block PINK_ROSES = registerPlantBlock("pink_roses",
            builder -> builder.creativeTab("westeros_flowers_tab").hardness(0.0f).resistance(0.0f).noCollision()
                    .breakInstantly().soundType("grass").nonOpaque().layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2).texture("flowers/pink_roses1"));
    public static final Block PINK_SWEET_PEAS = registerPlantBlock("pink_sweet_peas",
            builder -> builder.creativeTab("westeros_flowers_tab").hardness(0.0f).resistance(0.0f).noCollision()
                    .breakInstantly().soundType("grass").nonOpaque().layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2).texture("flowers/pink_sweet_peas1"));
    public static final Block PINK_THISTLE = registerPlantBlock("pink_thistle",
            builder -> builder.creativeTab("westeros_flowers_tab").hardness(0.0f).resistance(0.0f).noCollision()
                    .breakInstantly().soundType("grass").nonOpaque().layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2).texture("flowers/pink_thistle/side1"));
    public static final Block PINK_TULIPS = registerPlantBlock("pink_tulips",
            builder -> builder.creativeTab("westeros_flowers_tab").hardness(0.0f).resistance(0.0f).noCollision()
                    .breakInstantly().soundType("grass").nonOpaque().layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2).texture("flowers/pink_tulips1"));
    public static final Block PINK_WILDFLOWERS = registerPlantBlock("pink_wildflowers",
            builder -> builder.creativeTab("westeros_flowers_tab").hardness(0.0f).resistance(0.0f).noCollision()
                    .breakInstantly().soundType("grass").nonOpaque().layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2).texture("flowers/pink_wildflowers"));
    public static final Block PURPLE_ALPINE_SOWTHISTLE = registerPlantBlock("purple_alpine_sowthistle",
            builder -> builder.creativeTab("westeros_flowers_tab").hardness(0.0f).resistance(0.0f).noCollision()
                    .breakInstantly().soundType("grass").nonOpaque().layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2).texture("flowers/purple_alpine_sowthistle/side1"));
    public static final Block PURPLE_FOXGLOVE = registerPlantBlock("purple_foxglove",
            builder -> builder.creativeTab("westeros_flowers_tab").hardness(0.0f).resistance(0.0f).noCollision()
                    .breakInstantly().soundType("grass").nonOpaque().layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2).texture("flowers/purple_foxglove1"));
    public static final Block PURPLE_LAVENDER = registerPlantBlock("purple_lavender",
            builder -> builder.creativeTab("westeros_flowers_tab").hardness(0.0f).resistance(0.0f).noCollision()
                    .breakInstantly().soundType("grass").nonOpaque().layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2).texture("flowers/purple_lavender1"));
    public static final Block PURPLE_PANSIES = registerPlantBlock("purple_pansies",
            builder -> builder.creativeTab("westeros_flowers_tab").hardness(0.0f).resistance(0.0f).noCollision()
                    .breakInstantly().soundType("grass").nonOpaque().layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2).texture("flowers/purple_pansies1"));
    public static final Block PURPLE_ROSES = registerPlantBlock("purple_roses",
            builder -> builder.creativeTab("westeros_flowers_tab").hardness(0.0f).resistance(0.0f).noCollision()
                    .breakInstantly().soundType("grass").nonOpaque().layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2).texture("flowers/purple_roses1"));
    public static final Block PURPLE_VIOLETS = registerPlantBlock("purple_violets",
            builder -> builder.creativeTab("westeros_flowers_tab").hardness(0.0f).resistance(0.0f).noCollision()
                    .breakInstantly().soundType("grass").nonOpaque().layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2).texture("flowers/purple_violets1"));
    public static final Block RED_ASTER = registerPlantBlock("red_aster",
            builder -> builder.creativeTab("westeros_flowers_tab").hardness(0.0f).resistance(0.0f).noCollision()
                    .breakInstantly().soundType("grass").nonOpaque().layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2).texture("flowers/red_aster1"));
    public static final Block RED_CARNATIONS = registerPlantBlock("red_carnations",
            builder -> builder.creativeTab("westeros_flowers_tab").hardness(0.0f).resistance(0.0f).noCollision()
                    .breakInstantly().soundType("grass").nonOpaque().layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2).texture("flowers/red_carnations1"));
    public static final Block RED_CHRYSANTHEMUM = registerPlantBlock("red_chrysanthemum",
            builder -> builder.creativeTab("westeros_flowers_tab").hardness(0.0f).resistance(0.0f).noCollision()
                    .breakInstantly().soundType("grass").nonOpaque().layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2).texture("flowers/red_chrysanthemum1"));
    public static final Block RED_DARK_ROSES = registerPlantBlock("red_dark_roses",
            builder -> builder.creativeTab("westeros_flowers_tab").hardness(0.0f).resistance(0.0f).noCollision()
                    .breakInstantly().soundType("grass").nonOpaque().layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2).texture("flowers/red_dark_roses1"));
    public static final Block RED_FERN = registerPlantBlock("red_fern",
            builder -> builder.creativeTab("westeros_grasses_shrubs_tab").hardness(0.0f).resistance(0.0f).noCollision()
                    .breakInstantly().soundType("grass").nonOpaque().layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2).texture("red_fern/side1"));
    public static final Block RED_FLOWERING_SPINY_HERB = registerPlantBlock("red_flowering_spiny_herb",
            builder -> builder.creativeTab("westeros_flowers_tab").hardness(0.0f).resistance(0.0f).noCollision()
                    .breakInstantly().soundType("grass").nonOpaque().layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2).texture("flowers/red_flowering_spiny_herb1"));
    public static final Block RED_MUSHROOM_1 = registerPlantBlock("red_mushroom_1",
            builder -> builder.creativeTab("westeros_grasses_shrubs_tab").hardness(0.0f).resistance(0.0f).noCollision()
                    .breakInstantly().soundType("grass").nonOpaque().layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2).texture("red_mushroom_block/mushroom_red_0"));
    public static final Block RED_MUSHROOM_2 = registerPlantBlock("red_mushroom_2",
            builder -> builder.creativeTab("westeros_grasses_shrubs_tab").hardness(0.0f).resistance(0.0f).noCollision()
                    .breakInstantly().soundType("grass").nonOpaque().layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2).texture("red_mushroom_block/mushroom_red_1"));
    public static final Block RED_MUSHROOM_3 = registerPlantBlock("red_mushroom_3",
            builder -> builder.creativeTab("westeros_grasses_shrubs_tab").hardness(0.0f).resistance(0.0f).noCollision()
                    .breakInstantly().soundType("grass").nonOpaque().layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2).texture("red_mushroom_block/mushroom_red_2"));
    public static final Block RED_MUSHROOM_4 = registerPlantBlock("red_mushroom_4",
            builder -> builder.creativeTab("westeros_grasses_shrubs_tab").hardness(0.0f).resistance(0.0f).noCollision()
                    .breakInstantly().soundType("grass").nonOpaque().layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2).texture("red_mushroom_block/mushroom_red_3"));
    public static final Block RED_MUSHROOM_5 = registerPlantBlock("red_mushroom_5",
            builder -> builder.creativeTab("westeros_grasses_shrubs_tab").hardness(0.0f).resistance(0.0f).noCollision()
                    .breakInstantly().soundType("grass").nonOpaque().layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2).texture("red_mushroom_block/mushroom_red_4"));
    public static final Block RED_MUSHROOM_6 = registerPlantBlock("red_mushroom_6",
            builder -> builder.creativeTab("westeros_grasses_shrubs_tab").hardness(0.0f).resistance(0.0f).noCollision()
                    .breakInstantly().soundType("grass").nonOpaque().layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2).texture("red_mushroom_block/mushroom_red_5"));
    public static final Block RED_MUSHROOM_7 = registerPlantBlock("red_mushroom_7",
            builder -> builder.creativeTab("westeros_grasses_shrubs_tab").hardness(0.0f).resistance(0.0f).noCollision()
                    .breakInstantly().soundType("grass").nonOpaque().layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2).texture("red_mushroom_block/mushroom_red_6"));
    public static final Block RED_MUSHROOM_8 = registerPlantBlock("red_mushroom_8",
            builder -> builder.creativeTab("westeros_grasses_shrubs_tab").hardness(0.0f).resistance(0.0f).noCollision()
                    .breakInstantly().soundType("grass").nonOpaque().layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2).texture("red_mushroom_block/mushroom_red_7"));
    public static final Block RED_MUSHROOM_9 = registerPlantBlock("red_mushroom_9",
            builder -> builder.creativeTab("westeros_grasses_shrubs_tab").hardness(0.0f).resistance(0.0f).noCollision()
                    .breakInstantly().soundType("grass").nonOpaque().layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2).texture("red_mushroom_block/mushroom_red_8"));
    public static final Block RED_POPPIES = registerPlantBlock("red_poppies",
            builder -> builder.creativeTab("westeros_flowers_tab").hardness(0.0f).resistance(0.0f).noCollision()
                    .breakInstantly().soundType("grass").nonOpaque().layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2).texture("flowers/red_poppies1"));
    public static final Block RED_ROSES = registerPlantBlock("red_roses",
            builder -> builder.creativeTab("westeros_flowers_tab").hardness(0.0f).resistance(0.0f).noCollision()
                    .breakInstantly().soundType("grass").nonOpaque().layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2).texture("flowers/red_roses1"));
    public static final Block RED_SORREL = registerPlantBlock("red_sorrel",
            builder -> builder.creativeTab("westeros_flowers_tab").hardness(0.0f).resistance(0.0f).noCollision()
                    .breakInstantly().soundType("grass").nonOpaque().layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2).texture("flowers/red_sorrel1"));
    public static final Block RED_SOURLEAF_BUSH = registerPlantBlock("red_sourleaf_bush",
            builder -> builder.creativeTab("westeros_crops_herbs_tab").hardness(0.0f).resistance(0.0f).noCollision()
                    .breakInstantly().soundType("grass").nonOpaque().layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2).texture("flowers/red_sourleaf_bush1"));
    public static final Block RED_TULIPS = registerPlantBlock("red_tulips",
            builder -> builder.creativeTab("westeros_flowers_tab").hardness(0.0f).resistance(0.0f).noCollision()
                    .breakInstantly().soundType("grass").nonOpaque().layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2).texture("flowers/red_tulips1"));
    public static final Block STRAWBERRY_BUSH = registerPlantBlock("strawberry_bush",
            builder -> builder.creativeTab("westeros_crops_herbs_tab").hardness(0.0f).resistance(0.0f).noCollision()
                    .breakInstantly().soundType("grass").nonOpaque().layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2).texture("flowers/strawberry"));
    public static final Block THICK_GRASS = registerPlantBlock("thick_grass",
            builder -> builder.creativeTab("westeros_grasses_shrubs_tab").hardness(0.0f).resistance(0.0f).noCollision()
                    .breakInstantly().soundType("grass").nonOpaque().layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2).texture("minecraft:block/grass/grass1"));
    public static final Block UNSHADED_GRASS = registerPlantBlock("unshaded_grass",
            builder -> builder.creativeTab("westeros_grasses_shrubs_tab").hardness(0.0f).resistance(0.0f).noCollision()
                    .breakInstantly().soundType("grass").nonOpaque().layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2).texture("deadbush/side1"));
    public static final Block WHITE_CHAMOMILE = registerPlantBlock("white_chamomile",
            builder -> builder.creativeTab("westeros_flowers_tab").hardness(0.0f).resistance(0.0f).noCollision()
                    .breakInstantly().soundType("grass").nonOpaque().layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2).texture("flowers/white_chamomile1"));
    public static final Block WHITE_DAISIES = registerPlantBlock("white_daisies",
            builder -> builder.creativeTab("westeros_flowers_tab").hardness(0.0f).resistance(0.0f).noCollision()
                    .breakInstantly().soundType("grass").nonOpaque().layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2).texture("flowers/white_daisies1"));
    public static final Block WHITE_LILYOFTHEVALLEY = registerPlantBlock("white_lilyofthevalley",
            builder -> builder.creativeTab("westeros_flowers_tab").hardness(0.0f).resistance(0.0f).noCollision()
                    .breakInstantly().soundType("grass").nonOpaque().layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2).texture("flowers/white_lily_valley1"));
    public static final Block WHITE_PEONY = registerPlantBlock("white_peony",
            builder -> builder.creativeTab("westeros_flowers_tab").hardness(0.0f).resistance(0.0f).noCollision()
                    .breakInstantly().soundType("grass").nonOpaque().layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2).texture("flowers/white_peony1"));
    public static final Block WHITE_ROSES = registerPlantBlock("white_roses",
            builder -> builder.creativeTab("westeros_flowers_tab").hardness(0.0f).resistance(0.0f).noCollision()
                    .breakInstantly().soundType("grass").nonOpaque().layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2).texture("flowers/white_roses1"));
    public static final Block YELLOW_BEDSTRAW = registerPlantBlock("yellow_bedstraw",
            builder -> builder.creativeTab("westeros_flowers_tab").hardness(0.0f).resistance(0.0f).noCollision()
                    .breakInstantly().soundType("grass").nonOpaque().layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2).texture("flowers/yellow_bedstraw/side1"));
    public static final Block YELLOW_BELLS = registerPlantBlock("yellow_bells",
            builder -> builder.creativeTab("westeros_flowers_tab").hardness(0.0f).resistance(0.0f).noCollision()
                    .breakInstantly().soundType("grass").nonOpaque().layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2).texture("flowers/yellow_bells1"));
    public static final Block YELLOW_BUTTERCUPS = registerPlantBlock("yellow_buttercups",
            builder -> builder.creativeTab("westeros_flowers_tab").hardness(0.0f).resistance(0.0f).noCollision()
                    .breakInstantly().soundType("grass").nonOpaque().layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2).texture("flowers/yellow_buttercups1"));
    public static final Block YELLOW_DAFFODILS = registerPlantBlock("yellow_daffodils",
            builder -> builder.creativeTab("westeros_flowers_tab").hardness(0.0f).resistance(0.0f).noCollision()
                    .breakInstantly().soundType("grass").nonOpaque().layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2).texture("flowers/yellow_daffodils1"));
    public static final Block YELLOW_DAISIES = registerPlantBlock("yellow_daisies",
            builder -> builder.creativeTab("westeros_flowers_tab").hardness(0.0f).resistance(0.0f).noCollision()
                    .breakInstantly().soundType("grass").nonOpaque().layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2).texture("flowers/yellow_daisies1"));
    public static final Block YELLOW_DANDELIONS = registerPlantBlock("yellow_dandelions",
            builder -> builder.creativeTab("westeros_flowers_tab").hardness(0.0f).resistance(0.0f).noCollision()
                    .breakInstantly().soundType("grass").nonOpaque().layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2).texture("flowers/yellow_dandelions1"));
    public static final Block YELLOW_HELLEBORE = registerPlantBlock("yellow_hellebore",
            builder -> builder.creativeTab("westeros_flowers_tab").hardness(0.0f).resistance(0.0f).noCollision()
                    .breakInstantly().soundType("grass").nonOpaque().layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2).texture("flowers/yellow_hellebore1"));
    public static final Block YELLOW_LUPINE = registerPlantBlock("yellow_lupine",
            builder -> builder.creativeTab("westeros_flowers_tab").hardness(0.0f).resistance(0.0f).noCollision()
                    .breakInstantly().soundType("grass").nonOpaque().layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2).texture("flowers/yellow_lupine1"));
    public static final Block YELLOW_ROSES = registerPlantBlock("yellow_roses",
            builder -> builder.creativeTab("westeros_flowers_tab").hardness(0.0f).resistance(0.0f).noCollision()
                    .breakInstantly().soundType("grass").nonOpaque().layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2).texture("flowers/yellow_roses1"));
    public static final Block YELLOW_RUDBECKIA = registerPlantBlock("yellow_rudbeckia",
            builder -> builder.creativeTab("westeros_flowers_tab").hardness(0.0f).resistance(0.0f).noCollision()
                    .breakInstantly().soundType("grass").nonOpaque().layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2).texture("flowers/yellow_rudbeckia1"));
    public static final Block YELLOW_SUNFLOWER = registerPlantBlock("yellow_sunflower",
            builder -> builder.creativeTab("westeros_flowers_tab").hardness(0.0f).resistance(0.0f).noCollision()
                    .breakInstantly().soundType("grass").nonOpaque().layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2).texture("flowers/yellow_sunflower1"));
    public static final Block YELLOW_TANSY = registerPlantBlock("yellow_tansy",
            builder -> builder.creativeTab("westeros_flowers_tab").hardness(0.0f).resistance(0.0f).noCollision()
                    .breakInstantly().soundType("grass").nonOpaque().layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2).texture("flowers/yellow_tansy"));
    public static final Block YELLOW_WILDFLOWERS = registerPlantBlock("yellow_wildflowers",
            builder -> builder.creativeTab("westeros_flowers_tab").hardness(0.0f).resistance(0.0f).noCollision()
                    .breakInstantly().soundType("grass").nonOpaque().layerSensitive()
                    .boundingBox(4.8, 11.2, 0, 9.6, 4.8, 11.2).texture("flowers/yellow_sunflower/side1"));

    // CROP BLOCKS
    public static final Block CANDLE_ALTAR = registerCropBlock("candle_altar", builder -> builder
            .creativeTab("westeros_lighting_tab")
            .hardness(0.0f)
            .resistance(0.0f)
            .noCollision()
            .breakInstantly()
            .soundType("powder")
            .nonOpaque()
            .toggleOnUse()
            .boundingBox(0.125, 0.875, 0, 1, 0.125, 0.875)
            .lightLevel(10)
            .texture("lighting/candle_altar/lit1"));

    public static final Block CROP_CARROTS = registerCropBlock("crop_carrots", builder -> builder
            .creativeTab("westeros_crops_herbs_tab")
            .hardness(5.0f)
            .resistance(4.0f)
            .noCollision()
            .breakInstantly()
            .soundType("grass")
            .nonOpaque()
            .layerSensitive()
            .toggleOnUse()
            .harvestLevel(3)
            .requiresPickaxe()
            .texture("carrots/carrots_stage_0"));

    public static final Block CROP_PEAS = registerCropBlock("crop_peas", builder -> builder
            .creativeTab("westeros_crops_herbs_tab")
            .hardness(5.0f)
            .resistance(4.0f)
            .noCollision()
            .breakInstantly()
            .soundType("grass")
            .nonOpaque()
            .layerSensitive()
            .toggleOnUse()
            .harvestLevel(3)
            .requiresPickaxe()
            .texture("peas/peas_stage_0"));

    public static final Block CROP_TURNIPS = registerCropBlock("crop_turnips", builder -> builder
            .creativeTab("westeros_crops_herbs_tab")
            .hardness(5.0f)
            .resistance(4.0f)
            .noCollision()
            .breakInstantly()
            .soundType("grass")
            .nonOpaque()
            .layerSensitive()
            .toggleOnUse()
            .harvestLevel(3)
            .requiresPickaxe()
            .texture("turnips/turnips_stage_0"));

    public static final Block CROP_WHEAT = registerCropBlock("crop_wheat", builder -> builder
            .creativeTab("westeros_crops_herbs_tab")
            .hardness(5.0f)
            .resistance(4.0f)
            .noCollision()
            .breakInstantly()
            .soundType("grass")
            .nonOpaque()
            .layerSensitive()
            .toggleOnUse()
            .harvestLevel(3)
            .requiresPickaxe()
            .texture("wheat/wheat_stage_0"));

    public static final Block SEAGRASS = registerCropBlock("seagrass", builder -> builder
            .creativeTab("westeros_water_air_tab")
            .hardness(0.0f)
            .resistance(0.0f)
            .noCollision()
            .breakInstantly()
            .soundType("grass")
            .nonOpaque()
            .layerSensitive()
            .texture("seagrass/side1"));

    // FLOWER POT BLOCKS
    public static final Block POTTED_BLUE_BELLS = registerFlowerPotBlock(
            "potted_blue_bells",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .plantContent(BLUE_BELLS)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/blue_bells"));

    public static final Block POTTED_BLUE_CHICORY = registerFlowerPotBlock(
            "potted_blue_chicory",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .plantContent(BLUE_CHICORY)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/blue_chicory/side1"));

    public static final Block POTTED_BLUE_FLAX = registerFlowerPotBlock(
            "potted_blue_flax",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .plantContent(BLUE_FLAX)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/blue_flax1"));

    public static final Block POTTED_BLUE_FORGETMENOTS = registerFlowerPotBlock(
            "potted_blue_forgetmenots",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .plantContent(BLUE_FORGETMENOTS)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/blue_forgetmenots1"));

    public static final Block POTTED_BLUE_HYACINTH = registerFlowerPotBlock(
            "potted_blue_hyacinth",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .plantContent(BLUE_HYACINTH)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/blue_hyacinth1"));

    public static final Block POTTED_BLUE_ORCHID = registerFlowerPotBlock(
            "potted_blue_orchid",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .plantContent(BLUE_ORCHID)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/blue_orchid1"));

    public static final Block POTTED_BLUE_SWAMP_BELLS = registerFlowerPotBlock(
            "potted_blue_swamp_bells",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .plantContent(BLUE_SWAMP_BELLS)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/blue_swamp_bells1"));

    public static final Block POTTED_BRACKEN = registerFlowerPotBlock(
            "potted_bracken",
            builder -> builder
                    .creativeTab("westeros_grasses_shrubs_tab")
                    .plantContent(BRACKEN)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "bracken/side1"));

    // Additional potted flowerpot blocks
    public static final Block POTTED_BROWN_MUSHROOM_1 = registerFlowerPotBlock(
            "potted_brown_mushroom_1",
            builder -> builder
                    .creativeTab("westeros_grasses_shrubs_tab")
                    .plantContent(BROWN_MUSHROOM_1)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot",
                            "brown_mushroom_block/mushroom_brown_0"));

    public static final Block POTTED_BROWN_MUSHROOM_3 = registerFlowerPotBlock(
            "potted_brown_mushroom_3",
            builder -> builder
                    .creativeTab("westeros_grasses_shrubs_tab")
                    .plantContent(BROWN_MUSHROOM_3)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot",
                            "brown_mushroom_block/mushroom_brown_2"));

    public static final Block POTTED_BROWN_MUSHROOM_6 = registerFlowerPotBlock(
            "potted_brown_mushroom_6",
            builder -> builder
                    .creativeTab("westeros_grasses_shrubs_tab")
                    .plantContent(BROWN_MUSHROOM_6)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot",
                            "brown_mushroom_block/mushroom_brown_5"));

    public static final Block POTTED_BROWN_MUSHROOM_13 = registerFlowerPotBlock(
            "potted_brown_mushroom_13",
            builder -> builder
                    .creativeTab("westeros_grasses_shrubs_tab")
                    .plantContent(BROWN_MUSHROOM_13)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot",
                            "brown_mushroom_block/mushroom_brown_12"));

    public static final Block POTTED_CATTAILS = registerFlowerPotBlock(
            "potted_cattails",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .plantContent(null) // No corresponding plant block yet
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "cattails/side1"));

    public static final Block POTTED_COW_PARSELY = registerFlowerPotBlock(
            "potted_cow_parsely",
            builder -> builder
                    .creativeTab("westeros_grasses_shrubs_tab")
                    .plantContent(COW_PARSELY)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "cow_parsely/side1"));

    public static final Block POTTED_DEAD_BRACKEN = registerFlowerPotBlock(
            "potted_dead_bracken",
            builder -> builder
                    .creativeTab("westeros_grasses_shrubs_tab")
                    .plantContent(null) // No corresponding plant block yet
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "dead_bracken/side1"));

    public static final Block POTTED_DEAD_BUSH = registerFlowerPotBlock(
            "potted_dead_bush",
            builder -> builder
                    .creativeTab("westeros_grasses_shrubs_tab")
                    .plantContent(DEAD_BUSH)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "dorne_bush_thorny/side1"));

    public static final Block POTTED_DEAD_SCRUB_GRASS = registerFlowerPotBlock(
            "potted_dead_scrub_grass",
            builder -> builder
                    .creativeTab("westeros_grasses_shrubs_tab")
                    .plantContent(DEAD_SCRUB_GRASS)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/dead_scrub_grass1"));

    public static final Block POTTED_DOCK_LEAF = registerFlowerPotBlock(
            "potted_dock_leaf",
            builder -> builder
                    .creativeTab("westeros_crops_herbs_tab")
                    .plantContent(DOCK_LEAF)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "dock_leaf/side1"));

    public static final Block POTTED_FIREWEED = registerFlowerPotBlock(
            "potted_fireweed",
            builder -> builder
                    .creativeTab("westeros_grasses_shrubs_tab")
                    .plantContent(FIREWEED)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "fireweed/side1"));

    public static final Block POTTED_GRASS = registerFlowerPotBlock(
            "potted_grass",
            builder -> builder
                    .creativeTab("westeros_grasses_shrubs_tab")
                    .plantContent(GRASS)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "minecraft:block/fern/fern1"));

    public static final Block POTTED_GREEN_LEAFY_HERB = registerFlowerPotBlock(
            "potted_green_leafy_herb",
            builder -> builder
                    .creativeTab("westeros_grasses_shrubs_tab")
                    .plantContent(GREEN_LEAFY_HERB)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/green_leafy_herb"));

    public static final Block POTTED_GREEN_SCRUB_GRASS = registerFlowerPotBlock(
            "potted_green_scrub_grass",
            builder -> builder
                    .creativeTab("westeros_grasses_shrubs_tab")
                    .plantContent(GREEN_SCRUB_GRASS)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/green_scrub_grass1"));

    public static final Block POTTED_GREEN_SPINY_HERB = registerFlowerPotBlock(
            "potted_green_spiny_herb",
            builder -> builder
                    .creativeTab("westeros_grasses_shrubs_tab")
                    .plantContent(GREEN_SPINY_HERB)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/green_spiny_herb1"));

    public static final Block POTTED_HEATHER = registerFlowerPotBlock(
            "potted_heather",
            builder -> builder
                    .creativeTab("westeros_grasses_shrubs_tab")
                    .plantContent(HEATHER)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "heather/side1"));

    public static final Block POTTED_LADY_FERN = registerFlowerPotBlock(
            "potted_lady_fern",
            builder -> builder
                    .creativeTab("westeros_grasses_shrubs_tab")
                    .plantContent(LADY_FERN)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "lady_fern/side1"));

    public static final Block POTTED_MAGENTA_ROSES = registerFlowerPotBlock(
            "potted_magenta_roses",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .plantContent(MAGENTA_ROSES)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/magenta_roses1"));

    public static final Block POTTED_MEADOW_FESCUE = registerFlowerPotBlock(
            "potted_meadow_fescue",
            builder -> builder
                    .creativeTab("westeros_grasses_shrubs_tab")
                    .plantContent(MEADOW_FESCUE)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/meadow_fescue/side1"));

    public static final Block POTTED_NETTLE = registerFlowerPotBlock(
            "potted_nettle",
            builder -> builder
                    .creativeTab("westeros_grasses_shrubs_tab")
                    .plantContent(NETTLE)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "nettle/side1"));

    public static final Block POTTED_ORANGE_BELLS = registerFlowerPotBlock(
            "potted_orange_bells",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .plantContent(ORANGE_BELLS)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/orange_bells1"));

    public static final Block POTTED_ORANGE_BOG_ASPHODEL = registerFlowerPotBlock(
            "potted_orange_bog_asphodel",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .plantContent(ORANGE_BOG_ASPHODEL)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/orange_bog_asphodel1"));

    public static final Block POTTED_ORANGE_MARIGOLDS = registerFlowerPotBlock(
            "potted_orange_marigolds",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .plantContent(ORANGE_MARIGOLDS)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/orange_marigolds1"));

    public static final Block POTTED_ORANGE_SUN_STAR = registerFlowerPotBlock(
            "potted_orange_sun_star",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .plantContent(ORANGE_SUN_STAR)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/orange_sun_star1"));

    public static final Block POTTED_ORANGE_TROLLIUS = registerFlowerPotBlock(
            "potted_orange_trollius",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .plantContent(ORANGE_TROLLIUS)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/orange_trollius1"));

    public static final Block POTTED_PINK_ALLIUM = registerFlowerPotBlock(
            "potted_pink_allium",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .plantContent(PINK_ALLIUM)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/pink_allium1"));

    public static final Block POTTED_PINK_PRIMROSE = registerFlowerPotBlock(
            "potted_pink_primrose",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .plantContent(PINK_PRIMROSE)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/pink_primrose1"));

    public static final Block POTTED_PINK_ROSES = registerFlowerPotBlock(
            "potted_pink_roses",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .plantContent(PINK_ROSES)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/pink_roses1"));

    public static final Block POTTED_PINK_SWEET_PEAS = registerFlowerPotBlock(
            "potted_pink_sweet_peas",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .plantContent(PINK_SWEET_PEAS)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/pink_sweet_peas1"));

    public static final Block POTTED_PINK_THISTLE = registerFlowerPotBlock(
            "potted_pink_thistle",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .plantContent(PINK_THISTLE)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/pink_thistle/side1"));

    public static final Block POTTED_PINK_TULIPS = registerFlowerPotBlock(
            "potted_pink_tulips",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .plantContent(PINK_TULIPS)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/pink_tulips1"));

    public static final Block POTTED_PINK_WILDFLOWERS = registerFlowerPotBlock(
            "potted_pink_wildflowers",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .plantContent(PINK_WILDFLOWERS)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/pink_wildflowers"));

    public static final Block POTTED_PURPLE_ALPINE_SOWTHISTLE = registerFlowerPotBlock(
            "potted_purple_alpine_sowthistle",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .plantContent(PURPLE_ALPINE_SOWTHISTLE)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot",
                            "flowers/purple_alpine_sowthistle/side1"));

    public static final Block POTTED_PURPLE_FOXGLOVE = registerFlowerPotBlock(
            "potted_purple_foxglove",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .plantContent(PURPLE_FOXGLOVE)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/purple_foxglove1"));

    public static final Block POTTED_PURPLE_LAVENDER = registerFlowerPotBlock(
            "potted_purple_lavender",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .plantContent(PURPLE_LAVENDER)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/purple_lavender1"));

    public static final Block POTTED_PURPLE_PANSIES = registerFlowerPotBlock(
            "potted_purple_pansies",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .plantContent(PURPLE_PANSIES)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/purple_pansies1"));

    public static final Block POTTED_PURPLE_ROSES = registerFlowerPotBlock(
            "potted_purple_roses",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .plantContent(PURPLE_ROSES)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/purple_roses1"));

    public static final Block POTTED_PURPLE_VIOLETS = registerFlowerPotBlock(
            "potted_purple_violets",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .plantContent(PURPLE_VIOLETS)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/purple_violets1"));

    public static final Block POTTED_RED_ASTER = registerFlowerPotBlock(
            "potted_red_aster",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .plantContent(RED_ASTER)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/red_aster1"));

    public static final Block POTTED_RED_CARNATIONS = registerFlowerPotBlock(
            "potted_red_carnations",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .plantContent(RED_CARNATIONS)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/red_carnations1"));

    public static final Block POTTED_RED_CHRYSANTHEMUM = registerFlowerPotBlock(
            "potted_red_chrysanthemum",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .plantContent(RED_CHRYSANTHEMUM)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/red_chrysanthemum1"));

    public static final Block POTTED_RED_DARK_ROSES = registerFlowerPotBlock(
            "potted_red_dark_roses",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .plantContent(RED_DARK_ROSES)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/red_dark_roses1"));

    public static final Block POTTED_RED_FERN = registerFlowerPotBlock(
            "potted_red_fern",
            builder -> builder
                    .creativeTab("westeros_grasses_shrubs_tab")
                    .plantContent(RED_FERN)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "red_fern/side1"));

    public static final Block POTTED_RED_FLOWERING_SPINY_HERB = registerFlowerPotBlock(
            "potted_red_flowering_spiny_herb",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .plantContent(RED_FLOWERING_SPINY_HERB)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot",
                            "flowers/red_flowering_spiny_herb1"));

    public static final Block POTTED_RED_MUSHROOM_1 = registerFlowerPotBlock(
            "potted_red_mushroom_1",
            builder -> builder
                    .creativeTab("westeros_grasses_shrubs_tab")
                    .plantContent(RED_MUSHROOM_1)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot",
                            "red_mushroom_block/mushroom_red_0"));

    public static final Block POTTED_RED_MUSHROOM_2 = registerFlowerPotBlock(
            "potted_red_mushroom_2",
            builder -> builder
                    .creativeTab("westeros_grasses_shrubs_tab")
                    .plantContent(RED_MUSHROOM_2)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot",
                            "red_mushroom_block/mushroom_red_1"));

    public static final Block POTTED_RED_MUSHROOM_3 = registerFlowerPotBlock(
            "potted_red_mushroom_3",
            builder -> builder
                    .creativeTab("westeros_grasses_shrubs_tab")
                    .plantContent(RED_MUSHROOM_3)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot",
                            "red_mushroom_block/mushroom_red_2"));

    public static final Block POTTED_RED_MUSHROOM_7 = registerFlowerPotBlock(
            "potted_red_mushroom_7",
            builder -> builder
                    .creativeTab("westeros_grasses_shrubs_tab")
                    .plantContent(null) // No corresponding plant block yet
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot",
                            "red_mushroom_block/mushroom_red_6"));

    public static final Block POTTED_RED_MUSHROOM_8 = registerFlowerPotBlock(
            "potted_red_mushroom_8",
            builder -> builder
                    .creativeTab("westeros_grasses_shrubs_tab")
                    .plantContent(null) // No corresponding plant block yet
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot",
                            "red_mushroom_block/mushroom_red_7"));

    public static final Block POTTED_RED_MUSHROOM_9 = registerFlowerPotBlock(
            "potted_red_mushroom_9",
            builder -> builder
                    .creativeTab("westeros_grasses_shrubs_tab")
                    .plantContent(RED_MUSHROOM_9)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot",
                            "red_mushroom_block/mushroom_red_8"));

    public static final Block POTTED_RED_POPPIES = registerFlowerPotBlock(
            "potted_red_poppies",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .plantContent(RED_POPPIES)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/red_poppies1"));

    public static final Block POTTED_RED_ROSES = registerFlowerPotBlock(
            "potted_red_roses",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .plantContent(RED_ROSES)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/red_roses1"));

    public static final Block POTTED_RED_SORREL = registerFlowerPotBlock(
            "potted_red_sorrel",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .plantContent(RED_SORREL)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/red_sorrel1"));

    public static final Block POTTED_RED_SOURLEAF_BUSH = registerFlowerPotBlock(
            "potted_red_sourleaf_bush",
            builder -> builder
                    .creativeTab("westeros_crops_herbs_tab")
                    .plantContent(RED_SOURLEAF_BUSH)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/red_sourleaf_bush1"));

    public static final Block POTTED_RED_TULIPS = registerFlowerPotBlock(
            "potted_red_tulips",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .plantContent(RED_TULIPS)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/red_tulips1"));

    public static final Block POTTED_WHITE_CHAMOMILE = registerFlowerPotBlock(
            "potted_white_chamomile",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .plantContent(WHITE_CHAMOMILE)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/white_chamomile1"));

    public static final Block POTTED_WHITE_DAISIES = registerFlowerPotBlock(
            "potted_white_daisies",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .plantContent(WHITE_DAISIES)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/white_daisies1"));

    public static final Block POTTED_WHITE_LILYOFTHEVALLEY = registerFlowerPotBlock(
            "potted_white_lilyofthevalley",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .plantContent(WHITE_LILYOFTHEVALLEY)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/white_lily_valley1"));

    public static final Block POTTED_WHITE_PEONY = registerFlowerPotBlock(
            "potted_white_peony",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .plantContent(WHITE_PEONY)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/white_peony1"));

    public static final Block POTTED_WHITE_ROSES = registerFlowerPotBlock(
            "potted_white_roses",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .plantContent(WHITE_ROSES)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/white_roses1"));

    public static final Block POTTED_YELLOW_BEDSTRAW = registerFlowerPotBlock(
            "potted_yellow_bedstraw",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .plantContent(YELLOW_BEDSTRAW)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/yellow_bedstraw/side1"));

    public static final Block POTTED_YELLOW_BELLS = registerFlowerPotBlock(
            "potted_yellow_bells",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .plantContent(YELLOW_BELLS)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/yellow_bells1"));

    public static final Block POTTED_YELLOW_BUTTERCUPS = registerFlowerPotBlock(
            "potted_yellow_buttercups",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .plantContent(YELLOW_BUTTERCUPS)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/yellow_buttercups1"));

    public static final Block POTTED_YELLOW_DAFFODILS = registerFlowerPotBlock(
            "potted_yellow_daffodils",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .plantContent(YELLOW_DAFFODILS)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/yellow_daffodils1"));

    public static final Block POTTED_YELLOW_DAISIES = registerFlowerPotBlock(
            "potted_yellow_daisies",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .plantContent(YELLOW_DAISIES)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/yellow_daisies1"));

    public static final Block POTTED_YELLOW_DANDELIONS = registerFlowerPotBlock(
            "potted_yellow_dandelions",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .plantContent(YELLOW_DANDELIONS)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/yellow_dandelions1"));

    public static final Block POTTED_YELLOW_HELLEBORE = registerFlowerPotBlock(
            "potted_yellow_hellebore",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .plantContent(YELLOW_HELLEBORE)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/yellow_hellebore1"));

    public static final Block POTTED_YELLOW_LUPINE = registerFlowerPotBlock(
            "potted_yellow_lupine",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .plantContent(YELLOW_LUPINE)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/yellow_lupine1"));

    public static final Block POTTED_YELLOW_ROSES = registerFlowerPotBlock(
            "potted_yellow_roses",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .plantContent(YELLOW_ROSES)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/yellow_roses1"));

    public static final Block POTTED_YELLOW_RUDBECKIA = registerFlowerPotBlock(
            "potted_yellow_rudbeckia",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .plantContent(YELLOW_RUDBECKIA)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/yellow_rudbeckia1"));

    public static final Block POTTED_YELLOW_SUNFLOWER = registerFlowerPotBlock(
            "potted_yellow_sunflower",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .plantContent(YELLOW_SUNFLOWER)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/yellow_sunflower1"));

    public static final Block POTTED_YELLOW_TANSY = registerFlowerPotBlock(
            "potted_yellow_tansy",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .plantContent(YELLOW_TANSY)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/yellow_tansy1"));

    public static final Block POTTED_YELLOW_WILDFLOWERS = registerFlowerPotBlock(
            "potted_yellow_wildflowers",
            builder -> builder
                    .creativeTab("westeros_flowers_tab")
                    .plantContent(YELLOW_WILDFLOWERS)
                    .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/yellow_wildflowers1"));

    // WEB BLOCKS
    public static final Block ALYSSAS_TEARS_MIST_ONE = registerWebBlock(
            "alyssas_tears_mist_one",
            builder -> builder
                    .creativeTab("westeros_water_air_tab")
                    .hardness(0.8f)
                    .resistance(0.8f)
                    .noCollision()
                    .soundType("cloth")
                    .nonOpaque()
                    .alphaRender()
                    .texture("alyssas_tears_mist/mist1"));

    public static final Block ALYSSAS_TEARS_MIST_TWO = registerWebBlock(
            "alyssas_tears_mist_two",
            builder -> builder
                    .creativeTab("westeros_water_air_tab")
                    .hardness(0.8f)
                    .resistance(0.8f)
                    .noCollision()
                    .soundType("cloth")
                    .nonOpaque()
                    .alphaRender()
                    .texture("alyssas_tears_mist/mist2"));

    public static final Block ALYSSAS_TEARS_MIST_THREE = registerWebBlock(
            "alyssas_tears_mist_three",
            builder -> builder
                    .creativeTab("westeros_water_air_tab")
                    .hardness(0.8f)
                    .resistance(0.8f)
                    .noCollision()
                    .soundType("cloth")
                    .nonOpaque()
                    .alphaRender()
                    .texture("alyssas_tears_mist/mist3"));

    public static final Block ALYSSAS_TEARS_MIST_FOUR = registerWebBlock(
            "alyssas_tears_mist_four",
            builder -> builder
                    .creativeTab("westeros_water_air_tab")
                    .hardness(0.8f)
                    .resistance(0.8f)
                    .noCollision()
                    .soundType("cloth")
                    .nonOpaque()
                    .alphaRender()
                    .texture("alyssas_tears_mist/mist4"));

    public static final Block BLACK_BRICICLE = registerWebBlock(
            "black_bricicle",
            builder -> builder
                    .creativeTab("westeros_misc_tab")
                    .hardness(2.0f)
                    .resistance(6.0f)
                    .harvestLevel(1)
                    .requiresPickaxe()
                    .soundType("stone")
                    .texture("ashlar_melted/black/bricicle/side"));

    public static final Block BEES = registerWebBlock(
            "bees",
            builder -> builder
                    .creativeTab("westeros_misc_tab")
                    .hardness(0.8f)
                    .resistance(0.8f)
                    .noCollision()
                    .soundType("cloth")
                    .nonOpaque()
                    .alphaRender()
                    .texture("web_block/bug_bees"));

    // Additional web blocks from JSON definitions
    public static final Block BUSHEL_OF_HERBS = registerWebBlock(
            "bushel_of_herbs",
            builder -> builder
                    .creativeTab("westeros_food_blocks_tab")
                    .hardness(0.8f)
                    .resistance(0.8f)
                    .noCollision()
                    .soundType("cloth")
                    .nonOpaque()
                    .alphaRender()
                    .texture("web_block/food_herbs"));

    public static final Block BUSHEL_OF_SOURLEAF = registerWebBlock(
            "bushel_of_sourleaf",
            builder -> builder
                    .creativeTab("westeros_crops_herbs_tab")
                    .hardness(0.8f)
                    .resistance(0.8f)
                    .noCollision()
                    .soundType("cloth")
                    .nonOpaque()
                    .alphaRender()
                    .texture("web_block/food_sourleaf"));

    public static final Block BUTTERFLY_BLUE = registerWebBlock(
            "butterfly_blue",
            builder -> builder
                    .creativeTab("westeros_misc_tab")
                    .hardness(0.8f)
                    .resistance(0.8f)
                    .noCollision()
                    .soundType("cloth")
                    .nonOpaque()
                    .alphaRender()
                    .texture("web_block/bug_butterfly_blue"));

    public static final Block BUTTERFLY_ORANGE = registerWebBlock(
            "butterfly_orange",
            builder -> builder
                    .creativeTab("westeros_misc_tab")
                    .hardness(0.8f)
                    .resistance(0.8f)
                    .noCollision()
                    .soundType("cloth")
                    .nonOpaque()
                    .alphaRender()
                    .texture("web_block/bug_butterfly_orange"));

    public static final Block BUTTERFLY_RED = registerWebBlock(
            "butterfly_red",
            builder -> builder
                    .creativeTab("westeros_misc_tab")
                    .hardness(0.8f)
                    .resistance(0.8f)
                    .noCollision()
                    .soundType("cloth")
                    .nonOpaque()
                    .alphaRender()
                    .texture("web_block/bug_butterfly_red"));

    public static final Block BUTTERFLY_WHITE = registerWebBlock(
            "butterfly_white",
            builder -> builder
                    .creativeTab("westeros_misc_tab")
                    .hardness(0.8f)
                    .resistance(0.8f)
                    .noCollision()
                    .soundType("cloth")
                    .nonOpaque()
                    .alphaRender()
                    .texture("web_block/bug_butterfly_white"));

    public static final Block BUTTERFLY_YELLOW = registerWebBlock(
            "butterfly_yellow",
            builder -> builder
                    .creativeTab("westeros_misc_tab")
                    .hardness(0.8f)
                    .resistance(0.8f)
                    .noCollision()
                    .soundType("cloth")
                    .nonOpaque()
                    .alphaRender()
                    .texture("web_block/bug_butterfly_yellow"));

    public static final Block CATTAILS = registerWebBlock(
            "cattails",
            builder -> builder
                    .creativeTab("westeros_grasses_shrubs_tab")
                    .hardness(0.0f)
                    .resistance(0.0f)
                    .noCollision()
                    .soundType("grass")
                    .nonOpaque()
                    .alphaRender()
                    .layerSensitive()
                    .texture("cattails/side1"));

    public static final Block CHAIN_BLOCK_HARNESS = registerWebBlock(
            "chain_block_harness",
            builder -> builder
                    .creativeTab("westeros_metal_tab")
                    .hardness(0.8f)
                    .resistance(0.8f)
                    .noCollision()
                    .soundType("cloth")
                    .nonOpaque()
                    .alphaRender()
                    .texture("web_block/chain_blockharness"));

    public static final Block CHILI_RISTRA = registerWebBlock(
            "chili_ristra",
            builder -> builder
                    .creativeTab("westeros_food_blocks_tab")
                    .hardness(0.8f)
                    .resistance(0.8f)
                    .noCollision()
                    .soundType("cloth")
                    .nonOpaque()
                    .alphaRender()
                    .texture("web_block/food_chili_ristra"));

    public static final Block COBWEB = registerWebBlock(
            "cobweb",
            builder -> builder
                    .creativeTab("westeros_decor_tab")
                    .hardness(0.8f)
                    .resistance(0.8f)
                    .noCollision()
                    .soundType("cloth")
                    .nonOpaque()
                    .alphaRender()
                    .toggleOnUse()
                    .texture("cobweb/side1"));

    public static final Block DEAD_FISH = registerWebBlock(
            "dead_fish",
            builder -> builder
                    .creativeTab("westeros_food_blocks_tab")
                    .hardness(0.8f)
                    .resistance(0.8f)
                    .noCollision()
                    .soundType("cloth")
                    .nonOpaque()
                    .alphaRender()
                    .texture("dead_fish/fish_dead1"));

    public static final Block DEAD_FOWL = registerWebBlock(
            "dead_fowl",
            builder -> builder
                    .creativeTab("westeros_food_blocks_tab")
                    .hardness(0.8f)
                    .resistance(0.8f)
                    .noCollision()
                    .soundType("cloth")
                    .nonOpaque()
                    .alphaRender()
                    .texture("dead_fowl/fowl"));

    public static final Block DEAD_FROG = registerWebBlock(
            "dead_frog",
            builder -> builder
                    .creativeTab("westeros_food_blocks_tab")
                    .hardness(0.8f)
                    .resistance(0.8f)
                    .noCollision()
                    .soundType("cloth")
                    .nonOpaque()
                    .alphaRender()
                    .texture("dead_frog/toad_dead1"));

    public static final Block DEAD_HARE = registerWebBlock(
            "dead_hare",
            builder -> builder
                    .creativeTab("westeros_food_blocks_tab")
                    .hardness(0.8f)
                    .resistance(0.8f)
                    .noCollision()
                    .soundType("cloth")
                    .nonOpaque()
                    .alphaRender()
                    .texture("dead_hare/rabbit_dead1"));

    public static final Block DEAD_JUNGLE_TALL_GRASS = registerWebBlock(
            "dead_jungle_tall_grass",
            builder -> builder
                    .creativeTab("westeros_grasses_shrubs_tab")
                    .hardness(0.0f)
                    .resistance(0.0f)
                    .noCollision()
                    .soundType("grass")
                    .nonOpaque()
                    .alphaRender()
                    .layerSensitive()
                    .texture("dead_jungle_tall_grass/down_side1"));

    public static final Block DEAD_RAT = registerWebBlock(
            "dead_rat",
            builder -> builder
                    .creativeTab("westeros_food_blocks_tab")
                    .hardness(0.8f)
                    .resistance(0.8f)
                    .noCollision()
                    .soundType("cloth")
                    .nonOpaque()
                    .alphaRender()
                    .texture("dead_rat/rat1"));

    public static final Block DEAD_SAVANNA_TALL_GRASS = registerWebBlock(
            "dead_savanna_tall_grass",
            builder -> builder
                    .creativeTab("westeros_grasses_shrubs_tab")
                    .hardness(0.0f)
                    .resistance(0.0f)
                    .noCollision()
                    .soundType("grass")
                    .nonOpaque()
                    .alphaRender()
                    .layerSensitive()
                    .texture("dead_savanna_tall_grass/dead_savanna_tall_grass"));

    public static final Block DRAGONFLY = registerWebBlock(
            "dragonfly",
            builder -> builder
                    .creativeTab("westeros_misc_tab")
                    .hardness(0.8f)
                    .resistance(0.8f)
                    .noCollision()
                    .soundType("cloth")
                    .nonOpaque()
                    .alphaRender()
                    .texture("web_block/bug_dragonfly"));

    public static final Block FLIES = registerWebBlock(
            "flies",
            builder -> builder
                    .creativeTab("westeros_misc_tab")
                    .hardness(0.8f)
                    .resistance(0.8f)
                    .noCollision()
                    .soundType("cloth")
                    .nonOpaque()
                    .alphaRender()
                    .texture("web_block/bug_flies"));

    public static final Block GARLIC_STRAND = registerWebBlock(
            "garlic_strand",
            builder -> builder
                    .creativeTab("westeros_food_blocks_tab")
                    .hardness(0.8f)
                    .resistance(0.8f)
                    .noCollision()
                    .soundType("cloth")
                    .nonOpaque()
                    .alphaRender()
                    .texture("web_block/food_garlic_strand"));

    public static final Block ICICLE = registerWebBlock(
            "icicle",
            builder -> builder
                    .creativeTab("westeros_water_air_tab")
                    .hardness(0.8f)
                    .resistance(0.8f)
                    .noCollision()
                    .soundType("cloth")
                    .nonOpaque()
                    .alphaRender()
                    .texture("icicle/side"));

    public static final Block IRON_THRONE_RANDOM_BLADES = registerWebBlock(
            "iron_throne_random_blades",
            builder -> builder
                    .creativeTab("westeros_metal_tab")
                    .hardness(0.8f)
                    .resistance(0.8f)
                    .noCollision()
                    .soundType("cloth")
                    .nonOpaque()
                    .alphaRender()
                    .texture("blades_random/side1"));

    public static final Block JUNGLE_TALL_FERN = registerWebBlock(
            "jungle_tall_fern",
            builder -> builder
                    .creativeTab("westeros_grasses_shrubs_tab")
                    .hardness(0.0f)
                    .resistance(0.0f)
                    .noCollision()
                    .soundType("grass")
                    .nonOpaque()
                    .alphaRender()
                    .layerSensitive()
                    .texture("jungle_tall_fern/side"));

    public static final Block JUNGLE_TALL_GRASS = registerWebBlock(
            "jungle_tall_grass",
            builder -> builder
                    .creativeTab("westeros_grasses_shrubs_tab")
                    .hardness(0.0f)
                    .resistance(0.0f)
                    .noCollision()
                    .soundType("grass")
                    .nonOpaque()
                    .alphaRender()
                    .layerSensitive()
                    .texture("jungle_tall_grass/down_side1"));

    public static final Block ROPE_BLOCK_HARNESS = registerWebBlock(
            "rope_block_harness",
            builder -> builder
                    .creativeTab("westeros_cloth_fibers_tab")
                    .hardness(0.8f)
                    .resistance(0.8f)
                    .noCollision()
                    .soundType("cloth")
                    .nonOpaque()
                    .alphaRender()
                    .texture("web_block/rope_blockharness"));

    public static final Block SAUSAGES_LEG_OF_HAM = registerWebBlock(
            "sausages_leg_of_ham",
            builder -> builder
                    .creativeTab("westeros_food_blocks_tab")
                    .hardness(0.8f)
                    .resistance(0.8f)
                    .noCollision()
                    .soundType("cloth")
                    .nonOpaque()
                    .alphaRender()
                    .texture("sausages_leg_of_ham/default"));

    public static final Block SAVANNA_TALL_GRASS = registerWebBlock(
            "savanna_tall_grass",
            builder -> builder
                    .creativeTab("westeros_grasses_shrubs_tab")
                    .hardness(0.0f)
                    .resistance(0.0f)
                    .noCollision()
                    .soundType("grass")
                    .nonOpaque()
                    .alphaRender()
                    .layerSensitive()
                    .texture("savanna_tall_grass/savanna_tall_grass"));

    public static final Block SMOKE = registerWebBlock(
            "smoke",
            builder -> builder
                    .creativeTab("westeros_water_air_tab")
                    .hardness(0.8f)
                    .resistance(0.8f)
                    .noCollision()
                    .soundType("cloth")
                    .nonOpaque()
                    .alphaRender()
                    .toggleOnUse()
                    .texture("smoke/smoke1"));

    public static final Block VERTICAL_CHAIN = registerWebBlock(
            "vertical_chain",
            builder -> builder
                    .creativeTab("westeros_metal_tab")
                    .hardness(0.8f)
                    .resistance(0.8f)
                    .noCollision()
                    .soundType("cloth")
                    .nonOpaque()
                    .alphaRender()
                    .texture("web_block/chain_vertical"));

    public static final Block VERTICAL_ROPE = registerWebBlock(
            "vertical_rope",
            builder -> builder
                    .creativeTab("westeros_cloth_fibers_tab")
                    .hardness(0.8f)
                    .resistance(0.8f)
                    .noCollision()
                    .soundType("cloth")
                    .nonOpaque()
                    .alphaRender()
                    .texture("web_block/rope_vertical"));

    // Ladder blocks
    public static final Block IRON_RUNGS = registerLadderBlock(
            "iron_rungs",
            builder -> builder
                    .creativeTab("westeros_metal_tab")
                    .hardness(0.4f)
                    .resistance(3.0f)
                    .soundType("ladder")
                    .allowUnsupported()
                    .texture("iron_rungs/ladder"));

    public static final Block IRON_RUNGS_BROKEN = registerLadderBlock(
            "iron_rungs_broken",
            builder -> builder
                    .creativeTab("westeros_metal_tab")
                    .hardness(0.4f)
                    .resistance(3.0f)
                    .soundType("ladder")
                    .allowUnsupported()
                    .texture("iron_rungs/ladder2"));

    public static final Block ROPE_LADDER = registerLadderBlock(
            "rope_ladder",
            builder -> builder
                    .creativeTab("westeros_cloth_fibers_tab")
                    .hardness(0.4f)
                    .resistance(3.0f)
                    .soundType("ladder")
                    .allowUnsupported()
                    .texture("rope_ladder/side"));

    public static final Block VINE_JASMINE = registerLadderBlock(
            "vine_jasmine",
            builder -> builder
                    .creativeTab("westeros_foliage_tab")
                    .hardness(0.4f)
                    .resistance(3.0f)
                    .soundType("ladder")
                    .allowUnsupported()
                    .textures("jasmine_vines/side1", "jasmine_vines/side2", "jasmine_vines/side3",
                            "jasmine_vines/side4", "jasmine_vines/side5"));

    public static final Block WINTERFELL_STONE_LADDER = registerLadderBlock(
            "winterfell_stone_ladder",
            builder -> builder
                    .creativeTab("westeros_misc_tab")
                    .hardness(0.4f)
                    .resistance(3.0f)
                    .soundType("stone")
                    .allowUnsupported()
                    .textures("winterfell_stone_ladder/side1", "winterfell_stone_ladder/side2"));

    public static final Block WOOD_LADDER = registerLadderBlock(
            "wood_ladder",
            builder -> builder
                    .creativeTab("westeros_wood_planks_tab")
                    .hardness(0.4f)
                    .resistance(3.0f)
                    .soundType("ladder")
                    .allowUnsupported()
                    .texture("wood_ladder/side"));

    // Pane Blocks
    public static final Block VERTICAL_NET = registerPaneBlock(
            "vertical_net",
            builder -> builder
                    .creativeTab("westeros_cloth_fibers_tab")
                    .hardness(1.0f)
                    .resistance(3.0f)
                    .soundType("glass")
                    .unconnect(false)
                    .texture("vertical_net/vertical_net1"));

    public static final Block DORNE_CARVED_STONE_WINDOW = registerPaneBlock(
            "dorne_carved_stone_window",
            builder -> builder
                    .creativeTab("westeros_windows_glass_tab")
                    .hardness(1.0f)
                    .resistance(3.0f)
                    .soundType("stone")
                    .unconnect(false)
                    .texture("pane_block/moorish_stone_window_pane"));

    public static final Block DORNE_CARVED_WOODEN_WINDOW = registerPaneBlock(
            "dorne_carved_wooden_window",
            builder -> builder
                    .creativeTab("westeros_windows_glass_tab")
                    .hardness(1.0f)
                    .resistance(3.0f)
                    .soundType("stone")
                    .unconnect(false)
                    .texture("pane_block/moorish_wood_window_pane"));

    public static final Block IRON_BARS = registerPaneBlock(
            "iron_bars",
            builder -> builder
                    .creativeTab("westeros_metal_tab")
                    .hardness(5.0f)
                    .resistance(10.0f)
                    .soundType("metal")
                    .unconnect(false)
                    .texture("bars_iron_block/iron_bars"));

    public static final Block OXIDIZED_IRON_CROSSBAR = registerPaneBlock(
            "oxidized_iron_crossbar",
            builder -> builder
                    .creativeTab("westeros_metal_tab")
                    .hardness(5.0f)
                    .resistance(10.0f)
                    .soundType("metal")
                    .unconnect(false)
                    .texture("bars_iron_block/bars_iron_oxidized_crossbars"));

    public static final Block IRON_CROSSBAR = registerPaneBlock(
            "iron_crossbar",
            builder -> builder
                    .creativeTab("westeros_metal_tab")
                    .hardness(5.0f)
                    .resistance(10.0f)
                    .soundType("metal")
                    .unconnect(false)
                    .texture("bars_iron_block/bars_iron_crossbars"));

    public static final Block OXIDIZED_IRON_BARS = registerPaneBlock(
            "oxidized_iron_bars",
            builder -> builder
                    .creativeTab("westeros_metal_tab")
                    .hardness(5.0f)
                    .resistance(10.0f)
                    .soundType("metal")
                    .unconnect(false)
                    .texture("bars_iron_block/bars_iron_oxidized"));


    // Solid Block Examples
    public static final Block SIX_SIDED_BIRCH = registerSolidBlock(
            "6sided_birch",
            builder -> builder
                    .creativeTab("westeros_logs_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .soundType("wood")
                    .texture("bark/birch/side"));

    // Additional Solid Blocks from JSON definitions
    public static final Block APPLE_BASKET = registerSolidBlock(
            "apple_basket",
            builder -> builder
                    .creativeTab("westeros_food_blocks_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .soundType("cloth")
                    .textures("crate_block/basket_bottom", "crate_block/basket_apple", "crate_block/basket_side"));

    public static final Block APPLE_CRATE = registerSolidBlock(
            "apple_crate",
            builder -> builder
                    .creativeTab("westeros_food_blocks_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .soundType("wood")
                    .textures("crate_block/side_bot1", "crate_block/crate_top_apples", "crate_block/side_bot1"));

    public static final Block APPROVAL_UTILITY_BLOCK = registerSolidBlock(
            "approval_utility_block",
            builder -> builder
                    .creativeTab("westeros_utility_tab")
                    .hardness(5.0f)
                    .resistance(10.0f)
                    .soundType("metal")
                    .harvestLevel(3)
                    .texture("utility_block/approved"));

    public static final Block APRICOT_BASKET = registerSolidBlock(
            "apricot_basket",
            builder -> builder
                    .creativeTab("westeros_food_blocks_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .soundType("cloth")
                    .textures("crate_block/basket_bottom", "crate_block/basket_apricot", "crate_block/basket_side"));

    public static final Block SIX_SIDED_JUNGLE = registerSolidBlock(
            "6sided_jungle",
            builder -> builder
                    .creativeTab("westeros_logs_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .soundType("wood")
                    .texture("bark/jungle/side"));

    public static final Block SIX_SIDED_OAK = registerSolidBlock(
            "6sided_oak",
            builder -> builder
                    .creativeTab("westeros_logs_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .soundType("wood")
                    .texture("bark/oak/side"));

    public static final Block SIX_SIDED_SPRUCE = registerSolidBlock(
            "6sided_spruce",
            builder -> builder
                    .creativeTab("westeros_logs_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .soundType("wood")
                    .texture("bark/spruce/side"));

    public static final Block SIX_SIDED_STONE_SLAB = registerSolidBlock(
            "6sided_stone_slab",
            builder -> builder
                    .creativeTab("westeros_half_ashlar_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .soundType("stone")
                    .texture("ashlar_half/white/tile"));

    public static final Block ARBOR_BRICK_ORNATE = registerSolidBlock(
            "arbor_brick_ornate",
            builder -> builder
                    .creativeTab("westeros_panelling_carvings_tab")
                    .hardness(5.0f)
                    .resistance(10.0f)
                    .soundType("stone")
                    .texture("ashlar_engraved/arbor/all"));

    public static final Block BENCH_BUTCHER_KNIVES = registerSolidBlock(
            "bench_butcher_knives",
            builder -> builder
                    .creativeTab("westeros_furniture_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .soundType("wood")
                    .textures("bench_block/spruce_top", "bench_block/crafting_table_top", "bench_block/bench_butcher_knives"));

    public static final Block BENCH_CARPENTRY_HAMMER_SAW = registerSolidBlock(
            "bench_carpentry_hammer_saw",
            builder -> builder
                    .creativeTab("westeros_furniture_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .soundType("wood")
                    .textures("bench_block/spruce_top", "bench_block/crafting_table_top", "bench_block/bench_carpentry_hammer_saw"));

    public static final Block BENCH_DRAWERS = registerSolidBlock(
            "bench_drawers",
            builder -> builder
                    .creativeTab("westeros_furniture_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .soundType("wood")
                    .textures("bench_block/spruce_top", "bench_block/crafting_table_top", "bench_block/bench_drawers"));

    public static final Block BENCH_KITCHEN_KNIVES = registerSolidBlock(
            "bench_kitchen_knives",
            builder -> builder
                    .creativeTab("westeros_furniture_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .soundType("wood")
                    .textures("bench_block/spruce_top", "bench_block/crafting_table_top", "bench_block/bench_kitchen_knives"));

    public static final Block BENCH_KITCHEN_PANS = registerSolidBlock(
            "bench_kitchen_pans",
            builder -> builder
                    .creativeTab("westeros_furniture_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .soundType("wood")
                    .textures("bench_block/spruce_top", "bench_block/crafting_table_top", "bench_block/bench_kitchen_pans"));

    public static final Block BENCH_MASON_HAMMER_MALLET = registerSolidBlock(
            "bench_mason_hammer_mallet",
            builder -> builder
                    .creativeTab("westeros_furniture_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .soundType("wood")
                    .textures("bench_block/spruce_top", "bench_block/crafting_table_top", "bench_block/bench_mason_hammer_mallet"));

    public static final Block BERRY_BASKET = registerSolidBlock(
            "berry_basket",
            builder -> builder
                    .creativeTab("westeros_food_blocks_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .soundType("cloth")
                    .textures("crate_block/basket_bottom", "crate_block/basket_berry", "crate_block/basket_side"));

    public static final Block BERRY_CRATE = registerSolidBlock(
            "berry_crate",
            builder -> builder
                    .creativeTab("westeros_food_blocks_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .soundType("wood")
                    .textures("crate_block/side_bot1", "crate_block/crate_top_berry", "crate_block/side_bot1"));

    public static final Block BLACK_BRICK_ENGRAVED = registerSolidBlock(
            "black_brick_engraved",
            builder -> builder
                    .creativeTab("westeros_panelling_carvings_tab")
                    .hardness(5.0f)
                    .resistance(10.0f)
                    .soundType("stone")
                    .texture("ashlar_engraved/black/all"));

    public static final Block BLUEGREEN_CARVED_SANDSTONE = registerSolidBlock(
            "bluegreen_carved_sandstone",
            builder -> builder
                    .creativeTab("westeros_panelling_carvings_tab")
                    .hardness(5.0f)
                    .resistance(10.0f)
                    .soundType("stone")
                    .texture("bluegreen_carved_sandstone/side"));

    // Additional Solid Blocks from JSON definitions - Part 2
    public static final Block BONE_DIRT = registerSolidBlock(
            "bone_dirt",
            builder -> builder
                    .creativeTab("westeros_grass_dirt_tab")
                    .hardness(2.0f)
                    .resistance(2.0f)
                    .soundType("grass")
                    .texture("dirt/bone"));

    public static final Block BOOKSHELF_ABANDONED = registerSolidBlock(
            "bookshelf_abandoned",
            builder -> builder
                    .creativeTab("westeros_furniture_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .soundType("wood")
                    .textures("bench_block/spruce_top", "bench_block/spruce_top", "bookshelf_abandoned/side1"));

    public static final Block BOOKSHELF_LIBRARY = registerSolidBlock(
            "bookshelf_library",
            builder -> builder
                    .creativeTab("westeros_furniture_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .soundType("wood")
                    .textures("bench_block/spruce_top", "bench_block/spruce_top", "bookshelf_library/side1"));

    public static final Block BROKEN_CABINET = registerSolidBlock(
            "broken_cabinet",
            builder -> builder
                    .creativeTab("westeros_furniture_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .soundType("wood")
                    .textures("cabinet/top_bottom", "cabinet/top_bottom", "bench_block/cabinet_broken"));

    public static final Block BOOKSHELF_MAESTER = registerSolidBlock(
            "bookshelf_maester",
            builder -> builder
                    .creativeTab("westeros_furniture_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .soundType("wood")
                    .textures("bench_block/spruce_top", "bench_block/spruce_top", "bookshelf_maester/side1"));

    public static final Block BROWN_GREY_BRICK_ENGRAVED = registerSolidBlock(
            "brown_grey_brick_engraved",
            builder -> builder
                    .creativeTab("westeros_panelling_carvings_tab")
                    .hardness(5.0f)
                    .resistance(10.0f)
                    .soundType("stone")
                    .texture("ashlar_engraved/brown_grey/all"));

    public static final Block CABINET_DRAWER = registerSolidBlock(
            "cabinet_drawer",
            builder -> builder
                    .creativeTab("westeros_furniture_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .soundType("wood")
                    .textures("cabinet/top_bottom", "cabinet/top_bottom", "cabinet/drawer/side1"));

    public static final Block CAGE = registerSolidBlock(
            "cage",
            builder -> builder
                    .creativeTab("westeros_decor_tab")
                    .hardness(2.0f)
                    .resistance(10.0f)
                    .soundType("stone")
                    .nonOpaque()
                    .alphaRender()
                    .textures("cage/bottom", "cage/top", "cage/side"));

    public static final Block CARROT_BASKET = registerSolidBlock(
            "carrot_basket",
            builder -> builder
                    .creativeTab("westeros_food_blocks_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .soundType("cloth")
                    .textures("crate_block/basket_bottom", "crate_block/basket_carrot", "crate_block/basket_side"));

    public static final Block CARROT_CRATE = registerSolidBlock(
            "carrot_crate",
            builder -> builder
                    .creativeTab("westeros_food_blocks_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .soundType("wood")
                    .textures("crate_block/side_bot1", "crate_block/crate_top_carrot", "crate_block/side_bot1"));

    public static final Block CLOSED_BASKET = registerSolidBlock(
            "closed_basket",
            builder -> builder
                    .creativeTab("westeros_decor_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .soundType("cloth")
                    .textures("crate_block/basket_bottom", "crate_block/basket_top_closed", "crate_block/basket_side"));

    public static final Block CLOSED_CABINET = registerSolidBlock(
            "closed_cabinet",
            builder -> builder
                    .creativeTab("westeros_furniture_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .soundType("wood")
                    .textures("cabinet/top_bottom", "cabinet/top_bottom", "cabinet/closed/side"));

    public static final Block COARSE_DARK_RED_CARVED_SANDSTONE = registerSolidBlock(
            "coarse_dark_red_carved_sandstone",
            builder -> builder
                    .creativeTab("westeros_panelling_carvings_tab")
                    .hardness(5.0f)
                    .resistance(10.0f)
                    .soundType("stone")
                    .texture("ashlar_engraved/pale_dark_red/all"));

    public static final Block COARSE_RED_CARVED_SANDSTONE = registerSolidBlock(
            "coarse_red_carved_sandstone",
            builder -> builder
                    .creativeTab("westeros_panelling_carvings_tab")
                    .hardness(5.0f)
                    .resistance(10.0f)
                    .soundType("stone")
                    .texture("ashlar_engraved/pale_red/all"));

    public static final Block COBBLE_KEYSTONE = registerSolidBlock(
            "cobble_keystone",
            builder -> builder
                    .creativeTab("westeros_panelling_carvings_tab")
                    .hardness(5.0f)
                    .resistance(10.0f)
                    .soundType("stone")
                    .withConnectState()
                    .textures("cobblestone/grey/keystone/top_bottom", "cobblestone/grey/keystone/top_bottom", "cobblestone/grey/keystone/side"));

    public static final Block COLOURED_SEPT_WINDOW = registerSolidBlock(
            "coloured_sept_window",
            builder -> builder
                    .creativeTab("westeros_windows_glass_tab")
                    .hardness(2.0f)
                    .resistance(10.0f)
                    .soundType("stone")
                    .nonOpaque()
                    .alphaRender()
                    .texture("glass/sept/all"));

    public static final Block CRATE = registerSolidBlock(
            "crate",
            builder -> builder
                    .creativeTab("westeros_decor_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .soundType("wood")
                    .textures("crate_block/side_bot1", "crate_block/crate_side_crossbar_right", "crate_block/side_bot1"));

    public static final Block CRATE2 = registerSolidBlock(
            "crate2",
            builder -> builder
                    .creativeTab("westeros_decor_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .soundType("wood")
                    .textures("crate_block/side_bot1", "crate_block/crate_side_crossbar_left", "crate_block/side_bot1"));

    public static final Block CRATE3 = registerSolidBlock(
            "crate3",
            builder -> builder
                    .creativeTab("westeros_decor_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .soundType("wood")
                    .textures("crate_block/side_bot1", "crate_block/crate_side_crossbar_crossed", "crate_block/side_bot1"));

    public static final Block DARK_GREY_BRICK_ENGRAVED = registerSolidBlock(
            "dark_grey_brick_engraved",
            builder -> builder
                    .creativeTab("westeros_panelling_carvings_tab")
                    .hardness(5.0f)
                    .resistance(10.0f)
                    .soundType("stone")
                    .texture("ashlar_engraved/dark_grey/all"));

    public static final Block DATE_BASKET = registerSolidBlock(
            "date_basket",
            builder -> builder
                    .creativeTab("westeros_food_blocks_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .soundType("cloth")
                    .textures("crate_block/basket_bottom", "crate_block/basket_dates", "crate_block/basket_side"));

    public static final Block DATES = registerSolidBlock(
            "dates",
            builder -> builder
                    .creativeTab("westeros_food_blocks_tab")
                    .hardness(2.0f)
                    .resistance(2.0f)
                    .soundType("grass")
                    .textures("dates/bottom", "dates/top", "dates/side"));

    // Additional Solid Blocks from JSON definitions - Part 3
    public static final Block DESERT_SANDSTONE_ENGRAVED = registerSolidBlock(
            "desert_sandstone_engraved",
            builder -> builder
                    .creativeTab("westeros_panelling_carvings_tab")
                    .hardness(3.0f)
                    .resistance(10.0f)
                    .soundType("stone")
                    .texture("ashlar_engraved/sandstone/all"));

    public static final Block DOMESTIC_UTILITY_BLOCK = registerSolidBlock(
            "domestic_utility_block",
            builder -> builder
                    .creativeTab("westeros_utility_tab")
                    .hardness(5.0f)
                    .resistance(10.0f)
                    .soundType("metal")
                    .requiresPickaxe(3)
                    .texture("utility_block/domestic"));

    public static final Block DONE_UTILITY_BLOCK = registerSolidBlock(
            "done_utility_block",
            builder -> builder
                    .creativeTab("westeros_utility_tab")
                    .hardness(5.0f)
                    .resistance(10.0f)
                    .soundType("metal")
                    .requiresPickaxe(3)
                    .texture("utility_block/done"));

    public static final Block DRAGON_CARVING = registerSolidBlock(
            "dragon_carving",
            builder -> builder
                    .creativeTab("westeros_panelling_carvings_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .soundType("stone")
                    .texture("dragon_carving/side1"));

    public static final Block EMPTY_BARREL = registerSolidBlock(
            "empty_barrel",
            builder -> builder
                    .creativeTab("westeros_decor_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .soundType("wood")
                    .textures("barrel_closed/barrel_top_closed", "crate_block/barrel_top_empty", "barrel_sides/side0"));

    public static final Block EMPTY_CABINET = registerSolidBlock(
            "empty_cabinet",
            builder -> builder
                    .creativeTab("westeros_furniture_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .soundType("wood")
                    .textures("cabinet/top_bottom", "cabinet/top_bottom", "cabinet/empty/side"));

    public static final Block FAITH_CARVED_ARBOR_BRICK = registerSolidBlock(
            "faith_carved_arbor_brick",
            builder -> builder
                    .creativeTab("westeros_panelling_carvings_tab")
                    .hardness(5.0f)
                    .resistance(10.0f)
                    .soundType("stone")
                    .texture("ashlar_third/arbor/faith_carved"));

    public static final Block FAITH_CARVED_BLACK_BRICK = registerSolidBlock(
            "faith_carved_black_brick",
            builder -> builder
                    .creativeTab("westeros_panelling_carvings_tab")
                    .hardness(5.0f)
                    .resistance(5.0f)
                    .soundType("stone")
                    .texture("ashlar_quarter/black/faith_carved"));

    public static final Block FAITH_CARVED_BROWN_GREY_BRICK = registerSolidBlock(
            "faith_carved_brown_grey_brick",
            builder -> builder
                    .creativeTab("westeros_panelling_carvings_tab")
                    .hardness(5.0f)
                    .resistance(5.0f)
                    .soundType("stone")
                    .texture("ashlar_quarter/brown_grey/faith_carved"));

    public static final Block FAITH_CARVED_COARSE_RED_BRICK = registerSolidBlock(
            "faith_carved_coarse_red_brick",
            builder -> builder
                    .creativeTab("westeros_panelling_carvings_tab")
                    .hardness(5.0f)
                    .resistance(5.0f)
                    .soundType("stone")
                    .texture("ashlar_third/pale_red/faith_carved"));

    public static final Block FAITH_CARVED_DARK_GREY_BRICK = registerSolidBlock(
            "faith_carved_dark_grey_brick",
            builder -> builder
                    .creativeTab("westeros_panelling_carvings_tab")
                    .hardness(5.0f)
                    .resistance(5.0f)
                    .soundType("stone")
                    .texture("ashlar_quarter/dark_grey/faith_carved"));

    public static final Block FAITH_CARVED_DUN_BRICK = registerSolidBlock(
            "faith_carved_dun_brick",
            builder -> builder
                    .creativeTab("westeros_panelling_carvings_tab")
                    .hardness(5.0f)
                    .resistance(10.0f)
                    .soundType("stone")
                    .texture("ashlar_third/dun/faith_carved"));

    public static final Block FAITH_CARVED_GREY_BRICK = registerSolidBlock(
            "faith_carved_grey_brick",
            builder -> builder
                    .creativeTab("westeros_panelling_carvings_tab")
                    .hardness(5.0f)
                    .resistance(5.0f)
                    .soundType("stone")
                    .texture("ashlar_third/grey/faith_carved"));

    public static final Block FAITH_CARVED_OLDTOWN_BRICK = registerSolidBlock(
            "faith_carved_oldtown_brick",
            builder -> builder
                    .creativeTab("westeros_panelling_carvings_tab")
                    .hardness(5.0f)
                    .resistance(5.0f)
                    .soundType("stone")
                    .texture("ashlar_quarter_rounded/light_oldtown/faith_carved"));

    public static final Block FAITH_CARVED_PINK_SANDSTONE = registerSolidBlock(
            "faith_carved_pink_sandstone",
            builder -> builder
                    .creativeTab("westeros_panelling_carvings_tab")
                    .hardness(5.0f)
                    .resistance(10.0f)
                    .soundType("stone")
                    .texture("ashlar_third/sandy_pink/faith_carved"));

    public static final Block FAITH_CARVED_REACH_BRICK = registerSolidBlock(
            "faith_carved_reach_brick",
            builder -> builder
                    .creativeTab("westeros_panelling_carvings_tab")
                    .hardness(5.0f)
                    .resistance(5.0f)
                    .soundType("stone")
                    .texture("ashlar_quarter_rounded/reach/faith_carved"));

    public static final Block FAITH_CARVED_SMALL_STONE_BRICK = registerSolidBlock(
            "faith_carved_small_stone_brick",
            builder -> builder
                    .creativeTab("westeros_panelling_carvings_tab")
                    .hardness(5.0f)
                    .resistance(5.0f)
                    .soundType("stone")
                    .texture("ashlar_quarter/green_grey/faith_carved"));

    public static final Block FAITH_CARVED_STONE_BRICK = registerSolidBlock(
            "faith_carved_stone_brick",
            builder -> builder
                    .creativeTab("westeros_panelling_carvings_tab")
                    .hardness(5.0f)
                    .resistance(5.0f)
                    .soundType("stone")
                    .texture("ashlar_half/white/faith_carved"));

    public static final Block FAITH_CARVED_STORMLANDS_BRICK = registerSolidBlock(
            "faith_carved_stormlands_brick",
            builder -> builder
                    .creativeTab("westeros_panelling_carvings_tab")
                    .hardness(5.0f)
                    .resistance(10.0f)
                    .soundType("stone")
                    .texture("ashlar_quarter/stormlands/faith_carved"));

    public static final Block FAITH_CARVED_WESTERLANDS_BRICK = registerSolidBlock(
            "faith_carved_westerlands_brick",
            builder -> builder
                    .creativeTab("westeros_panelling_carvings_tab")
                    .hardness(5.0f)
                    .resistance(5.0f)
                    .soundType("stone")
                    .texture("ashlar_quarter/westerlands/faith_carved"));

    public static final Block FISH_BARREL = registerSolidBlock(
            "fish_barrel",
            builder -> builder
                    .creativeTab("westeros_food_blocks_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .soundType("wood")
                    .textures("barrel_closed/barrel_top_closed", "crate_block/barrel_top_fish", "barrel_sides/side0"));

    public static final Block FISH_BASKET = registerSolidBlock(
            "fish_basket",
            builder -> builder
                    .creativeTab("westeros_food_blocks_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .soundType("cloth")
                    .textures("crate_block/basket_bottom", "crate_block/basket_fish", "crate_block/basket_side"));

    public static final Block FISH_TRAP = registerSolidBlock(
            "fish_trap",
            builder -> builder
                    .creativeTab("westeros_decor_tab")
                    .hardness(2.0f)
                    .resistance(10.0f)
                    .soundType("wood")
                    .nonOpaque()
                    .texture("fish_trap/side1"));

    public static final Block FLAGSTONE = registerSolidBlock(
            "flagstone",
            builder -> builder
                    .creativeTab("westeros_cobblestone_tab")
                    .hardness(2.0f)
                    .resistance(2.0f)
                    .soundType("stone")
                    .texture("stone_block/flagstone"));

    public static final Block FULL_CABINET = registerSolidBlock(
            "full_cabinet",
            builder -> builder
                    .creativeTab("westeros_furniture_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .soundType("wood")
                    .textures("cabinet/top_bottom", "cabinet/top_bottom", "cabinet/full/side"));

    public static final Block GLOWING_EMBERS = registerSolidBlock(
            "glowing_embers",
            builder -> builder
                    .creativeTab("westeros_lighting_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .soundType("wood")
                    .lightLevel(2)
                    .texture("lighting/coals_glowing"));

    public static final Block GRAIN_BASKET = registerSolidBlock(
            "grain_basket",
            builder -> builder
                    .creativeTab("westeros_food_blocks_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .soundType("cloth")
                    .textures("crate_block/basket_bottom", "crate_block/basket_grain", "crate_block/basket_side"));

    public static final Block GRAIN_CRATE = registerSolidBlock(
            "grain_crate",
            builder -> builder
                    .creativeTab("westeros_food_blocks_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .soundType("wood")
                    .textures("crate_block/side_bot1", "crate_block/crate_top_grain", "crate_block/side_bot1"));

    public static final Block GREEN_GREY_BRICK_ENGRAVED = registerSolidBlock(
            "green_grey_brick_engraved",
            builder -> builder
                    .creativeTab("westeros_panelling_carvings_tab")
                    .hardness(5.0f)
                    .resistance(10.0f)
                    .soundType("stone")
                    .texture("ashlar_engraved/green_grey/all"));

    public static final Block GREY_BRICK_ENGRAVED = registerSolidBlock(
            "grey_brick_engraved",
            builder -> builder
                    .creativeTab("westeros_panelling_carvings_tab")
                    .hardness(5.0f)
                    .resistance(10.0f)
                    .soundType("stone")
                    .texture("ashlar_engraved/grey/all"));

    public static final Block GREY_KEYSTONE = registerSolidBlock(
            "grey_keystone",
            builder -> builder
                    .creativeTab("westeros_panelling_carvings_tab")
                    .hardness(3.0f)
                    .resistance(10.0f)
                    .soundType("stone")
                    .texture("stone_block/keystone_grey"));

    public static final Block HIGH_CLASS_UTILITY_BLOCK = registerSolidBlock(
            "high_class_utility_block",
            builder -> builder
                    .creativeTab("westeros_utility_tab")
                    .hardness(5.0f)
                    .resistance(10.0f)
                    .soundType("metal")
                    .requiresPickaxe(3)
                    .texture("utility_block/highclass"));

    public static final Block HOP_BASKET = registerSolidBlock(
            "hop_basket",
            builder -> builder
                    .creativeTab("westeros_food_blocks_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .soundType("cloth")
                    .textures("crate_block/basket_bottom", "crate_block/basket_hop", "crate_block/basket_side"));

    public static final Block HOP_CRATE = registerSolidBlock(
            "hop_crate",
            builder -> builder
                    .creativeTab("westeros_food_blocks_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .soundType("wood")
                    .textures("crate_block/crate_side_crossbar_right", "crate_block/crate_hops", "crate_block/crate_side_crossbar_left"));

    public static final Block HOUSE_COUNT_UTILITY_BLOCK = registerSolidBlock(
            "house_count_utility_block",
            builder -> builder
                    .creativeTab("westeros_utility_tab")
                    .hardness(5.0f)
                    .resistance(10.0f)
                    .soundType("metal")
                    .requiresPickaxe(3)
                    .texture("utility_block/housecount"));

    public static final Block INDUSTRY_UTILITY_BLOCK = registerSolidBlock(
            "industry_utility_block",
            builder -> builder
                    .creativeTab("westeros_utility_tab")
                    .hardness(5.0f)
                    .resistance(10.0f)
                    .soundType("metal")
                    .requiresPickaxe(3)
                    .texture("utility_block/industry"));

    public static final Block IRON_CRATE = registerSolidBlock(
            "iron_crate",
            builder -> builder
                    .creativeTab("westeros_decor_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .soundType("wood")
                    .textures("crate_block/side_bot1", "crate_block/crate_top_iron", "crate_block/side_bot1"));

    public static final Block KL_DUN_CARVED_BRICK = registerSolidBlock(
            "kl_dun_carved_brick",
            builder -> builder
                    .creativeTab("westeros_panelling_carvings_tab")
                    .hardness(5.0f)
                    .resistance(10.0f)
                    .soundType("stone")
                    .texture("ashlar_engraved/dun/all"));

    public static final Block LANNISPORT_KEYSTONE_ORANGE_PLASTER = registerSolidBlock(
            "lannisport_keystone_orange_plaster",
            builder -> builder
                    .creativeTab("westeros_marble_plaster_tab")
                    .hardness(5.0f)
                    .resistance(10.0f)
                    .soundType("stone")
                    .textures("fieldstone/westerlands/all", "fieldstone/westerlands/all", "plaster/smooth/lannisport_orange/all"));

    public static final Block LANNISPORT_KEYSTONE_YELLOW_PLASTER = registerSolidBlock(
            "lannisport_keystone_yellow_plaster",
            builder -> builder
                    .creativeTab("westeros_marble_plaster_tab")
                    .hardness(5.0f)
                    .resistance(10.0f)
                    .soundType("stone")
                    .textures("fieldstone/westerlands/all", "fieldstone/westerlands/all", "plaster/smooth/lannisport_yellow/all"));

    public static final Block LARGE_CLAY_POT_SOLID = registerSolidBlock(
            "large_clay_pot_solid",
            builder -> builder
                    .creativeTab("westeros_decor_tab")
                    .hardness(5.0f)
                    .resistance(4.0f)
                    .soundType("wood")
                    .requiresPickaxe(3)
                    .textures("wood/oak/all", "clay_pot/solid", "clay_pot/solid"));

    public static final Block LAVENDER_BASKET = registerSolidBlock(
            "lavender_basket",
            builder -> builder
                    .creativeTab("westeros_food_blocks_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .soundType("cloth")
                    .textures("crate_block/basket_bottom", "crate_block/basket_lavender", "crate_block/basket_side"));

    public static final Block LAVENDER_CRATE = registerSolidBlock(
            "lavender_crate",
            builder -> builder
                    .creativeTab("westeros_food_blocks_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .soundType("wood")
                    .textures("crate_block/crate_side_crossbar_right", "crate_block/crate_lavender", "crate_block/crate_side_crossbar_crossed"));

    public static final Block LEMON_BASKET = registerSolidBlock(
            "lemon_basket",
            builder -> builder
                    .creativeTab("westeros_food_blocks_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .soundType("cloth")
                    .textures("crate_block/basket_bottom", "crate_block/basket_lemons", "crate_block/basket_side"));

    public static final Block LIGHT_GREY_BRICK_ENGRAVED = registerSolidBlock(
            "light_grey_brick_engraved",
            builder -> builder
                    .creativeTab("westeros_panelling_carvings_tab")
                    .hardness(5.0f)
                    .resistance(10.0f)
                    .soundType("stone")
                    .texture("ashlar_engraved/light_grey/all"));

    public static final Block LIGHT_GREY_STONE_WHITE_PLASTER = registerSolidBlock(
            "light_grey_stone_white_plaster",
            builder -> builder
                    .creativeTab("westeros_marble_plaster_tab")
                    .hardness(5.0f)
                    .resistance(5.0f)
                    .soundType("stone")
                    .textures("fieldstone/grey/all", "fieldstone/grey/all", "plaster/smooth/white/all"));

    public static final Block LIGHT_OLDTOWN_BRICK_ENGRAVED = registerSolidBlock(
            "light_oldtown_brick_engraved",
            builder -> builder
                    .creativeTab("westeros_panelling_carvings_tab")
                    .hardness(5.0f)
                    .resistance(10.0f)
                    .soundType("stone")
                    .texture("ashlar_engraved/light_oldtown/all"));

    public static final Block LIME_BASKET = registerSolidBlock(
            "lime_basket",
            builder -> builder
                    .creativeTab("westeros_food_blocks_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .soundType("cloth")
                    .textures("crate_block/basket_bottom", "crate_block/basket_limes", "crate_block/basket_side"));

    public static final Block LOW_CLASS_UTILITY_BLOCK = registerSolidBlock(
            "low_class_utility_block",
            builder -> builder
                    .creativeTab("westeros_utility_tab")
                    .hardness(5.0f)
                    .resistance(10.0f)
                    .soundType("metal")
                    .requiresPickaxe(3)
                    .texture("utility_block/lowclass"));

    public static final Block MIDDLE_CLASS_UTILITY_BLOCK = registerSolidBlock(
            "middle_class_utility_block",
            builder -> builder
                    .creativeTab("westeros_utility_tab")
                    .hardness(5.0f)
                    .resistance(10.0f)
                    .soundType("metal")
                    .requiresPickaxe(3)
                    .texture("utility_block/middleclass"));

    public static final Block MIRROR_BLOCK = registerSolidBlock(
            "mirror_block",
            builder -> builder
                    .creativeTab("westeros_furniture_tab")
                    .hardness(3.0f)
                    .resistance(10.0f)
                    .soundType("wood")
                    .textures("mirror/top_bottom", "mirror/top_bottom", "mirror/side"));

    public static final Block MONOCHROME_DARK_SANDSTONE_ENGRAVED = registerSolidBlock(
            "monochrome_dark_sandstone_engraved",
            builder -> builder
                    .creativeTab("westeros_panelling_carvings_tab")
                    .hardness(5.0f)
                    .resistance(10.0f)
                    .soundType("stone")
                    .texture("ashlar_engraved/light_brown/all"));

    public static final Block MONOCHROME_SANDSTONE_ENGRAVED = registerSolidBlock(
            "monochrome_sandstone_engraved",
            builder -> builder
                    .creativeTab("westeros_panelling_carvings_tab")
                    .hardness(5.0f)
                    .resistance(10.0f)
                    .soundType("stone")
                    .texture("ashlar_engraved/westerlands/all"));

    public static final Block NETHER_BRICK_KEYSTONE = registerSolidBlock(
            "nether_brick_keystone",
            builder -> builder
                    .creativeTab("westeros_panelling_carvings_tab")
                    .hardness(3.0f)
                    .resistance(10.0f)
                    .soundType("stone")
                    .texture("ashlar_half/black/embellished"));

    public static final Block NORTHERN_CARVINGS = registerSolidBlock(
            "northern_carvings",
            builder -> builder
                    .creativeTab("westeros_panelling_carvings_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .soundType("wood")
                    .textures("wood/spruce/carving/top_bottom", "wood/spruce/carving/top_bottom", "wood/spruce/carving/side"));

    public static final Block NOTE_UTILITY_BLOCK = registerSolidBlock(
            "note_utility_block",
            builder -> builder
                    .creativeTab("westeros_utility_tab")
                    .hardness(5.0f)
                    .resistance(10.0f)
                    .soundType("metal")
                    .requiresPickaxe(3)
                    .texture("utility_block/note"));

    public static final Block OLIVE_BASKET = registerSolidBlock(
            "olive_basket",
            builder -> builder
                    .creativeTab("westeros_food_blocks_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .soundType("cloth")
                    .textures("crate_block/basket_bottom", "crate_block/basket_olives", "crate_block/basket_side"));

    public static final Block OPEN_BASKET = registerSolidBlock(
            "open_basket",
            builder -> builder
                    .creativeTab("westeros_decor_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .soundType("cloth")
                    .textures("crate_block/basket_bottom", "crate_block/basket_top", "crate_block/basket_side"));

    public static final Block OPEN_CRATE = registerSolidBlock(
            "open_crate",
            builder -> builder
                    .creativeTab("westeros_decor_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .soundType("wood")
                    .textures("crate_block/side_bot1", "crate_block/crate_top_empty", "crate_block/side_bot1"));

    public static final Block ORANGE_BASKET = registerSolidBlock(
            "orange_basket",
            builder -> builder
                    .creativeTab("westeros_food_blocks_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .soundType("cloth")
                    .textures("crate_block/basket_bottom", "crate_block/basket_oranges", "crate_block/basket_side"));

    public static final Block ORANGE_BRICK_ARCH_SINGLE = registerSolidBlock(
            "orange_brick_arch_single",
            builder -> builder
                    .creativeTab("westeros_brick_tab")
                    .hardness(3.0f)
                    .resistance(10.0f)
                    .soundType("stone")
                    .withConnectState()
                    .textures("brick/orange/all1", "brick/orange/all1", "brick/orange/arch_single"));

    public static final Block ORANGE_BRICK_ARCH_DOUBLE = registerSolidBlock(
            "orange_brick_arch_double",
            builder -> builder
                    .creativeTab("westeros_brick_tab")
                    .hardness(3.0f)
                    .resistance(10.0f)
                    .soundType("stone")
                    .withConnectState()
                    .textures("brick/orange/all1", "brick/orange/all1", "brick/orange/arch_double"));

    public static final Block ORANGE_BRICK_DENTIL = registerSolidBlock(
            "orange_brick_dentil",
            builder -> builder
                    .creativeTab("westeros_brick_tab")
                    .hardness(3.0f)
                    .resistance(10.0f)
                    .soundType("stone")
                    .textures("brick/orange/all1", "brick/orange/all1", "brick/orange/dentil"));

    public static final Block ORNATE_MARBLE = registerSolidBlock(
            "ornate_marble",
            builder -> builder
                    .creativeTab("westeros_panelling_carvings_tab")
                    .hardness(5.0f)
                    .resistance(10.0f)
                    .soundType("stone")
                    .texture("marble/quartz/ornate/side"));

    public static final Block ORANGE_BRICK_ROWLOCK = registerSolidBlock(
            "orange_brick_rowlock",
            builder -> builder
                    .creativeTab("westeros_brick_tab")
                    .hardness(3.0f)
                    .resistance(10.0f)
                    .soundType("stone")
                    .textures("brick/orange/all1", "brick/orange/all1", "brick/orange/rowlock1"));

    public static final Block ORNATE_SANDSTONE = registerSolidBlock(
            "ornate_sandstone",
            builder -> builder
                    .creativeTab("westeros_panelling_carvings_tab")
                    .hardness(5.0f)
                    .resistance(10.0f)
                    .soundType("stone")
                    .texture("ashlar_third/sandstone/ornate/side"));

    public static final Block PARQUET_FLOOR = registerSolidBlock(
            "parquet_floor",
            builder -> builder
                    .creativeTab("westeros_wood_planks_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .soundType("wood")
                    .texture("wood/oak/ornate"));

    public static final Block PILED_BONES = registerSolidBlock(
            "piled_bones",
            builder -> builder
                    .creativeTab("westeros_misc_tab")
                    .hardness(5.0f)
                    .resistance(10.0f)
                    .soundType("stone")
                    .texture("piled_bones/side"));

    public static final Block PINK_SANDSTONE_ENGRAVED = registerSolidBlock(
            "pink_sandstone_engraved",
            builder -> builder
                    .creativeTab("westeros_panelling_carvings_tab")
                    .hardness(5.0f)
                    .resistance(10.0f)
                    .soundType("stone")
                    .texture("ashlar_engraved/sandy_pink/all"));

    public static final Block PISTON_TOP = registerSolidBlock(
            "piston_top",
            builder -> builder
                    .creativeTab("westeros_tool_blocks_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .soundType("wood")
                    .texture("wood/oak/studded"));

    public static final Block POMEGRANATE_BASKET = registerSolidBlock(
            "pomegranate_basket",
            builder -> builder
                    .creativeTab("westeros_food_blocks_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .soundType("cloth")
                    .textures("crate_block/basket_bottom", "crate_block/basket_pomegranates", "crate_block/basket_side"));

    public static final Block PURPLE_GRAPE_BASKET = registerSolidBlock(
            "purple_grape_basket",
            builder -> builder
                    .creativeTab("westeros_food_blocks_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .soundType("cloth")
                    .textures("crate_block/basket_bottom", "crate_block/basket_grape_purple", "crate_block/basket_side"));

    public static final Block PURPLE_GRAPE_CRATE = registerSolidBlock(
            "purple_grape_crate",
            builder -> builder
                    .creativeTab("westeros_food_blocks_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .soundType("wood")
                    .textures("crate_block/crate_side_crossbar_right", "crate_block/crate_top_grape_purple", "crate_block/crate_side_crossbar_crossed"));

    public static final Block REACH_BRICK_ENGRAVED = registerSolidBlock(
            "reach_brick_engraved",
            builder -> builder
                    .creativeTab("westeros_panelling_carvings_tab")
                    .hardness(5.0f)
                    .resistance(10.0f)
                    .soundType("stone")
                    .texture("ashlar_engraved/reach/all"));

    public static final Block REACH_OAK_WOOD_PANELLING = registerSolidBlock(
            "reach_oak_wood_panelling",
            builder -> builder
                    .creativeTab("westeros_panelling_carvings_tab")
                    .hardness(5.0f)
                    .resistance(10.0f)
                    .soundType("wood")
                    .textures("wood/oak/panelling/top_bottom", "wood/oak/panelling/top_bottom", "wood/oak/panelling/side1"));

    public static final Block RED_LANTERN2 = registerSolidBlock(
            "red_lantern2",
            builder -> builder
                    .creativeTab("westeros_lighting_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .soundType("wood")
                    .lightLevel(7)
                    .textures("lighting/lantern_red_bottom", "lighting/lantern_red_top", "lighting/lantern_red_side"));

    public static final Block REACH_SPRUCE_WOOD_PANELLING = registerSolidBlock(
            "reach_spruce_wood_panelling",
            builder -> builder
                    .creativeTab("westeros_panelling_carvings_tab")
                    .hardness(5.0f)
                    .resistance(10.0f)
                    .soundType("wood")
                    .textures("wood/spruce/panelling/top_bottom", "wood/spruce/panelling/top_bottom", "wood/spruce/panelling/side1"));

    public static final Block REDORANGE_CARVED_SANDSTONE = registerSolidBlock(
            "redorange_carved_sandstone",
            builder -> builder
                    .creativeTab("westeros_panelling_carvings_tab")
                    .hardness(5.0f)
                    .resistance(10.0f)
                    .soundType("stone")
                    .texture("redorange_carved_sandstone/side"));

    public static final Block SALT_CRATE = registerSolidBlock(
            "salt_crate",
            builder -> builder
                    .creativeTab("westeros_food_blocks_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .soundType("wood")
                    .textures("crate_block/side_bot1", "crate_block/crate_top_salt", "crate_block/side_bot1"));

    public static final Block SANDY_STONE_SLABS = registerSolidBlock(
            "sandy_stone_slabs",
            builder -> builder
                    .creativeTab("westeros_cobblestone_tab")
                    .hardness(3.0f)
                    .resistance(10.0f)
                    .soundType("stone")
                    .texture("sandy_stone/sandy_stone_0"));

    public static final Block SEPT_CRYSTAL_LARGE = registerSolidBlock(
            "sept_crystal_large",
            builder -> builder
                    .creativeTab("westeros_windows_glass_tab")
                    .hardness(2.0f)
                    .resistance(10.0f)
                    .soundType("stone")
                    .nonOpaque()
                    .alphaRender()
                    .texture("crystal/all"));

    public static final Block SHOP_UTILITY_BLOCK = registerSolidBlock(
            "shop_utility_block",
            builder -> builder
                    .creativeTab("westeros_utility_tab")
                    .hardness(5.0f)
                    .resistance(10.0f)
                    .soundType("metal")
                    .requiresPickaxe(3)
                    .texture("utility_block/shop"));

    public static final Block SILVER_TIN_CRATE = registerSolidBlock(
            "silver_tin_crate",
            builder -> builder
                    .creativeTab("westeros_decor_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .soundType("wood")
                    .textures("crate_block/crate_side_crossbar_right", "crate_block/crate_top_tin", "crate_block/crate_side_crossbar_crossed"));

    public static final Block SMALL_ORANGE_BRICKS_ORNATE_TOP = registerSolidBlock(
            "small_orange_bricks_ornate_top",
            builder -> builder
                    .creativeTab("westeros_panelling_carvings_tab")
                    .hardness(3.0f)
                    .resistance(10.0f)
                    .soundType("stone")
                    .textures("brick/orange/ornate/top_bottom", "brick/orange/ornate/top_bottom", "brick/orange/all1"));

    public static final Block SMALL_ORANGE_BRICKS_ORNATE = registerSolidBlock(
            "small_orange_bricks_ornate",
            builder -> builder
                    .creativeTab("westeros_panelling_carvings_tab")
                    .hardness(3.0f)
                    .resistance(10.0f)
                    .soundType("stone")
                    .texture("brick/orange/ornate/top_bottom"));

    public static final Block SMALL_SMOOTH_STONE_BRICK_BLUE_PLASTER = registerSolidBlock(
            "small_smooth_stone_brick_blue_plaster",
            builder -> builder
                    .creativeTab("westeros_marble_plaster_tab")
                    .hardness(5.0f)
                    .resistance(5.0f)
                    .soundType("stone")
                    .textures("ashlar_quarter_rounded/grey/all", "ashlar_quarter_rounded/grey/all", "plaster/smooth/blue/all"));

    public static final Block SMALL_SMOOTH_STONE_BRICK_WHITE_PLASTER = registerSolidBlock(
            "small_smooth_stone_brick_white_plaster",
            builder -> builder
                    .creativeTab("westeros_marble_plaster_tab")
                    .hardness(5.0f)
                    .resistance(5.0f)
                    .soundType("stone")
                    .textures("ashlar_quarter_rounded/grey/all", "ashlar_quarter_rounded/grey/all", "plaster/smooth/brown_white/all"));

    public static final Block SMALL_STONE_BRICK_WHITE_PLASTER = registerSolidBlock(
            "small_stone_brick_white_plaster",
            builder -> builder
                    .creativeTab("westeros_marble_plaster_tab")
                    .hardness(5.0f)
                    .resistance(5.0f)
                    .soundType("stone")
                    .textures("ashlar_quarter/green_grey/all", "ashlar_quarter/green_grey/all", "plaster/smooth/white/all"));

    public static final Block SMALL_WHITE_BRICK_BROWNISH_WHITE_PLASTER = registerSolidBlock(
            "small_white_brick_brownish_white_plaster",
            builder -> builder
                    .creativeTab("westeros_marble_plaster_tab")
                    .hardness(5.0f)
                    .resistance(5.0f)
                    .soundType("stone")
                    .textures("ashlar_quarter_rounded/white/all", "ashlar_quarter_rounded/white/all", "plaster/smooth/brown_white/all"));

    public static final Block SMALL_WHITE_BRICK_WHITE_PLASTER = registerSolidBlock(
            "small_white_brick_white_plaster",
            builder -> builder
                    .creativeTab("westeros_marble_plaster_tab")
                    .hardness(5.0f)
                    .resistance(5.0f)
                    .soundType("stone")
                    .textures("ashlar_quarter_rounded/white/all", "ashlar_quarter_rounded/white/all", "plaster/smooth/white/all"));

    public static final Block SOURLEAF_BASKET = registerSolidBlock(
            "sourleaf_basket",
            builder -> builder
                    .creativeTab("westeros_food_blocks_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .soundType("cloth")
                    .textures("crate_block/basket_bottom", "crate_block/basket_sourleaf", "crate_block/basket_side"));

    public static final Block SOURLEAF_CRATE = registerSolidBlock(
            "sourleaf_crate",
            builder -> builder
                    .creativeTab("westeros_food_blocks_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .soundType("wood")
                    .textures("crate_block/crate_side_crossbar_right", "crate_block/crate_sourleaf", "crate_block/crate_side_crossbar_crossed"));

    public static final Block SOUTHERN_BRICK_ARCH_FLAT = registerSolidBlock(
            "southern_brick_arch_flat",
            builder -> builder
                    .creativeTab("westeros_brick_tab")
                    .hardness(3.0f)
                    .resistance(10.0f)
                    .soundType("stone")
                    .withConnectState()
                    .textures("brick/southern/all1", "brick/southern/all1", "brick/southern/arch_flat"));

    public static final Block SOUTHERN_BRICK_ARCH = registerSolidBlock(
            "southern_brick_arch",
            builder -> builder
                    .creativeTab("westeros_brick_tab")
                    .hardness(3.0f)
                    .resistance(10.0f)
                    .soundType("stone")
                    .withConnectState()
                    .textures("brick/southern/all1", "brick/southern/all1", "brick/southern/arch"));

    public static final Block SOUTHERN_BRICK_LINTEL = registerSolidBlock(
            "southern_brick_lintel",
            builder -> builder
                    .creativeTab("westeros_brick_tab")
                    .hardness(3.0f)
                    .resistance(10.0f)
                    .soundType("stone")
                    .textures("brick/southern/all1", "brick/southern/all1", "brick/southern/lintel"));

    public static final Block SPECIAL_UTILITY_BLOCK = registerSolidBlock(
            "special_utility_block",
            builder -> builder
                    .creativeTab("westeros_utility_tab")
                    .hardness(5.0f)
                    .resistance(10.0f)
                    .soundType("metal")
                    .requiresPickaxe(3)
                    .texture("utility_block/special"));

    public static final Block SPIT_ROAST = registerSolidBlock(
            "spit_roast",
            builder -> builder
                    .creativeTab("westeros_food_blocks_tab")
                    .hardness(2.0f)
                    .resistance(2.0f)
                    .soundType("grass")
                    .texture("meat_spitroast/all"));

    public static final Block SQUASH = registerSolidBlock(
            "squash",
            builder -> builder
                    .creativeTab("westeros_food_blocks_tab")
                    .hardness(2.0f)
                    .resistance(2.0f)
                    .soundType("grass")
                    .textures("squash/top", "squash/top", "squash/side"));

    public static final Block STACKED_BONES_SOLID = registerSolidBlock(
            "stacked_bones_solid",
            builder -> builder
                    .creativeTab("westeros_misc_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .soundType("stone")
                    .requiresAxe(1)
                    .textures("stacked_bones/bone_stacked_front", "stacked_bones/bone_stacked_front", "stacked_bones/bone_stacked_rotated"));

    public static final Block STORMLANDS_BRICK_ENGRAVED = registerSolidBlock(
            "stormlands_brick_engraved",
            builder -> builder
                    .creativeTab("westeros_panelling_carvings_tab")
                    .hardness(5.0f)
                    .resistance(10.0f)
                    .soundType("stone")
                    .texture("ashlar_engraved/stormlands/all"));

    public static final Block TABLE_BOOKS = registerSolidBlock(
            "table_books",
            builder -> builder
                    .creativeTab("westeros_furniture_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .soundType("wood")
                    .textures("bench_block/spruce_top", "bench_block/table_top", "bench_block/table_drawer_books_side"));

    public static final Block TABLE_DRAWERS = registerSolidBlock(
            "table_drawers",
            builder -> builder
                    .creativeTab("westeros_furniture_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .soundType("wood")
                    .textures("bench_block/spruce_top", "bench_block/table_top", "bench_block/table_drawers_side"));

    public static final Block TABLE_WIDGETS = registerSolidBlock(
            "table_widgets",
            builder -> builder
                    .creativeTab("westeros_furniture_tab")
                    .hardness(2.0f)
                    .resistance(5.0f)
                    .soundType("wood")
                    .textures("bench_block/spruce_top", "bench_block/table_top", "bench_block/table_drawer_widgets_side"));

    public static final Block TERRACOTTA_ENGRAVED = registerSolidBlock(
            "terracotta_engraved",
            builder -> builder
                    .creativeTab("westeros_panelling_carvings_tab")
                    .hardness(3.0f)
                    .resistance(10.0f)
                    .soundType("stone")
                    .texture("ashlar_engraved/terracotta/all"));

    public static final Block THICK_GRASS_BLOCK = registerSolidBlock(
            "thick_grass_block",
            builder -> builder
                    .creativeTab("westeros_grass_dirt_tab")
                    .hardness(5.0f)
                    .resistance(5.0f)
                    .soundType("grass")
                    .requiresShovel(3)
                    .texture("grass_block/forest_top1"));

    // Food blocks
    public static final Block TURNIP_BASKET = registerSolidBlock(
        "turnip_basket",
        builder -> builder
            .creativeTab("westeros_food_blocks_tab")
            .hardness(2.0f)
            .resistance(5.0f)
            .soundType("cloth")
            .textures("crate_block/basket_bottom", "crate_block/basket_turnip", "crate_block/basket_side")
    );

    public static final Block TURNIP_CRATE = registerSolidBlock(
        "turnip_crate",
        builder -> builder
            .creativeTab("westeros_food_blocks_tab")
            .hardness(2.0f)
            .resistance(5.0f)
            .soundType("wood")
            .textures("crate_block/side_bot1", "crate_block/crate_top_turnip", "crate_block/side_bot1")
    );

    // Plaster blocks
    public static final Block UNUSED_BROWN_PLASTER = registerSolidBlock(
        "unused_brown_plaster",
        builder -> builder
            .creativeTab("westeros_marble_plaster_tab")
            .hardness(5.0f)
            .resistance(10.0f)
            .soundType("stone")
            .textures("ashlar_quarter_rounded/light_brown/all", "ashlar_quarter_rounded/light_brown/all", "plaster/smooth/brown_white/all")
    );

    public static final Block UNUSED_PURPLE_PLASTER = registerSolidBlock(
        "unused_purple_plaster",
        builder -> builder
            .creativeTab("westeros_marble_plaster_tab")
            .hardness(5.0f)
            .resistance(10.0f)
            .soundType("stone")
            .textures("ashlar_quarter_rounded/dark_red/all", "ashlar_quarter_rounded/dark_red/all", "plaster/smooth/brown_white/all")
    );

    // Engraved blocks
    public static final Block VIVID_DARK_SANDSTONE_ENGRAVED = registerSolidBlock(
        "vivid_dark_sandstone_engraved",
        builder -> builder
            .creativeTab("westeros_panelling_carvings_tab")
            .hardness(5.0f)
            .resistance(10.0f)
            .soundType("stone")
            .texture("ashlar_engraved/dark_tan/all")
    );

    public static final Block VIVID_SANDSTONE_ENGRAVED = registerSolidBlock(
        "vivid_sandstone_engraved",
        builder -> builder
            .creativeTab("westeros_panelling_carvings_tab")
            .hardness(5.0f)
            .resistance(10.0f)
            .soundType("stone")
            .texture("ashlar_engraved/tan/all")
    );

    // Barrel block
    public static final Block WATER_BARREL = registerSolidBlock(
        "water_barrel",
        builder -> builder
            .creativeTab("westeros_decor_tab")
            .hardness(2.0f)
            .resistance(5.0f)
            .soundType("wood")
            .textures("barrel_closed/barrel_top_closed", "crate_block/barrel_top_water", "barrel_sides/side0")
    );

    // Engraved white brick
    public static final Block WHITE_BRICK_ENGRAVED = registerSolidBlock(
        "white_brick_engraved",
        builder -> builder
            .creativeTab("westeros_panelling_carvings_tab")
            .hardness(5.0f)
            .resistance(10.0f)
            .soundType("stone")
            .texture("ashlar_engraved/white/all")
    );

    // Grape baskets and crates
    public static final Block WHITE_GRAPE_BASKET = registerSolidBlock(
        "white_grape_basket",
        builder -> builder
            .creativeTab("westeros_food_blocks_tab")
            .hardness(2.0f)
            .resistance(5.0f)
            .soundType("cloth")
            .textures("crate_block/basket_bottom", "crate_block/basket_grape_white", "crate_block/basket_side")
    );

    public static final Block WHITE_GRAPE_CRATE = registerSolidBlock(
        "white_grape_crate",
        builder -> builder
            .creativeTab("westeros_food_blocks_tab")
            .hardness(2.0f)
            .resistance(5.0f)
            .soundType("wood")
            .textures("crate_block/crate_side_crossbar_right", "crate_block/crate_top_grape_white", "crate_block/crate_side_crossbar_crossed")
    );

    // Carving block
    public static final Block WINTERFELL_CARVING = registerSolidBlock(
        "winterfell_carving",
        builder -> builder
            .creativeTab("westeros_panelling_carvings_tab")
            .hardness(5.0f)
            .resistance(10.0f)
            .soundType("stone")
            .texture("ashlar_third/dark_grey/carving/side")
    );

    // Utility blocks
    public static final Block WIP_UTILITY_BLOCK = registerSolidBlock(
        "wip_utility_block",
        builder -> builder
            .creativeTab("westeros_utility_tab")
            .hardness(5.0f)
            .resistance(10.0f)
            .soundType("metal")
            .requiresPickaxe(3)
            .lightLevel(0)
            .texture("utility_block/wip")
    );

    public static final Block WORKSHOP_UTILITY_BLOCK = registerSolidBlock(
        "workshop_utility_block",
        builder -> builder
            .creativeTab("westeros_utility_tab")
            .hardness(5.0f)
            .resistance(10.0f)
            .soundType("metal")
            .requiresPickaxe(3)
            .lightLevel(0)
            .texture("utility_block/workshop")
    );

    public static final Block YARD_UTILITY_BLOCK = registerSolidBlock(
        "yard_utility_block",
        builder -> builder
            .creativeTab("westeros_utility_tab")
            .hardness(5.0f)
            .resistance(10.0f)
            .soundType("metal")
            .requiresPickaxe(3)
            .lightLevel(0)
            .texture("utility_block/yard")
    );

    // Timber block with multi-texture states and overlay textures
    public static final Block TIMBER_NORTHERN_BLUE_BRESSUMMER = registerSolidBlock(
        "timber_northern_blue_bressummer",
        builder -> builder
            .creativeTab("westeros_timber_frame_tab")
            .hardness(2.0f)
            .resistance(5.0f)
            .soundType("wood")
            .withConnectState()
            .toggleOnUse()
            .withMultiTextureStateProperty(
                List.of("state_0", "state_1", "state_2", "state_3", "state_4"),
                Map.of(
                    "state_0", new String[]{"plaster/smooth/gulltown_blue/all", "wood/northern/all", "plaster/smooth/gulltown_blue/all"},
                    "state_1", new String[]{"plaster/smooth/light_blue/all", "wood/northern/all", "plaster/smooth/light_blue/all"},
                    "state_2", new String[]{"plaster/smooth/blue/all", "wood/northern/all", "plaster/smooth/blue/all"},
                    "state_3", new String[]{"plaster/rough/gulltown_blue/all1", "wood/northern/all", "plaster/rough/gulltown_blue/all1"},
                    "state_4", new String[]{"plaster/wattle/gulltown_blue/all", "wood/northern/all", "plaster/wattle/gulltown_blue/all"}
                ),
                "state_0"
            )
            .withOverlayTextures(
                Map.of(
                    "state_0", new String[]{"timber/northern/bottom", "transparent", "timber/northern/bressummer"},
                    "state_1", new String[]{"timber/northern/bottom", "transparent", "timber/northern/bressummer"},
                    "state_2", new String[]{"timber/northern/bottom", "transparent", "timber/northern/bressummer"},
                    "state_3", new String[]{"timber/northern/bottom", "transparent", "timber/northern/bressummer"},
                    "state_4", new String[]{"timber/northern/bottom", "transparent", "timber/northern/bressummer"}
                )
            )
    );

    // Clay block
    public static final Block YELLOW_STAINED_CLAY = registerSolidBlock(
        "yellow_stained_clay",
        builder -> builder
            .creativeTab("westeros_sand_gravel_tab")
            .hardness(5.0f)
            .resistance(5.0f)
            .soundType("grass")
            .texture("clay/yellow_stained_clay")
    );

    @FunctionalInterface
    public interface BlockBuilderConfigurator {
        BlockBuilder configure(BlockBuilder builder);
    }

    public static Block registerArrowSlitBlock(String name, BlockBuilderConfigurator configurator) {
        BlockBuilder builder = new BlockBuilder(name)
                .creativeTab("westeros_decor_tab")
                .hardness(2.0f)
                .resistance(6.0f)
                .harvestLevel(1)
                .texture("side.block"); // Default texture

        builder.setBlockType(BlockBuilder.BlockType.ARROW_SLIT);
        return configurator.configure(builder).register();
    }

    public static Block registerTableBlock(String name, BlockBuilderConfigurator configurator) {
        BlockBuilder builder = new BlockBuilder(name)
                .creativeTab("westeros_furniture_tab")
                .hardness(2.0f)
                .resistance(6.0f)
                .harvestLevel(1)
                .woodType(WoodType.OAK); // Default wood type

        builder.setBlockType(BlockBuilder.BlockType.TABLE);
        return configurator.configure(builder).register();
    }

    public static Block registerChairBlock(String name, BlockBuilderConfigurator configurator) {
        BlockBuilder builder = new BlockBuilder(name)
                .creativeTab("westeros_furniture_tab")
                .hardness(2.0f)
                .resistance(6.0f)
                .harvestLevel(1)
                .woodType(WoodType.OAK)
                .nonOpaque();

        builder.setBlockType(BlockBuilder.BlockType.CHAIR);
        return configurator.configure(builder).register();
    }

    public static Block registerWaySignBlock(String name, BlockBuilderConfigurator configurator) {
        BlockBuilder builder = new BlockBuilder(name)
                .creativeTab("westeros_decor_tab")
                .hardness(2.0f)
                .resistance(6.0f)
                .harvestLevel(1)
                .woodType(WoodType.OAK); // Default wood type

        builder.setBlockType(BlockBuilder.BlockType.WAY_SIGN);
        return configurator.configure(builder).register();
    }

    public static Block registerTrapDoorBlock(String name, BlockBuilderConfigurator configurator) {
        BlockBuilder builder = new BlockBuilder(name)
                .creativeTab("westeros_decor_tab")
                .hardness(2.0f)
                .resistance(6.0f)
                .harvestLevel(1)
                .woodType(WoodType.OAK)
                .soundType("iron")
                .locked(false);

        builder.setBlockType(BlockBuilder.BlockType.TRAPDOOR);
        return configurator.configure(builder).register();
    }

    public static Block registerLogBlock(String name, BlockBuilderConfigurator configurator) {
        BlockBuilder builder = new BlockBuilder(name)
                .creativeTab("westeros_logs_tab")
                .hardness(2.0f)
                .resistance(6.0f)
                .harvestLevel(1)
                .requiresAxe()
                .woodType(WoodType.OAK)
                .soundType("wood");

        builder.setBlockType(BlockBuilder.BlockType.LOG);
        return configurator.configure(builder).register();
    }

    public static Block registerTorchBlock(String name, BlockBuilderConfigurator configurator) {
        BlockBuilder builder = new BlockBuilder(name)
                .creativeTab("westeros_lighting_tab")
                .hardness(0.0f)
                .resistance(0.0f)
                .noCollision()
                .dropsNothing()
                .alphaRender()
                .texture("lighting/torch"); // Default texture

        builder.setBlockType(BlockBuilder.BlockType.TORCH);
        return configurator.configure(builder).register();
    }

    public static Block registerDoorBlock(String name, BlockBuilderConfigurator configurator) {
        BlockBuilder builder = new BlockBuilder(name)
                .creativeTab("westeros_wood_planks_tab")
                .hardness(2.0f)
                .resistance(5.0f)
                .harvestLevel(1)
                .requiresAxe()
                .woodType(WoodType.OAK)
                .soundType("wood")
                .locked(false);

        builder.setBlockType(BlockBuilder.BlockType.DOOR);
        return configurator.configure(builder).register();
    }

    public static Block registerSandBlock(String name, BlockBuilderConfigurator configurator) {
        BlockBuilder builder = new BlockBuilder(name)
                .creativeTab("westeros_sand_gravel_tab")
                .hardness(0.5f)
                .resistance(0.5f)
                .requiresShovel()
                .soundType("sand")
                .texture("sand"); // Default texture

        builder.setBlockType(BlockBuilder.BlockType.SAND);
        return configurator.configure(builder).register();
    }

    public static Block registerFanBlock(String name, BlockBuilderConfigurator configurator) {
        BlockBuilder builder = new BlockBuilder(name)
                .creativeTab("westeros_water_air_tab")
                .hardness(0.0f)
                .resistance(0.0f)
                .noCollision()
                .dropsNothing()
                .soundType("stone")
                .nonOpaque()
                .alphaRender()
                .texture("coral/brain/fan1"); // Default texture

        builder.setBlockType(BlockBuilder.BlockType.FAN);
        return configurator.configure(builder).register();
    }

    public static Block registerVinesBlock(String name, BlockBuilderConfigurator configurator) {
        BlockBuilder builder = new BlockBuilder(name)
                .creativeTab("westeros_foliage_tab")
                .hardness(0.0f)
                .resistance(0.0f)
                .noCollision()
                .dropsNothing()
                .soundType("grass")
                .nonOpaque()
                .alphaRender()
                .texture("vines/vines"); // Default texture

        builder.setBlockType(BlockBuilder.BlockType.VINES);
        return configurator.configure(builder).register();
    }

    public static Block registerHalfDoorBlock(String name, BlockBuilderConfigurator configurator) {
        BlockBuilder builder = new BlockBuilder(name)
                .creativeTab("westeros_windows_glass_tab")
                .hardness(2.0f)
                .resistance(5.0f)
                .harvestLevel(1)
                .requiresAxe()
                .woodType(WoodType.BIRCH)
                .soundType("wood")
                .allowUnsupported()
                .texture("wood/birch/shutters"); // Default texture

        builder.setBlockType(BlockBuilder.BlockType.HALF_DOOR);
        return configurator.configure(builder).register();
    }

    public static Block registerPlantBlock(String name, BlockBuilderConfigurator configurator) {
        BlockBuilder builder = new BlockBuilder(name)
                .creativeTab("westeros_flowers_tab")
                .hardness(0.0f)
                .resistance(0.0f)
                .noCollision()
                .breakInstantly()
                .soundType("grass")
                .nonOpaque()
                .texture("flowers/plant"); // Default texture

        builder.setBlockType(BlockBuilder.BlockType.PLANT);
        return configurator.configure(builder).register();
    }

    public static Block registerCropBlock(String name, BlockBuilderConfigurator configurator) {
        BlockBuilder builder = new BlockBuilder(name)
                .creativeTab("westeros_crops_herbs_tab")
                .hardness(0.0f)
                .resistance(0.0f)
                .noCollision()
                .breakInstantly()
                .soundType("grass")
                .nonOpaque()
                .texture("crops/crop"); // Default texture

        builder.setBlockType(BlockBuilder.BlockType.CROP);
        return configurator.configure(builder).register();
    }

    public static Block registerFlowerPotBlock(String name, BlockBuilderConfigurator configurator) {
        BlockBuilder builder = new BlockBuilder(name)
                .creativeTab("westeros_flowers_tab")
                .hardness(0.0f)
                .resistance(0.0f)
                .soundType("stone")
                .nonOpaque()
                .texture("flower_pot"); // Default texture

        builder.setBlockType(BlockBuilder.BlockType.FLOWER_POT);
        return configurator.configure(builder).register();
    }

    public static Block registerWebBlock(String name, BlockBuilderConfigurator configurator) {
        BlockBuilder builder = new BlockBuilder(name)
                .creativeTab("westeros_misc_tab")
                .hardness(0.8f)
                .resistance(0.8f)
                .noCollision()
                .soundType("cloth")
                .nonOpaque()
                .alphaRender()
                .texture("web_block/web"); // Default texture

        builder.setBlockType(BlockBuilder.BlockType.WEB);
        return configurator.configure(builder).register();
    }

    public static Block registerLadderBlock(String name, BlockBuilderConfigurator configurator) {
        BlockBuilder builder = new BlockBuilder(name)
                .creativeTab("westeros_decor_tab")
                .hardness(0.4f)
                .resistance(3.0f)
                .soundType("ladder")
                .nonOpaque()
                .texture("ladder"); // Default texture

        builder.setBlockType(BlockBuilder.BlockType.LADDER);
        return configurator.configure(builder).register();
    }

    public static Block registerPaneBlock(String name, BlockBuilderConfigurator configurator) {
        BlockBuilder builder = new BlockBuilder(name)
                .creativeTab("westeros_windows_glass_tab")
                .hardness(1.0f)
                .resistance(3.0f)
                .soundType("glass")
                .nonOpaque()
                .texture("vertical_net/vertical_net1");

        builder.setBlockType(BlockBuilder.BlockType.PANE);
        return configurator.configure(builder).register();
    }

    public static Block registerRailBlock(String name, BlockBuilderConfigurator configurator) {
        BlockBuilder builder = new BlockBuilder(name)
                .creativeTab("westeros_cloth_fibers_tab")
                .hardness(0.7f)
                .resistance(3.5f)
                .soundType("metal")
                .nonOpaque()
                .noCollision()
                .texture("rail/rail"); // Default texture

        builder.setBlockType(BlockBuilder.BlockType.RAIL);
        return configurator.configure(builder).register();
    }

    public static Block registerSolidBlock(String name, BlockBuilderConfigurator configurator) {
        BlockBuilder builder = new BlockBuilder(name)
                .creativeTab("westeros_decor_tab")
                .hardness(2.0f)
                .resistance(6.0f)
                .soundType("stone")
                .texture("stone/stone"); // Default texture

        builder.setBlockType(BlockBuilder.BlockType.SOLID);
        return configurator.configure(builder).register();
    }

    public static Block registerFireBlock(String name, BlockBuilderConfigurator configurator) {
        BlockBuilder builder = new BlockBuilder(name)
                .creativeTab("westeros_fire_tab")
                .hardness(0.0f)
                .resistance(0.0f)
                .noCollision()
                .breakInstantly()
                .soundType("fire")
                .nonOpaque()
                .alphaRender()
                .texture("fire/fire"); // Default texture

        builder.setBlockType(BlockBuilder.BlockType.FIRE);
        return configurator.configure(builder).register();
    }


    /**
     * Initialize all builder-based blocks
     */
    public static void initialize() {
        WesterosBlocks.LOGGER.info("Initializing builder-based blocks");
        // The static final fields will be initialized when this class is loaded
    }
}