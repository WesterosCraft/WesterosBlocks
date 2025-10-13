package com.westerosblocks.datagen.providers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.data.BlockDefinitionRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class ModPolytoneProvider implements DataProvider {
    private final DataOutput.PathResolver blockModifiersPathResolver;
    private final DataOutput.PathResolver itemModifiersPathResolver;

    public ModPolytoneProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        // Create path resolvers for polytone/block_modifiers and polytone/item_modifiers
        this.blockModifiersPathResolver = output.getResolver(DataOutput.OutputType.RESOURCE_PACK, "polytone/block_modifiers");
        this.itemModifiersPathResolver = output.getResolver(DataOutput.OutputType.RESOURCE_PACK, "polytone/item_modifiers");
    }

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        Map<String, List<String>> blocksByColormap = new HashMap<>();
        BlockDefinitionRegistry registry = BlockDefinitionRegistry.getInstance();

        if (!registry.isInitialized()) {
            WesterosBlocks.LOGGER.warn("BlockDefinitionRegistry not initialized - skipping Polytone generation");
            return CompletableFuture.completedFuture(null);
        }

        // Group blocks by their colorMult value
        for (BlockDefinition definition : registry.getAllDefinitions()) {
            String colorMult = definition.getColorMult();

            if (colorMult != null && !colorMult.isEmpty()) {
                // Extract the colormap identifier from the path
                // E.g., "textures/colormap/sand" -> "sand" or just "birch" -> "birch"
                String colormapId = extractColormapId(colorMult);

                String blockId = WesterosBlocks.MOD_ID + ":" + definition.getBlockName();

                blocksByColormap.computeIfAbsent(colormapId, k -> new ArrayList<>()).add(blockId);
            }
        }

        if (blocksByColormap.isEmpty()) {
            WesterosBlocks.LOGGER.info("No blocks with colorMult property found - skipping Polytone generation");
            return CompletableFuture.completedFuture(null);
        }

        // Create list of write futures
        List<CompletableFuture<?>> futures = new ArrayList<>();

        for (Map.Entry<String, List<String>> entry : blocksByColormap.entrySet()) {
            String colormapId = entry.getKey();
            List<String> blockIds = entry.getValue();

            // Sort block IDs for consistent output
            Collections.sort(blockIds);

            // Create JSON object
            JsonObject json = new JsonObject();

            JsonArray targetsArray = new JsonArray();
            blockIds.forEach(targetsArray::add);
            json.add("targets", targetsArray);

            json.addProperty("colormap", WesterosBlocks.MOD_ID + ":" + colormapId);

            // Write to block_modifiers folder
            Identifier blockFileId = Identifier.of(WesterosBlocks.MOD_ID, colormapId + "_tinted_blocks");
            Path blockOutputPath = blockModifiersPathResolver.resolveJson(blockFileId);
            CompletableFuture<?> blockFuture = DataProvider.writeToPath(writer, json, blockOutputPath);
            futures.add(blockFuture);

            // Write to item_modifiers folder (same content)
            Identifier itemFileId = Identifier.of(WesterosBlocks.MOD_ID, colormapId + "_tinted_blocks");
            Path itemOutputPath = itemModifiersPathResolver.resolveJson(itemFileId);
            CompletableFuture<?> itemFuture = DataProvider.writeToPath(writer, json, itemOutputPath);
            futures.add(itemFuture);

            WesterosBlocks.LOGGER.info("Generated Polytone modifier: {} with {} blocks (block_modifiers + item_modifiers)",
                    colormapId + "_tinted_blocks.json", blockIds.size());
        }

        WesterosBlocks.LOGGER.info("Successfully generated {} Polytone modifier files ({} in block_modifiers, {} in item_modifiers)",
                blocksByColormap.size() * 2, blocksByColormap.size(), blocksByColormap.size());

        // Return combined future of all writes
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    /**
     * Extracts the colormap identifier from a colorMult value
     * E.g., "textures/colormap/sand" -> "sand", "birch" -> "birch"
     */
    private String extractColormapId(String colorMult) {
        if (colorMult.contains("/")) {
            String[] parts = colorMult.split("/");
            return parts[parts.length - 1];
        }
        return colorMult;
    }

    @Override
    public String getName() {
        return "Polytone Block & Item Modifiers";
    }
}
