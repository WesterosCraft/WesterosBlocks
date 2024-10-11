package com.westeroscraft.westerosblocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.westeroscraft.westerosblocks.blocks.*;


import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.chunk.RenderChunkRegion;
import net.minecraft.client.resources.LegacyStuffWrapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegisterEvent;

//
// Template for block configuration data (populated using GSON)
//
public class WesterosBlockDef extends WesterosBlockStateRecord {
    private static final float DEF_FLOAT = -999.0F;
    public static final int DEF_INT = -999;

    public static final String LAYER_SENSITIVE = "layerSensitive";

    public String blockName; // Locally unique block name
    public String blockType = "solid"; // Block type ('solid', 'liquid', 'plant', 'log', 'stairs', etc)
    public float hardness = DEF_FLOAT; // Block hardness
    public String stepSound = null; // Step sound (powder, wood, gravel, grass, stone, metal, glass, cloth, sand,
    // snow, ladder, anvil)
    public String material = null; // Generic material (ai, grass, ground, wood, rock, iron, anvil, water, lava,
    // leaves, plants, vine, sponge, etc)
    public float resistance = DEF_FLOAT; // Explosion resistance
    public int lightOpacity = DEF_INT; // Light opacity
    public List<HarvestLevel> harvestLevel = null; // List of harvest levels
    public int fireSpreadSpeed = 0; // Fire spread speed
    public int flamability = 0; // Flamability
    public String creativeTab = null; // Creative tab for items
    public List<String> customTags = null;    // If block should add any custom tags

    public String type = ""; // Type field (used for plant types or other block type specific values)

    public boolean alphaRender = false; // If true, do render on pass 2 (for alpha blending)
    public Boolean ambientOcclusion = null; // Set ambient occlusion (default is true)
    public boolean nonOpaque = false; // If true, does not block visibility of shared faces (solid blocks) and doesn't
    // allow torches
    // ('solid', 'sound', 'sand', 'soulsand' blocks)
    public String label; // Label for item associated with block
    public String itemTexture = null; // Item texture, if any
    public int itemTextureIndex = 0; // Index of texture for item icon
    public List<String> soundList = null; // List of custom sound names or sound IDs (for 'sound' blocks)

    public List<WesterosBlockStateRecord> stack = null; // List of elements for a stack, first is bottom-most (for *-stack)

    public List<WesterosBlockStateRecord> states = null;

    private StateProperty stateProp = null;

    public String connectBy = "block";    // Connection logic - by block, material - only for CTM-like blocks

    public String legacyBlockID = null;
    public List<String> legacyBlockIDList = null;

    public boolean isConnectMatch(BlockState bs1, BlockState bs2) {
        if (this.connectBy.equals("material")) {
            return AuxMaterial.getMaterial(bs1) == AuxMaterial.getMaterial(bs2);
        } else {
            return bs1.getBlock() == bs2.getBlock();
        }
    }

    public static class RandomTextureSet {
        public List<String> textures = null; // List of textures (for single texture set)
        public Integer weight = null;        // Weight for texture set (default = 1)

        // Get number of base textures
        public int getTextureCount() {
            if (textures != null) {
                return textures.size();
            }
            return 0;
        }

        public String getTextureByIndex(int idx) {
            int cnt = getTextureCount();
            if (cnt > 0) {
                if (idx >= cnt) {
                    idx = cnt - 1;
                }
                return textures.get(idx);
            }
            return null;
        }

    }

    ;

    public static class StackElement {
        public List<String> textures = null; // List of textures
        public BoundingBox boundingBox = null; // Bounding box
        public List<Cuboid> cuboids = null; // List of cuboids composing block (for 'cuboid', and others)
        public List<BoundingBox> collisionBoxes = null; // For 'solid', used for raytrace (arrow shots)
        public List<RandomTextureSet> randomTextures = null;    // On supported blocks (solid, leaves, slabs, stairs),
        // defines sets of textures used for additional random models
        // If randomTextures is used, textures is ignored

        public String getTextureByIndex(int idx) {
            if ((textures != null) && (textures.size() > 0)) {
                if (idx >= textures.size()) {
                    idx = textures.size() - 1;
                }
                return textures.get(idx);
            }
            return null;
        }

        // Get number of random texture sets
        public int getRandomTextureSetCount() {
            if ((randomTextures != null) && (randomTextures.size() > 0)) {
                return randomTextures.size();
            }
            return 0;
        }

        // Get given random texture set
        public RandomTextureSet getRandomTextureSet(int setnum) {
            if ((randomTextures != null) && (randomTextures.size() > 0)) {
                if (setnum >= randomTextures.size()) {
                    setnum = randomTextures.size() - 1;
                }
                return randomTextures.get(setnum);
            }
            return null;
        }

    }

    ;

    public static class HarvestLevel {
        public String tool;
        public int level;
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
                aabb = Shapes.box(xMin, yMin, zMin, xMax, yMax, zMax);
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

    public static enum CuboidRotation {
        NONE(0, 0, 0, new int[]{0, 1, 2, 3, 4, 5}, new int[]{0, 0, 0, 0, 0, 0}),
        ROTY90(0, 90, 0, new int[]{0, 1, 4, 5, 3, 2}, new int[]{270, 90, 0, 0, 0, 0}),
        ROTY180(0, 180, 0, new int[]{0, 1, 3, 2, 5, 4}, new int[]{180, 180, 0, 0, 0, 0}),
        ROTY270(0, 270, 0, new int[]{0, 1, 5, 4, 2, 3}, new int[]{90, 270, 0, 0, 0, 0}),
        ROTZ90(0, 0, 90, new int[]{5, 4, 2, 3, 0, 1}, new int[]{270, 90, 270, 90, 90, 90}),
        ROTZ270(0, 0, 270, new int[]{4, 5, 2, 3, 1, 0}, new int[]{90, 270, 90, 270, 270, 270});

