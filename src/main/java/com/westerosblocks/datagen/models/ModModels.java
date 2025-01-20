package com.westerosblocks.datagen.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.westerosblocks.WesterosBlocks;
import net.minecraft.data.client.Model;
import net.minecraft.data.client.Models;
import net.minecraft.data.client.TextureKey;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.Optional;

public class ModModels {
    private static final String GENERATED_PATH = "block/";

    static Model createAllSides(String parent, String namespace) {
        return block(parent,
                namespace,
                TextureKey.DOWN,
                TextureKey.UP,
                TextureKey.NORTH,
                TextureKey.SOUTH,
                TextureKey.WEST,
                TextureKey.EAST,
                TextureKey.PARTICLE);
    }

    public static Model createAllSidesWithOverlay(String parent) {
        return block(parent,
                TextureKey.DOWN,
                TextureKey.UP,
                TextureKey.NORTH,
                TextureKey.SOUTH,
                TextureKey.WEST,
                TextureKey.EAST,
                TextureKey.PARTICLE,
                ModTextureKey.DOWN_OVERLAY,
                ModTextureKey.UP_OVERLAY,
                ModTextureKey.NORTH_OVERLAY,
                ModTextureKey.SOUTH_OVERLAY,
                ModTextureKey.WEST_OVERLAY,
                ModTextureKey.EAST_OVERLAY);
    }

    public static Model createBottomTopSideWithOverlay(String parent) {
        return block(parent,
                TextureKey.BOTTOM,
                TextureKey.TOP,
                TextureKey.SIDE,
                TextureKey.PARTICLE,
                ModTextureKey.BOTTOM_OVERLAY,
                ModTextureKey.TOP_OVERLAY,
                ModTextureKey.SIDE_OVERLAY
        );
    }

    public static Model createBottomTopSide(String parent) {
        return block(parent,
                TextureKey.BOTTOM,
                TextureKey.TOP,
                TextureKey.SIDE,
                TextureKey.PARTICLE
        );
    }

    public static Model createLadderModel(String parent) {
        return block(parent,
                ModTextureKey.LADDER
        );
    }

    public static Model createBetterFoliageModel(String parent) {
        return block(parent,
                TextureKey.PARTICLE,
                TextureKey.ALL,
                ModTextureKey.LEAVES_OVERLAY_END,
                ModTextureKey.LEAVES_OVERLAY_SIDE
        );
    }

    public static Model createCuboidModel(String parent) {
        return block(parent,
                TextureKey.PARTICLE,
                ModTextureKey.TEXTURE_0,
                ModTextureKey.TEXTURE_1,
                ModTextureKey.TEXTURE_2,
                ModTextureKey.TEXTURE_3,
                ModTextureKey.TEXTURE_4,
                ModTextureKey.TEXTURE_5);
    }

    public static Model createRotatedCuboidModel(String parent, float rotation) {
        return new Model(Optional.of(Identifier.ofVanilla("block/block")),
                Optional.empty(),
                TextureKey.PARTICLE,
                ModTextureKey.TEXTURE_0,
                ModTextureKey.TEXTURE_1,
                ModTextureKey.TEXTURE_2,
                ModTextureKey.TEXTURE_3,
                ModTextureKey.TEXTURE_4,
                ModTextureKey.TEXTURE_5) {
            @Override
            public JsonObject createJson(Identifier id, Map<TextureKey, Identifier> textures) {
                JsonObject json = super.createJson(id, textures);

                // Add display settings
                JsonObject display = new JsonObject();

                JsonArray displayRotation = new JsonArray();
                displayRotation.add(0);
                displayRotation.add(90);
                displayRotation.add(0);
                display.add("rotation", displayRotation);

                JsonArray translation = new JsonArray();
                translation.add(0);
                translation.add(0);
                translation.add(0);
                display.add("translation", translation);

                JsonArray scale = new JsonArray();
                scale.add(0.5);
                scale.add(0.5);
                scale.add(0.5);
                display.add("scale", scale);

                json.add("display", display);

                // Create element
                JsonObject element = new JsonObject();

                // From coordinates (4,0,4)
                JsonArray from = new JsonArray();
                from.add(4.0);
                from.add(0.0);
                from.add(4.0);
                element.add("from", from);

                // To coordinates (12,8,12)
                JsonArray to = new JsonArray();
                to.add(12.0);
                to.add(8.0);
                to.add(12.0);
                element.add("to", to);

                // Add faces
                JsonObject faces = new JsonObject();

                // East face
                JsonObject eastFace = new JsonObject();
                eastFace.addProperty("texture", "#txt5");
                JsonArray eastUV = new JsonArray();
                eastUV.add(4.0);
                eastUV.add(8.0);
                eastUV.add(12.0);
                eastUV.add(16.0);
                eastFace.add("uv", eastUV);
                faces.add("east", eastFace);

                // South face
                JsonObject southFace = new JsonObject();
                southFace.addProperty("texture", "#txt3");
                JsonArray southUV = new JsonArray();
                southUV.add(4.0);
                southUV.add(8.0);
                southUV.add(12.0);
                southUV.add(16.0);
                southFace.add("uv", southUV);
                faces.add("south", southFace);

                // North face
                JsonObject northFace = new JsonObject();
                northFace.addProperty("texture", "#txt2");
                JsonArray northUV = new JsonArray();
                northUV.add(4.0);
                northUV.add(8.0);
                northUV.add(12.0);
                northUV.add(16.0);
                northFace.add("uv", northUV);
                faces.add("north", northFace);

                // West face
                JsonObject westFace = new JsonObject();
                westFace.addProperty("texture", "#txt4");
                JsonArray westUV = new JsonArray();
                westUV.add(4.0);
                westUV.add(8.0);
                westUV.add(12.0);
                westUV.add(16.0);
                westFace.add("uv", westUV);
                faces.add("west", westFace);

                // Up face
                JsonObject upFace = new JsonObject();
                upFace.addProperty("texture", "#txt1");
                JsonArray upUV = new JsonArray();
                upUV.add(4.0);
                upUV.add(4.0);
                upUV.add(12.0);
                upUV.add(12.0);
                upFace.add("uv", upUV);
                faces.add("up", upFace);

                // Down face
                JsonObject downFace = new JsonObject();
                downFace.addProperty("texture", "#txt0");
                downFace.addProperty("cullface", "down");
                JsonArray downUV = new JsonArray();
                downUV.add(4.0);
                downUV.add(4.0);
                downUV.add(12.0);
                downUV.add(12.0);
                downFace.add("uv", downUV);
                faces.add("down", downFace);

                element.add("faces", faces);

                // Add rotation if needed
                if (rotation != 0) {
                    JsonObject rotJson = new JsonObject();
                    rotJson.addProperty("angle", rotation);
                    rotJson.addProperty("axis", "y");
                    JsonArray originArray = new JsonArray();
                    originArray.add(8);
                    originArray.add(8);
                    originArray.add(8);
                    rotJson.add("origin", originArray);
                    element.add("rotation", rotJson);
                }

                // Create elements array and add our cube
                JsonArray elements = new JsonArray();
                elements.add(element);
                json.add("elements", elements);

                return json;
            }
        };
    }

