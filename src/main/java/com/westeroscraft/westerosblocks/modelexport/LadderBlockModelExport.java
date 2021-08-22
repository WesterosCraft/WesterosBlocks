package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlocks;

import net.minecraft.block.Block;

public class LadderBlockModelExport extends ModelExport {
    // Template objects for Gson export of block models
    public static class ModelObjectLadder {
        public String parent = WesterosBlocks.MOD_ID + ":block/untinted/ladder";    // Use 'ladder' model for single texture
        public Texture textures = new Texture();
    }
    public static class Texture {
        public String ladder;
    }
    public static class ModelObject {
    	public String parent;
    }

    public LadderBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
        super(blk, def, dest);
        addNLSString("block." + WesterosBlocks.MOD_ID + "." + def.blockName, def.label);
    }
    
	static final String faces[] = { "north", "south", "east", "west" };
	static final int[] y = { 0, 180, 90, 270 };

    @Override
    public void doBlockStateExport() throws IOException {
        StateObject so = new StateObject();
    	for (int faceidx = 0; faceidx < 4; faceidx++) {
	    	// Loop over the random sets we've got
	        for (int setidx = 0; setidx < def.getRandomTextureSetCount(); setidx++) {
	        	WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setidx);
            	Variant var = new Variant();
	            var.model = WesterosBlocks.MOD_ID + ":block/generated/" + getModelName("base", setidx);
            	var.weight = set.weight;
	            if (def.isCustomModel())
	            	var.model = WesterosBlocks.MOD_ID + ":block/custom/" + getModelName("base", setidx);
            	if (y[faceidx] > 0) var.y = y[faceidx];
    	        so.addVariant("facing=" + faces[faceidx], var, set.condIDs);
	        }
    	}
        this.writeBlockStateFile(def.blockName, so);
    }

    @Override
    public void doModelExports() throws IOException {
        // Export if not set to custom model
        if (!def.isCustomModel()) {
	        List<Variant> varn = new ArrayList<Variant>();
	    	// Loop over the random sets we've got
	        for (int setidx = 0; setidx < def.getRandomTextureSetCount(); setidx++) {
	        	WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setidx);
	            ModelObjectLadder mod = new ModelObjectLadder();
	            mod.textures.ladder = getTextureID(set.getTextureByIndex(0));
	        	this.writeBlockModelFile(getModelName("base", setidx), mod);
	        }
        }
        // Build simple item model that refers to block model
        ModelObject mo = new ModelObject();
        if (!def.isCustomModel())
        	mo.parent = WesterosBlocks.MOD_ID + ":block/generated/" + getModelName("base", 0);
        else
        	mo.parent = WesterosBlocks.MOD_ID + ":block/custom/" + getModelName("base", 0);  
        this.writeItemModelFile(def.blockName, mo);
        
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
    	for (String facing : FACING) {
    		oldstate.put("facing", facing);
    		newstate.put("facing", facing);
    		addWorldConverterRecord(oldID, oldstate, def.getBlockName(), newstate);
    	}
    }

}
