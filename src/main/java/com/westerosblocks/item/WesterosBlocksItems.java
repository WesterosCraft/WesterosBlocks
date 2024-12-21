package com.westerosblocks.item;

import com.westerosblocks.WesterosBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class WesterosBlocksItems {
//    public static final Item TEST_ITEM = registerItem("test_item", new Item(new Item.Settings()));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(WesterosBlocks.MOD_ID, name), item);
    }

    public static void registerModItems() {
        WesterosBlocks.LOGGER.info("Registering items for " + WesterosBlocks.MOD_ID);
    }
}
