package com.westerosblocks.datagen.custom;

import com.westerosblocks.datagen.ModModels;
import com.westerosblocks.datagen.ModTextureMap;
import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.Block;
import net.minecraft.block.enums.SlabType;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;

import java.util.List;

/**
 * Simplified slab block exporter following block-models.md patterns.
 * Handles bottom, top, and double slab variants cleanly.
 */
public class SlabBlockExporter extends BaseBlockExporter {

        /**
         * Registers a slab block with multiple textures.
         * Texture order: down, up, north, south, east, west
         * Follows block-models.md section on blockstate variants.
         */
        public static void registerCustomSlabBlock(BlockStateModelGenerator generator, Block block, String... texturePaths) {
                validateTexturePaths(texturePaths, 1);

                String[] filledTextures = fillTextureArray(texturePaths);
                TextureMap textureMap = ModTextureMap.customAllSides(filledTextures);

                // Upload models for all three slab variants
                Identifier bottomModelId = ModModels.SLAB_BOTTOM.upload(createNestedModelId(block, getBlockName(block) + "_bottom"), textureMap, generator.modelCollector);
                Identifier topModelId = ModModels.SLAB_TOP.upload(createNestedModelId(block, getBlockName(block) + "_top"), textureMap, generator.modelCollector);
                Identifier fullModelId = Models.CUBE.upload(createNestedModelId(block, getBlockName(block) + "_double"), textureMap, generator.modelCollector);

                // Create blockstate with slab type variants
                BlockStateVariantMap variants = BlockStateVariantMap.create(Properties.SLAB_TYPE)
                        .register(SlabType.BOTTOM, createVariant(bottomModelId))
                        .register(SlabType.TOP, createVariant(topModelId))
                        .register(SlabType.DOUBLE, createVariant(fullModelId));

                generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block).coordinate(variants));
                registerParentedItemModel(generator, block, bottomModelId);
        }

        /**
         * Registers a slab block from a BlockDefinition.
         * Uses textures from the definition's texture array.
         */
        public static void registerCustomSlabBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
                List<String> textureList = definition.getTextures();
                if (textureList != null && !textureList.isEmpty()) {
                        String[] textures = textureList.toArray(new String[0]);
                        registerCustomSlabBlock(generator, block, textures);
                }
        }
}
