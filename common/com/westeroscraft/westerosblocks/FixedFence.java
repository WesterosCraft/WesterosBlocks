package com.westeroscraft.westerosblocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;

public class FixedFence extends BlockFence {
    public FixedFence(int id, String txtid, Material material) {
        super(id, txtid, material);
    }
    /**
     * Returns true if the specified block can be connected by a fence
     */
    @Override
    public boolean canConnectFenceTo(IBlockAccess world, int x, int y, int z) {
        int id = world.getBlockId(x, y, z);
        Block block = Block.blocksList[id];

        if ((id != this.blockID) && (id != Block.fenceGate.blockID) && (!(block instanceof BlockFence))) {
            return block != null && block.blockMaterial.isOpaque() && block.renderAsNormalBlock() ? block.blockMaterial != Material.pumpkin : false;
        }
        else {
            return true;
        }
    }
}
