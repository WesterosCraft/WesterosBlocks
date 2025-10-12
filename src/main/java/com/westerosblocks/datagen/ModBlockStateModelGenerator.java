package com.westerosblocks.datagen;

import com.westerosblocks.datagen.custom.*;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Clean datagen builder following block-models.md conventions.
 * Implements the pattern from sections 5.2-5.6: Model instances, TextureMap builders, 
 * BlockStateSupplier methods, and clean datagen methods.
 */
public class ModBlockStateModelGenerator extends BaseBlockExporter {

        /**
         * Simple unified builder for all block types
         */
        public static class CustomBlockBuilder {
                private final BlockStateModelGenerator generator;
                private final Block block;
                private final String blockType;

                private String texture = "";
                private String[] textures = new String[0];
                private List<String[]> randomTextures = new ArrayList<>();
                private List<String[]> states = new ArrayList<>();
                private boolean isTinted = false;

                public CustomBlockBuilder(BlockStateModelGenerator generator, Block block, String blockType) {
                        this.generator = generator;
                        this.block = block;
                        this.blockType = blockType;
                }

                public CustomBlockBuilder texture(String texturePath) {
                        this.texture = texturePath;
                        return this;
                }

                public CustomBlockBuilder textures(String... texturePaths) {
                        this.textures = texturePaths;
                        return this;
                }

                public CustomBlockBuilder randomTexture(String... texturePaths) {
                        this.randomTextures.add(texturePaths);
                        return this;
                }

                public CustomBlockBuilder state(String... texturePaths) {
                        this.states.add(texturePaths);
                        return this;
                }

                public CustomBlockBuilder isTinted(boolean tinted) {
                        this.isTinted = tinted;
                        return this;
                }

                public void build() {
                        switch (blockType) {
//                                case "solid" -> buildSolid();
//                                case "slab" -> buildSlab();
//                                case "log" -> buildLog();
                                case "branch" -> buildBranch();
//                                case "door" -> buildDoor();
//                                case "half_door" -> buildHalfDoor();
//                                case "trapdoor" -> buildTrapdoor();
//                                case "pane" -> buildPane();
//                                case "torch" -> buildTorch();
                                case "chair" -> buildChair();
                                case "table" -> buildTable();
                                case "arrow_slit" -> buildArrowSlit();
//                                case "rail" -> buildRail();
//                                case "fan" -> buildFan();
//                                case "furnace" -> buildFurnace();
//                                case "plant" -> buildPlant();
//                                case "cross" -> buildPlant(); // Alias for plant
//                                case "crop" -> buildCrop();
                                case "flowerbed" -> buildFlowerbed();
//                                case "cuboid" -> buildCuboid();
//                                case "bed" -> buildBed();
                                default -> throw new IllegalArgumentException("Unknown block type: " + blockType);
                        }
                }


                private void buildBranch() {
                        generateBranch(generator, block, !texture.isEmpty() ? texture : textures[0]);
                }




                private void buildChair() {
                        generateChair(generator, block, !texture.isEmpty() ? texture : textures[0]);
                }

                private void buildTable() {
                        generateTable(generator, block, !texture.isEmpty() ? texture : textures[0]);
                }

                private void buildArrowSlit() {
                        generateArrowSlit(generator, block, !texture.isEmpty() ? texture : textures[0]);
                }


                private void buildFlowerbed() {
                        if (!texture.isEmpty()) {
                                FlowerbedBlockExporter.generateCustomFlowerbed(generator, block, "block/flowerbed", TextureKey.STEM, "", texture);
                        } else if (textures.length > 0) {
                                String stemTexture = textures.length > 1 ? textures[0] : "";
                                String flowerTexture = textures.length > 1 ? textures[1] : textures[0];
                                FlowerbedBlockExporter.generateCustomFlowerbed(generator, block, "block/flowerbed", TextureKey.STEM, stemTexture, flowerTexture);
                        }
                }

        }
        
        private static void generateBranch(BlockStateModelGenerator generator, Block block, String texturePath) {
                BranchBlockExporter.registerBranchBlock(generator, block, texturePath);
        }


        private static void generateChair(BlockStateModelGenerator generator, Block block, String texturePath) {
                ChairBlockExporter.registerChairBlock(generator, block, texturePath);
        }


        private static void generateTable(BlockStateModelGenerator generator, Block block, String texturePath) {
                TableBlockExporter.registerCustomTableBlock(generator, block, texturePath);
        }

        private static void generateArrowSlit(BlockStateModelGenerator generator, Block block, String texturePath) {
                ArrowSlitBlockExporter.registerArrowSlitBlock(generator, block, texturePath);
        }

        // Factory methods for each block type as needed
        public static CustomBlockBuilder registerCustomBranchBlock(BlockStateModelGenerator generator, Block block) {
                return new CustomBlockBuilder(generator, block, "branch");
        }

        public static CustomBlockBuilder registerCustomChairBlock(BlockStateModelGenerator generator, Block block) {
                return new CustomBlockBuilder(generator, block, "chair");
        }

        public static CustomBlockBuilder registerCustomTableBlock(BlockStateModelGenerator generator, Block block) {
                return new CustomBlockBuilder(generator, block, "table");
        }

        public static CustomBlockBuilder registerCustomArrowSlitBlock(BlockStateModelGenerator generator, Block block) {
                return new CustomBlockBuilder(generator, block, "arrow_slit");
        }

        public static FlowerbedBlockExporter.CustomFlowerbedBuilder registerCustomFlowerbedBlock(BlockStateModelGenerator generator, Block block) {
                return FlowerbedBlockExporter.registerCustomFlowerbedBlock(generator, block);
        }

}