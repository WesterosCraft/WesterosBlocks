package com.westerosblocks.datagen.providers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.data.BlockDefinition;
import com.westerosblocks.data.BlockDefinitionRegistry;
import com.westerosblocks.data.ColorMapDefinition;
import com.westerosblocks.data.ColorMapEntry;
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
        List<String> xzOffsetBlocks = new ArrayList<>();
        BlockDefinitionRegistry registry = BlockDefinitionRegistry.getInstance();

        if (!registry.isInitialized()) {
            WesterosBlocks.LOGGER.warn("BlockDefinitionRegistry not initialized - skipping Polytone generation");
            return CompletableFuture.completedFuture(null);
        }

        // Group blocks by their colorMult value(s) and collect blocks with xz offset
        for (BlockDefinition definition : registry.getAllDefinitions()) {
            // Skip test blocks (blocks in westeros_test_tab)
            if ("westeros_test_tab".equals(definition.getCreativeTab())) {
                continue;
            }

            String blockId = WesterosBlocks.MOD_ID + ":" + definition.getBlockName();

            // Collect blocks with xz offset
            if (definition.isDoOffsetXZ()) {
                xzOffsetBlocks.add(blockId);
            }

            // Handle single colorMult
            if (definition.hasColorMult()) {
                String colorMult = definition.getColorMult();
                String colormapId = extractColormapId(colorMult);
                blocksByColormap.computeIfAbsent(colormapId, k -> new ArrayList<>()).add(blockId);
            }

            // Handle multiple colorMults (blocks can have multiple colormaps applied)
            if (definition.hasColorMults()) {
                List<String> colorMults = definition.getColorMults();
                for (String colorMult : colorMults) {
                    String colormapId = extractColormapId(colorMult);
                    blocksByColormap.computeIfAbsent(colormapId, k -> new ArrayList<>()).add(blockId);
                }
            }
        }

        // Merge vanilla blocks from color_maps.json
        ColorMapDefinition colorMaps = registry.getColorMaps();
        int vanillaBlockCount = 0;
        if (colorMaps != null && colorMaps.hasColorMaps()) {
            for (ColorMapEntry entry : colorMaps.getColorMaps()) {
                if (entry.hasColorMult() && entry.hasBlockNames()) {
                    String colorMult = entry.getColorMult();
                    // Skip hex colors (e.g., "#FFFFFF") as they don't reference colormap textures
                    if (colorMult.startsWith("#")) {
                        WesterosBlocks.LOGGER.debug("Skipping hex color {} for blocks: {}", colorMult, entry.getBlockNames());
                        continue;
                    }
                    String colormapId = extractColormapId(colorMult);
                    List<String> blockIds = entry.getBlockNames();
                    blocksByColormap.computeIfAbsent(colormapId, k -> new ArrayList<>()).addAll(blockIds);
                    vanillaBlockCount += blockIds.size();
                }
            }
            WesterosBlocks.LOGGER.info("Added {} vanilla/external blocks from color_maps.json", vanillaBlockCount);
        }

        if (blocksByColormap.isEmpty() && xzOffsetBlocks.isEmpty()) {
            WesterosBlocks.LOGGER.info("No blocks with colorMult or doOffsetXZ found - skipping Polytone generation");
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
        }

        // Generate xz_offset_blocks.json for plant blocks
        if (!xzOffsetBlocks.isEmpty()) {
            Collections.sort(xzOffsetBlocks);

            JsonObject xzJson = new JsonObject();
            JsonArray xzTargets = new JsonArray();
            xzOffsetBlocks.forEach(xzTargets::add);
            xzJson.add("targets", xzTargets);
            xzJson.addProperty("offset_type", "xz");

            Identifier xzFileId = Identifier.of(WesterosBlocks.MOD_ID, "xz_offset_blocks");
            Path xzOutputPath = blockModifiersPathResolver.resolveJson(xzFileId);
            futures.add(DataProvider.writeToPath(writer, xzJson, xzOutputPath));

            WesterosBlocks.LOGGER.info("Generated xz_offset_blocks.json with {} blocks", xzOffsetBlocks.size());
        }

        WesterosBlocks.LOGGER.info("Successfully generated {} Polytone modifier files ({} in block_modifiers, {} in item_modifiers)",
                blocksByColormap.size() * 2 + (xzOffsetBlocks.isEmpty() ? 0 : 1), blocksByColormap.size() + (xzOffsetBlocks.isEmpty() ? 0 : 1), blocksByColormap.size());

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
