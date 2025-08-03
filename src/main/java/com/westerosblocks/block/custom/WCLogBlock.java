package com.westerosblocks.block.custom;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.PillarBlock;

public class WCLogBlock extends PillarBlock {
    public WCLogBlock(Settings settings) {
        super(settings);
    }

    public static class Factory extends BlockFactory {
        public Block buildBlockClass(AbstractBlock.Settings settings, Object... params) {
            return new WCLogBlock(settings);
        }
    }
}
