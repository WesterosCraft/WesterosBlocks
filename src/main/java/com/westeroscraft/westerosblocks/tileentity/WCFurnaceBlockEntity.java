package com.westeroscraft.westerosblocks.tileentity;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.FurnaceMenu;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.network.chat.Component;

import com.westeroscraft.westerosblocks.WesterosBlockDef;

import net.minecraft.core.BlockPos;

public class WCFurnaceBlockEntity extends AbstractFurnaceBlockEntity {
	public static final String ENTITYTYPE = "wcfurnacetileentity";
	public WCFurnaceBlockEntity(BlockPos pos, BlockState bs) {		
		super(WesterosBlockDef.getBlockEntityType(ENTITYTYPE), pos, bs, RecipeType.SMELTING);
	}

	protected Component getDefaultName() {
	   return Component.literal("container.furnace");
	}

	protected AbstractContainerMenu createMenu(int p_59293_, Inventory p_59294_) {
		return new FurnaceMenu(p_59293_, p_59294_, this, this.dataAccess);
	}
}
