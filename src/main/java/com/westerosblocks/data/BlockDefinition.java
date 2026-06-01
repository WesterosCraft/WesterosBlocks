package com.westerosblocks.data;

import com.google.gson.annotations.SerializedName;
import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.utils.ModProperties;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.sound.BlockSoundGroup;

import java.util.*;
import java.util.function.UnaryOperator;

/**
 * Represents a block definition loaded from JSON files in definitions/block_definitions directory.
 * These definitions are used to automatically register blocks with their properties.
 */
public class BlockDefinition {
    /**
     * The unique identifier for this block (e.g., "oak_table", "stone_wall")
     */
    // Fields that BlockSetExpander sets directly are package-private (not private).
    // Gson still works with package-private fields via Field.setAccessible(true).

    @SerializedName("blockName")
    String blockName;

    @SerializedName("blockType")
    String blockType;

    @SerializedName("soundGroup")
    String soundGroup;

    @SerializedName("resistance")
    float resistance;

    @SerializedName("hardness")
    float hardness;

    @SerializedName("strength")
    private Float strength;

    @SerializedName("creativeTab")
    String creativeTab;

    @SerializedName("customTags")
    List<String> customTags;

    @SerializedName("label")
    String label;

    @SerializedName("textures")
    List<String> textures;

    @SerializedName("options")
    OptionsProperties options;

    @SerializedName("renderLayer")
    String renderLayer;

    @SerializedName("randomTextures")
    List<RandomTextureVariant> randomTextures;

    @SerializedName("states")
    List<StateVariant> states;

    @SerializedName("overlayTextures")
    List<String> overlayTextures;

    @SerializedName("colorMult")
    String colorMult;

    /**
     * Array of colormap paths for multi-colormap tinting (e.g., ["textures/colormap/grass", "textures/colormap/birch"])
     */
    @SerializedName("colorMults")
    private List<String> colorMults;

    /**
     * Map color used by paper maps and minimap mods (Xaero, Dynmap). Name of a constant from
     * {@link net.minecraft.block.MapColor} (e.g. "STONE_GRAY", "OAK_TAN"). Optional — if omitted,
     * a sensible color is derived from {@link #soundGroup}.
     */
    @SerializedName("mapColor")
    String mapColor;

    /**
     * Custom texture path for item form (overrides default block texture)
     */
    @SerializedName("itemTexture")
    private String itemTexture;

    /**
     * Use custom item texture from item/ directory instead of block texture
     */
    @SerializedName("customItemTexture")
    private Boolean customItemTexture;

    /**
     * Which texture index to use for item rendering (when block has multiple textures)
     */
    @SerializedName("itemTextureIndex")
    private Integer itemTextureIndex;

    /**
     * GUI transformation settings for item display in inventory
     */
    @SerializedName("display")
    private DisplaySettings display;

    /**
     * Tooltip text lines shown when hovering over the block in inventory
     */
    @SerializedName("tooltips")
    private List<TooltipEntry> tooltips;

    /**
     * Light level emitted by block (0-15)
     */
    @SerializedName("luminance")
    Integer luminance;

    @SerializedName("harvestLevel")
    List<HarvestLevel> harvestLevel;

    /**
     * Block requires correct tool to harvest (drops nothing without correct tool)
     */
    @SerializedName("requiresTool")
    private Boolean requiresTool;

    /**
     * How much light the block blocks (0 = transparent, 15 = fully opaque)
     */
    @SerializedName("lightOpacity")
    Integer lightOpacity;

    /**
     * Array of collision boxes for complex collision shapes
     */
    @SerializedName("collisionBoxes")
    private List<BoundingBox> collisionBoxes;

    /**
     * Block is transparent/non-opaque (lets light through)
     */
    @SerializedName("nonOpaque")
    Boolean nonOpaque;

    /**
     * Block has no collision box (can walk through)
     */
    @SerializedName("noCollision")
    private Boolean noCollision;

