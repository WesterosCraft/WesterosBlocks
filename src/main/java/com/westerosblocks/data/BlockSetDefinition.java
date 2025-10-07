package com.westerosblocks.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

/**
 * Represents a block set definition loaded from JSON files in block_set_definitions/ directory.
 * Block sets define a base block with multiple variants (solid, stairs, slab, wall, fence, etc.)
 * that share common properties but may have variant-specific overrides.
 *
 * <p>This class mirrors the old ModBlockSet structure and expands to multiple BlockDefinition instances.
 */
public class BlockSetDefinition {
    // === CORE PROPERTIES ===

    /** The base name used to generate variant names (e.g., "arbor_brick" → "arbor_brick_stairs") */
    @SerializedName("baseBlockName")
    private String baseBlockName;

    /** The base label for all variants (e.g., "Medium Ashlar Arbor" → "Medium Ashlar Arbor Stairs") */
    @SerializedName("baseLabel")
    private String baseLabel;

    /** List of variants to create (e.g., ["solid", "stairs", "slab", "wall", "fence"]) */
    @SerializedName("variants")
    private List<String> variants;

    // === OVERRIDE PROPERTIES ===

    /** Alternative names for specific variants (e.g., {"solid": "birch_planks"}) */
    @SerializedName("altNames")
    private Map<String, String> altNames;

    /** Alternative labels for specific variants */
    @SerializedName("altLabels")
    private Map<String, String> altLabels;

    /** Type attributes for specific variants (e.g., {"fence_gate": "locked:true"}) */
    @SerializedName("types")
    private Map<String, String> types;

    /** Custom tags for specific variants (e.g., {"solid": ["wood-ctm"]}) */
    @SerializedName("altCustomTags")
    private Map<String, List<String>> altCustomTags;

    /** Alternative textures for specific variants (e.g., {"solid": ["tex1", "tex2", "tex3"]}) */
    @SerializedName("altTextures")
    private Map<String, List<String>> altTextures;

    // === SHARED BLOCK PROPERTIES ===

    /** Block hardness */
    @SerializedName("hardness")
    private Float hardness;

    /** Sound effect when walking/placing/breaking */
    @SerializedName("stepSound")
    private String stepSound;

    /** Generic material type */
    @SerializedName("material")
    private String material;

    /** Explosion resistance */
    @SerializedName("resistance")
    private Float resistance;

    /** Light opacity (0-15) */
    @SerializedName("lightOpacity")
    private Integer lightOpacity;

    /** Harvest level requirements */
    @SerializedName("harvestLevel")
    private List<BlockDefinition.HarvestLevel> harvestLevel;

    /** Fire spread speed */
    @SerializedName("fireSpreadSpeed")
    private Integer fireSpreadSpeed;

    /** Flammability */
    @SerializedName("flamability")
    private Integer flamability;

    /** Creative tab */
    @SerializedName("creativeTab")
    private String creativeTab;

    /** Custom tags (applied to all variants unless overridden) */
    @SerializedName("customTags")
    private List<String> customTags;

    // === RENDERING PROPERTIES ===

    /** Alpha rendering for transparency */
    @SerializedName("alphaRender")
    private Boolean alphaRender;

    /** Ambient occlusion */
    @SerializedName("ambientOcclusion")
    private Boolean ambientOcclusion;

    /** Non-opaque block */
    @SerializedName("nonOpaque")
    private Boolean nonOpaque;

    /** Render layer - "cutout", "cutout_mipped", or "translucent" */
    @SerializedName("renderLayer")
    private String renderLayer;

    /** Light emission level (0-15) */
    @SerializedName("lightValue")
    private Float lightValue;

    /** Color multiplier */
    @SerializedName("colorMult")
    private String colorMult;

    // === TEXTURE PROPERTIES ===

    /**
     * Texture map for variants.
     * Supports special keys: "all", "sides", "bottom", "top", "west", "east", "south", "north"
     * Also supports variant-specific keys like "arrow-slit", "window-frame", "cover"
     */
    @SerializedName("textures")
    private Map<String, String> textures;

