package com.westerosblocks.block.custom;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;

public class WCTableBlock extends Block {
    public static final BooleanProperty NORTH = Properties.NORTH;
    public static final BooleanProperty EAST = Properties.EAST;
    public static final BooleanProperty SOUTH = Properties.SOUTH;
    public static final BooleanProperty WEST = Properties.WEST;
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    private final String blockName;
    private final String creativeTab;

    protected WCTableBlock(AbstractBlock.Settings settings, String blockName, String creativeTab) {
        super(settings);
        this.blockName = blockName;
        this.creativeTab = creativeTab;
        this.setDefaultState(this.getDefaultState()
                .with(NORTH, false)
                .with(EAST, false)
                .with(SOUTH, false)
                .with(WEST, false)
                .with(WATERLOGGED, false));
    }

    public String getBlockName() {
        return blockName;
    }

    public String getCreativeTab() {
        return creativeTab;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, WATERLOGGED);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        FluidState fluidstate = ctx.getWorld().getFluidState(ctx.getBlockPos());
        return this.getDefaultState()
                .with(WATERLOGGED, fluidstate.isIn(FluidTags.WATER));
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState,
                                              WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        if (direction.getAxis().isHorizontal()) {
            boolean isConnected = neighborState.isOf(this);
            return state.with(getPropertyForDirection(direction), isConnected);
        }

        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    private static BooleanProperty getPropertyForDirection(Direction direction) {
        return switch (direction) {
            case NORTH -> NORTH;
            case EAST -> EAST;
            case SOUTH -> SOUTH;
            case WEST -> WEST;
            default -> throw new IllegalArgumentException("Invalid direction: " + direction);
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

        public WCTableBlock build() {
            if (blockName == null || creativeTab == null) {
                throw new IllegalStateException("Block name and creative tab must be set");
            }

            AbstractBlock.Settings settings = AbstractBlock.Settings.create()
                .strength(hardness, resistance)
                .requiresTool()
                .mapColor(net.minecraft.block.MapColor.OAK_TAN)
                .nonOpaque();

            return new WCTableBlock(settings, blockName, creativeTab);
        }
    }
} 