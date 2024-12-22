package com.westerosblocks.block.custom;

import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.WesterosBlockFactory;
import com.westerosblocks.block.WesterosBlockLifecycle;
import net.minecraft.block.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

import java.util.Map;
import java.util.stream.Collectors;

public class WCVinesBlock extends VineBlock implements WesterosBlockLifecycle {
    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
            AbstractBlock.Settings settings = def.makeProperties().nonOpaque();
            Block blk = new WCVinesBlock(settings, def);
            return def.registerRenderType(blk, false, false);
        }
    }

    private WesterosBlockDef def;
    private boolean allow_unsupported = false;
    private boolean no_climb = false;
    public boolean has_down = false;
    public static final BooleanProperty DOWN = PipeBlock.DOWN;
    private static final VoxelShape UP_AABB = VoxelShapes.cuboid(0.0D, 15.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private static final VoxelShape DOWN_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);
    private static final VoxelShape WEST_AABB = Block.box(0.0D, 0.0D, 0.0D, 1.0D, 16.0D, 16.0D);
    private static final VoxelShape EAST_AABB = Block.box(15.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private static final VoxelShape NORTH_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 1.0D);
    private static final VoxelShape SOUTH_AABB = Block.box(0.0D, 0.0D, 15.0D, 16.0D, 16.0D, 16.0D);
    private final Map<BlockState, VoxelShape> shapesCache;

    public static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = PipeBlock.PROPERTY_BY_DIRECTION.entrySet().stream().collect(Util.toMap());
    // Support waterlogged on these blocks
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    protected WCVinesBlock(AbstractBlock.Settings settings, WesterosBlockDef def) {
        super(settings);
        this.def = def;        
        String t = def.getType();
        if (t != null) {
            String[] toks = t.split(",");
            for (String tok : toks) {
                if (tok.equals("allow-unsupported")) {
                    allow_unsupported = true;
                }
                if (tok.equals("no-climb")) {
                	no_climb = true;
                }
                if (tok.equals("has-down")) {
                	has_down = true;
                }
            }
        }
        this.shapesCache = ImmutableMap.copyOf(getDefaultState().getPossibleStates().stream().collect(Collectors.toMap(Function.identity(), WCVinesBlock::calculateShape)));
        setDefaultState(getDefaultState()
                .with(UP, Boolean.FALSE)
                .with(NORTH, Boolean.FALSE)
                .with(EAST, Boolean.FALSE)
        		.with(SOUTH, Boolean.FALSE)
                .with(WEST, Boolean.FALSE)
        		.with(DOWN, Boolean.FALSE)
                .with(WATERLOGGED, Boolean.FALSE));
    }
    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }

    private static VoxelShape calculateShape(BlockState state) {
        VoxelShape voxelshape = VoxelShapes.empty();
        if (state.get(UP)) {
           voxelshape = UP_AABB;
        }
        if (state.get(DOWN)) {
            voxelshape = VoxelShapes.union(voxelshape, DOWN_AABB);
         }

        if (state.get(NORTH)) {
           voxelshape = VoxelShapes.union(voxelshape, NORTH_AABB);
        }

        if (state.get(SOUTH)) {
           voxelshape = VoxelShapes.union(voxelshape, SOUTH_AABB);
        }

        if (state.get(EAST)) {
           voxelshape = VoxelShapes.union(voxelshape, EAST_AABB);
        }

        if (state.get(WEST)) {
           voxelshape = VoxelShapes.union(voxelshape, WEST_AABB);
        }
        return voxelshape;
     }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return this.shapesCache.get(p_220053_1_);
    }

    @Override
    public VoxelShape getSidesShape(BlockState state, BlockView world, BlockPos pos) {
        return VoxelShapes.empty();
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
    	return allow_unsupported || this.hasFaces(this.getUpdatedState(p_196260_1_, p_196260_2_, p_196260_3_));
    }

     private boolean hasFaces(BlockState p_196543_1_) {
        return this.countFaces(p_196543_1_) > 0;
     }

     private int countFaces(BlockState p_208496_1_) {
        int i = 0;

        for(BooleanProperty booleanproperty : PROPERTY_BY_DIRECTION.values()) {
           if (p_208496_1_.getValue(booleanproperty)) {
              ++i;
           }
        }

        return i;
     }

     private boolean canSupportAtFace(BlockGetter p_196541_1_, BlockPos p_196541_2_, Direction p_196541_3_) {
        if ((!has_down) && (p_196541_3_ == Direction.DOWN)) {
           return false;
        } else {
           BlockPos blockpos = p_196541_2_.offset(p_196541_3_);
           if (allow_unsupported ) {
        	   return true;
           }
           if (isAcceptableNeighbour(p_196541_1_, blockpos, p_196541_3_)) {
              return true;
           } else if (p_196541_3_.getAxis() == Direction.Axis.Y) {
              return false;
           } else {
              BooleanProperty booleanproperty = PROPERTY_BY_DIRECTION.get(p_196541_3_);
              BlockState blockstate = p_196541_1_.getBlockState(p_196541_2_.up());
              return blockstate.isOf(this) && blockstate.get(booleanproperty);
           }
        }
     }

     public static boolean isAcceptableNeighbour(BlockGetter p_196542_0_, BlockPos p_196542_1_, Direction p_196542_2_) {
        BlockState blockstate = p_196542_0_.getBlockState(p_196542_1_);
        return Block.isFaceFull(blockstate.getCollisionShape(p_196542_0_, p_196542_1_), p_196542_2_.getOpposite());
     }

     private BlockState getUpdatedState(BlockState p_196545_1_, BlockGetter p_196545_2_, BlockPos p_196545_3_) {
        BlockPos blockpos = p_196545_3_.up();
        if (p_196545_1_.get(UP)) {
           p_196545_1_ = p_196545_1_.with(UP, Boolean.valueOf(allow_unsupported || isAcceptableNeighbour(p_196545_2_, blockpos, Direction.DOWN)));
        }

        BlockState blockstate = null;

        for(Direction direction : Direction.Plane.HORIZONTAL) {
           BooleanProperty booleanproperty = getPropertyForFace(direction);
           if (p_196545_1_.get(booleanproperty)) {
              boolean flag = allow_unsupported || this.canSupportAtFace(p_196545_2_, p_196545_3_, direction);
              if (!flag) {
                 if (blockstate == null) {
                    blockstate = p_196545_2_.getBlockState(blockpos);
                 }

                 flag = blockstate.isOf(this) && get.getValue(booleanproperty);
              }

              p_196545_1_ = p_196545_1_.with(booleanproperty, flag);
           }
        }

        return p_196545_1_;
     }

    @Override
    public BlockState getStateForNeighborUpdate(
            BlockState state,
            Direction direction,
            BlockState neighborState,
            WorldAccess world,
            BlockPos pos,
            BlockPos neighborPos
    ) {          if ((!has_down) && (direction == Direction.DOWN)) {
           return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
        } else {
           BlockState blockstate = this.getUpdatedState(state, world, pos);
           return !this.hasFaces(blockstate) ? Blocks.AIR.getDefaultState() : blockstate;
        }
     }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState blockstate = ctx.getWorld().getBlockState(ctx.getBlockPos());
        boolean flag = blockstate.isOf(this);
        BlockState blockstate1 = flag ? blockstate : getDefaultState();
        FluidState fluidstate = ctx.getWorld().getFluidState(ctx.getBlockPos());
        blockstate1 = blockstate1.with(WATERLOGGED, fluidstate.isIn(FluidTags.WATER));

        for(Direction direction : ctx.getPlacementDirections()) {
           if (has_down || (direction != Direction.DOWN)) {
              BooleanProperty booleanproperty = getPropertyForFace(direction);
              boolean flag1 = flag && blockstate.get(booleanproperty);
              if (!flag1 && this.canSupportAtFace(ctx.getWorld(), ctx.getBlockPos(), direction)) {
                 return blockstate1.with(booleanproperty, Boolean.TRUE);
              }
           }
        }
        return flag ? blockstate1 : null;
     }

	@Override 
     public FluidState getFluidState(BlockState state) { 
         return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
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
     public void randomTick(BlockState p_225542_1_, ServerLevel p_225542_2_, BlockPos p_225542_3_, RandomSource p_225542_4_) {
     }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    	 builder.add(UP, NORTH, EAST, SOUTH, WEST, DOWN, WATERLOGGED);
     }

     public static BooleanProperty getPropertyForFace(Direction p_176267_0_) {
         return PROPERTY_BY_DIRECTION.get(p_176267_0_);
      }

     private static String[] TAGS = { "climbable" };
     private static String[] TAGS_NOCLIMB = {  };
     @Override
     public String[] getBlockTags() {
    	if (no_climb) return TAGS_NOCLIMB;
    	return TAGS;
     }
}
