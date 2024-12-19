package com.westerosblocks.needsported.modelexport;

import java.io.File;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.westeroscraft.westerosblocks.WesterosBlockDef;

import net.minecraft.world.level.block.Block;

public class SoundBlockModelExport extends SolidBlockModelExport {

    public SoundBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
        super(blk, def, dest);
    }

    @Override
    public void doWorldConverterMigrate() throws IOException {
    	String oldID = def.getLegacyBlockName();
    	if (oldID == null) return;
    	Map<String, String> oldmap = def.getLegacyBlockMap();
    	addWorldConverterComment(def.legacyBlockID + "(" + def.label + ")");
    	// BUild old variant map
    	Map<String, String> oldstate = new HashMap<String, String>();
    	Map<String, String> newstate = new HashMap<String, String>();
    	if (oldmap != null) {
    		for (String k : oldmap.keySet()) {
        		oldstate.put(k, oldmap.get(k));    			
    		}
    	}
    	newstate.put("powered", "false");
        addWorldConverterRecord(oldID, oldstate, def.getBlockName(), newstate);
    }
}
