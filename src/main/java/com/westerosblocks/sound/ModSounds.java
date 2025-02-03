package com.westerosblocks.sound;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.WesterosBlocksJsonLoader;
import com.westerosblocks.block.WesterosBlockDef;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.List;

public class ModSounds {
    private static HashMap<String, SoundEvent> REGISTERED_SOUNDS = new HashMap<>();
    public static WesterosBlockDef[] customBlockDefs = WesterosBlocksJsonLoader.getCustomBlockDefs();

    public static void registerSoundEvent(String name) {
        SoundEvent event = REGISTERED_SOUNDS.get(name);
        if (event == null) {
            Identifier id = WesterosBlocks.id(name);
            SoundEvent soundEvent = SoundEvent.of(id);
            SoundEvent registered = Registry.register(Registries.SOUND_EVENT, id, soundEvent);
            REGISTERED_SOUNDS.put(name, registered);
        }
    }

    public static void registerSounds() {
        for (WesterosBlockDef customBlockDef : customBlockDefs) {
            if (customBlockDef == null) continue;
            registerBlockSoundEvents(customBlockDef.soundList);
        }
        WesterosBlocks.LOGGER.info("Registering Mod Sounds for " + WesterosBlocks.MOD_ID);
    }

    public static void registerBlockSoundEvents(List<String> soundList) {
        if (soundList != null) {
            for (String snd : soundList) {
                ModSounds.registerSoundEvent(snd);
            }
        }
    }

    public static SoundEvent getRegisteredSound(String soundName) {
        return REGISTERED_SOUNDS.get(soundName);
    }

}
