package com.westeroscraft.westerosblocks.blocks;

import java.util.List;

import com.westeroscraft.westerosblocks.WesterosBlocksCreativeTab;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;

public class BlockIron extends Block {
    private final int metacnt;
    private Icon[] icons;
    
    public BlockIron(int blockid, int meta_cnt) {
        super(blockid, Material.iron);
        this.setCreativeTab(WesterosBlocksCreativeTab.tabWesterosBlocks);
        this.metacnt = meta_cnt;
        this.icons = new Icon[metacnt];
    }
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        for (int i = 0; i < metacnt; i++) {
            icons[i] = iconRegister.registerIcon("westerosblocks:block1_" + i);
        }
    }
    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int meta) {
        if (meta >= metacnt) meta = 0;
        return icons[meta];
    }
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(int id, CreativeTabs tab, List list)
    {
        for (int i = 0; i < metacnt; i++) {
            list.add(new ItemStack(id, 1, i));
        }
    }
}