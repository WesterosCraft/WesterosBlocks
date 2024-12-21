package com.westerosblocks.block.custom;

import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.WesterosBlockFactory;
import com.westerosblocks.block.WesterosBlockLifecycle;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.block.enums.StairShape;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.block.Block;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.stream.IntStream;

public class WCStairBlock extends Block implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
            AbstractBlock.Settings settings = def.makeProperties();
            // See if we have a state property
            WesterosBlockDef.StateProperty state = def.buildStateProperty();
            if (state != null) {
                tempSTATE = state;
            }
            // Process types
            String t = def.getType();
            boolean doUnconnect = false;
            boolean doConnectstate = false;
            if (t != null) {
                String[] toks = t.split(",");
                for (String tok : toks) {
                    String[] parts = tok.split(":");
                    // See if we have unconnect
                    if (parts[0].equals("unconnect")) {
                        doUnconnect = true;
                        tempUNCONNECT = UNCONNECT;
                    }
                    // See if we have connectstate
                    if (parts[0].equals("connectstate")) {
                        doConnectstate = true;
                        tempCONNECTSTATE = CONNECTSTATE;
                    }
                }
            }

            Block blk = new WCStairBlock(settings, def, doUnconnect, doConnectstate);
            return def.registerRenderType(blk, false, false);
        }
    }

    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final EnumProperty<BlockHalf> HALF = Properties.BLOCK_HALF;
    public static final EnumProperty<StairShape> SHAPE = Properties.STAIR_SHAPE;
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    protected static final VoxelShape BOTTOM_AABB = VoxelShapes.cuboid(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);
    protected static final VoxelShape TOP_AABB = VoxelShapes.cuboid(0.0, 8.0, 0.0, 16.0, 16.0, 16.0);
    protected static final VoxelShape OCTET_NNN = VoxelShapes.cuboid(0.0, 0.0, 0.0, 8.0, 8.0, 8.0);
    protected static final VoxelShape OCTET_NNP = VoxelShapes.cuboid(0.0, 0.0, 8.0, 8.0, 8.0, 16.0);
    protected static final VoxelShape OCTET_NPN = VoxelShapes.cuboid(0.0, 8.0, 0.0, 8.0, 16.0, 8.0);
    protected static final VoxelShape OCTET_NPP = VoxelShapes.cuboid(0.0, 8.0, 8.0, 8.0, 16.0, 16.0);
    protected static final VoxelShape OCTET_PNN = VoxelShapes.cuboid(8.0, 0.0, 0.0, 16.0, 8.0, 8.0);
    protected static final VoxelShape OCTET_PNP = VoxelShapes.cuboid(8.0, 0.0, 8.0, 16.0, 8.0, 16.0);
    protected static final VoxelShape OCTET_PPN = VoxelShapes.cuboid(8.0, 8.0, 0.0, 16.0, 16.0, 8.0);
    protected static final VoxelShape OCTET_PPP = VoxelShapes.cuboid(8.0, 8.0, 8.0, 16.0, 16.0, 16.0);

    private final WesterosBlockDef def;
    public static final BooleanProperty UNCONNECT = BooleanProperty.of("unconnect");
    protected static BooleanProperty tempUNCONNECT;
    public final boolean unconnect;

    public static final IntProperty CONNECTSTATE = IntProperty.of("connectstate", 0, 3);
    protected static IntProperty tempCONNECTSTATE;
    public final boolean connectstate;

    protected static WesterosBlockDef.StateProperty tempSTATE;
    protected WesterosBlockDef.StateProperty STATE;

    protected boolean toggleOnUse = false;

    public final boolean no_uvlock;

    protected static final VoxelShape[] TOP_SHAPES = makeShapes(TOP_AABB, OCTET_NNN, OCTET_PNN, OCTET_NNP, OCTET_PNP);
    protected static final VoxelShape[] BOTTOM_SHAPES = makeShapes(BOTTOM_AABB, OCTET_NPN, OCTET_PPN, OCTET_NPP, OCTET_PPP);
    private static final int[] SHAPE_BY_STATE = new int[]{12, 5, 3, 10, 14, 13, 7, 11, 13, 7, 11, 14, 8, 4, 1, 2, 4, 1, 2, 8};

    protected WCStairBlock(AbstractBlock.Settings settings, WesterosBlockDef def, boolean doUnconnect, boolean doConnectstate) {
        super(settings);
        this.def = def;

        String t = def.getType();
        boolean no_uvlock = false;
        if (t != null) {
            String[] toks = t.split(",");
            for (String tok : toks) {
                if (tok.equals("no-uvlock")) {
                    no_uvlock = true;
                }
                if (tok.equals("toggleOnUse")) {
                    toggleOnUse = true;
                }
            }
        }

        this.no_uvlock = no_uvlock;
        this.unconnect = doUnconnect;
        this.connectstate = doConnectstate;
        BlockState defbs = getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(HALF, BlockHalf.BOTTOM)
                .with(SHAPE, StairShape.STRAIGHT)
                .with(WATERLOGGED, Boolean.FALSE);
        if (unconnect) {
            defbs = defbs.with(UNCONNECT, Boolean.FALSE);
        }
        if (connectstate) {
            defbs = defbs.with(CONNECTSTATE, 0);
        }
        if (STATE != null) {
            defbs = defbs.with(STATE, STATE.defValue);
        }
        setDefaultState(defbs);
    }

    protected WCStairBlock(AbstractBlock.Settings settings, WesterosBlockDef def, boolean doUnconnect) {
        this(settings, def, doUnconnect, false);
    }

    protected WCStairBlock(AbstractBlock.Settings settings, WesterosBlockDef def) {
        this(settings, def, false, false);
    }

    private int getShapeIndex(BlockState blockState) {
        return blockState.get(SHAPE).ordinal() * 4 + blockState.get(FACING).getHorizontal();
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        if (tempUNCONNECT != null) {
            builder.add(tempUNCONNECT);
            tempUNCONNECT = null;
        }
        if (tempCONNECTSTATE != null) {
            builder.add(tempCONNECTSTATE);
            tempCONNECTSTATE = null;
        }
        if (tempSTATE != null) {
            STATE = tempSTATE;
            tempSTATE = null;
        }
        if (STATE != null) {
            builder.add(STATE);
        }
        builder.add(FACING, HALF, SHAPE, WATERLOGGED);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction direction = ctx.getSide();
        BlockPos blockPos = ctx.getBlockPos();
        FluidState fluidState = ctx.getWorld().getFluidState(blockPos);
        BlockState blockState = (this.getDefaultState()
                .with(FACING, ctx.getPlayerLookDirection()))
                .with(HALF, direction == Direction.DOWN
                        || direction != Direction.UP
                        && ctx.getHitPos().y - (double) blockPos.getY() > 0.5 ? BlockHalf.TOP : BlockHalf.BOTTOM)
                .with(WATERLOGGED, fluidState.isOf(Fluids.WATER));
        return blockState.with(SHAPE, getStairShape(blockState, ctx.getWorld(), blockPos));
    }

    private static VoxelShape[] makeShapes(VoxelShape voxelShape, VoxelShape voxelShape2, VoxelShape voxelShape3, VoxelShape voxelShape4, VoxelShape voxelShape5) {
        return IntStream.range(0, 16).mapToObj(n -> makeStairShape(n, voxelShape, voxelShape2, voxelShape3, voxelShape4, voxelShape5)).toArray(VoxelShape[]::new);
    }

    private static VoxelShape makeStairShape(int n, VoxelShape voxelShape, VoxelShape voxelShape2, VoxelShape voxelShape3, VoxelShape voxelShape4, VoxelShape voxelShape5) {
        VoxelShape voxelShape6 = voxelShape;
        if ((n & 1) != 0) {
            voxelShape6 = VoxelShapes.union(voxelShape6, voxelShape2);
        }
        if ((n & 2) != 0) {
            voxelShape6 = VoxelShapes.union(voxelShape6, voxelShape3);
        }
        if ((n & 4) != 0) {
            voxelShape6 = VoxelShapes.union(voxelShape6, voxelShape4);
        }
        if ((n & 8) != 0) {
            voxelShape6 = VoxelShapes.union(voxelShape6, voxelShape5);
        }
        return voxelShape6;
    }


    @Override
    public boolean isTransparent(BlockState state, BlockView world, BlockPos pos) {
        return false;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState blockState, BlockView world, BlockPos pos, ShapeContext context) {
        return (blockState.get(HALF) == BlockHalf.TOP ? TOP_SHAPES : BOTTOM_SHAPES)[SHAPE_BY_STATE[this.getShapeIndex(blockState)]];
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState,
                                                WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (unconnect && state.get(UNCONNECT)) {
            if (state.get(WATERLOGGED)) {
                world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
            }
            return state;
        }
        if (state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        if (direction.getAxis().isHorizontal()) {
            return state.with(SHAPE, getStairShape(state, world, pos));
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    private static StairShape getStairShape(BlockState blockState, WorldAccess world, BlockPos blockPos) {
        Direction direction;
        Direction facing = blockState.get(FACING);
        BlockState neighborState = world.getBlockState(blockPos.offset(facing));  // relative -> offset

        if (isStairs(neighborState)
                && blockState.get(HALF) == neighborState.get(HALF)) {
            Direction neighborFacing = neighborState.get(FACING);
            if (neighborFacing.getAxis() != facing.getAxis()
                    && canTakeShape(blockState, world, blockPos, neighborFacing.getOpposite())) {
                if (neighborFacing == facing.rotateCounterclockwise(Direction.Axis.Y)) {  // getCounterClockWise -> rotateCounterclockwise
                    return StairShape.OUTER_LEFT;
                }
                return StairShape.OUTER_RIGHT;
            }
        }

        BlockState oppositeState = world.getBlockState(blockPos.offset(facing.getOpposite()));
        if (isStairs(oppositeState)
                && blockState.get(HALF) == oppositeState.get(HALF)) {  // StateHolder removed, direct BlockState usage
            direction = oppositeState.get(FACING);
            if (direction.getAxis() != facing.getAxis()
                    && canTakeShape(blockState, world, blockPos, direction)) {
                if (direction == facing.rotateCounterclockwise(Direction.Axis.Y)) {
                    return StairShape.INNER_LEFT;
                }
                return StairShape.INNER_RIGHT;
            }
        }
        return StairShape.STRAIGHT;
    }

    private static boolean canTakeShape(BlockState blockState, WorldAccess world, BlockPos blockPos, Direction direction) {
        BlockState blockState2 = world.getBlockState(blockPos.offset(direction));
        return !isStairs(blockState2) || blockState2.get(FACING) != blockState.get(FACING) || blockState2.get(HALF) != blockState.get(HALF);
    }

    public static boolean isStairs(BlockState blockState) {
        return blockState.getBlock() instanceof WCStairBlock;
    }

    @Override
    public FluidState getFluidState(BlockState blockState) {
        if (blockState.get(WATERLOGGED)) {
            return Fluids.WATER.getStill(false);
        }
        return super.getFluidState(blockState);
    }

    @Override
    protected boolean canPathfindThrough(BlockState state, NavigationType type) {
        return false;
    }

    @Override
    protected BlockState rotate(BlockState blockState, BlockRotation rotation) {
        return blockState.with(FACING, rotation.rotate(blockState.get(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState blockState, BlockMirror mirror) {
        Direction direction = blockState.get(FACING);
        StairShape stairsShape = blockState.get(SHAPE);
        switch (mirror) {
            case LEFT_RIGHT: {
                if (direction.getAxis() != Direction.Axis.Z) break;
                return switch (stairsShape) {
                    case INNER_LEFT -> blockState.rotate(BlockRotation.CLOCKWISE_180)
                            .with(SHAPE, StairShape.INNER_RIGHT);
                    case INNER_RIGHT -> blockState.rotate(BlockRotation.CLOCKWISE_180)
                            .with(SHAPE, StairShape.INNER_LEFT);
                    case OUTER_LEFT -> blockState.rotate(BlockRotation.CLOCKWISE_180)
                            .with(SHAPE, StairShape.OUTER_RIGHT);
                    case OUTER_RIGHT -> blockState.rotate(BlockRotation.CLOCKWISE_180)
                            .with(SHAPE, StairShape.OUTER_LEFT);
                    default -> blockState.rotate(BlockRotation.CLOCKWISE_180);
                };
            }
            case FRONT_BACK: {
                if (direction.getAxis() != Direction.Axis.X) break;
                return switch (stairsShape) {
                    case INNER_LEFT -> blockState.rotate(BlockRotation.CLOCKWISE_180)
                            .with(SHAPE, StairShape.INNER_LEFT);
                    case INNER_RIGHT -> blockState.rotate(BlockRotation.CLOCKWISE_180)
                            .with(SHAPE, StairShape.INNER_RIGHT);
                    case OUTER_LEFT -> blockState.rotate(BlockRotation.CLOCKWISE_180)
                            .with(SHAPE, StairShape.OUTER_RIGHT);
                    case OUTER_RIGHT -> blockState.rotate(BlockRotation.CLOCKWISE_180)
                            .with(SHAPE, StairShape.OUTER_LEFT);
                    case STRAIGHT -> blockState.rotate(BlockRotation.CLOCKWISE_180);
                };
            }
        }
        return super.mirror(blockState, mirror);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player,
                                 BlockHitResult hit) {
        Hand hand = player.getActiveHand();
        if (this.toggleOnUse && (this.STATE != null) && player.isCreative() && player.getStackInHand(hand).isEmpty()) {
            state = state.cycle(this.STATE);
            world.setBlockState(pos, state, 10);
            world.syncWorldEvent(player, 1006, pos, 0);
            return ActionResult.success(world.isClient);
        } else {
            return ActionResult.PASS;
        }
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (state.isOf(oldState.getBlock())) {
            return;
        }
        super.onBlockAdded(state, world, pos, oldState, notify);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.isOf(newState.getBlock())) {
            return;
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    private static final String[] TAGS = {"stairs"};

    @Override
    public String[] getBlockTags() {
        return TAGS;
    }

    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }

}
