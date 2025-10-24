package com.westerosblocks.datagen.custom;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;
import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.datagen.ModTextureKey;
import com.westerosblocks.block.custom.WCCuboidBlock;
import com.westerosblocks.data.BlockDefinition;

import java.util.*;


public class CuboidBlockExporter extends BaseBlockExporter {

    private static final int[] STANDARD_TEXTURE_INDICES = {0, 1, 2, 3, 4, 5};
    private static final boolean[] NO_TINT_ALL = {false, false, false, false, false, false};


    /**
     * Registers a cuboid block from a BlockDefinition.
     * Uses uniform iteration pattern: After doInit(), states is ALWAYS non-empty,
     * and each state has randomTextures normalized from simple textures.
     */
    public static void registerCustomCuboidBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        if (!(block instanceof WCCuboidBlock cuboidBlock)) {
            throw new IllegalArgumentException("Block must be a WCCuboidBlock instance");
        }

        // After doInit(), states is ALWAYS non-empty (at least synthetic base state exists)
        var states = definition.getStates();

        if (states == null || states.isEmpty()) {
            throw new IllegalStateException("Block definition states should never be null/empty after doInit() for block: " + getBlockName(block));
        }

        // Determine if this block actually has multiple states (needs STATE property in variants)
        boolean hasMultipleStates = definition.getStateCount() > 1;

        // Check for custom model first - but only if it's a single state block
        // Multi-state blocks with custom models need per-state iteration
        if (definition.hasCustomModel() && !hasMultipleStates) {
            registerCustomModelCuboidBlock(generator, block, definition, cuboidBlock);
            return;
        }

        // Collect all model identifiers for all states
        Map<String, List<Identifier>> stateModelIds = new HashMap<>();
        Identifier firstModel = null;

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

            // Iterate through all texture sets for this state
            for (int setIdx = 0; setIdx < textureSetCount; setIdx++) {
                Identifier modelId;
                String variantName = (hasMultipleStates ? stateId + "_" : "") + "v" + (setIdx + 1);

                // Check if this state uses custom models
                if (state.isCustomModel()) {
                    // Use custom model reference instead of generating from textures
                    modelId = createCustomModelId(block, variantName);
                } else {
                    BlockDefinition.RandomTextureVariant set = state.getRandomTextureSet(setIdx);
                    if (set == null || set.getTextureCount() == 0) {
                        continue;
                    }

                    // Extract textures from this set
                    String[] textures = new String[set.getTextureCount()];
                    for (int i = 0; i < set.getTextureCount(); i++) {
                        textures[i] = set.getTextureByIndex(i);
                    }

                    // Convert to List for compatibility with existing helper methods
                    List<String> textureList = Arrays.asList(textures);

                    if (hasCuboids(definition)) {
                        // Generate custom cuboid models with geometry
                        modelId = createCuboidModel(generator, block, definition, textureList, setIdx, variantName);
                    } else {
                        // Generate standard cube models
                        TextureMap textureMap = createCuboidTextureMap(textureList);
                        if (textureList.size() == 1) {
                            modelId = Models.CUBE_ALL.upload(createGeneratedModelId(block, variantName), textureMap, generator.modelCollector);
                        } else {
                            modelId = Models.CUBE.upload(createGeneratedModelId(block, variantName), textureMap, generator.modelCollector);
                        }
                    }
                }

                modelIds.add(modelId);
                if (firstModel == null) firstModel = modelId;
            }

