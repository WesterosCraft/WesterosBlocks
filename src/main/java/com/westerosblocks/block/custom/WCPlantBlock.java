package com.westerosblocks.block.custom;

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

public class WCPlantBlock extends Block {
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    private final String blockName;
    private final String creativeTab;
    private final boolean layerSensitive;
    private final boolean toggleOnUse;
    private IntProperty LAYERS;

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

    public WCPlantBlock(AbstractBlock.Settings settings) {
        this(settings, "plant", "building_blocks", false, false);
    }

    public WCPlantBlock(AbstractBlock.Settings settings, String blockName, String creativeTab) {
        this(settings, blockName, creativeTab, false, false);
    }

    public WCPlantBlock(AbstractBlock.Settings settings, String blockName, String creativeTab, boolean layerSensitive,
            boolean toggleOnUse) {
        super(settings);
        this.blockName = blockName;
        this.creativeTab = creativeTab;
        this.layerSensitive = layerSensitive;
        this.toggleOnUse = toggleOnUse;

        BlockState defbs = this.getDefaultState().with(WATERLOGGED, false);
        this.setDefaultState(defbs);
    }

    public String getBlockName() {
        return blockName;
    }

    public String getCreativeTab() {
        return creativeTab;
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

        if (layerSensitive && LAYERS != null) {
            BlockState below = ctx.getWorld().getBlockState(ctx.getBlockPos().offset(Direction.DOWN));
            if (below.contains(Properties.LAYERS)) {
                Block blk = below.getBlock();
                int layer = below.get(Properties.LAYERS);
                // See if soft layer
                if ((blk instanceof SnowBlock) || ((blk instanceof WCLayerBlock) && ((WCLayerBlock) blk).softLayer)) {
                    layer = (layer > 2) ? layer - 2 : 1;
                }
                bs = bs.with(LAYERS, layer);
            } else if (below.getBlock() instanceof SlabBlock) {
                if (below.get(Properties.SLAB_TYPE) == SlabType.BOTTOM) {
                    bs = bs.with(LAYERS, 4);
                }
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
        if (layerSensitive) {
            LAYERS = Properties.LAYERS;
            builder.add(LAYERS);
        }
        builder.add(WATERLOGGED);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        Hand hand = player.getActiveHand();
        if (this.toggleOnUse && player.isCreative() && player.getStackInHand(hand).isEmpty()) {
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