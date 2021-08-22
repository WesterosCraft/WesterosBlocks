package com.westeroscraft.westerosblocks.blocks;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

import javax.annotation.Nullable;

import net.minecraft.block.WallBlock;
import org.dynmap.modsupport.ModModelDefinition;
import org.dynmap.modsupport.ModTextureDefinition;
import org.dynmap.modsupport.WallFenceBlockModel;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.WallHeight;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.util.math.BlockPos;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockDynmapSupport;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;

public class WCWallBlock extends WallBlock implements WesterosBlockLifecycle, WesterosBlockDynmapSupport {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
        	AbstractBlock.Properties props = def.makeProperties();
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
    protected WCWallBlock(AbstractBlock.Properties props, WesterosBlockDef def) {
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
            this.registerDefaultState(this.stateDefinition.any().setValue(UP, Boolean.valueOf(true)).setValue(NORTH_WALL, WallHeight.NONE).setValue(EAST_WALL, WallHeight.NONE).setValue(SOUTH_WALL, WallHeight.NONE).setValue(WEST_WALL, WallHeight.NONE).setValue(WATERLOGGED, Boolean.valueOf(false)).setValue(COND, COND.defValue));

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
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> stateContainer) {
    	if (tempCOND != null) {
    		COND = tempCOND;
    		tempCOND = null;
    	}
    	if (COND != null) {
    		stateContainer.add(UP, NORTH_WALL, EAST_WALL, WEST_WALL, SOUTH_WALL, WATERLOGGED, COND); 	       
     	}
    	else {
    		stateContainer.add(UP, NORTH_WALL, EAST_WALL, WEST_WALL, SOUTH_WALL, WATERLOGGED); 	       
    	}
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        return this.ourShapeByIndex.get(p_220053_1_);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState p_220071_1_, IBlockReader p_220071_2_, BlockPos p_220071_3_, ISelectionContext p_220071_4_) {
        return this.ourCollisionShapeByIndex.get(p_220071_1_);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext ctx) {
    	BlockState bs = super.getStateForPlacement(ctx);
    	if ((bs != null) && (COND != null)) {
    		bs = bs.setValue(COND, def.getMatchingCondition(ctx.getLevel(), ctx.getClickedPos())); 
    	}
    	return bs;
    }
    @Override
    public void registerDynmapRenderData(ModTextureDefinition mtd) {
        ModModelDefinition md = mtd.getModelDefinition();
        String blkname = def.getBlockName();
        def.defaultRegisterTextures(mtd);
        def.registerPatchTextureBlock(mtd, 3);
        // Get plant model, and set for all defined meta
        WallFenceBlockModel pbm = md.addWallFenceModel(blkname, WallFenceBlockModel.FenceType.WALL);
    }
    private static String[] TAGS = { "walls" };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }    

}
