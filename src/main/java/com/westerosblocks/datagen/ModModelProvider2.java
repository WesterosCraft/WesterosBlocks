package com.westerosblocks.datagen;


import com.westerosblocks.block.ModBlocks2;
import com.westerosblocks.datagen.models.*;
import net.minecraft.data.client.*;

public class ModModelProvider2 {
    static ArrowSlitBlockExport arrowSlitExport = new ArrowSlitBlockExport();
    static TableBlockExport tableExport = new TableBlockExport();
    static SandBlockExport sandExport = new SandBlockExport();
    static DoorBlockExport doorExporter = new DoorBlockExport();
    static ChairBlockExport chairExporter = new ChairBlockExport();
    static TrapDoorBlockExport trapDoorExporter = new TrapDoorBlockExport();
    static WaySignBlockExport waySignExporter = new WaySignBlockExport();
    static LogBlockExport logExporter = new LogBlockExport();
    static TorchBlockExport torchExporter = new TorchBlockExport();
    static FanBlockExport fanExporter = new FanBlockExport();
    static VinesBlockExport vinesExporter = new VinesBlockExport();
    static HalfDoorBlockExport halfDoorExporter = new HalfDoorBlockExport();
    static StandaloneCrossBlockExport crossExporter = new StandaloneCrossBlockExport();

