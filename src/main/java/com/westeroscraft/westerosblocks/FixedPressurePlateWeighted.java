package com.westeroscraft.westerosblocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockPressurePlate;
import net.minecraft.block.BlockPressurePlateWeighted;
import net.minecraft.block.BlockWall;
import net.minecraft.block.EnumMobType;
import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class FixedPressurePlateWeighted extends BlockPressurePlateWeighted {

    
    public FixedPressurePlateWeighted(int blockID, String texture,
            Material material, int par4) {
        super(blockID, texture, material, par4);
    }

    // Fix to allow any fence to be placed upon
    public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4)
    {
        return par1World.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4) || (Block.blocksList[par1World.getBlockId(par2, par3 - 1, par4)] instanceof BlockFence);
    }

    // Fix to allow any fence to be placed upon
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
        boolean flag = false;

        if (!par1World.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4) && !(Block.blocksList[par1World.getBlockId(par2, par3 - 1, par4)] instanceof BlockFence))
        {
            flag = true;
        }

        if (flag)
        {
            this.dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
            par1World.setBlockToAir(par2, par3, par4);
        }
    }

}
