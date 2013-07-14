package com.westeroscraft.westerosblocks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.westeroscraft.westerosblocks.blocks.WCSolidBlock;
import com.westeroscraft.westerosblocks.blocks.WCStairBlock;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.Block;
import net.minecraft.block.StepSound;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraftforge.common.MinecraftForge;

//
// Template for block configuration data (populated using GSON)
//
public class WesterosBlockDef {
    public String blockName;                // Locally unique block name
    public int blockID;                     // Block ID number (default)
    public String blockType = "solid";      // Block type ('solid', 'liquid', 'plant', 'log', 'stairs', etc)
    public float hardness = 1.5F;           // Block hardness
    public String stepSound = "stone";      // Step sound (powder, wood, gravel, grass, stone, metal, glass, cloth, sand, snow, ladder, anvil)
    public String material = "rock";        // Generic material (ai, grass, ground, wood, rock, iron, anvil, water, lava, leaves, plants, vine, sponge, etc)
    public float resistance = 10.0F;        // Explosion resistance
    public int lightOpacity = 255;          // Light opacity
    public List<HarvestLevel> harvestLevel = null;  // List of harvest levels
    public int harvestItemID = 0;           // Harvest item ID
    public int fireSpreadSpeed = 0;         // Fire spread speed
    public int flamability = 0;             // Flamability
    public String creativeTab = null;       // Creative tab for items
    public float lightValue = 0.0F;         // Emitted light level (0.0-1.0)
    public List<Subblock> subBlocks = null; // Subblocks
    public String modelBlockName = null;    // Name of solid block modelled from (used by 'stairs' type) - can be number of block ID
    public int modelBlockMeta = -1;         // Metadata of model block to use 
    
    public static class HarvestLevel {
        public String tool;
        public int level;
    }
    
    public static class Subblock {
        public int meta;        // Meta value for subblock (base value, if more than one associated with block type)
        public String label;    // Label for item associated with block
        public List<HarvestLevel> harvestLevel; // List of harvest levels
        public int harvestItemID = -1;           // Harvest item ID (-1=use block level)
        public int fireSpreadSpeed = -1;         // Fire spread speed (-1=use block level)
        public int flamability = -1;             // Flamability (-1=use block level)
        public List<String> textures = null;    // List of textures
    }

    @SideOnly(Side.CLIENT)
    private transient Icon[][] icons_by_meta;
    
    private static final Map<String, Material> materialTable = new HashMap<String, Material>();
    private static final Map<String, StepSound> stepSoundTable = new HashMap<String, StepSound>();
    private static final Map<String, CreativeTabs> tabTable = new HashMap<String, CreativeTabs>();
    private static final Map<String, WesterosBlockFactory> typeTable = new HashMap<String, WesterosBlockFactory>();
    
    public Block createBlock(int idx) {
        WesterosBlockFactory bf = typeTable.get(blockType);
        if (bf == null) {
            FMLLog.severe("Invalid blockType '%s' in block '%s'", blockType, blockName);
            return null;
        }
        return bf.buildBlockClass(idx, this);
    }
    
    public Material getMaterial() {
        Material m = materialTable.get(material);
        if (m == null) {
            FMLLog.warning("Invalid material '%s' in block '%s'", material, blockName);
            return Material.rock;
        }
        return m;
    }
    
    public StepSound getStepSound() {
        StepSound ss = stepSoundTable.get(stepSound);
        if (ss == null) {
            FMLLog.warning("Invalid material '%s' in block '%s'", stepSound, blockName);
            return Block.soundStoneFootstep;
        }
        return ss;
    }
    
    public CreativeTabs getCreativeTab() {
        CreativeTabs ct = tabTable.get(creativeTab);
        if (ct == null) {
            FMLLog.warning("Invalid tab name '%s' in block '%s'", creativeTab, blockName);
            ct = WesterosBlocksCreativeTab.tabWesterosBlocks;
        }
        return ct;
    }
    
    // Do standard constructor settings for given block class
    public void doStandardContructorSettings(Block blk) {
        blk.setHardness(this.hardness);
        blk.setLightOpacity(this.lightOpacity);
        blk.setResistance(this.resistance);
        blk.setLightValue(this.lightValue);
        blk.setUnlocalizedName(this.blockName);
        blk.setStepSound(this.getStepSound());
        Block.setBurnProperties(this.blockID, this.fireSpreadSpeed, this.flamability);
        if (creativeTab != null) {
            blk.setCreativeTab(getCreativeTab());
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
    }

    
    @SideOnly(Side.CLIENT)
    public void doStandardRegisterIcons(IconRegister ir) {
        icons_by_meta = new Icon[16][];
        if (subBlocks != null) {
            HashMap<String, Icon> map = new HashMap<String, Icon>();
            for (Subblock sb : subBlocks) {
                if (sb.textures == null) {
                    FMLLog.warning("No textures for subblock '%d' of block '%s'", sb.meta, this.blockName);
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

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void getStandardCreativeItems(Block blk, ArrayList itemList) {
        if (subBlocks != null) {
            for (Subblock sb : subBlocks) {
                itemList.add(new ItemStack(blk, 1, sb.meta));
            }
        }
    }

    public static void addCreativeTab(String name, CreativeTabs tab) {
        tabTable.put(name,  tab);
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
     }
}