    /**
     * Block should have XZ random offset (like plants)
     */
    @SerializedName("doOffsetXZ")
    private Boolean doOffsetXZ;

    /**
     * What block material to connect to (e.g., "material" for webs)
     */
    @SerializedName("connectTo")
    private String connectTo;

    /**
     * Uses custom model files instead of generated ones
     */
    @SerializedName("isCustomModel")
    private Boolean isCustomModel;

    /**
     * Block uses tinted rendering (grass-like color variation)
     */
    @SerializedName("isTinted")
    private Boolean isTinted;

    /**
     * Block uses alpha/translucent rendering
     */
    @SerializedName("alphaRender")
    Boolean alphaRender;

    /**
     * Legacy model for pane blocks
     */
    @SerializedName("isLegacyModel")
    private String isLegacyModel;

    /**
     * Stack elements for cuboid-nsew-stack blocks
     */
    @SerializedName("stack")
    private List<StackElement> stack;

    /**
     * Bounding box for cuboid blocks
     */
    @SerializedName("boundingBox")
    BoundingBox boundingBox;

    @SerializedName("cuboids")
    List<CuboidElement> cuboids;

    /**
     * Wood type for wooden blocks (e.g., "oak", "spruce", "birch")
     */
    @SerializedName("woodType")
    private String woodType;

    /**
     * Particle type for particle emitter blocks (e.g., "flame", "cascade", "wildfire")
     */
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
        List<String> textures;

        @SerializedName("weight")
        Integer weight;

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
        String stateID;

        @SerializedName("textures")
        List<String> textures;

        @SerializedName("randomTextures")
        List<RandomTextureVariant> randomTextures;

        @SerializedName("overlayTextures")
        List<String> overlayTextures;

        @SerializedName("luminance")
        Integer luminance;

        @SerializedName("colorMult")
        String colorMult;

        @SerializedName("boundingBox")
        BoundingBox boundingBox;

        @SerializedName("cuboids")
        List<CuboidElement> cuboids;

        @SerializedName("rotYOffset")
        Integer rotYOffset;

        @SerializedName("isCustomModel")
        Boolean isCustomModel;

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

        @FunctionalInterface
        public interface TextureSetVisitor {
            /**
             * @param setIdx 0-based texture set index
             * @param set    the texture set, or {@code null} for the synthetic
             *               single iteration performed when a custom-model state
             *               has no texture sets defined
             */
            void visit(int setIdx, RandomTextureVariant set);
        }

        /**
         * Iterates every texture set in this state. For custom-model states with
         * no texture sets, performs exactly one iteration with {@code set=null}
         * so callers can still emit a model reference. Non-custom states with no
         * texture sets iterate zero times.
         */
        public void forEachTextureSet(TextureSetVisitor visitor) {
            int count = getRandomTextureSetCount();
            if (count == 0) {
                if (isCustomModel()) {
                    visitor.visit(0, null);
                }
                return;
            }
            for (int i = 0; i < count; i++) {
                visitor.visit(i, randomTextures.get(i));
            }
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

        public TooltipEntry() {
        }

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
        double xMin;

        @SerializedName("xMax")
        double xMax;

        @SerializedName("yMin")
        double yMin;

        @SerializedName("yMax")
        double yMax;

        @SerializedName("zMin")
        double zMin;

        @SerializedName("zMax")
        double zMax;

        public BoundingBox() {}

        public BoundingBox(double xMin, double yMin, double zMin, double xMax, double yMax, double zMax) {
            this.xMin = xMin; this.yMin = yMin; this.zMin = zMin;
            this.xMax = xMax; this.yMax = yMax; this.zMax = zMax;
        }

        public double getXMin() {
            return xMin;
        }

        public double getXMax() {
            return xMax;
        }

        public double getYMin() {
            return yMin;
        }

        public double getYMax() {
            return yMax;
        }

        public double getZMin() {
            return zMin;
        }

        public double getZMax() {
            return zMax;
        }
    }

    public static class CuboidElement {
        @SerializedName("xMin")
        double xMin;

