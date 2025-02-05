package com.westerosblocks.block.custom;

import com.westerosblocks.block.ModBlocks;
import com.westerosblocks.block.ModBlock;
import com.westerosblocks.block.ModBlockFactory;
import com.westerosblocks.block.ModBlockLifecycle;
import net.minecraft.block.*;
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
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.stream.IntStream;

public class WCStairBlock extends Block implements ModBlockLifecycle {

    public static class Factory extends ModBlockFactory {
        @Override
        public Block buildBlockClass(ModBlock def) {
            AbstractBlock.Settings settings = def.makeBlockSettings();
            // See if we have a state property
            ModBlock.StateProperty state = def.buildStateProperty();
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

            return def.registerRenderType(ModBlocks.registerBlock(def.blockName, blk), false, false);
        }
    }

    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final EnumProperty<BlockHalf> HALF = Properties.BLOCK_HALF;
    public static final EnumProperty<StairShape> SHAPE = Properties.STAIR_SHAPE;
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    protected static final VoxelShape TOP_SHAPE = Block.createCuboidShape(0.0, 8.0, 0.0, 16.0, 16.0, 16.0);
    protected static final VoxelShape BOTTOM_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);
    protected static final VoxelShape BOTTOM_NORTH_WEST_CORNER_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 8.0, 8.0, 8.0);
    protected static final VoxelShape BOTTOM_SOUTH_WEST_CORNER_SHAPE = Block.createCuboidShape(0.0, 0.0, 8.0, 8.0, 8.0, 16.0);
    protected static final VoxelShape TOP_NORTH_WEST_CORNER_SHAPE = Block.createCuboidShape(0.0, 8.0, 0.0, 8.0, 16.0, 8.0);
    protected static final VoxelShape TOP_SOUTH_WEST_CORNER_SHAPE = Block.createCuboidShape(0.0, 8.0, 8.0, 8.0, 16.0, 16.0);
    protected static final VoxelShape BOTTOM_NORTH_EAST_CORNER_SHAPE = Block.createCuboidShape(8.0, 0.0, 0.0, 16.0, 8.0, 8.0);
    protected static final VoxelShape BOTTOM_SOUTH_EAST_CORNER_SHAPE = Block.createCuboidShape(8.0, 0.0, 8.0, 16.0, 8.0, 16.0);
    protected static final VoxelShape TOP_NORTH_EAST_CORNER_SHAPE = Block.createCuboidShape(8.0, 8.0, 0.0, 16.0, 16.0, 8.0);
    protected static final VoxelShape TOP_SOUTH_EAST_CORNER_SHAPE = Block.createCuboidShape(8.0, 8.0, 8.0, 16.0, 16.0, 16.0);

    private final ModBlock def;
    public static final BooleanProperty UNCONNECT = BooleanProperty.of("unconnect");
    protected static BooleanProperty tempUNCONNECT;
    public final boolean unconnect;

    public static final IntProperty CONNECTSTATE = IntProperty.of("connectstate", 0, 3);
    protected static IntProperty tempCONNECTSTATE;
    public final boolean connectstate;

    protected static ModBlock.StateProperty tempSTATE;
    protected ModBlock.StateProperty STATE;

    protected boolean toggleOnUse = false;

    public final boolean no_uvlock;

    protected static final VoxelShape[] TOP_SHAPES = composeShapes(TOP_SHAPE, BOTTOM_NORTH_WEST_CORNER_SHAPE, BOTTOM_NORTH_EAST_CORNER_SHAPE, BOTTOM_SOUTH_WEST_CORNER_SHAPE, BOTTOM_SOUTH_EAST_CORNER_SHAPE);
    protected static final VoxelShape[] BOTTOM_SHAPES = composeShapes(BOTTOM_SHAPE, TOP_NORTH_WEST_CORNER_SHAPE, TOP_NORTH_EAST_CORNER_SHAPE, TOP_SOUTH_WEST_CORNER_SHAPE, TOP_SOUTH_EAST_CORNER_SHAPE);
    private static final int[] SHAPE_INDICES = new int[]{12, 5, 3, 10, 14, 13, 7, 11, 13, 7, 11, 14, 8, 4, 1, 2, 4, 1, 2, 8};

    protected WCStairBlock(AbstractBlock.Settings settings, ModBlock def, boolean doUnconnect, boolean doConnectstate) {
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
        BlockState defbs = this.getDefaultState()
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
                .with(FACING, ctx.getHorizontalPlayerFacing()))
                .with(HALF, direction == Direction.DOWN
                        || direction != Direction.UP
                        && ctx.getHitPos().y - (double) blockPos.getY() > 0.5 ? BlockHalf.TOP : BlockHalf.BOTTOM)
                .with(WATERLOGGED, fluidState.isOf(Fluids.WATER));
        return blockState.with(SHAPE, getStairShape(blockState, ctx.getWorld(), blockPos));
    }

    protected static VoxelShape[] composeShapes(VoxelShape base, VoxelShape northWest, VoxelShape northEast, VoxelShape southWest, VoxelShape southEast) {
        return IntStream.range(0, 16)
                .mapToObj(i -> composeShape(i, base, northWest, northEast, southWest, southEast))
                .toArray(VoxelShape[]::new);
    }

    @Override
    protected boolean hasSidedTransparency(BlockState state) {
        return true;
    }

    private static VoxelShape composeShape(int i, VoxelShape base, VoxelShape northWest, VoxelShape northEast, VoxelShape southWest, VoxelShape southEast) {
        VoxelShape shape = base;
        if ((i & 1) != 0) shape = VoxelShapes.union(shape, northWest);
        if ((i & 2) != 0) shape = VoxelShapes.union(shape, northEast);
        if ((i & 4) != 0) shape = VoxelShapes.union(shape, southWest);
        if ((i & 8) != 0) shape = VoxelShapes.union(shape, southEast);
        return shape;
    }

    @Override
    public boolean isTransparent(BlockState state, BlockView world, BlockPos pos) {
        return false;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return (state.get(HALF) == BlockHalf.TOP ? TOP_SHAPES : BOTTOM_SHAPES)[SHAPE_INDICES[getShapeIndexIndex(state)]];
    }

    private int getShapeIndexIndex(BlockState state) {
        return state.get(SHAPE).ordinal() * 4 + state.get(FACING).getHorizontal();
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
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
        BlockState neighborState = world.getBlockState(blockPos.offset(facing));

        if (isStairs(neighborState)
                && blockState.get(HALF) == neighborState.get(HALF)) {
            Direction neighborFacing = neighborState.get(FACING);
            if (neighborFacing.getAxis() != facing.getAxis()
                    && canTakeShape(blockState, world, blockPos, neighborFacing.getOpposite())) {
                if (neighborFacing == facing.rotateCounterclockwise(Direction.Axis.Y)) {
                    return StairShape.OUTER_LEFT;
                }
                return StairShape.OUTER_RIGHT;
            }
        }

        BlockState oppositeState = world.getBlockState(blockPos.offset(facing.getOpposite()));
        if (isStairs(oppositeState) && blockState.get(HALF) == oppositeState.get(HALF)) {
            direction = oppositeState.get(FACING);
            if (direction.getAxis() != facing.getAxis() && canTakeShape(blockState, world, blockPos, direction)) {
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
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
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
    public ModBlock getWBDefinition() {
        return def;
    }

}
