package com.westerosblocks.block.custom;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.util.StringIdentifiable;

import java.util.Map;

public class WCArrowSlitBlock extends Block {
    public static final EnumProperty<ArrowSlitType> TYPE = EnumProperty.of("type", ArrowSlitType.class);
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

    // Define the base shapes for each part of the arrow slit
    private static final VoxelShape LEFT_WALL = Block.createCuboidShape(0, 0, 3, 3, 16, 13);
    private static final VoxelShape RIGHT_WALL = Block.createCuboidShape(13, 0, 3, 16, 16, 13);
    private static final VoxelShape LEFT_SLIT_WALL = Block.createCuboidShape(3, 2, 3, 6, 14, 13);
    private static final VoxelShape RIGHT_SLIT_WALL = Block.createCuboidShape(10, 2, 3, 13, 14, 13);
    private static final VoxelShape BOTTOM_BASE = Block.createCuboidShape(0, 0, 0, 16, 4, 16);
    private static final VoxelShape TOP_BASE = Block.createCuboidShape(0, 12, 0, 16, 16, 16);
    private static final VoxelShape BOTTOM_LEDGE_LEFT = Block.createCuboidShape(0, 15, 3, 3, 16, 13);
    private static final VoxelShape BOTTOM_LEDGE_RIGHT = Block.createCuboidShape(13, 15, 3, 16, 16, 13);
    private static final VoxelShape TOP_LEDGE_LEFT = Block.createCuboidShape(0, 0, 3, 3, 1, 13);
    private static final VoxelShape TOP_LEDGE_RIGHT = Block.createCuboidShape(13, 0, 3, 16, 1, 13);

    // Pre-computed shape maps for efficient lookups
    private final Map<BlockState, VoxelShape> shapeByIndex;

    private final String blockName;
    private final String creativeTab;

    public WCArrowSlitBlock(AbstractBlock.Settings settings, String blockName, String creativeTab) {
        super(settings);
        this.blockName = blockName;
        this.creativeTab = creativeTab;
        setDefaultState(getDefaultState()
                .with(TYPE, ArrowSlitType.SINGLE)
                .with(FACING, Direction.NORTH));

        // Pre-compute all possible shape combinations
        this.shapeByIndex = this.makeShapes();
    }

    private Map<BlockState, VoxelShape> makeShapes() {
        ImmutableMap.Builder<BlockState, VoxelShape> builder = ImmutableMap.builder();

        // Generate all possible state combinations
        for (ArrowSlitType type : ArrowSlitType.values()) {
            for (Direction facing : Direction.Type.HORIZONTAL) {
                VoxelShape shape = this.getShapeForState(type, facing);

                BlockState state = this.getDefaultState()
                        .with(TYPE, type)
                        .with(FACING, facing);

                builder.put(state, shape);
            }
        }

        return builder.build();
    }

    private VoxelShape getShapeForState(ArrowSlitType type, Direction facing) {
        return switch (facing) {
            case NORTH -> getNorthShape(type);
            case SOUTH -> getSouthShape(type);
            case EAST -> getEastShape(type);
            case WEST -> getWestShape(type);
            default -> getNorthShape(ArrowSlitType.SINGLE);
        };
    }

