package com.westerosblocks.data;

import com.google.gson.*;
import com.westerosblocks.WesterosBlocks;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Loads all block definitions and block set definitions from the single
 * consolidated definitions/WesterosBlocks.json file:
 *
 * <pre>
 * {
 *     "blocks": [ { BlockDefinition }, ... ],
 *     "blockSets": [ { BlockSetDefinition }, ... ]
 * }
 * </pre>
 *
 * Array order is authoritative: definitions register (and appear in creative
 * tabs) in the order they are declared in the file, blocks first, then
 * expanded block sets.
 */
public class ConsolidatedDefinitionLoader {
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

    /** Gson target for the sectioned file. */
    private static class ConsolidatedDefinitions {
        List<BlockDefinition> blocks;
        List<BlockSetDefinition> blockSets;
    }

    private final String definitionsFilePath;
    private final List<String> duplicateNames = new ArrayList<>();
    private ConsolidatedDefinitions parsed;

    public ConsolidatedDefinitionLoader(String definitionsFilePath) {
        this.definitionsFilePath = definitionsFilePath;
    }

    /** Block names that were seen in more than one individual definition (collapsed in the map). */
    public List<String> getDuplicateNames() {
        return duplicateNames;
    }

    /**
     * Reads and parses the consolidated definitions file from mod resources.
     *
     * @return true if the file was found and parsed
     */
    public boolean load() {
        WesterosBlocks.LOGGER.info("Loading block definitions from: {}", definitionsFilePath);

        ModContainer container = FabricLoader.getInstance().getModContainer(WesterosBlocks.MOD_ID).orElse(null);
        if (container == null) {
            WesterosBlocks.LOGGER.error("Could not find mod container for {}", WesterosBlocks.MOD_ID);
            return false;
        }

        String pathWithoutSlash = definitionsFilePath.startsWith("/")
            ? definitionsFilePath.substring(1)
            : definitionsFilePath;

        // In dev environments the mod container can expose multiple root paths;
        // load from the first one containing the file so it is parsed exactly once.
        for (Path rootPath : container.getRootPaths()) {
            Path filePath = rootPath.resolve(pathWithoutSlash);
            if (!Files.exists(filePath) || !Files.isRegularFile(filePath)) {
                continue;
            }
            try {
                String content = Files.readString(filePath);
                parsed = GSON.fromJson(content, ConsolidatedDefinitions.class);
                if (parsed == null) {
                    WesterosBlocks.LOGGER.error("Consolidated definitions file is empty: {}", filePath);
                    return false;
                }
                // Both sections are required (schemas/westerosblocks.schema.json); a missing
                // section means the file is corrupt or was mis-edited.
                if (parsed.blocks == null || parsed.blockSets == null) {
                    WesterosBlocks.LOGGER.error("Consolidated definitions file is missing its '{}' section: {}",
                        parsed.blocks == null ? "blocks" : "blockSets", filePath);
                    parsed = null;
                    return false;
                }
                return true;
            } catch (IOException e) {
                WesterosBlocks.LOGGER.error("Failed to read consolidated definitions file: {}", filePath, e);
                return false;
            } catch (JsonSyntaxException e) {
                WesterosBlocks.LOGGER.error("Invalid JSON syntax in consolidated definitions file: {}", filePath, e);
                return false;
            }
        }

        WesterosBlocks.LOGGER.error("Consolidated definitions file not found: {}", definitionsFilePath);
        return false;
    }

    /**
     * Returns the individual block definitions from the "blocks" section,
     * keyed by block name in declaration order.
     */
    public Map<String, BlockDefinition> getBlockDefinitions() {
        Map<String, BlockDefinition> definitions = new LinkedHashMap<>();

        if (parsed == null || parsed.blocks == null) {
            WesterosBlocks.LOGGER.warn("Consolidated definitions file has no 'blocks' section");
            return definitions;
        }

        for (BlockDefinition definition : parsed.blocks) {
            addDefinition(definition, definitions);
        }

        WesterosBlocks.LOGGER.info("Successfully loaded {} block definitions", definitions.size());
        return definitions;
    }

