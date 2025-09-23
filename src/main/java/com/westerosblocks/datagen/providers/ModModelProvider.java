package com.westerosblocks.datagen.providers;

import com.westerosblocks.block.ModBlocks;
import com.westerosblocks.block.PlantBlocks;
import com.westerosblocks.block.SolidBlocks;
import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.data.BlockDefinitionRegistry;
import com.westerosblocks.datagen.custom.*;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;


import static com.westerosblocks.datagen.ModBlockStateModelGenerator.*;

public class ModModelProvider extends FabricModelProvider {

    private final FabricDataOutput output;

    public ModModelProvider(FabricDataOutput output) {
        super(output);
        this.output = output;
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator bsmg) {
        generateModelsFromDefinitions(bsmg);

        // Solid Blocks
//        registerCustomSolidBlock(bsmg, SolidBlocks.TIMBER_NORTHERN_BLUE_BRESSUMMER).state("plaster/smooth/gulltown_blue/all", "wood/northern/all", "plaster/smooth/gulltown_blue/all").state("plaster/smooth/light_blue/all", "wood/northern/all", "plaster/smooth/light_blue/all").state("plaster/smooth/blue/all", "wood/northern/all", "plaster/smooth/blue/all").state("plaster/rough/gulltown_blue/all1", "wood/northern/all", "plaster/rough/gulltown_blue/all1").state("plaster/wattle/gulltown_blue/all", "wood/northern/all", "plaster/wattle/gulltown_blue/all").build();
//        registerCustomSolidBlock(bsmg, SolidBlocks.TIMBER_NORTHERN_GREEN_LEFTHATCH).state("plaster/smooth/gulltown_green/all", "wood/northern/all","plaster/smooth/gulltown_green/all").state("plaster/smooth/green/all", "wood/northern/all", "plaster/smooth/green/all").state("plaster/smooth/highgarden_green/all", "wood/northern/all", "plaster/smooth/highgarden_green/all").state("plaster/rough/gulltown_green/all1", "wood/northern/all", "plaster/rough/gulltown_green/all1").state("plaster/wattle/gulltown_green/all", "wood/northern/all", "plaster/wattle/gulltown_green/all").build();

        // Table Blocks
        registerCustomTableBlock(bsmg, ModBlocks.OAK_TABLE).texture("wood/oak/all").build();

        // Log Blocks
        registerCustomLogBlock(bsmg, ModBlocks.ARCHERY_TARGET)
                .textures("archery_target/side", "archery_target/front")
                .build();

        registerCustomLogBlock(bsmg, ModBlocks.CLOSED_BARREL)
                .textures("barrel_sides/side1", "barrel_closed/barrel_top_closed")
                .build();

        registerCustomLogBlock(bsmg, ModBlocks.FIREWOOD)
                .textures("firewood/side", "firewood/top")
                .build();

        registerCustomLogBlock(bsmg, ModBlocks.JUNGLE_LOG_CHAIN)
                .textures("bark/jungle/chain", "bark/jungle/top")
                .build();

        registerCustomLogBlock(bsmg, ModBlocks.JUNGLE_LOG_ROPE)
                .textures("bark/jungle/rope", "bark/jungle/top")
                .build();

        registerCustomLogBlock(bsmg, ModBlocks.MARBLE_PILLAR_VERTICAL_CTM)
                .textures("marble/quartz/column_side_ctm", "marble/quartz/column_topbottom")
                .build();

        registerCustomLogBlock(bsmg, ModBlocks.MARBLE_PILLAR)
                .textures("marble/quartz/column_side", "marble/quartz/column_topbottom")
                .build();

        registerCustomLogBlock(bsmg, ModBlocks.MOSSY_BIRCH_LOG)
                .textures("bark/birch/mossy/side", "bark/birch/mossy/top")
                .build();

        registerCustomLogBlock(bsmg, ModBlocks.MOSSY_JUNGLE_LOG)
                .textures("bark/jungle/mossy/side", "bark/jungle/mossy/top")
                .build();

        registerCustomLogBlock(bsmg, ModBlocks.MOSSY_OAK_LOG)
                .textures("bark/oak/mossy/side", "bark/oak/mossy/top")
                .build();

        registerCustomLogBlock(bsmg, ModBlocks.MOSSY_SPRUCE_LOG)
                .textures("bark/spruce/mossy/side", "bark/spruce/mossy/top")
                .build();

        registerCustomLogBlock(bsmg, ModBlocks.OAK_LOG_CHAIN)
                .textures("bark/oak/chain", "bark/oak/top")
                .build();

        registerCustomLogBlock(bsmg, ModBlocks.OAK_LOG_ROPE)
                .textures("bark/oak/rope", "bark/oak/top")
                .build();

        registerCustomLogBlock(bsmg, ModBlocks.PALM_TREE_LOG)
                .textures("bark/palm/side", "bark/palm/top")
                .build();

        registerCustomLogBlock(bsmg, ModBlocks.SANDSTONE_PILLAR)
                .textures("ashlar_third/sandstone/column_side", "ashlar_third/sandstone/column_top")
                .build();

        registerCustomLogBlock(bsmg, ModBlocks.SPRUCE_LOG_CHAIN)
                .textures("bark/spruce/chain", "bark/spruce/top")
                .build();

        registerCustomLogBlock(bsmg, ModBlocks.SPRUCE_LOG_ROPE)
                .textures("bark/spruce/rope", "bark/spruce/top")
                .build();

        registerCustomLogBlock(bsmg, ModBlocks.STACKED_BONES)
                .textures("stacked_bones/bone_stacked_side", "stacked_bones/bone_stacked_front")
                .build();

        registerCustomLogBlock(bsmg, ModBlocks.WEIRWOOD_FACE_0)
                .textures("bark/weirwood/side", "bark/weirwood/face_0")
                .build();

        registerCustomLogBlock(bsmg, ModBlocks.WEIRWOOD_FACE_1)
                .textures("bark/weirwood/side", "bark/weirwood/face_1")
                .build();

        registerCustomLogBlock(bsmg, ModBlocks.WEIRWOOD_FACE_2)
                .textures("bark/weirwood/side", "bark/weirwood/face_2")
                .build();

        registerCustomLogBlock(bsmg, ModBlocks.WEIRWOOD_FACE_3)
                .textures("bark/weirwood/side", "bark/weirwood/face_3")
                .build();

        registerCustomLogBlock(bsmg, ModBlocks.WEIRWOOD_FACE_4)
                .textures("bark/weirwood/side", "bark/weirwood/face_4")
                .build();

        registerCustomLogBlock(bsmg, ModBlocks.WEIRWOOD_FACE_5)
                .textures("bark/weirwood/side", "bark/weirwood/face_5")
                .build();

        registerCustomLogBlock(bsmg, ModBlocks.WEIRWOOD_FACE_6)
                .textures("bark/weirwood/side", "bark/weirwood/face_6")
                .build();

        registerCustomLogBlock(bsmg, ModBlocks.WEIRWOOD_FACE_7)
                .textures("bark/weirwood/side", "bark/weirwood/face_7")
                .build();

        registerCustomLogBlock(bsmg, ModBlocks.WEIRWOOD_FACE_8)
                .textures("bark/weirwood/side", "bark/weirwood/face_8")
                .build();

        registerCustomLogBlock(bsmg, ModBlocks.WEIRWOOD_SCARS)
                .textures("bark/weirwood/side", "bark/weirwood/scars")
                .build();

        registerCustomLogBlock(bsmg, ModBlocks.STRIPPED_OAK_LOG)
                .textures("wood/oak/stripped_oak_log", "wood/oak/stripped_oak_log_top")
                .build();

        // Slab Blocks
        registerCustomSlabBlock(bsmg, ModBlocks.APPLE_BASKET_SLAB)
                .textures("crate_block/basket_bottom", "crate_block/basket_apple",
                        "crate_block/basket_side_slab")
                .build();

        registerCustomSlabBlock(bsmg, ModBlocks.APRICOT_BASKET_SLAB)
                .textures("crate_block/basket_bottom", "crate_block/basket_apricot",
                        "crate_block/basket_side_slab")
                .build();

        registerCustomSlabBlock(bsmg, ModBlocks.CLOSED_BASKET_SLAB)
                .textures("crate_block/basket_bottom", "crate_block/basket_top_closed",
                        "crate_block/basket_side_slab")
                .build();

        registerCustomSlabBlock(bsmg, ModBlocks.BERRY_BASKET_SLAB)
                .textures("crate_block/basket_bottom", "crate_block/basket_berry",
                        "crate_block/basket_side_slab")
                .build();

        registerCustomSlabBlock(bsmg, ModBlocks.CARROT_BASKET_SLAB)
                .textures("crate_block/basket_bottom", "crate_block/basket_carrot",
                        "crate_block/basket_side_slab")
                .build();

        registerCustomSlabBlock(bsmg, ModBlocks.CUT_GRAIN_FLOUR_SACK)
                .textures("grain_sack/all", "grain_sack/cut", "grain_sack/front", "grain_sack/front",
                        "grain_sack/side")
                .build();

        registerCustomSlabBlock(bsmg, ModBlocks.DATE_BASKET_SLAB)
                .textures("crate_block/basket_bottom", "crate_block/basket_dates",
                        "crate_block/basket_side_slab")
                .build();

        registerCustomSlabBlock(bsmg, ModBlocks.FIREWOOD_SLAB)
                .textures("firewood/side", "firewood/side", "firewood/top")
                .build();

        registerCustomSlabBlock(bsmg, ModBlocks.FISH_BASKET_SLAB)
                .textures("crate_block/basket_bottom", "crate_block/basket_fish",
                        "crate_block/basket_side_slab")
                .build();

        registerCustomSlabBlock(bsmg, ModBlocks.GRAIN_BASKET_SLAB)
                .textures("crate_block/basket_bottom", "crate_block/basket_grain",
                        "crate_block/basket_side_slab")
                .build();

        registerCustomSlabBlock(bsmg, ModBlocks.GRAIN_FLOUR_SACK)
                .textures("grain_sack/all", "grain_sack/all", "grain_sack/front", "grain_sack/front",
                        "grain_sack/side")
                .build();

        registerCustomSlabBlock(bsmg, ModBlocks.HOP_BASKET_SLAB)
                .textures("crate_block/basket_bottom", "crate_block/basket_hop",
                        "crate_block/basket_side_slab")
                .build();

        // Branch Blocks
        registerCustomBranchBlock(bsmg, ModBlocks.OAK_BRANCH).texture("bark/oak/side")
                .build();
        registerCustomBranchBlock(bsmg, ModBlocks.BIRCH_BRANCH).texture("bark/birch/side").build();

        // Door Blocks
        registerCustomDoorBlock(bsmg, ModBlocks.BIRCH_DOOR)
                .textures("wood/birch/door_top", "wood/birch/door_bottom").build();
        registerCustomDoorBlock(bsmg, ModBlocks.EYRIE_WEIRWOOD_DOOR)
                .textures("door_block/door_weirwood_top", "door_block/door_weirwood_bottom").build();
        registerCustomDoorBlock(bsmg, ModBlocks.GREY_WOOD_DOOR)
                .textures("wood/grey/door_top", "wood/grey/door_bottom").build();
        registerCustomDoorBlock(bsmg, ModBlocks.HARRENHAL_SECRET_DOOR)
                .textures("ashlar_third/black/all_noctm", "ashlar_third/black/all_noctm").build();
        registerCustomDoorBlock(bsmg, ModBlocks.JUNGLE_DOOR)
                .textures("wood/jungle/door_top", "wood/jungle/door_bottom").build();
        registerCustomDoorBlock(bsmg, ModBlocks.LOCKED_BIRCH_DOOR)
                .textures("wood/birch/door_locked_top", "wood/birch/door_locked_bottom").build();
        registerCustomDoorBlock(bsmg, ModBlocks.LOCKED_DARK_NORTHERN_WOOD_DOOR)
                .textures("wood/northern/door_locked_top", "wood/northern/door_locked_bottom").build();
        registerCustomDoorBlock(bsmg, ModBlocks.LOCKED_GREY_WOOD_DOOR)
                .textures("wood/grey/door_locked_top", "wood/grey/door_locked_bottom").build();
        registerCustomDoorBlock(bsmg, ModBlocks.LOCKED_JUNGLE_DOOR)
                .textures("wood/jungle/door_locked_top", "wood/jungle/door_locked_bottom").build();
        registerCustomDoorBlock(bsmg, ModBlocks.LOCKED_OAK_DOOR)
                .textures("wood/oak/door_locked_top", "wood/oak/door_locked_bottom").build();
        registerCustomDoorBlock(bsmg, ModBlocks.LOCKED_SPRUCE_DOOR)
                .textures("wood/spruce/door_locked_top", "wood/spruce/door_locked_bottom").build();
        registerCustomDoorBlock(bsmg, ModBlocks.LOCKED_WHITE_WOOD_DOOR)
                .textures("wood/white/door_locked_top", "wood/white/door_locked_bottom").build();
        registerCustomDoorBlock(bsmg, ModBlocks.NORTHERN_WOOD_DOOR)
                .textures("wood/northern/door_top", "wood/northern/door_bottom").build();
        registerCustomDoorBlock(bsmg, ModBlocks.OAK_DOOR)
                .textures("wood/oak/door_top", "wood/oak/door_bottom").build();
        registerCustomDoorBlock(bsmg, ModBlocks.RED_KEEP_SECRET_DOOR)
                .textures("ashlar_third/black/all_noctm", "ashlar_third/black/all_noctm").build();
        registerCustomDoorBlock(bsmg, ModBlocks.SPRUCE_DOOR)
                .textures("wood/spruce/door_top", "wood/spruce/door_bottom").build();
        registerCustomDoorBlock(bsmg, ModBlocks.WHITE_WOOD_DOOR)
                .textures("wood/white/door_top", "wood/white/door_bottom").build();

        // Half Door Blocks (Shutters)
        registerCustomHalfDoorBlock(bsmg, ModBlocks.BIRCH_WINDOW_SHUTTERS)
                .texture("wood/birch/shutters").build();
        registerCustomHalfDoorBlock(bsmg, ModBlocks.DORNE_RED_WINDOW_SHUTTERS)
                .texture("shutter_block/shutters_dorne").build();
        registerCustomHalfDoorBlock(bsmg, ModBlocks.GREEN_LANNISPORT_WINDOW_SHUTTERS)
                .texture("shutter_block/shutters_lannisport").build();
        registerCustomHalfDoorBlock(bsmg, ModBlocks.GREY_WOOD_WINDOW_SHUTTERS)
                .texture("wood/grey/shutters").build();
        registerCustomHalfDoorBlock(bsmg, ModBlocks.JUNGLE_WINDOW_SHUTTERS)
                .texture("wood/jungle/shutters").build();
        registerCustomHalfDoorBlock(bsmg, ModBlocks.NORTHERN_WOOD_WINDOW_SHUTTERS)
                .texture("wood/northern/shutters").build();
        registerCustomHalfDoorBlock(bsmg, ModBlocks.OAK_WINDOW_SHUTTERS)
                .texture("wood/oak/shutters").build();
        registerCustomHalfDoorBlock(bsmg, ModBlocks.REACH_BLUE_WINDOW_SHUTTERS)
                .texture("shutter_block/shutters_reach").build();
        registerCustomHalfDoorBlock(bsmg, ModBlocks.SPRUCE_WINDOW_SHUTTERS)
                .texture("wood/spruce/shutters").build();
        registerCustomHalfDoorBlock(bsmg, ModBlocks.WHITE_WOOD_WINDOW_SHUTTERS)
                .texture("wood/white/shutters").build();

        // Pane Blocks
        registerCustomPaneBlock(bsmg, ModBlocks.DORNE_CARVED_STONE_WINDOW)
                .texture("pane_block/moorish_stone_window_pane").build();
        registerCustomPaneBlock(bsmg, ModBlocks.DORNE_CARVED_WOODEN_WINDOW)
                .texture("pane_block/moorish_wood_window_pane").build();
        registerCustomPaneBlock(bsmg, ModBlocks.IRON_BARS)
                .texture("bars_iron_block/iron_bars").build();
        registerCustomPaneBlock(bsmg, ModBlocks.IRON_CROSSBAR)
                .texture("bars_iron_block/bars_iron_crossbars").build();
        registerCustomPaneBlock(bsmg, ModBlocks.OXIDIZED_IRON_BARS)
                .texture("bars_iron_block/bars_iron_oxidized").build();
        registerCustomPaneBlock(bsmg, ModBlocks.OXIDIZED_IRON_CROSSBAR)
                .texture("bars_iron_block/bars_iron_oxidized_crossbars").build();
        registerCustomPaneBlock(bsmg, ModBlocks.VERTICAL_NET)
                .randomTexture("vertical_net/vertical_net1")
                .randomTexture("vertical_net/vertical_net2")
                .randomTexture("vertical_net/vertical_net3")
                .randomTexture("vertical_net/vertical_net4")
                .randomTexture("vertical_net/vertical_net5")
                .build();

        // Torch Blocks
        registerCustomTorchBlock(bsmg, ModBlocks.TORCH).texture("lighting/torch").build();
        registerCustomTorchBlock(bsmg, ModBlocks.TORCH_UNLIT).texture("lighting/torch_unlit").build();
        registerCustomTorchBlock(bsmg, ModBlocks.CANDLE).texture("lighting/candle").build();
        registerCustomTorchBlock(bsmg, ModBlocks.CANDLE_UNLIT).texture("lighting/candle_unlit").build();

        // Chair Blocks
        registerCustomChairBlock(bsmg, ModBlocks.OAK_CHAIR).texture("bark/oak/side").build();

        // Arrow Slit Blocks
        registerCustomArrowSlitBlock(bsmg, ModBlocks.ARBOR_BRICK_ARROW_SLIT).texture("ashlar_third/arbor/all").build();

        // Fan Blocks
        registerCustomFanBlock(bsmg, ModBlocks.CORAL_TUBE_FAN)
                .randomTexture("coral/tube/fan1")
                .randomTexture("coral/tube/fan2")
                .build();

        registerCustomFanBlock(bsmg, ModBlocks.CORAL_BRAIN_FAN)
                .randomTexture("coral/brain/fan1")
                .randomTexture("coral/brain/fan2")
                .build();

        registerCustomFanBlock(bsmg, ModBlocks.CORAL_BUBBLE_FAN)
                .randomTexture("coral/bubble/fan1")
                .randomTexture("coral/bubble/fan2")
                .build();

        registerCustomFanBlock(bsmg, ModBlocks.CORAL_FIRE_FAN)
                .randomTexture("coral/fire/fan1")
                .randomTexture("coral/fire/fan2")
                .build();

        registerCustomFanBlock(bsmg, ModBlocks.CORAL_HORN_FAN)
                .randomTexture("coral/horn/fan1")
                .randomTexture("coral/horn/fan2")
                .build();

        // Rail Blocks
        registerCustomRailBlock(bsmg, ModBlocks.FANCY_BLUE_CARPET).texture("carpet/fancy_blue_carpet").build();
        registerCustomRailBlock(bsmg, ModBlocks.FANCY_RED_CARPET).texture("carpet/fancy_red_carpet").build();
        registerCustomRailBlock(bsmg, ModBlocks.HORIZONTAL_CHAIN).textures("rail_block/chain", "rail_block/chain_turned").build();
        registerCustomRailBlock(bsmg, ModBlocks.HORIZONTAL_NET).textures("rail_block/net_large", "rail_block/net_large_turned").build();
        registerCustomRailBlock(bsmg, ModBlocks.HORIZONTAL_ROPE).textures("rail_block/rope", "rail_block/rope_turned").build();
        registerCustomRailBlock(bsmg, ModBlocks.PACKED_SNOW).textures("rail_block/packed_snow", "rail_block/packed_snow_turned").build();

        // Plant Block
        registerCustomPlantBlock(bsmg, PlantBlocks.BLUE_BELLS).texture("flowers/blue_bells").build();
        registerCustomPlantBlock(bsmg, PlantBlocks.BLUE_CHICORY)
                .randomTexture("flowers/blue_chicory/side1")
                .randomTexture("flowers/blue_chicory/side2")
                .randomTexture("flowers/blue_chicory/side3")
                .randomTexture("flowers/blue_chicory/side4")
                .build();
        registerCustomPlantBlock(bsmg, PlantBlocks.BLUE_FORGETMENOTS)
                .randomTexture("flowers/blue_forgetmenots1")
                .randomTexture("flowers/blue_forgetmenots2")
                .randomTexture("flowers/blue_forgetmenots3")
                .randomTexture("flowers/blue_forgetmenots4")
                .build();
        registerCustomPlantBlock(bsmg, PlantBlocks.BLUE_FLAX)
                .randomTexture("flowers/blue_flax1")
                .randomTexture("flowers/blue_flax2")
                .randomTexture("flowers/blue_flax3")
                .randomTexture("flowers/blue_flax4")
                .build();
        registerCustomPlantBlock(bsmg, PlantBlocks.BLUE_HYACINTH)
                .randomTexture("flowers/blue_hyacinth1")
                .randomTexture("flowers/blue_hyacinth2")
                .randomTexture("flowers/blue_hyacinth3")
                .randomTexture("flowers/blue_hyacinth4")
                .build();
        registerCustomPlantBlock(bsmg, PlantBlocks.BLUE_ORCHID)
                .randomTexture("flowers/blue_orchid1")
                .randomTexture("flowers/blue_orchid2")
                .randomTexture("flowers/blue_orchid3")
                .build();
        registerCustomPlantBlock(bsmg, PlantBlocks.BLUE_SWAMP_BELLS).texture("flowers/blue_swamp_bells1").build();

        registerCustomPlantBlock(bsmg, PlantBlocks.BRACKEN)
                .randomTexture("bracken/side1")
                .randomTexture("bracken/side2")
                .randomTexture("bracken/side3")
                .randomTexture("bracken/side4")
                .randomTexture("bracken/side5")
                .randomTexture("bracken/side6")
                .randomTexture("bracken/side7")
                .randomTexture("bracken/side8")
                .build();

        registerCustomPlantBlock(bsmg, PlantBlocks.BROWN_MUSHROOM_1).texture("brown_mushroom_block/mushroom_brown_0").build();
        registerCustomPlantBlock(bsmg, PlantBlocks.BROWN_MUSHROOM_2).texture("brown_mushroom_block/mushroom_brown_1").build();
        registerCustomPlantBlock(bsmg, PlantBlocks.BROWN_MUSHROOM_3).texture("brown_mushroom_block/mushroom_brown_2").build();
        registerCustomPlantBlock(bsmg, PlantBlocks.BROWN_MUSHROOM_4).texture("brown_mushroom_block/mushroom_brown_3").build();
        registerCustomPlantBlock(bsmg, PlantBlocks.BROWN_MUSHROOM_5).texture("brown_mushroom_block/mushroom_brown_4").build();
        registerCustomPlantBlock(bsmg, PlantBlocks.BROWN_MUSHROOM_6).texture("brown_mushroom_block/mushroom_brown_5").build();
        registerCustomPlantBlock(bsmg, PlantBlocks.BROWN_MUSHROOM_7).texture("brown_mushroom_block/mushroom_brown_6").build();
        registerCustomPlantBlock(bsmg, PlantBlocks.BROWN_MUSHROOM_8).texture("brown_mushroom_block/mushroom_brown_7").build();
        registerCustomPlantBlock(bsmg, PlantBlocks.BROWN_MUSHROOM_9).texture("brown_mushroom_block/mushroom_brown_8").build();
        registerCustomPlantBlock(bsmg, PlantBlocks.BROWN_MUSHROOM_10).texture("brown_mushroom_block/mushroom_brown_9").build();
        registerCustomPlantBlock(bsmg, PlantBlocks.BROWN_MUSHROOM_11).texture("brown_mushroom_block/mushroom_brown_10").build();
        registerCustomPlantBlock(bsmg, PlantBlocks.BROWN_MUSHROOM_12).texture("brown_mushroom_block/mushroom_brown_11").build();
        registerCustomPlantBlock(bsmg, PlantBlocks.BROWN_MUSHROOM_13).texture("brown_mushroom_block/mushroom_brown_12").build();

        registerCustomPlantBlock(bsmg, PlantBlocks.CORAL_BRAIN_WEB)
                .randomTexture("coral/brain/web1")
                .randomTexture("coral/brain/web2")
                .build();

        registerCustomPlantBlock(bsmg, PlantBlocks.CORAL_BUBBLE_WEB)
                .randomTexture("coral/bubble/web1")
                .randomTexture("coral/bubble/web2")
                .build();

        registerCustomPlantBlock(bsmg, PlantBlocks.CORAL_FIRE_WEB)
                .randomTexture("coral/fire/web1")
                .randomTexture("coral/fire/web2")
                .build();

        registerCustomPlantBlock(bsmg, PlantBlocks.CORAL_HORN_WEB)
                .randomTexture("coral/horn/web1")
                .randomTexture("coral/horn/web2")
                .build();

        registerCustomPlantBlock(bsmg, PlantBlocks.CORAL_TUBE_WEB)
                .randomTexture("coral/tube/web1")
                .randomTexture("coral/tube/web2")
                .build();

        registerCustomPlantBlock(bsmg, PlantBlocks.COW_PARSELY)
                .randomTexture("cow_parsely/side1")
                .randomTexture("cow_parsely/side2")
                .build();

        registerCustomPlantBlock(bsmg, PlantBlocks.CRANBERRY_BUSH)
                .randomTexture("cranberry/base1")
                .randomTexture("cranberry/base2")
                .build();

        registerCustomPlantBlock(bsmg, PlantBlocks.DEAD_BRACKEN)
                .randomTexture("dead_bracken/side1")
                .randomTexture("dead_bracken/side2")
                .randomTexture("dead_bracken/side3")
                .randomTexture("dead_bracken/side4")
                .randomTexture("dead_bracken/side5")
                .randomTexture("dead_bracken/side6")
                .randomTexture("dead_bracken/side7")
                .randomTexture("dead_bracken/side8")
                .randomTexture("dead_bracken/side9")
                .randomTexture("dead_bracken/side10")
                .randomTexture("dead_bracken/side11")
                .randomTexture("dead_bracken/side12")
                .build();

        registerCustomPlantBlock(bsmg, PlantBlocks.DEAD_BUSH)
                .randomTexture("dorne_bush_thorny/side1")
                .randomTexture("dorne_bush_thorny/side2")
                .randomTexture("dorne_bush_thorny/side3")
                .build();

        registerCustomPlantBlock(bsmg, PlantBlocks.DEAD_SCRUB_GRASS)
                .randomTexture("flowers/dead_scrub_grass1")
                .randomTexture("flowers/dead_scrub_grass2")
                .randomTexture("flowers/dead_scrub_grass3")
                .randomTexture("flowers/dead_scrub_grass4")
                .randomTexture("flowers/dead_scrub_grass5")
                .randomTexture("flowers/dead_scrub_grass6")
                .randomTexture("flowers/dead_scrub_grass7")
                .randomTexture("flowers/dead_scrub_grass8")
                .randomTexture("flowers/dead_scrub_grass9")
                .build();

        registerCustomPlantBlock(bsmg, PlantBlocks.DOCK_LEAF)
                .randomTexture("dock_leaf/side1")
                .randomTexture("dock_leaf/side2")
                .build();

        registerCustomPlantBlock(bsmg, PlantBlocks.FIREWEED)
                .randomTexture("fireweed/side1")
                .randomTexture("fireweed/side2")
                .randomTexture("fireweed/side3")
                .randomTexture("fireweed/side4")
                .build();

        registerCustomPlantBlock(bsmg, PlantBlocks.GRASS)
                .randomTexture("minecraft:block/fern/fern1")
                .randomTexture("minecraft:block/fern/fern2")
                .randomTexture("minecraft:block/fern/fern3")
                .randomTexture("minecraft:block/fern/fern4")
                .randomTexture("minecraft:block/fern/fern5")
                .randomTexture("minecraft:block/fern/fern6")
                .randomTexture("minecraft:block/fern/fern7")
                .randomTexture("minecraft:block/fern/fern8")
                .isTinted(true)
                .build();

        registerCustomPlantBlock(bsmg, PlantBlocks.GREEN_LEAFY_HERB)
                .texture("flowers/green_leafy_herb")
                .build();

        registerCustomPlantBlock(bsmg, PlantBlocks.GREEN_SCRUB_GRASS)
                .randomTexture("flowers/green_scrub_grass1")
                .randomTexture("flowers/green_scrub_grass2")
                .randomTexture("flowers/green_scrub_grass3")
                .randomTexture("flowers/green_scrub_grass4")
                .randomTexture("flowers/green_scrub_grass5")
                .randomTexture("flowers/green_scrub_grass6")
                .build();

        registerCustomPlantBlock(bsmg, PlantBlocks.GREEN_SPINY_HERB)
                .randomTexture("flowers/green_spiny_herb1")
                .randomTexture("flowers/green_spiny_herb2")
                .randomTexture("flowers/green_spiny_herb3")
                .randomTexture("flowers/green_spiny_herb4")
                .randomTexture("flowers/green_spiny_herb5")
                .build();

        registerCustomPlantBlock(bsmg, PlantBlocks.HEATHER)
                .randomTexture("heather/side1")
                .randomTexture("heather/side2")
                .randomTexture("heather/side3")
                .randomTexture("heather/side4")
                .build();

        registerCustomPlantBlock(bsmg, PlantBlocks.KELP)
                .randomTexture("kelp/side1")
                .randomTexture("kelp/side2")
                .build();

        registerCustomPlantBlock(bsmg, PlantBlocks.LADY_FERN)
                .randomTexture("lady_fern/side1")
                .randomTexture("lady_fern/side2")
                .randomTexture("lady_fern/side3")
                .randomTexture("lady_fern/side4")
                .build();

        registerCustomPlantBlock(bsmg, PlantBlocks.MAGENTA_ROSES)
                .randomTexture("flowers/magenta_roses1")
                .randomTexture("flowers/magenta_roses2")
                .randomTexture("flowers/magenta_roses3")
                .randomTexture("flowers/magenta_roses4")
                .build();

        registerCustomPlantBlock(bsmg, PlantBlocks.MEADOW_FESCUE)
                .randomTexture("flowers/meadow_fescue/side1")
                .randomTexture("flowers/meadow_fescue/side2")
                .build();

        registerCustomPlantBlock(bsmg, PlantBlocks.NETTLE)
                .randomTexture("nettle/side1")
                .randomTexture("nettle/side2")
                .randomTexture("nettle/side3")
                .build();

        registerCustomPlantBlock(bsmg, PlantBlocks.ORANGE_BELLS)
                .texture("flowers/orange_bells1")
                .build();

        registerCustomPlantBlock(bsmg, PlantBlocks.ORANGE_BOG_ASPHODEL)
                .randomTexture("flowers/orange_bog_asphodel1")
                .randomTexture("flowers/orange_bog_asphodel2")
                .randomTexture("flowers/orange_bog_asphodel3")
                .randomTexture("flowers/orange_bog_asphodel4")
                .build();

        registerCustomPlantBlock(bsmg, PlantBlocks.ORANGE_MARIGOLDS)
                .randomTexture("flowers/orange_marigolds1")
                .randomTexture("flowers/orange_marigolds2")
                .randomTexture("flowers/orange_marigolds3")
                .randomTexture("flowers/orange_marigolds4")
                .build();

        registerCustomPlantBlock(bsmg, PlantBlocks.ORANGE_SUN_STAR)
                .randomTexture("flowers/orange_sun_star1")
                .randomTexture("flowers/orange_sun_star2")
                .build();

        registerCustomPlantBlock(bsmg, PlantBlocks.ORANGE_TROLLIUS)
                .randomTexture("flowers/orange_trollius1")
                .randomTexture("flowers/orange_trollius2")
                .build();

        registerCustomPlantBlock(bsmg, PlantBlocks.PINK_ALLIUM)
                .randomTexture("flowers/pink_allium1")
                .randomTexture("flowers/pink_allium2")
                .randomTexture("flowers/pink_allium3")
                .randomTexture("flowers/pink_allium4")
                .build();

        registerCustomPlantBlock(bsmg, PlantBlocks.PINK_PRIMROSE)
                .randomTexture("flowers/pink_primrose1")
                .randomTexture("flowers/pink_primrose2")
                .build();

        registerCustomPlantBlock(bsmg, PlantBlocks.PINK_ROSES)
                .randomTexture("flowers/pink_roses1")
                .randomTexture("flowers/pink_roses2")
                .randomTexture("flowers/pink_roses3")
                .randomTexture("flowers/pink_roses4")
                .build();

        registerCustomPlantBlock(bsmg, PlantBlocks.PINK_SWEET_PEAS)
                .texture("flowers/pink_sweet_peas1")
                .build();

        registerCustomPlantBlock(bsmg, PlantBlocks.PINK_THISTLE)
                .randomTexture("flowers/pink_thistle/side1")
                .randomTexture("flowers/pink_thistle/side2")
                .randomTexture("flowers/pink_thistle/side3")
                .randomTexture("flowers/pink_thistle/side4")
                .build();

        registerCustomPlantBlock(bsmg, PlantBlocks.PINK_TULIPS)
                .randomTexture("flowers/pink_tulips1")
                .randomTexture("flowers/pink_tulips2")
                .randomTexture("flowers/pink_tulips3")
                .randomTexture("flowers/pink_tulips4")
                .build();

        registerCustomPlantBlock(bsmg, PlantBlocks.PINK_WILDFLOWERS)
                .texture("flowers/pink_wildflowers")
                .build();

        registerCustomPlantBlock(bsmg, PlantBlocks.RED_ASTER)
                .randomTexture("flowers/red_aster1")
                .randomTexture("flowers/red_aster2")
                .randomTexture("flowers/red_aster3")
                .randomTexture("flowers/red_aster4")
                .build();

        registerCustomPlantBlock(bsmg, PlantBlocks.RED_CARNATIONS)
                .randomTexture("flowers/red_carnations1")
                .randomTexture("flowers/red_carnations2")
                .randomTexture("flowers/red_carnations3")
                .randomTexture("flowers/red_carnations4")
                .build();

        registerCustomPlantBlock(bsmg, PlantBlocks.RED_CHRYSANTHEMUM)
                .texture("flowers/red_chrysanthemum1")
                .build();

        registerCustomPlantBlock(bsmg, PlantBlocks.RED_DARK_ROSES)
                .randomTexture("flowers/red_dark_roses1")
                .randomTexture("flowers/red_dark_roses2")
                .randomTexture("flowers/red_dark_roses3")
                .randomTexture("flowers/red_dark_roses4")
                .build();

        registerCustomPlantBlock(bsmg, PlantBlocks.RED_FERN)
                .randomTexture("red_fern/side1")
                .randomTexture("red_fern/side2")
                .randomTexture("red_fern/side3")
                .build();

        registerCustomPlantBlock(bsmg, PlantBlocks.RED_FLOWERING_SPINY_HERB)
                .texture("flowers/red_flowering_spiny_herb1")
                .build();

        registerCustomPlantBlock(bsmg, PlantBlocks.RED_MUSHROOM_1).texture("red_mushroom_block/mushroom_red_0").build();
        registerCustomPlantBlock(bsmg, PlantBlocks.RED_MUSHROOM_2).texture("red_mushroom_block/mushroom_red_1").build();
        registerCustomPlantBlock(bsmg, PlantBlocks.RED_MUSHROOM_3).texture("red_mushroom_block/mushroom_red_2").build();
        registerCustomPlantBlock(bsmg, PlantBlocks.RED_MUSHROOM_4).texture("red_mushroom_block/mushroom_red_3").build();
        registerCustomPlantBlock(bsmg, PlantBlocks.RED_MUSHROOM_5).texture("red_mushroom_block/mushroom_red_4").build();
        registerCustomPlantBlock(bsmg, PlantBlocks.RED_MUSHROOM_6).texture("red_mushroom_block/mushroom_red_5").build();
        registerCustomPlantBlock(bsmg, PlantBlocks.RED_MUSHROOM_7).texture("red_mushroom_block/mushroom_red_6").build();
        registerCustomPlantBlock(bsmg, PlantBlocks.RED_MUSHROOM_8).texture("red_mushroom_block/mushroom_red_7").build();
        registerCustomPlantBlock(bsmg, PlantBlocks.RED_MUSHROOM_9).texture("red_mushroom_block/mushroom_red_8").build();

        registerCustomPlantBlock(bsmg, PlantBlocks.RED_POPPIES)
                .randomTexture("flowers/red_poppies1")
                .randomTexture("flowers/red_poppies2")
                .build();

        registerCustomPlantBlock(bsmg, PlantBlocks.RED_ROSES)
                .randomTexture("flowers/red_roses1")
                .randomTexture("flowers/red_roses2")
                .randomTexture("flowers/red_roses3")
                .randomTexture("flowers/red_roses4")
                .build();

        registerCustomPlantBlock(bsmg, PlantBlocks.RED_SORREL)
                .texture("flowers/red_sorrel1")
                .build();

        registerCustomPlantBlock(bsmg, PlantBlocks.RED_SOURLEAF_BUSH)
                .randomTexture("flowers/red_sourleaf_bush1")
                .randomTexture("flowers/red_sourleaf_bush2")
                .build();

        registerCustomPlantBlock(bsmg, PlantBlocks.RED_TULIPS)
                .randomTexture("flowers/red_tulips1")
                .randomTexture("flowers/red_tulips2")
                .randomTexture("flowers/red_tulips3")
                .randomTexture("flowers/red_tulips4")
                .build();

        registerCustomPlantBlock(bsmg, PlantBlocks.STRAWBERRY_BUSH)
                .texture("flowers/strawberry")
                .build();

        registerCustomPlantBlock(bsmg, PlantBlocks.THICK_GRASS)
                .randomTexture("minecraft:block/grass/grass1")
                .randomTexture("minecraft:block/grass/grass2")
                .randomTexture("minecraft:block/grass/grass3")
                .randomTexture("minecraft:block/grass/grass4")
                .randomTexture("minecraft:block/grass/grass5")
                .randomTexture("minecraft:block/grass/grass6")
                .randomTexture("minecraft:block/grass/grass7")
                .randomTexture("minecraft:block/grass/grass8")
                .randomTexture("minecraft:block/grass/grass9")
                .randomTexture("minecraft:block/grass/grass10")
                .build();

        registerCustomPlantBlock(bsmg, PlantBlocks.UNSHADED_GRASS)
                .randomTexture("deadbush/side1")
                .randomTexture("deadbush/side2")
                .randomTexture("deadbush/side3")
                .randomTexture("deadbush/side4")
                .randomTexture("deadbush/side5")
                .randomTexture("deadbush/side6")
                .randomTexture("deadbush/side7")
                .randomTexture("deadbush/side8")
                .build();

        registerCustomPlantBlock(bsmg, PlantBlocks.WHITE_CHAMOMILE)
                .randomTexture("flowers/white_chamomile1")
                .randomTexture("flowers/white_chamomile2")
                .randomTexture("flowers/white_chamomile3")
                .build();

        registerCustomPlantBlock(bsmg, PlantBlocks.WHITE_DAISIES)
                .randomTexture("flowers/white_daisies1")
                .randomTexture("flowers/white_daisies2")
                .randomTexture("flowers/white_daisies3")
                .randomTexture("flowers/white_daisies4")
                .build();

        registerCustomPlantBlock(bsmg, PlantBlocks.WHITE_LILYOFTHEVALLEY)
                .texture("flowers/white_lily_valley1")
                .build();

        registerCustomPlantBlock(bsmg, PlantBlocks.WHITE_PEONY)
                .texture("flowers/white_peony1")
                .build();

        registerCustomPlantBlock(bsmg, PlantBlocks.WHITE_ROSES)
                .randomTexture("flowers/white_roses1")
                .randomTexture("flowers/white_roses2")
                .randomTexture("flowers/white_roses3")
                .randomTexture("flowers/white_roses4")
                .build();

        registerCustomPlantBlock(bsmg, PlantBlocks.YELLOW_BEDSTRAW)
                .randomTexture("flowers/yellow_bedstraw/side1")
                .randomTexture("flowers/yellow_bedstraw/side2")
                .randomTexture("flowers/yellow_bedstraw/side3")
                .build();

        registerCustomPlantBlock(bsmg, PlantBlocks.YELLOW_BELLS)
                .texture("flowers/yellow_bells1")
                .build();

        registerCustomPlantBlock(bsmg, PlantBlocks.YELLOW_BUTTERCUPS)
                .randomTexture("flowers/yellow_buttercups1")
                .randomTexture("flowers/yellow_buttercups2")
                .randomTexture("flowers/yellow_buttercups3")
                .randomTexture("flowers/yellow_buttercups4")
                .build();

        registerCustomPlantBlock(bsmg, PlantBlocks.YELLOW_DAFFODILS)
                .randomTexture("flowers/yellow_daffodils1")
                .randomTexture("flowers/yellow_daffodils2")
                .randomTexture("flowers/yellow_daffodils3")
                .randomTexture("flowers/yellow_daffodils4")
                .build();

        registerCustomPlantBlock(bsmg, PlantBlocks.YELLOW_DAISIES)
                .randomTexture("flowers/yellow_daisies1")
                .randomTexture("flowers/yellow_daisies2")
                .build();

        registerCustomPlantBlock(bsmg, PlantBlocks.YELLOW_DANDELIONS)
                .randomTexture("flowers/yellow_dandelions1")
                .randomTexture("flowers/yellow_dandelions2")
                .randomTexture("flowers/yellow_dandelions3")
                .build();

        registerCustomPlantBlock(bsmg, PlantBlocks.YELLOW_HELLEBORE)
                .randomTexture("flowers/yellow_hellebore1")
                .randomTexture("flowers/yellow_hellebore2")
                .build();

        registerCustomPlantBlock(bsmg, PlantBlocks.YELLOW_LUPINE)
                .randomTexture("flowers/yellow_lupine1")
                .randomTexture("flowers/yellow_lupine2")
                .randomTexture("flowers/yellow_lupine3")
                .randomTexture("flowers/yellow_lupine4")
                .build();

        registerCustomPlantBlock(bsmg, PlantBlocks.YELLOW_ROSES)
                .randomTexture("flowers/yellow_roses1")
                .randomTexture("flowers/yellow_roses2")
                .randomTexture("flowers/yellow_roses3")
                .randomTexture("flowers/yellow_roses4")
                .build();

        registerCustomPlantBlock(bsmg, PlantBlocks.YELLOW_RUDBECKIA)
                .texture("flowers/yellow_rudbeckia1")
                .build();

        registerCustomPlantBlock(bsmg, PlantBlocks.YELLOW_SUNFLOWER)
                .randomTexture("flowers/yellow_sunflower/side1")
                .randomTexture("flowers/yellow_sunflower/side2")
                .randomTexture("flowers/yellow_sunflower/side3")
                .build();

        registerCustomPlantBlock(bsmg, PlantBlocks.YELLOW_TANSY)
                .texture("flowers/yellow_tansy")
                .build();

        registerCustomPlantBlock(bsmg, PlantBlocks.YELLOW_WILDFLOWERS)
                .texture("flowers/yellow_wildflowers")
                .build();

        // Flowerbed Blocks
        registerCustomFlowerbedBlock(bsmg, ModBlocks.CLOVER)
                .stemTexture("flowerbed/clover_stem")
                .flowerTexture("flowerbed/clover")
                .build();

        // Web Blocks
        registerCustomCrossBlock(bsmg, ModBlocks.BEES)
                .texture("web_block/bug_bees")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.ALYSSAS_TEARS_MIST_ONE)
                .texture("alyssas_tears_mist/mist1")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.ALYSSAS_TEARS_MIST_TWO)
                .texture("alyssas_tears_mist/mist2")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.ALYSSAS_TEARS_MIST_THREE)
                .texture("alyssas_tears_mist/mist3")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.ALYSSAS_TEARS_MIST_FOUR)
                .texture("alyssas_tears_mist/mist4")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.BLACK_BRICICLE)
                .texture("ashlar_melted/black/bricicle/side")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.BUSHEL_OF_HERBS)
                .texture("web_block/food_herbs")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.BUSHEL_OF_SOURLEAF)
                .texture("web_block/food_sourleaf")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.BUTTERFLY_BLUE)
                .texture("web_block/bug_butterfly_blue")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.BUTTERFLY_ORANGE)
                .texture("web_block/bug_butterfly_orange")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.BUTTERFLY_RED)
                .texture("web_block/bug_butterfly_red")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.BUTTERFLY_WHITE)
                .texture("web_block/bug_butterfly_white")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.BUTTERFLY_YELLOW)
                .texture("web_block/bug_butterfly_yellow")
                .build();
        registerCustomCrossBlock(bsmg, PlantBlocks.CATTAILS)
                .randomTexture("cattails/side1")
                .randomTexture("cattails/side2")
                .randomTexture("cattails/side3")
                .randomTexture("cattails/side4")
                .randomTexture("cattails/side5")
                .randomTexture("cattails/side6")
                .randomTexture("cattails/side7")
                .randomTexture("cattails/side8")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.CHAIN_BLOCK_HARNESS)
                .texture("web_block/chain_blockharness")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.CHILI_RISTRA)
                .texture("web_block/food_chili_ristra")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.COBWEB)
                .randomTexture("cobweb/side1")
                .randomTexture("cobweb/side2")
                .randomTexture("cobweb/side3")
                .randomTexture("cobweb/side4")
                .randomTexture("cobweb/side5")
                .randomTexture("cobweb/side6")
                .randomTexture("cobweb/side7")
                .randomTexture("cobweb/side8")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.DEAD_FISH)
                .randomTexture("dead_fish/fish_dead1")
                .randomTexture("dead_fish/fish_dead2")
                .randomTexture("dead_fish/fish_dead3")
                .randomTexture("dead_fish/fish_dead4")
                .randomTexture("dead_fish/fish_dead5")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.DEAD_FOWL)
                .randomTexture("dead_fowl/fowl")
                .randomTexture("dead_fowl/fowl2")
                .randomTexture("dead_fowl/gooseplains")
                .randomTexture("dead_fowl/gooseplains2")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.DEAD_FROG)
                .randomTexture("dead_frog/toad_dead1")
                .randomTexture("dead_frog/toad_dead2")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.DEAD_HARE)
                .randomTexture("dead_hare/rabbit_dead1")
                .randomTexture("dead_hare/rabbit_dead2")
                .randomTexture("dead_hare/rabbit_dead3")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.DEAD_JUNGLE_TALL_GRASS)
                .texture("dead_jungle_tall_grass/down_side1")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.DEAD_RAT)
                .randomTexture("dead_rat/rat1")
                .randomTexture("dead_rat/rat2")
                .randomTexture("dead_rat/rat3")
                .randomTexture("dead_rat/rat4")
                .randomTexture("dead_rat/rat5")
                .randomTexture("dead_rat/rat6")
                .randomTexture("dead_rat/rat7")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.DEAD_SAVANNA_TALL_GRASS)
                .texture("dead_savanna_tall_grass/dead_savanna_tall_grass")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.DRAGONFLY)
                .texture("web_block/bug_dragonfly")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.FLIES)
                .texture("web_block/bug_flies")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.GARLIC_STRAND)
                .texture("web_block/food_garlic_strand")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.ICICLE)
                .texture("icicle/side")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.IRON_THRONE_RANDOM_BLADES)
                .randomTexture("blades_random/side1")
                .randomTexture("blades_random/side2")
                .randomTexture("blades_random/side3")
                .randomTexture("blades_random/side4")
                .randomTexture("blades_random/side5")
                .randomTexture("blades_random/side6")
                .randomTexture("blades_random/side7")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.JUNGLE_TALL_FERN)
                .texture("jungle_tall_fern/side")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.JUNGLE_TALL_GRASS)
                .texture("jungle_tall_grass/down_side1")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.ROPE_BLOCK_HARNESS)
                .texture("web_block/rope_blockharness")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.SAUSAGES_LEG_OF_HAM)
                .texture("sausages_leg_of_ham/default")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.SAVANNA_TALL_GRASS)
                .texture("savanna_tall_grass/savanna_tall_grass")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.SMOKE)
                .state("smoke/smoke1")
                .state("smoke/smoke2")
                .state("smoke/smoke3")
                .state("smoke/smoke4")
                .state("smoke/smoke5")
                .state("smoke/smoke6")
                .state("smoke/smoke7")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.VERTICAL_CHAIN)
                .texture("web_block/chain_vertical")
                .build();
        registerCustomCrossBlock(bsmg, ModBlocks.VERTICAL_ROPE)
                .texture("web_block/rope_vertical")
                .build();

        // Crop Blocks
        CropBlockDatagen
                .generateCropBlock(bsmg, ModBlocks.CROP_CARROTS, "crop_carrots")
                .addState("age0", "carrots/carrots_stage_0")
                .addState("age1", "carrots/carrots_stage_1")
                .addState("age2", "carrots/carrots_stage_2")
                .addState("age3", "carrots/carrots_stage_3")
                .build();

        CropBlockDatagen
                .generateCropBlock(bsmg, ModBlocks.CROP_TURNIPS, "crop_turnips")
                .addState("age0", "turnips/turnips_stage_0")
                .addState("age1", "turnips/turnips_stage_1")
                .addState("age2", "turnips/turnips_stage_2")
                .addState("age3", "turnips/turnips_stage_3")
                .isLayerSensitive()
                .build();

        CropBlockDatagen
                .generateCropBlock(bsmg, ModBlocks.CROP_PEAS, "crop_peas")
                .addState("age0", "peas/peas_stage_0")
                .addState("age1", "peas/peas_stage_1")
                .addState("age2", "peas/peas_stage_2")
                .isLayerSensitive()
                .build();

        CropBlockDatagen
                .generateCropBlock(bsmg, ModBlocks.CANDLE_ALTAR, "candle_altar")
                .addStateRandomTextures("lit", "lighting/candle_altar/lit1", "lighting/candle_altar/lit2", "lighting/candle_altar/lit3")
                .addStateRandomTextures("unlit", "lighting/candle_altar/unlit1", "lighting/candle_altar/unlit2", "lighting/candle_altar/unlit3")
                .build();

        CropBlockDatagen
                .generateCropBlock(bsmg, ModBlocks.CROP_WHEAT, "crop_wheat")
                .isLayerSensitive()
                .addState("age0", "wheat/wheat_stage_0")
                .addState("age1", "wheat/wheat_stage_1")
                .addState("age2", "wheat/wheat_stage_2")
                .addState("age3", "wheat/wheat_stage_3")
                .addStateRandomTextures("age4", "wheat/stage4_1", "wheat/stage4_2")
                .addStateRandomTextures("age5", "wheat/stage5_1", "wheat/stage5_2")
                .addStateRandomTextures("age6", "wheat/stage6_1", "wheat/stage6_2")
                .addStateRandomTextures("age7", "wheat/stage7_1", "wheat/stage7_2", "wheat/stage7_3", "wheat/stage7_4")
                .build();

        CropBlockDatagen
                .generateCropBlock(bsmg, ModBlocks.SEAGRASS, "seagrass")
                .addRandomTexture("seagrass/side1")
                .addRandomTexture("seagrass/side2")
                .addRandomTexture("seagrass/side3")
                .addRandomTexture("seagrass/side4")
                .isLayerSensitive()
                .build();

        // Bed Blocks
        BedBlockDatagen.generateBedBlock(bsmg, ModBlocks.ITCHY_STRAW_BED, "itchy_straw_bed")
                .bedType("normal")
                .texture("bed_block/bed_straw_itchy_0")
                .texture("bed_block/bed_straw_itchy_1")
                .texture("bed_block/bed_straw_itchy_2")
                .texture("bed_block/bed_straw_itchy_3")
                .texture("bed_block/bed_straw_itchy_4")
                .texture("bed_block/bed_straw_itchy_5")
                .build();

        BedBlockDatagen.generateBedBlock(bsmg, ModBlocks.HAMMOCK, "hammock")
                .bedType("hammock")
                .texture("bed_block/bed_hammock_0")
                .texture("bed_block/bed_hammock_1")
                .texture("bed_block/bed_hammock_2")
                .texture("bed_block/bed_hammock_3")
                .texture("bed_block/bed_hammock_4")
                .texture("bed_block/bed_hammock_5")
                .build();

        BedBlockDatagen.generateBedBlock(bsmg, ModBlocks.NIGHTS_WATCH_BED, "nights_watch_bed")
                .bedType("normal")
                .texture("bed_block/bed_night_watch_0")
                .texture("bed_block/bed_night_watch_1")
                .texture("bed_block/bed_night_watch_2")
                .texture("bed_block/bed_night_watch_3")
                .texture("bed_block/bed_night_watch_4")
                .texture("bed_block/bed_night_watch_5")
                .build();

        BedBlockDatagen.generateBedBlock(bsmg, ModBlocks.NOBLE_BLUE_BED, "noble_blue_bed")
                .bedType("raised")
                .texture("bed_block/bed_noble_blue_0")
                .texture("bed_block/bed_noble_blue_1")
                .texture("bed_block/bed_noble_blue_2")
                .texture("bed_block/bed_noble_blue_3")
                .texture("bed_block/bed_noble_blue_4")
                .texture("bed_block/bed_noble_blue_5")
                .build();

        BedBlockDatagen.generateBedBlock(bsmg, ModBlocks.NOBLE_RED_BED, "noble_red_bed")
                .bedType("raised")
                .texture("bed_block/bed_noble_red_0")
                .texture("bed_block/bed_noble_red_1")
                .texture("bed_block/bed_noble_red_2")
                .texture("bed_block/bed_noble_red_3")
                .texture("bed_block/bed_noble_red_4")
                .texture("bed_block/bed_noble_red_5")
                .build();

        BedBlockDatagen.generateBedBlock(bsmg, ModBlocks.NORTHERN_BED, "northern_bed")
                .bedType("normal")
                .texture("bed_block/bed_north_0")
                .texture("bed_block/bed_north_1")
                .texture("bed_block/bed_north_2")
                .texture("bed_block/bed_north_3")
                .texture("bed_block/bed_north_4")
                .texture("bed_block/bed_north_5")
                .build();

        BedBlockDatagen.generateBedBlock(bsmg, ModBlocks.PALE_GREEN_BED, "pale_green_bed")
                .bedType("normal")
                .texture("bed_block/bed_patchy_green_0")
                .texture("bed_block/bed_patchy_green_1")
                .texture("bed_block/bed_patchy_green_2")
                .texture("bed_block/bed_patchy_green_3")
                .texture("bed_block/bed_patchy_green_4")
                .texture("bed_block/bed_patchy_green_5")
                .build();

        BedBlockDatagen.generateBedBlock(bsmg, ModBlocks.PALE_RED_BED, "pale_red_bed")
                .bedType("normal")
                .texture("bed_block/bed_patchy_red_0")
                .texture("bed_block/bed_patchy_red_1")
                .texture("bed_block/bed_patchy_red_2")
                .texture("bed_block/bed_patchy_red_3")
                .texture("bed_block/bed_patchy_red_4")
                .texture("bed_block/bed_patchy_red_5")
                .build();

        BedBlockDatagen.generateBedBlock(bsmg, ModBlocks.STRAW_BED, "straw_bed")
                .bedType("normal")
                .texture("bed_block/bed_straw_0")
                .texture("bed_block/bed_straw_1")
                .texture("bed_block/bed_straw_2")
                .texture("bed_block/bed_straw_3")
                .texture("bed_block/bed_straw_4")
                .texture("bed_block/bed_straw_5")
                .build();

        LeavesBlockDatagen.generateLeavesBlock(bsmg, ModBlocks.APPLE_FRUIT_LEAVES, "apple_fruit_leaves")
                .betterFoliage()
                .hasOverlay()
                .rotateRandom()
                .isTinted()
                .addRandomTextureSet(10, "leaves/birch/all", "transparent", "leaves/overlay/apple0")
                .addRandomTextureSet(10, "leaves/birch/all", "transparent", "leaves/overlay/apple1")
                .addRandomTextureSet(10, "leaves/birch/all", "transparent", "leaves/overlay/apple2")
                .addRandomTextureSet(2, "leaves/birch/all", "transparent", "leaves/overlay/apple3")
                .build();

        LeavesBlockDatagen.generateLeavesBlock(bsmg, ModBlocks.APRICOT_FRUIT_LEAVES, "apricot_fruit_leaves")
                .betterFoliage()
                .hasOverlay()
                .rotateRandom()
                .isTinted()
                .addRandomTextureSet(10, "leaves/oak/all", "transparent", "leaves/overlay/apricot0")
                .addRandomTextureSet(10, "leaves/oak/all", "transparent", "leaves/overlay/apricot1")
                .addRandomTextureSet(10, "leaves/oak/all", "transparent", "leaves/overlay/apricot2")
                .addRandomTextureSet(2, "leaves/oak/all", "transparent", "leaves/overlay/apricot3")
                .build();

        LeavesBlockDatagen.generateLeavesBlock(bsmg, ModBlocks.BLACKBERRY_BUSH, "blackberry_bush")
                .betterFoliage()
                .hasOverlay()
                .rotateRandom()
                .isTinted()
                .addRandomTextureSet(10, "leaves/birch/all", "transparent", "leaves/overlay/blackberry0")
                .addRandomTextureSet(10, "leaves/birch/all", "transparent", "leaves/overlay/blackberry1")
                .addRandomTextureSet(10, "leaves/birch/all", "transparent", "leaves/overlay/blackberry2")
                .addRandomTextureSet(2, "leaves/birch/all", "transparent", "leaves/overlay/blackberry3")
                .build();

        LeavesBlockDatagen.generateLeavesBlock(bsmg, ModBlocks.BLUEBERRY_BUSH, "blueberry_bush")
                .betterFoliage()
                .hasOverlay()
                .rotateRandom()
                .isTinted()
                .addRandomTextureSet(10, "leaves/oak/all", "transparent", "leaves/overlay/blueberry0")
                .addRandomTextureSet(10, "leaves/oak/all", "transparent", "leaves/overlay/blueberry1")
                .addRandomTextureSet(10, "leaves/oak/all", "transparent", "leaves/overlay/blueberry2")
                .addRandomTextureSet(2, "leaves/oak/all", "transparent", "leaves/overlay/blueberry3")
                .build();

        LeavesBlockDatagen.generateLeavesBlock(bsmg, ModBlocks.CHERRY_FRUIT_LEAVES, "cherry_fruit_leaves")
                .betterFoliage()
                .hasOverlay()
                .rotateRandom()
                .isTinted()
                .addRandomTextureSet(10, "leaves/oak/all", "transparent", "leaves/overlay/cherry0")
                .addRandomTextureSet(10, "leaves/oak/all", "transparent", "leaves/overlay/cherry1")
                .addRandomTextureSet(10, "leaves/oak/all", "transparent", "leaves/overlay/cherry2")
                .addRandomTextureSet(2, "leaves/oak/all", "transparent", "leaves/overlay/cherry3")
                .build();

        LeavesBlockDatagen.generateLeavesBlock(bsmg, ModBlocks.HOP_FRUIT_LEAVES, "hop_fruit_leaves")
                .betterFoliage()
                .hasOverlay()
                .rotateRandom()
                .isTinted()
                .addRandomTextureSet(10, "leaves/jungle/all", "transparent", "leaves/overlay/hop0")
                .addRandomTextureSet(10, "leaves/jungle/all", "transparent", "leaves/overlay/hop1")
                .addRandomTextureSet(10, "leaves/jungle/all", "transparent", "leaves/overlay/hop2")
                .addRandomTextureSet(2, "leaves/jungle/all", "transparent", "leaves/overlay/hop3")
                .build();

        LeavesBlockDatagen.generateLeavesBlock(bsmg, ModBlocks.JUNIPER_BUSH, "juniper_bush")
                .betterFoliage()
                .hasOverlay()
                .rotateRandom()
                .isTinted()
                .addRandomTextureSet(10, "leaves/spruce/all", "transparent", "leaves/overlay/juniper0")
                .addRandomTextureSet(10, "leaves/spruce/all", "transparent", "leaves/overlay/juniper1")
                .addRandomTextureSet(10, "leaves/spruce/all", "transparent", "leaves/overlay/juniper2")
                .addRandomTextureSet(2, "leaves/spruce/all", "transparent", "leaves/overlay/juniper3")
                .build();

        LeavesBlockDatagen.generateLeavesBlock(bsmg, ModBlocks.LEMON_FRUIT_LEAVES, "lemon_fruit_leaves")
                .betterFoliage()
                .hasOverlay()
                .rotateRandom()
                .isTinted()
                .addRandomTextureSet(10, "leaves/oak/all", "transparent", "leaves/overlay/lemon0")
                .addRandomTextureSet(10, "leaves/oak/all", "transparent", "leaves/overlay/lemon1")
                .addRandomTextureSet(10, "leaves/oak/all", "transparent", "leaves/overlay/lemon2")
                .addRandomTextureSet(2, "leaves/oak/all", "transparent", "leaves/overlay/lemon3")
                .build();

        LeavesBlockDatagen.generateLeavesBlock(bsmg, ModBlocks.LIME_FRUIT_LEAVES, "lime_fruit_leaves")
                .betterFoliage()
                .hasOverlay()
                .rotateRandom()
                .isTinted()
                .addRandomTextureSet(10, "leaves/jungle/all", "transparent", "leaves/overlay/lime0")
                .addRandomTextureSet(10, "leaves/jungle/all", "transparent", "leaves/overlay/lime1")
                .addRandomTextureSet(10, "leaves/jungle/all", "transparent", "leaves/overlay/lime2")
                .addRandomTextureSet(2, "leaves/jungle/all", "transparent", "leaves/overlay/lime3")
                .build();

        LeavesBlockDatagen.generateLeavesBlock(bsmg, ModBlocks.OLIVE_FRUIT_LEAVES, "olive_fruit_leaves")
                .betterFoliage()
                .hasOverlay()
                .rotateRandom()
                .isTinted()
                .addRandomTextureSet(10, "leaves/oak/all", "transparent", "leaves/overlay/olive0")
                .addRandomTextureSet(10, "leaves/oak/all", "transparent", "leaves/overlay/olive1")
                .addRandomTextureSet(10, "leaves/oak/all", "transparent", "leaves/overlay/olive2")
                .addRandomTextureSet(2, "leaves/oak/all", "transparent", "leaves/overlay/olive3")
                .build();

        LeavesBlockDatagen.generateLeavesBlock(bsmg, ModBlocks.ORANGE_FRUIT_LEAVES, "orange_fruit_leaves")
                .betterFoliage()
                .hasOverlay()
                .rotateRandom()
                .isTinted()
                .addRandomTextureSet(10, "leaves/jungle/all", "transparent", "leaves/overlay/orange0")
                .addRandomTextureSet(10, "leaves/jungle/all", "transparent", "leaves/overlay/orange1")
                .addRandomTextureSet(10, "leaves/jungle/all", "transparent", "leaves/overlay/orange2")
                .addRandomTextureSet(2, "leaves/jungle/all", "transparent", "leaves/overlay/orange3")
                .build();

        LeavesBlockDatagen.generateLeavesBlock(bsmg, ModBlocks.PALM_LEAVES, "palm_leaves")
                .betterFoliage()
                .rotateRandom()
                .isTinted()
                .textures("leaves/palm/all")
                .build();

        LeavesBlockDatagen.generateLeavesBlock(bsmg, ModBlocks.PEACH_FRUIT_LEAVES, "peach_fruit_leaves")
                .betterFoliage()
                .hasOverlay()
                .rotateRandom()
                .isTinted()
                .addRandomTextureSet(10, "leaves/birch/all", "transparent", "leaves/overlay/peach0")
                .addRandomTextureSet(10, "leaves/birch/all", "transparent", "leaves/overlay/peach1")
                .addRandomTextureSet(10, "leaves/birch/all", "transparent", "leaves/overlay/peach2")
                .addRandomTextureSet(2, "leaves/birch/all", "transparent", "leaves/overlay/peach3")
                .build();

        LeavesBlockDatagen.generateLeavesBlock(bsmg, ModBlocks.PINK_ROSE_BUSH, "pink_rose_bush")
                .betterFoliage()
                .hasOverlay()
                .rotateRandom()
                .isTinted()
                .addRandomTextureSet(10, "leaves/oak/all", "transparent", "leaves/overlay/pink_rose0")
                .addRandomTextureSet(10, "leaves/oak/all", "transparent", "leaves/overlay/pink_rose1")
                .addRandomTextureSet(10, "leaves/oak/all", "transparent", "leaves/overlay/pink_rose2")
                .addRandomTextureSet(2, "leaves/oak/all", "transparent", "leaves/overlay/pink_rose3")
                .build();

        LeavesBlockDatagen.generateLeavesBlock(bsmg, ModBlocks.PLUM_FRUIT_LEAVES, "plum_fruit_leaves")
                .betterFoliage()
                .hasOverlay()
                .rotateRandom()
                .isTinted()
                .addRandomTextureSet(10, "leaves/oak/all", "transparent", "leaves/overlay/plum0")
                .addRandomTextureSet(10, "leaves/oak/all", "transparent", "leaves/overlay/plum1")
                .addRandomTextureSet(10, "leaves/oak/all", "transparent", "leaves/overlay/plum2")
                .addRandomTextureSet(2, "leaves/oak/all", "transparent", "leaves/overlay/plum3")
                .build();

        LeavesBlockDatagen.generateLeavesBlock(bsmg, ModBlocks.POMEGRANATE_FRUIT_LEAVES, "pomegranate_fruit_leaves")
                .betterFoliage()
                .hasOverlay()
                .rotateRandom()
                .isTinted()
                .addRandomTextureSet(10, "leaves/birch/all", "transparent", "leaves/overlay/pomegranate0")
                .addRandomTextureSet(10, "leaves/birch/all", "transparent", "leaves/overlay/pomegranate1")
                .addRandomTextureSet(10, "leaves/birch/all", "transparent", "leaves/overlay/pomegranate2")
                .addRandomTextureSet(2, "leaves/birch/all", "transparent", "leaves/overlay/pomegranate3")
                .build();

        LeavesBlockDatagen.generateLeavesBlock(bsmg, ModBlocks.PURPLE_GRAPE_FRUIT_LEAVES, "purple_grape_fruit_leaves")
                .betterFoliage()
                .hasOverlay()
                .rotateRandom()
                .isTinted()
                .addRandomTextureSet(10, "leaves/jungle/all", "transparent", "leaves/overlay/grapes_purple0")
                .addRandomTextureSet(10, "leaves/jungle/all", "transparent", "leaves/overlay/grapes_purple1")
                .addRandomTextureSet(10, "leaves/jungle/all", "transparent", "leaves/overlay/grapes_purple2")
                .addRandomTextureSet(2, "leaves/jungle/all", "transparent", "leaves/overlay/grapes_purple3")
                .build();

        LeavesBlockDatagen.generateLeavesBlock(bsmg, ModBlocks.RASPBERRY_BUSH, "raspberry_bush")
                .betterFoliage()
                .hasOverlay()
                .rotateRandom()
                .isTinted()
                .addRandomTextureSet(10, "leaves/jungle/all", "transparent", "leaves/overlay/raspberry0")
                .addRandomTextureSet(10, "leaves/jungle/all", "transparent", "leaves/overlay/raspberry1")
                .addRandomTextureSet(10, "leaves/jungle/all", "transparent", "leaves/overlay/raspberry2")
                .addRandomTextureSet(2, "leaves/jungle/all", "transparent", "leaves/overlay/raspberry3")
                .build();

        LeavesBlockDatagen.generateLeavesBlock(bsmg, ModBlocks.RED_ROSE_BUSH, "red_rose_bush")
                .betterFoliage()
                .hasOverlay()
                .rotateRandom()
                .isTinted()
                .addRandomTextureSet(10, "leaves/oak/all", "transparent", "leaves/overlay/red_rose0")
                .addRandomTextureSet(10, "leaves/oak/all", "transparent", "leaves/overlay/red_rose1")
                .addRandomTextureSet(10, "leaves/oak/all", "transparent", "leaves/overlay/red_rose2")
                .addRandomTextureSet(2, "leaves/oak/all", "transparent", "leaves/overlay/red_rose3")
                .build();

        LeavesBlockDatagen.generateLeavesBlock(bsmg, ModBlocks.SNOWY_SPRUCE_LEAVES, "snowy_spruce_leaves")
                .betterFoliage()
                .rotateRandom()
                .textures("leaves/spruce/snowy/all")
                .build();

        LeavesBlockDatagen.generateLeavesBlock(bsmg, ModBlocks.SNOWY_WEIRWOOD_LEAVES, "snowy_weirwood_leaves")
                .betterFoliage()
                .rotateRandom()
                .textures("leaves/weirwood/snowy/all")
                .build();

        LeavesBlockDatagen.generateLeavesBlock(bsmg, ModBlocks.WEIRWOOD_LEAVES, "weirwood_leaves")
                .betterFoliage()
                .rotateRandom()
                .textures("leaves/weirwood/all")
                .build();

        LeavesBlockDatagen.generateLeavesBlock(bsmg, ModBlocks.WHITE_GRAPE_FRUIT_LEAVES, "white_grape_fruit_leaves")
                .betterFoliage()
                .hasOverlay()
                .rotateRandom()
                .isTinted()
                .addRandomTextureSet(10, "leaves/jungle/all", "transparent", "leaves/overlay/grapes_white0")
                .addRandomTextureSet(10, "leaves/jungle/all", "transparent", "leaves/overlay/grapes_white1")
                .addRandomTextureSet(10, "leaves/jungle/all", "transparent", "leaves/overlay/grapes_white2")
                .addRandomTextureSet(2, "leaves/jungle/all", "transparent", "leaves/overlay/grapes_white3")
                .build();

        LeavesBlockDatagen.generateLeavesBlock(bsmg, ModBlocks.WHITE_ROSE_BUSH, "white_rose_bush")
                .betterFoliage()
                .hasOverlay()
                .rotateRandom()
                .isTinted()
                .addRandomTextureSet(10, "leaves/oak/all", "transparent", "leaves/overlay/white_rose0")
                .addRandomTextureSet(10, "leaves/oak/all", "transparent", "leaves/overlay/white_rose1")
                .addRandomTextureSet(10, "leaves/oak/all", "transparent", "leaves/overlay/white_rose2")
                .addRandomTextureSet(2, "leaves/oak/all", "transparent", "leaves/overlay/white_rose3")
                .build();

        LeavesBlockDatagen.generateLeavesBlock(bsmg, ModBlocks.YELLOW_ROSE_BUSH, "yellow_rose_bush")
                .betterFoliage()
                .hasOverlay()
                .rotateRandom()
                .isTinted()
                .addRandomTextureSet(10, "leaves/oak/all", "transparent", "leaves/overlay/yellow_rose0")
                .addRandomTextureSet(10, "leaves/oak/all", "transparent", "leaves/overlay/yellow_rose1")
                .addRandomTextureSet(10, "leaves/oak/all", "transparent", "leaves/overlay/yellow_rose2")
                .addRandomTextureSet(2, "leaves/oak/all", "transparent", "leaves/overlay/yellow_rose3")
                .build();

        VinesBlockDatagen.generateVinesBlock(bsmg, ModBlocks.DAPPLED_MOSS, "dappled_moss")
                .isTinted()
                .textures("dappled_moss/dappled", "dappled_moss/dappled")
                .build();

        VinesBlockDatagen.generateVinesBlock(bsmg, ModBlocks.FALLING_WATER_BLOCK_ONE, "falling_water_block_one")
                .textures("alyssas_tears_mist/mist1", "alyssas_tears_mist/mist1")
                .isTinted()
                .build();

        VinesBlockDatagen.generateVinesBlock(bsmg, ModBlocks.FALLING_WATER_BLOCK_TWO, "falling_water_block_two")
                .textures("alyssas_tears_mist/mist2", "alyssas_tears_mist/mist2")
                .isTinted()
                .build();

        VinesBlockDatagen.generateVinesBlock(bsmg, ModBlocks.FALLING_WATER_BLOCK_THREE, "falling_water_block_three")
                .textures("alyssas_tears_mist/mist3", "alyssas_tears_mist/mist3")
                .isTinted()
                .build();

        VinesBlockDatagen.generateVinesBlock(bsmg, ModBlocks.FALLING_WATER_BLOCK_FOUR, "falling_water_block_four")
                .textures("alyssas_tears_mist/mist4", "alyssas_tears_mist/mist4")
                .isTinted()
                .build();

        VinesBlockDatagen.generateVinesBlock(bsmg, ModBlocks.JASMINE_VINES, "jasmine_vines")
                .addRandomTextureSet(1, "jasmine_vines/side1", "jasmine_vines/side1")
                .addRandomTextureSet(1, "jasmine_vines/side2", "jasmine_vines/side2")
                .addRandomTextureSet(1, "jasmine_vines/side3", "jasmine_vines/side3")
                .addRandomTextureSet(1, "jasmine_vines/side4", "jasmine_vines/side4")
                .addRandomTextureSet(1, "jasmine_vines/side5", "jasmine_vines/side5")
                .isTinted()
                .build();

        VinesBlockDatagen.generateVinesBlock(bsmg, ModBlocks.VINES, "vines")
                .addRandomTextureSet(1, "vines/side1", "vines/side1")
                .addRandomTextureSet(1, "vines/side2", "vines/side2")
                .addRandomTextureSet(1, "vines/side3", "vines/side3")
                .addRandomTextureSet(1, "vines/side4", "vines/side4")
                .addRandomTextureSet(1, "vines/side5", "vines/side5")
                .isTinted()
                .build();

        // ladder blocks
        LadderBlockDatagen.generateLadderBlock(bsmg, ModBlocks.IRON_RUNGS, "iron_rungs")
                .isCustom()
                .texture("iron_rungs/ladder")
                .build();

        LadderBlockDatagen.generateLadderBlock(bsmg, ModBlocks.IRON_RUNGS_BROKEN, "iron_rungs_broken")
                .isCustom()
                .addRandomTextureSet("")
                .addRandomTextureSet("")
                .addRandomTextureSet("")
                .addRandomTextureSet("")
                .addRandomTextureSet("")
                .addRandomTextureSet("")
                .build();

        LadderBlockDatagen.generateLadderBlock(bsmg, ModBlocks.ROPE_LADDER, "rope_ladder")
                .texture("rope_ladder/side")
                .build();

        // TODO: eventually remove this block
        LadderBlockDatagen.generateLadderBlock(bsmg, ModBlocks.VINE_JASMINE, "vine_jasmine")
                .addRandomTextureSet("jasmine_vines/side1")
                .addRandomTextureSet("jasmine_vines/side2")
                .addRandomTextureSet("jasmine_vines/side3")
                .addRandomTextureSet("jasmine_vines/side4")
                .addRandomTextureSet("jasmine_vines/side5")
                .build();

        LadderBlockDatagen.generateLadderBlock(bsmg, ModBlocks.WINTERFELL_STONE_LADDER, "winterfell_stone_ladder")
                .addRandomTextureSet("winterfell_stone_ladder/side1")
                .addRandomTextureSet("winterfell_stone_ladder/side2")
                .build();

        LadderBlockDatagen.generateLadderBlock(bsmg, ModBlocks.WOOD_LADDER, "wood_ladder")
                .isCustom()
                .texture("wood_ladder/side")
                .build();

        // Flower pot blocks
        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_BLUE_BELLS, "potted_blue_bells")
                .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/blue_bells")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_BLUE_SWAMP_BELLS, "potted_blue_swamp_bells")
                .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/blue_swamp_bells1")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_BROWN_MUSHROOM_13, "potted_brown_mushroom_13")
                .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "brown_mushroom_block/mushroom_brown_12")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_BROWN_MUSHROOM_1, "potted_brown_mushroom_1")
                .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "brown_mushroom_block/mushroom_brown_0")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_BROWN_MUSHROOM_3, "potted_brown_mushroom_3")
                .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "brown_mushroom_block/mushroom_brown_2")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_BROWN_MUSHROOM_6, "potted_brown_mushroom_6")
                .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "brown_mushroom_block/mushroom_brown_5")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_DEAD_SCRUB_GRASS, "potted_dead_scrub_grass")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/dead_scrub_grass1")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/dead_scrub_grass2")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/dead_scrub_grass3")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/dead_scrub_grass4")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/dead_scrub_grass5")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/dead_scrub_grass6")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/dead_scrub_grass7")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/dead_scrub_grass8")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/dead_scrub_grass9")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_FIREWEED, "potted_fireweed")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "fireweed/side1")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "fireweed/side2")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "fireweed/side3")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "fireweed/side4")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_GREEN_LEAFY_HERB, "potted_green_leafy_herb")
                .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/green_leafy_herb")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_GREEN_SCRUB_GRASS, "potted_green_scrub_grass")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/green_scrub_grass1")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/green_scrub_grass2")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/green_scrub_grass3")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/green_scrub_grass4")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/green_scrub_grass5")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/green_scrub_grass6")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_GREEN_SPINY_HERB, "potted_green_spiny_herb")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/green_spiny_herb1")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/green_spiny_herb2")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/green_spiny_herb3")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/green_spiny_herb4")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/green_spiny_herb5")

                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_HEATHER, "potted_heather")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "heather/side1")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "heather/side2")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "heather/side3")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "heather/side4")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_LADY_FERN, "potted_lady_fern")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "lady_fern/side1")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "lady_fern/side2")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "lady_fern/side3")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "lady_fern/side4")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_MAGENTA_ROSES, "potted_magenta_roses")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/magenta_roses1")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/magenta_roses2")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/magenta_roses3")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/magenta_roses4")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_MEADOW_FESCUE, "potted_meadow_fescue")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/meadow_fescue/side1")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/meadow_fescue/side2")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_NETTLE, "potted_nettle")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "nettle/side1")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "nettle/side2")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "nettle/side3")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_ORANGE_BELLS, "potted_orange_bells")
                .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/orange_bells1")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_ORANGE_BOG_ASPHODEL, "potted_orange_bog_asphodel")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/orange_bog_asphodel1")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/orange_bog_asphodel2")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/orange_bog_asphodel3")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/orange_bog_asphodel4")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_ORANGE_MARIGOLDS, "potted_orange_marigolds")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/orange_marigolds1")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/orange_marigolds2")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/orange_marigolds3")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/orange_marigolds4")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_ORANGE_SUN_STAR, "potted_orange_sun_star")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/orange_sun_star1")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/orange_sun_star2")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_ORANGE_TROLLIUS, "potted_orange_trollius")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/orange_trollius1")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/orange_trollius2")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_PINK_ALLIUM, "potted_pink_allium")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/pink_allium1")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/pink_allium2")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/pink_allium3")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/pink_allium4")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_PINK_PRIMROSE, "potted_pink_primrose")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/pink_primrose1")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/pink_primrose2")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_PINK_ROSES, "potted_pink_roses")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/pink_roses1")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/pink_roses2")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/pink_roses3")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/pink_roses4")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_PINK_SWEET_PEAS, "potted_pink_sweet_peas")
                .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/pink_sweet_peas1")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_PINK_THISTLE, "potted_pink_thistle")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/pink_thistle/side1")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/pink_thistle/side2")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/pink_thistle/side3")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/pink_thistle/side4")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_PINK_TULIPS, "potted_pink_tulips")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/pink_tulips1")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/pink_tulips2")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/pink_tulips3")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/pink_tulips4")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_PINK_WILDFLOWERS, "potted_pink_wildflowers")
                .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/pink_wildflowers")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_PURPLE_ALPINE_SOWTHISTLE, "potted_purple_alpine_sowthistle")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/purple_alpine_sowthistle/side1")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/purple_alpine_sowthistle/side2")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/purple_alpine_sowthistle/side3")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/purple_alpine_sowthistle/side4")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_PURPLE_FOXGLOVE, "potted_purple_foxglove")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/purple_foxglove1")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/purple_foxglove2")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/purple_foxglove3")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/purple_foxglove4")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_PURPLE_LAVENDER, "potted_purple_lavender")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/purple_lavender1")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/purple_lavender2")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/purple_lavender3")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_PURPLE_PANSIES, "potted_purple_pansies")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/purple_pansies1")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/purple_pansies2")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/purple_pansies3")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/purple_pansies4")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_PURPLE_ROSES, "potted_purple_roses")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/purple_roses1")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/purple_roses2")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/purple_roses3")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/purple_roses4")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_PURPLE_VIOLETS, "potted_purple_violets")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/purple_violets1")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/purple_violets2")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/purple_violets3")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_RED_ASTER, "potted_red_aster")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/red_aster1")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/red_aster2")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/red_aster3")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/red_aster4")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_RED_CARNATIONS, "potted_red_carnations")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/red_carnations1")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/red_carnations2")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/red_carnations3")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/red_carnations4")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_RED_CHRYSANTHEMUM, "potted_red_chrysanthemum")
                .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/red_chrysanthemum1")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_RED_DARK_ROSES, "potted_red_dark_roses")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/red_dark_roses1")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/red_dark_roses2")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/red_dark_roses3")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/red_dark_roses4")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_RED_FERN, "potted_red_fern")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "red_fern/side1")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "red_fern/side2")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "red_fern/side3")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_RED_FLOWERING_SPINY_HERB, "potted_red_flowering_spiny_herb")
                .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/red_flowering_spiny_herb1")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_RED_MUSHROOM_1, "potted_red_mushroom_1")
                .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "red_mushroom_block/mushroom_red_0")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_RED_MUSHROOM_2, "potted_red_mushroom_2")
                .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "red_mushroom_block/mushroom_red_1")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_RED_MUSHROOM_3, "potted_red_mushroom_3")
                .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "red_mushroom_block/mushroom_red_2")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_RED_MUSHROOM_7, "potted_red_mushroom_7")
                .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "red_mushroom_block/mushroom_red_6")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_RED_MUSHROOM_8, "potted_red_mushroom_8")
                .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "red_mushroom_block/mushroom_red_7")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_RED_MUSHROOM_9, "potted_red_mushroom_9")
                .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "red_mushroom_block/mushroom_red_8")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_RED_POPPIES, "potted_red_poppies")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/red_poppies1")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/red_poppies2")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_RED_ROSES, "potted_red_roses")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/red_roses1")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/red_roses2")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/red_roses3")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/red_roses4")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_RED_SORREL, "potted_red_sorrel")
                .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/red_sorrel1")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_RED_SOURLEAF_BUSH, "potted_red_sourleaf_bush")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/red_sourleaf_bush1")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/red_sourleaf_bush2")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_RED_TULIPS, "potted_red_tulips")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/red_tulips1")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/red_tulips2")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/red_tulips3")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/red_tulips4")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_WHITE_CHAMOMILE, "potted_white_chamomile")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/white_chamomile1")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/white_chamomile2")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/white_chamomile3")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_WHITE_DAISIES, "potted_white_daisies")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/white_daisies1")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/white_daisies2")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/white_daisies3")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/white_daisies4")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_WHITE_LILYOFTHEVALLEY, "potted_white_lilyofthevalley")
                .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/white_lily_valley1")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_WHITE_PEONY, "potted_white_peony")
                .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/white_peony1")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_WHITE_ROSES, "potted_white_roses")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/white_roses1")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/white_roses2")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/white_roses3")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/white_roses4")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_YELLOW_BEDSTRAW, "potted_yellow_bedstraw")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/yellow_bedstraw/side1")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/yellow_bedstraw/side2")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/yellow_bedstraw/side3")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_YELLOW_BELLS, "potted_yellow_bells")
                .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/yellow_bells1")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_YELLOW_BUTTERCUPS, "potted_yellow_buttercups")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/yellow_buttercups1")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/yellow_buttercups2")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/yellow_buttercups3")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/yellow_buttercups4")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_YELLOW_DAFFODILS, "potted_yellow_daffodils")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/yellow_daffodils1")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/yellow_daffodils2")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/yellow_daffodils3")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/yellow_daffodils4")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_YELLOW_DAISIES, "potted_yellow_daisies")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/yellow_daisies1")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/yellow_daisies2")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_YELLOW_DANDELIONS, "potted_yellow_dandelions")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/yellow_dandelions1")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/yellow_dandelions2")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/yellow_dandelions3")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_YELLOW_HELLEBORE, "potted_yellow_hellebore")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/yellow_hellebore1")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/yellow_hellebore2")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_YELLOW_LUPINE, "potted_yellow_lupine")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/yellow_lupine1")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/yellow_lupine2")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/yellow_lupine3")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/yellow_lupine4")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_YELLOW_ROSES, "potted_yellow_roses")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/yellow_roses1")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/yellow_roses2")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/yellow_roses3")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/yellow_roses4")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_YELLOW_RUDBECKIA, "potted_yellow_rudbeckia")
                .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/yellow_rudbeckia1")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_YELLOW_SUNFLOWER, "potted_yellow_sunflower")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/yellow_sunflower/side1")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/yellow_sunflower/side2")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/yellow_sunflower/side3")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_YELLOW_TANSY, "potted_yellow_tansy")
                .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/yellow_tansy")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_YELLOW_WILDFLOWERS, "potted_yellow_wildflowers")
                .textures("minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/yellow_wildflowers")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_BLUE_CHICORY, "potted_blue_chicory")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/blue_chicory/side1")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/blue_chicory/side2")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/blue_chicory/side3")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/blue_chicory/side4")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_BLUE_FLAX, "potted_blue_flax")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/blue_flax1")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/blue_flax2")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/blue_flax3")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/blue_flax4")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_BLUE_FORGETMENOTS, "potted_blue_forgetmenots")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/blue_forgetmenots1")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/blue_forgetmenots2")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/blue_forgetmenots3")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/blue_forgetmenots4")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_BLUE_HYACINTH, "potted_blue_hyacinth")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/blue_hyacinth1")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/blue_hyacinth2")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/blue_hyacinth3")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/blue_hyacinth4")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_BLUE_ORCHID, "potted_blue_orchid")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/blue_orchid1")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/blue_orchid2")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "flowers/blue_orchid3")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_BRACKEN, "potted_bracken")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "bracken/side1")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "bracken/side2")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "bracken/side3")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "bracken/side4")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "bracken/side5")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "bracken/side6")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "bracken/side7")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "bracken/side8")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_CATTAILS, "potted_cattails")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "cattails/side1")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "cattails/side2")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "cattails/side3")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "cattails/side4")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "cattails/side5")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "cattails/side6")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "cattails/side7")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "cattails/side8")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_COW_PARSELY, "potted_cow_parsely")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "cow_parsely/side1")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "cow_parsely/side2")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_DEAD_BRACKEN, "potted_dead_bracken")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "dead_bracken/side1")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "dead_bracken/side2")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "dead_bracken/side3")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "dead_bracken/side4")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "dead_bracken/side5")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "dead_bracken/side6")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "dead_bracken/side7")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "dead_bracken/side8")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "dead_bracken/side9")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "dead_bracken/side10")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "dead_bracken/side11")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "dead_bracken/side12")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_DEAD_BUSH, "potted_dead_bush")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "dorne_bush_thorny/side1")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "dorne_bush_thorny/side2")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "dorne_bush_thorny/side3")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_DOCK_LEAF, "potted_dock_leaf")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "dock_leaf/side1")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "dock_leaf/side2")
                .build();

        FlowerPotBlockDatagen.generateFlowerPotBlock(bsmg, PlantBlocks.POTTED_GRASS, "potted_grass")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "minecraft:block/fern/fern1")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "minecraft:block/fern/fern2")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "minecraft:block/fern/fern3")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "minecraft:block/fern/fern4")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "minecraft:block/fern/fern5")
                .addRandomTextureSet(1, "minecraft:block/dirt", "minecraft:block/flower_pot", "minecraft:block/fern/fern6")
                .build();

        FireBlockDatagen.generateFireBlock(bsmg, ModBlocks.SAFE_FIRE, "safe_fire")
                .textures("safe_fire/fire_layer_0", "safe_fire/fire_layer_1")
                .build();

        FireBlockDatagen.generateFireBlock(bsmg, ModBlocks.WILDFIRE, "wildfire")
                .textures("wildfire/wildfire_layer_0", "wildfire/wildfire_layer_1")
                .build();

        // Fence Blocks
        FenceBlockDatagen.generateFenceBlock(bsmg, ModBlocks.BIRCH_BARK_FENCE, "birch_bark_fence")
                .texture("bark/birch/side")
                .build();

        FenceBlockDatagen.generateFenceBlock(bsmg, ModBlocks.BIRCH_FENCE_WITH_GRAPES, "birch_fence_with_grapes")
                .texture("wood/birch/grapevines")
                .build();

        FenceBlockDatagen.generateFenceBlock(bsmg, ModBlocks.BIRCH_FENCE_WITH_VINES, "birch_fence_with_vines")
                .texture("wood/birch/vines")
                .build();

        FenceBlockDatagen.generateFenceBlock(bsmg, ModBlocks.JUNGLE_BARK_FENCE, "jungle_bark_fence")
                .texture("bark/jungle/side")
                .build();

        FenceBlockDatagen.generateFenceBlock(bsmg, ModBlocks.JUNGLE_FENCE_WITH_GRAPES, "jungle_fence_with_grapes")
                .texture("wood/jungle/grapevines")
                .build();

        FenceBlockDatagen.generateFenceBlock(bsmg, ModBlocks.JUNGLE_FENCE_WITH_VINES, "jungle_fence_with_vines")
                .texture("wood/jungle/vines")
                .build();

        FenceBlockDatagen.generateFenceBlock(bsmg, ModBlocks.MARBLE_COLUMN_FENCE, "marble_column_fence")
                .texture("marble/quartz/column_side")
                .build();

        FenceBlockDatagen.generateFenceBlock(bsmg, ModBlocks.OAK_BARK_FENCE, "oak_bark_fence")
                .texture("bark/oak/side")
                .build();

        FenceBlockDatagen.generateFenceBlock(bsmg, ModBlocks.OAK_FENCE_WITH_GRAPES, "oak_fence_with_grapes")
                .texture("wood/oak/grapevines")
                .build();

        FenceBlockDatagen.generateFenceBlock(bsmg, ModBlocks.OAK_FENCE_WITH_VINES, "oak_fence_with_vines")
                .texture("wood/oak/vines")
                .build();

        FenceBlockDatagen.generateFenceBlock(bsmg, ModBlocks.PALM_FENCE, "palm_fence")
                .texture("bark/palm/side")
                .build();

        FenceBlockDatagen.generateFenceBlock(bsmg, ModBlocks.REINFORCED_OAK_FENCE, "reinforced_oak_fence")
                .texture("wood/oak/reinforced")
                .build();

        FenceBlockDatagen.generateFenceBlock(bsmg, ModBlocks.SEPT_CRYSTAL_SMALL, "sept_crystal_small")
                .textures("crystal/fence_top", "crystal/fence_top", "crystal/fence")
                .build();

        FenceBlockDatagen.generateFenceBlock(bsmg, ModBlocks.SPRUCE_BARK_FENCE, "spruce_bark_fence")
                .texture("bark/spruce/side")
                .build();

        FenceBlockDatagen.generateFenceBlock(bsmg, ModBlocks.SPRUCE_FENCE_WITH_GRAPES, "spruce_fence_with_grapes")
                .texture("wood/spruce/grapevines")
                .build();

        FenceBlockDatagen.generateFenceBlock(bsmg, ModBlocks.SPRUCE_FENCE_WITH_VINES, "spruce_fence_with_vines")
                .texture("wood/spruce/vines")
                .build();

        FenceBlockDatagen.generateFenceBlock(bsmg, ModBlocks.STACKED_BONES_FENCE, "stacked_bones_fence")
                .texture("stacked_bones/bone_stacked_side")
                .build();

        // Fence Gate Blocks
        FenceGateBlockDatagen.generateFenceGateBlock(bsmg, ModBlocks.LOCKED_BIRCH_BARK_FENCE_GATE, "locked_birch_bark_fence_gate")
                .texture("bark/birch/side")
                .build();

        FenceGateBlockDatagen.generateFenceGateBlock(bsmg, ModBlocks.LOCKED_JUNGLE_BARK_FENCE_GATE, "locked_jungle_bark_fence_gate")
                .texture("bark/jungle/side")
                .build();

        FenceGateBlockDatagen.generateFenceGateBlock(bsmg, ModBlocks.LOCKED_OAK_BARK_FENCE_GATE, "locked_oak_bark_fence_gate")
                .texture("bark/oak/side")
                .build();

        FenceGateBlockDatagen.generateFenceGateBlock(bsmg, ModBlocks.LOCKED_SPRUCE_BARK_FENCE_GATE, "locked_spruce_bark_fence_gate")
                .texture("bark/spruce/side")
                .build();

        // Particle Emitter Blocks
        ParticleEmitterDatagen.registerCustomParticleEmitterBlock(bsmg, ModBlocks.CASCADE_PARTICLE_EMITTER, "cascade_particle_emitter");
        ParticleEmitterDatagen.registerCustomParticleEmitterBlock(bsmg, ModBlocks.COSY_SMOKE_PARTICLE_EMITTER, "cosy_smoke_particle_emitter");
        ParticleEmitterDatagen.registerCustomParticleEmitterBlock(bsmg, ModBlocks.SIGNAL_SMOKE_PARTICLE_EMITTER, "signal_smoke_particle_emitter");
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        // Item models are now handled automatically by the block exporters
    }

    private void generateModelsFromDefinitions(BlockStateModelGenerator bsmg) {
        BlockDefinitionRegistry registry = BlockDefinitionRegistry.getInstance();

        if (!registry.isInitialized()) {
            return;
        }

        // Generate solid block models
        for (BlockDefinition definition : registry.getByType("solid")) {
            Block block = ModBlocks.getAutoRegisteredBlock(definition.getBlockName());
            if (block != null) {
                SolidBlockExporter.registerCustomSolidBlock(bsmg, block, definition);
            }
        }

        // TODO: Add other block types (door, slab, etc.) as needed
    }
}
