package com.westerosblocks.datagen;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;

public class ModLanguageProvider extends FabricLanguageProvider {

    public ModLanguageProvider(FabricDataOutput dataOutput,
            CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, "en_us", registryLookup);
    }

    @Override
    public void generateTranslations(WrapperLookup registryLookup, TranslationBuilder translationBuilder) {
        translationBuilder.add("block.westerosblocks.6sided_birch", "Six-Sided Birch");
        translationBuilder.add("block.westerosblocks.6sided_jungle", "Six-Sided Jungle");
        translationBuilder.add("block.westerosblocks.6sided_oak", "Six-Sided Oak");
        translationBuilder.add("block.westerosblocks.6sided_spruce", "Six-Sided Spruce");
        translationBuilder.add("block.westerosblocks.6sided_stone_slab", "Six-Sided Stone Slab");
        translationBuilder.add("block.westerosblocks.apple_basket", "Apple Basket");
        translationBuilder.add("block.westerosblocks.apple_crate", "Apple Crate");
        translationBuilder.add("block.westerosblocks.approval_utility_block", "'Approval' Utility Block");
        translationBuilder.add("block.westerosblocks.arbor_brick_ornate", "Ashlar Engraved Arbor");
        translationBuilder.add("block.westerosblocks.bench_butcher_knives", "Bench Butcher Knives");
        translationBuilder.add("block.westerosblocks.bench_carpentry_hammer_saw", "Bench Carpentry Hammer Saw");
    }

}
