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
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

/**
 * A 2-wide x 3-tall door that behaves like a vanilla door: a single rigid panel
 * hinged on ONE vertical edge (chosen at placement) that snaps between closed
 * and open (no smooth animation, exactly like vanilla). Rendered entirely with
 * blockstate models — no block entity.
 *
 * <p>Multiblock: 6 cells (2 columns x 3 rows). The origin cell is
 * {@code BOTTOM_LEFT}; the door extends one column toward {@code facing.right}.
 * Closed leaves a thin slab on each cell's FACING face (a 2x3 wall). Open puts a
 * perpendicular panel only on the hinge column, extending one block into the
 * swing cell; the other column is empty (you walk through it).
 */
public class WCBigNarrowDoorBlock extends Block implements WCBlockDef {
    protected BlockDefinition def;

    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final BooleanProperty OPEN = Properties.OPEN;
    public static final EnumProperty<DoorHinge> HINGE = Properties.DOOR_HINGE;
    public static final EnumProperty<NarrowDoorPart> PART = EnumProperty.of("part", NarrowDoorPart.class);

    private final boolean locked;

    // Door collision: a thin 3-pixel slab on each cardinal face. Closed uses the
    // FACING-face slab on every cell; open uses the hinge-edge slab on the hinge
    // column only. Both are in-cell, matching the blockstate models exactly.
    private static final VoxelShape NORTH_SLAB = Block.createCuboidShape(0, 0, 0, 16, 16, 3);
    private static final VoxelShape SOUTH_SLAB = Block.createCuboidShape(0, 0, 13, 16, 16, 16);
    private static final VoxelShape WEST_SLAB = Block.createCuboidShape(0, 0, 0, 3, 16, 16);
    private static final VoxelShape EAST_SLAB = Block.createCuboidShape(13, 0, 0, 16, 16, 16);

    // Suppresses the AIR-return in getStateForNeighborUpdate while onBreak is
    // tearing the multiblock down (mirrors WCBigDoorBlock).
    private static final ThreadLocal<Boolean> CLEANUP_IN_PROGRESS = ThreadLocal.withInitial(() -> false);

    public enum NarrowDoorPart implements StringIdentifiable {
        BOTTOM_LEFT("bottom_left", 0, 0),
        BOTTOM_RIGHT("bottom_right", 1, 0),
        MIDDLE_LEFT("middle_left", 0, 1),
        MIDDLE_RIGHT("middle_right", 1, 1),
        TOP_LEFT("top_left", 0, 2),
        TOP_RIGHT("top_right", 1, 2);

        private final String name;
        private final int column;        // 0 = origin column, 1 = toward facing.right
        private final int verticalOffset; // 0 bottom, 1 middle, 2 top

        NarrowDoorPart(String name, int column, int verticalOffset) {
            this.name = name;
            this.column = column;
            this.verticalOffset = verticalOffset;
        }

        @Override
        public String asString() {
            return this.name;
        }

        public int getColumn() {
            return column;
        }

        public int getRow() {
            return verticalOffset;
        }

