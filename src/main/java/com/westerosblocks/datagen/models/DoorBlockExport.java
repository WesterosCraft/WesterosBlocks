package com.westerosblocks.datagen.models;

import com.westerosblocks.WesterosBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.block.enums.DoorHinge;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.Optional;

/**
 * Standalone door block export for generating block states and models.
 * This class extends ModelExport2 and uses BlockStateModelGenerator methods where possible.
 * It produces the same output as the original DoorBlockExport.
 */
public class DoorBlockExport extends ModelExport2 {

    /**
     * Generates block state models for a door block.
     * 
     * @param generator The BlockStateModelGenerator to register models with
     * @param block The door block to generate models for
     * @param texturePath The texture path for the door (legacy single texture support)
     */
    @Override
    public void generateBlockStateModels(BlockStateModelGenerator generator, Block block, String texturePath) {
        // For backward compatibility, use the same texture for both top and bottom
        generateBlockStateModels(generator, block, new String[]{texturePath, texturePath});
    }

    /**
     * Generates block state models for a door block with separate top and bottom textures.
     * 
     * @param generator The BlockStateModelGenerator to register models with
     * @param block The door block to generate models for
     * @param texturePaths Variable number of texture paths [top, bottom] for the door
     */
    public void generateBlockStateModels(BlockStateModelGenerator generator, Block block, String... texturePaths) {
        // Ensure we have at least 2 textures (top and bottom)
        if (texturePaths.length < 2) {
            throw new IllegalArgumentException("Door blocks require at least 2 textures (top and bottom)");
        }
        
        String topTexture = texturePaths[0];
        String bottomTexture = texturePaths[1];
        // Create the base models for each door state
        Identifier bottomLeftModelId = createDoorModel(generator, block, topTexture, bottomTexture, "bottom_left");
        Identifier bottomRightModelId = createDoorModel(generator, block, topTexture, bottomTexture, "bottom_right");
        Identifier bottomLeftOpenModelId = createDoorModel(generator, block, topTexture, bottomTexture, "bottom_left_open");
        Identifier bottomRightOpenModelId = createDoorModel(generator, block, topTexture, bottomTexture, "bottom_right_open");
        Identifier topLeftModelId = createDoorModel(generator, block, topTexture, bottomTexture, "top_left");
        Identifier topRightModelId = createDoorModel(generator, block, topTexture, bottomTexture, "top_right");
        Identifier topLeftOpenModelId = createDoorModel(generator, block, topTexture, bottomTexture, "top_left_open");
        Identifier topRightOpenModelId = createDoorModel(generator, block, topTexture, bottomTexture, "top_right_open");

        // Create variants for all door states using string conditions like the original
        BlockStateVariantMap variants = BlockStateVariantMap.create(DoorBlock.FACING, DoorBlock.HALF, DoorBlock.HINGE, DoorBlock.OPEN)
            // EAST facing
            .register(Direction.EAST, DoubleBlockHalf.LOWER, DoorHinge.LEFT, false, createVariant(bottomLeftModelId))
            .register(Direction.EAST, DoubleBlockHalf.LOWER, DoorHinge.LEFT, true, createVariant(bottomLeftOpenModelId, 90))
            .register(Direction.EAST, DoubleBlockHalf.LOWER, DoorHinge.RIGHT, false, createVariant(bottomRightModelId))
            .register(Direction.EAST, DoubleBlockHalf.LOWER, DoorHinge.RIGHT, true, createVariant(bottomRightOpenModelId, 270))
            .register(Direction.EAST, DoubleBlockHalf.UPPER, DoorHinge.LEFT, false, createVariant(topLeftModelId))
            .register(Direction.EAST, DoubleBlockHalf.UPPER, DoorHinge.LEFT, true, createVariant(topLeftOpenModelId, 90))
            .register(Direction.EAST, DoubleBlockHalf.UPPER, DoorHinge.RIGHT, false, createVariant(topRightModelId))
            .register(Direction.EAST, DoubleBlockHalf.UPPER, DoorHinge.RIGHT, true, createVariant(topRightOpenModelId, 270))

            // SOUTH facing
            .register(Direction.SOUTH, DoubleBlockHalf.LOWER, DoorHinge.LEFT, false, createVariant(bottomLeftModelId, 90))
            .register(Direction.SOUTH, DoubleBlockHalf.LOWER, DoorHinge.LEFT, true, createVariant(bottomLeftOpenModelId, 180))
            .register(Direction.SOUTH, DoubleBlockHalf.LOWER, DoorHinge.RIGHT, false, createVariant(bottomRightModelId, 90))
            .register(Direction.SOUTH, DoubleBlockHalf.LOWER, DoorHinge.RIGHT, true, createVariant(bottomRightOpenModelId, 0))
            .register(Direction.SOUTH, DoubleBlockHalf.UPPER, DoorHinge.LEFT, false, createVariant(topLeftModelId, 90))
            .register(Direction.SOUTH, DoubleBlockHalf.UPPER, DoorHinge.LEFT, true, createVariant(topLeftOpenModelId, 180))
            .register(Direction.SOUTH, DoubleBlockHalf.UPPER, DoorHinge.RIGHT, false, createVariant(topRightModelId, 90))
            .register(Direction.SOUTH, DoubleBlockHalf.UPPER, DoorHinge.RIGHT, true, createVariant(topRightOpenModelId, 0))

            // WEST facing
            .register(Direction.WEST, DoubleBlockHalf.LOWER, DoorHinge.LEFT, false, createVariant(bottomLeftModelId, 180))
            .register(Direction.WEST, DoubleBlockHalf.LOWER, DoorHinge.LEFT, true, createVariant(bottomLeftOpenModelId, 270))
            .register(Direction.WEST, DoubleBlockHalf.LOWER, DoorHinge.RIGHT, false, createVariant(bottomRightModelId, 180))
            .register(Direction.WEST, DoubleBlockHalf.LOWER, DoorHinge.RIGHT, true, createVariant(bottomRightOpenModelId, 90))
            .register(Direction.WEST, DoubleBlockHalf.UPPER, DoorHinge.LEFT, false, createVariant(topLeftModelId, 180))
            .register(Direction.WEST, DoubleBlockHalf.UPPER, DoorHinge.LEFT, true, createVariant(topLeftOpenModelId, 270))
            .register(Direction.WEST, DoubleBlockHalf.UPPER, DoorHinge.RIGHT, false, createVariant(topRightModelId, 180))
            .register(Direction.WEST, DoubleBlockHalf.UPPER, DoorHinge.RIGHT, true, createVariant(topRightOpenModelId, 90))

            // NORTH facing
            .register(Direction.NORTH, DoubleBlockHalf.LOWER, DoorHinge.LEFT, false, createVariant(bottomLeftModelId, 270))
            .register(Direction.NORTH, DoubleBlockHalf.LOWER, DoorHinge.LEFT, true, createVariant(bottomLeftOpenModelId, 0))
            .register(Direction.NORTH, DoubleBlockHalf.LOWER, DoorHinge.RIGHT, false, createVariant(bottomRightModelId, 270))
            .register(Direction.NORTH, DoubleBlockHalf.LOWER, DoorHinge.RIGHT, true, createVariant(bottomRightOpenModelId, 180))
            .register(Direction.NORTH, DoubleBlockHalf.UPPER, DoorHinge.LEFT, false, createVariant(topLeftModelId, 270))
            .register(Direction.NORTH, DoubleBlockHalf.UPPER, DoorHinge.LEFT, true, createVariant(topLeftOpenModelId, 0))
            .register(Direction.NORTH, DoubleBlockHalf.UPPER, DoorHinge.RIGHT, false, createVariant(topRightModelId, 270))
            .register(Direction.NORTH, DoubleBlockHalf.UPPER, DoorHinge.RIGHT, true, createVariant(topRightOpenModelId, 180));

        // Register the block state with the generator
        registerBlockState(generator, block, variants);
    }

