package com.westerosblocks.block.custom;

import com.google.common.collect.ImmutableMap;
import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.*;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import java.util.Map;

public class WCMountedSlabBlock extends Block implements WCBlockDef {
    protected BlockDefinition def;
    private final boolean allowUnsupported;

    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final EnumProperty<BlockHalf> HALF = Properties.BLOCK_HALF;

    private final Map<Direction, Map<BlockHalf, VoxelShape>> shapesByState;

    public WCMountedSlabBlock(Settings settings, BlockDefinition def, Map<Direction, Map<BlockHalf, VoxelShape>> shapes, boolean allowUnsupported, boolean defaultTop) {
        super(settings);
        this.def = def;
        this.allowUnsupported = allowUnsupported;
        this.shapesByState = shapes != null ? shapes : createDefaultShapes();
        this.setDefaultState(this.getStateManager().getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(HALF, defaultTop ? BlockHalf.TOP : BlockHalf.BOTTOM));
    }

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(BlockDefinition definition) {
            AbstractBlock.Settings settings = definition.makeSettings();
            Map<Direction, Map<BlockHalf, VoxelShape>> shapes = createRotatedShapes(definition);
            boolean allowUnsupported = definition.isAllowUnsupported();
            boolean defaultTop = definition.isMountedSlabDefaultTop();
            return new WCMountedSlabBlock(settings, definition, shapes, allowUnsupported, defaultTop);
        }

        /**
         * Creates rotated VoxelShapes for all 8 combinations (4 facings × 2 halves).
         * JSON bbox is defined for EAST facing, BOTTOM half.
         */
        private static Map<Direction, Map<BlockHalf, VoxelShape>> createRotatedShapes(BlockDefinition definition) {
            if (!definition.hasBoundingBox()) {
                return createDefaultShapes();
            }

            BlockDefinition.BoundingBox bbox = definition.getBoundingBox();
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

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return shapesByState.get(state.get(FACING)).get(state.get(HALF));
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return shapesByState.get(state.get(FACING)).get(state.get(HALF));
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
