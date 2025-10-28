package com.westerosblocks.datagen.custom;

import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.datagen.ModModels;

import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.BlockStateVariant;
import net.minecraft.data.client.BlockStateVariantMap;
import net.minecraft.data.client.TextureKey;
import net.minecraft.data.client.TextureMap;
import net.minecraft.data.client.VariantSettings;
import net.minecraft.data.client.VariantsBlockStateSupplier;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction.Axis;

public class LogBlockExporter extends BaseBlockExporter {


        public static void registerLogBlock(BlockStateModelGenerator generator, Block block,
                        String sideTexture, String endTexture, boolean uvLocked) {
                if (sideTexture.isEmpty() || endTexture.isEmpty()) {
                        throw new IllegalArgumentException("Side and end textures are required for log blocks");
                }

                // Create texture map - block-models.md section 5.3: Using Texture Map
                TextureMap textureMap = createLogTextureMap(sideTexture, endTexture);

                // Upload models for each axis - block-models.md section 5.2: Parent Block Model
                Identifier verticalModelId = ModModels.LOG.upload(createNestedModelId(block), textureMap, generator.modelCollector);
                Identifier horizontalModelId = ModModels.LOG_HORIZONTAL.upload(createNestedModelId(block, "horizontal"), textureMap, generator.modelCollector);

                // Create blockstate variants - block-models.md section 5.4: Custom BlockStateSupplier Method
                BlockStateVariantMap variants = createLogVariants(verticalModelId, horizontalModelId);
                generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block).coordinate(variants));

                // Register item model - block-models.md section 5.5: Custom Datagen Method
                registerParentedItemModel(generator, block, verticalModelId);
        }

        public static void registerCustomLogBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
                if (definition.getTextures() == null || definition.getTextures().size() < 2) {
                        throw new IllegalArgumentException("Log blocks require at least 2 textures: [side, end] or [end, end, side]");
                }

                // For log blocks, texture order is expected to be [side, end] or [end, end, side]
                String sideTexture;
                String endTexture;

                if (definition.getTextures().size() == 2) {
                        // [side, end] format
                        sideTexture = definition.getTextures().get(0);
                        endTexture = definition.getTextures().get(1);
                } else if (definition.getTextures().size() == 3) {
                        // [end, end, side] format (legacy compatibility)
                        sideTexture = definition.getTextures().get(2);
                        endTexture = definition.getTextures().get(0);
                } else {
                        throw new IllegalArgumentException("Log blocks require 2 or 3 textures, got: " + definition.getTextures().size());
                }

                registerLogBlock(generator, block, sideTexture, endTexture, false);
        }


        private static TextureMap createLogTextureMap(String sideTexture, String endTexture) {
                return new TextureMap()
                        .put(TextureKey.SIDE, createBlockIdentifier(sideTexture))
                        .put(TextureKey.END, createBlockIdentifier(endTexture))
                        .put(TextureKey.PARTICLE, createBlockIdentifier(sideTexture));
        }

        private static BlockStateVariantMap createLogVariants(Identifier verticalModelId, Identifier horizontalModelId) {
                return BlockStateVariantMap.create(net.minecraft.state.property.Properties.AXIS)
                        .register(Axis.Y, createVariant(verticalModelId))
                        .register(Axis.Z, BlockStateVariant.create()
                                .put(VariantSettings.MODEL, horizontalModelId)
                                .put(VariantSettings.X, VariantSettings.Rotation.R90))
                        .register(Axis.X, BlockStateVariant.create()
                                .put(VariantSettings.MODEL, horizontalModelId)
                                .put(VariantSettings.X, VariantSettings.Rotation.R90)
                                .put(VariantSettings.Y, VariantSettings.Rotation.R90));
        }
}
