package com.westerosblocks;

import com.westerosblocks.datagen.providers.ModLanguageProvider;
import com.westerosblocks.datagen.providers.ModModelProvider;
import com.westerosblocks.datagen.providers.ModBlockTagProvider;
import com.westerosblocks.datagen.providers.ModItemTagProvider;
import com.westerosblocks.datagen.providers.ModPolytoneProvider;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class WesterosBlocksDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(ModModelProvider::new);
		pack.addProvider(ModLanguageProvider::new);
		pack.addProvider(ModBlockTagProvider::new);
		pack.addProvider(ModItemTagProvider::new);
		pack.addProvider(ModPolytoneProvider::new);
	}
}
