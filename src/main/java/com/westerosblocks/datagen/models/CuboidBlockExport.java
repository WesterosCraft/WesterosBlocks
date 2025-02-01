package com.westerosblocks.datagen.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.WesterosBlockStateRecord;
import com.westerosblocks.block.custom.WCCuboidBlock;
import com.westerosblocks.datagen.ModelExport;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

import java.util.*;

public class CuboidBlockExport extends ModelExport {
    private final BlockStateModelGenerator generator;
    private final Block block;
    private final WesterosBlockDef def;
    private final WCCuboidBlock cuboidBlock;

    private static final int[] STANDARD_TEXTURE_INDICES = {0, 1, 2, 3, 4, 5};
    private static final boolean[] NO_TINT_ALL = {false, false, false, false, false, false};

    public CuboidBlockExport(BlockStateModelGenerator generator, Block block, WesterosBlockDef def) {
        super(generator, block, def);
        this.generator = generator;
        this.block = block;
        this.def = def;
        this.cuboidBlock = (WCCuboidBlock) block;
    }

    public void generateBlockStateModels() {
        BlockStateBuilder blockStateBuilder = new BlockStateBuilder(block);
        final Map<String, List<BlockStateVariant>> variants = blockStateBuilder.getVariants();

        for (WesterosBlockStateRecord sr : def.states) {
            boolean justBase = sr.stateID == null;
            Set<String> stateIDs = justBase ? null : Collections.singleton(sr.stateID);
            String baseName = justBase ? "base" : sr.stateID;

            for (int setIdx = 0; setIdx < sr.getRandomTextureSetCount(); setIdx++) {
                WesterosBlockDef.RandomTextureSet set = sr.getRandomTextureSet(setIdx);
                int rotationCount = sr.rotateRandom ? 4 : 1;

                if (!sr.isCustomModel()) {
                    generateCuboidModels(generator, sr, setIdx);
                }

                for (int i = 0; i < rotationCount; i++) {
                    BlockStateVariant variant = BlockStateVariant.create();
                    Identifier modelId = getModelId(baseName, setIdx, sr.isCustomModel());
                    variant.put(VariantSettings.MODEL, modelId);

                    if (set.weight != null) {
                        variant.put(VariantSettings.WEIGHT, set.weight);
                    }

                    int rotation = (90 * i + sr.rotYOffset) % 360;
                    if (rotation > 0) {
                        variant.put(VariantSettings.Y, getRotation(rotation));
                    }

                    blockStateBuilder.addVariant("", variant, stateIDs, variants);
                }
            }
        }

        generateBlockStateFiles(generator, block, variants);
    }

    public void generateCuboidModels(BlockStateModelGenerator generator, WesterosBlockStateRecord sr, int setIdx) {
        WesterosBlockDef.RandomTextureSet set = sr.getRandomTextureSet(setIdx);
        boolean isTinted = sr.isTinted();
        TextureMap textureMap = createCuboidTextureMap(set);

        Identifier modelId = getModelId(sr.stateID == null ? "base" : sr.stateID, setIdx, false);
        createCuboidModel(sr, isTinted, set.getTextureCount()).upload(modelId, textureMap, generator.modelCollector);
    }

