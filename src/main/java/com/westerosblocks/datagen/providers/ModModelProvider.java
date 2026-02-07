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

public class ModModelProvider extends FabricModelProvider {

    private final FabricDataOutput output;

    public ModModelProvider(FabricDataOutput output) {
        super(output);
        this.output = output;
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator bsmg) {
        generateModelsFromDefinitions(bsmg);
    }

    private void generateModelsFromDefinitions(BlockStateModelGenerator bsmg) {
        BlockDefinitionRegistry registry = BlockDefinitionRegistry.getInstance();

        if (!registry.isInitialized()) {
            return;
        }

        // Generate models for all block definitions
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
                case "sand":
                    SolidBlockExporter.registerCustomSolidBlock(bsmg, block, definition);
                    break;

                case "door":
                    DoorBlockExporter.registerCustomDoorBlock(bsmg, block, definition);
                    break;

                case "trapdoor":
                    TrapDoorBlockExporter.registerCustomTrapDoorBlock(bsmg, block, definition);
                    break;

                case "log":
                    LogBlockExporter.registerCustomLogBlock(bsmg, block, definition);
                    break;

                case "flowerpot":
                    FlowerPotBlockExporter.registerCustomFlowerPotBlock(bsmg, block, definition);
                    break;

                case "web":
                case "plant":
                    CrossBlockExporter.registerCustomCrossBlock(bsmg, block, definition);
                    break;

                case "slab":
                    SlabBlockExporter.registerCustomSlabBlock(bsmg, block, definition);
                    break;

                case "halfdoor":
                    HalfDoorBlockExporter.registerCustomHalfDoorBlock(bsmg, block, definition);
                    break;

                case "fire":
                    FireBlockExporter.registerCustomFireBlock(bsmg, block, definition);
                    break;

                case "ladder":
                    LadderBlockExporter.registerCustomLadderBlock(bsmg, block, definition);
                    break;

                case "vines":
                    VinesBlockExporter.registerCustomVinesBlock(bsmg, block, definition);
                    break;

                case "pane":
                    PaneBlockExporter.registerCustomPaneBlock(bsmg, block, definition);
                    break;

                case "fence":
                    FenceBlockExporter.registerCustomFenceBlock(bsmg, block, definition);
                    break;

                case "fencegate":
                    FenceGateBlockExporter.registerCustomFenceGateBlock(bsmg, block, definition);
                    break;

                case "leaves":
                    LeavesBlockExporter.registerCustomLeavesBlock(bsmg, block, definition);
                    break;

                case "bed":
                    BedBlockExporter.registerCustomBedBlock(bsmg, block, definition);
                    break;

                case "table":
                    TableBlockExporter.registerTableBlock2(bsmg, block, definition);
                    break;

                case "bench":
                    BenchBlockExporter.registerCustomBenchBlock(bsmg, block, definition);
                    break;

                case "crop":
                    CropBlockExporter.registerCustomCropBlock(bsmg, block, definition);
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
                    FurnaceBlockExporter.registerCustomFurnaceBlock(bsmg, block, definition);
                    break;

                case "wall":
                    WallBlockExporter.registerCustomWallBlock(bsmg, block, definition);
                    break;

                case "cuboid",
                     "beacon":
                    CuboidBlockExporter.registerCustomCuboidBlock(bsmg, block, definition);
                    break;

                case "cuboid-nsew":
                    CuboidNSEWBlockExporter.registerCustomCuboidNSEWBlock(bsmg, block, definition);
                    break;

                case "cuboid-nsew-stack":
                    CuboidNSEWStackBlockExporter.registerCustomCuboidNSEWStackBlock(bsmg, block, definition);
                    break;

                case "cuboid-ne":
                    CuboidNEBlockExporter.registerCustomCuboidNEBlock(bsmg, block, definition);
                    break;

                case "cuboid-nsewud":
                    CuboidNSEWUDBlockExporter.registerCustomCuboidNSEWUDBlock(bsmg, block, definition);
                    break;

                case "cuboid-16way":
                    Cuboid16WayBlockExporter.registerCustomCuboid16WayBlock(bsmg, block, definition);
                    break;

                case "stair":
                    StairBlockExporter.registerCustomStairBlock(bsmg, block, definition);
                    break;

                case "layer":
                    LayerBlockExporter.registerCustomLayerBlock(bsmg, block, definition);
                    break;

                case "particle":
                    ParticleEmitterExporter.registerCustomParticleEmitterBlock(bsmg, block, definition);
                    break;

                case "branch":
                    BranchBlockExporter.registerCustomBranchBlock(bsmg, block, definition);
                    break;

                case "chair":
                    ChairBlockExporter.registerChairBlock(bsmg, block, definition);
                    break;

                case "mounted":
                    MountedBlockExporter.registerMountedBlock(bsmg, block, definition);
                    break;

                case "flowerbed":
                    String[] flowerTextures = definition.getTexturesAsArray();
                    String stemTex = flowerTextures.length > 0 ? flowerTextures[0] : "";
                    String flowerTex = flowerTextures.length > 1 ? flowerTextures[1] : "";
                    FlowerbedBlockExporter.registerCustomFlowerbedBlock(bsmg, block)
                        .stemTexture(stemTex)
                        .flowerTexture(flowerTex)
                        .build();
                    break;

                case "awning":
                    AwningBlockExporter.registerCustomAwningBlock(bsmg, block, definition);
                    break;

                case "bigdoor":
                    BigDoorBlockExporter.registerCustomBigDoorBlock(bsmg, block, definition);
                    break;

                case "bunting":
                    BuntingBlockExporter.registerBuntingBlock(bsmg, block, definition);
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
