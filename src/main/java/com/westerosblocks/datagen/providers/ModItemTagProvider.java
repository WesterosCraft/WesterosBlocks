package com.westerosblocks.datagen.providers;

import com.westerosblocks.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider<Item> {
    // Conventional tag for shields
    private static final TagKey<Item> SHIELDS = TagKey.of(RegistryKeys.ITEM, Identifier.of("c", "shield"));

    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.ITEM, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(SHIELDS)
                .setReplace(false)
                .add(ModItems.TARGARYEN_HEATER_SHIELD)
                .add(ModItems.BLACKFYRE_HEATER_SHIELD)
                .add(ModItems.BLACKWOOD_HEATER_SHIELD)
                .add(ModItems.BRACKEN_HEATER_SHIELD)
                .add(ModItems.TULLY_HEATER_SHIELD)
                .add(ModItems.HEDGE_KNIGHT_HEATER_SHIELD)
                .add(ModItems.LAUGHING_TREE_HEATER_SHIELD)
                .add(ModItems.GREYJOY_ROUND_SHIELD)
        ;
    }
}