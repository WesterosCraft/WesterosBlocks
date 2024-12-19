package com.westerosblocks.sound;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.WesterosBlockDef;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.HashMap;

public class ModSounds {

    public static void registerSoundEvent(String name) {
        Identifier id = Identifier.of(WesterosBlocks.MOD_ID, name);
        Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void registerSounds(WesterosBlockDef[] customBlockDefs) {
        for (WesterosBlockDef customBlockDef : customBlockDefs) {
            if (customBlockDef == null)
                continue;
            // Register sound events
            customBlockDef.registerBlockSoundEvents(customBlockDef.soundList);
        }
        WesterosBlocks.LOGGER.info("Registering Mod Sounds for " + WesterosBlocks.MOD_ID);
    }

    private static HashMap<String, SoundEvent> registered_sounds = new HashMap<String, SoundEvent>();

    public static SoundEvent registerSound(String soundName, RegisterEvent.RegisterHelper<SoundEvent> helper) {
        SoundEvent event = registered_sounds.get(soundName);
        if (event == null) {
            ResourceLocation location = ResourceLocation.fromNamespaceAndPath(MOD_ID, soundName);
            event = SoundEvent.createVariableRangeEvent(location);

//            SOUND_EVENTS.register(soundName, () -> finalEvent);
            helper.register(location, event);
            registered_sounds.put(soundName, event);
        }
        return event;
    }

    public static SoundEvent getRegisteredSound(String soundName) {
        return registered_sounds.get(soundName);
    }

}
