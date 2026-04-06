package com.westerosblocks.block.custom;

import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.utils.ModProperties;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Direction;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;

import com.westerosblocks.data.BlockDefinition.CuboidElement.CuboidRotation;

import java.util.List;

public class WCCuboidNSEWBlock extends WCCuboidBlock {
    public static final EnumProperty<Direction> FACING = HorizontalFacingBlock.FACING;

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(BlockDefinition definition) {
            AbstractBlock.Settings settings = definition.makeSettings();
            ModProperties.StateProperty stateProperty = definition.buildStateProperty();
            boolean doToggleOnUse = definition.toggleOnUse();

            tempSTATE = stateProperty;

            return new WCCuboidNSEWBlock(settings, definition, doToggleOnUse);
        }
    }

    public WCCuboidNSEWBlock(AbstractBlock.Settings settings, BlockDefinition def, boolean doToggleOnUse) {
        super(settings, def, 4, doToggleOnUse);  // modelsPerState = 4 (one per facing)

        // Rotate cuboids for each facing direction
        int stcnt = def.getStateCount();
        for (int stidx = 0; stidx < stcnt; stidx++) {
            int off = stidx * this.modelsPerState;

            // Rotate base cuboids to create 4 facing variants
            List<BlockDefinition.CuboidElement> baseCuboids = cuboid_by_facing[off];
            if (baseCuboids != null && !baseCuboids.isEmpty()) {
                for (BlockDefinition.CuboidElement cuboid : baseCuboids) {
                    cuboid_by_facing[off + 1].add(cuboid.rotateCuboid(CuboidRotation.ROTY90));   // SOUTH
                    cuboid_by_facing[off + 2].add(cuboid.rotateCuboid(CuboidRotation.ROTY180));  // WEST
                    cuboid_by_facing[off + 3].add(cuboid.rotateCuboid(CuboidRotation.ROTY270));  // NORTH
                }
            }
        }

        // Compute shapes from rotated cuboids
        for (int i = 0; i < cuboid_by_facing.length; i++) {
            if (SHAPE_BY_INDEX[i] == null) {
                SHAPE_BY_INDEX[i] = computeShapeFromCuboids(cuboid_by_facing[i]);
            }
        }

        // Set default state with facing
        BlockState defbs = this.getDefaultState()
            .with(FACING, Direction.EAST)
            .with(WATERLOGGED, false);

        if (STATE != null) {
            defbs = defbs.with(STATE, STATE.defValue);
        }
        this.setDefaultState(defbs);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
        Direction[] placementDirections = ctx.getPlacementDirections();
        Direction facing = Direction.EAST;

        for (Direction direction : placementDirections) {
            if (direction == Direction.EAST || direction == Direction.WEST ||
                direction == Direction.NORTH || direction == Direction.SOUTH) {
                facing = direction.getOpposite();
                break;
            }
        }

        BlockState state = this.getDefaultState()
            .with(FACING, facing)
            .with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);

        if (STATE != null) {
            state = state.with(STATE, STATE.defValue);
        }

        return state;
    }

    @Override
    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    /**
     * Override to add facing offset to state index.
     * Index = (stateIdx × modelsPerState) + facingOffset
     */
    @Override
    protected int getIndexFromState(BlockState state) {
        int off = super.getIndexFromState(state);  // stateIdx × modelsPerState
        return switch (state.get(FACING)) {
            case EAST  -> off;      // +0
            case SOUTH -> off + 1;  // +1
            case WEST  -> off + 2;  // +2
            case NORTH -> off + 3;  // +3
            default    -> off;
        };
    }
}
