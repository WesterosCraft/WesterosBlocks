package com.westerosblocks.datagen.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.datagen.ModModels;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.state.property.Properties;

/**
 * Exporter for torch blocks following block-models.md patterns.
 * Generates models for both standing and wall torch variants.
 *
 * <p>Structure follows block-models.md sections 5.2-5.6:
 * <ul>
 *   <li>Model instances (references ModModels.TORCH, ModModels.TORCH_WALL)</li>
 *   <li>TextureMap builders (createTorchTextureMap)</li>
 *   <li>BlockStateSupplier methods (createWallTorchVariants)</li>
 *   <li>Clean datagen methods (registerTorchBlock, registerStandingTorch, registerWallTorch)</li>
 *   <li>BlockDefinition integration (registerTorchBlockFromDefinition)</li>
 * </ul>
 *
 * @see ModModels#TORCH
 * @see ModModels#TORCH_WALL
 */
public class TorchBlockExporter extends BaseBlockExporter {

    /**
     * Registers a torch block with both standing and wall variants.
     * Follows block-models.md pattern for multi-variant blocks.
     *
     * <p>Automatically finds and registers the corresponding wall torch variant
     * using the naming convention: wall_[standing_torch_name]
     *
     * @param generator The BlockStateModelGenerator to register models with
     * @param standingTorch The standing torch block
     * @param texturePath Texture path for the torch flame
     */
    public static void registerTorchBlock(BlockStateModelGenerator generator, Block standingTorch, String texturePath) {
        Block wallTorch = Registries.BLOCK.get(WesterosBlocks.id("wall_" + standingTorch.getTranslationKey().replace("block.westerosblocks.", "")));

        registerStandingTorch(generator, standingTorch, texturePath);
        // Pass standingTorch to registerWallTorch to keep models organized together
        registerWallTorch(generator, wallTorch, standingTorch, texturePath);
        registerSimpleItemModel(generator, standingTorch, createBlockIdentifier(texturePath));
    }

    /**
     * Registers a torch block from a BlockDefinition.
     * Automatically extracts texture from the definition and finds the wall torch variant.
     *
     * @param generator The BlockStateModelGenerator to register models with
     * @param standingTorch The standing torch block
     * @param definition The block definition containing texture information
     */
    public static void registerTorchBlockFromDefinition(BlockStateModelGenerator generator, Block standingTorch, BlockDefinition definition) {
        // Get texture from definition
        String texturePath = getTextureFromDefinition(definition);

        // Find the wall torch block
        Block wallTorch = Registries.BLOCK.get(WesterosBlocks.id("wall_" + definition.getBlockName()));

        if (wallTorch != null) {
            registerStandingTorch(generator, standingTorch, texturePath);
            // Pass standingTorch to registerWallTorch to keep models organized together
            registerWallTorch(generator, wallTorch, standingTorch, texturePath);
            registerSimpleItemModel(generator, standingTorch, createBlockIdentifier(texturePath));
        } else {
            WesterosBlocks.LOGGER.warn("Could not find wall torch for: {}", definition.getBlockName());
        }
    }

    // ========================================
    // Helper Methods (block-models.md 5.2-5.4)
    // ========================================

    /**
     * Extracts texture path from BlockDefinition with fallback.
     *
     * @param definition The block definition
     * @return Texture path (first texture from definition, or lighting/[blockName] fallback)
     */
    private static String getTextureFromDefinition(BlockDefinition definition) {
        if (definition.getTextures() != null && !definition.getTextures().isEmpty()) {
            return definition.getTextures().get(0);
        }
        // Fallback to a default texture based on block name
        return "lighting/" + definition.getBlockName();
    }

    /**
     * Creates a TextureMap for torch blocks.
     * Follows block-models.md section 5.3: Using Texture Map.
     *
     * @param texturePath Texture path for the torch
     * @return Configured TextureMap with TORCH key
     */
    private static TextureMap createTorchTextureMap(String texturePath) {
        return new TextureMap().put(TextureKey.TORCH, createBlockIdentifier(texturePath));
    }

    /**
     * Registers the standing torch variant.
     * Follows block-models.md section 5.2: Parent Block Model.
     *
     * @param generator The generator
     * @param block The standing torch block
     * @param texturePath Texture path
     */
    private static void registerStandingTorch(BlockStateModelGenerator generator, Block block, String texturePath) {
        TextureMap textureMap = createTorchTextureMap(texturePath);
        // Use "base" variant to organize torch models together
        Identifier modelId = ModModels.TORCH.upload(createNestedModelId(block, "base"), textureMap, generator.modelCollector);

        generator.blockStateCollector.accept(createSimpleBlockState(block, modelId));
    }

    /**
     * Registers the wall torch variant with directional facing.
     * Follows block-models.md section 5.4: Custom BlockStateSupplier Method.
     *
     * @param generator The generator
     * @param wallTorch The wall torch block
     * @param standingTorch The standing torch block (used for model directory organization)
     * @param texturePath Texture path
     */
    private static void registerWallTorch(BlockStateModelGenerator generator, Block wallTorch, Block standingTorch, String texturePath) {
        TextureMap textureMap = createTorchTextureMap(texturePath);
        // Use standing torch's directory with "wall" variant to keep models organized together
        Identifier modelId = ModModels.TORCH_WALL.upload(createNestedModelId(standingTorch, "wall"), textureMap, generator.modelCollector);

        BlockStateVariantMap variants = createWallTorchVariants(modelId);
        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(wallTorch).coordinate(variants));
    }

    /**
     * Creates blockstate variants for wall torch with directional facing.
     * Follows block-models.md section 5.4: Custom BlockStateSupplier Method.
     *
     * Note: Uses legacy rotation mapping from old WesterosBlocks (East=0°, not North=0°)
     *
     * @param modelId Model identifier for the wall torch
     * @return Configured BlockStateVariantMap for all horizontal directions
     */
    private static BlockStateVariantMap createWallTorchVariants(Identifier modelId) {
        return BlockStateVariantMap.create(Properties.HORIZONTAL_FACING)
            .register(Direction.EAST, createVariant(modelId))
            .register(Direction.SOUTH, createVariant(modelId, 90))
            .register(Direction.WEST, createVariant(modelId, 180))
            .register(Direction.NORTH, createVariant(modelId, 270));
    }
}