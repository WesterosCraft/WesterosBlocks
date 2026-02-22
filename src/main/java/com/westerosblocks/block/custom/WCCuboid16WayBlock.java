package com.westerosblocks.block.custom;

import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.utils.ModProperties;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import com.westerosblocks.data.BlockDefinition.CuboidElement.CuboidRotation;

import java.util.List;

public class WCCuboid16WayBlock extends WCCuboidBlock {
    public static final IntProperty ROTATION = Properties.ROTATION;
    private static final int ROTATIONS = 16;
    private static final CuboidRotation[] SHAPE_ROTATIONS = {
        null, CuboidRotation.ROTY90, CuboidRotation.ROTY180, CuboidRotation.ROTY270
    };

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(BlockDefinition definition) {
            AbstractBlock.Settings settings = definition.makeSettings();
            ModProperties.StateProperty stateProperty = definition.buildStateProperty();
            boolean doToggleOnUse = definition.toggleOnUse();

            tempSTATE = stateProperty;

            return new WCCuboid16WayBlock(settings, definition, doToggleOnUse);
        }
    }

    public WCCuboid16WayBlock(AbstractBlock.Settings settings, BlockDefinition def, boolean doToggleOnUse) {
        super(settings, def, 16, doToggleOnUse);  // modelsPerState = 16 (16 visual rotations)

        // Build rotations - create 16 entries but only 4 unique rotations (one per 90° quadrant)
        // Visual 22.5° increments are handled by model rotation in the exporter
        int stcnt = def.getStateCount();
        for (int stidx = 0; stidx < stcnt; stidx++) {
            int idx = stidx * this.modelsPerState;

            List<BlockDefinition.CuboidElement> baseCuboids = cuboid_by_facing[idx];
            if (baseCuboids != null && !baseCuboids.isEmpty()) {
                // Create 16 rotation entries, but only 4 unique geometries
                for (int i = 1; i < ROTATIONS; i++) {
                    // Determine which 90° rotation to use based on quadrant
                    // Rotations 0-3: 0°, 4-7: 90°, 8-11: 180°, 12-15: 270°
                    int quadrant = i / 4;  // 0, 1, 2, or 3
                    CuboidRotation rot = SHAPE_ROTATIONS[quadrant];

                    for (BlockDefinition.CuboidElement cuboid : baseCuboids) {
                        cuboid_by_facing[idx + i].add(rot != null ? cuboid.rotateCuboid(rot) : cuboid);
                    }
                }
            }
        }

        // Compute shapes from rotated cuboids
        for (int i = 0; i < cuboid_by_facing.length; i++) {
            if (SHAPE_BY_INDEX[i] == null) {
                SHAPE_BY_INDEX[i] = computeShapeFromCuboids(cuboid_by_facing[i]);
            }
        }

        // Set default state with rotation 0
        BlockState defbs = this.getDefaultState()
            .with(ROTATION, 0)
            .with(WATERLOGGED, false);

        if (STATE != null) {
            defbs = defbs.with(STATE, STATE.defValue);
        }
        this.setDefaultState(defbs);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(ROTATION);
    }

    /**
     * Override to add rotation offset to state index.
     * Index = (stateIdx × modelsPerState) + rotation
     */
    @Override
    protected int getIndexFromState(BlockState state) {
        int off = super.getIndexFromState(state);  // stateIdx × modelsPerState
        int rotation = state.get(ROTATION);
        return off + rotation;  // Add rotation (0-15)
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
        int rotation = MathHelper.floor((double)(ctx.getPlayerYaw() * 16.0F / 360.0F) + 0.5D) & 15;

        BlockState state = this.getDefaultState()
            .with(ROTATION, rotation)
            .with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);

        if (STATE != null) {
            state = state.with(STATE, STATE.defValue);
        }

        return state;
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        Hand hand = player.getActiveHand();

        // If toggle on use is enabled and player is in creative mode with empty hand
        if (this.toggleOnUse && player.isCreative() && player.getStackInHand(hand).isEmpty()) {
            // First priority: cycle through states if STATE property exists
            if (this.STATE != null && state.contains(this.STATE)) {
                state = state.cycle(this.STATE);
                world.setBlockState(pos, state, Block.NOTIFY_ALL);
                world.syncWorldEvent(player, 1006, pos, 0);
                return ActionResult.success(world.isClient);
            }

            else if (state.contains(ROTATION)) {
                int currentRotation = state.get(ROTATION);
                int newRotation = (currentRotation + 1) & 15;
                state = state.with(ROTATION, newRotation);
                world.setBlockState(pos, state, Block.NOTIFY_ALL);
                world.syncWorldEvent(player, 1006, pos, 0);
                return ActionResult.success(world.isClient);
            }
        }

        return ActionResult.PASS;
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        int currentRotation = state.get(ROTATION);
        int rotationSteps = switch (rotation) {
            case CLOCKWISE_90 -> 4;  // 90° = 4 steps of 22.5°
            case CLOCKWISE_180 -> 8; // 180° = 8 steps
            case COUNTERCLOCKWISE_90 -> 12; // 270° = 12 steps
            default -> 0;
        };

        int newRotation = (currentRotation + rotationSteps) & 15; // Keep in range 0-15
        return state.with(ROTATION, newRotation);
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        int rotation = state.get(ROTATION);

        int mirroredRotation = switch (mirror) {
            case FRONT_BACK -> (16 - rotation) & 15;
            case LEFT_RIGHT -> (32 - rotation) & 15;
            default -> rotation;
        };

        return state.with(ROTATION, mirroredRotation);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }
}
