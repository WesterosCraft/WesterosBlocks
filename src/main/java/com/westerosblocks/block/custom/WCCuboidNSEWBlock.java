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
import java.util.Collection;
import java.util.List;

public class WCCuboidNSEWBlock extends WCCuboidBlock {
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    protected VoxelShape[] boundingBoxesByFacing = new VoxelShape[4];
    protected VoxelShape[][] stateSpecificBoundingBoxes = null;

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(AbstractBlock.Settings settings, BlockDefinition definition) {
            boolean doToggleOnUse = definition != null && definition.toggleOnUse();
            int numStates = definition != null ? definition.getStateCount() : 0;
            boolean doAddStates = numStates > 0;

            VoxelShape customBoundingBox = null;
            if (definition != null && definition.hasBoundingBox()) {
                BlockDefinition.BoundingBox bbox = definition.getBoundingBox();
                customBoundingBox = VoxelShapes.cuboid(
                    bbox.getXMin(), bbox.getYMin(), bbox.getZMin(),
                    bbox.getXMax(), bbox.getYMax(), bbox.getZMax()
                );
            }

            if (doAddStates) {
                List<String> stateValues = definition.getStateValues();
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

            return new WCCuboidNSEWBlock(settings, doToggleOnUse, doAddStates, customBoundingBox, definition);
        }
    }

    public WCCuboidNSEWBlock(AbstractBlock.Settings settings, boolean doToggleOnUse, boolean addStates, VoxelShape customBoundingBox, BlockDefinition definition) {
        super(settings, doToggleOnUse, addStates, customBoundingBox);

        // Check if we have state-specific bounding boxes
        if (definition != null && definition.hasStates()) {
            List<BlockDefinition.StateVariant> states = definition.getStates();
            boolean hasStateSpecificBoundingBoxes = false;

            // Check if any state has its own bounding box
            for (BlockDefinition.StateVariant state : states) {
                if (state.getBoundingBox() != null) {
                    hasStateSpecificBoundingBoxes = true;
                    break;
                }
            }

            if (hasStateSpecificBoundingBoxes) {
                calculateStateSpecificBoundingBoxes(definition, states);
            }
        }

        // Calculate default rotated bounding boxes for each facing direction
        VoxelShape defaultBoundingBox = customBoundingBox != null ? customBoundingBox : VoxelShapes.fullCube();
        calculateRotatedBoundingBoxes(defaultBoundingBox);

        // Set default state with facing (match old version)
        BlockState defbs = this.getDefaultState()
            .with(FACING, Direction.EAST)
            .with(WATERLOGGED, false);

        if (STATE != null) {
            defbs = defbs.with(STATE, STATE.defValue);
        }
        this.setDefaultState(defbs);
    }

    private void calculateStateSpecificBoundingBoxes(BlockDefinition definition, List<BlockDefinition.StateVariant> states) {
        int stateCount = states.size();
        stateSpecificBoundingBoxes = new VoxelShape[stateCount][4];

        for (int i = 0; i < stateCount; i++) {
            BlockDefinition.StateVariant state = states.get(i);
            VoxelShape stateShape;

            if (state.getBoundingBox() != null) {
                BlockDefinition.BoundingBox bbox = state.getBoundingBox();
                VoxelShape baseBBox = VoxelShapes.cuboid(
                    bbox.getXMin(), bbox.getYMin(), bbox.getZMin(),
                    bbox.getXMax(), bbox.getYMax(), bbox.getZMax()
                );

                Integer rotYOffset = state.getRotYOffset();
                if (rotYOffset != null && rotYOffset != 0) {

                    int inverseRotation = rotYOffset % 360;
                    stateShape = applyYRotationToBoundingBox(baseBBox, inverseRotation);
                } else {
                    stateShape = baseBBox;
                }
            } else {
                stateShape = boundingBox;
            }

            stateSpecificBoundingBoxes[i][0] = stateShape; // EAST (0°) - base, matches old
            stateSpecificBoundingBoxes[i][1] = rotateShapeY90(stateShape); // SOUTH (90°)
            stateSpecificBoundingBoxes[i][2] = rotateShapeY180(stateShape); // WEST (180°)
            stateSpecificBoundingBoxes[i][3] = rotateShapeY270(stateShape); // NORTH (270°)
        }
    }

