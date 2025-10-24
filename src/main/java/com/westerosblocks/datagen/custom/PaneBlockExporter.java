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
     * Method for JSON definition system integration.
     * Uses uniform iteration pattern: After doInit(), states is ALWAYS non-empty,
     * and each state has randomTextures normalized from simple textures.
     * Pane blocks only use a single texture for both side and cap.
     */
    public static void registerCustomPaneBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        // After doInit(), states is ALWAYS non-empty (at least synthetic base state exists)
        var states = definition.getStates();

        if (states == null || states.isEmpty()) {
            throw new IllegalStateException("Block definition states should never be null/empty after doInit() for block: " + getBlockName(block));
        }

        // Pane blocks only need a single texture - extract from first state's first texture set
        BlockDefinition.StateVariant state = states.get(0);
        String texture = "missingno";

        int textureSetCount = state.getRandomTextureSetCount();
        if (textureSetCount > 0) {
            BlockDefinition.RandomTextureVariant set = state.getRandomTextureSet(0);
            if (set != null && set.getTextureCount() > 0) {
                texture = set.getTextureByIndex(0);
            }
        }

        registerPaneBlock(generator, block, texture);
    }
}