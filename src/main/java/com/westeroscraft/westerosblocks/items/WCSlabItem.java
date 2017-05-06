package com.westeroscraft.westerosblocks.items;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.item.ItemSlab;

public class WCSlabItem extends ItemSlab
{
	private static BlockSlab	halfSlab	= null;
	private static BlockSlab	fullSlab	= null;

	static public void setSlabs(BlockSlab hSlab, BlockSlab fSlab)
	{
	    halfSlab = hSlab;
		fullSlab = fSlab;
	}

	public WCSlabItem(Block blk) {
		super(blk, halfSlab, fullSlab);
	}
}
