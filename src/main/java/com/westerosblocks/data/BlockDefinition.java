package com.westerosblocks.data;

import com.google.gson.annotations.SerializedName;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.sound.BlockSoundGroup;

import java.util.*;
import java.util.function.UnaryOperator;

/**
 * Represents a block definition loaded from JSON files in definitions/block_definitions directory.
 * These definitions are used to automatically register blocks with their properties.
 */
public class BlockDefinition {
    /** The unique identifier for this block (e.g., "oak_table", "stone_wall") */
    @SerializedName("blockName")
    private String blockName;

    /** The block type determines which class to use (e.g., "solid", "door", "wall", "slab") */
    @SerializedName("blockType")
    private String blockType;

    /** Sound effect when walking/placing/breaking (e.g., "wood", "stone", "metal", "grass") */
    @SerializedName("soundGroup")
    private String soundGroup;

    /** The blast resistance of the block. */
    @SerializedName("resistance")
    private float resistance;

    /** The hardness of the block. */
    @SerializedName("hardness")
    private float hardness;

    /** Shorthand property that sets both hardness and resistance to the same value */
    @SerializedName("strength")
    private Float strength;

    /** Material type (e.g., "iron", "rock", "wood") - affects various block behaviors */
    @Deprecated
    @SerializedName("material")
    private String material;

    /** Which creative mode tab to place this block in */
    @SerializedName("creativeTab")
    private String creativeTab;

    /** Display name shown in-game (e.g., "Oak Table", "Stone Wall") */
    @SerializedName("label")
    private String label;

    // === VISUAL PROPERTIES ===

    /** Texture file paths (relative to textures/block/) - order varies by block type */
    @SerializedName("textures")
    private List<String> textures;

    /** Legacy type field - mostly replaced by specific properties */
    @SerializedName("type")
    private String type;

    /** Render layer - "cutout", "cutout_mipped", or "translucent" */
    @SerializedName("renderLayer")
    private String renderLayer;

    /** Multiple texture variants with weights for random selection */
    @SerializedName("randomTextures")
    private List<RandomTextureVariant> randomTextures;

    /** Different texture sets for different block states */
    @SerializedName("states")
    private List<StateVariant> states;

    /** Additional overlay textures (for tinted blocks) */
    @SerializedName("overlayTextures")
    private List<String> overlayTextures;

    /** Hex color for texture tinting (e.g., "#FF0000" for red) */
    @SerializedName("colorMult")
    private String colorMult;

    /** Array of colormap paths for multi-colormap tinting (e.g., ["textures/colormap/grass", "textures/colormap/birch"]) */
    @SerializedName("colorMults")
    private List<String> colorMults;

    /** Custom texture path for item form (overrides default block texture) */
    @SerializedName("itemTexture")
    private String itemTexture;

    /** Which texture index to use for item rendering (when block has multiple textures) */
    @SerializedName("itemTextureIndex")
    private Integer itemTextureIndex;

    /** GUI transformation settings for item display in inventory */
    @SerializedName("display")
    private DisplaySettings display;

    /** Tooltip text lines shown when hovering over the block in inventory */
    @SerializedName("tooltips")
    private List<String> tooltips;

    /** Light level emitted by block (0-15) */
    @SerializedName("luminance")
    private Integer luminance;

    // === BEHAVIOR PROPERTIES ===

    /** Tools required to break this block efficiently */
    @SerializedName("harvestLevel")
    private List<HarvestLevel> harvestLevel;

    /** Block requires correct tool to harvest (drops nothing without correct tool) */
    @SerializedName("requiresTool")
    private Boolean requiresTool;

    /** How much light the block blocks (0 = transparent, 15 = fully opaque) */
    @SerializedName("lightOpacity")
    private Integer lightOpacity;

    /** Array of collision boxes for complex collision shapes */
    @SerializedName("collisionBoxes")
    private List<BoundingBox> collisionBoxes;

    /** Block is transparent/non-opaque (lets light through) */
    @SerializedName("nonOpaque")
    private Boolean nonOpaque;

    /** Block has no collision box (can walk through) */
    @SerializedName("noCollision")
    private Boolean noCollision;

    /** Plants only: breaks when supporting layer changes */
    @SerializedName("layerSensitive")
    private Boolean layerSensitive;

    /** Block can be placed in unsupported locations */
    @SerializedName("allowUnsupported")
    private Boolean allowUnsupported;

