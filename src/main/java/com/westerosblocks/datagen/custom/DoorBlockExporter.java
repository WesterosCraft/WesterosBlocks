package com.westerosblocks.datagen.custom;

import com.westerosblocks.datagen.ModModels;
import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.Block;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.block.enums.DoorHinge;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.List;

public class DoorBlockExporter extends BaseBlockExporter {

    /**
     * Registers a door block with top and bottom textures.
     * Follows block-models.md section 3.5: Doors and Trapdoors.
     *
     * @param generator    The BlockStateModelGenerator to register models with
     * @param block        The door block to generate models for
     * @param texturePaths Texture paths [top, bottom] for the door
     */
    public static void registerDoorBlock(BlockStateModelGenerator generator, Block block, String... texturePaths) {
        validateTexturePaths(texturePaths, 2);

        String topTexture = texturePaths[0];
        String bottomTexture = texturePaths[1];

        // Create all door models using simplified helper
        Identifier bottomLeftModelId = createDoorModel(generator, block, topTexture, bottomTexture, "bottom_left", ModModels.DOOR_BOTTOM_LEFT);
        Identifier bottomRightModelId = createDoorModel(generator, block, topTexture, bottomTexture, "bottom_right", ModModels.DOOR_BOTTOM_RIGHT);
        Identifier bottomLeftOpenModelId = createDoorModel(generator, block, topTexture, bottomTexture, "bottom_left_open", ModModels.DOOR_BOTTOM_LEFT_OPEN);
        Identifier bottomRightOpenModelId = createDoorModel(generator, block, topTexture, bottomTexture, "bottom_right_open", ModModels.DOOR_BOTTOM_RIGHT_OPEN);
        Identifier topLeftModelId = createDoorModel(generator, block, topTexture, bottomTexture, "top_left", ModModels.DOOR_TOP_LEFT);
        Identifier topRightModelId = createDoorModel(generator, block, topTexture, bottomTexture, "top_right", ModModels.DOOR_TOP_RIGHT);
        Identifier topLeftOpenModelId = createDoorModel(generator, block, topTexture, bottomTexture, "top_left_open", ModModels.DOOR_TOP_LEFT_OPEN);
        Identifier topRightOpenModelId = createDoorModel(generator, block, topTexture, bottomTexture, "top_right_open", ModModels.DOOR_TOP_RIGHT_OPEN);

        // Create and register blockstate with all variants
        BlockStateVariantMap variants = createDoorVariants(
            bottomLeftModelId, bottomRightModelId, bottomLeftOpenModelId, bottomRightOpenModelId,
            topLeftModelId, topRightModelId, topLeftOpenModelId, topRightOpenModelId);
        
        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block).coordinate(variants));
        registerSimpleItemModel(generator, block, createBlockIdentifier(bottomTexture));
    }

    /**
     * Creates all door variant mappings.
     * Extracted method to reduce complexity and follow block-models.md patterns.
     */
    private static BlockStateVariantMap createDoorVariants(
            Identifier bottomLeftModelId, Identifier bottomRightModelId, 
            Identifier bottomLeftOpenModelId, Identifier bottomRightOpenModelId,
            Identifier topLeftModelId, Identifier topRightModelId, 
            Identifier topLeftOpenModelId, Identifier topRightOpenModelId) {
        
        return BlockStateVariantMap.create(DoorBlock.FACING, DoorBlock.HALF, DoorBlock.HINGE, DoorBlock.OPEN)
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
    }

    /**
     * Registers a door block from a BlockDefinition.
     * Automatically extracts textures from the definition and registers the door block.
     *
     * @param generator  The BlockStateModelGenerator to register models with
     * @param block      The door block to generate models for
     * @param definition The block definition containing texture information
     */
    public static void registerCustomDoorBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        if (definition.getTextures() == null || definition.getTextures().isEmpty()) {
            throw new IllegalArgumentException("Door block definition must have textures");
        }

        List<String> textures = definition.getTextures();

        if (textures.size() < 2) {
            throw new IllegalArgumentException("Door block requires at least 2 textures (top and bottom)");
        }

        // Extract top and bottom textures from definition
        String topTexture = textures.get(0);
        String bottomTexture = textures.get(1);

        // Use the existing registerDoorBlock method
        registerDoorBlock(generator, block, topTexture, bottomTexture);
    }

    /**
     * Creates a door model with top and bottom textures.
     * Follows block-models.md texture mapping patterns.
     */
    private static Identifier createDoorModel(BlockStateModelGenerator generator, Block block, String topTexture,
                                              String bottomTexture, String variant, Model model) {
        TextureMap textureMap = new TextureMap()
                .put(TextureKey.TOP, createBlockIdentifier(topTexture))
                .put(TextureKey.BOTTOM, createBlockIdentifier(bottomTexture));

        Identifier modelId = createNestedModelId(block, variant);
        model.upload(modelId, textureMap, generator.modelCollector);

        return modelId;
    }
}
