package com.westerosblocks.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Base class for state-specific block properties.
 * Can represent either a full block definition or a specific state variant.
 * This class contains properties that can vary between different states of the same block.
 *
 * Design pattern from legacy 1.18.2 code:
 * - All texture/visual properties live here
 * - Normalization happens via doStateRecordInit()
 * - Simple textures are converted to RandomTextureSet format for uniform access
 */
public class BlockStateRecord {

    // ========================================
    // State Identification
    // ========================================

    /** Unique identifier for this state variant (e.g., "flame", "cascade", "wildfire") */
    @SerializedName("stateID")
    protected String stateID = null;

    // ========================================
    // Visual Properties (State-Specific)
    // ========================================

    /** Simple texture list (gets converted to randomTextures during init) */
    @SerializedName("textures")
    protected List<String> textures = null;

    /** Weighted random texture variants (normalized format used internally) */
    @SerializedName("randomTextures")
    protected List<RandomTextureSet> randomTextures = null;

    /** Overlay textures for tinted blocks */
    @SerializedName("overlayTextures")
    protected List<String> overlayTextures = null;

    /** Color multiplier for tinting */
    @SerializedName("colorMult")
    protected String colorMult = "#FFFFFF";

    /** Multiple color multipliers for multi-tint blocks */
    @SerializedName("colorMults")
    protected List<String> colorMults = null;

    /** Whether this state uses custom model files */
    @SerializedName("isCustomModel")
    protected Boolean isCustomModel = false;

    /** Whether to randomly rotate block models on placement */
    @SerializedName("rotateRandom")
    protected boolean rotateRandom = false;

    // ========================================
    // Geometry Properties (State-Specific)
    // ========================================

    /** Bounding box for the block */
    @SerializedName("boundingBox")
    protected BoundingBox boundingBox = null;

    /** Complex cuboid geometry */
    @SerializedName("cuboids")
    protected List<CuboidElement> cuboids = null;

    /** Collision boxes (for raytrace/arrow shots) */
    @SerializedName("collisionBoxes")
    protected List<BoundingBox> collisionBoxes = null;

    /** Support boxes (for fence/wall connections, torch placement) */
    @SerializedName("supportBoxes")
    protected List<BoundingBox> supportBoxes = null;

    // ========================================
    // Light & Rotation
    // ========================================

    /** Light level emitted by this state (0.0-1.0) */
    @SerializedName("lightValue")
    protected float lightValue = 0.0F;

    /** Additional Y-axis rotation offset for cuboid blocks (0, 90, 180, 270) */
    @SerializedName("rotYOffset")
    protected Integer rotYOffset = 0;

    // ========================================
    // Initialization & Normalization
    // ========================================

    /**
     * Normalizes state data after loading from JSON.
     * This is the key method that makes texture access uniform across all blocks.
     *
     * Performs:
     * - Converts simple textures list to RandomTextureSet format (all code can assume randomTextures exists)
     * - Generates bounding boxes from cuboids or vice versa
     * - Ensures consistent data structure for exporters
     */
    public void doStateRecordInit() {
        // CRITICAL: Convert simple textures to RandomTextureSet format
        // After this, ALL texture access goes through randomTextures
        if (this.randomTextures == null) {
            if (this.textures == null) {
                this.textures = new ArrayList<>();
            }
            this.randomTextures = new ArrayList<>();
            RandomTextureSet set = new RandomTextureSet();
            set.textures = this.textures;
            set.weight = 1;
            this.randomTextures.add(set);
        }

        // If bounding box but no cuboids, create trivial cuboid from box
        if (this.boundingBox != null && this.cuboids == null) {
            CuboidElement c = new CuboidElement();
            c.xMin = this.boundingBox.xMin;
            c.xMax = this.boundingBox.xMax;
            c.yMin = this.boundingBox.yMin;
            c.yMax = this.boundingBox.yMax;
            c.zMin = this.boundingBox.zMin;
            c.zMax = this.boundingBox.zMax;
            this.cuboids = Collections.singletonList(c);
        }

        // If cuboids but no bounding box, compute bounding box from cuboids
        if (this.cuboids != null && this.boundingBox == null) {
            this.boundingBox = new BoundingBox();
            this.boundingBox.xMin = this.boundingBox.yMin = this.boundingBox.zMin = 1.0;
            this.boundingBox.xMax = this.boundingBox.yMax = this.boundingBox.zMax = 0.0;
            for (CuboidElement c : this.cuboids) {
                this.boundingBox.xMin = Math.min(this.boundingBox.xMin, c.xMin);
                this.boundingBox.yMin = Math.min(this.boundingBox.yMin, c.yMin);
                this.boundingBox.zMin = Math.min(this.boundingBox.zMin, c.zMin);
                this.boundingBox.xMax = Math.max(this.boundingBox.xMax, c.xMax);
                this.boundingBox.yMax = Math.max(this.boundingBox.yMax, c.yMax);
                this.boundingBox.zMax = Math.max(this.boundingBox.zMax, c.zMax);
            }
        }
    }

