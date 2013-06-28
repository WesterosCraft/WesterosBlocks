package com.westeroscraft.westerosblocks.blocks;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;

public class Block1 extends Block {
    private final int metacnt;
    private Icon[] icons;
    
    public Block1(int blockid, int meta_cnt) {
        super(blockid, Material.rock);
        this.setCreativeTab(CreativeTabs.tabBlock);
        this.metacnt = meta_cnt;
        this.icons = new Icon[metacnt];
        
    }
    public void registerIcons(IconRegister iconRegister)
    {
        for (int i = 0; i < metacnt; i++) {
            icons[i] = iconRegister.registerIcon("WesterosBlocks:block1_" + i);
        }
    }
    public Icon getIcon(int side, int meta) {
        if (meta >= metacnt) meta = 0;
        return icons[meta];
    }
    public void getSubBlocks(int id, CreativeTabs tab, List list)
    {
        for (int i = 0; i < metacnt; i++)
            list.add(new ItemStack(id, 1, i));
    }
}