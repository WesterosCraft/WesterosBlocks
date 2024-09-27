package com.westeroscraft.westerosblocks.blocks;

import net.minecraft.world.level.block.WallBlock;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.WallSide;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;

public class WCWallBlock extends Block implements SimpleWaterloggedBlock, WesterosBlockLifecycle {

	public static class Factory extends WesterosBlockFactory {
		@Override
		public Block buildBlockClass(WesterosBlockDef def) {
			BlockBehaviour.Properties props = def.makeProperties();
			// See if we have a state property
			WesterosBlockDef.StateProperty state = def.buildStateProperty();
			if (state != null) {
				tempSTATE = state;
			}
			// Process types
			String t = def.getType();
			boolean doUnconnect = false;
			boolean doConnectstate = false;
			if (t != null) {
				String[] toks = t.split(",");
				for (String tok : toks) {
					String[] parts = tok.split(":");
					// See if we have unconnect
					if (parts[0].equals("unconnect")) {
						doUnconnect = true;
						tempUNCONNECT = UNCONNECT;
					}
					// See if we have connectstate
					if (parts[0].equals("connectstate")) {
						doConnectstate = true;
						tempCONNECTSTATE = CONNECTSTATE;
					}
				}
			}
			return def.registerRenderType(def.registerBlock(new WCWallBlock(props, def, doUnconnect, doConnectstate)), false, false);
		}
	}

	public static final BooleanProperty UP = BlockStateProperties.UP;
	public static final EnumProperty<WallSide> EAST_WALL = BlockStateProperties.EAST_WALL;
	public static final EnumProperty<WallSide> NORTH_WALL = BlockStateProperties.NORTH_WALL;
	public static final EnumProperty<WallSide> SOUTH_WALL = BlockStateProperties.SOUTH_WALL;
	public static final EnumProperty<WallSide> WEST_WALL = BlockStateProperties.WEST_WALL;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	private static final VoxelShape POST_TEST = Block.box(7.0D, 0.0D, 7.0D, 9.0D, 16.0D, 9.0D);
	private static final VoxelShape NORTH_TEST = Block.box(7.0D, 0.0D, 0.0D, 9.0D, 16.0D, 9.0D);
	private static final VoxelShape SOUTH_TEST = Block.box(7.0D, 0.0D, 7.0D, 9.0D, 16.0D, 16.0D);
	private static final VoxelShape WEST_TEST = Block.box(0.0D, 0.0D, 7.0D, 9.0D, 16.0D, 9.0D);
	private static final VoxelShape EAST_TEST = Block.box(7.0D, 0.0D, 7.0D, 16.0D, 16.0D, 9.0D);

	private WesterosBlockDef def;
	public static final BooleanProperty UNCONNECT = BooleanProperty.create("unconnect");
	protected static BooleanProperty tempUNCONNECT;
	public final boolean unconnect;

	public static final IntegerProperty CONNECTSTATE = IntegerProperty.create("connectstate", 0, 3);
	protected static IntegerProperty tempCONNECTSTATE;
  public final boolean connectstate;

	protected static WesterosBlockDef.StateProperty tempSTATE;
	protected WesterosBlockDef.StateProperty STATE;

	protected boolean toggleOnUse = false;

	public final VoxelShape[] ourShapeByIndex;
	public final VoxelShape[] ourCollisionShapeByIndex;

	private static VoxelShape[] ourCollisionShapeByIndexShared = null;
	private static VoxelShape[] ourShapeByIndexSharedShort = null;
	private static VoxelShape[] ourShapeByIndexSharedNormal = null;

	public static enum WallSize {
		NORMAL, // 16/16 high wall
		SHORT // 13/16 high wall
	};

	public final WallSize wallSize; // "normal", or "short"

