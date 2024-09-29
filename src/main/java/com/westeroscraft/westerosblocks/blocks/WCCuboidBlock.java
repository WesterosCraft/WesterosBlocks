package com.westeroscraft.westerosblocks.blocks;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.westeroscraft.westerosblocks.*;

import net.minecraft.resources.ResourceLocation;
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
import net.neoforged.neoforge.registries.RegisterEvent;

public class WCCuboidBlock extends Block implements WesterosBlockLifecycle, SimpleWaterloggedBlock {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def, RegisterEvent.RegisterHelper<Block> helper) {
        	def.nonOpaque = true;
        	BlockBehaviour.Properties props = def.makeProperties();
        	// See if we have a state property
        	WesterosBlockDef.StateProperty state = def.buildStateProperty();
        	if (state != null) {
        		tempSTATE = state;
        	}
            Block blk = new WCCuboidBlock(props, def, 1);
            helper.register(ResourceLocation.fromNamespaceAndPath(WesterosBlocks.MOD_ID, def.blockName), blk);
            def.registerBlockItem(def.blockName, blk);
            return def.registerRenderType(blk, false, false);
        }

    }
    // Support waterlogged on these blocks
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    
    protected static WesterosBlockDef.StateProperty tempSTATE;
    protected WesterosBlockDef.StateProperty STATE;
    protected boolean toggleOnUse = false;
    protected int modelsPerState;

    protected WesterosBlockDef def;
    
    protected VoxelShape[] SHAPE_BY_INDEX;
    protected VoxelShape[] SUPPORT_BY_INDEX;
    protected List<WesterosBlockDef.Cuboid> cuboid_by_facing[];

    protected WCCuboidBlock(BlockBehaviour.Properties props, WesterosBlockDef def, int modelsPerState) {
        super(props);
        this.def = def;
        this.modelsPerState = modelsPerState;
        
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
        this.cuboid_by_facing = new List[cnt * modelsPerState];
    	SHAPE_BY_INDEX = new VoxelShape[cnt * modelsPerState];
    	for (int i = 0; i < cnt; i++) {
    		cuboid_by_facing[i * modelsPerState] = def.states.get(i).getCuboidList();
    		for (int j = 1; j < modelsPerState; j++) {
    			cuboid_by_facing[i * modelsPerState + j] = new ArrayList<WesterosBlockDef.Cuboid>();
    		}
            SHAPE_BY_INDEX[i * modelsPerState] = getBoundingBoxFromCuboidList(cuboid_by_facing[i * modelsPerState]);
    	}
    	SUPPORT_BY_INDEX = new VoxelShape[cnt];
    	for (int i = 0; i < cnt; i++) {
            SUPPORT_BY_INDEX[i] = def.states.get(i).makeSupportBoxShape(null);
    	}
    	BlockState defbs = this.stateDefinition.any().setValue(WATERLOGGED, Boolean.valueOf(false));
        if (STATE != null) {
        	defbs = defbs.setValue(STATE, STATE.defValue);
        }
        this.registerDefaultState(defbs);        	
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
    		return modelsPerState * STATE.getIndex(state.getValue(STATE));
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
    	int idx = 0;
    	if (STATE != null)
    		idx = STATE.getIndex(state.getValue(STATE)); 
        return SUPPORT_BY_INDEX[idx];
    }
    @Override
    public VoxelShape getVisualShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext ctx) {
        return SHAPE_BY_INDEX[getIndexFromState(state)];
    }
    @SuppressWarnings("deprecation")
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
    @SuppressWarnings("deprecation")
	@Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        switch(pathComputationType) {
            case LAND:
                return false;
            case WATER:
                return state.getFluidState().is(FluidTags.WATER);
            case AIR:
                return false;
            default:
                return false;
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (this.toggleOnUse && (this.STATE != null) && player.isCreative() && player.getMainHandItem().isEmpty()) {
            state = state.cycle(this.STATE);
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
    	return cuboid_by_facing[modelsPerState * stateIdx];
    }

    private static String[] TAGS = { };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }    

}
