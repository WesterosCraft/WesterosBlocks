package com.westeroscraft.westerosblocks;

import net.minecraft.block.Block;

// Abstract factory class : each custom block type needs to have one
public abstract class WesterosBlockFactory {
    /* Build instance of given block type and given blknum within factory (replace for factories needing more than one block def)
     * 
     * @param def - definition loaded for block
     * @returns block based on definition
     */
    public abstract Block[] buildBlockClasses(WesterosBlockDef def);
    /**
     * Get number of blocks defined for factory
     */
    public int getBlockClassCount() { return 1; }
}
