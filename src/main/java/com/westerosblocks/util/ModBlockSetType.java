package com.westerosblocks.util;

import net.minecraft.block.BlockSetType;
import net.minecraft.sound.SoundEvents;

public class ModBlockSetType {

    public static final BlockSetType WEIRWOOD =
            new BlockSetType(
                    "weirwood",
                    true,
                    true,
                    true,
                    BlockSetType.ActivationRule.EVERYTHING,
                    ModBlockSoundGroup.WEIRWOOD,
                    SoundEvents.BLOCK_WOODEN_DOOR_CLOSE,
                    SoundEvents.BLOCK_WOODEN_DOOR_OPEN,
                    SoundEvents.BLOCK_WOODEN_TRAPDOOR_CLOSE,
                    SoundEvents.BLOCK_WOODEN_TRAPDOOR_OPEN,
                    SoundEvents.BLOCK_WOODEN_PRESSURE_PLATE_CLICK_OFF,
                    SoundEvents.BLOCK_WOODEN_PRESSURE_PLATE_CLICK_ON,
                    SoundEvents.BLOCK_WOODEN_BUTTON_CLICK_OFF,
                    SoundEvents.BLOCK_WOODEN_BUTTON_CLICK_ON
            );


    public static BlockSetType getBlockSetType(String blockSetType) {
        if (blockSetType != null) {
            blockSetType = blockSetType.toLowerCase();

            return switch (blockSetType) {
                case "oak" -> BlockSetType.OAK;
                case "spruce" -> BlockSetType.SPRUCE;
                case "birch" -> BlockSetType.BIRCH;
                case "jungle" -> BlockSetType.JUNGLE;
                case "acacia" -> BlockSetType.ACACIA;
                case "dark_oak" -> BlockSetType.DARK_OAK;
                case "crimson" -> BlockSetType.CRIMSON;
                case "warped" -> BlockSetType.WARPED;
                case "cherry" -> BlockSetType.CHERRY;
                case "mangrove" -> BlockSetType.MANGROVE;
                case "bamboo" -> BlockSetType.BAMBOO;
                case "weirwood" -> WEIRWOOD;
                default -> BlockSetType.OAK;
            };
        }

        return BlockSetType.OAK;
    }
}
