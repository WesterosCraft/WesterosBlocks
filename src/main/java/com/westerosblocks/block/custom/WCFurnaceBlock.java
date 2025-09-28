package com.westerosblocks.block.custom;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FurnaceBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.Map;

public class WCFurnaceBlock extends FurnaceBlock {
    private final boolean alwaysOn;

    public static class Factory extends BlockFactory {
        @Override
        public Block buildBlockClass(AbstractBlock.Settings settings, Object... params) {
            if (params.length > 0 && params[0] instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> paramMap = (Map<String, Object>) params[0];
                boolean alwaysOn = (Boolean) paramMap.getOrDefault("alwaysOn", false);
                return new WCFurnaceBlock(settings, alwaysOn);
            }
            // Fallback for legacy parameter style
            boolean alwaysOn = params.length > 0 && params[0] instanceof Boolean ? (Boolean) params[0] : false;
            return new WCFurnaceBlock(settings, alwaysOn);
        }
    }

    protected WCFurnaceBlock(AbstractBlock.Settings settings, boolean alwaysOn) {
        super(settings);
        this.alwaysOn = alwaysOn;
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(LIT, false));
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        boolean lit = state.get(LIT);
        boolean active = alwaysOn || lit;

        if (active) {
            double d0 = (double) pos.getX() + 0.5D;
            double d1 = (double) pos.getY();
            double d2 = (double) pos.getZ() + 0.5D;

            if (random.nextDouble() < 0.1D) {
                world.playSound(d0, d1, d2, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE,
                        SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            }

            Direction direction = state.get(FACING);
            Direction.Axis axis = direction.getAxis();
            double d4 = random.nextDouble() * 0.6D - 0.3D;
            double d5 = axis == Direction.Axis.X ? (double) direction.getOffsetX() * 0.52D : d4;
            double d6 = random.nextDouble() * 6.0D / 16.0D;
            double d7 = axis == Direction.Axis.Z ? (double) direction.getOffsetZ() * 0.52D : d4;

            world.addParticle(ParticleTypes.SMOKE, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
            world.addParticle(ParticleTypes.FLAME, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    protected void openScreen(World world, BlockPos pos, PlayerEntity player) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof NamedScreenHandlerFactory) {
            player.openHandledScreen((NamedScreenHandlerFactory) blockEntity);
            player.incrementStat(Stats.INTERACT_WITH_FURNACE);
        }
    }
}
