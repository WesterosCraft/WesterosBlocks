package com.westerosblocks;

import com.google.gson.*;
import com.westerosblocks.block.ModBlock;
import com.westerosblocks.block.ModBlockColorMap;
import com.westerosblocks.block.ModBlockSet;
import com.westerosblocks.block.ModBlockTags;
import com.westerosblocks.item.WesterosItemMenuOverrides;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

// load definitions from resources/definitions parsed data
public class WesterosBlocksDefLoader {
    public static WesterosBlocksDefLoader.WesterosBlocksConfig customConfig;
    private static ModBlock[] customBlockDefs;
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private static final List<String> configFiles = List.of(
            "/definitions/color_maps.json",
            "/definitions/block_tags.json",
            "/definitions/menu_overrides.json"
    );
    private static final String BLOCKS_DIRECTORY = "/definitions/blocks";
    private static final String BLOCK_SETS_DIRECTORY = "/definitions/block_sets";

    // determines order of how blocks get loaded, because certain block types need to be registered before others
    private static final Map<String, Integer> BLOCK_TYPE_PRIORITIES = Map.of(
            "plant", 1,
            "web", 2,
            "flowerpot", 3
    );

    public static void initialize() {
        customConfig = WesterosBlocksDefLoader.getBlockConfig(configFiles);
        customBlockDefs = getBlockDefs(customConfig);
        WesterosBlocks.LOGGER.info("Loaded {} block definitions", customBlockDefs.length);

        if (!sanityCheck(customBlockDefs)) {
            WesterosBlocks.LOGGER.error("WesterosBlocks block definitions failed sanity check");
        }
    }

    public static class WesterosBlocksConfig {
        public ModBlockSet[] blockSets;
        public ModBlock[] blocks;
        public ModBlockColorMap[] colorMaps;
        public WesterosItemMenuOverrides[] menuOverrides;
        public ModBlockTags[] blockTags;
    }

    public static class BlockConfigNotFoundException extends Exception {
        public BlockConfigNotFoundException(String message) {
            super(message);
        }
    }

