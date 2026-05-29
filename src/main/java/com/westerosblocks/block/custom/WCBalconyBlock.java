package com.westerosblocks.block.custom;

import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.utils.ModProperties;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
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

public class WCBalconyBlock extends Block implements Waterloggable, WCBlockDef {
    protected BlockDefinition def;
    public static final BooleanProperty NORTH = Properties.NORTH;
    public static final BooleanProperty SOUTH = Properties.SOUTH;
    public static final BooleanProperty EAST = Properties.EAST;
    public static final BooleanProperty WEST = Properties.WEST;
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    public static final BooleanProperty WALL = BooleanProperty.of("wall");

    protected static ModProperties.StateProperty tempSTATE;
    public ModProperties.StateProperty STATE;
    protected boolean toggleOnUse = false;

    // Outline shapes — normal (outside block space)
    private static final VoxelShape VEAST = Block.createCuboidShape(16, 0, 0, 18, 14, 16);
    private static final VoxelShape VSOUTH = Block.createCuboidShape(0, 0, 16, 16, 14, 18);
    private static final VoxelShape VWEST = Block.createCuboidShape(-2, 0, 0, 0, 14, 16);
    private static final VoxelShape VNORTH = Block.createCuboidShape(0, 0, -2, 16, 14, 0);

    // Outline shapes — wall (inside block space)
    private static final VoxelShape VEAST_WALL = Block.createCuboidShape(14, 0, 0, 16, 14, 16);
    private static final VoxelShape VSOUTH_WALL = Block.createCuboidShape(0, 0, 14, 16, 14, 16);
    private static final VoxelShape VWEST_WALL = Block.createCuboidShape(0, 0, 0, 2, 14, 16);
    private static final VoxelShape VNORTH_WALL = Block.createCuboidShape(0, 0, 0, 16, 14, 2);

    // Collision shapes — match outline height so players can jump over
    private static final VoxelShape E_COLLISION = Block.createCuboidShape(14, 0, 0, 16, 14, 16);
    private static final VoxelShape S_COLLISION = Block.createCuboidShape(0, 0, 14, 16, 14, 16);
    private static final VoxelShape W_COLLISION = Block.createCuboidShape(0, 0, 0, 2, 14, 16);
    private static final VoxelShape N_COLLISION = Block.createCuboidShape(0, 0, 0, 16, 14, 2);

