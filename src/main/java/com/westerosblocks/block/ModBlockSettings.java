package com.westerosblocks.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.sound.BlockSoundGroup;

import java.util.HashMap;
import java.util.Map;

public class ModBlockSettings {
    private static final Map<String, AbstractBlock.Settings> BASE_SETTINGS = new HashMap<>();

    static {
        BASE_SETTINGS.put("rock", AbstractBlock.Settings.create()
                .strength(1.5F)
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

        BASE_SETTINGS.put("plants", AbstractBlock.Settings.create()
                .noCollision()
                .breakInstantly()
                .sounds(BlockSoundGroup.GRASS)
                .nonOpaque());

        BASE_SETTINGS.put("vine", AbstractBlock.Settings.create()
                .noCollision()
                .ticksRandomly()
                .strength(0.2F)
                .sounds(BlockSoundGroup.GRASS)
                .nonOpaque());

        BASE_SETTINGS.put("sponge", AbstractBlock.Settings.create()
                .strength(0.6F)
                .sounds(BlockSoundGroup.GRASS));

        BASE_SETTINGS.put("cloth", AbstractBlock.Settings.create()
                .strength(0.8F)
                .sounds(BlockSoundGroup.WOOL)
                .burnable());

        BASE_SETTINGS.put("fire", AbstractBlock.Settings.create()
                .noCollision()
                .breakInstantly()
                .luminance(state -> 15)
                .sounds(BlockSoundGroup.WOOL));

        BASE_SETTINGS.put("sand", AbstractBlock.Settings.create()
                .strength(0.5F)
                .sounds(BlockSoundGroup.SAND)
                .mapColor(MapColor.PALE_YELLOW));

        BASE_SETTINGS.put("tnt", AbstractBlock.Settings.create()
                .breakInstantly()
                .sounds(BlockSoundGroup.GRASS)
                .burnable());

        BASE_SETTINGS.put("coral", AbstractBlock.Settings.create()
                .strength(1.5F)
                .requiresTool()
                .sounds(BlockSoundGroup.STONE));

        BASE_SETTINGS.put("cactus", AbstractBlock.Settings.create()
                .strength(0.4F)
                .sounds(BlockSoundGroup.WOOL)
                .nonOpaque());

        BASE_SETTINGS.put("clay", AbstractBlock.Settings.create()
                .strength(0.6F)
                .sounds(BlockSoundGroup.GRAVEL)
                .mapColor(MapColor.LIGHT_BLUE));

        BASE_SETTINGS.put("portal", AbstractBlock.Settings.create()
                .noCollision()
                .strength(-1.0F)
                .sounds(BlockSoundGroup.GLASS)
                .luminance(state -> 11));

        BASE_SETTINGS.put("cake", AbstractBlock.Settings.create()
                .strength(0.5F)
                .sounds(BlockSoundGroup.WOOL)
                .nonOpaque());

        BASE_SETTINGS.put("web", AbstractBlock.Settings.create()
                .noCollision()
                .requiresTool()
                .strength(4.0F)
                .nonOpaque());

        BASE_SETTINGS.put("piston", AbstractBlock.Settings.create()
                .strength(1.5F)
                .sounds(BlockSoundGroup.STONE)
                .pistonBehavior(PistonBehavior.BLOCK));

        BASE_SETTINGS.put("decoration", AbstractBlock.Settings.create()
                .noCollision()
                .strength(0.0F)
                .sounds(BlockSoundGroup.STONE)
                .nonOpaque());
    }

    public static AbstractBlock.Settings get(String material) {
        return BASE_SETTINGS.getOrDefault(material,
                AbstractBlock.Settings.create());
    }
}
