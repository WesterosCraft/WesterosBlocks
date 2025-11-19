package com.westerosblocks.datagen.custom;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;
import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.datagen.ModTextureKey;
import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.utils.ModProperties;

import java.util.*;

public class CuboidBlockExporter extends BaseBlockExporter {
    private static final int[] STANDARD_TEXTURE_INDICES = {0, 1, 2, 3, 4, 5};
    private static final boolean[] NO_TINT_ALL = {false, false, false, false, false, false};

    public static void registerCustomCuboidBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        boolean isTinted = definition.isTinted();
        int rotationCount = definition.hasRotateRandom() ? 4 : 1;
        List<BlockDefinition.StateVariant> states = definition.getStates();
        boolean hasMultipleStates = definition.getStateCount() > 1;

        if (states == null || states.isEmpty()) {
            throw new IllegalStateException("Block definition states should never be null/empty after doInit() for block: " + getBlockName(block));
        }

        generateBlockState(generator, block, definition, states, hasMultipleStates, rotationCount);

        Identifier firstModel = generateModels(generator, block, definition, states, hasMultipleStates, isTinted, rotationCount);

        if (firstModel != null) {
            registerParentedItemModel(generator, block, firstModel);
        }
    }

    /**
     * Phase 2: Generate blockstate JSON
     * Matches CrossBlockExporter pattern with state tracking and rotation support
     */
    private static void generateBlockState(BlockStateModelGenerator generator, Block block,
                                          BlockDefinition definition,
                                          List<BlockDefinition.StateVariant> states,
                                          boolean hasMultipleStates, int rotationCount) {
        // Collect all model identifiers for all states
        Map<String, List<Identifier>> stateModelIds = new HashMap<>();

        for (BlockDefinition.StateVariant state : states) {
            String stateId = state.getStateID();
            if (stateId == null) stateId = "base";

            List<Identifier> modelIds = new ArrayList<>();

            // Check if we have texture sets to work with
            int textureSetCount = state.getRandomTextureSetCount();

            // For custom model states with no textures, ensure at least one iteration
            if (textureSetCount == 0) {
                if (state.isCustomModel()) {
                    textureSetCount = 1;  // Force one iteration for custom model reference
                } else {
                    continue;  // Skip non-custom-model states with no textures
                }
            }

            // Build model IDs for all texture sets
            for (int setIdx = 0; setIdx < textureSetCount; setIdx++) {
                String statePrefix = hasMultipleStates ? stateId : "base";
                String variantName = getModelName(statePrefix, setIdx);
                Identifier modelId = state.isCustomModel()
                    ? createCustomModelId(block, variantName)
                    : createNestedModelId(block, variantName);

                modelIds.add(modelId);
            }

            if (!modelIds.isEmpty()) {
                stateModelIds.put(stateId, modelIds);
            }
        }

        if (stateModelIds.isEmpty()) {
            // Fallback: Create single missing texture model
            String variantName = getModelName("base", 0);
            Identifier modelId = createNestedModelId(block, variantName);
            stateModelIds.put("base", List.of(modelId));
        }

        // Generate blockstate variants based on configuration
        if (hasMultipleStates && hasStateProperty(block)) {
            // Multi-state block with STATE property
            ModProperties.StateProperty stateProperty = getStateProperty(block);
            BlockStateVariantMap.SingleProperty<String> stateMap =
                BlockStateVariantMap.create(stateProperty);

            for (Map.Entry<String, List<Identifier>> entry : stateModelIds.entrySet()) {
                String stateId = entry.getKey();
                List<Identifier> modelIds = entry.getValue();
                List<BlockStateVariant> variants = new ArrayList<>();

                for (int i = 0; i < modelIds.size(); i++) {
                    Identifier modelId = modelIds.get(i);

                    // Get weight from corresponding random texture set
                    BlockDefinition.StateVariant state = findStateById(states, stateId);
                    int weight = 1;
                    if (state != null && i < state.getRandomTextureSetCount()) {
                        BlockDefinition.RandomTextureVariant rtv = state.getRandomTextureSet(i);
                        if (rtv != null) {
                            weight = rtv.getWeight();
                        }
                    }

                    // Add rotation variants
                    for (int rot = 0; rot < rotationCount; rot++) {
                        BlockStateVariant variant = createWeightedVariant(modelId, rot * 90, weight);
                        variants.add(variant);
                    }
                }

                stateMap.register(stateId, variants);
            }

            generator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(block).coordinate(stateMap)
            );
        } else {
            // Single state block or block without STATE property
            List<Identifier> modelIds = stateModelIds.values().iterator().next();
            List<BlockStateVariant> variants = new ArrayList<>();

            BlockDefinition.StateVariant state = states.get(0);

            for (int i = 0; i < modelIds.size(); i++) {
                Identifier modelId = modelIds.get(i);

                // Get weight from corresponding random texture set
                int weight = 1;
                if (i < state.getRandomTextureSetCount()) {
                    BlockDefinition.RandomTextureVariant rtv = state.getRandomTextureSet(i);
                    if (rtv != null) {
                        weight = rtv.getWeight();
                    }
                }

                // Add rotation variants
                for (int rot = 0; rot < rotationCount; rot++) {
                    BlockStateVariant variant = createWeightedVariant(modelId, rot * 90, weight);
                    variants.add(variant);
                }
            }

            generator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(block, variants.toArray(new BlockStateVariant[0]))
            );
        }
    }

    /**
     * Phase 3: Generate model files
     * Matches CrossBlockExporter pattern with state-based iteration
     */
    private static Identifier generateModels(BlockStateModelGenerator generator, Block block,
                                            BlockDefinition definition,
                                            List<BlockDefinition.StateVariant> states,
                                            boolean hasMultipleStates, boolean isTinted, int rotationCount) {
        Identifier firstModel = null;

        for (BlockDefinition.StateVariant state : states) {
            if (state.isCustomModel()) {
                // Skip model generation for custom models - they already exist
                String stateId = state.getStateID() != null ? state.getStateID() : "base";
                String statePrefix = hasMultipleStates ? stateId : "base";

                // Still record first model for item
                if (firstModel == null) {
                    firstModel = createCustomModelId(block, getModelName(statePrefix, 0));
                }
                continue;
            }

            String stateId = state.getStateID();
            if (stateId == null) stateId = "base";
            String statePrefix = hasMultipleStates ? stateId : "base";

            for (int setIdx = 0; setIdx < state.getRandomTextureSetCount(); setIdx++) {
                BlockDefinition.RandomTextureVariant set = state.getRandomTextureSet(setIdx);
                if (set == null || set.getTextureCount() == 0) {
                    continue;
                }

                // Extract textures from this set
                String[] textures = new String[set.getTextureCount()];
                for (int i = 0; i < set.getTextureCount(); i++) {
                    textures[i] = set.getTextureByIndex(i);
                }
                List<String> textureList = Arrays.asList(textures);

                // Generate model based on block configuration
                String variantName = getModelName(statePrefix, setIdx);
                Identifier modelId;

                if (hasCuboids(definition) || state.hasCuboids()) {

                    // Handle per-state rotation offset
                    Float rotation = null;
                    if (state.getRotYOffset() != null) {
                        rotation = state.getRotYOffset().floatValue();
                    }

                    modelId = createCuboidModel(generator, block, definition,
                                               textureList, setIdx, variantName, rotation);
                } else {
                    // Generate standard cube models
                    TextureMap textureMap = createCuboidTextureMap(textureList);
                    if (textureList.size() == 1) {
                        modelId = Models.CUBE_ALL.upload(createGeneratedModelId(block, variantName),
                                                         textureMap, generator.modelCollector);
                    } else {
                        modelId = Models.CUBE.upload(createGeneratedModelId(block, variantName),
                                                    textureMap, generator.modelCollector);
                    }
                }

                if (firstModel == null) {
                    firstModel = modelId;
                }
            }
        }

        return firstModel;
    }

    /**
     * Helper method to get model name from state ID and set index.
     */
    private static String getModelName(String stateId, int setIdx) {
        return stateId + "_v" + (setIdx + 1);
    }

    /**
     * Helper method to find state by ID.
     */
    private static BlockDefinition.StateVariant findStateById(List<BlockDefinition.StateVariant> states, String stateId) {
        for (BlockDefinition.StateVariant state : states) {
            if (stateId.equals(state.getStateID())) {
                return state;
            }
        }
        return null;
    }

    /**
     * Checks if block has STATE property.
     */
    private static boolean hasStateProperty(Block block) {
        for (var property : block.getStateManager().getProperties()) {
            if (property.getName().equals("state")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the STATE property from block.
     */
    private static ModProperties.StateProperty getStateProperty(Block block) {
        for (var property : block.getStateManager().getProperties()) {
            if (property instanceof ModProperties.StateProperty stateProperty) {
                return stateProperty;
            }
        }
        return null;
    }


    /**
     * Creates a custom cuboid model from definition.
     */
    static Identifier createCuboidModel(BlockStateModelGenerator generator, Block block, BlockDefinition definition, List<String> textures, int stateIndex, String variant) {
        return createCuboidModel(generator, block, definition, textures, stateIndex, variant, null);
    }

    /**
     * Creates a custom cuboid model from definition with optional rotation.
     * @param rotation Y-axis rotation angle in degrees (e.g., -22.5, -45, 22.5) or null for no rotation
     */
    static Identifier createCuboidModel(BlockStateModelGenerator generator, Block block, BlockDefinition definition, List<String> textures, int stateIndex, String variant, Float rotation) {
        TextureMap textureMap = createCustomCuboidTextureMap(textures);
        Identifier modelId = createGeneratedModelId(block, variant);

        Model cuboidModel = createCuboidModelFromDefinition(definition, textures, stateIndex, rotation);
        cuboidModel.upload(modelId, textureMap, generator.modelCollector);

        return modelId;
    }

    /**
     * Creates a model identifier for generated cuboid models with the correct path prefix.
     * Alias for createGeneratedModelId for compatibility with CrossBlockExporter pattern.
     */
    public static Identifier createNestedModelId(Block block, String variant) {
        return createGeneratedModelId(block, variant);
    }

    /**
     * Creates a model identifier for generated cuboid models with the correct path prefix.
     */
    public static Identifier createGeneratedModelId(Block block, String variant) {
        String blockName = getBlockName(block);
        String modelPath = "block/" + blockName + "/" + variant;
        return WesterosBlocks.id(modelPath);
    }

    /**
     * Creates a model identifier for custom cuboid models with the custom path prefix.
     */
    private static Identifier createCustomModelId(Block block, String variant) {
        String blockName = getBlockName(block);
        String modelPath = "block/custom/" + blockName + "/" + variant;
        return WesterosBlocks.id(modelPath);
    }

    /**
     * Creates a Model instance from BlockDefinition cuboids.
     */
    private static Model createCuboidModelFromDefinition(BlockDefinition definition, List<String> textures, int stateIndex, Float rotation) {
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

                // Display properties would be added here if supported in BlockDefinition

                // Add elements array
                JsonArray elements = new JsonArray();
                List<BlockDefinition.CuboidElement> cuboids = definition.getCuboids();

                if (cuboids != null && !cuboids.isEmpty()) {
                    for (BlockDefinition.CuboidElement cuboid : cuboids) {
                        if ("crossed".equals(cuboid.getShape())) {
                            // Handle crossed shape (like plants)
                            json.addProperty("ambientocclusion", false);

                            // First diagonal
                            JsonObject element1 = createCrossedElement(cuboid, true, definition.isTinted());
                            if (rotation != null) {
                                addRotation(element1, rotation);
                            }
                            elements.add(element1);

                            // Second diagonal
                            JsonObject element2 = createCrossedElement(cuboid, false, definition.isTinted());
                            if (rotation != null) {
                                addRotation(element2, rotation);
                            }
                            elements.add(element2);
                        } else {
                            JsonObject element = new JsonObject();
                            addCuboidElement(element, cuboid, definition.isTinted());
                            if (rotation != null) {
                                addRotation(element, rotation);
                            }
                            elements.add(element);
                        }
                    }
                } else if (definition.hasBoundingBox()) {
                    // Create a single cuboid element from the bounding box
                    BlockDefinition.BoundingBox bbox = definition.getBoundingBox();
                    JsonObject element = new JsonObject();
                    addBoundingBoxElement(element, bbox, definition.isTinted());
                    if (rotation != null) {
                        addRotation(element, rotation);
                    }
                    elements.add(element);
                }

                json.add("elements", elements);
                return json;
            }
        };
    }

    /**
     * Adds rotation property to a model element.
     */
    private static void addRotation(JsonObject element, float angle) {
        JsonObject rotation = new JsonObject();

        JsonArray origin = new JsonArray();
        origin.add(8.0);
        origin.add(8.0);
        origin.add(8.0);
        rotation.add("origin", origin);

        rotation.addProperty("axis", "y");
        rotation.addProperty("angle", angle);
        rotation.addProperty("rescale", false);

        element.add("rotation", rotation);
    }

    /**
     * Creates a crossed element for plant-like blocks.
     */
    private static JsonObject createCrossedElement(BlockDefinition.CuboidElement cuboid, boolean firstDiagonal, boolean isTinted) {
        JsonObject element = new JsonObject();

        // From coordinates
        JsonArray from = new JsonArray();
        if (firstDiagonal) {
            from.add(getClamped(cuboid.getXMin()));
            from.add(getClamped(cuboid.getYMin()));
            from.add(8.0);
        } else {
            from.add(8.0);
            from.add(getClamped(cuboid.getYMin()));
            from.add(getClamped(cuboid.getZMin()));
        }
        element.add("from", from);

        // To coordinates
        JsonArray to = new JsonArray();
        if (firstDiagonal) {
            to.add(getClamped(cuboid.getXMax()));
            to.add(getClamped(cuboid.getYMax()));
            to.add(8.0);
        } else {
            to.add(8.0);
            to.add(getClamped(cuboid.getYMax()));
            to.add(getClamped(cuboid.getZMax()));
        }
        element.add("to", to);

        // Set shade to false for crossed elements
        element.addProperty("shade", false);

        // Add faces
        JsonObject faces = new JsonObject();
        if (firstDiagonal) {
            addCrossedFace(faces, "north", isTinted);
            addCrossedFace(faces, "south", isTinted);
        } else {
            addCrossedFace(faces, "east", isTinted);
            addCrossedFace(faces, "west", isTinted);
        }
        element.add("faces", faces);

        return element;
    }

    /**
     * Adds a face for crossed elements.
     */
    private static void addCrossedFace(JsonObject faces, String direction, boolean isTinted) {
        JsonObject face = new JsonObject();
        JsonArray uv = new JsonArray();
        uv.add(0.0);
        uv.add(0.0);
        uv.add(16.0);
        uv.add(16.0);
        face.add("uv", uv);
        face.addProperty("texture", "#txt0");
        if (isTinted) {
            face.addProperty("tintindex", 0);
        }
        faces.add(direction, face);
    }

    /**
     * Adds a cuboid element to the model.
     */
    private static void addCuboidElement(JsonObject element, BlockDefinition.CuboidElement cuboid, boolean isTinted) {
        // From coordinates
        JsonArray from = new JsonArray();
        from.add(getClamped(cuboid.getXMin()));
        from.add(getClamped(cuboid.getYMin()));
        from.add(getClamped(cuboid.getZMin()));
        element.add("from", from);

        // To coordinates
        JsonArray to = new JsonArray();
        to.add(getClamped(cuboid.getXMax()));
        to.add(getClamped(cuboid.getYMax()));
        to.add(getClamped(cuboid.getZMax()));
        element.add("to", to);

        // Add faces
        JsonObject faces = new JsonObject();
        addCuboidFaces(faces, cuboid, isTinted);
        element.add("faces", faces);
    }

    /**
     * Adds a bounding box element to the model.
     */
    private static void addBoundingBoxElement(JsonObject element, BlockDefinition.BoundingBox bbox, boolean isTinted) {
        // From coordinates
        JsonArray from = new JsonArray();
        from.add(getClamped(bbox.getXMin()));
        from.add(getClamped(bbox.getYMin()));
        from.add(getClamped(bbox.getZMin()));
        element.add("from", from);

        // To coordinates
        JsonArray to = new JsonArray();
        to.add(getClamped(bbox.getXMax()));
        to.add(getClamped(bbox.getYMax()));
        to.add(getClamped(bbox.getZMax()));
        element.add("to", to);

        // Add faces using standard texture mapping
        JsonObject faces = new JsonObject();
        addBoundingBoxFaces(faces, bbox, isTinted);
        element.add("faces", faces);
    }

    /**
     * Adds faces to a cuboid element.
     */
    private static void addCuboidFaces(JsonObject faces, BlockDefinition.CuboidElement cuboid, boolean isTinted) {
        int[] sidetxt = cuboid.getSideTextures() != null ? cuboid.getSideTextures() : STANDARD_TEXTURE_INDICES;
        boolean[] noTint = cuboid.getNoTint() != null ? cuboid.getNoTint() : NO_TINT_ALL;
        int[] siderot = cuboid.getSideRotations() != null ?
                cuboid.getSideRotations() : new int[]{0, 0, 0, 0, 0, 0};

        // Add each face (down, up, north, south, west, east)
        addFace(faces, "down", 0, cuboid, sidetxt, noTint, siderot, isTinted);
        addFace(faces, "up", 1, cuboid, sidetxt, noTint, siderot, isTinted);
        addFace(faces, "north", 2, cuboid, sidetxt, noTint, siderot, isTinted);
        addFace(faces, "south", 3, cuboid, sidetxt, noTint, siderot, isTinted);
        addFace(faces, "west", 4, cuboid, sidetxt, noTint, siderot, isTinted);
        addFace(faces, "east", 5, cuboid, sidetxt, noTint, siderot, isTinted);
    }

    /**
     * Adds faces to a bounding box element using standard texture mapping.
     */
    private static void addBoundingBoxFaces(JsonObject faces, BlockDefinition.BoundingBox bbox, boolean isTinted) {
        int[] sidetxt = STANDARD_TEXTURE_INDICES;
        boolean[] noTint = NO_TINT_ALL;
        int[] siderot = {0, 0, 0, 0, 0, 0};

        // Add each face (down, up, north, south, west, east)
        addBoundingBoxFace(faces, "down", 0, bbox, sidetxt, noTint, siderot, isTinted);
        addBoundingBoxFace(faces, "up", 1, bbox, sidetxt, noTint, siderot, isTinted);
        addBoundingBoxFace(faces, "north", 2, bbox, sidetxt, noTint, siderot, isTinted);
        addBoundingBoxFace(faces, "south", 3, bbox, sidetxt, noTint, siderot, isTinted);
        addBoundingBoxFace(faces, "west", 4, bbox, sidetxt, noTint, siderot, isTinted);
        addBoundingBoxFace(faces, "east", 5, bbox, sidetxt, noTint, siderot, isTinted);
    }

    /**
     * Adds a single face to the faces object.
     */
    private static void addFace(JsonObject faces, String face, int index,
                         BlockDefinition.CuboidElement cuboid, int[] sidetxt,
                         boolean[] noTint, int[] siderot, boolean isTinted) {
        JsonObject faceObj = new JsonObject();

        // Set UV coordinates based on face
        JsonArray uv = new JsonArray();
        calculateUVs(face, cuboid, uv);
        faceObj.add("uv", uv);

        // Get correct texture key
        faceObj.addProperty("texture", "#txt" + sidetxt[index]);

        // Add rotation if needed
        if (siderot[index] != 0) {
            faceObj.addProperty("rotation", siderot[index]);
        }

        // Add tint if needed
        if (isTinted && !noTint[index]) {
            faceObj.addProperty("tintindex", 0);
        }

        // Add cullface if needed
        String cullface = getCullface(face, cuboid);
        if (cullface != null) {
            faceObj.addProperty("cullface", cullface);
        }

        faces.add(face, faceObj);
    }

    /**
     * Adds a single face to the faces object for bounding box elements.
     */
    private static void addBoundingBoxFace(JsonObject faces, String face, int index,
                                    BlockDefinition.BoundingBox bbox, int[] sidetxt,
                                    boolean[] noTint, int[] siderot, boolean isTinted) {
        JsonObject faceObj = new JsonObject();

        // Set UV coordinates based on face
        JsonArray uv = new JsonArray();
        calculateBoundingBoxUVs(face, bbox, uv);
        faceObj.add("uv", uv);

        // Get correct texture key
        faceObj.addProperty("texture", "#txt" + sidetxt[index]);

        // Add rotation if needed
        if (siderot[index] != 0) {
            faceObj.addProperty("rotation", siderot[index]);
        }

        // Add tint if needed
        if (isTinted && !noTint[index]) {
            faceObj.addProperty("tintindex", 0);
        }

        // Add cullface if needed
        String cullface = getBoundingBoxCullface(face, bbox);
        if (cullface != null) {
            faceObj.addProperty("cullface", cullface);
        }

        faces.add(face, faceObj);
    }

    /**
     * Calculates UV coordinates for a face.
     */
    private static void calculateUVs(String face, BlockDefinition.CuboidElement cuboid, JsonArray uv) {
        // Calculate UV coordinates based on face and cuboid dimensions
        switch (face) {
            case "down" -> {
                uv.add(getClamped(cuboid.getXMin()));
                uv.add(16 - getClamped(cuboid.getZMax()));
                uv.add(getClamped(cuboid.getXMax()));
                uv.add(16 - getClamped(cuboid.getZMin()));
            }
            case "up" -> {
                uv.add(getClamped(cuboid.getXMin()));
                uv.add(getClamped(cuboid.getZMin()));
                uv.add(getClamped(cuboid.getXMax()));
                uv.add(getClamped(cuboid.getZMax()));
            }
            case "north" -> {
                uv.add(16 - getClamped(cuboid.getXMax()));
                uv.add(16 - getClamped(cuboid.getYMax()));
                uv.add(16 - getClamped(cuboid.getXMin()));
                uv.add(16 - getClamped(cuboid.getYMin()));
            }
            case "south" -> {
                uv.add(getClamped(cuboid.getXMin()));
                uv.add(16 - getClamped(cuboid.getYMax()));
                uv.add(getClamped(cuboid.getXMax()));
                uv.add(16 - getClamped(cuboid.getYMin()));
            }
            case "west" -> {
                uv.add(getClamped(cuboid.getZMin()));
                uv.add(16 - getClamped(cuboid.getYMax()));
                uv.add(getClamped(cuboid.getZMax()));
                uv.add(16 - getClamped(cuboid.getYMin()));
            }
            case "east" -> {
                uv.add(16 - getClamped(cuboid.getZMax()));
                uv.add(16 - getClamped(cuboid.getYMax()));
                uv.add(16 - getClamped(cuboid.getZMin()));
                uv.add(16 - getClamped(cuboid.getYMin()));
            }
        }
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
     * Gets the cullface for a face if needed.
     */
    private static String getCullface(String face, BlockDefinition.CuboidElement cuboid) {
        return switch (face) {
            case "down" -> cuboid.getYMin() <= 0 ? "down" : null;
            case "up" -> cuboid.getYMax() >= 1 ? "up" : null;
            case "north" -> cuboid.getZMin() <= 0 ? "north" : null;
            case "south" -> cuboid.getZMax() >= 1 ? "south" : null;
            case "west" -> cuboid.getXMin() <= 0 ? "west" : null;
            case "east" -> cuboid.getXMax() >= 1 ? "east" : null;
            default -> null;
        };
    }

    /**
     * Calculates UV coordinates for a bounding box face.
     */
    private static void calculateBoundingBoxUVs(String face, BlockDefinition.BoundingBox bbox, JsonArray uv) {
        // Calculate UV coordinates based on face and bounding box dimensions
        switch (face) {
            case "down" -> {
                uv.add(getClamped(bbox.getXMin()));
                uv.add(16 - getClamped(bbox.getZMax()));
                uv.add(getClamped(bbox.getXMax()));
                uv.add(16 - getClamped(bbox.getZMin()));
            }
            case "up" -> {
                uv.add(getClamped(bbox.getXMin()));
                uv.add(getClamped(bbox.getZMin()));
                uv.add(getClamped(bbox.getXMax()));
                uv.add(getClamped(bbox.getZMax()));
            }
            case "north" -> {
                uv.add(16 - getClamped(bbox.getXMax()));
                uv.add(16 - getClamped(bbox.getYMax()));
                uv.add(16 - getClamped(bbox.getXMin()));
                uv.add(16 - getClamped(bbox.getYMin()));
            }
            case "south" -> {
                uv.add(getClamped(bbox.getXMin()));
                uv.add(16 - getClamped(bbox.getYMax()));
                uv.add(getClamped(bbox.getXMax()));
                uv.add(16 - getClamped(bbox.getYMin()));
            }
            case "west" -> {
                uv.add(getClamped(bbox.getZMin()));
                uv.add(16 - getClamped(bbox.getYMax()));
                uv.add(getClamped(bbox.getZMax()));
                uv.add(16 - getClamped(bbox.getYMin()));
            }
            case "east" -> {
                uv.add(16 - getClamped(bbox.getZMax()));
                uv.add(16 - getClamped(bbox.getYMax()));
                uv.add(16 - getClamped(bbox.getZMin()));
                uv.add(16 - getClamped(bbox.getYMin()));
            }
        }
    }

    /**
     * Gets the cullface for a bounding box face if needed.
     */
    private static String getBoundingBoxCullface(String face, BlockDefinition.BoundingBox bbox) {
        return switch (face) {
            case "down" -> bbox.getYMin() <= 0 ? "down" : null;
            case "up" -> bbox.getYMax() >= 1 ? "up" : null;
            case "north" -> bbox.getZMin() <= 0 ? "north" : null;
            case "south" -> bbox.getZMax() >= 1 ? "south" : null;
            case "west" -> bbox.getXMin() <= 0 ? "west" : null;
            case "east" -> bbox.getXMax() >= 1 ? "east" : null;
            default -> null;
        };
    }

    /**
     * Creates a texture map for cuboid blocks using standard texture keys.
     */
    public static TextureMap createCuboidTextureMap(List<String> textures) {
        TextureMap textureMap = new TextureMap();

        // Handle null or empty texture lists
        if (textures == null || textures.isEmpty()) {
            // Use missing texture as fallback
            String fallbackTexture = "missing";
            Identifier fallbackId = createBlockIdentifier(fallbackTexture);
            textureMap.put(TextureKey.DOWN, fallbackId);
            textureMap.put(TextureKey.UP, fallbackId);
            textureMap.put(TextureKey.NORTH, fallbackId);
            textureMap.put(TextureKey.SOUTH, fallbackId);
            textureMap.put(TextureKey.WEST, fallbackId);
            textureMap.put(TextureKey.EAST, fallbackId);
            textureMap.put(TextureKey.PARTICLE, fallbackId);
            textureMap.put(TextureKey.ALL, fallbackId);
            return textureMap;
        }

        int textureCount = textures.size();

        // For single texture, add ALL key for Models.CUBE_ALL
        if (textureCount == 1) {
            Identifier textureId = createBlockIdentifier(textures.get(0));
            textureMap.put(TextureKey.ALL, textureId);
            textureMap.put(TextureKey.PARTICLE, textureId);
        }

        // Map textures to standard cube faces: down, up, north, south, west, east
        TextureKey[] faceKeys = {TextureKey.DOWN, TextureKey.UP, TextureKey.NORTH, TextureKey.SOUTH, TextureKey.WEST, TextureKey.EAST};

        for (int i = 0; i < 6; i++) {
            String texture = i < textureCount ? textures.get(i) : textures.get(textureCount - 1);
            textureMap.put(faceKeys[i], createBlockIdentifier(texture));
        }

        textureMap.put(TextureKey.PARTICLE, createBlockIdentifier(textures.get(0)));
        return textureMap;
    }

    /**
     * Creates a texture map for custom cuboid models using ModTextureKey keys.
     */
    private static TextureMap createCustomCuboidTextureMap(List<String> textures) {
        TextureMap textureMap = new TextureMap();
        int textureCount = textures.size();

        // For blocks with single texture, we need to map it to all 6 sides
        int requiredTextures = Math.max(6, textureCount);

        for (int i = 0; i < requiredTextures; i++) {
            String texture = i < textureCount ? textures.get(i) : textures.get(textureCount - 1);
            textureMap.put(ModTextureKey.getTextureNKey(i), createBlockIdentifier(texture));
        }

        textureMap.put(TextureKey.PARTICLE, createBlockIdentifier(textures.get(0)));
        return textureMap;
    }

    /**
     * Checks if the definition has cuboids or bounding box that requires custom model generation.
     */
    public static boolean hasCuboids(BlockDefinition definition) {
        return (definition.getCuboids() != null && !definition.getCuboids().isEmpty()) ||
               definition.hasBoundingBox();
    }
}