package com.westerosblocks.particle.custom;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;

@Environment(EnvType.CLIENT)
public class WildfireParticle extends SpriteBillboardParticle {
    private final SpriteProvider spriteProvider;
    private float baseScale;

    protected WildfireParticle(ClientWorld world, double x, double y, double z,
                               double velocityX, double velocityY, double velocityZ,
                               SpriteProvider spriteProvider) {
        super(world, x, y, z, velocityX, velocityY, velocityZ);

        this.spriteProvider = spriteProvider;
        this.maxAge = 15 + this.random.nextInt(10);
        this.baseScale = 0.5F + this.random.nextFloat() * 0.3F;
        this.scale = this.baseScale;

        // Set green flame colors
        this.red = 0.2F;
        this.green = 0.8F + this.random.nextFloat() * 0.2F;
        this.blue = 0.3F;
        this.alpha = 0.8F;

        // Set initial velocities
        this.velocityX = velocityX * 0.1D + (this.random.nextGaussian() * 0.02D);
        this.velocityY = velocityY * 0.1D + 0.03D + (this.random.nextFloat() * 0.02D);
        this.velocityZ = velocityZ * 0.1D + (this.random.nextGaussian() * 0.02D);

        this.setSpriteForAge(spriteProvider);
    }

    @Override
    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;

        if (this.age++ >= this.maxAge) {
            this.markDead();
            return;
        }

        // More volatile movement
        this.velocityX *= 1.02D;
        this.velocityY *= 0.98D;
        this.velocityZ *= 1.02D;

        // Add sine wave motion for more chaotic movement
        double ageOffset = this.age / 3.0;
        this.velocityX += Math.sin(ageOffset) * 0.01D;
        this.velocityZ += Math.cos(ageOffset) * 0.01D;

        this.move(this.velocityX, this.velocityY, this.velocityZ);

        // Update appearance
        float lifeProgress = (float)this.age / (float)this.maxAge;
        this.alpha = Math.max(0, 0.8F - lifeProgress * 0.8F);
        this.scale = this.baseScale * (1.0F + lifeProgress * 0.5F);

        this.setSpriteForAge(this.spriteProvider);
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientWorld world,
                                       double x, double y, double z,
                                       double velX, double velY, double velZ) {
            return new WildfireParticle(world, x, y, z, velX, velY, velZ, this.spriteProvider);
        }
    }
}