package com.westerosblocks.util;

import net.minecraft.block.BlockSetType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvents;

public class ModBlockSetType {

    public static final BlockSetType WEIRWOOD =
            // todo customize these as needed in the future
            new BlockSetType(
                    "weirwood",
                    true,
                    true,
                    true,
                    BlockSetType.ActivationRule.EVERYTHING,
                    BlockSoundGroup.NETHER_WOOD,
                    SoundEvents.BLOCK_NETHER_WOOD_DOOR_CLOSE,
                    SoundEvents.BLOCK_NETHER_WOOD_DOOR_OPEN,
                    SoundEvents.BLOCK_NETHER_WOOD_TRAPDOOR_CLOSE,
                    SoundEvents.BLOCK_NETHER_WOOD_TRAPDOOR_OPEN,
                    SoundEvents.BLOCK_NETHER_WOOD_PRESSURE_PLATE_CLICK_OFF,
                    SoundEvents.BLOCK_NETHER_WOOD_PRESSURE_PLATE_CLICK_ON,
                    SoundEvents.BLOCK_NETHER_WOOD_BUTTON_CLICK_OFF,
                    SoundEvents.BLOCK_NETHER_WOOD_BUTTON_CLICK_ON
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
