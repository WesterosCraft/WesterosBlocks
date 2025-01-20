package com.westerosblocks.datagen.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.datagen.ModelExport;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class HalfDoorBlockModelHandler extends ModelExport {
    private final BlockStateModelGenerator generator;
    private final Block block;
    private final WesterosBlockDef def;

    private static class ModelRec {
        String cond;
        String ext;
        int y;
        ModelRec(String c, String e, int y) {
            cond = c; ext = e; this.y = y;
        }
    }

    // Model definitions for half door variants
    private static final ModelRec[] MODELS = {
            // EAST facing
            new ModelRec("facing=east,hinge=left,open=false", "bottom_left", 0),
            new ModelRec("facing=east,hinge=left,open=true", "bottom_left_open", 90),
            new ModelRec("facing=east,hinge=right,open=false", "bottom_right", 0),
            new ModelRec("facing=east,hinge=right,open=true", "bottom_right_open", 270),

            // SOUTH facing
            new ModelRec("facing=south,hinge=left,open=false", "bottom_left", 90),
            new ModelRec("facing=south,hinge=left,open=true", "bottom_left_open", 180),
            new ModelRec("facing=south,hinge=right,open=false", "bottom_right", 90),
            new ModelRec("facing=south,hinge=right,open=true", "bottom_right_open", 0),

            // WEST facing
            new ModelRec("facing=west,hinge=left,open=false", "bottom_left", 180),
            new ModelRec("facing=west,hinge=left,open=true", "bottom_left_open", 270),
            new ModelRec("facing=west,hinge=right,open=false", "bottom_right", 180),
            new ModelRec("facing=west,hinge=right,open=true", "bottom_right_open", 90),

            // NORTH facing
            new ModelRec("facing=north,hinge=left,open=false", "bottom_left", 270),
            new ModelRec("facing=north,hinge=left,open=true", "bottom_left_open", 0),
            new ModelRec("facing=north,hinge=right,open=false", "bottom_right", 270),
            new ModelRec("facing=north,hinge=right,open=true", "bottom_right_open", 180)
    };

    public HalfDoorBlockModelHandler(BlockStateModelGenerator generator, Block block, WesterosBlockDef def) {
        super(generator, block, def);
        this.generator = generator;
        this.block = block;
        this.def = def;
    }

    public void generateBlockStateModels() {
        if (!def.isCustomModel()) {
            for (int setIdx = 0; setIdx < def.getRandomTextureSetCount(); setIdx++) {
                WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setIdx);
                generateHalfDoorModels(generator, set);
            }
        }

        BlockStateBuilder blockStateBuilder = new BlockStateBuilder(block);
        final Map<String, List<BlockStateVariant>> variants = blockStateBuilder.getVariants();

        for (ModelRec rec : MODELS) {
            BlockStateVariant variant = BlockStateVariant.create();
            Identifier modelId = getModelId(rec.ext, def.isCustomModel());
            variant.put(VariantSettings.MODEL, modelId);

            if (rec.y != 0) {
                variant.put(VariantSettings.Y, getRotation(rec.y));
            }

            blockStateBuilder.addVariant(rec.cond, variant, null, variants);
        }

        generateBlockStateFiles(generator, block, variants);
    }

    private void generateHalfDoorModels(BlockStateModelGenerator generator, WesterosBlockDef.RandomTextureSet set) {
        TextureMap textureMap = new TextureMap()
                .put(TextureKey.TOP, createBlockIdentifier(set.getTextureByIndex(0)))
                .put(TextureKey.BOTTOM, createBlockIdentifier(set.getTextureByIndex(1)));

        // Generate models for each door variant
        String[][] variants = {
                {"bottom_left", "door_bottom_left"},
                {"bottom_right", "door_bottom_right"},
                {"bottom_left_open", "door_bottom_left_open"},
                {"bottom_right_open", "door_bottom_right_open"}
        };



        for (String[] variant : variants) {
            String modelName = variant[0];
            String parentPath = "block/" + variant[1];

            Identifier modelId = getModelId(modelName, false);
            Model doorModel = new Model(
                    Optional.of(Identifier.ofVanilla(parentPath)),
                    Optional.empty(),
                    TextureKey.TOP,
                    TextureKey.BOTTOM
            );
            doorModel.upload(modelId, textureMap, generator.modelCollector);
        }
    }

    private Identifier getModelId(String variant, boolean isCustom) {
        return Identifier.of(WesterosBlocks.MOD_ID,
                String.format("%s%s/%s_v1",
                        isCustom ? CUSTOM_PATH : GENERATED_PATH,
                        def.getBlockName(),
                        variant));
    }

    public static void generateItemModels(ItemModelGenerator itemModelGenerator,
                                          Block block,
                                          WesterosBlockDef blockDefinition) {
        WesterosBlockDef.RandomTextureSet firstSet = blockDefinition.getRandomTextureSet(0);
        TextureMap textureMap = TextureMap.layer0(createBlockIdentifier(firstSet.getTextureByIndex(0)));

        Models.GENERATED.upload(
                ModelIds.getItemModelId(block.asItem()),
                textureMap,
                itemModelGenerator.writer
        );
    }
}


