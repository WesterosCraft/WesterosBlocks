package com.westerosblocks.datagen;

import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.BlockStateVariant;
import net.minecraft.data.client.Models;
import net.minecraft.data.client.TextureKey;
import net.minecraft.data.client.TextureMap;
import net.minecraft.data.client.VariantSettings;
import net.minecraft.data.client.VariantsBlockStateSupplier;
import net.minecraft.util.Identifier;

public class ModBlockStateModelGenerator {

        public ModBlockStateModelGenerator() {
        }

        /**
         * Registers a custom solid block with a specific texture path
         */
        public static void registerSimpleCustomSolidBlock(BlockStateModelGenerator generator, Block block,
                        String texturePath) {
                TextureMap textureMap = new TextureMap().put(TextureKey.ALL,
                                Identifier.of("westerosblocks", "block/" + texturePath));

                // Get the block name for nested folder structure
                String blockName = getBlockName(block);
                Identifier nestedModelId = Identifier.of("westerosblocks", "block/" + blockName + "/" + blockName);

                // Create the model with nested path
                Identifier modelId = Models.CUBE_ALL.upload(nestedModelId, textureMap, generator.modelCollector);
                generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block,
                                BlockStateVariant.create().put(VariantSettings.MODEL, modelId)));
                generator.registerParentedItemModel(block, modelId);
        }

        /**
         * Registers a custom solid block with multiple textures
         * Texture order: down, up, north, south, east, west
         */
        public static void registerCustomSolidBlock(BlockStateModelGenerator generator, Block block,
                        String... texturePaths) {
                if (texturePaths.length == 0) {
                        throw new IllegalArgumentException("At least one texture path is required");
                }

                // Fill remaining slots with the last texture if less than 6 provided
                String[] filledTextures = new String[6];
                for (int i = 0; i < 6; i++) {
                        if (i < texturePaths.length) {
                                filledTextures[i] = texturePaths[i];
                        } else {
                                filledTextures[i] = texturePaths[texturePaths.length - 1];
                        }
                }

                TextureMap textureMap = new TextureMap()
                                .put(TextureKey.DOWN, Identifier.of("westerosblocks", "block/" + filledTextures[0]))
                                .put(TextureKey.UP, Identifier.of("westerosblocks", "block/" + filledTextures[1]))
                                .put(TextureKey.NORTH, Identifier.of("westerosblocks", "block/" + filledTextures[2]))
                                .put(TextureKey.SOUTH, Identifier.of("westerosblocks", "block/" + filledTextures[3]))
                                .put(TextureKey.EAST, Identifier.of("westerosblocks", "block/" + filledTextures[4]))
                                .put(TextureKey.WEST, Identifier.of("westerosblocks", "block/" + filledTextures[5]))
                                .put(TextureKey.PARTICLE,
                                                Identifier.of("westerosblocks", "block/" + filledTextures[0])); // Use
                                                                                                                // down
                                                                                                                // texture
                                                                                                                // as
                                                                                                                // particle

                // Get the block name for nested folder structure
                String blockName = getBlockName(block);
                Identifier nestedModelId = Identifier.of("westerosblocks", "block/" + blockName + "/" + blockName);

                // Create the model with nested path
                Identifier modelId = Models.CUBE.upload(nestedModelId, textureMap, generator.modelCollector);
                generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block,
                                BlockStateVariant.create().put(VariantSettings.MODEL, modelId)));
                generator.registerParentedItemModel(block, modelId);
        }

        /**
         * Extracts the block name from the block's registry key
         */
        private static String getBlockName(Block block) {
                String blockString = block.toString();
                if (blockString.contains(":")) {
                        return blockString.split(":")[1].replace("}", "");
                }
                return blockString.toLowerCase().replace("block{", "").replace("}", "");
        }
}