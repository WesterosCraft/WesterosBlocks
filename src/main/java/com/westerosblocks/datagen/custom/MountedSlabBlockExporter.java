package com.westerosblocks.datagen.custom;

import com.westerosblocks.datagen.ModModels;
import com.westerosblocks.datagen.ModTextureKey;
import com.westerosblocks.datagen.ModTextureMap;
import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.Block;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.List;

public class MountedSlabBlockExporter extends BaseBlockExporter {

    private static final Direction[] DIRECTIONS = {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
    private static final int[] ROTATIONS = {0, 90, 180, 270};

    public static void registerMountedSlabBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        // Determine variant count from randomTextures (or default to 1)
        int variantCount = 1;
        List<Integer> weights = new ArrayList<>();

        BlockDefinition.StateVariant state = null;
        if (definition.hasStates() && !definition.getStates().isEmpty()) {
            state = definition.getStates().get(0);
        }

        if (definition.hasRandomTextures()) {
            variantCount = definition.getRandomTextures().size();
            for (BlockDefinition.RandomTextureVariant rtv : definition.getRandomTextures()) {
                weights.add(rtv.getWeight());
            }
        } else if (state != null && state.hasRandomTextures()) {
            variantCount = state.getRandomTextures().size();
            for (BlockDefinition.RandomTextureVariant rtv : state.getRandomTextures()) {
                weights.add(rtv.getWeight());
            }
        }

        if (weights.isEmpty()) {
            for (int i = 0; i < variantCount; i++) {
                weights.add(1);
            }
        }

        boolean isCustom = definition.hasCustomModel() || (state != null && state.isCustomModel());

        // Build model ID lists and optionally generate models
        List<Identifier> topModels = new ArrayList<>();
        List<Identifier> bottomModels = new ArrayList<>();
        for (int i = 0; i < variantCount; i++) {
            String topVariant = "top_v" + (i + 1);
            String bottomVariant = "bottom_v" + (i + 1);

            if (isCustom) {
                topModels.add(createCustomModelId(block, topVariant));
                bottomModels.add(createCustomModelId(block, bottomVariant));
            } else {
                topModels.add(createNestedModelId(block, topVariant));
                bottomModels.add(createNestedModelId(block, bottomVariant));
                generateMountedSlabModels(generator, block, i, state, definition);
            }
        }

        // Generate blockstate using Fabric API
        BlockStateVariantMap.DoubleProperty<Direction, BlockHalf> variantMap =
            BlockStateVariantMap.create(Properties.HORIZONTAL_FACING, Properties.BLOCK_HALF);

        for (int d = 0; d < 4; d++) {
            Direction dir = DIRECTIONS[d];
            int rot = ROTATIONS[d];

            if (variantCount == 1) {
                // Single variant
                variantMap.register(dir, BlockHalf.BOTTOM, createVariantWithRotation(bottomModels.get(0), rot));
                variantMap.register(dir, BlockHalf.TOP, createVariantWithRotation(topModels.get(0), rot));
            } else {
                // Multiple variants with weights
                List<BlockStateVariant> bottomVariants = new ArrayList<>();
                List<BlockStateVariant> topVariants = new ArrayList<>();
                for (int i = 0; i < variantCount; i++) {
                    BlockStateVariant bv = createVariantWithRotation(bottomModels.get(i), rot);
                    BlockStateVariant tv = createVariantWithRotation(topModels.get(i), rot);
                    int weight = weights.get(i);
                    if (weight > 1) {
                        bv.put(VariantSettings.WEIGHT, weight);
                        tv.put(VariantSettings.WEIGHT, weight);
                    }
                    bottomVariants.add(bv);
                    topVariants.add(tv);
                }
                variantMap.register(dir, BlockHalf.BOTTOM, bottomVariants);
                variantMap.register(dir, BlockHalf.TOP, topVariants);
            }
        }

        generator.blockStateCollector.accept(
            VariantsBlockStateSupplier.create(block).coordinate(variantMap)
        );

        // Item model parented to first bottom variant
        registerParentedItemModel(generator, block, bottomModels.get(0));
    }

