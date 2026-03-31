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

    public static void registerTorchBlockFromDefinition(BlockStateModelGenerator generator,
                                                        Block standingTorch,
                                                        BlockDefinition definition) {
        String texturePath = getTextureFromDefinition(definition);

        Identifier wallTorchId = WesterosBlocks.id("wall_" + definition.getBlockName());
        if (!Registries.BLOCK.containsId(wallTorchId)) {
            WesterosBlocks.LOGGER.warn("Could not find wall torch for: {}", definition.getBlockName());
            return;
        }
        Block wallTorch = Registries.BLOCK.get(wallTorchId);

        ModelPair models = generateTorchModels(generator, standingTorch, texturePath);

        generateStandingTorchBlockState(generator, standingTorch, models.standingModel);
        generateWallTorchBlockState(generator, wallTorch, models.wallModel);

        generateTorchItemModel(generator, standingTorch, texturePath);
    }

    private static ModelPair generateTorchModels(BlockStateModelGenerator generator,
                                                 Block standingTorch,
                                                 String texturePath) {
        TextureMap textureMap = createTorchTextureMap(texturePath);

        Identifier standingModelId = ModModels.TORCH.upload(
            createNestedModelId(standingTorch, "base"),
            textureMap,
            generator.modelCollector
        );

        Identifier wallModelId = ModModels.TORCH_WALL.upload(
            createNestedModelId(standingTorch, "wall"),
            textureMap,
            generator.modelCollector
        );

        return new ModelPair(standingModelId, wallModelId);
    }

    private static void generateStandingTorchBlockState(BlockStateModelGenerator generator,
                                                        Block standingTorch,
                                                        Identifier modelId) {
        generator.blockStateCollector.accept(createSimpleBlockState(standingTorch, modelId));
    }

    private static void generateWallTorchBlockState(BlockStateModelGenerator generator,
                                                    Block wallTorch,
                                                    Identifier modelId) {
        BlockStateVariantMap variants = createWallTorchVariants(modelId);
        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(wallTorch).coordinate(variants));
    }

    private static void generateTorchItemModel(BlockStateModelGenerator generator,
                                               Block standingTorch,
                                               String texturePath) {
        registerSimpleItemModel(generator, standingTorch, createBlockIdentifier(texturePath));
    }

    private static BlockStateVariantMap createWallTorchVariants(Identifier modelId) {
        return BlockStateVariantMap.create(Properties.HORIZONTAL_FACING)
            .register(Direction.EAST, createVariant(modelId))
            .register(Direction.SOUTH, createVariant(modelId, 90))
            .register(Direction.WEST, createVariant(modelId, 180))
            .register(Direction.NORTH, createVariant(modelId, 270));
    }

    private static TextureMap createTorchTextureMap(String texturePath) {
        return new TextureMap()
                .put(TextureKey.TORCH, createBlockIdentifier(texturePath))
                .put(TextureKey.PARTICLE, createBlockIdentifier(texturePath));
    }

    private static String getTextureFromDefinition(BlockDefinition definition) {
        if (definition.getTextures() != null && !definition.getTextures().isEmpty()) {
            return definition.getTextures().get(0);
        }
        // Fallback to default texture based on block name
        return "lighting/" + definition.getBlockName();
    }

    private record ModelPair(Identifier standingModel, Identifier wallModel) {}
}
