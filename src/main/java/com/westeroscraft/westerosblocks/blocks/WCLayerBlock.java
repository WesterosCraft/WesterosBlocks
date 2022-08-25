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

			newLAYERS = IntegerProperty.create("layers", 1, getLayerCount(def));

			return def.registerRenderType(def.registerBlock(new WCLayerBlock(props, def)), false, false);
		}
	}

	// Support waterlogged on these blocks
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	private static int getLayerCount(WesterosBlockDef def) {
		int layerCount = 8;
		int off = def.type.indexOf("cnt:");
		if (off >= 0) {
			try {
				layerCount = Integer.parseInt(def.type.substring(off + 4));
			} catch (NumberFormatException nfx) {
				WesterosBlocks.log.info("Error parsing 'cnt:' in " + def.blockName);
			}
		}
		if (layerCount < 2)
			layerCount = 2;
		if (layerCount > 16)
			layerCount = 16;
		return layerCount;
	}

	protected VoxelShape[] SHAPE_BY_LAYER;

	private WesterosBlockDef def;
	public int layerCount;
	public IntegerProperty LAYERS;
	public static IntegerProperty newLAYERS;

	protected WCLayerBlock(BlockBehaviour.Properties props, WesterosBlockDef def) {
		super(props);
		this.def = def;
		this.layerCount = getLayerCount(def);
		this.registerDefaultState(this.stateDefinition.any().setValue(LAYERS, Integer.valueOf(1)).setValue(WATERLOGGED,
				Boolean.valueOf(false)));
		SHAPE_BY_LAYER = new VoxelShape[layerCount + 1];
		SHAPE_BY_LAYER[0] = Shapes.empty();
		for (int i = 1; i <= layerCount; i++) {
			float f = (float) i / (float) layerCount;
			SHAPE_BY_LAYER[i] = Block.box(0, 0, 0, 16, 16 * f, 16);
		}
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos,
			CollisionContext ctx) {
		return SHAPE_BY_LAYER[state.getValue(LAYERS)];
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state_, BlockGetter world, BlockPos pos,
			CollisionContext ctx) {
		return SHAPE_BY_LAYER[state_.getValue(LAYERS) - 1];
	}

	@Override
	public VoxelShape getBlockSupportShape(BlockState state, BlockGetter world, BlockPos pos) {
		return SHAPE_BY_LAYER[state.getValue(LAYERS)];
	}

	@Override
	public VoxelShape getVisualShape(BlockState state, BlockGetter world, BlockPos pos,
			CollisionContext ctx) {
		return SHAPE_BY_LAYER[state.getValue(LAYERS)];
	}

	@Override
	public boolean useShapeForLightOcclusion(BlockState state) {
		return (state.getValue(LAYERS) < layerCount);
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
			return i == 1;
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
		if (newLAYERS != null) {
			this.LAYERS = newLAYERS;
			newLAYERS = null;
		}
		StateDefinition.add(LAYERS, WATERLOGGED);
	}

	private static String[] TAGS = {};

	@Override
	public String[] getBlockTags() {
		return TAGS;
	}

}
