package com.westerosblocks.datagen.custom;

import com.westerosblocks.WesterosBlocks;
import net.minecraft.data.client.*;
import net.minecraft.block.Block;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FenceGateBlockDatagen {

    // Parent Block Models - fence gate templates
    private static Model createFenceGateModel(boolean tinted) {
        String tintPath = tinted ? "block/tinted/" : "block/untinted/";
        String path = tintPath + "template_fence_gate";
        return new Model(Optional.of(WesterosBlocks.id(path)), Optional.empty(), TextureKey.TEXTURE, TextureKey.PARTICLE);
    }

    private static Model createFenceGateOpenModel(boolean tinted) {
        String tintPath = tinted ? "block/tinted/" : "block/untinted/";
        String path = tintPath + "template_fence_gate_open";
        return new Model(Optional.of(WesterosBlocks.id(path)), Optional.empty(), TextureKey.TEXTURE, TextureKey.PARTICLE);
    }

    private static Model createFenceGateWallModel(boolean tinted) {
        String tintPath = tinted ? "block/tinted/" : "block/untinted/";
        String path = tintPath + "template_fence_gate_wall";
        return new Model(Optional.of(WesterosBlocks.id(path)), Optional.empty(), TextureKey.TEXTURE, TextureKey.PARTICLE);
    }

    private static Model createFenceGateWallOpenModel(boolean tinted) {
        String tintPath = tinted ? "block/tinted/" : "block/untinted/";
        String path = tintPath + "template_fence_gate_wall_open";
        return new Model(Optional.of(WesterosBlocks.id(path)), Optional.empty(), TextureKey.TEXTURE, TextureKey.PARTICLE);
    }

    // Builder pattern for fence gate block generation
    public static class FenceGateBlockBuilder {
        private final BlockStateModelGenerator generator;
        private final Block fenceGateBlock;
        private final String gateName;
        private boolean isTinted = false;
        private final List<RandomTextureSet> randomTextureSets = new ArrayList<>();
        private final List<String> simpleTextures = new ArrayList<>();

        // Inner class to hold random texture set information
        public static class RandomTextureSet {
            public final String texture;
            public final int weight;

            public RandomTextureSet(int weight, String texture) {
                this.weight = weight;
                this.texture = texture;
            }
        }

        public FenceGateBlockBuilder(BlockStateModelGenerator generator, Block fenceGateBlock, String gateName) {
            this.generator = generator;
            this.fenceGateBlock = fenceGateBlock;
            this.gateName = gateName;
        }

        public FenceGateBlockBuilder isTinted() {
            this.isTinted = true;
            return this;
        }

        // Single texture for fence gate
        public FenceGateBlockBuilder texture(String texture) {
            this.simpleTextures.clear();
            this.simpleTextures.add(texture);
            return this;
        }

        // Add random texture set with weight
        public FenceGateBlockBuilder addRandomTextureSet(int weight, String texture) {
            this.randomTextureSets.add(new RandomTextureSet(weight, texture));
            return this;
        }

        public void build() {
            // If simple textures are provided, convert to a single random texture set
            if (!simpleTextures.isEmpty()) {
                if (randomTextureSets.isEmpty()) {
                    randomTextureSets.add(new RandomTextureSet(1, simpleTextures.get(0)));
                } else {
                    throw new IllegalStateException("Cannot use both .texture() and .addRandomTextureSet() methods");
                }
            }

            if (randomTextureSets.isEmpty()) {
                throw new IllegalStateException("No textures defined for fence gate block " + fenceGateBlock + ". Use .texture() or .addRandomTextureSet()");
            }

            List<Identifier> gateModelIds = new ArrayList<>();
            List<Identifier> gateOpenModelIds = new ArrayList<>();
            List<Identifier> gateWallModelIds = new ArrayList<>();
            List<Identifier> gateWallOpenModelIds = new ArrayList<>();

            generateFenceGateModels(gateModelIds, gateOpenModelIds, gateWallModelIds, gateWallOpenModelIds);
            VariantsBlockStateSupplier blockStateSupplier = generateBlockStateVariants(gateModelIds, gateOpenModelIds, gateWallModelIds, gateWallOpenModelIds);

            generator.blockStateCollector.accept(blockStateSupplier);

            // Create fence gate item model
            generateFenceGateItemModel();
        }

        private void generateFenceGateModels(List<Identifier> gateModelIds, List<Identifier> gateOpenModelIds,
                                           List<Identifier> gateWallModelIds, List<Identifier> gateWallOpenModelIds) {
            for (int setIdx = 0; setIdx < randomTextureSets.size(); setIdx++) {
                RandomTextureSet textureSet = randomTextureSets.get(setIdx);

                // Create texture map
                TextureMap textureMap = new TextureMap()
                        .put(TextureKey.TEXTURE, WesterosBlocks.id("block/" + textureSet.texture))
                        .put(TextureKey.PARTICLE, WesterosBlocks.id("block/" + textureSet.texture));

                // Generate gate models (closed)
                String gateSuffix = "/gate_v" + (setIdx + 1);
                Identifier gateModelId = createFenceGateModel(isTinted)
                        .upload(fenceGateBlock, gateSuffix, textureMap, generator.modelCollector);
                gateModelIds.add(gateModelId);

                // Generate gate open models
                String gateOpenSuffix = "/gate_open_v" + (setIdx + 1);
                Identifier gateOpenModelId = createFenceGateOpenModel(isTinted)
                        .upload(fenceGateBlock, gateOpenSuffix, textureMap, generator.modelCollector);
                gateOpenModelIds.add(gateOpenModelId);

                // Generate gate wall models (closed in wall)
                String gateWallSuffix = "/gate_wall_v" + (setIdx + 1);
                Identifier gateWallModelId = createFenceGateWallModel(isTinted)
                        .upload(fenceGateBlock, gateWallSuffix, textureMap, generator.modelCollector);
                gateWallModelIds.add(gateWallModelId);

                // Generate gate wall open models
                String gateWallOpenSuffix = "/gate_wall_open_v" + (setIdx + 1);
                Identifier gateWallOpenModelId = createFenceGateWallOpenModel(isTinted)
                        .upload(fenceGateBlock, gateWallOpenSuffix, textureMap, generator.modelCollector);
                gateWallOpenModelIds.add(gateWallOpenModelId);
            }
        }

        private VariantsBlockStateSupplier generateBlockStateVariants(List<Identifier> gateModelIds, List<Identifier> gateOpenModelIds,
                                                                     List<Identifier> gateWallModelIds, List<Identifier> gateWallOpenModelIds) {
            // Create variants for all 16 combinations
            BlockStateVariant[] variants = new BlockStateVariant[16];
            int index = 0;

            // Generate all combinations: 4 facings × 2 open states × 2 in_wall states
            Direction[] facings = {
                Direction.EAST,
                Direction.NORTH,
                Direction.SOUTH,
                Direction.WEST
            };
            int[] rotations = {270, 180, 0, 90};
            boolean[] openStates = {false, true};
            boolean[] inWallStates = {false, true};

            for (int f = 0; f < facings.length; f++) {
                for (boolean open : openStates) {
                    for (boolean inWall : inWallStates) {
                        List<Identifier> modelIds;
                        if (!open && !inWall) {
                            modelIds = gateModelIds;
                        } else if (open && !inWall) {
                            modelIds = gateOpenModelIds;
                        } else if (!open && inWall) {
                            modelIds = gateWallModelIds;
                        } else {
                            modelIds = gateWallOpenModelIds;
                        }

                        variants[index] = createVariant(modelIds.get(0), rotations[f]);
                        index++;
                    }
                }
            }

            // Create the variant map with proper property mappings
            return VariantsBlockStateSupplier.create(fenceGateBlock)
                    .coordinate(
                        BlockStateVariantMap.create(Properties.HORIZONTAL_FACING, Properties.OPEN, Properties.IN_WALL)
                                .register(Direction.EAST, false, false, variants[0])
                                .register(Direction.EAST, false, true, variants[1])
                                .register(Direction.EAST, true, false, variants[2])
                                .register(Direction.EAST, true, true, variants[3])
                                .register(Direction.NORTH, false, false, variants[4])
                                .register(Direction.NORTH, false, true, variants[5])
                                .register(Direction.NORTH, true, false, variants[6])
                                .register(Direction.NORTH, true, true, variants[7])
                                .register(Direction.SOUTH, false, false, variants[8])
                                .register(Direction.SOUTH, false, true, variants[9])
                                .register(Direction.SOUTH, true, false, variants[10])
                                .register(Direction.SOUTH, true, true, variants[11])
                                .register(Direction.WEST, false, false, variants[12])
                                .register(Direction.WEST, false, true, variants[13])
                                .register(Direction.WEST, true, false, variants[14])
                                .register(Direction.WEST, true, true, variants[15])
                    );
        }

        private BlockStateVariant createVariant(Identifier modelId, int yRotation) {
            BlockStateVariant variant = BlockStateVariant.create()
                    .put(VariantSettings.MODEL, modelId)
                    .put(VariantSettings.UVLOCK, true);

            if (yRotation != 0) {
                VariantSettings.Rotation rotation = switch (yRotation) {
                    case 90 -> VariantSettings.Rotation.R90;
                    case 180 -> VariantSettings.Rotation.R180;
                    case 270 -> VariantSettings.Rotation.R270;
                    default -> VariantSettings.Rotation.R0;
                };
                variant = variant.put(VariantSettings.Y, rotation);
            }

            return variant;
        }

        private void generateFenceGateItemModel() {
            // Use first texture set for item model
            RandomTextureSet firstTextureSet = randomTextureSets.get(0);
            TextureMap itemTextureMap = new TextureMap()
                    .put(TextureKey.TEXTURE, WesterosBlocks.id("block/" + firstTextureSet.texture))
                    .put(TextureKey.PARTICLE, WesterosBlocks.id("block/" + firstTextureSet.texture));

            Identifier itemModelId = Identifier.of("westerosblocks", "item/" + gateName);
            createFenceGateModel(isTinted).upload(itemModelId, itemTextureMap, generator.modelCollector);
        }
    }

    // Entry point for builder pattern
    public static FenceGateBlockBuilder generateFenceGateBlock(BlockStateModelGenerator generator, Block fenceGateBlock, String gateName) {
        return new FenceGateBlockBuilder(generator, fenceGateBlock, gateName);
    }
}
