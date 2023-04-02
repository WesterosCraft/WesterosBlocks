package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockStateRecord;

import net.minecraft.world.level.block.Block;

public class CuboidNEBlockModelExport extends CuboidBlockModelExport {
    
    public CuboidNEBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
        super(blk, def, dest);
    }
    
    @Override
    public void doBlockStateExport() throws IOException {
        StateObject so = new StateObject();
    	for (WesterosBlockStateRecord sr : def.states) {
    		boolean justBase = sr.stateID == null;
	        // Loop over the random sets we've got
	        for (int setidx = 0; setidx < sr.getRandomTextureSetCount(); setidx++) {
	        	// East is base model
	        	Variant var = new Variant();
	        	var.model = modelFileName(justBase ? "base" : sr.stateID, setidx);
	        	so.addVariant("facing=east", var, null);
	        }
	        // North is 90 degree rotate
	        for (int setidx = 0; setidx < sr.getRandomTextureSetCount(); setidx++) {
	        	Variant var = new Variant();
	        	var.model = modelFileName(justBase ? "base" : sr.stateID, setidx);
	        	var.y = 90;
	        	so.addVariant("facing=north", var, null);
	        }
    	}
        this.writeBlockStateFile(def.blockName, so);
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
    	for (String facing : FACINGNE) {
    		oldstate.put("facing", facing);
    		newstate.put("facing", facing);
    		addWorldConverterRecord(oldID, oldstate, def.getBlockName(), newstate);
    	}
    }

}