    // ========================================
    // Convenient Accessors (Uniform Interface)
    // ========================================

    /**
     * Gets texture at index from the first random texture set.
     * After doStateRecordInit(), this always works.
     *
     * @param idx Texture index (clamped to valid range)
     * @return Texture path or null
     */
    public String getTextureByIndex(int idx) {
        RandomTextureSet set = getRandomTextureSet(0);
        if (set != null) {
            return set.getTextureByIndex(idx);
        }
        return null;
    }

    /**
     * Gets overlay texture at index.
     *
     * @param idx Overlay texture index (clamped to valid range)
     * @return Overlay texture path or null
     */
    public String getOverlayTextureByIndex(int idx) {
        if (this.overlayTextures != null && !this.overlayTextures.isEmpty()) {
            if (idx >= this.overlayTextures.size()) {
                idx = this.overlayTextures.size() - 1;
            }
            return this.overlayTextures.get(idx);
        }
        return null;
    }

    /**
     * Gets the number of random texture sets (variants).
     *
     * @return Number of texture sets
     */
    public int getRandomTextureSetCount() {
        return (randomTextures != null) ? randomTextures.size() : 0;
    }

    /**
     * Gets a specific random texture set by index.
     *
     * @param setnum Set index (clamped to valid range)
     * @return RandomTextureSet or null
     */
    public RandomTextureSet getRandomTextureSet(int setnum) {
        if (randomTextures != null && !randomTextures.isEmpty()) {
            if (setnum >= randomTextures.size()) {
                setnum = randomTextures.size() - 1;
            }
            return randomTextures.get(setnum);
        }
        return null;
    }

    /**
     * Gets the number of textures in the first set.
     * Useful for blocks that expect a specific texture count.
     *
     * @return Number of textures in first set
     */
    public int getTextureCount() {
        RandomTextureSet set = getRandomTextureSet(0);
        return (set != null) ? set.getTextureCount() : 0;
    }

    /**
     * Gets the list of cuboids for this state.
     *
     * @return Cuboid list or empty list
     */
    public List<CuboidElement> getCuboidList() {
        return (cuboids != null) ? cuboids : Collections.emptyList();
    }

    /**
     * Gets the list of collision boxes.
     *
     * @return Collision box list or empty list
     */
    public List<BoundingBox> getCollisionBoxList() {
        return (collisionBoxes != null) ? collisionBoxes : Collections.emptyList();
    }

    /**
     * Checks if this state uses tinted rendering.
     *
     * @return true if colorMult is not white or colorMults is defined
     */
    public boolean isTinted() {
        return (colorMult != null && !colorMult.equals("#FFFFFF")) ||
               (colorMults != null && !colorMults.isEmpty());
    }

    /**
     * Checks if this state uses custom model files.
     *
     * @return true if isCustomModel is set
     */
    public boolean isCustomModel() {
        return Boolean.TRUE.equals(isCustomModel);
    }

    // ========================================
    // Getters for Direct Access
    // ========================================

    public String getStateID() {
        return stateID;
    }

    public List<String> getTextures() {
        return textures;
    }

