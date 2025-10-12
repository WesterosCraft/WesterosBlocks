package com.westerosblocks.datagen.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.custom.WCFanBlock;
import com.westerosblocks.block.custom.WCWallFanBlock;
import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.datagen.ModModels;
import com.westerosblocks.datagen.ModTextureKey;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

/**
 * Exporter for fan blocks following block-models.md patterns.
 * Generates models for decorative fan blocks with both standing and wall-mounted variants.
 *
 * @see ModModels#FAN
 * @see ModModels#WALL_FAN
 * @see WCFanBlock
 * @see WCWallFanBlock
 */
public class FanBlockExporter extends BaseBlockExporter {

    public static void registerFanBlock(BlockStateModelGenerator generator, Block standingFan, String texturePath) {
        // Get the wall fan block using the same pattern as TorchBlockExporter
        Block wallFan = Registries.BLOCK.get(WesterosBlocks.id("wall_" + standingFan.getTranslationKey().replace("block.westerosblocks.", "")));

        // Generate standing fan block state
        generateStandingFanBlockState(generator, standingFan, texturePath);
        
        // Generate wall fan block state if it exists
        if (wallFan != null) {
            generateWallFanBlockState(generator, wallFan, texturePath);
        }
        
        // Generate item model for standing fan only (wall fan has no item)
        generateStandingFanItemModel(generator, standingFan, texturePath);
    }

    private static void generateStandingFanBlockState(BlockStateModelGenerator generator, Block block, String texturePath) {
        // Create the standing fan model
        Identifier standingModelId = createStandingFanModel(generator, block, texturePath);

        // Create block state variants for waterlogged states
        BlockStateVariantMap variants = BlockStateVariantMap.create(WCFanBlock.WATERLOGGED)
            .register(false, createVariant(standingModelId))
            .register(true, createVariant(standingModelId));

        // Register the block state using the generator's collector
        generator.blockStateCollector.accept(
            VariantsBlockStateSupplier.create(block).coordinate(variants)
        );
    }

    private static void generateWallFanBlockState(BlockStateModelGenerator generator, Block block, String texturePath) {
        // Create the wall fan model
        Identifier wallModelId = createWallFanModel(generator, block, texturePath);

        // Create variants for each facing direction and waterlogged state
        BlockStateVariantMap variants = BlockStateVariantMap.create(WCWallFanBlock.FACING, WCWallFanBlock.WATERLOGGED)
            .register(Direction.NORTH, false, createVariant(wallModelId))
            .register(Direction.NORTH, true, createVariant(wallModelId))
            .register(Direction.EAST, false, createVariant(wallModelId, 90))
            .register(Direction.EAST, true, createVariant(wallModelId, 90))
            .register(Direction.SOUTH, false, createVariant(wallModelId, 180))
            .register(Direction.SOUTH, true, createVariant(wallModelId, 180))
            .register(Direction.WEST, false, createVariant(wallModelId, 270))
            .register(Direction.WEST, true, createVariant(wallModelId, 270));

        // Register the block state
        generator.blockStateCollector.accept(
            VariantsBlockStateSupplier.create(block).coordinate(variants)
        );
    }

    private static Identifier createStandingFanModel(BlockStateModelGenerator generator, Block block, String texturePath) {
        // Create texture map for standing fan
        TextureMap textureMap = new TextureMap()
            .put(ModTextureKey.FAN, createBlockIdentifier(texturePath));

        // Create model identifier
        Identifier modelId = createModelId(block);

        // Use predefined ModModels fan model
        ModModels.FAN.upload(modelId, textureMap, generator.modelCollector);

        return modelId;
    }

    private static Identifier createWallFanModel(BlockStateModelGenerator generator, Block block, String texturePath) {
        // Create texture map for wall fan
        TextureMap textureMap = new TextureMap()
            .put(ModTextureKey.FAN, createBlockIdentifier(texturePath));

        // Create model identifier
        Identifier modelId = createModelId(block);

        // Use predefined ModModels wall fan model
        ModModels.WALL_FAN.upload(modelId, textureMap, generator.modelCollector);

        return modelId;
    }

    public static void generateItemModels(ItemModelGenerator generator, Block standingFan) {
        // Only generate item model for the standing fan (wall fan has no item)
        generateItemModel(generator, standingFan);
    }

    private static void generateStandingFanItemModel(BlockStateModelGenerator generator, Block block, String texturePath) {
        // Create texture map for the item model
        TextureMap textureMap = TextureMap.layer0(createBlockIdentifier(texturePath));
        // Generate item model using the generator's registerParentedItemModel method
        Identifier itemModelId = ModelIds.getItemModelId(block.asItem());
        Models.GENERATED.upload(itemModelId, textureMap, generator.modelCollector);
    }

    // Utility method for generating item models in ItemModelGenerator
    private static void generateItemModel(ItemModelGenerator generator, Block block) {
        String texturePath = getTexturePath(block);
        TextureMap textureMap = TextureMap.layer0(createBlockIdentifier(texturePath));
        Models.GENERATED.upload(ModelIds.getItemModelId(block.asItem()), textureMap, generator.writer);
    }

    private static String getTexturePath(Block block) {
        // Extract block name and return appropriate texture path
        String blockName = block.getTranslationKey().replace("block.westerosblocks.", "");

        // For now, assume all fan blocks use a consistent naming convention
        // This can be expanded as needed for specific fan variants
        return "fan/" + blockName.replace("_fan", "").replace("wall_", "");
    }

    /**
     * Registers a fan block from a BlockDefinition
     */
    public static void registerFanBlockFromDefinition(BlockStateModelGenerator generator, Block standingFan, BlockDefinition definition) {
        // Check if the definition has random textures
        if (definition.hasRandomTextures()) {
            registerFanBlockWithRandomTextures(generator, standingFan, definition);
        } else {
            // Get primary texture from definition
            String texturePath = getTextureFromDefinition(definition);
            // Use the existing registerFanBlock method which handles wall fan lookup
            registerFanBlock(generator, standingFan, texturePath);
        }
    }

    /**
     * Registers a fan block with random textures from definition.
     * This method mimics the original registerCustomFanBlock().randomTexture().randomTexture().build() pattern
     */
    private static void registerFanBlockWithRandomTextures(BlockStateModelGenerator generator, Block standingFan, BlockDefinition definition) {
        // Extract all random texture paths from the definition
        String[] randomTexturePaths = definition.getRandomTextures().stream()
            .map(variant -> variant.getTextures().get(0)) // Get first texture from each variant
            .toArray(String[]::new);

        // Use the first texture as the primary texture for the model generation
        String primaryTexture = randomTexturePaths.length > 0 ? randomTexturePaths[0] : getTextureFromDefinition(definition);

        // Use the existing registerFanBlock method which handles wall fan lookup and model generation
        registerFanBlock(generator, standingFan, primaryTexture);
    }

    private static String getTextureFromDefinition(BlockDefinition definition) {
        if (definition.getTextures() != null && !definition.getTextures().isEmpty()) {
            return definition.getTextures().get(0);
        }
        // Fallback to a default texture based on block name
        return "fan/" + definition.getBlockName().replace("_fan", "");
    }
}
