package com.westerosblocks.block.custom;

import net.minecraft.block.*;
import net.minecraft.block.enums.WallShape;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import com.westerosblocks.data.BlockDefinition;

public class WCWallBlock extends WallBlock implements Waterloggable {

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(AbstractBlock.Settings settings, BlockDefinition definition) {
            boolean unconnect = definition != null && definition.isUnconnect();
            boolean connectState = definition != null && definition.isConnectState();
            String size = definition != null && definition.getWallSize() != null ? definition.getWallSize() : "normal";
            boolean toggleOnUse = definition != null && definition.toggleOnUse();

            return new WCWallBlock(settings, unconnect, connectState, size, toggleOnUse);
        }
    }

    public static final BooleanProperty UNCONNECT = BooleanProperty.of("unconnect");
    public static final IntProperty CONNECT_STATE = IntProperty.of("connectstate", 0, 3);

    private final boolean hasUnconnect;
    private final boolean hasConnectState;
    private final boolean toggleOnUse;
    private final WallSize wallSize;

    public enum WallSize {
        NORMAL(16.0f),
        SHORT(13.0f);

        public final float height;

        WallSize(float height) {
            this.height = height;
        }
    }

    // Cached shapes for performance
    private static VoxelShape[] normalShapes = null;
    private static VoxelShape[] shortShapes = null;
    private static VoxelShape[] collisionShapes = null;

    protected WCWallBlock(AbstractBlock.Settings settings, boolean unconnect, boolean connectState, String size, boolean toggleOnUse) {
        super(settings);
        this.hasUnconnect = unconnect;
        this.hasConnectState = connectState;
        this.toggleOnUse = toggleOnUse;
        this.wallSize = size.equals("short") ? WallSize.SHORT : WallSize.NORMAL;

        BlockState defaultState = this.stateManager.getDefaultState()
                .with(UP, true)
                .with(NORTH_SHAPE, WallShape.NONE)
                .with(EAST_SHAPE, WallShape.NONE)
                .with(SOUTH_SHAPE, WallShape.NONE)
                .with(WEST_SHAPE, WallShape.NONE)
                .with(WATERLOGGED, false);

        if (hasUnconnect) {
            defaultState = defaultState.with(UNCONNECT, false);
        }
        if (hasConnectState) {
            defaultState = defaultState.with(CONNECT_STATE, 0);
        }

        this.setDefaultState(defaultState);

        // Initialize shapes if not already done
        if (normalShapes == null) {
            normalShapes = makeShapes(4.0f, 3.0f, 16.0f, 0.0f, 16.0f, 16.0f);
        }
        if (shortShapes == null) {
            shortShapes = makeShapes(4.0f, 3.0f, 16.0f, 0.0f, 13.0f, 16.0f);
        }
        if (collisionShapes == null) {
            collisionShapes = makeShapes(4.0f, 3.0f, 24.0f, 0.0f, 24.0f, 24.0f);
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        if (hasUnconnect) {
            builder.add(UNCONNECT);
        }
        if (hasConnectState) {
            builder.add(CONNECT_STATE);
        }
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        VoxelShape[] shapes = (wallSize == WallSize.SHORT) ? shortShapes : normalShapes;
        return shapes[getShapeIndex(state)];
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return collisionShapes[getShapeIndex(state)];
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        if (hasUnconnect && state.get(UNCONNECT)) {
            return state;
        }

        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (toggleOnUse && hasConnectState && player.isCreative() && player.getMainHandStack().isEmpty()) {
            int currentState = state.get(CONNECT_STATE);
            int nextState = (currentState + 1) % 4;
            world.setBlockState(pos, state.with(CONNECT_STATE, nextState), Block.NOTIFY_ALL);
            world.syncWorldEvent(player, 1006, pos, 0);
            return ActionResult.success(world.isClient);
        }
        return ActionResult.PASS;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        switch (rotation) {
            case CLOCKWISE_180:
                return state.with(NORTH_SHAPE, state.get(SOUTH_SHAPE))
                        .with(EAST_SHAPE, state.get(WEST_SHAPE))
                        .with(SOUTH_SHAPE, state.get(NORTH_SHAPE))
                        .with(WEST_SHAPE, state.get(EAST_SHAPE));
            case COUNTERCLOCKWISE_90:
                return state.with(NORTH_SHAPE, state.get(EAST_SHAPE))
                        .with(EAST_SHAPE, state.get(SOUTH_SHAPE))
                        .with(SOUTH_SHAPE, state.get(WEST_SHAPE))
                        .with(WEST_SHAPE, state.get(NORTH_SHAPE));
            case CLOCKWISE_90:
                return state.with(NORTH_SHAPE, state.get(WEST_SHAPE))
                        .with(EAST_SHAPE, state.get(NORTH_SHAPE))
                        .with(SOUTH_SHAPE, state.get(EAST_SHAPE))
                        .with(WEST_SHAPE, state.get(SOUTH_SHAPE));
            default:
                return state;
        }
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        switch (mirror) {
            case LEFT_RIGHT:
                return state.with(NORTH_SHAPE, state.get(SOUTH_SHAPE))
                        .with(SOUTH_SHAPE, state.get(NORTH_SHAPE));
            case FRONT_BACK:
                return state.with(EAST_SHAPE, state.get(WEST_SHAPE))
                        .with(WEST_SHAPE, state.get(EAST_SHAPE));
            default:
                return super.mirror(state, mirror);
        }
    }

    private static int getShapeIndex(BlockState state) {
        int up = state.get(UP) ? 1 : 0;
        int north = state.get(NORTH_SHAPE).ordinal();
        int east = state.get(EAST_SHAPE).ordinal();
        int south = state.get(SOUTH_SHAPE).ordinal();
        int west = state.get(WEST_SHAPE).ordinal();

        return up + (east * 2) + (west * 6) + (north * 18) + (south * 54);
    }

    private static VoxelShape[] makeShapes(float postWidth, float sideWidth, float postHeight,
                                         float minY, float sideHeight, float tallSideHeight) {
        float halfPost = 8.0f - postWidth;
        float halfPostEnd = 8.0f + postWidth;
        float halfSide = 8.0f - sideWidth;
        float halfSideEnd = 8.0f + sideWidth;

        // Create base shapes
        VoxelShape postShape = Block.createCuboidShape(halfPost, 0.0, halfPost, halfPostEnd, postHeight, halfPostEnd);
        VoxelShape northLowShape = Block.createCuboidShape(halfSide, minY, 0.0, halfSideEnd, sideHeight, halfSideEnd);
        VoxelShape southLowShape = Block.createCuboidShape(halfSide, minY, halfSide, halfSideEnd, sideHeight, 16.0);
        VoxelShape westLowShape = Block.createCuboidShape(0.0, minY, halfSide, halfSideEnd, sideHeight, halfSideEnd);
        VoxelShape eastLowShape = Block.createCuboidShape(halfSide, minY, halfSide, 16.0, sideHeight, halfSideEnd);
        VoxelShape northTallShape = Block.createCuboidShape(halfSide, minY, 0.0, halfSideEnd, tallSideHeight, halfSideEnd);
        VoxelShape southTallShape = Block.createCuboidShape(halfSide, minY, halfSide, halfSideEnd, tallSideHeight, 16.0);
        VoxelShape westTallShape = Block.createCuboidShape(0.0, minY, halfSide, halfSideEnd, tallSideHeight, halfSideEnd);
        VoxelShape eastTallShape = Block.createCuboidShape(halfSide, minY, halfSide, 16.0, tallSideHeight, halfSideEnd);

        VoxelShape[] shapes = new VoxelShape[2 * 3 * 3 * 3 * 3]; // up * north * east * south * west

        for (Boolean up : UP.getValues()) {
            for (WallShape north : NORTH_SHAPE.getValues()) {
                for (WallShape east : EAST_SHAPE.getValues()) {
                    for (WallShape south : SOUTH_SHAPE.getValues()) {
                        for (WallShape west : WEST_SHAPE.getValues()) {
                            VoxelShape shape = VoxelShapes.empty();

                            // Add side shapes
                            shape = applyWallShape(shape, east, eastLowShape, eastTallShape);
                            shape = applyWallShape(shape, west, westLowShape, westTallShape);
                            shape = applyWallShape(shape, north, northLowShape, northTallShape);
                            shape = applyWallShape(shape, south, southLowShape, southTallShape);

                            // Add post if up
                            if (up) {
                                shape = VoxelShapes.union(shape, postShape);
                            }

                            int index = (up ? 1 : 0) + (east.ordinal() * 2) + (west.ordinal() * 6) +
                                      (north.ordinal() * 18) + (south.ordinal() * 54);
                            shapes[index] = shape;
                        }
                    }
                }
            }
        }

        return shapes;
    }

    private static VoxelShape applyWallShape(VoxelShape base, WallShape wallShape, VoxelShape lowShape, VoxelShape tallShape) {
        if (wallShape == WallShape.TALL) {
            return VoxelShapes.union(base, tallShape);
        } else if (wallShape == WallShape.LOW) {
            return VoxelShapes.union(base, lowShape);
        } else {
            return base;
        }
    }
}