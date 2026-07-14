package com.westerosblocks.block.custom;

import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.*;
import net.minecraft.block.enums.DoorHinge;
import net.minecraft.block.piston.PistonBehavior;
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
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

/**
 * A 1-wide x 3-tall door that behaves exactly like a vanilla door: a single
 * rigid panel hinged on one vertical edge (chosen at placement) that snaps
 * between closed and open (no smooth animation). Rendered entirely with
 * blockstate models — no block entity.
 *
 * <p>Multiblock: 3 vertically stacked cells. The origin cell is {@code BOTTOM}.
 * Closed leaves a thin slab on each cell's FACING face; open leaves a thin slab
 * on each cell's hinge face — both are vanilla DoorBlock's shape rules, just
 * spanning 3 cells instead of 2.
 */
public class WCTallDoorBlock extends Block implements WCBlockDef {
    protected BlockDefinition def;

    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final BooleanProperty OPEN = Properties.OPEN;
    public static final EnumProperty<DoorHinge> HINGE = Properties.DOOR_HINGE;
    public static final EnumProperty<TallDoorPart> PART = EnumProperty.of("part", TallDoorPart.class);

    private final boolean locked;

    // Door collision: a thin 3-pixel slab on each cardinal face. Closed uses the
    // FACING-face slab on every cell; open uses the hinge-face slab on every
    // cell. Both are in-cell, matching vanilla DoorBlock's shapes exactly.
    private static final VoxelShape NORTH_SLAB = Block.createCuboidShape(0, 0, 0, 16, 16, 3);
    private static final VoxelShape SOUTH_SLAB = Block.createCuboidShape(0, 0, 13, 16, 16, 16);
    private static final VoxelShape WEST_SLAB = Block.createCuboidShape(0, 0, 0, 3, 16, 16);
    private static final VoxelShape EAST_SLAB = Block.createCuboidShape(13, 0, 0, 16, 16, 16);

    // Suppresses the AIR-return in getStateForNeighborUpdate while onBreak is
    // tearing the multiblock down (mirrors WCBigDoorBlock).
    private static final ThreadLocal<Boolean> CLEANUP_IN_PROGRESS = ThreadLocal.withInitial(() -> false);

    public enum TallDoorPart implements StringIdentifiable {
        BOTTOM("bottom", 0),
        MIDDLE("middle", 1),
        TOP("top", 2);

        private final String name;
        private final int row; // 0 bottom, 1 middle, 2 top

        TallDoorPart(String name, int row) {
            this.name = name;
            this.row = row;
        }

        @Override
        public String asString() {
            return this.name;
        }

