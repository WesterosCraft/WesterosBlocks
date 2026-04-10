package com.westerosblocks.item.custom;

import net.fabric_extras.shield_api.item.CustomShieldItem;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class ModShieldItem extends CustomShieldItem {
    private final Identifier geoPath;
    private final Identifier texPath;

    public ModShieldItem(
            Identifier geoPath,
            Identifier texPath,
            @Nullable RegistryEntry<SoundEvent> equipSound,
            Supplier<Ingredient> repairIngredient,
            List<Pair<RegistryEntry<EntityAttribute>, EntityAttributeModifier>> attributeModifiers,
            Item.Settings settings) {
        super(equipSound, repairIngredient, attributeModifiers, settings);
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
