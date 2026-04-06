package com.westerosblocks.block.custom;

import com.google.common.collect.ImmutableMap;
import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.entity.ModEntities;
import com.westerosblocks.entity.custom.ChairEntity;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Direction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;
import net.minecraft.util.math.random.Random;

import java.util.List;
import java.util.Map;

public class WCBenchBlock extends Block implements WCBlockDef {
    protected BlockDefinition def;

    public static final EnumProperty<Direction> FACING = HorizontalFacingBlock.FACING;
    public static final EnumProperty<ConnectionType> CONNECTION = EnumProperty.of("connection", ConnectionType.class);
    private static final VoxelShape BENCH_SHAPE_NS = Block.createCuboidShape(1, 6, 3, 15, 8, 13);
    private static final VoxelShape BENCH_SHAPE_EW = Block.createCuboidShape(3, 6, 1, 13, 8, 15);

    private final Map<BlockState, VoxelShape> shapeByIndex;

    public WCBenchBlock(Settings settings, BlockDefinition def) {
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
            AbstractBlock.Settings settings = definition.makeSettings();
            return new WCBenchBlock(settings, definition);
        }
    }

    public enum ConnectionType implements StringIdentifiable {
        SINGLE("single"),   // No connections
        LEFT("left"),       // Left end (connects on right)
        RIGHT("right"),     // Right end (connects on left)
        MIDDLE("middle");   // Middle piece (connects on both sides)

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
                    ? BENCH_SHAPE_NS
                    : BENCH_SHAPE_EW;

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
    protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView,
            BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
        // Only update if the change is on our left or right side
        if (direction.getAxis().isHorizontal()) {
            Direction facing = state.get(FACING);
            Direction left = facing.rotateYCounterclockwise();
            Direction right = facing.rotateYClockwise();

            // Only recalculate if the neighbor change is on our left or right
            if (direction == left || direction == right) {
                ConnectionType newConnection = getConnectionType(world, pos, facing);
                return state.with(CONNECTION, newConnection);
            }
        }

        return super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random);
    }

    private ConnectionType getConnectionType(BlockView world, BlockPos pos, Direction facing) {
        Direction left = facing.rotateYCounterclockwise();
        Direction right = facing.rotateYClockwise();

        boolean connectsLeft = canConnectTo(world, pos, left, facing);
        boolean connectsRight = canConnectTo(world, pos, right, facing);

        if (connectsLeft && connectsRight) {
            return ConnectionType.MIDDLE;
        } else if (connectsLeft) {
            // Connects on left, so this is the right end
            return ConnectionType.RIGHT;
        } else if (connectsRight) {
            // Connects on right, so this is the left end
            return ConnectionType.LEFT;
        } else {
            return ConnectionType.SINGLE;
        }
    }

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

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient()) {
            Entity entity = null;
            List<ChairEntity> entities = world.getEntitiesByType(ModEntities.CHAIR, new Box(pos), chair -> true);
            if (entities.isEmpty()) {
                entity = ModEntities.CHAIR.spawn((ServerWorld) world, pos, SpawnReason.TRIGGERED);
            } else {
                entity = entities.get(0);
            }

            if (entity != null) {
                player.startRiding(entity);
            }
        }

        return ActionResult.SUCCESS;
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
