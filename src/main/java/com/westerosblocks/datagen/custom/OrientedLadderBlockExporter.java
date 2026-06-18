package com.westerosblocks.datagen.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.custom.WCOrientedLadderBlock;
import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.Block;
import net.minecraft.block.enums.BlockFace;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.BlockStateVariantMap;
import net.minecraft.data.client.VariantsBlockStateSupplier;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

/**
 * Exporter for {@code oriented_ladder} blocks. These always use hand-authored custom
 * models (one per FACE × ROTATED combination); FACING is applied as a Y rotation.
 *
 * Expected model files under {@code block/custom/<blockName>/}:
 * <ul>
 *   <li>{@code floor_v1}          — FLOOR, upright</li>
 *   <li>{@code floor_rotated_v1}  — FLOOR, lying flat</li>
 *   <li>{@code wall_v1}           — WALL, normal</li>
 *   <li>{@code wall_rotated_v1}   — WALL, rotated 90°</li>
 * </ul>
 * Models are authored facing NORTH (0°), matching {@link #getRotationForDirection}.
 */
public class OrientedLadderBlockExporter extends BaseBlockExporter {

    private static final Direction[] HORIZONTAL = {
            Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST
    };

    private static String variantName(BlockFace face, boolean rotated) {
        String base = face == BlockFace.FLOOR ? "floor" : "wall";
        return rotated ? base + "_rotated" : base;
    }

    private static Identifier modelId(Block block, BlockFace face, boolean rotated) {
        return WesterosBlocks.id("block/custom/" + getBlockName(block) + "/" + variantName(face, rotated) + "_v1");
    }

    public static void registerCustomOrientedLadderBlock(BlockStateModelGenerator generator, Block block,
                                                         BlockDefinition definition) {
        BlockStateVariantMap.TripleProperty<BlockFace, Direction, Boolean> variantMap =
                BlockStateVariantMap.create(WCOrientedLadderBlock.FACE, Properties.HORIZONTAL_FACING,
                        WCOrientedLadderBlock.ROTATED);

        for (BlockFace face : new BlockFace[]{BlockFace.FLOOR, BlockFace.WALL}) {
            for (Direction dir : HORIZONTAL) {
                int yRotation = getRotationForDirection(dir);
                for (boolean rotated : new boolean[]{false, true}) {
                    variantMap.register(face, dir, rotated,
                            createVariant(modelId(block, face, rotated), yRotation));
                }
            }
        }

        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block).coordinate(variantMap));
        registerParentedItemModel(generator, block, modelId(block, BlockFace.WALL, false));
    }
}
