package com.westerosblocks.datagen.models;

import com.westerosblocks.WesterosBlocks;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.HashSet;
import java.util.Set;

public class RailBlockExport2 {
    private static final String[] SHAPES = {
            "shape=north_south", "shape=east_west",
            "shape=ascending_east", "shape=ascending_west",
            "shape=ascending_north", "shape=ascending_south",
            "shape=south_east", "shape=south_west",
            "shape=north_west", "shape=north_east"
    };

    private static final String[] MODEL_TYPES = {
            "flat", "flat",
            "raised_ne", "raised_sw",
            "raised_ne", "raised_sw",
            "curved", "curved",
            "curved", "curved"
    };

    private static final int[] ROTATIONS = {
            0, 90,
            90, 90,
            0, 0,
            0, 90,
            180, 270
    };

    public RailBlockExport2() {
    }

    /**
     * Generates block state models for a rail block.
     * 
     * @param generator The block state model generator
     * @param block The rail block
     * @param texture The texture path for the rail
     */
    public void generateBlockStateModels(BlockStateModelGenerator generator, Block block, String texture) {
        // Generate the rail models first
        Set<String> generatedModels = new HashSet<>();
        for (String modelType : MODEL_TYPES) {
            if (generatedModels.add(modelType)) { // Only generate if we haven't seen this type before
                generateRailModel(generator, modelType, block, texture);
            }
        }

        // Create block state variants
        BlockStateVariant[] variants = new BlockStateVariant[SHAPES.length];
        for (int i = 0; i < SHAPES.length; i++) {
            Identifier modelId = getModelId(MODEL_TYPES[i], block);
            variants[i] = BlockStateVariant.create()
                .put(VariantSettings.MODEL, modelId)
                .put(VariantSettings.Y, getRotation(ROTATIONS[i]));
        }

        // Register the block state
        generator.blockStateCollector.accept(
            VariantsBlockStateSupplier.create(block, variants)
        );
    }

    /**
     * Generates item models for a rail block.
     * 
     * @param itemModelGenerator The item model generator
     * @param block The rail block
     * @param texture The texture path for the rail
     */
    public void generateItemModels(ItemModelGenerator itemModelGenerator, Block block, String texture) {
        String texturePath = String.format("block/%s", texture);
        Identifier textureId = WesterosBlocks.id(texturePath);

        Models.GENERATED.upload(
                ModelIds.getItemModelId(block.asItem()),
                TextureMap.layer0(textureId),
                itemModelGenerator.writer
        );
    }

    private void generateRailModel(BlockStateModelGenerator generator, String type, Block block, String texture) {
        String texturePath = String.format("block/%s", texture);
        TextureMap textureMap = new TextureMap()
                .put(TextureKey.RAIL, WesterosBlocks.id(texturePath));

        Model model = switch(type) {
            case "flat" -> Models.RAIL_FLAT;
            case "curved" -> Models.RAIL_CURVED;
            case "raised_ne" -> Models.TEMPLATE_RAIL_RAISED_NE;
            case "raised_sw" -> Models.TEMPLATE_RAIL_RAISED_SW;
            default -> Models.RAIL_FLAT;
        };

        Identifier modelId = getModelId(type, block);
        model.upload(modelId, textureMap, generator.modelCollector);
    }

    private Identifier getModelId(String type, Block block) {
        return Identifier.of(WesterosBlocks.MOD_ID,
                String.format("block/%s_%s", Registries.BLOCK.getId(block).getPath(), type));
    }

    private VariantSettings.Rotation getRotation(int degrees) {
        return switch (degrees) {
            case 90 -> VariantSettings.Rotation.R90;
            case 180 -> VariantSettings.Rotation.R180;
            case 270 -> VariantSettings.Rotation.R270;
            default -> VariantSettings.Rotation.R0;
        };
    }
} 