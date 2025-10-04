package com.westerosblocks.datagen.custom;

import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;
import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.datagen.ModTextureMap;
import com.westerosblocks.block.custom.WCSolidBlock;
import com.westerosblocks.data.BlockDefinition;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Exporter for solid/cube blocks following block-models.md patterns.
 * Handles standard cubes, symmetrical blocks, tinted blocks, and blocks with random textures/states.
 *
 * <p>Structure follows block-models.md sections 5.2-5.6:
 * <ul>
 *   <li>Model instances (Models.CUBE_ALL, Models.CUBE, tinted variants)</li>
 *   <li>TextureMap builders (single texture, multiple textures, random variants)</li>
 *   <li>BlockStateSupplier methods (simple, symmetrical, random, states)</li>
 *   <li>Clean datagen methods (registerSimpleCustomSolidBlock, registerCustomSolidBlock)</li>
 *   <li>BlockDefinition integration (registerCustomSolidBlock with definition)</li>
 * </ul>
 *
 * <p><b>Block Variants Supported:</b>
 * <ul>
 *   <li><b>Standard Blocks:</b> Simple cube_all or multi-textured cubes</li>
 *   <li><b>Symmetrical Blocks:</b> Blocks with symmetrical property (mirrored variants)</li>
 *   <li><b>Tinted Blocks:</b> Blocks with biome-based tinting (grass, leaves, etc.)</li>
 *   <li><b>Random Textures:</b> Blocks with multiple texture variants for variation</li>
 *   <li><b>State-Based:</b> Blocks with custom state properties</li>
 * </ul>
 *
 * <p><b>Texture Order for Multi-Texture Cubes:</b>
 * {@code [down, up, north, south, east, west]}
 *
 * @see Models#CUBE_ALL
 * @see Models#CUBE
 */
public class SolidBlockExporter extends BaseBlockExporter {

    // ========================================
    // Utility Methods
    // ========================================

    /**
     * Checks if a block has the symmetrical property.
     * Symmetrical blocks generate separate model variants for mirrored states.
     *
     * @param block The block to check
     * @return {@code true} if the block is a WCSolidBlock with symmetrical=true
     */
    private static boolean isSymmetrical(Block block) {
        if (block instanceof WCSolidBlock) {
            return ((WCSolidBlock) block).symmetrical;
        }
        return false;
    }

    // ========================================
    // Public Registration Methods
    // ========================================

    /**
     * Registers a solid block with a single texture (cube_all pattern).
     * Follows block-models.md section 3.1: Simple Cube All.
     *
     * <p>Automatically handles symmetrical blocks if the block has the symmetrical property.
     *
     * @param generator The BlockStateModelGenerator
     * @param block The solid block to register
     * @param texturePath Single texture path used for all 6 faces
     */
    public static void registerSimpleCustomSolidBlock(BlockStateModelGenerator generator, Block block, String texturePath) {
        registerSimpleCustomSolidBlock(generator, block, texturePath, false);
    }

    /**
     * Registers a solid block with a single texture and tinted option.
     */
    public static void registerSimpleCustomSolidBlock(BlockStateModelGenerator generator, Block block, String texturePath, boolean isTinted) {
        if (isSymmetrical(block)) {
            registerSymmetricalSolidBlock(generator, block, texturePath, isTinted);
        } else {
            registerStandardSolidBlock(generator, block, texturePath, isTinted);
        }
    }

    /**
     * Standard solid block registration using cube_all or tinted model.
     */
    private static void registerStandardSolidBlock(BlockStateModelGenerator generator, Block block, String texturePath) {
        registerStandardSolidBlock(generator, block, texturePath, false);
    }

    /**
     * Standard solid block registration using cube_all or tinted model.
     */
    private static void registerStandardSolidBlock(BlockStateModelGenerator generator, Block block, String texturePath, boolean isTinted) {
        TextureMap textureMap = new TextureMap().put(TextureKey.ALL, createBlockIdentifier(texturePath));
        Identifier modelId;
        
        if (isTinted) {
            // Use tinted cube model
            Model tintedModel = new Model(
                Optional.of(WesterosBlocks.id("block/tinted/cube")),
                Optional.empty(),
                TextureKey.ALL
            );
            modelId = tintedModel.upload(createNestedModelId(block), textureMap, generator.modelCollector);
        } else {
            modelId = Models.CUBE_ALL.upload(createNestedModelId(block), textureMap, generator.modelCollector);
        }
        
        generator.blockStateCollector.accept(createSimpleBlockState(block, modelId));
        registerParentedItemModel(generator, block, modelId);
    }

