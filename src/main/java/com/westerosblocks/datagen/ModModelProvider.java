package com.westerosblocks.datagen;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.WesterosBlocksDefLoader;
import com.westerosblocks.block.ModBlocks;
import com.westerosblocks.block.ModBlock;
import com.westerosblocks.datagen.models.*;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;

import java.util.Map;

public class ModModelProvider extends FabricModelProvider {
    private static final Map<String, BlockExportFactory> BLOCK_EXPORTERS = Map.ofEntries(
            Map.entry("soulsand", SolidBlockExport::new),
            Map.entry("sand", SolidBlockExport::new),
            Map.entry("solid", SolidBlockExport::new),
            Map.entry("sound", SoundBlockExport::new),
            Map.entry("stair", StairBlockExport::new),
            Map.entry("leaves", LeavesBlockExport::new),
            Map.entry("log", LogBlockExport::new),
            Map.entry("plant", CrossBlockExport::new),
            Map.entry("web", CrossBlockExport::new),
            Map.entry("pane", PaneBlockExport::new),
            Map.entry("crop", CropBlockExport::new),
            Map.entry("door", DoorBlockExport::new),
            Map.entry("slab", SlabBlockExport::new),
            Map.entry("fence", FenceBlockExport::new),
            Map.entry("wall", WallBlockExport::new),
            Map.entry("trapdoor", TrapDoorBlockExport::new),
            Map.entry("torch", TorchBlockExport::new),
            Map.entry("fan", FanBlockExport::new),
            Map.entry("ladder", LadderBlockExport::new),
            Map.entry("fire", FireBlockExport::new),
            Map.entry("bed", BedBlockExport::new),
            Map.entry("cuboid", CuboidBlockExport::new),
            Map.entry("beacon", CuboidBlockExport::new),
            Map.entry("cuboid-ne", CuboidNEBlockExport::new),
            Map.entry("cuboid-nsew", CuboidNSEWBlockExport::new),
            Map.entry("cuboid-16way", Cuboid16WayBlockExport::new),
            Map.entry("cuboid-8way", Cuboid8WayBlockExport::new),
            Map.entry("cuboid-wall-16way", CuboidWall16WayBlockExport::new),
            Map.entry("cuboid-nsew-stack", CuboidNSEWStackBlockExport::new),
            Map.entry("cuboid-nsewud", CuboidNSEWUDBlockExport::new),
            Map.entry("layer", LayerBlockExport::new),
            Map.entry("rail", RailBlockExport::new),
            Map.entry("halfdoor", HalfDoorBlockExport::new),
            Map.entry("cake", CakeBlockExport::new),
            Map.entry("furnace", FurnaceBlockExport::new),
            Map.entry("vines", VinesBlockExport::new),
            Map.entry("flowerpot", FlowerPotExport::new),
            Map.entry("fencegate", FenceGateBlockExport::new),
            Map.entry("particle", ParticleEmitterBlockExport::new),
            Map.entry("cuboid-vertical-8way", CuboidVertical8WayBlockExport::new)
    );

    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        // Use the new dynamic model provider for arrow slits
        ArrowSlitBlockExport.generateBlockStateModels(blockStateModelGenerator, ModBlocks.ARBOR_BRICK_ARROW_SLIT,
            "westerosblocks:block/ashlar_third/arbor/all");
        ArrowSlitBlockExport.generateBlockStateModels(blockStateModelGenerator, ModBlocks.BLACK_GRANITE_ARROW_SLIT,
            "westerosblocks:block/ashlar_third/black/all");

        // Use the new dynamic model provider for tables
        TableBlockExport.generateBlockStateModels(blockStateModelGenerator, ModBlocks.OAK_TABLE,
            "westerosblocks:block/wood/oak/all");
        TableBlockExport.generateBlockStateModels(blockStateModelGenerator, ModBlocks.BIRCH_TABLE,
            "westerosblocks:block/wood/birch/all");
        TableBlockExport.generateBlockStateModels(blockStateModelGenerator, ModBlocks.SPRUCE_TABLE,
            "westerosblocks:block/wood/spruce/all");

        // Handle other blocks from definition files
        Map<String, Block> customBlocks = ModBlocks.getCustomBlocks();
        ModBlock[] customBlockDefs = WesterosBlocksDefLoader.getCustomBlockDefs();

        for (ModBlock customBlockDef : customBlockDefs) {
            if (customBlockDef == null || customBlockDef.getBlockName() == null) continue;

            Block currentBlock = customBlocks.get(customBlockDef.getBlockName());
            if (currentBlock == null) continue;

            try {
                BlockExportFactory factory = BLOCK_EXPORTERS.get(customBlockDef.blockType);
                if (factory != null) {
                    ModelExport exporter = factory.create(blockStateModelGenerator, currentBlock, customBlockDef);
                    exporter.generateBlockStateModels();
                } else {
                    WesterosBlocks.LOGGER.warn("DATAGEN: Unknown block type: {} for block {}",
                            customBlockDef.blockType, customBlockDef.blockName);
                }
            } catch (Exception e) {
                WesterosBlocks.LOGGER.error("Error generating models for {}: {}",
                        customBlockDef.blockName, e.getMessage(), e);
            }
        }
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        // Use the new dynamic model provider for arrow slits
        ArrowSlitBlockExport.generateItemModels(itemModelGenerator, ModBlocks.ARBOR_BRICK_ARROW_SLIT);
        ArrowSlitBlockExport.generateItemModels(itemModelGenerator, ModBlocks.BLACK_GRANITE_ARROW_SLIT);

