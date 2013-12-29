package com.westeroscraft.westerosblocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPane;
import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.ForgeDirection;

public class FixedPane extends BlockPane {

    protected FixedPane(int blockID, String txt1, String txt2,
            Material material, boolean canDrop) {
        super(blockID, txt1, txt2, material, canDrop);
    }

    @Override
    public boolean canPaneConnectTo(IBlockAccess access, int x, int y, int z, ForgeDirection dir)
    {
        int id = access.getBlockId(x+dir.offsetX, y+dir.offsetY, z+dir.offsetZ);
        if (canThisPaneConnectToThisBlockID(id) || access.isBlockSolidOnSide(x+dir.offsetX, y+dir.offsetY, z+dir.offsetZ, dir.getOpposite(), false))
            return true;
        if ((Block.blocksList[id] instanceof BlockPane) && (this.blockMaterial == Block.blocksList[id].blockMaterial))
            return true;
        return false;
    }
}
