package com.westeroscraft.westerosblocks.blocks;

import java.util.ArrayList; 
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.tags.FluidTags;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.VoxelShape;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockLifecycle;
import com.westeroscraft.westerosblocks.WesterosBlockFactory;

public class WCCuboidNEBlock extends WCCuboidBlock implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
        	def.nonOpaque = true;
        	BlockBehaviour.Properties props = def.makeProperties();
        	return def.registerRenderType(def.registerBlock(new WCCuboidNEBlock(props, def)), false, false);
        }
    }
    
    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.EAST, Direction.NORTH);

    protected List<WesterosBlockDef.Cuboid> cuboid_by_facing[] = new List[2];
    
    @SuppressWarnings("unchecked")
    protected WCCuboidNEBlock(BlockBehaviour.Properties props, WesterosBlockDef def) {
        super(props, def);
        
        cuboid_by_facing[0] = def.getCuboidList();	// East facing
        cuboid_by_facing[1] = new ArrayList<WesterosBlockDef.Cuboid>();
        for (WesterosBlockDef.Cuboid c : cuboid_by_facing[0]) {
            cuboid_by_facing[1].add(c.rotateCuboid(WesterosBlockDef.CuboidRotation.ROTY90));
        }
        this.SHAPE_BY_INDEX = new VoxelShape[cuboid_by_facing.length];
        for (int i = 0; i < cuboid_by_facing.length; i++) {
            SHAPE_BY_INDEX[i] = getBoundingBoxFromCuboidList(cuboid_by_facing[i]);
        }
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.EAST));
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> StateDefinition) {
       StateDefinition.add(FACING, WATERLOGGED);
    }
    
    @Override
    protected int getIndexFromState(BlockState state) {
    	return (state.getValue(FACING) == Direction.EAST) ? 0 : 1;
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        switch(rot) {
        	case CLOCKWISE_180:
            default:
                return state;
            case COUNTERCLOCKWISE_90:
            case CLOCKWISE_90:
            	return (state.getValue(FACING) == Direction.EAST) ?
            			state.setValue(FACING, Direction.NORTH) :
        				state.setValue(FACING, Direction.EAST);
        }
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
       FluidState fluidstate = ctx.getLevel().getFluidState(ctx.getClickedPos());
       Direction[] adirection = ctx.getNearestLookingDirections();
       Direction dir = Direction.EAST;	// Default
       for (Direction d : adirection) {
    	   if (d == Direction.EAST || d == Direction.WEST) {
    		   dir = Direction.EAST;
    		   break;
    	   }
    	   if (d == Direction.NORTH || d == Direction.SOUTH) {
    		   dir = Direction.NORTH;
    		   break;
    	   }
       }
       return this.defaultBlockState().setValue(FACING, dir).setValue(WATERLOGGED, Boolean.valueOf(fluidstate.is(FluidTags.WATER)));
    }
}
