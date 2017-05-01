package com.westeroscraft.westerosblocks.items;

import java.util.List;

import com.google.common.collect.ImmutableSet;
import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MultiBlockItem extends ItemBlock {
    private Block blk;

    public MultiBlockItem(Block par1) {
        super(par1);
        setMaxDamage(0);
        setHasSubtypes(true);
        this.blk = par1;
    }

    public Block getBlock() {
        return blk;
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack) {
        return super.getUnlocalizedName() + "_" + itemstack.getMetadata();
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> subItems)
    {        
        if (blk instanceof WesterosBlockLifecycle) {
            WesterosBlockDef def = ((WesterosBlockLifecycle)blk).getWBDefinition();
            def.getStandardCreativeItems(blk, tab, subItems);
        }
    }
    
}

