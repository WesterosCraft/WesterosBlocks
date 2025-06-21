package com.westerosblocks.block.custom;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.util.StringIdentifiable;

public class WCChairBlock extends Block {
    public static final IntProperty ROTATION = IntProperty.of("rotation", 0, 7);

    // Define the base shapes for the chair components
    private static final VoxelShape SEAT = Block.createCuboidShape(2, 9, 1, 14, 11, 14);
    private static final VoxelShape BACKREST = Block.createCuboidShape(2, 11, 12, 14, 21, 14);
    private static final VoxelShape LEG_FRONT_LEFT = Block.createCuboidShape(2, 0, 2, 4, 9, 4);
    private static final VoxelShape LEG_FRONT_RIGHT = Block.createCuboidShape(2, 0, 11, 4, 9, 13);
    private static final VoxelShape LEG_BACK_LEFT = Block.createCuboidShape(12, 0, 2, 14, 9, 4);
    private static final VoxelShape LEG_BACK_RIGHT = Block.createCuboidShape(12, 0, 11, 14, 9, 13);

    // Base chair shape (facing north)
    private static final VoxelShape NORTH_SHAPE = VoxelShapes.union(
        SEAT, BACKREST, LEG_FRONT_LEFT, LEG_FRONT_RIGHT, LEG_BACK_LEFT, LEG_BACK_RIGHT
    );

    // Rotated shapes for each direction - manually defined for accuracy
    private static final VoxelShape EAST_SHAPE = VoxelShapes.union(
        Block.createCuboidShape(1, 9, 2, 14, 11, 14), // rotated seat
        Block.createCuboidShape(1, 11, 2, 3, 21, 14), // rotated backrest
        Block.createCuboidShape(2, 0, 2, 4, 9, 4), // rotated leg front left
        Block.createCuboidShape(11, 0, 2, 13, 9, 4), // rotated leg front right
        Block.createCuboidShape(2, 0, 12, 4, 9, 14), // rotated leg back left
        Block.createCuboidShape(11, 0, 12, 13, 9, 14) // rotated leg back right
    );

    private static final VoxelShape SOUTH_SHAPE = VoxelShapes.union(
        Block.createCuboidShape(2, 9, 1, 14, 11, 14), // rotated seat
        Block.createCuboidShape(2, 11, 1, 14, 21, 3), // rotated backrest
        Block.createCuboidShape(2, 0, 11, 4, 9, 13), // rotated leg front left
        Block.createCuboidShape(2, 0, 2, 4, 9, 4), // rotated leg front right
        Block.createCuboidShape(12, 0, 11, 14, 9, 13), // rotated leg back left
        Block.createCuboidShape(12, 0, 2, 14, 9, 4) // rotated leg back right
    );

    private static final VoxelShape WEST_SHAPE = VoxelShapes.union(
        Block.createCuboidShape(1, 9, 2, 14, 11, 14), // rotated seat
        Block.createCuboidShape(12, 11, 2, 14, 21, 14), // rotated backrest
        Block.createCuboidShape(11, 0, 11, 13, 9, 13), // rotated leg front left
        Block.createCuboidShape(2, 0, 11, 4, 9, 13), // rotated leg front right
        Block.createCuboidShape(11, 0, 2, 13, 9, 4), // rotated leg back left
        Block.createCuboidShape(2, 0, 2, 4, 9, 4) // rotated leg back right
    );
    
    // Diagonal shapes - simplified for now, using base shape
    // These would need more complex calculations for true diagonal collision
    private static final VoxelShape NORTHEAST_SHAPE = NORTH_SHAPE;
    private static final VoxelShape SOUTHEAST_SHAPE = EAST_SHAPE;
    private static final VoxelShape SOUTHWEST_SHAPE = SOUTH_SHAPE;
    private static final VoxelShape NORTHWEST_SHAPE = WEST_SHAPE;

    private final String blockName;
    private final String creativeTab;

    protected WCChairBlock(AbstractBlock.Settings settings, String blockName, String creativeTab) {
        super(settings);
        this.blockName = blockName;
        this.creativeTab = creativeTab;
        setDefaultState(getDefaultState().with(ROTATION, 0));
    }

    public String getBlockName() {
        return blockName;
    }

    public String getCreativeTab() {
        return creativeTab;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(ROTATION);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        // Use the same approach as cuboid blocks for 8-directional rotation
        // But adjust the rotation so the chair faces the player
        int rotation = MathHelper.floor((double) (ctx.getPlayerYaw() * 8.0F / 360.0F) + 0.5D) & 7;
        
        // Adjust rotation so chair faces the player (backrest towards player)
        rotation = (rotation + 4) % 8;
        
        return this.getDefaultState().with(ROTATION, rotation);
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
        int rotation = state.get(ROTATION);
        return switch (rotation) {
            case 0 -> NORTH_SHAPE;
            case 1 -> NORTHEAST_SHAPE;
            case 2 -> EAST_SHAPE;
            case 3 -> SOUTHEAST_SHAPE;
            case 4 -> SOUTH_SHAPE;
            case 5 -> SOUTHWEST_SHAPE;
            case 6 -> WEST_SHAPE;
            case 7 -> NORTHWEST_SHAPE;
            default -> NORTH_SHAPE;
        };
    }

    public static class Builder {
        private String blockName;
        private String creativeTab;
        private float hardness = 2.0f;
        private float resistance = 6.0f;
        private String material = "wood";
        private String stepSound = "wood";
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

        public WCChairBlock build() {
            if (blockName == null || creativeTab == null) {
                throw new IllegalStateException("Block name and creative tab must be set");
            }

            AbstractBlock.Settings settings = AbstractBlock.Settings.create()
                .strength(hardness, resistance)
                .requiresTool()
                .mapColor(net.minecraft.block.MapColor.OAK_TAN)
                .nonOpaque();

            return new WCChairBlock(settings, blockName, creativeTab);
        }
    }
} 