    /** Stack blocks: allows top half to be broken independently */
    @SerializedName("allowHalfBreak")
    private Boolean allowHalfBreak;

    /** Creative players can cycle states by right-clicking */
    @SerializedName("toggleOnUse")
    private Boolean toggleOnUse;

    /** Doors/gates are locked and cannot be opened */
    @SerializedName("locked")
    private Boolean locked;

    /** Torches don't emit particles */
    @SerializedName("noParticle")
    private Boolean noParticle;

    /** Furnaces are always lit (no off state) */
    @SerializedName("alwaysOn")
    private Boolean alwaysOn;

    /** Vines are climbable like ladders */
    @SerializedName("hasClimb")
    private Boolean hasClimb;

    /** Vines can grow downward */
    @SerializedName("hasDown")
    private Boolean hasDown;

    /** What block material to connect to (e.g., "material" for webs) */
    @SerializedName("connectTo")
    private String connectTo;

    /** Block has symmetrical texture mapping (mirrors textures) */
    @SerializedName("symmetrical")
    private Boolean symmetrical;

    // === RENDERING/MODEL PROPERTIES ===

    /** Uses custom model files instead of generated ones */
    @SerializedName("isCustomModel")
    private Boolean isCustomModel;

    /** Block uses tinted rendering (grass-like color variation) */
    @SerializedName("isTinted")
    private Boolean isTinted;

    /** Block has overlay textures for tinting */
    @SerializedName("hasOverlay")
    private Boolean hasOverlay;

    /** OptiFine Better Foliage support */
    @SerializedName("hasBetterFoliage")
    private Boolean hasBetterFoliage;

    /** OptiFine Better Foliage support (alternate field name) */
    @SerializedName("betterFoliage")
    private Boolean betterFoliage;

    /** Leaves should not decay */
    @SerializedName("noDecay")
    private Boolean noDecay;

    /** Randomly rotate block models */
    @SerializedName("hasRotateRandom")
    private Boolean hasRotateRandom;

    /** Randomly rotate block on placement (Y-axis rotation) */
    @SerializedName("rotateRandom")
    private Boolean rotateRandom;

    /** Block uses alpha/translucent rendering */
    @SerializedName("alphaRender")
    private Boolean alphaRender;

    // === BLOCK-SPECIFIC PROPERTIES ===

    /** Legacy model for pane blocks */
    @SerializedName("isLegacyModel")
    private String isLegacyModel;

    /** Bed type for bed blocks */
    @SerializedName("bedType")
    private String bedType;

    /** Wall height: "normal" (16 blocks) or "short" (13 blocks) */
    @SerializedName("wallSize")
    private String wallSize;

    /** Walls have connect state cycling feature */
    @SerializedName("connectState")
    private Boolean connectState;

    /** Walls don't connect to adjacent blocks when true */
    @SerializedName("unconnect")
    private Boolean unconnect;

    /** Stack elements for cuboid-nsew-stack blocks */
    @SerializedName("stack")
    private List<StackElement> stack;

    /** Bounding box for cuboid blocks */
    @SerializedName("boundingBox")
    private BoundingBox boundingBox;

    /** Cuboid elements for complex cuboid blocks */
    @SerializedName("cuboids")
    private List<CuboidElement> cuboids;

    /** Wood type for wooden blocks (e.g., "oak", "spruce", "birch") */
    @SerializedName("woodType")
    private String woodType;

    /** Particle type for particle emitter blocks (e.g., "flame", "cascade", "wildfire") */
    @SerializedName("particle")
    private String particle;

    // ========================================
    // Nested Classes
    // ========================================

    /**
     * Display settings for GUI transformation (item rendering in inventory/hand).
     * Contains transformation data for how the item appears in different contexts.
     */
    public static class DisplaySettings {
        @SerializedName("gui")
        private GuiTransform gui;

        public GuiTransform getGui() {
            return gui;
        }

        public boolean hasGui() {
            return gui != null;
        }
    }

    /**
     * GUI transformation settings (rotation, translation, scale).
     * Used to customize how items appear in inventory GUI.
     */
    public static class GuiTransform {
        @SerializedName("rotation")
        private double[] rotation;

        @SerializedName("translation")
        private double[] translation;

        @SerializedName("scale")
        private double[] scale;

        public double[] getRotation() {
            return rotation;
        }

