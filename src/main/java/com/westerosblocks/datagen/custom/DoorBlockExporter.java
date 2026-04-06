package com.westerosblocks.datagen.custom;

import com.westerosblocks.datagen.ModModels;
import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.Block;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.block.enums.DoorHinge;
import net.minecraft.client.data.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

public class DoorBlockExporter extends BaseBlockExporter {

    public static void registerDoorBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition, String... texturePaths) {
        validateTexturePaths(texturePaths, 2);

        String topTexture = texturePaths[0];
        String bottomTexture = texturePaths[1];

        Identifier bottomLeftModelId = createDoorModel(generator, block, topTexture, bottomTexture, "bottom_left", ModModels.DOOR_BOTTOM_LEFT);
        Identifier bottomRightModelId = createDoorModel(generator, block, topTexture, bottomTexture, "bottom_right", ModModels.DOOR_BOTTOM_RIGHT);
        Identifier bottomLeftOpenModelId = createDoorModel(generator, block, topTexture, bottomTexture, "bottom_left_open", ModModels.DOOR_BOTTOM_LEFT_OPEN);
        Identifier bottomRightOpenModelId = createDoorModel(generator, block, topTexture, bottomTexture, "bottom_right_open", ModModels.DOOR_BOTTOM_RIGHT_OPEN);
        Identifier topLeftModelId = createDoorModel(generator, block, topTexture, bottomTexture, "top_left", ModModels.DOOR_TOP_LEFT);
        Identifier topRightModelId = createDoorModel(generator, block, topTexture, bottomTexture, "top_right", ModModels.DOOR_TOP_RIGHT);
        Identifier topLeftOpenModelId = createDoorModel(generator, block, topTexture, bottomTexture, "top_left_open", ModModels.DOOR_TOP_LEFT_OPEN);
        Identifier topRightOpenModelId = createDoorModel(generator, block, topTexture, bottomTexture, "top_right_open", ModModels.DOOR_TOP_RIGHT_OPEN);

        BlockStateVariantMap variants = createDoorVariants(
                bottomLeftModelId, bottomRightModelId, bottomLeftOpenModelId, bottomRightOpenModelId,
                topLeftModelId, topRightModelId, topLeftOpenModelId, topRightOpenModelId);

        generator.blockStateCollector.accept(VariantsBlockModelDefinitionCreator.of(block).with(variants));

        Identifier itemTextureId;
        if (definition.hasCustomItemTexture()) {
            String blockName = getBlockName(block);
            itemTextureId = Identifier.of("westerosblocks", "item/" + blockName);
        } else if (definition.hasItemTexture()) {
            itemTextureId = createBlockIdentifier(definition.getItemTexture());
        } else {
            itemTextureId = createBlockIdentifier(bottomTexture);
        }
        registerSimpleItemModel(generator, block, itemTextureId);
    }

    private static BlockStateVariantMap createDoorVariants(
            Identifier bottomLeftModelId, Identifier bottomRightModelId,
            Identifier bottomLeftOpenModelId, Identifier bottomRightOpenModelId,
            Identifier topLeftModelId, Identifier topRightModelId,
            Identifier topLeftOpenModelId, Identifier topRightOpenModelId) {

        return BlockStateVariantMap.models(DoorBlock.FACING, DoorBlock.HALF, DoorBlock.HINGE, DoorBlock.OPEN)
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

    public static void registerCustomDoorBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        String[] textures = definition.getTexturesAsArray();

        registerDoorBlock(generator, block, definition, textures[0], textures[1]);
    }


    private static Identifier createDoorModel(BlockStateModelGenerator generator, Block block, String topTexture,
                                              String bottomTexture, String variant, Model model) {
        TextureMap textureMap = new TextureMap()
                .put(TextureKey.TOP, createBlockIdentifier(topTexture))
                .put(TextureKey.BOTTOM, createBlockIdentifier(bottomTexture))
                .put(TextureKey.PARTICLE, createBlockIdentifier(bottomTexture));

        Identifier modelId = createNestedModelId(block, variant);
        model.upload(modelId, textureMap, generator.modelCollector);

        return modelId;
    }
}
