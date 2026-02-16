package com.westerosblocks.datagen.custom;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.westerosblocks.datagen.ModModels;
import com.westerosblocks.datagen.ModTextureMap;
import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

import java.util.List;

public class SlabBlockExporter extends BaseBlockExporter {

    public static void registerCustomSlabBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        var states = definition.getStates();
        if (states == null || states.isEmpty()) {
            throw new IllegalStateException("Block definition states should never be null/empty after doInit() for block: " + getBlockName(block));
        }

        generateBlockState(generator, block, states);

        for (int stateIdx = 0; stateIdx < states.size(); stateIdx++) {
            BlockDefinition.StateVariant state = states.get(stateIdx);
            String stateID = state.getStateID();
            String fname = getStateIdOrBase(stateID);

            for (int setIdx = 0; setIdx < state.getRandomTextureSetCount(); setIdx++) {
                if (definition.hasCustomModel() || state.isCustomModel()) {
                    continue;
                } else {
                    generateSlabModels(generator, block, fname, setIdx, state, definition);
                }
            }
        }

        BlockDefinition.StateVariant firstState = states.get(0);
        String firstName = getStateIdOrBase(firstState.getStateID());
        Identifier itemModelId = createNestedModelId(block, getModelName(firstName, 0, "bottom"));
        registerParentedItemModel(generator, block, itemModelId);
    }

    private static void generateBlockState(BlockStateModelGenerator generator, Block block,
                                          List<BlockDefinition.StateVariant> states) {
        JsonObject root = new JsonObject();
        JsonObject variants = new JsonObject();

        for (BlockDefinition.StateVariant state : states) {
            String stateID = state.getStateID();
            String fname = getStateIdOrBase(stateID);

            for (int setIdx = 0; setIdx < state.getRandomTextureSetCount(); setIdx++) {
                BlockDefinition.RandomTextureVariant set = state.getRandomTextureSet(setIdx);
                if (set == null) continue;

                int weight = set.getWeight();

                // Build model IDs for the three slab types
                Identifier bottomModel = createNestedModelId(block, getModelName(fname, setIdx, "bottom"));
                Identifier topModel = createNestedModelId(block, getModelName(fname, setIdx, "top"));
                Identifier doubleModel = createNestedModelId(block, getModelName(fname, setIdx, "double"));

                // Add variants for each slab type
                addSlabVariant(variants, "type=bottom", bottomModel, weight);
                addSlabVariant(variants, "type=top", topModel, weight);
                addSlabVariant(variants, "type=double", doubleModel, weight);
            }
        }

        root.add("variants", variants);

        // Register the blockstate
        generator.blockStateCollector.accept(new BlockStateSupplier() {
            @Override
            public Block getBlock() {
                return block;
            }

            @Override
            public JsonElement get() {
                return root;
            }
        });
    }

    private static void addSlabVariant(JsonObject variants, String variantKey, Identifier modelId, int weight) {
        JsonObject variantData = new JsonObject();
        variantData.addProperty("model", modelId.toString());
        if (weight > 1) {
            variantData.addProperty("weight", weight);
        }

        if (variants.has(variantKey)) {
            // Already exists - convert to array if needed
            JsonElement existing = variants.get(variantKey);
            if (existing.isJsonObject()) {
                // Convert single object to array
                JsonArray array = new JsonArray();
                array.add(existing);
                array.add(variantData);
                variants.add(variantKey, array);
            } else if (existing.isJsonArray()) {
                // Add to existing array
                existing.getAsJsonArray().add(variantData);
            }
        } else {
            // First variant for this key
            variants.add(variantKey, variantData);
        }
    }

    private static void generateSlabModels(BlockStateModelGenerator generator, Block block,
                                          String fname, int setIdx, BlockDefinition.StateVariant state,
                                          BlockDefinition definition) {
        BlockDefinition.RandomTextureVariant set = state.getRandomTextureSet(setIdx);
        if (set == null || set.getTextureCount() == 0) {
            // Fallback to missing texture
            generateSlabModelsWithTextures(generator, block, fname, setIdx,
                new String[]{"missing"}, false, false);
            return;
        }

        String[] textures = new String[set.getTextureCount()];
        for (int i = 0; i < set.getTextureCount(); i++) {
            textures[i] = set.getTextureByIndex(i);
        }

        boolean isTinted = definition.isTinted() || state.hasOverlayTextures();
        boolean isOverlay = state.hasOverlayTextures();

        generateSlabModelsWithTextures(generator, block, fname, setIdx, textures, isTinted, isOverlay);
    }


    private static void generateSlabModelsWithTextures(BlockStateModelGenerator generator, Block block,
                                                       String fname, int setIdx, String[] textures,
                                                       boolean isTinted, boolean isOverlay) {
        String[] filledTextures = fillTextureArray(textures);
        TextureMap textureMap = ModTextureMap.customAllSides(filledTextures);

        Model bottomModel = ModModels.SLAB_BOTTOM;
        Model topModel = ModModels.SLAB_TOP;
        Model doubleModel = Models.CUBE;

        // Upload all three models
        Identifier bottomModelId = createNestedModelId(block, getModelName(fname, setIdx, "bottom"));
        bottomModel.upload(bottomModelId, textureMap, generator.modelCollector);

        Identifier topModelId = createNestedModelId(block, getModelName(fname, setIdx, "top"));
        topModel.upload(topModelId, textureMap, generator.modelCollector);

        Identifier doubleModelId = createNestedModelId(block, getModelName(fname, setIdx, "double"));
        doubleModel.upload(doubleModelId, textureMap, generator.modelCollector);
    }

    protected static String getModelName(String fname, int setIdx, String variant) {
        if (setIdx == 0 && fname.equals("base")) {
            // For base state with single texture set, use simple names
            return variant;
        }
        return fname + "_v" + (setIdx + 1) + "_" + variant;
    }
}
