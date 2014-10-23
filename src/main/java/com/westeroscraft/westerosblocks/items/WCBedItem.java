package com.westeroscraft.westerosblocks.items;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.blocks.WCBedBlock;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class WCBedItem extends ItemBlock
{
    private WCBedBlock blk;
    private WesterosBlockDef def;
    public static WCBedBlock block;
    
    public WCBedItem(Block id)
    {
        super(id);
        this.blk = block;
        this.def = block.getWBDefinition();
        this.maxStackSize = 1;
        this.setCreativeTab(def.getCreativeTab());
    }

    @Override
    public String getUnlocalizedName (ItemStack itemstack)
    {
        return blk.getUnlocalizedName();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons (IIconRegister iconRegister)
    {
        def.doStandardItemRegisterIcons(iconRegister);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIconFromDamage(int meta)
    {
        return def.getItemIcon(0);
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public boolean onItemUse(ItemStack item, EntityPlayer player, World world, int x, int y, int z, int side, float p_77648_8_, float p_77648_9_, float p_77648_10_)
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
            BlockBed blockbed = (BlockBed) this.blk;
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

            if (player.canPlayerEdit(x, y, z, side, item) && player.canPlayerEdit(x + b0, y, z + b1, side, item))
            {
                if (world.isAirBlock(x, y, z) && world.isAirBlock(x + b0, y, z + b1) && World.doesBlockHaveSolidTopSurface(world, x, y - 1, z) && World.doesBlockHaveSolidTopSurface(world, x + b0, y - 1, z + b1))
                {
                    world.setBlock(x, y, z, blockbed, i1, 3);

                    if (world.getBlock(x, y, z) == blockbed)
                    {
                        world.setBlock(x + b0, y, z + b1, blockbed, i1 + 8, 3);
                    }

                    --item.stackSize;
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
