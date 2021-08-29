package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlocks;

import net.minecraft.block.Block;

public class SolidBlockModelExport extends ModelExport {
    // Template objects for Gson export of block models
    public static class ModelObjectCubeAll {
        public String parent = "minecraft:block/cube_all";    // Use 'cube_all' model for single texture
        public TextureAll textures = new TextureAll();
    }
    public static class TextureAll {
        public String all;
    }
    // Template objects for Gson export of block models
    public static class ModelObjectCube {
        public String parent = "minecraft:block/cube";    // Use 'cube' model for multiple textures
        public Texture textures = new Texture();
    }
    public static class Texture {
        public String down, up, north, south, west, east, particle;
    }
    public static class ModelObject {
    	public String parent;
    }
    
    public SolidBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
        super(blk, def, dest);
        addNLSString("block." + WesterosBlocks.MOD_ID + "." + def.blockName, def.label);
    }
    
    protected String getModelName(String ext, int setidx) {
    	return def.blockName + "/" + ext + ("_v" + (setidx+1));
    }
    @Override
    public void doBlockStateExport() throws IOException {
    	StateObject so = new StateObject();

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
    			so.addVariant("", var, set.condIDs);	// Add our variant                	
            }
        }
    	this.writeBlockStateFile(def.blockName, so);
    }

    @Override
    public void doModelExports() throws IOException {
        Object model;
        boolean isTinted = def.isTinted();
        // Loop over the random sets we've got
        for (int setidx = 0; setidx < def.getRandomTextureSetCount(); setidx++) {
        	WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setidx);
        	if ((set.getTextureCount() > 1) || isTinted) { // More than one texture, or tinted?
        		ModelObjectCube mod = new ModelObjectCube();
        		mod.textures.down = getTextureID(set.getTextureByIndex(0));
        		mod.textures.up = getTextureID(set.getTextureByIndex(1));
        		mod.textures.north = getTextureID(set.getTextureByIndex(2));
        		mod.textures.south = getTextureID(set.getTextureByIndex(3));
        		mod.textures.west = getTextureID(set.getTextureByIndex(4));
        		mod.textures.east = getTextureID(set.getTextureByIndex(5));
        		mod.textures.particle = getTextureID(set.getTextureByIndex(2));
        		if (isTinted) {
        			mod.parent = WesterosBlocks.MOD_ID + ":block/tinted/cube";
        		}
        		model = mod;
        	}
        	else {
        		ModelObjectCubeAll mod = new ModelObjectCubeAll();
        		mod.textures.all = getTextureID(set.getTextureByIndex(0)); 
        		model = mod;
        	}
        	this.writeBlockModelFile(getModelName("base", setidx), model);
        }
        // Build simple item model that refers to block model
        ModelObject mo = new ModelObject();
        mo.parent = WesterosBlocks.MOD_ID + ":block/generated/" + getModelName("base", 0);
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
    	Map<String, String> oldmap = def.getLegacyBlockMap();
    	addWorldConverterComment(def.legacyBlockID + "(" + def.label + ")");
    	// BUild old variant map
    	Map<String, String> oldstate = new HashMap<String, String>();
    	Map<String, String> newstate = null;
    	if (def.condStates != null) {
    		newstate = new HashMap<String, String>();
    		newstate.put("cond", def.getDefaultCondID());    		
    	}
    	if (oldmap != null) {
    		for (String k : oldmap.keySet()) {
        		oldstate.put(k, oldmap.get(k));    			
    		}
    	}
        addWorldConverterRecord(oldID, oldstate, def.getBlockName(), newstate);
    }
}
