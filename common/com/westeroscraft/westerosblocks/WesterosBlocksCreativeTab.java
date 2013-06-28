package com.westeroscraft.westerosblocks;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;


public class  WesterosBlocksCreativeTab extends CreativeTabs {

    public static final CreativeTabs tabWesterosBlocks = new  WesterosBlocksCreativeTab("WesterosBlocks");
    
    public  WesterosBlocksCreativeTab(String label) {
        super(label);
    }

    @Override
    public ItemStack getIconItemStack() {
        return new ItemStack(WesterosBlocks.block1);
    }
    
    @Override
    public String getTranslatedTabLabel() {
        return "WesterosBlocks";
    }
}
