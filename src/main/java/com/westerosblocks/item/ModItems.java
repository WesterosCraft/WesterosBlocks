package com.westerosblocks.item;

import com.westerosblocks.WesterosBlocks;

import com.westerosblocks.item.custom.ModShieldItem;

import com.westerosblocks.item.custom.ValyrianSteelSwordItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.SwordItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModItems {
    public static final Item LONGCLAW = registerItem("longclaw",
            new ValyrianSteelSwordItem(ModToolMaterials.VALYRIAN_STEEL, new Item.Settings()
                    .maxDamage(3000)
                    .attributeModifiers(SwordItem.createAttributeModifiers(ModToolMaterials.VALYRIAN_STEEL, 8, -2.8f))));

    public static final Item VALYRIAN_STEEL_INGOT = registerItem("valyrian_steel_ingot",
            new Item(new Item.Settings()));

//    public static final Item STARK_KITE_SHIELD = registerItem("stark_kite_shield",
//            new KiteShieldItem(new Item.Settings().maxDamage(3200), 6, 18, Items.NETHERITE_INGOT, "stark_kite_shield"));
//
    public static final Item TARGARYEN_HEATER_SHIELD = registerItem("targaryen_heater_shield",
            new ModShieldItem(
                    new Item.Settings().maxDamage(2800),
                    4,
                    15,
                    WesterosBlocks.id("geo/item/targaryen_heater_shield.geo.json"),
                    WesterosBlocks.id("textures/item/targaryen_heater_shield.png"),
                    VALYRIAN_STEEL_INGOT));

    public static final Item BLACKFYRE_HEATER_SHIELD = registerItem("blackfyre_heater_shield",
            new ModShieldItem(
                    new Item.Settings().maxDamage(2800),
                    4,
                    15,
                    WesterosBlocks.id("geo/item/blackfyre_heater_shield.geo.json"),
                    WesterosBlocks.id("textures/item/blackfyre_heater_shield.png"),
                    VALYRIAN_STEEL_INGOT));

    public static final Item BLACKWOOD_HEATER_SHIELD = registerItem("blackwood_heater_shield",
            new ModShieldItem(
                    new Item.Settings().maxDamage(2800),
                    4,
                    15,
                    WesterosBlocks.id("geo/item/blackwood_heater_shield.geo.json"),
                    WesterosBlocks.id("textures/item/blackwood_heater_shield.png"),
                    VALYRIAN_STEEL_INGOT));

    public static final Item BRACKEN_HEATER_SHIELD = registerItem("bracken_heater_shield",
            new ModShieldItem(
                    new Item.Settings().maxDamage(2800),
                    4,
                    15,
                    WesterosBlocks.id("geo/item/bracken_heater_shield.geo.json"),
                    WesterosBlocks.id("textures/item/bracken_heater_shield.png"),
                    VALYRIAN_STEEL_INGOT));

    public static final Item TULLY_HEATER_SHIELD = registerItem("tully_heater_shield",
            new ModShieldItem(
                    new Item.Settings().maxDamage(2800),
                    4,
                    15,
                    WesterosBlocks.id("geo/item/tully_heater_shield.geo.json"),
                    WesterosBlocks.id("textures/item/tully_heater_shield.png"),
                    VALYRIAN_STEEL_INGOT));

    public static final Item HEDGE_KNIGHT_HEATER_SHIELD = registerItem("hedge_knight_heater_shield",
            new ModShieldItem(
                    new Item.Settings().maxDamage(2800),
                    4,
                    15,
                    WesterosBlocks.id("geo/item/hedge_knight_heater_shield.geo.json"),
                    WesterosBlocks.id("textures/item/hedge_knight_heater_shield.png"),
                    VALYRIAN_STEEL_INGOT));

    public static final Item LAUGHING_TREE_HEATER_SHIELD = registerItem("laughing_tree_heater_shield",
            new ModShieldItem(
                    new Item.Settings().maxDamage(2800),
                    4,
                    15,
                    WesterosBlocks.id("geo/item/laughing_tree_heater_shield.geo.json"),
                    WesterosBlocks.id("textures/item/laughing_tree_heater_shield.png"),
                    VALYRIAN_STEEL_INGOT));

    public static final Item GREYJOY_ROUND_SHIELD = registerItem("greyjoy_round_shield",
            new ModShieldItem(new Item.Settings().maxDamage(2800),
                    4,
                    15,
                    WesterosBlocks.id("geo/item/greyjoy_round_shield.geo.json"),
                    WesterosBlocks.id("textures/item/greyjoy_round_shield.png"),
                    VALYRIAN_STEEL_INGOT));


    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, WesterosBlocks.id(name), item);
    }

    public static void registerModItems() {
        WesterosBlocks.LOGGER.info("Registering Mod Items for " + WesterosBlocks.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
            // weapons
            entries.add(LONGCLAW);
            // shields
//            entries.add(STARK_KITE_SHIELD);
            entries.add(TARGARYEN_HEATER_SHIELD);
            entries.add(GREYJOY_ROUND_SHIELD);
            entries.add(BLACKFYRE_HEATER_SHIELD);
            entries.add(BLACKWOOD_HEATER_SHIELD);
            entries.add(BRACKEN_HEATER_SHIELD);
            entries.add(TULLY_HEATER_SHIELD);
            entries.add(HEDGE_KNIGHT_HEATER_SHIELD);
            entries.add(LAUGHING_TREE_HEATER_SHIELD);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.add(VALYRIAN_STEEL_INGOT);
        });
    }
}
