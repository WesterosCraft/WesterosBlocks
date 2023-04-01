package com.westeroscraft.westerosblocks.blocks;

import java.util.List;

import javax.annotation.Nullable;

import com.westeroscraft.westerosblocks.*;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.tags.FluidTags;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;

public class WCCuboidBlock extends Block implements WesterosBlockLifecycle, SimpleWaterloggedBlock {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
        	def.nonOpaque = true;
        	BlockBehaviour.Properties props = def.makeProperties();
        	// See if we have a state property
        	WesterosBlockDef.StateProperty state = def.buildStateProperty();
        	if (state != null) {
        		tempSTATE = state;
        	}        	
        	return def.registerRenderType(def.registerBlock(new WCCuboidBlock(props, def)), false, false);
        }
    }
    // Support waterlogged on these blocks
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    
    protected static WesterosBlockDef.StateProperty tempSTATE;
    protected WesterosBlockDef.StateProperty STATE;
    protected boolean toggleOnUse = false;

    protected WesterosBlockDef def;
    
    protected VoxelShape[] SHAPE_BY_INDEX;

    protected WCCuboidBlock(BlockBehaviour.Properties props, WesterosBlockDef def) {
        super(props);
        this.def = def;
        
        String t = def.getType();
        if (t != null) {
            String[] toks = t.split(",");
            for (String tok : toks) {
                if (tok.equals("toggleOnUse")) {
                    toggleOnUse = true;
                }
            }
        }
        int cnt = def.states.size();
    	SHAPE_BY_INDEX = new VoxelShape[cnt];
    	for (int i = 0; i < cnt; i++) {
            SHAPE_BY_INDEX[i] = getBoundingBoxFromCuboidList(def.states.get(i).getCuboidList());
    	}
        if (STATE != null) {
            this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, Boolean.valueOf(false)).setValue(STATE, STATE.defValue));
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
    	if (tempSTATE != null) {
    		STATE = tempSTATE;
    		tempSTATE = null;
    	}
    	if (STATE != null) {
	       StateDefinition.add(STATE);
    	}
       StateDefinition.add(WATERLOGGED);
    }

    protected int getIndexFromState(BlockState state) {
    	if (STATE != null)
    		return STATE.getIndex(state.getValue(STATE));
    	else
    		return 0;
    }
    
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext ctx) {
        return SHAPE_BY_INDEX[getIndexFromState(state)];
    }
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext ctx) {
        return SHAPE_BY_INDEX[getIndexFromState(state)];
    }
    @Override
    public VoxelShape getBlockSupportShape(BlockState state, BlockGetter reader, BlockPos pos) {
        return SHAPE_BY_INDEX[getIndexFromState(state)];
    }
    @Override
    public VoxelShape getVisualShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext ctx) {
        return SHAPE_BY_INDEX[getIndexFromState(state)];
    }
    @Override
    public BlockState updateShape(BlockState state, Direction face, BlockState state2, LevelAccessor world, BlockPos pos, BlockPos pos2) {
       if (state.getValue(WATERLOGGED)) {
          world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
       }
       return super.updateShape(state, face, state2, world, pos, pos2);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
       FluidState fluidstate = ctx.getLevel().getFluidState(ctx.getClickedPos());
       BlockState bs = this.defaultBlockState().setValue(WATERLOGGED, Boolean.valueOf(fluidstate.is(FluidTags.WATER)));
       if (STATE != null) {
    	   bs = bs.setValue(STATE, STATE.defValue); 
       }
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
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitrslt) {
    	WesterosBlocks.log.info("usee=" + def.blockName);
        if (this.toggleOnUse && (this.STATE != null)) {
        	WesterosBlocks.log.info("toggleState=" + state.getValue(this.STATE));
            state = state.cycle(this.STATE);
        	WesterosBlocks.log.info("toggleState(after)=" + state.getValue(this.STATE));
            level.setBlock(pos, state, 10);
            level.levelEvent(player, 1006, pos, 0);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        else {
			return InteractionResult.PASS;
        }
	}

    protected VoxelShape getBoundingBoxFromCuboidList(List<WesterosBlockDef.Cuboid> cl) {
        VoxelShape vs = Shapes.empty();
        if (cl != null) {
            for(WesterosBlockDef.Cuboid c : cl) {
            	vs = Shapes.or(vs, Shapes.box(c.xMin, c.yMin, c.zMin, c.xMax, c.yMax, c.zMax));
            }
        }
        return vs;
    }
    public List<WesterosBlockDef.Cuboid> getModelCuboids(int stateIdx) {
    	return def.states.get(stateIdx).cuboids;
    }

    private static String[] TAGS = { };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }    

}