    public static void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        // Arrow Slits
        arrowSlitExport.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.ARBOR_BRICK_ARROW_SLIT,
            "westerosblocks:block/ashlar_third/arbor/all");
        arrowSlitExport.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.BLACK_GRANITE_ARROW_SLIT,
            "westerosblocks:block/ashlar_third/black/all");

        // Tables
        tableExport.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.OAK_TABLE,
            "westerosblocks:block/wood/oak/all");
        tableExport.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.BIRCH_TABLE,
            "westerosblocks:block/wood/birch/all");
        tableExport.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.SPRUCE_TABLE,
            "westerosblocks:block/wood/spruce/all");

        // Chairs
        chairExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.OAK_CHAIR,
            "westerosblocks:block/wood/oak/all");
        chairExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.BIRCH_CHAIR,
            "westerosblocks:block/wood/birch/all");
        chairExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.SPRUCE_CHAIR,
            "westerosblocks:block/wood/spruce/all");

        // way signs
        waySignExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.OAK_WAY_SIGN,
            "westerosblocks:block/wood/oak/all");

        // trap doors
        trapDoorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.KINGS_LANDING_SEWER_MANHOLE,
            "westerosblocks:block/trapdoor_block/kings_landing_manhole");
        trapDoorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.OLDTOWN_SEWER_MANHOLE,
            "westerosblocks:block/trapdoor_block/oldtown_manhole");
        trapDoorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.SEWER_MANHOLE,
            "westerosblocks:block/trapdoor_block/manhole");
        trapDoorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.WHITE_HARBOR_SEWER_MANHOLE,
            "westerosblocks:block/trapdoor_block/white_harbor_manhole");

        // log blocks
        logExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.OAK_LOG_CHAIN,
            "bark/oak/top", "bark/oak/top", "bark/oak/chain");
        logExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.JUNGLE_LOG_CHAIN,
            "bark/jungle/top", "bark/jungle/top", "bark/jungle/chain");
        logExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.JUNGLE_LOG_ROPE,
            "bark/jungle/top", "bark/jungle/top", "bark/jungle/rope");
        logExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.ARCHERY_TARGET,
            "archery_target/front", "archery_target/front", "archery_target/side");
        logExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.CLOSED_BARREL,
            "barrel_closed/barrel_top_closed", "barrel_closed/barrel_top_closed", "barrel_sides/side1");
        logExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.FIREWOOD,
            "firewood/top", "firewood/top", "firewood/side");
        logExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.MARBLE_PILLAR,
            "marble/quartz/column_topbottom", "marble/quartz/column_topbottom", "marble/quartz/column_side");
        logExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.MARBLE_PILLAR_VERTICAL_CTM,
            "marble/quartz/column_topbottom", "marble/quartz/column_topbottom", "marble/quartz/column_side_ctm");
        logExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.MOSSY_OAK_LOG,
            "bark/oak/mossy/bottom", "bark/oak/mossy/top", "bark/oak/mossy/side");
        logExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.MOSSY_BIRCH_LOG,
            "bark/birch/mossy/bottom", "bark/birch/mossy/top", "bark/birch/mossy/side");
        logExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.MOSSY_SPRUCE_LOG,
            "bark/spruce/mossy/bottom", "bark/spruce/mossy/top", "bark/spruce/mossy/side");
        logExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.MOSSY_JUNGLE_LOG,
            "bark/jungle/mossy/bottom", "bark/jungle/mossy/top", "bark/jungle/mossy/side");
        logExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.OAK_LOG_ROPE,
            "bark/oak/top", "bark/oak/top", "bark/oak/rope");
        logExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.PALM_TREE_LOG,
            "bark/palm/top", "bark/palm/top", "bark/palm/side");
        logExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.SANDSTONE_PILLAR,
            "ashlar_third/sandstone/column_top", "ashlar_third/sandstone/column_top", "ashlar_third/sandstone/column_side");
        logExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.SPRUCE_LOG_CHAIN,
            "bark/spruce/top", "bark/spruce/top", "bark/spruce/chain");
        logExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.SPRUCE_LOG_ROPE,
            "bark/spruce/top", "bark/spruce/top", "bark/spruce/rope");
        logExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.STACKED_BONES,
            "stacked_bones/bone_stacked_front", "stacked_bones/bone_stacked_front", "stacked_bones/bone_stacked_side");
        logExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.WEIRWOOD_FACE_0,
            "bark/weirwood/face_0", "bark/weirwood/face_0", "bark/weirwood/side");
        logExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.WEIRWOOD_FACE_1,
            "bark/weirwood/face_1", "bark/weirwood/face_1", "bark/weirwood/side");
        logExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.WEIRWOOD_FACE_2,
            "bark/weirwood/face_2", "bark/weirwood/face_2", "bark/weirwood/side");
        logExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.WEIRWOOD_FACE_3,
            "bark/weirwood/face_3", "bark/weirwood/face_3", "bark/weirwood/side");
        logExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.WEIRWOOD_FACE_4,
            "bark/weirwood/face_4", "bark/weirwood/face_4", "bark/weirwood/side");
        logExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.WEIRWOOD_FACE_5,
            "bark/weirwood/face_5", "bark/weirwood/face_5", "bark/weirwood/side");
        logExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.WEIRWOOD_FACE_6,
            "bark/weirwood/face_6", "bark/weirwood/face_6", "bark/weirwood/side");
        logExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.WEIRWOOD_FACE_7,
            "bark/weirwood/face_7", "bark/weirwood/face_7", "bark/weirwood/side");
        logExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.WEIRWOOD_FACE_8,
            "bark/weirwood/face_8", "bark/weirwood/face_8", "bark/weirwood/side");
        logExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.WEIRWOOD_SCARS,
            "bark/weirwood/scars", "bark/weirwood/scars", "bark/weirwood/side");

        // Door blocks
        doorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.WHITE_WOOD_DOOR, 
            "wood/white/door_top", "wood/white/door_bottom");
        doorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.LOCKED_WHITE_WOOD_DOOR, 
            "wood/white/door_locked_top", "wood/white/door_locked_bottom");
        doorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.NORTHERN_WOOD_DOOR, 
            "wood/northern/door_top", "wood/northern/door_bottom");
        doorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.SPRUCE_DOOR, 
            "wood/spruce/door_top", "wood/spruce/door_bottom");
        doorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.OAK_DOOR, 
            "wood/oak/door_top", "wood/oak/door_bottom");
        doorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.BIRCH_DOOR, 
            "wood/birch/door_top", "wood/birch/door_bottom");
        doorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.EYRIE_WEIRWOOD_DOOR, 
            "door_block/door_weirwood_top", "door_block/door_weirwood_bottom");
        doorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.GREY_WOOD_DOOR, 
            "wood/grey/door_top", "wood/grey/door_bottom");
        doorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.JUNGLE_DOOR, 
            "wood/jungle/door_top", "wood/jungle/door_bottom");
        doorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.RED_KEEP_SECRET_DOOR, 
            "ashlar_third/pale_red/all_noctm", "ashlar_third/pale_red/all_noctm");
        doorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.HARRENHAL_SECRET_DOOR, 
            "ashlar_third/black/all_noctm", "ashlar_third/black/all_noctm");
        doorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.LOCKED_BIRCH_DOOR, 
            "wood/birch/door_locked_top", "wood/birch/door_locked_bottom");
        doorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.LOCKED_DARK_NORTHERN_WOOD_DOOR, 
            "wood/northern/door_locked_top", "wood/northern/door_locked_bottom");
        doorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.LOCKED_GREY_WOOD_DOOR, 
            "wood/grey/door_locked_top", "wood/grey/door_locked_bottom");
        doorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.LOCKED_SPRUCE_DOOR, 
            "wood/spruce/door_locked_top", "wood/spruce/door_locked_bottom");
        doorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.LOCKED_JUNGLE_DOOR, 
            "wood/jungle/door_locked_top", "wood/jungle/door_locked_bottom");
        doorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.LOCKED_OAK_DOOR, 
            "wood/oak/door_locked_top", "wood/oak/door_locked_bottom");


        // Torch Blocks
        torchExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.TORCH, "lighting/torch");
        torchExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.TORCH_UNLIT, "lighting/torch_unlit");
        torchExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.CANDLE, "lighting/candle");
        torchExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.CANDLE_UNLIT, "lighting/candle_unlit");

        // Sand Blocks
        sandExport.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.SAND_SKELETON, 
            "sand_block/sand_skeleton");

        // Coral Fan Blocks
        fanExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.CORAL_BRAIN_FAN, 
            "coral/brain/fan1");
        fanExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.CORAL_BUBBLE_FAN, 
            "coral/bubble/fan1");
        fanExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.CORAL_FIRE_FAN, 
            "coral/fire/fan1");
        fanExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.CORAL_HORN_FAN, 
            "coral/horn/fan1");
        fanExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.CORAL_TUBE_FAN, 
            "coral/tube/fan1");

        // Vines Blocks
        vinesExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.DAPPLED_MOSS, 
            "dappled_moss/dappled", "dappled_moss/dappled");
        vinesExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.JASMINE_VINES, 
            "jasmine_vines/side1", "jasmine_vines/side1");
        vinesExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.VINES, 
            "vines/side1", "vines/side1");
        vinesExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.FALLING_WATER_BLOCK_ONE, 
            "alyssas_tears_mist/mist1", "alyssas_tears_mist/mist1");
        vinesExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.FALLING_WATER_BLOCK_TWO, 
            "alyssas_tears_mist/mist2", "alyssas_tears_mist/mist2");
        vinesExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.FALLING_WATER_BLOCK_THREE, 
            "alyssas_tears_mist/mist3", "alyssas_tears_mist/mist3");
        vinesExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.FALLING_WATER_BLOCK_FOUR, 
            "alyssas_tears_mist/mist4", "alyssas_tears_mist/mist4");

        // Half Door Blocks
        halfDoorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.BIRCH_WINDOW_SHUTTERS, 
            "wood/birch/shutters");
        halfDoorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.DORNE_RED_WINDOW_SHUTTERS, 
            "shutter_block/shutters_dorne");
        halfDoorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.GREEN_LANNISPORT_WINDOW_SHUTTERS, 
            "shutter_block/shutters_lannisport");
        halfDoorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.GREY_WOOD_WINDOW_SHUTTERS, 
            "wood/grey/shutters");
        halfDoorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.JUNGLE_WINDOW_SHUTTERS, 
            "wood/jungle/shutters");
        halfDoorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.NORTHERN_WOOD_WINDOW_SHUTTERS, 
            "wood/northern/shutters");
        halfDoorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.OAK_WINDOW_SHUTTERS, 
            "wood/oak/shutters");
        halfDoorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.REACH_BLUE_WINDOW_SHUTTERS, 
            "shutter_block/shutters_reach");
        halfDoorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.SPRUCE_WINDOW_SHUTTERS, 
            "wood/spruce/shutters");
        halfDoorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.WHITE_WOOD_WINDOW_SHUTTERS, 
            "wood/white/shutters");

        // Cross Blocks (Plant Blocks)
        crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.BLUE_BELLS, 
            "flowers/blue_bells", true, 4);
        crossExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.BLUE_CHICORY, 
            "flowers/blue_chicory/side1", true, 4);
    }

    public static void generateItemModels(ItemModelGenerator itemModelGenerator) {
        // Arrow Slit Blocks
        arrowSlitExport.generateItemModels(itemModelGenerator, ModBlocks2.ARBOR_BRICK_ARROW_SLIT);
        arrowSlitExport.generateItemModels(itemModelGenerator, ModBlocks2.BLACK_GRANITE_ARROW_SLIT);

        // Table Blocks
        tableExport.generateItemModels(itemModelGenerator, ModBlocks2.OAK_TABLE);
        tableExport.generateItemModels(itemModelGenerator, ModBlocks2.BIRCH_TABLE);
        tableExport.generateItemModels(itemModelGenerator, ModBlocks2.SPRUCE_TABLE);

        // Chair Blocks
        chairExporter.generateItemModels(itemModelGenerator, ModBlocks2.OAK_CHAIR);
        chairExporter.generateItemModels(itemModelGenerator, ModBlocks2.BIRCH_CHAIR);
        chairExporter.generateItemModels(itemModelGenerator, ModBlocks2.SPRUCE_CHAIR);

        // Way Sign Blocks
        waySignExporter.generateItemModels(itemModelGenerator, ModBlocks2.OAK_WAY_SIGN);

        // TrapDoor Blocks
        trapDoorExporter.generateItemModels(itemModelGenerator, ModBlocks2.KINGS_LANDING_SEWER_MANHOLE);
        trapDoorExporter.generateItemModels(itemModelGenerator, ModBlocks2.OLDTOWN_SEWER_MANHOLE);
        trapDoorExporter.generateItemModels(itemModelGenerator, ModBlocks2.SEWER_MANHOLE);
        trapDoorExporter.generateItemModels(itemModelGenerator, ModBlocks2.WHITE_HARBOR_SEWER_MANHOLE);

        // Log Blocks
        logExporter.generateItemModels(itemModelGenerator, ModBlocks2.OAK_LOG_CHAIN);
        logExporter.generateItemModels(itemModelGenerator, ModBlocks2.JUNGLE_LOG_CHAIN);
        logExporter.generateItemModels(itemModelGenerator, ModBlocks2.JUNGLE_LOG_ROPE);
        logExporter.generateItemModels(itemModelGenerator, ModBlocks2.ARCHERY_TARGET);
        logExporter.generateItemModels(itemModelGenerator, ModBlocks2.CLOSED_BARREL);
        logExporter.generateItemModels(itemModelGenerator, ModBlocks2.FIREWOOD);
        logExporter.generateItemModels(itemModelGenerator, ModBlocks2.MARBLE_PILLAR);
        logExporter.generateItemModels(itemModelGenerator, ModBlocks2.MARBLE_PILLAR_VERTICAL_CTM);
        logExporter.generateItemModels(itemModelGenerator, ModBlocks2.MOSSY_OAK_LOG);
        logExporter.generateItemModels(itemModelGenerator, ModBlocks2.MOSSY_BIRCH_LOG);
        logExporter.generateItemModels(itemModelGenerator, ModBlocks2.MOSSY_SPRUCE_LOG);
        logExporter.generateItemModels(itemModelGenerator, ModBlocks2.MOSSY_JUNGLE_LOG);
        logExporter.generateItemModels(itemModelGenerator, ModBlocks2.OAK_LOG_ROPE);
        logExporter.generateItemModels(itemModelGenerator, ModBlocks2.PALM_TREE_LOG);
        logExporter.generateItemModels(itemModelGenerator, ModBlocks2.SANDSTONE_PILLAR);
        logExporter.generateItemModels(itemModelGenerator, ModBlocks2.SPRUCE_LOG_CHAIN);
        logExporter.generateItemModels(itemModelGenerator, ModBlocks2.SPRUCE_LOG_ROPE);
        logExporter.generateItemModels(itemModelGenerator, ModBlocks2.STACKED_BONES);
        logExporter.generateItemModels(itemModelGenerator, ModBlocks2.WEIRWOOD_FACE_0);
        logExporter.generateItemModels(itemModelGenerator, ModBlocks2.WEIRWOOD_FACE_1);
        logExporter.generateItemModels(itemModelGenerator, ModBlocks2.WEIRWOOD_FACE_2);
        logExporter.generateItemModels(itemModelGenerator, ModBlocks2.WEIRWOOD_FACE_3);
        logExporter.generateItemModels(itemModelGenerator, ModBlocks2.WEIRWOOD_FACE_4);
        logExporter.generateItemModels(itemModelGenerator, ModBlocks2.WEIRWOOD_FACE_5);
        logExporter.generateItemModels(itemModelGenerator, ModBlocks2.WEIRWOOD_FACE_6);
        logExporter.generateItemModels(itemModelGenerator, ModBlocks2.WEIRWOOD_FACE_7);
        logExporter.generateItemModels(itemModelGenerator, ModBlocks2.WEIRWOOD_FACE_8);
        logExporter.generateItemModels(itemModelGenerator, ModBlocks2.WEIRWOOD_SCARS);

        // Torch Blocks
        torchExporter.generateItemModels(itemModelGenerator, ModBlocks2.TORCH);
        torchExporter.generateItemModels(itemModelGenerator, ModBlocks2.TORCH_UNLIT);
        torchExporter.generateItemModels(itemModelGenerator, ModBlocks2.CANDLE);
        torchExporter.generateItemModels(itemModelGenerator, ModBlocks2.CANDLE_UNLIT);

        // Door Blocks
        doorExporter.generateItemModels(itemModelGenerator, ModBlocks2.WHITE_WOOD_DOOR, "westerosblocks:item/white_door");
        doorExporter.generateItemModels(itemModelGenerator, ModBlocks2.LOCKED_WHITE_WOOD_DOOR, "westerosblocks:item/white_door");
        doorExporter.generateItemModels(itemModelGenerator, ModBlocks2.NORTHERN_WOOD_DOOR, "westerosblocks:item/northern_door");
        doorExporter.generateItemModels(itemModelGenerator, ModBlocks2.LOCKED_DARK_NORTHERN_WOOD_DOOR, "westerosblocks:item/northern_door");
        doorExporter.generateItemModels(itemModelGenerator, ModBlocks2.SPRUCE_DOOR, "westerosblocks:item/spruce_door");
        doorExporter.generateItemModels(itemModelGenerator, ModBlocks2.LOCKED_SPRUCE_DOOR, "westerosblocks:item/spruce_door");
        doorExporter.generateItemModels(itemModelGenerator, ModBlocks2.OAK_DOOR, "westerosblocks:item/oak_door");
        doorExporter.generateItemModels(itemModelGenerator, ModBlocks2.LOCKED_OAK_DOOR, "westerosblocks:item/oak_door");
        doorExporter.generateItemModels(itemModelGenerator, ModBlocks2.BIRCH_DOOR, "westerosblocks:item/birch_door");
        doorExporter.generateItemModels(itemModelGenerator, ModBlocks2.LOCKED_BIRCH_DOOR, "westerosblocks:item/birch_door");
        doorExporter.generateItemModels(itemModelGenerator, ModBlocks2.EYRIE_WEIRWOOD_DOOR, "westerosblocks:item/moon_door");
        doorExporter.generateItemModels(itemModelGenerator, ModBlocks2.GREY_WOOD_DOOR, "westerosblocks:item/grey_door");
        doorExporter.generateItemModels(itemModelGenerator, ModBlocks2.LOCKED_GREY_WOOD_DOOR, "westerosblocks:item/grey_door");
        doorExporter.generateItemModels(itemModelGenerator, ModBlocks2.JUNGLE_DOOR, "westerosblocks:item/jungle_door");
        doorExporter.generateItemModels(itemModelGenerator, ModBlocks2.LOCKED_JUNGLE_DOOR, "westerosblocks:item/jungle_door");
        doorExporter.generateItemModels(itemModelGenerator, ModBlocks2.RED_KEEP_SECRET_DOOR, "westerosblocks:item/red_keep_door");
        doorExporter.generateItemModels(itemModelGenerator, ModBlocks2.HARRENHAL_SECRET_DOOR, "westerosblocks:item/harrenhal_door");

        // Standalone sand item models
        sandExport.generateItemModels(itemModelGenerator, ModBlocks2.SAND_SKELETON);

        // Coral Fan Blocks
        fanExporter.generateItemModels(itemModelGenerator, ModBlocks2.CORAL_BRAIN_FAN);
        fanExporter.generateItemModels(itemModelGenerator, ModBlocks2.CORAL_BUBBLE_FAN);
        fanExporter.generateItemModels(itemModelGenerator, ModBlocks2.CORAL_FIRE_FAN);
        fanExporter.generateItemModels(itemModelGenerator, ModBlocks2.CORAL_HORN_FAN);
        fanExporter.generateItemModels(itemModelGenerator, ModBlocks2.CORAL_TUBE_FAN);

        // Vines Blocks
        vinesExporter.generateItemModels(itemModelGenerator, ModBlocks2.DAPPLED_MOSS, "dappled_moss/dappled");
        vinesExporter.generateItemModels(itemModelGenerator, ModBlocks2.JASMINE_VINES, "jasmine_vines/side1");
        vinesExporter.generateItemModels(itemModelGenerator, ModBlocks2.VINES, "vines/side1");
        vinesExporter.generateItemModels(itemModelGenerator, ModBlocks2.FALLING_WATER_BLOCK_ONE, "alyssas_tears_mist/mist1");
        vinesExporter.generateItemModels(itemModelGenerator, ModBlocks2.FALLING_WATER_BLOCK_TWO, "alyssas_tears_mist/mist2");
        vinesExporter.generateItemModels(itemModelGenerator, ModBlocks2.FALLING_WATER_BLOCK_THREE, "alyssas_tears_mist/mist3");
        vinesExporter.generateItemModels(itemModelGenerator, ModBlocks2.FALLING_WATER_BLOCK_FOUR, "alyssas_tears_mist/mist4");

        // Half Door Blocks
        halfDoorExporter.generateItemModels(itemModelGenerator, ModBlocks2.BIRCH_WINDOW_SHUTTERS, "wood/birch/shutters");
        halfDoorExporter.generateItemModels(itemModelGenerator, ModBlocks2.DORNE_RED_WINDOW_SHUTTERS, "shutter_block/shutters_dorne");
        halfDoorExporter.generateItemModels(itemModelGenerator, ModBlocks2.GREEN_LANNISPORT_WINDOW_SHUTTERS, "shutter_block/shutters_lannisport");
        halfDoorExporter.generateItemModels(itemModelGenerator, ModBlocks2.GREY_WOOD_WINDOW_SHUTTERS, "wood/grey/shutters");
        halfDoorExporter.generateItemModels(itemModelGenerator, ModBlocks2.JUNGLE_WINDOW_SHUTTERS, "wood/jungle/shutters");
        halfDoorExporter.generateItemModels(itemModelGenerator, ModBlocks2.NORTHERN_WOOD_WINDOW_SHUTTERS, "wood/northern/shutters");
        halfDoorExporter.generateItemModels(itemModelGenerator, ModBlocks2.OAK_WINDOW_SHUTTERS, "wood/oak/shutters");
        halfDoorExporter.generateItemModels(itemModelGenerator, ModBlocks2.REACH_BLUE_WINDOW_SHUTTERS, "shutter_block/shutters_reach");
        halfDoorExporter.generateItemModels(itemModelGenerator, ModBlocks2.SPRUCE_WINDOW_SHUTTERS, "wood/spruce/shutters");
        halfDoorExporter.generateItemModels(itemModelGenerator, ModBlocks2.WHITE_WOOD_WINDOW_SHUTTERS, "wood/white/shutters");

        // Cross Blocks (Plant Blocks)
        crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.BLUE_BELLS, "flowers/blue_bells");
        crossExporter.generateItemModels(itemModelGenerator, ModBlocks2.BLUE_CHICORY, "flowers/blue_chicory/side1");
    }
} 