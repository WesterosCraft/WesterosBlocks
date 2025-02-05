package com.westerosblocks.block;

// Used to define the interfaces for properly priming one of our custom block definitions
// must also have constructor with WesterosBlock as parameter

public interface ModBlockLifecycle {
    /**
     * Get definition for block
     */
    ModBlock getWBDefinition();

    /**
     * Get block tags for block
     */
    String[] getBlockTags();
}
