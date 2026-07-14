package com.westerosblocks.datagen.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.custom.WCTallDoorBlock;
import com.westerosblocks.block.custom.WCTallDoorBlock.TallDoorPart;
import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.Block;
import net.minecraft.block.enums.DoorHinge;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.Optional;

/**
 * Exporter for the tall door (1x3 vanilla-style multiblock). No block entity —
 * the door is blockstate models that snap between closed and open, following
 * vanilla DoorBlock's exact face rules:
 * <ul>
 *   <li>Closed: a 3px slab on each cell's {@code facing.getOpposite()} face.</li>
 *   <li>Open: a 3px slab on each cell's open face
 *       (LEFT -> facing.rotateYCounterclockwise, RIGHT -> facing.rotateYClockwise).</li>
 * </ul>
 * Models are authored for FACING = NORTH and oriented per-facing with
 * getRotationForDirection — and the block's collision uses the same face, so
 * model and collision always agree.
 */
public class TallDoorBlockExporter extends BaseBlockExporter {

    public static void registerCustomTallDoorBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        String[] textures = definition.getTexturesAsArray();
        validateTexturePaths(textures, 1);

        String texture = textures[0];
        Identifier textureId = createBlockIdentifier(texture);
        TextureMap textureMap = new TextureMap()
                .put(TextureKey.ALL, textureId)
                .put(TextureKey.PARTICLE, textureId);

        // Per-block child models (parent = hand-authored canonical geometry,
        // binding #all -> this block's texture). Each row carries its own 1x3
        // UV slice so the texture maps as one coherent door instead of
        // repeating per cell.
        for (String variant : new String[]{
                "closed_r0", "closed_r1", "closed_r2",
                "open_r0_left", "open_r1_left", "open_r2_left",
                "open_r0_right", "open_r1_right", "open_r2_right"}) {
            uploadModel(geometryModel(variant), block, variant, textureMap, generator.modelCollector);
        }

        // Full key: FACING x HINGE x OPEN x PART = 4 x 2 x 2 x 3 = 48 variants.
        BlockStateVariantMap.QuadrupleProperty<Direction, DoorHinge, Boolean, TallDoorPart> variants =
                BlockStateVariantMap.create(
                        WCTallDoorBlock.FACING, WCTallDoorBlock.HINGE,
                        WCTallDoorBlock.OPEN, WCTallDoorBlock.PART);

        for (Direction facing : Direction.Type.HORIZONTAL) {
            int rotation = getRotationForDirection(facing);
            for (DoorHinge hinge : DoorHinge.values()) {
                for (boolean open : new boolean[]{false, true}) {
                    for (TallDoorPart part : TallDoorPart.values()) {
                        String variant = open
                                ? "open_r" + part.getRow() + (hinge == DoorHinge.LEFT ? "_left" : "_right")
                                : "closed_r" + part.getRow();
                        variants.register(facing, hinge, open, part,
                                createVariant(createNestedModelId(block, variant), rotation));
                    }
                }
            }
        }

        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block).coordinate(variants));

        registerSimpleItemModel(generator, block, resolveItemTextureId(block, definition, texture));
    }

    private static Identifier resolveItemTextureId(Block block, BlockDefinition definition, String fallbackBlockTexture) {
        if (definition.hasCustomItemTexture()) {
            return WesterosBlocks.id("item/" + getBlockName(block));
        }
        if (definition.hasItemTexture()) {
            return createBlockIdentifier(definition.getItemTexture());
        }
        return createBlockIdentifier(fallbackBlockTexture);
    }

    /** Child model parented to the hand-authored geometry, binding #all to the block texture. */
    private static Model geometryModel(String variant) {
        return new Model(
                Optional.of(WesterosBlocks.id("block/talldoor/" + variant)),
                Optional.empty(),
                TextureKey.ALL,
                TextureKey.PARTICLE
        );
    }
}
