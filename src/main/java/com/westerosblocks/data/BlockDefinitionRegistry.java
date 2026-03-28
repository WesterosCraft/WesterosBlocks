package com.westerosblocks.data;

import com.westerosblocks.WesterosBlocks;

import java.util.*;
import java.util.stream.Collectors;

public class BlockDefinitionRegistry {
    private static BlockDefinitionRegistry instance;
    private final Map<String, BlockDefinition> definitions;
    private final Map<String, List<BlockDefinition>> definitionsByType;
    private List<BlockSetDefinition> blockSetDefinitions = new ArrayList<>();
    private ColorMapDefinition colorMaps;
    private boolean initialized = false;

    private BlockDefinitionRegistry() {
        this.definitions = new HashMap<>();
        this.definitionsByType = new HashMap<>();
    }

    public static synchronized BlockDefinitionRegistry getInstance() {
        if (instance == null) {
            instance = new BlockDefinitionRegistry();
        }
        return instance;
    }

    public void initialize(String blockDefinitionsPath, String blockSetDefinitionsPath) {
        if (initialized) {
            WesterosBlocks.LOGGER.warn("BlockDefinitionRegistry is already initialized. Skipping re-initialization.");
            return;
        }

        // Load individual block definitions
        BlockDefinitionLoader loader = new BlockDefinitionLoader(blockDefinitionsPath);
        Map<String, BlockDefinition> loadedDefinitions = loader.loadAllDefinitions();

        if (loadedDefinitions.isEmpty()) {
            WesterosBlocks.LOGGER.warn("No individual block definitions were loaded!");
        }

        definitions.putAll(loadedDefinitions);
        loader.validateDefinitions(loadedDefinitions);

        // Load block set definitions and expand them
        if (blockSetDefinitionsPath != null) {
            BlockSetDefinitionLoader setLoader = new BlockSetDefinitionLoader(blockSetDefinitionsPath);
            Map<String, BlockSetDefinition> loadedBlockSets = setLoader.loadAllDefinitions();

            if (!loadedBlockSets.isEmpty()) {
                blockSetDefinitions = new ArrayList<>(loadedBlockSets.values());
                int expandedCount = 0;

                for (BlockSetDefinition blockSet : loadedBlockSets.values()) {
                    List<BlockDefinition> expandedDefinitions = BlockSetExpander.expand(blockSet);
                    for (BlockDefinition def : expandedDefinitions) {
                        if (definitions.containsKey(def.getBlockName())) {
                            WesterosBlocks.LOGGER.warn("Block set '{}' generated duplicate block name '{}' - skipping",
                                blockSet.getBaseBlockName(), def.getBlockName());
                        } else {
                            definitions.put(def.getBlockName(), def);
                            expandedCount++;
                        }
                    }
                }

                WesterosBlocks.LOGGER.info("Expanded block sets into {} additional block definitions", expandedCount);
                setLoader.validateDefinitions(loadedBlockSets);
            } else {
                WesterosBlocks.LOGGER.warn("No block set definitions were loaded!");
            }
        }

        // Group all definitions by type
        definitionsByType.putAll(loader.groupByType(definitions));

        // Load color maps for Polytone integration
        ColorMapLoader colorMapLoader = new ColorMapLoader("definitions/color_maps.json");
        this.colorMaps = colorMapLoader.loadDefinition();
        if (this.colorMaps != null) {
            colorMapLoader.validateDefinition(this.colorMaps);
        }

        initialized = true;
        WesterosBlocks.LOGGER.info("BlockDefinitionRegistry initialized with {} total definitions.",
            definitions.size());
    }

    public boolean isInitialized() {
        return initialized;
    }

    public BlockDefinition getDefinition(String blockName) {
        if (!initialized) {
            throw new IllegalStateException("BlockDefinitionRegistry not initialized!");
        }
        return definitions.get(blockName);
    }

    public List<BlockDefinition> getByType(String blockType) {
        if (!initialized) {
            throw new IllegalStateException("BlockDefinitionRegistry not initialized!");
        }
        return definitionsByType.getOrDefault(blockType, Collections.emptyList());
    }

    public Collection<BlockDefinition> getAllDefinitions() {
        if (!initialized) {
            throw new IllegalStateException("BlockDefinitionRegistry not initialized!");
        }
        return Collections.unmodifiableCollection(definitions.values());
    }

    public ColorMapDefinition getColorMaps() {
        if (!initialized) {
            throw new IllegalStateException("BlockDefinitionRegistry not initialized!");
        }
        return colorMaps;
    }

    public List<BlockSetDefinition> getBlockSetDefinitions() {
        if (!initialized) {
            throw new IllegalStateException("BlockDefinitionRegistry not initialized!");
        }
        return Collections.unmodifiableList(blockSetDefinitions);
    }

    public int getCount() {
        if (!initialized) {
            throw new IllegalStateException("BlockDefinitionRegistry not initialized!");
        }
        return definitions.size();
    }

    public Map<String, Integer> getTypeStatistics() {
        if (!initialized) {
            throw new IllegalStateException("BlockDefinitionRegistry not initialized!");
        }
        return definitionsByType.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> entry.getValue().size()
            ));
    }

    public void printStatistics() {
        if (!initialized) {
            WesterosBlocks.LOGGER.warn("Cannot print statistics - BlockDefinitionRegistry not initialized!");
            return;
        }

        WesterosBlocks.LOGGER.info("=== Block Definition Registry Statistics ===");
        WesterosBlocks.LOGGER.info("Total definitions loaded: {}", getCount());
        WesterosBlocks.LOGGER.info("Block types found: {}", definitionsByType.size());

        for (Map.Entry<String, Integer> entry : getTypeStatistics().entrySet()) {
            WesterosBlocks.LOGGER.info("  {}: {} blocks", entry.getKey(), entry.getValue());
        }
    }

    public void clear() {
        definitions.clear();
        definitionsByType.clear();
        initialized = false;
        WesterosBlocks.LOGGER.info("BlockDefinitionRegistry cleared");
    }
}