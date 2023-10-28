package com.westeroscraft.westerosblocks.blocks;

import javax.annotation.Nullable;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;

public class WCCuboid16WayBlock extends WCCuboidBlock implements WesterosBlockLifecycle {
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
        	return def.registerRenderType(def.registerBlock(new WCCuboid16WayBlock(props, def)), false, false);
        }
    }

    private static final int ROTATIONS = 16;
    public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;
    
    private static final WesterosBlockDef.CuboidRotation[] shape_rotations = { 
		WesterosBlockDef.CuboidRotation.NONE,
		WesterosBlockDef.CuboidRotation.ROTY90,
		WesterosBlockDef.CuboidRotation.ROTY180,
		WesterosBlockDef.CuboidRotation.ROTY270 };
    protected WCCuboid16WayBlock(BlockBehaviour.Properties props, WesterosBlockDef def) {
        super(props, def, ROTATIONS);
        
        // Build rotations - one set for each state, if needed
        int stcnt = def.states.size();
        for (int stidx = 0; stidx < stcnt; stidx++) {
        	int idx = ROTATIONS * stidx;
	        for (int i = 1; i < ROTATIONS; i++) {
		        for (WesterosBlockDef.Cuboid c : cuboid_by_facing[idx]) {
		        	cuboid_by_facing[idx+i].add(c.rotateCuboid(shape_rotations[i / 4]));
		        }
	        }
        }
        for (int i = 0; i < cuboid_by_facing.length; i++) {
        	if (SHAPE_BY_INDEX[i] == null) {
        		SHAPE_BY_INDEX[i] = getBoundingBoxFromCuboidList(cuboid_by_facing[i]);
        	}
        }
        BlockState defbs = this.stateDefinition.any().setValue(ROTATION, 0).setValue(WATERLOGGED, Boolean.valueOf(false));
        if (STATE != null) {
        	defbs = defbs.setValue(STATE, STATE.defValue);
        }
    	this.registerDefaultState(defbs);        	
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> StateDefinition) {
        super.createBlockStateDefinition(StateDefinition);
        StateDefinition.add(ROTATION);
    }
    
    @Override
    protected int getIndexFromState(BlockState state) {
    	if (STATE != null) {
    		return (STATE.getIndex(state.getValue(STATE)) * ROTATIONS) + state.getValue(ROTATION);
    	}
    	else {
    		return state.getValue(ROTATION);
    	}
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(ROTATION, Integer.valueOf(rot.rotate(state.getValue(ROTATION), 16)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.setValue(ROTATION, Integer.valueOf(mirror.mirror(state.getValue(ROTATION), 16)));
     }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
       FluidState fluidstate = ctx.getLevel().getFluidState(ctx.getClickedPos());
       Integer dir = Integer.valueOf(Mth.floor((double)(ctx.getRotation() * 16.0F / 360.0F) + 0.5D) & 15);
       BlockState bs = this.defaultBlockState().setValue(ROTATION, dir).setValue(WATERLOGGED, Boolean.valueOf(fluidstate.is(FluidTags.WATER)));
       if (STATE != null) {
    	   bs = bs.setValue(STATE, STATE.defValue); 
       }
       return bs;
    }
}
