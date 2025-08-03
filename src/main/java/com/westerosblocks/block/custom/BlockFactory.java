package com.westerosblocks.block.custom;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;

/**
 * Base factory interface for creating WesterosBlocks custom blocks.
 * All block factories should extend this interface to provide a consistent
 * buildBlockClass method.
 */
public abstract class BlockFactory {

    /**
     * Builds a block instance with the given settings and parameters.
     * 
     * @param settings The block settings
     * @param params   Additional parameters specific to the block type
     * @return The created block instance
     */
    public abstract Block buildBlockClass(AbstractBlock.Settings settings, Object... params);
}