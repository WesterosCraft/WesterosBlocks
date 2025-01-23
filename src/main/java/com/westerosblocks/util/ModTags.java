package com.westerosblocks.util;

import com.westerosblocks.WesterosBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModTags {
    public static class Blocks {
//        public static final TagKey<Block> NEEDS_PINK_GARNET_TOOL = createTag("needs_pink_garnet_tool");
//        public static final TagKey<Block> INCORRECT_FOR_PINK_GARNET_TOOL = createTag("incorrect_for_pink_garnet_tool");

        private static TagKey<Block> createTag(String name) {
            return TagKey.of(RegistryKeys.BLOCK, WesterosBlocks.id(name));
        }
    }


    // Register any item tags here if we get any
//    public static class Items {
//        private static TagKey<Item> createTag(String name) {
//            return TagKey.of(RegistryKeys.ITEM, Identifier.of(WesterosBlocks.MOD_ID, name));
//        }
//    }
}
