package com.westerosblocks.datagen.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.utils.ModProperties;
import net.minecraft.data.client.*;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CropBlockExporter extends BaseBlockExporter {

    private static Model createCropStageModel(boolean tinted) {
        return createTintedModel(tinted, "crop", TextureKey.CROP);
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

        // Check if block has states
        if (definition.hasStates()) {
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
                registerCropBlockSimple(generator, block, tinted, textures);
            } else {
                WesterosBlocks.LOGGER.warn("Crop block '{}' has no states, textures, or random textures defined",
                        definition.getBlockName());
            }
        }
    }

    public static class StateTexture {
        public final String stateID;
        public final List<String> randomTextures;

        public StateTexture(String stateID, List<String> randomTextures) {
            this.stateID = stateID;
            this.randomTextures = randomTextures;
        }
    }
}
