package com.westerosblocks.datagen.models;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.datagen.ModelExport;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;

import java.util.*;
import java.util.Optional;

/**
 * Crop block export class that extends ModelExport2 for the builder-based
 * system.
 * Handles crop-specific model generation with support for layer sensitivity and
 * rotations.
 */
public class CropBlockExport2 extends ModelExport2 {

    /**
     * Generates block state models for crop blocks.
     * 
     * @param generator   The BlockStateModelGenerator to use
     * @param block       The crop block to generate models for
     * @param texturePath The texture path for the crop
     */
    @Override
    public void generateBlockStateModels(BlockStateModelGenerator generator, Block block, String texturePath) {
        generateBlockStateModels(generator, block, texturePath, false, 1);
    }

    /**
     * Generates block state models for crop blocks with additional parameters.
     * 
     * @param generator      The BlockStateModelGenerator to use
     * @param block          The crop block to generate models for
     * @param texturePath    The texture path for the crop
     * @param layerSensitive Whether the crop supports layers
     * @param rotationCount  Number of random rotations (1-4)
     */
    public void generateBlockStateModels(BlockStateModelGenerator generator, Block block, String texturePath,
            boolean layerSensitive, int rotationCount) {

        // Auto-detect if the block has LAYERS property (fallback)
        boolean hasLayersProperty = block.getDefaultState().contains(Properties.LAYERS);
        if (hasLayersProperty) {
            layerSensitive = true;
        }

        // Use BlockStateBuilder like the original CropBlockExport
        ModelExport.BlockStateBuilder blockStateBuilder = new ModelExport.BlockStateBuilder(block);
        final Map<String, List<BlockStateVariant>> variants = blockStateBuilder.getVariants();

        if (layerSensitive) {
            // Generate layer-sensitive variants
            generateLayerSensitiveVariants(generator, block, texturePath, rotationCount, variants);
        } else {
            // Generate simple variants with rotations
            generateSimpleVariants(generator, block, texturePath, rotationCount, variants);
        }

        // Use the same generateBlockStateFiles method as the original
        ModelExport.generateBlockStateFiles(generator, block, variants);
    }

    /**
     * Generates layer-sensitive crop variants.
     */
    private void generateLayerSensitiveVariants(BlockStateModelGenerator generator, Block block,
            String texturePath, int rotationCount,
            Map<String, List<BlockStateVariant>> variants) {

        // For layer-sensitive crops, handle each layer separately
        // Follow the original pattern: layers=8 is base, layers=1-7 are layer-specific
        String[] layerConditions = {
                "layers=8", "layers=1", "layers=2", "layers=3", "layers=4",
                "layers=5", "layers=6", "layers=7"
        };

        for (int layer = 0; layer < layerConditions.length; layer++) {
            String layerCondition = layerConditions[layer];

            // Create model for this layer (layer 0 for base, layer 1-7 for layer-specific)
            int modelLayer = layer == 0 ? 0 : layer; // layer 0 = base crop, layer 1-7 = layer-specific
            Identifier modelId = createCropModel(generator, block, texturePath, false, modelLayer);

            // Generate variants for this layer
            for (int rot = 0; rot < rotationCount; rot++) {
                BlockStateVariant variant = BlockStateVariant.create();
                variant.put(VariantSettings.MODEL, modelId);

                if (rot > 0) {
                    variant.put(VariantSettings.Y, getRotation(90 * rot));
                }

                // Add variant directly to the map
                List<BlockStateVariant> existingVariants = variants.computeIfAbsent(layerCondition,
                        k -> new ArrayList<>());
                existingVariants.add(variant);
            }
        }
    }

    /**
     * Generates simple crop variants with rotations.
     */
    private void generateSimpleVariants(BlockStateModelGenerator generator, Block block,
            String texturePath, int rotationCount,
            Map<String, List<BlockStateVariant>> variants) {

        // Create the crop model
        Identifier modelId = createCropModel(generator, block, texturePath, false, 0);

        // Generate variants with rotations
        for (int rot = 0; rot < rotationCount; rot++) {
            BlockStateVariant variant = BlockStateVariant.create();
            variant.put(VariantSettings.MODEL, modelId);

            if (rot > 0) {
                variant.put(VariantSettings.Y, getRotation(90 * rot));
            }

            // Add variant directly to the map
            List<BlockStateVariant> existingVariants = variants.computeIfAbsent("", k -> new ArrayList<>());
            existingVariants.add(variant);
        }
    }

    /**
     * Creates a crop model with the specified parameters.
     * 
     * @param generator   The BlockStateModelGenerator to use
     * @param block       The crop block
     * @param texturePath The texture path
     * @param isTinted    Whether the crop should be tinted
     * @param layer       The layer number (0 for non-layer-sensitive, 1-8 for
     *                    layer-sensitive)
     * @return The created model identifier
     */
    private Identifier createCropModel(BlockStateModelGenerator generator, Block block,
            String texturePath, boolean isTinted, int layer) {

        // Use the correct parent model names
        String parentPath = isTinted ? "block/tinted/crop" : "block/untinted/crop";
        if (layer > 0) {
            parentPath += "_layer" + layer;
        }

        // Use the proper model ID pattern like the original
        String modelName = layer > 0 ? "base_layer" + layer : "base";
        String blockName = getBlockName(block);
        Identifier modelId = WesterosBlocks.id(String.format("block/generated/%s/%s_v1", blockName, modelName));
        Identifier textureId = createBlockIdentifier(texturePath);

        TextureMap textureMap = new TextureMap()
                .put(TextureKey.CROP, textureId);

        Model model = new Model(
                Optional.of(WesterosBlocks.id(parentPath)),
                Optional.empty(),
                TextureKey.CROP);

        model.upload(modelId, textureMap, generator.modelCollector);
        return modelId;
    }

    /**
     * Gets the block name from the block's translation key.
     */
    private String getBlockName(Block block) {
        String translationKey = block.getTranslationKey();
        if (translationKey.startsWith("block.westerosblocks.")) {
            return translationKey.substring("block.westerosblocks.".length());
        }
        return block.getTranslationKey();
    }

    /**
     * Generates item models for crop blocks.
     * 
     * @param generator The ItemModelGenerator to use
     * @param block     The crop block to generate item models for
     */
    @Override
    public void generateItemModels(ItemModelGenerator generator, Block block) {
        generateItemModel(generator, block);
    }

    /**
     * Generates item models for crop blocks with a specific texture.
     * 
     * @param generator   The ItemModelGenerator to use
     * @param block       The crop block to generate item models for
     * @param texturePath The texture path to use for the item model
     */
    public void generateItemModels(ItemModelGenerator generator, Block block, String texturePath) {
        TextureMap textureMap = TextureMap.layer0(createBlockIdentifier(texturePath));

        Models.GENERATED.upload(
                ModelIds.getItemModelId(block.asItem()),
                textureMap,
                generator.writer);
    }
}