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
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;
import net.minecraft.util.math.random.Random;

import java.util.ArrayList;
import java.util.List;

public class WCCuboidBlock extends Block implements Waterloggable, WCBlockDef {
    protected BlockDefinition def;
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    protected static ModProperties.StateProperty tempSTATE;
    public ModProperties.StateProperty STATE;
    protected boolean toggleOnUse = false;
    protected int modelsPerState;
    protected VoxelShape[] SHAPE_BY_INDEX;
    protected VoxelShape[] SUPPORT_BY_INDEX;
    protected List<BlockDefinition.CuboidElement>[] cuboid_by_facing;

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(BlockDefinition definition) {
            AbstractBlock.Settings settings = definition.makeSettings();
            ModProperties.StateProperty stateProperty = definition.buildStateProperty();
            boolean doToggleOnUse = definition.toggleOnUse();

            // Set the STATE property for constructor
            tempSTATE = stateProperty;

            return new WCCuboidBlock(settings, definition, 1, doToggleOnUse);
        }
    }

    public WCCuboidBlock(AbstractBlock.Settings settings, BlockDefinition def, int modelsPerState, boolean doToggleOnUse) {
        super(settings);
        this.def = def;
        this.modelsPerState = modelsPerState;
        this.toggleOnUse = doToggleOnUse;

        int cnt = def.getStateCount();

        // Initialize arrays
        this.cuboid_by_facing = new List[cnt * modelsPerState];
        this.SHAPE_BY_INDEX = new VoxelShape[cnt * modelsPerState];
        this.SUPPORT_BY_INDEX = new VoxelShape[cnt];

        // Compute shapes for each state
        List<BlockDefinition.StateVariant> states = def.getStates();
        for (int i = 0; i < cnt; i++) {
            BlockDefinition.StateVariant state = states.get(i);

            // Get cuboids for this state (priority: state cuboids > definition cuboids > boundingBox > fullCube)
            List<BlockDefinition.CuboidElement> cuboids = state.getCuboids();

            // Fall back to definition-level cuboids if state doesn't have its own
            if (cuboids == null || cuboids.isEmpty()) {
                cuboids = def.getCuboids();
            }

            // If still no cuboids, convert boundingBox to cuboid
            if (cuboids == null || cuboids.isEmpty()) {
                BlockDefinition.BoundingBox bbox = state.getBoundingBox();
                if (bbox == null) {
                    bbox = def.getBoundingBox();
                }
                if (bbox != null) {
                    // Store as a list for consistency, but we'll compute shape directly
                    cuboids = new ArrayList<>();
                }
            }

            // Store cuboids for base rotation (index i * modelsPerState)
            cuboid_by_facing[i * modelsPerState] = cuboids != null ? new ArrayList<>(cuboids) : new ArrayList<>();

            // Initialize empty lists for rotations (filled by subclasses)
            for (int j = 1; j < modelsPerState; j++) {
                cuboid_by_facing[i * modelsPerState + j] = new ArrayList<>();
            }

            // Compute base shape (index i * modelsPerState)
            if (cuboids != null && !cuboids.isEmpty()) {
                SHAPE_BY_INDEX[i * modelsPerState] = computeShapeFromCuboids(cuboids);
            } else {
                // Use boundingBox if no cuboids
                BlockDefinition.BoundingBox bbox = state.getBoundingBox();
                if (bbox == null) {
                    bbox = def.getBoundingBox();
                }
                if (bbox != null) {
                    SHAPE_BY_INDEX[i * modelsPerState] = VoxelShapes.cuboid(
                        bbox.getXMin(), bbox.getYMin(), bbox.getZMin(),
                        bbox.getXMax(), bbox.getYMax(), bbox.getZMax()
                    );
                } else {
                    SHAPE_BY_INDEX[i * modelsPerState] = VoxelShapes.fullCube();
                }
            }

            // Compute support shape (same as base shape for simple blocks)
            SUPPORT_BY_INDEX[i] = SHAPE_BY_INDEX[i * modelsPerState];
        }

        // Set default state
        BlockState defbs = this.getDefaultState().with(WATERLOGGED, false);
        if (STATE != null) {
            defbs = defbs.with(STATE, STATE.defValue);
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
    protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView,
            BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
        if (state.get(WATERLOGGED)) {
            tickView.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        return super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (this.toggleOnUse && (this.STATE != null) && player.isCreative() && player.getMainHandStack().isEmpty()) {
            if (state.contains(this.STATE)) {
                state = state.cycle(this.STATE);
                world.setBlockState(pos, state, Block.NOTIFY_ALL);
                world.syncWorldEvent(player, 1006, pos, 0);
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.PASS;
    }

    /**
     * Gets the array index for the given block state.
     * Base implementation returns state index × modelsPerState.
     * Subclasses override to add rotation offset.
     */
    protected int getIndexFromState(BlockState state) {
        if (STATE != null)
            return modelsPerState * STATE.getIndex(state.get(STATE));
        else
            return 0;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE_BY_INDEX[getIndexFromState(state)];
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE_BY_INDEX[getIndexFromState(state)];
    }

    @Override
    public VoxelShape getCullingShape(BlockState state) {
        return SHAPE_BY_INDEX[getIndexFromState(state)];
    }

    public BlockDefinition getDefinition() {
        return def;
    }

    /**
     * Helper method to convert a list of cuboid elements into a combined VoxelShape.
     * @param cuboids List of cuboid elements to convert
     * @return Combined VoxelShape representing all cuboids, or full cube if list is empty
     */
    protected VoxelShape computeShapeFromCuboids(List<BlockDefinition.CuboidElement> cuboids) {
        if (cuboids == null || cuboids.isEmpty()) {
            return VoxelShapes.fullCube();
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

    /**
     * Gets the cuboid list for the specified state index.
     * Used by exporters to generate models.
     * @param stateIdx State index (0-based)
     * @return List of cuboid elements for this state
     */
    public List<BlockDefinition.CuboidElement> getModelCuboids(int stateIdx) {
        return cuboid_by_facing[modelsPerState * stateIdx];
    }
}