	protected WCWallBlock(BlockBehaviour.Properties props, WesterosBlockDef def, boolean doUnconnect, boolean doConnectstate) {
		super(props);
		this.def = def;

		String t = def.getType();
		if (t != null) {
				String[] toks = t.split(",");
				for (String tok : toks) {
						if (tok.equals("toggleOnUse")) {
								toggleOnUse = true;
						}
				}
		}

		String height = def.getTypeValue("size", "normal");
		float wheight;
		if (height.equals("short")) {
			wallSize = WallSize.SHORT;
			wheight = 13;
		} else {
			wallSize = WallSize.NORMAL;
			wheight = 16;
		}
		unconnect = doUnconnect;
		connectstate = doConnectstate;
		BlockState defbs = this.stateDefinition.any()
												.setValue(UP, Boolean.valueOf(true))
												.setValue(NORTH_WALL, WallSide.NONE)
												.setValue(EAST_WALL, WallSide.NONE)
												.setValue(SOUTH_WALL, WallSide.NONE)
												.setValue(WEST_WALL, WallSide.NONE)
												.setValue(WATERLOGGED, Boolean.valueOf(false));
		if (unconnect) {
			defbs = defbs.setValue(UNCONNECT, Boolean.valueOf(false));
		}
		if (connectstate) {
			defbs = defbs.setValue(CONNECTSTATE, 0);
		}
		if (STATE != null) {
			defbs = defbs.setValue(STATE, STATE.defValue);
		}
		this.registerDefaultState(defbs);
		
		if (ourCollisionShapeByIndexShared == null) {
			ourCollisionShapeByIndexShared = makeShapes(4.0F, 3.0F, 24.0F, 0.0F, 24.0F, 24.0F);
		}
		this.ourCollisionShapeByIndex = ourCollisionShapeByIndexShared;
		if (height.equals("short")) {
			if (ourShapeByIndexSharedShort == null) {
				ourShapeByIndexSharedShort = makeShapes(4.0F, 3.0F, 16.0F, 0.0F, wheight, 16.0F);
			}
			this.ourShapeByIndex = ourShapeByIndexSharedShort;
		} else {
			if (ourShapeByIndexSharedNormal == null) {
				ourShapeByIndexSharedNormal = makeShapes(4.0F, 3.0F, 16.0F, 0.0F, wheight, 16.0F);
			}
			this.ourShapeByIndex = ourShapeByIndexSharedNormal;
		}
	}

	protected WCWallBlock(BlockBehaviour.Properties props, WesterosBlockDef def, boolean doUnconnect) {
		this(props, def, doUnconnect, false);
	}

	protected WCWallBlock(BlockBehaviour.Properties props, WesterosBlockDef def) {
		this(props, def, false, false);
	}

	@Override
	public WesterosBlockDef getWBDefinition() {
		return def;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter p_58051_, BlockPos p_58052_, CollisionContext p_58053_) {
		return this.ourShapeByIndex[getStateIndex(state)];
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter p_58056_, BlockPos p_58057_,
			CollisionContext p_58058_) {
		return this.ourCollisionShapeByIndex[getStateIndex(state)];
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> sd) {
		if (tempUNCONNECT != null) {
			sd.add(tempUNCONNECT);
			tempUNCONNECT = null;
		}
		if (tempCONNECTSTATE != null) {
			sd.add(tempCONNECTSTATE);
			tempCONNECTSTATE = null;
		}
		if (tempSTATE != null) {
			STATE = tempSTATE;
			tempSTATE = null;
		}
		if (STATE != null) {
			sd.add(STATE);
		}
		sd.add(UP, NORTH_WALL, EAST_WALL, WEST_WALL, SOUTH_WALL, WATERLOGGED);
	}

	private static VoxelShape applyWallShape(VoxelShape p_58034_, WallSide p_58035_, VoxelShape p_58036_,
			VoxelShape p_58037_) {
		if (p_58035_ == WallSide.TALL) {
			return Shapes.or(p_58034_, p_58037_);
		} else {
			return p_58035_ == WallSide.LOW ? Shapes.or(p_58034_, p_58036_) : p_58034_;
		}
	}

	private static int getStateIndex(BlockState bs) {
		return getStateIndex(bs.getValue(UP).booleanValue(), bs.getValue(EAST_WALL).ordinal(),
				bs.getValue(WEST_WALL).ordinal(), bs.getValue(NORTH_WALL).ordinal(), bs.getValue(SOUTH_WALL).ordinal());
	}

	private static int getStateIndex(boolean up, int east, int west, int north, int south) {
		return (up ? 1 : 0) + (east * 2) + (west * 6) + (north * 18) + (south * 54);
	}

