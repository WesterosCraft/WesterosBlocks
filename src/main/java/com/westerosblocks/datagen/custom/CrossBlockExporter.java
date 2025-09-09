package com.westerosblocks.datagen.custom;

import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.BlockStateVariant;
import net.minecraft.data.client.Models;
import net.minecraft.data.client.TextureKey;
import net.minecraft.data.client.TextureMap;
import net.minecraft.data.client.VariantsBlockStateSupplier;
import net.minecraft.util.Identifier;
import com.westerosblocks.WesterosBlocks;
import java.util.Optional;

/**
 * Exporter for cross/plant blocks following the unified builder pattern.
 * Generates cross models for decorative plants and vegetation blocks.
 */
public class CrossBlockExporter extends BaseBlockExporter {

    /**
     * Generates block state models for cross blocks with a single texture.
     * 
     * @param generator The BlockStateModelGenerator to use
     * @param block The block to generate models for
     * @param texturePath The texture path to use
     */
    public static void generateCross(BlockStateModelGenerator generator, Block block, String texturePath) {
        generateCross(generator, block, texturePath, false, 1);
    }

    /**
     * Generates block state models for cross blocks with rotation variants.
     * 
     * @param generator The BlockStateModelGenerator to use
     * @param block The block to generate models for
     * @param texturePath The texture path to use
     * @param isTinted Whether the block should be tinted
     * @param rotationCount Number of random rotations (1 or 4)
     */
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

    /**
     * Generates block state models for cross blocks with multiple random texture variants.
     * 
     * @param generator The BlockStateModelGenerator to use
     * @param block The block to generate models for
     * @param texturePaths Array of texture paths for random variants
     */
    public static void generateCrossWithRandomTextures(BlockStateModelGenerator generator, Block block, String[] texturePaths) {
        generateCrossWithRandomTextures(generator, block, texturePaths, false, 1);
    }

    /**
     * Generates block state models for cross blocks with multiple random texture variants and rotations.
     * 
     * @param generator The BlockStateModelGenerator to use
     * @param block The block to generate models for
     * @param texturePaths Array of texture paths for random variants
     * @param isTinted Whether the block should be tinted
     * @param rotationCount Number of random rotations per texture (1 or 4)
     */
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

    /**
     * Creates a cross model with the specified texture.
     * 
     * @param generator The BlockStateModelGenerator to use
     * @param block The block to create a model for
     * @param texturePath The texture path to use
     * @param isTinted Whether the block should be tinted
     * @return The created model identifier
     */
    private static Identifier createCrossModel(BlockStateModelGenerator generator, Block block, String texturePath, boolean isTinted) {
        Identifier modelId = createModelId(block);
        return createCrossModelWithId(generator, modelId, texturePath, isTinted);
    }

    /**
     * Creates a cross model with the specified model ID and texture.
     * 
     * @param generator The BlockStateModelGenerator to use
     * @param modelId The model identifier to use
     * @param texturePath The texture path to use
     * @param isTinted Whether the block should be tinted
     * @return The created model identifier
     */
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
}