        public Vec3i getOffset(Direction facing) {
            // Column 0 = origin; column 1 = one block toward facing.right.
            Direction right = facing.rotateYClockwise();
            int x = right.getOffsetX() * column;
            int z = right.getOffsetZ() * column;
            return new Vec3i(x, verticalOffset, z);
        }
    }

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(BlockDefinition definition) {
            return new WCBigNarrowDoorBlock(definition.makeSettings(), definition, definition.isLocked());
        }
    }

    public WCBigNarrowDoorBlock(Settings settings, BlockDefinition def, boolean locked) {
        super(settings.nonOpaque().pistonBehavior(PistonBehavior.BLOCK));
        this.def = def;
        this.locked = locked;

        this.setDefaultState(this.stateManager.getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(OPEN, false)
                .with(HINGE, DoorHinge.LEFT)
                .with(PART, NarrowDoorPart.BOTTOM_LEFT));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, OPEN, HINGE, PART);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return getCollisionShape(state, world, pos, context);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction facing = state.get(FACING);

        if (!state.get(OPEN)) {
            // Closed: a thin slab on the facing.opposite face of every cell
            // forms the full 2x3 wall (matches vanilla DoorBlock's closed slab).
            return slab(facing.getOpposite());
        }
        // Open: the whole 2-wide door has swung 90° about its hinge edge and now
        // stands perpendicular — 2 blocks DEEP (its former width) along FACING,
        // 3px thick on the hinge face. The hinge column's cells carry that full
        // 2-deep panel (their cell + one block into the swing cell); the other
        // column is empty (the door swung out of it). Matches the open models.
        NarrowDoorPart part = state.get(PART);
        DoorHinge hinge = state.get(HINGE);
        if (part.getColumn() != hingeColumn(hinge)) {
            return VoxelShapes.empty();
        }
        return openPanel(facing, hinge);
    }

    /**
     * Which multiblock column hosts the hinge. col0 (the placed block) is the
     * facing.rotateYCounterclockwise side; col1 is the clockwise side.
     * LEFT hinge -> col0, RIGHT hinge -> col1 (matches vanilla's open-face side).
     */
    private static int hingeColumn(DoorHinge hinge) {
        return hinge == DoorHinge.LEFT ? 0 : 1;
    }

    /**
     * The world face the open panel's thickness sits against — exactly vanilla
     * DoorBlock's rule: LEFT hinge -> facing.rotateYCounterclockwise, RIGHT ->
     * facing.rotateYClockwise.
     */
    private static Direction openFace(DoorHinge hinge, Direction facing) {
        return hinge == DoorHinge.LEFT ? facing.rotateYCounterclockwise() : facing.rotateYClockwise();
    }

    // Open-panel collision per (facing, hinge): a 3px slab on the openFace,
    // 2 blocks deep along FACING (hinge cell + one block into the swing cell).
    // Matches the open_<facing>_<hinge> models exactly. Indexed [facing][hinge].
    private static final VoxelShape[][] OPEN_PANELS = new VoxelShape[6][2];
    static {
        for (Direction f : Direction.Type.HORIZONTAL) {
            for (DoorHinge h : DoorHinge.values()) {
                OPEN_PANELS[f.ordinal()][h.ordinal()] = buildOpenPanel(f, h);
            }
        }
    }

    private static VoxelShape openPanel(Direction facing, DoorHinge hinge) {
        VoxelShape shape = OPEN_PANELS[facing.ordinal()][hinge.ordinal()];
        return shape != null ? shape : VoxelShapes.empty();
    }

    private static VoxelShape buildOpenPanel(Direction facing, DoorHinge hinge) {
        Direction openFace = openFace(hinge, facing);
        double minX = 0, minZ = 0, maxX = 16, maxZ = 16;
        // 3px thickness flush against the hinge (open) face.
        switch (openFace) {
            case NORTH -> maxZ = 3;
            case SOUTH -> minZ = 13;
            case WEST -> maxX = 3;
            case EAST -> minX = 13;
            default -> { return VoxelShapes.empty(); }
        }
        // 2 blocks deep along FACING: this cell plus one block toward FACING.
        switch (facing) {
            case NORTH -> { minZ = -16; maxZ = 16; }
            case SOUTH -> { minZ = 0; maxZ = 32; }
            case WEST -> { minX = -16; maxX = 16; }
            case EAST -> { minX = 0; maxX = 32; }
            default -> { return VoxelShapes.empty(); }
        }
        return Block.createCuboidShape(minX, 0, minZ, maxX, 16, maxZ);
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

    private static void forEachPart(BlockPos origin, Direction facing, BiConsumer<NarrowDoorPart, BlockPos> action) {
        for (NarrowDoorPart part : NarrowDoorPart.values()) {
            action.accept(part, origin.add(part.getOffset(facing)));
        }
    }

    private static boolean allParts(BlockPos origin, Direction facing, BiPredicate<NarrowDoorPart, BlockPos> test) {
        for (NarrowDoorPart part : NarrowDoorPart.values()) {
            if (!test.test(part, origin.add(part.getOffset(facing)))) {
                return false;
            }
        }
        return true;
    }

    private static boolean anyPart(BlockPos origin, Direction facing, BiPredicate<NarrowDoorPart, BlockPos> test) {
        for (NarrowDoorPart part : NarrowDoorPart.values()) {
            if (test.test(part, origin.add(part.getOffset(facing)))) {
                return true;
            }
        }
        return false;
    }

    /** World position of the origin (BOTTOM_LEFT) for any part of the door. */
    private static BlockPos originPos(BlockPos pos, Direction facing, NarrowDoorPart part) {
        Vec3i offset = part.getOffset(facing);
        return pos.add(-offset.getX(), -offset.getY(), -offset.getZ());
    }

    private static BlockPos originPos(BlockPos pos, BlockState state) {
        return originPos(pos, state.get(FACING), state.get(PART));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction facing = ctx.getHorizontalPlayerFacing();
        if (!canPlaceAt(ctx, facing)) {
            return null;
        }
        return this.getDefaultState()
                .with(FACING, facing)
                .with(OPEN, false)
                .with(HINGE, getHinge(ctx, facing))
                .with(PART, NarrowDoorPart.BOTTOM_LEFT);
    }

    private boolean canPlaceAt(ItemPlacementContext ctx, Direction facing) {
        World world = ctx.getWorld();
        return allParts(ctx.getBlockPos(), facing, (part, partPos) ->
                world.getBlockState(partPos).canReplace(ctx) && world.getWorldBorder().contains(partPos));
    }

    /**
     * Hinge side, ported from vanilla {@code DoorBlock.getHinge} and widened for
     * the 2-cell footprint. The placed block (origin) is the LEFT column
     * (facing.rotateYCounterclockwise side); col1 is the RIGHT column. We score
     * full-cube neighbors just outside each side over all 3 rows (left -1, right
     * +1), prefer hinging away from adjacent doors, and otherwise fall back to
     * which half of the block the player clicked — so it "swings from the left
     * depending on how you place it", exactly like vanilla.
     */
    private DoorHinge getHinge(ItemPlacementContext ctx, Direction facing) {
        World world = ctx.getWorld();
        BlockPos origin = ctx.getBlockPos();               // left column, bottom
        Direction leftDir = facing.rotateYCounterclockwise();
        Direction rightDir = facing.rotateYClockwise();
        BlockPos rightColumn = origin.offset(rightDir);    // right column, bottom

        int score = 0;
        boolean leftIsDoor = false;
        boolean rightIsDoor = false;
        for (int row = 0; row < 3; row++) {
            BlockPos leftPos = origin.up(row).offset(leftDir);
            BlockPos rightPos = rightColumn.up(row).offset(rightDir);
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
        forEachPart(origin, state.get(FACING), (part, partPos) -> {
            if (part != NarrowDoorPart.BOTTOM_LEFT) {
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
                forEachPart(origin, state.get(FACING), (part, partPos) -> {
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
        boolean neighborIsExpectedPart = anyPart(origin, state.get(FACING),
                (part, partPos) -> partPos.equals(neighborPos));
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
        Direction facing = state.get(FACING);

        // Snap every part to the new OPEN state (NOTIFY_ALL so collision + model
        // both refresh). Like vanilla, opening is never blocked by the swing path.
        forEachPart(origin, facing, (part, partPos) -> {
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
