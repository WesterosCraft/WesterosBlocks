package com.westerosblocks.datagen.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.data.BlockDefinition;
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
    private static Model createLadderModel(String blockName, boolean tinted, boolean isCustom) {
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

        public LadderBlockBuilder addRandomTextureSet(String texture) {
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
            return createLadderModel(ladderName, isTinted, false) // Always use non-custom for generated models
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

    // Method for JSON definition system integration
    public static void registerCustomLadderBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        List<String> textureList = definition.getTextures();
        LadderBlockBuilder builder = generateLadderBlock(generator, block, definition.getBlockName());

        // Check if this should use custom models based on the definition
        boolean isCustomModel = definition.hasCustomModel();

        if (definition.hasRandomTextures()) {
            // Count the random texture variants to determine how many models to generate
            List<BlockDefinition.RandomTextureVariant> randomTextures = definition.getRandomTextures();

            // Only set as custom if explicitly specified in JSON
            if (isCustomModel) {
                builder.isCustom();
            }

            for (int i = 0; i < randomTextures.size(); i++) {
                BlockDefinition.RandomTextureVariant randomTexture = randomTextures.get(i);
                List<String> textures = randomTexture.getTextures();

                // For empty objects {} in JSON, textures will be null or empty
                // Each empty object represents one model variant (base_v1, base_v2, etc.)
                if (textures == null || textures.isEmpty()) {
                    // Add empty texture set (for custom models) or fallback texture
                    if (isCustomModel) {
                        builder.addRandomTextureSet("");
                    } else {
                        builder.addRandomTextureSet("missingno");
                    }
                } else {
                    // Use actual textures if provided
                    for (String texture : textures) {
                        builder.addRandomTextureSet(texture);
                    }
                }
            }
            builder.build();
        } else if (textureList != null && !textureList.isEmpty() && !textureList.get(0).isEmpty()) {
            // Use first texture as main texture if it's not empty
            String texturePath = textureList.get(0);
            if (isCustomModel) {
                builder.isCustom();
            }
            builder.texture(texturePath).build();
        } else {
            // Fallback for missing or empty textures
            if (isCustomModel) {
                builder.isCustom().addRandomTextureSet("").build();
            } else {
                builder.texture("missingno").build();
            }
        }
    }
}
