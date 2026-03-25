package com.westerosblocks.datagen.custom;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.westerosblocks.data.BlockDefinition;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import com.westerosblocks.WesterosBlocks;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ParticleEmitterExporter extends BaseBlockExporter {

    public static void registerCustomParticleEmitterBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        Identifier modelId = createNestedModelId(block, "on");

        // Create model with custom element (4x4x4 cube centered at bottom)
        Identifier textureId = WesterosBlocks.id("block/particle_emitter/on");
        uploadParticleEmitterModel(modelId, textureId, 6, 0, 6, 10, 4, 10, generator.modelCollector);

        generator.blockStateCollector.accept(BlockStateModelGenerator.createSingletonBlockState(block, modelId));

        if (definition.hasCustomItemTexture()) {
            String blockName = getBlockName(block);
            Identifier itemTextureId = Identifier.of("westerosblocks", "item/" + blockName);
            registerSimpleItemModel(generator, block, itemTextureId);
        } else if (definition.hasItemTexture()) {
            Identifier itemTextureId = createBlockIdentifier(definition.getItemTexture());
            registerSimpleItemModel(generator, block, itemTextureId);
        } else {
            generator.registerParentedItemModel(block, modelId);
        }
    }

    private static void uploadParticleEmitterModel(Identifier modelId, Identifier textureId,
                                                   double fromX, double fromY, double fromZ,
                                                   double toX, double toY, double toZ,
                                                   BiConsumer<Identifier, Supplier<JsonElement>> modelCollector) {
        JsonObject modelJson = new JsonObject();

        // Add textures
        JsonObject textures = new JsonObject();
        textures.addProperty("all", textureId.toString());
        textures.addProperty("particle", textureId.toString());
        modelJson.add("textures", textures);

        // Create custom element
        JsonArray elements = new JsonArray();
        JsonObject element = new JsonObject();

        // Set from/to coordinates
        JsonArray from = new JsonArray();
        from.add(fromX);
        from.add(fromY);
        from.add(fromZ);
        element.add("from", from);

        JsonArray to = new JsonArray();
        to.add(toX);
        to.add(toY);
        to.add(toZ);
        element.add("to", to);

        // Create faces with proper UVs
        JsonObject faces = new JsonObject();
        String[] faceNames = {"down", "up", "north", "south", "west", "east"};

        for (String faceName : faceNames) {
            JsonObject face = new JsonObject();
            face.addProperty("texture", "#all");

            // Create UV array [minU, minV, maxU, maxV]
            JsonArray uv = new JsonArray();
            uv.add(fromX);
            uv.add(fromZ);
            uv.add(toX);
            uv.add(toZ);
            face.add("uv", uv);

            faces.add(faceName, face);
        }
        element.add("faces", faces);

        elements.add(element);
        modelJson.add("elements", elements);

        // Upload model
        modelCollector.accept(modelId, () -> modelJson);
    }
}
