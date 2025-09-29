package com.westerosblocks.datagen.providers;

import com.westerosblocks.WesterosBlocks;
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

        // Chair Blocks
        registerCustomChairBlock(bsmg, ModBlocks.OAK_CHAIR).texture("bark/oak/side").build();

        // Arrow Slit Blocks
        registerCustomArrowSlitBlock(bsmg, ModBlocks.ARBOR_BRICK_ARROW_SLIT).texture("ashlar_third/arbor/all").build();

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

        // Generate models for all block definitions in a single loop
        for (BlockDefinition definition : registry.getAllDefinitions()) {
            Block block = ModBlocks.getAutoRegisteredBlock(definition.getBlockName());
            if (block != null) {
                generateModelFromDefinition(bsmg, block, definition);
            }
        }
    }

    /**
     * Generates models for a block from its definition using the appropriate exporter
     */
    private void generateModelFromDefinition(BlockStateModelGenerator bsmg, Block block, BlockDefinition definition) {
        String blockType = definition.getBlockType();

        try {
            switch (blockType.toLowerCase()) {
                case "solid":
                    SolidBlockExporter.registerCustomSolidBlock(bsmg, block, definition);
                    break;

                case "door":
                    DoorBlockExporter.registerCustomDoorBlock(bsmg, block, definition);
                    break;

                case "log":
                    LogBlockExporter.registerCustomLogBlock(bsmg, block, definition);
                    break;

                case "plant":
                    PlantBlockExporter.registerCustomPlantBlock(bsmg, block, definition);
                    break;

                case "flowerpot":
                    FlowerPotBlockDatagen.registerCustomFlowerPotBlock(bsmg, block, definition);
                    break;

                case "web":
                    CrossBlockExporter.registerCrossBlockFromDefinition(bsmg, block, definition);
                    break;

                case "slab":
                    SlabBlockExporter.registerCustomSlabBlock(bsmg, block, definition);
                    break;

                case "halfdoor":
                    HalfDoorBlockExporter.registerCustomHalfDoorBlock(bsmg, block, definition);
                    break;

                case "fire":
                    FireBlockDatagen.registerCustomFireBlock(bsmg, block, definition);
                    break;

                case "ladder":
                    LadderBlockDatagen.registerCustomLadderBlock(bsmg, block, definition);
                    break;

                case "vines":
                    VinesBlockDatagen.registerCustomVinesBlock(bsmg, block, definition);
                    break;

                case "pane":
                    PaneBlockExporter.registerCustomPaneBlock(bsmg, block, definition);
                    break;

                case "fence":
                    FenceBlockDatagen.registerCustomFenceBlock(bsmg, block, definition);
                    break;

                case "fencegate":
                    FenceGateBlockDatagen.registerCustomFenceGateBlock(bsmg, block, definition);
                    break;

                case "leaves":
                    LeavesBlockDatagen.registerCustomLeavesBlock(bsmg, block, definition);
                    break;

                case "bed":
                    BedBlockDatagen.registerCustomBedBlock(bsmg, block, definition);
                    break;

                case "crop":
                    CropBlockDatagen.registerCustomCropBlock(bsmg, block, definition);
                    break;

                case "torch":
                    TorchBlockExporter.registerTorchBlockFromDefinition(bsmg, block, definition);
                    break;

                case "fan":
                    FanBlockExporter.registerFanBlockFromDefinition(bsmg, block, definition);
                    break;

                case "rail":
                    RailBlockExporter.registerRailBlockFromDefinition(bsmg, block, definition);
                    break;

                case "furnace":
                    FurnaceBlockDatagen.registerCustomFurnaceBlock(bsmg, block, definition);
                    break;

                case "wall":
                    WallBlockDatagen.registerCustomWallBlock(bsmg, block, definition);
                    break;

                default:
                    WesterosBlocks.LOGGER.warn("Unsupported block type '{}' for model generation: {}",
                            blockType, definition.getBlockName());
                    break;
            }
        } catch (Exception e) {
            WesterosBlocks.LOGGER.error("Error generating model for block '{}': {}",
                    definition.getBlockName(), e.getMessage());
        }
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        // Item models are now handled automatically by the block exporters
    }
}
