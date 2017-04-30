package com.westeroscraft.westerosblocks.blocks;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.util.IIcon;

public class ShiftedIcon implements IIcon {
    private IIcon baseIcon;
    private float uOff;

    public ShiftedIcon(IIcon base, boolean pos) {
        baseIcon = base;
        uOff = (base.getMaxU() - base.getMinU()) / 2.0F;
        if (!pos)
            uOff = -uOff;
    }

    public ShiftedIcon(IIcon base, float offset) {
        baseIcon = base;
        uOff = offset * (base.getMaxU() - base.getMinU());
    }

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
