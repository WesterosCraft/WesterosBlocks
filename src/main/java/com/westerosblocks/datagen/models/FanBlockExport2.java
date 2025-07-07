package com.westerosblocks.datagen.models;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.custom.WCFanBlock;
import com.westerosblocks.block.custom.WCWallFanBlock;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.Optional;

public class FanBlockExport2 extends ModelExport2 {

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator generator, Block standingFan, String texturePath) {
        // Get the wall fan block using the same pattern as TorchBlockExport
        Block wallFan = Registries.BLOCK.get(WesterosBlocks.id("wall_" + standingFan.getTranslationKey().replace("block.westerosblocks.", "")));

        // Generate standing fan block state
        generateStandingFanBlockState(generator, standingFan, texturePath);
        
        // Generate wall fan block state if it exists
        if (wallFan != null) {
            generateWallFanBlockState(generator, wallFan, texturePath);
        }
    }

    private static void generateStandingFanBlockState(BlockStateModelGenerator generator, Block block, String texturePath) {
        // Create the standing fan model
        Identifier standingModelId = createStandingFanModel(generator, block, texturePath);

        // Create block state variants for waterlogged states
        BlockStateVariantMap variants = BlockStateVariantMap.create(WCFanBlock.WATERLOGGED)
            .register(false, createVariant(standingModelId))
            .register(true, createVariant(standingModelId));

        // Register the block state using ModelExport2 utility method
        registerBlockState(generator, block, variants);
    }

    private static void generateWallFanBlockState(BlockStateModelGenerator generator, Block block, String texturePath) {
        // Create the wall fan model
        Identifier wallModelId = createWallFanModel(generator, block, texturePath);

        // Create variants for each facing direction and waterlogged state
        BlockStateVariantMap variants = BlockStateVariantMap.create(WCWallFanBlock.FACING, WCWallFanBlock.WATERLOGGED)
            .register(Direction.NORTH, false, createVariant(wallModelId))
            .register(Direction.NORTH, true, createVariant(wallModelId))
            .register(Direction.EAST, false, createVariant(wallModelId, 90))
            .register(Direction.EAST, true, createVariant(wallModelId, 90))
            .register(Direction.SOUTH, false, createVariant(wallModelId, 180))
            .register(Direction.SOUTH, true, createVariant(wallModelId, 180))
            .register(Direction.WEST, false, createVariant(wallModelId, 270))
            .register(Direction.WEST, true, createVariant(wallModelId, 270));

        // Register the block state using the generator's collector directly
        generator.blockStateCollector.accept(
            VariantsBlockStateSupplier.create(block)
                .coordinate(variants)
        );
    }

    private static Identifier createStandingFanModel(BlockStateModelGenerator generator, Block block, String texturePath) {
        // Create texture map for standing fan
        TextureMap textureMap = new TextureMap()
            .put(TextureKey.FAN, createBlockIdentifier(texturePath));

        // Create model identifier using ModelExport2 utility method
        Identifier modelId = createModelId(block);

        // Create and upload the model using the template
        Model model = new Model(
            Optional.of(WesterosBlocks.id("block/untinted/fan")),
            Optional.empty(),
            TextureKey.FAN
        );
        model.upload(modelId, textureMap, generator.modelCollector);

        return modelId;
    }

    private static Identifier createWallFanModel(BlockStateModelGenerator generator, Block block, String texturePath) {
        // Create texture map for wall fan
        TextureMap textureMap = new TextureMap()
            .put(TextureKey.FAN, createBlockIdentifier(texturePath));

        // Create model identifier using ModelExport2 utility method
        Identifier modelId = createModelId(block);

        // Create and upload the model using the template
        Model model = new Model(
            Optional.of(WesterosBlocks.id("block/untinted/wall_fan")),
            Optional.empty(),
            TextureKey.FAN
        );
        model.upload(modelId, textureMap, generator.modelCollector);

        return modelId;
    }



    public static void generateItemModelsAuto(ItemModelGenerator generator, Block standingFan, Block wallFan) {
        // Generate item models for both standing and wall fan blocks using ModelExport2 utility method
        generateItemModel(generator, standingFan);
        if (wallFan != null) {
            generateItemModel(generator, wallFan);
        }
    }

    @Override
    public void generateItemModels(ItemModelGenerator generator, Block block) {
        // Generate item model for a single block using the same pattern as TorchBlockExport
        Block wallFan = Registries.BLOCK.get(WesterosBlocks.id("wall_" + block.getTranslationKey().replace("block.westerosblocks.", "")));
        generateItemModelsAuto(generator, block, wallFan);
    }
} 