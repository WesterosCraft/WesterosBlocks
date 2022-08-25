package com.westeroscraft.westerosblocks;

import com.westeroscraft.westerosblocks.modelexport.ModelExport;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class  WesterosBlocksCreativeTab extends CreativeModeTab {

    public static final CreativeModeTab tabWesterosBlocks = new  WesterosBlocksCreativeTab("WesterosBlocks", "Westeros Blocks", "oxidized_iron_block");
    public static final CreativeModeTab tabWesterosDecorative = new  WesterosBlocksCreativeTab("WesterosDeco", "Westeros Decorative", "oxidized_iron_stairs");
    public static final CreativeModeTab tabWesterosPlants = new  WesterosBlocksCreativeTab("WesterosPlants", "Westeros Plants", "yellow_wildflowers");
    public static final CreativeModeTab tabWesterosSounds = new  WesterosBlocksCreativeTab("WesterosSounds", "Westeros Sounds", "tavern_small");
    public static final CreativeModeTab tabWesterosCTMSamples = new  WesterosBlocksCreativeTab("WesterosCTMSamples", "CTM Samples", "ctm_vertical");
    
    public static void init() {
        
    }
    
    private String type;
    private Block itm = null;
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
            itm = WesterosBlocks.findBlockByName(this.type);
            if (itm == null) {
                itm = WesterosBlocks.customBlocks[0];
            }
        }
        return new ItemStack(itm);
    }
}
