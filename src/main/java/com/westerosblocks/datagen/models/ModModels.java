package com.westerosblocks.datagen.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.ModBlock;
import com.westerosblocks.datagen.ModelExport;
import net.minecraft.data.client.Model;
import net.minecraft.data.client.Models;
import net.minecraft.data.client.TextureKey;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.Optional;

public class ModModels extends Models {

    public static Model ALL_SIDES(String namespace, String parent, ModBlock def) {
        return block(parent,
                namespace,
                def,
                TextureKey.DOWN,
                TextureKey.UP,
                TextureKey.NORTH,
                TextureKey.SOUTH,
                TextureKey.WEST,
                TextureKey.EAST,
                TextureKey.PARTICLE);
    }

    public static Model ALL_SIDES_OVERLAY(String parent, ModBlock def) {
        return block(parent,
                def,
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

    public static Model BOTTOM_TOP_SIDE_OVERLAY(String parent, ModBlock def) {
        return block(parent,
                def,
                TextureKey.BOTTOM,
                TextureKey.TOP,
                TextureKey.SIDE,
                TextureKey.PARTICLE,
                ModTextureKey.BOTTOM_OVERLAY,
                ModTextureKey.TOP_OVERLAY,
                ModTextureKey.SIDE_OVERLAY
        );
    }

    public static Model BOTTOM_TOP_SIDE(String parent, ModBlock def) {
        return block(parent,
                def,
                TextureKey.BOTTOM,
                TextureKey.TOP,
                TextureKey.SIDE,
                TextureKey.PARTICLE
        );
    }

    public static Model WC_LADDER(String parent, ModBlock def) {
        return block(parent,
                def,
                ModTextureKey.LADDER
        );
    }

    public static Model BETTER_FOLIAGE(String parent, ModBlock def) {
        return block(parent,
                def,
                TextureKey.PARTICLE,
                TextureKey.ALL,
                ModTextureKey.LEAVES_OVERLAY_END,
                ModTextureKey.LEAVES_OVERLAY_SIDE
        );
    }

    public static Model CUBOID(String parent, ModBlock def) {
        return block(parent,
                def,
                TextureKey.PARTICLE,
                ModTextureKey.TEXTURE_0,
                ModTextureKey.TEXTURE_1,
                ModTextureKey.TEXTURE_2,
                ModTextureKey.TEXTURE_3,
                ModTextureKey.TEXTURE_4,
                ModTextureKey.TEXTURE_5);
    }

    public static Model ROTATED_CUBOID(String parent, float rotation) {
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

    public static Model WC_LAYER(float yMax, boolean isTinted) {
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

    public static Model CUBOID_NSEW_STACK() {
        return new Model(
                Optional.of(Identifier.ofVanilla("block/block")),
                Optional.empty(),
                ModTextureKey.TEXTURE_4,
                ModTextureKey.TEXTURE_5,
                TextureKey.PARTICLE
        ) {
            @Override
            public JsonObject createJson(Identifier id, Map<TextureKey, Identifier> textures) {
                JsonObject json = super.createJson(id, textures);

                // Add display settings
                JsonObject display = new JsonObject();
                JsonArray rotation = new JsonArray();
                rotation.add(0);
                rotation.add(90);
                rotation.add(0);
                display.add("rotation", rotation);

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

                // add gui object
                JsonObject gui = new JsonObject();
                JsonArray guirotation = new JsonArray();
                guirotation.add(0);
                guirotation.add(90);
                guirotation.add(0);
                gui.add("rotation", guirotation);

                JsonArray guitranslation = new JsonArray();
                guitranslation.add(0);
                guitranslation.add(0);
                guitranslation.add(0);
                gui.add("translation", guitranslation);

                JsonArray guiscale = new JsonArray();
                guiscale.add(0.825);
                guiscale.add(0.825);
                guiscale.add(0.825);
                gui.add("scale", guiscale);

                display.add("gui", gui);

                json.add("display", display);

                // Add elements array
                JsonArray elements = new JsonArray();
                JsonObject element = new JsonObject();

                JsonArray from = new JsonArray();
                from.add(15.5);
                from.add(0.0);
                from.add(0.0);
                element.add("from", from);

                JsonArray to = new JsonArray();
                to.add(16.0);
                to.add(16.0);
                to.add(16.0);
                element.add("to", to);

                // Add faces
                JsonObject faces = new JsonObject();

                // East face
                JsonObject eastFace = new JsonObject();
                JsonArray eastUV = new JsonArray();
                eastUV.add(0.0);
                eastUV.add(0.0);
                eastUV.add(16.0);
                eastUV.add(16.0);
                eastFace.add("uv", eastUV);
                eastFace.addProperty("texture", "#txt5");
                eastFace.addProperty("cullface", "east");
                faces.add("east", eastFace);

                // West face
                JsonObject westFace = new JsonObject();
                JsonArray westUV = new JsonArray();
                westUV.add(0.0);
                westUV.add(0.0);
                westUV.add(16.0);
                westUV.add(16.0);
                westFace.add("uv", westUV);
                westFace.addProperty("texture", "#txt4");
                faces.add("west", westFace);

                element.add("faces", faces);
                elements.add(element);
                json.add("elements", elements);

                return json;
            }
        };
    }

    public static Model BED_PART(String parent, ModBlock def) {
        return block(parent,
                def,
                ModTextureKey.BED_TOP,
                ModTextureKey.BED_END,
                ModTextureKey.BED_SIDE,
                TextureKey.PARTICLE
        );
    }

    public static Model BED_ITEM(String parent) {
        return item(parent,
                ModTextureKey.BED_TOP,
                ModTextureKey.BED_END,
                ModTextureKey.BED_SIDE,
                ModTextureKey.BED_TOP2,
                ModTextureKey.BED_END2,
                ModTextureKey.BED_SIDE2);
    }

    public static Model CAKE(int bites) {
        return new Model(
                Optional.of(Identifier.ofVanilla("block/thin_block")),
                Optional.empty(),
                TextureKey.PARTICLE,
                TextureKey.BOTTOM,
                TextureKey.TOP,
                TextureKey.SIDE,
                ModTextureKey.INSIDE
        ) {
            @Override
            public JsonObject createJson(Identifier id, Map<TextureKey, Identifier> textures) {
                JsonObject json = super.createJson(id, textures);

                // Create elements array
                JsonArray elements = new JsonArray();
                JsonObject element = new JsonObject();

                // Set dimensions based on bite state
                int xMin = 1 + (2 * bites);
                JsonArray from = new JsonArray();
                from.add(xMin);
                from.add(0);
                from.add(1);
                element.add("from", from);

                JsonArray to = new JsonArray();
                to.add(15);
                to.add(8);
                to.add(15);
                element.add("to", to);

                // Add faces
                JsonObject faces = new JsonObject();

                // Bottom face
                JsonObject bottomFace = new JsonObject();
                bottomFace.addProperty("texture", "#bottom");
                bottomFace.addProperty("cullface", "down");
                faces.add("down", bottomFace);

                // Top face
                JsonObject topFace = new JsonObject();
                topFace.addProperty("texture", "#top");
                faces.add("up", topFace);

                // Side faces
                JsonObject northFace = new JsonObject();
                northFace.addProperty("texture", "#side");
                faces.add("north", northFace);

                JsonObject southFace = new JsonObject();
                southFace.addProperty("texture", "#side");
                faces.add("south", southFace);

                // West face (shows inside texture when bitten)
                JsonObject westFace = new JsonObject();
                westFace.addProperty("texture", bites == 0 ? "#side" : "#inside");
                faces.add("west", westFace);

                // East face
                JsonObject eastFace = new JsonObject();
                eastFace.addProperty("texture", "#side");
                faces.add("east", eastFace);

                element.add("faces", faces);
                elements.add(element);
                json.add("elements", elements);

                return json;
            }
        };
    }

    public static Model LEAVES_OVERLAY(String parent, ModBlock def){
        return block(parent,
                def,
                TextureKey.END,
                TextureKey.SIDE,
                ModTextureKey.LEAVES_OVERLAY_END,
                ModTextureKey.LEAVES_OVERLAY_SIDE,
                TextureKey.PARTICLE
        );
    }

    private static void addDisplayToJson(JsonObject json, ModBlock.DisplayProperties display) {
        // blockDef can be directly from ModelExport.def
//        WesterosBlocks.LOGGER.info("Adding display to JSON for block: {} with display: {}", def.blockName, def.display);

        if (display != null && display.gui != null) {
            JsonObject genDisplay = json.has("display") ?
                    json.getAsJsonObject("display") : new JsonObject();
            JsonObject gui = new JsonObject();

            if (display.gui.rotation != null) {
                JsonArray rotation = new JsonArray();
                for (float v : display.gui.rotation) {
                    rotation.add(v);
                }
                gui.add("rotation", rotation);
            }

            if (display.gui.scale != null) {
                JsonArray scale = new JsonArray();
                for (float v : display.gui.scale) {
                    scale.add(v);
                }
                gui.add("scale", scale);
            }

            if (display.gui.translation != null) {
                JsonArray translation = new JsonArray();
                for (float v : display.gui.translation) {
                    translation.add(v);
                }
                gui.add("translation", translation);
            }

            genDisplay.add("gui", gui);
            json.add("display", genDisplay);
        }
    }



    private static Model block(String parent, String namespace, ModBlock def, TextureKey... requiredTextureKeys) {

        return new Model(Optional.of(Identifier.of(namespace != null ? namespace : WesterosBlocks.MOD_ID, "block/" + parent)), Optional.empty(), requiredTextureKeys) {
            @Override
            public JsonObject createJson(Identifier id, Map<TextureKey, Identifier> textures) {
                JsonObject json = super.createJson(id, textures);
                addDisplayToJson(json, def.display);
                return json;
            }
        };
    }

    private static Model block(String parent, ModBlock def, TextureKey... requiredTextureKeys) {
        return block(parent, WesterosBlocks.MOD_ID,def, requiredTextureKeys);
    }

    private static Model item(String parent, TextureKey... requiredTextureKeys) {
        return new Model(Optional.of(Identifier.of(WesterosBlocks.MOD_ID, "item/" + parent)), Optional.empty(), requiredTextureKeys);
    }
}