package com.westerosblocks.sound;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.WesterosBlockDef;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

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
}
