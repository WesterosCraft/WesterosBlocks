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

public class LeavesBlockModelExport extends ModelExport {
    private WesterosBlockDef def;

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
        public Integer y;
        public Integer weight;
    }
    // Template objects for Gson export of block models
    public static class ModelObjectLeaves {
        public String parent = "minecraft:block/block";    // Use 'block'
        public Texture textures = new Texture();
        public Elements elements[] = { new Elements() };
    }
    public static class Texture {
        public String end, side, particle;
    }
    public static class Elements {
    	int[] from = { 0, 0, 0 };
    	int[] to = { 16, 16, 16 };
    	Map<String, Faces> faces = new HashMap<String, Faces>();
    }
    public static class Faces {
    	int[] uv = { 0, 0, 16, 16 };
    	String texture;
    	Integer tintindex;
    	String cullface;
    	public Faces(String txt, String face, boolean isTinted) {
    		texture = txt;
    		cullface = face;
    		tintindex = (isTinted?0:null);
    	}
    }
    public static class ModelObject {
    	public String parent;
    }

    
    public LeavesBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
        super(blk, def, dest);
        this.def = def;
        addNLSString("block." + WesterosBlocks.MOD_ID + "." + def.blockName, def.label);
    }
    
    private String getModelName(int setidx) {
    	return def.blockName + ((setidx == 0)?"":("-v" + (setidx+1)));
    }

    @Override
    public void doBlockStateExport() throws IOException {
        List<Variant> vars = new ArrayList<Variant>();
        // Loop over the random sets we've got
        for (int setidx = 0; setidx < def.getRandomTextureSetCount(); setidx++) {
        	WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setidx);
        	String model = WesterosBlocks.MOD_ID + ":block/" + getModelName(setidx);
        	int cnt = def.rotateRandom ? 4 : 1;	// 4 for random, just 1 if not
            for (int i = 0; i < cnt; i++) {
            	Variant var = new Variant();
            	var.model = model;
            	var.weight = set.weight;
            	if (i > 0) var.y = 90*i;
            	vars.add(var);
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
    
    @Override
    public void doModelExports() throws IOException {
        boolean isTinted = def.isTinted();
        // Loop over the random sets we've got
        for (int setidx = 0; setidx < def.getRandomTextureSetCount(); setidx++) {
        	WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setidx);
        	
            ModelObjectLeaves mod = new ModelObjectLeaves();
            mod.elements[0].faces.put("down", new Faces("#end", "down", isTinted));
            mod.elements[0].faces.put("up", new Faces("#end", "up", isTinted));
            mod.elements[0].faces.put("north", new Faces("#side", "north", isTinted));
            mod.elements[0].faces.put("south", new Faces("#side", "south", isTinted));
            mod.elements[0].faces.put("west", new Faces("#side", "west", isTinted));
            mod.elements[0].faces.put("east", new Faces("#side", "east", isTinted));
            mod.textures.end = getTextureID(set.getTextureByIndex(0)); 
            mod.textures.side = getTextureID(set.getTextureByIndex(1)); 
            mod.textures.particle = mod.textures.side;
            this.writeBlockModelFile(getModelName(setidx), mod);
        }
        // Build simple item model that refers to block model
        ModelObject mo = new ModelObject();
        mo.parent = WesterosBlocks.MOD_ID + ":block/" + getModelName(0);
        this.writeItemModelFile(def.getBlockName(), mo);
        // Add tint overrides
        if (isTinted) {
            String tintres = def.getBlockColorMapResource();
            if (tintres != null) {
                ModelExport.addTintingOverride(def.getBlockName(), "", tintres);
            }
        }
    }

}
