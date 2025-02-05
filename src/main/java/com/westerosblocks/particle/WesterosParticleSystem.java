package com.westerosblocks.particle;

import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class WesterosParticleSystem {
    public static void spawnFireParticles(World world, BlockPos pos, Random random, String particleType) {
        if (random.nextFloat() < 0.7f) {
            double x = pos.getX() + 0.5 + (random.nextFloat() - 0.5) * 0.2;
            double y = pos.getY() + 0.2 + random.nextFloat() * 0.6;
            double z = pos.getZ() + 0.5 + (random.nextFloat() - 0.5) * 0.2;

            ParticleEffect particle = ModParticles.getParticle(particleType);
            world.addParticle(particle, x, y, z, 0, 0.05, 0);
        }
    }
}
