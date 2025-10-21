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

/**
 * Exporter for arrow slit blocks following block-models.md patterns.
 * Generates models for vertically stackable arrow slit blocks with four type variants.
 *
 * <p>Structure follows block-models.md sections 5.2-5.6:
 * <ul>
 *   <li>Model instances (ARROW_SLIT_SINGLE, ARROW_SLIT_MIDDLE, ARROW_SLIT_TOP, ARROW_SLIT_BOTTOM)</li>
 *   <li>TextureMap builders (createArrowSlitTextureMap)</li>
 *   <li>BlockStateSupplier methods (createArrowSlitVariants)</li>
 *   <li>Clean datagen methods (registerArrowSlitBlock)</li>
 *   <li>Model creation helpers (arrowSlitBlock, createArrowSlitModel)</li>
 * </ul>
 *
 * <p>Arrow slit types:
 * <ul>
 *   <li>SINGLE - Standalone arrow slit</li>
 *   <li>MIDDLE - Middle section when vertically stacked</li>
 *   <li>TOP - Top section of vertical stack</li>
 *   <li>BOTTOM - Bottom section of vertical stack</li>
 * </ul>
 */
public class ArrowSlitBlockExporter extends BaseBlockExporter {

    /** Model for single (standalone) arrow slit */
    private static final Model ARROW_SLIT_SINGLE = arrowSlitBlock("arrow_slits/arrow_slit_single", TextureKey.TEXTURE);

    /** Model for middle section of vertically stacked arrow slits */
    private static final Model ARROW_SLIT_MIDDLE = arrowSlitBlock("arrow_slits/arrow_slit_middle", TextureKey.TEXTURE);

    /** Model for middle edge section of vertically stacked arrow slits (4+ blocks) */
    private static final Model ARROW_SLIT_MIDDLE_EDGE = arrowSlitBlock("arrow_slits/arrow_slit_middle_edge", TextureKey.TEXTURE);

    /** Model for top section of vertically stacked arrow slits */
    private static final Model ARROW_SLIT_TOP = arrowSlitBlock("arrow_slits/arrow_slit_top", TextureKey.TEXTURE);

    /** Model for bottom section of vertically stacked arrow slits */
    private static final Model ARROW_SLIT_BOTTOM = arrowSlitBlock("arrow_slits/arrow_slit_bottom", TextureKey.TEXTURE);


    public static void registerArrowSlitBlock(BlockStateModelGenerator generator, Block block, String texturePath) {
        Identifier singleModelId = createArrowSlitModel(generator, block, texturePath, "single", ARROW_SLIT_SINGLE);
        Identifier middleModelId = createArrowSlitModel(generator, block, texturePath, "middle", ARROW_SLIT_MIDDLE);
        Identifier middleEdgeModelId = createArrowSlitModel(generator, block, texturePath, "middle_edge", ARROW_SLIT_MIDDLE_EDGE);
        Identifier topModelId = createArrowSlitModel(generator, block, texturePath, "top", ARROW_SLIT_TOP);
        Identifier bottomModelId = createArrowSlitModel(generator, block, texturePath, "bottom", ARROW_SLIT_BOTTOM);

        BlockStateVariantMap variants = createArrowSlitVariants(singleModelId, middleModelId, middleEdgeModelId, topModelId, bottomModelId);
        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block).coordinate(variants));

        registerParentedItemModel(generator, block, singleModelId);
    }

    private static TextureMap createArrowSlitTextureMap(String texturePath) {
        return new TextureMap()
                .put(TextureKey.TEXTURE, createBlockIdentifier(texturePath))
                .put(TextureKey.PARTICLE, createBlockIdentifier(texturePath));
    }

    private static BlockStateVariantMap createArrowSlitVariants(Identifier singleModelId, Identifier middleModelId,
                                                                 Identifier middleEdgeModelId, Identifier topModelId,
                                                                 Identifier bottomModelId) {
        return BlockStateVariantMap.create(WCArrowSlitBlock.FACING, WCArrowSlitBlock.TYPE)
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
                // Middle edge state
                .register(Direction.NORTH, ArrowSlitType.MIDDLE_EDGE, createVariant(middleEdgeModelId))
                .register(Direction.EAST, ArrowSlitType.MIDDLE_EDGE, createVariant(middleEdgeModelId, 90))
                .register(Direction.SOUTH, ArrowSlitType.MIDDLE_EDGE, createVariant(middleEdgeModelId, 180))
                .register(Direction.WEST, ArrowSlitType.MIDDLE_EDGE, createVariant(middleEdgeModelId, 270))
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
    }

    private static Identifier createArrowSlitModel(BlockStateModelGenerator generator, Block block, String texturePath,
                                                   String variant, Model model) {
        Identifier modelId = createNestedModelId(block, variant);
        TextureMap textureMap = createArrowSlitTextureMap(texturePath);
        model.upload(modelId, textureMap, generator.modelCollector);
        return modelId;
    }

    private static Model arrowSlitBlock(String parent, TextureKey... requiredTextureKeys) {
        return new Model(Optional.of(WesterosBlocks.id("block/" + parent)), Optional.empty(), requiredTextureKeys);
    }
}
