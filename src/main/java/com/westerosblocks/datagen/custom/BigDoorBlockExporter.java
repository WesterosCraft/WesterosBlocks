package com.westerosblocks.datagen.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.custom.WCBigDoorBlock;
import com.westerosblocks.block.custom.WCBigDoorBlock.BigDoorPart;
import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.Optional;

/**
 * Exporter for big door (3x3 multiblock) blocks.
 * The animated BlockEntity renderer draws the door, so blockstate variants all
 * resolve to an empty model regardless of OPEN. The item model uses a dedicated icon.
 */
public class BigDoorBlockExporter extends BaseBlockExporter {

    public static void registerCustomBigDoorBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        String[] textures = definition.getTexturesAsArray();
        validateTexturePaths(textures, 1);

        String texture = textures[0];
        boolean isCustomModel = definition.hasCustomModel();

        if (isCustomModel) {
            registerCustomModelBigDoor(generator, block, definition);
        } else {
            registerGeneratedBigDoor(generator, block, texture);
        }
    }

    private static void registerCustomModelBigDoor(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        String blockName = getBlockName(block);

        // OPEN is omitted from the variant key: the animated BE draws the door,
        // so the static model is identical for open/closed. Each variant key
        // wildcards OPEN, halving the emitted blockstate to 36 entries.
        BlockStateVariantMap.DoubleProperty<Direction, BigDoorPart> variants =
                BlockStateVariantMap.create(WCBigDoorBlock.FACING, WCBigDoorBlock.PART);

        for (Direction facing : Direction.Type.HORIZONTAL) {
            for (BigDoorPart part : BigDoorPart.values()) {
                String modelPath = "block/custom/bigdoor/" + blockName + "/" + part.asString();
                Identifier modelId = WesterosBlocks.id(modelPath);
                int rotation = getRotationForDirection(facing);
                variants.register(facing, part, createVariant(modelId, rotation));
            }
        }

        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block).coordinate(variants));

        Identifier itemModelId = WesterosBlocks.id("block/custom/bigdoor/" + blockName + "/bottom_center");
        registerParentedItemModel(generator, block, itemModelId);
    }

    private static void registerGeneratedBigDoor(BlockStateModelGenerator generator, Block block, String texture) {
        Identifier textureId = createBlockIdentifier(texture);

        // One empty model per part; the animated BE draws the full door.
        for (BigDoorPart part : BigDoorPart.values()) {
            Model model = emptyModel();
            TextureMap textureMap = new TextureMap()
                    .put(TextureKey.ALL, textureId)
                    .put(TextureKey.TEXTURE, textureId)
                    .put(TextureKey.PARTICLE, textureId);
            uploadModel(model, block, part.asString(), textureMap, generator.modelCollector);
        }

        // OPEN is omitted from the variant key: the animated BE draws the door,
        // so the static model is identical for open/closed. Each variant key
        // wildcards OPEN, halving the emitted blockstate to 36 entries.
        BlockStateVariantMap.DoubleProperty<Direction, BigDoorPart> variants =
                BlockStateVariantMap.create(WCBigDoorBlock.FACING, WCBigDoorBlock.PART);

        for (Direction facing : Direction.Type.HORIZONTAL) {
            for (BigDoorPart part : BigDoorPart.values()) {
                Identifier modelId = createNestedModelId(block, part.asString());
                int rotation = getRotationForDirection(facing);
                variants.register(facing, part, createVariant(modelId, rotation));
            }
        }

        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block).coordinate(variants));

        Identifier itemModelId = registerIconModel(generator, block, textureId);
        registerParentedItemModel(generator, block, itemModelId);
    }

    private static Model emptyModel() {
        return new Model(
                Optional.of(WesterosBlocks.id("block/bigdoor/open_center")),
                Optional.empty(),
                TextureKey.ALL,
                TextureKey.TEXTURE,
                TextureKey.PARTICLE
        );
    }

    private static Identifier registerIconModel(BlockStateModelGenerator generator, Block block, Identifier textureId) {
        Model iconModel = new Model(
                Optional.of(WesterosBlocks.id("block/bigdoor/closed_center")),
                Optional.empty(),
                TextureKey.ALL,
                TextureKey.TEXTURE,
                TextureKey.PARTICLE
        );
        TextureMap textureMap = new TextureMap()
                .put(TextureKey.ALL, textureId)
                .put(TextureKey.TEXTURE, textureId)
                .put(TextureKey.PARTICLE, textureId);
        return uploadModel(iconModel, block, "icon", textureMap, generator.modelCollector);
    }
}
