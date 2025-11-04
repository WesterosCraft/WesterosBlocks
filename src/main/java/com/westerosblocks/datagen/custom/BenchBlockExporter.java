package com.westerosblocks.datagen.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.custom.WCBenchBlock;
import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.datagen.ModTextureKey;

import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.BlockStateVariant;
import net.minecraft.data.client.BlockStateVariantMap;
import net.minecraft.data.client.Model;
import net.minecraft.data.client.TextureKey;
import net.minecraft.data.client.TextureMap;
import net.minecraft.data.client.VariantSettings;
import net.minecraft.data.client.VariantsBlockStateSupplier;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.List;
import java.util.Optional;

/**
 * Exporter for bench blocks with FACING and CONNECTION properties.
 * Generates models for connectable bench blocks that form straight lines.
 *
 * @see WCBenchBlock
 */
public class BenchBlockExporter extends BaseBlockExporter {

    /**
     * Registers a custom bench block with FACING and CONNECTION properties
     */
    public static void registerCustomBenchBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        List<String> textureList = definition.getTextures();
        String texturePath = (textureList != null && !textureList.isEmpty()) ? textureList.get(0) : "missingno";

        registerCustomBenchBlock(generator, block, texturePath, texturePath);
    }

    /**
     * Internal implementation for registering bench blocks
     */
    private static void registerCustomBenchBlock(BlockStateModelGenerator generator, Block block, String texturePath, String particleTexture) {
        String blockName = getBlockName(block);

        TextureMap textureMap = new TextureMap()
                .put(ModTextureKey.BENCH, createBlockIdentifier(texturePath))
                .put(TextureKey.PARTICLE, createBlockIdentifier(particleTexture));

        // Create model parents referencing the custom bench models
        Model singleParent = new Model(Optional.of(WesterosBlocks.id("block/bench/wood_bench_1x1")), Optional.empty(), ModTextureKey.BENCH, TextureKey.PARTICLE);
        Model leftParent = new Model(Optional.of(WesterosBlocks.id("block/bench/wood_bench_edge")), Optional.empty(), ModTextureKey.BENCH, TextureKey.PARTICLE);
        Model rightParent = new Model(Optional.of(WesterosBlocks.id("block/bench/wood_bench_right")), Optional.empty(), ModTextureKey.BENCH, TextureKey.PARTICLE);
        Model middleParent = new Model(Optional.of(WesterosBlocks.id("block/bench/wood_bench_middle")), Optional.empty(), ModTextureKey.BENCH, TextureKey.PARTICLE);

        // Upload models with block-specific texture mapping
        Identifier singleModelId = singleParent.upload(createNestedModelId(block, "single"), textureMap, generator.modelCollector);
        Identifier leftModelId = leftParent.upload(createNestedModelId(block, "left"), textureMap, generator.modelCollector);
        Identifier rightModelId = rightParent.upload(createNestedModelId(block, "right"), textureMap, generator.modelCollector);
        Identifier middleModelId = middleParent.upload(createNestedModelId(block, "middle"), textureMap, generator.modelCollector);

        // Create variant map for FACING × CONNECTION
        BlockStateVariantMap.DoubleProperty<Direction, WCBenchBlock.ConnectionType> variantMap =
            BlockStateVariantMap.create(WCBenchBlock.FACING, WCBenchBlock.CONNECTION);

        // Register all combinations of facing and connection
        for (Direction facing : Direction.Type.HORIZONTAL) {
            int rotation = getRotationForFacing(facing);

            // SINGLE connection
            variantMap.register(facing, WCBenchBlock.ConnectionType.SINGLE,
                    BlockStateVariant.create()
                            .put(VariantSettings.MODEL, singleModelId)
                            .put(VariantSettings.Y, getRotationEnum(rotation)));

            // LEFT connection
            variantMap.register(facing, WCBenchBlock.ConnectionType.LEFT,
                    BlockStateVariant.create()
                            .put(VariantSettings.MODEL, leftModelId)
                            .put(VariantSettings.Y, getRotationEnum(rotation)));

            // RIGHT connection
            variantMap.register(facing, WCBenchBlock.ConnectionType.RIGHT,
                    BlockStateVariant.create()
                            .put(VariantSettings.MODEL, rightModelId)
                            .put(VariantSettings.Y, getRotationEnum(rotation)));

            // MIDDLE connection
            variantMap.register(facing, WCBenchBlock.ConnectionType.MIDDLE,
                    BlockStateVariant.create()
                            .put(VariantSettings.MODEL, middleModelId)
                            .put(VariantSettings.Y, getRotationEnum(rotation)));
        }

        // Register the blockstate with all variants
        generator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(block).coordinate(variantMap));

        // Register item model using the single variant
        registerParentedItemModel(generator, block, singleModelId);
    }

    /**
     * Gets the Y-axis rotation degrees for a facing direction
     */
    private static int getRotationForFacing(Direction facing) {
        return switch (facing) {
            case NORTH -> 180;
            case SOUTH -> 0;
            case WEST -> 90;
            case EAST -> 270;
            default -> 0;
        };
    }

    /**
     * Converts rotation degrees to VariantSettings.Rotation enum
     */
    private static VariantSettings.Rotation getRotationEnum(int degrees) {
        return switch (degrees) {
            case 0 -> VariantSettings.Rotation.R0;
            case 90 -> VariantSettings.Rotation.R90;
            case 180 -> VariantSettings.Rotation.R180;
            case 270 -> VariantSettings.Rotation.R270;
            default -> VariantSettings.Rotation.R0;
        };
    }
}
