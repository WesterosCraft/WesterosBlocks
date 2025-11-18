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
    protected BlockDefinition def;
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    protected static ModProperties.StateProperty tempSTATE;
    public ModProperties.StateProperty STATE;
    protected boolean toggleOnUse = false;
    protected VoxelShape boundingBox = VoxelShapes.fullCube();
    protected VoxelShape[] stateSpecificShapes = null;

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(BlockDefinition definition) {
            AbstractBlock.Settings settings = definition.makeSettings();
            ModProperties.StateProperty stateProperty = definition.buildStateProperty();
            boolean doToggleOnUse = definition.toggleOnUse();

            // Calculate default bounding box (priority: cuboids > boundingBox > fullCube)
            VoxelShape customBoundingBox = null;

            // Check for cuboids first (highest priority)
            if (definition.getCuboids() != null && !definition.getCuboids().isEmpty()) {
                customBoundingBox = convertCuboidsToVoxelShape(definition.getCuboids());
            }
            // Fall back to boundingBox if no cuboids
            else if (definition.hasBoundingBox()) {
                BlockDefinition.BoundingBox bbox = definition.getBoundingBox();
                customBoundingBox = VoxelShapes.cuboid(
                    bbox.getXMin(), bbox.getYMin(), bbox.getZMin(),
                    bbox.getXMax(), bbox.getYMax(), bbox.getZMax()
                );
            }

            // Check for per-state bounding boxes/cuboids
            VoxelShape[] stateShapes = null;
            if (definition.hasStates()) {
                List<BlockDefinition.StateVariant> states = definition.getStates();
                boolean hasStateSpecificGeometry = false;

                // Check if any state has its own geometry
                for (BlockDefinition.StateVariant state : states) {
                    if ((state.getCuboids() != null && !state.getCuboids().isEmpty()) ||
                        state.getBoundingBox() != null) {
                        hasStateSpecificGeometry = true;
                        break;
                    }
                }

                if (hasStateSpecificGeometry) {
                    stateShapes = new VoxelShape[states.size()];
                    for (int i = 0; i < states.size(); i++) {
                        BlockDefinition.StateVariant state = states.get(i);

                        // Priority: state cuboids > state boundingBox > default shape
                        if (state.getCuboids() != null && !state.getCuboids().isEmpty()) {
                            stateShapes[i] = convertCuboidsToVoxelShape(state.getCuboids());
                        } else if (state.getBoundingBox() != null) {
                            BlockDefinition.BoundingBox bbox = state.getBoundingBox();
                            stateShapes[i] = VoxelShapes.cuboid(
                                bbox.getXMin(), bbox.getYMin(), bbox.getZMin(),
                                bbox.getXMax(), bbox.getYMax(), bbox.getZMax()
                            );
                        } else {
                            // Use default shape for this state
                            stateShapes[i] = customBoundingBox != null ? customBoundingBox : VoxelShapes.fullCube();
                        }
                    }
                }
            }

            // Set the STATE property if stateValues are provided
            tempSTATE = stateProperty;

            return new WCCuboidBlock(settings, definition, doToggleOnUse, customBoundingBox, stateShapes);
        }
    }

    public WCCuboidBlock(AbstractBlock.Settings settings, BlockDefinition def, boolean doToggleOnUse, VoxelShape customBoundingBox, VoxelShape[] stateShapes) {
        super(settings);
        this.def = def;
        this.toggleOnUse = doToggleOnUse;

        if (customBoundingBox != null) {
            this.boundingBox = customBoundingBox;
        }

        this.stateSpecificShapes = stateShapes;

        BlockState defbs = this.getDefaultState().with(WATERLOGGED, false);
        if (tempSTATE != null) {
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

    /**
     * Gets the appropriate VoxelShape for the given block state.
     * Uses state-specific shapes if available, otherwise falls back to default boundingBox.
     */
    protected VoxelShape getShapeForState(BlockState state) {
        if (stateSpecificShapes != null && STATE != null) {
            int stateIndex = STATE.getIndex(state.get(STATE));
            if (stateIndex >= 0 && stateIndex < stateSpecificShapes.length) {
                return stateSpecificShapes[stateIndex];
            }
        }
        return boundingBox;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return getShapeForState(state);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return getShapeForState(state);
    }

    @Override
    public VoxelShape getCullingShape(BlockState state, BlockView world, BlockPos pos) {
        return getShapeForState(state);
    }

    public BlockDefinition getDefinition() {
        return def;
    }

    /**
     * Helper method to convert a list of cuboid elements into a combined VoxelShape.
     * @param cuboids List of cuboid elements to convert
     * @return Combined VoxelShape representing all cuboids, or null if list is empty
     */
    protected static VoxelShape convertCuboidsToVoxelShape(List<BlockDefinition.CuboidElement> cuboids) {
        if (cuboids == null || cuboids.isEmpty()) {
            return null;
        }

        VoxelShape shape = VoxelShapes.empty();
        for (BlockDefinition.CuboidElement cuboid : cuboids) {
            VoxelShape cuboidShape = VoxelShapes.cuboid(
                cuboid.getXMin(), cuboid.getYMin(), cuboid.getZMin(),
                cuboid.getXMax(), cuboid.getYMax(), cuboid.getZMax()
            );
            shape = VoxelShapes.union(shape, cuboidShape);
        }
        return shape;
    }
}