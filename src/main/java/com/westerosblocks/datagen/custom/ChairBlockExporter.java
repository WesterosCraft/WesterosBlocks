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
 * Exporter for chair blocks following block-models.md patterns.
 * Generates models for interactive seating blocks with 8-directional rotation support.
 *
 * <p>Structure follows block-models.md sections 5.2-5.6:
 * <ul>
 *   <li>Model instances (references ModModels.CHAIR for cardinal, ModModels.CHAIR_45 for diagonal)</li>
 *   <li>TextureMap builders (createChairModel with ALL texture key)</li>
 *   <li>BlockStateSupplier methods (BlockStateVariantMap for ROTATION property)</li>
 *   <li>Clean datagen methods (registerChairBlock)</li>
 * </ul>
 *
 * <p><b>Chair Rotation Variants:</b>
 * <ul>
 *   <li><b>Cardinal directions (0, 2, 4, 6):</b> North, East, South, West - uses base model with 0°, 90°, 180°, 270° rotations</li>
 *   <li><b>Diagonal directions (1, 3, 5, 7):</b> NE, SE, SW, NW - uses 45° rotated model with adjusted Y rotations</li>
 *   <li><b>Total:</b> 8 variants for complete 360° rotation in 45° increments</li>
 * </ul>
 *
 * <p><b>Model Types:</b>
 * <ul>
 *   <li>cardinal - Base chair model for N/E/S/W facing</li>
 *   <li>diagonal - 45-degree rotated model for NE/SE/SW/NW facing</li>
 * </ul>
 *
 * <p><b>Texture Order:</b> Single texture applied to all faces with {@code TextureKey.ALL}
 *
 * @see ModModels#CHAIR
 * @see ModModels#CHAIR_45
 * @see WCChairBlock
 */
public class ChairBlockExporter extends BaseBlockExporter {
    public static void registerChairBlock(BlockStateModelGenerator generator, Block block, String texturePath) {
        Identifier cardinalModelId = createChairModel(generator, block, texturePath, "cardinal", ModModels.CHAIR);
        Identifier diagonalModelId = createChairModel(generator, block, texturePath, "diagonal", ModModels.CHAIR_45);

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

        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block).coordinate(variants));

        generator.registerParentedItemModel(block, cardinalModelId);
    }

    private static Identifier createChairModel(BlockStateModelGenerator generator, Block block, String texturePath, String variant, Model model) {
        String blockName = getBlockName(block);
        String modelPath = "block/" + blockName + "/" + variant;
        Identifier modelId = WesterosBlocks.id(modelPath);

        TextureMap textureMap = new TextureMap()
                .put(TextureKey.ALL, createBlockIdentifier(texturePath));

        model.upload(modelId, textureMap, generator.modelCollector);

        return modelId;
    }
}