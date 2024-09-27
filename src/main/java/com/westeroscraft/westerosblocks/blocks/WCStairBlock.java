package com.westeroscraft.westerosblocks.blocks;

import java.util.stream.IntStream;

import com.westeroscraft.westerosblocks.*;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WCStairBlock extends Block implements WesterosBlockLifecycle {

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
            return def.registerRenderType(def.registerBlock(new WCStairBlock(props, def, doUnconnect, doConnectstate)), false, false);
        }
    }

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;
    public static final EnumProperty<StairsShape> SHAPE = BlockStateProperties.STAIRS_SHAPE;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    protected static final VoxelShape BOTTOM_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);
    protected static final VoxelShape TOP_AABB = Block.box(0.0, 8.0, 0.0, 16.0, 16.0, 16.0);
    protected static final VoxelShape OCTET_NNN = Block.box(0.0, 0.0, 0.0, 8.0, 8.0, 8.0);
    protected static final VoxelShape OCTET_NNP = Block.box(0.0, 0.0, 8.0, 8.0, 8.0, 16.0);
    protected static final VoxelShape OCTET_NPN = Block.box(0.0, 8.0, 0.0, 8.0, 16.0, 8.0);
    protected static final VoxelShape OCTET_NPP = Block.box(0.0, 8.0, 8.0, 8.0, 16.0, 16.0);
    protected static final VoxelShape OCTET_PNN = Block.box(8.0, 0.0, 0.0, 16.0, 8.0, 8.0);
    protected static final VoxelShape OCTET_PNP = Block.box(8.0, 0.0, 8.0, 16.0, 8.0, 16.0);
    protected static final VoxelShape OCTET_PPN = Block.box(8.0, 8.0, 0.0, 16.0, 16.0, 8.0);
    protected static final VoxelShape OCTET_PPP = Block.box(8.0, 8.0, 8.0, 16.0, 16.0, 16.0);

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

    public final boolean no_uvlock;

    protected static final VoxelShape[] TOP_SHAPES = makeShapes(TOP_AABB, OCTET_NNN, OCTET_PNN, OCTET_NNP, OCTET_PNP);
    protected static final VoxelShape[] BOTTOM_SHAPES = makeShapes(BOTTOM_AABB, OCTET_NPN, OCTET_PPN, OCTET_NPP, OCTET_PPP);
    private static final int[] SHAPE_BY_STATE = new int[]{12, 5, 3, 10, 14, 13, 7, 11, 13, 7, 11, 14, 8, 4, 1, 2, 4, 1, 2, 8};

    protected WCStairBlock(BlockBehaviour.Properties props, WesterosBlockDef def, boolean doUnconnect, boolean doConnectstate) {
        super(props);
        this.def = def;

        String t = def.getType();
        boolean no_uvlock = false;
        if (t != null) {
            String[] toks = t.split(",");
            for (String tok : toks) {
                if (tok.equals("no-uvlock")) {
                	no_uvlock = true;
                }
                if (tok.equals("toggleOnUse")) {
                    toggleOnUse = true;
                }
            }
        }

        this.no_uvlock = no_uvlock;
        this.unconnect = doUnconnect;
        this.connectstate = doConnectstate;
		BlockState defbs = this.stateDefinition.any()
                            .setValue(FACING, Direction.NORTH)
                            .setValue(HALF, Half.BOTTOM)
				            .setValue(SHAPE, StairsShape.STRAIGHT)
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
    }

    protected WCStairBlock(BlockBehaviour.Properties props, WesterosBlockDef def, boolean doUnconnect) {
        this(props, def, doUnconnect, false);
    }

    protected WCStairBlock(BlockBehaviour.Properties props, WesterosBlockDef def) {
        this(props, def, false, false);
    }

    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState blockState) {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return (blockState.getValue(HALF) == Half.TOP ? TOP_SHAPES : BOTTOM_SHAPES)[SHAPE_BY_STATE[this.getShapeIndex(blockState)]];
    }

    private int getShapeIndex(BlockState blockState) {
        return blockState.getValue(SHAPE).ordinal() * 4 + blockState.getValue(FACING).get2DDataValue();
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
        sd.add(FACING, HALF, SHAPE, WATERLOGGED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        Direction direction = blockPlaceContext.getClickedFace();
        BlockPos blockPos = blockPlaceContext.getClickedPos();
        FluidState fluidState = blockPlaceContext.getLevel().getFluidState(blockPos);
        BlockState blockState = (BlockState)((BlockState)((BlockState)this.defaultBlockState().setValue(FACING, blockPlaceContext.getHorizontalDirection())).setValue(HALF, direction == Direction.DOWN || direction != Direction.UP && blockPlaceContext.getClickLocation().y - (double)blockPos.getY() > 0.5 ? Half.TOP : Half.BOTTOM)).setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
        return (BlockState)blockState.setValue(SHAPE, getStairsShape(blockState, blockPlaceContext.getLevel(), blockPos));
    }

    private static VoxelShape[] makeShapes(VoxelShape voxelShape, VoxelShape voxelShape2, VoxelShape voxelShape3, VoxelShape voxelShape4, VoxelShape voxelShape5) {
        return (VoxelShape[])IntStream.range(0, 16).mapToObj(n -> makeStairShape(n, voxelShape, voxelShape2, voxelShape3, voxelShape4, voxelShape5)).toArray(VoxelShape[]::new);
    }

    private static VoxelShape makeStairShape(int n, VoxelShape voxelShape, VoxelShape voxelShape2, VoxelShape voxelShape3, VoxelShape voxelShape4, VoxelShape voxelShape5) {
        VoxelShape voxelShape6 = voxelShape;
        if ((n & 1) != 0) {
            voxelShape6 = Shapes.or(voxelShape6, voxelShape2);
        }
        if ((n & 2) != 0) {
            voxelShape6 = Shapes.or(voxelShape6, voxelShape3);
        }
        if ((n & 4) != 0) {
            voxelShape6 = Shapes.or(voxelShape6, voxelShape4);
        }
        if ((n & 8) != 0) {
            voxelShape6 = Shapes.or(voxelShape6, voxelShape5);
        }
        return voxelShape6;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState state2, LevelAccessor world, BlockPos pos, BlockPos pos2) {
        if (unconnect && state.getValue(UNCONNECT)) {
            if (state.getValue(WATERLOGGED).booleanValue()) {
                world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
            }
            return state;
        }
        if (state.getValue(WATERLOGGED).booleanValue()) {
            world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        }
        if (direction.getAxis().isHorizontal()) {
            return (BlockState)state.setValue(SHAPE, getStairsShape(state, world, pos));
        }
        return super.updateShape(state, direction, state2, world, pos, pos2);
    }

    private static StairsShape getStairsShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        Direction direction;
        Object object;
        Direction direction2 = blockState.getValue(FACING);
        BlockState blockState2 = blockGetter.getBlockState(blockPos.relative(direction2));
        if (isStairs(blockState2) && blockState.getValue(HALF) == blockState2.getValue(HALF) && ((Direction)(object = blockState2.getValue(FACING))).getAxis() != blockState.getValue(FACING).getAxis() && canTakeShape(blockState, blockGetter, blockPos, ((Direction)object).getOpposite())) {
            if (object == direction2.getCounterClockWise()) {
                return StairsShape.OUTER_LEFT;
            }
            return StairsShape.OUTER_RIGHT;
        }
        object = blockGetter.getBlockState(blockPos.relative(direction2.getOpposite()));
        if (isStairs((BlockState)object) && blockState.getValue(HALF) == ((StateHolder)object).getValue(HALF) && (direction = ((Direction)((StateHolder)object).getValue(FACING))).getAxis() != blockState.getValue(FACING).getAxis() && canTakeShape(blockState, blockGetter, blockPos, direction)) {
            if (direction == direction2.getCounterClockWise()) {
                return StairsShape.INNER_LEFT;
            }
            return StairsShape.INNER_RIGHT;
        }
        return StairsShape.STRAIGHT;
    }

    private static boolean canTakeShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, Direction direction) {
        BlockState blockState2 = blockGetter.getBlockState(blockPos.relative(direction));
        return !isStairs(blockState2) || blockState2.getValue(FACING) != blockState.getValue(FACING) || blockState2.getValue(HALF) != blockState.getValue(HALF);
    }

    public static boolean isStairs(BlockState blockState) {
        return blockState.getBlock() instanceof WCStairBlock;
    }

    @Override
    public FluidState getFluidState(BlockState blockState) {
        if (blockState.getValue(WATERLOGGED).booleanValue()) {
            return Fluids.WATER.getSource(false);
        }
        return super.getFluidState(blockState);
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return false;
    }

    @Override
    public BlockState rotate(BlockState blockState, Rotation rotation) {
        return (BlockState)blockState.setValue(FACING, rotation.rotate(blockState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState blockState, Mirror mirror) {
        Direction direction = blockState.getValue(FACING);
        StairsShape stairsShape = blockState.getValue(SHAPE);
        switch (mirror) {
            case LEFT_RIGHT: {
                if (direction.getAxis() != Direction.Axis.Z) break;
                switch (stairsShape) {
                    case INNER_LEFT: {
                        return (BlockState)blockState.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.INNER_RIGHT);
                    }
                    case INNER_RIGHT: {
                        return (BlockState)blockState.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.INNER_LEFT);
                    }
                    case OUTER_LEFT: {
                        return (BlockState)blockState.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.OUTER_RIGHT);
                    }
                    case OUTER_RIGHT: {
                        return (BlockState)blockState.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.OUTER_LEFT);
                    }
                }
                return blockState.rotate(Rotation.CLOCKWISE_180);
            }
            case FRONT_BACK: {
                if (direction.getAxis() != Direction.Axis.X) break;
                switch (stairsShape) {
                    case INNER_LEFT: {
                        return (BlockState)blockState.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.INNER_LEFT);
                    }
                    case INNER_RIGHT: {
                        return (BlockState)blockState.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.INNER_RIGHT);
                    }
                    case OUTER_LEFT: {
                        return (BlockState)blockState.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.OUTER_RIGHT);
                    }
                    case OUTER_RIGHT: {
                        return (BlockState)blockState.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.OUTER_LEFT);
                    }
                    case STRAIGHT: {
                        return blockState.rotate(Rotation.CLOCKWISE_180);
                    }
                }
                break;
            }
        }
        return super.mirror(blockState, mirror);
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

    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        if (blockState.is(blockState.getBlock())) {
            return;
        }
        super.onPlace(blockState, level, blockPos, blockState2, false);
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        if (blockState.is(blockState2.getBlock())) {
            return;
        }
        super.onRemove(blockState, level, blockPos, blockState2, bl);
    }

    private static String[] TAGS = { "stairs" };
    @Override
    public String[] getBlockTags() {
        return TAGS;
    }

}
