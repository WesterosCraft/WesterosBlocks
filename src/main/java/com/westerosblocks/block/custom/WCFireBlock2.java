package com.westerosblocks.block.custom;

import com.westerosblocks.particle.ModParticles;
import net.minecraft.block.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import java.util.List;

/**
 * WesterosBlocks Fire Block for the builder system.
 * Custom fire block with controlled spread and particle effects.
 */
public class WCFireBlock2 extends FireBlock {
    private final String particleType;
    private final List<String> tooltips;

    /**
     * Creates a new WCFireBlock2 with the specified settings and particle type.
     * 
     * @param settings The block settings
     * @param particleType The particle type to spawn (can be null)
     * @param tooltips List of tooltip strings (can be null)
     */
    public WCFireBlock2(AbstractBlock.Settings settings, String particleType, List<String> tooltips) {
        super(settings);
        this.particleType = particleType;
        this.tooltips = tooltips;
    }

    // Prevents fire from being replaced by other fire blocks
    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return state;
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return true;
    }

    // Prevents vanilla fire spread mechanics
    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        // No-op to prevent spread
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        // Keep the original fire ambient sound
        if (random.nextInt(24) == 0) {
            world.playSound(
                    pos.getX() + 0.5,
                    pos.getY() + 0.5,
                    pos.getZ() + 0.5,
                    SoundEvents.BLOCK_FIRE_AMBIENT,
                    SoundCategory.BLOCKS,
                    1.0F + random.nextFloat(),
                    random.nextFloat() * 0.7F + 0.3F,
                    false
            );
        }

        // Spawn custom particles if specified
        if (particleType != null) {
            ParticleEffect particle = ModParticles.get(particleType);
            if (particle != null && random.nextFloat() < 0.7f) {
                double x = pos.getX() + 0.5 + (random.nextFloat() - 0.5) * 0.2;
                double y = pos.getY() + 0.2 + random.nextFloat() * 0.6;
                double z = pos.getZ() + 0.5 + (random.nextFloat() - 0.5) * 0.2;

                double velocityX = (random.nextFloat() - 0.5) * 0.02;
                double velocityY = 0.05 + random.nextFloat() * 0.02;
                double velocityZ = (random.nextFloat() - 0.5) * 0.02;

                world.addParticle(particle, x, y, z, velocityX, velocityY, velocityZ);
            }
        }
    }

    @Override
    protected boolean isFlammable(BlockState state) {
        return false;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return BASE_SHAPE;
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!oldState.isOf(this)) {
            super.onBlockAdded(state, world, pos, oldState, notify);
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
        if (tooltips != null) {
            for (String tooltipText : tooltips) {
                tooltip.add(Text.translatable(tooltipText));
            }
        }
        super.appendTooltip(stack, context, tooltip, options);
    }
} 