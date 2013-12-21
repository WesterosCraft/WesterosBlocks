package com.westeroscraft.westerosblocks.blocks;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class WCLayerItem extends ItemBlockWithMetadata
{
    public static WCLayerBlock block;
    
    public WCLayerItem(int par1)
    {
        super(par1, block);
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float par8, float par9, float par10)
    {
        if (stack.stackSize == 0)
        {
            return false;
        }
        else if (!player.canPlayerEdit(x, y, z, side, stack))
        {
            return false;
        }
        else
        {
            int blkid = world.getBlockId(x, y, z);
            if (blkid == this.getBlockID())
            {
                if (!world.canPlaceEntityOnSide(block.blockID, x, y, z, false, side, player, stack)) {
                    return false;
                }
                WCLayerBlock block = (WCLayerBlock) Block.blocksList[blkid];
                int meta = world.getBlockMetadata(x, y, z);
                int k1 = meta % block.layerCount;

                if ((k1 < (block.layerCount-1)) && world.checkNoEntityCollision(block.getCollisionBoundingBoxFromPool(world, x, y, z)) && world.setBlockMetadataWithNotify(x, y, z, k1 + 1, 3))
                {
                    world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), block.stepSound.getPlaceSound(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
                    --stack.stackSize;
                    return true;
                }
            }

            return super.onItemUse(stack, player, world, x, y, z, side, par8, par9, par10);
        }
    }
}
