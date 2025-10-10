package com.westerosblocks.block.custom;

import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;

import java.util.Map;

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

    /**
     * Builds a block instance with the given settings, block definition, and parameters.
     * This method is called by BlockBuilder to pass additional parameters like wallBlock.
     *
     * @param settings The block settings
     * @param definition The block definition containing all block properties
     * @param parameters Additional parameters from BlockBuilder
     * @return The created block instance
     */
    public Block buildBlockClass(AbstractBlock.Settings settings, BlockDefinition definition, Map<String, Object> parameters) {
        // Default implementation calls the old method for backwards compatibility
        return buildBlockClass(settings, definition);
    }
}