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
import com.westerosblocks.block.custom.WCSolidBlock;

import java.io.IOException;
import com.google.gson.stream.JsonWriter;
import net.minecraft.data.client.BlockStateSupplier;
import net.minecraft.block.BlockState;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class SolidBlockExporter {

    /**
     * Generates model name with symmetrical/asymmetrical directory structure
     */
    public static String getModelName(Block block, String ext, int setIdx, Boolean symmetrical) {
        String blockName = getBlockName(block);
        String symmetricalPath = symmetrical ? "symmetrical" : "asymmetrical";
        return blockName + "/" + symmetricalPath + "/" + ext + "_v" + (setIdx + 1);
    }

    /**
     * Creates model file name with symmetrical/asymmetrical path
     */
    public static Identifier modelFileName(Block block, String ext, int setIdx, Boolean symmetrical) {
        return Identifier.of(WesterosBlocks.MOD_ID, "block/" + getModelName(block, ext, setIdx, symmetrical));
    }

    /**
     * Checks if a block is symmetrical
     */
    public static boolean isSymmetrical(Block block) {
        if (block instanceof WCSolidBlock) {
            return ((WCSolidBlock) block).symmetrical;
        }
        return false;
    }

    /**
     * Registers a custom solid block with a specific texture path
     */
    public static void registerSimpleCustomSolidBlock(BlockStateModelGenerator generator, Block block,
            String texturePath) {
        boolean isSymmetrical = isSymmetrical(block);

        if (isSymmetrical) {
            // Generate both symmetrical and asymmetrical variants in a single block state
            generateSymmetricalBlockState(generator, block, texturePath);
        } else {
            // Standard registration for non-symmetrical blocks
            TextureMap textureMap = new TextureMap().put(TextureKey.ALL,
                    Identifier.of(WesterosBlocks.MOD_ID, "block/" + texturePath));

            String blockName = getBlockName(block);
            Identifier nestedModelId = Identifier.of(WesterosBlocks.MOD_ID, "block/" + blockName + "/" + blockName);

            Identifier modelId = Models.CUBE_ALL.upload(nestedModelId, textureMap, generator.modelCollector);
            generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block,
                    BlockStateVariant.create().put(VariantSettings.MODEL, modelId)));
            generator.registerParentedItemModel(block, modelId);
        }
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

        boolean isSymmetrical = isSymmetrical(block);

        if (isSymmetrical) {
            // Generate both symmetrical and asymmetrical variants in a single block state
            generateSymmetricalBlockState(generator, block, texturePaths);
        } else {
            // Standard registration for non-symmetrical blocks
            String[] filledTextures = new String[6];
            for (int i = 0; i < 6; i++) {
                if (i < texturePaths.length) {
                    filledTextures[i] = texturePaths[i];
                } else {
                    filledTextures[i] = texturePaths[texturePaths.length - 1];
                }
            }

            TextureMap textureMap = ModTextureMap.customAllSides(filledTextures);

            String blockName = getBlockName(block);
            Identifier nestedModelId = Identifier.of(WesterosBlocks.MOD_ID, "block/" + blockName + "/" + blockName);

            Identifier modelId = Models.CUBE.upload(nestedModelId, textureMap, generator.modelCollector);
            generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block,
                    BlockStateVariant.create().put(VariantSettings.MODEL, modelId)));
            generator.registerParentedItemModel(block, modelId);
        }
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

        boolean isSymmetrical = isSymmetrical(block);

        if (isSymmetrical) {
            // Generate symmetrical variants in a single block state
            generateSymmetricalBlockStateWithRandomTextures(generator, block, textureArrays);
        } else {
            // Standard registration for non-symmetrical blocks
            String blockName = getBlockName(block);
            List<Identifier> modelIds = new ArrayList<>();

            for (int i = 0; i < textureArrays.length; i++) {
                String[] texturePaths = textureArrays[i];
                if (texturePaths.length == 0) {
                    throw new IllegalArgumentException(
                            "At least one texture path is required in array " + i);
                }

                String[] filledTextures = new String[6];
                for (int j = 0; j < 6; j++) {
                    if (j < texturePaths.length) {
                        filledTextures[j] = texturePaths[j];
                    } else {
                        filledTextures[j] = texturePaths[texturePaths.length - 1];
                    }
                }

                TextureMap textureMap = ModTextureMap.customAllSides(filledTextures);

                Identifier nestedModelId = Identifier.of(WesterosBlocks.MOD_ID,
                        "block/" + blockName + "/base_v" + (i + 1));
                Identifier modelId = Models.CUBE.upload(nestedModelId, textureMap, generator.modelCollector);
                modelIds.add(modelId);
            }

            List<BlockStateVariant> variants = modelIds.stream()
                    .map(modelId -> BlockStateVariant.create().put(VariantSettings.MODEL, modelId))
                    .toList();

            generator.blockStateCollector.accept(
                    VariantsBlockStateSupplier.create(block, variants.toArray(new BlockStateVariant[0])));

            if (!modelIds.isEmpty()) {
                generator.registerParentedItemModel(block, modelIds.get(0));
            }
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

        boolean isSymmetrical = isSymmetrical(block);

        if (isSymmetrical) {
            // Generate symmetrical variants in a single block state
            generateSymmetricalBlockStateWithStates(generator, block, textureArrays);
        } else {
            // Standard registration for non-symmetrical blocks
            String blockName = getBlockName(block);
            List<Identifier> modelIds = new ArrayList<>();

            for (int i = 0; i < textureArrays.length; i++) {
                String[] texturePaths = textureArrays[i];
                if (texturePaths.length == 0) {
                    throw new IllegalArgumentException(
                            "At least one texture path is required in array " + i);
                }

                String[] filledTextures = new String[6];
                for (int j = 0; j < 6; j++) {
                    if (j < texturePaths.length) {
                        filledTextures[j] = texturePaths[j];
                    } else {
                        filledTextures[j] = texturePaths[texturePaths.length - 1];
                    }
                }

                TextureMap textureMap = ModTextureMap.customAllSides(filledTextures);

                Identifier nestedModelId = Identifier.of(WesterosBlocks.MOD_ID,
                        "block/" + blockName + "/state" + i + "_v" + (i + 1));

                Identifier modelId = Models.CUBE.upload(nestedModelId, textureMap, generator.modelCollector);
                modelIds.add(modelId);
            }

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

            if (!modelIds.isEmpty()) {
                generator.registerParentedItemModel(block, modelIds.get(0));
            }
        }
    }

    /**
     * Generates symmetrical block state for simple texture blocks
     */
    private static void generateSymmetricalBlockState(BlockStateModelGenerator generator, Block block,
            String texturePath) {
        // Generate both symmetrical and asymmetrical models
        TextureMap textureMap = new TextureMap().put(TextureKey.ALL,
                Identifier.of(WesterosBlocks.MOD_ID, "block/" + texturePath));

        Identifier symmetricalModelId = Models.CUBE_ALL.upload(
                modelFileName(block, "base", 0, true),
                textureMap,
                generator.modelCollector);

        Identifier asymmetricalModelId = Models.CUBE_ALL.upload(
                modelFileName(block, "base", 0, false),
                textureMap,
                generator.modelCollector);

        // Create single block state with both symmetrical variants
        generator.blockStateCollector.accept(new BlockStateSupplier() {
            @Override
            public Block getBlock() {
                return block;
            }

            @Override
            public JsonElement get() {
                JsonObject json = new JsonObject();
                JsonObject variants = new JsonObject();

                JsonObject symmetricalVariant = new JsonObject();
                symmetricalVariant.addProperty("model", symmetricalModelId.toString());
                variants.add("symmetrical=true", symmetricalVariant);

                JsonObject asymmetricalVariant = new JsonObject();
                asymmetricalVariant.addProperty("model", asymmetricalModelId.toString());
                variants.add("symmetrical=false", asymmetricalVariant);

                json.add("variants", variants);
                return json;
            }
        });

        // Register item model using the symmetrical variant
        generator.registerParentedItemModel(block, symmetricalModelId);
    }

    /**
     * Generates symmetrical block state for multi-texture blocks
     */
    private static void generateSymmetricalBlockState(BlockStateModelGenerator generator, Block block,
            String[] texturePaths) {
        String[] filledTextures = new String[6];
        for (int i = 0; i < 6; i++) {
            if (i < texturePaths.length) {
                filledTextures[i] = texturePaths[i];
            } else {
                filledTextures[i] = texturePaths[texturePaths.length - 1];
            }
        }

        TextureMap textureMap = ModTextureMap.customAllSides(filledTextures);

        // Generate both symmetrical and asymmetrical models
        Identifier symmetricalModelId = Models.CUBE.upload(
                modelFileName(block, "base", 0, true),
                textureMap,
                generator.modelCollector);

        Identifier asymmetricalModelId = Models.CUBE.upload(
                modelFileName(block, "base", 0, false),
                textureMap,
                generator.modelCollector);

        // Create single block state with both symmetrical variants
        generator.blockStateCollector.accept(new BlockStateSupplier() {
            @Override
            public Block getBlock() {
                return block;
            }

            @Override
            public JsonElement get() {
                JsonObject json = new JsonObject();
                JsonObject variants = new JsonObject();

                JsonObject symmetricalVariant = new JsonObject();
                symmetricalVariant.addProperty("model", symmetricalModelId.toString());
                variants.add("symmetrical=true", symmetricalVariant);

                JsonObject asymmetricalVariant = new JsonObject();
                asymmetricalVariant.addProperty("model", asymmetricalModelId.toString());
                variants.add("symmetrical=false", asymmetricalVariant);

                json.add("variants", variants);
                return json;
            }
        });

        // Register item model using the symmetrical variant
        generator.registerParentedItemModel(block, symmetricalModelId);
    }

    /**
     * Generates symmetrical block state with random textures
     */
    private static void generateSymmetricalBlockStateWithRandomTextures(BlockStateModelGenerator generator, Block block,
            String[][] textureArrays) {
        List<Identifier> symmetricalModelIds = new ArrayList<>();
        List<Identifier> asymmetricalModelIds = new ArrayList<>();

        for (int i = 0; i < textureArrays.length; i++) {
            String[] texturePaths = textureArrays[i];
            if (texturePaths.length == 0) {
                throw new IllegalArgumentException(
                        "At least one texture path is required in array " + i);
            }

            String[] filledTextures = new String[6];
            for (int j = 0; j < 6; j++) {
                if (j < texturePaths.length) {
                    filledTextures[j] = texturePaths[j];
                } else {
                    filledTextures[j] = texturePaths[texturePaths.length - 1];
                }
            }

            TextureMap textureMap = ModTextureMap.customAllSides(filledTextures);

            // Generate both symmetrical and asymmetrical models for each texture array
            Identifier symmetricalModelId = Models.CUBE.upload(
                    modelFileName(block, "base", i, true),
                    textureMap,
                    generator.modelCollector);
            symmetricalModelIds.add(symmetricalModelId);

            Identifier asymmetricalModelId = Models.CUBE.upload(
                    modelFileName(block, "base", i, false),
                    textureMap,
                    generator.modelCollector);
            asymmetricalModelIds.add(asymmetricalModelId);
        }

        // Create single block state with both symmetrical variants
        generator.blockStateCollector.accept(new BlockStateSupplier() {
            @Override
            public Block getBlock() {
                return block;
            }

            @Override
            public JsonElement get() {
                JsonObject json = new JsonObject();
                JsonObject variants = new JsonObject();

                // Add symmetrical variants
                for (int i = 0; i < symmetricalModelIds.size(); i++) {
                    JsonObject variant = new JsonObject();
                    variant.addProperty("model", symmetricalModelIds.get(i).toString());
                    variants.add("symmetrical=true", variant);
                }

                // Add asymmetrical variants
                for (int i = 0; i < asymmetricalModelIds.size(); i++) {
                    JsonObject variant = new JsonObject();
                    variant.addProperty("model", asymmetricalModelIds.get(i).toString());
                    variants.add("symmetrical=false", variant);
                }

                json.add("variants", variants);
                return json;
            }
        });

        // Register item model using the first symmetrical variant
        if (!symmetricalModelIds.isEmpty()) {
            generator.registerParentedItemModel(block, symmetricalModelIds.get(0));
        }
    }

    /**
     * Generates symmetrical block state with states
     */
    private static void generateSymmetricalBlockStateWithStates(BlockStateModelGenerator generator, Block block,
            String[][] textureArrays) {
        List<Identifier> symmetricalModelIds = new ArrayList<>();
        List<Identifier> asymmetricalModelIds = new ArrayList<>();

        for (int i = 0; i < textureArrays.length; i++) {
            String[] texturePaths = textureArrays[i];
            if (texturePaths.length == 0) {
                throw new IllegalArgumentException(
                        "At least one texture path is required in array " + i);
            }

            String[] filledTextures = new String[6];
            for (int j = 0; j < 6; j++) {
                if (j < texturePaths.length) {
                    filledTextures[j] = texturePaths[j];
                } else {
                    filledTextures[j] = texturePaths[texturePaths.length - 1];
                }
            }

            TextureMap textureMap = ModTextureMap.customAllSides(filledTextures);

            // Generate both symmetrical and asymmetrical models for each state
            Identifier symmetricalModelId = Models.CUBE.upload(
                    modelFileName(block, "state" + i, i, true),
                    textureMap,
                    generator.modelCollector);
            symmetricalModelIds.add(symmetricalModelId);

            Identifier asymmetricalModelId = Models.CUBE.upload(
                    modelFileName(block, "state" + i, i, false),
                    textureMap,
                    generator.modelCollector);
            asymmetricalModelIds.add(asymmetricalModelId);
        }

        // Create single block state with both symmetrical variants and states
        generator.blockStateCollector.accept(new BlockStateSupplier() {
            @Override
            public Block getBlock() {
                return block;
            }

            @Override
            public JsonElement get() {
                JsonObject json = new JsonObject();
                JsonObject variants = new JsonObject();

                // Add symmetrical variants with states
                for (int i = 0; i < symmetricalModelIds.size(); i++) {
                    JsonObject variant = new JsonObject();
                    variant.addProperty("model", symmetricalModelIds.get(i).toString());
                    variants.add("state=state" + i + ",symmetrical=true", variant);
                }

                // Add asymmetrical variants with states
                for (int i = 0; i < asymmetricalModelIds.size(); i++) {
                    JsonObject variant = new JsonObject();
                    variant.addProperty("model", asymmetricalModelIds.get(i).toString());
                    variants.add("state=state" + i + ",symmetrical=false", variant);
                }

                json.add("variants", variants);
                return json;
            }
        });

        // Register item model using the first symmetrical variant
        if (!symmetricalModelIds.isEmpty()) {
            generator.registerParentedItemModel(block, symmetricalModelIds.get(0));
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
