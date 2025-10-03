package com.westerosblocks.datagen.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.datagen.ModModels;

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

/**
 * Exporter for log/pillar blocks following block-models.md patterns.
 * Generates models with axis rotation and separate side/end textures.
 *
 * <p>Structure follows block-models.md sections 5.2-5.6:
 * <ul>
 *   <li>Model instances (references ModModels.LOG, ModModels.LOG_HORIZONTAL)</li>
 *   <li>TextureMap builders (createLogTextureMap)</li>
 *   <li>BlockStateSupplier methods (createLogVariants)</li>
 *   <li>Clean datagen methods (registerLogBlock)</li>
 *   <li>BlockDefinition integration (registerCustomLogBlock)</li>
 * </ul>
 *
 * @see com.westerosblocks.datagen.ModModels#LOG
 * @see com.westerosblocks.datagen.ModModels#LOG_HORIZONTAL
 */
public class LogBlockExporter extends BaseBlockExporter {

        /**
         * Registers a log block with side and end textures.
         * Follows block-models.md pillar block pattern.
         *
         * <p>Texture order: [sideTexture, endTexture]
         *
         * <p>Generates three axis variants:
         * <ul>
         *   <li>Y-axis (vertical): no rotation</li>
         *   <li>X-axis: 90° X rotation, 90° Y rotation</li>
         *   <li>Z-axis: 90° X rotation</li>
         * </ul>
         *
         * @param generator The BlockStateModelGenerator to register models with
         * @param block The log block to generate models for
         * @param sideTexture Texture path for the log's sides (bark)
         * @param endTexture Texture path for the log's ends (rings)
         * @param uvLocked Whether to lock UV coordinates (currently unused, for future implementation)
         */
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

        /**
         * Registers a log block from a BlockDefinition.
         * Automatically extracts textures from the definition and registers the log block.
         *
         * <p>Texture order in definition:
         * <ul>
         *   <li>2 textures: [side, end] - preferred format</li>
         *   <li>3 textures: [end, end, side] - legacy compatibility</li>
         * </ul>
         *
         * @param generator The BlockStateModelGenerator to register models with
         * @param block The log block to generate models for
         * @param definition The block definition containing texture information
         * @throws IllegalArgumentException if definition doesn't have required textures
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

                registerLogBlock(generator, block, sideTexture, endTexture, false);
        }

        // ========================================
        // Helper Methods (block-models.md 5.2-5.4)
        // ========================================

        /**
         * Creates a TextureMap for log blocks.
         * Follows block-models.md section 5.3: Using Texture Map.
         *
         * @param sideTexture Texture path for sides
         * @param endTexture Texture path for ends
         * @return Configured TextureMap with SIDE, END, and PARTICLE keys
         */
        private static TextureMap createLogTextureMap(String sideTexture, String endTexture) {
                return new TextureMap()
                        .put(TextureKey.SIDE, createBlockIdentifier(sideTexture))
                        .put(TextureKey.END, createBlockIdentifier(endTexture))
                        .put(TextureKey.PARTICLE, createBlockIdentifier(sideTexture));
        }

        /**
         * Creates blockstate variants for log blocks with axis rotation.
         * Follows block-models.md section 5.4: Custom BlockStateSupplier Method.
         *
         * @param verticalModelId Model ID for Y-axis (vertical) orientation
         * @param horizontalModelId Model ID for X/Z-axis (horizontal) orientations
         * @return Configured BlockStateVariantMap for all three axes
         */
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
