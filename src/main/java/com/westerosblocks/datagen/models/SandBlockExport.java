package com.westerosblocks.datagen.models;

import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

/**
 * Export class for standalone sand blocks that extends ModelExport2.
 * Generates block state models and item models for sand blocks using BlockStateModelGenerator.
 * Sand blocks are simple blocks that don't have complex state variations.
 */
public class SandBlockExport extends ModelExport2 {

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator generator, Block block, String texturePath) {
        // Create the base model for the sand block using utility methods from ModelExport2
        // Use "all" texture key for cube_all parent model
        Identifier modelId = createModel(
            generator, block,
            "minecraft:block/cube_all",
            "westerosblocks:block/" + texturePath,
            "base",
            "all"
        );

        // Register a simple block state with no variations
        // For blocks with no properties, we can use the registerSimpleCubeAll method
        generator.registerSimpleCubeAll(block);
    }

    @Override
    public void generateItemModels(ItemModelGenerator generator, Block block) {
        // Create a simple item model that inherits from the block model
        generateItemModel(generator, block, "base");
    }

    /**
     * Generates block state models for a sand block with custom dust color.
     * Overloaded method that calls the basic implementation.
     * 
     * @param generator The BlockStateModelGenerator to register the models with
     * @param block The sand block to generate models for
     * @param texturePath The texture path for the sand block
     * @param dustColor The dust color for falling particles (optional)
     */
    public void generateBlockStateModels(BlockStateModelGenerator generator, Block block, String texturePath, String dustColor) {
        // For now, we'll use the same implementation as the basic version
        // In the future, this could be extended to handle custom dust colors
        generateBlockStateModels(generator, block, texturePath);
    }
} 