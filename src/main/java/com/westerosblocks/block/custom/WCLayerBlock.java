package com.westerosblocks.block.custom;

import com.westerosblocks.block.ModBlocks;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.WesterosBlockFactory;
import com.westerosblocks.block.WesterosBlockLifecycle;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

public class WCLayerBlock extends Block implements WesterosBlockLifecycle {
	public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
	public static final IntProperty LAYERS = Properties.LAYERS;
	public final int layerCount = 8;
	public boolean softLayer = false;

	public static final VoxelShape[] SHAPE_BY_LAYER = new VoxelShape[]{VoxelShapes.empty(),
			VoxelShapes.cuboid(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),
			VoxelShapes.cuboid(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D),
			VoxelShapes.cuboid(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D),
			VoxelShapes.cuboid(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D),
			VoxelShapes.cuboid(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D),
			VoxelShapes.cuboid(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D),
			VoxelShapes.cuboid(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D),
			VoxelShapes.cuboid(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)};

	private WesterosBlockDef def;

	public static class Factory extends WesterosBlockFactory {
		@Override
		public Block buildBlockClass(WesterosBlockDef def) {
			AbstractBlock.Settings settings = def.makeBlockSettings();
			settings = settings.blockVision((state, level, pos) -> state.get(LAYERS) >= 8);
			Block blk = new WCLayerBlock(settings, def);

			return def.registerRenderType(ModBlocks.registerBlock(def.blockName, blk), false, false);
		}
	}

	protected WCLayerBlock(AbstractBlock.Settings settings, WesterosBlockDef def) {
		super(settings);
		this.def = def;
		String t = def.getType();
		if ((t != null) && (t.contains("softLayer"))) {
			softLayer = true;
		}
		this.setDefaultState(this.getDefaultState()
				.with(LAYERS, 1)
				.with(WATERLOGGED, Boolean.FALSE));
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE_BY_LAYER[state.get(LAYERS)];
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE_BY_LAYER[state.get(LAYERS) - 1];
	}

	@Override
	public VoxelShape getSidesShape(BlockState state, BlockView world, BlockPos pos) {
		return SHAPE_BY_LAYER[state.get(LAYERS)];
	}

	@Override
	public VoxelShape getCameraCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE_BY_LAYER[state.get(LAYERS)];
	}

	@Override
	public WesterosBlockDef getWBDefinition() {
		return def;
	}

	@Override
	protected boolean canReplace(BlockState state, ItemPlacementContext context) {
		int i = state.get(LAYERS);
		if (context.getStack().getItem() == this.asItem() && i < layerCount) {
			if (context.canReplaceExisting()) {
				return context.getSide() == Direction.UP;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		BlockPos blockpos = ctx.getBlockPos();
		BlockState blockstate = ctx.getWorld().getBlockState(blockpos);
		FluidState fluidstate = ctx.getWorld().getFluidState(blockpos);

		if (blockstate.isOf(this)) {
			int i = blockstate.get(LAYERS);
			int newCount = Math.min(layerCount, i + 1);
			return blockstate
					.with(LAYERS, newCount).
					with(WATERLOGGED, (newCount < layerCount) && fluidstate.isIn(FluidTags.WATER));
		}
		else {
			return this.getDefaultState().with(WATERLOGGED, fluidstate.isIn(FluidTags.WATER));
		}
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState,
												WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (state.get(WATERLOGGED)) {
			world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}
		return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
	}

	@Override
	protected boolean canPathfindThrough(BlockState state, NavigationType type) {
        return switch (type) {
            case LAND -> state.get(LAYERS) <= (layerCount / 2);
            case WATER -> state.getFluidState().isIn(FluidTags.WATER);
            case AIR -> false;
            default -> false;
        };
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(LAYERS, WATERLOGGED);
	}

	private static String[] TAGS = {};

	@Override
	public String[] getBlockTags() {
		return TAGS;
	}

}
