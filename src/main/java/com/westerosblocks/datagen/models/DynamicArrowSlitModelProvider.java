package com.westerosblocks.datagen.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.custom.WCArrowSlitBlock;
import com.westerosblocks.block.custom.WCArrowSlitBlock.ArrowSlitType;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.Optional;

public class DynamicArrowSlitModelProvider {
    private static final String PARTICLE_KEY = "particle";

    public static class TextureConfig {
        private final String textureKey;
        private final String texturePath;
        private final String particlePath;

        public TextureConfig(String textureKey, String texturePath) {
            this(textureKey, texturePath, texturePath);
        }

        public TextureConfig(String textureKey, String texturePath, String particlePath) {
            this.textureKey = textureKey;
            this.texturePath = texturePath;
            this.particlePath = particlePath;
        }

        public String getTextureKey() { return textureKey; }
        public String getTexturePath() { return texturePath; }
        public String getParticlePath() { return particlePath; }
    }

    public static void generateBlockStateModels(BlockStateModelGenerator generator, Block block, String texturePath) {
        generateBlockStateModels(generator, block, new TextureConfig("side", texturePath));
    }

    public static void generateBlockStateModels(BlockStateModelGenerator generator, Block block, TextureConfig textureConfig) {
        // Create the base models for each type
        Identifier singleModelId = createModel(generator, block, ArrowSlitType.SINGLE, textureConfig);
        Identifier midModelId = createModel(generator, block, ArrowSlitType.MID, textureConfig);
        Identifier topModelId = createModel(generator, block, ArrowSlitType.TOP, textureConfig);
        Identifier bottomModelId = createModel(generator, block, ArrowSlitType.BOTTOM, textureConfig);

        // Create variants for each state and direction
        BlockStateVariantMap variants = BlockStateVariantMap.create(WCArrowSlitBlock.FACING, WCArrowSlitBlock.TYPE)
            // Single state
            .register(Direction.NORTH, ArrowSlitType.SINGLE, BlockStateVariant.create()
                .put(VariantSettings.MODEL, singleModelId))
            .register(Direction.EAST, ArrowSlitType.SINGLE, BlockStateVariant.create()
                .put(VariantSettings.MODEL, singleModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R90))
            .register(Direction.SOUTH, ArrowSlitType.SINGLE, BlockStateVariant.create()
                .put(VariantSettings.MODEL, singleModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R180))
            .register(Direction.WEST, ArrowSlitType.SINGLE, BlockStateVariant.create()
                .put(VariantSettings.MODEL, singleModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R270))

            // Mid state
            .register(Direction.NORTH, ArrowSlitType.MID, BlockStateVariant.create()
                .put(VariantSettings.MODEL, midModelId))
            .register(Direction.EAST, ArrowSlitType.MID, BlockStateVariant.create()
                .put(VariantSettings.MODEL, midModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R90))
            .register(Direction.SOUTH, ArrowSlitType.MID, BlockStateVariant.create()
                .put(VariantSettings.MODEL, midModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R180))
            .register(Direction.WEST, ArrowSlitType.MID, BlockStateVariant.create()
                .put(VariantSettings.MODEL, midModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R270))

            // Bottom state
            .register(Direction.NORTH, ArrowSlitType.BOTTOM, BlockStateVariant.create()
                .put(VariantSettings.MODEL, bottomModelId))
            .register(Direction.EAST, ArrowSlitType.BOTTOM, BlockStateVariant.create()
                .put(VariantSettings.MODEL, bottomModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R90))
            .register(Direction.SOUTH, ArrowSlitType.BOTTOM, BlockStateVariant.create()
                .put(VariantSettings.MODEL, bottomModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R180))
            .register(Direction.WEST, ArrowSlitType.BOTTOM, BlockStateVariant.create()
                .put(VariantSettings.MODEL, bottomModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R270))

            // Top state
            .register(Direction.NORTH, ArrowSlitType.TOP, BlockStateVariant.create()
                .put(VariantSettings.MODEL, topModelId))
            .register(Direction.EAST, ArrowSlitType.TOP, BlockStateVariant.create()
                .put(VariantSettings.MODEL, topModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R90))
            .register(Direction.SOUTH, ArrowSlitType.TOP, BlockStateVariant.create()
                .put(VariantSettings.MODEL, topModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R180))
            .register(Direction.WEST, ArrowSlitType.TOP, BlockStateVariant.create()
                .put(VariantSettings.MODEL, topModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R270));

        // Register the block state
        generator.blockStateCollector.accept(
            VariantsBlockStateSupplier.create(block)
                .coordinate(variants)
        );
    }

