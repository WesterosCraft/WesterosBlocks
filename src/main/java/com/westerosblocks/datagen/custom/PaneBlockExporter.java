package com.westerosblocks.datagen.custom;

import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import com.westerosblocks.datagen.ModModels;
import com.westerosblocks.datagen.ModTextureKey;
import com.westerosblocks.block.custom.WCPaneBlock;

/**
 * Simplified pane block exporter following block-models.md patterns.
 * Handles both regular panes and bars models with clean, maintainable code.
 */
public class PaneBlockExporter extends BaseBlockExporter {

    /**
     * Registers a pane block with a single texture.
     * Follows block-models.md multipart blockstate pattern.
     */
    public static void registerPaneBlock(BlockStateModelGenerator generator, Block block, String texturePath) {
        WCPaneBlock paneBlock = (WCPaneBlock) block;
        
        // Create texture map for pane models
        TextureMap paneTextureMap = new TextureMap()
                .put(TextureKey.SIDE, createBlockIdentifier(texturePath))
                .put(ModTextureKey.CAP, createBlockIdentifier(texturePath));

        // Upload models
        Identifier postModelId = ModModels.PANE_POST.upload(createNestedModelId(block, getBlockName(block) + "_post"), paneTextureMap, generator.modelCollector);
        Identifier sideModelId = ModModels.PANE_SIDE.upload(createNestedModelId(block, getBlockName(block) + "_side"), paneTextureMap, generator.modelCollector);
        Identifier nosideModelId = ModModels.PANE_NOSIDE.upload(createNestedModelId(block, getBlockName(block) + "_noside"), paneTextureMap, generator.modelCollector);

        // Create multipart blockstate
        MultipartBlockStateSupplier supplier = createPaneBlockState(block, paneBlock, postModelId, sideModelId, nosideModelId);
        generator.blockStateCollector.accept(supplier);

        // Register item model
        registerSimpleItemModel(generator, block, createBlockIdentifier(texturePath));
    }

    /**
     * Registers a pane block with random texture support.
     */
    public static void registerPaneBlockWithRandomTextures(BlockStateModelGenerator generator, Block block, String[] texturePaths) {
        validateTexturePaths(texturePaths, 1);
        registerPaneBlock(generator, block, texturePaths[0]);
    }

    /**
     * Creates a multipart blockstate for pane blocks.
     * Handles both regular panes and bars models.
     */
    private static MultipartBlockStateSupplier createPaneBlockState(Block block, WCPaneBlock paneBlock, 
            Identifier postModelId, Identifier sideModelId, Identifier nosideModelId) {
        
        MultipartBlockStateSupplier supplier = MultipartBlockStateSupplier.create(block);

        // Post model (always present for non-bars models)
        if (!paneBlock.isBarsModel()) {
            supplier = supplier.with(createVariant(postModelId));
        }

        // Side connections
        supplier = supplier.with(When.create().set(Properties.NORTH, true), createVariant(sideModelId))
                          .with(When.create().set(Properties.EAST, true), createVariant(sideModelId, 90));

        // No-side connections for non-bars models
        if (!paneBlock.isBarsModel()) {
            supplier = supplier.with(When.create().set(Properties.NORTH, false), createVariant(nosideModelId))
                              .with(When.create().set(Properties.EAST, false), createVariant(nosideModelId, 90));
        }

        return supplier;
    }
}