package com.westeroscraft.westerosblocks.blocks;

import java.util.ArrayList; 
import java.util.List;

import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;

import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.LevelAccessor;
import javax.annotation.Nullable;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.core.Direction;

// Cuboid with horizontal connections - adds four boolean states for north, south, east, west (boolean)
public class WCCuboidHorizBlock extends WCCuboidBlock implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
        	BlockBehaviour.Properties props = def.makeProperties();
        	return def.registerRenderType(def.registerBlock(new WCCuboidHorizBlock(props, def)), true, def.nonOpaque);
        }
    }    
    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;

    protected List<WesterosBlockDef.Cuboid> cuboid_by_connect[] = new List[16];

    public static final int NORTH_BITS = 1;
    public static final int EAST_BITS = 2;
    public static final int SOUTH_BITS = 4;
    public static final int WEST_BITS = 8;

    // Model index order: 
    //  0=none
    //  1=one connect north
    //  2=two connect north+east
    //  3=two connect north+south 
    //  4=three connect north, east, south
    //  5=four connects
    public static class ModelToConnect {
    	public WesterosBlockDef.CuboidRotation rot;
    	public int modelIdx;
    	ModelToConnect(WesterosBlockDef.CuboidRotation r, int i) { rot = r; modelIdx = i; }
    };
    
    public static ModelToConnect modelMap[] = {
		new ModelToConnect(WesterosBlockDef.CuboidRotation.NONE, 0), // None (----)
		new ModelToConnect(WesterosBlockDef.CuboidRotation.NONE, 1), // North (---N)
		new ModelToConnect(WesterosBlockDef.CuboidRotation.ROTY90, 1), // East (--E-)
		new ModelToConnect(WesterosBlockDef.CuboidRotation.NONE, 2), // North+East (--EN)
		new ModelToConnect(WesterosBlockDef.CuboidRotation.ROTY180, 1), // South (-S--)
		new ModelToConnect(WesterosBlockDef.CuboidRotation.NONE, 3), // North+South (-S-N)
		new ModelToConnect(WesterosBlockDef.CuboidRotation.ROTY90, 2), // East+South (-SE-)
		new ModelToConnect(WesterosBlockDef.CuboidRotation.NONE, 4), // North+East+South (-SEN)
		new ModelToConnect(WesterosBlockDef.CuboidRotation.ROTY270, 1), // West (W---)
		new ModelToConnect(WesterosBlockDef.CuboidRotation.ROTY270, 2), // North+West (W--N)
		new ModelToConnect(WesterosBlockDef.CuboidRotation.ROTY270, 3), // East+West (W-E-)
		new ModelToConnect(WesterosBlockDef.CuboidRotation.ROTY270, 4), // North+East+West (W-EN)
		new ModelToConnect(WesterosBlockDef.CuboidRotation.ROTY180, 2),	// South+West (WS--)
		new ModelToConnect(WesterosBlockDef.CuboidRotation.ROTY180, 4),	// North+South+West (WS-N)
		new ModelToConnect(WesterosBlockDef.CuboidRotation.ROTY90, 4),	// East+South+West (WSE-)
		new ModelToConnect(WesterosBlockDef.CuboidRotation.NONE, 5)	// North+South+East+West (WSEN)
    };
    
    protected WCCuboidHorizBlock(BlockBehaviour.Properties props, WesterosBlockDef def) {
        super(props, def);

        // Build rotated models
        for (int i = 0; i < 16; i++) {
        	List<WesterosBlockDef.Cuboid> mod = def.getCuboidList(modelMap[i].modelIdx);	// Get base cuboid model
        	if (modelMap[i].rot == WesterosBlockDef.CuboidRotation.NONE) {
        		cuboid_by_connect[i] = mod;
        	}
        	else {
        		cuboid_by_connect[i] = new ArrayList<WesterosBlockDef.Cuboid>();
                for (WesterosBlockDef.Cuboid c : mod) {
                    cuboid_by_connect[i].add(c.rotateCuboid(modelMap[i].rot));
                }
        	}
        }
        this.SHAPE_BY_INDEX = new VoxelShape[cuboid_by_connect.length];
        for (int i = 0; i < cuboid_by_connect.length; i++) {
            SHAPE_BY_INDEX[i] = getBoundingBoxFromCuboidList(cuboid_by_connect[i]);
        }
        this.registerDefaultState(this.stateDefinition.any().setValue(NORTH, Boolean.valueOf(false))
        		.setValue(SOUTH, Boolean.valueOf(false)).setValue(EAST, Boolean.valueOf(false))
        		.setValue(WEST, Boolean.valueOf(false)).setValue(WATERLOGGED, Boolean.valueOf(false)));
    }
    
    @Override
    protected int getIndexFromState(BlockState state) {
    	return (state.getValue(NORTH) ? NORTH_BITS : 0) |
    			(state.getValue(EAST) ? EAST_BITS : 0) |
    			(state.getValue(SOUTH) ? SOUTH_BITS : 0) |
    			(state.getValue(WEST) ? WEST_BITS : 0);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> container) {
    	super.createBlockStateDefinition(container);
    	container.add(NORTH, SOUTH, EAST, WEST);
    }
    
    private BlockState updateStateHorizontal(BlockState bs, BlockGetter reader, BlockPos pos) {
    	BlockState bsneighbor = reader.getBlockState(pos.north());
    	Boolean north = Boolean.valueOf(def.isConnectMatch(bs, bsneighbor));
    	bsneighbor = reader.getBlockState(pos.south());
    	Boolean south = Boolean.valueOf(def.isConnectMatch(bs, bsneighbor));
    	bsneighbor = reader.getBlockState(pos.east());
    	Boolean east = Boolean.valueOf(def.isConnectMatch(bs, bsneighbor));
    	bsneighbor = reader.getBlockState(pos.west());
    	Boolean west = Boolean.valueOf(def.isConnectMatch(bs, bsneighbor));
    	return bs.setValue(NORTH, north).setValue(SOUTH, south).setValue(EAST, east).setValue(WEST, west);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction dir, BlockState state2, LevelAccessor world, BlockPos pos, BlockPos pos2) {
    	state = super.updateShape(state, dir, state2, world, pos, pos2);
    	if (state != null) {
    		state = updateStateHorizontal(state, world, pos);
    	}
    	return state;
    }

    @Nullable 
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
    	BlockState bs = super.getStateForPlacement(ctx);
    	if (bs != null) {
    		bs = updateStateHorizontal(this.defaultBlockState(), ctx.getLevel(), ctx.getClickedPos());
    	}
    	return bs;
    }
}
