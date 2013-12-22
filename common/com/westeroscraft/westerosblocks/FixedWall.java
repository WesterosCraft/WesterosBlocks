package com.westeroscraft.westerosblocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockWall;
import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;

public class FixedWall extends BlockWall {
    public FixedWall(int id, Block block) {
        super(id, block);
    }

    /**
     * Return whether an adjacent block can connect to a wall.
     */
    @Override
    public boolean canConnectWallTo(IBlockAccess world, int x, int y, int z)
    {
        int id = world.getBlockId(x, y, z);
        Block block = Block.blocksList[id];

        if (id != this.blockID && id != Block.fenceGate.blockID && (!(block instanceof BlockWall))) {
            return block != null && block.blockMaterial.isOpaque() && block.renderAsNormalBlock() ? block.blockMaterial != Material.pumpkin : false;
        }
        else {
            return true;
        }
    }
}
