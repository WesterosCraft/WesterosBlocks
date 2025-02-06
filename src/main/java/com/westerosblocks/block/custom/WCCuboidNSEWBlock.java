package com.westerosblocks.block.custom;

import com.westerosblocks.block.ModBlocks;
import com.westerosblocks.block.ModBlock;
import com.westerosblocks.block.ModBlockFactory;
import com.westerosblocks.block.ModBlockLifecycle;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.text.Text;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.Direction;

import java.util.List;

public class WCCuboidNSEWBlock extends WCCuboidBlock implements ModBlockLifecycle {

	public static class Factory extends ModBlockFactory {
		@Override
		public Block buildBlockClass(ModBlock def) {
			def.nonOpaque = true;
			AbstractBlock.Settings settings = def.makeBlockSettings();
        	// See if we have a state property
        	ModBlock.StateProperty state = def.buildStateProperty();
        	if (state != null) {
        		tempSTATE = state;
        	}
			Block blk = new WCCuboidNSEWBlock(settings, def);
			return def.registerRenderType(ModBlocks.registerBlock(def.blockName, blk), false, false);
		}
	}

	public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;

	protected WCCuboidNSEWBlock(AbstractBlock.Settings settings, ModBlock def) {
		super(settings, def, 4);

		int stcnt = def.states.size();
		for (int stidx = 0; stidx < stcnt; stidx++) {
			int off = stidx * this.modelsPerState;
			for (ModBlock.Cuboid c : cuboid_by_facing[off]) {
				cuboid_by_facing[off + 1].add(c.rotateCuboid(ModBlock.CuboidRotation.ROTY90));
				cuboid_by_facing[off + 2].add(c.rotateCuboid(ModBlock.CuboidRotation.ROTY180));
				cuboid_by_facing[off + 3].add(c.rotateCuboid(ModBlock.CuboidRotation.ROTY270));
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
	protected BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(FACING, rotation.rotate(state.get(FACING)));
	}

	@Override
	protected BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.rotate(mirror.getRotation(state.get(FACING)));
	}

	@Override
	protected int getIndexFromState(BlockState state) {
		int off = super.getIndexFromState(state);
        return switch (state.get(FACING)) {
            default -> off;
            case SOUTH -> off + 1;
            case WEST -> off + 2;
            case NORTH -> off + 3;
        };
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		FluidState fluidstate = ctx.getWorld().getFluidState(ctx.getBlockPos());
		Direction[] adirection = ctx.getPlacementDirections();
		Direction dir = Direction.EAST;
		for (Direction d : adirection) {
			if (d == Direction.EAST || d == Direction.WEST || d == Direction.NORTH || d == Direction.SOUTH) {
				dir = d.getOpposite();
				break;
			}
		}
		BlockState bs = this.getDefaultState().with(FACING, dir).with(WATERLOGGED, fluidstate.isIn(FluidTags.WATER));
		if (STATE != null) {
			bs = bs.with(STATE, STATE.defValue);
		}
		return bs;
	}

	@Override
	public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
		addCustomTooltip(tooltip);
		super.appendTooltip(stack, context, tooltip, options);
	}
}
