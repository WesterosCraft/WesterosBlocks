package com.westerosblocks.sound;

import com.westerosblocks.WesterosBlocks;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {
    /**
     * Maps string sound names to BlockSoundGroup instances
     */
    public static BlockSoundGroup getSoundGroupFromString(String soundName) {
        if (soundName == null) return BlockSoundGroup.STONE;

        return switch (soundName.toLowerCase()) {
            case "wood" -> BlockSoundGroup.WOOD;
            case "stone" -> BlockSoundGroup.STONE;
            case "metal" -> BlockSoundGroup.METAL;
            case "grass" -> BlockSoundGroup.GRASS;
            case "wool", "cloth" -> BlockSoundGroup.WOOL;
            case "gravel" -> BlockSoundGroup.GRAVEL;
            case "glass" -> BlockSoundGroup.GLASS;
            case "candle" -> BlockSoundGroup.CANDLE;
            case "bone" -> BlockSoundGroup.BONE;
            case "ladder" -> BlockSoundGroup.LADDER;
            case "crop", "plant" -> BlockSoundGroup.CROP;
            case "snow" -> BlockSoundGroup.SNOW;
            case "chain" -> BlockSoundGroup.CHAIN;
            case "powder_snow", "powder" -> BlockSoundGroup.POWDER_SNOW;
            case "mud" -> BlockSoundGroup.MUD;
            case "packed_mud" -> BlockSoundGroup.PACKED_MUD;
            case "sand" -> BlockSoundGroup.SAND;
            case "vine" -> BlockSoundGroup.VINE;
            case "fungus" -> BlockSoundGroup.FUNGUS;
            case "rooted_dirt" -> BlockSoundGroup.ROOTED_DIRT;
            case "scaffolding" -> BlockSoundGroup.SCAFFOLDING;
            case "lantern" -> BlockSoundGroup.LANTERN;
            case "coral" -> BlockSoundGroup.CORAL;
            case "wet_grass" -> BlockSoundGroup.WET_GRASS;
            case "moss_carpet" -> BlockSoundGroup.MOSS_CARPET;
            case "tuff" -> BlockSoundGroup.TUFF;
            case "pot" -> BlockSoundGroup.DECORATED_POT;
            default -> {
                WesterosBlocks.LOGGER.warn("Unknown sound type '{}', defaulting to STONE", soundName);
                yield BlockSoundGroup.STONE;
            }
        };
    }

    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = Identifier.of(WesterosBlocks.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void registerSounds() {
        registerSoundEvent("cascade");
        WesterosBlocks.LOGGER.info("Registering Mod Sounds for " + WesterosBlocks.MOD_ID);
    }
}
