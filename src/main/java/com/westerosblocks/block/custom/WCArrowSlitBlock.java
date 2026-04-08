package com.westerosblocks.block.custom;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

import com.westerosblocks.data.BlockDefinition;

import java.util.Map;

public class WCArrowSlitBlock extends Block implements WCBlockDef {
    protected BlockDefinition def;
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final BooleanProperty UP = Properties.UP;
    public static final BooleanProperty DOWN = Properties.DOWN;

    private static final VoxelShape BOTTOM_BASE = Block.createCuboidShape(0, 0, 0, 16, 3, 16);
    private static final VoxelShape TOP_BASE = Block.createCuboidShape(0, 13, 0, 16, 16, 16);

    private final Map<BlockState, VoxelShape> shapeByIndex;

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(BlockDefinition definition) {
            AbstractBlock.Settings settings = definition.makeSettings();
            return new WCArrowSlitBlock(settings, definition);
        }
    }

    public WCArrowSlitBlock(AbstractBlock.Settings settings, BlockDefinition def) {
        super(settings);
        this.def = def;
        setDefaultState(getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(UP, false)
                .with(DOWN, false));
        this.shapeByIndex = this.makeShapes();
    }

    private Map<BlockState, VoxelShape> makeShapes() {
        ImmutableMap.Builder<BlockState, VoxelShape> builder = ImmutableMap.builder();
        for (Direction facing : Direction.Type.HORIZONTAL) {
            for (boolean up : new boolean[]{false, true}) {
                for (boolean down : new boolean[]{false, true}) {
                    BlockState state = this.getDefaultState()
                            .with(FACING, facing)
                            .with(UP, up)
                            .with(DOWN, down);
                    builder.put(state, getShapeForState(facing, up, down));
                }
            }
        }
        return builder.build();
    }

    private VoxelShape getShapeForState(Direction facing, boolean up, boolean down) {
        boolean northSouth = (facing == Direction.NORTH || facing == Direction.SOUTH);

        // Wall shapes depend on orientation
        VoxelShape leftWall, rightWall, leftSlit, rightSlit;
        if (northSouth) {
            leftWall = Block.createCuboidShape(0, 0, 0, 6, 16, 13);
            rightWall = Block.createCuboidShape(10, 0, 0, 16, 16, 13);
            leftSlit = Block.createCuboidShape(0, 0, 0, 6, 16, 13);
            rightSlit = Block.createCuboidShape(10, 0, 0, 16, 16, 13);
        } else {
            leftWall = Block.createCuboidShape(3, 0, 0, 13, 16, 6);
            rightWall = Block.createCuboidShape(3, 0, 10, 13, 16, 16);
            leftSlit = Block.createCuboidShape(3, 0, 0, 13, 16, 6);
            rightSlit = Block.createCuboidShape(3, 0, 10, 13, 16, 16);
        }

        if (!up && !down) {
            // Single: walls + top base + bottom base
            return VoxelShapes.union(leftWall, rightWall, BOTTOM_BASE, TOP_BASE);
        } else if (up && !down) {
            // Bottom of stack: walls + bottom base
            return VoxelShapes.union(leftWall, rightWall, BOTTOM_BASE);
        } else if (!up && down) {
            // Top of stack: walls + top base
            return VoxelShapes.union(leftWall, rightWall, TOP_BASE);
        } else {
            // Middle: walls only
            return VoxelShapes.union(leftWall, rightWall);
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, UP, DOWN);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState,
                                                WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (direction == Direction.UP) {
            return state.with(UP, neighborState.isOf(this));
        }
        if (direction == Direction.DOWN) {
            return state.with(DOWN, neighborState.isOf(this));
        }
        return state;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockPos pos = ctx.getBlockPos();
        boolean up = ctx.getWorld().getBlockState(pos.up()).isOf(this);
        boolean down = ctx.getWorld().getBlockState(pos.down()).isOf(this);
        return this.getDefaultState()
                .with(FACING, ctx.getHorizontalPlayerFacing().getOpposite())
                .with(UP, up)
                .with(DOWN, down);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return this.shapeByIndex.get(state);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return this.shapeByIndex.get(state);
    }

    public BlockDefinition getDefinition() {
        return def;
    }
}
