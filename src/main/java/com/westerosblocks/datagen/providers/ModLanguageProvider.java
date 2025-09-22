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
        }

        private void generateTranslationsFromDefinitions(TranslationBuilder translationBuilder) {
                BlockDefinitionRegistry registry = BlockDefinitionRegistry.getInstance();

                if (!registry.isInitialized()) {
                        return;
                }

                for (BlockDefinition definition : registry.getAllDefinitions()) {
                        if (definition.getLabel() != null && !definition.getLabel().isEmpty()) {
                                String translationKey = "block.westerosblocks." + definition.getBlockName();
                                translationBuilder.add(translationKey, definition.getLabel());
                        }
                }
        }

}
