package com.westeroscraft.westerosblocks.blocks;

import com.westeroscraft.westerosblocks.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;

import javax.annotation.Nullable;

public class WCSolidBlock extends Block implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
        	BlockBehaviour.Properties props = def.makeProperties();
            String t = def.getType();
			boolean doConnectstate = false;
			if (t != null) {
				String[] toks = t.split(",");
				for (String tok : toks) {
					String[] parts = tok.split(":");
					if (parts[0].equals("connectstate")) {
						doConnectstate = true;
						tempCONNECTSTATE = CONNECTSTATE;
					}
				}
			}
        	return def.registerRenderType(def.registerBlock(new WCSolidBlock(props, def, doConnectstate)), true, def.nonOpaque);
        }
    }    
    protected WesterosBlockDef def;
    protected VoxelShape collisionbox;
    protected VoxelShape supportbox;

	public static final IntegerProperty CONNECTSTATE = IntegerProperty.create("connectstate", 0, 3);
	protected static IntegerProperty tempCONNECTSTATE;
    public final boolean connectstate;
    
    protected WCSolidBlock(BlockBehaviour.Properties props, WesterosBlockDef def, boolean doConnectstate) {
        super(props);
        this.def = def;
        collisionbox = def.makeCollisionBoxShape();
        if (def.supportBoxes == null) {
        	supportbox = collisionbox;
        }
        else {
        	supportbox = def.makeSupportBoxShape(null);
        }
        
        connectstate = doConnectstate;
		BlockState defbs = this.stateDefinition.any();
		if (connectstate) {
			defbs = defbs.setValue(CONNECTSTATE, 0);
		}
		this.registerDefaultState(defbs);
    }

    protected WCSolidBlock(BlockBehaviour.Properties props, WesterosBlockDef def) {
        this(props, def, false);
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
        return supportbox;
    }
    @Override
    public VoxelShape getVisualShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext ctx) {
    	if (def.nonOpaque)
    		return Shapes.empty();
    	else
    		return collisionbox;
    }
    
    @SuppressWarnings("deprecation")
	public boolean skipRendering(BlockState state, BlockState other_state, Direction direction) {
    	if (def.nonOpaque)
    		return other_state.is(this) ? true : super.skipRendering(state, other_state, direction);
    	else
    		return false;
    }


     public float getShadeBrightness(BlockState p_48731_, BlockGetter p_48732_, BlockPos p_48733_) {
        return (def.lightOpacity == 0) ? 1.0F : 0.2F;
     }

     public boolean propagatesSkylightDown(BlockState p_48740_, BlockGetter p_48741_, BlockPos p_48742_) {
    	 return (def.lightOpacity == 0);
     }

    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> StateDefinition) {
        if (tempCONNECTSTATE != null) {
            StateDefinition.add(tempCONNECTSTATE);
            tempCONNECTSTATE = null;
        }
    	super.createBlockStateDefinition(StateDefinition);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
    	BlockState bs = super.getStateForPlacement(ctx);
    	return bs;
    }

    private static String[] TAGS = { };
    @Override
    public String[] getBlockTags() {
    	return TAGS;
    }    

}
