package com.westerosblocks.block.custom;

import com.google.common.collect.ImmutableMap;
import com.westerosblocks.block.ModBlocks;
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

public class WCBuntingBlock extends Block implements WCBlockDef {
    protected BlockDefinition def;
    private final boolean allowUnsupported;
    private final Block ceilingBlock;

    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

    private final Map<Direction, VoxelShape> shapesByFacing;

    public WCBuntingBlock(Settings settings, BlockDefinition def, Map<Direction, VoxelShape> shapes,
                          boolean allowUnsupported, Block ceilingBlock) {
        super(settings);
        this.def = def;
        this.allowUnsupported = allowUnsupported;
        this.ceilingBlock = ceilingBlock;
        this.shapesByFacing = shapes != null ? shapes : createDefaultShapes();
        this.setDefaultState(this.getStateManager().getDefaultState()
                .with(FACING, Direction.NORTH));
    }

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(BlockDefinition definition) {
            boolean allowUnsupported = definition.isAllowUnsupported();

            // Create ceiling block first
            AbstractBlock.Settings ceilingSettings = definition.makeSettings();
            VoxelShape ceilingShape = createCeilingShape(definition);
            Block ceilingBlock = new WCCeilingBuntingBlock(ceilingSettings, definition, ceilingShape, allowUnsupported);

            // Register ceiling block without item
            String ceilingName = definition.getBlockName() + "_ceiling";
            Block registeredCeilingBlock = ModBlocks.registerBlockWithoutItem(ceilingName, ceilingBlock);

            // Create wall (main) block with reference to ceiling block
            AbstractBlock.Settings wallSettings = definition.makeSettings();
            Map<Direction, VoxelShape> wallShapes = createRotatedShapes(definition);
            return new WCBuntingBlock(wallSettings, definition, wallShapes, allowUnsupported, registeredCeilingBlock);
        }

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

            return ImmutableMap.of(
                    Direction.EAST, Block.createCuboidShape(
                            xMin * 16, yMin * 16, zMin * 16,
                            xMax * 16, yMax * 16, zMax * 16),
                    Direction.WEST, Block.createCuboidShape(
                            (1 - xMax) * 16, yMin * 16, zMin * 16,
                            (1 - xMin) * 16, yMax * 16, zMax * 16),
                    Direction.SOUTH, Block.createCuboidShape(
                            zMin * 16, yMin * 16, xMin * 16,
                            zMax * 16, yMax * 16, xMax * 16),
                    Direction.NORTH, Block.createCuboidShape(
                            zMin * 16, yMin * 16, (1 - xMax) * 16,
                            zMax * 16, yMax * 16, (1 - xMin) * 16)
            );
        }

        private static VoxelShape createCeilingShape(BlockDefinition definition) {
            return Block.createCuboidShape(4, 12, 4, 12, 16, 12);
        }
    }

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
        // If clicking the underside of a block, place ceiling variant
        if (ctx.getSide() == Direction.DOWN) {
            BlockPos ceilingPos = ctx.getBlockPos();
            BlockState ceilingState = ceilingBlock.getDefaultState();
            if (ceilingState.canPlaceAt(ctx.getWorld(), ceilingPos)) {
                // Face the player's horizontal facing
                Direction playerFacing = ctx.getHorizontalPlayerFacing();
                return ceilingState.with(WCCeilingBuntingBlock.FACING, playerFacing);
            }
        }

        // Otherwise try wall placement
        BlockState state = this.getDefaultState();
        WorldView world = ctx.getWorld();
        BlockPos pos = ctx.getBlockPos();

        for (Direction direction : ctx.getPlacementDirections()) {
            if (direction.getAxis().isHorizontal()) {
                Direction opposite = direction.getOpposite();
                state = state.with(FACING, opposite);
                if (state.canPlaceAt(world, pos)) {
                    return state;
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

    public BlockDefinition getDefinition() {
        return def;
    }
}
