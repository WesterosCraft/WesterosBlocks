package com.westeroscraft.westerosblocks.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

public class WCItemBlock extends ItemBlock {
    public WCItemBlock(int blockid) {
        super(blockid);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }
    public String getUnlocalizedName(ItemStack item)
    {
        return "block" + this.getBlockID() + "_" + item.getItemDamage();
    }
    public int getMetadata(int meta) {
        return meta;
    }
}