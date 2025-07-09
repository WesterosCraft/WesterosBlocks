package com.westerosblocks.datagen.models;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.custom.StandaloneFlowerPotBlock;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

import java.util.Optional;

/**
 * Exporter for StandaloneFlowerPotBlock instances.
 * 
 * <p>This exporter generates block state models and item models for flower pot blocks
 * that use the standalone system. It supports both tinted and untinted variants
 * and handles the three texture components: dirt, flower pot, and plant.
 * 
 * <p>Usage:
 * <pre>{@code
 * StandaloneFlowerPotBlockExport flowerPotExporter = new StandaloneFlowerPotBlockExport();
 * flowerPotExporter.generateBlockStateModels(generator, block, "dirt", "flower_pot", "plant");
 * flowerPotExporter.generateItemModels(itemGenerator, block);
 * }</pre>
 */
public class StandaloneFlowerPotBlockExport extends ModelExport2 {

    /**
     * Generates block state models for a flower pot block.
     * 
     * @param generator The BlockStateModelGenerator to use
     * @param block The flower pot block
     * @param dirtTexture The dirt texture path (e.g., "minecraft:block/dirt")
     * @param flowerPotTexture The flower pot texture path (e.g., "minecraft:block/flower_pot")
     * @param plantTexture The plant texture path (e.g., "flowers/blue_bells")
     */
    public void generateBlockStateModels(BlockStateModelGenerator generator, Block block, 
                                       String dirtTexture, String flowerPotTexture, String plantTexture) {
        
        // Create the model with the three textures
        Identifier modelId = createModelId(block);
        createFlowerPotModel(generator, block, modelId, dirtTexture, flowerPotTexture, plantTexture, false);
        
        // Register the block state with a simple variant (no rotation needed for flower pots)
        BlockStateVariant variant = createVariant(modelId);
        generator.blockStateCollector.accept(
            VariantsBlockStateSupplier.create(block, variant)
        );
    }

    /**
     * Generates block state models for a flower pot block with tinting support.
     * 
     * @param generator The BlockStateModelGenerator to use
     * @param block The flower pot block
     * @param dirtTexture The dirt texture path (e.g., "minecraft:block/dirt")
     * @param flowerPotTexture The flower pot texture path (e.g., "minecraft:block/flower_pot")
     * @param plantTexture The plant texture path (e.g., "flowers/blue_bells")
     * @param isTinted Whether the plant should be tinted
     */
    public void generateBlockStateModels(BlockStateModelGenerator generator, Block block, 
                                       String dirtTexture, String flowerPotTexture, String plantTexture, boolean isTinted) {
        
        // Create the model with the three textures
        Identifier modelId = createModelId(block);
        createFlowerPotModel(generator, block, modelId, dirtTexture, flowerPotTexture, plantTexture, isTinted);
        
        // Register the block state with a simple variant (no rotation needed for flower pots)
        BlockStateVariant variant = createVariant(modelId);
        generator.blockStateCollector.accept(
            VariantsBlockStateSupplier.create(block, variant)
        );
    }

    /**
     * Overloaded method that takes a single texture path and assumes standard textures.
     * 
     * @param generator The BlockStateModelGenerator to use
     * @param block The flower pot block
     * @param plantTexture The plant texture path
     */
    @Override
    public void generateBlockStateModels(BlockStateModelGenerator generator, Block block, String plantTexture) {
        generateBlockStateModels(generator, block, 
            "minecraft:block/dirt", 
            "minecraft:block/flower_pot", 
            plantTexture);
    }

    /**
     * Creates a flower pot model with the specified textures.
     * 
     * @param generator The BlockStateModelGenerator to use
     * @param block The flower pot block
     * @param modelId The model identifier
     * @param dirtTexture The dirt texture path
     * @param flowerPotTexture The flower pot texture path
     * @param plantTexture The plant texture path
     * @param isTinted Whether the plant should be tinted
     */
    private void createFlowerPotModel(BlockStateModelGenerator generator, Block block, Identifier modelId,
                                    String dirtTexture, String flowerPotTexture, String plantTexture, boolean isTinted) {
        
        // Create texture map with the three required textures
        TextureMap textureMap = new TextureMap()
            .put(TextureKey.DIRT, createBlockIdentifier(dirtTexture))
            .put(ModTextureKey.FLOWER_POT, createBlockIdentifier(flowerPotTexture))
            .put(TextureKey.PLANT, createBlockIdentifier(plantTexture))
            .put(TextureKey.PARTICLE, createBlockIdentifier(flowerPotTexture));

        // Determine if we should use tinted or untinted template
        String parentPath = isTinted ? "tinted/flower_pot_cross" : "untinted/flower_pot_cross";

        // Create the model
        Model model = new Model(
            Optional.of(WesterosBlocks.id("block/" + parentPath)),
            Optional.empty(),
            TextureKey.DIRT,
            ModTextureKey.FLOWER_POT,
            TextureKey.PLANT,
            TextureKey.PARTICLE
        );
        
        // Upload the model
        model.upload(modelId, textureMap, generator.modelCollector);
    }

    /**
     * Generates item models for a flower pot block.
     * 
     * @param generator The ItemModelGenerator to use
     * @param block The flower pot block
     */
    @Override
    public void generateItemModels(ItemModelGenerator generator, Block block) {
        // For flower pots, the item model should inherit from the block model
        // This ensures the item gets the same tinting as the block
        String modelPath = "block/generated/" + block.getTranslationKey().replace("block.westerosblocks.", "");
        Model model = new Model(
            Optional.of(WesterosBlocks.id(modelPath)),
            Optional.empty()
        );
        generator.register(block.asItem(), model);
    }

    /**
     * Generates item models for a flower pot block with a custom texture.
     * 
     * @param generator The ItemModelGenerator to use
     * @param block The flower pot block
     * @param itemTexture The item texture path
     */
    public void generateItemModels(ItemModelGenerator generator, Block block, String itemTexture) {
        // Create a simple item model that uses the specified texture
        Identifier modelId = createModelId(block);
        
        TextureMap textureMap = new TextureMap()
            .put(TextureKey.LAYER0, createBlockIdentifier(itemTexture));
        
        Models.GENERATED.upload(
            ModelIds.getItemModelId(block.asItem()),
            textureMap,
            generator.writer
        );
    }
} 