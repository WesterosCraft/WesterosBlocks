package com.westerosblocks.datagen;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.ModBlocks;
import com.westerosblocks.block.WesterosBlockDef;
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
        WesterosBlockDef[] customBlockDefs = WesterosBlocks.getCustomBlockDefs();

        for (WesterosBlockDef customBlockDef : customBlockDefs) {
            if (customBlockDef == null) continue;
            Block currentBlock = customBlocks.get(customBlockDef.getBlockName());

            switch (customBlockDef.blockType) {
                case "soulsand":
                case "sand":
                case "solid": {
                    new SolidBlockModelHandler(blockStateModelGenerator, currentBlock, customBlockDef).generateBlockStateModels();
                    break;
                }
                case "sound": {
                    new SoundBlockModelHandler(blockStateModelGenerator, currentBlock, customBlockDef).generateBlockStateModels();
                    break;
                }
                case "stair": {
                    new StairBlockModelHandler(blockStateModelGenerator, currentBlock, customBlockDef).generateBlockStateModels();
                    break;
                }
                case "leaves": {
                    new LeavesBlockModelHandler(blockStateModelGenerator, currentBlock, customBlockDef).generateBlockStateModels();
                    break;
                }
                case "log": {
                    new LogBlockModelHandler(blockStateModelGenerator, currentBlock, customBlockDef).generateBlockStateModels();
                    break;
                }
                case "plant":
                case "web": {
                    new CrossBlockModelHandler(blockStateModelGenerator, currentBlock, customBlockDef).generateBlockStateModels();
                    break;
                }
                case "pane": {
                    new PaneBlockModelHandler(blockStateModelGenerator, currentBlock, customBlockDef).generateBlockStateModels();
                    break;
                }
                case "crop": {
                    new CropBlockModelHandler(blockStateModelGenerator, currentBlock, customBlockDef).generateBlockStateModels();
                    break;
                }
                case "door": {
                    new DoorBlockModelHandler(blockStateModelGenerator, currentBlock, customBlockDef).generateBlockStateModels();
                    break;
                }
                case "slab": {
                    new SlabBlockModelHandler(blockStateModelGenerator, currentBlock, customBlockDef).generateBlockStateModels();
                    break;
                }
                case "fence": {
                    new FenceBlockModelHandler(blockStateModelGenerator, currentBlock, customBlockDef).generateBlockStateModels();
                    break;
                }
                case "wall": {
                    new WallBlockModelHandler(blockStateModelGenerator, currentBlock, customBlockDef).generateBlockStateModels();
                    break;
                }
                case "trapdoor": {
                    new TrapDoorBlockModelHandler(blockStateModelGenerator, currentBlock, customBlockDef).generateBlockStateModels();
                    break;
                }
                case "torch": {
                    new TorchBlockModelHandler(blockStateModelGenerator, currentBlock, customBlockDef).generateBlockStateModels();
                    break;
                }
                case "fan": {
                    new FanBlockModelHandler(blockStateModelGenerator, currentBlock, customBlockDef).generateBlockStateModels();
                    break;
                }
                case "ladder": {
                    new LadderBlockModelHandler(blockStateModelGenerator, currentBlock, customBlockDef).generateBlockStateModels();
                    break;
                }
                case "fire": {
                    new FireBlockModelHandler(blockStateModelGenerator, currentBlock, customBlockDef).generateBlockStateModels();
                    break;
                }
                case "bed": {
                    new BedBlockModelHandler(blockStateModelGenerator, currentBlock, customBlockDef).generateBlockStateModels();
                    break;
                }
                case "cuboid":
                case "beacon": {
                    new CuboidBlockModelHandler(blockStateModelGenerator, currentBlock, customBlockDef).generateBlockStateModels();
                    break;
                }
                case "cuboid-ne": {
                    new CuboidNEBlockModelHandler(blockStateModelGenerator, currentBlock, customBlockDef).generateBlockStateModels();
                    break;
                }
                case "cuboid-nsew": {
                    new CuboidNSEWBlockModelHandler(blockStateModelGenerator, currentBlock, customBlockDef).generateBlockStateModels();
                    break;
                }
                case "cuboid-16way": {
                    new Cuboid16WayBlockModelHandler(blockStateModelGenerator, currentBlock, customBlockDef).generateBlockStateModels();
                    break;
                }
                case "cuboid-nsew-stack": {
                    new CuboidNSEWStackBlockModelHandler(blockStateModelGenerator, currentBlock, customBlockDef).generateBlockStateModels();
                    break;
                }
                case "cuboid-nsewud": {
                    new CuboidNSEWUDBlockModelHandler(blockStateModelGenerator, currentBlock, customBlockDef).generateBlockStateModels();
                    break;
                }
                case "layer": {
                    new LayerBlockModelHandler(blockStateModelGenerator, currentBlock, customBlockDef).generateBlockStateModels();
                    break;
                }
                case "rail": {
                    new RailBlockModelHandler(blockStateModelGenerator, currentBlock, customBlockDef).generateBlockStateModels();
                    break;
                }
                case "halfdoor": {
                    new HalfDoorBlockModelHandler(blockStateModelGenerator, currentBlock, customBlockDef).generateBlockStateModels();
                    break;
                }
                case "cake": {
                    new CakeBlockModelHandler(blockStateModelGenerator, currentBlock, customBlockDef).generateBlockStateModels();
                    break;
                }
                case "furnace": {
                    new FurnaceBlockModelHandler(blockStateModelGenerator, currentBlock, customBlockDef).generateBlockStateModels();
                    break;
                }
                case "vines": {
                    new VinesBlockModelHandler(blockStateModelGenerator, currentBlock, customBlockDef).generateBlockStateModels();
                    break;
                }
                case "flowerpot": {
                    new FlowerPotModelHandler(blockStateModelGenerator, currentBlock, customBlockDef).generateBlockStateModels();
                }
                case "fencegate": {
                    new FenceGateBlockModelHandler(blockStateModelGenerator, currentBlock, customBlockDef).generateBlockStateModels();
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
        WesterosBlockDef[] customBlockDefs = WesterosBlocks.getCustomBlockDefs();

        for (WesterosBlockDef customBlockDef : customBlockDefs) {
            if (customBlockDef == null) continue;
            Block currentBlock = customBlocks.get(customBlockDef.getBlockName());
            switch (customBlockDef.blockType) {
                case "soulsand":
                case "sand":
                case "solid": {
                    SolidBlockModelHandler.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
                    break;
                }
                case "stair": {
                    StairBlockModelHandler.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
                    break;
                }
                case "slab": {
                    SlabBlockModelHandler.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
                    break;
                }
                case "ladder": {
                    LadderBlockModelHandler.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
                    break;
                }
                case "log": {
                    LogBlockModelHandler.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
                    break;
                }
                case "rail": {
                    RailBlockModelHandler.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
                    break;
                }
                case "trapdoor": {
                    TrapDoorBlockModelHandler.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
                    break;
                }
                case "torch": {
                    TorchBlockModelHandler.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
                    break;
                }
                case "leaves": {
                    LeavesBlockModelHandler.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
                    break;
                }
                case "crop": {
                    CropBlockModelHandler.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
                    break;
                }
                case "plant":
                case "web": {
                    CrossBlockModelHandler.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
                    break;
                }
                case "fencegate": {
                    FenceGateBlockModelHandler.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
                    break;
                }
                case "vines": {
                    VinesBlockModelHandler.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
                    break;
                }
                case "bed": {
                    BedBlockModelHandler.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
                    break;
                }
                case "door": {
                    DoorBlockModelHandler.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
                    break;
                }
                case "fan": {
                    FanBlockModelHandler.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
                    break;
                }
                case "fire": {
                    FireBlockModelHandler.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
                    break;
                }
                case "layer": {
                    LayerBlockModelHandler.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
                    break;
                }
                case "pane": {
                    PaneBlockModelHandler.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
                    break;
                }
                case "halfdoor": {
                    HalfDoorBlockModelHandler.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
                    break;
                }
                // TODO waiting on block to be done
//                case "flowerpot": {
//                    FlowerPotModelHandler.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
//                }
                case "fence": {
                    FenceBlockModelHandler.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
                    break;
                }
                case "wall": {
                    WallBlockModelHandler.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
                    break;
                }
                case "cuboid": {
                    CuboidBlockModelHandler.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
                    break;
                }
                default:
                    WesterosBlocks.LOGGER.warn("DATAGEN: Unknown item type: {} for item {}", customBlockDef.blockType, customBlockDef.blockName);
            }
        }
    }
}
