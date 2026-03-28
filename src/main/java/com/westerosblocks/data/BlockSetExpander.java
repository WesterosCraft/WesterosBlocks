package com.westerosblocks.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.westerosblocks.WesterosBlocks;

import java.util.*;

/**
 * Expands BlockSetDefinition instances into individual BlockDefinition instances.
 * Converts one block set JSON file into multiple block definitions (one per variant).
 *
 */
public class BlockSetExpander {

    // Default variants created if none specified
    static final List<String> DEFAULT_VARIANTS = Arrays.asList(
            "solid", "stairs", "slab", "wall", "fence", "hopper"
    );

    // All supported variant types
    static final List<String> SUPPORTED_VARIANTS = Arrays.asList(
            "solid", "stairs", "slab", "wall", "fence", "hopper", "tip",
            "carpet", "fence_gate", "half_door", "cover", "hollow_hopper",
            "log", "directional", "layer", "pane", "sand", "path",
            "window_frame", "window_frame_mullion",
            "arrow_slit", "arrow_slit_window", "arrow_slit_ornate",
            "bench"
    );

    // Maps variant names to block types
    private static final Map<String, String> VARIANT_TYPES = new HashMap<>();
    static {
        VARIANT_TYPES.put("stairs", "stair");
        VARIANT_TYPES.put("hopper", "cuboid");
        VARIANT_TYPES.put("tip", "cuboid");
        VARIANT_TYPES.put("carpet", "cuboid");
        VARIANT_TYPES.put("fence_gate", "fencegate");
        VARIANT_TYPES.put("half_door", "cuboid-nsew");
        VARIANT_TYPES.put("cover", "rail");
        VARIANT_TYPES.put("hollow_hopper", "cuboid");
        VARIANT_TYPES.put("directional", "cuboid-nsew");
        VARIANT_TYPES.put("path", "cuboid");
        VARIANT_TYPES.put("window_frame", "solid");
        VARIANT_TYPES.put("window_frame_mullion", "solid");
        VARIANT_TYPES.put("arrow_slit", "solid");
        VARIANT_TYPES.put("arrow_slit_window", "solid");
        VARIANT_TYPES.put("arrow_slit_ornate", "solid");
        // For other variants, blockType = variant name
    }

    // Maps variant names to required texture keys
    private static final Map<String, String[]> VARIANT_TEXTURES = new HashMap<>();
    static {
        VARIANT_TEXTURES.put("solid", new String[]{"bottom", "top", "west", "east", "south", "north"});
        VARIANT_TEXTURES.put("stairs", new String[]{"bottom", "top", "sides"});
        VARIANT_TEXTURES.put("slab", new String[]{"bottom", "top", "sides"});
        VARIANT_TEXTURES.put("wall", new String[]{"bottom", "top", "sides"});
        VARIANT_TEXTURES.put("fence", new String[]{"bottom", "top", "sides"});
        VARIANT_TEXTURES.put("hopper", new String[]{"sides"});
        VARIANT_TEXTURES.put("tip", new String[]{"sides"});
        VARIANT_TEXTURES.put("carpet", new String[]{"sides"});
        VARIANT_TEXTURES.put("fence_gate", new String[]{"sides"});
        VARIANT_TEXTURES.put("half_door", new String[]{"sides"});
        VARIANT_TEXTURES.put("cover", new String[]{"cover"});
        VARIANT_TEXTURES.put("hollow_hopper", new String[]{"sides"});
        VARIANT_TEXTURES.put("log", new String[]{"bottom", "top", "sides"});
        VARIANT_TEXTURES.put("directional", new String[]{"bottom", "top", "west", "east", "south", "north"});
        VARIANT_TEXTURES.put("layer", new String[]{"sides"});
        VARIANT_TEXTURES.put("pane", new String[]{"sides", "top"});
        VARIANT_TEXTURES.put("sand", new String[]{"bottom", "top", "west", "east", "south", "north"});
        VARIANT_TEXTURES.put("path", new String[]{"sides"});
        VARIANT_TEXTURES.put("window_frame", new String[]{"window-topbottom", "window-topbottom", "window-frame"});
        VARIANT_TEXTURES.put("window_frame_mullion", new String[]{"window-topbottom", "window-topbottom", "window-frame-mullion"});
        VARIANT_TEXTURES.put("arrow_slit", new String[]{"window-topbottom", "window-topbottom", "arrow-slit"});
        VARIANT_TEXTURES.put("arrow_slit_window", new String[]{"window-topbottom", "window-topbottom", "arrow-slit-window"});
        VARIANT_TEXTURES.put("arrow_slit_ornate", new String[]{"window-topbottom", "window-topbottom", "arrow-slit-ornate"});
        VARIANT_TEXTURES.put("bench", new String[]{"sides"});
    }

