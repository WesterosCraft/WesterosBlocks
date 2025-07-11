package com.westerosblocks.datagen.models;

import com.westerosblocks.WesterosBlocks;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.Optional;

/**
 * LadderBlockExport that extends ModelExport2 for consistent model generation
 * patterns.
 * Handles ladder blocks with cardinal direction variants using
 * BlockStateVariantMap.
 */
public class LadderBlockExport extends ModelExport2 {

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator generator, Block block, String texturePath) {
        // Create the ladder model with proper texture mapping
        Identifier ladderModelId = createLadderModel(generator, block, texturePath);

        // Create block state variants using BlockStateVariantMap with the FACING
        // property
        BlockStateVariantMap variants = BlockStateVariantMap.create(Properties.HORIZONTAL_FACING)
                .register(Direction.NORTH, createVariant(ladderModelId))
                .register(Direction.EAST, createVariant(ladderModelId, 90))
                .register(Direction.SOUTH, createVariant(ladderModelId, 180))
                .register(Direction.WEST, createVariant(ladderModelId, 270));

        // Register the block state using the utility method
        registerBlockState(generator, block, variants);
    }

    private Identifier createLadderModel(BlockStateModelGenerator generator, Block block, String texturePath) {
        // Create texture map with the correct texture keys for the untinted ladder
        // parent
        TextureMap textureMap = new TextureMap()
                .put(TextureKey.TEXTURE, createBlockIdentifier(texturePath))
                .put(TextureKey.PARTICLE, createBlockIdentifier(texturePath));

        // Create model ID
        Identifier modelId = WesterosBlocks.id(
                "block/generated/" + block.getTranslationKey().replace("block.westerosblocks.", "") + "_ladder");

        // Create and upload the model using the untinted ladder parent
        Model model = new Model(
                Optional.of(Identifier.of("westerosblocks", "block/untinted/ladder")),
                Optional.empty(),
                TextureKey.TEXTURE,
                TextureKey.PARTICLE);
        model.upload(modelId, textureMap, generator.modelCollector);

        return modelId;
    }

    @Override
    public void generateItemModels(ItemModelGenerator generator, Block block) {
        // Create a simple item model that inherits from the block model
        String modelPath = "block/generated/" + block.getTranslationKey().replace("block.westerosblocks.", "")
                + "_ladder";
        Model model = new Model(
                Optional.of(Identifier.of("westerosblocks", modelPath)),
                Optional.empty());
        generator.register(block.asItem(), model);
    }
}