        final int xrot, yrot, zrot;
        final int txtidx[];
        final int txtrot[];

        CuboidRotation(int xr, int yr, int zr, int[] txt_idx, int[] txt_rot) {
            xrot = xr;
            yrot = yr;
            zrot = zr;
            txtidx = txt_idx;
            txtrot = txt_rot;
        }

        public int getRotY() {
            return yrot;
        }
    }

    // Shape for normal cuboid (box)
    public static final String SHAPE_BOX = "box";
    // Shape for crossed squares (plant-style) (texture is index 0 in list)
    public static final String SHAPE_CROSSED = "crossed";

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

        public Cuboid(float x0, float y0, float z0, float x1, float y1, float z1, int[] sidetextures, boolean noTint[]) {
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

    public static class Particle {
        public float x = 0.5F, y = 0.5F, z = 0.5F; // Default position of effect
        public float vx = 0.0F, vy = 0.0F, vz = 0.0F; // Default velocity of effect
        public float xrand = 0.0F, yrand = 0.0F, zrand = 0.0F; // Default random position of effect (-rand to +rand)
        public float vxrand = 0.0F, vyrand = 0.0F, vzrand = 0.0F; // Default random velocity of effect (-rand to +rand)
        public float chance = 1.0F;
        public String particle;
    }

    // Base color multiplier (fixed)
    public static abstract class ColorMultHandler {
        ColorMultHandler() {
        }

        @OnlyIn(Dist.CLIENT)
        public int getItemColor(ItemStack stack, int tintIndex) {
            BlockColors blockColors = Minecraft.getInstance().getBlockColors();
            BlockState BlockState = ((BlockItem) stack.getItem()).getBlock().defaultBlockState();
            return blockColors.getColor(BlockState, null, null, tintIndex);
        }

        @OnlyIn(Dist.CLIENT)
        public abstract int getColor(BlockState arg0, BlockAndTintGetter arg1, BlockPos arg2, int arg3);

        public abstract int getColor(Biome biome, double x, double z);
    }

    // Fixed color multiplier (fixed)
    public static class FixedColorMultHandler extends ColorMultHandler {
        protected int fixedMult;

        FixedColorMultHandler(int mult) {
            fixedMult = mult;
        }

        @Override
        @OnlyIn(Dist.CLIENT)
        public int getColor(BlockState arg0, BlockAndTintGetter arg1, BlockPos arg2, int arg3) {
            return fixedMult;
        }

        @Override
        public int getColor(Biome biome, double x, double z) {
            return fixedMult;
        }


    }

    // Foliage color multiplier
    public static class FoliageColorMultHandler extends ColorMultHandler {
        FoliageColorMultHandler() {
        }

        @Override
        @OnlyIn(Dist.CLIENT)
        public int getColor(BlockState arg0, BlockAndTintGetter world, BlockPos pos, int arg3) {
            if (world != null && pos != null)
                return BiomeColors.getAverageFoliageColor(world, pos);
            else
                return FoliageColor.getDefaultColor();
        }

        @Override
        public int getColor(Biome biome, double x, double z) {
            return biome.getFoliageColor();
        }

    }

    // Grass color multiplier
    public static class GrassColorMultHandler extends ColorMultHandler {
        GrassColorMultHandler() {
        }

        @Override
        @OnlyIn(Dist.CLIENT)
        public int getColor(BlockState arg0, BlockAndTintGetter world, BlockPos pos, int arg3) {
            if (world != null && pos != null)
                return BiomeColors.getAverageGrassColor(world, pos);
            else
                return GrassColor.get(0.5D, 1.0D);
        }

        @Override
        public int getColor(Biome biome, double x, double z) {
            return biome.getGrassColor(x, z);
        }

    }

    // Water color multiplier
    public static class WaterColorMultHandler extends ColorMultHandler {
        WaterColorMultHandler() {
        }

        @Override
        @OnlyIn(Dist.CLIENT)
        public int getColor(BlockState arg0, BlockAndTintGetter world, BlockPos pos, int arg3) {
            return BiomeColors.getAverageWaterColor(world, pos) | 0xFF000000;
        }

        @Override
        public int getColor(Biome biome, double x, double z) {
            return biome.getWaterColor();
        }
    }

    public static class PineColorMultHandler extends ColorMultHandler {
        @Override
        @OnlyIn(Dist.CLIENT)
        public int getColor(BlockState arg0, BlockAndTintGetter world, BlockPos pos, int arg3) {
            return FoliageColor.getEvergreenColor();
        }

        @Override
        public int getColor(Biome biome, double x, double z) {
            return FoliageColor.getEvergreenColor();
        }
    }

    public static class BirchColorMultHandler extends ColorMultHandler {
        @Override
        @OnlyIn(Dist.CLIENT)
        public int getColor(BlockState arg0, BlockAndTintGetter world, BlockPos pos, int arg3) {
            return FoliageColor.getBirchColor();
        }

        @Override
        public int getColor(Biome biome, double x, double z) {
            return FoliageColor.getBirchColor();
        }
    }

    public static class BasicColorMultHandler extends ColorMultHandler {
        @Override
        @OnlyIn(Dist.CLIENT)
        public int getColor(BlockState arg0, BlockAndTintGetter world, BlockPos pos, int arg3) {
            return FoliageColor.getDefaultColor();
        }

        @Override
        public int getColor(Biome biome, double x, double z) {
            return FoliageColor.getDefaultColor();
        }
    }


    public WesterosBlockStateRecord getStackElementByIndex(int idx) {
        if ((stack != null) && (stack.size() > 0)) {
            if (idx >= stack.size()) {
                idx = stack.size() - 1;
            }
            return stack.get(idx);
        }
        return null;
    }

    // Custom color multiplier
    public static class CustomColorMultHandler extends ColorMultHandler implements ColorResolver {
        private final List<int[]> colorBuffers;
        private final List<String> rnames;
        private boolean brokenCustomRenderer = false;

        CustomColorMultHandler(String rname, String blockName) {
            this(Collections.singletonList(rname), blockName);
        }

        CustomColorMultHandler(List<String> rnames, String blockName) {
            super();
            this.colorBuffers = new ArrayList<int[]>();
            this.rnames = rnames;
            for (String rname : rnames) {
                colorBuffers.add(new int[65536]);
            }
        }

        public int calculateColor(LevelReader rdr, BlockPos pos, int txtindx) {
            int red = 0;
            int green = 0;
            int blue = 0;

            for (int xx = -1; xx <= 1; ++xx) {
                for (int zz = -1; zz <= 1; ++zz) {
                    BlockPos bp = pos.offset(xx, 0, zz);
                    Biome biome = rdr.getBiome(bp).value();
                    int mult = getColor(biome.getBaseTemperature(), biome.getModifiedClimateSettings().downfall(), txtindx);
                    red += (mult & 0xFF0000) >> 16;
                    green += (mult & 0x00FF00) >> 8;
                    blue += (mult & 0x0000FF);
                }
            }
            return (((red / 9) & 0xFF) << 16) | (((green / 9) & 0xFF) << 8) | ((blue / 9) & 0xFF);
        }

        @Override
        @OnlyIn(Dist.CLIENT)
        public int getColor(BlockState state, BlockAndTintGetter world, BlockPos pos, int txtindx) {
            if ((world != null) && (pos != null)) {
                LevelReader rdr = null;

                if (world instanceof RenderChunkRegion) {
                    // TODO FIXME idk if using Minecraft.getInstance is correct
//					rdr = ((RenderChunkRegion)world).level;
                    rdr = Minecraft.getInstance().level;
                } else if (world instanceof LevelReader) {
                    rdr = (LevelReader) world;
                }
                if (rdr != null) {
                    return calculateColor(rdr, pos, txtindx);
                }
                // Workaround to attempt to support custom renderers such as Optifine or Embeddium
                else {
                    if (!brokenCustomRenderer) {
                        // First try to access non-thread-safe level reader from minecraft client instance...
                        try {
                            rdr = Minecraft.getInstance().level;
                            return calculateColor(rdr, pos, txtindx);
                        } catch (Exception x) {
                            // If fails, try to use color resolver method...
                            try {
                                return world.getBlockTint(pos, this);
                            } catch (Exception y) {
                                // If both fail, biome colors will be broken, but should not explode.
                                brokenCustomRenderer = true;
                            }
                        }
                    }
                }
            }
            return getColor(null, 0.5D, 1.0D, txtindx);
        }

        private int getColor(double tmp, double hum, int txtindx) {
            tmp = Mth.clamp(tmp, 0.0F, 1.0F);
            hum = Mth.clamp(hum, 0.0F, 1.0F);
            hum *= tmp;
            int i = (int) ((1.0D - tmp) * 255.0D);
            int j = (int) ((1.0D - hum) * 255.0D);
            return colorBuffers.get(txtindx)[j << 8 | i];
        }

        @Override
        public int getColor(Biome biome, double x, double z) {
            return getColor(biome, x, z, 0);
        }

        public int getColor(Biome biome, double x, double z, int txtindx) {
            float hum = 1.0F;
            float tmp = 0.5F;
            if (biome != null) {
                hum = biome.getModifiedClimateSettings().downfall();
                tmp = biome.getBaseTemperature();
            }
            tmp = Mth.clamp(tmp, 0.0F, 1.0F);
            hum = Mth.clamp(hum, 0.0F, 1.0F);
            hum *= tmp;
            int i = (int) ((1.0D - tmp) * 255.0D);
            int j = (int) ((1.0D - hum) * 255.0D);
            return colorBuffers.get(txtindx)[j << 8 | i];
        }

        @OnlyIn(Dist.CLIENT)
        public void loadColorMaps(ResourceManager resMgr) {
            int txtindx = 0;
            for (String resName : rnames) {
                if (resName.indexOf(':') < 0)
                    resName = WesterosBlocks.MOD_ID + ":" + resName;
                if (resName.endsWith(".png") == false)
                    resName += ".png";
                try {

					colorBuffers.set(txtindx, LegacyStuffWrapper.getPixels(resMgr, ResourceLocation.parse(resName)));
                    WesterosBlocks.log.debug(String.format("Loaded color resource '%s'", resName));
                } catch (Exception e) {
                    WesterosBlocks.log.error(String.format("Invalid color resource '%s'", resName), e);
                    Arrays.fill(colorBuffers.get(txtindx), 0xFFFFFF);
                }
                txtindx++;
            }
        }
    }

    public String getType() {
        return this.type;
    }

    private transient Map<String, String> parsedType;

    public Map<String, String> getMappedType() {
        if (parsedType == null) {
            parsedType = new HashMap<String, String>();
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

    private transient boolean hasCollisionBoxes = false;

    private static final Map<String, AuxMaterial> materialTable = new HashMap<String, AuxMaterial>();
    private static final Map<String, SoundType> stepSoundTable = new HashMap<String, SoundType>();
    private static final Map<String, CreativeModeTab> tabTable = new HashMap<>();
    private static final Map<String, WesterosBlockFactory> typeTable = new HashMap<String, WesterosBlockFactory>();
    private static final Map<String, ColorMultHandler> colorMultTable = new HashMap<String, ColorMultHandler>();
    private static final Map<String, ParticleType<?>> particles = new HashMap<String, ParticleType<?>>();

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
        for (WesterosBlockStateRecord rec : this.states) {
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
            for (WesterosBlockStateRecord se : this.stack) {
                se.doStateRecordInit();
            }
        }
        if (this.states.size() > 1) {
            ArrayList<String> ids = new ArrayList<String>();
            for (int i = 0; i < states.size(); i++) {
                WesterosBlockStateRecord rec = states.get(i);
                if (rec.stateID == null) rec.stateID = String.format("state%d", i);
                ids.add(rec.stateID);
            }
            stateProp = new StateProperty(ids);
        }

        didInit = true;
    }

    private static Map<String, long[]> perfCounts = new HashMap<String, long[]>();

    public Block createBlock(RegisterEvent.RegisterHelper<Block> helper) {
        try {
            doInit(); // Prime the block model
        } catch (Exception x) {
            WesterosBlocks.log.error("Exception during doInit: blockName=" + this.blockName);
            throw x;
        }
        long[] pc = perfCounts.get(blockType);
        if (pc == null) {
            pc = new long[2];
            perfCounts.put(blockType, pc);
        }
        WesterosBlockFactory bf = typeTable.get(blockType);
        if (bf == null) {
            WesterosBlocks.log.error(String.format("Invalid blockType '%s' in block '%s'", blockType, blockName));
            return null;
        }
        long start = System.currentTimeMillis();
        Block blk = bf.buildBlockClass(this, helper);
        long end = System.currentTimeMillis();
        pc[0]++;
        pc[1] += (end - start);

        return blk;
    }

    public static void dumpBlockPerf() {
        WesterosBlocks.log.info("Block create perf");
        for (String blktype : perfCounts.keySet()) {
            long[] pc = perfCounts.get(blktype);
            WesterosBlocks.log.info(String.format("type %s: %d count, %d total ms, %d ms/call", blktype, pc[0], pc[1], pc[1] / pc[0]));
        }
    }

    // TODO: we can use render_type in the model file itself instead
    public Block registerRenderType(Block block, boolean isSolid, boolean isTransparent) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            if (this.alphaRender) {
                ItemBlockRenderTypes.setRenderLayer(block, RenderType.translucent());
            } else if (!isSolid) {
                ItemBlockRenderTypes.setRenderLayer(block, RenderType.cutout());
            } else if (isTransparent) {
                ItemBlockRenderTypes.setRenderLayer(block, RenderType.cutoutMipped());
            }
        }
        return block;
    }

