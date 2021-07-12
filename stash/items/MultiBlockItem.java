package com.westeroscraft.westerosblocks.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

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
        return super.getUnlocalizedName() + "_" + itemstack.getMetadata();
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }
}

