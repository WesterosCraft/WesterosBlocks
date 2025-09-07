package com.westerosblocks.datagen.custom;

import net.minecraft.block.Block;
import net.minecraft.block.RailBlock;
import net.minecraft.block.enums.RailShape;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

import java.util.*;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.datagen.ModModels;

public class RailBlockExporter extends BaseBlockExporter {

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

    /**
     * Registers a rail block with single texture
     */
    public static void registerRailBlock(BlockStateModelGenerator generator, Block block, String texturePath) {
        registerRailBlock(generator, block, new String[]{texturePath});
    }

    /**
     * Registers a rail block with multiple texture variants
     * First texture is used for flat/raised models, second for curved models
     */
    public static void registerRailBlock(BlockStateModelGenerator generator, Block block, String[] texturePaths) {
        validateTexturePaths(texturePaths, 1);
        
        String flatTexture = texturePaths[0];
        String curvedTexture = texturePaths.length > 1 ? texturePaths[1] : texturePaths[0];
        
        generateRailBlockState(generator, block, flatTexture, curvedTexture);
        generateRailModels(generator, block, flatTexture, curvedTexture);
        
        // Register item model using flat texture
        Identifier textureId = createBlockIdentifier(flatTexture);
        Models.GENERATED.upload(
            ModelIds.getItemModelId(block.asItem()),
            TextureMap.layer0(textureId),
            generator.modelCollector
        );
    }

    /**
     * Registers a rail block with random texture variants
     */
    public static void registerRailBlockWithRandomTextures(BlockStateModelGenerator generator, Block block, String[][] textureArrays) {
        if (textureArrays.length == 0) {
            throw new IllegalArgumentException("At least one texture array is required");
        }
        
        generateRailBlockStateWithRandomTextures(generator, block, textureArrays);
        generateRailModelsWithRandomTextures(generator, block, textureArrays);
        
        // Register item model using first texture set
        Identifier textureId = createBlockIdentifier(textureArrays[0][0]);
        Models.GENERATED.upload(
            ModelIds.getItemModelId(block.asItem()),
            TextureMap.layer0(textureId),
            generator.modelCollector
        );
    }

    private static void generateRailBlockState(BlockStateModelGenerator generator, Block block, String flatTexture, String curvedTexture) {
        String blockName = getBlockName(block);
        
        // Create variants map like the old working version
        Map<String, BlockStateVariant> variantMap = new HashMap<>();
        
        for (int i = 0; i < SHAPES.length; i++) {
            String modelType = MODEL_TYPES[i];
            int rotation = ROTATIONS[i];
            
            Identifier modelId = WesterosBlocks.id("block/" + blockName + "/" + modelType);
            BlockStateVariant variant = BlockStateVariant.create()
                .put(VariantSettings.MODEL, modelId);
            
            if (rotation != 0) {
                variant = variant.put(VariantSettings.Y, VariantSettings.Rotation.valueOf("R" + rotation));
            }
            
            variantMap.put(SHAPES[i], variant);
        }
        
        generator.blockStateCollector.accept(
            VariantsBlockStateSupplier.create(block)
                .coordinate(BlockStateVariantMap.create(RailBlock.SHAPE)
                    .register(RailShape.NORTH_SOUTH, variantMap.get("shape=north_south"))
                    .register(RailShape.EAST_WEST, variantMap.get("shape=east_west"))
                    .register(RailShape.ASCENDING_EAST, variantMap.get("shape=ascending_east"))
                    .register(RailShape.ASCENDING_WEST, variantMap.get("shape=ascending_west"))
                    .register(RailShape.ASCENDING_NORTH, variantMap.get("shape=ascending_north"))
                    .register(RailShape.ASCENDING_SOUTH, variantMap.get("shape=ascending_south"))
                    .register(RailShape.SOUTH_EAST, variantMap.get("shape=south_east"))
                    .register(RailShape.SOUTH_WEST, variantMap.get("shape=south_west"))
                    .register(RailShape.NORTH_WEST, variantMap.get("shape=north_west"))
                    .register(RailShape.NORTH_EAST, variantMap.get("shape=north_east"))
                )
        );
    }

    private static void generateRailBlockStateWithRandomTextures(BlockStateModelGenerator generator, Block block, String[][] textureArrays) {
        String blockName = getBlockName(block);
        
        // For random textures, we'll simplify and just create random variants for the flat model
        // This follows the pattern of other exporters like SolidBlockExporter for random textures
        List<BlockStateVariant> variants = new ArrayList<>();
        for (int setIdx = 0; setIdx < textureArrays.length; setIdx++) {
            Identifier modelId = WesterosBlocks.id("block/" + blockName + "/flat_v" + (setIdx + 1));
            variants.add(BlockStateVariant.create().put(VariantSettings.MODEL, modelId));
        }
        
        generator.blockStateCollector.accept(
            VariantsBlockStateSupplier.create(block, variants.toArray(new BlockStateVariant[0]))
        );
    }

    private static void generateRailModels(BlockStateModelGenerator generator, Block block, String flatTexture, String curvedTexture) {
        String blockName = getBlockName(block);
        Set<String> generatedModels = new HashSet<>();

        for (String modelType : MODEL_TYPES) {
            if (generatedModels.add(modelType)) {
                String texture = modelType.equals("curved") ? curvedTexture : flatTexture;
                generateRailModel(generator, blockName, modelType, texture, "");
            }
        }
    }

    private static void generateRailModelsWithRandomTextures(BlockStateModelGenerator generator, Block block, String[][] textureArrays) {
        String blockName = getBlockName(block);

        for (int setIdx = 0; setIdx < textureArrays.length; setIdx++) {
            String[] texturePaths = textureArrays[setIdx];
            String flatTexture = texturePaths[0];
            String curvedTexture = texturePaths.length > 1 ? texturePaths[1] : texturePaths[0];
            String variantSuffix = "_v" + (setIdx + 1);
            
            Set<String> generatedModels = new HashSet<>();

            for (String modelType : MODEL_TYPES) {
                if (generatedModels.add(modelType)) {
                    String texture = modelType.equals("curved") ? curvedTexture : flatTexture;
                    generateRailModel(generator, blockName, modelType, texture, variantSuffix);
                }
            }
        }
    }

    private static void generateRailModel(BlockStateModelGenerator generator, String blockName, String modelType, String texture, String variantSuffix) {
        TextureMap textureMap = new TextureMap()
                .put(TextureKey.RAIL, createBlockIdentifier(texture));

        Model model = switch(modelType) {
            case "flat" -> ModModels.RAIL_FLAT;
            case "curved" -> ModModels.RAIL_CURVED;
            case "raised_ne" -> ModModels.TEMPLATE_RAIL_RAISED_NE;
            case "raised_sw" -> ModModels.TEMPLATE_RAIL_RAISED_SW;
            default -> ModModels.RAIL_FLAT;
        };

        Identifier modelId = WesterosBlocks.id("block/" + blockName + "/" + modelType + variantSuffix);
        model.upload(modelId, textureMap, generator.modelCollector);
    }

}