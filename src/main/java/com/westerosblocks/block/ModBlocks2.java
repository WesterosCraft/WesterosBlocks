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

    // CLEAN BLOCK REGISTRATION USING BUILDER PATTERN WITH DEFAULTS

    public static final Block ARBOR_BRICK_ARROW_SLIT = registerArrowSlitBlock(
        "arbor_brick_arrow_slit",
        builder -> builder
            .creativeTab("westeros_decor_tab")
            .texture("ashlar_third/arbor/all")
    );

    public static final Block BLACK_GRANITE_ARROW_SLIT = registerArrowSlitBlock(
        "black_granite_arrow_slit",
        builder -> builder
            .creativeTab("westeros_decor_tab")
            .texture("ashlar_third/black/all")
    );

    public static final Block OAK_TABLE = registerTableBlock(
        "oak_table",
        builder -> builder
            .creativeTab("westeros_furniture_tab")
            .texture("oak_planks")
            .woodType(WoodType.OAK)
    );

    public static final Block BIRCH_TABLE = registerTableBlock(
        "birch_table",
        builder -> builder
            .creativeTab("westeros_furniture_tab")
            .texture("birch_planks")
            .woodType(WoodType.BIRCH)
    );

    public static final Block SPRUCE_TABLE = registerTableBlock(
        "spruce_table",
        builder -> builder
            .creativeTab("westeros_furniture_tab")
            .texture("spruce_planks")
            .woodType(WoodType.SPRUCE)
    );

    public static final Block OAK_CHAIR = registerChairBlock(
        "oak_chair",
        builder -> builder
            .creativeTab("westeros_furniture_tab")
            .texture("wood/oak/all")
            .woodType(WoodType.OAK)
    );

    public static final Block BIRCH_CHAIR = registerChairBlock(
        "birch_chair",
        builder -> builder
            .creativeTab("westeros_furniture_tab")
            .texture("wood/birch/all")
            .woodType(WoodType.BIRCH)
    );

    public static final Block SPRUCE_CHAIR = registerChairBlock(
        "spruce_chair",
        builder -> builder
            .creativeTab("westeros_furniture_tab")
            .texture("wood/spruce/all")
            .woodType(WoodType.SPRUCE)
    );

    public static final Block OAK_WAY_SIGN = registerWaySignBlock(
        "oak_way_sign",
        builder -> builder
            .creativeTab("westeros_decor_tab")
            .texture("wood/oak/all")
            .woodType(WoodType.OAK)
    );

    public static final Block KINGS_LANDING_SEWER_MANHOLE = registerStandaloneTrapDoorBlock(
        "kings_landing_sewer_manhole",
        builder -> builder
            .creativeTab("westeros_decor_tab")
            .hardness(2.0f)
            .resistance(5.0f)
            .texture("trapdoor_block/kings_landing_manhole")
            .woodType(WoodType.OAK)
            .soundType("iron")
    );

    public static final Block OLDTOWN_SEWER_MANHOLE = registerStandaloneTrapDoorBlock(
        "oldtown_sewer_manhole",
        builder -> builder
            .creativeTab("westeros_decor_tab")
            .hardness(2.0f)
            .resistance(5.0f)
            .texture("trapdoor_block/oldtown_manhole")
            .woodType(WoodType.OAK)
            .soundType("iron")
    );

    public static final Block SEWER_MANHOLE = registerStandaloneTrapDoorBlock(
        "sewer_manhole",
        builder -> builder
            .creativeTab("westeros_decor_tab")
            .hardness(2.0f)
            .resistance(5.0f)
            .texture("trapdoor_block/manhole")
            .woodType(WoodType.OAK)
            .soundType("iron")
    );

    public static final Block WHITE_HARBOR_SEWER_MANHOLE = registerStandaloneTrapDoorBlock(
        "white_harbor_sewer_manhole",
        builder -> builder
            .creativeTab("westeros_decor_tab")
            .hardness(2.0f)
            .resistance(5.0f)
            .texture("trapdoor_block/white_harbor_manhole")
            .woodType(WoodType.OAK)
            .soundType("iron")
    );

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
            .textures("bark/oak/top", "bark/oak/top", "bark/oak/chain")
    );

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
            .textures("bark/jungle/top", "bark/jungle/top", "bark/jungle/chain")
    );

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
            .textures("bark/jungle/top", "bark/jungle/top", "bark/jungle/rope")
    );

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
            .textures("archery_target/front", "archery_target/front", "archery_target/side")
    );

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
            .textures("barrel_closed/barrel_top_closed", "barrel_closed/barrel_top_closed", "barrel_sides/side1")
    );

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
            .textures("firewood/top", "firewood/top", "firewood/side")
    );

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
            .textures("marble/quartz/column_topbottom", "marble/quartz/column_topbottom", "marble/quartz/column_side")
    );

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
            .textures("marble/quartz/column_topbottom", "marble/quartz/column_topbottom", "marble/quartz/column_side_ctm")
    );

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
            .textures("bark/oak/mossy/bottom", "bark/oak/mossy/top", "bark/oak/mossy/side")
    );

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
            .textures("bark/birch/mossy/bottom", "bark/birch/mossy/top", "bark/birch/mossy/side")
    );

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
            .textures("bark/spruce/mossy/bottom", "bark/spruce/mossy/top", "bark/spruce/mossy/side")
    );

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
            .textures("bark/jungle/mossy/bottom", "bark/jungle/mossy/top", "bark/jungle/mossy/side")
    );

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
            .textures("bark/oak/top", "bark/oak/top", "bark/oak/rope")
    );

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
            .textures("bark/palm/top", "bark/palm/top", "bark/palm/side")
    );

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
            .textures("ashlar_third/sandstone/column_top", "ashlar_third/sandstone/column_top", "ashlar_third/sandstone/column_side")
    );

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
            .textures("bark/spruce/top", "bark/spruce/top", "bark/spruce/chain")
    );

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
            .textures("bark/spruce/top", "bark/spruce/top", "bark/spruce/rope")
    );

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
            .textures("stacked_bones/bone_stacked_front", "stacked_bones/bone_stacked_front", "stacked_bones/bone_stacked_side")
    );

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
            .textures("bark/weirwood/face_0", "bark/weirwood/face_0", "bark/weirwood/side")
    );

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
            .textures("bark/weirwood/face_1", "bark/weirwood/face_1", "bark/weirwood/side")
    );

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
            .textures("bark/weirwood/face_2", "bark/weirwood/face_2", "bark/weirwood/side")
    );

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
            .textures("bark/weirwood/face_3", "bark/weirwood/face_3", "bark/weirwood/side")
    );

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
            .textures("bark/weirwood/face_4", "bark/weirwood/face_4", "bark/weirwood/side")
    );

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
            .textures("bark/weirwood/face_5", "bark/weirwood/face_5", "bark/weirwood/side")
    );

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
            .textures("bark/weirwood/face_6", "bark/weirwood/face_6", "bark/weirwood/side")
    );

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
            .textures("bark/weirwood/face_7", "bark/weirwood/face_7", "bark/weirwood/side")
    );

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
            .textures("bark/weirwood/face_8", "bark/weirwood/face_8", "bark/weirwood/side")
    );

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
            .textures("bark/weirwood/scars", "bark/weirwood/scars", "bark/weirwood/side")
    );

    // TORCH BLOCKS
    public static final Block TORCH = registerTorchBlock(
        "torch",
        builder -> builder
            .creativeTab("westeros_lighting_tab")
            .texture("lighting/torch")
            .allowUnsupported()
            .soundType("metal")
            .lightLevel(13)
            .alphaRender()
    );

    public static final Block TORCH_UNLIT = registerTorchBlock(
        "torch_unlit",
        builder -> builder
            .creativeTab("westeros_lighting_tab")
            .texture("lighting/torch_unlit")
            .allowUnsupported()
            .noParticle()
            .soundType("metal")
            .lightLevel(0)
            .alphaRender()
    );

    public static final Block CANDLE = registerTorchBlock(
        "candle",
        builder -> builder
            .creativeTab("westeros_lighting_tab")
            .texture("lighting/candle")
            .allowUnsupported()
            .soundType("powder")
            .lightLevel(10)
            .alphaRender()
    );

    public static final Block CANDLE_UNLIT = registerTorchBlock(
        "candle_unlit",
        builder -> builder
            .creativeTab("westeros_lighting_tab")
            .texture("lighting/candle_unlit")
            .allowUnsupported()
            .noParticle()
            .soundType("powder")
            .lightLevel(0)
            .alphaRender()
    );

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
            .textures("wood/white/door_top", "wood/white/door_bottom")
    );

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
            .textures("wood/white/door_locked_top", "wood/white/door_locked_bottom")
    );

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
            .textures("wood/northern/door_top", "wood/northern/door_bottom")
    );

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
            .textures("wood/spruce/door_top", "wood/spruce/door_bottom")
    );

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
            .textures("wood/oak/door_top", "wood/oak/door_bottom")
    );

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
            .textures("wood/birch/door_top", "wood/birch/door_bottom")
    );

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
            .textures("door_block/door_weirwood_top", "door_block/door_weirwood_bottom")
    );

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
            .textures("wood/grey/door_top", "wood/grey/door_bottom")
    );

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
            .textures("wood/jungle/door_top", "wood/jungle/door_bottom")
    );

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
            .textures("ashlar_third/pale_red/all_noctm", "ashlar_third/pale_red/all_noctm")
    );

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
            .textures("ashlar_third/black/all_noctm", "ashlar_third/black/all_noctm")
    );

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
            .textures("wood/birch/door_locked_top", "wood/birch/door_locked_bottom")
    );

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
            .textures("wood/northern/door_locked_top", "wood/northern/door_locked_bottom")
    );

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
            .textures("wood/grey/door_locked_top", "wood/grey/door_locked_bottom")
    );

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
            .textures("wood/spruce/door_locked_top", "wood/spruce/door_locked_bottom")
    );

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
            .textures("wood/jungle/door_locked_top", "wood/jungle/door_locked_bottom")
    );

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
            .textures("wood/oak/door_locked_top", "wood/oak/door_locked_bottom")
    );

    // SAND BLOCKS
    public static final Block SAND_SKELETON = registerSandBlock(
        "sand_skeleton",
        builder -> builder
            .creativeTab("westeros_sand_gravel_tab")
            .hardness(0.5f)
            .resistance(0.5f)
            .requiresShovel()
            .soundType("sand")
            .texture("sand_block/sand_skeleton")
    );

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
            .texture("coral/brain/fan1")
    );

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
            .texture("coral/bubble/fan1")
    );

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
            .texture("coral/fire/fan1")
    );

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
            .texture("coral/horn/fan1")
    );

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
            .texture("coral/tube/fan1")
    );

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
            .texture("dappled_moss/dappled")
    );

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
            .textures("jasmine_vines/side1", "jasmine_vines/side1")
    );

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
            .textures("vines/side1", "vines/side1")
    );

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
            .textures("alyssas_tears_mist/mist1", "alyssas_tears_mist/mist1")
    );

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
            .textures("alyssas_tears_mist/mist2", "alyssas_tears_mist/mist2")
    );

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
            .textures("alyssas_tears_mist/mist3", "alyssas_tears_mist/mist3")
    );

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
            .textures("alyssas_tears_mist/mist4", "alyssas_tears_mist/mist4")
    );

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
            .texture("wood/birch/shutters")
    );

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
            .texture("shutter_block/shutters_dorne")
    );

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
            .texture("shutter_block/shutters_lannisport")
    );

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
            .texture("wood/grey/shutters")
    );

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
            .texture("wood/jungle/shutters")
    );

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
            .texture("wood/northern/shutters")
    );

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
            .texture("wood/oak/shutters")
    );

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
            .texture("shutter_block/shutters_reach")
    );

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
            .texture("wood/spruce/shutters")
    );

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
            .texture("wood/white/shutters")
    );

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
            .texture("flowers/blue_bells")
    );

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
            .texture("flowers/blue_chicory/side1")
    );


    // BATCH REGISTRATION METHODS FOR MASS BLOCK CREATION
    // public static void registerArrowSlits() {
    //     String[] materials = {
    //         "arbor_brick", "black_granite", "grey_granite", "white_granite",
    //         "red_brick", "yellow_brick", "blue_brick", "green_brick"
    //     };

    //     for (String material : materials) {
    //         registerArrowSlitBlock(material + "_arrow_slit", builder -> builder
    //             .texture("westerosblocks:block/ashlar_third/" + material.replace("_brick", "") + "/all"));
    //     }
    // }

    /**
     * Parses block type string into parameters and flags
     */
    public static java.util.Map<String, String> parseBlockParameters(String typeString) {
        java.util.Map<String, String> params = new java.util.HashMap<>();
        if (typeString != null) {
            for (String token : typeString.split(",")) {
                token = token.trim();
                if (token.contains(":")) {
                    String[] parts = token.split(":", 2);
                    params.put(parts[0].trim(), parts[1].trim());
                } else {
                    // For flags without values, store them with an empty string value
                    params.put(token, "");
                }
            }
        }
        return params;
    }

    // NEW BUILDER-BASED REGISTRATION METHODS WITH DEFAULTS

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

    public static Block registerStandaloneTrapDoorBlock(String name, BlockBuilderConfigurator configurator) {
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

    /**
     * Initialize all builder-based blocks
     */
    public static void initialize() {
        WesterosBlocks.LOGGER.info("Initializing builder-based blocks");
        // The static final fields will be initialized when this class is loaded
    }
} 