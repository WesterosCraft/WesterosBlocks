package com.westerosblocks.block.custom;

import com.westerosblocks.block.ModBlocks;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.WesterosBlockFactory;
import com.westerosblocks.block.WesterosBlockLifecycle;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.math.Direction;

import java.util.List;

public class WCCuboidNSEWUDBlock extends WCCuboidBlock implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
            def.nonOpaque = true;
            AbstractBlock.Settings settings = def.makeBlockSettings();
            // See if we have a state property
            WesterosBlockDef.StateProperty state = def.buildStateProperty();
            if (state != null) {
                tempSTATE = state;
            }
            Block blk = new WCCuboidNSEWUDBlock(settings, def);
            return def.registerRenderType(ModBlocks.registerBlock(def.blockName, blk), false, false);
        }
    }

    public static final DirectionProperty FACING = DirectionProperty.of("facing", Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.NORTH, Direction.DOWN, Direction.UP);

    protected WCCuboidNSEWUDBlock(AbstractBlock.Settings settings, WesterosBlockDef def) {
        super(settings, def, 6);

        int stcnt = def.states.size();
        for (int stidx = 0; stidx < stcnt; stidx++) {
            int off = stidx * this.modelsPerState;
            for (WesterosBlockDef.Cuboid c : cuboid_by_facing[off]) {
                cuboid_by_facing[off + 1].add(c.rotateCuboid(WesterosBlockDef.CuboidRotation.ROTY90));
                cuboid_by_facing[off + 2].add(c.rotateCuboid(WesterosBlockDef.CuboidRotation.ROTY180));
                cuboid_by_facing[off + 3].add(c.rotateCuboid(WesterosBlockDef.CuboidRotation.ROTY270));
                cuboid_by_facing[off + 4].add(c.rotateCuboid(WesterosBlockDef.CuboidRotation.ROTZ90));
                cuboid_by_facing[off + 5].add(c.rotateCuboid(WesterosBlockDef.CuboidRotation.ROTZ270));
            }
        }
        for (int i = 0; i < cuboid_by_facing.length; i++) {
            if (SHAPE_BY_INDEX[i] == null) {
                SHAPE_BY_INDEX[i] = getBoundingBoxFromCuboidList(cuboid_by_facing[i]);
            }
        }
        BlockState defbs = this.getDefaultState().with(FACING, Direction.EAST).with(WATERLOGGED, Boolean.FALSE);
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
    public List<WesterosBlockDef.Cuboid> getModelCuboids(int stateIdx) {
        return cuboid_by_facing[modelsPerState * stateIdx + 3];
    }

    @Override
    protected int getIndexFromState(BlockState state) {
        int off = super.getIndexFromState(state);
        return switch (state.get(FACING)) {
            case EAST -> off;
            case SOUTH -> off + 1;
            case WEST -> off + 2;
            case NORTH -> off + 3;
            case DOWN -> off + 4;
            case UP -> off + 5;
        };
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        FluidState fluidstate = ctx.getWorld().getFluidState(ctx.getBlockPos());
        Direction[] adirection = ctx.getPlacementDirections();
        Direction dir = Direction.EAST;    // Default
        for (Direction d : adirection) {
            dir = d;
            break;
        }
        BlockState bs = this.getDefaultState().with(FACING, dir).with(WATERLOGGED, fluidstate.isIn(FluidTags.WATER));
        if (STATE != null) {
            bs = bs.with(STATE, STATE.defValue);
        }
        return bs;
    }
}
