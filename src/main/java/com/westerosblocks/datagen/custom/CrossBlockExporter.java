package com.westerosblocks.datagen.custom;

import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.custom.WCPlantBlock;

import java.util.Arrays;
import java.util.Optional;

/**
 * Exporter for cross-shaped blocks following block-models.md patterns.
 * Generates models for plants, webs, and other decorative cross-pattern blocks with optional layer sensitivity.
 *
 * <p>Structure follows block-models.md sections 5.2-5.6:
 * <ul>
 *   <li>Model instances (cross template with tinted/untinted variants)</li>
 *   <li>TextureMap builders (CROSS texture key)</li>
 *   <li>BlockStateSupplier methods (VariantsBlockStateSupplier or layer-based variants)</li>
 *   <li>Clean datagen methods (generateCross, generateLayerSensitiveCross)</li>
 *   <li>BlockDefinition integration (registerCrossBlockFromDefinition)</li>
 * </ul>
 *
 * <p><b>Cross Block Variants:</b>
 * <ul>
 *   <li><b>Simple Cross:</b> Single texture with optional rotations (1 or 4 variants)</li>
 *   <li><b>Random Textures:</b> Multiple texture variants with rotations (N textures × 4 rotations)</li>
 *   <li><b>Layer Sensitive:</b> Height-based variants (layers 1-8) with textures and rotations</li>
 *   <li><b>State-Based:</b> Complex blocks with multiple states (smoke, cobweb variants)</li>
 * </ul>
 *
 * <p><b>Layer Sensitivity:</b>
 * <ul>
 *   <li>Layer 1-7: Uses cross_layer1 through cross_layer7 templates (partial height)</li>
 *   <li>Layer 8: Uses standard cross template (full height)</li>
 *   <li>Each layer supports texture variants and rotations</li>
 * </ul>
 *
 * <p><b>Tinting Support:</b>
 * <ul>
 *   <li>Tinted: Uses block/tinted/cross or block/tinted/cross_layerN templates</li>
 *   <li>Untinted: Uses block/untinted/cross or block/untinted/cross_layerN templates</li>
 *   <li>Determined by BlockDefinition.hasColorMult()</li>
 * </ul>
 *
 * <p><b>Texture Order:</b> Single CROSS texture key
 *
 * @see WCPlantBlock
 */