    public BlockBehaviour.Properties makeProperties() {
        return makeAndCopyProperties(null);
    }

    private static boolean never(BlockState p_50806_, BlockGetter p_50807_, BlockPos p_50808_) {
        return false;
    }

    public BlockBehaviour.Properties makeAndCopyProperties(Block blk) {
        BlockBehaviour.Properties props;
        if (blk != null) {
            props = BlockBehaviour.Properties.ofFullCopy(blk);
        } else {
            AuxMaterial mat = getMaterial();
            props = BlockBehaviour.Properties.of(); // TODO - material color?
        }
        if (hardness >= 0.0F) {
            if (resistance >= 0.0)
                props = props.strength(hardness, resistance);
            else
                props = props.strength(hardness);
        }
        if (stepSound != null) {
            props = props.sound(getSoundType());
        }
        // See if any nonzero light levels
        if (this.stateProp != null) {
            Map<String, Integer> llmap = null;
            for (int i = 0; i < this.states.size(); i++) {
                WesterosBlockStateRecord sr = this.states.get(i);
                if (sr.lightValue > 0.0F) {
                    if (llmap == null) llmap = new HashMap<String, Integer>();
                    llmap.put(sr.stateID, (int) (16.0 * sr.lightValue));
                }
                if (llmap != null) {
                    final Map<String, Integer> final_llmap = llmap;
                    props = props.lightLevel((state) ->
                            final_llmap.getOrDefault(state.getValue(this.stateProp), 0));
                }
            }
        } else {
            float ll = this.states.get(0).lightValue;
            if (ll > 0.0F) {
                props = props.lightLevel((state) -> (int) (16.0 * ll));
            }
        }
        if (lightValue > 0.0F) {
            props = props.lightLevel((state) -> (int) (16.0 * lightValue));
        }
        if ((!ambientOcclusion) || (nonOpaque)) { // If no ambient occlusion
            props = props.noOcclusion();
            props = props.isViewBlocking(WesterosBlockDef::never);
        }
        return props;
    }