    private static void generateMountedSlabModels(BlockStateModelGenerator generator, Block block,
                                                   int setIdx, BlockDefinition.StateVariant state,
                                                   BlockDefinition definition) {
        BlockDefinition.RandomTextureVariant set = null;
        if (state != null && state.hasRandomTextures()) {
            set = state.getRandomTextureSet(setIdx);
        } else if (definition.hasRandomTextures()) {
            set = definition.getRandomTextures().get(setIdx);
        }

        String[] textures;
        if (set != null && set.getTextureCount() > 0) {
            textures = new String[set.getTextureCount()];
            for (int i = 0; i < set.getTextureCount(); i++) {
                textures[i] = set.getTextureByIndex(i);
            }
        } else if (definition.getTextures() != null && !definition.getTextures().isEmpty()) {
            textures = definition.getTextures().toArray(new String[0]);
        } else {
            textures = new String[]{"missing"};
        }

        boolean isTinted = definition.isTinted() || definition.hasColorMult();
        boolean isOverlay = state != null && state.hasOverlayTextures();

        String[] filledTextures = fillTextureArray(textures);
        TextureMap textureMap = ModTextureMap.customAllSides(filledTextures);

        if (isOverlay) {
            List<String> overlayTextures = state.getOverlayTextures();
            if (overlayTextures != null && !overlayTextures.isEmpty()) {
                textureMap.put(ModTextureKey.DOWN_OVERLAY, createBlockIdentifier(getOverlayTextureByIndex(overlayTextures, 0)));
                textureMap.put(ModTextureKey.UP_OVERLAY, createBlockIdentifier(getOverlayTextureByIndex(overlayTextures, 1)));
                textureMap.put(ModTextureKey.NORTH_OVERLAY, createBlockIdentifier(getOverlayTextureByIndex(overlayTextures, 2)));
                textureMap.put(ModTextureKey.SOUTH_OVERLAY, createBlockIdentifier(getOverlayTextureByIndex(overlayTextures, 3)));
                textureMap.put(ModTextureKey.WEST_OVERLAY, createBlockIdentifier(getOverlayTextureByIndex(overlayTextures, 4)));
                textureMap.put(ModTextureKey.EAST_OVERLAY, createBlockIdentifier(getOverlayTextureByIndex(overlayTextures, 5)));
            }
        }

        Model bottomModel, topModel;
        if (isOverlay) {
            bottomModel = isTinted ? ModModels.SLAB_BOTTOM_OVERLAY_TINTED : ModModels.SLAB_BOTTOM_OVERLAY_UNTINTED;
            topModel = isTinted ? ModModels.SLAB_TOP_OVERLAY_TINTED : ModModels.SLAB_TOP_OVERLAY_UNTINTED;
        } else if (isTinted) {
            bottomModel = ModModels.SLAB_BOTTOM_TINTED;
            topModel = ModModels.SLAB_TOP_TINTED;
        } else {
            bottomModel = ModModels.SLAB_BOTTOM;
            topModel = ModModels.SLAB_TOP;
        }

        Identifier bottomModelId = createNestedModelId(block, "bottom_v" + (setIdx + 1));
        bottomModel.upload(bottomModelId, textureMap, generator.modelCollector);

        Identifier topModelId = createNestedModelId(block, "top_v" + (setIdx + 1));
        topModel.upload(topModelId, textureMap, generator.modelCollector);
    }

    private static BlockStateVariant createVariantWithRotation(Identifier modelId, int rotation) {
        BlockStateVariant variant = BlockStateVariant.create().put(VariantSettings.MODEL, modelId);
        if (rotation > 0) {
            variant.put(VariantSettings.Y, switch (rotation) {
                case 90 -> VariantSettings.Rotation.R90;
                case 180 -> VariantSettings.Rotation.R180;
                case 270 -> VariantSettings.Rotation.R270;
                default -> VariantSettings.Rotation.R0;
            });
        }
        return variant;
    }
}
