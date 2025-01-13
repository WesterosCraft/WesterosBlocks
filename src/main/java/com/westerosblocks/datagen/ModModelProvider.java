package com.westerosblocks.datagen;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.ModBlocks;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.custom.WCSolidBlock;
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
            if (customBlockDef == null || customBlockDef.isCustomModel()) continue;
            Block currentBlock = customBlocks.get(customBlockDef.blockName);
            switch (customBlockDef.blockType) {

                case "solid": {
                    SolidBlockModelHandler.generateBlockStateAndModels(blockStateModelGenerator, currentBlock, customBlockDef);
                    break;
                }
//                case "stair": {
//                    StairBlockModelHandler.generateBlockStateModels(blockStateModelGenerator, currentBlock, customBlockDef);
//                    break;
//                }
//                case "slab": {
//                    SlabBlockModelHandler.generateBlockStateModels(blockStateModelGenerator, currentBlock, customBlockDef);
//                    break;
//                }
//                case "fence": {
//                    FenceBlockModelHandler.generateBlockStateModels(blockStateModelGenerator, currentBlock, customBlockDef);
//                    break;
//                }
//                case "wall": {
//                    WallBlockModelHandler.generateBlockStateModels(blockStateModelGenerator, currentBlock, customBlockDef);
//                    break;
//                }
//                case "cuboid": {
//                    CuboidBlockModelHandler.generateBlockStateModels(blockStateModelGenerator, currentBlock, customBlockDef);
//                    break;
//                }
//                case "door": {
//                    blockStateModelGenerator.registerDoor(currentBlock);
//                    break;
//                }
//                case "walltorch": {
//                    blockStateModelGenerator.registerTorch();
//                }
                default:
                    WesterosBlocks.LOGGER.warn("DATAGEN: Unknown block type: {} for block {}", customBlockDef.blockType, customBlockDef.blockName);
            }
        }

    }


    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        HashMap<String, Block> customBlocks = ModBlocks.getCustomBlocksByName();
        WesterosBlockDef[] customBlockDefs = WesterosBlocks.getCustomBlockDefs();

        for (WesterosBlockDef customBlockDef : customBlockDefs) {
            if (customBlockDef == null || customBlockDef.isCustomModel()) continue;
            Block currentBlock = customBlocks.get(customBlockDef.blockName);
            switch (customBlockDef.blockType) {

                case "solid": {
                    SolidBlockModelHandler.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
                    break;
                }
////                case "stair": {
////                    StairBlockModelHandler.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
////                    break;
////                }
////                case "slab": {
////                    SlabBlockModelHandler.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
////                    break;
////                }
////                case "fence": {
////                    FenceBlockModelHandler.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
////                    break;
////                }
////                case "wall": {
////                    WallBlockModelHandler.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
////                    break;
////                }
////                case "cuboid": {
////                    CuboidBlockModelHandler.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
////                    break;
////                }
                default:
                    WesterosBlocks.LOGGER.warn("DATAGEN: Unknown item type: {} for item {}", customBlockDef.blockType, customBlockDef.blockName);
            }
        }
    }
}
