package com.westerosblocks.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.MapColor;
import net.minecraft.sound.BlockSoundGroup;

import java.util.HashMap;
import java.util.Map;

public class WesterosBlockSettings {
    private static final Map<String, AbstractBlock.Settings> BASE_SETTINGS = new HashMap<>();

    static {
        // Rock/Stone base settings
        BASE_SETTINGS.put("rock", AbstractBlock.Settings.create()
                .strength(1.5F)  // Default strength
                .requiresTool()
                .sounds(BlockSoundGroup.STONE)
                .mapColor(MapColor.LIGHT_GRAY));

        BASE_SETTINGS.put("air", AbstractBlock.Settings.create()
                .noCollision()
                .dropsNothing()
                .air());

        BASE_SETTINGS.put("grass", AbstractBlock.Settings.create()
                .strength(0.6F)
                .sounds(BlockSoundGroup.GRASS)
                .mapColor(MapColor.GREEN));

        BASE_SETTINGS.put("ground", AbstractBlock.Settings.create()
                .strength(0.5F)
                .sounds(BlockSoundGroup.GRAVEL)
                .mapColor(MapColor.DIRT_BROWN));

        BASE_SETTINGS.put("wood", AbstractBlock.Settings.create()
                .strength(2.0F)
                .sounds(BlockSoundGroup.WOOD)
                .burnable()
                .mapColor(MapColor.OAK_TAN));

        BASE_SETTINGS.put("iron", AbstractBlock.Settings.create()
                .strength(5.0F)
                .requiresTool()
                .sounds(BlockSoundGroup.METAL)
                .mapColor(MapColor.IRON_GRAY));

        BASE_SETTINGS.put("water", AbstractBlock.Settings.create()
                .noCollision()
                .dropsNothing()
                .liquid());

        BASE_SETTINGS.put("lava", AbstractBlock.Settings.create()
                .noCollision()
                .dropsNothing()
                .liquid()
                .luminance(state -> 15));

        BASE_SETTINGS.put("leaves", AbstractBlock.Settings.create()
                .strength(0.2F)
                .sounds(BlockSoundGroup.GRASS)
                .nonOpaque()
                .burnable()
                .ticksRandomly()
                .suffocates((state, world, pos) -> false)
                .blockVision((state, world, pos) -> false));

        BASE_SETTINGS.put("glass", AbstractBlock.Settings.create()
                .strength(0.3F)
                .sounds(BlockSoundGroup.GLASS)
                .nonOpaque()
                .allowsSpawning((state, world, pos, type) -> false));

        BASE_SETTINGS.put("ice", AbstractBlock.Settings.create()
                .strength(0.5F)
                .slipperiness(0.98F)
                .sounds(BlockSoundGroup.GLASS)
                .nonOpaque());

        BASE_SETTINGS.put("snow", AbstractBlock.Settings.create()
                .strength(0.1F)
                .sounds(BlockSoundGroup.SNOW)
                .mapColor(MapColor.WHITE));

    }

    public static AbstractBlock.Settings get(String material) {
        return BASE_SETTINGS.getOrDefault(material,
                AbstractBlock.Settings.create());
    }
}
