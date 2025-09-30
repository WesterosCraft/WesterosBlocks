package com.westerosblocks.datagen.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.data.BlockDefinition;
import net.minecraft.data.client.*;
import net.minecraft.block.Block;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FurnaceBlockDatagen {

    // Parent Block Model - following block-models.md #parent-block-model pattern
    private static Model createFurnaceModel(boolean tinted, boolean isCustom) {
        String basePath = isCustom ? "block/custom/" : (tinted ? "block/tinted/" : "block/untinted/");
        String path = basePath + "orientable";
        return new Model(Optional.of(WesterosBlocks.id(path)), Optional.empty(),
            TextureKey.TOP, TextureKey.FRONT, TextureKey.SIDE);
    }

    // Builder pattern for furnace block generation
    public static class FurnaceBlockBuilder {
        private final BlockStateModelGenerator generator;
        private final Block furnaceBlock;
        private final String furnaceName;
        private boolean isTinted = false;
        private boolean isCustom = false;
        private final List<String> textures = new ArrayList<>();

        // Furnace state variants - 8 states (4 facings × 2 lit states)
        private static final FurnaceVariant[] FURNACE_VARIANTS = {
            new FurnaceVariant(Direction.NORTH, true, "lit", 0),
            new FurnaceVariant(Direction.SOUTH, true, "lit", 180),
            new FurnaceVariant(Direction.WEST, true, "lit", 270),
            new FurnaceVariant(Direction.EAST, true, "lit", 90),
            new FurnaceVariant(Direction.NORTH, false, "base", 0),
            new FurnaceVariant(Direction.SOUTH, false, "base", 180),
            new FurnaceVariant(Direction.WEST, false, "base", 270),
            new FurnaceVariant(Direction.EAST, false, "base", 90)
        };

        // Inner class to hold furnace variant information
        public static class FurnaceVariant {
            public final Direction facing;
            public final boolean lit;
            public final String modelType;
            public final int yRotation;

            public FurnaceVariant(Direction facing, boolean lit, String modelType, int yRotation) {
                this.facing = facing;
                this.lit = lit;
                this.modelType = modelType;
                this.yRotation = yRotation;
            }
        }

        public FurnaceBlockBuilder(BlockStateModelGenerator generator, Block furnaceBlock, String furnaceName) {
            this.generator = generator;
            this.furnaceBlock = furnaceBlock;
            this.furnaceName = furnaceName;
        }

        public FurnaceBlockBuilder isTinted() {
            this.isTinted = true;
            return this;
        }

        public FurnaceBlockBuilder isCustom() {
            this.isCustom = true;
            return this;
        }

        public FurnaceBlockBuilder textures(String top, String side, String frontLit, String frontUnlit) {
            if (textures.size() != 0) {
                throw new IllegalStateException("Textures already set for furnace block " + furnaceBlock);
            }
            this.textures.add(top);      // 0: top texture
            this.textures.add(side);     // 1: side texture
            this.textures.add(frontLit); // 2: front texture when lit
            this.textures.add(frontUnlit); // 3: front texture when unlit
            return this;
        }

        public FurnaceBlockBuilder texture(String texture) {
            // Single texture for all sides (fallback)
            return textures(texture, texture, texture, texture);
        }

        public void build() {
            if (textures.size() != 4) {
                throw new IllegalStateException("Furnace block " + furnaceBlock + " requires exactly 4 textures (top, side, frontLit, frontUnlit), got " + textures.size());
            }

            List<Identifier> modelIds = generateFurnaceModels();
            VariantsBlockStateSupplier blockStateSupplier = generateBlockStateVariants(modelIds);

            generator.blockStateCollector.accept(blockStateSupplier);

            // Register item model using the unlit base model
            Identifier baseModelId = modelIds.size() >= 2 ? modelIds.get(1) : modelIds.get(0); // Use base (unlit) model
            generator.registerParentedItemModel(furnaceBlock, baseModelId);
        }

        private List<Identifier> generateFurnaceModels() {
            List<Identifier> modelIds = new ArrayList<>();

            if (isCustom) {
                // For custom models, don't generate - just reference existing models
                modelIds.add(WesterosBlocks.id("block/custom/" + furnaceName + "/lit_v1"));
                modelIds.add(WesterosBlocks.id("block/custom/" + furnaceName + "/base_v1"));
            } else {
                // Generate lit model (textures: top, side, frontLit)
                TextureMap litTextureMap = new TextureMap()
                        .put(TextureKey.TOP, WesterosBlocks.id("block/" + textures.get(0)))
                        .put(TextureKey.SIDE, WesterosBlocks.id("block/" + textures.get(1)))
                        .put(TextureKey.FRONT, WesterosBlocks.id("block/" + textures.get(2)));

                String litModelSuffix = "/lit_v1";
                Identifier litModelId = createFurnaceModel(isTinted, false)
                        .upload(furnaceBlock, litModelSuffix, litTextureMap, generator.modelCollector);
                modelIds.add(litModelId);

                // Generate base/unlit model (textures: top, side, frontUnlit)
                TextureMap baseTextureMap = new TextureMap()
                        .put(TextureKey.TOP, WesterosBlocks.id("block/" + textures.get(0)))
                        .put(TextureKey.SIDE, WesterosBlocks.id("block/" + textures.get(1)))
                        .put(TextureKey.FRONT, WesterosBlocks.id("block/" + textures.get(3)));

                String baseModelSuffix = "/base_v1";
                Identifier baseModelId = createFurnaceModel(isTinted, false)
                        .upload(furnaceBlock, baseModelSuffix, baseTextureMap, generator.modelCollector);
                modelIds.add(baseModelId);
            }

            return modelIds;
        }

        private VariantsBlockStateSupplier generateBlockStateVariants(List<Identifier> modelIds) {
            // modelIds[0] is lit, modelIds[1] is base/unlit
            Identifier litModelId = modelIds.get(0);
            Identifier baseModelId = modelIds.size() > 1 ? modelIds.get(1) : litModelId;

            BlockStateVariantMap.DoubleProperty<Direction, Boolean> variantMap =
                BlockStateVariantMap.create(Properties.HORIZONTAL_FACING, Properties.LIT);

            // Register variants for each facing and lit state
            for (FurnaceVariant variant : FURNACE_VARIANTS) {
                Identifier modelId = variant.lit ? litModelId : baseModelId;

                BlockStateVariant blockVariant = BlockStateVariant.create()
                        .put(VariantSettings.MODEL, modelId);

                // Add Y rotation if not 0
                if (variant.yRotation != 0) {
                    blockVariant = blockVariant.put(VariantSettings.Y, VariantSettings.Rotation.valueOf("R" + variant.yRotation));
                }

                variantMap.register(variant.facing, variant.lit, blockVariant);
            }

            return VariantsBlockStateSupplier.create(furnaceBlock).coordinate(variantMap);
        }
    }

    // Entry point for builder pattern
    public static FurnaceBlockBuilder generateFurnaceBlock(BlockStateModelGenerator generator, Block furnaceBlock, String furnaceName) {
        return new FurnaceBlockBuilder(generator, furnaceBlock, furnaceName);
    }

    // Method for JSON definition system integration
    public static void registerCustomFurnaceBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        List<String> textureList = definition.getTextures();
        FurnaceBlockBuilder builder = generateFurnaceBlock(generator, block, definition.getBlockName());

        // Check if this should use custom models
        boolean isCustomModel = definition.hasCustomModel();
        if (isCustomModel) {
            builder.isCustom();
        }

        // Check if tinted
        if (definition.isTinted() || definition.hasColorMult()) {
            builder.isTinted();
        }

        // Handle textures - furnace needs 4 textures: top, side, frontLit, frontUnlit
        if (textureList != null && textureList.size() >= 4) {
            // Use textures from definition: [top, side, frontLit, frontUnlit]
            builder.textures(textureList.get(0), textureList.get(1), textureList.get(2), textureList.get(3));
        } else if (textureList != null && textureList.size() >= 1) {
            // Fallback: use first texture for all sides
            String fallbackTexture = textureList.get(0);
            builder.textures(fallbackTexture, fallbackTexture, fallbackTexture, fallbackTexture);
            WesterosBlocks.LOGGER.warn("Furnace block '{}' has insufficient textures ({}), using fallback texture for all sides",
                definition.getBlockName(), textureList.size());
        } else {
            // No textures - use missingno or empty for custom
            String fallbackTexture = isCustomModel ? "" : "missingno";
            builder.textures(fallbackTexture, fallbackTexture, fallbackTexture, fallbackTexture);

        }

        builder.build();
    }
}
