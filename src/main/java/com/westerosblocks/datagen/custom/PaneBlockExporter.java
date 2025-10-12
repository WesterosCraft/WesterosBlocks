package com.westerosblocks.datagen.custom;

import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import com.westerosblocks.datagen.ModModels;
import com.westerosblocks.datagen.ModTextureKey;
import com.westerosblocks.block.custom.WCPaneBlock;
import com.westerosblocks.data.BlockDefinition;

import java.util.List;

public class PaneBlockExporter extends BaseBlockExporter {

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

    /**
     * Method for JSON definition system integration
     */
    public static void registerCustomPaneBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        // Use centralized texture extraction with priority logic
        BlockDefinition.TextureSource source = definition.getPrimaryTextureSource();

        String texture = switch (source) {
            case RANDOM_TEXTURES -> {
                // Use first texture from first random variant
                List<BlockDefinition.TextureVariantSet> variants = definition.getRandomTextureVariantSets();
                if (!variants.isEmpty() && !variants.get(0).textures.isEmpty()) {
                    yield variants.get(0).textures.get(0);
                }
                yield "missingno";
            }
            case TEXTURES -> definition.getFirstTexture("missingno");
            case STATES, CUSTOM_MODEL, NONE -> "missingno";
        };

        registerPaneBlock(generator, block, texture);
    }
}