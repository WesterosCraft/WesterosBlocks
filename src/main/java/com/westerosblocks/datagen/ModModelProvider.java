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

import java.util.HashMap;
import java.util.Map;

public class ModModelProvider extends FabricModelProvider {
    private static final Map<String, BlockExportFactory> BLOCK_EXPORTERS = new HashMap<>();

    static {
        BLOCK_EXPORTERS.put("soulsand", SolidBlockExport::new);
        BLOCK_EXPORTERS.put("sand", SolidBlockExport::new);
        BLOCK_EXPORTERS.put("solid", SolidBlockExport::new);
        BLOCK_EXPORTERS.put("sound", SoundBlockExport::new);
        BLOCK_EXPORTERS.put("stair", StairBlockExport::new);
        BLOCK_EXPORTERS.put("leaves", LeavesBlockExport::new);
        BLOCK_EXPORTERS.put("log", LogBlockExport::new);
        BLOCK_EXPORTERS.put("plant", CrossBlockExport::new);
        BLOCK_EXPORTERS.put("web", CrossBlockExport::new);
        BLOCK_EXPORTERS.put("pane", PaneBlockExport::new);
        BLOCK_EXPORTERS.put("crop", CropBlockExport::new);
        BLOCK_EXPORTERS.put("door", DoorBlockExport::new);
        BLOCK_EXPORTERS.put("slab", SlabBlockExport::new);
        BLOCK_EXPORTERS.put("fence", FenceBlockExport::new);
        BLOCK_EXPORTERS.put("wall", WallBlockExport::new);
        BLOCK_EXPORTERS.put("trapdoor", TrapDoorBlockExport::new);
        BLOCK_EXPORTERS.put("torch", TorchBlockExport::new);
        BLOCK_EXPORTERS.put("fan", FanBlockExport::new);
        BLOCK_EXPORTERS.put("ladder", LadderBlockExport::new);
        BLOCK_EXPORTERS.put("fire", FireBlockExport::new);
        BLOCK_EXPORTERS.put("bed", BedBlockExport::new);
        BLOCK_EXPORTERS.put("cuboid", CuboidBlockExport::new);
        BLOCK_EXPORTERS.put("beacon", CuboidBlockExport::new);
        BLOCK_EXPORTERS.put("cuboid-ne", CuboidNEBlockExport::new);
        BLOCK_EXPORTERS.put("cuboid-nsew", CuboidNSEWBlockExport::new);
        BLOCK_EXPORTERS.put("cuboid-16way", Cuboid16WayBlockExport::new);
        BLOCK_EXPORTERS.put("cuboid-nsew-stack", CuboidNSEWStackBlockExport::new);
        BLOCK_EXPORTERS.put("cuboid-nsewud", CuboidNSEWUDBlockExport::new);
        BLOCK_EXPORTERS.put("layer", LayerBlockExport::new);
        BLOCK_EXPORTERS.put("rail", RailBlockExport::new);
        BLOCK_EXPORTERS.put("halfdoor", HalfDoorBlockExport::new);
        BLOCK_EXPORTERS.put("cake", CakeBlockExport::new);
        BLOCK_EXPORTERS.put("furnace", FurnaceBlockExport::new);
        BLOCK_EXPORTERS.put("vines", VinesBlockExport::new);
        BLOCK_EXPORTERS.put("flowerpot", FlowerPotExport::new);
        BLOCK_EXPORTERS.put("fencegate", FenceGateBlockExport::new);
    }

    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        HashMap<String, Block> customBlocks = ModBlocks.getCustomBlocksByName();
        ModBlock[] customBlockDefs = WesterosBlocksDefLoader.getCustomBlockDefs();

        for (ModBlock customBlockDef : customBlockDefs) {
            if (customBlockDef == null) continue;
            Block currentBlock = customBlocks.get(customBlockDef.getBlockName());

            BlockExportFactory factory = BLOCK_EXPORTERS.get(customBlockDef.blockType);
            if (factory != null) {
                factory.create(blockStateModelGenerator, currentBlock, customBlockDef).generateBlockStateModels();
            } else {
                WesterosBlocks.LOGGER.warn("DATAGEN: Unknown block type: {} for block {}",
                        customBlockDef.blockType, customBlockDef.blockName);
            }
        }
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        HashMap<String, Block> customBlocks = ModBlocks.getCustomBlocksByName();
        ModBlock[] customBlockDefs = WesterosBlocksDefLoader.getCustomBlockDefs();

        for (ModBlock customBlockDef : customBlockDefs) {
            if (customBlockDef == null) continue;
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
                default:
                    WesterosBlocks.LOGGER.warn("DATAGEN: Unknown item type: {} for item {}", customBlockDef.blockType, customBlockDef.blockName);
            }
        }
    }
}