    private VoxelShape getNorthShape(ArrowSlitType type) {
        return switch (type) {
            case SINGLE -> VoxelShapes.union(
                    Block.createCuboidShape(0, 0, 3, 3, 16, 13),
                    Block.createCuboidShape(13, 0, 3, 16, 16, 13),
                    Block.createCuboidShape(0, 4, 3, 6, 12, 13),
                    Block.createCuboidShape(10, 4, 3, 16, 12, 13),
                    BOTTOM_BASE, TOP_BASE);
            case TOP -> VoxelShapes.union(
                    Block.createCuboidShape(0, 0, 3, 3, 16, 13),
                    Block.createCuboidShape(13, 0, 3, 16, 16, 13),
                    Block.createCuboidShape(0, 1, 3, 6, 12, 13),
                    Block.createCuboidShape(10, 1, 3, 16, 12, 13),
                    TOP_BASE, TOP_LEDGE_LEFT, TOP_LEDGE_RIGHT);
            case BOTTOM -> VoxelShapes.union(
                    Block.createCuboidShape(0, 0, 3, 3, 16, 13),
                    Block.createCuboidShape(13, 0, 3, 16, 16, 13),
                    Block.createCuboidShape(0, 4, 3, 6, 15, 13),
                    Block.createCuboidShape(10, 4, 3, 16, 15, 13),
                    BOTTOM_BASE, BOTTOM_LEDGE_LEFT, BOTTOM_LEDGE_RIGHT);
            case MIDDLE -> VoxelShapes.union(
                    LEFT_WALL, RIGHT_WALL, LEFT_SLIT_WALL, RIGHT_SLIT_WALL);
        };
    }

    private VoxelShape getSouthShape(ArrowSlitType type) {
        return switch (type) {
            case SINGLE -> VoxelShapes.union(
                    Block.createCuboidShape(0, 0, 3, 3, 16, 13),
                    Block.createCuboidShape(13, 0, 3, 16, 16, 13),
                    Block.createCuboidShape(0, 4, 3, 6, 12, 13),
                    Block.createCuboidShape(10, 4, 3, 16, 12, 13),
                    BOTTOM_BASE, TOP_BASE);
            case TOP -> VoxelShapes.union(
                    Block.createCuboidShape(0, 0, 3, 3, 16, 13),
                    Block.createCuboidShape(13, 0, 3, 16, 16, 13),
                    Block.createCuboidShape(0, 1, 3, 6, 12, 13),
                    Block.createCuboidShape(10, 1, 3, 16, 12, 13),
                    TOP_BASE, TOP_LEDGE_LEFT, TOP_LEDGE_RIGHT);
            case BOTTOM -> VoxelShapes.union(
                    Block.createCuboidShape(0, 0, 3, 3, 16, 13),
                    Block.createCuboidShape(13, 0, 3, 16, 16, 13),
                    Block.createCuboidShape(0, 4, 3, 6, 15, 13),
                    Block.createCuboidShape(10, 4, 3, 16, 15, 13),
                    BOTTOM_BASE, BOTTOM_LEDGE_LEFT, BOTTOM_LEDGE_RIGHT);
            case MIDDLE -> VoxelShapes.union(
                    Block.createCuboidShape(0, 0, 3, 3, 16, 13),
                    Block.createCuboidShape(13, 0, 3, 16, 16, 13),
                    Block.createCuboidShape(3, 2, 3, 6, 14, 13),
                    Block.createCuboidShape(10, 2, 3, 13, 14, 13));
        };
    }

    private VoxelShape getEastShape(ArrowSlitType type) {
        return switch (type) {
            case SINGLE -> VoxelShapes.union(
                    Block.createCuboidShape(3, 0, 0, 13, 16, 3),
                    Block.createCuboidShape(3, 0, 13, 13, 16, 16),
                    Block.createCuboidShape(3, 4, 3, 13, 12, 6),
                    Block.createCuboidShape(3, 4, 10, 13, 12, 13),
                    BOTTOM_BASE, TOP_BASE);
            case TOP -> VoxelShapes.union(
                    Block.createCuboidShape(3, 0, 0, 13, 16, 3),
                    Block.createCuboidShape(3, 0, 13, 13, 16, 16),
                    Block.createCuboidShape(3, 1, 3, 13, 12, 6),
                    Block.createCuboidShape(3, 1, 10, 13, 12, 13),
                    TOP_BASE, TOP_LEDGE_LEFT, TOP_LEDGE_RIGHT);
            case BOTTOM -> VoxelShapes.union(
                    Block.createCuboidShape(3, 0, 0, 13, 16, 3),
                    Block.createCuboidShape(3, 0, 13, 13, 16, 16),
                    Block.createCuboidShape(3, 4, 3, 13, 15, 6),
                    Block.createCuboidShape(3, 4, 10, 13, 15, 13),
                    BOTTOM_BASE, BOTTOM_LEDGE_LEFT, BOTTOM_LEDGE_RIGHT);
            case MIDDLE -> VoxelShapes.union(
                    Block.createCuboidShape(3, 0, 0, 13, 16, 3),
                    Block.createCuboidShape(3, 0, 13, 13, 16, 16),
                    Block.createCuboidShape(3, 2, 3, 13, 14, 6),
                    Block.createCuboidShape(3, 2, 10, 13, 14, 13));
        };
    }