    public static Model wcLayerModel(float yMax, boolean isTinted) {
        return new Model(
                Optional.empty(),
                Optional.empty(),
                TextureKey.PARTICLE,
                ModTextureKey.TEXTURE_0,
                ModTextureKey.TEXTURE_1,
                ModTextureKey.TEXTURE_2,
                ModTextureKey.TEXTURE_3,
                ModTextureKey.TEXTURE_4,
                ModTextureKey.TEXTURE_5) {
            @Override
            public JsonObject createJson(Identifier id, Map<TextureKey, Identifier> textures) {
                JsonObject json = super.createJson(id, textures);

                // Add elements array
                JsonArray elements = new JsonArray();
                JsonObject element = new JsonObject();

                // From coordinates (always start at 0)
                JsonArray from = new JsonArray();
                from.add(0);
                from.add(0);
                from.add(0);
                element.add("from", from);

                // To coordinates (height varies by layer)
                JsonArray to = new JsonArray();
                to.add(16);
                to.add(yMax);  // Variable height based on layer
                to.add(16);
                element.add("to", to);

                // Add faces
                JsonObject faces = new JsonObject();

                // Down face
                faces.add("down", createFace(0, 0, 16, 16, "#txt0", "down", isTinted));

                // Up face
                faces.add("up", createFace(0, 0, 16, 16, "#txt1", yMax >= 16 ? "up" : null, isTinted));

                // Side faces - UV coordinates are adjusted for height
                faces.add("north", createFace(0, 16 - yMax, 16, 16, "#txt2", "north", isTinted));
                faces.add("south", createFace(0, 16 - yMax, 16, 16, "#txt3", "south", isTinted));
                faces.add("west", createFace(0, 16 - yMax, 16, 16, "#txt4", "west", isTinted));
                faces.add("east", createFace(0, 16 - yMax, 16, 16, "#txt5", "east", isTinted));

                element.add("faces", faces);
                elements.add(element);
                json.add("elements", elements);

                return json;
            }

            private JsonObject createFace(float uMin, float vMin, float uMax, float vMax,
                                          String texture, String cullface, boolean tinted) {
                JsonObject face = new JsonObject();

                // UV coordinates
                JsonArray uv = new JsonArray();
                uv.add(uMin);
                uv.add(vMin);
                uv.add(uMax);
                uv.add(vMax);
                face.add("uv", uv);

                // Texture reference
                face.addProperty("texture", texture);

                // Cullface if specified
                if (cullface != null) {
                    face.addProperty("cullface", cullface);
                }

                // Tint index if tinted
                if (tinted) {
                    face.addProperty("tintindex", 0);
                }

                return face;
            }
        };
    }

    private static Model block(String parent, String namespace, TextureKey... requiredTextureKeys) {
        return new Model(Optional.of(Identifier.of(namespace != null ? namespace : WesterosBlocks.MOD_ID, GENERATED_PATH + parent)), Optional.empty(), requiredTextureKeys);
    }

    private static Model block(String parent, TextureKey... requiredTextureKeys) {
        return new Model(Optional.of(Identifier.of(WesterosBlocks.MOD_ID, GENERATED_PATH + parent)), Optional.empty(), requiredTextureKeys);
    }
}