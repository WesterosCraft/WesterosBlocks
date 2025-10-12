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

/**
 * Exporter for fence gate blocks following block-models.md patterns.
 * Generates models for fence gates with open/closed and in_wall states.
 */
public class FenceGateBlockExporter extends BaseBlockExporter {

    private static Model createFenceGateModel(boolean tinted) {
        return createTintedModel(tinted, "template_fence_gate", TextureKey.TEXTURE, TextureKey.PARTICLE);
    }

    private static Model createFenceGateOpenModel(boolean tinted) {
        return createTintedModel(tinted, "template_fence_gate_open", TextureKey.TEXTURE, TextureKey.PARTICLE);
    }

    private static Model createFenceGateWallModel(boolean tinted) {
        return createTintedModel(tinted, "template_fence_gate_wall", TextureKey.TEXTURE, TextureKey.PARTICLE);
    }

    private static Model createFenceGateWallOpenModel(boolean tinted) {
        return createTintedModel(tinted, "template_fence_gate_wall_open", TextureKey.TEXTURE, TextureKey.PARTICLE);
    }

    /**
     * Creates a TextureMap for fence gate blocks.
     */
    private static TextureMap createFenceGateTextureMap(String texture) {
        return new TextureMap()
                .put(TextureKey.TEXTURE, createBlockIdentifier(texture))
                .put(TextureKey.PARTICLE, createBlockIdentifier(texture));
    }

    /**
     * Creates blockstate variants for fence gates.
     * 16 variants: 4 facings × 2 open states × 2 in_wall states
     */
    private static BlockStateVariantMap createFenceGateVariants(Identifier gateModelId, Identifier gateOpenModelId,
                                                                Identifier gateWallModelId, Identifier gateWallOpenModelId) {
        return BlockStateVariantMap.create(Properties.HORIZONTAL_FACING, Properties.OPEN, Properties.IN_WALL)
                // EAST facing
                .register(Direction.EAST, false, false, createVariant(gateModelId, 270))
                .register(Direction.EAST, false, true, createVariant(gateWallModelId, 270))
                .register(Direction.EAST, true, false, createVariant(gateOpenModelId, 270))
                .register(Direction.EAST, true, true, createVariant(gateWallOpenModelId, 270))
                // NORTH facing
                .register(Direction.NORTH, false, false, createVariant(gateModelId, 180))
                .register(Direction.NORTH, false, true, createVariant(gateWallModelId, 180))
                .register(Direction.NORTH, true, false, createVariant(gateOpenModelId, 180))
                .register(Direction.NORTH, true, true, createVariant(gateWallOpenModelId, 180))
                // SOUTH facing
                .register(Direction.SOUTH, false, false, createVariant(gateModelId, 0))
                .register(Direction.SOUTH, false, true, createVariant(gateWallModelId, 0))
                .register(Direction.SOUTH, true, false, createVariant(gateOpenModelId, 0))
                .register(Direction.SOUTH, true, true, createVariant(gateWallOpenModelId, 0))
                // WEST facing
                .register(Direction.WEST, false, false, createVariant(gateModelId, 90))
                .register(Direction.WEST, false, true, createVariant(gateWallModelId, 90))
                .register(Direction.WEST, true, false, createVariant(gateOpenModelId, 90))
                .register(Direction.WEST, true, true, createVariant(gateWallOpenModelId, 90));
    }

    // ========================================
    // Public Registration Methods (block-models.md 5.5)
    // ========================================

