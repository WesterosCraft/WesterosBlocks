package com.westerosblocks.block.custom;

import com.westerosblocks.util.ModUtils;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.util.StringIdentifiable;

public class WCArrowSlitBlock extends Block {
    public static final EnumProperty<ArrowSlitType> TYPE = EnumProperty.of("type", ArrowSlitType.class);
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

    // Define the base shapes for each part of the arrow slit
    private static final VoxelShape LEFT_WALL = Block.createCuboidShape(0, 0, 3, 3, 16, 13);
    private static final VoxelShape RIGHT_WALL = Block.createCuboidShape(13, 0, 3, 16, 16, 13);
    private static final VoxelShape LEFT_SLIT_WALL = Block.createCuboidShape(3, 2, 3, 6, 14, 13);
    private static final VoxelShape RIGHT_SLIT_WALL = Block.createCuboidShape(10, 2, 3, 13, 14, 13);
    private static final VoxelShape BOTTOM_BASE = Block.createCuboidShape(0, 0, 0, 16, 4, 16);
    private static final VoxelShape TOP_BASE = Block.createCuboidShape(0, 12, 0, 16, 16, 16);
    private static final VoxelShape BOTTOM_LEDGE_LEFT = Block.createCuboidShape(0, 15, 3, 3, 16, 13);
    private static final VoxelShape BOTTOM_LEDGE_RIGHT = Block.createCuboidShape(13, 15, 3, 16, 16, 13);
    private static final VoxelShape TOP_LEDGE_LEFT = Block.createCuboidShape(0, 0, 3, 3, 1, 13);
    private static final VoxelShape TOP_LEDGE_RIGHT = Block.createCuboidShape(13, 0, 3, 16, 1, 13);

    // Define shapes for each direction and type
    private static final VoxelShape NORTH_SINGLE = VoxelShapes.union(
        Block.createCuboidShape(0, 0, 3, 3, 16, 13),
        Block.createCuboidShape(13, 0, 3, 16, 16, 13),
        Block.createCuboidShape(0, 4, 3, 6, 12, 13),
        Block.createCuboidShape(10, 4, 3, 16, 12, 13),
        BOTTOM_BASE, TOP_BASE
    );
    private static final VoxelShape NORTH_TOP = VoxelShapes.union(
        Block.createCuboidShape(0, 0, 3, 3, 16, 13),
        Block.createCuboidShape(13, 0, 3, 16, 16, 13),
        Block.createCuboidShape(0, 1, 3, 6, 12, 13),
        Block.createCuboidShape(10, 1, 3, 16, 12, 13),
        TOP_BASE, TOP_LEDGE_LEFT, TOP_LEDGE_RIGHT
    );
    private static final VoxelShape NORTH_BOTTOM = VoxelShapes.union(
        Block.createCuboidShape(0, 0, 3, 3, 16, 13),
        Block.createCuboidShape(13, 0, 3, 16, 16, 13),
        Block.createCuboidShape(0, 4, 3, 6, 15, 13),
        Block.createCuboidShape(10, 4, 3, 16, 15, 13),
        BOTTOM_BASE, BOTTOM_LEDGE_LEFT, BOTTOM_LEDGE_RIGHT
    );
    private static final VoxelShape NORTH_MID = VoxelShapes.union(
        LEFT_WALL, RIGHT_WALL, LEFT_SLIT_WALL, RIGHT_SLIT_WALL
    );

