package com.westerosblocks.datagen.custom;

import com.westerosblocks.WesterosBlocks;
import net.minecraft.data.client.*;
import net.minecraft.block.Block;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class FireBlockDatagen {

    public static class FireBlockBuilder {
        private final BlockStateModelGenerator generator;
        private final Block fireBlock;
        private final String fireName;
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

        public FireBlockBuilder(BlockStateModelGenerator generator, Block fireBlock, String fireName) {
            this.generator = generator;
            this.fireBlock = fireBlock;
            this.fireName = fireName;
        }

        public FireBlockBuilder textures(String... textures) {
            this.simpleTextures.clear();
            for (String texture : textures) {
                this.simpleTextures.add(texture);
            }
            return this;
        }

        public FireBlockBuilder addRandomTextureSet(int weight, String... textures) {
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
                throw new IllegalStateException("No textures defined for fire block " + fireBlock + ". Use .textures() or .addRandomTextureSet()");
            }

            // Ensure we have at least 2 textures for fire animation
            for (RandomTextureSet textureSet : randomTextureSets) {
                if (textureSet.textures.length < 2) {
                    throw new IllegalStateException("Fire blocks require at least 2 textures for animation. Set: " +
                        java.util.Arrays.toString(textureSet.textures));
                }
            }

            generateFireModels();
            MultipartBlockStateSupplier blockStateSupplier = generateBlockStateVariants();

            generator.blockStateCollector.accept(blockStateSupplier);

            // Register item model using first floor model (matching old behavior)
            if (!randomTextureSets.isEmpty()) {
                Identifier firstFloorModel = getModelId("floor0", 0);
                generator.registerParentedItemModel(fireBlock, firstFloorModel);
            }
        }

        private void generateFireModels() {
            for (int setIdx = 0; setIdx < randomTextureSets.size(); setIdx++) {
                RandomTextureSet textureSet = randomTextureSets.get(setIdx);
                String texture0 = textureSet.textures[0];
                String texture1 = textureSet.textures[1];

                // Generate floor models
                generateFireModel("floor0", texture0, setIdx, Models.TEMPLATE_FIRE_FLOOR);
                generateFireModel("floor1", texture1, setIdx, Models.TEMPLATE_FIRE_FLOOR);

                // Generate side models
                generateFireModel("side0", texture0, setIdx, Models.TEMPLATE_FIRE_SIDE);
                generateFireModel("side1", texture1, setIdx, Models.TEMPLATE_FIRE_SIDE);
                generateFireModel("side_alt0", texture0, setIdx, Models.TEMPLATE_FIRE_SIDE_ALT);
                generateFireModel("side_alt1", texture1, setIdx, Models.TEMPLATE_FIRE_SIDE_ALT);

                // Generate up models
                generateFireModel("up0", texture0, setIdx, Models.TEMPLATE_FIRE_UP);
                generateFireModel("up1", texture1, setIdx, Models.TEMPLATE_FIRE_UP);
                generateFireModel("up_alt0", texture0, setIdx, Models.TEMPLATE_FIRE_UP_ALT);
                generateFireModel("up_alt1", texture1, setIdx, Models.TEMPLATE_FIRE_UP_ALT);
            }
        }

        private void generateFireModel(String variant, String texture, int setIdx, Model model) {
            TextureMap textureMap = new TextureMap()
                    .put(TextureKey.FIRE, parseTextureIdentifier(texture));

            model.upload(getModelId(variant, setIdx), textureMap, generator.modelCollector);
        }

        private Identifier parseTextureIdentifier(String texture) {
            // If texture already contains a namespace (like "minecraft:block/fire"), use it directly
            if (texture.contains(":")) {
                return Identifier.of(texture);
            } else {
                // If it's just a path (like "fire/wildfire"), add our namespace and block prefix
                return WesterosBlocks.id("block/" + texture);
            }
        }

        private Identifier getModelId(String variant, int setIdx) {
            return Identifier.of(WesterosBlocks.MOD_ID,
                    String.format("block/custom/%s/%s_v%d",
                            fireName,
                            variant,
                            setIdx + 1));
        }

        private MultipartBlockStateSupplier generateBlockStateVariants() {
            MultipartBlockStateSupplier stateSupplier = MultipartBlockStateSupplier.create(fireBlock);

            for (int setIdx = 0; setIdx < randomTextureSets.size(); setIdx++) {
                // Base case: floor fire when no sides are connected
                When baseCase = When.create()
                        .set(Properties.NORTH, false)
                        .set(Properties.SOUTH, false)
                        .set(Properties.EAST, false)
                        .set(Properties.WEST, false)
                        .set(Properties.UP, false);

                stateSupplier.with(baseCase,
                        BlockStateVariant.create().put(VariantSettings.MODEL, getModelId("floor0", setIdx)),
                        BlockStateVariant.create().put(VariantSettings.MODEL, getModelId("floor1", setIdx)));

                // Directional fire variants
                addDirectionalFireVariants(stateSupplier, setIdx, "north", 0);
                addDirectionalFireVariants(stateSupplier, setIdx, "east", 90);
                addDirectionalFireVariants(stateSupplier, setIdx, "south", 180);
                addDirectionalFireVariants(stateSupplier, setIdx, "west", 270);

                // Up case: ceiling fire
                When upCase = When.create().set(Properties.UP, true);
                stateSupplier.with(upCase,
                        BlockStateVariant.create().put(VariantSettings.MODEL, getModelId("up0", setIdx)),
                        BlockStateVariant.create().put(VariantSettings.MODEL, getModelId("up1", setIdx)),
                        BlockStateVariant.create().put(VariantSettings.MODEL, getModelId("up_alt0", setIdx)),
                        BlockStateVariant.create().put(VariantSettings.MODEL, getModelId("up_alt1", setIdx)));
            }

            return stateSupplier;
        }

        private void addDirectionalFireVariants(MultipartBlockStateSupplier stateSupplier, int setIdx, String direction, int rotation) {
            When dirCase = When.create().set((Property<Boolean>) getDirection(direction), true);
            BlockStateVariant rotationVariant = BlockStateVariant.create()
                    .put(VariantSettings.Y, getRotation(rotation));

            stateSupplier.with(dirCase,
                    BlockStateVariant.union(
                            BlockStateVariant.create().put(VariantSettings.MODEL, getModelId("side0", setIdx)),
                            rotationVariant),
                    BlockStateVariant.union(
                            BlockStateVariant.create().put(VariantSettings.MODEL, getModelId("side1", setIdx)),
                            rotationVariant),
                    BlockStateVariant.union(
                            BlockStateVariant.create().put(VariantSettings.MODEL, getModelId("side_alt0", setIdx)),
                            rotationVariant),
                    BlockStateVariant.union(
                            BlockStateVariant.create().put(VariantSettings.MODEL, getModelId("side_alt1", setIdx)),
                            rotationVariant));
        }

        private Property<?> getDirection(String direction) {
            return switch (direction) {
                case "east" -> Properties.EAST;
                case "south" -> Properties.SOUTH;
                case "west" -> Properties.WEST;
                default -> Properties.NORTH;
            };
        }

        private VariantSettings.Rotation getRotation(int rotation) {
            return switch (rotation) {
                case 90 -> VariantSettings.Rotation.R90;
                case 180 -> VariantSettings.Rotation.R180;
                case 270 -> VariantSettings.Rotation.R270;
                default -> VariantSettings.Rotation.R0;
            };
        }
    }

    // Entry point for builder pattern
    public static FireBlockBuilder generateFireBlock(BlockStateModelGenerator generator, Block fireBlock, String fireName) {
        return new FireBlockBuilder(generator, fireBlock, fireName);
    }
}
