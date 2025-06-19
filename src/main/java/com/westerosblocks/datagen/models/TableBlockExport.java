package com.westerosblocks.datagen.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.custom.WCTableBlock;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.Optional;

public class TableBlockExport {
    private static final String PARTICLE_KEY = "particle";
    private static final String TEXTURE_KEY = "1";

    public enum TableType {
        SINGLE("single"),
        DOUBLE("double"),
        CENTER("center"),
        CORNER("corner");

        private final String name;

        TableType(String name) {
            this.name = name;
        }

        public String asString() {
            return this.name;
        }
    }

    public static void generateBlockStateModels(BlockStateModelGenerator generator, Block block, String texturePath) {
        Identifier singleModelId = createModel(generator, block, TableType.SINGLE, texturePath);
        Identifier doubleModelId = createModel(generator, block, TableType.DOUBLE, texturePath);
        Identifier centerModelId = createModel(generator, block, TableType.CENTER, texturePath);
        Identifier cornerModelId = createModel(generator, block, TableType.CORNER, texturePath);

        BlockStateVariantMap variants = BlockStateVariantMap.create(WCTableBlock.NORTH, WCTableBlock.EAST, WCTableBlock.SOUTH, WCTableBlock.WEST, WCTableBlock.WATERLOGGED)
            .register(false, false, false, false, false, BlockStateVariant.create()
                .put(VariantSettings.MODEL, singleModelId))
            .register(false, false, false, false, true, BlockStateVariant.create()
                .put(VariantSettings.MODEL, singleModelId))
            
            .register(false, false, false, true, false, BlockStateVariant.create()
                .put(VariantSettings.MODEL, doubleModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R90))
            .register(false, false, false, true, true, BlockStateVariant.create()
                .put(VariantSettings.MODEL, doubleModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R90))
            .register(false, false, true, false, false, BlockStateVariant.create()
                .put(VariantSettings.MODEL, doubleModelId))
            .register(false, false, true, false, true, BlockStateVariant.create()
                .put(VariantSettings.MODEL, doubleModelId))
            .register(false, true, false, false, false, BlockStateVariant.create()
                .put(VariantSettings.MODEL, doubleModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R270))
            .register(false, true, false, false, true, BlockStateVariant.create()
                .put(VariantSettings.MODEL, doubleModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R270))
            .register(true, false, false, false, false, BlockStateVariant.create()
                .put(VariantSettings.MODEL, doubleModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R180))
            .register(true, false, false, false, true, BlockStateVariant.create()
                .put(VariantSettings.MODEL, doubleModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R180))

            // Corner variants - 2 adjacent sides connected
            // North-West corner (north=true, west=true, east=false, south=false)
            .register(false, false, true, true, false, BlockStateVariant.create()
                .put(VariantSettings.MODEL, cornerModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R90))
            .register(false, false, true, true, true, BlockStateVariant.create()
                .put(VariantSettings.MODEL, cornerModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R90))
            
            // North-East corner (north=true, east=true, west=false, south=false)
            .register(true, true, false, false, false, BlockStateVariant.create()
                .put(VariantSettings.MODEL, cornerModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R270))
            .register(true, true, false, false, true, BlockStateVariant.create()
                .put(VariantSettings.MODEL, cornerModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R270))
            
            // South-West corner (south=true, west=true, north=false, east=false)
            .register(false, true, true, false, false, BlockStateVariant.create()
                .put(VariantSettings.MODEL, cornerModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R0))
            .register(false, true, true, false, true, BlockStateVariant.create()
                .put(VariantSettings.MODEL, cornerModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R0))
            
            // South-East corner (south=true, east=true, north=false, west=false)
            .register(true, false, false, true, false, BlockStateVariant.create()
                .put(VariantSettings.MODEL, cornerModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R180))
            .register(true, false, false, true, true, BlockStateVariant.create()
                .put(VariantSettings.MODEL, cornerModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R180))
            
            // North-South center (north=true, south=true, east=false, west=false)
            .register(true, false, true, false, false, BlockStateVariant.create()
                .put(VariantSettings.MODEL, centerModelId))
            .register(true, false, true, false, true, BlockStateVariant.create()
                .put(VariantSettings.MODEL, centerModelId))
            
            // East-West center (east=true, west=true, north=false, south=false)
            .register(false, true, false, true, false, BlockStateVariant.create()
                .put(VariantSettings.MODEL, centerModelId))
            .register(false, true, false, true, true, BlockStateVariant.create()
                .put(VariantSettings.MODEL, centerModelId))

            .register(false, true, true, true, false, BlockStateVariant.create()
                .put(VariantSettings.MODEL, centerModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R90))
            .register(false, true, true, true, true, BlockStateVariant.create()
                .put(VariantSettings.MODEL, centerModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R90))
            .register(true, false, true, true, false, BlockStateVariant.create()
                .put(VariantSettings.MODEL, centerModelId))
            .register(true, false, true, true, true, BlockStateVariant.create()
                .put(VariantSettings.MODEL, centerModelId))
            .register(true, true, false, true, false, BlockStateVariant.create()
                .put(VariantSettings.MODEL, centerModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R270))
            .register(true, true, false, true, true, BlockStateVariant.create()
                .put(VariantSettings.MODEL, centerModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R270))
            .register(true, true, true, false, false, BlockStateVariant.create()
                .put(VariantSettings.MODEL, centerModelId))
            .register(true, true, true, false, true, BlockStateVariant.create()
                .put(VariantSettings.MODEL, centerModelId))
            .register(true, true, true, true, false, BlockStateVariant.create()
                .put(VariantSettings.MODEL, centerModelId))
            .register(true, true, true, true, true, BlockStateVariant.create()
                .put(VariantSettings.MODEL, centerModelId));

        generator.blockStateCollector.accept(
            VariantsBlockStateSupplier.create(block)
                .coordinate(variants)
        );
    }

