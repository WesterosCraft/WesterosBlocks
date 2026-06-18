package com.westerosblocks.datagen.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.custom.WCBigNarrowDoorBlock;
import com.westerosblocks.block.custom.WCBigNarrowDoorBlock.NarrowDoorPart;
import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.Block;
import net.minecraft.block.enums.DoorHinge;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.Optional;

/**
 * Exporter for the big narrow door (2x3 vanilla-style multiblock). No block
 * entity — the door is blockstate models that snap between closed and open,
 * following vanilla DoorBlock's exact face rules:
 * <ul>
 *   <li>Closed: a 3px slab on each cell's {@code facing.getOpposite()} face.</li>
 *   <li>Open: the hinge column's cell carries a slab on the open face
 *       (LEFT -> facing.rotateYCounterclockwise, RIGHT -> facing.rotateYClockwise);
 *       the other column is empty.</li>
 * </ul>
 * Because the faces are world directions, each cell just picks the matching
 * world-faced slab model with NO y-rotation — and the block's collision uses the
 * same face, so model and collision always agree.
 */
public class BigNarrowDoorBlockExporter extends BaseBlockExporter {

    private static final String EMPTY = "empty";

    public static void registerCustomBigNarrowDoorBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        String[] textures = definition.getTexturesAsArray();
        validateTexturePaths(textures, 1);

        String texture = textures[0];
        Identifier textureId = createBlockIdentifier(texture);
        TextureMap textureMap = new TextureMap()
                .put(TextureKey.ALL, textureId)
                .put(TextureKey.PARTICLE, textureId);

        // Per-block child models (parent = hand-authored canonical geometry,
        // binding #all -> this block's texture). Models are authored for FACING
        // = NORTH and oriented per-facing with getRotationForDirection. Each
        // cell carries its own 2x3 UV slice so the texture maps as one coherent
        // door instead of repeating per cell.
        for (String variant : new String[]{
                "closed_c0_r0", "closed_c0_r1", "closed_c0_r2",
                "closed_c1_r0", "closed_c1_r1", "closed_c1_r2",
                "open_r0_left", "open_r1_left", "open_r2_left",
                "open_r0_right", "open_r1_right", "open_r2_right",
                EMPTY}) {
            uploadModel(geometryModel(variant), block, variant, textureMap, generator.modelCollector);
        }

        // Full key: FACING x HINGE x OPEN x PART = 4 x 2 x 2 x 6 = 96 variants.
        BlockStateVariantMap.QuadrupleProperty<Direction, DoorHinge, Boolean, NarrowDoorPart> variants =
                BlockStateVariantMap.create(
                        WCBigNarrowDoorBlock.FACING, WCBigNarrowDoorBlock.HINGE,
                        WCBigNarrowDoorBlock.OPEN, WCBigNarrowDoorBlock.PART);

        for (Direction facing : Direction.Type.HORIZONTAL) {
            int rotation = getRotationForDirection(facing);
            for (DoorHinge hinge : DoorHinge.values()) {
                int hingeColumn = hinge == DoorHinge.LEFT ? 0 : 1;
                for (boolean open : new boolean[]{false, true}) {
                    for (NarrowDoorPart part : NarrowDoorPart.values()) {
                        if (!open) {
                            // Closed: this cell's 2x3 slice, oriented by facing.
                            String variant = "closed_c" + part.getColumn() + "_r" + part.getRow();
                            variants.register(facing, hinge, open, part,
                                    createVariant(createNestedModelId(block, variant), rotation));
                        } else if (part.getColumn() == hingeColumn) {
                            // Open: the 2-deep panel on the hinge column, this row.
                            String variant = "open_r" + part.getRow()
                                    + (hinge == DoorHinge.LEFT ? "_left" : "_right");
                            variants.register(facing, hinge, open, part,
                                    createVariant(createNestedModelId(block, variant), rotation));
                        } else {
                            variants.register(facing, hinge, open, part,
                                    createVariant(createNestedModelId(block, EMPTY)));
                        }
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
                Optional.of(WesterosBlocks.id("block/bignarrowdoor/" + variant)),
                Optional.empty(),
                TextureKey.ALL,
                TextureKey.PARTICLE
        );
    }
}
