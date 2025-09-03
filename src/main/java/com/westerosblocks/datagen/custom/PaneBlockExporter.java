package com.westerosblocks.datagen.custom;

import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.datagen.ModModels;
import com.westerosblocks.datagen.ModTextureKey;
import com.westerosblocks.block.custom.WCPaneBlock;

public class PaneBlockExporter extends BaseBlockExporter {

    public static void registerPaneBlock(BlockStateModelGenerator generator, Block block, String texturePath) {
        WCPaneBlock paneBlock = (WCPaneBlock) block;
        String blockName = getBlockName(block);

        // Create texture map for pane models
        TextureMap paneTextureMap = new TextureMap()
                .put(TextureKey.SIDE, Identifier.of(WesterosBlocks.MOD_ID, "block/" + texturePath))
                .put(ModTextureKey.CAP, Identifier.of(WesterosBlocks.MOD_ID, "block/" + texturePath));

        // Generate models using ModModels
        Identifier postModelId = ModModels.PANE_POST.upload(
                Identifier.of(WesterosBlocks.MOD_ID, "block/" + blockName + "_post"),
                paneTextureMap,
                generator.modelCollector);

        Identifier sideModelId = ModModels.PANE_SIDE.upload(
                Identifier.of(WesterosBlocks.MOD_ID, "block/" + blockName + "_side"),
                paneTextureMap,
                generator.modelCollector);



        Identifier nosideModelId = ModModels.PANE_NOSIDE.upload(
                Identifier.of(WesterosBlocks.MOD_ID, "block/" + blockName + "_noside"),
                paneTextureMap,
                generator.modelCollector);

        // Create multipart block state
        MultipartBlockStateSupplier supplier = MultipartBlockStateSupplier.create(block);

        // Post model (always present for non-bars models)
        if (!paneBlock.isBarsModel()) {
            supplier = supplier.with(BlockStateVariant.create().put(VariantSettings.MODEL, postModelId));
        }

        // Side connections
        supplier = supplier.with(When.create().set(Properties.NORTH, true),
                BlockStateVariant.create().put(VariantSettings.MODEL, sideModelId));
        supplier = supplier.with(When.create().set(Properties.EAST, true),
                BlockStateVariant.create().put(VariantSettings.MODEL, sideModelId).put(VariantSettings.Y, VariantSettings.Rotation.R90));

        // No-side connections for non-bars models
        if (!paneBlock.isBarsModel()) {
            supplier = supplier.with(When.create().set(Properties.NORTH, false),
                    BlockStateVariant.create().put(VariantSettings.MODEL, nosideModelId));
            supplier = supplier.with(When.create().set(Properties.EAST, false),
                    BlockStateVariant.create().put(VariantSettings.MODEL, nosideModelId).put(VariantSettings.Y, VariantSettings.Rotation.R90));
        }

        generator.blockStateCollector.accept(supplier);

        // Register item model using generated model with layer0 texture for transparency
        TextureMap itemTextureMap = TextureMap.layer0(Identifier.of(WesterosBlocks.MOD_ID, "block/" + texturePath));
        Models.GENERATED.upload(
                ModelIds.getItemModelId(block.asItem()),
                itemTextureMap,
                generator.modelCollector);
    }

    public static void registerPaneBlockWithRandomTextures(BlockStateModelGenerator generator, Block block, String[] texturePaths) {
        validateTexturePaths(texturePaths, 1);
        
        // For random textures, use the first texture for block models and item model
        registerPaneBlock(generator, block, texturePaths[0]);
    }


}