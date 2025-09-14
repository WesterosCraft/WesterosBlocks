package com.westerosblocks.datagen.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.datagen.ModModels;
import com.westerosblocks.block.custom.WCHalfDoorBlock;
import net.minecraft.block.Block;
import net.minecraft.block.enums.DoorHinge;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.data.client.VariantSettings.Rotation;

import java.util.Optional;

public class HalfDoorBlockExporter extends BaseBlockExporter {

    /**
     * Generates block state models for a half door block.
     * 
     * @param generator   The BlockStateModelGenerator to register models with
     * @param block       The half door block to generate models for
     * @param texturePath The texture path for the half door
     */
    public static void registerHalfDoorBlock(BlockStateModelGenerator generator, Block block, String texturePath) {
        // Create the base models for each half door variant
        Identifier bottomLeftModelId = createHalfDoorModel(generator, block, texturePath, "left", ModModels.HALF_DOOR_LEFT);
        Identifier bottomRightModelId = createHalfDoorModel(generator, block, texturePath, "right", ModModels.HALF_DOOR_RIGHT);
        Identifier bottomLeftOpenModelId = createHalfDoorModel(generator, block, texturePath, "left_open", ModModels.HALF_DOOR_LEFT_OPEN);
        Identifier bottomRightOpenModelId = createHalfDoorModel(generator, block, texturePath, "right_open", ModModels.HALF_DOOR_RIGHT_OPEN);

        // Create variants for all half door states
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
        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block).coordinate(variants));

        // Create 2D item model using the texture
        Identifier itemModelId = ModelIds.getItemModelId(block.asItem());
        TextureMap itemTextureMap = TextureMap.layer0(createBlockIdentifier(texturePath));
        Models.GENERATED.upload(itemModelId, itemTextureMap, generator.modelCollector);
    }

    /**
     * Creates a half door model with the specified variant using the same approach as the working old code.
     * 
     * @param generator   The BlockStateModelGenerator to register the model with
     * @param block       The block this model is for
     * @param texturePath The texture path to use
     * @param variant     The variant name (e.g., "left", "right_open")
     * @param model       The predefined model from ModModels to use
     * @return The created model Identifier
     */
    private static Identifier createHalfDoorModel(BlockStateModelGenerator generator, Block block, String texturePath, String variant, Model model) {
        String modelPath = "block/" + block.getTranslationKey().replace("block.westerosblocks.", "") + "/" + block.getTranslationKey().replace("block.westerosblocks.", "") + "_" + variant;
        Identifier modelId = WesterosBlocks.id(modelPath);

        // Create texture map with bottom texture (matching the working old code approach)
        TextureMap textureMap = new TextureMap()
            .put(TextureKey.BOTTOM, createBlockIdentifier(texturePath));

        // Create custom model with parent model path (matching old working approach)
        String parentModelPath = "block/untinted/" + getParentModelName(variant);
        
        Model doorModel = new Model(
            Optional.of(WesterosBlocks.id(parentModelPath)),
            Optional.empty(),
            TextureKey.BOTTOM
        );
        doorModel.upload(modelId, textureMap, generator.modelCollector);

        return modelId;
    }

    /**
     * Maps half door variants to their parent model names (matching old working code).
     * 
     * @param variant The half door variant
     * @return The parent model name
     */
    private static String getParentModelName(String variant) {
        return switch (variant) {
            case "left" -> "half_door_left";
            case "right" -> "half_door_right";
            case "left_open" -> "half_door_left_open";
            case "right_open" -> "half_door_right_open";
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
    public static void generateItemModels(ItemModelGenerator generator, Block block, String texturePath) {
        // Create a simple item model that uses the block texture
        Identifier modelId = ModelIds.getItemModelId(block.asItem());
        TextureMap textureMap = TextureMap.layer0(createBlockIdentifier(texturePath));
        
        Models.GENERATED.upload(modelId, textureMap, generator.writer);
    }
}
