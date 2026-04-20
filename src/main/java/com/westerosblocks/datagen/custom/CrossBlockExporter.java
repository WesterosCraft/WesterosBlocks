package com.westerosblocks.datagen.custom;

import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.datagen.ModModels;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Properties;

import net.minecraft.util.Identifier;
import com.westerosblocks.WesterosBlocks;

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

    private static void generateBlockState(BlockStateModelGenerator generator, Block block,
                                          List<BlockDefinition.StateVariant> states,
                                          boolean isLayerSensitive, int rotationCount) {
        boolean useStateMap = states.size() > 1
            && states.stream().allMatch(s -> s.getStateID() != null)
            && hasStateProperty(block);

        if (useStateMap && !isLayerSensitive) {
            BlockStateVariantMap.SingleProperty<String> stateMap =
                BlockStateVariantMap.create(getStateProperty(block));

            for (BlockDefinition.StateVariant state : states) {
                String stateID = getStateIdOrBase(state.getStateID());
                List<BlockStateVariant> variants = buildStateVariants(block, state, stateID, rotationCount);
                if (variants.isEmpty()) continue;
                stateMap.register(stateID, variants);
            }

            generator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(block).coordinate(stateMap));
            return;
        }

        if (isLayerSensitive) {
            int[] layerConds = {8, 1, 2, 3, 4, 5, 6, 7};
            BlockStateVariantMap.SingleProperty<Integer> layerMap =
                BlockStateVariantMap.create(Properties.LAYERS);

            for (int layerIdx = 0; layerIdx < layerConds.length; layerIdx++) {
                List<BlockStateVariant> variants = new ArrayList<>();
                for (BlockDefinition.StateVariant state : states) {
                    String id = getStateIdOrBase(state.getStateID());
                    if (layerIdx > 0) id = id + "_layer" + layerIdx;
                    variants.addAll(buildStateVariants(block, state, id, rotationCount));
                }
                layerMap.register(layerConds[layerIdx], variants);
            }

            generator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(block).coordinate(layerMap));
            return;
        }

        List<BlockStateVariant> variants = new ArrayList<>();
        for (BlockDefinition.StateVariant state : states) {
            String id = getStateIdOrBase(state.getStateID());
            variants.addAll(buildStateVariants(block, state, id, rotationCount));
        }
        generator.blockStateCollector.accept(
            VariantsBlockStateSupplier.create(block, variants.toArray(new BlockStateVariant[0])));
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
