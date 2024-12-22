package com.westerosblocks.block;

// Used to define the interfaces for properly priming one of our custom block definitions
// must also have constructor with WesterosBlockDef as parameter

import com.mojang.serialization.MapCodec;
import net.minecraft.state.property.Properties;

public interface WesterosBlockLifecycle {
    /**
     * Get definition for block
     */
    WesterosBlockDef getWBDefinition();

    /**
     * Get block tags for block
     */
    String[] getBlockTags();
}
