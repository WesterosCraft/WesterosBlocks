package com.westerosblocks.datagen;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.ModBlocks;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.datagen.models.SlabBlockModelHandler;
import com.westerosblocks.datagen.models.SolidBlockModelHandler;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import java.util.HashMap;

public class WesterosBlocksModelProvider extends FabricModelProvider {
    public WesterosBlocksModelProvider(FabricDataOutput output) {
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
                    SolidBlockModelHandler.generateBlockStateModels(blockStateModelGenerator, currentBlock, customBlockDef);
                    break;
                }
                case "slab": {
                    SlabBlockModelHandler.generateBlockStateModels(blockStateModelGenerator, currentBlock, customBlockDef);
                }
//                case "door": {
//                    blockStateModelGenerator.registerDoor(currentBlock);
//                    break;
//                }
//                case "walltorch": {
//                    blockStateModelGenerator.registerTorch();
//                }
//                case "stair":
//                    BlockStateModelGenerator.BlockTexturePool blk = blockStateModelGenerator.registerCubeAllModelTexturePool(customBlocks.get(customBlockDef.blockName));
//
//                    blk.stairs(currentBlock);
//                    break;
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
            if (customBlockDef == null || customBlockDef.isCustomModel()) continue;
            Block currentBlock = customBlocks.get(customBlockDef.blockName);
            switch (customBlockDef.blockType) {

                case "solid": {
                    SolidBlockModelHandler.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
                    break;
                }
                case "slab": {
                    SlabBlockModelHandler.generateItemModels(itemModelGenerator, currentBlock, customBlockDef);
                }
//                case "door": {
//                    blockStateModelGenerator.registerDoor(currentBlock);
//                    break;
//                }
//                case "walltorch": {
//                    blockStateModelGenerator.registerTorch();
//                }
//                case "stair":
//                    BlockStateModelGenerator.BlockTexturePool blk = blockStateModelGenerator.registerCubeAllModelTexturePool(customBlocks.get(customBlockDef.blockName));
//
//                    blk.stairs(currentBlock);
//                    break;
                default:
                    WesterosBlocks.LOGGER.warn("DATAGEN: Unknown item type: {} for item {}", customBlockDef.blockType, customBlockDef.blockName);
            }
        }

    }
}