    public List<RandomTextureSet> getRandomTextures() {
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

    public String getColorMult() {
        return colorMult;
    }

    public List<String> getColorMults() {
        return colorMults;
    }

    public boolean hasColorMults() {
        return colorMults != null && !colorMults.isEmpty();
    }

    public boolean isRotateRandom() {
        return rotateRandom;
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

    public boolean hasCuboids() {
        return cuboids != null && !cuboids.isEmpty();
    }

    public float getLightValue() {
        return lightValue;
    }

    public Integer getRotYOffset() {
        return rotYOffset;
    }

    // ========================================
    // Equality (for validation)
    // ========================================

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof BlockStateRecord)) return false;
        BlockStateRecord that = (BlockStateRecord) other;
        if (this.stateID == null) {
            return that.stateID == null;
        }
        return this.stateID.equals(that.stateID);
    }

    @Override
    public int hashCode() {
        return (stateID != null) ? stateID.hashCode() : 0;
    }

    // ========================================
    // Nested Classes (moved from BlockDefinition)
    // ========================================

    /**
     * Represents a weighted set of textures for random selection.
     * This is the normalized format used internally after doStateRecordInit().
     */
    public static class RandomTextureSet {
        @SerializedName("textures")
        public List<String> textures = null;

        @SerializedName("weight")
        public Integer weight = null;

        /**
         * Gets the number of textures in this set.
         *
         * @return Texture count
         */
        public int getTextureCount() {
            return (textures != null) ? textures.size() : 0;
        }

        /**
         * Gets texture at index (clamped to valid range).
         *
         * @param idx Texture index
         * @return Texture path or null
         */
        public String getTextureByIndex(int idx) {
            if (textures != null && !textures.isEmpty()) {
                if (idx >= textures.size()) {
                    idx = textures.size() - 1;
                }
                return textures.get(idx);
            }
            return null;
        }

        /**
         * Gets the weight for this texture set (default 1).
         *
         * @return Weight value
         */
        public int getWeight() {
            return (weight != null) ? weight : 1;
        }

        /**
         * Gets textures as List.
         *
         * @return Texture list
         */
        public List<String> getTextures() {
            return textures;
        }
    }

    /**
     * Represents a bounding box with min/max coordinates.
     */
    public static class BoundingBox {
        @SerializedName("xMin")
        public double xMin = 0.0;

        @SerializedName("xMax")
        public double xMax = 1.0;

        @SerializedName("yMin")
        public double yMin = 0.0;

        @SerializedName("yMax")
        public double yMax = 1.0;

        @SerializedName("zMin")
        public double zMin = 0.0;

        @SerializedName("zMax")
        public double zMax = 1.0;

        public BoundingBox() {
        }

        public BoundingBox(double xMin, double yMin, double zMin, double xMax, double yMax, double zMax) {
            this.xMin = xMin;
            this.xMax = xMax;
            this.yMin = yMin;
            this.yMax = yMax;
            this.zMin = zMin;
            this.zMax = zMax;
        }

        // Getter methods for compatibility
        public double getXMin() { return xMin; }
        public double getXMax() { return xMax; }
        public double getYMin() { return yMin; }
        public double getYMax() { return yMax; }
        public double getZMin() { return zMin; }
        public double getZMax() { return zMax; }
    }

    /**
     * Represents a cuboid element with dimensions and texture mapping.
     */
    public static class CuboidElement {
        @SerializedName("xMin")
        public double xMin = 0.0;

        @SerializedName("xMax")
        public double xMax = 1.0;

        @SerializedName("yMin")
        public double yMin = 0.0;

        @SerializedName("yMax")
        public double yMax = 1.0;

        @SerializedName("zMin")
        public double zMin = 0.0;

        @SerializedName("zMax")
        public double zMax = 1.0;

        @SerializedName("sideTextures")
        public int[] sideTextures = null;

        @SerializedName("sideRotations")
        public int[] sideRotations = null;

        @SerializedName("noTint")
        public boolean[] noTint = null;

        @SerializedName("shape")
        public String shape = "box";

        public CuboidElement() {
        }

        public CuboidElement(double xMin, double yMin, double zMin, double xMax, double yMax, double zMax) {
            this.xMin = xMin;
            this.xMax = xMax;
            this.yMin = yMin;
            this.yMax = yMax;
            this.zMin = zMin;
            this.zMax = zMax;
        }

        // Getter methods for compatibility
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
}