    public static List<BlockDefinition> expand(BlockSetDefinition blockSet) {
        List<BlockDefinition> definitions = new ArrayList<>();

        // Preprocess maps to handle comma-separated keys
        Map<String, String> options = preprocessVariantMap(blockSet.getOptions());
        Map<String, List<String>> altCustomTags = preprocessVariantMap(blockSet.getAltCustomTags());
        Map<String, List<String>> altTextures = preprocessVariantMap(blockSet.getAltTextures());

        // Determine which variants to create
        List<String> variantsToCreate = blockSet.hasVariants() ? blockSet.getVariants() : DEFAULT_VARIANTS;

        for (String variant : variantsToCreate) {
            if (!SUPPORTED_VARIANTS.contains(variant)) {
                WesterosBlocks.LOGGER.warn("Unsupported variant '{}' in block set '{}'", variant, blockSet.getBaseBlockName());
                continue;
            }

            BlockDefinition definition = createVariantDefinition(blockSet, variant, options, altCustomTags, altTextures);
            if (definition != null) {
                definitions.add(definition);
            }
        }

        WesterosBlocks.LOGGER.debug("Expanded block set '{}' into {} variants", blockSet.getBaseBlockName(), definitions.size());
        return definitions;
    }

    /**
     * Creates a BlockDefinition for a specific variant.
     */
    private static BlockDefinition createVariantDefinition(BlockSetDefinition blockSet, String variant,
                                                          Map<String, String> options,
                                                          Map<String, List<String>> altCustomTags,
                                                          Map<String, List<String>> altTextures) {
        // Use reflection to create BlockDefinition (since it has no public constructor)
        // We'll create a JSON string and parse it with Gson
        Map<String, Object> defMap = new HashMap<>();

        // 1. Derive block name
        String blockName = deriveBlockName(blockSet, variant);
        defMap.put("blockName", blockName);

        // 2. Derive block type
        String blockType = VARIANT_TYPES.getOrDefault(variant, variant);
        defMap.put("blockType", blockType);

        // 3. Derive label
        String label = deriveLabel(blockSet, variant);
        defMap.put("label", label);

        // 4. Copy common properties
        if (blockSet.getHardness() != null) defMap.put("hardness", blockSet.getHardness());
        if (blockSet.getSoundGroup() != null) defMap.put("soundGroup", blockSet.getSoundGroup());
        if (blockSet.getResistance() != null) defMap.put("resistance", blockSet.getResistance());
        if (blockSet.getLightOpacity() != null) defMap.put("lightOpacity", blockSet.getLightOpacity());
        if (blockSet.getHarvestLevel() != null) defMap.put("harvestLevel", blockSet.getHarvestLevel());
        if (blockSet.getCreativeTab() != null) defMap.put("creativeTab", blockSet.getCreativeTab());
        if (blockSet.getAlphaRender() != null) defMap.put("alphaRender", blockSet.getAlphaRender());
        if (blockSet.getNonOpaque() != null) defMap.put("nonOpaque", blockSet.getNonOpaque());
        if (blockSet.getRenderLayer() != null) defMap.put("renderLayer", blockSet.getRenderLayer());
        if (blockSet.getColorMult() != null) defMap.put("colorMult", blockSet.getColorMult());

        // 5. Handle custom tags
        if (altCustomTags != null && altCustomTags.containsKey(variant)) {
            List<String> tags = altCustomTags.get(variant);
            if (!tags.isEmpty()) {
                defMap.put("customTags", tags);
            }
        } else if (blockSet.getCustomTags() != null) {
            defMap.put("customTags", blockSet.getCustomTags());
        }

        // 6. Handle options attribute
        if (options != null && options.containsKey(variant)) {
            // User-specified options (string that will be parsed by OptionsPropertiesDeserializer)
            defMap.put("options", options.get(variant));
        } else {
            // Apply default options for specific variants
            OptionsProperties defaultOptions = getDefaultOptions(variant);
            if (defaultOptions != null) {
                defMap.put("options", convertOptionsPropertiesToMap(defaultOptions));
            }
        }

        // 7. Handle textures
        List<String> textures = pickVariantTextures(blockSet.getTextures(), altTextures, variant);
        if (textures != null && !textures.isEmpty()) {
            defMap.put("textures", textures);
        }

        // 7b. Handle random textures
        if (blockSet.getRandomTextures() != null && !blockSet.getRandomTextures().isEmpty()) {
            List<Map<String, Object>> randomTexturesList = new ArrayList<>();
            for (BlockSetDefinition.RandomTextureEntry entry : blockSet.getRandomTextures()) {
                // Process the texture map for this variant
                Map<String, String> processedMap = preprocessTextureMap(entry.getTextures());
                List<String> variantTextures = getTexturesForVariant(processedMap, variant);

                if (variantTextures != null && !variantTextures.isEmpty()) {
                    Map<String, Object> randomTextureMap = new HashMap<>();
                    randomTextureMap.put("textures", variantTextures);
                    randomTextureMap.put("weight", entry.getWeight());
                    randomTexturesList.add(randomTextureMap);
                }
            }

            if (!randomTexturesList.isEmpty()) {
                defMap.put("randomTextures", randomTexturesList);
            }
        }

        // 8. Handle states if present
        if (blockSet.hasStates()) {
            List<Map<String, Object>> statesList = new ArrayList<>();

            for (BlockSetDefinition.StateRecord stateRec : blockSet.getStates()) {
                // Check if this variant should be excluded from this state
                if (isVariantExcluded(variant, stateRec.getExcludeVariants())) {
                    continue;
                }

                // Create state definition map
                Map<String, Object> stateMap = new HashMap<>();
                stateMap.put("stateID", stateRec.getStateID());

                // Process state textures (expand "all" and "sides", pick for variant)
                List<String> stateTextures = pickVariantTextures(
                    stateRec.getTextures(),
                    stateRec.getAltTextures(),
                    variant
                );
                if (stateTextures != null && !stateTextures.isEmpty()) {
                    stateMap.put("textures", stateTextures);
                }

                // Process state overlay textures if present
                if (stateRec.getOverlayTextures() != null && !stateRec.getOverlayTextures().isEmpty()) {
                    Map<String, String> overlayMap = preprocessTextureMap(stateRec.getOverlayTextures());
                    List<String> overlayTextures = getTexturesForVariant(overlayMap, variant);
                    if (overlayTextures != null && !overlayTextures.isEmpty()) {
                        stateMap.put("overlayTextures", overlayTextures);
                    }
                }

                // Copy other state properties
                if (stateRec.getLightValue() != null) {
                    stateMap.put("luminance", stateRec.getLightValue().intValue());
                }
                if (stateRec.getColorMult() != null) {
                    stateMap.put("colorMult", stateRec.getColorMult());
                }

                statesList.add(stateMap);
            }

            // Only add states if 2+ remain after exclusions
            if (statesList.size() >= 2) {
                defMap.put("states", statesList);

                // Add toggleOnUse property automatically when states are present
                defMap.put("toggleOnUse", true);
            }
        }

        // 9. Handle special geometry for certain variants
        addSpecialGeometry(defMap, variant);

        // Convert map to JSON and then to BlockDefinition
        return convertMapToBlockDefinition(defMap);
    }

