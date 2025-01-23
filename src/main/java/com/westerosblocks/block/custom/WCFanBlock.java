package com.westerosblocks.block.custom;

import com.westerosblocks.block.ModBlocks;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.WesterosBlockFactory;
import com.westerosblocks.block.WesterosBlockLifecycle;
import net.minecraft.block.*;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public class WCFanBlock extends WCFanParentBlock implements WesterosBlockLifecycle {

    public static class Factory extends WesterosBlockFactory {
        @Override
        public Block buildBlockClass(WesterosBlockDef def) {
            AbstractBlock.Settings settings = def.makeBlockSettings().noCollision().breakInstantly();
            Block fanBlock = new WCFanBlock(settings, def);
            Block wallFanBlock = new WCWallFanBlock(settings, def);

            def.registerRenderType(ModBlocks.registerBlock(def.blockName + "_wall", wallFanBlock), false, false);
            ModBlocks.getCustomBlocksByName().put(def.blockName + "_wall", wallFanBlock);
            return def.registerRenderType(ModBlocks.registerBlock(def.blockName, fanBlock), false, false);
        }
    }

    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    private final WesterosBlockDef def;
    private final boolean allowUnsupported;
    private static final VoxelShape SHAPE = Block.createCuboidShape(2.0, 0.0, 2.0, 14.0, 4.0, 14.0);

    protected WCFanBlock(AbstractBlock.Settings settings, WesterosBlockDef def) {
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

        this.setDefaultState(this.getStateManager().getDefaultState()
                .with(WATERLOGGED, false));
    }

    @Override
    public WesterosBlockDef getWBDefinition() {
        return def;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
        return getDefaultState()
                .with(WATERLOGGED, fluidState.isIn(FluidTags.WATER));
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState,
                                                WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (direction == Direction.DOWN && !canPlaceAt(state, world, pos)) {
            return Blocks.AIR.getDefaultState();
        }

        if (state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        if (allowUnsupported) return true;
        BlockPos blockBelow = pos.down();
        return world.getBlockState(blockBelow).isSideSolidFullSquare(world, blockBelow, Direction.UP);
    }

    @Override
    public boolean isTransparent(BlockState state, BlockView world, BlockPos pos) {
        return !state.getFluidState().isEmpty();
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
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
