package com.westerosblocks.data;

import com.google.gson.annotations.SerializedName;
import com.westerosblocks.utils.ModProperties;
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

    /** Texture file paths (relative to textures/block/) - order varies by block type */
    @SerializedName("textures")
    private List<String> textures;

    /** Type properties - structured format for block-specific properties */
    @SerializedName("type")
    private TypeProperties type;

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

    /** Use custom item texture from item/ directory instead of block texture */
    @SerializedName("customItemTexture")
    private Boolean customItemTexture;

    /** Which texture index to use for item rendering (when block has multiple textures) */
    @SerializedName("itemTextureIndex")
    private Integer itemTextureIndex;

    /** GUI transformation settings for item display in inventory */
    @SerializedName("display")
    private DisplaySettings display;

    /** Tooltip text lines shown when hovering over the block in inventory */
    @SerializedName("tooltips")
    private List<TooltipEntry> tooltips;

    /** Light level emitted by block (0-15) */
    @SerializedName("luminance")
    private Integer luminance;

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


    /** Stack blocks: allows top half to be broken independently */
    @SerializedName("allowHalfBreak")
    private Boolean allowHalfBreak;


    /** Vines are climbable like ladders */
    @SerializedName("hasClimb")
    private Boolean hasClimb;

    /** Vines can grow downward */
    @SerializedName("hasDown")
    private Boolean hasDown;

    /** What block material to connect to (e.g., "material" for webs) */
    @SerializedName("connectTo")
    private String connectTo;


    /** Uses custom model files instead of generated ones */
    @SerializedName("isCustomModel")
    private Boolean isCustomModel;

    /** Block uses tinted rendering (grass-like color variation) */
    @SerializedName("isTinted")
    private Boolean isTinted;


    /** Randomly rotate block models */
    @SerializedName("hasRotateRandom")
    private Boolean hasRotateRandom;

    /** Randomly rotate block on placement (Y-axis rotation) */
    @SerializedName("rotateRandom")
    private Boolean rotateRandom;

    /** Block uses alpha/translucent rendering */
    @SerializedName("alphaRender")
    private Boolean alphaRender;

    /** Legacy model for pane blocks */
    @SerializedName("isLegacyModel")
    private String isLegacyModel;

    /** Bed type for bed blocks */
    @SerializedName("bedType")
    private String bedType;

    /** Wall height: "normal" (16 blocks) or "short" (13 blocks) */
    @SerializedName("wallSize")
    private String wallSize;

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

        public int getTextureCount() {
            return (textures != null) ? textures.size() : 0;
        }

        public String getTextureByIndex(int index) {
            if (textures == null || textures.isEmpty()) {
                return null;
            }
            if (index >= textures.size()) {
                index = textures.size() - 1;
            }
            return textures.get(index);
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

        public int getRandomTextureSetCount() {
            return (randomTextures != null) ? randomTextures.size() : 0;
        }

        public RandomTextureVariant getRandomTextureSet(int index) {
            if (randomTextures == null || index < 0 || index >= randomTextures.size()) {
                return null;
            }
            return randomTextures.get(index);
        }

        public String getTextureByIndex(int index) {
            // Try to get from first random texture set
            if (randomTextures != null && !randomTextures.isEmpty()) {
                RandomTextureVariant firstSet = randomTextures.get(0);
                return firstSet.getTextureByIndex(index);
            }
            // Fallback to direct textures list
            if (textures != null && !textures.isEmpty()) {
                if (index >= textures.size()) {
                    index = textures.size() - 1;
                }
                return textures.get(index);
            }
            return null;
        }


        public void doInit() {
            // Normalize textures to randomTextures format
            if ((randomTextures == null || randomTextures.isEmpty()) &&
                textures != null && !textures.isEmpty()) {
                randomTextures = new ArrayList<>();
                RandomTextureVariant rtv = new RandomTextureVariant();
                rtv.textures = new ArrayList<>(textures);
                rtv.weight = 1;
                randomTextures.add(rtv);
            }

            // Compute bounding box from cuboids if needed
            if (cuboids != null && !cuboids.isEmpty() && boundingBox == null) {
                computeBoundingBoxFromCuboids();
            }

            // Create trivial cuboid from bounding box if needed
            if (boundingBox != null && (cuboids == null || cuboids.isEmpty())) {
                createCuboidFromBoundingBox();
            }
        }

        /**
         * Computes bounding box from cuboid list.
         */
        private void computeBoundingBoxFromCuboids() {
            double minX = 1.0, minY = 1.0, minZ = 1.0;
            double maxX = 0.0, maxY = 0.0, maxZ = 0.0;

            for (CuboidElement c : cuboids) {
                minX = Math.min(minX, c.getXMin());
                minY = Math.min(minY, c.getYMin());
                minZ = Math.min(minZ, c.getZMin());
                maxX = Math.max(maxX, c.getXMax());
                maxY = Math.max(maxY, c.getYMax());
                maxZ = Math.max(maxZ, c.getZMax());
            }

            boundingBox = new BoundingBox();
            boundingBox.xMin = minX;
            boundingBox.xMax = maxX;
            boundingBox.yMin = minY;
            boundingBox.yMax = maxY;
            boundingBox.zMin = minZ;
            boundingBox.zMax = maxZ;
        }

        /**
         * Creates a single cuboid from bounding box.
         */
        private void createCuboidFromBoundingBox() {
            CuboidElement c = new CuboidElement();
            c.xMin = boundingBox.xMin;
            c.xMax = boundingBox.xMax;
            c.yMin = boundingBox.yMin;
            c.yMax = boundingBox.yMax;
            c.zMin = boundingBox.zMin;
            c.zMax = boundingBox.zMax;
            cuboids = List.of(c);
        }
    }

    /**
     * Represents a single tooltip entry with text and optional formatting.
     * Used in block definitions to provide per-line tooltip formatting.
     */
    public static class TooltipEntry {
        @SerializedName("text")
        private String text;

        @SerializedName("format")
        private String format;  // Optional, defaults to "GRAY"

        public TooltipEntry() {}

        public TooltipEntry(String text, String format) {
            this.text = text;
            this.format = format;
        }

        public String getText() {
            return text;
        }

        public String getFormat() {
            return format != null ? format : "GRAY";
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
    /** Tracks whether doInit() has been called */
    private transient boolean didInit = false;

    /** State property for blocks with multiple states */
    private transient StateProperty stateProperty = null;

    /**
     * Initializes the block definition after JSON loading.
     * This method:
     * - Normalizes texture data (converts simple textures to randomTextures)
     * - Inherits properties from base definition to states
     * - Processes stack elements
     * - Creates state property for multi-state blocks
     * - Computes derived properties
     *
     * Called automatically by BlockDefinitionLoader after JSON parsing.
     */
    public void doInit() {
        if (didInit) return;

        if (hasOverlayTextures()) {
            this.nonOpaque = true;
        }

        normalizeBaseTextures();
        processStates();
        createStateProperty();

        didInit = true;
    }

    /**
     * Converts simple texture list to randomTextures format.
     * If randomTextures already exists, this is a no-op.
     */
    private void normalizeBaseTextures() {
        if (randomTextures == null && textures != null && !textures.isEmpty()) {
            randomTextures = new ArrayList<>();
            RandomTextureVariant rtv = new RandomTextureVariant();
            rtv.textures = new ArrayList<>(textures);
            rtv.weight = 1;
            randomTextures.add(rtv);
        }
    }

    /**
     * Processes state variants - inherits properties from base and initializes each state.
     * If no states are defined, creates a synthetic "base" state from this definition's properties.
     * This ensures states is never null/empty after initialization, simplifying exporter logic.
     */
    private void processStates() {
        // If no states defined, create a synthetic base state from this definition
        // This matches the old 1.18.2 pattern where def.states always had at least one element
        if (states == null || states.isEmpty()) {
            StateVariant baseState = new StateVariant();
            baseState.stateID = "base";

            // Copy all base-level properties to the synthetic state
            baseState.textures = this.textures;
            baseState.randomTextures = this.randomTextures;
            baseState.overlayTextures = this.overlayTextures;
            baseState.boundingBox = this.boundingBox;
            baseState.cuboids = this.cuboids;
            baseState.rotYOffset = 0;
            baseState.isCustomModel = this.isCustomModel;

            // Initialize the synthetic state
            baseState.doInit();

            // Set states to contain just this synthetic base state
            states = new ArrayList<>();
            states.add(baseState);
        } else {
            // Process existing states
            for (int i = 0; i < states.size(); i++) {
                StateVariant state = states.get(i);

                // Generate stateID if missing
                if (state.stateID == null || state.stateID.isEmpty()) {
                    state.stateID = "state" + i;
                }

                // Initialize the state FIRST (normalizes textures, computes bounding boxes)
                // This must happen before inheritance so state-specific textures aren't overwritten
                state.doInit();

                // Then inherit undefined properties from base definition
                inheritPropertiesToState(state);

                // If state has overlay textures, mark base as nonOpaque
                if (state.hasOverlayTextures()) {
                    this.nonOpaque = true;
                }
            }
        }
    }

    /**
     * Inherits properties from base definition to a state variant if not defined.
     */
    private void inheritPropertiesToState(StateVariant state) {
        // Inherit textures
        if ((state.textures == null || state.textures.isEmpty()) && this.textures != null) {
            state.textures = new ArrayList<>(this.textures);
        }

        // Inherit randomTextures
        if ((state.randomTextures == null || state.randomTextures.isEmpty()) && this.randomTextures != null) {
            state.randomTextures = new ArrayList<>(this.randomTextures);
        }

        // Inherit overlayTextures
        if ((state.overlayTextures == null || state.overlayTextures.isEmpty()) && this.overlayTextures != null) {
            state.overlayTextures = new ArrayList<>(this.overlayTextures);
        }

        // Inherit bounding box
        if (state.boundingBox == null && this.boundingBox != null) {
            state.boundingBox = this.boundingBox;
        }

        // Inherit cuboids
        if ((state.cuboids == null || state.cuboids.isEmpty()) && this.cuboids != null) {
            state.cuboids = new ArrayList<>(this.cuboids);
        }
    }

    /**
     * Creates StateProperty for blocks with multiple states.
     */
    private void createStateProperty() {
        if (states != null && states.size() > 1) {
            List<String> stateIds = new ArrayList<>();
            for (StateVariant state : states) {
                stateIds.add(state.getStateID());
            }
            stateProperty = new StateProperty(stateIds);
        }
    }

    /**
     * Property for multi-state blocks.
     * Similar to Minecraft's EnumProperty but for custom state IDs.
     */
    public static class StateProperty {
        private final List<String> values;
        private final Map<String, String> valueMap;
        private final String defaultValue;

        public StateProperty(List<String> stateIDs) {
            this.values = List.copyOf(stateIDs); // Immutable copy
            Map<String, String> map = new HashMap<>();
            for (String id : stateIDs) {
                map.put(id, id);
            }
            this.valueMap = Map.copyOf(map); // Immutable copy
            this.defaultValue = stateIDs.get(0);
        }

        public List<String> getValues() {
            return values;
        }

        public String getDefaultValue() {
            return defaultValue;
        }
    }

    /**
     * Returns the state property for multi-state blocks.
     * @return StateProperty if block has multiple states, null otherwise
     */
    public StateProperty getStateProperty() {
        return stateProperty;
    }

    /**
     * Builds and returns the ModProperties.StateProperty for this block definition.
     * This method is used by block factories to obtain the state property during block creation.
     *
     * @return ModProperties.StateProperty if block has multiple states, null otherwise
     */
    public ModProperties.StateProperty buildStateProperty() {
        if (stateProperty == null) {
            return null;
        }

        return new ModProperties.StateProperty(stateProperty.getValues());
    }

    /**
     * Returns the default state ID for multi-state blocks.
     */
    public String getDefaultStateID() {
        return stateProperty != null ? stateProperty.getDefaultValue() : null;
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

    public Boolean isLegacyModel() { return false; }

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
        return type != null && Boolean.TRUE.equals(type.getToggleOnUse());
    }

    public String getLabel() {
        return label;
    }

    public List<String> getTextures() {
        return textures;
    }

    public TypeProperties getType() {
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
        return type != null && Boolean.TRUE.equals(type.getLayerSensitive());
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

    public boolean isLocked() {
        return type != null && Boolean.TRUE.equals(type.getLocked());
    }

    public boolean isAllowUnsupported() {
        return type != null && Boolean.TRUE.equals(type.getAllowUnsupported());
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

    public boolean hasCustomItemTexture() {
        return Boolean.TRUE.equals(customItemTexture);
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

    public List<TooltipEntry> getTooltips() {
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
        return Boolean.TRUE.equals(isTinted) || hasColorMult() || hasColorMults();
    }

    public boolean hasOverlay() {
        return type != null && Boolean.TRUE.equals(type.getOverlay());
    }

    public boolean hasBetterFoliage() {
        return type != null && Boolean.TRUE.equals(type.getBetterFoliage());
    }

    public boolean hasRotateRandom() {
        return Boolean.TRUE.equals(hasRotateRandom) || Boolean.TRUE.equals(rotateRandom);
    }

    public boolean isNoDecay() {
        return type != null && Boolean.TRUE.equals(type.getNoDecay());
    }

    public boolean isAlphaRender() {
        return Boolean.TRUE.equals(alphaRender);
    }

    public boolean isNoParticle() {
        return type != null && Boolean.TRUE.equals(type.getNoParticle());
    }

    public String getBedType() {
        return bedType;
    }

    public boolean hasBedType() {
        return bedType != null && !bedType.isEmpty();
    }

    public boolean isAlwaysOn() {
        return type != null && Boolean.TRUE.equals(type.getAlwaysOn());
    }

    public boolean hasDown() {
        return Boolean.TRUE.equals(hasDown);
    }

    public boolean isSymmetrical() {
        return type != null && Boolean.TRUE.equals(type.getSymmetrical());
    }

    public String getWallSize() {
        return wallSize != null ? wallSize : "normal";
    }

    public boolean isConnectState() {
        return type != null && Boolean.TRUE.equals(type.getConnectstate());
    }

    public boolean isUnconnect() {
        return type != null && Boolean.TRUE.equals(type.getUnconnect());
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
        // Default to oak
        return "oak";
    }

    public boolean hasWoodType() {
        return woodType != null && !woodType.isEmpty();
    }


    /** Whether block has no-climb property */
    public boolean isNoClimb() {
        return type != null && Boolean.TRUE.equals(type.getNoClimb());
    }

    /** Whether vines can grow downward (alias for hasDown for backward compatibility) */
    public boolean canGrowDownward() {
        return hasDown();
    }

    /** Whether block should not be contained in web */
    public boolean isNoInWeb() {
        return type != null && Boolean.TRUE.equals(type.getNoInWeb());
    }

    /** Whether pane uses bars model */
    public boolean isBarsModel() {
        return type != null && Boolean.TRUE.equals(type.getBarsModel());
    }

    /** Particle type for particle emitter blocks (e.g., "flame", "cascade", "wildfire") */
    public String getParticle() {
        return particle != null ? particle : "flame";
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
     * Returns the number of textures defined.
     *
     * @return Number of textures in the textures list
     */
    public int getTextureCount() {
        return textures != null ? textures.size() : 0;
    }

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

        if (hasStrength()) {
            // Use strength shorthand if provided (sets both to same value)
            settings = settings.strength(strength, strength);
        } else if (hardness != 0.0f || resistance != 0.0f) {
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