    /**
     * Returns this WesterosBlockDef's default Material
     *
     * @return this WesterosBlockDef's default Material
     */
    public AuxMaterial getMaterial() {
        AuxMaterial m = materialTable.get(material);
        if (m == null) {
            WesterosBlocks.log.warn(String.format("Invalid material '%s' in block '%s'", material, blockName));
            return AuxMaterial.STONE;
        }
        return m;
    }

    /**
     * Returns this WesterosBlockDef's default SoundType
     *
     * @return this WesterosBlockDef's default SoundType
     */
    public SoundType getSoundType() {
        SoundType ss = stepSoundTable.get(stepSound);
        if (ss == null) {
            WesterosBlocks.log.warn(String.format("Invalid step sound '%s' in block '%s'", stepSound, blockName));
            return SoundType.STONE;
        }
        return ss;
    }

    public CreativeModeTab getCreativeTab() {
        CreativeModeTab ct = tabTable.get(creativeTab);
        if (ct == null) {
            WesterosBlocks.log.warn(String.format("Invalid tab name '%s' in block '%s'", creativeTab, blockName));
            // TODO FIXME
//			ct = WesterosBlocksCreativeTab.tabWesterosMisc;
        }
        return ct;
    }

    public boolean hasCollisionBoxes() {
        return hasCollisionBoxes;
    }

