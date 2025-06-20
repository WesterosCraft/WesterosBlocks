package com.westerosblocks.datagen.models;

import com.google.gson.JsonObject;
import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.custom.WCArrowSlitBlock;
import com.westerosblocks.block.custom.WCArrowSlitBlock.ArrowSlitType;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.Optional;

public class ArrowSlitBlockExport {
    private static final String PARTICLE_KEY = "particle";
    private static final String TEXTURE_KEY = "1";

    public static void generateBlockStateModels(BlockStateModelGenerator generator, Block block, String texturePath) {
        // Create the base models for each type
        Identifier singleModelId = createModel(generator, block, ArrowSlitType.SINGLE, texturePath);
        Identifier midModelId = createModel(generator, block, ArrowSlitType.MIDDLE, texturePath);
        Identifier topModelId = createModel(generator, block, ArrowSlitType.TOP, texturePath);
        Identifier bottomModelId = createModel(generator, block, ArrowSlitType.BOTTOM, texturePath);

        // Create variants for each state and direction
        BlockStateVariantMap variants = BlockStateVariantMap.create(WCArrowSlitBlock.FACING, WCArrowSlitBlock.TYPE)
            // Single state
            .register(Direction.NORTH, ArrowSlitType.SINGLE, BlockStateVariant.create()
                .put(VariantSettings.MODEL, singleModelId))
            .register(Direction.EAST, ArrowSlitType.SINGLE, BlockStateVariant.create()
                .put(VariantSettings.MODEL, singleModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R90))
            .register(Direction.SOUTH, ArrowSlitType.SINGLE, BlockStateVariant.create()
                .put(VariantSettings.MODEL, singleModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R180))
            .register(Direction.WEST, ArrowSlitType.SINGLE, BlockStateVariant.create()
                .put(VariantSettings.MODEL, singleModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R270))

            // Middle state
            .register(Direction.NORTH, ArrowSlitType.MIDDLE, BlockStateVariant.create()
                .put(VariantSettings.MODEL, midModelId))
            .register(Direction.EAST, ArrowSlitType.MIDDLE, BlockStateVariant.create()
                .put(VariantSettings.MODEL, midModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R90))
            .register(Direction.SOUTH, ArrowSlitType.MIDDLE, BlockStateVariant.create()
                .put(VariantSettings.MODEL, midModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R180))
            .register(Direction.WEST, ArrowSlitType.MIDDLE, BlockStateVariant.create()
                .put(VariantSettings.MODEL, midModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R270))

            // Bottom state
            .register(Direction.NORTH, ArrowSlitType.BOTTOM, BlockStateVariant.create()
                .put(VariantSettings.MODEL, bottomModelId))
            .register(Direction.EAST, ArrowSlitType.BOTTOM, BlockStateVariant.create()
                .put(VariantSettings.MODEL, bottomModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R90))
            .register(Direction.SOUTH, ArrowSlitType.BOTTOM, BlockStateVariant.create()
                .put(VariantSettings.MODEL, bottomModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R180))
            .register(Direction.WEST, ArrowSlitType.BOTTOM, BlockStateVariant.create()
                .put(VariantSettings.MODEL, bottomModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R270))

            // Top state
            .register(Direction.NORTH, ArrowSlitType.TOP, BlockStateVariant.create()
                .put(VariantSettings.MODEL, topModelId))
            .register(Direction.EAST, ArrowSlitType.TOP, BlockStateVariant.create()
                .put(VariantSettings.MODEL, topModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R90))
            .register(Direction.SOUTH, ArrowSlitType.TOP, BlockStateVariant.create()
                .put(VariantSettings.MODEL, topModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R180))
            .register(Direction.WEST, ArrowSlitType.TOP, BlockStateVariant.create()
                .put(VariantSettings.MODEL, topModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R270));

        // Register the block state
        generator.blockStateCollector.accept(
            VariantsBlockStateSupplier.create(block)
                .coordinate(variants)
        );
    }

    private static Identifier createModel(BlockStateModelGenerator generator, Block block, ArrowSlitType type, String texturePath) {
        JsonObject modelJson = new JsonObject();
        
        // Use the existing models as parent
        String parentModelPath = "westerosblocks:block/custom/arrow_slits/arrow_slit_" + type.asString();
        modelJson.addProperty("parent", parentModelPath);
        modelJson.addProperty("credit", "Generated by WesterosBlocks");

        // Override the texture to use the dynamic texture path
        JsonObject textures = new JsonObject();
        textures.addProperty(TEXTURE_KEY, texturePath);
        textures.addProperty(PARTICLE_KEY, texturePath);
        modelJson.add("textures", textures);

        // Create a unique model ID for this block and type
        String modelPath = "block/generated/" + block.getTranslationKey().replace("block.westerosblocks.", "") + "_" + type.asString();
        Identifier modelId = WesterosBlocks.id(modelPath);

        // Register the model
        generator.modelCollector.accept(modelId, () -> modelJson);

        return modelId;
    }

    public static void generateItemModels(ItemModelGenerator generator, Block block) {
        // Create a simple item model that inherits from the block model
        String modelPath = "block/generated/" + block.getTranslationKey().replace("block.westerosblocks.", "");
        Model model = new Model(
            Optional.of(WesterosBlocks.id(modelPath + "_single")),
            Optional.empty()
        );
        generator.register(block.asItem(), model);
    }
} 