        public double[] getTranslation() {
            return translation;
        }

        public double[] getScale() {
            return scale;
        }
    }

    public static class RandomTextureVariant {
        @SerializedName("textures")
        private List<String> textures;

        @SerializedName("weight")
        private Integer weight;

        public List<String> getTextures() {
            return textures;
        }

        public int getWeight() {
            return weight != null ? weight : 1;
        }
    }

    public static class StateVariant {
        @SerializedName("stateID")
        private String stateID;

        @SerializedName("textures")
        private List<String> textures;

        @SerializedName("randomTextures")
        private List<RandomTextureVariant> randomTextures;

        @SerializedName("overlayTextures")
        private List<String> overlayTextures;

        @SerializedName("boundingBox")
        private BoundingBox boundingBox;

        @SerializedName("cuboids")
        private List<CuboidElement> cuboids;

        @SerializedName("rotYOffset")
        private Integer rotYOffset;

        @SerializedName("isCustomModel")
        private Boolean isCustomModel;

        public String getStateID() {
            return stateID;
        }

        public List<String> getTextures() {
            return textures;
        }

        public List<RandomTextureVariant> getRandomTextures() {
            return randomTextures;
        }

        public boolean hasRandomTextures() {
            return randomTextures != null && !randomTextures.isEmpty();
        }

        public List<String> getOverlayTextures() {
            return overlayTextures;
        }

        public boolean hasOverlayTextures() {
            return overlayTextures != null && !overlayTextures.isEmpty();
        }

        public BoundingBox getBoundingBox() {
            return boundingBox;
        }

        public List<CuboidElement> getCuboids() {
            return cuboids;
        }

        public boolean hasCuboids() {
            return cuboids != null && !cuboids.isEmpty();
        }

        public Integer getRotYOffset() {
            return rotYOffset;
        }

        public boolean isCustomModel() {
            return Boolean.TRUE.equals(isCustomModel);
        }
    }

    public static class HarvestLevel {
        @SerializedName("tool")
        private String tool;

        @SerializedName("level")
        private int level;

        public String getTool() {
            return tool;
        }

        public int getLevel() {
            return level;
        }
    }

    public static class StackElement {
        @SerializedName("textures")
        private List<String> textures;

        @SerializedName("boundingBox")
        private BoundingBox boundingBox;

        public List<String> getTextures() {
            return textures;
        }

        public BoundingBox getBoundingBox() {
            return boundingBox;
        }

        public boolean hasBoundingBox() {
            return boundingBox != null;
        }
    }

    public static class BoundingBox {
        @SerializedName("xMin")
        private double xMin;

        @SerializedName("xMax")
        private double xMax;

        @SerializedName("yMin")
        private double yMin;

        @SerializedName("yMax")
        private double yMax;

        @SerializedName("zMin")
        private double zMin;

        @SerializedName("zMax")
        private double zMax;

        public double getXMin() { return xMin; }
        public double getXMax() { return xMax; }
        public double getYMin() { return yMin; }
        public double getYMax() { return yMax; }
        public double getZMin() { return zMin; }
        public double getZMax() { return zMax; }
    }

    public static class CuboidElement {
        @SerializedName("xMin")
        private double xMin;

        @SerializedName("xMax")
        private double xMax;

        @SerializedName("yMin")
        private double yMin;

        @SerializedName("yMax")
        private double yMax;

        @SerializedName("zMin")
        private double zMin;

        @SerializedName("zMax")
        private double zMax;

        @SerializedName("sideTextures")
        private int[] sideTextures;

        @SerializedName("sideRotations")
        private int[] sideRotations;

        @SerializedName("noTint")
        private boolean[] noTint;

        @SerializedName("shape")
        private String shape;

        public double getXMin() { return xMin; }
        public double getXMax() { return xMax; }
        public double getYMin() { return yMin; }
        public double getYMax() { return yMax; }
        public double getZMin() { return zMin; }
        public double getZMax() { return zMax; }
        public int[] getSideTextures() { return sideTextures; }
        public int[] getSideRotations() { return sideRotations; }
        public boolean[] getNoTint() { return noTint; }
        public String getShape() { return shape; }
    }

    public String getBlockName() {
        return blockName;
    }

    public String getBlockType() {
        return blockType;
    }

    public String getSoundGroup() {
        return soundGroup;
    }

