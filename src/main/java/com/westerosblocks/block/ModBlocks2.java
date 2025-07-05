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

    /**
     * Initialize all builder-based blocks
     */
    public static void initialize() {
        WesterosBlocks.LOGGER.info("Initializing builder-based blocks");
        // The static final fields will be initialized when this class is loaded
    }
} 