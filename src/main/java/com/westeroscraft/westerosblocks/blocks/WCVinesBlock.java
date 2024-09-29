package com.westeroscraft.westerosblocks.blocks;

import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import com.westeroscraft.westerosblocks.WesterosBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.tags.FluidTags;

import com.google.common.collect.ImmutableMap;
import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;
import net.neoforged.neoforge.registries.RegisterEvent;

public class WCVinesBlock extends VineBlock implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def, RegisterEvent.RegisterHelper<Block> helper) {
        	BlockBehaviour.Properties props = def.makeProperties().noOcclusion();
            Block blk = new WCVinesBlock(props, def);
            helper.register(ResourceLocation.fromNamespaceAndPath(WesterosBlocks.MOD_ID, def.blockName), blk);
            def.registerBlockItem(def.blockName, blk);
            return def.registerRenderType(blk, false, false);
        }
    }

    private WesterosBlockDef def;
    private boolean allow_unsupported = false;
    private boolean no_climb = false;
    public boolean has_down = false;
    public static final BooleanProperty DOWN = PipeBlock.DOWN;
    private static final VoxelShape UP_AABB = Block.box(0.0D, 15.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private static final VoxelShape DOWN_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);
    private static final VoxelShape WEST_AABB = Block.box(0.0D, 0.0D, 0.0D, 1.0D, 16.0D, 16.0D);
    private static final VoxelShape EAST_AABB = Block.box(15.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private static final VoxelShape NORTH_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 1.0D);
    private static final VoxelShape SOUTH_AABB = Block.box(0.0D, 0.0D, 15.0D, 16.0D, 16.0D, 16.0D);
    private final Map<BlockState, VoxelShape> shapesCache;

    public static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = PipeBlock.PROPERTY_BY_DIRECTION.entrySet().stream().collect(Util.toMap());
    // Support waterlogged on these blocks
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    protected WCVinesBlock(BlockBehaviour.Properties props, WesterosBlockDef def) {
        super(props);
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
        this.shapesCache = ImmutableMap.copyOf(this.stateDefinition.getPossibleStates().stream().collect(Collectors.toMap(Function.identity(), WCVinesBlock::calculateShape)));
        this.registerDefaultState(this.stateDefinition.any().setValue(UP, Boolean.valueOf(false)).setValue(NORTH, Boolean.valueOf(false)).setValue(EAST, Boolean.valueOf(false))
        		.setValue(SOUTH, Boolean.valueOf(false)).setValue(WEST, Boolean.valueOf(false))
        		.setValue(DOWN, Boolean.valueOf(false)).setValue(WATERLOGGED, Boolean.valueOf(false)));
    }
    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }

    private static VoxelShape calculateShape(BlockState p_242685_0_) {
        VoxelShape voxelshape = Shapes.empty();
        if (p_242685_0_.getValue(UP)) {
           voxelshape = UP_AABB;
        }
        if (p_242685_0_.getValue(DOWN)) {
            voxelshape = Shapes.or(voxelshape, DOWN_AABB);
         }

        if (p_242685_0_.getValue(NORTH)) {
           voxelshape = Shapes.or(voxelshape, NORTH_AABB);
        }

        if (p_242685_0_.getValue(SOUTH)) {
           voxelshape = Shapes.or(voxelshape, SOUTH_AABB);
        }

        if (p_242685_0_.getValue(EAST)) {
           voxelshape = Shapes.or(voxelshape, EAST_AABB);
        }

        if (p_242685_0_.getValue(WEST)) {
           voxelshape = Shapes.or(voxelshape, WEST_AABB);
        }
        return voxelshape;
     }

    
    @Override
    public VoxelShape getShape(BlockState p_220053_1_, BlockGetter p_220053_2_, BlockPos p_220053_3_, CollisionContext p_220053_4_) {
        return this.shapesCache.get(p_220053_1_);
    } 
    @Override
    public VoxelShape getBlockSupportShape(BlockState state, BlockGetter reader, BlockPos pos) {
        return Shapes.empty();
    }

    @Override
    public boolean canSurvive(BlockState p_196260_1_, LevelReader p_196260_2_, BlockPos p_196260_3_) {
    	
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
           BlockPos blockpos = p_196541_2_.relative(p_196541_3_);
           if (allow_unsupported ) {
        	   return true;
           }
           if (isAcceptableNeighbour(p_196541_1_, blockpos, p_196541_3_)) {
              return true;
           } else if (p_196541_3_.getAxis() == Direction.Axis.Y) {
              return false;
           } else {
              BooleanProperty booleanproperty = PROPERTY_BY_DIRECTION.get(p_196541_3_);
              BlockState blockstate = p_196541_1_.getBlockState(p_196541_2_.above());
              return blockstate.is(this) && blockstate.getValue(booleanproperty);
           }
        }
     }

     public static boolean isAcceptableNeighbour(BlockGetter p_196542_0_, BlockPos p_196542_1_, Direction p_196542_2_) {
        BlockState blockstate = p_196542_0_.getBlockState(p_196542_1_);
        return Block.isFaceFull(blockstate.getCollisionShape(p_196542_0_, p_196542_1_), p_196542_2_.getOpposite());
     }

     private BlockState getUpdatedState(BlockState p_196545_1_, BlockGetter p_196545_2_, BlockPos p_196545_3_) {
        BlockPos blockpos = p_196545_3_.above();
        if (p_196545_1_.getValue(UP)) {
           p_196545_1_ = p_196545_1_.setValue(UP, Boolean.valueOf(allow_unsupported || isAcceptableNeighbour(p_196545_2_, blockpos, Direction.DOWN)));
        }

        BlockState blockstate = null;

        for(Direction direction : Direction.Plane.HORIZONTAL) {
           BooleanProperty booleanproperty = getPropertyForFace(direction);
           if (p_196545_1_.getValue(booleanproperty)) {
              boolean flag = allow_unsupported || this.canSupportAtFace(p_196545_2_, p_196545_3_, direction);
              if (!flag) {
                 if (blockstate == null) {
                    blockstate = p_196545_2_.getBlockState(blockpos);
                 }

                 flag = blockstate.is(this) && blockstate.getValue(booleanproperty);
              }

              p_196545_1_ = p_196545_1_.setValue(booleanproperty, Boolean.valueOf(flag));
           }
        }

        return p_196545_1_;
     }

     @Override
     public BlockState updateShape(BlockState p_196271_1_, Direction p_196271_2_, BlockState p_196271_3_, LevelAccessor p_196271_4_, BlockPos p_196271_5_, BlockPos p_196271_6_) {
        if ((!has_down) && (p_196271_2_ == Direction.DOWN)) {
           return super.updateShape(p_196271_1_, p_196271_2_, p_196271_3_, p_196271_4_, p_196271_5_, p_196271_6_);
        } else {
           BlockState blockstate = this.getUpdatedState(p_196271_1_, p_196271_4_, p_196271_5_);
           return !this.hasFaces(blockstate) ? Blocks.AIR.defaultBlockState() : blockstate;
        }
     } 

     @Nullable 
     @Override
     public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState blockstate = ctx.getLevel().getBlockState(ctx.getClickedPos());
        boolean flag = blockstate.is(this);
        BlockState blockstate1 = flag ? blockstate : this.defaultBlockState();
        FluidState fluidstate = ctx.getLevel().getFluidState(ctx.getClickedPos());
        blockstate1 = blockstate1.setValue(WATERLOGGED, Boolean.valueOf(fluidstate.is(FluidTags.WATER)));

        for(Direction direction : ctx.getNearestLookingDirections()) {
           if (has_down || (direction != Direction.DOWN)) {
              BooleanProperty booleanproperty = getPropertyForFace(direction);
              boolean flag1 = flag && blockstate.getValue(booleanproperty);
              if (!flag1 && this.canSupportAtFace(ctx.getLevel(), ctx.getClickedPos(), direction)) {
                 return blockstate1.setValue(booleanproperty, Boolean.valueOf(true));
              }
           }
        }
        return flag ? blockstate1 : null;
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
     public void randomTick(BlockState p_225542_1_, ServerLevel p_225542_2_, BlockPos p_225542_3_, RandomSource p_225542_4_) {
     }
     @Override
     protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> container) {
    	 container.add(UP, NORTH, EAST, SOUTH, WEST, DOWN, WATERLOGGED);
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
