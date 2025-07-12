package com.westerosblocks.block;

import com.westerosblocks.WesterosBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.WoodType;

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

    /**
     * Initialize all builder-based blocks
     */
    public static void initialize() {
        WesterosBlocks.LOGGER.info("Initializing builder-based blocks");
        // The static final fields will be initialized when this class is loaded
    }
}