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