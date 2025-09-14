package com.westerosblocks.datagen.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.utils.ModProperties;
import net.minecraft.data.client.*;
import net.minecraft.block.Block;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CropBlockDatagen {
    private static Model createCropStageModel(int stage, boolean tinted) {
        String path = tinted ? "block/tinted/crop_stage_" + stage : "block/untinted/crop_stage_" + stage;
        return new Model(Optional.of(WesterosBlocks.id(path)), Optional.empty(), TextureKey.TEXTURE);
    }

    public static TextureMap createCropTextureMap(String cropName, int stage) {
        return new TextureMap()
            .put(TextureKey.TEXTURE, WesterosBlocks.id("block/" + cropName + "/" + cropName + "_stage_" + stage));
    }
    
    // Step 4: BlockStateSupplier for crop growth stages - following block-models.md #custom-supplier-method pattern
    public static VariantsBlockStateSupplier createCropBlockStates(Block cropBlock, String cropName, 
            Identifier stage0Model, Identifier stage1Model, Identifier stage2Model, Identifier stage3Model,
            Identifier stage4Model, Identifier stage5Model, Identifier stage6Model, Identifier stage7Model) {
        
        // Create a custom StateProperty with age values for this specific crop
        ModProperties.StateProperty cropStateProperty = new ModProperties.StateProperty(
            List.of("age0", "age1", "age2", "age3", "age4", "age5", "age6", "age7")
        );
        
        return VariantsBlockStateSupplier.create(cropBlock)
            .coordinate(BlockStateVariantMap.create(cropStateProperty)
                .register("age0", BlockStateVariant.create().put(VariantSettings.MODEL, stage0Model))
                .register("age1", BlockStateVariant.create().put(VariantSettings.MODEL, stage1Model))
                .register("age2", BlockStateVariant.create().put(VariantSettings.MODEL, stage2Model))
                .register("age3", BlockStateVariant.create().put(VariantSettings.MODEL, stage3Model))
                .register("age4", BlockStateVariant.create().put(VariantSettings.MODEL, stage4Model))
                .register("age5", BlockStateVariant.create().put(VariantSettings.MODEL, stage5Model))
                .register("age6", BlockStateVariant.create().put(VariantSettings.MODEL, stage6Model))
                .register("age7", BlockStateVariant.create().put(VariantSettings.MODEL, stage7Model))
            );
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
            
            // Create StateProperty with all state IDs
            List<String> stateIDs = states.stream().map(state -> state.stateID).collect(Collectors.toList());
            
            if (stateIDs.isEmpty()) {
                throw new IllegalStateException("StateIDs list is empty for crop block " + cropBlock);
            }
            
            ModProperties.StateProperty cropStateProperty = new ModProperties.StateProperty(stateIDs);
            
            // Generate models and variants
            List<Identifier> modelIds = new ArrayList<>();
            BlockStateVariantMap.SingleProperty<String> variantMap = BlockStateVariantMap.create(cropStateProperty);
            
            for (int i = 0; i < states.size(); i++) {
                StateVariant state = states.get(i);
                
                // Create texture map for this state (use first texture if multiple)
                TextureMap textureMap = new TextureMap()
                        .put(TextureKey.TEXTURE, WesterosBlocks.id("block/" + state.textures[0]));
                
                // Generate model identifier
                Identifier modelId = createCropStageModel(i, isTinted)
                        .upload(cropBlock, "_" + state.stateID, textureMap, generator.modelCollector);
                
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
