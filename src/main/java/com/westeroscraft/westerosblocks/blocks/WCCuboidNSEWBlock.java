package com.westeroscraft.westerosblocks.blocks;

import java.util.ArrayList; 
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
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

public class WCCuboidNSEWBlock extends WCCuboidBlock implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
        	def.nonOpaque = true;
        	BlockBehaviour.Properties props = def.makeProperties();
        	return def.registerRenderType(def.registerBlock(new WCCuboidNSEWBlock(props, def)), false, false);
        }
    }
    
    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.NORTH);

    protected List<WesterosBlockDef.Cuboid> cuboid_by_facing[] = new List[4];
    
    @SuppressWarnings("unchecked")
    protected WCCuboidNSEWBlock(BlockBehaviour.Properties props, WesterosBlockDef def) {
        super(props, def);
        
        cuboid_by_facing[0] = def.getCuboidList();	// East facing
        cuboid_by_facing[1] = new ArrayList<WesterosBlockDef.Cuboid>();
        cuboid_by_facing[2] = new ArrayList<WesterosBlockDef.Cuboid>();
        cuboid_by_facing[3] = new ArrayList<WesterosBlockDef.Cuboid>();
        for (WesterosBlockDef.Cuboid c : cuboid_by_facing[0]) {
            cuboid_by_facing[1].add(c.rotateCuboid(WesterosBlockDef.CuboidRotation.ROTY90));
            cuboid_by_facing[2].add(c.rotateCuboid(WesterosBlockDef.CuboidRotation.ROTY180));
            cuboid_by_facing[3].add(c.rotateCuboid(WesterosBlockDef.CuboidRotation.ROTY270));
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
    	switch (state.getValue(FACING)) {
    	case EAST:
    		return 0;
    	case SOUTH:
    		return 1;
    	case WEST:
    		return 2;
    	case NORTH:
    		return 3;
    	}
    	return 0;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
       FluidState fluidstate = ctx.getLevel().getFluidState(ctx.getClickedPos());
       Direction[] adirection = ctx.getNearestLookingDirections();
       Direction dir = Direction.EAST;	// Default
       for (Direction d : adirection) {
    	   if (d == Direction.EAST || d == Direction.WEST || d == Direction.NORTH || d == Direction.SOUTH) {
    		   dir = d;
    		   break;
    	   }
       }
       return this.defaultBlockState().setValue(FACING, dir).setValue(WATERLOGGED, Boolean.valueOf(fluidstate.is(FluidTags.WATER)));
    }
}
