package com.westerosblocks.item.custom;

import net.minecraft.item.ShieldItem;

public class ModShieldItem extends ShieldItem {
    public ModShieldItem(Settings settings) {
        super(settings.maxDamage(336));
    }
}