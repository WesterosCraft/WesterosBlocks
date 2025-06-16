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

public class ArrowSlitBlockExport {
    private static final String PARTICLE_KEY = "particle";
    private static final String TEXTURE_KEY = "1";

    public static void generateBlockStateModels(BlockStateModelGenerator generator, Block block, String texturePath) {
        // Create the base models for each type
        Identifier singleModelId = createModel(generator, block, ArrowSlitType.SINGLE, texturePath);
        Identifier midModelId = createModel(generator, block, ArrowSlitType.MID, texturePath);
        Identifier topModelId = createModel(generator, block, ArrowSlitType.TOP, texturePath);
        Identifier bottomModelId = createModel(generator, block, ArrowSlitType.BOTTOM, texturePath);

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

    private static Identifier createModel(BlockStateModelGenerator generator, Block block, ArrowSlitType type, String texturePath) {
        JsonObject modelJson = new JsonObject();
        modelJson.addProperty("parent", "minecraft:block/block");
        modelJson.addProperty("credit", "Generated by WesterosBlocks");
        modelJson.addProperty("texture_size", 32);

        // Add textures
        JsonObject textures = new JsonObject();
        textures.addProperty(TEXTURE_KEY, texturePath);
        textures.addProperty(PARTICLE_KEY, texturePath);
        modelJson.add("textures", textures);

        // Add elements based on type
        JsonArray elements = new JsonArray();
        
        // Common elements for all types
        JsonObject rightWall = createRightWall();
        elements.add(rightWall);
        
        JsonObject leftWall = createLeftWall();
        elements.add(leftWall);

        // Type-specific elements
        switch (type) {
            case SINGLE -> {
                elements.add(createBottomLedge());
                elements.add(createTopLedge());
            }
            case TOP -> elements.add(createTopLedge());
            case BOTTOM -> elements.add(createBottomLedge());
            case MID -> {} // No additional elements needed
        }

        modelJson.add("elements", elements);

        // Create a unique model ID for this block and type
        String modelPath = "block/generated/" + block.getTranslationKey().replace("block.westerosblocks.", "") + "_" + type.asString();
        Identifier modelId = WesterosBlocks.id(modelPath);

        // Register the model
        generator.modelCollector.accept(modelId, () -> modelJson);

        return modelId;
    }

    private static JsonObject createRightWall() {
        JsonObject element = new JsonObject();
        element.add("from", createArray(10, 0, 0));
        element.add("to", createArray(16, 16, 16));
        
        JsonObject rotation = new JsonObject();
        rotation.addProperty("angle", 0);
        rotation.addProperty("axis", "y");
        rotation.add("origin", createArray(10, 0, 0));
        element.add("rotation", rotation);

        JsonObject faces = new JsonObject();
        faces.add("north", createFace(0, 0, 6, 16, true));
        faces.add("east", createFace(0, 0, 16, 16, true));
        faces.add("south", createFace(10, 0, 16, 16, true));
        faces.add("west", createFace(0, 0, 16, 16, false));
        faces.add("up", createFace(10, 0, 16, 16, true));
        faces.add("down", createFace(10, 0, 16, 16, true));
        element.add("faces", faces);

        return element;
    }

    private static JsonObject createLeftWall() {
        JsonObject element = new JsonObject();
        element.add("from", createArray(0, 0, 0));
        element.add("to", createArray(6, 16, 16));

        JsonObject faces = new JsonObject();
        faces.add("north", createFace(10, 0, 16, 16, true));
        faces.add("east", createFace(0, 0, 16, 16, false));
        faces.add("south", createFace(0, 0, 6, 16, true));
        faces.add("west", createFace(0, 0, 16, 16, true));
        faces.add("up", createFace(0, 0, 6, 16, true));
        faces.add("down", createFace(0, 0, 6, 16, true));
        element.add("faces", faces);

        return element;
    }

    private static JsonObject createBottomLedge() {
        JsonObject element = new JsonObject();
        element.add("from", createArray(6, 0, 0));
        element.add("to", createArray(10, 3, 16));

        JsonObject rotation = new JsonObject();
        rotation.addProperty("angle", 0);
        rotation.addProperty("axis", "y");
        rotation.add("origin", createArray(7, 0, 0));
        element.add("rotation", rotation);

        JsonObject faces = new JsonObject();
        faces.add("north", createFace(6, 13, 10, 16, true));
        faces.add("south", createFace(6, 13, 10, 16, true));
        faces.add("up", createFace(6, 0, 10, 16, false));
        faces.add("down", createFace(6, 0, 10, 16, true));
        element.add("faces", faces);

        return element;
    }

    private static JsonObject createTopLedge() {
        JsonObject element = new JsonObject();
        element.add("from", createArray(6, 13, 0));
        element.add("to", createArray(10, 16, 16));

        JsonObject rotation = new JsonObject();
        rotation.addProperty("angle", 0);
        rotation.addProperty("axis", "y");
        rotation.add("origin", createArray(7, 15, 0));
        element.add("rotation", rotation);

        JsonObject faces = new JsonObject();
        faces.add("north", createFace(6, 0, 10, 3, true));
        faces.add("south", createFace(6, 0, 10, 3, true));
        faces.add("up", createFace(6, 0, 10, 16, true));
        faces.add("down", createFace(6, 0, 10, 16, false));
        element.add("faces", faces);

        return element;
    }

    private static JsonObject createFace(int u1, int v1, int u2, int v2, boolean cullface) {
        JsonObject face = new JsonObject();
        face.add("uv", createArray(u1, v1, u2, v2));
        face.addProperty("texture", "#" + TEXTURE_KEY);
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
        String modelPath = "block/generated/" + block.getTranslationKey().replace("block.westerosblocks.", "");
        Model model = new Model(
            Optional.of(WesterosBlocks.id(modelPath + "_single")),
            Optional.empty()
        );
        generator.register(block.asItem(), model);
    }
} 