    private void calculateRotatedBoundingBoxes(VoxelShape originalShape) {
        boundingBoxesByFacing[0] = originalShape; // EAST (0°) - base, matches old
        boundingBoxesByFacing[1] = rotateShapeY90(originalShape); // SOUTH (90°)
        boundingBoxesByFacing[2] = rotateShapeY180(originalShape); // WEST (180°)
        boundingBoxesByFacing[3] = rotateShapeY270(originalShape); // NORTH (270°)
    }

    private VoxelShape rotateShapeY90(VoxelShape shape) {
        return shape.isEmpty() ? VoxelShapes.empty() :
            VoxelShapes.cuboid(1.0 - shape.getBoundingBox().maxZ, shape.getBoundingBox().minY, shape.getBoundingBox().minX,
                              1.0 - shape.getBoundingBox().minZ, shape.getBoundingBox().maxY, shape.getBoundingBox().maxX);
    }

    private VoxelShape rotateShapeY180(VoxelShape shape) {
        return shape.isEmpty() ? VoxelShapes.empty() :
            VoxelShapes.cuboid(1.0 - shape.getBoundingBox().maxX, shape.getBoundingBox().minY, 1.0 - shape.getBoundingBox().maxZ,
                              1.0 - shape.getBoundingBox().minX, shape.getBoundingBox().maxY, 1.0 - shape.getBoundingBox().minZ);
    }

    private VoxelShape rotateShapeY270(VoxelShape shape) {
        return shape.isEmpty() ? VoxelShapes.empty() :
            VoxelShapes.cuboid(shape.getBoundingBox().minZ, shape.getBoundingBox().minY, 1.0 - shape.getBoundingBox().maxX,
                              shape.getBoundingBox().maxZ, shape.getBoundingBox().maxY, 1.0 - shape.getBoundingBox().minX);
    }

    private VoxelShape applyYRotationToBoundingBox(VoxelShape shape, int degrees) {
        int normalizedDegrees = degrees % 360;
        return switch (normalizedDegrees) {
            case 0 -> shape;
            case 90 -> rotateShapeY90(shape);
            case 180 -> rotateShapeY180(shape);
            case 270 -> rotateShapeY270(shape);
            default -> shape;
        };
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
        Direction facing = Direction.EAST;

        for (Direction direction : placementDirections) {
            if (direction == Direction.EAST || direction == Direction.WEST ||
                direction == Direction.NORTH || direction == Direction.SOUTH) {
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
        return getBoundingBoxForState(state);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return getBoundingBoxForState(state);
    }

    @Override
    public VoxelShape getCullingShape(BlockState state, BlockView world, BlockPos pos) {
        return getBoundingBoxForState(state);
    }

    private VoxelShape getBoundingBoxForFacing(Direction facing) {
        return switch (facing) {
            case EAST -> boundingBoxesByFacing[0];   // Base (0°) - matches old
            case SOUTH -> boundingBoxesByFacing[1];  // 90°
            case WEST -> boundingBoxesByFacing[2];   // 180°
            case NORTH -> boundingBoxesByFacing[3];  // 270°
            default -> boundingBox;
        };
    }

    private VoxelShape getBoundingBoxForState(BlockState state) {
        if (stateSpecificBoundingBoxes != null && STATE != null && state.contains(STATE)) {
            String currentStateValue = state.get(STATE);

            Collection<String> stateValues = STATE.getValues();
            List<String> stateValuesList = new ArrayList<>(stateValues);
            int stateIndex = stateValuesList.indexOf(currentStateValue);

            if (stateIndex >= 0 && stateIndex < stateSpecificBoundingBoxes.length) {
                Direction facing = state.get(FACING);
                int facingIndex = switch (facing) {
                    case SOUTH -> 0;  // Index 0 - use base for SOUTH (180° shift)
                    case WEST -> 1;   // Index 1 - use 90° for WEST
                    case NORTH -> 2;  // Index 2 - use 180° for NORTH
                    case EAST -> 3;   // Index 3 - use 270° for EAST
                    default -> 0;
                };

                return stateSpecificBoundingBoxes[stateIndex][facingIndex];
            }
        }

        return getBoundingBoxForFacing(state.get(FACING));
    }
}