    /**
     * Returns the block set definitions from the "blockSets" section,
     * keyed by base block name in declaration order.
     */
    public Map<String, BlockSetDefinition> getBlockSetDefinitions() {
        Map<String, BlockSetDefinition> definitions = new LinkedHashMap<>();

        if (parsed == null || parsed.blockSets == null) {
            WesterosBlocks.LOGGER.warn("Consolidated definitions file has no 'blockSets' section");
            return definitions;
        }

        for (BlockSetDefinition definition : parsed.blockSets) {
            if (definition == null) {
                WesterosBlocks.LOGGER.warn("Failed to parse block set definition in: {}", definitionsFilePath);
                continue;
            }

            if (definition.getBaseBlockName() == null || definition.getBaseBlockName().isEmpty()) {
                WesterosBlocks.LOGGER.warn("Block set definition missing baseBlockName in: {}", definitionsFilePath);
                continue;
            }

            if (definitions.containsKey(definition.getBaseBlockName())) {
                WesterosBlocks.LOGGER.warn("Duplicate block set definition found for '{}'",
                    definition.getBaseBlockName());
            }

            definitions.put(definition.getBaseBlockName(), definition);
            WesterosBlocks.LOGGER.debug("Loaded block set definition: {} with {} variants",
                definition.getBaseBlockName(),
                definition.hasVariants() ? definition.getVariants().size() : 0);
        }

        WesterosBlocks.LOGGER.info("Successfully loaded {} block set definitions", definitions.size());
        return definitions;
    }

    /**
     * Validates and adds a block definition to the definitions map.
     *
     * @param definition The block definition to add
     * @param definitions The map to add the definition to
     * @return true if the definition was successfully added, false if validation failed
     */
    private boolean addDefinition(BlockDefinition definition, Map<String, BlockDefinition> definitions) {
        if (definition == null) {
            WesterosBlocks.LOGGER.warn("Failed to parse block definition in: {}", definitionsFilePath);
            return false;
        }

        if (definition.getBlockName() == null || definition.getBlockName().isEmpty()) {
            WesterosBlocks.LOGGER.warn("Block definition missing blockName in: {}", definitionsFilePath);
            return false;
        }

        if (definition.getBlockType() == null || definition.getBlockType().isEmpty()) {
            WesterosBlocks.LOGGER.warn("Block definition missing blockType in: {}", definitionsFilePath);
            return false;
        }

        // Initialize the definition after loading (normalizes textures, inherits properties, etc.)
        try {
            definition.doInit();
            WesterosBlocks.LOGGER.debug("Initialized block definition: {}", definition.getBlockName());
        } catch (Exception e) {
            WesterosBlocks.LOGGER.error("Failed to initialize block definition '{}'",
                definition.getBlockName(), e);
            return false;
        }

        if (definitions.containsKey(definition.getBlockName())) {
            WesterosBlocks.LOGGER.warn("Duplicate block definition found for '{}'",
                definition.getBlockName());
            duplicateNames.add(definition.getBlockName());
        }

        definitions.put(definition.getBlockName(), definition);
        WesterosBlocks.LOGGER.debug("Loaded block definition: {} ({})",
            definition.getBlockName(), definition.getBlockType());

        return true;
    }

    public Map<String, List<BlockDefinition>> groupByType(Map<String, BlockDefinition> definitions) {
        Map<String, List<BlockDefinition>> groupedDefinitions = new LinkedHashMap<>();

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

            // Check textures (skip for custom model blocks as they handle textures in model files,
            // for particle emitters which have no block textures by design, and for definitions
            // whose states are all flagged as custom models)
            boolean stateLevelCustomModel = definition.getStates() != null
                    && !definition.getStates().isEmpty()
                    && definition.getStates().stream().allMatch(s -> s.isCustomModel());
            boolean skipTextureCheck = definition.hasCustomModel()
                    || stateLevelCustomModel
                    || "particle".equals(definition.getBlockType());
            if (!skipTextureCheck) {
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

    /**
     * Validates block set definitions.
     *
     * @param definitions Map of block set definitions to validate
     */
    public void validateBlockSets(Map<String, BlockSetDefinition> definitions) {
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
            if (definition.getSoundGroup() == null || definition.getSoundGroup().isEmpty()) {
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
