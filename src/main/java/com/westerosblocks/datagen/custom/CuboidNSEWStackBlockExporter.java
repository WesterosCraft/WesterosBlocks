package com.westerosblocks.datagen.custom;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.westerosblocks.datagen.ModModels;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;
import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.datagen.ModTextureKey;
import com.westerosblocks.block.custom.WCCuboidNSEWStackBlock;
import com.westerosblocks.data.BlockDefinition;

import java.util.*;


public class CuboidNSEWStackBlockExporter extends BaseBlockExporter {
    public static void registerCustomCuboidNSEWStackBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        if (!(block instanceof WCCuboidNSEWStackBlock stackBlock)) {
            throw new IllegalArgumentException("Block must be a WCCuboidNSEWStackBlock instance");
        }

        if (!definition.hasStackElements() || definition.getStackElements().size() < 2) {
            throw new IllegalArgumentException("CuboidNSEWStackBlock requires at least 2 stack elements");
        }

        // Get bottom and top stack elements
        BlockDefinition.StackElement bottomElement = definition.getStackElements().get(0);
        BlockDefinition.StackElement topElement = definition.getStackElements().get(1);

        // Generate models for bottom and top halves
        Identifier bottomModelId;
        Identifier topModelId;

        if (definition.hasCustomModel()) {
            // Use custom model files
            bottomModelId = createCustomModelId(block, "base_v1");
            topModelId = createCustomModelId(block, "top_v1");
        } else {
            // Generate models from stack elements
            bottomModelId = createStackElementModel(generator, block, definition, bottomElement, "base_v1");
            topModelId = createStackElementModel(generator, block, definition, topElement, "top_v1");
        }

        // Create blockstate with facing and half variants
        VariantsBlockStateSupplier blockStateSupplier = VariantsBlockStateSupplier.create(block);

        // Bottom half variants (facing=north,east,south,west with half=lower)
        blockStateSupplier.coordinate(
            BlockStateVariantMap.create(
                WCCuboidNSEWStackBlock.FACING,
                WCCuboidNSEWStackBlock.HALF
            )
            .register((facing, half) -> {
                if (half.asString().equals("lower")) {
                    // Bottom half
                    int rotation = switch (facing.asString()) {
                        case "north" -> 270;
                        case "east" -> 0;
                        case "south" -> 90;
                        case "west" -> 180;
                        default -> 0;
                    };
                    return createVariant(bottomModelId, rotation);
                } else {
                    // Top half
                    int rotation = switch (facing.asString()) {
                        case "north" -> 270;
                        case "east" -> 0;
                        case "south" -> 90;
                        case "west" -> 180;
                        default -> 0;
                    };
                    return createVariant(topModelId, rotation);
                }
            })
        );

        generator.blockStateCollector.accept(blockStateSupplier);

