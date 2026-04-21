package com.westerosblocks.block.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.blockentity.custom.WCBigDoorBlockEntity;
import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
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
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

public class WCBigDoorBlock extends Block implements WCBlockDef, BlockEntityProvider {
    protected BlockDefinition def;

    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final BooleanProperty OPEN = Properties.OPEN;
    public static final EnumProperty<BigDoorPart> PART = EnumProperty.of("part", BigDoorPart.class);

    // Thin 3-pixel slabs along each cardinal face of a 1x1 block. All door collision
    // shapes — closed panel, open-left leaf, open-right leaf — are one of these four,
    // selected by direction at query time.
    private static final VoxelShape NORTH_SLAB = Block.createCuboidShape(0, 0, 0, 16, 16, 3);
    private static final VoxelShape SOUTH_SLAB = Block.createCuboidShape(0, 0, 13, 16, 16, 16);
    private static final VoxelShape WEST_SLAB = Block.createCuboidShape(0, 0, 0, 3, 16, 16);
    private static final VoxelShape EAST_SLAB = Block.createCuboidShape(13, 0, 0, 16, 16, 16);

    private final boolean locked;

    // Suppresses the AIR-return in getStateForNeighborUpdate while onBreak is
    // already tearing the multiblock down. Without this, the NOTIFY_ALL flags
    // used by onBreak would bounce into sibling parts' neighbor-update path,
    // which drops items (ignoring our SKIP_DROPS on the original setBlockState).
    private static final ThreadLocal<Boolean> CLEANUP_IN_PROGRESS = ThreadLocal.withInitial(() -> false);

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
        private final int horizontalOffset;
        private final int verticalOffset;

        BigDoorPart(String name, int horizontalOffset, int verticalOffset) {
            this.name = name;
            this.horizontalOffset = horizontalOffset;
            this.verticalOffset = verticalOffset;
        }

        @Override
        public String asString() {
            return this.name;
        }

        public boolean isCenterColumn() {
            return horizontalOffset == 1;
        }

        public boolean isLeftColumn() {
            return horizontalOffset == 0;
        }

        public boolean isRightColumn() {
            return horizontalOffset == 2;
        }

        public Vec3i getOffset(Direction facing) {
            // LEFT (0) -> +1 toward facing.left(); CENTER (1) -> 0; RIGHT (2) -> -1.
            int relativeHorizontal = 1 - horizontalOffset;
            Direction left = facing.rotateYCounterclockwise();
            int x = left.getOffsetX() * relativeHorizontal;
            int z = left.getOffsetZ() * relativeHorizontal;
            return new Vec3i(x, verticalOffset, z);
        }
    }

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(BlockDefinition definition) {
            return new WCBigDoorBlock(definition.makeSettings(), definition, definition.isLocked());
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
        BigDoorPart part = state.get(PART);

        if (!state.get(OPEN)) {
            // Closed: the geo's closed leaves sit on the FACING-direction face
            // of the block (e.g. z=0..3 for FACING=NORTH). The multiblock's
            // back wall is at `facing`, not `facing.getOpposite()`.
            return slab(facing);
        }
        if (part.isCenterColumn()) {
            return VoxelShapes.empty();
        }
        // Open: 3-px slab on the column's outer wall, inside the block. The
        // geo's new pivot (center of the leaf's thickness axis) means the
        // rotated leaf lies flush with the outer face from the inside, so the
        // open-state collision occupies the same 3×16×16 voxel footprint as
        // the closed-state slab — just on a perpendicular face. The 21-px
        // ornamental tail past the FACING face is visual-only, no collision.
        Direction outer = part.isLeftColumn() ? facing.rotateYCounterclockwise() : facing.rotateYClockwise();
        return slab(outer);
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

    /** Iterate all 9 parts of the door relative to origin. */
    private static void forEachPart(BlockPos origin, Direction facing, BiConsumer<BigDoorPart, BlockPos> action) {
        for (BigDoorPart part : BigDoorPart.values()) {
            action.accept(part, origin.add(part.getOffset(facing)));
        }
    }

    /** Test every part position; return false on the first failure. */
    private static boolean allParts(BlockPos origin, Direction facing, BiPredicate<BigDoorPart, BlockPos> test) {
        for (BigDoorPart part : BigDoorPart.values()) {
            if (!test.test(part, origin.add(part.getOffset(facing)))) {
                return false;
            }
        }
        return true;
    }

    /** Return true if any part position satisfies the predicate. */
    private static boolean anyPart(BlockPos origin, Direction facing, BiPredicate<BigDoorPart, BlockPos> test) {
        for (BigDoorPart part : BigDoorPart.values()) {
            if (test.test(part, origin.add(part.getOffset(facing)))) {
                return true;
            }
        }
        return false;
    }

    /** World position of the origin (BOTTOM_CENTER) for any part of the door. */
    private static BlockPos originPos(BlockPos pos, Direction facing, BigDoorPart part) {
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
                .with(PART, BigDoorPart.BOTTOM_CENTER);
    }

    private boolean canPlaceAt(ItemPlacementContext ctx, Direction facing) {
        World world = ctx.getWorld();
        return allParts(ctx.getBlockPos(), facing, (part, partPos) ->
                world.getBlockState(partPos).canReplace(ctx) && world.getWorldBorder().contains(partPos));
    }

    @Override
    public void onPlaced(World world, BlockPos origin, BlockState state, LivingEntity placer, ItemStack stack) {
        if (world.isClient()) {
            return;
        }
        forEachPart(origin, state.get(FACING), (part, partPos) -> {
            if (part != BigDoorPart.BOTTOM_CENTER) {
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

        // Start the swing before flipping OPEN so the BE sync packet is queued
        // before the 9 blockstate updates. Client then processes the swing first
        // (swingDir=OPENING/CLOSING) and renders the leaf mid-swing when OPEN
        // flips, avoiding a 1-frame snap to the far-end rest pose.
        if (world.getBlockEntity(origin) instanceof WCBigDoorBlockEntity bde) {
            bde.startSwingAnimation(player, targetOpen);
        }
        forEachPart(origin, state.get(FACING), (part, partPos) -> {
            BlockState partState = world.getBlockState(partPos);
            if (partState.isOf(this)) {
                world.setBlockState(partPos, partState.with(OPEN, targetOpen), Block.NOTIFY_LISTENERS);
            }
        });
        return ActionResult.CONSUME;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        if (state.get(PART) != BigDoorPart.BOTTOM_CENTER) {
            return null;
        }
        return new WCBigDoorBlockEntity(pos, state, def.getBlockName());
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if (world.isClient()) {
            return null;
        }
        return (w, pos, s, be) -> {
            if (be instanceof WCBigDoorBlockEntity bde) {
                WCBigDoorBlockEntity.serverTick(w, pos, s, bde);
            }
        };
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

    // Pilot placeholders. Scale-up path: read per-door geo/texture paths from
    // the BlockDefinition (e.g. new bigdoorGeo/bigdoorTexture fields) and return
    // them here so each door type renders with its own atlas + mesh.
    private static final Identifier DEFAULT_GEO = WesterosBlocks.id("geo/block/bigdoor.geo.json");
    private static final Identifier DEFAULT_TEXTURE = WesterosBlocks.id("textures/block/debug/bigdoor_test.png");

    public Identifier getGeoLocation() {
        return DEFAULT_GEO;
    }

    public Identifier getTextureLocation() {
        return DEFAULT_TEXTURE;
    }
}
