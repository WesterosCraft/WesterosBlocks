package com.westerosblocks.item;

import com.westerosblocks.WesterosBlocks;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModItems {
//    public static final Item TEST_ITEM = registerItem("test_item", new Item(new Item.Settings()));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, WesterosBlocks.id(name), item);
    }

    public static void registerModItems() {
        WesterosBlocks.LOGGER.info("Registering items for " + WesterosBlocks.MOD_ID);
    }
}
