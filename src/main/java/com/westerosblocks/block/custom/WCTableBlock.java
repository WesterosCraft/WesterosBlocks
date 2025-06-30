package com.westerosblocks.block.custom;

import com.westerosblocks.util.ModUtils;
import com.westerosblocks.util.ModWoodType;
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
import net.minecraft.block.WoodType;

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

    // Define shapes for each table variant
    // Single table (no connections)
    private static final VoxelShape SINGLE_SHAPE = VoxelShapes.union(
        TABLE_TOP,
        TABLE_TOP_INNER,
        TABLE_LEG, // Front-left leg
        Block.createCuboidShape(1, 0, 12, 4, 11, 15), // Front-right leg
        Block.createCuboidShape(12, 0, 12, 15, 11, 15), // Back-right leg
        Block.createCuboidShape(12, 0, 1, 15, 11, 4) // Back-left leg
    );

    // Double table (connected on one side)
    private static final VoxelShape DOUBLE_SHAPE = VoxelShapes.union(
        TABLE_TOP,
        Block.createCuboidShape(1, 11, 1, 15, 14, 16), // Extended inner top
        TABLE_LEG, // Left leg
        Block.createCuboidShape(12, 0, 1, 15, 11, 4) // Right leg
    );

    // Double table for east/west connections (rotated 90 degrees)
    private static final VoxelShape DOUBLE_SHAPE_EAST_WEST = VoxelShapes.union(
        TABLE_TOP,
        Block.createCuboidShape(1, 11, 1, 16, 14, 15), // Extended inner top (rotated)
        Block.createCuboidShape(1, 0, 1, 4, 11, 4), // Front leg
        Block.createCuboidShape(1, 0, 12, 4, 11, 15) // Back leg
    );

    // Double table for west connections (mirrored from east/west)
    private static final VoxelShape DOUBLE_SHAPE_WEST = VoxelShapes.union(
        TABLE_TOP,
        Block.createCuboidShape(0, 11, 1, 15, 14, 15), // Extended inner top (mirrored)
        Block.createCuboidShape(12, 0, 1, 15, 11, 4), // Front leg
        Block.createCuboidShape(12, 0, 12, 15, 11, 15) // Back leg
    );

    // Double table for north connections (flipped 180 from south)
    private static final VoxelShape DOUBLE_SHAPE_NORTH = VoxelShapes.union(
        TABLE_TOP,
        Block.createCuboidShape(1, 11, 0, 15, 14, 15), // Extended inner top (flipped)
        Block.createCuboidShape(1, 0, 12, 4, 11, 15), // Front leg
        Block.createCuboidShape(12, 0, 12, 15, 11, 15) // Back leg
    );

    // Corner table (connected on two adjacent sides)
    private static final VoxelShape CORNER_SHAPE = VoxelShapes.union(
        TABLE_TOP,
        Block.createCuboidShape(1, 11, 1, 16, 14, 16), // Extended inner top
        TABLE_LEG // Single corner leg
    );

    // Corner shapes for different orientations
    // South-East corner (south and east connected)
    private static final VoxelShape CORNER_SHAPE_SOUTH_EAST = VoxelShapes.union(
        TABLE_TOP,
        Block.createCuboidShape(1, 11, 1, 16, 14, 16), // Extended inner top
        TABLE_LEG // Single corner leg
    );

    // South-West corner (south and west connected)
    private static final VoxelShape CORNER_SHAPE_SOUTH_WEST = VoxelShapes.union(
        TABLE_TOP,
        Block.createCuboidShape(0, 11, 1, 15, 14, 16), // Extended inner top
        Block.createCuboidShape(12, 0, 1, 15, 11, 4) // Single corner leg
    );

    // North-East corner (north and east connected)
    private static final VoxelShape CORNER_SHAPE_NORTH_EAST = VoxelShapes.union(
        TABLE_TOP,
        Block.createCuboidShape(1, 11, 0, 16, 14, 15), // Extended inner top
        Block.createCuboidShape(1, 0, 12, 4, 11, 15) // Single corner leg
    );

    // North-West corner (north and west connected)
    private static final VoxelShape CORNER_SHAPE_NORTH_WEST = VoxelShapes.union(
        TABLE_TOP,
        Block.createCuboidShape(0, 11, 0, 15, 14, 15), // Extended inner top
        Block.createCuboidShape(12, 0, 12, 15, 11, 15) // Single corner leg
    );

    // Center table (connected on opposite sides)
    private static final VoxelShape CENTER_SHAPE = VoxelShapes.union(
        TABLE_TOP,
        Block.createCuboidShape(1, 11, 0, 15, 14, 16) // Full width inner top
    );

    // Center shapes for different orientations
    // North-South center (north and south connected)
    private static final VoxelShape CENTER_SHAPE_NORTH_SOUTH = VoxelShapes.union(
        TABLE_TOP,
        Block.createCuboidShape(1, 11, 0, 15, 14, 16) // Full width inner top (north/south)
    );

    // East-West center (east and west connected)
    private static final VoxelShape CENTER_SHAPE_EAST_WEST = VoxelShapes.union(
        TABLE_TOP,
        Block.createCuboidShape(0, 11, 1, 16, 14, 15) // Full width inner top (east/west)
    );

    private final String blockName;
    private final String creativeTab;
    private final WoodType woodType;

    public WCTableBlock(AbstractBlock.Settings settings, String blockName, String creativeTab) {
        this(settings, blockName, creativeTab, "oak");
    }

    public WCTableBlock(AbstractBlock.Settings settings, String blockName, String creativeTab, String woodType) {
        this(settings, blockName, creativeTab, ModWoodType.getWoodType(woodType));
    }

    public WCTableBlock(AbstractBlock.Settings settings, String blockName, String creativeTab, WoodType woodType) {
        super(settings);
        this.blockName = blockName;
        this.creativeTab = creativeTab;
        this.woodType = woodType;
        this.setDefaultState(this.getDefaultState()
                .with(NORTH, false)
                .with(EAST, false)
                .with(SOUTH, false)
                .with(WEST, false)
                .with(WATERLOGGED, false));
    }

    public String getBlockName() {
        return blockName;
    }

    public String getCreativeTab() {
        return creativeTab;
    }

    public WoodType getWoodType() {
        return woodType;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, WATERLOGGED);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        FluidState fluidstate = ctx.getWorld().getFluidState(ctx.getBlockPos());
        return this.getDefaultState()
                .with(WATERLOGGED, fluidstate.isIn(FluidTags.WATER));
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
        return getShapeForState(state);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return getShapeForState(state);
    }

    private VoxelShape getShapeForState(BlockState state) {
        boolean north = state.get(NORTH);
        boolean east = state.get(EAST);
        boolean south = state.get(SOUTH);
        boolean west = state.get(WEST);

        // Count connections
        int connections = (north ? 1 : 0) + (east ? 1 : 0) + (south ? 1 : 0) + (west ? 1 : 0);

        return switch (connections) {
            case 0 -> SINGLE_SHAPE;
            case 1 -> {
                // Determine which double shape to use based on connection direction
                if (north) {
                    yield DOUBLE_SHAPE_NORTH;
                } else if (south) {
                    yield DOUBLE_SHAPE;
                } else if (east) {
                    yield DOUBLE_SHAPE_EAST_WEST;
                } else {
                    yield DOUBLE_SHAPE_WEST;
                }
            }
            case 2 -> {
                // Check if it's adjacent connections (corner) or opposite connections (center)
                if ((north && south) || (east && west)) {
                    // Determine which center shape to use based on connected sides
                    if (north && south) {
                        yield CENTER_SHAPE_NORTH_SOUTH;
                    } else {
                        yield CENTER_SHAPE_EAST_WEST;
                    }
                } else {
                    // Determine which corner shape to use based on connected sides
                    if (south && east) {
                        yield CORNER_SHAPE_SOUTH_EAST;
                    } else if (south && west) {
                        yield CORNER_SHAPE_SOUTH_WEST;
                    } else if (north && east) {
                        yield CORNER_SHAPE_NORTH_EAST;
                    } else {
                        yield CORNER_SHAPE_NORTH_WEST;
                    }
                }
            }
            case 3, 4 -> {
                // For multiple connections, determine the primary orientation
                if ((north && south) || (east && west)) {
                    // If we have opposite connections, use the appropriate center shape
                    if (north && south) {
                        yield CENTER_SHAPE_NORTH_SOUTH;
                    } else {
                        yield CENTER_SHAPE_EAST_WEST;
                    }
                } else {
                    // Default to north-south center for complex connections
                    yield CENTER_SHAPE_NORTH_SOUTH;
                }
            }
            default -> SINGLE_SHAPE;
        };
    }
} 