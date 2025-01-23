package com.westerosblocks.block.custom;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
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

public class WCWallFanBlock extends Block implements Waterloggable, WesterosBlockLifecycle {
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    private final WesterosBlockDef def;
    private final boolean allowUnsupported;

    private static final Map<Direction, VoxelShape> SHAPES = Maps.newEnumMap(ImmutableMap.of(
            Direction.NORTH, Block.createCuboidShape(0.0, 4.0, 5.0, 16.0, 12.0, 16.0),
            Direction.SOUTH, Block.createCuboidShape(0.0, 4.0, 0.0, 16.0, 12.0, 11.0),
            Direction.WEST, Block.createCuboidShape(5.0, 4.0, 0.0, 16.0, 12.0, 16.0),
            Direction.EAST, Block.createCuboidShape(0.0, 4.0, 0.0, 11.0, 12.0, 16.0)
    ));

    protected WCWallFanBlock(AbstractBlock.Settings settings, WesterosBlockDef def) {
        super(settings);
        this.def = def;

        boolean allowUnsupported = false;
        String type = def.getType();
        if (type != null) {
            String[] tokens = type.split(",");
            for (String token : tokens) {
                if (token.equals("allow-unsupported")) {
                    allowUnsupported = true;
                    break;
                }
            }
        }
        this.allowUnsupported = allowUnsupported;

        setDefaultState(getStateManager().getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(WATERLOGGED, false));
    }

    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState state = getDefaultState();
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
        World world = ctx.getWorld();
        BlockPos pos = ctx.getBlockPos();

        for (Direction direction : ctx.getPlacementDirections()) {
            if (direction.getAxis().isHorizontal()) {
                Direction opposite = direction.getOpposite();
                state = state.with(FACING, opposite);
                if (state.canPlaceAt(world, pos)) {
                    return state.with(WATERLOGGED, fluidState.isIn(FluidTags.WATER));
                }
            }
        }
        return null;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState,
                                                WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (direction.getOpposite() == state.get(FACING) && !canPlaceAt(state, world, pos)) {
            return Blocks.AIR.getDefaultState();
        }

        if (state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        return state;
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        if (allowUnsupported) return true;
        Direction direction = state.get(FACING);
        BlockPos attachPos = pos.offset(direction.getOpposite());
        return world.getBlockState(attachPos).isSideSolidFullSquare(world, attachPos, direction);
    }

    @Override
    public boolean isTransparent(BlockState state, BlockView world, BlockPos pos) {
        return !state.getFluidState().isEmpty() || state.get(WATERLOGGED);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPES.get(state.get(FACING));
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
    protected boolean canPathfindThrough(BlockState state, NavigationType type) {
        return switch (type) {
            case LAND -> false;
            case WATER -> state.getFluidState().isIn(FluidTags.WATER);
            case AIR -> false;
            default -> false;
        };
    }

    private static final String[] TAGS = {"fans"};

    @Override
    public String[] getBlockTags() {
        return TAGS;
    }
}