    private static Identifier createModel(BlockStateModelGenerator generator, Block block, TableType type, String texturePath) {
        JsonObject modelJson = new JsonObject();
        modelJson.addProperty("parent", "minecraft:block/cube");
        modelJson.addProperty("credit", "Generated by WesterosBlocks");
        modelJson.addProperty("texture_size", 32);

        JsonObject textures = new JsonObject();
        textures.addProperty(TEXTURE_KEY, texturePath);
        textures.addProperty(PARTICLE_KEY, texturePath);
        modelJson.add("textures", textures);

        JsonArray elements = new JsonArray();
        
        switch (type) {
            case SINGLE -> {
                elements.add(createTableTopSingle());
                elements.add(createTableTopInnerSingle());
                elements.add(createTableLegSingle(1, 0, 1, 13, 5, 16, 16));
                elements.add(createTableLegSingle(1, 0, 12, 0, 5, 3, 16));
                elements.add(createTableLegSingle(12, 0, 12, 13, 5, 16, 16));
                elements.add(createTableLegSingle(12, 0, 1, 0, 5, 3, 16));
            }
            case DOUBLE -> {
                elements.add(createTableLegDouble(1, 0, 1, 0, 5, 3, 16));
                elements.add(createTableLegDouble(12, 0, 1, 13, 5, 16, 16));
                elements.add(createTableTopDouble());
                elements.add(createTableTopInnerDouble());
            }
            case CENTER -> {
                elements.add(createTableTopCenter());
                elements.add(createTableTopInnerCenter());
            }
            case CORNER -> {
                elements.add(createTableTopCorner());
                elements.add(createTableTopInnerCorner());
                elements.add(createTableLegCorner());
            }
        }

        modelJson.add("elements", elements);

        String modelPath = "block/generated/" + block.getTranslationKey().replace("block.westerosblocks.", "") + "_" + type.asString();
        Identifier modelId = WesterosBlocks.id(modelPath);

        generator.modelCollector.accept(modelId, () -> modelJson);

        return modelId;
    }

    private static JsonObject createTableTopSingle() {
        JsonObject element = new JsonObject();
        element.addProperty("name", "top");
        element.add("from", createArray(0, 14, 0));
        element.add("to", createArray(16, 16, 16));
        element.add("rotation", createRotation(0, "y", 8, 10.25, 8));

        JsonObject faces = new JsonObject();
        faces.add("north", createFaceWithCullface(0, 0, 16, 2, "#1", "north"));
        faces.add("east", createFaceWithCullface(0, 0, 16, 2, "#1", "east"));
        faces.add("south", createFaceWithCullface(0, 0, 16, 2, "#1", "south"));
        faces.add("west", createFaceWithCullface(0, 0, 16, 2, "#1", "west"));
        faces.add("up", createFaceWithCullface(0, 0, 16, 16, "#1", "up"));
        faces.add("down", createFace(0, 0, 16, 16, "#1"));
        element.add("faces", faces);

        return element;
    }

    private static JsonObject createTableTopInnerSingle() {
        JsonObject element = new JsonObject();
        element.addProperty("name", "top");
        element.add("from", createArray(1, 11, 1));
        element.add("to", createArray(15, 14, 15));
        element.add("rotation", createRotation(0, "y", 8, 10.25, 8));

        JsonObject faces = new JsonObject();
        faces.add("north", createFace(0, 2, 16, 5, "#1"));
        faces.add("east", createFace(0, 2, 16, 5, "#1"));
        faces.add("south", createFace(0, 2, 16, 5, "#1"));
        faces.add("west", createFace(0, 2, 16, 5, "#1"));
        faces.add("down", createFace(1, 1, 15, 15, "#1"));
        element.add("faces", faces);

        return element;
    }

