package com.westerosblocks.datagen.custom;

import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.utils.ModProperties;
import net.minecraft.block.Block;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.List;

/**
 * Datagen for {@link com.westerosblocks.block.custom.WCBeamBlock}. Each definition state is a
 * distinct shape model (straight / corner / T / ...) the player cycles through; FACING supplies the
 * Y rotation and HALF picks the bottom/top model. All models are custom (hand-authored) under
 * {@code block/custom/<blockName>/}, named {@code bottom_<stateID>} / {@code top_<stateID>}.
 * Generic over the number of states.
 */
public class BeamBlockExporter extends BaseBlockExporter {

    private static final Direction[] DIRECTIONS = {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};

    public static void registerBeamBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        ModProperties.StateProperty stateProp = getStateProperty(block);
        List<BlockDefinition.StateVariant> states = definition.getStates();
        boolean multi = stateProp != null && states != null && states.size() > 1;

        // FACING -> Y rotation only (model comes from the HALF×STATE coordinate)
        BlockStateVariantMap.SingleProperty<Direction> facingMap =
                BlockStateVariantMap.create(Properties.HORIZONTAL_FACING);
        for (Direction dir : DIRECTIONS) {
            facingMap.register(dir, rotationOnlyVariant(getRotationForDirection(dir)));
        }

        Identifier firstBody;
        VariantsBlockStateSupplier supplier;

        if (multi) {
            // (HALF, STATE) -> model
            BlockStateVariantMap.DoubleProperty<BlockHalf, String> modelMap =
                    BlockStateVariantMap.create(Properties.BLOCK_HALF, stateProp);
            for (BlockDefinition.StateVariant state : states) {
                String id = state.getStateID();
                modelMap.register(BlockHalf.BOTTOM, id, modelVariant(block, "bottom_" + id));
                modelMap.register(BlockHalf.TOP, id, modelVariant(block, "top_" + id));
            }
            supplier = VariantsBlockStateSupplier.create(block).coordinate(facingMap).coordinate(modelMap);
            firstBody = createCustomModelId(block, "bottom_" + states.get(0).getStateID());
        } else {
            // No STATE: HALF -> model
            BlockStateVariantMap.SingleProperty<BlockHalf> modelMap =
                    BlockStateVariantMap.create(Properties.BLOCK_HALF);
            modelMap.register(BlockHalf.BOTTOM, modelVariant(block, "bottom"));
            modelMap.register(BlockHalf.TOP, modelVariant(block, "top"));
            supplier = VariantsBlockStateSupplier.create(block).coordinate(facingMap).coordinate(modelMap);
            firstBody = createCustomModelId(block, "bottom");
        }

        generator.blockStateCollector.accept(supplier);
        registerParentedItemModel(generator, block, firstBody);
    }

    private static BlockStateVariant modelVariant(Block block, String variantName) {
        return BlockStateVariant.create().put(VariantSettings.MODEL, createCustomModelId(block, variantName));
    }
}
