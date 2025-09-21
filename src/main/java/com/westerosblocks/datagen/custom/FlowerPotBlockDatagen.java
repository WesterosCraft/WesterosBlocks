package com.westerosblocks.datagen.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.datagen.ModTextureKey;
import net.minecraft.data.client.*;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FlowerPotBlockDatagen {

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

    // Builder pattern for flower pot block generation
    public static class FlowerPotBlockBuilder {
        private final BlockStateModelGenerator generator;
        private final Block flowerPotBlock;
        private final String potName;
        private boolean isTinted = false;
        private boolean rotateRandom = false;
        private final List<RandomTextureSet> randomTextureSets = new ArrayList<>();
        private final List<String> simpleTextures = new ArrayList<>();

        // Inner class to hold random texture set information
        public static class RandomTextureSet {
            public final String[] textures;
            public final int weight;

            public RandomTextureSet(int weight, String... textures) {
                this.weight = weight;
                this.textures = textures;
            }
        }

        public FlowerPotBlockBuilder(BlockStateModelGenerator generator, Block flowerPotBlock, String potName) {
            this.generator = generator;
            this.flowerPotBlock = flowerPotBlock;
            this.potName = potName;
        }

        public FlowerPotBlockBuilder isTinted() {
            this.isTinted = true;
            return this;
        }

        public FlowerPotBlockBuilder rotateRandom() {
            this.rotateRandom = true;
            return this;
        }

        public FlowerPotBlockBuilder textures(String... textures) {
            this.simpleTextures.clear();
            for (String texture : textures) {
                this.simpleTextures.add(texture);
            }
            return this;
        }

        public FlowerPotBlockBuilder addRandomTextureSet(int weight, String... textures) {
            this.randomTextureSets.add(new RandomTextureSet(weight, textures));
            return this;
        }

        public void build() {
            // If simple textures are provided, convert to a single random texture set
            if (!simpleTextures.isEmpty()) {
                if (randomTextureSets.isEmpty()) {
                    randomTextureSets.add(new RandomTextureSet(1, simpleTextures.toArray(new String[0])));
                } else {
                    throw new IllegalStateException("Cannot use both .textures() and .addRandomTextureSet() methods");
                }
            }

            if (randomTextureSets.isEmpty()) {
                throw new IllegalStateException("No textures defined for flower pot block " + flowerPotBlock + ". Use .textures() or .addRandomTextureSet()");
            }

            List<Identifier> modelIds = generateFlowerPotModels();
            VariantsBlockStateSupplier blockStateSupplier = generateBlockStateVariants(modelIds);

            generator.blockStateCollector.accept(blockStateSupplier);

            // Register item model using first model
            if (!modelIds.isEmpty()) {
                generator.registerParentedItemModel(flowerPotBlock, modelIds.get(0));
            }
        }

        private List<Identifier> generateFlowerPotModels() {
            List<Identifier> modelIds = new ArrayList<>();

            for (int setIdx = 0; setIdx < randomTextureSets.size(); setIdx++) {
                RandomTextureSet textureSet = randomTextureSets.get(setIdx);

                // Determine if this is an empty pot (only 2 textures) or filled pot (3 textures)
                boolean isEmpty = textureSet.textures.length == 2;

                TextureMap textureMap = createTextureMap(textureSet, isEmpty);

                String modelSuffix = "/base_v" + (setIdx + 1);
                Identifier modelId = createFlowerPotModel(isEmpty, isTinted)
                        .upload(flowerPotBlock, modelSuffix, textureMap, generator.modelCollector);

                modelIds.add(modelId);
            }

            return modelIds;
        }

        private TextureMap createTextureMap(RandomTextureSet textureSet, boolean isEmpty) {
            TextureMap textureMap = new TextureMap();

            if (isEmpty) {
                // Empty pot: dirt[0], flowerpot[1]
                textureMap.put(ModTextureKey.DIRT, parseTextureIdentifier(textureSet.textures[0]));
                textureMap.put(ModTextureKey.FLOWERPOT, parseTextureIdentifier(textureSet.textures[1]));
                textureMap.put(TextureKey.PARTICLE, parseTextureIdentifier(textureSet.textures[1]));
            } else {
                // Filled pot: dirt[0], flowerpot[1], plant[2]
                textureMap.put(ModTextureKey.DIRT, parseTextureIdentifier(textureSet.textures[0]));
                textureMap.put(ModTextureKey.FLOWERPOT, parseTextureIdentifier(textureSet.textures[1]));
                textureMap.put(ModTextureKey.PLANT, parseTextureIdentifier(textureSet.textures[2]));
                textureMap.put(TextureKey.PARTICLE, parseTextureIdentifier(textureSet.textures[1]));
            }

            return textureMap;
        }

        private Identifier parseTextureIdentifier(String texture) {
            // If texture already contains a namespace (like "minecraft:block/dirt"), use it directly
            if (texture.contains(":")) {
                return Identifier.of(texture);
            } else {
                // If it's just a path (like "flowers/blue_bells"), add our namespace and block prefix
                return WesterosBlocks.id("block/" + texture);
            }
        }

        private VariantsBlockStateSupplier generateBlockStateVariants(List<Identifier> modelIds) {
            List<BlockStateVariant> variants = new ArrayList<>();
            boolean hasWeights = false;

            for (int setIdx = 0; setIdx < randomTextureSets.size(); setIdx++) {
                RandomTextureSet textureSet = randomTextureSets.get(setIdx);
                Identifier modelId = modelIds.get(setIdx);

                int rotationCount = rotateRandom ? 4 : 1;

                for (int rotation = 0; rotation < rotationCount; rotation++) {
                    BlockStateVariant variant = BlockStateVariant.create()
                            .put(VariantSettings.MODEL, modelId);

                    if (textureSet.weight > 1) {
                        variant = variant.put(VariantSettings.WEIGHT, textureSet.weight);
                        hasWeights = true;
                    }

                    if (rotation > 0) {
                        variant = variant.put(VariantSettings.Y, VariantSettings.Rotation.valueOf("R" + (90 * rotation)));
                    }

                    variants.add(variant);
                }
            }

            // If only one variant without weights, use single variant format
            if (variants.size() == 1 && !hasWeights) {
                return VariantsBlockStateSupplier.create(flowerPotBlock, variants.get(0));
            } else {
                return VariantsBlockStateSupplier.create(flowerPotBlock, variants.toArray(new BlockStateVariant[0]));
            }
        }
    }

    // Entry point for builder pattern
    public static FlowerPotBlockBuilder generateFlowerPotBlock(BlockStateModelGenerator generator, Block flowerPotBlock, String potName) {
        return new FlowerPotBlockBuilder(generator, flowerPotBlock, potName);
    }
}
