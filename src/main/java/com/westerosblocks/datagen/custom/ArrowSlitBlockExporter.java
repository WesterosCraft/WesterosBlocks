package com.westerosblocks.datagen.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.custom.WCArrowSlitBlock;
import com.westerosblocks.block.custom.WCArrowSlitBlock.ArrowSlitType;
import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.BlockStateVariantMap;
import net.minecraft.data.client.Model;
import net.minecraft.data.client.TextureKey;
import net.minecraft.data.client.TextureMap;
import net.minecraft.data.client.VariantsBlockStateSupplier;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.Optional;

public class ArrowSlitBlockExporter extends BaseBlockExporter {

    // Arrow slit model definitions
    private static final Model ARROW_SLIT_SINGLE = arrowSlitBlock("arrow_slits/arrow_slit_single", TextureKey.TEXTURE);
    private static final Model ARROW_SLIT_MIDDLE = arrowSlitBlock("arrow_slits/arrow_slit_middle", TextureKey.TEXTURE);
    private static final Model ARROW_SLIT_TOP = arrowSlitBlock("arrow_slits/arrow_slit_top", TextureKey.TEXTURE);
    private static final Model ARROW_SLIT_BOTTOM = arrowSlitBlock("arrow_slits/arrow_slit_bottom", TextureKey.TEXTURE);

    /**
     * Generates block state models for an arrow slit block.
     *
     * @param generator   The BlockStateModelGenerator to register models with
     * @param block       The arrow slit block to generate models for
     * @param texturePath The texture path for the arrow slit
     */
    public static void registerArrowSlitBlock(BlockStateModelGenerator generator, Block block, String texturePath) {
        // Create the base models for each type
        Identifier singleModelId = createArrowSlitModel(generator, block, texturePath, "single", ARROW_SLIT_SINGLE);
        Identifier middleModelId = createArrowSlitModel(generator, block, texturePath, "middle", ARROW_SLIT_MIDDLE);
        Identifier topModelId = createArrowSlitModel(generator, block, texturePath, "top", ARROW_SLIT_TOP);
        Identifier bottomModelId = createArrowSlitModel(generator, block, texturePath, "bottom", ARROW_SLIT_BOTTOM);

        // Create variants for each state and direction
        BlockStateVariantMap variants = BlockStateVariantMap.create(WCArrowSlitBlock.FACING, WCArrowSlitBlock.TYPE)
                // Single state
                .register(Direction.NORTH, ArrowSlitType.SINGLE, createVariant(singleModelId))
                .register(Direction.EAST, ArrowSlitType.SINGLE, createVariant(singleModelId, 90))
                .register(Direction.SOUTH, ArrowSlitType.SINGLE, createVariant(singleModelId, 180))
                .register(Direction.WEST, ArrowSlitType.SINGLE, createVariant(singleModelId, 270))

                // Middle state
                .register(Direction.NORTH, ArrowSlitType.MIDDLE, createVariant(middleModelId))
                .register(Direction.EAST, ArrowSlitType.MIDDLE, createVariant(middleModelId, 90))
                .register(Direction.SOUTH, ArrowSlitType.MIDDLE, createVariant(middleModelId, 180))
                .register(Direction.WEST, ArrowSlitType.MIDDLE, createVariant(middleModelId, 270))

                // Bottom state
                .register(Direction.NORTH, ArrowSlitType.BOTTOM, createVariant(bottomModelId))
                .register(Direction.EAST, ArrowSlitType.BOTTOM, createVariant(bottomModelId, 90))
                .register(Direction.SOUTH, ArrowSlitType.BOTTOM, createVariant(bottomModelId, 180))
                .register(Direction.WEST, ArrowSlitType.BOTTOM, createVariant(bottomModelId, 270))

                // Top state
                .register(Direction.NORTH, ArrowSlitType.TOP, createVariant(topModelId))
                .register(Direction.EAST, ArrowSlitType.TOP, createVariant(topModelId, 90))
                .register(Direction.SOUTH, ArrowSlitType.TOP, createVariant(topModelId, 180))
                .register(Direction.WEST, ArrowSlitType.TOP, createVariant(topModelId, 270));

        // Register the block state with the generator
        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block).coordinate(variants));

        // Create 3D item model using the single block variant
        generator.registerParentedItemModel(block, singleModelId);
    }

    /**
     * Creates an arrow slit model with the specified variant.
     *
     * @param generator   The BlockStateModelGenerator to register the model with
     * @param block       The block this model is for
     * @param texturePath The texture path to use
     * @param variant     The variant name (e.g., "single", "middle", "top", "bottom")
     * @param model       The predefined model to use
     * @return The created model Identifier
     */
    private static Identifier createArrowSlitModel(BlockStateModelGenerator generator, Block block, String texturePath,
                                                   String variant, Model model) {
        // Create a unique model ID for this block and variant
        String blockName = getBlockName(block);
        String modelPath = "block/" + blockName + "/" + variant;
        Identifier modelId = WesterosBlocks.id(modelPath);

        // Create texture map
        TextureMap textureMap = new TextureMap()
                .put(TextureKey.TEXTURE, createBlockIdentifier(texturePath))
                .put(TextureKey.PARTICLE, createBlockIdentifier(texturePath));

        model.upload(modelId, textureMap, generator.modelCollector);

        return modelId;
    }

    /**
     * Helper method for creating arrow slit block models.
     *
     * @param parent              The model parent path
     * @param requiredTextureKeys The required texture keys for this model
     * @return The created Model
     */
    private static Model arrowSlitBlock(String parent, TextureKey... requiredTextureKeys) {
        return new Model(Optional.of(WesterosBlocks.id("block/" + parent)), Optional.empty(), requiredTextureKeys);
    }
}
