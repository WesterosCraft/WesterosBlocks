package com.westerosblocks.block.custom;

import com.westerosblocks.utils.ModProperties;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WCCuboidBlock extends Block implements Waterloggable {
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    protected static ModProperties.StateProperty tempSTATE;
    public static ModProperties.StateProperty STATE;

    protected boolean toggleOnUse = false;

    // Default bounding box - can be overridden by constructor
    protected VoxelShape boundingBox = VoxelShapes.fullCube();

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(AbstractBlock.Settings settings, Object... params) {
            if (params.length > 0 && params[0] instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> paramMap = (Map<String, Object>) params[0];

                boolean doToggleOnUse = (Boolean) paramMap.getOrDefault("toggleOnUse", false);
                Integer numStates = (Integer) paramMap.get("states");
                boolean doAddStates = numStates != null && numStates > 0;

                // Handle bounding box if provided
                @SuppressWarnings("unchecked")
                Map<String, Object> boundingBoxMap = (Map<String, Object>) paramMap.get("boundingBox");
                VoxelShape customBoundingBox = null;

                if (boundingBoxMap != null) {
                    double xMin = ((Number) boundingBoxMap.getOrDefault("xMin", 0.0)).doubleValue();
                    double yMin = ((Number) boundingBoxMap.getOrDefault("yMin", 0.0)).doubleValue();
                    double zMin = ((Number) boundingBoxMap.getOrDefault("zMin", 0.0)).doubleValue();
                    double xMax = ((Number) boundingBoxMap.getOrDefault("xMax", 1.0)).doubleValue();
                    double yMax = ((Number) boundingBoxMap.getOrDefault("yMax", 1.0)).doubleValue();
                    double zMax = ((Number) boundingBoxMap.getOrDefault("zMax", 1.0)).doubleValue();

                    customBoundingBox = VoxelShapes.cuboid(xMin, yMin, zMin, xMax, yMax, zMax);
                }

                if (doAddStates) {
                    ArrayList<String> stateIds = new ArrayList<>();
                    @SuppressWarnings("unchecked")
                    List<String> stateValues = (List<String>) paramMap.get("stateValues");
                    if (stateValues != null && !stateValues.isEmpty()) {
                        stateIds.addAll(stateValues);
                    } else {
                        for (int i = 0; i < numStates; i++) {
                            stateIds.add("state" + i);
                        }
                    }
                    STATE = new ModProperties.StateProperty(stateIds);
                    tempSTATE = STATE;
                }

                return new WCCuboidBlock(settings, doToggleOnUse, doAddStates, customBoundingBox);
            }

            // Fallback for legacy parameter style
            boolean doToggleOnUse = params.length > 0 && params[0] instanceof Boolean ? (Boolean) params[0] : false;
            boolean doAddStates = params.length > 1 && params[1] instanceof Integer && (Integer) params[1] > 0;
            VoxelShape customBoundingBox = params.length > 2 && params[2] instanceof VoxelShape ? (VoxelShape) params[2] : null;

            if (doAddStates) {
                int numStates = (Integer) params[1];
                ArrayList<String> stateIds = new ArrayList<>();
                for (int i = 0; i < numStates; i++) {
                    stateIds.add("state" + i);
                }
                STATE = new ModProperties.StateProperty(stateIds);
                tempSTATE = STATE;
            }

            return new WCCuboidBlock(settings, doToggleOnUse, doAddStates, customBoundingBox);
        }
    }

    public WCCuboidBlock(AbstractBlock.Settings settings) {
        this(settings, false, false, null);
    }

    public WCCuboidBlock(AbstractBlock.Settings settings, boolean doToggleOnUse, boolean addStates, VoxelShape customBoundingBox) {
        super(settings);

        this.toggleOnUse = doToggleOnUse;

        if (customBoundingBox != null) {
            this.boundingBox = customBoundingBox;
        }

        BlockState defbs = this.getDefaultState().with(WATERLOGGED, false);
        if (addStates && STATE != null) {
            defbs = defbs.with(STATE, STATE.defValue);
        }
        this.setDefaultState(defbs);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED);
        if (tempSTATE != null) {
            builder.add(tempSTATE);
            tempSTATE = null;
        }
        super.appendProperties(builder);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
        BlockState bs = this.getDefaultState().with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
        if (STATE != null) {
            bs = bs.with(STATE, STATE.defValue);
        }
        return bs;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState,
            WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        Hand hand = player.getActiveHand();
        if (this.toggleOnUse && (STATE != null) && player.isCreative() && player.getStackInHand(hand).isEmpty()) {
            if (state.contains(STATE)) {
                state = state.cycle(STATE);
                world.setBlockState(pos, state, Block.NOTIFY_ALL);
                world.syncWorldEvent(player, 1006, pos, 0);
                return ActionResult.success(world.isClient);
            }
        }
        return ActionResult.PASS;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return boundingBox;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return boundingBox;
    }

    @Override
    public VoxelShape getCullingShape(BlockState state, BlockView world, BlockPos pos) {
        return boundingBox;
    }
}