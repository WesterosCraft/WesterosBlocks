package com.westerosblocks.block.custom;

import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.utils.ModProperties;
import net.minecraft.block.*;
import net.minecraft.block.enums.SlabType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.List;

public class WCPlantBlock extends Block {
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    private final boolean layerSensitive;
    private final boolean toggleOnUse;
    private IntProperty LAYERS;
    private ModProperties.StateProperty STATE;

    protected static IntProperty tempLAYERS;
    protected static ModProperties.StateProperty tempSTATE;

    public static final VoxelShape[] SHAPE_BY_LAYER = new VoxelShape[] {
            VoxelShapes.empty(),
            Block.createCuboidShape(0.0D, -14.0D, 0.0D, 16.0D, 2.0D, 16.0D),
            Block.createCuboidShape(0.0D, -12.0D, 0.0D, 16.0D, 4.0D, 16.0D),
            Block.createCuboidShape(0.0D, -10.0D, 0.0D, 16.0D, 6.0D, 16.0D),
            Block.createCuboidShape(0.0D, -8.0D, 0.0D, 16.0D, 8.0D, 16.0D),
            Block.createCuboidShape(0.0D, -6.0D, 0.0D, 16.0D, 10.0D, 16.0D),
            Block.createCuboidShape(0.0D, -4.0D, 0.0D, 16.0D, 12.0D, 16.0D),
            Block.createCuboidShape(0.0D, -2.0D, 0.0D, 16.0D, 14.0D, 16.0D),
            Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)
    };

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(AbstractBlock.Settings settings, BlockDefinition definition) {
            boolean layerSensitive = definition != null && definition.isLayerSensitive();
            boolean toggleOnUse = definition != null && definition.toggleOnUse();
            List<String> stateValues = definition != null ? definition.getStateValues() : null;

            // Reset static fields before setting them (prevent leakage between blocks)
            tempLAYERS = null;
            tempSTATE = null;

            if (layerSensitive) {
                tempLAYERS = Properties.LAYERS;
            }

            // Set the STATE property if stateValues are provided (need at least 2 for a valid property)
            if (stateValues != null && stateValues.size() > 1) {
                tempSTATE = new ModProperties.StateProperty(stateValues);
            }

            return new WCPlantBlock(settings, layerSensitive, toggleOnUse);
        }
    }

    protected WCPlantBlock(AbstractBlock.Settings settings, boolean layerSensitive, boolean toggleOnUse) {
        super(settings);
        this.layerSensitive = layerSensitive;
        this.toggleOnUse = toggleOnUse;

        BlockState defbs = this.getDefaultState().with(WATERLOGGED, false);
        if (layerSensitive && tempLAYERS != null) {
            defbs = defbs.with(tempLAYERS, 8); // Default to full height
        }
        if (tempSTATE != null) {
            defbs = defbs.with(tempSTATE, tempSTATE.defValue);
        }
        this.setDefaultState(defbs);
    }

    public boolean isLayerSensitive() {
        return layerSensitive;
    }

    public boolean isToggleOnUse() {
        return toggleOnUse;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState bs = super.getPlacementState(ctx);
        if (bs == null)
            return null;

        FluidState fluidstate = ctx.getWorld().getFluidState(ctx.getBlockPos());
        bs = bs.with(WATERLOGGED, fluidstate.isIn(FluidTags.WATER));

        if (STATE != null) {
            bs = bs.with(STATE, STATE.defValue);
        }

        if (layerSensitive && LAYERS != null) {
            BlockState below = ctx.getWorld().getBlockState(ctx.getBlockPos().offset(Direction.DOWN));
            if (below.contains(Properties.LAYERS)) {
                Block blk = below.getBlock();
                int layer = below.get(Properties.LAYERS);
                if ((blk instanceof SnowBlock) || ((blk instanceof WCLayerBlock) && ((WCLayerBlock) blk).softLayer)) {
                    layer = (layer > 2) ? layer - 2 : 1;
                }
                bs = bs.with(LAYERS, layer);
            } else if (below.getBlock() instanceof SlabBlock) {
                SlabType slabType = below.get(Properties.SLAB_TYPE);
                if (slabType == SlabType.BOTTOM) {
                    bs = bs.with(LAYERS, 4); // Half height for bottom slabs
                } else {
                    bs = bs.with(LAYERS, 8); // Full height for top/double slabs
                }
            } else {
                bs = bs.with(LAYERS, 8); // Default full height
            }
        }
        return bs;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState,
            WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED);
        if (tempSTATE != null) {
            STATE = tempSTATE;
            tempSTATE = null;
            builder.add(STATE);
        }
        if (tempLAYERS != null) {
            LAYERS = tempLAYERS;
            tempLAYERS = null;
            builder.add(LAYERS);
        }
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        Hand hand = player.getActiveHand();
        if (this.toggleOnUse && (this.STATE != null) && player.isCreative() && player.getStackInHand(hand).isEmpty()) {
            state = state.cycle(this.STATE);
            world.setBlockState(pos, state, 10);
            world.syncWorldEvent(player, 1006, pos, 0);
            return ActionResult.success(world.isClient);
        }
        return ActionResult.PASS;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (layerSensitive && LAYERS != null) {
            return SHAPE_BY_LAYER[state.get(LAYERS)];
        }
        return VoxelShapes.fullCube();
    }
}
