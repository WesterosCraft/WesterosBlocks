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

    /**
     * Builds a block instance with manual settings and parameters (for BlockBuilder).
     * This method is used when creating blocks programmatically without a JSON definition.
     * Used by BlockBuilder for manual block creation.
     *
     * @param settings The block settings (built externally)
     * @param parameters Additional parameters from BlockBuilder
     * @return The created block instance
     */
    public Block buildBlockClass(AbstractBlock.Settings settings, Map<String, Object> parameters) {
        // Default implementation - should be overridden by factories that support manual creation
        throw new UnsupportedOperationException(
            "This factory does not support manual block creation. Override buildBlockClass(Settings, Map) to support BlockBuilder."
        );
    }

    /**
     * @deprecated Old method signature. Use buildBlockClass(BlockDefinition) instead.
     */
    @Deprecated
    public Block buildBlockClass(AbstractBlock.Settings settings, BlockDefinition definition) {
        // Backwards compatibility - delegate to new method
        return buildBlockClass(definition);
    }

    /**
     * @deprecated Old method signature. Use buildBlockClass(BlockDefinition) for JSON blocks
     * or buildBlockClass(Settings, Map) for manual creation.
     */
    @Deprecated
    public Block buildBlockClass(AbstractBlock.Settings settings, BlockDefinition definition, Map<String, Object> parameters) {
        // Backwards compatibility - delegate to parameters-based method for BlockBuilder
        return buildBlockClass(settings, parameters);
    }
}