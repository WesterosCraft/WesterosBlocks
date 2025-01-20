package com.westerosblocks.block.custom;

import com.westerosblocks.block.*;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
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
import net.minecraft.world.WorldView;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

public class WCHalfDoorBlock extends Block implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
            AbstractBlock.Settings settings = def.makeBlockSettings();
            Block blk = new WCHalfDoorBlock(settings, def);
            return def.registerRenderType(ModBlocks.registerBlock(def.blockName, blk), false, false);
        }
    }

    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final BooleanProperty OPEN = Properties.OPEN;
    public static final BooleanProperty RIGHT_HINGE = BooleanProperty.of("right_hinge");
    public static final BooleanProperty POWERED = Properties.POWERED;

    protected static final VoxelShape SOUTH_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 3.0D);
    protected static final VoxelShape NORTH_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 13.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape WEST_SHAPE = Block.createCuboidShape(13.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape EAST_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 3.0D, 16.0D, 16.0D);

    private final WesterosBlockDef def;
    private boolean locked = false;
    private boolean allowUnsupported = false;

    protected WCHalfDoorBlock(AbstractBlock.Settings settings, WesterosBlockDef def) {
        super(settings);
        this.def = def;

        String type = def.getType();
        if (type != null) {
            String[] toks = type.split(",");
            for (String tok : toks) {
                if (tok.equals("allow-unsupported")) {
                    allowUnsupported = true;
                }
                String[] flds = tok.split(":");
                if (flds.length < 2) continue;
                if (flds[0].equals("locked")) {
                    locked = flds[1].equals("true");
                }
            }
        }

        this.setDefaultState(this.stateManager.getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(OPEN, false)
                .with(RIGHT_HINGE, false)
                .with(POWERED, false));
    }

    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction direction = state.get(FACING);
        boolean closed = !state.get(OPEN);
        boolean rightHinge = state.get(RIGHT_HINGE);

        return switch (direction) {
            case EAST -> closed ? EAST_SHAPE : (rightHinge ? NORTH_SHAPE : SOUTH_SHAPE);
            case SOUTH -> closed ? SOUTH_SHAPE : (rightHinge ? EAST_SHAPE : WEST_SHAPE);
            case WEST -> closed ? WEST_SHAPE : (rightHinge ? SOUTH_SHAPE : NORTH_SHAPE);
            default -> closed ? NORTH_SHAPE : (rightHinge ? WEST_SHAPE : EAST_SHAPE);
        };
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockPos pos = ctx.getBlockPos();
        World world = ctx.getWorld();
        boolean powered = world.isReceivingRedstonePower(pos);

        Direction facing = ctx.getHorizontalPlayerFacing().getOpposite();
        Vec3d hitPos = ctx.getHitPos();
        double offset = switch (facing) {
            case NORTH, SOUTH -> hitPos.x - pos.getX();
            case EAST, WEST -> hitPos.z - pos.getZ();
            default -> 0.0;
        };
        boolean rightHinge = offset > 0.5;

        return this.getDefaultState()
                .with(FACING, facing)
                .with(RIGHT_HINGE, rightHinge)
                .with(POWERED, powered)
                .with(OPEN, powered);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (this.locked) {
            return ActionResult.PASS;
        }

        state = state.cycle(OPEN);
        world.setBlockState(pos, state, Block.NOTIFY_LISTENERS);
        world.emitGameEvent(player, state.get(OPEN) ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);

        return ActionResult.success(world.isClient);
    }

    public boolean isOpen(BlockState state) {
        return state.get(OPEN);
    }

    public void setOpen(@Nullable Entity entity, World world, BlockState state, BlockPos pos, boolean open) {
        if (!state.isOf(this) || state.get(OPEN) == open) {
            return;
        }

        world.setBlockState(pos, state.with(OPEN, open), Block.NOTIFY_LISTENERS);
        world.emitGameEvent(entity, open ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (!world.isClient) {
            boolean powered = world.isReceivingRedstonePower(pos);
            if (powered != state.get(POWERED)) {
                if (state.get(OPEN) != powered) {
                    state = state.with(OPEN, powered);
                    world.emitGameEvent(null, powered ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
                }
                world.setBlockState(pos, state.with(POWERED, powered), Block.NOTIFY_LISTENERS);
            }
        }
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        if (allowUnsupported) {
            return true;
        }
        return world.getBlockState(pos.down()).isSideSolidFullSquare(world, pos.down(), Direction.UP);
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return mirror == BlockMirror.NONE ? state :
                state.rotate(mirror.getRotation(state.get(FACING))).with(RIGHT_HINGE, !state.get(RIGHT_HINGE));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, OPEN, RIGHT_HINGE, POWERED);
    }

    private static final String[] TAGS = {"doors"};

    @Override
    public String[] getBlockTags() {
        return TAGS;
    }
}