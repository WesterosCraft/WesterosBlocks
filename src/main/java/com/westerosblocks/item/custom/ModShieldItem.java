package com.westerosblocks.item.custom;

import com.github.crimsondawn45.fabricshieldlib.lib.object.FabricShieldItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public class ModShieldItem extends FabricShieldItem {
    private final Identifier geoPath;
    private final Identifier texPath;

    public ModShieldItem(Settings settings, int coolDownTicks, int enchantability, Identifier geoPath, Identifier texPath, Item... repairItems) {
        super(settings, coolDownTicks, enchantability, repairItems);
        this.geoPath = geoPath;
        this.texPath = texPath;
    }

    public Identifier getGeoPath() {
        return geoPath;
    }

    public Identifier getTexPath() {
        return texPath;
    }
}
