package com.westerosblocks.datagen;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.WesterosBlocksDefLoader;
import com.westerosblocks.block.ModBlocks;
import com.westerosblocks.block.ModBlock;
import com.westerosblocks.block.ModBlockLifecycle;
import com.westerosblocks.util.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    WesterosBlocksDefLoader.WesterosBlocksConfig config = WesterosBlocksDefLoader.getCustomConfig();

    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup lookup) {
        // Handle custom tags defined in config
        Map<String, FabricTagBuilder> tagBuilders = new HashMap<>();

        if (config.blockTags != null) {
            for (ModTags.ModBlockTag tag : config.blockTags) {
                String tagId = tag.customTag.toLowerCase();
                TagKey<Block> tagKey = TagKey.of(RegistryKey.ofRegistry(Identifier.of("blocks")),
                        Identifier.of(WesterosBlocks.MOD_ID, tagId));

                FabricTagBuilder builder = getOrCreateTagBuilder(tagKey).setReplace(false);
                tagBuilders.put(tagId, builder);

                // Add blocks listed in config tag
                for (String blockId : tag.blockNames) {
                    builder.add(Identifier.of(blockId.toLowerCase()));
                }
            }
        }

        // Handle tags from blocks
        for (Map.Entry<String, Block> entry : ModBlocks.CUSTOM_BLOCKS.entrySet()) {
            Block block = entry.getValue();
            if (block instanceof ModBlockLifecycle wcBlock) {
                ModBlock def = wcBlock.getWBDefinition();

                // Add vanilla tags
                for (String tag : wcBlock.getBlockTags()) {
                    TagKey<Block> tagKey = TagKey.of(RegistryKey.ofRegistry(Identifier.of("blocks")),
                            Identifier.ofVanilla(tag.toLowerCase()));
                    getOrCreateTagBuilder(tagKey).add(block).setReplace(false);
                }

                // Add custom tags
                if (def.customTags != null) {
                    for (String tag : def.customTags) {
                        String tagId = tag.toLowerCase();
                        FabricTagBuilder builder = tagBuilders.get(tagId);
                        if (builder == null) {
                            WesterosBlocks.LOGGER.error("Invalid customTag {} on block {}", tag, entry.getKey());
                            continue;
                        }
                        builder.add(block);
                    }
                }
            }
        }
    }
}