    public String getBlockName() {
        return this.blockName;
    }

    public static void addCreativeTab(String name, CreativeModeTab tab) {
        tabTable.put(name, tab);
    }

    public static boolean sanityCheck(WesterosBlockDef[] defs) {
        HashSet<String> names = new HashSet<String>();
        // Make sure block IDs and names are unique
        for (WesterosBlockDef def : defs) {
            if (def == null)
                continue;
            if (def.blockName == null) {
                WesterosBlocks.log.error("Block definition is missing blockName");
                return false;
            }
            if (names.add(def.blockName) == false) { // If alreay defined
                WesterosBlocks.log.error(String.format("Block '%s' - blockName duplicated", def.blockName));
                return false;
            }
        }
        WesterosBlocks.log.info("WesterosBlocks.json passed sanity check");
        return true;
    }

    // Return true if defs strictly subsumes validation (i.e., it preserves every block name, as well as
    // every state-related attribute for each block); otherwise false.
    public static boolean compareBlockDefs(WesterosBlockDef[] defs, WesterosBlockDef[] validation) {
        Map<String, WesterosBlockDef> defmap = defsToMap(defs);
        boolean error = false;
        for (WesterosBlockDef val : validation) {
            if (!defmap.containsKey(val.blockName)) {
                WesterosBlocks.log.warn(String.format("validation: blockName '%s' missing", val.blockName));
                error = true;
                continue;
            }

            WesterosBlockDef def = defmap.get(val.blockName);
            if (!def.blockType.equals(val.blockType)) {
                // allow for solid subtypes to be recast
                if (!def.blockType.matches("solid|sand|soulsand") && !def.blockType.matches("solid|sand|soulsand")) {
                    WesterosBlocks.log.warn(String.format("validation: blockName '%s' has different blockType attribute", val.blockName));
                    error = true;
                    continue;
                }
            }

            String[] valTypeAttrs = val.type.split(",");
            for (String typeAttr : valTypeAttrs) {
                if (!def.type.contains(typeAttr)) {
                    WesterosBlocks.log.warn(String.format("validation: blockName '%s' is missing type attribute '%s'", val.blockName, typeAttr));
                    error = true;
                    continue;
                }
            }

            boolean substateError = false;
            if (val.stack != null) {
                if (def.stack == null || def.stack.size() != val.stack.size())
                    substateError = true;
                else {
                    for (int i = 0; i < val.stack.size(); i++) {
                        if (!def.stack.get(i).equals(val.stack.get(i)))
                            substateError = true;
                    }
                }
            }
            if (val.states != null) {
                if (def.states == null || def.states.size() != val.states.size())
                    substateError = true;
                else {
                    for (int i = 0; i < val.states.size(); i++) {
                        if (!def.states.get(i).equals(val.states.get(i)))
                            substateError = true;
                    }
                }
            }
            if (substateError) {
                WesterosBlocks.log.warn(String.format("validation: blockName '%s' has different stack or state lists", val.blockName));
                error = true;
                continue;
            }
        }
        return !error;
    }

    public static Map<String, WesterosBlockDef> defsToMap(WesterosBlockDef[] defs) {
        Map<String, WesterosBlockDef> map = new HashMap<String, WesterosBlockDef>();
        for (WesterosBlockDef def : defs) {
            map.put(def.blockName, def);
        }
        return map;
    }

