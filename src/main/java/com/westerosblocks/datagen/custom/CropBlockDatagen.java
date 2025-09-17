package com.westerosblocks.datagen.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.utils.ModProperties;
import net.minecraft.data.client.*;
import net.minecraft.block.Block;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CropBlockDatagen {
    
    // Parent Block Model - following block-models.md #parent-block-model pattern
    private static Model createCropStageModel(boolean tinted) {
        String path = tinted ? "block/tinted/crop" : "block/untinted/crop";
        return new Model(Optional.of(WesterosBlocks.id(path)), Optional.empty(), TextureKey.CROP);
    }

    // Builder pattern for crop block generation
    public static class CropBlockBuilder {
        private final BlockStateModelGenerator generator;
        private final Block cropBlock;
        private final String cropName;
        private boolean isTinted = false;
        private boolean isLayerSensitive = false;
        private final List<StateVariant> states = new ArrayList<>();
        private final List<String> randomTextures = new ArrayList<>();

        private static final String[] LAYER_CONDITIONS = {
                "layers=8", "layers=1", "layers=2", "layers=3",
                "layers=4", "layers=5", "layers=6", "layers=7"
        };
        
        // Inner class to hold state variant information
        public static class StateVariant {
            public final String stateID;
            public final String[] textures;
            public final boolean doRandomTextures;
            
            public StateVariant(String stateID, String[] textures, boolean doRandomTextures) {
                this.stateID = stateID;
                this.textures = textures;
                this.doRandomTextures = doRandomTextures;
            }
        }
        
        public CropBlockBuilder(BlockStateModelGenerator generator, Block cropBlock, String cropName) {
            this.generator = generator;
            this.cropBlock = cropBlock;
            this.cropName = cropName;
        }
        
        public CropBlockBuilder isTinted() {
            this.isTinted = true;
            return this;
        }
        
        public CropBlockBuilder isLayerSensitive() {
            this.isLayerSensitive = true;
            return this;
        }
        
        public CropBlockBuilder addState(String stateID, String... textures) {
            this.states.add(new StateVariant(stateID, textures, false));
            return this;
        }
        
        public CropBlockBuilder addStateRandomTextures(String stateID, String... textures) {
            this.states.add(new StateVariant(stateID, textures, true));
            return this;
        }
        
        public CropBlockBuilder addRandomTexture(String texturePath) {
            this.randomTextures.add(texturePath);
            return this;
        }
        
        public void build() {
            if (states.isEmpty() && randomTextures.isEmpty()) {
                throw new IllegalStateException("No states or random textures defined for crop block " + cropBlock);
            }
            
            List<Identifier> modelIds = new ArrayList<>();
            VariantsBlockStateSupplier blockStateSupplier;
            
            if (!randomTextures.isEmpty() && states.isEmpty()) {
                blockStateSupplier = generateRandomTextureModels(modelIds);
            } else {
                blockStateSupplier = generateStateBasedModels(modelIds);
            }

            blockStateSupplier = applyLayerSensitivity(blockStateSupplier);

            generator.blockStateCollector.accept(blockStateSupplier);

            if (!modelIds.isEmpty()) {
                generator.registerParentedItemModel(cropBlock, modelIds.get(0));
            }
        }
        
        private VariantsBlockStateSupplier generateRandomTextureModels(List<Identifier> modelIds) {
            for (int i = 0; i < randomTextures.size(); i++) {
                String texturePath = randomTextures.get(i);
                
                TextureMap textureMap = new TextureMap()
                        .put(TextureKey.CROP, WesterosBlocks.id("block/" + texturePath));
                
                String modelSuffix = "_v" + (i + 1);
                Identifier modelId = createCropStageModel(isTinted)
                        .upload(cropBlock, modelSuffix, textureMap, generator.modelCollector);
                
                modelIds.add(modelId);
            }
            
            List<BlockStateVariant> variants = modelIds.stream()
                    .map(modelId -> BlockStateVariant.create().put(VariantSettings.MODEL, modelId))
                    .collect(Collectors.toList());
            
            return VariantsBlockStateSupplier.create(cropBlock, variants.toArray(new BlockStateVariant[0]));
        }
        
        private VariantsBlockStateSupplier generateStateBasedModels(List<Identifier> modelIds) {
            // Get the STATE property that should already be defined on the block
            ModProperties.StateProperty blockStateProperty = null;
            
            for (var property : cropBlock.getStateManager().getProperties()) {
                if (property instanceof ModProperties.StateProperty stateProperty && "state".equals(property.getName())) {
                    blockStateProperty = stateProperty;
                    break;
                }
            }
            
            if (blockStateProperty == null) {
                throw new IllegalStateException("Block " + cropBlock + " does not have a STATE property defined. " +
                    "Make sure the block was created with stateValues parameter.");
            }
            
            // Validate that all our state IDs exist in the block's property
            List<String> stateIDs = states.stream().map(state -> state.stateID).collect(Collectors.toList());
            Collection<String> blockStateValues = blockStateProperty.getValues();
            
            for (String stateID : stateIDs) {
                if (!blockStateValues.contains(stateID)) {
                    throw new IllegalStateException("State '" + stateID + "' is not defined in block's STATE property. " +
                        "Available states: " + blockStateValues);
                }
            }
            
            // Generate models and variants
            BlockStateVariantMap.SingleProperty<String> variantMap = BlockStateVariantMap.create(blockStateProperty);
            
            for (StateVariant state : states) {
                if (state.doRandomTextures && state.textures.length > 1) {
                    // Generate multiple models with random textures
                    List<Identifier> stateModelIds = new ArrayList<>();
                    for (int j = 0; j < state.textures.length; j++) {
                        TextureMap textureMap = new TextureMap()
                                .put(TextureKey.CROP, WesterosBlocks.id("block/" + state.textures[j]));
                        
                        String modelSuffix = "/" + cropName + "_" + state.stateID + "_v" + (j + 1);
                        Identifier modelId = createCropStageModel(isTinted)
                                .upload(cropBlock, modelSuffix, textureMap, generator.modelCollector);
                        
                        stateModelIds.add(modelId);
                    }
                    
                    List<BlockStateVariant> variants = stateModelIds.stream()
                            .map(modelId -> BlockStateVariant.create().put(VariantSettings.MODEL, modelId))
                            .collect(Collectors.toList());
                    variantMap.register(state.stateID, variants);
                    
                    modelIds.addAll(stateModelIds);
                } else {
                    // Single texture model
                    TextureMap textureMap = new TextureMap()
                            .put(TextureKey.CROP, WesterosBlocks.id("block/" + state.textures[0]));
                    
                    String modelSuffix = "/" + cropName + "_" + state.stateID;
                    Identifier modelId = createCropStageModel(isTinted)
                            .upload(cropBlock, modelSuffix, textureMap, generator.modelCollector);
                    
                    modelIds.add(modelId);
                    variantMap.register(state.stateID, BlockStateVariant.create().put(VariantSettings.MODEL, modelId));
                }
            }
            
            return VariantsBlockStateSupplier.create(cropBlock).coordinate(variantMap);
        }
        
        private VariantsBlockStateSupplier applyLayerSensitivity(VariantsBlockStateSupplier blockStateSupplier) {
            if (isLayerSensitive) {
                BlockStateVariantMap.SingleProperty<Integer> layerMap = BlockStateVariantMap.create(Properties.LAYERS);
                for (String layerCondition : LAYER_CONDITIONS) {
                    int layerValue = Integer.parseInt(layerCondition.split("=")[1]);
                    layerMap.register(layerValue, BlockStateVariant.create());
                }
                return blockStateSupplier.coordinate(layerMap);
            }
            return blockStateSupplier;
        }
    }
    
    // Entry point for builder pattern
    public static CropBlockBuilder generateCropBlock(BlockStateModelGenerator generator, Block cropBlock, String cropName) {
        return new CropBlockBuilder(generator, cropBlock, cropName);
    }
    
}