package com.westerosblocks.block.custom;

import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoulSandBlock;

public class WCSoulSandBlock extends SoulSandBlock implements WCBlockDef {
    protected BlockDefinition def;

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(BlockDefinition definition) {
            AbstractBlock.Settings settings = definition.makeSettings();
            return new WCSoulSandBlock(settings, definition);
        }
    }

    public WCSoulSandBlock(Settings settings, BlockDefinition def) {
        super(settings);
        this.def = def;
    }

    public BlockDefinition getDefinition() {
        return def;
    }
}
