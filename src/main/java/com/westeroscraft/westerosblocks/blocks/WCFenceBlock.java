package com.westeroscraft.westerosblocks.blocks;

import javax.annotation.Nullable;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluids;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;
import net.minecraft.world.level.block.state.BlockState;

public class WCFenceBlock extends FenceBlock implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
        	BlockBehaviour.Properties props = def.makeProperties();
            String t = def.getType();
            Boolean doUnconnect = null;
            if (t != null) {
                String[] toks = t.split(",");
                for (String tok : toks) {
                	String[] parts = tok.split(":");
                    if (parts[0].equals("unconnect")) {
                    	doUnconnect = "true".equals(parts[1]);
                    	tempUNCONNECT = UNCONNECT;
                    }
                }
            }
        	return def.registerRenderType(def.registerBlock(new WCFenceBlock(props, def, doUnconnect)), false, false);
        }
    };
    
    public static final BooleanProperty UNCONNECT = BooleanProperty.create("unconnect");
    protected static BooleanProperty tempUNCONNECT;

    public final boolean unconnect;
    public final Boolean unconnectDef;
    
    private WesterosBlockDef def;

    protected WCFenceBlock(BlockBehaviour.Properties props, WesterosBlockDef def, Boolean doUnconnect) {
        super(props);
        this.def = def;
        unconnect = (doUnconnect != null);
        unconnectDef = doUnconnect;
        if (unconnect) {
        	this.registerDefaultState(this.stateDefinition.any().setValue(NORTH, Boolean.valueOf(false)).setValue(EAST, Boolean.valueOf(false)).setValue(SOUTH, Boolean.valueOf(false)).setValue(WEST, Boolean.valueOf(false)).setValue(WATERLOGGED, Boolean.valueOf(false)).setValue(UNCONNECT, unconnectDef));
        }
    }

    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }    
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinition) {
    	if (tempUNCONNECT != null) {
    		stateDefinition.add(tempUNCONNECT);
    		tempUNCONNECT = null;
    	}
    	super.createBlockStateDefinition(stateDefinition);
    }

    @Override  
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
    	if (unconnect && unconnectDef) {
    		return this.defaultBlockState();
    	}
    	return super.getStateForPlacement(ctx);
    }
    

    @Override  
    public BlockState updateShape(BlockState state, Direction dir, BlockState nstate, LevelAccessor world, BlockPos pos, BlockPos pos2) {
    	if (unconnect && state.getValue(UNCONNECT)) {
            if (state.getValue(WATERLOGGED)) {
                world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
            }
            return state;
    	}
    	return super.updateShape(state, dir, nstate, world, pos, pos);
    }

    @Override  
    public boolean connectsTo(BlockState p_53330_, boolean p_53331_, Direction p_53332_) {
        Block block = p_53330_.getBlock();
        boolean flag = this.isSameFence(p_53330_) && ((!p_53330_.hasProperty(UNCONNECT)) || (!p_53330_.getValue(UNCONNECT)));
        boolean flag1 = block instanceof FenceGateBlock && FenceGateBlock.connectsToDirection(p_53330_, p_53332_);
        return !isExceptionForConnection(p_53330_) && p_53331_ || flag || flag1;
     }

     private boolean isSameFence(BlockState p_153255_) {
        return p_153255_.is(BlockTags.FENCES) && p_153255_.is(BlockTags.WOODEN_FENCES) == this.defaultBlockState().is(BlockTags.WOODEN_FENCES);
     }

    private static String[] TAGS = { "fences" };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }    
}