    public float getResistance() {
        return resistance;
    }

    public Boolean isLegacyModel() { return Boolean.TRUE.equals(isLegacyModel); }

    public float getHardness() {
        return hardness;
    }

    public Float getStrength() {
        return strength;
    }

    public boolean hasStrength() {
        return strength != null;
    }

    public String getMaterial() {
        return material;
    }

    public boolean hasMaterial() {
        return material != null && !material.isEmpty();
    }

    public String getCreativeTab() {
        return creativeTab;
    }

    public boolean toggleOnUse() {
        return Boolean.TRUE.equals(toggleOnUse);
    }

    public String getLabel() {
        return label;
    }

    public List<String> getTextures() {
        return textures;
    }

    public String getType() {
        return type;
    }

    public List<HarvestLevel> getHarvestLevel() {
        return harvestLevel;
    }

    public boolean isRequiresTool() {
        return Boolean.TRUE.equals(requiresTool);
    }

    public Integer getLightOpacity() {
        return lightOpacity;
    }

    public boolean hasLightOpacity() {
        return lightOpacity != null;
    }

    public List<BoundingBox> getCollisionBoxes() {
        return collisionBoxes;
    }

    public boolean hasCollisionBoxes() {
        return collisionBoxes != null && !collisionBoxes.isEmpty();
    }

    public String getRenderLayer() {
        return renderLayer;
    }

    public boolean hasRenderLayer() {
        return renderLayer != null && !renderLayer.isEmpty();
    }

    public boolean isNonOpaque() {
        return Boolean.TRUE.equals(nonOpaque);
    }

    public boolean isLayerSensitive() {
        return Boolean.TRUE.equals(layerSensitive);
    }

    public boolean hasNoCollision() {
        return Boolean.TRUE.equals(noCollision);
    }

    public List<RandomTextureVariant> getRandomTextures() {
        return randomTextures;
    }

    public boolean hasRandomTextures() {
        return randomTextures != null && !randomTextures.isEmpty();
    }

    public List<StateVariant> getStates() { return states; }

    public boolean hasStates() {
        return states != null && !states.isEmpty();
    }

    public int getStateCount() {
        if (hasStates()) {
            return states.size();
        }
        return 0;
    }

    public List<String> getOverlayTextures() {
        return overlayTextures;
    }

    public boolean hasOverlayTextures() {
        return overlayTextures != null && !overlayTextures.isEmpty();
    }

    public Boolean getLocked() {
        return locked;
    }

    public boolean isLocked() {
        return Boolean.TRUE.equals(locked);
    }

    public boolean isAllowUnsupported() {
        return Boolean.TRUE.equals(allowUnsupported);
    }

    public boolean isAllowHalfBreak() {
        return Boolean.TRUE.equals(allowHalfBreak);
    }

    public String getColorMult() {
        return colorMult;
    }

    public boolean hasColorMult() {
        return colorMult != null && !colorMult.isEmpty();
    }

    public List<String> getColorMults() {
        return colorMults;
    }

    public boolean hasColorMults() {
        return colorMults != null && !colorMults.isEmpty();
    }

    public String getItemTexture() {
        return itemTexture;
    }

    public boolean hasItemTexture() {
        return itemTexture != null && !itemTexture.isEmpty();
    }

    public Integer getItemTextureIndex() {
        return itemTextureIndex;
    }

    public boolean hasItemTextureIndex() {
        return itemTextureIndex != null;
    }

    public DisplaySettings getDisplay() {
        return display;
    }

    public boolean hasDisplay() {
        return display != null;
    }

    public List<String> getTooltips() {
        return tooltips;
    }

    public boolean hasTooltips() {
        return tooltips != null && !tooltips.isEmpty();
    }

    public int getLuminance() {
        return luminance != null ? luminance : 0;
    }

    public boolean hasCustomModel() {
        return Boolean.TRUE.equals(isCustomModel);
    }

    public boolean isTinted() {
        return Boolean.TRUE.equals(isTinted);
    }

    public boolean hasOverlay() {
        return Boolean.TRUE.equals(hasOverlay);
    }

    public boolean hasBetterFoliage() {
        return Boolean.TRUE.equals(hasBetterFoliage) || Boolean.TRUE.equals(betterFoliage);
    }

    public boolean hasRotateRandom() {
        return Boolean.TRUE.equals(hasRotateRandom);
    }

