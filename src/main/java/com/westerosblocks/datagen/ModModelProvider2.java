package com.westerosblocks.datagen;


import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.ModBlocks2;
import com.westerosblocks.datagen.models.*;
import net.minecraft.data.client.*;
import net.minecraft.registry.Registries;

public class ModModelProvider2 {

    public static void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        // Create instances of export classes
        ArrowSlitBlockExport arrowSlitExport = new ArrowSlitBlockExport();
        TableBlockExport tableExport = new TableBlockExport();
        StandaloneSandBlockExport sandExport = new StandaloneSandBlockExport();

        // arrow slits
        arrowSlitExport.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.ARBOR_BRICK_ARROW_SLIT,
            "westerosblocks:block/ashlar_third/arbor/all");
        arrowSlitExport.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.BLACK_GRANITE_ARROW_SLIT,
            "westerosblocks:block/ashlar_third/black/all");

        // tables
        tableExport.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.OAK_TABLE,
            "westerosblocks:block/wood/oak/all");
        tableExport.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.BIRCH_TABLE,
            "westerosblocks:block/wood/birch/all");
        tableExport.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.SPRUCE_TABLE,
            "westerosblocks:block/wood/spruce/all");

        // chairs
        ChairBlockExport.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.OAK_CHAIR,
            "westerosblocks:block/wood/oak/all");
        ChairBlockExport.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.BIRCH_CHAIR,
            "westerosblocks:block/wood/birch/all");
        ChairBlockExport.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.SPRUCE_CHAIR,
            "westerosblocks:block/wood/spruce/all");

        // way signs
        WaySignBlockExport.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.OAK_WAY_SIGN,
            "westerosblocks:block/wood/oak/all");

        // trap doors
        TrapDoorBlockExport.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.KINGS_LANDING_SEWER_MANHOLE,
            "westerosblocks:block/trapdoor_block/kings_landing_manhole");
        TrapDoorBlockExport.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.OLDTOWN_SEWER_MANHOLE,
            "westerosblocks:block/trapdoor_block/oldtown_manhole");
        TrapDoorBlockExport.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.SEWER_MANHOLE,
            "westerosblocks:block/trapdoor_block/manhole");
        TrapDoorBlockExport.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.WHITE_HARBOR_SEWER_MANHOLE,
            "westerosblocks:block/trapdoor_block/white_harbor_manhole");

        // log blocks
        LogBlockExport.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.OAK_LOG_CHAIN,
            new String[]{"bark/oak/top", "bark/oak/top", "bark/oak/chain"});
        LogBlockExport.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.JUNGLE_LOG_CHAIN,
            new String[]{"bark/jungle/top", "bark/jungle/top", "bark/jungle/chain"});
        LogBlockExport.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.JUNGLE_LOG_ROPE,
            new String[]{"bark/jungle/top", "bark/jungle/top", "bark/jungle/rope"});
        LogBlockExport.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.ARCHERY_TARGET,
            new String[]{"archery_target/front", "archery_target/front", "archery_target/side"});
        LogBlockExport.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.CLOSED_BARREL,
            new String[]{"barrel_closed/barrel_top_closed", "barrel_closed/barrel_top_closed", "barrel_sides/side1"});
        LogBlockExport.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.FIREWOOD,
            new String[]{"firewood/top", "firewood/top", "firewood/side"});
        LogBlockExport.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.MARBLE_PILLAR,
            new String[]{"marble/quartz/column_topbottom", "marble/quartz/column_topbottom", "marble/quartz/column_side"});
        LogBlockExport.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.MARBLE_PILLAR_VERTICAL_CTM,
            new String[]{"marble/quartz/column_topbottom", "marble/quartz/column_topbottom", "marble/quartz/column_side_ctm"});
        LogBlockExport.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.MOSSY_OAK_LOG,
            new String[]{"bark/oak/mossy/bottom", "bark/oak/mossy/top", "bark/oak/mossy/side"});
        LogBlockExport.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.MOSSY_BIRCH_LOG,
            new String[]{"bark/birch/mossy/bottom", "bark/birch/mossy/top", "bark/birch/mossy/side"});
        LogBlockExport.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.MOSSY_SPRUCE_LOG,
            new String[]{"bark/spruce/mossy/bottom", "bark/spruce/mossy/top", "bark/spruce/mossy/side"});
        LogBlockExport.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.MOSSY_JUNGLE_LOG,
            new String[]{"bark/jungle/mossy/bottom", "bark/jungle/mossy/top", "bark/jungle/mossy/side"});
        LogBlockExport.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.OAK_LOG_ROPE,
            new String[]{"bark/oak/top", "bark/oak/top", "bark/oak/rope"});
        LogBlockExport.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.PALM_TREE_LOG,
            new String[]{"bark/palm/top", "bark/palm/top", "bark/palm/side"});
        LogBlockExport.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.SANDSTONE_PILLAR,
            new String[]{"ashlar_third/sandstone/column_top", "ashlar_third/sandstone/column_top", "ashlar_third/sandstone/column_side"});
        LogBlockExport.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.SPRUCE_LOG_CHAIN,
            new String[]{"bark/spruce/top", "bark/spruce/top", "bark/spruce/chain"});
        LogBlockExport.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.SPRUCE_LOG_ROPE,
            new String[]{"bark/spruce/top", "bark/spruce/top", "bark/spruce/rope"});
        LogBlockExport.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.STACKED_BONES,
            new String[]{"stacked_bones/bone_stacked_front", "stacked_bones/bone_stacked_front", "stacked_bones/bone_stacked_side"});
        LogBlockExport.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.WEIRWOOD_FACE_0,
            new String[]{"bark/weirwood/face_0", "bark/weirwood/face_0", "bark/weirwood/side"});
        LogBlockExport.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.WEIRWOOD_FACE_1,
            new String[]{"bark/weirwood/face_1", "bark/weirwood/face_1", "bark/weirwood/side"});
        LogBlockExport.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.WEIRWOOD_FACE_2,
            new String[]{"bark/weirwood/face_2", "bark/weirwood/face_2", "bark/weirwood/side"});
        LogBlockExport.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.WEIRWOOD_FACE_3,
            new String[]{"bark/weirwood/face_3", "bark/weirwood/face_3", "bark/weirwood/side"});
        LogBlockExport.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.WEIRWOOD_FACE_4,
            new String[]{"bark/weirwood/face_4", "bark/weirwood/face_4", "bark/weirwood/side"});
        LogBlockExport.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.WEIRWOOD_FACE_5,
            new String[]{"bark/weirwood/face_5", "bark/weirwood/face_5", "bark/weirwood/side"});
        LogBlockExport.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.WEIRWOOD_FACE_6,
            new String[]{"bark/weirwood/face_6", "bark/weirwood/face_6", "bark/weirwood/side"});
        LogBlockExport.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.WEIRWOOD_FACE_7,
            new String[]{"bark/weirwood/face_7", "bark/weirwood/face_7", "bark/weirwood/side"});
        LogBlockExport.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.WEIRWOOD_FACE_8,
            new String[]{"bark/weirwood/face_8", "bark/weirwood/face_8", "bark/weirwood/side"});
        LogBlockExport.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.WEIRWOOD_SCARS,
            new String[]{"bark/weirwood/scars", "bark/weirwood/scars", "bark/weirwood/side"});

        // Standalone door blocks
        DoorBlockExport doorExporter = new DoorBlockExport();
        doorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.WHITE_WOOD_DOOR, 
            new String[]{"wood/white/door_top", "wood/white/door_bottom"});
        doorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.LOCKED_WHITE_WOOD_DOOR, 
            new String[]{"wood/white/door_locked_top", "wood/white/door_locked_bottom"});
        doorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.NORTHERN_WOOD_DOOR, 
            new String[]{"wood/northern/door_top", "wood/northern/door_bottom"});
        doorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.SPRUCE_DOOR, 
            new String[]{"wood/spruce/door_top", "wood/spruce/door_bottom"});
        doorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.OAK_DOOR, 
            new String[]{"wood/oak/door_top", "wood/oak/door_bottom"});
        doorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.BIRCH_DOOR, 
            new String[]{"wood/birch/door_top", "wood/birch/door_bottom"});
        doorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.EYRIE_WEIRWOOD_DOOR, 
            new String[]{"door_block/door_weirwood_top", "door_block/door_weirwood_bottom"});
        doorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.GREY_WOOD_DOOR, 
            new String[]{"wood/grey/door_top", "wood/grey/door_bottom"});
        doorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.JUNGLE_DOOR, 
            new String[]{"wood/jungle/door_top", "wood/jungle/door_bottom"});
        doorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.RED_KEEP_SECRET_DOOR, 
            new String[]{"ashlar_third/pale_red/all_noctm", "ashlar_third/pale_red/all_noctm"});
        doorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.HARRENHAL_SECRET_DOOR, 
            new String[]{"ashlar_third/black/all_noctm", "ashlar_third/black/all_noctm"});
        doorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.LOCKED_BIRCH_DOOR, 
            new String[]{"wood/birch/door_locked_top", "wood/birch/door_locked_bottom"});
        doorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.LOCKED_DARK_NORTHERN_WOOD_DOOR, 
            new String[]{"wood/northern/door_locked_top", "wood/northern/door_locked_bottom"});
        doorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.LOCKED_GREY_WOOD_DOOR, 
            new String[]{"wood/grey/door_locked_top", "wood/grey/door_locked_bottom"});
        doorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.LOCKED_SPRUCE_DOOR, 
            new String[]{"wood/spruce/door_locked_top", "wood/spruce/door_locked_bottom"});
        doorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.LOCKED_JUNGLE_DOOR, 
            new String[]{"wood/jungle/door_locked_top", "wood/jungle/door_locked_bottom"});
        doorExporter.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.LOCKED_OAK_DOOR, 
            new String[]{"wood/oak/door_locked_top", "wood/oak/door_locked_bottom"});


        // Torch
        TorchBlockExport.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.TORCH, 
            Registries.BLOCK.get(WesterosBlocks.id("wall_torch")), "lighting/torch");
        TorchBlockExport.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.TORCH_UNLIT, 
            Registries.BLOCK.get(WesterosBlocks.id("wall_torch_unlit")), "lighting/torch_unlit");
        TorchBlockExport.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.CANDLE, 
            Registries.BLOCK.get(WesterosBlocks.id("wall_candle")), "lighting/candle");
        TorchBlockExport.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.CANDLE_UNLIT, 
            Registries.BLOCK.get(WesterosBlocks.id("wall_candle_unlit")), "lighting/candle_unlit");

        // Standalone sand blocks
        sandExport.generateBlockStateModels(blockStateModelGenerator, ModBlocks2.SAND_SKELETON, 
            "westerosblocks:block/sand_block/sand_skeleton");
    }

    public static void generateItemModels(ItemModelGenerator itemModelGenerator) {
        // Create instances of export classes
        ArrowSlitBlockExport arrowSlitExport = new ArrowSlitBlockExport();
        TableBlockExport tableExport = new TableBlockExport();
        StandaloneSandBlockExport sandExport = new StandaloneSandBlockExport();

        // Use the new dynamic model provider for arrow slits
        arrowSlitExport.generateItemModels(itemModelGenerator, ModBlocks2.ARBOR_BRICK_ARROW_SLIT);
        arrowSlitExport.generateItemModels(itemModelGenerator, ModBlocks2.BLACK_GRANITE_ARROW_SLIT);

        // Use the new dynamic model provider for tables
        tableExport.generateItemModels(itemModelGenerator, ModBlocks2.OAK_TABLE);
        tableExport.generateItemModels(itemModelGenerator, ModBlocks2.BIRCH_TABLE);
        tableExport.generateItemModels(itemModelGenerator, ModBlocks2.SPRUCE_TABLE);

        // Use the new dynamic model provider for chairs
        ChairBlockExport.generateItemModels(itemModelGenerator, ModBlocks2.OAK_CHAIR);
        ChairBlockExport.generateItemModels(itemModelGenerator, ModBlocks2.BIRCH_CHAIR);
        ChairBlockExport.generateItemModels(itemModelGenerator, ModBlocks2.SPRUCE_CHAIR);

        // Use the new dynamic model provider for way signs
        WaySignBlockExport.generateItemModels(itemModelGenerator, ModBlocks2.OAK_WAY_SIGN);

        // Use the new dynamic model provider for trap doors
        TrapDoorBlockExport.generateItemModels(itemModelGenerator, ModBlocks2.KINGS_LANDING_SEWER_MANHOLE);
        TrapDoorBlockExport.generateItemModels(itemModelGenerator, ModBlocks2.OLDTOWN_SEWER_MANHOLE);
        TrapDoorBlockExport.generateItemModels(itemModelGenerator, ModBlocks2.SEWER_MANHOLE);
        TrapDoorBlockExport.generateItemModels(itemModelGenerator, ModBlocks2.WHITE_HARBOR_SEWER_MANHOLE);

        // Use the new dynamic model provider for standalone log blocks
        LogBlockExport.generateItemModels(itemModelGenerator, ModBlocks2.OAK_LOG_CHAIN);
        LogBlockExport.generateItemModels(itemModelGenerator, ModBlocks2.JUNGLE_LOG_CHAIN);
        LogBlockExport.generateItemModels(itemModelGenerator, ModBlocks2.JUNGLE_LOG_ROPE);
        LogBlockExport.generateItemModels(itemModelGenerator, ModBlocks2.ARCHERY_TARGET);
        LogBlockExport.generateItemModels(itemModelGenerator, ModBlocks2.CLOSED_BARREL);
        LogBlockExport.generateItemModels(itemModelGenerator, ModBlocks2.FIREWOOD);
        LogBlockExport.generateItemModels(itemModelGenerator, ModBlocks2.MARBLE_PILLAR);
        LogBlockExport.generateItemModels(itemModelGenerator, ModBlocks2.MARBLE_PILLAR_VERTICAL_CTM);
        LogBlockExport.generateItemModels(itemModelGenerator, ModBlocks2.MOSSY_OAK_LOG);
        LogBlockExport.generateItemModels(itemModelGenerator, ModBlocks2.MOSSY_BIRCH_LOG);
        LogBlockExport.generateItemModels(itemModelGenerator, ModBlocks2.MOSSY_SPRUCE_LOG);
        LogBlockExport.generateItemModels(itemModelGenerator, ModBlocks2.MOSSY_JUNGLE_LOG);
        LogBlockExport.generateItemModels(itemModelGenerator, ModBlocks2.OAK_LOG_ROPE);
        LogBlockExport.generateItemModels(itemModelGenerator, ModBlocks2.PALM_TREE_LOG);
        LogBlockExport.generateItemModels(itemModelGenerator, ModBlocks2.SANDSTONE_PILLAR);
        LogBlockExport.generateItemModels(itemModelGenerator, ModBlocks2.SPRUCE_LOG_CHAIN);
        LogBlockExport.generateItemModels(itemModelGenerator, ModBlocks2.SPRUCE_LOG_ROPE);
        LogBlockExport.generateItemModels(itemModelGenerator, ModBlocks2.STACKED_BONES);
        LogBlockExport.generateItemModels(itemModelGenerator, ModBlocks2.WEIRWOOD_FACE_0);
        LogBlockExport.generateItemModels(itemModelGenerator, ModBlocks2.WEIRWOOD_FACE_1);
        LogBlockExport.generateItemModels(itemModelGenerator, ModBlocks2.WEIRWOOD_FACE_2);
        LogBlockExport.generateItemModels(itemModelGenerator, ModBlocks2.WEIRWOOD_FACE_3);
        LogBlockExport.generateItemModels(itemModelGenerator, ModBlocks2.WEIRWOOD_FACE_4);
        LogBlockExport.generateItemModels(itemModelGenerator, ModBlocks2.WEIRWOOD_FACE_5);
        LogBlockExport.generateItemModels(itemModelGenerator, ModBlocks2.WEIRWOOD_FACE_6);
        LogBlockExport.generateItemModels(itemModelGenerator, ModBlocks2.WEIRWOOD_FACE_7);
        LogBlockExport.generateItemModels(itemModelGenerator, ModBlocks2.WEIRWOOD_FACE_8);
        LogBlockExport.generateItemModels(itemModelGenerator, ModBlocks2.WEIRWOOD_SCARS);

        // Standalone torch item models
        TorchBlockExport.generateItemModels(itemModelGenerator, ModBlocks2.TORCH,
            net.minecraft.registry.Registries.BLOCK.get(WesterosBlocks.id("wall_torch")));
        TorchBlockExport.generateItemModels(itemModelGenerator, ModBlocks2.TORCH_UNLIT,
            net.minecraft.registry.Registries.BLOCK.get(WesterosBlocks.id("wall_torch_unlit")));
        TorchBlockExport.generateItemModels(itemModelGenerator, ModBlocks2.CANDLE,
            net.minecraft.registry.Registries.BLOCK.get(WesterosBlocks.id("wall_candle")));
        TorchBlockExport.generateItemModels(itemModelGenerator, ModBlocks2.CANDLE_UNLIT,
            net.minecraft.registry.Registries.BLOCK.get(WesterosBlocks.id("wall_candle_unlit")));

        // Standalone door item models
        DoorBlockExport doorExporter = new DoorBlockExport();
        // Use custom item texture for white wood door
        doorExporter.generateItemModels(itemModelGenerator, ModBlocks2.WHITE_WOOD_DOOR, "westerosblocks:item/white_door");
        doorExporter.generateItemModels(itemModelGenerator, ModBlocks2.LOCKED_WHITE_WOOD_DOOR, "westerosblocks:item/white_door");
        doorExporter.generateItemModels(itemModelGenerator, ModBlocks2.NORTHERN_WOOD_DOOR, "wood/northern/door_top");
        doorExporter.generateItemModels(itemModelGenerator, ModBlocks2.SPRUCE_DOOR, "wood/spruce/door_top");
        doorExporter.generateItemModels(itemModelGenerator, ModBlocks2.OAK_DOOR, "wood/oak/door_top");
        doorExporter.generateItemModels(itemModelGenerator, ModBlocks2.BIRCH_DOOR, "wood/birch/door_top");
        doorExporter.generateItemModels(itemModelGenerator, ModBlocks2.EYRIE_WEIRWOOD_DOOR, "westerosblocks:item/moon_door");
        doorExporter.generateItemModels(itemModelGenerator, ModBlocks2.GREY_WOOD_DOOR, "wood/grey/door_top");
        doorExporter.generateItemModels(itemModelGenerator, ModBlocks2.JUNGLE_DOOR, "wood/jungle/door_top");
        doorExporter.generateItemModels(itemModelGenerator, ModBlocks2.RED_KEEP_SECRET_DOOR, "ashlar_third/pale_red/all_noctm");
        doorExporter.generateItemModels(itemModelGenerator, ModBlocks2.HARRENHAL_SECRET_DOOR, "ashlar_third/black/all_noctm");
        doorExporter.generateItemModels(itemModelGenerator, ModBlocks2.LOCKED_BIRCH_DOOR, "wood/birch/door_locked_top");
        doorExporter.generateItemModels(itemModelGenerator, ModBlocks2.LOCKED_DARK_NORTHERN_WOOD_DOOR, "wood/northern/door_locked_top");
        doorExporter.generateItemModels(itemModelGenerator, ModBlocks2.LOCKED_GREY_WOOD_DOOR, "wood/grey/door_locked_top");
        doorExporter.generateItemModels(itemModelGenerator, ModBlocks2.LOCKED_SPRUCE_DOOR, "wood/spruce/door_locked_top");
        doorExporter.generateItemModels(itemModelGenerator, ModBlocks2.LOCKED_JUNGLE_DOOR, "wood/jungle/door_locked_top");
        doorExporter.generateItemModels(itemModelGenerator, ModBlocks2.LOCKED_OAK_DOOR, "wood/oak/door_locked_top");

        // Standalone sand item models
        sandExport.generateItemModels(itemModelGenerator, ModBlocks2.SAND_SKELETON);
    }
} 