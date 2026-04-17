package com.westerosblocks.datagen.custom;

import com.westerosblocks.datagen.ModModels;
import com.westerosblocks.block.custom.WCHalfDoorBlock;
import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.Block;
import net.minecraft.block.enums.DoorHinge;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.List;

public class HalfDoorBlockExporter extends BaseBlockExporter {

    public static void registerHalfDoorBlock(BlockStateModelGenerator generator, Block block, String texturePath) {
        Identifier leftModelId = createHalfDoorModel(generator, block, texturePath, "left", ModModels.HALF_DOOR_LEFT);
        Identifier rightModelId = createHalfDoorModel(generator, block, texturePath, "right", ModModels.HALF_DOOR_RIGHT);
        Identifier leftOpenModelId = createHalfDoorModel(generator, block, texturePath, "left_open", ModModels.HALF_DOOR_LEFT_OPEN);
        Identifier rightOpenModelId = createHalfDoorModel(generator, block, texturePath, "right_open", ModModels.HALF_DOOR_RIGHT_OPEN);

        BlockStateVariantMap variants = createHalfDoorVariants(leftModelId, rightModelId, leftOpenModelId, rightOpenModelId);
        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block).coordinate(variants));
    }

    public static void registerCustomHalfDoorBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        registerHalfDoorBlock(generator, block, resolveTexturePath(definition));
    }

    /** Item-model pass: generates an {@code item/generated} model with layer0 = the block's bottom texture. */
    public static void registerCustomHalfDoorItemModel(ItemModelGenerator generator, Block block, BlockDefinition definition) {
        registerSimpleItemModel(generator, block, createBlockIdentifier(resolveTexturePath(definition)));
    }

    private static String resolveTexturePath(BlockDefinition definition) {
        List<String> textureList = definition.getTextures();
        return (textureList != null && !textureList.isEmpty()) ? textureList.get(0) : "missingno";
    }

    private static TextureMap createHalfDoorTextureMap(String texturePath) {

        return new TextureMap()
                .put(TextureKey.BOTTOM, createBlockIdentifier(texturePath))
                .put(TextureKey.PARTICLE, createBlockIdentifier(texturePath));
    }

    private static BlockStateVariantMap createHalfDoorVariants(Identifier leftModelId, Identifier rightModelId,
                                                                Identifier leftOpenModelId, Identifier rightOpenModelId) {
        return BlockStateVariantMap.create(WCHalfDoorBlock.FACING, WCHalfDoorBlock.HINGE, WCHalfDoorBlock.OPEN)
            // EAST facing
            .register(Direction.EAST, DoorHinge.LEFT, false, createVariant(leftModelId))
            .register(Direction.EAST, DoorHinge.LEFT, true, createVariant(leftOpenModelId, 90))
            .register(Direction.EAST, DoorHinge.RIGHT, false, createVariant(rightModelId))
            .register(Direction.EAST, DoorHinge.RIGHT, true, createVariant(rightOpenModelId, 270))
            // SOUTH facing
            .register(Direction.SOUTH, DoorHinge.LEFT, false, createVariant(leftModelId, 90))
            .register(Direction.SOUTH, DoorHinge.LEFT, true, createVariant(leftOpenModelId, 180))
            .register(Direction.SOUTH, DoorHinge.RIGHT, false, createVariant(rightModelId, 90))
            .register(Direction.SOUTH, DoorHinge.RIGHT, true, createVariant(rightOpenModelId, 0))
            // WEST facing
            .register(Direction.WEST, DoorHinge.LEFT, false, createVariant(leftModelId, 180))
            .register(Direction.WEST, DoorHinge.LEFT, true, createVariant(leftOpenModelId, 270))
            .register(Direction.WEST, DoorHinge.RIGHT, false, createVariant(rightModelId, 180))
            .register(Direction.WEST, DoorHinge.RIGHT, true, createVariant(rightOpenModelId, 90))
            // NORTH facing
            .register(Direction.NORTH, DoorHinge.LEFT, false, createVariant(leftModelId, 270))
            .register(Direction.NORTH, DoorHinge.LEFT, true, createVariant(leftOpenModelId, 0))
            .register(Direction.NORTH, DoorHinge.RIGHT, false, createVariant(rightModelId, 270))
            .register(Direction.NORTH, DoorHinge.RIGHT, true, createVariant(rightOpenModelId, 180));
    }

    private static Identifier createHalfDoorModel(BlockStateModelGenerator generator, Block block, String texturePath,
                                                   String variant, Model model) {
        String blockName = getBlockName(block);
        Identifier modelId = createNestedModelId(block, blockName + "_" + variant);
        TextureMap textureMap = createHalfDoorTextureMap(texturePath);
        model.upload(modelId, textureMap, generator.modelCollector);
        return modelId;
    }
}
