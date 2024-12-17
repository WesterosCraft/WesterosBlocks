package com.westerosblocks;

import com.google.gson.*;
import com.westerosblocks.block.WesterosBlockColorMap;
import com.westerosblocks.block.WesterosBlockDef;
import com.westerosblocks.block.WesterosBlockSetDef;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;

// Top level container for WesterosBlocks.json parsed data
public class WesterosBlocksJsonLoader {
    public static class WesterosBlocksConfig {
        public WesterosBlockSetDef[] blockSets;
        public WesterosBlockDef[] blocks;
        // TODO
        public WesterosBlockColorMap[] colorMaps;
        // public WesterosItemMenuOverrides[] menuOverrides;
        // public WesterosBlockTags[] blockTags;
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
     *
     * @param filenames List of resource paths to JSON configuration files
     * @return Merged WesterosBlockConfig configuration
     * @throws BlockConfigNotFoundException If any of the specified files cannot be found
     * @throws JsonParseException           If there's an error parsing any of the JSON files
     */
    public static WesterosBlocksConfig loadBlockConfigs(List<String> filenames)
            throws BlockConfigNotFoundException, JsonParseException {
        Gson gson = new Gson();
        JsonObject mergedConfig = new JsonObject();

        for (String filename : filenames) {
            // Read our block definition resources
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
        // Convert merged JsonObject back to WesterosBlockConfig
        return gson.fromJson(mergedConfig, WesterosBlocksConfig.class);
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
                // If the key doesn't exist in base, add it
                base.add(key, newElement);
            } else if (existingElement.isJsonObject() && newElement.isJsonObject()) {
                // If both are objects, recursively merge
                mergeJsonObjects(existingElement.getAsJsonObject(), newElement.getAsJsonObject());
            } else {
                // For non-object types, the additional file's value takes precedence
                base.add(key, newElement);
            }
        }
    }

    /**
     * Returns the block config defined in blocks.json, blockSets.json etc
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

    public static boolean sanityCheck(WesterosBlockDef[] defs) {
        HashSet<String> names = new HashSet<String>();
        // Make sure block IDs and names are unique
        for (WesterosBlockDef def : defs) {
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
        WesterosBlocks.LOGGER.info("WesterosBlocks.json passed sanity check");
        return true;
    }
}
