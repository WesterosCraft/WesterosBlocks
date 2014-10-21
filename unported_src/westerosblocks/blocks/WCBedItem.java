package com.westeroscraft.westerosblocks.blocks;

import com.westeroscraft.westerosblocks.WesterosBlockDef;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class WCBedItem extends ItemBlock
{
    private Block blk;
    private WesterosBlockDef def;
    public static WCBedBlock block;
    public static int lastItemID;
    
    public WCBedItem(int id)
    {
        super(id);
        this.blk = block;
        this.def = block.getWBDefinition();
        this.maxStackSize = 1;
        this.setCreativeTab(def.getCreativeTab());
        lastItemID = this.itemID;
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

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float par8, float par9, float par10)
    {
        if (world.isRemote)
        {
            return true;
        }
        else if (side != 1)
        {
            return false;
        }
        else
        {
            ++y;
            int i1 = MathHelper.floor_double((double)(player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
            byte b0 = 0;
            byte b1 = 0;

            if (i1 == 0)
            {
                b1 = 1;
            }

            if (i1 == 1)
            {
                b0 = -1;
            }

            if (i1 == 2)
            {
                b1 = -1;
            }

            if (i1 == 3)
            {
                b0 = 1;
            }

            if (player.canPlayerEdit(x, y, z, side, stack) && player.canPlayerEdit(x + b0, y, z + b1, side, stack))
            {
                if (world.isAirBlock(x, y, z) && world.isAirBlock(x + b0, y, z + b1) && world.doesBlockHaveSolidTopSurface(x, y - 1, z) && world.doesBlockHaveSolidTopSurface(x + b0, y - 1, z + b1))
                {
                    if (!world.canPlaceEntityOnSide(block.blockID, x, y, z, false, side, player, stack)) {
                        return false;
                    }
                    
                    world.setBlock(x, y, z, blk.blockID, i1, 3);

                    if (world.getBlockId(x, y, z) == blk.blockID)
                    {
                        world.setBlock(x + b0, y, z + b1, blk.blockID, i1 + 8, 3);
                    }

                    --stack.stackSize;
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else
            {
                return false;
            }
        }
    }
}
