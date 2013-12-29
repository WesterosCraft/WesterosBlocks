package com.westeroscraft.westerosblocks.blocks;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.westeroscraft.westerosblocks.WesterosBlockDef;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class WCDoorItem extends ItemBlock
{
    private Block blk;
    private WesterosBlockDef def;
    public static WCDoorBlock block;
    public static int lastItemID;
    
    public WCDoorItem(int id)
    {
        super(id);
        this.blk = block;
        this.def = block.getWBDefinition();
        this.maxStackSize = 1;
        this.setCreativeTab(def.getCreativeTab());
        lastItemID = this.itemID;
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
        if (side != 1)
        {
            return false;
        }
        else
        {
            ++y;

            if (player.canPlayerEdit(x, y, z, side, stack) && player.canPlayerEdit(x, y + 1, z, side, stack))
            {
                if ((!blk.canPlaceBlockAt(world, x, y, z)) ||
                    (!world.canPlaceEntityOnSide(block.blockID, x, y, z, false, side, player, stack))) {
                    return false;
                }
                else
                {
                    int i1 = MathHelper.floor_double((double)((player.rotationYaw + 180.0F) * 4.0F / 360.0F) - 0.5D) & 3;
                    ItemDoor.placeDoorBlock(world, x, y, z, i1, blk);
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
        return blk.getUnlocalizedName() + "_item";
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
        return def.getItemIcon(meta);
    }
}
