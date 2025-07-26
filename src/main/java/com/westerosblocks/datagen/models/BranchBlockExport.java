package com.westerosblocks.datagen.models;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.custom.WCBranchBlock;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.Optional;

public class BranchBlockExport extends ModelExport2 {

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator generator, Block block, String texturePath) {
        // For backward compatibility, convert single texture to array
        generateBlockStateModels(generator, block, new String[] { texturePath });
    }

    public void generateBlockStateModels(BlockStateModelGenerator generator, Block block, String... texturePaths) {
        // Get the branch type from the block
        String branchType = "large_branch"; // Default branch type
        if (block instanceof WCBranchBlock) {
            branchType = ((WCBranchBlock) block).getBranchType();
        }

        // Create models for each axis
        Identifier xModelId = createBranchModel(generator, block, branchType, texturePaths, "x");
        Identifier yModelId = createBranchModel(generator, block, branchType, texturePaths, "y");
        Identifier zModelId = createBranchModel(generator, block, branchType, texturePaths, "z");

        // Create block state variants using BlockStateVariantMap with the AXIS property
        BlockStateVariantMap variants = BlockStateVariantMap.create(WCBranchBlock.AXIS)
                .register(Direction.Axis.X, BlockStateVariant.create()
                        .put(VariantSettings.MODEL, xModelId)
                        .put(VariantSettings.X, VariantSettings.Rotation.R90)
                        .put(VariantSettings.Y, VariantSettings.Rotation.R90))
                .register(Direction.Axis.Y, createVariant(yModelId))
                .register(Direction.Axis.Z, BlockStateVariant.create()
                        .put(VariantSettings.MODEL, zModelId)
                        .put(VariantSettings.X, VariantSettings.Rotation.R90));

        // Register the block state using the utility method
        registerBlockState(generator, block, variants);
    }

    private Identifier createBranchModel(BlockStateModelGenerator generator, Block block, String branchType,
            String[] texturePaths, String axis) {
        // Expand texturePaths to 6 items by filling remaining slots with the last
        // texture
        String[] expandedTextures = expandTextureArray(texturePaths);

        // Map textures to faces: [bottom, top, north, south, west, east]
        String bottomTexture = expandedTextures[0]; // down
        String topTexture = expandedTextures[1]; // up
        String northTexture = expandedTextures[2]; // north
        String southTexture = expandedTextures[3]; // south
        String westTexture = expandedTextures[4]; // west
        String eastTexture = expandedTextures[5]; // east
        String particleTexture = expandedTextures[2]; // Use north texture for particle

        // Create texture map with proper mapping for each face
        TextureMap textureMap = new TextureMap()
                .put(TextureKey.DOWN, createBlockIdentifier(bottomTexture))
                .put(TextureKey.UP, createBlockIdentifier(topTexture))
                .put(TextureKey.NORTH, createBlockIdentifier(northTexture))
                .put(TextureKey.SOUTH, createBlockIdentifier(southTexture))
                .put(TextureKey.WEST, createBlockIdentifier(westTexture))
                .put(TextureKey.EAST, createBlockIdentifier(eastTexture))
                .put(TextureKey.PARTICLE, createBlockIdentifier(particleTexture));

        Identifier modelId = WesterosBlocks.id(
                "block/generated/" + block.getTranslationKey().replace("block.westerosblocks.", "") + "/" + axis);

        // Use a custom model template for branches, or fall back to log template
        Optional<Identifier> parentModel = Optional.empty();
        if ("large_branch".equals(branchType)) {
            parentModel = Optional.of(WesterosBlocks.id("block/custom/branches/large_branch"));
        } else {
            parentModel = Optional.of(WesterosBlocks.id("block/untinted/cube_log")); // Default to log template
        }
        // Add more branch type specific templates here as needed

        Model model = new Model(parentModel, Optional.empty(),
                TextureKey.DOWN, TextureKey.UP, TextureKey.NORTH, TextureKey.SOUTH, TextureKey.WEST, TextureKey.EAST,
                TextureKey.PARTICLE);
        model.upload(modelId, textureMap, generator.modelCollector);

        return modelId;
    }

    /**
     * Expands a texture array to exactly 6 items by filling remaining slots with
     * the last texture.
     * If the input array has fewer than 6 items, the last item is used to fill the
     * remaining slots.
     * 
     * @param texturePaths The input texture array
     * @return An array with exactly 6 texture paths
     */
    private String[] expandTextureArray(String... texturePaths) {
        if (texturePaths == null || texturePaths.length == 0) {
            // Return default textures if none provided
            return new String[] { "oak_branch", "oak_branch", "oak_branch", "oak_branch", "oak_branch", "oak_branch" };
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

    @Override
    public void generateItemModels(ItemModelGenerator generator, Block block) {
        // Use the vertical (Y-axis) model for the item
        String modelPath = "block/generated/" + block.getTranslationKey().replace("block.westerosblocks.", "") + "/y";
        Model model = new Model(
                Optional.of(Identifier.of("westerosblocks", modelPath)),
                Optional.empty());
        generator.register(block.asItem(), model);
    }
}