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

    public static void registerCustomCrossBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        boolean isTinted = definition.hasColorMult();
        boolean isLayerSensitive = definition.isLayerSensitive();
        String blockName = getBlockName(block);

        List<BlockDefinition.StateVariant> states = definition.getStates();
        if (states == null || states.isEmpty()) {
            return;
        }

        // Use rotateRandom flag (like old code), NOT texture count
        int rotationCount = definition.hasRotateRandom() ? 4 : 1;

        // Layer conditions array like old code: index 0 = layers=8, index 1 = layers=1, etc.
        int[] layerConds = isLayerSensitive ? new int[] {8, 1, 2, 3, 4, 5, 6, 7} : new int[] {0};

        Map<Integer, List<Identifier>> layerModelIds = new HashMap<>();
        String firstTexturePath = null;

        // Phase 1: Generate all models (like old code's doModelExports)
        for (int layerIdx = 0; layerIdx < layerConds.length; layerIdx++) {
            int layerValue = layerConds[layerIdx];
            List<Identifier> modelsForLayer = new ArrayList<>();

            for (int stateIdx = 0; stateIdx < states.size(); stateIdx++) {
                BlockDefinition.StateVariant state = states.get(stateIdx);
                String stateID = state.getStateID();
                String id = (stateID == null) ? "base" : stateID;

                // Old code: if (layer > 0) where layer is the INDEX, not the value
                // Index 0 = layers=8 (no suffix), Index 1+ = layers=1-7 (add suffix)
                if (layerIdx > 0) {
                    id = id + "_layer" + layerIdx;
                }

                // Loop over the random texture SETS (like old code)
                for (int setIdx = 0; setIdx < state.getRandomTextureSetCount(); setIdx++) {
                    BlockDefinition.RandomTextureVariant set = state.getRandomTextureSet(setIdx);
                    if (set == null) continue;

                    // Use only FIRST texture from the set (like old code: set.getTextureByIndex(0))
                    String texturePath = set.getTextureByIndex(0);
                    if (texturePath == null) continue;

                    // Track first texture for item model
                    if (firstTexturePath == null) {
                        firstTexturePath = texturePath;
                    }

                    Identifier modelId = WesterosBlocks.id("block/" + blockName + "/" + getModelName(id, setIdx));

                    // Determine parent template
                    String parentPath;
                    if (layerIdx > 0) {
                        // Indices 1-7 use cross_layer{1-7}
                        parentPath = isTinted ? "block/tinted/cross_layer" + layerIdx : "block/untinted/cross_layer" + layerIdx;
                    } else {
                        // Index 0 (layers=8) uses regular cross
                        parentPath = isTinted ? "block/tinted/cross" : "block/untinted/cross";
                    }

                    // Generate model
                    Identifier textureId = createBlockIdentifier(texturePath);
                    TextureMap textureMap = new TextureMap().put(TextureKey.CROSS, textureId);
                    Model model = new Model(Optional.of(WesterosBlocks.id(parentPath)), Optional.empty(), TextureKey.CROSS);
                    model.upload(modelId, textureMap, generator.modelCollector);

                    modelsForLayer.add(modelId);
                }
            }

            layerModelIds.put(layerValue, modelsForLayer);
        }

        // Phase 2: Generate blockstate (like old code's doBlockStateExport)
        if (isLayerSensitive) {
            BlockStateVariantMap.SingleProperty<Integer> layerMap = BlockStateVariantMap.create(Properties.LAYERS);

            for (int layerValue : layerConds) {
                List<BlockStateVariant> variants = new ArrayList<>();
                List<Identifier> modelIds = layerModelIds.get(layerValue);

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

                layerMap.register(layerValue, variants);
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

        // Register item model (like old code)
        if (firstTexturePath != null) {
            Identifier textureId = createBlockIdentifier(firstTexturePath);
            TextureMap itemTextures = new TextureMap().put(TextureKey.LAYER0, textureId);
            Models.GENERATED.upload(ModelIds.getItemModelId(block.asItem()), itemTextures, generator.modelCollector);
        }
    }

    private static String getModelName(String id, int setIdx) {
        return id + "_v" + (setIdx + 1);
    }
}
