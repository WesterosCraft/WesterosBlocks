package com.westerosblocks.datagen;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.ModBlocks;
import com.westerosblocks.block.WesterosBlockDef;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryWrapper;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class ModLanguageProvider extends FabricLanguageProvider {
    HashMap<String, Block> customBlocks = ModBlocks.getCustomBlocksByName();
    WesterosBlockDef[] customBlockDefs = WesterosBlocks.getCustomBlockDefs();

    public ModLanguageProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, "en_us", registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup registryLookup, TranslationBuilder translationBuilder) {

        for (WesterosBlockDef customBlockDef : customBlockDefs) {
            if (customBlockDef == null) continue;
            Block currentBlock = customBlocks.get(customBlockDef.blockName);

            translationBuilder.add(currentBlock, customBlockDef.label);
        }

    }
}
