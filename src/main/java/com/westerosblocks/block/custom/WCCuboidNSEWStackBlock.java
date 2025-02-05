package com.westerosblocks.block.custom;

import com.westerosblocks.block.*;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import java.util.ArrayList;

public class WCCuboidNSEWStackBlock extends WCCuboidBlock implements ModBlockLifecycle {

    public static class Factory extends ModBlockFactory {
        @Override
        public Block buildBlockClass(ModBlock def) {
        	def.nonOpaque = true;
            AbstractBlock.Settings settings = def.makeBlockSettings();
            Block blk = new WCCuboidNSEWStackBlock(settings, def);
            return def.registerRenderType(ModBlocks.registerBlock(def.blockName, blk), false, false);
        }
    }
    // Support waterlogged on these blocks
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    public static final DirectionProperty FACING = DirectionProperty.of("facing", Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.NORTH);
    public static final EnumProperty<DoubleBlockHalf> HALF = Properties.DOUBLE_BLOCK_HALF;
    
    public final boolean allowHalfBreak;
    // Index = FACING + 4*TOP

    protected WCCuboidNSEWStackBlock(AbstractBlock.Settings settings, ModBlock def) {
        super(settings, def, 8);
        String t = def.getType();
        boolean brk = false;
        if (t != null) {
            String[] toks = t.split(",");
            for (String tok : toks) {
                if (tok.equals("allowHalfBreak")) {
                    brk = true;
                    break;
                }
            }
        }
        this.allowHalfBreak = brk;
        
        for (int i = 0; i < 2; i++) {
        	ModBlockStateRecord se = def.getStackElementByIndex(i);
            for (ModBlock.Cuboid c : se.cuboids) {
    			cuboid_by_facing[4*i] = new ArrayList<>();	// Use clean, since parent uses cuboid not stack
                cuboid_by_facing[4*i].add(c);
                cuboid_by_facing[4*i + 1].add(c.rotateCuboid(ModBlock.CuboidRotation.ROTY90));
                cuboid_by_facing[4*i + 2].add(c.rotateCuboid(ModBlock.CuboidRotation.ROTY180));
                cuboid_by_facing[4*i + 3].add(c.rotateCuboid(ModBlock.CuboidRotation.ROTY270));
            }        	
        }
        for (int j = 0; j < cuboid_by_facing.length; j++) {
            SHAPE_BY_INDEX[j] = getBoundingBoxFromCuboidList(cuboid_by_facing[j]);
        }
        this.setDefaultState(this.getDefaultState()
        		.with(HALF, DoubleBlockHalf.LOWER)
        		.with(FACING, Direction.EAST)
        		.with(WATERLOGGED, Boolean.FALSE));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
    	builder.add(FACING, HALF);
    }

    @Override
    protected int getIndexFromState(BlockState state) {
    	int topoff = (state.get(HALF) == DoubleBlockHalf.LOWER) ? 0 : 4;
        return switch (state.get(FACING)) {
            default -> topoff;
            case SOUTH -> topoff + 1;
            case WEST -> topoff + 2;
            case NORTH -> topoff + 3;
        };
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockPos blockpos = ctx.getBlockPos();
       if (blockpos.getY() < ctx.getWorld().getTopY() && ctx.getWorld().getBlockState(blockpos.up()).canReplace(ctx)) {
           FluidState fluidstate = ctx.getWorld().getFluidState(ctx.getBlockPos());
           Direction[] adirection = ctx.getPlacementDirections();
           Direction dir = Direction.EAST;	// Default
           for (Direction d : adirection) {
           	if (d == Direction.EAST || d == Direction.WEST || d == Direction.NORTH || d == Direction.SOUTH) {
       			dir = d;
       			break;
           	}
           }
           return this.getDefaultState()
       		.with(FACING, dir)
       		.with(HALF, DoubleBlockHalf.LOWER)
       		.with(WATERLOGGED, fluidstate.isIn(FluidTags.WATER));
       }
       else {
    	   return null;
       }
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState,
                                                WorldAccess world, BlockPos pos, BlockPos neighborPos) {
    	if (allowHalfBreak) { return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos); }
        DoubleBlockHalf doubleblockhalf = state.get(HALF);
        if (direction.getAxis() != Direction.Axis.Y
                || doubleblockhalf == DoubleBlockHalf.LOWER != (direction == Direction.UP)
                || neighborState.isOf(this) && neighborState.get(HALF) != doubleblockhalf) {
           return doubleblockhalf == DoubleBlockHalf.LOWER
                   && direction == Direction.DOWN
                   && !state.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
        } else {
           return Blocks.AIR.getDefaultState();
        }
     }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
    	BlockPos above = pos.up();
        FluidState fluidstate = world.getFluidState(above);
        BlockState newstate = this.getDefaultState()
			.with(FACING, state.get(FACING))
			.with(HALF, DoubleBlockHalf.UPPER)
			.with(WATERLOGGED, fluidstate.isIn(FluidTags.WATER));
        world.setBlockState(above, newstate, 3);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        if (this.allowHalfBreak || (state.get(HALF) != DoubleBlockHalf.UPPER)) {
           return super.canPlaceAt(state, world, pos);
        }
        else {
           BlockState blockstate = world.getBlockState(pos.down());
           if (state.getBlock() != this) {
        	   return super.canPlaceAt(state, world, pos);
           }
           return blockstate.isOf(this) && blockstate.get(HALF) == DoubleBlockHalf.LOWER;
        }
     }
}
