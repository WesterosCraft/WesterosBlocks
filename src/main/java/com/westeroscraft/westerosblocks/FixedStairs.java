package com.westeroscraft.westerosblocks;

import com.westeroscraft.westerosblocks.blocks.ShiftedIcon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.util.Icon;

public class FixedStairs extends BlockStairs {

    private Icon offsetIconXN = null;
    private Icon offsetIconXP = null;
    private Icon offsetIconZN = null;
    private Icon offsetIconZP = null;

    public FixedStairs(int par1, Block par2Block, int par3) {
        super(par1, par2Block, par3);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int meta) {
        Icon ico = super.getIcon(side, meta);
        if (side == 2) {
            if(this.getBlockBoundsMaxX() == 0.5) {
                if (offsetIconXP == null) {
                    offsetIconXP = new ShiftedIcon(ico, true);
                }
                ico = offsetIconXP;
            }
            else if(this.getBlockBoundsMinX() == 0.5) {
                if (offsetIconXN == null) {
                    offsetIconXN = new ShiftedIcon(ico, false);
                }
                ico = offsetIconXN;
            }
        }
        else if (side == 5) {
            if (this.getBlockBoundsMaxZ() == 0.5) {
                if (offsetIconZP == null) {
                    offsetIconZP = new ShiftedIcon(ico, true);
                }
                ico = offsetIconZP;
            }
            else if (this.getBlockBoundsMinZ() == 0.5) {
                if (offsetIconZN == null) {
                    offsetIconZN = new ShiftedIcon(ico, false);
                }
                ico = offsetIconZN;
            }
        }
        return ico;
    }

}
