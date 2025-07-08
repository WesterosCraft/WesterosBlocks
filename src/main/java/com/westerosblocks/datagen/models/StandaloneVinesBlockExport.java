package com.westerosblocks.datagen.models;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.custom.StandaloneVinesBlock;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class StandaloneVinesBlockExport {
    public StandaloneVinesBlockExport() {
    }

    public void generateBlockStateModels(BlockStateModelGenerator generator, Block block, String baseTexture, String topTexture) {
        StandaloneVinesBlock vinesBlock = (StandaloneVinesBlock) block;
        String blockName = vinesBlock.getBlockName();
        
        MultipartBlockStateSupplier stateSupplier = MultipartBlockStateSupplier.create(block);

        // Generate the vine models first
        generateVineModels(generator, blockName, baseTexture, topTexture);

        // SOUTH
        When.PropertyCondition southCondition = When.create().set(Properties.SOUTH, true);
        BlockStateVariant southVariant = createVariant(blockName, "base", 0, false, 0);
        stateSupplier.with(southCondition, southVariant);

        // WEST
        When.PropertyCondition westCondition = When.create().set(Properties.WEST, true);
        BlockStateVariant westVariant = createVariant(blockName, "base", 90, false, 0);
        stateSupplier.with(westCondition, westVariant);

        // NORTH
        When.PropertyCondition northCondition = When.create().set(Properties.NORTH, true);
        BlockStateVariant northVariant = createVariant(blockName, "base", 180, false, 0);
        stateSupplier.with(northCondition, northVariant);

        // EAST
        When.PropertyCondition eastCondition = When.create().set(Properties.EAST, true);
        BlockStateVariant eastVariant = createVariant(blockName, "base", 270, false, 0);
        stateSupplier.with(eastCondition, eastVariant);

        // UP
        When.PropertyCondition upCondition = When.create().set(Properties.UP, true);
        BlockStateVariant upVariant = createVariant(blockName, "top", 0, true, 0);
        stateSupplier.with(upCondition, upVariant);

        // DOWN (only if canGrowDownward is true)
        if (vinesBlock.isCanGrowDownward()) {
            When.PropertyCondition downCondition = When.create().set(Properties.DOWN, true);
            BlockStateVariant downVariant = createVariant(blockName, "top", 0, true, 180);
            stateSupplier.with(downCondition, downVariant);
        }

        generator.blockStateCollector.accept(stateSupplier);
    }

    private BlockStateVariant createVariant(String blockName, String type, int yRotation, boolean isVertical, int xRotation) {
        Identifier modelId = getModelId(blockName, type);
        return VariantBuilder.create(modelId, yRotation, xRotation);
    }

    private void generateVineModels(BlockStateModelGenerator generator, String blockName, String baseTexture, String topTexture) {
        // base vine model
        TextureMap baseTextureMap = new TextureMap().put(ModTextureKey.VINES, createBlockIdentifier(baseTexture));
        Identifier baseModelId = getModelId(blockName, "base");
        Model baseModel = new Model(
                Optional.of(WesterosBlocks.id("block/vines/vine_1")),
                Optional.empty(),
                ModTextureKey.VINES
        );
        baseModel.upload(baseModelId, baseTextureMap, generator.modelCollector);

        // top vine model
        TextureMap topTextureMap = new TextureMap().put(ModTextureKey.VINES, createBlockIdentifier(topTexture));
        Identifier topModelId = getModelId(blockName, "top");
        Model topModel = new Model(
                Optional.of(WesterosBlocks.id("block/vines/vine_u")),
                Optional.empty(),
                ModTextureKey.VINES
        );
        topModel.upload(topModelId, topTextureMap, generator.modelCollector);
    }

    private Identifier getModelId(String blockName, String type) {
        return WesterosBlocks.id(String.format("block/generated/%s/%s", blockName, type));
    }

    private static Identifier createBlockIdentifier(String texturePath) {
        // If the texture path includes a namespace
        if (texturePath != null && texturePath.contains(":")) {
            String namespace = texturePath.substring(0, texturePath.indexOf(':'));
            String path = texturePath.substring(texturePath.indexOf(':') + 1);
            // If it's not a minecraft texture, prepend block/
            if (!namespace.equals("minecraft")) {
                path = "block/" + path;
            }
            return Identifier.of(namespace, path);
        }
        // No namespace, use mod ID and prepend block/
        return WesterosBlocks.id("block/" + texturePath);
    }

    public void generateItemModels(ItemModelGenerator itemModelGenerator, Block block, String baseTexture) {
        TextureMap textureMap = TextureMap.layer0(createBlockIdentifier(baseTexture));
        Models.GENERATED.upload(ModelIds.getItemModelId(block.asItem()), textureMap, itemModelGenerator.writer);
    }

    private static class VariantBuilder {
        public static BlockStateVariant create(Identifier modelId, int yRotation, int xRotation) {
            BlockStateVariant variant = BlockStateVariant.create()
                    .put(VariantSettings.MODEL, modelId);

            if (yRotation != 0) {
                variant.put(VariantSettings.Y, getRotation(yRotation));
            }

            if (xRotation != 0) {
                variant.put(VariantSettings.X, getRotation(xRotation));
            }

            return variant;
        }

        private static VariantSettings.Rotation getRotation(int degrees) {
            return switch (degrees) {
                case 90 -> VariantSettings.Rotation.R90;
                case 180 -> VariantSettings.Rotation.R180;
                case 270 -> VariantSettings.Rotation.R270;
                default -> VariantSettings.Rotation.R0;
            };
        }
    }
} 