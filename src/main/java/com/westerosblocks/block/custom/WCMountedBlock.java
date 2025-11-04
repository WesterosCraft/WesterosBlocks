package com.westerosblocks.block.custom;

import com.google.common.collect.ImmutableMap;
import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
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

public class WCMountedBlock extends Block {
    protected BlockDefinition def;
    private final boolean allowUnsupported;

    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

    // Map of VoxelShapes for each facing direction
    private final Map<Direction, VoxelShape> shapesByFacing;

    public WCMountedBlock(Settings settings, BlockDefinition def, Map<Direction, VoxelShape> shapes, boolean allowUnsupported) {
        super(settings);
        this.def = def;
        this.allowUnsupported = allowUnsupported;
        this.shapesByFacing = shapes != null ? shapes : createDefaultShapes();
        this.setDefaultState(this.getStateManager().getDefaultState()
                .with(FACING, Direction.NORTH));
    }

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(BlockDefinition definition) {
            AbstractBlock.Settings settings = definition.makeSettings();
            Map<Direction, VoxelShape> shapes = createRotatedShapes(definition);
            boolean allowUnsupported = definition.isAllowUnsupported();
            return new WCMountedBlock(settings, definition, shapes, allowUnsupported);
        }

        @Override
        public Block buildBlockClass(AbstractBlock.Settings settings, Map<String, Object> parameters) {
            return new WCMountedBlock(settings, null, null, false);
        }

        /**
         * Creates rotated VoxelShapes for all 4 horizontal facings from the BlockDefinition's bounding box
         * Assumes JSON bbox is defined for WEST facing (thin in X, positioned at west edge)
         */
        private static Map<Direction, VoxelShape> createRotatedShapes(BlockDefinition definition) {
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

            // Create rotated shapes for each facing direction
            // JSON bbox is assumed to be for EAST facing: thin in X, at west edge (x near 0)
            // When FACING=direction, the block faces that direction but is attached to the OPPOSITE wall
            return ImmutableMap.of(
                    // EAST: Faces east, attached to WEST wall (thin in X, west edge x near 0)
                    Direction.EAST, Block.createCuboidShape(
                            xMin * 16, yMin * 16, zMin * 16,
                            xMax * 16, yMax * 16, zMax * 16),
                    // WEST: Faces west, attached to EAST wall (thin in X, east edge x near 16)
                    Direction.WEST, Block.createCuboidShape(
                            (1 - xMax) * 16, yMin * 16, zMin * 16,
                            (1 - xMin) * 16, yMax * 16, zMax * 16),
                    // SOUTH: Faces south, attached to NORTH wall (thin in Z, north edge z near 0)
                    Direction.SOUTH, Block.createCuboidShape(
                            zMin * 16, yMin * 16, xMin * 16,
                            zMax * 16, yMax * 16, xMax * 16),
                    // NORTH: Faces north, attached to SOUTH wall (thin in Z, south edge z near 16)
                    Direction.NORTH, Block.createCuboidShape(
                            zMin * 16, yMin * 16, (1 - xMax) * 16,
                            zMax * 16, yMax * 16, (1 - xMin) * 16)
            );
        }
    }

    /**
     * Creates default shapes if no bounding box is defined
     */
    private static Map<Direction, VoxelShape> createDefaultShapes() {
        return ImmutableMap.of(
                Direction.NORTH, Block.createCuboidShape(0, 0, 14, 16, 16, 16),
                Direction.SOUTH, Block.createCuboidShape(0, 0, 0, 16, 16, 2),
                Direction.EAST, Block.createCuboidShape(0, 0, 0, 2, 16, 16),
                Direction.WEST, Block.createCuboidShape(14, 0, 0, 16, 16, 16)
        );
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState state = this.getDefaultState();
        WorldView world = ctx.getWorld();
        BlockPos pos = ctx.getBlockPos();

        // Try each horizontal direction to find a valid wall
        for (Direction direction : ctx.getPlacementDirections()) {
            if (direction.getAxis().isHorizontal()) {
                // Block faces opposite of the clicked direction (away from wall)
                Direction opposite = direction.getOpposite();
                state = state.with(FACING, opposite);
                if (state.canPlaceAt(world, pos)) {
                    return state;
                }
            }
        }

        return null; // Cannot place if no valid wall found
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

        // Check if the block behind is a solid full square face
        return attachState.isSideSolidFullSquare(world, attachPos, facing);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState,
                                                 WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        // Check if the supporting wall was removed
        if (direction.getOpposite() == state.get(FACING) && !this.canPlaceAt(state, world, pos)) {
            return Blocks.AIR.getDefaultState();
        }

        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return shapesByFacing.get(state.get(FACING));
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return shapesByFacing.get(state.get(FACING));
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    /**
     * Gets the BlockDefinition for this block.
     * @return BlockDefinition if block was created from JSON, null if created programmatically
     */
    public BlockDefinition getDefinition() {
        return def;
    }
}
