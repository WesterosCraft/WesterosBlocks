package com.westeroscraft.westerosblocks.blocks;

import net.minecraft.block.BlockHalfSlab;
import net.minecraft.item.ItemSlab;

public class WCSlabItem extends ItemSlab
{
	private static BlockHalfSlab	halfSlab	= null;
	private static BlockHalfSlab	fullSlab	= null;

	static public void setSlabs(BlockHalfSlab hSlab, BlockHalfSlab fSlab)
	{
	    halfSlab = hSlab;
		fullSlab = fSlab;
	}

	public WCSlabItem(int id) {
		super(id, halfSlab, fullSlab, id == fullSlab.blockID);
	}

	@Override
	public int getMetadata(int meta)
	{
		return meta & 7;
	}	
}
