package com.westeroscraft.westerosblocks.blocks;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.westeroscraft.westerosblocks.WesterosBlockDef;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class WCHalfDoorItem extends Item
{
    private Block blk;
    private WesterosBlockDef def;
    
    public WCHalfDoorItem(Block blk, WesterosBlockDef def)
    {
        super(def.blockID);
        this.blk = blk;
        this.def = def;
        this.maxStackSize = 1;
        this.setCreativeTab(def.getCreativeTab());
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float par8, float par9, float par10)
    {
        if (side != 1)
        {
            return false;
        }
        else
        {
            ++y;

            if (player.canPlayerEdit(x, y, z, side, stack))
            {
                if (!blk.canPlaceBlockAt(world, x, y, z))
                {
                    return false;
                }
                else
                {
                    int i1 = MathHelper.floor_double((double)((player.rotationYaw + 180.0F) * 4.0F / 360.0F) - 0.5D) & 3;
                    placeDoorBlock(world, x, y, z, i1, blk);
                    --stack.stackSize;
                    return true;
                }
            }
            else
            {
                return false;
            }
        }
    }
    
    @Override
    public String getUnlocalizedName (ItemStack itemstack)
    {
        return blk.getUnlocalizedName();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons (IconRegister iconRegister)
    {
        def.doStandardItemRegisterIcons(iconRegister);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public Icon getIconFromDamage(int meta)
    {
        return def.getItemIcon(0);
    }
    
    public static void placeDoorBlock(World par0World, int par1, int par2, int par3, int side, Block par5Block)
    {
        byte xoff = 0;
        byte zoff = 0;

        if (side == 0)
        {
            zoff = 1;
        }

        if (side == 1)
        {
            xoff = -1;
        }

        if (side == 2)
        {
            zoff = -1;
        }

        if (side == 3)
        {
            xoff = 1;
        }

        int i1 = (par0World.isBlockNormalCube(par1 - xoff, par2, par3 - zoff) ? 1 : 0);
        int j1 = (par0World.isBlockNormalCube(par1 + xoff, par2, par3 + zoff) ? 1 : 0);
        boolean flag = par0World.getBlockId(par1 - xoff, par2, par3 - zoff) == par5Block.blockID;
        boolean flag1 = par0World.getBlockId(par1 + xoff, par2, par3 + zoff) == par5Block.blockID;
        boolean flag2 = false;

        if (flag && !flag1)
        {
            flag2 = true;
        }
        else if (j1 > i1)
        {
            flag2 = true;
        }

        int meta = side | (flag2 ? 8 : 0);
        par0World.setBlock(par1, par2, par3, par5Block.blockID, meta, 2);
        par0World.notifyBlocksOfNeighborChange(par1, par2, par3, par5Block.blockID);

    }

}
