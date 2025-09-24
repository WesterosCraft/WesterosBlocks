package com.westerosblocks.datagen;

import com.westerosblocks.datagen.custom.*;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

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
                                case "solid" -> buildSolid();
                                case "slab" -> buildSlab(); 
                                case "log" -> buildLog();
                                case "branch" -> buildBranch();
                                case "door" -> buildDoor();
                                case "half_door" -> buildHalfDoor();
                                case "pane" -> buildPane();
                                case "torch" -> buildTorch();
                                case "chair" -> buildChair();
                                case "table" -> buildTable();
                                case "arrow_slit" -> buildArrowSlit();
                                case "rail" -> buildRail();
                                case "fan" -> buildFan();
                                case "plant" -> buildPlant();
                                case "cross" -> buildPlant(); // Alias for plant
//                                case "crop" -> buildCrop();
                                case "flowerbed" -> buildFlowerbed();
//                                case "bed" -> buildBed();
                                default -> throw new IllegalArgumentException("Unknown block type: " + blockType);
                        }
                }

                private void buildSolid() {
                        if (!states.isEmpty()) {
                                SolidBlockExporter.registerCustomSolidBlockWithStates(generator, block, states.toArray(new String[0][0]));
                        } else if (!randomTextures.isEmpty()) {
                                SolidBlockExporter.registerCustomSolidBlockWithRandomTextures(generator, block, randomTextures.toArray(new String[0][0]), isTinted);
                        } else if (!texture.isEmpty()) {
                                SolidBlockExporter.registerSimpleCustomSolidBlock(generator, block, texture, isTinted);
                        } else {
                                SolidBlockExporter.registerCustomSolidBlock(generator, block, isTinted, textures);
                        }
                }

                private void buildSlab() {
                        if (!texture.isEmpty()) {
                                generateSlab(generator, block, texture);
                        } else {
                                generateSlab(generator, block, textures);
                        }
                }

                private void buildLog() {
                        if (textures.length >= 2) {
                                generateLog(generator, block, textures[0], textures[1]);
                        } else if (!texture.isEmpty()) {
                                generateLog(generator, block, texture, texture);
                        }
                }

                private void buildBranch() {
                        generateBranch(generator, block, !texture.isEmpty() ? texture : textures[0]);
                }

                private void buildDoor() {
                        if (textures.length >= 2) {
                                generateDoor(generator, block, textures[0], textures[1]);
                        } else if (!texture.isEmpty()) {
                                generateDoor(generator, block, texture, texture);
                        }
                }

                private void buildHalfDoor() {
                        generateHalfDoor(generator, block, !texture.isEmpty() ? texture : textures[0]);
                }

                private void buildPane() {
                        if (!randomTextures.isEmpty()) {
                                String[] textureArray = randomTextures.stream()
                                        .map(arr -> arr[0])
                                        .toArray(String[]::new);
                                generatePaneWithRandomTextures(generator, block, textureArray);
                        } else {
                                generatePane(generator, block, !texture.isEmpty() ? texture : textures[0]);
                        }
                }

                private void buildTorch() {
                        generateTorch(generator, block, !texture.isEmpty() ? texture : textures[0]);
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

                private void buildRail() {
                        List<String> textureList = new ArrayList<>();
                        if (!texture.isEmpty()) {
                                textureList.add(texture);
                        } else if (textures.length > 0) {
                                textureList.addAll(List.of(textures));
                        } else if (!randomTextures.isEmpty()) {
                                for (String[] randomSet : randomTextures) {
                                        textureList.addAll(List.of(randomSet));
                                }
                        }
                        generateRail(generator, block, textureList);
                }

                private void buildFan() {
                        if (!randomTextures.isEmpty()) {
                                generateFanWithRandomTextures(generator, block, randomTextures.toArray(new String[0][0]));
                        } else if (!texture.isEmpty()) {
                                generateFan(generator, block, texture);
                        } else if (textures.length > 0) {
                                generateFan(generator, block, textures[0]);
                        }
                }

                private void buildPlant() {
                        if (!randomTextures.isEmpty()) {
                                String[] textureArray = randomTextures.stream()
                                        .map(arr -> arr[0])
                                        .toArray(String[]::new);
                                generatePlantWithRandomTextures(generator, block, textureArray, isTinted);
                        } else if (!texture.isEmpty()) {
                                generatePlant(generator, block, texture, isTinted);
                        } else if (textures.length > 0) {
                                generatePlant(generator, block, textures[0], isTinted);
                        }
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

//                private void buildBed() {
//                        if (!texture.isEmpty()) {
//                                BedBlockExporter.registerSingleTextureBedBlock(generator, block, texture);
//                        } else if (textures.length >= 1) {
//                                // Pass all textures to handle the full 6-texture bed system
//                                BedBlockExporter.registerSimpleCustomBedBlock(generator, block, textures);
//                        }
//                }
        }

        // Clean datagen methods following block-models.md pattern

        private static void generateSimpleSolid(BlockStateModelGenerator generator, Block block, String texturePath) {
                TextureMap textureMap = TextureMap.all(createBlockIdentifier(texturePath));
                Identifier modelId = Models.CUBE_ALL.upload(createModelId(block), textureMap, generator.modelCollector);
                generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block, 
                        BlockStateVariant.create().put(VariantSettings.MODEL, modelId)));
                generator.registerParentedItemModel(block, modelId);
        }

        private static void generateSolid(BlockStateModelGenerator generator, Block block, String[] texturePaths) {
                String[] filled = fillTextureArray(texturePaths);
                TextureMap textureMap = ModTextureMap.customAllSides(filled);
                Identifier modelId = Models.CUBE.upload(createModelId(block), textureMap, generator.modelCollector);
                generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block,
                        BlockStateVariant.create().put(VariantSettings.MODEL, modelId)));
                generator.registerParentedItemModel(block, modelId);
        }

        private static void generateSolidWithRandomTextures(BlockStateModelGenerator generator, Block block, String[][] textureArrays) {
                List<Identifier> modelIds = new ArrayList<>();
                for (int i = 0; i < textureArrays.length; i++) {
                        String[] filled = fillTextureArray(textureArrays[i]);
                        TextureMap textureMap = ModTextureMap.customAllSides(filled);
                        Identifier modelId = Models.CUBE.upload(createModelId(block, "variant_" + (i + 1)), textureMap, generator.modelCollector);
                        modelIds.add(modelId);
                }
                
                BlockStateVariant[] variants = modelIds.stream()
                        .map(id -> BlockStateVariant.create().put(VariantSettings.MODEL, id))
                        .toArray(BlockStateVariant[]::new);
                        
                generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block, variants));
                generator.registerParentedItemModel(block, modelIds.get(0));
        }

        private static void generateSolidWithStates(BlockStateModelGenerator generator, Block block, String[][] textureArrays) {
                // Similar to random textures but with state properties
                generateSolidWithRandomTextures(generator, block, textureArrays);
        }

        private static void generateSlab(BlockStateModelGenerator generator, Block block, String texturePath) {
                TextureMap textureMap = TextureMap.all(createBlockIdentifier(texturePath));
                generateSlab(generator, block, textureMap);
        }

        private static void generateSlab(BlockStateModelGenerator generator, Block block, String[] texturePaths) {
                String[] filled = fillTextureArray(texturePaths);
                TextureMap textureMap = ModTextureMap.customAllSides(filled);
                generateSlab(generator, block, textureMap);
        }

        private static void generateSlab(BlockStateModelGenerator generator, Block block, TextureMap textureMap) {
                Identifier bottomId = ModModels.SLAB_BOTTOM.upload(createModelId(block, "bottom"), textureMap, generator.modelCollector);
                Identifier topId = ModModels.SLAB_TOP.upload(createModelId(block, "top"), textureMap, generator.modelCollector);
                Identifier doubleId = Models.CUBE.upload(createModelId(block, "double"), textureMap, generator.modelCollector);
                
                generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block)
                        .coordinate(BlockStateVariantMap.create(net.minecraft.state.property.Properties.SLAB_TYPE)
                                .register(net.minecraft.block.enums.SlabType.BOTTOM, BlockStateVariant.create().put(VariantSettings.MODEL, bottomId))
                                .register(net.minecraft.block.enums.SlabType.TOP, BlockStateVariant.create().put(VariantSettings.MODEL, topId))
                                .register(net.minecraft.block.enums.SlabType.DOUBLE, BlockStateVariant.create().put(VariantSettings.MODEL, doubleId))));
                                
                generator.registerParentedItemModel(block, bottomId);
        }

        private static void generateLog(BlockStateModelGenerator generator, Block block, String sideTexture, String endTexture) {
                TextureMap textureMap = new TextureMap()
                        .put(TextureKey.SIDE, createBlockIdentifier(sideTexture))
                        .put(TextureKey.END, createBlockIdentifier(endTexture));
                        
                Identifier verticalId = ModModels.LOG.upload(createModelId(block), textureMap, generator.modelCollector);
                Identifier horizontalId = ModModels.LOG_HORIZONTAL.upload(createModelId(block, "horizontal"), textureMap, generator.modelCollector);
                
                generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block)
                        .coordinate(BlockStateVariantMap.create(net.minecraft.state.property.Properties.AXIS)
                                .register(net.minecraft.util.math.Direction.Axis.Y, BlockStateVariant.create().put(VariantSettings.MODEL, verticalId))
                                .register(net.minecraft.util.math.Direction.Axis.Z, BlockStateVariant.create().put(VariantSettings.MODEL, horizontalId).put(VariantSettings.X, VariantSettings.Rotation.R90))
                                .register(net.minecraft.util.math.Direction.Axis.X, BlockStateVariant.create().put(VariantSettings.MODEL, horizontalId).put(VariantSettings.X, VariantSettings.Rotation.R90).put(VariantSettings.Y, VariantSettings.Rotation.R90))));
                                
                generator.registerParentedItemModel(block, verticalId);
        }

        private static void generateBranch(BlockStateModelGenerator generator, Block block, String texturePath) {
                // Branch blocks use complex multipart logic - delegate to BranchBlockExporter
                BranchBlockExporter.registerBranchBlock(generator, block, texturePath);
        }

        private static void generateDoor(BlockStateModelGenerator generator, Block block, String topTexture, String bottomTexture) {
                // Delegate to DoorBlockExporter for complex door logic
                DoorBlockExporter.registerDoorBlock(generator, block, topTexture, bottomTexture);
        }

        private static void generateHalfDoor(BlockStateModelGenerator generator, Block block, String texturePath) {
                // Delegate to HalfDoorBlockExporter for complex shutter logic
                HalfDoorBlockExporter.registerHalfDoorBlock(generator, block, texturePath);
        }

        private static void generatePane(BlockStateModelGenerator generator, Block block, String texturePath) {
                PaneBlockExporter.registerPaneBlock(generator, block, texturePath);
        }

        private static void generatePaneWithRandomTextures(BlockStateModelGenerator generator, Block block, String[] textures) {
                PaneBlockExporter.registerPaneBlockWithRandomTextures(generator, block, textures);
        }

        private static void generateTorch(BlockStateModelGenerator generator, Block block, String texturePath) {
                TorchBlockExporter.registerTorchBlock(generator, block, texturePath);
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

        private static void generateRail(BlockStateModelGenerator generator, Block block, List<String> texturePaths) {
                if (texturePaths.size() == 1) {
                        RailBlockExporter.registerRailBlock(generator, block, texturePaths.get(0));
                } else {
                        RailBlockExporter.registerRailBlock(generator, block, texturePaths.toArray(new String[0]));
                }
        }

        private static void generateFan(BlockStateModelGenerator generator, Block block, String texturePath) {
                FanBlockExporter.registerFanBlock(generator, block, texturePath);
        }

        private static void generateFanWithRandomTextures(BlockStateModelGenerator generator, Block block, String[][] randomTexturePaths) {
                // For random textures, use the first texture of the first set as the primary texture
                String primaryTexture = randomTexturePaths.length > 0 && randomTexturePaths[0].length > 0 
                        ? randomTexturePaths[0][0] : "coral/tube/fan1";
                FanBlockExporter.registerFanBlock(generator, block, primaryTexture);
        }

        private static void generatePlant(BlockStateModelGenerator generator, Block block, String texturePath, boolean isTinted) {
                // Check if this is a layer-sensitive plant
                if (block instanceof com.westerosblocks.block.custom.WCPlantBlock && 
                    ((com.westerosblocks.block.custom.WCPlantBlock) block).isLayerSensitive()) {
                        CrossBlockExporter.generateLayerSensitiveCross(generator, block, texturePath, isTinted, 1);
                } else {
                        CrossBlockExporter.generateCross(generator, block, texturePath, isTinted, 1);
                }
        }

        private static void generatePlantWithRandomTextures(BlockStateModelGenerator generator, Block block, String[] texturePaths, boolean isTinted) {
                if (block instanceof com.westerosblocks.block.custom.WCPlantBlock && 
                    ((com.westerosblocks.block.custom.WCPlantBlock) block).isLayerSensitive()) {
                        CrossBlockExporter.generateLayerSensitiveCrossWithRandomTextures(generator, block, texturePaths, isTinted, 1);
                } else {
                        CrossBlockExporter.generateCrossWithRandomTextures(generator, block, texturePaths, isTinted, 1);
                }
        }

        // Factory methods for each block type

        public static CustomBlockBuilder registerCustomSolidBlock(BlockStateModelGenerator generator, Block block) {
                return new CustomBlockBuilder(generator, block, "solid");
        }

        public static CustomBlockBuilder registerCustomSlabBlock(BlockStateModelGenerator generator, Block block) {
                return new CustomBlockBuilder(generator, block, "slab");
        }

        public static CustomBlockBuilder registerCustomLogBlock(BlockStateModelGenerator generator, Block block) {
                return new CustomBlockBuilder(generator, block, "log");
        }

        public static CustomBlockBuilder registerCustomBranchBlock(BlockStateModelGenerator generator, Block block) {
                return new CustomBlockBuilder(generator, block, "branch");
        }

        public static CustomBlockBuilder registerCustomDoorBlock(BlockStateModelGenerator generator, Block block) {
                return new CustomBlockBuilder(generator, block, "door");
        }

        public static CustomBlockBuilder registerCustomHalfDoorBlock(BlockStateModelGenerator generator, Block block) {
                return new CustomBlockBuilder(generator, block, "half_door");
        }

        public static CustomBlockBuilder registerCustomPaneBlock(BlockStateModelGenerator generator, Block block) {
                return new CustomBlockBuilder(generator, block, "pane");
        }

        public static CustomBlockBuilder registerCustomTorchBlock(BlockStateModelGenerator generator, Block block) {
                return new CustomBlockBuilder(generator, block, "torch");
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

        public static CustomBlockBuilder registerCustomRailBlock(BlockStateModelGenerator generator, Block block) {
                return new CustomBlockBuilder(generator, block, "rail");
        }

        public static CustomBlockBuilder registerCustomFanBlock(BlockStateModelGenerator generator, Block block) {
                return new CustomBlockBuilder(generator, block, "fan");
        }


        public static FlowerbedBlockExporter.CustomFlowerbedBuilder registerCustomFlowerbedBlock(BlockStateModelGenerator generator, Block block) {
                return FlowerbedBlockExporter.registerCustomFlowerbedBlock(generator, block);
        }

}