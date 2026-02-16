package com.westerosblocks.datagen.custom;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.BlockStateSupplier;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class MountedSlabBlockExporter extends BaseBlockExporter {

    private static final int[] ROTATIONS = {0, 90, 180, 270};
    private static final String[] DIRECTIONS = {"north", "east", "south", "west"};

    public static void registerMountedSlabBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        // Determine variant count from randomTextures (or default to 1)
        int variantCount = 1;
        List<Integer> weights = new ArrayList<>();

        if (definition.hasRandomTextures()) {
            variantCount = definition.getRandomTextures().size();
            for (BlockDefinition.RandomTextureVariant rtv : definition.getRandomTextures()) {
                weights.add(rtv.getWeight());
            }
        } else if (definition.hasStates() && definition.getStates().get(0).hasRandomTextures()) {
            variantCount = definition.getStates().get(0).getRandomTextures().size();
            for (BlockDefinition.RandomTextureVariant rtv : definition.getStates().get(0).getRandomTextures()) {
                weights.add(rtv.getWeight());
            }
        }

        if (weights.isEmpty()) {
            for (int i = 0; i < variantCount; i++) {
                weights.add(1);
            }
        }

        // Build model ID lists
        List<Identifier> topModels = new ArrayList<>();
        List<Identifier> bottomModels = new ArrayList<>();
        for (int i = 0; i < variantCount; i++) {
            topModels.add(createCustomModelId(block, "top_v" + (i + 1)));
            bottomModels.add(createCustomModelId(block, "bottom_v" + (i + 1)));
        }

        // Generate blockstate
        final int vc = variantCount;
        final List<Integer> w = weights;

        generator.blockStateCollector.accept(new BlockStateSupplier() {
            @Override
            public Block getBlock() {
                return block;
            }

            @Override
            public JsonElement get() {
                JsonObject json = new JsonObject();
                JsonObject variants = new JsonObject();

                for (int d = 0; d < 4; d++) {
                    String dir = DIRECTIONS[d];
                    int rot = ROTATIONS[d];

                    String bottomKey = "facing=" + dir + ",half=bottom";
                    String topKey = "facing=" + dir + ",half=top";

                    if (vc == 1) {
                        // Single variant — simple object
                        variants.add(bottomKey, createVariantJson(bottomModels.get(0), rot));
                        variants.add(topKey, createVariantJson(topModels.get(0), rot));
                    } else {
                        // Multiple variants — array with weights
                        JsonArray bottomArr = new JsonArray();
                        JsonArray topArr = new JsonArray();
                        for (int i = 0; i < vc; i++) {
                            JsonObject bv = createVariantJson(bottomModels.get(i), rot).getAsJsonObject();
                            JsonObject tv = createVariantJson(topModels.get(i), rot).getAsJsonObject();
                            int weight = w.get(i);
                            if (weight > 1) {
                                bv.addProperty("weight", weight);
                                tv.addProperty("weight", weight);
                            }
                            bottomArr.add(bv);
                            topArr.add(tv);
                        }
                        variants.add(bottomKey, bottomArr);
                        variants.add(topKey, topArr);
                    }
                }

                json.add("variants", variants);
                return json;
            }
        });

        // Item model parented to first bottom variant
        registerParentedItemModel(generator, block, bottomModels.get(0));
    }

    private static JsonElement createVariantJson(Identifier modelId, int rotation) {
        JsonObject obj = new JsonObject();
        obj.addProperty("model", modelId.toString());
        if (rotation > 0) {
            obj.addProperty("y", rotation);
        }
        return obj;
    }
}
