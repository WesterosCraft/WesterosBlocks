package com.westerosblocks.util;

import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvents;

public class ModBlockSoundGroup {
    public static final BlockSoundGroup WEIRWOOD = new BlockSoundGroup(
            1.0f, // volume
            1.0f, // pitch
            SoundEvents.BLOCK_WOOD_BREAK,    // break sound
            SoundEvents.BLOCK_WOOD_STEP,     // step sound
            SoundEvents.BLOCK_WOOD_PLACE,    // place sound
            SoundEvents.BLOCK_WOOD_HIT,      // hit sound
            SoundEvents.BLOCK_WOOD_FALL      // fall sound
    );

    public static final BlockSoundGroup METAL = new BlockSoundGroup(
            1.0f, // volume
            1.0f, // pitch
            SoundEvents.BLOCK_METAL_BREAK,    // break sound
            SoundEvents.BLOCK_METAL_STEP,     // step sound
            SoundEvents.BLOCK_METAL_PLACE,    // place sound
            SoundEvents.BLOCK_METAL_HIT,      // hit sound
            SoundEvents.BLOCK_METAL_FALL      // fall sound
    );

    public static BlockSoundGroup getBlockSoundGroup(String soundGroup) {
        if (soundGroup != null) {
            soundGroup = soundGroup.toLowerCase();

            return switch (soundGroup) {
                case "weirwood" -> WEIRWOOD;
                case "metal" -> METAL;
                default -> BlockSoundGroup.WOOD; // Default to wood sounds if not found
            };
        }

        return BlockSoundGroup.WOOD;
    }
}