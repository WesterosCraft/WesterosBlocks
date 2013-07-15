package com.westeroscraft.westerosblocks;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.westeroscraft.westerosblocks.blocks.WCLogBlock;
import com.westeroscraft.westerosblocks.blocks.WCSolidBlock;
import com.westeroscraft.westerosblocks.blocks.WCStairBlock;

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
    public int fireSpreadSpeed = DEF_INT;   // Fire spread speed
    public int flamability = DEF_INT;       // Flamability
    public String creativeTab = null;       // Creative tab for items
    public float lightValue = DEF_FLOAT;    // Emitted light level (0.0-1.0)
    public List<Subblock> subBlocks = null; // Subblocks
    public String modelBlockName = null;    // Name of solid block modelled from (used by 'stairs' type) - can be number of block ID
    public int modelBlockMeta = DEF_INT;    // Metadata of model block to use 
    
    public static class HarvestLevel {
        public String tool;
        public int level;
    }
    
    public static class Subblock {
        public int meta;        // Meta value for subblock (base value, if more than one associated with block type)
        public String label;    // Label for item associated with block
        public List<HarvestLevel> harvestLevel = null; // List of harvest levels
        public int harvestItemID = DEF_INT;     // Harvest item ID (-1=use block level)
        public int fireSpreadSpeed = DEF_INT;   // Fire spread speed (-1=use block level)
        public int flamability = DEF_INT;       // Flamability (-1=use block level)
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
        if ((this.fireSpreadSpeed != DEF_INT) || (this.flamability != DEF_INT)) {
            Block.setBurnProperties(this.blockID, this.fireSpreadSpeed, this.flamability);
        }
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
     }
}
