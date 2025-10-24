package com.westerosblocks.datagen.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.utils.ModProperties;
import net.minecraft.data.client.*;
import net.minecraft.block.Block;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CropBlockExporter extends BaseBlockExporter {

    private static Model createCropStageModel(boolean tinted) {
        return createCropStageModel(tinted, "");
    }

    private static TextureMap createCropTextureMap(String texture) {
        return new TextureMap().put(TextureKey.CROP, createBlockIdentifier(texture));
    }

    /**
     * Registers a crop block with state-based variants using custom STATE property.
     */
    public static void registerCropBlock(BlockStateModelGenerator generator, Block block, boolean tinted,
                                        List<StateTexture> stateTextures) {
        ModProperties.StateProperty blockStateProperty = null;
        for (var property : block.getStateManager().getProperties()) {
            if (property instanceof ModProperties.StateProperty stateProperty && "state".equals(property.getName())) {
                blockStateProperty = stateProperty;
                break;
            }
        }

        if (blockStateProperty == null) {
            throw new IllegalStateException("Block " + block + " does not have a STATE property defined");
        }

        List<String> stateIDs = stateTextures.stream().map(st -> st.stateID).toList();
        Collection<String> blockStateValues = blockStateProperty.getValues();

        for (String stateID : stateIDs) {
            if (!blockStateValues.contains(stateID)) {
                throw new IllegalStateException("State '" + stateID + "' is not defined in block's STATE property");
            }
        }

        BlockStateVariantMap.SingleProperty<String> variantMap = BlockStateVariantMap.create(blockStateProperty);
        List<Identifier> modelIds = new ArrayList<>();

        for (StateTexture stateTexture : stateTextures) {
            if (stateTexture.randomTextures.size() > 1) {
                // Multiple random texture variants
                List<Identifier> stateModelIds = new ArrayList<>();
                for (int j = 0; j < stateTexture.randomTextures.size(); j++) {
                    String texture = stateTexture.randomTextures.get(j);
                    TextureMap textureMap = createCropTextureMap(texture);
                    Identifier modelId = uploadModel(createCropStageModel(tinted), block,
                                                    stateTexture.stateID + "_v" + (j + 1),
                                                    textureMap, generator.modelCollector);
                    stateModelIds.add(modelId);
                }

                List<BlockStateVariant> variants = stateModelIds.stream()
                        .map(modelId -> BlockStateVariant.create().put(VariantSettings.MODEL, modelId))
                        .collect(Collectors.toList());
                variantMap.register(stateTexture.stateID, variants);
                modelIds.addAll(stateModelIds);
            } else {
                // Single texture
                String texture = stateTexture.randomTextures.get(0);
                TextureMap textureMap = createCropTextureMap(texture);
                Identifier modelId = uploadModel(createCropStageModel(tinted), block, stateTexture.stateID,
                                                textureMap, generator.modelCollector);
                modelIds.add(modelId);
                variantMap.register(stateTexture.stateID, createVariant(modelId));
            }
        }

        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block).coordinate(variantMap));
        registerParentedItemModel(generator, block, modelIds.get(0));
    }

    /**
     * Registers a simple crop block with random texture variants (no state property).
     */
    public static void registerCropBlockSimple(BlockStateModelGenerator generator, Block block, boolean tinted,
                                              List<String> textures) {
        List<Identifier> modelIds = new ArrayList<>();

        for (int i = 0; i < textures.size(); i++) {
            TextureMap textureMap = createCropTextureMap(textures.get(i));
            Identifier modelId = uploadModel(createCropStageModel(tinted), block, "v" + (i + 1),
                                            textureMap, generator.modelCollector);
            modelIds.add(modelId);
        }

        List<BlockStateVariant> variants = modelIds.stream()
                .map(BaseBlockExporter::createVariant)
                .collect(Collectors.toList());

        generator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(block, variants.toArray(new BlockStateVariant[0])));
        registerParentedItemModel(generator, block, modelIds.get(0));
    }

    public static void registerCustomCropBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        boolean tinted = definition.isTinted() || definition.hasColorMult();
        boolean layerSensitive = definition.isLayerSensitive();

        // Check if block has multiple actual states (not just synthetic base state)
        boolean hasMultipleStates = definition.getStateCount() > 1;

        if (hasMultipleStates) {
            if (layerSensitive) {
                // Layer-sensitive with states
                registerCropBlockLayerSensitiveWithStates(generator, block, definition, tinted);
            } else {
                // Regular crop with states
                List<StateTexture> stateTextures = new ArrayList<>();

                for (var stateDefinition : definition.getStates()) {
                    String stateID = stateDefinition.getStateID();
                    List<String> textures = new ArrayList<>();

                    // Check if this state has randomTextures instead of regular textures
                    if ((stateDefinition.getTextures() == null || stateDefinition.getTextures().isEmpty())
                            && stateDefinition.hasRandomTextures()) {
                        // Extract textures from randomTextures array
                        for (var randomVariant : stateDefinition.getRandomTextures()) {
                            if (randomVariant.getTextures() != null && !randomVariant.getTextures().isEmpty()) {
                                textures.addAll(randomVariant.getTextures());
                            }
                        }
                    } else if (stateDefinition.getTextures() != null && !stateDefinition.getTextures().isEmpty()) {
                        textures.addAll(stateDefinition.getTextures());
                    }

                    if (!textures.isEmpty()) {
                        stateTextures.add(new StateTexture(stateID, textures));
                    } else {
                        WesterosBlocks.LOGGER.warn("Crop block '{}' state '{}' has no textures",
                                definition.getBlockName(), stateID);
                    }
                }

                if (!stateTextures.isEmpty()) {
                    registerCropBlock(generator, block, tinted, stateTextures);
                }
            }
        } else {
            // No states defined, check for root-level textures or randomTextures
            List<String> textures = new ArrayList<>();

            // Check regular textures first
            if (definition.getTextures() != null && !definition.getTextures().isEmpty()) {
                textures.addAll(definition.getTextures());
            }

            // Check randomTextures at root level (like seagrass)
            if (textures.isEmpty() && definition.hasRandomTextures()) {
                for (var randomVariant : definition.getRandomTextures()) {
                    if (randomVariant.getTextures() != null && !randomVariant.getTextures().isEmpty()) {
                        textures.addAll(randomVariant.getTextures());
                    }
                }
            }

            if (!textures.isEmpty()) {
                if (layerSensitive) {
                    // Layer-sensitive without states (like seagrass)
                    registerCropBlockLayerSensitiveSimple(generator, block, definition, tinted, textures);
                } else {
                    registerCropBlockSimple(generator, block, tinted, textures);
                }
            } else {
                WesterosBlocks.LOGGER.warn("Crop block '{}' has no states, textures, or random textures defined",
                        definition.getBlockName());
            }
        }
    }

    /**
     * Registers a layer-sensitive crop block without states (like seagrass).
     * Generates models for layers 1-8, each with random texture variants.
     */
    private static void registerCropBlockLayerSensitiveSimple(BlockStateModelGenerator generator, Block block,
                                                              BlockDefinition definition, boolean tinted, List<String> textures) {
        // Layers: 8 (full), 1-7 (partial)
        int[] layers = {8, 1, 2, 3, 4, 5, 6, 7};

        List<Identifier> allModelIds = new ArrayList<>();
        BlockStateVariantMap.SingleProperty<Integer> layerMap = BlockStateVariantMap.create(Properties.LAYERS);

        for (int layer : layers) {
            String layerSuffix = (layer == 8) ? "" : "_layer" + layer;
            String parentSuffix = (layer == 8) ? "" : "_layer" + layer;
            Model cropModel = createCropStageModel(tinted, parentSuffix);

            List<Identifier> layerModelIds = new ArrayList<>();

            for (int i = 0; i < textures.size(); i++) {
                TextureMap textureMap = createCropTextureMap(textures.get(i));
                Identifier modelId = uploadModel(cropModel, block, "base" + layerSuffix + "_v" + (i + 1),
                                                textureMap, generator.modelCollector);
                layerModelIds.add(modelId);
                if (allModelIds.isEmpty()) allModelIds.add(modelId);  // Keep first for item model
            }

            // Create variants for this layer
            List<BlockStateVariant> variants = layerModelIds.stream()
                    .map(BaseBlockExporter::createVariant)
                    .collect(Collectors.toList());

            // Register this layer's variants
            layerMap.register(layer, variants);
        }

        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block).coordinate(layerMap));
        registerParentedItemModel(generator, block, allModelIds.get(0));
    }

    /**
     * Registers a layer-sensitive crop block with states.
     * Generates models for each state × layer combination.
     * For example: "layers=1,state=age0", "layers=2,state=age0", etc.
     */
    private static void registerCropBlockLayerSensitiveWithStates(BlockStateModelGenerator generator, Block block,
                                                                   BlockDefinition definition, boolean tinted) {
        // Layers: 8 (full), 1-7 (partial)
        int[] layers = {8, 1, 2, 3, 4, 5, 6, 7};

        // Get the STATE property from the block
        ModProperties.StateProperty blockStateProperty = null;
        for (var property : block.getStateManager().getProperties()) {
            if (property instanceof ModProperties.StateProperty stateProperty && "state".equals(property.getName())) {
                blockStateProperty = stateProperty;
                break;
            }
        }

        if (blockStateProperty == null) {
            throw new IllegalStateException("Block " + block + " does not have a STATE property defined");
        }

        List<Identifier> allModelIds = new ArrayList<>();

        // Build variant map with both LAYERS and STATE properties
        BlockStateVariantMap.DoubleProperty<Integer, String> layerStateMap =
            BlockStateVariantMap.create(Properties.LAYERS, blockStateProperty);

        for (int layer : layers) {
            String layerSuffix = (layer == 8) ? "" : "_layer" + layer;
            String parentSuffix = (layer == 8) ? "" : "_layer" + layer;
            Model cropModel = createCropStageModel(tinted, parentSuffix);

            // Loop through each state
            for (var stateDefinition : definition.getStates()) {
                String stateID = stateDefinition.getStateID();
                List<String> textures = new ArrayList<>();

                // Extract textures from this state
                if ((stateDefinition.getTextures() == null || stateDefinition.getTextures().isEmpty())
                        && stateDefinition.hasRandomTextures()) {
                    for (var randomVariant : stateDefinition.getRandomTextures()) {
                        if (randomVariant.getTextures() != null && !randomVariant.getTextures().isEmpty()) {
                            textures.addAll(randomVariant.getTextures());
                        }
                    }
                } else if (stateDefinition.getTextures() != null && !stateDefinition.getTextures().isEmpty()) {
                    textures.addAll(stateDefinition.getTextures());
                }

                if (textures.isEmpty()) {
                    WesterosBlocks.LOGGER.warn("Layer-sensitive crop block '{}' state '{}' has no textures",
                            definition.getBlockName(), stateID);
                    continue;
                }

                List<Identifier> stateModelIds = new ArrayList<>();

                for (int i = 0; i < textures.size(); i++) {
                    TextureMap textureMap = createCropTextureMap(textures.get(i));
                    String modelName = stateID + layerSuffix + "_v" + (i + 1);
                    Identifier modelId = uploadModel(cropModel, block, modelName, textureMap, generator.modelCollector);
                    stateModelIds.add(modelId);
                    if (allModelIds.isEmpty()) allModelIds.add(modelId);
                }

                // Create variants for this layer + state combination
                List<BlockStateVariant> variants = stateModelIds.stream()
                        .map(BaseBlockExporter::createVariant)
                        .collect(Collectors.toList());

                // Register this layer + state combination
                layerStateMap.register(layer, stateID, variants);
            }
        }

        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block).coordinate(layerStateMap));
        registerParentedItemModel(generator, block, allModelIds.get(0));
    }

    public static class StateTexture {
        public final String stateID;
        public final List<String> randomTextures;

        public StateTexture(String stateID, List<String> randomTextures) {
            this.stateID = stateID;
            this.randomTextures = randomTextures;
        }
    }

    private static Model createCropStageModel(boolean tinted, String layerSuffix) {
        String parent = (tinted ? "westerosblocks:block/tinted/crop" : "westerosblocks:block/untinted/crop") + layerSuffix;
        return new Model(java.util.Optional.of(Identifier.of(parent)), java.util.Optional.empty(), TextureKey.CROP);
    }
}
