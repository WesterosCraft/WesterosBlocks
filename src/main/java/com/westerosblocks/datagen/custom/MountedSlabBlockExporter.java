package com.westerosblocks.datagen.custom;

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

        if (definition.hasRandomTextures()) {
            variantCount = definition.getRandomTextures().size();
            for (BlockDefinition.RandomTextureVariant rtv : definition.getRandomTextures()) {
                weights.add(rtv.getWeight());
            }
        } else if (definition.hasStates() && definition.getStates().get(0).hasRandomTextures()) {
            variantCount = definition.getStates().get(0).getRandomTextures().size();
            for (BlockDefinition.RandomTextureVariant rtv : definition.getStates().get(0).getRandomTextures()) {
                weights.add(rtv.getWeight());
            }
        }

        if (weights.isEmpty()) {
            for (int i = 0; i < variantCount; i++) {
                weights.add(1);
            }
        }

        // Build model ID lists
        List<Identifier> topModels = new ArrayList<>();
        List<Identifier> bottomModels = new ArrayList<>();
        for (int i = 0; i < variantCount; i++) {
            topModels.add(createCustomModelId(block, "top_v" + (i + 1)));
            bottomModels.add(createCustomModelId(block, "bottom_v" + (i + 1)));
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
