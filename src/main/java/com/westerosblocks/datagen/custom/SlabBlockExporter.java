package com.westerosblocks.datagen.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.datagen.ModModels;
import com.westerosblocks.datagen.ModTextureMap;

import net.minecraft.block.Block;
import net.minecraft.block.enums.SlabType;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.BlockStateVariant;
import net.minecraft.data.client.BlockStateVariantMap;
import net.minecraft.data.client.Models;
import net.minecraft.data.client.TextureMap;
import net.minecraft.data.client.VariantSettings;
import net.minecraft.data.client.VariantsBlockStateSupplier;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;

public class SlabBlockExporter extends BaseBlockExporter {

        /**
         * Uploads a custom model with the slab parent
         */
        private static Identifier uploadSlabModel(Identifier modelId, TextureMap textureMap,
                        BlockStateModelGenerator generator) {
                return ModModels.SLAB_BOTTOM.upload(modelId, textureMap, generator.modelCollector);
        }

        /**
         * Uploads a custom model with the slab_top parent
         */
        private static Identifier uploadSlabTopModel(Identifier modelId, TextureMap textureMap,
                        BlockStateModelGenerator generator) {
                return ModModels.SLAB_TOP.upload(modelId, textureMap, generator.modelCollector);
        }

        /**
         * Registers a custom slab block with separate textures for all 6 sides
         */
        public static void registerCustomSlabBlock(BlockStateModelGenerator generator, Block block,
                        String... texturePaths) {
                validateTexturePaths(texturePaths, 1);

                // Fill remaining slots with the last texture if less than 6 provided
                String[] filledTextures = fillTextureArray(texturePaths);

                String blockName = getBlockName(block);

                // Create texture maps using ModTextureMap utility for all 6 sides
                TextureMap bottomTextureMap = ModTextureMap.customAllSides(filledTextures);
                TextureMap topTextureMap = ModTextureMap.customAllSides(filledTextures);
                TextureMap fullTextureMap = ModTextureMap.customAllSides(filledTextures);

                // Create custom slab models using the same half_slab parent for both variants
                Identifier bottomModelId = uploadSlabModel(
                                Identifier.of(WesterosBlocks.MOD_ID,
                                                "block/" + blockName + "/" + blockName + "_bottom"),
                                bottomTextureMap, generator);

                Identifier topModelId = uploadSlabTopModel(
                                Identifier.of(WesterosBlocks.MOD_ID, "block/" + blockName + "/" + blockName + "_top"),
                                topTextureMap, generator);

                // Full block uses cube model
                Identifier fullModelId = Models.CUBE.upload(
                                Identifier.of(WesterosBlocks.MOD_ID,
                                                "block/" + blockName + "/" + blockName + "_double"),
                                fullTextureMap, generator.modelCollector);

                // Create blockstate with slab variants
                generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block)
                                .coordinate(BlockStateVariantMap
                                                .create(Properties.SLAB_TYPE)
                                                .register(SlabType.BOTTOM,
                                                                BlockStateVariant.create().put(VariantSettings.MODEL,
                                                                                bottomModelId))
                                                .register(SlabType.TOP,
                                                                BlockStateVariant.create().put(VariantSettings.MODEL,
                                                                                topModelId))
                                                .register(SlabType.DOUBLE, BlockStateVariant.create()
                                                                .put(VariantSettings.MODEL, fullModelId))));

                // Register item model using the bottom variant
                generator.registerParentedItemModel(block, bottomModelId);
        }

        /**
         * Registers a custom slab block with separate textures for bottom, top, sides,
         * and full block (legacy method for backward compatibility)
         */
        public static void registerCustomSlabBlock(BlockStateModelGenerator generator, Block block,
                        String bottomTexture, String topTexture, String sideTexture, String fullTexture) {
                // Use the new method with side texture for all sides
                registerCustomSlabBlock(generator, block, bottomTexture, topTexture,
                                sideTexture, sideTexture, sideTexture, sideTexture);
        }
}