    private static Identifier createModel(BlockStateModelGenerator generator, Block block, ArrowSlitType type, TextureConfig textureConfig) {
        JsonObject modelJson = new JsonObject();
        modelJson.addProperty("parent", "minecraft:block/block");
        modelJson.addProperty("credit", "Generated by WesterosBlocks");
        modelJson.addProperty("texture_size", 32);

        // Add textures
        JsonObject textures = new JsonObject();
        textures.addProperty(textureConfig.getTextureKey(), textureConfig.getTexturePath());
        textures.addProperty(PARTICLE_KEY, textureConfig.getParticlePath());
        modelJson.add("textures", textures);

        // Add elements based on type
        JsonArray elements = new JsonArray();
        
        // Common elements for all types
        JsonObject rightWall = createRightWall(textureConfig);
        elements.add(rightWall);
        
        JsonObject leftWall = createLeftWall(textureConfig);
        elements.add(leftWall);

        // Type-specific elements
        switch (type) {
            case SINGLE -> {
                elements.add(createBottomLedge(textureConfig));
                elements.add(createTopLedge(textureConfig));
            }
            case TOP -> elements.add(createTopLedge(textureConfig));
            case BOTTOM -> elements.add(createBottomLedge(textureConfig));
            case MID -> {} // No additional elements needed
        }

        modelJson.add("elements", elements);

        // Create a unique model ID for this block and type
        String modelPath = "block/" + block.getTranslationKey().replace("block.westerosblocks.", "") + "_" + type.asString();
        Identifier modelId = WesterosBlocks.id(modelPath);

        // Register the model
        generator.modelCollector.accept(modelId, () -> modelJson);

        return modelId;
    }

    private static JsonObject createRightWall(TextureConfig textureConfig) {
        JsonObject element = new JsonObject();
        element.add("from", createArray(10, 0, 0));
        element.add("to", createArray(16, 16, 16));
        
        JsonObject rotation = new JsonObject();
        rotation.addProperty("angle", 0);
        rotation.addProperty("axis", "y");
        rotation.add("origin", createArray(10, 0, 0));
        element.add("rotation", rotation);

        JsonObject faces = new JsonObject();
        faces.add("north", createFace(0, 0, 6, 16, true, textureConfig));
        faces.add("east", createFace(0, 0, 16, 16, true, textureConfig));
        faces.add("south", createFace(10, 0, 16, 16, true, textureConfig));
        faces.add("west", createFace(0, 0, 16, 16, false, textureConfig));
        faces.add("up", createFace(10, 0, 16, 16, true, textureConfig));
        faces.add("down", createFace(10, 0, 16, 16, true, textureConfig));
        element.add("faces", faces);

        return element;
    }

    private static JsonObject createLeftWall(TextureConfig textureConfig) {
        JsonObject element = new JsonObject();
        element.add("from", createArray(0, 0, 0));
        element.add("to", createArray(6, 16, 16));

        JsonObject faces = new JsonObject();
        faces.add("north", createFace(10, 0, 16, 16, true, textureConfig));
        faces.add("east", createFace(0, 0, 16, 16, false, textureConfig));
        faces.add("south", createFace(0, 0, 6, 16, true, textureConfig));
        faces.add("west", createFace(0, 0, 16, 16, true, textureConfig));
        faces.add("up", createFace(0, 0, 6, 16, true, textureConfig));
        faces.add("down", createFace(0, 0, 6, 16, true, textureConfig));
        element.add("faces", faces);

        return element;
    }

    private static JsonObject createBottomLedge(TextureConfig textureConfig) {
        JsonObject element = new JsonObject();
        element.add("from", createArray(6, 0, 0));
        element.add("to", createArray(10, 1, 16));

        JsonObject rotation = new JsonObject();
        rotation.addProperty("angle", 0);
        rotation.addProperty("axis", "y");
        rotation.add("origin", createArray(7, 0, 0));
        element.add("rotation", rotation);

        JsonObject faces = new JsonObject();
        faces.add("north", createFace(6, 15, 10, 16, true, textureConfig));
        faces.add("south", createFace(6, 15, 10, 16, true, textureConfig));
        faces.add("up", createFace(6, 0, 10, 16, false, textureConfig));
        faces.add("down", createFace(6, 0, 10, 16, true, textureConfig));
        element.add("faces", faces);

        return element;
    }

    private static JsonObject createTopLedge(TextureConfig textureConfig) {
        JsonObject element = new JsonObject();
        element.add("from", createArray(6, 15, 0));
        element.add("to", createArray(10, 16, 16));

        JsonObject rotation = new JsonObject();
        rotation.addProperty("angle", 0);
        rotation.addProperty("axis", "y");
        rotation.add("origin", createArray(7, 15, 0));
        element.add("rotation", rotation);

        JsonObject faces = new JsonObject();
        faces.add("north", createFace(6, 0, 10, 1, true, textureConfig));
        faces.add("south", createFace(6, 0, 10, 1, true, textureConfig));
        faces.add("up", createFace(6, 0, 10, 16, true, textureConfig));
        faces.add("down", createFace(6, 0, 10, 16, false, textureConfig));
        element.add("faces", faces);

        return element;
    }

    private static JsonObject createFace(int u1, int v1, int u2, int v2, boolean cullface, TextureConfig textureConfig) {
        JsonObject face = new JsonObject();
        face.add("uv", createArray(u1, v1, u2, v2));
        face.addProperty("texture", "#" + textureConfig.getTextureKey());
        if (cullface) {
            face.addProperty("cullface", "north"); // The actual cullface will be set per-face
        }
        return face;
    }

    private static JsonArray createArray(int... values) {
        JsonArray array = new JsonArray();
        for (int value : values) {
            array.add(value);
        }
        return array;
    }

    public static void generateItemModels(ItemModelGenerator generator, Block block) {
        // Create a simple item model that inherits from the block model
        String modelPath = "block/" + block.getTranslationKey().replace("block.westerosblocks.", "") + "_single";
        Model model = new Model(
            Optional.of(WesterosBlocks.id(modelPath)),
            Optional.empty()
        );
        generator.register(block.asItem(), model);
    }
} 