        // Use the new dynamic model provider for tables
        TableBlockExport.generateItemModels(itemModelGenerator, ModBlocks.OAK_TABLE);
        TableBlockExport.generateItemModels(itemModelGenerator, ModBlocks.BIRCH_TABLE);
        TableBlockExport.generateItemModels(itemModelGenerator, ModBlocks.SPRUCE_TABLE);

        // Handle other blocks from definition files
        Map<String, Block> customBlocks = ModBlocks.getCustomBlocks();
        ModBlock[] customBlockDefs = WesterosBlocksDefLoader.getCustomBlockDefs();

        for (ModBlock customBlockDef : customBlockDefs) {
            if (customBlockDef == null || customBlockDef.getBlockName() == null) {
                WesterosBlocks.LOGGER.warn("Skipping null item definition for item name: {}", customBlockDef);
                continue;
            }
            Block currentBlock = customBlocks.get(customBlockDef.getBlockName());
            switch (customBlockDef.blockType) {
                case "soulsand":
                case "sand":
                case "solid": {
                    SolidBlockExport.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
                    break;
                }
                case "stair": {
                    StairBlockExport.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
                    break;
                }
                case "slab": {
                    SlabBlockExport.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
                    break;
                }
                case "ladder": {
                    LadderBlockExport.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
                    break;
                }
                case "log": {
                    LogBlockExport.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
                    break;
                }
                case "rail": {
                    RailBlockExport.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
                    break;
                }
                case "trapdoor": {
                    TrapDoorBlockExport.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
                    break;
                }
                case "torch": {
                    TorchBlockExport.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
                    break;
                }
                case "leaves": {
                    LeavesBlockExport.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
                    break;
                }
                case "crop": {
                    CropBlockExport.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
                    break;
                }
                case "plant":
                case "web": {
                    CrossBlockExport.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
                    break;
                }
                case "fencegate": {
                    FenceGateBlockExport.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
                    break;
                }
                case "vines": {
                    VinesBlockExport.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
                    break;
                }
                case "bed": {
                    BedBlockExport.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
                    break;
                }
                case "door": {
                    DoorBlockExport.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
                    break;
                }
                case "fan": {
                    FanBlockExport.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
                    break;
                }
                case "fire": {
                    FireBlockExport.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
                    break;
                }
                case "layer": {
                    LayerBlockExport.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
                    break;
                }
                case "pane": {
                    PaneBlockExport.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
                    break;
                }
                case "halfdoor": {
                    HalfDoorBlockExport.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
                    break;
                }
                case "cuboid-16way": {
                    Cuboid16WayBlockExport.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
                    break;
                }
                case "cuboid-8way": {
                    Cuboid8WayBlockExport.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
                    break;
                }
//                case "cuboid-wall-16way": {
//                    CuboidWall16WayBlockExport.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
//                    break;
//                }
                case "flowerpot": {
                    FlowerPotExport.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
                    break;
                }
                case "fence": {
                    FenceBlockExport.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
                    break;
                }
                case "wall": {
                    WallBlockExport.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
                    break;
                }
                case "beacon":
                case "cuboid": {
                    CuboidBlockExport.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
                    break;
                }
                case "cuboid-ne": {
                    CuboidNEBlockExport.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
                    break;
                }
                case "cuboid-nsew": {
                    CuboidNSEWBlockExport.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
                    break;
                }
                case "cuboid-nsew-stack": {
                    CuboidNSEWStackBlockExport.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
                    break;
                }
                case "cuboid-nsewud": {
                    CuboidNSEWUDBlockExport.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
                    break;
                }
                case "cake": {
                    CakeBlockExport.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
                    break;
                }
                case "furnace": {
                    FurnaceBlockExport.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
                    break;
                }
                case "sound": {
                    SoundBlockExport.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
                    break;
                }
                case "particle": {
                    ParticleEmitterBlockExport.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
                    break;
                }
                case "cuboid-vertical-8way": {
                    CuboidVertical8WayBlockExport.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
                    break;
                }
                // case "arrow-slit": {
                //     ArrowSlitModelProvider.generateItemModels(itemModelGenerator, currentBlock);
                //     break;
                // }
                default:
                    WesterosBlocks.LOGGER.warn("DATAGEN: Unknown item type: {} for item {}", customBlockDef.blockType, customBlockDef.blockName);
            }
        }
    }
}
