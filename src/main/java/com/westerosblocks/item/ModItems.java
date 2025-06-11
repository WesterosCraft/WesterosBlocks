package com.westerosblocks.item;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.item.custom.HeaterShieldItem;
import com.westerosblocks.item.custom.KiteShieldItem;
import com.westerosblocks.item.custom.LongclawItem;
import com.westerosblocks.item.custom.RoundShieldItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModItems {
    public static final Item LONGCLAW = registerItem("longclaw",
            new LongclawItem(ModToolMaterials.VALYRIAN_STEEL, new Item.Settings()
                    .attributeModifiers(SwordItem.createAttributeModifiers(ToolMaterials.NETHERITE, 7, -3.4f))));

    // TODO make the settings make sense
    public static final Item STARK_KITE_SHIELD = registerItem("stark_kite_shield",
            new KiteShieldItem(new Item.Settings().maxDamage(2800), 8, 15, Items.NETHERITE_INGOT, "stark_kite_shield"));

    public static final Item TULLY_HEATER_SHIELD = registerItem("tully_heater_shield",
            new HeaterShieldItem(new Item.Settings().maxDamage(2800), 3, 15, Items.NETHERITE_INGOT, "tully_heater_shield"));

    public static final Item GREYJOY_ROUND_SHIELD = registerItem("greyjoy_round_shield",
            new RoundShieldItem(new Item.Settings().maxDamage(2800), 3, 15, Items.NETHERITE_INGOT, "greyjoy_round_shield"));

    public static final Item TARGARYEN_HEATER_SHIELD = registerItem("targaryen_heater_shield",
            new HeaterShieldItem(new Item.Settings().maxDamage(2800), 3, 15, Items.NETHERITE_INGOT, "targaryen_heater_shield"));

    public static final Item BLACKFYRE_HEATER_SHIELD = registerItem("blackfyre_heater_shield",
            new HeaterShieldItem(new Item.Settings().maxDamage(2800), 3, 15, Items.NETHERITE_INGOT, "blackfyre_heater_shield"));

    public static final Item BLACKWOOD_HEATER_SHIELD = registerItem("blackwood_heater_shield",
            new HeaterShieldItem(new Item.Settings().maxDamage(2800), 3, 15, Items.NETHERITE_INGOT, "blackwood_heater_shield"));

    public static final Item BRACKEN_HEATER_SHIELD = registerItem("bracken_heater_shield",
            new HeaterShieldItem(new Item.Settings().maxDamage(2800), 3, 15, Items.NETHERITE_INGOT, "bracken_heater_shield"));

    public static final Item HEDGE_KNIGHT_HEATER_SHIELD = registerItem("hedge_knight_heater_shield",
            new HeaterShieldItem(new Item.Settings().maxDamage(2800), 3, 15, Items.NETHERITE_INGOT, "hedge_knight_heater_shield"));

    public static final Item LAUGHING_TREE_HEATER_SHIELD = registerItem("laughing_tree_heater_shield",
            new HeaterShieldItem(new Item.Settings().maxDamage(2800), 3, 15, Items.NETHERITE_INGOT, "laughing_tree_heater_shield"));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, WesterosBlocks.id(name), item);
    }

    public static void registerModItems() {
        WesterosBlocks.LOGGER.info("Registering Mod Items for " + WesterosBlocks.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
            // weapons
            entries.add(LONGCLAW);
            // shields
            entries.add(STARK_KITE_SHIELD);
            entries.add(TULLY_HEATER_SHIELD);
            entries.add(GREYJOY_ROUND_SHIELD);
            entries.add(BLACKFYRE_HEATER_SHIELD);
            entries.add(BLACKWOOD_HEATER_SHIELD);
            entries.add(BRACKEN_HEATER_SHIELD);
            entries.add(TARGARYEN_HEATER_SHIELD);
            entries.add(HEDGE_KNIGHT_HEATER_SHIELD);
            entries.add(LAUGHING_TREE_HEATER_SHIELD);
        });
    }
}
