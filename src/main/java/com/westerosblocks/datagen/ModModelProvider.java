package com.westerosblocks.datagen;

import com.westerosblocks.block.ModBlocks;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;

import static com.westerosblocks.datagen.ModBlockStateModelGenerator.*;

public class ModModelProvider extends FabricModelProvider {

        private final FabricDataOutput output;

        public ModModelProvider(FabricDataOutput output) {
                super(output);
                this.output = output;
        }

        @Override
        public void generateBlockStateModels(BlockStateModelGenerator bsmg) {
                // Create the custom model provider for custom textures
                CustomModelProvider customModelProvider = new CustomModelProvider(output);

                // SOLID BLOCKS
                registerCustomSolidBlock(
                                bsmg,
                                ModBlocks.SIX_SIDED_BIRCH,
                                ModTextureMap.customAll(ModBlocks.SIX_SIDED_BIRCH,
                                                ""));

                // Generate the custom models
                customModelProvider.generateBlockStateModels(bsmg);
        }

        @Override
        public void generateItemModels(ItemModelGenerator itemModelGenerator) {

        }

}
