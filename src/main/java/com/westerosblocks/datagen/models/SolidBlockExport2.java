package com.westerosblocks.datagen.models;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.custom.WCSolidBlock2;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.Map;

/**
 * Solid block model export for the builder system.
 * Generates block state models and item models for solid blocks.
 */
public class SolidBlockExport2 {
    
    public SolidBlockExport2() {
    }
    
    /**
     * Generates block state models for a solid block.
     * 
     * @param generator The block state model generator
     * @param block The solid block
     * @param texture The texture path for the block
     */
    public void generateBlockStateModels(BlockStateModelGenerator generator, Block block, String texture) {
        // Check if this is a WCSolidBlock2 with state properties
        if (block instanceof WCSolidBlock2 solidBlock && solidBlock.stateProperty != null) {
            generateStatefulBlockModels(generator, solidBlock);
        } else {
            // Generate the block model
            generateSolidModel(generator, block, texture);
            
            // Generate block state
            BlockStateVariant variant = BlockStateVariant.create()
                    .put(VariantSettings.MODEL, getModelId(block));
            
            generator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(block, variant)
            );
        }
    }

    /**
     * Generates block state models for a solid block with multiple textures.
     * 
     * @param generator The block state model generator
     * @param block The solid block
     * @param textures The texture paths for the block (bottom, top, side)
     */
    public void generateBlockStateModels(BlockStateModelGenerator generator, Block block, String... textures) {
        // Check if this is a WCSolidBlock2 with state properties
        if (block instanceof WCSolidBlock2 solidBlock && solidBlock.stateProperty != null) {
            generateStatefulBlockModels(generator, solidBlock);
        } else {
            // Generate the block model with multiple textures
            generateSolidModel(generator, block, textures);
            
            // Generate block state
            BlockStateVariant variant = BlockStateVariant.create()
                    .put(VariantSettings.MODEL, getModelId(block));
            
            generator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(block, variant)
            );
        }
    }

    /**
     * Generates block state models for a solid block with state properties.
     * 
     * @param generator The block state model generator
     * @param block The solid block with state properties
     */
    private void generateStatefulBlockModels(BlockStateModelGenerator generator, WCSolidBlock2 block) {
        // Check if this block uses multi-texture states
        if (block.hasMultiTextureStates()) {
            generateMultiTextureStatefulBlockModels(generator, block);
        } else {
            generateSingleTextureStatefulBlockModels(generator, block);
        }
    }

    /**
     * Generates block state models for a solid block with single-texture state properties.
     * 
     * @param generator The block state model generator
     * @param block The solid block with state properties
     */
    private void generateSingleTextureStatefulBlockModels(BlockStateModelGenerator generator, WCSolidBlock2 block) {
        Map<String, String> stateTextures = block.getStateTextures();
        
        // Generate models for each state
        for (Map.Entry<String, String> entry : stateTextures.entrySet()) {
            String stateValue = entry.getKey();
            String texture = entry.getValue();
            
            // Generate model for this state
            generateSolidModel(generator, block, texture, stateValue);
        }
        
        // Generate block state with variants for each state
        BlockStateVariant[] variants = new BlockStateVariant[stateTextures.size()];
        int i = 0;
        for (String stateValue : stateTextures.keySet()) {
            variants[i] = BlockStateVariant.create()
                    .put(VariantSettings.MODEL, getModelId(block, stateValue));
            i++;
        }
        
        generator.blockStateCollector.accept(
            VariantsBlockStateSupplier.create(block, variants)
        );
    }

    /**
     * Generates block state models for a solid block with multi-texture state properties.
     * 
     * @param generator The block state model generator
     * @param block The solid block with multi-texture state properties
     */
    private void generateMultiTextureStatefulBlockModels(BlockStateModelGenerator generator, WCSolidBlock2 block) {
        Map<String, String[]> stateMultiTextures = block.getStateMultiTextures();
        
        // Generate models for each state
        for (Map.Entry<String, String[]> entry : stateMultiTextures.entrySet()) {
            String stateValue = entry.getKey();
            String[] textures = entry.getValue();
            
            // Generate model for this state with multiple textures
            generateSolidModel(generator, block, stateValue, textures);
        }
        
        // Generate block state with variants for each state (following Fabric docs pattern)
        BlockStateVariant[] variants = new BlockStateVariant[stateMultiTextures.size()];
        int i = 0;
        for (String stateValue : stateMultiTextures.keySet()) {
            variants[i] = BlockStateVariant.create()
                    .put(VariantSettings.MODEL, getModelId(block, stateValue));
            i++;
        }
        
        generator.blockStateCollector.accept(
            VariantsBlockStateSupplier.create(block, variants)
        );
    }
    
    /**
     * Generates item models for a solid block.
     * 
     * @param itemModelGenerator The item model generator
     * @param block The solid block
     * @param texture The texture path for the block
     */
    public void generateItemModels(ItemModelGenerator itemModelGenerator, Block block, String texture) {
        String texturePath = String.format("block/%s", texture);
        Identifier textureId = WesterosBlocks.id(texturePath);

        Models.CUBE_ALL.upload(
                ModelIds.getItemModelId(block.asItem()),
                TextureMap.all(textureId),
                itemModelGenerator.writer
        );
    }
    
    /**
     * Generates the solid block model.
     * 
     * @param generator The block state model generator
     * @param block The solid block
     * @param texture The texture path for the block
     */
    private void generateSolidModel(BlockStateModelGenerator generator, Block block, String texture) {
        String texturePath = String.format("block/%s", texture);
        TextureMap textureMap = new TextureMap()
                .put(TextureKey.ALL, WesterosBlocks.id(texturePath));

        Models.CUBE_ALL.upload(getModelId(block), textureMap, generator.modelCollector);
    }

    /**
     * Generates the solid block model with multiple textures.
     * 
     * @param generator The block state model generator
     * @param block The solid block
     * @param textures The texture paths for the block (bottom, top, side)
     */
    private void generateSolidModel(BlockStateModelGenerator generator, Block block, String... textures) {
        // Expand textures to 6 items using the utility method
        String[] expandedTextures = TextureUtils.expandTextureArray(textures);
        
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
                .put(TextureKey.DOWN, WesterosBlocks.id("block/" + bottomTexture))
                .put(TextureKey.UP, WesterosBlocks.id("block/" + topTexture))
                .put(TextureKey.NORTH, WesterosBlocks.id("block/" + northTexture))
                .put(TextureKey.SOUTH, WesterosBlocks.id("block/" + southTexture))
                .put(TextureKey.WEST, WesterosBlocks.id("block/" + westTexture))
                .put(TextureKey.EAST, WesterosBlocks.id("block/" + eastTexture))
                .put(TextureKey.PARTICLE, WesterosBlocks.id("block/" + particleTexture));

        Models.CUBE.upload(getModelId(block), textureMap, generator.modelCollector);
    }

    /**
     * Generates the solid block model for a specific state.
     * 
     * @param generator The block state model generator
     * @param block The solid block
     * @param texture The texture path for the block
     * @param stateValue The state value
     */
    private void generateSolidModel(BlockStateModelGenerator generator, Block block, String texture, String stateValue) {
        String texturePath = String.format("block/%s", texture);
        TextureMap textureMap = new TextureMap()
                .put(TextureKey.ALL, WesterosBlocks.id(texturePath));

        Models.CUBE_ALL.upload(getModelId(block, stateValue), textureMap, generator.modelCollector);
    }

    /**
     * Generates the solid block model for a specific state with multiple textures.
     * 
     * @param generator The block state model generator
     * @param block The solid block
     * @param stateValue The state value
     * @param textures The texture paths for the block (bottom, top, side)
     */
    private void generateSolidModel(BlockStateModelGenerator generator, Block block, String stateValue, String... textures) {
        // Expand textures to 6 items using the utility method
        String[] expandedTextures = TextureUtils.expandTextureArray(textures);
        
        // Map textures to faces: [bottom, top, north, south, west, east]
        String bottomTexture = expandedTextures[0]; // down
        String topTexture = expandedTextures[1];    // up
        String northTexture = expandedTextures[2];  // north
        String southTexture = expandedTextures[3];  // south
        String westTexture = expandedTextures[4];   // west
        String eastTexture = expandedTextures[5];   // east
        String particleTexture = expandedTextures[2]; // Use north texture for particle

        // Create texture map following Fabric documentation patterns
        TextureMap textureMap = new TextureMap()
                .put(TextureKey.DOWN, WesterosBlocks.id("block/" + bottomTexture))
                .put(TextureKey.UP, WesterosBlocks.id("block/" + topTexture))
                .put(TextureKey.NORTH, WesterosBlocks.id("block/" + northTexture))
                .put(TextureKey.SOUTH, WesterosBlocks.id("block/" + southTexture))
                .put(TextureKey.WEST, WesterosBlocks.id("block/" + westTexture))
                .put(TextureKey.EAST, WesterosBlocks.id("block/" + eastTexture))
                .put(TextureKey.PARTICLE, WesterosBlocks.id("block/" + particleTexture));

        // Use CUBE model for multi-texture blocks (following Fabric docs)
        Models.CUBE.upload(getModelId(block, stateValue), textureMap, generator.modelCollector);
    }
    
    /**
     * Gets the model identifier for a block.
     * 
     * @param block The block
     * @return The model identifier
     */
    private Identifier getModelId(Block block) {
        return Identifier.of(WesterosBlocks.MOD_ID,
                String.format("block/%s", Registries.BLOCK.getId(block).getPath()));
    }

    /**
     * Gets the model identifier for a block with a specific state.
     * 
     * @param block The block
     * @param stateValue The state value
     * @return The model identifier
     */
    private Identifier getModelId(Block block, String stateValue) {
        return Identifier.of(WesterosBlocks.MOD_ID,
                String.format("block/%s_%s", Registries.BLOCK.getId(block).getPath(), stateValue));
    }
} 