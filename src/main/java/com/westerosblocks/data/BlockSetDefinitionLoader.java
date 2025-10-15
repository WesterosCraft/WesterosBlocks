package com.westerosblocks.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.westerosblocks.WesterosBlocks;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Loads BlockSetDefinition instances from JSON files in the definitions/block_set_definitions directory.
 */
public class BlockSetDefinitionLoader {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final String blockSetDefinitionsPath;

    public BlockSetDefinitionLoader(String blockSetDefinitionsPath) {
        this.blockSetDefinitionsPath = blockSetDefinitionsPath;
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
        ModContainer container = FabricLoader.getInstance().getModContainer(WesterosBlocks.MOD_ID).orElse(null);
        if (container == null) {
            WesterosBlocks.LOGGER.error("Could not find mod container for {}", WesterosBlocks.MOD_ID);
            return;
        }

        // For each root path in the mod jar/directory
        for (Path rootPath : container.getRootPaths()) {
            // Remove leading slash if present and resolve path
            String pathWithoutSlash = blockSetDefinitionsPath.startsWith("/")
                ? blockSetDefinitionsPath.substring(1)
                : blockSetDefinitionsPath;
            Path dirPath = rootPath.resolve(pathWithoutSlash);

            if (Files.exists(dirPath) && Files.isDirectory(dirPath)) {
                WesterosBlocks.LOGGER.debug("Loading block set definitions from: {}", dirPath);
                loadDefinitionsRecursively(dirPath, definitions);
            } else {
                WesterosBlocks.LOGGER.debug("Block set definitions directory not found at: {}", dirPath);
            }
        }

        if (definitions.isEmpty()) {
            WesterosBlocks.LOGGER.warn("No block set definitions were loaded!");
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
