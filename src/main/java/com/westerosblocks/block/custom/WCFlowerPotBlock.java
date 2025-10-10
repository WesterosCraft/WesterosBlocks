package com.westerosblocks.block.custom;

import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.block.AbstractBlock;

public class WCFlowerPotBlock extends FlowerPotBlock {

    public WCFlowerPotBlock(Block content, AbstractBlock.Settings settings) {
        super(content, settings);
    }

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(AbstractBlock.Settings settings, BlockDefinition definition) {
            Block content = Blocks.AIR; // Default to empty pot
            return new WCFlowerPotBlock(content, settings);
        }
    }
}
