package com.westerosblocks.data;

import com.google.gson.*;
import com.westerosblocks.WesterosBlocks;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class BlockDefinitionLoader {
    /**
     * Custom deserializer for TooltipEntry that handles both formats:
     * - Simple string: "tooltip text" -> TooltipEntry{text: "tooltip text", format: "GRAY"}
     * - Object: {"text": "tooltip text", "format": "RED"} -> TooltipEntry{text: "tooltip text", format: "RED"}
     */
    private static final JsonDeserializer<BlockDefinition.TooltipEntry> TOOLTIP_ENTRY_DESERIALIZER =
        (json, typeOfT, context) -> {
            if (json == null || json.isJsonNull()) {
                return null;
            }
            if (json.isJsonPrimitive() && json.getAsJsonPrimitive().isString()) {
                // Simple string format - convert to TooltipEntry with default format
                return new BlockDefinition.TooltipEntry(json.getAsString(), "GRAY");
            } else if (json.isJsonObject()) {
                // Object format - deserialize manually
                JsonObject obj = json.getAsJsonObject();
                String text = obj.has("text") ? obj.get("text").getAsString() : "";
                String format = obj.has("format") ? obj.get("format").getAsString() : "GRAY";
                return new BlockDefinition.TooltipEntry(text, format);
            }
            return null;
        };

    private static final Gson GSON = new GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapter(BlockDefinition.TooltipEntry.class, TOOLTIP_ENTRY_DESERIALIZER)
        .create();
    private final String blockDefinitionsPath;

    public BlockDefinitionLoader(String blockDefinitionsPath) {
        this.blockDefinitionsPath = blockDefinitionsPath;
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
        ModContainer container = FabricLoader.getInstance().getModContainer(WesterosBlocks.MOD_ID).orElse(null);
        if (container == null) {
            WesterosBlocks.LOGGER.error("Could not find mod container for {}", WesterosBlocks.MOD_ID);
            return;
        }

        // For each root path in the mod jar/directory
        for (Path rootPath : container.getRootPaths()) {
            // Remove leading slash if present and resolve path
            String pathWithoutSlash = blockDefinitionsPath.startsWith("/")
                ? blockDefinitionsPath.substring(1)
                : blockDefinitionsPath;
            Path dirPath = rootPath.resolve(pathWithoutSlash);

            if (Files.exists(dirPath) && Files.isDirectory(dirPath)) {
                WesterosBlocks.LOGGER.debug("Loading block definitions from: {}", dirPath);
                loadDefinitionsRecursively(dirPath, definitions);
            } else {
                WesterosBlocks.LOGGER.debug("Block definitions directory not found at: {}", dirPath);
            }
        }

        if (definitions.isEmpty()) {
            WesterosBlocks.LOGGER.warn("No individual block definitions were loaded!");
        }
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

        // Initialize the definition after loading (normalizes textures, inherits properties, etc.)
        try {
            definition.doInit();
            WesterosBlocks.LOGGER.debug("Initialized block definition: {}", definition.getBlockName());
        } catch (Exception e) {
            WesterosBlocks.LOGGER.error("Failed to initialize block definition '{}' from file: {}",
                definition.getBlockName(), filePath, e);
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