package com.westerosblocks.datagen;

import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;

import java.util.ArrayList;
import java.util.List;

import com.westerosblocks.datagen.custom.SolidBlockExporter;
import com.westerosblocks.datagen.custom.SlabBlockExporter;
import com.westerosblocks.datagen.custom.LogBlockExporter;
import com.westerosblocks.datagen.custom.BranchBlockExporter;

public class ModBlockStateModelGenerator {

        public ModBlockStateModelGenerator() {
        }

        /**
         * Builder class for custom solid blocks
         */
        public static class CustomBlockBuilder {
                private final BlockStateModelGenerator generator;
                private final Block block;
                private String[] textures = new String[0];
                private List<String[]> randomTextures = new ArrayList<>();
                private List<String[]> states = new ArrayList<>();
                private boolean isSimple = false;

                public CustomBlockBuilder(BlockStateModelGenerator generator, Block block) {
                        this.generator = generator;
                        this.block = block;
                }

                /**
                 * Set a single texture for all sides (simple block)
                 */
                public CustomBlockBuilder texture(String texturePath) {
                        this.textures = new String[] { texturePath };
                        this.isSimple = true;
                        return this;
                }

                /**
                 * Set multiple textures for different sides
                 * Texture order: down, up, north, south, east, west
                 */
                public CustomBlockBuilder textures(String... texturePaths) {
                        this.textures = texturePaths;
                        this.isSimple = false;
                        return this;
                }

                /**
                 * Add a random texture variant
                 * Texture order: down, up, north, south, east, west
                 */
                public CustomBlockBuilder randomTexture(String... texturePaths) {
                        this.randomTextures.add(texturePaths);
                        this.isSimple = false;
                        return this;
                }

                /**
                 * Add a random texture variant
                 * Texture order: down, up, north, south, east, west
                 */
                public CustomBlockBuilder state(String... texturePaths) {
                        this.states.add(texturePaths);
                        return this;
                }

                /**
                 * Build and register the block
                 */
                public void build() {
                        if (!states.isEmpty()) {
                                String[][] textureArrays = states.toArray(new String[0][0]);
                                SolidBlockExporter.registerCustomSolidBlockWithStates(generator, block,
                                                textureArrays);
                        } else if (!randomTextures.isEmpty()) {
                                String[][] textureArrays = randomTextures.toArray(new String[0][0]);
                                SolidBlockExporter.registerCustomSolidBlockWithRandomTextures(generator, block,
                                                textureArrays);
                        } else if (isSimple) {
                                SolidBlockExporter.registerSimpleCustomSolidBlock(generator, block, textures[0]);
                        } else {
                                SolidBlockExporter.registerCustomSolidBlock(generator, block, textures);
                        }
                }
        }

        /**
         * Builder class for custom slab blocks
         */
        public static class CustomSlabBlockBuilder {
                private final BlockStateModelGenerator generator;
                private final Block block;
                private String[] textures = new String[0];

                public CustomSlabBlockBuilder(BlockStateModelGenerator generator, Block block) {
                        this.generator = generator;
                        this.block = block;
                }

                /**
                 * Set a single texture for all sides of the slab
                 */
                public CustomSlabBlockBuilder texture(String texturePath) {
                        this.textures = new String[] { texturePath };
                        return this;
                }

                /**
                 * Set separate textures for bottom, top, and sides
                 */
                public CustomSlabBlockBuilder textures(String bottomTexture, String topTexture, String sideTexture) {
                        this.textures = new String[] { bottomTexture, topTexture, sideTexture };
                        return this;
                }

                /**
                 * Set separate textures for all 6 sides
                 */
                public CustomSlabBlockBuilder textures(String downTexture, String upTexture, String northTexture,
                                String southTexture, String eastTexture, String westTexture) {
                        this.textures = new String[] { downTexture, upTexture, northTexture, southTexture, eastTexture,
                                        westTexture };
                        return this;
                }

                /**
                 * Set textures with variable parameters (fills remaining with last texture)
                 */
                public CustomSlabBlockBuilder textures(String... texturePaths) {
                        this.textures = texturePaths;
                        return this;
                }

                /**
                 * Build and register the slab block
                 */
                public void build() {
                        SlabBlockExporter.registerCustomSlabBlock(generator, block, textures);
                }
        }

        /**
         * Builder class for custom log blocks
         */
        public static class CustomLogBlockBuilder {
                private final BlockStateModelGenerator generator;
                private final Block block;
                private String sideTexture = "";
                private String endTexture = "";
                private boolean uvLocked = false;

                public CustomLogBlockBuilder(BlockStateModelGenerator generator, Block block) {
                        this.generator = generator;
                        this.block = block;
                }

                /**
                 * Set a single texture for all sides of the log
                 */
                public CustomLogBlockBuilder texture(String texturePath) {
                        this.sideTexture = texturePath;
                        this.endTexture = texturePath;
                        return this;
                }

                /**
                 * Set separate textures for side and end
                 */
                public CustomLogBlockBuilder textures(String sideTexture, String endTexture) {
                        this.sideTexture = sideTexture;
                        this.endTexture = endTexture;
                        return this;
                }

                /**
                 * Enable UV locking for the log (like bamboo)
                 */
                public CustomLogBlockBuilder uvLocked() {
                        this.uvLocked = true;
                        return this;
                }

                /**
                 * Build and register the log block
                 */
                public void build() {
                        LogBlockExporter.registerCustomLogBlock(generator, block, sideTexture, endTexture, uvLocked);
                }
        }

        /**
         * Start building a custom solid block
         */
        public static CustomBlockBuilder registerCustomSolidBlock(BlockStateModelGenerator generator, Block block) {
                return new CustomBlockBuilder(generator, block);
        }

        /**
         * Start building a custom slab block
         */
        public static CustomSlabBlockBuilder registerCustomSlabBlock(BlockStateModelGenerator generator, Block block) {
                return new CustomSlabBlockBuilder(generator, block);
        }

        /**
         * Start building a custom log block
         */
        public static CustomLogBlockBuilder registerCustomLogBlock(BlockStateModelGenerator generator, Block block) {
                return new CustomLogBlockBuilder(generator, block);
        }

        /**
         * Builder class for custom branch blocks
         */
        public static class CustomBranchBlockBuilder {
                private final BlockStateModelGenerator generator;
                private final Block block;
                private String[] textures = new String[0];

                public CustomBranchBlockBuilder(BlockStateModelGenerator generator, Block block) {
                        this.generator = generator;
                        this.block = block;
                }

                /**
                 * Set a single texture for the branch
                 */
                public CustomBranchBlockBuilder texture(String texturePath) {
                        this.textures = new String[] { texturePath };
                        return this;
                }

                /**
                 * Set multiple textures for the branch
                 */
                public CustomBranchBlockBuilder textures(String... texturePaths) {
                        this.textures = texturePaths;
                        return this;
                }

                /**
                 * Build and register the branch block
                 */
                public void build() {
                        BranchBlockExporter.registerBranchBlock(generator, block, textures);
                }
        }

        public static CustomBranchBlockBuilder registerCustomBranchBlock(BlockStateModelGenerator generator,
                        Block block) {
                return new CustomBranchBlockBuilder(generator, block);
        }

}