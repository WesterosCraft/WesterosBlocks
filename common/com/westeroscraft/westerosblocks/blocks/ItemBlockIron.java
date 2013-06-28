package com.westeroscraft.westerosblocks.blocks;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockIron extends ItemBlock {
    public ItemBlockIron(int blockid) {
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