        @SerializedName("xMax")
        double xMax;

        @SerializedName("yMin")
        double yMin;

        @SerializedName("yMax")
        double yMax;

        @SerializedName("zMin")
        double zMin;

        @SerializedName("zMax")
        double zMax;

        @SerializedName("sideTextures")
        int[] sideTextures;

        @SerializedName("sideRotations")
        private int[] sideRotations;

        @SerializedName("noTint")
        private boolean[] noTint;

        @SerializedName("shape")
        private String shape;

        /**
         * Rotation operations for cuboid elements.
         * Each enum carries face index remapping (txtidx) and UV rotation (txtrot) arrays.
         * Face order: 0=bottom(down), 1=top(up), 2=north, 3=south, 4=west, 5=east
         */
        public enum CuboidRotation {
            ROTY90(new int[]{0,1,4,5,3,2}, new int[]{270,90,0,0,0,0}),
            ROTY180(new int[]{0,1,3,2,5,4}, new int[]{180,180,0,0,0,0}),
            ROTY270(new int[]{0,1,5,4,2,3}, new int[]{90,270,0,0,0,0}),
            ROTZ90(new int[]{5,4,2,3,0,1}, new int[]{270,90,270,90,90,90}),
            ROTZ270(new int[]{4,5,2,3,1,0}, new int[]{90,270,90,270,270,270});

            final int[] txtidx;
            final int[] txtrot;

            CuboidRotation(int[] txtidx, int[] txtrot) {
                this.txtidx = txtidx;
                this.txtrot = txtrot;
            }
        }

        /**
         * Creates a new CuboidElement rotated by the given rotation.
         * Transforms coordinates and remaps sideTextures, sideRotations, and noTint arrays.
         *
         * @param rot The rotation to apply
         * @return A new CuboidElement with rotated coordinates and remapped textures
         */
        public CuboidElement rotateCuboid(CuboidRotation rot) {
            CuboidElement result = new CuboidElement();

            // Rotate coordinates
            switch (rot) {
                case ROTY90:
                    result.xMin = 1.0 - this.zMax;
                    result.xMax = 1.0 - this.zMin;
                    result.yMin = this.yMin;
                    result.yMax = this.yMax;
                    result.zMin = this.xMin;
                    result.zMax = this.xMax;
                    break;
                case ROTY180:
                    result.xMin = 1.0 - this.xMax;
                    result.xMax = 1.0 - this.xMin;
                    result.yMin = this.yMin;
                    result.yMax = this.yMax;
                    result.zMin = 1.0 - this.zMax;
                    result.zMax = 1.0 - this.zMin;
                    break;
                case ROTY270:
                    result.xMin = this.zMin;
                    result.xMax = this.zMax;
                    result.yMin = this.yMin;
                    result.yMax = this.yMax;
                    result.zMin = 1.0 - this.xMax;
                    result.zMax = 1.0 - this.xMin;
                    break;
                case ROTZ90:
                    result.xMin = this.yMin;
                    result.xMax = this.yMax;
                    result.yMin = 1.0 - this.xMax;
                    result.yMax = 1.0 - this.xMin;
                    result.zMin = this.zMin;
                    result.zMax = this.zMax;
                    break;
                case ROTZ270:
                    result.xMin = 1.0 - this.yMax;
                    result.xMax = 1.0 - this.yMin;
                    result.yMin = this.xMin;
                    result.yMax = this.xMax;
                    result.zMin = this.zMin;
                    result.zMax = this.zMax;
                    break;
            }

            // Remap sideTextures via txtidx
            if (this.sideTextures != null) {
                result.sideTextures = new int[6];
                for (int i = 0; i < 6; i++) {
                    result.sideTextures[i] = this.sideTextures[rot.txtidx[i]];
                }
            }

            // Replace sideRotations with txtrot
            result.sideRotations = rot.txtrot.clone();

            // Remap noTint via txtidx
            if (this.noTint != null) {
                result.noTint = new boolean[6];
                for (int i = 0; i < 6; i++) {
                    result.noTint[i] = this.noTint[rot.txtidx[i]];
                }
            }

            // Copy shape unchanged
            result.shape = this.shape;

            return result;
        }

