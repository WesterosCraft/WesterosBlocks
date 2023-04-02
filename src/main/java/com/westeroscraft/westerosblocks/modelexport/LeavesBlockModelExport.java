package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlocks;
import com.westeroscraft.westerosblocks.blocks.WCLeavesBlock;

import net.minecraft.world.level.block.Block;

public class LeavesBlockModelExport extends ModelExport {
    private WCLeavesBlock blk;

    // Template objects for Gson export of block state
    // Template objects for Gson export of block state
    public static class StateObject {
        public Map<String, List<Variant>> variants = new HashMap<String, List<Variant>>();
    }
    public static class StateObject1 {
        public Map<String, Variant> variants = new HashMap<String, Variant>();
    }
    public static class Variant {
        public String model;
        public Integer x;
        public Integer y;
        public Integer weight;
    }
    // Template objects for Gson export of block models
    public static class ModelObjectLeaves {
        public String parent = "westeroscraft:block/tinted/leaves";
        public Texture textures = new Texture();
    }
    public static class Texture {
        public String end, side, bf, overlayend, overlayside, particle;
    }

    public LeavesBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
        super(blk, def, dest);
        this.blk = (WCLeavesBlock) blk;
        addNLSString("block." + WesterosBlocks.MOD_ID + "." + def.blockName, def.label);
    }
    
    protected String getModelName(String ext, int setidx) {
    	return def.blockName + "/" + ext + ("-v" + (setidx+1));
    }

    @Override
    public void doBlockStateExport() throws IOException {
        List<Variant> vars = new ArrayList<Variant>();
        // Loop over the random sets we've got
        for (int setidx = 0; setidx < def.getRandomTextureSetCount(); setidx++) {
        	WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setidx);
        	String model = WesterosBlocks.MOD_ID + ":block/generated/" + getModelName("base", setidx);
        	int cnt = def.rotateRandom ? 4 : 1;	// 4 for random, just 1 if not
            for (int i = 0; i < cnt; i++) {
            	Variant var = new Variant();
            	var.model = model;
            	var.weight = set.weight;
            	if (i > 0) var.y = 90*i;
            	vars.add(var);
            }
            if (blk.betterfoliage) {
            	model = WesterosBlocks.MOD_ID + ":block/generated/" + getModelName("base2", setidx);
                for (int i = 0; i < cnt; i++) {
                	Variant var = new Variant();
                	var.model = model;
                	var.weight = set.weight;
                	if (i > 0) var.y = 90*i;
                	vars.add(var);
                }            	
            }
        }
        if (vars.size() == 1) {
            StateObject1 so = new StateObject1();
            Variant v = vars.get(0);
            v.weight = null;
        	so.variants.put("", v);
            this.writeBlockStateFile(def.blockName, so);        	
        }
        else {
        	StateObject so = new StateObject();
        	so.variants.put("", vars);
        	this.writeBlockStateFile(def.blockName, so);
        }
    }
    
    private ModelObjectLeaves makeModel(int setidx, boolean isTinted, boolean bf, boolean overlay, int bfidx) {
    	WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setidx);
    	
        ModelObjectLeaves mod = new ModelObjectLeaves();
        mod.textures.end = getTextureID(set.getTextureByIndex(0)); 
        mod.textures.side = getTextureID(set.getTextureByIndex(1)); 
        mod.textures.particle = mod.textures.side;
        if (bf) {
    		mod.textures.bf = getTextureID(set.getTextureByIndex(2)); 
        	if (overlay) {
        		mod.parent = isTinted ? "westerosblocks:block/tinted/leaves_overlay_bf" : "westerosblocks:block/untinted/leaves_overlay_bf";
        		if (bfidx > 0) mod.parent = mod.parent + (bfidx+1);
        		mod.textures.overlayend = getTextureID(set.getTextureByIndex(3)); 
        		mod.textures.overlayside = getTextureID(set.getTextureByIndex(4)); 
        	}
        	else {
        		mod.parent = isTinted ? "westerosblocks:block/tinted/leaves_bf" : "westerosblocks:block/untinted/leaves_bf";        		
           		if (bfidx > 0) mod.parent = mod.parent + (bfidx+1);
        	}
        }
        else {
        	if (overlay) {
        		mod.parent = isTinted ? "westerosblocks:block/tinted/leaves_overlay" : "westerosblocks:block/untinted/leaves_overlay";
        		mod.textures.overlayend = getTextureID(set.getTextureByIndex(3)); 
        		mod.textures.overlayside = getTextureID(set.getTextureByIndex(4)); 
        	}
        	else {
        		mod.parent = isTinted ? "westerosblocks:block/tinted/leaves" : "westerosblocks:block/untinted/leaves";        		
        	}        	
        }
        return mod;
    }
    @Override
    public void doModelExports() throws IOException {
        boolean isTinted = def.isTinted();
        
        // Loop over the random sets we've got
        for (int setidx = 0; setidx < def.getRandomTextureSetCount(); setidx++) {
            ModelObjectLeaves mod = makeModel(setidx, isTinted, blk.betterfoliage, blk.overlay, 0);

            this.writeBlockModelFile(getModelName("base", setidx), mod);
            
            if (blk.betterfoliage) {
                mod = makeModel(setidx, isTinted, blk.betterfoliage, blk.overlay, 1);

                this.writeBlockModelFile(getModelName("base2", setidx), mod);
            	
            }
        }
        // Build base model for item
        ModelObjectLeaves mo = makeModel(0, isTinted, false, blk.overlay, 0);
        this.writeItemModelFile(def.getBlockName(), mo);
        // Add tint overrides
        if (isTinted) {
            String tintres = def.getBlockColorMapResource();
            if (tintres != null) {
                ModelExport.addTintingOverride(def.getBlockName(), "", tintres);
            }
        }
    }
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
    	oldstate.put("decayable", "false");
    	for (String check_decay : BOOLEAN) {
        	oldstate.put("check_decay", check_decay);
           	newstate.put("persistent", check_decay.equals("true") ? "false" : "true");
        	newstate.put("distance", "7");           		
            addWorldConverterRecord(oldID, oldstate, def.getBlockName(), newstate);
    	}
    }
}
