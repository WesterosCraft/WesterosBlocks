package com.westeroscraft.westerosblocks;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;


public class  WesterosBlocksCreativeTab extends CreativeTabs {

    public static final CreativeTabs tabWesterosBlocks = new  WesterosBlocksCreativeTab("WesterosBlocks", "Westeros Blocks", 0);
    public static final CreativeTabs tabWesterosDecorative = new  WesterosBlocksCreativeTab("WesterosDeco", "Westeros Decorative", 1);
    
    private int tab;
    private String lbl;
    public  WesterosBlocksCreativeTab(String id, String label, int tabnum) {
        super(id);
        tab = tabnum;
        lbl = label;
    }

    @Override
    public ItemStack getIconItemStack() {
        switch (tab) {
            case 0:
                return new ItemStack(WesterosBlocks.blockIron, 1, 0);
            default:
                return new ItemStack(WesterosBlocks.blockIronStairs1, 1, 0);
        }
    }
    @Override
    public String getTranslatedTabLabel() {
        return lbl;
    }
}
