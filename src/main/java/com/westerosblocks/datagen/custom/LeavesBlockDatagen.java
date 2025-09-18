package com.westerosblocks.datagen.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.datagen.ModTextureKey;
import net.minecraft.data.client.*;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class LeavesBlockDatagen {
    
    // Parent Block Models - following block-models.md #parent-block-model pattern
    private static Model createLeavesModel(boolean tinted, boolean overlay) {
        String tintPath = tinted ? "block/tinted/" : "block/untinted/";
        String overlayPath = overlay ? "leaves_overlay" : "leaves";
        String path = tintPath + overlayPath;
        
        if (overlay) {
            return new Model(Optional.of(WesterosBlocks.id(path)), Optional.empty(), 
                TextureKey.END, TextureKey.SIDE, ModTextureKey.LEAVES_OVERLAY_END, ModTextureKey.LEAVES_OVERLAY_SIDE, TextureKey.PARTICLE);
        } else {
            return new Model(Optional.of(WesterosBlocks.id(path)), Optional.empty(), 
                TextureKey.END, TextureKey.SIDE, TextureKey.PARTICLE);
        }
    }
    
    private static Model createLeavesBetterFoliageModel(boolean tinted, boolean overlay, int variant) {
        String tintPath = tinted ? "block/tinted/" : "block/untinted/";
        String overlayPath = overlay ? "leaves_overlay_bf" : "leaves_bf";
        String path = tintPath + overlayPath + variant;
        
        if (overlay) {
            return new Model(Optional.of(WesterosBlocks.id(path)), Optional.empty(), 
                TextureKey.ALL, ModTextureKey.LEAVES_OVERLAY_END, ModTextureKey.LEAVES_OVERLAY_SIDE, TextureKey.PARTICLE);
        } else {
            return new Model(Optional.of(WesterosBlocks.id(path)), Optional.empty(), 
                TextureKey.ALL, TextureKey.PARTICLE);
        }
    }

    // Builder pattern for leaves block generation
    public static class LeavesBlockBuilder {
        private final BlockStateModelGenerator generator;
        private final Block leavesBlock;
        private final String leavesName;
        private boolean isTinted = false;
        private boolean hasOverlay = false;
        private boolean betterFoliage = false;
        private boolean rotateRandom = false;
        private final List<RandomTextureSet> randomTextureSets = new ArrayList<>();
        
        // Inner class to hold random texture set information
        public static class RandomTextureSet {
            public final String[] textures;
            public final int weight;
            
            public RandomTextureSet(int weight, String... textures) {
                this.weight = weight;
                this.textures = textures;
            }
        }
        
        public LeavesBlockBuilder(BlockStateModelGenerator generator, Block leavesBlock, String leavesName) {
            this.generator = generator;
            this.leavesBlock = leavesBlock;
            this.leavesName = leavesName;
        }
        
        public LeavesBlockBuilder isTinted() {
            this.isTinted = true;
            return this;
        }
        
        public LeavesBlockBuilder hasOverlay() {
            this.hasOverlay = true;
            return this;
        }
        
        public LeavesBlockBuilder betterFoliage() {
            this.betterFoliage = true;
            return this;
        }
        
        public LeavesBlockBuilder rotateRandom() {
            this.rotateRandom = true;
            return this;
        }
        
        public LeavesBlockBuilder addRandomTextureSet(int weight, String... textures) {
            this.randomTextureSets.add(new RandomTextureSet(weight, textures));
            return this;
        }
        
        public void build() {
            if (randomTextureSets.isEmpty()) {
                throw new IllegalStateException("No random texture sets defined for leaves block " + leavesBlock);
            }
            
            List<Identifier> modelIds = generateLeavesModels();
            VariantsBlockStateSupplier blockStateSupplier = generateBlockStateVariants(modelIds);
            
            generator.blockStateCollector.accept(blockStateSupplier);
            
            // Register item model using first model
            if (!modelIds.isEmpty()) {
                generator.registerParentedItemModel(leavesBlock, modelIds.get(0));
            }
        }
        
        private List<Identifier> generateLeavesModels() {
            List<Identifier> modelIds = new ArrayList<>();
            
            for (int setIdx = 0; setIdx < randomTextureSets.size(); setIdx++) {
                RandomTextureSet textureSet = randomTextureSets.get(setIdx);
                
                if (betterFoliage) {
                    // Generate 3 better foliage variants
                    for (int bfIdx = 1; bfIdx <= 3; bfIdx++) {
                        TextureMap textureMap = createTextureMapForBetterFoliage(textureSet);
                        
                        String modelSuffix = "/bf" + bfIdx + "_v" + (setIdx + 1);
                        Identifier modelId = createLeavesBetterFoliageModel(isTinted, hasOverlay, bfIdx)
                                .upload(leavesBlock, modelSuffix, textureMap, generator.modelCollector);
                        
                        modelIds.add(modelId);
                    }
                } else {
                    // Standard leaves model
                    TextureMap textureMap = createTextureMapForStandard(textureSet);
                    
                    String modelSuffix = "/base_v" + (setIdx + 1);
                    Identifier modelId = createLeavesModel(isTinted, hasOverlay)
                            .upload(leavesBlock, modelSuffix, textureMap, generator.modelCollector);
                    
                    modelIds.add(modelId);
                }
            }
            
            return modelIds;
        }
        
        private TextureMap createTextureMapForStandard(RandomTextureSet textureSet) {
            TextureMap textureMap = new TextureMap();
            
            if (hasOverlay) {
                // Overlay model: end, side, overlayend, overlayside
                textureMap.put(TextureKey.END, WesterosBlocks.id("block/" + textureSet.textures[0]));
                textureMap.put(TextureKey.SIDE, WesterosBlocks.id("block/" + textureSet.textures[1]));
                textureMap.put(TextureKey.PARTICLE, WesterosBlocks.id("block/" + textureSet.textures[1]));
                
                if (textureSet.textures.length >= 4) {
                    textureMap.put(ModTextureKey.LEAVES_OVERLAY_END, WesterosBlocks.id("block/" + textureSet.textures[2]));
                    textureMap.put(ModTextureKey.LEAVES_OVERLAY_SIDE, WesterosBlocks.id("block/" + textureSet.textures[3]));
                }
            } else {
                // Standard model: end, side
                textureMap.put(TextureKey.END, WesterosBlocks.id("block/" + textureSet.textures[0]));
                textureMap.put(TextureKey.SIDE, WesterosBlocks.id("block/" + textureSet.textures[1]));
                textureMap.put(TextureKey.PARTICLE, WesterosBlocks.id("block/" + textureSet.textures[1]));
            }
            
            return textureMap;
        }
        
        private TextureMap createTextureMapForBetterFoliage(RandomTextureSet textureSet) {
            TextureMap textureMap = new TextureMap();
            
            if (hasOverlay) {
                // Better foliage overlay: all, overlayend, overlayside
                textureMap.put(TextureKey.ALL, WesterosBlocks.id("block/" + textureSet.textures[0]));
                textureMap.put(TextureKey.PARTICLE, WesterosBlocks.id("block/" + textureSet.textures[0]));
                
                if (textureSet.textures.length >= 3) {
                    textureMap.put(ModTextureKey.LEAVES_OVERLAY_END, WesterosBlocks.id("block/" + textureSet.textures[1]));
                    textureMap.put(ModTextureKey.LEAVES_OVERLAY_SIDE, WesterosBlocks.id("block/" + textureSet.textures[2]));
                }
            } else {
                // Better foliage standard: all
                textureMap.put(TextureKey.ALL, WesterosBlocks.id("block/" + textureSet.textures[0]));
                textureMap.put(TextureKey.PARTICLE, WesterosBlocks.id("block/" + textureSet.textures[0]));
            }
            
            return textureMap;
        }
        
        private VariantsBlockStateSupplier generateBlockStateVariants(List<Identifier> modelIds) {
            List<BlockStateVariant> variants = new ArrayList<>();
            boolean hasWeights = false;
            
            int modelIndex = 0;
            for (int setIdx = 0; setIdx < randomTextureSets.size(); setIdx++) {
                RandomTextureSet textureSet = randomTextureSets.get(setIdx);
                int rotationCount = rotateRandom ? 4 : 1;
                
                if (betterFoliage) {
                    // 3 better foliage variants per texture set
                    for (int bfIdx = 0; bfIdx < 3; bfIdx++) {
                        for (int rotation = 0; rotation < rotationCount; rotation++) {
                            BlockStateVariant variant = BlockStateVariant.create()
                                    .put(VariantSettings.MODEL, modelIds.get(modelIndex));
                            
                            if (textureSet.weight > 1) {
                                variant = variant.put(VariantSettings.WEIGHT, textureSet.weight);
                                hasWeights = true;
                            }
                            
                            if (rotation > 0) {
                                variant = variant.put(VariantSettings.Y, VariantSettings.Rotation.valueOf("R" + (90 * rotation)));
                            }
                            
                            variants.add(variant);
                        }
                        modelIndex++;
                    }
                } else {
                    // Standard single model per texture set
                    for (int rotation = 0; rotation < rotationCount; rotation++) {
                        BlockStateVariant variant = BlockStateVariant.create()
                                .put(VariantSettings.MODEL, modelIds.get(modelIndex));
                        
                        if (textureSet.weight > 1) {
                            variant = variant.put(VariantSettings.WEIGHT, textureSet.weight);
                            hasWeights = true;
                        }
                        
                        if (rotation > 0) {
                            variant = variant.put(VariantSettings.Y, VariantSettings.Rotation.valueOf("R" + (90 * rotation)));
                        }
                        
                        variants.add(variant);
                    }
                    modelIndex++;
                }
            }
            
            // If only one variant without weights, use single variant format
            if (variants.size() == 1 && !hasWeights) {
                return VariantsBlockStateSupplier.create(leavesBlock, variants.get(0));
            } else {
                return VariantsBlockStateSupplier.create(leavesBlock, variants.toArray(new BlockStateVariant[0]));
            }
        }
    }
    
    // Entry point for builder pattern
    public static LeavesBlockBuilder generateLeavesBlock(BlockStateModelGenerator generator, Block leavesBlock, String leavesName) {
        return new LeavesBlockBuilder(generator, leavesBlock, leavesName);
    }
}
