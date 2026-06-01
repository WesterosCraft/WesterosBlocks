package com.westerosblocks.block.custom;

import com.google.common.collect.ImmutableMap;
import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.utils.ModProperties;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Wall-mounted block with a left/right pair of authored models, chosen from the
 * horizontal hit position at placement. Behaves like {@link WCMountedSlabBlock} but
 * swaps the top/bottom ({@code HALF}) axis for a left/right axis carried by the
 * {@code STATE} property (values {@code left}/{@code right}), so {@code toggleOnUse}
 * flips the two sides. Each side is its own authored model; the blockstate only applies
 * the FACING Y-rotation (Minecraft blockstates cannot truly mirror).
 */
public class WCMountedMirrorBlock extends Block implements WCBlockDef {
    protected BlockDefinition def;
    private final boolean allowUnsupported;
    private final boolean toggleOnUse;

    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

    // STATE property (left/right). Uses the temp* pattern because appendProperties()
    // runs from the super constructor before the subclass body.
    protected static ModProperties.StateProperty tempSTATE;
    public ModProperties.StateProperty STATE;

    // Key used for the shape map when the block has no STATE property.
    private static final String DEFAULT_KEY = "";

    // shapes keyed by stateID (or DEFAULT_KEY) -> FACING -> shape
    private final Map<String, Map<Direction, VoxelShape>> shapesByState;

    public WCMountedMirrorBlock(Settings settings, BlockDefinition def,
                                Map<String, Map<Direction, VoxelShape>> shapes,
                                boolean allowUnsupported, boolean toggleOnUse) {
        super(settings);
        this.def = def;
        this.allowUnsupported = allowUnsupported;
        this.toggleOnUse = toggleOnUse;
        this.shapesByState = (shapes != null && !shapes.isEmpty()) ? shapes : wrapDefault();

        BlockState defbs = this.getStateManager().getDefaultState()
                .with(FACING, Direction.NORTH);
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

            Map<String, Map<Direction, VoxelShape>> shapes = createRotatedShapes(definition);
            boolean allowUnsupported = definition.isAllowUnsupported();
            boolean toggleOnUse = definition.toggleOnUse();
            return new WCMountedMirrorBlock(settings, definition, shapes, allowUnsupported, toggleOnUse);
        }

        /**
         * Builds VoxelShapes for every shape key. Multi-state blocks (more than one
         * state) get one shape set per stateID, each from that state's bounding box
         * (falling back to the block-level box). Single/no-state blocks get one set
         * under {@link #DEFAULT_KEY}.
         */
        private static Map<String, Map<Direction, VoxelShape>> createRotatedShapes(BlockDefinition definition) {
            Map<String, Map<Direction, VoxelShape>> result = new LinkedHashMap<>();

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

            // No (or single) state: a single shape set under DEFAULT_KEY.
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
         * Creates rotated VoxelShapes for all 4 facings. The JSON bbox is authored for
         * the NORTH facing (Y=0); each other facing rotates that box about Y to match
         * the model's blockstate rotation (N=0, E=90, S=180, W=270).
         */
        private static Map<Direction, VoxelShape> computeShapesForBox(BlockDefinition.BoundingBox bbox) {
            double x0 = bbox.getXMin() * 16, x1 = bbox.getXMax() * 16;
            double y0 = bbox.getYMin() * 16, y1 = bbox.getYMax() * 16;
            double z0 = bbox.getZMin() * 16, z1 = bbox.getZMax() * 16;

            VoxelShape north = Block.createCuboidShape(x0, y0, z0, x1, y1, z1);
            // EAST (y=90):  newX = [16-z1, 16-z0], newZ = [x0, x1]
            VoxelShape east = Block.createCuboidShape(16 - z1, y0, x0, 16 - z0, y1, x1);
            // SOUTH (y=180): newX = [16-x1, 16-x0], newZ = [16-z1, 16-z0]
            VoxelShape south = Block.createCuboidShape(16 - x1, y0, 16 - z1, 16 - x0, y1, 16 - z0);
            // WEST (y=270):  newX = [z0, z1], newZ = [16-x1, 16-x0]
            VoxelShape west = Block.createCuboidShape(z0, y0, 16 - x1, z1, y1, 16 - x0);

            return ImmutableMap.of(
                    Direction.NORTH, north,
                    Direction.EAST, east,
                    Direction.SOUTH, south,
                    Direction.WEST, west
            );
        }

        private static Map<Direction, VoxelShape> createDefaultShapes() {
            VoxelShape full = VoxelShapes.fullCube();
            return ImmutableMap.of(
                    Direction.NORTH, full,
                    Direction.EAST, full,
                    Direction.SOUTH, full,
                    Direction.WEST, full
            );
        }
    }

    private static Map<String, Map<Direction, VoxelShape>> wrapDefault() {
        return ImmutableMap.of(DEFAULT_KEY, Factory.createDefaultShapes());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
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

        // Find a valid wall-facing direction (block faces away from the wall).
        for (Direction direction : ctx.getPlacementDirections()) {
            if (direction.getAxis().isHorizontal()) {
                Direction opposite = direction.getOpposite();
                state = state.with(FACING, opposite);
                if (state.canPlaceAt(world, pos)) {
                    return state.with(STATE, pickSide(ctx, opposite));
                }
            }
        }

        return null;
    }

    /**
     * Picks {@code left}/{@code right} from the horizontal hit position projected onto
     * the lateral axis ({@code facing.rotateYClockwise()}). If the model sides feel
     * reversed in-game, swap the authored left/right model files.
     */
    private String pickSide(ItemPlacementContext ctx, Direction facing) {
        String left = stateValueOrDefault("left");
        String right = stateValueOrDefault("right");
        if (STATE == null) {
            return left;
        }

        BlockPos pos = ctx.getBlockPos();
        Vec3d local = ctx.getHitPos().subtract(pos.getX(), pos.getY(), pos.getZ());
        // Signed distance from block center along the lateral axis (block's right when
        // facing). Positive => hit on the lateral/right side, negative => left.
        Direction lateral = facing.rotateYClockwise();
        double along = lateral.getOffsetX() * (local.x - 0.5) + lateral.getOffsetZ() * (local.z - 0.5);
        return along < 0 ? left : right;
    }

    private String stateValueOrDefault(String id) {
        if (STATE != null && STATE.valMap.containsKey(id)) {
            return id;
        }
        return STATE != null ? STATE.defValue : id;
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
        Map<Direction, VoxelShape> byFacing = shapesByState.get(key);
        if (byFacing == null) {
            byFacing = shapesByState.values().iterator().next();
        }
        return byFacing.get(state.get(FACING));
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
        state = state.rotate(mirror.getRotation(state.get(FACING)));
        if (mirror != BlockMirror.NONE && STATE != null && state.contains(STATE)) {
            state = state.cycle(STATE);
        }
        return state;
    }

    public BlockDefinition getDefinition() {
        return def;
    }
}
