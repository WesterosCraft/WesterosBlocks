package com.westerosblocks.datagen.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.datagen.ModTextureKey;
import net.minecraft.data.client.*;
import net.minecraft.block.Block;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FenceBlockDatagen {

    // Parent Block Models - following block-models.md #parent-block-model pattern
    private static Model createFencePostModel(boolean tinted, boolean overlay) {
        String tintPath = tinted ? "block/tinted/" : "block/untinted/";
        String overlayPath = overlay ? "fence_post_overlay" : "fence_post";
        String path = tintPath + overlayPath;

        if (overlay) {
            return new Model(Optional.of(WesterosBlocks.id(path)), Optional.empty(),
                TextureKey.BOTTOM, TextureKey.TOP, TextureKey.SIDE,
                ModTextureKey.BOTTOM_OVERLAY, ModTextureKey.TOP_OVERLAY, ModTextureKey.SIDE_OVERLAY, TextureKey.PARTICLE);
        } else {
            return new Model(Optional.of(WesterosBlocks.id(path)), Optional.empty(),
                TextureKey.BOTTOM, TextureKey.TOP, TextureKey.SIDE, TextureKey.PARTICLE);
        }
    }

    private static Model createFenceSideModel(boolean tinted, boolean overlay) {
        String tintPath = tinted ? "block/tinted/" : "block/untinted/";
        String overlayPath = overlay ? "fence_side_overlay" : "fence_side";
        String path = tintPath + overlayPath;

        if (overlay) {
            return new Model(Optional.of(WesterosBlocks.id(path)), Optional.empty(),
                TextureKey.BOTTOM, TextureKey.TOP, TextureKey.SIDE,
                ModTextureKey.BOTTOM_OVERLAY, ModTextureKey.TOP_OVERLAY, ModTextureKey.SIDE_OVERLAY, TextureKey.PARTICLE);
        } else {
            return new Model(Optional.of(WesterosBlocks.id(path)), Optional.empty(),
                TextureKey.BOTTOM, TextureKey.TOP, TextureKey.SIDE, TextureKey.PARTICLE);
        }
    }

    private static Model createFenceInventoryModel(boolean tinted, boolean overlay) {
        String tintPath = tinted ? "block/tinted/" : "block/untinted/";
        String overlayPath = overlay ? "fence_inventory_overlay" : "fence_inventory";
        String path = tintPath + overlayPath;

        if (overlay) {
            return new Model(Optional.of(WesterosBlocks.id(path)), Optional.empty(),
                TextureKey.BOTTOM, TextureKey.TOP, TextureKey.SIDE,
                ModTextureKey.BOTTOM_OVERLAY, ModTextureKey.TOP_OVERLAY, ModTextureKey.SIDE_OVERLAY, TextureKey.PARTICLE);
        } else {
            return new Model(Optional.of(WesterosBlocks.id(path)), Optional.empty(),
                TextureKey.BOTTOM, TextureKey.TOP, TextureKey.SIDE, TextureKey.PARTICLE);
        }
    }

    // Builder pattern for fence block generation
    public static class FenceBlockBuilder {
        private final BlockStateModelGenerator generator;
        private final Block fenceBlock;
        private final String fenceName;
        private boolean isTinted = false;
        private boolean hasOverlay = false;
        private final List<RandomTextureSet> randomTextureSets = new ArrayList<>();
        private final List<String> simpleTextures = new ArrayList<>();

        // Inner class to hold random texture set information
        public static class RandomTextureSet {
            public final String[] textures;
            public final String[] overlayTextures;
            public final int weight;

            public RandomTextureSet(int weight, String[] textures, String[] overlayTextures) {
                this.weight = weight;
                this.textures = textures;
                this.overlayTextures = overlayTextures;
            }

            public RandomTextureSet(int weight, String... textures) {
                this(weight, textures, null);
            }
        }

        public FenceBlockBuilder(BlockStateModelGenerator generator, Block fenceBlock, String fenceName) {
            this.generator = generator;
            this.fenceBlock = fenceBlock;
            this.fenceName = fenceName;
        }

        public FenceBlockBuilder isTinted() {
            this.isTinted = true;
            return this;
        }

        public FenceBlockBuilder hasOverlay() {
            this.hasOverlay = true;
            return this;
        }

        // Single texture for all fence parts
        public FenceBlockBuilder texture(String texture) {
            this.simpleTextures.clear();
            this.simpleTextures.add(texture);
            return this;
        }

        // Simple textures for basic fence blocks (bottom, top, side)
        // If only one texture is provided, it will be used for all three parts
        public FenceBlockBuilder textures(String... textures) {
            this.simpleTextures.clear();
            for (String texture : textures) {
                this.simpleTextures.add(texture);
            }
            return this;
        }

        // Add random texture set with weight
        public FenceBlockBuilder addRandomTextureSet(int weight, String... textures) {
            this.randomTextureSets.add(new RandomTextureSet(weight, textures));
            return this;
        }

        // Add random texture set with overlay textures
        public FenceBlockBuilder addRandomTextureSetWithOverlay(int weight, String[] textures, String[] overlayTextures) {
            this.randomTextureSets.add(new RandomTextureSet(weight, textures, overlayTextures));
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
                throw new IllegalStateException("No textures defined for fence block " + fenceBlock + ". Use .textures() or .addRandomTextureSet()");
            }

            // Validate and expand texture arrays
            for (int i = 0; i < randomTextureSets.size(); i++) {
                RandomTextureSet textureSet = randomTextureSets.get(i);

                // Expand single texture to three textures (bottom, top, side)
                String[] expandedTextures;
                if (textureSet.textures.length == 1) {
                    expandedTextures = new String[]{textureSet.textures[0], textureSet.textures[0], textureSet.textures[0]};
                } else if (textureSet.textures.length == 3) {
                    expandedTextures = textureSet.textures;
                } else {
                    throw new IllegalStateException("Fence blocks require 1 or 3 textures (single texture or bottom, top, side), got " + textureSet.textures.length);
                }

                // Handle overlay textures
                String[] expandedOverlayTextures = null;
                if (hasOverlay) {
                    if (textureSet.overlayTextures == null) {
                        throw new IllegalStateException("Fence blocks with overlay require overlay textures");
                    }
                    if (textureSet.overlayTextures.length == 1) {
                        expandedOverlayTextures = new String[]{textureSet.overlayTextures[0], textureSet.overlayTextures[0], textureSet.overlayTextures[0]};
                    } else if (textureSet.overlayTextures.length == 3) {
                        expandedOverlayTextures = textureSet.overlayTextures;
                    } else {
                        throw new IllegalStateException("Fence blocks with overlay require 1 or 3 overlay textures, got " + textureSet.overlayTextures.length);
                    }
                }

                // Replace the texture set with expanded version
                randomTextureSets.set(i, new RandomTextureSet(textureSet.weight, expandedTextures, expandedOverlayTextures));
            }

            List<Identifier> postModelIds = new ArrayList<>();
            List<Identifier> sideModelIds = new ArrayList<>();

            generateFenceModels(postModelIds, sideModelIds);
            MultipartBlockStateSupplier blockStateSupplier = generateBlockStateVariants(postModelIds, sideModelIds);

            generator.blockStateCollector.accept(blockStateSupplier);

            // Create fence inventory item model
            generateFenceItemModel();
        }

        private void generateFenceModels(List<Identifier> postModelIds, List<Identifier> sideModelIds) {
            for (int setIdx = 0; setIdx < randomTextureSets.size(); setIdx++) {
                RandomTextureSet textureSet = randomTextureSets.get(setIdx);

                // Create texture map for post and side models
                TextureMap textureMap = createTextureMap(textureSet);

                // Generate post model
                String postModelSuffix = "/post_v" + (setIdx + 1);
                Identifier postModelId = createFencePostModel(isTinted, hasOverlay)
                        .upload(fenceBlock, postModelSuffix, textureMap, generator.modelCollector);
                postModelIds.add(postModelId);

                // Generate side model
                String sideModelSuffix = "/side_v" + (setIdx + 1);
                Identifier sideModelId = createFenceSideModel(isTinted, hasOverlay)
                        .upload(fenceBlock, sideModelSuffix, textureMap, generator.modelCollector);
                sideModelIds.add(sideModelId);
            }
        }

        private TextureMap createTextureMap(RandomTextureSet textureSet) {
            TextureMap textureMap = new TextureMap()
                    .put(TextureKey.BOTTOM, WesterosBlocks.id("block/" + textureSet.textures[0]))
                    .put(TextureKey.TOP, WesterosBlocks.id("block/" + textureSet.textures[1]))
                    .put(TextureKey.SIDE, WesterosBlocks.id("block/" + textureSet.textures[2]))
                    .put(TextureKey.PARTICLE, WesterosBlocks.id("block/" + textureSet.textures[2]));

            if (hasOverlay && textureSet.overlayTextures != null) {
                textureMap.put(ModTextureKey.BOTTOM_OVERLAY, WesterosBlocks.id("block/" + textureSet.overlayTextures[0]));
                textureMap.put(ModTextureKey.TOP_OVERLAY, WesterosBlocks.id("block/" + textureSet.overlayTextures[1]));
                textureMap.put(ModTextureKey.SIDE_OVERLAY, WesterosBlocks.id("block/" + textureSet.overlayTextures[2]));
            }

            return textureMap;
        }

        private MultipartBlockStateSupplier generateBlockStateVariants(List<Identifier> postModelIds, List<Identifier> sideModelIds) {
            // Create multipart blockstate for fence connections
            MultipartBlockStateSupplier supplier = MultipartBlockStateSupplier.create(fenceBlock);

            // Add post models (always present - no conditions)
            List<BlockStateVariant> postVariants = new ArrayList<>();
            for (int i = 0; i < postModelIds.size(); i++) {
                RandomTextureSet textureSet = randomTextureSets.get(i);
                BlockStateVariant postVariant = BlockStateVariant.create()
                        .put(VariantSettings.MODEL, postModelIds.get(i));

                if (textureSet.weight > 1) {
                    postVariant = postVariant.put(VariantSettings.WEIGHT, textureSet.weight);
                }

                postVariants.add(postVariant);
            }
            supplier.with(postVariants);

            // Add side models for each direction
            addDirectionalSideModels(supplier, sideModelIds, Direction.NORTH);
            addDirectionalSideModels(supplier, sideModelIds, Direction.EAST);
            addDirectionalSideModels(supplier, sideModelIds, Direction.SOUTH);
            addDirectionalSideModels(supplier, sideModelIds, Direction.WEST);

            return supplier;
        }

        private void addDirectionalSideModels(MultipartBlockStateSupplier supplier, List<Identifier> sideModelIds,
                                            Direction direction) {
            List<BlockStateVariant> sideVariants = new ArrayList<>();

            for (int i = 0; i < sideModelIds.size(); i++) {
                RandomTextureSet textureSet = randomTextureSets.get(i);
                BlockStateVariant sideVariant = BlockStateVariant.create()
                        .put(VariantSettings.MODEL, sideModelIds.get(i));

                // Add rotation based on direction
                switch (direction) {
                    case EAST -> sideVariant = sideVariant.put(VariantSettings.Y, VariantSettings.Rotation.R90);
                    case SOUTH -> sideVariant = sideVariant.put(VariantSettings.Y, VariantSettings.Rotation.R180);
                    case WEST -> sideVariant = sideVariant.put(VariantSettings.Y, VariantSettings.Rotation.R270);
                    // NORTH gets no rotation (0 degrees)
                }

                if (textureSet.weight > 1) {
                    sideVariant = sideVariant.put(VariantSettings.WEIGHT, textureSet.weight);
                }

                // Add UV lock for rotated models
                if (direction != Direction.NORTH) {
                    sideVariant = sideVariant.put(VariantSettings.UVLOCK, true);
                }

                sideVariants.add(sideVariant);
            }

            // Add condition for when this side should be rendered (when the property is true)
            When condition = switch (direction) {
                case NORTH -> When.create().set(Properties.NORTH, true);
                case EAST -> When.create().set(Properties.EAST, true);
                case SOUTH -> When.create().set(Properties.SOUTH, true);
                case WEST -> When.create().set(Properties.WEST, true);
            };

            supplier.with(condition, sideVariants);
        }

        private void generateFenceItemModel() {
            // Use first texture set for item model
            RandomTextureSet firstTextureSet = randomTextureSets.get(0);
            TextureMap itemTextureMap = createTextureMap(firstTextureSet);

            Identifier itemModelId = Identifier.of("westerosblocks", "item/" + fenceName);
            createFenceInventoryModel(isTinted, hasOverlay)
                    .upload(itemModelId, itemTextureMap, generator.modelCollector);
        }

        // Direction enum for internal use
        private enum Direction {
            NORTH, EAST, SOUTH, WEST
        }
    }

    // Entry point for builder pattern
    public static FenceBlockBuilder generateFenceBlock(BlockStateModelGenerator generator, Block fenceBlock, String fenceName) {
        return new FenceBlockBuilder(generator, fenceBlock, fenceName);
    }
}
