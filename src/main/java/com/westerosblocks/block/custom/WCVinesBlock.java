package com.westerosblocks.block.custom;

import com.westerosblocks.block.ModBlocks;
import com.westerosblocks.block.ModBlock;
import com.westerosblocks.block.ModBlockFactory;
import com.westerosblocks.block.ModBlockLifecycle;
import net.minecraft.block.*;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class WCVinesBlock extends VineBlock implements ModBlockLifecycle {
    public static class Factory extends ModBlockFactory {
        @Override
        public Block buildBlockClass(ModBlock def) {
            AbstractBlock.Settings settings = def.makeBlockSettings().nonOpaque();
            Block blk = new WCVinesBlock(settings, def);
            return def.registerRenderType(ModBlocks.registerBlock(def.blockName, blk), false, false);
        }
    }

    private final ModBlock def;
    private boolean allow_unsupported = false;
    private boolean no_climb = false;
    public boolean has_down = false;
    public static final BooleanProperty DOWN = Properties.DOWN;
    private static final VoxelShape UP_AABB = Block.createCuboidShape(0.0D, 15.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private static final VoxelShape DOWN_AABB = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);
    private static final VoxelShape WEST_AABB = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 1.0D, 16.0D, 16.0D);
    private static final VoxelShape EAST_AABB = Block.createCuboidShape(15.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private static final VoxelShape NORTH_AABB = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 1.0D);
    private static final VoxelShape SOUTH_AABB = Block.createCuboidShape(0.0D, 0.0D, 15.0D, 16.0D, 16.0D, 16.0D);
    private final Map<BlockState, VoxelShape> shapesCache;

    public static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = createDirectionPropertyMap();
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    protected WCVinesBlock(AbstractBlock.Settings settings, ModBlock def) {
        super(settings);
        this.def = def;
        String t = def.getType();
        if (t != null) {
            String[] toks = t.split(",");
            for (String tok : toks) {
                if (tok.equals("allow-unsupported")) {
                    allow_unsupported = true;
                }
                if (tok.equals("no-climb")) {
                    no_climb = true;
                }
                if (tok.equals("has-down")) {
                    has_down = true;
                }
            }
        }
        this.shapesCache = ImmutableMap.copyOf(getStateManager()
                .getStates()
                .stream()
                .collect(Collectors.toMap(Function.identity(), WCVinesBlock::calculateShape)));
        setDefaultState(this.getDefaultState()
                .with(UP, Boolean.FALSE)
                .with(NORTH, Boolean.FALSE)
                .with(EAST, Boolean.FALSE)
                .with(SOUTH, Boolean.FALSE)
                .with(WEST, Boolean.FALSE)
                .with(DOWN, Boolean.FALSE)
                .with(WATERLOGGED, Boolean.FALSE));
    }

    @Override
    public ModBlock getWBDefinition() {
        return def;
    }

    private static VoxelShape calculateShape(BlockState state) {
        VoxelShape voxelshape = VoxelShapes.empty();
        if (state.get(UP)) {
            voxelshape = UP_AABB;
        }
        if (state.get(DOWN)) {
            voxelshape = VoxelShapes.union(voxelshape, DOWN_AABB);
        }

        if (state.get(NORTH)) {
            voxelshape = VoxelShapes.union(voxelshape, NORTH_AABB);
        }

        if (state.get(SOUTH)) {
            voxelshape = VoxelShapes.union(voxelshape, SOUTH_AABB);
        }

        if (state.get(EAST)) {
            voxelshape = VoxelShapes.union(voxelshape, EAST_AABB);
        }

        if (state.get(WEST)) {
            voxelshape = VoxelShapes.union(voxelshape, WEST_AABB);
        }
        return voxelshape;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return this.shapesCache.get(state);
    }

    @Override
    public VoxelShape getSidesShape(BlockState state, BlockView world, BlockPos pos) {
        return VoxelShapes.empty();
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return allow_unsupported || this.hasFaces(this.getUpdatedState(state, world, pos));
    }

    private boolean hasFaces(BlockState state) {
        return this.countFaces(state) > 0;
    }

    private int countFaces(BlockState state) {
        int i = 0;

        for (BooleanProperty booleanproperty : PROPERTY_BY_DIRECTION.values()) {
            if (state.get(booleanproperty)) {
                ++i;
            }
        }

        return i;
    }

    private boolean canSupportAtFace(BlockView world, BlockPos pos, Direction direction) {
        if ((!has_down) && (direction == Direction.DOWN)) {
            return false;
        } else {
            BlockPos blockpos = pos.offset(direction);
            if (allow_unsupported) {
                return true;
            }
            if (isAcceptableNeighbour(world, blockpos, direction)) {
                return true;
            } else if (direction.getAxis() == Direction.Axis.Y) {
                return false;
            } else {
                BooleanProperty booleanproperty = PROPERTY_BY_DIRECTION.get(direction);
                BlockState blockstate = world.getBlockState(pos.up());
                return blockstate.isOf(this) && blockstate.get(booleanproperty);
            }
        }
    }

    public static boolean isAcceptableNeighbour(BlockView world, BlockPos pos, Direction direction) {
        BlockState neighborState = world.getBlockState(pos);
        return Block.isFaceFullSquare(neighborState.getCollisionShape(world, pos), direction.getOpposite());
    }

    private BlockState getUpdatedState(BlockState currentState, BlockView world, BlockPos pos) {
        BlockPos abovePos = pos.up();
        if (currentState.get(UP)) {
            currentState = currentState.with(UP, allow_unsupported || isAcceptableNeighbour(world, abovePos, Direction.DOWN));
        }

        for (Direction direction : Direction.Type.HORIZONTAL) {
            BooleanProperty directionProperty = getPropertyForFace(direction);
            if (currentState.get(directionProperty)) {
                boolean canSupport = allow_unsupported || this.canSupportAtFace(world, pos, direction);
                if (!canSupport) {
                    BlockState aboveState = world.getBlockState(abovePos);
                    canSupport = aboveState.isOf(this) && aboveState.get(directionProperty);
                }

                currentState = currentState.with(directionProperty, canSupport);
            }
        }

        if (has_down && currentState.get(DOWN)) {
            boolean canSupportDown = allow_unsupported || this.canSupportAtFace(world, pos, Direction.DOWN);
            currentState = currentState.with(DOWN, canSupportDown);
        }

        return currentState;
    }

    @Override
    public BlockState getStateForNeighborUpdate(
            BlockState state,
            Direction direction,
            BlockState neighborState,
            WorldAccess world,
            BlockPos pos,
            BlockPos neighborPos
    ) {
        if ((!has_down) && (direction == Direction.DOWN)) {
            return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
        } else {
            BlockState blockstate = this.getUpdatedState(state, world, pos);
            return !this.hasFaces(blockstate) ? Blocks.AIR.getDefaultState() : blockstate;
        }
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState blockstate = ctx.getWorld().getBlockState(ctx.getBlockPos());
        boolean flag = blockstate.isOf(this);
        BlockState blockstate1 = flag ? blockstate : getDefaultState();
        FluidState fluidstate = ctx.getWorld().getFluidState(ctx.getBlockPos());
        blockstate1 = blockstate1.with(WATERLOGGED, fluidstate.isIn(FluidTags.WATER));

        for (Direction direction : ctx.getPlacementDirections()) {
            if (has_down || (direction != Direction.DOWN)) {
                BooleanProperty booleanproperty = getPropertyForFace(direction);
                boolean flag1 = flag && blockstate.get(booleanproperty);
                if (!flag1 && this.canSupportAtFace(ctx.getWorld(), ctx.getBlockPos(), direction)) {
                    return blockstate1.with(booleanproperty, Boolean.TRUE);
                }
            }
        }
        return flag ? blockstate1 : null;
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
        builder.add(UP, NORTH, EAST, SOUTH, WEST, DOWN, WATERLOGGED);
    }

    public static BooleanProperty getPropertyForFace(Direction p_176267_0_) {
        return PROPERTY_BY_DIRECTION.get(p_176267_0_);
    }

    private static final String[] TAGS = {"climbable"};
    private static final String[] TAGS_NOCLIMB = {};

    @Override
    public String[] getBlockTags() {
        if (no_climb) return TAGS_NOCLIMB;
        return TAGS;
    }

    private static Map<Direction, BooleanProperty> createDirectionPropertyMap() {
        Map<Direction, BooleanProperty> map = new HashMap<>();
        map.put(Direction.NORTH, Properties.NORTH);
        map.put(Direction.EAST, Properties.EAST);
        map.put(Direction.SOUTH, Properties.SOUTH);
        map.put(Direction.WEST, Properties.WEST);
        map.put(Direction.UP, Properties.UP);
        map.put(Direction.DOWN, Properties.DOWN);
        return Map.copyOf(map);
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
        addCustomTooltip(tooltip);
        super.appendTooltip(stack, context, tooltip, options);
    }
}
