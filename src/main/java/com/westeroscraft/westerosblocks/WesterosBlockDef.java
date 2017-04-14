package com.westeroscraft.westerosblocks;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.dynmap.modsupport.BlockSide;
import org.dynmap.modsupport.BlockTextureRecord;
import org.dynmap.modsupport.ModTextureDefinition;
import org.dynmap.modsupport.TextureModifier;
import org.dynmap.modsupport.TransparencyMode;

import com.westeroscraft.westerosblocks.asm.ClassTransformer;
import com.westeroscraft.westerosblocks.blocks.WCBeaconBlock;
import com.westeroscraft.westerosblocks.blocks.WCBedBlock;
import com.westeroscraft.westerosblocks.blocks.WCCakeBlock;
import com.westeroscraft.westerosblocks.blocks.WCCropBlock;
import com.westeroscraft.westerosblocks.blocks.WCCuboidBlock;
import com.westeroscraft.westerosblocks.blocks.WCCuboidNEBlock;
import com.westeroscraft.westerosblocks.blocks.WCCuboidNEStackBlock;
import com.westeroscraft.westerosblocks.blocks.WCCuboidNSEWBlock;
import com.westeroscraft.westerosblocks.blocks.WCCuboidNSEWStackBlock;
import com.westeroscraft.westerosblocks.blocks.WCCuboidNSEWUDBlock;
import com.westeroscraft.westerosblocks.blocks.WCDoorBlock;
import com.westeroscraft.westerosblocks.blocks.WCFenceBlock;
import com.westeroscraft.westerosblocks.blocks.WCFireBlock;
import com.westeroscraft.westerosblocks.blocks.WCFurnaceBlock;
import com.westeroscraft.westerosblocks.blocks.WCHalfDoorBlock;
import com.westeroscraft.westerosblocks.blocks.WCLadderBlock;
import com.westeroscraft.westerosblocks.blocks.WCLayerBlock;
import com.westeroscraft.westerosblocks.blocks.WCLeavesBlock;
import com.westeroscraft.westerosblocks.blocks.WCLogBlock;
import com.westeroscraft.westerosblocks.blocks.WCPaneBlock;
import com.westeroscraft.westerosblocks.blocks.WCPlantBlock;
import com.westeroscraft.westerosblocks.blocks.WCRailBlock;
import com.westeroscraft.westerosblocks.blocks.WCSandBlock;
import com.westeroscraft.westerosblocks.blocks.WCSlabBlock;
import com.westeroscraft.westerosblocks.blocks.WCSolidBlock;
import com.westeroscraft.westerosblocks.blocks.WCSoulSandBlock;
import com.westeroscraft.westerosblocks.blocks.WCSoundBlock;
import com.westeroscraft.westerosblocks.blocks.WCStairBlock;
import com.westeroscraft.westerosblocks.blocks.WCTorchBlock;
import com.westeroscraft.westerosblocks.blocks.WCTrapDoorBlock;
import com.westeroscraft.westerosblocks.blocks.WCWallBlock;
import com.westeroscraft.westerosblocks.blocks.WCWebBlock;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.fml.common.registry.GameData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

//
// Template for block configuration data (populated using GSON)
//
public class WesterosBlockDef {
    private static final float DEF_FLOAT = -999.0F;
    public static final int DEF_INT = -999;
    
    public String blockName;                // Locally unique block name
    public String blockType = "solid";      // Block type ('solid', 'liquid', 'plant', 'log', 'stairs', etc)
    public int blockID = DEF_INT;           // Block ID number (default)
    public int[] blockIDs = null;           // Block ID numbers (default) - for definitions with more than one block
    public float hardness = DEF_FLOAT;      // Block hardness
    public String stepSound = null;         // Step sound (powder, wood, gravel, grass, stone, metal, glass, cloth, sand, snow, ladder, anvil)
    public String material = null;          // Generic material (ai, grass, ground, wood, rock, iron, anvil, water, lava, leaves, plants, vine, sponge, etc)
    public float resistance = DEF_FLOAT;    // Explosion resistance
    public int lightOpacity = DEF_INT;      // Light opacity
    public List<HarvestLevel> harvestLevel = null;  // List of harvest levels
    public int harvestItemID = DEF_INT;     // Harvest item ID
    public int fireSpreadSpeed = 0;         // Fire spread speed
    public int flamability = 0;             // Flamability
    public String creativeTab = null;       // Creative tab for items
    public float lightValue = 0.0F;         // Emitted light level (0.0-1.0)
    public List<Subblock> subBlocks = null; // Subblocks
    public String modelBlockName = null;    // Name of solid block modelled from (used by 'stairs' type) - can be number of block ID
    public int modelBlockMeta = DEF_INT;    // Metadata of model block to use 
    public BoundingBox boundingBox = null;  // Bounding box
    public String colorMult = "#FFFFFF";    // Color multiplier ("#rrggbb' for fixed value, 'foliage', 'grass', 'water')
    public String type = "";                // Type field (used for plant types or other block type specific values)
    public boolean alphaRender = false;     // If true, do render on pass 2 (for alpha blending)
    public boolean nonOpaque = false;       // If true, does not block visibility of shared faces (solid blocks) and doesn't allow torches 
                                            // ('solid', 'sound', 'sand', 'soulsand' blocks)
    
