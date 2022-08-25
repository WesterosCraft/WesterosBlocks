package com.westeroscraft.westerosblocks.blocks;

import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;

import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import javax.annotation.Nullable;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.WebBlock;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public class WCWebVertBlock extends WebBlock implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
        	BlockBehaviour.Properties props = def.makeProperties().noCollission();
        	// See if we have a cond property
        	WesterosBlockDef.CondProperty prop = def.buildCondProperty();
        	if (prop != null) {
        		tempCOND = prop;
        	}        	
        	return def.registerRenderType(def.registerBlock(new WCWebVertBlock(props, def)), false, false);
        }
    }
    private WesterosBlockDef def;
    private boolean noInWeb = false;
    public static final BooleanProperty DOWN = BlockStateProperties.DOWN;
    public static final BooleanProperty UP = BlockStateProperties.UP;
    protected static WesterosBlockDef.CondProperty tempCOND;
    protected WesterosBlockDef.CondProperty COND;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    
    protected WCWebVertBlock(BlockBehaviour.Properties props, WesterosBlockDef def) {
        super(props);
        this.def = def;
        String t = def.getType();
        if (t != null) {
            String[] toks = t.split(",");
            for (String tok : toks) {
                if (tok.equals("no-in-web")) {
                    noInWeb = true;
                }
            }
        }
        if (COND != null) {
            this.registerDefaultState(this.stateDefinition.any().setValue(UP, Boolean.valueOf(false)).setValue(DOWN, Boolean.valueOf(false)).setValue(COND, COND.defValue).setValue(WATERLOGGED, Boolean.valueOf(false)));
        }
        else {
        	this.registerDefaultState(this.stateDefinition.any().setValue(UP, Boolean.valueOf(false)).setValue(DOWN, Boolean.valueOf(false)).setValue(WATERLOGGED, Boolean.valueOf(false)));
        }
    }

    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> container) {
    	super.createBlockStateDefinition(container);
    	if (tempCOND != null) {
    		COND = tempCOND;
    		tempCOND = null;
    	}
    	if (COND != null) {
	       container.add(COND);
    	}
        container.add(WATERLOGGED, UP, DOWN);
    }
    
    private BlockState updateStateVertical(BlockState bs, BlockGetter reader, BlockPos pos) {
    	BlockState bsneighbor = reader.getBlockState(pos.above());
    	Boolean up = Boolean.valueOf(def.isConnectMatch(bs, bsneighbor));
    	bsneighbor = reader.getBlockState(pos.below());
    	Boolean down = Boolean.valueOf(def.isConnectMatch(bs, bsneighbor));
    	return bs.setValue(UP, up).setValue(DOWN, down);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction dir, BlockState state2, LevelAccessor world, BlockPos pos, BlockPos pos2) {
    	state = super.updateShape(state, dir, state2, world, pos, pos2);
    	if (state != null) {
    		state = updateStateVertical(state, world, pos);
    	}
    	return state;
    }

    @Nullable 
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
    	BlockState bs = super.getStateForPlacement(ctx);
    	if (bs != null) {
    		bs = updateStateVertical(this.defaultBlockState(), ctx.getLevel(), ctx.getClickedPos());
    	}
    	if ((COND != null) && (bs != null)) {
    		bs = bs.setValue(COND, def.getMatchingCondition(ctx.getLevel(), ctx.getClickedPos())); 
    	}
        FluidState fluidstate = ctx.getLevel().getFluidState(ctx.getClickedPos());
        bs = bs.setValue(WATERLOGGED, Boolean.valueOf(fluidstate.is(FluidTags.WATER)));
    	return bs;
    }

    @Override
    public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
    	if (!noInWeb)
    		super.entityInside(state, world, pos, entity);
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

    private static String[] TAGS = { };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }    

}
