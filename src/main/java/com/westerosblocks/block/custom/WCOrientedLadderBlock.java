package com.westerosblocks.block.custom;

import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.enums.BlockFace;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

/**
 * A ladder-style block that can be attached to either the floor or a wall, with a
 * per-attachment ROTATED toggle. Combined states:
 * <ul>
 *   <li>FLOOR + !ROTATED → upright, free-standing on the ground</li>
 *   <li>FLOOR +  ROTATED → lying flat on the ground</li>
 *   <li>WALL  + !ROTATED → mounted normally against a wall</li>
 *   <li>WALL  +  ROTATED → mounted against a wall, rotated 90°</li>
 * </ul>
 * FACING gives the four horizontal orientations within each attachment.
 * Climbing is provided by the {@code minecraft:climbable} block tag (added in
 * {@code ModBlockTagProvider} unless {@code noClimb} is set), mirroring {@code WCLadderBlock}.
 */
public class WCOrientedLadderBlock extends Block implements WCBlockDef, Waterloggable {

    // Only FLOOR and WALL are used (no CEILING) — EnumProperty.of accepts a value subset.
    public static final EnumProperty<BlockFace> FACE =
            EnumProperty.of("face", BlockFace.class, BlockFace.FLOOR, BlockFace.WALL);
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final BooleanProperty ROTATED = BooleanProperty.of("rotated");
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    protected final BlockDefinition def;
    private final boolean allowUnsupported;
    private final boolean toggleOnUse;

    protected WCOrientedLadderBlock(AbstractBlock.Settings settings, BlockDefinition def,
                                    boolean allowUnsupported, boolean toggleOnUse) {
        super(settings);
        this.def = def;
        this.allowUnsupported = allowUnsupported;
        this.toggleOnUse = toggleOnUse;
        this.setDefaultState(this.getStateManager().getDefaultState()
                .with(FACE, BlockFace.WALL)
                .with(FACING, Direction.NORTH)
                .with(ROTATED, false)
                .with(WATERLOGGED, false));
    }

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(BlockDefinition definition) {
            AbstractBlock.Settings settings = definition.makeSettings().nonOpaque();
            return new WCOrientedLadderBlock(settings, definition,
                    definition.isAllowUnsupported(), definition.toggleOnUse());
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACE, FACING, ROTATED, WATERLOGGED);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        WorldView world = ctx.getWorld();
        BlockPos pos = ctx.getBlockPos();
        boolean waterlogged = world.getFluidState(pos).getFluid() == Fluids.WATER;
        BlockState base = this.getDefaultState().with(ROTATED, false).with(WATERLOGGED, waterlogged);

        // Clicking the top of a block → floor-mounted ladder.
        if (ctx.getSide() == Direction.UP) {
            BlockState floor = base.with(FACE, BlockFace.FLOOR)
                    .with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
            if (floor.canPlaceAt(world, pos)) {
                return floor;
            }
        }

        // Otherwise prefer a wall, mirroring vanilla LadderBlock placement order.
        for (Direction direction : ctx.getPlacementDirections()) {
            if (direction.getAxis().isHorizontal()) {
                BlockState wall = base.with(FACE, BlockFace.WALL)
                        .with(FACING, direction.getOpposite());
                if (wall.canPlaceAt(world, pos)) {
                    return wall;
                }
            }
        }

        // Fall back to a floor placement (e.g. clicking a side but standing on ground).
        BlockState floor = base.with(FACE, BlockFace.FLOOR)
                .with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
        return floor.canPlaceAt(world, pos) ? floor : null;
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        if (this.allowUnsupported) {
            return true;
        }
        Direction support = supportDirection(state);
        BlockPos attachPos = pos.offset(support);
        return world.getBlockState(attachPos).isSideSolidFullSquare(world, attachPos, support.getOpposite());
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState,
                                                WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (!this.allowUnsupported && direction == supportDirection(state) && !this.canPlaceAt(state, world, pos)) {
            return Blocks.AIR.getDefaultState();
        }
        if (state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    /** Direction toward the block this ladder is attached to. */
    private static Direction supportDirection(BlockState state) {
        return state.get(FACE) == BlockFace.FLOOR
                ? Direction.DOWN
                : state.get(FACING).getOpposite();
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (this.toggleOnUse && player.isCreative() && player.getMainHandStack().isEmpty() && state.contains(ROTATED)) {
            world.setBlockState(pos, state.cycle(ROTATED), Block.NOTIFY_ALL);
            world.syncWorldEvent(player, 1006, pos, 0);
            return ActionResult.success(world.isClient);
        }
        return ActionResult.PASS;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return shapeFor(state);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        // Solid collision like a vanilla ladder — you can't walk through it; climbing is
        // handled by the climbable tag, not by removing collision.
        return shapeFor(state);
    }

    private static VoxelShape shapeFor(BlockState state) {
        if (state.get(FACE) == BlockFace.FLOOR) {
            if (state.get(ROTATED)) {
                return FLOOR_FLAT_SHAPE; // lying flat on the ground
            }
            return state.get(FACING).getAxis() == Direction.Axis.Z ? FLOOR_UPRIGHT_NS : FLOOR_UPRIGHT_EW;
        }
        return WALL_SHAPES[state.get(FACING).getHorizontal()];
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
    public BlockDefinition getDefinition() {
        return def;
    }

    // ---- Voxel shapes (placeholder geometry; refine alongside the real model) ----

    private static final VoxelShape FLOOR_FLAT_SHAPE = Block.createCuboidShape(0, 0, 0, 16, 3, 16);
    private static final VoxelShape FLOOR_UPRIGHT_NS = Block.createCuboidShape(0, 0, 6.5, 16, 16, 9.5);
    private static final VoxelShape FLOOR_UPRIGHT_EW = Block.createCuboidShape(6.5, 0, 0, 9.5, 16, 16);

    // Indexed by Direction.getHorizontal(): 0=SOUTH, 1=WEST, 2=NORTH, 3=EAST.
    // The ladder hugs the wall opposite its FACING.
    private static final VoxelShape[] WALL_SHAPES = new VoxelShape[]{
            Block.createCuboidShape(0, 0, 0, 16, 16, 3),   // SOUTH facing → support NORTH
            Block.createCuboidShape(13, 0, 0, 16, 16, 16),  // WEST facing → support EAST
            Block.createCuboidShape(0, 0, 13, 16, 16, 16),  // NORTH facing → support SOUTH
            Block.createCuboidShape(0, 0, 0, 3, 16, 16)     // EAST facing → support WEST
    };
}
