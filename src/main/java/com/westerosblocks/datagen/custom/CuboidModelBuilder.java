package com.westerosblocks.datagen.custom;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.datagen.ModTextureKey;
import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.Model;
import net.minecraft.data.client.TextureKey;
import net.minecraft.data.client.TextureMap;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Assembles Minecraft block-model JSON from {@link BlockDefinition} cuboid and
 * bounding-box data. Handles UV calculation, face rotations, cullface detection,
 * crossed (plant-style) elements, and optional Y-axis rotation of the whole
 * element.
 *
 * <p>This class only produces model JSON — it does not emit blockstates or
 * orchestrate iteration across states. Callers (e.g. {@link CuboidBlockExporter})
 * drive state/texture-set iteration and delegate per-variant model creation here.
 */
final class CuboidModelBuilder {
    private static final int[] STANDARD_TEXTURE_INDICES = {0, 1, 2, 3, 4, 5};
    private static final boolean[] NO_TINT_ALL = {false, false, false, false, false, false};

    private CuboidModelBuilder() {}

    /** @see #createCuboidModel(BlockStateModelGenerator, Block, BlockDefinition, List, int, String, Float, List) */
    static Identifier createCuboidModel(BlockStateModelGenerator generator, Block block,
            BlockDefinition definition, List<String> textures, int stateIndex, String variant) {
        return createCuboidModel(generator, block, definition, textures, stateIndex, variant, null, null);
    }

    /** @see #createCuboidModel(BlockStateModelGenerator, Block, BlockDefinition, List, int, String, Float, List) */
    static Identifier createCuboidModel(BlockStateModelGenerator generator, Block block,
            BlockDefinition definition, List<String> textures, int stateIndex, String variant, Float rotation) {
        return createCuboidModel(generator, block, definition, textures, stateIndex, variant, rotation, null);
    }

    /**
     * Builds and uploads a custom cuboid model.
     *
     * @param rotation       optional Y-axis rotation in degrees (e.g. -22.5, 45); {@code null} for none
     * @param cuboidOverride pre-transformed cuboids to use instead of {@code definition.getCuboids()};
     *                       {@code null} means use the definition's cuboids as-is
     */
    static Identifier createCuboidModel(BlockStateModelGenerator generator, Block block,
            BlockDefinition definition, List<String> textures, int stateIndex, String variant,
            Float rotation, List<BlockDefinition.CuboidElement> cuboidOverride) {
        TextureMap textureMap = customCuboidTextureMap(textures);
        Identifier modelId = BaseBlockExporter.createGeneratedModelId(block, variant);

        Model cuboidModel = buildModel(definition, textures, rotation, cuboidOverride);
        cuboidModel.upload(modelId, textureMap, generator.modelCollector);

        return modelId;
    }

    /**
     * Texture map for standard cube models using vanilla {@link TextureKey} face
     * slots (DOWN, UP, NORTH, SOUTH, WEST, EAST, PARTICLE, ALL). Fills missing
     * slots with the last texture. Falls back to {@code "missing"} when the list
     * is null/empty.
     */
    static TextureMap standardCubeTextureMap(List<String> textures) {
        TextureMap textureMap = new TextureMap();

        if (textures == null || textures.isEmpty()) {
            Identifier fallback = BaseBlockExporter.createBlockIdentifier("missing");
            textureMap.put(TextureKey.DOWN, fallback);
            textureMap.put(TextureKey.UP, fallback);
            textureMap.put(TextureKey.NORTH, fallback);
            textureMap.put(TextureKey.SOUTH, fallback);
            textureMap.put(TextureKey.WEST, fallback);
            textureMap.put(TextureKey.EAST, fallback);
            textureMap.put(TextureKey.PARTICLE, fallback);
            textureMap.put(TextureKey.ALL, fallback);
            return textureMap;
        }

        int textureCount = textures.size();

        if (textureCount == 1) {
            Identifier textureId = BaseBlockExporter.createBlockIdentifier(textures.get(0));
            textureMap.put(TextureKey.ALL, textureId);
            textureMap.put(TextureKey.PARTICLE, textureId);
        }

        TextureKey[] faceKeys = {TextureKey.DOWN, TextureKey.UP, TextureKey.NORTH, TextureKey.SOUTH, TextureKey.WEST, TextureKey.EAST};
        for (int i = 0; i < 6; i++) {
            String texture = i < textureCount ? textures.get(i) : textures.get(textureCount - 1);
            textureMap.put(faceKeys[i], BaseBlockExporter.createBlockIdentifier(texture));
        }
        textureMap.put(TextureKey.PARTICLE, BaseBlockExporter.createBlockIdentifier(textures.get(0)));
        return textureMap;
    }