    public static void initialize() {
        materialTable.put("air", AuxMaterial.AIR);
        materialTable.put("grass", AuxMaterial.GRASS);
        materialTable.put("ground", AuxMaterial.DIRT);
        materialTable.put("wood", AuxMaterial.WOOD);
        materialTable.put("rock", AuxMaterial.STONE);
        materialTable.put("iron", AuxMaterial.METAL);
        materialTable.put("anvil", AuxMaterial.METAL);
        materialTable.put("water", AuxMaterial.WATER);
        materialTable.put("lava", AuxMaterial.LAVA);
        materialTable.put("leaves", AuxMaterial.LEAVES);
        materialTable.put("plants", AuxMaterial.PLANT);
        materialTable.put("vine", AuxMaterial.PLANT);
        materialTable.put("sponge", AuxMaterial.SPONGE);
        materialTable.put("cloth", AuxMaterial.CLOTH_DECORATION);
        materialTable.put("fire", AuxMaterial.FIRE);
        materialTable.put("sand", AuxMaterial.SAND);
        materialTable.put("glass", AuxMaterial.GLASS);
        materialTable.put("tnt", AuxMaterial.EXPLOSIVE);
        materialTable.put("coral", AuxMaterial.STONE);
        materialTable.put("ice", AuxMaterial.ICE);
        materialTable.put("snow", AuxMaterial.SNOW);
        materialTable.put("craftedSnow", AuxMaterial.SNOW);
        materialTable.put("cactus", AuxMaterial.CACTUS);
        materialTable.put("clay", AuxMaterial.CLAY);
        materialTable.put("portal", AuxMaterial.PORTAL);
        materialTable.put("cake", AuxMaterial.CAKE);
        materialTable.put("web", AuxMaterial.WEB);
        materialTable.put("piston", AuxMaterial.PISTON);
        materialTable.put("decoration", AuxMaterial.DECORATION);

        stepSoundTable.put("powder", SoundType.SAND);
        stepSoundTable.put("wood", SoundType.WOOD);
        stepSoundTable.put("gravel", SoundType.GRAVEL);
        stepSoundTable.put("grass", SoundType.GRASS);
        stepSoundTable.put("stone", SoundType.STONE);
        stepSoundTable.put("metal", SoundType.METAL);
        stepSoundTable.put("glass", SoundType.GLASS);
        stepSoundTable.put("cloth", SoundType.WOOL);
        stepSoundTable.put("sand", SoundType.SAND);
        stepSoundTable.put("snow", SoundType.SNOW);
        stepSoundTable.put("ladder", SoundType.LADDER);
        stepSoundTable.put("anvil", SoundType.ANVIL);
        stepSoundTable.put("plant", SoundType.CROP);
        stepSoundTable.put("slime", SoundType.FUNGUS);
        // Tab table
        tabTable.put("buildingBlocks", BuiltInRegistries.CREATIVE_MODE_TAB.getOrThrow(CreativeModeTabs.BUILDING_BLOCKS));
        //tabTable.put("decorations", CreativeModeTabs.DECORATIONS);
        tabTable.put("redstone", BuiltInRegistries.CREATIVE_MODE_TAB.getOrThrow(CreativeModeTabs.REDSTONE_BLOCKS));
        //tabTable.put("transportation", CreativeModeTabs.TRANSPORTATION);
        //tabTable.put("misc", CreativeModeTabs.MISC);
        tabTable.put("food", BuiltInRegistries.CREATIVE_MODE_TAB.getOrThrow(CreativeModeTabs.FOOD_AND_DRINKS));
        tabTable.put("tools", BuiltInRegistries.CREATIVE_MODE_TAB.getOrThrow(CreativeModeTabs.TOOLS_AND_UTILITIES));
        tabTable.put("combat", BuiltInRegistries.CREATIVE_MODE_TAB.getOrThrow(CreativeModeTabs.COMBAT));
        //tabTable.put("brewing", CreativeModeTabs.BREWING);
        tabTable.put("materials", BuiltInRegistries.CREATIVE_MODE_TAB.getOrThrow(CreativeModeTabs.INGREDIENTS));

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

        // Standard color multipliers
        colorMultTable.put("#FFFFFF", new FixedColorMultHandler(0xFFFFFF));
        colorMultTable.put("water", new WaterColorMultHandler());
        colorMultTable.put("foliage", new FoliageColorMultHandler());
        colorMultTable.put("grass", new GrassColorMultHandler());
        colorMultTable.put("pine", new PineColorMultHandler());
        colorMultTable.put("birch", new BirchColorMultHandler());
        colorMultTable.put("basic", new BasicColorMultHandler());
        colorMultTable.put("lily", new FixedColorMultHandler(2129968));

        // Valid particle values
        particles.put("hugeexplosion", ParticleTypes.EXPLOSION);
        particles.put("largeexplode", ParticleTypes.EXPLOSION);
        particles.put("fireworksSpark", ParticleTypes.FIREWORK);
        particles.put("bubble", ParticleTypes.BUBBLE);
        particles.put("suspended", ParticleTypes.UNDERWATER);
        particles.put("depthsuspend", ParticleTypes.UNDERWATER);
        //particles.put("townaura", ParticleTypes.BARRIER);
        particles.put("crit", ParticleTypes.CRIT);
        particles.put("magicCrit", ParticleTypes.CRIT);
        particles.put("smoke", ParticleTypes.SMOKE);
        particles.put("mobSpell", ParticleTypes.ENCHANT);
        particles.put("mobSpellAmbient", ParticleTypes.ENCHANT);
        particles.put("spell", ParticleTypes.ENCHANT);
        particles.put("instantSpell", ParticleTypes.INSTANT_EFFECT);
        particles.put("witchMagic", ParticleTypes.WITCH);
        particles.put("note", ParticleTypes.NOTE);
        particles.put("portal", ParticleTypes.PORTAL);
        particles.put("enchantmenttable", ParticleTypes.POOF);
        particles.put("explode", ParticleTypes.EXPLOSION);
        particles.put("flame", ParticleTypes.FLAME);
        particles.put("lava", ParticleTypes.LAVA);
        particles.put("splash", ParticleTypes.SPLASH);
        particles.put("largesmoke", ParticleTypes.LARGE_SMOKE);
        particles.put("cloud", ParticleTypes.CLOUD);
        particles.put("snowballpoof", ParticleTypes.POOF);
        particles.put("dripWater", ParticleTypes.DRIPPING_WATER);
        particles.put("dripLava", ParticleTypes.DRIPPING_LAVA);
        particles.put("snowshovel", ParticleTypes.ITEM_SNOWBALL);
        particles.put("slime", ParticleTypes.ITEM_SLIME);
        particles.put("heart", ParticleTypes.HEART);
        particles.put("angryVillager", ParticleTypes.ANGRY_VILLAGER);
        particles.put("happyVillager", ParticleTypes.HAPPY_VILLAGER);
    }

    // Force reload of color handlers
    public static void reloadColorHandler(ResourceManager pResourceManager) {
        Set<String> hndids = new HashSet<String>(colorMultTable.keySet());
        for (String hndid : hndids) {
            ColorMultHandler prev = colorMultTable.get(hndid);
            // Only reload those from resources
            if (prev instanceof CustomColorMultHandler) {
                ((CustomColorMultHandler) prev).loadColorMaps(pResourceManager);
            }
        }
    }

