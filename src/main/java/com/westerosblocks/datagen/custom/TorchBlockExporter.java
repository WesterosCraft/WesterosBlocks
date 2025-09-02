package com.westerosblocks.datagen.custom;

import com.westerosblocks.WesterosBlocks;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.state.property.Properties;
import net.minecraft.data.client.VariantSettings.Rotation;

import java.util.Optional;

public class TorchBlockExporter {

    public static void registerTorchBlock(BlockStateModelGenerator generator, Block standingTorch, String texturePath) {
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

        // Create model identifier
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
        // Generate item models for both standing and wall torch blocks
        generateItemModel(generator, standingTorch);
        generateItemModel(generator, wallTorch);
    }

    public static void generateItemModels(ItemModelGenerator generator, Block block) {
        // Generate item model for a single block
        Block wallTorch = Registries.BLOCK.get(WesterosBlocks.id("wall_" + block.getTranslationKey().replace("block.westerosblocks.", "")));
        generateItemModelsAuto(generator, block, wallTorch);
    }

    // Utility methods

    private static BlockStateVariantMap createCardinalVariants(Identifier modelId) {
        return BlockStateVariantMap.create(Properties.HORIZONTAL_FACING)
            .register(Direction.NORTH, createVariant(modelId))
            .register(Direction.SOUTH, createVariant(modelId, 180))
            .register(Direction.EAST, createVariant(modelId, 90))
            .register(Direction.WEST, createVariant(modelId, 270));
    }

    private static Identifier createModelId(Block block) {
        String blockName = getBlockName(block);
        return WesterosBlocks.id("block/" + blockName + "/" + blockName);
    }

    private static Identifier createBlockIdentifier(String texturePath) {
        if (texturePath != null && texturePath.contains(":")) {
            String namespace = texturePath.substring(0, texturePath.indexOf(':'));
            String path = texturePath.substring(texturePath.indexOf(':') + 1);
            return Identifier.of(namespace, path);
        }
        return WesterosBlocks.id("block/" + texturePath);
    }

    private static BlockStateVariant createVariant(Identifier modelId, int rotation) {
        Rotation rotationEnum = switch (rotation) {
            case 0 -> Rotation.R0;
            case 90 -> Rotation.R90;
            case 180 -> Rotation.R180;
            case 270 -> Rotation.R270;
            default -> throw new IllegalArgumentException("Invalid rotation: " + rotation);
        };
        return BlockStateVariant.create().put(VariantSettings.MODEL, modelId).put(VariantSettings.Y, rotationEnum);
    }

    private static BlockStateVariant createVariant(Identifier modelId) {
        return BlockStateVariant.create().put(VariantSettings.MODEL, modelId);
    }

    private static void generateItemModel(ItemModelGenerator generator, Block block) {
        Identifier itemModelId = ModelIds.getItemModelId(block.asItem());
        String texturePath = getTexturePath(block);
        TextureMap textureMap = TextureMap.layer0(createBlockIdentifier(texturePath));
        Models.GENERATED.upload(itemModelId, textureMap, generator.writer);
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

    private static String getBlockName(Block block) {
        String blockString = block.toString();
        if (blockString.contains(":")) {
            return blockString.split(":")[1].replace("}", "");
        }
        return blockString.toLowerCase().replace("block{", "").replace("}", "");
    }
}