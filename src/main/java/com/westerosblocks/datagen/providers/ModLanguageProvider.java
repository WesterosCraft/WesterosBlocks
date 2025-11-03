package com.westerosblocks.datagen.providers;

import java.util.concurrent.CompletableFuture;

import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.data.BlockDefinitionRegistry;
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
                generateTranslationsFromDefinitions(translationBuilder);

                // Items
                translationBuilder.add("tag.item.westerosblocks.c.shield", "shield");
                translationBuilder.add("item.westerosblocks.longclaw", "Longclaw");
                translationBuilder.add("item.westerosblocks.valyrian_steel_ingot", "Valyrian Steel Ingot");
                // translationBuilder.add("item.westerosblocks.lannister_shield", "Lannister
                // Shield");
//                translationBuilder.add("item.westerosblocks.stark_kite_shield", "Stark Kite Shield");
                translationBuilder.add("item.westerosblocks.tully_heater_shield", "Tully Heater Shield");
                translationBuilder.add("item.westerosblocks.hedge_knight_heater_shield", "Hedge Knight Heater Shield");
                translationBuilder.add("item.westerosblocks.laughing_tree_heater_shield", "Laughing Tree Heater Shield");
                translationBuilder.add("item.westerosblocks.targaryen_heater_shield", "Targaryen Heater Shield");
                translationBuilder.add("item.westerosblocks.bracken_heater_shield", "Bracken Heater Shield");
                translationBuilder.add("item.westerosblocks.greyjoy_round_shield", "Greyjoy Round Shield");
                translationBuilder.add("item.westerosblocks.blackfyre_heater_shield", "Blackfyre Heater Shield");
                translationBuilder.add("item.westerosblocks.blackwood_heater_shield", "Blackwood Heater Shield");

                // Config translations
                translationBuilder.add("config.westerosblocks.title", "WesterosBlocks Config");
                translationBuilder.add("config.westerosblocks.category.general", "General");
                translationBuilder.add("config.westerosblocks.dumpWorldPainterCSV", "Dump WorldPainter CSV");
                translationBuilder.add("config.westerosblocks.dumpWorldPainterCSV.tooltip", "Export block data to WorldPainter CSV format");

        }

        private void generateTranslationsFromDefinitions(TranslationBuilder translationBuilder) {
                BlockDefinitionRegistry registry = BlockDefinitionRegistry.getInstance();

                if (!registry.isInitialized()) {
                        return;
                }

                for (BlockDefinition definition : registry.getAllDefinitions()) {
                        // Skip test blocks
                        if ("westeros_test_tab".equals(definition.getCreativeTab())) {
                                continue;
                        }

                        if (definition.getLabel() != null && !definition.getLabel().isEmpty()) {
                                String translationKey = "block.westerosblocks." + definition.getBlockName();
                                translationBuilder.add(translationKey, definition.getLabel());
                        }
                }
        }

}
