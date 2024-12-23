package com.westerosblocks.block.custom;

import com.westerosblocks.block.ModBlocks;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.WesterosBlockFactory;
import com.westerosblocks.block.WesterosBlockLifecycle;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.MathHelper;

public class WCCuboid16WayBlock extends WCCuboidBlock implements WesterosBlockLifecycle {
    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
        	def.nonOpaque = true;
            AbstractBlock.Settings settings = def.makeBlockSettings();
        	// See if we have a state property
        	WesterosBlockDef.StateProperty state = def.buildStateProperty();
        	if (state != null) {
        		tempSTATE = state;
        	}
            Block blk = new WCCuboid16WayBlock(settings, def);
            return def.registerRenderType(ModBlocks.registerBlock(def.blockName, blk), true, true);
        }
    }

    private static final int ROTATIONS = 16;
    public static final IntProperty ROTATION = Properties.ROTATION;
    
    private static final WesterosBlockDef.CuboidRotation[] shape_rotations = { 
		WesterosBlockDef.CuboidRotation.NONE,
		WesterosBlockDef.CuboidRotation.ROTY90,
		WesterosBlockDef.CuboidRotation.ROTY180,
		WesterosBlockDef.CuboidRotation.ROTY270 };
    protected WCCuboid16WayBlock(AbstractBlock.Settings settings, WesterosBlockDef def) {
        super(settings, def, ROTATIONS);
        
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
        BlockState defbs = getDefaultState()
                .with(ROTATION, 0)
                .with(WATERLOGGED, Boolean.FALSE);
        if (STATE != null) {
        	defbs = defbs.with(STATE, STATE.defValue);
        }
        setDefaultState(defbs);
    }
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(ROTATION);
    }
    
    @Override
    protected int getIndexFromState(BlockState state) {
    	if (STATE != null) {
    		return (STATE.getIndex(state.get(STATE)) * ROTATIONS) + state.get(ROTATION);
    	}
    	else {
    		return state.get(ROTATION);
    	}
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rot) {
        return state.with(ROTATION, rot.rotate(state.get(ROTATION), 16));
    }

    @Override
    protected BlockState mirror(BlockState blockState, BlockMirror mirror) {
        return blockState.with(ROTATION, mirror.mirror(blockState.get(ROTATION), 16));
     }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
       FluidState fluidstate = ctx.getWorld().getFluidState(ctx.getBlockPos());
       Integer dir = MathHelper.floor((double) (ctx.getPlayerYaw() * 16.0F / 360.0F) + 0.5D) & 15;
       BlockState bs = getDefaultState()
               .with(ROTATION, dir)
               .with(WATERLOGGED, fluidstate.isIn(FluidTags.WATER));
       if (STATE != null) {
    	   bs = bs.with(STATE, STATE.defValue);
       }
       return bs;
    }
}
