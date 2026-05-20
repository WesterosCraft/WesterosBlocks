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
 * resolve to an empty model regardless of OPEN. The inventory uses a flat 2D
 * sprite via the standard {@code minecraft:item/generated} parent.
 */
public class BigDoorBlockExporter extends BaseBlockExporter {

    public static void registerCustomBigDoorBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        String[] textures = definition.getTexturesAsArray();
        validateTexturePaths(textures, 1);

        if (definition.hasCustomModel()) {
            registerCustomModelBigDoor(generator, block, definition);
        } else {
            registerGeneratedBigDoor(generator, block, definition);
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

        registerSimpleItemModel(generator, block, resolveItemTextureId(block, definition, definition.getTexturesAsArray()[0]));
    }

    private static void registerGeneratedBigDoor(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        String texture = definition.getTexturesAsArray()[0];
        Identifier textureId = createBlockIdentifier(texture);

        // One empty model per part; the animated BE draws the full door.
        // `#all` drives any faces in parent models; `#particle` is declared
        // explicitly so the break-particle resolves even if a future parent
        // drops its "particle": "#all" alias.
        TextureMap textureMap = new TextureMap()
                .put(TextureKey.ALL, textureId)
                .put(TextureKey.PARTICLE, textureId);
        for (BigDoorPart part : BigDoorPart.values()) {
            uploadModel(emptyModel(), block, part.asString(), textureMap, generator.modelCollector);
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

        registerSimpleItemModel(generator, block, resolveItemTextureId(block, definition, texture));
    }

    /**
     * Three-tier fallback chain matching {@code DoorBlockExporter}: explicit
     * {@code customItemTexture} → explicit {@code itemTexture} override → block's
     * primary texture. Drives the flat {@code item/generated} layer0 sprite.
     */
    private static Identifier resolveItemTextureId(Block block, BlockDefinition definition, String fallbackBlockTexture) {
        if (definition.hasCustomItemTexture()) {
            return WesterosBlocks.id("item/" + getBlockName(block));
        }
        if (definition.hasItemTexture()) {
            return createBlockIdentifier(definition.getItemTexture());
        }
        return createBlockIdentifier(fallbackBlockTexture);
    }

    private static Model emptyModel() {
        return new Model(
                Optional.of(WesterosBlocks.id("block/bigdoor/open_center")),
                Optional.empty(),
                TextureKey.ALL,
                TextureKey.PARTICLE
        );
    }
}
