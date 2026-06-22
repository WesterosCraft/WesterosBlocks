package com.westerosblocks.datagen.custom;

import com.westerosblocks.block.custom.WCBenchBlock;
import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.datagen.ModModels;
import com.westerosblocks.datagen.ModTextureKey;

import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.BlockStateVariant;
import net.minecraft.data.client.BlockStateVariantMap;
import net.minecraft.data.client.TextureKey;
import net.minecraft.data.client.TextureMap;
import net.minecraft.data.client.VariantSettings;
import net.minecraft.data.client.VariantsBlockStateSupplier;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.List;

public class BenchBlockExporter extends BaseBlockExporter {

    public static void registerCustomBenchBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        List<String> textureList = definition.getTextures();
        String texturePath = (textureList != null && !textureList.isEmpty()) ? textureList.get(0) : "missingno";

        registerCustomBenchBlock(generator, block, texturePath, texturePath);
    }

    private static void registerCustomBenchBlock(BlockStateModelGenerator generator, Block block, String texturePath, String particleTexture) {
        TextureMap textureMap = new TextureMap()
                .put(ModTextureKey.BENCH, createBlockIdentifier(texturePath))
                .put(TextureKey.PARTICLE, createBlockIdentifier(particleTexture));

        // Upload all 12 models (4 connection templates × 3 offsets) with block-specific textures.
        Identifier singleMiddle = ModModels.BENCH_SINGLE_MIDDLE.upload(createNestedModelId(block, "single_middle"), textureMap, generator.modelCollector);
        Identifier singleLeft = ModModels.BENCH_SINGLE_LEFT.upload(createNestedModelId(block, "single_left"), textureMap, generator.modelCollector);
        Identifier singleRight = ModModels.BENCH_SINGLE_RIGHT.upload(createNestedModelId(block, "single_right"), textureMap, generator.modelCollector);
        Identifier leftMiddle = ModModels.BENCH_LEFT_MIDDLE.upload(createNestedModelId(block, "left_middle"), textureMap, generator.modelCollector);
        Identifier leftLeft = ModModels.BENCH_LEFT_LEFT.upload(createNestedModelId(block, "left_left"), textureMap, generator.modelCollector);
        Identifier leftRight = ModModels.BENCH_LEFT_RIGHT.upload(createNestedModelId(block, "left_right"), textureMap, generator.modelCollector);
        Identifier rightMiddle = ModModels.BENCH_RIGHT_MIDDLE.upload(createNestedModelId(block, "right_middle"), textureMap, generator.modelCollector);
        Identifier rightLeft = ModModels.BENCH_RIGHT_LEFT.upload(createNestedModelId(block, "right_left"), textureMap, generator.modelCollector);
        Identifier rightRight = ModModels.BENCH_RIGHT_RIGHT.upload(createNestedModelId(block, "right_right"), textureMap, generator.modelCollector);
        Identifier middleMiddle = ModModels.BENCH_MIDDLE_MIDDLE.upload(createNestedModelId(block, "middle_middle"), textureMap, generator.modelCollector);
        Identifier middleLeft = ModModels.BENCH_MIDDLE_LEFT.upload(createNestedModelId(block, "middle_left"), textureMap, generator.modelCollector);
        Identifier middleRight = ModModels.BENCH_MIDDLE_RIGHT.upload(createNestedModelId(block, "middle_right"), textureMap, generator.modelCollector);

        // The model file is chosen jointly by CONNECTION × OFFSET.
        BlockStateVariantMap.DoubleProperty<WCBenchBlock.ConnectionType, WCBenchBlock.OffsetType> modelMap =
            BlockStateVariantMap.create(WCBenchBlock.CONNECTION, WCBenchBlock.OFFSET);
        modelMap.register(WCBenchBlock.ConnectionType.SINGLE, WCBenchBlock.OffsetType.MIDDLE, BlockStateVariant.create().put(VariantSettings.MODEL, singleMiddle));
        modelMap.register(WCBenchBlock.ConnectionType.SINGLE, WCBenchBlock.OffsetType.LEFT, BlockStateVariant.create().put(VariantSettings.MODEL, singleLeft));
        modelMap.register(WCBenchBlock.ConnectionType.SINGLE, WCBenchBlock.OffsetType.RIGHT, BlockStateVariant.create().put(VariantSettings.MODEL, singleRight));
        modelMap.register(WCBenchBlock.ConnectionType.LEFT, WCBenchBlock.OffsetType.MIDDLE, BlockStateVariant.create().put(VariantSettings.MODEL, leftMiddle));
        modelMap.register(WCBenchBlock.ConnectionType.LEFT, WCBenchBlock.OffsetType.LEFT, BlockStateVariant.create().put(VariantSettings.MODEL, leftLeft));
        modelMap.register(WCBenchBlock.ConnectionType.LEFT, WCBenchBlock.OffsetType.RIGHT, BlockStateVariant.create().put(VariantSettings.MODEL, leftRight));
        modelMap.register(WCBenchBlock.ConnectionType.RIGHT, WCBenchBlock.OffsetType.MIDDLE, BlockStateVariant.create().put(VariantSettings.MODEL, rightMiddle));
        modelMap.register(WCBenchBlock.ConnectionType.RIGHT, WCBenchBlock.OffsetType.LEFT, BlockStateVariant.create().put(VariantSettings.MODEL, rightLeft));
        modelMap.register(WCBenchBlock.ConnectionType.RIGHT, WCBenchBlock.OffsetType.RIGHT, BlockStateVariant.create().put(VariantSettings.MODEL, rightRight));
        modelMap.register(WCBenchBlock.ConnectionType.MIDDLE, WCBenchBlock.OffsetType.MIDDLE, BlockStateVariant.create().put(VariantSettings.MODEL, middleMiddle));
        modelMap.register(WCBenchBlock.ConnectionType.MIDDLE, WCBenchBlock.OffsetType.LEFT, BlockStateVariant.create().put(VariantSettings.MODEL, middleLeft));
        modelMap.register(WCBenchBlock.ConnectionType.MIDDLE, WCBenchBlock.OffsetType.RIGHT, BlockStateVariant.create().put(VariantSettings.MODEL, middleRight));

        // The Y rotation is chosen by FACING alone.
        BlockStateVariantMap.SingleProperty<Direction> facingMap = BlockStateVariantMap.create(WCBenchBlock.FACING);
        for (Direction facing : Direction.Type.HORIZONTAL) {
            facingMap.register(facing, BlockStateVariant.create()
                    .put(VariantSettings.Y, toYRotation(getFacingSouthDefaultRotation(facing))));
        }

        // Chaining the two coordinate maps produces the CONNECTION × OFFSET × FACING product,
        // merging the MODEL and Y settings into each variant.
        generator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(block).coordinate(modelMap).coordinate(facingMap));

        // Register item model using the default (single, middle offset) variant.
        registerParentedItemModel(generator, block, singleMiddle);
    }

}
