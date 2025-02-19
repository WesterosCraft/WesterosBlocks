package com.westerosblocks.item;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.item.custom.LongclawItem;
import com.westerosblocks.item.custom.ModShieldItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModItems {
    public static final Item LONGCLAW = registerItem("longclaw",
            new LongclawItem(ModToolMaterials.VALYRIAN_STEEL, new Item.Settings()
                    .attributeModifiers(SwordItem.createAttributeModifiers(ToolMaterials.NETHERITE, 7, -3.4f))));

    public static final Item LANNISTER_SHIELD = registerItem("lannister_shield", new ModShieldItem(new Item.Settings().maxDamage(2500), 10, 13, Items.NETHERITE_INGOT));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, WesterosBlocks.id(name), item);
    }

    public static void registerModItems() {
        WesterosBlocks.LOGGER.info("Registering Mod Items for " + WesterosBlocks.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
            entries.add(LONGCLAW);
            entries.add(LANNISTER_SHIELD);
        });
    }
}