    /**
     * Creates a door model with the specified variant.
     * 
     * @param generator The BlockStateModelGenerator to register the model with
     * @param block The block this model is for
     * @param topTexture The top texture path to use
     * @param bottomTexture The bottom texture path to use
     * @param variant The variant name (e.g., "bottom_left", "top_right_open")
     * @return The created model Identifier
     */
    private static Identifier createDoorModel(BlockStateModelGenerator generator, Block block, String topTexture, String bottomTexture, String variant) {
        // Create a unique model ID for this block and variant
        String modelPath = "block/generated/" + block.getTranslationKey().replace("block.westerosblocks.", "") + "_" + variant;
        Identifier modelId = WesterosBlocks.id(modelPath);

        // Create texture map with separate top and bottom textures
        TextureMap textureMap = new TextureMap()
            .put(TextureKey.TOP, createBlockIdentifier(topTexture))
            .put(TextureKey.BOTTOM, createBlockIdentifier(bottomTexture));

        // Determine the parent model based on the variant
        String parentModelPath = "block/" + getParentModelName(variant);

        // Create and upload the model
        Model doorModel = new Model(
            Optional.of(Identifier.ofVanilla(parentModelPath)),
            Optional.empty(),
            TextureKey.TOP,
            TextureKey.BOTTOM
        );
        doorModel.upload(modelId, textureMap, generator.modelCollector);

        return modelId;
    }

