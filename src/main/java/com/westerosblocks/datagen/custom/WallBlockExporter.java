package com.westerosblocks.datagen.custom;

import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.datagen.ModModels;
import net.minecraft.block.enums.WallShape;
import net.minecraft.data.client.*;
import net.minecraft.block.Block;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

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

    // ========================================
    // Helper Methods (block-models.md 5.3-5.4)
    // ========================================

    private static MultipartBlockStateSupplier createWallVariants(Block block, List<Identifier> postModelIds,
                                                                    List<Identifier> sideModelIds, List<Identifier> tallModelIds,
                                                                    List<Integer> weights) {
        MultipartBlockStateSupplier supplier = MultipartBlockStateSupplier.create(block);

        // Single loop: for each texture variant, add post + all directional sides (low + tall)
        for (int i = 0; i < postModelIds.size(); i++) {
            // Add post model (when up=true)
            addPostVariant(supplier, postModelIds.get(i), weights.get(i));

            // Add side models for each direction (low and tall)
            addSideVariant(supplier, sideModelIds.get(i), weights.get(i), "north", WallShape.LOW);
            addSideVariant(supplier, tallModelIds.get(i), weights.get(i), "north", WallShape.TALL);
            addSideVariant(supplier, sideModelIds.get(i), weights.get(i), "east", WallShape.LOW);
            addSideVariant(supplier, tallModelIds.get(i), weights.get(i), "east", WallShape.TALL);
            addSideVariant(supplier, sideModelIds.get(i), weights.get(i), "south", WallShape.LOW);
            addSideVariant(supplier, tallModelIds.get(i), weights.get(i), "south", WallShape.TALL);
            addSideVariant(supplier, sideModelIds.get(i), weights.get(i), "west", WallShape.LOW);
            addSideVariant(supplier, tallModelIds.get(i), weights.get(i), "west", WallShape.TALL);
        }

        return supplier;
    }

    /**
     * Adds a single post variant.
     */
    private static void addPostVariant(MultipartBlockStateSupplier supplier, Identifier postModelId, int weight) {
        BlockStateVariant postVariant = BlockStateVariant.create()
                .put(VariantSettings.MODEL, postModelId);

        if (weight > 1) {
            postVariant = postVariant.put(VariantSettings.WEIGHT, weight);
        }

        supplier.with(When.create().set(Properties.UP, true), postVariant);
    }

    /**
     * Adds a single side variant for a specific direction and shape (low or tall).
     */
    private static void addSideVariant(MultipartBlockStateSupplier supplier, Identifier sideModelId,
                                       int weight, String direction, WallShape shape) {
        BlockStateVariant sideVariant = BlockStateVariant.create()
                .put(VariantSettings.MODEL, sideModelId)
                .put(VariantSettings.UVLOCK, true);

        int yRotation = switch (direction) {
            case "east" -> 90;
            case "south" -> 180;
            case "west" -> 270;
            default -> 0;
        };
        if (yRotation != 0) {
            sideVariant = sideVariant.put(VariantSettings.Y, toYRotation(yRotation));
        }

        if (weight > 1) {
            sideVariant = sideVariant.put(VariantSettings.WEIGHT, weight);
        }

        // Create condition based on direction and shape
        When condition = switch (direction) {
            case "north" -> When.create().set(Properties.NORTH_WALL_SHAPE, shape);
            case "east" -> When.create().set(Properties.EAST_WALL_SHAPE, shape);
            case "south" -> When.create().set(Properties.SOUTH_WALL_SHAPE, shape);
            case "west" -> When.create().set(Properties.WEST_WALL_SHAPE, shape);
            default -> throw new IllegalArgumentException("Unknown direction: " + direction);
        };

        supplier.with(condition, sideVariant);
    }

    // ========================================
    // Public Registration Methods (block-models.md 5.5)
    // ========================================

    public static void registerWallBlock(BlockStateModelGenerator generator, Block block, boolean tinted,
                                        boolean overlay, boolean isShort, String[] textures, String[] overlayTextures) {
        // Expand single texture to three if needed
        String[] expandedTextures = fillTextureArray(textures, 3);
        String[] expandedOverlays = overlay && overlayTextures != null ? fillTextureArray(overlayTextures, 3) : null;

        // Create texture map
        TextureMap textureMap = createFenceWallTextureMap(expandedTextures, expandedOverlays);

        // Upload post, side, and tall models
        Identifier postModelId = getWallPostModel(tinted, overlay)
                .upload(createNestedModelId(block, "post"), textureMap, generator.modelCollector);
        Identifier sideModelId = getWallSideModel(tinted, overlay, isShort)
                .upload(createNestedModelId(block, "side"), textureMap, generator.modelCollector);
        Identifier tallModelId = getWallSideTallModel(tinted, overlay)
                .upload(createNestedModelId(block, "side_tall"), textureMap, generator.modelCollector);

        // Create blockstate
        MultipartBlockStateSupplier blockstate = createWallVariants(block,
                List.of(postModelId), List.of(sideModelId), List.of(tallModelId), List.of(1));
        generator.blockStateCollector.accept(blockstate);

        // Register item model
        TextureMap itemTextureMap = new TextureMap()
                .put(TextureKey.WALL, createBlockIdentifier(expandedTextures[2])); // Use side texture
        Identifier itemModelId = ModelIds.getItemModelId(block.asItem());
        Models.WALL_INVENTORY.upload(itemModelId, itemTextureMap, generator.modelCollector);
    }

    public static void registerWallBlockWithRandomTextures(BlockStateModelGenerator generator, Block block, boolean tinted,
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

        // Create blockstate
        MultipartBlockStateSupplier blockstate = createWallVariants(block, postModelIds, sideModelIds, tallModelIds, weights);
        generator.blockStateCollector.accept(blockstate);

        // Register item model (using first texture set)
        BlockDefinition.TextureVariantSet firstSet = textureSets.get(0);
        String[] expandedTextures = fillTextureArray(firstSet.getTexturesAsArray(), 3);
        TextureMap itemTextureMap = new TextureMap()
                .put(TextureKey.WALL, createBlockIdentifier(expandedTextures[2])); // Use side texture
        Identifier itemModelId = ModelIds.getItemModelId(block.asItem());
        Models.WALL_INVENTORY.upload(itemModelId, itemTextureMap, generator.modelCollector);
    }

    // ========================================
    // BlockDefinition Integration (block-models.md 5.6)
    // ========================================

    public static void registerCustomWallBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        boolean tinted = definition.isTinted() || definition.hasColorMult();
        boolean overlay = definition.hasOverlayTextures();
        boolean isShort = "short".equals(definition.getWallSize());
        List<String> textureList = definition.getTextures();

        if (definition.hasRandomTextures()) {
            registerWallBlockWithRandomTextures(generator, block, tinted, overlay, isShort, extractTextureVariantSets(definition));
        } else if (textureList != null && !textureList.isEmpty()) {
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
