package com.westerosblocks.util;

import com.westerosblocks.block.ModBlock;
import net.minecraft.block.AbstractBlock;
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


    public static BlockSetType getBlockSetType(AbstractBlock.Settings settings, ModBlock def) {
        //TODO: implment
        return BlockSetType.OAK;
    }
}
