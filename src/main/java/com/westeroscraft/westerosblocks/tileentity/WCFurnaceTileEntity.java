package com.westeroscraft.westerosblocks.tileentity;

import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.FurnaceContainer;

import com.westeroscraft.westerosblocks.WesterosBlockDef;

import net.minecraft.entity.player.PlayerInventory;

public class WCFurnaceTileEntity extends AbstractFurnaceTileEntity {
	public static final String ENTITYTYPE = "wcfurnacetileentity";
	public WCFurnaceTileEntity() {		
		super(WesterosBlockDef.getTileEntityType(ENTITYTYPE).get(), IRecipeType.SMELTING);
	}

	protected ITextComponent getDefaultName() {
		return new TranslationTextComponent("container.furnace");
	}

	protected Container createMenu(int p_213906_1_, PlayerInventory p_213906_2_) {
		return new FurnaceContainer(p_213906_1_, p_213906_2_, this, this.dataAccess);
	}
}
