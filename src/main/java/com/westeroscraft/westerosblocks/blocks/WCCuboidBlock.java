package com.westeroscraft.westerosblocks.blocks;

import java.util.List;

import javax.annotation.Nullable;

import com.westeroscraft.westerosblocks.*;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.tags.FluidTags;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.StateDefinition;

import net.minecraft.world.phys.shapes.CollisionContext;

public class WCCuboidBlock extends Block implements WesterosBlockLifecycle, SimpleWaterloggedBlock {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
        	def.nonOpaque = true;
        	BlockBehaviour.Properties props = def.makeProperties();
        	// See if we have a cond property
        	WesterosBlockDef.CondProperty prop = def.buildCondProperty();
        	if (prop != null) {
        		tempCOND = prop;
        	}        	
        	return def.registerRenderType(def.registerBlock(new WCCuboidBlock(props, def)), false, false);
        }
    }
    // Support waterlogged on these blocks
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    
    protected static WesterosBlockDef.CondProperty tempCOND;
    protected WesterosBlockDef.CondProperty COND;

    protected WesterosBlockDef def;
    
    protected VoxelShape[] SHAPE_BY_INDEX;

    protected WCCuboidBlock(BlockBehaviour.Properties props, WesterosBlockDef def) {
        super(props);
        this.def = def;
        SHAPE_BY_INDEX = new VoxelShape[1];
        SHAPE_BY_INDEX[0] = getBoundingBoxFromCuboidList(def.getCuboidList());
        if (COND != null) {
            this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, Boolean.valueOf(false)).setValue(COND, COND.defValue));
        }
        else {
            this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, Boolean.valueOf(false)));        	
        }
    }
    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> StateDefinition) {
    	if (tempCOND != null) {
    		COND = tempCOND;
    		tempCOND = null;
    	}
    	if (COND != null) {
	       StateDefinition.add(COND);
    	}
       StateDefinition.add(WATERLOGGED);
    }

    protected int getIndexFromState(BlockState state) {
    	return 0;
    }
    
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext ctx) {
        return SHAPE_BY_INDEX[getIndexFromState(state)];
    }
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext ctx) {
        return SHAPE_BY_INDEX[getIndexFromState(state)];
    }
    @Override
    public VoxelShape getBlockSupportShape(BlockState state, BlockGetter reader, BlockPos pos) {
        return SHAPE_BY_INDEX[getIndexFromState(state)];
    }
    @Override
    public VoxelShape getVisualShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext ctx) {
        return SHAPE_BY_INDEX[getIndexFromState(state)];
    }
    @Override
    public BlockState updateShape(BlockState state, Direction face, BlockState state2, LevelAccessor world, BlockPos pos, BlockPos pos2) {
       if (state.getValue(WATERLOGGED)) {
          world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
       }
       return super.updateShape(state, face, state2, world, pos, pos2);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
       FluidState fluidstate = ctx.getLevel().getFluidState(ctx.getClickedPos());
       BlockState bs = this.defaultBlockState().setValue(WATERLOGGED, Boolean.valueOf(fluidstate.is(FluidTags.WATER)));
       if ((COND != null) && (bs != null)) {
    	   bs = bs.setValue(COND, def.getMatchingCondition(ctx.getLevel(), ctx.getClickedPos())); 
       }
       return bs;
    }
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
    
    public List<WesterosBlockDef.Cuboid> getModelCuboids() {
    	return def.cuboids;
    }
    protected VoxelShape getBoundingBoxFromCuboidList(List<WesterosBlockDef.Cuboid> cl) {
        VoxelShape vs = Shapes.empty();
        if (cl != null) {
            for(WesterosBlockDef.Cuboid c : cl) {
            	vs = Shapes.or(vs, Shapes.box(c.xMin, c.yMin, c.zMin, c.xMax, c.yMax, c.zMax));
            }
        }
        return vs;
    }
    
    private static String[] TAGS = { };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }    

}
