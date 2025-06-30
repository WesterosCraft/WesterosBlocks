package com.westerosblocks.util;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.MapColor;

/**
 * Utility class containing reusable components for the WesterosBlocks mod.
 * Provides a reusable Builder pattern for custom blocks.
 */
public class ModUtils {
    
    /**
     * Reusable Builder class for creating custom blocks with common properties.
     * This reduces code duplication across different block classes.
     */
    public static class BlockBuilder {
        private String blockName;
        private String creativeTab;
        private float hardness = 2.0f;
        private float resistance = 6.0f;
        private MapColor mapColor = MapColor.STONE_GRAY;
        private boolean requiresTool = true;
        private boolean nonOpaque = false;
        private int harvestLevel = 1;

        public BlockBuilder(String blockName) {
            this.blockName = blockName;
        }

        public BlockBuilder creativeTab(String creativeTab) {
            this.creativeTab = creativeTab;
            return this;
        }

        public BlockBuilder hardness(float hardness) {
            this.hardness = hardness;
            return this;
        }

        public BlockBuilder resistance(float resistance) {
            this.resistance = resistance;
            return this;
        }

        public BlockBuilder mapColor(MapColor mapColor) {
            this.mapColor = mapColor;
            return this;
        }

        public BlockBuilder requiresTool(boolean requiresTool) {
            this.requiresTool = requiresTool;
            return this;
        }

        public BlockBuilder nonOpaque(boolean nonOpaque) {
            this.nonOpaque = nonOpaque;
            return this;
        }

        public BlockBuilder harvestLevel(int level) {
            this.harvestLevel = level;
            return this;
        }

        /**
         * Convenience method for stone-like blocks
         */
        public BlockBuilder stoneLike() {
            this.mapColor = MapColor.STONE_GRAY;
            this.requiresTool = true;
            this.nonOpaque = false;
            return this;
        }

        /**
         * Convenience method for wood-like blocks
         */
        public BlockBuilder woodLike() {
            this.mapColor = MapColor.OAK_TAN;
            this.requiresTool = true;
            this.nonOpaque = true;
            return this;
        }

        /**
         * Convenience method for metal-like blocks
         */
        public BlockBuilder metalLike() {
            this.mapColor = MapColor.IRON_GRAY;
            this.requiresTool = true;
            this.nonOpaque = false;
            return this;
        }

        /**
         * Builds the AbstractBlock.Settings for the block
         */
        public AbstractBlock.Settings buildSettings() {
            AbstractBlock.Settings settings = AbstractBlock.Settings.create()
                .strength(hardness, resistance)
                .mapColor(mapColor);

            if (requiresTool) {
                settings = settings.requiresTool();
            }

            if (nonOpaque) {
                settings = settings.nonOpaque();
            }

            return settings;
        }

        /**
         * Validates that required fields are set
         */
        public void validate() {
            if (blockName == null || creativeTab == null) {
                throw new IllegalStateException("Block name and creative tab must be set");
            }
        }

        // Getters for the built block to use
        public String getBlockName() {
            return blockName;
        }

        public String getCreativeTab() {
            return creativeTab;
        }

        public float getHardness() {
            return hardness;
        }

        public float getResistance() {
            return resistance;
        }

        public MapColor getMapColor() {
            return mapColor;
        }

        public boolean isRequiresTool() {
            return requiresTool;
        }

        public boolean isNonOpaque() {
            return nonOpaque;
        }

        public int getHarvestLevel() {
            return harvestLevel;
        }
    }
} 