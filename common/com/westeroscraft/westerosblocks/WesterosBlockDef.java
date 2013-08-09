package com.westeroscraft.westerosblocks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.westeroscraft.westerosblocks.blocks.WCCropBlock;
import com.westeroscraft.westerosblocks.blocks.WCCuboidBlock;
import com.westeroscraft.westerosblocks.blocks.WCDoorBlock;
import com.westeroscraft.westerosblocks.blocks.WCFenceBlock;
import com.westeroscraft.westerosblocks.blocks.WCLeavesBlock;
import com.westeroscraft.westerosblocks.blocks.WCLogBlock;
import com.westeroscraft.westerosblocks.blocks.WCPaneBlock;
import com.westeroscraft.westerosblocks.blocks.WCPlantBlock;
import com.westeroscraft.westerosblocks.blocks.WCSandBlock;
import com.westeroscraft.westerosblocks.blocks.WCSlabBlock;
import com.westeroscraft.westerosblocks.blocks.WCSolidBlock;
import com.westeroscraft.westerosblocks.blocks.WCStairBlock;
import com.westeroscraft.westerosblocks.blocks.WCStepSound;
import com.westeroscraft.westerosblocks.blocks.WCTorchBlock;
import com.westeroscraft.westerosblocks.blocks.WCWallBlock;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.Block;
import net.minecraft.block.StepSound;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;

//
// Template for block configuration data (populated using GSON)
//
public class WesterosBlockDef {
    private static final float DEF_FLOAT = -999.0F;
    private static final int DEF_INT = -999;
    
