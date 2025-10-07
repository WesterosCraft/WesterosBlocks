package com.westerosblocks.datagen.custom;

import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;

/**
 * Exporter for plant blocks following block-models.md patterns.
 * Delegates to CrossBlockExporter for actual model generation with plant-specific configuration.
 *
 * <p>Structure follows block-models.md sections 5.2-5.6:
 * <ul>
 *   <li>Model instances (delegated to CrossBlockExporter)</li>
 *   <li>TextureMap builders (delegated to CrossBlockExporter)</li>
 *   <li>BlockStateSupplier methods (delegated to CrossBlockExporter)</li>
 *   <li>BlockDefinition integration (registerCustomPlantBlock)</li>
 * </ul>
 *
 * <p><b>Plant Block Variants:</b>
 * <ul>
 *   <li><b>Standard Plants:</b> Cross-shaped decorative blocks with single or random textures</li>
 *   <li><b>Layer-Sensitive Plants:</b> Height-sensitive variants (layers 1-8) for grass, flowers, etc.</li>
 *   <li><b>Tinted Plants:</b> Biome-colored foliage using color multipliers</li>
 *   <li><b>Rotation Support:</b> 4 random rotations for natural variation</li>
 * </ul>
 *
 * <p><b>Delegation Strategy:</b>
 * <ul>
 *   <li>Analyzes BlockDefinition for texture type and layer sensitivity</li>
 *   <li>Determines tinting from colorMult property</li>
 *   <li>Routes to appropriate CrossBlockExporter method</li>
 *   <li>Maintains separation of concerns while avoiding code duplication</li>
 * </ul>
 *
 * <p><b>Type Detection:</b>
 * <ul>
 *   <li>Layer Sensitivity: Detected via {@code type="layerSensitive"} in BlockDefinition</li>
 *   <li>Tinting: Detected via presence of {@code colorMult} property</li>
 *   <li>Random Textures: Uses {@code randomTextures} array if present</li>
 * </ul>
 *
 * <p><b>Texture Order:</b> Delegates texture handling to CrossBlockExporter
 *
 * @see CrossBlockExporter
 * @see BlockDefinition
 */
public class PlantBlockExporter extends BaseBlockExporter {

    /**
     * Registers a custom plant block using BlockDefinition
     */
    public static void registerCustomPlantBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        // Validate texture data using centralized method
        definition.validateTextureData();

        String blockName = getBlockName(block);
        boolean isLayerSensitive = definition.getType() != null && "layerSensitive".equals(definition.getType());

        // Use centralized tinting detection
        boolean isTinted = definition.shouldUseTintedModel();

        // Use centralized priority logic
        BlockDefinition.TextureSource source = definition.getPrimaryTextureSource();

        if (source == BlockDefinition.TextureSource.RANDOM_TEXTURES) {
            // Handle random textures - flatten all texture variants
            String[] texturePaths = definition.getRandomTextureVariantSets().stream()
                .flatMap(variant -> variant.textures.stream())
                .toArray(String[]::new);

            if (isLayerSensitive) {
                CrossBlockExporter.generateLayerSensitiveCrossWithRandomTextures(
                    generator, block, texturePaths, isTinted, 4
                );
            } else {
                CrossBlockExporter.generateCrossWithRandomTextures(
                    generator, block, texturePaths, isTinted, 4
                );
            }
        } else if (source == BlockDefinition.TextureSource.TEXTURES) {
            // Handle single texture using centralized method
            String texturePath = definition.getFirstTexture();

            if (isLayerSensitive) {
                CrossBlockExporter.generateLayerSensitiveCross(
                    generator, block, texturePath, isTinted, 1
                );
            } else {
                CrossBlockExporter.generateCross(
                    generator, block, texturePath, isTinted, 1
                );
            }
        }
    }
}