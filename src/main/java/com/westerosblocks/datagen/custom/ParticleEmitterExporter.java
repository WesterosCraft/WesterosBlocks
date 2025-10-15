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

/**
 * Exporter for particle emitter blocks following block-models.md patterns.
 * Handles powered/unpowered states with off/on textures and custom element bounding boxes.
 */
public class ParticleEmitterExporter extends BaseBlockExporter {

    /**
     * Registers a particle emitter block with off and on textures.
     *
     * @param generator The block state model generator
     * @param block The particle emitter block
     */
    public static void registerCustomParticleEmitterBlock(BlockStateModelGenerator generator, Block block, BlockDefinition definition) {
        // Create block-specific model paths
        Identifier offModelId = createNestedModelId(block, "off");
        Identifier onModelId = createNestedModelId(block, "on");

        // Create off state model with custom element (8x8x8 cube)
        Identifier offTextureId = WesterosBlocks.id("block/particle_emitter/off");
        uploadParticleEmitterModel(offModelId, offTextureId, 4, 4, 4, 12, 12, 12, generator.modelCollector);

        // Create on state model with custom element (4x4x4 cube)
        Identifier onTextureId = WesterosBlocks.id("block/particle_emitter/on");
        uploadParticleEmitterModel(onModelId, onTextureId, 6, 6, 6, 10, 10, 10, generator.modelCollector);

        // Create blockstate with powered variants
        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block)
            .coordinate(BlockStateVariantMap.create(Properties.POWERED)
                .register(false, BlockStateVariant.create().put(VariantSettings.MODEL, offModelId))
                .register(true, BlockStateVariant.create().put(VariantSettings.MODEL, onModelId))));

        // Generate item model using off model
        generator.registerParentedItemModel(block, offModelId);
    }

    /**
     * Uploads a particle emitter model with custom element bounding box.
     *
     * @param modelId The model identifier
     * @param textureId The texture identifier
     * @param fromX X coordinate of element start (0-16)
     * @param fromY Y coordinate of element start (0-16)
     * @param fromZ Z coordinate of element start (0-16)
     * @param toX X coordinate of element end (0-16)
     * @param toY Y coordinate of element end (0-16)
     * @param toZ Z coordinate of element end (0-16)
     * @param modelCollector The model collector
     */
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