        public double getXMin() {
            return xMin;
        }

        public double getXMax() {
            return xMax;
        }

        public double getYMin() {
            return yMin;
        }

        public double getYMax() {
            return yMax;
        }

        public double getZMin() {
            return zMin;
        }

        public double getZMax() {
            return zMax;
        }

        public int[] getSideTextures() {
            return sideTextures;
        }

        public int[] getSideRotations() {
            return sideRotations;
        }

        public boolean[] getNoTint() {
            return noTint;
        }

        public String getShape() {
            return shape;
        }
    }

    private transient boolean didInit = false;

    /**
     * State property for blocks with multiple states
     */
    private transient StateProperty stateProperty = null;

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

        // If state now has a boundingBox but still no cuboids, create cuboid from bbox
        if (state.boundingBox != null && (state.cuboids == null || state.cuboids.isEmpty())) {
            state.createCuboidFromBoundingBox();
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
     *
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

    public boolean isLegacyModel() {
        return isLegacyModel != null && !isLegacyModel.isEmpty();
    }

    public String getLegacyModel() {
        return isLegacyModel;
    }

    public float getHardness() {
        return hardness;
    }

    public Float getStrength() {
        return strength;
    }

    public boolean hasStrength() {
        return strength != null;
    }

    public String getCreativeTab() {
        return creativeTab;
    }

    public List<String> getCustomTags() {
        return customTags;
    }

    public boolean toggleOnUse() {
        return options != null && Boolean.TRUE.equals(options.getToggleOnUse());
    }

    public String getLabel() {
        return label;
    }

    public List<String> getTextures() {
        return textures;
    }

    public OptionsProperties getOptions() {
        return options;
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
        return options != null && Boolean.TRUE.equals(options.getLayerSensitive());
    }

    public boolean isSoftLayer() {
        return options != null && Boolean.TRUE.equals(options.getSoftLayer());
    }

    public boolean hasNoCollision() {
        return Boolean.TRUE.equals(noCollision);
    }

    public boolean isDoOffsetXZ() {
        return Boolean.TRUE.equals(doOffsetXZ);
    }

    public List<RandomTextureVariant> getRandomTextures() {
        return randomTextures;
    }

    public boolean hasRandomTextures() {
        return randomTextures != null && !randomTextures.isEmpty();
    }

    public List<StateVariant> getStates() {
        return states;
    }

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
        return options != null && Boolean.TRUE.equals(options.getLocked());
    }

    public boolean isAllowUnsupported() {
        return options != null && Boolean.TRUE.equals(options.getAllowUnsupported());
    }

    public boolean isAllowHalfBreak() {
        return options != null && Boolean.TRUE.equals(options.getAllowHalfBreak());
    }

