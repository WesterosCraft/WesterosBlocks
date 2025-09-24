package com.westerosblocks.datagen.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.data.BlockDefinition;

import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.BlockStateVariant;
import net.minecraft.data.client.BlockStateVariantMap;
import net.minecraft.data.client.Models;
import net.minecraft.data.client.TextureKey;
import net.minecraft.data.client.TextureMap;
import net.minecraft.data.client.VariantSettings;
import net.minecraft.data.client.VariantsBlockStateSupplier;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction.Axis;

public class LogBlockExporter extends BaseBlockExporter {
        /**
         * Registers a custom log block with separate textures for side and end, and
         * optional UV locking
         */
        public static void registerCustomLogBlock(BlockStateModelGenerator generator, Block block,
                        String sideTexture, String endTexture, boolean uvLocked) {
                if (sideTexture.isEmpty() || endTexture.isEmpty()) {
                        throw new IllegalArgumentException("Side and end textures are required for log blocks");
                }

                String blockName = getBlockName(block);

                // Create texture maps for side and end
                TextureMap sideTextureMap = new TextureMap()
                                .put(TextureKey.SIDE, Identifier.of(WesterosBlocks.MOD_ID, "block/" + sideTexture))
                                .put(TextureKey.END, Identifier.of(WesterosBlocks.MOD_ID, "block/" + endTexture))
                                .put(TextureKey.PARTICLE, Identifier.of(WesterosBlocks.MOD_ID, "block/" + sideTexture));

                // Create three separate models for each axis
                Identifier modelY = Models.CUBE_COLUMN.upload(
                                Identifier.of(WesterosBlocks.MOD_ID, "block/" + blockName + "/y"),
                                sideTextureMap, generator.modelCollector);

                Identifier modelX = Models.CUBE_COLUMN.upload(
                                Identifier.of(WesterosBlocks.MOD_ID, "block/" + blockName + "/x"),
                                sideTextureMap, generator.modelCollector);

                Identifier modelZ = Models.CUBE_COLUMN.upload(
                                Identifier.of(WesterosBlocks.MOD_ID, "block/" + blockName + "/z"),
                                sideTextureMap, generator.modelCollector);

                // Create blockstate with axis rotation referencing the three models
                generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block)
                                .coordinate(BlockStateVariantMap
                                                .create(net.minecraft.state.property.Properties.AXIS)
                                                .register(Axis.Y,
                                                                BlockStateVariant.create().put(VariantSettings.MODEL,
                                                                                modelY))
                                                .register(Axis.X,
                                                                BlockStateVariant.create().put(VariantSettings.MODEL,
                                                                                modelX).put(VariantSettings.X,
                                                                                                VariantSettings.Rotation.R90)
                                                                                .put(VariantSettings.Y,
                                                                                                VariantSettings.Rotation.R90))
                                                .register(Axis.Z,
                                                                BlockStateVariant.create().put(VariantSettings.MODEL,
                                                                                modelZ).put(VariantSettings.X,
                                                                                                VariantSettings.Rotation.R90))));

                // Register item model using the Y model (vertical orientation)
                generator.registerParentedItemModel(block, modelY);
        }

        /**
         * Registers a custom log block using BlockDefinition
         */
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

                registerCustomLogBlock(generator, block, sideTexture, endTexture, false);
        }
}
