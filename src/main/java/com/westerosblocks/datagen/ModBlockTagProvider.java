package com.westerosblocks.datagen;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.WesterosBlocksJsonLoader;
import com.westerosblocks.block.WesterosBlockTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        WesterosBlocksJsonLoader.WesterosBlocksConfig config = WesterosBlocks.getCustomConfig();

        if (config.blockTags != null) {
            for (WesterosBlockTags tag : config.blockTags) {
                for (String blockName : tag.blockNames) {
                    String namespace = blockName.split(":")[0];
                    TagKey<Block> CUSTOM_TAG_NAME = TagKey.of(RegistryKeys.BLOCK,
                            Identifier.of(namespace, tag.customTag));

                    getOrCreateTagBuilder(CUSTOM_TAG_NAME)
                            .add(Registries.BLOCK.get(Identifier.of(namespace, blockName.split(":")[1])));
                }
            }
        }
    }
}
