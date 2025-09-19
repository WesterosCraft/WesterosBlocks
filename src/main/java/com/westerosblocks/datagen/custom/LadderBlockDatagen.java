package com.westerosblocks.datagen.custom;

import com.westerosblocks.WesterosBlocks;
import net.minecraft.data.client.*;
import net.minecraft.block.Block;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

public class LadderBlockDatagen {

    // Random texture set class for weighted variants
    public static class RandomTextureSet {
        private final String texture;
        private final int weight;

        public RandomTextureSet(String texture, int weight) {
            this.texture = texture;
            this.weight = weight;
        }

        public RandomTextureSet(String texture) {
            this(texture, 1); // Default weight of 1
        }

        public String getTexture() {
            return texture;
        }

        public int getWeight() {
            return weight;
        }
    }

    // Helper class for model variants with weights
    private static class ModelVariant {
        final Identifier modelId;
        final int weight;

        ModelVariant(Identifier modelId, int weight) {
            this.modelId = modelId;
            this.weight = weight;
        }
    }

    // Parent Block Model - following block-models.md #parent-block-model pattern
    private static Model createLadderModel(boolean tinted, boolean isCustom) {
        String basePath = isCustom ? "block/custom/" : (tinted ? "block/tinted/" : "block/untinted/");
        String path = basePath + "ladder";
        return new Model(Optional.of(WesterosBlocks.id(path)), Optional.empty(),
            TextureKey.TEXTURE, TextureKey.PARTICLE);
    }

    // Builder pattern for ladder block generation
    public static class LadderBlockBuilder {
        private final BlockStateModelGenerator generator;
        private final Block ladderBlock;
        private final String ladderName;
        private boolean isTinted = false;
        private boolean isCustom = false;
        private String texturePath;
        private final List<RandomTextureSet> randomTextureSets = new ArrayList<>();

        public LadderBlockBuilder(BlockStateModelGenerator generator, Block ladderBlock, String ladderName) {
            this.generator = generator;
            this.ladderBlock = ladderBlock;
            this.ladderName = ladderName;
        }

        public LadderBlockBuilder isTinted() {
            this.isTinted = true;
            return this;
        }

        public LadderBlockBuilder isCustom() {
            this.isCustom = true;
            return this;
        }

        public LadderBlockBuilder texture(String texturePath) {
            this.texturePath = texturePath;
            return this;
        }

        public LadderBlockBuilder randomTexture(String texture, int weight) {
            this.randomTextureSets.add(new RandomTextureSet(texture, weight));
            return this;
        }

        public LadderBlockBuilder randomTexture(String texture) {
            this.randomTextureSets.add(new RandomTextureSet(texture));
            return this;
        }

        // Allow empty string texture for custom models
        public LadderBlockBuilder randomTexture(String texture, int weight, boolean allowEmpty) {
            if (allowEmpty || !texture.isEmpty()) {
                this.randomTextureSets.add(new RandomTextureSet(texture, weight));
            }
            return this;
        }

        public void build() {
            // Use random texture sets if available, otherwise fall back to single texture
            if (!randomTextureSets.isEmpty()) {
                buildWithRandomTextures();
            } else {
                if (texturePath == null || texturePath.isEmpty()) {
                    throw new IllegalStateException("Texture path is required for ladder block " + ladderBlock);
                }
                buildSingleTexture();
            }
        }

        private void buildSingleTexture() {
            Identifier modelId;
            if (isCustom) {
                // For custom models, don't generate the model file - just reference it
                modelId = WesterosBlocks.id("block/custom/" + ladderName + "/base_v1");
            } else {
                // For non-custom models, generate the model file
                modelId = generateLadderModel(texturePath, 0);
            }

            VariantsBlockStateSupplier blockStateSupplier = generateBlockStateVariants(List.of(
                new ModelVariant(modelId, 1)
            ));

            generator.blockStateCollector.accept(blockStateSupplier);
            generator.registerParentedItemModel(ladderBlock, modelId);
        }

        private void buildWithRandomTextures() {
            List<ModelVariant> modelVariants = new ArrayList<>();

            // Generate models for each texture set
            for (int i = 0; i < randomTextureSets.size(); i++) {
                RandomTextureSet textureSet = randomTextureSets.get(i);
                Identifier modelId;

                if (isCustom) {
                    // For custom models, reference pre-existing model
                    String modelName = i == 0 ? "base_v1" : "base_v" + (i + 1);
                    modelId = WesterosBlocks.id("block/custom/" + ladderName + "/" + modelName);
                } else {
                    // Generate model file for non-custom
                    if (!textureSet.getTexture().isEmpty()) {
                        modelId = generateLadderModel(textureSet.getTexture(), i);
                    } else {
                        // Skip empty textures for non-custom models
                        continue;
                    }
                }

                modelVariants.add(new ModelVariant(modelId, textureSet.getWeight()));
            }

            if (modelVariants.isEmpty()) {
                throw new IllegalStateException("No valid texture variants for ladder block " + ladderBlock);
            }

            VariantsBlockStateSupplier blockStateSupplier = generateBlockStateVariants(modelVariants);
            generator.blockStateCollector.accept(blockStateSupplier);
            generator.registerParentedItemModel(ladderBlock, modelVariants.get(0).modelId);
        }

        private Identifier generateLadderModel(String texture, int index) {
            TextureMap textureMap = new TextureMap()
                    .put(TextureKey.TEXTURE, WesterosBlocks.id("block/" + texture))
                    .put(TextureKey.PARTICLE, WesterosBlocks.id("block/" + texture));

            // Generate model using the block as the base for the path
            String modelSuffix = index == 0 ? "/base_v1" : "/base_v" + (index + 1);
            return createLadderModel(isTinted, false) // Always use non-custom for generated models
                    .upload(ladderBlock, modelSuffix, textureMap, generator.modelCollector);
        }

        private VariantsBlockStateSupplier generateBlockStateVariants(List<ModelVariant> modelVariants) {
            BlockStateVariantMap.SingleProperty<Direction> variantMap =
                BlockStateVariantMap.create(Properties.HORIZONTAL_FACING);

            // For each direction, create a list of variants with proper rotation
            for (Direction direction : new Direction[]{Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST}) {
                List<BlockStateVariant> directionVariants = new ArrayList<>();

                for (ModelVariant variant : modelVariants) {
                    BlockStateVariant stateVariant = BlockStateVariant.create()
                        .put(VariantSettings.MODEL, variant.modelId);

                    // Add rotation for each direction
                    switch (direction) {
                        case EAST -> stateVariant.put(VariantSettings.Y, VariantSettings.Rotation.R90);
                        case SOUTH -> stateVariant.put(VariantSettings.Y, VariantSettings.Rotation.R180);
                        case WEST -> stateVariant.put(VariantSettings.Y, VariantSettings.Rotation.R270);
                        // NORTH gets no rotation (0 degrees)
                    }

                    // Add weight if greater than 1
                    if (variant.weight > 1) {
                        stateVariant.put(VariantSettings.WEIGHT, variant.weight);
                    }

                    directionVariants.add(stateVariant);
                }

                // Register all variants for this direction at once
                variantMap.register(direction, directionVariants);
            }

            return VariantsBlockStateSupplier.create(ladderBlock).coordinate(variantMap);
        }
    }

    // Entry point for builder pattern
    public static LadderBlockBuilder generateLadderBlock(BlockStateModelGenerator generator, Block ladderBlock, String ladderName) {
        return new LadderBlockBuilder(generator, ladderBlock, ladderName);
    }
}
