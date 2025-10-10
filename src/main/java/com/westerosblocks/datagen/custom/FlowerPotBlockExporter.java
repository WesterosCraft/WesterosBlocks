package com.westerosblocks.datagen.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.datagen.ModTextureKey;
import net.minecraft.data.client.*;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FlowerPotBlockExporter extends BaseBlockExporter {
    private static Model createFlowerPotModel(boolean isEmpty, boolean tinted) {
        String tintPath = tinted ? "block/tinted/" : "block/untinted/";
        String potType = isEmpty ? "flower_pot" : "flower_pot_cross";
        String path = tintPath + potType;

        if (isEmpty) {
            // Empty pot: dirt, flowerpot textures
            return new Model(Optional.of(WesterosBlocks.id(path)), Optional.empty(),
                ModTextureKey.DIRT, ModTextureKey.FLOWERPOT, TextureKey.PARTICLE);
        } else {
            // Filled pot: dirt, flowerpot, plant textures
            return new Model(Optional.of(WesterosBlocks.id(path)), Optional.empty(),
                ModTextureKey.DIRT, ModTextureKey.FLOWERPOT, ModTextureKey.PLANT, TextureKey.PARTICLE);
        }
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

    private static VariantsBlockStateSupplier createFlowerPotBlockstate(Block block, List<Identifier> modelIds,
                                                                        boolean rotateRandom, List<Integer> weights) {
        List<BlockStateVariant> variants = new ArrayList<>();
        int rotationCount = rotateRandom ? 4 : 1;

        for (int i = 0; i < modelIds.size(); i++) {
            for (int rotation = 0; rotation < rotationCount; rotation++) {
                BlockStateVariant variant = BlockStateVariant.create()
                        .put(VariantSettings.MODEL, modelIds.get(i));

                if (weights != null && weights.get(i) > 1) {
                    variant = variant.put(VariantSettings.WEIGHT, weights.get(i));
                }

                if (rotation > 0) {
                    variant = variant.put(VariantSettings.Y, VariantSettings.Rotation.valueOf("R" + (90 * rotation)));
                }

                variants.add(variant);
            }
        }

        if (variants.size() == 1) {
            return VariantsBlockStateSupplier.create(block, variants.get(0));
        } else {
            return VariantsBlockStateSupplier.create(block, variants.toArray(new BlockStateVariant[0]));
        }
    }

    public static void registerFlowerPotBlock(BlockStateModelGenerator generator, Block block, boolean tinted,
                                             boolean rotateRandom, String[] textures) {
        boolean isEmpty = textures.length == 2;
        TextureMap textureMap = createFlowerPotTextureMap(textures, isEmpty);

        Identifier modelId = createFlowerPotModel(isEmpty, tinted)
                .upload(createNestedModelId(block, "base"), textureMap, generator.modelCollector);

        VariantsBlockStateSupplier blockstate = createFlowerPotBlockstate(block, List.of(modelId), rotateRandom, List.of(1));
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

            Identifier modelId = createFlowerPotModel(isEmpty, tinted)
                    .upload(createNestedModelId(block, "base_v" + (i + 1)), textureMap, generator.modelCollector);

            modelIds.add(modelId);
            weights.add(set.weight);
        }

        VariantsBlockStateSupplier blockstate = createFlowerPotBlockstate(block, modelIds, rotateRandom, weights);
        generator.blockStateCollector.accept(blockstate);
        generator.registerParentedItemModel(block, modelIds.get(0));
    }

    public static void registerCustomFlowerPotBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        boolean tinted = definition.isTinted() || definition.hasColorMult();
        boolean rotateRandom = true; // Always rotate flower pots

        if (definition.hasRandomTextures()) {
            List<BlockDefinition.TextureVariantSet> textureSets = new ArrayList<>();
            for (BlockDefinition.RandomTextureVariant randomTexture : definition.getRandomTextures()) {
                String[] textures = randomTexture.getTextures().toArray(new String[0]);
                int weight = randomTexture.getWeight();
                textureSets.add(new BlockDefinition.TextureVariantSet(textures, weight, null));
            }
            registerFlowerPotBlockWithRandomTextures(generator, block, tinted, rotateRandom, textureSets);
        } else if (definition.getTextures() != null && !definition.getTextures().isEmpty()) {
            String[] textures = definition.getTextures().toArray(new String[0]);
            registerFlowerPotBlock(generator, block, tinted, rotateRandom, textures);
        } else {
            throw new IllegalArgumentException("Flower pot blocks require either textures or randomTextures");
        }
    }
}
