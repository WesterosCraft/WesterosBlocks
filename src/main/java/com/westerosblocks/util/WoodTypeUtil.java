package com.westerosblocks.util;

import com.westerosblocks.block.WesterosBlockDef;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockSetType;
import net.minecraft.block.WoodType;

public class WoodTypeUtil {
    /**
     * Gets the WoodType for a block based on its definition and settings.
     * This method can be used by any block that needs to determine its wood type,
     * such as fence gates, doors, etc.
     *
     * @param woodtype The Westeros block definition
     * @return The appropriate WoodType, defaults to OAK if not specified
     */
    public static WoodType getWoodType(String woodtype) {
        if (woodtype != null) {
            woodtype = woodtype.toLowerCase();

            // Map the material to corresponding WoodType
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
                default -> WoodType.OAK;
            };
        }
        return WoodType.OAK;
    }

    /**
     * Handles custom wood types that aren't part of vanilla Minecraft.
     * This method can be extended to support mod-specific wood types.
     *
     * @param woodType The custom wood type identifier
     * @return The appropriate WoodType, defaults to OAK if not recognized
     */
    private static WoodType getCustomWoodType(String woodType) {
        // Add custom wood type handling here if needed
        // For now, return OAK as default
        return WoodType.OAK;
    }

    public static BlockSetType getBlockSetType(AbstractBlock.Settings settings, WesterosBlockDef def) {
        //TODO: implment
        return BlockSetType.OAK;
    }
}
