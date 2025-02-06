package com.westerosblocks;

import com.google.gson.*;
import com.westerosblocks.block.ModBlock;
import com.westerosblocks.block.ModBlockSetDef;
import com.westerosblocks.block.ModBlockTags;
import com.westerosblocks.item.WesterosItemMenuOverrides;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

// load definitions from resources/definitions parsed data
public class WesterosBlocksDefLoader {
    public static WesterosBlocksDefLoader.WesterosBlocksConfig customConfig;
    private static ModBlock[] customBlockDefs;

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
        public ModBlockSetDef[] blockSets;
        public ModBlock[] blocks;
        public WesterosItemMenuOverrides[] menuOverrides;
        public ModBlockTags[] blockTags;
    }

    public static class BlockConfigNotFoundException extends Exception {
        public BlockConfigNotFoundException() {
        }

        public BlockConfigNotFoundException(String message) {
            super(message);
        }
    }

    /**
     * Loads multiple JSON configuration files and merges them into a single configuration.
     */
    public static WesterosBlocksConfig loadBlockConfigs(List<String> filenames)
            throws BlockConfigNotFoundException, JsonParseException {
        Gson gson = new Gson();
        JsonObject mergedConfig = new JsonObject();

        // Load standard config files
        for (String filename : filenames) {
            try (InputStream in = WesterosBlocks.class.getResourceAsStream(filename);
                 BufferedReader rdr = in != null ? new BufferedReader(new InputStreamReader(in)) : null) {

                if (rdr == null) {
                    throw new BlockConfigNotFoundException("Could not find config file: " + filename);
                }

                // Parse the individual JSON file
                JsonObject currentConfig = gson.fromJson(rdr, JsonObject.class);

                for (String key : currentConfig.keySet()) {
                    JsonElement existingElement = mergedConfig.get(key);
                    JsonElement newElement = currentConfig.get(key);

                    if (existingElement == null) {
                        mergedConfig.add(key, newElement);
                    } else if (existingElement.isJsonObject() && newElement.isJsonObject()) {
                        mergeJsonObjects(existingElement.getAsJsonObject(), newElement.getAsJsonObject());
                    } else {
                        mergedConfig.add(key, newElement);
                    }
                }
            } catch (IOException e) {
                throw new BlockConfigNotFoundException("Error reading config file: " + filename);
            }
        }

        // Load block definitions from the new directory structure
        JsonArray blocksArray = loadBlocksFromDirectory(BLOCKS_DIRECTORY);
        mergedConfig.add("blocks", blocksArray);

        // Load block set definitions from the new directory structure
        JsonArray blockSetsArray = loadBlocksFromDirectory(BLOCK_SETS_DIRECTORY);
        mergedConfig.add("blockSets", blockSetsArray);

        // Convert merged JsonObject back to WesterosBlockConfig
        return gson.fromJson(mergedConfig, WesterosBlocksConfig.class);
    }

    /**
     * Loads JSON files from a directory and returns them as a JsonArray.
     *
     * @param directory The directory to load JSON files from
     * @return JsonArray containing all JSON objects from the directory
     */
    private static JsonArray loadBlocksFromDirectory(String directory) {
        Gson gson = new Gson();
        JsonArray jsonArray = new JsonArray();

        try {
            // Traverse the directory
            Path dirPath = Paths.get(WesterosBlocks.class.getResource(directory).toURI());
            Files.walk(dirPath)
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".json"))
                    .forEach(path -> {
                        try (InputStream in = Files.newInputStream(path);
                             BufferedReader rdr = new BufferedReader(new InputStreamReader(in))) {
                            // Parse each JSON file
                            JsonObject jsonObject = gson.fromJson(rdr, JsonObject.class);
                            jsonArray.add(jsonObject);
                        } catch (IOException e) {
                            WesterosBlocks.LOGGER.error("Error reading file: " + path, e);
                        }
                    });
        } catch (Exception e) {
            WesterosBlocks.LOGGER.error("Error loading definitions from directory: " + directory, e);
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
        for (String key : additional.keySet()) {
            JsonElement existingElement = base.get(key);
            JsonElement newElement = additional.get(key);

            if (existingElement == null) {
                base.add(key, newElement);
            } else if (existingElement.isJsonObject() && newElement.isJsonObject()) {
                mergeJsonObjects(existingElement.getAsJsonObject(), newElement.getAsJsonObject());
            } else {
                // For non-object types, the additional file's value takes precedence
                base.add(key, newElement);
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
                WesterosBlocks.LOGGER.error(String.format("Block '%s' - blockName duplicated", def.blockName));
                return false;
            }
        }
        WesterosBlocks.LOGGER.info("WesterosBlocks block definition files passed sanity check");
        return true;
    }

    // Expand block set definitions to obtain the full block definition list
    public static ModBlock[] getBlockDefs(WesterosBlocksConfig config) {
        ModBlockSetDef[] blockSetDefs = config.blockSets;
        ModBlock[] blockDefs = config.blocks;
        List<ModBlock> expandedBlockDefs = new LinkedList<>(Arrays.asList(blockDefs));

        if (config.blockSets.length > 0) {
            for (ModBlockSetDef blockSetDef : blockSetDefs) {
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