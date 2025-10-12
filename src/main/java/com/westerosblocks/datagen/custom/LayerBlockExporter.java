package com.westerosblocks.datagen.custom;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.custom.WCLayerBlock;
import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Exporter for layer blocks following block-models.md patterns.
 * Generates models for stackable layer blocks (snow layers, carpets, etc.) with variable heights.
 */
public class LayerBlockExporter extends BaseBlockExporter {

    /**
     * Registers a layer block from a BlockDefinition.
     * Automatically handles states, randomTextures, and layer counts.
     */
    public static void registerCustomLayerBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        if (!(block instanceof WCLayerBlock layerBlock)) {
            throw new IllegalArgumentException("Block must be a WCLayerBlock instance");
        }

        // Use centralized priority logic from BlockDefinition
        BlockDefinition.TextureSource source = definition.getPrimaryTextureSource();

        switch (source) {
            case RANDOM_TEXTURES -> registerLayerBlockWithRandomTextures(generator, block, definition, layerBlock);
            case TEXTURES -> registerSimpleLayerBlock(generator, block, definition, layerBlock);
            case CUSTOM_MODEL -> registerCustomModelLayerBlock(generator, block, definition, layerBlock);
            case NONE, STATES -> registerFallbackLayerBlock(generator, block, definition, layerBlock);
        }
    }

    /**
     * Registers a simple layer block with basic textures.
     */
    private static void registerSimpleLayerBlock(BlockStateModelGenerator generator, Block block,
                                                  BlockDefinition definition, WCLayerBlock layerBlock) {
        List<String> textures = definition.getTextures();
        Map<Integer, Identifier> layerModels = new HashMap<>();

        // Generate models for each layer height (1 through layerCount)
        for (int layer = 1; layer <= layerBlock.layerCount; layer++) {
            Identifier modelId;
            if (definition.hasCustomModel()) {
                modelId = createCustomModelId(block, "layer" + layer + "_v1");
            } else {
                modelId = generateLayerModel(generator, block, definition, textures, layer, 0, layerBlock);
            }
            layerModels.put(layer, modelId);
        }

        // Generate blockstate with layer variants
        generator.blockStateCollector.accept(createLayerBlockState(block, layerModels, null));

        // Register item model (uses layer 1)
        registerParentedItemModel(generator, block, layerModels.get(1));
    }

    /**
     * Registers a layer block with random texture variants.
     */
    private static void registerLayerBlockWithRandomTextures(BlockStateModelGenerator generator, Block block,
                                                             BlockDefinition definition, WCLayerBlock layerBlock) {
        List<BlockDefinition.RandomTextureVariant> randomTextures = definition.getRandomTextures();
        Map<Integer, List<LayerModelSet>> layerModelSets = new HashMap<>();

        // For each layer height
        for (int layer = 1; layer <= layerBlock.layerCount; layer++) {
            List<LayerModelSet> modelSets = new ArrayList<>();

            // For each random texture variant
            for (int i = 0; i < randomTextures.size(); i++) {
                BlockDefinition.RandomTextureVariant variant = randomTextures.get(i);
                List<String> textures = variant.getTextures();

                Identifier modelId;
                if (definition.hasCustomModel()) {
                    modelId = createCustomModelId(block, "layer" + layer + "_v" + (i + 1));
                } else {
                    modelId = generateLayerModel(generator, block, definition, textures, layer, i, layerBlock);
                }
                modelSets.add(new LayerModelSet(modelId, variant.getWeight()));
            }

            layerModelSets.put(layer, modelSets);
        }

        // Generate blockstate with weighted random variants
        generator.blockStateCollector.accept(createLayerBlockStateWithRandomTextures(block, layerModelSets));

        // Register item model (uses layer 1, first variant)
        registerParentedItemModel(generator, block, layerModelSets.get(1).get(0).model);
    }

    /**
     * Registers a layer block with custom model references.
     */
    private static void registerCustomModelLayerBlock(BlockStateModelGenerator generator, Block block,
                                                      BlockDefinition definition, WCLayerBlock layerBlock) {
        Map<Integer, Identifier> layerModels = new HashMap<>();

        for (int layer = 1; layer <= layerBlock.layerCount; layer++) {
            layerModels.put(layer, createCustomModelId(block, "layer" + layer + "_v1"));
        }

        generator.blockStateCollector.accept(createLayerBlockState(block, layerModels, null));
        registerParentedItemModel(generator, block, layerModels.get(1));
    }

    /**
     * Fallback registration for layer blocks with no textures defined.
     */
    private static void registerFallbackLayerBlock(BlockStateModelGenerator generator, Block block,
                                                   BlockDefinition definition, WCLayerBlock layerBlock) {
        List<String> fallbackTextures = List.of("missing", "missing", "missing", "missing", "missing", "missing");
        Map<Integer, Identifier> layerModels = new HashMap<>();

        for (int layer = 1; layer <= layerBlock.layerCount; layer++) {
            Identifier modelId = generateLayerModel(generator, block, definition, fallbackTextures, layer, 0, layerBlock);
            layerModels.put(layer, modelId);
        }

        generator.blockStateCollector.accept(createLayerBlockState(block, layerModels, null));
        registerParentedItemModel(generator, block, layerModels.get(1));
    }

    /**
     * Generates a layer model for a specific height.
     */
    private static Identifier generateLayerModel(BlockStateModelGenerator generator, Block block,
                                                BlockDefinition definition, List<String> textures,
                                                int layer, int variantIndex, WCLayerBlock layerBlock) {
        String variantName = "layer" + layer + "_v" + (variantIndex + 1);
        Identifier modelId = createGeneratedModelId(block, variantName);

        // Create model JSON
        JsonObject modelJson = new JsonObject();
        modelJson.addProperty("parent", "minecraft:block/thin_block");

        // Add textures
        JsonObject texturesJson = new JsonObject();
        int cnt = Math.max(6, textures.size());
        for (int j = 0; j < cnt; j++) {
            String texture = j < textures.size() ? textures.get(j) : textures.get(textures.size() - 1);
            texturesJson.addProperty("txt" + j, "westerosblocks:block/" + texture);
        }
        texturesJson.addProperty("particle", "westerosblocks:block/" + textures.get(0));
        modelJson.add("textures", texturesJson);

        // Calculate height for this layer
        float ymax = (16.0f / layerBlock.layerCount) * layer;

        // Add elements array with single cuboid element
        JsonArray elements = new JsonArray();
        JsonObject element = new JsonObject();

        // From/to coordinates
        JsonArray from = new JsonArray();
        from.add(0);
        from.add(0);
        from.add(0);
        element.add("from", from);

        JsonArray to = new JsonArray();
        to.add(16);
        to.add(ymax);
        to.add(16);
        element.add("to", to);

        // Add faces
        JsonObject faces = new JsonObject();
        boolean isTinted = definition.isTinted();

        // Down face
        addLayerFace(faces, "down", 0, 0, 16, 16, "#txt0", "down", isTinted);

        // Up face (only cullface if at max height)
        String upCullface = (layer >= layerBlock.layerCount) ? "up" : null;
        addLayerFace(faces, "up", 0, 0, 16, 16, "#txt1", upCullface, isTinted);

        // North face (UV adjusts for height)
        addLayerFace(faces, "north", 0, 16 - ymax, 16, 16, "#txt2", "north", isTinted);

        // South face
        addLayerFace(faces, "south", 0, 16 - ymax, 16, 16, "#txt3", "south", isTinted);

        // West face
        addLayerFace(faces, "west", 0, 16 - ymax, 16, 16, "#txt4", "west", isTinted);

        // East face
        addLayerFace(faces, "east", 0, 16 - ymax, 16, 16, "#txt5", "east", isTinted);

        element.add("faces", faces);
        elements.add(element);
        modelJson.add("elements", elements);

        // Upload model
        generator.modelCollector.accept(modelId, () -> modelJson);

        return modelId;
    }

    /**
     * Adds a face to the layer element.
     */
    private static void addLayerFace(JsonObject faces, String direction, float u0, float v0, float u1, float v1,
                                     String texture, String cullface, boolean tinted) {
        JsonObject face = new JsonObject();

        JsonArray uv = new JsonArray();
        uv.add(u0);
        uv.add(v0);
        uv.add(u1);
        uv.add(v1);
        face.add("uv", uv);

        face.addProperty("texture", texture);

        if (cullface != null) {
            face.addProperty("cullface", cullface);
        }

        if (tinted) {
            face.addProperty("tintindex", 0);
        }

        faces.add(direction, face);
    }

    /**
     * Creates a blockstate for layer blocks with simple models.
     */
    private static BlockStateSupplier createLayerBlockState(Block block, Map<Integer, Identifier> layerModels,
                                                            Map<Integer, Integer> weights) {
        return new BlockStateSupplier() {
            @Override
            public Block getBlock() {
                return block;
            }

            @Override
            public JsonElement get() {
                JsonObject json = new JsonObject();
                JsonObject variants = new JsonObject();

                for (Map.Entry<Integer, Identifier> entry : layerModels.entrySet()) {
                    int layer = entry.getKey();
                    Identifier modelId = entry.getValue();

                    JsonObject variant = new JsonObject();
                    variant.addProperty("model", modelId.toString());

                    if (weights != null && weights.containsKey(layer)) {
                        variant.addProperty("weight", weights.get(layer));
                    }

                    variants.add("layers=" + layer, variant);
                }

                json.add("variants", variants);
                return json;
            }
        };
    }

    /**
     * Creates a blockstate for layer blocks with random textures.
     */
    private static BlockStateSupplier createLayerBlockStateWithRandomTextures(Block block,
                                                                              Map<Integer, List<LayerModelSet>> layerModelSets) {
        return new BlockStateSupplier() {
            @Override
            public Block getBlock() {
                return block;
            }

            @Override
            public JsonElement get() {
                JsonObject json = new JsonObject();
                JsonObject variants = new JsonObject();

                for (Map.Entry<Integer, List<LayerModelSet>> entry : layerModelSets.entrySet()) {
                    int layer = entry.getKey();
                    List<LayerModelSet> modelSets = entry.getValue();

                    if (modelSets.size() == 1) {
                        // Single variant
                        JsonObject variant = new JsonObject();
                        variant.addProperty("model", modelSets.get(0).model.toString());
                        variants.add("layers=" + layer, variant);
                    } else {
                        // Multiple weighted variants
                        JsonArray variantArray = new JsonArray();
                        for (LayerModelSet modelSet : modelSets) {
                            JsonObject variant = new JsonObject();
                            variant.addProperty("model", modelSet.model.toString());
                            if (modelSet.weight > 1) {
                                variant.addProperty("weight", modelSet.weight);
                            }
                            variantArray.add(variant);
                        }
                        variants.add("layers=" + layer, variantArray);
                    }
                }

                json.add("variants", variants);
                return json;
            }
        };
    }

    private static Identifier createCustomModelId(Block block, String variant) {
        String blockName = getBlockName(block);
        return WesterosBlocks.id("block/custom/" + blockName + "/" + variant);
    }

    private static Identifier createGeneratedModelId(Block block, String variant) {
        String blockName = getBlockName(block);
        return WesterosBlocks.id("block/" + blockName + "/" + variant);
    }

    /**
     * Helper class to hold a layer model with weight.
     */
    private static class LayerModelSet {
        final Identifier model;
        final int weight;

        LayerModelSet(Identifier model, int weight) {
            this.model = model;
            this.weight = weight;
        }
    }
}
