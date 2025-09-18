package com.westerosblocks.datagen.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.custom.WCVinesBlock;
import com.westerosblocks.datagen.ModTextureKey;
import net.minecraft.data.client.*;
import net.minecraft.block.Block;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class VinesBlockDatagen {
    
    // Parent Block Models - following block-models.md #parent-block-model pattern
    private static Model createVineModel(String vineType, boolean tinted) {
        String tintPath = tinted ? "block/tinted/" : "block/untinted/";
        String path = tintPath + "vine_" + vineType;
        return new Model(Optional.of(WesterosBlocks.id(path)), Optional.empty(), ModTextureKey.VINES);
    }

    // Builder pattern for vines block generation
    public static class VinesBlockBuilder {
        private final BlockStateModelGenerator generator;
        private final Block vinesBlock;
        private final String vinesName;
        private boolean isTinted = false;
        private final List<RandomTextureSet> randomTextureSets = new ArrayList<>();
        private boolean rotateRandom = false;
        private final List<String> textures = new ArrayList<>();
        
        // Inner class to hold random texture set information
        public static class RandomTextureSet {
            public final String[] textures;
            public final int weight;
            
            public RandomTextureSet(int weight, String... textures) {
                this.weight = weight;
                this.textures = textures;
            }
        }
        
        public VinesBlockBuilder(BlockStateModelGenerator generator, Block vinesBlock, String vinesName) {
            this.generator = generator;
            this.vinesBlock = vinesBlock;
            this.vinesName = vinesName;
        }
        
        public VinesBlockBuilder isTinted() {
            this.isTinted = true;
            return this;
        }
        
        public VinesBlockBuilder rotateRandom() {
            this.rotateRandom = true;
            return this;
        }
        
        public VinesBlockBuilder textures(String... textures) {
            if (textures.length != 2) {
                throw new IllegalArgumentException("Vines blocks require exactly 2 textures (side, top), got " + textures.length);
            }
            this.textures.clear();
            for (String texture : textures) {
                this.textures.add(texture);
            }
            return this;
        }
        
        public VinesBlockBuilder addRandomTextureSet(int weight, String... textures) {
            if (textures.length != 2) {
                throw new IllegalArgumentException("Vines blocks require exactly 2 textures per set (side, top), got " + textures.length);
            }
            this.randomTextureSets.add(new RandomTextureSet(weight, textures));
            return this;
        }
        
        public void build() {
            // If simple textures are provided, convert to a single random texture set
            if (!textures.isEmpty()) {
                if (randomTextureSets.isEmpty()) {
                    randomTextureSets.add(new RandomTextureSet(1, textures.toArray(new String[0])));
                } else {
                    throw new IllegalStateException("Cannot use both .textures() and .addRandomTextureSet() methods");
                }
            }
            
            if (randomTextureSets.isEmpty()) {
                throw new IllegalStateException("No textures defined for vines block " + vinesBlock + ". Use .textures() or .addRandomTextureSet()");
            }
            
            List<Identifier> modelIds = generateVinesModels();
            MultipartBlockStateSupplier blockStateSupplier = generateBlockStateVariants(modelIds);
            
            generator.blockStateCollector.accept(blockStateSupplier);
            
            // Register item model using first side texture
            generateItemModel();
        }
        
        private List<Identifier> generateVinesModels() {
            List<Identifier> modelIds = new ArrayList<>();
            
            // Generate models for each texture set
            for (int setIdx = 0; setIdx < randomTextureSets.size(); setIdx++) {
                RandomTextureSet textureSet = randomTextureSets.get(setIdx);
                
                // Side/base model (for north, south, east, west)
                TextureMap sideTextureMap = new TextureMap()
                        .put(ModTextureKey.VINES, WesterosBlocks.id("block/" + textureSet.textures[0]));
                
                String sideModelSuffix = "/base_v" + (setIdx + 1);
                Identifier sideModelId = createVineModel("1", isTinted)
                        .upload(vinesBlock, sideModelSuffix, sideTextureMap, generator.modelCollector);
                
                modelIds.add(sideModelId);
                
                // Top/up model (for up and down attachments)
                TextureMap topTextureMap = new TextureMap()
                        .put(ModTextureKey.VINES, WesterosBlocks.id("block/" + textureSet.textures[1]));
                
                String topModelSuffix = "/top_v" + (setIdx + 1);
                Identifier topModelId = createVineModel("u", isTinted)
                        .upload(vinesBlock, topModelSuffix, topTextureMap, generator.modelCollector);
                
                modelIds.add(topModelId);
            }
            
            return modelIds;
        }
        
        private MultipartBlockStateSupplier generateBlockStateVariants(List<Identifier> modelIds) {
            MultipartBlockStateSupplier supplier = MultipartBlockStateSupplier.create(vinesBlock);
            
            // For each texture set, create variants for all directions
            for (int setIdx = 0; setIdx < randomTextureSets.size(); setIdx++) {
                RandomTextureSet textureSet = randomTextureSets.get(setIdx);
                
                // Get model IDs for this set (side and top models)
                Identifier sideModelId = modelIds.get(setIdx * 2);     // base model
                Identifier topModelId = modelIds.get(setIdx * 2 + 1);  // top model
                
                // Create variants for each attachment direction
                addDirectionalVariants(supplier, sideModelId, topModelId, textureSet.weight);
            }
            
            return supplier;
        }
        
        private void addDirectionalVariants(MultipartBlockStateSupplier supplier, 
                                          Identifier sideModelId, Identifier topModelId, int weight) {
            // South attachment (base model, no rotation)
            BlockStateVariant southVariant = BlockStateVariant.create()
                    .put(VariantSettings.MODEL, sideModelId);
            if (weight > 1) {
                southVariant = southVariant.put(VariantSettings.WEIGHT, weight);
            }
            supplier.with(When.create().set(Properties.SOUTH, true), southVariant);
            
            // West attachment (base model, 90° rotation)
            BlockStateVariant westVariant = BlockStateVariant.create()
                    .put(VariantSettings.MODEL, sideModelId)
                    .put(VariantSettings.Y, VariantSettings.Rotation.R90);
            if (weight > 1) {
                westVariant = westVariant.put(VariantSettings.WEIGHT, weight);
            }
            supplier.with(When.create().set(Properties.WEST, true), westVariant);
            
            // North attachment (base model, 180° rotation)
            BlockStateVariant northVariant = BlockStateVariant.create()
                    .put(VariantSettings.MODEL, sideModelId)
                    .put(VariantSettings.Y, VariantSettings.Rotation.R180);
            if (weight > 1) {
                northVariant = northVariant.put(VariantSettings.WEIGHT, weight);
            }
            supplier.with(When.create().set(Properties.NORTH, true), northVariant);
            
            // East attachment (base model, 270° rotation)
            BlockStateVariant eastVariant = BlockStateVariant.create()
                    .put(VariantSettings.MODEL, sideModelId)
                    .put(VariantSettings.Y, VariantSettings.Rotation.R270);
            if (weight > 1) {
                eastVariant = eastVariant.put(VariantSettings.WEIGHT, weight);
            }
            supplier.with(When.create().set(Properties.EAST, true), eastVariant);
            
            // Up attachment (top model, no rotation)
            BlockStateVariant upVariant = BlockStateVariant.create()
                    .put(VariantSettings.MODEL, topModelId);
            if (weight > 1) {
                upVariant = upVariant.put(VariantSettings.WEIGHT, weight);
            }
            supplier.with(When.create().set(Properties.UP, true), upVariant);
            
            // Down attachment (top model, 180° X rotation)
            BlockStateVariant downVariant = BlockStateVariant.create()
                    .put(VariantSettings.MODEL, topModelId)
                    .put(VariantSettings.X, VariantSettings.Rotation.R180);
            if (weight > 1) {
                downVariant = downVariant.put(VariantSettings.WEIGHT, weight);
            }
            supplier.with(When.create().set(WCVinesBlock.DOWN, true), downVariant);
        }
        
        private void generateItemModel() {
            // Use first side texture for item model
            String firstTexture = randomTextureSets.get(0).textures[0];
            
            // Create simple generated item model
            TextureMap itemTextureMap = new TextureMap()
                    .put(TextureKey.LAYER0, WesterosBlocks.id("block/" + firstTexture));
            
            Models.GENERATED.upload(
                    Identifier.of("westerosblocks", "item/" + vinesName),
                    itemTextureMap,
                    generator.modelCollector
            );
        }
    }
    
    // Entry point for builder pattern
    public static VinesBlockBuilder generateVinesBlock(BlockStateModelGenerator generator, Block vinesBlock, String vinesName) {
        return new VinesBlockBuilder(generator, vinesBlock, vinesName);
    }
}