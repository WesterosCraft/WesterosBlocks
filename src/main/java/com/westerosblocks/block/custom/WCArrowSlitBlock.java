package com.westerosblocks.block.custom;

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
    private static final VoxelShape LEFT_WALL = Block.createCuboidShape(0, 0, 0, 6, 16, 16);
    private static final VoxelShape RIGHT_WALL = Block.createCuboidShape(10, 0, 0, 16, 16, 16);
    private static final VoxelShape BOTTOM_LEDGE = Block.createCuboidShape(6, 0, 0, 10, 1, 16);
    private static final VoxelShape TOP_LEDGE = Block.createCuboidShape(6, 15, 0, 10, 16, 16);

    // Define shapes for each direction and type
    private static final VoxelShape NORTH_SINGLE = VoxelShapes.union(LEFT_WALL, RIGHT_WALL, BOTTOM_LEDGE, TOP_LEDGE);
    private static final VoxelShape NORTH_TOP = VoxelShapes.union(LEFT_WALL, RIGHT_WALL, TOP_LEDGE);
    private static final VoxelShape NORTH_BOTTOM = VoxelShapes.union(LEFT_WALL, RIGHT_WALL, BOTTOM_LEDGE);
    private static final VoxelShape NORTH_MID = VoxelShapes.union(LEFT_WALL, RIGHT_WALL);

    private static final VoxelShape SOUTH_SINGLE = VoxelShapes.union(
        Block.createCuboidShape(0, 0, 0, 6, 16, 16),
        Block.createCuboidShape(10, 0, 0, 16, 16, 16),
        Block.createCuboidShape(6, 0, 0, 10, 1, 16),
        Block.createCuboidShape(6, 15, 0, 10, 16, 16)
    );
    private static final VoxelShape SOUTH_TOP = VoxelShapes.union(
        Block.createCuboidShape(0, 0, 0, 6, 16, 16),
        Block.createCuboidShape(10, 0, 0, 16, 16, 16),
        Block.createCuboidShape(6, 15, 0, 10, 16, 16)
    );
    private static final VoxelShape SOUTH_BOTTOM = VoxelShapes.union(
        Block.createCuboidShape(0, 0, 0, 6, 16, 16),
        Block.createCuboidShape(10, 0, 0, 16, 16, 16),
        Block.createCuboidShape(6, 0, 0, 10, 1, 16)
    );
    private static final VoxelShape SOUTH_MID = VoxelShapes.union(
        Block.createCuboidShape(0, 0, 0, 6, 16, 16),
        Block.createCuboidShape(10, 0, 0, 16, 16, 16)
    );

    private static final VoxelShape EAST_SINGLE = VoxelShapes.union(
        Block.createCuboidShape(0, 0, 0, 16, 16, 6),
        Block.createCuboidShape(0, 0, 10, 16, 16, 16),
        Block.createCuboidShape(0, 0, 6, 16, 1, 10),
        Block.createCuboidShape(0, 15, 6, 16, 16, 10)
    );
    private static final VoxelShape EAST_TOP = VoxelShapes.union(
        Block.createCuboidShape(0, 0, 0, 16, 16, 6),
        Block.createCuboidShape(0, 0, 10, 16, 16, 16),
        Block.createCuboidShape(0, 15, 6, 16, 16, 10)
    );
    private static final VoxelShape EAST_BOTTOM = VoxelShapes.union(
        Block.createCuboidShape(0, 0, 0, 16, 16, 6),
        Block.createCuboidShape(0, 0, 10, 16, 16, 16),
        Block.createCuboidShape(0, 0, 6, 16, 1, 10)
    );
    private static final VoxelShape EAST_MID = VoxelShapes.union(
        Block.createCuboidShape(0, 0, 0, 16, 16, 6),
        Block.createCuboidShape(0, 0, 10, 16, 16, 16)
    );

    private static final VoxelShape WEST_SINGLE = VoxelShapes.union(
        Block.createCuboidShape(0, 0, 0, 16, 16, 6),
        Block.createCuboidShape(0, 0, 10, 16, 16, 16),
        Block.createCuboidShape(0, 0, 6, 16, 1, 10),
        Block.createCuboidShape(0, 15, 6, 16, 16, 10)
    );
    private static final VoxelShape WEST_TOP = VoxelShapes.union(
        Block.createCuboidShape(0, 0, 0, 16, 16, 6),
        Block.createCuboidShape(0, 0, 10, 16, 16, 16),
        Block.createCuboidShape(0, 15, 6, 16, 16, 10)
    );
    private static final VoxelShape WEST_BOTTOM = VoxelShapes.union(
        Block.createCuboidShape(0, 0, 0, 16, 16, 6),
        Block.createCuboidShape(0, 0, 10, 16, 16, 16),
        Block.createCuboidShape(0, 0, 6, 16, 1, 10)
    );
    private static final VoxelShape WEST_MID = VoxelShapes.union(
        Block.createCuboidShape(0, 0, 0, 16, 16, 6),
        Block.createCuboidShape(0, 0, 10, 16, 16, 16)
    );

    private final String blockName;
    private final String creativeTab;

    protected WCArrowSlitBlock(AbstractBlock.Settings settings, String blockName, String creativeTab) {
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
                newType = ArrowSlitType.MID;
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
                case MID -> NORTH_MID;
            };
            case SOUTH -> switch (type) {
                case SINGLE -> SOUTH_SINGLE;
                case TOP -> SOUTH_TOP;
                case BOTTOM -> SOUTH_BOTTOM;
                case MID -> SOUTH_MID;
            };
            case EAST -> switch (type) {
                case SINGLE -> EAST_SINGLE;
                case TOP -> EAST_TOP;
                case BOTTOM -> EAST_BOTTOM;
                case MID -> EAST_MID;
            };
            case WEST -> switch (type) {
                case SINGLE -> WEST_SINGLE;
                case TOP -> WEST_TOP;
                case BOTTOM -> WEST_BOTTOM;
                case MID -> WEST_MID;
            };
            default -> NORTH_SINGLE;
        };
    }

    public static class Builder {
        private String blockName;
        private String creativeTab;
        private float hardness = 2.0f;
        private float resistance = 6.0f;
        private String material = "stone";
        private String stepSound = "stone";
        private int harvestLevel = 1;

        public Builder(String blockName) {
            this.blockName = blockName;
        }

        public Builder creativeTab(String creativeTab) {
            this.creativeTab = creativeTab;
            return this;
        }

        public Builder hardness(float hardness) {
            this.hardness = hardness;
            return this;
        }

        public Builder resistance(float resistance) {
            this.resistance = resistance;
            return this;
        }

        public Builder material(String material) {
            this.material = material;
            return this;
        }

        public Builder stepSound(String stepSound) {
            this.stepSound = stepSound;
            return this;
        }

        public Builder harvestLevel(int level) {
            this.harvestLevel = level;
            return this;
        }

        public WCArrowSlitBlock build() {
            if (blockName == null || creativeTab == null) {
                throw new IllegalStateException("Block name and creative tab must be set");
            }

            AbstractBlock.Settings settings = AbstractBlock.Settings.create()
                .strength(hardness, resistance)
                .requiresTool()
                .mapColor(net.minecraft.block.MapColor.STONE_GRAY);

            return new WCArrowSlitBlock(settings, blockName, creativeTab);
        }
    }

    public enum ArrowSlitType implements StringIdentifiable {
        SINGLE("single"),
        TOP("top"),
        BOTTOM("bottom"),
        MID("mid");

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