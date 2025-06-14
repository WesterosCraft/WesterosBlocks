package com.westerosblocks.datagen;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.WesterosBlocksDefLoader;
import com.westerosblocks.item.ModItems;
import com.westerosblocks.util.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.lang.reflect.Field;
import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {
    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }
    WesterosBlocksDefLoader.WesterosBlocksConfig config = WesterosBlocksDefLoader.getCustomConfig();

    @SuppressWarnings("unchecked")
    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        // Add Valyrian steel tools tag
        getOrCreateTagBuilder(TagKey.of(RegistryKey.ofRegistry(Identifier.of("items")),
                WesterosBlocks.id("valyrian_steel_tools")))
                .add(ModItems.LONGCLAW)
                .setReplace(false);

        if (config.itemTags != null) {
            for (ModTags.ModItemTag tag : config.itemTags) {
                String tagId = tag.customTag.toLowerCase();
                TagKey<Item> tagKey;

                if (tag.isConventional) {
                    // For conventional tags, look up in ConventionalItemTags first
                    try {
                        Field field = ConventionalItemTags.class.getField(tagId.toUpperCase());
                        tagKey = (TagKey<Item>) field.get(null);
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        // If not found in ConventionalItemTags, create in 'c' namespace
                        tagKey = TagKey.of(RegistryKey.ofRegistry(Identifier.of("items")),
                                Identifier.of("c", tagId));
                        WesterosBlocks.LOGGER.warn("Conventional tag {} not found in ConventionalItemTags, creating manually", tagId);
                    }
                } else {
                    // For mod-specific tags, use our mod's namespace
                    tagKey = TagKey.of(RegistryKey.ofRegistry(Identifier.of("items")),
                            Identifier.of(WesterosBlocks.MOD_ID, tagId));
                }

                FabricTagBuilder builder = getOrCreateTagBuilder(tagKey);

                // Add items listed in config tag
                for (String itemId : tag.itemNames) {
                    builder.add(Identifier.of(itemId.toLowerCase())).setReplace(false);
                }
            }
        }
    }
}