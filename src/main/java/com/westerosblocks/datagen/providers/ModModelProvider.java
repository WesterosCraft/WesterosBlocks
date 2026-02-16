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

import java.util.Map;

public class ModModelProvider extends FabricModelProvider {

    @FunctionalInterface
    private interface BlockExporter {
        void export(BlockStateModelGenerator generator, Block block, BlockDefinition definition);
    }

    private static final Map<String, BlockExporter> EXPORTERS = Map.ofEntries(
        Map.entry("solid", SolidBlockExporter::registerCustomSolidBlock),
        Map.entry("sand", SolidBlockExporter::registerCustomSolidBlock),
        Map.entry("door", DoorBlockExporter::registerCustomDoorBlock),
        Map.entry("trapdoor", TrapDoorBlockExporter::registerCustomTrapDoorBlock),
        Map.entry("log", LogBlockExporter::registerCustomLogBlock),
        Map.entry("flowerpot", FlowerPotBlockExporter::registerCustomFlowerPotBlock),
        Map.entry("web", CrossBlockExporter::registerCustomCrossBlock),
        Map.entry("plant", CrossBlockExporter::registerCustomCrossBlock),
        Map.entry("slab", SlabBlockExporter::registerCustomSlabBlock),
        Map.entry("halfdoor", HalfDoorBlockExporter::registerCustomHalfDoorBlock),
        Map.entry("fire", FireBlockExporter::registerCustomFireBlock),
        Map.entry("ladder", LadderBlockExporter::registerCustomLadderBlock),
        Map.entry("vines", VinesBlockExporter::registerCustomVinesBlock),
        Map.entry("pane", PaneBlockExporter::registerCustomPaneBlock),
        Map.entry("fence", FenceBlockExporter::registerCustomFenceBlock),
        Map.entry("fencegate", FenceGateBlockExporter::registerCustomFenceGateBlock),
        Map.entry("leaves", LeavesBlockExporter::registerCustomLeavesBlock),
        Map.entry("bed", BedBlockExporter::registerCustomBedBlock),
        Map.entry("table", TableBlockExporter::registerTableBlock2),
        Map.entry("bench", BenchBlockExporter::registerCustomBenchBlock),
        Map.entry("crop", CropBlockExporter::registerCustomCropBlock),
        Map.entry("torch", TorchBlockExporter::registerTorchBlockFromDefinition),
        Map.entry("fan", FanBlockExporter::registerFanBlockFromDefinition),
        Map.entry("rail", RailBlockExporter::registerRailBlockFromDefinition),
        Map.entry("furnace", FurnaceBlockExporter::registerCustomFurnaceBlock),
        Map.entry("wall", WallBlockExporter::registerCustomWallBlock),
        Map.entry("cuboid", CuboidBlockExporter::registerCustomCuboidBlock),
        Map.entry("beacon", CuboidBlockExporter::registerCustomCuboidBlock),
        Map.entry("cuboid-nsew", CuboidNSEWBlockExporter::registerCustomCuboidNSEWBlock),
        Map.entry("cuboid-nsew-stack", CuboidNSEWStackBlockExporter::registerCustomCuboidNSEWStackBlock),
        Map.entry("cuboid-ne", CuboidNEBlockExporter::registerCustomCuboidNEBlock),
        Map.entry("cuboid-nsewud", CuboidNSEWUDBlockExporter::registerCustomCuboidNSEWUDBlock),
        Map.entry("cuboid-16way", Cuboid16WayBlockExporter::registerCustomCuboid16WayBlock),
        Map.entry("stair", StairBlockExporter::registerCustomStairBlock),
        Map.entry("layer", LayerBlockExporter::registerCustomLayerBlock),
        Map.entry("particle", ParticleEmitterExporter::registerCustomParticleEmitterBlock),
        Map.entry("chair", ChairBlockExporter::registerChairBlock),
        Map.entry("mounted", MountedBlockExporter::registerMountedBlock),
        Map.entry("flowerbed", FlowerbedBlockExporter::registerCustomFlowerbedBlock),
        Map.entry("awning", AwningBlockExporter::registerCustomAwningBlock),
        Map.entry("bigdoor", BigDoorBlockExporter::registerCustomBigDoorBlock),
        Map.entry("bunting", BuntingBlockExporter::registerBuntingBlock)
    );

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
        BlockExporter exporter = EXPORTERS.get(definition.getBlockType().toLowerCase());
        if (exporter != null) {
            try {
                exporter.export(bsmg, block, definition);
            } catch (Exception e) {
                WesterosBlocks.LOGGER.error("Error generating model for block '{}': {}",
                        definition.getBlockName(), e.getMessage());
            }
        } else {
            WesterosBlocks.LOGGER.warn("Unsupported block type '{}' for model generation: {}",
                    definition.getBlockType(), definition.getBlockName());
        }
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        // Item models are now handled automatically by the block exporters
    }
}
