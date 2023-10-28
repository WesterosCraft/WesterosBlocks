package com.westeroscraft.westerosblocks.blocks;

import javax.annotation.Nullable;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.tags.FluidTags;
import net.minecraft.core.Direction;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;

public class WCCuboidNEBlock extends WCCuboidBlock implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
        	def.nonOpaque = true;
        	BlockBehaviour.Properties props = def.makeProperties();
        	// See if we have a state property
        	WesterosBlockDef.StateProperty state = def.buildStateProperty();
        	if (state != null) {
        		tempSTATE = state;
        	}        	        	
        	return def.registerRenderType(def.registerBlock(new WCCuboidNEBlock(props, def)), false, false);
        }
    }
    
    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.EAST, Direction.NORTH);
    
    protected WCCuboidNEBlock(BlockBehaviour.Properties props, WesterosBlockDef def) {
        super(props, def, 2);
        
        int stcnt = def.states.size();
        for (int stidx = 0; stidx < stcnt; stidx++) {
        	for (WesterosBlockDef.Cuboid c : cuboid_by_facing[2*stidx]) {
        		cuboid_by_facing[2*stidx + 1].add(c.rotateCuboid(WesterosBlockDef.CuboidRotation.ROTY90));
        	}
        }
        for (int i = 0; i < cuboid_by_facing.length; i++) {
        	if (SHAPE_BY_INDEX[i] == null) {
        		SHAPE_BY_INDEX[i] = getBoundingBoxFromCuboidList(cuboid_by_facing[i]);
        	}
        }
        BlockState defbs = this.stateDefinition.any().setValue(FACING, Direction.EAST).setValue(WATERLOGGED, Boolean.valueOf(false));
        if (STATE != null) {
        	defbs = defbs.setValue(STATE, STATE.defValue);
        }
    	this.registerDefaultState(defbs);
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> StateDefinition) {
        super.createBlockStateDefinition(StateDefinition);
        StateDefinition.add(FACING);
    }
    
    @Override
    protected int getIndexFromState(BlockState state) {
    	return super.getIndexFromState(state) + ((state.getValue(FACING) == Direction.EAST) ? 0 : 1);
    }
    
    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        switch(rot) {
        	case CLOCKWISE_180:
            default:
                return state;
            case COUNTERCLOCKWISE_90:
            case CLOCKWISE_90:
            	return (state.getValue(FACING) == Direction.EAST) ?
            			state.setValue(FACING, Direction.NORTH) :
        				state.setValue(FACING, Direction.EAST);
        }
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
       FluidState fluidstate = ctx.getLevel().getFluidState(ctx.getClickedPos());
       Direction[] adirection = ctx.getNearestLookingDirections();
       Direction dir = Direction.EAST;	// Default
       for (Direction d : adirection) {
    	   if (d == Direction.EAST || d == Direction.WEST) {
    		   dir = Direction.EAST;
    		   break;
    	   }
    	   if (d == Direction.NORTH || d == Direction.SOUTH) {
    		   dir = Direction.NORTH;
    		   break;
    	   }
       }
       BlockState bs = this.defaultBlockState().setValue(FACING, dir).setValue(WATERLOGGED, Boolean.valueOf(fluidstate.is(FluidTags.WATER)));
       if (STATE != null) {
    	   bs = bs.setValue(STATE, STATE.defValue); 
       }
       return bs;
    }
}
