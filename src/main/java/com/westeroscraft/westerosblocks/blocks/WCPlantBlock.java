package com.westeroscraft.westerosblocks.blocks;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.IPlantable;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.tags.FluidTags;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;

import javax.annotation.Nullable;

public class WCPlantBlock extends Block implements WesterosBlockLifecycle, IPlantable {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
        	BlockBehaviour.Properties props = def.makeProperties().noCollission().instabreak();
        	// See if we have a state property
        	WesterosBlockDef.StateProperty state = def.buildStateProperty();
        	if (state != null) {
        		tempSTATE = state;
        	}        	
            String t = def.getType();
            if ((t != null) && (t.indexOf(WesterosBlockDef.LAYER_SENSITIVE) >= 0)) {
            	tempLAYERS = BlockStateProperties.LAYERS;
            }
        	return def.registerRenderType(def.registerBlock(new WCPlantBlock(props, def)), false, false);
        }
    }    
    // Support waterlogged on these blocks
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private WesterosBlockDef def;
    protected static WesterosBlockDef.StateProperty tempSTATE;
    protected static IntegerProperty tempLAYERS;
    protected WesterosBlockDef.StateProperty STATE;
    protected IntegerProperty LAYERS;
    protected boolean toggleOnUse = false;
    public boolean layerSensitive = false;

    public static final VoxelShape[] SHAPE_BY_LAYER = new VoxelShape[]{Shapes.empty(), Block.box(0.0D, -14.0D, 0.0D, 16.0D, 2.0D, 16.0D), Block.box(0.0D, -12.0D, 0.0D, 16.0D, 4.0D, 16.0D), Block.box(0.0D, -10.0D, 0.0D, 16.0D, 6.0D, 16.0D), Block.box(0.0D, -8.0D, 0.0D, 16.0D, 8.0D, 16.0D), Block.box(0.0D, -6.0D, 0.0D, 16.0D, 10.0D, 16.0D), Block.box(0.0D, -4.0D, 0.0D, 16.0D, 12.0D, 16.0D), Block.box(0.0D, -2.0D, 0.0D, 16.0D, 14.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)};

    protected WCPlantBlock(BlockBehaviour.Properties props, WesterosBlockDef def) {
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
        BlockState defbs = this.stateDefinition.any().setValue(WATERLOGGED, Boolean.valueOf(false));
        if (STATE != null) {
        	defbs = defbs.setValue(STATE, STATE.defValue);
        }
    	if (LAYERS != null) {        		
    		defbs = defbs.setValue(LAYERS, 8);
    	}
		this.registerDefaultState(defbs);
    }
    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }
    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
    	BlockState bs = super.getStateForPlacement(ctx);
    	if (bs == null) return null;
        FluidState fluidstate = ctx.getLevel().getFluidState(ctx.getClickedPos());
        bs = bs.setValue(WATERLOGGED, Boolean.valueOf(fluidstate.is(FluidTags.WATER)));
        if (STATE != null) {
        	bs = bs.setValue(STATE, STATE.defValue);
        }
        if (LAYERS != null) {
        	BlockState below = ctx.getLevel().getBlockState(ctx.getClickedPos().relative(Direction.DOWN));
        	if ((below != null) && (below.hasProperty(BlockStateProperties.LAYERS))) {
        		Block blk = below.getBlock();
        		Integer layer = below.getValue(BlockStateProperties.LAYERS);
        		// See if soft layer
        		if ((blk instanceof SnowLayerBlock) || ((blk instanceof WCLayerBlock) && ((WCLayerBlock)blk).softLayer)) {
        			layer = (layer > 2) ? Integer.valueOf(layer - 2) : Integer.valueOf(1);
        		}
        		bs = bs.setValue(LAYERS, layer);
        	}
        	else if ((below != null) && (below.getBlock() instanceof SlabBlock)) {
        		SlabType slabtype = below.getValue(BlockStateProperties.SLAB_TYPE);
        		if (slabtype == SlabType.BOTTOM) bs = bs.setValue(LAYERS, 4);
        	}
        }
    	return bs;
    }

    @SuppressWarnings("deprecation")
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
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinition) {
    	if (tempSTATE != null) {
    		STATE = tempSTATE;
    		tempSTATE = null;
    	}
    	if (tempLAYERS != null) {
    		LAYERS = tempLAYERS;
    		tempLAYERS = null;
    	}
    	if (STATE != null) {
	       stateDefinition.add(STATE);
    	}
    	if (LAYERS != null) {
    		stateDefinition.add(LAYERS);
    	}
    	stateDefinition.add(WATERLOGGED);
    }
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitrslt) {
        if (this.toggleOnUse && (this.STATE != null) && player.isCreative()) {
            state = state.cycle(this.STATE);
            level.setBlock(pos, state, 10);
            level.levelEvent(player, 1006, pos, 0);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        else {
			return InteractionResult.PASS;
        }
	}

	@Override
	public BlockState getPlant(BlockGetter world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);
      	if (state.getBlock() != this) return defaultBlockState();
      	return state;
	}
	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
		return state.getFluidState().isEmpty();
    }
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
		if (LAYERS != null) {
			return SHAPE_BY_LAYER[state.getValue(LAYERS)];
		}
		else {
			return Shapes.block();
		}
	}

    private static String[] TAGS = { "flowers" };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }    
	
}
