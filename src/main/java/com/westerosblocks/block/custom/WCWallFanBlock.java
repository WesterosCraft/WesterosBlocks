package com.westerosblocks.block.custom;

import com.google.common.collect.Maps;
import com.sun.jdi.Mirror;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.WesterosBlockLifecycle;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;

import java.util.Map;

public class WCWallFanBlock extends Block implements SimpleWaterloggedBlock, WesterosBlockLifecycle {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private WesterosBlockDef def;
    private boolean allow_unsupported = false;

    private static final Map<Direction, VoxelShape> SHAPES = Maps.newEnumMap((Map)ImmutableMap.of((Object) Direction.NORTH, (Object)Block.box(0.0, 4.0, 5.0, 16.0, 12.0, 16.0), (Object)Direction.SOUTH, (Object)Block.box(0.0, 4.0, 0.0, 16.0, 12.0, 11.0), (Object)Direction.WEST, (Object)Block.box(5.0, 4.0, 0.0, 16.0, 12.0, 16.0), (Object)Direction.EAST, (Object)Block.box(0.0, 4.0, 0.0, 11.0, 12.0, 16.0)));
    
    protected WCWallFanBlock(AbstractBlock.Settings settings, WesterosBlockDef def) {
        super(settings);
        this.def = def;
        String t = def.getType();
        if (t != null) {
            String[] toks = t.split(",");
            for (String tok : toks) {
                if (tok.equals("allow-unsupported")) {
                    allow_unsupported = true;
                }
            }
        }
        BlockState defbs = this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, Boolean.valueOf(false));
        this.registerDefaultState(defbs);
    }

    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        // Get waterlogging state
        BlockState bs = super.getStateForPlacement(ctx);
        if (bs == null) return null;
        FluidState fluidstate = ctx.getLevel().getFluidState(ctx.getClickedPos());
        bs = bs.setValue(WATERLOGGED, Boolean.valueOf(fluidstate.is(FluidTags.WATER)));
        // Get direction state
        Direction[] directionArray;
        Level level = ctx.getLevel();
        BlockPos blockPos = ctx.getClickedPos();
        for (Direction direction : directionArray = ctx.getNearestLookingDirections()) {
            if (!direction.getAxis().isHorizontal() || !(bs = (BlockState)bs.setValue(FACING, direction.getOpposite())).canSurvive(level, blockPos)) continue;
            return bs;
        }
        return null;
    }

    @SuppressWarnings("deprecation")
	  @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        switch(pathComputationType) {
        case LAND:
           return false;
        case WATER:
           return state.getFluidState().is(FluidTags.WATER);
        case AIR:
           return false;
        default:
           return false;
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinition) {
    	stateDefinition.add(FACING, WATERLOGGED);
    }

    @Override
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
        if (direction.getOpposite() == blockState.getValue(FACING) && !blockState.canSurvive(levelAccessor, blockPos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return blockState;
    }

    @Override
    public boolean canSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
        if (this.allow_unsupported) return true;
        Direction direction = blockState.getValue(FACING);
        BlockPos blockPos2 = blockPos.relative(direction.getOpposite());
        BlockState blockState2 = levelReader.getBlockState(blockPos2);
        return blockState2.isFaceSturdy(levelReader, blockPos2, direction);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
      return state.getFluidState().isEmpty();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
      return SHAPES.get(state.getValue(FACING));
    }

    @Override
    public BlockState rotate(BlockState blockState, Rotation rotation) {
        return (BlockState)blockState.setValue(FACING, rotation.rotate(blockState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.rotate(mirror.getRotation(blockState.getValue(FACING)));
    }
    
    private static String[] TAGS = { "fans" };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }
    
}
