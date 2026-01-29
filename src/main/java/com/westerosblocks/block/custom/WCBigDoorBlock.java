package com.westerosblocks.block.custom;

import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.GameEvent;

public class WCBigDoorBlock extends Block {
    protected BlockDefinition def;

    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final BooleanProperty OPEN = Properties.OPEN;
    public static final EnumProperty<BigDoorPart> PART = EnumProperty.of("part", BigDoorPart.class);

    // Door thickness: 3 pixels (0.1875 blocks)
    private static final double THICKNESS = 3.0;

    // Collision shapes for closed door (thin wall)
    protected static final VoxelShape NORTH_SHAPE = Block.createCuboidShape(0.0, 0.0, 13.0, 16.0, 16.0, 16.0);
    protected static final VoxelShape SOUTH_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 3.0);
    protected static final VoxelShape EAST_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 3.0, 16.0, 16.0);
    protected static final VoxelShape WEST_SHAPE = Block.createCuboidShape(13.0, 0.0, 0.0, 16.0, 16.0, 16.0);

    // Open shapes for side columns (door swings inward)
    // Left column when open - door panel on the left side
    protected static final VoxelShape NORTH_OPEN_LEFT = Block.createCuboidShape(0.0, 0.0, 0.0, 3.0, 16.0, 16.0);
    protected static final VoxelShape SOUTH_OPEN_LEFT = Block.createCuboidShape(13.0, 0.0, 0.0, 16.0, 16.0, 16.0);
    protected static final VoxelShape EAST_OPEN_LEFT = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 3.0);
    protected static final VoxelShape WEST_OPEN_LEFT = Block.createCuboidShape(0.0, 0.0, 13.0, 16.0, 16.0, 16.0);

    // Right column when open - door panel on the right side
    protected static final VoxelShape NORTH_OPEN_RIGHT = Block.createCuboidShape(13.0, 0.0, 0.0, 16.0, 16.0, 16.0);
    protected static final VoxelShape SOUTH_OPEN_RIGHT = Block.createCuboidShape(0.0, 0.0, 0.0, 3.0, 16.0, 16.0);
    protected static final VoxelShape EAST_OPEN_RIGHT = Block.createCuboidShape(0.0, 0.0, 13.0, 16.0, 16.0, 16.0);
    protected static final VoxelShape WEST_OPEN_RIGHT = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 3.0);

    private final boolean locked;

    /**
     * Enum representing the 9 positions in the 3x3 door grid
     */
    public enum BigDoorPart implements StringIdentifiable {
        BOTTOM_LEFT("bottom_left", 0, 0),
        BOTTOM_CENTER("bottom_center", 1, 0),
        BOTTOM_RIGHT("bottom_right", 2, 0),
        MIDDLE_LEFT("middle_left", 0, 1),
        MIDDLE_CENTER("middle_center", 1, 1),
        MIDDLE_RIGHT("middle_right", 2, 1),
        TOP_LEFT("top_left", 0, 2),
        TOP_CENTER("top_center", 1, 2),
        TOP_RIGHT("top_right", 2, 2);

        private final String name;
        private final int horizontalOffset; // 0=left, 1=center, 2=right
        private final int verticalOffset;   // 0=bottom, 1=middle, 2=top

        BigDoorPart(String name, int horizontalOffset, int verticalOffset) {
            this.name = name;
            this.horizontalOffset = horizontalOffset;
            this.verticalOffset = verticalOffset;
        }

        @Override
        public String asString() {
            return this.name;
        }

        public int getHorizontalOffset() {
            return horizontalOffset;
        }

        public int getVerticalOffset() {
            return verticalOffset;
        }

        /**
         * Returns true if this part is in the center column (passable when open)
         */
        public boolean isCenterColumn() {
            return horizontalOffset == 1;
        }

        /**
         * Returns true if this part is in the left column
         */
        public boolean isLeftColumn() {
            return horizontalOffset == 0;
        }

        /**
         * Returns true if this part is in the right column
         */
        public boolean isRightColumn() {
            return horizontalOffset == 2;
        }

        /**
         * Get the world offset from the origin (bottom_center) for this part, given a facing direction.
         * The door is placed with bottom_center at the clicked position.
         */
        public Vec3i getOffset(Direction facing) {
            // Horizontal offset: -1 for left, 0 for center, 1 for right (relative to facing)
            int relativeHorizontal = horizontalOffset - 1;
            // Vertical offset is always Y
            int y = verticalOffset;

            // Calculate X and Z based on facing
            Direction left = facing.rotateYCounterclockwise();
            int x = left.getOffsetX() * relativeHorizontal;
            int z = left.getOffsetZ() * relativeHorizontal;

            return new Vec3i(x, y, z);
        }

        /**
         * Get the part at the given offsets
         */
        public static BigDoorPart fromOffsets(int horizontal, int vertical) {
            for (BigDoorPart part : values()) {
                if (part.horizontalOffset == horizontal && part.verticalOffset == vertical) {
                    return part;
                }
            }
            return BOTTOM_CENTER; // fallback
        }
    }

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(BlockDefinition definition) {
            AbstractBlock.Settings settings = definition.makeSettings();
            boolean locked = definition.isLocked();
            return new WCBigDoorBlock(settings, definition, locked);
        }
    }

    public WCBigDoorBlock(Settings settings, BlockDefinition def, boolean locked) {
        super(settings.nonOpaque());
        this.def = def;
        this.locked = locked;

        this.setDefaultState(this.stateManager.getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(OPEN, false)
                .with(PART, BigDoorPart.BOTTOM_CENTER));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, OPEN, PART);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return getCollisionShape(state, world, pos, context);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction facing = state.get(FACING);
        boolean open = state.get(OPEN);
        BigDoorPart part = state.get(PART);

        if (!open) {
            // Closed: all blocks have the thin wall shape
            return getClosedShape(facing);
        } else {
            // Open: center column is passable, side columns have door panels
            // Left column gets right shape (door swings to outer edge)
            // Right column gets left shape (door swings to outer edge)
            if (part.isCenterColumn()) {
                return VoxelShapes.empty();
            } else if (part.isLeftColumn()) {
                return getOpenRightShape(facing);
            } else {
                return getOpenLeftShape(facing);
            }
        }
    }

    private VoxelShape getClosedShape(Direction facing) {
        return switch (facing) {
            case NORTH -> NORTH_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            case EAST -> EAST_SHAPE;
            case WEST -> WEST_SHAPE;
            default -> NORTH_SHAPE;
        };
    }

    private VoxelShape getOpenLeftShape(Direction facing) {
        return switch (facing) {
            case NORTH -> NORTH_OPEN_LEFT;
            case SOUTH -> SOUTH_OPEN_LEFT;
            case EAST -> EAST_OPEN_LEFT;
            case WEST -> WEST_OPEN_LEFT;
            default -> NORTH_OPEN_LEFT;
        };
    }

    private VoxelShape getOpenRightShape(Direction facing) {
        return switch (facing) {
            case NORTH -> NORTH_OPEN_RIGHT;
            case SOUTH -> SOUTH_OPEN_RIGHT;
            case EAST -> EAST_OPEN_RIGHT;
            case WEST -> WEST_OPEN_RIGHT;
            default -> NORTH_OPEN_RIGHT;
        };
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockPos pos = ctx.getBlockPos();
        World world = ctx.getWorld();
        Direction facing = ctx.getHorizontalPlayerFacing();

        // Check if all 9 positions are available
        if (!canPlaceAt(world, pos, facing, ctx)) {
            return null;
        }

        return this.getDefaultState()
                .with(FACING, facing)
                .with(OPEN, false)
                .with(PART, BigDoorPart.BOTTOM_CENTER);
    }

    /**
     * Check if the 3x3 area is free for placement
     */
    private boolean canPlaceAt(World world, BlockPos origin, Direction facing, ItemPlacementContext ctx) {
        for (BigDoorPart part : BigDoorPart.values()) {
            Vec3i offset = part.getOffset(facing);
            BlockPos checkPos = origin.add(offset);

            if (!world.getBlockState(checkPos).canReplace(ctx)) {
                return false;
            }
            if (!world.getWorldBorder().contains(checkPos)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        if (world.isClient()) {
            return;
        }

        Direction facing = state.get(FACING);

        // Place all 9 blocks
        for (BigDoorPart part : BigDoorPart.values()) {
            if (part == BigDoorPart.BOTTOM_CENTER) {
                continue; // Already placed
            }

            Vec3i offset = part.getOffset(facing);
            BlockPos partPos = pos.add(offset);

            world.setBlockState(partPos, state.with(PART, part), Block.NOTIFY_ALL);
        }
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient()) {
            Direction facing = state.get(FACING);
            BigDoorPart thisPart = state.get(PART);

            // Find the origin (bottom_center) position
            Vec3i thisOffset = thisPart.getOffset(facing);
            BlockPos origin = pos.add(-thisOffset.getX(), -thisOffset.getY(), -thisOffset.getZ());

            // Remove all other parts
            for (BigDoorPart part : BigDoorPart.values()) {
                Vec3i offset = part.getOffset(facing);
                BlockPos partPos = origin.add(offset);

                if (!partPos.equals(pos)) {
                    BlockState partState = world.getBlockState(partPos);
                    if (partState.isOf(this)) {
                        world.setBlockState(partPos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL | Block.SKIP_DROPS);
                        world.syncWorldEvent(player, 2001, partPos, Block.getRawIdFromState(partState));
                    }
                }
            }
        }

        return super.onBreak(world, pos, state, player);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState,
                                                WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        // Check if a neighboring part was removed
        Direction facing = state.get(FACING);
        BigDoorPart thisPart = state.get(PART);

        // Calculate where the neighbor should be
        Vec3i thisOffset = thisPart.getOffset(facing);
        BlockPos origin = pos.add(-thisOffset.getX(), -thisOffset.getY(), -thisOffset.getZ());

        // Check if the neighborPos is one of our parts
        for (BigDoorPart part : BigDoorPart.values()) {
            Vec3i offset = part.getOffset(facing);
            BlockPos expectedPos = origin.add(offset);

            if (expectedPos.equals(neighborPos)) {
                // This neighbor should be part of our door
                if (!neighborState.isOf(this)) {
                    // Part was removed, destroy this block too
                    return Blocks.AIR.getDefaultState();
                }
            }
        }

        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (this.locked) {
            return ActionResult.PASS;
        }

        if (world.isClient()) {
            return ActionResult.SUCCESS;
        }

        // Toggle all 9 blocks
        toggleDoor(world, pos, state, player);
        return ActionResult.CONSUME;
    }

    private void toggleDoor(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        Direction facing = state.get(FACING);
        BigDoorPart thisPart = state.get(PART);
        boolean newOpen = !state.get(OPEN);

        // Find the origin
        Vec3i thisOffset = thisPart.getOffset(facing);
        BlockPos origin = pos.add(-thisOffset.getX(), -thisOffset.getY(), -thisOffset.getZ());

        // Update all 9 parts
        for (BigDoorPart part : BigDoorPart.values()) {
            Vec3i offset = part.getOffset(facing);
            BlockPos partPos = origin.add(offset);

            BlockState partState = world.getBlockState(partPos);
            if (partState.isOf(this)) {
                world.setBlockState(partPos, partState.with(OPEN, newOpen), Block.NOTIFY_LISTENERS | Block.REDRAW_ON_MAIN_THREAD);
            }
        }

        // Play sound and emit game event
        playToggleSound(world, origin, newOpen);
        world.emitGameEvent(player, newOpen ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, origin);
    }

    private void playToggleSound(World world, BlockPos pos, boolean open) {
        world.playSound(
                null,
                pos,
                open ? SoundEvents.BLOCK_WOODEN_DOOR_OPEN : SoundEvents.BLOCK_WOODEN_DOOR_CLOSE,
                SoundCategory.BLOCKS,
                1.0F,
                world.getRandom().nextFloat() * 0.1F + 0.9F
        );
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    public BlockDefinition getDefinition() {
        return def;
    }
}
