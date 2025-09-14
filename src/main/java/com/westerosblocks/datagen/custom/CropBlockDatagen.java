package com.westerosblocks.datagen.custom;

import com.westerosblocks.WesterosBlocks;
import net.minecraft.data.client.*;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;

import java.util.Optional;

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
        
        return VariantsBlockStateSupplier.create(cropBlock)
            .coordinate(BlockStateVariantMap.create(net.minecraft.state.property.Properties.AGE_7)
                .register(0, BlockStateVariant.create().put(VariantSettings.MODEL, stage0Model))
                .register(1, BlockStateVariant.create().put(VariantSettings.MODEL, stage1Model))
                .register(2, BlockStateVariant.create().put(VariantSettings.MODEL, stage2Model))
                .register(3, BlockStateVariant.create().put(VariantSettings.MODEL, stage3Model))
                .register(4, BlockStateVariant.create().put(VariantSettings.MODEL, stage4Model))
                .register(5, BlockStateVariant.create().put(VariantSettings.MODEL, stage5Model))
                .register(6, BlockStateVariant.create().put(VariantSettings.MODEL, stage6Model))
                .register(7, BlockStateVariant.create().put(VariantSettings.MODEL, stage7Model))
            );
    }
    
    // Builder pattern for crop block generation
    public static class CropBlockBuilder {
        private final BlockStateModelGenerator generator;
        private final Block cropBlock;
        private final String cropName;
        private boolean isTinted = false;
        private boolean isLayerSensitive = false;
        
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
        
        public void build() {
            // Generate model identifiers using dynamically created models based on tinted option
            // Each stage needs a unique suffix to avoid duplicate model definitions
            Identifier stage0ModelId = createCropStageModel(0, isTinted).upload(cropBlock, "_stage0", createCropTextureMap(cropName, 0), generator.modelCollector);
            Identifier stage1ModelId = createCropStageModel(1, isTinted).upload(cropBlock, "_stage1", createCropTextureMap(cropName, 1), generator.modelCollector);
            Identifier stage2ModelId = createCropStageModel(2, isTinted).upload(cropBlock, "_stage2", createCropTextureMap(cropName, 2), generator.modelCollector);
            Identifier stage3ModelId = createCropStageModel(3, isTinted).upload(cropBlock, "_stage3", createCropTextureMap(cropName, 3), generator.modelCollector);
            Identifier stage4ModelId = createCropStageModel(4, isTinted).upload(cropBlock, "_stage4", createCropTextureMap(cropName, 4), generator.modelCollector);
            Identifier stage5ModelId = createCropStageModel(5, isTinted).upload(cropBlock, "_stage5", createCropTextureMap(cropName, 5), generator.modelCollector);
            Identifier stage6ModelId = createCropStageModel(6, isTinted).upload(cropBlock, "_stage6", createCropTextureMap(cropName, 6), generator.modelCollector);
            Identifier stage7ModelId = createCropStageModel(7, isTinted).upload(cropBlock, "_stage7", createCropTextureMap(cropName, 7), generator.modelCollector);
            
            // TODO: Apply layer sensitive options when implementing
            // if (isLayerSensitive) { ... }
            
            // Pass those models into createCropBlockStates
            VariantsBlockStateSupplier blockStateSupplier = createCropBlockStates(cropBlock, cropName,
                    stage0ModelId, stage1ModelId, stage2ModelId, stage3ModelId,
                    stage4ModelId, stage5ModelId, stage6ModelId, stage7ModelId);
            
            // BlockStateSupplier gets passed into the blockStateCollector
            generator.blockStateCollector.accept(blockStateSupplier);
            
            // Create a model for the crop item
            generator.registerParentedItemModel(cropBlock, stage0ModelId);
        }
    }
    
    // Entry point for builder pattern
    public static CropBlockBuilder generateCropBlock(BlockStateModelGenerator generator, Block cropBlock, String cropName) {
        return new CropBlockBuilder(generator, cropBlock, cropName);
    }
    
}
