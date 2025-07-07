package com.westerosblocks.datagen.models;

import com.westerosblocks.WesterosBlocks;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class TorchBlockExport extends ModelExport2 {

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator generator, Block standingTorch, String texturePath) {
        Block wallTorch = Registries.BLOCK.get(WesterosBlocks.id("wall_" + standingTorch.getTranslationKey().replace("block.westerosblocks.", "")));

        // Generate standing torch block state
        generateStandingTorchBlockState(generator, standingTorch, texturePath);
        // Generate wall torch block state
        generateWallTorchBlockState(generator, wallTorch, texturePath);
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

        // Create variants for each facing direction using ModelExport2 utility method
        BlockStateVariantMap variants = createCardinalVariants(wallModelId);

        // Register the block state using ModelExport2 utility method
        registerBlockState(generator, block, variants);
    }

    private static Identifier createStandingTorchModel(BlockStateModelGenerator generator, Block block, String texturePath) {
        // Create texture map for standing torch
        TextureMap textureMap = new TextureMap()
            .put(TextureKey.TORCH, createBlockIdentifier(texturePath));

        // Create model identifier using ModelExport2 utility method
        Identifier modelId = createModelId(block);

        // Create and upload the model using the template
        Model model = new Model(
            Optional.of(WesterosBlocks.id("block/untinted/template_torch")),
            Optional.empty(),
            TextureKey.TORCH
        );
        model.upload(modelId, textureMap, generator.modelCollector);

        return modelId;
    }

    private static Identifier createWallTorchModel(BlockStateModelGenerator generator, Block block, String texturePath) {
        // Create texture map for wall torch
        TextureMap textureMap = new TextureMap()
            .put(TextureKey.TORCH, createBlockIdentifier(texturePath));

        // Create model identifier using ModelExport2 utility method
        Identifier modelId = createModelId(block);

        // Create and upload the model using the template
        Model model = new Model(
            Optional.of(WesterosBlocks.id("block/untinted/template_torch_wall")),
            Optional.empty(),
            TextureKey.TORCH
        );
        model.upload(modelId, textureMap, generator.modelCollector);

        return modelId;
    }

    public static void generateItemModelsAuto(ItemModelGenerator generator, Block standingTorch, Block wallTorch) {
        // Generate item models for both standing and wall torch blocks using ModelExport2 utility method
        generateItemModel(generator, standingTorch);
        generateItemModel(generator, wallTorch);
    }

    private static String getTexturePath(Block block) {
        // Extract texture path from block name
        String blockName = block.getTranslationKey().replace("block.westerosblocks.", "");
        
        // Map block names to texture paths
        return switch (blockName) {
            case "torch" -> "lighting/torch";
            case "torch_unlit" -> "lighting/torch_unlit";
            case "candle" -> "lighting/candle";
            case "candle_unlit" -> "lighting/candle_unlit";
            case "wall_torch" -> "lighting/torch";
            case "wall_torch_unlit" -> "lighting/torch_unlit";
            case "wall_candle" -> "lighting/candle";
            case "wall_candle_unlit" -> "lighting/candle_unlit";
            default -> "lighting/torch"; // fallback
        };
    }

    @Override
    public void generateItemModels(ItemModelGenerator generator, Block block) {
        // Generate item model for a single block
        Block wallTorch = Registries.BLOCK.get(WesterosBlocks.id("wall_" + block.getTranslationKey().replace("block.westerosblocks.", "")));
        generateItemModelsAuto(generator, block, wallTorch);
    }
} 