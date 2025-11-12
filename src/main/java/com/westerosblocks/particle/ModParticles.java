package com.westerosblocks.particle;

import com.westerosblocks.WesterosBlocks;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;



public class ModParticles {
//    public static final SimpleParticleType WILDFIRE = FabricParticleTypes.simple(false);
//    public static final SimpleParticleType CASCADE = FabricParticleTypes.simple(true);
//    public static final SimpleParticleType COSY_SMOKE = FabricParticleTypes.simple(true);
//    public static final SimpleParticleType SIGNAL_SMOKE = FabricParticleTypes.simple(true);



    private static SimpleParticleType registerParticle(String name, SimpleParticleType particleType) {
        return Registry.register(Registries.PARTICLE_TYPE, WesterosBlocks.id(name), particleType);
    }

    public static void registerParticles() {
        WesterosBlocks.LOGGER.info("Registering Particles for " + WesterosBlocks.MOD_ID);
    }

}
