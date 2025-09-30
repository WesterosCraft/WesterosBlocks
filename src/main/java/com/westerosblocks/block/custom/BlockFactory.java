package com.westerosblocks.block.custom;

import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;

/**
 * Base factory interface for creating WesterosBlocks custom blocks.
 * All block factories should extend this interface to provide a consistent
 * buildBlockClass method.
 */
public abstract class BlockFactory {

    /**
     * Builds a block instance with the given settings and block definition.
     *
     * @param settings The block settings
     * @param definition The block definition containing all block properties
     * @return The created block instance
     */
    public abstract Block buildBlockClass(AbstractBlock.Settings settings, BlockDefinition definition);
}