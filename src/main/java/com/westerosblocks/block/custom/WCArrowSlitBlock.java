package com.westerosblocks.block.custom;

import com.westerosblocks.block.*;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import java.util.ArrayList;

public class WCArrowSlitBlock extends WCCuboidBlock {

    public static class Factory extends ModBlockFactory {
        @Override
        public Block buildBlockClass(ModBlock def) {
            def.nonOpaque = true;
            AbstractBlock.Settings settings = def.applyCustomProperties();
            Block blk = new WCArrowSlitBlock(settings, def);
            return def.registerRenderType(ModBlocks.registerBlock(def.blockName, blk), false, false);
        }
    }

    // Support waterlogged on these blocks
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    public static final DirectionProperty FACING = DirectionProperty.of("facing", Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.NORTH);
    public static final BooleanProperty STACKED = BooleanProperty.of("stacked");

    protected WCArrowSlitBlock(AbstractBlock.Settings settings, ModBlock def) {
        super(settings, def, 8); // 8 possible states: 4 directions * 2 stacked states
        
        // Initialize cuboids for each facing direction and stacked state
        for (int i = 0; i < 2; i++) { // i=0 for unstacked, i=1 for stacked
            ModBlockStateRecord se = def.getStackElementByIndex(i);
            for (ModBlock.Cuboid c : se.cuboids) {
                cuboid_by_facing[4*i] = new ArrayList<>(); // East facing
                cuboid_by_facing[4*i].add(c);
                cuboid_by_facing[4*i + 1] = new ArrayList<>(); // South facing
                cuboid_by_facing[4*i + 1].add(c.rotateCuboid(ModBlock.CuboidRotation.ROTY90));
                cuboid_by_facing[4*i + 2] = new ArrayList<>(); // West facing
                cuboid_by_facing[4*i + 2].add(c.rotateCuboid(ModBlock.CuboidRotation.ROTY180));
                cuboid_by_facing[4*i + 3] = new ArrayList<>(); // North facing
                cuboid_by_facing[4*i + 3].add(c.rotateCuboid(ModBlock.CuboidRotation.ROTY270));
            }
        }

        // Create shapes for all combinations
        for (int j = 0; j < cuboid_by_facing.length; j++) {
            SHAPE_BY_INDEX[j] = getBoundingBoxFromCuboidList(cuboid_by_facing[j]);
        }

        this.setDefaultState(this.getDefaultState()
                .with(FACING, Direction.EAST)
                .with(STACKED, false)
                .with(WATERLOGGED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING, STACKED);
    }

    @Override
    protected int getIndexFromState(BlockState state) {
        int stackedOffset = state.get(STACKED) ? 4 : 0;
        return switch (state.get(FACING)) {
            case SOUTH -> stackedOffset + 1;
            case WEST -> stackedOffset + 2;
            case NORTH -> stackedOffset + 3;
            default -> stackedOffset;
        };
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockPos blockpos = ctx.getBlockPos();
        FluidState fluidstate = ctx.getWorld().getFluidState(blockpos);
        Direction[] adirection = ctx.getPlacementDirections();
        Direction dir = Direction.EAST; // Default
        for (Direction d : adirection) {
            if (d == Direction.EAST || d == Direction.WEST || d == Direction.NORTH || d == Direction.SOUTH) {
                dir = d;
                break;
            }
        }
        return this.getDefaultState()
                .with(FACING, dir)
                .with(WATERLOGGED, fluidstate.isIn(FluidTags.WATER));
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState,
                                              WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (direction == Direction.UP) {
            boolean isStacked = neighborState.isOf(this);
            return state.with(STACKED, isStacked);
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        // Update the block below if it exists
        BlockPos below = pos.down();
        BlockState belowState = world.getBlockState(below);
        if (belowState.isOf(this)) {
            world.setBlockState(below, belowState.with(STACKED, true), 3);
        }
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        // Update the block below if it exists
        BlockPos below = pos.down();
        BlockState belowState = world.getBlockState(below);
        if (belowState.isOf(this)) {
            world.setBlockState(below, belowState.with(STACKED, false), 3);
        }
        return super.onBreak(world, pos, state, player);
    }
} 