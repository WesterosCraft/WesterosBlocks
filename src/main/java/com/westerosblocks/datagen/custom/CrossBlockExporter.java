package com.westerosblocks.datagen.custom;

import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import com.westerosblocks.WesterosBlocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CrossBlockExporter extends BaseBlockExporter {
    private static Identifier createCrossModel(BlockStateModelGenerator generator, Block block, String texturePath, boolean isTinted) {
        Identifier modelId = createModelId(block);
        return createCrossModelWithId(generator, modelId, texturePath, isTinted);
    }

    private static Identifier createCrossModelWithId(BlockStateModelGenerator generator, Identifier modelId, String texturePath, boolean isTinted) {
        String parentPath = isTinted ? "block/tinted/cross" : "block/untinted/cross";
        Identifier textureId = createBlockIdentifier(texturePath);

        TextureMap textureMap = new TextureMap()
            .put(TextureKey.CROSS, textureId);

        Model model = new Model(
            Optional.of(WesterosBlocks.id(parentPath)),
            Optional.empty(),
            TextureKey.CROSS
        );

        model.upload(modelId, textureMap, generator.modelCollector);
        return modelId;
    }

    private static int calculateRotationCount(BlockDefinition definition) {
        List<BlockDefinition.StateVariant> states = definition.getStates();
        if (states == null || states.isEmpty()) {
            return 1;
        }

        BlockDefinition.StateVariant state = states.getFirst();

        // Multiple texture sets = multiple variants
        if (state.getRandomTextureSetCount() > 1) {
            return 4;
        }

        // Single texture set with multiple textures = multiple variants
        if (state.getRandomTextureSetCount() == 1) {
            BlockDefinition.RandomTextureVariant set = state.getRandomTextureSet(0);
            if (set != null && set.getTextureCount() > 1) {
                return 4;
            }
        }

        return 1;
    }

    public static void registerCustomCrossBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        boolean isTinted = definition.hasColorMult();
        boolean isLayerSensitive = definition.isLayerSensitive();
        String blockName = getBlockName(block);

        List<BlockDefinition.StateVariant> states = definition.getStates();

        int rotationCount = calculateRotationCount(definition);
        int[] layers = isLayerSensitive ? new int[] {1, 2, 3, 4, 5, 6, 7, 8} : new int[] {0};

        Map<Integer, List<Identifier>> layerModelIds = new HashMap<>();
        String firstTexturePath = null;

        // Phase 1: Generate all models
        for (int layer : layers) {
            List<Identifier> modelsForLayer = new ArrayList<>();

            for (int stateIdx = 0; stateIdx < states.size(); stateIdx++) {
                BlockDefinition.StateVariant state = states.get(stateIdx);

                for (int setIdx = 0; setIdx < state.getRandomTextureSetCount(); setIdx++) {
                    BlockDefinition.RandomTextureVariant set = state.getRandomTextureSet(setIdx);
                    if (set == null) continue;

                    for (int textureIdx = 0; textureIdx < set.getTextureCount(); textureIdx++) {
                        String texturePath = set.getTextureByIndex(textureIdx);
                        if (texturePath == null) continue;

                        // Track first texture for item model
                        if (firstTexturePath == null) {
                            firstTexturePath = texturePath;
                        }

                        // Create model with appropriate parent template
                        Identifier modelId;
                        if (isLayerSensitive && layer > 0) {
                            // Layer-sensitive model
                            String layerSuffix = (layer == 8) ? "" : "_layer" + layer;
                            String textureSuffix = (set.getTextureCount() > 1) ? "_v" + (textureIdx + 1) : "";
                            modelId = WesterosBlocks.id("block/" + blockName + "/base" + layerSuffix + textureSuffix);

                            String parentPath;
                            if (layer == 8) {
                                parentPath = isTinted ? "block/tinted/cross" : "block/untinted/cross";
                            } else {
                                parentPath = isTinted ? "block/tinted/cross_layer" + layer : "block/untinted/cross_layer" + layer;
                            }

                            Identifier textureId = createBlockIdentifier(texturePath);
                            TextureMap textureMap = new TextureMap().put(TextureKey.CROSS, textureId);
                            Model model = new Model(Optional.of(WesterosBlocks.id(parentPath)), Optional.empty(), TextureKey.CROSS);
                            model.upload(modelId, textureMap, generator.modelCollector);
                        } else {
                            // Normal cross model (non-layer-sensitive)
                            if (set.getTextureCount() > 1) {
                                // Multiple textures - use nested model ID
                                modelId = WesterosBlocks.id("block/" + blockName + "/" + blockName + "_v" + (textureIdx + 1));
                                modelId = createCrossModelWithId(generator, modelId, texturePath, isTinted);
                            } else {
                                // Single texture
                                modelId = createCrossModel(generator, block, texturePath, isTinted);
                            }
                        }

                        modelsForLayer.add(modelId);
                    }
                }
            }

            layerModelIds.put(layer, modelsForLayer);
        }

        // Phase 2: Generate blockstate
        if (isLayerSensitive) {
            BlockStateVariantMap.SingleProperty<Integer> layerMap = BlockStateVariantMap.create(Properties.LAYERS);

            for (int layer : layers) {
                List<BlockStateVariant> variants = new ArrayList<>();
                List<Identifier> modelIds = layerModelIds.get(layer);

                for (Identifier modelId : modelIds) {
                    for (int rotation = 0; rotation < rotationCount; rotation++) {
                        BlockStateVariant variant = BlockStateVariant.create()
                            .put(VariantSettings.MODEL, modelId);

                        if (rotation > 0) {
                            variant = variant.put(VariantSettings.Y, VariantSettings.Rotation.valueOf("R" + (rotation * 90)));
                        }

                        variants.add(variant);
                    }
                }

                layerMap.register(layer, variants);
            }

            generator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(block).coordinate(layerMap)
            );
        } else {
            List<BlockStateVariant> variants = new ArrayList<>();
            List<Identifier> modelIds = layerModelIds.get(0);

            for (Identifier modelId : modelIds) {
                for (int rotation = 0; rotation < rotationCount; rotation++) {
                    BlockStateVariant variant = BlockStateVariant.create()
                        .put(VariantSettings.MODEL, modelId);

                    if (rotation > 0) {
                        variant = variant.put(VariantSettings.Y, VariantSettings.Rotation.valueOf("R" + (rotation * 90)));
                    }

                    variants.add(variant);
                }
            }

            generator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(block, variants.toArray(new BlockStateVariant[0]))
            );
        }

        // Register item model
        if (firstTexturePath != null) {
            Identifier textureId = createBlockIdentifier(firstTexturePath);
            TextureMap itemTextures = new TextureMap().put(TextureKey.LAYER0, textureId);
            Models.GENERATED.upload(ModelIds.getItemModelId(block.asItem()), itemTextures, generator.modelCollector);
        }
    }
}
