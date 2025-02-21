package com.westerosblocks.block.custom;

import java.util.List;

import com.westerosblocks.block.ModBlock;
import com.westerosblocks.block.ModBlockFactory;
import com.westerosblocks.block.ModBlockLifecycle;
import com.westerosblocks.block.ModBlocks;
import com.westerosblocks.particle.ModParticles;

import net.minecraft.block.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class WCParticleEmitterBlock extends Block implements ModBlockLifecycle, Waterloggable {
    protected static final VoxelShape SHAPE = Block.createCuboidShape(4.0D, 4.0D, 4.0D, 12.0D, 12.0D, 12.0D);
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    private final ModBlock def;

    public static class Factory extends ModBlockFactory {
        @Override
        public Block buildBlockClass(ModBlock def) {
            AbstractBlock.Settings settings = def.applyCustomProperties()
                    .noCollision()
                    .strength(-1.0F, 3600000.0F)
                    .sounds(BlockSoundGroup.AMETHYST_BLOCK);

            Block blk = new WCParticleEmitterBlock(settings, def);
            return def.registerRenderType(ModBlocks.registerBlock(def.blockName, blk), false, true);
        }
    }

    public WCParticleEmitterBlock(Settings settings, ModBlock def) {
        super(settings);
        this.def = def;
        this.setDefaultState(this.getDefaultState().with(WATERLOGGED, false));
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (def.particle != null) {
            ParticleEffect particle = ModParticles.get(def.particle);
            if (particle != null) {
                // Spawn particle at block center
                double x = pos.getX() + 0.5 + (random.nextFloat() - 0.5) * 0.2;
                double y = pos.getY() + 0.2 + random.nextFloat() * 0.6;
                double z = pos.getZ() + 0.5 + (random.nextFloat() - 0.5) * 0.2;

                world.addParticle(particle, x, y, z, 0.0D, 0.0D, 0.00);
            }
        }
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.empty();
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
        return this.getDefaultState().with(WATERLOGGED, fluidState.isIn(FluidTags.WATER));
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
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
        addCustomTooltip(tooltip);
        super.appendTooltip(stack, context, tooltip, options);
    }

    @Override
    public ModBlock getWBDefinition() {
        return def;
    }

    private static final String[] TAGS = {};

    @Override
    public String[] getBlockTags() {
        return TAGS;
    }

}
