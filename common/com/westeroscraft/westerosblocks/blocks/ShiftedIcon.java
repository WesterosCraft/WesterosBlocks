package com.westeroscraft.westerosblocks.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.Icon;

public class ShiftedIcon implements Icon {
    private Icon baseIcon;
    private float uOff;

    public ShiftedIcon(Icon base, boolean pos) {
        baseIcon = base;
        uOff = (base.getMaxU() - base.getMinU()) / 2.0F;
        if (!pos)
            uOff = -uOff;
    }
//    @Override
//    @SideOnly(Side.CLIENT)
//    public int getOriginX() {
//        return baseIcon.getOriginX();
//    }

//    @Override
//    @SideOnly(Side.CLIENT)
//    public int getOriginY() {
//        return baseIcon.getOriginY();
//    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getMinU() {
        return baseIcon.getMinU() + uOff;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getMaxU() {
        return baseIcon.getMaxU() + uOff;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getInterpolatedU(double d0) {
        float f = this.getMaxU() - this.getMinU();
        return this.getMinU() + f * ((float)d0 / 16.0F);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getMinV() {
        return baseIcon.getMinV();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getMaxV() {
        return baseIcon.getMaxV();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getInterpolatedV(double d0) {
        return baseIcon.getInterpolatedV(d0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getIconName() {
        return baseIcon.getIconName();
    }
    @Override
    @SideOnly(Side.CLIENT)
    public int getIconWidth() {
        return baseIcon.getIconWidth();
    }
    @Override
    @SideOnly(Side.CLIENT)
    public int getIconHeight() {
        return baseIcon.getIconHeight();
    }
}
