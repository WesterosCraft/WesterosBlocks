package com.westerosblocks.block.custom;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public abstract class FenceMimicBlock extends Block {
    
    public static final BooleanProperty NORTH = Properties.NORTH;
    public static final BooleanProperty EAST = Properties.EAST;
    public static final BooleanProperty SOUTH = Properties.SOUTH;
    public static final BooleanProperty WEST = Properties.WEST;
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    
    private static final VoxelShape NORTH_SHAPE = Block.createCuboidShape(7.0, 0.0, 0.0, 9.0, 16.0, 9.0);
    private static final VoxelShape SOUTH_SHAPE = Block.createCuboidShape(7.0, 0.0, 7.0, 9.0, 16.0, 16.0);
    private static final VoxelShape WEST_SHAPE = Block.createCuboidShape(0.0, 0.0, 7.0, 9.0, 16.0, 9.0);
    private static final VoxelShape EAST_SHAPE = Block.createCuboidShape(7.0, 0.0, 7.0, 16.0, 16.0, 9.0);
    private static final VoxelShape POST_SHAPE = Block.createCuboidShape(7.0, 0.0, 7.0, 9.0, 16.0, 9.0);
    
    protected FenceMimicBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getStateManager().getDefaultState()
            .with(NORTH, false)
            .with(EAST, false)
            .with(SOUTH, false)
            .with(WEST, false)
            .with(WATERLOGGED, false));
    }
    
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, WATERLOGGED);
    }
    
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockView blockView = ctx.getWorld();
        BlockPos blockPos = ctx.getBlockPos();
        BlockPos blockPos2 = blockPos.north();
        BlockPos blockPos3 = blockPos.south();
        BlockPos blockPos4 = blockPos.west();
        BlockPos blockPos5 = blockPos.east();
        BlockState blockState = blockView.getBlockState(blockPos2);
        BlockState blockState2 = blockView.getBlockState(blockPos3);
        BlockState blockState3 = blockView.getBlockState(blockPos4);
        BlockState blockState4 = blockView.getBlockState(blockPos5);
        return this.getDefaultState()
            .with(NORTH, this.canConnect(blockState, blockState.isSideSolidFullSquare(blockView, blockPos2, Direction.SOUTH), Direction.SOUTH))
            .with(SOUTH, this.canConnect(blockState2, blockState2.isSideSolidFullSquare(blockView, blockPos3, Direction.NORTH), Direction.NORTH))
            .with(WEST, this.canConnect(blockState3, blockState3.isSideSolidFullSquare(blockView, blockPos4, Direction.EAST), Direction.EAST))
            .with(EAST, this.canConnect(blockState4, blockState4.isSideSolidFullSquare(blockView, blockPos5, Direction.WEST), Direction.WEST));
    }
    
    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, net.minecraft.fluid.Fluids.WATER, net.minecraft.fluid.Fluids.WATER.getTickRate(world));
        }
        
        if (direction.getAxis().getType() == Direction.Type.HORIZONTAL) {
            return state.with(getPropertyForDirection(direction), this.canConnect(neighborState, neighborState.isSideSolidFullSquare(world, neighborPos, direction.getOpposite()), direction.getOpposite()));
        }
        
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }
    
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        VoxelShape voxelShape = POST_SHAPE;
        if (state.get(NORTH)) {
            voxelShape = VoxelShapes.union(voxelShape, NORTH_SHAPE);
        }
        if (state.get(EAST)) {
            voxelShape = VoxelShapes.union(voxelShape, EAST_SHAPE);
        }
        if (state.get(SOUTH)) {
            voxelShape = VoxelShapes.union(voxelShape, SOUTH_SHAPE);
        }
        if (state.get(WEST)) {
            voxelShape = VoxelShapes.union(voxelShape, WEST_SHAPE);
        }
        return voxelShape;
    }
    
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return this.getOutlineShape(state, world, pos, context);
    }
    
    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        BlockPos blockPos = pos.down();
        BlockState blockState = world.getBlockState(blockPos);
        return blockState.isSideSolidFullSquare(world, blockPos, Direction.UP);
    }
    
    private boolean canConnect(BlockState state, boolean neighborIsFullSquare, Direction dir) {
        Block block = state.getBlock();
        boolean bl = this.isSameFence(state);
        boolean bl2 = block instanceof FenceGateBlock && FenceGateBlock.canWallConnect(state, dir);
        return !Block.cannotConnect(state) && neighborIsFullSquare || bl || bl2;
    }
    
    private boolean isSameFence(BlockState state) {
        return state.isIn(net.minecraft.registry.tag.BlockTags.FENCES) && state.isIn(net.minecraft.registry.tag.BlockTags.WOODEN_FENCES) == this.getDefaultState().isIn(net.minecraft.registry.tag.BlockTags.WOODEN_FENCES);
    }
    
    private static BooleanProperty getPropertyForDirection(Direction direction) {
        return switch (direction) {
            case NORTH -> NORTH;
            case SOUTH -> SOUTH;
            case WEST -> WEST;
            case EAST -> EAST;
            default -> throw new IllegalArgumentException("Invalid direction: " + direction);
        };
    }
} 