    /** True if this definition requires custom cuboid-model generation (cuboids or bounding box present). */
    static boolean hasCuboids(BlockDefinition definition) {
        return (definition.getCuboids() != null && !definition.getCuboids().isEmpty())
                || definition.hasBoundingBox();
    }

    /**
     * Texture map keyed by numbered {@link ModTextureKey} slots (txt0, txt1, ...)
     * used by the custom cuboid model format. Fills missing slots with the last
     * texture. Always populates at least 6 slots.
     */
    private static TextureMap customCuboidTextureMap(List<String> textures) {
        TextureMap textureMap = new TextureMap();
        int textureCount = textures.size();
        int requiredTextures = Math.max(6, textureCount);

        for (int i = 0; i < requiredTextures; i++) {
            String texture = i < textureCount ? textures.get(i) : textures.get(textureCount - 1);
            textureMap.put(ModTextureKey.getTextureNKey(i), BaseBlockExporter.createBlockIdentifier(texture));
        }

        textureMap.put(TextureKey.PARTICLE, BaseBlockExporter.createBlockIdentifier(textures.get(0)));
        return textureMap;
    }

    private static Model buildModel(BlockDefinition definition, List<String> textures,
                                    Float rotation, List<BlockDefinition.CuboidElement> cuboidOverride) {
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

                JsonArray elements = new JsonArray();
                List<BlockDefinition.CuboidElement> cuboids =
                        cuboidOverride != null ? cuboidOverride : definition.getCuboids();

                if (cuboids != null && !cuboids.isEmpty()) {
                    for (BlockDefinition.CuboidElement cuboid : cuboids) {
                        addCuboid(elements, cuboid, definition.isTinted(), rotation, json);
                    }
                } else if (definition.hasBoundingBox()) {
                    JsonObject element = new JsonObject();
                    addBoundingBoxElement(element, definition.getBoundingBox(), definition.isTinted());
                    if (rotation != null) addRotation(element, rotation);
                    elements.add(element);
                }

                json.add("elements", elements);
                return json;
            }
        };
    }

    private static void addCuboid(JsonArray elements, BlockDefinition.CuboidElement cuboid,
                                  boolean isTinted, Float rotation, JsonObject modelJson) {
        if ("crossed".equals(cuboid.getShape())) {
            modelJson.addProperty("ambientocclusion", false);
            JsonObject e1 = createCrossedElement(cuboid, true, isTinted);
            JsonObject e2 = createCrossedElement(cuboid, false, isTinted);
            if (rotation != null) {
                addRotation(e1, rotation);
                addRotation(e2, rotation);
            }
            elements.add(e1);
            elements.add(e2);
        } else {
            JsonObject element = new JsonObject();
            addCuboidElement(element, cuboid, isTinted);
            if (rotation != null) addRotation(element, rotation);
            elements.add(element);
        }
    }

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

    private static JsonObject createCrossedElement(BlockDefinition.CuboidElement cuboid,
                                                    boolean firstDiagonal, boolean isTinted) {
        JsonObject element = new JsonObject();

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

        element.addProperty("shade", false);

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

    private static void addCrossedFace(JsonObject faces, String direction, boolean isTinted) {
        JsonObject face = new JsonObject();
        JsonArray uv = new JsonArray();
        uv.add(0.0);
        uv.add(0.0);
        uv.add(16.0);
        uv.add(16.0);
        face.add("uv", uv);
        face.addProperty("texture", "#txt0");
        if (isTinted) face.addProperty("tintindex", 0);
        faces.add(direction, face);
    }

    private static void addCuboidElement(JsonObject element, BlockDefinition.CuboidElement cuboid, boolean isTinted) {
        JsonArray from = new JsonArray();
        from.add(getClamped(cuboid.getXMin()));
        from.add(getClamped(cuboid.getYMin()));
        from.add(getClamped(cuboid.getZMin()));
        element.add("from", from);

        JsonArray to = new JsonArray();
        to.add(getClamped(cuboid.getXMax()));
        to.add(getClamped(cuboid.getYMax()));
        to.add(getClamped(cuboid.getZMax()));
        element.add("to", to);

        JsonObject faces = new JsonObject();
        addCuboidFaces(faces, cuboid, isTinted);
        element.add("faces", faces);
    }

    private static void addBoundingBoxElement(JsonObject element, BlockDefinition.BoundingBox bbox, boolean isTinted) {
        JsonArray from = new JsonArray();
        from.add(getClamped(bbox.getXMin()));
        from.add(getClamped(bbox.getYMin()));
        from.add(getClamped(bbox.getZMin()));
        element.add("from", from);

        JsonArray to = new JsonArray();
        to.add(getClamped(bbox.getXMax()));
        to.add(getClamped(bbox.getYMax()));
        to.add(getClamped(bbox.getZMax()));
        element.add("to", to);

        JsonObject faces = new JsonObject();
        addBoundingBoxFaces(faces, bbox, isTinted);
        element.add("faces", faces);
    }

    private static void addCuboidFaces(JsonObject faces, BlockDefinition.CuboidElement cuboid, boolean isTinted) {
        int[] sidetxt = cuboid.getSideTextures() != null ? cuboid.getSideTextures() : STANDARD_TEXTURE_INDICES;
        boolean[] noTint = cuboid.getNoTint() != null ? cuboid.getNoTint() : NO_TINT_ALL;
        int[] siderot = cuboid.getSideRotations() != null ? cuboid.getSideRotations() : new int[]{0, 0, 0, 0, 0, 0};

        addFace(faces, "down",  0, cuboid, sidetxt, noTint, siderot, isTinted);
        addFace(faces, "up",    1, cuboid, sidetxt, noTint, siderot, isTinted);
        addFace(faces, "north", 2, cuboid, sidetxt, noTint, siderot, isTinted);
        addFace(faces, "south", 3, cuboid, sidetxt, noTint, siderot, isTinted);
        addFace(faces, "west",  4, cuboid, sidetxt, noTint, siderot, isTinted);
        addFace(faces, "east",  5, cuboid, sidetxt, noTint, siderot, isTinted);
    }

    private static void addBoundingBoxFaces(JsonObject faces, BlockDefinition.BoundingBox bbox, boolean isTinted) {
        int[] sidetxt = STANDARD_TEXTURE_INDICES;
        boolean[] noTint = NO_TINT_ALL;
        int[] siderot = {0, 0, 0, 0, 0, 0};

        addBoundingBoxFace(faces, "down",  0, bbox, sidetxt, noTint, siderot, isTinted);
        addBoundingBoxFace(faces, "up",    1, bbox, sidetxt, noTint, siderot, isTinted);
        addBoundingBoxFace(faces, "north", 2, bbox, sidetxt, noTint, siderot, isTinted);
        addBoundingBoxFace(faces, "south", 3, bbox, sidetxt, noTint, siderot, isTinted);
        addBoundingBoxFace(faces, "west",  4, bbox, sidetxt, noTint, siderot, isTinted);
        addBoundingBoxFace(faces, "east",  5, bbox, sidetxt, noTint, siderot, isTinted);
    }

    private static void addFace(JsonObject faces, String face, int index,
                                BlockDefinition.CuboidElement cuboid, int[] sidetxt,
                                boolean[] noTint, int[] siderot, boolean isTinted) {
        JsonObject faceObj = new JsonObject();

        JsonArray uv = new JsonArray();
        calculateUVs(face, cuboid, uv);
        if (siderot[index] == 90 || siderot[index] == 270) swapUV(uv);
        faceObj.add("uv", uv);

        faceObj.addProperty("texture", "#txt" + sidetxt[index]);
        if (siderot[index] != 0) faceObj.addProperty("rotation", siderot[index]);
        if (isTinted && !noTint[index]) faceObj.addProperty("tintindex", 0);

        String cullface = getCullface(face, cuboid);
        if (cullface != null) faceObj.addProperty("cullface", cullface);

        faces.add(face, faceObj);
    }

    private static void addBoundingBoxFace(JsonObject faces, String face, int index,
                                           BlockDefinition.BoundingBox bbox, int[] sidetxt,
                                           boolean[] noTint, int[] siderot, boolean isTinted) {
        JsonObject faceObj = new JsonObject();

        JsonArray uv = new JsonArray();
        calculateBoundingBoxUVs(face, bbox, uv);
        if (siderot[index] == 90 || siderot[index] == 270) swapUV(uv);
        faceObj.add("uv", uv);

        faceObj.addProperty("texture", "#txt" + sidetxt[index]);
        if (siderot[index] != 0) faceObj.addProperty("rotation", siderot[index]);
        if (isTinted && !noTint[index]) faceObj.addProperty("tintindex", 0);

        String cullface = getBoundingBoxCullface(face, bbox);
        if (cullface != null) faceObj.addProperty("cullface", cullface);

        faces.add(face, faceObj);
    }

    // Swap U↔V when face rotation is 90° or 270° (matches 1.18.2 processRotation).
    private static void swapUV(JsonArray uv) {
        JsonElement tmp0 = uv.get(0);
        JsonElement tmp2 = uv.get(2);
        uv.set(0, uv.get(1));
        uv.set(1, tmp0);
        uv.set(2, uv.get(3));
        uv.set(3, tmp2);
    }

    private static void calculateUVs(String face, BlockDefinition.CuboidElement cuboid, JsonArray uv) {
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

    private static void calculateBoundingBoxUVs(String face, BlockDefinition.BoundingBox bbox, JsonArray uv) {
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

    private static float getClamped(double v) {
        float f = (float) (16.0 * v);
        if (f < -16f) f = -16f;
        if (f > 32f) f = 32f;
        return f;
    }

    private static String getCullface(String face, BlockDefinition.CuboidElement cuboid) {
        return switch (face) {
            case "down"  -> cuboid.getYMin() <= 0 ? "down"  : null;
            case "up"    -> cuboid.getYMax() >= 1 ? "up"    : null;
            case "north" -> cuboid.getZMin() <= 0 ? "north" : null;
            case "south" -> cuboid.getZMax() >= 1 ? "south" : null;
            case "west"  -> cuboid.getXMin() <= 0 ? "west"  : null;
            case "east"  -> cuboid.getXMax() >= 1 ? "east"  : null;
            default -> null;
        };
    }

    private static String getBoundingBoxCullface(String face, BlockDefinition.BoundingBox bbox) {
        return switch (face) {
            case "down"  -> bbox.getYMin() <= 0 ? "down"  : null;
            case "up"    -> bbox.getYMax() >= 1 ? "up"    : null;
            case "north" -> bbox.getZMin() <= 0 ? "north" : null;
            case "south" -> bbox.getZMax() >= 1 ? "south" : null;
            case "west"  -> bbox.getXMin() <= 0 ? "west"  : null;
            case "east"  -> bbox.getXMax() >= 1 ? "east"  : null;
            default -> null;
        };
    }
}
