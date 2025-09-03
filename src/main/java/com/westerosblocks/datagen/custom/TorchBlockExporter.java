package com.westerosblocks.datagen.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.datagen.ModModels;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.state.property.Properties;
import net.minecraft.data.client.VariantSettings.Rotation;

import java.util.Optional;

public class TorchBlockExporter extends BaseBlockExporter {

    public static void registerTorchBlock(BlockStateModelGenerator generator, Block standingTorch, String texturePath) {
        Block wallTorch = Registries.BLOCK.get(WesterosBlocks.id("wall_" + standingTorch.getTranslationKey().replace("block.westerosblocks.", "")));

        // Generate standing torch block state
        generateStandingTorchBlockState(generator, standingTorch, texturePath);
        // Generate wall torch block state
        generateWallTorchBlockState(generator, wallTorch, texturePath);
        // Generate item model for standing torch only (wall torch has no item)
        generateStandingTorchItemModel(generator, standingTorch, texturePath);
    }

    private static void generateStandingTorchBlockState(BlockStateModelGenerator generator, Block block, String texturePath) {
        // Create the standing torch model
        Identifier standingModelId = createStandingTorchModel(generator, block, texturePath);

        // Create block state variant (standing torch has no properties)
        BlockStateVariant standingVariant = createVariant(standingModelId);

        // Register the block state using the generator's collector
        generator.blockStateCollector.accept(
            VariantsBlockStateSupplier.create(block, standingVariant)
        );
    }

    private static void generateWallTorchBlockState(BlockStateModelGenerator generator, Block block, String texturePath) {
        // Create the wall torch model
        Identifier wallModelId = createWallTorchModel(generator, block, texturePath);

        // Create variants for each facing direction
        BlockStateVariantMap variants = createCardinalVariants(wallModelId);

        // Register the block state
        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block).coordinate(variants));
    }

    private static Identifier createStandingTorchModel(BlockStateModelGenerator generator, Block block, String texturePath) {
        // Create texture map for standing torch
        TextureMap textureMap = new TextureMap()
            .put(TextureKey.TORCH, createBlockIdentifier(texturePath));

        // Create model identifier
        Identifier modelId = createModelId(block);

        // Use predefined ModModels torch model
        ModModels.TORCH.upload(modelId, textureMap, generator.modelCollector);

        return modelId;
    }

    private static Identifier createWallTorchModel(BlockStateModelGenerator generator, Block block, String texturePath) {
        // Create texture map for wall torch
        TextureMap textureMap = new TextureMap()
            .put(TextureKey.TORCH, createBlockIdentifier(texturePath));

        // Create model identifier
        Identifier modelId = createModelId(block);

        // Use predefined ModModels wall torch model
        ModModels.TORCH_WALL.upload(modelId, textureMap, generator.modelCollector);

        return modelId;
    }

    public static void generateItemModels(ItemModelGenerator generator, Block standingTorch) {
        // Only generate item model for the standing torch (wall torch has no item)
        generateItemModel(generator, standingTorch);
    }

    private static void generateStandingTorchItemModel(BlockStateModelGenerator generator, Block block, String texturePath) {
        // Create texture map for the item model
        TextureMap textureMap = TextureMap.layer0(createBlockIdentifier(texturePath));
        // Generate item model using the generator's registerParentedItemModel method
        Identifier itemModelId = ModelIds.getItemModelId(block.asItem());
        Models.GENERATED.upload(itemModelId, textureMap, generator.modelCollector);
    }

    // Utility methods

    private static BlockStateVariantMap createCardinalVariants(Identifier modelId) {
        return BlockStateVariantMap.create(Properties.HORIZONTAL_FACING)
            .register(Direction.NORTH, createVariant(modelId))
            .register(Direction.SOUTH, createVariant(modelId, 180))
            .register(Direction.EAST, createVariant(modelId, 90))
            .register(Direction.WEST, createVariant(modelId, 270));
    }

    private static void generateItemModel(ItemModelGenerator generator, Block block) {
        String texturePath = getTexturePath(block);
        TextureMap textureMap = TextureMap.layer0(createBlockIdentifier(texturePath));
        Models.GENERATED.upload(ModelIds.getItemModelId(block.asItem()), textureMap, generator.writer);
    }

    private static String getTexturePath(Block block) {
        String blockName = block.getTranslationKey().replace("block.westerosblocks.", "");
        
        return switch (blockName) {
            case "torch" -> "lighting/torch";
            case "torch_unlit" -> "lighting/torch_unlit";
            case "candle" -> "lighting/candle";
            case "candle_unlit" -> "lighting/candle_unlit";
            case "wall_torch" -> "lighting/torch";
            case "wall_torch_unlit" -> "lighting/torch_unlit";
            case "wall_candle" -> "lighting/candle";
            case "wall_candle_unlit" -> "lighting/candle_unlit";
            default -> "lighting/torch";
        };
    }

}