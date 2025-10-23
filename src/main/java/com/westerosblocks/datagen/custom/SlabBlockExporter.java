package com.westerosblocks.datagen.custom;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.datagen.ModModels;
import com.westerosblocks.datagen.ModTextureMap;
import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.Block;
import net.minecraft.block.enums.SlabType;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class SlabBlockExporter extends BaseBlockExporter {

    /**
     * Registers a slab block from a BlockDefinition.
     * Automatically handles textures, randomTextures, and waterlogged states.
     *
     * Uses uniform iteration pattern: After doInit(), states is ALWAYS non-empty,
     * and each state has randomTextures normalized from simple textures.
     */
    public static void registerCustomSlabBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        // After doInit(), states is ALWAYS non-empty (at least synthetic base state exists)
        var states = definition.getStates();

        if (states == null || states.isEmpty()) {
            throw new IllegalStateException("Block definition states should never be null/empty after doInit() for block: " + getBlockName(block));
        }

        // For now, use the first state (multi-state slab blocks can be handled later if needed)
        BlockDefinition.StateVariant state = states.get(0);

        // Check for custom model first
        if (definition.hasCustomModel() || state.isCustomModel()) {
            registerCustomModelSlabBlock(generator, block, definition);
            return;
        }

        // Check if we have texture sets to work with
        int textureSetCount = state.getRandomTextureSetCount();

        if (textureSetCount == 0) {
            // No texture sets at all - fallback
            registerFallbackSlabBlock(generator, block, definition);
            return;
        }

        if (textureSetCount == 1) {
            // Single texture set
            BlockDefinition.RandomTextureVariant set = state.getRandomTextureSet(0);
            if (set == null || set.getTextureCount() == 0) {
                registerFallbackSlabBlock(generator, block, definition);
                return;
            }

            // Extract textures from the single set
            String[] textures = new String[set.getTextureCount()];
            for (int i = 0; i < set.getTextureCount(); i++) {
                textures[i] = set.getTextureByIndex(i);
            }

            registerSimpleSlabBlockWithTextures(generator, block, textures);
        } else {
            // Multiple random texture sets
            registerSlabBlockWithMultipleTextureSets(generator, block, state);
        }
    }

    /**
     * Registers a simple slab block with basic textures.
     */
    private static void registerSimpleSlabBlockWithTextures(BlockStateModelGenerator generator, Block block, String[] textures) {
        String[] filledTextures = fillTextureArray(textures);
        TextureMap textureMap = ModTextureMap.customAllSides(filledTextures);

        // Upload models for all three slab variants
        Identifier bottomModelId = ModModels.SLAB_BOTTOM.upload(createNestedModelId(block, getBlockName(block) + "_bottom"), textureMap, generator.modelCollector);
        Identifier topModelId = ModModels.SLAB_TOP.upload(createNestedModelId(block, getBlockName(block) + "_top"), textureMap, generator.modelCollector);
        Identifier fullModelId = Models.CUBE.upload(createNestedModelId(block, getBlockName(block) + "_double"), textureMap, generator.modelCollector);

        // Create blockstate with slab type and waterlogged variants
        generator.blockStateCollector.accept(createSlabBlockState(block, bottomModelId, topModelId, fullModelId));
        registerParentedItemModel(generator, block, bottomModelId);
    }

    /**
     * Registers a slab block with multiple random texture sets from a state.
     */
    private static void registerSlabBlockWithMultipleTextureSets(BlockStateModelGenerator generator, Block block, BlockDefinition.StateVariant state) {
        List<SlabModelSet> modelSets = new ArrayList<>();

        for (int i = 0; i < state.getRandomTextureSetCount(); i++) {
            BlockDefinition.RandomTextureVariant set = state.getRandomTextureSet(i);
            if (set == null || set.getTextureCount() == 0) {
                continue;
            }

            // Extract textures from this set
            String[] textures = new String[set.getTextureCount()];
            for (int j = 0; j < set.getTextureCount(); j++) {
                textures[j] = set.getTextureByIndex(j);
            }

            String[] filledTextures = fillTextureArray(textures);
            TextureMap textureMap = ModTextureMap.customAllSides(filledTextures);

            // Upload models for this variant
            String variantSuffix = "_v" + (i + 1);
            Identifier bottomModelId = ModModels.SLAB_BOTTOM.upload(createNestedModelId(block, getBlockName(block) + "_bottom" + variantSuffix), textureMap, generator.modelCollector);
            Identifier topModelId = ModModels.SLAB_TOP.upload(createNestedModelId(block, getBlockName(block) + "_top" + variantSuffix), textureMap, generator.modelCollector);
            Identifier fullModelId = Models.CUBE.upload(createNestedModelId(block, getBlockName(block) + "_double" + variantSuffix), textureMap, generator.modelCollector);

            modelSets.add(new SlabModelSet(bottomModelId, topModelId, fullModelId, set.getWeight()));
        }

        if (modelSets.isEmpty()) {
            // Fallback if no valid texture sets found
            registerFallbackSlabBlock(generator, block, null);
            return;
        }

        // Create blockstate with weighted random variants
        generator.blockStateCollector.accept(createSlabBlockStateWithRandomTextures(block, modelSets));
        registerParentedItemModel(generator, block, modelSets.get(0).bottom);
    }

    /**
     * Registers a slab block with custom model references.
     */
    private static void registerCustomModelSlabBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        Identifier bottomModelId = createCustomModelId(block, "bottom_v1");
        Identifier topModelId = createCustomModelId(block, "top_v1");
        Identifier fullModelId = createCustomModelId(block, "double_v1");

        generator.blockStateCollector.accept(createSlabBlockState(block, bottomModelId, topModelId, fullModelId));
        registerParentedItemModel(generator, block, bottomModelId);
    }

    /**
     * Fallback registration for slab blocks with no textures defined.
     */
    private static void registerFallbackSlabBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        String[] fallbackTextures = new String[]{"missing"};
        String[] filledTextures = fillTextureArray(fallbackTextures);
        TextureMap textureMap = ModTextureMap.customAllSides(filledTextures);

        Identifier bottomModelId = ModModels.SLAB_BOTTOM.upload(createNestedModelId(block, getBlockName(block) + "_bottom"), textureMap, generator.modelCollector);
        Identifier topModelId = ModModels.SLAB_TOP.upload(createNestedModelId(block, getBlockName(block) + "_top"), textureMap, generator.modelCollector);
        Identifier fullModelId = Models.CUBE.upload(createNestedModelId(block, getBlockName(block) + "_double"), textureMap, generator.modelCollector);

        generator.blockStateCollector.accept(createSlabBlockState(block, bottomModelId, topModelId, fullModelId));
        registerParentedItemModel(generator, block, bottomModelId);
    }

    /**
     * Creates a blockstate for slab blocks.
     */
    private static BlockStateSupplier createSlabBlockState(Block block, Identifier bottomModel, Identifier topModel, Identifier fullModel) {
        return new BlockStateSupplier() {
            @Override
            public Block getBlock() {
                return block;
            }

            @Override
            public JsonElement get() {
                JsonObject json = new JsonObject();
                JsonObject variants = new JsonObject();

                // For each slab type
                for (SlabType type : SlabType.values()) {
                    String variantKey = "type=" + type.asString();
                    JsonObject variant = new JsonObject();

                    Identifier modelId = switch (type) {
                        case BOTTOM -> bottomModel;
                        case TOP -> topModel;
                        case DOUBLE -> fullModel;
                    };

                    variant.addProperty("model", modelId.toString());
                    variants.add(variantKey, variant);
                }

                json.add("variants", variants);
                return json;
            }
        };
    }

    /**
     * Creates a blockstate for slab blocks with random textures.
     */
    private static BlockStateSupplier createSlabBlockStateWithRandomTextures(Block block, List<SlabModelSet> modelSets) {
        return new BlockStateSupplier() {
            @Override
            public Block getBlock() {
                return block;
            }

            @Override
            public JsonElement get() {
                JsonObject json = new JsonObject();
                JsonObject variants = new JsonObject();

                // For each slab type
                for (SlabType type : SlabType.values()) {
                    String variantKey = "type=" + type.asString();

                    if (modelSets.size() == 1) {
                        // Single variant
                        JsonObject variant = new JsonObject();
                        Identifier modelId = getModelForType(modelSets.get(0), type);
                        variant.addProperty("model", modelId.toString());
                        variants.add(variantKey, variant);
                    } else {
                        // Multiple weighted variants
                        JsonArray variantArray = new JsonArray();
                        for (SlabModelSet modelSet : modelSets) {
                            JsonObject variant = new JsonObject();
                            Identifier modelId = getModelForType(modelSet, type);
                            variant.addProperty("model", modelId.toString());
                            if (modelSet.weight > 1) {
                                variant.addProperty("weight", modelSet.weight);
                            }
                            variantArray.add(variant);
                        }
                        variants.add(variantKey, variantArray);
                    }
                }

                json.add("variants", variants);
                return json;
            }
        };
    }

    /**
     * Gets the appropriate model ID for a slab type.
     */
    private static Identifier getModelForType(SlabModelSet modelSet, SlabType type) {
        return switch (type) {
            case BOTTOM -> modelSet.bottom;
            case TOP -> modelSet.top;
            case DOUBLE -> modelSet.full;
        };
    }

    private static Identifier createCustomModelId(Block block, String variant) {
        String blockName = getBlockName(block);
        return WesterosBlocks.id("block/custom/" + blockName + "/" + variant);
    }

    /**
     * Helper class to hold a set of slab models with weight.
     */
    private static class SlabModelSet {
        final Identifier bottom;
        final Identifier top;
        final Identifier full;
        final int weight;

        SlabModelSet(Identifier bottom, Identifier top, Identifier full, int weight) {
            this.bottom = bottom;
            this.top = top;
            this.full = full;
            this.weight = weight;
        }
    }
}
