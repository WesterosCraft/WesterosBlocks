package com.westerosblocks.datagen.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.custom.WCTableBlock;
import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.datagen.ModTextureKey;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.List;
import java.util.Optional;

public class TableBlockExporter extends BaseBlockExporter {

    public static void registerCustomTableBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        List<String> textureList = definition.getTextures();
        String texturePath = (textureList != null && !textureList.isEmpty()) ? textureList.getFirst() : "missingno";

        registerCustomTableBlock(generator, block, texturePath, texturePath);
    }

    private static void registerCustomTableBlock(BlockStateModelGenerator generator, Block block, String texturePath, String particleTexture) {
        String blockName = getBlockName(block);

        TextureMap textureMap = new TextureMap()
                .put(ModTextureKey.TABLE, createBlockIdentifier(texturePath))
                .put(TextureKey.PARTICLE, createBlockIdentifier(particleTexture));

        // Create model parents referencing the custom table models
        Model singleParent = new Model(Optional.of(WesterosBlocks.id("block/table/wood_table_1x1")), Optional.empty(), ModTextureKey.TABLE, TextureKey.PARTICLE);
        Model leftParent = new Model(Optional.of(WesterosBlocks.id("block/table/wood_table_left")), Optional.empty(), ModTextureKey.TABLE, TextureKey.PARTICLE);
        Model rightParent = new Model(Optional.of(WesterosBlocks.id("block/table/wood_table_right")), Optional.empty(), ModTextureKey.TABLE, TextureKey.PARTICLE);
        Model middleParent = new Model(Optional.of(WesterosBlocks.id("block/table/wood_table_middle")), Optional.empty(), ModTextureKey.TABLE, TextureKey.PARTICLE);

        Identifier singleModelId = singleParent.upload(createNestedModelId(block, "single"), textureMap, generator.modelCollector);
        Identifier leftModelId = leftParent.upload(createNestedModelId(block, "left"), textureMap, generator.modelCollector);
        Identifier rightModelId = rightParent.upload(createNestedModelId(block, "right"), textureMap, generator.modelCollector);
        Identifier middleModelId = middleParent.upload(createNestedModelId(block, "middle"), textureMap, generator.modelCollector);

        BlockStateVariantMap.DoubleProperty<Direction, WCTableBlock.ConnectionType> variantMap =
            BlockStateVariantMap.create(WCTableBlock.FACING, WCTableBlock.CONNECTION);

        // Register all combinations of facing and connection
        for (Direction facing : Direction.Type.HORIZONTAL) {
            int rotation = getRotationForFacing(facing);

            // SINGLE connection
            variantMap.register(facing, WCTableBlock.ConnectionType.SINGLE,
                    BlockStateVariant.create()
                            .put(VariantSettings.MODEL, singleModelId)
                            .put(VariantSettings.Y, getRotationEnum(rotation)));

            // LEFT connection (rotated 90 degrees clockwise)
            variantMap.register(facing, WCTableBlock.ConnectionType.LEFT,
                    BlockStateVariant.create()
                            .put(VariantSettings.MODEL, leftModelId)
                            .put(VariantSettings.Y, getRotationEnum((rotation + 90) % 360)));

            // RIGHT connection
            variantMap.register(facing, WCTableBlock.ConnectionType.RIGHT,
                    BlockStateVariant.create()
                            .put(VariantSettings.MODEL, rightModelId)
                            .put(VariantSettings.Y, getRotationEnum(rotation)));

            // MIDDLE connection
            variantMap.register(facing, WCTableBlock.ConnectionType.MIDDLE,
                    BlockStateVariant.create()
                            .put(VariantSettings.MODEL, middleModelId)
                            .put(VariantSettings.Y, getRotationEnum(rotation)));
        }

        // Register the blockstate with all variants
        generator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(block).coordinate(variantMap));

        registerParentedItemModel(generator, block, singleModelId);
    }

    private static int getRotationForFacing(Direction facing) {
        return switch (facing) {
            case NORTH -> 180;
            case SOUTH -> 0;
            case WEST -> 90;
            case EAST -> 270;
            default -> 0;
        };
    }

    private static VariantSettings.Rotation getRotationEnum(int degrees) {
        return switch (degrees) {
            case 0 -> VariantSettings.Rotation.R0;
            case 90 -> VariantSettings.Rotation.R90;
            case 180 -> VariantSettings.Rotation.R180;
            case 270 -> VariantSettings.Rotation.R270;
            default -> VariantSettings.Rotation.R0;
        };
    }
}
