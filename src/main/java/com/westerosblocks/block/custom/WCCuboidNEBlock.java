package com.westerosblocks.block.custom;

import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.WesterosBlockFactory;
import com.westerosblocks.block.WesterosBlockLifecycle;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.data.client.VariantSettings.Rotation;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.math.Direction;

public class WCCuboidNEBlock extends WCCuboidBlock implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
            def.nonOpaque = true;
            AbstractBlock.Settings settings = def.makeProperties();
            // See if we have a state property
            WesterosBlockDef.StateProperty state = def.buildStateProperty();
            if (state != null) {
                tempSTATE = state;
            }
            Block blk = new WCCuboidNEBlock(settings, def);
            return def.registerRenderType(blk, false, false);
        }
    }

    public static final DirectionProperty FACING = DirectionProperty.of("facing", Direction.EAST, Direction.NORTH);

    protected WCCuboidNEBlock(AbstractBlock.Settings settings, WesterosBlockDef def) {
        super(settings, def, 2);

        int stcnt = def.states.size();
        for (int stidx = 0; stidx < stcnt; stidx++) {
            for (WesterosBlockDef.Cuboid c : cuboid_by_facing[2 * stidx]) {
                cuboid_by_facing[2 * stidx + 1].add(c.rotateCuboid(WesterosBlockDef.CuboidRotation.ROTY90));
            }
        }
        for (int i = 0; i < cuboid_by_facing.length; i++) {
            if (SHAPE_BY_INDEX[i] == null) {
                SHAPE_BY_INDEX[i] = getBoundingBoxFromCuboidList(cuboid_by_facing[i]);
            }
        }
        BlockState defbs = getDefaultState().with(FACING, Direction.EAST).with(WATERLOGGED, Boolean.valueOf(false));
        if (STATE != null) {
            defbs = defbs.with(STATE, STATE.defValue);
        }
        setDefaultState(defbs);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING);
    }

    @Override
    protected int getIndexFromState(BlockState state) {
        return super.getIndexFromState(state) + ((state.get(FACING) == Direction.EAST) ? 0 : 1);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        switch (rot) {
            case CLOCKWISE_180:
            default:
                return state;
            case COUNTERCLOCKWISE_90:
            case CLOCKWISE_90:
                return (state.get(FACING) == Direction.EAST) ?
                        state.with(FACING, Direction.NORTH) :
                        state.with(FACING, Direction.EAST);
        }
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        FluidState fluidstate = ctx.getWorld().getFluidState(ctx.getBlockPos());
        Direction[] adirection = ctx.getPlacementDirections();
        Direction dir = Direction.EAST;    // Default
        for (Direction d : adirection) {
            if (d == Direction.EAST || d == Direction.WEST) {
                dir = Direction.EAST;
                break;
            }
            if (d == Direction.NORTH || d == Direction.SOUTH) {
                dir = Direction.NORTH;
                break;
            }
        }
        BlockState bs = getDefaultState().with(FACING, dir).with(WATERLOGGED, Boolean.valueOf(fluidstate.is(FluidTags.WATER)));
        if (STATE != null) {
            bs = bs.with(STATE, STATE.defValue);
        }
        return bs;
    }
}