    // Get color muliplier
    public static ColorMultHandler getColorHandler(String hnd, String blockName) {
        String hndid = hnd.toLowerCase();
        ColorMultHandler cmh = colorMultTable.get(hndid);
        if (cmh == null) {
            // See if color code
            if ((hndid.length() == 7) && (hndid.charAt(0) == '#')) {
                try {
                    cmh = new FixedColorMultHandler(Integer.parseInt(hndid.substring(1), 16));
                    colorMultTable.put(hndid, cmh);
                } catch (NumberFormatException nfx) {
                }
            }
            // See if resource
            else {
                int idx = hnd.indexOf(':');
                if (idx < 0) {
                    hnd = WesterosBlocks.MOD_ID + ":" + hnd;
                    hndid = hnd.toLowerCase();
                }
                cmh = colorMultTable.get(hndid);
                if (cmh == null) {
                    cmh = new CustomColorMultHandler(hnd, blockName);
                    colorMultTable.put(hndid, cmh);
                }
            }
        }

        return cmh;
    }

    public static ColorMultHandler getColorHandler(List<String> hnd, String blockName) {
        String hndid = String.join("_", hnd).toLowerCase();
        ColorMultHandler cmh = colorMultTable.get(hndid);
        if (cmh == null) {
            for (int i = 0; i < hnd.size(); i++) {
                int idx = hnd.get(i).indexOf(':');
                if (idx < 0) {
                    hnd.set(i, WesterosBlocks.MOD_ID + ":" + hnd.get(i));
                }
            }
            hndid = String.join("_", hnd).toLowerCase();
            cmh = colorMultTable.get(hndid);
            if (cmh == null) {
                cmh = new CustomColorMultHandler(hnd, blockName);
                colorMultTable.put(hndid, cmh);
            }
        }

        return cmh;
    }

