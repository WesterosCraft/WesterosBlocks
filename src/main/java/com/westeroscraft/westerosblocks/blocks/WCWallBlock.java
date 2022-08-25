package com.westeroscraft.westerosblocks.blocks;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

import javax.annotation.Nullable;

import net.minecraft.world.level.block.WallBlock;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.WallSide;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.BlockPos;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;

public class WCWallBlock extends WallBlock implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
        	BlockBehaviour.Properties props = def.makeProperties();
        	// See if we have a cond property
        	WesterosBlockDef.CondProperty prop = def.buildCondProperty();
        	if (prop != null) {
        		tempCOND = prop;
        	}        	
        	return def.registerRenderType(def.registerBlock(new WCWallBlock(props, def)), false, false);
        }
    }
    private WesterosBlockDef def;
    
    private static WesterosBlockDef.CondProperty tempCOND;
    private WesterosBlockDef.CondProperty COND;

    public final Map<BlockState, VoxelShape> ourShapeByIndex;
    public final Map<BlockState, VoxelShape> ourCollisionShapeByIndex;

    public static enum WallSize {
    	NORMAL,	// 14/16 high wall
    	SHORT	// 13/16 high wall
    };
    public final WallSize wallSize;	// "normal", or "short"
    protected WCWallBlock(BlockBehaviour.Properties props, WesterosBlockDef def) {
        super(props);
        this.def = def;
        String height = def.getTypeValue("size", "normal");
        if (height.equals("short")) { 
        	wallSize = WallSize.SHORT;
        }
        else {
        	wallSize = WallSize.NORMAL;
        }
        if (COND != null) {
            this.registerDefaultState(this.stateDefinition.any().setValue(UP, Boolean.valueOf(true)).setValue(NORTH_WALL, WallSide.NONE).setValue(EAST_WALL, WallSide.NONE).setValue(SOUTH_WALL, WallSide.NONE).setValue(WEST_WALL, WallSide.NONE).setValue(WATERLOGGED, Boolean.valueOf(false)).setValue(COND, COND.defValue));

            // Now, need to fix the shape by index mappings
            Builder<BlockState, VoxelShape> builder = ImmutableMap.builder();
            for (BlockState bs : this.shapeByIndex.keySet()) {
            	VoxelShape shape = this.shapeByIndex.get(bs);
            	// Add our combinations with COND to new map
            	for (String val : COND.getPossibleValues()) {
            		BlockState bsnew = bs.setValue(COND, val);
            		builder.put(bsnew, shape);
            	}
            }
            this.ourShapeByIndex = builder.build();
            // Now, need to fix the collision  by index mappings
            builder = builder = ImmutableMap.builder();
            for (BlockState bs : this.collisionShapeByIndex.keySet()) {
            	VoxelShape shape = this.collisionShapeByIndex.get(bs);
            	// See if the other cond values are in place
            	for (String val : COND.getPossibleValues()) {
            		BlockState bsnew = bs.setValue(COND, val);
            		builder.put(bsnew, shape);
            	}
            }
            this.ourCollisionShapeByIndex = builder.build();
        }
        else {	// Just use parent's one
        	this.ourCollisionShapeByIndex = this.collisionShapeByIndex;
        	this.ourShapeByIndex = this.shapeByIndex;
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
    		StateDefinition.add(UP, NORTH_WALL, EAST_WALL, WEST_WALL, SOUTH_WALL, WATERLOGGED, COND); 	       
     	}
    	else {
    		StateDefinition.add(UP, NORTH_WALL, EAST_WALL, WEST_WALL, SOUTH_WALL, WATERLOGGED); 	       
    	}
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, BlockGetter p_220053_2_, BlockPos p_220053_3_, CollisionContext p_220053_4_) {
        return this.ourShapeByIndex.get(p_220053_1_);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState p_220071_1_, BlockGetter p_220071_2_, BlockPos p_220071_3_, CollisionContext p_220071_4_) {
        return this.ourCollisionShapeByIndex.get(p_220071_1_);
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
    private static String[] TAGS = { "walls" };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }    

}
