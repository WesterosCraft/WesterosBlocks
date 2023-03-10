package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.blocks.WCCuboidHorizBlock;

import net.minecraft.world.level.block.Block;

public class CuboidHorizBlockModelExport extends CuboidBlockModelExport {
    
    public CuboidHorizBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
        super(blk, def, dest);
    }
    
    static String[] modelBaseIDs = { "none", "single", "angle", "straight", "triple", "all" };
    @Override
    public void doModelExports() throws IOException {
        boolean isTinted = def.isTinted();
        // Export if not set to custom model
        if (!def.isCustomModel()) {
        	for (int modidx = 0; modidx < modelBaseIDs.length; modidx++) {
        		// Build model for each random
        		for (int setidx = 0; setidx < def.getRandomTextureSetCount(); setidx++) {
                	WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setidx);
                	ModelObjectCuboid mod = new ModelObjectCuboid();
                	String txt0 = set.getTextureByIndex(0);
                	mod.textures.put("particle", getTextureID(set.getTextureByIndex(0)));
                	int cnt = Math.max(6, set.getTextureCount());
                	String[] textures = new String[cnt];
                	for (int i = 0; i < cnt; i++) {
                		textures[i] = set.getTextureByIndex(i);
                	}
                	doCuboidModel(getModelName(modelBaseIDs[modidx], setidx), isTinted, txt0, textures,
                			def.getCuboidList(modidx), null);
        		}
        	}
        }
        // Build simple item model that refers to block model
        ModelObject mo = new ModelObject();
        mo.parent = modelFileName(modelBaseIDs[0], 0);
        this.writeItemModelFile(def.blockName, mo);
        // Add tint overrides
        if (isTinted) {
            String tintres = def.getBlockColorMapResource();
            if (tintres != null) {
                ModelExport.addTintingOverride(def.blockName, "", tintres);
            }
        }
    }

    @Override
    public void doBlockStateExport() throws IOException {
        StateObject so = new StateObject();
        // Loop over model mappings
        for (int i = 0; i < WCCuboidHorizBlock.modelMap.length; i++) {
        	String varid = "north=" + ((i & WCCuboidHorizBlock.NORTH_BITS) > 0) + 
        			",south=" + ((i & WCCuboidHorizBlock.SOUTH_BITS) > 0) + 
        			",east=" + ((i & WCCuboidHorizBlock.EAST_BITS) > 0) + 
        			",west=" + ((i & WCCuboidHorizBlock.WEST_BITS) > 0);
        	int modidx = WCCuboidHorizBlock.modelMap[i].modelIdx;
        	
            // Loop over the random sets we've got
            for (int setidx = 0; setidx < def.getRandomTextureSetCount(); setidx++) {
            	WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setidx);
            	Variant var = new Variant();
            	var.model = modelFileName(modelBaseIDs[modidx], setidx);
            	var.y = WCCuboidHorizBlock.modelMap[i].rot.getRotY();
            	var.uvlock = true;	// With rotations, we want uvlock
            	so.addVariant(varid, var, set.condIDs);
            }        	
        }
        this.writeBlockStateFile(def.blockName, so);
    }
    @Override
    public void doWorldConverterMigrate() throws IOException {
    	String oldID = def.getLegacyBlockName();
    	if (oldID == null) return;
    	String oldVariant = def.getLegacyBlockVariant();
    	addWorldConverterComment(def.legacyBlockID + "(" + def.label + ")");
    	// BUild old variant map
    	HashMap<String, String> oldstate = new HashMap<String, String>();
    	HashMap<String, String> newstate = new HashMap<String, String>();
    	oldstate.put("variant", oldVariant);
    	newstate.put("waterlogged", "false");
    	newstate.put("north", "false");
    	newstate.put("south", "false");
    	newstate.put("east", "false");
    	newstate.put("west", "false");
    }
}
