package com.westerosblocks.block.custom;

import net.minecraft.block.*;
import net.minecraft.block.enums.DoorHinge;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.event.GameEvent;

import java.util.List;

/**
 * Standalone half door block that doesn't depend on the definition file system.
 * This class provides the same functionality as WCHalfDoorBlock but with simplified
 * parameter handling for the builder-based registration system.
 */
public class WCHalfDoorBlock extends Block {
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final BooleanProperty OPEN = Properties.OPEN;
    public static final EnumProperty<DoorHinge> HINGE = Properties.DOOR_HINGE;
    public static final BooleanProperty POWERED = Properties.POWERED;

    protected static final VoxelShape NORTH_SHAPE = Block.createCuboidShape(0.0, 0.0, 13.0, 16.0, 16.0, 16.0);
    protected static final VoxelShape SOUTH_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 3.0);
    protected static final VoxelShape EAST_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 3.0, 16.0, 16.0);
    protected static final VoxelShape WEST_SHAPE = Block.createCuboidShape(13.0, 0.0, 0.0, 16.0, 16.0, 16.0);

    private final boolean locked;
    private final boolean allowUnsupported;
    private final String translationKey;

    /**
     * Creates a new standalone half door block.
     * 
     * @param settings The block settings
     * @param locked Whether this door is locked (cannot be opened by players)
     * @param allowUnsupported Whether this door can be placed without a supporting block below
     * @param translationKey The translation key for this block
     */
    public WCHalfDoorBlock(AbstractBlock.Settings settings, boolean locked, boolean allowUnsupported, String translationKey) {
        super(settings);
        this.locked = locked;
        this.allowUnsupported = allowUnsupported;
        this.translationKey = translationKey;

        this.setDefaultState(this.stateManager.getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(OPEN, false)
                .with(HINGE, DoorHinge.LEFT)
                .with(POWERED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, OPEN, HINGE, POWERED);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction direction = state.get(FACING);
        boolean closed = !state.get(OPEN);
        boolean rightHinge = state.get(HINGE) == DoorHinge.RIGHT;

        return switch(direction) {
            case EAST -> closed ? EAST_SHAPE : (rightHinge ? NORTH_SHAPE : SOUTH_SHAPE);
            case SOUTH -> closed ? SOUTH_SHAPE : (rightHinge ? EAST_SHAPE : WEST_SHAPE);
            case WEST -> closed ? WEST_SHAPE : (rightHinge ? SOUTH_SHAPE : NORTH_SHAPE);
            default -> closed ? NORTH_SHAPE : (rightHinge ? WEST_SHAPE : EAST_SHAPE);
        };
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockPos pos = ctx.getBlockPos();
        World world = ctx.getWorld();
        boolean powered = world.isReceivingRedstonePower(pos);

        return this.getDefaultState()
                .with(FACING, ctx.getHorizontalPlayerFacing().getOpposite())
                .with(HINGE, calculateHinge(ctx))
                .with(POWERED, powered)
                .with(OPEN, powered);
    }

    private DoorHinge calculateHinge(ItemPlacementContext ctx) {
        BlockView world = ctx.getWorld();
        BlockPos pos = ctx.getBlockPos();
        Direction direction = ctx.getHorizontalPlayerFacing();
        Direction counterClockwise = direction.rotateYCounterclockwise();
        Direction clockwise = direction.rotateYClockwise();

        BlockPos counterPos = pos.offset(counterClockwise);
        BlockPos clockPos = pos.offset(clockwise);

        BlockState counterState = world.getBlockState(counterPos);
        BlockState clockState = world.getBlockState(clockPos);

        int sum = (counterState.isFullCube(world, counterPos) ? -1 : 0) +
                (clockState.isFullCube(world, clockPos) ? 1 : 0);

        boolean counterIsDoor = counterState.getBlock() instanceof WCHalfDoorBlock;
        boolean clockIsDoor = clockState.getBlock() instanceof WCHalfDoorBlock;

        if ((!counterIsDoor || clockIsDoor) && sum <= 0) {
            if ((!clockIsDoor || counterIsDoor) && sum >= 0) {
                Vec3d hit = ctx.getHitPos();
                double x = hit.x - pos.getX();
                double z = hit.z - pos.getZ();
                int dirX = direction.getOffsetX();
                int dirZ = direction.getOffsetZ();

                return (dirX >= 0 || z >= 0.5) && (dirX <= 0 || z <= 0.5) &&
                        (dirZ >= 0 || x <= 0.5) && (dirZ <= 0 || x >= 0.5) ?
                        DoorHinge.LEFT : DoorHinge.RIGHT;
            }
            return DoorHinge.LEFT;
        }
        return DoorHinge.RIGHT;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (this.locked) {
            return ActionResult.PASS;
        }

        state = state.cycle(OPEN);
        world.setBlockState(pos, state, Block.NOTIFY_LISTENERS | Block.REDRAW_ON_MAIN_THREAD);
        playToggleSound(world, pos, state.get(OPEN));
        world.emitGameEvent(player, state.get(OPEN) ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
        return ActionResult.success(world.isClient);
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
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        boolean powered = world.isReceivingRedstonePower(pos);

        if (!state.isOf(block) && powered != state.get(POWERED)) {
            if (powered != state.get(OPEN)) {
                playToggleSound(world, pos, powered);
                world.emitGameEvent(null, powered ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
            }

            world.setBlockState(pos, state
                            .with(POWERED, powered)
                            .with(OPEN, powered),
                    Block.NOTIFY_LISTENERS);
        }
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState,
                                                WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (direction == Direction.DOWN && !canPlaceAt(state, world, pos)) {
            return Blocks.AIR.getDefaultState();
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        if (this.allowUnsupported) return true;
        BlockPos belowPos = pos.down();
        return world.getBlockState(belowPos).isSideSolid(world, belowPos, Direction.UP, SideShapeType.RIGID);
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return mirror == BlockMirror.NONE ? state :
                state.rotate(mirror.getRotation(state.get(FACING))).cycle(HINGE);
    }

    @Override
    public String getTranslationKey() {
        return translationKey;
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
        super.appendTooltip(stack, context, tooltip, options);
    }
} 