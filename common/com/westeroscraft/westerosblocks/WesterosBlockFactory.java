package com.westeroscraft.westerosblocks;

import net.minecraft.block.Block;

// Abstract factory class : each custom block type needs to have one
public abstract class WesterosBlockFactory {
    /* Build instance of given block type
     * @param index - index of custom block (good for getting to table in WesterosBlocks.customBlocks[])
     * @param def - definition loaded for block
     */
    public abstract Block buildBlockClass(int index, WesterosBlockDef def);
}
