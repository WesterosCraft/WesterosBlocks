package com.westerosblocks.datagen.custom;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;
import com.westerosblocks.data.BlockDefinition;

import java.util.*;

public class CuboidBlockExporter extends BaseBlockExporter {

    public static void registerCustomCuboidBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        List<BlockDefinition.StateVariant> states = definition.getStates();
        if (states == null || states.isEmpty()) {
            throw new IllegalStateException("Block definition states should never be null/empty after doInit() for block: " + getBlockName(block));
        }

        int rotationCount = definition.hasRotateRandom() ? 4 : 1;
        boolean hasMultipleStates = definition.getStateCount() > 1;

        CuboidBlockExporter exporter = new CuboidBlockExporter();
        Map<String, List<Identifier>> stateModelMap = exporter.generateModelsReturnMap(generator, block, definition);

        if (stateModelMap.isEmpty()) {
            Identifier fallback = createNestedModelId(block, getModelName("base", 0));
            stateModelMap.put("base", List.of(fallback));
        }

        generateBlockState(generator, block, states, stateModelMap, hasMultipleStates, rotationCount);

        registerParentedItemModel(generator, block, stateModelMap.values().iterator().next().get(0));
    }

    private static void generateBlockState(BlockStateModelGenerator generator, Block block,
                                          List<BlockDefinition.StateVariant> states,
                                          Map<String, List<Identifier>> stateModelMap,
                                          boolean hasMultipleStates, int rotationCount) {
        if (hasMultipleStates && hasStateProperty(block)) {
            BlockStateVariantMap.SingleProperty<String> stateMap =
                BlockStateVariantMap.create(getStateProperty(block));

            for (BlockDefinition.StateVariant state : states) {
                String stateId = getStateIdOrBase(state.getStateID());
                List<Identifier> modelIds = stateModelMap.get(stateId);
                if (modelIds == null || modelIds.isEmpty()) continue;
                stateMap.register(stateId, buildVariants(state, modelIds, rotationCount));
            }

            generator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(block).coordinate(stateMap));
        } else {
            BlockDefinition.StateVariant state = states.get(0);
            List<Identifier> modelIds = stateModelMap.values().iterator().next();
            List<BlockStateVariant> variants = buildVariants(state, modelIds, rotationCount);

            generator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(block, variants.toArray(new BlockStateVariant[0])));
        }
    }

    /**
     * Returns the cuboids to use for model generation. Subclasses can override
     * to transform cuboids (e.g., rotate for NSEWUD blocks). Returns null to
     * use the default cuboids from the definition.
     */
    protected List<BlockDefinition.CuboidElement> getModelCuboids(BlockDefinition definition) {
        return null;
    }

    /**
     * Generates model files for every state × texture-set combination and returns
     * them keyed by state ID. Single-state blocks use {@code "base"} as the model
     * name prefix regardless of the state's actual ID; multi-state blocks use the
     * state ID. Uses {@link LinkedHashMap} so iteration order matches definition
     * order (needed for deterministic item-model selection).
     */
    protected Map<String, List<Identifier>> generateModelsReturnMap(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        List<BlockDefinition.StateVariant> states = definition.getStates();
        boolean hasMultipleStates = definition.getStateCount() > 1;

        Map<String, List<Identifier>> stateModelMap = new LinkedHashMap<>();

        for (BlockDefinition.StateVariant state : states) {
            String stateId = getStateIdOrBase(state.getStateID());
            String modelNamePrefix = hasMultipleStates ? stateId : "base";

            List<Identifier> modelIds = new ArrayList<>();
            state.forEachTextureSet((setIdx, set) -> {
                String variantName = getModelName(modelNamePrefix, setIdx);

                if (state.isCustomModel()) {
                    modelIds.add(createCustomModelId(block, variantName));
                    return;
                }
                if (set == null || set.getTextureCount() == 0) {
                    return;
                }

                List<String> textureList = new ArrayList<>(set.getTextures());

                Identifier modelId;
                if (CuboidModelBuilder.hasCuboids(definition) || state.hasCuboids()) {
                    Float rotation = state.getRotYOffset() != null
                            ? state.getRotYOffset().floatValue()
                            : null;
                    modelId = CuboidModelBuilder.createCuboidModel(generator, block, definition,
                            textureList, setIdx, variantName, rotation, getModelCuboids(definition));
                } else {
                    TextureMap textureMap = CuboidModelBuilder.standardCubeTextureMap(textureList);
                    Model parent = textureList.size() == 1 ? Models.CUBE_ALL : Models.CUBE;
                    modelId = parent.upload(createGeneratedModelId(block, variantName),
                            textureMap, generator.modelCollector);
                }
                modelIds.add(modelId);
            });

            if (!modelIds.isEmpty()) {
                stateModelMap.put(stateId, modelIds);
            }
        }

        return stateModelMap;
    }

    /**
     * Configuration for a single facing direction's rotation.
     */
    protected record FacingRotation(String facingValue, int yRot, int xRot) {
        FacingRotation(String facingValue, int yRot) {
            this(facingValue, yRot, 0);
        }
    }

    /**
     * Provider that returns facing rotations per state.
     * Allows per-state customization (e.g., different conventions for custom vs generated models).
     */
    @FunctionalInterface
    protected interface FacingRotationProvider {
        FacingRotation[] getRotations(BlockDefinition.StateVariant state);
    }

    /**
     * Shared blockstate generator for all directional cuboid exporters.
     * Builds facing variant JSON from a rotation configuration, handling:
     * - Single and multi-state blocks
     * - Weighted random texture variants
     * - Per-state rotYOffset
     * - X-axis rotation (for UP/DOWN directions)
     */
    protected static BlockStateSupplier generateFacingBlockState(
            Block block,
            Map<String, List<Identifier>> stateModelMap,
            List<BlockDefinition.StateVariant> states,
            boolean hasMultipleStates,
            FacingRotationProvider rotationProvider) {
        return new BlockStateSupplier() {
            @Override
            public Block getBlock() {
                return block;
            }

            @Override
            public JsonElement get() {
                JsonObject json = new JsonObject();
                JsonObject variants = new JsonObject();

                for (BlockDefinition.StateVariant state : states) {
                    String stateId = state.getStateID() != null ? state.getStateID() : "base";
                    List<Identifier> modelIds = stateModelMap.get(stateId);
                    if (modelIds == null || modelIds.isEmpty()) continue;

                    int rotYOffset = 0;
                    if (state.getRotYOffset() != null) {
                        rotYOffset = state.getRotYOffset().intValue();
                    }

                    FacingRotation[] rotations = rotationProvider.getRotations(state);
                    List<BlockDefinition.RandomTextureVariant> randomTextures = state.getRandomTextures();
                    boolean hasWeights = randomTextures != null && !randomTextures.isEmpty() && modelIds.size() > 1;

                    for (FacingRotation fr : rotations) {
                        String key = hasMultipleStates
                                ? "facing=" + fr.facingValue() + ",state=" + stateId
                                : "facing=" + fr.facingValue();

                        if (hasWeights) {
                            JsonArray variantArray = new JsonArray();
                            for (int i = 0; i < modelIds.size() && i < randomTextures.size(); i++) {
                                int weight = randomTextures.get(i).getWeight();
                                for (int w = 0; w < weight; w++) {
                                    variantArray.add(buildVariantJson(modelIds.get(i), fr, rotYOffset));
                                }
                            }
                            variants.add(key, variantArray);
                        } else {
                            variants.add(key, buildVariantJson(modelIds.get(0), fr, rotYOffset));
                        }
                    }
                }

                json.add("variants", variants);
                return json;
            }
        };
    }

    /**
     * Builds a single variant JSON object with model, y rotation, and optional x rotation.
     */
    private static JsonObject buildVariantJson(Identifier modelId, FacingRotation fr, int rotYOffset) {
        JsonObject variant = new JsonObject();
        variant.addProperty("model", modelId.toString());
        int yRot = (fr.yRot() + rotYOffset) % 360;
        if (yRot > 0) variant.addProperty("y", yRot);
        if (fr.xRot() != 0) variant.addProperty("x", fr.xRot());
        return variant;
    }

}
