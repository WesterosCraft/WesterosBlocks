package com.westerosblocks.datagen.custom;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.custom.WCCuboidNSEWUDBlock;
import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.westerosblocks.datagen.ModTextureKey;

import java.util.Optional;

/**
 * Exporter for NSEWD (6-direction) cuboid blocks with directional facing support.
 * Generates blockstate files with facing variants for all 6 directions including UP and DOWN.
 */
public class CuboidNSEWUDBlockExporter extends BaseBlockExporter {

    public static void registerCustomCuboidNSEWUDBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        if (!(block instanceof WCCuboidNSEWUDBlock)) {
            throw new IllegalArgumentException("Block must be a WCCuboidNSEWUDBlock instance");
        }

        // Use centralized priority logic from BlockDefinition
        BlockDefinition.TextureSource source = definition.getPrimaryTextureSource();

        switch (source) {
            case STATES -> registerCuboidNSEWUDBlockWithStates(generator, block, definition);
            case RANDOM_TEXTURES -> registerCuboidNSEWUDBlockWithRandomTextures(generator, block, definition);
            case CUSTOM_MODEL -> registerCustomModelCuboidNSEWUDBlock(generator, block, definition);
            case TEXTURES, NONE -> registerSimpleCuboidNSEWUDBlock(generator, block, definition);
        }
    }

    /**
     * Registers a simple cuboid block with generated model from cuboids or textures.
     */
    private static void registerSimpleCuboidNSEWUDBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        List<String> textures = definition.getTextures();

        Identifier modelId;
        if (definition.hasCustomModel()) {
            // Reference pre-existing custom model
            modelId = createCustomModelId(block, "base_v1");
        } else {
            // Always generate custom cuboid model for NSEWUD blocks to get proper element format
            modelId = createCuboidNSEWUDModel(generator, block, definition, textures, 0, "base_v1");
        }

        // Generate blockstate with rotations for all 6 directions
        generator.blockStateCollector.accept(createCuboidNSEWUDBlockState(block, modelId));

        // Register item model
        registerParentedItemModel(generator, block, modelId);
    }


    /**
     * Registers a cuboid block with random texture variants.
     */
    private static void registerCuboidNSEWUDBlockWithRandomTextures(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        List<BlockDefinition.RandomTextureVariant> randomTextures = definition.getRandomTextures();
        List<ModelVariant> modelVariants = new ArrayList<>();

        for (int i = 0; i < randomTextures.size(); i++) {
            BlockDefinition.RandomTextureVariant variant = randomTextures.get(i);
            List<String> textures = variant.getTextures();

            Identifier modelId;
            if (definition.hasCustomModel()) {
                // Reference pre-existing custom model
                modelId = createCustomModelId(block, "base_v" + (i + 1));
            } else {
                // Always generate custom cuboid model for NSEWUD blocks to get proper element format
                modelId = createCuboidNSEWUDModel(generator, block, definition, textures, i, "base_v" + (i + 1));
            }

            modelVariants.add(new ModelVariant(modelId, variant.getWeight()));
        }

        // Generate blockstate with weighted random variants
        generator.blockStateCollector.accept(createCuboidNSEWUDBlockStateWithRandomTextures(block, modelVariants));

        // Register item model
        registerParentedItemModel(generator, block, modelVariants.get(0).model);
    }

    /**
     * Registers a cuboid block with multiple states.
     */
    private static void registerCuboidNSEWUDBlockWithStates(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        List<BlockDefinition.StateVariant> states = definition.getStates();
        Map<String, List<ModelVariant>> stateModelMap = new HashMap<>();

        Identifier firstModel = null;

        for (BlockDefinition.StateVariant state : states) {
            String stateId = state.getStateID() != null ? state.getStateID() : "base";
            List<ModelVariant> modelVariants = new ArrayList<>();

            if (state.hasRandomTextures()) {
                // Handle state with random textures
                List<BlockDefinition.RandomTextureVariant> randomTextures = state.getRandomTextures();
                for (int i = 0; i < randomTextures.size(); i++) {
                    List<String> textures = randomTextures.get(i).getTextures();

                    Identifier modelId;
                    if (definition.hasCustomModel()) {
                        // Reference pre-existing custom model
                        modelId = createCustomModelId(block, stateId + "_v" + (i + 1));
                    } else {
                        // Always generate custom cuboid model for NSEWUD blocks to get proper element format
                        modelId = createCuboidNSEWUDModel(generator, block, definition, textures, i, stateId + "_v" + (i + 1));
                    }

                    modelVariants.add(new ModelVariant(modelId, randomTextures.get(i).getWeight()));
                    if (firstModel == null) firstModel = modelId;
                }
            } else {
                // Handle state with single texture set
                List<String> textures = state.getTextures() != null ? state.getTextures() : definition.getTextures();

                Identifier modelId;
                if (definition.hasCustomModel()) {
                    // Reference pre-existing custom model
                    modelId = createCustomModelId(block, stateId + "_v1");
                } else {
                    // Always generate custom cuboid model for NSEWUD blocks to get proper element format
                    modelId = createCuboidNSEWUDModel(generator, block, definition, textures, 0, stateId + "_v1");
                }

                modelVariants.add(new ModelVariant(modelId, 1));
                if (firstModel == null) firstModel = modelId;
            }

            stateModelMap.put(stateId, modelVariants);
        }

        // Generate blockstate with states
        generator.blockStateCollector.accept(createCuboidNSEWUDBlockStateWithStates(block, definition, stateModelMap, states));

        // Register item model
        if (firstModel != null) {
            registerParentedItemModel(generator, block, firstModel);
        }
    }

    /**
     * Registers a cuboid block using pre-existing custom model.
     */
    private static void registerCustomModelCuboidNSEWUDBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        Identifier modelId = createCustomModelId(block, "base_v1");

        generator.blockStateCollector.accept(createCuboidNSEWUDBlockState(block, modelId));
        registerParentedItemModel(generator, block, modelId);
    }

    /**
     * Creates a blockstate for cuboid blocks with all 6 facing directions.
     * Uses a single model with X and Y rotations.
     */
    private static BlockStateSupplier createCuboidNSEWUDBlockState(Block block, Identifier modelId) {
        return new BlockStateSupplier() {
            @Override
            public Block getBlock() {
                return block;
            }

            @Override
            public JsonElement get() {
                JsonObject json = new JsonObject();
                JsonObject variants = new JsonObject();

                // Horizontal facings (Y rotation only)
                addVariant(variants, "facing=north", modelId, 0, 0);
                addVariant(variants, "facing=east", modelId, 0, 90);
                addVariant(variants, "facing=south", modelId, 0, 180);
                addVariant(variants, "facing=west", modelId, 0, 270);

                // Vertical facings (X rotation for pitch, Y for yaw)
                addVariant(variants, "facing=up", modelId, 270, 0);
                addVariant(variants, "facing=down", modelId, 90, 0);

                json.add("variants", variants);
                return json;
            }
        };
    }

    /**
     * Creates a blockstate for cuboid blocks with random textures.
     */
    private static BlockStateSupplier createCuboidNSEWUDBlockStateWithRandomTextures(Block block, List<ModelVariant> modelVariants) {
        return new BlockStateSupplier() {
            @Override
            public Block getBlock() {
                return block;
            }

            @Override
            public JsonElement get() {
                JsonObject json = new JsonObject();
                JsonObject variants = new JsonObject();

                // Horizontal facings with random textures
                addVariantWithWeights(variants, "facing=north", modelVariants, 0, 0);
                addVariantWithWeights(variants, "facing=east", modelVariants, 0, 90);
                addVariantWithWeights(variants, "facing=south", modelVariants, 0, 180);
                addVariantWithWeights(variants, "facing=west", modelVariants, 0, 270);

                // Vertical facings with random textures
                addVariantWithWeights(variants, "facing=up", modelVariants, 270, 0);
                addVariantWithWeights(variants, "facing=down", modelVariants, 90, 0);

                json.add("variants", variants);
                return json;
            }
        };
    }

    /**
     * Creates a blockstate for cuboid blocks with states.
     */
    private static BlockStateSupplier createCuboidNSEWUDBlockStateWithStates(Block block, BlockDefinition definition,
                                                                            Map<String, List<ModelVariant>> stateModelMap,
                                                                            List<BlockDefinition.StateVariant> states) {
        return new BlockStateSupplier() {
            @Override
            public Block getBlock() {
                return block;
            }

            @Override
            public JsonElement get() {
                JsonObject json = new JsonObject();
                JsonObject variants = new JsonObject();

                for (BlockDefinition.StateVariant state : states) {
                    String stateId = state.getStateID() != null ? state.getStateID() : "base";
                    List<ModelVariant> modelVariants = stateModelMap.get(stateId);

                    if (modelVariants != null && !modelVariants.isEmpty()) {
                        // Horizontal facings
                        addVariantWithWeightsAndState(variants, "facing=north", modelVariants, 0, 0, stateId);
                        addVariantWithWeightsAndState(variants, "facing=east", modelVariants, 0, 90, stateId);
                        addVariantWithWeightsAndState(variants, "facing=south", modelVariants, 0, 180, stateId);
                        addVariantWithWeightsAndState(variants, "facing=west", modelVariants, 0, 270, stateId);

                        // Vertical facings
                        addVariantWithWeightsAndState(variants, "facing=up", modelVariants, 270, 0, stateId);
                        addVariantWithWeightsAndState(variants, "facing=down", modelVariants, 90, 0, stateId);
                    }
                }

                json.add("variants", variants);
                return json;
            }
        };
    }

    // Helper method to add variant with rotation
    private static void addVariant(JsonObject variants, String condition, Identifier model, int x, int y) {
        JsonObject variant = new JsonObject();
        variant.addProperty("model", model.toString());
        if (x != 0) variant.addProperty("x", x);
        if (y != 0) variant.addProperty("y", y);
        variants.add(condition, variant);
    }

    // Helper method to add variant with weighted models
    private static void addVariantWithWeights(JsonObject variants, String condition, List<ModelVariant> modelVariants, int x, int y) {
        if (modelVariants.size() == 1) {
            addVariant(variants, condition, modelVariants.get(0).model, x, y);
        } else {
            JsonArray variantArray = new JsonArray();
            for (ModelVariant mv : modelVariants) {
                JsonObject variant = new JsonObject();
                variant.addProperty("model", mv.model.toString());
                if (x != 0) variant.addProperty("x", x);
                if (y != 0) variant.addProperty("y", y);
                if (mv.weight != 1) variant.addProperty("weight", mv.weight);
                variantArray.add(variant);
            }
            variants.add(condition, variantArray);
        }
    }

    // Helper method to add variant with weights and state
    private static void addVariantWithWeightsAndState(JsonObject variants, String condition, List<ModelVariant> modelVariants, int x, int y, String stateId) {
        String fullCondition = "state=" + stateId + "," + condition;
        addVariantWithWeights(variants, fullCondition, modelVariants, x, y);
    }

    private static Identifier createCustomModelId(Block block, String variant) {
        String blockName = getBlockName(block);
        return WesterosBlocks.id("block/custom/" + blockName + "/" + variant);
    }

    private static Identifier createGeneratedModelId(Block block, String variant) {
        String blockName = getBlockName(block);
        return WesterosBlocks.id("block/" + blockName + "/" + variant);
    }

    /**
     * Checks if the definition has cuboids array for model generation.
     */
    private static boolean hasCuboids(BlockDefinition definition) {
        return definition.getCuboids() != null && !definition.getCuboids().isEmpty();
    }

    /**
     * Creates a texture map for cuboid blocks from a list of textures.
     * Follows standard Minecraft cube texture ordering: down, up, north, south, west, east
     */
    private static TextureMap createCuboidTextureMap(List<String> textures) {
        if (textures == null || textures.isEmpty()) {
            return TextureMap.all(createBlockIdentifier("missing"));
        }

        TextureMap textureMap = new TextureMap();
        int textureCount = textures.size();

        // Standard Minecraft cube face ordering
        TextureKey[] faceKeys = {
            TextureKey.DOWN,   // 0: bottom
            TextureKey.UP,     // 1: top
            TextureKey.NORTH,  // 2: north
            TextureKey.SOUTH,  // 3: south
            TextureKey.WEST,   // 4: west
            TextureKey.EAST    // 5: east
        };

        for (int i = 0; i < 6; i++) {
            String texture = i < textureCount ? textures.get(i) : textures.get(textureCount - 1);
            textureMap.put(faceKeys[i], createBlockIdentifier(texture));
        }

        // Particle texture defaults to first texture
        textureMap.put(TextureKey.PARTICLE, createBlockIdentifier(textures.get(0)));
        return textureMap;
    }

    /**
     * Creates a custom cuboid model from definition for NSEWUD blocks.
     * Transforms coordinates from X-axis orientation to Z-axis orientation.
     */
    static Identifier createCuboidNSEWUDModel(BlockStateModelGenerator generator, Block block, BlockDefinition definition, List<String> textures, int stateIndex, String variant) {
        TextureMap textureMap = createCustomCuboidTextureMap(textures);
        Identifier modelId = createGeneratedModelId(block, variant);

        Model cuboidModel = createCuboidNSEWUDModelFromDefinition(definition, textures, stateIndex);
        cuboidModel.upload(modelId, textureMap, generator.modelCollector);

        return modelId;
    }

    /**
     * Creates a Model instance from BlockDefinition cuboids for NSEWUD blocks.
     * Transforms X-axis oriented cuboids to Z-axis oriented cuboids.
     */
    private static Model createCuboidNSEWUDModelFromDefinition(BlockDefinition definition, List<String> textures, int stateIndex) {
        int requiredTextures = Math.max(6, textures.size());

        List<TextureKey> textureKeys = new ArrayList<>();
        textureKeys.add(TextureKey.PARTICLE);

        for (int i = 0; i < requiredTextures; i++) {
            textureKeys.add(ModTextureKey.getTextureNKey(i));
        }

        return new Model(Optional.empty(), Optional.empty(), textureKeys.toArray(new TextureKey[0])) {
            @Override
            public JsonObject createJson(Identifier id, Map<TextureKey, Identifier> textures) {
                JsonObject json = super.createJson(id, textures);
                json.addProperty("parent", "block/block");

                // Add elements array
                JsonArray elements = new JsonArray();
                List<BlockDefinition.CuboidElement> cuboids = definition.getCuboids();

                if (cuboids != null && !cuboids.isEmpty()) {
                    for (BlockDefinition.CuboidElement cuboid : cuboids) {
                        JsonObject element = new JsonObject();
                        addCuboidNSEWUDElement(element, cuboid, definition.isTinted());
                        elements.add(element);
                    }
                }

                json.add("elements", elements);
                return json;
            }
        };
    }

    /**
     * Adds a cuboid element to the model for NSEWUD blocks.
     * Transforms coordinates: newX = oldZ, newY = oldY, newZ = 16-oldX (reversed)
     */
    private static void addCuboidNSEWUDElement(JsonObject element, BlockDefinition.CuboidElement cuboid, boolean isTinted) {
        // Transform coordinates
        JsonArray from = new JsonArray();
        from.add(getClamped(cuboid.getZMin()));        // new X = old Z
        from.add(getClamped(cuboid.getYMin()));        // new Y = old Y
        from.add(16 - getClamped(cuboid.getXMax()));   // new Z from = 16 - old X max
        element.add("from", from);

        JsonArray to = new JsonArray();
        to.add(getClamped(cuboid.getZMax()));          // new X = old Z
        to.add(getClamped(cuboid.getYMax()));          // new Y = old Y
        to.add(16 - getClamped(cuboid.getXMin()));     // new Z to = 16 - old X min
        element.add("to", to);

        // Add faces
        JsonObject faces = new JsonObject();
        addCuboidNSEWUDFaces(faces, cuboid, isTinted);
        element.add("faces", faces);
    }

    /**
     * Adds faces to a cuboid element for NSEWUD blocks.
     */
    private static void addCuboidNSEWUDFaces(JsonObject faces, BlockDefinition.CuboidElement cuboid, boolean isTinted) {
        int[] sidetxt = cuboid.getSideTextures() != null ? cuboid.getSideTextures() : new int[]{0, 1, 2, 3, 4, 5};
        boolean[] noTint = cuboid.getNoTint() != null ? cuboid.getNoTint() : new boolean[]{false, false, false, false, false, false};
        int[] siderot = cuboid.getSideRotations() != null ? cuboid.getSideRotations() : new int[]{0, 0, 0, 0, 0, 0};

        // Add each face with transformed coordinates
        // Original order: down(0), up(1), north(2), south(3), west(4), east(5)
        // After transform: down(0), up(1), west(4), east(5), north(2), south(3)
        addFaceNSEWUD(faces, "down", 0, cuboid, sidetxt, noTint, siderot, isTinted);
        addFaceNSEWUD(faces, "up", 1, cuboid, sidetxt, noTint, siderot, isTinted);
        addFaceNSEWUD(faces, "north", 2, cuboid, sidetxt, noTint, siderot, isTinted);
        addFaceNSEWUD(faces, "south", 3, cuboid, sidetxt, noTint, siderot, isTinted);
        addFaceNSEWUD(faces, "west", 4, cuboid, sidetxt, noTint, siderot, isTinted);
        addFaceNSEWUD(faces, "east", 5, cuboid, sidetxt, noTint, siderot, isTinted);
    }

    /**
     * Adds a single face to the faces object for NSEWUD blocks.
     */
    private static void addFaceNSEWUD(JsonObject faces, String face, int index,
                         BlockDefinition.CuboidElement cuboid, int[] sidetxt,
                         boolean[] noTint, int[] siderot, boolean isTinted) {
        JsonObject faceObj = new JsonObject();

        // Set UV coordinates based on face
        JsonArray uv = new JsonArray();
        calculateNSEWUDUVs(face, cuboid, uv);
        faceObj.add("uv", uv);

        // Get correct texture key
        faceObj.addProperty("texture", "#txt" + sidetxt[index]);

        // Add rotation for down/up faces or from siderot array
        if (face.equals("down") && siderot[index] == 0) {
            faceObj.addProperty("rotation", 90);
        } else if (face.equals("up") && siderot[index] == 0) {
            faceObj.addProperty("rotation", 270);
        } else if (siderot[index] != 0) {
            faceObj.addProperty("rotation", siderot[index]);
        }

        // Add tint if needed
        if (isTinted && !noTint[index]) {
            faceObj.addProperty("tintindex", 0);
        }

        // Add cullface if needed
        String cullface = getCullfaceNSEWUD(face, cuboid);
        if (cullface != null) {
            faceObj.addProperty("cullface", cullface);
        }

        faces.add(face, faceObj);
    }

    /**
     * Calculates UV coordinates for a face for NSEWUD blocks.
     * Accounts for coordinate transformation: newX=oldZ, newY=oldY, newZ=16-oldX.
     */
    private static void calculateNSEWUDUVs(String face, BlockDefinition.CuboidElement cuboid, JsonArray uv) {
        switch (face) {
            case "down" -> {
                // Down face: newX (=oldZ) for U, oldX for V (since newZ=16-oldX, we use oldX directly)
                uv.add(getClamped(cuboid.getZMin()));
                uv.add(getClamped(cuboid.getXMin()));
                uv.add(getClamped(cuboid.getZMax()));
                uv.add(getClamped(cuboid.getXMax()));
            }
            case "up" -> {
                // Up face: newX (=oldZ) for U, 16-oldX for V (=newZ reversed)
                uv.add(getClamped(cuboid.getZMin()));
                uv.add(16 - getClamped(cuboid.getXMax()));
                uv.add(getClamped(cuboid.getZMax()));
                uv.add(16 - getClamped(cuboid.getXMin()));
            }
            case "north" -> {
                // North face: newX (=oldZ) for U, Y for V
                uv.add(getClamped(cuboid.getZMin()));
                uv.add(16 - getClamped(cuboid.getYMax()));
                uv.add(getClamped(cuboid.getZMax()));
                uv.add(16 - getClamped(cuboid.getYMin()));
            }
            case "south" -> {
                // South face: newX (=oldZ) for U, Y for V
                uv.add(getClamped(cuboid.getZMin()));
                uv.add(16 - getClamped(cuboid.getYMax()));
                uv.add(getClamped(cuboid.getZMax()));
                uv.add(16 - getClamped(cuboid.getYMin()));
            }
            case "west" -> {
                // West face: 16-oldX (=newZ reversed) for U, Y for V
                uv.add(16 - getClamped(cuboid.getXMax()));
                uv.add(16 - getClamped(cuboid.getYMax()));
                uv.add(16 - getClamped(cuboid.getXMin()));
                uv.add(16 - getClamped(cuboid.getYMin()));
            }
            case "east" -> {
                // East face: oldX for U (since newZ=16-oldX), Y for V
                uv.add(getClamped(cuboid.getXMin()));
                uv.add(16 - getClamped(cuboid.getYMax()));
                uv.add(getClamped(cuboid.getXMax()));
                uv.add(16 - getClamped(cuboid.getYMin()));
            }
        }
    }

    /**
     * Gets the cullface for a face for NSEWUD blocks.
     * Accounts for coordinate transformation: newX=oldZ, newY=oldY, newZ=16-oldX.
     */
    private static String getCullfaceNSEWUD(String face, BlockDefinition.CuboidElement cuboid) {
        return switch (face) {
            case "down" -> cuboid.getYMin() <= 0 ? "down" : null;
            case "up" -> cuboid.getYMax() >= 1 ? "up" : null;
            case "north" -> (16 - getClamped(cuboid.getXMax())) <= 0 ? "north" : null;  // newZ min = 16-oldX max
            case "south" -> (16 - getClamped(cuboid.getXMin())) >= 16 ? "south" : null;  // newZ max = 16-oldX min
            case "west" -> cuboid.getZMin() <= 0 ? "west" : null;    // newX min = oldZ min
            case "east" -> cuboid.getZMax() >= 1 ? "east" : null;    // newX max = oldZ max
            default -> null;
        };
    }

    /**
     * Clamps a coordinate value to valid range.
     */
    private static float getClamped(double v) {
        float f = (float) (16.0 * v);
        if (f < -16f) f = -16f;
        if (f > 32f) f = 32f;
        return f;
    }

    /**
     * Creates a texture map for custom cuboid models using ModTextureKey keys.
     */
    private static TextureMap createCustomCuboidTextureMap(List<String> textures) {
        TextureMap textureMap = new TextureMap();

        if (textures == null || textures.isEmpty()) {
            String fallbackTexture = "missing";
            for (int i = 0; i < 6; i++) {
                textureMap.put(ModTextureKey.getTextureNKey(i), createBlockIdentifier(fallbackTexture));
            }
            textureMap.put(TextureKey.PARTICLE, createBlockIdentifier(fallbackTexture));
            return textureMap;
        }

        int textureCount = textures.size();
        int requiredTextures = Math.max(6, textureCount);

        for (int i = 0; i < requiredTextures; i++) {
            String texture = i < textureCount ? textures.get(i) : textures.get(textureCount - 1);
            textureMap.put(ModTextureKey.getTextureNKey(i), createBlockIdentifier(texture));
        }

        textureMap.put(TextureKey.PARTICLE, createBlockIdentifier(textures.get(0)));
        return textureMap;
    }

    /**
     * Helper class to hold a model with its weight.
     */
    private static class ModelVariant {
        final Identifier model;
        final int weight;

        ModelVariant(Identifier model, int weight) {
            this.model = model;
            this.weight = weight;
        }
    }
}
