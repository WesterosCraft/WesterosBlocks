package com.westerosblocks.datagen.custom;

import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.state.property.Properties;
import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.datagen.ModModels;

/**
 * Exporter for flowerbed blocks following block-models.md patterns.
 * Generates models for placeable flower clusters with variable amounts (1-4) and directional facing.
 *
 * Supports fluent builder API for flexible texture configuration.
 *
 * @see ModModels#FLOWERBED_1
 * @see ModModels#FLOWERBED_2
 * @see ModModels#FLOWERBED_3
 * @see ModModels#FLOWERBED_4
 */
public class FlowerbedBlockExporter extends BaseBlockExporter {
    public static class CustomFlowerbedBuilder {
        private final BlockStateModelGenerator generator;
        private final Block block;
        private String parentModel = "block/flowerbed";
        private TextureKey stemTextureKey = TextureKey.STEM;
        private String stemTexture = "";
        private String flowerTexture = "";

        public CustomFlowerbedBuilder(BlockStateModelGenerator generator, Block block) {
            this.generator = generator;
            this.block = block;
        }

        public CustomFlowerbedBuilder stemTexture(String texturePath) {
            this.stemTexture = texturePath;
            return this;
        }

        public CustomFlowerbedBuilder flowerTexture(String texturePath) {
            this.flowerTexture = texturePath;
            return this;
        }

        public void build() {
            generateCustomFlowerbed(generator, block, parentModel, stemTextureKey, stemTexture, flowerTexture);
        }
    }

    public static CustomFlowerbedBuilder registerCustomFlowerbedBlock(BlockStateModelGenerator generator, Block block) {
        return new CustomFlowerbedBuilder(generator, block);
    }

