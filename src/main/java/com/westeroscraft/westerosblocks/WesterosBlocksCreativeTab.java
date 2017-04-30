package com.westeroscraft.westerosblocks;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;


public class  WesterosBlocksCreativeTab extends CreativeTabs {

    public static final CreativeTabs tabWesterosBlocks = new  WesterosBlocksCreativeTab("WesterosBlocks", "Westeros Blocks", "metal_block_0");
    public static final CreativeTabs tabWesterosDecorative = new  WesterosBlocksCreativeTab("WesterosDeco", "Westeros Decorative", "metal_block_0_stair_1");
//    public static final CreativeTabs tabWesterosPlants = new  WesterosBlocksCreativeTab("WesterosPlants", "Westeros Plants", "yellow_flower_block_0");
//    public static final CreativeTabs tabWesterosSounds = new  WesterosBlocksCreativeTab("WesterosSounds", "Westeros Sounds", "sound_blocks_0");
    
    public static void init() {
        
    }
    
    private String lbl;
    private String type;
    private Block blk = null;
    public  WesterosBlocksCreativeTab(String id, String label, String type) {
        super(id);
        lbl = label;
        this.type = type;
        WesterosBlockDef.addCreativeTab(id,  this);
    }

    @Override
    public ItemStack getIconItemStack() {
        if (blk == null) {
            blk = WesterosBlocks.findBlockByName(this.type);
            if (blk == null) {
                blk = WesterosBlocks.customBlocks[0];
            }
        }
        return new ItemStack(blk, 1, 0);
    }
    @Override
    public String getTranslatedTabLabel() {
        return lbl;
    }

    @Override
    public ItemStack getTabIconItem() {
        return getIconItemStack();
    }
}
