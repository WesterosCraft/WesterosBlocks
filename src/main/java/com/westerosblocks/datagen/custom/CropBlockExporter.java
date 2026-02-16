package com.westerosblocks.datagen.custom;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

import java.util.List;

public class CropBlockExporter extends BaseBlockExporter {

    public static void registerCustomCropBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        boolean tinted = definition.isTinted() || definition.hasColorMult();
        boolean layerSensitive = definition.isLayerSensitive();
        boolean rotateRandom = definition.hasRotateRandom();
        var states = definition.getStates();

        if (states == null || states.isEmpty()) {
            throw new IllegalStateException("Crop block definition states should never be null/empty for block: " + getBlockName(block));
        }

        generateBlockState(generator, block, definition, states, layerSensitive, rotateRandom);

        for (BlockDefinition.StateVariant state : states) {
            if (state.isCustomModel()) continue;

            String stateID = state.getStateID();
            String baseName = getStateIdOrBase(stateID);

            int layerCount = layerSensitive ? 8 : 1;
            for (int layer = 8; layer >= (layerSensitive ? 1 : 8); layer--) {

                String layerSuffix = layer != 8 ? "_layer" + layer : "";
                String modelName = baseName + layerSuffix;

                // Loop over texture sets
                for (int setIdx = 0; setIdx < state.getRandomTextureSetCount(); setIdx++) {
                    generateCropModel(generator, block, modelName, state, setIdx, tinted, layer, layerSensitive);
                }

            }
        }

        BlockDefinition.StateVariant firstState = states.get(0);
        String firstName = getStateIdOrBase(firstState.getStateID());
        Identifier itemModelId = createGeneratedModelId(block, getModelName(firstName, 0));
        registerParentedItemModel(generator, block, itemModelId);
    }

    private static void generateBlockState(BlockStateModelGenerator generator, Block block,
                                          BlockDefinition definition, List<BlockDefinition.StateVariant> states,
                                          boolean layerSensitive, boolean rotateRandom) {
        int rotationCount = rotateRandom ? 4 : 1; // 4 rotations if random, 1 if not

        generator.blockStateCollector.accept(new BlockStateSupplier() {
            @Override
            public Block getBlock() {
                return block;
            }

            @Override
            public JsonElement get() {
                JsonObject json = new JsonObject();
                JsonObject variants = new JsonObject();

                // Determine layer conditions (matches old layerConds logic)
                String[] layerConds = layerSensitive
                    ? new String[]{"layers=8", "layers=1", "layers=2", "layers=3", "layers=4", "layers=5", "layers=6", "layers=7"}
                    : new String[]{""};

                // Check if block has STATE property (for multi-state crops)
                boolean hasStateProperty = hasStateProperty(block);

                // Loop: layers → states → texture sets → rotations (matches old nested loop exactly)
                for (String layerCond : layerConds) {
                    for (int stateIdx = 0; stateIdx < states.size(); stateIdx++) {
                        BlockDefinition.StateVariant state = states.get(stateIdx);
                        String stateID = state.getStateID();
                        String baseName = getStateIdOrBase(stateID);

                        // Add layer suffix to model name if layer > 0
                        String modelName = baseName;
                        if (layerSensitive && !layerCond.isEmpty() && !layerCond.equals("layers=8")) {
                            // Extract layer number from condition like "layers=1"
                            int layerNum = Integer.parseInt(layerCond.substring(layerCond.indexOf('=') + 1));
                            modelName = baseName + "_layer" + layerNum;
                        }

                        // Loop over texture sets
                        for (int setIdx = 0; setIdx < state.getRandomTextureSetCount(); setIdx++) {
                            BlockDefinition.RandomTextureVariant set = state.getRandomTextureSet(setIdx);
                            if (set == null || set.getTextureCount() == 0) continue;

                            // Loop over rotations
                            for (int rot = 0; rot < rotationCount; rot++) {
                                // Build variant key
                                String variantKey = buildVariantKey(layerCond, stateID, hasStateProperty);

                                // Create model identifier
                                Identifier modelId = state.isCustomModel()
                                    ? createCustomModelId(block, getModelName(modelName, setIdx))
                                    : createGeneratedModelId(block, getModelName(modelName, setIdx));

                                // Create variant JSON
                                JsonObject variant = new JsonObject();
                                variant.addProperty("model", modelId.toString());

                                if (set.getWeight() > 1) {
                                    variant.addProperty("weight", set.getWeight());
                                }

                                if (rot > 0) {
                                    variant.addProperty("y", 90 * rot);
                                }

                                addVariantToKey(variants, variantKey, variant);
                            }
                        }
                    }
                }

                json.add("variants", variants);
                return json;
            }
        });
    }

    private static void generateCropModel(BlockStateModelGenerator generator, Block block, String modelName,
                                         BlockDefinition.StateVariant state, int setIdx, boolean tinted,
                                         int layer, boolean layerSensitive) {
        BlockDefinition.RandomTextureVariant set = state.getRandomTextureSet(setIdx);
        if (set == null || set.getTextureCount() == 0) return;

        String fullModelName = getModelName(modelName, setIdx);
        Identifier modelId = createGeneratedModelId(block, fullModelName);

        // Determine parent model (matches old parent logic)
        String parentBase = tinted ? "westerosblocks:block/tinted/crop" : "westerosblocks:block/untinted/crop";
        String layerSuffix = (layerSensitive && layer != 8) ? "_layer" + layer : "";
        String parent = parentBase + layerSuffix;

        // Create model JSON
        JsonObject modelJson = new JsonObject();
        modelJson.addProperty("parent", parent);

        // Add texture
        JsonObject texturesJson = new JsonObject();
        texturesJson.addProperty("crop", "westerosblocks:block/" + set.getTextureByIndex(0));
        modelJson.add("textures", texturesJson);

        // Upload model
        generator.modelCollector.accept(modelId, () -> modelJson);
    }

    private static String buildVariantKey(String layerCond, String stateID, boolean hasStateProperty) {
        if (layerCond.isEmpty()) {
            // No layer condition
            if (stateID != null && hasStateProperty) {
                return "state=" + stateID;
            }
            return "";
        } else {
            // Has layer condition
            if (stateID != null && hasStateProperty) {
                return layerCond + ",state=" + stateID;
            }
            return layerCond;
        }
    }

    private static void addVariantToKey(JsonObject variants, String key, JsonObject variant) {
        if (variants.has(key)) {

            JsonElement existing = variants.get(key);
            if (existing.isJsonArray()) {
                existing.getAsJsonArray().add(variant);
            } else {

                JsonArray array = new JsonArray();
                array.add(existing);
                array.add(variant);
                variants.add(key, array);
            }
        } else {

            variants.add(key, variant);
        }
    }

}