    /** Overlay texture map (for tinted blocks with overlays) */
    @SerializedName("overlayTextures")
    private Map<String, String> overlayTextures;

    /**
     * Random texture variants applied to all variants in this block set.
     * Each entry contains a texture map that will be expanded for the specific variant type.
     */
    @SerializedName("randomTextures")
    private List<RandomTextureEntry> randomTextures;

    /**
     * Random texture entry for block sets.
     * Contains a texture map that will be expanded based on variant requirements.
     */
    public static class RandomTextureEntry {
        @SerializedName("textures")
        private Map<String, String> textures;

        @SerializedName("weight")
        private Integer weight;

        public Map<String, String> getTextures() { return textures; }
        public Integer getWeight() { return weight != null ? weight : 1; }
    }

    // === STATE PROPERTIES (for blocks with multiple states) ===

    /** State records for blocks with multiple states */
    @SerializedName("states")
    private List<StateRecord> states;

    /**
     * State record for block sets with multiple states.
     * Each state can have its own textures and properties.
     */
    public static class StateRecord {
        @SerializedName("stateID")
        private String stateID;

        @SerializedName("excludeVariants")
        private String excludeVariants;

        @SerializedName("lightValue")
        private Float lightValue;

        @SerializedName("colorMult")
        private String colorMult;

        @SerializedName("altTextures")
        private Map<String, List<String>> altTextures;

        @SerializedName("textures")
        private Map<String, String> textures;

        @SerializedName("overlayTextures")
        private Map<String, String> overlayTextures;

        public String getStateID() { return stateID; }
        public String getExcludeVariants() { return excludeVariants; }
        public Float getLightValue() { return lightValue; }
        public String getColorMult() { return colorMult; }
        public Map<String, List<String>> getAltTextures() { return altTextures; }
        public Map<String, String> getTextures() { return textures; }
        public Map<String, String> getOverlayTextures() { return overlayTextures; }
    }

    // === GETTERS ===

    public String getBaseBlockName() { return baseBlockName; }
    public String getBaseLabel() { return baseLabel; }
    public List<String> getVariants() { return variants; }
    public Map<String, String> getAltNames() { return altNames; }
    public Map<String, String> getAltLabels() { return altLabels; }
    public Map<String, String> getTypes() { return types; }
    public Map<String, List<String>> getAltCustomTags() { return altCustomTags; }
    public Map<String, List<String>> getAltTextures() { return altTextures; }

    public Float getHardness() { return hardness; }
    public String getStepSound() { return stepSound; }
    public String getMaterial() { return material; }
    public Float getResistance() { return resistance; }
    public Integer getLightOpacity() { return lightOpacity; }
    public List<BlockDefinition.HarvestLevel> getHarvestLevel() { return harvestLevel; }
    public String getCreativeTab() { return creativeTab; }
    public List<String> getCustomTags() { return customTags; }

    public Boolean getAlphaRender() { return alphaRender; }
    public Boolean getAmbientOcclusion() { return ambientOcclusion; }
    public Boolean getNonOpaque() { return nonOpaque; }
    public String getRenderLayer() { return renderLayer; }
    public Float getLightValue() { return lightValue; }
    public String getColorMult() { return colorMult; }

    public Map<String, String> getTextures() { return textures; }
    public Map<String, String> getOverlayTextures() { return overlayTextures; }
    public List<RandomTextureEntry> getRandomTextures() { return randomTextures; }
    public List<StateRecord> getStates() { return states; }

    // === UTILITY METHODS ===

    public boolean hasVariants() {
        return variants != null && !variants.isEmpty();
    }

    public boolean hasStates() {
        return states != null && !states.isEmpty();
    }

    public boolean hasTextures() {
        return textures != null && !textures.isEmpty();
    }

    @Override
    public String toString() {
        return String.format("BlockSetDefinition{baseBlockName='%s', variants=%d}",
                baseBlockName, variants != null ? variants.size() : 0);
    }
}
