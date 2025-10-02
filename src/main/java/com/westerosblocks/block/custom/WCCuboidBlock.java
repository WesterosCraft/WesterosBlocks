package com.westerosblocks.block.custom;

import com.westerosblocks.data.BlockDefinition;
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

public class WCCuboidBlock extends Block implements Waterloggable {
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    protected static ModProperties.StateProperty tempSTATE;
    public ModProperties.StateProperty STATE;

    protected boolean toggleOnUse = false;

    // Default bounding box - can be overridden by constructor
    protected VoxelShape boundingBox = VoxelShapes.fullCube();

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(AbstractBlock.Settings settings, BlockDefinition definition) {
            // Handle null definition (from BlockBuilder) with sensible defaults
            boolean doToggleOnUse = definition != null && definition.toggleOnUse();
            int numStates = definition != null ? definition.getStateCount() : 0;
            boolean doAddStates = numStates > 0;

            // Handle bounding box if provided
            VoxelShape customBoundingBox = null;
            if (definition != null && definition.hasBoundingBox()) {
                BlockDefinition.BoundingBox bbox = definition.getBoundingBox();
                customBoundingBox = VoxelShapes.cuboid(
                    bbox.getXMin(), bbox.getYMin(), bbox.getZMin(),
                    bbox.getXMax(), bbox.getYMax(), bbox.getZMax()
                );
            }

            // Set the STATE property if stateValues are provided
            if (doAddStates) {
                List<String> stateValues = definition != null ? definition.getStateValues() : null;
                if (stateValues != null && !stateValues.isEmpty()) {
                    tempSTATE = new ModProperties.StateProperty(stateValues);
                } else {
                    // Generate default state IDs if not provided
                    ArrayList<String> stateIds = new ArrayList<>();
                    for (int i = 0; i < numStates; i++) {
                        stateIds.add("state" + i);
                    }
                    tempSTATE = new ModProperties.StateProperty(stateIds);
                }
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
        if (addStates && tempSTATE != null) {
            defbs = defbs.with(tempSTATE, tempSTATE.defValue);
        }
        this.setDefaultState(defbs);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED);
        if (tempSTATE != null) {
            STATE = tempSTATE;
            tempSTATE = null;
            builder.add(STATE);
        }
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
        if (this.toggleOnUse && (this.STATE != null) && player.isCreative() && player.getStackInHand(hand).isEmpty()) {
            if (state.contains(this.STATE)) {
                state = state.cycle(this.STATE);
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