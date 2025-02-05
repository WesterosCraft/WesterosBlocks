package com.westerosblocks.particle;

import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.world.World;

public class ModParticleEffect {
    private ParticleEffect particleEffect;

    public ModParticleEffect(String hexColor) {
        // Parse hex color to RGB (0-1 range)
        int rgb = Integer.parseInt(hexColor.replace("#", ""), 16);
        float red = ((rgb >> 16) & 0xFF) / 255.0f;
        float green = ((rgb >> 8) & 0xFF) / 255.0f;
        float blue = (rgb & 0xFF) / 255.0f;

        this.particleEffect = new DustParticleEffect(
                new org.joml.Vector3f(red, green, blue),
                1.0f // Default scale
        );
    }

    public void spawn(World world, double x, double y, double z) {
        world.addParticle(particleEffect, x, y, z, 0.0, 0.0, 0.0);
    }
}