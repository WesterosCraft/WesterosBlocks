package com.westerosblocks.datagen.models;

import net.minecraft.block.Block;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

/**
 * Standalone trap door block export for generating block states and models.
 * This class is decoupled from the definition file system and follows Fabric datagen patterns.
 */
public class StandaloneTrapDoorBlockExport {

    /**
     * Generates block state models for a trap door block.
     * 
     * @param generator The BlockStateModelGenerator to register models with
     * @param block The trap door block to generate models for
     * @param texturePath The texture path for the trap door
     */
    public static void generateBlockStateModels(BlockStateModelGenerator generator, Block block, String texturePath) {
        // Create the base models for each trap door state
        Identifier bottomModelId = createTrapDoorModel(generator, block, texturePath, "bottom");
        Identifier topModelId = createTrapDoorModel(generator, block, texturePath, "top");
        Identifier openModelId = createTrapDoorModel(generator, block, texturePath, "open");

        // Create variants for all trap door states
        BlockStateVariantMap variants = BlockStateVariantMap.create(TrapdoorBlock.FACING, TrapdoorBlock.OPEN, TrapdoorBlock.HALF)
            // Bottom half, closed
            .register(Direction.NORTH, false, BlockHalf.BOTTOM, BlockStateVariant.create()
                .put(VariantSettings.MODEL, bottomModelId))
            .register(Direction.EAST, false, BlockHalf.BOTTOM, BlockStateVariant.create()
                .put(VariantSettings.MODEL, bottomModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R90))
            .register(Direction.SOUTH, false, BlockHalf.BOTTOM, BlockStateVariant.create()
                .put(VariantSettings.MODEL, bottomModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R180))
            .register(Direction.WEST, false, BlockHalf.BOTTOM, BlockStateVariant.create()
                .put(VariantSettings.MODEL, bottomModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R270))

            // Top half, closed
            .register(Direction.NORTH, false, BlockHalf.TOP, BlockStateVariant.create()
                .put(VariantSettings.MODEL, topModelId))
            .register(Direction.EAST, false, BlockHalf.TOP, BlockStateVariant.create()
                .put(VariantSettings.MODEL, topModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R90))
            .register(Direction.SOUTH, false, BlockHalf.TOP, BlockStateVariant.create()
                .put(VariantSettings.MODEL, topModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R180))
            .register(Direction.WEST, false, BlockHalf.TOP, BlockStateVariant.create()
                .put(VariantSettings.MODEL, topModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R270))

            // Bottom half, open
            .register(Direction.NORTH, true, BlockHalf.BOTTOM, BlockStateVariant.create()
                .put(VariantSettings.MODEL, openModelId))
            .register(Direction.EAST, true, BlockHalf.BOTTOM, BlockStateVariant.create()
                .put(VariantSettings.MODEL, openModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R90))
            .register(Direction.SOUTH, true, BlockHalf.BOTTOM, BlockStateVariant.create()
                .put(VariantSettings.MODEL, openModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R180))
            .register(Direction.WEST, true, BlockHalf.BOTTOM, BlockStateVariant.create()
                .put(VariantSettings.MODEL, openModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R270))

            // Top half, open
            .register(Direction.NORTH, true, BlockHalf.TOP, BlockStateVariant.create()
                .put(VariantSettings.MODEL, openModelId))
            .register(Direction.EAST, true, BlockHalf.TOP, BlockStateVariant.create()
                .put(VariantSettings.MODEL, openModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R90))
            .register(Direction.SOUTH, true, BlockHalf.TOP, BlockStateVariant.create()
                .put(VariantSettings.MODEL, openModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R180))
            .register(Direction.WEST, true, BlockHalf.TOP, BlockStateVariant.create()
                .put(VariantSettings.MODEL, openModelId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R270));

        // Register the block state with the generator
        generator.blockStateCollector.accept(
            VariantsBlockStateSupplier.create(block)
                .coordinate(variants)
        );
    }

    /**
     * Creates a trap door model with the specified variant.
     * 
     * @param generator The BlockStateModelGenerator to register the model with
     * @param block The block this model is for
     * @param texturePath The texture path to use
     * @param variant The variant name (bottom, top, open)
     * @return The created model Identifier
     */
    private static Identifier createTrapDoorModel(BlockStateModelGenerator generator, Block block, String texturePath, String variant) {
        // Create a unique model ID for this block and variant
        String modelPath = "block/generated/" + block.getTranslationKey().replace("block.westerosblocks.", "") + "_" + variant;
        Identifier modelId = Identifier.of("westerosblocks", modelPath);

        // Create texture map
        TextureMap textureMap = new TextureMap()
            .put(TextureKey.TEXTURE, Identifier.of(texturePath))
            .put(TextureKey.PARTICLE, Identifier.of(texturePath));

        // Use the correct vanilla template models like the existing TrapDoorBlockExport
        switch (variant) {
            case "bottom":
                Models.TEMPLATE_TRAPDOOR_BOTTOM.upload(modelId, textureMap, generator.modelCollector);
                break;
            case "top":
                Models.TEMPLATE_TRAPDOOR_TOP.upload(modelId, textureMap, generator.modelCollector);
                break;
            case "open":
                Models.TEMPLATE_TRAPDOOR_OPEN.upload(modelId, textureMap, generator.modelCollector);
                break;
        }

        return modelId;
    }

    /**
     * Generates item models for a trap door block.
     * 
     * @param generator The ItemModelGenerator to register models with
     * @param block The trap door block to generate item models for
     */
    public static void generateItemModels(ItemModelGenerator generator, Block block) {
        // Create a simple item model that inherits from the block model
        String modelPath = "block/generated/" + block.getTranslationKey().replace("block.westerosblocks.", "") + "_bottom";
        Model model = new Model(
            java.util.Optional.of(Identifier.of("westerosblocks", modelPath)),
            java.util.Optional.empty()
        );
        generator.register(block.asItem(), model);
    }
} 