    public static void registerCustomFlowerbedBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        String[] flowerTextures = definition.getTexturesAsArray();
        String stemTex = flowerTextures.length > 0 ? flowerTextures[0] : "";
        String flowerTex = flowerTextures.length > 1 ? flowerTextures[1] : "";
        new CustomFlowerbedBuilder(generator, block)
            .stemTexture(stemTex)
            .flowerTexture(flowerTex)
            .build();
    }

    public static void generateCustomFlowerbed(BlockStateModelGenerator generator, Block block, String parentModel, TextureKey stemTextureKey, String stemTexture, String flowerTexture) {
        // Create texture map for the flowerbed
        String actualStemTexture = stemTexture.isEmpty() ? getBlockName(block) + "_stem" : stemTexture;
        String actualFlowerTexture = flowerTexture.isEmpty() ? getBlockName(block) : flowerTexture;

        TextureMap textureMap = new TextureMap()
                .put(stemTextureKey, createBlockIdentifier(actualStemTexture))
                .put(TextureKey.FLOWERBED, createBlockIdentifier(actualFlowerTexture));

        // Create models for different flower amounts (1-4)
        Identifier model1 = ModModels.FLOWERBED_1.upload(createBlockIdentifier(getBlockName(block) + "/" + getBlockName(block) + "_1"), textureMap, generator.modelCollector);
        Identifier model2 = ModModels.FLOWERBED_2.upload(createBlockIdentifier(getBlockName(block) + "/" + getBlockName(block) + "_2"), textureMap, generator.modelCollector);
        Identifier model3 = ModModels.FLOWERBED_3.upload(createBlockIdentifier(getBlockName(block) + "/" + getBlockName(block) + "_3"), textureMap, generator.modelCollector);
        Identifier model4 = ModModels.FLOWERBED_4.upload(createBlockIdentifier(getBlockName(block) + "/" + getBlockName(block) + "_4"), textureMap, generator.modelCollector);

        // Create blockstate with multipart for FACING and FLOWER_AMOUNT properties (like vanilla pink_petals)
        generator.blockStateCollector.accept(MultipartBlockStateSupplier.create(block)
                // Model 1 for flower_amount 1, 2, 3, 4
                .with(When.create().set(Properties.FLOWER_AMOUNT, 1, 2, 3, 4).set(Properties.HORIZONTAL_FACING, Direction.NORTH),
                        BlockStateVariant.create().put(VariantSettings.MODEL, model1))
                .with(When.create().set(Properties.FLOWER_AMOUNT, 1, 2, 3, 4).set(Properties.HORIZONTAL_FACING, Direction.EAST),
                        BlockStateVariant.create().put(VariantSettings.MODEL, model1).put(VariantSettings.Y, VariantSettings.Rotation.R90))
                .with(When.create().set(Properties.FLOWER_AMOUNT, 1, 2, 3, 4).set(Properties.HORIZONTAL_FACING, Direction.SOUTH),
                        BlockStateVariant.create().put(VariantSettings.MODEL, model1).put(VariantSettings.Y, VariantSettings.Rotation.R180))
                .with(When.create().set(Properties.FLOWER_AMOUNT, 1, 2, 3, 4).set(Properties.HORIZONTAL_FACING, Direction.WEST),
                        BlockStateVariant.create().put(VariantSettings.MODEL, model1).put(VariantSettings.Y, VariantSettings.Rotation.R270))

                // Model 2 for flower_amount 2, 3, 4
                .with(When.create().set(Properties.FLOWER_AMOUNT, 2, 3, 4).set(Properties.HORIZONTAL_FACING, Direction.NORTH),
                        BlockStateVariant.create().put(VariantSettings.MODEL, model2))
                .with(When.create().set(Properties.FLOWER_AMOUNT, 2, 3, 4).set(Properties.HORIZONTAL_FACING, Direction.EAST),
                        BlockStateVariant.create().put(VariantSettings.MODEL, model2).put(VariantSettings.Y, VariantSettings.Rotation.R90))
                .with(When.create().set(Properties.FLOWER_AMOUNT, 2, 3, 4).set(Properties.HORIZONTAL_FACING, Direction.SOUTH),
                        BlockStateVariant.create().put(VariantSettings.MODEL, model2).put(VariantSettings.Y, VariantSettings.Rotation.R180))
                .with(When.create().set(Properties.FLOWER_AMOUNT, 2, 3, 4).set(Properties.HORIZONTAL_FACING, Direction.WEST),
                        BlockStateVariant.create().put(VariantSettings.MODEL, model2).put(VariantSettings.Y, VariantSettings.Rotation.R270))

                // Model 3 for flower_amount 3, 4
                .with(When.create().set(Properties.FLOWER_AMOUNT, 3, 4).set(Properties.HORIZONTAL_FACING, Direction.NORTH),
                        BlockStateVariant.create().put(VariantSettings.MODEL, model3))
                .with(When.create().set(Properties.FLOWER_AMOUNT, 3, 4).set(Properties.HORIZONTAL_FACING, Direction.EAST),
                        BlockStateVariant.create().put(VariantSettings.MODEL, model3).put(VariantSettings.Y, VariantSettings.Rotation.R90))
                .with(When.create().set(Properties.FLOWER_AMOUNT, 3, 4).set(Properties.HORIZONTAL_FACING, Direction.SOUTH),
                        BlockStateVariant.create().put(VariantSettings.MODEL, model3).put(VariantSettings.Y, VariantSettings.Rotation.R180))
                .with(When.create().set(Properties.FLOWER_AMOUNT, 3, 4).set(Properties.HORIZONTAL_FACING, Direction.WEST),
                        BlockStateVariant.create().put(VariantSettings.MODEL, model3).put(VariantSettings.Y, VariantSettings.Rotation.R270))

                // Model 4 for flower_amount 4 only
                .with(When.create().set(Properties.FLOWER_AMOUNT, 4).set(Properties.HORIZONTAL_FACING, Direction.NORTH),
                        BlockStateVariant.create().put(VariantSettings.MODEL, model4))
                .with(When.create().set(Properties.FLOWER_AMOUNT, 4).set(Properties.HORIZONTAL_FACING, Direction.EAST),
                        BlockStateVariant.create().put(VariantSettings.MODEL, model4).put(VariantSettings.Y, VariantSettings.Rotation.R90))
                .with(When.create().set(Properties.FLOWER_AMOUNT, 4).set(Properties.HORIZONTAL_FACING, Direction.SOUTH),
                        BlockStateVariant.create().put(VariantSettings.MODEL, model4).put(VariantSettings.Y, VariantSettings.Rotation.R180))
                .with(When.create().set(Properties.FLOWER_AMOUNT, 4).set(Properties.HORIZONTAL_FACING, Direction.WEST),
                        BlockStateVariant.create().put(VariantSettings.MODEL, model4).put(VariantSettings.Y, VariantSettings.Rotation.R270)));

        // Register item model
        generator.registerParentedItemModel(block, model1);
    }
}