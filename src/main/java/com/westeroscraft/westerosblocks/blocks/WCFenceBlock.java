package com.westeroscraft.westerosblocks.blocks;

import javax.annotation.Nullable;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.StateDefinition;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;
import net.minecraft.world.level.block.state.BlockState;

public class WCFenceBlock extends FenceBlock implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
        	BlockBehaviour.Properties props = def.makeProperties();
        	// See if we have a cond property
        	WesterosBlockDef.CondProperty prop = def.buildCondProperty();
        	if (prop != null) {
        		tempCOND = prop;
        	}        	
        	return def.registerRenderType(def.registerBlock(new WCFenceBlock(props, def)), false, false);
        }
    };
    
    private WesterosBlockDef def;
    private static WesterosBlockDef.CondProperty tempCOND;
    private WesterosBlockDef.CondProperty COND;

    protected WCFenceBlock(BlockBehaviour.Properties props, WesterosBlockDef def) {
        super(props);
        this.def = def;
        if (COND != null) {
            this.registerDefaultState(this.stateDefinition.any().setValue(NORTH, Boolean.valueOf(false)).setValue(EAST, Boolean.valueOf(false)).setValue(SOUTH, Boolean.valueOf(false)).setValue(WEST, Boolean.valueOf(false)).setValue(WATERLOGGED, Boolean.valueOf(false)).setValue(COND, COND.defValue));
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
    	if (COND != null) {
	       StateDefinition.add(COND);
    	}
    }

    @Override  
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
    	BlockState bs = super.getStateForPlacement(ctx);
    	if ((bs != null) && (COND != null)) {
    		bs = bs.setValue(COND, def.getMatchingCondition(ctx.getLevel(), ctx.getClickedPos())); 
    	}
    	return bs;
    }

    private static String[] TAGS = { "fences" };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }    
}
