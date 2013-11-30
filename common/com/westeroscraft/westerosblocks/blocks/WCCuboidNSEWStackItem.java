package com.westeroscraft.westerosblocks.blocks;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class WCCuboidNSEWStackItem extends MultiBlockItem {

    public WCCuboidNSEWStackItem(int par1) {
        super(par1);
    }
    
    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float par8, float par9, float par10)
    {
        int dir = 0;

        switch (side) {
            case 0:
                --y;
                dir = MathHelper.floor_double((double)((player.rotationYaw + 180.0F) * 4.0F / 360.0F) - 0.5D) & 3;
                break;
            case 1:
                ++y;
                dir = MathHelper.floor_double((double)((player.rotationYaw + 180.0F) * 4.0F / 360.0F) - 0.5D) & 3;
                break;
            case 2:
                --z;
                dir = 1;
                break;
            case 3:
                ++z;
                dir = 3;
                break;
            case 4:
                --x;
                dir = 0;
                break;
            case 5:
                ++x;
                dir = 2;
                break;
        }
        // Check if we can change both blocks
        if (player.canPlayerEdit(x, y, z, side, stack) && player.canPlayerEdit(x, y + 1, z, side, stack)) {
            Block block = getBlock();

            if ((block == null) || (!block.canPlaceBlockAt(world, x, y, z))) {
                return false;
            }
            else {
                placeCuboidBlock(world, x, y, z, dir, block, stack.getItemDamage() & 0x2);
                --stack.stackSize;
                return true;
            }
        }
        else {
            return false;
        }
    }
    public static void placeCuboidBlock(World world, int x, int y, int z, int side, Block block, int meta)
    {
        meta += (4 * side);

        world.setBlock(x, y, z, block.blockID, meta, 2);
        world.setBlock(x, y + 1, z, block.blockID, meta | 0x1, 2);
        world.notifyBlocksOfNeighborChange(x, y, z, block.blockID);
        world.notifyBlocksOfNeighborChange(x, y + 1, z, block.blockID);
    }
}
