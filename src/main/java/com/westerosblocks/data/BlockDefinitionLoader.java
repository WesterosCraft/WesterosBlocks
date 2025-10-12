package com.westerosblocks.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.westerosblocks.WesterosBlocks;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class BlockDefinitionLoader {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final Path blockDefinitionsPath;

    public BlockDefinitionLoader(String blockDefinitionsPath) {
        this.blockDefinitionsPath = Paths.get(blockDefinitionsPath);
    }

    public Map<String, BlockDefinition> loadAllDefinitions() {
        Map<String, BlockDefinition> definitions = new HashMap<>();

        WesterosBlocks.LOGGER.info("Loading block definitions from resources: {}", blockDefinitionsPath);

        try {
            loadDefinitionsFromResources(definitions);
            WesterosBlocks.LOGGER.info("Successfully loaded {} block definitions", definitions.size());
        } catch (Exception e) {
            WesterosBlocks.LOGGER.error("Failed to load block definitions from resources", e);
        }

        return definitions;
    }

    private void loadDefinitionsFromResources(Map<String, BlockDefinition> definitions) throws Exception {
        // Get resource URL for the block_definitions directory
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        // Try to find the block_definitions as a resource
        try (var inputStream = classLoader.getResourceAsStream("definitions/block_definitions")) {
            if (inputStream == null) {
                // Try alternative resource paths
                String[] resourcePaths = {
                    "assets/westerosblocks/definitions/block_definitions",
                    "data/westerosblocks/definitions/block_definitions",
                    "block_definitions"
                };

                for (String resourcePath : resourcePaths) {
                    try (var stream = classLoader.getResourceAsStream(resourcePath)) {
                        if (stream != null) {
                            WesterosBlocks.LOGGER.info("Found block definitions at resource path: {}", resourcePath);
                            loadDefinitionsFromResourcePath(resourcePath, definitions);
                            return;
                        }
                    }
                }

                WesterosBlocks.LOGGER.error("Could not find block_definitions in resources. Tried paths: {}",
                    java.util.Arrays.toString(resourcePaths));
                return;
            }
        }

        // Load from the default path
        loadDefinitionsFromResourcePath("definitions/block_definitions", definitions);
    }

    private void loadDefinitionsFromResourcePath(String resourcePath, Map<String, BlockDefinition> definitions) throws Exception {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        // Get all JSON files from the resource directory
        // This is a bit tricky with resources, so we'll need to use a different approach
        java.net.URL resourceUrl = classLoader.getResource(resourcePath);
        if (resourceUrl == null) {
            WesterosBlocks.LOGGER.error("Resource path not found: {}", resourcePath);
            return;
        }

        if ("file".equals(resourceUrl.getProtocol())) {
            // If it's a file URL, we can use the filesystem approach
            Path resourceDir = Paths.get(resourceUrl.toURI());
            loadDefinitionsRecursively(resourceDir, definitions);
        } else {
            // If it's in a JAR, we need to handle it differently
            WesterosBlocks.LOGGER.warn("Loading from JAR resources not yet implemented. Resource URL: {}", resourceUrl);
            // For now, fall back to manual resource loading for known files
            loadKnownResourceFiles(resourcePath, definitions);
        }
    }

    private void loadKnownResourceFiles(String basePath, Map<String, BlockDefinition> definitions) {
        // List of known subdirectories in block_definitions
        String[] blockTypes = {
            "solid", "door", "log", "plant", "flowerpot", "web"
//                ,"slab", "halfdoor", "pane", "torch",
//            "chair", "table", "branch", "beacon", "bed", "crop", "fan",
//            "fence", "fencegate", "fire", "furnace", "ladder",
//            "leaves", "particle", "rail", "vines", "wall"
        };

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        for (String blockType : blockTypes) {
            String typePath = basePath + "/" + blockType;

            // Try to get the directory listing (this won't work in JARs)
            try (var inputStream = classLoader.getResourceAsStream(typePath)) {
                if (inputStream != null) {
                    WesterosBlocks.LOGGER.debug("Found block type directory: {}", typePath);
                    // Unfortunately, we can't easily list files in a JAR resource
                    // This would need a more sophisticated approach using reflection or
                    // creating a manifest of all JSON files
                }
            } catch (Exception e) {
                WesterosBlocks.LOGGER.debug("Could not access resource directory: {}", typePath);
            }
        }

        WesterosBlocks.LOGGER.warn("Resource-based loading needs enhancement for JAR files. " +
            "Consider using filesystem approach during development.");
    }

    private void loadDefinitionsRecursively(Path directory, Map<String, BlockDefinition> definitions) throws IOException {
        try (Stream<Path> paths = Files.walk(directory)) {
            paths.filter(Files::isRegularFile)
                 .filter(path -> path.toString().endsWith(".json"))
                 .forEach(path -> loadDefinitionFile(path, definitions));
        }
    }

    private void loadDefinitionFile(Path filePath, Map<String, BlockDefinition> definitions) {
        try {
            String content = Files.readString(filePath);

            // Try to parse as array first (new consolidated format)
            try {
                BlockDefinition[] definitionArray = GSON.fromJson(content, BlockDefinition[].class);

                if (definitionArray != null && definitionArray.length > 0) {
                    // Successfully parsed as array - load all definitions
                    WesterosBlocks.LOGGER.debug("Loading {} block definitions from consolidated file: {}",
                        definitionArray.length, filePath);

                    for (BlockDefinition definition : definitionArray) {
                        if (!addDefinition(definition, definitions, filePath)) {
                            continue; // Skip invalid definitions
                        }
                    }
                    return;
                }
            } catch (JsonSyntaxException e) {
                // Not an array, try as single object (legacy format)
            }

            // Fall back to parsing as single object (legacy format)
            BlockDefinition definition = GSON.fromJson(content, BlockDefinition.class);
            addDefinition(definition, definitions, filePath);

        } catch (IOException e) {
            WesterosBlocks.LOGGER.error("Failed to read block definition file: {}", filePath, e);
        } catch (JsonSyntaxException e) {
            WesterosBlocks.LOGGER.error("Invalid JSON syntax in block definition file: {}", filePath, e);
        } catch (Exception e) {
            WesterosBlocks.LOGGER.error("Unexpected error loading block definition file: {}", filePath, e);
        }
    }

    /**
     * Validates and adds a block definition to the definitions map.
     *
     * @param definition The block definition to add
     * @param definitions The map to add the definition to
     * @param filePath The source file path (for logging)
     * @return true if the definition was successfully added, false if validation failed
     */
    private boolean addDefinition(BlockDefinition definition, Map<String, BlockDefinition> definitions, Path filePath) {
        if (definition == null) {
            WesterosBlocks.LOGGER.warn("Failed to parse block definition in file: {}", filePath);
            return false;
        }

        if (definition.getBlockName() == null || definition.getBlockName().isEmpty()) {
            WesterosBlocks.LOGGER.warn("Block definition missing blockName in file: {}", filePath);
            return false;
        }

        if (definition.getBlockType() == null || definition.getBlockType().isEmpty()) {
            WesterosBlocks.LOGGER.warn("Block definition missing blockType in file: {}", filePath);
            return false;
        }

        if (definitions.containsKey(definition.getBlockName())) {
            WesterosBlocks.LOGGER.warn("Duplicate block definition found for '{}' in file: {}",
                definition.getBlockName(), filePath);
        }

        definitions.put(definition.getBlockName(), definition);
        WesterosBlocks.LOGGER.debug("Loaded block definition: {} ({})",
            definition.getBlockName(), definition.getBlockType());

        return true;
    }

    public Map<String, List<BlockDefinition>> groupByType(Map<String, BlockDefinition> definitions) {
        Map<String, List<BlockDefinition>> groupedDefinitions = new HashMap<>();

        for (BlockDefinition definition : definitions.values()) {
            String blockType = definition.getBlockType();
            groupedDefinitions.computeIfAbsent(blockType, k -> new ArrayList<>()).add(definition);
        }

        return groupedDefinitions;
    }

    public void validateDefinitions(Map<String, BlockDefinition> definitions) {
        int validCount = 0;
        int warningCount = 0;

        for (BlockDefinition definition : definitions.values()) {
            boolean isValid = true;

            // Check textures (skip for custom model blocks as they handle textures in model files)
            if (!definition.hasCustomModel()) {
                // Check if block has textures defined either directly, in randomTextures, or in states
                boolean hasTextures = false;

                // Check direct textures
                if (definition.getTextures() != null && !definition.getTextures().isEmpty()) {
                    hasTextures = true;
                }

                // Check random textures
                if (definition.getRandomTextures() != null && !definition.getRandomTextures().isEmpty()) {
                    hasTextures = true;
                }

                // Check textures in states
                if (definition.getStates() != null && !definition.getStates().isEmpty()) {
                    for (var state : definition.getStates()) {
                        if ((state.getTextures() != null && !state.getTextures().isEmpty()) ||
                            (state.getRandomTextures() != null && !state.getRandomTextures().isEmpty())) {
                            hasTextures = true;
                            break;
                        }
                    }
                }

                // Check textures in stack property (for cuboid-nsew-stack blocks)
                if (definition.getStack() != null && !definition.getStack().isEmpty()) {
                    for (var stackElement : definition.getStack()) {
                        if (stackElement.getTextures() != null && !stackElement.getTextures().isEmpty()) {
                            hasTextures = true;
                            break;
                        }
                    }
                }

                if (!hasTextures) {
                    WesterosBlocks.LOGGER.warn("Block '{}' has no textures defined", definition.getBlockName());
                    warningCount++;
                    isValid = false;
                }
            }

            if (definition.getLabel() == null || definition.getLabel().isEmpty()) {
                WesterosBlocks.LOGGER.warn("Block '{}' has no label defined", definition.getBlockName());
                warningCount++;
                isValid = false;
            }

            if (isValid) {
                validCount++;
            }
        }

        WesterosBlocks.LOGGER.info("Validation complete: {} valid, {} with warnings", validCount, warningCount);
    }
}