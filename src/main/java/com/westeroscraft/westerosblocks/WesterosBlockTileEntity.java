package com.westeroscraft.westerosblocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

// Used to define the interfaces for properly priming one of our custom block definitions
//   Block definitions must also have constructor with WesterosBlockDef as parameter
public interface WesterosBlockTileEntity {
	public BlockEntityType<?> getBlockEntityType(DeferredRegister<BlockEntityType<?>> registry, Block blk);
}