    private static JsonObject createTableLegSingle(int x, int y, int z, int u1, int v1, int u2, int v2) {
        JsonObject element = new JsonObject();
        element.addProperty("name", "leg");
        element.add("from", createArray(x, y, z));
        element.add("to", createArray(x + 3, y + 11, z + 3));
        element.add("rotation", createRotation(0, "y", 8, 10.25, 8));

        JsonObject faces = new JsonObject();
        faces.add("north", createFace(u1, v1, u2, v2, "#1"));
        faces.add("east", createFace(u1, v1, u2, v2, "#1"));
        faces.add("south", createFace(u1, v1, u2, v2, "#1"));
        faces.add("west", createFace(u1, v1, u2, v2, "#1"));
        faces.add("down", createFaceWithCullface(u1, 13, u2, 16, "#1", "down"));
        element.add("faces", faces);

        return element;
    }

    private static JsonObject createTableLegDouble(int x, int y, int z, int u1, int v1, int u2, int v2) {
        JsonObject element = new JsonObject();
        element.addProperty("name", "leg");
        element.add("from", createArray(x, y, z));
        element.add("to", createArray(x + 3, y + 11, z + 3));
        element.add("rotation", createRotation(0, "y", 8, 10.25, 5.5));

        JsonObject faces = new JsonObject();
        faces.add("north", createFace(u1, v1, u2, v2, "#1"));
        faces.add("east", createFace(u1, v1, u2, v2, "#1"));
        faces.add("south", createFace(u1, v1, u2, v2, "#1"));
        faces.add("west", createFace(u1, v1, u2, v2, "#1"));
        faces.add("down", createFaceWithCullface(u1, 13, u2, 16, "#1", "down"));
        element.add("faces", faces);

        return element;
    }

    private static JsonObject createTableTopDouble() {
        JsonObject element = new JsonObject();
        element.addProperty("name", "top");
        element.add("from", createArray(0, 14, 0));
        element.add("to", createArray(16, 16, 16));
        element.add("rotation", createRotation(0, "y", 8, 10.25, 5.5));

        JsonObject faces = new JsonObject();
        faces.add("north", createFaceWithCullface(0, 0, 16, 2, "#1", "north"));
        faces.add("east", createFaceWithCullface(0, 0, 16, 2, "#1", "east"));
        faces.add("south", createFaceWithCullface(0, 0, 16, 2, "#1", "south"));
        faces.add("west", createFaceWithCullface(0, 0, 16, 2, "#1", "west"));
        faces.add("up", createFaceWithCullface(0, 0, 16, 16, "#1", "up"));
        faces.add("down", createFace(0, 0, 16, 16, "#1"));
        element.add("faces", faces);

        return element;
    }

    private static JsonObject createTableTopInnerDouble() {
        JsonObject element = new JsonObject();
        element.addProperty("name", "top");
        element.add("from", createArray(1, 11, 1));
        element.add("to", createArray(15, 14, 16));
        element.add("rotation", createRotation(0, "y", 8, 10.25, 5.5));

        JsonObject faces = new JsonObject();
        faces.add("north", createFace(1, 2, 16, 5, "#1"));
        faces.add("east", createFace(0, 2, 15, 5, "#1"));
        faces.add("south", createFaceWithCullface(1, 2, 15, 5, "#1", "south"));
        faces.add("west", createFace(1, 2, 16, 5, "#1"));
        faces.add("down", createFace(1, 1, 15, 15, "#1"));
        element.add("faces", faces);

        return element;
    }

    private static JsonObject createTableTopCenter() {
        JsonObject element = new JsonObject();
        element.addProperty("name", "top");
        element.add("from", createArray(0, 14, 0));
        element.add("to", createArray(16, 16, 16));
        element.add("rotation", createRotation(0, "y", 8, 13.75, 8));

        JsonObject faces = new JsonObject();
        faces.add("north", createFaceWithCullface(0, 0, 16, 2, "#1", "north"));
        faces.add("east", createFaceWithCullface(0, 0, 16, 2, "#1", "east"));
        faces.add("south", createFaceWithCullface(0, 0, 16, 2, "#1", "south"));
        faces.add("west", createFaceWithCullface(0, 0, 16, 2, "#1", "west"));
        faces.add("up", createFaceWithCullface(0, 0, 16, 16, "#1", "up"));
        faces.add("down", createFace(0, 0, 16, 16, "#1"));
        element.add("faces", faces);

        return element;
    }

