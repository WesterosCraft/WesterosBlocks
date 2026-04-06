package com.westerosblocks.item.custom;

import net.minecraft.item.Item;

// SwordItem no longer exists in 1.21.11 - sword behavior is now applied via Item.Settings.sword()
public class ValyrianSteelSwordItem extends Item {
    public ValyrianSteelSwordItem(Settings settings) {
        super(settings);
    }
}