    private Model createCuboidModel(WesterosBlockStateRecord sr, boolean isTinted, int textureCount) {
        // Ensure we have at least 6 texture keys for the standard faces
        int requiredTextures = Math.max(6, textureCount);

        List<TextureKey> textureKeys = new ArrayList<>();
        textureKeys.add(TextureKey.PARTICLE);

        for (int i = 0; i < requiredTextures; i++) {
            textureKeys.add(modTextureKeyForIndex(i));
        }

        return new Model(Optional.empty(), Optional.empty(), textureKeys.toArray(new TextureKey[0])) {
            @Override
            public JsonObject createJson(Identifier id, Map<TextureKey, Identifier> textures) {
                JsonObject json = super.createJson(id, textures);
                json.addProperty("parent", "block/block");

                // Add display settings
                JsonObject display = new JsonObject();

                // Add thirdperson settings
                JsonObject thirdperson = new JsonObject();
                JsonArray thirdRotation = new JsonArray();
                thirdRotation.add(0);
                thirdRotation.add(0);
                thirdRotation.add(0);
                thirdperson.add("rotation", thirdRotation);
                JsonArray thirdTranslation = new JsonArray();
                thirdTranslation.add(0);
                thirdTranslation.add(0);
                thirdTranslation.add(0);
                thirdperson.add("translation", thirdTranslation);
                JsonArray thirdScale = new JsonArray();
                thirdScale.add(0.375);
                thirdScale.add(0.375);
                thirdScale.add(0.375);
                thirdperson.add("scale", thirdScale);

                display.add("thirdperson_righthand", thirdperson);
                display.add("thirdperson_lefthand", thirdperson);

                // Add firstperson settings
                JsonObject firstperson = new JsonObject();
                JsonArray firstRotation = new JsonArray();
                firstRotation.add(0);
                firstRotation.add(-90);
                firstRotation.add(25);
                firstperson.add("rotation", firstRotation);
                JsonArray firstTranslation = new JsonArray();
                firstTranslation.add(0);
                firstTranslation.add(4);
                firstTranslation.add(2);
                firstperson.add("translation", firstTranslation);
                JsonArray firstScale = new JsonArray();
                firstScale.add(0.375);
                firstScale.add(0.375);
                firstScale.add(0.375);
                firstperson.add("scale", firstScale);

                display.add("firstperson_righthand", firstperson);
                display.add("firstperson_lefthand", firstperson);

                // Add gui settings
                JsonObject gui = new JsonObject();
                JsonArray guiRotation = new JsonArray();
                guiRotation.add(0);
                guiRotation.add(0);
                guiRotation.add(0);
                gui.add("rotation", guiRotation);
                JsonArray guiTranslation = new JsonArray();
                guiTranslation.add(0);
                guiTranslation.add(0);
                guiTranslation.add(0);
                gui.add("translation", guiTranslation);
                JsonArray guiScale = new JsonArray();
                guiScale.add(0.625);
                guiScale.add(0.625);
                guiScale.add(0.625);
                gui.add("scale", guiScale);

                display.add("gui", gui);

                // Add display object to json
                json.add("display", display);

                // Add elements array
                JsonArray elements = new JsonArray();

                for (WesterosBlockDef.Cuboid cuboid : cuboidBlock.getModelCuboids(def.states.indexOf(sr))) {
                    JsonObject element = new JsonObject();

                    // From coordinates
                    JsonArray from = new JsonArray();
                    from.add(getClamped(cuboid.xMin));
                    from.add(getClamped(cuboid.yMin));
                    from.add(getClamped(cuboid.zMin));
                    element.add("from", from);

                    // To coordinates
                    JsonArray to = new JsonArray();
                    to.add(getClamped(cuboid.xMax));
                    to.add(getClamped(cuboid.yMax));
                    to.add(getClamped(cuboid.zMax));
                    element.add("to", to);

                    // Add faces
                    JsonObject faces = new JsonObject();
                    addFaces(faces, cuboid, isTinted, textures);
                    element.add("faces", faces);

                    if ("crossed".equals(cuboid.shape)) {
                        element.addProperty("shade", false);
                        json.addProperty("ambientocclusion", false);
                    }

                    elements.add(element);
                }

                json.add("elements", elements);
                return json;
            }

            private void addFaces(JsonObject faces, WesterosBlockDef.Cuboid cuboid,
                                  boolean isTinted, Map<TextureKey, Identifier> textures) {
                int[] sidetxt = cuboid.sideTextures != null ? cuboid.sideTextures : STANDARD_TEXTURE_INDICES;
                boolean[] noTint = cuboid.noTint != null ? cuboid.noTint : NO_TINT_ALL;
                int[] siderot = cuboid.sideRotations != null ?
                        cuboid.sideRotations : new int[]{0, 0, 0, 0, 0, 0};

                // Add each face (down, up, north, south, west, east)
                addFace(faces, "down", 0, cuboid, sidetxt, noTint, siderot, isTinted, textures);
                addFace(faces, "up", 1, cuboid, sidetxt, noTint, siderot, isTinted, textures);
                addFace(faces, "north", 2, cuboid, sidetxt, noTint, siderot, isTinted, textures);
                addFace(faces, "south", 3, cuboid, sidetxt, noTint, siderot, isTinted, textures);
                addFace(faces, "west", 4, cuboid, sidetxt, noTint, siderot, isTinted, textures);
                addFace(faces, "east", 5, cuboid, sidetxt, noTint, siderot, isTinted, textures);
            }

            private void addFace(JsonObject faces, String face, int index,
                                 WesterosBlockDef.Cuboid cuboid, int[] sidetxt,
                                 boolean[] noTint, int[] siderot, boolean isTinted,
                                 Map<TextureKey, Identifier> textures) {
                JsonObject faceObj = new JsonObject();

                // Set UV coordinates based on face
                JsonArray uv = new JsonArray();
                calculateUVs(face, cuboid, uv);
                faceObj.add("uv", uv);

                // Get correct texture key
//                TextureKey textureKey = modTextureKeyForIndex(sidetxt[index]);

                // Add texture reference
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
        };
    }

    private static float getClamped(float v) {
        v = 16F * v;
        if (v < -16f) v = -16f;
        if (v > 32f) v = 32f;
        return v;
    }

    private void calculateUVs(String face, WesterosBlockDef.Cuboid cuboid, JsonArray uv) {
        // Calculate UV coordinates based on face and cuboid dimensions
        switch (face) {
            case "down" -> {
                uv.add(getClamped(cuboid.xMin));
                uv.add(16 - getClamped(cuboid.zMax));
                uv.add(getClamped(cuboid.xMax));
                uv.add(16 - getClamped(cuboid.zMin));
            }
            case "up" -> {
                uv.add(getClamped(cuboid.xMin));
                uv.add(getClamped(cuboid.zMin));
                uv.add(getClamped(cuboid.xMax));
                uv.add(getClamped(cuboid.zMax));
            }
            case "north" -> {
                uv.add(16 - getClamped(cuboid.xMax));
                uv.add(16 - getClamped(cuboid.yMax));
                uv.add(16 - getClamped(cuboid.xMin));
                uv.add(16 - getClamped(cuboid.yMin));
            }
            case "south" -> {
                uv.add(getClamped(cuboid.xMin));
                uv.add(16 - getClamped(cuboid.yMax));
                uv.add(getClamped(cuboid.xMax));
                uv.add(16 - getClamped(cuboid.yMin));
            }
            case "west" -> {
                uv.add(getClamped(cuboid.zMin));
                uv.add(16 - getClamped(cuboid.yMax));
                uv.add(getClamped(cuboid.zMax));
                uv.add(16 - getClamped(cuboid.yMin));
            }
            case "east" -> {
                uv.add(16 - getClamped(cuboid.zMax));
                uv.add(16 - getClamped(cuboid.yMax));
                uv.add(16 - getClamped(cuboid.zMin));
                uv.add(16 - getClamped(cuboid.yMin));
            }
        }
    }

    private String getCullface(String face, WesterosBlockDef.Cuboid cuboid) {
        return switch (face) {
            case "down" -> cuboid.yMin <= 0 ? "down" : null;
            case "up" -> cuboid.yMax >= 16 ? "up" : null;
            case "north" -> cuboid.zMin <= 0 ? "north" : null;
            case "south" -> cuboid.zMax >= 16 ? "south" : null;
            case "west" -> cuboid.xMin <= 0 ? "west" : null;
            case "east" -> cuboid.xMax >= 16 ? "east" : null;
            default -> null;
        };
    }

    public TextureMap createCuboidTextureMap(WesterosBlockDef.RandomTextureSet set) {
        TextureMap textureMap = new TextureMap();
        int textureCount = set.getTextureCount();

        // For blocks with single texture, we need to map it to all 6 sides
        int requiredTextures = Math.max(6, textureCount);

        for (int i = 0; i < requiredTextures; i++) {
            String texture = i < textureCount ?
                    set.getTextureByIndex(i) :
                    set.getTextureByIndex(0);

            textureMap.put(modTextureKeyForIndex(i), createBlockIdentifier(texture));
        }

        textureMap.put(TextureKey.PARTICLE, createBlockIdentifier(set.getTextureByIndex(0)));
        return textureMap;
    }

    public TextureKey modTextureKeyForIndex(int index) {
//        return ModTextureKey.getTextureNKey(index);
        return switch (index) {
            case 0 -> ModTextureKey.TEXTURE_0;
            case 1 -> ModTextureKey.TEXTURE_1;
            case 2 -> ModTextureKey.TEXTURE_2;
            case 3 -> ModTextureKey.TEXTURE_3;
            case 4 -> ModTextureKey.TEXTURE_4;
            case 5 -> ModTextureKey.TEXTURE_5;
            case 6 -> ModTextureKey.TEXTURE_6;
            case 7 -> ModTextureKey.TEXTURE_7;
            case 8 -> ModTextureKey.TEXTURE_8;
            case 9 -> ModTextureKey.TEXTURE_9;
            case 10 -> ModTextureKey.TEXTURE_10;
            case 11 -> ModTextureKey.TEXTURE_11;
            case 12 -> ModTextureKey.TEXTURE_12;
            case 13 -> ModTextureKey.TEXTURE_13;
            case 14 -> ModTextureKey.TEXTURE_14;
            case 15 -> ModTextureKey.TEXTURE_15;
            case 16 -> ModTextureKey.TEXTURE_16;
            case 17 -> ModTextureKey.TEXTURE_17;
            default -> throw new IllegalArgumentException("Invalid texture index: " + index);
        };
    }

    public Identifier getModelId(String variant, int setIdx, boolean isCustom) {
        return WesterosBlocks.id(String.format("%s%s/%s_v%d",
                isCustom ? CUSTOM_PATH : GENERATED_PATH,
                def.getBlockName(),
                variant,
                setIdx + 1));
    }

    private boolean isTransparent(String texture) {
        return texture.equals("transparent");
    }

    public static void generateItemModels(ItemModelGenerator itemModelGenerator, Block block, WesterosBlockDef blockDefinition) {
        WesterosBlockStateRecord firstState = blockDefinition.states.getFirst();
        String baseName = firstState.stateID == null ? "base" : firstState.stateID;
        String pathPrefix = firstState.isCustomModel() ? CUSTOM_PATH : GENERATED_PATH;
        String path = String.format("%s%s/%s_v1", pathPrefix, blockDefinition.getBlockName(), baseName);

        itemModelGenerator.register(
                block.asItem(),
                new Model(Optional.of(WesterosBlocks.id(path)), Optional.empty())
        );
    }
}