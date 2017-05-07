package com.westeroscraft.westerosblocks;

import com.westeroscraft.westerosblocks.modelexport.ModelExport;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class  WesterosBlocksCreativeTab extends CreativeTabs {

    public static final CreativeTabs tabWesterosBlocks = new  WesterosBlocksCreativeTab("WesterosBlocks", "Westeros Blocks", "metal_block_0");
    public static final CreativeTabs tabWesterosDecorative = new  WesterosBlocksCreativeTab("WesterosDeco", "Westeros Decorative", "metal_block_0_stair_1");
    public static final CreativeTabs tabWesterosPlants = new  WesterosBlocksCreativeTab("WesterosPlants", "Westeros Plants", "yellow_flower_block_0");
    public static final CreativeTabs tabWesterosSounds = new  WesterosBlocksCreativeTab("WesterosSounds", "Westeros Sounds", "sound_blocks_0");
    
    public static void init() {
        
    }
    
    private String type;
    private Block blk = null;
    public  WesterosBlocksCreativeTab(String id, String label, String type) {
        super(id);
        this.type = type;
        WesterosBlockDef.addCreativeTab(id,  this);
        // Add to our NLS export
        ModelExport.addNLSString("itemGroup." + id, label);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public ItemStack getTabIconItem()
    {
        if (blk == null) {
            blk = WesterosBlocks.findBlockByName(this.type);
            if (blk == null) {
                blk = WesterosBlocks.customBlocks[0];
            }
        }
        return new ItemStack(blk, 1, 0);
    }
}
