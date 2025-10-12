package com.westerosblocks.datagen.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.datagen.ModTextureKey;
import net.minecraft.block.enums.WallShape;
import net.minecraft.data.client.*;
import net.minecraft.block.Block;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WallBlockExporter extends BaseBlockExporter {

    private static Model createWallPostModel(boolean tinted, boolean overlay) {
        String tintPath = tinted ? "block/tinted/" : "block/untinted/";
        String overlayPath = overlay ? "_overlay" : "";
        String path = tintPath + "template_wall_post" + overlayPath;

        if (overlay) {
            return new Model(Optional.of(WesterosBlocks.id(path)), Optional.empty(),
                    TextureKey.BOTTOM,
                    TextureKey.TOP,
                    TextureKey.SIDE,
                    ModTextureKey.BOTTOM_OVERLAY,
                    ModTextureKey.TOP_OVERLAY,
                    ModTextureKey.SIDE_OVERLAY,
                    TextureKey.PARTICLE);
        } else {
            return new Model(Optional.of(WesterosBlocks.id(path)), Optional.empty(),
                    TextureKey.BOTTOM,
                    TextureKey.TOP,
                    TextureKey.SIDE,
                    TextureKey.PARTICLE);
        }
    }

    private static Model createWallSideModel(boolean tinted, boolean overlay, boolean isShort) {
        String tintPath = tinted ? "block/tinted/" : "block/untinted/";
        String overlayPath = overlay ? "_overlay" : "";
        String shortPath = isShort ? "_2" : "";
        String path = tintPath + "template_wall_side" + shortPath + overlayPath;

        if (overlay) {
            return new Model(Optional.of(WesterosBlocks.id(path)), Optional.empty(),
                TextureKey.BOTTOM, TextureKey.TOP, TextureKey.SIDE,
                ModTextureKey.BOTTOM_OVERLAY, ModTextureKey.TOP_OVERLAY, ModTextureKey.SIDE_OVERLAY, TextureKey.PARTICLE);
        } else {
            return new Model(Optional.of(WesterosBlocks.id(path)), Optional.empty(),
                TextureKey.BOTTOM, TextureKey.TOP, TextureKey.SIDE, TextureKey.PARTICLE);
        }
    }

    private static Model createWallSideTallModel(boolean tinted, boolean overlay) {
        String tintPath = tinted ? "block/tinted/" : "block/untinted/";
        String overlayPath = overlay ? "_overlay" : "";
        String path = tintPath + "template_wall_side_tall" + overlayPath;

        if (overlay) {
            return new Model(Optional.of(WesterosBlocks.id(path)), Optional.empty(),
                TextureKey.BOTTOM, TextureKey.TOP, TextureKey.SIDE,
                ModTextureKey.BOTTOM_OVERLAY, ModTextureKey.TOP_OVERLAY, ModTextureKey.SIDE_OVERLAY, TextureKey.PARTICLE);
        } else {
            return new Model(Optional.of(WesterosBlocks.id(path)), Optional.empty(),
                TextureKey.BOTTOM, TextureKey.TOP, TextureKey.SIDE, TextureKey.PARTICLE);
        }
    }

    // ========================================
    // Helper Methods (block-models.md 5.3-5.4)
    // ========================================

    private static TextureMap createWallTextureMap(String[] textures, String[] overlayTextures) {
        TextureMap textureMap = new TextureMap()
                .put(TextureKey.BOTTOM, createBlockIdentifier(textures[0]))
                .put(TextureKey.TOP, createBlockIdentifier(textures[1]))
                .put(TextureKey.SIDE, createBlockIdentifier(textures[2]))
                .put(TextureKey.PARTICLE, createBlockIdentifier(textures[2]));

        if (overlayTextures != null) {
            textureMap.put(ModTextureKey.BOTTOM_OVERLAY, createBlockIdentifier(overlayTextures[0]));
            textureMap.put(ModTextureKey.TOP_OVERLAY, createBlockIdentifier(overlayTextures[1]));
            textureMap.put(ModTextureKey.SIDE_OVERLAY, createBlockIdentifier(overlayTextures[2]));
        }

        return textureMap;
    }

    private static MultipartBlockStateSupplier createWallVariants(Block block, List<Identifier> postModelIds,
                                                                    List<Identifier> sideModelIds, List<Identifier> tallModelIds,
                                                                    List<Integer> weights) {
        MultipartBlockStateSupplier supplier = MultipartBlockStateSupplier.create(block);

        // Add post models (when up=true)
        List<BlockStateVariant> postVariants = new ArrayList<>();
        for (int i = 0; i < postModelIds.size(); i++) {
            BlockStateVariant postVariant = BlockStateVariant.create()
                    .put(VariantSettings.MODEL, postModelIds.get(i));

            if (weights.get(i) > 1) {
                postVariant = postVariant.put(VariantSettings.WEIGHT, weights.get(i));
            }

            postVariants.add(postVariant);
        }
        supplier.with(When.create().set(Properties.UP, true), postVariants);

        // Add side models for each direction (when that direction is "low")
        addDirectionalSideModels(supplier, sideModelIds, weights, "north", WallShape.LOW);
        addDirectionalSideModels(supplier, sideModelIds, weights, "east", WallShape.LOW);
        addDirectionalSideModels(supplier, sideModelIds, weights, "south", WallShape.LOW);
        addDirectionalSideModels(supplier, sideModelIds, weights, "west", WallShape.LOW);

        // Add tall side models for each direction (when that direction is "tall")
        addDirectionalSideModels(supplier, tallModelIds, weights, "north", WallShape.TALL);
        addDirectionalSideModels(supplier, tallModelIds, weights, "east", WallShape.TALL);
        addDirectionalSideModels(supplier, tallModelIds, weights, "south", WallShape.TALL);
        addDirectionalSideModels(supplier, tallModelIds, weights, "west", WallShape.TALL);

        return supplier;
    }

    private static void addDirectionalSideModels(MultipartBlockStateSupplier supplier, List<Identifier> modelIds,
                                                  List<Integer> weights, String direction, WallShape shape) {
        List<BlockStateVariant> sideVariants = new ArrayList<>();

        for (int i = 0; i < modelIds.size(); i++) {
            BlockStateVariant sideVariant = BlockStateVariant.create()
                    .put(VariantSettings.MODEL, modelIds.get(i));

            // Add rotation based on direction
            switch (direction) {
                case "east" -> sideVariant = sideVariant.put(VariantSettings.Y, VariantSettings.Rotation.R90);
                case "south" -> sideVariant = sideVariant.put(VariantSettings.Y, VariantSettings.Rotation.R180);
                case "west" -> sideVariant = sideVariant.put(VariantSettings.Y, VariantSettings.Rotation.R270);
                // NORTH gets no rotation (0 degrees)
            }

            if (weights.get(i) > 1) {
                sideVariant = sideVariant.put(VariantSettings.WEIGHT, weights.get(i));
            }

            // Add UV lock for rotated models
            if (!direction.equals("north")) {
                sideVariant = sideVariant.put(VariantSettings.UVLOCK, true);
            }

            sideVariants.add(sideVariant);
        }

        // Create condition based on direction and shape
        When condition = switch (direction) {
            case "north" -> When.create().set(Properties.NORTH_WALL_SHAPE, shape);
            case "east" -> When.create().set(Properties.EAST_WALL_SHAPE, shape);
            case "south" -> When.create().set(Properties.SOUTH_WALL_SHAPE, shape);
            case "west" -> When.create().set(Properties.WEST_WALL_SHAPE, shape);
            default -> throw new IllegalArgumentException("Unknown direction: " + direction);
        };

        supplier.with(condition, sideVariants);
    }

    // ========================================
    // Public Registration Methods (block-models.md 5.5)
    // ========================================

    public static void registerWallBlock(BlockStateModelGenerator generator, Block block, boolean tinted,
                                        boolean overlay, boolean isShort, String[] textures, String[] overlayTextures) {
        // Expand single texture to three if needed
        String[] expandedTextures = expandTextureArray(textures);
        String[] expandedOverlays = overlay && overlayTextures != null ? expandTextureArray(overlayTextures) : null;

        // Create texture map
        TextureMap textureMap = createWallTextureMap(expandedTextures, expandedOverlays);

        // Upload post, side, and tall models
        Identifier postModelId = createWallPostModel(tinted, overlay)
                .upload(createNestedModelId(block, "post"), textureMap, generator.modelCollector);
        Identifier sideModelId = createWallSideModel(tinted, overlay, isShort)
                .upload(createNestedModelId(block, "side"), textureMap, generator.modelCollector);
        Identifier tallModelId = createWallSideTallModel(tinted, overlay)
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
            String[] expandedTextures = expandTextureArray(set.getTexturesAsArray());
            String[] expandedOverlays = overlay && set.hasOverlay() ? expandTextureArray(set.getOverlayTexturesAsArray()) : null;

            TextureMap textureMap = createWallTextureMap(expandedTextures, expandedOverlays);

            Identifier postModelId = createWallPostModel(tinted, overlay)
                    .upload(createNestedModelId(block, "post_v" + (i + 1)), textureMap, generator.modelCollector);
            Identifier sideModelId = createWallSideModel(tinted, overlay, isShort)
                    .upload(createNestedModelId(block, "side_v" + (i + 1)), textureMap, generator.modelCollector);
            Identifier tallModelId = createWallSideTallModel(tinted, overlay)
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
        String[] expandedTextures = expandTextureArray(firstSet.getTexturesAsArray());
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

            registerWallBlockWithRandomTextures(generator, block, tinted, overlay, isShort, textureSets);
        } else if (textureList != null && !textureList.isEmpty()) {
            String[] textures = textureList.toArray(new String[0]);
            String[] overlays = overlay && definition.getOverlayTextures() != null
                    ? definition.getOverlayTextures().toArray(new String[0])
                    : null;

            registerWallBlock(generator, block, tinted, overlay, isShort, textures, overlays);
        } else {
            // Fallback
            registerWallBlock(generator, block, tinted, overlay, isShort, new String[]{"missingno"}, null);
        }
    }

    // ========================================
    // Helper Classes
    // ========================================


    private static String[] expandTextureArray(String[] textures) {
        if (textures.length == 1) {
            return new String[]{textures[0], textures[0], textures[0]};
        } else if (textures.length == 3) {
            return textures;
        } else {
            throw new IllegalArgumentException("Wall blocks require 1 or 3 textures, got " + textures.length);
        }
    }
}
