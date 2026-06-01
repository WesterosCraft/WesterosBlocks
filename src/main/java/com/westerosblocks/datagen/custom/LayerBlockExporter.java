package com.westerosblocks.datagen.custom;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.westerosblocks.block.custom.WCLayerBlock;
import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

/**
 * Exporter for {@code layer} blocks. Builds per-layer-height model JSON by hand (Gson) because
 * each layer needs custom element geometry not expressible via a vanilla {@code Model} template;
 * the JSON is still emitted through {@code generator.modelCollector} (the idiomatic sink).
 */
public class LayerBlockExporter extends BaseBlockExporter {

    public static void registerCustomLayerBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        if (!(block instanceof WCLayerBlock layerBlock)) {
            throw new IllegalArgumentException("Block must be a WCLayerBlock instance");
        }

        var states = definition.getStates();
        if (states == null || states.isEmpty()) {
            throw new IllegalStateException("Block definition states should never be null/empty after doInit() for block: " + getBlockName(block));
        }

        BlockDefinition.StateVariant state = states.getFirst();

        generateBlockState(generator, block, definition, state, layerBlock);

        if (!definition.hasCustomModel()) {
            for (int layer = 1; layer <= layerBlock.layerCount; layer++) {
                for (int setIdx = 0; setIdx < state.getRandomTextureSetCount(); setIdx++) {
                    generateLayerModel(generator, block, definition, state, layer, setIdx, layerBlock);
                }
            }
        }

        String firstName = "layer1";
        Identifier itemModelId = definition.hasCustomModel()
                ? createCustomModelId(block, firstName + "_v1")
                : createGeneratedModelId(block, getModelName(firstName, 0));
        registerParentedItemModel(generator, block, itemModelId);
    }

    private static void generateBlockState(BlockStateModelGenerator generator, Block block,
                                          BlockDefinition definition, BlockDefinition.StateVariant state,
                                          WCLayerBlock layerBlock) {
        boolean isCustomModel = definition.hasCustomModel();

        BlockStateVariantMap.SingleProperty<Integer> variantMap =
            BlockStateVariantMap.create(Properties.LAYERS);

        for (int layer = 1; layer <= layerBlock.layerCount; layer++) {
            if (isCustomModel) {
                Identifier modelId = createCustomModelId(block, "layer" + layer + "_v1");
                variantMap.register(layer, BlockStateVariant.create().put(VariantSettings.MODEL, modelId));
            } else {
                List<BlockStateVariant> layerVariants = new ArrayList<>();

                for (int setIdx = 0; setIdx < state.getRandomTextureSetCount(); setIdx++) {
                    BlockDefinition.RandomTextureVariant set = state.getRandomTextureSet(setIdx);
                    if (set == null || set.getTextureCount() == 0) {
                        continue;
                    }

                    Identifier modelId = createGeneratedModelId(block, getModelName("layer" + layer, setIdx));
                    BlockStateVariant variant = BlockStateVariant.create()
                        .put(VariantSettings.MODEL, modelId);
                    if (set.getWeight() > 1) {
                        variant.put(VariantSettings.WEIGHT, set.getWeight());
                    }
                    layerVariants.add(variant);
                }

                if (layerVariants.size() == 1) {
                    variantMap.register(layer, layerVariants.getFirst());
                } else if (!layerVariants.isEmpty()) {
                    variantMap.register(layer, layerVariants);
                }
            }
        }

        generator.blockStateCollector.accept(
            VariantsBlockStateSupplier.create(block).coordinate(variantMap)
        );
    }

    // Model generation stays as raw JSON due to custom element geometry
    private static void generateLayerModel(BlockStateModelGenerator generator, Block block,
                                          BlockDefinition definition, BlockDefinition.StateVariant state,
                                          int layer, int setIdx, WCLayerBlock layerBlock) {
        BlockDefinition.RandomTextureVariant set = state.getRandomTextureSet(setIdx);
        if (set == null || set.getTextureCount() == 0) {
            return;
        }

        String variantName = getModelName("layer" + layer, setIdx);
        Identifier modelId = createGeneratedModelId(block, variantName);
        boolean isTinted = definition.isTinted();

        JsonObject modelJson = new JsonObject();
        modelJson.addProperty("parent", "minecraft:block/thin_block");

        JsonObject texturesJson = new JsonObject();
        int cnt = Math.max(6, set.getTextureCount());
        for (int j = 0; j < cnt; j++) {
            String texture = j < set.getTextureCount()
                ? set.getTextureByIndex(j)
                : set.getTextureByIndex(set.getTextureCount() - 1);
            texturesJson.addProperty("txt" + j, "westerosblocks:block/" + texture);
        }
        texturesJson.addProperty("particle", "westerosblocks:block/" + set.getTextureByIndex(0));
        modelJson.add("textures", texturesJson);

        float ymax = (16.0f / layerBlock.layerCount) * layer;

        JsonArray elements = new JsonArray();
        JsonObject element = new JsonObject();

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

        JsonObject faces = new JsonObject();
        addLayerFace(faces, "down", 0, 0, 16, 16, "#txt0", "down", isTinted);
        String upCullface = (layer >= layerBlock.layerCount) ? "up" : null;
        addLayerFace(faces, "up", 0, 0, 16, 16, "#txt1", upCullface, isTinted);
        addLayerFace(faces, "north", 0, 16 - ymax, 16, 16, "#txt2", "north", isTinted);
        addLayerFace(faces, "south", 0, 16 - ymax, 16, 16, "#txt3", "south", isTinted);
        addLayerFace(faces, "west", 0, 16 - ymax, 16, 16, "#txt4", "west", isTinted);
        addLayerFace(faces, "east", 0, 16 - ymax, 16, 16, "#txt5", "east", isTinted);

        element.add("faces", faces);
        elements.add(element);
        modelJson.add("elements", elements);

        generator.modelCollector.accept(modelId, () -> modelJson);
    }

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
}
