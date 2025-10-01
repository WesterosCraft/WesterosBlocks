package com.westerosblocks.block.custom;

import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.utils.ModProperties;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.ShapeContext;
import net.minecraft.world.BlockView;

import java.util.ArrayList;
import java.util.List;

public class WCCuboidNSEWBlock extends WCCuboidBlock {
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;

    // Store rotated bounding boxes for each facing direction
    protected VoxelShape[] boundingBoxesByFacing = new VoxelShape[4];

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

            return new WCCuboidNSEWBlock(settings, doToggleOnUse, doAddStates, customBoundingBox);
        }
    }

    public WCCuboidNSEWBlock(AbstractBlock.Settings settings) {
        this(settings, false, false, null);
    }

    public WCCuboidNSEWBlock(AbstractBlock.Settings settings, boolean doToggleOnUse, boolean addStates, VoxelShape customBoundingBox) {
        super(settings, doToggleOnUse, addStates, customBoundingBox);

        // Calculate rotated bounding boxes for each facing direction
        calculateRotatedBoundingBoxes(customBoundingBox != null ? customBoundingBox : VoxelShapes.fullCube());

        // Set default state with facing
        BlockState defbs = this.getDefaultState()
            .with(FACING, Direction.NORTH)
            .with(WATERLOGGED, false);

        if (STATE != null) {
            defbs = defbs.with(STATE, STATE.defValue);
        }
        this.setDefaultState(defbs);
    }

    private void calculateRotatedBoundingBoxes(VoxelShape originalShape) {
        boundingBoxesByFacing[0] = originalShape; // NORTH (0°)
        boundingBoxesByFacing[1] = rotateShapeY90(originalShape); // EAST (90°)
        boundingBoxesByFacing[2] = rotateShapeY180(originalShape); // SOUTH (180°)
        boundingBoxesByFacing[3] = rotateShapeY270(originalShape); // WEST (270°)
    }

    private VoxelShape rotateShapeY90(VoxelShape shape) {
        // Rotate 90 degrees around Y axis: (x,y,z) -> (1-z,y,x)
        return shape.isEmpty() ? VoxelShapes.empty() :
            VoxelShapes.cuboid(1.0 - shape.getBoundingBox().maxZ, shape.getBoundingBox().minY, shape.getBoundingBox().minX,
                              1.0 - shape.getBoundingBox().minZ, shape.getBoundingBox().maxY, shape.getBoundingBox().maxX);
    }

    private VoxelShape rotateShapeY180(VoxelShape shape) {
        // Rotate 180 degrees around Y axis: (x,y,z) -> (1-x,y,1-z)
        return shape.isEmpty() ? VoxelShapes.empty() :
            VoxelShapes.cuboid(1.0 - shape.getBoundingBox().maxX, shape.getBoundingBox().minY, 1.0 - shape.getBoundingBox().maxZ,
                              1.0 - shape.getBoundingBox().minX, shape.getBoundingBox().maxY, 1.0 - shape.getBoundingBox().minZ);
    }

    private VoxelShape rotateShapeY270(VoxelShape shape) {
        // Rotate 270 degrees around Y axis: (x,y,z) -> (z,y,1-x)
        return shape.isEmpty() ? VoxelShapes.empty() :
            VoxelShapes.cuboid(shape.getBoundingBox().minZ, shape.getBoundingBox().minY, 1.0 - shape.getBoundingBox().maxX,
                              shape.getBoundingBox().maxZ, shape.getBoundingBox().maxY, 1.0 - shape.getBoundingBox().minX);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
        Direction[] placementDirections = ctx.getPlacementDirections();
        Direction facing = Direction.NORTH;

        // Find the first horizontal direction from placement directions
        for (Direction direction : placementDirections) {
            if (direction.getAxis().isHorizontal()) {
                facing = direction.getOpposite();
                break;
            }
        }

        BlockState state = this.getDefaultState()
            .with(FACING, facing)
            .with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);

        if (STATE != null) {
            state = state.with(STATE, STATE.defValue);
        }

        return state;
    }

    @Override
    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return getBoundingBoxForFacing(state.get(FACING));
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return getBoundingBoxForFacing(state.get(FACING));
    }

    @Override
    public VoxelShape getCullingShape(BlockState state, BlockView world, BlockPos pos) {
        return getBoundingBoxForFacing(state.get(FACING));
    }

    private VoxelShape getBoundingBoxForFacing(Direction facing) {
        return switch (facing) {
            case NORTH -> boundingBoxesByFacing[0];
            case EAST -> boundingBoxesByFacing[1];
            case SOUTH -> boundingBoxesByFacing[2];
            case WEST -> boundingBoxesByFacing[3];
            default -> boundingBox;
        };
    }
}