        // Register item model using bottom model
//        registerParentedItemModel(generator, block, bottomModelId);
        TextureMap itemTextureMap = new TextureMap()
                .put(ModTextureKey.TEXTURE_1, createBlockIdentifier(topElement.getTextures().get(4)))
                .put(ModTextureKey.TEXTURE_2, createBlockIdentifier(bottomElement.getTextures().get(4)))
                .put(ModTextureKey.TEXTURE_5, createBlockIdentifier(topElement.getTextures().get(5)))
                .put(TextureKey.PARTICLE, createBlockIdentifier(topElement.getTextures().get(4)));
        Identifier itemModelId = WesterosBlocks.id("item/" + definition.getBlockName());
        Model combinedItemModel = ModModels.CUBOID_NSEW_STACK_ITEM();
        combinedItemModel.upload(itemModelId, itemTextureMap, generator.modelCollector);
    }

    /**
     * Creates a model for a stack element.
     */
    private static Identifier createStackElementModel(BlockStateModelGenerator generator, Block block,
                                                      BlockDefinition definition, BlockDefinition.StackElement element,
                                                      String variant) {
        List<String> textures = element.getTextures();

        if (textures == null || textures.isEmpty()) {
            // Fallback to default texture
            textures = List.of("missing");
        }

        // Always create cuboid model with bounding box from stack element
        return createCuboidStackModel(generator, block, element, textures, variant);
    }

    /**
     * Creates a cuboid model for a stack element using custom geometry.
     */
    private static Identifier createCuboidStackModel(BlockStateModelGenerator generator, Block block,
                                                     BlockDefinition.StackElement element, List<String> textures, String variant) {
        Identifier modelId = createGeneratedModelId(block, variant);

        // Create custom model JSON with elements
        JsonObject modelJson = new JsonObject();
        modelJson.addProperty("parent", "minecraft:block/block");

        // Find first non-transparent texture for particle
        String particleTexture = textures.stream()
                .filter(t -> t != null && !t.equals("transparent"))
                .findFirst()
                .orElse(textures.get(0));

        // Add textures - only the ones that are not transparent
        JsonObject texturesJson = new JsonObject();
        texturesJson.addProperty("particle", "westerosblocks:block/" + particleTexture);

        // Add texture mappings for txt4 and txt5 (west and east faces)
        if (textures.size() >= 5) {
            texturesJson.addProperty("txt4", "westerosblocks:block/" + textures.get(4));
        }
        if (textures.size() >= 6) {
            texturesJson.addProperty("txt5", "westerosblocks:block/" + textures.get(5));
        }

        modelJson.add("textures", texturesJson);

        // Create single cuboid element from bounding box
        if (element.hasBoundingBox()) {
            JsonArray elementsArray = new JsonArray();
            JsonObject elementJson = createStackElementJson(element, textures);
            elementsArray.add(elementJson);
            modelJson.add("elements", elementsArray);
        }

        // Add display settings
        JsonObject display = new JsonObject();

        // GUI display
        JsonObject guiDisplay = new JsonObject();
        JsonArray guiRotation = new JsonArray();
        guiRotation.add(0);
        guiRotation.add(90);
        guiRotation.add(0);
        guiDisplay.add("rotation", guiRotation);

        JsonArray guiScale = new JsonArray();
        guiScale.add(0.825);
        guiScale.add(0.825);
        guiScale.add(0.825);
        guiDisplay.add("scale", guiScale);

        JsonArray guiTranslation = new JsonArray();
        guiTranslation.add(0);
        guiTranslation.add(0);
        guiTranslation.add(0);
        guiDisplay.add("translation", guiTranslation);

        display.add("gui", guiDisplay);
        modelJson.add("display", display);

        // Upload the model
        generator.modelCollector.accept(modelId, () -> modelJson);

        return modelId;
    }

    /**
     * Creates a JSON element for a stack element.
     */
    private static JsonObject createStackElementJson(BlockDefinition.StackElement element, List<String> textures) {
        BlockDefinition.BoundingBox bbox = element.getBoundingBox();
        JsonObject elementJson = new JsonObject();

        // Add from/to coordinates (convert from 0-1 range to 0-16 pixel range)
        JsonArray from = new JsonArray();
        from.add(bbox.getXMin() * 16);
        from.add(bbox.getYMin() * 16);
        from.add(bbox.getZMin() * 16);
        elementJson.add("from", from);

        JsonArray to = new JsonArray();
        to.add(bbox.getXMax() * 16);
        to.add(bbox.getYMax() * 16);
        to.add(bbox.getZMax() * 16);
        elementJson.add("to", to);

        // Add faces - only west and east for banner-like blocks
        JsonObject faces = new JsonObject();

        // West face (txt4 - index 4 in textures array)
        if (textures.size() >= 5 && !textures.get(4).equals("transparent")) {
            JsonObject westFace = new JsonObject();
            westFace.addProperty("texture", "#txt4");
            JsonArray westUv = new JsonArray();
            westUv.add(0.0);
            westUv.add(0.0);
            westUv.add(16.0);
            westUv.add(16.0);
            westFace.add("uv", westUv);
            faces.add("west", westFace);
        }

        // East face (txt5 - index 5 in textures array) with cullface
        if (textures.size() >= 6 && !textures.get(5).equals("transparent")) {
            JsonObject eastFace = new JsonObject();
            eastFace.addProperty("cullface", "east");
            eastFace.addProperty("texture", "#txt5");
            JsonArray eastUv = new JsonArray();
            eastUv.add(0.0);
            eastUv.add(0.0);
            eastUv.add(16.0);
            eastUv.add(16.0);
            eastFace.add("uv", eastUv);
            faces.add("east", eastFace);
        }

        elementJson.add("faces", faces);

        return elementJson;
    }

    /**
     * Creates a custom model identifier.
     */
    private static Identifier createCustomModelId(Block block, String variant) {
        String blockName = getBlockName(block);
        return WesterosBlocks.id("block/custom/" + blockName + "/" + variant);
    }

    /**
     * Creates a generated model identifier.
     */
    private static Identifier createGeneratedModelId(Block block, String variant) {
        String blockName = getBlockName(block);
        return WesterosBlocks.id("block/" + blockName + "/" + variant);
    }
}