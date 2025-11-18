package com.westerosblocks.block.custom;

import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.Block;

/**
 * Base factory interface for creating WesterosBlocks custom blocks.
 * All block factories should extend this interface to provide a consistent
 * buildBlockClass method.
 */
public abstract class BlockFactory {

    /**
     * Builds a block instance from a BlockDefinition (primary method for JSON-based blocks).
     * This method should:
     * 1. Call definition.makeSettings() to build block settings
     * 2. Call definition.buildStateProperty() to get state property
     * 3. Extract block-specific properties from definition
     * 4. Create and return the block instance
     *
     * @param definition The block definition containing all block properties
     * @return The created block instance
     */
    public abstract Block buildBlockClass(BlockDefinition definition);
}