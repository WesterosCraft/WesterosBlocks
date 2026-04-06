package com.westerosblocks.datagen.custom;

import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.datagen.ModModels;
import com.westerosblocks.utils.ModProperties;
import net.minecraft.block.enums.WallShape;
import net.minecraft.client.data.*;
import net.minecraft.client.render.model.json.ModelVariantOperator;
import net.minecraft.client.render.model.json.MultipartModelConditionBuilder;
import net.minecraft.client.render.model.json.WeightedVariant;
import net.minecraft.client.render.model.json.ModelVariant;
import net.minecraft.block.Block;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WallBlockExporter extends BaseBlockExporter {

    private static Model getWallPostModel(boolean tinted, boolean overlay) {
        if (overlay) return tinted ? ModModels.WALL_POST_OVERLAY_TINTED : ModModels.WALL_POST_OVERLAY_UNTINTED;
        return tinted ? ModModels.WALL_POST_TINTED : ModModels.WALL_POST_UNTINTED;
    }

    private static Model getWallSideModel(boolean tinted, boolean overlay, boolean isShort) {
        if (isShort) {
            if (overlay) return tinted ? ModModels.WALL_SIDE_SHORT_OVERLAY_TINTED : ModModels.WALL_SIDE_SHORT_OVERLAY_UNTINTED;
            return tinted ? ModModels.WALL_SIDE_SHORT_TINTED : ModModels.WALL_SIDE_SHORT_UNTINTED;
        }
        if (overlay) return tinted ? ModModels.WALL_SIDE_OVERLAY_TINTED : ModModels.WALL_SIDE_OVERLAY_UNTINTED;
        return tinted ? ModModels.WALL_SIDE_TINTED : ModModels.WALL_SIDE_UNTINTED;
    }

    private static Model getWallSideTallModel(boolean tinted, boolean overlay) {
        if (overlay) return tinted ? ModModels.WALL_SIDE_TALL_OVERLAY_TINTED : ModModels.WALL_SIDE_TALL_OVERLAY_UNTINTED;
        return tinted ? ModModels.WALL_SIDE_TALL_TINTED : ModModels.WALL_SIDE_TALL_UNTINTED;
    }

    private record WallModelSet(List<Identifier> postModelIds, List<Identifier> sideModelIds,
                                List<Identifier> tallModelIds, List<Integer> weights) {}

    // ========================================
    // Helper Methods
    // ========================================

    /**
     * Creates multipart blockstate supplier for wall blocks without states.
     */
    private static MultipartBlockModelDefinitionCreator createWallVariants(Block block, List<Identifier> postModelIds,
                                                                    List<Identifier> sideModelIds, List<Identifier> tallModelIds,
                                                                    List<Integer> weights) {
        MultipartBlockModelDefinitionCreator supplier = MultipartBlockModelDefinitionCreator.create(block);
        addWallEntries(supplier, postModelIds, sideModelIds, tallModelIds, weights, null, null);
        return supplier;
    }

    /**
     * Creates multipart blockstate supplier for wall blocks with state property.
     */
    private static MultipartBlockModelDefinitionCreator createWallVariantsWithStates(Block block,
            Map<String, WallModelSet> stateModels, ModProperties.StateProperty stateProperty) {
        MultipartBlockModelDefinitionCreator supplier = MultipartBlockModelDefinitionCreator.create(block);

        for (Map.Entry<String, WallModelSet> entry : stateModels.entrySet()) {
            String stateId = entry.getKey();
            WallModelSet models = entry.getValue();
            addWallEntries(supplier, models.postModelIds, models.sideModelIds, models.tallModelIds,
                    models.weights, stateProperty, stateId);
        }

        return supplier;
    }

    /**
     * Adds wall post and side entries to the multipart supplier.
     * If stateProperty and stateId are non-null, adds state condition to when clauses.
     */
    private static void addWallEntries(MultipartBlockModelDefinitionCreator supplier, List<Identifier> postModelIds,
            List<Identifier> sideModelIds, List<Identifier> tallModelIds, List<Integer> weights,
            ModProperties.StateProperty stateProperty, String stateId) {
        for (int i = 0; i < postModelIds.size(); i++) {
            addPostVariant(supplier, postModelIds.get(i), weights.get(i), stateProperty, stateId);

            addSideVariant(supplier, sideModelIds.get(i), weights.get(i), "north", WallShape.LOW, stateProperty, stateId);
            addSideVariant(supplier, tallModelIds.get(i), weights.get(i), "north", WallShape.TALL, stateProperty, stateId);
            addSideVariant(supplier, sideModelIds.get(i), weights.get(i), "east", WallShape.LOW, stateProperty, stateId);
            addSideVariant(supplier, tallModelIds.get(i), weights.get(i), "east", WallShape.TALL, stateProperty, stateId);
            addSideVariant(supplier, sideModelIds.get(i), weights.get(i), "south", WallShape.LOW, stateProperty, stateId);
            addSideVariant(supplier, tallModelIds.get(i), weights.get(i), "south", WallShape.TALL, stateProperty, stateId);
            addSideVariant(supplier, sideModelIds.get(i), weights.get(i), "west", WallShape.LOW, stateProperty, stateId);
            addSideVariant(supplier, tallModelIds.get(i), weights.get(i), "west", WallShape.TALL, stateProperty, stateId);
        }
    }

    /**
     * Adds a single post variant, optionally with state condition.
     */
    private static void addPostVariant(MultipartBlockModelDefinitionCreator supplier, Identifier postModelId, int weight,
                                       ModProperties.StateProperty stateProperty, String stateId) {
        WeightedVariant postVariant = BlockStateModelGenerator.createWeightedVariant(postModelId);

        if (weight > 1) {
        }

        MultipartModelConditionBuilder condition = new MultipartModelConditionBuilder().put(Properties.UP, true);
        if (stateProperty != null && stateId != null) {
            condition.set(stateProperty, stateId);
        }

        supplier.with(condition, postVariant);
    }

    /**
     * Adds a single side variant for a specific direction and shape (low or tall),
     * optionally with state condition.
     */
    private static void addSideVariant(MultipartBlockModelDefinitionCreator supplier, Identifier sideModelId,
                                       int weight, String direction, WallShape shape,
                                       ModProperties.StateProperty stateProperty, String stateId) {
        WeightedVariant sideVariant = BlockStateModelGenerator.createWeightedVariant(sideModelId)
                .withUVLock(true);

        int yRotation = switch (direction) {
            case "east" -> 90;
            case "south" -> 180;
            case "west" -> 270;
            default -> 0;
        };
        if (yRotation != 0) {
            sideVariant = sideVariant.put(ModelVariantOperator.ROTATION_Y, toYRotation(yRotation));
        }

        if (weight > 1) {
        }

        MultipartModelConditionBuilder condition = switch (direction) {
            case "north" -> new MultipartModelConditionBuilder().put(Properties.NORTH_WALL_SHAPE, shape);
            case "east" -> new MultipartModelConditionBuilder().put(Properties.EAST_WALL_SHAPE, shape);
            case "south" -> new MultipartModelConditionBuilder().put(Properties.SOUTH_WALL_SHAPE, shape);
            case "west" -> new MultipartModelConditionBuilder().put(Properties.WEST_WALL_SHAPE, shape);
            default -> throw new IllegalArgumentException("Unknown direction: " + direction);
        };

        if (stateProperty != null && stateId != null) {
            condition.set(stateProperty, stateId);
        }

        supplier.with(condition, sideVariant);
    }

    /**
     * Uploads wall post, side, and tall models for a given set of random textures.
     */
    private static WallModelSet uploadWallModels(BlockStateModelGenerator generator, Block block,
            boolean tinted, boolean overlay, boolean isShort, String modelPrefix,
            List<BlockDefinition.RandomTextureVariant> randomSets, List<String> overlayTextures) {
        List<Identifier> postModelIds = new ArrayList<>();
        List<Identifier> sideModelIds = new ArrayList<>();
        List<Identifier> tallModelIds = new ArrayList<>();
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
            String tallName = modelPrefix + "side_tall_v" + (i + 1);

            Identifier postModelId = getWallPostModel(tinted, overlay)
                    .upload(createNestedModelId(block, postName), textureMap, generator.modelCollector);
            Identifier sideModelId = getWallSideModel(tinted, overlay, isShort)
                    .upload(createNestedModelId(block, sideName), textureMap, generator.modelCollector);
            Identifier tallModelId = getWallSideTallModel(tinted, overlay)
                    .upload(createNestedModelId(block, tallName), textureMap, generator.modelCollector);

            postModelIds.add(postModelId);
            sideModelIds.add(sideModelId);
            tallModelIds.add(tallModelId);
            weights.add(set.getWeight());
        }

        return new WallModelSet(postModelIds, sideModelIds, tallModelIds, weights);
    }

    // ========================================
    // Public Registration Methods
    // ========================================

    public static void registerCustomWallBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        boolean tinted = definition.isTinted() || definition.hasColorMult();
        boolean isShort = "short".equals(definition.getWallSize());

        var states = definition.getStates();
        ModProperties.StateProperty stateProperty = getStateProperty(block);
        boolean hasMultipleStates = stateProperty != null && states != null && states.size() > 1;

        if (hasMultipleStates) {
            // Multi-state wall: generate per-state models and state-aware blockstate
            Map<String, WallModelSet> stateModels = new LinkedHashMap<>();
            String firstSideTexture = null;

            for (BlockDefinition.StateVariant state : states) {
                String stateId = getStateIdOrBase(state.getStateID());
                boolean stateOverlay = state.hasOverlayTextures();
                String modelPrefix = stateId + "/";

                // After doInit(), randomTextures is always populated
                List<BlockDefinition.RandomTextureVariant> randomSets = new ArrayList<>();
                for (int i = 0; i < state.getRandomTextureSetCount(); i++) {
                    randomSets.add(state.getRandomTextureSet(i));
                }

                if (randomSets.isEmpty()) continue;

                WallModelSet modelSet = uploadWallModels(generator, block, tinted, stateOverlay, isShort,
                        modelPrefix, randomSets, state.getOverlayTextures());
                stateModels.put(state.getStateID(), modelSet);

                // Capture first state's side texture for item model
                if (firstSideTexture == null) {
                    BlockDefinition.RandomTextureVariant firstSet = randomSets.get(0);
                    String[] textures = new String[firstSet.getTextureCount()];
                    for (int t = 0; t < firstSet.getTextureCount(); t++) {
                        textures[t] = firstSet.getTextureByIndex(t);
                    }
                    String[] expanded = fillTextureArray(textures, 3);
                    firstSideTexture = expanded[2]; // side texture
                }
            }

            if (!stateModels.isEmpty()) {
                MultipartBlockModelDefinitionCreator blockstate = createWallVariantsWithStates(block, stateModels, stateProperty);
                generator.blockStateCollector.accept(blockstate);

                // Item model from first state's side texture
                TextureMap itemTextureMap = new TextureMap()
                        .put(TextureKey.WALL, createBlockIdentifier(firstSideTexture));
                Identifier itemModelId = ModelIds.getItemModelId(block.asItem());
                Models.WALL_INVENTORY.upload(itemModelId, itemTextureMap, generator.modelCollector);
            }
        } else {
            // Single-state wall: existing behavior
            boolean overlay = definition.hasOverlayTextures();
            if (definition.hasRandomTextures()) {
                registerWallBlockWithRandomTextures(generator, block, tinted, overlay, isShort, extractTextureVariantSets(definition));
            } else {
                List<String> textureList = definition.getTextures();
                if (textureList != null && !textureList.isEmpty()) {
                    String[] textures = textureList.toArray(new String[0]);
                    String[] overlays = overlay && definition.getOverlayTextures() != null
                            ? definition.getOverlayTextures().toArray(new String[0])
                            : null;
                    registerWallBlock(generator, block, tinted, overlay, isShort, textures, overlays);
                } else {
                    registerWallBlock(generator, block, tinted, overlay, isShort, new String[]{"missingno"}, null);
                }
            }
        }
    }

    private static void registerWallBlock(BlockStateModelGenerator generator, Block block, boolean tinted,
                                        boolean overlay, boolean isShort, String[] textures, String[] overlayTextures) {
        String[] expandedTextures = fillTextureArray(textures, 3);
        String[] expandedOverlays = overlay && overlayTextures != null ? fillTextureArray(overlayTextures, 3) : null;

        TextureMap textureMap = createFenceWallTextureMap(expandedTextures, expandedOverlays);

        Identifier postModelId = getWallPostModel(tinted, overlay)
                .upload(createNestedModelId(block, "post"), textureMap, generator.modelCollector);
        Identifier sideModelId = getWallSideModel(tinted, overlay, isShort)
                .upload(createNestedModelId(block, "side"), textureMap, generator.modelCollector);
        Identifier tallModelId = getWallSideTallModel(tinted, overlay)
                .upload(createNestedModelId(block, "side_tall"), textureMap, generator.modelCollector);

        MultipartBlockModelDefinitionCreator blockstate = createWallVariants(block,
                List.of(postModelId), List.of(sideModelId), List.of(tallModelId), List.of(1));
        generator.blockStateCollector.accept(blockstate);

        TextureMap itemTextureMap = new TextureMap()
                .put(TextureKey.WALL, createBlockIdentifier(expandedTextures[2]));
        Identifier itemModelId = ModelIds.getItemModelId(block.asItem());
        Models.WALL_INVENTORY.upload(itemModelId, itemTextureMap, generator.modelCollector);
    }

    private static void registerWallBlockWithRandomTextures(BlockStateModelGenerator generator, Block block, boolean tinted,
                                                           boolean overlay, boolean isShort, List<BlockDefinition.TextureVariantSet> textureSets) {
        List<Identifier> postModelIds = new ArrayList<>();
        List<Identifier> sideModelIds = new ArrayList<>();
        List<Identifier> tallModelIds = new ArrayList<>();
        List<Integer> weights = new ArrayList<>();

        for (int i = 0; i < textureSets.size(); i++) {
            BlockDefinition.TextureVariantSet set = textureSets.get(i);
            String[] expandedTextures = fillTextureArray(set.getTexturesAsArray(), 3);
            String[] expandedOverlays = overlay && set.hasOverlay() ? fillTextureArray(set.getOverlayTexturesAsArray(), 3) : null;

            TextureMap textureMap = createFenceWallTextureMap(expandedTextures, expandedOverlays);

            Identifier postModelId = getWallPostModel(tinted, overlay)
                    .upload(createNestedModelId(block, "post_v" + (i + 1)), textureMap, generator.modelCollector);
            Identifier sideModelId = getWallSideModel(tinted, overlay, isShort)
                    .upload(createNestedModelId(block, "side_v" + (i + 1)), textureMap, generator.modelCollector);
            Identifier tallModelId = getWallSideTallModel(tinted, overlay)
                    .upload(createNestedModelId(block, "side_tall_v" + (i + 1)), textureMap, generator.modelCollector);

            postModelIds.add(postModelId);
            sideModelIds.add(sideModelId);
            tallModelIds.add(tallModelId);
            weights.add(set.weight);
        }

        MultipartBlockModelDefinitionCreator blockstate = createWallVariants(block, postModelIds, sideModelIds, tallModelIds, weights);
        generator.blockStateCollector.accept(blockstate);

        BlockDefinition.TextureVariantSet firstSet = textureSets.get(0);
        String[] expandedTextures = fillTextureArray(firstSet.getTexturesAsArray(), 3);
        TextureMap itemTextureMap = new TextureMap()
                .put(TextureKey.WALL, createBlockIdentifier(expandedTextures[2]));
        Identifier itemModelId = ModelIds.getItemModelId(block.asItem());
        Models.WALL_INVENTORY.upload(itemModelId, itemTextureMap, generator.modelCollector);
    }
}
