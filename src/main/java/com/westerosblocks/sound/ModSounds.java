package com.westerosblocks.sound;

import com.westerosblocks.WesterosBlocks;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {
    public static final SoundEvent CASCADE = registerSoundEvent("cascade");

    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = Identifier.of(WesterosBlocks.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void registerSounds() {
        WesterosBlocks.LOGGER.info("Registering Mod Sounds for " + WesterosBlocks.MOD_ID);
    }
}