    private VoxelShape getWestShape(ArrowSlitType type) {
        return switch (type) {
            case SINGLE -> VoxelShapes.union(
                    Block.createCuboidShape(3, 0, 0, 13, 16, 3),
                    Block.createCuboidShape(3, 0, 13, 13, 16, 16),
                    Block.createCuboidShape(3, 4, 3, 13, 12, 6),
                    Block.createCuboidShape(3, 4, 10, 13, 12, 13),
                    BOTTOM_BASE, TOP_BASE);
            case TOP -> VoxelShapes.union(
                    Block.createCuboidShape(3, 0, 0, 13, 16, 3),
                    Block.createCuboidShape(3, 0, 13, 13, 16, 16),
                    Block.createCuboidShape(3, 1, 3, 13, 12, 6),
                    Block.createCuboidShape(3, 1, 10, 13, 12, 13),
                    TOP_BASE, TOP_LEDGE_LEFT, TOP_LEDGE_RIGHT);
            case BOTTOM -> VoxelShapes.union(
                    Block.createCuboidShape(3, 0, 0, 13, 16, 3),
                    Block.createCuboidShape(3, 0, 13, 13, 16, 16),
                    Block.createCuboidShape(3, 4, 3, 13, 15, 6),
                    Block.createCuboidShape(3, 4, 10, 13, 15, 13),
                    BOTTOM_BASE, BOTTOM_LEDGE_LEFT, BOTTOM_LEDGE_RIGHT);
            case MIDDLE -> VoxelShapes.union(
                    Block.createCuboidShape(3, 0, 0, 13, 16, 3),
                    Block.createCuboidShape(3, 0, 13, 13, 16, 16),
                    Block.createCuboidShape(3, 2, 3, 13, 14, 6),
                    Block.createCuboidShape(3, 2, 10, 13, 14, 13));
        };
    }

    public String getBlockName() {
        return blockName;
    }

    public String getCreativeTab() {
        return creativeTab;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(TYPE, FACING);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState,
            WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (direction == Direction.UP || direction == Direction.DOWN) {
            boolean hasTop = world.getBlockState(pos.up()).isOf(this);
            boolean hasBottom = world.getBlockState(pos.down()).isOf(this);

            ArrowSlitType newType;
            if (hasTop && hasBottom) {
                newType = ArrowSlitType.MIDDLE;
            } else if (hasTop) {
                newType = ArrowSlitType.BOTTOM;
            } else if (hasBottom) {
                newType = ArrowSlitType.TOP;
            } else {
                newType = ArrowSlitType.SINGLE;
            }

            return state.with(TYPE, newType);
        }
        return state;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState()
                .with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        return super.onBreak(world, pos, state, player);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return this.shapeByIndex.get(state);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return this.shapeByIndex.get(state);
    }

    public enum ArrowSlitType implements StringIdentifiable {
        SINGLE("single"),
        TOP("top"),
        BOTTOM("bottom"),
        MIDDLE("middle");

        private final String name;

        ArrowSlitType(String name) {
            this.name = name;
        }

        @Override
        public String asString() {
            return this.name;
        }
    }
}