package com.westerosblocks.datagen.models;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.custom.WCHalfDoorBlock;
import net.minecraft.block.Block;
import net.minecraft.block.enums.DoorHinge;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.Optional;

/**
 * Standalone half door block export for generating block states and models.
 * This class extends ModelExport2 and uses BlockStateModelGenerator methods where possible.
 * It produces the same output as the original HalfDoorBlockExport but without definition dependencies.
 */
public class HalfDoorBlockExport extends ModelExport2 {

    /**
     * Generates block state models for a half door block.
     * 
     * @param generator The BlockStateModelGenerator to register models with
     * @param block The half door block to generate models for
     * @param texturePath The texture path for the half door
     */
    @Override
    public void generateBlockStateModels(BlockStateModelGenerator generator, Block block, String texturePath) {
        // Create the base models for each half door variant
        Identifier bottomLeftModelId = createHalfDoorModel(generator, block, texturePath, texturePath, "bottom_left");
        Identifier bottomRightModelId = createHalfDoorModel(generator, block, texturePath, texturePath, "bottom_right");
        Identifier bottomLeftOpenModelId = createHalfDoorModel(generator, block, texturePath, texturePath, "bottom_left_open");
        Identifier bottomRightOpenModelId = createHalfDoorModel(generator, block, texturePath, texturePath, "bottom_right_open");

        // Create variants for all half door states using BlockStateModelGenerator patterns
        BlockStateVariantMap variants = BlockStateVariantMap.create(
            WCHalfDoorBlock.FACING,
            WCHalfDoorBlock.HINGE,
            WCHalfDoorBlock.OPEN
        )
            // EAST facing
            .register(Direction.EAST, DoorHinge.LEFT, false, createVariant(bottomLeftModelId))
            .register(Direction.EAST, DoorHinge.LEFT, true, createVariant(bottomLeftOpenModelId, 90))
            .register(Direction.EAST, DoorHinge.RIGHT, false, createVariant(bottomRightModelId))
            .register(Direction.EAST, DoorHinge.RIGHT, true, createVariant(bottomRightOpenModelId, 270))

            // SOUTH facing
            .register(Direction.SOUTH, DoorHinge.LEFT, false, createVariant(bottomLeftModelId, 90))
            .register(Direction.SOUTH, DoorHinge.LEFT, true, createVariant(bottomLeftOpenModelId, 180))
            .register(Direction.SOUTH, DoorHinge.RIGHT, false, createVariant(bottomRightModelId, 90))
            .register(Direction.SOUTH, DoorHinge.RIGHT, true, createVariant(bottomRightOpenModelId, 0))

            // WEST facing
            .register(Direction.WEST, DoorHinge.LEFT, false, createVariant(bottomLeftModelId, 180))
            .register(Direction.WEST, DoorHinge.LEFT, true, createVariant(bottomLeftOpenModelId, 270))
            .register(Direction.WEST, DoorHinge.RIGHT, false, createVariant(bottomRightModelId, 180))
            .register(Direction.WEST, DoorHinge.RIGHT, true, createVariant(bottomRightOpenModelId, 90))

            // NORTH facing
            .register(Direction.NORTH, DoorHinge.LEFT, false, createVariant(bottomLeftModelId, 270))
            .register(Direction.NORTH, DoorHinge.LEFT, true, createVariant(bottomLeftOpenModelId, 0))
            .register(Direction.NORTH, DoorHinge.RIGHT, false, createVariant(bottomRightModelId, 270))
            .register(Direction.NORTH, DoorHinge.RIGHT, true, createVariant(bottomRightOpenModelId, 180));

        // Register the block state with the generator
        registerBlockState(generator, block, variants);
    }

    /**
     * Creates a half door model with the specified variant.
     * 
     * @param generator The BlockStateModelGenerator to register the model with
     * @param block The block this model is for
     * @param topTexture The top texture path to use
     * @param bottomTexture The bottom texture path to use
     * @param variant The variant name (e.g., "bottom_left", "bottom_right_open")
     * @return The created model Identifier
     */
    private static Identifier createHalfDoorModel(BlockStateModelGenerator generator, Block block, String topTexture, String bottomTexture, String variant) {
        // Create a unique model ID for this block and variant
        String modelPath = "block/generated/" + block.getTranslationKey().replace("block.westerosblocks.", "") + "_" + variant;
        Identifier modelId = WesterosBlocks.id(modelPath);

        // Create texture map with bottom texture (parent models only use #bottom)
        TextureMap textureMap = new TextureMap()
            .put(TextureKey.BOTTOM, createBlockIdentifier(bottomTexture));

        // Determine the parent model based on the variant
        String parentModelPath = "block/untinted/" + getParentModelName(variant);

        // Create and upload the model
        Model doorModel = new Model(
            Optional.of(Identifier.of(WesterosBlocks.MOD_ID, parentModelPath)),
            Optional.empty(),
            TextureKey.BOTTOM
        );
        doorModel.upload(modelId, textureMap, generator.modelCollector);

        return modelId;
    }

    /**
     * Maps half door variants to their parent model names.
     * 
     * @param variant The half door variant
     * @return The parent model name
     */
    private static String getParentModelName(String variant) {
        return switch (variant) {
            case "bottom_left" -> "half_door_left";
            case "bottom_right" -> "half_door_right";
            case "bottom_left_open" -> "half_door_left_open";
            case "bottom_right_open" -> "half_door_right_open";
            default -> throw new IllegalArgumentException("Unknown half door variant: " + variant);
        };
    }

    /**
     * Generates item models for a half door block.
     * 
     * @param generator The ItemModelGenerator to register the model with
     * @param block The half door block to generate item models for
     * @param texturePath The texture path to use for the item model
     */
    public void generateItemModels(ItemModelGenerator generator, Block block, String texturePath) {
        // Create a simple item model that uses the block texture
        Identifier modelId = ModelIds.getItemModelId(block.asItem());
        TextureMap textureMap = TextureMap.layer0(createBlockIdentifier(texturePath));
        
        Models.GENERATED.upload(modelId, textureMap, generator.writer);
    }

    /**
     * Generates item models for a half door block using the default texture.
     * 
     * @param generator The ItemModelGenerator to register the model with
     * @param block The half door block to generate item models for
     */
    @Override
    public void generateItemModels(ItemModelGenerator generator, Block block) {
        // Use a default texture path based on the block name
        String defaultTexture = "wood/birch/shutters"; // Default texture
        generateItemModels(generator, block, defaultTexture);
    }
} 