    public String getBlockColorMapResource() {
        String res = null;
        String blockColor = colorMult;
        if (blockColor == null && colorMults != null && colorMults.size() >= 1) {
            blockColor = colorMults.get(0);
        }
        if ((blockColor != null) && (blockColor.startsWith("#") == false)) {
            String tok[] = blockColor.split(":");
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

    private static class BlockEntityRec {
        ArrayList<Block> blocks = new ArrayList<Block>();
        BlockEntityType<?> regobj;
    }

    private static HashMap<String, BlockEntityRec> te_rec = new HashMap<String, BlockEntityRec>();

    public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, WesterosBlocks.MOD_ID);

    public void registerBlockItem(String blockName, Block block) {
        WesterosBlocks.ITEMS.register(blockName, () -> new BlockItem(block, new Item.Properties()));
    }

    public static <T extends BlockEntity> void registerBlockEntity(String name, BlockEntityType.BlockEntitySupplier<T> BlockEntitySupplier, Block blk) {
        BlockEntityRec rec = (BlockEntityRec) te_rec.get(name);
        if (rec == null) {
            rec = new BlockEntityRec();
            te_rec.put(name, rec);
            final BlockEntityRec frec = rec;
            // TODO FIXME
//			rec.regobj = TILE_ENTITY_TYPES.register(name, () -> BlockEntityType.Builder.of(BlockEntitySupplier, frec.blocks.toArray(new Block[frec.blocks.size()])).build(null));
        }
        rec.blocks.add(blk);
    }

    public static BlockEntityType<?> getBlockEntityType(String name) {
        BlockEntityRec rec = te_rec.get(name);
        if (rec != null)
            return rec.regobj;
        return null;
    }

    public static ColorMultHandler getStateColorHandler(WesterosBlockStateRecord rec, String blockName) {
        if (rec.colorMults != null) {
            return getColorHandler(rec.colorMults, blockName);
        } else {
            return getColorHandler(rec.colorMult, blockName);
        }
    }

    // Handle registration of tint handling and other client rendering
    @OnlyIn(Dist.CLIENT)
    public void registerBlockColorHandler(Block blk, RegisterColorHandlersEvent.Block event) {
        if (this.isTinted()) {
            if (this.stateProp != null) {
                final Map<String, ColorMultHandler> cmmap = new HashMap<String, ColorMultHandler>();
                for (WesterosBlockStateRecord rec : this.states) {
                    ColorMultHandler handler = getStateColorHandler(rec, this.blockName);
                    cmmap.put(rec.stateID, handler);
                }
                event.register((BlockState state, BlockAndTintGetter world, BlockPos pos, int txtindx) ->
                        cmmap.get(state.getValue(this.stateProp)).getColor(state, world, pos, txtindx), blk);
                final ColorMultHandler itemHandler = cmmap.get(this.states.get(0).stateID);
            } else {
                ColorMultHandler handler = getStateColorHandler(this, this.blockName);

                event.register((BlockState state, BlockAndTintGetter world, BlockPos pos, int txtindx) -> handler
                        .getColor(state, world, pos, txtindx), blk);
            }
        }
    }

    // Handle registration of tint handling and other client rendering
    @OnlyIn(Dist.CLIENT)
    public static void registerVanillaBlockColorHandler(String blockName, Block blk, String colorMult, RegisterColorHandlersEvent.Block event) {
        ColorMultHandler handler = getColorHandler(colorMult, blockName);
        event.register((BlockState state, BlockAndTintGetter world, BlockPos pos, int txtindx) -> handler
                .getColor(state, world, pos, txtindx), blk);
        // If water shader, override global one too
        if (blockName.equals("minecraft:water") && (handler instanceof CustomColorMultHandler)) {
            final CustomColorMultHandler cchandler = (CustomColorMultHandler) handler;    // crappy java lambda limitation workaround
            // TODO FIXME
//			BiomeColors.WATER_COLOR_RESOLVER = (Biome b, double tmp, double hum) -> cchandler.getColor(b, tmp, hum);
        }
    }


    // Handle registration of tint handling and other client rendering
    @OnlyIn(Dist.CLIENT)
    public void registerItemColorHandler(Block blk, RegisterColorHandlersEvent.Item event) {
        if (this.isTinted()) {
            if (this.stateProp != null) {
                final Map<String, ColorMultHandler> cmmap = new HashMap<String, ColorMultHandler>();
                for (WesterosBlockStateRecord rec : this.states) {
                    ColorMultHandler handler = getStateColorHandler(rec, this.blockName);
                    cmmap.put(rec.stateID, handler);
                }
                final ColorMultHandler itemHandler = cmmap.get(this.states.get(0).stateID);
                event.register((ItemStack stack, int tintIndex) -> itemHandler.getItemColor(stack, tintIndex), blk);
            } else {
                ColorMultHandler handler = getStateColorHandler(this, this.blockName);
                // TODO FIXME
//				itemColors.register((ItemStack stack, int tintIndex) -> handler.getItemColor(stack, tintIndex), blk);
            }
        }
    }


    // Handle registration of tint handling and other client rendering
    @OnlyIn(Dist.CLIENT)
    public static void registerVanillaItemColorHandler(String blockName, Block blk, String colorMult, RegisterColorHandlersEvent.Item event) {
        ColorMultHandler handler = getColorHandler(colorMult, blockName);
        event.register((ItemStack stack, int tintIndex) -> handler.getItemColor(stack, tintIndex), blk);
    }

    public void registerSoundEvents(RegisterEvent.RegisterHelper<SoundEvent> helper) {
        if (this.soundList != null) {
            for (String snd : this.soundList) {
                WesterosBlocks.registerSound(snd, helper);
            }
        }
    }



    // Get customized collision box for default solid block
    public VoxelShape makeCollisionBoxShape() {
        if (collisionBoxes == null) {
            return Shapes.block();    // Default to solid block
        }
        VoxelShape s = Shapes.empty();
        for (BoundingBox b : collisionBoxes) {
            s = Shapes.or(s, b.getAABB());
        }
        return s;
    }

    public String getLegacyBlockName() {
        if (legacyBlockID == null) return null;
        String v = legacyBlockID;
        int sqoff = v.indexOf('[');
        if (sqoff >= 0) {
            v = v.substring(0, sqoff);
        }
        String[] tok = v.split(":");
        if ((tok.length > 2) || (tok[0].equals("minecraft"))) {
            return tok[0] + ":" + tok[1];
        }
        return "westerosblocks:" + tok[0];
    }

    public Map<String, String> getLegacyBlockMap() {
        if (legacyBlockID == null) return null;
        Map<String, String> mval = new HashMap<String, String>();
        if (legacyBlockID.indexOf('[') >= 0) {
            String p = legacyBlockID;
            int st = p.indexOf('[');
            int en = p.indexOf(']');
            p = p.substring(st + 1, en);
            String[] ptoks = p.split(",");    // Split at commas, if any
            for (String pair : ptoks) {
                String[] av = pair.split("=");
                if (av.length > 1) {
                    mval.put(av[0], av[1]);
                } else {
                    mval.put("variant", av[0]);
                }
            }
            return mval;
        }
        String[] tok = legacyBlockID.split(":");
        if ((tok.length == 2) && tok[0].equals("minecraft")) return null;
        if (tok.length >= 2) {
            String v = tok[tok.length - 1];
            if (v.indexOf('=') < 0) {
                if (!v.equals("default")) {
                    mval.put("variant", v);
                }
            } else {
                String[] stok = v.split(",");
                for (String sv : stok) {
                    String[] vtok = sv.split("=");
                    if (vtok.length > 1)
                        mval.put(vtok[0], vtok[1]);
                }
            }
            return mval;
        }
        return null;
    }

    public static class StateProperty extends Property<String> {
        public final ImmutableList<String> values;
        public final ImmutableMap<String, String> valMap;
        public final String defValue;

        public StateProperty(List<String> stateIDs) {
            super("state", String.class);
            Map<String, String> map = Maps.newHashMap();
            List<String> vals = new ArrayList<String>();
            int len = stateIDs.size();
            for (int i = 0; i < len; i++) {
                String s = stateIDs.get(i);
                map.put(s, s);
                vals.add(s);
            }
            this.values = ImmutableList.copyOf(vals);
            this.valMap = ImmutableMap.copyOf(map);
            this.defValue = stateIDs.get(0);
        }

        @Override
        public Collection<String> getPossibleValues() {
            return this.values;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            } else if (obj instanceof StateProperty && super.equals(obj)) {
                StateProperty stateproperty = (StateProperty) obj;
                return this.values.equals(stateproperty.values);
            } else {
                return false;
            }
        }

        @Override
        public int generateHashCode() {
            return 31 * super.generateHashCode() + this.values.hashCode();
        }

        @Override
        public Optional<String> getValue(String key) {
            String val = this.valMap.get(key);
            return (val != null) ? Optional.of(val) : Optional.empty();
        }

        @Override
        public String getName(String val) {
            return val;
        }

        public int getIndex(String val) {
            int v = this.values.indexOf(val);
            return (v >= 0) ? v : 0;
        }
    }

    // Build 'state' property for the 
    public StateProperty buildStateProperty() {
        return stateProp;
    }

    // Get default state ID
    public String getDefaultStateID() {
        if (this.states == null) return null;
        return this.states.get(0).stateID;
    }
}
