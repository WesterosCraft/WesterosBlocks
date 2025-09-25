package com.westerosblocks.datagen.providers;

import com.westerosblocks.block.ModBlocks;

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

        // Table Blocks
        registerCustomTableBlock(bsmg, ModBlocks.OAK_TABLE)
                .texture("wood/oak/all").build();

        // Branch Blocks
        registerCustomBranchBlock(bsmg, ModBlocks.OAK_BRANCH)
                .texture("bark/oak/side")
                .build();
        registerCustomBranchBlock(bsmg, ModBlocks.BIRCH_BRANCH)
                .texture("bark/birch/side")
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

        // Flowerbed Blocks
        registerCustomFlowerbedBlock(bsmg, ModBlocks.CLOVER)
                .stemTexture("flowerbed/clover_stem")
                .flowerTexture("flowerbed/clover")
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
//        BedBlockDatagen.generateBedBlock(bsmg, ModBlocks.ITCHY_STRAW_BED, "itchy_straw_bed")
//                .bedType("normal")
//                .texture("bed_block/bed_straw_itchy_0")
//                .texture("bed_block/bed_straw_itchy_1")
//                .texture("bed_block/bed_straw_itchy_2")
//                .texture("bed_block/bed_straw_itchy_3")
//                .texture("bed_block/bed_straw_itchy_4")
//                .texture("bed_block/bed_straw_itchy_5")
//                .build();
//
//        BedBlockDatagen.generateBedBlock(bsmg, ModBlocks.HAMMOCK, "hammock")
//                .bedType("hammock")
//                .texture("bed_block/bed_hammock_0")
//                .texture("bed_block/bed_hammock_1")
//                .texture("bed_block/bed_hammock_2")
//                .texture("bed_block/bed_hammock_3")
//                .texture("bed_block/bed_hammock_4")
//                .texture("bed_block/bed_hammock_5")
//                .build();
//
//        BedBlockDatagen.generateBedBlock(bsmg, ModBlocks.NIGHTS_WATCH_BED, "nights_watch_bed")
//                .bedType("normal")
//                .texture("bed_block/bed_night_watch_0")
//                .texture("bed_block/bed_night_watch_1")
//                .texture("bed_block/bed_night_watch_2")
//                .texture("bed_block/bed_night_watch_3")
//                .texture("bed_block/bed_night_watch_4")
//                .texture("bed_block/bed_night_watch_5")
//                .build();
//
//        BedBlockDatagen.generateBedBlock(bsmg, ModBlocks.NOBLE_BLUE_BED, "noble_blue_bed")
//                .bedType("raised")
//                .texture("bed_block/bed_noble_blue_0")
//                .texture("bed_block/bed_noble_blue_1")
//                .texture("bed_block/bed_noble_blue_2")
//                .texture("bed_block/bed_noble_blue_3")
//                .texture("bed_block/bed_noble_blue_4")
//                .texture("bed_block/bed_noble_blue_5")
//                .build();
//
//        BedBlockDatagen.generateBedBlock(bsmg, ModBlocks.NOBLE_RED_BED, "noble_red_bed")
//                .bedType("raised")
//                .texture("bed_block/bed_noble_red_0")
//                .texture("bed_block/bed_noble_red_1")
//                .texture("bed_block/bed_noble_red_2")
//                .texture("bed_block/bed_noble_red_3")
//                .texture("bed_block/bed_noble_red_4")
//                .texture("bed_block/bed_noble_red_5")
//                .build();
//
//        BedBlockDatagen.generateBedBlock(bsmg, ModBlocks.NORTHERN_BED, "northern_bed")
//                .bedType("normal")
//                .texture("bed_block/bed_north_0")
//                .texture("bed_block/bed_north_1")
//                .texture("bed_block/bed_north_2")
//                .texture("bed_block/bed_north_3")
//                .texture("bed_block/bed_north_4")
//                .texture("bed_block/bed_north_5")
//                .build();
//
//        BedBlockDatagen.generateBedBlock(bsmg, ModBlocks.PALE_GREEN_BED, "pale_green_bed")
//                .bedType("normal")
//                .texture("bed_block/bed_patchy_green_0")
//                .texture("bed_block/bed_patchy_green_1")
//                .texture("bed_block/bed_patchy_green_2")
//                .texture("bed_block/bed_patchy_green_3")
//                .texture("bed_block/bed_patchy_green_4")
//                .texture("bed_block/bed_patchy_green_5")
//                .build();
//
//        BedBlockDatagen.generateBedBlock(bsmg, ModBlocks.PALE_RED_BED, "pale_red_bed")
//                .bedType("normal")
//                .texture("bed_block/bed_patchy_red_0")
//                .texture("bed_block/bed_patchy_red_1")
//                .texture("bed_block/bed_patchy_red_2")
//                .texture("bed_block/bed_patchy_red_3")
//                .texture("bed_block/bed_patchy_red_4")
//                .texture("bed_block/bed_patchy_red_5")
//                .build();
//
//        BedBlockDatagen.generateBedBlock(bsmg, ModBlocks.STRAW_BED, "straw_bed")
//                .bedType("normal")
//                .texture("bed_block/bed_straw_0")
//                .texture("bed_block/bed_straw_1")
//                .texture("bed_block/bed_straw_2")
//                .texture("bed_block/bed_straw_3")
//                .texture("bed_block/bed_straw_4")
//                .texture("bed_block/bed_straw_5")
//                .build();

        // Particle Emitter Blocks
        ParticleEmitterDatagen.registerCustomParticleEmitterBlock(bsmg, ModBlocks.CASCADE_PARTICLE_EMITTER, "cascade_particle_emitter");
        ParticleEmitterDatagen.registerCustomParticleEmitterBlock(bsmg, ModBlocks.COSY_SMOKE_PARTICLE_EMITTER, "cosy_smoke_particle_emitter");
        ParticleEmitterDatagen.registerCustomParticleEmitterBlock(bsmg, ModBlocks.SIGNAL_SMOKE_PARTICLE_EMITTER, "signal_smoke_particle_emitter");
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

        // Generate door block models
        for (BlockDefinition definition : registry.getByType("door")) {
            Block block = ModBlocks.getAutoRegisteredBlock(definition.getBlockName());
            if (block != null) {
                DoorBlockExporter.registerCustomDoorBlock(bsmg, block, definition);
            }
        }

        // Generate log block models
        for (BlockDefinition definition : registry.getByType("log")) {
            Block block = ModBlocks.getAutoRegisteredBlock(definition.getBlockName());
            if (block != null) {
                LogBlockExporter.registerCustomLogBlock(bsmg, block, definition);
            }
        }

        // Generate plant block models
        for (BlockDefinition definition : registry.getByType("plant")) {
            Block block = ModBlocks.getAutoRegisteredBlock(definition.getBlockName());
            if (block != null) {
                PlantBlockExporter.registerCustomPlantBlock(bsmg, block, definition);
            }
        }

        // Generate flowerpot block models
        for (BlockDefinition definition : registry.getByType("flowerpot")) {
            Block block = ModBlocks.getAutoRegisteredBlock(definition.getBlockName());
            if (block != null) {
                FlowerPotBlockExporter.registerCustomFlowerPotBlock(bsmg, block, definition);
            }
        }

        // Generate web block models
        for (BlockDefinition definition : registry.getByType("web")) {
            Block block = ModBlocks.getAutoRegisteredBlock(definition.getBlockName());
            if (block != null) {
                CrossBlockExporter.registerCrossBlockFromDefinition(bsmg, block, definition);
            }
        }

        // Generate slab block models
        for (BlockDefinition definition : registry.getByType("slab")) {
            Block block = ModBlocks.getAutoRegisteredBlock(definition.getBlockName());
            if (block != null) {
                SlabBlockExporter.registerCustomSlabBlock(bsmg, block, definition);
            }
        }

        // Generate halfdoor block models
        for (BlockDefinition definition : registry.getByType("halfdoor")) {
            Block block = ModBlocks.getAutoRegisteredBlock(definition.getBlockName());
            if (block != null) {
                HalfDoorBlockExporter.registerCustomHalfDoorBlock(bsmg, block, definition);
            }
        }

        // Generate fire block models
        for (BlockDefinition definition : registry.getByType("fire")) {
            Block block = ModBlocks.getAutoRegisteredBlock(definition.getBlockName());
            if (block != null) {
                FireBlockDatagen.registerCustomFireBlock(bsmg, block, definition);
            }
        }

        // Generate ladder block models
        for (BlockDefinition definition : registry.getByType("ladder")) {
            Block block = ModBlocks.getAutoRegisteredBlock(definition.getBlockName());
            if (block != null) {
                LadderBlockDatagen.registerCustomLadderBlock(bsmg, block, definition);
            }
        }

        // Generate vines block models
        for (BlockDefinition definition : registry.getByType("vines")) {
            Block block = ModBlocks.getAutoRegisteredBlock(definition.getBlockName());
            if (block != null) {
                VinesBlockDatagen.registerCustomVinesBlock(bsmg, block, definition);
            }
        }

        // Generate pane block models
        for (BlockDefinition definition : registry.getByType("pane")) {
            Block block = ModBlocks.getAutoRegisteredBlock(definition.getBlockName());
            if (block != null) {
                PaneBlockExporter.registerCustomPaneBlock(bsmg, block, definition);
            }
        }

        // Generate fence block models
        for (BlockDefinition definition : registry.getByType("fence")) {
            Block block = ModBlocks.getAutoRegisteredBlock(definition.getBlockName());
            if (block != null) {
                FenceBlockDatagen.registerCustomFenceBlock(bsmg, block, definition);
            }
        }

        // Generate fencegate block models
        for (BlockDefinition definition : registry.getByType("fencegate")) {
            Block block = ModBlocks.getAutoRegisteredBlock(definition.getBlockName());
            if (block != null) {
                FenceGateBlockDatagen.registerCustomFenceGateBlock(bsmg, block, definition);
            }
        }

        // Generate leaves block models
        for (BlockDefinition definition : registry.getByType("leaves")) {
            Block block = ModBlocks.getAutoRegisteredBlock(definition.getBlockName());
            if (block != null) {
                LeavesBlockDatagen.registerCustomLeavesBlock(bsmg, block, definition);
            }
        }

        // Generate bed block models
        for (BlockDefinition definition : registry.getByType("bed")) {
            Block block = ModBlocks.getAutoRegisteredBlock(definition.getBlockName());
            if (block != null) {
                BedBlockDatagen.registerCustomBedBlock(bsmg, block, definition);
            }
        }

        // TODO: Add other block types as needed
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        // Item models are now handled automatically by the block exporters
    }
}