//public class HalfDoorBlockModelHandler extends ModelExport {
//    private final BlockStateModelGenerator generator;
//    private final Block block;
//    private final WesterosBlockDef def;
//
//    private static class ModelRec {
//        String cond;
//        String ext;
//        int y;
//        ModelRec(String c, String e, int y) {
//            cond = c; ext = e; this.y = y;
//        }
//    }
//
//    private static final ModelRec[] MODELS = {
//            // East facing
//            new ModelRec("facing=east,hinge=right,open=true", "base", 270),
//            new ModelRec("facing=east,hinge=left,open=true", "rh", 90),
//            new ModelRec("facing=east,hinge=left,open=false", "base", 0),
//            new ModelRec("facing=east,hinge=right,open=false", "rh", 0),
//
//            // West facing
//            new ModelRec("facing=west,hinge=right,open=true", "base", 90),
//            new ModelRec("facing=west,hinge=left,open=true", "rh", 270),
//            new ModelRec("facing=west,hinge=right,open=false", "rh", 180),
//            new ModelRec("facing=west,hinge=left,open=false", "base", 180),
//
//            // North facing
//            new ModelRec("facing=north,hinge=right,open=true", "base", 180),
//            new ModelRec("facing=north,hinge=left,open=true", "rh", 0),
//            new ModelRec("facing=north,hinge=right,open=false", "rh", 270),
//            new ModelRec("facing=north,hinge=left,open=false", "base", 270),
//
//            // South facing
//            new ModelRec("facing=south,hinge=right,open=true", "base", 0),
//            new ModelRec("facing=south,hinge=left,open=true", "rh", 180),
//            new ModelRec("facing=south,hinge=right,open=false", "rh", 90),
//            new ModelRec("facing=south,hinge=left,open=false", "base", 90)
//    };
//
//    public HalfDoorBlockModelHandler(BlockStateModelGenerator generator, Block block, WesterosBlockDef def) {
//        super(generator, block, def);
//        this.generator = generator;
//        this.block = block;
//        this.def = def;
//    }
//
//    public void generateBlockStateModels() {
//        if (!def.isCustomModel()) {
//            for (int setIdx = 0; setIdx < def.getRandomTextureSetCount(); setIdx++) {
//                WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setIdx);
//                generateHalfDoorModels(generator, set);
//            }
//        }
//
//        BlockStateBuilder blockStateBuilder = new BlockStateBuilder(block);
//        final Map<String, List<BlockStateVariant>> variants = blockStateBuilder.getVariants();
//
//        for (ModelRec rec : MODELS) {
//            BlockStateVariant variant = BlockStateVariant.create();
//            Identifier modelId = getModelId(rec.ext, def.isCustomModel());
//            variant.put(VariantSettings.MODEL, modelId);
//
//            if (rec.y != 0) {
//                variant.put(VariantSettings.Y, getRotation(rec.y));
//            }
//
//            blockStateBuilder.addVariant(rec.cond, variant, null, variants);
//        }
//
//        generateBlockStateFiles(generator, block, variants);
//    }
//
//    private void generateHalfDoorModels(BlockStateModelGenerator generator, WesterosBlockDef.RandomTextureSet set) {
//        // Create transforms object for base model
//        JsonObject baseTransforms = new JsonObject();
//
//        // Third person transform
//        JsonObject thirdPerson = new JsonObject();
//        JsonArray thirdRotation = new JsonArray();
//        thirdRotation.add(0);
//        thirdRotation.add(0);
//        thirdRotation.add(0);
//        thirdPerson.add("rotation", thirdRotation);
//
//        JsonArray thirdTranslation = new JsonArray();
//        thirdTranslation.add(0);
//        thirdTranslation.add(0);
//        thirdTranslation.add(0);
//        thirdPerson.add("translation", thirdTranslation);
//
//        JsonArray thirdScale = new JsonArray();
//        thirdScale.add(0.375);
//        thirdScale.add(0.375);
//        thirdScale.add(0.375);
//        thirdPerson.add("scale", thirdScale);
//
//        baseTransforms.add("thirdperson_righthand", thirdPerson);
//        baseTransforms.add("thirdperson_lefthand", thirdPerson);
//
//        // First person transform
//        JsonObject firstPerson = new JsonObject();
//        JsonArray firstRotation = new JsonArray();
//        firstRotation.add(0);
//        firstRotation.add(-90);
//        firstRotation.add(25);
//        firstPerson.add("rotation", firstRotation);
//
//        JsonArray firstTranslation = new JsonArray();
//        firstTranslation.add(0);
//        firstTranslation.add(4);
//        firstTranslation.add(2);
//        firstPerson.add("translation", firstTranslation);
//
//        JsonArray firstScale = new JsonArray();
//        firstScale.add(0.375);
//        firstScale.add(0.375);
//        firstScale.add(0.375);
//        firstPerson.add("scale", firstScale);
//
//        baseTransforms.add("firstperson_righthand", firstPerson);
//        baseTransforms.add("firstperson_lefthand", firstPerson);
//
//        // GUI transform
//        JsonObject gui = new JsonObject();
//        JsonArray guiRotation = new JsonArray();
//        guiRotation.add(0);
//        guiRotation.add(0);
//        guiRotation.add(0);
//        gui.add("rotation", guiRotation);
//
//        JsonArray guiTranslation = new JsonArray();
//        guiTranslation.add(0);
//        guiTranslation.add(0);
//        guiTranslation.add(0);
//        gui.add("translation", guiTranslation);
//
//        JsonArray guiScale = new JsonArray();
//        guiScale.add(0.625);
//        guiScale.add(0.625);
//        guiScale.add(0.625);
//        gui.add("scale", guiScale);
//
//        baseTransforms.add("gui", gui);
//
//        TextureMap textureMap = new TextureMap()
//                .put(TextureKey.TOP, createBlockIdentifier(set.getTextureByIndex(0)))
//                .put(TextureKey.BOTTOM, createBlockIdentifier(set.getTextureByIndex(1)));
//
//        // Generate base and right-hand models
//        String[][] variants = {
//                {"base", "untinted/half_door"},      // Base model
//                {"rh", "untinted/half_door_rh"}      // Right-hand model
//        };
//
//        for (String[] variant : variants) {
//            String modelName = variant[0];
//            String parentPath = "block/" + variant[1];
//
//            Identifier modelId = getModelId(modelName, false);
//            Model doorModel = new Model(
//                    Optional.of(Identifier.of(WesterosBlocks.MOD_ID, parentPath)),
//                    Optional.empty(),
//                    TextureKey.TOP,
//                    TextureKey.BOTTOM
//            ) {
//                @Override
//                public JsonObject createJson(Identifier id, Map<TextureKey, Identifier> textures) {
//                    JsonObject json = super.createJson(id, textures);
//                    if (modelName.equals("base")) {
//                        json.add("display", baseTransforms);
//                    }
//                    return json;
//                }
//            };
//            doorModel.upload(modelId, textureMap, generator.modelCollector);
//        }
//    }
//
//    private Identifier getModelId(String variant, boolean isCustom) {
//        return Identifier.of(WesterosBlocks.MOD_ID,
//                String.format("%s%s/%s_v1",
//                        isCustom ? CUSTOM_PATH : GENERATED_PATH,
//                        def.getBlockName(),
//                        variant));
//    }
//
//    public static void generateItemModels(ItemModelGenerator itemModelGenerator,
//                                          Block block,
//                                          WesterosBlockDef blockDefinition) {
//        WesterosBlockDef.RandomTextureSet firstSet = blockDefinition.getRandomTextureSet(0);
//        TextureMap textureMap = TextureMap.layer0(createBlockIdentifier(firstSet.getTextureByIndex(0)));
//
//        Models.GENERATED.upload(
//                ModelIds.getItemModelId(block.asItem()),
//                textureMap,
//                itemModelGenerator.writer
//        );
//    }
//}