    private static JsonObject createTableTopInnerCenter() {
        JsonObject element = new JsonObject();
        element.addProperty("name", "top");
        element.add("from", createArray(1, 11, 0));
        element.add("to", createArray(15, 14, 16));
        element.add("rotation", createRotation(0, "y", 8, 13.75, 8));

        JsonObject faces = new JsonObject();
        faces.add("north", createFaceWithCullface(1, 2, 15, 5, "#1", "north"));
        faces.add("east", createFace(0, 2, 16, 5, "#1"));
        faces.add("south", createFaceWithCullface(1, 2, 15, 5, "#1", "south"));
        faces.add("west", createFace(0, 2, 16, 5, "#1"));
        faces.add("down", createFace(1, 1, 15, 15, "#1"));
        element.add("faces", faces);

        return element;
    }

    private static JsonObject createTableTopCorner() {
        JsonObject element = new JsonObject();
        element.addProperty("name", "top");
        element.add("from", createArray(0, 14, 0));
        element.add("to", createArray(16, 16, 16));
        element.add("rotation", createRotation(0, "y", 5.5, 10.25, 5.5));

        JsonObject faces = new JsonObject();
        faces.add("north", createFaceWithCullface(0, 0, 16, 2, "#1", "north"));
        faces.add("east", createFaceWithCullface(0, 0, 16, 2, "#1", "east"));
        faces.add("south", createFaceWithCullface(0, 0, 16, 2, "#1", "south"));
        faces.add("west", createFaceWithCullface(0, 0, 16, 2, "#1", "west"));
        faces.add("up", createFaceWithCullface(0, 0, 16, 16, "#1", "up"));
        faces.add("down", createFace(0, 0, 16, 16, "#1"));
        element.add("faces", faces);

        return element;
    }

    private static JsonObject createTableTopInnerCorner() {
        JsonObject element = new JsonObject();
        element.addProperty("name", "top");
        element.add("from", createArray(1, 11, 1));
        element.add("to", createArray(16, 14, 16));
        element.add("rotation", createRotation(0, "y", 5.5, 10.25, 5.5));

        JsonObject faces = new JsonObject();
        faces.add("north", createFace(0, 2, 15, 5, "#1"));
        faces.add("east", createFaceWithCullface(0, 2, 15, 5, "#1", "east"));
        faces.add("south", createFaceWithCullface(1, 2, 16, 5, "#1", "south"));
        faces.add("west", createFace(1, 2, 16, 5, "#1"));
        faces.add("down", createFace(1, 0, 16, 15, "#1"));
        element.add("faces", faces);

        return element;
    }

    private static JsonObject createTableLegCorner() {
        JsonObject element = new JsonObject();
        element.addProperty("name", "leg");
        element.add("from", createArray(1, 0, 1));
        element.add("to", createArray(4, 11, 4));
        element.add("rotation", createRotation(0, "y", 5.5, 10.25, 5.5));

        JsonObject faces = new JsonObject();
        faces.add("north", createFace(0, 5, 3, 16, "#1"));
        faces.add("east", createFace(0, 5, 3, 16, "#1"));
        faces.add("south", createFace(0, 5, 3, 16, "#1"));
        faces.add("west", createFace(0, 5, 3, 16, "#1"));
        faces.add("down", createFaceWithCullface(0, 13, 3, 16, "#1", "down"));
        element.add("faces", faces);

        return element;
    }

    private static JsonObject createFace(int u1, int v1, int u2, int v2, String texture) {
        JsonObject face = new JsonObject();
        face.add("uv", createArray(u1, v1, u2, v2));
        face.addProperty("texture", texture);
        return face;
    }

    private static JsonObject createFaceWithCullface(int u1, int v1, int u2, int v2, String texture, String cullface) {
        JsonObject face = new JsonObject();
        face.add("uv", createArray(u1, v1, u2, v2));
        face.addProperty("texture", texture);
        face.addProperty("cullface", cullface);
        return face;
    }

    private static JsonObject createRotation(int angle, String axis, double x, double y, double z) {
        JsonObject rotation = new JsonObject();
        rotation.addProperty("angle", angle);
        rotation.addProperty("axis", axis);
        rotation.add("origin", createArray(x, y, z));
        return rotation;
    }

    private static JsonArray createArray(double... values) {
        JsonArray array = new JsonArray();
        for (double value : values) {
            array.add(value);
        }
        return array;
    }

    private static JsonArray createArray(int... values) {
        JsonArray array = new JsonArray();
        for (int value : values) {
            array.add(value);
        }
        return array;
    }

    public static void generateItemModels(ItemModelGenerator generator, Block block) {
        String modelPath = "block/generated/" + block.getTranslationKey().replace("block.westerosblocks.", "") + "_single";
        Model model = new Model(
            Optional.of(WesterosBlocks.id(modelPath)),
            Optional.empty()
        );
        generator.register(block.asItem(), model);
    }
} 