package com.westerosblocks.block.custom;

import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoulSandBlock;

import java.util.Map;

public class WCSoulSandBlock extends SoulSandBlock {
    protected BlockDefinition def;

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(BlockDefinition definition) {
            AbstractBlock.Settings settings = definition.makeSettings();
            return new WCSoulSandBlock(settings, definition);
        }

        @Override
        public Block buildBlockClass(AbstractBlock.Settings settings, Map<String, Object> parameters) {
            return new WCSoulSandBlock(settings, null);
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