	private static VoxelShape[] makeShapes(float p_57966_, float p_57967_, float p_57968_, float p_57969_,
			float p_57970_, float p_57971_) {
		float f = 8.0F - p_57966_;
		float f1 = 8.0F + p_57966_;
		float f2 = 8.0F - p_57967_;
		float f3 = 8.0F + p_57967_;
		VoxelShape voxelshape = Block.box((double) f, 0.0D, (double) f, (double) f1, (double) p_57968_, (double) f1);
		VoxelShape voxelshape1 = Block.box((double) f2, (double) p_57969_, 0.0D, (double) f3, (double) p_57970_,
				(double) f3);
		VoxelShape voxelshape2 = Block.box((double) f2, (double) p_57969_, (double) f2, (double) f3, (double) p_57970_,
				16.0D);
		VoxelShape voxelshape3 = Block.box(0.0D, (double) p_57969_, (double) f2, (double) f3, (double) p_57970_,
				(double) f3);
		VoxelShape voxelshape4 = Block.box((double) f2, (double) p_57969_, (double) f2, 16.0D, (double) p_57970_,
				(double) f3);
		VoxelShape voxelshape5 = Block.box((double) f2, (double) p_57969_, 0.0D, (double) f3, (double) p_57971_,
				(double) f3);
		VoxelShape voxelshape6 = Block.box((double) f2, (double) p_57969_, (double) f2, (double) f3, (double) p_57971_,
				16.0D);
		VoxelShape voxelshape7 = Block.box(0.0D, (double) p_57969_, (double) f2, (double) f3, (double) p_57971_,
				(double) f3);
		VoxelShape voxelshape8 = Block.box((double) f2, (double) p_57969_, (double) f2, 16.0D, (double) p_57971_,
				(double) f3);
		VoxelShape[] map = new VoxelShape[2 * 3 * 3 * 3 * 3];

		for (Boolean up : UP.getPossibleValues()) {
			for (WallSide east : EAST_WALL.getPossibleValues()) {
				for (WallSide north : NORTH_WALL.getPossibleValues()) {
					for (WallSide west : WEST_WALL.getPossibleValues()) {
						for (WallSide south : SOUTH_WALL.getPossibleValues()) {
							VoxelShape shape = Shapes.empty();
							shape = applyWallShape(shape, east, voxelshape4, voxelshape8);
							shape = applyWallShape(shape, west, voxelshape3, voxelshape7);
							shape = applyWallShape(shape, north, voxelshape1, voxelshape5);
							shape = applyWallShape(shape, south, voxelshape2, voxelshape6);
							if (up) {
								shape = Shapes.or(shape, voxelshape);
							}
							map[getStateIndex(up.booleanValue(), east.ordinal(), west.ordinal(),
									north.ordinal(), south.ordinal())] = shape;
						}
					}
				}
			}
		}
		return map;
	}

