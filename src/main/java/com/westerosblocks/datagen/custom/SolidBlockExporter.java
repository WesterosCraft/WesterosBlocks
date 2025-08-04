package com.westerosblocks.datagen.custom;

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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.datagen.ModTextureMap;
import com.westerosblocks.utils.ModProperties;

import java.io.IOException;
import com.google.gson.stream.JsonWriter;
import net.minecraft.data.client.BlockStateSupplier;
import net.minecraft.block.BlockState;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class SolidBlockExporter {

    /**
     * Registers a custom solid block with a specific texture path
     */
    public static void registerSimpleCustomSolidBlock(BlockStateModelGenerator generator, Block block,
            String texturePath) {
        TextureMap textureMap = new TextureMap().put(TextureKey.ALL,
                Identifier.of(WesterosBlocks.MOD_ID, "block/" + texturePath));

        // Get the block name for nested folder structure
        String blockName = getBlockName(block);
        Identifier nestedModelId = Identifier.of(WesterosBlocks.MOD_ID, "block/" + blockName + "/" + blockName);

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

        TextureMap textureMap = ModTextureMap.customAllSides(filledTextures);

        // Get the block name for nested folder structure
        String blockName = getBlockName(block);
        Identifier nestedModelId = Identifier.of(WesterosBlocks.MOD_ID, "block/" + blockName + "/" + blockName);

        // Create the model with nested path
        Identifier modelId = Models.CUBE.upload(nestedModelId, textureMap, generator.modelCollector);
        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block,
                BlockStateVariant.create().put(VariantSettings.MODEL, modelId)));
        generator.registerParentedItemModel(block, modelId);
    }

    /**
     * Registers a custom solid block with random texture variants
     * Each inner array should contain texture paths in order: down, up, north,
     * south, east, west
     * Generates multiple model variants for random selection
     */
    public static void registerCustomSolidBlockWithRandomTextures(BlockStateModelGenerator generator, Block block,
            String[][] textureArrays) {
        if (textureArrays.length == 0) {
            throw new IllegalArgumentException("At least one texture array is required");
        }

        String blockName = getBlockName(block);
        List<Identifier> modelIds = new ArrayList<>();

        // Generate a model for each texture array
        for (int i = 0; i < textureArrays.length; i++) {
            String[] texturePaths = textureArrays[i];
            if (texturePaths.length == 0) {
                throw new IllegalArgumentException(
                        "At least one texture path is required in array " + i);
            }

            // Fill remaining slots with the last texture if less than 6 provided
            String[] filledTextures = new String[6];
            for (int j = 0; j < 6; j++) {
                if (j < texturePaths.length) {
                    filledTextures[j] = texturePaths[j];
                } else {
                    filledTextures[j] = texturePaths[texturePaths.length - 1];
                }
            }

            TextureMap textureMap = ModTextureMap.customAllSides(filledTextures);

            // Create model with version suffix
            Identifier nestedModelId = Identifier.of(WesterosBlocks.MOD_ID,
                    "block/" + blockName + "/base_v" + (i + 1));
            Identifier modelId = Models.CUBE.upload(nestedModelId, textureMap, generator.modelCollector);
            modelIds.add(modelId);
        }

        // Create blockstate with multiple variants
        List<BlockStateVariant> variants = modelIds.stream()
                .map(modelId -> BlockStateVariant.create().put(VariantSettings.MODEL, modelId))
                .toList();

        generator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(block, variants.toArray(new BlockStateVariant[0])));

        // Register item model using the first variant
        if (!modelIds.isEmpty()) {
            generator.registerParentedItemModel(block, modelIds.get(0));
        }
    }

    /**
     * Registers a custom solid block with multiple states
     * Each inner array should contain texture paths in order: down, up, north,
     * south, east, west
     * Generates multiple model variants for different states
     */
    public static void registerCustomSolidBlockWithStates(BlockStateModelGenerator generator, Block block,
            String[][] textureArrays) {
        if (textureArrays.length == 0) {
            throw new IllegalArgumentException("At least one texture array is required");
        }

        String blockName = getBlockName(block);
        List<Identifier> modelIds = new ArrayList<>();

        // Generate a model for each texture array
        for (int i = 0; i < textureArrays.length; i++) {
            String[] texturePaths = textureArrays[i];
            if (texturePaths.length == 0) {
                throw new IllegalArgumentException(
                        "At least one texture path is required in array " + i);
            }

            // Fill remaining slots with the last texture if less than 6 provided
            String[] filledTextures = new String[6];
            for (int j = 0; j < 6; j++) {
                if (j < texturePaths.length) {
                    filledTextures[j] = texturePaths[j];
                } else {
                    filledTextures[j] = texturePaths[texturePaths.length - 1];
                }
            }

            TextureMap textureMap = ModTextureMap.customAllSides(filledTextures);

            // Create model with state suffix and proper path structure
            Identifier nestedModelId = Identifier.of(WesterosBlocks.MOD_ID,
                    "block/" + blockName + "/state" + i + "_v" + (i + 1));

            Identifier modelId = Models.CUBE.upload(nestedModelId, textureMap, generator.modelCollector);
            modelIds.add(modelId);
        }

        // Create a custom blockstate supplier that generates the proper JSON structure
        generator.blockStateCollector.accept(new BlockStateSupplier() {
            @Override
            public Block getBlock() {
                return block;
            }

            @Override
            public JsonElement get() {
                JsonObject json = new JsonObject();
                JsonObject variants = new JsonObject();

                for (int i = 0; i < modelIds.size(); i++) {
                    JsonObject variant = new JsonObject();
                    variant.addProperty("model", modelIds.get(i).toString());
                    variants.add("state=state" + i, variant);
                }

                json.add("variants", variants);
                return json;
            }
        });

        // Register item model using the first variant
        if (!modelIds.isEmpty()) {
            generator.registerParentedItemModel(block, modelIds.get(0));
        }
    }

    /**
     * Extracts the block name from the block's registry key
     */
    public static String getBlockName(Block block) {
        String blockString = block.toString();
        if (blockString.contains(":")) {
            return blockString.split(":")[1].replace("}", "");
        }
        return blockString.toLowerCase().replace("block{", "").replace("}", "");
    }
}
