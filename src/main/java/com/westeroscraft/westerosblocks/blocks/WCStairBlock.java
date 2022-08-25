package com.westeroscraft.westerosblocks.blocks;

import javax.annotation.Nullable;

import com.westeroscraft.westerosblocks.*;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.StateDefinition;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.core.Direction;

public class WCStairBlock extends StairBlock implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
            if (def.modelBlockName == null) {
                WesterosBlocks.log.error("Type 'stair' requires modelBlockName settings");
                return null;
            }
            // Try to find model block
            Block blk = WesterosBlocks.findBlockByName(def.modelBlockName);
            if (blk == null) {
                WesterosBlocks.log.error(String.format("modelBlockName '%s' not found for block '%s'", def.modelBlockName, def.blockName));
                return null;
            }
            WesterosBlockDef mbdef = null;
            // If a WB, look up def
            if (blk instanceof WesterosBlockLifecycle) {
            	mbdef = ((WesterosBlockLifecycle) blk). getWBDefinition();
            	// See if we have a cond property
            	WesterosBlockDef.CondProperty prop = mbdef.buildCondProperty();
            	if (prop != null) {
            		tempCOND = prop;
            	}        	
            }
        	BlockBehaviour.Properties props = def.makeAndCopyProperties(blk);
        	return def.registerRenderType(def.registerBlock(new WCStairBlock(blk.defaultBlockState(), props, def, mbdef)), false, false);
        }
    }
    
    private WesterosBlockDef def;
    private static WesterosBlockDef.CondProperty tempCOND;
    private WesterosBlockDef.CondProperty COND;
    
    protected WCStairBlock(BlockState modelstate, BlockBehaviour.Properties props, WesterosBlockDef def, WesterosBlockDef moddef) {
        super(() -> modelstate, props);
        this.def = def;
        if (COND != null) {
            this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(HALF, Half.BOTTOM).setValue(SHAPE, StairsShape.STRAIGHT).setValue(WATERLOGGED, Boolean.valueOf(false)).setValue(COND, COND.defValue));
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

    private static String[] TAGS = { "stairs" };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }    

}
