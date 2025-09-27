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
//        registerCustomTorchBlock(bsmg, ModBlocks.TORCH).texture("lighting/torch").build();
//        registerCustomTorchBlock(bsmg, ModBlocks.TORCH_UNLIT).texture("lighting/torch_unlit").build();
//        registerCustomTorchBlock(bsmg, ModBlocks.CANDLE).texture("lighting/candle").build();
//        registerCustomTorchBlock(bsmg, ModBlocks.CANDLE_UNLIT).texture("lighting/candle_unlit").build();

        // Chair Blocks
        registerCustomChairBlock(bsmg, ModBlocks.OAK_CHAIR).texture("bark/oak/side").build();

        // Arrow Slit Blocks
        registerCustomArrowSlitBlock(bsmg, ModBlocks.ARBOR_BRICK_ARROW_SLIT).texture("ashlar_third/arbor/all").build();

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

        // Generate crop block models
        for (BlockDefinition definition : registry.getByType("crop")) {
            Block block = ModBlocks.getAutoRegisteredBlock(definition.getBlockName());
            if (block != null) {
                CropBlockDatagen.registerCustomCropBlock(bsmg, block, definition);
            }
        }

        // Generate torch block models
        for (BlockDefinition definition : registry.getByType("torch")) {
            Block block = ModBlocks.getAutoRegisteredBlock(definition.getBlockName());
            if (block != null) {
                TorchBlockExporter.registerTorchBlockFromDefinition(bsmg, block, definition);
            }
        }

        // Generate fan block models
        for (BlockDefinition definition : registry.getByType("fan")) {
            Block block = ModBlocks.getAutoRegisteredBlock(definition.getBlockName());
            if (block != null) {
                FanBlockExporter.registerFanBlockFromDefinition(bsmg, block, definition);
            }
        }

        // TODO: Add other block types as needed
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        // Item models are now handled automatically by the block exporters
    }
}
