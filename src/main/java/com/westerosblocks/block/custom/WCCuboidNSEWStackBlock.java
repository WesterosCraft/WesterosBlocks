package com.westerosblocks.block.custom;

import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.utils.ModProperties;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import java.util.ArrayList;
import java.util.List;

public class WCCuboidNSEWStackBlock extends WCCuboidBlock implements Waterloggable {
    public static final DirectionProperty FACING = DirectionProperty.of("facing", Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.NORTH);
    public static final EnumProperty<DoubleBlockHalf> HALF = Properties.DOUBLE_BLOCK_HALF;

    protected final boolean allowHalfBreak;
    protected final VoxelShape[] SHAPE_BY_INDEX;

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(AbstractBlock.Settings settings, BlockDefinition definition) {
            boolean doAllowHalfBreak = definition != null && definition.isAllowHalfBreak();

            boolean doToggleOnUse = definition != null && definition.toggleOnUse();
            int numStates = definition != null ? definition.getStateCount() : 0;
            boolean doAddStates = numStates > 1;

            if (doAddStates) {
                List<String> stateValues = definition != null ? definition.getStateValues() : null;
                if (stateValues != null && !stateValues.isEmpty()) {
                    tempSTATE = new ModProperties.StateProperty(stateValues);
                } else {
                    ArrayList<String> stateIds = new ArrayList<>();
                    for (int i = 0; i < numStates; i++) {
                        stateIds.add("state" + i);
                    }
                    tempSTATE = new ModProperties.StateProperty(stateIds);
                }
            }

            return new WCCuboidNSEWStackBlock(settings, doAllowHalfBreak, doToggleOnUse, doAddStates, definition);
        }
    }

    public WCCuboidNSEWStackBlock(AbstractBlock.Settings settings, boolean doAllowHalfBreak, boolean doToggleOnUse, boolean addStates, BlockDefinition definition) {
        super(settings, doToggleOnUse, addStates, null);
        this.allowHalfBreak = doAllowHalfBreak;
        this.SHAPE_BY_INDEX = new VoxelShape[8];

        if (definition != null && definition.hasStackElements() && definition.getStackElements().size() >= 2) {
            BlockDefinition.StackElement bottomElement = definition.getStackElements().get(0);
            BlockDefinition.StackElement topElement = definition.getStackElements().get(1);

            initializeShapesForHalf(bottomElement, 0);

            initializeShapesForHalf(topElement, 4);
        } else {
            for (int i = 0; i < 8; i++) {
                SHAPE_BY_INDEX[i] = VoxelShapes.fullCube();
            }
        }

        BlockState defbs = this.getDefaultState()
            .with(WATERLOGGED, false)
            .with(HALF, DoubleBlockHalf.LOWER)
            .with(FACING, Direction.EAST);

        if (addStates && tempSTATE != null) {
            defbs = defbs.with(tempSTATE, tempSTATE.defValue);
        }
        this.setDefaultState(defbs);
    }

    private void initializeShapesForHalf(BlockDefinition.StackElement element, int baseIndex) {
        if (element != null && element.hasBoundingBox()) {
            BlockDefinition.BoundingBox bbox = element.getBoundingBox();

            SHAPE_BY_INDEX[baseIndex] = createRotatedShape(bbox, 0); // EAST (0°)
            SHAPE_BY_INDEX[baseIndex + 1] = createRotatedShape(bbox, 90); // SOUTH (90°)
            SHAPE_BY_INDEX[baseIndex + 2] = createRotatedShape(bbox, 180); // WEST (180°)
            SHAPE_BY_INDEX[baseIndex + 3] = createRotatedShape(bbox, 270); // NORTH (270°)
        } else {
            for (int i = 0; i < 4; i++) {
                SHAPE_BY_INDEX[baseIndex + i] = VoxelShapes.fullCube();
            }
        }
    }

    private VoxelShape createRotatedShape(BlockDefinition.BoundingBox bbox, int rotationDegrees) {
        double xMin = bbox.getXMin();
        double yMin = bbox.getYMin();
        double zMin = bbox.getZMin();
        double xMax = bbox.getXMax();
        double yMax = bbox.getYMax();
        double zMax = bbox.getZMax();

        // Rotate around Y-axis (center point is 0.5, 0.5)
        double centerX = 0.5;
        double centerZ = 0.5;

        // Calculate corners relative to center
        double x1 = xMin - centerX;
        double z1 = zMin - centerZ;
        double x2 = xMax - centerX;
        double z2 = zMax - centerZ;

        double newX1, newZ1, newX2, newZ2;

        switch (rotationDegrees) {
            case 0: // EAST - no rotation
                newX1 = x1;
                newZ1 = z1;
                newX2 = x2;
                newZ2 = z2;
                break;
            case 90: // SOUTH - rotate 90° clockwise
                newX1 = -z2;
                newZ1 = x1;
                newX2 = -z1;
                newZ2 = x2;
                break;
            case 180: // WEST - rotate 180°
                newX1 = -x2;
                newZ1 = -z2;
                newX2 = -x1;
                newZ2 = -z1;
                break;
            case 270: // NORTH - rotate 270° clockwise (90° counter-clockwise)
                newX1 = z1;
                newZ1 = -x2;
                newX2 = z2;
                newZ2 = -x1;
                break;
            default:
                throw new IllegalArgumentException("Invalid rotation: " + rotationDegrees);
        }

        // Convert back to absolute coordinates
        newX1 += centerX;
        newZ1 += centerZ;
        newX2 += centerX;
        newZ2 += centerZ;

        // Ensure min < max
        double finalXMin = Math.min(newX1, newX2);
        double finalXMax = Math.max(newX1, newX2);
        double finalZMin = Math.min(newZ1, newZ2);
        double finalZMax = Math.max(newZ1, newZ2);

        return VoxelShapes.cuboid(finalXMin, yMin, finalZMin, finalXMax, yMax, finalZMax);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING, HALF);
    }

    protected int getIndexFromState(BlockState state) {
        int topOffset = (state.get(HALF) == DoubleBlockHalf.LOWER) ? 0 : 4;
        return switch (state.get(FACING)) {
            case SOUTH -> topOffset + 1;
            case WEST -> topOffset + 2;
            case NORTH -> topOffset + 3;
            default -> topOffset; // EAST
        };
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, net.minecraft.world.BlockView world, BlockPos pos, net.minecraft.block.ShapeContext context) {
        return SHAPE_BY_INDEX[getIndexFromState(state)];
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, net.minecraft.world.BlockView world, BlockPos pos, net.minecraft.block.ShapeContext context) {
        return SHAPE_BY_INDEX[getIndexFromState(state)];
    }

    @Override
    public VoxelShape getCullingShape(BlockState state, net.minecraft.world.BlockView world, BlockPos pos) {
        return SHAPE_BY_INDEX[getIndexFromState(state)];
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockPos blockpos = ctx.getBlockPos();
        if (blockpos.getY() < ctx.getWorld().getTopY() && ctx.getWorld().getBlockState(blockpos.up()).canReplace(ctx)) {
            FluidState fluidstate = ctx.getWorld().getFluidState(ctx.getBlockPos());
            Direction[] adirection = ctx.getPlacementDirections();
            Direction dir = Direction.EAST; // Default
            for (Direction d : adirection) {
                if (d == Direction.EAST || d == Direction.WEST || d == Direction.NORTH || d == Direction.SOUTH) {
                    dir = d;
                    break;
                }
            }
            BlockState bs = this.getDefaultState()
                .with(FACING, dir)
                .with(HALF, DoubleBlockHalf.LOWER)
                .with(WATERLOGGED, fluidstate.isIn(FluidTags.WATER));

            if (STATE != null) {
                bs = bs.with(STATE, STATE.defValue);
            }
            return bs;
        } else {
            return null;
        }
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState,
                                                WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (allowHalfBreak) {
            return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
        }

        DoubleBlockHalf doubleblockhalf = state.get(HALF);
        if (direction.getAxis() != Direction.Axis.Y
                || doubleblockhalf == DoubleBlockHalf.LOWER != (direction == Direction.UP)
                || neighborState.isOf(this) && neighborState.get(HALF) != doubleblockhalf) {
            return doubleblockhalf == DoubleBlockHalf.LOWER
                    && direction == Direction.DOWN
                    && !state.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
        } else {
            return Blocks.AIR.getDefaultState();
        }
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        BlockPos above = pos.up();
        FluidState fluidstate = world.getFluidState(above);
        BlockState newstate = this.getDefaultState()
            .with(FACING, state.get(FACING))
            .with(HALF, DoubleBlockHalf.UPPER)
            .with(WATERLOGGED, fluidstate.isIn(FluidTags.WATER));

        if (STATE != null && state.contains(STATE)) {
            newstate = newstate.with(STATE, state.get(STATE));
        }
        world.setBlockState(above, newstate, 3);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        if (this.allowHalfBreak || (state.get(HALF) != DoubleBlockHalf.UPPER)) {
            return super.canPlaceAt(state, world, pos);
        } else {
            BlockState blockstate = world.getBlockState(pos.down());
            if (state.getBlock() != this) {
                return super.canPlaceAt(state, world, pos);
            }
            return blockstate.isOf(this) && blockstate.get(HALF) == DoubleBlockHalf.LOWER;
        }
    }
}
