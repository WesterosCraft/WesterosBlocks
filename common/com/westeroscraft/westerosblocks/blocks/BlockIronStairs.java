package com.westeroscraft.westerosblocks.blocks;

import com.westeroscraft.westerosblocks.WesterosBlocksCreativeTab;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;

public class BlockIronStairs extends BlockStairs {

    public BlockIronStairs(int id, Block blk, int meta) {
        super(id, blk, meta);
        this.setCreativeTab(WesterosBlocksCreativeTab.tabWesterosBlocks);
    }
}
