package com.westeroscraft.westerosblocks.blocks;

import javax.annotation.Nullable;

import org.dynmap.modsupport.BoxBlockModel;
import org.dynmap.modsupport.ModModelDefinition;
import org.dynmap.modsupport.ModTextureDefinition;
import org.dynmap.modsupport.TransparencyMode;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockDynmapSupport;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;
import com.westeroscraft.westerosblocks.WesterosBlocks;

public class WCLayerBlock extends Block implements WesterosBlockLifecycle, WesterosBlockDynmapSupport, IWaterLoggable {

	public static class Factory extends WesterosBlockFactory {
		@Override
		public Block buildBlockClass(WesterosBlockDef def) {
			AbstractBlock.Properties props = def.makeProperties();

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

	protected WCLayerBlock(AbstractBlock.Properties props, WesterosBlockDef def) {
		super(props);
		this.def = def;
		this.layerCount = getLayerCount(def);
		this.registerDefaultState(this.stateDefinition.any().setValue(LAYERS, Integer.valueOf(1)).setValue(WATERLOGGED,
				Boolean.valueOf(false)));
		SHAPE_BY_LAYER = new VoxelShape[layerCount + 1];
		SHAPE_BY_LAYER[0] = VoxelShapes.empty();
		for (int i = 1; i <= layerCount; i++) {
			float f = (float) i / (float) layerCount;
			SHAPE_BY_LAYER[i] = Block.box(0, 0, 0, 16, 16 * f, 16);
		}
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos,
			ISelectionContext ctx) {
		return SHAPE_BY_LAYER[state.getValue(LAYERS)];
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state_, IBlockReader world, BlockPos pos,
			ISelectionContext ctx) {
		return SHAPE_BY_LAYER[state_.getValue(LAYERS) - 1];
	}

	@Override
	public VoxelShape getBlockSupportShape(BlockState state, IBlockReader world, BlockPos pos) {
		return SHAPE_BY_LAYER[state.getValue(LAYERS)];
	}

	@Override
	public VoxelShape getVisualShape(BlockState state, IBlockReader world, BlockPos pos,
			ISelectionContext ctx) {
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
	public boolean canBeReplaced(BlockState state, BlockItemUseContext itemContext) {
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
	public BlockState getStateForPlacement(BlockItemUseContext itemContext) {
		BlockPos blockpos = itemContext.getClickedPos();
		BlockState blockstate = itemContext.getLevel().getBlockState(blockpos);
		if (blockstate.is(this)) {
			int i = blockstate.getValue(LAYERS);
			FluidState fluidstate = itemContext.getLevel().getFluidState(blockpos);
			int newCount = Math.min(layerCount, i + 1);
			return blockstate.setValue(LAYERS, Integer.valueOf(newCount)).setValue(WATERLOGGED,
					Boolean.valueOf((newCount < layerCount) && (fluidstate.getType() == Fluids.WATER)));
		} else {
			return super.getStateForPlacement(itemContext);
		}
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	public boolean placeLiquid(IWorld world, BlockPos pos, BlockState state, FluidState fluid) {
		return (state.getValue(LAYERS) < layerCount) ? IWaterLoggable.super.placeLiquid(world, pos, state, fluid)
				: false;
	}

	@Override
	public boolean canPlaceLiquid(IBlockReader world, BlockPos pos, BlockState state, Fluid fluid) {
		return (state.getValue(LAYERS) < layerCount) ? IWaterLoggable.super.canPlaceLiquid(world, pos, state, fluid)
				: false;
	}

	@Override
	public BlockState updateShape(BlockState state, Direction dir, BlockState state2, IWorld world, BlockPos pos,
			BlockPos pos2) {
		if (state.getValue(WATERLOGGED)) {
			world.getLiquidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
		}
		return super.updateShape(state, dir, state2, world, pos, pos2);
	}

	public boolean isPathfindable(BlockState state, IBlockReader world, BlockPos pos, PathType pathtype) {
		switch (pathtype) {
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
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> stateContainer) {
		if (newLAYERS != null) {
			this.LAYERS = newLAYERS;
			newLAYERS = null;
		}
		stateContainer.add(LAYERS, WATERLOGGED);
	}

	@Override
	public void registerDynmapRenderData(ModTextureDefinition mtd) {
		ModModelDefinition md = mtd.getModelDefinition();
		String blkname = def.getBlockName();
		def.defaultRegisterTextures(mtd);
		def.defaultRegisterTextureBlock(mtd, TransparencyMode.TRANSPARENT, 0, layerCount);
		/* Make models for each layer thickness */
		for (int i = 0; i < layerCount; i++) {
			BoxBlockModel mod = md.addBoxModel(blkname);
			mod.setYRange(0.0, (double) (i + 1) / (double) layerCount);
			mod.setMetaValue(i);
		}
	}

	private static String[] TAGS = {};

	@Override
	public String[] getBlockTags() {
		return TAGS;
	}

}
