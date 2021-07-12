package com.westeroscraft.westerosblocks;

import com.westeroscraft.westerosblocks.modelexport.ModelExport;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;


public class  WesterosBlocksCreativeTab extends ItemGroup {

    public static final ItemGroup tabWesterosBlocks = new  WesterosBlocksCreativeTab("WesterosBlocks", "Westeros Blocks", "metal_block_0");
    public static final ItemGroup tabWesterosDecorative = new  WesterosBlocksCreativeTab("WesterosDeco", "Westeros Decorative", "metal_block_0_stair_1");
    public static final ItemGroup tabWesterosPlants = new  WesterosBlocksCreativeTab("WesterosPlants", "Westeros Plants", "yellow_flower_block_0");
    public static final ItemGroup tabWesterosSounds = new  WesterosBlocksCreativeTab("WesterosSounds", "Westeros Sounds", "sound_blocks_0");
    
    public static void init() {
        
    }
    
    private String type;
    private Item itm = null;
    public  WesterosBlocksCreativeTab(String id, String label, String type) {
        super(id);
        this.type = type;
        WesterosBlockDef.addCreativeTab(id,  this);
        // Add to our NLS export
        ModelExport.addNLSString("itemGroup." + id, label);
    }
    
    @Override
    public ItemStack makeIcon()
    {
        if (itm == null) {
        	//TODO
            //itm = WesterosBlocks.findBlockByName(this.type);
            //if (itm == null) {
            //    itm = WesterosBlocks.customBlocks[0];
            //}
        }
        return new ItemStack(itm);
    }
}
