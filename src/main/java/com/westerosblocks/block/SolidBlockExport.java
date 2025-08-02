package com.westerosblocks.block;

import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for registering custom solid blocks with custom textures and
 * model generation.
 * Provides a builder pattern for easy configuration of block properties.
 */
public class SolidBlockExport {
    private final BlockStateModelGenerator blockStateModelGenerator;
    private final Map<Block, BlockConfig> blockConfigs = new HashMap<>();

    public SolidBlockExport(BlockStateModelGenerator blockStateModelGenerator) {
        this.blockStateModelGenerator = blockStateModelGenerator;
    }

    /**
     * Register a simple cube all block with custom texture
     * 
     * @param block The block to register
     * @return BlockBuilder for further configuration
     */
    public BlockBuilder registerSimpleCubeAll(Block block) {
        return new BlockBuilder(block);
    }

    /**
     * Builder class for configuring block properties
     */
    public class BlockBuilder {
        private final Block block;
        private String texturePath;
        private String parentModel = "minecraft:block/cube_all";
        private String textureKey = "all";

        public BlockBuilder(Block block) {
            this.block = block;
        }

        /**
         * Set the texture path for the block
         * 
         * @param texturePath The texture path (e.g., "bark/birch/side")
         * @return this builder for chaining
         */
        public BlockBuilder texture(String texturePath) {
            this.texturePath = texturePath;
            return this;
        }

        /**
         * Set a custom parent model
         * 
         * @param parentModel The parent model identifier (e.g.,
         *                    "minecraft:block/cube_all")
         * @return this builder for chaining
         */
        public BlockBuilder parent(String parentModel) {
            this.parentModel = parentModel;
            return this;
        }

        /**
         * Set a custom texture key (default is "all")
         * 
         * @param textureKey The texture key to use
         * @return this builder for chaining
         */
        public BlockBuilder textureKey(String textureKey) {
            this.textureKey = textureKey;
            return this;
        }

        /**
         * Build and register the block configuration
         * 
         * @return this builder for chaining
         */
        public BlockBuilder build() {
            BlockConfig config = new BlockConfig(texturePath, parentModel, textureKey);
            blockConfigs.put(block, config);

            // For now, use the standard method
            // The custom texture will be handled by the generated model file
            blockStateModelGenerator.registerSimpleCubeAll(block);

            return this;
        }
    }

    /**
     * Configuration class for block properties
     */
    private static class BlockConfig {
        private final String texturePath;
        private final String parentModel;
        private final String textureKey;

        public BlockConfig(String texturePath, String parentModel, String textureKey) {
            this.texturePath = texturePath;
            this.parentModel = parentModel;
            this.textureKey = textureKey;
        }
    }

    /**
     * Get the block configurations for model generation
     * 
     * @return Map of blocks to their configurations
     */
    public Map<Block, BlockConfig> getBlockConfigs() {
        return blockConfigs;
    }
}