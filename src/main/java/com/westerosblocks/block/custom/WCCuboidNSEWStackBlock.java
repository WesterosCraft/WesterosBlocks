package com.westerosblocks.block.custom;


import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.WesterosBlockFactory;
import com.westerosblocks.block.WesterosBlockLifecycle;
import com.westerosblocks.block.WesterosBlockStateRecord;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;

public class WCCuboidNSEWStackBlock extends WCCuboidBlock implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
        	def.nonOpaque = true;
            AbstractBlock.Settings settings = def.makeProperties();
            Block blk = new WCCuboidNSEWStackBlock(settings, def);
            return def.registerRenderType(blk, false, false);
        }
    }
    // Support waterlogged on these blocks
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.NORTH);
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    
    public final boolean allowHalfBreak;
    // Index = FACING + 4*TOP

    protected WCCuboidNSEWStackBlock(AbstractBlock.Settings settings, WesterosBlockDef def) {
        super(settings, def, 8);
        String t = def.getType();
        boolean brk = false;
        if (t != null) {
            String[] toks = t.split(",");
            for (String tok : toks) {
                if (tok.equals("allowHalfBreak")) {
                	brk = true;
                }
            }
        }
        this.allowHalfBreak = brk;
        
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
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
    	builder.add(FACING, HALF);
    }

    @Override
    protected int getIndexFromState(BlockState state) {
    	int topoff = (state.get(HALF) == DoubleBlockHalf.LOWER) ? 0 : 4;
    	switch (state.get(FACING)) {
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
       if (blockpos.getY() < ctx.getLevel().getMaxBuildHeight() && ctx.getLevel().getBlockState(blockpos.above()).canBeReplaced(ctx)) {
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
    	if (allowHalfBreak) { return super.updateShape(state, dir, state2, world, pos, pos2); }
        DoubleBlockHalf doubleblockhalf = state.get(HALF);
        if (dir.getAxis() != Direction.Axis.Y || doubleblockhalf == DoubleBlockHalf.LOWER != (dir == Direction.UP) || state2.is(this) && state2.get(HALF) != doubleblockhalf) {
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
			.setValue(FACING, state.get(FACING))
			.setValue(HALF, DoubleBlockHalf.UPPER)
			.setValue(WATERLOGGED, Boolean.valueOf(fluidstate.is(FluidTags.WATER)));
        world.setBlock(pos.above(), newstate, 3);
    }

    @SuppressWarnings("deprecation")
	@Override
    public boolean canSurvive(BlockState state, LevelReader reader, BlockPos pos) {
        if (this.allowHalfBreak || (state.get(HALF) != DoubleBlockHalf.UPPER)) {
           return super.canSurvive(state, reader, pos);
        }
        else {
           BlockState blockstate = reader.getBlockState(pos.below());
           if (state.getBlock() != this) {
        	   return super.canSurvive(state, reader, pos); 
           }
           return blockstate.is(this) && blockstate.get(HALF) == DoubleBlockHalf.LOWER;
        }
     }
}