    public String blockName;                // Locally unique block name
    public int blockID = DEF_INT;           // Block ID number (default)
    public String blockType = "solid";      // Block type ('solid', 'liquid', 'plant', 'log', 'stairs', etc)
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
        public BoundingBox boundingBox = null;  // Bounding box
        public String type = null;              // Block type specific type string (e.g. plant type)
        public int itemTextureIndex = 0;        // Index of texture for item icon
        public String colorMult = null;         // Color multiplier ("#rrggbb' for fixed value, 'foliage', 'grass', 'water')
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
        public int colorMultiplier(IBlockAccess access, int x, int y, int z) {
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
        public int colorMultiplier(IBlockAccess access, int x, int y, int z) {
            int red = 0;
            int green = 0;
            int blue = 0;

            for (int xx = -1; xx <= 1; ++xx) {
                for (int zz = -1; zz <= 1; ++zz) {
                    int mult = access.getBiomeGenForCoords(x + xx, z + zz).getBiomeFoliageColor();
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
        public int colorMultiplier(IBlockAccess access, int x, int y, int z) {
            int red = 0;
            int green = 0;
            int blue = 0;

            for (int xx = -1; xx <= 1; ++xx) {
                for (int zz = -1; zz <= 1; ++zz) {
                    int mult = access.getBiomeGenForCoords(x + xx, z + zz).getBiomeGrassColor();
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
        public int colorMultiplier(IBlockAccess access, int x, int y, int z) {
            int red = 0;
            int green = 0;
            int blue = 0;

            for (int xx = -1; xx <= 1; ++xx) {
                for (int zz = -1; zz <= 1; ++zz) {
                    int mult = access.getBiomeGenForCoords(x + xx, z + zz).getWaterColorMultiplier();
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
        public int colorMultiplier(IBlockAccess access, int x, int y, int z) {
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
        public int colorMultiplier(IBlockAccess access, int x, int y, int z) {
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
        public int colorMultiplier(IBlockAccess access, int x, int y, int z) {
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
                colorBuffer = TextureUtil.func_110986_a(Minecraft.getMinecraft().func_110442_L(), new ResourceLocation(rname));
            } catch (IOException e) {
                WesterosBlocks.log.severe(String.format("Invalid color resource '%s' in block '%s'", rname, blkname));
                Arrays.fill(colorBuffer,  0xFFFFFF);
            }
        }

        private int getColor(float tmp, float hum)
        {
            tmp = MathHelper.clamp_float(tmp, 0.0F, 1.0F);
            hum = MathHelper.clamp_float(hum, 0.0F, 1.0F);
            hum *= tmp;
            int i = (int)((1.0D - tmp) * 255.0D);
            int j = (int)((1.0D - hum) * 255.0D);
            return colorBuffer[j << 8 | i];
        }
        
        @Override
        @SideOnly(Side.CLIENT)
        public int colorMultiplier(IBlockAccess access, int x, int y, int z) {
            int red = 0;
            int green = 0;
            int blue = 0;

            for (int xx = -1; xx <= 1; ++xx) {
                for (int zz = -1; zz <= 1; ++zz) {
                    BiomeGenBase biome = access.getBiomeGenForCoords(x + xx, z + zz);
                    int mult = getColor(biome.getFloatTemperature(), biome.getFloatRainfall());
                    red += (mult & 0xFF0000) >> 16;
                    green += (mult & 0x00FF00) >> 8;
                    blue += (mult & 0x0000FF);
                }
            }
            return (((red / 9) & 0xFF) << 16) | (((green / 9) & 0xFF) << 8) | ((blue / 9) & 0xFF);
        }
    }

    
    @SideOnly(Side.CLIENT)
    private transient Icon[][] icons_by_meta;

    private transient Subblock subblock_by_meta[];
    private transient int fireSpreadSpeed_by_meta[] = null;
    private transient int flamability_by_meta[] = null;
    private transient int lightValue_by_meta[] = null;
    private transient int lightOpacity_by_meta[] = null;
    private transient int lightValueInt;
    private transient ColorMultHandler colorMultHandler;
    private transient ColorMultHandler colorMultHandlerByMeta[];
    
    private static final Map<String, Material> materialTable = new HashMap<String, Material>();
    private static final Map<String, StepSound> stepSoundTable = new HashMap<String, StepSound>();
    private static final Map<String, CreativeTabs> tabTable = new HashMap<String, CreativeTabs>();
    private static final Map<String, WesterosBlockFactory> typeTable = new HashMap<String, WesterosBlockFactory>();
    private static final Map<String, ColorMultHandler> colorMultTable = new HashMap<String, ColorMultHandler>();
    
    public Block createBlock(int idx) {
        WesterosBlockFactory bf = typeTable.get(blockType);
        if (bf == null) {
            WesterosBlocks.log.severe(String.format("Invalid blockType '%s' in block '%s'", blockType, blockName));
            return null;
        }
        return bf.buildBlockClass(idx, this);
    }
    
    public Material getMaterial() {
        Material m = materialTable.get(material);
        if (m == null) {
            WesterosBlocks.log.warning(String.format("Invalid material '%s' in block '%s'", material, blockName));
            return Material.rock;
        }
        return m;
    }
    
    public StepSound getStepSound() {
        StepSound ss = stepSoundTable.get(stepSound);
        if (ss == null) {
            WesterosBlocks.log.warning(String.format("Invalid step sound '%s' in block '%s'", stepSound, blockName));
            return Block.soundStoneFootstep;
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

    private void initMeta() {
        subblock_by_meta = new Subblock[16];
        lightValueInt = (int)(15.0F * lightValue);
        this.colorMultHandler = getColorHandler(this.colorMult);
        if (this.colorMultHandler == null) {
            WesterosBlocks.log.warning(String.format("Invalid colorMult '%s' in block '%s'", this.colorMult, blockName));
            this.colorMultHandler = getColorHandler("#FFFFFF");
        }
        if (subBlocks != null) {
            for (Subblock sb : subBlocks) {
                subblock_by_meta[sb.meta] = sb;
                if (sb.fireSpreadSpeed != DEF_INT) {
                    if (fireSpreadSpeed_by_meta == null) {
                        fireSpreadSpeed_by_meta = new int[16];
                        if (this.fireSpreadSpeed != DEF_INT) {
                            Arrays.fill(fireSpreadSpeed_by_meta, this.fireSpreadSpeed);
                        }
                    }
                    fireSpreadSpeed_by_meta[sb.meta] = sb.fireSpreadSpeed; 
                }
                if (sb.flamability != DEF_INT) {
                    if (flamability_by_meta == null) {
                        flamability_by_meta = new int[16];
                        if (this.flamability != DEF_INT) {
                            Arrays.fill(flamability_by_meta, this.flamability);
                        }
                    }
                    flamability_by_meta[sb.meta] = sb.flamability; 
                }
                if (sb.lightValue != DEF_FLOAT) {
                    if (lightValue_by_meta == null) {
                        lightValue_by_meta = new int[16];
                        if (this.lightValue != DEF_FLOAT) {
                            Arrays.fill(lightValue_by_meta, (int)(15.0F * this.lightValue));
                        }
                    }
                    lightValue_by_meta[sb.meta] = (int)(15.0F * sb.lightValue); 
                }
                if (sb.lightOpacity != DEF_INT) {
                    if (lightOpacity_by_meta == null) {
                        lightOpacity_by_meta = new int[16];
                        if (this.lightOpacity != DEF_INT) {
                            Arrays.fill(lightOpacity_by_meta, this.lightOpacity);
                        }
                    }
                    lightOpacity_by_meta[sb.meta] = sb.lightOpacity; 
                }
                if (sb.colorMult != null) {
                    if (this.colorMultHandlerByMeta == null) {
                        this.colorMultHandlerByMeta = new ColorMultHandler[16];
                        Arrays.fill(this.colorMultHandlerByMeta, this.colorMultHandler);
                    }
                    this.colorMultHandlerByMeta[sb.meta] = getColorHandler(sb.colorMult);
                    if (this.colorMultHandlerByMeta[sb.meta] == null) {
                        WesterosBlocks.log.warning(String.format("Invalid colorMult '%s' in block '%s'", sb.colorMult, blockName));
                        this.colorMultHandlerByMeta[sb.meta] = this.colorMultHandler;
                    }
                }
            }
        }
    }
    
    private Subblock getByMeta(int meta) {
        if (subblock_by_meta == null) {
            initMeta();
        }
        return subblock_by_meta[meta];
    }

    // Do standard constructor settings for given block class
    public void doStandardContructorSettings(Block blk) {
        if (this.hardness != DEF_FLOAT) {
            blk.setHardness(this.hardness);
        }
        if (this.lightOpacity != DEF_INT) {
            blk.setLightOpacity(this.lightOpacity);
        }
        if (this.resistance != DEF_FLOAT) {
            blk.setResistance(this.resistance);
        }
        if (this.lightValue != DEF_FLOAT) {
            blk.setLightValue(this.lightValue);
        }
        blk.setUnlocalizedName(this.blockName);
        if (this.stepSound != null) {
            blk.setStepSound(this.getStepSound());
        }
        if ((this.fireSpreadSpeed > 0) || (this.flamability > 0)) {
            Block.setBurnProperties(this.blockID, this.fireSpreadSpeed, this.flamability);
        }
        if (creativeTab != null) {
            blk.setCreativeTab(getCreativeTab());
        }
        if (boundingBox != null) {
            blk.setBlockBounds(boundingBox.xMin, boundingBox.yMin, boundingBox.zMin, boundingBox.xMax, boundingBox.yMax, boundingBox.zMax);
        }
    }
    // Do standard initialize actions
    public void doStandardInitializeActions(Block blk) {
        // Register any harvest levels
        if (harvestLevel != null) { // Do overall first
            for (HarvestLevel hl : harvestLevel) {
                MinecraftForge.setBlockHarvestLevel(blk, hl.tool, hl.level);
            }
        }
        // And do any meta-specific overrides second
        if (this.subBlocks != null) {
            for (Subblock sb : this.subBlocks) {
                if (sb.harvestLevel != null) { // Do overall first
                    for (HarvestLevel hl : sb.harvestLevel) {
                        MinecraftForge.setBlockHarvestLevel(blk, hl.tool, hl.level);
                    }
                }
            }
        }
    }
    
    // Do standard register actions
    public void doStandardRegisterActions(Block blk, Class<? extends ItemBlock> itmclass) {
        // Register the block
        if (itmclass != null)
            GameRegistry.registerBlock(blk, itmclass, this.blockName);
        else
            GameRegistry.registerBlock(blk, this.blockName);
        // And register strings for each item block
        if ((this.subBlocks != null) && (this.subBlocks.size() > 0)) {
            for (Subblock sb : this.subBlocks) {
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

    
    @SideOnly(Side.CLIENT)
    public void doStandardRegisterIcons(IconRegister ir) {
        if (subblock_by_meta == null) {
            initMeta();
        }
        icons_by_meta = new Icon[16][];
        if (subBlocks != null) {
            HashMap<String, Icon> map = new HashMap<String, Icon>();
            for (Subblock sb : subBlocks) {
                if (sb.textures == null) {
                    WesterosBlocks.log.warning(String.format("No textures for subblock '%d' of block '%s'", sb.meta, this.blockName));
                    sb.textures = Collections.singletonList("INVALID_" + blockName + "_" + sb.meta);
                }
                icons_by_meta[sb.meta] = new Icon[sb.textures.size()];
                for (int i = 0; i < sb.textures.size(); i++) {
                    String txt = sb.textures.get(i);
                    if (txt.indexOf(':') < 0) {
                        txt = "westerosblocks:" + txt;
                    }
                    Icon ico = map.get(txt);
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
    public Icon doStandardIconGet(int side, int meta) {
        if (icons_by_meta == null) {
            return null;
        }
        if (meta >= icons_by_meta.length) {
            meta = 0;
        }
        Icon[] ico = icons_by_meta[meta];
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

    public int getFlammability(IBlockAccess world, int x, int y, int z, int metadata, ForgeDirection face) {
        if (flamability_by_meta != null) {
            return flamability_by_meta[metadata];
        }
        return this.flamability;
    }
    
    public int getFireSpreadSpeed(World world, int x, int y, int z, int metadata, ForgeDirection face) {
        if (fireSpreadSpeed_by_meta != null) {
            return fireSpreadSpeed_by_meta[metadata];
        }
        return this.fireSpreadSpeed;
    }
    
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        if (this.lightValue_by_meta != null) {
            return this.lightValue_by_meta[world.getBlockMetadata(x,  y,  z)];
        }
        return this.lightValueInt;
    }
    
    public int getLightOpacity(World world, int x, int y, int z) {
        if (this.lightOpacity_by_meta != null) {
            return this.lightOpacity_by_meta[world.getBlockMetadata(x,  y,  z)];
        }
        return Block.lightOpacity[this.blockID];
    }
    
    public int getBlockColor() {
        return this.colorMultHandler.getBlockColor();
    }
    
    public int getRenderColor(int meta) {
        if (this.colorMultHandlerByMeta != null) {
            return this.colorMultHandlerByMeta[meta].getBlockColor();
        }
        return this.colorMultHandler.getBlockColor();
    }
    
    public int colorMultiplier(IBlockAccess access, int x, int y, int z, int metaMask) {
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
    private BoundingBox getBoundingBox(int meta) {
        BoundingBox bb = this.boundingBox;
        Subblock sb = getByMeta(meta);
        if ((sb != null) && (sb.boundingBox != null)) {
            bb = sb.boundingBox;
        }
        return bb;
    }
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        BoundingBox bb = getBoundingBox(meta);
        if (bb != null) {
            return AxisAlignedBB.getAABBPool().getAABB((double)x + bb.xMin, (double)y + bb.yMin, (double)z + bb.zMin, (double)x + bb.xMax, (double)y + bb.yMax, (double)z + bb.zMax);
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
        Subblock sb = getByMeta(meta);
        if ((sb != null) && (sb.type != null)) {
            return sb.type;
        }
        return this.type;
    }

    public EnumPlantType getPlantType(int meta) {
        EnumPlantType pt = EnumPlantType.Plains;
        String t = getType(meta);
        if (t != null) {
            pt = EnumPlantType.valueOf(t);
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
        BitSet ids = new BitSet();
        BitSet metas = new BitSet();
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
            if (ids.get(def.blockID)) {    // If alreay defined
                WesterosBlocks.log.severe(String.format("Block '%s' - blockID duplicated", def.blockName));
                return false;
            }
            ids.set(def.blockID);
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
    
    public Icon getItemIcon(int meta) {
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
        materialTable.put("rock",  Material.rock);
        materialTable.put("iron", Material.iron);
        materialTable.put("anvil", Material.anvil);
        materialTable.put("water", Material.water);
        materialTable.put("lava", Material.lava);
        materialTable.put("leaves", Material.leaves);
        materialTable.put("plants", Material.plants);
        materialTable.put("vine", Material.vine);
        materialTable.put("sponge", Material.sponge);
        materialTable.put("cloth", Material.cloth);
        materialTable.put("fire", Material.fire);
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
        materialTable.put("pumpkin", Material.pumpkin);
        materialTable.put("dragonEgg", Material.dragonEgg);
        materialTable.put("portal", Material.portal);
        materialTable.put("cake", Material.cake);
        materialTable.put("web", Material.web);
        materialTable.put("piston", Material.piston);

        stepSoundTable.put("powder", Block.soundPowderFootstep);
        stepSoundTable.put("wood", Block.soundWoodFootstep);
        stepSoundTable.put("gravel", Block.soundGravelFootstep);
        stepSoundTable.put("grass", Block.soundGrassFootstep);
        stepSoundTable.put("stone", Block.soundStoneFootstep);
        stepSoundTable.put("metal", Block.soundMetalFootstep);
        stepSoundTable.put("glass", Block.soundGlassFootstep);
        stepSoundTable.put("cloth", Block.soundClothFootstep);
        stepSoundTable.put("sand", Block.soundSandFootstep);
        stepSoundTable.put("snow", Block.soundSnowFootstep);
        stepSoundTable.put("ladder", Block.soundLadderFootstep);
        stepSoundTable.put("anvil", Block.soundAnvilFootstep);
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
        typeTable.put("fence", new WCFenceBlock.Factory());
        typeTable.put("wall", new WCWallBlock.Factory());
        typeTable.put("pane", new WCPaneBlock.Factory());
        typeTable.put("sand", new WCSandBlock.Factory());
        typeTable.put("cuboid", new WCCuboidBlock.Factory());
        typeTable.put("torch", new WCTorchBlock.Factory());
        typeTable.put("leaves", new WCLeavesBlock.Factory());
        typeTable.put("door", new WCDoorBlock.Factory());

        // Standard color multipliers
        colorMultTable.put("#FFFFFF", new ColorMultHandler());
        colorMultTable.put("water", new WaterColorMultHandler());
        colorMultTable.put("foliage", new FoliageColorMultHandler());
        colorMultTable.put("grass", new GrassColorMultHandler());
        colorMultTable.put("pine", new PineColorMultHandler());
        colorMultTable.put("birch", new BirchColorMultHandler());
        colorMultTable.put("basic", new BasicColorMultHandler());
        colorMultTable.put("lily", new ColorMultHandler(2129968));
     }
    // Get color muliplier
    public ColorMultHandler getColorHandler(String hnd) {
        ColorMultHandler cmh = colorMultTable.get(hnd);
        if (cmh == null) {
            hnd = hnd.toUpperCase();
            cmh = colorMultTable.get(hnd);
        }
        if (cmh == null) { 
            // See if color code
            if ((hnd.length() == 7) && (hnd.charAt(0) == '#')) {
                try {
                    cmh = new ColorMultHandler(Integer.parseInt(hnd.substring(1), 16));
                    colorMultTable.put(hnd, cmh);
                } catch (NumberFormatException nfx) {
                }
            }
            // See if resource
            else {
                int idx = hnd.indexOf(':');
                if (idx < 0) {
                    hnd = "westeroscraft:" + hnd;
                    cmh = colorMultTable.get(hnd);
                }
                if (cmh == null) {
                    cmh = new CustomColorMultHandler(hnd, blockName);
                    colorMultTable.put(hnd, cmh);
                }
            }
        }
        return cmh;
    }
    // Register custom step sound
    public static void registerStepSound(WesterosBlockStepSound ss) {
        WCStepSound stepsound = new WCStepSound(ss);
        stepSoundTable.put(ss.name, stepsound);
    }
}
