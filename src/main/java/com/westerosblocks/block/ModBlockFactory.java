package com.westerosblocks.block;

import net.minecraft.block.Block;

// Abstract factory class : each custom block type needs to have one
public abstract class ModBlockFactory {
    /* Build instance of given block type and given blknum within factory (replace for factories needing more than one block def)
     *
     * @param def - definition loaded for block
     * @returns block based on definition
     */
    public abstract Block buildBlockClass(ModBlock def);
}
