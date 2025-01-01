package com.westerosblocks.datagen;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.WesterosBlocksJsonLoader;
import com.westerosblocks.block.ModBlocks;
import com.westerosblocks.block.WesterosBlockTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
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
                TagKey<Block> CUSTOM_TAG_NAME = TagKey.of(RegistryKeys.BLOCK, Identifier.of(WesterosBlocks.MOD_ID, tag.customTag));

                for (String blockName : tag.blockNames) {
                    getOrCreateTagBuilder(CUSTOM_TAG_NAME).add(ModBlocks.findBlockByName(blockName.split(":")[1], blockName.split(":")[0]));
                }
            }
        }
    }
}
