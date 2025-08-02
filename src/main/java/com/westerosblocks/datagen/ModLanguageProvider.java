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
        translationBuilder.add("block.westerosblocks.bench_drawers", "Bench Drawers");
        translationBuilder.add("block.westerosblocks.bench_kitchen_knives", "Bench Kitchen Knives");
        translationBuilder.add("block.westerosblocks.bench_kitchen_pans", "Bench Kitchen Pans");
        translationBuilder.add("block.westerosblocks.bench_mason_hammer_mallet", "Bench Mason Hammer Mallet");
        translationBuilder.add("block.westerosblocks.berry_basket", "Berry Basket");
        translationBuilder.add("block.westerosblocks.berry_crate", "Berry Crate");
        translationBuilder.add("block.westerosblocks.apricot_basket", "Apricot Basket");
        translationBuilder.add("block.westerosblocks.black_brick_engraved", "Ashlar Engraved Black");
        translationBuilder.add("block.westerosblocks.bluegreen_carved_sandstone", "Blue/Green Carved Sandstone");
        translationBuilder.add("block.westerosblocks.bone_dirt", "Bone Dirt");
        translationBuilder.add("block.westerosblocks.bookshelf_abandoned", "Bookshelf Abandoned");
        translationBuilder.add("block.westerosblocks.bookshelf_library", "Bookshelf Library");
        translationBuilder.add("block.westerosblocks.bookshelf_maester", "Bookshelf Maester");
        translationBuilder.add("block.westerosblocks.broken_cabinet", "Broken Cabinet");
        translationBuilder.add("block.westerosblocks.brown_grey_brick_engraved", "Ashlar Engraved Brown Grey");
        translationBuilder.add("block.westerosblocks.cabinet_drawer", "Cabinet Drawer");
        translationBuilder.add("block.westerosblocks.cage", "Cage");
        translationBuilder.add("block.westerosblocks.carrot_basket", "Carrot Basket");
        translationBuilder.add("block.westerosblocks.carrot_crate", "Carrot Crate");
        translationBuilder.add("block.westerosblocks.closed_basket", "Closed Basket");
        translationBuilder.add("block.westerosblocks.closed_cabinet", "Closed Cabinet");
        translationBuilder.add("block.westerosblocks.coarse_dark_red_carved_sandstone",
                "Ashlar Engraved Pale Dark Red");
        translationBuilder.add("block.westerosblocks.coarse_red_carved_sandstone", "Ashlar Engraved Pale Red");
        translationBuilder.add("block.westerosblocks.cobble_keystone", "Cobblestone Keystone Grey");
        translationBuilder.add("block.westerosblocks.coloured_sept_window", "Coloured Sept Window");
        translationBuilder.add("block.westerosblocks.crate", "Crate One");
        translationBuilder.add("block.westerosblocks.crate2", "Crate Two");
        translationBuilder.add("block.westerosblocks.crate3", "Crate Three");
        translationBuilder.add("block.westerosblocks.dark_grey_brick_engraved", "Ashlar Engraved Dark Grey");
        translationBuilder.add("block.westerosblocks.date_basket", "Date Basket");
        translationBuilder.add("block.westerosblocks.dates", "Dates");
        translationBuilder.add("block.westerosblocks.desert_sandstone_engraved", "Ashlar Engraved Sandstone");
        translationBuilder.add("block.westerosblocks.domestic_utility_block", "'Domestic' Utility Block");
        translationBuilder.add("block.westerosblocks.done_utility_block", "'Done' Utility Block");
        translationBuilder.add("block.westerosblocks.dragon_carving", "Dragon Carving");
        translationBuilder.add("block.westerosblocks.empty_barrel", "Empty Barrel");
        translationBuilder.add("block.westerosblocks.empty_cabinet", "Empty Cabinet");
        translationBuilder.add("block.westerosblocks.faith_carved_arbor_brick", "Ashlar Faith Carved Arbor");
        translationBuilder.add("block.westerosblocks.faith_carved_black_brick", "Ashlar Faith Carved Black");
        translationBuilder.add("block.westerosblocks.faith_carved_brown_grey_brick", "Ashlar Faith Carved Brown Grey");
        translationBuilder.add("block.westerosblocks.faith_carved_coarse_red_brick", "Ashlar Faith Carved Pale Red");
        translationBuilder.add("block.westerosblocks.faith_carved_dark_grey_brick", "Ashlar Faith Carved Dark Grey");
        translationBuilder.add("block.westerosblocks.faith_carved_dun_brick", "Ashlar Faith Carved Dun");
        translationBuilder.add("block.westerosblocks.faith_carved_grey_brick", "Ashlar Faith Carved Normal Grey");
        translationBuilder.add("block.westerosblocks.faith_carved_oldtown_brick", "Ashlar Faith Carved Light Oldtown");
        translationBuilder.add("block.westerosblocks.faith_carved_pink_sandstone", "Ashlar Faith Carved Sandy Pink");
        translationBuilder.add("block.westerosblocks.faith_carved_reach_brick", "Ashlar Faith Carved Reach");
        translationBuilder.add("block.westerosblocks.faith_carved_small_stone_brick", "Ashlar Faith Carved Green Grey");
        translationBuilder.add("block.westerosblocks.faith_carved_stone_brick", "Ashlar Faith Carved White");
        translationBuilder.add("block.westerosblocks.faith_carved_stormlands_brick", "Ashlar Faith Carved Stormlands");
        translationBuilder.add("block.westerosblocks.faith_carved_westerlands_brick",
                "Ashlar Faith Carved Westerlands");
        translationBuilder.add("block.westerosblocks.fish_barrel", "Fish Barrel");
        translationBuilder.add("block.westerosblocks.fish_basket", "Fish Basket");
        translationBuilder.add("block.westerosblocks.fish_trap", "Fish Trap");
        translationBuilder.add("block.westerosblocks.flagstone", "Flagstone");
        translationBuilder.add("block.westerosblocks.full_cabinet", "Full Cabinet");
        translationBuilder.add("block.westerosblocks.glowing_embers", "Glowing Embers");
        translationBuilder.add("block.westerosblocks.grain_basket", "Grain Basket");
        translationBuilder.add("block.westerosblocks.grain_crate", "Grain Crate");
        translationBuilder.add("block.westerosblocks.green_grey_brick_engraved", "Ashlar Engraved Green Grey");
        translationBuilder.add("block.westerosblocks.grey_brick_engraved", "Ashlar Engraved Normal Grey");
        translationBuilder.add("block.westerosblocks.grey_keystone", "Grey Keystone");
        translationBuilder.add("block.westerosblocks.high_class_utility_block", "'High Class' Utility Block");
        translationBuilder.add("block.westerosblocks.hop_basket", "Hop Basket");
        translationBuilder.add("block.westerosblocks.hop_crate", "Hop Crate");
        translationBuilder.add("block.westerosblocks.house_count_utility_block", "'House Count' Utility Block");
        translationBuilder.add("block.westerosblocks.industry_utility_block", "'Industry' Utility Block");
        translationBuilder.add("block.westerosblocks.iron_crate", "Iron Crate");
        translationBuilder.add("block.westerosblocks.archery_target", "Archery Target");
        translationBuilder.add("block.westerosblocks.apple_basket_slab", "Apple Basket Slab");
    }

}
