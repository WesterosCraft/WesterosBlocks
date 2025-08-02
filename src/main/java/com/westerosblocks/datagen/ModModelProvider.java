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
                registerCustomSolidBlock(bsmg, ModBlocks.SIX_SIDED_BIRCH).texture("bark/birch/side").build();
                registerCustomSolidBlock(bsmg, ModBlocks.SIX_SIDED_JUNGLE).texture("bark/jungle/side").build();
                registerCustomSolidBlock(bsmg, ModBlocks.SIX_SIDED_OAK).texture("bark/oak/side").build();
                registerCustomSolidBlock(bsmg, ModBlocks.SIX_SIDED_SPRUCE).texture("bark/spruce/side").build();
                registerCustomSolidBlock(bsmg, ModBlocks.SIX_SIDED_STONE_SLAB).texture("ashlar_half/white/tile")
                                .build();
                registerCustomSolidBlock(bsmg, ModBlocks.APPLE_BASKET).textures(
                                "crate_block/basket_bottom",
                                "crate_block/basket_apple",
                                "crate_block/basket_side").build();
                registerCustomSolidBlock(bsmg, ModBlocks.APPLE_CRATE)
                                .randomTexture(
                                                "crate_block/side_bot1",
                                                "crate_block/crate_top_apples",
                                                "crate_block/side_bot1")
                                .randomTexture(
                                                "crate_block/side_bot2",
                                                "crate_block/crate_top_apples",
                                                "crate_block/side_bot2")
                                .randomTexture(
                                                "crate_block/side_bot3",
                                                "crate_block/crate_top_apples",
                                                "crate_block/side_bot3")
                                .build();
                registerCustomSolidBlock(bsmg, ModBlocks.APPROVAL_UTILITY_BLOCK).texture("utility_block/approved")
                                .build();
                registerCustomSolidBlock(bsmg, ModBlocks.ARBOR_BRICK_ORNATE).texture("ashlar_engraved/arbor/all")
                                .build();
                registerCustomSolidBlock(bsmg, ModBlocks.BENCH_BUTCHER_KNIVES)
                                .textures(
                                                "bench_block/spruce_top",
                                                "bench_block/crafting_table_top",
                                                "bench_block/bench_butcher_knives")
                                .build();
                registerCustomSolidBlock(bsmg, ModBlocks.BENCH_CARPENTRY_HAMMER_SAW).textures(
                                "bench_block/spruce_top",
                                "bench_block/crafting_table_top",
                                "bench_block/bench_carpentry_hammer_saw").build();

                registerCustomSolidBlock(bsmg, ModBlocks.BENCH_DRAWERS)
                                .textures(
                                                "bench_block/spruce_top",
                                                "bench_block/crafting_table_top",
                                                "bench_block/bench_drawers")
                                .build();

                registerCustomSolidBlock(bsmg, ModBlocks.BENCH_KITCHEN_KNIVES)
                                .textures(
                                                "bench_block/spruce_top",
                                                "bench_block/crafting_table_top",
                                                "bench_block/bench_kitchen_knives")
                                .build();

                registerCustomSolidBlock(bsmg, ModBlocks.BENCH_KITCHEN_PANS)
                                .textures(
                                                "bench_block/spruce_top",
                                                "bench_block/crafting_table_top",
                                                "bench_block/bench_kitchen_pans")
                                .build();

                registerCustomSolidBlock(bsmg, ModBlocks.BENCH_MASON_HAMMER_MALLET)
                                .textures(
                                                "bench_block/spruce_top",
                                                "bench_block/crafting_table_top",
                                                "bench_block/bench_mason_hammer_mallet")
                                .build();

                registerCustomSolidBlock(bsmg, ModBlocks.BERRY_BASKET).textures(
                                "crate_block/basket_bottom",
                                "crate_block/basket_berry",
                                "crate_block/basket_side").build();

                registerCustomSolidBlock(bsmg, ModBlocks.BERRY_CRATE)
                                .randomTexture(
                                                "crate_block/side_bot1",
                                                "crate_block/crate_top_berry",
                                                "crate_block/side_bot1")
                                .randomTexture(
                                                "crate_block/side_bot2",
                                                "crate_block/crate_top_berry",
                                                "crate_block/side_bot2")
                                .randomTexture(
                                                "crate_block/side_bot3",
                                                "crate_block/crate_top_berry",
                                                "crate_block/side_bot3")
                                .build();

                registerCustomSolidBlock(bsmg, ModBlocks.APRICOT_BASKET).textures(
                                "crate_block/basket_bottom",
                                "crate_block/basket_apricot",
                                "crate_block/basket_side").build();

                // Log Blocks
                registerCustomLogBlock(bsmg, ModBlocks.ARCHERY_TARGET)
                                .textures("archery_target/side", "archery_target/front").build();

                // Slab Blocks
                registerCustomSlabBlock(bsmg, ModBlocks.APPLE_BASKET_SLAB)
                                .textures(
                                                "crate_block/basket_bottom",
                                                "crate_block/basket_apple",
                                                "crate_block/basket_side_slab")
                                .build();
        }

        @Override
        public void generateItemModels(ItemModelGenerator itemModelGenerator) {

        }

}
