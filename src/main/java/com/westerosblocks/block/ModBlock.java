package com.westerosblocks.block;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.custom.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

import java.util.*;

// Our block configuration data that's populated using GSON, sourced from blocks.json
public class ModBlock extends ModBlockStateRecord {
    private static final float DEF_FLOAT = -999.0F;
    public static final int DEF_INT = -999;
    public static final String LAYER_SENSITIVE = "layerSensitive";
    public String blockName; // Locally unique block name
    public String blockType = "solid"; // Block type ('solid', 'liquid', 'plant', 'log', 'stairs', etc)
    public float hardness = DEF_FLOAT;  // Block hardness
    public String stepSound = null; // Step sound (powder, wood, gravel, grass, stone, metal, glass, cloth, sand, snow, ladder, anvil)
    public String material = null; // Generic material (air, grass, ground, wood, rock, iron, anvil, water, lava, leaves, plants, vine, sponge, etc.)
    public float resistance = DEF_FLOAT; // Explosion resistance
    public int lightOpacity = DEF_INT;
    public List<HarvestLevel> harvestLevel = null; // List of harvest levels
    public int fireSpreadSpeed = 0;
    public int flammability = 0;
    public String creativeTab = null; // Creative tab for items
    public List<String> customTags = null; // If block should add any custom tags
    public String type = ""; // Type field (used for plant types or other block type specific values)
    public boolean alphaRender = false; // If true, do render on pass 2 (for alpha blending)
    public Boolean ambientOcclusion = null; // Set ambient occlusion (default is true)
    public boolean nonOpaque = false; // If true, does not block visibility of shared faces (solid blocks) and doesn't allow torches
    public String label; // Label for item associated with block
    public String itemTexture = null; // Item texture, if any
    public int itemTextureIndex = 0; // Index of texture for item icon
    public List<String> soundList = null; // List of custom sound names or sound IDs (for 'sound' blocks)
    public List<String> tooltips; // the block tooltips
    public List<ModBlockStateRecord> stack = null;  // List of elements for a stack, first is bottom-most (for *-stack)
    public List<ModBlockStateRecord> states = null;
    private StateProperty stateProp = null;
    public static final String SHAPE_BOX = "box"; // Shape for normal cuboid (box)
    public static final String SHAPE_CROSSED = "crossed"; // Shape for plant-style crossed (texture 0)
    public String woodType = null;
    public String particle;
    public DisplayProperties display = null;

    private static final Map<String, BlockSoundGroup> stepSoundTable = new HashMap<>();
    private static final Map<String, ModBlockFactory> typeTable = new HashMap<>();
    private transient Map<String, String> parsedType;

    public static class DisplayProperties {
        public GuiProperties gui;

        public static class GuiProperties {
            public float[] rotation;
            public float[] scale;
            public float[] translation;
        }
    }

    public static class HarvestLevel {
        public String tool;
        public int level;
    }

    public static class RandomTextureSet {
        public List<String> textures = null; // List of textures (for single texture set)
        public Integer weight = null;        // Weight for texture set (default = 1)

        public int getTextureCount() {
            return textures != null ? textures.size() : 0;
        }

        public String getTextureByIndex(int index) {
            if (textures == null || textures.isEmpty()) {
                return null;
            }
            return textures.get(index % textures.size());
        }

    }

    public static class BoundingBox {
        public float xMin = 0.0F;
        public float xMax = 1.0F;
        public float yMin = 0.0F;
        public float yMax = 1.0F;
        public float zMin = 0.0F;
        public float zMax = 1.0F;


        public BoundingBox() {
        }

        public BoundingBox(float x0, float y0, float z0, float x1, float y1, float z1) {
            this.xMin = x0;
            this.xMax = x1;
            this.yMin = y0;
            this.yMax = y1;
            this.zMin = z0;
            this.zMax = z1;
        }

        private transient VoxelShape aabb = null;

        public VoxelShape getAABB() {
            if (aabb == null) {
                aabb = VoxelShapes.cuboid(xMin, yMin, zMin, xMax, yMax, zMax);
            }
            return aabb;
        }

    }

    public static class Vector {
        float x, y, z;

