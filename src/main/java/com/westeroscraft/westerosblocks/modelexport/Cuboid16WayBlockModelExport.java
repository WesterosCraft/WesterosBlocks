package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockStateRecord;

import net.minecraft.world.level.block.Block;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Cuboid16WayBlockModelExport extends CuboidBlockModelExport {
    
    public Cuboid16WayBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
        super(blk, def, dest);
    }
    
    private static final String modRot[] = { "", "_rotn22", "_rotn45", "_rot22" };
    @Override
    public void doBlockStateExport() throws IOException {
        StateObject so = new StateObject();        
    	int stcnt = def.states.size();
    	// For each state
    	for (int stidx = 0; stidx < stcnt; stidx++) {
    		WesterosBlockStateRecord sr = def.states.get(stidx);
    		boolean justBase = sr.stateID == null;
    		Set<String> stateIDs = justBase ? null : Collections.singleton(sr.stateID);
    		String fname = justBase ? "base" : sr.stateID;
	        // For each direction
	        for (int rotation = 0; rotation < 16; rotation++) {
		        // Loop over the random sets we've got
		        for (int setidx = 0; setidx < sr.getRandomTextureSetCount(); setidx++) {
		        	Variant var = new Variant();
		        	var.model = modelFileName(fname + modRot[rotation % 4], setidx, sr.isCustomModel());
	            	int rot = (90 * (((rotation + 1) % 16) / 4) + sr.rotYOffset) % 360;
	            	if (rot > 0) var.y = rot;
		        	so.addVariant("rotation=" + rotation, var, stateIDs);
		        }
	        }
    	}
        this.writeBlockStateFile(def.blockName, so);
    }
    @Override
    public void doModelExports() throws IOException {
        boolean isTinted = def.isTinted();
    	int stcnt = def.states.size();
    	WesterosBlockStateRecord sr;
    	for (int stidx = 0; stidx < stcnt; stidx++) {
    		sr = def.states.get(stidx);
            // Export if not set to custom model
    		if (sr.isCustomModel()) continue;
    		boolean justBase = sr.stateID == null;
    		String fname = justBase ? "base" : sr.stateID;
            // Loop over the random sets we've got for base model (and for each 22 degree model
            for (int setidx = 0; setidx < def.getRandomTextureSetCount(); setidx++) {
            	doCuboidModel(getModelName(fname + modRot[0], setidx), sr.isTinted(), setidx, null, sr, stidx);
            	doCuboidModel(getModelName(fname + modRot[1], setidx), sr.isTinted(), setidx, -22.5F, sr, stidx);
            	doCuboidModel(getModelName(fname + modRot[2], setidx), sr.isTinted(), setidx, -45F, sr, stidx);
            	doCuboidModel(getModelName(fname + modRot[3], setidx), sr.isTinted(), setidx, 22.5F, sr, stidx);
            }
    	}
        // Build simple item model that refers to block model
        ModelObject mo = new ModelObject();
        sr = def.states.get(0);
    	String n = (sr.stateID == null) ? "base" : sr.stateID;
        mo.parent = modelFileName(n, 0, sr.isCustomModel());
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
    public void doWorldConverterMigrate() throws IOException {
    	String oldID = def.getLegacyBlockName();
    	if (oldID == null) return;
    	Map<String, String> oldmap = def.getLegacyBlockMap();
    	addWorldConverterComment(def.legacyBlockID + "(" + def.label + ")");
    	// BUild old variant map
    	HashMap<String, String> oldstate = new HashMap<String, String>();
    	HashMap<String, String> newstate = new HashMap<String, String>();
    	if (oldmap != null) {
    		for (String k : oldmap.keySet()) {
        		oldstate.put(k, oldmap.get(k));    			
    		}
    	}
    	newstate.put("waterlogged", "false");
    	for (int rotation = 0; rotation < 16; rotation++) {
    		oldstate.put("rotation", Integer.toString(rotation));
    		newstate.put("rotation", Integer.toString(rotation));
    		addWorldConverterRecord(oldID, oldstate, def.getBlockName(), newstate);
    	}
    }

}
