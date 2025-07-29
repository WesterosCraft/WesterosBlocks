package com.westerosblocks.block.custom;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

import java.util.List;
import java.util.Map;

public class WCBranchBlock extends Block implements Waterloggable {
    public static final BooleanProperty NORTH = Properties.NORTH;
    public static final BooleanProperty EAST = Properties.EAST;
    public static final BooleanProperty SOUTH = Properties.SOUTH;
    public static final BooleanProperty WEST = Properties.WEST;
    public static final BooleanProperty UP = Properties.UP;
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    // Define base shapes for branch components
    private static final VoxelShape BRANCH_CENTER = Block.createCuboidShape(4, 0, 4, 12, 16, 12);
    private static final VoxelShape BRANCH_NORTH = Block.createCuboidShape(4, 8, 0, 12, 12, 8);
    private static final VoxelShape BRANCH_EAST = Block.createCuboidShape(8, 8, 4, 16, 12, 12);
    private static final VoxelShape BRANCH_SOUTH = Block.createCuboidShape(4, 8, 8, 12, 12, 16);
    private static final VoxelShape BRANCH_WEST = Block.createCuboidShape(0, 8, 4, 8, 12, 12);
    private static final VoxelShape BRANCH_UP = Block.createCuboidShape(4, 16, 4, 12, 20, 12);

    // Pre-computed shape maps for efficient lookups
    private final Map<BlockState, VoxelShape> shapeByIndex;

    private final String blockName;
    private final String creativeTab;
    private final String woodType;
    private final String branchType;

    public WCBranchBlock(AbstractBlock.Settings settings, String blockName, String creativeTab, String woodType,
            String branchType) {
        super(settings);
        this.blockName = blockName;
        this.creativeTab = creativeTab;
        this.woodType = woodType;
        this.branchType = branchType;
        this.setDefaultState(this.getDefaultState()
                .with(NORTH, false)
                .with(EAST, false)
                .with(SOUTH, false)
                .with(WEST, false)
                .with(UP, false)
                .with(WATERLOGGED, false));

        // Pre-compute all possible shape combinations
        this.shapeByIndex = this.makeShapes();
    }

    private Map<BlockState, VoxelShape> makeShapes() {
        ImmutableMap.Builder<BlockState, VoxelShape> builder = ImmutableMap.builder();

        // Generate all possible state combinations
        for (boolean north : new boolean[] { false, true }) {
            for (boolean east : new boolean[] { false, true }) {
                for (boolean south : new boolean[] { false, true }) {
                    for (boolean west : new boolean[] { false, true }) {
                        for (boolean up : new boolean[] { false, true }) {
                            VoxelShape shape = this.getShapeForConnections(north, east, south, west, up);

                            // Add both waterlogged and non-waterlogged states
                            BlockState state = this.getDefaultState()
                                    .with(NORTH, north)
                                    .with(EAST, east)
                                    .with(SOUTH, south)
                                    .with(WEST, west)
                                    .with(UP, up);

                            builder.put(state.with(WATERLOGGED, false), shape);
                            builder.put(state.with(WATERLOGGED, true), shape);
                        }
                    }
                }
            }
        }

        return builder.build();
    }

    private VoxelShape getShapeForConnections(boolean north, boolean east, boolean south, boolean west, boolean up) {
        // If UP is false and there are horizontal connections, use horizontal shape
        if (!up && (north || east || south || west)) {
            // Horizontal model extends from z=0 to z=16 at y=8-16
            return Block.createCuboidShape(4, 8, 0, 12, 16, 16);
        }

        // Otherwise use the standard vertical shape
        VoxelShape shape = BRANCH_CENTER;

        if (north) {
            shape = VoxelShapes.union(shape, BRANCH_NORTH);
        }
        if (east) {
            shape = VoxelShapes.union(shape, BRANCH_EAST);
        }
        if (south) {
            shape = VoxelShapes.union(shape, BRANCH_SOUTH);
        }
        if (west) {
            shape = VoxelShapes.union(shape, BRANCH_WEST);
        }
        if (up) {
            // Add vertical extension for UP connection
            shape = VoxelShapes.union(shape, BRANCH_UP);
        }

        return shape;
    }

    public String getBlockName() {
        return blockName;
    }

    public String getCreativeTab() {
        return creativeTab;
    }

    public String getWoodType() {
        return woodType;
    }

    public String getBranchType() {
        return branchType;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        FluidState fluidstate = ctx.getWorld().getFluidState(ctx.getBlockPos());
        BlockState defaultState = this.getDefaultState()
                .with(WATERLOGGED, fluidstate.isIn(FluidTags.WATER));

        // Check connections in all directions
        BlockView world = ctx.getWorld();
        BlockPos pos = ctx.getBlockPos();

        boolean north = this.canConnect(world.getBlockState(pos.north()),
                world.getBlockState(pos.north()).isSideSolidFullSquare(world, pos.north(), Direction.SOUTH),
                Direction.SOUTH);
        boolean east = this.canConnect(world.getBlockState(pos.east()),
                world.getBlockState(pos.east()).isSideSolidFullSquare(world, pos.east(), Direction.WEST),
                Direction.WEST);
        boolean south = this.canConnect(world.getBlockState(pos.south()),
                world.getBlockState(pos.south()).isSideSolidFullSquare(world, pos.south(), Direction.NORTH),
                Direction.NORTH);
        boolean west = this.canConnect(world.getBlockState(pos.west()),
                world.getBlockState(pos.west()).isSideSolidFullSquare(world, pos.west(), Direction.EAST),
                Direction.EAST);
        boolean up = this.canConnect(world.getBlockState(pos.up()),
                world.getBlockState(pos.up()).isSideSolidFullSquare(world, pos.up(), Direction.DOWN), Direction.DOWN);

        return defaultState
                .with(NORTH, north)
                .with(EAST, east)
                .with(SOUTH, south)
                .with(WEST, west)
                .with(UP, up);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState,
            WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        if (direction.getAxis().isHorizontal()) {
            boolean isConnected = this.canConnect(neighborState,
                    neighborState.isSideSolidFullSquare(world, neighborPos, direction.getOpposite()),
                    direction.getOpposite());
            return state.with(getPropertyForDirection(direction), isConnected);
        } else if (direction == Direction.UP) {
            boolean isConnected = this.canConnect(neighborState,
                    neighborState.isSideSolidFullSquare(world, neighborPos, Direction.DOWN), Direction.DOWN);
            return state.with(UP, isConnected);
        }

        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    private boolean canConnect(BlockState state, boolean neighborIsFullSquare, Direction dir) {
        Block block = state.getBlock();

        // Connect to other branch blocks
        if (block instanceof WCBranchBlock) {
            return true;
        }

        // Connect to solid blocks (like walls, logs, etc.)
        if (!Block.cannotConnect(state) && neighborIsFullSquare) {
            return true;
        }

        // Connect to specific block types - add more as needed
        if (state.isIn(BlockTags.LOGS)) {
            return true;
        }

        return false;
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
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, UP, WATERLOGGED);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return this.shapeByIndex.get(state);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return this.shapeByIndex.get(state);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }
}