package com.westerosblocks.item.custom;

// TODO: re-enable when Shield API supports 1.21.11
// import net.fabric_extras.shield_api.item.CustomShieldItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public class ModShieldItem extends Item {
    private final Identifier geoPath;
    private final Identifier texPath;

    public ModShieldItem(
            Identifier geoPath,
            Identifier texPath,
            Item.Settings settings) {
        super(settings);
        this.geoPath = geoPath;
        this.texPath = texPath;
    }

    public Identifier getGeoPath() {
        return geoPath;
    }

    public Identifier getTexPath() {
        return texPath;
    }

    // TODO: re-enable when Shield API supports 1.21.11
    // Original constructor also accepted: equipSound, repairIngredient, attributeModifiers
    // These were passed to CustomShieldItem super constructor
}
