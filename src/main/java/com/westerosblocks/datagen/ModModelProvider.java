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

public class ModModelProvider extends FabricModelProvider {
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

            switch (customBlockDef.blockType) {
                case "soulsand":
                case "sand":
                case "solid": {
                    new SolidBlockExport(blockStateModelGenerator, currentBlock, customBlockDef).generateBlockStateModels();
                    break;
                }
                case "sound": {
                    new SoundBlockExport(blockStateModelGenerator, currentBlock, customBlockDef).generateBlockStateModels();
                    break;
                }
                case "stair": {
                    new StairBlockExport(blockStateModelGenerator, currentBlock, customBlockDef).generateBlockStateModels();
                    break;
                }
                case "leaves": {
                    new LeavesBlockExport(blockStateModelGenerator, currentBlock, customBlockDef).generateBlockStateModels();
                    break;
                }
                case "log": {
                    new LogBlockExport(blockStateModelGenerator, currentBlock, customBlockDef).generateBlockStateModels();
                    break;
                }
                case "plant":
                case "web": {
                    new CrossBlockExport(blockStateModelGenerator, currentBlock, customBlockDef).generateBlockStateModels();
                    break;
                }
                case "pane": {
                    new PaneBlockExport(blockStateModelGenerator, currentBlock, customBlockDef).generateBlockStateModels();
                    break;
                }
                case "crop": {
                    new CropBlockExport(blockStateModelGenerator, currentBlock, customBlockDef).generateBlockStateModels();
                    break;
                }
                case "door": {
                    new DoorBlockExport(blockStateModelGenerator, currentBlock, customBlockDef).generateBlockStateModels();
                    break;
                }
                case "slab": {
                    new SlabBlockExport(blockStateModelGenerator, currentBlock, customBlockDef).generateBlockStateModels();
                    break;
                }
                case "fence": {
                    new FenceBlockExport(blockStateModelGenerator, currentBlock, customBlockDef).generateBlockStateModels();
                    break;
                }
                case "wall": {
                    new WallBlockExport(blockStateModelGenerator, currentBlock, customBlockDef).generateBlockStateModels();
                    break;
                }
                case "trapdoor": {
                    new TrapDoorBlockExport(blockStateModelGenerator, currentBlock, customBlockDef).generateBlockStateModels();
                    break;
                }
                case "torch": {
                    new TorchBlockExport(blockStateModelGenerator, currentBlock, customBlockDef).generateBlockStateModels();
                    break;
                }
                case "fan": {
                    new FanBlockExport(blockStateModelGenerator, currentBlock, customBlockDef).generateBlockStateModels();
                    break;
                }
                case "ladder": {
                    new LadderBlockExport(blockStateModelGenerator, currentBlock, customBlockDef).generateBlockStateModels();
                    break;
                }
                case "fire": {
                    new FireBlockExport(blockStateModelGenerator, currentBlock, customBlockDef).generateBlockStateModels();
                    break;
                }
                case "bed": {
                    new BedBlockExport(blockStateModelGenerator, currentBlock, customBlockDef).generateBlockStateModels();
                    break;
                }
                case "cuboid":
                case "beacon": {
                    new CuboidBlockExport(blockStateModelGenerator, currentBlock, customBlockDef).generateBlockStateModels();
                    break;
                }
                case "cuboid-ne": {
                    new CuboidNEBlockExport(blockStateModelGenerator, currentBlock, customBlockDef).generateBlockStateModels();
                    break;
                }
                case "cuboid-nsew": {
                    new CuboidNSEWBlockExport(blockStateModelGenerator, currentBlock, customBlockDef).generateBlockStateModels();
                    break;
                }
                case "cuboid-16way": {
                    new Cuboid16WayBlockExport(blockStateModelGenerator, currentBlock, customBlockDef).generateBlockStateModels();
                    break;
                }
                case "cuboid-nsew-stack": {
                    new CuboidNSEWStackBlockExport(blockStateModelGenerator, currentBlock, customBlockDef).generateBlockStateModels();
                    break;
                }
                case "cuboid-nsewud": {
                    new CuboidNSEWUDBlockExport(blockStateModelGenerator, currentBlock, customBlockDef).generateBlockStateModels();
                    break;
                }
                case "layer": {
                    new LayerBlockExport(blockStateModelGenerator, currentBlock, customBlockDef).generateBlockStateModels();
                    break;
                }
                case "rail": {
                    new RailBlockExport(blockStateModelGenerator, currentBlock, customBlockDef).generateBlockStateModels();
                    break;
                }
                case "halfdoor": {
                    new HalfDoorBlockExport(blockStateModelGenerator, currentBlock, customBlockDef).generateBlockStateModels();
                    break;
                }
                case "cake": {
                    new CakeBlockExport(blockStateModelGenerator, currentBlock, customBlockDef).generateBlockStateModels();
                    break;
                }
                case "furnace": {
                    new FurnaceBlockExport(blockStateModelGenerator, currentBlock, customBlockDef).generateBlockStateModels();
                    break;
                }
                case "vines": {
                    new VinesBlockExport(blockStateModelGenerator, currentBlock, customBlockDef).generateBlockStateModels();
                    break;
                }
                case "flowerpot": {
                    new FlowerPotExport(blockStateModelGenerator, currentBlock, customBlockDef).generateBlockStateModels();
                    break;
                }
                case "fencegate": {
                    new FenceGateBlockExport(blockStateModelGenerator, currentBlock, customBlockDef).generateBlockStateModels();
                    break;
                }
                default:
                    WesterosBlocks.LOGGER.warn("DATAGEN: Unknown block type: {} for block {}", customBlockDef.blockType, customBlockDef.blockName);
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