    /**
     * Derives the block name for a variant.
     */
    private static String deriveBlockName(BlockSetDefinition blockSet, String variant) {
        // Check for alternative name
        if (blockSet.getAltNames() != null && blockSet.getAltNames().containsKey(variant)) {
            return blockSet.getAltNames().get(variant);
        }

        // Generate name from base name and variant
        String suffix = variant.equals("solid") ? "" : "_" + variant;
        return blockSet.getBaseBlockName() + suffix;
    }

    /**
     * Derives the label for a variant.
     */
    private static String deriveLabel(BlockSetDefinition blockSet, String variant) {
        // Check for alternative label
        if (blockSet.getAltLabels() != null && blockSet.getAltLabels().containsKey(variant)) {
            return blockSet.getAltLabels().get(variant);
        }

        // Generate label from base label and variant
        if (blockSet.getBaseLabel() != null) {
            String suffixLabel = variant.equals("solid") ? "" : " " + generateLabel(variant);
            return (blockSet.getBaseLabel() + suffixLabel).trim();
        }

        // Fallback: generate from block name
        return generateLabel(deriveBlockName(blockSet, variant));
    }

    /**
     * Generates a human-readable label from a name (snake_case → Title Case).
     */
    private static String generateLabel(String name) {
        if (name == null || name.isEmpty() || name.equals("_")) {
            return "";
        }

        String[] words = name.split("_");
        StringBuilder label = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                String wordCap = word.substring(0, 1).toUpperCase() + word.substring(1);
                label.append(wordCap).append(" ");
            }
        }
        return label.toString().trim();
    }

    /**
     * Preprocesses a map to handle comma-separated keys (e.g., "solid,stairs" → two entries).
     */
    private static <T> Map<String, T> preprocessVariantMap(Map<String, T> map) {
        if (map == null) return null;

        Map<String, T> newMap = new HashMap<>();
        for (Map.Entry<String, T> entry : map.entrySet()) {
            String key = entry.getKey();
            T value = entry.getValue();
            if (!key.contains(",")) {
                newMap.put(key, value);
            } else {
                // Split comma-separated keys and add each one
                String[] keys = key.split(",");
                for (String k : keys) {
                    newMap.put(k.trim(), value);
                }
            }
        }
        return newMap;
    }

    /**
     * Picks textures for a specific variant from the texture map.
     */
    private static List<String> pickVariantTextures(Map<String, String> textures,
                                                    Map<String, List<String>> altTextures,
                                                    String variant) {
        // Check for variant-specific override first
        if (altTextures != null && altTextures.containsKey(variant)) {
            return altTextures.get(variant);
        }

        // Use base textures
        return getTexturesForVariant(preprocessTextureMap(textures), variant);
    }

    /**
     * Preprocesses texture map to expand "all" and "sides" keys.
     */
    private static Map<String, String> preprocessTextureMap(Map<String, String> textureMap) {
        if (textureMap == null) return null;

        Map<String, String> processed = new HashMap<>(textureMap);

        // Expand "all" to all faces
        if (processed.containsKey("all")) {
            String allTexture = processed.get("all");
            processed.putIfAbsent("bottom", allTexture);
            processed.putIfAbsent("top", allTexture);
            processed.putIfAbsent("sides", allTexture);
        }

        // Expand "sides" to individual directions
        if (processed.containsKey("sides")) {
            String sidesTexture = processed.get("sides");
            processed.putIfAbsent("west", sidesTexture);
            processed.putIfAbsent("east", sidesTexture);
            processed.putIfAbsent("south", sidesTexture);
            processed.putIfAbsent("north", sidesTexture);
        }

        // Fallback: if sides not specified but bottom is, use bottom for sides
        if (!processed.containsKey("sides") && processed.containsKey("bottom")) {
            processed.put("sides", processed.get("bottom"));
        }

        // Special texture fallbacks
        processed.putIfAbsent("window-topbottom", "transparent");
        if (processed.containsKey("sides") && !processed.containsKey("cover")) {
            processed.put("cover", processed.get("sides"));
        }

        return processed;
    }

    /**
     * Extracts texture list for a specific variant from the processed texture map.
     */
    private static List<String> getTexturesForVariant(Map<String, String> textureMap, String variant) {
        if (textureMap == null || !VARIANT_TEXTURES.containsKey(variant)) {
            return null;
        }

        List<String> textureList = new ArrayList<>();
        String[] requiredKeys = VARIANT_TEXTURES.get(variant);

        for (String key : requiredKeys) {
            if (textureMap.containsKey(key)) {
                textureList.add(textureMap.get(key));
            }
        }

        return !textureList.isEmpty() ? textureList : null;
    }

    /**
     * Returns default OptionsProperties for specific variants.
     */
    private static OptionsProperties getDefaultOptions(String variant) {
        OptionsProperties props = new OptionsProperties();
        switch (variant) {
            case "stairs", "wall", "fence", "pane" -> props.setUnconnect(false);
            case "arrow_slit", "arrow_slit_window", "arrow_slit_ornate",
                 "window_frame", "window_frame_mullion" -> props.setConnectstate(true);
            case "cover" -> props.setAllowUnsupported(true);
            default -> { return null; }
        }
        return props;
    }

    /**
     * Converts OptionsProperties to a Map for JSON serialization.
     * Only includes non-null values.
     */
    private static Map<String, Object> convertOptionsPropertiesToMap(OptionsProperties props) {
        if (props == null) {
            return null;
        }

        Map<String, Object> map = new HashMap<>();

        if (props.getUnconnect() != null) map.put("unconnect", props.getUnconnect());
        if (props.getConnectstate() != null) map.put("connectstate", props.getConnectstate());
        if (props.getNoUvlock() != null) map.put("noUvlock", props.getNoUvlock());
        if (props.getBarsModel() != null) map.put("barsModel", props.getBarsModel());
        if (props.getLegacyModel() != null) map.put("legacyModel", props.getLegacyModel());
        if (props.getNoDecay() != null) map.put("noDecay", props.getNoDecay());
        if (props.getBetterFoliage() != null) map.put("betterFoliage", props.getBetterFoliage());
        if (props.getOverlay() != null) map.put("overlay", props.getOverlay());
        if (props.getAllowUnsupported() != null) map.put("allowUnsupported", props.getAllowUnsupported());
        if (props.getNoParticle() != null) map.put("noParticle", props.getNoParticle());
        if (props.getLocked() != null) map.put("locked", props.getLocked());
        if (props.getAlwaysOn() != null) map.put("alwaysOn", props.getAlwaysOn());
        if (props.getPlantId() != null) map.put("plantId", props.getPlantId());
        if (props.getNoInWeb() != null) map.put("noInWeb", props.getNoInWeb());
        if (props.getNoClimb() != null) map.put("noClimb", props.getNoClimb());
        if (props.getToggleOnUse() != null) map.put("toggleOnUse", props.getToggleOnUse());
        if (props.getLayerSensitive() != null) map.put("layerSensitive", props.getLayerSensitive());
        if (props.getSymmetrical() != null) map.put("symmetrical", props.getSymmetrical());

        return map.isEmpty() ? null : map;
    }

    /**
     * Checks if a variant should be excluded based on the excludeVariants string.
     * @param variant The variant to check (e.g., "solid", "window_frame")
     * @param excludeVariants Comma-separated list of variants to exclude (e.g., "window_frame,window_frame_mullion")
     * @return true if the variant is in the exclusion list, false otherwise
     */
    private static boolean isVariantExcluded(String variant, String excludeVariants) {
        if (excludeVariants == null || excludeVariants.isEmpty()) {
            return false;
        }
        String[] excluded = excludeVariants.split(",");
        for (String ex : excluded) {
            if (ex.trim().equals(variant)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds special geometry (cuboids, bounding boxes) for variants that need them.
     */
    private static void addSpecialGeometry(Map<String, Object> defMap, String variant) {
        switch (variant) {
            case "hopper" -> addHopperGeometry(defMap);
            case "tip" -> addTipGeometry(defMap);
            case "carpet" -> addCarpetGeometry(defMap);
            case "half_door" -> addHalfDoorGeometry(defMap);
            case "hollow_hopper" -> addHollowHopperGeometry(defMap);
            case "directional" -> addDirectionalGeometry(defMap);
            case "path" -> addPathGeometry(defMap);
            case "arrow_slit", "arrow_slit_window", "arrow_slit_ornate",
                 "window_frame", "window_frame_mullion" -> addWindowGeometry(defMap);
        }
    }

    private static void addHopperGeometry(Map<String, Object> defMap) {
        // Mark as non-opaque
        defMap.put("nonOpaque", true);
        defMap.put("lightOpacity", 0);

        // Add hopper cuboid geometry
        List<Map<String, Object>> cuboids = new ArrayList<>();

        // Bottom spout
        Map<String, Object> cuboid1 = new HashMap<>();
        cuboid1.put("xMin", 0.3755);
        cuboid1.put("yMin", 0.0);
        cuboid1.put("zMin", 0.3755);
        cuboid1.put("xMax", 0.6245);
        cuboid1.put("yMax", 0.275);
        cuboid1.put("zMax", 0.6245);
        cuboid1.put("sideTextures", new int[]{0, 0, 0, 0, 0, 0});
        cuboids.add(cuboid1);

        // Middle section
        Map<String, Object> cuboid2 = new HashMap<>();
        cuboid2.put("xMin", 0.25);
        cuboid2.put("yMin", 0.275);
        cuboid2.put("zMin", 0.25);
        cuboid2.put("xMax", 0.75);
        cuboid2.put("yMax", 0.625);
        cuboid2.put("zMax", 0.75);
        cuboid2.put("sideTextures", new int[]{0, 0, 0, 0, 0, 0});
        cuboids.add(cuboid2);

        // Top rim
        Map<String, Object> cuboid3 = new HashMap<>();
        cuboid3.put("xMin", 0.0);
        cuboid3.put("yMin", 0.625);
        cuboid3.put("zMin", 0.0);
        cuboid3.put("xMax", 1.0);
        cuboid3.put("yMax", 1.0);
        cuboid3.put("zMax", 1.0);
        cuboid3.put("sideTextures", new int[]{0, 0, 0, 0, 0, 0});
        cuboids.add(cuboid3);

        defMap.put("cuboids", cuboids);
    }

    private static void addTipGeometry(Map<String, Object> defMap) {
        defMap.put("nonOpaque", true);
        defMap.put("lightOpacity", 0);

        // Add tip cuboid geometry (inverted hopper)
        List<Map<String, Object>> cuboids = new ArrayList<>();

        // Top spout
        Map<String, Object> cuboid1 = new HashMap<>();
        cuboid1.put("xMin", 0.3755);
        cuboid1.put("yMin", 0.625);
        cuboid1.put("zMin", 0.3755);
        cuboid1.put("xMax", 0.6245);
        cuboid1.put("yMax", 1.0);
        cuboid1.put("zMax", 0.6245);
        cuboid1.put("sideTextures", new int[]{0, 0, 0, 0, 0, 0});
        cuboids.add(cuboid1);

        // Middle section
        Map<String, Object> cuboid2 = new HashMap<>();
        cuboid2.put("xMin", 0.25);
        cuboid2.put("yMin", 0.275);
        cuboid2.put("zMin", 0.25);
        cuboid2.put("xMax", 0.75);
        cuboid2.put("yMax", 0.625);
        cuboid2.put("zMax", 0.75);
        cuboid2.put("sideTextures", new int[]{0, 0, 0, 0, 0, 0});
        cuboids.add(cuboid2);

        // Bottom base
        Map<String, Object> cuboid3 = new HashMap<>();
        cuboid3.put("xMin", 0.0);
        cuboid3.put("yMin", 0.0);
        cuboid3.put("zMin", 0.0);
        cuboid3.put("xMax", 1.0);
        cuboid3.put("yMax", 0.275);
        cuboid3.put("zMax", 1.0);
        cuboid3.put("sideTextures", new int[]{0, 0, 0, 0, 0, 0});
        cuboids.add(cuboid3);

        defMap.put("cuboids", cuboids);
    }

    private static void addCarpetGeometry(Map<String, Object> defMap) {
        defMap.put("nonOpaque", true);
        defMap.put("lightOpacity", 0);

        // Add carpet cuboid geometry (thin layer)
        List<Map<String, Object>> cuboids = new ArrayList<>();

        Map<String, Object> cuboid = new HashMap<>();
        cuboid.put("xMin", 0.0);
        cuboid.put("yMin", 0.0);
        cuboid.put("zMin", 0.0);
        cuboid.put("xMax", 1.0);
        cuboid.put("yMax", 0.0625);
        cuboid.put("zMax", 1.0);
        cuboid.put("sideTextures", new int[]{0, 0, 0, 0, 0, 0});
        cuboids.add(cuboid);

        defMap.put("cuboids", cuboids);
    }

    private static void addHalfDoorGeometry(Map<String, Object> defMap) {
        // Add bounding box for half door
        Map<String, Double> boundingBox = new HashMap<>();
        boundingBox.put("xMin", 0.0);
        boundingBox.put("yMin", 0.0);
        boundingBox.put("zMin", 0.0);
        boundingBox.put("xMax", 0.1875);
        boundingBox.put("yMax", 1.0);
        boundingBox.put("zMax", 1.0);
        defMap.put("boundingBox", boundingBox);
    }

    private static void addHollowHopperGeometry(Map<String, Object> defMap) {
        defMap.put("nonOpaque", true);
        defMap.put("lightOpacity", 0);

        List<Map<String, Object>> cuboids = new ArrayList<>();

        // Bottom spout
        Map<String, Object> cuboid1 = new HashMap<>();
        cuboid1.put("xMin", 0.3755);
        cuboid1.put("yMin", 0.16);
        cuboid1.put("zMin", 0.3755);
        cuboid1.put("xMax", 0.6245);
        cuboid1.put("yMax", 0.275);
        cuboid1.put("zMax", 0.6245);
        cuboid1.put("sideTextures", new int[]{0, 0, 0, 0, 0, 0});
        cuboids.add(cuboid1);

        // Middle section
        Map<String, Object> cuboid2 = new HashMap<>();
        cuboid2.put("xMin", 0.25);
        cuboid2.put("yMin", 0.275);
        cuboid2.put("zMin", 0.25);
        cuboid2.put("xMax", 0.75);
        cuboid2.put("yMax", 0.625);
        cuboid2.put("zMax", 0.75);
        cuboid2.put("sideTextures", new int[]{0, 0, 0, 0, 0, 0});
        cuboids.add(cuboid2);

        // Base plate
        Map<String, Object> cuboid3 = new HashMap<>();
        cuboid3.put("xMin", 0.0);
        cuboid3.put("yMin", 0.625);
        cuboid3.put("zMin", 0.0);
        cuboid3.put("xMax", 1.0);
        cuboid3.put("yMax", 0.65);
        cuboid3.put("zMax", 1.0);
        cuboid3.put("sideTextures", new int[]{0, 0, 0, 0, 0, 0});
        cuboids.add(cuboid3);

        // West wall
        Map<String, Object> cuboid4 = new HashMap<>();
        cuboid4.put("xMin", 0.0);
        cuboid4.put("yMin", 0.625);
        cuboid4.put("zMin", 0.0);
        cuboid4.put("xMax", 0.125);
        cuboid4.put("yMax", 1.0);
        cuboid4.put("zMax", 1.0);
        cuboid4.put("sideTextures", new int[]{0, 0, 0, 0, 0, 0});
        cuboids.add(cuboid4);

        // East wall
        Map<String, Object> cuboid5 = new HashMap<>();
        cuboid5.put("xMin", 0.875);
        cuboid5.put("yMin", 0.625);
        cuboid5.put("zMin", 0.0);
        cuboid5.put("xMax", 1.0);
        cuboid5.put("yMax", 1.0);
        cuboid5.put("zMax", 1.0);
        cuboid5.put("sideTextures", new int[]{0, 0, 0, 0, 0, 0});
        cuboids.add(cuboid5);

        // North wall
        Map<String, Object> cuboid6 = new HashMap<>();
        cuboid6.put("xMin", 0.0);
        cuboid6.put("yMin", 0.625);
        cuboid6.put("zMin", 0.0);
        cuboid6.put("xMax", 1.0);
        cuboid6.put("yMax", 1.0);
        cuboid6.put("zMax", 0.125);
        cuboid6.put("sideTextures", new int[]{0, 0, 0, 0, 0, 0});
        cuboids.add(cuboid6);

        // South wall
        Map<String, Object> cuboid7 = new HashMap<>();
        cuboid7.put("xMin", 0.0);
        cuboid7.put("yMin", 0.625);
        cuboid7.put("zMin", 0.875);
        cuboid7.put("xMax", 1.0);
        cuboid7.put("yMax", 1.0);
        cuboid7.put("zMax", 1.0);
        cuboid7.put("sideTextures", new int[]{0, 0, 0, 0, 0, 0});
        cuboids.add(cuboid7);

        defMap.put("cuboids", cuboids);
    }

    private static void addDirectionalGeometry(Map<String, Object> defMap) {
        // Full cube for directional blocks
        List<Map<String, Object>> cuboids = new ArrayList<>();

        Map<String, Object> cuboid = new HashMap<>();
        cuboid.put("xMin", 0.0);
        cuboid.put("yMin", 0.0);
        cuboid.put("zMin", 0.0);
        cuboid.put("xMax", 1.0);
        cuboid.put("yMax", 1.0);
        cuboid.put("zMax", 1.0);
        cuboids.add(cuboid);

        defMap.put("cuboids", cuboids);
    }

    private static void addPathGeometry(Map<String, Object> defMap) {
        defMap.put("nonOpaque", true);
        defMap.put("lightOpacity", 0);

        List<Map<String, Object>> cuboids = new ArrayList<>();

        Map<String, Object> cuboid = new HashMap<>();
        cuboid.put("xMin", 0.0);
        cuboid.put("yMin", 0.0);
        cuboid.put("zMin", 0.0);
        cuboid.put("xMax", 1.0);
        cuboid.put("yMax", 0.9375);
        cuboid.put("zMax", 1.0);
        cuboid.put("sideTextures", new int[]{0, 0, 0, 0, 0, 0});
        cuboids.add(cuboid);

        defMap.put("cuboids", cuboids);
    }

    private static void addWindowGeometry(Map<String, Object> defMap) {
        defMap.put("nonOpaque", true);
        defMap.put("lightOpacity", 0);
        defMap.put("renderLayer", "cutout");
        // Collision boxes and support boxes would be added here
    }

    /**
     * Converts a map to a BlockDefinition using Gson.
     * Calls doInit() to normalize the definition after creation.
     */
    private static BlockDefinition convertMapToBlockDefinition(Map<String, Object> defMap) {
        Gson gson = new GsonBuilder()
            .registerTypeAdapter(OptionsProperties.class, new OptionsPropertiesDeserializer())
            .create();
        String json = gson.toJson(defMap);
        BlockDefinition definition = gson.fromJson(json, BlockDefinition.class);

        // Initialize the definition to normalize textures and create synthetic base state
        if (definition != null) {
            definition.doInit();
        }

        return definition;
    }
}