    /**
     * Maps door variants to their parent model names.
     * 
     * @param variant The door variant
     * @return The parent model name
     */
    private static String getParentModelName(String variant) {
        return switch (variant) {
            case "bottom_left" -> "door_bottom_left";
            case "bottom_right" -> "door_bottom_right";
            case "bottom_left_open" -> "door_bottom_left_open";
            case "bottom_right_open" -> "door_bottom_right_open";
            case "top_left" -> "door_top_left";
            case "top_right" -> "door_top_right";
            case "top_left_open" -> "door_top_left_open";
            case "top_right_open" -> "door_top_right_open";
            default -> throw new IllegalArgumentException("Unknown door variant: " + variant);
        };
    }

    /**
     * Generates item models for a door block.
     * 
     * @param generator The ItemModelGenerator to register the model with
     * @param block The door block to generate item models for
     */
    @Override
    public void generateItemModels(ItemModelGenerator generator, Block block) {
        // Create a simple item model that inherits from the block model
        String modelPath = "block/generated/" + block.getTranslationKey().replace("block.westerosblocks.", "");
        Model model = new Model(
            Optional.of(WesterosBlocks.id(modelPath)),
            Optional.empty()
        );
        generator.register(block.asItem(), model);
    }

    /**
     * Generates item models for a door block with a specific texture.
     * 
     * @param generator The ItemModelGenerator to register the model with
     * @param block The door block to generate item models for
     * @param texturePath The texture path to use for the item model
     */
    public void generateItemModels(ItemModelGenerator generator, Block block, String texturePath) {
        TextureMap textureMap = TextureMap.layer0(createItemIdentifier(texturePath));
        
        Models.GENERATED.upload(
            ModelIds.getItemModelId(block.asItem()),
            textureMap,
            generator.writer
        );
    }

    /**
     * Creates an identifier for item textures, handling namespaces properly.
     * Unlike createBlockIdentifier, this doesn't prepend "block/" for item textures.
     * 
     * @param texturePath The texture path (can include namespace like "westerosblocks:item/white_door")
     * @return The identifier for the item texture
     */
    private static Identifier createItemIdentifier(String texturePath) {
        // If the texture path includes a namespace
        if (texturePath != null && texturePath.contains(":")) {
            String namespace = texturePath.substring(0, texturePath.indexOf(':'));
            String path = texturePath.substring(texturePath.indexOf(':') + 1);
            return Identifier.of(namespace, path);
        }
        // No namespace, use mod ID and assume it's already the correct path
        return WesterosBlocks.id(texturePath);
    }

} 