package com.westerosblocks.block.custom;

import com.google.common.collect.ImmutableMap;
import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.Map;

public class WCTableBlock extends Block implements WCBlockDef {
    protected BlockDefinition def;

    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    public static final EnumProperty<ConnectionType> CONNECTION = EnumProperty.of("connection", ConnectionType.class);

    private static final VoxelShape TABLE_SHAPE_NS = Block.createCuboidShape(0, 15, 0, 16, 16, 16);
    private static final VoxelShape TABLE_SHAPE_EW = Block.createCuboidShape(0, 15, 0, 16, 16, 16);

    private final Map<BlockState, VoxelShape> shapeByIndex;

    public WCTableBlock(Settings settings, BlockDefinition def) {
        super(settings);
        this.def = def;
        this.setDefaultState(this.getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(CONNECTION, ConnectionType.SINGLE));
        this.shapeByIndex = this.makeShapes();
    }

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(BlockDefinition definition) {
            Settings settings = definition.makeSettings();
            return new WCTableBlock(settings, definition);
        }
    }

    public enum ConnectionType implements StringIdentifiable {
        SINGLE("single"),   // No connections
        LEFT("left"),       // Left end (has neighbor to right)
        RIGHT("right"),     // Right end (has neighbor to left)
        MIDDLE("middle");   // Middle piece (neighbors both sides)

        private final String name;

        ConnectionType(String name) {
            this.name = name;
        }

        @Override
        public String asString() {
            return this.name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    private Map<BlockState, VoxelShape> makeShapes() {
        ImmutableMap.Builder<BlockState, VoxelShape> builder = ImmutableMap.builder();

        for (Direction facing : Direction.Type.HORIZONTAL) {
            VoxelShape shape = (facing == Direction.NORTH || facing == Direction.SOUTH)
                    ? TABLE_SHAPE_NS
                    : TABLE_SHAPE_EW;

            for (ConnectionType connection : ConnectionType.values()) {
                BlockState state = this.getDefaultState()
                        .with(FACING, facing)
                        .with(CONNECTION, connection);

                builder.put(state, shape);
            }
        }

        return builder.build();
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, CONNECTION);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction facing = ctx.getHorizontalPlayerFacing().getOpposite();
        BlockPos pos = ctx.getBlockPos();
        World world = ctx.getWorld();

        ConnectionType connectionType = getConnectionType(world, pos, facing);

        return this.getDefaultState()
                .with(FACING, facing)
                .with(CONNECTION, connectionType);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState,
            WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        // Only update if the change is on a horizontal axis
        if (direction.getAxis().isHorizontal()) {
            Direction facing = state.get(FACING);
            Direction left = getLeftDirection(facing);
            Direction right = getRightDirection(facing);

            // Only recalculate if the neighbor change is on our left or right
            if (direction == left || direction == right) {
                ConnectionType newConnection = getConnectionType(world, pos, facing);
                return state.with(CONNECTION, newConnection);
            }
        }

        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    /**
     * Determines the connection type based on adjacent tables.
     * Following user's requirements: neighbor to LEFT → this becomes RIGHT end
     */
    private ConnectionType getConnectionType(BlockView world, BlockPos pos, Direction facing) {
        Direction left = getLeftDirection(facing);
        Direction right = getRightDirection(facing);

        boolean hasLeft = canConnectTo(world, pos, left, facing);
        boolean hasRight = canConnectTo(world, pos, right, facing);

        if (hasLeft && hasRight) {
            return ConnectionType.MIDDLE;
        } else if (hasLeft) {
            // Neighbor to left → we're the RIGHT end
            return ConnectionType.RIGHT;
        } else if (hasRight) {
            // Neighbor to right → we're the LEFT end
            return ConnectionType.LEFT;
        } else {
            return ConnectionType.SINGLE;
        }
    }

    /**
     * Checks if this table can connect to a neighbor in the given direction
     */
    private boolean canConnectTo(BlockView world, BlockPos pos, Direction direction, Direction thisFacing) {
        BlockPos neighborPos = pos.offset(direction);
        BlockState neighborState = world.getBlockState(neighborPos);

        // Must be the same block type
        if (!neighborState.isOf(this)) {
            return false;
        }

        // Neighbor must have the same facing direction
        Direction neighborFacing = neighborState.get(FACING);
        return neighborFacing == thisFacing;
    }

    /**
     * Get the left direction relative to the facing direction
     */
    private Direction getLeftDirection(Direction facing) {
        return facing.rotateYCounterclockwise();
    }

    /**
     * Get the right direction relative to the facing direction
     */
    private Direction getRightDirection(Direction facing) {
        return facing.rotateYClockwise();
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
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
