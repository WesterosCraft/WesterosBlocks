package com.westerosblocks.datagen.custom;

import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Properties;

import net.minecraft.util.Identifier;
import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.utils.ModProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * CrossBlockExporter - Handles cross-shaped blocks (web blocks, plants with layers)
 *
 * Follows three-phase pattern from EXPORTER_PATTERN.md:
 * 1. Generate blockstate JSON (with state tracking)
 * 2. Generate model files
 * 3. Register item model
 */
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
        }
    }

    /**
     * Phase 1: Generate blockstate JSON
     * Matches old doBlockStateExport pattern with state tracking
     */
    private static void generateBlockState(BlockStateModelGenerator generator, Block block,
                                          List<BlockDefinition.StateVariant> states,
                                          boolean isLayerSensitive, int rotationCount) {
        String blockName = getBlockName(block);
        int[] layerConds = isLayerSensitive ? new int[] {8, 1, 2, 3, 4, 5, 6, 7} : new int[] {0};

        // Check if we have multiple states with IDs (like cobweb, smoke)
        // Only use state map if: multiple states AND all have non-null IDs AND block has STATE property
        boolean useStateMap = states.size() > 1
            && states.stream().allMatch(s -> s.getStateID() != null)
            && hasStateProperty(block);

        if (useStateMap && !isLayerSensitive) {
            // Multi-state block: Create separate entries for each state
            // Matches old: so.addVariant(cond, var, Collections.singleton(rec.stateID))

            // Get the STATE property from the block instance (not the static default one)
            ModProperties.StateProperty stateProperty = getStateProperty(block);
            if (stateProperty == null) {
                // Fallback to simple variant list if property not found
                generateSimpleVariants(generator, block, states, rotationCount);
                return;
            }

            BlockStateVariantMap.SingleProperty<String> stateMap =
                BlockStateVariantMap.create(stateProperty);

            for (BlockDefinition.StateVariant state : states) {
                String stateID = (state.getStateID() == null) ? "base" : state.getStateID();
                List<BlockStateVariant> variants = new ArrayList<>();

                for (int setIdx = 0; setIdx < state.getRandomTextureSetCount(); setIdx++) {
                    BlockDefinition.RandomTextureVariant set = state.getRandomTextureSet(setIdx);
                    if (set == null) continue;

                    int weight = set.getWeight();
                    Identifier modelId = createNestedModelId(block, getModelName(stateID, setIdx));

                    for (int rot = 0; rot < rotationCount; rot++) {
                        BlockStateVariant variant = createWeightedVariant(modelId, rot * 90, weight);
                        variants.add(variant);
                    }
                }

                stateMap.register(stateID, variants);
            }

            generator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(block).coordinate(stateMap)
            );
        }
        else if (isLayerSensitive) {
            // Layer-sensitive block: Create entries per layer value
            BlockStateVariantMap.SingleProperty<Integer> layerMap =
                BlockStateVariantMap.create(Properties.LAYERS);

            for (int layerIdx = 0; layerIdx < layerConds.length; layerIdx++) {
                int layerValue = layerConds[layerIdx];
                List<BlockStateVariant> variants = new ArrayList<>();

                for (BlockDefinition.StateVariant state : states) {
                    String stateID = state.getStateID();
                    String id = (stateID == null) ? "base" : stateID;
                    if (layerIdx > 0) {
                        id = id + "_layer" + layerIdx;
                    }

                    for (int setIdx = 0; setIdx < state.getRandomTextureSetCount(); setIdx++) {
                        BlockDefinition.RandomTextureVariant set = state.getRandomTextureSet(setIdx);
                        if (set == null) continue;

                        int weight = set.getWeight();
                        Identifier modelId = createNestedModelId(block, getModelName(id, setIdx));

                        for (int rot = 0; rot < rotationCount; rot++) {
                            BlockStateVariant variant = createWeightedVariant(modelId, rot * 90, weight);
                            variants.add(variant);
                        }
                    }
                }

                layerMap.register(layerValue, variants);
            }

            generator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(block).coordinate(layerMap)
            );
        }
        else {
            // Single-state block: Simple variant list
            List<BlockStateVariant> variants = new ArrayList<>();

            for (BlockDefinition.StateVariant state : states) {
                String stateID = state.getStateID();
                String id = (stateID == null) ? "base" : stateID;

                for (int setIdx = 0; setIdx < state.getRandomTextureSetCount(); setIdx++) {
                    BlockDefinition.RandomTextureVariant set = state.getRandomTextureSet(setIdx);
                    if (set == null) continue;

                    int weight = set.getWeight();
                    Identifier modelId = createNestedModelId(block, getModelName(id, setIdx));

                    for (int rot = 0; rot < rotationCount; rot++) {
                        BlockStateVariant variant = createWeightedVariant(modelId, rot * 90, weight);
                        variants.add(variant);
                    }
                }
            }

            generator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(block, variants.toArray(new BlockStateVariant[0]))
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
        String blockName = getBlockName(block);
        int[] layerConds = isLayerSensitive ? new int[] {8, 1, 2, 3, 4, 5, 6, 7} : new int[] {0};
        String firstTexture = null;

        for (int layerIdx = 0; layerIdx < layerConds.length; layerIdx++) {
            for (BlockDefinition.StateVariant state : states) {
                if (state.isCustomModel()) continue;

                String stateID = state.getStateID();
                String id = (stateID == null) ? "base" : stateID;
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

                    // Determine parent template
                    String parentPath;
                    if (layerIdx > 0) {
                        parentPath = isTinted ? "block/tinted/cross_layer" + layerIdx : "block/untinted/cross_layer" + layerIdx;
                    } else {
                        parentPath = isTinted ? "block/tinted/cross" : "block/untinted/cross";
                    }

                    // Generate model
                    Identifier modelId = createNestedModelId(block, getModelName(id, setIdx));
                    Identifier textureId = createBlockIdentifier(texturePath);
                    TextureMap textureMap = new TextureMap().put(TextureKey.CROSS, textureId);
                    Model model = new Model(Optional.of(WesterosBlocks.id(parentPath)), Optional.empty(), TextureKey.CROSS);
                    model.upload(modelId, textureMap, generator.modelCollector);
                }
            }
        }

        return firstTexture;
    }

    private static String getModelName(String id, int setIdx) {
        return id + "_v" + (setIdx + 1);
    }

    private static boolean hasStateProperty(Block block) {
        for (var property : block.getStateManager().getProperties()) {
            if (property.getName().equals("state")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the STATE property from a block instance.
     * This retrieves the actual property with the correct state values,
     * not the static ModProperties.STATE default.
     */
    private static ModProperties.StateProperty getStateProperty(Block block) {
        for (var property : block.getStateManager().getProperties()) {
            if (property instanceof ModProperties.StateProperty stateProperty) {
                return stateProperty;
            }
        }
        return null;
    }

    /**
     * Fallback method to generate simple variants (no state map).
     * Used when state property is not properly configured.
     */
    private static void generateSimpleVariants(BlockStateModelGenerator generator, Block block,
                                              List<BlockDefinition.StateVariant> states, int rotationCount) {
        List<BlockStateVariant> variants = new ArrayList<>();

        for (BlockDefinition.StateVariant state : states) {
            String stateID = state.getStateID();
            String id = (stateID == null) ? "base" : stateID;

            for (int setIdx = 0; setIdx < state.getRandomTextureSetCount(); setIdx++) {
                BlockDefinition.RandomTextureVariant set = state.getRandomTextureSet(setIdx);
                if (set == null) continue;

                int weight = set.getWeight();
                Identifier modelId = createNestedModelId(block, getModelName(id, setIdx));

                for (int rot = 0; rot < rotationCount; rot++) {
                    BlockStateVariant variant = createWeightedVariant(modelId, rot * 90, weight);
                    variants.add(variant);
                }
            }
        }

        generator.blockStateCollector.accept(
            VariantsBlockStateSupplier.create(block, variants.toArray(new BlockStateVariant[0]))
        );
    }
}
