package com.westerosblocks.block.custom;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

import java.util.Map;

public class WCTableBlock extends Block {
    public static final BooleanProperty NORTH = Properties.NORTH;
    public static final BooleanProperty EAST = Properties.EAST;
    public static final BooleanProperty SOUTH = Properties.SOUTH;
    public static final BooleanProperty WEST = Properties.WEST;
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    // Define base shapes for table components
    private static final VoxelShape TABLE_TOP = Block.createCuboidShape(0, 14, 0, 16, 16, 16);
    private static final VoxelShape TABLE_TOP_INNER = Block.createCuboidShape(1, 11, 1, 15, 14, 15);
    private static final VoxelShape TABLE_LEG = Block.createCuboidShape(1, 0, 1, 4, 11, 4);

    // Pre-computed shape maps for efficient lookups
    private final Map<BlockState, VoxelShape> shapeByIndex;

    public WCTableBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState()
                .with(NORTH, false)
                .with(EAST, false)
                .with(SOUTH, false)
                .with(WEST, false)
                .with(WATERLOGGED, false));
        // Pre-compute all possible shape combinations
        this.shapeByIndex = this.makeShapes();
    }

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(AbstractBlock.Settings settings, Object... params) {
            return new WCTableBlock(settings);
        }
    }

    private Map<BlockState, VoxelShape> makeShapes() {
        ImmutableMap.Builder<BlockState, VoxelShape> builder = ImmutableMap.builder();

        // Generate all possible state combinations
        for (boolean north : new boolean[] { false, true }) {
            for (boolean east : new boolean[] { false, true }) {
                for (boolean south : new boolean[] { false, true }) {
                    for (boolean west : new boolean[] { false, true }) {
                        VoxelShape shape = this.getShapeForConnections(north, east, south, west);

                        // Add both waterlogged and non-waterlogged states
                        BlockState state = this.getDefaultState()
                                .with(NORTH, north)
                                .with(EAST, east)
                                .with(SOUTH, south)
                                .with(WEST, west);

                        builder.put(state.with(WATERLOGGED, false), shape);
                        builder.put(state.with(WATERLOGGED, true), shape);
                    }
                }
            }
        }

        return builder.build();
    }

    private VoxelShape getShapeForConnections(boolean north, boolean east, boolean south, boolean west) {
        // Count connections
        int connections = (north ? 1 : 0) + (east ? 1 : 0) + (south ? 1 : 0) + (west ? 1 : 0);

        return switch (connections) {
            case 0 -> getSingleShape();
            case 1 -> getDoubleShape(north, east, south, west);
            case 2 -> getCornerOrCenterShape(north, east, south, west);
            case 3, 4 -> getComplexShape(north, east, south, west);
            default -> getSingleShape();
        };
    }

    private VoxelShape getSingleShape() {
        return VoxelShapes.union(
                TABLE_TOP,
                TABLE_TOP_INNER,
                TABLE_LEG, // Front-left leg
                Block.createCuboidShape(1, 0, 12, 4, 11, 15), // Front-right leg
                Block.createCuboidShape(12, 0, 12, 15, 11, 15), // Back-right leg
                Block.createCuboidShape(12, 0, 1, 15, 11, 4) // Back-left leg
        );
    }

    private VoxelShape getDoubleShape(boolean north, boolean east, boolean south, boolean west) {
        if (north) {
            return VoxelShapes.union(
                    TABLE_TOP,
                    Block.createCuboidShape(1, 11, 0, 15, 14, 15), // Extended inner top (flipped)
                    Block.createCuboidShape(1, 0, 12, 4, 11, 15), // Front leg
                    Block.createCuboidShape(12, 0, 12, 15, 11, 15) // Back leg
            );
        } else if (south) {
            return VoxelShapes.union(
                    TABLE_TOP,
                    Block.createCuboidShape(1, 11, 1, 15, 14, 16), // Extended inner top
                    TABLE_LEG, // Left leg
                    Block.createCuboidShape(12, 0, 1, 15, 11, 4) // Right leg
            );
        } else if (east) {
            return VoxelShapes.union(
                    TABLE_TOP,
                    Block.createCuboidShape(1, 11, 1, 16, 14, 15), // Extended inner top (rotated)
                    Block.createCuboidShape(1, 0, 1, 4, 11, 4), // Front leg
                    Block.createCuboidShape(1, 0, 12, 4, 11, 15) // Back leg
            );
        } else { // west
            return VoxelShapes.union(
                    TABLE_TOP,
                    Block.createCuboidShape(0, 11, 1, 15, 14, 15), // Extended inner top (mirrored)
                    Block.createCuboidShape(12, 0, 1, 15, 11, 4), // Front leg
                    Block.createCuboidShape(12, 0, 12, 15, 11, 15) // Back leg
            );
        }
    }

    private VoxelShape getCornerOrCenterShape(boolean north, boolean east, boolean south, boolean west) {
        // Check if it's adjacent connections (corner) or opposite connections (center)
        if ((north && south) || (east && west)) {
            // Center shape - connected on opposite sides
            if (north && south) {
                return VoxelShapes.union(
                        TABLE_TOP,
                        Block.createCuboidShape(1, 11, 0, 15, 14, 16) // Full width inner top (north/south)
                );
            } else {
                return VoxelShapes.union(
                        TABLE_TOP,
                        Block.createCuboidShape(0, 11, 1, 16, 14, 15) // Full width inner top (east/west)
                );
            }
        } else {
            // Corner shape - connected on adjacent sides
            if (south && east) {
                return VoxelShapes.union(
                        TABLE_TOP,
                        Block.createCuboidShape(1, 11, 1, 16, 14, 16), // Extended inner top
                        TABLE_LEG // Single corner leg
                );
            } else if (south && west) {
                return VoxelShapes.union(
                        TABLE_TOP,
                        Block.createCuboidShape(0, 11, 1, 15, 14, 16), // Extended inner top
                        Block.createCuboidShape(12, 0, 1, 15, 11, 4) // Single corner leg
                );
            } else if (north && east) {
                return VoxelShapes.union(
                        TABLE_TOP,
                        Block.createCuboidShape(1, 11, 0, 16, 14, 15), // Extended inner top
                        Block.createCuboidShape(1, 0, 12, 4, 11, 15) // Single corner leg
                );
            } else { // north && west
                return VoxelShapes.union(
                        TABLE_TOP,
                        Block.createCuboidShape(0, 11, 0, 15, 14, 15), // Extended inner top
                        Block.createCuboidShape(12, 0, 12, 15, 11, 15) // Single corner leg
                );
            }
        }
    }

    private VoxelShape getComplexShape(boolean north, boolean east, boolean south, boolean west) {
        // For multiple connections, determine the primary orientation
        if ((north && south) || (east && west)) {
            // If we have opposite connections, use the appropriate center shape
            if (north && south) {
                return VoxelShapes.union(
                        TABLE_TOP,
                        Block.createCuboidShape(1, 11, 0, 15, 14, 16) // Full width inner top (north/south)
                );
            } else {
                return VoxelShapes.union(
                        TABLE_TOP,
                        Block.createCuboidShape(0, 11, 1, 16, 14, 15) // Full width inner top (east/west)
                );
            }
        } else {
            // Default to north-south center for complex connections
            return VoxelShapes.union(
                    TABLE_TOP,
                    Block.createCuboidShape(1, 11, 0, 15, 14, 16) // Full width inner top (north/south)
            );
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, WATERLOGGED);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockPos pos = ctx.getBlockPos();
        BlockView world = ctx.getWorld();
        FluidState fluidState = world.getFluidState(pos);

        boolean connectNorth = world.getBlockState(pos.north()).isOf(this);
        boolean connectEast = world.getBlockState(pos.east()).isOf(this);
        boolean connectSouth = world.getBlockState(pos.south()).isOf(this);
        boolean connectWest = world.getBlockState(pos.west()).isOf(this);

        return this.getDefaultState()
                .with(WATERLOGGED, fluidState.isIn(FluidTags.WATER))
                .with(NORTH, connectNorth)
                .with(EAST, connectEast)
                .with(SOUTH, connectSouth)
                .with(WEST, connectWest);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState,
            WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        if (direction.getAxis().isHorizontal()) {
            boolean isConnected = neighborState.isOf(this);
            return state.with(getPropertyForDirection(direction), isConnected);
        }

        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    private static BooleanProperty getPropertyForDirection(Direction direction) {
        return switch (direction) {
            case NORTH -> NORTH;
            case EAST -> EAST;
            case SOUTH -> SOUTH;
            case WEST -> WEST;
            default -> throw new IllegalArgumentException("Invalid direction: " + direction);
        };
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return this.shapeByIndex.get(state);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return this.shapeByIndex.get(state);
    }
}
