package com.westerosblocks.datagen.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.datagen.ModModels;
import com.westerosblocks.block.custom.WCChairBlock;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;
import net.minecraft.data.client.VariantSettings.Rotation;

import java.util.Optional;

public class ChairBlockExporter extends BaseBlockExporter {
    public static void registerChairBlock(BlockStateModelGenerator generator, Block block, String texturePath) {
        Identifier cardinalModelId = createChairModel(generator, block, texturePath, "cardinal", ModModels.CHAIR);
        Identifier diagonalModelId = createChairModel(generator, block, texturePath, "diagonal", ModModels.CHAIR_45);

        BlockStateVariantMap variants = BlockStateVariantMap.create(WCChairBlock.ROTATION)
                // Cardinal directions (0, 2, 4, 6) use the base model with rotations
                .register(0, createVariant(cardinalModelId))
                .register(2, createVariant(cardinalModelId, 90))
                .register(4, createVariant(cardinalModelId, 180))
                .register(6, createVariant(cardinalModelId, 270))
                // Diagonal directions (1, 3, 5, 7) use the 45-degree rotated model with adjusted Y rotations
                .register(1, createVariant(diagonalModelId))
                .register(3, createVariant(diagonalModelId, 90))
                .register(5, createVariant(diagonalModelId, 180))
                .register(7, createVariant(diagonalModelId, 270));

        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block).coordinate(variants));

        generator.registerParentedItemModel(block, cardinalModelId);
    }

    private static Identifier createChairModel(BlockStateModelGenerator generator, Block block, String texturePath, String variant, Model model) {
        String blockName = getBlockName(block);
        String modelPath = "block/" + blockName + "/" + variant;
        Identifier modelId = WesterosBlocks.id(modelPath);

        TextureMap textureMap = new TextureMap()
                .put(TextureKey.ALL, createBlockIdentifier(texturePath));

        model.upload(modelId, textureMap, generator.modelCollector);

        return modelId;
    }
}