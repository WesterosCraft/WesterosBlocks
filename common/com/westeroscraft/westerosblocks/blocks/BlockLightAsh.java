package com.westeroscraft.westerosblocks.blocks;

import com.westeroscraft.westerosblocks.WesterosBlocksCreativeTab;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.Block;
import net.minecraft.block.BlockMycelium;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;

public class BlockLightAsh extends BlockMycelium {
    @SideOnly(Side.CLIENT)
    private Icon top;
    
    public BlockLightAsh(int par1) {
        super(par1);
        this.setCreativeTab(WesterosBlocksCreativeTab.tabWesterosBlocks);
    }
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        blockIcon = iconRegister.registerIcon("westerosblocks:ash_light/ash_light_side");
        top = iconRegister.registerIcon("westerosblocks:ash_light/ash_light_top");
    }
    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int par1, int par2)
    {
        return par1 == 1 ? this.top : (par1 == 0 ? Block.dirt.getBlockTextureFromSide(par1) : this.blockIcon);
    }
    @SideOnly(Side.CLIENT)
    @Override
    public Icon getBlockTexture(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        // Don't want snow effects with our ash block
        return this.getIcon(side, blockAccess.getBlockMetadata(x, y, z));
    }
    //TODO: Do we want to to turn off random ticking on this?
}
