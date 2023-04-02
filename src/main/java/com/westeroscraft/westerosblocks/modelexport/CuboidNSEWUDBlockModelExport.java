package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlockStateRecord;
import com.westeroscraft.westerosblocks.WesterosBlocks;

import net.minecraft.world.level.block.Block;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CuboidNSEWUDBlockModelExport extends CuboidBlockModelExport {
    
    public CuboidNSEWUDBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
        super(blk, def, dest);
    }
    
    @Override
    public void doBlockStateExport() throws IOException {
        StateObject so = new StateObject();

    	for (WesterosBlockStateRecord sr : def.states) {
    		boolean justBase = sr.stateID == null;
	
	        for (int setidx = 0; setidx < sr.getRandomTextureSetCount(); setidx++) {
	        	Variant var = new Variant();
	        	var.model = modelFileName(justBase ? "base" : sr.stateID, setidx);
	        	so.addVariant("facing=north", var, null);
	        }
	        
	        for (int setidx = 0; setidx < sr.getRandomTextureSetCount(); setidx++) {
	        	Variant var = new Variant();
	        	var.model = modelFileName(justBase ? "base" : sr.stateID, setidx);
	            var.y = 90;
	        	so.addVariant("facing=east", var, null);
	        }
	        
	        for (int setidx = 0; setidx < sr.getRandomTextureSetCount(); setidx++) {
	        	Variant var = new Variant();
	        	var.model = modelFileName(justBase ? "base" : sr.stateID, setidx);
	        	var.y = 180;
	        	so.addVariant("facing=south", var, null);
	        }
	        
	        for (int setidx = 0; setidx < sr.getRandomTextureSetCount(); setidx++) {
	        	Variant var = new Variant();
	        	var.model = modelFileName(justBase ? "base" : sr.stateID, setidx);
	        	var.y = 270;
	        	so.addVariant("facing=west", var, null);
	        }
	        
	        for (int setidx = 0; setidx < sr.getRandomTextureSetCount(); setidx++) {
	        	Variant var = new Variant();
	        	var.model = modelFileName(justBase ? "base" : sr.stateID, setidx);
	        	var.x = 270;
	        	so.addVariant("facing=up", var, null);
	        }
	        
	        for (int setidx = 0; setidx < sr.getRandomTextureSetCount(); setidx++) {
	        	Variant var = new Variant();
	        	var.model = modelFileName(justBase ? "base" : sr.stateID, setidx);
	        	var.x = 90;
	        	so.addVariant("facing=down", var, null);
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
    	for (String facing : ALLFACING) {
    		oldstate.put("facing", facing);
    		newstate.put("facing", facing);
    		addWorldConverterRecord(oldID, oldstate, def.getBlockName(), newstate);
    	}
    }
}
