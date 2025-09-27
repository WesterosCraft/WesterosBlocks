package com.westerosblocks.datagen.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.data.BlockDefinition;
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
     * Registers a torch block from definition
     */
    public static void registerTorchBlockFromDefinition(BlockStateModelGenerator generator, Block standingTorch, BlockDefinition definition) {
        // Get texture from definition
        String texturePath = getTextureFromDefinition(definition);

        // Find the wall torch block
        Block wallTorch = Registries.BLOCK.get(WesterosBlocks.id("wall_" + definition.getBlockName()));

        if (wallTorch != null) {
            registerStandingTorch(generator, standingTorch, texturePath);
            registerWallTorch(generator, wallTorch, texturePath);
            registerSimpleItemModel(generator, standingTorch, createBlockIdentifier(texturePath));
        } else {
            WesterosBlocks.LOGGER.warn("Could not find wall torch for: {}", definition.getBlockName());
        }
    }

    private static String getTextureFromDefinition(BlockDefinition definition) {
        if (definition.getTextures() != null && !definition.getTextures().isEmpty()) {
            return definition.getTextures().get(0);
        }
        // Fallback to a default texture based on block name
        return "lighting/" + definition.getBlockName();
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