    private static final VoxelShape SOUTH_SINGLE = VoxelShapes.union(
        Block.createCuboidShape(0, 0, 3, 3, 16, 13),
        Block.createCuboidShape(13, 0, 3, 16, 16, 13),
        Block.createCuboidShape(0, 4, 3, 6, 12, 13),
        Block.createCuboidShape(10, 4, 3, 16, 12, 13),
        Block.createCuboidShape(0, 0, 0, 16, 4, 16),
        Block.createCuboidShape(0, 12, 0, 16, 16, 16)
    );
    private static final VoxelShape SOUTH_TOP = VoxelShapes.union(
        Block.createCuboidShape(0, 0, 3, 3, 16, 13),
        Block.createCuboidShape(13, 0, 3, 16, 16, 13),
        Block.createCuboidShape(0, 1, 3, 6, 12, 13),
        Block.createCuboidShape(10, 1, 3, 16, 12, 13),
        Block.createCuboidShape(0, 12, 0, 16, 16, 16),
        Block.createCuboidShape(0, 0, 3, 3, 1, 13),
        Block.createCuboidShape(13, 0, 3, 16, 1, 13)
    );
    private static final VoxelShape SOUTH_BOTTOM = VoxelShapes.union(
        Block.createCuboidShape(0, 0, 3, 3, 16, 13),
        Block.createCuboidShape(13, 0, 3, 16, 16, 13),
        Block.createCuboidShape(0, 4, 3, 6, 15, 13),
        Block.createCuboidShape(10, 4, 3, 16, 15, 13),
        Block.createCuboidShape(0, 0, 0, 16, 4, 16),
        Block.createCuboidShape(0, 15, 3, 3, 16, 13),
        Block.createCuboidShape(13, 15, 3, 16, 16, 13)
    );
    private static final VoxelShape SOUTH_MID = VoxelShapes.union(
        Block.createCuboidShape(0, 0, 3, 3, 16, 13),
        Block.createCuboidShape(13, 0, 3, 16, 16, 13),
        Block.createCuboidShape(3, 2, 3, 6, 14, 13),
        Block.createCuboidShape(10, 2, 3, 13, 14, 13)
    );

    private static final VoxelShape EAST_SINGLE = VoxelShapes.union(
        Block.createCuboidShape(3, 0, 0, 13, 16, 3),
        Block.createCuboidShape(3, 0, 13, 13, 16, 16),
        Block.createCuboidShape(3, 4, 3, 13, 12, 6),
        Block.createCuboidShape(3, 4, 10, 13, 12, 13),
        Block.createCuboidShape(0, 0, 0, 16, 4, 16),
        Block.createCuboidShape(0, 12, 0, 16, 16, 16)
    );
    private static final VoxelShape EAST_TOP = VoxelShapes.union(
        Block.createCuboidShape(3, 0, 0, 13, 16, 3),
        Block.createCuboidShape(3, 0, 13, 13, 16, 16),
        Block.createCuboidShape(3, 1, 3, 13, 12, 6),
        Block.createCuboidShape(3, 1, 10, 13, 12, 13),
        Block.createCuboidShape(0, 12, 0, 16, 16, 16),
        Block.createCuboidShape(3, 0, 0, 13, 1, 3),
        Block.createCuboidShape(3, 0, 13, 13, 1, 16)
    );
    private static final VoxelShape EAST_BOTTOM = VoxelShapes.union(
        Block.createCuboidShape(3, 0, 0, 13, 16, 3),
        Block.createCuboidShape(3, 0, 13, 13, 16, 16),
        Block.createCuboidShape(3, 4, 3, 13, 15, 6),
        Block.createCuboidShape(3, 4, 10, 13, 15, 13),
        Block.createCuboidShape(0, 0, 0, 16, 4, 16),
        Block.createCuboidShape(3, 15, 0, 13, 16, 3),
        Block.createCuboidShape(3, 15, 13, 13, 16, 16)
    );
    private static final VoxelShape EAST_MID = VoxelShapes.union(
        Block.createCuboidShape(3, 0, 0, 13, 16, 3),
        Block.createCuboidShape(3, 0, 13, 13, 16, 16),
        Block.createCuboidShape(3, 2, 3, 13, 14, 6),
        Block.createCuboidShape(3, 2, 10, 13, 14, 13)
    );

