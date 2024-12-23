package com.westerosblocks.block.custom;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.sun.jdi.Mirror;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.WesterosBlockLifecycle;
import net.minecraft.block.*;
import net.minecraft.data.client.VariantSettings.Rotation;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import java.util.Map;

public class WCWallFanBlock extends Block implements WesterosBlockLifecycle {

    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    private final WesterosBlockDef def;
    private boolean allow_unsupported = false;

    private static final Map<Direction, VoxelShape> SHAPES = Maps.newEnumMap(
            ImmutableMap.of(Direction.NORTH,
                    VoxelShapes.cuboid(0.0, 4.0, 5.0, 16.0, 12.0, 16.0),
                    Direction.SOUTH,
                    VoxelShapes.cuboid(0.0, 4.0, 0.0, 16.0, 12.0, 11.0),
                    Direction.WEST,
                    VoxelShapes.cuboid(5.0, 4.0, 0.0, 16.0, 12.0, 16.0),
                    Direction.EAST,
                    VoxelShapes.cuboid(0.0, 4.0, 0.0, 11.0, 12.0, 16.0)));

    protected WCWallFanBlock(AbstractBlock.Settings settings, WesterosBlockDef def) {
        super(settings);
        this.def = def;
        String t = def.getType();
        if (t != null) {
            String[] toks = t.split(",");
            for (String tok : toks) {
                if (tok.equals("allow-unsupported")) {
                    allow_unsupported = true;
                    break;
                }
            }
        }
        BlockState defbs = getDefaultState().with(FACING, Direction.NORTH).with(WATERLOGGED, Boolean.valueOf(false));
        setDefaultState(defbs);
    }

    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        // Get waterlogging state
        BlockState bs = super.getPlacementState(ctx);
        if (bs == null) return null;
        FluidState fluidstate = ctx.getWorld().getFluidState(ctx.getBlockPos());
        bs = bs.with(WATERLOGGED, Boolean.valueOf(fluidstate.isIn(FluidTags.WATER)));
        // Get direction state
        Direction[] directionArray;
        World world = ctx.getWorld();
        BlockPos blockPos = ctx.getBlockPos();
        for (Direction direction : directionArray = ctx.getPlacementDirections()) {
            if (!direction.getAxis().isHorizontal() || !(bs = bs.with(FACING, direction.getOpposite())).canPlaceAt(world, blockPos))
                continue;
            return bs;
        }
        return null;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    protected boolean canPathfindThrough(BlockState state, NavigationType type) {
        return switch (type) {
            case LAND -> false;
            case WATER -> state.getFluidState().isIn(FluidTags.WATER);
            case AIR -> false;
            default -> false;
        };
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState,
                                                WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (direction.getOpposite() == state.get(FACING) && !state.canPlaceAt(world, pos)) {
            return Blocks.AIR.getDefaultState();
        }
        return state;
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        if (this.allow_unsupported) return true;
        Direction direction = state.get(FACING);
        BlockPos blockPos2 = pos.offset(direction.getOpposite());
        BlockState blockState2 = world.getBlockState(blockPos2);
        return blockState2.isSideSolidFullSquare(world, blockPos2, direction);
    }

    @Override
    protected boolean isTransparent(BlockState state, BlockView world, BlockPos pos) {
        return state.getFluidState().isEmpty();
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPES.get(state.get(FACING));
    }

    @Override
    protected BlockState rotate(BlockState blockState, BlockRotation rotation) {
        return blockState.with(FACING, rotation.rotate(blockState.get(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState blockState, BlockMirror mirror) {
        return blockState.rotate(mirror.getRotation(blockState.get(FACING)));
    }

    private static final String[] TAGS = {"fans"};

    @Override
    public String[] getBlockTags() {
        return TAGS;
    }
}
