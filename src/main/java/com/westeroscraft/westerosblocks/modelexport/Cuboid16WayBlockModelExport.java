package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockStateRecord;

import net.minecraft.world.level.block.Block;
import java.util.HashMap;

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
    		WesterosBlockStateRecord st = def.states.get(stidx);
        	String n = (st.stateID == null) ? "base" : st.stateID;
	        // For each direction
	        for (int rotation = 0; rotation < 16; rotation++) {
		        // Loop over the random sets we've got
		        for (int setidx = 0; setidx < st.getRandomTextureSetCount(); setidx++) {
		        	Variant var = new Variant();
		        	var.model = modelFileName(n + modRot[rotation % 4], setidx);
		        	var.y = 90 * (((rotation + 1) % 16) / 4);
		        	so.addVariant("rotation=" + rotation, var, null);
		        }
	        }
    	}
        this.writeBlockStateFile(def.blockName, so);
    }
    @Override
    public void doModelExports() throws IOException {
        boolean isTinted = def.isTinted();
        // Export if not set to custom model
        if (!def.isCustomModel()) {
        	int stcnt = def.states.size();
        	for (int stidx = 0; stidx < stcnt; stidx++) {
        		WesterosBlockStateRecord st = def.states.get(stidx);
	            // Loop over the random sets we've got for base model (and for each 22 degree model
	            for (int setidx = 0; setidx < def.getRandomTextureSetCount(); setidx++) {
	            	String n = (st.stateID == null) ? "base" : st.stateID;
	            	doCuboidModel(getModelName(n + modRot[0], setidx), st.isTinted(), setidx, null, st, stidx);
	            	doCuboidModel(getModelName(n + modRot[1], setidx), st.isTinted(), setidx, -22.5F, st, stidx);
	            	doCuboidModel(getModelName(n + modRot[2], setidx), st.isTinted(), setidx, -45F, st, stidx);
	            	doCuboidModel(getModelName(n + modRot[3], setidx), st.isTinted(), setidx, 22.5F, st, stidx);
	            }
        	}
        }
        // Build simple item model that refers to block model
        ModelObject mo = new ModelObject();
    	String n = (def.states.get(0).stateID == null) ? "base" : def.states.get(0).stateID;
        mo.parent = modelFileName(n, 0);
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
    	String oldVariant = def.getLegacyBlockVariant();
    	addWorldConverterComment(def.legacyBlockID + "(" + def.label + ")");
    	// BUild old variant map
    	HashMap<String, String> oldstate = new HashMap<String, String>();
    	HashMap<String, String> newstate = new HashMap<String, String>();
    	oldstate.put("variant", oldVariant);
    	newstate.put("waterlogged", "false");
    	for (int rotation = 0; rotation < 16; rotation++) {
    		oldstate.put("rotation", Integer.toString(rotation));
    		newstate.put("rotation", Integer.toString(rotation));
    		addWorldConverterRecord(oldID, oldstate, def.getBlockName(), newstate);
    	}
    }

}
