package com.westerosblocks.datagen;

import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.BlockStateVariant;
import net.minecraft.data.client.ModelIds;
import net.minecraft.data.client.Models;
import net.minecraft.data.client.TextureMap;
import net.minecraft.data.client.VariantSettings;
import net.minecraft.data.client.VariantsBlockStateSupplier;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public class ModBlockStateModelGenerator {

    public ModBlockStateModelGenerator() {
    }

    public static void registerCustomSolidBlock(BlockStateModelGenerator blockStateModelGenerator, Block solidBlock,
            TextureMap textureMap) {
        Identifier identifier = ModModels.CUSTOM_CUBE_ALL.upload(solidBlock, textureMap,
                blockStateModelGenerator.modelCollector);
        blockStateModelGenerator.blockStateCollector.accept(VariantsBlockStateSupplier.create(solidBlock,
                BlockStateVariant.create().put(VariantSettings.MODEL, identifier)));
        blockStateModelGenerator.registerParentedItemModel(solidBlock, identifier);
    }

    public static void registerItemModel(Item item, BlockStateModelGenerator blockStateModelGenerator) {
        Models.GENERATED.upload(ModelIds.getItemModelId(item), TextureMap.layer0(item),
                blockStateModelGenerator.modelCollector);
    }
}