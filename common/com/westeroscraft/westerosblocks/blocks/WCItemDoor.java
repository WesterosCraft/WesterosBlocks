package com.westeroscraft.westerosblocks.blocks;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.westeroscraft.westerosblocks.WesterosBlockDef;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class WCItemDoor extends Item
{
    private Block blk;
    private String unlocname;
    private WesterosBlockDef def;
    
    public WCItemDoor(Block blk, WesterosBlockDef def)
    {
        super(def.itemID);
        this.blk = blk;
        this.def = def;
        this.maxStackSize = 1;
        this.unlocname = def.blockName + "_item";
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

            if (player.canPlayerEdit(x, y, z, side, stack) && player.canPlayerEdit(x, y + 1, z, side, stack))
            {
                if (!blk.canPlaceBlockAt(world, x, y, z))
                {
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
        return unlocname;
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