    private static final VoxelShape[] OUTLINE_SHAPES = precomputeShapes(VNORTH, VSOUTH, VEAST, VWEST);
    private static final VoxelShape[] WALL_OUTLINE_SHAPES = precomputeShapes(VNORTH_WALL, VSOUTH_WALL, VEAST_WALL, VWEST_WALL);
    private static final VoxelShape[] COLLISION_SHAPES = precomputeShapes(N_COLLISION, S_COLLISION, E_COLLISION, W_COLLISION);

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(BlockDefinition definition) {
            AbstractBlock.Settings settings = definition.makeSettings();
            ModProperties.StateProperty stateProperty = definition.buildStateProperty();
            boolean doToggleOnUse = definition.toggleOnUse();

            tempSTATE = stateProperty;

            return new WCBalconyBlock(settings, definition, doToggleOnUse);
        }
    }

    protected WCBalconyBlock(AbstractBlock.Settings settings, BlockDefinition def, boolean doToggleOnUse) {
        super(settings);
        this.def = def;
        this.toggleOnUse = doToggleOnUse;

        BlockState defbs = this.getDefaultState()
                .with(NORTH, false)
                .with(SOUTH, false)
                .with(EAST, false)
                .with(WEST, false)
                .with(WALL, false)
                .with(WATERLOGGED, false);
        if (STATE != null) {
            defbs = defbs.with(STATE, STATE.defValue);
        }
        this.setDefaultState(defbs);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(NORTH, SOUTH, EAST, WEST, WALL, WATERLOGGED);
        if (tempSTATE != null) {
            STATE = tempSTATE;
            tempSTATE = null;
            builder.add(STATE);
        }
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
        Direction direction = ctx.getHorizontalPlayerFacing();
        BlockState state = this.getDefaultState()
                .with(getPropertyForDirection(direction), true)
                .with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);

        // Check if facing direction has a solid wall
        BlockPos neighborPos = ctx.getBlockPos().offset(direction);
        BlockState neighborState = ctx.getWorld().getBlockState(neighborPos);
        if (neighborState.isSideSolidFullSquare(ctx.getWorld(), neighborPos, direction.getOpposite())) {
            state = state.with(WALL, true);
        }
        return state;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return state.get(WALL) ? WALL_OUTLINE_SHAPES[getShapeIndex(state)] : OUTLINE_SHAPES[getShapeIndex(state)];
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return COLLISION_SHAPES[getShapeIndex(state)];
    }

    @Override
    public VoxelShape getCullingShape(BlockState state, BlockView world, BlockPos pos) {
        return VoxelShapes.empty();
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        ItemStack heldItem = player.getMainHandStack();

        // Toggle STATE when creative + empty hand
        if (this.toggleOnUse && (this.STATE != null) && player.isCreative() && heldItem.isEmpty()) {
            if (state.contains(this.STATE)) {
                state = state.cycle(this.STATE);
                world.setBlockState(pos, state, Block.NOTIFY_ALL);
                world.syncWorldEvent(player, 1006, pos, 0);
                return ActionResult.success(world.isClient);
            }
        }

        // Add direction when holding a balcony item
        if (heldItem.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof WCBalconyBlock) {
            if (world.isClient) {
                return ActionResult.SUCCESS;
            }

            Direction direction = player.getHorizontalFacing();
            BooleanProperty directionProperty = getPropertyForDirection(direction);

            if (!state.get(directionProperty)) {
                BlockState newState = state.with(directionProperty, true);
                // Recheck wall adjacency with the new direction added
                newState = newState.with(WALL, hasAnyWallAdjacent(newState, world, pos));
                world.setBlockState(pos, newState, Block.NOTIFY_ALL);

                if (!player.getAbilities().creativeMode) {
                    heldItem.decrement(1);
                }

                return ActionResult.SUCCESS;
            }

            return ActionResult.PASS;
        }

        return ActionResult.PASS;
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient && !player.isCreative()) {
            int dropCount = 0;
            if (state.get(NORTH)) dropCount++;
            if (state.get(SOUTH)) dropCount++;
            if (state.get(EAST)) dropCount++;
            if (state.get(WEST)) dropCount++;

            // Drop extra items (the first one is dropped by the normal loot table)
            for (int i = 1; i < dropCount; i++) {
                ItemStack stack = new ItemStack(this);
                double x = pos.getX() + 0.5;
                double y = pos.getY() + 0.5;
                double z = pos.getZ() + 0.5;
                world.spawnEntity(new ItemEntity(world, x, y, z, stack));
            }
        }

        return super.onBreak(world, pos, state, player);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState,
            WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        // Recheck wall adjacency when a horizontal neighbor changes
        if (direction.getAxis().isHorizontal()) {
            state = state.with(WALL, hasAnyWallAdjacent(state, world, pos));
        }
        return state;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    public BlockDefinition getDefinition() {
        return def;
    }

    private static BooleanProperty getPropertyForDirection(Direction direction) {
        return switch (direction) {
            case NORTH -> NORTH;
            case SOUTH -> SOUTH;
            case EAST -> EAST;
            case WEST -> WEST;
            default -> NORTH;
        };
    }

    private static int getShapeIndex(BlockState state) {
        int index = 0;
        if (state.get(NORTH)) index |= 1;
        if (state.get(SOUTH)) index |= 2;
        if (state.get(EAST)) index |= 4;
        if (state.get(WEST)) index |= 8;
        return index;
    }

    private static VoxelShape[] precomputeShapes(VoxelShape north, VoxelShape south, VoxelShape east, VoxelShape west) {
        VoxelShape[] shapes = new VoxelShape[16];
        for (int i = 0; i < 16; i++) {
            VoxelShape shape = VoxelShapes.empty();
            if ((i & 1) != 0) shape = VoxelShapes.union(shape, north);
            if ((i & 2) != 0) shape = VoxelShapes.union(shape, south);
            if ((i & 4) != 0) shape = VoxelShapes.union(shape, east);
            if ((i & 8) != 0) shape = VoxelShapes.union(shape, west);
            shapes[i] = shape;
        }
        return shapes;
    }

    private boolean hasAnyWallAdjacent(BlockState state, WorldAccess world, BlockPos pos) {
        Direction[] directions = {Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST};
        for (Direction dir : directions) {
            if (state.get(getPropertyForDirection(dir))) {
                BlockPos neighborPos = pos.offset(dir);
                BlockState neighborState = world.getBlockState(neighborPos);
                if (neighborState.isSideSolidFullSquare(world, neighborPos, dir.getOpposite())) {
                    return true;
                }
            }
        }
        return false;
    }
}
