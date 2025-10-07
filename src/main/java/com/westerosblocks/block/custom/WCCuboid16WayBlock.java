package com.westerosblocks.block.custom;

import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.utils.ModProperties;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

import java.util.ArrayList;
import java.util.List;

/**
 * A cuboid block with 16-way rotation (22.5° increments).
 * Supports custom bounding boxes, waterlogging, and state properties.
 */
public class WCCuboid16WayBlock extends WCCuboidBlock {
    public static final IntProperty ROTATION = Properties.ROTATION;
    private static final int ROTATIONS = 16;

    // Store rotated bounding boxes for each rotation (0-15)
    protected VoxelShape[] boundingBoxesByRotation = new VoxelShape[ROTATIONS];

    // Store state-specific bounding boxes (if block has states with different bounding boxes)
    protected VoxelShape[][] stateSpecificBoundingBoxes = null;

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

            return new WCCuboid16WayBlock(settings, doToggleOnUse, doAddStates, customBoundingBox, definition);
        }
    }

    public WCCuboid16WayBlock(AbstractBlock.Settings settings) {
        this(settings, false, false, null, null);
    }

    public WCCuboid16WayBlock(AbstractBlock.Settings settings, boolean doToggleOnUse, boolean addStates, VoxelShape customBoundingBox) {
        this(settings, doToggleOnUse, addStates, customBoundingBox, null);
    }

    public WCCuboid16WayBlock(AbstractBlock.Settings settings, boolean doToggleOnUse, boolean addStates,
                              VoxelShape customBoundingBox, BlockDefinition definition) {
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

        // Calculate default rotated bounding boxes for each rotation (0-15)
        VoxelShape defaultBoundingBox = customBoundingBox != null ? customBoundingBox : VoxelShapes.fullCube();
        calculateRotatedBoundingBoxes(defaultBoundingBox);

        // Set default state with rotation 0
        BlockState defbs = this.getDefaultState()
            .with(ROTATION, 0)
            .with(WATERLOGGED, false);

        if (STATE != null) {
            defbs = defbs.with(STATE, STATE.defValue);
        }
        this.setDefaultState(defbs);
    }

    private void calculateStateSpecificBoundingBoxes(BlockDefinition definition, List<BlockDefinition.StateVariant> states) {
        int stateCount = states.size();
        stateSpecificBoundingBoxes = new VoxelShape[stateCount][ROTATIONS]; // [state][rotation]

        for (int i = 0; i < stateCount; i++) {
            BlockDefinition.StateVariant state = states.get(i);
            VoxelShape stateShape;

            if (state.getBoundingBox() != null) {
                // Use state-specific bounding box
                BlockDefinition.BoundingBox bbox = state.getBoundingBox();
                stateShape = VoxelShapes.cuboid(
                    bbox.getXMin(), bbox.getYMin(), bbox.getZMin(),
                    bbox.getXMax(), bbox.getYMax(), bbox.getZMax()
                );
            } else {
                // Fallback to default bounding box
                stateShape = boundingBox;
            }

            // Calculate rotations for this state's bounding box
            for (int rot = 0; rot < ROTATIONS; rot++) {
                stateSpecificBoundingBoxes[i][rot] = applyYRotationToBoundingBox(stateShape, rot * 22.5f);
            }
        }
    }

    private void calculateRotatedBoundingBoxes(VoxelShape baseBoundingBox) {
        // Calculate bounding boxes for all 16 rotations (0° to 337.5° in 22.5° steps)
        for (int i = 0; i < ROTATIONS; i++) {
            float angleDegrees = i * 22.5f;
            boundingBoxesByRotation[i] = applyYRotationToBoundingBox(baseBoundingBox, angleDegrees);
        }
    }

    /**
     * Applies a Y-axis rotation to a bounding box.
     * @param shape The original bounding box
     * @param angleDegrees Rotation angle in degrees
     * @return Rotated bounding box
     */
    private VoxelShape applyYRotationToBoundingBox(VoxelShape shape, float angleDegrees) {
        if (Math.abs(angleDegrees) < 0.01f || shape == VoxelShapes.fullCube()) {
            return shape;
        }

        // Convert angle to radians
        double angleRadians = Math.toRadians(angleDegrees);
        double cos = Math.cos(angleRadians);
        double sin = Math.sin(angleRadians);

        // Get bounding box
        double minX = shape.getMin(net.minecraft.util.math.Direction.Axis.X);
        double minY = shape.getMin(net.minecraft.util.math.Direction.Axis.Y);
        double minZ = shape.getMin(net.minecraft.util.math.Direction.Axis.Z);
        double maxX = shape.getMax(net.minecraft.util.math.Direction.Axis.X);
        double maxY = shape.getMax(net.minecraft.util.math.Direction.Axis.Y);
        double maxZ = shape.getMax(net.minecraft.util.math.Direction.Axis.Z);

        // Translate to origin (center at 0.5, 0.5)
        double cx = 0.5;
        double cz = 0.5;

        // Rotate all 4 corners of the bounding box
        double[][] corners = {
            {minX - cx, minZ - cz},
            {maxX - cx, minZ - cz},
            {minX - cx, maxZ - cz},
            {maxX - cx, maxZ - cz}
        };

        double newMinX = Double.MAX_VALUE;
        double newMaxX = Double.MIN_VALUE;
        double newMinZ = Double.MAX_VALUE;
        double newMaxZ = Double.MIN_VALUE;

        for (double[] corner : corners) {
            double x = corner[0];
            double z = corner[1];

            // Apply rotation
            double rotatedX = x * cos - z * sin;
            double rotatedZ = x * sin + z * cos;

            // Translate back
            rotatedX += cx;
            rotatedZ += cz;

            // Track min/max
            newMinX = Math.min(newMinX, rotatedX);
            newMaxX = Math.max(newMaxX, rotatedX);
            newMinZ = Math.min(newMinZ, rotatedZ);
            newMaxZ = Math.max(newMaxZ, rotatedZ);
        }

        // Clamp to valid range [0, 1]
        newMinX = MathHelper.clamp(newMinX, 0, 1);
        newMaxX = MathHelper.clamp(newMaxX, 0, 1);
        newMinZ = MathHelper.clamp(newMinZ, 0, 1);
        newMaxZ = MathHelper.clamp(newMaxZ, 0, 1);

        return VoxelShapes.cuboid(newMinX, minY, newMinZ, newMaxX, maxY, newMaxZ);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(ROTATION);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (stateSpecificBoundingBoxes != null && STATE != null) {
            // Use state-specific bounding box with rotation
            int stateIndex = STATE.getIndex(state.get(STATE));
            int rotation = state.get(ROTATION);
            return stateSpecificBoundingBoxes[stateIndex][rotation];
        } else {
            // Use default rotated bounding box
            int rotation = state.get(ROTATION);
            return boundingBoxesByRotation[rotation];
        }
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());

        // Calculate rotation based on player's horizontal rotation (0-15)
        // Use getHorizontalPlayerFacing().asRotation() which gives us the player's horizontal rotation (0-360)
        float playerRotation = ctx.getPlayerLookDirection().getOpposite().asRotation();
        int rotation = MathHelper.floor((playerRotation * 16.0F / 360.0F) + 0.5F) & 15;

        BlockState state = this.getDefaultState()
            .with(ROTATION, rotation)
            .with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);

        if (STATE != null) {
            state = state.with(STATE, STATE.defValue);
        }

        return state;
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        // BlockRotation provides NONE, CLOCKWISE_90, CLOCKWISE_180, COUNTERCLOCKWISE_90
        int currentRotation = state.get(ROTATION);
        int rotationSteps = switch (rotation) {
            case CLOCKWISE_90 -> 4;  // 90° = 4 steps of 22.5°
            case CLOCKWISE_180 -> 8; // 180° = 8 steps
            case COUNTERCLOCKWISE_90 -> 12; // 270° = 12 steps (or -4)
            default -> 0;
        };

        int newRotation = (currentRotation + rotationSteps) & 15; // Keep in range 0-15
        return state.with(ROTATION, newRotation);
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        int rotation = state.get(ROTATION);

        // Mirror the rotation value
        int mirroredRotation = switch (mirror) {
            case FRONT_BACK -> (16 - rotation) & 15;  // Flip across Z axis
            case LEFT_RIGHT -> (32 - rotation) & 15;  // Flip across X axis
            default -> rotation;
        };

        return state.with(ROTATION, mirroredRotation);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }
}