        private void rotate(int xcnt, int ycnt, int zcnt) {
            double xx, yy, zz;
            xx = x - 0.5F;
            yy = y - 0.5F;
            zz = z - 0.5F; // Shoft to center of block
            /* Do X rotation */
            double rot = Math.toRadians(xcnt);
            double nval = zz * Math.sin(rot) + yy * Math.cos(rot);
            zz = zz * Math.cos(rot) - yy * Math.sin(rot);
            yy = nval;
            /* Do Y rotation */
            rot = Math.toRadians(ycnt);
            nval = xx * Math.cos(rot) - zz * Math.sin(rot);
            zz = xx * Math.sin(rot) + zz * Math.cos(rot);
            xx = nval;
            /* Do Z rotation */
            rot = Math.toRadians(zcnt);
            nval = yy * Math.sin(rot) + xx * Math.cos(rot);
            yy = yy * Math.cos(rot) - xx * Math.sin(rot);
            xx = nval;
            x = (float) xx + 0.5F;
            y = (float) yy + 0.5F;
            z = (float) zz + 0.5F; // Shoft back to corner
            // Clip value
            if (x > 1.0F)
                x = 1.0F;
            if (y > 1.0F)
                y = 1.0F;
            if (z > 1.0F)
                z = 1.0F;
            if (x < 0.0F)
                x = 0.0F;
            if (y < 0.0F)
                y = 0.0F;
            if (z < 0.0F)
                z = 0.0F;
        }

    }

    public static class StateProperty extends Property<String> {
        public final ImmutableList<String> values;
        public final ImmutableMap<String, String> valMap;
        public final String defValue;

        public StateProperty(List<String> stateIDs) {
            super("state", String.class);
            Map<String, String> map = Maps.newHashMap();
            List<String> vals = new ArrayList<>();
            for (String s : stateIDs) {
                map.put(s, s);
                vals.add(s);
            }
            this.values = ImmutableList.copyOf(vals);
            this.valMap = ImmutableMap.copyOf(map);
            this.defValue = stateIDs.getFirst();
        }

        @Override
        public Collection<String> getValues() {
            return this.values;
        }

        @Override
        public String name(String value) {
            return value;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            } else if (obj instanceof StateProperty stateproperty && super.equals(obj)) {
                return this.values.equals(stateproperty.values);
            } else {
                return false;
            }
        }

        @Override
        public Optional<String> parse(String value) {
            return Optional.ofNullable(this.valMap.get(value));
        }