    /**
     * Registers a solid block with multiple textures (cube pattern).
     * Texture order: down, up, north, south, east, west
     * Follows block-models.md section 3.2: Singletons with TextureMap.
     */
    public static void registerCustomSolidBlock(BlockStateModelGenerator generator, Block block, String... texturePaths) {
        registerCustomSolidBlock(generator, block, false, texturePaths);
    }

    /**
     * Registers a solid block with multiple textures and tinted option.
     */
    public static void registerCustomSolidBlock(BlockStateModelGenerator generator, Block block, boolean isTinted, String... texturePaths) {
        validateTexturePaths(texturePaths, 1);

        if (isSymmetrical(block)) {
            registerSymmetricalSolidBlock(generator, block, isTinted, texturePaths);
        } else {
            registerStandardSolidBlock(generator, block, isTinted, texturePaths);
        }
    }

    /**
     * Registers a solid block from a BlockDefinition.
     * Automatically handles textures vs randomTextures vs states and chooses the appropriate method.
     */
    public static void registerCustomSolidBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        // Check for states first (highest priority)
        if (definition.hasStates()) {
            // Convert states to String[][] format
            List<BlockDefinition.StateVariant> states = definition.getStates();
            String[][] textureArrays = new String[states.size()][];

            for (int i = 0; i < states.size(); i++) {
                List<String> stateTextures = states.get(i).getTextures();
                textureArrays[i] = stateTextures.toArray(new String[0]);
            }

            registerCustomSolidBlockWithStates(generator, block, textureArrays);

        } else if (definition.hasRandomTextures()) {
            // Convert randomTextures to String[][] format using helper
            List<TextureVariantSet> variants = extractRandomTextureVariants(definition);
            String[][] textureArrays = convertToTextureArrays(variants);

            registerCustomSolidBlockWithRandomTextures(generator, block, textureArrays);

        } else if (definition.getTextures() != null && !definition.getTextures().isEmpty()) {
            List<String> textures = definition.getTextures();

            if (textures.size() == 1) {
                // Single texture
                registerSimpleCustomSolidBlock(generator, block, textures.get(0));
            } else {
                // Multiple textures
                String[] textureArray = textures.toArray(new String[0]);
                registerCustomSolidBlock(generator, block, textureArray);
            }
        }
    }

    /**
     * Standard solid block registration using cube model with multiple textures.
     */
    private static void registerStandardSolidBlock(BlockStateModelGenerator generator, Block block, String... texturePaths) {
        registerStandardSolidBlock(generator, block, false, texturePaths);
    }

    /**
     * Standard solid block registration using cube model with multiple textures and tinted option.
     */
    private static void registerStandardSolidBlock(BlockStateModelGenerator generator, Block block, boolean isTinted, String... texturePaths) {
        String[] filledTextures = fillTextureArray(texturePaths);
        TextureMap textureMap = ModTextureMap.customAllSides(filledTextures);
        Identifier modelId;
        
        if (isTinted) {
            // Use tinted cube model
            Model tintedModel = new Model(
                Optional.of(WesterosBlocks.id("block/tinted/cube")),
                Optional.empty(),
                TextureKey.DOWN, TextureKey.UP, TextureKey.NORTH, TextureKey.SOUTH, TextureKey.EAST, TextureKey.WEST
            );
            modelId = tintedModel.upload(createNestedModelId(block), textureMap, generator.modelCollector);
        } else {
            modelId = Models.CUBE.upload(createNestedModelId(block), textureMap, generator.modelCollector);
        }
        
        generator.blockStateCollector.accept(createSimpleBlockState(block, modelId));
        registerParentedItemModel(generator, block, modelId);
    }

    /**
     * Registers a solid block with random texture variants.
     * Each inner array contains texture paths: down, up, north, south, east, west
     * Creates multiple model variants for random selection.
     */
    public static void registerCustomSolidBlockWithRandomTextures(BlockStateModelGenerator generator, Block block, String[][] textureArrays) {
        registerCustomSolidBlockWithRandomTextures(generator, block, textureArrays, false);
    }

    /**
     * Registers a solid block with random texture variants and tinted option.
     */
    public static void registerCustomSolidBlockWithRandomTextures(BlockStateModelGenerator generator, Block block, String[][] textureArrays, boolean isTinted) {
        if (textureArrays.length == 0) {
            throw new IllegalArgumentException("At least one texture array is required");
        }

        if (isSymmetrical(block)) {
            registerSymmetricalSolidBlockWithRandomTextures(generator, block, textureArrays, isTinted);
        } else {
            registerStandardSolidBlockWithRandomTextures(generator, block, textureArrays, isTinted);
        }
    }

    /**
     * Standard registration for solid blocks with random texture variants.
     */
    private static void registerStandardSolidBlockWithRandomTextures(BlockStateModelGenerator generator, Block block, String[][] textureArrays) {
        registerStandardSolidBlockWithRandomTextures(generator, block, textureArrays, false);
    }

    /**
     * Standard registration for solid blocks with random texture variants and tinted option.
     */
    private static void registerStandardSolidBlockWithRandomTextures(BlockStateModelGenerator generator, Block block, String[][] textureArrays, boolean isTinted) {
        List<Identifier> modelIds = new ArrayList<>();

        for (int i = 0; i < textureArrays.length; i++) {
            validateTexturePaths(textureArrays[i], 1);
            String[] filledTextures = fillTextureArray(textureArrays[i]);
            TextureMap textureMap = ModTextureMap.customAllSides(filledTextures);
            Identifier modelId;
            
            if (isTinted) {
                Model tintedModel = new Model(
                    Optional.of(WesterosBlocks.id("block/tinted/cube")),
                    Optional.empty(),
                    TextureKey.DOWN, TextureKey.UP, TextureKey.NORTH, TextureKey.SOUTH, TextureKey.EAST, TextureKey.WEST
                );
                modelId = tintedModel.upload(createNestedModelId(block, "base_v" + (i + 1)), textureMap, generator.modelCollector);
            } else {
                modelId = Models.CUBE.upload(createNestedModelId(block, "base_v" + (i + 1)), textureMap, generator.modelCollector);
            }
            modelIds.add(modelId);
        }

        List<BlockStateVariant> variants = modelIds.stream()
            .map(BaseBlockExporter::createVariant)
            .toList();

        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block, variants.toArray(new BlockStateVariant[0])));
        
        if (!modelIds.isEmpty()) {
            registerParentedItemModel(generator, block, modelIds.get(0));
        }
    }

    /**
     * Registers a solid block with multiple states.
     * Each inner array contains texture paths: down, up, north, south, east, west
     * Creates state-based variants with custom properties.
     */
    public static void registerCustomSolidBlockWithStates(BlockStateModelGenerator generator, Block block, String[][] textureArrays) {
        if (textureArrays.length == 0) {
            throw new IllegalArgumentException("At least one texture array is required");
        }

        if (isSymmetrical(block)) {
            registerSymmetricalSolidBlockWithStates(generator, block, textureArrays);
        } else {
            registerStandardSolidBlockWithStates(generator, block, textureArrays);
        }
    }

    /**
     * Standard registration for solid blocks with multiple states.
     */
    private static void registerStandardSolidBlockWithStates(BlockStateModelGenerator generator, Block block, String[][] textureArrays) {
        List<Identifier> modelIds = new ArrayList<>();

        for (int i = 0; i < textureArrays.length; i++) {
            validateTexturePaths(textureArrays[i], 1);
            String[] filledTextures = fillTextureArray(textureArrays[i]);
            TextureMap textureMap = ModTextureMap.customAllSides(filledTextures);
            Identifier modelId = Models.CUBE.upload(createNestedModelId(block, "state" + i + "_v" + (i + 1)), textureMap, generator.modelCollector);
            modelIds.add(modelId);
        }

        // Create custom blockstate supplier for state-based variants
        generator.blockStateCollector.accept(createStatesBlockState(block, modelIds));
        
        if (!modelIds.isEmpty()) {
            registerParentedItemModel(generator, block, modelIds.get(0));
        }
    }

    /**
     * Creates a blockstate supplier for state-based variants.
     */
    private static BlockStateSupplier createStatesBlockState(Block block, List<Identifier> modelIds) {
        return new BlockStateSupplier() {
            @Override
            public Block getBlock() {
                return block;
            }

            @Override
            public JsonElement get() {
                JsonObject json = new JsonObject();
                JsonObject variants = new JsonObject();

                for (int i = 0; i < modelIds.size(); i++) {
                    JsonObject variant = new JsonObject();
                    variant.addProperty("model", modelIds.get(i).toString());
                    variants.add("state=state" + i, variant);
                }

                json.add("variants", variants);
                return json;
            }
        };
    }

    // Symmetrical block registration methods - simplified

    /**
     * Registers a symmetrical solid block with single texture.
     */
    private static void registerSymmetricalSolidBlock(BlockStateModelGenerator generator, Block block, String texturePath) {
        registerSymmetricalSolidBlock(generator, block, texturePath, false);
    }

    /**
     * Registers a symmetrical solid block with single texture and tinted option.
     */
    private static void registerSymmetricalSolidBlock(BlockStateModelGenerator generator, Block block, String texturePath, boolean isTinted) {
        TextureMap textureMap = new TextureMap().put(TextureKey.ALL, createBlockIdentifier(texturePath));
        Identifier symmetricalModelId, asymmetricalModelId;
        
        if (isTinted) {
            Model tintedModel = new Model(
                Optional.of(WesterosBlocks.id("block/tinted/cube")),
                Optional.empty(),
                TextureKey.ALL
            );
            symmetricalModelId = tintedModel.upload(createNestedModelId(block, "symmetrical/base_v1"), textureMap, generator.modelCollector);
            asymmetricalModelId = tintedModel.upload(createNestedModelId(block, "asymmetrical/base_v1"), textureMap, generator.modelCollector);
        } else {
            symmetricalModelId = Models.CUBE_ALL.upload(createNestedModelId(block, "symmetrical/base_v1"), textureMap, generator.modelCollector);
            asymmetricalModelId = Models.CUBE_ALL.upload(createNestedModelId(block, "asymmetrical/base_v1"), textureMap, generator.modelCollector);
        }

        generator.blockStateCollector.accept(createSymmetricalBlockState(block, symmetricalModelId, asymmetricalModelId));
        registerParentedItemModel(generator, block, symmetricalModelId);
    }

    /**
     * Registers a symmetrical solid block with multiple textures.
     */
    private static void registerSymmetricalSolidBlock(BlockStateModelGenerator generator, Block block, String... texturePaths) {
        registerSymmetricalSolidBlock(generator, block, false, texturePaths);
    }

    /**
     * Registers a symmetrical solid block with multiple textures and tinted option.
     */
    private static void registerSymmetricalSolidBlock(BlockStateModelGenerator generator, Block block, boolean isTinted, String... texturePaths) {
        String[] filledTextures = fillTextureArray(texturePaths);
        TextureMap textureMap = ModTextureMap.customAllSides(filledTextures);
        Identifier symmetricalModelId, asymmetricalModelId;
        
        if (isTinted) {
            Model tintedModel = new Model(
                Optional.of(WesterosBlocks.id("block/tinted/cube")),
                Optional.empty(),
                TextureKey.DOWN, TextureKey.UP, TextureKey.NORTH, TextureKey.SOUTH, TextureKey.EAST, TextureKey.WEST
            );
            symmetricalModelId = tintedModel.upload(createNestedModelId(block, "symmetrical/base_v1"), textureMap, generator.modelCollector);
            asymmetricalModelId = tintedModel.upload(createNestedModelId(block, "asymmetrical/base_v1"), textureMap, generator.modelCollector);
        } else {
            symmetricalModelId = Models.CUBE.upload(createNestedModelId(block, "symmetrical/base_v1"), textureMap, generator.modelCollector);
            asymmetricalModelId = Models.CUBE.upload(createNestedModelId(block, "asymmetrical/base_v1"), textureMap, generator.modelCollector);
        }

        generator.blockStateCollector.accept(createSymmetricalBlockState(block, symmetricalModelId, asymmetricalModelId));
        registerParentedItemModel(generator, block, symmetricalModelId);
    }

    /**
     * Registers a symmetrical solid block with random textures.
     */
    private static void registerSymmetricalSolidBlockWithRandomTextures(BlockStateModelGenerator generator, Block block, String[][] textureArrays) {
        registerSymmetricalSolidBlockWithRandomTextures(generator, block, textureArrays, false);
    }

    /**
     * Registers a symmetrical solid block with random textures and tinted option.
     */
    private static void registerSymmetricalSolidBlockWithRandomTextures(BlockStateModelGenerator generator, Block block, String[][] textureArrays, boolean isTinted) {
        List<Identifier> symmetricalModelIds = new ArrayList<>();
        List<Identifier> asymmetricalModelIds = new ArrayList<>();

        for (int i = 0; i < textureArrays.length; i++) {
            String[] filledTextures = fillTextureArray(textureArrays[i]);
            TextureMap textureMap = ModTextureMap.customAllSides(filledTextures);
            
            symmetricalModelIds.add(Models.CUBE.upload(createNestedModelId(block, "symmetrical/base_v" + (i + 1)), textureMap, generator.modelCollector));
            asymmetricalModelIds.add(Models.CUBE.upload(createNestedModelId(block, "asymmetrical/base_v" + (i + 1)), textureMap, generator.modelCollector));
        }

        generator.blockStateCollector.accept(createSymmetricalBlockStateWithVariants(block, symmetricalModelIds, asymmetricalModelIds));
        if (!symmetricalModelIds.isEmpty()) {
            registerParentedItemModel(generator, block, symmetricalModelIds.get(0));
        }
    }

    /**
     * Registers a symmetrical solid block with states.
     */
    private static void registerSymmetricalSolidBlockWithStates(BlockStateModelGenerator generator, Block block, String[][] textureArrays) {
        List<Identifier> symmetricalModelIds = new ArrayList<>();
        List<Identifier> asymmetricalModelIds = new ArrayList<>();

        for (int i = 0; i < textureArrays.length; i++) {
            String[] filledTextures = fillTextureArray(textureArrays[i]);
            TextureMap textureMap = ModTextureMap.customAllSides(filledTextures);
            
            symmetricalModelIds.add(Models.CUBE.upload(createNestedModelId(block, "symmetrical/state" + i + "_v" + (i + 1)), textureMap, generator.modelCollector));
            asymmetricalModelIds.add(Models.CUBE.upload(createNestedModelId(block, "asymmetrical/state" + i + "_v" + (i + 1)), textureMap, generator.modelCollector));
        }

        generator.blockStateCollector.accept(createSymmetricalBlockStateWithStates(block, symmetricalModelIds, asymmetricalModelIds));
        if (!symmetricalModelIds.isEmpty()) {
            registerParentedItemModel(generator, block, symmetricalModelIds.get(0));
        }
    }

    // Utility methods for creating symmetrical blockstates

    /**
     * Creates a simple symmetrical blockstate with two variants.
     */
    private static BlockStateSupplier createSymmetricalBlockState(Block block, Identifier symmetricalModelId, Identifier asymmetricalModelId) {
        return new BlockStateSupplier() {
            @Override
            public Block getBlock() {
                return block;
            }

            @Override
            public JsonElement get() {
                JsonObject json = new JsonObject();
                JsonObject variants = new JsonObject();

                JsonObject symmetricalVariant = new JsonObject();
                symmetricalVariant.addProperty("model", symmetricalModelId.toString());
                variants.add("symmetrical=true", symmetricalVariant);

                JsonObject asymmetricalVariant = new JsonObject();
                asymmetricalVariant.addProperty("model", asymmetricalModelId.toString());
                variants.add("symmetrical=false", asymmetricalVariant);

                json.add("variants", variants);
                return json;
            }
        };
    }

    /**
     * Creates a symmetrical blockstate with multiple variants.
     */
    private static BlockStateSupplier createSymmetricalBlockStateWithVariants(Block block, List<Identifier> symmetricalModelIds, List<Identifier> asymmetricalModelIds) {
        return new BlockStateSupplier() {
            @Override
            public Block getBlock() {
                return block;
            }

            @Override
            public JsonElement get() {
                JsonObject json = new JsonObject();
                JsonObject variants = new JsonObject();

                // Add symmetrical variants
                for (Identifier modelId : symmetricalModelIds) {
                    JsonObject variant = new JsonObject();
                    variant.addProperty("model", modelId.toString());
                    variants.add("symmetrical=true", variant);
                }

                // Add asymmetrical variants
                for (Identifier modelId : asymmetricalModelIds) {
                    JsonObject variant = new JsonObject();
                    variant.addProperty("model", modelId.toString());
                    variants.add("symmetrical=false", variant);
                }

                json.add("variants", variants);
                return json;
            }
        };
    }

    /**
     * Creates a symmetrical blockstate with states.
     */
    private static BlockStateSupplier createSymmetricalBlockStateWithStates(Block block, List<Identifier> symmetricalModelIds, List<Identifier> asymmetricalModelIds) {
        return new BlockStateSupplier() {
            @Override
            public Block getBlock() {
                return block;
            }

            @Override
            public JsonElement get() {
                JsonObject json = new JsonObject();
                JsonObject variants = new JsonObject();

                // Add symmetrical variants with states
                for (int i = 0; i < symmetricalModelIds.size(); i++) {
                    JsonObject variant = new JsonObject();
                    variant.addProperty("model", symmetricalModelIds.get(i).toString());
                    variants.add("state=state" + i + ",symmetrical=true", variant);
                }

                // Add asymmetrical variants with states
                for (int i = 0; i < asymmetricalModelIds.size(); i++) {
                    JsonObject variant = new JsonObject();
                    variant.addProperty("model", asymmetricalModelIds.get(i).toString());
                    variants.add("state=state" + i + ",symmetrical=false", variant);
                }

                json.add("variants", variants);
                return json;
            }
        };
    }
}
