package com.westerosblocks.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.westerosblocks.WesterosBlocks;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Loads BlockSetDefinition instances from JSON files in the block_set_definitions/ directory.
 */
public class BlockSetDefinitionLoader {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final Path blockSetDefinitionsPath;

    public BlockSetDefinitionLoader(String blockSetDefinitionsPath) {
        this.blockSetDefinitionsPath = Paths.get(blockSetDefinitionsPath);
    }

    /**
     * Loads all block set definitions from the configured path.
     *
     * @return Map of base block name to BlockSetDefinition
     */
    public Map<String, BlockSetDefinition> loadAllDefinitions() {
        Map<String, BlockSetDefinition> definitions = new HashMap<>();

        WesterosBlocks.LOGGER.info("Loading block set definitions from resources: {}", blockSetDefinitionsPath);

        try {
            loadDefinitionsFromResources(definitions);
            WesterosBlocks.LOGGER.info("Successfully loaded {} block set definitions", definitions.size());
        } catch (Exception e) {
            WesterosBlocks.LOGGER.error("Failed to load block set definitions from resources", e);
        }

        return definitions;
    }

    private void loadDefinitionsFromResources(Map<String, BlockSetDefinition> definitions) throws Exception {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        // Try to find the block_set_definitions directory
        String[] resourcePaths = {
            "block_set_definitions",
            "assets/westerosblocks/block_set_definitions",
            "data/westerosblocks/block_set_definitions"
        };

        for (String resourcePath : resourcePaths) {
            java.net.URL resourceUrl = classLoader.getResource(resourcePath);
            if (resourceUrl != null) {
                loadDefinitionsFromResourcePath(resourceUrl, definitions);
                return;
            }
        }

        WesterosBlocks.LOGGER.warn("Could not find block_set_definitions in resources. Tried paths: {}",
            java.util.Arrays.toString(resourcePaths));
    }

    private void loadDefinitionsFromResourcePath(java.net.URL resourceUrl, Map<String, BlockSetDefinition> definitions) throws Exception {
        if ("file".equals(resourceUrl.getProtocol())) {
            // If it's a file URL, we can use the filesystem approach
            Path resourceDir = Paths.get(resourceUrl.toURI());
            loadDefinitionsRecursively(resourceDir, definitions);
        } else {
            WesterosBlocks.LOGGER.warn("Loading from JAR resources not yet fully implemented. Resource URL: {}", resourceUrl);
            // For JAR files, we'd need a different approach (using JarFile API or resource manifest)
            // For now, this is mainly for development where files are on the filesystem
        }
    }

    private void loadDefinitionsRecursively(Path directory, Map<String, BlockSetDefinition> definitions) throws IOException {
        try (Stream<Path> paths = Files.walk(directory)) {
            paths.filter(Files::isRegularFile)
                 .filter(path -> path.toString().endsWith(".json"))
                 .forEach(path -> loadDefinitionFile(path, definitions));
        }
    }

    private void loadDefinitionFile(Path filePath, Map<String, BlockSetDefinition> definitions) {
        try {
            String content = Files.readString(filePath);
            BlockSetDefinition definition = GSON.fromJson(content, BlockSetDefinition.class);

            if (definition == null) {
                WesterosBlocks.LOGGER.warn("Failed to parse block set definition file: {}", filePath);
                return;
            }

            if (definition.getBaseBlockName() == null || definition.getBaseBlockName().isEmpty()) {
                WesterosBlocks.LOGGER.warn("Block set definition missing baseBlockName in file: {}", filePath);
                return;
            }

            if (definitions.containsKey(definition.getBaseBlockName())) {
                WesterosBlocks.LOGGER.warn("Duplicate block set definition found for '{}' in file: {}",
                    definition.getBaseBlockName(), filePath);
            }

            definitions.put(definition.getBaseBlockName(), definition);
            WesterosBlocks.LOGGER.debug("Loaded block set definition: {} with {} variants",
                definition.getBaseBlockName(),
                definition.hasVariants() ? definition.getVariants().size() : 0);

        } catch (IOException e) {
            WesterosBlocks.LOGGER.error("Failed to read block set definition file: {}", filePath, e);
        } catch (JsonSyntaxException e) {
            WesterosBlocks.LOGGER.error("Invalid JSON syntax in block set definition file: {}", filePath, e);
        } catch (Exception e) {
            WesterosBlocks.LOGGER.error("Unexpected error loading block set definition file: {}", filePath, e);
        }
    }

    /**
     * Validates block set definitions.
     *
     * @param definitions Map of block set definitions to validate
     */
    public void validateDefinitions(Map<String, BlockSetDefinition> definitions) {
        int validCount = 0;
        int warningCount = 0;

        for (BlockSetDefinition definition : definitions.values()) {
            boolean isValid = true;

            // Check if textures are defined
            if (!definition.hasTextures()) {
                WesterosBlocks.LOGGER.warn("Block set '{}' has no textures defined", definition.getBaseBlockName());
                warningCount++;
                isValid = false;
            }

            // Check if base label is defined
            if (definition.getBaseLabel() == null || definition.getBaseLabel().isEmpty()) {
                WesterosBlocks.LOGGER.warn("Block set '{}' has no baseLabel defined", definition.getBaseBlockName());
                warningCount++;
                isValid = false;
            }

            // Check if hardness is defined
            if (definition.getHardness() == null) {
                WesterosBlocks.LOGGER.warn("Block set '{}' has no hardness defined", definition.getBaseBlockName());
                warningCount++;
                isValid = false;
            }

            // Check if sound is defined
            if (definition.getStepSound() == null || definition.getStepSound().isEmpty()) {
                WesterosBlocks.LOGGER.warn("Block set '{}' has no stepSound defined", definition.getBaseBlockName());
                warningCount++;
                isValid = false;
            }

            if (isValid) {
                validCount++;
            }
        }

        WesterosBlocks.LOGGER.info("Block set validation complete: {} valid, {} with warnings", validCount, warningCount);
    }
}
