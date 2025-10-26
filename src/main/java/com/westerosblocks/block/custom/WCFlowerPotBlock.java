package com.westerosblocks.block.custom;

import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.block.AbstractBlock;

import java.util.Map;

public class WCFlowerPotBlock extends FlowerPotBlock {
    protected BlockDefinition def;

    public WCFlowerPotBlock(Block content, AbstractBlock.Settings settings, BlockDefinition def) {
        super(content, settings);
        this.def = def;
    }

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(BlockDefinition definition) {
            AbstractBlock.Settings settings = definition.makeSettings();
            Block content = Blocks.AIR; // Default to empty pot
            return new WCFlowerPotBlock(content, settings, definition);
        }

        @Override
        public Block buildBlockClass(AbstractBlock.Settings settings, Map<String, Object> parameters) {
            Block content = (Block) parameters.getOrDefault("content", Blocks.AIR);
            return new WCFlowerPotBlock(content, settings, null);
        }
    }

    /**
     * Gets the BlockDefinition for this block.
     * @return BlockDefinition if block was created from JSON, null if created programmatically
     */
    public BlockDefinition getDefinition() {
        return def;
    }
}