    public boolean isNoBreakUnder() {
        return options != null && Boolean.TRUE.equals(options.getNoBreakUnder());
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

    public boolean hasPerStateLuminance() {
        if (states == null) return false;
        for (StateVariant sv : states) {
            if (sv.luminance != null) return true;
        }
        return false;
    }

    /**
     * Overrides the settings luminance function with one that reads the
     * per-state {@code luminance} field from {@link StateVariant}. Falls back
     * to the top-level luminance when a state has none. If no state defines
     * a luminance, the settings are returned unchanged so the top-level value
     * already applied by {@link #makeSettings()} remains in effect.
     */
    public AbstractBlock.Settings applyStateLuminance(
            AbstractBlock.Settings settings,
            ModProperties.StateProperty stateProperty) {
        if (!hasPerStateLuminance() || stateProperty == null || states == null) {
            return settings;
        }
        int fallback = getLuminance();
        Map<String, Integer> byState = new HashMap<>();
        for (StateVariant sv : states) {
            int lum = sv.luminance != null ? sv.luminance : fallback;
            byState.put(sv.getStateID(), lum);
        }
        return settings.luminance(state -> {
            if (state.contains(stateProperty)) {
                Integer v = byState.get(state.get(stateProperty));
                if (v != null) return v;
            }
            return fallback;
        });
    }

    public boolean hasCustomModel() {
        return Boolean.TRUE.equals(isCustomModel);
    }

    public boolean isTinted() {
        return Boolean.TRUE.equals(isTinted) || hasColorMult() || hasColorMults();
    }

    public boolean hasOverlay() {
        return options != null && Boolean.TRUE.equals(options.getOverlay());
    }

    public boolean hasBetterFoliage() {
        return options != null && Boolean.TRUE.equals(options.getBetterFoliage());
    }

    public boolean hasRotateRandom() {
        return options != null && Boolean.TRUE.equals(options.getRotateRandom());
    }

    public boolean isNoDecay() {
        return options != null && Boolean.TRUE.equals(options.getNoDecay());
    }

    public boolean isAlphaRender() {
        return Boolean.TRUE.equals(alphaRender);
    }

    public boolean isNoParticle() {
        return options != null && Boolean.TRUE.equals(options.getNoParticle());
    }

    public String getBedType() {
        return options != null ? options.getBedType() : null;
    }

    public boolean hasBedType() {
        return options != null && options.getBedType() != null && !options.getBedType().isEmpty();
    }

    public boolean isAlwaysOn() {
        return options != null && Boolean.TRUE.equals(options.getAlwaysOn());
    }

    public boolean hasDown() {
        return options != null && Boolean.TRUE.equals(options.getHasDown());
    }

    public boolean hasClimb() {
        return options != null && Boolean.TRUE.equals(options.getHasClimb());
    }

    public boolean isSymmetrical() {
        return options != null && Boolean.TRUE.equals(options.getSymmetrical());
    }

    public String getWallSize() {
        if (options != null && options.getWallSize() != null) {
            return options.getWallSize();
        }
        return "normal";
    }

    public String getWallModel() {
        if (options != null && options.getWallModel() != null) {
            return options.getWallModel();
        }
        return "normal";
    }

    public boolean isConnectState() {
        return options != null && Boolean.TRUE.equals(options.getConnectstate());
    }

    public boolean isUnconnect() {
        return options != null && options.getUnconnect() != null;
    }

    public boolean getUnconnectDefault() {
        return options != null && Boolean.TRUE.equals(options.getUnconnect());
    }

    public List<StackElement> getStack() {
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

    public boolean isNoClimb() {
        return options != null && Boolean.TRUE.equals(options.getNoClimb());
    }

    public boolean canGrowDownward() {
        return hasDown();
    }

    public boolean isNoInWeb() {
        return options != null && Boolean.TRUE.equals(options.getNoInWeb());
    }

    public boolean isBarsModel() {
        return options != null && Boolean.TRUE.equals(options.getBarsModel());
    }

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
         * @param textures        List of texture paths
         * @param weight          Variant weight for random selection (minimum 1)
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

    public String getMapColor() {
        return mapColor;
    }

    public boolean hasMapColor() {
        return mapColor != null && !mapColor.isEmpty();
    }

    private static final Map<String, MapColor> SOUND_GROUP_TO_MAP_COLOR = createSoundGroupMapColorMap();

    private static Map<String, MapColor> createSoundGroupMapColorMap() {
        Map<String, MapColor> map = new HashMap<>();
        map.put("wood", MapColor.OAK_TAN);
        map.put("bamboo", MapColor.OAK_TAN);
        map.put("ladder", MapColor.OAK_TAN);
        map.put("stone", MapColor.STONE_GRAY);
        map.put("anvil", MapColor.STONE_GRAY);
        map.put("gravel", MapColor.STONE_GRAY);
        map.put("nether_bricks", MapColor.DARK_RED);
        map.put("netherite", MapColor.BLACK);
        map.put("metal", MapColor.IRON_GRAY);
        map.put("lantern", MapColor.IRON_GRAY);
        map.put("sand", MapColor.PALE_YELLOW);
        map.put("powder", MapColor.PALE_YELLOW);
        map.put("snow", MapColor.WHITE);
        map.put("grass", MapColor.DIRT_BROWN);
        map.put("plant", MapColor.DARK_GREEN);
        map.put("cloth", MapColor.WHITE_GRAY);
        map.put("wool", MapColor.WHITE_GRAY);
        map.put("glass", MapColor.CLEAR);
        map.put("slime", MapColor.PALE_GREEN);
        return map;
    }

    /**
     * Lookup table from MapColor name (our public JSON API) to the actual constant.
     * <p>Direct field references — not reflection — because Loom remaps Yarn names
     * (e.g. {@code MapColor.OAK_TAN}) to intermediary names (e.g. {@code field_16005})
     * during build. Reflection by raw string fails on the production-remapped jar
     * (server side), even though it works in dev. See: server boot logs reporting
     * "Unknown mapColor 'OAK_TAN'" while dev/runDatagen reports none.
     */
    private static final Map<String, MapColor> NAMED_MAP_COLORS = createNamedMapColors();

    private static Map<String, MapColor> createNamedMapColors() {
        Map<String, MapColor> m = new HashMap<>();
        m.put("CLEAR", MapColor.CLEAR);
        m.put("PALE_GREEN", MapColor.PALE_GREEN);
        m.put("PALE_YELLOW", MapColor.PALE_YELLOW);
        m.put("WHITE_GRAY", MapColor.WHITE_GRAY);
        m.put("BRIGHT_RED", MapColor.BRIGHT_RED);
        m.put("PALE_PURPLE", MapColor.PALE_PURPLE);
        m.put("IRON_GRAY", MapColor.IRON_GRAY);
        m.put("DARK_GREEN", MapColor.DARK_GREEN);
        m.put("WHITE", MapColor.WHITE);
        m.put("LIGHT_BLUE_GRAY", MapColor.LIGHT_BLUE_GRAY);
        m.put("DIRT_BROWN", MapColor.DIRT_BROWN);
        m.put("STONE_GRAY", MapColor.STONE_GRAY);
        m.put("WATER_BLUE", MapColor.WATER_BLUE);
        m.put("OAK_TAN", MapColor.OAK_TAN);
        m.put("OFF_WHITE", MapColor.OFF_WHITE);
        m.put("ORANGE", MapColor.ORANGE);
        m.put("MAGENTA", MapColor.MAGENTA);
        m.put("LIGHT_BLUE", MapColor.LIGHT_BLUE);
        m.put("YELLOW", MapColor.YELLOW);
        m.put("LIME", MapColor.LIME);
        m.put("PINK", MapColor.PINK);
        m.put("GRAY", MapColor.GRAY);
        m.put("LIGHT_GRAY", MapColor.LIGHT_GRAY);
        m.put("CYAN", MapColor.CYAN);
        m.put("PURPLE", MapColor.PURPLE);
        m.put("BLUE", MapColor.BLUE);
        m.put("BROWN", MapColor.BROWN);
        m.put("GREEN", MapColor.GREEN);
        m.put("RED", MapColor.RED);
        m.put("BLACK", MapColor.BLACK);
        m.put("GOLD", MapColor.GOLD);
        m.put("DIAMOND_BLUE", MapColor.DIAMOND_BLUE);
        m.put("LAPIS_BLUE", MapColor.LAPIS_BLUE);
        m.put("EMERALD_GREEN", MapColor.EMERALD_GREEN);
        m.put("SPRUCE_BROWN", MapColor.SPRUCE_BROWN);
        m.put("DARK_RED", MapColor.DARK_RED);
        m.put("TERRACOTTA_WHITE", MapColor.TERRACOTTA_WHITE);
        m.put("TERRACOTTA_ORANGE", MapColor.TERRACOTTA_ORANGE);
        m.put("TERRACOTTA_MAGENTA", MapColor.TERRACOTTA_MAGENTA);
        m.put("TERRACOTTA_LIGHT_BLUE", MapColor.TERRACOTTA_LIGHT_BLUE);
        m.put("TERRACOTTA_YELLOW", MapColor.TERRACOTTA_YELLOW);
        m.put("TERRACOTTA_LIME", MapColor.TERRACOTTA_LIME);
        m.put("TERRACOTTA_PINK", MapColor.TERRACOTTA_PINK);
        m.put("TERRACOTTA_GRAY", MapColor.TERRACOTTA_GRAY);
        m.put("TERRACOTTA_LIGHT_GRAY", MapColor.TERRACOTTA_LIGHT_GRAY);
        m.put("TERRACOTTA_CYAN", MapColor.TERRACOTTA_CYAN);
        m.put("TERRACOTTA_PURPLE", MapColor.TERRACOTTA_PURPLE);
        m.put("TERRACOTTA_BLUE", MapColor.TERRACOTTA_BLUE);
        m.put("TERRACOTTA_BROWN", MapColor.TERRACOTTA_BROWN);
        m.put("TERRACOTTA_GREEN", MapColor.TERRACOTTA_GREEN);
        m.put("TERRACOTTA_RED", MapColor.TERRACOTTA_RED);
        m.put("TERRACOTTA_BLACK", MapColor.TERRACOTTA_BLACK);
        m.put("DULL_RED", MapColor.DULL_RED);
        m.put("DULL_PINK", MapColor.DULL_PINK);
        m.put("DARK_CRIMSON", MapColor.DARK_CRIMSON);
        m.put("TEAL", MapColor.TEAL);
        m.put("DARK_AQUA", MapColor.DARK_AQUA);
        m.put("DARK_DULL_PINK", MapColor.DARK_DULL_PINK);
        m.put("BRIGHT_TEAL", MapColor.BRIGHT_TEAL);
        m.put("DEEPSLATE_GRAY", MapColor.DEEPSLATE_GRAY);
        m.put("RAW_IRON_PINK", MapColor.RAW_IRON_PINK);
        m.put("LICHEN_GREEN", MapColor.LICHEN_GREEN);
        return m;
    }

    /**
     * Resolves a {@link MapColor} from a string name matching a constant on
     * {@link MapColor} (e.g. "STONE_GRAY"). Case-insensitive. Returns null on miss.
     */
    private static MapColor resolveNamedMapColor(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        return NAMED_MAP_COLORS.get(name.toUpperCase(Locale.ROOT));
    }

    /**
     * Resolves the effective {@link MapColor} for this definition. Checks the explicit
     * {@code mapColor} field first, then falls back to a soundGroup-derived default.
     * Returns null if no sensible color can be determined (caller should leave settings unchanged).
     */
    public MapColor resolveMapColor() {
        if (hasMapColor()) {
            MapColor named = resolveNamedMapColor(mapColor);
            if (named != null) {
                return named;
            }
            WesterosBlocks.LOGGER.warn("Unknown mapColor '{}' on block '{}' — using soundGroup fallback",
                    mapColor, blockName);
        }
        if (soundGroup != null) {
            return SOUND_GROUP_TO_MAP_COLOR.get(soundGroup.toLowerCase(Locale.ROOT));
        }
        return null;
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

        // Apply map color (used by paper maps + minimap mods like Xaero/Dynmap).
        // Explicit mapColor wins; otherwise derive from soundGroup. Skip when copyFrom
        // is set with no explicit override so we inherit the source block's color.
        if (hasMapColor() || copyFrom == null) {
            MapColor resolved = resolveMapColor();
            if (resolved != null) {
                settings = settings.mapColor(resolved);
            }
        }

        // Apply luminance (light level 0-15)
        int light = getLuminance();
        if (light > 0) {
            settings = settings.luminance(lum -> light);
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
     * @param copyFrom   Optional block to copy from
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