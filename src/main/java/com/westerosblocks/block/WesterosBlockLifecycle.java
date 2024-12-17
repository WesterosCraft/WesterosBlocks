package com.westerosblocks.block;

// Used to define the interfaces for properly priming one of our custom block definitions
// must also have constructor with WesterosBlockDef as parameter

public interface WesterosBlockLifecycle {
    /**
     * Get definition for block
     */
    public WesterosBlockDef getWBDefinition();
    /**
     * Get block tags for block
     */
    public String[] getBlockTags();
}
