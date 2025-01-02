package com.westerosblocks.datagen.models;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.WesterosBlockStateRecord;
import net.minecraft.block.Block;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.block.enums.StairShape;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class StairBlockModelHandler {
    private static final String GENERATED_PATH = "block/generated/";
    private static final String BLOCK_PATH = "block/";

    public static void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator, Block currentBlock, WesterosBlockDef blockDefinition) {
        if (!currentBlock.getStateManager().getProperties().contains(Properties.HORIZONTAL_FACING) ||
                !currentBlock.getStateManager().getProperties().contains(Properties.BLOCK_HALF) ||
                !currentBlock.getStateManager().getProperties().contains(Properties.STAIR_SHAPE)) {
            WesterosBlocks.LOGGER.error("Block {} is missing required stair properties", blockDefinition.blockName);
            return;
        }

        Map<String, BlockStateVariant> variants = new HashMap<>();

        for (WesterosBlockStateRecord state : blockDefinition.states) {
            String baseName = state.stateID == null ? "base" : state.stateID;
            processStateVariants(blockStateModelGenerator, blockDefinition, state, baseName, variants);
        }

        blockStateModelGenerator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(currentBlock)
                        .coordinate(BlockStateVariantMap.create(Properties.HORIZONTAL_FACING, Properties.BLOCK_HALF, Properties.STAIR_SHAPE)
                                .register((facing, half, shape) -> {
                                    String key = String.format("facing=%s,half=%s,shape=%s",
                                            facing.getName(), half.toString().toLowerCase(), shape.toString().toLowerCase());
                                    return variants.get(key);
                                })));
    }

    private static void processStateVariants(
            BlockStateModelGenerator blockStateModelGenerator,
            WesterosBlockDef blockDefinition,
            WesterosBlockStateRecord state,
            String baseName,
            Map<String, BlockStateVariant> variants
    ) {
        for (int setIdx = 0; setIdx < state.getRandomTextureSetCount(); setIdx++) {
            WesterosBlockDef.RandomTextureSet textureSet = state.getRandomTextureSet(setIdx);
            generateStairModels(blockStateModelGenerator, blockDefinition, baseName, setIdx, textureSet, state);
            addStairStateVariants(variants, blockDefinition, setIdx, textureSet);
        }
    }

    private static final java.util.Set<String> generatedModels = new java.util.HashSet<>();

    private static void generateStairModels(
            BlockStateModelGenerator blockStateModelGenerator,
            WesterosBlockDef blockDefinition,
            String baseName,
            int setIdx,
            WesterosBlockDef.RandomTextureSet textureSet,
            WesterosBlockStateRecord stateRecord
    ) {
        // Skip if we've already generated these models
        String modelKey = blockDefinition.blockName + "_" + setIdx;
        if (!generatedModels.add(modelKey)) {
            return;
        }

        boolean isTinted = stateRecord.isTinted();
        boolean hasOverlay = stateRecord.getOverlayTextureByIndex(0) != null;
        boolean hasAmbientOcclusion = blockDefinition.ambientOcclusion != null ?
                blockDefinition.ambientOcclusion : true;

        // Determine parent model path
        String parentModelBase = "westerosblocks:block/";
        String parentModel = parentModelBase +
                (hasAmbientOcclusion ?
                        (isTinted ?
                                (hasOverlay ? "tinted/stairs_overlay" : "tinted/stairs") :
                                (hasOverlay ? "untinted/stairs_overlay" : "untinted/stairs")) :
                        (isTinted ?
                                (hasOverlay ? "tintednoocclusion/stairs_overlay" : "tintednoocclusion/stairs") :
                                (hasOverlay ? "noocclusion/stairs_overlay" : "noocclusion/stairs")));

        // Generate base stair model
        TextureMap baseTextures = createTextureMap(textureSet);
        Models.STAIRS.upload(
                createModelIdentifier(blockDefinition.blockName, "base", setIdx),
                baseTextures,
                blockStateModelGenerator.modelCollector
        );

        // Generate inner stair model
        TextureMap innerTextures = createTextureMap(textureSet);
        Models.INNER_STAIRS.upload(
                createModelIdentifier(blockDefinition.blockName, "inner", setIdx),
                innerTextures,
                blockStateModelGenerator.modelCollector
        );

        // Generate outer stair model
        TextureMap outerTextures = createTextureMap(textureSet);
        Models.OUTER_STAIRS.upload(
                createModelIdentifier(blockDefinition.blockName, "outer", setIdx),
                outerTextures,
                blockStateModelGenerator.modelCollector
        );
    }

    private static void addStairStateVariants(
            Map<String, BlockStateVariant> variants,
            WesterosBlockDef blockDefinition,
            int setIdx,
            WesterosBlockDef.RandomTextureSet textureSet
    ) {
        for (Direction facing : Direction.Type.HORIZONTAL) {
            for (BlockHalf half : BlockHalf.values()) {
                for (StairShape shape : StairShape.values()) {
                    BlockStateVariant variant = BlockStateVariant.create()
                            .put(VariantSettings.UVLOCK, true);

                    // Add model
                    Identifier modelId = switch (shape) {
                        case STRAIGHT -> createModelIdentifier(blockDefinition.blockName, "base", setIdx);
                        case INNER_LEFT, INNER_RIGHT -> createModelIdentifier(blockDefinition.blockName, "inner", setIdx);
                        case OUTER_LEFT, OUTER_RIGHT -> createModelIdentifier(blockDefinition.blockName, "outer", setIdx);
                    };
                    variant.put(VariantSettings.MODEL, modelId);

                    // Add Y rotation
                    switch (shape) {
                        case STRAIGHT, INNER_RIGHT, OUTER_RIGHT -> {
                            switch (facing) {
                                case EAST -> {}  // No rotation needed
                                case WEST -> variant.put(VariantSettings.Y, VariantSettings.Rotation.R180);
                                case SOUTH -> variant.put(VariantSettings.Y, VariantSettings.Rotation.R90);
                                case NORTH -> variant.put(VariantSettings.Y, VariantSettings.Rotation.R270);
                            }
                        }
                        case INNER_LEFT -> {
                            switch (facing) {
                                case EAST -> variant.put(VariantSettings.Y, VariantSettings.Rotation.R270);
                                case WEST -> variant.put(VariantSettings.Y, VariantSettings.Rotation.R90);
                                case SOUTH -> variant.put(VariantSettings.Y, VariantSettings.Rotation.R0);
                                case NORTH -> variant.put(VariantSettings.Y, VariantSettings.Rotation.R180);
                            }
                        }
                        case OUTER_LEFT -> {
                            switch (facing) {
                                case EAST -> variant.put(VariantSettings.Y, VariantSettings.Rotation.R270);
                                case WEST -> variant.put(VariantSettings.Y, VariantSettings.Rotation.R90);
                                case SOUTH -> {}  // No rotation needed
                                case NORTH -> variant.put(VariantSettings.Y, VariantSettings.Rotation.R180);
                            }
                        }
                    }

                    // Add X rotation for top half
                    if (half == BlockHalf.TOP) {
                        variant.put(VariantSettings.X, VariantSettings.Rotation.R180);
                    }

                    String variantKey = String.format("facing=%s,half=%s,shape=%s",
                            facing.getName(),
                            half.toString().toLowerCase(),
                            shape.toString().toLowerCase());
                    variants.put(variantKey, variant);
                }
            }
        }
    }

    private static Identifier createModelIdentifier(String blockName, String type, int setIdx) {
        return Identifier.of(WesterosBlocks.MOD_ID,
                String.format("%s%s/%s_v%d", GENERATED_PATH, blockName, type, setIdx + 1));
    }

    private static TextureMap createTextureMap(WesterosBlockDef.RandomTextureSet textureSet) {
        return new TextureMap()
                .put(TextureKey.BOTTOM, Identifier.of(WesterosBlocks.MOD_ID,
                        BLOCK_PATH + textureSet.getTextureByIndex(0)))
                .put(TextureKey.TOP, Identifier.of(WesterosBlocks.MOD_ID,
                        BLOCK_PATH + textureSet.getTextureByIndex(1)))
                .put(TextureKey.SIDE, Identifier.of(WesterosBlocks.MOD_ID,
                        BLOCK_PATH + textureSet.getTextureByIndex(2)));
    }

    private static final java.util.Set<String> registeredItems = new java.util.HashSet<>();

    public static void generateItemModels(ItemModelGenerator itemModelGenerator, Block currentBlock, WesterosBlockDef blockDefinition) {
        // Skip if we've already registered this item model
        if (!registeredItems.add(blockDefinition.blockName)) {
            return;
        }

        WesterosBlockStateRecord firstState = blockDefinition.states.getFirst();
        String baseName = firstState.stateID == null ? "base" : firstState.stateID;

        String path = String.format("%s%s/base_v1", GENERATED_PATH, blockDefinition.blockName);

        itemModelGenerator.register(
                currentBlock.asItem(),
                new Model(Optional.of(Identifier.of(WesterosBlocks.MOD_ID, path)), Optional.empty())
        );
    }
}