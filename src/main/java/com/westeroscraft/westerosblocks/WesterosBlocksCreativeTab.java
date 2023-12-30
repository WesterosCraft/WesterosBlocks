package com.westeroscraft.westerosblocks;

import com.mojang.blaze3d.platform.Lighting;
import com.westeroscraft.westerosblocks.modelexport.ModelExport;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class  WesterosBlocksCreativeTab extends CreativeModeTab {

    public static final CreativeModeTab tabWesterosCobbleStone = new  WesterosBlocksCreativeTab("WesterosCobbleStone", "§fCobble and Stone", "river_cobble"/*, CreativeModeTab.TAB_BUILDING_BLOCKS*/);
    public static final CreativeModeTab tabWesterosBrick = new  WesterosBlocksCreativeTab("WesterosBrick", "§fBrick", "sandy_pink_large_brick");
    public static final CreativeModeTab tabWesterosMarblePlaster = new  WesterosBlocksCreativeTab("WesterosMarblePlaster", "§fMarble and Plaster", "marble_pillar");
    public static final CreativeModeTab tabWesterosTimberFrame = new  WesterosBlocksCreativeTab("WesterosTimberFrame", "§fTimber Frame", "timber_oak_reach_brick_crosshatch");
    public static final CreativeModeTab tabWesterosSandstone = new  WesterosBlocksCreativeTab("WesterosSandstone", "§fSandstone", "vivid_sandstone");
    public static final CreativeModeTab tabWesterosRoofing = new  WesterosBlocksCreativeTab("WesterosRoofing", "§fRoofing", "orange_slate");
    public static final CreativeModeTab tabWesterosWoodPlanks = new  WesterosBlocksCreativeTab("WesterosWoodPlanks", "§fWood and Planks", "oak_vertical_planks");
    public static final CreativeModeTab tabWesterosPanellingCarvings = new  WesterosBlocksCreativeTab("WesterosPanellingCarvings", "§fPanelling and Carvings", "dragon_carving");
    public static final CreativeModeTab tabWesterosMetal = new  WesterosBlocksCreativeTab("WesterosMetal", "§fMetal", "oxidized_bronze_block");
    public static final CreativeModeTab tabWesterosWindowsGlass = new  WesterosBlocksCreativeTab("WesterosWindowsGlass", "§fWindows and Glass", "coloured_sept_window");
    public static final CreativeModeTab tabWesterosClothFibers = new  WesterosBlocksCreativeTab("WesterosClothFibers", "§fCloth and Fibers", "fancy_blue_carpet");
    public static final CreativeModeTab tabWesterosFurniture = new  WesterosBlocksCreativeTab("WesterosFurniture", "§fFurniture", "table");
    public static final CreativeModeTab tabWesterosLighting = new  WesterosBlocksCreativeTab("WesterosLighting", "§fLighting", "red_lantern2");
    public static final CreativeModeTab tabWesterosToolBlocks = new  WesterosBlocksCreativeTab("WesterosToolBlocks", "§fTool Blocks", "piston_extension");
    public static final CreativeModeTab tabWesterosFoodBlocks = new  WesterosBlocksCreativeTab("WesterosFoodBlocks", "§fFood Blocks", "squash");
    public static final CreativeModeTab tabWesterosDecor = new  WesterosBlocksCreativeTab("WesterosDecor", "§fDecor", "dead_hare");
    public static final CreativeModeTab tabWesterosTerrainSets = new  WesterosBlocksCreativeTab("WesterosTerrainSets", "§fTerrain Sets", "terrainset_eastern_islands");
    public static final CreativeModeTab tabWesterosGrassDirt = new  WesterosBlocksCreativeTab("WesterosGrassDirt", "§fGrass and Dirt", "classic_grass_block");
    public static final CreativeModeTab tabWesterosSandGravel = new  WesterosBlocksCreativeTab("WesterosSandGravel", "§fSand and Gravel", "sand_skeleton");
    public static final CreativeModeTab tabWesterosLogs = new  WesterosBlocksCreativeTab("WesterosLogs", "§fLogs", "weirwood_face_4");
    public static final CreativeModeTab tabWesterosLeaves = new  WesterosBlocksCreativeTab("WesterosLeaves", "§fLeaves", "snowy_weirwood_leaves");
    public static final CreativeModeTab tabWesterosGrassesShrubs = new  WesterosBlocksCreativeTab("WesterosGrassesShrubs", "§fGrasses and Shrubs", "yellow_rose_bush");
    public static final CreativeModeTab tabWesterosFlowers = new  WesterosBlocksCreativeTab("WesterosFlowers", "§fFlowers", "blue_bells");
    public static final CreativeModeTab tabWesterosCropsHerbs = new  WesterosBlocksCreativeTab("WesterosCropsHerbs", "§fCrops and Herbs", "strawberry_bush");
    public static final CreativeModeTab tabWesterosWaterAir = new  WesterosBlocksCreativeTab("WesterosWaterAir", "§fWater and Air", "falling_water_block_one");
    public static final CreativeModeTab tabWesterosMisc = new  WesterosBlocksCreativeTab("WesterosMisc", "§fMiscellaneous", "piled_bones");
    public static final CreativeModeTab tabWesterosUtility = new  WesterosBlocksCreativeTab("WesterosUtility", "§fUtility", "approval_utility_block");
    public static final CreativeModeTab tabWesterosTest = new  WesterosBlocksCreativeTab("WesterosTest", "§fTest", "test_block");
    public static final CreativeModeTab tabWesterosSounds = new  WesterosBlocksCreativeTab("WesterosSounds", "§fSounds", "tavern_small");
    public static final CreativeModeTab tabWesterosDoNotUse = new  WesterosBlocksCreativeTab("WesterosDoNotUse", "§fDo Not Use", "note_utility_block");


    public static void init() {
        
    }
    
    private String type;
    private Block itm = null;
    public  WesterosBlocksCreativeTab(String id, String label, String type) {
    	this(id, label, type, null);
    }
    public  WesterosBlocksCreativeTab(String id, String label, String type, CreativeModeTab insertBefore) {
        super(id);
        this.type = type;
        WesterosBlockDef.addCreativeTab(id,  this);
        if (insertBefore != null) {	// Tab to be inserted before?
        	for (int i = 0; i < CreativeModeTab.TABS.length; i++) {
        		if (CreativeModeTab.TABS[i] == insertBefore) {
                	// We were inserted last, so shift everyone forward from index
        			for (int j = CreativeModeTab.TABS.length - 2; j >= i; j--) {
        				CreativeModeTab.TABS[j+1] = CreativeModeTab.TABS[j];
        				CreativeModeTab.TABS[j+1].id = j+1;
        			}
        			CreativeModeTab.TABS[i] = this;
        			CreativeModeTab.TABS[i].id = i;
        			break;
        		}
        	}
        }
        // Add to our NLS export
        ModelExport.addNLSString("itemGroup." + id, label);
    }
    
    @Override
    public ItemStack makeIcon()
    {
        if (itm == null) {
            itm = WesterosBlocks.findBlockByName(this.type);
            if (itm == null) {
                itm = WesterosBlocks.customBlocks[0];
            }
        }
        return new ItemStack(itm);
    }
}