public class CrossBlockExporter extends BaseBlockExporter {
    public static void generateCross(BlockStateModelGenerator generator, Block block, String texturePath, boolean isTinted, int rotationCount) {
        // Create the cross model
        Identifier modelId = createCrossModel(generator, block, texturePath, isTinted);
        
        // Create variants for different rotations
        if (rotationCount == 1) {
            // Single variant - no rotation
            BlockStateVariant variant = createVariant(modelId);
            generator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(block, variant)
            );
        } else {
            // Multiple variants with rotations
            BlockStateVariant[] variants = new BlockStateVariant[rotationCount];
            for (int rotation = 0; rotation < rotationCount; rotation++) {
                variants[rotation] = createVariant(modelId, rotation * 90);
            }
            generator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(block, variants)
            );
        }

        // Register item model
        generator.registerParentedItemModel(block, modelId);
    }

    public static void generateCrossWithRandomTextures(BlockStateModelGenerator generator, Block block, String[] texturePaths, boolean isTinted, int rotationCount) {
        if (texturePaths.length == 0) {
            throw new IllegalArgumentException("At least one texture path is required");
        }

        String blockName = getBlockName(block);
        BlockStateVariant[] allVariants = new BlockStateVariant[texturePaths.length * rotationCount];
        int variantIndex = 0;

        Identifier firstModelId = null;

        // Create models for each texture
        for (int textureIndex = 0; textureIndex < texturePaths.length; textureIndex++) {
            String texturePath = texturePaths[textureIndex];
            
            // Create model for this texture variant
            Identifier nestedModelId = WesterosBlocks.id("block/" + blockName + "/" + blockName + "_v" + (textureIndex + 1));
            Identifier modelId = createCrossModelWithId(generator, nestedModelId, texturePath, isTinted);
            
            if (firstModelId == null) {
                firstModelId = modelId;
            }

            // Create rotations for this texture
            for (int rotation = 0; rotation < rotationCount; rotation++) {
                if (rotation == 0) {
                    allVariants[variantIndex] = createVariant(modelId);
                } else {
                    allVariants[variantIndex] = createVariant(modelId, rotation * 90);
                }
                variantIndex++;
            }
        }

        generator.blockStateCollector.accept(
            VariantsBlockStateSupplier.create(block, allVariants)
        );

        // Register item model using the first texture variant
        if (firstModelId != null) {
            generator.registerParentedItemModel(block, firstModelId);
        }
    }


    private static Identifier createCrossModel(BlockStateModelGenerator generator, Block block, String texturePath, boolean isTinted) {
        Identifier modelId = createModelId(block);
        return createCrossModelWithId(generator, modelId, texturePath, isTinted);
    }

    private static Identifier createCrossModelWithId(BlockStateModelGenerator generator, Identifier modelId, String texturePath, boolean isTinted) {
        String parentPath = isTinted ? "block/tinted/cross" : "block/untinted/cross";
        Identifier textureId = createBlockIdentifier(texturePath);
        
        TextureMap textureMap = new TextureMap()
            .put(TextureKey.CROSS, textureId);
        
        net.minecraft.data.client.Model model = new net.minecraft.data.client.Model(
            Optional.of(WesterosBlocks.id(parentPath)),
            Optional.empty(),
            TextureKey.CROSS
        );
        
        model.upload(modelId, textureMap, generator.modelCollector);
        return modelId;
    }

    /**
     * Generates block state models for layer-sensitive cross blocks with multiple random textures and rotations.
     * Creates array-based blockstate variants for each layer with all texture variants.
     * 
     * @param generator The BlockStateModelGenerator to use
     * @param block The layer-sensitive plant block to generate models for
     * @param texturePaths Array of texture paths for random variants
     * @param isTinted Whether the block should be tinted
     * @param rotationCount Number of random rotations per texture (1 or 4)
     */
    public static void generateLayerSensitiveCrossWithRandomTextures(BlockStateModelGenerator generator, Block block, String[] texturePaths, boolean isTinted, int rotationCount) {
        if (texturePaths.length == 0) {
            throw new IllegalArgumentException("At least one texture path is required");
        }

        // Verify this is actually a layer-sensitive plant
        if (!(block instanceof WCPlantBlock) || !((WCPlantBlock) block).isLayerSensitive()) {
            // Fall back to regular cross generation with random textures
            generateCrossWithRandomTextures(generator, block, texturePaths, isTinted, rotationCount);
            return;
        }

        String blockName = getBlockName(block);
        
        // Create models for each layer (1-8) and each texture variant
        Identifier[][] layerTextureModels = new Identifier[9][texturePaths.length]; // [layer][textureIndex]
        
        for (int layer = 1; layer <= 8; layer++) {
            for (int textureIndex = 0; textureIndex < texturePaths.length; textureIndex++) {
                String layerSuffix = (layer == 8) ? "" : "_layer" + layer;
                String textureSuffix = "_v" + (textureIndex + 1);
                String parentPath;
                
                // Layer 8 (full height) uses the regular cross template, not cross_layer8
                if (layer == 8) {
                    parentPath = isTinted ? "block/tinted/cross" : "block/untinted/cross";
                } else {
                    parentPath = isTinted ? "block/tinted/cross_layer" + layer : "block/untinted/cross_layer" + layer;
                }
                
                Identifier modelId = WesterosBlocks.id("block/" + blockName + "/base" + layerSuffix + textureSuffix);
                Identifier textureId = createBlockIdentifier(texturePaths[textureIndex]);
                
                TextureMap textureMap = new TextureMap()
                    .put(TextureKey.CROSS, textureId);
                
                Model model = new Model(
                    Optional.of(WesterosBlocks.id(parentPath)),
                    Optional.empty(),
                    TextureKey.CROSS
                );
                
                model.upload(modelId, textureMap, generator.modelCollector);
                layerTextureModels[layer][textureIndex] = modelId;
            }
        }
        
        // Create blockstate with layer variants as arrays
        BlockStateVariantMap layerMap = BlockStateVariantMap.create(Properties.LAYERS);
        
        for (int layer = 1; layer <= 8; layer++) {
            // Create array of variants for this layer (all texture variants with rotations)
            BlockStateVariant[] allVariantsForLayer = new BlockStateVariant[texturePaths.length * rotationCount];
            int variantIndex = 0;
            
            for (int textureIndex = 0; textureIndex < texturePaths.length; textureIndex++) {
                Identifier modelId = layerTextureModels[layer][textureIndex];
                
                for (int rotation = 0; rotation < rotationCount; rotation++) {
                    BlockStateVariant variant = BlockStateVariant.create()
                        .put(VariantSettings.MODEL, modelId);
                    
                    if (rotation > 0) {
                        variant = variant.put(VariantSettings.Y, VariantSettings.Rotation.valueOf("R" + (rotation * 90)));
                    }
                    
                    allVariantsForLayer[variantIndex] = variant;
                    variantIndex++;
                }
            }
            
            ((BlockStateVariantMap.SingleProperty<Integer>) layerMap).register(layer, Arrays.asList(allVariantsForLayer));
        }
        
        generator.blockStateCollector.accept(
            VariantsBlockStateSupplier.create(block)
                .coordinate(layerMap)
        );

        // Register item model using the first texture variant at full height (layer 8)
        generator.registerParentedItemModel(block, layerTextureModels[8][0]);
    }

    /**
     * Generates block state models for layer-sensitive cross blocks with rotation variants.
     * 
     * @param generator The BlockStateModelGenerator to use
     * @param block The layer-sensitive plant block to generate models for
     * @param texturePath The texture path to use
     * @param isTinted Whether the block should be tinted
     * @param rotationCount Number of random rotations (1 or 4)
     */
    public static void generateLayerSensitiveCross(BlockStateModelGenerator generator, Block block, String texturePath, boolean isTinted, int rotationCount) {
        // Verify this is actually a layer-sensitive plant
        if (!(block instanceof WCPlantBlock) || !((WCPlantBlock) block).isLayerSensitive()) {
            // Fall back to regular cross generation
            generateCross(generator, block, texturePath, isTinted, rotationCount);
            return;
        }

        String blockName = getBlockName(block);
        
        // Create models for each layer (1-8)
        Identifier[] layerModels = new Identifier[9]; // Index 0 unused, 1-8 for layers
        
        for (int layer = 1; layer <= 8; layer++) {
            String layerSuffix = "_layer" + layer;
            String parentPath;
            
            // Layer 8 (full height) uses the regular cross template, not cross_layer8
            if (layer == 8) {
                parentPath = isTinted ? "block/tinted/cross" : "block/untinted/cross";
            } else {
                parentPath = isTinted ? "block/tinted/cross_layer" + layer : "block/untinted/cross_layer" + layer;
            }
            
            Identifier modelId = WesterosBlocks.id("block/" + blockName + "/" + blockName + layerSuffix);
            Identifier textureId = createBlockIdentifier(texturePath);
            
            TextureMap textureMap = new TextureMap()
                .put(TextureKey.CROSS, textureId);
            
            Model model = new Model(
                Optional.of(WesterosBlocks.id(parentPath)),
                Optional.empty(),
                TextureKey.CROSS
            );
            
            model.upload(modelId, textureMap, generator.modelCollector);
            layerModels[layer] = modelId;
        }
        
        // Create blockstate with layer variants
        BlockStateVariantMap layerMap = BlockStateVariantMap.create(Properties.LAYERS);
        
        for (int layer = 1; layer <= 8; layer++) {
            if (rotationCount == 1) {
                // Single variant per layer
                ((BlockStateVariantMap.SingleProperty<Integer>) layerMap).register(layer, 
                    BlockStateVariant.create().put(VariantSettings.MODEL, layerModels[layer])
                );
            } else {
                // Multiple rotations per layer
                BlockStateVariant[] rotationVariants = new BlockStateVariant[rotationCount];
                for (int rotation = 0; rotation < rotationCount; rotation++) {
                    BlockStateVariant variant = BlockStateVariant.create()
                        .put(VariantSettings.MODEL, layerModels[layer]);
                    
                    if (rotation > 0) {
                        variant = variant.put(VariantSettings.Y, VariantSettings.Rotation.valueOf("R" + (rotation * 90)));
                    }
                    
                    rotationVariants[rotation] = variant;
                }
                ((BlockStateVariantMap.SingleProperty<Integer>) layerMap).register(layer, Arrays.asList(rotationVariants));
            }
        }
        
        generator.blockStateCollector.accept(
            VariantsBlockStateSupplier.create(block)
                .coordinate(layerMap)
        );

        // Register item model using layer 8 (full height)
        generator.registerParentedItemModel(block, layerModels[8]);
    }

    /**
     * Registers a cross block using BlockDefinition (for web blocks and other cross blocks)
     */
    public static void registerCrossBlockFromDefinition(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        if (definition.getTextures() == null || definition.getTextures().isEmpty()) {
            if ((definition.getStates() == null || definition.getStates().isEmpty()) &&
                (definition.getRandomTextures() == null || definition.getRandomTextures().isEmpty())) {
                throw new IllegalArgumentException("Cross blocks require textures, states, or randomTextures");
            }
        }

        // Check for color multiplier to determine if tinted
        boolean isTinted = definition.hasColorMult();

        // Check if this is a layer-sensitive plant
        boolean isLayerSensitive = "layerSensitive".equals(definition.getType()) ||
                                   (definition.getType() != null && definition.getType().contains("layerSensitive"));

        if (definition.hasStates()) {
            // Handle complex state-based blocks (like smoke, cobweb)
            handleStatesBasedCrossBlock(generator, block, definition, isTinted, isLayerSensitive);
        } else if (definition.getRandomTextures() != null && !definition.getRandomTextures().isEmpty()) {
            // Handle random texture blocks
            String[] texturePaths = definition.getRandomTextures().stream()
                .flatMap(randomTexture -> randomTexture.getTextures().stream())
                .toArray(String[]::new);

            if (isLayerSensitive) {
                generateLayerSensitiveCrossWithRandomTextures(generator, block, texturePaths, isTinted, 4);
            } else {
                generateCrossWithRandomTextures(generator, block, texturePaths, isTinted, 4);
            }
        } else {
            // Handle simple texture blocks
            String texturePath = definition.getTextures().get(0);

            if (isLayerSensitive) {
                generateLayerSensitiveCross(generator, block, texturePath, isTinted, 1);
            } else {
                generateCross(generator, block, texturePath, isTinted, 1);
            }
        }
    }

    /**
     * Handles blocks with complex states structure (like smoke, cobweb)
     */
    private static void handleStatesBasedCrossBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition, boolean isTinted, boolean isLayerSensitive) {
        // For blocks with states, we need to look for the "random" state or use the first available state
        for (BlockDefinition.StateVariant state : definition.getStates()) {
            if ("random".equals(state.getStateID()) && state.hasRandomTextures()) {
                // Handle random state with random textures
                String[] texturePaths = state.getRandomTextures().stream()
                    .flatMap(randomTexture -> randomTexture.getTextures().stream())
                    .toArray(String[]::new);

                if (isLayerSensitive) {
                    generateLayerSensitiveCrossWithRandomTextures(generator, block, texturePaths, isTinted, 4);
                } else {
                    generateCrossWithRandomTextures(generator, block, texturePaths, isTinted, 4);
                }
                return;
            } else if (state.getTextures() != null && !state.getTextures().isEmpty()) {
                // Handle first found state with simple textures
                String texturePath = state.getTextures().get(0);

                if (isLayerSensitive) {
                    generateLayerSensitiveCross(generator, block, texturePath, isTinted, 1);
                } else {
                    generateCross(generator, block, texturePath, isTinted, 1);
                }
                return;
            }
        }

        // Fallback if no suitable state found
        if (definition.getTextures() != null && !definition.getTextures().isEmpty()) {
            String texturePath = definition.getTextures().get(0);

            if (isLayerSensitive) {
                generateLayerSensitiveCross(generator, block, texturePath, isTinted, 1);
            } else {
                generateCross(generator, block, texturePath, isTinted, 1);
            }
        }
    }
}
