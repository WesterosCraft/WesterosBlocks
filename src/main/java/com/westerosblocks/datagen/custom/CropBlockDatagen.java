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

        private static final String[] LAYER_CONDITIONS = {
                "layers=8", "layers=1", "layers=2", "layers=3",
                "layers=4", "layers=5", "layers=6", "layers=7"
        };
        
        // Inner class to hold state variant information
        public static class StateVariant {
            public final String stateID;
            public final String[] textures;
            
            public StateVariant(String stateID, String[] textures) {
                this.stateID = stateID;
                this.textures = textures;
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
            this.states.add(new StateVariant(stateID, textures));
            return this;
        }
        
        public void build() {
            if (states.isEmpty()) {
                throw new IllegalStateException("No states defined for crop block " + cropBlock);
            }
            
            // Get the STATE property that should already be defined on the block
            // The block must have been created with stateValues parameter for this to work
            ModProperties.StateProperty blockStateProperty = null;
            
            // Find the STATE property from the block's state definition
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
            List<Identifier> modelIds = new ArrayList<>();
            BlockStateVariantMap.SingleProperty<String> variantMap = BlockStateVariantMap.create(blockStateProperty);
            
            for (int i = 0; i < states.size(); i++) {
                StateVariant state = states.get(i);
                
                // Create texture map for this state (use first texture if multiple)
                TextureMap textureMap = new TextureMap()
                        .put(TextureKey.CROP, WesterosBlocks.id("block/" + state.textures[0]));
                
                // Generate model identifier with block name in path
                String modelSuffix = "/" + cropName + "_" + state.stateID;
                Identifier modelId = createCropStageModel(isTinted)
                        .upload(cropBlock, modelSuffix, textureMap, generator.modelCollector);
                
                modelIds.add(modelId);
                
                // Register variant
                variantMap.register(state.stateID, BlockStateVariant.create().put(VariantSettings.MODEL, modelId));
            }
            
            // Create blockstate supplier
            VariantsBlockStateSupplier blockStateSupplier = VariantsBlockStateSupplier.create(cropBlock)
                    .coordinate(variantMap);
            
            // Apply layer sensitive options if enabled
            if (isLayerSensitive) {
                // Add layer variants by coordinating with LAYERS property
                BlockStateVariantMap.SingleProperty<Integer> layerMap = BlockStateVariantMap.create(Properties.LAYERS);
                for (String layerCondition : LAYER_CONDITIONS) {
                    // Extract layer value from condition (e.g. "layers=8" -> 8)
                    int layerValue = Integer.parseInt(layerCondition.split("=")[1]);
                    layerMap.register(layerValue, BlockStateVariant.create());
                }
                blockStateSupplier = blockStateSupplier.coordinate(layerMap);
            }
            
            // BlockStateSupplier gets passed into the blockStateCollector
            generator.blockStateCollector.accept(blockStateSupplier);
            
            // Create a model for the crop item (use first model)
            if (!modelIds.isEmpty()) {
                generator.registerParentedItemModel(cropBlock, modelIds.get(0));
            }
        }
    }
    
    // Entry point for builder pattern
    public static CropBlockBuilder generateCropBlock(BlockStateModelGenerator generator, Block cropBlock, String cropName) {
        return new CropBlockBuilder(generator, cropBlock, cropName);
    }
    
}