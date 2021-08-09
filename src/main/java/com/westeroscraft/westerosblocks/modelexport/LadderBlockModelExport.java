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
    private WesterosBlockDef def;

    // Template objects for Gson export of block state
    public static class StateObject {
        public Map<String, List<Variant>> variants = new HashMap<String, List<Variant>>();
    }
    public static class Variant {
        public String model;
        public Integer x;
        public Integer y;
        public Integer weight;
    }
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

    private String getModelName(String ext, int setidx) {
    	return def.blockName + ext + ((setidx == 0)?"":("-v" + (setidx+1)));
    }

    public LadderBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
        super(blk, def, dest);
        this.def = def;
        addNLSString("block." + WesterosBlocks.MOD_ID + "." + def.blockName, def.label);
    }
    
	static final String faces[] = { "north", "south", "east", "west" };
	static final int[] y = { 0, 180, 90, 270 };
	static final String up[] = { "false", "false", "true", "true" };
	static final String down[] = { "false", "true", "false", "true" };
	static final String ext[] = { "", "_down", "_up", "_both" };

    @Override
    public void doBlockStateExport() throws IOException {
        StateObject so = new StateObject();
    	for (int faceidx = 0; faceidx < 4; faceidx++) {
	        List<Variant> varn = new ArrayList<Variant>();
	    	// Loop over the random sets we've got
	        for (int setidx = 0; setidx < def.getRandomTextureSetCount(); setidx++) {
	        	WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setidx);
            	Variant var = new Variant();
	            var.model = WesterosBlocks.MOD_ID + ":block/generated/" + getModelName("", setidx);
            	var.weight = set.weight;
	            if (def.isCustomModel())
	            	var.model = WesterosBlocks.MOD_ID + ":block/custom/" + getModelName("", setidx);
            	if (y[faceidx] > 0) var.y = y[faceidx];
            	varn.add(var);
	        }
            so.variants.put("facing=" + faces[faceidx], varn);
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
	        	this.writeBlockModelFile(getModelName("", setidx), mod);
	        }
        }
        // Build simple item model that refers to block model
        ModelObject mo = new ModelObject();
        if (!def.isCustomModel())
        	mo.parent = WesterosBlocks.MOD_ID + ":block/generated/" + getModelName("", 0);
        else
        	mo.parent = WesterosBlocks.MOD_ID + ":block/custom/" + getModelName("", 0);  
        this.writeItemModelFile(def.blockName, mo);
        
    }
}
