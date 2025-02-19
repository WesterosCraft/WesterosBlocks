package com.westerosblocks.item.custom;
import com.github.crimsondawn45.fabricshieldlib.lib.object.FabricShieldItem;
import net.minecraft.item.Item;

public class ModShieldItem extends FabricShieldItem {
    public ModShieldItem(Settings settings, int cooldownTicks, int enchantability, Item repairItems) {
        super(settings, cooldownTicks, enchantability, repairItems);
    }
}