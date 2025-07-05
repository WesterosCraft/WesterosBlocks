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

    /**
     * Initialize all builder-based blocks
     */
    public static void initialize() {
        WesterosBlocks.LOGGER.info("Initializing builder-based blocks");
        // The static final fields will be initialized when this class is loaded
    }
} 