        public int getIndex(String val) {
            int v = this.values.indexOf(val);
            return Math.max(v, 0);
        }
    }

    public StateProperty buildStateProperty() {
        return stateProp;
    }

    public String getType() {
        return this.type;
    }

    public String getWoodType() {
        return this.woodType;
    }

    public ModBlockStateRecord getStackElementByIndex(int idx) {
        if ((stack != null) && (!stack.isEmpty())) {
            if (idx >= stack.size()) {
                idx = stack.size() - 1;
            }
            return stack.get(idx);
        }
        return null;
    }

    public enum CuboidRotation {
        NONE(0, 0, 0, new int[]{0, 1, 2, 3, 4, 5}, new int[]{0, 0, 0, 0, 0, 0}),
        ROTY45(0, 45, 0, new int[]{0, 1, 4, 5, 2, 3}, new int[]{315, 45, 0, 0, 0, 0}),
        ROTY90(0, 90, 0, new int[]{0, 1, 4, 5, 3, 2}, new int[]{270, 90, 0, 0, 0, 0}),
        ROTY135(0, 135, 0, new int[]{0, 1, 3, 2, 4, 5}, new int[]{225, 135, 0, 0, 0, 0}),
        ROTY180(0, 180, 0, new int[]{0, 1, 3, 2, 5, 4}, new int[]{180, 180, 0, 0, 0, 0}),
        ROTY225(0, 225, 0, new int[]{0, 1, 5, 4, 3, 2}, new int[]{135, 225, 0, 0, 0, 0}),
        ROTY270(0, 270, 0, new int[]{0, 1, 5, 4, 2, 3}, new int[]{90, 270, 0, 0, 0, 0}),
        ROTY315(0, 315, 0, new int[]{0, 1, 2, 3, 5, 4}, new int[]{45, 315, 0, 0, 0, 0}),
        ROTZ90(0, 0, 90, new int[]{5, 4, 2, 3, 0, 1}, new int[]{270, 90, 270, 90, 90, 90}),
        ROTZ270(0, 0, 270, new int[]{4, 5, 2, 3, 1, 0}, new int[]{90, 270, 90, 270, 270, 270});

        final int xrot, yrot, zrot;
        final int[] txtidx;
        final int[] txtrot;

        CuboidRotation(int xr, int yr, int zr, int[] txt_idx, int[] txt_rot) {
            xrot = xr;
            yrot = yr;
            zrot = zr;
            txtidx = txt_idx;
            txtrot = txt_rot;
        }
    }

    public static class Cuboid extends BoundingBox {
        public int[] sideTextures = null;
        public int[] sideRotations = {0, 0, 0, 0, 0, 0};
        public String shape = SHAPE_BOX; // "box" = normal cuboid, "crossed" = plant-style crossed (texture 0)
        public boolean[] noTint;


        public Cuboid rotateCuboid(CuboidRotation rot) {
            Cuboid c = new Cuboid();
            Vector v0 = new Vector();
            Vector v1 = new Vector();
            v0.x = xMin;
            v0.y = yMin;
            v0.z = zMin;
            v1.x = xMax;
            v1.y = yMax;
            v1.z = zMax;
            // Rotate corners
            v0.rotate(rot.xrot, rot.yrot, rot.zrot);
            v1.rotate(rot.xrot, rot.yrot, rot.zrot);
            // Compute net min/max
            c.xMin = Math.min(v0.x, v1.x);
            c.xMax = Math.max(v0.x, v1.x);
            c.yMin = Math.min(v0.y, v1.y);
            c.yMax = Math.max(v0.y, v1.y);
            c.zMin = Math.min(v0.z, v1.z);
            c.zMax = Math.max(v0.z, v1.z);
            if (this.sideTextures != null) {
                c.sideTextures = new int[rot.txtidx.length];
                int cnt = this.sideTextures.length;
                for (int i = 0; i < c.sideTextures.length; i++) {
                    int newidx = rot.txtidx[i];
                    if (newidx < cnt) {
                        c.sideTextures[i] = this.sideTextures[newidx];
                    } else {
                        c.sideTextures[i] = this.sideTextures[cnt - 1];
                    }
                }
            } else {
                c.sideTextures = rot.txtidx;
            }
            c.sideRotations = rot.txtrot;
            c.shape = this.shape;
            return c;
        }

        public Cuboid() {
        }

        public Cuboid(float x0, float y0, float z0, float x1, float y1, float z1) {
            this(x0, y0, z0, x1, y1, z1, null, null);
        }

        public Cuboid(float x0, float y0, float z0, float x1, float y1, float z1, int[] sidetextures) {
            this(x0, y0, z0, x1, y1, z1, sidetextures, null);
        }

        public Cuboid(float x0, float y0, float z0, float x1, float y1, float z1, int[] sidetextures, boolean[] noTint) {
            this.xMin = x0;
            this.xMax = x1;
            this.yMin = y0;
            this.yMax = y1;
            this.zMin = z0;
            this.zMax = z1;
            this.sideTextures = sidetextures;
            this.noTint = noTint;
        }

    }

    public Map<String, String> getMappedType() {
        if (parsedType == null) {
            parsedType = new HashMap<>();
            String[] toks = type.split(",");
            for (String tok : toks) {
                String[] flds = tok.split(":");
                if (flds.length < 2)
                    continue;
                parsedType.put(flds[0], flds[1]);
            }
        }
        return parsedType;
    }

    public String getTypeValue(String key, String defval) {
        String v = getMappedType().get(key);
        if (v == null) v = defval;
        return v;
    }

    public String getTypeValue(String key) {
        return getTypeValue(key, "");
    }

    private transient boolean didInit = false;

    public void doInit() {
        if (didInit)
            return;
        // If no states, just use base as the one state
        if (this.states == null) {
            this.states = Collections.singletonList(this);
        }
        if (this.ambientOcclusion == null) {
            this.ambientOcclusion = true; // Default to true
        }
        // If overlay textures, set nonOpaque to true
        if (this.overlayTextures != null) {
            this.nonOpaque = true;
        }
        for (ModBlockStateRecord rec : this.states) {
            // if states array, allow attributes to be inherited from base def if not specified
            if (rec.boundingBox == null) rec.boundingBox = this.boundingBox;
            if (rec.cuboids == null) rec.cuboids = this.cuboids;
            if (rec.collisionBoxes == null) rec.collisionBoxes = this.collisionBoxes;
            if (rec.supportBoxes == null) rec.supportBoxes = this.supportBoxes;
            if (rec.textures == null) rec.textures = this.textures;
            if (rec.randomTextures == null) rec.randomTextures = this.randomTextures;
            if (rec.overlayTextures == null) rec.overlayTextures = this.overlayTextures;
            if (rec.colorMult.equals("#FFFFFF")) rec.colorMult = this.colorMult;
            if (rec.colorMults == null) rec.colorMults = this.colorMults;
            rec.doStateRecordInit();

            // If any state has overlay textures, set nonOpaque to true
            if (rec.overlayTextures != null) {
                this.nonOpaque = true;
            }
        }
        // If stacks, process these too
        if (this.stack != null) {
            for (ModBlockStateRecord se : this.stack) {
                se.doStateRecordInit();
            }
        }

        if (this.states.size() > 1) {
            ArrayList<String> ids = new ArrayList<>();
            for (int i = 0; i < states.size(); i++) {
                ModBlockStateRecord rec = states.get(i);
                if (rec.stateID == null) {
                    rec.stateID = String.format("state%d", i);
                }
                ids.add(rec.stateID);
            }
            stateProp = new StateProperty(ids);
        }

        didInit = true;
    }

    private static final Map<String, long[]> perfCounts = new HashMap<>();

    public Block createBlock() {
        try {
            doInit(); // Prime the block model
        } catch (Exception x) {
            WesterosBlocks.LOGGER.error("Exception during doInit: blockName={}", this.blockName);
            throw x;
        }
        long[] pc = perfCounts.computeIfAbsent(blockType, k -> new long[2]);
        ModBlockFactory bf = typeTable.get(blockType);

        if (bf == null) {
            WesterosBlocks.LOGGER.error(String.format("Invalid blockType '%s' in block '%s'", blockType, blockName));
            return null;
        }
        long start = System.currentTimeMillis();
        Block blk = bf.buildBlockClass(this);
        long end = System.currentTimeMillis();
        pc[0]++;
        pc[1] += (end - start);

        return blk;
    }

    public static void dumpBlockPerf() {
        WesterosBlocks.LOGGER.info("Block Creation Performance Statistics:");
        WesterosBlocks.LOGGER.info(String.format("%-20s %-10s %-15s %-15s", "Block Type", "Count", "Total (ms)", "Avg (ms)"));
        WesterosBlocks.LOGGER.info("-".repeat(60));

        perfCounts.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    String blockType = entry.getKey();
                    long count = entry.getValue()[0];
                    long totalMs = entry.getValue()[1];
                    double avgMs = count > 0 ? (double) totalMs / count : 0;

                    WesterosBlocks.LOGGER.info(String.format("%-20s %-10d %-15d %-15.2f",
                            blockType, count, totalMs, avgMs));
                });
    }

    public Block registerRenderType(Block block, boolean isSolid, boolean isTransparent) {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            if (this.alphaRender) {
                BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getTranslucent());
            } else if (!isSolid) {
                BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout());
            } else if (isTransparent) {
                BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutoutMipped());
            }
        }
        return block;
    }

    public AbstractBlock.Settings applyCustomProperties() {
        AbstractBlock.Settings settings = AbstractBlock.Settings.create();

        if (this.hardness >= 0.0F) {
            if (this.resistance >= 0.0) {
                settings.strength(this.hardness, this.resistance);
            } else {
                settings.strength(this.hardness);
            }
        }

        if (this.stepSound != null) {
            settings.sounds(getSoundType());
        }

        // state-dependent light levels
        if (this.stateProp != null) {
            Map<String, Integer> lightLevels = new HashMap<>();
            for (ModBlockStateRecord sr : this.states) {
                if (sr.lightValue > 0) {
                    lightLevels.put(sr.stateID, (int) (sr.lightValue));
                }
            }
            if (!lightLevels.isEmpty()) {
                settings.luminance((state) -> {
                    if (state.contains(this.stateProp)) {
                        String currentState = state.get(this.stateProp);
                        return lightLevels.getOrDefault(currentState, 0);
                    }
                    return 0;
                });
            }
        // handle simple light level
        } else if (this.lightValue > 0 || !this.states.isEmpty() && this.states.getFirst().lightValue > 0.0F) {
            float lightLevel = Math.max(lightValue, this.states.isEmpty() ? 0 : this.states.getFirst().lightValue);
            settings.luminance((state) -> (int) (lightLevel));
        }
        // simple light level
        else if (this.lightValue > 0 || !states.isEmpty() && states.getFirst().lightValue > 0.0F) {
            float lightLevel = Math.max(lightValue, states.isEmpty() ? 0 : states.getFirst().lightValue);
            settings.luminance((state) -> (int) (lightLevel));
        }

        // transparency/occlusion
        if ((!this.ambientOcclusion) || (this.nonOpaque)) {
            settings.nonOpaque().blockVision(ModBlock::never);
        }

        return settings;
    }

    public static Map<String, String> parseTypeParameters(String type) {
        Map<String, String> params = new HashMap<>();
        for (String token : type.split(",")) {
            String[] parts = token.split(":");
            if (parts.length == 2) {
                params.put(parts[0].trim(), parts[1].trim());
            }
        }
        return params;
    }

    private static boolean never(BlockState state, BlockView world, BlockPos pos) {
        return false;
    }

    // Get customized collision box for default solid block
    public VoxelShape makeCollisionBoxShape() {
        if (collisionBoxes == null) {
            return VoxelShapes.fullCube();  // Default to solid block
        }
        VoxelShape s = VoxelShapes.empty();
        for (BoundingBox b : collisionBoxes) {
            s = VoxelShapes.union(s, b.getAABB());
        }
        return s;
    }

    public String getBlockName() {
        return this.blockName;
    }

    // TODO move this out of here
    public static void initialize() {
        stepSoundTable.put("powder", BlockSoundGroup.SAND);
        stepSoundTable.put("wood", BlockSoundGroup.WOOD);
        stepSoundTable.put("gravel", BlockSoundGroup.GRAVEL);
        stepSoundTable.put("grass", BlockSoundGroup.GRASS);
        stepSoundTable.put("stone", BlockSoundGroup.STONE);
        stepSoundTable.put("metal", BlockSoundGroup.METAL);
        stepSoundTable.put("glass", BlockSoundGroup.GLASS);
        stepSoundTable.put("cloth", BlockSoundGroup.WOOL);
        stepSoundTable.put("sand", BlockSoundGroup.SAND);
        stepSoundTable.put("snow", BlockSoundGroup.SNOW);
        stepSoundTable.put("ladder", BlockSoundGroup.LADDER);
        stepSoundTable.put("anvil", BlockSoundGroup.ANVIL);
        stepSoundTable.put("plant", BlockSoundGroup.CROP);
        stepSoundTable.put("slime", BlockSoundGroup.FUNGUS);

        // Tab table
//        tabTable.put("buildingBlocks", BuiltInRegistries.CREATIVE_MODE_TAB.getOrThrow(CreativeModeTabs.BUILDING_BLOCKS));
        //tabTable.put("decorations", CreativeModeTabs.DECORATIONS);
//        tabTable.put("redstone", BuiltInRegistries.CREATIVE_MODE_TAB.getOrThrow(CreativeModeTabs.REDSTONE_BLOCKS));
        //tabTable.put("transportation", CreativeModeTabs.TRANSPORTATION);
        //tabTable.put("misc", CreativeModeTabs.MISC);
//        tabTable.put("food", BuiltInRegistries.CREATIVE_MODE_TAB.getOrThrow(CreativeModeTabs.FOOD_AND_DRINKS));
//        tabTable.put("tools", BuiltInRegistries.CREATIVE_MODE_TAB.getOrThrow(CreativeModeTabs.TOOLS_AND_UTILITIES));
//        tabTable.put("combat", BuiltInRegistries.CREATIVE_MODE_TAB.getOrThrow(CreativeModeTabs.COMBAT));
        //tabTable.put("brewing", CreativeModeTabs.BREWING);
//        tabTable.put("materials", BuiltInRegistries.CREATIVE_MODE_TAB.getOrThrow(CreativeModeTabs.INGREDIENTS));

        // Standard block types
        typeTable.put("solid", new WCSolidBlock.Factory());
        typeTable.put("stair", new WCStairBlock.Factory());
        typeTable.put("log", new WCLogBlock.Factory());
        typeTable.put("plant", new WCPlantBlock.Factory());
        typeTable.put("crop", new WCCropBlock.Factory());
        typeTable.put("slab", new WCSlabBlock.Factory());
        typeTable.put("wall", new WCWallBlock.Factory());
        typeTable.put("fence", new WCFenceBlock.Factory());
        typeTable.put("web", new WCWebBlock.Factory());
        typeTable.put("torch", new WCTorchBlock.Factory());
        typeTable.put("fan", new WCFanBlock.Factory());
        typeTable.put("ladder", new WCLadderBlock.Factory());
        typeTable.put("cuboid", new WCCuboidBlock.Factory());
        typeTable.put("cuboid-nsew", new WCCuboidNSEWBlock.Factory());
        typeTable.put("cuboid-16way", new WCCuboid16WayBlock.Factory());
//        typeTable.put("cuboid-wall-16way", new WCCuboidWall16WayBlock.Factory());
        typeTable.put("cuboid-ne", new WCCuboidNEBlock.Factory());
        typeTable.put("cuboid-nsewud", new WCCuboidNSEWUDBlock.Factory());
        typeTable.put("cuboid-nsew-stack", new WCCuboidNSEWStackBlock.Factory());
        typeTable.put("door", new WCDoorBlock.Factory());
        typeTable.put("fire", new WCFireBlock.Factory());
        typeTable.put("leaves", new WCLeavesBlock.Factory());
        typeTable.put("pane", new WCPaneBlock.Factory());
        typeTable.put("layer", new WCLayerBlock.Factory());
        typeTable.put("soulsand", new WCSoulSandBlock.Factory());
        typeTable.put("rail", new WCRailBlock.Factory());
        typeTable.put("cake", new WCCakeBlock.Factory());
        typeTable.put("bed", new WCBedBlock.Factory());
        typeTable.put("sand", new WCSandBlock.Factory());
        typeTable.put("halfdoor", new WCHalfDoorBlock.Factory());
        typeTable.put("furnace", new WCFurnaceBlock.Factory());
        typeTable.put("sound", new WCSoundBlock.Factory());
        typeTable.put("trapdoor", new WCTrapDoorBlock.Factory());
        typeTable.put("beacon", new WCBeaconBlock.Factory());
        typeTable.put("vines", new WCVinesBlock.Factory());
        typeTable.put("flowerpot", new WCFlowerPotBlock.Factory());
        typeTable.put("fencegate", new WCFenceGateBlock.Factory());
        typeTable.put("particle", new WCParticleEmitterBlock.Factory());
        typeTable.put("cuboid-8way", new WCCuboid8WayBlock.Factory());
        typeTable.put("cuboid-vertical-8way", new WCCuboidVertical8WayBlock.Factory());
    }

    public BlockSoundGroup getSoundType() {
        BlockSoundGroup ss = stepSoundTable.get(stepSound);
        if (ss == null) {
            WesterosBlocks.LOGGER.warn("Invalid step sound '{}' in block '{}'", stepSound, blockName);
            return BlockSoundGroup.STONE;
        }
        return ss;
    }

    public String getBlockColorMapResource() {
        String res = null;
        String blockColor = colorMult;
        if (blockColor == null && colorMults != null && !colorMults.isEmpty()) {
            blockColor = colorMults.getFirst();
        }
        if ((blockColor != null) && (!blockColor.startsWith("#"))) {
            String[] tok = blockColor.split(":");
            if (tok.length == 1) {
                if (tok[0].startsWith("textures/"))
                    tok[0] = tok[0].substring(9);
                res = WesterosBlocks.MOD_ID + ":" + tok[0];
            } else {
                if (tok[1].startsWith("textures/"))
                    tok[1] = tok[1].substring(9);
                res = tok[0] + ":" + tok[1];
            }
        }
        return res;
    }
}