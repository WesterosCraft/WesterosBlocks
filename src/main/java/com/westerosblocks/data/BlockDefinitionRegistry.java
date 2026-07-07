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
    private BlockTagDefinition blockTags;
    private boolean initialized = false;
    private final List<String> duplicateBlockNames = new ArrayList<>();

    private BlockDefinitionRegistry() {
        // LinkedHashMap preserves JSON load order so creative-tab ordering is
        // deterministic and follows the declaration order in WesterosBlocks.json.
        this.definitions = new LinkedHashMap<>();
        this.definitionsByType = new LinkedHashMap<>();
    }

    public static synchronized BlockDefinitionRegistry getInstance() {
        if (instance == null) {
            instance = new BlockDefinitionRegistry();
        }
        return instance;
    }

    public void initialize(String definitionsFilePath) {
        if (initialized) {
            WesterosBlocks.LOGGER.warn("BlockDefinitionRegistry is already initialized. Skipping re-initialization.");
            return;
        }

        ConsolidatedDefinitionLoader loader = new ConsolidatedDefinitionLoader(definitionsFilePath);
        if (!loader.load()) {
            // The whole mod hangs off this one file; a soft failure here would boot with zero
            // blocks and let existing worlds resolve every WesterosBlocks block to air.
            throw new IllegalStateException("Failed to load block definitions from '" + definitionsFilePath
                + "' - see errors above for the cause (missing file, JSON syntax error, or missing section)");
        }

        // Load individual block definitions
        Map<String, BlockDefinition> loadedDefinitions = loader.getBlockDefinitions();

        if (loadedDefinitions.isEmpty()) {
            WesterosBlocks.LOGGER.warn("No individual block definitions were loaded!");
        }

        definitions.putAll(loadedDefinitions);
        loader.validateDefinitions(loadedDefinitions);
        duplicateBlockNames.addAll(loader.getDuplicateNames());

        // Load block set definitions and expand them
        Map<String, BlockSetDefinition> loadedBlockSets = loader.getBlockSetDefinitions();

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
            loader.validateBlockSets(loadedBlockSets);
        } else {
            WesterosBlocks.LOGGER.warn("No block set definitions were loaded!");
        }

        // Group all definitions by type
        definitionsByType.putAll(loader.groupByType(definitions));

        // Load color maps for Polytone integration
        ColorMapLoader colorMapLoader = new ColorMapLoader("definitions/color_maps.json");
        this.colorMaps = colorMapLoader.loadDefinition();
        if (this.colorMaps != null) {
            colorMapLoader.validateDefinition(this.colorMaps);
        }

        // Load block tag seed lists
        BlockTagLoader blockTagLoader = new BlockTagLoader("definitions/block_tags.json");
        this.blockTags = blockTagLoader.loadDefinition();

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

    /** Block names the loaders saw more than once. Used by the world-compatibility guard. */
    public List<String> getDuplicateBlockNames() {
        return Collections.unmodifiableList(duplicateBlockNames);
    }

    public ColorMapDefinition getColorMaps() {
        if (!initialized) {
            throw new IllegalStateException("BlockDefinitionRegistry not initialized!");
        }
        return colorMaps;
    }

    public BlockTagDefinition getBlockTags() {
        if (!initialized) {
            throw new IllegalStateException("BlockDefinitionRegistry not initialized!");
        }
        return blockTags;
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