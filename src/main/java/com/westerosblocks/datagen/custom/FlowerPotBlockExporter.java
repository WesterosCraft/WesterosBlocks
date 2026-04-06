package com.westerosblocks.datagen.custom;

import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.datagen.ModModels;
import com.westerosblocks.datagen.ModTextureKey;
import net.minecraft.client.data.*;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class FlowerPotBlockExporter extends BaseBlockExporter {
    private static Model getFlowerPotModel(boolean isEmpty, boolean tinted) {
        if (isEmpty) {
            return tinted ? ModModels.FLOWERPOT_EMPTY_TINTED : ModModels.FLOWERPOT_EMPTY_UNTINTED;
        }
        return tinted ? ModModels.FLOWERPOT_FILLED_TINTED : ModModels.FLOWERPOT_FILLED_UNTINTED;
    }

    private static TextureMap createFlowerPotTextureMap(String[] textures, boolean isEmpty) {
        TextureMap textureMap = new TextureMap();

        if (isEmpty) {
            // Empty pot: dirt[0], flowerpot[1]
            textureMap.put(ModTextureKey.DIRT, createBlockIdentifier(textures[0]));
            textureMap.put(ModTextureKey.FLOWERPOT, createBlockIdentifier(textures[1]));
            textureMap.put(TextureKey.PARTICLE, createBlockIdentifier(textures[1]));
        } else {
            // Filled pot: dirt[0], flowerpot[1], plant[2]
            textureMap.put(ModTextureKey.DIRT, createBlockIdentifier(textures[0]));
            textureMap.put(ModTextureKey.FLOWERPOT, createBlockIdentifier(textures[1]));
            textureMap.put(ModTextureKey.PLANT, createBlockIdentifier(textures[2]));
            textureMap.put(TextureKey.PARTICLE, createBlockIdentifier(textures[1]));
        }

        return textureMap;
    }

    public static void registerFlowerPotBlock(BlockStateModelGenerator generator, Block block, boolean tinted,
                                             boolean rotateRandom, String[] textures) {
        boolean isEmpty = textures.length == 2;
        TextureMap textureMap = createFlowerPotTextureMap(textures, isEmpty);

        Identifier modelId = getFlowerPotModel(isEmpty, tinted)
                .upload(createNestedModelId(block, "base"), textureMap, generator.modelCollector);

        VariantsBlockModelDefinitionCreator blockstate = createRotatedVariantsBlockState(block, List.of(modelId), List.of(1), rotateRandom);
        generator.blockStateCollector.accept(blockstate);
        generator.registerParentedItemModel(block, modelId);
    }

    public static void registerFlowerPotBlockWithRandomTextures(BlockStateModelGenerator generator, Block block,
                                                               boolean tinted, boolean rotateRandom, List<BlockDefinition.TextureVariantSet> textureSets) {
        List<Identifier> modelIds = new ArrayList<>();
        List<Integer> weights = new ArrayList<>();

        for (int i = 0; i < textureSets.size(); i++) {
            BlockDefinition.TextureVariantSet set = textureSets.get(i);
            String[] textures = set.getTexturesAsArray();
            boolean isEmpty = textures.length == 2;
            TextureMap textureMap = createFlowerPotTextureMap(textures, isEmpty);

            Identifier modelId = getFlowerPotModel(isEmpty, tinted)
                    .upload(createNestedModelId(block, "base_v" + (i + 1)), textureMap, generator.modelCollector);

            modelIds.add(modelId);
            weights.add(set.weight);
        }

        VariantsBlockModelDefinitionCreator blockstate = createRotatedVariantsBlockState(block, modelIds, weights, rotateRandom);
        generator.blockStateCollector.accept(blockstate);
        generator.registerParentedItemModel(block, modelIds.get(0));
    }

    public static void registerCustomFlowerPotBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        boolean tinted = definition.isTinted() || definition.hasColorMult();
        boolean rotateRandom = true; // Always rotate flower pots

        if (definition.hasRandomTextures()) {
            registerFlowerPotBlockWithRandomTextures(generator, block, tinted, rotateRandom, extractTextureVariantSets(definition));
        } else if (definition.getTextures() != null && !definition.getTextures().isEmpty()) {
            String[] textures = definition.getTextures().toArray(new String[0]);
            registerFlowerPotBlock(generator, block, tinted, rotateRandom, textures);
        } else {
            throw new IllegalArgumentException("Flower pot blocks require either textures or randomTextures");
        }
    }
}
