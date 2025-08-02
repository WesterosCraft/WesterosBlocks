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
                // Register SIX_SIDED_BIRCH with custom texture path using the new pattern
                registerCustomSolidBlock(bsmg, ModBlocks.SIX_SIDED_BIRCH,
                                "bark/birch/side");
        }

        @Override
        public void generateItemModels(ItemModelGenerator itemModelGenerator) {

        }

}
