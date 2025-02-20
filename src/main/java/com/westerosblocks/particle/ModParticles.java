package com.westerosblocks.particle;

import com.westerosblocks.WesterosBlocks;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.particle.WaterSplashParticle;
import net.minecraft.particle.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import java.util.HashMap;
import java.util.Map;

public class ModParticles {
    private static final Map<String, ParticleEffect> PARTICLE_EFFECTS = new HashMap<>();
    public static final SimpleParticleType WILDFIRE = FabricParticleTypes.simple(false);
    public static final SimpleParticleType CASCADE = FabricParticleTypes.simple(true);
    public static final SimpleParticleType MIST = FabricParticleTypes.simple(true);

    public static void initialize() {
        PARTICLE_EFFECTS.put("hugeexplosion", ParticleTypes.EXPLOSION);
        PARTICLE_EFFECTS.put("fireworksSpark", ParticleTypes.FIREWORK);
        PARTICLE_EFFECTS.put("bubble", ParticleTypes.BUBBLE);
        PARTICLE_EFFECTS.put("suspended", ParticleTypes.UNDERWATER);
        PARTICLE_EFFECTS.put("depthsuspend", ParticleTypes.UNDERWATER);
        PARTICLE_EFFECTS.put("crit", ParticleTypes.CRIT);
        PARTICLE_EFFECTS.put("magicCrit", ParticleTypes.CRIT);
        PARTICLE_EFFECTS.put("smoke", ParticleTypes.SMOKE);
        PARTICLE_EFFECTS.put("mobSpell", ParticleTypes.ENCHANT);
        PARTICLE_EFFECTS.put("mobSpellAmbient", ParticleTypes.ENCHANT);
        PARTICLE_EFFECTS.put("spell", ParticleTypes.ENCHANT);
        PARTICLE_EFFECTS.put("instantSpell", ParticleTypes.INSTANT_EFFECT);
        PARTICLE_EFFECTS.put("witchMagic", ParticleTypes.WITCH);
        PARTICLE_EFFECTS.put("note", ParticleTypes.NOTE);
        PARTICLE_EFFECTS.put("portal", ParticleTypes.PORTAL);
        PARTICLE_EFFECTS.put("enchantmenttable", ParticleTypes.POOF);
        PARTICLE_EFFECTS.put("flame", ParticleTypes.FLAME);
        PARTICLE_EFFECTS.put("lava", ParticleTypes.LAVA);
        PARTICLE_EFFECTS.put("splash", ParticleTypes.SPLASH);
        PARTICLE_EFFECTS.put("largesmoke", ParticleTypes.LARGE_SMOKE);
        PARTICLE_EFFECTS.put("cloud", ParticleTypes.CLOUD);
        PARTICLE_EFFECTS.put("snowballpoof", ParticleTypes.POOF);
        PARTICLE_EFFECTS.put("dripWater", ParticleTypes.DRIPPING_WATER);
        PARTICLE_EFFECTS.put("dripLava", ParticleTypes.DRIPPING_LAVA);
        PARTICLE_EFFECTS.put("snowshovel", ParticleTypes.ITEM_SNOWBALL);
        PARTICLE_EFFECTS.put("slime", ParticleTypes.ITEM_SLIME);
        PARTICLE_EFFECTS.put("heart", ParticleTypes.HEART);
        PARTICLE_EFFECTS.put("angryVillager", ParticleTypes.ANGRY_VILLAGER);
        PARTICLE_EFFECTS.put("happyVillager", ParticleTypes.HAPPY_VILLAGER);
        PARTICLE_EFFECTS.put("soul_flame", ParticleTypes.SOUL_FIRE_FLAME);

        registerParticle("wildfire", WILDFIRE);
        registerParticle("cascade", CASCADE);
        registerParticle("mist", MIST);
    }

    public static void initializeClient() {
        ParticleFactoryRegistry.getInstance().register(WILDFIRE, FlameParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(CASCADE, WaterSplashParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(MIST, WaterSplashParticle.Factory::new);
    }

    private static void registerParticle(String name, SimpleParticleType particleType) {
        Registry.register(Registries.PARTICLE_TYPE, WesterosBlocks.id(name), particleType);
        PARTICLE_EFFECTS.put(name, particleType);
    }

    public static ParticleEffect get(String name) {
        return PARTICLE_EFFECTS.get(name);
    }
}
