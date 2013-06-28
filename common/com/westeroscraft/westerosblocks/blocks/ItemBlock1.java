package com.westeroscraft.westerosblocks.blocks;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlock1 extends ItemBlock {
    public ItemBlock1(int blockid) {
        super(blockid);
    }
    public String getUnlocalizedName(ItemStack item)
    {
        return "block1_" + item.getItemDamage();
    }
    public int getMetadata(int meta) {
        return meta;
    }
}