package com.westeroscraft.westerosblocks.items;

import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MultiBlockItem extends ItemBlock {
    private Block blk;

    public MultiBlockItem(Block par1) {
        super(par1);
        setMaxDamage(0);
        setHasSubtypes(true);
        this.blk = par1;
    }

    public Block getBlock() {
        return blk;
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack) {
        return super.getUnlocalizedName(itemstack) + "_" + itemstack.getItemDamage();
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }
    
}

