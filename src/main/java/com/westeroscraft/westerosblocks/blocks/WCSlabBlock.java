package com.westeroscraft.westerosblocks.blocks;

import com.westeroscraft.westerosblocks.*;

import javax.annotation.Nullable;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.SlabType;

public class WCSlabBlock extends SlabBlock implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
        	BlockBehaviour.Properties props = def.makeProperties();
        	// See if we have a cond property
        	WesterosBlockDef.CondProperty prop = def.buildCondProperty();
        	if (prop != null) {
        		tempCOND = prop;
        	}        	        	
        	return def.registerRenderType(def.registerBlock(new WCSlabBlock(props, def)), false, false);
        }
    }
    protected WesterosBlockDef def;
    private static WesterosBlockDef.CondProperty tempCOND;
    private WesterosBlockDef.CondProperty COND;

    protected WCSlabBlock(BlockBehaviour.Properties props, WesterosBlockDef def) {
        super(props);
        this.def = def;
        if (COND != null) {
            this.registerDefaultState(this.defaultBlockState().setValue(TYPE, SlabType.BOTTOM).setValue(WATERLOGGED, Boolean.valueOf(false)).setValue(COND, COND.defValue));
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

    private static String[] TAGS = { "slabs" };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }    

}
