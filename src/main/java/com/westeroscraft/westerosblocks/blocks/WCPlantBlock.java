package com.westeroscraft.westerosblocks.blocks;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraftforge.common.IPlantable;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.tags.FluidTags;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;

import javax.annotation.Nullable;

public class WCPlantBlock extends Block implements WesterosBlockLifecycle, IPlantable {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
        	BlockBehaviour.Properties props = def.makeProperties().noCollission().instabreak();
        	// See if we have a cond property
        	WesterosBlockDef.CondProperty prop = def.buildCondProperty();
        	if (prop != null) {
        		tempCOND = prop;
        	}        	

        	return def.registerRenderType(def.registerBlock(new WCPlantBlock(props, def)), false, false);
        }
    }    
    // Support waterlogged on these blocks
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private WesterosBlockDef def;
    protected static WesterosBlockDef.CondProperty tempCOND;
    protected WesterosBlockDef.CondProperty COND;

    protected WCPlantBlock(BlockBehaviour.Properties props, WesterosBlockDef def) {
        super(props);
        this.def = def;
        if (COND != null) {
            this.registerDefaultState(this.stateDefinition.any().setValue(COND, COND.defValue).setValue(WATERLOGGED, Boolean.valueOf(false)));
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
    	super.createBlockStateDefinition(StateDefinition);
    	if (tempCOND != null) {
    		COND = tempCOND;
    		tempCOND = null;
    	}
        StateDefinition.add(WATERLOGGED);
    	if (COND != null) {
	       StateDefinition.add(COND);
    	}
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
    	BlockState bs = super.getStateForPlacement(ctx);
    	if ((COND != null) && (bs != null)) {
    		bs = bs.setValue(COND, def.getMatchingCondition(ctx.getLevel(), ctx.getClickedPos())); 
    	}
        FluidState fluidstate = ctx.getLevel().getFluidState(ctx.getClickedPos());
        bs = bs.setValue(WATERLOGGED, Boolean.valueOf(fluidstate.is(FluidTags.WATER)));
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
    
	@Override
	public BlockState getPlant(BlockGetter world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);
      	if (state.getBlock() != this) return defaultBlockState();
      	return state;
	}
	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
		return state.getFluidState().isEmpty();
    }
    private static String[] TAGS = { "flowers" };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }    
	
}
