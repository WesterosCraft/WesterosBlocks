package com.westerosblocks.particle.custom;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;

@Environment(EnvType.CLIENT)
public class WildfireParticle extends SpriteBillboardParticle {
    private final SpriteProvider spriteProvider;

    protected WildfireParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
        super(world, x, y, z, velocityX, velocityY, velocityZ);
        this.spriteProvider = spriteProvider;
        this.red = 0.3f;
        this.green = 0.8f;
        this.blue = 0.3f;
        this.alpha = 0.8f;
        this.scale *= 0.8F;
        this.maxAge = 20 + this.random.nextInt(10);
        this.velocityMultiplier = 0.9F;
        this.setSpriteForAge(spriteProvider);
    }

    @Override
    public void tick() {
        super.tick();
        float lifeProgress = (float) this.age / (float) this.maxAge;
        this.alpha = 0.8F * (1.0F - lifeProgress);
        this.setSpriteForAge(spriteProvider);
        this.velocityX += (this.random.nextFloat() - 0.5) * 0.05;
        this.velocityZ += (this.random.nextFloat() - 0.5) * 0.05;
        if (this.alpha < 0.01F) this.markDead();
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
                                       double x, double y, double z, double velX, double velY, double velZ) {
            return new WildfireParticle(world, x, y, z, velX, velY, velZ, this.spriteProvider);
        }
    }
}