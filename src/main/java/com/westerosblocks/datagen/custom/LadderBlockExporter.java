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
 * Exporter for ladder blocks following block-models.md patterns.
 * Generates models for directional ladder blocks with optional random textures.
 */
public class LadderBlockExporter extends BaseBlockExporter {

    private static Model createLadderModel(boolean tinted) {
        return createTintedModel(tinted, "ladder", TextureKey.TEXTURE, TextureKey.PARTICLE);
    }


    private static TextureMap createLadderTextureMap(String texture) {
        return new TextureMap()
                .put(TextureKey.TEXTURE, createBlockIdentifier(texture))
                .put(TextureKey.PARTICLE, createBlockIdentifier(texture));
    }

    private static VariantsBlockStateSupplier createLadderBlockstate(Block block, List<Identifier> modelIds,
                                                                     List<Integer> weights) {
        BlockStateVariantMap.SingleProperty<Direction> variantMap = BlockStateVariantMap.create(Properties.HORIZONTAL_FACING);

        for (Direction direction : new Direction[]{Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST}) {
            List<BlockStateVariant> directionVariants = new ArrayList<>();

            int yRotation = getRotationForDirection(direction);

            for (int i = 0; i < modelIds.size(); i++) {
                BlockStateVariant variant = BlockStateVariant.create()
                        .put(VariantSettings.MODEL, modelIds.get(i));

                if (yRotation != 0) {
                    variant = variant.put(VariantSettings.Y, toYRotation(yRotation));
                }

                if (weights != null && weights.get(i) > 1) {
                    variant = variant.put(VariantSettings.WEIGHT, weights.get(i));
                }

                directionVariants.add(variant);
            }

            variantMap.register(direction, directionVariants);
        }

        return VariantsBlockStateSupplier.create(block).coordinate(variantMap);
    }

    // ========================================
    // Public Registration Methods (block-models.md 5.5)
    // ========================================

    public static void registerLadderBlock(BlockStateModelGenerator generator, Block block, boolean tinted, String texture) {
        TextureMap textureMap = createLadderTextureMap(texture);
        Identifier modelId = uploadModel(createLadderModel(tinted), block, "base", textureMap, generator.modelCollector);

        VariantsBlockStateSupplier blockstate = createLadderBlockstate(block, List.of(modelId), List.of(1));
        generator.blockStateCollector.accept(blockstate);
        registerParentedItemModel(generator, block, modelId);
    }

    public static void registerLadderBlockWithRandomTextures(BlockStateModelGenerator generator, Block block,
                                                             boolean tinted, List<BlockDefinition.TextureVariantSet> textureSets) {
        ModelRegistry registry = new ModelRegistry();

        for (int i = 0; i < textureSets.size(); i++) {
            BlockDefinition.TextureVariantSet set = textureSets.get(i);
            TextureMap textureMap = createLadderTextureMap(set.getFirstTexture());
            Identifier modelId = uploadModel(createLadderModel(tinted), block, "base_v" + (i + 1),
                                            textureMap, generator.modelCollector);
            registry.add(modelId, set.weight);
        }

        VariantsBlockStateSupplier blockstate = createLadderBlockstate(block,
                                                                       registry.getModelIds(), registry.getWeights());
        generator.blockStateCollector.accept(blockstate);
        registerParentedItemModel(generator, block, registry.getModelIds().get(0));
    }

    /**
     * Registers a ladder block using custom model references (no model generation).
     * For ladders with pre-existing custom models.
     */
    public static void registerLadderBlockCustomModel(BlockStateModelGenerator generator, Block block, String blockName, int variantCount) {
        List<Identifier> modelIds = new ArrayList<>();
        List<Integer> weights = new ArrayList<>();

        for (int i = 0; i < variantCount; i++) {
            String modelName = i == 0 ? "base_v1" : "base_v" + (i + 1);
            Identifier modelId = WesterosBlocks.id("block/custom/" + blockName + "/" + modelName);
            modelIds.add(modelId);
            weights.add(1); // Default weight
        }

        VariantsBlockStateSupplier blockstate = createLadderBlockstate(block, modelIds, weights);
        generator.blockStateCollector.accept(blockstate);
        generator.registerParentedItemModel(block, modelIds.get(0));
    }

    // ========================================
    // BlockDefinition Integration (block-models.md 5.6)
    // ========================================

    public static void registerCustomLadderBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        boolean tinted = definition.isTinted() || definition.hasColorMult();
        boolean isCustomModel = definition.hasCustomModel();

        if (definition.hasRandomTextures()) {
            List<BlockDefinition.RandomTextureVariant> randomTextures = definition.getRandomTextures();

            // If custom model, just reference existing models
            if (isCustomModel) {
                registerLadderBlockCustomModel(generator, block, definition.getBlockName(), randomTextures.size());
                return;
            }

            // Generate models with textures
            List<BlockDefinition.TextureVariantSet> textureSets = new ArrayList<>();
            for (BlockDefinition.RandomTextureVariant randomTexture : randomTextures) {
                List<String> textures = randomTexture.getTextures();
                int weight = randomTexture.getWeight();

                if (textures != null && !textures.isEmpty()) {
                    textureSets.add(new BlockDefinition.TextureVariantSet(textures.get(0), weight));
                } else {
                    textureSets.add(new BlockDefinition.TextureVariantSet("missingno", weight));
                }
            }
            registerLadderBlockWithRandomTextures(generator, block, tinted, textureSets);
        } else if (definition.getTextures() != null && !definition.getTextures().isEmpty()) {
            // Single texture case
            if (isCustomModel) {
                registerLadderBlockCustomModel(generator, block, definition.getBlockName(), 1);
            } else {
                registerLadderBlock(generator, block, tinted, definition.getTextures().get(0));
            }
        } else {
            // No textures
            if (isCustomModel) {
                registerLadderBlockCustomModel(generator, block, definition.getBlockName(), 1);
            } else {
                registerLadderBlock(generator, block, tinted, "missingno");
            }
        }
    }

}
