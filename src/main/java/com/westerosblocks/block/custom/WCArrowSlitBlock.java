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
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.block.custom.BlockFactory;

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

        this.shapeByIndex = this.makeShapes();
    }

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(AbstractBlock.Settings settings, BlockDefinition definition) {
            // Handle null definition (from BlockBuilder) with sensible defaults
            String blockName = definition != null ? definition.getBlockName() : "arrow_slit";
            String creativeTab = definition != null ? definition.getCreativeTab() : "building_blocks";
            return new WCArrowSlitBlock(settings, blockName, creativeTab);
        }
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
            case MIDDLE, MIDDLE_EDGE -> VoxelShapes.union(
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
            case MIDDLE, MIDDLE_EDGE -> VoxelShapes.union(
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
            case MIDDLE, MIDDLE_EDGE -> VoxelShapes.union(
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
            case MIDDLE, MIDDLE_EDGE -> VoxelShapes.union(
                    Block.createCuboidShape(3, 0, 0, 13, 16, 3),
                    Block.createCuboidShape(3, 0, 13, 13, 16, 16),
                    Block.createCuboidShape(3, 2, 3, 13, 14, 6),
                    Block.createCuboidShape(3, 2, 10, 13, 14, 13));
        };
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(TYPE, FACING);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState,
                                                WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (direction == Direction.UP || direction == Direction.DOWN) {
            // Count blocks upward
            int blocksAbove = 0;
            BlockPos currentPos = pos.up();
            while (world.getBlockState(currentPos).isOf(this)) {
                blocksAbove++;
                currentPos = currentPos.up();
            }

            // Count blocks downward
            int blocksBelow = 0;
            currentPos = pos.down();
            while (world.getBlockState(currentPos).isOf(this)) {
                blocksBelow++;
                currentPos = currentPos.down();
            }

            // Calculate stack height and position
            int stackHeight = blocksAbove + blocksBelow + 1; // +1 for current block
            int position = blocksBelow; // 0 = bottom of stack

            // Determine type based on height and position
            ArrowSlitType newType;
            if (stackHeight == 1) {
                // Single isolated block
                newType = ArrowSlitType.SINGLE;
            } else if (position == 0) {
                // Bottom of stack
                newType = ArrowSlitType.BOTTOM;
            } else if (position == stackHeight - 1) {
                // Top of stack
                newType = ArrowSlitType.TOP;
            } else if (stackHeight == 3) {
                // 3-block stack: middle is always MIDDLE
                newType = ArrowSlitType.MIDDLE;
            } else if (stackHeight == 4) {
                // 4-block stack: no MIDDLE, only MIDDLE_EDGE
                newType = ArrowSlitType.MIDDLE_EDGE;
            } else {
                // 5+ blocks: MIDDLE at height/2, MIDDLE_EDGE elsewhere
                int middlePosition = stackHeight / 2;
                if (position == middlePosition) {
                    newType = ArrowSlitType.MIDDLE;
                } else {
                    newType = ArrowSlitType.MIDDLE_EDGE;
                }
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
        MIDDLE("middle"),
        MIDDLE_EDGE("middle_edge");

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