    /**
     * Loads multiple JSON configuration files and merges them into a single configuration.
     */
    public static WesterosBlocksConfig loadBlockConfigs(List<String> filenames)
            throws BlockConfigNotFoundException, JsonParseException {
        JsonObject mergedConfig = new JsonObject();

        for (String filename : filenames) {
            try (InputStream in = WesterosBlocks.class.getResourceAsStream(filename);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {

                JsonElement element = JsonParser.parseReader(reader);
                if (!element.isJsonObject()) {
                    throw new JsonParseException("Expected JSON object in " + filename);
                }
                mergeJsonObjects(mergedConfig, element.getAsJsonObject());
            } catch (IOException e) {
                throw new BlockConfigNotFoundException("Error reading config file: " + filename);
            }
        }

        try {
            // block defs
            JsonArray blocksArray = loadBlocksFromDirectory(BLOCKS_DIRECTORY);
            mergedConfig.add("blocks", blocksArray);

            // block set defs
            JsonArray blockSetsArray = loadBlocksFromDirectory(BLOCK_SETS_DIRECTORY);
            mergedConfig.add("blockSets", blockSetsArray);
        } catch (Exception e) {
            WesterosBlocks.LOGGER.error("Error loading block definitions", e);
        }

        return GSON.fromJson(mergedConfig, WesterosBlocksConfig.class);
    }

    /**
     * Loads JSON files from a directory and returns them as a JsonArray.
     *
     * @param directory The directory to load JSON files from
     * @return JsonArray containing all JSON objects from the directory
     */
    private static JsonArray loadBlocksFromDirectory(String directory) {
        JsonArray jsonArray = new JsonArray();

        ModContainer container = FabricLoader.getInstance().getModContainer(WesterosBlocks.MOD_ID).orElse(null);
        if (container == null) {
            WesterosBlocks.LOGGER.error("Could not find mod container for " + WesterosBlocks.MOD_ID);
            return jsonArray;
        }

        // For each root path in the mod jar
        for (Path rootPath : container.getRootPaths()) {
            Path dirPath = rootPath.resolve(directory.substring(1)); // removes leading slash

            if (Files.exists(dirPath) && Files.isDirectory(dirPath)) {
                try {
                    Files.walk(dirPath)
                            .filter(path -> path.toString().endsWith(".json"))
                            .forEach(path -> {
                                try {
                                    // Convert path to resource path for getResourceAsStream
                                    String relativePath = rootPath.relativize(path).toString();
                                    String resourcePath = "/" + relativePath.replace('\\', '/');

                                    try (InputStream stream = WesterosBlocks.class.getResourceAsStream(resourcePath)) {
                                        if (stream != null) {
                                            JsonElement element = JsonParser.parseReader(
                                                    new InputStreamReader(stream)
                                            );
                                            if (element.isJsonObject()) {
                                                jsonArray.add(element);
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    WesterosBlocks.LOGGER.error("Error reading file: {}", path, e);
                                }
                            });
                } catch (IOException e) {
                    WesterosBlocks.LOGGER.error("Error walking directory: {}", dirPath, e);
                }
            }
        }

        return jsonArray;
    }


    /**
     * Recursively merges two JsonObjects.
     *
     * @param base       The base JsonObject to merge into
     * @param additional The additional JsonObject to merge from
     */
    private static void mergeJsonObjects(JsonObject base, JsonObject additional) {
        for (Map.Entry<String, JsonElement> entry : additional.entrySet()) {
            String key = entry.getKey();
            JsonElement value = entry.getValue();
            JsonElement existing = base.get(key);

            if (existing == null) {
                base.add(key, value);
            } else if (existing.isJsonObject() && value.isJsonObject()) {
                mergeJsonObjects(existing.getAsJsonObject(), value.getAsJsonObject());
            } else if (existing.isJsonArray() && value.isJsonArray()) {
                JsonArray baseArray = existing.getAsJsonArray();
                value.getAsJsonArray().forEach(baseArray::add);
            } else {
                base.add(key, value);
            }
        }
    }

    /**
     * Returns the block config defined in definitions/blocks and definitions/block_sets etc
     */
    public static WesterosBlocksConfig getBlockConfig(List<String> filenames) {
        WesterosBlocksConfig combinedConfig = null;
        try {
            combinedConfig = loadBlockConfigs(filenames);
        } catch (BlockConfigNotFoundException e) {
            WesterosBlocks.LOGGER.error("WesterosBlocks couldn't find its block definition resource");
        } catch (JsonSyntaxException iox) {
            WesterosBlocks.LOGGER.error("WesterosBlocks couldn't parse its block definition");
        } catch (JsonParseException ex) {
            WesterosBlocks.LOGGER.warn("couldn't read oldWesterosBlocks.json block definition; skipping");
        }

        if (combinedConfig == null) {
            WesterosBlocks.LOGGER.error("WesterosBlocks couldn't read its block definition");
        }
        return combinedConfig;
    }

    public static boolean sanityCheck(ModBlock[] defs) {
        HashSet<String> names = new HashSet<>();
        // Make sure block IDs and names are unique
        for (ModBlock def : defs) {
            if (def == null)
                continue;
            if (def.blockName == null) {
                WesterosBlocks.LOGGER.error("Block definition is missing blockName");
                return false;
            }
            if (!names.add(def.blockName)) { // If already defined
                WesterosBlocks.LOGGER.error("Block '{}' - blockName duplicated", def.blockName);
                return false;
            }
        }
        WesterosBlocks.LOGGER.info("WesterosBlocks block definition files passed sanity check");
        return true;
    }

    // Expand block set definitions to obtain the full block definition list
    public static ModBlock[] getBlockDefs(WesterosBlocksConfig config) {
        ModBlockSet[] blockSetDefs = config.blockSets;
        ModBlock[] blockDefs = config.blocks;
        List<ModBlock> expandedBlockDefs = new LinkedList<>(Arrays.asList(blockDefs));

        if (config.blockSets.length > 0) {
            for (ModBlockSet blockSetDef : blockSetDefs) {
                if (blockSetDef == null)
                    continue;
                List<ModBlock> variantBlockDefs = blockSetDef.generateBlockDefs();
                expandedBlockDefs.addAll(variantBlockDefs);
            }
        }

        // sorts block definitions by priority
        expandedBlockDefs.sort(Comparator.comparingInt(block ->
                BLOCK_TYPE_PRIORITIES.getOrDefault(block.blockType, Integer.MAX_VALUE)
        ));

        return expandedBlockDefs.toArray(new ModBlock[0]);
    }

    public static ModBlock[] getCustomBlockDefs() {
        return customBlockDefs;
    }

    public static WesterosBlocksConfig getCustomConfig() {
        return customConfig;
    }
}