    public boolean isRotateRandom() {
        return Boolean.TRUE.equals(rotateRandom);
    }

    public boolean isNoDecay() {
        return Boolean.TRUE.equals(noDecay);
    }

    public boolean isAlphaRender() {
        return Boolean.TRUE.equals(alphaRender);
    }

    public boolean isNoParticle() {
        return Boolean.TRUE.equals(noParticle);
    }

    public String getBedType() {
        return bedType;
    }

    public boolean hasBedType() {
        return bedType != null && !bedType.isEmpty();
    }

    public boolean isAlwaysOn() {
        return Boolean.TRUE.equals(alwaysOn);
    }

    public boolean hasClimb() {
        return Boolean.TRUE.equals(hasClimb);
    }

    public boolean hasDown() {
        return Boolean.TRUE.equals(hasDown);
    }

    public String getConnectTo() {
        return connectTo;
    }

    public boolean hasConnectTo() {
        return connectTo != null && !connectTo.isEmpty();
    }

    public boolean isSymmetrical() {
        return Boolean.TRUE.equals(symmetrical);
    }

    public String getWallSize() {
        return wallSize != null ? wallSize : "normal";
    }

    public boolean isConnectState() {
        return Boolean.TRUE.equals(connectState);
    }

    public boolean isUnconnect() {
        return Boolean.TRUE.equals(unconnect);
    }

    public List<StackElement> getStack() {
        return stack;
    }

    public List<StackElement> getStackElements() {
        return stack;
    }

