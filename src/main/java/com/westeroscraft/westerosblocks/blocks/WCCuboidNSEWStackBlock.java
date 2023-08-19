package com.westeroscraft.westerosblocks.blocks;

import java.util.ArrayList;

import javax.annotation.Nullable;

import com.westeroscraft.westerosblocks.*;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.tags.FluidTags;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.StateDefinition;

import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class WCCuboidNSEWStackBlock extends WCCuboidBlock implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
        	def.nonOpaque = true;
        	BlockBehaviour.Properties props = def.makeProperties();
        	return def.registerRenderType(def.registerBlock(new WCCuboidNSEWStackBlock(props, def)), false, false);
        }
    }
    // Support waterlogged on these blocks
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.NORTH);
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    
    // Index = FACING + 4*TOP

    protected WCCuboidNSEWStackBlock(BlockBehaviour.Properties props, WesterosBlockDef def) {
        super(props, def, 8);
        for (int i = 0; i < 2; i++) {
        	WesterosBlockStateRecord se = def.getStackElementByIndex(i);
            for (WesterosBlockDef.Cuboid c : se.cuboids) {
    			cuboid_by_facing[4*i] = new ArrayList<WesterosBlockDef.Cuboid>();	// Use clean, since parent uses cuboid not stack
                cuboid_by_facing[4*i].add(c);
                cuboid_by_facing[4*i + 1].add(c.rotateCuboid(WesterosBlockDef.CuboidRotation.ROTY90));
                cuboid_by_facing[4*i + 2].add(c.rotateCuboid(WesterosBlockDef.CuboidRotation.ROTY180));
                cuboid_by_facing[4*i + 3].add(c.rotateCuboid(WesterosBlockDef.CuboidRotation.ROTY270));
            }        	
        }
        for (int j = 0; j < cuboid_by_facing.length; j++) {
            SHAPE_BY_INDEX[j] = getBoundingBoxFromCuboidList(cuboid_by_facing[j]);
        }
        this.registerDefaultState(this.stateDefinition.any()
        		.setValue(HALF, DoubleBlockHalf.LOWER)
        		.setValue(FACING, Direction.EAST)
        		.setValue(WATERLOGGED, Boolean.valueOf(false)));
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> StateDefinition) {
        super.createBlockStateDefinition(StateDefinition);
    	StateDefinition.add(FACING, HALF);
    }
    @Override
    protected int getIndexFromState(BlockState state) {
    	int topoff = (state.getValue(HALF) == DoubleBlockHalf.LOWER) ? 0 : 4;
    	switch (state.getValue(FACING)) {
	    	case EAST:
	    	default:
	    		return topoff;
	    	case SOUTH:
	    		return topoff+1;
	    	case WEST:
	    		return topoff+2;
	    	case NORTH:
	    		return topoff+3;
    	}
    }    
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
       BlockPos blockpos = ctx.getClickedPos();
       if (blockpos.getY() < 255 && ctx.getLevel().getBlockState(blockpos.above()).canBeReplaced(ctx)) {
           FluidState fluidstate = ctx.getLevel().getFluidState(ctx.getClickedPos());
           Direction[] adirection = ctx.getNearestLookingDirections();
           Direction dir = Direction.EAST;	// Default
           for (Direction d : adirection) {
           	if (d == Direction.EAST || d == Direction.WEST || d == Direction.NORTH || d == Direction.SOUTH) {
       			dir = d;
       			break;
           	}
           }
           return this.defaultBlockState()
       		.setValue(FACING, dir)
       		.setValue(HALF, DoubleBlockHalf.LOWER)
       		.setValue(WATERLOGGED, Boolean.valueOf(fluidstate.is(FluidTags.WATER)));
       }
       else {
    	   return null;
       }
    }

    @Override
    public BlockState updateShape(BlockState state, Direction dir, BlockState state2, LevelAccessor world, BlockPos pos, BlockPos pos2) {
        DoubleBlockHalf doubleblockhalf = state.getValue(HALF);
        if (dir.getAxis() != Direction.Axis.Y || doubleblockhalf == DoubleBlockHalf.LOWER != (dir == Direction.UP) || state2.is(this) && state2.getValue(HALF) != doubleblockhalf) {
           return doubleblockhalf == DoubleBlockHalf.LOWER && dir == Direction.DOWN && !state.canSurvive(world, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, dir, state2, world, pos, pos2);
        } else {
           return Blocks.AIR.defaultBlockState();
        }
     }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack item) {
    	BlockPos above = pos.above();
        FluidState fluidstate =world.getFluidState(above);
        BlockState newstate = this.defaultBlockState()
			.setValue(FACING, state.getValue(FACING))
			.setValue(HALF, DoubleBlockHalf.UPPER)
			.setValue(WATERLOGGED, Boolean.valueOf(fluidstate.is(FluidTags.WATER)));
        world.setBlock(pos.above(), newstate, 3);
    }

    @SuppressWarnings("deprecation")
	@Override
    public boolean canSurvive(BlockState state, LevelReader reader, BlockPos pos) {
        if (state.getValue(HALF) != DoubleBlockHalf.UPPER) {
           return super.canSurvive(state, reader, pos);
        }
        else {
           BlockState blockstate = reader.getBlockState(pos.below());
           if (state.getBlock() != this) {
        	   return super.canSurvive(state, reader, pos); 
           }
           return blockstate.is(this) && blockstate.getValue(HALF) == DoubleBlockHalf.LOWER;
        }
     }
}
