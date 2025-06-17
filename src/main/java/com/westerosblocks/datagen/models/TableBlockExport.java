package com.westerosblocks.datagen.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.custom.WCTableBlock;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

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
        // Create models for each table type
        Identifier singleModelId = createModel(generator, block, TableType.SINGLE, texturePath);
        Identifier doubleModelId = createModel(generator, block, TableType.DOUBLE, texturePath);
        Identifier centerModelId = createModel(generator, block, TableType.CENTER, texturePath);
        Identifier cornerModelId = createModel(generator, block, TableType.CORNER, texturePath);

        // Create variants for each state
        BlockStateVariantMap variants = BlockStateVariantMap.create(WCTableBlock.NORTH, WCTableBlock.EAST, WCTableBlock.SOUTH, WCTableBlock.WEST, WCTableBlock.WATERLOGGED)
            // Single table (no connections)
            .register(false, false, false, false, false, BlockStateVariant.create()
                .put(VariantSettings.MODEL, singleModelId))
            .register(false, false, false, false, true, BlockStateVariant.create()
                .put(VariantSettings.MODEL, singleModelId))
            
            // Double table (connected in one direction)
            .register(false, false, false, true, false, BlockStateVariant.create()
                .put(VariantSettings.MODEL, doubleModelId))
            .register(false, false, false, true, true, BlockStateVariant.create()
                .put(VariantSettings.MODEL, doubleModelId))
            .register(false, false, true, false, false, BlockStateVariant.create()
                .put(VariantSettings.MODEL, doubleModelId))
            .register(false, false, true, false, true, BlockStateVariant.create()
                .put(VariantSettings.MODEL, doubleModelId))
            .register(false, true, false, false, false, BlockStateVariant.create()
                .put(VariantSettings.MODEL, doubleModelId))
            .register(false, true, false, false, true, BlockStateVariant.create()
                .put(VariantSettings.MODEL, doubleModelId))
            .register(true, false, false, false, false, BlockStateVariant.create()
                .put(VariantSettings.MODEL, doubleModelId))
            .register(true, false, false, false, true, BlockStateVariant.create()
                .put(VariantSettings.MODEL, doubleModelId))
            
            // Corner table (connected in two adjacent directions)
            .register(false, false, true, true, false, BlockStateVariant.create()
                .put(VariantSettings.MODEL, cornerModelId))
            .register(false, false, true, true, true, BlockStateVariant.create()
                .put(VariantSettings.MODEL, cornerModelId))
            .register(false, true, false, true, false, BlockStateVariant.create()
                .put(VariantSettings.MODEL, cornerModelId))
            .register(false, true, false, true, true, BlockStateVariant.create()
                .put(VariantSettings.MODEL, cornerModelId))
            .register(false, true, true, false, false, BlockStateVariant.create()
                .put(VariantSettings.MODEL, cornerModelId))
            .register(false, true, true, false, true, BlockStateVariant.create()
                .put(VariantSettings.MODEL, cornerModelId))
            .register(true, false, false, true, false, BlockStateVariant.create()
                .put(VariantSettings.MODEL, cornerModelId))
            .register(true, false, false, true, true, BlockStateVariant.create()
                .put(VariantSettings.MODEL, cornerModelId))
            .register(true, false, true, false, false, BlockStateVariant.create()
                .put(VariantSettings.MODEL, cornerModelId))
            .register(true, false, true, false, true, BlockStateVariant.create()
                .put(VariantSettings.MODEL, cornerModelId))
            .register(true, true, false, false, false, BlockStateVariant.create()
                .put(VariantSettings.MODEL, cornerModelId))
            .register(true, true, false, false, true, BlockStateVariant.create()
                .put(VariantSettings.MODEL, cornerModelId))
            
            // Center table (connected in three or four directions)
            .register(false, true, true, true, false, BlockStateVariant.create()
                .put(VariantSettings.MODEL, centerModelId))
            .register(false, true, true, true, true, BlockStateVariant.create()
                .put(VariantSettings.MODEL, centerModelId))
            .register(true, false, true, true, false, BlockStateVariant.create()
                .put(VariantSettings.MODEL, centerModelId))
            .register(true, false, true, true, true, BlockStateVariant.create()
                .put(VariantSettings.MODEL, centerModelId))
            .register(true, true, false, true, false, BlockStateVariant.create()
                .put(VariantSettings.MODEL, centerModelId))
            .register(true, true, false, true, true, BlockStateVariant.create()
                .put(VariantSettings.MODEL, centerModelId))
            .register(true, true, true, false, false, BlockStateVariant.create()
                .put(VariantSettings.MODEL, centerModelId))
            .register(true, true, true, false, true, BlockStateVariant.create()
                .put(VariantSettings.MODEL, centerModelId))
            .register(true, true, true, true, false, BlockStateVariant.create()
                .put(VariantSettings.MODEL, centerModelId))
            .register(true, true, true, true, true, BlockStateVariant.create()
                .put(VariantSettings.MODEL, centerModelId));

        // Register the block state
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

        // Add textures
        JsonObject textures = new JsonObject();
        textures.addProperty(TEXTURE_KEY, texturePath);
        textures.addProperty(PARTICLE_KEY, texturePath);
        modelJson.add("textures", textures);

        // Add elements based on type
        JsonArray elements = new JsonArray();
        
        // Common table top for all types
        elements.add(createTableTop());

        // Type-specific legs
        switch (type) {
            case SINGLE -> {
                elements.add(createTableLeg(2, 0, 2));
                elements.add(createTableLeg(2, 0, 10));
                elements.add(createTableLeg(10, 0, 10));
                elements.add(createTableLeg(10, 0, 2));
            }
            case DOUBLE -> {
                elements.add(createTableLeg(2, 0, 2));
                elements.add(createTableLeg(10, 0, 2));
            }
            case CENTER -> {
                // No legs for center piece
            }
            case CORNER -> {
                elements.add(createTableLeg(2, 0, 2));
            }
        }

        modelJson.add("elements", elements);

        // Create a unique model ID for this block and type
        String modelPath = "block/generated/" + block.getTranslationKey().replace("block.westerosblocks.", "") + "_" + type.asString();
        Identifier modelId = WesterosBlocks.id(modelPath);

        // Register the model
        generator.modelCollector.accept(modelId, () -> modelJson);

        return modelId;
    }

    private static JsonObject createTableTop() {
        JsonObject element = new JsonObject();
        element.addProperty("name", "top");
        element.add("from", createArray(0, 12, 0));
        element.add("to", createArray(16, 16, 16));
        
        JsonObject rotation = new JsonObject();
        rotation.addProperty("angle", 0);
        rotation.addProperty("axis", "y");
        rotation.add("origin", createArray(8, 2, 8));
        element.add("rotation", rotation);

        JsonObject faces = new JsonObject();
        faces.add("north", createFace(0, 0, 16, 4, "#1"));
        faces.add("east", createFace(0, 0, 16, 4, "#1"));
        faces.add("south", createFace(0, 0, 16, 4, "#1"));
        faces.add("west", createFace(0, 0, 16, 4, "#1"));
        faces.add("up", createFace(0, 0, 16, 16, "#1"));
        faces.add("down", createFace(0, 0, 16, 16, "#1"));
        element.add("faces", faces);

        return element;
    }

    private static JsonObject createTableLeg(int x, int y, int z) {
        JsonObject element = new JsonObject();
        element.addProperty("name", "leg");
        element.add("from", createArray(x, y, z));
        element.add("to", createArray(x + 4, y + 12, z + 4));
        
        JsonObject rotation = new JsonObject();
        rotation.addProperty("angle", 0);
        rotation.addProperty("axis", "y");
        rotation.add("origin", createArray(x, y, z));
        element.add("rotation", rotation);

        JsonObject faces = new JsonObject();
        faces.add("north", createFace(0, 0, 4, 16, "#1"));
        faces.add("east", createFace(0, 0, 4, 16, "#1"));
        faces.add("south", createFace(0, 0, 4, 16, "#1"));
        faces.add("west", createFace(0, 0, 4, 16, "#1"));
        faces.add("up", createFace(0, 0, 2, 2, "#missing"));
        faces.add("down", createFace(0, 0, 4, 4, "#1"));
        element.add("faces", faces);

        return element;
    }

    private static JsonObject createFace(int u1, int v1, int u2, int v2, String texture) {
        JsonObject face = new JsonObject();
        face.add("uv", createArray(u1, v1, u2, v2));
        face.addProperty("texture", texture);
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
        // Create a simple item model that inherits from the single table model
        String modelPath = "block/generated/" + block.getTranslationKey().replace("block.westerosblocks.", "") + "_single";
        Model model = new Model(
            Optional.of(WesterosBlocks.id(modelPath)),
            Optional.empty()
        );
        generator.register(block.asItem(), model);
    }
} 