	@Override
	protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
		return false;
	}

	private boolean connectsTo(BlockState p_58021_, boolean p_58022_, Direction p_58023_) {
		Block block = p_58021_.getBlock();
		boolean flag = block instanceof FenceGateBlock && FenceGateBlock.connectsToDirection(p_58021_, p_58023_);
		return p_58021_.is(BlockTags.WALLS) || !isExceptionForConnection(p_58021_) && p_58022_
				|| block instanceof IronBarsBlock || flag;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext p_57973_) {
		LevelReader levelreader = p_57973_.getLevel();
		BlockPos blockpos = p_57973_.getClickedPos();
		FluidState fluidstate = p_57973_.getLevel().getFluidState(p_57973_.getClickedPos());
		BlockPos blockpos1 = blockpos.north();
		BlockPos blockpos2 = blockpos.east();
		BlockPos blockpos3 = blockpos.south();
		BlockPos blockpos4 = blockpos.west();
		BlockPos blockpos5 = blockpos.above();
		BlockState blockstate = levelreader.getBlockState(blockpos1);
		BlockState blockstate1 = levelreader.getBlockState(blockpos2);
		BlockState blockstate2 = levelreader.getBlockState(blockpos3);
		BlockState blockstate3 = levelreader.getBlockState(blockpos4);
		BlockState blockstate4 = levelreader.getBlockState(blockpos5);
		boolean flag = this.connectsTo(blockstate, blockstate.isFaceSturdy(levelreader, blockpos1, Direction.SOUTH),
				Direction.SOUTH);
		boolean flag1 = this.connectsTo(blockstate1, blockstate1.isFaceSturdy(levelreader, blockpos2, Direction.WEST),
				Direction.WEST);
		boolean flag2 = this.connectsTo(blockstate2, blockstate2.isFaceSturdy(levelreader, blockpos3, Direction.NORTH),
				Direction.NORTH);
		boolean flag3 = this.connectsTo(blockstate3, blockstate3.isFaceSturdy(levelreader, blockpos4, Direction.EAST),
				Direction.EAST);
		BlockState blockstate5 = this.defaultBlockState().setValue(WATERLOGGED,
				Boolean.valueOf(fluidstate.getType() == Fluids.WATER));
		return this.updateShape(levelreader, blockstate5, blockpos5, blockstate4, flag, flag1, flag2, flag3);
	}

	@Override
	public BlockState updateShape(BlockState state, Direction dir, BlockState state2, LevelAccessor world, BlockPos pos,
			BlockPos pos2) {
		if (state.getValue(WATERLOGGED)) {
			world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
		}
		if (unconnect && state.getValue(UNCONNECT)) {
			return state;
		}
		if (dir == Direction.DOWN) {
			return super.updateShape(state, dir, state2, world, pos, pos2);
		} else {
			return dir == Direction.UP ? this.topUpdate(world, state, pos2, state2)
					: this.sideUpdate(world, pos, state, pos2, state2, dir);
		}
	}

	private static boolean isConnected(BlockState p_58011_, Property<WallSide> p_58012_) {
		return p_58011_.getValue(p_58012_) != WallSide.NONE;
	}

	private static boolean isCovered(VoxelShape p_58039_, VoxelShape p_58040_) {
		return !Shapes.joinIsNotEmpty(p_58040_, p_58039_, BooleanOp.ONLY_FIRST);
	}

	private BlockState topUpdate(LevelReader p_57975_, BlockState p_57976_, BlockPos p_57977_, BlockState p_57978_) {
		boolean flag = isConnected(p_57976_, NORTH_WALL);
		boolean flag1 = isConnected(p_57976_, EAST_WALL);
		boolean flag2 = isConnected(p_57976_, SOUTH_WALL);
		boolean flag3 = isConnected(p_57976_, WEST_WALL);
		return this.updateShape(p_57975_, p_57976_, p_57977_, p_57978_, flag, flag1, flag2, flag3);
	}

	private BlockState sideUpdate(LevelReader p_57989_, BlockPos p_57990_, BlockState p_57991_, BlockPos p_57992_,
			BlockState p_57993_, Direction p_57994_) {
		Direction direction = p_57994_.getOpposite();
		boolean flag = p_57994_ == Direction.NORTH
				? this.connectsTo(p_57993_, p_57993_.isFaceSturdy(p_57989_, p_57992_, direction), direction)
				: isConnected(p_57991_, NORTH_WALL);
		boolean flag1 = p_57994_ == Direction.EAST
				? this.connectsTo(p_57993_, p_57993_.isFaceSturdy(p_57989_, p_57992_, direction), direction)
				: isConnected(p_57991_, EAST_WALL);
		boolean flag2 = p_57994_ == Direction.SOUTH
				? this.connectsTo(p_57993_, p_57993_.isFaceSturdy(p_57989_, p_57992_, direction), direction)
				: isConnected(p_57991_, SOUTH_WALL);
		boolean flag3 = p_57994_ == Direction.WEST
				? this.connectsTo(p_57993_, p_57993_.isFaceSturdy(p_57989_, p_57992_, direction), direction)
				: isConnected(p_57991_, WEST_WALL);
		BlockPos blockpos = p_57990_.above();
		BlockState blockstate = p_57989_.getBlockState(blockpos);
		return this.updateShape(p_57989_, p_57991_, blockpos, blockstate, flag, flag1, flag2, flag3);
	}

	private BlockState updateShape(LevelReader p_57980_, BlockState p_57981_, BlockPos p_57982_, BlockState p_57983_,
			boolean p_57984_, boolean p_57985_, boolean p_57986_, boolean p_57987_) {
		VoxelShape voxelshape = p_57983_.getCollisionShape(p_57980_, p_57982_).getFaceShape(Direction.DOWN);
		BlockState blockstate = this.updateSides(p_57981_, p_57984_, p_57985_, p_57986_, p_57987_, voxelshape);
		return blockstate.setValue(UP, Boolean.valueOf(this.shouldRaisePost(blockstate, p_57983_, voxelshape)));
	}

	private boolean shouldRaisePost(BlockState p_58007_, BlockState p_58008_, VoxelShape p_58009_) {
		boolean flag = p_58008_.getBlock() instanceof WallBlock && p_58008_.getValue(UP);
		if (flag) {
			return true;
		} else {
			WallSide wallside = p_58007_.getValue(NORTH_WALL);
			WallSide wallside1 = p_58007_.getValue(SOUTH_WALL);
			WallSide wallside2 = p_58007_.getValue(EAST_WALL);
			WallSide wallside3 = p_58007_.getValue(WEST_WALL);
			boolean flag1 = wallside1 == WallSide.NONE;
			boolean flag2 = wallside3 == WallSide.NONE;
			boolean flag3 = wallside2 == WallSide.NONE;
			boolean flag4 = wallside == WallSide.NONE;
			boolean flag5 = flag4 && flag1 && flag2 && flag3 || flag4 != flag1 || flag2 != flag3;
			if (flag5) {
				return true;
			} else {
				boolean flag6 = wallside == WallSide.TALL && wallside1 == WallSide.TALL
						|| wallside2 == WallSide.TALL && wallside3 == WallSide.TALL;
				if (flag6) {
					return false;
				} else {
					return p_58008_.is(BlockTags.WALL_POST_OVERRIDE) || isCovered(p_58009_, POST_TEST);
				}
			}
		}
	}

	private BlockState updateSides(BlockState p_58025_, boolean p_58026_, boolean p_58027_, boolean p_58028_,
			boolean p_58029_, VoxelShape p_58030_) {
		return p_58025_.setValue(NORTH_WALL, this.makeWallState(p_58026_, p_58030_, NORTH_TEST))
				.setValue(EAST_WALL, this.makeWallState(p_58027_, p_58030_, EAST_TEST))
				.setValue(SOUTH_WALL, this.makeWallState(p_58028_, p_58030_, SOUTH_TEST))
				.setValue(WEST_WALL, this.makeWallState(p_58029_, p_58030_, WEST_TEST));
	}

	private WallSide makeWallState(boolean p_58042_, VoxelShape p_58043_, VoxelShape p_58044_) {
		if (p_58042_) {
			return isCovered(p_58043_, p_58044_) ? WallSide.TALL : WallSide.LOW;
		} else {
			return WallSide.NONE;
		}
	}

	@Override
	public FluidState getFluidState(BlockState p_58060_) {
		return p_58060_.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(p_58060_);
	}

	@Override
	public boolean propagatesSkylightDown(BlockState p_58046_, BlockGetter p_58047_, BlockPos p_58048_) {
		return !p_58046_.getValue(WATERLOGGED);
	}

	@Override
	public BlockState rotate(BlockState p_58004_, Rotation p_58005_) {
		switch (p_58005_) {
		case CLOCKWISE_180:
			return p_58004_.setValue(NORTH_WALL, p_58004_.getValue(SOUTH_WALL))
					.setValue(EAST_WALL, p_58004_.getValue(WEST_WALL))
					.setValue(SOUTH_WALL, p_58004_.getValue(NORTH_WALL))
					.setValue(WEST_WALL, p_58004_.getValue(EAST_WALL));
		case COUNTERCLOCKWISE_90:
			return p_58004_.setValue(NORTH_WALL, p_58004_.getValue(EAST_WALL))
					.setValue(EAST_WALL, p_58004_.getValue(SOUTH_WALL))
					.setValue(SOUTH_WALL, p_58004_.getValue(WEST_WALL))
					.setValue(WEST_WALL, p_58004_.getValue(NORTH_WALL));
		case CLOCKWISE_90:
			return p_58004_.setValue(NORTH_WALL, p_58004_.getValue(WEST_WALL))
					.setValue(EAST_WALL, p_58004_.getValue(NORTH_WALL))
					.setValue(SOUTH_WALL, p_58004_.getValue(EAST_WALL))
					.setValue(WEST_WALL, p_58004_.getValue(SOUTH_WALL));
		default:
			return p_58004_;
		}
	}

	@Override
	public BlockState mirror(BlockState p_58001_, Mirror p_58002_) {
		switch (p_58002_) {
		case LEFT_RIGHT:
			return p_58001_.setValue(NORTH_WALL, p_58001_.getValue(SOUTH_WALL)).setValue(SOUTH_WALL,
					p_58001_.getValue(NORTH_WALL));
		case FRONT_BACK:
			return p_58001_.setValue(EAST_WALL, p_58001_.getValue(WEST_WALL)).setValue(WEST_WALL,
					p_58001_.getValue(EAST_WALL));
		default:
			return super.mirror(p_58001_, p_58002_);
		}
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		if (this.toggleOnUse && (this.STATE != null) && player.isCreative() && player.getMainHandItem().isEmpty()) {
				state = state.cycle(this.STATE);
				level.setBlock(pos, state, 10);
				level.levelEvent(player, 1006, pos, 0);
				return InteractionResult.sidedSuccess(level.isClientSide);
		}
		else {
			return InteractionResult.PASS;
		}
	}

	private static String[] TAGS = { "walls" };
	@Override
	public String[] getBlockTags() {
		return TAGS;
	}

}
