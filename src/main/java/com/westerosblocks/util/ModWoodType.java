package com.westerosblocks.util;

import net.minecraft.block.WoodType;

public class ModWoodType {
    public static final WoodType WEIRWOOD = new WoodType("weirwood", ModBlockSetType.WEIRWOOD);

    /**
     * Gets the WoodType for a block based on its definition and settings.
     * This method can be used by any block that needs to determine its wood type,
     * such as fence gates, doors, etc.
     */
    public static WoodType getWoodType(String woodtype) {
        if (woodtype != null) {
            woodtype = woodtype.toLowerCase();

            return switch (woodtype) {
                case "oak" -> WoodType.OAK;
                case "spruce" -> WoodType.SPRUCE;
                case "birch" -> WoodType.BIRCH;
                case "jungle" -> WoodType.JUNGLE;
                case "acacia" -> WoodType.ACACIA;
                case "dark_oak" -> WoodType.DARK_OAK;
                case "crimson" -> WoodType.CRIMSON;
                case "warped" -> WoodType.WARPED;
                case "cherry" -> WoodType.CHERRY;
                case "mangrove" -> WoodType.MANGROVE;
                case "bamboo" -> WoodType.BAMBOO;
                case "weirwood" -> WEIRWOOD;
                default -> WoodType.OAK;
            };
        }
        return WoodType.OAK;
    }
}
