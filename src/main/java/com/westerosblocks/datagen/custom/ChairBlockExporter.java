package com.westerosblocks.datagen.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.datagen.ModModels;
import com.westerosblocks.block.custom.WCChairBlock;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;
import net.minecraft.data.client.VariantSettings.Rotation;

import java.util.Optional;

/**
 * Chair block exporter for generating block states and models.
 * This class follows the same pattern as other exporters in the codebase.
 */
public class ChairBlockExporter {

    /**
     * Generates block state models for a chair block.
     * 
     * @param generator   The BlockStateModelGenerator to register models with
     * @param block       The chair block to generate models for
     * @param texturePath The texture path for the chair
     */
    public static void registerChairBlock(BlockStateModelGenerator generator, Block block, String texturePath) {
        // Create the base models for each chair variant using predefined ModModels
        Identifier cardinalModelId = createChairModel(generator, block, texturePath, "cardinal", ModModels.CHAIR);
        Identifier diagonalModelId = createChairModel(generator, block, texturePath, "diagonal", ModModels.CHAIR_45);

        // Create variants for all chair rotations using BlockStateModelGenerator patterns
        BlockStateVariantMap variants = BlockStateVariantMap.create(WCChairBlock.ROTATION)
                // Cardinal directions (0, 2, 4, 6) use the base model with rotations
                .register(0, createVariant(cardinalModelId))
                .register(2, createVariant(cardinalModelId, 90))
                .register(4, createVariant(cardinalModelId, 180))
                .register(6, createVariant(cardinalModelId, 270))
                // Diagonal directions (1, 3, 5, 7) use the 45-degree rotated model with adjusted Y rotations
                .register(1, createVariant(diagonalModelId))
                .register(3, createVariant(diagonalModelId, 90))
                .register(5, createVariant(diagonalModelId, 180))
                .register(7, createVariant(diagonalModelId, 270));

        // Register the block state with the generator
        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block).coordinate(variants));

        // Register item model using the cardinal variant
        generator.registerParentedItemModel(block, cardinalModelId);
    }

    /**
     * Creates a chair model with the specified variant using predefined ModModels.
     * 
     * @param generator   The BlockStateModelGenerator to register the model with
     * @param block       The block this model is for
     * @param texturePath The texture path to use
     * @param variant     The variant name (e.g., "cardinal", "diagonal")
     * @param model       The predefined model from ModModels to use
     * @return The created model Identifier
     */
    private static Identifier createChairModel(BlockStateModelGenerator generator, Block block, String texturePath, String variant, Model model) {
        // Create a unique model ID for this block and variant
        String blockName = getBlockName(block);
        String modelPath = "block/" + blockName + "/" + variant;
        Identifier modelId = WesterosBlocks.id(modelPath);

        // Create texture map using ALL key (as used by chair model JSON files)
        TextureMap textureMap = new TextureMap()
                .put(TextureKey.ALL, createBlockIdentifier(texturePath));

        // Upload the model using the predefined ModModels model
        model.upload(modelId, textureMap, generator.modelCollector);

        return modelId;
    }


    /**
     * Creates an identifier for block textures, handling namespaces properly.
     * 
     * @param texturePath The texture path (can include namespace like "westerosblocks:block/texture")
     * @return The identifier for the block texture
     */
    private static Identifier createBlockIdentifier(String texturePath) {
        // If the texture path includes a namespace
        if (texturePath != null && texturePath.contains(":")) {
            String namespace = texturePath.substring(0, texturePath.indexOf(':'));
            String path = texturePath.substring(texturePath.indexOf(':') + 1);
            return Identifier.of(namespace, path);
        }
        // No namespace, use mod ID and prepend "block/"
        return WesterosBlocks.id("block/" + texturePath);
    }

    /**
     * Creates a variant with optional rotation.
     * 
     * @param modelId  The model identifier
     * @param rotation The rotation in degrees (0, 90, 180, 270)
     * @return The block state variant
     */
    private static BlockStateVariant createVariant(Identifier modelId, int rotation) {
        Rotation rotationEnum = switch (rotation) {
            case 0 -> Rotation.R0;
            case 90 -> Rotation.R90;
            case 180 -> Rotation.R180;
            case 270 -> Rotation.R270;
            default -> throw new IllegalArgumentException("Invalid rotation: " + rotation + ". Must be 0, 90, 180, or 270.");
        };
        return BlockStateVariant.create().put(VariantSettings.MODEL, modelId).put(VariantSettings.Y, rotationEnum);
    }

    /**
     * Creates a variant without rotation.
     * 
     * @param modelId The model identifier
     * @return The block state variant
     */
    private static BlockStateVariant createVariant(Identifier modelId) {
        return BlockStateVariant.create().put(VariantSettings.MODEL, modelId);
    }

    /**
     * Extracts the block name from the block's registry key.
     * 
     * @param block The block
     * @return The block name
     */
    private static String getBlockName(Block block) {
        String blockString = block.toString();
        if (blockString.contains(":")) {
            return blockString.split(":")[1].replace("}", "");
        }
        return blockString.toLowerCase().replace("block{", "").replace("}", "");
    }
}