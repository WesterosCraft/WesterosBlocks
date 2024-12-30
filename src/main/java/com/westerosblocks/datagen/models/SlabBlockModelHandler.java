package com.westerosblocks.datagen.models;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.WesterosBlockStateRecord;
import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.enums.SlabType;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;

import java.util.*;

public class SlabBlockModelHandler {
    private static final String GENERATED_PATH = "block/generated/";
    private static final String BLOCK_PATH = "block/";

    public static void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator, Block currentBlock, WesterosBlockDef blockDefinition) {
        List<BlockStateVariant> bottomVariants = new ArrayList<>();
        List<BlockStateVariant> topVariants = new ArrayList<>();
        List<BlockStateVariant> doubleVariants = new ArrayList<>();

        for (WesterosBlockStateRecord state : blockDefinition.states) {
            String baseName = state.stateID == null ? "base" : state.stateID;

            for (int setIdx = 0; setIdx < state.getRandomTextureSetCount(); setIdx++) {
                WesterosBlockDef.RandomTextureSet textureSet = state.getRandomTextureSet(setIdx);
                generateSlabVariants(blockStateModelGenerator, blockDefinition, baseName, setIdx, textureSet,
                        bottomVariants, topVariants, doubleVariants);
            }
        }

        // Create a single blockstate definition for the block
        blockStateModelGenerator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(currentBlock)
                        .coordinate(BlockStateVariantMap.create(Properties.SLAB_TYPE)
                                .register(net.minecraft.block.enums.SlabType.BOTTOM, bottomVariants)
                                .register(net.minecraft.block.enums.SlabType.TOP, topVariants)
                                .register(net.minecraft.block.enums.SlabType.DOUBLE, doubleVariants))
        );
    }

    private static void generateSlabVariants(
            BlockStateModelGenerator blockStateModelGenerator,
            WesterosBlockDef blockDefinition,
            String baseName,
            int setIdx,
            WesterosBlockDef.RandomTextureSet textureSet,
            List<BlockStateVariant> bottomVariants,
            List<BlockStateVariant> topVariants,
            List<BlockStateVariant> doubleVariants
    ) {
        // Generate models
        String baseModelPath = baseName == "base" ?
                String.format("%s%s", GENERATED_PATH, blockDefinition.blockName) :
                String.format("%s%s/%s", GENERATED_PATH, blockDefinition.blockName, baseName);

        // Bottom variant
        BlockStateVariant bottomVariant = BlockStateVariant.create()
                .put(VariantSettings.MODEL, Identifier.of(WesterosBlocks.MOD_ID, baseModelPath + "/bottom_v" + (setIdx + 1)));
        if (textureSet.weight != null && textureSet.weight > 0) {
            bottomVariant.put(VariantSettings.WEIGHT, textureSet.weight);
        }
        bottomVariants.add(bottomVariant);

        // Top variant
        BlockStateVariant topVariant = BlockStateVariant.create()
                .put(VariantSettings.MODEL, Identifier.of(WesterosBlocks.MOD_ID, baseModelPath + "/top_v" + (setIdx + 1)));
        if (textureSet.weight != null && textureSet.weight > 0) {
            topVariant.put(VariantSettings.WEIGHT, textureSet.weight);
        }
        topVariants.add(topVariant);

        // Double variant
        BlockStateVariant doubleVariant = BlockStateVariant.create()
                .put(VariantSettings.MODEL, Identifier.of(WesterosBlocks.MOD_ID, baseModelPath + "/double_v" + (setIdx + 1)));
        if (textureSet.weight != null && textureSet.weight > 0) {
            doubleVariant.put(VariantSettings.WEIGHT, textureSet.weight);
        }
        doubleVariants.add(doubleVariant);

        // Generate the models
        TextureMap textureMap = createTextureMap(textureSet);
        Models.SLAB.upload(Identifier.of(WesterosBlocks.MOD_ID, baseModelPath + "/bottom_v" + (setIdx + 1)), textureMap, blockStateModelGenerator.modelCollector);
        Models.SLAB_TOP.upload(Identifier.of(WesterosBlocks.MOD_ID, baseModelPath + "/top_v" + (setIdx + 1)), textureMap, blockStateModelGenerator.modelCollector);
        Models.CUBE_ALL.upload(Identifier.of(WesterosBlocks.MOD_ID, baseModelPath + "/double_v" + (setIdx + 1)), textureMap, blockStateModelGenerator.modelCollector);
    }

    private static TextureMap createTextureMap(WesterosBlockDef.RandomTextureSet textureSet) {
        return new TextureMap()
                .put(TextureKey.ALL, Identifier.of(WesterosBlocks.MOD_ID,
                        BLOCK_PATH + textureSet.getTextureByIndex(0)))
                .put(TextureKey.TOP, Identifier.of(WesterosBlocks.MOD_ID,
                        BLOCK_PATH + textureSet.getTextureByIndex(1)))
                .put(TextureKey.BOTTOM, Identifier.of(WesterosBlocks.MOD_ID,
                        BLOCK_PATH + textureSet.getTextureByIndex(0)))
                .put(TextureKey.SIDE, Identifier.of(WesterosBlocks.MOD_ID,
                        BLOCK_PATH + textureSet.getTextureByIndex(2)));
    }

    public static void generateItemModels(ItemModelGenerator itemModelGenerator, Block currentBlock, WesterosBlockDef blockDefinition) {
        WesterosBlockStateRecord firstState = blockDefinition.states.getFirst();
        String baseName = firstState.stateID == null ? "base" : firstState.stateID;

        String path = baseName == "base" ?
                String.format("%s%s/bottom_v1", GENERATED_PATH, blockDefinition.blockName) :
                String.format("%s%s/%s/bottom_v1", GENERATED_PATH, blockDefinition.blockName, baseName);

        itemModelGenerator.register(
                currentBlock.asItem(),
                new Model(Optional.of(Identifier.of(WesterosBlocks.MOD_ID, path)), Optional.empty())
        );
    }
}