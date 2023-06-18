package com.westeroscraft.westerosblocks.blocks;

import javax.annotation.Nullable;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.tags.FluidTags;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;
import com.westeroscraft.westerosblocks.WesterosBlocks;

public class WCLayerBlock extends Block implements WesterosBlockLifecycle, SimpleWaterloggedBlock {

	public static class Factory extends WesterosBlockFactory {
		@Override
		public Block buildBlockClass(WesterosBlockDef def) {
			BlockBehaviour.Properties props = def.makeProperties();

			return def.registerRenderType(def.registerBlock(new WCLayerBlock(props, def)), false, false);
		}
	}

	// Support waterlogged on these blocks
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final IntegerProperty LAYERS = BlockStateProperties.LAYERS;
	public final int layerCount = 8;
	public boolean softLayer = false;
	
    public static final VoxelShape[] SHAPE_BY_LAYER = new VoxelShape[]{Shapes.empty(), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)};

	private WesterosBlockDef def;

	protected WCLayerBlock(BlockBehaviour.Properties props, WesterosBlockDef def) {
		super(props);
		this.def = def;
		String t = def.getType();
		if ((t != null) && (t.indexOf("softLayer") >= 0)) {
			softLayer = true;
		}
		this.registerDefaultState(this.stateDefinition.any().setValue(LAYERS, Integer.valueOf(1)).setValue(WATERLOGGED,
				Boolean.valueOf(false)));
	}

	public VoxelShape getShape(BlockState p_56620_, BlockGetter p_56621_, BlockPos p_56622_, CollisionContext p_56623_) {
		return SHAPE_BY_LAYER[p_56620_.getValue(LAYERS)];
	}

	public VoxelShape getCollisionShape(BlockState p_56625_, BlockGetter p_56626_, BlockPos p_56627_, CollisionContext p_56628_) {
		return SHAPE_BY_LAYER[p_56625_.getValue(LAYERS) - 1];
	}

	public VoxelShape getBlockSupportShape(BlockState p_56632_, BlockGetter p_56633_, BlockPos p_56634_) {
		return SHAPE_BY_LAYER[p_56632_.getValue(LAYERS)];
	}

	public VoxelShape getVisualShape(BlockState p_56597_, BlockGetter p_56598_, BlockPos p_56599_, CollisionContext p_56600_) {
		return SHAPE_BY_LAYER[p_56597_.getValue(LAYERS)];
	}

	public boolean useShapeForLightOcclusion(BlockState p_56630_) {
		return true;
	}

	@Override
	public WesterosBlockDef getWBDefinition() {
		return def;
	}

	@Override
	public boolean canBeReplaced(BlockState state, BlockPlaceContext itemContext) {
		int i = state.getValue(LAYERS);
		if (itemContext.getItemInHand().getItem() == this.asItem() && i < layerCount) {
			if (itemContext.replacingClickedOnBlock()) {
				return itemContext.getClickedFace() == Direction.UP;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext itemContext) {
		BlockPos blockpos = itemContext.getClickedPos();
		BlockState blockstate = itemContext.getLevel().getBlockState(blockpos);
		FluidState fluidstate = itemContext.getLevel().getFluidState(blockpos);
		if (blockstate.is(this)) {
			int i = blockstate.getValue(LAYERS);
			int newCount = Math.min(layerCount, i + 1);
			return blockstate.setValue(LAYERS, Integer.valueOf(newCount)).
					setValue(WATERLOGGED, Boolean.valueOf((newCount < layerCount) && fluidstate.is(FluidTags.WATER)));
		}
		else {
			return this.defaultBlockState().setValue(WATERLOGGED, Boolean.valueOf(fluidstate.is(FluidTags.WATER)));
		}
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	public boolean placeLiquid(LevelAccessor world, BlockPos pos, BlockState state, FluidState fluid) {
		return (state.getValue(LAYERS) < layerCount) ? SimpleWaterloggedBlock.super.placeLiquid(world, pos, state, fluid)
				: false;
	}

	@Override
	public boolean canPlaceLiquid(BlockGetter world, BlockPos pos, BlockState state, Fluid fluid) {
		return (state.getValue(LAYERS) < layerCount) ? SimpleWaterloggedBlock.super.canPlaceLiquid(world, pos, state, fluid)
				: false;
	}

	@Override
	public BlockState updateShape(BlockState state, Direction dir, BlockState state2, LevelAccessor level, BlockPos pos,
			BlockPos pos2) {
		if (state.getValue(WATERLOGGED)) {
			level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		}
		return super.updateShape(state, dir, state2, level, pos, pos2);
	}

	public boolean isPathfindable(BlockState state, BlockGetter world, BlockPos pos, PathComputationType PathComputationType) {
		switch (PathComputationType) {
		case LAND:
			return state.getValue(LAYERS) <= (layerCount / 2);
		case WATER:
			return world.getFluidState(pos).is(FluidTags.WATER);
		case AIR:
			return false;
		default:
			return false;
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> StateDefinition) {
		StateDefinition.add(LAYERS, WATERLOGGED);
	}

	private static String[] TAGS = {};

	@Override
	public String[] getBlockTags() {
		return TAGS;
	}

}
