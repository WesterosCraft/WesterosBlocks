package com.westerosblocks.datagen.models;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.custom.WCLogBlock;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.Optional;

public class LogBlockExport {

    public static void generateBlockStateModels(BlockStateModelGenerator generator, Block block, String[] texturePaths) {
        // Create the base models for each axis
        // Use the correct parent model names: cube_log_horizontal for horizontal, cube_log for vertical
        Identifier xModelId = createLogModel(generator, block, "cube_log_horizontal", texturePaths, "x");
        Identifier yModelId = createLogModel(generator, block, "cube_log", texturePaths, "y");
        Identifier zModelId = createLogModel(generator, block, "cube_log_horizontal", texturePaths, "z");

        // Create block state variants using BlockStateVariantMap with the AXIS property
        BlockStateVariantMap variants = BlockStateVariantMap.create(WCLogBlock.AXIS)
            // X-axis variant (horizontal log) - x: 90, y: 90 (same as old system)
            .register(Direction.Axis.X, BlockStateVariant.create()
                .put(VariantSettings.MODEL, xModelId)
                .put(VariantSettings.X, VariantSettings.Rotation.R90)
                .put(VariantSettings.Y, VariantSettings.Rotation.R90))
            
            // Y-axis variant (vertical log) - no rotation (same as old system)
            .register(Direction.Axis.Y, BlockStateVariant.create()
                .put(VariantSettings.MODEL, yModelId))
            
            // Z-axis variant (horizontal log) - x: 90 (same as old system)
            .register(Direction.Axis.Z, BlockStateVariant.create()
                .put(VariantSettings.MODEL, zModelId)
                .put(VariantSettings.X, VariantSettings.Rotation.R90));

        // Register the block state using the generator's collector
        generator.blockStateCollector.accept(
            VariantsBlockStateSupplier.create(block)
                .coordinate(variants)
        );
    }

    private static Identifier createLogModel(BlockStateModelGenerator generator, Block block, String modelType, String[] texturePaths, String axis) {
        // Expand texturePaths to 6 items by filling remaining slots with the last texture
        String[] expandedTextures = expandTextureArray(texturePaths);

        // Map textures to faces: [bottom, top, north, south, west, east]
        String bottomTexture = expandedTextures[0]; // down
        String topTexture = expandedTextures[1];    // up
        String northTexture = expandedTextures[2];  // north
        String southTexture = expandedTextures[3];  // south
        String westTexture = expandedTextures[4];   // west
        String eastTexture = expandedTextures[5];   // east
        String particleTexture = expandedTextures[2]; // Use north texture for particle

        // Create texture map with proper mapping for each face
        TextureMap textureMap = new TextureMap()
                .put(TextureKey.DOWN, Identifier.of("westerosblocks", "block/" + bottomTexture))
                .put(TextureKey.UP, Identifier.of("westerosblocks", "block/" + topTexture))
                .put(TextureKey.NORTH, Identifier.of("westerosblocks", "block/" + northTexture))
                .put(TextureKey.SOUTH, Identifier.of("westerosblocks", "block/" + southTexture))
                .put(TextureKey.WEST, Identifier.of("westerosblocks", "block/" + westTexture))
                .put(TextureKey.EAST, Identifier.of("westerosblocks", "block/" + eastTexture))
                .put(TextureKey.PARTICLE, Identifier.of("westerosblocks", "block/" + particleTexture));

        // Create model identifier
        Identifier modelId = Identifier.of(WesterosBlocks.MOD_ID, 
            "block/generated/" + block.getTranslationKey().replace("block.westerosblocks.", "") + "/" + axis);

        // Create and upload the model using the correct parent model path
        // modelType is now the full parent model name (e.g., "cube_log_horizontal" or "cube_log")
        Model model = new Model(Optional.of(Identifier.of("westerosblocks", "block/untinted/" + modelType)), Optional.empty(),
                TextureKey.DOWN, TextureKey.UP, TextureKey.NORTH, TextureKey.SOUTH, TextureKey.WEST, TextureKey.EAST, TextureKey.PARTICLE);
        model.upload(modelId, textureMap, generator.modelCollector);

        return modelId;
    }

    /**
     * Expands a texture array to exactly 6 items by filling remaining slots with the last texture.
     * If the input array has fewer than 6 items, the last item is used to fill the remaining slots.
     * 
     * @param texturePaths The input texture array
     * @return An array with exactly 6 texture paths
     */
    private static String[] expandTextureArray(String[] texturePaths) {
        if (texturePaths == null || texturePaths.length == 0) {
            // Return default textures if none provided
            return new String[]{"stone", "stone", "stone", "stone", "stone", "stone"};
        }
        
        if (texturePaths.length >= 6) {
            // Already has 6 or more textures, return first 6
            String[] result = new String[6];
            System.arraycopy(texturePaths, 0, result, 0, 6);
            return result;
        }
        
        // Expand to 6 textures by filling remaining slots with the last texture
        String[] result = new String[6];
        System.arraycopy(texturePaths, 0, result, 0, texturePaths.length);
        
        String lastTexture = texturePaths[texturePaths.length - 1];
        for (int i = texturePaths.length; i < 6; i++) {
            result[i] = lastTexture;
        }
        
        return result;
    }

    public static void generateItemModels(ItemModelGenerator generator, Block block) {
        // Use the vertical (Y-axis) model for the item
        String modelPath = "block/generated/" + block.getTranslationKey().replace("block.westerosblocks.", "") + "/y";
        Model model = new Model(
            Optional.of(Identifier.of("westerosblocks", modelPath)),
            Optional.empty()
        );
        generator.register(block.asItem(), model);
    }
} 