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
         * Unified builder class for all custom block types
         */
        public static class CustomBlockBuilder {
                private final BlockStateModelGenerator generator;
                private final Block block;
                private final BlockType blockType;

                // Common properties
                private String[] textures = new String[0];
                private List<String[]> randomTextures = new ArrayList<>();
                private List<String[]> states = new ArrayList<>();
                private boolean isSimple = false;

                // Log-specific properties
                private String sideTexture = "";
                private String endTexture = "";
                private boolean uvLocked = false;

                public enum BlockType {
                        SOLID, SLAB, LOG, BRANCH
                }

                public CustomBlockBuilder(BlockStateModelGenerator generator, Block block, BlockType blockType) {
                        this.generator = generator;
                        this.block = block;
                        this.blockType = blockType;
                }

                /**
                 * Set a single texture for all sides (simple block)
                 */
                public CustomBlockBuilder texture(String texturePath) {
                        switch (blockType) {
                                case SOLID:
                                        this.textures = new String[] { texturePath };
                                        this.isSimple = true;
                                        break;
                                case SLAB:
                                        this.textures = new String[] { texturePath };
                                        break;
                                case LOG:
                                        this.sideTexture = texturePath;
                                        this.endTexture = texturePath;
                                        break;
                                case BRANCH:
                                        this.textures = new String[] { texturePath };
                                        break;
                        }
                        return this;
                }

                /**
                 * Set multiple textures for different sides
                 * Texture order: down, up, north, south, east, west
                 */
                public CustomBlockBuilder textures(String... texturePaths) {
                        switch (blockType) {
                                case SOLID:
                                        this.textures = texturePaths;
                                        this.isSimple = false;
                                        break;
                                case SLAB:
                                        this.textures = texturePaths;
                                        break;
                                case LOG:
                                        if (texturePaths.length >= 2) {
                                                this.sideTexture = texturePaths[0];
                                                this.endTexture = texturePaths[1];
                                        }
                                        break;
                                case BRANCH:
                                        this.textures = texturePaths;
                                        break;
                        }
                        return this;
                }

                /**
                 * Add a random texture variant (SOLID blocks only)
                 * Texture order: down, up, north, south, east, west
                 */
                public CustomBlockBuilder randomTexture(String... texturePaths) {
                        if (blockType == BlockType.SOLID) {
                                this.randomTextures.add(texturePaths);
                                this.isSimple = false;
                        }
                        return this;
                }

                /**
                 * Add a state variant (SOLID blocks only)
                 * Texture order: down, up, north, south, east, west
                 */
                public CustomBlockBuilder state(String... texturePaths) {
                        if (blockType == BlockType.SOLID) {
                                this.states.add(texturePaths);
                        }
                        return this;
                }

                /**
                 * Enable UV locking for logs (LOG blocks only)
                 */
                public CustomBlockBuilder uvLocked() {
                        if (blockType == BlockType.LOG) {
                                this.uvLocked = true;
                        }
                        return this;
                }

                /**
                 * Build and register the block based on its type
                 */
                public void build() {
                        switch (blockType) {
                                case SOLID:
                                        buildSolidBlock();
                                        break;
                                case SLAB:
                                        buildSlabBlock();
                                        break;
                                case LOG:
                                        buildLogBlock();
                                        break;
                                case BRANCH:
                                        buildBranchBlock();
                                        break;
                        }
                }

                private void buildSolidBlock() {
                        if (!states.isEmpty()) {
                                String[][] textureArrays = states.toArray(new String[0][0]);
                                SolidBlockExporter.registerCustomSolidBlockWithStates(generator, block, textureArrays);
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

                private void buildSlabBlock() {
                        SlabBlockExporter.registerCustomSlabBlock(generator, block, textures);
                }

                private void buildLogBlock() {
                        LogBlockExporter.registerCustomLogBlock(generator, block, sideTexture, endTexture, uvLocked);
                }

                private void buildBranchBlock() {
                        BranchBlockExporter.registerBranchBlock(generator, block, textures);
                }
        }

        /**
         * Start building a custom solid block
         */
        public static CustomBlockBuilder registerCustomSolidBlock(BlockStateModelGenerator generator, Block block) {
                return new CustomBlockBuilder(generator, block, CustomBlockBuilder.BlockType.SOLID);
        }

        /**
         * Start building a custom slab block
         */
        public static CustomBlockBuilder registerCustomSlabBlock(BlockStateModelGenerator generator, Block block) {
                return new CustomBlockBuilder(generator, block, CustomBlockBuilder.BlockType.SLAB);
        }

        /**
         * Start building a custom log block
         */
        public static CustomBlockBuilder registerCustomLogBlock(BlockStateModelGenerator generator, Block block) {
                return new CustomBlockBuilder(generator, block, CustomBlockBuilder.BlockType.LOG);
        }

        /**
         * Start building a custom branch block
         */
        public static CustomBlockBuilder registerCustomBranchBlock(BlockStateModelGenerator generator, Block block) {
                return new CustomBlockBuilder(generator, block, CustomBlockBuilder.BlockType.BRANCH);
        }

}