            if (!modelIds.isEmpty()) {
                stateModelIds.put(stateId, modelIds);
            }
        }

        if (stateModelIds.isEmpty()) {
            // Fallback if no valid models generated
            registerFallbackCuboidBlock(generator, block, definition, cuboidBlock);
            return;
        }

        // Generate blockstate based on whether we have multiple states
        if (hasMultipleStates) {
            generator.blockStateCollector.accept(createAdvancedStatesBlockState(block, stateModelIds));
        } else {
            // Single state - no state prefix in variants
            List<Identifier> modelIds = stateModelIds.values().iterator().next();
            if (modelIds.size() == 1) {
                // Single model
                generator.blockStateCollector.accept(createSimpleBlockState(block, modelIds.get(0)));
            } else {
                // Multiple models (random textures)
                List<BlockStateVariant> variants = modelIds.stream()
                    .map(BaseBlockExporter::createVariant)
                    .toList();
                generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block, variants.toArray(new BlockStateVariant[0])));
            }
        }

        // Register item model
        if (firstModel != null) {
            registerParentedItemModel(generator, block, firstModel);
        }
    }

    /**
     * Registers a simple cuboid block with basic textures.
     */
    private static void registerSimpleCuboidBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition, WCCuboidBlock cuboidBlock) {
        List<String> textures = definition.getTextures();

        Identifier modelId;
        if (definition.hasCustomModel()) {
            // For custom models, just reference the existing model file
            modelId = createCustomModelId(block, "base_v1");
        } else if (hasCuboids(definition)) {
            if (textures != null && !textures.isEmpty()) {
                modelId = createCuboidModel(generator, block, definition, textures, 0, "base_v1");
            } else {
                // Reference existing custom model files
                modelId = createGeneratedModelId(block, "base_v1");
            }
        } else {
            // Use standard cube model
            TextureMap textureMap = createCuboidTextureMap(textures);
            if (textures != null && textures.size() == 1) {
                modelId = Models.CUBE_ALL.upload(createGeneratedModelId(block, "base_v1"), textureMap, generator.modelCollector);
            } else {
                modelId = Models.CUBE.upload(createGeneratedModelId(block, "base_v1"), textureMap, generator.modelCollector);
            }
        }

        generator.blockStateCollector.accept(createSimpleBlockState(block, modelId));
        registerParentedItemModel(generator, block, modelId);
    }

    /**
     * Registers a cuboid block with random texture variants.
     */
    private static void registerCustomCuboidBlockWithRandomTextures(BlockStateModelGenerator generator, Block block, BlockDefinition definition, WCCuboidBlock cuboidBlock) {
        List<BlockDefinition.RandomTextureVariant> randomTextures = definition.getRandomTextures();
        List<Identifier> modelIds = new ArrayList<>();

        for (int i = 0; i < randomTextures.size(); i++) {
            BlockDefinition.RandomTextureVariant variant = randomTextures.get(i);
            List<String> textures = variant.getTextures();

            Identifier modelId;
            if (definition.hasCustomModel()) {
                // For custom models, just reference the existing model files
                modelId = createCustomModelId(block, "base_v" + (i + 1));
            } else if (hasCuboids(definition)) {
                // Generate custom cuboid models with geometry
                modelId = createCuboidModel(generator, block, definition, textures, i, "base_v" + (i + 1));
            } else {
                // Generate standard cube models
                TextureMap textureMap = createCuboidTextureMap(textures);
                modelId = Models.CUBE.upload(createGeneratedModelId(block, "base_v" + (i + 1)), textureMap, generator.modelCollector);
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
     * Registers a cuboid block with multiple states.
     */
    private static void registerCustomCuboidBlockWithStates(BlockStateModelGenerator generator, Block block, BlockDefinition definition, WCCuboidBlock cuboidBlock) {
        List<BlockDefinition.StateVariant> states = definition.getStates();
        Map<String, List<Identifier>> stateModelIds = new HashMap<>();

        for (int i = 0; i < states.size(); i++) {
            BlockDefinition.StateVariant state = states.get(i);
            String stateId = state.getStateID() != null ? state.getStateID() : "state" + i;

            // Check if this state has randomTextures
            if (state.getRandomTextures() != null && !state.getRandomTextures().isEmpty()) {
                // Handle state with multiple random texture variants
                List<Identifier> variantIds = new ArrayList<>();
                List<BlockDefinition.RandomTextureVariant> randomTextures = state.getRandomTextures();

                for (int j = 0; j < randomTextures.size(); j++) {
                    BlockDefinition.RandomTextureVariant variant = randomTextures.get(j);
                    List<String> textures = variant.getTextures();
                    String variantName = stateId + "_v" + (j + 1);

                    Identifier modelId;
                    if (definition.hasCustomModel()) {
                        // For custom models, just reference the existing model file
                        modelId = createCustomModelId(block, variantName);
                    } else if (hasCuboids(definition)) {
                        if (textures != null && !textures.isEmpty()) {
                            modelId = createCuboidModel(generator, block, definition, textures, j, variantName);
                        } else {
                            modelId = createGeneratedModelId(block, variantName);
                        }
                    } else {
                        TextureMap textureMap = createCuboidTextureMap(textures);
                        modelId = Models.CUBE.upload(createGeneratedModelId(block, variantName), textureMap, generator.modelCollector);
                    }
                    variantIds.add(modelId);
                }
                stateModelIds.put(stateId, variantIds);
            } else {
                // Handle state with single texture set
                List<String> textures = state.getTextures();
                String variantName = stateId + "_v1";

                Identifier modelId;
                if (definition.hasCustomModel()) {
                    // For custom models, just reference the existing model file
                    modelId = createCustomModelId(block, variantName);
                } else if (hasCuboids(definition)) {
                    if (textures != null && !textures.isEmpty()) {
                        modelId = createCuboidModel(generator, block, definition, textures, i, variantName);
                    } else {
                        modelId = createGeneratedModelId(block, variantName);
                    }
                } else {
                    TextureMap textureMap = createCuboidTextureMap(textures);
                    modelId = Models.CUBE.upload(createGeneratedModelId(block, variantName), textureMap, generator.modelCollector);
                }
                stateModelIds.put(stateId, List.of(modelId));
            }
        }

        // Create custom blockstate supplier for state-based variants
        generator.blockStateCollector.accept(createAdvancedStatesBlockState(block, stateModelIds));

        // Use the first model for item model
        if (!stateModelIds.isEmpty()) {
            List<Identifier> firstStateModels = stateModelIds.values().iterator().next();
            if (!firstStateModels.isEmpty()) {
                registerParentedItemModel(generator, block, firstStateModels.get(0));
            }
        }
    }

    /**
     * Registers a cuboid block with custom models but no defined textures (like bushy_grass_spruce).
     */
    private static void registerCustomModelCuboidBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition, WCCuboidBlock cuboidBlock) {
        if (definition.hasRandomTextures()) {
            // Handle empty random texture variants - only reference existing models for isCustomModel
            List<BlockDefinition.RandomTextureVariant> randomTextures = definition.getRandomTextures();
            List<Identifier> modelIds = new ArrayList<>();

            for (int i = 0; i < randomTextures.size(); i++) {
                if (definition.hasCustomModel()) {
                    // For custom models, just reference the existing model files
                    Identifier modelId = createCustomModelId(block, "base_v" + (i + 1));
                    modelIds.add(modelId);
                } else {
                    // Generate actual models for non-custom model blocks
                    BlockDefinition.RandomTextureVariant variant = randomTextures.get(i);
                    List<String> textures = variant.getTextures();

                    // If no textures defined, use fallback texture
                    if (textures == null || textures.isEmpty()) {
                        textures = List.of("missing");
                    }

                    Identifier modelId = createCuboidModel(generator, block, definition, textures, i, "base_v" + (i + 1));
                    modelIds.add(modelId);
                }
            }

            // Check if we need to generate rotation variants
            if (definition.hasRotateRandom()) {
                List<BlockStateVariant> variants = new ArrayList<>();
                for (Identifier modelId : modelIds) {
                    // Add rotation variants: 0°, 90°, 180°, 270°
                    variants.add(createVariant(modelId, 0));
                    variants.add(createVariant(modelId, 90));
                    variants.add(createVariant(modelId, 180));
                    variants.add(createVariant(modelId, 270));
                }
                generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block, variants.toArray(new BlockStateVariant[0])));
            } else {
                List<BlockStateVariant> variants = modelIds.stream()
                    .map(BaseBlockExporter::createVariant)
                    .toList();
                generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block, variants.toArray(new BlockStateVariant[0])));
            }

            if (!modelIds.isEmpty()) {
                registerParentedItemModel(generator, block, modelIds.get(0));
            }
        } else {
            // Single custom model
            Identifier modelId;
            if (definition.hasCustomModel()) {
                // For custom models, just reference the existing model file
                modelId = createCustomModelId(block, "base_v1");
            } else {
                // Generate actual model for non-custom model blocks
                List<String> fallbackTextures = List.of("missing");
                modelId = createCuboidModel(generator, block, definition, fallbackTextures, 0, "base_v1");
            }

            if (definition.hasRotateRandom()) {
                List<BlockStateVariant> variants = List.of(
                    createVariant(modelId, 0),
                    createVariant(modelId, 90),
                    createVariant(modelId, 180),
                    createVariant(modelId, 270)
                );
                generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block, variants.toArray(new BlockStateVariant[0])));
            } else {
                generator.blockStateCollector.accept(createSimpleBlockState(block, modelId));
            }

            registerParentedItemModel(generator, block, modelId);
        }
    }

    /**
     * Fallback registration for cuboid blocks with no textures or models defined.
     */
    private static void registerFallbackCuboidBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition, WCCuboidBlock cuboidBlock) {
        // Use a default texture based on block name or create a missing texture
        String defaultTexture = "missing"; // Minecraft's missing texture
        List<String> fallbackTextures = List.of(defaultTexture);

        TextureMap textureMap = createCuboidTextureMap(fallbackTextures);
        Identifier modelId = Models.CUBE_ALL.upload(createGeneratedModelId(block, "base_v1"), textureMap, generator.modelCollector);

        generator.blockStateCollector.accept(createSimpleBlockState(block, modelId));
        registerParentedItemModel(generator, block, modelId);
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

        // Handle null or empty texture lists
        if (textures == null || textures.isEmpty()) {
            // Use missing texture as fallback
            String fallbackTexture = "missing";
            for (int i = 0; i < 6; i++) {
                textureMap.put(ModTextureKey.getTextureNKey(i), createBlockIdentifier(fallbackTexture));
            }
            textureMap.put(TextureKey.PARTICLE, createBlockIdentifier(fallbackTexture));
            return textureMap;
        }

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


    /**
     * Creates an advanced blockstate supplier for states with multiple model variants.
     */
    private static BlockStateSupplier createAdvancedStatesBlockState(Block block, Map<String, List<Identifier>> stateModelIds) {
        return new BlockStateSupplier() {
            @Override
            public Block getBlock() {
                return block;
            }

            @Override
            public JsonElement get() {
                JsonObject json = new JsonObject();
                JsonObject variants = new JsonObject();

                for (Map.Entry<String, List<Identifier>> entry : stateModelIds.entrySet()) {
                    String stateId = entry.getKey();
                    List<Identifier> modelIds = entry.getValue();

                    if (modelIds.size() == 1) {
                        // Single model variant
                        JsonObject variant = new JsonObject();
                        variant.addProperty("model", modelIds.get(0).toString());
                        variants.add("state=" + stateId, variant);
                    } else {
                        // Multiple model variants (array)
                        JsonArray variantArray = new JsonArray();
                        for (Identifier modelId : modelIds) {
                            JsonObject variant = new JsonObject();
                            variant.addProperty("model", modelId.toString());
                            variantArray.add(variant);
                        }
                        variants.add("state=" + stateId, variantArray);
                    }
                }

                json.add("variants", variants);
                return json;
            }
        };
    }
}