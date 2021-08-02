package com.westeroscraft.westerosblocks;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.DeferredRegister;

// Used to define the interfaces for properly priming one of our custom block definitions
//   Block definitions must also have constructor with WesterosBlockDef as parameter
public interface WesterosBlockTileEntity {
	public TileEntityType<?> getTileEntityType(DeferredRegister<TileEntityType<?>> registry, Block blk);
}
