package com.westerosblocks.particle;

import com.westerosblocks.WesterosBlocks;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.client.particle.CampfireSmokeParticle;
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
    public static final SimpleParticleType COSY_SMOKE = FabricParticleTypes.simple(true);
    public static final SimpleParticleType SIGNAL_SMOKE = FabricParticleTypes.simple(true);

    public static void initialize() {
        PARTICLE_EFFECTS.put("huge_explosion", ParticleTypes.EXPLOSION);
        PARTICLE_EFFECTS.put("fireworksSpark", ParticleTypes.FIREWORK);
        PARTICLE_EFFECTS.put("bubble", ParticleTypes.BUBBLE);
        PARTICLE_EFFECTS.put("suspended", ParticleTypes.UNDERWATER);
        PARTICLE_EFFECTS.put("depth_suspend", ParticleTypes.UNDERWATER);
        PARTICLE_EFFECTS.put("crit", ParticleTypes.CRIT);
        PARTICLE_EFFECTS.put("magic_crit", ParticleTypes.CRIT);
        PARTICLE_EFFECTS.put("smoke", ParticleTypes.SMOKE);
        PARTICLE_EFFECTS.put("mobSpell", ParticleTypes.ENCHANT);
        PARTICLE_EFFECTS.put("mobSpellAmbient", ParticleTypes.ENCHANT);
        PARTICLE_EFFECTS.put("spell", ParticleTypes.ENCHANT);
        PARTICLE_EFFECTS.put("instantSpell", ParticleTypes.INSTANT_EFFECT);
        PARTICLE_EFFECTS.put("witchMagic", ParticleTypes.WITCH);
        PARTICLE_EFFECTS.put("note", ParticleTypes.NOTE);
        PARTICLE_EFFECTS.put("portal", ParticleTypes.PORTAL);
        PARTICLE_EFFECTS.put("enchantment_table", ParticleTypes.POOF);
        PARTICLE_EFFECTS.put("flame", ParticleTypes.FLAME);
        PARTICLE_EFFECTS.put("lava", ParticleTypes.LAVA);
        PARTICLE_EFFECTS.put("splash", ParticleTypes.SPLASH);
        PARTICLE_EFFECTS.put("large_smoke", ParticleTypes.LARGE_SMOKE);
        PARTICLE_EFFECTS.put("cloud", ParticleTypes.CLOUD);
        PARTICLE_EFFECTS.put("snowball_poof", ParticleTypes.POOF);
        PARTICLE_EFFECTS.put("dripWater", ParticleTypes.DRIPPING_WATER);
        PARTICLE_EFFECTS.put("dripLava", ParticleTypes.DRIPPING_LAVA);
        PARTICLE_EFFECTS.put("snowball", ParticleTypes.ITEM_SNOWBALL);
        PARTICLE_EFFECTS.put("slime", ParticleTypes.ITEM_SLIME);
        PARTICLE_EFFECTS.put("heart", ParticleTypes.HEART);
        PARTICLE_EFFECTS.put("angry_villager", ParticleTypes.ANGRY_VILLAGER);
        PARTICLE_EFFECTS.put("happy_villager", ParticleTypes.HAPPY_VILLAGER);
        PARTICLE_EFFECTS.put("soul_flame", ParticleTypes.SOUL_FIRE_FLAME);
        PARTICLE_EFFECTS.put("campfire_cosy_smoke", ParticleTypes.CAMPFIRE_COSY_SMOKE);
        PARTICLE_EFFECTS.put("campfire_signal_smoke", ParticleTypes.CAMPFIRE_SIGNAL_SMOKE);

        registerParticle("signal_smoke", SIGNAL_SMOKE);
        registerParticle("cosy_smoke", COSY_SMOKE);
        registerParticle("wildfire", WILDFIRE);
        registerParticle("cascade", CASCADE);
    }

    public static void initializeClient() {
        ParticleFactoryRegistry.getInstance().register(WILDFIRE, FlameParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(CASCADE, WaterSplashParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(COSY_SMOKE, CampfireSmokeParticle.CosySmokeFactory::new);
        ParticleFactoryRegistry.getInstance().register(SIGNAL_SMOKE, CampfireSmokeParticle.SignalSmokeFactory::new);
    }

    private static void registerParticle(String name, SimpleParticleType particleType) {
        Registry.register(Registries.PARTICLE_TYPE, WesterosBlocks.id(name), particleType);
        PARTICLE_EFFECTS.put(name, particleType);
    }

    public static ParticleEffect get(String name) {
        return PARTICLE_EFFECTS.get(name);
    }
}
