package com.westerosblocks.datagen.custom;

import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.datagen.ModModels;
import net.minecraft.block.Block;
import net.minecraft.client.data.*;
import net.minecraft.client.render.model.json.WeightedVariant;
import net.minecraft.client.render.model.json.ModelVariant;
import net.minecraft.state.property.Properties;

import net.minecraft.util.Identifier;
import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.utils.ModProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CrossBlockExporter extends BaseBlockExporter {

    public static void registerCustomCrossBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        boolean isTinted = definition.hasColorMult();
        boolean isLayerSensitive = definition.isLayerSensitive();
        int rotationCount = definition.hasRotateRandom() ? 4 : 1;

        List<BlockDefinition.StateVariant> states = definition.getStates();
        if (states == null || states.isEmpty()) {
            return;
        }

        // Phase 1: Generate blockstate (matches old doBlockStateExport)
        generateBlockState(generator, block, states, isLayerSensitive, rotationCount);

        // Phase 2: Generate models (matches old doModelExports)
        String firstTexture = generateModels(generator, block, states, isLayerSensitive, isTinted);

        // Phase 3: Item model
        if (firstTexture != null) {
            Identifier textureId = createBlockIdentifier(firstTexture);
            TextureMap itemTextures = new TextureMap().put(TextureKey.LAYER0, textureId);
            Models.GENERATED.upload(ModelIds.getItemModelId(block.asItem()), itemTextures, generator.modelCollector);
        } else {
            // All states are custom models - use first custom model as item parent
            for (BlockDefinition.StateVariant state : states) {
                if (state.isCustomModel()) {
                    String stateID = getStateIdOrBase(state.getStateID());
                    Identifier customModelId = createCustomModelId(block, getModelName(stateID, 0));
                    registerParentedItemModel(generator, block, customModelId);
                    break;
                }
            }
        }
    }

    /**
     * Phase 1: Generate blockstate JSON
     * Matches old doBlockStateExport pattern with state tracking
     */
    private static void generateBlockState(BlockStateModelGenerator generator, Block block,
                                          List<BlockDefinition.StateVariant> states,
                                          boolean isLayerSensitive, int rotationCount) {
        int[] layerConds = isLayerSensitive ? new int[] {8, 1, 2, 3, 4, 5, 6, 7} : new int[] {0};

        boolean useStateMap = states.size() > 1
            && states.stream().allMatch(s -> s.getStateID() != null)
            && hasStateProperty(block);

        if (useStateMap && !isLayerSensitive) {
            ModProperties.StateProperty stateProperty = getStateProperty(block);

            BlockStateVariantMap.SingleProperty<WeightedVariant, String> stateMap =
            BlockStateVariantMap.models(stateProperty);

            for (BlockDefinition.StateVariant state : states) {
                String stateID = getStateIdOrBase(state.getStateID());
                List<WeightedVariant> variants = new ArrayList<>();

                int textureSetCount = state.getRandomTextureSetCount();
                if (textureSetCount == 0) {
                    if (state.isCustomModel()) textureSetCount = 1;
                    else continue;
                }

                for (int setIdx = 0; setIdx < textureSetCount; setIdx++) {
                    BlockDefinition.RandomTextureVariant set = state.getRandomTextureSet(setIdx);
                    int weight = (set != null) ? set.getWeight() : 1;
                    Identifier modelId = state.isCustomModel()
                        ? createCustomModelId(block, getModelName(stateID, setIdx))
                        : createNestedModelId(block, getModelName(stateID, setIdx));

                    for (int rot = 0; rot < rotationCount; rot++) {
                        WeightedVariant variant = createWeightedVariant(modelId, rot * 90, weight);
                        variants.add(variant);
                    }
                }

                stateMap.register(stateID, mergeVariants(variants));
            }

            generator.blockStateCollector.accept(
                VariantsBlockModelDefinitionCreator.of(block).with(stateMap)
            );
        }
        else if (isLayerSensitive) {
            BlockStateVariantMap.SingleProperty<WeightedVariant, Integer> layerMap =
            BlockStateVariantMap.models(Properties.LAYERS);

            for (int layerIdx = 0; layerIdx < layerConds.length; layerIdx++) {
                int layerValue = layerConds[layerIdx];
                List<WeightedVariant> variants = new ArrayList<>();

                for (BlockDefinition.StateVariant state : states) {
                    String stateID = state.getStateID();
                    String id = getStateIdOrBase(stateID);
                    if (layerIdx > 0) {
                        id = id + "_layer" + layerIdx;
                    }

                    int textureSetCount = state.getRandomTextureSetCount();
                    if (textureSetCount == 0) {
                        if (state.isCustomModel()) textureSetCount = 1;
                        else continue;
                    }

                    for (int setIdx = 0; setIdx < textureSetCount; setIdx++) {
                        BlockDefinition.RandomTextureVariant set = state.getRandomTextureSet(setIdx);
                        int weight = (set != null) ? set.getWeight() : 1;
                        Identifier modelId = state.isCustomModel()
                            ? createCustomModelId(block, getModelName(id, setIdx))
                            : createNestedModelId(block, getModelName(id, setIdx));

                        for (int rot = 0; rot < rotationCount; rot++) {
                            WeightedVariant variant = createWeightedVariant(modelId, rot * 90, weight);
                            variants.add(variant);
                        }
                    }
                }

                layerMap.register(layerValue, mergeVariants(variants));
            }

            generator.blockStateCollector.accept(
                VariantsBlockModelDefinitionCreator.of(block).with(layerMap)
            );
        }
        else {
            // Single-state block: Simple variant list
            List<WeightedVariant> variants = new ArrayList<>();

            for (BlockDefinition.StateVariant state : states) {
                String stateID = state.getStateID();
                String id = getStateIdOrBase(stateID);

                int textureSetCount = state.getRandomTextureSetCount();
                if (textureSetCount == 0) {
                    if (state.isCustomModel()) textureSetCount = 1;
                    else continue;
                }

                for (int setIdx = 0; setIdx < textureSetCount; setIdx++) {
                    BlockDefinition.RandomTextureVariant set = state.getRandomTextureSet(setIdx);
                    int weight = (set != null) ? set.getWeight() : 1;
                    Identifier modelId = state.isCustomModel()
                        ? createCustomModelId(block, getModelName(id, setIdx))
                        : createNestedModelId(block, getModelName(id, setIdx));

                    for (int rot = 0; rot < rotationCount; rot++) {
                        WeightedVariant variant = createWeightedVariant(modelId, rot * 90, weight);
                        variants.add(variant);
                    }
                }
            }

            generator.blockStateCollector.accept(
                VariantsBlockModelDefinitionCreator.of(block, mergeVariants(variants))
            );
        }
    }

    /**
     * Phase 2: Generate model files
     * Matches old doModelExports pattern
     */
    private static String generateModels(BlockStateModelGenerator generator, Block block,
                                        List<BlockDefinition.StateVariant> states,
                                        boolean isLayerSensitive, boolean isTinted) {
        int[] layerConds = isLayerSensitive ? new int[] {8, 1, 2, 3, 4, 5, 6, 7} : new int[] {0};
        String firstTexture = null;

        for (int layerIdx = 0; layerIdx < layerConds.length; layerIdx++) {
            for (BlockDefinition.StateVariant state : states) {
                if (state.isCustomModel()) continue;

                String stateID = state.getStateID();
                String id = getStateIdOrBase(stateID);
                if (layerIdx > 0) {
                    id = id + "_layer" + layerIdx;
                }

                for (int setIdx = 0; setIdx < state.getRandomTextureSetCount(); setIdx++) {
                    BlockDefinition.RandomTextureVariant set = state.getRandomTextureSet(setIdx);
                    if (set == null) continue;

                    String texturePath = set.getTextureByIndex(0);
                    if (texturePath == null) continue;

                    if (firstTexture == null) {
                        firstTexture = texturePath;
                    }

                    // Generate model
                    Identifier modelId = createNestedModelId(block, getModelName(id, setIdx));
                    Identifier textureId = createBlockIdentifier(texturePath);
                    TextureMap textureMap = new TextureMap()
                            .put(TextureKey.CROSS, textureId)
                            .put(TextureKey.PARTICLE, textureId);

                    Model model;
                    if (layerIdx > 0) {
                        String parentPath = isTinted ? "block/tinted/cross_layer" + layerIdx : "block/untinted/cross_layer" + layerIdx;
                        model = new Model(Optional.of(WesterosBlocks.id(parentPath)), Optional.empty(), TextureKey.CROSS, TextureKey.PARTICLE);
                    } else {
                        model = isTinted ? ModModels.CROSS_TINTED : ModModels.CROSS_UNTINTED;
                    }
                    model.upload(modelId, textureMap, generator.modelCollector);
                }
            }
        }

        return firstTexture;
    }

}
