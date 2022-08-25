package com.westeroscraft.westerosblocks.blocks;

import com.westeroscraft.westerosblocks.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;

import javax.annotation.Nullable;

public class WCSolidBlock extends Block implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
        	BlockBehaviour.Properties props = def.makeProperties();
        	// See if we have a cond property
        	WesterosBlockDef.CondProperty prop = def.buildCondProperty();
        	if (prop != null) {
        		tempCOND = prop;
        	}        	
        	return def.registerRenderType(def.registerBlock(new WCSolidBlock(props, def)), true, def.nonOpaque);
        }
    }    
    protected WesterosBlockDef def;
    protected VoxelShape collisionbox;
    
    protected static WesterosBlockDef.CondProperty tempCOND;
    protected WesterosBlockDef.CondProperty COND;
    
    protected WCSolidBlock(BlockBehaviour.Properties props, WesterosBlockDef def) {
        super(props);
        this.def = def;
        collisionbox = def.makeCollisionBoxShape();
        if (COND != null) {
            this.registerDefaultState(this.stateDefinition.any().setValue(COND, COND.defValue));
        }
    }
    
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext ctx) {
        return collisionbox;
    }
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext ctx) {
        return collisionbox;
    }
    @Override
    public VoxelShape getBlockSupportShape(BlockState state, BlockGetter reader, BlockPos pos) {
        return collisionbox;
    }
    @Override
    public VoxelShape getVisualShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext ctx) {
        return collisionbox;
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
    	if ((COND != null) && (bs != null)) {
    		bs = bs.setValue(COND, def.getMatchingCondition(ctx.getLevel(), ctx.getClickedPos())); 
    	}
    	return bs;
    }

    private static String[] TAGS = { };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }    

}