    private static final VoxelShape WEST_SINGLE = VoxelShapes.union(
        Block.createCuboidShape(3, 0, 0, 13, 16, 3),
        Block.createCuboidShape(3, 0, 13, 13, 16, 16),
        Block.createCuboidShape(3, 4, 3, 13, 12, 6),
        Block.createCuboidShape(3, 4, 10, 13, 12, 13),
        Block.createCuboidShape(0, 0, 0, 16, 4, 16),
        Block.createCuboidShape(0, 12, 0, 16, 16, 16)
    );
    private static final VoxelShape WEST_TOP = VoxelShapes.union(
        Block.createCuboidShape(3, 0, 0, 13, 16, 3),
        Block.createCuboidShape(3, 0, 13, 13, 16, 16),
        Block.createCuboidShape(3, 1, 3, 13, 12, 6),
        Block.createCuboidShape(3, 1, 10, 13, 12, 13),
        Block.createCuboidShape(0, 12, 0, 16, 16, 16),
        Block.createCuboidShape(3, 0, 0, 13, 1, 3),
        Block.createCuboidShape(3, 0, 13, 13, 1, 16)
    );
    private static final VoxelShape WEST_BOTTOM = VoxelShapes.union(
        Block.createCuboidShape(3, 0, 0, 13, 16, 3),
        Block.createCuboidShape(3, 0, 13, 13, 16, 16),
        Block.createCuboidShape(3, 4, 3, 13, 15, 6),
        Block.createCuboidShape(3, 4, 10, 13, 15, 13),
        Block.createCuboidShape(0, 0, 0, 16, 4, 16),
        Block.createCuboidShape(3, 15, 0, 13, 16, 3),
        Block.createCuboidShape(3, 15, 13, 13, 16, 16)
    );
    private static final VoxelShape WEST_MID = VoxelShapes.union(
        Block.createCuboidShape(3, 0, 0, 13, 16, 3),
        Block.createCuboidShape(3, 0, 13, 13, 16, 16),
        Block.createCuboidShape(3, 2, 3, 13, 14, 6),
        Block.createCuboidShape(3, 2, 10, 13, 14, 13)
    );

    private final String blockName;
    private final String creativeTab;

    public WCArrowSlitBlock(AbstractBlock.Settings settings, String blockName, String creativeTab) {
        super(settings);
        this.blockName = blockName;
        this.creativeTab = creativeTab;
        setDefaultState(getDefaultState()
            .with(TYPE, ArrowSlitType.SINGLE)
            .with(FACING, Direction.NORTH));
    }

    public String getBlockName() {
        return blockName;
    }

    public String getCreativeTab() {
        return creativeTab;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(TYPE, FACING);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (direction == Direction.UP || direction == Direction.DOWN) {
            boolean hasTop = world.getBlockState(pos.up()).isOf(this);
            boolean hasBottom = world.getBlockState(pos.down()).isOf(this);

            ArrowSlitType newType;
            if (hasTop && hasBottom) {
                newType = ArrowSlitType.MIDDLE;
            } else if (hasTop) {
                newType = ArrowSlitType.BOTTOM;
            } else if (hasBottom) {
                newType = ArrowSlitType.TOP;
            } else {
                newType = ArrowSlitType.SINGLE;
            }

            return state.with(TYPE, newType);
        }
        return state;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState()
            .with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        // The getStateForNeighborUpdate method will handle state updates for neighboring blocks
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        // The getStateForNeighborUpdate method will handle state updates for neighboring blocks
        return super.onBreak(world, pos, state, player);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return getShapeForState(state);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return getShapeForState(state);
    }

    private VoxelShape getShapeForState(BlockState state) {
        Direction facing = state.get(FACING);
        ArrowSlitType type = state.get(TYPE);
        
        return switch (facing) {
            case NORTH -> switch (type) {
                case SINGLE -> NORTH_SINGLE;
                case TOP -> NORTH_TOP;
                case BOTTOM -> NORTH_BOTTOM;
                case MIDDLE -> NORTH_MID;
            };
            case SOUTH -> switch (type) {
                case SINGLE -> SOUTH_SINGLE;
                case TOP -> SOUTH_TOP;
                case BOTTOM -> SOUTH_BOTTOM;
                case MIDDLE -> SOUTH_MID;
            };
            case EAST -> switch (type) {
                case SINGLE -> EAST_SINGLE;
                case TOP -> EAST_TOP;
                case BOTTOM -> EAST_BOTTOM;
                case MIDDLE -> EAST_MID;
            };
            case WEST -> switch (type) {
                case SINGLE -> WEST_SINGLE;
                case TOP -> WEST_TOP;
                case BOTTOM -> WEST_BOTTOM;
                case MIDDLE -> WEST_MID;
            };
            default -> NORTH_SINGLE;
        };
    }

    public enum ArrowSlitType implements StringIdentifiable {
        SINGLE("single"),
        TOP("top"),
        BOTTOM("bottom"),
        MIDDLE("middle");

        private final String name;

        ArrowSlitType(String name) {
            this.name = name;
        }

        @Override
        public String asString() {
            return this.name;
        }
    }
} 