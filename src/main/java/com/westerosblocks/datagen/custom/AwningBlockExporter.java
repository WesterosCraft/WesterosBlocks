package com.westerosblocks.datagen.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.custom.WCAwningBlock;
import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.Block;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.List;
import java.util.Optional;

/**
 * Exporter for awning blocks with HALF, FACING, and RAISED properties.
 * Generates child models that reference parent models in models/block/awnings/
 *
 * Parent model mapping:
 * - awning_slanted: TOP half, raised=false
 * - awning_slanted_bottom: BOTTOM half, raised=false
 * - awning_raised: TOP half, raised=true
 * - awning_raised_bottom: BOTTOM half, raised=true
 */
public class AwningBlockExporter extends BaseBlockExporter {

    // Parent model paths
    private static final String PARENT_TOP_NORMAL = "block/awnings/awning_slanted";
    private static final String PARENT_BOTTOM_NORMAL = "block/awnings/awning_slanted_bottom";
    private static final String PARENT_TOP_RAISED = "block/awnings/awning_raised";
    private static final String PARENT_BOTTOM_RAISED = "block/awnings/awning_raised_bottom";

    // TextureKey constants - must be same instance for TextureMap and Model
    private static final TextureKey TEXTURE_1 = TextureKey.of("1");
    private static final TextureKey TEXTURE_2 = TextureKey.of("2");

    public static void registerCustomAwningBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        List<String> textureList = definition.getTextures();

        // Get textures - texture[0] is wool/fabric (#1), texture[1] is awning side/frame (#2)
        String woolTexture = (textureList != null && !textureList.isEmpty()) ? textureList.get(0) : "wool/red/all";
        String sideTexture = (textureList != null && textureList.size() > 1) ? textureList.get(1) : "awning/red_awning_side";

        // Generate the 4 child models
        Identifier topNormalModel = generateChildModel(generator, block, "top", false, woolTexture, sideTexture);
        Identifier bottomNormalModel = generateChildModel(generator, block, "bottom", false, woolTexture, sideTexture);
        Identifier topRaisedModel = generateChildModel(generator, block, "top_raised", true, woolTexture, sideTexture);
        Identifier bottomRaisedModel = generateChildModel(generator, block, "bottom_raised", true, woolTexture, sideTexture);

        // Generate blockstate with all 16 variants
        BlockStateVariantMap variants = createAwningVariants(
                topNormalModel, bottomNormalModel, topRaisedModel, bottomRaisedModel);
        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block).coordinate(variants));

        // Register item model using one of the generated models
        registerParentedItemModel(generator, block, bottomNormalModel);
    }

    /**
     * Generates a child model that references a parent awning model.
     */
    private static Identifier generateChildModel(BlockStateModelGenerator generator, Block block,
                                                   String variant, boolean raised,
                                                   String woolTexture, String sideTexture) {
        String blockName = getBlockName(block);
        Identifier modelId = WesterosBlocks.id("block/" + blockName + "/" + variant);

        // Select parent model based on variant
        String parentPath;
        if (variant.contains("top")) {
            parentPath = raised ? PARENT_TOP_RAISED : PARENT_TOP_NORMAL;
        } else {
            parentPath = raised ? PARENT_BOTTOM_RAISED : PARENT_BOTTOM_NORMAL;
        }

        // Create texture map with #1 (wool), #2 (awning side), and particle
        // Must use same TextureKey instances in both TextureMap and Model
        TextureMap textureMap = new TextureMap()
                .put(TEXTURE_1, createBlockIdentifier(woolTexture))
                .put(TEXTURE_2, createBlockIdentifier(sideTexture))
                .put(TextureKey.PARTICLE, createBlockIdentifier(woolTexture));

        // Create model with parent reference
        Model childModel = new Model(
                Optional.of(WesterosBlocks.id(parentPath)),
                Optional.empty(),
                TEXTURE_1, TEXTURE_2, TextureKey.PARTICLE
        );

        childModel.upload(modelId, textureMap, generator.modelCollector);
        return modelId;
    }

    /**
     * Creates the BlockStateVariantMap with all 16 variants.
     * Applies Y-rotation based on FACING direction.
     */
    private static BlockStateVariantMap createAwningVariants(
            Identifier topNormal, Identifier bottomNormal,
            Identifier topRaised, Identifier bottomRaised) {

        return BlockStateVariantMap.create(WCAwningBlock.FACING, WCAwningBlock.HALF, WCAwningBlock.RAISED)
                // NORTH facing (y=0)
                .register(Direction.NORTH, BlockHalf.TOP, false, createVariant(topNormal, 0))
                .register(Direction.NORTH, BlockHalf.TOP, true, createVariant(topRaised, 0))
                .register(Direction.NORTH, BlockHalf.BOTTOM, false, createVariant(bottomNormal, 0))
                .register(Direction.NORTH, BlockHalf.BOTTOM, true, createVariant(bottomRaised, 0))
                // EAST facing (y=90)
                .register(Direction.EAST, BlockHalf.TOP, false, createVariant(topNormal, 90))
                .register(Direction.EAST, BlockHalf.TOP, true, createVariant(topRaised, 90))
                .register(Direction.EAST, BlockHalf.BOTTOM, false, createVariant(bottomNormal, 90))
                .register(Direction.EAST, BlockHalf.BOTTOM, true, createVariant(bottomRaised, 90))
                // SOUTH facing (y=180)
                .register(Direction.SOUTH, BlockHalf.TOP, false, createVariant(topNormal, 180))
                .register(Direction.SOUTH, BlockHalf.TOP, true, createVariant(topRaised, 180))
                .register(Direction.SOUTH, BlockHalf.BOTTOM, false, createVariant(bottomNormal, 180))
                .register(Direction.SOUTH, BlockHalf.BOTTOM, true, createVariant(bottomRaised, 180))
                // WEST facing (y=270)
                .register(Direction.WEST, BlockHalf.TOP, false, createVariant(topNormal, 270))
                .register(Direction.WEST, BlockHalf.TOP, true, createVariant(topRaised, 270))
                .register(Direction.WEST, BlockHalf.BOTTOM, false, createVariant(bottomNormal, 270))
                .register(Direction.WEST, BlockHalf.BOTTOM, true, createVariant(bottomRaised, 270));
    }
}
