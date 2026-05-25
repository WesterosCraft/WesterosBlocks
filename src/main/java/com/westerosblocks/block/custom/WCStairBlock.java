package com.westerosblocks.block.custom;

import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.data.OptionsProperties;
import com.westerosblocks.utils.ModProperties;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.block.enums.StairShape;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.stream.IntStream;

public class WCStairBlock extends Block implements Waterloggable, WCBlockDef {
    protected BlockDefinition def;

    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final EnumProperty<BlockHalf> HALF = Properties.BLOCK_HALF;
    public static final EnumProperty<StairShape> SHAPE = Properties.STAIR_SHAPE;
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    public static final BooleanProperty UNCONNECT = BooleanProperty.of("unconnect");
    public static final IntProperty CONNECTSTATE = ModProperties.CONNECTSTATE;

    protected static ModProperties.StateProperty tempSTATE;
    public ModProperties.StateProperty STATE;

    protected static BooleanProperty tempUNCONNECT;
    protected static IntProperty tempCONNECTSTATE;

    protected boolean toggleOnUse = false;
    public final boolean unconnect;
    public final boolean connectstate;
    public final boolean no_uvlock;

    protected static final VoxelShape BOTTOM_AABB = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);
    protected static final VoxelShape TOP_AABB = Block.createCuboidShape(0.0, 8.0, 0.0, 16.0, 16.0, 16.0);
    protected static final VoxelShape OCTET_NNN = Block.createCuboidShape(0.0, 0.0, 0.0, 8.0, 8.0, 8.0);
    protected static final VoxelShape OCTET_NNP = Block.createCuboidShape(0.0, 0.0, 8.0, 8.0, 8.0, 16.0);
    protected static final VoxelShape OCTET_NPN = Block.createCuboidShape(0.0, 8.0, 0.0, 8.0, 16.0, 8.0);
    protected static final VoxelShape OCTET_NPP = Block.createCuboidShape(0.0, 8.0, 8.0, 8.0, 16.0, 16.0);
    protected static final VoxelShape OCTET_PNN = Block.createCuboidShape(8.0, 0.0, 0.0, 16.0, 8.0, 8.0);
    protected static final VoxelShape OCTET_PNP = Block.createCuboidShape(8.0, 0.0, 8.0, 16.0, 8.0, 16.0);
    protected static final VoxelShape OCTET_PPN = Block.createCuboidShape(8.0, 8.0, 0.0, 16.0, 16.0, 8.0);
    protected static final VoxelShape OCTET_PPP = Block.createCuboidShape(8.0, 8.0, 8.0, 16.0, 16.0, 16.0);

    protected static final VoxelShape[] TOP_SHAPES = makeShapes(TOP_AABB, OCTET_NNN, OCTET_PNN, OCTET_NNP, OCTET_PNP);
    protected static final VoxelShape[] BOTTOM_SHAPES = makeShapes(BOTTOM_AABB, OCTET_NPN, OCTET_PPN, OCTET_NPP, OCTET_PPP);
    private static final int[] SHAPE_BY_STATE = new int[]{12, 5, 3, 10, 14, 13, 7, 11, 13, 7, 11, 14, 8, 4, 1, 2, 4, 1, 2, 8};

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(BlockDefinition definition) {
            AbstractBlock.Settings settings = definition.makeSettings();
            ModProperties.StateProperty stateProperty = definition.buildStateProperty();

            if (stateProperty != null) {
                tempSTATE = stateProperty;
            }

            boolean doToggleOnUse = definition.toggleOnUse();

            // Extract options properties
            OptionsProperties opts = definition.getOptions();
            boolean doUnconnect = definition.isUnconnect();
            boolean doConnectstate = opts != null && Boolean.TRUE.equals(opts.getConnectstate());
            boolean noUvlock = opts != null && Boolean.TRUE.equals(opts.getNoUvlock());

            if (doUnconnect) {
                tempUNCONNECT = UNCONNECT;
            }
            if (doConnectstate) {
                tempCONNECTSTATE = CONNECTSTATE;
            }

            return new WCStairBlock(settings, definition, doToggleOnUse, doUnconnect, doConnectstate, noUvlock);
        }

    }

    public WCStairBlock(AbstractBlock.Settings settings, BlockDefinition def, boolean doToggleOnUse,
                       boolean doUnconnect, boolean doConnectstate, boolean noUvlock) {
        super(settings);
        this.def = def;

        this.toggleOnUse = doToggleOnUse;
        this.unconnect = doUnconnect;
        this.connectstate = doConnectstate;
        this.no_uvlock = noUvlock;

        BlockState defbs = this.getDefaultState()
            .with(FACING, Direction.NORTH)
            .with(HALF, BlockHalf.BOTTOM)
            .with(SHAPE, StairShape.STRAIGHT)
            .with(WATERLOGGED, false);

        if (doUnconnect) {
            defbs = defbs.with(UNCONNECT, false);
        }
        if (doConnectstate) {
            defbs = defbs.with(CONNECTSTATE, 0);
        }
        if (STATE != null) {
            defbs = defbs.with(STATE, STATE.defValue);
        }

        this.setDefaultState(defbs);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, HALF, SHAPE, WATERLOGGED);
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
            builder.add(STATE);
        }
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction clickedFace = ctx.getSide();
        BlockPos pos = ctx.getBlockPos();
        FluidState fluidState = ctx.getWorld().getFluidState(pos);

        BlockHalf half = (clickedFace == Direction.DOWN ||
                         (clickedFace != Direction.UP && ctx.getHitPos().y - pos.getY() > 0.5))
            ? BlockHalf.TOP : BlockHalf.BOTTOM;

        BlockState state = this.getDefaultState()
            .with(FACING, ctx.getHorizontalPlayerFacing())
            .with(HALF, half)
            .with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);

        state = state.with(SHAPE, getStairShape(state, ctx.getWorld(), pos));

        if (STATE != null) {
            state = state.with(STATE, STATE.defValue);
        }

        return state;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState,
            WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        if (unconnect && state.get(UNCONNECT)) {
            return state;
        }

        if (direction.getAxis().isHorizontal()) {
            return state.with(SHAPE, getStairShape(state, world, pos));
        }

        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return (state.get(HALF) == BlockHalf.TOP ? TOP_SHAPES : BOTTOM_SHAPES)[SHAPE_BY_STATE[getShapeIndex(state)]];
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (this.toggleOnUse && (this.STATE != null) && player.isCreative() && player.getMainHandStack().isEmpty()) {
            if (state.contains(this.STATE)) {
                state = state.cycle(this.STATE);
                world.setBlockState(pos, state, Block.NOTIFY_ALL);
                world.syncWorldEvent(player, 1006, pos, 0);
                return ActionResult.success(world.isClient);
            }
        }
        return ActionResult.PASS;
    }

    private int getShapeIndex(BlockState state) {
        return state.get(SHAPE).ordinal() * 4 + state.get(FACING).getHorizontal();
    }

    private static StairShape getStairShape(BlockState state, BlockView world, BlockPos pos) {
        Direction facing = state.get(FACING);
        BlockState adjacentState = world.getBlockState(pos.offset(facing));

        if (isStairs(adjacentState) && state.get(HALF) == adjacentState.get(HALF)) {
            Direction adjacentFacing = adjacentState.get(FACING);
            if (adjacentFacing.getAxis() != facing.getAxis() && canTakeShape(state, world, pos, adjacentFacing.getOpposite())) {
                if (adjacentFacing == facing.rotateYCounterclockwise()) {
                    return StairShape.OUTER_LEFT;
                }
                return StairShape.OUTER_RIGHT;
            }
        }

        BlockState oppositeState = world.getBlockState(pos.offset(facing.getOpposite()));
        if (isStairs(oppositeState) && state.get(HALF) == oppositeState.get(HALF)) {
            Direction oppositeFacing = oppositeState.get(FACING);
            if (oppositeFacing.getAxis() != facing.getAxis() && canTakeShape(state, world, pos, oppositeFacing)) {
                if (oppositeFacing == facing.rotateYCounterclockwise()) {
                    return StairShape.INNER_LEFT;
                }
                return StairShape.INNER_RIGHT;
            }
        }

        return StairShape.STRAIGHT;
    }

    private static boolean canTakeShape(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        BlockState adjacentState = world.getBlockState(pos.offset(direction));
        return !isStairs(adjacentState) || adjacentState.get(FACING) != state.get(FACING) || adjacentState.get(HALF) != state.get(HALF);
    }

    private static boolean isStairs(BlockState state) {
        return state.getBlock() instanceof WCStairBlock;
    }

    private static VoxelShape[] makeShapes(VoxelShape base, VoxelShape octet1, VoxelShape octet2, VoxelShape octet3, VoxelShape octet4) {
        return IntStream.range(0, 16)
            .mapToObj(i -> makeStairShape(i, base, octet1, octet2, octet3, octet4))
            .toArray(VoxelShape[]::new);
    }

    private static VoxelShape makeStairShape(int index, VoxelShape base, VoxelShape octet1, VoxelShape octet2, VoxelShape octet3, VoxelShape octet4) {
        VoxelShape shape = base;
        if ((index & 1) != 0) {
            shape = VoxelShapes.union(shape, octet1);
        }
        if ((index & 2) != 0) {
            shape = VoxelShapes.union(shape, octet2);
        }
        if ((index & 4) != 0) {
            shape = VoxelShapes.union(shape, octet3);
        }
        if ((index & 8) != 0) {
            shape = VoxelShapes.union(shape, octet4);
        }
        return shape;
    }

    public BlockDefinition getDefinition() {
        return def;
    }
}
