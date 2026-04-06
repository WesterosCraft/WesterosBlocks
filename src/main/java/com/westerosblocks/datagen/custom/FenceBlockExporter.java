package com.westerosblocks.datagen.custom;

import com.westerosblocks.datagen.ModModels;
import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.utils.ModProperties;
import net.minecraft.client.data.*;
import net.minecraft.client.render.model.json.ModelVariantOperator;
import net.minecraft.client.render.model.json.MultipartModelConditionBuilder;
import net.minecraft.client.render.model.json.WeightedVariant;
import net.minecraft.client.render.model.json.ModelVariant;
import net.minecraft.block.Block;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FenceBlockExporter extends BaseBlockExporter {

    private static Model getFencePostModel(boolean tinted, boolean overlay) {
        if (overlay) return tinted ? ModModels.FENCE_POST_OVERLAY_TINTED : ModModels.FENCE_POST_OVERLAY_UNTINTED;
        return tinted ? ModModels.FENCE_POST_TINTED : ModModels.FENCE_POST_UNTINTED;
    }

    private static Model getFenceSideModel(boolean tinted, boolean overlay) {
        if (overlay) return tinted ? ModModels.FENCE_SIDE_OVERLAY_TINTED : ModModels.FENCE_SIDE_OVERLAY_UNTINTED;
        return tinted ? ModModels.FENCE_SIDE_TINTED : ModModels.FENCE_SIDE_UNTINTED;
    }

    private static Model getFenceInventoryModel(boolean tinted, boolean overlay) {
        if (overlay) return tinted ? ModModels.FENCE_INVENTORY_OVERLAY_TINTED : ModModels.FENCE_INVENTORY_OVERLAY_UNTINTED;
        return tinted ? ModModels.FENCE_INVENTORY_TINTED : ModModels.FENCE_INVENTORY_UNTINTED;
    }

    private record FenceModelSet(List<Identifier> postModelIds, List<Identifier> sideModelIds, List<Integer> weights) {}

    /**
     * Creates multipart blockstate supplier for fence blocks without states.
     */
    private static MultipartBlockModelDefinitionCreator createFenceVariants(Block block, List<Identifier> postModelIds,
                                                                    List<Identifier> sideModelIds, List<Integer> weights) {
        MultipartBlockModelDefinitionCreator supplier = MultipartBlockModelDefinitionCreator.create(block);
        addFenceEntries(supplier, postModelIds, sideModelIds, weights, null, null);
        return supplier;
    }

    /**
     * Creates multipart blockstate supplier for fence blocks with state property.
     */
    private static MultipartBlockModelDefinitionCreator createFenceVariantsWithStates(Block block,
            Map<String, FenceModelSet> stateModels, ModProperties.StateProperty stateProperty) {
        MultipartBlockModelDefinitionCreator supplier = MultipartBlockModelDefinitionCreator.create(block);

        for (Map.Entry<String, FenceModelSet> entry : stateModels.entrySet()) {
            String stateId = entry.getKey();
            FenceModelSet models = entry.getValue();
            addFenceEntries(supplier, models.postModelIds, models.sideModelIds, models.weights, stateProperty, stateId);
        }

        return supplier;
    }

    /**
     * Adds fence post and side entries to the multipart supplier.
     * If stateProperty and stateId are non-null, adds state condition to when clauses.
     */
    private static void addFenceEntries(MultipartBlockModelDefinitionCreator supplier, List<Identifier> postModelIds,
            List<Identifier> sideModelIds, List<Integer> weights,
            ModProperties.StateProperty stateProperty, String stateId) {
        for (int i = 0; i < postModelIds.size(); i++) {
            // Add post model
            WeightedVariant postVariant = createWeightedVariant(postModelIds.get(i), 0, weights.get(i));

            if (stateProperty != null && stateId != null) {
                supplier.with(new MultipartModelConditionBuilder().put(stateProperty, stateId), postVariant);
            } else {
                supplier.with(postVariant);
            }

            // Add side models for each direction
            addSideVariant(supplier, sideModelIds.get(i), weights.get(i), Direction.NORTH, stateProperty, stateId);
            addSideVariant(supplier, sideModelIds.get(i), weights.get(i), Direction.EAST, stateProperty, stateId);
            addSideVariant(supplier, sideModelIds.get(i), weights.get(i), Direction.SOUTH, stateProperty, stateId);
            addSideVariant(supplier, sideModelIds.get(i), weights.get(i), Direction.WEST, stateProperty, stateId);
        }
    }

    /**
     * Adds a single side variant for a specific direction, optionally with state condition.
     */
    private static void addSideVariant(MultipartBlockModelDefinitionCreator supplier, Identifier sideModelId,
                                       int weight, Direction direction,
                                       ModProperties.StateProperty stateProperty, String stateId) {
        ModelVariant mv = new ModelVariant(sideModelId).withUVLock(true);
        int yRotation = getRotationForDirection(direction);
        if (yRotation != 0) {
            mv = mv.withRotationY(toYRotation(yRotation));
        }
        WeightedVariant sideVariant = weight > 1
            ? new WeightedVariant(net.minecraft.util.collection.Pool.builder().add(mv, weight).build())
            : BlockStateModelGenerator.createWeightedVariant(mv);

        MultipartModelConditionBuilder condition = switch (direction) {
            case NORTH -> new MultipartModelConditionBuilder().put(Properties.NORTH, true);
            case EAST -> new MultipartModelConditionBuilder().put(Properties.EAST, true);
            case SOUTH -> new MultipartModelConditionBuilder().put(Properties.SOUTH, true);
            case WEST -> new MultipartModelConditionBuilder().put(Properties.WEST, true);
            default -> null;
        };

        if (condition != null && stateProperty != null && stateId != null) {
            condition.put(stateProperty, stateId);
        }

        supplier.with(condition, sideVariant);
    }

    /**
     * Uploads fence post and side models for a given texture set.
     */
    private static FenceModelSet uploadFenceModels(BlockStateModelGenerator generator, Block block,
            boolean tinted, boolean overlay, String modelPrefix, List<BlockDefinition.RandomTextureVariant> randomSets,
            List<String> overlayTextures) {
        List<Identifier> postModelIds = new ArrayList<>();
        List<Identifier> sideModelIds = new ArrayList<>();
        List<Integer> weights = new ArrayList<>();

        for (int i = 0; i < randomSets.size(); i++) {
            BlockDefinition.RandomTextureVariant set = randomSets.get(i);
            String[] textures = new String[set.getTextureCount()];
            for (int t = 0; t < set.getTextureCount(); t++) {
                textures[t] = set.getTextureByIndex(t);
            }
            String[] expandedTextures = fillTextureArray(textures, 3);

            String[] expandedOverlays = null;
            if (overlay && overlayTextures != null && !overlayTextures.isEmpty()) {
                expandedOverlays = fillTextureArray(overlayTextures.toArray(new String[0]), 3);
            }

            TextureMap textureMap = createFenceWallTextureMap(expandedTextures, expandedOverlays);

            String postName = modelPrefix + "post_v" + (i + 1);
            String sideName = modelPrefix + "side_v" + (i + 1);

            Identifier postModelId = getFencePostModel(tinted, overlay)
                    .upload(createNestedModelId(block, postName), textureMap, generator.modelCollector);
            Identifier sideModelId = getFenceSideModel(tinted, overlay)
                    .upload(createNestedModelId(block, sideName), textureMap, generator.modelCollector);

            postModelIds.add(postModelId);
            sideModelIds.add(sideModelId);
            weights.add(set.getWeight());
        }

        return new FenceModelSet(postModelIds, sideModelIds, weights);
    }

    public static void registerCustomFenceBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        boolean tinted = definition.isTinted() || definition.hasColorMult();

        var states = definition.getStates();
        ModProperties.StateProperty stateProperty = getStateProperty(block);
        boolean hasMultipleStates = stateProperty != null && states != null && states.size() > 1;

        if (hasMultipleStates) {
            // Multi-state fence: generate per-state models and state-aware blockstate
            Map<String, FenceModelSet> stateModels = new LinkedHashMap<>();
            TextureMap firstItemTextureMap = null;

            for (BlockDefinition.StateVariant state : states) {
                String stateId = getStateIdOrBase(state.getStateID());
                boolean stateOverlay = state.hasOverlayTextures();
                boolean stateTinted = tinted;
                String modelPrefix = stateId + "/";

                // After doInit(), randomTextures is always populated from textures
                List<BlockDefinition.RandomTextureVariant> randomSets = new ArrayList<>();
                for (int i = 0; i < state.getRandomTextureSetCount(); i++) {
                    randomSets.add(state.getRandomTextureSet(i));
                }

                if (randomSets.isEmpty()) continue;

                FenceModelSet modelSet = uploadFenceModels(generator, block, stateTinted, stateOverlay,
                        modelPrefix, randomSets, state.getOverlayTextures());
                stateModels.put(state.getStateID(), modelSet);

                // Capture first state's textures for item model
                if (firstItemTextureMap == null) {
                    BlockDefinition.RandomTextureVariant firstSet = randomSets.get(0);
                    String[] textures = new String[firstSet.getTextureCount()];
                    for (int t = 0; t < firstSet.getTextureCount(); t++) {
                        textures[t] = firstSet.getTextureByIndex(t);
                    }
                    String[] expanded = fillTextureArray(textures, 3);
                    String[] expandedOverlays = null;
                    if (stateOverlay && state.getOverlayTextures() != null) {
                        expandedOverlays = fillTextureArray(state.getOverlayTextures().toArray(new String[0]), 3);
                    }
                    firstItemTextureMap = createFenceWallTextureMap(expanded, expandedOverlays);
                }
            }

            if (!stateModels.isEmpty()) {
                MultipartBlockModelDefinitionCreator blockstate = createFenceVariantsWithStates(block, stateModels, stateProperty);
                generator.blockStateCollector.accept(blockstate);

                // Item model from first state
                boolean firstOverlay = states.get(0).hasOverlayTextures();
                Identifier itemModelId = Identifier.of("westerosblocks", "item/" + getBlockName(block));
                getFenceInventoryModel(tinted, firstOverlay)
                        .upload(itemModelId, firstItemTextureMap, generator.modelCollector);
            }
        } else {
            // Single-state fence: existing behavior
            boolean overlay = definition.hasOverlayTextures();
            if (definition.hasRandomTextures()) {
                registerFenceBlockWithRandomTextures(generator, block, tinted, overlay, extractTextureVariantSets(definition));
            } else {
                List<String> textureList = definition.getTextures();
                if (textureList != null && !textureList.isEmpty()) {
                    String[] textures = textureList.toArray(new String[0]);
                    String[] overlays = overlay && definition.getOverlayTextures() != null
                            ? definition.getOverlayTextures().toArray(new String[0])
                            : null;
                    registerFenceBlock(generator, block, tinted, overlay, textures, overlays);
                } else {
                    registerFenceBlock(generator, block, tinted, overlay, new String[]{"missingno"}, null);
                }
            }
        }
    }

    private static void registerFenceBlock(BlockStateModelGenerator generator, Block block, boolean tinted,
                                         boolean overlay, String[] textures, String[] overlayTextures) {
        String[] expandedTextures = fillTextureArray(textures, 3);
        String[] expandedOverlays = overlay && overlayTextures != null ? fillTextureArray(overlayTextures, 3) : null;

        TextureMap textureMap = createFenceWallTextureMap(expandedTextures, expandedOverlays);

        Identifier postModelId = getFencePostModel(tinted, overlay)
                .upload(createNestedModelId(block, "post"), textureMap, generator.modelCollector);
        Identifier sideModelId = getFenceSideModel(tinted, overlay)
                .upload(createNestedModelId(block, "side"), textureMap, generator.modelCollector);

        MultipartBlockModelDefinitionCreator blockstate = createFenceVariants(block,
                List.of(postModelId), List.of(sideModelId), List.of(1));
        generator.blockStateCollector.accept(blockstate);

        Identifier itemModelId = Identifier.of("westerosblocks", "item/" + getBlockName(block));
        getFenceInventoryModel(tinted, overlay)
                .upload(itemModelId, textureMap, generator.modelCollector);
    }

    private static void registerFenceBlockWithRandomTextures(BlockStateModelGenerator generator, Block block, boolean tinted,
                                                           boolean overlay, List<BlockDefinition.TextureVariantSet> textureSets) {
        List<Identifier> postModelIds = new ArrayList<>();
        List<Identifier> sideModelIds = new ArrayList<>();
        List<Integer> weights = new ArrayList<>();

        for (int i = 0; i < textureSets.size(); i++) {
            BlockDefinition.TextureVariantSet set = textureSets.get(i);
            String[] expandedTextures = fillTextureArray(set.getTexturesAsArray(), 3);
            String[] expandedOverlays = overlay && set.hasOverlay() ? fillTextureArray(set.getOverlayTexturesAsArray(), 3) : null;

            TextureMap textureMap = createFenceWallTextureMap(expandedTextures, expandedOverlays);

            Identifier postModelId = getFencePostModel(tinted, overlay)
                    .upload(createNestedModelId(block, "post_v" + (i + 1)), textureMap, generator.modelCollector);
            Identifier sideModelId = getFenceSideModel(tinted, overlay)
                    .upload(createNestedModelId(block, "side_v" + (i + 1)), textureMap, generator.modelCollector);

            postModelIds.add(postModelId);
            sideModelIds.add(sideModelId);
            weights.add(set.weight);
        }

        MultipartBlockModelDefinitionCreator blockstate = createFenceVariants(block, postModelIds, sideModelIds, weights);
        generator.blockStateCollector.accept(blockstate);

        BlockDefinition.TextureVariantSet firstSet = textureSets.get(0);
        String[] expandedTextures = fillTextureArray(firstSet.getTexturesAsArray(), 3);
        String[] expandedOverlays = overlay && firstSet.hasOverlay() ? fillTextureArray(firstSet.getOverlayTexturesAsArray(), 3) : null;
        TextureMap itemTextureMap = createFenceWallTextureMap(expandedTextures, expandedOverlays);

        Identifier itemModelId = Identifier.of("westerosblocks", "item/" + getBlockName(block));
        getFenceInventoryModel(tinted, overlay)
                .upload(itemModelId, itemTextureMap, generator.modelCollector);
    }
}
