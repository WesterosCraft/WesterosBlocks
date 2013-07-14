package com.westeroscraft.westerosblocks.blocks;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class MultiBlockItem extends ItemBlock {
    private Block blk;

    public MultiBlockItem(int par1) {
        super(par1);
        setMaxDamage(0);
        setHasSubtypes(true);
    }

    @SideOnly(Side.CLIENT)
    private Block getBlock() {
        if (blk == null) {
            blk = Block.blocksList[getBlockID()];
        }
        return blk;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getColorFromItemStack(ItemStack itemstack, int par2) {
        return getBlock().getRenderColor(itemstack.getItemDamage());
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Icon getIconFromDamage(int meta) {
        return getBlock().getIcon(0, meta);
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

