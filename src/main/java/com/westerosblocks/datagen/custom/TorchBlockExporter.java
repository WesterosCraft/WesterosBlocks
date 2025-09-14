package com.westerosblocks.datagen.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.datagen.ModModels;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.state.property.Properties;

public class TorchBlockExporter extends BaseBlockExporter {
    public static void registerTorchBlock(BlockStateModelGenerator generator, Block standingTorch, String texturePath) {
        Block wallTorch = Registries.BLOCK.get(WesterosBlocks.id("wall_" + standingTorch.getTranslationKey().replace("block.westerosblocks.", "")));

        registerStandingTorch(generator, standingTorch, texturePath);
        registerWallTorch(generator, wallTorch, texturePath);
        registerSimpleItemModel(generator, standingTorch, createBlockIdentifier(texturePath));
    }

    /**
     * Registers the standing torch variant.
     */
    private static void registerStandingTorch(BlockStateModelGenerator generator, Block block, String texturePath) {
        TextureMap textureMap = new TextureMap().put(TextureKey.TORCH, createBlockIdentifier(texturePath));
        Identifier modelId = ModModels.TORCH.upload(createNestedModelId(block), textureMap, generator.modelCollector);
        
        generator.blockStateCollector.accept(createSimpleBlockState(block, modelId));
    }

    /**
     * Registers the wall torch variant
     */
    private static void registerWallTorch(BlockStateModelGenerator generator, Block block, String texturePath) {
        TextureMap textureMap = new TextureMap().put(TextureKey.TORCH, createBlockIdentifier(texturePath));
        Identifier modelId = ModModels.TORCH_WALL.upload(createNestedModelId(block), textureMap, generator.modelCollector);

        BlockStateVariantMap variants = BlockStateVariantMap.create(Properties.HORIZONTAL_FACING)
            .register(Direction.NORTH, createVariant(modelId))
            .register(Direction.SOUTH, createVariant(modelId, 180))
            .register(Direction.EAST, createVariant(modelId, 90))
            .register(Direction.WEST, createVariant(modelId, 270));

        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block).coordinate(variants));
    }
}