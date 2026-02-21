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
        String blockName = getBlockName(block);

        TextureMap textureMap = new TextureMap()
                .put(ModTextureKey.BENCH, createBlockIdentifier(texturePath))
                .put(TextureKey.PARTICLE, createBlockIdentifier(particleTexture));

        // Upload models with block-specific texture mapping
        Identifier singleModelId = ModModels.BENCH_SINGLE.upload(createNestedModelId(block, "single"), textureMap, generator.modelCollector);
        Identifier leftModelId = ModModels.BENCH_LEFT.upload(createNestedModelId(block, "left"), textureMap, generator.modelCollector);
        Identifier rightModelId = ModModels.BENCH_RIGHT.upload(createNestedModelId(block, "right"), textureMap, generator.modelCollector);
        Identifier middleModelId = ModModels.BENCH_MIDDLE.upload(createNestedModelId(block, "middle"), textureMap, generator.modelCollector);

        // Create variant map for FACING × CONNECTION
        BlockStateVariantMap.DoubleProperty<Direction, WCBenchBlock.ConnectionType> variantMap =
            BlockStateVariantMap.create(WCBenchBlock.FACING, WCBenchBlock.CONNECTION);

        // Register all combinations of facing and connection
        for (Direction facing : Direction.Type.HORIZONTAL) {
            VariantSettings.Rotation rotation = toYRotation(getFacingSouthDefaultRotation(facing));

            // SINGLE connection
            variantMap.register(facing, WCBenchBlock.ConnectionType.SINGLE,
                    BlockStateVariant.create()
                            .put(VariantSettings.MODEL, singleModelId)
                            .put(VariantSettings.Y, rotation));

            // LEFT connection
            variantMap.register(facing, WCBenchBlock.ConnectionType.LEFT,
                    BlockStateVariant.create()
                            .put(VariantSettings.MODEL, leftModelId)
                            .put(VariantSettings.Y, rotation));

            // RIGHT connection
            variantMap.register(facing, WCBenchBlock.ConnectionType.RIGHT,
                    BlockStateVariant.create()
                            .put(VariantSettings.MODEL, rightModelId)
                            .put(VariantSettings.Y, rotation));

            // MIDDLE connection
            variantMap.register(facing, WCBenchBlock.ConnectionType.MIDDLE,
                    BlockStateVariant.create()
                            .put(VariantSettings.MODEL, middleModelId)
                            .put(VariantSettings.Y, rotation));
        }

        // Register the blockstate with all variants
        generator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(block).coordinate(variantMap));

        // Register item model using the single variant
        registerParentedItemModel(generator, block, singleModelId);
    }

}
