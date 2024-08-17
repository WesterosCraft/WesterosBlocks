package com.westeroscraft.westerosblocks.blocks;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.tags.FluidTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import javax.annotation.Nullable;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;

public class WCFanBlock extends Block implements SimpleWaterloggedBlock, WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
        	BlockBehaviour.Properties floorprops = def.makeProperties().noCollission().instabreak();
        	Block floorblock = new WCFanBlock(floorprops, def);
        	@SuppressWarnings("deprecation")
			    BlockBehaviour.Properties wallprops = def.makeProperties().noCollission().instabreak().dropsLike(floorblock);
        	Block wallblock = new WCWallFanBlock(wallprops, def);
        	def.registerWallOrFloorBlock(floorblock, wallblock);
        	
        	def.registerRenderType(floorblock, false, false);
        	def.registerRenderType(wallblock, false, false);
        	return floorblock;
        }
    }

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    
    private WesterosBlockDef def;
    private boolean allow_unsupported = false;

    private static final VoxelShape SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 4.0, 14.0);
    
    protected WCFanBlock(BlockBehaviour.Properties props, WesterosBlockDef def) {
        super(props);
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
        BlockState defbs = this.stateDefinition.any().setValue(WATERLOGGED, Boolean.valueOf(false));
        this.registerDefaultState(defbs);
    }

    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
    	BlockState bs = super.getStateForPlacement(ctx);
    	if (bs == null) return null;
      FluidState fluidstate = ctx.getLevel().getFluidState(ctx.getClickedPos());
      bs = bs.setValue(WATERLOGGED, Boolean.valueOf(fluidstate.is(FluidTags.WATER)));
    	return bs;
    }

    @SuppressWarnings("deprecation")
	  @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType PathComputationType) {
        switch(PathComputationType) {
        case LAND:
           return false;
        case WATER:
           return reader.getFluidState(pos).is(FluidTags.WATER);
        case AIR:
           return false;
        default:
           return false;
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinition) {
    	stateDefinition.add(WATERLOGGED);
    }

    @Override
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
        if (direction == Direction.DOWN && !this.canSurvive(blockState, levelAccessor, blockPos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(blockState, direction, blockState2, levelAccessor, blockPos, blockPos2);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader levelReader, BlockPos blockPos) {
    	if (this.allow_unsupported) return true;
      BlockPos blockPos2 = blockPos.below();
      return levelReader.getBlockState(blockPos2).isFaceSturdy(levelReader, blockPos2, Direction.UP);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
      return state.getFluidState().isEmpty();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
      return SHAPE;
    }

    private static String[] TAGS = { "fans" };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }
    
}
