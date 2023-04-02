package com.westeroscraft.westerosblocks.blocks;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.tags.FluidTags;
import net.minecraft.core.Direction;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;

public class WCCuboidNSEWBlock extends WCCuboidBlock implements WesterosBlockLifecycle {

	public static class Factory extends WesterosBlockFactory {
		@Override
		public Block buildBlockClass(WesterosBlockDef def) {
			def.nonOpaque = true;
			BlockBehaviour.Properties props = def.makeProperties();
        	// See if we have a state property
        	WesterosBlockDef.StateProperty state = def.buildStateProperty();
        	if (state != null) {
        		tempSTATE = state;
        	}        	
			return def.registerRenderType(def.registerBlock(new WCCuboidNSEWBlock(props, def)), false, false);
		}
	}

	// public static final DirectionProperty FACING =
	// DirectionProperty.create("facing", Direction.EAST, Direction.SOUTH,
	// Direction.WEST, Direction.NORTH);
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

	protected WCCuboidNSEWBlock(BlockBehaviour.Properties props, WesterosBlockDef def) {
		super(props, def, 4);

		int stcnt = def.states.size();
		for (int stidx = 0; stidx < stcnt; stidx++) {
			int off = stidx * this.modelsPerState;
			for (WesterosBlockDef.Cuboid c : cuboid_by_facing[off]) {
				cuboid_by_facing[off + 1].add(c.rotateCuboid(WesterosBlockDef.CuboidRotation.ROTY90));
				cuboid_by_facing[off + 2].add(c.rotateCuboid(WesterosBlockDef.CuboidRotation.ROTY180));
				cuboid_by_facing[off + 3].add(c.rotateCuboid(WesterosBlockDef.CuboidRotation.ROTY270));
			}
		}
		for (int i = 0; i < cuboid_by_facing.length; i++) {
			if (SHAPE_BY_INDEX[i] == null) {
				SHAPE_BY_INDEX[i] = getBoundingBoxFromCuboidList(cuboid_by_facing[i]);
			}
		}
		if (STATE != null) {
			this.registerDefaultState(
					this.stateDefinition.any().setValue(FACING, Direction.EAST).setValue(STATE, STATE.defValue));
		} else {
			this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.EAST));
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> StateDefinition) {
		super.createBlockStateDefinition(StateDefinition);
		StateDefinition.add(FACING);
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mir) {
		return state.rotate(mir.getRotation(state.getValue(FACING)));
	}

	@Override
	protected int getIndexFromState(BlockState state) {
		int off = super.getIndexFromState(state);
		switch (state.getValue(FACING)) {
		case EAST:
		default:
			return off;
		case SOUTH:
			return off + 1;
		case WEST:
			return off + 2;
		case NORTH:
			return off + 3;
		}
	}

	@Override
	@Nullable
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		FluidState fluidstate = ctx.getLevel().getFluidState(ctx.getClickedPos());
		Direction[] adirection = ctx.getNearestLookingDirections();
		Direction dir = Direction.EAST; // Default
		for (Direction d : adirection) {
			if (d == Direction.EAST || d == Direction.WEST || d == Direction.NORTH || d == Direction.SOUTH) {
				dir = d;
				break;
			}
		}
		BlockState bs = this.defaultBlockState().setValue(FACING, dir).setValue(WATERLOGGED,
				Boolean.valueOf(fluidstate.is(FluidTags.WATER)));
		if (STATE != null) {
			bs = bs.setValue(STATE, STATE.defValue);
		}
		return bs;
	}
}
