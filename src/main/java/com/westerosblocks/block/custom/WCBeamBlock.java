package com.westerosblocks.block.custom;

import com.google.common.collect.ImmutableMap;
import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.utils.ModProperties;
import net.minecraft.block.*;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Wall-mounted horizontal beam. Like a mounted slab it has {@link #FACING} (away from the wall)
 * and {@link #HALF} (top/bottom), plus a data-driven {@link #STATE}: each definition state is a
 * different shape model (e.g. straight / corner / T) that the player cycles through by
 * right-clicking when {@code toggleOnUse} is set.
 */
public class WCBeamBlock extends Block implements WCBlockDef {
    protected BlockDefinition def;
    private final boolean allowUnsupported;
    private final boolean toggleOnUse;

    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final EnumProperty<BlockHalf> HALF = Properties.BLOCK_HALF;

    // STATE property (only for multi-state beams). Uses the temp* pattern because
    // appendProperties() runs from the super constructor before the subclass body.
    protected static ModProperties.StateProperty tempSTATE;
    public ModProperties.StateProperty STATE;

    // Key used for the shape map when the block has no STATE property.
    private static final String DEFAULT_KEY = "";

    // shapes keyed by stateID (or DEFAULT_KEY) -> FACING -> HALF -> shape
    private final Map<String, Map<Direction, Map<BlockHalf, VoxelShape>>> shapesByState;

    public WCBeamBlock(Settings settings, BlockDefinition def,
                       Map<String, Map<Direction, Map<BlockHalf, VoxelShape>>> shapes,
                       boolean allowUnsupported, boolean toggleOnUse) {
        super(settings);
        this.def = def;
        this.allowUnsupported = allowUnsupported;
        this.toggleOnUse = toggleOnUse;
        this.shapesByState = (shapes != null && !shapes.isEmpty()) ? shapes : wrapDefault();

        BlockState defbs = this.getStateManager().getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(HALF, BlockHalf.BOTTOM);
        if (STATE != null) {
            defbs = defbs.with(STATE, STATE.defValue);
        }
        this.setDefaultState(defbs);
    }

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(BlockDefinition definition) {
            AbstractBlock.Settings settings = definition.makeSettings();

            ModProperties.StateProperty stateProperty = definition.buildStateProperty();
            if (stateProperty != null) {
                tempSTATE = stateProperty;
            }

            Map<String, Map<Direction, Map<BlockHalf, VoxelShape>>> shapes = createRotatedShapes(definition);
            boolean allowUnsupported = definition.isAllowUnsupported();
            boolean toggleOnUse = definition.toggleOnUse();
            return new WCBeamBlock(settings, definition, shapes, allowUnsupported, toggleOnUse);
        }

        /**
         * Builds VoxelShapes for every shape key. Multi-state beams get one shape set per stateID
         * (from that state's bounding box, falling back to the block-level box); single/no-state
         * beams get one set under {@link #DEFAULT_KEY}.
         */
        private static Map<String, Map<Direction, Map<BlockHalf, VoxelShape>>> createRotatedShapes(BlockDefinition definition) {
            Map<String, Map<Direction, Map<BlockHalf, VoxelShape>>> result = new LinkedHashMap<>();

            if (definition.getStateCount() > 1) {
                for (BlockDefinition.StateVariant state : definition.getStates()) {
                    BlockDefinition.BoundingBox bbox = state.getBoundingBox();
                    if (bbox == null && definition.hasBoundingBox()) {
                        bbox = definition.getBoundingBox();
                    }
                    result.put(state.getStateID(),
                            bbox != null ? computeShapesForBox(bbox) : createDefaultShapes());
                }
                return result;
            }

            BlockDefinition.BoundingBox bbox = null;
            if (definition.hasStates() && !definition.getStates().isEmpty()
                    && definition.getStates().get(0).getBoundingBox() != null) {
                bbox = definition.getStates().get(0).getBoundingBox();
            } else if (definition.hasBoundingBox()) {
                bbox = definition.getBoundingBox();
            }
            result.put(DEFAULT_KEY, bbox != null ? computeShapesForBox(bbox) : createDefaultShapes());
            return result;
        }

        /**
         * Creates rotated VoxelShapes for all 8 combinations (4 facings × 2 halves).
         * JSON bbox is defined for EAST facing, BOTTOM half.
         */
        private static Map<Direction, Map<BlockHalf, VoxelShape>> computeShapesForBox(BlockDefinition.BoundingBox bbox) {
            double xMin = bbox.getXMin();
            double xMax = bbox.getXMax();
            double yMin = bbox.getYMin();
            double yMax = bbox.getYMax();
            double zMin = bbox.getZMin();
            double zMax = bbox.getZMax();

            // Top half: shift Y so block sits at top of block space
            double height = yMax - yMin;
            double topYMin = 1.0 - height;
            double topYMax = 1.0;

            // EAST: bbox as-is (thin in X, at west edge)
            VoxelShape eastBottom = Block.createCuboidShape(
                    xMin * 16, yMin * 16, zMin * 16,
                    xMax * 16, yMax * 16, zMax * 16);
            VoxelShape eastTop = Block.createCuboidShape(
                    xMin * 16, topYMin * 16, zMin * 16,
                    xMax * 16, topYMax * 16, zMax * 16);

            // WEST: mirror X
            VoxelShape westBottom = Block.createCuboidShape(
                    (1 - xMax) * 16, yMin * 16, zMin * 16,
                    (1 - xMin) * 16, yMax * 16, zMax * 16);
            VoxelShape westTop = Block.createCuboidShape(
                    (1 - xMax) * 16, topYMin * 16, zMin * 16,
                    (1 - xMin) * 16, topYMax * 16, zMax * 16);

            // SOUTH: rotate 90° (swap X and Z)
            VoxelShape southBottom = Block.createCuboidShape(
                    zMin * 16, yMin * 16, xMin * 16,
                    zMax * 16, yMax * 16, xMax * 16);
            VoxelShape southTop = Block.createCuboidShape(
                    zMin * 16, topYMin * 16, xMin * 16,
                    zMax * 16, topYMax * 16, xMax * 16);

            // NORTH: rotate 90° + mirror Z
            VoxelShape northBottom = Block.createCuboidShape(
                    zMin * 16, yMin * 16, (1 - xMax) * 16,
                    zMax * 16, yMax * 16, (1 - xMin) * 16);
            VoxelShape northTop = Block.createCuboidShape(
                    zMin * 16, topYMin * 16, (1 - xMax) * 16,
                    zMax * 16, topYMax * 16, (1 - xMin) * 16);

            return ImmutableMap.of(
                    Direction.EAST, ImmutableMap.of(BlockHalf.BOTTOM, eastBottom, BlockHalf.TOP, eastTop),
                    Direction.WEST, ImmutableMap.of(BlockHalf.BOTTOM, westBottom, BlockHalf.TOP, westTop),
                    Direction.SOUTH, ImmutableMap.of(BlockHalf.BOTTOM, southBottom, BlockHalf.TOP, southTop),
                    Direction.NORTH, ImmutableMap.of(BlockHalf.BOTTOM, northBottom, BlockHalf.TOP, northTop)
            );
        }
    }

    private static Map<String, Map<Direction, Map<BlockHalf, VoxelShape>>> wrapDefault() {
        return ImmutableMap.of(DEFAULT_KEY, createDefaultShapes());
    }

    private static Map<Direction, Map<BlockHalf, VoxelShape>> createDefaultShapes() {
        VoxelShape defaultBottom = Block.createCuboidShape(0, 0, 14, 16, 8, 16);
        VoxelShape defaultTop = Block.createCuboidShape(0, 8, 14, 16, 16, 16);
        ImmutableMap<BlockHalf, VoxelShape> northShapes = ImmutableMap.of(BlockHalf.BOTTOM, defaultBottom, BlockHalf.TOP, defaultTop);

        defaultBottom = Block.createCuboidShape(0, 0, 0, 16, 8, 2);
        defaultTop = Block.createCuboidShape(0, 8, 0, 16, 16, 2);
        ImmutableMap<BlockHalf, VoxelShape> southShapes = ImmutableMap.of(BlockHalf.BOTTOM, defaultBottom, BlockHalf.TOP, defaultTop);

        defaultBottom = Block.createCuboidShape(0, 0, 0, 2, 8, 16);
        defaultTop = Block.createCuboidShape(0, 8, 0, 2, 16, 16);
        ImmutableMap<BlockHalf, VoxelShape> eastShapes = ImmutableMap.of(BlockHalf.BOTTOM, defaultBottom, BlockHalf.TOP, defaultTop);

        defaultBottom = Block.createCuboidShape(14, 0, 0, 16, 8, 16);
        defaultTop = Block.createCuboidShape(14, 8, 0, 16, 16, 16);
        ImmutableMap<BlockHalf, VoxelShape> westShapes = ImmutableMap.of(BlockHalf.BOTTOM, defaultBottom, BlockHalf.TOP, defaultTop);

        return ImmutableMap.of(
                Direction.NORTH, northShapes,
                Direction.SOUTH, southShapes,
                Direction.EAST, eastShapes,
                Direction.WEST, westShapes
        );
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, HALF);
        if (tempSTATE != null) {
            STATE = tempSTATE;
            builder.add(STATE);
            tempSTATE = null;
        }
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (this.toggleOnUse && (this.STATE != null) && player.isCreative() && player.getMainHandStack().isEmpty()) {
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
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState state = this.getDefaultState();
        WorldView world = ctx.getWorld();
        BlockPos pos = ctx.getBlockPos();

        // Find a valid wall-facing direction
        for (Direction direction : ctx.getPlacementDirections()) {
            if (direction.getAxis().isHorizontal()) {
                Direction opposite = direction.getOpposite();
                state = state.with(FACING, opposite);
                if (state.canPlaceAt(world, pos)) {
                    // Determine top/bottom half from hit position
                    Direction clickedFace = ctx.getSide();
                    BlockHalf half = (clickedFace == Direction.DOWN ||
                            (clickedFace != Direction.UP && ctx.getHitPos().y - pos.getY() > 0.5))
                            ? BlockHalf.TOP : BlockHalf.BOTTOM;
                    return state.with(HALF, half);
                }
            }
        }

        return null;
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        if (this.allowUnsupported) {
            return true;
        }

        Direction facing = state.get(FACING);
        Direction attachmentDir = facing.getOpposite();
        BlockPos attachPos = pos.offset(attachmentDir);
        BlockState attachState = world.getBlockState(attachPos);

        return attachState.isSideSolidFullSquare(world, attachPos, facing);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState,
                                                 WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (direction.getOpposite() == state.get(FACING) && !this.canPlaceAt(state, world, pos)) {
            return Blocks.AIR.getDefaultState();
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    private VoxelShape getShape(BlockState state) {
        String key = (STATE != null && state.contains(STATE)) ? state.get(STATE) : DEFAULT_KEY;
        Map<Direction, Map<BlockHalf, VoxelShape>> byFacing = shapesByState.get(key);
        if (byFacing == null) {
            byFacing = shapesByState.values().iterator().next();
        }
        return byFacing.get(state.get(FACING)).get(state.get(HALF));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return getShape(state);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return getShape(state);
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    public BlockDefinition getDefinition() {
        return def;
    }
}
