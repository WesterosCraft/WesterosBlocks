package com.westerosblocks.datagen.custom;

import com.westerosblocks.datagen.ModModels;
import com.westerosblocks.datagen.ModTextureMap;
import com.westerosblocks.data.BlockDefinition;
import net.minecraft.data.client.*;
import net.minecraft.block.Block;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.List;

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

    private static TextureMap createFenceTextureMap(String[] textures, String[] overlayTextures) {
        if (overlayTextures != null) {
            return ModTextureMap.fenceWallOverlayTextures(
                    textures[0], textures[1], textures[2],
                    overlayTextures[0], overlayTextures[1], overlayTextures[2]);
        }
        return ModTextureMap.fenceWallTextures(textures[0], textures[1], textures[2]);
    }

    /**
     * Creates multipart blockstate supplier for fence blocks.
     * Follows block-models.md section 5.4: Custom BlockStateSupplier Method.
     * Each variant is added individually as a separate multipart entry.
     */
    private static MultipartBlockStateSupplier createFenceVariants(Block block, List<Identifier> postModelIds,
                                                                    List<Identifier> sideModelIds, List<Integer> weights) {
        MultipartBlockStateSupplier supplier = MultipartBlockStateSupplier.create(block);

        // Single loop: for each texture variant, add post + all directional sides
        for (int i = 0; i < postModelIds.size(); i++) {
            // Add post model (no condition - always present)
            BlockStateVariant postVariant = BlockStateVariant.create()
                    .put(VariantSettings.MODEL, postModelIds.get(i));
            if (weights.get(i) > 1) {
                postVariant = postVariant.put(VariantSettings.WEIGHT, weights.get(i));
            }
            supplier.with(postVariant);

            // Add side models for each direction
            addSideVariant(supplier, sideModelIds.get(i), weights.get(i), Direction.NORTH);
            addSideVariant(supplier, sideModelIds.get(i), weights.get(i), Direction.EAST);
            addSideVariant(supplier, sideModelIds.get(i), weights.get(i), Direction.SOUTH);
            addSideVariant(supplier, sideModelIds.get(i), weights.get(i), Direction.WEST);
        }

        return supplier;
    }

    /**
     * Adds a single side variant for a specific direction.
     */
    private static void addSideVariant(MultipartBlockStateSupplier supplier, Identifier sideModelId,
                                       int weight, Direction direction) {
        BlockStateVariant sideVariant = BlockStateVariant.create()
                .put(VariantSettings.MODEL, sideModelId)
                .put(VariantSettings.UVLOCK, true);

        int yRotation = getRotationForDirection(direction);
        if (yRotation != 0) {
            sideVariant = sideVariant.put(VariantSettings.Y, toYRotation(yRotation));
        }

        if (weight > 1) {
            sideVariant = sideVariant.put(VariantSettings.WEIGHT, weight);
        }

        // Add condition for when this side should be rendered
        When condition = switch (direction) {
            case NORTH -> When.create().set(Properties.NORTH, true);
            case EAST -> When.create().set(Properties.EAST, true);
            case SOUTH -> When.create().set(Properties.SOUTH, true);
            case WEST -> When.create().set(Properties.WEST, true);
            default -> null;
        };

        supplier.with(condition, sideVariant);
    }

    public static void registerFenceBlock(BlockStateModelGenerator generator, Block block, boolean tinted,
                                         boolean overlay, String[] textures, String[] overlayTextures) {
        // Expand single texture to three if needed
        String[] expandedTextures = fillTextureArray(textures, 3);
        String[] expandedOverlays = overlay && overlayTextures != null ? fillTextureArray(overlayTextures, 3) : null;

        // Create texture map
        TextureMap textureMap = createFenceTextureMap(expandedTextures, expandedOverlays);

        // Upload post and side models
        Identifier postModelId = getFencePostModel(tinted, overlay)
                .upload(createNestedModelId(block, "post"), textureMap, generator.modelCollector);
        Identifier sideModelId = getFenceSideModel(tinted, overlay)
                .upload(createNestedModelId(block, "side"), textureMap, generator.modelCollector);

        // Create blockstate
        MultipartBlockStateSupplier blockstate = createFenceVariants(block,
                List.of(postModelId), List.of(sideModelId), List.of(1));
        generator.blockStateCollector.accept(blockstate);

        // Register item model
        Identifier itemModelId = Identifier.of("westerosblocks", "item/" + getBlockName(block));
        getFenceInventoryModel(tinted, overlay)
                .upload(itemModelId, textureMap, generator.modelCollector);
    }

    /**
     * Registers a fence block with random texture variants.
     */
    public static void registerFenceBlockWithRandomTextures(BlockStateModelGenerator generator, Block block, boolean tinted,
                                                           boolean overlay, List<BlockDefinition.TextureVariantSet> textureSets) {
        List<Identifier> postModelIds = new ArrayList<>();
        List<Identifier> sideModelIds = new ArrayList<>();
        List<Integer> weights = new ArrayList<>();

        for (int i = 0; i < textureSets.size(); i++) {
            BlockDefinition.TextureVariantSet set = textureSets.get(i);
            String[] expandedTextures = fillTextureArray(set.getTexturesAsArray(), 3);
            String[] expandedOverlays = overlay && set.hasOverlay() ? fillTextureArray(set.getOverlayTexturesAsArray(), 3) : null;

            TextureMap textureMap = createFenceTextureMap(expandedTextures, expandedOverlays);

            Identifier postModelId = getFencePostModel(tinted, overlay)
                    .upload(createNestedModelId(block, "post_v" + (i + 1)), textureMap, generator.modelCollector);
            Identifier sideModelId = getFenceSideModel(tinted, overlay)
                    .upload(createNestedModelId(block, "side_v" + (i + 1)), textureMap, generator.modelCollector);

            postModelIds.add(postModelId);
            sideModelIds.add(sideModelId);
            weights.add(set.weight);
        }

        // Create blockstate
        MultipartBlockStateSupplier blockstate = createFenceVariants(block, postModelIds, sideModelIds, weights);
        generator.blockStateCollector.accept(blockstate);

        // Register item model (using first texture set)
        BlockDefinition.TextureVariantSet firstSet = textureSets.get(0);
        String[] expandedTextures = fillTextureArray(firstSet.getTexturesAsArray(), 3);
        String[] expandedOverlays = overlay && firstSet.hasOverlay() ? fillTextureArray(firstSet.getOverlayTexturesAsArray(), 3) : null;
        TextureMap itemTextureMap = createFenceTextureMap(expandedTextures, expandedOverlays);

        Identifier itemModelId = Identifier.of("westerosblocks", "item/" + getBlockName(block));
        getFenceInventoryModel(tinted, overlay)
                .upload(itemModelId, itemTextureMap, generator.modelCollector);
    }

    public static void registerCustomFenceBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        boolean tinted = definition.isTinted() || definition.hasColorMult();
        boolean overlay = definition.hasOverlayTextures();
        List<String> textureList = definition.getTextures();

        if (definition.hasRandomTextures()) {
            List<BlockDefinition.TextureVariantSet> textureSets = new ArrayList<>();
            List<BlockDefinition.TextureVariantSet> variants = definition.getRandomTextureVariantSets();

            for (BlockDefinition.TextureVariantSet variant : variants) {
                List<String> textures = variant.textures;
                int weight = variant.weight;

                if (!textures.isEmpty()) {
                    String[] textureArray = textures.toArray(new String[0]);
                    String[] overlayArray = null;

                    if (overlay && definition.getOverlayTextures() != null && definition.getOverlayTextures().size() >= textures.size()) {
                        overlayArray = definition.getOverlayTextures().subList(0, textures.size()).toArray(new String[0]);
                    }

                    textureSets.add(new BlockDefinition.TextureVariantSet(textureArray, weight, overlayArray));
                } else {
                    textureSets.add(new BlockDefinition.TextureVariantSet(new String[]{"missingno"}, weight, null));
                }
            }

            registerFenceBlockWithRandomTextures(generator, block, tinted, overlay, textureSets);
        } else if (textureList != null && !textureList.isEmpty()) {
            String[] textures = textureList.toArray(new String[0]);
            String[] overlays = overlay && definition.getOverlayTextures() != null
                    ? definition.getOverlayTextures().toArray(new String[0])
                    : null;

            registerFenceBlock(generator, block, tinted, overlay, textures, overlays);
        } else {
            // Fallback
            registerFenceBlock(generator, block, tinted, overlay, new String[]{"missingno"}, null);
        }
    }

    /**
     * Helper class to hold texture set with weight for random textures.
     */

}
