package com.westerosblocks.datagen.custom;

import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.Block;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

/**
 * Exporter for trapdoor blocks following block-models.md patterns.
 * Generates models for trapdoors with open/closed states, top/bottom halves, and directional facing.
 *
 */
public class TrapDoorBlockExporter extends BaseBlockExporter {

    public static void registerTrapDoorBlock(BlockStateModelGenerator generator, Block block, String texturePath) {
        // Create texture identifier
        Identifier textureId = createBlockIdentifier(texturePath);

        // Create trapdoor variants manually using BlockStateModelGenerator's internal models
        // Trapdoor has models: bottom, top, and open
        TextureMap textureMap = new TextureMap().put(TextureKey.TEXTURE, textureId);

        Identifier bottomModel = Models.TEMPLATE_ORIENTABLE_TRAPDOOR_BOTTOM.upload(block, textureMap, generator.modelCollector);
        Identifier topModel = Models.TEMPLATE_ORIENTABLE_TRAPDOOR_TOP.upload(block, textureMap, generator.modelCollector);
        Identifier openModel = Models.TEMPLATE_ORIENTABLE_TRAPDOOR_OPEN.upload(block, textureMap, generator.modelCollector);

        // Register blockstate with variants
        generator.blockStateCollector.accept(createTrapdoorBlockState(block, bottomModel, topModel, openModel));

        // Register item model
        registerParentedItemModel(generator, block, bottomModel);
    }

    /**
     * Creates trapdoor blockstate with all variants (open/closed, top/bottom, facing).
     */
    private static BlockStateSupplier createTrapdoorBlockState(Block block, Identifier bottomModel, Identifier topModel, Identifier openModel) {
        return VariantsBlockStateSupplier.create(block)
            .coordinate(BlockStateVariantMap.create(TrapdoorBlock.FACING, TrapdoorBlock.HALF, TrapdoorBlock.OPEN)
                // Bottom variants
                .register(Direction.NORTH, BlockHalf.BOTTOM, false, createVariant(bottomModel))
                .register(Direction.SOUTH, BlockHalf.BOTTOM, false, createVariant(bottomModel, 180))
                .register(Direction.EAST, BlockHalf.BOTTOM, false, createVariant(bottomModel, 90))
                .register(Direction.WEST, BlockHalf.BOTTOM, false, createVariant(bottomModel, 270))
                // Top variants
                .register(Direction.NORTH, BlockHalf.TOP, false, createVariant(topModel))
                .register(Direction.SOUTH, BlockHalf.TOP, false, createVariant(topModel, 180))
                .register(Direction.EAST, BlockHalf.TOP, false, createVariant(topModel, 90))
                .register(Direction.WEST, BlockHalf.TOP, false, createVariant(topModel, 270))
                // Open variants
                .register(Direction.NORTH, BlockHalf.BOTTOM, true, createVariant(openModel))
                .register(Direction.SOUTH, BlockHalf.BOTTOM, true, createVariant(openModel, 180))
                .register(Direction.EAST, BlockHalf.BOTTOM, true, createVariant(openModel, 90))
                .register(Direction.WEST, BlockHalf.BOTTOM, true, createVariant(openModel, 270))
                .register(Direction.NORTH, BlockHalf.TOP, true, createVariant(openModel, 180, 180))
                .register(Direction.SOUTH, BlockHalf.TOP, true, createVariant(openModel, 180, 0))
                .register(Direction.EAST, BlockHalf.TOP, true, createVariant(openModel, 180, 270))
                .register(Direction.WEST, BlockHalf.TOP, true, createVariant(openModel, 180, 90))
            );
    }

    /**
     * Helper to create a variant with x and y rotations.
     */
    private static BlockStateVariant createVariant(Identifier modelId, int xRotation, int yRotation) {
        return BlockStateVariant.create()
            .put(VariantSettings.MODEL, modelId)
            .put(VariantSettings.X, getRotation(xRotation))
            .put(VariantSettings.Y, getRotation(yRotation));
    }

    /**
     * Converts degrees to VariantSettings.Rotation.
     */
    private static VariantSettings.Rotation getRotation(int degrees) {
        return switch (degrees % 360) {
            case 90 -> VariantSettings.Rotation.R90;
            case 180 -> VariantSettings.Rotation.R180;
            case 270 -> VariantSettings.Rotation.R270;
            default -> VariantSettings.Rotation.R0;
        };
    }

    public static void registerCustomTrapDoorBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        // Validate texture data using centralized method
        definition.validateTextureData();
        definition.validateTextureCount(1);

        // Extract textures using centralized method
        String[] textures = definition.getTexturesAsArray();

        // Use the existing registerTrapDoorBlock method
        registerTrapDoorBlock(generator, block, textures[0]);
    }
}
