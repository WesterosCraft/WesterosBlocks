package com.westerosblocks.datagen.custom;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.BlockStateSupplier;
import net.minecraft.util.Identifier;

public class LogBlockExporter extends BaseBlockExporter {

    public static void registerCustomLogBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        boolean tinted = definition.isTinted() || definition.hasColorMult();
        var states = definition.getStates();

        if (states == null || states.isEmpty()) {
            throw new IllegalStateException("Log block definition states should never be null/empty for block: " + getBlockName(block));
        }

        BlockDefinition.StateVariant state = states.get(0);

        generateBlockState(generator, block, state);

        for (int setIdx = 0; setIdx < state.getRandomTextureSetCount(); setIdx++) {
            generateLogModels(generator, block, state, setIdx, tinted);
        }

        Identifier itemModelId = createGeneratedModelId(block, getModelName("y", 0));
        registerParentedItemModel(generator, block, itemModelId);
    }

    private static void generateBlockState(BlockStateModelGenerator generator, Block block,
                                          BlockDefinition.StateVariant state) {
        String[] axisStates = {"axis=x", "axis=y", "axis=z"};
        int[] xRotations = {90, 0, 90};
        int[] yRotations = {90, 0, 0};
        String[] modelNames = {"x", "y", "z"};

        generator.blockStateCollector.accept(new BlockStateSupplier() {
            @Override
            public Block getBlock() {
                return block;
            }

            @Override
            public JsonElement get() {
                JsonObject json = new JsonObject();
                JsonObject variants = new JsonObject();

                // Loop: axes → texture sets (matches old nested loop exactly)
                for (int i = 0; i < axisStates.length; i++) {
                    String axisState = axisStates[i];
                    String modelName = modelNames[i];
                    int xRot = xRotations[i];
                    int yRot = yRotations[i];

                    // Loop over texture sets
                    for (int setIdx = 0; setIdx < state.getRandomTextureSetCount(); setIdx++) {
                        BlockDefinition.RandomTextureVariant set = state.getRandomTextureSet(setIdx);
                        if (set == null || set.getTextureCount() == 0) continue;

                        Identifier modelId = createGeneratedModelId(block, getModelName(modelName, setIdx));

                        JsonObject variant = new JsonObject();
                        variant.addProperty("model", modelId.toString());

                        if (xRot > 0) {
                            variant.addProperty("x", xRot);
                        }

                        if (yRot > 0) {
                            variant.addProperty("y", yRot);
                        }

                        if (set.getWeight() > 1) {
                            variant.addProperty("weight", set.getWeight());
                        }

                        addVariantToKey(variants, axisState, variant);
                    }
                }

                json.add("variants", variants);
                return json;
            }
        });
    }

    private static void generateLogModels(BlockStateModelGenerator generator, Block block,
                                         BlockDefinition.StateVariant state, int setIdx, boolean tinted) {
        BlockDefinition.RandomTextureVariant set = state.getRandomTextureSet(setIdx);
        if (set == null || set.getTextureCount() == 0) return;

        String down = set.getTextureByIndex(0);
        String up = set.getTextureByIndex(1);
        String north = set.getTextureByIndex(2);
        String south = set.getTextureByIndex(3);
        String west = set.getTextureByIndex(4);
        String east = set.getTextureByIndex(5);
        String particle = north; // Use north as particle

        // Determine parent models
        String parentVertical = tinted ? "westerosblocks:block/tinted/cube_log" : "westerosblocks:block/untinted/cube_log";
        String parentHorizontal = tinted ? "westerosblocks:block/tinted/cube_log_horizontal" : "westerosblocks:block/untinted/cube_log_horizontal";

        // Generate Y-axis model
        generateLogModel(generator, block, "y", setIdx, parentVertical, down, up, north, south, west, east, particle);

        // Generate X-axis model
        generateLogModel(generator, block, "x", setIdx, parentHorizontal, down, up, north, south, west, east, particle);

        // Generate Z-axis model
        generateLogModel(generator, block, "z", setIdx, parentHorizontal, down, up, north, south, west, east, particle);
    }

    private static void generateLogModel(BlockStateModelGenerator generator, Block block, String axis, int setIdx,
                                        String parent, String down, String up, String north, String south,
                                        String west, String east, String particle) {
        String modelName = getModelName(axis, setIdx);
        Identifier modelId = createGeneratedModelId(block, modelName);

        // Create model JSON
        JsonObject modelJson = new JsonObject();
        modelJson.addProperty("parent", parent);

        JsonObject texturesJson = new JsonObject();
        texturesJson.addProperty("down", "westerosblocks:block/" + down);
        texturesJson.addProperty("up", "westerosblocks:block/" + up);
        texturesJson.addProperty("north", "westerosblocks:block/" + north);
        texturesJson.addProperty("south", "westerosblocks:block/" + south);
        texturesJson.addProperty("west", "westerosblocks:block/" + west);
        texturesJson.addProperty("east", "westerosblocks:block/" + east);
        texturesJson.addProperty("particle", "westerosblocks:block/" + particle);
        modelJson.add("textures", texturesJson);

        generator.modelCollector.accept(modelId, () -> modelJson);
    }

    private static void addVariantToKey(JsonObject variants, String key, JsonObject variant) {
        if (variants.has(key)) {
            // Key exists - convert to array or add to existing array
            JsonElement existing = variants.get(key);
            if (existing.isJsonArray()) {
                existing.getAsJsonArray().add(variant);
            } else {
                // Convert single variant to array
                JsonArray array = new JsonArray();
                array.add(existing);
                array.add(variant);
                variants.add(key, array);
            }
        } else {
            // First variant for this key
            variants.add(key, variant);
        }
    }

    private static Identifier createGeneratedModelId(Block block, String variant) {
        String blockName = getBlockName(block);
        return WesterosBlocks.id("block/" + blockName + "/" + variant);
    }

    private static String getModelName(String axis, int setIdx) {
        return axis + "_v" + (setIdx + 1);
    }
}