    /**
     * Registers a fence gate block with a single texture.
     */
    public static void registerFenceGateBlock(BlockStateModelGenerator generator, Block block, boolean tinted, String texture) {
        TextureMap textureMap = createFenceGateTextureMap(texture);

        // Upload all 4 model variants
        Identifier gateModelId = uploadModel(createFenceGateModel(tinted), block, "gate", textureMap, generator.modelCollector);
        Identifier gateOpenModelId = uploadModel(createFenceGateOpenModel(tinted), block, "gate_open", textureMap, generator.modelCollector);
        Identifier gateWallModelId = uploadModel(createFenceGateWallModel(tinted), block, "gate_wall", textureMap, generator.modelCollector);
        Identifier gateWallOpenModelId = uploadModel(createFenceGateWallOpenModel(tinted), block, "gate_wall_open", textureMap, generator.modelCollector);

        // Create blockstate
        BlockStateVariantMap variants = createFenceGateVariants(gateModelId, gateOpenModelId, gateWallModelId, gateWallOpenModelId);
        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block).coordinate(variants));

        // Register item model
        Identifier itemModelId = Identifier.of("westerosblocks", "item/" + getBlockName(block));
        createFenceGateModel(tinted).upload(itemModelId, textureMap, generator.modelCollector);
    }

    /**
     * Registers a fence gate block with random texture variants.
     */
    public static void registerFenceGateBlockWithRandomTextures(BlockStateModelGenerator generator, Block block,
                                                               boolean tinted, List<TextureVariant> textureVariants) {
        ModelRegistry gateRegistry = new ModelRegistry();
        ModelRegistry gateOpenRegistry = new ModelRegistry();
        ModelRegistry gateWallRegistry = new ModelRegistry();
        ModelRegistry gateWallOpenRegistry = new ModelRegistry();

        for (int i = 0; i < textureVariants.size(); i++) {
            TextureVariant variant = textureVariants.get(i);
            TextureMap textureMap = createFenceGateTextureMap(variant.texture);

            gateRegistry.add(uploadModel(createFenceGateModel(tinted), block, "gate_v" + (i + 1),
                                        textureMap, generator.modelCollector), variant.weight);
            gateOpenRegistry.add(uploadModel(createFenceGateOpenModel(tinted), block, "gate_open_v" + (i + 1),
                                            textureMap, generator.modelCollector), variant.weight);
            gateWallRegistry.add(uploadModel(createFenceGateWallModel(tinted), block, "gate_wall_v" + (i + 1),
                                            textureMap, generator.modelCollector), variant.weight);
            gateWallOpenRegistry.add(uploadModel(createFenceGateWallOpenModel(tinted), block, "gate_wall_open_v" + (i + 1),
                                                textureMap, generator.modelCollector), variant.weight);
        }

        // Create blockstate with weighted random textures
        generator.blockStateCollector.accept(createFenceGateBlockstateWithRandomTextures(block,
                gateRegistry.getModelIds(), gateOpenRegistry.getModelIds(),
                gateWallRegistry.getModelIds(), gateWallOpenRegistry.getModelIds(),
                gateRegistry.getWeights()));

        // Register item model (using first texture)
        TextureMap itemTextureMap = createFenceGateTextureMap(textureVariants.get(0).texture);
        Identifier itemModelId = Identifier.of("westerosblocks", "item/" + getBlockName(block));
        createFenceGateModel(tinted).upload(itemModelId, itemTextureMap, generator.modelCollector);
    }

    /**
     * Creates blockstate supplier with weighted random texture variants.
     */
    private static VariantsBlockStateSupplier createFenceGateBlockstateWithRandomTextures(Block block,
            List<Identifier> gateIds, List<Identifier> gateOpenIds, List<Identifier> gateWallIds,
            List<Identifier> gateWallOpenIds, List<Integer> weights) {

        return VariantsBlockStateSupplier.create(block).coordinate(
                BlockStateVariantMap.create(Properties.HORIZONTAL_FACING, Properties.OPEN, Properties.IN_WALL)
                        // EAST facing
                        .register(Direction.EAST, false, false, createWeightedVariants(gateIds, weights, 270))
                        .register(Direction.EAST, false, true, createWeightedVariants(gateWallIds, weights, 270))
                        .register(Direction.EAST, true, false, createWeightedVariants(gateOpenIds, weights, 270))
                        .register(Direction.EAST, true, true, createWeightedVariants(gateWallOpenIds, weights, 270))
                        // NORTH facing
                        .register(Direction.NORTH, false, false, createWeightedVariants(gateIds, weights, 180))
                        .register(Direction.NORTH, false, true, createWeightedVariants(gateWallIds, weights, 180))
                        .register(Direction.NORTH, true, false, createWeightedVariants(gateOpenIds, weights, 180))
                        .register(Direction.NORTH, true, true, createWeightedVariants(gateWallOpenIds, weights, 180))
                        // SOUTH facing
                        .register(Direction.SOUTH, false, false, createWeightedVariants(gateIds, weights, 0))
                        .register(Direction.SOUTH, false, true, createWeightedVariants(gateWallIds, weights, 0))
                        .register(Direction.SOUTH, true, false, createWeightedVariants(gateOpenIds, weights, 0))
                        .register(Direction.SOUTH, true, true, createWeightedVariants(gateWallOpenIds, weights, 0))
                        // WEST facing
                        .register(Direction.WEST, false, false, createWeightedVariants(gateIds, weights, 90))
                        .register(Direction.WEST, false, true, createWeightedVariants(gateWallIds, weights, 90))
                        .register(Direction.WEST, true, false, createWeightedVariants(gateOpenIds, weights, 90))
                        .register(Direction.WEST, true, true, createWeightedVariants(gateWallOpenIds, weights, 90))
        );
    }


    /**
     * Registers a fence gate block from a BlockDefinition.
     */
    public static void registerCustomFenceGateBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        boolean tinted = definition.isTinted() || definition.hasColorMult();
        List<String> textureList = definition.getTextures();

        if (definition.hasRandomTextures()) {
            List<TextureVariant> textureVariants = new ArrayList<>();
            List<BlockDefinition.RandomTextureVariant> randomTextures = definition.getRandomTextures();

            for (BlockDefinition.RandomTextureVariant randomTexture : randomTextures) {
                List<String> textures = randomTexture.getTextures();
                int weight = randomTexture.getWeight();

                if (textures != null && !textures.isEmpty()) {
                    textureVariants.add(new TextureVariant(weight, textures.get(0)));
                } else {
                    textureVariants.add(new TextureVariant(weight, "missingno"));
                }
            }

            registerFenceGateBlockWithRandomTextures(generator, block, tinted, textureVariants);
        } else if (textureList != null && !textureList.isEmpty()) {
            registerFenceGateBlock(generator, block, tinted, textureList.get(0));
        } else {
            registerFenceGateBlock(generator, block, tinted, "missingno");
        }
    }

    /**
     * Helper class for texture variants with weight.
     */
    public static class TextureVariant {
        public final int weight;
        public final String texture;

        public TextureVariant(int weight, String texture) {
            this.weight = weight;
            this.texture = texture;
        }
    }
}
