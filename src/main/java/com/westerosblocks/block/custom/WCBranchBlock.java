package com.westerosblocks.block.custom;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

import java.util.Map;

import com.westerosblocks.data.BlockDefinition;

public class WCBranchBlock extends Block implements Waterloggable {
    protected BlockDefinition def;
    public static final BooleanProperty NORTH = Properties.NORTH;
    public static final BooleanProperty EAST = Properties.EAST;
    public static final BooleanProperty SOUTH = Properties.SOUTH;
    public static final BooleanProperty WEST = Properties.WEST;
    public static final BooleanProperty UP = Properties.UP;
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    private static final VoxelShape BRANCH_CENTER = Block.createCuboidShape(4, 0, 4, 12, 16, 12);
    private static final VoxelShape BRANCH_NORTH = Block.createCuboidShape(4, 8, 0, 12, 12, 8);
    private static final VoxelShape BRANCH_EAST = Block.createCuboidShape(8, 8, 4, 16, 12, 12);
    private static final VoxelShape BRANCH_SOUTH = Block.createCuboidShape(4, 8, 8, 12, 12, 16);
    private static final VoxelShape BRANCH_WEST = Block.createCuboidShape(0, 8, 4, 8, 12, 12);
    private static final VoxelShape BRANCH_UP = Block.createCuboidShape(4, 16, 4, 12, 16, 12);

    // Pre-computed shape maps for efficient lookups
    private final Map<BlockState, VoxelShape> shapeByIndex;
    private final String woodType;
    private final String branchType;

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(BlockDefinition definition) {
            // Handle null definition for manual block creation
            AbstractBlock.Settings settings = definition != null
                    ? definition.makeSettings()
                    : AbstractBlock.Settings.create();
            String woodType = definition != null ? definition.getWoodType() : "oak";
            // Using blockType as branchType since branchType getter doesn't exist yet
            String branchType = (definition != null && definition.getBlockType() != null)
                    ? definition.getBlockType()
                    : "large_branch";
            return new WCBranchBlock(settings, definition, woodType, branchType);
        }

        @Override
        public Block buildBlockClass(AbstractBlock.Settings settings, Map<String, Object> parameters) {
            String woodType = (String) parameters.getOrDefault("woodType", "oak");
            String branchType = (String) parameters.getOrDefault("branchType", "large_branch");
            return new WCBranchBlock(settings, null, woodType, branchType);
        }
    }

    public WCBranchBlock(AbstractBlock.Settings settings, BlockDefinition def, String woodType, String branchType) {
        super(settings);
        this.def = def;
        this.woodType = woodType;
        this.branchType = branchType;
        this.setDefaultState(this.getDefaultState()
                .with(NORTH, false)
                .with(EAST, false)
                .with(SOUTH, false)
                .with(WEST, false)
                .with(UP, false)
                .with(WATERLOGGED, false));
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
        // If all neighbors are false, use the large_branch model (just the center)
        if (!north && !east && !south && !west && !up) {
            return BRANCH_CENTER;
        }

        // If UP is false (no branch below), use horizontal shape
        if (!up) {
            // Horizontal model extends from z=0 to z=16 at y=8-16
            return Block.createCuboidShape(4, 8, 0, 12, 16, 16);
        }

        // Otherwise use the standard vertical shape (UP is true, has branch below)
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
            shape = VoxelShapes.union(shape, BRANCH_UP);
        }

        return shape;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return this.getShapeForConnections(
                state.get(NORTH),
                state.get(EAST),
                state.get(SOUTH),
                state.get(WEST),
                state.get(UP));
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return this.getOutlineShape(state, world, pos, context);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, UP, WATERLOGGED);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        FluidState fluidstate = ctx.getWorld().getFluidState(ctx.getBlockPos());
        BlockPos pos = ctx.getBlockPos();
        boolean hasSolidBlockBelow = !ctx.getWorld().getBlockState(pos.down()).isAir();

        return this.getDefaultState()
                .with(WATERLOGGED, fluidstate.isIn(FluidTags.WATER))
                .with(UP, hasSolidBlockBelow);
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
        } else if (direction == Direction.DOWN) {
            // Check if there's a solid block below (not in air)
            boolean hasSolidBlockBelow = !neighborState.isAir();

            // UP should be false if there's air below, true if there's a solid block below
            return state.with(UP, hasSolidBlockBelow);
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
        return state.isIn(BlockTags.LOGS);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    private BooleanProperty getPropertyForDirection(Direction direction) {
        return switch (direction) {
            case NORTH -> NORTH;
            case SOUTH -> SOUTH;
            case WEST -> WEST;
            case EAST -> EAST;
            case UP -> UP;
            default -> throw new IllegalArgumentException("Invalid direction: " + direction);
        };
    }

    private boolean canConnect(BlockState state) {
        return state.isIn(BlockTags.LOGS);
    }

    public String getBranchType() {
        return branchType;
    }

    /**
     * Gets the BlockDefinition for this block.
     * @return BlockDefinition if block was created from JSON, null if created programmatically
     */
    public BlockDefinition getDefinition() {
        return def;
    }
}
