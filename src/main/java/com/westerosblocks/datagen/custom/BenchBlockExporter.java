package com.westerosblocks.datagen.custom;

import com.westerosblocks.block.custom.WCBenchBlock;
import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.datagen.ModModels;
import com.westerosblocks.datagen.ModTextureKey;

import net.minecraft.block.Block;
import net.minecraft.client.data.*;
import net.minecraft.client.render.model.json.ModelVariant;
import net.minecraft.client.render.model.json.WeightedVariant;
import net.minecraft.util.math.AxisRotation;
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
            BlockStateVariantMap.models(WCBenchBlock.FACING, WCBenchBlock.CONNECTION);

        // Register all combinations of facing and connection
        for (Direction facing : Direction.Type.HORIZONTAL) {
            AxisRotation rotation = toYRotation(getFacingSouthDefaultRotation(facing));

            // SINGLE connection
            variantMap.register(facing, WCBenchBlock.ConnectionType.SINGLE,
                    BlockStateModelGenerator.createWeightedVariant(new ModelVariant(singleModelId).withRotationY(rotation)));

            // LEFT connection
            variantMap.register(facing, WCBenchBlock.ConnectionType.LEFT,
                    BlockStateModelGenerator.createWeightedVariant(new ModelVariant(leftModelId).withRotationY(rotation)));

            // RIGHT connection
            variantMap.register(facing, WCBenchBlock.ConnectionType.RIGHT,
                    BlockStateModelGenerator.createWeightedVariant(new ModelVariant(rightModelId).withRotationY(rotation)));

            // MIDDLE connection
            variantMap.register(facing, WCBenchBlock.ConnectionType.MIDDLE,
                    BlockStateModelGenerator.createWeightedVariant(new ModelVariant(middleModelId).withRotationY(rotation)));
        }

        // Register the blockstate with all variants
        generator.blockStateCollector.accept(
                VariantsBlockModelDefinitionCreator.of(block).with(variantMap));

        // Register item model using the single variant
        registerParentedItemModel(generator, block, singleModelId);
    }

}
