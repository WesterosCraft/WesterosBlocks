package com.westerosblocks.needsported;

import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.RegisterEvent;

// Abstract factory class : each custom block type needs to have one
public abstract class WesterosBlockFactory {
    /* Build instance of given block type and given blknum within factory (replace for factories needing more than one block def)
     * 
     * @param def - definition loaded for block
     * @returns block based on definition
     */
    public abstract Block buildBlockClass(WesterosBlockDef def, RegisterEvent.RegisterHelper<Block> helper);
}
