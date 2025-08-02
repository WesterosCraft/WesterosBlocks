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
                // Solid Blocks
                registerSimpleCustomSolidBlock(bsmg, ModBlocks.SIX_SIDED_BIRCH,
                                "bark/birch/side");
                registerSimpleCustomSolidBlock(bsmg, ModBlocks.SIX_SIDED_JUNGLE,
                                "bark/jungle/side");
                registerSimpleCustomSolidBlock(bsmg, ModBlocks.SIX_SIDED_OAK,
                                "bark/oak/side");
                registerSimpleCustomSolidBlock(bsmg, ModBlocks.SIX_SIDED_SPRUCE,
                                "bark/spruce/side");
                registerSimpleCustomSolidBlock(bsmg, ModBlocks.SIX_SIDED_STONE_SLAB,
                                "ashlar_half/white/tile");
                registerCustomSolidBlock(bsmg, ModBlocks.APPLE_BASKET,
                                "crate_block/basket_bottom",
                                "crate_block/basket_apple",
                                "crate_block/basket_side");
        }

        @Override
        public void generateItemModels(ItemModelGenerator itemModelGenerator) {

        }

}
