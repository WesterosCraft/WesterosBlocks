package com.westeroscraft.westerosblocks.blocks;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class WCLayerItem extends ItemBlockWithMetadata
{
    public static WCLayerBlock block;
    private WCLayerBlock lblock;
    
    public WCLayerItem(Block par1)
    {
        super(par1, block);
        lblock = block;
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
        else if (world.isRemote) {
            return true;
        }
        else if (!player.canPlayerEdit(x, y, z, side, stack))
        {
            return false;
        }
        else
        {
            Block blkid = world.getBlock(x, y, z);
            if (blkid == this.field_150939_a)
            {
                int meta = world.getBlockMetadata(x, y, z);
                world.setBlock(x, y, z, Blocks.air, 0, 0);
                if (!world.canPlaceEntityOnSide(block, x, y, z, false, side, player, stack)) {
                    world.setBlock(x, y, z, blkid, meta, 0); // Restore
                    return false;
                }
                int k1 = meta % this.lblock.layerCount;

                if ((k1 < (block.layerCount-1)) && world.checkNoEntityCollision(block.getCollisionBoundingBoxFromPool(world, x, y, z)) && world.setBlock(x, y, z, blkid, k1 + 1, 3))
                {
                    world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), block.stepSound.func_150496_b(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
                    --stack.stackSize;
                    return true;
                }
                else {
                    world.setBlock(x, y, z, blkid, meta, 0); // Restore
                }
            }

            return super.onItemUse(stack, player, world, x, y, z, side, par8, par9, par10);
        }
    }
}