    private int blockIDCount = 1;           // NUmber of block IDs for definition (set by block type handler)
    
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
    }
    public static class Vector {
        float x, y, z;
        
        private void rotate(int xcnt, int ycnt, int zcnt) {
            double xx, yy, zz;
            xx = x - 0.5F; yy = y - 0.5F; zz = z - 0.5F; // Shoft to center of block
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
            x = (float)xx + 0.5F; y = (float)yy + 0.5F; z = (float)zz + 0.5F; // Shoft back to corner
            // Clip value
            if (x > 1.0F) x = 1.0F;
            if (y > 1.0F) y = 1.0F;
            if (z > 1.0F) z = 1.0F;
            if (x < 0.0F) x = 0.0F;
            if (y < 0.0F) y = 0.0F;
            if (z < 0.0F) z = 0.0F;
        }

    }
    public static enum CuboidRotation {
        NONE(0, 0, 0, new int[] { 0, 1, 2, 3, 4, 5 }, new int[] { 0, 0, 0, 0, 0, 0 }),
        ROTY90(0, 90, 0, new int[] { 0, 1, 4, 5, 3, 2 }, new int[] { 270, 90, 0, 0, 0, 0 }),
        ROTY180(0, 180, 0, new int[] { 0, 1, 3, 2, 5, 4 }, new int[] { 180, 180, 0, 0, 0, 0 }), 
        ROTY270(0, 270, 0, new int[] { 0, 1, 5, 4, 2, 3 }, new int[] { 90, 270, 0, 0, 0, 0 }),
        ROTZ90(0, 0, 90, new int[] { 5, 4, 2, 3, 0, 1 }, new int[] { 270, 90, 270, 90, 90, 90 }),
        ROTZ270(0, 0, 270, new int[] { 4, 5, 2, 3, 1, 0 }, new int[] { 90, 270, 90, 270, 270, 270 });
        
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
        
    }
    // Shape for normal cuboid (box)
    public static final String SHAPE_BOX = "box";
    // Shape for crossed squares (plant-style) (texture is index 0 in list)
    public static final String SHAPE_CROSSED = "crossed";
    
    public static class Cuboid extends BoundingBox {
        public int[] sideTextures = null;
        public int[] sideRotations = { 0, 0, 0, 0, 0, 0 };
        public String shape = SHAPE_BOX; // "box" = normal cuboid, "crossed" = plant-style crossed (texture 0)
        
        public Cuboid rotateCuboid(CuboidRotation rot) {
            Cuboid c = new Cuboid();
            Vector v0 = new Vector();
            Vector v1 = new Vector();
            v0.x = xMin; v0.y = yMin; v0.z = zMin;
            v1.x = xMax; v1.y = yMax; v1.z = zMax;
            // Rotate corners
            v0.rotate(rot.xrot, rot.yrot, rot.zrot);
            v1.rotate(rot.xrot, rot.yrot, rot.zrot);
            // Compute net min/max
            c.xMin = Math.min(v0.x,  v1.x);
            c.xMax = Math.max(v0.x,  v1.x);
            c.yMin = Math.min(v0.y,  v1.y);
            c.yMax = Math.max(v0.y,  v1.y);
            c.zMin = Math.min(v0.z,  v1.z);
            c.zMax = Math.max(v0.z,  v1.z);
            if (this.sideTextures != null) {
                c.sideTextures = new int[rot.txtidx.length];
                int cnt = this.sideTextures.length;
                for (int i = 0; i < c.sideTextures.length; i++) {
                    int newidx = rot.txtidx[i];
                    if (newidx < cnt) {
                        c.sideTextures[i] = this.sideTextures[newidx];
                    }
                    else {
                        c.sideTextures[i] = this.sideTextures[cnt-1];
                    }
                }
            }
            else {
                c.sideTextures = rot.txtidx;
            }
            c.sideRotations = rot.txtrot;
            c.shape = this.shape;
            return c;
        }
        public Cuboid() {
        }
        public Cuboid(float x0, float y0, float z0, float x1, float y1, float z1) {
            this(x0, y0, z0, x1, y1, z1, null);
        }
        public Cuboid(float x0, float y0, float z0, float x1, float y1, float z1, int[] sidetextures) {
            this.xMin = x0;
            this.xMax = x1;
            this.yMin = y0;
            this.yMax = y1;
            this.zMin = z0;
            this.zMax = z1;
            this.sideTextures = sidetextures;
        }
    }
    
    public static class Particle {
        public float x = 0.5F, y = 0.5F, z = 0.5F;  // Default position of effect
        public float vx = 0.0F, vy = 0.0F, vz = 0.0F;   // Default velocity of effect
        public float xrand = 0.0F, yrand = 0.0F, zrand = 0.0F;  // Default random position of effect (-rand to +rand)
        public float vxrand = 0.0F, vyrand = 0.0F, vzrand = 0.0F;  // Default random velocity of effect (-rand to +rand)
        public float chance = 1.0F;
        public String particle;
    }
    
    public static class Subblock {
        public int meta;        // Meta value for subblock (base value, if more than one associated with block type)
        public String label;    // Label for item associated with block
        public List<HarvestLevel> harvestLevel = null; // List of harvest levels
        public int harvestItemID = DEF_INT;     // Harvest item ID (-1=use block level)
        public int fireSpreadSpeed = DEF_INT;   // Fire spread speed (-1=use block level)
        public int flamability = DEF_INT;       // Flamability (-1=use block level)
        public float lightValue = DEF_FLOAT;    // Emitted light level (0.0-1.0)
        public int lightOpacity = DEF_INT;      // Light opacity
        public List<String> textures = null;    // List of textures
        public String itemTexture = null;       // Item texture, if any
        public BoundingBox boundingBox = null;  // Bounding box
        public String type = null;              // Block type specific type string (e.g. plant type)
        public int itemTextureIndex = 0;        // Index of texture for item icon
        public String colorMult = null;         // Color multiplier ("#rrggbb' for fixed value, 'foliage', 'grass', 'water')
        public List<Cuboid> cuboids = null;     // List of cuboids composing block (for 'cuboid', and others)
        public List<BoundingBox> collisionBoxes = null;     // For 'solid', used for raytrace (arrow shots)
        public List<String> soundList = null;   // List of custom sound names or sound IDs (for 'sound' blocks)
        public boolean noInventoryItem = false; // If true, don't register inventory item for subblock
        public List<Particle> particles = null; // List of particles to be randomly emitted
        
        public String getTextureByIndex(int idx) {
            if ((textures != null) && (textures.size() > 0)) {
                if (idx >= textures.size()) {
                    idx = textures.size() - 1;
                }
                return textures.get(idx);
            }
            return null;
        }
    }

    // Base color multiplier (fixed)
    public static class ColorMultHandler {
        protected int fixedMult;
        
        ColorMultHandler() {
            fixedMult = 0xFFFFFF;
        }
        ColorMultHandler(int mult) {
            fixedMult = mult;
        }
        public int getBlockColor() {
            return fixedMult;
        }
        public int colorMultiplier(IBlockAccess access, BlockPos pos) {
            return fixedMult;
        }
        protected void setBaseColor() {
        }
        protected void loadRes(String rname, String blkname) {
        }
    }
    // Foliage color multiplier
    public static class FoliageColorMultHandler extends ColorMultHandler {
        FoliageColorMultHandler() {
        }
        @Override
        @SideOnly(Side.CLIENT)
        public int getBlockColor() {
            return ColorizerFoliage.getFoliageColor(0.5, 1.0);
        }
        @Override
        @SideOnly(Side.CLIENT)
        public int colorMultiplier(IBlockAccess access, BlockPos pos) {
            int red = 0;
            int green = 0;
            int blue = 0;

            for (int xx = -1; xx <= 1; ++xx) {
                for (int zz = -1; zz <= 1; ++zz) {
                    BlockPos bp = pos.add(xx, 0, zz);
                    int mult = access.getBiome(bp).getFoliageColorAtPos(bp);
                    red += (mult & 0xFF0000) >> 16;
                    green += (mult & 0x00FF00) >> 8;
                    blue += (mult & 0x0000FF);
                }
            }
            return (((red / 9) & 0xFF) << 16) | (((green / 9) & 0xFF) << 8) | ((blue / 9) & 0xFF);
        }
    }
    // Grass color multiplier
    public static class GrassColorMultHandler extends ColorMultHandler {
        GrassColorMultHandler() {
        }
        @Override
        @SideOnly(Side.CLIENT)
        public int getBlockColor() {
            return ColorizerGrass.getGrassColor(0.5, 1.0);
        }
        @Override
        @SideOnly(Side.CLIENT)
        public int colorMultiplier(IBlockAccess access, BlockPos pos) {
            int red = 0;
            int green = 0;
            int blue = 0;

            for (int xx = -1; xx <= 1; ++xx) {
                for (int zz = -1; zz <= 1; ++zz) {
                    BlockPos bp = pos.add(xx, 0, zz);
                    int mult = access.getBiome(bp).getGrassColorAtPos(bp);
                    red += (mult & 0xFF0000) >> 16;
                    green += (mult & 0x00FF00) >> 8;
                    blue += (mult & 0x0000FF);
                }
            }
            return (((red / 9) & 0xFF) << 16) | (((green / 9) & 0xFF) << 8) | ((blue / 9) & 0xFF);
        }
    }
    // Water color multiplier
    public static class WaterColorMultHandler extends ColorMultHandler {
        WaterColorMultHandler() {
        }
        @Override
        @SideOnly(Side.CLIENT)
        public int colorMultiplier(IBlockAccess access, BlockPos pos) {
            int red = 0;
            int green = 0;
            int blue = 0;

            for (int xx = -1; xx <= 1; ++xx) {
                for (int zz = -1; zz <= 1; ++zz) {
                    BlockPos bp = pos.add(xx, 0, zz);
                    int mult = access.getBiome(bp).getWaterColorMultiplier();
                    red += (mult & 0xFF0000) >> 16;
                    green += (mult & 0x00FF00) >> 8;
                    blue += (mult & 0x0000FF);
                }
            }
            return (((red / 9) & 0xFF) << 16) | (((green / 9) & 0xFF) << 8) | ((blue / 9) & 0xFF);
        }
    }

    public static class PineColorMultHandler extends ColorMultHandler {
        @Override
        @SideOnly(Side.CLIENT)
        public int getBlockColor() {
            return ColorizerFoliage.getFoliageColorPine();
        }
        @Override
        @SideOnly(Side.CLIENT)
        public int colorMultiplier(IBlockAccess access, BlockPos pos) {
            return ColorizerFoliage.getFoliageColorPine();
        }
    }
    
    public static class BirchColorMultHandler extends ColorMultHandler {
        @Override
        @SideOnly(Side.CLIENT)
        public int getBlockColor() {
            return ColorizerFoliage.getFoliageColorBirch();
        }
        @Override
        @SideOnly(Side.CLIENT)
        public int colorMultiplier(IBlockAccess access, BlockPos pos) {
            return ColorizerFoliage.getFoliageColorBirch();
        }
    }

    public static class BasicColorMultHandler extends ColorMultHandler {
        @Override
        @SideOnly(Side.CLIENT)
        public int getBlockColor() {
            return ColorizerFoliage.getFoliageColorBasic();
        }
        @Override
        @SideOnly(Side.CLIENT)
        public int colorMultiplier(IBlockAccess access, BlockPos pos) {
            return ColorizerFoliage.getFoliageColorBasic();
        }
    }

    // Custom color multiplier
    public static class CustomColorMultHandler extends ColorMultHandler {
        private int[] colorBuffer = new int[65536];
        
        CustomColorMultHandler(String rname, String blockName) {
            super();
            
            loadRes(rname, blockName);
        }
        @Override
        @SideOnly(Side.CLIENT)
        public int getBlockColor() {
            return getColor(0.5F, 1.0F);
        }
        @SideOnly(Side.CLIENT)
        @Override
        protected void loadRes(String rname, String blkname) {
            try {
                colorBuffer = TextureUtil.readImageData(Minecraft.getMinecraft().getResourceManager(), new ResourceLocation(rname));
            } catch (Exception e) {
                WesterosBlocks.log.severe(String.format("Invalid color resource '%s' in block '%s'", rname, blkname));
                Arrays.fill(colorBuffer,  0xFFFFFF);
            }
        }

        private int getColor(float tmp, float hum)
        {
            tmp = MathHelper.clamp(tmp, 0.0F, 1.0F);
            hum = MathHelper.clamp(hum, 0.0F, 1.0F);
            hum *= tmp;
            int i = (int)((1.0D - tmp) * 255.0D);
            int j = (int)((1.0D - hum) * 255.0D);
            return colorBuffer[j << 8 | i];
        }
        
        @Override
        @SideOnly(Side.CLIENT)
        public int colorMultiplier(IBlockAccess access, BlockPos pos) {
            int red = 0;
            int green = 0;
            int blue = 0;

            for (int xx = -1; xx <= 1; ++xx) {
                for (int zz = -1; zz <= 1; ++zz) {
                    BlockPos bp = pos.add(xx, 0, zz);
                    Biome biome = access.getBiome(bp);
                    int mult = getColor(biome.getFloatTemperature(bp), biome.getRainfall());
                    red += (mult & 0xFF0000) >> 16;
                    green += (mult & 0x00FF00) >> 8;
                    blue += (mult & 0x0000FF);
                }
            }
            return (((red / 9) & 0xFF) << 16) | (((green / 9) & 0xFF) << 8) | ((blue / 9) & 0xFF);
        }
    }

    
    @SideOnly(Side.CLIENT)
    private transient IIcon[][] icons_by_meta;
    private transient IIcon[] itemicons_by_meta;
    
    private transient Subblock subblock_by_meta[];
    private transient int fireSpreadSpeed_by_meta[] = null;
    private transient int flamability_by_meta[] = null;
    private transient int lightValue_by_meta[] = null;
    private transient int lightOpacity_by_meta[] = null;
    private transient int lightValueInt;
    private transient ColorMultHandler colorMultHandler;
    private transient ColorMultHandler colorMultHandlerByMeta[];
    private transient BoundingBox boundingBoxByMeta[];
    private transient List<String> sounds_by_meta[] = null;
    private transient List<Particle> particles_by_meta[] = null;
    private transient boolean hasCollisionBoxes = false;
    
    private static final Map<String, Material> materialTable = new HashMap<String, Material>();
    private static final Map<String, Block.SoundType> stepSoundTable = new HashMap<String, Block.SoundType>();
    private static final Map<String, CreativeTabs> tabTable = new HashMap<String, CreativeTabs>();
    private static final Map<String, WesterosBlockFactory> typeTable = new HashMap<String, WesterosBlockFactory>();
    private static final Map<String, ColorMultHandler> colorMultTable = new HashMap<String, ColorMultHandler>();
    private static final HashSet<String> particles = new HashSet<String>();

    private int metaMask = 0xF; // Bitmask for translating raw metadata values to base (subblock) meta values
    
    public void setMetaMask(int mask) {
        metaMask = mask;
    }
    
    public Block[] createBlocks() {
        WesterosBlockFactory bf = typeTable.get(blockType);
        if (bf == null) {
            WesterosBlocks.log.severe(String.format("Invalid blockType '%s' in block '%s'", blockType, blockName));
            return null;
        }
        return bf.buildBlockClasses(this);
    }
    
    public Material getMaterial() {
        Material m = materialTable.get(material);
        if (m == null) {
            WesterosBlocks.log.warning(String.format("Invalid material '%s' in block '%s'", material, blockName));
            return Material.ROCK;
        }
        return m;
    }
    
    public Block.SoundType getStepSound() {
        Block.SoundType ss = stepSoundTable.get(stepSound);
        if (ss == null) {
            WesterosBlocks.log.warning(String.format("Invalid step sound '%s' in block '%s'", stepSound, blockName));
            return Block.soundTypeStone;
        }
        return ss;
    }
    
    public CreativeTabs getCreativeTab() {
        CreativeTabs ct = tabTable.get(creativeTab);
        if (ct == null) {
            WesterosBlocks.log.warning(String.format("Invalid tab name '%s' in block '%s'", creativeTab, blockName));
            ct = WesterosBlocksCreativeTab.tabWesterosBlocks;
        }
        return ct;
    }

    @SuppressWarnings("unchecked")
    private void initMeta() {
        subblock_by_meta = new Subblock[metaMask+1];
        lightValueInt = (int)(15.0F * lightValue);
        this.colorMultHandler = getColorHandler(this.colorMult);
        if (this.colorMultHandler == null) {
            WesterosBlocks.log.warning(String.format("Invalid colorMult '%s' in block '%s'", this.colorMult, blockName));
            this.colorMultHandler = getColorHandler("#FFFFFF");
        }
        boundingBoxByMeta = new BoundingBox[16];    // Always do all 16: rotated blocks change this
        for (int i = 0; i < 16; i++) {
            boundingBoxByMeta[i] = this.boundingBox;
        }
        
        if (subBlocks != null) {
            for (Subblock sb : subBlocks) {
                subblock_by_meta[sb.meta] = sb;
                if (sb.fireSpreadSpeed != DEF_INT) {
                    if (fireSpreadSpeed_by_meta == null) {
                        fireSpreadSpeed_by_meta = new int[metaMask+1];
                        if (this.fireSpreadSpeed != DEF_INT) {
                            Arrays.fill(fireSpreadSpeed_by_meta, this.fireSpreadSpeed);
                        }
                    }
                    fireSpreadSpeed_by_meta[sb.meta] = sb.fireSpreadSpeed; 
                }
                if (sb.flamability != DEF_INT) {
                    if (flamability_by_meta == null) {
                        flamability_by_meta = new int[metaMask+1];
                        if (this.flamability != DEF_INT) {
                            Arrays.fill(flamability_by_meta, this.flamability);
                        }
                    }
                    flamability_by_meta[sb.meta] = sb.flamability; 
                }
                if (sb.lightValue != DEF_FLOAT) {
                    if (lightValue_by_meta == null) {
                        lightValue_by_meta = new int[metaMask+1];
                        if (this.lightValue != DEF_FLOAT) {
                            Arrays.fill(lightValue_by_meta, (int)(15.0F * this.lightValue));
                        }
                    }
                    lightValue_by_meta[sb.meta] = (int)(15.0F * sb.lightValue); 
                }
                if (sb.lightOpacity != DEF_INT) {
                    if (lightOpacity_by_meta == null) {
                        lightOpacity_by_meta = new int[metaMask+1];
                        if (this.lightOpacity != DEF_INT) {
                            Arrays.fill(lightOpacity_by_meta, this.lightOpacity);
                        }
                    }
                    lightOpacity_by_meta[sb.meta] = sb.lightOpacity; 
                }
                if (sb.colorMult != null) {
                    if (this.colorMultHandlerByMeta == null) {
                        this.colorMultHandlerByMeta = new ColorMultHandler[metaMask+1];
                        Arrays.fill(this.colorMultHandlerByMeta, this.colorMultHandler);
                    }
                    this.colorMultHandlerByMeta[sb.meta] = getColorHandler(sb.colorMult);
                    if (this.colorMultHandlerByMeta[sb.meta] == null) {
                        WesterosBlocks.log.warning(String.format("Invalid colorMult '%s' in block '%s'", sb.colorMult, blockName));
                        this.colorMultHandlerByMeta[sb.meta] = this.colorMultHandler;
                    }
                }
                if ((sb.boundingBox != null) && (sb.cuboids == null)) {
                    Cuboid c = new Cuboid();
                    c.xMin = sb.boundingBox.xMin;
                    c.xMax = sb.boundingBox.xMax;
                    c.yMin = sb.boundingBox.yMin;
                    c.yMax = sb.boundingBox.yMax;
                    c.zMin = sb.boundingBox.zMin;
                    c.zMax = sb.boundingBox.zMax;
                    sb.cuboids = Collections.singletonList(c);
                }
                if ((sb.cuboids != null) && (sb.boundingBox == null)) {
                    sb.boundingBox = new BoundingBox();
                    sb.boundingBox.xMin = sb.boundingBox.yMin = sb.boundingBox.zMin = 1.0F;
                    sb.boundingBox.xMax = sb.boundingBox.yMax = sb.boundingBox.zMax = 0.0F;
                    for (BoundingBox bb : sb.cuboids) {
                        if (bb.xMin < sb.boundingBox.xMin) sb.boundingBox.xMin = bb.xMin;
                        if (bb.yMin < sb.boundingBox.yMin) sb.boundingBox.yMin = bb.yMin;
                        if (bb.zMin < sb.boundingBox.zMin) sb.boundingBox.zMin = bb.zMin;
                        if (bb.xMax > sb.boundingBox.xMax) sb.boundingBox.xMax = bb.xMax;
                        if (bb.yMax > sb.boundingBox.yMax) sb.boundingBox.yMax = bb.yMax;
                        if (bb.zMax > sb.boundingBox.zMax) sb.boundingBox.zMax = bb.zMax;
                    }
                }
                // If block specific bounding box, copy it to all matching meta slots
                if (sb.boundingBox != null) {
                    for (int i = 0; i < 16; i++) {
                        if ((i & metaMask) == sb.meta) {
                            this.boundingBoxByMeta[i] = sb.boundingBox;
                        }
                    }
                }
                // If custom sounds
                if (sb.soundList != null) {
                    if (this.sounds_by_meta == null) {
                        this.sounds_by_meta = new List[metaMask+1];
                    }
                    this.sounds_by_meta[sb.meta] = sb.soundList;
                }
                // If particle effects
                if ((sb.particles != null) && (sb.particles.size() > 0)) {
                    if (this.particles_by_meta == null) {
                        this.particles_by_meta = new List[metaMask+1];
                    }
                    this.particles_by_meta[sb.meta]= sb.particles; 
                    for (Particle pid : sb.particles) {
                        if (particles.contains(pid.particle) == false) {
                            WesterosBlocks.log.warning(String.format("Invalid particle '%s' in block '%s'", pid.particle, blockName));
                            pid.particle = "smoke"; // Use smoke by default
                        }
                    }
                }
                // If any collision boxes, mark ti
                if (sb.collisionBoxes != null) {
                    hasCollisionBoxes = true;
                }
            }
        }
    }
    
    public boolean hasCollisionBoxes() {
        return hasCollisionBoxes;
    }
    
    public Subblock getByMeta(int meta) {
        if (subblock_by_meta == null) {
            initMeta();
        }
        return subblock_by_meta[meta & metaMask];
    }

    // Do standard constructor settings for given block class
    public void doStandardContructorSettings(Block blk) {
        doStandardContructorSettings(blk, 0);
    }
    public void doStandardContructorSettings(Block blk, int idx) {
        if (this.hardness != DEF_FLOAT) {
            blk.setHardness(this.hardness);
        }
        if (this.lightOpacity == DEF_INT) {
            if (blk.isOpaqueCube()) {
                this.lightOpacity = 255;
            }
            else {
                this.lightOpacity = 0;
            }
        }
        blk.setLightOpacity(this.lightOpacity);
        
        if (this.resistance != DEF_FLOAT) {
            blk.setResistance(this.resistance);
        }
        if (this.lightValue == DEF_FLOAT) {
            this.lightValue = 0.0F;
        }
        blk.setLightLevel(this.lightValue);
        blk.setBlockName(this.getBlockName(idx));
        if (this.stepSound != null) {
            blk.setStepSound(this.getStepSound());
        }
        if ((this.fireSpreadSpeed > 0) || (this.flamability > 0)) {
            Blocks.FIRE.setFireInfo(blk, this.fireSpreadSpeed, this.flamability);
        }
        if (creativeTab != null) {
            blk.setCreativeTab(getCreativeTab());
        }
        if (boundingBox != null) {
            blk.setBlockBounds(boundingBox.xMin, boundingBox.yMin, boundingBox.zMin, boundingBox.xMax, boundingBox.yMax, boundingBox.zMax);
        }
        if (this.particles_by_meta != null) {   // Any particles?
            blk.setTickRandomly(true);
        }
        ClassTransformer.setUseNeighborBrightness(blk, ((blk instanceof BlockStairs) || (blk instanceof BlockSlab) || (blk instanceof BlockFarmland) || ClassTransformer.getCanBlockGrass(blk) || (blk.getLightOpacity() == 0)));
    }
    // Do standard initialize actions
    public void doStandardInitializeActions(Block blk) {
        // Register any harvest levels
        if (harvestLevel != null) { // Do overall first
            for (HarvestLevel hl : harvestLevel) {
                blk.setHarvestLevel(hl.tool, hl.level);
            }
        }
        // And do any meta-specific overrides second
        if (this.subBlocks != null) {
            for (Subblock sb : this.subBlocks) {
                if (sb.harvestLevel != null) { // Do overall first
                    for (HarvestLevel hl : sb.harvestLevel) {
                        blk.setHarvestLevel(hl.tool, hl.level);
                    }
                }
            }
        }
    }
    
    public String getBlockName(int blknum) {
        if (blknum == 0)
            return this.blockName;
        else
            return this.blockName + "_" + (blknum+1);
    }
    // Do standard register actions
    public void doStandardRegisterActions(Block blk, Class<? extends ItemBlock> itmclass) {
        doStandardRegisterActions(blk, itmclass, 0);
    }
    // Do standard register actions
    public void doStandardRegisterActions(Block blk, Class<? extends ItemBlock> itmclass, int idx) {
        int reqID = (this.blockIDs[idx] >= 0) ? this.blockIDs[idx] : -1;
        if (itmclass == null) itmclass = ItemBlock.class;
        // Register the block
        //GameRegistry.registerBlock(blk, itmclass, this.getBlockName(idx));
        registerBlock(blk, itmclass, this.getBlockName(idx), reqID);
        // And register strings for each item block
        if ((this.subBlocks != null) && (this.subBlocks.size() > 0)) {
            for (Subblock sb : this.subBlocks) {
                if (sb.noInventoryItem) continue; 
                if (sb.label == null) {
                    sb.label = this.blockName + " " + sb.meta;
                }
                LanguageRegistry.addName(new ItemStack(blk, 1, sb.meta), sb.label);
            }
        }
        if (subblock_by_meta == null) {
            initMeta();
        }
    }

    private static Block registerBlock(Block block, Class<? extends ItemBlock> itemclass, String name, int id)
    {
        try
        {
            ItemBlock i = null;
            if (itemclass != null)
            {
                Class<?>[] ctorArgClasses = new Class<?>[1];
                ctorArgClasses[0] = Block.class;
                Constructor<? extends ItemBlock> itemCtor = itemclass.getConstructor(ctorArgClasses);
                i = itemCtor.newInstance(new Object[] { block });
            }
            // block registration has to happen first
            GameData.getBlockRegistry().addObject(id, name, block);
            if (i != null)
            {
                GameData.getBlockRegistry().addObject(id, name, i);
            }
            return block;
        }
        catch (Exception e)
        {
            WesterosBlocks.crash(e, "Caught an exception during block registration");
            return null;
        }
    }

    
    @SideOnly(Side.CLIENT)
    public void doStandardRegisterIcons(IIconRegister ir) {
        if (subblock_by_meta == null) {
            initMeta();
        }
        icons_by_meta = new IIcon[metaMask+1][];
        if (subBlocks != null) {
            HashMap<String, IIcon> map = new HashMap<String, IIcon>();
            for (Subblock sb : subBlocks) {
                if (sb.textures == null) {
                    WesterosBlocks.log.warning(String.format("No textures for subblock '%d' of block '%s'", sb.meta, this.blockName));
                    sb.textures = Collections.singletonList("INVALID_" + blockName + "_" + sb.meta);
                }
                icons_by_meta[sb.meta] = new IIcon[sb.textures.size()];
                for (int i = 0; i < sb.textures.size(); i++) {
                    String txt = sb.textures.get(i);
                    if (txt.indexOf(':') < 0) {
                        txt = "westerosblocks:" + txt;
                    }
                    IIcon ico = map.get(txt);
                    if (ico == null) {
                        ico = ir.registerIcon(txt);
                        map.put(txt, ico);
                    }
                    icons_by_meta[sb.meta][i] = ico;
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void doStandardItemRegisterIcons(IIconRegister ir) {
        if (subblock_by_meta == null) {
            initMeta();
        }
        itemicons_by_meta = new IIcon[metaMask+1];
        if (subBlocks != null) {
            HashMap<String, IIcon> map = new HashMap<String, IIcon>();
            for (Subblock sb : subBlocks) {
                if (sb.noInventoryItem) continue; 
                if (sb.itemTexture != null) {
                    String txt = sb.itemTexture;
                    if (txt.indexOf(':') < 0) {
                        txt = "westerosblocks:" + txt;
                    }
                    IIcon ico = map.get(txt);
                    if (ico == null) {
                        ico = ir.registerIcon(txt);
                        map.put(txt, ico);
                    }
                    itemicons_by_meta[sb.meta] = ico;
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public IIcon doStandardIconGet(int side, int meta) {
        if (icons_by_meta == null) {
            return null;
        }
        int m = meta & metaMask;
        if (m >= icons_by_meta.length) {
            m = 0;
        }
        IIcon[] ico = icons_by_meta[m];
        if (ico != null) {
            if (side >= ico.length) {
                side = ico.length - 1;
            }
            return ico[side];
        }
        return null;
    }

    @SideOnly(Side.CLIENT)
    public void getStandardSubBlocks(Block blk, int id, CreativeTabs tab, List<ItemStack> list) {
        if (subBlocks != null) {
            for (Subblock sb : subBlocks) {
                if (sb.noInventoryItem) continue; 
                list.add(new ItemStack(blk, 1, sb.meta));
            }
        }
    }
    /*
     * Get default texture (first one)
     */
    public String getFirstTexture() {
        if (subBlocks != null) {
            for (Subblock sb : subBlocks) {
                if ((sb.textures != null) && (sb.textures.size() > 0)) {
                    return sb.textures.get(0);
                }
            }
        }
        return "INVALID_" + this.blockName;
    }

    public int getFlammability(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
        if (flamability_by_meta != null) {
            return flamability_by_meta[world.getBlockMetadata(x,  y,  z) & metaMask];
        }
        return this.flamability;
    }

    public List<String> getSoundIDList(int metadata) {
        metadata &= metaMask;
        if (this.sounds_by_meta != null) {
            return this.sounds_by_meta[metadata];
        }
        return null;
    }

    public List<Particle> getParticleList(int metadata) {
        metadata &= metaMask;
        if (this.particles_by_meta != null) {
            return this.particles_by_meta[metadata];
        }
        return null;
    }

    public int getFireSpreadSpeed(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
        if (fireSpreadSpeed_by_meta != null) {
            return fireSpreadSpeed_by_meta[world.getBlockMetadata(x,  y,  z) & metaMask];
        }
        return this.fireSpreadSpeed;
    }
        
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        if (this.lightValue_by_meta != null) {
            return this.lightValue_by_meta[world.getBlockMetadata(x,  y,  z) & metaMask];
        }
        return this.lightValueInt;
    }
        
    public int getLightOpacity(Block blk, IBlockAccess world, int x, int y, int z) {
        if (this.lightOpacity_by_meta != null) {
            return this.lightOpacity_by_meta[world.getBlockMetadata(x,  y,  z) & metaMask];
        }
        return blk.getLightOpacity();
    }
    
    public int getBlockColor() {
        return this.colorMultHandler.getBlockColor();
    }
    
    public int getRenderColor(int meta) {
        meta &= metaMask;
        if (this.colorMultHandlerByMeta != null) {
            return this.colorMultHandlerByMeta[meta].getBlockColor();
        }
        return this.colorMultHandler.getBlockColor();
    }
    
    public int colorMultiplier(IBlockAccess access, int x, int y, int z) {
        if (this.colorMultHandlerByMeta != null) {
            int meta = access.getBlockMetadata(x, y, z) & metaMask;
            return this.colorMultHandlerByMeta[meta].colorMultiplier(access, x, y, z);
        }
        return this.colorMultHandler.colorMultiplier(access, x, y, z);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void getStandardCreativeItems(Block blk, ArrayList itemList) {
        if (subBlocks != null) {
            for (Subblock sb : subBlocks) {
                itemList.add(new ItemStack(blk, 1, sb.meta));
            }
        }
    }

    // Override default bounding box for given meta
    public void setBoundingBox(int meta, float xmin, float ymin, float zmin, float xmax, float ymax, float zmax) {
        BoundingBox bb = new BoundingBox();
        this.boundingBoxByMeta[meta] = bb;
        bb.xMin = xmin;
        bb.xMax = xmax;
        bb.yMin = ymin;
        bb.yMax = ymax;
        bb.zMin = zmin;
        bb.zMax = zmax;
    }
    
    public BoundingBox getBoundingBox(int meta) {
        return boundingBoxByMeta[meta];
    }
    
    public List<Cuboid> getCuboidList(int meta) {
        meta &= metaMask;
        
        Subblock sb = getByMeta(meta);
        if ((sb != null) && (sb.cuboids != null)) {
           return sb.cuboids;
        }
        return Collections.emptyList();
    }

    public List<BoundingBox> getCollisionBoxList(int meta) {
        meta &= metaMask;
        
        Subblock sb = getByMeta(meta);
        if ((sb != null) && (sb.collisionBoxes != null)) {
           return sb.collisionBoxes;
        }
        return Collections.emptyList();
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        BoundingBox bb = getBoundingBox(meta);
        if (bb != null) {
            return AxisAlignedBB.getBoundingBox((double)x + bb.xMin, (double)y + bb.yMin, (double)z + bb.zMin, (double)x + bb.xMax, (double)y + bb.yMax, (double)z + bb.zMax);
        }
        return null;
    }
    public void setBlockBoundsBasedOnState(Block blk, IBlockAccess blockaccess, int x, int y, int z) {
        int meta = blockaccess.getBlockMetadata(x, y, z);
        BoundingBox bb = getBoundingBox(meta);
        if (bb != null) {
            blk.setBlockBounds(bb.xMin,  bb.yMin,  bb.zMin, bb.xMax, bb.yMax, bb.zMax);
        }
        else {
            blk.setBlockBounds(0, 0, 0, 1, 1, 1);
        }
    }
    public boolean shouldSideBeRendered(IBlockAccess access, int x, int y, int z, int side) {
        int meta = access.getBlockMetadata(x, y, z);
        BoundingBox bb = getBoundingBox(meta);
        if (bb == null) return true;
        switch (side) {
            case 0: // Bottom
                return (bb.yMin > 0.0F);
            case 1: // Top
                return (bb.yMax < 1.0F);
            case 2: // Zmin
                return (bb.zMin > 0.0F);
            case 3: // Zmax
                return (bb.zMax < 1.0F);
            case 4: // Xmin
                return (bb.xMin > 0.0F);
            case 5: // Xmax
                return (bb.xMax < 1.0F);
            default:
                return true;
        }
    }
    
    public static void addCreativeTab(String name, CreativeTabs tab) {
        tabTable.put(name,  tab);
    }

    public String getType(int meta) {
        meta &= metaMask;
        
        Subblock sb = getByMeta(meta);
        if ((sb != null) && (sb.type != null)) {
            return sb.type;
        }
        return this.type;
    }

    public EnumPlantType getPlantType(int meta) {
        meta &= metaMask;
        EnumPlantType pt = EnumPlantType.Plains;
        String t = getType(meta);
        if ((t != null) && (t.length() > 0)) {
            try {
                pt = EnumPlantType.valueOf(t);
            } catch (IllegalArgumentException iax) {
            }
            if (pt == null) {
                WesterosBlocks.log.severe(String.format("Invalid plant type '%s' at meta %d of block '%s'", t, meta, this.blockName));
                pt = EnumPlantType.Plains;
                Subblock sb = getByMeta(meta);
                sb.type = pt.name();
            }
        }
        return pt;
    }

    private static final int[] all_meta = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 };
    
    // Used by factory classes to confirm that configuration has acceptable meta values
    public boolean validateMetaValues(int[] valid_meta, int[] req_meta) {
        if (valid_meta == null) {
            valid_meta = all_meta;
        }
        if (req_meta == null) {
            req_meta = new int[0];
        }
        if (subBlocks != null) {
            for (Subblock sb : subBlocks) {
                if (sb == null) continue;
                int m = sb.meta;
                boolean match = false;
                for (int vmeta : valid_meta) {
                    if (m == vmeta) {
                        match = true;
                        break;
                    }
                }
                if (!match) {
                    WesterosBlocks.log.severe(String.format("meta value %d for block '%s' is not valid for block type '%s'", sb.meta, this.blockName, this.blockType));
                    return false;
                }
                // If value exceeds metaMask-ed bits
                if ((m & metaMask) != m) {
                    WesterosBlocks.log.severe(String.format("meta value %d for block '%s' is not valid for block type '%s' - metaMask=%x", sb.meta, this.blockName, this.blockType, metaMask));
                    return false;
                }
            }
        }
        // Check for required values
        for (int req : req_meta) {
            boolean match = false;
            if (subBlocks != null) {
                for (Subblock sb : subBlocks) {
                    if (sb == null) continue;
                    if (sb.meta == req) {
                        match = true;
                        break;
                    }
                }
            }
            if (!match) {
                WesterosBlocks.log.severe(String.format("Block '%s' is missing required meta value %d for block type '%s'", this.blockName, req, this.blockType));
                return false;
            }
        }
        return true;
    }
    
    public static boolean sanityCheck(WesterosBlockDef[] defs) {
        HashSet<String> names = new HashSet<String>();
        BitSet metas = new BitSet();
        BitSet ids = new BitSet();
        // Make sure block IDs and names are unique
        for (WesterosBlockDef def : defs) {
            if (def == null) continue;
            if (def.blockName == null) {
                WesterosBlocks.log.severe("Block definition is missing blockName");
                return false;
            }
            if (names.add(def.blockName) == false) {    // If alreay defined
                WesterosBlocks.log.severe(String.format("Block '%s' - blockName duplicated", def.blockName));
                return false;
            }
            
            if (def.blockIDs != null) {
                for (int i = 0; i < def.blockIDs.length; i++) {
                    if (def.blockIDs[i] < 0) {  // Autoassign is OK
                        continue;
                    }
                    else if (def.blockIDs[i] > 4095) {
                        WesterosBlocks.log.severe(String.format("Block '%s' - blockIDs[%d] invalid", def.blockName, i));
                        return false;
                    }
                    else if (ids.get(def.blockIDs[i])) {    // If already defined
                        WesterosBlocks.log.severe(String.format("Block '%s' - blockIDs[%d] duplicated", def.blockName, i));
                        return false;
                    }
                    ids.set(def.blockIDs[i]);
                }
                def.blockID = def.blockIDs[0];
            }
            else {
                if (def.blockID < 0) {  // Autoassign is OK
                    
                }
                else if (def.blockID > 4095) {
                    WesterosBlocks.log.severe(String.format("Block '%s' - blockID invalid", def.blockName));
                    return false;
                }
                else if (ids.get(def.blockID)) {    // If already defined
                    WesterosBlocks.log.severe(String.format("Block '%s' - blockID duplicated", def.blockName));
                    return false;
                }
                else {
                    ids.set(def.blockID);
                }
                def.blockIDs = new int[] { def.blockID };
            }
            // Check for duplicate meta
            metas.clear();
            if (def.subBlocks != null) {
                for (Subblock sb : def.subBlocks) {
                    if (sb == null) continue;
                    if (metas.get(sb.meta)) {
                        WesterosBlocks.log.severe(String.format("Block '%s' - duplicate meta value %d", def.blockName, sb.meta));
                        return false;
                    }
                    metas.set(sb.meta);
                }
            }
        }
        WesterosBlocks.log.info("WesterosBlocks.json passed sanity check");
        return true;
    }
    
    public IIcon getItemIcon(int meta) {
        meta &= metaMask;
        if (itemicons_by_meta != null) {
            if (meta >= itemicons_by_meta.length) {
                meta = 0;
            }
            if (itemicons_by_meta[meta] != null) {
                return itemicons_by_meta[meta];
            }
        }
        Subblock sb = getByMeta(meta);
        int idx = 0;
        if (sb != null) {
            idx = sb.itemTextureIndex;
        }
        return doStandardIconGet(idx, meta);
    }
    
    public static void initialize() {
        materialTable.put("air",  Material.air);
        materialTable.put("grass",  Material.grass);
        materialTable.put("ground",  Material.ground);
        materialTable.put("wood",  Material.wood);
        materialTable.put("rock",  Material.ROCK);
        materialTable.put("iron", Material.iron);
        materialTable.put("anvil", Material.anvil);
        materialTable.put("water", Material.water);
        materialTable.put("lava", Material.lava);
        materialTable.put("leaves", Material.leaves);
        materialTable.put("plants", Material.plants);
        materialTable.put("vine", Material.vine);
        materialTable.put("sponge", Material.sponge);
        materialTable.put("cloth", Material.cloth);
        materialTable.put("fire", Material.FIRE);
        materialTable.put("sand", Material.sand);
        materialTable.put("circuits", Material.circuits);
        materialTable.put("glass", Material.glass);
        materialTable.put("redstoneLight", Material.redstoneLight);
        materialTable.put("tnt", Material.tnt);
        materialTable.put("coral", Material.coral);
        materialTable.put("ice", Material.ice);
        materialTable.put("snow", Material.snow);
        materialTable.put("craftedSnow", Material.craftedSnow);
        materialTable.put("cactus", Material.cactus);
        materialTable.put("clay", Material.clay);
        materialTable.put("pumpkin", Material.gourd);
        materialTable.put("dragonEgg", Material.dragonEgg);
        materialTable.put("portal", Material.portal);
        materialTable.put("cake", Material.cake);
        materialTable.put("web", Material.web);
        materialTable.put("piston", Material.piston);

        stepSoundTable.put("powder", Block.soundTypeSand);
        stepSoundTable.put("wood", Block.soundTypeWood);
        stepSoundTable.put("gravel", Block.soundTypeGravel);
        stepSoundTable.put("grass", Block.soundTypeGrass);
        stepSoundTable.put("stone", Block.soundTypeStone);
        stepSoundTable.put("metal", Block.soundTypeMetal);
        stepSoundTable.put("glass", Block.soundTypeGlass);
        stepSoundTable.put("cloth", Block.soundTypeCloth);
        stepSoundTable.put("sand", Block.soundTypeSand);
        stepSoundTable.put("snow", Block.soundTypeSnow);
        stepSoundTable.put("ladder", Block.soundTypeLadder);
        stepSoundTable.put("anvil", Block.soundTypeAnvil);
        // Tab table
        tabTable.put("buildingBlocks", CreativeTabs.tabBlock);
        tabTable.put("decorations", CreativeTabs.tabDecorations);
        tabTable.put("redstone", CreativeTabs.tabRedstone);
        tabTable.put("transportation", CreativeTabs.tabTransport);
        tabTable.put("misc", CreativeTabs.tabMisc);
        tabTable.put("food", CreativeTabs.tabFood);
        tabTable.put("tools", CreativeTabs.tabTools);
        tabTable.put("combat", CreativeTabs.tabCombat);
        tabTable.put("brewing", CreativeTabs.tabBrewing);
        tabTable.put("materials", CreativeTabs.tabMaterials);

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
        typeTable.put("ladder", new WCLadderBlock.Factory());
        typeTable.put("cuboid", new WCCuboidBlock.Factory());
        typeTable.put("cuboid-nsew", new WCCuboidNSEWBlock.Factory());
        typeTable.put("cuboid-ne", new WCCuboidNEBlock.Factory());
        typeTable.put("cuboid-nsewud", new WCCuboidNSEWUDBlock.Factory());
        typeTable.put("cuboid-nsew-stack", new WCCuboidNSEWStackBlock.Factory());
        typeTable.put("cuboid-ne-stack", new WCCuboidNEStackBlock.Factory());
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
        // Standard color multipliers
        colorMultTable.put("#FFFFFF", new ColorMultHandler());
        colorMultTable.put("water", new WaterColorMultHandler());
        colorMultTable.put("foliage", new FoliageColorMultHandler());
        colorMultTable.put("grass", new GrassColorMultHandler());
        colorMultTable.put("pine", new PineColorMultHandler());
        colorMultTable.put("birch", new BirchColorMultHandler());
        colorMultTable.put("basic", new BasicColorMultHandler());
        colorMultTable.put("lily", new ColorMultHandler(2129968));
        
        // Valid particle values
        particles.add("hugeexplosion");
        particles.add("largeexplode");
        particles.add("fireworksSpark");
        particles.add("bubble");
        particles.add("suspended");
        particles.add("depthsuspend");
        particles.add("townaura");
        particles.add("crit");
        particles.add("magicCrit");
        particles.add("smoke");
        particles.add("mobSpell");
        particles.add("mobSpellAmbient");
        particles.add("spell");
        particles.add("instantSpell");
        particles.add("witchMagic");
        particles.add("note");
        particles.add("portal");
        particles.add("enchantmenttable");
        particles.add("explode");
        particles.add("flame");
        particles.add("lava");
        particles.add("footstep");
        particles.add("splash");
        particles.add("largesmoke");
        particles.add("cloud");
        particles.add("reddust");
        particles.add("snowballpoof");
        particles.add("dripWater");
        particles.add("dripLava");
        particles.add("snowshovel");
        particles.add("slime");
        particles.add("heart");
        particles.add("angryVillager");
        particles.add("happyVillager");
     }
    // Get color muliplier
    public ColorMultHandler getColorHandler(String hnd) {
        String hndid = hnd.toUpperCase();
        ColorMultHandler cmh = colorMultTable.get(hndid);
        if (cmh == null) { 
            // See if color code
            if ((hndid.length() == 7) && (hndid.charAt(0) == '#')) {
                try {
                    cmh = new ColorMultHandler(Integer.parseInt(hndid.substring(1), 16));
                    colorMultTable.put(hndid, cmh);
                } catch (NumberFormatException nfx) {
                }
            }
            // See if resource
            else {
                int idx = hnd.indexOf(':');
                if (idx < 0) {
                    hnd = "westerosblocks:" + hnd;
                    cmh = colorMultTable.get(hnd);
                }
                if (hnd.endsWith(".png") == false) {
                    hnd = hnd + ".png";
                }
                if (cmh == null) {
                    cmh = new CustomColorMultHandler(hnd, blockName);
                    colorMultTable.put(hndid, cmh);
                }
            }
        }
        return cmh;
    }
    /**
     * Default texture registration for Dynmap
     */
    public void defaultRegisterTextures(ModTextureDefinition mtd) {
        mtd.setTexturePath("assets/westerosblocks/textures/blocks/");
        HashSet<String> txtids = new HashSet<String>(); // Build set if distinct IDs
        HashSet<String> maptxtids = new HashSet<String>(); // Build set if distinct IDs
        if ((colorMult != null) && (colorMult.startsWith("#") == false)) {
            maptxtids.add(colorMult);
        }
        if (this.subblock_by_meta != null) {
            for (int i = 0; i < this.subblock_by_meta.length; i++) {
                Subblock sb = subblock_by_meta[i];
                if (sb != null) {
                    if (sb.textures != null) {
                        txtids.addAll(sb.textures);
                    }
                    if ((sb.colorMult != null) && (sb.colorMult.startsWith("#") == false)) {
                        maptxtids.add(sb.colorMult);
                    }
                }
            }
        }
        // Register the textures
        for (String txtid : txtids) {
            int colon = txtid.indexOf(':');
            if (colon < 0) {
                mtd.registerTextureFile(txtid);
            }
            else {
                mtd.registerTextureFile(txtid.replace(':', '_'), 
                        "assets/" + txtid.substring(0, colon).toLowerCase() + "/textures/blocks/" + txtid.substring(colon+1) + ".png");
            }
        }
        // Register the maps
        for (String txtid : maptxtids) {
            int colon = txtid.indexOf(':');
            if (colon < 0) {
                mtd.registerBiomeTextureFile(txtid, "assets/westerosblocks/" + txtid + ".png");
            }
            else {
                mtd.registerBiomeTextureFile(txtid.replace(':', '_'), 
                        "assets/" + txtid.substring(0, colon).toLowerCase() + "/" + txtid.substring(colon+1) + ".png");
            }
        }
    }
    /**
     * Default texture block registration for Dynmap (min patch count
     */
    public void registerPatchTextureBlock(ModTextureDefinition mtd, int minPatchCount) {
        registerPatchTextureBlock(mtd, minPatchCount, TransparencyMode.TRANSPARENT);
    }
    public void registerPatchTextureBlock(ModTextureDefinition mtd, int minPatchCount, TransparencyMode tm) {
        if (this.subblock_by_meta != null) {
            TextureModifier tmod = TextureModifier.NONE;
            if (this.nonOpaque) {
                tmod = TextureModifier.CLEARINSIDE;
            }
            for (int idx = 0; idx < this.blockIDCount; idx++) {
                String blkname = this.getBlockName(idx);
                for (int i = 0; i < this.subblock_by_meta.length; i++) {
                    Subblock sb = subblock_by_meta[i];
                    if ((sb != null) && (sb.textures != null)) {
                        BlockTextureRecord mtr = mtd.addBlockTextureRecord(blkname);
                        if (tm != null) {
                            mtr.setTransparencyMode(tm);
                        }
                        // Set for all associated metas
                        for (int meta = i; meta < 16; meta++) {
                            if ((meta & metaMask) == (i & metaMask)) {
                                mtr.setMetaValue(meta);
                            }
                        }
                        int cnt = sb.textures.size();
                        if (cnt < minPatchCount) {
                            cnt = minPatchCount;
                        }
                        for (int patch = 0; patch < cnt; patch++) {
                            int fidx = patch;
                            if (fidx >= sb.textures.size()) {
                                fidx = sb.textures.size() - 1;
                            }
                            String txtid = sb.textures.get(fidx);
                            mtr.setPatchTexture(txtid.replace(':', '_'), tmod, patch);
                        }
                        setBlockColorMap(mtr, sb);
                    }
                }
            }
        }
    }
    /**
     * Default texture block (6 face) registration for Dynmap
     */
    public void defaultRegisterTextureBlock(ModTextureDefinition mtd) {
        defaultRegisterTextureBlock(mtd, 0, null);
    }
    /**
     * Default texture block (6 face) registration for Dynmap
     */
    public void defaultRegisterTextureBlock(ModTextureDefinition mtd, int idx, TransparencyMode tm) {
        if (this.subblock_by_meta != null) {
            TextureModifier tmod = TextureModifier.NONE;
            if (this.nonOpaque) {
                tmod = TextureModifier.CLEARINSIDE;
            }
            String blkname = this.getBlockName(idx);
            for (int i = 0; i < this.subblock_by_meta.length; i++) {
                Subblock sb = subblock_by_meta[i];
                if ((sb != null) && (sb.textures != null)) {
                    BlockTextureRecord mtr = mtd.addBlockTextureRecord(blkname);
                    if (tm != null) {
                        mtr.setTransparencyMode(tm);
                    }
                    // Set for all associated metas
                    for (int meta = i; meta < 16; meta++) {
                        if ((meta & metaMask) == (i & metaMask)) {
                            mtr.setMetaValue(meta);
                        }
                    }
                    for (int face = 0; face < 6; face++) {
                        int fidx = face;
                        if (fidx >= sb.textures.size()) {
                            fidx = sb.textures.size() - 1;
                        }
                        String txtid = sb.textures.get(fidx);
                        mtr.setSideTexture(txtid.replace(':', '_'), tmod, BlockSide.valueOf("FACE_" + face));
                    }
                    setBlockColorMap(mtr, sb);
                }
            }
        }
    }
    public void setBlockColorMap(BlockTextureRecord mtr, Subblock sb) {
        String blockColor = sb.colorMult;
        if (blockColor == null) blockColor = this.colorMult;
        if ((blockColor != null) && (blockColor.startsWith("#") == false)) {
            mtr.setBlockColorMapTexture(blockColor.replace(':', '_'));
        }
    }
    /**
     * Do standard random display processing
     */
    @SideOnly(Side.CLIENT)
    public void doRandomDisplayTick(World world, int x, int y, int z, Random rnd) {
        doRandomDisplayTick(world, x, y, z, rnd, null);
    }
    
    public void doRandomDisplayTick(World world, int x, int y, int z, Random rnd, CuboidRotation[] rot) {

        if (this.particles_by_meta == null) {
            return;
        }
        int meta = world.getBlockMetadata(x,  y,  z);
        List<Particle> lst = this.particles_by_meta[meta & metaMask]; // Get particles, if any
        if (lst == null) {
            return;
        }
        // Compute rotation
        CuboidRotation cr = CuboidRotation.NONE;
        if (rot != null) {
            int mmult = meta / (metaMask + 1);
            if (mmult >= rot.length) {
                mmult = 0;
            }
            cr = rot[mmult];
        }
        // And display particles
        for (Particle p : lst) {    // Loop through our particles
            if (p.particle == null) {
                continue;
            }
            // If not 100% on, and too high random, skip it
            if ((p.chance < 1.0F) && (p.chance >= rnd.nextFloat())) {
                continue;
            }
            double dx = p.x;
            double dy = p.y;
            double dz = p.z;
            double dvx = p.vx;
            double dvy = p.vy;
            double dvz = p.vz;
            if (p.xrand > 0.0F) { dx += p.xrand * (rnd.nextFloat() - 0.5F) * 2.0; }
            if (p.yrand > 0.0F) { dy += p.yrand * (rnd.nextFloat() - 0.5F) * 2.0; }
            if (p.zrand > 0.0F) { dz += p.zrand * (rnd.nextFloat() - 0.5F) * 2.0; }
            if (p.vxrand > 0.0F) { dvx += p.vxrand * (rnd.nextFloat() - 0.5F) * 2.0; }
            if (p.vyrand > 0.0F) { dvy += p.vyrand * (rnd.nextFloat() - 0.5F) * 2.0; }
            if (p.vzrand > 0.0F) { dvz += p.vzrand * (rnd.nextFloat() - 0.5F) * 2.0; }
            switch (cr) {
                default:
                    world.spawnParticle(p.particle, x + dx, y + dy, z + dz, dvx, dvy, dvz);
                    break;
                case ROTY90:
                    world.spawnParticle(p.particle, x + dz, y + dy, z + 1.0 - dx, -dvz, dvy, dvx);
                    break;
                case ROTY180:
                    world.spawnParticle(p.particle, x + 1.0 - dx, y + dy, z + 1.0 - dz, -dvx, dvy, -dvz);
                    break;
                case ROTY270:
                    world.spawnParticle(p.particle, x + 1.0 - dz, y + dy, z + dx, dvz, dvy, -dvx);
                    break;
            }
        }
    }
    public void setBlockIDCount(int cnt) {
        blockIDCount = cnt;
    }
}