        public int getRow() {
            return row;
        }
    }

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(BlockDefinition definition) {
            return new WCTallDoorBlock(definition.makeSettings(), definition, definition.isLocked());
        }
    }

    public WCTallDoorBlock(Settings settings, BlockDefinition def, boolean locked) {
        super(settings.nonOpaque().pistonBehavior(PistonBehavior.BLOCK));
        this.def = def;
        this.locked = locked;

        this.setDefaultState(this.stateManager.getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(OPEN, false)
                .with(HINGE, DoorHinge.LEFT)
                .with(PART, TallDoorPart.BOTTOM));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, OPEN, HINGE, PART);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return getCollisionShape(state, world, pos, context);
    }

    // Ports 1.18.2's doorNoConnect=true behavior: no sturdy faces, so
    // walls/fences/panes never connect to doors regardless of open state.
    @Override
    public VoxelShape getSidesShape(BlockState state, BlockView world, BlockPos pos) {
        return VoxelShapes.empty();
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction facing = state.get(FACING);
        if (!state.get(OPEN)) {
            // Closed: a thin slab on the facing.opposite face of every cell
            // (matches vanilla DoorBlock's closed slab).
            return slab(facing.getOpposite());
        }
        // Open: the panel has swung 90° about its hinge edge and now sits flush
        // against the hinge-side face of the same cell (matches vanilla).
        return slab(openFace(state.get(HINGE), facing));
    }

    /**
     * The world face the open panel sits against — exactly vanilla DoorBlock's
     * rule: LEFT hinge -> facing.rotateYCounterclockwise, RIGHT ->
     * facing.rotateYClockwise.
     */
    private static Direction openFace(DoorHinge hinge, Direction facing) {
        return hinge == DoorHinge.LEFT ? facing.rotateYCounterclockwise() : facing.rotateYClockwise();
    }

    private static VoxelShape slab(Direction face) {
        return switch (face) {
            case NORTH -> NORTH_SLAB;
            case SOUTH -> SOUTH_SLAB;
            case WEST -> WEST_SLAB;
            case EAST -> EAST_SLAB;
            default -> VoxelShapes.empty();
        };
    }

    // --- Multiblock iteration helpers ---

    private static void forEachPart(BlockPos origin, BiConsumer<TallDoorPart, BlockPos> action) {
        for (TallDoorPart part : TallDoorPart.values()) {
            action.accept(part, origin.up(part.getRow()));
        }
    }

    private static boolean allParts(BlockPos origin, BiPredicate<TallDoorPart, BlockPos> test) {
        for (TallDoorPart part : TallDoorPart.values()) {
            if (!test.test(part, origin.up(part.getRow()))) {
                return false;
            }
        }
        return true;
    }

    private static boolean anyPart(BlockPos origin, BiPredicate<TallDoorPart, BlockPos> test) {
        for (TallDoorPart part : TallDoorPart.values()) {
            if (test.test(part, origin.up(part.getRow()))) {
                return true;
            }
        }
        return false;
    }

    /** World position of the origin (BOTTOM) for any part of the door. */
    private static BlockPos originPos(BlockPos pos, BlockState state) {
        return pos.down(state.get(PART).getRow());
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction facing = ctx.getHorizontalPlayerFacing();
        if (!canPlaceAt(ctx)) {
            return null;
        }
        return this.getDefaultState()
                .with(FACING, facing)
                .with(OPEN, false)
                .with(HINGE, getHinge(ctx, facing))
                .with(PART, TallDoorPart.BOTTOM);
    }

    private boolean canPlaceAt(ItemPlacementContext ctx) {
        World world = ctx.getWorld();
        return allParts(ctx.getBlockPos(), (part, partPos) ->
                world.getBlockState(partPos).canReplace(ctx) && world.getWorldBorder().contains(partPos));
    }

    /**
     * Hinge side, ported from vanilla {@code DoorBlock.getHinge} and extended
     * over all 3 rows. We score full-cube neighbors just outside each side
     * (left = facing.rotateYCounterclockwise, right = facing.rotateYClockwise),
     * prefer hinging away from adjacent doors, and otherwise fall back to which
     * half of the block the player clicked — so it "swings from the left
     * depending on how you place it", exactly like vanilla.
     */
    private DoorHinge getHinge(ItemPlacementContext ctx, Direction facing) {
        World world = ctx.getWorld();
        BlockPos origin = ctx.getBlockPos();
        Direction leftDir = facing.rotateYCounterclockwise();
        Direction rightDir = facing.rotateYClockwise();

        int score = 0;
        boolean leftIsDoor = false;
        boolean rightIsDoor = false;
        for (int row = 0; row < 3; row++) {
            BlockPos leftPos = origin.up(row).offset(leftDir);
            BlockPos rightPos = origin.up(row).offset(rightDir);
            BlockState leftState = world.getBlockState(leftPos);
            BlockState rightState = world.getBlockState(rightPos);
            if (leftState.isFullCube(world, leftPos)) score--;
            if (rightState.isFullCube(world, rightPos)) score++;
            if (row == 0) {
                leftIsDoor = leftState.isOf(this);
                rightIsDoor = rightState.isOf(this);
            }
        }

        if ((!leftIsDoor || rightIsDoor) && score <= 0) {
            if ((!rightIsDoor || leftIsDoor) && score >= 0) {
                int j = facing.getOffsetX();
                int k = facing.getOffsetZ();
                Vec3d hit = ctx.getHitPos();
                double d = hit.x - origin.getX();
                double e = hit.z - origin.getZ();
                return (j >= 0 || !(e < 0.5)) && (j <= 0 || !(e > 0.5))
                        && (k >= 0 || !(d > 0.5)) && (k <= 0 || !(d < 0.5))
                        ? DoorHinge.LEFT : DoorHinge.RIGHT;
            }
            return DoorHinge.LEFT;
        }
        return DoorHinge.RIGHT;
    }

    @Override
    public void onPlaced(World world, BlockPos origin, BlockState state, LivingEntity placer, ItemStack stack) {
        if (world.isClient()) {
            return;
        }
        forEachPart(origin, (part, partPos) -> {
            if (part != TallDoorPart.BOTTOM) {
                world.setBlockState(partPos, state.with(PART, part), Block.NOTIFY_ALL);
            }
        });
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient()) {
            CLEANUP_IN_PROGRESS.set(Boolean.TRUE);
            try {
                BlockPos origin = originPos(pos, state);
                forEachPart(origin, (part, partPos) -> {
                    if (partPos.equals(pos)) {
                        return;
                    }
                    BlockState partState = world.getBlockState(partPos);
                    if (partState.isOf(this)) {
                        world.setBlockState(partPos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL | Block.SKIP_DROPS);
                        world.syncWorldEvent(player, 2001, partPos, Block.getRawIdFromState(partState));
                    }
                });
            } finally {
                CLEANUP_IN_PROGRESS.set(Boolean.FALSE);
            }
        }
        return super.onBreak(world, pos, state, player);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState,
                                                WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (CLEANUP_IN_PROGRESS.get()) {
            return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
        }
        BlockPos origin = originPos(pos, state);
        boolean neighborIsExpectedPart = anyPart(origin, (part, partPos) -> partPos.equals(neighborPos));
        if (neighborIsExpectedPart && !neighborState.isOf(this)) {
            return Blocks.AIR.getDefaultState();
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

        boolean targetOpen = !state.get(OPEN);
        BlockPos origin = originPos(pos, state);

        // Snap every part to the new OPEN state (NOTIFY_ALL so collision + model
        // both refresh). Like vanilla, opening is never blocked by the swing path.
        forEachPart(origin, (part, partPos) -> {
            BlockState partState = world.getBlockState(partPos);
            if (partState.isOf(this)) {
                world.setBlockState(partPos, partState.with(OPEN, targetOpen), Block.NOTIFY_ALL);
            }
        });

        world.playSound(null, origin,
                targetOpen ? SoundEvents.BLOCK_WOODEN_DOOR_OPEN : SoundEvents.BLOCK_WOODEN_DOOR_CLOSE,
                SoundCategory.BLOCKS, 1.0F, world.getRandom().nextFloat() * 0.1F + 0.9F);
        return ActionResult.CONSUME;
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        if (mirror == BlockMirror.NONE) {
            return state;
        }
        // Mirroring flips both the facing and the hinge side (matches vanilla doors).
        DoorHinge flipped = state.get(HINGE) == DoorHinge.LEFT ? DoorHinge.RIGHT : DoorHinge.LEFT;
        return state.rotate(mirror.getRotation(state.get(FACING))).with(HINGE, flipped);
    }

    public BlockDefinition getDefinition() {
        return def;
    }
}