    public boolean hasStackElements() {
        return stack != null && !stack.isEmpty();
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public boolean hasBoundingBox() {
        return boundingBox != null;
    }

    public List<CuboidElement> getCuboids() {
        return cuboids;
    }

    /** Wood type for wooden blocks (e.g., "oak", "spruce", "birch") */
    public String getWoodType() {
        // Use dedicated woodType field if present
        if (woodType != null && !woodType.isEmpty()) {
            return woodType;
        }
        // Fall back to type field for legacy support
        if (type != null && !type.isEmpty()) {
            return type;
        }
        // Default to oak
        return "oak";
    }

    public boolean hasWoodType() {
        return woodType != null && !woodType.isEmpty();
    }

    /** State values list for STATE property creation */
    public List<String> getStateValues() {
        if (hasStates()) {
            List<String> stateIds = new ArrayList<>();
            for (StateVariant state : states) {
                if (state != null && state.getStateID() != null && !state.getStateID().isEmpty()) {
                    stateIds.add(state.getStateID());
                }
            }
            return stateIds.isEmpty() ? null : stateIds;
        }
        return null;
    }

    /** Whether block has no-climb property (derived from type field) */
    public boolean isNoClimb() {
        return type != null && type.contains("no-climb");
    }

    /** Whether vines can grow downward (alias for hasDown for backward compatibility) */
    public boolean canGrowDownward() {
        return hasDown();
    }

    /** Whether block should not be contained in web (derived from type field) */
    public boolean isNoInWeb() {
        return type != null && type.contains("no-in-web");
    }

    /** Whether pane uses bars model (derived from type field) */
    public boolean isBarsModel() {
        return type != null && (type.contains("bars-model") || "bars".equals(type));
    }

    /** Particle type for particle emitter blocks (e.g., "flame", "cascade", "wildfire") */
    public String getParticle() {
        return particle != null ? particle : "flame";
    }

    public boolean hasParticle() {
        return particle != null && !particle.isEmpty();
    }

    /**
     * Enum representing the primary texture data source priority.
     * Used by exporters to determine which texture extraction method to use.
     */
    public enum TextureSource {
        /** Block has state variants with different textures per state */
        STATES,
        /** Block has random texture variants for variety */
        RANDOM_TEXTURES,
        /** Block uses a standard texture list */
        TEXTURES,
        /** Block uses pre-made custom model files */
        CUSTOM_MODEL,
        /** No texture data defined */
        NONE
    }

    /**
     * Universal container for texture variants with weight and optional overlays.
     * Supports both List-based (from JSON) and array-based (exporter) workflows.
     *
     */
    public static class TextureVariantSet {
        public final List<String> textures;
        public final List<String> overlayTextures;
        public final int weight;

        /**
         * Primary constructor with full overlay support.
         *
         * @param textures List of texture paths
         * @param weight Variant weight for random selection (minimum 1)
         * @param overlayTextures Optional overlay texture paths
         */
        public TextureVariantSet(List<String> textures, int weight, List<String> overlayTextures) {
            this.textures = textures != null ? textures : new ArrayList<>();
            this.weight = Math.max(1, weight);
            this.overlayTextures = overlayTextures != null ? overlayTextures : new ArrayList<>();
        }

        /**
         * Constructor without overlays (for backward compatibility).
         */
        public TextureVariantSet(List<String> textures, int weight) {
            this(textures, weight, null);
        }

        /**
         * Array-based constructor with overlays (for exporter use).
         */
        public TextureVariantSet(String[] textures, int weight, String[] overlayTextures) {
            this(
                textures != null ? Arrays.asList(textures) : new ArrayList<>(),
                weight,
                overlayTextures != null ? Arrays.asList(overlayTextures) : null
            );
        }

        /**
         * Array-based constructor without overlays (for exporter use).
         */
        public TextureVariantSet(String[] textures, int weight) {
            this(textures, weight, (String[]) null);
        }

        /**
         * Single texture constructor (for simple cases like ladders).
         */
        public TextureVariantSet(String texture, int weight) {
            this(
                texture != null ? Collections.singletonList(texture) : new ArrayList<>(),
                weight,
                null
            );
        }

        /**
         * Checks if this variant has overlay textures.
         */
        public boolean hasOverlay() {
            return overlayTextures != null && !overlayTextures.isEmpty();
        }

        /**
         * Returns textures as array for exporter processing.
         */
        public String[] getTexturesAsArray() {
            return textures.toArray(new String[0]);
        }

        /**
         * Returns overlay textures as array for exporter processing.
         */
        public String[] getOverlayTexturesAsArray() {
            return overlayTextures != null ? overlayTextures.toArray(new String[0]) : new String[0];
        }

        /**
         * Gets the first texture with safe fallback.
         */
        public String getFirstTexture() {
            return textures.isEmpty() ? "missingno" : textures.get(0);
        }
    }

    // ========================================
    // Random Texture Processing
    // ========================================

    /**
     * Extracts random texture variants as structured sets with weights.
     * Centralizes the common pattern used across all exporters.
     *
     * @return List of texture variant sets, empty if no random textures defined
     */
    public List<TextureVariantSet> getRandomTextureVariantSets() {
        if (!hasRandomTextures()) {
            return new ArrayList<>();
        }

        List<TextureVariantSet> variants = new ArrayList<>();
        for (RandomTextureVariant rtv : randomTextures) {
            variants.add(new TextureVariantSet(rtv.getTextures(), rtv.getWeight()));
        }
        return variants;
    }

    /**
     * Converts random texture variants to String[][] format.
     * Used by solid blocks, cuboid blocks, and other blocks that need array format.
     *
     * @return 2D array where each row is a texture variant
     */
    public String[][] getRandomTextureArrays() {
        List<TextureVariantSet> variants = getRandomTextureVariantSets();
        String[][] arrays = new String[variants.size()][];
        for (int i = 0; i < variants.size(); i++) {
            List<String> textures = variants.get(i).textures;
            arrays[i] = textures.toArray(new String[0]);
        }
        return arrays;
    }

    /**
     * Checks if definition has actual random textures (not just empty structure).
     * Some definitions may have randomTextures array but with no actual texture data.
     *
     * @return true if at least one random texture variant has actual textures
     */
    public boolean hasActualRandomTextures() {
        if (!hasRandomTextures()) {
            return false;
        }

        for (RandomTextureVariant rtv : randomTextures) {
            if (rtv.getTextures() != null && !rtv.getTextures().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    // ========================================
    // State Variant Processing
    // ========================================

    /**
     * Converts state variants to String[][] format.
     * Each row contains textures for one state variant.
     *
     * @return 2D array where each row is a state's textures
     */
    public String[][] getStateTextureArrays() {
        if (!hasStates()) {
            return new String[0][];
        }

        List<StateVariant> stateList = getStates();
        String[][] arrays = new String[stateList.size()][];

        for (int i = 0; i < stateList.size(); i++) {
            StateVariant state = stateList.get(i);
            List<String> stateTextures = state.getTextures();
            arrays[i] = stateTextures != null ? stateTextures.toArray(new String[0]) : new String[0];
        }

        return arrays;
    }

    /**
     * Extracts random texture variants from a specific StateVariant.
     * Useful for processing states that have their own random texture variants.
     *
     * @param stateVariant The state variant to extract random textures from
     * @return List of texture variant sets, empty if no random textures defined
     */
    public static List<TextureVariantSet> getRandomTextureVariantSetsFromState(StateVariant stateVariant) {
        if (!stateVariant.hasRandomTextures()) {
            return new ArrayList<>();
        }

        List<TextureVariantSet> variants = new ArrayList<>();
        for (RandomTextureVariant rtv : stateVariant.getRandomTextures()) {
            variants.add(new TextureVariantSet(rtv.getTextures(), rtv.getWeight()));
        }
        return variants;
    }

    /**
     * Returns textures as a String array for easy processing.
     *
     * @return Array of texture paths, or empty array if no textures defined
     */
    public String[] getTexturesAsArray() {
        if (textures == null || textures.isEmpty()) {
            return new String[0];
        }
        return textures.toArray(new String[0]);
    }


    /**
     * Gets the first texture with custom fallback.
     *
     * @param fallback The fallback texture to use if no textures defined
     * @return First texture path, or fallback if no textures defined
     */
    public String getFirstTexture(String fallback) {
        if (textures != null && !textures.isEmpty()) {
            return textures.getFirst();
        }
        return fallback;
    }

    /**
     * Returns the number of textures defined.
     *
     * @return Number of textures in the textures list
     */
    public int getTextureCount() {
        return textures != null ? textures.size() : 0;
    }

    /**
     * Determines the primary texture data source based on priority.
     * Priority order: STATES > RANDOM_TEXTURES > TEXTURES > CUSTOM_MODEL > NONE
     *
     * @return The primary texture source enum
     */
    public TextureSource getPrimaryTextureSource() {
        if (hasStates()) {
            return TextureSource.STATES;
        }
        if (hasActualRandomTextures()) {
            return TextureSource.RANDOM_TEXTURES;
        }
        if (textures != null && !textures.isEmpty()) {
            return TextureSource.TEXTURES;
        }
        if (hasCustomModel()) {
            return TextureSource.CUSTOM_MODEL;
        }
        return TextureSource.NONE;
    }

    /**
     * Extracts primary texture data based on priority.
     * Returns String[] for single textures, String[][] for variants/states.
     *
     * @return Texture data as Object (cast to String[] or String[][] based on source)
     */
    public Object extractTextures() {
        TextureSource source = getPrimaryTextureSource();
        return switch (source) {
            case STATES -> getStateTextureArrays();
            case RANDOM_TEXTURES -> getRandomTextureArrays();
            case TEXTURES -> getTexturesAsArray();
            case CUSTOM_MODEL, NONE -> new String[0];
        };
    }

    /**
     * Validates that the block definition has at least one texture source.
     *
     * @throws IllegalArgumentException if no texture data is defined
     */
    public void validateTextureData() {
        TextureSource source = getPrimaryTextureSource();
        if (source == TextureSource.NONE && !hasCustomModel()) {
            throw new IllegalArgumentException(
                "Block definition '" + blockName + "' must have at least one texture source " +
                "(textures, randomTextures, states, or isCustomModel)"
            );
        }
    }

    /**
     * Validates that the texture list has at least the expected count.
     *
     * @param expected The minimum number of textures required
     * @throws IllegalArgumentException if texture count is insufficient
     */
    public void validateTextureCount(int expected) {
        int actual = getTextureCount();
        if (actual < expected) {
            throw new IllegalArgumentException(
                "Block definition '" + blockName + "' requires at least " + expected +
                " texture(s), but only " + actual + " provided"
            );
        }
    }


    /**
     * Determines if the block should use a tinted model.
     * Combines multiple tinting indicators for comprehensive detection.
     *
     * @return true if block should use tinted rendering
     */
    public boolean shouldUseTintedModel() {
        return isTinted() || hasColorMult() || hasOverlay();
    }

    // ========================================
    // Block Settings/Properties Creation
    // ========================================

    // Static sound group mapping
    private static final Map<String, BlockSoundGroup> SOUND_GROUP_MAP = createSoundGroupMap();

    private static Map<String, BlockSoundGroup> createSoundGroupMap() {
        Map<String, BlockSoundGroup> map = new HashMap<>();
        map.put("powder", BlockSoundGroup.SAND);
        map.put("wood", BlockSoundGroup.WOOD);
        map.put("gravel", BlockSoundGroup.GRAVEL);
        map.put("grass", BlockSoundGroup.GRASS);
        map.put("stone", BlockSoundGroup.STONE);
        map.put("metal", BlockSoundGroup.METAL);
        map.put("glass", BlockSoundGroup.GLASS);
        map.put("cloth", BlockSoundGroup.WOOL);
        map.put("sand", BlockSoundGroup.SAND);
        map.put("snow", BlockSoundGroup.SNOW);
        map.put("ladder", BlockSoundGroup.LADDER);
        map.put("anvil", BlockSoundGroup.ANVIL);
        map.put("plant", BlockSoundGroup.CROP);
        map.put("wool", BlockSoundGroup.WOOL);
        map.put("slime", BlockSoundGroup.SLIME);
        map.put("bamboo", BlockSoundGroup.BAMBOO);
        map.put("lantern", BlockSoundGroup.LANTERN);
        map.put("nether_bricks", BlockSoundGroup.NETHER_BRICKS);
        map.put("netherite", BlockSoundGroup.NETHERITE);
        return map;
    }

    /**
     * Gets the BlockSoundGroup for this block definition.
     * Uses the soundGroup field to look up the appropriate sound group.
     *
     * @return The BlockSoundGroup, defaults to STONE if not found
     */
    public BlockSoundGroup getBlockSoundGroup() {
        if (soundGroup == null || soundGroup.isEmpty()) {
            return BlockSoundGroup.STONE;
        }
        return SOUND_GROUP_MAP.getOrDefault(soundGroup.toLowerCase(), BlockSoundGroup.STONE);
    }

    /**
     * Creates AbstractBlock.Settings from this block definition.
     * This is the primary method for creating block settings with default behavior.
     *
     * @return Configured AbstractBlock.Settings
     */
    public AbstractBlock.Settings makeSettings() {
        return makeSettings(null);
    }

    /**
     * Creates AbstractBlock.Settings from this block definition, optionally copying from another block.
     * Applies all relevant properties from the block definition including:
     * - Strength (hardness/resistance)
     * - Sound group
     * - Luminance (light level)
     * - Opacity
     * - Collision
     * - Tool requirements
     *
     * @param copyFrom Optional block to copy base settings from, or null to create fresh settings
     * @return Configured AbstractBlock.Settings based on this block definition
     */
    public AbstractBlock.Settings makeSettings(Block copyFrom) {
        AbstractBlock.Settings settings;

        // Start with either copy or fresh settings
        if (copyFrom != null) {
            settings = AbstractBlock.Settings.copy(copyFrom);
        } else {
            settings = AbstractBlock.Settings.create();
        }

        // Apply strength (hardness and resistance)
        if (hasStrength()) {
            // Use strength shorthand if provided (sets both to same value)
            settings = settings.strength(strength, strength);
        } else if (hardness != 0.0f || resistance != 0.0f) {
            // Apply individual hardness and resistance
            settings = settings.strength(hardness, resistance);
        }

        // Apply sound group
        settings = settings.sounds(getBlockSoundGroup());

        // Apply luminance (light level 0-15)
        int light = getLuminance();
        if (light > 0) {
            settings = settings.luminance(lum -> getLuminance());
        }

        // Apply opacity settings
        if (isNonOpaque()) {
            settings = settings.nonOpaque();
        }

        // Apply collision settings
        if (hasNoCollision()) {
            settings = settings.noCollision();
        }

        // Apply tool requirements
        if (isRequiresTool()) {
            settings = settings.requiresTool();
        }

        return settings;
    }

    /**
     * Creates AbstractBlock.Settings with custom overrides applied after base settings.
     * Useful when you need to apply the definition's settings but then customize further.
     *
     * @param copyFrom Optional block to copy from
     * @param customizer Function to apply custom modifications to the settings
     * @return Configured and customized AbstractBlock.Settings
     */
    public AbstractBlock.Settings makeSettings(
            Block copyFrom,
            UnaryOperator<AbstractBlock.Settings> customizer) {
        AbstractBlock.Settings settings = makeSettings(copyFrom);
        return customizer.apply(settings);
    }

    @Override
    public String toString() {
        return String.format("BlockDefinition{blockName='%s', blockType='%s', label='%s'}",
                blockName, blockType, label);
    }
}